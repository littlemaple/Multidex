package com.medzone.mcloud.exception;

/**
 * 
 * @author Robert
 * 
 */
public class UnInitedException extends NullPointerException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6966305004396158581L;

	public UnInitedException(String detailMessage) {
		super(detailMessage);
	}

}