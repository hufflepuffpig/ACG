package com.example.acg;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class NewsDetailActivity extends Activity
{
	private WebView webView;
	private ActionBar actionBar;
	private SharedPreferences sharedPreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsdetail_main);
		actionBar=getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		webView=(WebView) findViewById(R.id.webview_newsdetail);
		NewsNode node=(NewsNode) getIntent().getSerializableExtra("node");
		
		webView.loadUrl(MainActivity.url+"details/"+node.link);
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
			sharedPreferences=getSharedPreferences("data", NewsDetailActivity.MODE_PRIVATE);
			String username=sharedPreferences.getString("username", "?");
			if(username.equals("?"))
			{
				Toast.makeText(NewsDetailActivity.this, "没登陆不给评论哦(⊙o⊙)", Toast.LENGTH_SHORT).show();
			}
			else {
				NewsNode node=(NewsNode) getIntent().getSerializableExtra("node");
				Intent intent=new Intent(NewsDetailActivity.this, CommentActivity.class);
				intent.putExtra("node", node);
				intent.putExtra("item_num", getIntent().getIntExtra("item_num", 0));
				startActivity(intent);
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
}
