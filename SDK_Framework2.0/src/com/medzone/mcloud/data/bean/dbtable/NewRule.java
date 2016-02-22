package com.medzone.mcloud.data.bean.dbtable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新增的各模块判断规则库
 * @author hyc
 */
@DatabaseTable(tableName = "newRule")
public class NewRule extends BaseIdDatabaseObject {
    /**
     * 测量类型字段名
     */
    public static final String MEASURE_TYPE = "measure_type";

    /**
     * 测量模块的判定规则
     */
    public static final String MEASURE_RULE = "measure_rule";

    @DatabaseField(columnName = MEASURE_TYPE)
    private String measureType;

    @DatabaseField(columnName = MEASURE_RULE)
    private String measureRule;

    public Map<String,List<RuleItem>> parse(){
        Map<String,List<RuleItem>> map = new HashMap<>();
        List<RuleItem> items = parse(this.getMeasureRule());
        map.put(this.getMeasureType(),items);
        return map;
    }

    private List<RuleItem> parse(String jsonArrayStr){
        if (jsonArrayStr == null) return null;
        JSONObject jo;
        JSONArray ja;
        RuleItem item;
        List<RuleItem> items = new ArrayList<>();
        int state = -1;
        String result = "";
        String desc = "";
        String imgUrl = "";
        String imgUrlX = "";
        String value1 = "";

        try {
            ja = new JSONArray(jsonArrayStr);
            if (ja != null && ja.length() != 0) {
                for (int i = 0;i < ja.length();i++) {
                    jo = ja.getJSONObject(i);
                    if (jo.has(RuleItem.KEY_STATE)) {
                        state = jo.getInt(RuleItem.KEY_STATE);
                    }
                    if (jo.has(RuleItem.KEY_RESULT)) {
                        result = jo.getString(RuleItem.KEY_RESULT);
                    }
                    if (jo.has(RuleItem.KEY_DESCRIPTION)) {
                        desc = jo.getString(RuleItem.KEY_DESCRIPTION);
                    }
                    if (jo.has(RuleItem.KEY_IMG_URL)) {
                        imgUrl = jo.getString(RuleItem.KEY_IMG_URL);
                    }
                    if (jo.has(RuleItem.KEY_IMG_URL_X)) {
                        imgUrlX = jo.getString(RuleItem.KEY_IMG_URL_X);
                    }
                    if (jo.has(RuleItem.KEY_CONDS)) {
                        value1 = jo.getString(RuleItem.KEY_CONDS);
                    }
                    item = new RuleItem(value1,state,result,desc,imgUrl,imgUrlX);
                    items.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    @Override
    public String toString() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(MEASURE_TYPE,measureType);
            jo.put(MEASURE_RULE,measureRule);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getMeasureRule() {
        return measureRule;
    }

    public void setMeasureRule(String measureRule) {
        this.measureRule = measureRule;
    }
}
