package com.Login.login;

import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
/**
 * 提示用户填写的信息是保密的
 * @author tianq
 * 最近一次修改 2016-08-15 10：49 AM
 */
public class Login_Hint extends Activity implements OnClickListener {
    //下一步按钮
	private Button next;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login_hint);
		AppExit.getInstance().addActivity(this);
		// 初始化
		init();
	}
    /**
     * 初始化控件
     * @author Sun
     * @param void
     * @return void
     */
	private void init() {
		next = (Button) findViewById(R.id.btn_login_hint_next);
		next.setOnClickListener(this);
	}
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_hint_next:
			Intent intent = new Intent(Login_Hint.this, Login_info.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}

	}
}
