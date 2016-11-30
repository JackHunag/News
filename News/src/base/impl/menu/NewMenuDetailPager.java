package base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import base.BaseMenuDetailPager;

import com.android.news.MainActivity;
import com.android.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import domain.NewMenu.NewTagData;

/**
 * 菜单详情页-新闻中心
 * 
 * @author hsssf
 * 
 */

/*
 * 思路：在新闻页面因为为每一个头部分类栏目的布局都一样，所以创建一个布局对象统一处理
 * 在该类中传递相应数据过去
 * 注意：
 * 事件的处理和布局框架的编写
 * 开源框架ViewPagerIndicatorLibray使用：
 *  1.引入库 2.解决support-v4冲突(让两个版本一致) 3.从例子程序中拷贝布局文件
 * 4.从例子程序中拷贝相关代码(指示器和viewpager绑定; 重写getPageTitle返回标题) 5.在清单文件中增加样式 6.背景修改为白色
 * 7.修改样式-背景样式&文字样式
 * @Override public boolean dispatchTouchEvent(MotionEvent ev) { 该方法使到所有父控件
 * 不拦截子控件的事件 getParent().requestDisallowInterceptTouchEvent(true); return
 * super.dispatchTouchEvent(ev); }
 */
public class NewMenuDetailPager extends BaseMenuDetailPager implements
		OnPageChangeListener {

	private ViewPager mViewPager;

	private TabPageIndicator indicator;

	private ArrayList<NewTagData> mTagData;
	private TagDetailPager mDetailPager;

	private ArrayList<TagDetailPager> mPagerList;

	public NewMenuDetailPager(Activity mActivity, ArrayList<NewTagData> children) {
		super(mActivity);
		//从新闻中心传递过来相应的新闻数据
		mTagData = children;
	}

	@Override
	public View initView() {

		View view = View
				.inflate(mActivity, R.layout.new_menu_detailpager, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
		indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		ImageButton btn_next = (ImageButton) view.findViewById(R.id.btn_next);
		 
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//给分类栏目的按键添加点击事件
				//得到被选中的item值，并设置相应的item
				int currentItem = mViewPager.getCurrentItem();
				currentItem++;
				mViewPager.setCurrentItem(currentItem);
			}
		});
		indicator.setOnPageChangeListener(this);
		return view;
	}

	@Override
	public void initData() {

		mPagerList = new ArrayList<TagDetailPager>();
		//把获取到的新闻头部栏目数据对象封装好
		for (int i = 0; i < mTagData.size(); i++) {
			mDetailPager = new TagDetailPager(mActivity, mTagData.get(i));
			mPagerList.add(mDetailPager);
		}

		mViewPager.setAdapter(new DetailPagerAdapter());
		indicator.setViewPager(mViewPager);
		
		
	}

	class DetailPagerAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {

			// 获取 json中childre的数据
			NewTagData data = mTagData.get(position);
			return data.title;
		}

		@Override
		public int getCount() {

			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			TagDetailPager tagPager = mPagerList.get(position);

			View view = tagPager.mRootView;
			container.addView(view);
			// 初始化数据
			tagPager.initData();
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}
	}

	@Override
	public void onPageSelected(int position) {

		if (position == 0) {
			setSlidingMenuEnable(true);
		} else {
			setSlidingMenuEnable(false);
		}
	}

	private void setSlidingMenuEnable(boolean b) {

		// 获取slipmenu对象，才可禁止侧边栏

		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		// 设置它触摸的模式
		if (b) {

			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {

	}

}
