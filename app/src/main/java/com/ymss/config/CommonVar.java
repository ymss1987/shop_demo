package com.ymss.config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ymss.tinyshop.R;

/**
 * Created by adminstrator on 2016/11/7.
 */

public class CommonVar {
    public static String getGuidPreferencesKey(Context context){
        try {
                PackageManager manager = context.getPackageManager();
                PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                String version = info.versionName;
            return "isGuidShow_"+version.replace('.','_');
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }
}
