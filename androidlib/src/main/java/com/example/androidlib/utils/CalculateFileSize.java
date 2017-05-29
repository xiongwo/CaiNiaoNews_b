package com.example.androidlib.utils;

import java.io.File;

/**
 * 计算文件大小和转化为字符串
 * Created by liuyuhua on 2017/5/10.
 */

public class CalculateFileSize {

    // 计算该目录下，文件的大小。该目录可能就是一个文件，或者是一个文件夹，甚至是两者混合
    public static long getDirectoryFilesSize(File directory) {
        if (directory == null) {
            return 0;
        } else {
            if (!directory.isDirectory()) {
                return 0;
            } else {
                long dirSize = 0;
                File[] files = directory.listFiles();
                for (File file: files) {
                    if (file.isFile()) {
                        dirSize += file.length();
                    } else if (file.isDirectory()) {
                        dirSize += getDirectoryFilesSize(file); // 如果 file 是目录，则递归计算
                    }
                }
                return dirSize;
            }
        }
    }

    // 转化文件大小为字符串
    public static String formatSize(long size) {
        String suffix = "byte";
        if (size == 0) {
            return "";
        }
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = "G";
                    size /= 1024;
                }
            }
        }
//        if (size > 1000) {
//            String result = Double.toString(size / 1024.0).substring(0, 4) + suffix; // 如：0.99MB
//        }
        StringBuilder result = new StringBuilder(Long.toString(size));
        int commaOffset = result.length() - 3;
        while (commaOffset > 0) {
            result.insert(commaOffset, ",");
            commaOffset -= 3;
        }
        result.append(suffix);
        return result.toString();
    }
}
