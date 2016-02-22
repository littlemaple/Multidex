package com.medzone.mcloud.cache;

import java.util.ArrayList;
import java.util.List;

import com.medzone.framework.data.bean.BaseIdDatabaseObject;
import com.medzone.framework.data.navigation.Paging;
import com.medzone.framework.data.navigation.Stepable;
import com.medzone.framework.util.Args;

public abstract class AbstractPagingListCache<T extends BaseIdDatabaseObject, S extends Stepable<S>> extends AbstractBaseIdDBObjectListCache<T> {

	public AbstractPagingListCache() {
		super();
	}

	public abstract List<T> read(Paging<S> paging);

	@Override
	public List<T> read() {
		return read(new Paging<S>());
	}

	/**
	 * 
	 * 在缓存实例中有持有多个相同key的项，则一并进行移除。如果能够确保内存中的数据项是唯一的，则无需覆写。 当前无法确保内存中的数据项是唯一的。
	 */
	@Override
	public void remove(T item) {
		Args.notNull(item, "item");
		List<T> temp = null;
		for (T o : snapshot()) {
			if (o == null) {
				continue;
			}
			if (o.isSameRecord(item)) {
				if (temp == null) {
					temp = new ArrayList<T>();
				}
				temp.add(o);
			}
		}
		if (temp != null) {
			for (T t : temp) {
				super.remove(t);
			}
		}
	}

}
