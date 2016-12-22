package com.ymss.utility;

/**
 * Created by adminstrator on 2016/10/27.
 */

public class LogUtil {
    public static String makeLogTag(Class cls) {
        return "ymss_" + cls.getSimpleName();
    }
}
