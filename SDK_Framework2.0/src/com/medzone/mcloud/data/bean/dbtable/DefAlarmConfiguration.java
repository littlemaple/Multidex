package com.medzone.mcloud.data.bean.dbtable;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;

/**
 * 
 * @author Robert
 * @category 默认闹钟配置策略
 */
public class DefAlarmConfiguration extends BaseIdDatabaseObject {

	public static final String	FIELD_NAME_FOREIGN_ACCOUNT_ID	= "foreign_account_id";

	// TODO 待闹钟表接入后，变更为闹钟本地自增id
	public static final String	FIELD_NAME_CLOCK_ID				= "clockID";			// 闹钟本地id

	public static final String	FIELD_NAME_VERSION				= "version";			// 默认配置闹钟版本

	@DatabaseField(foreign = true, canBeNull = false, columnName = FIELD_NAME_FOREIGN_ACCOUNT_ID)
	private Account				account;

	@DatabaseField(canBeNull = false, columnName = FIELD_NAME_CLOCK_ID)
	private int					clockID;

	@DatabaseField(columnName = FIELD_NAME_VERSION)
	private int					version;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getClockID() {
		return clockID;
	}

	public void setClockID(int clockID) {
		this.clockID = clockID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
