package com.medzone.mcloud.data.bean.dbtable;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ Created by 44260 on 2015/12/14.
 */
public class Labeling extends BaseIdDatabaseContent {

    public static final String TYPE_BP = "bp";
    public static final String TYPE_OXY = "oxy";
    public static final String TYPE_BS = "bs";
    public static final String TYPE_ET = "et";
    public static final String TYPE_FH = "fh";
    public static final String TYPE_FM = "fm";
    public static final String TYPE_WEIGHT = "weight";
    public static final String TYPE_URINE = "ua";
    public static final String TYPE_UP="up";

    public static final String NAME_FIELD_TYPE = "label_type";

    @DatabaseField(columnName = NAME_FIELD_TYPE)
    private String type;
    @DatabaseField
    private String text;


    public Labeling() {
    }

    public Labeling(String type, String text) {
        this.text = text;
        this.type = type;
    }

    public static List<Labeling> create(@NonNull JSONObject jsonObject) {
        List<Labeling> res = new ArrayList<>();
        try {
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (TextUtils.isEmpty(key) || !jsonObject.has(key) && jsonObject.isNull(key))
                    continue;
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                for (int i = 0; i < jsonArray.length(); i++) {
                    res.add(new Labeling(key, jsonArray.getString(i)));
                }
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return res;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[ " + type + "," + text + " ]";
    }
}
