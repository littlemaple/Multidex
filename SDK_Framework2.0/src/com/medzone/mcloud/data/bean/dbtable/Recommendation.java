package com.medzone.mcloud.data.bean.dbtable;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;
import com.medzone.framework.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recommendation extends BaseIdDatabaseContent implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public final static String TAG					= "Recommendation";
	public static final String NAME_FIELD_TYPE		= "type";
	public static final String NAME_FIELD_TITLE = "title";
	public static final String NAME_FIELD_NOTE = "note";
	public static final String NAME_FIELD_IMAGE = "image";
	public static final String NAME_FIELD_URL = "url";
	/**
	 * @Attention 需要与服务器字段保持一致
	 */
	public final static String TYPE_RUMOUR			= "rumour";
	public final static String TYPE_READ			= "read";
	public final static String TYPE_DIET			= "food";
	public final static String TYPE_SPORT			= "sport";
	public final static String TYPE_MORE_URL		= "more_url";


	@DatabaseField
	private String title;
	@DatabaseField
	private String subTitle;
	@DatabaseField
	private String note;
	@DatabaseField
	private String image;
	@DatabaseField
	private String url;
	@DatabaseField
	private String html;
	@DatabaseField
	private String more_url;
	@DatabaseField
	private String typeName;
	@DatabaseField
	private String logo;
	@DatabaseField
	private String more;//JSONArray.toString()

	public Recommendation() {
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public String getMore_url() {
		return more_url;
	}

	public void setMore_url(String more_url) {
		this.more_url = more_url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getLogo() {
		return logo;
	}

	public String getTypename() {
		return typeName;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setTypename(String typename) {
		this.typeName = typename;
	}

	private static Recommendation parse(Recommendation item, JSONObject jo) {
		if (jo == null) {
			Log.e(TAG, "没有可待解析的json");
			return item;
		}
		try {
			if (jo.has("note") && !jo.isNull("note")) {
				item.note = jo.getString("note");
			}
			if (jo.has("typename") && !jo.isNull("typename")) {
				item.typeName = jo.getString("typename");
			}
			if (jo.has("image") && !jo.isNull("image")) {
				item.image = jo.getString("image");
			}
			if (jo.has("url") && !jo.isNull("url")) {
				item.url = jo.getString("url");
			}
			if (jo.has("html") && !jo.isNull("html")) {
				item.html = jo.getString("html");
			}
			if(jo.has("logo")&&!jo.isNull("logo")){
				item.logo=jo.getString("logo");
			}
			if (jo.has("more_url") && !jo.isNull("more_url")) {
				item.more_url = jo.getString("more_url");
			}
			if (jo.has("title") && !jo.isNull("title")) {
				item.title = jo.getString("title");
			}
			if (jo.has("more") && !jo.isNull("more")) {
				item.more = jo.getString("more");
			}
			item.invalidate();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return item;
	}

	public List<Recommendation> parseMore(){
		if (StringUtils.isBlank(this.more)) return null;
		List<Recommendation> recommendations = new ArrayList<>();
		recommendations.add(this);
		JSONArray ja;
		Recommendation rec;
		try {
			ja = new JSONArray(more);
			for (int i = 0;i < ja.length();i++) {
				rec = new Recommendation();
				rec = parse(rec, ja.getJSONObject(i));
				recommendations.add(rec);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return recommendations;
	}

	public static List<Recommendation> createRecommendation(JSONObject jo) {
		if (jo == null) return null;
		List<Recommendation> list = new ArrayList<Recommendation>();
		try {
			JSONArray jSystem = jo.getJSONArray("root");
			for (int i = 0; i < jSystem.length(); i++) {
				Recommendation recommendation = Recommendation.parse(new Recommendation(),jSystem.getJSONObject(i));
				list.add(recommendation);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
