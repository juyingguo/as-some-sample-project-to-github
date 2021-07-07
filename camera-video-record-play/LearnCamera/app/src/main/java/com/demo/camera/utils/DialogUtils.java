package com.demo.camera.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.demo.camera.R;


public class DialogUtils {
    public static final int TOAST_SHOW_TIMES = 8000;
    private static AlertDialog sDialog;

    public static String getDesc(Context context, String name) {
        String FunctionName = null;
        if (name.equals("com.demo.ibotnphone")) {
            FunctionName = context.getString(R.string.video_call);
        } else if (name.equals("com.demo.ibotnface")) {
            FunctionName = context.getString(R.string.face_recognize);
        } else if (name.equals("com.demo.ibotncamera")) {
            FunctionName = context.getString(R.string.video_record);
        }

        return FunctionName;
    }

    public interface OkClickListener {
        void okClick();
    }

    public static void showErrorDialog(Context context, String message, final OkClickListener okClickListener) {
        if(!isValidContext(context))
            return ;
        sDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.connect_camera_error))
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int arg1) {
                        sDialog.dismiss();
                        okClickListener.okClick();
                    }
                })
                .create();
        //sDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        sDialog.show();
    }

    private static boolean isValidContext(Context c) {
        Activity activity = (Activity)c;
        if (activity.isFinishing() || activity.isDestroyed())
            return false;
        else
            return true;

    }
}
