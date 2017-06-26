package com.profile;

import com.Course.Activity.MainCourseActiviy;
import com.Discover.Activity.discoverActivity;
import com.MainPage.Activity.Main_PageActiviy;
import com.Message.Activity.MessageActivity;
import com.PJ.pj.testTeacher;
import com.app.main.HelloApplication;
import com.example.hello1.R;
import com.example.hello1.R.id;
import com.exit.AppExit;
import com.pub.SingleGlobalVariables;
import com.pub.Dialog.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class User extends Activity implements OnClickListener {
	final static String Smail_phone = "18865513985";
	RelativeLayout relativeLayout;
	TextView tvname, feedback, exitAll, set, title, tvmsg;
	ImageView head;
	// 路径
	String phonepath, passpath;
	/** SharedPreferences服务 **/
	SharedPreferences sf_userinfo;
	SharedPreferences sf_pass;
	// 联系客服
	private TextView contant_support;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user);
		AppExit.getInstance().addActivity(this);
		init();
	}

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

	public void isExit() {
		new AlertDialog(User.this).builder().setTitle("确定退出吗？").setMsg("上课期间退出会被视为逃课哦！")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						Editor editor = sf_pass.edit();
						editor.putString("phone", "0");
						editor.putString("password", "0");
						editor.commit();
						AppExit exit = AppExit.getInstance();
						exit.getConext(User.this);
						exit.stopService();
						exit.exit();
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();
	}

	public void init() {
		RelativeLayout mine = (RelativeLayout) findViewById(R.id.re_profile);
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(User.this, "userinfo", true, phonepath, sf_userinfo);
		passpath = HelloApplication.getBaseCache();
		sf_pass = HelloApplication.PDAConfig(User.this, "pass", true, passpath, sf_pass);
		mine.setSelected(true);
		// TODO 自动生成的方法存根
		title = (TextView) findViewById(R.id.txt_title);
		tvname = (TextView) findViewById(R.id.tvname);
		tvmsg = (TextView) findViewById(R.id.tvmsg);
		relativeLayout = (RelativeLayout) findViewById(R.id.view_user);
		feedback = (TextView) findViewById(R.id.txt_smail);
		exitAll = (TextView) findViewById(R.id.txt_exit);
		head = (ImageView) findViewById(R.id.head);
		set = (TextView) findViewById(R.id.user_txt_set);
		contant_support = (TextView) findViewById(R.id.txt_phone);
		title.setText("我");
		tvname.setText(sf_userinfo.getString("name", "未命名"));
		tvmsg.setText(sf_userinfo.getString("learn_Id", "0"));

		feedback.setOnClickListener(this);
		relativeLayout.setOnClickListener(this);
		exitAll.setOnClickListener(this);
		set.setOnClickListener(this);
		contant_support.setOnClickListener(this);

		Bitmap photoBitmap = ImageTools.getPhotoFromSDCard(phonepath, "head");
		if (photoBitmap != null) {
			head.setImageBitmap(photoBitmap);
		}

	}

	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_zy:
			Intent intent = new Intent(User.this, Main_PageActiviy.class);
			startActivity(intent);
			break;
		case R.id.open_course:
			open_course();
			break;
		case R.id.re_message:
			Intent intent11 = new Intent(User.this, MessageActivity.class);
			startActivity(intent11);
			break;
		case R.id.re_Find:
			Intent intent111 = new Intent(User.this, discoverActivity.class);
			startActivity(intent111);
			break;
		case R.id.re_profile:
			break;
		default:
			break;
		}
	}

	// 联系客服
	private void toPhone() {
		new AlertDialog(User.this).builder().setTitle("确定拨打客服电话吗？").setMsg("客服电话：+86 18865513985")
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// String mobile = Smail_phone.toString();
						// 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Smail_phone));
						startActivity(intent);
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				}).show();

	}

	// 打开教师评价
	public void open_pj() {

		Intent intent = new Intent(User.this, testTeacher.class);
		startActivity(intent);

	}

	// 打开课程表
	public void open_course() {
		Intent intent = new Intent(User.this, MainCourseActiviy.class);
		startActivity(intent);

	}

	// 打开个人
	public void open_profile() {

		Intent intent = new Intent(User.this, User.class);
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_user:
			Intent intent = new Intent(User.this, Userinfor.class);
			startActivity(intent);
			break;
		case R.id.txt_smail:
			Intent intent2 = new Intent(User.this, FeedbackActivity.class);
			startActivity(intent2);
			break;
		case R.id.txt_exit:
			isExit();
			break;
		case R.id.user_txt_set:
			Intent intent3 = new Intent(User.this, setting.class);
			startActivity(intent3);
			break;
		case R.id.txt_phone:
			toPhone();
			break;
		default:
			break;
		}
	}

}
