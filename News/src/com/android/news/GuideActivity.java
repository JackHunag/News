package com.android.news;

import java.util.ArrayList;

import utils.ConstantValue;
import utils.DensityUtils;
import utils.SpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GuideActivity extends Activity implements OnClickListener {
	private Button btn_start;
	private ViewPager vp_guide;
	private LinearLayout ll_container;

	private int[] resimage = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private ArrayList<ImageView> mImgList;
	private ImageView iv_red_ponit;
	private ImageView mPonitView;
	private int mPonitDes;
	private int OldPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);

		initUI();
		initData();
	}

	private void initData() {

		mImgList = new ArrayList<ImageView>();
		for (int i = 0; i < resimage.length; i++) {
			// 初始化viewpager
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(resimage[i]);
			mImgList.add(imageView);

			// 初始化小圆点

			mPonitView = new ImageView(this);
			mPonitView.setImageResource(R.drawable.guide_ponit_selector);
			// 设置布局参数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			if (i > 0) {
				params.leftMargin = DensityUtils.dip2Px(this, 10);
				mPonitView.setEnabled(false);
			}
			mPonitView.setLayoutParams(params);
			ll_container.addView(mPonitView);
		}
		ll_container.getChildAt(0).setEnabled(true);
		vp_guide.setAdapter(new GuideAdpter());

		/*
		 * iv_red_ponit.getViewTreeObserver().addOnGlobalLayoutListener( new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() {
		 * iv_red_ponit.getViewTreeObserver()
		 * .removeGlobalOnLayoutListener(this); mPonitDes =
		 * ll_container.getChildAt(1).getLeft() -
		 * ll_container.getChildAt(0).getLeft();
		 * 
		 * } });
		 */
	}

	class GuideAdpter extends PagerAdapter {

		@Override
		public int getCount() {

			return mImgList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		// 初始化item
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView view = mImgList.get(position);
			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}
	}

	private void initUI() {
		btn_start = (Button) findViewById(R.id.btn_start);
		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		// iv_red_ponit = (ImageView) findViewById(R.id.iv_red_ponit);
		btn_start.setOnClickListener(this);
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 当页面被选时调用
				if (position == mImgList.size() - 1) {
					btn_start.setVisibility(View.VISIBLE);
				} else {
					btn_start.setVisibility(View.INVISIBLE);
				}

				int newPosition = position % mImgList.size();
				ll_container.getChildAt(OldPosition).setEnabled(false);
				ll_container.getChildAt(newPosition).setEnabled(true);
				OldPosition = newPosition;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 当页面在滑动时回调

				/*
				 * int leftMargin = (int)
				 * (mPonitDes*positionOffset)+position*mPonitDes;
				 * RelativeLayout.LayoutParams params =
				 * (RelativeLayout.LayoutParams) iv_red_ponit.getLayoutParams();
				 * params.leftMargin=leftMargin;
				 * iv_red_ponit.setLayoutParams(params);
				 */
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 当页面在状态改变时回调

			}
		});

	}

	@Override
	public void onClick(View v) {

		SpUtils.setBoolean(GuideActivity.this, ConstantValue.IS_FIRST_ENTER,
				false);
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}
}
