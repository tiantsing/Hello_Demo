package com.profile;

import com.Login.Util.Utils;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends Activity implements OnClickListener{
  
	private TextView feedback,fbsubmit,title;
	private EditText et_message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.login_feedback);
		AppExit.getInstance().addActivity(this);
		init();
	}
	public void init() {
		et_message = (EditText)findViewById(R.id.et_message);
		feedback=(TextView)findViewById(R.id.txt_left1);
		fbsubmit=(TextView)findViewById(R.id.txt_right);
		title=(TextView)findViewById(R.id.txt_title);
		
		title.setText("意见反馈");
		feedback.setText("返回");
		fbsubmit.setText("提交");
		
		feedback.setOnClickListener(this);
		fbsubmit.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
      switch (v.getId()) {
	case R.id.txt_left1:
		Intent intent = new Intent(FeedbackActivity.this, User.class);
		startActivity(intent);
		break;
	case R.id.txt_right:
		submityz();
		
		break;
	default:
		break;
	}	
	}
	//此函数为对反馈意见的判断
	private void submityz() {
		String strMessage=et_message.getText().toString();
		if (isNoBlankAndNoNull(strMessage)) {
			Utils.showLongToast(this, "提交成功！");
			Intent intent1 = new Intent(FeedbackActivity.this, User.class);
			startActivity(intent1);
		}else{
			Utils.showLongToast(this, "请输入意见内容再提交，谢谢^_^");
		}
	}
	/**
	 * 判断字符串是否非空非null
	 * @param strParm 需要判断的字符串
	 * @return 真假
	 */
    public static boolean isNoBlankAndNoNull(String strParm)
    {
      return ! ( (strParm == null) || (strParm.equals("")));
    }
	
}
