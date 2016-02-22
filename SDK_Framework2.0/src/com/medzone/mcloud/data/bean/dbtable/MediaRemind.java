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
public class MediaRemind {
    private int medicineId;
    private String name;
    private int restNumber;//余量
    private String unit;//单位
    private int times;  //每天服药次数。
    private int num;

    public MediaRemind() {
    }

    public MediaRemind(int medicineId, String name, int restnum, int num, int times, String unit) {
        this.medicineId = medicineId;
        this.name = name;
        this.restNumber = restnum;
        this.num = num;
        this.times = times;
        this.unit = unit;
    }

    public static ArrayList<MediaRemind> convertJsonArray(JSONArray ja) {
        ArrayList<MediaRemind> arrayList = new ArrayList<>();
        if (ja == null || ja.length() == 0) return null;
        MediaRemind item;
        try {
            for (int i = 0; i < ja.length(); i++) {
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

    public static MediaRemind convertMedicineItem(JSONObject jo) {
        if (jo == null || jo.length() == 0) return null;
        String name = "";
        int medicineId = 0;
        int restnum = 0;
        int num = 0;
        int times = 0;
        String unit = "";

        try {
            if (jo.has("id")) {
                medicineId = jo.getInt("id");
            }
            if (jo.has("name") && !jo.isNull("name")) {
                name = jo.getString("name");
            }
            if (jo.has("num") && !jo.isNull("num")) {
                num = jo.getInt("num");
            }
            if (jo.has("times") && !jo.isNull("times")) {
                times = jo.getInt("times");
            }
            if (jo.has("restnum") && !jo.isNull("restnum")) {
                restnum = jo.getInt("restnum");
            }
            if (jo.has("unit") && !jo.isNull("unit")) {
                unit = jo.getString("unit");
            }
            return new MediaRemind(medicineId, name, restnum, num, times, unit);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle parse(Bundle bundle, MediaRemind item) {
        Bundle b = bundle == null ? new Bundle() : bundle;
        if (item.getMedicineId() != 0) {
            b.putInt(Medication.KEY_MEDICINE_ID, item.getMedicineId());
        }
        if (!StringUtils.isBlank(item.getName())) {
            b.putString(Medication.KEY_MEDICINE_NAME, item.getName());
        }
        if (item.getNum() != 0) {
            b.putString(Medication.KEY_MEDICINE_DOSE, item.getNum() + "");
        }

        if (item.getRestNumber() >= 0) {
            b.putString(Medication.KEY_MEDICINE_LEFT, item.getRestNumber() + "");
        }

        if (!StringUtils.isBlank(item.getUnit())) {
            b.putString(Medication.KEY_MEDICINE_DOSE_UNIT, item.getUnit());
        }
        if (item.getTimes() != 0) {
            b.putString(Medication.KEY_MEDICINE_FREQ, String.valueOf(item.getTimes()));
        }
        return b;
    }

    public static MediaRemind parse(MediaRemind item) {
        MediaRemind medication = new MediaRemind();
        if (item.getMedicineId() != 0) {
            medication.setMedicineId(item.getMedicineId());
        }
        if (!StringUtils.isBlank(item.getName())) {
            medication.setName(item.getName());
        }
        if (item.getRestNumber() != 0) {
            medication.setRestNumber(item.getRestNumber());
        }

        if (item.getNum() != 0) {
            medication.setNum(item.getNum());
        }

        if (!StringUtils.isBlank(item.getUnit())) {
            medication.setUnit(item.getUnit());
        }

        if (item.getTimes() != 0) {
            medication.setTimes(item.getTimes());
        }
        return medication;
    }

    public static ArrayList<MediaRemind> convertFromMedicationItemList(ArrayList<MediaRemind> items) {
        if (items == null) {
            return null;
        }
        ArrayList<MediaRemind> medications = new ArrayList<>();
        MediaRemind item;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            medications.add(parse(item));
        }
        return medications;
    }

    public static String fromJSONArray(JSONArray ja) {
        String res = "";
        if (ja == null) {
            return res;
        }
        try {
            for (int i = 0; i < ja.length(); i++) {
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

    public int getRestNumber() {
        return restNumber;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setRestNumber(int restNumber) {
        this.restNumber = restNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
