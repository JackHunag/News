package base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.news.MainActivity;
import com.android.news.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 此类作用创建五个viewpager对象时把共有的东西写在一起
 * 
 * @author hsssf
 * 
 */
public class BasePager {

	public Activity mActivity;
	public TextView tvTitle;
	public ImageButton btnMenu;
	public FrameLayout flContent;
	public View mRootView;
	public ImageButton btn_photo;

	public BasePager(Activity mActivity) {
		this.mActivity = mActivity;
		mRootView = initView();

	}

	public View initView() {
		View view = View.inflate(mActivity, R.layout.viewpager_basepager, null);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		btn_photo = (ImageButton) view.findViewById(R.id.btn_photo);          
		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		return view;

	}

	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		// 调用改方法可打开或隐藏侧边栏
		slidingMenu.toggle();
	}

	public void initData() {
	}

}
