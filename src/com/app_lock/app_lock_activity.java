package com.app_lock;

import java.util.ArrayList;
import java.util.List;

import android.R.interpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CursorJoiner.Result;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app_lock.dao.AppLockDBDao;
import com.app_lock.domain.AppInfo;
import com.app_lock.service.WatchDogService;
import com.app_lock.utils.AppInfoProvider;
import com.example.hello1.R;
import com.exit.AppExit;
import com.android.*;
public class app_lock_activity extends Activity {
	private SharedPreferences sp;
	private AppInfo appInfo;
	private ListView lv_app;
	private LinearLayout ll_proBar;
	private List<AppInfo> appInfos;//
	private List<AppInfo> userAppInfos;
	private TextView bt_setting;
	private TextView tv_status;
	private MyAdapter adapter;
	private AppLockDBDao dao;
	private static String TAG = "MainActivity";
    static final int Result = 1;
	AlertDialog dialog;
	EditText et_password;
	EditText et_password_confirm;
	Button bt_ok;
	Button bt_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lock_activity);
		AppExit.getInstance().addActivity(this);
		
		dao = new AppLockDBDao(this);
		bt_setting = (TextView) findViewById(R.id.txt_right);
		lv_app = (ListView) findViewById(R.id.lv_app);
		ll_proBar = (LinearLayout) findViewById(R.id.ll_probar);
		tv_status = (TextView) findViewById(R.id.tv_status);
		bt_setting.setText("确定");
		final Intent intent = new Intent(this, WatchDogService.class);
		bt_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			setResult(Result);
			finish();
			}
		});
		fillData();
		lv_app.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0 || position == userAppInfos.size() + 1) { // 
					return;
				} else if (position <= userAppInfos.size()) {
					int newPosition = position - 1;
					appInfo = userAppInfos.get(newPosition);
				}
				ViewHolder holder = (ViewHolder) view.getTag();
				ImageView iv_applock = holder.iv_applock;
				if (dao.find(appInfo.getPackName())) {
					dao.delete(appInfo.getPackName());
					iv_applock.setImageResource(R.drawable.unlock);
				} else {
					dao.add(appInfo.getPackName());
					iv_applock.setImageResource(R.drawable.lock);
				}
			}
		});
		lv_app.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (userAppInfos != null) {// 
					if (firstVisibleItem <= userAppInfos.size()) {
						tv_status.setText("用户应用" + userAppInfos.size() + "个");
						tv_status.setBackgroundColor(getResources().getColor(
								R.color.bac));
						tv_status.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	private void fillData() {
		new Thread() {
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(app_lock_activity.this);
				userAppInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					userAppInfos.add(info);
					dao.add(info.getPackName());
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ll_proBar.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new MyAdapter();
							lv_app.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		}.start();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder;
			if (position == 0) {
				TextView textView = new TextView(app_lock_activity.this);
				textView.setText("用户应用" + userAppInfos.size() + "个");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(getResources()
						.getColor(R.color.bac));
				return textView;
			}
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(app_lock_activity.this,
						R.layout.list_item_applock, null);
				holder = new ViewHolder();
				holder.iv_app_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.tv_app_name = (TextView) view
						.findViewById(R.id.tv_app_name);
				holder.iv_applock = (ImageView) view
						.findViewById(R.id.iv_applock);
				view.setTag(holder);
			}
			if (position < userAppInfos.size() + 1) {
				holder.iv_app_icon.setImageDrawable(userAppInfos.get(
						position - 1).getAppIcon());
				holder.tv_app_name.setText(userAppInfos.get(position - 1)
						.getName());
				if (dao.find(userAppInfos.get(position - 1).getPackName())) {
					holder.iv_applock.setImageResource(R.drawable.lock);
				} else {
					holder.iv_applock.setImageResource(R.drawable.unlock);
				}
			}
			return view;
		}
	}

	static class ViewHolder {
		ImageView iv_app_icon;
		TextView tv_app_name;
		TextView tv_app_location;
		ImageView iv_applock;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
}