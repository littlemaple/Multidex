package com.medzone.mcloud.errorcode;

import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.NetCode;
import com.medzone.mcloud_framework.R.string;

class SettingCode extends AbstractCloudStausCode {

	public SettingCode() {
		super();
	}

	@Override
	protected void initCodeCollect() {
		super.initCodeCollect();
		errorCodeMap.put(NetCode.CODE_40700, string.SET_CODE_40700);
		errorCodeMap.put(NetCode.CODE_40701, string.SET_CODE_40701);
		errorCodeMap.put(NetCode.CODE_40702, string.SET_CODE_40702);
		errorCodeMap.put(NetCode.CODE_40703, string.SET_CODE_40703);
		errorCodeMap.put(NetCode.CODE_40704, string.SET_CODE_40704);
		errorCodeMap.put(NetCode.CODE_40705, string.SET_CODE_40705);

		errorCodeMap.put(LocalCode.CODE_NICKNAME_EMPTY, string.SET_CODE_NICKNAME_EMPTY);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_ILLAGE, string.SET_CODE_NICKNAME_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_TOO_LONG, string.SET_CODE_NICKNAME_TOO_LONG);

		errorCodeMap.put(LocalCode.CODE_REALNAME_EMPTY, string.SET_CODE_REALNAME_EMPTY);
		errorCodeMap.put(LocalCode.CODE_REALNAME_TOO_LONG, string.SET_CODE_REALNAME_TOO_LONG);
		errorCodeMap.put(LocalCode.CODE_REALNAME_ILLAGE, string.SET_CODE_REALNAME_ILLAGE);

		errorCodeMap.put(LocalCode.CODE_13105, string.SET_CODE_13105);
		errorCodeMap.put(LocalCode.CODE_13106, string.SET_CODE_13106);

		errorCodeMap.put(LocalCode.CODE_INFO_BIND_PHONE_NULL, string.SET_CODE_INFO_BIND_PHONE_NULL);
		errorCodeMap.put(LocalCode.CODE_13107, string.SET_CODE_13107);

		errorCodeMap.put(LocalCode.CODE_INFO_BIND_IDCRAD_NULL, string.SET_CODE_INFO_BIND_IDCRAD_NULL);
		errorCodeMap.put(LocalCode.CODE_INFO_BIND_IDCRAD_ILLAGE, string.SET_CODE_INFO_BIND_IDCRAD_ILLAGE);

		errorCodeMap.put(LocalCode.CODE_13109, string.SET_CODE_13109);
		errorCodeMap.put(LocalCode.CODE_13110, string.SET_CODE_13110);
		errorCodeMap.put(LocalCode.CODE_13111, string.SET_CODE_13111);
		errorCodeMap.put(LocalCode.CODE_13112, string.SET_CODE_13112);

		errorCodeMap.put(LocalCode.CODE_13201, string.SET_CODE_13201);
		errorCodeMap.put(LocalCode.CODE_RESET_NEW_PASSWORD_NULL, string.SET_CODE_RESET_NEW_PASSWORD_NULL);
		errorCodeMap.put(LocalCode.CODE_13202, string.SET_CODE_13202);
		errorCodeMap.put(LocalCode.CODE_RESET_NEW_PASSWORD_SUCCESS, string.SET_CODE_RESET_NEW_PASSWORD_SUCCESS);
		errorCodeMap.put(LocalCode.CODE_PASSWORD_ILLAGE, string.SET_CODE_PASSWORD_ILLAGE);

		errorCodeMap.put(LocalCode.CODE_13204, string.SET_CODE_13204);
		errorCodeMap.put(LocalCode.CODE_13205, string.SET_CODE_13205);

	}
}
