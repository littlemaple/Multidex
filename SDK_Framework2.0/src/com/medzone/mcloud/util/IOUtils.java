package com.medzone.mcloud.util;
import java.util.Date;

public class IOUtils {
	public static byte[] intToByteArray(final int integer) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
			byteArray[3 - n] = (byte) (integer >>> (n * 8));

		return (byteArray);
	}

	public static byte[] intToByteArray(final int integer, byte[] input,
			int offset) {
		int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer
				: integer)) / 8;

		for (int n = 0; n < byteNum; n++)
			input[offset + 3 - n] = (byte) (integer >>> (n * 8));

		return input;
	}
	
	public static byte[] shortToByteArray(final short integer) {
		int byteNum = 2;
		byte[] input = new byte[byteNum];
		
		for (int n = 0; n < byteNum; n++)
			input[ 1 - n] = (byte) (integer >>> (n * 8));

		return input;
	}
	
	public static int milli2int(long milli){
		Date date = new Date(milli);
		int total = 0;
		int year = date.getYear() - 112;
		int month = date.getMonth() + 1;
		int day = date.getDate();
		int hour = date.getHours();
		int minute = date.getMinutes();
		int sec = date.getSeconds();
		total = year << 26;
		total |= month << 22;
		total |= day << 17;
		total |= hour << 12;
		total |= minute << 6;
		total |= sec;
		return total;
	}

	public static byte[] shortToByteArray(final short integer, byte[] input,
			int offset) {
		int byteNum = 2;

		for (int n = 0; n < byteNum; n++)
			input[offset + 1 - n] = (byte) (integer >>> (n * 8));

		return input;
	}
	
	@SuppressWarnings("deprecation")
	public static Date intToDate(int total) {

		int year = (total & 0xFC000000) >> 26; // 6
		int month = (total & 0x03C00000) >> 22; // 4
		int day = (total & 0x003E0000) >> 17; // 5
		int hour = (total & 0x0001F000) >> 12; // 5
		int minute = (total & 0x00000FC0) >> 6; // 6
		int sec = (total & 0x0000003F); // 6
		year += 112;
		return new Date(year, month - 1, day, hour, minute, sec);
	}

	public static Date bytesToDate(byte[] input, int offset) {
		int total = byteArrayToInt(input, offset);
		return intToDate(total);
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}
	
	public static int byteArrayToInt2(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = i * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	public static short byteArrayToShort(byte[] b, int offset) {
		short value = 0;
		value += (b[0 + offset] & 0x000000FF) * 256;
		value += (b[1 + offset] & 0x000000FF);
		return value;
	}
	
	public static short byteArrayToShort2(byte[] b, int offset) {
		short value = 0;
		value += (b[0 + offset] & 0x000000FF) ;
		value += (b[1 + offset] & 0x000000FF) * 256;
		return value;
	}
	
	public static String cmdToString(byte[] cmd, int bufferSize) {
		char[] HEX = "0123456789ABCDEF".toCharArray();
		char[] chs = new char[bufferSize * 3];
		for (int j = 0, offset = 0; j < bufferSize; j++) {
			chs[offset++] = HEX[cmd[j] >> 4 & 0xf];
			chs[offset++] = HEX[cmd[j] & 0xf];
			chs[offset++] = ' ';
		}
		return new String(chs);
	}
	
	/**
	 * byte����ת����16�����ַ�����
	 * @param src
	 * @return
	 */
	public static String[] bytesToHexStrings(byte[] src){
		if( src == null || src.length <= 0 ){
			return null;
		}
		String[] str = new String[src.length];
		
		for( int i =0; i<src.length; i++)
		{
			int v = src[i]&0xff;
			String hv = Integer.toHexString(v);
			if( hv.length() <2 ){
				str[i] = "0";
			}
			str[i] = hv;
		}
		return str;
	}
	
	/**
	 * byte����ת����16�����ַ���
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder();
		if( src == null || src.length <= 0 ){
			return null;
		}
		for( int i =0; i<src.length; i++)
		{
			int v = src[i]&0xff;
			String hv = Integer.toHexString(v);
			if( hv.length() <2 ){
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static byte[] byteToByteArray(byte in) {
		byte[] content = new byte[1];
		content[0] = in;
		return (content);
	}

	public static short signedConvertToUnsigned(byte inbyte) {
		short outbyte = inbyte;
		if (inbyte < 0)
			outbyte += 256;
		return outbyte;
	}

	public static byte getComplement(byte input) {
		if (input > 0)
			return input;

		byte output = (byte) ((~input | 0x80) + 1);
		return output;
	}

}
