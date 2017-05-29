package com.example.liuyuhua.cainiaonews.activity.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.R;

import java.lang.reflect.Field;

/**
 * Created by liuyuhua on 2016/11/19.
 */

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 去掉 ActionBar，实现沉浸式状态栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 在Nexus5上运行下面的导航栏会变透明，但欢迎页面的按钮也会下移，导致按钮和导航栏部分重叠
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    // 布置自定义导航栏
    // 因为状态栏设置为透明后，导航栏的位置会上移，上面的一部分会与透明的状态栏重叠，
    // 所以导航栏的实际高度应是 xml 中设置的高度加上状态栏的高度
    protected void setStatusBar(int topBarRootLayoutId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final ViewGroup viewTitle = (ViewGroup) findViewById(topBarRootLayoutId);
            final int statusHeight = getStatusHeight();
            viewTitle.post(new Runnable() {
                @Override
                public void run() {
                    int titleHeight = viewTitle.getHeight();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewTitle.getLayoutParams();
                    layoutParams.height = statusHeight + titleHeight;
                    viewTitle.setLayoutParams(layoutParams);
                }
            });
        }
    }

    // 通过 Java 的反射机制来获取状态栏的高度
    protected int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object object = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(object).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        this.startActivity(intent);
//        this.finish(); // 不能返回、后退
    }

    protected void setTopBarTitle(int tvId, String title) {
        TextView textView = (TextView) findViewById(tvId);
        textView.setText(title);
    }
}
