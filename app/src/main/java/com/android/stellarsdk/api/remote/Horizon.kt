package com.android.stellarsdk.api.remote

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.android.stellarsdk.api.callback.OnResponse
import org.stellar.sdk.*
import org.stellar.sdk.Asset.createNonNativeAsset
import org.stellar.sdk.KeyPair.fromAccountId
import org.stellar.sdk.Transaction.Builder.TIMEOUT_INFINITE
import org.stellar.sdk.requests.EventListener
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import shadow.okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.TimeUnit


object Horizon : HorizonTasks {
    //TODO REFACTOR
    private lateinit var HORIZON_SERVER: Server

    override fun init(server: ServerType) {
        var serverAddress = ""
        when (server) {
            ServerType.PROD -> {
                serverAddress = "https://horizon.stellar.org"
                Network.usePublicNetwork()
            }
            ServerType.TEST_NET -> {
                serverAddress = "https://horizon-testnet.stellar.org"
                Network.useTestNetwork()
            }
        }
        HORIZON_SERVER = createServer(serverAddress)
    }

    fun sendMoney(transactionItem: TransactionItem, listener: OnResponse<SubmitTransactionResponse>) {
        loadSendMoney(transactionItem, listener)
    }

    fun addAsset(addAssetItem: AddAssetItem, listener: OnResponse<SubmitTransactionResponse>) {
        signAsset(addAssetItem, listener)
    }

    private fun signAsset(addAssetItem: AddAssetItem, listener: OnResponse<SubmitTransactionResponse>) {
        AsyncTask.execute {
            val server = getServer()
            val issuer = KeyPair.fromSecretSeed(addAssetItem.secretKey)
            val secretReceiver = KeyPair.fromSecretSeed("SBSNXDR3YV2S5QVB25ABYISKDNADI6X3Z23FY7M4RR5W64HEXP5MJJ22")
            val asset = createNonNativeAsset(addAssetItem.assetName, issuer)

            val accountResponse = server.accounts().account(secretReceiver)
            val allowAsset = Transaction.Builder(accountResponse)
                    .setTimeout(TIMEOUT_INFINITE)
                    .setOperationFee(100)
                    .addOperation(ChangeTrustOperation.Builder(asset, addAssetItem.limit).build())
                    .build()
            allowAsset.sign(secretReceiver)

            try {
                val assetResponse = server.submitTransaction(allowAsset)
                Handler(Looper.getMainLooper()).post {
                    if (assetResponse.isSuccess) listener.onSuccess(assetResponse)
                    else listener.onError("Identified for : ${addAssetItem.assetName}")
                }

            } catch (error: Exception) {
                error.message?.let { listener.onError(it) }
                        ?: run { listener.onError("Something went wrong") }
            }
        }
    }

    private fun loadSendMoney(transactionItem: TransactionItem, listener: OnResponse<SubmitTransactionResponse>) {
        AsyncTask.execute {
            val server = getServer()
            val issuer = KeyPair.fromSecretSeed(transactionItem.secretKey)
            val destination = fromAccountId(transactionItem.destination)

            val accountResponse = server.accounts().account(issuer)

            val transactionBuilder = Transaction.Builder(accountResponse).setTimeout(TIMEOUT_INFINITE)

            if (transactionItem.assetName.isNullOrEmpty()) {
                transactionBuilder.addOperation(PaymentOperation.Builder(destination, AssetTypeNative(), transactionItem.amount).build())
            } else {
                val asset = createNonNativeAsset(transactionItem.assetName, issuer)
                transactionBuilder.addOperation(PaymentOperation.Builder(destination, asset, transactionItem.amount).build())
            }

            transactionBuilder.setOperationFee(100)
            transactionBuilder.addMemo(Memo.text(transactionItem.memo))

            val transaction = transactionBuilder.build()
            transaction.sign(issuer)

            try {
                val transactionResponse = server.submitTransaction(transaction)
                Handler(Looper.getMainLooper()).post {
                    if (transactionResponse.isSuccess) listener.onSuccess(transactionResponse)
                    else listener.onError("Address identified for : " + destination.accountId)
                }

            } catch (error: Exception) {
                error.message?.let {
                    listener.onError(it)
                } ?: run {
                    listener.onError("Something went wrong")
                }

            }
        }
    }

    fun doReceiveMoney(accountId: String, listener: OnResponse<ReceiverResponse>) {
        loadReceiveMoney(accountId, listener)
    }

    private fun loadReceiveMoney(accountId: String, listener: OnResponse<ReceiverResponse>) {
        AsyncTask.execute {
            val server = getServer()
            val account = fromAccountId(accountId)

            val paymentsRequest = server.payments().forAccount(account)

            paymentsRequest.stream(EventListener { payment ->
                if (payment is PaymentOperationResponse) {
                    if (payment.to == account) {
                        return@EventListener
                    }

                    val amount = payment.amount

                    val asset = payment.asset
                    val assetName = if (asset == AssetTypeNative()) {
                        "lumens"
                    } else {
                        val assetNameBuilder = StringBuilder()
                        assetNameBuilder.append((asset as AssetTypeCreditAlphaNum).code)
                        assetNameBuilder.append(":")
                        assetNameBuilder.append(asset.issuer.accountId)
                        assetNameBuilder.toString()
                    }

                    val result = ReceiverResponse(amount, assetName, payment.from.accountId)

                    Handler(Looper.getMainLooper()).post {
                        listener.onSuccess(result)
                    }
                }
            })

            try {
                System.`in`.read()
            } catch (e: IOException) {
                e.message?.let {
                    listener.onError(it)
                } ?: run {
                    listener.onError("Something went wrong")
                }
            }
        }
    }

    fun getBalance(keyPair: KeyPair, listener: OnResponse<AccountResponse>) {
        loadBalances(keyPair, listener)
    }

    private fun loadBalances(keyPair: KeyPair, listener: OnResponse<AccountResponse>) {
        AsyncTask.execute {
            val server = getServer()
            try {
                val response = server.accounts().account(keyPair)
                Handler(Looper.getMainLooper()).post {
                    listener.onSuccess(response)
                }
            } catch (error: Exception) {
                error.message?.let {
                    listener.onError(it)
                } ?: run {
                    listener.onError("Something went wrong")
                }

            }
        }
    }

    /**
     * HORIZON_SUBMIT_TIMEOUT is a time in seconds after Horizon sends a timeout response
     * after internal txsub timeout.
     */
    private const val HORIZON_SUBMIT_TIMEOUT = 60L

    private fun getServer(): Server {
        checkNotNull(
                HORIZON_SERVER,
                lazyMessage = { "Horizon server has not been initialized, please call {${this::class.java}#init(..)" })
        return HORIZON_SERVER
    }

    private fun createServer(serverAddress: String): Server {
        val server = Server(serverAddress)

        val httpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(ShadowedStethoInterceptor())
                .build()

        val submitHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(HORIZON_SUBMIT_TIMEOUT + 5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(ShadowedStethoInterceptor())
                .build()

        server.httpClient = httpClient
        server.submitHttpClient = submitHttpClient

        return server
    }
}

data class ReceiverResponse(val amount: String, val assetName: String, val accountId: String)
data class TransactionItem(val destination: String, val secretKey: CharArray, val memo: String, val amount: String, val assetName: String?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TransactionItem

        if (!secretKey.contentEquals(other.secretKey)) return false

        return true
    }

    override fun hashCode(): Int {
        return secretKey.contentHashCode()
    }
}

class AddAssetItem(val secretKey: CharArray, val assetName: String, val limit: String)