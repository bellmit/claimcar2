/******************************************************************************
 * Copyright 2010-2011 the original author or authors.
 * CREATETIME : 2015-7-24 下午07:19:32
 ******************************************************************************/
package ins.platform.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;


/**
 * 使用 apache 对 字符串进行base64编码解码
 * @Copyright Copyright (c) 2015
 * @Company www.sinosoft.com.cn
 * @author ★<a href="mailto:liuping-gz@sinosoft.com.cn">LiuPing</a>
 * @since 2015-7-24 下午07:19:32
 */
public class Base64EncodedUtil {

	private static final String CharsetName = "UTF-8";

	/**
	 * 将字符串编码为BASE64字符串
	 * @param str
	 * @return
	 * @modified: ☆LiuPing(2016年7月24日 ): <br>
	 */
	public static String encode(String str) {
		String base64Str = null;
		try{
			base64Str = encode(str.getBytes(CharsetName));
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return base64Str;
	}

	/**
	 * 将二进制数据编码为BASE64字符串
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 * @modified: ☆LiuPing(2013-10-7 下午02:59:52): <br>
	 */
	public static String encode(byte[] binaryData) {
		String base64Str;
		try{
			base64Str = new String(Base64.encodeBase64(binaryData),CharsetName);
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null;
		}
		return base64Str;

	}
	
	/**
	 * 对base64字符串解码
	 * @param base64Str
	 * @return
	 * @throws IOException
	 * @modified: ☆LiuPing(2013-10-7 下午02:36:49): <br>
	 */
	public static String decode(String base64Str) {
		String str = base64Str;
		try{
			byte[] bt = Base64.decodeBase64(base64Str.getBytes(CharsetName));
			str = new String(bt,CharsetName);
		}catch(UnsupportedEncodingException e){
			throw new IllegalStateException("数据解密错误"+e.getMessage(),e);
		}
		return str;
	}

	/**
	 * 对base64字符串解码
	 * @param base64Str
	 * @return
	 * @throws IOException
	 * @modified: ☆LiuPing(2013-10-7 下午02:36:49): <br>
	 */
	public static byte[] decodeBase64(String base64Str) {
		byte[] bt;
		try{
			bt = Base64.decodeBase64(base64Str.getBytes(CharsetName));
		}catch(UnsupportedEncodingException e){
			throw new IllegalStateException("数据解密错误"+e.getMessage(),e);
		}
		return bt;
	}
}
