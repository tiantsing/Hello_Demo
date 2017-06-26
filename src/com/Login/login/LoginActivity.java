package com.Login.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.Login.Password.FgPassWord;
import com.Login.Register.register;
import com.Login.Util.Utils;
import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.app.main.LoadUserData;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.hello1.R;
import com.pub.http.HttpUtils;
import com.exit.AppExit;
import com.pub.SingleGlobalVariables;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

/**
 * App 登陆类
 * 
 * @author tianq 最近一次修改 2016-08-15 11：21 AM
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {
	Button register;// 注册
	Button login; // 登陆
	Button forgetpwd;// 忘记密码
	AutoCompleteTextView UserName;// 用户名
	EditText PassWord;// 密码
	SharedPreferences sf_pass;// 存储用户名密码
	SharedPreferences sf_userinfo;// 根据手机号分文件并存储信息
	static SharedPreferences sp_username;// use to save username
	Handler handler;
	String username;
	String password;
	String pathDir;// Smartland路径
	String phonepath;// 账号路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_main);
		AppExit.getInstance().addActivity(this);
		findView();
		init();
		autoComplete();
		myHandle();
		login();
		register();
		forgotpwp();
	}

	/**
	 * 初始化控件
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void findView() {
		UserName = (AutoCompleteTextView) findViewById(R.id.login_actv_username);
		PassWord = (EditText) findViewById(R.id.passWord);
	}

	/**
	 * 初始化变量
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void init() {
		pathDir = HelloApplication.getBaseCache();
		sf_pass = getSharedPreferences("pass", MODE_PRIVATE);
		sp_username = HelloApplication.PDAConfig(this, "passwordFile", true, pathDir, sp_username);
	}

	/**
	 * 用户账号自动填写功能函数，输入一部分数字可自动提示，可自动填入用户名密码
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void autoComplete() {
		UserName.setThreshold(1);// 输入1个字母就开始自动提示
		UserName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String[] allUserName = new String[sp_username.getAll().size()];// sp_username.getAll().size()返回的是有多少个键值对
				allUserName = sp_username.getAll().keySet().toArray(new String[0]);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginActivity.this,
						android.R.layout.simple_dropdown_item_1line, allUserName);
				UserName.setAdapter(adapter);// 设置数据适配器
			}

			@Override
			public void afterTextChanged(Editable s) {
				PassWord.setText(sp_username.getString(UserName.getText().toString(), ""));// 自动输入密码
			}
		});
	}

	/**
	 * handler线程类，接收子线程的数据，在主线程进行操作/
	 *  1.接收从服务器传来的消息，从读取出JSON数据包
	 *  2.若登录成功，将账号信息保存到对应账号下，同时将账号和密码存到userinfo.xml,用来显示账号提示。若登录后得到信息显示未填写个人真实信息
	 * ，则跳转到真实信息界面，否则跳转到主页
	 *  @author Sun
	 *  @param void
	 *  @return void
	 * 
	 */
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
						int success = Integer.parseInt(result.getString("success"));
						int learn_Id = Integer.parseInt(result.getString("learn_Id"));
						String id = result.getString("id");
						phonepath = HelloApplication.getPhonePath(username);
						sf_userinfo = HelloApplication.PDAConfig(LoginActivity.this, "userinfo", true, phonepath,
								sf_userinfo);
						Editor editor = sf_userinfo.edit();
						editor.putString("phone", username);
						editor.putString("password", password);
						editor.putString("learn_Id", learn_Id + "");
						editor.putString("id", id);//自动生成的id，传到服务器来连接school_id
						editor.commit();
						// 将用户名密码存储到，用来自动添加账号
						sp_username.edit().putString(username, password).commit();
						Editor editor2 = sf_pass.edit();
						editor2.putString("phone", username);
						editor2.putString("password", password);
						editor2.commit();
						// 将个人信息存储到对应账号下。
						if (success == 0) {
							Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
							Log.i("1111111111", success+"");
							Log.i("1111111111", learn_Id+"");
							if (learn_Id != 0) {
								EMChatManager.getInstance().login(username, password, new EMCallBack() {// 回调
									@Override
									public void onSuccess() {
										runOnUiThread(new Runnable() {
											public void run() {
												EMGroupManager.getInstance().loadAllGroups();
												EMChatManager.getInstance().loadAllConversations();
												Log.d("main", "登陆聊天服务器成功！");
												Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
												startActivity(new Intent(LoginActivity.this, Main_PageActiviy.class));
											}
										});
									}

									@Override
									public void onProgress(int progress, String status) {

									}

									@Override
									public void onError(int code, String message) {
										if (code == -1005) {
											message = "用户名或密码错误";
										}
										final String msg = message;
										runOnUiThread(new Runnable() {
											public void run() {
												Log.d("main", "登陆聊天服务器失败！");
												Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
											}
										});
									}
								});
								SingleGlobalVariables.Phone = username;
								LoadUserData.load(LoginActivity.this, username);
								Intent intent = new Intent(LoginActivity.this, Main_PageActiviy.class);
								startActivity(intent);
								finish();
							} else {
								Intent intent = new Intent(LoginActivity.this, Login_Hint.class);
								startActivity(intent);
								finish();
							}
						} else {
							Toast.makeText(LoginActivity.this, "输入的用户名或密码有错", Toast.LENGTH_LONG).show();
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
     * 登录按钮监听；
     * 监听登录按钮，触发后判断手机号和密码是否符合要求，不符合则显示提示，否则调用httpPost函数
     * @author Sun
     * @param void
     * @return void
     */
	private void login() {
		login = (Button) findViewById(R.id.login_btn);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				username = UserName.getText().toString();
				password = PassWord.getText().toString();
				if (!Utils.isMobileNO(username)) {
					Utils.showLongToast(LoginActivity.this, "请输入准确的手机号！ ");
					return;
				} else if (password.equals("")) {
					Utils.showLongToast(LoginActivity.this, "请输入密码！ ");
					return;
				} else {
					httpPost();
					//////////////////////////////////////////////
					
					///////////////////////////////////////////////
				}
			}
		});
	}
	/**
	 * 线程类。通过新建线程，将数据打包成JSON格式，通过调用HttpUtils.httpPostMethod方法，将JSON数据传递到服务器
	 * @author Sun
	 * @param void
	 * @return void
	 */
    private void httpPost()
    {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					JSONObject json = new JSONObject();
					json.put("UserName", UserName.getText().toString().trim());
					json.put("PassWord", PassWord.getText().toString().trim());
					json.put("RegistrationID", JPushInterface.getRegistrationID(LoginActivity.this));
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
	 * 注册按钮监听；
	 * 监听注册按钮，当监听事件触发时，页面跳转到注册界面
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void register() {
		register = (Button) findViewById(R.id.reg_tv);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, register.class);
				startActivity(intent);
			}
		});
	}
    /**
     * 找回密码按钮监听；
     * 监听忘记密码按钮，当监听事件触发时，页面跳转到找回密码页面
     */
	private void forgotpwp() {
		// TODO 自动生成的方法存根
		forgetpwd = (Button) findViewById(R.id.forget_password_tv);
		forgetpwd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(LoginActivity.this, FgPassWord.class);
				startActivity(intent);
			}
		});
	}

}
