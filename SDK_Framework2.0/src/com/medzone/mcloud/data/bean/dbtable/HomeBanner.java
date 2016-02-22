package com.medzone.mcloud.data.bean.dbtable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 12/24/2015.
 */
public class HomeBanner implements Serializable {
    public static final String OPEN_TYPE_OPEN = "open";
    public static final String OPEN_TYPE_WEB_VIEW = "webview";
    public static final String KEY_NAME_FIELD_URL = "url";
    public static final String KEY_NAME_FIELD_IMG_URL = "imgurl";
    public static final String KEY_NAME_FIELD_OPEN_TYPE = "opentype";

    public String url;
    public String imgUrl;

    /**
     * <code>OPEN_TYPE_OPEN</code><br/>
     * <code>OPEN_TYPE_WEB_VIEW</code>
     */
    public String openType;

    public HomeBanner() {
    }

    public HomeBanner(String url, String imgUrl, String openType) {
        this.url = url;
        this.imgUrl = imgUrl;
        this.openType = openType;
    }

    public static HomeBanner parse(@NonNull JSONObject jo, @Nullable HomeBanner homeBanner) {
        HomeBanner hb = homeBanner == null ? new HomeBanner() : homeBanner;
        try {
            if (jo.has(KEY_NAME_FIELD_URL) && !jo.isNull(KEY_NAME_FIELD_URL)) {
                hb.setUrl(jo.getString(KEY_NAME_FIELD_URL));
            }
            if (jo.has(KEY_NAME_FIELD_IMG_URL) && !jo.isNull(KEY_NAME_FIELD_IMG_URL)) {
                hb.setImgUrl(jo.getString(KEY_NAME_FIELD_IMG_URL));
            }
            if (jo.has(KEY_NAME_FIELD_OPEN_TYPE) && !jo.isNull(KEY_NAME_FIELD_OPEN_TYPE)) {
                hb.setOpenType(jo.getString(KEY_NAME_FIELD_OPEN_TYPE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hb;
    }

    public static List<HomeBanner> convertFromJSONArray(@NonNull JSONArray ja) {
        List<HomeBanner> banners = new ArrayList<>();
        HomeBanner banner;
        try {
            for (int i = 0; i < ja.length(); i++) {
                banner = parse(ja.getJSONObject(i), null);
                banners.add(banner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return banners;
    }

    /**
     *
     * @param jo contains a JSONArray
     * @return
     */
    public static List<HomeBanner> convertFromJSONObject(@NonNull JSONObject jo) {
        JSONArray ja = new JSONArray();
        try {
            ja = jo.getJSONArray("root");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertFromJSONArray(ja);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }
}
