package com.iheart.ssi.exception;

/**
 * @author 성인
 * Bad Request
 */
@SuppressWarnings("serial")
public class HTTP400Exception extends HTTPException{
	//
	private String code;
	
	

	public HTTP400Exception() {
		super();
		code = "400";
	}

	@Override
	public String getMessage() {
		//
		return getErrorCode(code);
	}
	
	
}
