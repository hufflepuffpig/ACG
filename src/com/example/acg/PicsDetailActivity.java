package com.example.acg;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class PicsDetailActivity extends Activity
{
	private LinearLayout linearLayout;
	private ImageView img_detial;
	private String img_url;
	
	private ImageLoaderConfiguration config;
	private DisplayImageOptions options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picsdetail_main);
		ActionBar actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		linearLayout=(LinearLayout) findViewById(R.id.pics_layout);
		img_detial=(ImageView) findViewById(R.id.img_detail);
		img_url=getIntent().getStringExtra("img_url");
		
		config=new ImageLoaderConfiguration.Builder(PicsDetailActivity.this).build();
		ImageLoader.getInstance().init(config);
		options=new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		
		final LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ImageLoader.getInstance().displayImage(img_url, img_detial, options, new ImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String arg0, View arg1)
			{
				// TODO Auto-generated method stub
				ProgressBar progressBar=new ProgressBar(PicsDetailActivity.this);
				progressBar.setLayoutParams(layoutParams);
				linearLayout.addView(progressBar,0);
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
			{
				// TODO Auto-generated method stub
				linearLayout.removeViewAt(0);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
