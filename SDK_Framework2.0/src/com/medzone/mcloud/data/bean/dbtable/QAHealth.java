package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.medzone.framework.network.NetworkClientResult;

public class QAHealth {

	private static final int	TYPE_INVALID			= 0x1000;
	public static final int		TYPE_NUM				= TYPE_INVALID + 1;
	public static final int		TYPE_NUMBER_PICKER		= TYPE_INVALID + 2;
	public static final int		TYPE_DATE_PICKER		= TYPE_INVALID + 3;
	public static final int		TYPE_CHOICE_ITEM		= TYPE_INVALID + 4;
	public static final int		TYPE_CHECKBOX_ITEM		= TYPE_INVALID + 5;

	public static final String	PROFILE_KEY_BIRTHDAY	= "birthday";
	public static final String	PROFILE_KEY_GENDER		= "gender";
	public static final String	PROFILE_KEY_WAISTLINE	= "waistline";
	public static final String	PROFILE_KEY_TALL		= "tall";
	public static final String	PROFILE_KEY_WEIGHT		= "weight";
	public static final String	PROFILE_KEY_BP_VALUE1	= "bp_value1";
	public static final String	PROFILE_KEY_BP_VALUE2	= "bp_value2";

	public static final String	UNIT_CM					= "cm";
	public static final String	UNIT_KG					= "kg";

	private String				profileidKey;								// 字段名
	private String				profileidDescription;						// 字段名的描述
	private String				profileidValue;							// 字段对应的值,这个值是会提交到API的参数

	private String				title;										// QA标题,当标题获取为空时,使用字段名描述进行显示
	private String				numUnit;
	private float					numMin;									// 范围的最小值
	private float					numMax;									// 范围的最大值

	private int					showType;									// 展示的样式

	private String[]			choiceItem;								// 选项值,可能与选型描述一致
	private String[]			choiceItemDescription;						// 选项描述,通常配合选型值进行呈现

	public String getProfileidKey() {
		return profileidKey;
	}

	public void setProfileidKey(String profileidKey) {
		this.profileidKey = profileidKey;
	}

	public String getProfileidDescription() {
		return profileidDescription;
	}

	public void setProfileidDescription(String profileidDescription) {
		this.profileidDescription = profileidDescription;
	}

	public String getProfileidValue() {
		return profileidValue;
	}

	public void setProfileidValue(String profileidValue) {
		this.profileidValue = profileidValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNumUnit() {
		return numUnit;
	}

	public void setNumUnit(String numUnit) {
		this.numUnit = numUnit;
	}

	public float getNumMin() {
		return numMin;
	}

	public void setNumMin(float numMin) {
		this.numMin = numMin;
	}

	public float getNumMax() {
		return numMax;
	}

	public void setNumMax(float numMax) {
		this.numMax = numMax;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}

	public String[] getChoiceItem() {
		return choiceItem;
	}

	public void setChoiceItem(String[] choiceItem) {
		this.choiceItem = choiceItem;
	}

	public String[] getChoiceItemDescription() {
		return choiceItemDescription;
	}

	public void setChoiceItemDescription(String[] choiceItemDescription) {
		this.choiceItemDescription = choiceItemDescription;
	}

	/**
	 * 解析获取QA问卷列表
	 * 
	 * @param res
	 */
	public static List<QAHealth> createQAHealthList(NetworkClientResult res) {

		JSONArray ja = null;
		List<QAHealth> qaList = null;
		JSONObject jo = res.getResponseResult();

		try {
			ja = jo.getJSONArray("root");
			qaList = new ArrayList<QAHealth>();
			for (int i = 0; i < ja.length(); i++) {
				QAHealth qa = createQAHealth((JSONObject) ja.get(i));
				if (qa != null) {
					qaList.add(qa);
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return qaList;
	}

	public static QAHealth createQAHealth(JSONObject jo) {

		if (jo != null) {
			QAHealth qa = new QAHealth();
			try {
				if (jo.has("profileid") && !jo.isNull("profileid")) {
					qa.setProfileidKey(jo.getString("profileid"));
				}
				if (jo.has("name") && !jo.isNull("name")) {
					qa.setProfileidDescription(jo.getString("name"));
				}
				if (jo.has("title") && !jo.isNull("title")) {
					qa.setTitle(jo.getString("title"));
				}
				if (jo.has("unit") && !jo.isNull("unit")) {
					qa.setNumUnit(jo.getString("unit"));
				}
				if (jo.has("value") && !jo.isNull("value")) {
					qa.setProfileidValue(jo.getString("value"));
				}
				if (jo.has("validator") && !jo.isNull("validator")) {
					String validatorValue = jo.getString("validator");
					if (validatorValue.matches("num")) {
						qa.setShowType(TYPE_NUM);
					}
					else if (validatorValue.matches("num:(.*)")) {
						qa.setShowType(TYPE_NUMBER_PICKER);
						final String split = validatorValue.replace("num:", "");
						String[] arr = split.split("-");
						if (arr != null && arr.length > 0) {
							qa.setNumMin(Float.valueOf(arr[0]));
							qa.setNumMax(Float.valueOf(arr[1]));
						}
					}
					else if (validatorValue.matches("date")) {
						qa.setShowType(TYPE_DATE_PICKER);
					}
					else if (validatorValue.matches("checkbox")) {
						qa.setShowType(TYPE_CHECKBOX_ITEM);
						String[] choiceItem = new String[] { "Y", "N" };
						String[] choiceItemDescription;
						Log.d("robert", "default locale:" + Locale.getDefault().toString());

						if (TextUtils.equals(Locale.getDefault().toString(), Locale.CHINA.toString()) || TextUtils.equals(Locale.getDefault().toString(), Locale.CHINESE.toString())) {
							choiceItemDescription = new String[] { "是", "否" };
						}
						else {
							choiceItemDescription = new String[] { "Yes", "No" };
						}

						qa.setChoiceItem(choiceItem);
						qa.setChoiceItemDescription(choiceItemDescription);
					}
					else if (validatorValue.matches("enum:(.*)=(.*)")) {
						qa.setShowType(TYPE_CHOICE_ITEM);

						final String split = validatorValue.replace("enum:", "");
						final String regex = "=|,";
						String[] arr = split.split(regex);

						if (arr != null && arr.length > 0) {
							final int choiceItemSize = arr.length / 2;
							String[] choiceItem = new String[choiceItemSize];
							String[] choiceItemDescription = new String[choiceItemSize];
							for (int i = 0; i < choiceItemSize; i++) {
								choiceItem[i] = arr[2 * i];
								choiceItemDescription[i] = parseEncode(arr[2 * i + 1]);
							}
							qa.setChoiceItem(choiceItem);
							qa.setChoiceItemDescription(choiceItemDescription);
						}
					}
					else if (validatorValue.matches("enum:(.*)")) {
						qa.setShowType(TYPE_CHOICE_ITEM);

						final String split = validatorValue.replace("enum:", "");
						final String regex = "=|,";
						String[] arr = split.split(regex);

						if (arr != null && arr.length > 0) {
							final int choiceItemSize = arr.length;
							String[] choiceItem = new String[choiceItemSize];
							String[] choiceItemDescription = new String[choiceItemSize];
							for (int i = 0; i < arr.length; i++) {
								choiceItem[i] = arr[i];
								choiceItemDescription[i] = arr[i];
								choiceItemDescription[i] = parseEncode(arr[i]);
							}
							qa.setChoiceItem(choiceItem);
							qa.setChoiceItemDescription(choiceItemDescription);
						}
					}
				}
				return qa;
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String parseEncode(String desc) {
		return desc.replaceAll("%2C", ",");
	}
}
