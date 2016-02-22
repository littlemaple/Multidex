package com.medzone.mcloud.data.bean.dbtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hyc on 2015/12/10.
 */
public class SearchMedicineItem implements Serializable {
    public static final String KEY_MEDICINE_DETAIL_URL = "medicine_url";

    private int drugid;
    private String name;
    private String specification;
    private String factory;
    private String unit;
    private String url;
    private String img;

    public SearchMedicineItem() {
    }

    public SearchMedicineItem(int drugid, String name, String specification, String factory, String unit, String url, String img) {
        this.drugid = drugid;
        this.name = name;
        this.specification = specification;
        this.factory = factory;
        this.unit = unit;
        this.url = url;
        this.img = img;
    }

    public static List<SearchMedicineItem> convertSearchResJSONObject(JSONObject json) {
        if (json == null) return null;
        List<SearchMedicineItem> items = new ArrayList<>();
        JSONArray ja;
        JSONObject jo;
        SearchMedicineItem item;
        try {
            ja = json.getJSONArray("root");
            for (int i = 0;i < ja.length();i++) {
                jo = ja.getJSONObject(i);
                item = convert2SearchMedicineItem(jo);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    public static SearchMedicineItem convert2SearchMedicineItem(JSONObject json) {
        SearchMedicineItem item = new SearchMedicineItem();
        try {
            if (json.has("drugid") && !json.isNull("drugid")) {
                item.setDrugid(Integer.parseInt(json.getString("drugid")));
            }
            if (json.has("name") && !json.isNull("name")) {
                item.setName(json.getString("name"));
            }
            if (json.has("specification") && !json.isNull("specification")) {
                item.setSpecification(json.getString("specification"));
            }
            if (json.has("factory") && !json.isNull("factory")) {
                item.setFactory(json.getString("factory"));
            }
            if (json.has("unit") && !json.isNull("unit")) {
                item.setUnit(json.getString("unit"));
            }
            if (json.has("url") && !json.isNull("url")) {
                item.setUrl(json.getString("url"));
            }
            if (json.has("img") && !json.isNull("img")) {
                item.setImg(json.getString("img"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }

    public int getDrugid() {
        return drugid;
    }

    public void setDrugid(int drugid) {
        this.drugid = drugid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
