package ins.sino.claimcar.commom.vo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.axis.encoding.Base64;
import jxl.demo.Write;


public class AesUtils {

	private static AesUtils instance=null;
	
	public AesUtils(){
		
	}
	
	public static AesUtils getInstance(){
		if (instance==null)
		instance= new AesUtils();
		return instance;
	}
	
    /**
     * 加密String明文输入,经过BASE64编码String密文输出
     * 
     * @param text,keystr,ivstr
     * @return
     * Wing.GL
     */
        
    public static String encrypt(String text, String keystr, String iv) throws Exception
    {
    	//String ivStr = "0000000000000000";
    	String ivStr = iv;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] ivBytes = new byte[16];

        byte[] b= keystr.getBytes("UTF-8");
        byte[] v = ivStr.getBytes("UTF-8");

        int len= b.length; 
        int len2 = v.length;

        if (len > keyBytes.length) len = keyBytes.length;
        if (len2> ivBytes.length) len2 = ivBytes.length;

        System.arraycopy(b, 0, keyBytes, 0, len);
        System.arraycopy(v,0,ivBytes,0,len2);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
        Base64 decoder = new Base64();
        byte [] results = cipher.doFinal(text.getBytes("UTF8"));
        String text1= new  String(decoder.encode(results));
        return text1;
    }

    /**
     * 解密 以BASE64形式String密文输入,String明文输出
     * 
     * @param text,keystr,ivstr
     * @return
     */
    public static String decrypt(String text, String keystr, String iv) throws Exception
    {
    	//String ivStr = "0000000000000000";
    	String ivStr = iv;
    	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes= new byte[16];
        byte[] ivBytes = new byte[16];
        byte[] b= keystr.getBytes("UTF-8");
        byte[] v = ivStr.getBytes("UTF-8");
        int len= b.length; 
        int len2 = v.length;
        if (len > keyBytes.length) len = keyBytes.length;
        if (len2> ivBytes.length) len2 = ivBytes.length;
               System.arraycopy(b, 0, keyBytes, 0, len);
        System.arraycopy(v,0,ivBytes,0,len2);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        Base64 decoder = new Base64();
        byte [] results = cipher.doFinal(decoder.decode(text));
        String text1=new String(results,"UTF-8");
        return  text1;
    }

    public static void main(String[] args) throws Exception {

        String text = "lksjdflkjsd;lfjksd;lkjsfdsfsdfsdfjjskjfldksjflksdjlfkjsdlkfjsldkjflsdjf";
        String key = "1234567812345678";
        String iv = "0000000000000000";
        String strenc = AesUtils.encrypt(text,key,iv);//加密
        System.out.println("加密结果:" + strenc);
        String strDes = AesUtils.decrypt(strenc,key,iv);//解密
        System.out.println("解密结果:" + strDes);

    }

    public static void charOutStream(String SCdATE ,String test) {
    	//利用File 找到操作对象
    	File file =new File("D:"+File.separator +"demo"+File.separator+ test+".txt");
    	if(!file.getParentFile().exists()){
    		file.getParentFile().mkdir();
    	}
    	//准备输出流
    	try {
			Writer out =new FileWriter(file,true);
			out.write(SCdATE+"\r\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
}