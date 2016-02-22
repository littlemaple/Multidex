package com.medzone.mcloud.data.bean.java;

import android.util.Log;

import com.medzone.mcloud.util.EcgFileWriter;
import com.medzone.mcloud.util.EcgTimeStamp;
import com.medzone.mcloud.util.IOUtils;

public class EcgSlice{
	public static final int INTERVAL = 125;
	public static final int STEP = 2;
	public static final int MIN_LEN = 5 + 20;
	public int timeStamp;
	public byte mode;
	public byte speed;
	public byte accurcy;
	public byte chanel;
	public byte type;
	public short[] ecgChanel = null;
	public long tsReceived   = 0;
	public long tsBuffered   = 0;
	public long tsDisplayed  = 0;
	
	static private EcgFileWriter mDumper = new EcgFileWriter();

	public EcgSlice() {
		timeStamp = 0;
		mode = 0;
		accurcy = 1;
		chanel = 1;
	}

	public void initDefault() {
		ecgChanel = new short[INTERVAL];
		for (int i = 0; i < INTERVAL; i++)
			ecgChanel[i] = (short) (i);
	}

	public void init(int size) {
		ecgChanel = new short[size];
	}
	
	public static EcgSlice readVRG(int timestamp, byte[] params, int offset, int len) {
		if ((params.length < MIN_LEN)
				|| (params.length > MIN_LEN && len < MIN_LEN) || params.length<(offset+len))
			return null;

		EcgSlice data = new EcgSlice();
		data.timeStamp = timestamp;
		int spec = params[offset] & 0xFF;
		data.type = 1;
		data.mode = (byte) (spec >> 4);
		data.speed = (byte) ((spec & 0x08) >> 3);
		data.accurcy = (byte) ((spec & 0x04) >> 2);
		data.chanel = (byte) (spec & 0x03);
		int paramIndex = 1 + offset;
		final int total = Math.min(len / 2, INTERVAL);
		data.ecgChanel = new short[total];
		int min =  600000;
		int max = -600000;
		for (int i = 0; i < total; i++) {
			data.ecgChanel[i] = IOUtils.byteArrayToShort(params, paramIndex + 2* i);
			
			if (data.ecgChanel[i] > max)
				max = data.ecgChanel[i];
			else if(data.ecgChanel[i] < min)
				min = data.ecgChanel[i];
		}
		
		Log.v("SLICE", "ecg_slice_timestamp = [" + data.timeStamp + "]");
		//data.dump();

		return data;
	}
	
//	private static int index = 0;

	public static EcgSlice read(byte[] params, int offset, int len) {
		if ((params.length < MIN_LEN)
				|| (params.length > MIN_LEN && len < MIN_LEN))
			return null;
		
		boolean needDump = offset < 0;
		if(offset<0)
			offset = 0;

		EcgSlice data = new EcgSlice();
		data.type = 3;
		data.timeStamp = IOUtils.byteArrayToInt(params, offset);
		int spec = params[offset + 4] & 0xFF;
		data.mode = (byte) (spec >> 4);
		data.speed = (byte) ((spec & 0x08) >> 3);
		data.accurcy = (byte) ((spec & 0x04) >> 2);
		data.chanel = (byte) (spec & 0x03);
		int paramIndex = 5 + offset;
		final int total = Math.min((len - 5) / 2, INTERVAL);
		data.ecgChanel = new short[total];
		int min =  600000;
		int max = -600000;
		for (int i = 0; i < total; i++) {
			data.ecgChanel[i] = IOUtils.byteArrayToShort(params, paramIndex + 2
					* i);
			
			if (data.ecgChanel[i] > max)
				max = data.ecgChanel[i];
			else if(data.ecgChanel[i] < min)
				min = data.ecgChanel[i];
		}
		
		Log.v("SLICE", "ecg_slice_range = [" + min + ", " + max + "]");
		if(needDump)
			data.dump();
		return data;
	}

	public static byte[] write(EcgSlice data) {
		int len = getByteLen(data);
		byte[] params = new byte[len];
		com.medzone.mcloud.util.IOUtils.intToByteArray(data.timeStamp, params, 0);
		params[5] = (byte) ((data.mode << 4) | (data.speed << 3)
				| (data.accurcy << 2) | data.chanel);
		int paramIndex = 5;
		for (int i = 0; i < data.ecgChanel.length; i++) {
			params[paramIndex + 2 * i] = (byte) (data.ecgChanel[i] >> 8);
			params[paramIndex + 2 * i + 1] = (byte) (data.ecgChanel[i]);
		}
		return params;
	}

	public static int getByteLen(EcgSlice data) {
		int len = 0;
		if (data.ecgChanel != null) {
			len = 5 + data.ecgChanel.length * 2;
		}
		return len;
	}

	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		text.append("time  " + (timeStamp >> 8) + "\r\n");
		text.append("accurcy " + accurcy + "\r\n");
		text.append("mode" + mode + "\r\n");
		text.append("chanel   " + chanel + "\r\n");
		text.append("length" + ecgChanel.length + "\r\n");
		return text.toString();
	}
	
	private void dump()
	{
		if(type != 3)
			return;
		
//		if(mLastTS == timeStamp)
//			return;
//		mLastTS = timeStamp;
		
		
//		byte data[] = IOUtils.intToByteArray(timeStamp);
		int gap = (int) EcgTimeStamp.getGapByTimeStamp(timeStamp);
		mDumper.writeFile("\n Timestamp = " + (gap / 4) + "\r\n");
		if (chanel == 1) {
			for(int i=0; i< ecgChanel.length; i++){
				mDumper.writeFile("" + ecgChanel[i] + ",");// ((ecgChanel[i] & 0xFF00)>>8)+"," + (ecgChanel[i] & 0xFF)+"," );
			}
		}
		else {
			for(int i=0; i< ecgChanel.length; i+=3){
				mDumper.writeFile("" + ecgChanel[i] + ","+ ecgChanel[i+1] + ", "+ ecgChanel[i+2]+"\r");// ((ecgChanel[i] & 0xFF00)>>8)+"," + (ecgChanel[i] & 0xFF)+"," );
			}
		}
		
	}
}
