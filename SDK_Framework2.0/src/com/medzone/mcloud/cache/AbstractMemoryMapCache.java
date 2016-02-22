package com.medzone.mcloud.cache;

import com.medzone.framework.Log;
import com.medzone.framework.data.bean.BaseDatabaseObject;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;
import com.medzone.framework.Config;

import java.util.List;

/**
 * Created by 44260 on 2015/12/9.
 */
public abstract class AbstractMemoryMapCache<K, T extends BaseDatabaseObject> extends AbstractDBObjectMapCache<K, T> {

    public void addOrUpdate(K key, T item) {
        if (item == null)
            return;
        if (!containsKey(key)) {
            Log.e(TAG, "缓存没有存该键，进行初始化操作");
            put(key, item);
            return;
        }
        for (T t : get(key)) {
            if (t.isSameRecord(item)) {
                t.cloneFrom(item);
                return;
            }
        }
        get(key).add(item);
    }

    public void addOrUpdate(K key, List<T> itemList) {
        for (int i = 0; i < (itemList == null ? 0 : itemList.size()); i++) {
            addOrUpdate(key, itemList);
        }
    }

    public void addAll(K key, int location, List<T> itemList) {
        throw new NullPointerException("addAll方法还未完善");
    }

    public void addAll(K key, List<T> itemList) {
        throw new NullPointerException("addAll方法还未完善");
    }

    public T get(K key, int location) {
        throw new NullPointerException("get方法还未完善");
    }

    public T hitMemoryId(int primaryId) {
        for (T t : snapshot()) {
            if (t instanceof BaseIdDatabaseObject) {
                BaseIdDatabaseObject tmp = (BaseIdDatabaseObject) t;
                if (tmp.getId() == primaryId) {
                    return t;
                }
            } else {
                if (!Config.isDeveloperMode)
                    return null;
                else
                    throw new IllegalArgumentException("非法的内存命中请求，无法命中无主键的对象。");
            }
        }
        return null;
    }

    public int indexOf(K key, T item) {
        throw new NullPointerException("方法还未完善");
    }

    public void remove(K key, int location) {
        if (!containsKey(key) || get(key).size() >= location)
            return;
        get(key).remove(location);
    }

    public void remove(K key, T item) {
        if (!containsKey(key))
            return;
        get(key).remove(item);
    }

    @Override
    public int update(T item) {
        return 0;
    }

    @Override
    public int delete(long primaryId) {
        return 0;
    }

    @Override
    public void asyncDelete(long primaryId) {

    }

    @Override
    public int size(K key) {
        return get(key) == null ? 0 : get(key).size();
    }
}
