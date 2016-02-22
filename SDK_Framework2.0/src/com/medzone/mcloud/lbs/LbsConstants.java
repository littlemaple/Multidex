package com.medzone.mcloud.lbs;

public class LbsConstants {

	static final int			LBS_BASE							= 5000;
	/** 61：GPS定位结果 */
	public static final int		LBS_GPS_RESULT_OK					= LBS_BASE + 1;
	/** 62：扫描整合定位依据失败。此时定位结果无效。 */
	public static final int		LBS_GPS_RESULT_FAILED				= LBS_BASE + 2;
	/** 63：网络异常，没有成功向服务器发起请求。此时定位结果无效。 */
	public static final int		LBS_NET_RESULT_FAILED				= LBS_BASE + 3;
	/** 65： 定位缓存的结果 */
	public static final int		LBS_RESULT_CACHE_OK					= LBS_BASE + 4;
	/** 66：离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果 */
	public static final int		LBS_OFFLINE_RESULT_OK				= LBS_BASE + 5;
	/** 67：离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果 */
	public static final int		LBS_RESULT_OFFLINE_FAILED			= LBS_BASE + 6;
	/** 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果 */
	public static final int		LBS_NET_RESULT_OFFLINE_OK			= LBS_BASE + 7;
	public static final int		LBS_NO_PERMISSION					= LBS_BASE + 8;

	/** 161： 表示网络定位结果 */
	public static final int		LBS_NET_RESULT_OK					= LBS_BASE + 100;
	/** 162-167： 服务端失败 */
	public static final int		LBS_SERVER_FAILED					= LBS_BASE + 101;
	/** 502：key参数错误 */
	public static final int		LBS_SERVER_KEY_PROVIDE_ERROR		= LBS_BASE + 102;
	/** 504：key不存在或者非法 */
	public static final int		LBS_SERVER_KEY_PROVIDE_ILLEGAL		= LBS_BASE + 103;
	/** 601：key服务被开发者自己禁用 */
	public static final int		LBS_SERVER_KEY_PROVIDE_DISABLE		= LBS_BASE + 104;
	/** 602：key code 不匹配 */
	public static final int		LBS_SERVER_KEY_PROVIDE_NOT_MATCH	= LBS_BASE + 105;
	/** 507-700，其他服务端异常，优先监测其他异常，当其他异常都不满足的情况下，检查该异常 */
	public static final int		LBS_SERVER_OTHER_EXCEPTION			= LBS_BASE + 106;

	// public static final int LBS_PASSIVE_RESULT_OK = LBS_BASE + 200;

	public static final String	LOCATION_LATITUDE					= "location_latitude";
	public static final String	LOCATION_LONTITUDE					= "location_lontitude";
	public static final String	LOCATION_ALTITUDE					= "location_altitude";
	public static final String	LOCATION_RADIUS						= "location_radius";
	public static final String	LOCATION_ADDRESS					= "location_address";
	public static final String	LOCATION_TIME						= "location_time";
	public static final String	LOCATION_ORGIN_CODE					= "location_orgin_code";
	public static final String	LOCATION_LOC_CODE					= "location_loc_code";

}
