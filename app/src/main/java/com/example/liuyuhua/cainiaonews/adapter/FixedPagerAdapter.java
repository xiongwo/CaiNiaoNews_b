package com.example.liuyuhua.cainiaonews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.liuyuhua.cainiaonews.entity.CategoryBean;
import com.example.androidlib.fragment.BaseFragment;

import java.util.List;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class FixedPagerAdapter extends FragmentStatePagerAdapter {

    private List<CategoryBean> mCategoryBeanList;
    private List<BaseFragment> mBaseFragments;

    public FixedPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void setCategoryBeanList(List<CategoryBean> categoryBeanList) {
        mCategoryBeanList = categoryBeanList;
    }

    public void setBaseFragments(List<BaseFragment> baseFragments) {
        mBaseFragments = baseFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mBaseFragments.get(position);
    }

    @Override
    public int getCount() {
        return mBaseFragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = null;
        fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryBeanList.get(position).getTitle();
    }
}
