package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.news.R;

/**
 * 新闻中心的向上刷新的ListView
 * 
 * @author hsssf 2016-11-13
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private int mHeightView;
	private View mView;
	private int mdownY;
	private static final int STATE_PULL_TO_REFRESH = 1;
	private static final int STATE_RELEASE_TO_REFRESH = 2;
	private static final int STATE_REFRESHING = 3;
	private int mState = STATE_PULL_TO_REFRESH;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private RotateAnimation mUpAmai;
	private RotateAnimation mDownAmai;
	private OnRefreshListen onRefreshListen;
	private View mFootView;
    private boolean isLoadMore;
	private int mFootHeight;
	public PullToRefreshListView(Context context) {
		super(context);
		initView();		
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);		
		initView();
	}

	private void initView() {
		initHeaderView();
		initAmai();
		initFootView();
	}

	private void initFootView() {
		mFootView = View.inflate(getContext(), R.layout.listview_refresh_foot, null);
		this.addFooterView(mFootView);
		mFootView.measure(0, 0);
		mFootHeight = mFootView.getMeasuredHeight();
		mFootView.setPadding(0,-mHeightView, 0, 0);
		
		//监听滑动事件
		 this.setOnScrollListener(this);
		
	}

	private void initHeaderView() {

		mView = View.inflate(getContext(), R.layout.listview_refresh_header,
				null);
		this.addHeaderView(mView);
		tvTitle = (TextView) mView.findViewById(R.id.tv_name);
		tvTime = (TextView) mView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mView.findViewById(R.id.iv_arrow);
		pbProgress = (ProgressBar) mView.findViewById(R.id.pb_progress);	
		mView.measure(0, 0);
		mHeightView = mView.getMeasuredHeight();
		mView.setPadding(0, -mHeightView, 0, 0);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			mdownY = (int) ev.getY();

			break;

		case MotionEvent.ACTION_MOVE:
			if (mdownY == -1) {
				mdownY = (int) ev.getY();
			}
			int moveY = (int) ev.getY();
			int dY = moveY - mdownY;

			if (mState == STATE_REFRESHING) {
				return super.onTouchEvent(ev);
			}

			// 刷新的三个状态
			/*
			 * private static final int STATE_PULL_TO_REFRESH = 1; private
			 * static final int STATE_RELEASE_TO_REFRESH = 2; private static
			 * final int STATE_REFRESHING = 3;
			 */

			if (dY > 0 && getFirstVisiblePosition() == 0) {
				int padding = dY - mHeightView;
				mView.setPadding(0, padding, 0, 0);

				if (padding < 0 && mState != STATE_PULL_TO_REFRESH) {
					mState = STATE_PULL_TO_REFRESH;
					onRefreshState();

				} else if (padding > 0 && mState != STATE_RELEASE_TO_REFRESH) {
					mState = STATE_RELEASE_TO_REFRESH;
					onRefreshState();

				}
				
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			mdownY = -1;
			if (mState == STATE_RELEASE_TO_REFRESH) {
				mState = STATE_REFRESHING;
				onRefreshState();
				mView.setPadding(0, 0, 0, 0);

				if (onRefreshListen != null) {
					onRefreshListen.onUpRefreshListen();
				} else if (mState == STATE_PULL_TO_REFRESH) {
					mView.setPadding(0, -mHeightView, 0, 0);
				}
			}
			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	private void onRefreshState() {

		switch (mState) {
		case STATE_PULL_TO_REFRESH:
			System.out.println("dsfadsafa");
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(mDownAmai);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvTitle.setText("释放刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(mUpAmai);

			break;
		case STATE_REFRESHING:
			ivArrow.setVisibility(View.INVISIBLE);
			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.clearAnimation();
			tvTitle.setText("正在刷新...");
			break;

		default:
			break;
		}

	}

	private void initAmai() {

		mUpAmai = new RotateAnimation(0, -180f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mUpAmai.setDuration(200);
		mUpAmai.setFillAfter(true);

		mDownAmai = new RotateAnimation(-180f, -360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mDownAmai.setDuration(200);
		mDownAmai.setFillAfter(true);

	}

	/**
	 * 网络数据加载时调用
	 */
	public void RefreshSuccess(boolean success) {
		
		if(!isLoadMore){
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.startAnimation(mDownAmai);
			mState = STATE_PULL_TO_REFRESH;
			mView.setPadding(0, -mHeightView, 0, 0);
			if(success){
				String time = setTime();
				tvTime.setText(time);
			}
		}else{
			mFootView.setPadding(0, -mFootHeight, 0, 0);
			isLoadMore=false;
		}
		
		
		
		
	}

	/**
	 * 数据更新时间
	 * 
	 * @return
	 */
	private String setTime() {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//滑动状态改变时调用
		  
		   //最后一条条目和滑动空闲状态时
		 if(scrollState==SCROLL_STATE_IDLE && getLastVisiblePosition()== getCount()-1){
			 isLoadMore=true;
			 mFootView.setPadding(0, 0, 0, 0);
			 //并将item设置为最后一个
			  setSelection(getCount()-1);
			  
			  if(onRefreshListen!=null){
				  onRefreshListen.onDownRefreshListen();
			  }
		 }
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	          //滑动过程时调用
		
	}
	public void setonRefreshListen(OnRefreshListen onRefreshListen) {
		this.onRefreshListen = onRefreshListen;

	}

	public interface OnRefreshListen {
		public void onUpRefreshListen();
		public void onDownRefreshListen();
	}
}
