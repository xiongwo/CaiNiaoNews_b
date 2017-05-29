package com.example.liuyuhua.cainiaonews.utils;

import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.entity.LeftItemMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class LeftMenuDataUtils {

    public static List<LeftItemMenu> getItemMenus() {
        List<LeftItemMenu> menus = new ArrayList<>();
        menus.add(new LeftItemMenu(R.drawable.icon_zhanghaoxinxi, "账号信息"));
        menus.add(new LeftItemMenu(R.drawable.ic_find, "发现"));
        menus.add(new LeftItemMenu(R.drawable.icon_wodeguanzhu, "我的关注"));
        menus.add(new LeftItemMenu(R.drawable.icon_shoucang, "我的收藏"));
        menus.add(new LeftItemMenu(R.drawable.icon_yijianfankui, "意见反馈"));
        menus.add(new LeftItemMenu(R.drawable.icon_shezhi, "设置"));
        return menus;
    }
}
