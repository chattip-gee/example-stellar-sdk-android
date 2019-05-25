package com.android.stellarsdk.api

import com.android.stellarsdk.api.model.account.AccountResponse
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse
import com.android.stellarsdk.api.model.transaction.TransactionResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import java.util.concurrent.TimeUnit

const val DEFAULT_FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ss"

class ConnectionManager {
    private val client = OkHttpClient.Builder()
    private var token = "bearer "

    fun initGetFriendBot(addr: String): Observable<FriendBotResponse> {
        return onConnect().getFriendBot(addr)
    }

    fun initGetAccounts(accountId: String): Observable<AccountResponse> {
        return onConnect().getAccounts(accountId)
    }

    fun initGetTransactions(hash: String): Observable<TransactionResponse> {
        return onConnect().getTransactions(hash)
    }

    private fun onConnect(): ApiListener {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(interceptor)

        client.readTimeout(30, TimeUnit.SECONDS)
        client.connectTimeout(30, TimeUnit.SECONDS)

        val gson = GsonBuilder()
            .setDateFormat(DEFAULT_FORMAT_DATE)
            .create()

        val retrofit = Retrofit.Builder()
            .client(client.build())
            .baseUrl(ApiConstant.sBaseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        return retrofit.create(ApiListener::class.java)
    }

}