package com.Message.Adapter;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.Message.common.SmileUtils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.example.hello1.R;

@SuppressLint({ "SdCardPath", "InflateParams" })
public class MessageAdapter extends BaseAdapter {
	
	// reference to conversation object in chatsdk
	private EMConversation conversation;

	private Context context;
	
	List<EMMessage>list_msg;

	public MessageAdapter(Context context, String username) {
		this.context = context;
		LayoutInflater.from(context);
		this.conversation = EMChatManager.getInstance().getConversation(username);
	}
	/**
	 * 获取item数
	 */
	public int getCount() {
		return conversation.getMsgCount();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		notifyDataSetChanged();
	}

	public EMMessage getItem(int position) {
		return conversation.getMessage(position);
	}

	public long getItemId(int position) {
		return position;
	}
	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		//获取此会话的所有消息
		List<EMMessage> messages = conversation.getAllMessages();
		final EMMessage message = getItem(position);
		final ViewHolder holder;
		EMMessage.Direct msg_dirct = message.direct;//消息类型
		// Log.e("message.getUserName()---->>>>.",message.getFrom());
		if (message.getUserName().equals("admin")) {
			convertView = LayoutInflater.from(context).inflate(R.layout.social_chat_admin_item, null);
			TextView timestamp = (TextView) convertView.findViewById(R.id.tv1_time);
			TextView tv_content = (TextView) convertView.findViewById(R.id.tv1_content);
			timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
			// 设置内容
			tv_content.setText(span, BufferType.SPANNABLE);
			return convertView;

		} else {

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.message_chat_listview_item, null);
				holder.rAvatar = (ImageView) convertView
						.findViewById(R.id.message_chat_input);
				holder.rName=(TextView)convertView.findViewById(R.id.message_chat_input_name);
				holder.rContent = (TextView) convertView
						.findViewById(R.id.message_chat_input_content);
				holder.chatTime = (TextView) convertView
						.findViewById(R.id.listview_item_time);
				holder.sContent = (TextView) convertView
						.findViewById(R.id.message_chat_send_content);
				holder.sName=(TextView)convertView.findViewById(R.id.message_chat_send_name);
				holder.sAvatar = (ImageView) convertView
						.findViewById(R.id.message_chat_send_avatar);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//设置消息内容
			handleTextMessage(message, holder, position);
			//根据消息的类型显示控件
			if (msg_dirct==message.direct.RECEIVE) {
				holder.rAvatar.setVisibility(View.VISIBLE);
				holder.rName.setVisibility(View.VISIBLE);
				holder.rContent.setVisibility(View.VISIBLE);
				holder.sContent.setVisibility(View.GONE);
				holder.sAvatar.setVisibility(View.GONE);
				holder.sName.setVisibility(View.GONE);
			} else if (msg_dirct==message.direct.SEND) {
				holder.rAvatar.setVisibility(View.GONE);
				holder.rName.setVisibility(View.GONE);
				holder.rContent.setVisibility(View.GONE);
				holder.sContent.setVisibility(View.VISIBLE);
				holder.sAvatar.setVisibility(View.VISIBLE);
				holder.sName.setVisibility(View.VISIBLE);
			}
			
			if (position == 0) {
				holder.chatTime.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
				holder.chatTime.setVisibility(View.VISIBLE);
			} else {
				// 两条消息时间离得如果稍长，显示时间
				if (DateUtils.isCloseEnough(message.getMsgTime(), conversation.getMessage(position - 1).getMsgTime())) {
					holder.chatTime.setVisibility(View.GONE);
				} else {
					holder.chatTime.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
					holder.chatTime.setVisibility(View.VISIBLE);
				}
			}
			return convertView;
		}
	}

	/**
	 * 文本消息
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
		// 设置内容
		holder.sContent.setText(span, BufferType.SPANNABLE);
		holder.rContent.setText(span, BufferType.SPANNABLE);
	}

	public static class ViewHolder {
		ImageView rAvatar;
		TextView rName;
		TextView rContent;
		TextView chatTime;
		TextView sContent;
		TextView sName;
		ImageView sAvatar;
	}
}