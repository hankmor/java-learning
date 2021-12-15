package com.belonk.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Algorithm3DES {

	private static Logger logger = LoggerFactory.getLogger(Algorithm3DES.class);

	public static String ALGORITHM = "DESede";//采用3des算法

	public static int num = 168;//24字节

	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	public static void encryptMode(AlgorithmData data) throws Exception {
		try {
			String src = data.getDataMing();
			SecretKey key = null;
			String keyStr = data.getKey();
			byte[] keybyte = null;
			if (keyStr == null) {
				KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
				kg.init(num);
				key = kg.generateKey();
				data.setKey(getFormattedText(key.getEncoded()));
			} else {
				keybyte = hex2Byte(keyStr);
				key = new SecretKeySpec(keybyte, ALGORITHM);
			}
			Cipher c1 = Cipher.getInstance(ALGORITHM);
			c1.init(Cipher.ENCRYPT_MODE, key);
			byte[] encoded = c1.doFinal(src.getBytes());
			data.setDataMi(getFormattedText(encoded));
		} catch (java.security.NoSuchAlgorithmException e1) {
			logger.error("不存在对应的算法实现", e1);
		} catch (javax.crypto.NoSuchPaddingException e2) {
			logger.error("对应的填充机制未提供", e2);
		} catch (Exception e3) {
			logger.error("3DES加密产生异常", e3);
		}
	}

	public static void decryptMode(AlgorithmData data) {
		try {
			String keystr = data.getKey();
			String srcstr = data.getDataMi();
			byte[] key = hex2Byte(keystr);
			byte[] src = hex2Byte(srcstr);
			SecretKey deskey = new SecretKeySpec(key, ALGORITHM);
			Cipher c1 = Cipher.getInstance(ALGORITHM);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			byte[] res = c1.doFinal(src);
			String mingwen = new String(res);
			data.setDataMing(mingwen);
//			if(data.isDoDisplay()){
//				data.setDataMing(doDisplay(mingwen));
//			}else{
//				data.setDataMing(mingwen);
//			}

		} catch (java.security.NoSuchAlgorithmException e1) {
			logger.error("不存在对应的算法实现", e1);
		} catch (javax.crypto.NoSuchPaddingException e2) {
			logger.error("对应的填充机制未提供", e2);
		} catch (Exception e3) {
			logger.error("3DES解密产生异常", e3);
		}
	}

	private static String doDisplay(String ming) {
		if (ming != null) {
			int len = ming.length();
			if (len >= 14) {
				String str1 = ming.substring(0, 6);
				String str2 = ming.substring(len - 4, len);
				String strStars = appendStars(len - 10);
				return str1 + strStars + str2;
			} else if (len >= 10) {
				String str1 = ming.substring(0, 3);
				String str2 = ming.substring(len - 3, len);
				String strStars = appendStars(len - 6);
				return str1 + strStars + str2;
			} else if (len > 0) {
				int n = len / 3;
				String str1 = ming.substring(0, n);
				String str2 = ming.substring(len - n, len);
				String strStars = appendStars(len - 2 * n);
				return str1 + strStars + str2;
			}
		}
		return null;
	}

	private static String appendStars(int num) {
		String res = "";
		for (int i = 0; i < num; i++) {
			res += "*";
		}
		return res;
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		//把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	private static byte[] hex2Byte(String str) {
		byte[] bytes = new byte[str.length() / 2];
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static void main(String[] args) throws Exception {
		AlgorithmData data = new AlgorithmData();
		String sourcestr = ":+-、\\%?&^%$#@!*()=[]";
		data.setDataMing(sourcestr);
		System.out.println("源字符串:" + data.getDataMing());
		Algorithm3DES.encryptMode(data);
		String encodedStr = data.getDataMi();
		System.out.println("加密后的字符串:" + encodedStr);
		//System.out.println("加密后的字符串的长度:"+encodedStr.length()+"源字符串长度："+sourcestr.length());
		//byte[] encodedbytestemp = hex2Byte(encodedStr);
		AlgorithmData data2 = new AlgorithmData();
		//data2.setDataMi(data.getDataMi());
		data2.setDataMi(encodedStr);
		data2.setKey(data.getKey());
		data2.setDoDisplay(true);
		Algorithm3DES.decryptMode(data2);
		System.out.println("解密后的字符串:" + data2.getDataMing());
	}

}
