package com.medzone.mcloud.data.bean.dbtable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.IUpoadAttachment;
import com.medzone.mcloud.util.BaseMeasureDataUtil;
import com.medzone.mcloud.util.UploadUtil;

public class FetalHeart extends BaseMeasureData implements Parcelable, IUpoadAttachment {

	public static final String	TAG							= "fh";
	/**
	 *
	 */
	private static final long	serialVersionUID			= 5993085085011508301L;
	public static final int		ID							= 4;						// 这个值取决于模块的配置顺序

	public static final String	NAME_FIELD_AVG_HEART_RATE	= "avgHeart";
	public static final String	NAME_FIELD_HEART_LONG		= "heartLong";
	public static final String	NAME_FIELD_WAV_ADDRESS		= "wavAddress";
	public static final String	NAME_FIELD_FETAL_TYPE		= "fetalType";
	public static final String	NAME_FIELD_MEASURE_DURATION	= "measure_duration";
	/** 数据库中胎心率的key **/
	public static final String	FLAG_FETAL_HEART_RATE		= "fetal_heart_rate";
	/** 数据库中胎心率间隔的key **/
	public static final String	FLAG_FETAL_HEART_INTERVAL	= "fetal_heart_interval";

	public static final String	FLAG_RATE_MIN				= "rate_min";
	public static final String	FLAG_RATE_MAX				= "rate_max";

	// -----------------------------------------------------

	@DatabaseField
	private Integer				avgFetal;
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[]				data;													// 封装测量的数据
	// 胎心：心率数组，开始时间，结束时间
	// 胎动：
	// 有效点击的时间戳数组、开始时间、结束时间、点击次数
	@DatabaseField
	private String				wavAddress;											// 录音文件存放的地址

	@DatabaseField(columnName = NAME_FIELD_MEASURE_DURATION)
	private Integer				measureDuration;										// 测量时长
	@DatabaseField(columnName = FLAG_RATE_MIN)
	private Integer				rateMin;
	@DatabaseField(columnName = FLAG_RATE_MAX)
	private Integer				rateMax;

	// ------------------------------胎心/胎动--------------------------

	private List<Integer>		hrArray;												// 胎心心率数组
	private List<Integer>		hrTimeArray;											// 胎心心率对应的时间戳

	private long				recordBeginTime;
	private long				recordEndTime;

	private boolean				isForceParse				= true;					// 标致是否强制解析

	public void setForceParse(boolean isForceParse) {
		this.isForceParse = isForceParse;
		if (this.isForceParse) {
			parsingContent();
		}
	}

	public FetalHeart() {
	}

	public Integer getRateMin() {
		if (rateMin == null) {
			initExtremeRange();
		}
		return rateMin;
	}

	public void setRateMin(Integer rateMin) {
		this.rateMin = rateMin;
	}

	public Integer getRateMax() {
		if (rateMax == null) {
			initExtremeRange();
		}
		return rateMax;
	}

	public void setRateMax(Integer rateMax) {
		this.rateMax = rateMax;
	}

	/**
	 * @Attention: 确保上传到云端包含这个参数，否则会导致解析过来的持续时间为0
	 * @param measureDuration
	 */
	public void setMeasureDuration(Integer measureDuration) {
		this.measureDuration = measureDuration;
	}

	/**
	 * @Attention:单位为秒
	 */
	public Integer getMeasureDuration() {
		// 持续时间从服务端解析过来，由于胎心的持续时间跟数据不太统一，由数据长度算出的持续时间小于持续时间
		parsingContent();
		if (measureDuration == null) {
			measureDuration = (int) ((recordEndTime - recordBeginTime) / 1000);
		}
		return measureDuration;
	}

	/**
	 * 平均心率或平均胎动
	 *
	 * @Attention 胎动跟胎心的单位不同 胎心 bpm 胎动 次/小时
	 * @return
	 */
	public Integer getAvgFetal() {
		if (avgFetal == null || avgFetal <= 0) return (int) BaseMeasureDataUtil.getAverage(getHrArray());
		return avgFetal;
	}

	public void setAvgFetal(Integer avgFetal) {
		this.avgFetal = avgFetal;
	}

	public String getAvgFetalDisplay(Context context) {
		return getValueDislay(context, getAvgFetal());
	}

	public String getValueDislay(Context context, Integer value) {
		if (value == null || value == 0) {
			int resId = context.getResources().getIdentifier("no_value", "string", context.getPackageName());
			return context.getString(resId);
		}
		else {
			return String.valueOf(value);
		}
	}

	public static List<Integer> cloneList(List<Integer> list) {
		if (list == null) return null;
		List<Integer> clone = new ArrayList<Integer>(list.size());
		for (Integer item : list)
			clone.add(item);
		return clone;
	}

	private void initExtremeRange() {
		List<Integer> rates = cloneList(getHrArray());
		if (rates != null && rates.size() > 0) {
			Collections.sort(rates);
			int i = 0;
			while (rates.get(i) == 0 && i < rates.size() - 1) {
				i++;
			}
			rateMin = rates.get(i);
			rateMax = rates.get(rates.size() - 1);
		}
		else {
			rateMin = 0;
			rateMax = 0;
		}
	}

	// ------------------end---------------//

	public byte[] getBytes() {
		return data;
	}

	public List<Integer> getPackFetalHeartRate() {
		return packFetalList(getHrTimeArray(), getHrArray());
	}

	@Deprecated
	/**
	 * 组装Byte的事情，不应当交由业务逻辑部分去处理。
	 * 用sql查询的时候需要设置
	 * @param data
	 */
	public void setBytes(byte[] data) {
		this.data = data;
	}

	public String getWavAddress() {
		return wavAddress;
	}

	public void setWavAddress(String wavAddress) {
		this.wavAddress = wavAddress;
	}

	public List<Integer> getHrArray() {
		if (isForceParse) {
			parsingContent();
		}
		return hrArray;
	}

	public void setHrArray(List<Integer> hrArray) {
		this.hrArray = hrArray;
	}

	public List<Integer> getHrTimeArray() {
		if (isForceParse) {
			parsingContent();
		}
		return hrTimeArray;
	}

	public void setHrTimeArray(List<Integer> hrTimeArray) {
		this.hrTimeArray = hrTimeArray;
	}

	/**
	 * 记录的开始时间，单位毫秒
	 *
	 * @return
	 */
	public long getRecordBeginTime() {
		if (isForceParse) {
			parsingContent();
		}
		return recordBeginTime;
	}

	public void setRecordBeginTime(long recordBeginTime) {
		this.recordBeginTime = recordBeginTime;
	}

	/**
	 * 记录的结束时间，单位毫秒
	 *
	 * @return
	 */
	public long getRecordEndTime() {
		if (isForceParse) {
			parsingContent();
		}
		return recordEndTime;
	}

	public void setRecordEndTime(long recordEndTime) {
		this.recordEndTime = recordEndTime;
	}

	public static List<FetalHeart> createFetalHeartList(JSONArray jsonArray, Account account) {
		List<FetalHeart> retList = new ArrayList<FetalHeart>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createFetalHeart(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;
	}

	public static FetalHeart createFetalHeart(JSONObject jo, Account account) {

		FetalHeart fh = new FetalHeart();
		fh.setBelongAccount(account);
		return parse(jo, fh);
	}

	public static FetalHeart updateFetalHeart(JSONObject jo, FetalHeart fh) {
		return parse(jo, fh);
	}

	private static FetalHeart parse(JSONObject jo, FetalHeart fh) {

		try {
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				fh.setRecordID(jo.getInt("recordid"));
			}
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {
				String uid = jo.getString("measureuid");
				fh.setMeasureUID(uid);
			}
			if (jo.has("source") && !jo.isNull("source")) {
				fh.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				fh.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				fh.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				fh.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				fh.setZ(jo.getDouble("z"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
				fh.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("value_duration") && !jo.isNull("value_duration")) {
				fh.setMeasureDuration(jo.getInt("value_duration"));
			}
			if (jo.has("value1_avg") && !jo.isNull("value1_avg")) {
				fh.setAvgFetal(jo.getInt("value1_avg"));
			}
			if (jo.has("value1") && !jo.isNull("value1")) {
				try {
					String heartLong = jo.getString("value1");
					byte[] orgin = android.util.Base64.decode(heartLong.getBytes(), android.util.Base64.DEFAULT);
					fh.setHrArray(convertFetal2List(BaseMeasureDataUtil.convertBytes2IntegerList(orgin), false));
					fh.setHrTimeArray(convertFetal2List(BaseMeasureDataUtil.convertBytes2IntegerList(orgin), true));
					Log.d(TAG, ">>>#parse heart time:" + fh.getHrTimeArray());
					Log.d(TAG, ">>>#parse heart array:" + fh.getHrArray());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			fh.setStateFlag(STATE_SYNCHRONIZED);
			fh.convertByte();

		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return fh;
	}

	public static final String	MEASURE_START_TIME			= "start_time"; // 测量开始时间
	public static final String	MESAURE_END_TIME			= "end_time";	// 测量结束时间
	public static final String	FETAL_EFFECTIVE_TIME_FRAME	= "data";		// 测量的有效数据

	/**
	 * 解析数据至内存中
	 */
	public synchronized void parsingContent() {

		setForceParse(false);

		Log.i(TAG, ">>>#=========parsingContent start==========");
		if (data == null) {
			return;
		}

		String jsonData = new String(data);

		try {
			JSONObject json = new JSONObject(jsonData);
			if (json.has(MEASURE_START_TIME) && !json.isNull(MEASURE_START_TIME)) {
				setRecordBeginTime(json.getLong(MEASURE_START_TIME));
			}
			if (json.has(MESAURE_END_TIME) && !json.isNull(MESAURE_END_TIME)) {
				setRecordEndTime(json.getLong(MESAURE_END_TIME));
			}

			if (json.has(FLAG_FETAL_HEART_INTERVAL) && !json.isNull(FLAG_FETAL_HEART_INTERVAL)) {
				JSONArray jarrTime = json.getJSONArray(FLAG_FETAL_HEART_INTERVAL);
				if (jarrTime != null) {
					List<Integer> hrTimeList = new ArrayList<Integer>();
					for (int i = 0; i < jarrTime.length(); i++) {
						hrTimeList.add(jarrTime.getInt(i));
					}
					setHrTimeArray(hrTimeList);
				}
			}
			if (json.has(FLAG_FETAL_HEART_RATE) && !json.isNull(FLAG_FETAL_HEART_RATE)) {
				JSONArray jarrRate = json.getJSONArray(FLAG_FETAL_HEART_RATE);
				if (jarrRate != null) {
					List<Integer> hrList = new ArrayList<Integer>();
					for (int i = 0; i < (jarrRate == null ? 0 : jarrRate.length()); i++) {
						hrList.add(jarrRate.getInt(i));
					}
					setHrArray(hrList);
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, ">>>#=========parsingContent end==========");
	}

	/**
	 * 转化成Byte，通常用于存入数据库
	 *
	 * @return
	 */
	public void convertByte() {

		Log.i(TAG, ">>>#=============convertByte start===================");
		JSONObject json = new JSONObject();
		try {
			json.put(FetalHeart.MEASURE_START_TIME, getRecordBeginTime());
			json.put(FetalHeart.MESAURE_END_TIME, getRecordEndTime());
			if (null != getHrArray()) {
				JSONArray hmArr = new JSONArray();
				for (long tmp : getHrArray()) {
					hmArr.put(tmp);
				}
				if (hmArr.length() > 0) {
					json.put(FetalHeart.FLAG_FETAL_HEART_RATE, hmArr);
				}
			}
			if (null != getHrTimeArray()) {
				JSONArray hmArr = new JSONArray();

				for (long tmp : getHrTimeArray()) {
					hmArr.put(tmp);
				}
				if (hmArr.length() > 0) {
					json.put(FetalHeart.FLAG_FETAL_HEART_INTERVAL, hmArr);
				}
			}

			setBytes(json.toString().getBytes());
			Log.d(TAG, ">>>>#convertByte:" + json.toString());
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i(TAG, ">>>#=============convertData end=================");
	}

	@Override
	public List<Rule> getRulesCollects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHealthState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 胎心率存在int的低16位中，时间间隔存在高16位
	 *
	 * @param list
	 * @param isTimeStyle
	 * @return
	 */
	public static List<Integer> convertFetal2List(List<Integer> list, boolean isTimeStyle) {
		List<Integer> resultList = new ArrayList<Integer>();
		if (list == null) return null;
		if (list.size() == 0) return resultList;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				resultList.add(isTimeStyle ? (list.get(i) >> 16 & 0XFFFF) : list.get(i) & 0XFFFF);
			}
		}
		return resultList;
	}

	public static List<Integer> packFetalList(List<Integer> timestampsList, List<Integer> heartRateList) {
		List<Integer> resultList = new ArrayList<Integer>();
		if (timestampsList == null || heartRateList == null || timestampsList.size() != heartRateList.size()) return null;
		if (timestampsList.size() == 0) return resultList;
		for (int i = 0; i < timestampsList.size(); i++) {
			int value = timestampsList.get(i) << 16 | heartRateList.get(i);
			resultList.add(value);
		}
		return resultList;
	}

	private FetalHeart(Parcel source) {
		setMeasureUID(source.readString());
		int accountId = source.readInt();
		Account account = new Account();
		account.setId(accountId);
		setBelongAccount(account);
		setDataCreateID(source.readInt());
		setActionFlag(source.readInt());
		setStateFlag(source.readInt());
		setRecordBeginTime(source.readLong());
		setRecordEndTime(source.readLong());
		setMeasureDuration(source.readInt());
		setAvgFetal(source.readInt());
		source.readList(hrArray, List.class.getClassLoader());
		source.readList(hrTimeArray, getClass().getClassLoader());
		setX(source.readDouble());
		setY(source.readDouble());
		setZ(source.readDouble());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(getMeasureUID());
		dest.writeInt(getBelongAccount().getId());
		dest.writeInt(getDataCreateID() != null ? getDataCreateID() : 0);
		dest.writeInt(getActionFlag() != null ? getActionFlag() : FetalHeart.ACTION_ADD_RECORD_INCOMPLETE);
		dest.writeInt(getStateFlag() != null ? getStateFlag() : FetalHeart.STATE_NOT_SYNCHRONIZED);
		dest.writeLong(getRecordBeginTime() < 0 ? getRecordBeginTime() : 0L);
		dest.writeLong(getRecordEndTime() < 0 ? getRecordEndTime() : 0L);
		dest.writeInt(getMeasureDuration() != null ? getMeasureDuration() : 0);
		dest.writeInt(getAvgFetal() != null ? getAvgFetal() : 0);
		dest.writeList(hrArray);
		dest.writeList(hrTimeArray);
		dest.writeDouble(getX() != null ? getX() : 0);
		dest.writeDouble(getY() != null ? getY() : 0);
		dest.writeDouble(getZ() != null ? getZ() : 0);
	}

	public static final Creator<FetalHeart>	CREATOR	= new Creator<FetalHeart>() {

		@Override
		public FetalHeart[] newArray(int size) {
			// TODO
			// Auto-generated
			// method stub
			return new FetalHeart[size];
		}

		@Override
		public FetalHeart createFromParcel(Parcel source) {
			// TODO
			// Auto-generated
			// method stub
			return new FetalHeart(source);
		}
	};

	@Override
	public String getUploadName() {
		if (TextUtils.isEmpty(getWavAddress())) {
			Log.i(TAG, "文件名不合法：" + getWavAddress());
			return null;
		}

		File file = new File(getWavAddress());
		if (!file.exists()) {
			Log.e(TAG, "文件不存在");
			return null;
		}
		if (getRecordID() == null) {
			Log.e(TAG, "数据还未同步到云端");
			return null;
		}
		String fileName = UploadUtil.formatFileName(TAG, Long.valueOf(getRecordID()), file.length(), UploadUtil.getFileSuffix(getWavAddress()));
		Log.i(TAG, "--->filePath:" + file.getName() + ",suffix:" + UploadUtil.getFileSuffix(getWavAddress()) + ",size:" + file.length() + ",uploadName:" + fileName);
		return fileName;
	}

	@Override
	public void toMap(Map<String, String> result) {
		//胎心规则由云端显示，并且由云端匹配
//		result.put("value1",String.valueOf(avgFetal));
//		result.put("beyond10",measureDuration > 10?"Y":"N");
	}

	@Override
	public String getMeasureName() {
		return TAG;
	}
}
