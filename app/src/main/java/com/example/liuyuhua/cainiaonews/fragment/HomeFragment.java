package com.example.liuyuhua.cainiaonews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidlib.adapter.BaseAdapterHelper;
import com.example.androidlib.adapter.BaseQuickAdapter;
import com.example.androidlib.adapter.QuickAdapter;
import com.example.androidlib.network.NetworkUtils;
import com.example.androidlib.utils.InterfaceDiskCache;
import com.example.androidlib.utils.SharedPreferencesHelper;
import com.example.androidlib.utils.VisibilityManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.ArticleDetailActivity;
import com.example.liuyuhua.cainiaonews.biz.HeadGalleryDataManager;
import com.example.liuyuhua.cainiaonews.biz.ListNewsDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.CategoryBean;
import com.example.liuyuhua.cainiaonews.entity.HeadGalleryBean;
import com.example.liuyuhua.cainiaonews.entity.ListNewsBean;
import com.example.androidlib.fragment.BaseFragment;
import com.example.androidlib.network.OkHttpManager;
import com.example.androidlib.widget.AutoGallery;
import com.example.androidlib.widget.FlowIndicator;
import com.example.androidlib.widget.PullToRefreshListView;
import com.example.liuyuhua.cainiaonews.utils.KeyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;

/**
 * “最新文章”
 * Created by liuyuhua on 2017/2/21.
 */

public class HomeFragment extends BaseFragment implements DefineView {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String KEY = "EXTRA";

    private View mView;
    private CategoryBean mCategoriesBean;
    private PullToRefreshListView mPullToRefreshListView;
    private View mGalleryView;
    private AutoGallery mAutoGallery;
    private FlowIndicator mIndicator;
    private LayoutInflater mLayoutInflater;

    private VisibilityManager mVisibilityManager;
    private FrameLayout mFrameLayout;
    private LinearLayout mLoadingLayout;
    private LinearLayout mLoadEmptyLayout;
    private LinearLayout mLoadErrorLayout;

    private List<ListNewsBean> mListNewsBeanList;
    private List<HeadGalleryBean> mHeadGalleryBeanList;
    private QuickAdapter<ListNewsBean> mQuickAdapter;
    private int mGallerySelectedPosition = 0; // 第一个 item 的 position
    private int mCircleSelectedPosition = 0; // FlowIndicator:指示图片的位置

    private ImageLoader mImageLoader; // UIL
    private int mLastItemPosition;
    private boolean mIsMore = true;

    private boolean isRequestSuccessful;

    private Handler mRefreshHandler = new Handler();


    public static HomeFragment newInstance(CategoryBean extra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, extra);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategoriesBean = (CategoryBean) bundle.getSerializable(KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home_layout, container, false);
            mLayoutInflater = LayoutInflater.from(getActivity());
            mGalleryView = mLayoutInflater.inflate(R.layout.layout_auto_gallery, null); // 用传进来的 inflater 会出问题！！

            initializeView();
            initializeValidData();
            initializeListener();
        }
        return mView;
    }

    @Override
    public void initializeView() {
        mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.lv_fragment_home);
        mPullToRefreshListView.addHeaderView(mGalleryView);

        mAutoGallery = (AutoGallery) mGalleryView.findViewById(R.id.auto_gallery);
        mIndicator = (FlowIndicator) mGalleryView.findViewById(R.id.flow_indicator);

        mFrameLayout = (FrameLayout) mView.findViewById(R.id.root_load_status_fl);
        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.layout_loading);
        mLoadEmptyLayout = (LinearLayout) mView.findViewById(R.id.layout_load_empty);
        mLoadErrorLayout = (LinearLayout) mView.findViewById(R.id.layout_load_error);
        mVisibilityManager = new VisibilityManager(mPullToRefreshListView, mFrameLayout, mLoadingLayout, mLoadEmptyLayout, mLoadErrorLayout);

        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initializeValidData() {
        mVisibilityManager.setLoadingViewVisible();

        getData();
    }

    @Override
    public void initializeListener() {
        // 下拉刷新
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 理想情况应该是服务器与本地的标志比较
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullToRefreshListView.onRefreshComplete();
                                Toast.makeText(getActivity(), "下拉刷新成功", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000);
                    }
                }).start();
            }
        });

        // 上拉加载
        mPullToRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mLastItemPosition - 3 + 1 == mListNewsBeanList.size() && mIsMore) {
                    mIsMore = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 如：完整地址是 "http://36kr.com/api/info-flow/main_site/posts?column_id=&b_id=5070814&per_page=20"
                            String newHref = "http://36kr.com/api/info-flow/main_site/posts?column_id=&b_id=" + mListNewsBeanList.get(mListNewsBeanList.size() - 1).getId()
                                    + "&" + "per_page=20";
                            Log.d(TAG, "newHref: " + newHref);
                            OkHttpManager.getAsync(newHref, new OkHttpManager.DataCallBack() {
                                @Override
                                public void requestFailure(Request request, IOException e) {
                                    Log.d(TAG, "最新文章加载失败！");
                                    Toast.makeText(getActivity(), "上拉加载更多失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void requestSuccess(String result) {
                                    List<ListNewsBean> newListNewsBean = ListNewsDataManager.getListNewsBeanList(result);
                                    mListNewsBeanList.addAll(newListNewsBean);
                                    mQuickAdapter.addAll(mListNewsBeanList);
                                }
                            });
                            mIsMore = true;
                        }
                    }, 3000);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mLastItemPosition = mPullToRefreshListView.getLastVisiblePosition();
                Log.d(TAG, "mLastItemPosition: " + mLastItemPosition);
            }
        });
    }

    @Override
    public void bindData() {

        if (mHeadGalleryBeanList != null) {
            final int topSize = mHeadGalleryBeanList.size();
            mGallerySelectedPosition = topSize * 50 + mCircleSelectedPosition;
            mAutoGallery.setLength(topSize);
            mAutoGallery.setSelection(mGallerySelectedPosition);
            mAutoGallery.setDelayMillis(4000);
            mAutoGallery.setAdapter(new GalleryAdapter());
            mAutoGallery.start();
            mAutoGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mGallerySelectedPosition = position;
                    mCircleSelectedPosition = mGallerySelectedPosition % topSize;
                    mIndicator.setSeletion(mCircleSelectedPosition);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mIndicator.setCount(topSize);
            mIndicator.setSeletion(mCircleSelectedPosition);
        }

        if (mListNewsBeanList != null) {

            mVisibilityManager.setTargetViewVisible();

            mQuickAdapter = new QuickAdapter<ListNewsBean>(getActivity(), R.layout.item_main_news_layout, mListNewsBeanList) {
                @Override
                protected void convert(BaseAdapterHelper helper, ListNewsBean item) {
                    helper.setText(R.id.tv_author, item.getUser().getName()).
                            setText(R.id.tv_post_time, item.getRelativePublishTime()).
                            setText(R.id.tv_label, item.getColumn().getName()).
                            setTextColor(R.id.tv_label, Color.parseColor(item.getColumn().getBg_color())).
                            setBackgroundColor(R.id.view_label, Color.parseColor(item.getColumn().getBg_color())).
                            setText(R.id.tv_title, item.getTitle());

                    mImageLoader.displayImage(item.getUser().getAvatar_url(), (ImageView)helper.getView(R.id.iv_author));
                    mImageLoader.displayImage(item.getCover(), (ImageView)helper.getView(R.id.iv_cover));
                }
            };
            // 点击item
            mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(Object item) {
                    ListNewsBean listNewsBean = (ListNewsBean) item; // 硬编码，强转
                    String detailHref = listNewsBean.getHref();
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    intent.putExtra(KeyUtils.INTENT_EXTRA_DETAIL, detailHref);
                    getActivity().startActivity(intent);
                }
            });
            mPullToRefreshListView.setAdapter(mQuickAdapter);
        } else {
            mVisibilityManager.setEmptyViewVisible();
        }
    }

    /**
     * 顶部轮播栏的适配器
     */
    private class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object getItem(int position) {
            return mHeadGalleryBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GalleryHolder galleryHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_gallery_layout, null);
                galleryHolder = new GalleryHolder();
                galleryHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_gallery);
                convertView.setTag(galleryHolder);
            } else {
                galleryHolder = (GalleryHolder) convertView.getTag();
            }
            mImageLoader.displayImage(mHeadGalleryBeanList.get(position % mHeadGalleryBeanList.size()).getImageUrl(), galleryHolder.imageView);
//            Picasso.with(getContext()).load(mHeadGalleryBeanList.get(position % mHeadGalleryBeanList.size()).getImageUrl()).into(galleryHolder.imageView);
//            galleryHolder.imageView.setImageResource(R.drawable.beauty);
            return convertView;
        }
    }

    private class GalleryHolder {
        ImageView imageView;
    }

    /**
     * 从SharedPreferences中获取数据
     *
     * 判断顺序：
     * 1.网络是否已经连接
     *  1.1 已经连接，判断缓存时间是否过期
     *   1.1.1 已经过期
     *   1.1.2 还没过期，或不存在
     *    1.1.2.1 不存在
     *    1.1.2.2 还没过期
     *  1.2 还没连接，判断缓存是否存在
     *   1.2.1 存在
     *   1.2.2 不存在
     *
     * 操作：
     * 1.1.1 已经过期：请求服务器，并保存；若失败，则加载过期数据
     * 1.1.2.1 不存在：请求服务器，并保存
     * 1.1.2.2 还没过期：从SharedPreferences中读取
     * 1.2.1 存在：从SharedPreferences中读取
     * 1.2.2 不存在：
     */
    private void getData() {

        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(getContext(), mCategoriesBean.getSharedName());

        String key = mCategoriesBean.getHref();
        String value =  helper.getStringValue(key);

        // 判断是否有网络已经连接
        if (NetworkUtils.isNetworkConnected(getContext())) {
            Log.d(TAG, "已经连接网络");

            // 根据接口设置的缓存时间，判断缓存是否过期；true为过期，false为缓存不存在或者还没有过期
            if (InterfaceDiskCache.isCacheOutOfDate(getContext(), mCategoriesBean.getSharedName(), mCategoriesBean.getHref() + "/time", mCategoriesBean.getExpires())) {
                Log.d(TAG, "文本数据已经过期");

                // 清除过期数据
                helper.remove(key).remove(key + "/time").apply();

                // 请求服务器
                if (!requestServer(helper, key)) {
                    // 若请求失败，则显示已经过期的数据
                    mListNewsBeanList = ListNewsDataManager.getListNewsBeanList(value);
                    mHeadGalleryBeanList = HeadGalleryDataManager.getHeadGalleryBeanList(mListNewsBeanList);
                    bindData();
                }
            } else {
                Log.d(TAG, "文本数据不存在或没过期");

                if (value == null) {
                    Log.d(TAG, "文本数据不存在");

                    // 请求服务器
                    requestServer(helper, key);
                } else {
                    Log.d(TAG, "文本数据没过期");

                    // 生成实体类对象，并绑定数据
                    mListNewsBeanList = ListNewsDataManager.getListNewsBeanList(value);
                    mHeadGalleryBeanList = HeadGalleryDataManager.getHeadGalleryBeanList(mListNewsBeanList);
                    bindData();
                }
            }
        } else {
            Log.d(TAG, "还没连接网络");

            // 缓存存在，则读取
            if (value != null) {
                // 生成实体类对象，并绑定数据
                mListNewsBeanList = ListNewsDataManager.getListNewsBeanList(value);
                bindData();
            }
        }
    }

    /**
     * 请求服务器
     * @param helper
     * @param key
     * @return
     */
    private boolean requestServer(final SharedPreferencesHelper helper, final String key) {
        OkHttpManager.getAsync(mCategoriesBean.getHref(), new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "请求失败！");
                mVisibilityManager.setErrorViewVisible();
                isRequestSuccessful = false;
            }

            @Override
            public void requestSuccess(String result) {
                Log.d(TAG, "请求成功！");
                isRequestSuccessful = true;

                // 以接口地址作为key，将返回的json数据和当前时间保存到对应的SharedPreferences中
                String hrefTime = key + "/time";
                helper.putStringValue(key, result).putLongValue(hrefTime, System.currentTimeMillis()).apply();

                // 生成实体对象，绑定数据
                mListNewsBeanList = ListNewsDataManager.getListNewsBeanList(result);
                mHeadGalleryBeanList = HeadGalleryDataManager.getHeadGalleryBeanList(mListNewsBeanList);
                bindData();
            }
        });
        return isRequestSuccessful;
    }
}
