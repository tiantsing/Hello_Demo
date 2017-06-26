package com.Login.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.example.hello1.R;
import com.exit.AppExit;
import com.pub.SingleGlobalVariables;
import com.pub.http.HttpUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import android.view.Window;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class Login_info extends Activity implements OnClickListener {
	Handler handler;// 线程
	Button submit; // 提交
	// 布局
	LinearLayout sex, school, collage, major, year;// bclass,
	// 中间值
	String sexattachValue, schoolattachValue, collageattachValue, majorattachValue; // bclassattachValue;
	int yearattchValue;
	// 显示控件
	TextView sexValue, schoolValue, collageValue, majorValue, yearValue;// bclassValue
	// 系统日历
	Calendar c = null;
	// 确认已阅读服务
	CheckBox ruleChecked;
	// 输入学号，姓名
	EditText xuhaoValue, nameValue;
	// 账号信息xml
	SharedPreferences sf_userinfo;
	Editor editor;
	String res;// 用于上传到服务器，并判断用户是否存在
	String phonepath;// 账号信息保存路径

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_info);
		AppExit.getInstance().addActivity(this);
		// 找到控件
		findView();
		// 初始化
		init();
		myHandler();
		ClickListener();
	}

	/**
	 * handler类； 新建handler，接收子线程的信息，根据消息进行处理；
	 * 从消息中读取JSON包，从中读取数据，如果成功，进入到主页，否则，提示错误信息
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	@SuppressLint("HandlerLeak")
	private void myHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						res = msg.getData().getString("res");
						Log.i("获取到的RES", res);
						JSONObject result = new JSONObject(res);
						int success = Integer.parseInt(result.getString("success"));
						Editor editor = sf_userinfo.edit();
						editor.putString("learn_Id", result.getString("learn_Id"));
						editor.commit();
						if (success == 0) {
							Intent intent = new Intent(Login_info.this, Main_PageActiviy.class);
							startActivity(intent);
							finish();
						} else if (success == 1) {
							Toast.makeText(Login_info.this, "输入失败,已存在该用户!", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(Login_info.this, "输入失败,系统繁忙!", Toast.LENGTH_LONG).show();
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
	 * 初始化控件
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void findView() {
		submit = (Button) findViewById(R.id.bt_login_info_submit);
		xuhaoValue = (EditText) findViewById(R.id.et_login_info_xuehao1);
		nameValue = (EditText) findViewById(R.id.et_login_info_name);
		sex = (LinearLayout) findViewById(R.id.re_login_info_sex);
		sexValue = (TextView) findViewById(R.id.tv_login_info_sex1);
		school = (LinearLayout) findViewById(R.id.re_login_info_school);
		schoolValue = (TextView) findViewById(R.id.tv_login_info_school1);
		collage = (LinearLayout) findViewById(R.id.re_login_info_collage);
		collageValue = (TextView) findViewById(R.id.tv_login_info_collage1);
		major = (LinearLayout) findViewById(R.id.re_login_info_major);
		majorValue = (TextView) findViewById(R.id.tv_login_info_major1);
		year = (LinearLayout) findViewById(R.id.re_login_info_year);
		yearValue = (TextView) findViewById(R.id.tv_login_info_year2);
		ruleChecked = (CheckBox) findViewById(R.id.cb_login_ifo_rule);
	}

	/**
	 * 初始化变量
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void init() {
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo = HelloApplication.PDAConfig(Login_info.this, "userinfo", true, phonepath, sf_userinfo);
	}
    
	/**
	 * 对部分控件进行点击事件监听，对另部分控件进行Change事件监听
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void ClickListener() {
		/* 对控件进行监听 */
		submit.setOnClickListener(this);
		submit.setEnabled(false);
//		sex.setEnabled(true);
//		school.setEnabled(true);
//		collage.setEnabled(true);
//		major.setEnabled(true);
//		year.setEnabled(true);
//		sexValue.setEnabled(true);
//		schoolValue.setEnabled(true);
//		collageValue.setEnabled(true);
//		majorValue.setEnabled(true);
//		yearValue.setEnabled(true);
		submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
		submit.setTextColor(0xFFD0EFC6);
		sex.setOnClickListener(this);
		school.setOnClickListener(this);
		collage.setOnClickListener(this);
		major.setOnClickListener(this);
		year.setOnClickListener(this);
		/* 对控件中的Change进行监听 */
		xuhaoValue.addTextChangedListener(new TextChange());
		nameValue.addTextChangedListener(new TextChange());
		sexValue.addTextChangedListener(new TextChange());
		schoolValue.addTextChangedListener(new TextChange());
		collageValue.addTextChangedListener(new TextChange());
		majorValue.addTextChangedListener(new TextChange());
		yearValue.addTextChangedListener(new TextChange());
	}

	/**
	 * 对部分按钮进行监听。 按钮触发监听事件，触发相应的函数
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_info_submit:
			httpTodatebase();
			Toast.makeText(this, res, Toast.LENGTH_LONG).show();
			break;
		case R.id.re_login_info_sex:
			dialog_sex();
			break;
		case R.id.re_login_info_school:
			dialog_school();
			break;
		case R.id.re_login_info_collage:
			dialog_collage();
			break;
		case R.id.re_login_info_major:
			dialog_major();
			break;
		case R.id.re_login_info_year:
			dialog_year();
			break;
		default:
			break;
		}

	}

	/**
	 * 将填写的数据保存到账号下面的文件夹中的userinfo.xml中，并新建线程，将数据打包成JSON格式，将其上传到服务器
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	public void httpTodatebase() {
		editor = sf_userinfo.edit();
		editor.putString("xuehao", xuhaoValue.getText().toString());
		editor.putString("name", nameValue.getText().toString());
		editor.putString("major", majorattachValue);
		editor.putString("collage", collageattachValue);
		editor.putString("school", schoolattachValue);
		editor.putString("sex", sexattachValue);
		editor.putInt("to_schoolyear", yearattchValue);
		editor.putString("RegistrationID", JPushInterface.getRegistrationID(getApplicationContext()));
		editor.commit();
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					JSONObject json = new JSONObject();
					json.put("xuehao", xuhaoValue.getText().toString());
					json.put("name", nameValue.getText().toString());
					json.put("id", sf_userinfo.getString("id", "0"));
					json.put("phone", sf_userinfo.getString("phone", ""));
					json.put("school", schoolattachValue);
					json.put("collage", collageattachValue);
					json.put("major", majorattachValue);
					// json.put("class", bclassattachValue);
					json.put("sex", sexattachValue);
					json.put("RegistrationID", JPushInterface.getRegistrationID(getApplicationContext()));
					json.put("to_schoolyear", yearattchValue);
					HttpUtils.httpPostMethod(SingleGlobalVariables.user_infourl, json, handler);
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

	/**
	 * CheckBox监听； 监听CheackBox点击事件
	 * 
	 * @author Sun
	 * @param void
	 * @return void
	 */
	private void sumbit_before_check() {
		// 给CheckBox设置事件监听
		ruleChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_bg_green));
					submit.setTextColor(0xFFFFFFFF);
					submit.setEnabled(true);

				} else {
					submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
					submit.setTextColor(0xFFD0EFC6);
					submit.setEnabled(false);
				}
			}
		});

	}

	/**
	 * 选择入学年份类
	 * 
	 */
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			yearValue.setText(year + "/" + (month + 1) + "/" + day);
			yearattchValue = year;
		}
	}

	/**
	* 选择入学年份控件
	*/
	private void dialog_year() {
		DatePickerFragment datePicker = new DatePickerFragment();
		datePicker.show(getFragmentManager(), "datePicker");
	}
    /**
     * 选择班级控件
     * @author Sun
     * @param void
     * @return void
     */
	@SuppressWarnings("unused")
	private void dialog_class() {
		final String items[] = SingleGlobalVariables.class_item;
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("选择班级"); // 设置标题
		builder.setIcon(R.drawable.ic_launcher);// 设置图标，图片id即可
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// bclassattachValue = items[which];
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// bclassValue.setText(bclassattachValue);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
    /**
     * 选择系控件
     * @author Sun
     * @param void
     * @return void
     */
	private void dialog_major() {
		final String items[] = SingleGlobalVariables.major_item;
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("选择专业"); // 设置标题
		builder.setIcon(R.drawable.ic_launcher);// 设置图标，图片id即可
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				majorattachValue = items[which];
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				majorValue.setText(majorattachValue);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
    /**
     * 选择系控件
     * @author Sun
     * @param void
     * @return void
     */
	private void dialog_collage() {
		final String items[] = SingleGlobalVariables.collage_item;
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("选择学院"); // 设置标题
		builder.setIcon(R.drawable.ic_launcher);// 设置图标，图片id即可
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				collageattachValue = items[which];
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				collageValue.setText(collageattachValue);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
    /**
     * 选择学校控件
     * @author Sun
     * @param void
     * @return void
     */
	private void dialog_school() {
		final String items[] = SingleGlobalVariables.school_item;
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("选择学校"); // 设置标题
		builder.setIcon(R.drawable.ic_launcher);// 设置图标，图片id即可
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				schoolattachValue = items[which];
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				schoolValue.setText(schoolattachValue);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
	/**
	 * 选择性别控件
	 * @author Sun
     * @param void
     * @return void
     */
	private void dialog_sex() {
		final String items[] = { "男", "女" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("性别选择"); // 设置标题
		builder.setIcon(R.drawable.ic_launcher);// 设置图标，图片id即可
		builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sexattachValue = items[which];
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				sexValue.setText(sexattachValue);

			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}
    /**
     * TextChange监听类
     * @author Sun
     * @param void
     * @return void
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
			boolean Sign1 = xuhaoValue.getText().length() > 0;
			boolean Sign2 = nameValue.getText().length() > 0;
			boolean Sign3 = sexValue.getText().length() > 0;
			boolean Sign4 = schoolValue.getText().length() > 0;
			boolean Sign5 = collageValue.getText().length() > 0;
			boolean Sign6 = majorValue.getText().length() > 0;
			// boolean Sign7 = bclassValue.getText().length() > 0;
			boolean Sign8 = yearValue.getText().length() > 0;

			if (Sign1 & Sign2 & Sign3 & Sign4 & Sign5 & Sign6 & Sign8) {
				sumbit_before_check();
			} else {
				submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_enable_green));
				submit.setTextColor(0xFFD0EFC6);
				submit.setEnabled(false);
			}
		}
	}
}
