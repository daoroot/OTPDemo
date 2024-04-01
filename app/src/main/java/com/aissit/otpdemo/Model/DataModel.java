package com.aissit.otpdemo.Model;


import com.aissit.otpdemo.net.CommonResponse;
import com.aissit.otpdemo.net.RetrofitHelper;

import io.reactivex.Observable;


public class DataModel {

    public static Observable<CommonResponse> uploadErrorInfo(String sdkId, String deviceId, String entrustScene, String errorType) {
        return RetrofitHelper.getRetrofitService().uploaderrorInfo(sdkId, deviceId, entrustScene, errorType);
    }

    public static Observable<CommonResponse> getMerchantAccount(String sdkId, String appName, int entrustScene, String deviceId) {
        return RetrofitHelper.getRetrofitService().getMerchantAccount(sdkId, appName, String.valueOf(entrustScene), deviceId);
    }
}
