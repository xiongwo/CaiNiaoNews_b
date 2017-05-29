package com.example.liuyuhua.cainiaonews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidlib.adapter.GalleryViewPagerAdapter;
import com.example.androidlib.network.OkHttpManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.biz.FindHeadAdDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.FindHeadAdBean;
import com.example.liuyuhua.cainiaonews.utils.FindDataUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by liuyuhua on 2017/4/27.
 */

public class FindActivity extends BaseActivity implements DefineView, View.OnTouchListener{

    private static final String TAG = FindActivity.class.getSimpleName();

    private Button mBackButton;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private ImageLoader mImageLoader;
    private List<FindHeadAdBean> mFindHeadAdBeanList;
    private int mPageCount;
    private GalleryViewPagerAdapter mGalleryViewPagerAdapter;
    private RelativeLayout mRelativeLayout;
    private RelativeLayout mInvestorRl;
    private boolean mIsRunning = false;
    private TextView mTopBarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        setStatusBar(R.id.root_top_bar_common_rl);
        setTopBarTitle(R.id.tv_top_bar_common_title, "发现");

        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsRunning = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsRunning = false;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countTime();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void initializeView() {
        mTopBarTitle = (TextView) findViewById(R.id.tv_top_bar_common_title);
        mBackButton = (Button) findViewById(R.id.btn_top_bar_common_back);

        mImageLoader = ImageLoader.getInstance();

        mViewPager = (ViewPager) findViewById(R.id.vp_activity_find);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_activity_find);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_activity_find_activity);
        mInvestorRl = (RelativeLayout) findViewById(R.id.rl_activity_find_investor);
    }

    @Override
    public void initializeValidData() {
        OkHttpManager.getAsync(FindDataUtils.sHeadAdUrl, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "请求顶部广告数据失败！");
            }

            @Override
            public void requestSuccess(String result) {
                mFindHeadAdBeanList = FindHeadAdDataManager.getFindHeadAdBeanList(result);
                bindData();
            }
        });
    }

    @Override
    public void initializeListener() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindActivity.this.finish();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: " + state);
            }
        });

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RecentActActivity.class);
            }
        });

        mInvestorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FindInvestorActivity.class);
            }
        });
    }

    @Override
    public void bindData() {
        mTopBarTitle.setText("发现");
        mPageCount = 1000 + mFindHeadAdBeanList.size() * 2; // 1、从中间开始也能够从第0个数据开始  2、Adapter中也能够从第0个开始
        mGalleryViewPagerAdapter = new GalleryViewPagerAdapter(mPageCount, getImageViewList(mFindHeadAdBeanList));
        mViewPager.setAdapter(mGalleryViewPagerAdapter);
        mViewPager.setCurrentItem(mPageCount / 2);// 显示的图片的伪代码为 data[(mPageCount / 2) % size] 这里就是 index == 0
        mViewPager.setOnTouchListener(this);
        countTime();
    }

    private List<ImageView> getImageViewList(List<FindHeadAdBean> findHeadAdBeanList) {
        List<ImageView> imageViewList = new ArrayList<>();
        ImageView imageView;
        for (int i = 0; i < findHeadAdBeanList.size(); i++) {
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageLoader.displayImage(findHeadAdBeanList.get(i).getImg_url(), imageView); // 可能有问题
            imageViewList.add(imageView);
        }
        return imageViewList;
    }

    private void countTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIsRunning = true;
                while (mIsRunning) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1); // 切换到下一个
                        }
                    });
                }
            }
        }).start();
    }
}
