package com.iheart.ssi.exception;

/**
 * @author 성인
 * Request URI Too Long Error
 */
@SuppressWarnings("serial")
public class HTTP414Exception extends HTTPException{
	//
	private String code;
	
	

	public HTTP414Exception() {
		super();
		code = "414";
	}

	@Override
	public String getMessage() {
		//
		return getErrorCode(code);
	}
	
	
}
