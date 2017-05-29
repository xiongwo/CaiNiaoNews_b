package com.example.liuyuhua.cainiaonews.entity;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class LeftItemMenu {

    private int leftIcon;
    private String title;

    public LeftItemMenu() {
    }

    public LeftItemMenu(int leftIcon, String title) {
        this.leftIcon = leftIcon;
        this.title = title;
    }

    public int getLeftIcon() {
        return leftIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setLeftIcon(int leftIcon) {
        this.leftIcon = leftIcon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
