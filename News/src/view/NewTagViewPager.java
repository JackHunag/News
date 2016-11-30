package view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NewTagViewPager extends ViewPager {

	private int downX;
	private int downY;

	public NewTagViewPager(Context context) {
		super(context);

	}

	public NewTagViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		// 根据不同情况来判断是否要父控件拦截事件
		getParent().requestDisallowInterceptTouchEvent(true);

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:

			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			int dx = moveX - downX;
			int dy = moveY - downY;

			if (Math.abs(dy) < Math.abs(dx)) {
				int currentItem = getCurrentItem();
				// 左右滑动
				if (dx > 0) {
					// 向右划
					if (currentItem == 0) {
						// 第一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 向左划
					int count = getAdapter().getCount();// item总数
					if (currentItem == count - 1) {
						// 最后一个页面,需要拦截
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// 上下滑动,需要拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
