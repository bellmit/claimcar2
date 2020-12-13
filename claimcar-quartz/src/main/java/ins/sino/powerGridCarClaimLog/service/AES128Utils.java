package ins.sino.powerGridCarClaimLog.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * <p> </p> 
 *
 * @author: liaoyunfeng
 */
public class AES128Utils {
	// 加密
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.err.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
//        if (sKey.length() != 16) {
//            System.out.print("Key长度不是16位");
//            return null;
//        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
 
        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    /**
     * @comment: 端口唯一码生成
	 * @create.date: 2016年6月22日 ( 下午3:19:34 )
	 * @author: scd
	 * @return onlyCode 唯一码
     */
    public static String buildOnlyCode(){
    	String onlyCode = "";
    	String cKey = "1234567890123456";
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String token = sd.format(new Date());
		try {
			onlyCode = AES128Utils.encrypt(token, cKey);
			onlyCode = onlyCode.replace("+", "_");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return onlyCode;
    }
    
    public static void main(String[] args) throws Exception {
        /*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         */
        String cKey = "1234567890123456";
        // 需要加密的字串
        String cSrc = "xiao12";
       // System.out.println(cSrc);
        // 加密
        String enString = AES128Utils.encrypt(cSrc, cKey);
       //  System.out.println("加密后的字串是：" + enString);
       //System.out.println(URLEncoder.encode(enString));
        // 解密
        String onlyCode = buildOnlyCode();
        System.out.println(onlyCode);
      //  System.out.println("解密后的字串是：" + DeString);
        
      //  System.out.println(toHexString("2ED74935579127AFBABDEA1CCE71A0F7"));
        
//        System.out.println(AES128Utils.decrypt("ZFk13qBiFc2pLWfoUWIp8A==", cKey));
 //       System.out.println(AES128Utils.decrypt("tBkO8u+i5PuhcXcN9yZNxA==", cKey));
  //      System.out.println(AES128Utils.encrypt("xiao12", cKey));
        
//        xiaoming: tBkO8u+i5PuhcXcN9yZNxA==
//          xiao12: 7btMqRjsP0NO7TM0/FhTNA==
        
 //       System.err.println("xiaoming: " + URLEncoder.encode("tBkO8u+i5PuhcXcN9yZNxA=="));
 //       System.err.println("xiao12: " + URLEncoder.encode("7btMqRjsP0NO7TM0/FhTNA=="));
        
        
    }
}
