package com.example.acg;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends Activity
{
	private ActionBar actionBar;
	public static String url="http://192.168.2.2/myroot/";
	
	public static NewsPreViewFragment newsPreViewFragment=new NewsPreViewFragment();
	public static PicsFragment picsFragment=new PicsFragment();
	public static UserFragment userFragment=new UserFragment();

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBar=getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for(int i=0;i<3;i++)
		{
			ActionBar.Tab tab=actionBar.newTab();
			switch(i)
			{
			case 0:
				tab.setText("动漫资讯");break;
			case 1:
				tab.setText("动漫美图");break;
			case 2:
				tab.setText("用户设置");break;
			}
			tab.setTabListener(new MyTabListener());
			actionBar.addTab(tab);
		}
		
		getFragmentManager().beginTransaction()
		.add(R.id.main_layout, newsPreViewFragment)
		.add(R.id.main_layout, picsFragment)
		.add(R.id.main_layout, userFragment)
		.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			final EditText ip_ediText=new EditText(MainActivity.this);
			AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("set IP address");
			builder.setView(ip_ediText);
			builder.setNegativeButton("取消", null);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// TODO Auto-generated method stub
					String ip=ip_ediText.getText().toString();
					MainActivity.url="http://"+ip+"/myroot/";
				}
			});
			builder.create().show();
		}
		return super.onOptionsItemSelected(item);
	}

	private class MyTabListener implements ActionBar.TabListener
	{

		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1)
		{
			// TODO Auto-generated method stub
			Log.d("Test", ""+arg0.getPosition());
			if(arg0.getPosition()==0)
			{
				getFragmentManager().beginTransaction()
				.hide(userFragment)
				.hide(picsFragment)
				.show(newsPreViewFragment)
				.commit();
			}
			else if(arg0.getPosition()==1)
			{
				getFragmentManager().beginTransaction()
				.hide(userFragment)
				.hide(newsPreViewFragment)
				.show(picsFragment)
				.commit();
			}
			else if(arg0.getPosition()==2)
			{
				getFragmentManager().beginTransaction()
				.hide(newsPreViewFragment)
				.hide(picsFragment)
				.show(userFragment)
				.commit();
			}
		}

		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
		{
			// TODO Auto-generated method stub
			
		}
		
	}
}
