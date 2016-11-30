package base.impl;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.GlobalConstants;
import android.app.Activity;
import android.graphics.Color;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetailPager;
import base.BasePager;
import base.impl.menu.InteractMenuDetailPager;
import base.impl.menu.NewMenuDetailPager;
import base.impl.menu.PhotosMenuDetailPager;
import base.impl.menu.TopicMenuDetailPager;

import com.android.news.MainActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import domain.NewMenu;
import fragment.LeftMenuFragment;

public class NewsCenterPager extends BasePager {

	private NewMenu mJsonData;
	private ArrayList<BaseMenuDetailPager> mMenuDetailPager;
	private BaseMenuDetailPager basePager;

	public NewsCenterPager(Activity mActivity) {
		super(mActivity);
	}

	public void initData() {
		System.out.println("新闻初始化了.....");
		btnMenu.setVisibility(View.VISIBLE);

		String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL,
				mActivity);

		// 判断是否有缓存，如果有则加载缓存数据
		if (!TextUtils.isEmpty(cache)) {
			processResult(cache);
		}
		getDataFromServer();
	}

	/**
	 * 请求服务器获取数据
	 */
	private void getDataFromServer() {

		// 获取Xutil对象
		HttpUtils xutil = new HttpUtils();

		// 向服务器发送一个请求,通过回调获取数据
		/*
		 * 三个参数：请求方式，服务器地址，回调接口
		 */
		xutil.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求成功时调用
						// 获取服务器返回结果

						String result = responseInfo.result;
						System.out.println("服务器返回结果" + result);

						processResult(result);
						loadPagerData();
						CacheUtils.setCache(result,
								GlobalConstants.CATEGORY_URL, mActivity);

					}

					@Override
					public void onFailure(HttpException error, String msg) {

						// 请求失败时调用
						error.printStackTrace();
						Toast.makeText(mActivity, msg, 1);

					}
				});

	}

	/**
	 * 加载侧边栏菜单页面
	 */

	protected void loadPagerData() {

		mMenuDetailPager = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPager.add(new NewMenuDetailPager(mActivity, mJsonData.data
				.get(0).children));
		mMenuDetailPager.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPager.add(new PhotosMenuDetailPager(mActivity,btn_photo));
		mMenuDetailPager.add(new InteractMenuDetailPager(mActivity));

		// 当加载页面时，就设置默认新闻中心为首页
		setCurrentDetailPager(0);
	}

	/**
	 * 解析服务器返回的数据 GSON
	 */
	protected void processResult(String json) {

		// 创建GSON对象
		Gson gson = new Gson();
		mJsonData = gson.fromJson(json, NewMenu.class);

		// 给leftfragment填充数据
		/*
		 * 思路： 在左侧边栏的fragment里面写一个接收数据的方法，在此调用，进行数据传递 获取leftfragment对象？
		 * 在mainactivity中写一个获取leftfragment对象的方法，通过mactivity调用
		 */

		MainActivity mainActivity = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mJsonData.data);
	}

	/**
	 * 设置侧边栏菜单页面，通过点击的条目更换页面
	 * 
	 * @param Position
	 */

	public void setCurrentDetailPager(int Position) {
		flContent.removeAllViews();
		if (mMenuDetailPager != null) {
			basePager = mMenuDetailPager.get(Position);

			View rootView = basePager.mRootView;// 获取指定的页面view

			// 添加前应把之前页面清除，避免页面重叠

			// 动态将指定view添加进basePager里的帧布局
			flContent.addView(rootView);
			// 设置title
			tvTitle.setText(mJsonData.data.get(Position).title);
			// 初始化页面的数据
			basePager.initData();
		   
		    if (basePager instanceof PhotosMenuDetailPager ){
		    	btn_photo.setVisibility(View.VISIBLE);
		    }else{
		    	btn_photo.setVisibility(View.GONE);
		    }
			
		
		} else {

			TextView textView = new TextView(mActivity);
			textView.setText("网络错误");
			textView.setTextSize(22);
			textView.setTextColor(Color.BLACK);
			flContent.addView(textView);
			textView.setGravity(Gravity.CENTER);
		}

	}
}
