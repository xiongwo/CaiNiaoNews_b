package com.example.liuyuhua.cainiaonews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidlib.cache.DiskLruCache;
import com.example.androidlib.cache.DiskLruCacheManager;
import com.example.androidlib.cache.LruCacheManager;
import com.example.androidlib.network.NetworkOperator;
import com.example.androidlib.utils.BitmapDecode;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.entity.FindInvestorBean;
import com.example.liuyuhua.cainiaonews.entity.ListNewsBean;
import com.example.liuyuhua.cainiaonews.entity.RecentActivityBean;
import com.example.liuyuhua.cainiaonews.holder.FindInvestorRecyclerItemViewHolder;
import com.example.liuyuhua.cainiaonews.holder.RecyclerItemCardViewHolder;
import com.example.liuyuhua.cainiaonews.holder.RecyclerItemListViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * “分类新闻”、“近期活动”、“寻找投资人”
 * Created by liuyuhua on 2017/4/14.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";

    public static int item_type_list = 0; // 分类新闻
    public static int item_type_card = 1; // 近期活动
    public static int item_type_find_investor = 2; // 寻找投资人
    private static final int TYPE_FOOTER = 3;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ListNewsBean> mListNewsBeanList;
    private ImageLoader mImageLoader;
    private OnItemClickListener mOnItemClickListener;
    private int mItemType;
    private List<RecentActivityBean> mRecentActivityBeanList;
    private List<FindInvestorBean> mFindInvestorBeanList;

    public RecyclerAdapter(Context context, int itemType) {
        mContext = context;
        mItemType = itemType;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (mItemType == item_type_list) {
            return item_type_list;
        }else if (mItemType == item_type_card) {
            return item_type_card;
        } else if (mItemType == item_type_find_investor) {
            return item_type_find_investor;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == item_type_list) {
            View itemView = mLayoutInflater.inflate(R.layout.item_page_news_layout, parent, false);
            RecyclerItemListViewHolder listViewHolder = new RecyclerItemListViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, (ListNewsBean) v.getTag());
                    }
                }
            });
            return listViewHolder;
        } else if (viewType == item_type_card) {
            View itemView = mLayoutInflater.inflate(R.layout.item_recent_activity, parent, false);
            RecyclerItemCardViewHolder cardViewHolder = new RecyclerItemCardViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(v, (ListNewsBean) v.getTag());
                    }
                }
            });
            return cardViewHolder;
        } else if (viewType == item_type_find_investor) {
            View itemView = mLayoutInflater.inflate(R.layout.item_find_investor, parent, false);
            FindInvestorRecyclerItemViewHolder findInvestorViewHolder = new FindInvestorRecyclerItemViewHolder(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(v, (ListNewsBean) v.getTag());
                    }
                }
            });
            return findInvestorViewHolder;
        }else {
            View footerView = mLayoutInflater.inflate(R.layout.footer_load_more, parent, false);
            RecyclerFooterViewHolder recyclerFooterViewHolder = new RecyclerFooterViewHolder(footerView);
            return recyclerFooterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerItemListViewHolder) {
            RecyclerItemListViewHolder itemHolder = (RecyclerItemListViewHolder) holder;
            if (mListNewsBeanList != null) {
                ListNewsBean listNewsBean = mListNewsBeanList.get(position);
                itemHolder.getView().setTag(listNewsBean);
                if (mImageLoader != null) {
                    mImageLoader.displayImage(listNewsBean.getCover(), itemHolder.getImageView());
                }

                itemHolder.getTitleText().setText(listNewsBean.getTitle());
                itemHolder.getSummaryText().setText(listNewsBean.getSummary());
                itemHolder.getAuthorText().setText(listNewsBean.getUser().getName());
                itemHolder.getPostTimeText().setText(listNewsBean.getRelativePublishTime());
            }
        } else if (holder instanceof RecyclerItemCardViewHolder) {
            RecyclerItemCardViewHolder cardViewHolder = (RecyclerItemCardViewHolder) holder;
            if (mRecentActivityBeanList != null) {
                RecentActivityBean recentActivityBean = mRecentActivityBeanList.get(position);
                cardViewHolder.getView().setTag(recentActivityBean);
                if (mImageLoader != null) {
                    mImageLoader.displayImage(recentActivityBean.getListImageUrl(), cardViewHolder.getImageView());
                }
                cardViewHolder.getTitleText().setText(recentActivityBean.getTitle());
                cardViewHolder.getCityText().setText(recentActivityBean.getCity());
                cardViewHolder.getDateText().setText(recentActivityBean.getBeginTime() + " - " + recentActivityBean.getEndTime());
            }
        } else if (holder instanceof FindInvestorRecyclerItemViewHolder) {
            FindInvestorRecyclerItemViewHolder findInvestorViewHolder = (FindInvestorRecyclerItemViewHolder) holder;
            if (mFindInvestorBeanList != null) {
                FindInvestorBean findInvestorBean = mFindInvestorBeanList.get(position);
                findInvestorViewHolder.getView().setTag(findInvestorBean);
                if (mImageLoader != null) {
                    mImageLoader.displayImage(findInvestorBean.getLogo(), findInvestorViewHolder.getImageView());
                }
                findInvestorViewHolder.getNameText().setText(findInvestorBean.getName());
                findInvestorViewHolder.getOrgText().setText(findInvestorBean.getOrgName());
                findInvestorViewHolder.getPositionText().setText(findInvestorBean.getPosition());
                String industry = "";
                for (String ins : findInvestorBean.getIndustry()) {
                    industry += ins;
                }
                findInvestorViewHolder.getIndustryText().setText(industry);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mItemType == item_type_list) {
            return mListNewsBeanList == null ? 0 : mListNewsBeanList.size() + 1;
        } else if (mItemType == item_type_card){
            return mRecentActivityBeanList == null ? 0 : mRecentActivityBeanList.size() + 1;
        } else if (mItemType == item_type_find_investor) {
            return mFindInvestorBeanList == null ? 0 : mFindInvestorBeanList.size() + 1;
        }
        return 0;
    }

    // 分类新闻的数据
    public void setListNewsBeanList(List<ListNewsBean> listNewsBeanList) {
        mListNewsBeanList = listNewsBeanList;
    }

    // 近期活动的数据
    public void setRecentActivityBeanList(List<RecentActivityBean> recentActivityBeanList) {
        mRecentActivityBeanList = recentActivityBeanList;
    }

    // 寻找投资人的数据
    public void setFindInvestorBeanList(List<FindInvestorBean> findInvestorBeanList) {
        mFindInvestorBeanList = findInvestorBeanList;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    /**
     * “上拉加载更多”的视图
     */
    private class RecyclerFooterViewHolder extends RecyclerView.ViewHolder {

        public RecyclerFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * Item 点击回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(View itemView, ListNewsBean listNewsBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
