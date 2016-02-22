package com.medzone.mcloud.data.bean.dbtable;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BasePagingContent;
import com.medzone.framework.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hyc on 2015/12/8.
 */
public class Medication extends BasePagingContent implements Serializable, Parcelable {
    public static final String TAG = "medicine";

    public static final String KEY_MEDICINE_ID = "medicine_id";
    public static final String KEY_MEDICINE_NAME = "medicine_name";
    public static final String KEY_MEDICINE_DOSE = "medicine_dose";
    public static final String KEY_MEDICINE_DOSE_UNIT = "medicine_dose_unit";
    public static final String KEY_MEDICINE_DOSE_UNIT_SET = "medicine_dose_unit_set";
    public static final String KEY_MEDICINE_FREQ = "medicine_frequency";
    public static final String KEY_MEDICINE_TIME = "medicine_time";
    public static final String KEY_MEDICINE_ALARM = "medicine_alarm";
    public static final String KEY_MEDICINE_LEFT = "medicine_left";
    public static final String KEY_MEDICINE_TOTAL = "medicine_total";
    public static final String KEY_MEDICINE_START_DATE = "medicine_start_date";
    public static final String KEY_MEDICINE_3RD_PART_DRUG_ID = "medicine_drug_id";
    public static final String KEY_MEDICINE_3RD_PART_DESC = "medicine_3rd_part_desc";
    public static final String KEY_MEDICINE_URL = "medicine_url";
    public static final String KEY_MEDICINE_STOPPED = "medicine_stopped";
    public static final String KEY_MEDICINE_SUB_HISTORY = "medicine_sub_history";

    @DatabaseField
    private int medicineID;                 //药品id，即云端id(区分本地记录的id（在父类里面）)

    @DatabaseField
    private String medicineName;            //药品名称。

    @DatabaseField
    private float num;                      //每次服药量。

    @DatabaseField
    private int times;                      //每天服药次数。

    @DatabaseField
    private float totalNum;                 //药品总量。

    @DatabaseField
    private float restNum;                  //药品余量。

    @DatabaseField
    private boolean isClock;                //是否开启闹钟。

    @DatabaseField
    private int addnum;                     //增加配药量

    @DatabaseField
    private String medicineTime;            //如开启闹钟，服药的时间。

    @DatabaseField
    private String unit;                    //药品单位。

    @DatabaseField
    private int days;                       //药品使用时间。

    @DatabaseField
    private int drugId;                    //第三方库药品ID。

    @DatabaseField
    private String description;             //第三方库药品描述

    @DatabaseField
    private String img;

    @DatabaseField
    private String url;                     //药品使用说明地址。

    @DatabaseField
    private String startTime;               //开始时间。

    @DatabaseField
    private String upTime;                  //更新时间。

    @DatabaseField
    private String stopTime;                //停药时间。

    private Medication(Parcel parcel) {
        setMedicineID(parcel.readInt());
        setMedicineName(parcel.readString());
        setNum(parcel.readFloat());
        setTimes(parcel.readInt());
        setTotalNum(parcel.readFloat());
        setRestNum(parcel.readFloat());
        setIsClock(parcel.readInt() == 0 ? false : true);
        setAddnum(parcel.readInt());
        setMedicineTime(parcel.readString());
        setUnit(parcel.readString());
        setDays(parcel.readInt());
        setDrugId(parcel.readInt());
        setDescription(parcel.readString());
        setImg(parcel.readString());
        setUrl(parcel.readString());
        setStartTime(parcel.readString());
        setUpTime(parcel.readString());
        setStopTime(parcel.readString());
    }

    public Medication() {
    }

    public static List<Medication> createMedicationList(JSONArray jsonArray, Account account) {
        List<Medication> retList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                retList.add(createMedication(jsonObj, account));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retList;
    }

    public static Medication createMedication(JSONObject jo, Account account) {
        Medication m = new Medication();
        m.setBelongAccount(account);
        return parse(jo, m);
    }

    public static Medication parse(JSONObject jo, Medication medication) {
        try {
            if (jo.has("id") && !jo.isNull("id")) {
                medication.setMedicineID(jo.getInt("id"));
            }
            if (jo.has("medicinename") && !jo.isNull("medicinename")) {
                medication.setMedicineName(jo.getString("medicinename"));
            }
            if (jo.has("num") && !jo.isNull("num")) {
                medication.setNum(jo.getInt("num"));
            }
            if (jo.has("times") && !jo.isNull("times")) {
                medication.setTimes(jo.getInt("times"));
            }
            if (jo.has("totalnum") && !jo.isNull("totalnum")) {
                medication.setTotalNum(Double.valueOf(jo.getDouble("totalnum")).floatValue());
            }
            if (jo.has("restnum") && !jo.isNull("restnum")) {
                medication.setRestNum(Double.valueOf(jo.getDouble("restnum")).floatValue());
            }
            if (jo.has("isclock") && !jo.isNull("isclock")) {
                medication.setIsClock(jo.getString("isclock").equals("Y") || jo.getString("isclock").equals("y"));
            }
            if (jo.has("addnum") && !jo.isNull("addnum")) {
                medication.setAddnum(jo.getInt("addnum"));
            }
            if (jo.has("medicinetime") && !jo.isNull("medicinetime")) {
                JSONArray ja = jo.getJSONArray("medicinetime");
                medication.setMedicineTime(ja.toString());
            }
            if (jo.has("unit") && !jo.isNull("unit")) {
                medication.setUnit(jo.getString("unit"));
            }
            if (jo.has("days") && !jo.isNull("days")) {
                medication.setDays(jo.getInt("days"));
            }
            if (jo.has("drugid") && !jo.isNull("drugid")) {
                medication.setDrugId(jo.getInt("drugid"));
            }
            if (jo.has("description") && !jo.isNull("description")) {
                medication.setDescription(jo.getString("description"));
            }
            if (jo.has("img") && !jo.isNull("img")) {
                medication.setImg(jo.getString("img"));
            }
            if (jo.has("url") && !jo.isNull("url")) {
                medication.setUrl(jo.getString("url"));
            }
            if (jo.has("starttime") && !jo.isNull("starttime")) {
                medication.setStartTime(jo.getString("starttime"));
            }
            if (jo.has("uptime") && !jo.isNull("uptime")) {
                medication.setUpTime(jo.getString("uptime"));
            }
            if (jo.has("stoptime") && !jo.isNull("stoptime")) {
                medication.setStopTime(jo.getString("stoptime"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        medication.setStateFlag(STATE_SYNCHRONIZED);
        return medication;
    }

    public static Medication parse(Bundle bundle, Medication medication) {
        if (bundle.containsKey(KEY_MEDICINE_ID)) {
            medication.setMedicineID(bundle.getInt(KEY_MEDICINE_ID));
        }
        if (bundle.containsKey(KEY_MEDICINE_NAME)) {
            medication.setMedicineName(bundle.getString(KEY_MEDICINE_NAME));
        }
        if (bundle.containsKey(KEY_MEDICINE_DOSE)) {
            medication.setNum(Float.parseFloat(bundle.getString(KEY_MEDICINE_DOSE)));
        }
        if (bundle.containsKey(KEY_MEDICINE_DOSE_UNIT)) {
            medication.setUnit(bundle.getString(KEY_MEDICINE_DOSE_UNIT));
        }
        if (bundle.containsKey(KEY_MEDICINE_FREQ)) {
            medication.setTimes(Integer.parseInt(bundle.getString(KEY_MEDICINE_FREQ)));
        }
        if (bundle.containsKey(KEY_MEDICINE_TIME)) {
            medication.setMedicineTime(convertFromString(bundle.getString(KEY_MEDICINE_TIME)).toString());
        }
        if (bundle.containsKey(KEY_MEDICINE_ALARM)) {
            medication.setIsClock(bundle.getBoolean(KEY_MEDICINE_ALARM));
        }
        if (bundle.containsKey(KEY_MEDICINE_LEFT)) {
            medication.setAddnum((int) Float.parseFloat(bundle.getString(KEY_MEDICINE_LEFT)));
        }
        if (bundle.containsKey(KEY_MEDICINE_TOTAL)) {
            medication.setTotalNum(Integer.parseInt(bundle.getString(KEY_MEDICINE_TOTAL)));
        }
        if (bundle.containsKey(KEY_MEDICINE_START_DATE)) {
            medication.setStartTime(bundle.getString(KEY_MEDICINE_START_DATE));
        }
        if (bundle.containsKey(KEY_MEDICINE_3RD_PART_DRUG_ID)) {
            medication.setDrugId(bundle.getInt(KEY_MEDICINE_3RD_PART_DRUG_ID));
        }
        if (bundle.containsKey(KEY_MEDICINE_3RD_PART_DESC)) {
            medication.setDescription(bundle.getString(KEY_MEDICINE_3RD_PART_DESC));
        }
        if (bundle.containsKey(KEY_MEDICINE_URL)) {
            medication.setUrl(bundle.getString(KEY_MEDICINE_URL));
        }
        return medication;
    }

    public static Bundle parse(@NonNull Medication medication, @Nullable Bundle bundle) {
        Bundle b = bundle == null ? new Bundle() : bundle;
        if (medication.getMedicineID() != 0) {
            b.putInt(KEY_MEDICINE_ID, medication.getMedicineID());
        }
        if (!StringUtils.isBlank(medication.getMedicineName())) {
            b.putString(KEY_MEDICINE_NAME, medication.getMedicineName());
        }
        if (medication.getNum() != 0) {
            b.putString(KEY_MEDICINE_DOSE, String.valueOf(Float.valueOf(medication.getNum()).intValue()));
        }
        if (!StringUtils.isBlank(medication.getUnit())) {
            b.putString(KEY_MEDICINE_DOSE_UNIT, medication.getUnit());
        }
        if (medication.getTimes() != 0) {
            b.putString(KEY_MEDICINE_FREQ, String.valueOf(medication.getTimes()));
        }
        if (!StringUtils.isBlank(medication.getMedicineTime())) {
            b.putString(KEY_MEDICINE_TIME, MedicineItem.fromJSONArray(medication.getMedicineTime()));
        }
        b.putBoolean(KEY_MEDICINE_ALARM, medication.isClock());
        if (medication.getRestNum() != 0) {
            b.putString(KEY_MEDICINE_LEFT, String.valueOf(Float.valueOf(medication.getRestNum()).intValue()));
        } else {
            b.putString(KEY_MEDICINE_LEFT, "0");
        }
        if (medication.getTotalNum() != 0) {
            b.putString(KEY_MEDICINE_TOTAL, String.valueOf(Float.valueOf(medication.getTotalNum()).intValue()));
        }
        if (!StringUtils.isBlank(medication.getStartTime())) {
            b.putString(KEY_MEDICINE_START_DATE, medication.getStartTime());
        }
        if (medication.getDrugId() != 0) {
            b.putInt(KEY_MEDICINE_3RD_PART_DRUG_ID, medication.getDrugId());
        }
        if (!StringUtils.isBlank(medication.getDescription())) {
            b.putString(KEY_MEDICINE_3RD_PART_DESC, medication.getDescription());
        }
        if (!StringUtils.isBlank(medication.getUrl())) {
            b.putString(KEY_MEDICINE_URL, medication.getUrl());
        }
        if (!StringUtils.isBlank(medication.getStopTime())) {
            b.putBoolean(KEY_MEDICINE_STOPPED, isStopped(medication));
        }
        return b;
    }

    public static Map<String, ArrayList<Medication>> convertMedicationHistoryJSONObj(JSONObject jo) {
        if (jo == null || jo.length() == 0) return null;
        Map<String, ArrayList<Medication>> map = new HashMap<>();
        ArrayList<Medication> list;
        String key;
        Iterator<String> keys = jo.keys();
        try {
            while (keys.hasNext()) {
                key = keys.next();
                JSONArray ja = jo.getJSONArray(key);
                list = convertMedicationSubHistoryJSONArray(ja);
                map.put(key, list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public static ArrayList<ArrayList<Medication>> convertMedicationHistoryJSONObject(JSONObject jo) {
        if (jo == null) return null;
        if (!jo.has("root")) return null;
        ArrayList<ArrayList<Medication>> medicationListList = new ArrayList<>();
        ArrayList<Medication> medicationList;
        Medication medication;
        JSONArray jsonArray, jsonArray1;
        JSONObject jsonObject;
        try {
            jsonArray = jo.getJSONArray("root");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonArray1 = jsonArray.getJSONArray(i);
                medicationList = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    jsonObject = jsonArray1.getJSONObject(j);
                    medication = new Medication();
                    medication = parse(jsonObject, medication);
                    medicationList.add(medication);
                }
                medicationListList.add(medicationList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return medicationListList;
    }

    public static ArrayList<Medication> convertMedicationHistory(ArrayList<ArrayList<Medication>> lists) {
        if (lists == null) return null;
        ArrayList<Medication> medicationArrayList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            medicationArrayList.add(lists.get(i).get(0));
        }
        return medicationArrayList;
    }

    public static ArrayList<Medication> convertMedicationSubHistory(String medication, ArrayList<ArrayList<Medication>> lists) {
        if (medication == null || lists == null) return null;
        ArrayList<Medication> medications = null;
        for (int i = 0; i < lists.size(); i++) {
            medications = lists.get(i);
            if (medications.size() == 0) continue;
            if (medication.equalsIgnoreCase(medications.get(0).getMedicineName().trim())) {
                break;
            }
        }
        return medications;
    }

    public static ArrayList<Medication> convertMedicationSubHistoryJSONArray(JSONArray ja) {
        ArrayList<Medication> list = new ArrayList<>();
        Medication medication;
        JSONObject jo;
        try {
            for (int i = 0; i < ja.length(); i++) {
                medication = new Medication();
                jo = ja.getJSONObject(i);
                medication = parse(jo, medication);
                list.add(medication);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    /**
     * @param arg 以分号隔开的字符串
     */
    public static JSONArray convertFromString(@NonNull String arg) {
        JSONArray ja = new JSONArray();
        String[] arg1 = arg.split(";");
        for (String arg2 : arg1) {
            ja.put(arg2);
        }
        return ja;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getMedicineID());
        parcel.writeString(getMedicineName());
        parcel.writeFloat(getNum());
        parcel.writeInt(getTimes());
        parcel.writeFloat(getTotalNum());
        parcel.writeFloat(getRestNum());
        parcel.writeInt(isClock() ? 1 : 0);
        parcel.writeInt(getAddnum());
        parcel.writeString(getMedicineTime());
        parcel.writeString(getUnit());
        parcel.writeInt(getDays());
        parcel.writeInt(getDrugId());
        parcel.writeString(getDescription());
        parcel.writeString(getImg());
        parcel.writeString(getUrl());
        parcel.writeString(getStartTime());
        parcel.writeString(getUpTime());
        parcel.writeString(getStopTime());
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }

        @Override
        public Medication createFromParcel(Parcel source) {
            return new Medication(source);
        }
    };

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public float getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(float totalNum) {
        this.totalNum = totalNum;
    }

    public float getRestNum() {
        return restNum;
    }

    public void setRestNum(float restNum) {
        this.restNum = restNum;
    }

    public boolean isClock() {
        return isClock;
    }

    public void setIsClock(boolean isClock) {
        this.isClock = isClock;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }

    public boolean isStopped(){
        return !stopTime.equals("0000-00-00");
    }

    public static boolean isStopped(Medication medication) {
        return !medication.getStopTime().equals("0000-00-00");
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getAddnum() {
        return addnum;
    }

    public void setAddnum(int addnum) {
        this.addnum = addnum;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(int medicineID) {
        this.medicineID = medicineID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
