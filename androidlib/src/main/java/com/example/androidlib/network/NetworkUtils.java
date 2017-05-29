package com.example.androidlib.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断连接的是哪一种网络
 * Created by liuyuhua on 2017/5/15.
 */

public class NetworkUtils {

    private static NetworkInfo getNetworkInfo(Context context) {
        NetworkInfo networkInfo = null;
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo;
    }

    /**
     * 判断是否有网络已经连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断移动网络是否已经连接
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断Wifi是否已经连接
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return networkInfo.isConnected();
            }
        }
        return false;
    }
}
