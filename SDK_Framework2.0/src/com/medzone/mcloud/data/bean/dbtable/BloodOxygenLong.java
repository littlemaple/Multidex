package com.medzone.mcloud.data.bean.dbtable;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.ILongTermStatistic;
import com.medzone.mcloud.util.BaseMeasureDataUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Robert.
 */
public class BloodOxygenLong extends BaseMeasureData implements ILongTermStatistic, Parcelable {

    public static final int OXYGEN_STATE_IDEAL = 1;
    public static final int OXYGEN_STATE_MISSING = 2;
    public static final int OXYGEN_STATE_LOW = 3;
    public static final String TAG_OXYL = "oxyl";
    public static final String NAME_FIELD_OXYGEN_LONG = "oxyl64";
    public static final String NAME_FIELD_RATE_LONG = "ratel64";
    public static final String NAME_FIELD_OXYGEN_AVERAGE = "oxygenAverage";
    public static final String NAME_FIELD_RATE_AVERAGE = "rateAverage";
    public static final String NAME_FIELD_RATE_MIN = "rate_min";
    public static final String NAME_FIELD_RATE_MAX = "rate_max";
    public static final String NAME_FIELD_OXYGEN_MIN = "oxygen_min";
    public static final String NAME_FIELD_OXYGEN_MAX = "oxygen_max";
    public static final Creator<BloodOxygenLong> CREATOR = new Creator<BloodOxygenLong>() {

        @Override
        public BloodOxygenLong[] newArray(int size) {
            return new BloodOxygenLong[size];
        }

        @Override
        public BloodOxygenLong createFromParcel(Parcel source) {
            return new BloodOxygenLong(source);
        }
    };
    /**
     *
     */
    private static final long serialVersionUID = 1705040203708809891L;
    private static final String TAG = BloodOxygenLong.class.getSimpleName();
    @DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_OXYGEN_LONG)
    private byte[] oxyl64;
    @DatabaseField(dataType = DataType.BYTE_ARRAY, columnName = NAME_FIELD_RATE_LONG)
    private byte[] ratel64;
    @DatabaseField(columnName = NAME_FIELD_OXYGEN_AVERAGE)
    private Integer oxygenAverage;
    @DatabaseField(columnName = NAME_FIELD_RATE_AVERAGE)
    private Integer rateAverage;
    @DatabaseField(columnName = NAME_FIELD_RATE_MIN)
    private Integer rateMin;
    @DatabaseField(columnName = NAME_FIELD_RATE_MAX)
    private Integer rateMax;
    @DatabaseField(columnName = NAME_FIELD_OXYGEN_MIN)
    private Integer oxygenMin;
    @DatabaseField(columnName = NAME_FIELD_OXYGEN_MAX)
    private Integer oxygenMax;
    @DatabaseField
    private Integer period;
    private List<Integer> oxygenLongList;
    private List<Integer> rateLongList;

    public BloodOxygenLong() {
        setTag(TAG_OXYL);
    }

    private BloodOxygenLong(Parcel source) {
        id = source.readInt();
        setMeasureUID(source.readString());
    }

    public static List<BloodOxygenLong> createBloodOxygenList(JSONArray jsonArray, Account account) {
        List<BloodOxygenLong> retList = new ArrayList<BloodOxygenLong>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                retList.add(createBloodOxygen(jsonObj, account));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retList;

    }

    public static BloodOxygenLong createBloodOxygen(JSONObject jo, Account account) {

        BloodOxygenLong bo = new BloodOxygenLong();
        bo.setBelongAccount(account);

        return parse(jo, bo);
    }

    public static BloodOxygenLong updateBloodOxygen(JSONObject jo, BloodOxygenLong bo) {

        return parse(jo, bo);
    }

    public Integer getPeriod() {
        return period == null ? 30 * 1000 : period * 1000;
    }

    public Integer getPeriodSec() {
        return period == null ? 30 : period;
    }

    public void setPeriod(@NonNull Integer period) {
        this.period = period;
    }

    private static BloodOxygenLong parse(JSONObject jo, BloodOxygenLong bo) {
        try {
            if (jo.has("recordid") && !jo.isNull("recordid")) {
                bo.setRecordID(jo.getInt("recordid"));
            }
            if (jo.has("measureuid") && !jo.isNull("measureuid")) {

                String uid = jo.getString("measureuid");
                bo.setMeasureUID(uid);
            }
            if (jo.has("source") && !jo.isNull("source")) {
                bo.setSource(jo.getString("source"));
            }
            if (jo.has("readme") && !jo.isNull("readme")) {
                bo.setReadme(jo.getString("readme"));
            }
            if (jo.has("x") && !jo.isNull("x")) {
                bo.setX(jo.getDouble("x"));
            }
            if (jo.has("y") && !jo.isNull("y")) {
                bo.setY(jo.getDouble("y"));
            }
            if (jo.has("z") && !jo.isNull("z")) {
                bo.setZ(jo.getDouble("z"));
            }
            if (jo.has("state") && !jo.isNull("state")) {
                bo.setAbnormal(jo.getInt("state"));
            }
            if (jo.has("uptime") && !jo.isNull("uptime")) {
                bo.setUptime(jo.getLong("uptime"));
            }
            if (jo.has("value1") && !jo.isNull("value1")) {
                try {
                    String oxyl64 = jo.getString("value1");
                    byte[] orgin = android.util.Base64.decode(oxyl64.getBytes(), android.util.Base64.DEFAULT);
                    bo.setOxyl(orgin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (jo.has("value2") && !jo.isNull("value2")) {
                try {
                    String rate64 = jo.getString("value2");
                    byte[] orgin = android.util.Base64.decode(rate64.getBytes(), android.util.Base64.DEFAULT);
                    bo.setRatel(orgin);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (jo.has("value1_avg") && !jo.isNull("value1_avg")) {
                bo.setOxygenAverage(jo.getInt("value1_avg"));
            }
            if (jo.has("value2_avg") && !jo.isNull("value2_avg")) {
                bo.setRateAverage(jo.getInt("value2_avg"));
            }
            if (jo.has("value_period") && !jo.isNull("value_period")) {
                bo.setPeriod(jo.getInt("value_period"));
            }
            bo.setStateFlag(STATE_SYNCHRONIZED);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bo;
    }

    public static List<Integer> cloneList(List<Integer> list) {
        if (list == null) return null;
        List<Integer> clone = new ArrayList<Integer>(list.size());
        for (Integer item : list)
            clone.add(item);
        return clone;
    }

    void initRateRange() {
        if (rateMin == null || rateMax == null) {
            Log.d("robert", "use data by parse.");
            HashMap<String, Object> map = getRateExtremeRange();
            setRateMin((Integer) map.get(RATE_MIN));
            setRateMax((Integer) map.get(RATE_MAX));
        } else {
            Log.d("robert", "use data from db.");
        }
    }

    void initOxygenRange() {
        if (oxygenMin == null || oxygenMax == null) {
            HashMap<String, Object> map = getOxygenExtremeRange();
            setOxygenMin((Integer) map.get(OXYGEN_MIN));
            setOxygenMax((Integer) map.get(OXYGEN_MAX));
        }
    }

    public Integer getRateMin() {
        initRateRange();
        return rateMin;
    }

    private void setRateMin(Integer rateMin) {
        this.rateMin = rateMin;
    }

    public Integer getRateMax() {
        initRateRange();
        return rateMax;
    }

    private void setRateMax(Integer rateMax) {
        this.rateMax = rateMax;
    }

    public Integer getOxygenMin() {
        initOxygenRange();
        return oxygenMin;
    }

    private void setOxygenMin(Integer oxygenMin) {
        this.oxygenMin = oxygenMin;
    }

    public Integer getOxygenMax() {
        initOxygenRange();
        return oxygenMax;
    }

    private void setOxygenMax(Integer oxygenMax) {
        this.oxygenMax = oxygenMax;
    }

    public byte[] getOxyl() {
        return oxyl64;
    }

    public void setOxyl(byte[] oxyl) {
        this.oxyl64 = oxyl;
    }

    public byte[] getRatel() {
        return ratel64;
    }

    public void setRatel(byte[] ratel) {
        this.ratel64 = ratel;
    }

    public Integer getOxygenAverage() {
        if (oxygenAverage == null || oxygenAverage == 0)
            setOxygenAverage((int) BaseMeasureDataUtil.getAverage(getOxygenLongList()));
        return oxygenAverage == null ? 0 : oxygenAverage;
    }

    public void setOxygenAverage(Integer oxygenAverage) {
        this.oxygenAverage = oxygenAverage;
    }

    public Integer getRateAverage() {
        if (rateAverage == null || rateAverage == 0)
            setRateAverage((int) BaseMeasureDataUtil.getAverage(getRateLongList()));
        return rateAverage == null ? 0 : rateAverage;
    }

    public void setRateAverage(Integer rateAverage) {
        this.rateAverage = rateAverage;
    }

    public List<Integer> getOxygenLongList() {
        long timemillis = System.nanoTime();
        if (this.oxygenLongList == null && getOxyl() != null) {
            this.oxygenLongList = BaseMeasureDataUtil.convertOXYLByteArr2List(getOxyl());
            filterIllegalOxygen(this.oxygenLongList);
        }
        Log.d("OxygenLong", ">>>getOxygenLongList:" + (System.nanoTime() - timemillis));
        return this.oxygenLongList;
    }

    public List<Integer> getRateLongList() {
        long timemillis = System.nanoTime();
        if (this.rateLongList == null && getRatel() != null) {
            this.rateLongList = BaseMeasureDataUtil.convertRATELByteArr2List(getRatel());
            filterIllegalOxygen(getOxygenLongList());
        }
        if (oxygenLongList == null || rateLongList == null) return null;
        if (oxygenLongList.size() <= rateLongList.size()) {
            rateLongList = rateLongList.subList(rateLongList.size() - oxygenLongList.size(), rateLongList.size());
        }
        Log.d("OxygenLong", ">>>getRateLongList:" + (System.nanoTime() - timemillis));
        return this.rateLongList;
    }

    // 除去血氧中前面为0的数据
    private void filterIllegalOxygen(List<Integer> list) {
//        long timemillis = System.nanoTime();
//        if (list == null || !list.contains(0)) return;
//        Iterator<Integer> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            Integer i = iterator.next();
//            if (i == null || i == 0) iterator.remove();
//            else {
//                break;
//            }
//        }
//        Log.d("OxygenLong", ">>>filterIllegalOxygen:" + (System.nanoTime() - timemillis));
    }

    @Override
    public List<Rule> getRulesCollects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isHealthState() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public double getScopeRate(double start, double end, Integer retentionNum) {
        List<Integer> oxygenList = getOxygenLongList();
        List<Integer> rateList = getRateLongList();
        if (oxygenList == null || oxygenList.size() == 0 || rateList == null || rateList.size() == 0)
            return 0d;
        if (oxygenList.size() != rateList.size()) {
            Log.e(TAG, ">>>#Please Attention：illegal long-term bloodOxygen datum that the oxygen and the rate are in inconsistent");
            return 0d;
        }
        int size = oxygenList.size();
        double count = 0;
        for (Integer i : oxygenList) {
            if (i <= 0) {
                size--;
                continue;
            }
            if (i >= start && i < end) count++;

        }
        if (size <= 0) {
            Log.e(TAG, ">>>#illegal size about the divide is lower than 0");
            return 0;
        }
        if (retentionNum == null) return count / size;
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(retentionNum);
        Double result = Double.valueOf(decimalFormat.format(count / size * 100));
        return result == null ? 0 : result;
    }

    @Override
    public long getDuration(double start, double end) {
        List<Integer> oxygenList = getOxygenLongList();
        List<Integer> rateList = getRateLongList();
        if (oxygenList == null || oxygenList.size() == 0 || rateList == null || rateList.size() == 0)
            return 0l;
        if (oxygenList.size() != rateList.size()) {
            Log.e(TAG, ">>>#Please Attention：illegal long-term bloodOxygen datum that the oxygen and the rate are in inconsistent");
            return 0l;
        }
        int count = 0;
        for (Integer i : oxygenList) {
            if (i >= start && i < end) count++;
        }
        // 需求中小于一分钟的显示一分钟
        return (long) (getPeriod() * (count - 1 < 0 ? 0 : count - 1));
    }

    @Override
    public HashMap<String, Object> getMinimumInfo() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Integer> oxygenList = getOxygenLongList();
        List<Integer> rateList = getRateLongList();
        if (oxygenList == null || oxygenList.size() == 0 || rateList == null || rateList.size() == 0)
            return null;
        if (oxygenList.size() != rateList.size()) {
            Log.e(TAG, ">>>#Please Attention：illegal long-term bloodOxygen datum that the oxygen and the rate are in inconsistent");
            return null;
        }
        int minOxygen = 0;
        int minRate = 0;
        int index = 0;
        // select the first data which not eq 0
        for (int i = 0; i < oxygenList.size(); i++) {
            if (oxygenList.get(i) > 0) {
                minOxygen = oxygenList.get(i);
                index = i;
                break;
            }
        }
        if (minOxygen == 0) {
            Log.e(TAG, ">>>#illegal datum");
            map.put(TIME, getMeasureTime() * 1000);
            map.put(OXYGEN, 0);
            map.put(RATE, 0);
            return map;
        }
        for (int i = 0; i < oxygenList.size(); i++) {
            if (oxygenList.get(i) < minOxygen && oxygenList.get(i) > 0) {
                minOxygen = oxygenList.get(i);
                index = i;
            }
        }
        // get the rate in the same index
        if (rateList.size() > index) minRate = rateList.get(index);
        map.put(TIME, getMeasureTime() * 1000 + getPeriod() * index);
        map.put(OXYGEN, minOxygen);
        map.put(RATE, minRate);
        return map;
    }

    @Override
    public double getRateAverate(Integer retentionNum) {
        if (retentionNum == null) return BaseMeasureDataUtil.getAverage(getRateLongList());
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(retentionNum);
        Double result = Double.valueOf(decimalFormat.format(BaseMeasureDataUtil.getAverage(getRateLongList())));
        return result == null ? 0 : result;
    }

    @Override
    public double getOxygenAverage(Integer retentionNum) {
        if (retentionNum == null) return BaseMeasureDataUtil.getAverage(getOxygenLongList());
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(retentionNum);
        Double result = Double.valueOf(decimalFormat.format(BaseMeasureDataUtil.getAverage(getOxygenLongList())));
        return result == null ? 0 : result;
    }

    @Override
    public HashMap<String, Object> getOxygenExtremeRange() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Integer> oxygenList = cloneList(getOxygenLongList());

        if (oxygenList != null && oxygenList.size() > 0) {
            Collections.sort(oxygenList);
            int i = 0;
            while (oxygenList.get(i) == 0 && i < oxygenList.size() - 1) {
                i++;
            }
            map.put(OXYGEN_MIN, oxygenList.get(i));
            map.put(OXYGEN_MAX, oxygenList.get(oxygenList.size() - 1));
        } else {
            map.put(OXYGEN_MIN, 0);
            map.put(OXYGEN_MAX, 0);
        }
        return map;
    }

    @Override
    public HashMap<String, Object> getRateExtremeRange() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Integer> rateLongList = cloneList(getRateLongList());
        if (rateLongList != null && rateLongList.size() > 0) {
            Collections.sort(rateLongList);
            int i = 0;
            while (rateLongList.get(i) == 0 && i < rateLongList.size() - 1) {
                i++;
            }
            map.put(RATE_MIN, rateLongList.get(i));
            map.put(RATE_MAX, rateLongList.get(rateLongList.size() - 1));
        } else {
            map.put(RATE_MIN, 0);
            map.put(RATE_MAX, 0);
        }
        return map;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(getMeasureUID());
    }

    @Override
    public void toMap(Map<String, String> result) {
        //血氧长期暂时没有规则，此处无需编码。
    }

    @Override
    public String getMeasureName() {
        return TAG_OXYL;
    }
}
