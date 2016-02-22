package com.medzone.mcloud.database;

import android.content.Context;
import android.util.SparseArray;

import com.medzone.framework.data.CoreDataBaseConfig;
import com.medzone.framework.data.CoreDataBaseConfig.OrmliteDBActionType;
import com.medzone.framework.data.CoreDatabaseHelper;
import com.medzone.framework.data.SqliteAction;
import com.medzone.framework.data.SqliteTiming;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.UseLog;
import com.medzone.mcloud.data.bean.dbtable.Assignment;
import com.medzone.mcloud.data.bean.dbtable.BloodOxygen;
import com.medzone.mcloud.data.bean.dbtable.BloodOxygenLong;
import com.medzone.mcloud.data.bean.dbtable.BloodPressure;
import com.medzone.mcloud.data.bean.dbtable.BloodSugar;
import com.medzone.mcloud.data.bean.dbtable.CheckListFactor;
import com.medzone.mcloud.data.bean.dbtable.Clock;
import com.medzone.mcloud.data.bean.dbtable.ContactPerson;
import com.medzone.mcloud.data.bean.dbtable.DefAlarmConfiguration;
import com.medzone.mcloud.data.bean.dbtable.DeviceKeyInfo;
import com.medzone.mcloud.data.bean.dbtable.EarTemperature;
import com.medzone.mcloud.data.bean.dbtable.EcgSegment;
import com.medzone.mcloud.data.bean.dbtable.FetalHeart;
import com.medzone.mcloud.data.bean.dbtable.FetalMovement;
import com.medzone.mcloud.data.bean.dbtable.Labeling;
import com.medzone.mcloud.data.bean.dbtable.Message;
import com.medzone.mcloud.data.bean.dbtable.MessageSession;
import com.medzone.mcloud.data.bean.dbtable.NewRule;
import com.medzone.mcloud.data.bean.dbtable.NotifyMessage;
import com.medzone.mcloud.data.bean.dbtable.Recommendation;
import com.medzone.mcloud.data.bean.dbtable.Record;
import com.medzone.mcloud.data.bean.dbtable.Subscribe;
import com.medzone.mcloud.data.bean.dbtable.UploadEntity;
import com.medzone.mcloud.data.bean.dbtable.Urinalysis;
import com.medzone.mcloud.data.bean.dbtable.UrinaryProduction;
import com.medzone.mcloud.data.bean.dbtable.WeightEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Robert
 * @category 数据库接口包装类
 */
public final class CloudDatabaseHelper {

    private static final String DATABASE_NAME = "mcloud.db";
    private static final int DATABASE_VERSION = 106;

    private static final SparseArray<Class<?>> sa = new SparseArray<>();
    static CoreDataBaseConfig config;

    private static void initTriggerMap() {
        if (sa.size() == 0) {

            sa.put(UseLog.TYPE_RECORD_BP, BloodPressure.class);
            sa.put(UseLog.TYPE_RECORD_OXY, BloodOxygen.class);
            sa.put(UseLog.TYPE_RECORD_ET, EarTemperature.class);
            sa.put(UseLog.TYPE_RECORD_BS, BloodSugar.class);
            sa.put(UseLog.TYPE_RECORD_FH, FetalHeart.class);
            sa.put(UseLog.TYPE_RECORD_OXYL, BloodOxygenLong.class);
            sa.put(UseLog.TYPE_RECORD_FM, FetalMovement.class);
            sa.put(UseLog.TYPE_RECORD_WEIGHT, WeightEntity.class);
            sa.put(UseLog.TYPE_RECORD_ULS, Urinalysis.class);
            sa.put(UseLog.TYPE_RECORD_UP, UrinaryProduction.class);
            sa.put(UseLog.TYPE_RECORD_ECG, Record.class);
            sa.put(UseLog.TYPE_RECORD_ECG_SEGMENT, EcgSegment.class);

        }
    }

    public synchronized static void init(final Context context) {
        if (config == null) {
            initTriggerMap();
            CoreDatabaseHelper.init(context, initFirst(context), initUpgradeMap(), initDropTriggerMap());
        }
    }

    public synchronized static void unInit() {
        CoreDatabaseHelper.unInit();
    }

    public static CoreDatabaseHelper getInstance() {
        return CoreDatabaseHelper.getInstance();
    }

    /**
     * 职责1：建立触发器 职责2：迁移数据
     *
     * @return
     */
    private static HashMap<Class<?>, String> initUpgradeMap() {
        HashMap<Class<?>, String> map = new HashMap<>();
        map.put(UseLog.class, "com.medzone.cloud.uselog.UseLogCache");
        map.put(Account.class, "com.medzone.cloud.base.account.AccountCache");
        map.put(BloodPressure.class, "com.medzone.cloud.measure.bloodpressure.cache.BloodPressureCache");
        map.put(BloodOxygen.class, "com.medzone.cloud.measure.bloodoxygen.cache.BloodOxygenCache");
        map.put(BloodOxygenLong.class, "com.medzone.cloud.measure.bloodoxygenlong.cache.BloodOxygenLongCache");
        map.put(EarTemperature.class, "com.medzone.cloud.measure.eartemperature.cache.EarTemperatureCache");
        map.put(BloodSugar.class, "com.medzone.cloud.measure.bloodsugar.cache.BloodSugarCache");
        map.put(FetalHeart.class, "com.medzone.cloud.measure.fetalheart.cache.FetalHeartCache");
        map.put(FetalMovement.class, "com.medzone.cloud.measure.fetalmovement.cache.FetalMovementCache");
        map.put(WeightEntity.class, "com.medzone.cloud.measure.weight.cache.WeightCache");
        map.put(Urinalysis.class, "com.medzone.cloud.measure.urinalysis.cache.UrinalysisCache");
        map.put(Clock.class, "com.medzone.cloud.clock.cache.ClockCache");
        map.put(UploadEntity.class, "com.medzone.cloud.upload.UploadCache");
        map.put(ContactPerson.class, "com.medzone.cloud.contact.cache.ContactCache");
        map.put(Subscribe.class, "com.medzone.cloud.subscribe.cache.SubscribeCache");
        map.put(UrinaryProduction.class, "com.medzone.cloud.measure.urinaproduction.cache.UrinaryProductionCache");
        map.put(Record.class, "com.medzone.cloud.measure.electrocardiogram.cache.RecordCache");
//		map.put(CheckListFactor.class, CheckListCache.class);
        // map.put(NotifyMessage.class, NotifyMessageCache.class);
        // map.put(Message.class, "com.medzone.cloud.comp.chatroom.cache.MessageCache");
        // map.put(MessageSession.class, "com.medzone.cloud.comp.chatroom.cache.MessageSessionCache");

        return map;
    }

    private static SparseArray<List<Class<?>>> initDropTriggerMap() {
        SparseArray<List<Class<?>>> sa = new SparseArray<>();
        final int v0 = 0;
        final List<Class<?>> v0List = new ArrayList<>();
        v0List.add(BloodPressure.class);
        v0List.add(BloodOxygen.class);
        v0List.add(EarTemperature.class);
        sa.put(v0, v0List);

        final int v94 = 94;
        final List<Class<?>> v94List = new ArrayList<>();
        v94List.add(BloodPressure.class);
        v94List.add(BloodOxygen.class);
        v94List.add(EarTemperature.class);
        v94List.add(BloodSugar.class);
        sa.put(v94, v94List);

        final int v95 = 95;
        final List<Class<?>> v95List = new ArrayList<>();
        v95List.add(BloodPressure.class);
        v95List.add(BloodOxygen.class);
        v95List.add(BloodOxygenLong.class);
        v95List.add(EarTemperature.class);
        v95List.add(BloodSugar.class);
        v95List.add(FetalHeart.class);
        v95List.add(FetalMovement.class);
        v95List.add(Urinalysis.class);
        v95List.add(CheckListFactor.class);
        v95List.add(WeightEntity.class);
        v95List.add(UrinaryProduction.class);
        sa.put(v95, v95List);

        final int v106 = 106;
        final List<Class<?>> v106List = new ArrayList<>();
        v106List.add(Record.class);
        v106List.add(EcgSegment.class);
        v106List.add(DeviceKeyInfo.class);
        sa.put(v106, v106List);

        return sa;
    }

    private static CoreDataBaseConfig initFirst(final Context context) {
        config = new CoreDataBaseConfig(DATABASE_NAME, DATABASE_VERSION, context);

        // create table

        config.addConfig(UseLog.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Account.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(ContactPerson.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Subscribe.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(NotifyMessage.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Message.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(MessageSession.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(BloodPressure.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(BloodOxygen.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(BloodOxygenLong.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(EarTemperature.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(BloodSugar.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(FetalHeart.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(FetalMovement.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(DefAlarmConfiguration.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Clock.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(UploadEntity.class, OrmliteDBActionType.ON_CREATE);

        config.addConfig(Assignment.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Recommendation.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Urinalysis.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(WeightEntity.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(CheckListFactor.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(NewRule.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(UrinaryProduction.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Labeling.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(Record.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(EcgSegment.class, OrmliteDBActionType.ON_CREATE);
        config.addConfig(DeviceKeyInfo.class, OrmliteDBActionType.ON_CREATE);

        // upgrade table

        config.addConfig(UseLog.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Account.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(ContactPerson.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Subscribe.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(NotifyMessage.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Message.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(MessageSession.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(BloodPressure.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(BloodOxygen.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(BloodOxygenLong.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(EarTemperature.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(BloodSugar.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(FetalHeart.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(FetalMovement.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Clock.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(UploadEntity.class, OrmliteDBActionType.ON_UPGRADE);

        config.addConfig(Assignment.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Recommendation.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Urinalysis.class, OrmliteDBActionType.ON_UPGRADE);

        config.addConfig(WeightEntity.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(CheckListFactor.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(NewRule.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(UrinaryProduction.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(Labeling.class, OrmliteDBActionType.ON_UPGRADE);

        config.addConfig(Record.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(EcgSegment.class, OrmliteDBActionType.ON_UPGRADE);
        config.addConfig(DeviceKeyInfo.class, OrmliteDBActionType.ON_UPGRADE);


        for (int i = 0; i < sa.size(); i++) {
            final int tableType = sa.keyAt(i);
            final Class<?> clazz = sa.valueAt(i);
            config.addTrigger(clazz, SqliteTiming.AFTER, SqliteAction.INSERT, tableType);
            config.addTrigger(clazz, SqliteTiming.AFTER, SqliteAction.DELETE, tableType);
        }

        return config;
    }
}
