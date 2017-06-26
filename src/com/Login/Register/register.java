package com.Login.Register;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.Login.Util.Utils;
import com.Login.login.LoginActivity;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.SingleGlobalVariables;
import com.pub.http.HttpUtils;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 
 * @author tianq 最近修改 2016-08-16 4：06 PM
 */
public class register extends BaseActivity implements OnClickListener {
	TextView txt_title;
	ImageView img_back;
	Button btn_register, btn_send;
	EditText et_usertel, et_password, et_code;
	static boolean RegisterSucess = false;
	int i = 30;
	static int RESULT = 1;
	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_register);
		super.onCreate(savedInstanceState);
		AppExit.getInstance().addActivity(this);
		initControl();
		SMS();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

	/**
	 * 初始化控件
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText(R.string.register_title_text);
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_register = (Button) findViewById(R.id.btn_register);
		et_usertel = (EditText) findViewById(R.id.et_usertel);
		et_password = (EditText) findViewById(R.id.et_password);
		et_code = (EditText) findViewById(R.id.et_code);
	}

	/**
	 * 启动短信验证SDK，同时设置监听事件，获取从服务器返回的信息,再注册注册回调监听接口
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void SMS() {
		// 启动短信验证sdk
		SMSSDK.initSDK(this, SingleGlobalVariables.APPKEY, SingleGlobalVariables.APPSECRETE);
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
	}

	/**
	 * 接口监听
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	@Override
	protected void setListener() {
		// TODO 自动生成的方法存根
		img_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		et_usertel.addTextChangedListener(new TelTextChange());
		et_password.addTextChangedListener(new TextChange());
		et_code.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		// TODO
		String phoneNums = et_usertel.getText().toString();
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(register.this);
			break;
		case R.id.btn_send:
			send_SMS(phoneNums);
			break;
		case R.id.btn_register:
			// 将收到的验证码和手机号提交再次核对
			SMSSDK.submitVerificationCode("86", phoneNums, et_code.getText().toString());
			break;
		default:
			break;
		}
	}

	/**
	 * 发送短信验证，同时新建子线程，向主线程发送信息
	 * 
	 * @author Sun
	 * @param phoneNums
	 */
	private void send_SMS(String phoneNums) {
		// 2. 通过sdk发送短信验证 ֤
		SMSSDK.getVerificationCode("86", phoneNums);
		// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
		btn_send.setClickable(false);
		btn_send.setText("重新发送(" + i + ")");
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (; i >= 0; i--) {
					handler.sendEmptyMessage(-9);
					if (i == 0) {
						btn_send.setClickable(true);
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(-8);
			}
		}).start();
	}

	/**
	 * handler,接收SMSSDK来自服务器的消息，根据服务器的消息做处理
	 * 
	 * @author Sun
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				btn_send.setText("重新发送(" + i + ")");
			} else if (msg.what == -8) {
				btn_send.setText("获取验证码");
				btn_send.setClickable(true);
				i = 30;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				try {
					Throwable throwable = (Throwable) data;
					throwable.printStackTrace();
					JSONObject object = new JSONObject(throwable.getMessage());
					String des = object.optString("detail");// 错误描述
					int status = object.optInt("status");// 错误代码
					if (status > 0 && !TextUtils.isEmpty(des)) {
						Utils.showLongToast(register.this, des);
					}
				} catch (Exception e) {
					// do something
				}
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 短信注册成功后，返回MainActivity,然后提示
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {/// 提交验证码成功
						Utils.showShortToast(register.this, "提交验证码成功！");
						Log.i("提交验证码", "");
						RegisterSucess = true;
						getRegister();
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Utils.showShortToast(register.this, "正在获取验证码！");
						et_usertel.setFocusable(false);
						Log.i("正在获取验证码", "");
					}
				}
			}
		}
	};
	/**
	 * handler,根据来自服务器的消息进行操作
	 * 
	 * @author Sun
	 */
	Handler handler1 = new Handler() {
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
					if (success == 0) {
						//////////////////////////////////////////////////
						new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                try {
                                    // 调用sdk注册方法
                                    EMChatManager.getInstance()
                                            .createAccountOnServer(et_usertel.getText().toString(),
                                                    et_password.getText().toString());
                                    mHandler.sendEmptyMessage(1000);
                                } catch (final EaseMobException e) {
                                    // 注册失败
                                    Log.i("TAG", "getErrorCode:" + e.getErrorCode());
//                                    int errorCode = e.getErrorCode();
//                                    if (errorCode == EMError.NONETWORK_ERROR) {
//                                        mHandler.sendEmptyMessage(1001);
//                                    } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
//                                        mHandler.sendEmptyMessage(1002);
//                                    } else if (errorCode == EMError.UNAUTHORIZED) {
//                                        mHandler.sendEmptyMessage(1003);
//                                    } else {
//                                        Message msg = Message.obtain();
//                                        msg.what = 1004;
//                                        msg.obj = e.getMessage();
//                                        mHandler.sendMessage(msg);
//                                    }
                                }
                            }
                        }).start();
						/////////////////////////////////////////////////////////////////
						Intent intent = new Intent(register.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(register.this, "注册成功！", Toast.LENGTH_LONG).show();
						
					} else if (success == 1) {
						Toast.makeText(register.this, "注册失败，已存在该用户！", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(register.this, "注册失败，系统繁忙！", Toast.LENGTH_LONG).show();
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

	/**
	 * 注册功能实现，将获取到的信息打包成JSON格式，通过HttpUtils.httpPostMethod函数发送到服务器
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void getRegister() {
		final String name = et_usertel.getText().toString();
		final String pwd = et_password.getText().toString();
		String code = et_code.getText().toString();
		if (RegisterSucess) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						JSONObject json = new JSONObject();
						json.put("UserName", name);
						json.put("PassWord", pwd);
						HttpUtils.httpPostMethod(SingleGlobalVariables.registerurl, json, handler1);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("json", "解析JSON出错");
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
		btn_send.setEnabled(false);
		RegisterSucess = false;
	}

	/**
	 * TextChange事件监听
	 * @author Administrator
	 *
	 */
	class TelTextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before, int count) {
			String phone = et_usertel.getText().toString();
			if (phone.length() == 11) {
				if (Utils.isMobileNO(phone)) {
					btn_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
					btn_send.setTextColor(0xFFFFFFFF);
					btn_send.setEnabled(true);
					// btn_register.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
					// btn_register.setTextColor(0xFFFFFFFF);
					// btn_register.setEnabled(true);
				} else {
					et_usertel.requestFocus();
					Utils.showLongToast(getBaseContext(), "请输入正确的手机号");
				}
			} else {
				btn_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_send.setTextColor(0xFFD0EFC6);
				btn_send.setEnabled(false);
				btn_register.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_register.setTextColor(0xFFD0EFC6);
				btn_register.setEnabled(true);
			}
		}
	}

	/**
	 * Textchange监听类
	 * @author Sun
	 *
	 */
	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before, int count) {
			boolean Sign1 = et_code.getText().length() > 0;
			boolean Sign2 = et_usertel.getText().length() > 0;
			boolean Sign3 = et_password.getText().length() > 0;
			if (Sign1 & Sign2 & Sign3) {
				btn_register.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
				btn_register.setTextColor(0xFFFFFFFF);
				btn_register.setEnabled(true);
			} else {
				btn_register.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_register.setTextColor(0xFFD0EFC6);
				btn_register.setEnabled(false);
			}
		}
	}
}
