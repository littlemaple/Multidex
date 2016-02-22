package com.medzone.mcloud.defender;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class CloudPush {

    public static final int NS_MAX_DISPLAY_NUMBER = 6;
    public static final String tag = CloudPush.class.getSimpleName();

    // ----------------------------推送类别-------------------------------------------

    public static final String EXTRA_TYPE_PERM_RESPONSE = "perm_response";                    // 请求授权
    public static final String EXTRA_TYPE_PERM_APPLY = "perm_apply";                    // 请求授权
    public static final String EXTRA_TYPE_CONTACT_INVITE = "contact_invite";                // 请求成为联系人

    public static final String EXTRA_TYPE_CONTACT_RESPONSE = "contact_response";                // 对方同意我的好友申请
    public static final String EXTRA_TYPE_CONTACT_DEL = "contact_del";                    // 对方删除我
    public static final String EXTRA_TYPE_CHAT_MESSAGE = "chat_message";                    // 聊天信息
    public static final String EXTRA_TYPE_LOGIN_KICKED = "kicked";                        // 登录踢号
    public static final String EXTRA_TYPE_OPEN_URL = "open_url";
    public static final String EXTRA_TYPE_MEDICINE_PUSH = "medicine_push";
    public static final String EXTRA_TYPE_SERVICE_REPLY = "service_reply";
    public static final String EXTRA_TYPE_SERVICE_MSG = "service_msg";

    // ----------------------------属性字段-------------------------------------------
    public static final String EXTRA_KEY_RESPONSE = "response";
    public static final String EXTRA_KEY_TYPE = "type";
    public static final String EXTRA_KEY_MSG_ID = "msg_id";
    public static final String EXTRA_KEY_SEND_ID = "sendid";
    public static final String EXTRA_KEY_GROUP_ID = "group_id";
    public static final String EXTRA_KEY_GROUP_TITLE = "group_title";
    public static final String EXTRA_KEY_SUBTYPE = "subtype";
    public static final String EXTRA_SERVICE_ID = "serviceid";
    public static final String EXTRA_MSG_TYPE = "msgtype";
    public static final String EXTRA_MSG_ID = "messageid";
    /**
     * 接收者 ID，用于比对是否需要显示
     */
    public static final String EXTRA_KEY_SYNC_ID = "syncid";
    public static final String EXTRA_KEY_OTHER_ID = "otherid";
    private Bundle orginialBundle;                                                // JPUSH返回的Bundle对象
    private long pushID;
    private String pushContent;
    private String pushTitle;
    private JSONObject json;                                                            // JPUSH的附加字段

    /**
     * @param bundle JPUSH返回的参数
     */
    public CloudPush(Bundle bundle) {
        if (bundle == null) return;
        setBundle(bundle);
        if (bundle.containsKey(JPushInterface.EXTRA_MESSAGE)) {
            setPushContent(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        }
        if (bundle.containsKey(JPushInterface.EXTRA_MSG_ID)) {
            setPushID(Long.valueOf(bundle
                    .getString(JPushInterface.EXTRA_MSG_ID)));
        }
        if (bundle.containsKey(JPushInterface.EXTRA_TITLE)) {
            setPushTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        }
        if (bundle.containsKey(JPushInterface.EXTRA_EXTRA)) {
            setPushExtraJSON(bundle.getString(JPushInterface.EXTRA_EXTRA));
        }
    }

    public void parseNotication(Bundle bundle) {
        if (bundle.containsKey(JPushInterface.EXTRA_ALERT)) {
            setPushContent(bundle.getString(JPushInterface.EXTRA_ALERT));
        }
        if (bundle.containsKey(JPushInterface.EXTRA_NOTIFICATION_TITLE)) {
            setPushTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
        }
        if (bundle.containsKey(JPushInterface.EXTRA_EXTRA)) {
            setPushExtraJSON(bundle.getString(JPushInterface.EXTRA_EXTRA));
        }
    }

    public Bundle getBundle() {
        return orginialBundle;
    }

    public void setBundle(Bundle bundle) {
        this.orginialBundle = bundle;
    }

    public long getPushID() {
        return pushID;
    }

    public void setPushID(long pushID) {
        this.pushID = pushID;
    }

    // ==========================
    // 获取Extra字段
    // ==========================

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public JSONObject getPushExtraJSON() {
        return json;
    }

    public void setPushExtraJSON(String extra) {
        try {
            this.json = new JSONObject(extra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPushExtraJSON(JSONObject pushExtraJSON) {
        this.json = pushExtraJSON;
    }

    public String getPushExtraJsonString() {
        if (json != null) {
            return json.toString();
        }
        return null;
    }

    public Integer getExtraOtherId() {
        return (Integer) getValue(EXTRA_KEY_OTHER_ID, Integer.class);
    }

    public String getExtraResponse() {
        return (String) getValue(EXTRA_KEY_RESPONSE, String.class);
    }

    public String getExtraType() {
        return (String) getValue(EXTRA_KEY_TYPE, String.class);
    }

    public Integer getExtraMsgId() {
        return (Integer) getValue(EXTRA_KEY_MSG_ID, Integer.class);
    }

    public Long getExtraSendId() {
        return (Long) getValue(EXTRA_KEY_SEND_ID, Long.class);
    }

    public Long getExtraGroupId() {
        return (Long) getValue(EXTRA_KEY_GROUP_ID, Long.class);
    }

    public String getExtraSubType() {
        return (String) getValue(EXTRA_KEY_SUBTYPE, String.class);
    }

    public String getExtraGroupTitle() {
        return (String) getValue(EXTRA_KEY_GROUP_TITLE, String.class);
    }

    public String getExtraSyncId() {
        return (String) getValue(EXTRA_KEY_SYNC_ID, String.class);
    }

    // ==========================
    // 工具类
    // ==========================
    private Object getValue(String key, Class<?> clazz) {
        if (this.json != null) {
            if (json.has(key) && !json.isNull(key)) {
                try {
                    if (clazz.equals(Integer.class)) {
                        return json.getInt(key);
                    } else if (clazz.equals(Long.class)) {
                        return json.getLong(key);
                    } else if (clazz.equals(String.class)) {
                        return json.getString(key);
                    } else if (clazz.equals(Boolean.class)) {
                        return json.getBoolean(key);
                    } else {
                        return json.get(key);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
