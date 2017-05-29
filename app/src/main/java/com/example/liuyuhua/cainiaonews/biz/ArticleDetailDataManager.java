package com.example.liuyuhua.cainiaonews.biz;

import com.example.androidlib.utils.TransformTime;
import com.example.liuyuhua.cainiaonews.entity.ArticleDetailBean;
import com.example.liuyuhua.cainiaonews.entity.AuthorBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析文章详情的Json数据，并生成ArticleDetailBean对象
 * Created by liuyuhua on 2017/4/18.
 */

public class ArticleDetailDataManager {

    public static ArticleDetailBean getArticleDetailBean(String response) {
        ArticleDetailBean articleDetailBean = new ArticleDetailBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject data = jsonObject.getJSONObject("data");

            Gson gson = new Gson();
            articleDetailBean = gson.fromJson(data.toString(), ArticleDetailBean.class);

            // 设置文章发布的相对时间
            String relativePublishTime = TransformTime.getRelativeTimeString(articleDetailBean.getPublished_at());
            articleDetailBean.setRelativePublishTime(relativePublishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return articleDetailBean;
    }
}
