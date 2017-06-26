package com.Login.splash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.Login.Util.Utils;
import com.Login.login.LoginActivity;
import com.Login.login.Login_Hint;
import com.Login.viewPager.ViewPagerActivity;
import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.app.main.LoadUserData;
import com.example.hello1.R;
import com.exit.AppExit;
import com.profile.ImageTools;
import com.pub.SingleGlobalVariables;
import com.pub.SingleMyCalendar;
import com.pub.Dialog.AlertDialog;
import com.pub.http.HttpUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * App 启动动画
 * 
 * @author tianq 最近一次修改 2016-08-13 10：13 PM
 * @version 1.0
 * 
 */
public class SplashActivity extends InstrumentedActivity {
	// 判断是否是第一次安装
	static SharedPreferences sf;
	// 只有一条信息，就是存储登陆以后没有通过推出按钮退出的账号的用户名和密码
	static SharedPreferences sf_pass;
	// 对应账号的文件夹
	static SharedPreferences sf_userinfo;
	// 启动动画图片资源
	private ImageView imageView;
	// 保存路径
	String pathDir;// save
	String phonepath;
	String phone;
	String password;
	Handler handler;
	String Success;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		AppExit.getInstance().addActivity(this);
		findView();
		start_push();
		init();
		isNetworkAvailable();
		AlphaAnimation();
		myHandle();
	}

	/**
	 * 初始化View控件
	 * 
	 * @param void
	 * @author Sun
	 * @return void
	 * 
	 */
	private void findView() {
		imageView = (ImageView) findViewById(R.id.activity_splash_loadImage);
	}

	/**
	 * 初始化常量
	 * 
	 * @param void
	 * @return void
	 * @author Sun
	 */
	private void init() {
		pathDir = HelloApplication.getBaseCache();
		sf = HelloApplication.PDAConfig(this, "isFristInstall", true, pathDir, sf);
		sf_pass = getSharedPreferences("pass", MODE_PRIVATE);
	}

	/**
	 * 判断是否有网络信息，如果没有的话弹窗提醒
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void isNetworkAvailable() {
		if (!Utils.isNetworkAvailable(this)) {
			// 如果未连接网络则进行弹窗提示
			final ImageView imageView1 = (ImageView) findViewById(R.id.activity_splash_loadImage);
			new AlertDialog(this).builder().setTitle("未连接网络！").setMsg("部分功能需连接网络才能使用，是否前去设置网络？").show();
			try {
				Thread.sleep(5000);/*** 休眠2000毫秒 ***/
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 第一次打开该APP，设置isFristInstall.xml中的IsFirstOpen为false，设置pass.
	 * xml中的phone和password为0，同时跳转到引导页面
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void isFirstOpen() {
		Log.i("第一次登陆？", "是的");
		Editor editor = sf.edit();
		editor.putBoolean("IsFirstOpen", false);
		editor.commit();
		Editor editor1 = sf_pass.edit();
		editor1.putString("phone", "0");
		editor1.putString("password", "0");
		editor1.commit();
		Intent intent = new Intent(SplashActivity.this, ViewPagerActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}

	/**
	 * 从pass.xml中获取phone和password，同时设置SingleGlobalVariables.Phone的值
	 * 若取得值都为0，则进入登录界面， 若都不为0，则判断learn_Id，为0表示还未填写真实信息，否则表示已填写。
	 * learn_id为0直接进入真实信息填写界面 否则进入主页， 并从相关账号文件夹下加载信息
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void isNotFirstOpen() {
		Log.i("第一次登陆？", "不是");
		// 判断是不是存在了用户名和密码，有的话直接进入主页，否则进入登录页面
		phone = sf_pass.getString("phone", "0");
		password = sf_pass.getString("password", "0");
		SingleGlobalVariables.Phone = phone;
		Log.i("用户名", phone);
		Log.i("密码", password);
		if (phone.equals("0") && password.equals("0")) {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		} else {
			httpPost();
			// 登录成功
			
		}
	}

	@SuppressLint("HandlerLeak")
	private void myHandle() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						Log.i("获取到的RES", res);
						JSONObject result = new JSONObject(res);
						Success = result.getString("success");
						if (Success.equals("0")) {
							phonepath = HelloApplication.getPhonePath(phone);
							sf_userinfo = HelloApplication.PDAConfig(SplashActivity.this, "userinfo", true, phonepath, sf_userinfo);
							String learn_Id = sf_userinfo.getString("learn_Id", "0");
							if (learn_Id.equals("0")) {
								// 跳转到个人真实信息界面
								Intent intent = new Intent(SplashActivity.this, Login_Hint.class);
								startActivity(intent);
								finish();
							} else {
								// 从服务器加载信息
								LoadUserData.load(SplashActivity.this, phone);
								// 跳转到主页
								Intent intent = new Intent(SplashActivity.this, Main_PageActiviy.class);
								startActivity(intent);
								finish();
							}
						}else{//登录失败跳到登录界面
							Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};

	}

	/**
	 * 线程类。通过新建线程，将数据打包成JSON格式，通过调用HttpUtils.httpPostMethod方法，将JSON数据传递到服务器
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */

	private void httpPost() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					JSONObject json = new JSONObject();
					json.put("UserName", phone);
					json.put("PassWord", password);
					json.put("RegistrationID", JPushInterface.getRegistrationID(SplashActivity.this));
					HttpUtils.httpPostMethod(SingleGlobalVariables.loginurl, json, handler);
				} catch (JSONException e) {
					Log.d("json", "解析JSON出错");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	/**
	 * 开启动画效果,并判断是否是第一次登陆， 若是第一次登陆，调用isFirstOpen函数 若不是， 若不是，调用isNotFirstOpen函数
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void AlphaAnimation() {
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(1000);
		imageView.setAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (sf.getBoolean("IsFirstOpen", true)) {
					isFirstOpen();
				} else {
					isNotFirstOpen();
				}
			}
		});
	}

	/**
	 * 初始化极光推送，开启极光推送服务
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void start_push() {
		// 开启极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
