package com.phuket.tour.audioplayer.util;

import android.util.Log;

/**
 * modify by
 */
public class LogUtil {

    /**
	 * model true:debug,release should false.
	 */
	private static final boolean FLAG = true;

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