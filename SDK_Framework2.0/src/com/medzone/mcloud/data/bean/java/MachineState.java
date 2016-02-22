package com.medzone.mcloud.data.bean.java;
import com.medzone.mcloud.util.IOUtils;

public class MachineState{
	public int filterTimeStamp;
	public int timeStamp;
	public int startTime;
	public int currentRecord;
	public byte channelMode;
	public int power;
	
	public static MachineState read(byte[] params, int offset) {
		if ((params.length < 4)
				|| (params.length > 4 && (params.length - offset) < 4))
			return null;

		MachineState data = new MachineState();
		data.filterTimeStamp = IOUtils.byteArrayToInt(params, offset);
		offset += 4;
		data.timeStamp = IOUtils.byteArrayToInt(params, offset);
		offset += 4;
		data.startTime = IOUtils.byteArrayToInt(params, offset);
		offset += 4;
		data.currentRecord = IOUtils.byteArrayToShort(params, offset);
		offset += 2;
		data.channelMode = (byte) ((params[offset] & 0x70) >> 4);
		offset += 1;
		data.power = params[offset];
		offset += 1;
		
		return data;
	}
}
