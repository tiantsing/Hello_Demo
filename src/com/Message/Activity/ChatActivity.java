package com.Message.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Common.Constants;
import com.Message.Adapter.MessageAdapter;
import com.Message.bean.ChatListData;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.MessageBody;
import com.easemob.chat.TextMessageBody;
import com.example.hello1.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 聊天类
 * 
 * @author tianq 完成时间：2016-09-05
 */
public class ChatActivity extends Activity implements OnClickListener {
	private EditText contentET;// 信息输入框
	private TextView topNameTV;// 顶部标题栏 用于显示所对话人员的昵称
	private Button sendBtn;// 发送按钮
	private ImageView back;// 返回按钮
	private NewMessageBroadcastReceiver msgReceiver;// 新消息

	private ListView mListView;// 会话列表
	private List<String> histroyNews = new ArrayList<String>();// 历史消息列表
	// private ChatListAdapter mAdapter;// 会话列表适配器
	private InputMethodManager imm;// 输入方法控制

	private String receiveName = null;// 接受者名字
	protected Handler mHandler;//
	Boolean IsSendMessage = false;// 布尔型常量，判断是否发送了信息 true-》发送了 false-》没有新消息
	List<EMMessage> messages;// 历史会话记录
	MessageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.message_chat);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0x00001:
					imm.hideSoftInputFromWindow(contentET.getApplicationWindowToken(), 0); // 隐藏键盘
					adapter.notifyDataSetChanged(); // 刷新聊天列表
					contentET.setText(""); // 清空发送内容
					break;
				default:
					break;
				}
			}

		};
		// 获取接受信息的用户
		receiveName = this.getIntent().getStringExtra(Constants.MEMBER_ID);
		// View的初始化
		initView();
		// 只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		imm = (InputMethodManager) contentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

		adapter = new MessageAdapter(this, receiveName);
		// 显示消息
		mListView.setAdapter(adapter);
		// 初始化监听
		initListener();
		// 初始化活动
		initEvent();
	}

	// View初始化
	private void initView() {
		contentET = (EditText) findViewById(R.id.chat_content);
		topNameTV = (TextView) findViewById(R.id.txt_title);
		sendBtn = (Button) findViewById(R.id.chat_send_btn);
		mListView = (ListView) findViewById(R.id.chat_listview);
		back = (ImageView) findViewById(R.id.img_back);
		// 设置标题
		topNameTV.setText(receiveName);
		// 返回按钮设置可见
		back.setVisibility(View.VISIBLE);
	}

	// Listener初始化
	private void initListener() {
		back.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
	}

	private void initEvent() {
		EMConversation conversation = EMChatManager.getInstance().getConversation(receiveName);
		// 获取此会话的所有消息
		messages = conversation.getAllMessages();
		// 把此会话的未读数置为0
		conversation.resetUnreadMsgCount();
		adapter = new MessageAdapter(this, receiveName);
		// 显示消息
		mListView.setAdapter(adapter);
		// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
		// 获取startMsgId之前的pagesize条消息，此方法获取的messages
		// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
		// List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId,
		// pagesize);
		contentET.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keycode, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (keycode == KeyEvent.KEYCODE_ENTER && arg2.getAction() == KeyEvent.ACTION_DOWN) {
					sendMsg();
					return true;
				}
				return false;
			}
		});
	}

	void sendMessageHX(String username, final String content) {
		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		EMConversation conversation = EMChatManager.getInstance().getConversation(username);

		// 创建一条文本消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// // 如果是群聊，设置chattype,默认是单聊
		// message.setChatType(ChatType.GroupChat);
		// 设置消息body
		TextMessageBody txtBody = new TextMessageBody(content);
		message.addBody(txtBody);
		// 设置接收人
		message.setReceipt(username);

		// 把消息加入到此会话对象中
		conversation.addMessage(message);
		// 发送消息
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("TAG", "消息发送失败");
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("TAG", "正在发送消息");
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Log.i("TAG", "消息发送成功");
				mHandler.sendEmptyMessage(0x00001);
				IsSendMessage = true;
			}
		});
	}

	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 注销广播
			abortBroadcast();

			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
			String msgId = intent.getStringExtra("msgid");
			// 发送方
			String username = intent.getStringExtra("from");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			EMConversation conversation = EMChatManager.getInstance().getConversation(username);

			MessageBody tmBody = message.getBody();

			mHandler.sendEmptyMessage(0x00001);
			IsSendMessage = true;
			Log.i("TAG", "收到消息：" + ((TextMessageBody) tmBody).getMessage());
			// 如果是群聊消息，获取到group id

			if (!username.equals(username)) {
				// 消息不是发给当前会话，return
				return;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(msgReceiver);

	}

	private void sendMsg() {
		String content = contentET.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(getApplicationContext(), "请输入发送的内容", Toast.LENGTH_SHORT).show();
		} else {
			sendMessageHX(receiveName, content);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Intent imgBacktoMessageMain = new Intent(ChatActivity.this, MessageActivity.class);

			startActivity(imgBacktoMessageMain);

			break;
		case R.id.chat_send_btn:
			sendMsg();
		default:
			break;
		}

	}

}
