package com.Message.bean;
/**
 * 会话界面用的数据
 * @author tianq
 *
 */
import java.util.Date;

public class ChatListData {

	String receiveAvatar;//右边来信头像
	String receiveName;//右边来信姓名
	String receiveContent;//右边来信消息内容
	Date chatTime;//时间
	String sendContent;//左边发信消息内容
	String sendName;//左边发信姓名
	String sendAvatar;//左边发信头像
	/**
	 * 1 发送； 2接收
	 */
	int type;
	/**
	 * 1 发送； 2接收
	 */
	public int getType() {
		return type;
	}
	/**
	 * 1 发送； 2接收
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getReceiveAvatar() {
		return receiveAvatar;
	}

	public void setReceiveAvatar(String receiveAvatar) {
		this.receiveAvatar = receiveAvatar;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getReceiveContent() {
		return receiveContent;
	}

	public void setReceiveContent(String receiveContent) {
		this.receiveContent = receiveContent;
	}

	public Date getChatTime() {
		return chatTime;
	}

	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}
	public String getSendContent() {
		return sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getSendAvatar() {
		return sendAvatar;
	}

	public void setSendAvatar(String sendAvatar) {
		this.sendAvatar = sendAvatar;
	}
     
	

}
