package com.medzone.mcloud.data.bean.dbtable;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Robert.
 * 
 */
public class UrinaryProduction extends BaseMeasureData {

	private static final long	serialVersionUID	= 3095444037718543678L;

	/**
	 * 这个值取决于服务端newrules表对不同模块的type的定义
	 */
	public static final String	TAG								= "up";

	public static final String	NAME_FIELD_UP			= "value1";


	@DatabaseField
	private Integer				value1;									// temperature


	public Integer getUrinaryProduction() {
		return value1;
	}


	public void setUrinaryProduction(Integer uv) {
		this.value1 = uv;
	}

	public UrinaryProduction() {
		setTag(TAG);
	}

	public static List<UrinaryProduction> createUrinaryProductionList(JSONArray jsonArray, Account account) {
		List<UrinaryProduction> retList = new ArrayList<UrinaryProduction>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createUrinaryProduction(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;

	}

	public static UrinaryProduction createUrinaryProduction(JSONObject jo, Account account) {
		UrinaryProduction et = new UrinaryProduction();
		et.setBelongAccount(account);

		return parse(jo, et);
	}

	public static UrinaryProduction updateUrinaryProduction(JSONObject jo, UrinaryProduction et) {
		return parse(jo, et);
	}

	private static UrinaryProduction parse(JSONObject jo, UrinaryProduction et) {

		try {
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				et.setRecordID(jo.getInt("recordid"));
			}
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {

				String uid = jo.getString("measureuid");
				et.setMeasureUID(uid);
			}
			if (jo.has("source") && !jo.isNull("source")) {
				et.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				et.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				et.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				et.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				et.setZ(jo.getDouble("z"));
			}
			if (jo.has("state") && !jo.isNull("state")) {
				et.setAbnormal(jo.getInt("state"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
				et.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("value1") && !jo.isNull("value1")) {
				et.setUrinaryProduction(jo.getInt("value1"));
			}
			et.setStateFlag(STATE_SYNCHRONIZED);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return et;
	}

	@Override
	public List<Rule> getRulesCollects() {
		return null;
	}

	@Override
	public boolean isHealthState() {
		return true;
	}

	@Override
	public void toMap(Map<String, String> result) {
		result.put("value1",String.valueOf(value1));
	}

	@Override
	public String getMeasureName() {
		return TAG;
	}
}
