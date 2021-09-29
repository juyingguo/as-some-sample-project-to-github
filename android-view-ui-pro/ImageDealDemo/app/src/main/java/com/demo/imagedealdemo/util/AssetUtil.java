package com.demo.imagedealdemo.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * Date:2021/9/29,15:34
 * author:jy
 */
public class AssetUtil {
    /**
     * 获取assets/tip下面子目录下的提示音乐文件列表。.
     * 调用方法：getAssetsMusicFiles("tip" + File.separator + "boot_restore_factory");
     * @param assetsPath assets 文件路径
     * @return 当前路径下文件数组
     */
    public File[] getAssetsMusicFiles(Context context, String assetsPath) {
        File[] musicFiles = null;
        try {
            String[] fileList = context.getAssets().list(assetsPath);
            if (fileList != null)
            {
                musicFiles = new File[fileList.length];
                for (int i=0;i<fileList.length;i++)
                {
                    String path = /*"file:///android_asset"  + File.separator +*/ assetsPath + File.separator + fileList[i];
                    musicFiles[i] = new File(path);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            musicFiles = null;
        }

        return musicFiles;
    }
}
