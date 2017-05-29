package com.example.androidlib.utils;

import android.view.View;

/**
 * 设置网络请求四种情况的布局可视
 * Created by liuyuhua on 2017/5/17.
 */

public class VisibilityManager {

    private View mTargetView;
    private View mStatusView;
    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;

    public VisibilityManager(View targetView, View statusView, View loadingView, View emptyView, View errorView) {
        mTargetView = targetView;
        mStatusView = statusView;
        mLoadingView = loadingView;
        mEmptyView = emptyView;
        mErrorView = errorView;
    }

    // 设置“加载成功”页面显示
    public void setTargetViewVisible() {
        mTargetView.setVisibility(View.VISIBLE);
        mStatusView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    // 设置“正在加载”页面显示
    public void setLoadingViewVisible() {
        mTargetView.setVisibility(View.GONE);
        mStatusView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    // 设置“数据为空”页面显示
    public void setEmptyViewVisible() {
        mTargetView.setVisibility(View.GONE);
        mStatusView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
    }

    // 设置“加载错误”页面显示
    public void setErrorViewVisible() {
        mTargetView.setVisibility(View.GONE);
        mStatusView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
