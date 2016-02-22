package com.medzone.mcloud.data.bean.dbtable;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hyc
 */
public class CommonResultDetailRecoReading implements Serializable {
    public static final String KEY_MORE_URL = "key_more_url";
    public static final long INVALID_RECORD_ID = -1L;
    private String title;
    private List<CommonResultDetailRecoReadingItem> articles;
    private String moreUrl;

    public static CommonResultDetailRecoReading parse(JSONObject jo, @NonNull CommonResultDetailRecoReading article) {
        try {
            if (jo.has("title") && !jo.isNull("title")) {
                article.setTitle(jo.getString("title"));
            }
            if (jo.has("articles") && !jo.isNull("articles")) {
                JSONArray ja = jo.getJSONArray("articles");
                article.setArticles(parse(ja));
            }
            if (jo.has("moreurl") && !jo.isNull("moreurl")) {
                article.setMoreUrl(jo.getString("moreurl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return article;
    }

    public static List<CommonResultDetailRecoReadingItem> parse(JSONArray ja) {
        if (ja == null || ja.length() == 0) return null;
        List<CommonResultDetailRecoReadingItem> items = new ArrayList<>();
        JSONObject jo;
        CommonResultDetailRecoReadingItem item;
        try {
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                item = new CommonResultDetailRecoReadingItem();
                items.add(CommonResultDetailRecoReadingItem.parse(jo, item));
            }
            return items;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonResultDetailRecoReadingItem> getArticles() {
        return articles;
    }

    public void setArticles(List<CommonResultDetailRecoReadingItem> articles) {
        this.articles = articles;
    }

    public String getMoreUrl() {
        return moreUrl;
    }

    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }
}
