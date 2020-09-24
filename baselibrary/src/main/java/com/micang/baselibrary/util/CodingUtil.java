package com.micang.baselibrary.util;


public class CodingUtil {
	private static final int BIT_SIZE = 0x10;
	private static final int BIZ_ZERO = 0X00;

	private static char[][] charArrays = new char[256][];

	static {
		int v;
		char[] ds;
		String temp;
		for (int i = 0; i < charArrays.length; i++) {
			ds = new char[2];
			v = i & 0xFF;
			temp = Integer.toHexString(v);
			if (v < BIT_SIZE) {
				ds[0] = '0';
				ds[1] = temp.charAt(0);
			} else {
				ds[0] = temp.charAt(0);
				ds[1] = temp.charAt(1);
			}
			charArrays[i] = ds;
		}
	}

	public static String bytesToHexString(byte[] src) {
		HexAppender helper = new HexAppender(src.length * 2);
		if (src == null || src.length <= BIZ_ZERO) {
			return null;
		}
		int v;
		char[] temp;
		for (int i = 0; i < src.length; i++) {
			v = src[i] & 0xFF;
			temp = charArrays[v];
			helper.append(temp[0], temp[1]);
		}
		return helper.toString();
	}






	private static class HexAppender {

		private int offerSet = 0;
		private char[] charData;

		public HexAppender(int size) {
			charData = new char[size];
		}

		public void append(char a, char b) {
			charData[offerSet++] = a;
			charData[offerSet++] = b;
		}

		@Override
		public String toString() {
			return new String(charData, 0, offerSet);
		}
	}



	public static byte[] hex2byte(byte[] b) {
		if (b.length % 2 != 0)
			throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[(n / 2)] = ((byte) Integer.parseInt(item, 16));
		}
		return b2;
	}



	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}




}
