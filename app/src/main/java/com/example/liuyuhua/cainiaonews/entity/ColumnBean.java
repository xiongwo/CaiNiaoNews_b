package com.example.liuyuhua.cainiaonews.entity;

/**
 * 分类信息的实体类
 * Created by liuyuhua on 2017/4/24.
 */

public class ColumnBean {

    // "data" - "items" - "column"
    private int id;
    private String name;
    private String introduction;
    private String bg_color;

    public ColumnBean() {
    }

    public ColumnBean(int id, String name, String introduction, String bg_color) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.bg_color = bg_color;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    @Override
    public String toString() {
        return "ColumnBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", bg_color='" + bg_color + '\'' +
                '}';
    }
}
