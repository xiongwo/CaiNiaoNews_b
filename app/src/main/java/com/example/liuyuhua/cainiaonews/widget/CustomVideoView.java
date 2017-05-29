package com.example.liuyuhua.cainiaonews.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.VideoView;

/**
 * Created by liuyuhua on 2016/11/19.
 */

public class CustomVideoView extends VideoView {

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 系统的VideoView即使是match_parent，在播放这个视频时，不会占满全屏正常播放
        // 这里重新测量，解决这个问题
        // 为什么这样做就可以了？应该还与这个mp4文件有关
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        // 调用setMeasuredDimensionRaw方法，该方法是将测量的宽、高存储起来，也就是这里的width、height
        Log.i("CustomVideoView", "width: " + width);
        Log.i("CustomVideoView", "height: " + height);
        setMeasuredDimension(width, height);
    }

    // 这样做有意义吗？
    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        super.setOnPreparedListener(l);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
