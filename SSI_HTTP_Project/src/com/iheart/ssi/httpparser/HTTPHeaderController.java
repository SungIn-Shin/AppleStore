package com.iheart.ssi.httpparser;

import java.io.InputStream;
import java.util.Map;

public interface HTTPHeaderController {
	public Map<String, String> parseHTTPHeader(InputStream reqData);
	public byte[] createHTTPProtocol(String resData);
}
