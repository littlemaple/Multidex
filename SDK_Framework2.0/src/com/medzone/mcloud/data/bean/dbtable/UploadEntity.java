package com.medzone.mcloud.data.bean.dbtable;

import java.io.File;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.medzone.framework.data.bean.BaseIdDatabaseContent;
import com.medzone.framework.util.Args;
import com.medzone.framework.Config;
import com.medzone.mcloud.data.bean.java.UploadBlockEntity;

/**
 * 如果你创建了一个UploadEntity，则默认表示为Pending状态。
 *
 * @author Robert
 */
@DatabaseTable
public final class UploadEntity extends BaseIdDatabaseContent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String NAME_FIELD_TYPE = "type";
    public static final String NAME_FIELD_FILE_NAME = "file_name";
    public static final String NAME_FIELD_FILE_ADDRESS = "file_local_address";
    public static final String NAME_FIELD_UPLOAD_STATE = "upload_state";
    public static final String NAME_FIELD_LOCAL_RECORD_ID = "local_record_id";
    public static final String NAME_FIELD_DECLARE_CLASS = "declare_class";

    public static final int STATE_PENDING = -1;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_ACCEPTED = 2;
    public static final int STATE_CANCELED = 3;


    public static final int TYPE_FH = 0x1011;
    public static final int TYPE_CHECKLIST = 0x1012;

    /**
     * 文件名，经过处理的文件名。文件名处理方法见：{@link #UploadEntity(String, File)}
     */
    @DatabaseField(columnName = NAME_FIELD_FILE_NAME)
    private String mFileName;
    /**
     * 测量类型
     */
    @DatabaseField(columnName = NAME_FIELD_TYPE)
    private int mType;

    /**
     * 本地对应的数据Id,根据类型mType跟mLocalRecordId可查找到指定的数据
     */
    @DatabaseField(columnName = NAME_FIELD_LOCAL_RECORD_ID)
    private long mLocalRecordId;

    /**
     * 供查找指定数据
     */
    @DatabaseField(columnName = NAME_FIELD_DECLARE_CLASS)
    private String declareClaszz;

    /**
     * 本地文件路径
     */
    @DatabaseField(columnName = NAME_FIELD_FILE_ADDRESS)
    private String mLocalFilePath;
    /**
     * 上传的状态
     */
    @DatabaseField(columnName = NAME_FIELD_UPLOAD_STATE)
    private int mState;

    private File mFile;
    final boolean isMultiEnable = true;
    UploadBlockEntity nextBlockEntity;

    private String remotePath;

    public void updateNextBlockEntity(UploadBlockEntity nextEntity) {
        nextEntity.bindParentEntity(this);
        nextBlockEntity = nextEntity;
    }

    /**
     * @param filename 要上传的文件名，建议取名为：{type}-{recordid}-{sizeX}.{ext}，其中： {type} =
     *                 测量类型，如：bp, bs, oxy ... {recordid} = 测量数据的云端 ID {sizeX} =
     *                 文件大小的十六进制 {ext} = 文件实际扩展名，如：wav
     * @param file     待上传的文件
     */
    public UploadEntity(String filename, File file) {
        mFileName = filename;
        mFile = file;
        if (Config.isDeveloperMode) {
            Args.notNull(mFileName, "mFileName");
        }
        setState(STATE_PENDING);
    }

    public UploadEntity() {
    }

    public void setLocalRecordId(long mLocalRecordId) {
        this.mLocalRecordId = mLocalRecordId;
    }

    public long getLocalRecordId() {
        return mLocalRecordId;
    }

    public void setDeclareClazz(String declareClaszz) {
        this.declareClaszz = declareClaszz;
    }

    public String getDeclareClazz() {
        return this.declareClaszz;
    }

    public int getState() {
        return mState;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public String getLocalFilePath() {
        return mLocalFilePath;
    }

    public void setLocalFilePath(String mLocalFilePath) {
        this.mLocalFilePath = mLocalFilePath;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public String getFileName() {
        return mFileName;
    }

    public File getFile() {
        if (mFile == null) {
            mFile = new File(getLocalFilePath());
        }
        return mFile;
    }

    public long totalFileSize() {
        if (mFile == null) {
            return 0;
        }
        return mFile.length();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof UploadEntity) {
            UploadEntity op = (UploadEntity) o;
            return TextUtils.equals(getFileName(), op.getFileName());
        }
        return super.equals(o);
    }

    public String getRemotePath() {
        return remotePath;
    }

    public UploadEntity setRemotePath(String remotePath) {
        this.remotePath = remotePath;
        return this;
    }
}
