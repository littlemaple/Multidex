package com.medzone.mcloud.errorcode;

import android.util.SparseArray;

import com.medzone.framework.data.errorcode.CodeProxy;
import com.medzone.framework.data.errorcode.ICode;
import com.medzone.framework.data.errorcode.IntStatusCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.NetCode;
import com.medzone.mcloud_framework.R.string;

/**
 * 
 * <p>
 * 1) .处理错误码以及错误提示信息的映射关系
 * </p>
 * <p>
 * 2) .需要注意这边同一错误码可能对应多份错误信息。（目前场景是分模块去呈现）
 * </p>
 * <p>
 * 3) .另外错误码应该允许被客户定制，即客户端可以选择不使用我们推荐的错误信息去呈现。（兼容同一模块内，也出现不同的错误提示信息极少数情况）
 * </p>
 * 
 * @author junqi
 * 
 */
public abstract class AbstractCloudStausCode extends IntStatusCode {

	protected void initCodeCollect() {
		super.initCodeCollect();
		errorCodeMap.put(CodeProxy.CODE_UNDEFINED, string.STATUS_CODE_UNDEFINED);
		errorCodeMap.put(CodeProxy.CODE_REQUEST_FAILED, string.STATUS_CODE_REQUEST_FAILED);
		errorCodeMap.put(CodeProxy.CODE_10000, string.STATUS_CODE_10000);
		errorCodeMap.put(CodeProxy.CODE_10001, string.STATUS_CODE_10001);
		errorCodeMap.put(CodeProxy.CODE_10002, string.STATUS_CODE_10002);
		errorCodeMap.put(CodeProxy.CODE_10003, string.STATUS_CODE_10003);
		errorCodeMap.put(CodeProxy.CODE_10004, string.STATUS_CODE_10004);
		errorCodeMap.put(CodeProxy.CODE_10005, string.STATUS_CODE_10005);

		errorCodeMap.put(NetCode.CODE_40000, string.SHARE_CODE_40000);
		errorCodeMap.put(NetCode.CODE_40001, string.SHARE_CODE_40001);
		errorCodeMap.put(NetCode.CODE_40002, string.SHARE_CODE_40002);
		errorCodeMap.put(NetCode.CODE_50000, string.SHARE_CODE_50000);
		errorCodeMap.put(NetCode.CODE_50001, string.SHARE_CODE_50001);
		// ====分享===//
		errorCodeMap.put(LocalCode.CODE_SHARE_COMPLETE, string.SHARE_CODE_SHARE_COMPLETE);
		errorCodeMap.put(LocalCode.CODE_SHARE_CANCEL, string.SHARE_CODE_SHARE_CANCEL);
		errorCodeMap.put(LocalCode.CODE_SHARE_FAIL, string.SHARE_CODE_SHARE_FAIL);
		errorCodeMap.put(LocalCode.CODE_WECHAT_CLIENT_INVALID, string.SHARE_CODE_WECHAT_CLIENT_INVALID);
		errorCodeMap.put(LocalCode.CODE_WECHAT_CLIENT_NOT_SUPPORTED, string.SHARE_CODE_WECHAT_CLIENT_NOT_SUPPORTED);
		errorCodeMap.put(LocalCode.CODE_MAIL_DATA_MISSING, string.SHARE_CODE_MAIL_DATA_MISSING);
		errorCodeMap.put(LocalCode.CODE_SHARE_DATA_MISSING, string.SHARE_CODE_SHARE_DATA_MISSING);
		errorCodeMap.put(LocalCode.CODE_SEND_MAIL_SUCCESS, string.SHARE_CODE_SEND_MAIL_SUCCESS);
		errorCodeMap.put(LocalCode.CODE_ADDRESS_ERROR, string.SHARE_CODE_ADDRESS_ERROR);
		errorCodeMap.put(LocalCode.CODE_ADDRESS_NULL, string.SHARE_CODE_ADDRESS_NULL);
		errorCodeMap.put(LocalCode.CODE_SIM_INVALIABLE, string.SHARE_CODE_SIM_INVALIABLE);

	}

	@Override
	protected boolean isContainsKey(int type) {
		CloudStatusCodeProxy proxy = (CloudStatusCodeProxy) CloudStatusCodeProxy.getInstance();
		SparseArray<ICode<Integer>> exceptionArrs = proxy.getCodes();
		return exceptionArrs != null && exceptionArrs.indexOfKey(type) >= 0;
	}
}
