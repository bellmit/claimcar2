/******************************************************************************
 * CREATETIME : 2009-8-25 上午10:29:20
 * FILE       : com.sinosoft.sso.intfutil.SSOIntfUtils
 * MODIFYLIST ：Name       Date            Reason/Contents
 *          --------------------------------------------------------------------
 *
 ******************************************************************************/
package ins.sino.claimcar.common.web.util;

import java.io.ByteArrayOutputStream;

/**
 * 单点登录加密工具类，请勿修改
 * @author ★LiuPing
 * @modified: ☆LiuPing(2009-8-25 上午10:29:20): <br>
 */
public class SSOIntfUtils {
	private static final String hexString="Aa0gOv$xyP6k8ZTw";
	private static final String Separate="@@"; 

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str){
		// 根据默认编码获取字节数组
		byte[] bytes=str.getBytes();
		StringBuilder sb=new StringBuilder(bytes.length*2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for(int i=0;i<bytes.length;i++){
			sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
			sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制数字解码成字符串,适用于所有字符
	 */
	public static String decode(String miStr){
		ByteArrayOutputStream baos=new ByteArrayOutputStream(miStr.length()/2);
		// 将每2位16进制整数组装成一个字节
		for(int i=0;i<miStr.length();i+=2){
			baos.write((hexString.indexOf(miStr.charAt(i))<<4 |hexString.indexOf(miStr.charAt(i+1))));
		}
		return new String(baos.toByteArray());
	}

	/**
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String[] strs){
		StringBuffer enCodeStr=null;
		for(String str:strs){
			if(enCodeStr==null){
				enCodeStr=new StringBuffer(str);
			}else{
				enCodeStr.append(Separate);
				enCodeStr.append(str);
			}
		}
		return encode(enCodeStr.toString());
	}
	
	/**
	 * 将16进制数字解码成字符串,适用于所有字符
	 */
	public static String[] decodeToArry(String miStr){
		String deCodeStr=decode(miStr);
		if(deCodeStr==null)return null;
		return deCodeStr.split(Separate);
	}
}
