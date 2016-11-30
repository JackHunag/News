package base.impl.menu;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.GlobalConstants;
import utils.SpUtils;
import view.NewTagViewPager;
import view.PullToRefreshListView;
import view.PullToRefreshListView.OnRefreshListen;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetailPager;

import com.android.news.NewDetailActivity;
import com.android.news.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import domain.NewMenu.NewTagData;
import domain.NewTagMenu;
import domain.NewTagMenu.NewTagData.NewData;
import domain.NewTagMenu.NewTagData.NewTopData;

public class TagDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	
	public NewTagData mTagDetailData;//
	private View view;
	private PullToRefreshListView lv_tag_connect;
	private NewTagViewPager vp_tag_viewpager;
	private String mURL;
	private NewTagMenu mJsonData;//解析json后的对象值
	private ArrayList<NewTopData> mTopnews; //新闻头条数据集合
	private TextView tv_tag_name;
	private CirclePageIndicator mIndicator;
	private ArrayList<NewData> mNewsList;//新闻内容数据集合
	private String mMoreDataUrl;
	private Handler mHandler;
	public TagDetailPager(Activity mActivity, NewTagData newTagData) {
		super(mActivity);
		//传递过来的新闻中心数据对象
		mTagDetailData = newTagData;
		mURL = newTagData.url;
	}

	@Override
	public View initView() {

		view = View.inflate(mActivity, R.layout.tag_detail_pager, null);
		 View headerView = View.inflate(mActivity, R.layout.listview_header, null);
		vp_tag_viewpager = (NewTagViewPager) headerView
				.findViewById(R.id.vp_tag_viewpager);
		lv_tag_connect = (PullToRefreshListView) view.findViewById(R.id.lv_tag_connect);
		tv_tag_name = (TextView) headerView.findViewById(R.id.tv_tag_name);
		mIndicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
		mIndicator.setOnPageChangeListener(this);
		lv_tag_connect.addHeaderView(headerView);
		lv_tag_connect.setonRefreshListen(new OnRefreshListen() {
			@Override
			public void onUpRefreshListen() {
				//下拉刷新调用方法加载网络数据
				getDataFromServer();
			}

			@Override
			public void onDownRefreshListen() {
				mMoreDataUrl = mJsonData.data.more;
				if(!TextUtils.isEmpty(mMoreDataUrl)){
					getLoadMoreData();
				}else{
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
					lv_tag_connect.RefreshSuccess(true);
				}				
			}
		});
		   
		lv_tag_connect.setOnItemClickListener(new OnItemClickListener() {
		
			 @Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//当前posittion等于原item加上头布局
				  int count = lv_tag_connect.getHeaderViewsCount();
				  position =position-count;
				  NewData  newData = mNewsList.get(position);
				  
				    //用sp记录己读新闻条目
				      String readIds = SpUtils.getString(mActivity, "readIds", "");
				  
				        //避免重复添加进sp中进行判断
				      if(!readIds.contains(newData.id+"")){
				    	  readIds = readIds+newData.id+",";
						  SpUtils.setString(mActivity, "readIds", readIds);
				      }
				     				  
				TextView tv_title = (TextView) view.findViewById(R.id.tv_data);
				tv_title.setTextColor(Color.GRAY);
				
				Intent intent = new Intent(mActivity, NewDetailActivity.class);
				intent.putExtra("url", newData.url);
			     mActivity.startActivity(intent);			
			 }
		});
		
		return view;

	}
       
	//从服务器加载更多数据
	protected void getLoadMoreData() {
	  
		   HttpUtils utils = new HttpUtils();
		   utils.send(HttpMethod.GET, GlobalConstants.SERVER_URL+mMoreDataUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result);
				lv_tag_connect.RefreshSuccess(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
				Toast toast = Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER,0, 0);
				
				toast.show();
				lv_tag_connect.RefreshSuccess(true);
				
			}
		});
		
	}

	class TagPagerAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TagPagerAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
		}

		@Override
		public int getCount() {

			return mTopnews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);
			NewTopData newTopData = mTopnews.get(position);
			/* imageView.setImageResource(newTopData.topimage); */
			String topimage = newTopData.topimage;
			mBitmapUtils.display(imageView, topimage);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);

		}

	}
    
	  class TagDataAdaptere extends BaseAdapter{
		  
		  private BitmapUtils mBitmapUtils;

		public TagDataAdaptere(){
			  
			  mBitmapUtils = new BitmapUtils(mActivity);
			  //设置加载前的默认图片
			  mBitmapUtils.configDefaultLoadFailedImage(R.drawable.news_pic_default);
		  }

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public NewData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			 if(convertView ==null){
				     holder =new ViewHolder();
				 convertView = View.inflate(mActivity, R.layout.list_tagdata_items, null);
				 holder.mIcon = (ImageView) convertView.findViewById(R.id.im_tag_image);
				 holder.mTagData =(TextView) convertView.findViewById(R.id.tv_data);
				 holder.mTime =(TextView) convertView.findViewById(R.id.tv_time);
				 convertView.setTag(holder);
			 }else{
				  holder = (ViewHolder) convertView.getTag();
			 }
			 NewData  newData = mNewsList.get(position);
			  holder.mTagData.setText(mNewsList.get(position).title);
			    String readIds = SpUtils.getString(mActivity, "readIds", "");
				if (readIds.contains(newData.id + "")) {
					 holder.mTagData.setTextColor(Color.GRAY);
				} else {
					 holder.mTagData.setTextColor(Color.BLACK);
				}
			 
			 holder.mTime.setText(mNewsList.get(position).pubdate);
			 //利用BitmapUtil进行加载图片和缓存
			  
			 mBitmapUtils.display(holder.mIcon , mNewsList.get(position).listimage);
			 return convertView;
		}
		  
	  }
	 
	   static class ViewHolder{
		   public ImageView mIcon;
		   public TextView mTagData;
		   public TextView mTime;
	   }
	  
	  @Override
	public void initData() {

		    //加载缓存数据
		    String cache = CacheUtils.getCache(mURL, mActivity);
		    if(!TextUtils.isEmpty(cache)){
		    	processData(cache);
		    }
		
		     getDataFromServer();
	}

	  
	  
  /**
  * 加载网络数据的方法
  */
	private void getDataFromServer() {
         
		 //Xutil加载网络数据
		HttpUtils xUtil = new HttpUtils();
		xUtil.send(HttpMethod.GET, GlobalConstants.SERVER_URL + mURL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;//返回的json数据

						processData(result);
						CacheUtils.setCache(result, mURL, mActivity);
						lv_tag_connect.RefreshSuccess(true);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						Toast.makeText(mActivity, msg, 0).show();
						lv_tag_connect.RefreshSuccess(false);
					}
				});
	}
     /**
      * 解析json数据方法
      * @param result 传递过来的json值
      */
	protected void processData(String result) {

		Gson gson = new Gson();
		mJsonData = gson.fromJson(result, NewTagMenu.class);
		mTopnews = mJsonData.data.topnews;
		mNewsList = mJsonData.data.news;
	
		if (mTopnews != null) {
			vp_tag_viewpager.setAdapter(new TagPagerAdapter());
			mIndicator.setViewPager(vp_tag_viewpager);
			mIndicator.setSnap(true);//以快照方式显示
		}
	  
		 if(mNewsList !=null){
			 lv_tag_connect.setAdapter(new TagDataAdaptere());
		 }
		tv_tag_name.setText(mTopnews.get(0).title);
		
		  //viewpager自动轮播
		 if(mHandler ==null){
			 mHandler = new Handler(){
				 
				 public void handleMessage(android.os.Message msg) {
					 /**
					  * 思路
					  *  获取viewpagre的items,items++后，然后重新设置进去
					  */
					 
					 int currentItem = vp_tag_viewpager.getCurrentItem();
					 currentItem++;
					 //在此之前应判断是否轮播到最后一个items.
					 if(currentItem>mTopnews.size()-1){
						 currentItem =0;//重新设置currentItems =0
					 }
					 vp_tag_viewpager.setCurrentItem(currentItem);
					 
					  //继续在里面3秒轮播图片，形成内循环
					 mHandler.sendEmptyMessageDelayed(0, 3000);
				 };
			 };
		 
			  //当图片轮播时，用户进行触摸事件，所以应进行相应的监听
			  
			  vp_tag_viewpager.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					  
					  switch (event.getAction()) {
					
					  case MotionEvent.ACTION_DOWN:
						     
						  //按下时取消轮播事件，
						    mHandler.removeCallbacksAndMessages(null);
						break;
                      
					  case MotionEvent.ACTION_CANCEL:
						  
						   //取消onTouch事件时调用
						    //原因：当进行listView的滑动操作时，抬起事件没有失效，使到轮播事件无法重新恢复
						  mHandler.sendEmptyMessageDelayed(0, 3000);
						  
   				      break;
					 
					  case MotionEvent.ACTION_UP:
							//抬起时恢复轮播事件
						  mHandler.sendEmptyMessageDelayed(0, 3000);
						     
							break;
					default:
						break;
					}
					return false;
				}
			});
			 
			 mHandler.sendEmptyMessageDelayed(0, 3000);//3秒轮播一次图片
		     
		 
		 }
		
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		
		
	}

	@Override
	public void onPageSelected(int position) {
		
		//更新viewpager中的title值
		tv_tag_name.setText(mTopnews.get(position).title);
				
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
		
	}
	
	
}
