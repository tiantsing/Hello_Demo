package com.profile;

import com.example.hello1.R;
import com.exit.AppExit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class about extends Activity implements OnClickListener {

	private TextView title;
	private ImageView back;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_about);
		AppExit.getInstance().addActivity(this);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.img_back);
		title = (TextView) findViewById(R.id.txt_title);

		back.setVisibility(View.VISIBLE);
		title.setText("关于我们");

		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Intent intent = new Intent(about.this, setting.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
}
