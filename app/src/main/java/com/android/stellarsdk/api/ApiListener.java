package com.android.stellarsdk.api;

import com.android.stellarsdk.api.model.account.AccountResponse;
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse;
import com.android.stellarsdk.api.model.transaction.TransactionResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Chattip Soontaku.
 */

public interface ApiListener {
    @GET("https://friendbot.stellar.org/")
    Observable<FriendBotResponse> getFriendBot(@Query("addr") String addr);

    @GET("https://horizon-testnet.stellar.org/accounts/{account_id}")
    Observable<AccountResponse> getAccounts(@Path("account_id") String accountId);

    @GET("https://horizon-testnet.stellar.org/transactions/{hash}")
    Observable<TransactionResponse> getTransactions(@Path("hash") String hash);
}