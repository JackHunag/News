package com.android.news;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragment.ContentFragment;
import fragment.LeftMenuFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

/**
 * 利用SlidingMenu进行操作 要继承相应的sliding中activity 把oncreate中的修饰符改为public
 * 
 * @author hsssf
 * 
 */
public class MainActivity extends SlidingFragmentActivity {

	private static final String LEFTMENU_FRAGMENT = "LEFTMENU_FRAGMENT";
	private static final String CONTENT_FRAGMENT = "CONTENT_FRAGMENT";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// 为侧边栏设置一个布局
		setBehindContentView(R.layout.left_menu);
		// 获取SlidingMenu对象
		SlidingMenu slidingMenu = getSlidingMenu();

		// 设置触摸模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
		 
		 WindowManager windowManager = getWindowManager();
		  
		   //屏幕适配
		   int screenWidth = windowManager.getDefaultDisplay().getWidth();
           int width =(200*screenWidth)/320;
		 
            // 设置可滑动区域
		slidingMenu.setBehindOffset(width);// 屏幕预留200像素宽度

		initFragment();
	}

	/**
	 * 初始化fragment
	 */
	private void initFragment() {

		FragmentManager sfm = getSupportFragmentManager();
		FragmentTransaction btt = sfm.beginTransaction();
		btt.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				LEFTMENU_FRAGMENT);
		btt.replace(R.id.fl_main, new ContentFragment(), CONTENT_FRAGMENT);
		btt.commit();
	}

	/**
	 * 获取LeftMenuFragment对象
	 * 
	 * @return
	 */
	public LeftMenuFragment getLeftMenuFragment() {

		FragmentManager sfm = getSupportFragmentManager();
		// 通过FragmentManager，利用指定的Tag获取指定fragment对象
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) sfm
				.findFragmentByTag(LEFTMENU_FRAGMENT);
		return leftMenuFragment;

	}

	/**
	 * 获取contentFragment对象
	 * 
	 * @return
	 */
	public ContentFragment getContentFragment() {
		FragmentManager sfm = getSupportFragmentManager();
		ContentFragment contentFragment = (ContentFragment) sfm
				.findFragmentByTag(CONTENT_FRAGMENT);
		return contentFragment;

	}

}
