/**
 * 
 */
package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BasePagingContent;
import com.medzone.framework.network.NetworkClientResult;

public class Message extends BasePagingContent {

	public static final int		SYSTEM_SENDER_ID				= 0;					// 系统管理员发出的消息

	public static final String	NOTIFY_JOIN						= "join";				// 加入群
	public static final String	NOTIFY_QUIT						= "quit";				// 退群
	public static final String	NOTIFY_KICKED					= "kicked";			// 被踢

	// private static final String KEY_MESSAGE_CONTENT = "content_key";

	// ---------------------------------消息类型------------------------------------------

	public static final int		TYPE_NORMAL						= 0;					// 普通文本消息
	public static final int		TYPE_LINK						= 1;					// 外部URL链接，支持图文
	public static final int		TYPE_RECORD						= 2;					// 测量数据分享
	public static final int		TYPE_NOTIFY						= 3;					// 用户加群、退群的通知（只针对普通群）
	public static final int		TYPE_IMAGE						= 4;					// 只有一张图片，data为图片网址
	public static final int		TYPE_VOICE						= 5;					// 一段语音，data为语音内容，base64编码，AMR格式

	// -----------------------------------Link消息子集类型----------------------------------------

	public static final int		LINK_TYPE_RECENT				= 0;
	public static final int		LINK_TYPE_MONTH					= 1;
	public static final int		LINK_TYPE_HEALTH_CENTRE			= 2;
	public static final int		LINK_TYPE_MONTH_TABLE			= 3;
	public static final int     LINK_TYPE_LIST                  = 4;

	// -----------------------------------库表字段----------------------------------------

	public static final String	FIELD_FOREIGN_NAME_SESSION_ID	= "session_id";

	public static final String	NAME_FIELD_MESSAGE_ID			= "messageID";
	public static final String	NAME_FIELD_MESSAGE_TYPE			= "messageType";
	public static final String	NAME_FIELD_MESSAGE_CHAT_CONTENT	= "messageChatContent";
	public static final String	NAME_FIELD_POST_TIME			= "postTime";
	public static final String	NAME_FIELD_IS_READ				= "isRead";

	// --------------------------------ForeignKey--------------------------------------

	@DatabaseField(foreign = true, canBeNull = false, columnName = FIELD_FOREIGN_NAME_SESSION_ID)
	protected MessageSession	session;												// 消息所依附的会话

	// --------------------------------Properties--------------------------------------

	@DatabaseField(columnName = NAME_FIELD_MESSAGE_ID)
	protected Long				messageID;												// 云端的消息唯一标示符

	@DatabaseField(columnName = NAME_FIELD_MESSAGE_TYPE)
	protected Integer			messageType;											// 消息类型

	@DatabaseField(columnName = NAME_FIELD_IS_READ)
	protected Boolean			isRead;												// 消息是否已读

	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_MESSAGE_CHAT_CONTENT)
	protected byte[]			data;													// 消息详情

	@DatabaseField(columnName = NAME_FIELD_POST_TIME)
	protected Long				postTime;												// 消息发出的时间

	// --------------------------------Sender--------------------------------------
	@DatabaseField
	protected String			senderNickname;
	@DatabaseField
	protected String			senderRemark;
	@DatabaseField
	protected String			senderUsername;
	@DatabaseField
	protected Integer			senderID;
	@DatabaseField
	protected String			senderIconUrl;

	// ---------------------------------Data------------------------------------------

	protected ChatNormal		chatNormal;

	protected ChatLink			chatLink;

	protected ChatRecord		chatRecord;

	protected ChatNotify		chatNotify;

	// ---------------------------------Setter/Getter------------------------------------------

	/**
	 * 判断本消息在本地会话中,是接收方还是发送方
	 * */
	public boolean isSenderInSession() {
		if (getSenderID() == null) return false;
		if (getBelongAccount() == null) {
			return false;
		}
		return getSenderID() == getBelongAccount().getId();
	}

	public void setSession(MessageSession session) {
		this.session = session;
	}

	public MessageSession getSession() {
		return session;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Boolean isRead() {
		return isRead;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public Integer getSenderID() {
		return senderID;
	}

	public String getSenderIconUrl() {
		return senderIconUrl;
	}

	public void setSenderIconUrl(String iconUrl) {
		this.senderIconUrl = iconUrl;
	}

	public Long getPostTime() {
		return postTime;
	}

	public void setPostTime(Long postTime) {
		this.postTime = postTime;
	}

	public String getSenderNickname() {
		return senderNickname;
	}

	private void setSenderNickname(String nickname) {
		this.senderNickname = nickname;
	}

	public String getSenderRemark() {
		return senderRemark;
	}

	public void setSenderRemark(String remark) {
		this.senderRemark = remark;
	}

	public String getSenderRealName() {
		return senderUsername;
	}

	public void setSenderRealName(String realName) {
		this.senderUsername = realName;
	}

	/**
	 * @return the messageID
	 */
	public Long getMessageID() {
		return messageID;
	}

	/**
	 * @param messageID
	 *            the messageID to set
	 */
	public void setMessageID(Long messageID) {
		this.messageID = messageID;
	}

	// ------------------------------------获取聊天内容---------------------------

	private void setChatNormal(ChatNormal chatNormal) {
		this.chatNormal = chatNormal;
	}

	private ChatNormal getChatNormal() {
		return chatNormal;
	}

	private void setChatLink(ChatLink chatLink) {
		this.chatLink = chatLink;
	}

	private ChatLink getChatLink() {
		return chatLink;
	}

	private void setChatRecord(ChatRecord chatRecord) {
		this.chatRecord = chatRecord;
	}

	private ChatRecord getChatRecord() {
		return chatRecord;
	}

	private void setChatNotify(ChatNotify chatNotify) {
		this.chatNotify = chatNotify;
	}

	private ChatNotify getChatNotify() {
		return chatNotify;
	}

	// ---------------------------------------APIParse-----------------------------------------

	public static List<Message> createMessageList(NetworkClientResult res, Account belongAccount, MessageSession session) {
		JSONArray ja = null;
		List<Message> list = null;
		JSONObject jo = res.getResponseResult();
		try {
			ja = jo.getJSONArray("root");
			list = new ArrayList<Message>();
			for (int i = 0; i < ja.length(); i++) {
				Message msg = createMessage((JSONObject) ja.get(i), belongAccount, session);
				if (msg != null) list.add(msg);
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return list;
	}

	public static Message createMessage(JSONObject jo, Account belongAccount, MessageSession session) {

		Message msg = new Message();
		return parse(jo, msg, belongAccount, session);
	}

	public static Message updateMessage(JSONObject jo, Message msg, Account belongAccount, MessageSession session) {
		return parse(jo, msg, belongAccount, session);
	}

	private static Message parse(JSONObject jo, Message msg, Account belongAccount, MessageSession session) {
		msg.setBelongAccount(belongAccount);
		msg.setSession(session);
		try {
			if (jo.has("sender") && !jo.isNull("sender")) {
				JSONObject o = jo.getJSONObject("sender");
				if (o.has("imagefile") && !o.isNull("imagefile")) {
					msg.setSenderIconUrl(o.getString("imagefile"));
				}
				if (o.has("nickname") && !o.isNull("nickname")) {
					msg.setSenderNickname(o.getString("nickname"));
				}
				if (o.has("username") && !o.isNull("username")) {
					msg.setSenderRealName(o.getString("username"));
				}
				if (o.has("remark") && !o.isNull("remark")) {
					msg.setSenderRemark(o.getString("remark"));
				}
				if (o.has("syncid") && !o.isNull("syncid")) {
					msg.setSenderID(o.getInt("syncid"));
				}
			}

			if (jo.has("chrono") && !jo.isNull("chrono")) {
				long milliseconds = jo.getLong("chrono") * 1000L;
				msg.setPostTime(milliseconds);
			}
			if (jo.has("messageid") && !jo.isNull("messageid")) {
				msg.setMessageID(jo.getLong("messageid"));
			}
			if (jo.has("type") && !jo.isNull("type")) {
				msg.setMessageType(jo.getInt("type"));
			}
			if (jo.has("data") && !jo.isNull("data")) {

				String data;
				switch (msg.getMessageType()) {
				case TYPE_NORMAL:
					data = jo.getString("data");
					break;
				default:
					data = jo.getJSONObject("data").toString();
					break;
				}

				msg.setData(data.getBytes());
			}
			return msg;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// ------------------------------------------解析消息主体----------------------------------------

	public ChatNormal parseChatNormal() {

		if (getChatNormal() != null) {
			return getChatNormal();
		}
		ChatNormal normal = new ChatNormal();
		normal.message = new String(getData());
		setChatNormal(normal);
		return normal;
	}

	public ChatLink parseChatLink() {

		if (getChatLink() != null) {
			return getChatLink();
		}
		try {
			JSONObject o = new JSONObject(new String(getData()));
			ChatLink link = new ChatLink();
			if (o.has("title") && !o.isNull("title")) {
				link.linkTitle = o.getString("title");
			}
			if (o.has("url") && !o.isNull("url")) {
				link.linkURL = o.getString("url");
			}
			if (o.has("description") && !o.isNull("description")) {
				link.linkDescription = o.getString("description");
			}
			if (o.has("image") && !o.isNull("image")) {
				link.linkImage = o.getString("image");
			}
			if (o.has("report_type") && !o.isNull("report_type")) {
				link.linkType = o.getInt("report_type");
			}
			if (o.has("link_params") && !o.isNull("link_params")) {
				link.linkParams = o.getString("link_params");
			}
			if (o.has("time") && !o.isNull("time")) {
				link.linkTime = o.getLong("time");
			}
			setChatLink(link);
			return link;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ChatRecord parseChatRecord() {

		if (getChatRecord() != null) {
			return getChatRecord();
		}
		try {
			JSONObject o = new JSONObject(new String(getData()));
			ChatRecord record = new ChatRecord();
			if (o.has("type") && !o.isNull("type")) {
				record.recordType = o.getString("type");
			}
			if (o.has("value1") && !o.isNull("value1")) {
				record.recordValue1 = o.getString("value1");
			}
			if (o.has("value2") && !o.isNull("value2")) {
				record.recordValue2 = o.getString("value2");
			}
			if (o.has("value3") && !o.isNull("value3")) {
				record.recordValue3 = o.getString("value3");
			}
			if (o.has("time") && !o.isNull("time")) {
				long milliseconds = o.getLong("time") * 1000L;
				record.recordTime = milliseconds;
			}
			if (o.has("value1_avg") && !o.isNull("value1_avg")) {
				int avg = o.getInt("value1_avg");
				record.valueAvg = avg;
			}
			if (o.has("value_duration") && !o.isNull("value_duration")) {
				long duration = o.getLong("value_duration");
				record.valueDuration = duration;
			}
			if (o.has("result") && !o.isNull("result")) {
				record.recordResult = o.getString("result");
			}
			if (o.has("state") && !o.isNull("state")) {
				record.recordState = o.getInt("state");
			}
			if (o.has("url") && !o.isNull("url")) {
				record.recordURL = o.getString("url");
			}
			if (o.has("unit") && !o.isNull("unit")) {
				record.recordUnit = (char) o.getInt("unit");
			}
			if (o.has("value_period") && !o.isNull("value_period")) {
				record.recordMeasureState = o.getInt("value_period");
			}/*
			 * else {
			 * record.recordUnit = '0';// default is 0.
			 * System.out.println("==============message===record" +
			 * record.recordUnit);
			 * }
			 */
			setChatRecord(record);
			return record;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ChatNotify parseChatNotify() {

		if (getChatNotify() != null) {
			return getChatNotify();
		}
		try {
			JSONObject o = new JSONObject(new String(getData()));
			ChatNotify notify = new ChatNotify();
			if (o.has(ChatNotify.NOTIFIED_TYPE) && !o.isNull(ChatNotify.NOTIFIED_TYPE)) {
				notify.notifiedType = o.getString(ChatNotify.NOTIFIED_TYPE);
			}
			if (o.has(ChatNotify.NOTIFIED_NAME) && !o.isNull(ChatNotify.NOTIFIED_NAME)) {
				notify.notifiedName = o.getString(ChatNotify.NOTIFIED_NAME);
			}
			if (o.has(ChatNotify.NOTIFIED_ACCOUNT_ID) && !o.isNull(ChatNotify.NOTIFIED_ACCOUNT_ID)) {
				notify.notifiedAccountID = o.getInt(ChatNotify.NOTIFIED_ACCOUNT_ID);
			}
			setChatNotify(notify);
			return notify;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析消息主体
	 */
	public Object getChatContent() {

		switch (getMessageType()) {
		case TYPE_NORMAL:
			return parseChatNormal();
		case TYPE_LINK:
			return parseChatLink();
		case TYPE_RECORD:
			return parseChatRecord();
		case TYPE_NOTIFY:
			return parseChatNotify();
		default:
			return null;
		}
	}

	public class ChatNormal {
		public String	message;
	}

	public class ChatLink {

		public String	linkTitle;

		public String	linkURL;

		public String	linkDescription;

		public String	linkImage;

		public Long		linkTime;

		public String	linkParams;		// JSONObject

		public Integer	linkType;

	}

	public class ChatRecord {

		public String	recordType;

		public String	recordValue1;

		public String	recordValue2;

		public String	recordValue3;

		public String	recordResult;

		public Long		recordTime;

		public Integer	valueAvg;

		public Long		valueDuration;

		public Integer	recordState;

		public String	recordURL;

		public char		recordUnit;

		public Integer	recordMeasureState;

	}

	public class ChatNotify {
		public static final String NOTIFIED_TYPE = "type";
		public static final String NOTIFIED_NAME = "nickname";
		public static final String NOTIFIED_ACCOUNT_ID = "syncid";

		public String	notifiedType;

		public String	notifiedName;

		public Integer	notifiedAccountID;
	}
}
