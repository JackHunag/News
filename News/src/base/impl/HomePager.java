package base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BasePager;

public class HomePager extends BasePager {

	public HomePager(Activity mActivity) {
		super(mActivity);
	}

	public void initData() {

		System.out.println("首页初始化了.....");
		TextView view = new TextView(mActivity);
		view.setText("首页");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		flContent.addView(view);
		// 修改页面标题
		tvTitle.setText("网易新闻");
		btnMenu.setVisibility(View.INVISIBLE);
	}

}
