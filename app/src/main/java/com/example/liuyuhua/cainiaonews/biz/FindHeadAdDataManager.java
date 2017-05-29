package com.example.liuyuhua.cainiaonews.biz;

import android.util.Log;

import com.example.liuyuhua.cainiaonews.entity.FindHeadAdBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * “发现” 顶部的轮播广告 解析JSON，并生成 FindHeadAdBean 对象
 * Created by liuyuhua on 2017/4/27.
 */

public class FindHeadAdDataManager {

    public static List<FindHeadAdBean> getFindHeadAdBeanList(String result) {
        List<FindHeadAdBean> findHeadAdBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray data = jsonObject.getJSONArray("data");

            Gson gson = new Gson();
            findHeadAdBeanList = gson.fromJson(data.toString(), new TypeToken<List<FindHeadAdBean>>(){}.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return findHeadAdBeanList;
    }
}
