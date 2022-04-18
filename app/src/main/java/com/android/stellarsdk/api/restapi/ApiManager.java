package com.android.stellarsdk.api.restapi;

import com.android.stellarsdk.api.callback.OnResponse;
import com.android.stellarsdk.api.model.account.AccountResponse;
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse;
import com.android.stellarsdk.api.model.transaction.TransactionResponse;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {

    public void getFriendBot(String addr, final OnResponse<FriendBotResponse> callback) {
        Observable<FriendBotResponse> observable = new ConnectionManager()
                .initGetFriendBot(addr);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FriendBotResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(FriendBotResponse response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public void getAccounts(String accountId, final OnResponse<AccountResponse> callback) {
        Observable<AccountResponse> observable = new ConnectionManager()
                .initGetAccounts(accountId);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(AccountResponse response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public void getTransaction(String hash, final OnResponse<TransactionResponse> callback) {
        Observable<TransactionResponse> observable = new ConnectionManager()
                .initGetTransactions(hash);

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TransactionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(TransactionResponse response) {
                        callback.onSuccess(response);
                    }
                });
    }
}
