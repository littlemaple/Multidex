package com.medzone.mcloud.data.bean.dbtable;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by asus on 2015/11/27.
 */
public class RuleItem {
    public static final String KEY_CONDS = "conds";
    public static final String KEY_STATE = "state";
    public static final String KEY_RESULT = "result";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMG_URL = "imageUrl";
    public static final String KEY_IMG_URL_X = "imageUrlX";

    public String value1;
    public Integer state;
    public String result;
    public String description;
    public String imageUrl;
    public String imageUrlX;
    public ArrayList<ArrayList<String[]>> condition;

    public RuleItem(String value1, Integer state, String result, String description, String imageUrl, String imageUrlX) {
        this.value1 = value1;
        this.state = state;
        this.result = result;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageUrlX = imageUrlX;
    }

    public void parse(){
        ArrayList<ArrayList<String[]>> conds = new ArrayList<>();
        ArrayList<String[]> condChildren;
        String[] map;
        JSONArray ja;
        JSONObject jo;
        Iterator<String> keyIterator;

        try {
            if(condition == null && value1 != null && !TextUtils.isEmpty(value1)){
                ja = new JSONArray(value1);
                if (ja.length() != 0) {
                    for (int i = 0;i < ja.length();i++) {
                        condChildren = new ArrayList<>();
                        jo = ja.getJSONObject(i);
                        keyIterator = jo.keys();
                        while (keyIterator.hasNext()) {
                            map = new String[2];
                            map[0] = keyIterator.next();
                            map[1] = jo.getString(map[0]);
                            condChildren.add(map);
                        }
                        conds.add(condChildren);
                    }
                }
                this.condition = conds;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.condition = null;
        }
    }

    public String getImageUrlX() {
        return imageUrlX;
    }

    public String getValue1() {
        return value1;
    }

    public Integer getState() {
        return state;
    }

    public String getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 此方法所返回的数据是固定格式的，不建议修改。
     */
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put(KEY_CONDS, value1);
            json.put(KEY_STATE, state);
            json.put(KEY_RESULT, result);
            json.put(KEY_DESCRIPTION, description);
            json.put(KEY_IMG_URL, imageUrl);
            json.put(KEY_IMG_URL_X, imageUrlX);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
