package com.medzone.mcloud.data.bean.java;

/**
 *
 */
public class ReportEntity {

    /**
     * 其他部分分享时间的秒数
     */
    public long startDate;
    /**
     * 曲线分享坐标轴最右侧时间的秒数
     */
    public long shareDate;

    public int period;

    public String curYearMonth;
    //    public String   curYYYYMMDD;
    public String startShareYYYYMMDD;
    // public long endDate;

    public int totalCounts;

    public int abnormalCounts;

    public String measureUID;
    public int recentDay;

    public int monthTotalCounts;
    public int monthAbnormalCounts;

    public String activityValue;

}
