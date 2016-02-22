package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseEntity;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.mcloud.data.bean.IChat;

/**
 * @category 订阅号
 */
@DatabaseTable
public class Subscribe extends BaseEntity implements IChat {

    // local type：闹钟即为心云内置的订阅号
    public static final int TYPE_BUILT_IN_ALARM = 0x1001;
    public static final int TYPE_BUILT_IN_HEALTH = 0x1002;
    public static final int TYPE_BUILT_IN_STORE = 0x1003;
    public static final int TYPE_BUILT_IN_HEALTH_ASSESSMENT = 0x1004;
    public static final int TYPE_BUILT_IN_APPLY_SERVICE = 0x1005;

    //net type
    public static final int TYPE_NORMAL = 0x0000;
    public static final int TYPE_SERVICE = 0x0001;
    public static final int TYPE_COMMUNITY = 0x0002;
    public static final int TYPE_SUBSCRIBE = 0x0003;
    public static final int TYPE_OPENURL = 0x0004;


    // ---------------------------------字段---------------------------------

    public static final String FIELD_PRIMARY_NAME_ID = "id";
    public static final String FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID = "master_account_id";
    public static final String NAME_FIELD_SUBSCRIBE_NAME = "subscribe_name";
    public static final String NAME_FIELD_SUBSCRIBE_DESCRIPTION = "subscribe_description";
    public static final String NAME_FIELD_VERIFY = "verify";
    public static final String NAME_FIELD_HEADPORTRAIT = "headportrait";
    public static final String NAME_FIELD_ISSUBSCRIBED = "issubscribed";
    public static final String NAME_FIELD_TYPE = "type";
    // -------------------------------------------------------------------------

    @DatabaseField(id = true, columnName = FIELD_PRIMARY_NAME_ID)
    private Integer id;

    @DatabaseField(foreign = true, columnName = FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID)
    private Account belongAccount;

    @DatabaseField(columnName = NAME_FIELD_SUBSCRIBE_NAME)
    private String subscribeName;

    @DatabaseField(columnName = NAME_FIELD_SUBSCRIBE_DESCRIPTION)
    private String subscribeDescription;

    @DatabaseField(columnName = NAME_FIELD_VERIFY)
    private String verify;

    @DatabaseField(columnName = NAME_FIELD_HEADPORTRAIT)
    private String headPortrait;

    @DatabaseField(columnName = NAME_FIELD_ISSUBSCRIBED)
    private Boolean isSubscribed;

    @DatabaseField(columnName = NAME_FIELD_TYPE)
    private Integer type;

    private Drawable headPortraitDrawable;

    protected SubscribeSetting subscribeSetting;

    private int memberNum;
    private int memberLimit;
    private boolean isAdmin;

    private IChat parentChat;

    @Override
    public IChat attach(IChat parent) {
        this.parentChat = parent;
        return this;
    }

    @Override
    public IChat getParent() {
        return parentChat;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public int getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public SubscribeSetting getSubscribeSetting() {
        return subscribeSetting;
    }

    public void setSubscribeSetting(SubscribeSetting subscribeSetting) {
        this.subscribeSetting = subscribeSetting;
    }

    /**
     * 检测下订阅号要显示的内容是WEBVIEW
     *
     * @return
     */
    public boolean isContentWebView() {
        if (TextUtils.isEmpty(getVerify())) {
            return false;
        } else {
            if (getVerify().startsWith("http://") || getVerify().startsWith("https://")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getBelongAccount() {
        return belongAccount;
    }

    public void setBelongAccount(Account belongAccount) {
        this.belongAccount = belongAccount;
    }

    public String getSubscribeName() {
        return subscribeName;
    }

    public void setSubscribeName(String subscribeName) {
        this.subscribeName = subscribeName;
    }

    public String getSubscribeDescription() {
        return subscribeDescription;
    }

    public void setSubscribeDescription(String subscribeDescription) {
        this.subscribeDescription = subscribeDescription;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public void setHeadPortraitDrawable(Drawable drawable) {
        this.headPortraitDrawable = drawable;
    }

    public Drawable getHeadPortraitDrawable() {
        return headPortraitDrawable;
    }

    public Boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean isSameRecord(Object o) {

        if (o != null) {
            Subscribe item = (Subscribe) o;
            if (getId().intValue() == item.getId().intValue()) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------parse------------------------

    public static List<Subscribe> createOrUpdateSubscribeList(NetworkClientResult res, Account account) {

        // 解析云端的API,返回云端的联系人列表
        JSONArray ja = null;
        List<Subscribe> list = null;

        if (account == null || res == null) {
            // in-valid
        } else {
            JSONObject jo = res.getResponseResult();
            try {
                ja = jo.getJSONArray("root");
                list = new ArrayList<Subscribe>();
                for (int i = 0; i < ja.length(); i++) {
                    Subscribe sub = createOrUpdateSubscribe((JSONObject) ja.get(i), account, null);
                    if (sub != null) {
                        list.add(sub);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Subscribe createOrUpdateSubscribe(JSONObject jo, Account account, Subscribe sub) {

        final Subscribe tmp;
        if (sub == null) {
            tmp = new Subscribe();
            tmp.setBelongAccount(account);
        } else {
            tmp = sub;
        }
        return parse(jo, tmp);
    }

    private static Subscribe parse(JSONObject jo, Subscribe sub) {
        try {
            if (jo.has("groupid") && !jo.isNull("groupid")) {
                sub.setId(jo.getInt("groupid"));
            }
            if (jo.has("title") && !jo.isNull("title")) {
                sub.setSubscribeName(jo.getString("title"));
            }
            if (jo.has("description") && !jo.isNull("description")) {
                sub.setSubscribeDescription(jo.getString("description"));
            }
            if (jo.has("verify") && !jo.isNull("verify")) {
                sub.setVerify(jo.getString("verify"));
            }
            if (jo.has("membernum") && !jo.isNull("membernum")) {
                sub.setMemberNum(jo.getInt("membernum"));
            }
            if (jo.has("type") && !jo.isNull("type")) {
                sub.setType(Integer.valueOf(jo.getString("type")));
            }
            if (jo.has("memberlimit") && !jo.isNull("memberlimit")) {
                sub.setMemberLimit(jo.getInt("memberlimit"));
            }
            if (jo.has("isadmin") && !jo.isNull("isadmin")) {

                String ret = jo.getString("isadmin");
                if (TextUtils.equals(ret, "Y")) {
                    sub.setAdmin(true);
                } else {
                    sub.setAdmin(false);
                }

            }
            if (jo.has("imageurl") && !jo.isNull("imageurl")) {
                sub.setHeadPortrait(jo.getString("imageurl"));
            }
            if (jo.has("subscribed") && !jo.isNull("subscribed")) {
                String ret = jo.getString("subscribed");
                if (TextUtils.equals(ret, "Y")) {
                    sub.setSubscribed(true);
                } else {
                    sub.setSubscribed(false);
                }
                Log.w("SubscribeController", "解析到我已经关注的机构号：" + sub.getId() + ":" + ret);
            }
            // TODO setting 解析，unread参数
            if (jo.has("setting") && !jo.isNull("setting")) {
                JSONObject jos = (JSONObject) jo.get("setting");
                sub.subscribeSetting = sub.new SubscribeSetting();
                if (jos.has("unread") && !jos.isNull("unread")) {
                    sub.subscribeSetting.settingUnRead = jos.getInt("unread");
                }
                if (jos.has("alert") && !jos.isNull("alert")) {
                    sub.subscribeSetting.settingAlert = jos.getInt("alert");
                }
                if (jos.has("readtime") && !jos.isNull("readtime")) {
                    sub.subscribeSetting.settingReadTime = jos.getLong("readtime");
                }
                if (jos.has("isupload") && !jos.isNull("isupload")) {
                    sub.subscribeSetting.settingUpLoad = jos.getString("isupload");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            sub.invalidate();
        }
        return sub;
    }

    @Override
    public int getIChatSessionType() {
        return MessageSession.TYPE_SUBSCRIBE_CHAT;
    }

    @Override
    public String getIChatDisplayName() {
        return getSubscribeName();
    }

    @Override
    public String getIChatHeadPortrait() {
        return getHeadPortrait();
    }

    @Override
    public String getIChatInterlocutorId() {
        StringBuilder builder = new StringBuilder();
        if (getId() != null) {
            builder.append(getId());
        } else {
            builder.append(INVALID_ID);
        }
        return builder.toString();
    }

    @Override
    public String getIChatInterlocutorIdServer() {
        StringBuilder builder = new StringBuilder();
        if (getId() != null) {
            builder.append(getId());
        } else {
            builder.append(INVALID_ID);
        }
        return builder.toString();
    }

    public class SubscribeSetting {
        public String settingUpLoad = null;
        public Integer settingAlert;
        public Long settingReadTime;
        public Integer settingUnRead;
    }

}
