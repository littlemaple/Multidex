package com.medzone.mcloud.data.bean.dbtable;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO 接入尿检相关的数据结构
 */
public class Urinalysis extends BaseMeasureData implements Parcelable {
    /**
     *
     */
    private static final long serialVersionUID = -1841600778196010376L;

    /**
     * 这个值取决于服务端newrules表对不同模块的type的定义
     */
    public static final String TAG = "ua";

    /**
     * 1.正常，2.异常
     */
    public static final int URINA_NORMAL = 1;
    public static final int URINA_ABNORMAL = 2;

    /**
     * 有11个子项
     */
    public static final int UA_ITEM_COUNT = 11;

    /**
     * 白细胞
     */
    public static final String NAME_FIELD_LEU = "LEU";
    /**
     * 亚硝酸盐
     */
    public static final String NAME_FIELD_NIT = "NIT";
    /**
     * 尿胆原
     */
    public static final String NAME_FIELD_UBG = "UBG";
    /**
     * 蛋白质
     */
    public static final String NAME_FIELD_PRO = "PRO";
    /**
     * PH值
     */
    public static final String NAME_FIELD_PH = "PH";
    /**
     * 潜血
     */
    public static final String NAME_FIELD_BLD = "BLD";
    /**
     * 尿比重
     */
    public static final String NAME_FIELD_SG = "SG";
    /**
     * 酮体
     */
    public static final String NAME_FIELD_KET = "KET";
    /**
     * 胆红素
     */
    public static final String NAME_FIELD_BIL = "BIL";
    /**
     * 葡萄糖
     */
    public static final String NAME_FIELD_GLU = "GLU";
    /**
     * 维生素C
     */
    public static final String NAME_FIELD_VC = "VC";
    public static final String[] englishName = {NAME_FIELD_LEU, NAME_FIELD_NIT, NAME_FIELD_UBG, NAME_FIELD_PRO, NAME_FIELD_PH, NAME_FIELD_BLD,
            NAME_FIELD_SG, NAME_FIELD_KET, NAME_FIELD_BIL, NAME_FIELD_GLU, NAME_FIELD_VC};

    // JSON封装字段的key

    /**
     * @author hyc
     */
    public static class FactorItem extends FactorItemBase<Integer> {
        public String info;
        public String display;
    }

    private List<FactorItem> factorItemList;
    private List<FactorItem> showFactorItemList = new ArrayList<>();

    //显示顺序和数据存入数据库的映射顺序,数组脚标为显示顺序，内容为存入数据库的顺序
    //存储顺序 LEU > NIT > UBG > PRO > PH > BLD > SG > KET > BIL > GLU > VC
    //显示顺序 LEU > NIT > UBG > PRO > PH > BLD > SG > KET > BIL > GLU > VC

    // josn数组转换成的字符串
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    byte[] valueJson;

    Integer LEU;
    Integer NIT;
    Integer UBG;
    Integer PRO;
    Integer PH;
    Integer BLD;
    Integer SG;
    Integer KET;
    Integer BIL;
    Integer GLU;
    Integer VC;

    //------------------------------------------------

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getLEU() {
        if (LEU == null)
            parseFactor();
        return LEU;
    }

    public Integer getNIT() {
        if (NIT == null)
            parseFactor();
        return NIT;
    }

    public Integer getUBG() {
        if (NIT == null)
            parseFactor();
        return UBG;
    }

    public Integer getPRO() {
        if (PRO == null)
            parseFactor();
        return PRO;
    }

    public Integer getPH() {
        if (PH == null)
            parseFactor();
        return PH;
    }


    public Integer getBLD() {
        if (BIL == null)
            parseFactor();
        return BLD;
    }


    public Integer getSG() {
        if (SG == null)
            parseFactor();
        return SG;
    }


    public Integer getKET() {
        if (KET == null)
            parseFactor();
        return KET;
    }

    public Integer getBIL() {
        if (BIL == null)
            parseFactor();
        return BIL;
    }

    public Integer getGLU() {
        if (GLU == null)
            parseFactor();
        return GLU;
    }

    public Integer getVC() {
        if (VC == null)
            parseFactor();
        return VC;
    }

    public final static String[][] displayMap = {
            {"-", "+-", "+", "++", "+++", "+++", "+++", "+++"},
            {"-", "+", "+", "+", "+", "+", "+", "+"},
            {"-", "+", "++", "+++", "+++", "+++", "+++", "+++"},
            {"-", "+-", "+", "++", "+++", "++++", "++++", "++++"},
            {"5.0", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "8.5"},
            {"-", "+-", "+", "++", "+++", "+++", "+++", "+++"},
            {"1.000", "1.005", "1.010", "1.015", "1.020", "1.025", "1.030",
                    "1.030"},
            {"-", "+-", "+", "++", "+++", "++++", "++++", "++++"},
            {"-", "+", "++", "+++", "+++", "+++", "+++", "+++"},
            {"-", "+-", "+", "++", "+++", "++++", "++++", "++++"},
            {"-", "+-", "+", "++", "+++", "+++", "+++", "+++"}};

    public final static String[][] infoMap = {
            {"0", "15", "70", "125", "500", "500", "500", "500"},
            {"NEGATIVE", "POSITIVE", "POSITIVE", "POSITIVE", "POSITIVE",
                    "POSITIVE", "POSITIVE", "POSITIVE"},
            {"3.2", "32", "64", "128", "128", "128", "128", "128"},
            {"0", "TRACE", "0.3", "1.0", "3.0", "20", "20", "20"},
            {"5.0", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "8.5"},
            {"0", "TRACE", "25", "80", "200", "200", "200", "200"},
            {"1.000", "1.005", "1.010", "1.015", "1.020", "1.025", "1.030",
                    "1.030"},
            {"0", "0.5", "1.5", "4.0", "8.0", "16.0", "16.0", "16.0"},
            {"0", "15", "50", "140", "140", "140", "140", "140"},
            {"0", "5", "15", "30", "60", "110", "110", "110"},
            {"0", "0.6", "1.4", "2.8", "5.6", "5.6", "5.6", "5.6"}};

//	public final static String[] englishName = new String[] { "LEU", "NIT",
//			"UBG", "PRO", "PH", "BLD", "SG", "KET", "BIL", "GLU", "VC" };


    public static final Map<String, String> refMappingValue = new HashMap<String, String>();
    public static final Map<String, String> refMappingName = new HashMap<>();
    public static final Map<String, String> refUnitMapping = new HashMap<>();

    static {
        refMappingValue.put("LEU", "-");
        refMappingValue.put("NIT", "-");
        refMappingValue.put("UBG", "-");
        refMappingValue.put("PRO", "-");
        refMappingValue.put("PH", "5.0~8.5");
        refMappingValue.put("BLD", "-");
        refMappingValue.put("SG", "1.005~1.025");
        refMappingValue.put("KET", "-");
        refMappingValue.put("BIL", "-");
        refMappingValue.put("GLU", "-");
        refMappingValue.put("VC", "-");
        refMappingName.put(NAME_FIELD_LEU, "白细胞");
        refMappingName.put(NAME_FIELD_NIT, "亚硝酸盐");
        refMappingName.put(NAME_FIELD_UBG, "尿胆原");
        refMappingName.put(NAME_FIELD_PRO, "蛋白质");
        refMappingName.put(NAME_FIELD_PH, "PH值");
        refMappingName.put(NAME_FIELD_BLD, "潜血");
        refMappingName.put(NAME_FIELD_SG, "尿比重");
        refMappingName.put(NAME_FIELD_KET, "酮体");
        refMappingName.put(NAME_FIELD_BIL, "胆红素");
        refMappingName.put(NAME_FIELD_GLU, "葡萄糖");
        refMappingName.put(NAME_FIELD_VC, "维生素C");

        refUnitMapping.put(NAME_FIELD_LEU, "Cells/μl");
        refUnitMapping.put(NAME_FIELD_NIT, "");
        refUnitMapping.put(NAME_FIELD_UBG, "μmol/l");
        refUnitMapping.put(NAME_FIELD_PRO, "g/l");
        refUnitMapping.put(NAME_FIELD_PH, "");
        refUnitMapping.put(NAME_FIELD_BLD, "Cells/μl");
        refUnitMapping.put(NAME_FIELD_SG, "");
        refUnitMapping.put(NAME_FIELD_KET, "mmol/l");
        refUnitMapping.put(NAME_FIELD_BIL, "μmol/l");
        refUnitMapping.put(NAME_FIELD_GLU, "mmol/l");
        refUnitMapping.put(NAME_FIELD_VC, "mmol/l");


    }

    public final static String[] unitMap = {"Cells/μl", "", "μmol/l", "g/l",
            "", "Cells/μl", "", "mmol/l", "μmol/l", "mmol/l", "mmol/l"};

    public String toString() {
        parseFactor();
        String result = "\nLEU =" + displayMap[0][LEU] + "(" + infoMap[0][LEU]
                + unitMap[0] + ");\nNIT=" + displayMap[1][NIT] + "("
                + infoMap[1][NIT] + unitMap[1] + ");\nUBG="
                + displayMap[2][UBG] + "(" + infoMap[2][UBG] + unitMap[2]
                + ");\nPRO=" + displayMap[3][PRO] + "(" + infoMap[3][PRO]
                + unitMap[3] + ");\nPH=" + displayMap[4][PH] + "("
                + infoMap[4][PH] + unitMap[4] + ");\nBLD=" + displayMap[5][BLD]
                + "(" + infoMap[5][BLD] + unitMap[5] + ");\nSG="
                + displayMap[6][SG] + "(" + infoMap[6][SG] + unitMap[6]
                + ");\nKET=" + displayMap[7][KET] + "(" + infoMap[7][KET]
                + unitMap[7] + ");\nBIL=" + displayMap[8][BIL] + "("
                + infoMap[8][BIL] + unitMap[8] + ");\nGLU="
                + displayMap[9][GLU] + "(" + infoMap[9][GLU] + unitMap[9]
                + ");\nVC=" + displayMap[10][VC] + "(" + infoMap[10][VC]
                + unitMap[10] + ")";
        return result;
    }

    public Urinalysis() {
        setTag(TAG);
    }

    public void setValue(byte[] arr) {
        this.valueJson = arr;
    }

    public JSONArray getValueJSONArray() {
        try {
            return new JSONArray(new String(valueJson));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<FactorItem> getFactorItemList() {
        if (factorItemList == null || factorItemList.size() == 0)
            parseFactor();
        return this.factorItemList;
    }


    public static final String[] refShow = new String[]{NAME_FIELD_PRO, NAME_FIELD_GLU, NAME_FIELD_KET, NAME_FIELD_SG, NAME_FIELD_LEU, NAME_FIELD_BLD,
            NAME_FIELD_BIL, NAME_FIELD_UBG, NAME_FIELD_PH, NAME_FIELD_VC, NAME_FIELD_NIT};

    public List<FactorItem> getShowItemList() {
        if (showFactorItemList != null && showFactorItemList.size() > 0)
            return showFactorItemList;
        List<FactorItem> items = getFactorItemList();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            showFactorItemList.add(fillShowItem(refShow[i], items));
        }
//		items.clear();
        factorItemList.clear();
        return showFactorItemList;
    }

    //对取出的数据进行排序（异常的在前面正常的后面）

    private List<FactorItem> abnormalList;

    /**
     * 异常的排在前面
     *
     * @return
     * 返回值为异常排序在前正常在后（返回集合大小为11项）
     */
    public List<FactorItem> getShowAbnormalList() {
        if (abnormalList != null && abnormalList.size() > 0)
            return abnormalList;
        abnormalList = new ArrayList<>();
        List<FactorItem> items = getShowItemList();
        int size = items.size();
        for (int i = 0; i < size; i++) {
            if (items.get(i).state == URINA_ABNORMAL) {
                abnormalList.add(items.get(i));
            }
        }
        for (int i = 0; i < size; i++) {
            if (items.get(i).state == URINA_NORMAL) {
                abnormalList.add(items.get(i));
            }
        }
        showFactorItemList.clear();
        return abnormalList;

    }


    private FactorItem fillShowItem(String name, List<FactorItem> items) {
        for (int i = 0; i < items.size(); i++) {
            FactorItem item = items.get(i);
            if (name.equals(item.name)) {
                return item;
            }
        }
        return null;
    }


    private void parseFactor() {

        if (valueJson == null) {
            throw new RuntimeException("必须先设置 valueJson的值");
        }
        try {
            JSONArray jsonArray = new JSONArray(new String(valueJson, Charset.defaultCharset()));
            if (factorItemList == null)
                factorItemList = new ArrayList<>();
            factorItemList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                FactorItem item = new FactorItem();
                item.name = jo.getString(FactorItem.NAME_FIELD_NAME);
                item.state = jo.getInt(FactorItem.NAME_FIELD_STATE);
                item.value = jo.getInt(FactorItem.NAME_FIELD_VALUE);
                factorItemList.add(item);
                if (NAME_FIELD_LEU.equals(item.name)) {
                    if (item.value > 4) item.value = 4;
                    LEU = item.value;
                    fillItemValue(item, NAME_FIELD_LEU, displayMap[0][item.value]);

                } else if (NAME_FIELD_NIT.equals(item.name)) {
                    if (item.value > 1) item.value = 1;
                    NIT = item.value;
                    fillItemValue(item, NAME_FIELD_NIT, displayMap[1][item.value]);
                } else if (NAME_FIELD_UBG.equals(item.name)) {
                    if (item.value > 3) item.value = 3;
                    UBG = item.value;
                    fillItemValue(item, NAME_FIELD_UBG, displayMap[2][item.value]);
                } else if (NAME_FIELD_PRO.equals(item.name)) {
                    if (item.value > 5) item.value = 5;
                    PRO = item.value;
                    fillItemValue(item, NAME_FIELD_PRO, displayMap[3][item.value]);
                } else if (NAME_FIELD_PH.equals(item.name)) {
                    PH = item.value;
                    int tmp = displayMap[4].length;
                    if (PH == tmp - 1) {
                        PH = tmp - 2;
                        item.value = PH;
                    }
                    fillItemValue(item, NAME_FIELD_PH, displayMap[4][item.value]);
                } else if (NAME_FIELD_BLD.equals(item.name)) {
                    if (item.value > 4) item.value = 4;
                    BLD = item.value;
                    fillItemValue(item, NAME_FIELD_BLD, displayMap[5][item.value]);
                } else if (NAME_FIELD_SG.equals(item.name)) {
                    SG = item.value;
                    fillItemValue(item, NAME_FIELD_SG, displayMap[6][item.value]);
                } else if (NAME_FIELD_KET.equals(item.name)) {
                    if (item.value > 5) item.value = 5;
                    KET = item.value;
                    fillItemValue(item, NAME_FIELD_KET, displayMap[7][item.value]);
                } else if (NAME_FIELD_BIL.equals(item.name)) {
                    if (item.value > 3) item.value = 3;
                    BIL = item.value;
                    fillItemValue(item, NAME_FIELD_BIL, displayMap[8][item.value]);
                } else if (NAME_FIELD_GLU.equals(item.name)) {
                    if (item.value > 5) item.value = 5;
                    GLU = item.value;
                    fillItemValue(item, NAME_FIELD_GLU, displayMap[9][item.value]);
                } else if (NAME_FIELD_VC.equals(item.name)) {
                    if (item.value > 4) item.value = 4;
                    VC = item.value;
                    fillItemValue(item, NAME_FIELD_VC, displayMap[10][item.value]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillItemValue(FactorItem item, String name, String display) {
        item.cname = refMappingName.get(name);
        item.display = display;
        item.unit = refUnitMapping.get(name);
    }


    public void setValueJson(int[] values, int[] states) {
        JSONArray ja = convert2JSONArray(values, states);
        if (ja != null) {
            this.valueJson = ja.toString().getBytes(Charset.defaultCharset());
        }
    }

    /**
     * 创建保存到数据库时用到
     *
     * @return
     */
    private JSONArray convert2JSONArray(int[] result, int[] states) {
        JSONArray ja = new JSONArray();
        // 先设置默认为正常，在判断过程中如果有一场就会判断成为异常
//		this.setAbnormal(states[]);
        for (int i = 0; i < result.length; i++) {
            ja.put(convert2JSONObject(englishName[i], result[i], states[i]));
        }
        return ja;
    }

    /**
     * 将数据按照一定规则封装成jsonobject
     *
     * @param position
     * @return
     */
    private JSONObject convert2JSONObject(String name, int position, int state) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("value", position);
            jo.put("name", name);
            //TODO 规则库需要重新填写
            jo.put("state", state);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jo;
    }

    @Override
    public List<Rule> getRulesCollects() {

        return allRules;
    }

    public static List<Urinalysis> createUrinalysisList(JSONArray ja,
                                                        Account account) {
        List<Urinalysis> resList = new ArrayList<>();
        try {
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                resList.add(createUrinalysis(jo, account));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resList;
    }

    public static Urinalysis createUrinalysis(JSONObject jo, Account account) {
        Urinalysis uls = new Urinalysis();
        uls.setBelongAccount(account);
        return parse(jo, uls);
    }

    @Override
    public void toMap(Map<String, String> result) {

//		result.put("PRO",String.valueOf(PRO));
//		result.put("LEU",String.valueOf(LEU));
//		result.put("NIT",String.valueOf(NIT));
//		result.put("UBG",String.valueOf(UBG));
//		result.put("PH",String.valueOf(PH));
//		result.put("BLD",String.valueOf(BLD));
//		result.put("SG",String.valueOf(SG));
//		result.put("KET",String.valueOf(KET));
//		result.put("BIL",String.valueOf(BIL));
//		result.put("GLU",String.valueOf(GLU));
//		result.put("VC",String.valueOf(VC));

    }

    private static Urinalysis parse(JSONObject jo, Urinalysis uls) {
        try {
            if (jo.has("recordid") && !jo.isNull("recordid")) {
                uls.setRecordID(jo.getInt("recordid"));
            }
            if (jo.has("measureuid") && !jo.isNull("measureuid")) {

                String uid = jo.getString("measureuid");
                uls.setMeasureUID(uid);

            }
            if (jo.has("source") && !jo.isNull("source")) {
                uls.setSource(jo.getString("source"));
            }
            if (jo.has("readme") && !jo.isNull("readme")) {
                uls.setReadme(jo.getString("readme"));
            }
            if (jo.has("x") && !jo.isNull("x")) {
                uls.setX(jo.getDouble("x"));
            }
            if (jo.has("y") && !jo.isNull("y")) {
                uls.setY(jo.getDouble("y"));
            }
            if (jo.has("z") && !jo.isNull("z")) {
                uls.setZ(jo.getDouble("z"));
            }
            if (jo.has("state") && !jo.isNull("state")) {
                uls.setAbnormal(jo.getInt("state"));
            }
            if (jo.has("uptime") && !jo.isNull("uptime")) {
                uls.setUptime(jo.getLong("uptime"));
            }
            if (jo.has("values") && !jo.isNull("values")) {
                JSONArray ja = jo.getJSONArray("values");

                uls.valueJson = ja.toString().getBytes();
//				 JSONArray2Uls(ja, uls);
            }

            uls.setStateFlag(STATE_SYNCHRONIZED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return uls;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//		dest.writeLong(getMeasureTime() != null ? getMeasureTime() : 0L);
//		dest.writeString(getMeasureTimeHelp());
        dest.writeString(getSource());
        dest.writeString(getMeasureUID());
//		dest.writeString(getMeasureTimeHelp());
        if (getStateFlag() != null) {
            dest.writeInt(getStateFlag());
        } else {
            dest.writeInt(Urinalysis.STATE_NOT_SYNCHRONIZED);
        }
        if (getActionFlag() != null) {
            dest.writeInt(getActionFlag());
        } else {
            dest.writeInt(Urinalysis.ACTION_ADD_RECORD);
        }
        dest.writeDouble(getX() != null ? getX() : 0);
        dest.writeDouble(getY() != null ? getY() : 0);
        dest.writeDouble(getZ() != null ? getZ() : 0);
    }

    private Urinalysis(Parcel source) {

//		setMeasureTime(source.readLong());
//		setMeasureTimeHelp(source.readString());
        setSource(source.readString());
        setMeasureUID(source.readString());
//		setMeasureTimeHelp(getMeasureTimeHelp());
        setStateFlag(source.readInt());
        setActionFlag(source.readInt());
        setX(source.readDouble());
        setY(source.readDouble());
        setZ(source.readDouble());
    }

    public static final Creator<Urinalysis> CREATOR = new Creator<Urinalysis>() {

        @Override
        public Urinalysis[] newArray(int size) {
            return new Urinalysis[size];
        }

        @Override
        public Urinalysis createFromParcel(Parcel source) {
            return new Urinalysis(source);
        }
    };

    @Override
    public boolean isHealthState() {
        return getAbnormal() == URINA_NORMAL;
    }

    @Override
    public String getMeasureName() {
        return TAG;
    }
}
