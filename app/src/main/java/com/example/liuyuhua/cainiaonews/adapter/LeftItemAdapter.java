package com.example.liuyuhua.cainiaonews.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.application.CNKApplication;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.entity.LeftItemMenu;
import com.example.liuyuhua.cainiaonews.utils.LeftMenuDataUtils;

import java.util.List;

/**
 * Created by liuyuhua on 2016/12/2.
 */

public class LeftItemAdapter extends BaseAdapter {

    public LayoutInflater mLayoutInflater;
    private List<LeftItemMenu> mLeftItemMenus;

    public LeftItemAdapter() {
        mLayoutInflater = LayoutInflater.from(CNKApplication.getInstance());
        mLeftItemMenus = LeftMenuDataUtils.getItemMenus();
    }

    @Override
    public int getCount() {
        return mLeftItemMenus != null ? mLeftItemMenus.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mLeftItemMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_main_left_layout, null);
            viewHolder.left_item_image_view = (ImageView) convertView.findViewById(R.id.left_item_image_view);
            viewHolder.left_item_text_view = (TextView) convertView.findViewById(R.id.left_item_text_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.left_item_image_view.setImageResource(mLeftItemMenus.get(position).getLeftIcon());
        viewHolder.left_item_text_view.setText(mLeftItemMenus.get(position).getTitle());

        return convertView;
    }

    private static class ViewHolder{
        ImageView left_item_image_view;
        TextView left_item_text_view;
    }
}
