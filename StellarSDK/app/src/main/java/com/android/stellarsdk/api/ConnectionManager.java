package com.android.stellarsdk.api;

import com.android.stellarsdk.api.model.account.AccountResponse;
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Chattip Soontaku.
 */

public class ConnectionManager {
    private OkHttpClient.Builder client = new OkHttpClient.Builder();
    private Map<String, String> mMapStr;
    private Map<String, Integer> mMapInt;
    private Map<String, Boolean> mMapBool;
    private Map<String, Double> mMapDoub;
    private Map<String, Object> mMapObj;
    private List<Integer> mList;
    private String path;
    private String token = "bearer ";
    private String mBody;
    private MultipartBody.Part multiPartBody;

    public Observable<FriendBotResponse> initGetFriendBot(String addr) {
        Observable<FriendBotResponse> observable = onConnect().getFriendBot(addr);

        return observable;
    }

    public Observable<AccountResponse> initGetAccount() {
        Observable<AccountResponse> observable = onConnect().getAccount(path);

        return observable;
    }

    public ConnectionManager(Builder builder) {
        super();
        mMapStr = builder.mMapStr;
        path = builder.path;
        mList = builder.mList;
        mMapInt = builder.mMapInt;
        mMapBool = builder.mMapBool;
        mMapDoub = builder.mMapDoub;
        mMapObj = builder.mMapObj;
        token += builder.token;
        multiPartBody = builder.multiPartBody;
    }

    private ApiListener onConnect() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);

        client.readTimeout(30, TimeUnit.SECONDS);
        client.connectTimeout(30, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(ApiConstant.sBaseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(ApiListener.class);
    }

    public static class Builder {
        private Map<String, String> mMapStr;
        private Map<String, Integer> mMapInt;
        private Map<String, Boolean> mMapBool;
        private Map<String, Double> mMapDoub;
        private Map<String, Object> mMapObj;
        private List<Integer> mList;
        private String path;
        private String token;
        private MultipartBody.Part multiPartBody;

        public Builder setMappingStr(Map<String, String> map) {
            mMapStr = map;
            return this;
        }

        public Builder setMappingInt(Map<String, Integer> map) {
            mMapInt = map;
            return this;
        }

        public Builder setMappingBool(Map<String, Boolean> map) {
            mMapBool = map;
            return this;
        }

        public Builder setMappingDoub(Map<String, Double> map) {
            mMapDoub = map;
            return this;
        }

        public Builder setList(List<Integer> list) {
            mList = list;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setMultipartBody(MultipartBody.Part body) {
            this.multiPartBody = body;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setMappingObj(Map<String, Object> mMapObj) {
            this.mMapObj = mMapObj;
            return this;
        }

        public ConnectionManager build() {
            return new ConnectionManager(this);
        }
    }
}
