package ins.sino.claimcar.claimcarYJ.Util.vo;

import java.security.MessageDigest;

public class MD5Util {
    public static String getMd5(String plainText) {  
          try {  
             MessageDigest md = MessageDigest.getInstance("MD5");  
             md.update(plainText.getBytes("utf-8"));  
             byte b[] = md.digest();  
             int i;  
             StringBuffer buf = new StringBuffer("");  
             for (int offset = 0; offset < b.length; offset++) {  
                 i = b[offset];  
                 if (i < 0)  
                     i += 256;  
                 if (i < 16)  
                     buf.append("0");  
                 buf.append(Integer.toHexString(i));  
         }  
             return buf.toString();  
         } catch (Exception e) {  
             e.printStackTrace();  
             return null;  
         }  
     }  
    public static String MD5Encode(String plainText,String characterEncoding) {  
        try {  
           MessageDigest md = MessageDigest.getInstance("MD5");  
           md.update(plainText.getBytes(characterEncoding));  
           byte b[] = md.digest();  
           int i;  
           StringBuffer buf = new StringBuffer("");  
           for (int offset = 0; offset < b.length; offset++) {  
               i = b[offset];  
               if (i < 0)  
                   i += 256;  
               if (i < 16)  
                   buf.append("0");  
               buf.append(Integer.toHexString(i));  
       }  
           return buf.toString();  
       } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
       }  
   }
}