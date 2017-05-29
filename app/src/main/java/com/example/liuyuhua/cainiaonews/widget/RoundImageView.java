package com.example.liuyuhua.cainiaonews.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.liuyuhua.cainiaonews.R;

/** 圆形头像（法2）
 * 在 xml 中已经添加了 src
 * Created by liuyuhua on 2017/3/26.
 */

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {

    // 起始的默认值相当于 xml 中的 dp 值。但修改后就相当于 px 值
    private int mRoundWidth = 10;
    private int mRoundHeight = 10;
    private Paint mPaint;
    private Paint mPaint2;

    // 用代码动态创建时调用
    public RoundImageView(Context context) {
        super(context);
        initialize(context, null);
    }

    // 在 xml 中布置了的情况下调用
    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    // 1、用代码动态添加 style 属性时，从这里传给父类
    // 2、如果在 xml 中添加的话，就会从上一个构造方法中的 AttributeSet 提取
    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        /**
         * 1、屏幕尺寸：指的是手机屏幕对角线的长度，单位为英寸
         * 2、Dots Per Inch（简称dpi）像素每英寸/每英寸点数：以mdpi--160dpi为基准
         * 3、变量density：是一个比值，其值近似为：实际屏幕的dpi / 160
         * 4、Density Independent Pixel(dip/dp)：密度独立像素
         * 5、px = dp * (dpi / 160)
         */

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            mRoundWidth = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_roundWidth, mRoundWidth);
            mRoundHeight = typedArray.getDimensionPixelSize(R.styleable.RoundImageView_roundHeight, mRoundHeight);
            typedArray.recycle();
        } else {
            float density = context.getResources().getDisplayMetrics().density;
            mRoundWidth = (int) (mRoundWidth * density);
            mRoundHeight = (int) (mRoundHeight * density);
        }
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE); // 其实看不见这个颜色，而且会在下面的混合模式中消失掉
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));// 源图sc以及目标图dis与源图重叠的地方消失

        mPaint2 = new Paint();
        mPaint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {

        /**
         * 原理（猜测）：
         * 先创建了一个空的画布，然后在 super 中完成正常情况下的所有操作，也就是把 xml 中布置的图片
         * 给绘制出来了，最后调用 drawLeftUp 等方法，实现对图片的遮盖。
         * 但要最终显示正常的话，需要用传递下来的 canvas 绘制。对此我的猜测是：传递下来的 canvas 才能
         * 最终显示到屏幕上。
         *
         * 为什么 drawBitmap 是用之前那个 bitmap ？
         * A Bitmap to hold the pixels; Specifies a mutable bitmap for the canvas to draw into.
         */

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        super.draw(canvas1);

        // 要 mRoundWidth == getWidth / 2，mRoundHeight 也是一样，才会是圆形
        drawLeftUp(canvas1);
        drawRightUp(canvas1);
        drawLeftDown(canvas1);
        drawRightDown(canvas1);

        canvas.drawBitmap(bitmap, 0, 0, mPaint2);
        bitmap.recycle(); // 回收
    }

    // 左上
    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, mRoundHeight);
        path.lineTo(0, 0);
        path.lineTo(mRoundWidth, 0);
        // 3点钟方向为0°，顺时针方向为正，逆时针方向为负
        path.arcTo(new RectF(0, 0, mRoundWidth * 2, mRoundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    // 左下
    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - mRoundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(mRoundWidth, getHeight());
        path.arcTo(new RectF(0, getHeight() - mRoundHeight * 2, mRoundWidth * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    // 右上
    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), mRoundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - mRoundWidth, 0);
        path.arcTo(new RectF(getWidth() - mRoundWidth * 2, 0, getWidth(), mRoundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    // 右下
    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - mRoundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - mRoundHeight);
        path.arcTo(new RectF(getWidth() - mRoundWidth * 2, getHeight() - mRoundHeight * 2, getWidth(), getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
