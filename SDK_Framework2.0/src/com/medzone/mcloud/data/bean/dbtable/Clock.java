package com.medzone.mcloud.data.bean.dbtable;

import java.util.Random;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;

/**
 * 目前先不放到framework中
 * 
 */
public class Clock extends BaseIdDatabaseObject {

	private static final long	serialVersionUID			= 9147843619546107034L;

	public static final String	TAG							= "health_clock";
	public static final String	NAME_FIELD_TYPE				= "type";
	public static final String	NAME_FIELD_LABEL			= "label";
	public static final String	NAME_FIELD_CLOCKTIME		= "clockTime";
	public static final String	NAME_FIELD_TIMEMILLIS		= "timemillis";
	public static final String	NAME_FIELD_REPETITION		= "repetition";
	public static final String	NAME_FIELD_SWITCHSTATE		= "switchState";
	public static final String	NAME_FIELD_ACCOUNT_ID		= "accountID";
	public static final String	NAME_FIELD_DEVICE_TYPE		= "deviceType";
	public static final String	NAME_FIELD_CLOCK_PRIMARY_ID	= "clock_primary_id";

	@DatabaseField
	private Integer				accountID;

	@DatabaseField
	private String				deviceType;

	@DatabaseField
	private String				type;

	@DatabaseField
	private String				label;

	@DatabaseField
	private String				clockTime;

	@DatabaseField
	private long				timemillis;										// 将clockTime
																					// 转成整型，目前只是为了排序HH:mm
																					// -->
																					// HHmm

	@DatabaseField
	private int					repetition;

	@DatabaseField
	private boolean				switchState;

	// @DatabaseField
	// private Boolean isUsed; // 应对单次闹钟的策略，设置的闹钟在当前时间之前

	public long getTimemillis() {
		return timemillis;
	}

	public void setTimemillis(long timemillis) {
		this.timemillis = timemillis;
	}

	public Integer getAccountID() {
		return accountID;
	}

	public void setAccountID(Integer accountID) {
		this.accountID = accountID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getClockID() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getClockTime() {
		return clockTime;
	}

	public void setClockTime(String clockTime) {
		this.clockTime = clockTime;
	}

	public int getRepetition() {
		return repetition;
	}

	public void setRepetition(int repetition) {
		this.repetition = repetition;
	}

	public boolean isSwitchState() {
		return switchState;
	}

	public void setSwitchState(boolean switchState) {
		this.switchState = switchState;
	}

	public boolean isSameRecord(Object o) {
		Clock item2 = (Clock) o;
		if (this.getClockID() != item2.getClockID()) {
			return false;
		}
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static class ClockUtil {
		public static String createID() {
			Random random = new Random();
			int next = random.nextInt(Integer.MAX_VALUE);
			random = null;
			return System.currentTimeMillis() + "0" + next;
		}
	}

	@Override
	public String toString() {
		return "id:" + getClockID() + ",time:" + getClockTime() + ",switchState:" + isSwitchState() + "\ndeviceType:" + getDeviceType() + ",accountId:" + getAccountID() + ",repetition:" + getRepetition();
	}
}
