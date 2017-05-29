package com.example.liuyuhua.cainiaonews.biz;

import com.example.liuyuhua.cainiaonews.entity.ColumnBean;
import com.example.liuyuhua.cainiaonews.entity.ListNewsBean;
import com.example.androidlib.utils.TransformTime;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析列表新闻的Json数据，并生成ListNewsBean对象
 * Created by liuyuhua on 2017/3/28.
 */

public class ListNewsDataManager {

    public static List<ListNewsBean> getListNewsBeanList(String content) {
        if (content != null) {
            List<ListNewsBean> listNewsBeanList = new ArrayList<>();
            try {
                JSONObject response = new JSONObject(content);
                JSONObject data = response.getJSONObject("data");
                JSONArray items = data.getJSONArray("items");

                Gson gson = new Gson();
                listNewsBeanList = gson.fromJson(items.toString(), new TypeToken<List<ListNewsBean>>(){}.getType());

                if (listNewsBeanList != null) {
                    for (int i = 0; i < listNewsBeanList.size(); i++) {
                        ListNewsBean listNewsBean = listNewsBeanList.get(i);
                        // 设置文章发布的相对时间
                        String relativePublishTime = TransformTime.getRelativeTimeString(listNewsBean.getPublished_at());
                        listNewsBean.setRelativePublishTime(relativePublishTime);
                        // 设置文章详情的地址（因为数据接口地址的原因，该id的数据，其文章详情的数据接口地址是下一篇的地址）
                        String detailHref = "http://36kr.com/api/post/" + listNewsBean.getId() + "/next";
                        listNewsBean.setHref(detailHref);

                        // 有些 Json 数据没有 Column 这个属性
                        if (listNewsBean.getColumn() == null) {
                            ColumnBean columnBean = new ColumnBean(-1, "默认", "这条数据没有column属性", "#000000");
                            listNewsBean.setColumn(columnBean);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listNewsBeanList;
        }
        return null;
    }
}
