package com.medzone.mcloud.util;

import java.util.HashMap;

import android.text.TextUtils;
import android.util.Log;

public class MultiClickUtil {

	public static HashMap<String, Long>	mHashMap		= new HashMap<String, Long>();
	private static long					intervalMillis	= 500;

	/**
	 * 过滤多次点击事件
	 * 
	 * @param key
	 *            给定key，UTIL会记录给定key的上次点击事件，如果给定key为空，则总是会执行event。
	 * @param event
	 */
	public static void doFilterMultiClick(String key, IMultiClickEvent event) {
		if (TextUtils.isEmpty(key)) {
			event.onValidEventRunning();
		}
		else {
			long curMillis = System.currentTimeMillis();
			long preMillis = 0;
			if (mHashMap.containsKey(key)) {
				preMillis = mHashMap.get(key);
			}
			if (Math.abs(curMillis - preMillis) > intervalMillis) {
				mHashMap.put(key, curMillis);
				event.onValidEventRunning();
			}
			else {
				Log.i("MultiClickUtil", "检测到：" + key + "频繁点击，自动过滤多余点击事件。");
			}
		}

	}

	public static void release() {
		mHashMap.clear();
	}

	public interface IMultiClickEvent {

		void onValidEventRunning();

	}
}
