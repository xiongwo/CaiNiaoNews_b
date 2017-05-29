package com.example.androidlib.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 计算内存、外存的大小
 * Created by liuyuhua on 2017/5/10.
 */

public class CalculateMemory {

    private static final int ERROR = -1;
    private static final int FLAG_AVAILABLE = 0; // 标志，计算可用
    private static final int FLAG_TOTAL = 1; // 标志，计算整体

    // 判断SD卡是否可用
    public static boolean IsExternalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // 获取SD卡可用的空间大小，单位为 byte
    public static long getAvailableExternalMemorySize() {
        if (IsExternalMemoryAvailable()) {
            File directory = Environment.getExternalStorageDirectory();
            return calculateMemorySize(directory, FLAG_AVAILABLE);
        } else {
            return ERROR;
        }
    }

    // 获取SD卡整体的空间大小，单位为 byte
    public static long getTotalExternalMemorySize() {
        if (IsExternalMemoryAvailable()) {
            File directory = Environment.getExternalStorageDirectory();
            return calculateMemorySize(directory, FLAG_TOTAL);
        } else {
            return ERROR;
        }
    }

    // 获取内部存储器可用的空间大小，单位为 byte
    public static long getAvailableInternalMemorySize() {
        File directory = Environment.getDataDirectory();
        return calculateMemorySize(directory, FLAG_AVAILABLE);
    }

    // 获取内部存储器整体的空间大小，单位为 byte
    public static long getTotalInternalMemorySize() {
        File directory = Environment.getDataDirectory();
        return calculateMemorySize(directory, FLAG_TOTAL);
    }

    @TargetApi(18)
    private static long calculateMemorySize(File directory, int flag) {
        StatFs statFs = new StatFs(directory.getPath());

        long blockSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong(); // 每个 block 的大小
        } else {
            blockSize = statFs.getBlockSize();
        }

        if (flag == FLAG_AVAILABLE) {

            long availableBlockCount;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlockCount = statFs.getAvailableBlocksLong(); // 可用的 block 数目
            } else {
                availableBlockCount = statFs.getAvailableBlocks();
            }
            return blockSize * availableBlockCount;

        } else {

            long totalBlockCount;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalBlockCount = statFs.getBlockCountLong(); // 全部的 block 数目
            } else {
                totalBlockCount = statFs.getAvailableBlocks();
            }
            return  blockSize * totalBlockCount;
        }
    }
}
