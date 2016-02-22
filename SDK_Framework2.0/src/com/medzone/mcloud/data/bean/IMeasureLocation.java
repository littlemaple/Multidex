package com.medzone.mcloud.data.bean;

import java.util.HashMap;

public interface IMeasureLocation {

	// 心云Key
	public static final String	LOCATION_LATITUDE	= "location_latitude";
	public static final String	LOCATION_LONTITUDE	= "location_lontitude";
	public static final String	LOCATION_ALTITUDE	= "location_altitude";
	public static final String	LOCATION_RADIUS		= "location_radius";
	public static final String	LOCATION_ADDRESS	= "location_address";
	public static final String	LOCATION_TIME		= "location_time";
	public static final String	LOCATION_ORGIN_CODE	= "location_orgin_code";

	public void setLocation(HashMap<String, ?> map);

}
