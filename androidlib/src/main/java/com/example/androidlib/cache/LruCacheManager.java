package com.example.androidlib.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 内存缓存管理类
 * 文本缓存：1/16的可用内存，图片缓存：1/8的可用内存
 * Created by liuyuhua on 2017/5/19.
 */

public class LruCacheManager {

    /**
     * 添加文本缓存
     * @param lruCache
     * @param key
     * @param value
     * @return 返回这个key对应的上一个value，若没有，则返回空
     */
    public static Object putStringToMemoryCache(LruCache<String, String> lruCache, String key, String value) {
        return lruCache.put(key, value);
    }

    /**
     * 读取文本缓存
     * @param lruCache
     * @param key
     * @return
     */
    public static String getStringFromMemoryCache(LruCache<String, String> lruCache, String key) {
        return lruCache.get(key);
    }

    /**
     * 添加图片缓存
     * @param lruCache
     * @param urlKey 以图片的url作为key
     * @param value 图片应是decode过的Bitmap
     * @return 返回这个key对应的上一个value，若没有，则返回空
     */
    public static Object putBitmapToMemoryCache(LruCache<String, Bitmap> lruCache, String urlKey, Bitmap value) {
        return lruCache.put(urlKey, value);
    }

    /**
     * 读取图片缓存
     * @param lruCache
     * @param urlKey
     * @return
     */
    public static Bitmap getBitmapFromMemoryCache(LruCache<String, Bitmap> lruCache, String urlKey) {
        return lruCache.get(urlKey);
    }
}
