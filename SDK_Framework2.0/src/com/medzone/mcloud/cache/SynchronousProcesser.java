package com.medzone.mcloud.cache;

import android.util.SparseArray;

import com.medzone.framework.data.bean.BaseIdDatabaseObject;

import java.util.List;

/**
 * Created by 44260 on 2015/12/9.
 */
public interface SynchronousProcesser<T extends BaseIdDatabaseObject> {
    long getDownSerial();

    void syncSerial(int downSerial);

    SparseArray<T> syncDownloadBackground(List<T> retList, int dataType);

    SparseArray<T> syncUploadBackground(List<T> retList, int dataType);

    SparseArray<T> syncDeleteBackground(List<String> delIDs, int dataType);

    void syncCallInPostExecute(String tag, SparseArray<T> newItem, int getTaskType);
}
