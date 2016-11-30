package com.android.news;

import utils.ConstantValue;
import utils.SpUtils;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity implements AnimationListener {

	private ImageView iv_splash;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initUI();
		initAnimation();
	}

	private void initAnimation() {

		// 旋转动画

		RotateAnimation rotateAnim = new RotateAnimation(0, 360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		rotateAnim.setDuration(1000);
		rotateAnim.setFillAfter(true);
		// 缩放动画
		ScaleAnimation scaleAnim = new ScaleAnimation(0, 1, 0, 1,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		scaleAnim.setDuration(1000);
		scaleAnim.setFillAfter(true);

		// 渐变动画
		AlphaAnimation alphaAnima = new AlphaAnimation(0, 1);
		alphaAnima.setDuration(2000);
		alphaAnima.setFillAfter(true);

		// 动画集合
		AnimationSet setAnim = new AnimationSet(false);
		setAnim.addAnimation(alphaAnima);
		setAnim.addAnimation(scaleAnim);
		setAnim.addAnimation(rotateAnim);
		// 开始动画效果
		iv_splash.startAnimation(setAnim);

		// 监听动画
		setAnim.setAnimationListener(this);

	}

	private void initUI() {

		iv_splash = (ImageView) findViewById(R.id.iv_splash);

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 动画结束时调用,当动画结束后进入新手引导页
		// 判断是否是第一次进入新手页

		boolean is_first = SpUtils.getBoolean(SplashActivity.this,
				ConstantValue.IS_FIRST_ENTER, true);
		if (is_first) {
			startActivity(new Intent(getApplicationContext(),
					GuideActivity.class));

		} else {
			startActivity(new Intent(getApplicationContext(),
					MainActivity.class));
		}
		finish();

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// 开始动画时调用

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// 动画重复，进行中时调用

	}

}
