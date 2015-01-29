package com.example.acg;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserFragment extends Fragment
{

	private SharedPreferences sharedPreferences;
	private String username,password;
	private boolean isInLogin_main,isInInfo_main;
	private AsyncHttpClient client;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		sharedPreferences=getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
		username=sharedPreferences.getString("username", "?");
		password=sharedPreferences.getString("password", "?");
		Log.d("Test", username+"  "+password);
		if(username.equals("?"))
		{
			Log.d("Test", "aaaa");
			isInLogin_main=true;
			isInInfo_main=false;
			return inflater.inflate(R.layout.user_login_main, container, false);
		}
		else {
			Log.d("Test", "bbbb");
			isInLogin_main=false;
			isInInfo_main=true;
			return inflater.inflate(R.layout.user_info_main,container,false);
		}
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		client=new AsyncHttpClient();
		if(isInLogin_main)
		{
			final EditText username_edit=(EditText) getActivity().findViewById(R.id.username_edit);
			final EditText password_edit=(EditText) getActivity().findViewById(R.id.password_edit);
			final Button login_btn=(Button) getActivity().findViewById(R.id.login_btn);
			login_btn.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					login_btn.setEnabled(false);
					Log.d("Test", "login click");
					RequestParams requestParams=new RequestParams();
					requestParams.add("username", username_edit.getText().toString());
					requestParams.add("password", password_edit.getText().toString());
					client.get(MainActivity.url+"loginV2.php", requestParams, new AsyncHttpResponseHandler()
					{
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
						{
							// TODO Auto-generated method stub
							Log.d("Test", "access success");
							AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
							switch(new String(arg2))
							{
							case "1":
								builder.setMessage("登陆成功");
								sharedPreferences=getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
								SharedPreferences.Editor editor=sharedPreferences.edit();
								editor.putString("username", username_edit.getText().toString());
								editor.putString("password", password_edit.getText().toString());
								editor.commit();
								builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
								{
									
									@Override
									public void onClick(DialogInterface arg0, int arg1)
									{
										// TODO Auto-generated method stub
										
										getActivity().getFragmentManager().beginTransaction()
										.remove(MainActivity.userFragment)
										.commit();
										MainActivity.userFragment=new UserFragment();
										getActivity().getFragmentManager().beginTransaction()
										.add(R.id.main_layout, MainActivity.userFragment)
										.show(MainActivity.userFragment)
										.commit();
									}
								});
								break;
							case "2":
								builder.setMessage("密码错误");
								builder.setPositiveButton("确定", null);
								break;
							case "3":
								builder.setMessage("账号不存在");
								builder.setPositiveButton("确定", null);
								break;
							}
							builder.create().show();
							Log.d("Test", "aaaa");
							login_btn.setEnabled(true);
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
						{
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
			final Button register_btn=(Button) getActivity().findViewById(R.id.register_btn);
			register_btn.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					register_btn.setEnabled(false);
					RequestParams requestParams=new RequestParams();
					requestParams.add("username", username_edit.getText().toString());
					requestParams.add("password", password_edit.getText().toString());
					client.get(MainActivity.url+"registerV2.php", requestParams, new AsyncHttpResponseHandler()
					{
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
						{
							// TODO Auto-generated method stub
							AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
							switch(new String(arg2))
							{
							case "0":
								builder.setMessage("该账号已被注册");break;
							case "1":
								builder.setMessage("注册成功");break;
							}
							builder.setPositiveButton("确定", null);
							builder.create().show();
							register_btn.setEnabled(true);
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
						{
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
		}
		else if(isInInfo_main)
		{
			final TextView username_tv=(TextView) getActivity().findViewById(R.id.username_tv);
			final Button logout_btn=(Button) getActivity().findViewById(R.id.logout_btn);
			username_tv.setText(username);
			logout_btn.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					sharedPreferences=getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
					SharedPreferences.Editor editor=sharedPreferences.edit();
					editor.putString("username", "?");
					editor.putString("password", "?");
					editor.commit();
					getActivity().getFragmentManager().beginTransaction()
					.remove(MainActivity.userFragment)
					.commit();
					MainActivity.userFragment=new UserFragment();
					getActivity().getFragmentManager().beginTransaction()
					.add(R.id.main_layout, MainActivity.userFragment)
					.show(MainActivity.userFragment)
					.commit();
				}
			});
		}
	}
	

}
