package com.aissit.otpdemo.net;


import android.util.Log;

import com.aissit.otpdemo.BuildConfig;


public class RetrofitHelper {
    private static final String SDK_DATAUPLOAD_DEBUG_URL = "https://ai.sandbox-abroadentropy.top/";
    private static final String SDK_DATAUPLOAD_RELEASE_URL = "https://ai.abroadentropy.top/";

    public static ApiService getRetrofitService() {
        Log.i("RetrofitHelper", "BuildConfig.DEBUG=" + BuildConfig.DEBUG);
        return RetrofitManager.getInstance().obtainRetrofitService(BuildConfig.DEBUG ? SDK_DATAUPLOAD_DEBUG_URL : SDK_DATAUPLOAD_RELEASE_URL, ApiService.class);
    }

}
