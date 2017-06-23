package com.example.liuyuhua.cainiaonews.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.androidlib.cache.DiskLruCache;
import com.example.androidlib.cache.DiskLruCacheManager;
import com.example.androidlib.cache.LruCacheManager;
import com.example.androidlib.network.NetworkOperator;
import com.example.androidlib.network.NetworkUtils;
import com.example.androidlib.network.OkHttpManager;
import com.example.androidlib.utils.InterfaceDiskCache;
import com.example.androidlib.utils.VisibilityManager;
import com.example.liuyuhua.cainiaonews.R;
import com.example.liuyuhua.cainiaonews.activity.base.BaseActivity;
import com.example.liuyuhua.cainiaonews.biz.ArticleDetailDataManager;
import com.example.liuyuhua.cainiaonews.biz.HeadGalleryDataManager;
import com.example.liuyuhua.cainiaonews.biz.ListNewsDataManager;
import com.example.liuyuhua.cainiaonews.common.DefineView;
import com.example.liuyuhua.cainiaonews.entity.ArticleDetailBean;
import com.example.liuyuhua.cainiaonews.utils.KeyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Request;

/**
 * “新闻详情”页面
 * Created by liuyuhua on 2017/4/18.
 */

public class ArticleDetailActivity extends BaseActivity implements DefineView{

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private Button mBackButton;
    private TextView mTitleTextView;
    private ImageView mAvatarImageView;
    private TextView mAuthorTextView;
    private TextView mPostTimeTextView;
    private ImageView mCoverImageView;
    private FrameLayout mFrameLayout;
    private LinearLayout mLoadingLayout;
    private LinearLayout mLoadEmptyLayout;
    private LinearLayout mLoadErrorLayout;
    private RelativeLayout mContentRelative;
    private ArticleDetailBean mArticleDetailBean;
    private ImageLoader mImageLoader;
    private WebView mContentWebView;
    private WebSettings mWebSettings;
    private ScrollView mScrollView;
    private VisibilityManager mVisibilityManager;

    private LruCache<String, String> mStringLruCache; // 文本信息的内存缓存对象
    private DiskLruCache mStringDiskLruCache; // 文本信息的磁盘缓存对象
    private String mDetailString; // 服务器返回的Json数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        setStatusBar(R.id.root_top_bar_article_detail_rl);
        initializeView();
        initializeValidData();
        initializeListener();
    }

    @Override
    public void initializeView() {
        mContentRelative = (RelativeLayout) findViewById(R.id.rl_article_detail_content);
        mBackButton = (Button) findViewById(R.id.btn_back);
        // 部分控件
        mTitleTextView = (TextView) findViewById(R.id.tv_article_detail_title);
        mAvatarImageView = (ImageView) findViewById(R.id.iv_article_detail_avatar);
        mAuthorTextView = (TextView) findViewById(R.id.tv_article_detail_author);
        mPostTimeTextView = (TextView) findViewById(R.id.tv_article_detail_post_time);
        mCoverImageView = (ImageView) findViewById(R.id.iv_article_detail_cover);
        mContentWebView = (WebView) findViewById(R.id.wv_article_detail_content);
        mScrollView = (ScrollView) findViewById(R.id.sv_article_detail_content);

        mFrameLayout = (FrameLayout) findViewById(R.id.root_load_status_fl);
        mLoadingLayout = (LinearLayout) findViewById(R.id.layout_loading);
        mLoadEmptyLayout = (LinearLayout) findViewById(R.id.layout_load_empty);
        mLoadErrorLayout = (LinearLayout) findViewById(R.id.layout_load_error);
        mVisibilityManager = new VisibilityManager(mScrollView, mFrameLayout, mLoadingLayout, mLoadEmptyLayout, mLoadErrorLayout);

        mImageLoader = ImageLoader.getInstance();
    }

    @Override
    public void initializeValidData() {
        setWebSettings();

        mVisibilityManager.setLoadingViewVisible();

        Intent intent = getIntent();
        if (intent != null) {
            String detailHref = intent.getStringExtra(KeyUtils.INTENT_EXTRA_DETAIL);
            getData(detailHref);
        }
    }

    @Override
    public void initializeListener() {
        // 导航栏“后退”，返回上一个页面
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.this.finish();
            }
        });
    }

    @Override
    public void bindData() {
        if (mArticleDetailBean != null) {
            mVisibilityManager.setTargetViewVisible();

            mTitleTextView.setText(mArticleDetailBean.getTitle());
            mImageLoader.displayImage(mArticleDetailBean.getUser().getAvatar_url(), mAvatarImageView);
            mAuthorTextView.setText(mArticleDetailBean.getUser().getName());
            mPostTimeTextView.setText(mArticleDetailBean.getRelativePublishTime());
            mImageLoader.displayImage(mArticleDetailBean.getCover(), mCoverImageView);
            mContentWebView.loadDataWithBaseURL(null, mArticleDetailBean.getContent(), "text/html", "UTF-8", null);
        } else {
            mVisibilityManager.setEmptyViewVisible();
        }
    }

    private void setWebSettings() {
        mWebSettings = mContentWebView.getSettings();

        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setBlockNetworkImage(true);

        // 设置自适应后，文字太小；最好的情况，还应该让图片match_parent
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        // 目前的解决方法是，让文字放大，最好是根据屏幕的dpi进行放大
        mWebSettings.setTextZoom(300);

        mContentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setBlockNetworkImage(false); // 神奇！为什么能行？
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

//        mWebSettings.setUseWideViewPort(true);
//        mWebSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void onBackPressed() {
        if (mContentWebView.canGoBack()) {
            mContentWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 从内存或磁盘中获取文本数据，还差清理磁盘缓存
     *
     * 1.判断内存中是否有缓存
     *  1.1 有
     *  1.2 没有，判断磁盘中是否有缓存
     *   1.2.1 有
     *   1.2.2 没有，判断网络是否可用
     *    1.2.2.1 可用
     *
     * 操作：
     * 1.1 从内存中读取
     * 1.2.1 从磁盘中读取，并缓存到内存中
     * 1.2.2.1 请求服务器
     */
    private void getData(String detailHref) {

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 16; // 内存的缓存空间
        mStringLruCache = new LruCache<String, String>(cacheSize) {
            @Override
            protected int sizeOf(String key, String value) {
                return value.getBytes().length;
            }
        };

        // 以详情页的url为key，从内存中获取缓存数据
        String stringCache = LruCacheManager.getStringFromMemoryCache(mStringLruCache, detailHref);

        // 判断内存中是否存在缓存
        if (stringCache == null) {
            Log.d(TAG, "内存中不存在缓存");

            // 在异步线程中检查磁盘中是否存在缓存
            new StringWorkerTask().execute(detailHref);
        } else {
            Log.d(TAG, "内存中存在缓存");

            // 绑定数据
            mArticleDetailBean = ArticleDetailDataManager.getArticleDetailBean(stringCache);
            bindData();
        }
    }

    /**
     * 异步执行请求服务器
     * @param detailHref
     */
    private void requestServer(final String detailHref, final String diskCacheKey) {
        OkHttpManager.getAsync(detailHref, new OkHttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.d(TAG, "请求失败！");

                mVisibilityManager.setErrorViewVisible();
                mDetailString = null;
            }

            @Override
            public void requestSuccess(String result) {
                Log.d(TAG, "请求成功！");
                Log.d(TAG, "当前线程：" + Thread.currentThread());

                mDetailString = result;
                new ResultWorkerTask(detailHref, diskCacheKey).execute(result);
            }
        });
    }

    /**
     * 判断磁盘中是否存在缓存；如果有，则执行读取操作，并缓存到内存中；如果没有，则执行请求操作
     */
    private class StringWorkerTask extends AsyncTask<String, Void, String> {

        private String detailHref = null;

        @Override
        protected String doInBackground(String... params) {
            detailHref = params[0];
            String stringCache = null;
            try {
                // 将详情页面的url地址进行MD5编码，作为磁盘操作的key
                String key = DiskLruCacheManager.getHashKeyForDiskCache(detailHref);

                // 获取磁盘缓存的路径
                File diskCacheDir = DiskLruCacheManager.getDiskCacheDir(ArticleDetailActivity.this, "string");
                if (!diskCacheDir.exists()) {
                    diskCacheDir.mkdirs();
                }

                // 创建DiskLruCache实例
                mStringDiskLruCache = DiskLruCache.open(diskCacheDir, DiskLruCacheManager.getAppVersion(ArticleDetailActivity.this), 1, 1024 * 1024);

                // 以MD5编码之后的String作为key，进行查找
                DiskLruCache.Snapshot snapshot = mStringDiskLruCache.get(key);

                if (snapshot == null) {
                    Log.d(TAG, "磁盘中不存在缓存");

                    // 判断网络是否可用，为true就请求服务器
                    if (NetworkUtils.isNetworkConnected(ArticleDetailActivity.this)) {
                        // 请求服务器，在重写的接口中执行进一步的操作
                        requestServer(detailHref, key);
                    }
                } else {
                    Log.d(TAG, "磁盘中存在缓存");

                    // 将磁盘缓存中的数据转换为String
                    stringCache = DiskLruCacheManager.getStringFromSnapshot(snapshot);

                    // 缓存到内存中
                    LruCacheManager.putStringToMemoryCache(mStringLruCache, detailHref, stringCache);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringCache;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                // 绑定数据
                mArticleDetailBean = ArticleDetailDataManager.getArticleDetailBean(s);
                bindData();
            }
        }
    }

    /**
     * 将请求结果分别写入内存和磁盘中
     */
    private class ResultWorkerTask extends AsyncTask<String, Void, String> {

        private String response = null;
        private String detailHref; // 详情页的url地址，作为内存缓存的key
        private String diskCacheKey; // 经过MD5编码的详情页url地址字符串，作为磁盘缓存的key

        public ResultWorkerTask(String detailHref, String diskCacheKey) {
            this.detailHref = detailHref;
            this.diskCacheKey = diskCacheKey;
        }

        @Override
        protected String doInBackground(String... params) {
            response = params[0];
            try {
                // 若请求服务器成功，就缓存到内存和磁盘中
                if (response != null) {
                    // 缓存到内存中
                    LruCacheManager.putStringToMemoryCache(mStringLruCache, detailHref, response);

                    // 缓存到磁盘中
                    DiskLruCache.Editor editor = mStringDiskLruCache.edit(diskCacheKey);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (NetworkOperator.writeStringToStream(response, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                // 绑定数据
                mArticleDetailBean = ArticleDetailDataManager.getArticleDetailBean(s);
                bindData();
            }
        }
    }
}
