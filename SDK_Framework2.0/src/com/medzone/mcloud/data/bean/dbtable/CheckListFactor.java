package com.medzone.mcloud.data.bean.dbtable;

import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.IUpoadAttachment;
import com.medzone.mcloud.util.UploadUtil;
import com.umeng.fb.model.Reply;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 44260 on 2015/11/17.
 */
public class CheckListFactor extends BaseMeasureData implements IUpoadAttachment {

    public static final String TAG = "check";    // 这个值取决于服务端规则库表对不同模块的类型的定义

    //化验单暂时分这么多类型
    /**
     * RB=血常规
     */
    public static final String TYPE_RB = "RB";
    /**
     * RT=尿常规
     */
    public static final String TYPE_RT = "RT";
    /**
     * BB=血生化
     */
    public static final String TYPE_BB = "BB";
    /**
     * BCF=凝血功能
     */
    public static final String TYPE_BCF = "BCF";
    /**
     * HBS=肝炎系列
     */
    public static final String TYPE_HBS = "HBS";
    /**
     * TM=肿瘤标志物
     */
    public static final String TYPE_TM = "TM";
    /**
     * GRAD1=群体反应抗体检测（LabScreen-I-DX）
     */
    public static final String TYPE_GRAD1 = "GRAD1";
    /**
     * GRAD2=群体反应抗体检测（LabScreen-II-DX）
     */
    public static final String TYPE_GRAD2 = "GRAD2";
    /**
     * BT=血型
     */
    public static final String TYPE_BT = "BT";
    /**
     * FK=血清FK-506
     */
    public static final String TYPE_FK = "FK";
    /**
     * BGA=血气分析
     */
    public static final String TYPE_BGA = "BGA";
    /**
     * FR=粪便常规
     */
    public static final String TYPE_FR = "FR";
    /**
     * ACPD=超敏c反应蛋白测定
     */
    public static final String TYPE_ACPD = "ACPD";
    /**
     * CSA=CSA浓度
     */
    public static final String TYPE_CSA = "CSA";
    /**
     * RAPA=RAPA浓度
     */
    public static final String TYPE_RAPA = "RAPA";

    /**
     * CDU=肾彩超,移植彩超
     */
    public static final String TYPE_CDU = "CDU";

    /**
     * PTH=甲状旁腺激素
     */
    public static final String TYPE_PTH = "PTH";
    /**
     * HBV-DNA=乙肝病毒DNA
     */
    public static final String TYPE_HBV_DNA = "HBV-DNA";
    /**
     * BKV=BK病毒
     */
    public static final String TYPE_BKV = "BKV";
    /**
     * MPA=霉酚酸
     */
    public static final String TYPE_MPA = "MPA";

    /**
     * RF=肾功能
     */
    public static final String TYPE_RENAL = "RF";

    /**
     * USB=泌尿系B超
     */
    public static final String TYPE_UTB = "USB";

    /**
     * LF=肝功能
     */
    public static final String TYPE_LF = "LF";

    /**
     * NVU=颈部血管B超
     */
    public static final String TYPE_NVB = "NVU";

    /**
     * BL=血脂
     */
    public static final String TYPE_LIPID = "BL";


    public static Map<String, String> mapCheckListType = new HashMap<>();

    static {
        mapCheckListType.put(TYPE_RB, "血常规");
        mapCheckListType.put(TYPE_RT, "尿常规");
        mapCheckListType.put(TYPE_BB, "血生化全套");
        mapCheckListType.put(TYPE_BCF, "凝血功能");
        mapCheckListType.put(TYPE_HBS, "肝炎系列");
        mapCheckListType.put(TYPE_TM, "肿瘤标志物");
        mapCheckListType.put(TYPE_GRAD1, "群体反应抗体(PRAⅠ)");
        mapCheckListType.put(TYPE_GRAD2, "群体反应抗体(PRAⅡ)");
        mapCheckListType.put(TYPE_BT, "血型");
        mapCheckListType.put(TYPE_FK, "FK506浓度");
        mapCheckListType.put(TYPE_BGA, "血气分析");
        mapCheckListType.put(TYPE_FR, "粪便常规");
        mapCheckListType.put(TYPE_ACPD, "超敏C反应蛋白");
        mapCheckListType.put(TYPE_CSA, "CSA浓度");
        mapCheckListType.put(TYPE_RAPA, "Rapa浓度");
        mapCheckListType.put(TYPE_CDU, "移植彩超");
        mapCheckListType.put(TYPE_PTH, "PTH");
        mapCheckListType.put(TYPE_HBV_DNA, "乙肝病毒");
        mapCheckListType.put(TYPE_BKV, "BKV");
        mapCheckListType.put(TYPE_MPA, "霉酚酸");
        mapCheckListType.put(TYPE_RENAL, "肾功能");
        mapCheckListType.put(TYPE_UTB, "泌尿系B超");
        mapCheckListType.put(TYPE_LF, "肝功能");
        mapCheckListType.put(TYPE_NVB, "颈部血管B超");
        mapCheckListType.put(TYPE_LIPID, "血脂");

    }

    public static final String NAME_FIELD_VALUE_TYPE = "value_type";
    public static final String NAME_FIELD_VALUE_DATE = "value_date";
    public static final String NAME_FIELD_VALUE_ORIGIN = "value_origin";
    public static final String NAME_FIELD_VALUES = "values";
    public static final String NAME_FIELD_LOCALPATH = "localPath";
    public static final String NAME_FIELD_UPLOAD_FILE = "upload_file";

    @DatabaseField(columnName = NAME_FIELD_VALUES, dataType = DataType.BYTE_ARRAY)
    private byte[] valueJson;

    @DatabaseField(columnName = NAME_FIELD_VALUE_TYPE)
    private String valueType;

    @DatabaseField(columnName = NAME_FIELD_VALUE_DATE)
    private String valueDate;

    @DatabaseField(columnName = NAME_FIELD_VALUE_ORIGIN)
    private String valueOrigin;

    @DatabaseField(columnName = NAME_FIELD_LOCALPATH)
    String localPath;
    @DatabaseField(columnName = NAME_FIELD_UPLOAD_FILE)
    public String uploadFile;


    public static List<CheckListFactor> createCheckListFactorList(JSONArray ja, Account account) {
        List<CheckListFactor> resList = new ArrayList<>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                resList.add(createCheckListFactor(jo, account));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resList;
    }

    public static CheckListFactor createCheckListFactor(JSONObject jo, Account account) {
        CheckListFactor uls = new CheckListFactor();
        uls.setBelongAccount(account);
        return parse(jo, uls);
    }

    private static CheckListFactor parse(JSONObject jo, CheckListFactor clf) {
        try {
            if (jo.has("recordid") && !jo.isNull("recordid")) {
                clf.setRecordID(jo.getInt("recordid"));
            }
            if (jo.has("measureuid") && !jo.isNull("measureuid")) {

                String uid = jo.getString("measureuid");
                clf.setMeasureUID(uid);

            }
            if (jo.has("source") && !jo.isNull("source")) {
                clf.setSource(jo.getString("source"));
            }
            if (jo.has("readme") && !jo.isNull("readme")) {
                clf.setReadme(jo.getString("readme"));
            }
            if (jo.has("x") && !jo.isNull("x")) {
                clf.setX(jo.getDouble("x"));
            }
            if (jo.has("y") && !jo.isNull("y")) {
                clf.setY(jo.getDouble("y"));
            }
            if (jo.has("z") && !jo.isNull("z")) {
                clf.setZ(jo.getDouble("z"));
            }
            if (jo.has("state") && !jo.isNull("state")) {
                clf.setAbnormal(jo.getInt("state"));
            }
            if (jo.has("uptime") && !jo.isNull("uptime")) {
                clf.setUptime(jo.getLong("uptime"));
            }
            if (jo.has("values") && !jo.isNull("values")) {
                String tmp = jo.getString("values");
                clf.valueJson = tmp.getBytes();
            }
            if (jo.has("value_origin") && !jo.isNull("value_origin")) {
                clf.setValueOrigin(jo.getString("value_origin"));
            }
            if (jo.has("value_date") && !jo.isNull("value_date")) {
                clf.setValueDate(jo.getString("value_date"));
            }
            if (jo.has("value_type") && !jo.isNull("value_type")) {
                clf.setValueType(jo.getString("value_type"));
            }
            if (jo.has("value_file") && !jo.isNull("value_file")) {
                clf.setUploadFile(jo.getString("value_file"));
            }
            clf.setStateFlag(STATE_SYNCHRONIZED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return clf;
    }

    public void parseFactor() {
        if (valueJson == null) {
            throw new RuntimeException("必须先设置 valueJson的值");
        }
        try {
            JSONArray jsonArray = new JSONArray(new String(valueJson, Charset.defaultCharset()));
            JSONObject jo = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                CheckFactor cf = new CheckFactor();
                jo = jsonArray.getJSONObject(i);
                cf.converteJsonObject(jo);
                checkItemList.add(cf);
            }
        } catch (JSONException e) {
            throw new RuntimeException("必须先设置 valueJson为非JSON串");
        }
    }

    public static void parseCheckList2Json(CheckListFactor clf, List<CheckFactor> list) {

        JSONArray ja = new JSONArray();
        try {
            for (CheckFactor cf : list) {
                JSONObject jo = new JSONObject();
                jo.put(CheckFactor.NAME_FIELD_NAME, cf.name);
                jo.put(CheckFactor.NAME_FIELD_STATE, cf.state == null ? 0 : cf.state);
                jo.put(CheckFactor.NAME_FIELD_VALUE, cf.value == null ? "" : cf.value);
                jo.put(CheckFactor.NAME_FIELD_CHINESENAME, cf.cname);
                jo.put(CheckFactor.NAME_FIELD_UNIT, cf.unit);
                ja.put(jo);
            }
            clf.setValueJson(ja.toString().getBytes());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public List<CheckFactor> checkItemList = new ArrayList<>();

    public List<CheckFactor> getCheckItemList() {
        if (checkItemList != null && checkItemList.size() <= 0) {
            parseFactor();
        }
        return checkItemList;
    }


    public static class CheckFactor extends FactorItemBase<String> {
        public static final String NAME_FIELD_CHINESENAME = "chineseName";
        //单位
        public static final String NAME_FIELD_UNIT = "unit";

        /*尿常规 begins*/
        public static final String TYPE_BLD = "BLD";
        public static final String TYPE_LE = "LE";
        public static final String TYPE_PRO = "PRO";
        public static final String TYPE_BIL = "BIL";
        public static final String TYPE_KET = "KET";
        public static final String TYPE_UBG = "UBG";
        public static final String TYPE_NIT = "NIT";
        public static final String TYPE_GLU = "GLU";
        public static final String TYPE_PH = "PH";
        public static final String TYPE_SG = "SG";
        public static final String TYPE_TUR = "TUR";
        public static final String TYPE_COL = "COL";
        public static final String TYPE_RBC = "RBC";
        public static final String TYPE_LEU = "LEU";
        public static final String TYPE_SPC = "SPC";
        public static final String TYPE_COND = "COND";
        public static final String TYPE_BACT = "BACT";
        public static final String TYPE_XTAL = "XTAL";
        public static final String TYPE_CAST = "CAST";
        public static final String TYPE_MUCUS = "MUCUS";
        public static final String TYPE_YLC = "YLC";
        /*尿常规 ends*/

        /*群体反应抗体检测（PRAI）（GRAD1）/ 群体反应抗体检测（PRAII）（GRAD2）*/
        public static final String TYPE_PRA = "PRA";
        /*群体反应抗体检测（PRAI）（GRAD1）/ 群体反应抗体检测（PRAII）（GRAD2）*/

        /*血型：（BT）*/
        public static final String TYPE_ABO = "ABO";
        public static final String TYPE_RH = "RH";
        /*血型：（BT）*/

        /*肝炎系列:（HBS）*/
        public static final String TYPE_HCVAG = "HCVAG";
        public static final String TYPE_HBSAG = "HBSAG";
        public static final String TYPE_HBSAB = "HBSAB";
        public static final String TYPE_HBCAB = "HBCAB";
        public static final String TYPE_HBEAG = "HBEAG";
        public static final String TYPE_HBEAB = "HBEAB";
        public static final String TYPE_HAVIGM = "HAVIGM";
        public static final String TYPE_PRE_S1AG = "PRE-S1AG";
        public static final String TYPE_HAVAGIGM = "HAVAGIGM";
        public static final String TYPE_HDVAG = "HDVAG";
        public static final String TYPE_HDVIGG = "HDVIGG";
        public static final String TYPE_HDVIGM = "HDVIGM";
        public static final String TYPE_HEIGM = "HEIGM";
        public static final String TYPE_HEIGG = "HEIGG";
        /*肝炎系列:（HBS）*/

        /*粪便常规：（FR）*/
        public static final String TYPE_COLOR = "COLOR";
        public static final String TYPE_CHARACTER = "CHARACTER";
        public static final String TYPE_BLOOD = "BLOOD";
        public static final String TYPE_MUCUS_ = "MUCUS-";
        public static final String TYPE_PUS = "PUS";
        public static final String TYPE_DDTF = "DDTF";
        public static final String TYPE_RBC_ = "RBC1";
        public static final String TYPE_WBC = "WBC";
        public static final String TYPE_PC = "PC";
        public static final String TYPE_OB = "OB";
        /*粪便常规：（FR）*/

        /**
         * 转化结果
         *
         * @param jo
         * @throws JSONException
         */
        public void converteJsonObject(JSONObject jo) throws JSONException {
            if (jo.has(NAME_FIELD_NAME) && !jo.isNull(NAME_FIELD_NAME)) {
                name = jo.getString(NAME_FIELD_NAME);
            }
            if (jo.has(NAME_FIELD_VALUE) && !jo.isNull(NAME_FIELD_VALUE)) {
                value = jo.getString(NAME_FIELD_VALUE);
            }
            if (jo.has(NAME_FIELD_STATE) && !jo.isNull(NAME_FIELD_STATE)) {
                state = jo.getInt(NAME_FIELD_STATE);
            }
            if (jo.has(NAME_FIELD_CHINESENAME) && !jo.isNull(NAME_FIELD_CHINESENAME)) {
                cname = jo.getString(NAME_FIELD_CHINESENAME);
            }
            if (jo.has(NAME_FIELD_UNIT) && !jo.isNull(NAME_FIELD_UNIT)) {
                unit = jo.getString(NAME_FIELD_UNIT);
            }
        }

        public static List<CheckFactor> convertJsonArray(JSONArray jsonArray) {
            try {
                List<CheckFactor> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    CheckFactor cf = new CheckFactor();
                    JSONObject jo = jsonArray.getJSONObject(i);
                    cf.converteJsonObject(jo);
                    list.add(cf);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 云端返回的图片路径
     */
    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    @Override
    public boolean isHealthState() {
        return false;
    }

    @Override
    public String getMeasureName() {
        return TAG;
    }

    @Override
    public List<Rule> getRulesCollects() {
        return null;
    }

    @Override
    public void toMap(Map<String, String> result) {

    }

    public JSONArray getValueJson() {
        if (valueJson == null) {
            return new JSONArray();
        }
        try {
            JSONArray ja = new JSONArray(new String(valueJson));
            return ja;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    public void setValueJson(byte[] valueJson) {
        this.valueJson = valueJson;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public String getValueOrigin() {
        return valueOrigin;
    }

    public void setValueOrigin(String valueOrigin) {
        this.valueOrigin = valueOrigin;
    }

    public void setJsonValues(byte[] jsonValues) {
        this.valueJson = jsonValues;
    }

    public void setLocalPath(String path) {
        localPath = path;
    }

    @Override
    public String getUploadName() {
        if (TextUtils.isEmpty(localPath)) {
            Log.i(TAG, "文件名不合法：" + localPath);
            return null;
        }

        File file = new File(localPath);
        if (!file.exists()) {
            Log.e(TAG, "文件不存在");
            return null;
        }
        String fileName = UploadUtil.formatFileName(TAG + "_" + valueType, System.currentTimeMillis(), file.length(), UploadUtil.getFileSuffix(localPath));

        Log.i(TAG, "--->filePath:" + file.getName() + ",suffix:" + UploadUtil.getFileSuffix(localPath) + ",size:" + file.length() + ",uploadName:" + fileName);
        return fileName;
    }

    @Override
    public String toString() {
        return " [ Id: " + getId() + " recordID: " + getRecordID() + " " + getValueType() + mapCheckListType.get(getValueType()) + " ]\n";
    }

    @Override
    public String obtainKey() {
        return valueType;
    }
}
