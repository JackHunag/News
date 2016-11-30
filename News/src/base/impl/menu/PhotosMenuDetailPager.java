package base.impl.menu;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.GlobalConstants;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetailPager;

import com.android.news.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import domain.NewPhotosMenu;
import domain.NewPhotosMenu.PhotosNewData;

/**
 * 菜单详情页-组图
 * 
 * @author hsssf
 * 
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements OnClickListener{

	@ViewInject(R.id.lv_detail_photos)
	private ListView mListVew;
	@ViewInject(R.id.gv_detail_photos)
	private GridView mGridView;
    private boolean isListView =true;
	private ImageView mBtn_photo;
	private ArrayList<PhotosNewData> mNewsList;
	

	public PhotosMenuDetailPager(Activity mActivity, ImageButton btn_photo) {
		super(mActivity);
		mBtn_photo = btn_photo;
		mBtn_photo.setOnClickListener(this);
	}

	@Override
	public View initView() {
    
		      View view = View.inflate(mActivity, R.layout.detail_photos, null);
		      ViewUtils.inject(this, view);
		  
		return view;
	}    
	
	public void initData(){
		String cache = CacheUtils.getCache(GlobalConstants.PHOTOS_URL, mActivity);
		
		  if(!TextUtils.isEmpty(cache)){
			  processData(cache);
		  }
		  getDataFromServer();
	  }

	
	private void getDataFromServer() {
		
		HttpUtils xutils = new HttpUtils();
		xutils.send(HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				
				String result = responseInfo.result;
				
				  processData(result);

					CacheUtils.setCache(GlobalConstants.PHOTOS_URL, result,
							mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
						.show();
				
			}
		});
		
	}


	protected void processData(String result ) {
		
		Gson gson = new Gson();
		 NewPhotosMenu fromJson = gson.fromJson(result,  NewPhotosMenu.class);
		 mNewsList = fromJson.data.news;
		 
		 if(mNewsList!=null){
			 mListVew.setAdapter(new PhotosAdapter());
			 mGridView.setAdapter(new PhotosAdapter());
		 }
		
	}


	class PhotosAdapter extends BaseAdapter{
		
		private BitmapUtils mBitmapUtils;

		public PhotosAdapter(){
			
			 mBitmapUtils = new BitmapUtils(mActivity);
			 mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			
			return mNewsList.size();
		}

		@Override
		public PhotosNewData getItem(int position) {
			
			return mNewsList.get(position) ;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				 holder = new ViewHolder();
				convertView=View.inflate(mActivity, R.layout.listview_photos_items, null);
				holder.mPhotos = (ImageView) convertView.findViewById(R.id.iv_photos);
				holder.mTilte = (TextView) convertView.findViewById(R.id.tv_photos_name);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.mTilte.setText(getItem(position).title);
			mBitmapUtils.display(holder.mPhotos, getItem(position).listimage);
			  
			return convertView;
		}
		  
	  }

     static class ViewHolder{
    	
    	 public ImageView mPhotos;
    	 public TextView mTilte;
     }

       
	@Override
	public void onClick(View v) {
		
		if(isListView){
			mGridView.setVisibility(View.GONE);
			mListVew.setVisibility(View.VISIBLE);
			mBtn_photo.setImageResource(R.drawable.icon_pic_list_type);
			isListView =false;
		}else{
			mGridView.setVisibility(View.VISIBLE);
			mListVew.setVisibility(View.GONE);
			mBtn_photo.setImageResource(R.drawable.icon_pic_grid_type);
			isListView =true;
		}
		
	}
}
