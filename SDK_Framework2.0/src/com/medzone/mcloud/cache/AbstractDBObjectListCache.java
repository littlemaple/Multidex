package com.medzone.mcloud.cache;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.medzone.mcloud.database.CloudDatabaseHelper;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;
import com.medzone.framework.data.bean.BaseDatabaseObject;
import com.medzone.framework.data.bean.BaseIdDatabaseObject;
import com.medzone.framework.data.model.AbstractListCache;
import com.medzone.framework.util.Args;
import com.medzone.framework.util.Asserts;
import com.medzone.framework.Config;

/**
 * 缓存职责，他的实例对象作为缓存对象的容器。通过维护实例，达到缓存的目的。 这个类提供并实现了，缓存类通用的方法。
 * 
 * @author Robert
 * @since 2.0
 * @param <T>
 *            该类实例维护的对象类型；
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDBObjectListCache<T extends BaseDatabaseObject> extends AbstractListCache<T> {

	protected final String	tag			= getClass().getSimpleName();
	public static final int	INVALID_ID	= -1;

	protected Class<T>		parameterizedClazz;

	private Account			account;

	public void setAccountAttached(Account account) {
		this.account = account;
	}

	public Account getAccountAttached() {
		return this.account;
	}

	// ----------------------------------------------------------

	public AbstractDBObjectListCache() {
		super();
		parameterizedClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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

	// ========================================
	// DB管理
	// ========================================

	@Override
	public void flush() {
		Args.notNull(snapshot(), "snapshot");
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			for (T item : snapshot()) {
				if (item.isInvalidate()) {
					dao.createOrUpdate(item);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush(T item) {
		Args.notNull(item, "item");
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			if (item.isInvalidate()) {
				dao.createOrUpdate(item);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush(List<T> list) {
		Args.notNull(list, "list");
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			List<T> cloneList = (List<T>) ((ArrayList<T>) list).clone();
			for (T item : cloneList) {
				if (item.isInvalidate()) {
					dao.createOrUpdate(item);
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void asyncFlush() {
		AsyncTask<Void, Void, Boolean> flushTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				flush();
				return true;
			}
		};
		flushTask.execute();
	}

	@Override
	public void asyncFlush(T item) {
		Args.notNull(item, "item");
		AsyncTask<T, Void, Boolean> flushTask = new AsyncTask<T, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(T... params) {
				for (T item : params) {
					flush(item);
				}
				return true;
			}
		};
		flushTask.execute(item);
	}

	@Override
	public void asyncFlush(List<T> list) {
		Args.notNull(list, "list");
		final List<T> tmp = list;
		AsyncTask<Void, Void, Boolean> flushTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				flush(tmp);
				return true;
			}
		};
		flushTask.execute();
	}

	@Override
	public int update(T item) {
		Args.notNull(item, "item");
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			if (item.isInvalidate()) {
				return dao.update(item);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return INVALID_ID;
	}

	@Override
	public int delete(T item) {
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			return dao.delete(item);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return INVALID_ID;
	}

	/**
	 * @param primaryId
	 *            主键Id，使用主键Id删除
	 */
	@Override
	public int delete(long primaryId) {
		Asserts.check(primaryId != INVALID_ID, "primaryId");
		try {
			Dao<T, Long> dao = CloudDatabaseHelper.getInstance().getDao(parameterizedClazz);
			return dao.deleteById(primaryId);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return INVALID_ID;
	}

	@Override
	public void asyncDelete(long primaryId) {
		Args.check(primaryId != INVALID_ID, "primaryId");
		AsyncTask<Long, Void, Boolean> flushTask = new AsyncTask<Long, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Long... params) {
				for (Long item : params) {
					delete(item);
				}
				return true;
			}
		};
		flushTask.execute(primaryId);
	}

	@Override
	public void asyncDelete(T item) {
		Args.notNull(item, "item");
		AsyncTask<T, Void, Boolean> flushTask = new AsyncTask<T, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(T... params) {
				for (T item : params) {
					delete(item);
				}
				return true;
			}
		};
		flushTask.execute(item);
	}

	public void asyncDelete(List<T> list) {
		Args.notNull(list, "list");
		final List<T> tmp = list;
		AsyncTask<Void, Void, Boolean> flushTask = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				for (T item : tmp) {
					delete(item);
				}
				return true;
			}
		};
		flushTask.execute();
	}

	// ========================================
	// 内存管理
	// ========================================

	@Override
	public void addOrUpdate(T item) {
		for (T t : snapshot()) {
			if (t.isSameRecord(item)) {
				t.cloneFrom(item);
				return;
			}
		}
		add(item);
	}

	/**
	 * 给定主键id,判断是否已有对象缓存在列表中
	 * 
	 * @param primaryId
	 * @return 如果缓存队列中存在该id,则返回.否则返回为空.
	 */
	@Override
	public T hitMemoryId(int primaryId) {
		for (T t : snapshot()) {
			if (t instanceof BaseIdDatabaseObject) {
				BaseIdDatabaseObject tmp = (BaseIdDatabaseObject) t;
				if (tmp.getId() == primaryId) {
					Log.i(tag, "尝试在内存中命中：" + primaryId + "成功!");
					return t;
				}
			}
			else {
				throw new IllegalArgumentException("非法的内存命中请求，无法命中无主键的对象。");
			}
		}
		Log.w(tag, "尝试在内存中命中：" + primaryId + "失败!");
		return null;
	}

}
