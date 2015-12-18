package com.iheart.ssi.exception;

/**
 * @author 성인
 * 지원하지 않는 캐릭터 셋 
 */
public class NotSupportCharsetException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMsg;
	
	public NotSupportCharsetException(String msg){
		this.errorMsg = msg;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return errorMsg;
	}

	@Override
	public void printStackTrace() {
		// TODO Auto-generated method stub
		super.printStackTrace();
	}
	//
	
}
