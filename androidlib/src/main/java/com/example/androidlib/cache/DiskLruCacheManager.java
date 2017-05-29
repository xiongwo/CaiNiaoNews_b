package com.example.androidlib.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 磁盘缓存管理类
 * 根目录应是：/sdcard/Android/data/<application package>/cache
 * 文本数据：根目录/string
 * 图片数据：根目录/bitmap
 *
 * 用法：
 * 1、利用getDiskCacheDir和getAppVersion来辅助open DisLruCache
 * 2、当写入图片缓存时，利用getHashKeyForDiskCache来辅助写入
 * Created by liuyuhua on 2017/5/19.
 */

public class DiskLruCacheManager {

//------------------------------------------辅助open-------------------------------------------------------
    /**
     * 获取磁盘缓存的目录
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            // getPath() 提示：是因为 getExternalCacheDir() 当sd卡未安装好，就会返回null；这里已判断sd卡的状态
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

//------------------------------------------辅助写入-------------------------------------------------------
    /**
     * 将图片或者是数据接口的url进行MD5编码，使其唯一且符合文件的命名规则
     * @param key
     * @return
     */
    public static String getHashKeyForDiskCache(String key) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();

//        for (int i = 0; i < bytes.length; i++) {
//            String hex = Integer.toHexString(0xFF & bytes[i]);
//            if (hex.length() == 1) {
//                stringBuilder.append('0');
//            }
//            stringBuilder.append(hex);
//        }

        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b); // '0' to '9' and 'a' to 'f'
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }

        return stringBuilder.toString();
    }

    //------------------------------------------辅助读取-------------------------------------------------------

    /**
     * 从传入的DiskLruCache.Snapshot中读取数据，并转换为String，应异步操作
     * @param snapshot
     * @return
     */
    public static String getStringFromSnapshot(DiskLruCache.Snapshot snapshot) {
        String cache = null;
        if (snapshot != null) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(snapshot.getInputStream(0));
                StringBuilder builder = new StringBuilder();
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = inputStream.read(buffer)) != -1) {
                    builder.append(new String(buffer, 0, byteCount));
                }
                cache = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cache;
    }
}
