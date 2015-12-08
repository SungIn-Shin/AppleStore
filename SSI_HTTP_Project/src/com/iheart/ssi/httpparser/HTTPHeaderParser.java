package com.iheart.ssi.httpparser;

public interface HTTPHeaderParser {
	public byte[] parseHTTPHeader(byte[] reqData);
}
