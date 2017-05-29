package com.example.androidlib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 设置decode Bitmap options
 * Created by liuyuhua on 2017/5/20.
 */

public class BitmapDecode {

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图大小
        final int srcWidth = options.outWidth;
        final int srcHeight = options.outHeight;
        int inSampleSize = 1;
        if (srcWidth > reqWidth || srcHeight > reqHeight) {
            // 计算源图和目标图大小的比率
            final int widthRatio = Math.round((float) srcWidth / (float) reqWidth);
            final int heightRatio = Math.round((float) srcHeight / (float) reqHeight);
            // 选择比率小的那个，如reqWidth = 200 , reqHeight = 100 , srcWidth = 400 , srcHeight = 400
            inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 并没有真正decode Bitmap，即没有分配内存，但仍可以获取源图尺寸
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }
}
