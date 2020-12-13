package ins.sino.claimcar.pinganUnion.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.pinganUnion.enums.PingAnImageCodeEnum;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.RespImageDataVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqBatchVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqFatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqMetaDataVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPageVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPagesVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqParameterVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqPhotoVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqSonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqVtreeVo;
import ins.sino.claimcar.xydImageUpload.service.XydImageUploadAllMethodService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
/**
 * 平安影像
 * @author Think
 *
 */
@Service("pingAnImageService")
public class PingAnImageServiceImpl implements PingAnHandleService {
	private static Logger logger = LoggerFactory.getLogger(PingAnImageServiceImpl.class);
	 @Autowired
	 private RegistService registService;

	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("平安影像接口信息报文--------------------------------》{}",respData);
		ResultBean resultBean=ResultBean.success();
		List<RespImageDataVo> respImageDataVos=JSON.parseArray(respData,RespImageDataVo.class);
        try {
        	if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("------报案号为空-----");
			}
   	    //将平安联盟上传的图片上传到影像系统
   	    if(respImageDataVos!=null && respImageDataVos.size()>0){
   	    	ReqImageBaseRootVo imageBaseRootVo=params(registNo,respImageDataVos);
   	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
   	    	List<ReqPhotoVo> photos = photos(respImageDataVos);
			String ip=SpringProperties.getProperty("YX_URL");
   	    	String port=SpringProperties.getProperty("YX_PORT");
   	    	String key=SpringProperties.getProperty("YX_key");
   	    	String id=SpringProperties.getProperty("YX_ID");
   	    	ReqParameterVo parameterVo =new ReqParameterVo();
   	    	parameterVo.setAppCode(CodeConstants.APPCODECLAIM);
   	    	parameterVo.setSunIcmsIp(ip);
   	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
   	    	parameterVo.setKey(key);
   	    	parameterVo.setId(id);
   	    	File file =new File("");
   	    	String path=file.getAbsolutePath();
   	    	logger.info("平安文件路径-------------------------------------------------------->{}",path);
   	    	String returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,"hn",null,path);
   	    	logger.info("信雅达影像返回信息--------------->{}", returnXml);
   	    	if(StringUtils.isNotBlank(returnXml) && returnXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
    	    }else{
    	    	if(StringUtils.isNotBlank(returnXml) && returnXml.length()>901){
    	    		resultBean.fail("平安联盟影像资料上传错误：--->"+returnXml.substring(0,900));
    	    	}else{
    	    		resultBean.fail("平安联盟影像资料上传错误");
    	    	}
    		   
    	  }
   	   }
   	 } catch (Exception e) {
   		logger.error("信雅达影像上传接口报错：",e);
   		resultBean.fail("平安联盟影像资料上传错误："+e.getMessage());
	 }
	    	
		
		return resultBean;
	}

	/**
	 * 组装请求数据
	 * @param registNo
	 * @param respImageDataVos
	 * @return
	 */
	private ReqImageBaseRootVo params(String registNo,List<RespImageDataVo> respImageDataVos){
		PrpLRegistVo  prpLRegistVo=registService.findRegistByRegistNo(registNo);
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode(prpLRegistVo.getComCode());
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("鼎和保险");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(registNo);
		batchVo.setComCode(prpLRegistVo.getComCode());
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
		//装载不同类型的图片
		Map<String,List<String>> hashMap=new HashMap<String,List<String>>();
		Set<String> imageTypeSet=new HashSet<String>();
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		List<ReqSonNodeVo> sonNodeCs=new ArrayList<ReqSonNodeVo>(0);
		List<ReqSonNodeVo> sonNodePs=new ArrayList<ReqSonNodeVo>(0);
		if(respImageDataVos!=null && respImageDataVos.size()>0){
			for(RespImageDataVo imageDataVo:respImageDataVos){
				if(imageTypeSet.add(imageDataVo.getDocumentType())){
					ReqSonNodeVo reqSonNodeVo=new ReqSonNodeVo();
					reqSonNodeVo.setId(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getSelfCode());
					reqSonNodeVo.setName(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getImageType());
					reqSonNodeVo.setRight(CodeConstants.YXRIGHTCODE);
					reqSonNodeVo.setReseize("800*600");
					reqSonNodeVo.setChildFlag("1");
					reqSonNodeVo.setBarCode("");
					reqSonNodeVo.setMaxpages("99999");
					reqSonNodeVo.setMinpages("0");
					if(CodeConstants.APPCODEL1.equals(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getRootNode())){
						sonNodeCs.add(reqSonNodeVo);
					}else{
						sonNodePs.add(reqSonNodeVo);
					}
					List<String> imageNames=new ArrayList<String>();
					imageNames.add(imageDataVo.getFileName());
					hashMap.put(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getSelfCode(),imageNames);
				}else{
					ReqSonNodeVo reqSonNodeVo=new ReqSonNodeVo();
					reqSonNodeVo.setId(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getSelfCode());
					reqSonNodeVo.setName(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getImageType());
					reqSonNodeVo.setRight(CodeConstants.YXRIGHTCODE);
					reqSonNodeVo.setReseize("800*600");
					reqSonNodeVo.setChildFlag("1");
					reqSonNodeVo.setBarCode("");
					reqSonNodeVo.setMaxpages("99999");
					reqSonNodeVo.setMinpages("0");
					if(CodeConstants.APPCODEL1.equals(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getRootNode())){
						sonNodeCs.add(reqSonNodeVo);
					}else{
						sonNodePs.add(reqSonNodeVo);
					}
				    List<String> imageNames=hashMap.get(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getSelfCode());
				    imageNames.add(imageDataVo.getFileName());
				    hashMap.put(PingAnImageCodeEnum.getEnumType(imageDataVo.getDocumentType()).getSelfCode(),imageNames);
				}
			}
		}
		if(sonNodeCs!=null && sonNodeCs.size()>0){
			//索赔清单类型
			ReqFatherNodeVo fatherNodeCVo=new ReqFatherNodeVo();
			fatherNodeCVo.setId(CodeConstants.APPCODEL1);
			fatherNodeCVo.setName(CodeConstants.APPNAMEL1);
			fatherNodeCVo.setBarCode("");
			fatherNodeCVo.setChildFlag("0");
			fatherNodeCVo.setMinpages("0");
			fatherNodeCVo.setMaxpages("99999");
			fatherNodeCVo.setReseize("800*600");
			fatherNodeCVo.setRight(CodeConstants.YXRIGHTCODE);
			fatherNodeCVo.setSonNodes(sonNodeCs);
			fatherNodeVos.add(fatherNodeCVo);
		}
		if(sonNodePs!=null && sonNodePs.size()>0){
			//车损损失类型
			ReqFatherNodeVo fatherNodePVo=new ReqFatherNodeVo();
			fatherNodePVo.setId(CodeConstants.APPCODEL2);
			fatherNodePVo.setName(CodeConstants.APPNAMEL2);
			fatherNodePVo.setBarCode("");
			fatherNodePVo.setChildFlag("0");
			fatherNodePVo.setMinpages("0");
			fatherNodePVo.setMaxpages("99999");
			fatherNodePVo.setReseize("800*600");
			fatherNodePVo.setRight(CodeConstants.YXRIGHTCODE);
			fatherNodePVo.setSonNodes(sonNodePs);
			fatherNodeVos.add(fatherNodePVo);
		}
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(imageTypeSet!=null && imageTypeSet.size()>0){
			for(String ImageType:imageTypeSet){
				ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
				pageNodeVo.setId(PingAnImageCodeEnum.getEnumType(ImageType).getSelfCode());
				pageNodeVo.setAction("ADD");
				List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
				List<String> imageUrls=hashMap.get(PingAnImageCodeEnum.getEnumType(ImageType).getSelfCode());
				for(String imageName:imageUrls){
					ReqPageVo pageVo=new ReqPageVo();
						pageVo.setFileName(imageName);
						pageVo.setRemark("平安联盟单证图片");
						pageVo.setUpUser("0000000000");
						pageVo.setUpUsername("0000000000");
						pageVo.setUpOrgname("总公司");
						pageVo.setUp_org("00000000");
						pageVo.setUpTime(DateFormatString(new Date()));
						pageVos.add(pageVo);
				}
				pageNodeVo.setPageVos(pageVos);
				pages.add(pageNodeVo);
			}
		}
		reqPagesVo.setReqPageNodeVos(pages);
		batchVo.setReqPagesVo(reqPagesVo);
		metaDataVo.setBatchVo(batchVo);
		root.setBaseDataVo(baseDataVo);
		root.setMetaDataVo(metaDataVo);
		return root;
		
	}
    /**
     * 文件图片访问路径组装
     * @param caseimageVos
     * @return
     * @throws Exception
     */
	private List<ReqPhotoVo> photos(List<RespImageDataVo> respImageDataVos) throws Exception{
		List<ReqPhotoVo> photoVos=new ArrayList<ReqPhotoVo>();
		if(respImageDataVos!=null && respImageDataVos.size()>0){
			String imageUrl=SpringProperties.getProperty("pingan_imageUrl");
			String replaceImageUrl=SpringProperties.getProperty("replace_imageUrl");
			for(RespImageDataVo caseimageVo:respImageDataVos){
				ReqPhotoVo photoVo=new ReqPhotoVo();
					photoVo.setImageName(caseimageVo.getFileName());
					photoVo.setImageUrl(caseimageVo.getUrl().replace(replaceImageUrl,imageUrl));
					photoVos.add(photoVo);
				
			}
		}
		return photoVos;
	}
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private static String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
}
