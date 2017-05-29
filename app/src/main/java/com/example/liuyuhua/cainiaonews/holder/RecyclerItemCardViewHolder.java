package com.example.liuyuhua.cainiaonews.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.R;

/**
 * Created by liuyuhua on 2017/4/28.
 */

public class RecyclerItemCardViewHolder extends RecyclerView.ViewHolder{

    private View mView;
    private ImageView mImageView;
    private TextView titleText;
    private TextView cityText;
    private TextView statusText;
    private TextView dateText;

    public RecyclerItemCardViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImageView = (ImageView) mView.findViewById(R.id.iv_item_recent_activity_head);
        titleText = (TextView) mView.findViewById(R.id.tv_item_recent_activity_title);
        cityText = (TextView) mView.findViewById(R.id.tv_item_recent_activity_city);
        statusText = (TextView) mView.findViewById(R.id.tv_item_recent_activity_status);
        dateText = (TextView) mView.findViewById(R.id.tv_item_recent_activity_date);
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getCityText() {
        return cityText;
    }

    public void setCityText(TextView cityText) {
        this.cityText = cityText;
    }

    public TextView getStatusText() {
        return statusText;
    }

    public void setStatusText(TextView statusText) {
        this.statusText = statusText;
    }

    public TextView getDateText() {
        return dateText;
    }

    public void setDateText(TextView dateText) {
        this.dateText = dateText;
    }
}
