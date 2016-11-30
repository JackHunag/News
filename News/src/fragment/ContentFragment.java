package fragment;

import java.util.ArrayList;

import view.NoScrollViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import base.BasePager;
import base.impl.GovAffairsPager;
import base.impl.HomePager;
import base.impl.NewsCenterPager;
import base.impl.SettingPager;
import base.impl.SmartServicePager;

import com.android.news.MainActivity;
import com.android.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 主页面fragment
 * 
 * @author hsssf
 * 
 */

public class ContentFragment extends BaseFragment {

	private NoScrollViewPager vp_content;
	private RadioGroup rg_group;
	private ArrayList<BasePager> mPagerList;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.content_fragment, null);
		vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 调用子类中的initdata()方法完成数据的初始化
				mPagerList.get(position).initData();

				// 来禁用首页和设置的侧边栏
				if (position == 0 || position == 4) {
					setSlipMenuable(false);
				} else {
					setSlipMenuable(true);
				}

			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int position) {

			}
		});

		return view;
	}

	protected void setSlipMenuable(boolean enable) {
		// 想对slipmenu进行操作，必须获取mainactivity对象，在get到slipmenu对象
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}

	@Override
	public void initData() {

		// 创建一个集合装载相应的布局类
		mPagerList = new ArrayList<BasePager>();
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovAffairsPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		MyPagerAdapter pagerAdapter = new MyPagerAdapter();
		vp_content.setAdapter(pagerAdapter);
		rg_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_home:
					// 首页
					// mViewPager.setCurrentItem(0);
					vp_content.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
					break;
				case R.id.rb_news:
					// 新闻中心
					vp_content.setCurrentItem(1, false);
					break;

				case R.id.rb_smart:
					// 智慧服务
					vp_content.setCurrentItem(2, false);
					break;
				case R.id.rb_gov:
					// 政务
					vp_content.setCurrentItem(3, false);
					break;
				case R.id.rb_setting:
					// 设置
					vp_content.setCurrentItem(4, false);
					break;
				default:
					break;
				}

			}
		});
		// 手动加载首页数据
		mPagerList.get(0).initData();
		// 把侧边栏默认设为false
		setSlipMenuable(false);
	}

	class MyPagerAdapter extends PagerAdapter {

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

			BasePager basePager = mPagerList.get(position);
			// basePager.mRootView获取当前页面的布局对象
			View view = basePager.mRootView;
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}
	}

	public NewsCenterPager getNewsCenterPager() {
		NewsCenterPager basePager = (NewsCenterPager) mPagerList.get(1);
		return basePager;

	}

}
