package com.example.androidlib.utils;

import android.content.Context;
import android.util.Log;

/**
 * 判断该接口的磁盘缓存是否已经过期
 * Created by liuyuhua on 2017/5/20.
 */

public class InterfaceDiskCache {

    /**
     *
     * @param context
     * @param sharedName
     * @param jsonTimeKey
     * @param cacheTime
     * @return 当读取时间和写入时间的差值大于设置的缓存存在时间，就返回true；如果结果是小于等于，或者写
     *          入时间为0，即没有写入缓存，就返回false
     */
    public static boolean isCacheOutOfDate(Context context, String sharedName, String jsonTimeKey, int cacheTime) {
        SharedPreferencesHelper helper= SharedPreferencesHelper.getInstance(context, sharedName);
        long lastWriteTime = helper.getLongValue(jsonTimeKey);
        Log.d("isCacheOutOfDate", "lastWriteTime: " + lastWriteTime);
        if (lastWriteTime == 0) {
            // 说明没有缓存
            return false;
        } else {
            long currentTime = System.currentTimeMillis();
            int intervalTime = (int) ((currentTime - lastWriteTime) / 1000 / 60 / 60);
            return intervalTime > cacheTime;
        }
    }
}
