package com.iheart.ssi.exception;

/**
 * @author 성인
 * Method Not Allowed 존재하는 Method이지만 사용하지 않는 메소드를 호출함.
 */
public class HTTP405Exception extends HTTPException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1469480156939148071L;
	private String code;

	public HTTP405Exception() {
		super();
		code = "405";
	}

	@Override
	public String getMessage() {
		//
		return getErrorCode(code);
	}
}
