package com.medzone.mcloud.cache;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.medzone.mcloud.database.CloudDatabaseHelper;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;
import com.medzone.framework.data.bean.BaseIdSyncDatabaseObject;
import com.medzone.framework.util.Args;
import com.medzone.mcloud.data.bean.dbtable.BaseMeasureData;
import com.medzone.mcloud.data.bean.dbtable.ContactPerson;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractBaseIdDBObjectMapCache<K, T extends BaseIdDatabaseObject> extends AbstractMemoryMapCache<K, T> {

    /**
     * 代理人，通常账号拥有者可以为他测量或者查看他的数据集合。代理人允许为空，表示默认为代理对象为自己。
     */
    private ContactPerson proxyPerson;
    protected int dataType;

    public void setProxyPerson(ContactPerson proxyPerson) {
        Args.notNull(proxyPerson, "proxyPerson");
        this.proxyPerson = proxyPerson;
    }

    public ContactPerson getProxyPerson() {
        return this.proxyPerson;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public T queryForPrimaryId(long primaryId) {
        if (!isValid()) return null;
        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
            Where<T, Long> where = queryBuilder.where();
            where.eq(BaseMeasureData.FIELD_PRIMARY_NAME_ID, primaryId);
            where.and();
            where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId());
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            List<T> list = dao.query(preparedQuery);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 该方法只允许测量数据的控制器调用，否则将抛出运行时异常。并且该方法默认以MEASURE_UID为本地唯一标记，进行匹配查找。
     *
     * @param t 测量数据对象
     * @return 含本地自增主键id的完整数据对象
     * @throws RuntimeException
     */
    public T queryForMeasureUID(T t) {

        if (!isValid()) return null;

        if (!(t instanceof BaseMeasureData)) {
            throw new RuntimeException("该方法只允许测量数据相关的控制器调用!");
        }
        BaseMeasureData data = (BaseMeasureData) t;
        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
            Where<T, Long> where = queryBuilder.where();
            where.eq(BaseMeasureData.NAME_FIELD_MEASUREU_ID, data.getMeasureUID());
            where.and();
            where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId());
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            return dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 该方法只允许测量数据的控制器调用，否则将抛出运行时异常。并且该方法默认以MEASURE_UID为本地唯一标记，进行匹配查找。
     *
     * @param measureUID 测量数据对象
     * @return 含本地自增主键id的完整数据对象
     * @throws RuntimeException
     */
    public T queryForMeasureUID(String measureUID) {

        if (!isValid()) return null;

        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
            Where<T, Long> where = queryBuilder.where();
            where.eq(BaseMeasureData.NAME_FIELD_MEASUREU_ID, measureUID);
            where.and();
            where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId());
            where.and();
            where.ne(BaseMeasureData.NAME_FIELD_ACTION_FLAG, BaseMeasureData.ACTION_DELETE_RECORD);
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            return dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 并且该方法默认以RECORD_ID为本地唯一标记，进行匹配查找。
     *
     * @param recordID 测量数据对象
     * @return 含本地自增主键id的完整数据对象
     */
    @SuppressWarnings("unchecked")
    public T queryForMeasureRID(String recordID) {

        if (!isValid()) return null;

        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
            Where<T, Long> where = queryBuilder.where();

            where.and(where.eq(BaseMeasureData.NAME_FIELD_RECORD_ID, recordID), where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId()));
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            return dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public int deleteForMeasureUID(String measureUID) {

        if (!isValid()) return BaseMeasureData.INVALID_ID;

        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            DeleteBuilder<T, Long> deleteBuilder = dao.deleteBuilder();
            Where<T, Long> where = deleteBuilder.where();
            where.and(where.eq(BaseMeasureData.NAME_FIELD_MEASUREU_ID, measureUID), where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId()));
            deleteBuilder.setWhere(where);
            return deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getDownSeries() {
        if (!isValid()) return 0;
        try {
            Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            QueryBuilder<T, Long> queryBuilder = dao.queryBuilder();
            Where<T, Long> where = queryBuilder.where();
            where.eq(BaseMeasureData.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, getAccountAttached().getId());
            queryBuilder.orderBy(BaseMeasureData.NAME_FIELD_UPTIME, false);
            PreparedQuery<T> preparedQuery = queryBuilder.prepare();
            T t = dao.queryForFirst(preparedQuery);
            if (t != null && t instanceof BaseIdSyncDatabaseObject) {
                return ((BaseIdSyncDatabaseObject) t).getUptime() == null ? 0 : ((BaseIdSyncDatabaseObject) t).getUptime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
