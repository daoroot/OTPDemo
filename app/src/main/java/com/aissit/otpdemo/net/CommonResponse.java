package com.aissit.otpdemo.net;


import com.google.gson.annotations.SerializedName;

public class CommonResponse {
    @SerializedName("code")
    private String code;
    @SerializedName("msg")
    private String message;
    @SerializedName("data")
    private Object data;
    private boolean success;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }
}
