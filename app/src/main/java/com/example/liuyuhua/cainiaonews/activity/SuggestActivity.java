package com.example.liuyuhua.cainiaonews.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.androidlib.network.OkHttpManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.adapter.PopupSuggestListAdapter;
import com.example.liuyuhua.cainiaonews.common.DefineView;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;

/**
 * Created by liuyuhua on 2017/5/3.
 */

public class SuggestActivity extends BaseActivity implements DefineView {

    private PopupWindow mSuggestPopupWindow;
    private String[] mTypes;
    private TextView mTypeText;
    private LinearLayout mSelectLayout;
    private Button mCommitBtn;
    private EditText mContentEt;
    private EditText mContactEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        setStatusBar(R.id.root_top_bar_common_rl);
        setTopBarTitle(R.id.tv_top_bar_common_title, "意见反馈");

        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    public void initializeView() {
        mSelectLayout = (LinearLayout) findViewById(R.id.ll_activity_suggest_select);
        mTypeText = (TextView) findViewById(R.id.tv_activity_suggest_type);
        mCommitBtn = (Button) findViewById(R.id.btn_activity_suggest_commit);
        mContentEt = (EditText) findViewById(R.id.et_activity_suggest_content);
        mContactEt = (EditText) findViewById(R.id.et_activity_suggest_contact);
    }

    @Override
    public void initializeValidData() {
        mTypes = getResources().getStringArray(R.array.array_popup_suggest_type);
    }

    @Override
    public void initializeListener() {
        mSelectLayout.setOnClickListener(new PopupSuggestOnClickListener());
        mCommitBtn.setOnClickListener(new PopupSuggestOnClickListener());
    }

    @Override
    public void bindData() {

    }

    private void initPopupWindow() {
        mSuggestPopupWindow = new PopupWindow(this);
        mSuggestPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mSuggestPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window_suggest, null);
        ListView typeList = (ListView) contentView.findViewById(R.id.lv_popup_window_suggest);
        PopupSuggestListAdapter listAdapter = new PopupSuggestListAdapter(this, mTypes);
        typeList.setAdapter(listAdapter);
        typeList.setOnItemClickListener(new PopupSuggestOnItemClickListener());
        mSuggestPopupWindow.setContentView(contentView);
        mSuggestPopupWindow.setFocusable(true);
        mSuggestPopupWindow.setOutsideTouchable(true);
        mSuggestPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mSuggestPopupWindow.showAsDropDown(mTypeText); // 相对这个 view 的正左下方出现弹窗
    }

    private void commitSuggestion() {
        String suggest = mContentEt.getText().toString();
        String contact = mContactEt.getText().toString();
        // 1、对输入的内容进行规则判断
        // 2、提交数据到服务器上
        HashMap<String, String> map = new HashMap<>();
        map.put("", "");
        OkHttpManager.postAsync("", map, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {

            }

            @Override
            public void requestSuccess(String result) {

            }
        });
    }

    private class PopupSuggestOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_activity_suggest_select:
                    if (mSuggestPopupWindow == null) {
                        initPopupWindow();
                    } else {
                        if (mSuggestPopupWindow.isShowing()) {
                            mSuggestPopupWindow.dismiss();
                        } else {
                            mSuggestPopupWindow.showAsDropDown(mTypeText);
                        }
                    }
                    break;
                case R.id.btn_activity_suggest_commit:
                    commitSuggestion();
                    break;
            }
        }
    }

    private class PopupSuggestOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mTypeText.setText(mTypes[(int) id]);
            if (mSuggestPopupWindow != null) {
                mSuggestPopupWindow.dismiss();
            }
        }
    }
}
