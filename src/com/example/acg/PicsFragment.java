package com.example.acg;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

public class PicsFragment extends Fragment
{
	//UI部分
	private StaggeredGridView gridView;
	private BaseAdapter baseAdapter;
	private ArrayList<String> picsNameArrayList=new ArrayList<>();
	//网络部分
	private AsyncHttpClient client=new AsyncHttpClient();
	private boolean no_more=false;
	private int curNum=1;
	private boolean loading=false;
	//异步图片加载部分
	private ImageLoaderConfiguration config;
	private DisplayImageOptions options;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.pics_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//获取StaggeredView对象
		gridView=(StaggeredGridView) getActivity().findViewById(R.id.grid_view);
		//定义好异步图片加载的配置
		config=new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader.getInstance().init(config);
		options=new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		
		getPicsNameFromServer("1");
		
		baseAdapter=new BaseAdapter()
		{
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2)
			{
				// TODO Auto-generated method stub
				final LinearLayout linearLayout=new LinearLayout(getActivity());
				ImageView img=new ImageView(getActivity());
				final LayoutParams layoutParams=new LayoutParams(getResources().getDisplayMetrics().widthPixels/2, LayoutParams.WRAP_CONTENT);
				img.setLayoutParams(layoutParams);
				linearLayout.addView(img);
				ImageLoader.getInstance().displayImage(MainActivity.url+"PicsV2/"+picsNameArrayList.get(arg0), img, options, new ImageLoadingListener()
				{
					
					@Override
					public void onLoadingStarted(String arg0, View arg1)
					{
						// TODO Auto-generated method stub
						ProgressBar progressBar=new ProgressBar(getActivity());
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
				return linearLayout;
			}
			
			@Override
			public long getItemId(int arg0)
			{
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int arg0)
			{
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount()
			{
				// TODO Auto-generated method stub
				return picsNameArrayList.size();
			}
		};
		gridView.setAdapter(baseAdapter);
		gridView.setOnScrollListener(new AbsListView.OnScrollListener()
		{
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3)
			{
				// TODO Auto-generated method stub
				if(!loading)
				{
					int lastinScreen=arg1+arg2;
					if(lastinScreen>=arg3)
					{
						loading=true;
						getPicsNameFromServer(""+curNum);
					}
				}
			}
		});
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), PicsDetailActivity.class);
				String img_url=MainActivity.url+"PicsV2/"+picsNameArrayList.get(arg2);
				intent.putExtra("img_url", img_url);
				startActivity(intent);
			}
		});
	}
	
	void getPicsNameFromServer(String from)
	{
		client.get(MainActivity.url+"getpicsnameV2.php", new RequestParams("from", from), new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				// TODO Auto-generated method stub
				try
				{
					if(!no_more)
					{
						for(int i=0;i<response.length();i++)
						{
							JSONObject object=response.getJSONObject(i);
							picsNameArrayList.add(object.getString("name"));
						}
						baseAdapter.notifyDataSetChanged();
						curNum+=response.length();
						if(response.length()<8)
							no_more=true;
					}
					loading=false;
				} catch (Exception e)
				{
					// TODO: handle exception
				}
			}
			
		});
	}

}
