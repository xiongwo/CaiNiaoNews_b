package com.example.liuyuhua.cainiaonews.entity;

/**
 * “发现” 顶部的轮播广告的实体类
 * Created by liuyuhua on 2017/4/27.
 */

public class FindHeadAdBean {

    private String img_url; // "data" - "img_url"
    private String link_url; // "data" - "link_url"
    private String title; // "data" - "title"

    public FindHeadAdBean() {
    }

    public FindHeadAdBean(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "FindHeadAdBean{" +
                "img_url='" + img_url + '\'' +
                '}';
    }
}
