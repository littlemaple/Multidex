package com.medzone.mcloud.data.bean.dbtable;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;
import com.medzone.framework.util.RefResourceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Assignment extends BaseIdDatabaseContent implements Serializable, Parcelable {

    private static final long serialVersionUID = 1L;
    // 服务端定义的任务类型
    // 任务子类型
    // 当type=record时：bs=血糖，bp=血压，fh=胎心，fm=胎动，ut=尿检，weight=体重
    // 当type=precheck时：precheck1=第一次产检、precheck2=第二次产检......
    // 当type=ask时：_pck3=第三次产检问卷
    // 当type=pregend时：pregend=孕期结束语
    public static final String TYPE_VACCINE = "vaccine";
    public static final String TYPE_EDUCATION = "education";
    public static final String TYPE_MS = "ms";
    public static final String TYPE_RECORD = "record";
    public static final String TYPE_PRECHECK = "precheck";
    public static final String TYPE_ASK = "ask";
    public static final String TYPE_PREGEND = "pregend";
    public static final String TYPE_WEBVIEW = "webview";
    public static final String TYPE_OPEN = "open";
    public static final String TYPE_VISIT = "visit";
    public static final String TYPE_MEDICINE = "medicine";
    public static final String SUBTYPE_RECORD_BP = "bp";
    public static final String SUBTYPE_RECORD_OXY = "oxy";
    public static final String SUBTYPE_RECORD_OXYL = "oxyl";
    public static final String SUBTYPE_RECORD_ET = "et";
    public static final String SUBTYPE_MEDICINE_DISPENSING = "dispensing";
    public static final String SUBTYPE_MEDICINE_DOSE = "dose";
    public static final String SUBTYPE_RECORD_BS = "bs";
    public static final String SUBTYPE_RECORD_FH = "fh";
    public static final String SUBTYPE_RECORD_FM = "fm";
    public static final String SUBTYPE_RECORD_UT = "ua";
    public static final String SUBTYPE_RECORD_UP = "up";
    public static final String SUBTYPE_RECORD_WEIGHT = "weight";
    public static final String subType_PRECHECK1 = "precheck1";
    public static final String subType_PRECHECK2 = "precheck2";
    public static final String subType_PRECHECK3 = "precheck3";
    public static final String SUBTYPE_ASK1 = "_pck1";
    public static final String SUBTYPE_ASK2 = "_pck2";
    public static final String SUBTYPE_ASK3 = "_pck3";
    public static final String SUBTYPE_PREGEND = "pregend";
    public static final String SUBTYPE_MANAGE="_manage";

    public static final String NAME_FIELD_DEGREE = "degree";

    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private String type;
    @DatabaseField
    private String subType;
    @DatabaseField
    private int progressTotal;
    @DatabaseField
    private int progress;
    @DatabaseField
    private String tip;
    @DatabaseField
    private boolean isOpen;
    @DatabaseField
    private boolean isFinished;
    @DatabaseField
    private String tag;
    @DatabaseField
    private String url;
    @DatabaseField
    private int resourceId;
    @DatabaseField
    private int degree;
    @DatabaseField
    private int prior;

    @DatabaseField
    private int taskId;

    @DatabaseField
    private String snstip;

    @DatabaseField
    private String doctorDate;
    public void setDoctorDate(String doctorDate) {
        this.doctorDate = doctorDate;
    }

    public String getDoctorDate() {
        return doctorDate;
    }

    public Assignment() {

    }

    public String getSnstip() {
        return snstip;
    }

    public void setSnstip(String snstip) {
        this.snstip = snstip;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return this.taskId;
    }

    /**
     * 该任务的类别，目前只有system跟custom两种
     *
     * @param tag
     */
    public Assignment(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public int getProgressTotal() {
        return progressTotal;
    }

    public void setProgressTotal(int progressTotal) {
        this.progressTotal = progressTotal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public int getIconResourceId(Context context) {
        if (TextUtils.equals(getType(), TYPE_RECORD)) {
            if (TextUtils.equals(SUBTYPE_RECORD_BP, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_pressure");
            if (TextUtils.equals(SUBTYPE_RECORD_ET, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_temperature_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_temperature");

            if (TextUtils.equals(SUBTYPE_RECORD_UP, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_urine_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_urine");
            if (TextUtils.equals(SUBTYPE_RECORD_BS, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_bloodsugar_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_bloodsugar");
            if (TextUtils.equals(SUBTYPE_RECORD_FM, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure") : RefResourceUtil.getDrawableId(context,
                        "home_icon_pressure");
            if (TextUtils.equals(SUBTYPE_RECORD_FH, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure") : RefResourceUtil.getDrawableId(context,
                        "home_icon_pressure");
            if (TextUtils.equals(SUBTYPE_RECORD_UT, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_urine_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_urine");
            if (TextUtils.equals(SUBTYPE_RECORD_WEIGHT, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_weight_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_weight");
        } else if (TextUtils.equals(getType(), TYPE_PRECHECK)) {
            return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure") : RefResourceUtil.getDrawableId(context, "home_icon_pressure");
        } else if (TextUtils.equals(getType(), TYPE_VISIT)) {
            return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_reminder_done") : RefResourceUtil.getDrawableId(context, "home_icon_reminder");
        } else if (TextUtils.equals(getType(), TYPE_MEDICINE)) {
            if (TextUtils.equals(SUBTYPE_MEDICINE_DISPENSING, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_dispensing_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_dispensing");
            if (TextUtils.equals(SUBTYPE_MEDICINE_DOSE, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_medicine_done") : RefResourceUtil.getDrawableId(context,
                        "home_icon_medicine");
        } else if (TextUtils.equals(getType(), TYPE_ASK)) {
            if(TextUtils.equals(SUBTYPE_MANAGE, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_plan") : RefResourceUtil.getDrawableId(context,
                        "home_icon_plan");
            // 根据任务等级筛选不同的图片，目前的等级范围3（孕前信息问卷），2（体征问卷），1（宝宝出生问卷），0（其他）
            switch (prior) {
                case 3:
                    return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_questionnaire") : RefResourceUtil.getDrawableId(context, "home_icon_questionnaire");
                case 2:
                    return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_questionnaire") : RefResourceUtil.getDrawableId(context, "home_icon_questionnaire");
                case 1:
                    return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_questionnaire") : RefResourceUtil.getDrawableId(context, "home_icon_questionnaire");
                case 0:
                    return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_questionnaire") : RefResourceUtil.getDrawableId(context, "home_icon_questionnaire");
                default:
                    return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_questionnaire") : RefResourceUtil.getDrawableId(context, "home_icon_questionnaire");

            }
        } else if (TextUtils.equals(getType(), SUBTYPE_PREGEND)) {
            return RefResourceUtil.getDrawableId(context, "home_icon_pressure");
        } else if (TextUtils.equals(getType(), TYPE_OPEN)) {
            if (TextUtils.equals(TYPE_OPEN, getSubType()))
                return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure") : RefResourceUtil.getDrawableId(context,
                        "home_icon_pressure");
        }
        return isFinished() ? RefResourceUtil.getDrawableId(context, "home_icon_pressure") : RefResourceUtil.getDrawableId(context, "home_icon_pressure");
    }

    public static List<Assignment> parse(JSONObject jo) {
        List<Assignment> list = new ArrayList<Assignment>();
        int degree = 0;
        try {
            JSONArray jSystem = jo.getJSONArray("system");
            for (int i = 0; i < jSystem.length(); i++) {
                Assignment assignment = Assignment.parse(new Assignment("system"), jSystem.getJSONObject(i));
                assignment.degree = ++degree;
                list.add(assignment);
            }
            JSONArray jCustom = jo.getJSONArray("custom");
            for (int i = 0; i < jCustom.length(); i++) {
                Assignment assignment = Assignment.parse(new Assignment("custom"), jCustom.getJSONObject(i));
                assignment.degree = ++degree;
                list.add(assignment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Assignment parse(Assignment assignment, JSONObject jo) {
        try {
            if (jo.has("taskid") && !jo.isNull("taskid")) {
                assignment.setTaskId(TextUtils.isEmpty(jo.getString("taskid")) ? 0 : jo.getInt("taskid"));
                Log.i("robert", "parse id>" + assignment.getTaskId());
            }
            if (jo.has("taskname") && !jo.isNull("taskname")) {
                assignment.setName(jo.getString("taskname"));
            }
            if (jo.has("tip") && !jo.isNull("tip")) {
                assignment.setTip(jo.getString("tip"));
            }
            if (jo.has("type") && !jo.isNull("type")) {
                assignment.setType(jo.getString("type"));
            }
            if (jo.has("subtype") && !jo.isNull("subtype")) {
                assignment.setSubType(jo.getString("subtype"));
            }
            if (jo.has("progress_total") && !jo.isNull("progress_total")) {
                assignment.setProgressTotal(TextUtils.isEmpty(jo.getString("progress_total")) ? 0 : jo.getInt("progress_total"));
            }
            if (jo.has("progress") && !jo.isNull("progress")) {
                assignment.setProgress(TextUtils.isEmpty(jo.getString("progress")) ? 0 : jo.getInt("progress"));
            }
            if (jo.has("url") && !jo.isNull("url")) {
                assignment.setUrl(jo.getString("url"));
            }
            if (jo.has("isopen") && !jo.isNull("isopen")) {
                assignment.setOpen(TextUtils.equals("y", jo.getString("isopen").toLowerCase(Locale.CHINA)));
            }
            if (jo.has("isfinished") && !jo.isNull("isfinished")) {
                assignment.setFinished(TextUtils.equals("y", jo.getString("isfinished").toLowerCase(Locale.CHINA)));
            }
            if (jo.has("prior") && !jo.isNull("prior")) {
                assignment.setPrior(TextUtils.isEmpty(jo.getString("prior")) ? 0 : jo.getInt("prior"));
            }
            if (jo.has("snstip") && !jo.isNull("snstip")) {
                assignment.setSnstip(jo.getString("snstip"));
            }
            if (jo.has("description") && !jo.isNull("description")) {
                assignment.setDescription(jo.getString("description"));
            }
            if (jo.has("setting") && !jo.isNull("setting")) {
                if (jo.getJSONObject("setting").has("date")&&!jo.getJSONObject("setting").isNull("date")) {
                    assignment.setDoctorDate(jo.getJSONObject("setting").getString("date"));
                }
            }
            assignment.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignment;
    }

    @Override
    public String toString() {
        return "[ taskId:" + getTaskId() + ",taskName:" + getName() + "\ntaskTip:" + getTip() + ",isOpen:" + isOpen + ",isFinish:" + isFinished() + "]";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(taskId);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(subType);
        dest.writeInt(progressTotal);
        dest.writeInt(progress);
        dest.writeString(tip);
        dest.writeByte((byte) (isOpen ? 1 : 0));
        dest.writeByte((byte) (isFinished ? 1 : 0));
        dest.writeString(tag);
        dest.writeString(url);
        dest.writeInt(resourceId);
        dest.writeString(snstip);
    }

    private Assignment(Parcel source) {
        this.id = source.readInt();
        this.taskId = source.readInt();
        this.name = source.readString();
        this.description = source.readString();
        this.type = source.readString();
        this.subType = source.readString();
        this.progressTotal = source.readInt();
        this.progress = source.readInt();
        this.tip = source.readString();
        this.isOpen = source.readByte() == 1 ? true : false;
        this.isFinished = source.readByte() == 1 ? true : false;
        this.tag = source.readString();
        this.url = source.readString();
        this.resourceId = source.readInt();
        this.snstip = source.readString();
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }

        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }
    };
}
