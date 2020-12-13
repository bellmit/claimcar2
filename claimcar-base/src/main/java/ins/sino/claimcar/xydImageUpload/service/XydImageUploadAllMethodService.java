package ins.sino.claimcar.xydImageUpload.service;
import com.sunyard.insurance.ecm.socket.client.AutoScanApi;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.sunyardimage.vo.common.ImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqParameterVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPhotoVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XydImageUploadAllMethodService {
	private Logger logger = LoggerFactory.getLogger(XydImageUploadAllMethodService.class);
	
	
	public String saveImageLocation(ReqImageBaseRootVo imageInfoRootVo,List<ReqPhotoVo> photos,ReqParameterVo parameterVo,String flagType,Map<String,File> pqyfiles,String path){
		File file=null;
		if("bill".equals(flagType)){
			file=new File(imageInfoRootVo.getMetaDataVo().getBatchVo().getBusiNo()+"F");
		}else{
			file=new File(path+"/"+imageInfoRootVo.getMetaDataVo().getBatchVo().getBusiNo()+"F");
		}
		 
		
		logger.info("影像文件夹路径-------------------------------------------》"+path);
		System.out.println("+++++++++++++++++++++"+file.getAbsolutePath());
		String returnXml="";
		File fileZip=null;
	  try {
		if(!file.exists()){
			System.out.println("文件夹是否创建成功----------------------》"+file.mkdir());
			System.out.println("文件夹是否创建成功----------------------》"+file.mkdir());
			System.out.println("文件夹是否创建成功----------------------》"+file.mkdirs());
		  }
		if("pays".equals(flagType)){
			if(pqyfiles!=null && pqyfiles.size()>0 && photos!=null && photos.size()>0 && StringUtils.isNotBlank(photos.get(0).getImageName())){
				logger.info("----------->"+flagType+"上传图片数量："+photos.size());
				for(Map.Entry<String,File> mapFileVo:pqyfiles.entrySet()){
				if(mapFileVo.getValue()!=null && mapFileVo.getValue().exists()){
					InputStream in=new FileInputStream(mapFileVo.getValue());
					byte[] b=new byte[in.available()];
					
					logger.info("源文件路径："+mapFileVo.getValue().getAbsolutePath());
					logger.info("字节长度："+b.length);
					logger.info("压缩包路径："+file.getAbsolutePath()+"/"+mapFileVo.getValue().getName());
					in.read(b);
					OutputStream out=new FileOutputStream(file.getAbsolutePath()+"/"+mapFileVo.getValue().getName());
					out.write(b);
					if(in!=null){
					 in.close();
					}
					if(out!=null){
					 out.flush();
					 out.close();
					}
					
				}
			}
			}
		}else{
			downloadImage(photos,file);
		}
		madeXmlFile(imageInfoRootVo,file);
		fileZip=fileTofilezip(file,imageInfoRootVo.getMetaDataVo().getBatchVo().getBusiNo()+"F");
		System.out.println("++++++++++++++++"+fileZip.getAbsolutePath());
		if(fileZip!=null){
			AutoScanApi autoScanApi=new AutoScanApi(parameterVo.getSunIcmsIp(),parameterVo.getSocketNo(),parameterVo.getId()+"#"+parameterVo.getKey());
			autoScanApi.setFormat("xml");
		    returnXml=autoScanApi.ScanImageFile(parameterVo.getAppCode(), fileZip.getAbsolutePath());
		}else{
			throw new IllegalArgumentException("没有发现上传的压缩包！");
		}
	    
	  }catch (Exception e) {
		  logger.error("信雅达上传接口错误信息：",e);
	  }finally{
		if(file!=null && file.exists()){
			  File[] files=file.listFiles();
			  for(int i=0;i<files.length;i++){
				  if(files[i].exists()){
					files[i].delete();
				  }
			  }
				boolean str= file.delete();
				 if(str){
					System.out.println("+++++++++++++++++++++++++++++++++++++++临时文件夹删除成功!");
				 }
				}
		  if(fileZip!=null && fileZip.exists()){
				if(fileZip.delete()){
					System.out.println("+++++++++++++++++++++++++++++++++++++++临时文件压缩包删除成功!");
				}
				
			}
			
		
		}
	  
		return returnXml;
	}
	public String uploadImage(ReqImageBaseRootVo imageInfoRootVo,File file,ReqParameterVo parameterVo){
		String returnXml="";
	  try {
		if(!file.exists()){
			file.mkdir();
		  }
		if(file!=null){
			AutoScanApi autoScanApi=new AutoScanApi(parameterVo.getSunIcmsIp(),parameterVo.getSocketNo(),parameterVo.getId()+"#"+parameterVo.getKey());
		    returnXml=autoScanApi.ScanImageFile(parameterVo.getAppCode(), file.getAbsolutePath());
		}else{
			throw new IllegalArgumentException("没有发现需要上传的文件！");
		}
	  }catch (Exception e) {
		  logger.error("信雅达上传接口错误信息：",e);
	  }
	  
		return returnXml;
	}
	/**
	 * 下载图片并保存在特定的文件夹里
	 * @param photos
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public void downloadImage(List<ReqPhotoVo> photos,File file) throws Exception{
		if(photos!=null && photos.size()>0){
			for(ReqPhotoVo photoVo:photos){
				URL url = new URL(photoVo.getImageUrl());
				 //打开链接 
	    		 HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
	    		 //设置请求方式为"POST" 
	    		 conn.setRequestMethod("POST");
	    		 //超时响应时间为20秒 
	    		 conn.setConnectTimeout(20 * 1000); 
	    		 //通过输入流获取图片数据 
	    	     InputStream inStream = conn.getInputStream();
	    	     // 1K的数据缓冲  
	    	     byte[] bs = new byte[1024];  
	    	     // 读取到的数据长度  
	    	     int len=0;
	    	     OutputStream osStream = new FileOutputStream(file.getAbsolutePath()+"/"+photoVo.getImageName());  
	    	        // 开始读取  
	    	        while ((len = inStream.read(bs)) != -1) {   
	    	          osStream.write(bs, 0, len);
	    	        }  
	    	        // 完毕，关闭所有链接  
	    	        if(inStream!=null){
	    	        	inStream.close();
	    	        }
	    	        if(osStream!=null){
	    	        	osStream.flush();
	    	        	osStream.close();
	    	        }
			}
		}
	}
	/**
	 * 生成xml文件
	 * @param imageInfoRootVo
	 * @param file
	 */
	public void madeXmlFile(ImageBaseRootVo imageInfoRootVo,File file){
		String reqXml=ClaimBaseCoder.objToXmlUtf(imageInfoRootVo);
		System.out.println("+++++++++++++++++++++++报文"+reqXml);
		File fileXml=new File(file.getAbsolutePath()+"/busi.xml");
		if(!fileXml.exists()){
			try {
				fileXml.createNewFile();
				Document document = DocumentHelper.parseText(reqXml);
				OutputXml(document,fileXml);
			} catch (Exception e) {
				 logger.error("信雅达上传接口错误信息：",e);
			}
		}
	}
	
	public void OutputXml(Document doc,File fileXml){
		   OutputFormat format = OutputFormat.createPrettyPrint();
		try {
	        //6.//创建一个xml文件 
	        OutputStream out = new FileOutputStream(fileXml);
	        Writer wr = new OutputStreamWriter(out, "UTF-8");//用可改变编码的OutputStreamWriter代替了普通的FileWriter解决中文乱码问题   
	        XMLWriter output = new XMLWriter(wr,format);
	        //将doc输出到xml文件中
	        output.write(doc);
	        //8.关闭资源
	        if(wr!=null){
	        	wr.close();
	        }
	        if(out!=null){
	          out.close();	
	        }
	        if(output!=null){
	          output.close();	
	        }
	       
		} catch (Exception e) {
			logger.error("信雅达上传接口错误信息：",e);
		}
	}
	
	public  File fileTofilezip(File sourceFile,String fileName){
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;   
        File zipFile = new File(sourceFile.getAbsolutePath()+"/"+fileName+".zip");
        if(!sourceFile.exists()){  
        	logger.info("待压缩的文件目录："+sourceFile.getAbsolutePath()+"不存在"); 
        	return null;
        }else{  
            try {    
                if(zipFile.exists()){  
                	zipFile.delete(); 
                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){  
                    	logger.info("待压缩的文件目录：" + sourceFile.getAbsolutePath() + "里面不存在文件，无需压缩");  
                    }else{  
                        fos = new FileOutputStream(zipFile);  
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                        byte[] bufs = new byte[1024];  
                        for(int i=0;i<sourceFiles.length;i++){  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName()); 
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFiles[i]);  
                            bis = new BufferedInputStream(fis, 1024);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                            if(bis!=null){
                            	bis.close();
                            }
                            if(fis!=null){
                            	fis.close();
                            }
                        }  
                    }  
                }  
            } catch (Exception e) {  
            	logger.error("信雅达上传接口错误信息：",e);
            } finally{  
                //关闭流  
                try {  
                	if(null != zos){
                    	zos.flush();
                    	zos.close();  
                    }
                    if(fos!=null){
                    	fos.flush();
                    	fos.close();
                    }
                    
                } catch (Exception e) {
                	logger.error("信雅达上传接口错误信息：",e);
                }  
            }  
        }  
		return zipFile; 
	}
}
