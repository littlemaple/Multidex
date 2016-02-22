package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;
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

public class FetalMovement extends BaseMeasureData implements Parcelable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 551475278807770788L;

	public static final String	TAG							= "fm";

	public static final int		ID							= 4;					// 这个值取决于模块的配置顺序

	public static final String	NAME_FIELD_AVG_HEART_RATE	= "avgHeart";
	public static final String	NAME_FIELD_MEASURE_DURATION	= "measure_duration";
	public static final String	NAME_FIELD_EFFECTIVE_TIMES	= "effectiveTimes";	// 有效胎动次数
	public static final String	NAME_FIELD_CLICK_TIMES		= "clickTimes";		// 测量过程中胎动点击次数
	/** 数据库中胎动的key **/
	public static final String	FLAG_FETAL_MOVEMENT			= "fetal_movement";

	// -----------------------------------------------------

	@DatabaseField
	private Integer				avgFetal;
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[]				data;												// 封装测量的数据
																					// 胎动：
																					// 有效点击的时间戳数组、开始时间、结束时间、点击次数

	@DatabaseField(columnName = NAME_FIELD_MEASURE_DURATION)
	private Integer				measureDuration;									// 测量时长

	// ------------------------------胎心/胎动--------------------------

	private List<Integer>		hmArray;											// 胎动的点击时间戳数组(秒)
	private int					hmClickCount;										// 总点击次数

	private long				recordBeginTime;
	private long				recordEndTime;

	private boolean				isForceParse				= true;				// 标致是否强制解析

	public void setForceParse(boolean isForceParse) {
		this.isForceParse = isForceParse;
		if (this.isForceParse) {
			parsingContent();
		}
	}

	public FetalMovement() {
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
		if (measureDuration == null) return 0;
		if (this.measureDuration < 0) return 0;
		return this.measureDuration;
	}

	/**
	 * 平均胎动
	 * 
	 * @Attention 次/小时
	 * @return
	 */
	public Integer getAvgFetal() {
		if(avgFetal == null || avgFetal<0){
			avgFetal = 0;
		}
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

	// ------------------end---------------//

	public byte[] getBytes() {
		return data;
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

	public List<Integer> getHmArray() {
		if (isForceParse) {
			parsingContent();
		}
		return hmArray;
	}

	public void setHmArray(List<Integer> hmArray) {
		this.hmArray = hmArray;
	}

	public int getHmClickCount() {
		if (isForceParse) {
			parsingContent();
		}
		return hmClickCount;
	}

	public void setHmClickCount(int hmClickCount) {
		this.hmClickCount = hmClickCount;
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

	public static List<FetalMovement> createFetalMovementList(JSONArray jsonArray, Account account) {
		List<FetalMovement> retList = new ArrayList<FetalMovement>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createFetalMovement(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;
	}

	public static FetalMovement createFetalMovement(JSONObject jo, Account account) {

		FetalMovement fh = new FetalMovement();
		fh.setBelongAccount(account);
		return parse(jo, fh);
	}

	public static FetalMovement updateFetalMovement(JSONObject jo, FetalMovement fh) {
		return parse(jo, fh);
	}

	private static FetalMovement parse(JSONObject jo, FetalMovement fh) {

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
					String movement = jo.getString("value1");
					if (!TextUtils.isEmpty(movement)) {
						Log.d(TAG, ">>>>#fetal movement str from server:" + movement);
						byte[] orgin = android.util.Base64.decode(movement.getBytes(), android.util.Base64.DEFAULT);
						Log.d(TAG, ">>>>#after decode fetal movement str:" + new String(orgin));
						fh.setHmArray(convertBytes2IntegerList(orgin));
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (jo.has("value2") && !jo.isNull("value2")) {
				try {
					String counts = jo.getString("value2");
					fh.setHmClickCount(Integer.valueOf(counts));
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

	public static final String	MEASURE_START_TIME			= "start_time";	// 测量开始时间
	public static final String	MESAURE_END_TIME			= "end_time";		// 测量结束时间
	public static final String	MEASURE_CLICK_COUNTS		= "click_counts";	// 点击次数
	public static final String	FETAL_EFFECTIVE_TIME_FRAME	= "data";			// 测量的有效数据

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
				Log.i("chenjq", "setRecordBeginTime:" + json.getLong(MEASURE_START_TIME));
			}
			if (json.has(MESAURE_END_TIME) && !json.isNull(MESAURE_END_TIME)) {
				setRecordEndTime(json.getLong(MESAURE_END_TIME));
				Log.i("chenjq", "setRecordEndTime:" + json.getLong(MESAURE_END_TIME));
			}
			if (json.has(FLAG_FETAL_MOVEMENT) && !json.isNull(FLAG_FETAL_MOVEMENT)) {
				JSONArray jarr = json.getJSONArray(FLAG_FETAL_MOVEMENT);
				if (jarr != null) {
					List<Integer> list = new ArrayList<Integer>();
					for (int i = 0; i < jarr.length(); i++) {
						list.add(jarr.getInt(i));
					}
					setHmArray(list);
				}
				Log.i(getClass().getSimpleName(), "胎动数组:" + jarr.toString());
			}
			if (json.has(MEASURE_CLICK_COUNTS) && !json.isNull(MEASURE_CLICK_COUNTS)) {
				setHmClickCount(json.getInt(MEASURE_CLICK_COUNTS));
				Log.i(getClass().getSimpleName(), "setHmClickCount：" + json.getInt(MEASURE_CLICK_COUNTS));
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
			json.put(FetalMovement.MEASURE_START_TIME, getRecordBeginTime());
			json.put(FetalMovement.MESAURE_END_TIME, getRecordEndTime());

			json.put(FetalMovement.MEASURE_CLICK_COUNTS, getHmClickCount());
			if (null != getHmArray()) {
				JSONArray hmArr = new JSONArray();
				for (long tmp : getHmArray()) {
					hmArr.put(tmp);
				}
				if (hmArr.length() > 0) {
					json.put(FetalMovement.FLAG_FETAL_MOVEMENT, hmArr);
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

	// ----------------------------以下待移动到工具类-------------------------//
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] int2ByteArrWithLittleEndian(int value) {
		byte[] bytes = new byte[4];

		bytes[3] = (byte) (value >> 24 & 0xff);

		bytes[2] = (byte) (value >> 16 & 0xff);

		bytes[1] = (byte) (value >> 8 & 0xff);

		bytes[0] = (byte) (value & 0xff);
		return bytes;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArr2IntWithLittleEndian(byte[] bytes) {
		int value = bytes[0] & 0xff | (bytes[1] & 0xff << 8) | (bytes[2] & 0xff << 16) | (bytes[3] & 0xff << 24);
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] int2ByteArrWithBigEndian(int value) {
		byte[] bytes = new byte[4];

		bytes[0] = (byte) (value >> 24 & 0xff);

		bytes[1] = (byte) (value >> 16 & 0xff);

		bytes[2] = (byte) (value >> 8 & 0xff);

		bytes[3] = (byte) (value & 0xff);
		return bytes;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int byteArr2IntWithBigEndian(byte[] bytes) {
		int value = ((bytes[0] & 0xff) << 24) | ((bytes[1] & 0xff) << 16) | ((bytes[2] & 0xff) << 8) | (bytes[3] & 0xff);
		return value;
	}

	// ---------------end------------------------//

	public static byte[] convertIntegerList2ByteArray(List<Integer> list) {
		if (list == null) return null;
		byte[] bytes = new byte[list.size() * 4];
		for (int i = 0; i < list.size(); i++) {
			byte[] tmp = int2ByteArrWithBigEndian(list.get(i) == null ? 0 : list.get(i));
			bytes[i * 4] = tmp[0];
			bytes[i * 4 + 1] = tmp[1];
			bytes[i * 4 + 2] = tmp[2];
			bytes[i * 4 + 3] = tmp[3];
		}
		return bytes;

	}

	public static List<Integer> convertBytes2IntegerList(byte[] bytes) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < bytes.length; i += 4) {
			byte[] tmp = new byte[4];
			tmp[0] = bytes[i];
			tmp[1] = bytes[i + 1];
			tmp[2] = bytes[i + 2];
			tmp[3] = bytes[i + 3];
			list.add(byteArr2IntWithBigEndian(tmp));
		}
		return list;
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

	@Override
	public void toMap(Map<String, String> result) {
		//胎动暂时没有规则，当提取出规则可随机解注释以下代码
//		result.put("value1",String.valueOf(avgFetal));
	}

	@Override
	public String getMeasureName() {
		return TAG;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	private FetalMovement(Parcel source) {
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
		setHmClickCount(source.readInt());

		if (hmArray == null) {
			hmArray = new ArrayList<Integer>();
		}
		else {
			hmArray.clear();
		}
		/**
		 * Read into an existing List object from the parcel at the current
		 * dataPosition(), using the given class loader to load any enclosed
		 * Parcelables. If it is null, the default class loader is used.
		 */
		source.readList(hmArray, List.class.getClassLoader());
		setX(source.readDouble());
		setY(source.readDouble());
		setZ(source.readDouble());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(getMeasureUID());
		dest.writeInt(getBelongAccount().getId());
		dest.writeInt(getDataCreateID() != null ? getDataCreateID() : 0);
		dest.writeInt(getActionFlag() != null ? getActionFlag() : FetalMovement.ACTION_ADD_RECORD_INCOMPLETE);
		dest.writeInt(getStateFlag() != null ? getStateFlag() : FetalMovement.STATE_NOT_SYNCHRONIZED);
		dest.writeLong(getRecordBeginTime() < 0 ? getRecordBeginTime() : 0L);
		dest.writeLong(getRecordEndTime() < 0 ? getRecordEndTime() : 0L);
		dest.writeInt(getMeasureDuration() != null ? getMeasureDuration() : 0);
		dest.writeInt(getAvgFetal() != null ? getAvgFetal() : 0);
		dest.writeInt(getHmClickCount());
		dest.writeList(hmArray);
		dest.writeDouble(getX() != null ? getX() : 0);
		dest.writeDouble(getY() != null ? getY() : 0);
		dest.writeDouble(getZ() != null ? getZ() : 0);
	}

	public static final Creator<FetalMovement>	CREATOR	= new Creator<FetalMovement>() {

															@Override
															public FetalMovement[] newArray(int size) {
																return new FetalMovement[size];
															}

															@Override
															public FetalMovement createFromParcel(Parcel source) {
																return new FetalMovement(source);
															}
														};
}
