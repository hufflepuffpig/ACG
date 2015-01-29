package com.example.acg;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification.Action;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity
{

	private NewsNode node;
	private int node_num;
	//UI部分
	private PullToRefreshListView list;
	private BaseAdapter baseAdapter;
	private ArrayList<CommentNode> commentsList=new ArrayList<>();
	//网络加载部分
	private static AsyncHttpClient client=new AsyncHttpClient();
	private int curNum=1;
	private boolean no_more=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_main);
		ActionBar actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		node=(NewsNode) getIntent().getSerializableExtra("node");
		node_num=getIntent().getIntExtra("item_num", 0);

		baseAdapter=new BaseAdapter()
		{
			
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2)
			{
				// TODO Auto-generated method stub
				CommentNode node=commentsList.get(arg0);
				View view=getLayoutInflater().inflate(R.layout.comment_item, null);
				TextView nickname=(TextView) view.findViewById(R.id.nickname);
				nickname.setText(node.nickname);
				TextView floornum=(TextView) view.findViewById(R.id.floornum);
				floornum.setText(node.floor+"楼");
				TextView comment=(TextView) view.findViewById(R.id.comment);
				comment.setText(node.comment);
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
				return commentsList.size();
			}
		};
		getCommentsFromServer(node.id+"", "1");
		list=(PullToRefreshListView) findViewById(R.id.comments_list);
		list.setAdapter(baseAdapter);
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
		{

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				// TODO Auto-generated method stub
				getCommentsFromServer(node.id+"", ""+curNum);
			}
		});
		
	}

	public void getCommentsFromServer(String news_id,String from)
	{
		RequestParams requestParams=new RequestParams();
		requestParams.put("news_id", news_id);
		requestParams.put("from", from);
		client.get(MainActivity.url+"getcommentsV2.php", requestParams, new JsonHttpResponseHandler(){

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
							CommentNode node=new CommentNode();
							node.news_id=object.getInt("news_id");
							node.nickname=object.getString("nickname");
							node.comment=object.getString("comment");
							node.floor=object.getInt("floor");
							commentsList.add(node);
						} 
						baseAdapter.notifyDataSetChanged();
						curNum+=response.length();
						if(response.length()<10)
							no_more=true;
					}
					list.onRefreshComplete();
				}catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newsdetail_menu, menu);
		return true;
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
		if(id==R.id.comment_menu)
		{
			final EditText editText=new EditText(CommentActivity.this);
			AlertDialog.Builder builder=new AlertDialog.Builder(CommentActivity.this);
			builder.setTitle("评论");
			builder.setView(editText);
			builder.setNegativeButton("取消", null);
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// TODO Auto-generated method stub
					SharedPreferences sharedPreferences=getSharedPreferences("data", NewsDetailActivity.MODE_PRIVATE);
					String nickname=sharedPreferences.getString("username", "?");
					String news_id=node.id+"";
					String comment=editText.getText().toString();
					String floor=""+curNum;
					if(comment==null)
					{
						Toast.makeText(CommentActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
					}
					else {
						RequestParams requestParams=new RequestParams();
						requestParams.put("news_id", news_id);
						requestParams.put("nickname", nickname);
						requestParams.put("comment", comment);
						requestParams.put("floor", floor);
						client.get(MainActivity.url+"setCommentsV2.php", requestParams, new AsyncHttpResponseHandler()
						{
							
							@Override
							public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
							{
								// TODO Auto-generated method stub
								NewsPreViewFragment.newsList.get(node_num).curfloor++;
								no_more=false;
								getCommentsFromServer(node.id+"", ""+curNum);
							}
							
							@Override
							public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
							{
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
			});
			AlertDialog dlg=builder.create();
			dlg.setCanceledOnTouchOutside(false);
			dlg.show();
		}
		return super.onOptionsItemSelected(item);
	}

}
