package com.iheart.ssi.exception;

/**
 * @author 성인
 * 사용자가 요청하는 페이지가 없을때 나타나는 Exception
 */
@SuppressWarnings("serial")
public class HTTP404Exception extends HTTPException{
	//
	private String code;
	
	

	public HTTP404Exception() {
		super();
		code = "404";
	}
	
	@Override
	public String getMessage() {
		//
		return getErrorCode(code);
	}
	
	
}
