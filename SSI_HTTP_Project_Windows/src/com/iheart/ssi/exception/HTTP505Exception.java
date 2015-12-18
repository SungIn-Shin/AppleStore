package com.iheart.ssi.exception;

public class HTTP505Exception extends HTTPException {
private static final long serialVersionUID = 1L;
	
	private String code;
	
	public HTTP505Exception() {
		super();
		code="505";
	}
	

	@Override
	public String getMessage() {
		//
		return getErrorCode(code);
	}
}
