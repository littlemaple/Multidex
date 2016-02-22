package com.medzone.mcloud.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.medzone.framework.Config;
import com.medzone.framework.Deploy;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseDatabaseObject;
import com.medzone.framework.data.bean.Gender;
import com.medzone.framework.data.controller.module.ModuleSpecification;
import com.medzone.framework.network.HttpMethod;
import com.medzone.framework.network.NetworkClientManagerProxy;
import com.medzone.framework.network.NetworkParams;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.util.StringUtils;
import com.medzone.framework.util.TimeUtils;
import com.medzone.mcloud.data.bean.dbtable.CommonResultDetailRecoReading;
import com.medzone.mcloud.data.bean.java.UnifiedOrder;

import org.apache.http.entity.InputStreamEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;


public final class NetworkClient {

    public static final String RESOURCE_TASK_DATA = "/api/taskData";
    public static final String URL_SHARE_RECOMMEND = "/app/recommend?nickname=%s";
    public static final String URL_AVATAR = "/app/groupImage?groupid=%s";
    public static final String URL_SUBSCRIBE_WEB_CENTRE = "/app/services?access_token=%s";
    public static final String URL_DOWNLOAD_APK = "/download/app?download=android";
    public static final String URL_VIEW_MEASURE_DATA = "/app/peekIndex?syncid=%d&access_token=%s";
    public static final String URL_PEEK_SUMMARY = "/app/peekSummary?syncid=%s&access_token=%s";
    public static final String URL_VIEW_APILOG = "/app/apilog?access_token=%s";
    public static final String URL_PREGLIST = "/app/pregList?day=%d";
    /* knowledge */
    public static final String URL_KNOWLEDGE = "/app/knowledge";
    public static final String URL_SUBSCRIBE_SERVICE_APPLY = "/app/applyService";
    public static final String URL_BUY_APPLY = "/site/buy";
    public static final String RESOURCE_TASK_LIST = "/api/taskList";
    private static final String RESOURCE_RECORD_ECG_SYNC = "/api/ecgDataSync";
    // private static final String RESOURCE_VERSION = "/api/version";
    private static final String RESOURCE_VERIFY = "/api/verify";
    private static final String RESOURCE_REGISTER = "/api/register";
    private static final String RESOURCE_RESETPWD = "/api/passwdReset";
    private static final String RESOURCE_LOGIN = "/api/login";
    private static final String RESOURCE_LOGOUT = "/api/logout";
    private static final String RESOURCE_PROFILE = "/api/profile";
    private static final String RESOURCE_PROFILE_EDIT = "/api/profileEdit";
    private static final String RESOURCE_PROFILE_EX = "/api/profileEx";
    /* contact api */
    private static final String RESOURCE_CONTACT_IF_REGISTER = "/api/ifRegister";
    private static final String RESOURCE_CONTACT_LIST = "/api/contactList";
    private static final String RESOURCE_CONTACT_ADD = "/api/contactAdd";
    private static final String RESOURCE_CONTACT_DEL = "/api/contactDel";
    private static final String RESOURCE_CONTACT_EDIT = "/api/contactEdit";
    private static final String RESOURCE_CONTACT_LABEL_SET = "/api/contactLabelSet";
    private static final String RESOURCE_CONTACT_APPLY_PERM = "/api/applyPerm";
    /* chat */
    private static final String RESOURCE_CHAT_MESSAGE_LIST = "/api/chatMessageList";
    private static final String RESOURCE_CHAT_MESSAGE_POST = "/api/chatMessagePost";
    private static final String RESOURCE_CHAT_MESSAGE_COUNT = "/api/chatMessageCount";
    /* record api */
    private static final String RESOURCE_RECORD_UPLOAD = "/api/recordUpload";
    private static final String RESOURCE_RECORD_DOWNLOAD = "/api/recordDownload";
    private static final String RESOURCE_RECORD_EDIT = "/api/recordEdit";
    private static final String RESOURCE_RECORD_DEL = "/api/recordDel";
    private static final String RESOURCE_RECORD_SHAREURL = "/api/recordShareUrl";
    private static final String RESOURCE_RECORD_ATTACHMENT_UPLOAD = "/api/attachment";
    /* module api */
    private static final String RESOURCE_MODULE_CONFIG = "/api/appModule";
    /* user api */
    private static final String RESOURCE_USER_QUERY = "/api/userQuery";
    /* message api */
    private static final String RESOURCE_MESSAGE_COUNT = "/api/messageCount";
    private static final String RESOURCE_MESSAGE_LIST = "/api/messageList";
    private static final String RESOURCE_MESSAGE_MARK = "/api/messageMark";
    private static final String RESOURCE_MESSAGE_RESPONSE = "/api/messageResponse";
    /* event api */
    private static final String RESOURCE_EVENT_LIST = "/api/eventList";
    /* group api */
    private static final String RESOURCE_GROUP_ADD = "/api/groupAdd";
    private static final String RESOURCE_GROUP_EDIT = "/api/groupEdit";
    private static final String RESOURCE_GROUP_DEL = "/api/groupDel";
    private static final String RESOURCE_GROUP_LIST = "/api/groupList";
    private static final String RESOURCE_GROUP_VIEW = "/api/groupView";
    private static final String RESOURCE_GROUP_AUTHORIZED = "/api/groupAuthzList";
    private static final String RESOURCE_GROUP_MEMBER_ADD = "/api/groupMemberAdd";
    private static final String RESOURCE_GROUP_MEMBER_EDIT = "/api/groupMemberEdit";
    private static final String RESOURCE_GROUP_MEMBER_PERM = "/api/groupMemberPerm";
    private static final String RESOURCE_GROUP_MEMBER_DEL = "/api/groupMemberDel";
    private static final String RESOURCE_GROUP_MEMBER_LIST = "/api/groupMemberList";
    private static final String RESOURCE_GROUP_MEMBER_CARE_LIST = "/api/groupCareList";
    /* Web */
    private static final String RESOURCE_GROUP_MEMBER_TEST_LIST = "/api/groupTestList";
    private static final String RESOURCE_GROUP_MESSAGE_COUNT = "/api/groupMessageCount";
    private static final String RESOURCE_GROUP_INVITE_URL = "/api/groupInviteUrl";
    private static final String RESOURCE_GROUP_MESSAGE_LIST = "/api/groupMessageList";
    private static final String RESOURCE_GROUP_MESSAGE_POST = "/api/groupMessagePost";
    /* rule api */
    private static final String RESOURCE_RULE_LIST = "/api/ruleList";
    private static final String RESOURCE_RULE_MATCH = "/api/ruleMatch";
    /* send email */
    private static final String RESOURCE_SEND_MAIL = "/api/sendmail";
    /* subscribe */
    private static final String RESOURCE_SUBSCRIBE_LIST = "/api/serviceList";
    private static final String RESOURCE_SUBSCRIBE_ADD = "/api/serviceAdd";
    private static final String RESOURCE_SUBSCRIBE_DEL = "/api/groupMemberDel";
    private static final String URL_PREGNANCY_DAILYSUGGEST = "/api/suggest";
    /*  open resource */
    private static final String RESOURCE_OPEN_REQUEST = "/api/openRequest";
    private static final String RESOURCE_OPEN_CONNECT = "/api/openConnect";

    /**
     * 检查单列表请求接口
     */
    private static final String RESOURCE_ACQUIRE_CHECKLIST = "/api/checkList";

    /* 用药 */
    private static final String RESOURCE_MEDICINE_LIST = "/api/medicineList";
    private static final String RESOURCE_MEDICINE_HANDLER = "/api/medicineHandle";
    private static final String RESOURCE_MEDICINE_SEARCH = "/api/medicineSearch";

    /**
     * 结果详情页阅读推荐列表和更多文章链接
     */
    private static final String RESOURCE_RESULT_DETAIL_RECOMMENDATION_ARTICLE = "/api/articleMatch";

    /**
     * 首页banner
     **/
    private static final String RESOURCE_BANNER_LIST = "/api/bannerList";

    private static final String RESOURCE_OBTAIN_LABELS = "/api/tagList";


    private static final String RESOURCE_SERVICE_LIST = "/api/service2List";
    private static final String RESOURCE_SERVICE_SEARCH = "/api/service2Search";
    private static final String RESOURCE_SERVICE_DATA = "/api/service2Data";
    private static final String RESOURCE_SERVICE_ADD_DEL = "/api/service2AddDel";
    private static final String RESOURCE_SERVICE_MESSAGE = "/api/service2Message";
    private static final String RESOURCE_SERVICE_MESSAGE_DATA = "/api/service2MessageData";
    private static final String RESOURCE_SERVICE_MESSAGE_POST = "/api/service2MessagePost";

    private static NetworkClient instance;

    private NetworkClient(final Context context) {

        if (!(Looper.getMainLooper().getThread() == Thread.currentThread())) {
            if (Looper.myLooper() == null)
                Looper.prepare();
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj != null && msg.obj instanceof JSONObject) {
                    Log.e(getClass().getSimpleName(), "handleMessage+"
                            + ((JSONObject) msg.obj).toString());

                    final JSONObject json = (JSONObject) msg.obj;
                    try {
                        if (json.has("apiversion")
                                && !json.isNull("apiversion")) {
                            final String serverApiVersion = json
                                    .getString("apiversion");
                            final Object obj;
                            if (json.has("message") && !json.isNull("message")) {
                                obj = (JSONObject) json.get("message");
                            } else {
                                obj = null;
                            }
                            NetworkClientHelper.detectAppIsForcedUpdate(
                                    context, serverApiVersion, obj);
                        }
                        if (json.has("ruleversion")
                                && !json.isNull("ruleversion")) {
                            String ruleVersion = json.getString("ruleversion");
                            NetworkClientHelper.ruleVersionForcedUpdateServer(
                                    context, ruleVersion);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        NetworkClientManagerProxy.init(context, getAppVersion(), handler);
    }

    /**
     * @param uri    短链接的形式，比如：/app/services?access_token=%s
     * @param params %s对应参数化的值
     * @return 完整的网址
     */
    public static String getWebSitePath(String uri, Object... params) {
        return NetworkClientManagerProxy.getWebSitePath(uri, params);
    }

    // ---------------------------helper + ------------------------------

    public static void init(Context context) {
        if (instance == null) {
            instance = new NetworkClient(context);
        }
    }

    // ---------------------------helper - ------------------------------

    /**
     * 当确实有需要彻底关闭程序时，调用该方法保证网络客户端资源得到正确的释放。
     */
    public static void uninit() {
        NetworkClientManagerProxy.discardClient();
    }

    public static NetworkClient getInstance() {
        return instance;
    }

    public static boolean testDNS() {
        try {
            DNSResolver dnsRes = new DNSResolver("http://api.mcloudlife.com");
            Thread t = new Thread(dnsRes);
            t.start();
            t.join(1000);
            InetAddress inetAddr = dnsRes.get();
            return inetAddr != null;
        } catch (Exception e) {
            return false;
        }
    }

    /* 用药 */

    /**
     * @param accessToken
     * @param is_history  "Y"/"N"
     * @return
     */
    public BaseResult getMedicineList(String accessToken, String is_history, String medicineId) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("is_history", is_history == null || is_history.equals("N") ? "N" : "Y");
            params.put("id", medicineId);
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params)
                            .setConnectTimeOut(15000).setSocketTimeOut(15000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult addMedicine(
            String accessToken, String medicineName, int num, int times,
            String medicinetime, String unit, boolean isclock, int addnum,
            int drugid, String starttime, String stoptime) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("isclock", isclock ? "Y" : "N");
            params.put("num", num);
            params.put("times", times);
            params.put("addnum", addnum);
            params.put("drugid", drugid);
            if (medicineName != null) {
                params.put("medicinename", medicineName);
            }
            if (medicinetime != null) {
                params.put("medicinetime", medicinetime);
            }
            if (unit != null) {
                params.put("unit", unit);
            }
            if (starttime != null) {
                params.put("starttime", starttime);
            }
            if (stoptime != null) {
                params.put("stoptime", stoptime);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_HANDLER,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult searchMedicines(String accessToken, String key) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (key != null) {
                params.put("key", key);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_SEARCH,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult delMedicine(String accessToken, String id, String medicinename,
                                  int num, int times, String medicinetime, String unit,
                                  String isclock, int addnum, int drugid, String starttime,
                                  String stoptime, String isdelete) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (id != null && !id.isEmpty()) {
                params.put("id", id);
            }
            if (medicinename != null && !medicinename.isEmpty()) {
                params.put("medicinename", medicinename);
            }
            if (num != -1) {
                params.put("num", num);
            }
            if (times != -1) {
                params.put("times", times);
            }
            if (medicinename != null && !medicinename.isEmpty()) {
                params.put("medicinetime", medicinetime);
            }
            if (unit != null && !unit.isEmpty()) {
                params.put("unit", medicinetime);
            }
            if (isclock != null && !isclock.isEmpty()) {
                params.put("isclock", isclock);
            }
            if (addnum != -1) {
                params.put("addnum", addnum);
            }
            if (drugid != -1) {
                params.put("drugid", drugid);
            }
            if (starttime != null && !starttime.isEmpty()) {
                params.put("starttime", starttime);
            }
            if (stoptime != null && !stoptime.isEmpty()) {
                params.put("stoptime", stoptime);
            }
            if (isdelete != null && !isdelete.isEmpty()) {
                params.put("isdelete", isdelete);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_HANDLER,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult stopMedicine(String accessToken, String id, String stoptime) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (id != null && !id.isEmpty()) {
                params.put("id", id);
            }
            if (stoptime != null && !stoptime.isEmpty()) {
                params.put("stoptime", stoptime);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_HANDLER,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult updateMedicine(String accessToken, int medicineId, String medicineName, int num, int times, String medicineTime, String unit, boolean isClock, int addNum, int drugId, String startTime, String stopTime, boolean isDelete) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("id", medicineId);
            if (!StringUtils.isBlank(medicineName)) {
                params.put("medicinename", medicineName);
            }
            if (num > 0) {
                params.put("num", num);
            }
            if (times > 0) {
                params.put("times", times);
            }
            if (!StringUtils.isBlank(medicineTime)) {
                params.put("medicinetime", medicineTime);
            }
            if (!StringUtils.isBlank(unit)) {
                params.put("unit", unit);
            }
            params.put("isclock", isClock ? "Y" : "N");
            if (addNum >= 1) {
                params.put("addnum", addNum);
            }
            if (drugId >= 0) {
                params.put("drugid", drugId);
            }
            if (!StringUtils.isBlank(startTime)) {
                params.put("starttime", startTime);
            }
            if (!StringUtils.isBlank(stopTime)) {
                params.put("stoptime", stopTime);
            }
            params.put("isdelete", isDelete ? "Y" : "N");
            return NetworkClientManagerProxy.call(RESOURCE_MEDICINE_HANDLER,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    /* 用药 */

    /* 首页banner */
    public BaseResult obtainBannerList() {
        JSONObject param = new JSONObject();
        return NetworkClientManagerProxy.call(RESOURCE_BANNER_LIST,
                NetworkParams.obtainBuilder().setHttpEntity(param));
    }

    /**
     * 测量详情页中阅读推荐文章和更多文章链接入口
     */
    public BaseResult obtainCommonResultRecoArticles(String accessToken, long recordId, String measureType) {
        JSONObject param = new JSONObject();
        try {
            param.put("access_token", accessToken);
            if (recordId != CommonResultDetailRecoReading.INVALID_RECORD_ID) {
                param.put("recordid", String.valueOf(recordId));
            }
            param.put("measuretype", measureType);
            return NetworkClientManagerProxy.call(RESOURCE_RESULT_DETAIL_RECOMMENDATION_ARTICLE,
                    NetworkParams.obtainBuilder().setHttpEntity(param));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param syncId  要分享的他人健康主页 ID,仅当 type=other 时有效
     * @param subType 分享的子类型 如表格分享，曲线分享 {@linkShareUrlRecordTask
     *                ShareUrlRecordTask
     *                ShareUrlRecordTask.SUBTYPE_STATS} and so on
     * @param hover   曲线中选中点的measureUID
     */
    public BaseResult shareUrlRecord(String accessToken, String type,
                                     String subType, String hover, String from, Integer syncId,
                                     Integer groupId, Integer recordId, String recent, Integer segmentId, String month,
                                     Integer period, String itemType) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            if (recordId != null)
                params.put("recordid", recordId);
            if (recent != null)
                params.put("recent", recent);
            if (month != null)
                params.put("month", month);
            if (syncId != null)
                params.put("syncid", syncId);
            if (groupId != null)
                params.put("groupid", groupId);
            if (subType != null)
                params.put("subtype", subType);
            if (hover != null)
                params.put("hover", hover);
            if (from != null)
                params.put("from", from);
            if (period != null)
                params.put("period", period);
            if (itemType != null)
                params.put("activeValue", itemType);
            if (segmentId != null) params.put("segmentid", segmentId);
            // System.out.println("<<>>#"+params.toString());
            return NetworkClientManagerProxy.call(RESOURCE_RECORD_SHAREURL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAppVersion() {
        String appVersion = Deploy.APP_VERSION_COMPLETE;

        if (Config.isDeveloperMode || Config.isTesterMode) {
            /**
             * 需要在请求的 User-Agent 中,包括WEBVIEW加入 我们的APP签 名：mCloud/2.x.y.z 如果是开发版就
             * mCloud/2.x.y.zD 最后加个大写的D
             */
            appVersion = appVersion.concat("D");
        }
        return appVersion;
    }

    public BaseResult detectApiHost() {
        return NetworkClientManagerProxy.call(null,
                NetworkParams.obtainBuilder());
    }

    /**
     * 已经在 {@link NetworkClientManagerProxy}封装了对Version的处理，所以推荐使用
     * {@link #detectApiHost()}来替代本方法。 该方法可能会触发两个getVersion操作：
     * {@link NetworkClientManagerProxy}中一次，和正常API请求一次。
     *
     * @author Robert
     */
    @Deprecated
    public BaseResult getVersion() {
        return NetworkClientManagerProxy.call(
                NetworkClientManagerProxy.RESOURCE_VERSION,
                NetworkParams.obtainBuilder());
    }

    public BaseResult doLogin(Account account) {

        JSONObject params = new JSONObject();
        try {

            String target;
            if (!TextUtils.isEmpty(account.getPhone())) {
                target = account.getPhone();
            } else {
                target = account.getEmail();
            }
            params.put("target", target);
            if (account.getPassword() != null) {
                params.put("password", account.getPassword());
            }
            if (!TextUtils.isEmpty(account.getAccessToken())) {
                params.put("token_old", account.getAccessToken());
            }
            if (!TextUtils.isEmpty(account.getPushID())) {
                params.put("push_id", account.getPushID());
            }

            return NetworkClientManagerProxy.call(RESOURCE_LOGIN,
                    NetworkParams.obtainBuilder().setConnectTimeOut(3000)
                            .setSocketTimeOut(3000).setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult doLogout(String accessToken) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(RESOURCE_LOGOUT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param target     要验证的手机号或 Email（请不要包含+86 或 0开头）
     * @param template   发送短信或邮件内容模板，{code} 自动被替换为验证码
     * @param checkExist 同时检测是否已经被注册：Y=要求已注册，N=要求未注册，默认不做检测
     * @param checkCode  用户填写的验证码，若提供此参数则该请求只做验证不会发送短信
     */
    public BaseResult verifyAccount(String target, String template,
                                    Boolean checkExist, String checkCode) {
        JSONObject params = new JSONObject();
        try {
            params.put("target", target);
            if (!TextUtils.isEmpty(template))
                params.put("template", template);
            if (checkExist != null)
                params.put("check_exist", checkExist.booleanValue() ? "Y" : "N");
            if (!TextUtils.isEmpty(checkCode))
                params.put("check_code", checkCode);

            return NetworkClientManagerProxy.call(RESOURCE_VERIFY,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param target   用户输入的手机号或邮箱 必填
     * @param code     验证码 必填
     * @param password 密码 必填
     * @param nickname
     * @param isMale
     * @param birthday
     * @param location
     * @return
     */
    public BaseResult doRegisterByEmail(String target, String code,
                                        String password, String nickname, Boolean isMale, Date birthday,
                                        String location) {
        JSONObject params = new JSONObject();

        try {
            params.put("email", target);
            params.put("email_code", code);
            params.put("password", password);
            params.put("nickname", nickname);
            params.put("birthday", TimeUtils.getTime(birthday.getTime(),
                    TimeUtils.DATE_FORMAT_DATE));
            params.put("gender", isMale ? Gender.MALE : Gender.FEMALE);
            if (location != null)
                params.put("location", location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return NetworkClientManagerProxy.call(RESOURCE_REGISTER, NetworkParams
                .obtainBuilder().setHttpEntity(params));
    }

    /**
     * @param target
     * @param code
     * @param password
     * @param nickname
     * @param isMale
     * @param birthday
     * @param location
     * @return
     */
    public BaseResult doRegisterByPhone(String target, String code,
                                        String password, String nickname, Boolean isMale, Date birthday,
                                        String location) {
        JSONObject params = new JSONObject();
        try {
            params.put("phone", target);
            params.put("phone_code", code);
            params.put("password", password);
            params.put("nickname", nickname);
            params.put("birthday", TimeUtils.getTime(birthday.getTime(),
                    TimeUtils.DATE_FORMAT_DATE));
            params.put("gender", isMale ? Gender.MALE : Gender.FEMALE);
            if (location != null)
                params.put("location", location);
            return NetworkClientManagerProxy.call(RESOURCE_REGISTER,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param target
     * @param checkCode
     * @param passwordNew
     * @return
     */
    public BaseResult resetPwd(String target, String checkCode,
                               String passwordNew) {
        JSONObject params = new JSONObject();
        try {

            params.put("target", target);
            params.put("check_code", checkCode);
            params.put("password_new", passwordNew);

            return NetworkClientManagerProxy.call(RESOURCE_RESETPWD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @return
     */
    public BaseResult getAccount(String accessToken, Integer otherId) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            if (otherId != null) {
                params.put("otherid", otherId);
            }
            return NetworkClientManagerProxy.call(RESOURCE_PROFILE,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param account
     * @param code        参数顺序：先旧后新
     * @return
     * @author Robert.
     */
    public BaseResult editAccount(String accessToken, Integer otherId,
                                  Account account, String... code) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (otherId != null) {
                params.put("otherid", otherId);
            }
            if (account.getPassword() != null) {
                params.put("password", account.getPassword());
            }
            if (account.getPhone() != null) {
                params.put("phone", account.getPhone());
            }
            if (code[0] != null) {
                params.put("phone_code_old", code[0]);// 不确定是否还使用?先兼容着!
            }
            if (code[1] != null) {
                params.put("phone_code_new", code[1]);// 不确定是否还使用?先兼容着!
                params.put("phone_code", code[1]);
            }
            if (account.getEmail() != null) {
                params.put("email", account.getEmail());
            }
            if (code[0] != null) {
                params.put("email_code", code[0]);
            }
            if (account.isMale() != null) {
                params.put("gender", account.isMale() ? Gender.MALE
                        : Gender.FEMALE);
            }
            if (account.getBirthday() != null) {
                params.put("birthday", TimeUtils.getTime(account.getBirthday()
                        .getTime(), TimeUtils.DATE_FORMAT_DATE));
            }
            if (account.getLocation() != null) {
                params.put("location", account.getLocation());
            }
            if (account.getRealName() != null) {
                params.put("username", account.getRealName());
            }
            if (account.getHeadPortRait() != null) {
                params.put("avatar", account.getHeadPortRait());
            }
            // 暂时不同步该字段
            // if (account.getFlag() != null) {
            // params.put("flag1", account.getFlag());
            // }
            if (account.getAddress() != null) {
                params.put("address", account.getAddress());
            }
            if (account.getNickname() != null) {
                params.put("nickname", account.getNickname());
            }
            if (account.getIDCard() != null) {
                params.put("idcode", account.getIDCard());
            }
            if (account.getEmail2() != null) {
                params.put("email2", account.getEmail2());
            }
            if (account.getPhone2() != null) {
                params.put("phone2", account.getPhone2());
            }
            if (account.getTall() != null) {
                params.put("tall", account.getTall().intValue());
            }
            if (account.getWeight() != null) {
                params.put("weight", account.getWeight());
            }
            if (account.getPrebornday() != null) {
                params.put("prebornday", account.getPrebornday());
            }
            if (account.getAthleteType() != null) {
                params.put("athletetype", account.getAthleteType());
            }
            return NetworkClientManagerProxy.call(RESOURCE_PROFILE_EDIT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param phones      需要判定的手机号码列表，多个手机号用半角逗号分隔。
     * @return
     */
    public BaseResult vertyContactsIfRegister(String accessToken, String phones) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("phones", phones);
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_IF_REGISTER,
                    NetworkParams.obtainBuilder().setHttpEntity(params)
                            .setConnectTimeOut(15000).setSocketTimeOut(15000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken    string 在登录授权后得到，参见如何登入。
     * @param otherID        int 列出目标用户 ID 的联系人（需要有编辑对方资料的权限，通常配合 filter_pustype=1 使用）。
     * @param filterPushType int 过滤只显示推送设定相匹配的联系人（0=不推/1=短信/2=心云/3=邮件）。
     */
    public BaseResult getAllContact(String accessToken, Integer otherID,
                                    Integer filterPushType) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            if (otherID != null) {
                params.put("otherid", otherID.intValue());
            }
            if (filterPushType != null) {
                params.put("filter_pushtype", filterPushType.intValue());
            }
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params)
                            .setConnectTimeOut(15000).setSocketTimeOut(15000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param username
     * @param phone
     * @param idcode
     * @param otherid     为他人用户创建的 通知联系人
     * @return
     */
    public BaseResult addContact(String accessToken, String username,
                                 String phone, String idcode, String addr, String avatar,
                                 Boolean gender, Integer otherid) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("username", username);
            if (phone != null) {
                params.put("phone", phone);
            }
            if (idcode != null) {
                params.put("idcode", idcode);
            }
            if (addr != null) {
                params.put("address", addr);
            }
            if (avatar != null) {
                params.put("avatar", avatar);
            }
            if (otherid != null) {
                params.put("otherid", otherid);
            }
            if (gender != null) {
                params.put("gender", gender ? Gender.MALE : Gender.FEMALE);
            }
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_ADD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param contactId
     * @return
     */
    public BaseResult delContact(String accessToken, String contactId,
                                 Integer otherID) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("syncid", contactId);
            if (otherID != null) {
                params.put("otherid", otherID);
            }
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param contactId
     * @param remark
     * @param iscare
     * @param allowedit
     * @param allowtest
     * @param allowchat
     * @param allowpush
     * @param allowview
     * @param substype
     * @param labels
     * @return
     */
    public BaseResult editContact(String accessToken, String contactId,
                                  String remark, String iscare, String allowedit, String allowtest,
                                  String allowchat, String allowpush, String allowview,
                                  Integer substype, String labels) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("syncid", contactId);
            if (remark != null)
                params.put("remark", remark);
            if (iscare != null)
                params.put("iscare", iscare);
            if (allowedit != null)
                params.put("allowedit", allowedit);
            if (allowtest != null)
                params.put("allowtest", allowtest);
            if (allowchat != null)
                params.put("allowchat", allowchat);
            if (allowpush != null)
                params.put("allowpush", allowpush);
            if (allowview != null)
                params.put("allowview", allowview);
            if (substype != null)
                params.put("substype", substype);
            if (labels != null)
                params.put("labels", labels);

            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_EDIT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult markContactTag(String accessToken, String newTag,
                                     String oldTag, String contactIDs) {
        JSONObject params = new JSONObject();

        try {
            params.put("access_token", accessToken);
            params.put("label", newTag);
            params.put("syncid_list", contactIDs);
            if (oldTag != null) {
                params.put("label_old", oldTag);
            }
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_LABEL_SET,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param data
     * @param syncId
     * @param serial      false
     * @param limit       false
     * @return
     */

    public BaseResult uploadRecord(String accessToken, String type,
                                   String data, Integer syncId, Long serial, Integer limit) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("up_data", data);
            if (syncId != null)
                params.put("up_syncid", syncId);
            if (serial != null)
                params.put("down_serial", serial.intValue());
            if (limit != null)
                params.put("down_limit", limit.intValue());
            String base = NetworkClientManagerProxy.getBaseUri();
            System.out.println();
            return NetworkClientManagerProxy.call(RESOURCE_RECORD_UPLOAD,
                    NetworkParams.obtainBuilder().setHttpEntity(params)
                            .setConnectTimeOut(15000).setSocketTimeOut(15000));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param timeRange
     * @param endId
     * @param serial
     * @param limit
     * @param offset
     * @param sort
     * @param downSyncid  待下载的帐号id,默认为当前自己
     * @return
     */
    public BaseResult getRecord(String accessToken, String type,
                                String timeRange, Integer endId, Integer serial, Integer limit,
                                Integer offset, String sort, Integer downSyncid) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("time_range", timeRange);
            params.put("end_id", endId);
            params.put("down_serial", serial);
            params.put("limit", limit);
            params.put("offset", offset);
            params.put("sort", sort);
            if (downSyncid != null) {
                params.put("down_syncid", downSyncid);
            }

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_DOWNLOAD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult getRecord(String accessToken, String type) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_DOWNLOAD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult getRecord(String accessToken, String type, String valueType,
                                String timeRange, Integer endId, Integer serial, Integer limit,
                                Integer offset, String sort, Integer downSyncid) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("value_type", valueType);
            params.put("time_range", timeRange);
            params.put("end_id", endId);
            params.put("down_serial", serial);
            params.put("limit", limit);
            params.put("offset", offset);
            params.put("sort", sort);
            if (downSyncid != null) {
                params.put("down_syncid", downSyncid);
            }

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_DOWNLOAD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param recordId
     * @param readMe
     * @param value1
     * @param value2
     * @param value3
     * @return
     */
    public BaseResult editRecord(String accessToken, String type,
                                 Integer recordId, String readMe, Float value1, Float value2,
                                 Float value3) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("recordid", recordId);
            params.put("readme", readMe);
            params.put("value1", value1);
            params.put("value2", value2);
            params.put("value3", value3);

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_EDIT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param recordId
     * @return
     */
    public BaseResult delRecord(String accessToken, String type,
                                Integer recordId) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("recordid", recordId);

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param recordId    多个id用 , 连接
     * @return
     */
    public BaseResult delRecord(String accessToken, String type, String recordId) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            params.put("recordid", recordId);

            return NetworkClientManagerProxy.call(RESOURCE_RECORD_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type
     * @param syncId      要分享的他人健康主页 ID,仅当 type=other 时有效
     * @param groupId
     * @param recordId
     * @param recent
     * @param month
     * @param subType     分享的子类型 如表格分享，曲线分享 @link ShareUrlRecordTask
     *                    ShareUrlRecordTask
     *                    ShareUrlRecordTask.SUBTYPE_STATS and so on
     * @param hover       曲线中选中点的measureUID
     * @return
     */
    public BaseResult shareUrlRecord(String accessToken, String type,
                                     String subType, String hover, String from, Integer syncId,
                                     Integer groupId, Integer recordId, String recent, String month,
                                     Integer period) {
        JSONObject params = new JSONObject();
        try {

            params.put("access_token", accessToken);
            params.put("type", type);
            if (recordId != null)
                params.put("recordid", recordId);
            if (recent != null)
                params.put("recent", recent);
            if (month != null)
                params.put("month", month);
            if (syncId != null)
                params.put("syncid", syncId);
            if (groupId != null)
                params.put("groupid", groupId);
            if (subType != null)
                params.put("subtype", subType);
            if (hover != null)
                params.put("hover", hover);
            if (from != null)
                params.put("from", from);
            if (period != null)
                params.put("period", period);
            // System.out.println("<<>>#"+params.toString());
            return NetworkClientManagerProxy.call(RESOURCE_RECORD_SHAREURL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult addGroup(String accessToken, String title,
                               String description) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("title", title);
            params.put("description", description);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_ADD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult editGroup(String accessToken, Integer groupid,
                                String title, String description, String image) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("title", title);
            params.put("description", description);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            params.put("image", image);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_EDIT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/* group */

    public BaseResult delGroup(String accessToken, Integer groupid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult getGroupByID(String accessToken, Integer groupid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_VIEW,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult getAllGroup(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult getAllGroupMember(String accessToken, Long groupid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param type        默认为view
     * @return
     */
    public BaseResult getAuthoList(String accessToken, String type) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (type != null) {
                params.put("type", type);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_AUTHORIZED,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/* group member */

    /**
     * @param accessToken
     * @param groupid
     * @param isupload
     * @param alert       群消息提醒方式 0，1，2
     * @return
     */
    public BaseResult editGroupMemberSetting(String accessToken,
                                             Integer groupid, Boolean isupload, Integer alert) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (isupload != null) {
                params.put("isupload", isupload.booleanValue() ? "Y" : "N");
            }
            if (alert != null) {
                params.put("alert", alert);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_EDIT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param syncid      目标群成员 账号ID
     * @param groupid     共同群ID
     * @param isView      查看 Y/N
     * @param isCare      关心 Y/N
     * @param isTest      测试 Y/N
     * @param remark      备注
     * @return
     */
    public BaseResult editGroupMemberPerm(String accessToken, Integer syncid,
                                          Integer groupid, Boolean isView, Boolean isCare, Boolean isTest,
                                          String remark) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);

            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }
            if (isView != null) {
                params.put("isview", isView == true ? "Y" : "N");
            }
            if (isCare != null) {
                params.put("iscare", isCare == true ? "Y" : "N");
            }
            if (isTest != null) {
                params.put("istest", isTest == true ? "Y" : "N");
            }
            if (remark != null) {
                params.put("remark", remark);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_PERM,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param reverse 是否反向查询对方对我的授权设置。（Y=是/N=否，默认为 N。设置为 Y 后只允许获取设置不允许改变设置）
     * @return
     */
    public BaseResult getAuthoForMe(String accessToken, Integer syncid,
                                    String reverse) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("syncid", syncid);
            params.put("reverse", reverse);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_PERM,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * @param accessToken
     * @param groupid
     * @param syncid
     * @return
     */
    public BaseResult delGroupMember(String accessToken, Integer groupid,
                                     Integer syncid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * @param accessToken
     * @param groupid
     * @param syncid
     * @return
     */
    public BaseResult addGroupMember(String accessToken, Integer groupid,
                                     Integer syncid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MEMBER_ADD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param upDataList
     * @param syncid
     * @return
     */
    public BaseResult getAppModule(String accessToken,
                                   List<ModuleSpecification> upDataList, Integer syncid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (upDataList != null) {
                JSONArray ja = new JSONArray();
                for (ModuleSpecification ms : upDataList) {
                    JSONObject o = new JSONObject();
                    o.put("moduleid", ms.getModuleID());
                    o.put("category", ms.getCategory());
                    o.put("classname", ms.getClassName());
                    o.put("link", ms.getDownLoadLink());
                    o.put("packagename", ms.getPackageName());
                    o.put("order", ms.getOrder());
                    o.put("status", ms.getModuleStatus().getStatusId());
                    o.put("settings", ms.getSetting());
                    o.put("default_install",
                            ms.getExtraAttributeBool(
                                    ModuleSpecification.EXTRA_ATTRIBUTES_DEFAULT_INSTALL,
                                    false));
                    o.put("default_show_in_homepage",
                            ms.getExtraAttributeBool(
                                    ModuleSpecification.EXTRA_ATTRIBUTES_DEFAULT_SHOW_IN_HOMEPAGE,
                                    false));
                    o.put("is_uninstallable",
                            ms.getExtraAttributeBool(
                                    ModuleSpecification.EXTRA_ATTRIBUTES_IS_UNINSTALLABLE,
                                    false));
                    ja.put(o);
                }
                params.put("up_data", ja.toString());
            }
            if (syncid != null) {
                params.put("syncid", syncid);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MODULE_CONFIG,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    private BaseResult getAppModule(String accessToken,
                                    ModuleSpecification moduleSpecification) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (moduleSpecification != null) {
                JSONObject jo = new JSONObject();
                jo.put("moduleid", moduleSpecification.getModuleID());
                jo.put("category", moduleSpecification.getCategory());
                jo.put("classname", moduleSpecification.getClassName());
                jo.put("link", moduleSpecification.getDownLoadLink());
                jo.put("packagename", moduleSpecification.getPackageName());
                jo.put("order", moduleSpecification.getOrder());
                jo.put("status", moduleSpecification.getModuleStatus()
                        .getStatusId());
                jo.put("settings", moduleSpecification.getSetting());
                jo.put("default_install",
                        moduleSpecification
                                .getExtraAttributeBool(
                                        ModuleSpecification.EXTRA_ATTRIBUTES_DEFAULT_INSTALL,
                                        false));
                jo.put("default_show_in_homepage",
                        moduleSpecification
                                .getExtraAttributeBool(
                                        ModuleSpecification.EXTRA_ATTRIBUTES_DEFAULT_SHOW_IN_HOMEPAGE,
                                        false));
                jo.put("is_uninstallable",
                        moduleSpecification
                                .getExtraAttributeBool(
                                        ModuleSpecification.EXTRA_ATTRIBUTES_IS_UNINSTALLABLE,
                                        false));
                params.put("up_data", jo.toString());
            }
            return NetworkClientManagerProxy.call(RESOURCE_MODULE_CONFIG,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param target
     * @return
     */
    public BaseResult queryAccountInfo(String accessToken, String target,
                                       Integer groupID) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("target", target);
            if (groupID != null && groupID != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupID);
            }

            return NetworkClientManagerProxy.call(RESOURCE_USER_QUERY,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @return
     */
    public BaseResult getMessageCount(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(RESOURCE_MESSAGE_COUNT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param address     邮件接收地址，多个邮箱之间用半角逗号分隔，不合法的邮箱会自动被忽略。
     * @param subject     邮件主题
     * @param body        邮件内容
     * @return
     */
    public BaseResult sendMail(String accessToken, String address,
                               String subject, String body) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("address", address);
            params.put("subject", subject);
            params.put("body", body);
            return NetworkClientManagerProxy.call(RESOURCE_SEND_MAIL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param groupId
     * @return
     */
    public BaseResult getGroupInviteUrl(String accessToken, Integer groupId) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupId != null)
                params.put("groupid", groupId);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_INVITE_URL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param limit
     * @param offset
     * @param system
     * @return
     */
    public BaseResult getGroupMessageList(String accessToken, Integer limit,
                                          Integer offset, Integer system) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (limit != null)
                params.put("limit", limit);
            if (offset != null)
                params.put("offset", offset);
            if (system != null)
                params.put("system", system);

            return NetworkClientManagerProxy.call(RESOURCE_MESSAGE_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param messageid
     * @param isread
     * @param system
     * @return
     */
    @Deprecated
    public BaseResult markNotifyMessageReaded(String accessToken,
                                              Integer messageid, Boolean isread, Integer system) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (messageid != null) {
                params.put("messageid", messageid);
            }
            if (isread != null) {
                params.put("isread", isread.booleanValue() ? "Y" : "N");
            }
            if (system != null) {
                params.put("system", system);
            }

            return NetworkClientManagerProxy.call(RESOURCE_MESSAGE_MARK,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param messageid
     * @param isread      Y=已读/N=未读/D=删除
     * @param system
     * @return
     */
    @Deprecated
    public BaseResult markMessageReaded(String accessToken, Integer messageid,
                                        String isread, Integer system) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (messageid != null) {
                params.put("messageid", messageid);
            }
            if (isread != null) {
                params.put("isread", isread);
            }
            if (system != null) {
                params.put("system", system);
            }

            return NetworkClientManagerProxy.call(RESOURCE_MESSAGE_MARK,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * @param accessToken
     * @param messageid
     * @param response
     * @return
     */
    public BaseResult markMessageResponse(String accessToken,
                                          Integer messageid, String response, String extra) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (messageid != null)
                params.put("messageid", messageid);
            if (response != null)
                params.put("response", response);
            if (extra != null) {
                params.put("extra", extra);
            }
            return NetworkClientManagerProxy.call(RESOURCE_MESSAGE_RESPONSE,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param syncid
     * @param groupid
     * @param limit
     * @param offset
     * @return
     */
    public BaseResult getEventList(String accessToken, Integer syncid,
                                   Integer groupid, Integer limit, Integer offset) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);

            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }
            if (limit != null) {
                params.put("limit", limit);
            }
            if (offset != null) {
                params.put("offset", offset);
            }
            return NetworkClientManagerProxy.call(RESOURCE_EVENT_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @return
     */
    public BaseResult getGroupMemberCareList(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(
                    RESOURCE_GROUP_MEMBER_CARE_LIST, NetworkParams
                            .obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @return
     */
    public BaseResult getGroupMemberTestList(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(
                    RESOURCE_GROUP_MEMBER_TEST_LIST, NetworkParams
                            .obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param syncid
     * @param groupid
     * @param begin_id
     * @param end_id
     * @return
     */
    public BaseResult getGroupMessageCount(String accessToken, Integer syncid,
                                           Integer groupid, Integer begin_id, Integer end_id, Boolean unRead) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }

            if (begin_id != null) {
                params.put("begin_id", begin_id);
            }
            if (end_id != null) {
                params.put("end_id", end_id);
            }
            if (unRead != null) {
                params.put("unread", unRead.booleanValue() ? "Y" : "N");
            }
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MESSAGE_COUNT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param syncid
     * @param groupid
     * @param data
     * @param type
     * @return
     */
    public BaseResult postGroupMessage(String accessToken, Integer syncid,
                                       Integer groupid, String data, Integer type) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);

            if (groupid != null && groupid != BaseDatabaseObject.INVALID_ID) {
                params.put("groupid", groupid);
            }
            if (syncid != null && syncid != Account.INVALID_ID) {
                params.put("syncid", syncid);
            }
            if (type != null) {
                params.put("type", type);
            }
            params.put("data", data);

            return NetworkClientManagerProxy.call(RESOURCE_GROUP_MESSAGE_POST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param measuretype
     * @return
     */
    public BaseResult getRuleList(Integer measuretype) {
        JSONObject params = new JSONObject();
        try {
            params.put("measuretype", measuretype);

            return NetworkClientManagerProxy.call(RESOURCE_RULE_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param syncid
     * @param type
     * @param value1
     * @param value2
     * @param value3
     * @param avg
     * @param valueDuration
     * @param valuePeriod
     * @param eval
     * @param state
     * @return
     */
    public BaseResult doRuleMatch(Integer syncid, String type, Float value1,
                                  Float value2, Float value3, Float avg, Integer valueDuration,
                                  Integer valuePeriod, String eval, Integer state) {
        JSONObject params = new JSONObject();
        try {
            params.put("type", type);
            params.put("syncid", syncid);
            params.put("value1", value1);
            if (value2 != null) {
                params.put("value2", value2);
            }
            if (value3 != null) {
                params.put("value3", value3);
            }
            if (avg != null) {
                params.put("value1_avg", avg);
            }
            if (valuePeriod != null) {
                params.put("value_period", valuePeriod);
            }
            if (valueDuration != null) {
                params.put("value_duration", valueDuration);
            }
            if (null != eval) {
                params.put("eval", eval);
            }
            if (state != null) {
                params.put("state", state);
            }
            return NetworkClientManagerProxy.call(RESOURCE_RULE_MATCH,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param groupId     提取消息所在群 ID。
     * @param syncid      对话的联系人 ID，此项和 groupid 至少必须提供一项。
     * @param beginId     起始消息 ID，只获取之后的数据。
     * @param endId       截止消息 ID，只获取之前的数据。
     * @param limit       最大获取条数，默认为 10。
     * @param offset      要跳过的数据条数据，默认为 0。
     * @return
     */
    public BaseResult getChatMessagesList(String accessToken, Long groupId,
                                          Long syncid, Integer beginId, Integer endId, Integer limit,
                                          Integer offset) {

        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupId != null)
                params.put("groupid", groupId);
            if (syncid != null)
                params.put("syncid", syncid);
            if (beginId != null)
                params.put("begin_id", beginId);
            if (endId != null)
                params.put("end_id", endId);
            if (limit != null)
                params.put("limit", limit);
            if (offset != null)
                params.put("offset", offset);
            return NetworkClientManagerProxy.call(RESOURCE_CHAT_MESSAGE_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param accessToken
     * @param groupId     发送消息到群的 ID。
     * @param syncid      发送消息给联系人的 ID，此项和 groupid 至少必须提供一项。
     * @param type        消息类型，默认为普通消息
     * @param data        消息内容，非普通消息请直接提供 JSON 编码的合法数据。
     * @return
     */
    public BaseResult postChatMessage(String accessToken, Long groupId,
                                      Long syncid, Integer type, String data) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("data", data);
            if (groupId != null)
                params.put("groupid", groupId);
            if (syncid != null)
                params.put("syncid", syncid);
            if (type != null)
                params.put("type", type);
            return NetworkClientManagerProxy.call(RESOURCE_CHAT_MESSAGE_POST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ----------------------------------Chat---------------------------------

    /**
     * @param accessToken
     * @param groupId     提取消息所在群 ID，设为 0 提供表示统计全部群，可配合 unread 使用
     * @param syncid      提取消息对话的联系人 ID，设为 0 提供全部联系人的消息数量，通常配合 unread 使用。
     * @param unread      是否只统计上次查看消息时间点以来的新消息数量，设置为 Y=仅未读/N=全部。
     * @return
     */
    public BaseResult getChatMessagesCount(String accessToken, Long groupId,
                                           Long syncid, Boolean unread) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (groupId != null)
                params.put("groupid", groupId);
            if (syncid != null)
                params.put("syncid", syncid);
            if (unread != null) {
                params.put("unread", unread.booleanValue() ? "Y" : "N");
            }
            return NetworkClientManagerProxy.call(RESOURCE_CHAT_MESSAGE_COUNT,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken
     * @param contactId   目标联系人 ID。
     * @param perm        需要请求的权限（allowedit=修改资料/allowview=查看数据/allowtest=代测）。
     * @param code        授权码，凭此码可自动完成授权（等候对方告知）
     * @return
     */
    public BaseResult applyPerm(String accessToken, Integer contactId,
                                String perm, String code) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("syncid", contactId);
            params.put("perm", perm);
            if (code != null) {
                params.put("code", code);
            }
            return NetworkClientManagerProxy.call(RESOURCE_CONTACT_APPLY_PERM,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult profileEx(String accessToken, String type,
                                String uploadData, Integer otherId, String full, String prefix) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (type != null)
                params.put("type", type);
            if (uploadData != null)
                params.put("up_data", uploadData);
            if (otherId != null)
                params.put("otherid", otherId);
            if (full != null)
                params.put("full", full);
            if (prefix != null)
                params.put("prefix", prefix);
            return NetworkClientManagerProxy.call(RESOURCE_PROFILE_EX,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult listSubscribed(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("filter_type", 1);
            return NetworkClientManagerProxy.call(RESOURCE_GROUP_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken access_token true string 在登录授权后得到，参见如何登入。
     * @param keyword     keyword false string 机构名称搜索关键字。
     * @param limit       limit false int 返回列表数量，默认为 10。
     * @return
     */
    public BaseResult listSubscribe(String accessToken, String keyword,
                                    Integer limit) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (keyword != null) {
                params.put("keyword", keyword);
            }
            if (limit != null) {
                params.put("limit", limit);
            }
            return NetworkClientManagerProxy.call(RESOURCE_SUBSCRIBE_LIST,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // --------------------------------------订阅号管理-----------------------------------

    public BaseResult addSubscribe(String accessToken, Integer groupid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("groupid", groupid);
            return NetworkClientManagerProxy.call(RESOURCE_SUBSCRIBE_ADD,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult delSubscribe(String accessToken, Integer groupid) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("groupid", groupid);
            return NetworkClientManagerProxy.call(RESOURCE_SUBSCRIBE_DEL,
                    NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在服务器端注册，并创建URL用于接收APP发送的文件流。
     *
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param filename    要上传的文件名，建议取名为：{type}-{recordid}-{sizeX}.{ext}，其中： {type} =
     *                    测量类型，如：bp, bs, oxy ... {recordid} = 测量数据的云端 ID {sizeX} =
     *                    文件大小的十六进制 {ext} = 文件实际扩展名，如：wav
     * @param size        要上传文件的完整字节数。
     */
    public BaseResult requestStorageSpace(String accessToken, String filename,
                                          long size) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("filename", filename);
            params.put("size", size);
            return NetworkClientManagerProxy.call(
                    RESOURCE_RECORD_ATTACHMENT_UPLOAD, NetworkParams
                            .obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param requestPath 接收文件流的处理类
     * @param entity      文件流实体，更多资料参见{@link # HttpEntity}
     * @return
     */
    public BaseResult requestUploadFile(String requestPath, HttpMethod method,
                                        InputStreamEntity entity) {
        return NetworkClientManagerProxy.call(requestPath, NetworkParams
                .obtainBuilder().setHttpMethod(method).setHttpEntity(entity));
    }

    public BaseResult requestTaskList(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (!Config.isTestApi)
                return NetworkClientManagerProxy.call(RESOURCE_TASK_LIST, NetworkParams.obtainBuilder().setHttpEntity(params));
            else {
                return NetworkClientManagerProxy.call("http://archive.sinaapp.com/api/tasklist", NetworkParams.obtainBuilder().setHttpEntity(params));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult requireDailySuggest(String token, String date) {
        JSONObject params = new JSONObject();

        try {
            params.put("access_token", token);
            if (!TextUtils.isEmpty(date)) {
                params.put("date", date);
            }
            return NetworkClientManagerProxy.call(URL_PREGNANCY_DAILYSUGGEST, NetworkParams.obtainBuilder().setHttpEntity(params));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BaseResult openConnect(String phone, String phoneCode, String openCode, String grants) {
        JSONObject params = new JSONObject();

        try {
            params.put("phone", phone);
            params.put("phone_code", phoneCode);
            params.put("open_code", openCode);
            if (!TextUtils.isEmpty(grants)) {
                params.put("open_grants", grants);
            }
            return NetworkClientManagerProxy.call(RESOURCE_OPEN_CONNECT, NetworkParams.obtainBuilder().setHttpEntity(params));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BaseResult openRequest(String partnerData, String accessToken, String pushId) {
        JSONObject params = new JSONObject();

        try {
            params.put("partner_data", partnerData);
            if (!TextUtils.isEmpty(accessToken)) {
                params.put("access_token", accessToken);
            }
            if (!TextUtils.isEmpty(pushId)) {
                params.put("push_id", pushId);
            }
            return NetworkClientManagerProxy.call(RESOURCE_OPEN_REQUEST, NetworkParams.obtainBuilder().setHttpEntity(params));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public BaseResult getOrUpdateTaskList(String accessToken, int taskId, Boolean isOpen, Boolean isFinished) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("taskid", taskId);
            if (isOpen != null) params.put("isopen", isOpen ? "Y" : "N");
            if (isFinished != null) params.put("isfinished", isFinished ? "Y" : "N");
            return NetworkClientManagerProxy.call(RESOURCE_TASK_DATA, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * @param accessToken
     * @return
     */
    public BaseResult ecgDataSync(String accessToken, String op, String measureuid, Integer recordID, String segmentid, String upData) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);

            if (op != null) {
                params.put("op", op);
            }
            if (measureuid != null) {
                params.put("measureuid", measureuid);
            }
            if (recordID != null) {
                params.put("recordid", recordID);
            }
            if (segmentid != null) {
                params.put("segmentid", segmentid);
            }
            if (upData != null) {
                params.put("up_data", upData);
            }
//			Log.v(TAG, "<<>># parms = " + params.toString());
            return NetworkClientManagerProxy.call(RESOURCE_RECORD_ECG_SYNC, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult acquireCheckListItem(String accessToken, String type) {
        JSONObject params = new JSONObject();
        try {
            if (type != null) {
                params.put("value_type", type);
            }
            return NetworkClientManagerProxy.call(RESOURCE_ACQUIRE_CHECKLIST, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult obtainLabels(String accessToken, String type) {
        if (Config.isTestApi)
            return NetworkClientManagerProxy.call("http://archive.sinaapp.com/api/labels/", NetworkParams.obtainBuilder());
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (!TextUtils.isEmpty(type))
                params.put("type", type);
            return NetworkClientManagerProxy.call(RESOURCE_OBTAIN_LABELS, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param isFollow    是否取消关注
     * @return BaseResult
     */
    public BaseResult followService(String accessToken, int serviceId, boolean isFollow) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("serviceid", serviceId);
            params.put("delete", isFollow ? "Y" : "N");
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_ADD_DEL, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @return BaseResult
     */
    public BaseResult getServiceList(String accessToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_LIST, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @return BaseResult
     */
    public BaseResult findService(String accessToken, String keyword, Integer limit) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            if (!TextUtils.isEmpty(keyword))
                params.put("keyword", keyword);
            if (limit != null)
                params.put("limit", limit);
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_SEARCH, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param serviceId   服务号id。
     * @return BaseResult
     */
    public BaseResult getServiceData(String accessToken, int serviceId) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("serviceid", serviceId);
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_DATA, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param serviceId   服务号id。
     * @param endTime     时间戳，最小单位为秒
     * @return BaseResult
     */
    public BaseResult getServiceMessage(String accessToken, int serviceId, Integer msgType, Integer endTime) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("serviceid", serviceId);
            if (msgType != null) {
                params.put("msgtype", msgType);
            }
            if (endTime != null) {
                params.put("end_time", endTime);
            }
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_MESSAGE, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param messageId   消息id。\
     * @return BaseResult
     */
    public BaseResult getServiceMessageDetail(String accessToken, int messageId) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("messageid", messageId);
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_MESSAGE_DATA, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaseResult unifiedOrder(@NonNull UnifiedOrder orderParams) {
        return NetworkClientManagerProxy.call(Deploy.UNIFIED_ORDER, orderParams.assembleMap());
    }

    /**
     * @param accessToken 在登录授权后得到，参见如何登入。
     * @param serviceId   服务号id。
     * @param doctorId    指定医生id。
     * @param messageId   消息id。如指定则表明回复该消息
     * @param description 症状描述
     * @param attachment  附件地址，多个附件用，隔开
     * @return BaseResult
     */
    public BaseResult launchInquire(String accessToken, int serviceId, Integer doctorId, Integer messageId, @NonNull String description, String attachment) {
        JSONObject params = new JSONObject();
        try {
            params.put("access_token", accessToken);
            params.put("serviceid", serviceId);
            if (doctorId != null)
                params.put("doctorid", doctorId);
            if (messageId != null)
                params.put("messageid", messageId);
            params.put("description", description);
            if (!TextUtils.isEmpty(attachment))
                params.put("attachment", attachment);
            return NetworkClientManagerProxy.call(RESOURCE_SERVICE_MESSAGE_POST, NetworkParams.obtainBuilder().setHttpEntity(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class DNSResolver implements Runnable {
        private String domain;
        private InetAddress inetAddr;

        public DNSResolver(String domain) {
            this.domain = domain;
        }

        public void run() {
            try {
                InetAddress addr = InetAddress.getByName(domain);
                set(addr);
            } catch (UnknownHostException ignored) {
            }
        }

        public synchronized void set(InetAddress inetAddr) {
            this.inetAddr = inetAddr;
        }

        public synchronized InetAddress get() {
            return inetAddr;
        }
    }

}
