package com.aissit.otpdemo.net;


import com.aissit.otpdemo.BuildConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final long TIME_OUT = 60;
    private OkHttpClient mOkHttpClient;
    private HashMap<String, Object> mRetrofitService = new HashMap<>();

    private RetrofitManager() {
    }

    private static final class RetrofitManagerHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return RetrofitManagerHolder.INSTANCE;
    }


    public <T> T obtainRetrofitService(String baseUrl, Class<T> service) {
        T retrofitService = (T) mRetrofitService.get(service.getCanonicalName());
        if (retrofitService == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitService == null) {
                    retrofitService = createRetrofit(baseUrl).create(service);
                    mRetrofitService.put(service.getCanonicalName(), retrofitService);
                }
            }
        }
        return retrofitService;
    }

    private Retrofit createRetrofit(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new EmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        mOkHttpClient = builder
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
        return mOkHttpClient;
    }

    public void clearCache() {
        if (mOkHttpClient != null) {
            try {
                mOkHttpClient.cache().evictAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
