package com.android.stellarsdk.api.remote

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.android.stellarsdk.api.callback.OnResponse
import org.stellar.sdk.*
import org.stellar.sdk.KeyPair.fromAccountId
import org.stellar.sdk.Transaction.Builder.TIMEOUT_INFINITE
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import shadow.okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


object Horizon : HorizonTasks {
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

    fun doSendMoney(
        destAddress: String,
        secretSeed: CharArray,
        memo: String,
        amount: String,
        listener: OnResponse<SubmitTransactionResponse>
    ) {
        loadSendMoney(destAddress, secretSeed, memo, amount, listener)
    }

    private fun loadSendMoney(
        destAddress: String,
        secretSeed: CharArray,
        memo: String,
        amount: String,
        listener: OnResponse<SubmitTransactionResponse>
    ) {
        AsyncTask.execute {
            val server = getServer()
            val sourceKeyPair = KeyPair.fromSecretSeed(secretSeed)
            val destKeyPair = fromAccountId(destAddress)

            val accountResponse = server.accounts().account(sourceKeyPair)

            val transaction = Transaction.Builder(accountResponse)
                .setTimeout(TIMEOUT_INFINITE)
                .setOperationFee(100)
                .addOperation(PaymentOperation.Builder(destKeyPair, AssetTypeNative(), amount).build())
                .addMemo(Memo.text(memo))
                .build()
            transaction.sign(sourceKeyPair)

            try {
                val transactionResponse = server.submitTransaction(transaction)
                Handler(Looper.getMainLooper()).post {
                    if (transactionResponse.isSuccess) listener.onSuccess(transactionResponse)
                    else listener.onError("Address identified for : " + destKeyPair.accountId)
                }

            } catch (error: Exception) {
                error.message?.let {
                    listener.onError(it)
                } ?: run {
                    listener.onError("Transaction Failed")
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
                    listener.onError("fail to get account")
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