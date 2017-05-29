package com.example.liuyuhua.cainiaonews.biz;

import com.example.liuyuhua.cainiaonews.entity.RecentActivityBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * “近期活动”
 * Created by liuyuhua on 2017/4/28.
 */

public class RecentActivityDataManager {

    public static List<RecentActivityBean> getRecentActivityBeanList(String result) {
        List<RecentActivityBean> recentActivityBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject dataObject = jsonObject.getJSONObject("data");
            JSONArray dataArray = dataObject.getJSONArray("data");

            Gson gson = new Gson();
            recentActivityBeanList = gson.fromJson(dataArray.toString(), new TypeToken<List<RecentActivityBean>>(){}.getType());

            if (recentActivityBeanList != null) {
                for (int i = 0; i < recentActivityBeanList.size(); i++) {
                    RecentActivityBean recentActivityBean = recentActivityBeanList.get(i);
                    recentActivityBean.setBeginTime(getTimeInString(recentActivityBean.getActivityBeginTime()));
                    recentActivityBean.setEndTime(getTimeInString(recentActivityBean.getActivityEndTime()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recentActivityBeanList;
    }

    private static String getTimeInString(long milliSecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
        String monthDay = dateFormat.format(calendar.getTime()).replaceAll("0", "");
        String[] text = monthDay.split("-", 2);
        String month = text[0] + "月";
        String day = text[1] + "日";
        return  month + day;
    }
}
