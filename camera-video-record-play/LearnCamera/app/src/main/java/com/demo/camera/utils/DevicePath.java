package com.demo.camera.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//import android.util.Log;
/*
 * @author lizihao@actions-semi.com
 * 设备，sd,u盘等路径工具类
 */
public class DevicePath {


    private static DevicePath instance;

    private static ArrayList<String> totalDevicesList;
    private static final String TAG = DevicePath.class.getSimpleName();

    private DevicePath(Context context) {
        ///////////
        //		totalDevicesList = new ArrayList<String>();
        //		StorageManager stmg = (StorageManager) context.getSystemService(context.STORAGE_SERVICE);
        //		String[] list = stmg.getVolumePaths();//DevicePath使用了classes.jar中的方法getVolumePaths有问题
        //		for(int i = 0; i < list.length; i++)
        //		{
        //			totalDevicesList.add(list[i]);
        //		}
        ///////////

        /////////
        //totalDevicesList = (ArrayList<String>) getStoragePaths(context);
        ////////
    }

    public static DevicePath getInstance(Context context) {
        if (instance == null) {
            synchronized (DevicePath.class) {
                if (instance == null) {
                    instance = new DevicePath(context);
                }
            }
        }
        //记得每次都需要重新获取 设备路径 列表。解决u盘移除后，但数据还在。
        totalDevicesList = (ArrayList<String>) getStoragePaths(context);
        return instance;
    }

    /**
     * @param cxt
     * @return 各个设备存储路径集合
     */
    public static List<String> getStoragePaths(Context cxt) {
        ArrayList<String> pathsList = new ArrayList<String>();
		/*if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD) {
			StringBuilder sb = new StringBuilder();
			try {
				pathsList.addAll(new SdCardFetcher().getStoragePaths(new FileReader("/proc/mounts"), sb));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				File externalFolder = Environment.getExternalStorageDirectory();
				if (externalFolder != null) {
					pathsList.add(externalFolder.getAbsolutePath());
				}
			}
		} else {}*/
        StorageManager storageManager = (StorageManager) cxt.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method method = StorageManager.class.getDeclaredMethod("getVolumePaths");
            method.setAccessible(true);
            Object result = method.invoke(storageManager);
            if (result != null && result instanceof String[]) {
                String[] pathes = (String[]) result;
                StatFs statFs;
                for (String path : pathes) {
                    if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                        statFs = new StatFs(path);
                        if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
                            pathsList.add(path);

                            LogUtils.d(TAG, ">>>>>>getStoragePaths()>>>path:" + path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            File externalFolder = Environment.getExternalStorageDirectory();
            if (externalFolder != null) {
                pathsList.add(externalFolder.getAbsolutePath());
            }
        }

//		return pathsList.toArray(new String[pathsList.size()]);
        return pathsList;
    }

    public String getSdStoragePath() {
        String path = "none";
        for (int i = 0; i < totalDevicesList.size(); i++) {
            if (!totalDevicesList.get(i).equals(Environment.getExternalStorageDirectory().getPath())) {
                if (totalDevicesList.get(i).contains("sd")) {
                    path = totalDevicesList.get(i);
                    return path;
                }
            }
        }
        return path;
    }

    /**
     * 获取内置sd卡目录
     *
     * @return
     */
    public String getInterStoragePath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    @SuppressWarnings("deprecation")
    public String getUsbStoragePath(Context context) {
        String path = "";
        totalDevicesList = (ArrayList<String>) getStoragePaths(context);
        for (int i = 0; i < totalDevicesList.size(); i++) {
            if (!totalDevicesList.get(i).equals(Environment.getExternalStorageDirectory().getPath())) {
                if (totalDevicesList.get(i).contains("host")) {

                    final String tempPath = totalDevicesList.get(i);

                    //检查USB存储设备总空间大小
                    if (!TextUtils.isEmpty(tempPath) && new File(tempPath).exists()) {
                        final StatFs statFs = new StatFs(tempPath);
                        if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
                            return tempPath;
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            }
        }

        return path;
    }

    public String getSDExtStoragePath(Context context) {
        String path = "";
        totalDevicesList = (ArrayList<String>) getStoragePaths(context);
        for (int i = 0; i < totalDevicesList.size(); i++) {
            if (!totalDevicesList.get(i).equals(Environment.getExternalStorageDirectory().getPath())) {
                if (totalDevicesList.get(i).contains("sd-ext")) {

                    final String tempPath = totalDevicesList.get(i);

                    //检查USB存储设备总空间大小
                    if (!TextUtils.isEmpty(tempPath) && new File(tempPath).exists()) {
                        final StatFs statFs = new StatFs(tempPath);
                        if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
                            return tempPath;
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            }
        }

        return path;
    }

    public boolean isExistUsbStoragePath(Context context) {
        boolean resut = !TextUtils.isEmpty(getUsbStoragePath(context));
        LogUtils.d(TAG, ">>>>>>isExistUsbStoragePath()>>>:" + resut);
        return resut;
    }

    public boolean isExistSDExtStoragePath(Context context) {
        boolean resut = !TextUtils.isEmpty(getSDExtStoragePath(context));
        LogUtils.d(TAG, ">>>>>>isExistSDExtStoragePath()>>>:" + resut);
        return resut;
    }

    public boolean hasMultiplePartition(String dPath) {
        try {
            File file = new File(dPath);
            String minor = null;
            String major = null;
            for (int i = 0; i < totalDevicesList.size(); i++) {
                if (dPath.equals(totalDevicesList.get(i))) {
                    String[] list = file.list();
                    for (int j = 0; j < list.length; j++) {
                        int lst = list[j].lastIndexOf("_");
                        if (lst != -1 && lst != (list[j].length() - 1)) {
                            major = list[j].substring(0, lst);
                            minor = list[j].substring(lst + 1, list[j].length());
                            try {

                                Integer.valueOf(major);
                                Integer.valueOf(minor);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            //Log.e(TAG, "hasMultiplePartition() exception e");
            return false;
        }
    }
}