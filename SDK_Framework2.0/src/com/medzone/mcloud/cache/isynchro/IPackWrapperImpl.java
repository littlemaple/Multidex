package com.medzone.mcloud.cache.isynchro;

import java.util.List;

/**
 * 
 * @author Robert
 * 
 * @param <T>
 *            原始数据类型
 * @param <S>
 *            包装器类型
 */
public interface IPackWrapperImpl<T, S> {

	/**
	 * 
	 * 装包器，将读取的列表数据格式，转化为指定的数据格式
	 * 
	 * @param t
	 *            从数据库中读取的原始数据列表
	 * @param dataType
	 *            数据标签，如血氧单次，血氧长期
	 * @return 指定类型的包装类
	 */
	S packAdd(List<T> t);

	/**
	 * 
	 * 装包器，将读取的列表数据格式，转化为指定的数据格式
	 * 
	 * @param t
	 *            从数据库中读取的原始数据列表
	 * @return 指定类型的包装类
	 */
	S packDelete(List<T> t);

	/**
	 * 解包器，将指定数据格式的包装类，拆解成列表数据格式
	 * 
	 * @param s
	 *            包装类
	 * @return 列表数据对象
	 */
	// List<T> unpackAdd(S s);
	// List<T> unpackDelete(S s);
}
