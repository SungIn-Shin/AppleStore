package com.iheart.slkp.client;

import java.nio.ByteBuffer;

public class SLKPUtil {
	/**
	 * @param value
	 * @return
	 * int 값을 Byte배열로 변환
	 */
	public static byte[] intToByteArray(int value){
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte)(value >> 24);
		byteArray[1] = (byte)(value >> 16);
		byteArray[2] = (byte)(value >> 8);
		byteArray[3] = (byte)(value);		
		return byteArray;
	}
	
	public static byte[] shortToByteArray(short value){
		//
		return new byte[] {(byte) ((value & 0xFF00) >> 8), (byte) (value & 0x00FF)}; 
	}
	
	public static short byteArrayToShort(byte[] value, int offset){
		return (short) ((value[offset] << 8) | (value[offset + 1]  & 0xff));
	}
	
	
	public static ByteBuffer makeOfProtocol(String msg, String type) {
		//
		int msgLength = msg.getBytes().length;
		
		int maxLength = 2 + 2 + msgLength + 1;
		
		byte[] typeToByte = type.getBytes();
		// Body의 길이 총 byte[] 의 길이 = body의 길이 + 4byte
		// int 는 4byte이기 때문에 2byte로 Header의 length를 2로 잡으려면 short로 변환해야한다.
		short len = (short) msgLength;		
		
		byte[] lenToByte = shortToByteArray(len);
		
		// 바이트 버퍼의 길이를 4(헤더의 길이) + body의 길이로 지정
				ByteBuffer bb = ByteBuffer.allocate(maxLength);
				bb.clear(); // ByteBuffer의 position값을 limit값과 동일하게 둔다.
				
				/** SLKProtocol HEADER Start **/
				// Type
				bb.limit(2);
				bb.put(typeToByte);
				// Length
				bb.position(2);
				bb.limit(4);
				bb.put(lenToByte);
				/** SLKProtocol HEADER End **/
				
				
				/** SLKProtocol BODY Start **/
				bb.position(4);
				bb.limit(maxLength-1);
				bb.put(msg.getBytes());
				/** SLKProtocol BODY End **/
		return bb;
	}
}
