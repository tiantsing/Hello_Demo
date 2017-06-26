package com.Message.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.Common.Constants;
import com.Course.Activity.MainCourseActiviy;
import com.Discover.Activity.discoverActivity;
import com.MainPage.Activity.Main_PageActiviy;
import com.Message.Adapter.ConversationAdapter;
import com.Message.Adapter.FriendListAdapter;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.exceptions.EaseMobException;
import com.example.hello1.R;
import com.exit.AppExit;
import com.profile.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 信息功能处理主函数
 * 
 * @author tianq
 *
 */
public class MessageActivity extends Activity {
	private View view_friend;// 需要滑动的页卡->联系人页卡
	private View view_news;// 需要滑动的页卡->会话页卡
	private ViewPager mVPActivity;// 消息页面的viewpager
	private List<View> viewList;// 把需要滑动的页卡添加到这个list中
	private List<String> titleList;// viewpager的标题
	int num;// List大小
	public List<Map<String, Object>> adapterList = null;

	private ImageView head_img;// 右下角按钮
	private ImageView bt1;// 右下角按钮点击后显示的按钮1
	private ImageView bt2;// 右下角按钮点击后显示的按钮2
	private boolean tag = true;// bool标识 tag=true则显示bt1、bt2 tag=false则隐藏bt1、bt2
	// message 1
	ListView ListView_news;// 消息界面列表
	ListView ListView_lxr;// 联系人列表
	/* 常量 */
	private final int CODE_ADD_FRIEND = 0x00001;
	protected Handler mHandler;// 添加好友时用到的Handler
	private List<String> data = new ArrayList<String>();// 联系人列表的数据
	private View addView;// 添加好友的view
	private EditText mIdET;// 需要添加好友的ID
	private EditText mReasonET;// 添加好友的原因
	FriendListAdapter mAdapter;// 自定义好又列表适配器
	ConversationAdapter nlAdapter;// 自定义最近联系人列表
	private List<EMConversation> normal_list = new ArrayList<EMConversation>();// 最近普通会话

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.message);
		AppExit.getInstance().addActivity(this);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case CODE_ADD_FRIEND:
					Toast.makeText(getApplicationContext(), "请求发送成功，等待对方验证", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}

		};
		

		init();
		initViewPager();

	}

	// 初始化
	public void init() {
		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		EMChat.getInstance().setAppInited();
		// 底部菜单栏消息为选中状态
		RelativeLayout msm = (RelativeLayout) findViewById(R.id.re_message);
		msm.setSelected(true);
		// ViewPager
		mVPActivity = (ViewPager) findViewById(R.id.message_activity);
		
	}

	// 初始化ViewPager
	private void initViewPager() {
		/* 联系人页面 */
		view_friend = findViewById(R.layout.message_all);

		// 加载布局管理器
		LayoutInflater lf = getLayoutInflater().from(this);
		// 将xml布局转换为view对象
		view_friend = lf.inflate(R.layout.message_all, null);
		ListView_lxr = (ListView) view_friend.findViewById(R.id.message_all_friend_list);
		initList();
		/* 新消息 */
		view_news = findViewById(R.layout.message_news);
		// 加载布局管理器
		LayoutInflater lf1 = getLayoutInflater().from(this);
		// 将xml布局转换为view对象
		view_news = lf1.inflate(R.layout.message_news, null);
		// 将要分页显示的View装入数组中
		viewList = new ArrayList<View>();
		viewList.add(view_news);
		viewList.add(view_friend);
		// 每个页面的Title数据
		titleList = new ArrayList<String>();
		titleList.add("消息");
		titleList.add("联系人");

		adapterList = new ArrayList<Map<String, Object>>();
        //最近会话列表表的处理
		ListView_news = (ListView) view_news.findViewById(R.id.message_news_listView);
		normal_list.addAll(loadConversationsWithRecentChat());
		nlAdapter = new ConversationAdapter(MessageActivity.this, normal_list);
		ListView_news.setAdapter(nlAdapter);
		// 联系人列表项监听

		ListView_lxr.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// //转入chatActivity
				Intent it = new Intent(MessageActivity.this, ChatActivity.class);
				it.putExtra(Constants.MEMBER_ID, data.get(arg2));
				Log.e("所选中的联系人ID为：", data.get(arg2));
				MessageActivity.this.startActivityForResult(it, 1);
			}
		});
        //*************以下为右下角的小按钮  start***********************//
		head_img = (ImageView) view_friend.findViewById(R.id.head_img);
		bt1 = (ImageView) view_friend.findViewById(R.id.bt1);
		bt2 = (ImageView) view_friend.findViewById(R.id.bt2);
		head_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (tag) {
					bt1.setVisibility(View.VISIBLE);
					bt2.setVisibility(View.VISIBLE);
					openAnimationAp_bg();
					openAnimationBt();
					// open_head_img_RotateAnimation();
				} else {
					cloesAnimationBt();
					cloesAnimationAp_bg();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							bt1.setVisibility(View.GONE);
							bt2.setVisibility(View.GONE);
						}
					}, 100);

				}
				tag = !tag;
			}
		});
		mVPActivity.setAdapter(new pagerAdapter());//本条为为给ViewPage设置适配器
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.message_chat_add_friends, null);
				mIdET = (EditText) addView.findViewById(R.id.chat_add_friend_id);
				mReasonET = (EditText) addView.findViewById(R.id.chat_add_friend_reason);
				new AlertDialog.Builder(MessageActivity.this).setTitle("添加好友").setView(addView)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								String idStr = mIdET.getText().toString().trim();
								String reasonStr = mReasonET.getText().toString().trim();
								try {
									EMContactManager.getInstance().addContact(idStr, reasonStr);
									mHandler.sendEmptyMessage(CODE_ADD_FRIEND);
								} catch (EaseMobException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.i("TAG", "addContacterrcode==>" + e.getErrorCode());
								} // 需异步处理
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.dismiss();
							}
						}).create().show();

			}
		});
	}
	//*************以下为右下角的小按钮  end***********************//
	private void initList() {
		try {
			data.clear();
			data = EMContactManager.getInstance().getContactUserNames();
			mAdapter = new FriendListAdapter(MessageActivity.this, data);
			ListView_lxr.setAdapter(mAdapter);
		} catch (EaseMobException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.i("TAG", "usernames errcode==>" + e1.getErrorCode());
			Log.i("TAG", "usernames errcode==>" + e1.getMessage());
		} // 需异步执行
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 刷新页面
	 */
	public void refresh() {
		 try {
	            // 可能会在子线程中调到这方法
	            this.runOnUiThread(new Runnable() {
	                public void run() {
	                	initList();
	                	mAdapter.notifyDataSetChanged();
	                    //tv_total.setText(String.valueOf(contactList.size())+"位联系人");
	                    
	                }
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();

		List<EMConversation> list = new ArrayList<EMConversation>();
		for (EMConversation conversation : conversations.values()) {

			if (conversation.getAllMessages().size() != 0) {
				// 不在置顶列表里面
				list.add(conversation);
			}
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	////////////////////////////////////////////////////////////////////////////////////
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAgreed(String username) {
			// 好友请求被同意
			Log.i("TAG", "onContactAgreed==>" + username);
			// 提示有新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
			Toast.makeText(getApplicationContext(), username + "同意了你的好友请求", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onContactRefused(String username) {
			// 好友请求被拒绝
			Log.i("TAG", "onContactRefused==>" + username);
		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 收到好友添加请求
			Log.i("TAG", username + "onContactInvited==>" + reason);
			showAgreedDialog(username, reason);
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();
		}

		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 好友被删除时回调此方法
			Log.i("TAG", "usernameListDeleted==>" + usernameList.size());
		}

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 添加了新的好友时回调此方法
			try {
				usernameList=EMContactManager.getInstance().getContactUserNames();
				mAdapter = new FriendListAdapter(MessageActivity.this, usernameList);
				ListView_lxr.setAdapter(mAdapter);
			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void showAgreedDialog(final String user, String reason) {
		new AlertDialog.Builder(MessageActivity.this).setTitle("应用提示")
				.setMessage("用户 " + user + " 想要添加您为好友，是否同意？\n" + "验证信息：" + reason)
				.setPositiveButton("同意", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							EMChatManager.getInstance().acceptInvitation(user);
							refresh();
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i("TAG", "showAgreedDialog1==>" + e.getErrorCode());
						}
					}
				}).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						try {
							EMChatManager.getInstance().refuseInvitation(user);
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i("TAG", "showAgreedDialog2==>" + e.getErrorCode());
						}
					}
				}).setNeutralButton("忽略", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).show();
	}

	private void openAnimationAp_bg() {
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(800);
		bt1.startAnimation(aa);
		bt2.startAnimation(aa);
	}

	private void cloesAnimationAp_bg() {
		AlphaAnimation aa = new AlphaAnimation(1.0f, 0.1f);
		aa.setDuration(150);
		bt1.startAnimation(aa);
		bt2.startAnimation(aa);
	}

	private void openAnimationBt() {
		Animation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(200);
		head_img.startAnimation(scaleAnimation);
		bt1.startAnimation(scaleAnimation);
		bt2.startAnimation(scaleAnimation);
	}

	private void cloesAnimationBt() {
		Animation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.1f);
		scaleAnimation.setDuration(150);
		// head_img.startAnimation(scaleAnimation);
		bt1.startAnimation(scaleAnimation);
		bt2.startAnimation(scaleAnimation);
	}

	// ViewPage适配器
	public class pagerAdapter extends PagerAdapter {
		// View个数
		@Override
		public int getCount() {

			return viewList.size();
		}

		// 当调用这个方法时销毁View
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));

		}

		// View Title
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);// 直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

		// 根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 把当前新增视图的位置（position）作为Key传过去
		@Override
		public int getItemPosition(Object object) {

			return super.getItemPosition(object);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_pager_demo, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(Intent.ACTION_MAIN);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addCategory(Intent.CATEGORY_HOME);
			startActivity(i);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_zy:
			Intent intent = new Intent(MessageActivity.this, Main_PageActiviy.class);
			startActivity(intent);
			break;
		case R.id.open_course:
			Intent intent1 = new Intent(MessageActivity.this, MainCourseActiviy.class);
			startActivity(intent1);
			break;
		case R.id.re_Find:
			Intent intent111 = new Intent(MessageActivity.this, discoverActivity.class);
			startActivity(intent111);
			break;
		case R.id.re_profile:
			Intent intent1111 = new Intent(MessageActivity.this, User.class);
			startActivity(intent1111);
			break;
		}
	}
	//Activity创建或者从被覆盖、后台重新回到前台时被调用  
    @Override  
    protected void onResume() {  
        super.onResume();  
       refresh(); 
    }  
}
