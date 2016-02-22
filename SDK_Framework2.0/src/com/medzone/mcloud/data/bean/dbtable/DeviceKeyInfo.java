package com.medzone.mcloud.data.bean.dbtable;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author ChenJunQi.
 * 
 */

public class DeviceKeyInfo {
	public static final String DEVICE_TAG = "key";
	public static final String NAME_FIELD_DEVICE_ADDR = "deviceAddr";
	public static final String NAME_FIELD_DEVICE_KEY = "devKey";

	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField
	private String deviceAddr;
	@DatabaseField
	private String devKey;

	public DeviceKeyInfo() {
	}

	public String getDeviceAddr() {
		return deviceAddr;
	}

	public void setDeviceAddr(String deviceAddr) {
		this.deviceAddr = deviceAddr;
	}

	public String getDevKey() {
		return devKey;
	}

	public void setDevKey(String devKey) {
		this.devKey = devKey;
	}

}
