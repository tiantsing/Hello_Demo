package com.MainPage.Model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.MainPage.Activity.Main_PageActiviy;
import com.bluetooth.Bluetooth;
import com.example.hello1.R;
import com.pub.SingleGlobalVariables;
import com.pub.http.HttpUtils;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 *  今日课程适配器
 *  @author Sun
 */
public class ListViewCourseAdapter extends BaseAdapter {
	public final class ViewHolder {
		public TextView jieshu;
		public TextView name;
		public TextView room;
		public Button signed;
	}

	List<Map<String, Object>> mData;
	LayoutInflater mInflater;
	Main_PageActiviy main_pagePresenter;
	Context context;
	Calendar calendar;
	Bluetooth bluetooth;
	Handler handler;

	public ListViewCourseAdapter(Context context, List mData) {
		this.mInflater = LayoutInflater.from(context);
		this.mData = mData;
		main_pagePresenter = new Main_PageActiviy();
		this.context = context;
		bluetooth = new Bluetooth(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mainpage_course_item, null);
			holder.jieshu = (TextView) convertView.findViewById(R.id.list_course_start);
			holder.name = (TextView) convertView.findViewById(R.id.list_course_name);
			holder.room = (TextView) convertView.findViewById(R.id.list_course_position);
			holder.signed = (Button) convertView.findViewById(R.id.signed);
			holder.signed.setId(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.jieshu.setText((String) mData.get(position).get("course_jieshu"));
		holder.name.setText((String) mData.get(position).get("course_name"));
		holder.room.setText((String) mData.get(position).get("course_room"));
		holder.signed.setBackgroundResource((Integer) mData.get(position).get("signed"));
		holder.signed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String classroom = mData.get(position).get("course_room").toString();
				int start = Integer.parseInt(mData.get(position).get("start").toString());
				int time = SingleGlobalVariables.time[start][0];
				calendar = Calendar.getInstance();
				int Minute = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60;
				if (Minute >= time - 5 && Minute <= time + 5) {
					// 签到模块
                    sign(start);
					
				} else if (Minute < time - 2) {
					Toast.makeText(context, "未到该节课签到时间", 1000).show();
				} else if (Minute > time + 5) {
					Toast.makeText(context, "已过该节课签到时间", 1000).show();
				}

			}
		});
		return convertView;
	}

	public void Handler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					try {
						String res = msg.getData().getString("res");
						Log.i("获取到的RES", res);
						JSONObject result = new JSONObject(res);
						int success = Integer.parseInt(result.getString("success"));
						if(success==0){//如果签到成功
							bluetooth.StopBlueToothSign();
						}else{
							Toast.makeText(context, "签到失败！", 1000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
		};
	}

	public void sign(final int start) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					bluetooth.StartBlueToothSign();
					Log.i("MAC地址", "" + bluetooth.putAddress());
					Log.i("蓝牙名称", "" + bluetooth.putName());
					Log.i("蓝牙Type", "" + bluetooth.putType());
					Log.i("蓝牙类别", "" + bluetooth.putBluetoothClass());
					JSONObject json = new JSONObject();
					json.put("start", start);
					json.put("Address", bluetooth.putAddress());
					json.put("Name", bluetooth.putName());
					json.put("Type", bluetooth.putType());
					json.put("BluetoothClass", bluetooth.putBluetoothClass());
					HttpUtils.httpPostMethod(SingleGlobalVariables.loginurl, json, handler);
				} catch (JSONException e) {
					Log.d("json", "解析JSON出错");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
}
