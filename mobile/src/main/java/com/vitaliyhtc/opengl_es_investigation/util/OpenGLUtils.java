package com.vitaliyhtc.opengl_es_investigation.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;

/**
 * Created by vitaliyhtc on 14.08.17.
 */

public class OpenGLUtils {
    private static final String TAG = "OpenGLUtils";


    public static int getReqGlEsVersion(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        Log.e(TAG, "reqGlEsVersion: " + configurationInfo.reqGlEsVersion + "; " +
                "getGlEsVersion: " + configurationInfo.getGlEsVersion() + ";");
        return configurationInfo.reqGlEsVersion;
    }

    public static boolean supportES2(Context context) {
        return (getReqGlEsVersion(context) >= 0x20000);
    }
}
