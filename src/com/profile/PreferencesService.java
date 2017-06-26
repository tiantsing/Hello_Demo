package com.profile;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;
	
	public PreferencesService(Context context) {
		this.context = context;
	}
	
	public void save(String name, String sex, String brith, String home, String school, String collage, String ruxue, String class1) {
		SharedPreferences preferences = context.getSharedPreferences("User_information", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putString("sex", sex);
		editor.putString("brith", brith);
		editor.putString("home", home);
		editor.putString("school", school);
		editor.putString("collage", collage);
		editor.putString("ruxue", ruxue);
		editor.putString("class", class1);
		editor.commit();
	}
	
	public Map<String, String> getPreferences(){
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("User_information", Context.MODE_PRIVATE);
		params.put("name", preferences.getString("name", ""));
		params.put("sex", preferences.getString("sex", ""));
		params.put("brith", preferences.getString("brith", ""));
		params.put("home", preferences.getString("home", ""));
		params.put("school", preferences.getString("school", ""));
		params.put("collage", preferences.getString("collage", ""));
		params.put("ruxue", preferences.getString("ruxue", ""));
		params.put("class", preferences.getString("class", ""));
		return params;
	}
}
