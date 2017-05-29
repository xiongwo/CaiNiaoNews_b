package com.example.liuyuhua.cainiaonews.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidlib.network.OkHttpManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.adapter.LeftItemAdapter;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.biz.UpdateInfoDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.UpdateInfoBean;
import com.example.liuyuhua.cainiaonews.receiver.UpdateBroadcastReceiver;
import com.example.liuyuhua.cainiaonews.utils.UpdateUrl;
import com.example.liuyuhua.cainiaonews.widget.DragLayout;
import com.nineoldandroids.view.ViewHelper;

import java.io.IOException;

import okhttp3.Request;

/**
 * 主页面
 * Created by liuyuhua on 2016/11/19.
 */
public class MainActivity extends BaseActivity implements DefineView{

    private static final String TAG = "MainActivity" ;

    private DragLayout mDragLayout;
    private ImageView mMainIcon;
    private ImageView mLeftIcon;
    private ListView mLeftListView;
    private UpdateBroadcastReceiver mUpdateBroadcastReceiver;
    private IntentFilter mIntentFilter;

    private boolean mIsDragLayoutOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusBar(R.id.top_bar_layout);

        registerBroadcastReceiver();

        initializeDragLayout();
        initializeView();
        initializeValidData();
        initializeListener();
        bindData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }

    private void initializeDragLayout() {
        mDragLayout = (DragLayout) findViewById(R.id.drag_layout);
        mDragLayout.setDragListener(new DragLayout.DragListener() {
            @Override
            public void onOpen() {
                Log.d(TAG, "DragLayout onOpen");
                mIsDragLayoutOpen = true;
            }

            @Override
            public void onClose() {
                Log.d(TAG, "DragLayout onClose");
                mIsDragLayoutOpen = false;
            }

            @Override
            public void onDrag(float percent) {
                ViewHelper.setAlpha(mMainIcon, 1 - percent);
            }
        });
    }

    @Override
    public void initializeView() {
        mMainIcon = (ImageView) findViewById(R.id.iv_avatar_top_bar);
        mLeftIcon = (ImageView) findViewById(R.id.left_image_view);
        mLeftListView = (ListView) findViewById(R.id.left_list_view);
    }

    @Override
    public void initializeValidData() {
        // 自动检测
        // 请求更新信息
        OkHttpManager.getAsync(UpdateUrl.UPDATE_URL, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "获取更新信息的响应失败");
                Log.d(TAG, e.toString());
            }

            @Override
            public void requestSuccess(String result) {
                UpdateInfoBean updateInfoBean = UpdateInfoDataManager.getUpdateInfoBean(result);
                Intent intent = new Intent(UpdateBroadcastReceiver.UPDATE_ACTION);
                intent.putExtra(UpdateBroadcastReceiver.UPDATE_INFO, updateInfoBean);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public void initializeListener() {
        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // 账号信息
                        break;
                    case 1: // 发现
                        openActivity(FindActivity.class);
                        break;
                    case 2: // 我的关注
                        break;
                    case 3: // 我的收藏
                        break;
                    case 4: // 意见反馈
                        openActivity(SuggestActivity.class);
                        break;
                    case 5: // 设置
                        break;
                }
            }
        });

        mMainIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDragLayout.open();
            }
        });
    }

    @Override
    public void bindData() {
        mLeftListView.setAdapter(new LeftItemAdapter());
    }

    /**
     * 如果DragLayout已经打开，就让它关闭；如果DragLayout已经关闭，就finish当前的页面
     */
    @Override
    public void onBackPressed() {
        if (mIsDragLayoutOpen) {
            mDragLayout.close();
        } else {
            super.onBackPressed();
        }
    }

    public DragLayout getDragLayout() {
        return mDragLayout;
    }

    /**
     * 注册“版本更新”广播
     */
    private void registerBroadcastReceiver() {
        mUpdateBroadcastReceiver = new UpdateBroadcastReceiver();
        mIntentFilter = new IntentFilter(UpdateBroadcastReceiver.UPDATE_ACTION);
        this.registerReceiver(mUpdateBroadcastReceiver, mIntentFilter);
    }

    /**
     * 注销“版本更新”广播
     */
    private void unregisterBroadcastReceiver() {
        this.unregisterReceiver(mUpdateBroadcastReceiver);
    }
}
