package com.profile;

import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Rule extends Activity implements OnClickListener{

	private TextView title;
	private ImageView back;
	private TextView agreement;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.user_rule);
	    AppExit.getInstance().addActivity(this);
		init();
	}

	private void init() {
		back=(ImageView)findViewById(R.id.img_back);
		title=(TextView)findViewById(R.id.txt_title);
		agreement = (TextView) this.findViewById(R.id.agreement);
		
		back.setVisibility(View.VISIBLE);
		title.setText("使用条款与隐私政策");
		agreement.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		back.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			 Intent intent =new Intent(Rule.this, setting.class);
			 startActivity(intent);
			break;

		default:
			break;
		}
		
	}
}
