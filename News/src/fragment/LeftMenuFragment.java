package fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import base.BasePager;
import base.impl.NewsCenterPager;

import com.android.news.MainActivity;
import com.android.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import domain.NewMenu.MenuData;

public class LeftMenuFragment extends BaseFragment {

	@ViewInject(R.id.lv_left_menu)
	private ListView listView;
	private ArrayList<MenuData> mMenuDataList;
	private int mCurrentPos;
	private LeftMenuAdapter mAdapter;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.left_menu_fragment, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

	}

	public void setMenuData(ArrayList<MenuData> data) {
		mMenuDataList = data;
		mCurrentPos = 0;

		mAdapter = new LeftMenuAdapter();

		listView.setAdapter(mAdapter);

		// 通过点击条目改变字体和图片颜色，默认第一个条目为enable=true
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mCurrentPos = position;
				mAdapter.notifyDataSetChanged();
				toggle();

				setCurrentDetailPager(position);
			}
		});

	}

	/**
	 * 加载侧边菜单详情页
	 * 
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {

		MainActivity mainUI = (MainActivity) mActivity;

		ContentFragment cfm = mainUI.getContentFragment();
		NewsCenterPager newsCenterPager = cfm.getNewsCenterPager();

		// 设置newsCenterPager里面的布局
		newsCenterPager.setCurrentDetailPager(position);

	}

	protected void toggle() {

		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然

	}

	class LeftMenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mMenuDataList.size();
		}

		@Override
		public MenuData getItem(int position) {

			return mMenuDataList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(mActivity,
					R.layout.listview_leftmenu_item, null);

			TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);
			tv_menu.setText(mMenuDataList.get(position).title);

			if (position == mCurrentPos) {
				tv_menu.setEnabled(true);
			} else {
				tv_menu.setEnabled(false);
			}

			// 设置字体颜色变化，默认新闻为红色
			return view;
		}

	}

}
