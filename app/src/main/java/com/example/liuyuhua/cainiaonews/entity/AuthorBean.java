package com.example.liuyuhua.cainiaonews.entity;

/**
 * 作者信息的实体类
 * Created by liuyuhua on 2017/3/28.
 */

public class AuthorBean {

    // 列表新闻  "data" - "items" - "user"
    // 文章详情  "data" - "user"
    private int id; // 作者id "user"-"id"
    private String name; // 作者名字 "user"-"name"
    private String avatar_url; // 作者头像地址 "user"-"avatar_url"
    private String introduction; // 作者简介 "user"-"introduction"

    private String mHref; // 作者主页地址
    private String mBadge; // 作者等级（如：新锐作者）
    private String mTotalArticles; // 发表文章的总数
    private String mReadAmount; // 文章的总阅读量

    public AuthorBean() {
    }

    public AuthorBean(int id, String name, String avatar_url, String introduction) {
        this.id = id;
        this.name = name;
        this.avatar_url = avatar_url;
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "AuthorBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
