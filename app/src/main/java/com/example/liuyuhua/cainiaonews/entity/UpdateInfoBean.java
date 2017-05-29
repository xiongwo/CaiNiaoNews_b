package com.example.liuyuhua.cainiaonews.entity;

import java.io.Serializable;

/**
 * 版本升级信息的实体类
 * Created by liuyuhua on 2017/5/10.
 */

public class UpdateInfoBean implements Serializable{

    private String mAppName;
    private String mServerVersionCode;
    private String mServerFlag;
    private String mLastForceCode;
    private String mUpdateUrl;
    private String mUpdateInfo;

    public UpdateInfoBean() {
    }

    public UpdateInfoBean(String appName, String serverVersionCode, String serverFlag, String lastForceCode, String updateUrl, String updateInfo) {
        mAppName = appName;
        mServerVersionCode = serverVersionCode;
        mServerFlag = serverFlag;
        mLastForceCode = lastForceCode;
        mUpdateUrl = updateUrl;
        mUpdateInfo = updateInfo;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public String getServerVersionCode() {
        return mServerVersionCode;
    }

    public void setServerVersionCode(String serverVersionCode) {
        mServerVersionCode = serverVersionCode;
    }

    public String getServerFlag() {
        return mServerFlag;
    }

    public void setServerFlag(String serverFlag) {
        mServerFlag = serverFlag;
    }

    public String getLastForceCode() {
        return mLastForceCode;
    }

    public void setLastForceCode(String lastForceCode) {
        mLastForceCode = lastForceCode;
    }

    public String getUpdateUrl() {
        return mUpdateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        mUpdateUrl = updateUrl;
    }

    public String getUpdateInfo() {
        return mUpdateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        mUpdateInfo = updateInfo;
    }

    @Override
    public String toString() {
        return "UpdateInfoBean{" +
                "mAppName='" + mAppName + '\'' +
                ", mServerVersionCode='" + mServerVersionCode + '\'' +
                ", mServerFlag='" + mServerFlag + '\'' +
                ", mLastForceCode='" + mLastForceCode + '\'' +
                ", mUpdateUrl='" + mUpdateUrl + '\'' +
                ", mUpdateInfo='" + mUpdateInfo + '\'' +
                '}';
    }
}
