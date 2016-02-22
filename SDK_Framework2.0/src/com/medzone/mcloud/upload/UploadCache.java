package com.medzone.mcloud.upload;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.medzone.mcloud.cache.AbstractDBObjectListCache;
import com.medzone.mcloud.database.CloudDatabaseHelper;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.mcloud.data.bean.dbtable.UploadEntity;

import java.sql.SQLException;
import java.util.List;

/**
 * @attention:将该cache初步的当作一个util
 */
public class UploadCache extends AbstractDBObjectListCache<UploadEntity> {

    public static final String TAG = UploadCache.class.getSimpleName();

    @Override
    public List<UploadEntity> read() {
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(UploadEntity.FIELD_PRIMARY_NAME_ID, true);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UploadEntity read(int id) {

        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.limit((long) 1);
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.FIELD_PRIMARY_NAME_ID, id);
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UploadEntity> retrieveAttachment(Account account) {

        if (!isValid(account)) return null;
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, account.getId());
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UploadEntity> retrieveAttachment(int type) {

        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.NAME_FIELD_TYPE, type);
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<UploadEntity> retrieveAttachment(Account account, int fileState) {
        return retrieveAttachment(account, fileState, false);
    }

    /**
     * will retrieve extra file if reverse is true
     *
     * @param account
     * @param fileState
     * @param reverse
     * @return
     */
    public static List<UploadEntity> retrieveAttachment(Account account, int fileState, boolean reverse) {
        if (!isValid(account)) return null;
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.NAME_FIELD_UPLOAD_STATE, fileState);
            where.and();
            where.eq(UploadEntity.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, account.getId());
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            where.and();
            if (reverse)
                where.eq(UploadEntity.NAME_FIELD_LOCAL_RECORD_ID, -1);
            else where.ne(UploadEntity.NAME_FIELD_LOCAL_RECORD_ID, -1);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param account
     * @param type
     * @param fileState
     * @return
     */
    public static List<UploadEntity> retrieveAttachment(Account account, int type, int fileState) {
        if (!isValid(account)) return null;
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, account.getId());
            where.and();
            where.eq(UploadEntity.NAME_FIELD_TYPE, type);
            where.and();
            where.eq(UploadEntity.NAME_FIELD_UPLOAD_STATE, fileState);
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            where.and();
            where.ne(UploadEntity.NAME_FIELD_LOCAL_RECORD_ID, -1);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断上传事件表中是否已经存在该条数据
     *
     * @param type
     * @param localId
     * @return
     */
    public static boolean isExist(Account account, int type, long localId) {
        if (account == null) return false;
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, account.getId());
            where.and();
            where.eq(UploadEntity.NAME_FIELD_TYPE, type);
            where.and();
            where.eq(UploadEntity.NAME_FIELD_LOCAL_RECORD_ID, localId);
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery).size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isExist(Account account, String fileName, String localPath) {
        if (account == null) return false;
        try {
            Dao<UploadEntity, Long> dao = CloudDatabaseHelper.getInstance().getDao(UploadEntity.class);
            QueryBuilder<UploadEntity, Long> queryBuilder = dao.queryBuilder();
            Where<UploadEntity, Long> where = queryBuilder.where();
            where.eq(UploadEntity.FIELD_FOREIGN_NAME_MASTER_ACCOUNT_ID, account.getId());
            where.and();
            where.eq(UploadEntity.NAME_FIELD_FILE_ADDRESS, localPath);
            where.and();
            where.eq(UploadEntity.NAME_FIELD_LOCAL_RECORD_ID, -1);
            where.and();
            where.eq(UploadEntity.NAME_FIELD_FILE_NAME, fileName);
            where.and();
            where.isNotNull(UploadEntity.NAME_FIELD_FILE_ADDRESS);
            PreparedQuery<UploadEntity> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery).size() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isValid(Account account) {
        if (account == null) {
            Log.e(TAG, "--->retrieveAttachment,the account is empty");
            return false;
        }
        return true;
    }
}
