package com.example.liuyuhua.cainiaonews.entity;

/**
 * “近期活动”
 * Created by liuyuhua on 2017/4/28.
 */

public class RecentActivityBean {

    private Object activityBriefArray; // "data" - "data" - "activityBriefArray"
    private String listImageUrl; // "data" - "data" - "listImageUrl"
    private String city; // "data" - "data" - "city"
    private String title; // "data" - "data" - "title"
    private long activityBeginTime; // "data" - "data" - "activityBeginTime"
    private long activityEndTime; // "data" - "data" - "activityEndTime"
    private String description; // "data" - "data" - "description"

    private String beginTime; // 4月5日
    private String endTime; // 5月22日

    public RecentActivityBean(String listImageUrl, String city, String title, long activityBeginTime, long activityEndTime, String description) {
        this.listImageUrl = listImageUrl;
        this.city = city;
        this.title = title;
        this.activityBeginTime = activityBeginTime;
        this.activityEndTime = activityEndTime;
        this.description = description;
    }

    public String getListImageUrl() {
        return listImageUrl;
    }

    public void setListImageUrl(String listImageUrl) {
        this.listImageUrl = listImageUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getActivityBeginTime() {
        return activityBeginTime;
    }

    public void setActivityBeginTime(long activityBeginTime) {
        this.activityBeginTime = activityBeginTime;
    }

    public long getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(long activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
