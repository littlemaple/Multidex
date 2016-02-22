package com.medzone.mcloud.errorcode;

import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.NetCode;
import com.medzone.mcloud_framework.R.string;

class LoginRigisterCode extends AbstractCloudStausCode {

	public LoginRigisterCode() {
		super();
	}

	@Override
	protected void initCodeCollect() {
		super.initCodeCollect();
		errorCodeMap.put(NetCode.CODE_40100, string.LOG_CODE_40100);
		errorCodeMap.put(NetCode.CODE_40101, string.LOG_CODE_40101);
		errorCodeMap.put(NetCode.CODE_40102, string.LOG_CODE_40102);
		errorCodeMap.put(NetCode.CODE_40103, string.LOG_CODE_40103);
		errorCodeMap.put(NetCode.CODE_40104, string.LOG_CODE_40104);
		errorCodeMap.put(NetCode.CODE_40105, string.LOG_CODE_40105);
		errorCodeMap.put(NetCode.CODE_40106, string.LOG_CODE_40106);
		errorCodeMap.put(NetCode.CODE_40300, string.LOG_CODE_40300);

		errorCodeMap.put(NetCode.CODE_40301, string.LOG_CODE_40301);
		errorCodeMap.put(NetCode.CODE_40302, string.LOG_CODE_40302);
		errorCodeMap.put(NetCode.CODE_40303, string.LOG_CODE_40303);
		errorCodeMap.put(NetCode.CODE_40400, string.LOG_CODE_40400);
		errorCodeMap.put(NetCode.CODE_40401, string.LOG_CODE_40401);
		errorCodeMap.put(NetCode.CODE_40500, string.LOG_CODE_40500);
		errorCodeMap.put(NetCode.CODE_40501, string.LOG_CODE_40501);
		errorCodeMap.put(NetCode.CODE_40502, string.LOG_CODE_40502);
		errorCodeMap.put(NetCode.CODE_40503, string.LOG_CODE_40503);

		errorCodeMap.put(LocalCode.CODE_ACCOUNT_ILLAGE_PHONE, string.CODE_ACCOUNT_ILLAGE_PHONE);
		errorCodeMap.put(LocalCode.CODE_ACCOUNT_ILLAGE, string.LOG_CODE_ACCOUNT_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_10076, string.LOG_CODE_10076);
		errorCodeMap.put(LocalCode.CODE_10203, string.LOG_CODE_10203);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_TOO_LONG, string.LOG_CODE_NICKNAME_TOO_LONG);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_ILLAGE, string.LOG_CODE_NICKNAME_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_10206, string.LOG_CODE_10206);
		errorCodeMap.put(LocalCode.CODE_10304, string.LOG_CODE_10304);

		errorCodeMap.put(LocalCode.CODE_10305, string.LOG_CODE_10305);
		errorCodeMap.put(LocalCode.CODE_PASSWORD_ERROR, string.LOG_CODE_PASSWORD_ERROR);
		errorCodeMap.put(LocalCode.CODE_LOGIN_KICKED_ERROR, string.LOG_CODE_LOGIN_KICKED_ERROR);

		errorCodeMap.put(LocalCode.CODE_PASSWORD_ILLAGE, string.LOG_CODE_PASSWORD_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_10307, string.LOG_CODE_10307);
		errorCodeMap.put(LocalCode.CODE_PASSWORD_EMPTY, string.LOG_CODE_PASSWORD_EMPTY);
		errorCodeMap.put(LocalCode.CODE_ACCOUNT_EMPTY, string.LOG_CODE_ACCOUNT_EMPTY);
		errorCodeMap.put(LocalCode.CODE_EMAIL_EMPTY, string.LOG_CODE_EMAIL_EMPTY);
		errorCodeMap.put(LocalCode.CODE_PHONE_EMPTY, string.LOG_CODE_PHONE_EMPTY);
		errorCodeMap.put(LocalCode.CODE_10211, string.LOG_CODE_10211);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_EMPTY, string.LOG_CODE_NICKNAME_EMPTY);
		errorCodeMap.put(LocalCode.CODE_10212, string.LOG_CODE_10212);
		errorCodeMap.put(LocalCode.CODE_10213, string.LOG_CODE_10213);
		errorCodeMap.put(LocalCode.CODE_10312, string.LOG_CODE_10312);
		errorCodeMap.put(LocalCode.CODE_10214, string.LOG_CODE_10214);
		errorCodeMap.put(LocalCode.CODE_PHONE_ILLAGE, string.LOG_CODE_PHONE_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_EMAIL_ILLAGE, string.LOG_CODE_EMAIL_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_PASSWORD_INCONSISTENT, string.CODE_PASSWORD_INCONSISTENT);
	}

}
