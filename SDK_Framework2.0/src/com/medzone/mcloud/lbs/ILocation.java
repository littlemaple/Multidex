package com.medzone.mcloud.lbs;

import java.util.HashMap;

// Application level
public interface ILocation<T> {

	/**
	 * 定位信息初始化
	 */
	public void init();

	/**
	 * 定位回调监听器接口初始化
	 * 
	 * @return
	 */
	public T initLocationImpl();

	/**
	 * 开始定位
	 */
	public void start();

	/**
	 * 获取最新的定位信息
	 * 
	 * @return
	 */
	public HashMap<String, ?> getLocationParams();

	/**
	 * 停止定位
	 */
	public void stop();

	/**
	 * 释放定位信息
	 */
	public void unInit();

}
