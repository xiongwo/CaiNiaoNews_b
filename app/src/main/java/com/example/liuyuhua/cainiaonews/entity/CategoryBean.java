package com.example.liuyuhua.cainiaonews.entity;

import java.io.Serializable;

/**
 * 新闻分类的实体类
 * Created by liuyuhua on 2017/2/20.
 */

public class CategoryBean implements Serializable{

    private String mTitle; // 分类标题
    private String mHref; // 分类的数据接口地址
    private String mSharedName; // SharedPreferences的文件名
    private int mExpires; // 缓存时间，以小时为单位

    public CategoryBean() {
        super();
    }


    public CategoryBean(String title, String href, String sharedName, int expires) {
        mTitle = title;
        mHref = href;
        mSharedName = sharedName;
        mExpires = expires;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        mHref = href;
    }

    public int getExpires() {
        return mExpires;
    }

    public void setExpires(int expires) {
        mExpires = expires;
    }

    public String getSharedName() {
        return mSharedName;
    }

    public void setSharedName(String sharedName) {
        mSharedName = sharedName;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "mTitle='" + mTitle + '\'' +
                ", mHref='" + mHref + '\'' +
                ", mSharedName='" + mSharedName + '\'' +
                ", mExpires=" + mExpires +
                '}';
    }
}
