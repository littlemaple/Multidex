/**
 * 
 */
package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BasePagingContent;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.mcloud.data.bean.IChat;

/**
 * 
 * 
 * 我们使用{@link MessageSession}来记录一次会话的过程与进度。以便于下次进入会话时，能够恢复到上次的会话进度。
 * 也可以再这里扩展出会话的已读未读状态。
 * 
 * @author Robert.
 * @category 会话职责，记录会话的进度及相关信息
 */
public class MessageSession extends BasePagingContent implements Parcelable {

	/**
	 * 联系人的会话
	 */
	public static final int		TYPE_CONTACT_PERSON_CHAT					= 0x1000;
	/**
	 * 联系人对管理员的会话
	 */
	public static final int		TYPE_SUBSCRIBE_CHAT							= 0x1001;
	/**
	 * 管理员对联系人的会话
	 */
	public static final int		TYPE_SUBSCRIBE_ADMIN_CHAT					= 0x1002;

	public static final int		ACTION_HIDE									= STATE_BASE + 1001;

	public static final String	FIELD_FOREIGN_NAME_INTERLOCUTOR_ID			= "interlocutor_id";
	public static final String	FIELD_FOREIGN_NAME_INTERLOCUTOR_ID_SERVER	= "interlocutor_id_server";
	public static final String	NAME_FIELD_SESSION_TITLE					= "sessionTitle";
	public static final String	NAME_FIELD_SESSION_LOGO						= "sessionLogo";
	public static final String	NAME_FIELD_SESSION_TYPE						= "sessionType";
	public static final String	NAME_FIELD_SESSION_CREATE_TIME				= "sessionCreateTime";
	public static final String	NAME_FIELD_SESSION_LAST_MSG_TIME			= "sessionLastMsgTime";
	public static final String	NAME_FIELD_SESSION_LAST_MSG_CONTENT			= "sessionLastMsgContent";
	public static final String	NAME_FIELD_NEW_MSG_COUNT					= "newMsgCount";
	public static final String	NAME_FIELD_HAS_NEW_MSG						= "hasNewMsg";

	// 改成LocalGroupId,LocalPersonId,ServerGroupId,ServerPersonId,不存在用0补足
	@DatabaseField(canBeNull = false, columnName = FIELD_FOREIGN_NAME_INTERLOCUTOR_ID)
	private String				foreignLocalId;														// 对话者Id，本地
	@DatabaseField(canBeNull = false, columnName = FIELD_FOREIGN_NAME_INTERLOCUTOR_ID_SERVER)
	private String				foreignServerId;														// 对话者Id，本地

	@DatabaseField(columnName = NAME_FIELD_SESSION_TITLE)
	private String				sessionTitle;															// 会话标题

	@DatabaseField(columnName = NAME_FIELD_SESSION_LOGO)
	private String				sessionLogo;															// 会话LOGO

	@DatabaseField(columnName = NAME_FIELD_SESSION_TYPE)
	private Integer				sessionType;															// 会话类别

	@DatabaseField(columnName = NAME_FIELD_SESSION_CREATE_TIME)
	private Long				sessionCreateTime;														// 会话创建时间

	@DatabaseField(columnName = NAME_FIELD_SESSION_LAST_MSG_TIME)
	private Long				sessionLastMsgTime;													// 最后一条消息的(发送/接收)时间

	@DatabaseField(columnName = NAME_FIELD_SESSION_LAST_MSG_CONTENT)
	private String				sessionLastMsgContent;													// 最后一条消息的(发送/接收)的内容

	@DatabaseField(columnName = NAME_FIELD_NEW_MSG_COUNT)
	private Integer				newMsgCount;															// 未读的新消息数量,每当有新消息来时+1

	@DatabaseField(columnName = NAME_FIELD_HAS_NEW_MSG)
	private Boolean				newMsgArrived;															// 判断该会话是否拥有新消息

	// public Long sessionLastReadTime; // 会话最后的阅读时间
	// public Message lastMessage; // 已读的最后聊天消息
	// public Message lastestMessage; // 最新的聊天消息(已读/未读)

	private IChat				iChatObject;															// 对话的对象，可能为（联系人、机构号、等）

	public void setForeignLocalId(String foreignId) {
		this.foreignLocalId = foreignId;
	}

	public String getForeignLocalId() {
		return foreignLocalId;
	}

	public void setForeignServerId(String foreignServerId) {
		this.foreignServerId = foreignServerId;
	}

	public String getForeignServerId() {
		return foreignServerId;
	}

	public IChat getiChatObject() {
		return iChatObject;
	}

	public void setiChatObject(IChat iChatObject) {
		this.iChatObject = iChatObject;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSessionLogo() {
		return sessionLogo;
	}

	public void setSessionLogo(String sessionLogo) {
		this.sessionLogo = sessionLogo;
	}

	public Integer getSessionType() {
		return sessionType;
	}

	public void setSessionType(Integer sessionType) {
		this.sessionType = sessionType;
	}

	public Long getSessionCreateTime() {
		return sessionCreateTime;
	}

	public void setSessionCreateTime(Long sessionCreateTime) {
		this.sessionCreateTime = sessionCreateTime;
	}

	public Long getSessionLastMsgTime() {
		return sessionLastMsgTime;
	}

	public void setSessionLastMsgTime(Long sessionLastMsgTime) {
		this.sessionLastMsgTime = sessionLastMsgTime;
	}

	public Boolean getNewMsgArrived() {
		return newMsgArrived;
	}

	public void setNewMsgArrived(Boolean newMsgArrived) {
		this.newMsgArrived = newMsgArrived;
	}

	public Integer getNewMsgCount() {
		return newMsgCount;
	}

	public void setNewMsgCount(Integer newMsgCount) {
		this.newMsgCount = newMsgCount;
	}

	public String getSessionLastMsgContent() {
		return sessionLastMsgContent;
	}

	public void setSessionLastMsgContent(String sessionLastMsgContent) {
		this.sessionLastMsgContent = sessionLastMsgContent;
	}

	public static List<MessageSession> getMessageListByResult(NetworkClientResult res, Account account) {
		JSONObject jo = res.getResponseResult();
		List<MessageSession> list = new ArrayList<MessageSession>();
		try {
			JSONArray ja;
			if (jo != null) {
				if (jo.has("contacts") && !jo.isNull("contacts")) {
					ja = jo.getJSONArray("contacts");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject mJson = ja.getJSONObject(i);
						MessageSession sm = createServiceMessage(mJson);
						sm.setBelongAccount(account);
						list.add(sm);
					}
				}
				if (jo.has("members") && !jo.isNull("members")) {
					ja = jo.getJSONArray("members");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject mJson = ja.getJSONObject(i);
						MessageSession sm = createServiceMessage(mJson);
						sm.setBelongAccount(account);
						list.add(sm);
					}
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static MessageSession createServiceMessage(JSONObject jo) {

		MessageSession am = new MessageSession();
		return parse(jo, am);
	}

	private static MessageSession parse(JSONObject jo, MessageSession am) {

		try {

			if (jo.has("count") && !jo.isNull("count")) {
				am.setNewMsgArrived(true);
				am.setNewMsgCount(jo.getInt("count"));
			}
			if (jo.has("msg") && !jo.isNull("msg")) {
				JSONObject msg = jo.getJSONObject("msg");

				if (msg.has("data") && !msg.isNull("data")) {
					am.setSessionLastMsgContent(msg.getString("data"));
				}
				if (msg.has("chrono") && !msg.isNull("chrono")) {
					am.setSessionLastMsgTime(1000 * msg.getLong("chrono"));
				}
				if (msg.has("sender") && !msg.isNull("sender")) {
					JSONObject sender = msg.getJSONObject("sender");
					if (sender.has("syncid") && !sender.isNull("syncid")) {
						// TODO 因为现在只有联系人才解析此处，如果订阅号会话也从此处进行解析，自需要额外处理
						ContactPerson ichat = new ContactPerson();
						ichat.setContactPersonID(sender.getInt("syncid"));
						ichat.setNickname(sender.getString("nickname"));
						ichat.setRemark(sender.getString("remark"));
						ichat.setHeadPortraits(sender.getString("imagefile"));
						am.setiChatObject(ichat);
					}
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return am;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(foreignLocalId);
		dest.writeString(foreignServerId);
		dest.writeInt(sessionType);
	}

	private MessageSession(Parcel in) {
		this.id = in.readInt();
		this.foreignLocalId = in.readString();
		this.foreignServerId = in.readString();
		this.sessionType = in.readInt();
	}

	public MessageSession() {

	}

	public static final Creator<MessageSession>	CREATOR	= new Creator<MessageSession>() {

															@Override
															public MessageSession[] newArray(int size) {
																return new MessageSession[size];
															}

															@Override
															public MessageSession createFromParcel(Parcel source) {
																return new MessageSession(source);
															}
														};

}
