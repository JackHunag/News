package base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BasePager;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity mActivity) {
		super(mActivity);
	}

	public void initData() {
		System.out.println("政务初始化了.....");
		TextView view = new TextView(mActivity);
		view.setText("政务");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// 修改页面标题
		tvTitle.setText("人口管理");

		btnMenu.setVisibility(View.VISIBLE);
	}

}
