package com.Discover.notes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Course.Activity.Add_LessonActivity;
import com.Course.Activity.Course_Set;
import com.Course.Activity.Lesson_Detail;
import com.example.hello1.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class main_notes extends Activity {
	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	ListView GroupManList;
	BaseAdapter baseAdapter;
	DbHelper db;
	TextView NewNote;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_notes);
		
		db = new DbHelper(this); // 获得数据库对象
		initView();
		addData();
		newNote();
		init();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	/**
	 * 监听新建笔记按钮
	 */
	private void newNote() {
		NewNote = (TextView) findViewById(R.id.newnote);
		NewNote.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(main_notes.this, Notes_edit.class);
				Bundle bundle = new Bundle();
				bundle.putString("New", "true");
				bundle.putString("Title", "");
				bundle.putString("Content", "");
				bundle.putString("Id", "");
				bundle.putString("Type", "");
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
	}
	/**
	 * 
	 */
	private void init() {
		baseAdapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return data.size();
			}

			@Override
			public Object getItem(int position) {
				return data.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater layoutInflater = getLayoutInflater();
					convertView = layoutInflater.inflate(R.layout.manager_group_list_item_parent, parent, false);
				}
				Map<String, Object> itemData = (Map<String, Object>) getItem(position);
				TextView Title = (TextView) convertView.findViewById(R.id.notes_Title);
				TextView time = (TextView) convertView.findViewById(R.id.notes_time);
				TextView content = (TextView) convertView.findViewById(R.id.notes_content);
				Title.setText(itemData.get("title").toString());
				content.setText(itemData.get("content").toString());
				time.setText(itemData.get("time").toString());
				return convertView;
			}
		};
		GroupManList.setAdapter(baseAdapter);
		final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(GroupManList, swipeListener, this);
		touchListener.SwipeType = ListViewSwipeGesture.Dismiss; // 设置两个选项列表项的背景
		GroupManList.setOnTouchListener(touchListener);
		GroupManList.setOnItemClickListener(mOnItemClickListener);
	}

	/**
	 * 从数据库读取数据并新添加到适配器
	 * 
	 * @param itemData
	 */
	public void addData() {
		List<NotesObj> noteobj_list = db.queryNote();
		for (int i = 0; i < noteobj_list.size(); i++) {
			HashMap<String, Object> itemData = new HashMap<String, Object>();
			itemData.put("title", noteobj_list.get(i).getTitle());
			itemData.put("time", noteobj_list.get(i).getTime());
			itemData.put("content", noteobj_list.get(i).getContent());
			itemData.put("type", noteobj_list.get(i).getType());
			data.add(itemData);
		}
	}

	ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {
		@Override
		public void FullSwipeListView(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(main_notes.this, "Action_2", Toast.LENGTH_SHORT).show();
			data.remove(position);
			baseAdapter.notifyDataSetChanged();
		}

		@Override
		public void HalfSwipeListView(final int position) {
			// TODO Auto-generated method stub
			// System.out.println("<<<<<<<" + position);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// execute the task
					data.remove(position);
					baseAdapter.notifyDataSetChanged();
					Toast.makeText(main_notes.this, "删除", Toast.LENGTH_SHORT).show();
				}
			}, 500);
		}

		@Override
		public void LoadDataForScroll(int count) {
		}

		@Override
		public void onDismiss(ListView listView, int[] reverseSortedPositions) {
		}

		@Override
		public void OnClickListView(int position) {
			// TODO Auto-generated method stub
		}
	};
	/**
	 * 点击list单条列表
	 */
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (parent.getId() == R.id.note_list) {
				NotesObj notesObj = db.queryId(position+ 1+"");
				Log.i("222222",""+ db.queryId(position + 1 + "").getId());
				Intent intent = new Intent(main_notes.this, Notes_edit.class);
				Bundle bundle = new Bundle();
				bundle.putString("Title", notesObj.getTitle());
				bundle.putString("Content", notesObj.getContent());
				bundle.putString("Id", position+1+"");
				bundle.putString("New", "false");
				bundle.putString("Type", notesObj.getType());
				Log.i("111111111111111111111111",notesObj.getType() );
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		}
	};

	/**
	 * 初始化View
	 */
	private void initView() {
		GroupManList = (ListView) findViewById(R.id.note_list);
	}

}
