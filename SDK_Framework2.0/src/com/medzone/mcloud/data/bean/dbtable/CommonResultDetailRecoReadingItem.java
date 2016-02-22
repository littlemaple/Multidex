package com.medzone.mcloud.data.bean.dbtable;

import com.medzone.framework.data.bean.BasePagingContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 在测量结果详情页中的阅读推荐
 *
 * @author hyc
 */
public class CommonResultDetailRecoReadingItem extends BasePagingContent implements Serializable {
    private String imgUrl;
    private String title;
    private String note;
    private String url;

    public static CommonResultDetailRecoReadingItem parse(JSONObject jo, CommonResultDetailRecoReadingItem item) {
        try {
            if (jo.has("url") && !jo.isNull("url")) {
                item.setUrl(jo.getString("url"));
            }
            if (jo.has("title") && !jo.isNull("title")) {
                item.setTitle(jo.getString("title"));
            }
            if (jo.has("note") && !jo.isNull("note")) {
                item.setNote(jo.getString("note"));
            }
            if (jo.has("imageurl") && !jo.isNull("imageurl")) {
                item.setImgUrl(jo.getString("imageurl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        item.setStateFlag(STATE_SYNCHRONIZED);
        return item;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
