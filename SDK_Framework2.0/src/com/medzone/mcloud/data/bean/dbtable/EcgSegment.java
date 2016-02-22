package com.medzone.mcloud.data.bean.dbtable;

import android.util.Base64;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.java.EcgSlice;
import com.medzone.mcloud.util.EcgTimeStamp;
import com.medzone.mcloud.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author ChenJunQi.
 * 
 */ 
public class EcgSegment extends BaseMeasureData {
	public static final int    ECG_ID    = 12;
	public static final String TAG       = "ecg_segment";
	
	public static final String NAME_FIELD_RECORDUID = "recordUId";
	public static final String NAME_FIELD_SEGMENTID = "segmentId";
	public static final String NAME_FIELD_EVENTTYPE = "eventType";
	public static final String NAME_FIELD_FEELTYPE  = "feelType";
	public static final String NAME_FIELD_SEGMENT   = "segment";
	
	public static final int    BYTE_LEN 	= 45000; 
	
	@DatabaseField
	private short recordUId;
	
	@DatabaseField
	private short segmentId;
	
	@DatabaseField
	private int   eventType;
	
	@DatabaseField
	private int   feelType;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] note; 
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	private byte[] ecg;

	public EcgSegment() {
		setTag(TAG);
	}

	public byte[] getEcgSegment() {
		return ecg;
	}
	
	
	
	public byte[] getNote() {
		return note;
	}

	public void setNote(byte[] note) {
		this.note = note;
	}

	public byte[] getEventBytes(){
		byte[] events = new byte[5];
		events[0] =  (byte) (eventType>>24);
		events[1] =  (byte) (eventType>>16);
		events[2] =  (byte) (eventType>>8 );
		events[3] =  (byte) (eventType    );
		events[4] =  (byte) (feelType    );
		return events;
	}
	
	public void setRecordId(short id)
	{
		recordUId = id;
	}
	
	public short getRecordUId(){
		return recordUId;
	}
	
	public void setSegmentId(short segmentId){
		this.segmentId = segmentId;
	}
	
	
	
	public short getSegmentId()
	{
		return segmentId;
	}
	public void setEventType(int type){
		eventType = type;
	}
	public int getEventType(){
		return eventType;
	}
	
	public void setFeelType(int feel){
		feelType = feel;
	}
	
	public void setEcgSegments(byte[] ecg) {
		int len = Math.min(ecg.length, BYTE_LEN);
		this.ecg = new byte[len];
		System.arraycopy(ecg, 0, this.ecg, 0, len);
	}

	public int getChanel(){
		if(ecg.length<16000)
			return 1;
		else if(ecg.length<31000)
			return 2;
		else
			return 3;

	}
	
	public byte[] getEcgBytes(){
		return ecg;
	}
	
	public List<EcgSlice> toSlice(int chanel, int sliceLen){
		int count    = sliceLen / chanel;
		int timeGap  = count * 4;
		int len = ecg.length;
		int timeStart = segmentId*30*1000;
		int sliceCount = 0;
		List<EcgSlice> slices = new ArrayList<EcgSlice>();
		for(int i = 0; i<len; i+= sliceLen*2,sliceCount++){
			int time = EcgTimeStamp.getTimeStampByGap(timeStart + sliceCount*timeGap);
			EcgSlice slice = new EcgSlice();
			slice.chanel = (byte) chanel;
			slice.timeStamp = time;
			slice.ecgChanel = new short[sliceLen];
			for(int j= 0; j<sliceLen;j++){
				slice.ecgChanel[j] = (short) IOUtils.byteArrayToShort2(ecg, i+j*2);
			}
			slices.add(slice);
		}
		return slices;
	}
	
	
	
	
	public static List<EcgSegment> createEcgSegment(JSONArray jsonArray, Account account){
		
		List<EcgSegment> retList = new ArrayList<EcgSegment>();
		
		try{
		
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			retList.add(createRead(jsonObj, account));
		}
		
		}catch(JSONException e){
			e.printStackTrace();
		}
		return retList;
	}
	

	public static EcgSegment createRead(JSONObject jo, Account account) {
        EcgSegment segment = new EcgSegment();
        segment.setBelongAccount(account);
		return parse(jo, segment);
	}

	/**
	 * 进行数据解析
	 * @param jo
	 * @param segment
	 * @return
	 */
	private static EcgSegment parse(JSONObject jo, EcgSegment segment) {
		
		//进行segment解析
		try{
			
			if(jo.has("segmentid") && !jo.isNull("segmentid")){
				segment.setSegmentId(Short.valueOf(jo.getString("segmentid")));
			}
			if(jo.has("event") && !jo.isNull("event")){
				byte[] events = Base64.decode(jo.getString("event"), Base64.DEFAULT);
//				events[0] =  (byte) (eventType>>24);
//				events[1] =  (byte) (eventType>>16);
//				events[2] =  (byte) (eventType>>8 );
//				events[3] =  (byte) (eventType    );
//				events[4] =  (byte) (feelType    );
				segment.eventType = events[0]<<24 + events[1]<<16+events[2]<<8+events[3];
				segment.feelType = events[4];
			}
			
			if(jo.has("wave") && !jo.isNull("wave") ){
				segment.ecg = Base64.decode(jo.getString("wave"), Base64.DEFAULT);
			}
			
			if(jo.has("voice") && !jo.isNull("voice")){
				segment.note = Base64.decode(jo.getString("voice"), Base64.DEFAULT);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		segment.setStateFlag(STATE_SYNCHRONIZED);
		return segment;
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
	public String getMeasureName() {
		return TAG;
	}

	@Override
	public void toMap(Map<String, String> result) {

	}
}
