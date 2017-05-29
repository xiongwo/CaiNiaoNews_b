package com.example.liuyuhua.cainiaonews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by liuyuhua on 2016/11/19.
 */

public class CustomRelativeLayout extends RelativeLayout {

    private DragLayout mDragLayout;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDragLayout(DragLayout dragLayout) {
        mDragLayout = dragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(DragLayout.TAG, "CustomRelativeLayout: onInterceptTouchEvent");
        if (mDragLayout.getStatus() != DragLayout.Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(DragLayout.TAG, "CustomRelativeLayout: onTouchEvent");
        if (mDragLayout.getStatus() != DragLayout.Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mDragLayout.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
