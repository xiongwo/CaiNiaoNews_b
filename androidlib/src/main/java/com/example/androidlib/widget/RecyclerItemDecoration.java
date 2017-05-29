package com.example.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 *  RecyclerView 的 Item 分割线
 * Created by liuyuhua on 2017/4/14.
 */

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    // 采用系统内置的风格，这里 sAttrs 的值实际只有一个
    private static final int[] sAttrs = new int[]{android.R.attr.listDivider};
    private Drawable mDrawable;

    public RecyclerItemDecoration(Context context) {
        Log.i("666", String.valueOf(sAttrs.length));
        TypedArray typedArray = context.obtainStyledAttributes(sAttrs);
        mDrawable = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i <childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            // layout_marginBottom > 0 ，childView 会在 layout_marginBottom == 0 的时候的上面
            // 而分割线的 top 位置应该在 layout_marginBottom == 0 时，childView.getBottom() 的地方
            int top = childView.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mDrawable.getIntrinsicHeight();
            mDrawable.setBounds(left, top, right, bottom); // 相当于设置位置
            mDrawable.draw(c);
        }
    }

    /**
     *  getItemOffsets 中为 outRect 设置的4个方向的值，将被计算进所有 decoration 的尺寸中，而这个
     *  尺寸，又被计入了 RecyclerView 每个 item view 的 padding 中
     *  但还是不理解为什么要这么做？
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDrawable.getIntrinsicHeight());
    }
}
