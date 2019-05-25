package com.android.stellarsdk.api.remote

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.android.stellarsdk.api.callback.OnResponse
import org.stellar.sdk.*
import org.stellar.sdk.KeyPair.fromAccountId
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

    fun sendMoney(keyPair: KeyPair, listener: OnResponse<SubmitTransactionResponse>) {
        LoadSendMoney(keyPair, listener)
    }

    private fun LoadSendMoney(keyPair: KeyPair, listener: OnResponse<SubmitTransactionResponse>) {
        AsyncTask.execute {
            val server = getServer()
            try {
                val source = KeyPair.fromSecretSeed(keyPair.secretSeed)
                val destination = fromAccountId("GC3RDG2BYV6CM77X663K72G34EYHYJVDPD7SFXKKFLZOFQM3UJTW5NHG")

                val accountResponse = server.accounts().account(keyPair)
                val transaction = Transaction.Builder(accountResponse)
                    .setTimeout(1000)
                    .addOperation(PaymentOperation.Builder(destination, AssetTypeNative(), "10").build())
                    .addMemo(Memo.text("Test Transaction"))
                    .build()
                transaction.sign(source)

                try {
                    val transactionResponse = server.submitTransaction(transaction)
                    Log.d("ComeHere ", "Success")
                    Handler(Looper.getMainLooper()).post {
                        listener.onSuccess(transactionResponse)
                    }
                } catch (e: Exception) {
                    Log.d("ComeHere ", "fail " + e.message)
                    // If the result is unknown (no response body, timeout etc.) we simply resubmit
                    // already built transaction:
                    // SubmitTransactionResponse response = server.submitTransaction(transaction);
                }


            } catch (error: Exception) {
                error.message?.let {
                    listener.onError(it)
                } ?: run {
                    listener.onError("fail to send money")
                }

            }
        }
    }

    fun getBalance(keyPair: KeyPair, listener: OnResponse<AccountResponse>) {
        LoadBalances(keyPair, listener)
    }

    private fun LoadBalances(keyPair: KeyPair, listener: OnResponse<AccountResponse>) {
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