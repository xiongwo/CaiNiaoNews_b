package com.example.liuyuhua.cainiaonews.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.R;

/**
 * Created by liuyuhua on 2017/4/29.
 */

public class FindInvestorRecyclerItemViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private ImageView mImageView;
    private TextView nameText;
    private TextView orgText;
    private TextView positionText;
    private TextView industryText;

    public FindInvestorRecyclerItemViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImageView = (ImageView) mView.findViewById(R.id.iv_item_find_investor);
        nameText = (TextView) mView.findViewById(R.id.tv_item_find_investor_name);
        orgText = (TextView) mView.findViewById(R.id.tv_item_find_investor_org);
        positionText = (TextView) mView.findViewById(R.id.tv_item_find_investor_position);
        industryText = (TextView) mView.findViewById(R.id.tv_item_find_investor_industry);
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

    public TextView getNameText() {
        return nameText;
    }

    public void setNameText(TextView nameText) {
        this.nameText = nameText;
    }

    public TextView getOrgText() {
        return orgText;
    }

    public void setOrgText(TextView orgText) {
        this.orgText = orgText;
    }

    public TextView getPositionText() {
        return positionText;
    }

    public void setPositionText(TextView positionText) {
        this.positionText = positionText;
    }

    public TextView getIndustryText() {
        return industryText;
    }

    public void setIndustryText(TextView industryText) {
        this.industryText = industryText;
    }
}
