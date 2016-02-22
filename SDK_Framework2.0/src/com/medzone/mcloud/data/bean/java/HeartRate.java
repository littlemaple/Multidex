package com.medzone.mcloud.data.bean.java;
import com.medzone.mcloud.util.EcgTimeStamp;
import com.medzone.mcloud.util.IOUtils;


public class HeartRate {
	public int timeStamp;
	public short heartRate;
	public boolean electrodeDrop;
	public boolean powerLow;
	public boolean heartOK;
	public boolean hasEvent;
	public byte    step;

	public static HeartRate read(byte[] params, int offset) {
		if ((params.length < 2)
				|| (params.length > 2 && (params.length - offset) < 2))
			return null;

		HeartRate data = new HeartRate();
		// data.timeStamp = params[offset]<<24 + params[offset+1]<<16 +
		// params[offset+2]<<8;
		offset += 0;
		short low = params[offset + 1];
		short high = (short) (params[offset] & 0x01);
		data.heartRate = (short) (high * 256 + low);
		data.electrodeDrop = ((params[offset] & 0x80) > 0);
		data.powerLow = ((params[offset] & 0x40) > 0);
		data.heartOK = ((params[offset] & 0x20) > 0);
		data.hasEvent = ((params[offset] & 0x10) > 0);
		data.step = 1;
		return data;
	}

	public static byte[] write(HeartRate obj) {
		byte[] data = new byte[5];
		data[0] = (byte) obj.heartRate;
		if (obj.heartRate > 255)
			data[1] |= 0x1;
		if (obj.electrodeDrop)
			data[1] |= 0x2;
		if (obj.powerLow)
			data[1] |= 0x4;

		return data;
	}

	public static HeartRate[] readArray(byte[] params, int offset, int len) {
		int datalen = params.length > len ? len : params.length;
		int size = (datalen - 4) >> 1;
		if (size < 0)
			return null;
		
		
		int timestamp = IOUtils.byteArrayToInt(params, offset);
		timestamp &= 0xFFFFFF00;
		offset += 3;
		long gap       = EcgTimeStamp.getGapByTimeStamp(timestamp);
		byte step = params[offset];
		offset ++;
		
		

		int realSize = 0;
		for (int i=0; i<size; i++, realSize ++){
			short low = params[offset + i*2 + 1];
			short high= params[offset + i*2];
			if (low == -1 && high == -1)
				break;
		}
		
		HeartRate heartRates[] = new HeartRate[realSize];
		for (int i=0; i<realSize; i++){
			short low =  (short) ((params[offset + 1] + 256)%256);
			short high = (short) (params[offset ] & 0x01);
			
			HeartRate hr = new HeartRate();
			hr.heartRate = (short) (high * 256 + low);
			hr.timeStamp = EcgTimeStamp.getTimeStampByGap(gap + 1000*step*i);
			hr.step      = step;
			heartRates[i]  = hr;
			offset += 2;
		}

		return heartRates;
	}
	
	public static short getHeartRate(byte[] params, int offset){
		short low =  (short) ((params[offset + 1] + 256)%256);
		short high = (short) (params[offset ] & 0x01);
		
		short heartRate = (short) (high * 256 + low);
		return heartRate;
	}
	
	public static HeartRate[] readTotalRecord(byte[] params, int len) {
		int datalen = params.length > len ? len : params.length;
		int size = (datalen) >> 1;
		if (size < 0)
			return null;
		
		
		int timestamp = 0;
		long gap      = EcgTimeStamp.getGapByTimeStamp(timestamp);
		byte step 	  = 60;
		int  offset   = 0;
		int realSize  = size;
		HeartRate heartRates[] = new HeartRate[realSize];
		for (int i=0; i<realSize; i++){
			short low =  (short) ((params[offset + 1] + 256)%256);
			short high = (short) (params[offset ] & 0x01);
			
			HeartRate hr = new HeartRate();
			hr.heartRate = (short) (high * 256 + low);
			hr.timeStamp = EcgTimeStamp.getTimeStampByGap(gap + 1000*step*i);
			hr.step      = step;
			heartRates[i]  = hr;
			offset += 2;
		}

		return heartRates;
	}

	public static byte[] writeArray(HeartRate[] data) {
		byte[] params = new byte[(data.length * 9 + 7) / 8];

		for (int i = 0; i < data.length; i++) {
			short heartRate = data[i].heartRate;
			int byteIndex = ((i + 1) * 9) / 8 - 1;
			int bRemain = ((i + 1) * 9) % 8;
			byte high = (byte) ((heartRate & 0x1FF) >> bRemain);
			byte low = (byte) ((heartRate << (32 - bRemain)) >>> (32 - bRemain));
			params[byteIndex] |= high;
			params[byteIndex + 1] |= low << (8 - bRemain);
		}

		return params;
	}

	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		text.append("ʱ���      " + (timeStamp >> 16) + "\r\n");
		text.append("����         " + heartRate + "\r\n");
		text.append("����         " + electrodeDrop + "\r\n");
		text.append("�͵� "     + powerLow + "\r\n");
		text.append("�¼�"      + hasEvent + "\r\n");
		return text.toString();
	}
}
