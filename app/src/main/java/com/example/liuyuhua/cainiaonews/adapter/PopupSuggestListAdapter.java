package com.example.liuyuhua.cainiaonews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.R;

/**
 * Created by liuyuhua on 2017/5/3.
 */

public class PopupSuggestListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private String[] mTypes;

    public PopupSuggestListAdapter(Context context, String[] types) {
        mLayoutInflater = LayoutInflater.from(context);
        mTypes = types;
    }

    @Override
    public int getCount() {
        if (mTypes == null) {
            return 0;
        } else {
            return mTypes.length;
        }
    }

    @Override
    public Object getItem(int position) {
        return mTypes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_popup_suggest, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.typeText = (TextView) convertView.findViewById(R.id.tv_item_popup_suggest);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.typeText.setText(mTypes[position]);
        return convertView;
    }

    private class ViewHolder {
        TextView typeText;
    }
}
