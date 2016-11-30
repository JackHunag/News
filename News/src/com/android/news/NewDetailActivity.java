package com.android.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewDetailActivity extends Activity implements OnClickListener {

	private ProgressBar mProgress;

	private String mUrl;// 新闻详情内容链接

	private WebView mWebView;

	private ImageButton btn_back;

	private ImageButton mTextSize;

	private ImageButton mShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_new_detail);

		initView();
		initWebView();

	}

	private void initView() {
		mWebView = (WebView) findViewById(R.id.wv_detail);
		mProgress = (ProgressBar) findViewById(R.id.pb_new_finish);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		mTextSize = (ImageButton) findViewById(R.id.btn_change_textsize);
		mShare = (ImageButton) findViewById(R.id.btn_share);
		Intent intent = getIntent();
		mUrl = intent.getStringExtra("url");
		btn_back.setOnClickListener(this);
		mTextSize.setOnClickListener(this);
		mShare.setOnClickListener(this);
	}

	private void initWebView() {
		mWebView.loadUrl(mUrl);
		mSettings = mWebView.getSettings();
		mSettings.setJavaScriptEnabled(true);// 网页可显示js效果
		mSettings.setBuiltInZoomControls(true);// 显示缩放按钮（wap网页不支持）
		mSettings.setUseWideViewPort(true);// 双击缩放（wap网页不支持）

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// 网页进度的变化
				super.onProgressChanged(view, newProgress);

				System.out.println("newProgress------" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// 网页标题
				super.onReceivedTitle(view, title);

				System.out.println("title------" + title);
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// 网页开始加载时调用
				super.onPageStarted(view, url, favicon);
				mProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// 网页结束加载时调用
				super.onPageFinished(view, url);
				mProgress.setVisibility(View.GONE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 所有网页链接都会走这个方法
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_change_textsize:
			// 展示对话框
			showDialog();
			break;

		case R.id.btn_share:
             showShare();
			break;

		default:

			break;
		}

	}

	private void showShare() {
		
			 ShareSDK.initSDK(this);
			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 

			// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
			 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			 oks.setTitle("分享");
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			 oks.setTitleUrl("http://sharesdk.cn");
			 // text是分享文本，所有平台都需要这个字段
			 oks.setText("我是分享文本");
			 //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
			 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
			 // url仅在微信（包括好友和朋友圈）中使用
			 oks.setUrl("http://sharesdk.cn");
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
			 oks.setComment("我是测试评论文本");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 oks.setSite("ShareSDK");
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 oks.setSiteUrl("http://sharesdk.cn");

			// 启动分享GUI
			 oks.show(this);
			 
		
	}

	private int mWhich;// 单选框被选中条目的位置id
	private int mCurrentWhich=2;

	private WebSettings mSettings;
	private void showDialog() {
		// 创建AlertDialog.Builder 对象
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体设置");
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
						
		// 设置单选框
		builder.setSingleChoiceItems(items, mCurrentWhich,
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						mWhich = which;
						System.out.println("which----" + which);
					}
				});

		builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (mWhich) {
				case 0:
              //"超大号字体"
					mSettings.setTextSize(TextSize.LARGEST);
					
					break;
				case 1:
                  // "大号字体"
					mSettings.setTextSize(TextSize.LARGER);
					break;
				case 2:
                   //, "正常字体"
					mSettings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
                     //"小号字体",
					mSettings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
                    //"超小号字体"
					mSettings.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}
				mCurrentWhich =mWhich;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();

	}

}
