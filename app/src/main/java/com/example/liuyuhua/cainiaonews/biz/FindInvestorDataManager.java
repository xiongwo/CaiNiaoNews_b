package com.example.liuyuhua.cainiaonews.biz;

import android.util.Log;

import com.example.liuyuhua.cainiaonews.entity.FindInvestorBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * “寻找投资人”
 * Created by liuyuhua on 2017/4/29.
 */

public class FindInvestorDataManager {

    public static List<FindInvestorBean> getFindInvestorBeanList(String result) {
        List<FindInvestorBean> findInvestorBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONObject pageObject = dataObject.getJSONObject("pageData");
            JSONArray dataArray = pageObject.getJSONArray("data");

            Gson gson = new Gson();
            findInvestorBeanList = gson.fromJson(dataArray.toString(), new TypeToken<List<FindInvestorBean>>(){}.getType());

//            for (int i = 0; i < dataArray.length(); i++) {
//                JSONObject data = dataArray.getJSONObject(i);
//                JSONArray industryArray = data.getJSONArray("industry");
//                String industry = null;
//                for (int j = 0; j < industryArray.length(); j++) {
//                    industry += industryArray.getString(j);
//                }
//                findInvestorBeanList.get(i).setIndustry(industry);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return findInvestorBeanList;
    }
}
