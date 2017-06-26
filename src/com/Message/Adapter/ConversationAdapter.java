package com.Message.Adapter;

import java.util.Date;
import java.util.List;
import com.Common.Constants;
import com.Message.Activity.ChatActivity;
import com.Message.common.SmileUtils;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.example.hello1.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * 消息界面列表适配器
 * 
 * @author tianq
 *
 */
@SuppressLint("InflateParams")
public class ConversationAdapter extends BaseAdapter {
	private List<EMConversation> normal_list;// 会话列表
	private LayoutInflater inflater;// 过滤器
	private Context context;
	private static final Factory spannableFactory = Spannable.Factory.getInstance();

	public ConversationAdapter(Context context, List<EMConversation> normal_list) {
		this.context = context;
		this.normal_list = normal_list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return normal_list.size();
	}

	@Override
	public EMConversation getItem(int position) {
		return normal_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.message_item_news, null);
			holder.iv_avatar = (ImageView) convertView.findViewById(R.id.msg_news_headimg);
			holder.tv_name = (TextView) convertView.findViewById(R.id.msg_news_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.msg_news_news);
			holder.tv_time = (TextView) convertView.findViewById(R.id.msg_news_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取与此用户的会话
		final EMConversation conversation = getItem(position);
		// 获取用户username
		final String username = conversation.getUserName();

		holder.tv_name.setText(username);
		// 单聊数据加载

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			holder.tv_content.setText(SmileUtils.getSmiledText(context, getMessageDigest(lastMessage, context)),
					BufferType.SPANNABLE);
			holder.tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
		}

		RelativeLayout re_parent = (RelativeLayout) convertView.findViewById(R.id.message_re_parent);

		re_parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 进入聊天页面
				Intent intent = new Intent(context, ChatActivity.class);
				// it is single chat
				intent.putExtra(Constants.MEMBER_ID, username);
				context.startActivity(intent);
			}

		});
		return convertView;
	}

	private static class ViewHolder {
		/** 和谁的聊天记录 */
		TextView tv_name;
		/** 消息未读数 */
		TextView tv_unread;
		/** 最后一条消息的内容 */
		TextView tv_content;
		/** 最后一条消息的时间 */
		TextView tv_time;
		/** 用户头像 */
		ImageView iv_avatar;
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";

		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		digest = txtBody.getMessage();
		return digest;
	}

}
