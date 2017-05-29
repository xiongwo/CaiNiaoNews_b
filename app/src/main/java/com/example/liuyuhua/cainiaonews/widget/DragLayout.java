package com.example.liuyuhua.cainiaonews.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.liuyuhua.cainiaonews.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by liuyuhua on 2016/11/19.
 */

public class DragLayout extends FrameLayout {

    // 最大的问题：
    // 1、不理解为什么这些操作要在这个重写的方法里实现？（应该与View和ViewGroup的实现逻辑和事件分发机制有关）
    // 2、整个效果的实现逻辑是什么？

    // 1、每个方法的作用是什么？
    // 2、什么时候被调用？（进一步：被谁调用？）
    // 3、返回什么？

    // 1、了解事件的分发，如：按下去时，调用了哪些方法？
    // 2、布局的改变，重新布局的情况？
    // 打印Log来了解各个方法分别是在什么时候调用的？了解它们的执行顺序

    public final static String TAG = "TAG";
    private Context mContext;
    private GestureDetectorCompat mGestureDetectorCompat;
    private ViewDragHelper mViewDragHelper;
    private DragListener mDragListener;
    //
    private int mainLeft;
    // 水平拖拽的距离
    private int range;
    // 拖拽的子view能够在水平方向滑动的范围
    private int width;
    //
    private int height;
    private RelativeLayout vg_left;
    private CustomRelativeLayout vg_main;
    //
    private ImageView iv_shadow;
    private boolean isShowShadow = true;
    private Status status = Status.Close;
    private boolean isDrag = true;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetectorCompat = new GestureDetectorCompat(context, new YScrollDetector());
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i(TAG, "onScroll");
            return Math.abs(distanceY) <= Math.abs(distanceX) && isDrag;
        }
    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.i(TAG, "tryCaptureView");
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.i(TAG, "clampViewPositionHorizontal " + "mainLeft = " + mainLeft);
            if (mainLeft + dx < 0) {
                return 0;
            }else if (mainLeft + dx > range) {
                return range;
            }else {
                return left;
            }
        }

        /**
         * 设置水平方向滑动的最远距离
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            Log.i(TAG, "getViewHorizontalDragRange");
            return width;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.i(TAG, "onViewPositionChanged " + "mainLeft = " + mainLeft);
            if (changedView == vg_main) {
                mainLeft = left;
            } else {
                mainLeft = mainLeft + left;
            }

            if (mainLeft < 0) {
                mainLeft = 0;
            } else if (mainLeft > range) {
                mainLeft = range;
            }

            if (isShowShadow) {
                iv_shadow.layout(mainLeft, 0, mainLeft + width, height);
            }

            if (changedView == vg_left) {
                vg_left.layout(0, 0, width, height);
                vg_main.layout(mainLeft, 0, mainLeft + width, height);
            }

            dispatchDragEvent(mainLeft);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.i(TAG, "onViewReleased " + "mainLeft = " + mainLeft);
            if (xvel > 0) {
                open();
            } else if (xvel < 0) {
                close();
            } else if (releasedChild == vg_main && mainLeft > range * 0.3) {
                open();
            } else if (releasedChild == vg_left && mainLeft > range * 0.7) {
                open();
            } else {
                close();
            }
        }
    }

    public interface DragListener {
        void onOpen();
        void onClose();
        void onDrag(float percent);
    }
    public void setDragListener(DragListener dragListener) {
        mDragListener = dragListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "onFinishInflate");
        if (isShowShadow) {
            iv_shadow = new ImageView(mContext);
            iv_shadow.setImageResource(R.mipmap.shadow);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            addView(iv_shadow, 1, layoutParams);
        }
        vg_left = (RelativeLayout) getChildAt(0);
        vg_left.setClickable(true);
        vg_main = (CustomRelativeLayout) getChildAt(isShowShadow ? 2 : 1);
        vg_main.setDragLayout(this);
        vg_main.setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: w = " + w + ", " + "h = " + h + ", " + "oldw = " + oldw + ", " + "oldh = " + oldh );
        // 为什么要在这个方法里调用？
        width = vg_left.getMeasuredWidth();
        height = vg_left.getMeasuredHeight();
        range = (int) (width * 0.6f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout " + "mainLeft = " + mainLeft);
        vg_left.layout(0, 0, width, height);
        vg_main.layout(mainLeft, 0, mainLeft + width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "DragLayout: onInterceptTouchEvent");
        return mViewDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetectorCompat.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "DragLayout: onTouchEvent");
        mViewDragHelper.processTouchEvent(event);
        return false;
    }

    private void dispatchDragEvent(int mainLeft) {
        if (mDragListener == null) {
            return;
        }
        float percent = mainLeft / (float) range;
        animateView(percent);
        mDragListener.onDrag(percent);
        Status lastStatus = status;
        if (lastStatus != getStatus() && status == Status.Close) {
            mDragListener.onClose();
        } else if (lastStatus != getStatus() && status == Status.Open) {
            mDragListener.onOpen();
        }
    }

    /**
     * 根据滑动的距离的比例,进行带有动画的缩小和放大View
     * @param percent
     */
    private void animateView(float percent) {
        float f1 = 1 - percent * 0.3f;
        //vg_main水平方向 根据百分比缩放
        ViewHelper.setScaleX(vg_main, f1);
        //vg_main垂直方向，根据百分比缩放
        ViewHelper.setScaleY(vg_main, f1);
        //沿着水平X轴平移
        ViewHelper.setTranslationX(vg_left, -vg_left.getWidth() / 2.3f + vg_left.getWidth() / 2.3f * percent);
        //vg_left水平方向 根据百分比缩放
        ViewHelper.setScaleX(vg_left, 0.5f + 0.5f * percent);
        //vg_left垂直方向 根据百分比缩放
        ViewHelper.setScaleY(vg_left, 0.5f + 0.5f * percent);
        //vg_left根据百分比进行设置透明度
        ViewHelper.setAlpha(vg_left, percent);
        if (isShowShadow) {
            //阴影效果视图大小进行缩放
            ViewHelper.setScaleX(iv_shadow, f1 * 1.4f * (1 - percent * 0.12f));
            ViewHelper.setScaleY(iv_shadow, f1 * 1.85f * (1 - percent * 0.12f));
        }
        getBackground().setColorFilter(evaluate(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    @Override
    public void computeScroll() {
        Log.i(TAG, "computeScroll");
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public enum Status {
        Drag, Open, Close
    }

    public Status getStatus() {
        if (mainLeft == 0) {
            status = Status.Close;
        } else if (mainLeft == range) {
            status = Status.Open;
        } else {
            status = Status.Drag;
        }
        return status;
    }

    public void open() {
        open(true);
    }

    public void open(boolean animate) {
        if (animate) {
            if (mViewDragHelper.smoothSlideViewTo(vg_main, range, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(range, 0, range * 2, height);
            dispatchDragEvent(range);
        }
    }

    public void close() {
        close(true);
    }

    public void close(boolean animate) {
        if (animate) {
            if (mViewDragHelper.smoothSlideViewTo(vg_main, 0, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            vg_main.layout(0, 0, width, height);
            dispatchDragEvent(0);
        }
    }

    public ViewGroup getVg_main() {
        return vg_main;
    }

    public ViewGroup getVg_left() {
        return vg_left;
    }

    public boolean isDrag() {
        return isDrag;
    }

    public void setIsDrag(boolean isDrag) {
        this.isDrag = isDrag;
        if (this.isDrag) {
            mViewDragHelper.abort();
        }
    }
}
