/** 
 * @Title: MD5.java 
 * @Package com.zhidoushi.util 
 * @Description: MD5加密工具类
 * @author Niuniu Fu 
 * @mail niuniu.fu@yun.aopa.org.cn
 * @date 2014-11-10 下午11:48:51 
 * @version V1.0 
 */
package com.hongyu.reward.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author FuNiuniu
 *
 */
public class MD5 {

	/*
	 * 方法名称：getMD5 
	 * 功    能：字符串MD5加密 
	 * 参    数：待转换字符串 
	 * 返 回 值：加密之后字符串
	 */
	public static String getMD5(String sourceStr)   {
		StringBuffer resultStr = new StringBuffer("");
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr.append(new String(ob));
			}
			
			return resultStr.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(MD5.getMD5("20150107010000000150"+"3.141592653589793"));
	}
}
