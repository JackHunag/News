package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 创建fragment时调用
		super.onCreate(savedInstanceState);
		// 获取当前fragment所依赖的activity,方便后面使用
		mActivity = getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化fragment布局时调用
		View view = initView();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// 当fragment所依赖的activity创建完成时调用
		super.onActivityCreated(savedInstanceState);
		initData();

	}

	/**
	 * 
	 * 让其子类继承该方法
	 * 
	 */

	public abstract View initView();

	public abstract void initData();

}
