package com.belonk.crypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by sun on 2022/3/31.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class RSADemo {
	//~ Static fields/constants/initializer

	public static final String CHARSET = "UTF-8";
	public static final String RSA_ALGORITHM = "RSA";

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static class StringKeyPair {
		private final String publicKey;
		private final String privateKey;

		public StringKeyPair(String publicKey, String privateKey) {
			this.publicKey = publicKey;
			this.privateKey = privateKey;
		}

		public String getPublicKey() {
			return publicKey;
		}

		public String getPrivateKey() {
			return privateKey;
		}
	}

	public static StringKeyPair createKeys(int keySize) {
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}

		//初始化KeyPairGenerator对象,密钥长度
		kpg.initialize(keySize);
		//生成密匙对
		KeyPair keyPair = kpg.generateKeyPair();
		//得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
		//得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
		return new StringKeyPair(publicKeyStr, privateKeyStr);
	}

	/**
	 * 得到公钥
	 *
	 * @param publicKey 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		return key;
	}

	/**
	 * 得到私钥
	 *
	 * @param privateKey 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		return key;
	}

	/**
	 * 公钥加密
	 *
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicEncrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		}
		byte[] resultDatas = out.toByteArray();
		IOUtils.closeQuietly(out);
		return resultDatas;
	}

	/**
	 * 私钥加密
	 *
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicDecrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	public static void main(String[] args) throws Exception {
		StringKeyPair keyPair = RSADemo.createKeys(1024 * 2);
		String publicKey = keyPair.getPublicKey();
		String privateKey = keyPair.getPrivateKey();
		System.out.println("公钥: \n" + publicKey);
		System.out.println("私钥： \n" + privateKey);

		// String str = "{\"code\":\"000000\",\"msg\":\"successful\",\"data\":{\"stage\":0,\"canPlay\":true,\"monsters\":[{\"id\":1006,\"level\":1,\"exp\":120,\"rarity\":1,\"expLimit\":120,\"canUp\":true,\"canPK\":true,\"canEvolve\":false,\"awarded\":false,\"ack\":48,\"hp\":389,\"skill\":15,\"leftHp\":389,\"model\":5,\"factor\":\"1.3\",\"gender\":1},{\"id\":1011,\"level\":1,\"exp\":120,\"rarity\":1,\"expLimit\":120,\"canUp\":true,\"canPK\":true,\"canEvolve\":false,\"awarded\":false,\"ack\":48,\"hp\":415,\"skill\":2,\"leftHp\":415,\"model\":14,\"factor\":\"1.2\",\"gender\":1},{\"id\":1083,\"level\":1,\"exp\":48,\"rarity\":1,\"expLimit\":120,\"canUp\":false,\"canPK\":true,\"canEvolve\":false,\"awarded\":false,\"ack\":48,\"hp\":397,\"skill\":6,\"leftHp\":397,\"model\":7,\"factor\":\"1.2\",\"gender\":1},{\"id\":1084,\"level\":1,\"exp\":48,\"rarity\":1,\"expLimit\":120,\"canUp\":false,\"canPK\":true,\"canEvolve\":false,\"awarded\":false,\"ack\":49,\"hp\":399,\"skill\":7,\"leftHp\":399,\"model\":12,\"factor\":\"1.3\",\"gender\":0},{\"id\":1085,\"level\":1,\"exp\":48,\"rarity\":1,\"expLimit\":120,\"canUp\":false,\"canPK\":true,\"canEvolve\":false,\"awarded\":false,\"ack\":48,\"hp\":392,\"skill\":15,\"leftHp\":392,\"model\":2,\"factor\":\"1.1\",\"gender\":0}]},\"auth\":\"ymhsrgq7etzw8bmv\"}";
		// String str = "string再来点中文";
		String str = "string再来点中文😄😄😄😄";
		System.out.println("公钥加密——私钥解密");
		System.out.println("明文：" + str);
		System.out.println("明文大小：" + str.getBytes().length);
		String encodedData = RSADemo.publicEncrypt(str, RSADemo.getPublicKey(publicKey));
		System.out.println("密文：\n" + encodedData);
		String decodedData = RSADemo.privateDecrypt(encodedData, RSADemo.getPrivateKey(privateKey));
		System.out.println("解密后文字: \n" + decodedData);

		System.out.println("私钥加密——公钥解密");

		encodedData = RSADemo.privateEncrypt(str, RSADemo.getPrivateKey(privateKey));
		System.out.println("密文：\n" + encodedData);
		decodedData = RSADemo.publicDecrypt(encodedData, RSADemo.getPublicKey(publicKey));
		System.out.println("解密后文字: \n" + decodedData);
	}
}
