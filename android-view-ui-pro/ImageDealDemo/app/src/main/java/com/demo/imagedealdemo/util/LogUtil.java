package com.demo.imagedealdemo.util;

import android.util.Log;

/**
 * modiy by
 */
public class LogUtil {

	private static final String KEY = "LogUtil";
	/**
	 * model true:debug,release should false.
	 */
	public static final boolean FLAG = true;

	public static void i(Object message) {
		if (FLAG) {
			Log.i(KEY, message.toString());
		}
	}

	public static void e(Object message) {
		if (FLAG) {
			Log.e(KEY, message.toString());
		}
	}
	
	public static void d(Object message) {
		if (FLAG) {
			Log.d(KEY, message.toString());
		}
	}
	
	public static void w(Object message) {
		if (FLAG) {
			Log.w(KEY, message.toString());
		}
	}

	public static void w(Object message, Throwable tr) {
		if (FLAG) {
			Log.w(KEY, message.toString(), tr);
		}
	}

	public static void i(Object obj, Object message) {
		if (FLAG) {
            if (obj instanceof String) {
                Log.i((String)obj, message.toString());
                return;
            }
			Log.i(obj.getClass().getSimpleName(), message.toString());
		}
	}

	public static void e(Object obj, Object message) {
		if (FLAG) {
            if (obj instanceof String) {
                Log.e((String)obj, message.toString());
                return;
            }
			Log.e(obj.getClass().getSimpleName(), message.toString());
		}
	}

	public static void d(Object obj, Object message) {
		if (FLAG) {
		    if (obj instanceof String) {
		        Log.d((String)obj, message.toString());
		        return;
		    }
			Log.d(obj.getClass().getSimpleName(), message.toString());
		}
	}

	public static void w(Object obj, Object message) {
		if (FLAG) {
            if (obj instanceof String) {
                Log.w((String)obj, message.toString());
                return;
            }
			Log.w(obj.getClass().getSimpleName(), message.toString());
		}
	}

}