package ins.sino.claimcar.hnbxrest.util;

import ins.platform.utils.Base64EncodedUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class EncryptUtils {
    
    public static String key = "s(p7~;W^";

    //加密
    public static String desEncrypt(String message) throws Exception 
    {
        try 
        {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte data[] = message.getBytes("UTF-8");
            byte[] encryptedData = cipher.doFinal(data);
            return getBASE64(encryptedData);
        } 
        catch (Exception e) 
        {  
                return null;
        }
}
    private static String getBASE64(byte[] b)
    {
        if (b == null)
            return null;
        try 
        {
            String returnstr = Base64EncodedUtil.encode(b);
            return returnstr;
        } 
        catch (Exception e) 
        {
            return null;
        }
 }
  
    //解密 
    public String deCrypt(String message) throws Exception 
    {
        try 
        {      
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte data[] = Base64EncodedUtil.encode(message).getBytes();
            byte[] encryptedData = cipher.doFinal(data);
            return new String(encryptedData);
        }
        catch (Exception e) 
        {
            //Global.getInstance().LogError(e);
            return null;
        }
}
}
