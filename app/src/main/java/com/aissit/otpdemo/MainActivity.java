package com.aissit.otpdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.aissit.otpdemo.Model.DataModel;
import com.aissit.otpdemo.Model.MerchantAccount;
import com.aissit.otpdemo.net.CommonResponse;
import com.aissit.otpdemo.utils.CommonUtil;
import com.aissit.otpdemo.utils.UpdateDialog;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceId = CommonUtil.getUserID(this);
        mUpdateDialog = new UpdateDialog(this);
    }

    private UpdateDialog mUpdateDialog;
    private static final int BUSINESS_SCENE_VERIFICATION_CODE = 3;
    private final int SUCCESS_START_MIDDLE_PAGE = 7;//展示商户验证码不带取消按钮中间页
    private final int SUCCESS_START_MIDDLE_PAGE_CLOSE = 8;//展示商户验证码带取消按钮中间页
    public static final int ERROR_FAILED_START_WHATS_APP = 1;//调起WHATSAPP失败
    private final int SUCESS_START_WHATS_APP = 2;//调起WHATSAPP成功
    private final int START_MIDDLE_PAGE_CLOSE = 9;//商户验证码客户点击取消按钮
    public static final int ERROR_FAILED_GET_APP_NAME = 0;//获取商户账号和APP名称失败
    private String sdkId = "ba2562372cd3428abb26874f1438a17d", appName = "OTPDemo";
    private String deviceId;


    private String mOpenRemark;

    public void startOtp(View view) {
        int entrustScene = BUSINESS_SCENE_VERIFICATION_CODE;
        DataModel.getMerchantAccount(sdkId, appName, entrustScene, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonResponse>() {
                    @Override
                    public void accept(CommonResponse commonResponse) throws Exception {
                        String code = commonResponse.getCode();
                        if ("2000".equals(code)) {
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) commonResponse.getData();
                            MerchantAccount merchantAccount = new MerchantAccount();
                            String cooperatorPersonalMobile = (String) linkedTreeMap.get("cooperatorPersonalMobile");
                            String appName = (String) linkedTreeMap.get("appName");
                            mOpenRemark = (String) linkedTreeMap.get("openRemark");

                            merchantAccount.setCooperatorPersonalMobile(cooperatorPersonalMobile);
                            merchantAccount.setAppName(appName);
                            Object jp = linkedTreeMap.get("jumpPath");
                            int jumpPath;//跳转路径,1(直接跳转),2(跳转至提示中间页（不带取消按钮）),3(跳转至提示中间页带取消按钮)
                            try {
                                if (jp instanceof Double) {
                                    jumpPath = ((Double) jp).intValue();
                                } else {
                                    jumpPath = Integer.parseInt(jp.toString());
                                }
                            } catch (Exception e) {
                                jumpPath = 0;
                            }
                            if (jumpPath > 1) {
                                boolean hideClose = jumpPath == 2;
                                uploadErrorInfo(sdkId, deviceId, String.valueOf(entrustScene), String.valueOf(hideClose ? SUCCESS_START_MIDDLE_PAGE : SUCCESS_START_MIDDLE_PAGE_CLOSE));
                                String prompt = (String) linkedTreeMap.get("authPrompt");
                                mUpdateDialog.showAndDismissDelay(prompt, hideClose, new UpdateDialog.OnItemClickListener() {
                                    @Override
                                    public void onConfirmUpdate() {
                                        startVerificationCode(sdkId, merchantAccount.getCooperatorPersonalMobile(), merchantAccount.getAppName());
                                    }

                                    @Override
                                    public void onCancelUpdate() {
                                        mUpdateDialog.dismiss();
                                        uploadErrorInfo(sdkId, deviceId, String.valueOf(entrustScene), String.valueOf(START_MIDDLE_PAGE_CLOSE));
                                    }
                                });
                            } else {
                                startVerificationCode(sdkId, merchantAccount.getCooperatorPersonalMobile(), merchantAccount.getAppName());
                            }
                        } else {
                            Log.e(TAG, "Merchant account query failed.");
                            uploadErrorInfo(sdkId, deviceId, String.valueOf(entrustScene), String.valueOf(ERROR_FAILED_GET_APP_NAME));
                        }
                    }
                }, throwable -> throwable.printStackTrace());
    }

    private void startVerificationCode(String sdkId, String phone, String appName) {
        String userID = CommonUtil.getUserID(this);
        long timecurrentTimeMillis = System.currentTimeMillis();
        String token = userID + String.valueOf(timecurrentTimeMillis);
        String openRemark = "";
        if (!TextUtils.isEmpty(mOpenRemark)) {
            openRemark = mOpenRemark.replace("{{tokenId}}", token).replace("{{appName}}", appName);
        }
        Log.d(TAG, "唤起传参：phone=" + phone + ",mark=" + openRemark);
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + phone + "?text=" + openRemark));
            intent.setPackage("com.whatsapp");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            uploadErrorInfo(sdkId, CommonUtil.getUserID(this), String.valueOf(BUSINESS_SCENE_VERIFICATION_CODE), String.valueOf(SUCESS_START_WHATS_APP));
        } catch (Exception exception) {
            uploadErrorInfo(sdkId, CommonUtil.getUserID(this), String.valueOf(BUSINESS_SCENE_VERIFICATION_CODE), String.valueOf(ERROR_FAILED_START_WHATS_APP));
            Log.d(TAG, exception.toString());
        }
    }

    private void uploadErrorInfo(String sdkId, String deviceId, String entrustScene, String errorType) {

        DataModel.uploadErrorInfo(sdkId, deviceId, entrustScene, errorType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CommonResponse>() {
                    @Override
                    public void accept(CommonResponse commonResponse) throws Exception {
                        if (commonResponse == null) {
                            return;
                        }
                        String code = commonResponse.getCode();
                        Log.e(TAG, "uploadErrorInfo.code=" + code);
                        if ("2000".equals(code)) {
                            Log.e(TAG, "uploadErrorInfo.sucess");
                        } else {
                            Log.e(TAG, "uploadErrorInfo.failed");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }
}