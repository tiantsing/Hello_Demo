package com.app.main;

import com.example.hello1.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends Activity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	final Window win = getWindow();
    	 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    	 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    	 win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
    	 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    	setContentView(R.layout.clock_warn);
    }
}
