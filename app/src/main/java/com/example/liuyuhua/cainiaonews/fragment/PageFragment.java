package com.example.liuyuhua.cainiaonews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.androidlib.adapter.QuickAdapter;
import com.example.androidlib.cache.DiskLruCache;
import com.example.androidlib.cache.DiskLruCacheManager;
import com.example.androidlib.network.NetworkOperator;
import com.example.androidlib.network.NetworkUtils;
import com.example.androidlib.utils.InterfaceDiskCache;
import com.example.androidlib.utils.SharedPreferencesHelper;
import com.example.androidlib.utils.VisibilityManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.ArticleDetailActivity;
import com.example.liuyuhua.cainiaonews.adapter.RecyclerAdapter;
import com.example.liuyuhua.cainiaonews.biz.ListNewsDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.CategoryBean;
import com.example.liuyuhua.cainiaonews.entity.ListNewsBean;
import com.example.androidlib.network.OkHttpManager;
import com.example.androidlib.widget.RecyclerItemDecoration;
import com.example.androidlib.fragment.BaseFragment;
import com.example.liuyuhua.cainiaonews.utils.KeyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.List;

import okhttp3.Request;

/**
 * 新闻分类中，除了“最新文章”
 * Created by liuyuhua on 2016/12/2.
 */

public class PageFragment extends BaseFragment implements DefineView{

    private static final String TAG = PageFragment.class.getSimpleName();
    private static final String KEY = "EXTRA";
    private View mView;
    private CategoryBean mCategoriesBean;
    private RecyclerView mRecyclerView;

    private List<ListNewsBean> mListNewsBeanList;
    private QuickAdapter<ListNewsBean> mQuickAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerAdapter mRecyclerAdapter;
    private ImageLoader mImageLoader;
    private RecyclerItemDecoration mRecyclerItemDecoration;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mLastItemPosition;
    private boolean mIsMore = true;

    private boolean isRequestSuccessful;

    public static PageFragment newInstance(CategoryBean extra) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, extra);
        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            mView = inflater.inflate(R.layout.fragment_page_layout, container, false);
            initializeView();
            initializeValidData();
            initializeListener();
        }
        return mView;
    }

    @Override
    public void initializeView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_fragment_page);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_fragment_page);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerAdapter = new RecyclerAdapter(getActivity(), RecyclerAdapter.item_type_list);
        mRecyclerItemDecoration = new RecyclerItemDecoration(getActivity());

        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initializeValidData() {

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(android.R.color.white));
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, getResources().getDisplayMetrics()));

        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(mRecyclerItemDecoration);

        getData();
    }

    @Override
    public void initializeListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        Toast.makeText(getActivity(), "下拉刷新成功", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });

//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemPosition + 1 == mLinearLayoutManager.getItemCount() && mIsMore) {
//                    mIsMore = false;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getActivity(), "下拉加载成功", Toast.LENGTH_SHORT).show();
//                            mIsMore = true;
//                        }
//                    }, 3000);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mLastItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
//            }
//        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastItemPosition + 1 == mLinearLayoutManager.getItemCount() && mIsMore) {
                    mIsMore = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 如： mCategoriesBean.getHref() = "http://36kr.com/api/post?column_id=23&b_id=&per_page=20"
                            String[] newHrefs = mCategoriesBean.getHref().split("&", 3);
                            // 如：完整地址是 "http://36kr.com/api/post?column_id=23&b_id=5070631&per_page=20"
                            // newHref: "http://36kr.com/api/post?column_id=23" + "&" + "b_id=" + "50706310" + "&" + "per_page=20"
                            String newHref = newHrefs[0] + "&" + newHrefs[1] + mListNewsBeanList.get(mListNewsBeanList.size() - 1).getId()
                                    + "&" + newHrefs[2];
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
                                    mRecyclerAdapter.notifyDataSetChanged();
                                }
                            });
                            mIsMore = true;
                        }
                    }, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        mRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, ListNewsBean listNewsBean) {
                String detailHref = listNewsBean.getHref();
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                intent.putExtra(KeyUtils.INTENT_EXTRA_DETAIL, detailHref);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void bindData() {
        if (mListNewsBeanList != null) {

            mRecyclerAdapter.setListNewsBeanList(mListNewsBeanList);
            mRecyclerAdapter.setImageLoader(mImageLoader);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        }
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
                bindData();
            }
        });
        return isRequestSuccessful;
    }
}
