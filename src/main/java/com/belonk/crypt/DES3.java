package com.belonk.crypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES（或称为Triple DES）是三重数据加密算法（TDEA，Triple Data Encryption Algorithm）块密码的通称。
 * 它相当于是对每个数据块应用三次DES加密算法。由于计算机运算能力的增强，原版DES密码的密钥长度变得容易
 * 被暴力破解；3DES即是设计用来提供一种相对简单的方法，即通过增加DES的密钥长度来避免类似的攻击，而不
 * 是设计一种全新的块密码算法。
 * <p>
 * <p>Created by Dendy on 2015/12/8.
 *
 * @author sunfuchang03@126.com
 * @version 0.1
 * @since 1.0
 */
public class DES3 {
	private static final String ALGORITHM = "DESede"; // 定义 加密算法,可用
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	/**
	 * 生产24字节的key。
	 */
	public static int num = 168;//24字节

	public static class EncodedResult<T> {
		private T key;
		private T data;

		public EncodedResult() {
		}

		public EncodedResult(T key, T data) {
			this.key = key;
			this.data = data;
		}

		public T getKey() {
			return key;
		}

		public void setKey(T key) {
			this.key = key;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
	}


	/**
	 * 加密。
	 *
	 * @param src
	 * @param keybyte
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(byte[] src, byte[] keybyte) throws Exception {
		if (src == null || src.length == 0)
			return null;
		if (keybyte == null || keybyte.length == 0)
			throw new RuntimeException("Must provide the key.");
		// 生成密钥
		SecretKey secretKey = new SecretKeySpec(keybyte, ALGORITHM);
		// 加密
		Cipher c1 = Cipher.getInstance(ALGORITHM);
		c1.init(Cipher.ENCRYPT_MODE, secretKey);
		return byte2hex(c1.doFinal(src));
	}

	/**
	 * 加密。
	 *
	 * @param src
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(byte[] src, String keyStr) throws Exception {
		if (src == null || src.length == 0)
			return null;
		if (keyStr == null || keyStr.length() == 0)
			throw new RuntimeException("Must provide the key.");
		byte[] keybyte = hex2Byte(keyStr);
		SecretKey secretKey = new SecretKeySpec(keybyte, ALGORITHM);
		// 加密
		Cipher c1 = Cipher.getInstance(ALGORITHM);
		c1.init(Cipher.ENCRYPT_MODE, secretKey);
		return byte2hex(c1.doFinal(src));
	}

	public static EncodedResult<String> encrypt(byte[] src) throws Exception {
		if (src == null || src.length == 0)
			return null;
		// 生成key
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(num);
		SecretKey secretKey = kg.generateKey();
		// 加密
		Cipher c1 = Cipher.getInstance(ALGORITHM);
		c1.init(Cipher.ENCRYPT_MODE, secretKey);
		return new EncodedResult<String>(byte2hex(secretKey.getEncoded()), byte2hex(c1.doFinal(src)));
	}

	/**
	 * 解密。
	 *
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static byte[] decrypt(String src, byte[] keybyte) throws Exception {
		if (src == null || src.length() == 0)
			return null;
		if (keybyte == null || keybyte.length == 0)
			throw new RuntimeException("Must provide the key.");
		SecretKey secretKey = null;
		secretKey = new SecretKeySpec(keybyte, ALGORITHM);
		Cipher c1 = Cipher.getInstance(ALGORITHM);
		c1.init(Cipher.DECRYPT_MODE, secretKey);
		return c1.doFinal(hex2Byte(src));
	}

	/**
	 * 解密。
	 *
	 * @param src
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String src, String keyStr) throws Exception {
		if (src == null || src.length() == 0)
			return null;
		if (keyStr == null || keyStr.length() == 0)
			throw new RuntimeException("Must provide the key.");
		byte[] keybyte = hex2Byte(keyStr);
		SecretKey secretKey = new SecretKeySpec(keybyte, ALGORITHM);
		Cipher c1 = Cipher.getInstance(ALGORITHM);
		c1.init(Cipher.DECRYPT_MODE, secretKey);
		return c1.doFinal(hex2Byte(src));
	}

	private static byte[] hex2Byte(String str) {
		byte[] bytes = new byte[str.length() / 2];
		int len = bytes.length;
		for (int i = 0; i < len; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	// 转换成十六进制字符串
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
//            if (n < b.length - 1) {
//                hs = hs + ":";
//            }
		}
		return hs;
	}

	public static void main(String[] args) throws Exception {
		String s = "DES3加密算法测试";
		// 3des是用3个des密钥加密一串明文，48个0~f。
		String pass = "0123456789abcdef9876543210abcdef0123456789abcdef";
		String ret = DES3.encrypt(s.getBytes("utf-8"), pass);
		System.out.println("加密后：" + ret);
		byte[] b = DES3.decrypt(ret, pass);
		System.out.println("解密后：" + new String(b, "utf-8"));

		EncodedResult rets = DES3.encrypt(s.getBytes("utf-8"));
		System.out.println("加密后：" + ret);
		b = DES3.decrypt((String) rets.getData(), (String) rets.getKey());
		System.out.println("解密后：" + new String(b, "utf-8"));
	}
}
