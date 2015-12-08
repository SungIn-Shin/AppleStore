package com.iheart.ssi.httpparser;

import java.util.Map;

public interface HTTPHeaderController {
	public Map<String, String> parseHTTPHeader(String reqData);
	public byte[] createHTTPProtocol(Map<String, String> resMap);
}
