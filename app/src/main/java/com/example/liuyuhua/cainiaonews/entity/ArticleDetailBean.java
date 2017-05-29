package com.example.liuyuhua.cainiaonews.entity;

import java.util.Arrays;

/**
 * 文章详情的实体类
 * Created by liuyuhua on 2017/4/18.
 */

public class ArticleDetailBean {

    // "data"
    private int id; // 这一条数据的id "id"
    private String title; // 文章标题 "title"
    private String summary; // 文章概要 "summary"
    private String cover; // 封面图的地址 "cover"
    private String related_post_ids; // 相关的数据id "related_post_ids"
    private String published_at; // 发表时间 "published_at"
    private String content; // 内容 "content"

    // "data" - "user"
    private AuthorBean user; // 作者信息 "user"

    private String mRelativePublishTime; // 相对用户查看的时间

    public ArticleDetailBean() {
    }

    public ArticleDetailBean(int id, String title, String summary, String cover, String related_post_ids, String published_at, String content, AuthorBean user) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.cover = cover;
        this.related_post_ids = related_post_ids;
        this.published_at = published_at;
        this.content = content;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRelated_post_ids() {
        return related_post_ids;
    }

    public void setRelated_post_ids(String related_post_ids) {
        this.related_post_ids = related_post_ids;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AuthorBean getUser() {
        return user;
    }

    public void setUser(AuthorBean user) {
        this.user = user;
    }

    public String getRelativePublishTime() {
        return mRelativePublishTime;
    }

    public void setRelativePublishTime(String relativePublishTime) {
        mRelativePublishTime = relativePublishTime;
    }

    @Override
    public String toString() {
        return "ArticleDetailBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", cover='" + cover + '\'' +
                ", related_post_ids='" + related_post_ids + '\'' +
                ", published_at='" + published_at + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", mRelativePublishTime='" + mRelativePublishTime + '\'' +
                '}';
    }
}
