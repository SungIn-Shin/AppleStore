package com.iheart.ssi.exception;

/**
 * @author 성인
 * Service Unavailable
 */
public class HTTP503Exception extends HTTPException {
private static final long serialVersionUID = 1L;
	
	private String code;
	
	public HTTP503Exception() {
		super();
		code="503";
	}
	

	@Override
	public String getMessage() {
		//
		
		return getErrorCode(code);
	}
}
