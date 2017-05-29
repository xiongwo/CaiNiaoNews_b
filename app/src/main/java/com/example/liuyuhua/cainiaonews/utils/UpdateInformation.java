package com.example.liuyuhua.cainiaonews.utils;

import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.application.CNKApplication;

/**
 * 版本信息与更新信息
 * Created by liuyuhua on 2017/5/10.
 */

public class UpdateInformation {

    // 本地信息
    public static String sAppName = CNKApplication.getInstance().getResources().getString(R.string.app_name);
    public static int sLocalVersionCode = 1; // 本地版本号
    public static String sLocalVersionName = "1.0"; // 本地版本名

    // 服务器信息
    public static int sServerVersionCode = 1; // 服务器版本号
    public static int sServerFlag = 0; // 服务器标志
    public static int sLastForceCode = 0; // 之前强制升级的版本号
    public static String sUpdateUrl = ""; // 升级包的获取地址
    public static String sUpdateInfo = ""; // 升级信息

    public static String sDownloadDir = "ztt_download"; // 下载目录
}
