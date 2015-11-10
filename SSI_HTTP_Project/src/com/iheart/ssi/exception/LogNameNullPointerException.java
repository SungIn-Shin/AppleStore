package com.iheart.ssi.exception;

public class LogNameNullPointerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public LogNameNullPointerException() {
	
	}
	
	public LogNameNullPointerException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		//
		if(msg == null){
			return super.getMessage();
		}
		return msg;
	}
	
	
	
}
