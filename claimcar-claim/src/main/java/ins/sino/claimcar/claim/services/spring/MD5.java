package ins.sino.claimcar.claim.services.spring;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;

/**
 * 
 *类名：MD5Util
 *描述：TODO
 *提示：TODO
 */

public class MD5 {

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串全部大写
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String signtoUpperCase(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
    }


    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * 签名对象
     * @param text 需要签名的对象
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String signObject(Object obj , String input_charset) {
        return DigestUtils.md5Hex(getContentBytes(JSON.toJSONString(obj) , input_charset));
    }
    
    /**
     * 校验签名对象正确性
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verifyObject(Object obj , String sign, String input_charset) {
    	
    	
    	String mysign = DigestUtils.md5Hex(getContentBytes(JSON.toJSONString(obj), input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}