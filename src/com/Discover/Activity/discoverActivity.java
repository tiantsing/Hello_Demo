package com.Discover.Activity;

import com.Course.Activity.MainCourseActiviy;
import com.MainPage.Activity.Main_PageActiviy;
import com.Message.Activity.MessageActivity;
import com.example.hello1.R;
import com.exit.AppExit;
import com.profile.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class discoverActivity extends Activity {
	Button register;
	Button login;
	Button forgetpwd;
	AutoCompleteTextView UserName;
	EditText PassWord;
	Handler handler;
	String username;
	String password;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.discover_main);
		AppExit.getInstance().addActivity(this);
		init();
	}
	/**
	 * 初始化变量
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void init() {
		//底部菜单栏发现为选中状态
		RelativeLayout find = (RelativeLayout) findViewById(R.id.re_Find);
		find.setSelected(true);
		title = (TextView) findViewById(R.id.txt_title);
		title.setText("发现");
	}
	/**
	 * 重定义后退事件，若触发后退键，直接退到HOME
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	/**
	 * 底部栏的触摸事件，根据值不同触发不同的事件
	 * @param view
	 * @return void
	 */
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_zy:
			Intent intent =new Intent(discoverActivity.this, Main_PageActiviy.class);
			startActivity(intent);
			break;
		case R.id.open_course:
			Intent intent1 = new Intent(discoverActivity.this, MainCourseActiviy.class);
			startActivity(intent1);
			break;
		case R.id.re_message:
			Intent intent11 = new Intent(discoverActivity.this, MessageActivity.class);
			startActivity(intent11);
			break;
		case R.id.re_Find:
			break;
		case R.id.re_profile:
			Intent intent111 = new Intent(discoverActivity.this, User.class);
			startActivity(intent111);
			break;
		}
	}
}
