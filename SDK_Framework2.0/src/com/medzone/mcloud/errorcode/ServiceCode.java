package com.medzone.mcloud.errorcode;

import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud_framework.R.string;

class ServiceCode extends AbstractCloudStausCode {

	public ServiceCode() {
		super();
	}

	@Override
	protected void initCodeCollect() {
		super.initCodeCollect();
		errorCodeMap.put(LocalCode.CODE_14101, string.SERVICE_CODE_14101);
		errorCodeMap.put(LocalCode.CODE_14102, string.SERVICE_CODE_14102);
		errorCodeMap.put(LocalCode.CODE_14103, string.SERVICE_CODE_14103);
		errorCodeMap.put(LocalCode.CODE_42901, string.SERVICE_CODE_42901);
	}
}
