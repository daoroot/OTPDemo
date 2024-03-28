package com.aissit.otpdemo.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

public class CommonUtil {

    public static String getUserID(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            UUID uuid = UUID.randomUUID();
            androidId = uuid.toString();
        }
        return androidId;
    }
}
