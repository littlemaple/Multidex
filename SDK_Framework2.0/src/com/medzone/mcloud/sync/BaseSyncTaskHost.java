package com.medzone.mcloud.sync;

import java.util.List;

public class BaseSyncTaskHost<T> {

	/**
	 * 解析服务器端数据后返回结果
	 */
	public void onPostExecuteCacheChanged(List<T> list) {
	}
}
