package com.medzone.mcloud.data.bean.java;

import com.medzone.mcloud.background.util.IOUtils;


public class VRG {
	public EcgSlice slice;
	public int timeStamp;
	public int timeGap;
	public int event;
	public short heartRate;
	public boolean electrodeDrop;
	public boolean powerLow;
	public boolean hasEvent;
	public boolean hasNext;
	public boolean heartOK;
	public byte channelRecv;
	public byte channelMode;
	public int power;
	public int devDelay = 0;
	public int displayDelay = 0;
	public long tsReceived;

	public VRG(byte[] params, int offset, int len) {
		if ((params.length < 8)
				|| (params.length > 8 && (params.length - offset) < 8))
			return ;

		timeStamp = IOUtils.byteArrayToInt(params, offset);
		offset += 4;
		event = IOUtils.byteArrayToInt(params, offset);
		offset += 4;
		short low   = IOUtils.signedConvertToUnsigned(params[offset + 1]);
		short high = (short) (params[offset] & 0x1) ;
		heartRate = (short) ( low + high * 256);
		electrodeDrop = ((params[offset] & 0x80) > 0);
		powerLow =  ((params[offset] & 0x40) >0);
		heartOK = ((params[offset] & 0x20) > 0);
		hasEvent = ((params[offset] & 0x10) > 0);
		hasNext = ((params[offset] & 0x08) > 0);
		offset+=2;
		power = params[offset];
		offset += 1;
		electrodeDrop |= ((params[offset] & 0x80) > 0);
		channelMode = (byte) ((params[offset] & 0x70) >> 4);
		channelRecv = (byte) ((params[offset] & 0x07));
		offset += 1;
		slice = EcgSlice.readVRG(timeStamp, params, offset, 166);
	}

	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		text.append("time:" + (timeStamp >> 8) + "\r\n");
		text.append("heartRate " + heartRate + "\r\n");
		text.append("electrodeDrop" + electrodeDrop + "\r\n");
		text.append("powerLow " + powerLow + "\r\n");
		int type = event;
		text.append("event" + type + "\r\n");
		return text.toString();
	}
}
