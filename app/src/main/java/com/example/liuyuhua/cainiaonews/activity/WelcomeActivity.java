package com.example.liuyuhua.cainiaonews.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.androidlib.utils.SharedPreferencesHelper;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.widget.CustomVideoView;

/**
 * 启动页面
 * Created by liuyuhua on 2016/11/19.
 */

public class WelcomeActivity extends BaseActivity implements DefineView{

    private final String TAG = "WelcomeActivity";
    private final String SHARED_NAME = "start_page";
    private final String SHARED_KEY = "isFirstStart";
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private Button mWelcomeButton;
    private CustomVideoView mCustomVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    public void initializeView() {
        mSharedPreferencesHelper = SharedPreferencesHelper.getInstance(WelcomeActivity.this, SHARED_NAME);

        mCustomVideoView = (CustomVideoView) findViewById(R.id.custom_video_view);
        mWelcomeButton = (Button) findViewById(R.id.btn_welcome);
    }

    @Override
    public void initializeValidData() {
        mCustomVideoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.kr36));
        mCustomVideoView.start();
    }

    @Override
    public void initializeListener() {
        // 是否首次打开应用
        if (mSharedPreferencesHelper.getBooleanValue(SHARED_KEY)) {
            Log.d(TAG, "首次打开应用，启动欢迎页");

            mCustomVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCustomVideoView.start();
                }
            });

            mWelcomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCustomVideoView.isPlaying()) {
                        mCustomVideoView.stopPlayback();
                        mCustomVideoView = null;
                    }

                    // 修改标志
                    mSharedPreferencesHelper.putBooleanValue(SHARED_KEY, false).apply();

                    openActivity(MainActivity.class);
                    finish();
                }
            });
        } else {
            Log.d(TAG, "非首次打开应用，直接跳转到主页");

            openActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void bindData() {

    }
}
