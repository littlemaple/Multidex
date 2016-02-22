package com.medzone.mcloud.data.bean.dbtable;

import android.os.Bundle;

import com.medzone.framework.util.StringUtils;
import com.medzone.framework.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hyc on 2015/12/9.
 */
public class MedicineItem {//// FIXME: 12/16/2015 如果有必要，可以与Medication类合并。
    private int medicineId;
    private String name;
    private int left;//余量
    private int totalNum;
    private int everyTime;//每次
    private int everyday;//每天
    private String unit;//单位
    private String startTime;
    private String stopTime;
    private String medicineTime;
    private String desc;
    private String isClock;
    private String url;

    public MedicineItem(int medicineId,String name, int left, int everyTime, int everyday, String unit,String startTime,String medicineTime, String desc) {
        this.medicineId = medicineId;
        this.name = name;
        this.left = left;
        this.everyTime = everyTime;
        this.everyday = everyday;
        this.unit = unit;
        this.startTime=startTime;
        this.medicineTime=medicineTime;
        this.desc = desc;
    }

    public MedicineItem(int medicineId, String name, int left, int everyTime, int everyday, String unit, String startTime, String medicineTime, String desc, String isClock) {
        this.medicineId = medicineId;
        this.name = name;
        this.left = left;
        this.everyTime = everyTime;
        this.everyday = everyday;
        this.unit = unit;
        this.startTime = startTime;
        this.medicineTime = medicineTime;
        this.desc = desc;
        this.isClock = isClock;
    }

    public MedicineItem(int medicineId, String name, int left, int everyTime, int everyday, String unit, String startTime, String medicineTime, String desc, String isClock, String url) {
        this.medicineId = medicineId;
        this.name = name;
        this.left = left;
        this.everyTime = everyTime;
        this.everyday = everyday;
        this.unit = unit;
        this.startTime = startTime;
        this.medicineTime = medicineTime;
        this.desc = desc;
        this.isClock = isClock;
        this.url = url;
    }

    public MedicineItem(int medicineId, String name, int left, int totalNum, int everyTime, int everyday, String unit, String startTime, String medicineTime, String desc, String isClock, String url) {
        this.medicineId = medicineId;
        this.name = name;
        this.left = left;
        this.totalNum = totalNum;
        this.everyTime = everyTime;
        this.everyday = everyday;
        this.unit = unit;
        this.startTime = startTime;
        this.medicineTime = medicineTime;
        this.desc = desc;
        this.isClock = isClock;
        this.url = url;
    }

    public MedicineItem(int medicineId, String name, int left, int totalNum, int everyTime, int everyday, String unit, String startTime, String stopTime, String medicineTime, String desc, String isClock, String url) {
        this.medicineId = medicineId;
        this.name = name;
        this.left = left;
        this.totalNum = totalNum;
        this.everyTime = everyTime;
        this.everyday = everyday;
        this.unit = unit;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.medicineTime = medicineTime;
        this.desc = desc;
        this.isClock = isClock;
        this.url = url;
    }

    public static ArrayList<MedicineItem> convertJsonArray(JSONArray ja) {
        ArrayList<MedicineItem> arrayList = new ArrayList<>();
        if (ja == null || ja.length() == 0) return null;
        MedicineItem item;
        try {
            for (int i = 0;i < ja.length();i++) {
                item = convertMedicineItem(ja.getJSONObject(i));
                if (item != null) {
                    arrayList.add(item);
                }
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MedicineItem convertMedicineItem(JSONObject jo) {
        if (jo == null || jo.length() == 0) return null;
        String name = "";
        int medicineId = 0;
        int left = 0;
        int totalNum = 0;
        int everytime = 0;
        int everyday = 0;
        String unit = "";
        String medicinetime="";
        String starttime= TimeUtils.getNowDate();
        String stopTime="";
        String desc = "";
        String isClock = "N";
        String url = "";
        try {
            if (jo.has("id")) {
                medicineId = jo.getInt("id");
            }
            if (jo.has("medicinename") && !jo.isNull("medicinename")) {
                name = jo.getString("medicinename");
            }
            if (jo.has("num") && !jo.isNull("num")) {
                everytime = jo.getInt("num");
            }
            if (jo.has("times") && !jo.isNull("times")) {
                everyday = jo.getInt("times");
            }
            if (jo.has("restnum") && !jo.isNull("restnum")) {
                left = jo.getInt("restnum");
            }
            if (jo.has("totalnum") && !jo.isNull("totalnum")) {
                totalNum = jo.getInt("totalnum");
            }
            if (jo.has("unit") && !jo.isNull("unit")) {
                unit = jo.getString("unit");
            }
            if(jo.has("starttime")&& !jo.isNull("starttime")){
                starttime=jo.getString("starttime");
            }
            if(jo.has("medicinetime")&& !jo.isNull("medicinetime")){
                JSONArray ja = jo.getJSONArray("medicinetime");
                medicinetime = ja.toString();
            }
            if(jo.has("description") && !jo.isNull("description")){
                desc = jo.getString("description");
            }
            if (jo.has("isclock") && !jo.isNull("isclock")) {
                isClock = jo.getString("isclock");
            }
            if (jo.has("url") && !jo.isNull("url")) {
                url = jo.getString("url");
            }
            if (jo.has("stoptime") && !jo.isNull("stoptime")) {
                stopTime = jo.getString("stoptime");
            }
            return new MedicineItem(medicineId,name,left,totalNum,everytime,everyday, unit,starttime,stopTime ,medicinetime,desc, isClock, url);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle parse(Bundle bundle,MedicineItem item) {
        Bundle b = bundle == null?new Bundle() : bundle;
        if (item.getMedicineId() != 0) {
            b.putInt(Medication.KEY_MEDICINE_ID, item.getMedicineId());
        }
        if (!StringUtils.isBlank(item.getName())) {
            b.putString(Medication.KEY_MEDICINE_NAME, item.getName());
        }
        if(item.getEveryTime() != 0){
            b.putString(Medication.KEY_MEDICINE_DOSE, item.getEveryTime() + "");
        }
        if (item.getEveryday() != 0) {
            b.putString(Medication.KEY_MEDICINE_FREQ, item.getEveryday() + "");
        }
        if(item.getLeft() >= 0){
            b.putString(Medication.KEY_MEDICINE_LEFT, item.getLeft() + "");
        }
        if (item.getTotalNum() != 0) {
            b.putString(Medication.KEY_MEDICINE_TOTAL,String.valueOf(item.getTotalNum()));
        }
        if(!StringUtils.isBlank(item.getUnit())){
            b.putString(Medication.KEY_MEDICINE_DOSE_UNIT, item.getUnit());
        }
        if(!StringUtils.isBlank(item.getStartTime())){
            b.putString(Medication.KEY_MEDICINE_START_DATE, item.getStartTime());
        }
        if(!StringUtils.isBlank(item.getMedicineTime())){
            b.putString(Medication.KEY_MEDICINE_TIME, fromJSONArray(item.getMedicineTime()));
        }
        if(!StringUtils.isBlank(item.getDesc())){
            b.putString(Medication.KEY_MEDICINE_3RD_PART_DESC, item.getDesc());
        }
        if (!StringUtils.isBlank(item.getIsClock())) {
            b.putBoolean(Medication.KEY_MEDICINE_ALARM, item.getIsClock().equals("Y"));
        }
        if (!StringUtils.isBlank(item.getUrl())) {
            b.putString(Medication.KEY_MEDICINE_URL, item.getUrl());
        }
        if (!StringUtils.isBlank(item.getStopTime())) {
            b.putBoolean(Medication.KEY_MEDICINE_STOPPED,isStopped(item));
        }
        return b;
    }

    public static Medication parse (MedicineItem item) {
        Medication medication = new Medication();
        if (item.getMedicineId() != 0) {
            medication.setMedicineID(item.getMedicineId());
        }
        if (!StringUtils.isBlank(item.getName())) {
            medication.setMedicineName(item.getName());
        }
        if (item.getLeft() != 0) {
            medication.setRestNum(item.getLeft());
        }
        if (item.getTotalNum() != 0) {
            medication.setTotalNum(item.getTotalNum());
        }
        if (item.getEveryTime() != 0) {
            medication.setNum(item.getEveryTime());
        }
        if (item.getEveryday() != 0) {
            medication.setTimes(item.getEveryday());
        }
        if (!StringUtils.isBlank(item.getUnit())) {
            medication.setUnit(item.getUnit());
        }
        if (!StringUtils.isBlank(item.getStartTime())) {
            medication.setStartTime(item.getStartTime());
        }
        if (!StringUtils.isBlank(item.getMedicineTime())) {
            medication.setMedicineTime(item.getMedicineTime());
        }
        if (!StringUtils.isBlank(item.getDesc())) {
            medication.setDescription(item.getDesc());
        }
        if (!StringUtils.isBlank(item.getUrl())) {
            medication.setUrl(item.getUrl());
        }
        if (!StringUtils.isBlank(item.getIsClock())) {
            medication.setIsClock(item.getIsClock().equals("Y"));
        }
        if (!StringUtils.isBlank(item.getStopTime())) {
            medication.setStopTime(item.getStopTime());
        }
        return medication;
    }

    public static ArrayList<Medication> convertFromMedicationItemList(ArrayList<MedicineItem> items) {
        if (items == null) {
            return null;
        }
        ArrayList<Medication> medications = new ArrayList<>();
        MedicineItem item;
        for (int i = 0;i < items.size();i++) {
            item = items.get(i);
            medications.add(parse(item));
        }
        return medications;
    }

    public static String fromJSONArray(JSONArray ja){
        String res = "";
        if (ja == null) {
            return res;
        }
        try {
            for (int i = 0;i < ja.length();i++) {
                res = res.concat(ja.getString(i));
                if (i != ja.length() - 1) {
                    res = res.concat(";");
                }
            }
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fromJSONArray(String str) {
        if (StringUtils.isBlank(str)) return "";
        JSONArray ja;
        try {
            ja = new JSONArray(str);
            return fromJSONArray(ja);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLeft() {
        return left;
    }

    public int getEveryTime() {
        return everyTime;
    }

    public void setEveryTime(int everyTime) {
        this.everyTime = everyTime;
    }

    public void setLeft(int left) {

        this.left = left;
    }

    public int getEveryday() {
        return everyday;
    }

    public void setEveryday(int everyday) {
        this.everyday = everyday;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIsClock() {
        return isClock;
    }

    public void setIsClock(String isClock) {
        this.isClock = isClock;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getStopTime() {
        return stopTime;
    }

    public static boolean isStopped(MedicineItem item) {
        return !item.getStopTime().equals("0000-00-00");
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
}
