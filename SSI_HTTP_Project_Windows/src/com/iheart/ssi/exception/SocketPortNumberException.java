package com.iheart.ssi.exception;

public class SocketPortNumberException extends IllegalArgumentException {
	//
	private static final long serialVersionUID = 1L;

	private String msg;

	public SocketPortNumberException() {

	}

	public SocketPortNumberException(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		//
		if (msg == null) {
			return super.getMessage();
		}
		return msg;
	}
}
