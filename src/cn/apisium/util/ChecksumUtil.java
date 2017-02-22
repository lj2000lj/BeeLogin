package cn.apisium.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {
	public static String md5(String string) {
		return md5(string.getBytes(StandardCharsets.UTF_8));
	}

	public static String md5(byte[] bytes) {
		String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] resultBytes = md5.digest(bytes);
			StringBuffer stringBuffer = new StringBuffer();
			for (byte b : resultBytes) {
				int bt = b & 0xff;
				if (bt < 16) {
					stringBuffer.append(0);
				}
				stringBuffer.append(Integer.toHexString(bt));
			}
			result = stringBuffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String md5twice(String string) {
		return md5(md5(string));
	}

	public static String salted2Md5(String string, String salt) {
		return md5(md5(string) + salt);
	}
}
