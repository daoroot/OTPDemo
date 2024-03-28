package com.aissit.otpdemo.Model;


import android.content.Context;

import com.aissit.otpdemo.net.CommonResponse;
import com.aissit.otpdemo.net.RetrofitHelper;
import com.aissit.otpdemo.utils.CommonUtil;

import io.reactivex.Observable;


public class DataModel {

    public static Observable<CommonResponse> uploadUserId(Context context, String cooperatorPersonalMobile, String appName) {
        String deviceId = CommonUtil.getUserID(context);
        return RetrofitHelper.getRetrofitService().uploadUserId(deviceId, cooperatorPersonalMobile, appName);
    }

    public static Observable<CommonResponse> uploadErrorInfo(String sdkId, String deviceId, String entrustScene, String errorType) {
        return RetrofitHelper.getRetrofitService().uploaderrorInfo(sdkId, deviceId, entrustScene, errorType);
    }

    public static Observable<CommonResponse> getMerchantAccount(String sdkId, String appName, int entrustScene, String deviceId) {
        return RetrofitHelper.getRetrofitService().getMerchantAccount(sdkId, appName, String.valueOf(entrustScene), deviceId);
    }
}
