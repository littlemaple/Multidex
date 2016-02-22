package com.medzone.mcloud.exception;

/**
 * 
 * @author Robert
 * 
 */
public class ContextMissException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6966305004396158581L;

	public ContextMissException() {
		this("Context reference is missing ,please check compontent's lifecycle.");
	}

	public ContextMissException(String detailMessage) {
		super(detailMessage);
	}

}