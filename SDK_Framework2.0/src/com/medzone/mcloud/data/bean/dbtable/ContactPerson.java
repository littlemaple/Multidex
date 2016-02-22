package com.medzone.mcloud.data.bean.dbtable;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.framework.util.JSONUtils;
import com.medzone.framework.Config;
import com.medzone.framework.util.MapUtils;
import com.medzone.mcloud.data.bean.IChat;
import com.medzone.framework.data.IRuleRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DatabaseTable
public class ContactPerson extends BaseIdDatabaseContent implements Cloneable, IChat,IRuleRes, Parcelable, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5986977603158155921L;

    public static final String TAG = ContactPerson.class.getSimpleName();

    public static final String DEFAULT_OFFLINE_AVATAR_URL = "http://www.mcloudlife.com/img/web/logo.jpg";

    public static final int ACTION_APPLY = ACTION_NORMAL + 500;

    // 查看权限:0=否/1=近期/2=全部
    public static final int PERM_VIEW_TYPE_NONE = 0;
    public static final int PERM_VIEW_TYPE_RECENT = 1;
    public static final int PERM_VIEW_TYPE_ALL = 2;

    // 推送权限:0=否/1=短信/2=心云/3=邮件
    public static final int PERM_PUSH_TYPE_NONE = 0;
    public static final int PERM_PUSH_TYPE_MESSAGE = 1;
    public static final int PERM_PUSH_TYPE_MCLOUD = 2;
    public static final int PERM_PUSH_TYPE_EMAIL = 3;

    public static final String NAME_FIELD_CONTACT_PERSON_ID = "contactPersonID";
    public static final String NAME_FIELD_IS_ACTIVE = "isActive";
    public static final String NAME_FIELD_AGE = "age";
    public static final String NAME_FIELD_WEIGHT = "weight";
    public static final String NAME_FIELD_BIRTHDAY = "birthday";
    public static final String NAME_FIELD_ATHLETETYPE = "athleteType";
    public static final String NAME_FIELD_HEIGHT = "height";
    public static final String NAME_FIELD_TALL = "tall";
    public static final String NAME_FIELD_USERNAME = "username";
    public static final String NAME_FIELD_NICKNAME = "nickname";
    public static final String NAME_FIELD_LOCATION = "location";
    public static final String NAME_FIELD_IS_MALE = "ismale";
    public static final String NAME_FIELD_HEAD_PORTRAITS = "headPortraits";
    public static final String NAME_FIELD_PHONE = "phone";
    public static final String NAME_FIELD_EMAIL = "email";
    public static final String NAME_FIELD_IDCARD = "iDCard";

    public static final String NAME_FIELD_IS_CARE = "isCare";
    public static final String NAME_FIELD_REMARK = "remark";
    public static final String NAME_FIELD_LABELS = "labels";
    public static final String NAME_FIELD_ALLOWGAUGE = "allowgauge";
    public static final String NAME_FIELD_ALLOWEDIT = "allowedit";
    public static final String NAME_FIELD_ALLOWCHAT = "allowchat";
    public static final String NAME_FIELD_ALLOWPUSH = "allowpush";
    public static final String NAME_FIELD_ALLOWVIEWTYPE = "allowViewType";
    public static final String NAME_FIELD_ALLOWPUSHTYPE = "subsType";
    public static final String NAME_FIELD_RULE_STATE = "ruleState";

    public static final String NAME_FIELD_ALLOWGAUGE_FM = "allowgaugeFM";
    public static final String NAME_FIELD_ALLOWEDIT_FM = "alloweditFM";
    public static final String NAME_FIELD_ALLOWCHAT_FM = "allowchatFM";
    public static final String NAME_FIELD_ALLOWPUSH_FM = "allowpushFM";
    public static final String NAME_FIELD_ALLOWVIEWTYPE_FM = "allowViewTypeFM";
    public static final String NAME_FIELD_ALLOWPUSHTYPE_FM = "allowPushTypeFM";
    public static final String NAME_FIELD_EXTS = "exts";

    // ----------------------------云端唯一标示符---------------------------

    @DatabaseField(columnName = NAME_FIELD_CONTACT_PERSON_ID)
    private Integer contactPersonID;                                                                // 联系人id

    // ----------------------------联系人基本信息---------------------------

    @DatabaseField(columnName = NAME_FIELD_IS_ACTIVE)
    private Boolean isActive;                                                                        // 账号是否激活

    @DatabaseField(columnName = NAME_FIELD_AGE)
    private Integer age;                                                                            // 年龄
    @DatabaseField(columnName = NAME_FIELD_USERNAME)
    private String username;                                                                        // 姓名
    @DatabaseField(columnName = NAME_FIELD_NICKNAME)
    private String nickname;                                                                        // 昵称
    @DatabaseField(columnName = NAME_FIELD_HEIGHT)
    private String height;
    @DatabaseField(columnName = NAME_FIELD_BIRTHDAY)
    private String birthday;
    //联系人生日
    @DatabaseField(columnName = NAME_FIELD_ATHLETETYPE)
    private Integer athleteType;                                                                  //运动员类型标记

    @DatabaseField(columnName = NAME_FIELD_WEIGHT)
    private String weight;
    @DatabaseField(columnName = NAME_FIELD_LOCATION)
    private String location;                                                                        // 地址
    @DatabaseField(columnName = NAME_FIELD_IS_MALE)
    private Boolean gender;                                                                        // 性别,默认性别为男
    @DatabaseField(columnName = NAME_FIELD_HEAD_PORTRAITS)
    private String headPortraits;                                                                    // 联系人头像
    @DatabaseField(columnName = NAME_FIELD_PHONE)
    private String phone;                                                                            // 手机号码
    @DatabaseField(columnName = NAME_FIELD_EMAIL)
    private String email;                                                                            // 邮箱号码
    @DatabaseField(columnName = NAME_FIELD_IDCARD)
    private String iDCard;                                                                        // 身份证号码
    @DatabaseField(columnName = NAME_FIELD_RULE_STATE)
    private Integer ruleState;
    // ----------------------------我对Ta的设置----------------------

    // 其他设置
    @DatabaseField(columnName = NAME_FIELD_IS_CARE)
    private Boolean isCare;                                                                        // 是否星标联系人
    @DatabaseField(columnName = NAME_FIELD_REMARK)
    private String remark;                                                                        // 我对Ta的备注名，添加时设置
    @DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_LABELS)
    private byte[] labels;                                                                        // 联系人标签JSON串，Base64存放到数据库中

    // 权限设置
    @DatabaseField(columnName = NAME_FIELD_ALLOWGAUGE)
    private Boolean allowgauge;                                                                    // 允许Ta代我测量，默认否，须授权
    @DatabaseField(columnName = NAME_FIELD_ALLOWEDIT)
    private Boolean allowedit;                                                                        // 是否允许修改资料，仅在自动创建时授予
    @DatabaseField(columnName = NAME_FIELD_ALLOWCHAT)
    private Boolean allowchat;                                                                        // 是否接收聊天
    @DatabaseField(columnName = NAME_FIELD_ALLOWPUSH)
    private Boolean allowpush;                                                                        // 是否接收通知
    @DatabaseField(columnName = NAME_FIELD_ALLOWVIEWTYPE)
    private Integer allowViewType;                                                                    // 允许Ta查看我的数据，0=否/1=近期/2=全部
    @DatabaseField(columnName = NAME_FIELD_ALLOWPUSHTYPE)
    private Integer subsType;                                                                        // 我设置给Ta的推送类型

    // -----------------------Ta对我的设置---------------------------------------

    @DatabaseField(columnName = NAME_FIELD_ALLOWGAUGE_FM)
    private Boolean allowgaugeFM;                                                                    // 我能否替Ta代测
    @DatabaseField(columnName = NAME_FIELD_ALLOWEDIT_FM)
    private Boolean alloweditFM;                                                                    // 我能否修改Ta的资料
    @DatabaseField(columnName = NAME_FIELD_ALLOWCHAT_FM)
    private Boolean allowchatFM;                                                                    // 我能否接收聊天
    @DatabaseField(columnName = NAME_FIELD_ALLOWPUSH_FM)
    private Boolean allowpushFM;                                                                    // 我能否接收通知
    @DatabaseField(columnName = NAME_FIELD_ALLOWVIEWTYPE_FM)
    private Integer allowViewTypeFM;                                                                // 我能否查看Ta的数据，0=否/1=近期/2=全部
    @DatabaseField(columnName = NAME_FIELD_ALLOWPUSHTYPE_FM)
    private Integer allowPushTypeFM;                                                                // Ta设定给我的推送类型
    @DatabaseField(columnName = NAME_FIELD_EXTS)
    private String extraJsonStr;
    // -----------------------其他---------------------------------------

    private boolean isChoosed;                                                                        // 标记联系人是否已经被选中

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

    public ContactPerson() {
    }

    public String getDisplayName() {
        if (!TextUtils.isEmpty(remark)) {
            return remark;
        }
        if (!TextUtils.isEmpty(nickname)) {
            return nickname;
        }
        return phone;
    }

    /**
     * 获取对联系人的描述信息
     *
     * @return 手机号码/心云id/或者同步状态都是可能的
     */
    public String getDiscriptionMessage() {

        StringBuilder builder = new StringBuilder();

        if (Config.isDeveloperMode) {
            builder.append("<id:");
            builder.append(getId());
            builder.append(">");
        }

        final Account account = getBelongAccount();
        if (account != null && getContactPersonID() != null && getContactPersonID().intValue() == account.getId()) {
            builder.append("(");
            builder.append(getContactPersonID());
            builder.append(")");
            return builder.toString();
        }

        if (!TextUtils.isEmpty(getPhone())) {
            builder.append(getPhone());
        } else if (!TextUtils.isEmpty(getEmail())) {
            builder.append(getEmail());
        } else {
            if (getContactPersonID() != null) {
                builder.append("(");
                builder.append(getContactPersonID());
                builder.append(")");
            } else {
                builder.append("(");
                builder.append("未同步");
                builder.append(")");
            }
        }

        return builder.toString();
    }

    // --------------------------Setter/Getter--------------------------------------

    public Integer getContactPersonID() {
        return contactPersonID;
    }

    public void setContactPersonID(Integer contactPersonID) {
        this.contactPersonID = contactPersonID;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAthleteType(Integer athleteType) {
        this.athleteType = athleteType;
    }

    public Integer getAthleteType() {
        return athleteType;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIDCard(String idCode) {
        this.iDCard = idCode;
    }

    public String getIDCard() {
        return iDCard;
    }

    public Integer getRuleState() {
        return ruleState;
    }

    public void setRuleState(Integer ruleState) {
        this.ruleState = ruleState;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getHeadPortraits() {
        return headPortraits;
    }

    public String getDisplayHeadPortraits() {
        return headPortraits;
    }

    public void setHeadPortraits(String headPortraits) {
        this.headPortraits = headPortraits;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isCare() {
        return isCare;
    }

    public void setCare(Boolean isCare) {
        this.isCare = isCare;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // ----------------标签处理------------------

    private List<String> tagList;

    public byte[] getTags() {
        return labels;
    }

    public List<String> getTagsList() {
        return getTagsList(true);
    }

    /**
     * @param isForce 标记为true，则强制解析流并保存，否则直接读取上次解析后的结果
     * @return
     */
    private List<String> getTagsList(boolean isForce) {

        if (isForce) {
            tagList = packByte2List(labels);
        }
        return tagList;
    }

    public void setTags(byte[] tags) {
        this.labels = tags;
        getTagsList(true);
    }

    // /**
    // * 保存从服务端获取的JSON串，同时会触发强制刷新下缓存的标签列表
    // *
    // * @param tag
    // */
    // public void setTags(String jsonArr) {
    // if (TextUtils.isEmpty(jsonArr)) {
    // tags = null;
    // return;
    // }
    // tags = jsonArr.getBytes();
    // getTagsList(true);
    // }

    // ----------------------End--------------------------
    public Boolean getAllowgauge() {
        return allowgauge;
    }

    public void setAllowgauge(Boolean allowgauge) {
        this.allowgauge = allowgauge;
    }

    public Boolean getAllowedit() {
        return allowedit;
    }

    public void setAllowedit(Boolean allowedit) {
        this.allowedit = allowedit;
    }

    public Boolean getAllowchat() {
        return allowchat;
    }

    public void setAllowchat(Boolean allowchat) {
        this.allowchat = allowchat;
    }

    public Boolean getAllowpush() {
        return allowpush;
    }

    public void setAllowpush(Boolean allowpush) {
        this.allowpush = allowpush;
    }

    public Integer getAllowViewType() {
        return allowViewType;
    }

    public void setAllowViewType(Integer allowViewType) {
        this.allowViewType = allowViewType;
    }

    public Integer getSubsType() {
        return subsType;
    }

    public void setSubsType(int allowPushType) {
        this.subsType = allowPushType;
    }

    public Boolean getAllowgaugeFM() {
        return allowgaugeFM;
    }

    public void setAllowgaugeFM(Boolean allowgaugeFM) {
        this.allowgaugeFM = allowgaugeFM;
    }

    public Boolean getAlloweditFM() {
        return alloweditFM;
    }

    public void setAlloweditFM(Boolean alloweditFM) {
        this.alloweditFM = alloweditFM;
    }

    public Boolean getAllowchatFM() {
        return allowchatFM;
    }

    public void setAllowchatFM(Boolean allowchatFM) {
        this.allowchatFM = allowchatFM;
    }

    public Boolean getAllowpushFM() {
        return allowpushFM;
    }

    public void setAllowpushFM(Boolean allowpushFM) {
        this.allowpushFM = allowpushFM;
    }

    public Integer getAllowViewTypeFM() {
        return allowViewTypeFM;
    }

    public void setAllowViewTypeFM(Integer allowViewTypeFM) {
        this.allowViewTypeFM = allowViewTypeFM;
    }

    public Integer getAllowPushTypeFM() {
        return allowPushTypeFM;
    }

    public void setAllowPushTypeFM(Integer allowPushTypeFM) {
        this.allowPushTypeFM = allowPushTypeFM;
    }

    // ================================获取键值对================+
    // private JSONObject extraJson;

    private void setExts(String exts) {
        this.extraJsonStr = exts;
    }

    // /**
    // * 强制要求解析一遍String extra
    // */
    // public void invalidateExtraValueParser() {
    // obtainExtraBodyStateJsonObject();
    // }

    private JSONObject obtainExtraBodyStateJsonObject() {
        // extraJson = null;
        if (extraJsonStr != null) {
            try {
                if (!TextUtils.isEmpty(extraJsonStr)) {
                    return new JSONObject(extraJsonStr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * TODO 身体状况常量，需要指派对应的key(String)与服务器的API对应。
     *
     * @author Robert
     */
    public enum ExtraBodyState {

        HighSugar("sick_diabetes");

        private String apiKey;

        private ExtraBodyState(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiKey() {
            return apiKey;
        }

    }

    // TODO 提取到工具类中，统一为身体状况作提取
    @SuppressWarnings("unchecked")
    public <T> T getExtsValue(ExtraBodyState key, T defValue) {
        JSONObject json = obtainExtraBodyStateJsonObject();
        if (json != null) {
            try {
                String value = null;
                if (json.has(key.getApiKey())) {
                    value = json.getString(key.getApiKey());
                }
                if (defValue instanceof Boolean) {
                    return (T) Boolean.valueOf(TextUtils.equals(value, "Y"));
                }
                return (T) value;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    // ================================获取键值对================-
    public boolean isContactSelf(Account o) {
        if (o == null) {
            return false;
        }
        final Account item = (Account) o;
        int tmpID = item.getId();
        Integer cID = getContactPersonID();
        if (cID == null) {
            Log.w(TAG, "If contact person is null , show that he's not myself.");
            return false;
        }
        return cID == tmpID;
    }

    // ---------------------------工具----------------------

    /**
     * 将列表装包为比特流,不base64
     *
     * @param list
     * @return
     */
    public static byte[] packList2Byte(List<String> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        JSONArray ja = new JSONArray();
        for (String tag : list) {
            ja.put(tag);
        }
        String ret = ja.toString();
        return ret.getBytes();
    }

    /**
     * 将比特流的串解包成列表
     *
     * @param tagBytes 未base64的串
     * @return
     */
    public static List<String> packByte2List(byte[] tagBytes) {

        if (tagBytes == null) return null;
        List<String> list = null;
        String ret = new String(tagBytes);
        try {
            JSONArray jsonArray = new JSONArray(ret);

            for (int i = 0; i < jsonArray.length(); i++) {
                if (list == null) {
                    list = new ArrayList<String>();
                }
                list.add(jsonArray.get(i).toString());
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean getBooleanValue(String netFlag) {

        if (TextUtils.equals(netFlag, "Y")) {
            return true;
        }
        if (TextUtils.equals(netFlag, "N")) {
            return false;
        }
        return true;
        // throw new RuntimeException("非法的字符串传入，导致错误的解析网络返回N或Y");
    }

    /**
     * 判断联系人是否相同,判断规则为 他们的云端id保持一致.
     * <p/>
     * *两个联系人对象是附属于同一个账号.*
     *
     * @param cp
     * @return
     */
    public boolean isSame(ContactPerson person) {
        if (person == null) return false;
        if (getContactPersonID().intValue() == person.getContactPersonID().intValue()) {
            return true;
        }
        return false;
    }

    /**
     * 联系人列表的同步概念
     *
     * @param res        API回调的联系人列表
     * @param account    联系人所附加的账号
     * @param localItems 本地的联系人副本拷贝,含本地联系人
     * @return
     */
    public static List<ContactPerson> createOrUpdateContactPersonList(NetworkClientResult res, Account account) {

        // 解析云端的API,返回云端的联系人列表
        JSONArray ja = null;
        List<ContactPerson> list = null;
        JSONObject jo = res.getResponseResult();
        try {
            ja = jo.getJSONArray("root");
            list = new ArrayList<ContactPerson>();
            for (int i = 0; i < ja.length(); i++) {
                ContactPerson person = createOrUpdateContactPerson((JSONObject) ja.get(i), account, null);
                if (person != null) {
                    list.add(person);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 处理同步属性:ADD/DEL/UPDATE
        return list;
    }

    public static ContactPerson createOrUpdateContactPerson(JSONObject jo, Account account, ContactPerson person) {

        final ContactPerson tmp;
        if (person == null) {
            tmp = new ContactPerson();
            tmp.setBelongAccount(account);
        } else {
            tmp = person;
        }
        return parse(jo, tmp);
    }

    private static ContactPerson parse(JSONObject jo, ContactPerson person) {

        try {
            if (jo.has("syncid") && !jo.isNull("syncid")) {
                person.setContactPersonID(jo.getInt("syncid"));
            }
            if (jo.has("remark") && !jo.isNull("remark")) {
                person.setRemark(jo.getString("remark"));
            }
            if (jo.has("iscare") && !jo.isNull("iscare")) {
                String netFlag = jo.getString("iscare");
                person.setCare(getBooleanValue(netFlag));
            }
            if (jo.has("allowedit") && !jo.isNull("allowedit")) {
                String netFlag = jo.getString("allowedit");
                person.setAllowedit(getBooleanValue(netFlag));
            }
            if (jo.has("allowtest") && !jo.isNull("allowtest")) {
                String netFlag = jo.getString("allowtest");
                person.setAllowgauge(getBooleanValue(netFlag));
            }
            if (jo.has("allowchat") && !jo.isNull("allowchat")) {
                String netFlag = jo.getString("allowchat");
                person.setAllowchat(getBooleanValue(netFlag));
            }
            if (jo.has("allowpush") && !jo.isNull("allowpush")) {
                String netFlag = jo.getString("allowpush");
                person.setAllowpush(getBooleanValue(netFlag));
            }
            if (jo.has("allowview") && !jo.isNull("allowview")) {
                String netFlag = jo.getString("allowview");
                boolean bool = getBooleanValue(netFlag);
                person.setAllowViewType(bool ? PERM_VIEW_TYPE_ALL : PERM_VIEW_TYPE_NONE);
            }
            if (jo.has("substype") && !jo.isNull("substype")) {
                int subsType = jo.getInt("substype");
                // 订阅对方的测量动态（0=不订阅/1=短信方式/2=心云方式/3=邮件方式）。
                person.setSubsType(subsType);
            }
            if (jo.has("chrono") && !jo.isNull("chrono")) {
                //
            }

            if (jo.has("user") && !jo.isNull("user")) {
                JSONObject user = jo.getJSONObject("user");

                if (user.has("username") && !user.isNull("username")) {
                    person.setUsername(user.getString("username"));
                }
                if (user.has("nickname") && !user.isNull("nickname")) {
                    person.setNickname(user.getString("nickname"));
                }
                if (user.has("location") && !user.isNull("location")) {
                    person.setLocation(user.getString("location"));
                }
                if (user.has("imagefile") && !user.isNull("imagefile")) {
                    person.setHeadPortraits(user.getString("imagefile"));
                }
                if (user.has("age") && !user.isNull("age")) {
                    person.setAge(user.getInt("age"));
                }

                if (user.has("tall") && !user.isNull("tall")) {
                    String tall = user.getString("tall");
                    if ("null".equalsIgnoreCase(tall)){
                        person.setHeight("");
                    }else{
                        person.setHeight(user.getString("tall"));
                    }

                }
                if (user.has("birthday") && !user.isNull("birthday")) {
                    person.setBirthday(user.getString("birthday"));
                }

                if (user.has("athlete") && !user.isNull("athlete")) {
                    person.setAthleteType(user.getInt("athlete"));
                }

                if (user.has("gender") && !user.isNull("gender")) {
                    String gender = user.getString("gender");
                    boolean isMale = true;
                    if (TextUtils.equals("女", gender)) {
                        isMale = false;
                    }
                    person.setGender(isMale);
                }
                if (user.has("idcode") && !user.isNull("idcode")) {
                    person.setIDCard(user.getString("idcode"));
                }
                if (user.has("email") && !user.isNull("email")) {
                    person.setEmail(user.getString("email"));
                }
                if (user.has("phone") && !user.isNull("phone")) {
                    person.setPhone(user.getString("phone"));
                }
                if (user.has("activated") && !user.isNull("activated")) {
                    String activated = user.getString("activated");
                    if (TextUtils.equals(activated, "N")) {
                        person.setActive(false);
                    } else {
                        person.setActive(true);
                    }
                }


            }
            if (jo.has("labels") && !jo.isNull("labels")) {
                JSONArray tags = jo.getJSONArray("labels");
                person.setTags(tags.toString().getBytes());
            }
            if (jo.has("passive") && !jo.isNull("passive")) {
                JSONObject passive = jo.getJSONObject("passive");
                if (passive.has("allowtest") && !passive.isNull("allowtest")) {
                    String netFlag = passive.getString("allowtest");
                    person.setAllowgaugeFM(getBooleanValue(netFlag));
                }
                if (passive.has("allowedit") && !passive.isNull("allowedit")) {
                    String netFlag = passive.getString("allowedit");
                    person.setAlloweditFM(getBooleanValue(netFlag));
                }
                if (passive.has("allowview") && !passive.isNull("allowview")) {
                    String netFlag = passive.getString("allowview");
                    boolean bool = getBooleanValue(netFlag);
                    person.setAllowViewTypeFM(bool ? PERM_VIEW_TYPE_ALL : PERM_VIEW_TYPE_NONE);
                }
                if (passive.has("allowchat") && !passive.isNull("allowchat")) {
                    String netFlag = passive.getString("allowchat");
                    person.setAllowchatFM(getBooleanValue(netFlag));
                }
                if (passive.has("allowpush") && !passive.isNull("allowpush")) {
                    String netFlag = passive.getString("allowpush");
                    person.setAllowpushFM(getBooleanValue(netFlag));
                }
                if (passive.has("pushtype") && !passive.isNull("pushtype")) {
                    int pushType = passive.getInt("pushtype");
                    person.setAllowPushTypeFM(pushType);
                }
            }

            if (jo.has("exts") && !jo.isNull("exts")) {
                JSONObject exts = jo.getJSONObject("exts");
                person.setExts(exts.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            person.setActionFlag(ACTION_NORMAL);
            person.setStateFlag(STATE_SYNCHRONIZED);
            person.invalidate();
        }

        return person;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int getIChatSessionType() {
        return MessageSession.TYPE_CONTACT_PERSON_CHAT;
    }

    @Override
    public String getIChatInterlocutorId() {
        StringBuilder builder = new StringBuilder();
        if (parentChat != null) {
            builder.append(parentChat.getIChatInterlocutorId());
        } else {
            builder.append(INVALID_ID);
        }
        builder.append(",");
        if (getId() != null) {
            builder.append(getId());
        } else {
            builder.append(INVALID_ID);
        }
        return builder.toString();
    }

    @Override
    public String getIChatDisplayName() {
        return getDisplayName();
    }

    @Override
    public String getIChatHeadPortrait() {
        return getHeadPortraits();
    }

    @Override
    public String getIChatInterlocutorIdServer() {

        StringBuilder builder = new StringBuilder();
        if (parentChat != null) {
            builder.append(parentChat.getIChatInterlocutorIdServer());
        } else {
            builder.append(INVALID_ID);
        }
        builder.append(",");
        if (getId() != null) {
            builder.append(getContactPersonID());
        } else {
            builder.append(INVALID_ID);
        }
        return builder.toString();
    }

    @Override
    public void toMap(Map<String, String> result) {
        result.put(NAME_FIELD_AGE,String.valueOf(age));
        result.put(NAME_FIELD_TALL,String.valueOf(height));
        result.put("athletetype",String.valueOf(athleteType));
        result.put("gender",(gender==null||gender)?"男":"女");
        result.put("ruleState",String.valueOf(ruleState));
        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(obtainExtraBodyStateJsonObject());
        if (!MapUtils.isEmpty(map)) {
            result.putAll(map);
        }
    }
// dest.writeInt(getActionFlag()!=null?getActionFlag():ACTION_NORMAL);
    // dest.writeInt(getStateFlag()!=null?getStateFlag():STATE_NOT_SYNCHRONIZED);
    // dest.writeInt(id);

    private ContactPerson(Parcel source) {
        age = source.readInt();
        allowViewType = source.readInt();
        allowViewTypeFM = source.readInt();
        allowchat = source.readByte() == 1;
        allowchatFM = source.readByte() == 1;
        allowedit = source.readByte() == 1;
        alloweditFM = source.readByte() == 1;
        allowgauge = source.readByte() == 1;
        allowgaugeFM = source.readByte() == 1;
        isCare = source.readByte() == 1;
        isChoosed = source.readByte() == 1;
        isActive = source.readByte() == 1;
        contactPersonID = source.readInt();
        email = source.readString();
        byte genderFlag = source.readByte();
        if (genderFlag != -1) {
            gender = genderFlag == 1;
        }
        headPortraits = source.readString();
        labels = source.readString().getBytes();
        location = source.readString();
        nickname = source.readString();
        phone = source.readString();
        remark = source.readString();
        subsType = source.readInt();
        Account account = new Account();
        account.setId(source.readInt());
        setBelongAccount(account);
        setActionFlag(source.readInt());
        setStateFlag(source.readInt());
        id = source.readInt();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(age != null ? age : 0);
        dest.writeInt(allowViewType != null ? allowViewType : 0);
        dest.writeInt(allowViewTypeFM != null ? allowViewTypeFM : 0);

        dest.writeByte((byte) ((allowchat != null && allowchat == true) ? 1 : 0));
        dest.writeByte((byte) ((allowchatFM != null && allowchatFM == true) ? 1 : 0));
        dest.writeByte((byte) ((allowedit != null && allowedit == true) ? 1 : 0));
        dest.writeByte((byte) ((alloweditFM != null && alloweditFM == true) ? 1 : 0));
        dest.writeByte((byte) ((allowgauge != null && allowgauge == true) ? 1 : 0));
        dest.writeByte((byte) ((allowgaugeFM != null && allowgaugeFM == true) ? 1 : 0));
        dest.writeByte((byte) ((isCare != null && isCare == true) ? 1 : 0));
        dest.writeByte((byte) ((isChoosed == true) ? 1 : 0));
        dest.writeByte((byte) ((isActive != null && isActive == true) ? 1 : 0));

        dest.writeInt(contactPersonID != null ? contactPersonID : 0);
        dest.writeString(email);
        if (gender == null) {
            dest.writeByte((byte) -1);
        } else {
            dest.writeByte((byte) (gender == true ? 1 : 0));
        }
        dest.writeString(headPortraits);
        if (labels == null) labels = new byte[]{};
        dest.writeString(new String(labels));
        dest.writeString(location);
        dest.writeString(nickname);
        dest.writeString(phone);
        dest.writeString(remark);
        dest.writeInt(subsType != null ? subsType : 0);
        dest.writeInt(getBelongAccount().getId());
        dest.writeInt(getActionFlag() != null ? getActionFlag() : ACTION_NORMAL);
        dest.writeInt(getStateFlag() != null ? getStateFlag() : STATE_NOT_SYNCHRONIZED);
        dest.writeInt(id != null ? id : 0);
    }

    public static final Creator<ContactPerson> CREATOR = new Creator<ContactPerson>() {

        @Override
        public ContactPerson[] newArray(int size) {
            // TODO
            // Auto-generated
            // method stub
            return new ContactPerson[size];
        }

        @Override
        public ContactPerson createFromParcel(Parcel source) {
            // TODO
            // Auto-generated
            // method stub
            return new ContactPerson(source);
        }
    };

}
