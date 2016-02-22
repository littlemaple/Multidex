package com.medzone.mcloud.lbs;

import java.util.HashMap;

/**
 * 
 * @author Robert
 * @category 将不同平台的SDK返回值，封装成心云平台统一的Key，Value格式
 */
public interface ILocationSerializate<T> {

	// 装包器，将第三方返回值，装包成心云格式
	HashMap<String, ?> packWrapper(T t);

	// 解包器，将心云格式转化成第三方格式
	// 绝大多数的情况下，不需要与第三方平台进行通讯
	T reversePackWrapper(HashMap<String, ?> map);

	// 将第三方的错误码，转化为心云的错误码
	int packErrorCode(int lbsCode);
}
