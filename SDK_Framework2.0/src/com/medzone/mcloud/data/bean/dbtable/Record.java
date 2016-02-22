package com.medzone.mcloud.data.bean.dbtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.diegocarloslima.fgelv.lib.ReflectionUtils;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.java.Event;
import com.medzone.mcloud.data.bean.java.HeartRate;
import com.medzone.mcloud.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Record extends BaseMeasureData {
	public static final String TAG					= "ecg";
	public static final int			RECORD_ID			= 10;
	public static final String RECORD_TAG			= "rd";
	public static final String NAME_FIELD_RECORD	= "record";
	public static final int			HEART_RATE_BYTE_LEN	= 1440 * 2;					// 24hr
	public static final int			EVENT_LIST_BYTE_LEN	= 8 * 2880;
	public static String DEVICE_ID           = "AABBCC";
	
	// heart rate
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	public byte[]					heartRateThumbnail;
	// event
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	public byte[]					eventList;

	@DatabaseField
	private long					duration; //second

	 //second
	private long					deviceDuration;
	
	// id
	public short					deviceRecordId;

	public boolean                  mIsDirty;
	
	
	@DatabaseField
	public short					maxHeartRate;
	@DatabaseField
	public short					minHeartRate;
	@DatabaseField
	public short					avgHeartRate;

	@DatabaseField
	public short					allEventTimes;

	@DatabaseField
	public short					asystoleTimes;


	@DatabaseField
	public short					fibrillationTimes;

	@DatabaseField
	public short					vTachTimes;

	@DatabaseField
	public short					vRhythmTimes;

	@DatabaseField
	public short					pairPVCTimes;

	@DatabaseField
	public short					multiPVCTimes;

	@DatabaseField
	public short					bigeminyTimes;

	@DatabaseField
	public short					trigeminyTimes;

	@DatabaseField
	public short					r_on_tTimes;

	@DatabaseField
	public short					unregularTime;

	@DatabaseField
	public short					bradyTimes;

	@DatabaseField
	public short					tachyTimes;

	@DatabaseField
	public short					missBeatTimes;

	@DatabaseField
	public short					chestCongestionTimes;
	@DatabaseField
	public short					dizzyTimes;
	@DatabaseField
	public short					chestPainyTimes;
	@DatabaseField
	public short					palpitateTimes;
	@DatabaseField
	public short					otherTimes;

	public Record() {
		setTag(TAG);
	}
	
	public static void setDeviceID(String devLogicAddr){
		DEVICE_ID = devLogicAddr;
	}

	public int getRecordId() {
		return deviceRecordId;
	}
	
	public void setRecordUId(short rd) {
		deviceRecordId = rd;
//		recordAbstracts.setRecordUId(rd);
	}

	public void setRecordDuration(long duration){
		deviceDuration = duration;
	}
	
	public long getRecordDuration() {
		return deviceDuration;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setDuration() {
		if(deviceDuration > duration) {
			duration = deviceDuration;
			mIsDirty = true;
		}
	}

	public boolean isDirty(){
		return mIsDirty;
	}

	public void setHeartThumbnail(byte[] thumbnails) {
//		recordAbstracts.setHeartThumbnail(thumbnails);
		
		heartRateThumbnail = thumbnails;
		if (thumbnails == null || thumbnails.length == 0) {
			return;
		}
		int len = thumbnails.length < HEART_RATE_BYTE_LEN ? thumbnails.length : HEART_RATE_BYTE_LEN;
		calcHeartRates(thumbnails, len);

		mIsDirty = false;
	}

	public void calcHeartRates(byte[] heartRateThumbnail, int len) {
		int heartLen = len >> 1;
		short heartRate0 = HeartRate.getHeartRate(heartRateThumbnail, 0);
		short max = heartRate0;
		short min = heartRate0;
		long sum = heartRate0;
		for (int i = 1; i < heartLen; i++) {
			short heartRate = HeartRate.getHeartRate(heartRateThumbnail, i * 2);
			sum += heartRate;
			if (heartRate < min) {
				min = heartRate;
			}
			else if (heartRate > max) {
				max = heartRate;
			}
		}

		maxHeartRate = max;
		minHeartRate = min;
		avgHeartRate = (short) (sum / heartLen);
	}

	public byte[] getHeartRateThumbnail() {
		return heartRateThumbnail;
	}

	public byte[] getEventList() {
		return eventList;
	}

	public void setEventList(byte[] events) {
		eventList = events;
		if (events == null || events.length == 0) return;
		int len = events.length < EVENT_LIST_BYTE_LEN ? events.length : EVENT_LIST_BYTE_LEN;
		calEvents(events, len);
	}
	
	public void calEvents(byte[] eventList, int len) {
		short[] result = Event.statisticArray(eventList, len);
		asystoleTimes = result[0];
		fibrillationTimes = result[1];
		vTachTimes = result[2];
		vRhythmTimes = result[3];
		pairPVCTimes = result[4];
		multiPVCTimes = result[5];
		bigeminyTimes = result[6];
		trigeminyTimes = result[7];
		r_on_tTimes = result[8];
		unregularTime = result[9];
		bradyTimes = result[10];
		tachyTimes = result[11];
		missBeatTimes = result[12];
		dizzyTimes = result[13];
		chestCongestionTimes = result[14];
		chestPainyTimes = result[15];
		palpitateTimes = result[16];
		otherTimes = result[17];
	}

	@Override
	public List<Rule> getRulesCollects() {
		return null;
	}

	@Override
	public boolean isHealthState() {
		return false;
	}

	@Override
	public String getMeasureName() {
		return "DCG Record";
	}

	public int getFeelTimes() {
		return chestCongestionTimes + dizzyTimes + chestPainyTimes + palpitateTimes + otherTimes;
	}


	public int getEventTimes() {
		return asystoleTimes + fibrillationTimes + vTachTimes + vRhythmTimes + pairPVCTimes + multiPVCTimes + bigeminyTimes + trigeminyTimes + r_on_tTimes + unregularTime + bradyTimes + tachyTimes
				+ missBeatTimes;
	}
	
	public void setEventTimes(short[] eventTimes){
		if(eventTimes == null || eventTimes.length!=13)
			return;
		
		asystoleTimes = eventTimes[0];
		fibrillationTimes = eventTimes[1];
		vTachTimes = eventTimes[2]; 
		vRhythmTimes= eventTimes[3]; 
		pairPVCTimes = eventTimes[4]; 
		multiPVCTimes = eventTimes[5]; 
		bigeminyTimes = eventTimes[6]; 
		trigeminyTimes = eventTimes[7]; 
		r_on_tTimes = eventTimes[8];
		unregularTime = eventTimes[9]; 
		bradyTimes = eventTimes[10]; 
		tachyTimes = eventTimes[11];
		missBeatTimes = eventTimes[12];
	}
	
	public void setFeelTimes(short[] feelTimes){
		if(feelTimes == null || feelTimes.length!=5)
			return;
		
		chestCongestionTimes =feelTimes[0];
		dizzyTimes =feelTimes[1];
		chestPainyTimes =feelTimes[2];
		palpitateTimes =feelTimes[3];
		otherTimes =feelTimes[4];
	}


	public int getWarningTimes() {
		return asystoleTimes + fibrillationTimes + vTachTimes + bradyTimes + tachyTimes;
	}

	public static Record read(byte[] params, int offset) {
		if ((params.length < 2) || (params.length > 2 && (params.length - offset) < 2)) return null;

		Record record = new Record();
		record.deviceRecordId = IOUtils.byteArrayToShort(params, offset + 0);
		Date startTime = IOUtils.bytesToDate(params, offset + 2);
		Date endTime   = IOUtils.bytesToDate(params, offset + 6);
		String measureUID = generateMeasureID(startTime, DEVICE_ID);
		record.setMeasureUID(measureUID);
		record.deviceDuration = (endTime.getTime() - startTime.getTime())/1000;
		return record;
	}
	
	@SuppressLint("SimpleDateFormat")
	private static String generateMeasureID(Date startTime, String deviceId) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = format.format(startTime);
		String dev = deviceId.substring(0, 6);
		String measureId = time + dev;
		return measureId;
	}

	public static Record[] readArray(byte[] params, int offset, int len) {
		int datalen = params.length > len ? len : params.length;
		int size = datalen / 10;
		if (size < 0) return null;

		Record[] records = new Record[size];
		for (int i = 0; i < size; i++) {
			records[i] = read(params, offset + i * 10);
		}
		return records;
	}

//	@Override
//	public String toString() {
//		StringBuffer text = new StringBuffer();
//		text.append("ID " + deviceRecordId + ":");
//		text.append(EcgTimeStamp.getTimeString(getMeasureTime()*1000) + "~" + EcgTimeStamp.getTimeString(getMeasureTime()*1000+duration) + "\r\n");
//		return text.toString();
//	}

	public static List<Record> createRecordList(JSONArray jsonArray, Account account) {
		List<Record> retList = new ArrayList<Record>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				retList.add(createRead(jsonObj, account));
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return retList;
	}

	public static Record createRead(JSONObject jo, Account account) {
		Record record = new Record();
		record.setBelongAccount(account);
		return parse(jo, record);
	}

	private static Record parse(JSONObject jo, Record rd) {
		try {
			// �������ݽ���
			if (jo.has("recordid") && !jo.isNull("recordid")) {
				// rd.setRecordUID(jo.getInt("recordid"));
				// rd.setRecordUId((short)jo.getInt("recordid"));
				rd.setRecordID(jo.getInt("recordid"));
				//System.out.println("<<>># json rid= "+jo.getInt("recordid"));
			}
			// �ĵ�������ݵ� measureuid Ϊ����ʱ�� YYYYMMDDHHIISS + 6λ(14+6)�����豸�� squence
			// id ���measureuid
			if (jo.has("measureuid") && !jo.isNull("measureuid")) {
				String uid = jo.getString("measureuid");

				rd.setMeasureUID(uid);
//				rd.getRecordAbstracts().setMeasureUID(uid);
				// ǰ14λYYYYMMDDHHmmSS
				uid = uid.substring(0, 14);
			}
			if (jo.has("source") && !jo.isNull("source")) {
				rd.setSource(jo.getString("source"));
			}
			if (jo.has("readme") && !jo.isNull("readme")) {
				rd.setReadme(jo.getString("readme"));
			}
			if (jo.has("x") && !jo.isNull("x")) {
				rd.setX(jo.getDouble("x"));
			}
			if (jo.has("y") && !jo.isNull("y")) {
				rd.setY(jo.getDouble("y"));
			}
			if (jo.has("z") && !jo.isNull("z")) {
				rd.setZ(jo.getDouble("z"));
			}
			if (jo.has("uptime") && !jo.isNull("uptime")) {
				rd.setUptime(jo.getLong("uptime"));
			}
			if (jo.has("voiceme") && !jo.isNull("voiceme")) {

			}
			if(jo.has("value_duration") && !jo.isNull("value_duration")){
				rd.setDuration(jo.getLong("value_duration"));
			}

			// value1,value2��Ҫ����base64����
			if (jo.has("value1") && !jo.isNull("value1")) {
				try {
					// heartRateThumbnail
					String heartRateStr = jo.getString("value1");
					//System.out.println("<<>># json hrs= "+heartRateStr);
					if (!TextUtils.isEmpty(heartRateStr)) {
						byte[] orgin = android.util.Base64.decode(heartRateStr.getBytes(), android.util.Base64.DEFAULT);
						rd.setHeartThumbnail(orgin);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (jo.has("value2") && !jo.isNull("value2")) {
				try {
					String eventStr = jo.getString("value2");
					if (!TextUtils.isEmpty(eventStr)) {
						byte[] eventList = Base64.decode(eventStr.getBytes(), Base64.DEFAULT);
						rd.setEventList(eventList);
					}

				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}

			rd.setStateFlag(STATE_SYNCHRONIZED);
			// rd.convertByte();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rd;
	}

	@Override
	public void setBelongAccount(Account account) {
		super.setBelongAccount(account);
	}

	@Override
	public void setActionFlag(Integer actionFlag) {
		super.setActionFlag(actionFlag);
	}

	@Override
	public void setStateFlag(Integer stateFlag) {
		super.setStateFlag(stateFlag);
	}

	@Override
	public void setMeasureUID(String measureUID) {
		super.setMeasureUID(measureUID);
	}

	public List<Integer> getDescription(Context context, int size) {
		if (size <= 0) {
			return null;
		}

		List<Integer> ret = new ArrayList<Integer>();

		if (asystoleTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_beating"));

		if (fibrillationTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_page_ic_vft"));

		if (bradyTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_tachycardia"));

		if (tachyTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_bradycardia"));

		if (vTachTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_arvc_vt"));

		if (vRhythmTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_ven_rhythm"));

		if (pairPVCTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_ven_two_repeating"));

		if (multiPVCTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_mul_repeating"));

		if (bigeminyTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_ven_bigeminy"));

		if (trigeminyTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_ven_trigeminy"));

		if (r_on_tTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_r_on_t"));

		if (unregularTime > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_irr_rhythm"));

		if (missBeatTimes > 0) ret.add(ReflectionUtils.getDrawableId(context, "home_ic_ecg_leakage"));
		// if (chestCongestionTimes > 0)
		// ret.add(ReflectionUtils.getDrawableId(context,
		// "home_page_ic_arrest"));
		// if (dizzyTimes > 0) ret.add(ReflectionUtils.getDrawableId(context,
		// "home_page_ic_arrest"));
		// if (chestPainyTimes > 0)
		// ret.add(ReflectionUtils.getDrawableId(context,
		// "home_page_ic_arrest"));
		// if (palpitateTimes > 0)
		// ret.add(ReflectionUtils.getDrawableId(context,
		// "home_page_ic_arrest"));
		return ret.size() > size ? ret.subList(0, size) : ret;
	}
	
	//////////////////RecordAbs///////////////////
	public Event[] getEvents() {
	   if (eventList == null) return null;
	   return Event.readArray(eventList, 0, eventList.length);
   }
	
	public HeartRate[] getHeartRates() {
	   if (heartRateThumbnail == null) return null;

	   return HeartRate.readTotalRecord(heartRateThumbnail, heartRateThumbnail.length);
    }

	@Override
	public void toMap(Map<String, String> result) {

	}
}
