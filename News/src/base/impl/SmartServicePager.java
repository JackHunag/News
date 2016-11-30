package base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BasePager;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity mActivity) {
		super(mActivity);
	}

	public void initData() {

		TextView view = new TextView(mActivity);
		view.setText("智慧服务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		flContent.addView(view);
		// 修改页面标题
		tvTitle.setText("生活");
		btnMenu.setVisibility(View.VISIBLE);
	}

}
