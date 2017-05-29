package com.example.liuyuhua.cainiaonews.application;

import android.app.Application;

import com.example.liuyuhua.cainiaonews.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class CNKApplication extends Application {

    private static CNKApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeImageLoader();
    }

    private void initializeImageLoader() {

        // 配置 UIL ，并创建全局变量
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().
                showImageOnLoading(R.drawable.ic_stub).
                showImageForEmptyUri(R.drawable.ic_empty).
                showImageOnFail(R.drawable.ic_error).
                cacheInMemory(true).
                cacheOnDisk(true).
                build();

        ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).
                defaultDisplayImageOptions(displayImageOptions).
                build();
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

    public static CNKApplication getInstance() {
        return instance;
    }
}
