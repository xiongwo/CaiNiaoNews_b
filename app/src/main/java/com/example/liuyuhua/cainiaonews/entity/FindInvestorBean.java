package com.example.liuyuhua.cainiaonews.entity;

import java.util.Arrays;

/**
 * “寻找投资人”
 * Created by liuyuhua on 2017/4/29.
 */

public class FindInvestorBean {

    private String position; // "data" - "data" - "position"
    private String logo; // "data" - "data" - "logo"
    private String name; // "data" - "data" - "name"
    private String orgName; // "data" - "data" - "orgName"

    // 可能会出错 经测试，Gson 可以自动解析
    private String[] industry; // "data" - "data" - "industry"

    public FindInvestorBean(String position, String logo, String name, String orgName, String[] industry) {
        this.position = position;
        this.logo = logo;
        this.name = name;
        this.orgName = orgName;
        this.industry = industry;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String[] getIndustry() {
        return industry;
    }

    public void setIndustry(String[] industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "FindInvestorBean{" +
                "position='" + position + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", orgName='" + orgName + '\'' +
                ", industry=" + Arrays.toString(industry) +
                '}';
    }
}
