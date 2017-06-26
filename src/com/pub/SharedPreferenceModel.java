package com.pub;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharedPreferenceModel {
	SharedPreferences sf;
	Editor editor ;
    public SharedPreferenceModel(Context context) {
		// TODO Auto-generated constructor stub
    	sf = context.getSharedPreferences("save", context.MODE_PRIVATE);
    	editor =sf.edit();
	}
 
    public void savespinnerPosition(int spinnerPosition)
    {
    	editor.putInt("spinnerPosition", spinnerPosition);//spinnerPosition是点击spinner设置的值，而不是默认的当前周数
		Log.i("spinnerPosotion", (spinnerPosition) + "");
		editor.commit();
    }
    public int getspinnerPosition()
    {
    	return sf.getInt("spinnerPosition", 1);
    }
}
