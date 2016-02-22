package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.util.BaseMeasureDataUtil;
import com.medzone.mcloud_framework.R.string;

/**
 * TODO 血糖规则库完善
 * 
 * <li>旧的规则库去除</li> <li>新的规则库规则接入</li> <li>序列化新的异常值</li>
 * 
 * @author Robert
 * 
 */
public class BloodSugar extends BaseMeasureData implements Parcelable {

	/**
	 * 
	 */
	private static final long	serialVersionUID				= 4667339514784555780L;

	/**
	 * 这个值取决于服务端newrules表对不同模块的type的定义
	 */
	public static final int		ID								= 4;

	public static final String	TAG								= "bs";

	public static final String	NAME_STATISTIC_DATE				= "name_statistic_date";
	public static final String	NAME_STATISTIC_BEFORE_DAWN		= "name_statistic_before_dawn";
	public static final String	NAME_STATISTIC_BEFORE_BREAKFAST	= "name_statistic_before_breakfast";
	public static final String	NAME_STATISTIC_AFTER_BREAKFAST	= "name_statistic_after_breakfast";
	public static final String	NAME_STATISTIC_BEFORE_LUNCH		= "name_statistic_before_lunch";
	public static final String	NAME_STATISTIC_AFTER_LUNCH		= "name_statistic_after_lunch";
	public static final String	NAME_STATISTIC_BEFORE_DINNER	= "name_statistic_before_dinner";
	public static final String	NAME_STATISTIC_AFTER_DINNER		= "name_statistic_after_dinner";
	public static final String	NAME_STATISTIC_BEFORE_SLEEP		= "name_statistic_before_sleep";

	public static final String	NAME_FIELD_BLOOD_SUGAR			= "value1";
	public static final String	NAME_FIELD_MEASURE_STATE		= "measureState";
	public static final String	NAME_FIELD_VALIDITY				= "validity";

	public static final int		STATE_BEFORE_DAWN				= 0x00000000;
	public static final int		STATE_BEFORE_BREAKFAST			= STATE_BEFORE_DAWN + 1;
	public static final int		STATE_AFTER_BREAKFAST			= STATE_BEFORE_DAWN + 2;
	public static final int		STATE_BEFORE_LUNCH				= STATE_BEFORE_DAWN + 3;
	public static final int		STATE_AFTER_LUNCH				= STATE_BEFORE_DAWN + 4;
	public static final int		STATE_BEFORE_DINNER				= STATE_BEFORE_DAWN + 5;
	public static final int		STATE_AFTER_DINNER				= STATE_BEFORE_DAWN + 6;
	public static final int		STATE_BEFORE_SLEEP				= STATE_BEFORE_DAWN + 7;

	/**
	 * 血糖极低
	 * <2.8
	 */
	public static final int		SUGAR_POLAR_LOW					= 1;
	/**
	 * 低血糖
	 * 
	 */
	public static final int		SUGAR_LOW						= SUGAR_POLAR_LOW + 1;
	/**
	 * 血糖偏低
	 */
	public static final int		SUGAR_LIGHT_LOW					= SUGAR_POLAR_LOW + 2;
	/**
	 * 血糖正常
	 */
	public static final int		SUGAR_NORMAL					= SUGAR_POLAR_LOW + 3;
	/**
	 * 血糖轻度偏高
	 */
	public static final int		SUGAR_LIGHT_HIGH				= SUGAR_POLAR_LOW + 4;
	/**
	 * 血糖过高
	 */
	public static final int		SUGAR_OVER_HIGH					= SUGAR_POLAR_LOW + 5;
	/**
	 * 血糖极高
	 */
	public static final int		SUGAR_POLAR_HIGH				= SUGAR_POLAR_LOW + 6;

	@DatabaseField
	private Float				value1;																// bloodsugar

	@DatabaseField
	private Integer				value2;																// empty

	@DatabaseField
	private Integer				value3;																// empty

	@DatabaseField
	private Integer				measureState;

	// @DatabaseField
	// private boolean validity; //the value's validity

	public BloodSugar() {
		setTag(TAG);
	}

	public Float getSugar() {
		return value1;
	}

	public boolean isExceptionValue() {
		if (value1 == null) return true;
		if (value1 < 0.6 || value1 > 33.8) return true;
		return false;
	}

	public String getSugarDisplay() {
		return BaseMeasureDataUtil.convertAccuracy(value1).toString();
	}

	public void setSugar(Float sugar) {
		this.value1 = sugar;
	}

	public Integer getMeasureState() {
		return measureState;
	}

	public int getMeasureStateDisplay() {
		if (measureState == null) return string.todo;
		switch (measureState) {
		case BloodSugar.STATE_BEFORE_DAWN:
			return /* "凌晨" */string.early_in_the_morning;
		case BloodSugar.STATE_BEFORE_BREAKFAST:
			return /* "早餐前" */string.before_breakfast;
		case BloodSugar.STATE_AFTER_BREAKFAST:
			return /* "早餐后" */string.after_breakfast;
		case BloodSugar.STATE_BEFORE_LUNCH:
			return /* "午餐前" */string.before_lunch;
		case BloodSugar.STATE_AFTER_LUNCH:
			return /* "午餐后" */string.after_lunch;
		case BloodSugar.STATE_BEFORE_DINNER:
			return /* "晚餐前" */string.before_dinner;
		case BloodSugar.STATE_AFTER_DINNER:
			return /* "晚餐后" */string.after_dinner;
		case BloodSugar.STATE_BEFORE_SLEEP:
			return /* "睡前" */string.bedtime;
		default:
			return string.todo;
		}
	}

	public void setMeasureState(Integer measureState) {
		this.measureState = measureState;
	}

	public static List<BloodSugar> createBloodSugarList(JSONArray jsonArray, Account account) {

		if (jsonArray == null || account == null) {
			return null;
		}
		List<BloodSugar> retList = new ArrayList<BloodSugar>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createBloodSugar(jsonObj, account));
			}
			return retList;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BloodSugar createBloodSugar(JSONObject jo, Account account) {

		BloodSugar et = new BloodSugar();
		et.setBelongAccount(account);
		return parse(jo, et);
	}

	public static BloodSugar updateBloodSugar(JSONObject jo, BloodSugar bs) {

		return parse(jo, bs);
	}

	private static BloodSugar parse(JSONObject jo, BloodSugar bs) {

		try {
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				bs.setRecordID(jo.getInt("recordid"));
			}
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {

				String uid = jo.getString("measureuid");
				bs.setMeasureUID(uid);
			}
			if (jo.has("source") && !jo.isNull("source")) {
				bs.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				bs.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				bs.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				bs.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				bs.setZ(jo.getDouble("z"));
			}
			if (jo.has("state") && !jo.isNull("state")) {
				bs.setAbnormal(jo.getInt("state"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
				bs.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("value1") && !jo.isNull("value1")) {
				bs.setSugar((float) jo.getDouble("value1"));
			}
			if (jo.has("value_period") && !jo.isNull("value_period")) {
				bs.setMeasureState(jo.getInt("value_period"));
			}
			bs.setStateFlag(STATE_SYNCHRONIZED);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return bs;
	}

	@Override
	@Deprecated
	public List<Rule> getRulesCollects() {
		// if (allRules == null) {
		// allRules = new ArrayList<Rule>();
		//
		// float[] min1s = new float[] { 0f, 3.9f, 7.8f };
		// float[] max1s = new float[] { 3.9f, 7.8f, 0f };
		// String[] results = new String[] { "偏低", "正常", "偏高" };
		// int[] states = new int[] { BLOODSUGAR_STATE_LOW,
		// BLOODSUGAR_STATE_NORMAL, BLOODSUGAR_STATE_HIGH };
		// for (int i = 0; i < results.length; i++) {
		// Rule r = new Rule();
		// r.setMin1(min1s[i]);
		// r.setMax1(max1s[i]);
		// r.setMin2(0f);
		// r.setMax2(0f);
		// r.setMeasureType(BloodSugar.TAG);
		// r.setResult(results[i]);
		// r.setState(states[i]);
		// allRules.add(r);
		// }
		// }
		return allRules;
	}

	@Override
	public void toMap(Map<String, String> result) {
		result.put("value1","" + value1);
		result.put("value_period","" + measureState);
	}

	@Override
	public boolean isHealthState() {
		if (getAbnormal() == null) return false;
		if (getAbnormal() == SUGAR_NORMAL) {
			return true;
		}
		return false;
	}

	@Override
	public String getMeasureName() {
		return TAG;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private BloodSugar(Parcel source) {
		setMeasureUID(source.readString());
		value1 = source.readFloat();
		setMeasureState(source.readInt());
		setStateFlag(source.readInt());
		setActionFlag(source.readInt());
		setAbnormal(source.readInt());
		setX(source.readDouble());
		setY(source.readDouble());
		setZ(source.readDouble());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getMeasureUID());
		dest.writeFloat(value1 != null ? value1 : 0);
		dest.writeInt(getMeasureState() != null ? getMeasureState() : BloodSugar.STATE_BEFORE_DAWN);
		dest.writeInt(getStateFlag() != null ? getStateFlag() : BloodSugar.STATE_NOT_SYNCHRONIZED);
		dest.writeInt(getActionFlag() != null ? getActionFlag() : BloodSugar.ACTION_ADD_RECORD);
		// TODO 当异常等级尚未被计算时，需要使用当前的状态值参与计算
		dest.writeInt(getAbnormal() != null ? getAbnormal() : BloodSugar.SUGAR_NORMAL);
		dest.writeDouble(getX() != null ? getX() : 0);
		dest.writeDouble(getY() != null ? getY() : 0);
		dest.writeDouble(getZ() != null ? getZ() : 0);
	}

	public static final Creator<BloodSugar>	CREATOR	= new Creator<BloodSugar>() {

														@Override
														public BloodSugar[] newArray(int size) {
															return new BloodSugar[size];
														}

														@Override
														public BloodSugar createFromParcel(Parcel source) {
															return new BloodSugar(source);
														}
													};

}
