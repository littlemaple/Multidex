package com.medzone.mcloud.util;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EcgTimeStamp {
	public static final int SEGMENT_STEP = 30 * 1000;		
	public static final int SECOND_STEP = 1000;				
	public static final int MILLI_SEC_STEP = 4;				

	public static int getTimeStampByGap(long gap) {
		long delta = gap;
		int first = (int)  (delta  / SEGMENT_STEP);  
		int second = (int) ((delta % SEGMENT_STEP) / SECOND_STEP);
		int third = (int)  (delta  % SECOND_STEP ) / MILLI_SEC_STEP;
		int result = ((first & 0xFFFF) << 16) | ((second & 0xFF) << 8) | (third & 0xFF);
		return result;
	}

	public static long getGapByTimeStamp(int ts) {
		int first = ts >> 16 & 0xFFFF;
		int second = ts >> 8 & 0xFF;
		int third = ts & 0xFF;
		long result = first * SEGMENT_STEP + second * SECOND_STEP + third
				* MILLI_SEC_STEP;
		return result;
	}
	
	public static String toString(int ts) {
		int first = ts >> 16 & 0xFFFF;
		int second = ts >> 8 & 0xFF;
		int hour  = first / 120;
		int minite = (first % 120) /2;
		second = (first % 2  )*30 + second; 
		String result = "" + hour + ":" + minite + ":" + second;
		return result;
	}
	
	public static String getTimeString(long milli)
	{
		SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		
    	Date d1=new Date(milli);
    	String st1=format.format(d1);
    	return st1;
	}
	
	public static String getTimeString2(long milli)
	{
		SimpleDateFormat format=new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
		
    	Date d1=new Date(milli);
    	String st1=format.format(d1);
    	return st1;
	}
	
	public static String getTimeString3(long milli)
	{
		SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
		
    	Date d1=new Date(milli);
    	String st1=format.format(d1);
    	return st1;
	}
	
	public static String getTimeStringShort(long mili){
		long allSeconds = mili /1000;
		int hour  = (int) (allSeconds / 3600);
		int min = (int) (allSeconds % 3600 ) / 60;
		int sec  = (int) (allSeconds % 60);
		String result = String.format("%02d:%02d:%02d", hour, min, sec);
    	return result;
	}
}
