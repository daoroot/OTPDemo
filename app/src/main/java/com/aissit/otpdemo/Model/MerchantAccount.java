package com.aissit.otpdemo.Model;

public class MerchantAccount {

    /**
     * 商户账号
     */
    private String cooperatorPersonalMobile;
    /**
     * app名称
     */
    private String appName;
    /**
     * 开场白
     */
    private String mOpenRemark;

    /**
     * 授权时长
     */
    private int authTime;
    /**
     * 授权页面提示语
     */
    private String authPrompt;

    /**
     * 频繁启动提示语
     */
    private String callPrompt;

    public String getCooperatorPersonalMobile() {
        return cooperatorPersonalMobile;
    }

    public void setCooperatorPersonalMobile(String cooperatorPersonalMobile) {
        this.cooperatorPersonalMobile = cooperatorPersonalMobile;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOpenRemark() {
        return mOpenRemark;
    }

    public void setOpenRemark(String openRemark) {
        mOpenRemark = openRemark;
    }

    public int getAuthTime() {
        return authTime;
    }

    public void setAuthTime(int authTime) {
        this.authTime = authTime;
    }

    public String getAuthPrompt() {
        return authPrompt;
    }

    public void setAuthPrompt(String authPrompt) {
        this.authPrompt = authPrompt;
    }

    public String getCallPrompt() {
        return callPrompt;
    }

    public void setCallPrompt(String callPrompt) {
        this.callPrompt = callPrompt;
    }


}
