package com.example.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidlib.R;


/**
 * 当前类注释:
 * ProjectName：App36Kr
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class PullToRefreshListView extends ListView implements OnScrollListener, OnClickListener {
	/**
	 * 下拉状态
	 */
	private static final int PULL_TO_REFRESH = 1;   //下拉-默认为初始状态  准备下拉刷新
	private static final int RELEASE_TO_REFRESH = 2;   //释放刷新
	private static final int REFRESHING = 3;       //正在刷新

	private static final String TAG = "PullRefreshListView";

	private OnRefreshListener mOnRefreshListener;

	/**
	 * 组件滑动监听器 scroll  当view在进行下拉滑动的时候，判断滑动的距离，
	 * 如果达到可以进行刷新的临界点时候，回调当前接口中的方法
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;

	//下拉刷新的的头部view
	private LinearLayout mRefreshView;
	private ImageView mRefreshViewImage;
	private ProgressBar mRefreshViewProgress;
	private TextView mRefreshViewText;
	private TextView mRefreshViewLastUpdated;


	private int mRefreshState;
	private int mCurrentScrollState;

	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private int mRefreshViewHeight;
	private int mRefreshOriginalTopPadding;
	private int mLastMotionY;
	private LinearLayout mLoadMorView;
    private LinearLayout mLoadMoreView;

    public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mRefreshView = (LinearLayout) View.inflate(context, R.layout.header_pull_to_refresh, null);
		mRefreshViewText = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage = (ImageView) mRefreshView.findViewById(R.id.iv_pull_to_refresh);
		mRefreshViewProgress = (ProgressBar) mRefreshView.findViewById(R.id.pb_pull_to_refresh);
		mRefreshViewLastUpdated = (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

		mRefreshState = PULL_TO_REFRESH;
		mRefreshViewImage.setMinimumHeight(50); //设置下拉最小的高度为50

        // 在这里的布局中会有问题
//        mLoadMoreView = (LinearLayout) View.inflate(context, R.layout.load_more_footview_layout, null);
//        addFooterView(mLoadMorView);

		setFadingEdgeLength(0);
		setHeaderDividersEnabled(false);

		//把refreshview加入到listview的头部
		addHeaderView(mRefreshView);
		super.setOnScrollListener(this);
		mRefreshView.setOnClickListener(this);

		mRefreshView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();
		mRefreshOriginalTopPadding = -mRefreshViewHeight;
		
		resetHeaderPadding();
	}

	/**
	 * Set the listener that will receive notifications every time the list scrolls.
	 *
	 * @param l  The scroll listener.
	 */
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * 注册listview下拉刷新回到接口
	 * Register a callback to be invoked when this list should be refreshed.
	 * 
	 * @param onRefreshListener  The callback to run.
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	 /**
	  * 进行设置设置上一次更新的时候
	  *
     * Set a text to represent when the list was last updated. 
     * @param lastUpdated Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
            mRefreshViewLastUpdated.setText(lastUpdated);
        } else {
            mRefreshViewLastUpdated.setVisibility(View.GONE);
        }
    }

	/**
	 * touch事件处理
	 * @param event
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// 控制view随滑动速度出现
			int offsetY = (int) event.getY();
			int deltY = Math.round(offsetY - mLastMotionY);
			mLastMotionY = offsetY;

			if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
				deltY = deltY / 2;
				mRefreshOriginalTopPadding += deltY;
				if (mRefreshOriginalTopPadding < -mRefreshViewHeight) {
					mRefreshOriginalTopPadding = -mRefreshViewHeight;
				}
				resetHeaderPadding();
			}
			break;
		case MotionEvent.ACTION_UP:
			//当手指抬开得时候 进行判断下拉的距离 ，如果>=临界值，那么进行刷洗，否则回归原位
			if (!isVerticalScrollBarEnabled()) {
				setVerticalScrollBarEnabled(true);
			}
			if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
				if (mRefreshView.getBottom() >= mRefreshViewHeight 
						&& mRefreshState == RELEASE_TO_REFRESH) {
					//准备开始刷新
					prepareForRefresh();
				} else {
					// Abort refresh
                    resetHeader();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL && mRefreshState != REFRESHING) {
			 if (firstVisibleItem == 0) {
                if ((mRefreshView.getBottom() >= mRefreshViewHeight)
                        && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_release);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if (mRefreshView.getBottom() < mRefreshViewHeight
                        && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_pull);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    mRefreshState = PULL_TO_REFRESH;
                }
            }
		}
		
		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
		
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	/**
	 * Sets the header padding back to original size.
	 */
	private void resetHeaderPadding() {
		mRefreshView.setPadding(
                mRefreshView.getPaddingLeft(),
                mRefreshOriginalTopPadding,
                mRefreshView.getPaddingRight(),
                mRefreshView.getPaddingBottom());
	}

	public void prepareForRefresh() {
		if (mRefreshState != REFRESHING) {
			mRefreshState = REFRESHING;
			
			mRefreshOriginalTopPadding = 0;
			resetHeaderPadding();
			
			mRefreshViewImage.clearAnimation();
			mRefreshViewImage.setVisibility(View.GONE);
			mRefreshViewProgress.setVisibility(View.VISIBLE);
			mRefreshViewText.setText(R.string.pull_to_refresh_refreshing);
			
			onRefresh();
		}
	}

	private void resetHeader() {
		mRefreshState = PULL_TO_REFRESH;
		
		mRefreshOriginalTopPadding = -mRefreshViewHeight;
		resetHeaderPadding();
		
		mRefreshViewImage.clearAnimation();
		mRefreshViewImage.setVisibility(View.VISIBLE);
		mRefreshViewProgress.setVisibility(View.GONE);
		mRefreshViewText.setText(R.string.pull_to_refresh_pull);
	}

	/**
	 * 开始回调刷新
	 */
	public void onRefresh() {
		Log.d(TAG, "onRefresh");
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}
	
	/**
     * Resets the list to a normal state after a refresh.
     */
    public void onRefreshComplete() {        
        Log.d(TAG, "onRefreshComplete");

        resetHeader();
    }
	
    @Override
	public void onClick(View v) {
    	Log.d(TAG, "onClick");
	}
    
	/**
	 * Interface definition for a callback to be invoked when list should be
	 * refreshed.
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the list should be refreshed.
		 * <p>
		 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
		 * expected to indicate that the refresh has completed.
		 */
		public void onRefresh();
	}
}