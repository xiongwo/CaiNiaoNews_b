package com.example.liuyuhua.cainiaonews.utils;

import com.example.liuyuhua.cainiaonews.entity.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻分类的数据
 * Created by liuyuhua on 2017/2/21.
 */

public class CategoryDataUtils {

    public static List<CategoryBean> getCategoriesBeans() {
        List<CategoryBean> categoriesBeen = new ArrayList<>();
        categoriesBeen.add(new CategoryBean("最新文章", "http://36kr.com/api/post?&b_id=&per_page=20", "new_article", 12));
        categoriesBeen.add(new CategoryBean("明星公司", "http://36kr.com/api/post?column_id=23&b_id=&per_page=20", "star_company", 12));
        categoriesBeen.add(new CategoryBean("行业新闻", "http://36kr.com/api/post?column_id=104&b_id=&per_page=20", "industry_news", 12));
        categoriesBeen.add(new CategoryBean("早期项目", "http://36kr.com/api/post?column_id=67&b_id=&per_page=20", "early_project", 12));
        categoriesBeen.add(new CategoryBean("深度报道", "http://36kr.com/api/post?column_id=70&b_id=&per_page=20", "deeply_report", 12));
        categoriesBeen.add(new CategoryBean("技能GET", "http://36kr.com/api/post?column_id=103&b_id=&per_page=20", "skill_get", 12));
        categoriesBeen.add(new CategoryBean("行业研究", "http://36kr.com/api/post?column_id=71&b_id=&per_page=20", "industry_research", 12));
        return categoriesBeen;
    }
}
