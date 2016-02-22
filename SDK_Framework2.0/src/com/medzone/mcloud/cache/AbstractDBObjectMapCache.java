package com.medzone.mcloud.cache;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.medzone.mcloud.database.CloudDatabaseHelper;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseDatabaseObject;
import com.medzone.framework.data.model.AbstractMapCache;
import com.medzone.framework.Config;

@SuppressWarnings("unchecked")
public abstract class AbstractDBObjectMapCache<K, V extends BaseDatabaseObject> extends AbstractMapCache<K, V> {

    protected Class<V> itemClass;
    protected Account account;

    public void setAccountAttached(Account account) {
        this.account = account;
    }

    public Account getAccountAttached() {
        return account;
    }

    public AbstractDBObjectMapCache() {
        super();
        itemClass = (Class<V>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public boolean isValid() {
        if (getAccountAttached() == null) {
            if (Config.isDeveloperMode) {
                Log.w(getClass().getSimpleName(), "检测到绑定的账号为空.（通常是发生顶号、账号登陆失效）");
            }
            return false;
        }
        return true;
    }

    @Override
    public void flush() {
        if (cache == null || cache.size() == 0) {
            return;
        }
        try {
            Dao<V, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            List<V> item;

            for (Map.Entry<K, List<V>> entry : cache.snapshot().entrySet()) {
                item = entry.getValue();
                if (item != null && item.size() > 0) {
                    for (V obj : item) {
                        if (obj.isInvalidate()) {
                            dao.createOrUpdate(obj);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush(V item) {
        try {
            Dao<V, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            if (item.isInvalidate()) {
                dao.createOrUpdate(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void flush(List<V> item) {
        try {
            Dao<V, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            List<V> cloneList = (List<V>) ((ArrayList<V>) item).clone();
            for (V tmp : cloneList) {
                if (tmp.isInvalidate()) {
                    dao.createOrUpdate(tmp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncFlush(V item) {
        AsyncTask<V, Integer, Boolean> task = new AsyncTask<V, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(V... params) {
                for (V val : params) {
                    flush(val);
                }
                return true;
            }
        };
        task.execute(item);
    }

    @Override
    public void asyncFlush(final List<V> item) {
        AsyncTask<Void, Integer, Boolean> task = new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                flush(item);
                return true;
            }
        };
        task.execute();
    }

    @Override
    public int delete(V item) {
        if (item == null) {
            return 0;
        }
        try {
            Dao<V, Long> dao = CloudDatabaseHelper.getInstance().getDao(itemClass);
            if (item.isInvalidate()) {
                return dao.delete(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void asyncDelete(final V item) {
        if (item == null)
            return;
        AsyncTask<Void, Void, Boolean> flushTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                delete(item);
                return true;
            }
        };
        flushTask.execute();
    }

    public void asyncDelete(final List<V> list) {
        if (list == null)
            return;
        AsyncTask<Void, Void, Boolean> flushTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                for (V v : list) {
                    delete(v);
                }
                return true;
            }
        };
        flushTask.execute();
    }

}
