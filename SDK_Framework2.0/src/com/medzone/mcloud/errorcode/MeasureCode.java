package com.medzone.mcloud.errorcode;

import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.NetCode;
import com.medzone.mcloud_framework.R.string;

class MeasureCode extends AbstractCloudStausCode {

	public MeasureCode() {
		super();
	}

	@Override
	protected void initCodeCollect() {
		super.initCodeCollect();
		errorCodeMap.put(NetCode.CODE_41300, string.MEASURE_CODE_41300);
		errorCodeMap.put(NetCode.CODE_41301, string.MEASURE_CODE_41301);
		errorCodeMap.put(NetCode.CODE_41302, string.MEASURE_CODE_41302);
		errorCodeMap.put(NetCode.CODE_41400, string.MEASURE_CODE_41400);
		errorCodeMap.put(NetCode.CODE_41401, string.MEASURE_CODE_41401);
		errorCodeMap.put(NetCode.CODE_41500, string.MEASURE_CODE_41500);
		errorCodeMap.put(NetCode.CODE_41501, string.MEASURE_CODE_41501);
		errorCodeMap.put(NetCode.CODE_41502, string.MEASURE_CODE_41502);
		errorCodeMap.put(NetCode.CODE_41600, string.MEASURE_CODE_41600);
		errorCodeMap.put(NetCode.CODE_41700, string.MEASURE_CODE_41700);
		errorCodeMap.put(NetCode.CODE_41800, string.MEASURE_CODE_41800);
		errorCodeMap.put(NetCode.CODE_45102, string.MEASURE_CODE_45102);

		errorCodeMap.put(LocalCode.CODE_11401, string.MEASURE_CODE_11401);
		errorCodeMap.put(LocalCode.CODE_11403, string.MEASURE_CODE_11403);
		errorCodeMap.put(LocalCode.CODE_11404, string.MEASURE_CODE_11404);
		errorCodeMap.put(LocalCode.CODE_11405, string.MEASURE_CODE_11405);
		errorCodeMap.put(LocalCode.CODE_DATA_UPLOAD_FAILURE, string.MEASURE_CODE_DATA_UPLOAD_FAILURE);
		errorCodeMap.put(LocalCode.CODE_CANNOT_DISPLAY_SUGGEST, string.MEASURE_CODE_CANNOT_DISPLAY_SUGGEST);

		errorCodeMap.put(LocalCode.CODE_HEART_RATE_EMPTY, string.MEASURE_CODE_HEART_RATE_EMPTY);
		errorCodeMap.put(LocalCode.CODE_SYSTOLIC_PRESSURE_EMPTY, string.MEASURE_CODE_SYSTOLIC_PRESSURE_EMPTY);
		errorCodeMap.put(LocalCode.CODE_DIASTOLIC_PRESSURE_EMPTY, string.MEASURE_CODE_DIASTOLIC_PRESSURE_EMPTY);
		errorCodeMap.put(LocalCode.CODE_BLOOD_OXYGEN_EMPTY, string.MEASURE_CODE_BLOOD_OXYGEN_EMPTY);
		errorCodeMap.put(LocalCode.CODE_TEMPERATURE_EMPTY, string.MEASURE_CODE_TEMPERATURE_EMPTY);
		errorCodeMap.put(LocalCode.CODE_SYSTOLIC_LESS_THAN_DIASTOLIC, string.MEASURE_CODE_SYSTOLIC_LESS_THAN_DIASTOLIC);
		errorCodeMap.put(LocalCode.CODE_18100, string.MEASURE_CODE_18100);
		errorCodeMap.put(LocalCode.CODE_SOMEONT_ELSE_IS_FINISED, string.MEASURE_CODE_SOMEONT_ELSE_IS_FINISED);
		errorCodeMap.put(LocalCode.CODE_MEASUREMENT_FOR_OTHERS, string.MEASURE_CODE_MEASUREMENT_FOR_OTHERS);
		errorCodeMap.put(LocalCode.CODE_18101, string.MEASURE_CODE_18101);
		errorCodeMap.put(LocalCode.CODE_UPLOAD_OTHER_SUCCESS, string.MEASURE_CODE_UPLOAD_OTHER_SUCCESS);
		errorCodeMap.put(LocalCode.CODE_UPLOAD_SELF_SUCCESS, string.MEASURE_CODE_UPLOAD_SELF_SUCCESS);
		errorCodeMap.put(LocalCode.CODE_BLOOD_SUGAR_EMPTY, string.MEASURE_CODE_BLOOD_SUGAR_EMPTY);
		errorCodeMap.put(LocalCode.CODE_CORRUPT_MEDIA, string.MEASURE_CODE_CORRUPT_MEDIA);

	}
}
