package com.medzone.mcloud.errorcode;

import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.NetCode;
import com.medzone.mcloud_framework.R.string;

class ContactCode extends AbstractCloudStausCode {

	public ContactCode() {
		super();
	}

	@Override
	protected void initCodeCollect() {
		super.initCodeCollect();

		errorCodeMap.put(LocalCode.CODE_12304, string.CONTACT_CODE_12304);
		errorCodeMap.put(LocalCode.CODE_ACCOUNT_EMPTY, string.CONTACT_CODE_ACCOUNT_EMPTY);
		errorCodeMap.put(LocalCode.CODE_ACCOUNT_ILLAGE, string.CONTACT_CODE_ACCOUNT_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_EMPTY, string.CONTACT_CODE_NICKNAME_EMPTY);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_TOO_LONG, string.CONTACT_CODE_NICKNAME_TOO_LONG);
		errorCodeMap.put(LocalCode.CODE_NICKNAME_ILLAGE, string.CONTACT_CODE_NICKNAME_ILLAGE);
		errorCodeMap.put(LocalCode.CODE_CONTACT_LIMIT, string.CONTACT_CODE_CONTACT_LIMIT);
		errorCodeMap.put(LocalCode.CODE_CONTACT_PHONE_IS_TIED, string.CONTACT_CODE_CONTACT_PHONE_IS_TIED);
		errorCodeMap.put(LocalCode.CODE_REMARK_TOO_LONG, string.CONTACT_CODE_REMARK_TOO_LONG);
		errorCodeMap.put(LocalCode.CODE_REMARK_ILLEGAL, string.CONTACT_CODE_REMARK_ILLEGAL);

		errorCodeMap.put(NetCode.CODE_40000, string.CONTACT_CODE_40000);
		errorCodeMap.put(NetCode.CODE_40600, string.CONTACT_CODE_40600);
		errorCodeMap.put(NetCode.CODE_40700, string.CONTACT_CODE_40700);
		errorCodeMap.put(NetCode.CODE_40701, string.CONTACT_CODE_40701);
		errorCodeMap.put(NetCode.CODE_40702, string.CONTACT_CODE_40702);
		errorCodeMap.put(NetCode.CODE_40703, string.CONTACT_CODE_40703);
		errorCodeMap.put(NetCode.CODE_40704, string.CONTACT_CODE_40704);
		errorCodeMap.put(NetCode.CODE_40705, string.CONTACT_CODE_40705);
		errorCodeMap.put(NetCode.CODE_40706, string.CONTACT_CODE_40706);
		errorCodeMap.put(NetCode.CODE_40707, string.CONTACT_CODE_40707);
		errorCodeMap.put(NetCode.CODE_40900, string.CONTACT_CODE_40900);
		errorCodeMap.put(NetCode.CODE_41000, string.CONTACT_CODE_41000);
		errorCodeMap.put(NetCode.CODE_41001, string.CONTACT_CODE_41001);
		errorCodeMap.put(NetCode.CODE_41002, string.CONTACT_CODE_41002);
		errorCodeMap.put(NetCode.CODE_41100, string.CONTACT_CODE_41100);
		errorCodeMap.put(NetCode.CODE_41101, string.CONTACT_CODE_41101);
		errorCodeMap.put(NetCode.CODE_41003, string.CONTACT_CODE_41003);
		errorCodeMap.put(NetCode.CODE_41004, string.CONTACT_CODE_41004);
		errorCodeMap.put(NetCode.CODE_41005, string.CONTACT_CODE_41005);
		errorCodeMap.put(NetCode.CODE_41200, string.CONTACT_CODE_41200);
		errorCodeMap.put(NetCode.CODE_41201, string.CONTACT_CODE_41201);
		errorCodeMap.put(NetCode.CODE_42901, string.CONTACT_CODE_42901);
		errorCodeMap.put(NetCode.CODE_42902, string.CONTACT_CODE_42902);
		errorCodeMap.put(NetCode.CODE_43800, string.CONTACT_CODE_43800);
		errorCodeMap.put(NetCode.CODE_43801, string.CONTACT_CODE_43801);
		errorCodeMap.put(NetCode.CODE_43802, string.CONTACT_CODE_43802);
		errorCodeMap.put(NetCode.CODE_43803, string.CONTACT_CODE_43803);
		errorCodeMap.put(NetCode.CODE_43804, string.CONTACT_CODE_43804);
		errorCodeMap.put(NetCode.CODE_43805, string.CONTACT_CODE_43805);
		errorCodeMap.put(NetCode.CODE_44600, string.CONTACT_CODE_44600);
		errorCodeMap.put(NetCode.CODE_44601, string.CONTACT_CODE_44601);
		errorCodeMap.put(NetCode.CODE_44700, string.CONTACT_CODE_44700);
		errorCodeMap.put(NetCode.CODE_44701, string.CONTACT_CODE_44701);

	}
}
