package com.example.liuyuhua.cainiaonews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.androidlib.network.OkHttpManager;
import com.example.androidlib.utils.VisibilityManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.adapter.RecyclerAdapter;
import com.example.liuyuhua.cainiaonews.biz.RecentActivityDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.RecentActivityBean;
import com.example.liuyuhua.cainiaonews.utils.FindDataUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * Created by liuyuhua on 2017/4/28.
 */

public class RecentActActivity extends BaseActivity implements DefineView{

    private static final String TAG = RecentActActivity.class.getSimpleName();

    private Button mBackButton;
    private RecyclerView mRecyclerView;

    private VisibilityManager mVisibilityManager;
    private FrameLayout mFrameLayout;
    private LinearLayout mLoadingLayout;
    private LinearLayout mLoadEmptyLayout;
    private LinearLayout mLoadErrorLayout;

    private ImageLoader mImageLoader;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private List<RecentActivityBean> mRecentActivityBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_act);
        setStatusBar(R.id.root_top_bar_common_rl);
        setTopBarTitle(R.id.tv_top_bar_common_title, "近期活动");

        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    public void initializeView() {
        mBackButton = (Button) findViewById(R.id.btn_top_bar_common_back);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_activity_recent_act);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerAdapter = new RecyclerAdapter(this, RecyclerAdapter.item_type_card);

        mFrameLayout = (FrameLayout) findViewById(R.id.root_load_status_fl);
        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        mLoadEmptyLayout = (LinearLayout) findViewById(R.id.layout_load_empty);
        mLoadErrorLayout = (LinearLayout) findViewById(R.id.layout_load_error);
        mVisibilityManager = new VisibilityManager(mRecyclerView, mFrameLayout, mLoadingLayout, mLoadEmptyLayout, mLoadErrorLayout);

        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initializeValidData() {
        mVisibilityManager.setLoadingViewVisible();

        OkHttpManager.getAsync(FindDataUtils.sActivityUrl, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "近期活动加载失败！");
                mVisibilityManager.setErrorViewVisible();
            }

            @Override
            public void requestSuccess(String result) {
                mRecentActivityBeanList = RecentActivityDataManager.getRecentActivityBeanList(result);
                bindData();
            }
        });
    }

    @Override
    public void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecentActActivity.this.finish();
            }
        });
    }

    @Override
    public void bindData() {
        if (mRecentActivityBeanList != null) {
            mVisibilityManager.setTargetViewVisible();

            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerAdapter.setImageLoader(mImageLoader);
            mRecyclerAdapter.setRecentActivityBeanList(mRecentActivityBeanList);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        } else {
            mVisibilityManager.setEmptyViewVisible();
        }
    }
}
