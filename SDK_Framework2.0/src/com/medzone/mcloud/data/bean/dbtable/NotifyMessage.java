package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.mcloud_framework.R.string;

/**
 * 
 * @author Robert.
 * @category 系统消息：[好友申请/其他]
 * 
 */
public class NotifyMessage extends BaseIdDatabaseContent implements Cloneable {

	public static final int		TYPE_BUTTON_ENABLE			= 0;
	public static final int		TYPE_BUTTON_DISABLE			= 1;

	public static final int		TYPE_NORMAL					= 0;					// 普通文本消息
	public static final int		TYPE_INVITE_GROUP			= 1;					// 邀请入群
	public static final int		TYPE_ACCEPT_GROUP			= 2;					// 同意入群
	public static final int		TYPE_REFUSE_GROUP			= 3;					// 拒绝入群
	public static final int		TYPE_KICK_GROUP				= 4;					// 被踢出群
	public static final int		TYPE_QUIT_GROUP				= 5;					// 用户退群
	public static final int		TYPE_DISMISS_GROUP			= 6;					// 解散群
	public static final int		TYPE_CONTACT_APPLY			= 101;					// 联系人加群
	public static final int		TYPE_ACCEPT_CONTACT			= 102;					// 同意成为联系人
	public static final int		TYPE_REFUSE_CONTACT			= 103;					// 拒绝成为联系人
	public static final int		TYPE_PERM_APPLY				= 110;					// 请求授权
	public static final int		TYPE_ACCEPT_APPLY			= 111;					// 同意授权
	public static final int		TYPE_REFUSE_APPLY			= 112;					// 拒绝授权

	public static final String	PERM_TYPE_EDIT				= "allowedit";
	public static final String	PERM_TYPE_VIEW				= "allowview";
	public static final String	PERM_TYPE_TEST				= "allowtest";

	// ---------------------------------------------库表字段--------------------------------------

	public static final String	NAME_FIELD_MESSAGE_ID		= "messageId";
	public static final String	NAME_FIELD_MESSAGE_TYPE		= "messageType";
	public static final String	NAME_FIELD_MESSAGE_TITLE	= "messageTitle";
	public static final String	NAME_FIELD_IS_READ			= "isRead";
	public static final String	NAME_FIELD_MESSAGE_RESPONSE	= "messageResponse";
	public static final String	NAME_FIELD_MESSAGE_CONTENT	= "messageContent";
	public static final String	NAME_FIELD_MESSAGE_SENDER	= "messageSender";

	@DatabaseField(columnName = NAME_FIELD_MESSAGE_ID)
	private int					messageId;											// 系统消息ID，由云端返回得到

	@DatabaseField(columnName = NAME_FIELD_MESSAGE_TYPE)
	private Integer				messageType;										// 系统消息类型

	@DatabaseField(columnName = NAME_FIELD_MESSAGE_TITLE)
	private String				messageTitle;										// 系统消息标题

	@DatabaseField(defaultValue = "0", columnName = NAME_FIELD_IS_READ)
	private Boolean				isRead;											// 系统消息是否已经查阅

	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_MESSAGE_CONTENT)
	private byte[]				messageContent;									// 系统消息主体(JSON)

	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_MESSAGE_RESPONSE)
	private byte[]				messageResponse;									// 系统消息的处理结果(JSON)

	@DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_MESSAGE_SENDER)
	private byte[]				sender;											// 发送者信息主体(JSON)

	// ----------------------------------------------其他属性-------------------------------------------

	private Boolean				isAcceptInvite;

	private Integer				groupId;
	private String				groupName;
	private Integer				groupType;

	private String				permType;
	private String				permCode;

	private String				senderImagefile;
	private String				senderName;
	private Integer				senderId;
	private String				senderNickname;

	public byte[] getSender() {
		return sender;
	}

	public void setSender(byte[] sender) {
		this.sender = sender;
	}

	public void setMessageResponse(byte[] messageResponse) {
		this.messageResponse = messageResponse;
	}

	public byte[] getMessageResponse() {
		return messageResponse;
	}

	public void setMessageContent(byte[] messageContent) {
		this.messageContent = messageContent;
	}

	public byte[] getMessageContent() {
		return messageContent;
	}

	public String getSenderImagefile() {
		return senderImagefile;
	}

	public void setSenderImagefile(String senderImagefile) {
		this.senderImagefile = senderImagefile;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public String getSenderNickname() {
		return senderNickname;
	}

	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public Boolean isAcceptInvite() {
		return isAcceptInvite;
	}

	public void setAcceptInvite(Boolean isAcceptInvite) {
		this.isAcceptInvite = isAcceptInvite;
	}

	public Boolean isRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermType(String permType) {
		this.permType = permType;
	}

	public String getPermType() {
		return permType;
	}

	public int getPermTypeDescription() {
		if (TextUtils.equals(permType, PERM_TYPE_VIEW)) {
			return /*"查看数据权限"*/string.data_permissions;
		}
		else if (TextUtils.equals(permType, PERM_TYPE_EDIT)) {
			return /*"修改资料权限"*/string.modify_permissions;
		}
		else if (TextUtils.equals(permType, PERM_TYPE_TEST)) {
			return /*"代测权限"*/string.test_permissions;
		}
		else {
			return /*"<未知错误>"*/string.unknown_error;
		}

	}

	public static List<NotifyMessage> getMessageListByResult(NetworkClientResult res, Account account) {
		JSONObject jo = res.getResponseResult();
		List<NotifyMessage> list = new ArrayList<NotifyMessage>();
		try {
			if (jo != null) {
				JSONArray ja = jo.getJSONArray("root");
				for (int i = 0; i < ja.length(); i++) {
					JSONObject mJson = ja.getJSONObject(i);
					NotifyMessage sm = createServiceMessage(mJson);
					sm.setBelongAccount(account);
					list.add(sm);
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static NotifyMessage createServiceMessage(JSONObject jo) {

		NotifyMessage am = new NotifyMessage();
		return parse(jo, am);
	}

	private static NotifyMessage parse(JSONObject jo, NotifyMessage am) {

		try {
			if (jo.has("messageid") && !jo.isNull("messageid")) {
				am.setMessageId(jo.getInt("messageid"));
			}
			if (jo.has("type") && !jo.isNull("type")) {
				am.setMessageType(jo.getInt("type"));
			}
			if (jo.has("sender") && !jo.isNull("sender")) {
				JSONObject mo = jo.getJSONObject("sender");

				JSONObject newJson = new JSONObject();
				newJson.put("sender", mo);
				am.setSender(newJson.toString().getBytes());

				if (mo.has("imagefile") && !mo.isNull("imagefile")) {
					am.setSenderImagefile(mo.getString("imagefile"));
				}
				if (mo.has("username") && !mo.isNull("username")) {
					am.setSenderName(mo.getString("username"));
				}
				if (mo.has("nickname") && !mo.isNull("nickname")) {
					am.setSenderNickname(mo.getString("nickname"));
				}
				if (mo.has("syncid") && !mo.isNull("syncid")) {
					am.setSenderId(mo.getInt("syncid"));
				}
			}
			if (jo.has("isread") && !jo.isNull("isread")) {
				String temp = jo.getString("isread");
				if (temp != null) {
					if (temp.equals("Y")) {
						am.setIsRead(true);
					}
					else {
						am.setIsRead(false);
					}
				}
			}
			if (jo.has("response") && !jo.isNull("response")) {

				String temp = jo.getString("response");
				if (temp.equals("Y")) am.setAcceptInvite(true);
				if (temp.equals("N")) am.setAcceptInvite(false);

				JSONObject newJson = new JSONObject();
				newJson.put("response", temp);
				am.setMessageResponse(newJson.toString().getBytes());

			}

			if (jo.has("data") && !jo.isNull("data")) {
				JSONObject mo = jo.getJSONObject("data");
				if (mo.has("group_title") && !mo.isNull("group_title")) {
					am.setGroupName(mo.getString("group_title"));
				}
				if (mo.has("group_id") && !mo.isNull("group_id")) {
					int groupid = Integer.valueOf(mo.getString("group_id"));
					am.setGroupId(groupid);
				}
				// 为了兼容v2.0的代码增加的解析方式
				if (mo.has("groupid") && !mo.isNull("groupid")) {
					int groupid = Integer.valueOf(mo.getString("groupid"));
					am.setGroupId(groupid);
				}
				if (mo.has("group_type") && !mo.isNull("group_type")) {
					am.setGroupType(mo.getInt("group_type"));
				}
				if (mo.has("perm") && !mo.isNull("perm")) {
					am.setPermType(mo.getString("perm"));
				}
				if (mo.has("code") && !mo.isNull("code")) {
					am.setPermCode(mo.getString("code"));
				}

				JSONObject newJson = new JSONObject();
				newJson.put("data", mo);
				am.setMessageResponse(newJson.toString().getBytes());

			}
			am.setStateFlag(STATE_SYNCHRONIZED);
			am.setActionFlag(ACTION_NORMAL);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return am;
	}
}
