package com.app_lock;

import com.example.hello1.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class learn_setting extends Activity implements OnClickListener{
	   private ImageView back;
	   private TextView title;
	    public learn_setting() {
	        // TODO Auto-generated constructor stub
	    }
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        // TODO Auto-generated method stub
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.lock_activity_setting);
	        
	        init();
	    }
	    private void init() {
	        // TODO 自动生成的方法存根
	        back=(ImageView)findViewById(R.id.img_back);
	        title=(TextView)findViewById(R.id.txt_title);
	        back.setVisibility(View.VISIBLE);
	        title.setText("学习设置");
	        back.setOnClickListener(this);
	    }
	    @Override
	    public void onClick(View v) {
	        switch (v.getId()) {
	        case R.id.img_back:
	             Intent intent = new Intent(learn_setting.this,main_time_control.class);
	             startActivity(intent);
	            break;

	        default:
	            break;
	        }
	        
	    }
	}

