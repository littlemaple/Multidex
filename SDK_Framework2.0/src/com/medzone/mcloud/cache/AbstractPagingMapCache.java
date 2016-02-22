package com.medzone.mcloud.cache;

import com.medzone.framework.data.bean.BaseIdDatabaseObject;
import com.medzone.framework.data.navigation.Paging;
import com.medzone.framework.data.navigation.Stepable;
import com.medzone.framework.util.Args;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPagingMapCache<K,T extends BaseIdDatabaseObject, S extends Stepable<S>> extends AbstractBaseIdDBObjectMapCache<K,T> {

	public AbstractPagingMapCache() {
		super();
	}

	public abstract List<T> read(K key,Paging<S> paging);


	@Override
	public List<T> read(K key) {
		return read(key,new Paging<S>());
	}

	/**
	 * 
	 * 在缓存实例中有持有多个相同key的项，则一并进行移除。如果能够确保内存中的数据项是唯一的，则无需覆写。 当前无法确保内存中的数据项是唯一的。
	 */
	public void remove(K key,T item) {
		Args.notNull(item, "item");
		List<T> temp = null;
		for (T o : snapshot()) {
			if (o == null) {
				continue;
			}
			if (o.isSameRecord(item)) {
				if (temp == null) {
					temp = new ArrayList<>();
				}
				temp.add(o);
			}
		}
		if (temp != null) {
			for (T t : temp) {
				super.remove(key,t);
			}
		}
	}

}
