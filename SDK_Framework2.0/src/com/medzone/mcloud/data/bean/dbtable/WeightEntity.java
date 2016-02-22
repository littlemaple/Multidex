package com.medzone.mcloud.data.bean.dbtable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Size;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class WeightEntity extends BaseMeasureData implements Parcelable {

    public static final String TAG = "weight";                // 这个值取决于服务端规则库表对不同模块的类型的定义
    public static final String NAME_FIELD_WEIGHT = "weight";
    public static final int MODULE_PERSONAL = 1;
    public static final int MOUDLE_MOMBABY = 2;
    //体重偏轻
    public static final int STATE_UNDER_WEIGHT = 1;
    //体重正常
    public static final int STATE_NORMAL_WEIGHT = 2;
    //体重偏重
    public static final int STATE_OVER_WEIGHT = 3;
    //体重肥胖
    public static final int STATE_OBESITY_WEIGHT = 5;
    //增长过缓
    public static final int STATE_PRE_UNDER_WEIGHT = 101;//之前是1
    //增长过快
    public static final int STATE_PRE_OVER_WEIGHT = 102;//之前是3
    public static final String NAME_FIELD_BMI = "BMI";
    public static final String NAME_FIELD_BMR = "BMR";
    public static final String NAME_FIELD_BODY_FAT = "bodyFat";
    public static final String NAME_FIELD_BODY_WATER = "bodyWater";
    public static final String NAME_FIELD_VISCERAL_FAT = "visceralFat";
    public static final String NAME_FIELD_MUSCLE_CONTENT = "muscleContent";
    public static final String NAME_FIELD_BONE_CONTENT = "boneContent";

    public static  String[] stateBMI = new String[]{"偏轻", "正常", "偏重", "过重","过重"};
    public static String[] stateBodyFat = new String[]{"偏瘦", "健康", "偏胖", "胖", "过胖"};
    public static  String[] stateBodyWater = new String[]{"偏低", "正常", "偏高"};
    public static String[] stateVFat = new String[]{ "正常", "偏高", "高","高"};
    public static String[] stateMuContent = new String[]{"偏低", "正常", "偏高"};
    public static String[] stateBoneContent = new String[]{"偏低", "正常", "偏高"};
    public static String[] stateBMR = new String[]{ "正常","正常","正常"};
    public static final Creator<WeightEntity> CREATOR = new Creator<WeightEntity>() {

        @Override
        public WeightEntity[] newArray(int size) {
            return new WeightEntity[size];
        }

        @Override
        public WeightEntity createFromParcel(Parcel source) {
            return new WeightEntity(source);
        }
    };
    //男性0~18岁bmi
    public static final double[] manBMI = new double[]{
            15.7, 15.7, 15.7, 15.7,
            15.3, 15.2, 15.3, 15.6,
            16.0, 16.4, 17.0, 17.5,
            18.1, 18.7, 19.2, 19.7,
            20.1, 20.5, 20.8
    };
    //女性0~18岁bmi
    public static final double[] womanBMI = new double[]{
            15.4, 15.4, 15.4, 15.4,
            15.2, 15.0, 15.0, 15.0,//4,5,6,7
            15.2, 15.6, 16.1, 16.7,//8,9,10,11
            17.4, 18.1, 18.8, 19.3,//12,13,14,15
            19.7, 20.0, 20.3

    };
    //脂肪率 水分含量 肌肉含量 bmi brm(新陈代谢率) 骨骼 内脏脂肪等级
//    public static final String[] weightChineseName = {"体脂肪率",  "肌肉含量", "体水分率","BMI", "基础代谢率", "骨量", "内脏脂肪"};
//    public static final String[] weightItemUnit = {"%", "kg", "%", "", "kcal", "kg", ""};
    /**
     *
     */
    private static final long serialVersionUID = -1681742759893501547L;
    //显示顺序和数据存入数据库的映射顺序,数组脚标为显示顺序内容为存入数据库的顺序
    //存储顺序 body_fat >muscle_content > body_water >  bmi > bmr > bone_content > visceral_fat
    //显示顺序 bmi > body_fat > muscle_content > body_water > visceral_fat > bone_content > bmr
    public static int[] showPosition = {3, 0, 2, 1, 6, 5, 4};
    //    public String[] resUnit = new String[]{"%", "kg", "%", "", "kg", "kcal"};
//                                             脂肪率 水分含量 肌肉含量 bmi brm(新陈代谢率) 骨骼 内脏脂肪等级
//    private String[] resHint = new String[]{"体脂肪率", "肌肉含量", "体内水分率", "内脏脂肪", "骨量", "基础代谢率"};
    private static String[] key_names = new String[]{NAME_FIELD_BODY_FAT, NAME_FIELD_MUSCLE_CONTENT,NAME_FIELD_BODY_WATER,  NAME_FIELD_BMI, NAME_FIELD_BMR, NAME_FIELD_BONE_CONTENT, NAME_FIELD_VISCERAL_FAT};
    private static String[] key_show_names = new String[]{NAME_FIELD_BMI,NAME_FIELD_BODY_FAT,NAME_FIELD_MUSCLE_CONTENT,NAME_FIELD_BODY_WATER,NAME_FIELD_VISCERAL_FAT,NAME_FIELD_BONE_CONTENT,NAME_FIELD_BMR};
    //体重
    @DatabaseField(columnName = NAME_FIELD_WEIGHT)
    private Float weight;
    //体脂肪率
    private Float bodyFat;
    //体内水分
    private Float bodyWater;
    //内脏脂肪
    private Float visceralFat;
    //肌肉含量
    private Float muscleContent;
    //骨骼含量
    private Float boneContent;
    //基础代谢率
    private Float BMR;
    private Float BMI;
    //1:代表个人测量体重模式
    //2:代表婴儿模式
    @DatabaseField
    private Integer weightMeasureType;
    //json格式的数据
    //此处不能讲字段名设置为 values 关键字冲突
    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] valueJson;
    private List<FactorItem> factorItemList;
    private List<FactorItem> showFactorItemList;

    public WeightEntity() {
        setTag(TAG);
    }

    private WeightEntity(Parcel source) {
        weight = source.readFloat();
        setSource(source.readString());
        setMeasureUID(source.readString());
        setStateFlag(source.readInt());
        setActionFlag(source.readInt());
        setX(source.readDouble());
        setY(source.readDouble());
        setZ(source.readDouble());
    }

    public static List<WeightEntity> createWeightEntityList(JSONArray jsonArray, Account account) {
        List<WeightEntity> retList = new ArrayList<WeightEntity>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                retList.add(createWeightEntity(jsonObj, account));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retList;
    }

    public static WeightEntity createWeightEntity(JSONObject jo, Account account) {

        WeightEntity bp = new WeightEntity();
        bp.setBelongAccount(account);
        return parse(jo, bp);
    }

    public static WeightEntity updateWeightEntity(JSONObject jo, WeightEntity bp) {

        return parse(jo, bp);
    }

    private static WeightEntity parse(JSONObject jo, WeightEntity we) {
        try {
            if (jo.has("recordid") && !jo.isNull("recordid")) {
                we.setRecordID(jo.getInt("recordid"));
            }
            if (jo.has("measureuid") && !jo.isNull("measureuid")) {

                String uid = jo.getString("measureuid");
                we.setMeasureUID(uid);

            }
            if (jo.has("source") && !jo.isNull("source")) {
                we.setSource(jo.getString("source"));
            }
            if (jo.has("readme") && !jo.isNull("readme")) {
                we.setReadme(jo.getString("readme"));
            }
            if (jo.has("x") && !jo.isNull("x")) {
                we.setX(jo.getDouble("x"));
            }
            if (jo.has("y") && !jo.isNull("y")) {
                we.setY(jo.getDouble("y"));
            }
            if (jo.has("z") && !jo.isNull("z")) {
                we.setZ(jo.getDouble("z"));
            }
            if (jo.has("state") && !jo.isNull("state")) {
                we.setAbnormal(jo.getInt("state"));
            }
            if (jo.has("uptime") && !jo.isNull("uptime")) {
                we.setUptime(jo.getLong("uptime"));
            }
            if (jo.has("value1") && !jo.isNull("value1")) {
                we.setWeight((float) jo.getDouble("value1"));
            }
            if (jo.has("values") && !jo.isNull("values")) {
                we.valueJson = jo.getString("values").getBytes("UTF-8");
            }
            if (we.valueJson == null) {
                we.setValues(new float[]{0, 0, 0, 0, 0, 0, 0}, new int[]{0, 0, 0, 0, 0, 0, 0});
            }
            we.setStateFlag(STATE_SYNCHRONIZED);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return we;
    }

    public List<FactorItem> getFactorItemList() {
        if (factorItemList == null || factorItemList.size() == 0)
            parseFactor();
        return this.factorItemList;
    }

    public List<FactorItem> getShowFactorItemList() {

        if (showFactorItemList != null)
            return showFactorItemList;

        List<FactorItem> factorItems = getFactorItemList();
        showFactorItemList = new ArrayList<>();
        for (int i = 0; i < factorItems.size(); i++) {
            showFactorItemList.add(getFactorItemByName(key_show_names[i], factorItems));
        }
        return showFactorItemList;
    }


    public FactorItem getFactorItemByName(String name,List<FactorItem> factorItems){
        for (int i = 0; i < factorItems.size(); i++) {
            FactorItem item = factorItems.get(i);
            if (name.equals(item.name)){
                return item;
            }
        }
        return null;
    }


    public String getValues() {
        try {
            return new String(valueJson, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public JSONArray getArrayValues() {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(getValues());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    /**
     * 顺序不能放错
     * body_fat > muscle_content > body_water > bmi > bmr > bone_content > visceral_fat
     *
     * @param values
     * @param states
     */
    public void setValues(@Size(value = 7) float[] values, @Size(value = 7) int[] states) {
        JSONArray ja = assembleFactor(key_names, values, states);
        if (ja != null) {
            this.valueJson = ja.toString().getBytes(Charset.defaultCharset());
        }
    }

    public void setValue(byte[] bytes) {
        this.valueJson = bytes;
    }

    private JSONArray assembleFactor(String[] names, float[] values, int[] states) {

        JSONArray ja = new JSONArray();
        if (values == null || states == null || values.length != states.length || values.length != key_names.length)
            return ja;
        for (int i = 0; i < key_names.length; i++) {
            ja.put(assembleFactorItem(key_names[i], values[i], states[i]));
        }
        return ja;
    }

    private JSONObject assembleFactorItem(String name, Float value, int state) {

        JSONObject jo = new JSONObject();
        try {
            jo.put("name", name);
            jo.put("value", value);
            jo.put("state", state);
        } catch (JSONException e) {
        }
        return jo;
    }

    private void parseFactor() {
        if (valueJson == null) {
            setValues(new float[]{0, 0, 0, 0, 0, 0, 0}, new int[]{2, 2, 2, 2, 2, 2, 2});
//            values = assembleFactor(key_names,new float[]{0,0,0,0,0,0,0},new int[]{2,2,2,2,2,2,2});
//            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(new String(valueJson, Charset.defaultCharset()));
            if (factorItemList == null)
                factorItemList = new ArrayList<>();
            factorItemList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                //TODO 移除bmi等字段
                FactorItem item = new FactorItem();
                item.name = jo.getString("name");
                item.state = Integer.valueOf(jo.getString("state"));
                if (item.state <=0)
                    item.state = 2;
                item.value = Math.round(Float.valueOf(jo.getString("value"))*10) / 10.0f;
//                item.unit = weightItemUnit[i];
//                item.cname = weightChineseName[i];
                factorItemList.add(item);
                if (NAME_FIELD_BMI.equals(item.name)) {
                    BMI = item.value;
                    item.cstate = fillState2String(stateBMI,item.state-1);
                    item.unit = "";
                    item.cname = "BMI";
                }
                if (NAME_FIELD_BODY_WATER.equals(item.name)){
                    bodyWater = item.value;
                    item.cstate = fillState2String(stateBodyWater,item.state-1);
                    item.unit = "%";
                    item.cname = "体水分率";
                }
                if (NAME_FIELD_BMR.equals(item.name)) {
                    BMR = item.value;
                    item.cstate = fillState2String(stateBMR,item.state-1);
                    item.unit = "kcal";
                    item.cname = "基础代谢率";
                }
                if (NAME_FIELD_BODY_FAT.equals(item.name)) {
                    bodyFat = item.value;
                    item.cstate = fillState2String(stateBodyFat,item.state-1);
                    item.unit = "%";
                    item.cname = "体脂肪率";
                }
                if (NAME_FIELD_BONE_CONTENT.equals(item.name)) {
                    boneContent = item.value;
                    item.cstate = fillState2String(stateBoneContent,item.state-1);
                    item.unit = "kg";
                    item.cname = "骨量";
                }
                if (NAME_FIELD_MUSCLE_CONTENT.equals(item.name)) {
                    muscleContent = item.value;
                    item.cstate = fillState2String(stateMuContent,item.state-1);
                    item.unit = "kg";
                    item.cname = "肌肉含量";
                }
                if (NAME_FIELD_VISCERAL_FAT.equals(item.name)) {
                    visceralFat = item.value;
                    item.cstate = fillState2String(stateVFat,item.state-2);
                    item.unit = "";
                    item.cname = "内脏脂肪";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fillState2String(String[]states,int state){
        try {
            return states[state];
        }catch (Exception e){
            return "";
        }
    }

    public Integer getWeightMeasureType() {
        weightMeasureType = (weightMeasureType == null ? MODULE_PERSONAL : weightMeasureType);
        return weightMeasureType;
    }

    public void setWeightMeasureType(Integer weightMeasureType) {
        this.weightMeasureType = weightMeasureType;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getBMI() {
        if (BMI == null)
            parseFactor();
        return BMI;
    }

    public Float getBodyFat() {
        if (bodyFat == null)
            parseFactor();
        return bodyFat;
    }

    public Float getBodyWater() {
        if (bodyWater == null)
            parseFactor();
        return bodyWater;
    }

    public Float getVisceralFat() {
        if (visceralFat == null)
            parseFactor();
        return visceralFat;
    }

    public Float getMuscleContent() {
        if (muscleContent == null)
            parseFactor();
        return muscleContent;
    }

    public Float getBoneContent() {
        if (boneContent == null)
            parseFactor();
        return boneContent;
    }

    public Float getBMR() {
        if (BMR == null)
            parseFactor();
        return BMR;
    }

    private boolean isValueValid(Float obj) {
        return (obj == null || obj == 0);
    }

    /**
     * 除去BMI之外其他值是否为空
     * @return
     */
    public boolean isInfoAllNUll() {

        if (isValueValid(getBodyFat()) && isValueValid(getBodyWater()) && isValueValid(getVisceralFat()) && isValueValid(getMuscleContent()) && isValueValid(getBoneContent()) && isValueValid(getBMR())) {
            return true;
        }
        return false;

    }

    @Override
    @Deprecated
    public List<Rule> getRulesCollects() {

        if (allRules == null) {
            allRules = new ArrayList<Rule>();
            float[] min1s = new float[]{75f, 45f, 0f};
            float[] max1s = new float[]{250f, 75f, 45f};
            String[] results = new String[]{"偏重", "正常", "偏轻"};
            int[] states = new int[]{1, 2, 3};
            for (int i = 0; i < results.length; i++) {
                Rule r = new Rule();
                r.setMin1(min1s[i]);
                r.setMax1(max1s[i]);
                r.setMin2(0f);
                r.setMax2(0f);
                r.setMeasureType(TAG);
                r.setResult(results[i]);

                r.setState(states[i]);
                allRules.add(r);
            }
        }
        return allRules;
    }

    @Override
    public void toMap(Map<String, String> result) {
        if (BMI != null) {
            result.put("bmi",String.valueOf(BMI));
        }
    }

    @Override
    public String getMeasureName() {
        return TAG;
    }

    @Override
    public boolean isHealthState() {
        if (getAbnormal() == null) return false;
        if (getAbnormal() == STATE_NORMAL_WEIGHT) {
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(weight != null ? weight : 0);
        dest.writeString(getSource());
        dest.writeString(getMeasureUID());
        if (getStateFlag() != null) {
            dest.writeInt(getStateFlag());
        } else {
            dest.writeInt(STATE_NOT_SYNCHRONIZED);
        }
        if (getActionFlag() != null) {
            dest.writeInt(getActionFlag());
        } else {
            dest.writeInt(ACTION_ADD_RECORD);
        }
        dest.writeDouble(getX() != null ? getX() : 0);
        dest.writeDouble(getY() != null ? getY() : 0);
        dest.writeDouble(getZ() != null ? getZ() : 0);
    }

    public static class FactorItem extends FactorItemBase<Float> {
        public String desc;
        public String unit;
        public String cstate;
    }
}