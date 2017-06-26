package com.Login.Password;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.Login.Register.BaseActivity;
import com.Login.Util.Utils;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.SingleGlobalVariables;
import com.pub.http.HttpUtils;
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

@SuppressWarnings("deprecation")
public class FgPassWord extends BaseActivity implements OnClickListener {
	private TextView txt_title;
	private ImageView img_back;
	private Button btn_ensure, btn_send;
	private EditText et_usertel, et_newpassword, et_code, et_ensurenewpassword;
	int i = 30;
	static int RESULT = 1;
	static boolean ISENSURE = false;

	protected void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_forgotpassword);
		super.onCreate(savedInstanceState);
		AppExit.getInstance().addActivity(this);
	}
	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("修改密码");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		btn_send = (Button) findViewById(R.id.btn_fpwd_send);
		et_usertel = (EditText) findViewById(R.id.et_fpwd_usertel);
		et_code = (EditText) findViewById(R.id.et_fpwd_code);
		btn_ensure = (Button) findViewById(R.id.next_btn);
		et_newpassword = (EditText) findViewById(R.id.et_newpassword);
		et_ensurenewpassword = (EditText) findViewById(R.id.et_ensurepassword);
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

	@Override
	protected void setListener() {
		// TODO 自动生成的方法存根
		img_back.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btn_ensure.setOnClickListener(this);
		et_usertel.addTextChangedListener(new TelTextChange());
		et_newpassword.addTextChangedListener(new TextChange());
		et_ensurenewpassword.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		String phoneNums = et_usertel.getText().toString();
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(FgPassWord.this);
			break;
		case R.id.btn_fpwd_send:
			// 2. 通过sdk发送短信验证 ֤
			SMSSDK.getVerificationCode("86", phoneNums);

			// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
			btn_send.setClickable(false);
			btn_send.setText("重新发送(" + i + ")");
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (; i > 0; i--) {
						handler.sendEmptyMessage(-9);
						if (i <= 0) {
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
			break;
		case R.id.next_btn:
			// 将收到的验证码和手机号提交再次核对
			SMSSDK.submitVerificationCode("86", phoneNums, et_code.getText().toString());
			break;
		default:
			break;
		}
	}

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
				Log.e("event", "event=" + event);
				try {
					Throwable throwable = (Throwable) data;
					throwable.printStackTrace();
					JSONObject object = new JSONObject(throwable.getMessage());
					String des = object.optString("detail");// 错误描述
					int status = object.optInt("status");// 错误代码
					if (status > 0 && !TextUtils.isEmpty(des)) {
						Utils.showLongToast(FgPassWord.this, des);
					}
				} catch (Exception e) {
					// do something
				}
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 短信注册成功后，返回MainActivity,然后提示
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {/// 提交验证码成功
						Utils.showShortToast(FgPassWord.this, "提交验证码成功！");
						ISENSURE = true;
						getNewPass();
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Utils.showShortToast(FgPassWord.this, "正在获取验证码！");
						et_usertel.setFocusable(false);
					} else {
						((Throwable) data).printStackTrace();
					}
				}
			}
		}
	};
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
						Toast.makeText(FgPassWord.this, "修改成功！", Toast.LENGTH_LONG).show();
						setResult(RESULT);
						finish();
					} else {
						Toast.makeText(FgPassWord.this, "修改失败，未找到该用户！", Toast.LENGTH_LONG).show();
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

	// 点击注册按钮后
	private void getNewPass() {
		final String name = et_usertel.getText().toString();
		final String code = et_code.getText().toString();
		final String pass = et_newpassword.getText().toString();
		String ensurepass = et_ensurenewpassword.getText().toString();
		if (pass.equals(ensurepass)) {
			if (ISENSURE) {
				new Thread() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							JSONObject json = new JSONObject();
							json.put("UserName", name);
							json.put("PassWord", pass);

							HttpUtils.httpPostMethod(SingleGlobalVariables.forgetPassurl, json, handler1);
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
		} else {
			Toast.makeText(FgPassWord.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
		}

		btn_ensure.setEnabled(false);
		btn_send.setEnabled(false);
	}

	public class TelTextChange implements TextWatcher {

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
				} else {
					et_usertel.requestFocus();
					Utils.showLongToast(getBaseContext(), "请输入正确的手机号码！");
				}
			} else {
				btn_send.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_send.setTextColor(0xFFD0EFC6);
				btn_send.setEnabled(false);
				btn_ensure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_ensure.setTextColor(0xFFD0EFC6);
				btn_ensure.setEnabled(true);
			}
		}
	}

	// 手机号 EditText监听器
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
			boolean Sign3 = et_newpassword.getText().length() > 0;
			boolean Sign4 = et_ensurenewpassword.getText().length() > 0;
			if (Sign1 & Sign2 & Sign3 & Sign4) {
				btn_ensure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
				btn_ensure.setTextColor(0xFFFFFFFF);
				btn_ensure.setEnabled(true);
			} else {
				btn_ensure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				btn_ensure.setTextColor(0xFFD0EFC6);
				btn_ensure.setEnabled(false);
			}
		}
	}

}
