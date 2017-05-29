package com.example.androidlib.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * ViewPager 轮播栏
 * 目前数据是 ImageView ，可拓展成泛型 T
 * Created by liuyuhua on 2017/4/26.
 */

public class GalleryViewPagerAdapter extends PagerAdapter {

    private int mPageCount;
    private List<ImageView> mImageViewList;

    public GalleryViewPagerAdapter(int pageCount, List<ImageView> imageViewList) {
        mPageCount = pageCount;
        mImageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mImageViewList.get(position % mImageViewList.size());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
