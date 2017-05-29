package com.example.liuyuhua.cainiaonews.entity;

/**
 *  顶部轮播信息的实体类
 * Created by liuyuhua on 2017/4/1.
 */

public class HeadGalleryBean {

    private String mTitle; // 标题
    private String mImageUrl; // 图片URL
    private String mHref; // 文章详情地址
    private String mTag; // 文章分类

    public HeadGalleryBean() {

    }

    public HeadGalleryBean(String title, String imageUrl, String href, String tag) {
        mTitle = title;
        mImageUrl = imageUrl;
        mHref = href;
        mTag = tag;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        mHref = href;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    @Override
    public String toString() {
        return "HeadGalleryBean{" +
                "mTitle='" + mTitle + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mHref='" + mHref + '\'' +
                ", mTag='" + mTag + '\'' +
                '}';
    }
}
