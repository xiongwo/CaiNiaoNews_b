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
import com.example.androidlib.widget.RecyclerItemDecoration;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.adapter.RecyclerAdapter;
import com.example.liuyuhua.cainiaonews.biz.FindInvestorDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.FindInvestorBean;
import com.example.liuyuhua.cainiaonews.utils.FindDataUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * Created by liuyuhua on 2017/4/29.
 */

public class FindInvestorActivity extends BaseActivity implements DefineView{

    private static final String TAG = FindInvestorActivity.class.getSimpleName();

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
    private List<FindInvestorBean> mFindInvestorBeanList;
    private RecyclerItemDecoration mRecyclerItemDecoration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_investor);
        setStatusBar(R.id.root_top_bar_common_rl);
        setTopBarTitle(R.id.tv_top_bar_common_title, "发现投资人");

        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    public void initializeView() {
        mBackButton = (Button) findViewById(R.id.btn_top_bar_common_back);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_activity_find_investor);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerAdapter = new RecyclerAdapter(this, RecyclerAdapter.item_type_find_investor);
        mRecyclerItemDecoration = new RecyclerItemDecoration(this);

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

        OkHttpManager.getAsync(FindDataUtils.sInvestorUrl, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "寻找投资人加载失败！");
                mVisibilityManager.setErrorViewVisible();
            }

            @Override
            public void requestSuccess(String result) {
                mFindInvestorBeanList = FindInvestorDataManager.getFindInvestorBeanList(result);
                bindData();
            }
        });
    }

    @Override
    public void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindInvestorActivity.this.finish();
            }
        });
    }

    @Override
    public void bindData() {
        if (mFindInvestorBeanList != null) {
            mVisibilityManager.setTargetViewVisible();

            mRecyclerView.addItemDecoration(mRecyclerItemDecoration);
            mRecyclerView.setHasFixedSize(true);

            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mRecyclerAdapter.setImageLoader(mImageLoader);
            mRecyclerAdapter.setFindInvestorBeanList(mFindInvestorBeanList);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        } else {
            mVisibilityManager.setEmptyViewVisible();
        }
    }
}
