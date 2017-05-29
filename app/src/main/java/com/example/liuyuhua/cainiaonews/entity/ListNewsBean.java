package com.example.liuyuhua.cainiaonews.entity;

import java.util.Arrays;

/**
 * 列表新闻的实体类
 * Created by liuyuhua on 2017/3/28.
 */

public class ListNewsBean {

    // "data" - "items"
    private int id; // 这一条数据的id "id"
    private String title; // 文章标题 "title"
    private String summary; // 文章概要 "summary"
    private String cover; // 缩略图的地址 "cover"
    private String related_post_ids; // 相关的数据id "related_post_ids"
    private String extraction_tags; // 内容属于哪些领域 "extraction_tags"
    private String published_at; // 发表时间 "published_at"

    // "data" - "items" - "column"
    private ColumnBean column; // 分类信息 "column"

    // "data" - "items" - "user"
    private AuthorBean user; // 作者信息 "user"

    private String mRelativePublishTime; // 相对用户查看的时间
    private String mHref; // 文章详情的地址

    public ListNewsBean() {
    }

    public ListNewsBean(int id, String title, String summary, String cover, String related_post_ids, String extraction_tags, String published_at, ColumnBean column, AuthorBean user) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.cover = cover;
        this.related_post_ids = related_post_ids;
        this.extraction_tags = extraction_tags;
        this.published_at = published_at;
        this.column = column;
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

    public String getExtraction_tags() {
        return extraction_tags;
    }

    public void setExtraction_tags(String extraction_tags) {
        this.extraction_tags = extraction_tags;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public ColumnBean getColumn() {
        return column;
    }

    public void setColumn(ColumnBean column) {
        this.column = column;
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

    public String getHref() {
        return mHref;
    }

    public void setHref(String href) {
        mHref = href;
    }

    @Override
    public String toString() {
        return "ListNewsBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", cover='" + cover + '\'' +
                ", related_post_ids='" + related_post_ids + '\'' +
                ", extraction_tags='" + extraction_tags + '\'' +
                ", published_at='" + published_at + '\'' +
                ", column=" + column +
                ", user=" + user +
                ", mRelativePublishTime='" + mRelativePublishTime + '\'' +
                ", mHref='" + mHref + '\'' +
                '}';
    }
}
