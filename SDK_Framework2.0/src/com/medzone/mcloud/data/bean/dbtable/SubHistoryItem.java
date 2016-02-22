package com.medzone.mcloud.data.bean.dbtable;

import java.util.ArrayList;

/**
 * Created by hyc on 2015/12/10.
 */
public class SubHistoryItem {
    private float everytimeValue;
    private String everytimeUnit;
    private int everydayValue;
    private String start2StopDate;

    public SubHistoryItem(float everytimeValue, String everytimeUnit, int everydayValue, String start2StopDate) {
        this.everytimeValue = everytimeValue;
        this.everytimeUnit = everytimeUnit;
        this.everydayValue = everydayValue;
        this.start2StopDate = start2StopDate;
    }

    public static SubHistoryItem convertMedication(Medication medication) {
        SubHistoryItem item = new SubHistoryItem(
                medication.getNum(),medication.getUnit(),medication.getTimes(),medication.getStartTime() + "~" + medication.getStopTime()
        );
        return item;
    }

    public static ArrayList<SubHistoryItem> convertMedicationArrayList(ArrayList<Medication> list) {
        if (list == null) return null;
        ArrayList<SubHistoryItem> items = new ArrayList<>();
        for (Medication m: list) {
            items.add(convertMedication(m));
        }
        return items;
    }

    public String getStart2StopDate() {
        return start2StopDate;
    }

    public void setStart2StopDate(String start2StopDate) {
        this.start2StopDate = start2StopDate;
    }

    public float getEverytimeValue() {
        return everytimeValue;
    }

    public void setEverytimeValue(float everytimeValue) {
        this.everytimeValue = everytimeValue;
    }

    public String getEverytimeUnit() {
        return everytimeUnit;
    }

    public void setEverytimeUnit(String everytimeUnit) {
        this.everytimeUnit = everytimeUnit;
    }

    public int getEverydayValue() {
        return everydayValue;
    }

    public void setEverydayValue(int everydayValue) {
        this.everydayValue = everydayValue;
    }
}
