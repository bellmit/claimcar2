package ins.sino.claimcar.pinganUnion.util;

import com.alibaba.fastjson.JSON;
import ins.platform.utils.Base64EncodedUtil;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.pinganUnion.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;


/**
 * 平安联盟接口对接MD5+RSA加解密工具类
 */
public class PingAnMD5Utils {
    private static Logger logger = LoggerFactory.getLogger(PingAnMD5Utils.class);

    /**
     * 对请求报文进行数字验签
     * @param data 签名内容
     *  1、请求报文签名内容为：siteCode+reqData（json字符串的base64编码）
     *  2、响应报文签名内容为：code+msg+respData（json字符串的base64编码）
     * @return
     */
    public static String sign(String data){
        //String privateKeyString = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALKFlJDJPuDCaWqt4m0Tr4bZmwD3bvYheFwhi9XjK5asvkEj4LO++Lw4vTMAUBIchgP7XvwPoBXbGXGo2T2MAFIzqFhuFtoHW1/9WHB9Qv/6mlnM/0hsLy2cPBCWS/umafbO8+gYX7bTnDh+RuDSTLVqkwtvldGNc6KAR2TUrv21AgMBAAECgYA/w1Z9IGRFAyuD3vew3gcjszWZ8sa/QtUqkxDPJIS9HJYXzijECsHs+JcbUFFJJsb3RSF4Bly3cbp0zkpJoWe7Wz+yM4l/PVKLo4Uxf6chMbKtd1avYd7ruR3sGfXNVMyQoipfXToU0pbGreQaYsMuEciMafvM3GxaiU+NgQdnQQJBAOodPLC4FoPANFpm9w4MIZXU+6mTLGX1TusTVU2/lUlXNDZw5wvKZ9G0nFujmWBvTspEzRaesUA/ldACyCOsBpsCQQDDNetoubl3Ka77ebsszSt27UXhlVT5pVXxRbi/V0zgBvoUR35A+Y0prIoPnyn4+nBe9vJgkyLWHTnFrjorcinvAkBcjo6N91m/YN/R1d9ayGUGzZtpYWwuKxu2SIBzKokk47sawuw1dyIwgE1I1ZcvzxHBWg4TIU5Gbl0WTeM+ZyZ9AkB9PtsLFh4ollXuguvUks6QPyvW0Dj0819wrYsbOKfaFJ/e4v/eMD6hvlHWNAh59jSiuU5JKB3xwk6OxnoL5fOBAkEA6A6hPjQwOuiSEh3ePCKQWy1ACOVnM8GcR+q6eKUfJcqUurixHeWeAAVlskTgFCm9YRuo3FpSHxDObk5dwTv3Zw==";
        String privateKeyString = SpringProperties.getProperty("pingan_privateKey");
        if (StringUtils.isBlank(privateKeyString)){
            throw new IllegalArgumentException("私钥未配置");
        }

        try {
            byte[] keyBytes = Base64EncodedUtil.decodeBase64(privateKeyString);//对私钥做base64解码
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(priKey);
            signature.update(data.getBytes("UTF-8"));//data为要生成签名的源数据字节数组
            return Base64EncodedUtil.encode(Base64EncodedUtil.encode(signature.sign()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("平安联盟接口对接-数字签名报错：", e);
        }
        return null;
    }

    /**
     * 验证数字签名
     * @param data 验签内容，与生成数字签名内容顺序一致
     * @return
     */
    public static Boolean verifySign(String data, String originSignature){
        //String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyhZSQyT7gwmlqreJtE6+G2ZsA9272IXhcIYvV4yuWrL5BI+Czvvi8OL0zAFASHIYD+178D6AV2xlxqNk9jABSM6hYbhbaB1tf/VhwfUL/+ppZzP9IbC8tnDwQlkv7pmn2zvPoGF+205w4fkbg0ky1apMLb5XRjXOigEdk1K79tQIDAQAB";
        String publicKeyString = SpringProperties.getProperty("pingan_publicKey");
        if (StringUtils.isBlank(publicKeyString)){
            throw new IllegalArgumentException("公钥未配置");
        }

        try {
            byte[]publicKey = Base64EncodedUtil.decodeBase64(publicKeyString);//对提供的公钥做base64解码
            byte[] sign = Base64EncodedUtil.decodeBase64(Base64EncodedUtil.decode(originSignature));//签名需要做二次base64解码
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(pubKey);
            signature.update(data.getBytes("UTF-8"));
            return signature.verify(sign);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("平安联盟接口对接-验证数字签名报错：", e);
        }
       return false;
    }


    /**
     * 生成签名
     * @param responseVo
     * @return
     */
    public static ResponseVo gengerateSign(ResponseVo responseVo){
        if (responseVo.getRespData() == null){//如果响应内容为空，默认设置空json字符串
            responseVo.setRespData(new HashMap<String,String>());
        }
        String respData = Base64EncodedUtil.encode(JSON.toJSONString(responseVo.getRespData()));//Base64编码
        String code = responseVo.getCode();
        String msg = responseVo.getMsg();
        String signature = sign(code + msg + respData);

        responseVo.setRespData(respData);
        responseVo.setSignature(signature);
        return responseVo;
    }
}
