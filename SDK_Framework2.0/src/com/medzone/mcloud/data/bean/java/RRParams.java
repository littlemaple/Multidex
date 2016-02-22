package com.medzone.mcloud.data.bean.java;

import com.medzone.mcloud.util.IOUtils;

public class RRParams {
	private static final int RR_PARAM_LEN = 20;
	public int timeStamp;
	public float QRSTimes;
	public float ventricularRate;
	public float RRIntervalAvg;
	public float RRIntervalMax;
	public float RRIntervalMin;
	public float maxMinIndex;
	public float standDev;
	public float variationCoeff;
	public short prematureBeat;

	public static RRParams read(byte[] params, int offset, int len) {
		if ((params.length < RR_PARAM_LEN)
				|| ((params.length - offset) > RR_PARAM_LEN && len != RR_PARAM_LEN))
			return null;

		RRParams data = new RRParams();

		data.timeStamp = 0;//IOUtils.byteArrayToShort(params, offset + 0) << 16;
		data.QRSTimes = IOUtils.byteArrayToInt(params, offset + 0) / 1000.0f;
		data.ventricularRate = IOUtils.byteArrayToShort(params, offset + 4);
		data.RRIntervalAvg = IOUtils.byteArrayToShort(params, offset + 6) / 1000.0f;
		data.RRIntervalMax = IOUtils.byteArrayToShort(params, offset + 8) / 1000.0f;
		data.RRIntervalMin = IOUtils.byteArrayToShort(params, offset + 10) / 1000.0f;
		data.maxMinIndex = IOUtils.byteArrayToShort(params, offset + 12) / 1000.0f;
		data.standDev = IOUtils.byteArrayToShort(params, offset + 14) / 1000.0f;
		data.variationCoeff = IOUtils.byteArrayToShort(params, offset + 16) / 1000.0f;
		data.prematureBeat = IOUtils.byteArrayToShort(params, offset + 18);
		return data;
	}

	public static byte[] write(RRParams obj) {
		byte[] data = new byte[22];
		// IOUtils.intToByteArray(obj.QRSTimes, data, 0);
		// IOUtils.intToByteArray(obj.ventricularRate, data, 4);
		// IOUtils.intToByteArray(obj.RRIntervalAvg, data, 8);
		// IOUtils.intToByteArray(obj.RRIntervalMax, data, 12);
		// IOUtils.intToByteArray(obj.RRIntervalMin, data, 16);
		// IOUtils.intToByteArray(obj.maxMinIndex, data, 20);
		// IOUtils.intToByteArray(obj.standDev, data, 24);
		// IOUtils.intToByteArray(obj.variationCoeff, data, 28);
		// IOUtils.intToByteArray(obj.prematureBeat, data, 32);
		return data;
	}

	@Override
	public String toString() {
		StringBuffer text = new StringBuffer();
		text.append("时间戳        " + (timeStamp >> 16) + "\r\n");
		text.append("QRS次数        " + QRSTimes + "\r\n");
		text.append("室率              " + ventricularRate + "\r\n");
		text.append("平均R-R期间 " + RRIntervalAvg + "\r\n");
		text.append("最大R-R期间 " + RRIntervalMax + "\r\n");
		text.append("最小R-R期间 " + RRIntervalMin + "\r\n");
		text.append("最大/最小     " + maxMinIndex + "\r\n");
		text.append("标准偏差       " + standDev + "\r\n");
		text.append("变动系数       " + variationCoeff + "\r\n");
		text.append("早搏类型       " + prematureBeat + "\r\n");
		return text.toString();
	}

}
