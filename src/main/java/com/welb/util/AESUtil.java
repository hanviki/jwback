package com.welb.util;

import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author 001225 加解密工具类 2019年5月30日
 */
public class AESUtil {

	private static final Logger logger = Logger.getLogger(AESUtil.class);

	// 加密
	public static String Encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			logger.error("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			logger.error("Key长度不是16位");
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		return new String(Base64.getEncoder().encode(encrypted));// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	// 解密
	public static String Decrypt(String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				logger.error("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				logger.error("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = Base64.getDecoder().decode(sSrc.getBytes());// 先用base64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original, "utf-8");
				return originalString;
			} catch (Exception e) {
				logger.error("AES解密异常" + e.getMessage(), e);
				return null;
			}
		} catch (Exception ex) {
			logger.error("AES解密异常" + ex.getMessage(), ex);
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		/*String cKey = "dzyzgdsbdzyzgdsb";
		// 需要加密的字串
//		String cSrc = "{\"scope\":\"0118\",\"content\":\"{\"检验单号\":\"2346585\",\"病人姓名\":\"王正华\",\"病人年龄\":\"50 岁\",\"组合项目\":\"血气分析仪+电解质Cobas b123\",\"临床科室\":\"急诊内科\",\"临床诊断\":\"西医:意识障碍\",\"联系人\":\"徐胜恩\",\"危急项目\":\"二氧化碳分压：9.00(mmHg      )；\",\"详细信息\":\"未填写\"}\"\"title\":\"测试数据\",\"source\":\"LIS\",\"dynamiccode\":\"C0A34C442A834467\"}";
		String cSrc = "zhangsan";
		System.out.println(cSrc);
		// 加密
		String enString = Encrypt(cSrc, cKey);
//		System.out.println("加密后的字串是：" + enString);

		// 解密
		String DeString = Decrypt("J7hZReijO6rXMLf6hNKGCA==", cKey);
		System.out.println("解密后的字串是：" + DeString);*/

		String a=AESUtil.Decrypt("%2F1QBdZleKkuK8cIt%2FSbo%20g%3D%3D","lkPYrWAiDcexfs0Wu4mjAeCXYxJZfm0SAy4QAi8x0KKR9Ixyuj8QPbH4SMvMwNhEILmLV3Fy6lAIeKZoKozgYaMnVooac0Y325zeoxTMwAVTU7MYo3TO5xb%2FoxSqg08BMRrJl4idHyUREXHkrgFqQkqKwFFmyWxSX6cmN%2B4I%2FPc5xPP4YYQTdTA%2BLWuA3k%2B34LXUgCfRuVswZMpGPkX89Q%3D%3D");
		System.out.println("解密后的字串是：" + a);
	}

}
