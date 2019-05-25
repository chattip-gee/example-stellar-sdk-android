package com.android.stellarsdk.api.remote

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.android.stellarsdk.api.callback.OnResponse
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
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