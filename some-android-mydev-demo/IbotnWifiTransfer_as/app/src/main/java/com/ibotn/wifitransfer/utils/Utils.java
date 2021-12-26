package com.ibotn.wifitransfer.utils;

import android.os.Build;

import java.text.DecimalFormat;

public class Utils {
    public static String getSerial(){
        String tmp = Build.SERIAL;
        if(tmp == null || tmp.length() < 13) {
            return null;
        }
        return tmp.substring(0, 13);
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0 MB";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
