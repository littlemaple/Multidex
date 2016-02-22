package com.medzone.mcloud.data.bean;

public interface IChat {

	/**
	 * 用于IChat之间的附加操作，并返回自身的聊天实体对象
	 */
	IChat attach(IChat parent);

	/**
	 * 获取该聊天实体的父容器
	 * 
	 * @return
	 */
	IChat getParent();

	/**
	 * 本地表示该聊天对象所能参与的会话类型，目前仅允许分配一种会话。
	 * 如果有需求允许聊天对象参与群聊，或者其他类别，可以按照位判断。
	 * 
	 * @author Robert
	 * @return
	 */
	int getIChatSessionType();

	/**
	 * 本地用于唯一标识一段会话，用它来降低因为接口问题造成的聊天模块耦合。
	 * 
	 * @author Robert
	 * @return
	 */
	String getIChatInterlocutorId();

	/**
	 * 云端用于唯一标识一段会话，用它来降低因为接口问题造成的聊天模块耦合。
	 * 
	 * @author Robert
	 * @return
	 */
	String getIChatInterlocutorIdServer();

	String getIChatDisplayName();

	String getIChatHeadPortrait();

}
