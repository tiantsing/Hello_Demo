package com.Discover.notes;

import com.Course.db.DBHelper;
import com.example.hello1.R;
import com.pub.SingleGlobalVariables;
import com.pub.SingleMyCalendar;

import android.R.raw;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import cn.jpush.a.a.s;

public class Notes_edit extends Activity {

	TextView save;
	TextView biaoti;
	RadioGroup radio;
	EditText Title;
	EditText Content;
	Intent intent;
	String m_Title;
	String m_Content;
	String m_Id;
	String m_new;
	String TitleType;
	String m_type;
	DbHelper db;
	boolean Sucess;
	boolean New;
	RadioGroup radioGroup;
	RadioButton putong;
	RadioButton zuoye;
	RadioButton biji;
	RadioButton shiwu;
	Spinner zuoyeSpinner;
	SingleMyCalendar singleMyCalendar;
	String kemu;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.notes_edit);
		init();
		initEdit();
		save();
		RadioListener();
	}

	/**
	 * 初始化变量
	 */
	private void init() {
		radioGroup = (RadioGroup) findViewById(R.id.notes_type);
		putong = (RadioButton) findViewById(R.id.notes_putong);
		zuoye = (RadioButton) findViewById(R.id.notes_zuoye);
		biji = (RadioButton) findViewById(R.id.notes_biji);
		shiwu = (RadioButton) findViewById(R.id.notes_shiwu);
		save = (TextView) findViewById(R.id.notes_save);
		radio = (RadioGroup) findViewById(R.id.notes_type);
		Title = (EditText) findViewById(R.id.Edit_notes_title);
		Content = (EditText) findViewById(R.id.edit_notes_content);
		zuoyeSpinner = (Spinner) findViewById(R.id.spinner_kemu);
		biaoti = (TextView) findViewById(R.id.notes_biaoti);
		intent = getIntent();
		m_Content = intent.getStringExtra("Content");
		m_Title = intent.getStringExtra("Title");
		m_new = intent.getStringExtra("New");
		m_Id = intent.getStringExtra("Id");
		m_type = intent.getStringExtra("Type");
		TitleType = "普通-";
		kemu = SingleGlobalVariables.kemu[0];
		Sucess = false;
		db = new DbHelper(this);
		singleMyCalendar = SingleMyCalendar.getInstance();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SingleGlobalVariables.kemu);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		zuoyeSpinner.setAdapter(adapter);
	}

	/**
	 * 监听Spinner
	 */
	private void SpinnerListener() {
		zuoyeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				kemu = SingleGlobalVariables.kemu[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	/**
	 * 监听Radio
	 */
	private void RadioListener() {
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == putong.getId()) {
					TitleType = "普通-";
					zuoyeSpinner.setVisibility(View.INVISIBLE);
					biaoti.setText("标题");
					Title.setVisibility(View.VISIBLE);
				} else if (checkedId == zuoye.getId()) {
					TitleType = "作业-";
					zuoyeSpinner.setVisibility(View.VISIBLE);
					Log.i("11111111111", "1111111111111");
					biaoti.setText("作业");
					Title.setVisibility(View.INVISIBLE);
				} else if (checkedId == shiwu.getId()) {
					TitleType = "事物-";
					zuoyeSpinner.setVisibility(View.INVISIBLE);
					biaoti.setText("标题");
					Title.setVisibility(View.VISIBLE);
				} else if (checkedId == biji.getId()) {
					TitleType = "笔记-";
					zuoyeSpinner.setVisibility(View.INVISIBLE);
					biaoti.setText("标题");
					Title.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	/**
	 * 初始化Title和Content
	 */
	private void initEdit() {
		if (!m_Title.equals(""))
			Title.setText(m_Title);
		if (!m_Content.equals(""))
			Content.setText(m_Content);
		if (m_new.equals("true"))
			New = true;
		else
			New = false;
		if (m_type.equals("普通-"))
			putong.setChecked(true);
		else if (m_type.equals("作业-"))
			zuoye.setChecked(true);
		else if (m_type.equals("事物-"))
			shiwu.setChecked(true);
		else if (m_type.equals("笔记-"))
			biji.setChecked(true);
		if (m_type.equals("作业-")) {
			zuoyeSpinner.setVisibility(View.VISIBLE);
			biaoti.setText("作业");
			Title.setVisibility(View.INVISIBLE);
		} else {
			zuoyeSpinner.setVisibility(View.INVISIBLE);
			biaoti.setText("标题");
			Title.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 保存事件
	 */
	private void save() {
		save = (TextView) findViewById(R.id.notes_save);
		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Sucess) {
					Sucess = true;
					String title = "";
					if (!TitleType.equals("作业-")) {
						title = TitleType + Title.getText().toString();
					} else {
						title = TitleType + kemu;
					}
					String content = Content.getText().toString();
					String time = singleMyCalendar.getNowTime();
					Log.i("N", New + "");
					if (New) {
						Log.i("insert1111111111111", "");
						db.insertNote(title, content, time, TitleType);
					} else {
						Log.i("update222222222222", "");
						db.updateNote(m_Id, title, content, time, TitleType);
					}
					Intent intent = new Intent(Notes_edit.this, main_notes.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

}
