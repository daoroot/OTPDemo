package com.aissit.otpdemo.net;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * SDK数据上报
     *
     * @return
     */
    @FormUrlEncoded
    @POST("sdk-upload-data/save")
    Observable<CommonResponse> uploadUserId(@Field("deviceId") String deviceId,@Field("cooperatorPersonalMobile") String cooperatorPersonalMobile,@Field("appName") String appName);

    @FormUrlEncoded
    @POST("sdk/save-error-log")
    Observable<CommonResponse> uploaderrorInfo(@Field("sdkId") String sdkId, @Field("deviceId") String deviceId, @Field("entrustScene") String entrustScene, @Field("errorType") String errorType);

    @GET("sdk/queryBySdkId")
    Observable<CommonResponse> getMerchantAccount(@Query("sdkId") String sdkId, @Query("appName") String appName, @Query("entrustScene") String entrustScene, @Query("deviceId") String deviceId);
}
