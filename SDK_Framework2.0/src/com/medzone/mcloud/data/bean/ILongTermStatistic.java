package com.medzone.mcloud.data.bean;

import java.util.HashMap;

/**
 * 
 * @author Robert
 * 
 *         为特定某个对象产生的接口规范，是不具备通配意义的
 */
@Deprecated
public interface ILongTermStatistic {

	public static final String	TIME		= "time";
	public static final String	OXYGEN		= "oxygen";
	public static final String	RATE		= "rate";
	public static final String	RATE_MIN	= "rate_min";
	public static final String	RATE_MAX	= "rate_max";
	public static final String	OXYGEN_MIN	= "oxygen_min";
	public static final String	OXYGEN_MAX	= "oxygen_max";

	/**
	 * @attention have been filtered the illegal data which eq 0
	 * @attention have been converted to percent
	 * @param start
	 *            the initial scope (include)
	 * @param end
	 *            the final scope (not include)
	 * @param retentionNum
	 *            the decimal digits
	 * @return
	 */
	public double getScopeRate(double start, double end, Integer retentionNum);

	/**
	 * get the duration in the special scope
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public long getDuration(double start, double end);

	/**
	 * put the minimum oxygen ,rate and time into HashMap
	 * the key in map reference to the model in ILongTermStatistic
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMinimumInfo();

	/**
	 * 
	 * @param retentionNum
	 *            the decimal digits
	 * @return
	 */
	public double getRateAverate(Integer retentionNum);

	/**
	 * 
	 * @param retentionNum
	 *            the decimal digits
	 * @return
	 */
	public double getOxygenAverage(Integer retentionNum);

	public HashMap<String, Object> getOxygenExtremeRange();

	public HashMap<String, Object> getRateExtremeRange();
}
