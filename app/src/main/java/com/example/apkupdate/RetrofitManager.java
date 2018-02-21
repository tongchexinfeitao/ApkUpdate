package com.example.apkupdate;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shadow on 2017/10/13.
 */

public class RetrofitManager {
    private Retrofit retrofit;
    public static final String BASE_URL = "http://111.205.104.180:80/bwiePush/";


    public static class SingelHolder {
        public static RetrofitManager instance = new RetrofitManager(BASE_URL);
    }


    public static RetrofitManager getInstance() {
        return SingelHolder.instance;
    }

    public RetrofitManager(String baseUrl) {
        Retrofit build = new Retrofit.Builder()
                .client(createOkHttp())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        this.retrofit = build;
    }

    public static RetrofitManager getRetrofitManager(String baseUrl) {
        return new RetrofitManager(baseUrl);
    }

    public <T> T create(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private OkHttpClient createOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }


}
