package com.example.liuyuhua.cainiaonews.biz;

import android.util.Log;

import com.example.liuyuhua.cainiaonews.entity.HeadGalleryBean;
import com.example.liuyuhua.cainiaonews.entity.ListNewsBean;
import com.example.liuyuhua.cainiaonews.utils.HeadGalleryImageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成顶部轮播栏数据
 * 说明：因为没有轮播栏的接口数据，所以我在“最新文章”的数据里为每一个栏目找一条数据来作为轮播的数据
 *       但“最新文章”里的20条（每次拿20条）数据不一定都包含6个栏目，所以轮播的图片数量不一定是6
 *       另外图片是静态获取的，会跟链接的文章内容不同
 * Created by liuyuhua on 2017/4/2.
 */

public class HeadGalleryDataManager {

    public static List<HeadGalleryBean> getHeadGalleryBeanList(List<ListNewsBean> listNewsBeanList) {
        if (listNewsBeanList != null) {
            int count = 0; // 标志：是否可以退出循环
            int[] flag = new int[]{0, 0, 0, 0, 0, 0}; // 标志:是否已经为该栏目找到数据
            List<HeadGalleryBean> headGalleryBeanList = new ArrayList<>();
            for (int i = 0; i < listNewsBeanList.size(); i++) {
                switch (listNewsBeanList.get(i).getColumn().getName()) {
                    case "明星公司":
                        if (flag[0] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 0));
                            flag[0] = 1;
                            count++;
                        }
                        break;
                    case "行业新闻":
                        if (flag[1] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 1));
                            flag[1] = 1;
                            count++;
                        }
                        break;
                    case "早期项目":
                        if (flag[2] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 2));
                            flag[2] = 1;
                            count++;
                        }
                        break;
                    case "深度报道":
                        if (flag[3] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 3));
                            flag[3] = 1;
                            count++;
                        }
                        break;
                    case "技能GET":
                        if (flag[4] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 4));
                            flag[4] = 1;
                            count++;
                        }
                        break;
                    case "行业研究":
                        if (flag[5] == 0) {
                            headGalleryBeanList.add(getHeadGalleryBean(listNewsBeanList.get(i), 5));
                            flag[5] = 1;
                            count++;
                        }
                        break;
                    default:
                        break;
                }
                if (count >= 6) {
                    break;
                }
            }
            return headGalleryBeanList;
        }
        return null;
    }

    private static HeadGalleryBean getHeadGalleryBean(ListNewsBean listNewsBean, int i) {
        HeadGalleryBean headGalleryBean = new HeadGalleryBean();
        headGalleryBean.setTitle(listNewsBean.getTitle());
        String[] splitStrings = listNewsBean.getCover().split("!", 2);
        String wholeImageHref = splitStrings[0];
        Log.i("666", wholeImageHref);
        headGalleryBean.setImageUrl(HeadGalleryImageUtils.getImageUrl(i));
        // 文章详情地址
        headGalleryBean.setTag(listNewsBean.getColumn().getName());
        return headGalleryBean;
    }

}
