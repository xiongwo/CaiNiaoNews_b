package com.example.liuyuhua.cainiaonews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.adapter.FixedPagerAdapter;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.CategoryBean;
import com.example.liuyuhua.cainiaonews.activity.MainActivity;
import com.example.liuyuhua.cainiaonews.utils.CategoryDataUtils;
import com.example.androidlib.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class MainInfoFragment extends BaseFragment implements DefineView, ViewPager.OnPageChangeListener{

    private static final String TAG = "MainInfoFragment";
    private View mView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<BaseFragment> mBaseFragments;
    private FixedPagerAdapter mFixedPagerAdapter;
    private List<CategoryBean> mCategoryBeanList = CategoryDataUtils.getCategoriesBeans();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_main_info_layout, container, false);
            initializeView();
            initializeValidData();
            initializeListener();
            bindData();
        }
        return mView;
    }

    @Override
    public void initializeView() {
        mTabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) mView.findViewById(R.id.info_view_pager);
    }

    @Override
    public void initializeValidData() {
        mBaseFragments = new ArrayList<>();
        for (int i = 0; i < mCategoryBeanList.size(); i++) {
            BaseFragment baseFragment = null;
            if ( i == 0) {
                baseFragment = HomeFragment.newInstance(mCategoryBeanList.get(i));
            } else {
                baseFragment = PageFragment.newInstance(mCategoryBeanList.get(i));
            }
            mBaseFragments.add(baseFragment);
        }

        mFixedPagerAdapter = new FixedPagerAdapter(getChildFragmentManager());
        mFixedPagerAdapter.setCategoryBeanList(mCategoryBeanList);
        mFixedPagerAdapter.setBaseFragments(mBaseFragments);

        mViewPager.setAdapter(mFixedPagerAdapter);

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void initializeListener() {
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void bindData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled");
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected");
        if (position == 0) {
            ((MainActivity) getActivity()).getDragLayout().setIsDrag(true);
        }else {
            ((MainActivity) getActivity()).getDragLayout().setIsDrag(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged");
    }
}
