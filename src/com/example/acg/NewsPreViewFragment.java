package com.example.acg;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import android.R.integer;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;



public class NewsPreViewFragment extends Fragment
{
	//UI部分
	private PullToRefreshListView list;
	private BaseAdapter baseAdapter;
	public static ArrayList<NewsNode> newsList=new ArrayList<>();
	//网络部分
	private static AsyncHttpClient client=new AsyncHttpClient();
	private int curNum=1;
	private boolean no_more=false;
	//异步加载图片
	private ImageLoaderConfiguration config;
	private DisplayImageOptions options;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.newspreview_main, container, false);
	}
	

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		config=new ImageLoaderConfiguration.Builder(getActivity()).build();
		ImageLoader.getInstance().init(config);
		options=new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.build();
		
		baseAdapter=new BaseAdapter()
		{
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2)
			{
				// TODO Auto-generated method stub
				NewsNode node=newsList.get(arg0);
				View view=getActivity().getLayoutInflater().inflate(R.layout.newspreview_item, null);
				ImageView imgView=(ImageView) view.findViewById(R.id.img_newspreview);
				ImageLoader.getInstance().displayImage(MainActivity.url+"images/"+node.img, imgView, options);
				TextView headingView=(TextView) view.findViewById(R.id.heading_newspreview);
				headingView.setText(node.heading);
				TextView textingView=(TextView) view.findViewById(R.id.texting_newspreview);
				textingView.setText(node.texting);
				return view;
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
				return newsList.size();
			}
		};
		getFromServer("1");
		list=(PullToRefreshListView) getActivity().findViewById(R.id.list);
		list.setAdapter(baseAdapter);
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
				{

					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView)
					{
						// TODO Auto-generated method stub
						getFromServer(""+curNum);
					}
				});
		list.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3)
				{
					// TODO Auto-generated method stub
					Log.e("arg2", ""+arg2);
					NewsNode node=newsList.get(arg2-1);
					Intent intent=new Intent(getActivity(), NewsDetailActivity.class);
					intent.putExtra("node", node);
					intent.putExtra("item_num", arg2-1);
					startActivity(intent);
				}
			});
	}
	
	
	
	
	void getFromServer(String from)
	{
		client.get(MainActivity.url+"getnewsV2.php", new RequestParams("from", from), new JsonHttpResponseHandler()
		{
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
							NewsNode node=new NewsNode();
							node.id=object.getInt("id");
							node.img=object.getString("img");
							node.heading=object.getString("heading");
							node.texting=object.getString("texting");
							node.link=object.getString("link");
							node.curfloor=object.getInt("curfloor");
							newsList.add(node);
						}
						baseAdapter.notifyDataSetChanged();
						curNum+=4;
						if(response.length()<4)
							no_more=true;
					}
					list.onRefreshComplete();
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	

	
	
}
