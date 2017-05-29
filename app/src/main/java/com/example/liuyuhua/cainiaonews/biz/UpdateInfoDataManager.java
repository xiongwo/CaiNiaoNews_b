package com.example.liuyuhua.cainiaonews.biz;

import com.example.liuyuhua.cainiaonews.entity.UpdateInfoBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析版本升级信息
 * Created by liuyuhua on 2017/5/10.
 */

public class UpdateInfoDataManager {

    public static UpdateInfoBean getUpdateInfoBean(String response) {
        UpdateInfoBean updateInfoBean = new UpdateInfoBean();
        try {
            JSONObject object = new JSONObject(response);
            Gson gson = new Gson();
            updateInfoBean = gson.fromJson(object.toString(), UpdateInfoBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateInfoBean;
    }
}
