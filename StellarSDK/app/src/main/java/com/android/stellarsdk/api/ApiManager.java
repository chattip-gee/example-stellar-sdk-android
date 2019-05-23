package com.android.stellarsdk.api;

import com.android.stellarsdk.api.callback.AccountCallback;
import com.android.stellarsdk.api.callback.BaseCallback;
import com.android.stellarsdk.api.model.account.AccountResponse;
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {

    public void getFriendBot(String addr, final BaseCallback baseCallback) {
        Observable<FriendBotResponse> observable = new ConnectionManager.Builder()
                .build()
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
                        baseCallback.onApiError(e.getMessage());
                    }

                    @Override
                    public void onNext(FriendBotResponse response) {
                        baseCallback.onApiSuccess("SUCCESS! You have a new account :)\n" + response.getLinks().getTransaction().getHref());
                    }
                });
    }

    public void getAccount(String accountId, final AccountCallback accountCallback) {
        Observable<AccountResponse> observable = new ConnectionManager.Builder()
                .setPath(accountId)
                .build()
                .initGetAccount();

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AccountResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        accountCallback.onApiError(e.getMessage());
                    }

                    @Override
                    public void onNext(AccountResponse accountResponse) {
                        accountCallback.onApiSuccess(accountResponse);
                    }
                });
    }
}
