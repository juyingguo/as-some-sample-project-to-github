/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * package-level logging flag
 */

package com.demo.camera.utils;

import android.os.Build;
import android.util.Log;

public class LogUtils {

    public final static String TAG = LogUtils.class.getSimpleName();
    public final static boolean DEBUG = "eng".equals(Build.TYPE) || "userdebug".equals(Build.TYPE);

    public static void v(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }

    public static void d(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }

    public static void i(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }

    public static void w(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void w(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }

    public static void e(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.ERROR)) {
            LogUtils.e(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void e(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }

    public static void e(String message, Exception e) {
        if (DEBUG || Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, message, e);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG || Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG + "/" + tag, message, e);
        }
    }

    public static void wtf(String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.ASSERT)) {
            Log.wtf(TAG, args == null ? message : String.format(message, args));
        }
    }

    public static void wtf(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(TAG, Log.ASSERT)) {
            Log.wtf(TAG + "/" + tag, args == null ? message : String.format(message, args));
        }
    }
}
