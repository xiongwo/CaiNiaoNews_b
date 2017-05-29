package com.example.liuyuhua.cainiaonews.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuyuhua.cainiaonews.R;

/**
 * Created by liuyuhua on 2017/4/28.
 */

public class RecyclerItemListViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private ImageView imageView;
    private TextView titleText;
    private TextView summaryText;
    private TextView authorText;
    private TextView postTimeText;
    private TextView extractionOneText;
    private TextView extractionTwoText;

    public RecyclerItemListViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        imageView = (ImageView) view.findViewById(R.id.iv_page_cover);
        titleText = (TextView) view.findViewById(R.id.tv_page_title);
        summaryText = (TextView) view.findViewById(R.id.tv_page_summary);
        authorText = (TextView) view.findViewById(R.id.tv_page_author);
        postTimeText = (TextView) view.findViewById(R.id.tv_page_post_time);
        extractionOneText = (TextView) view.findViewById(R.id.tv_page_extraction_one);
        extractionTwoText = (TextView) view.findViewById(R.id.tv_page_extraction_two);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(TextView summaryText) {
        this.summaryText = summaryText;
    }

    public TextView getAuthorText() {
        return authorText;
    }

    public void setAuthorText(TextView authorText) {
        this.authorText = authorText;
    }

    public TextView getPostTimeText() {
        return postTimeText;
    }

    public void setPostTimeText(TextView postTimeText) {
        this.postTimeText = postTimeText;
    }

    public TextView getExtractionOneText() {
        return extractionOneText;
    }

    public void setExtractionOneText(TextView extractionOneText) {
        this.extractionOneText = extractionOneText;
    }

    public TextView getExtractionTwoText() {
        return extractionTwoText;
    }

    public void setExtractionTwoText(TextView extractionTwoText) {
        this.extractionTwoText = extractionTwoText;
    }
}
