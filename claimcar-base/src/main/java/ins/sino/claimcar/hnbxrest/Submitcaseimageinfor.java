package ins.sino.claimcar.hnbxrest;

import ins.framework.dao.database.DatabaseDao;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.vo.PrpLcaseImageinfoMainVo;
import ins.sino.claimcar.hnbxrest.service.HnfastPayInfoService;
import ins.sino.claimcar.hnbxrest.vo.CasebankVo;
import ins.sino.claimcar.hnbxrest.vo.CaseimageVo;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;
import ins.sino.claimcar.hnbxrest.vo.RspCode;
import ins.sino.claimcar.hnbxrest.vo.SubmitcaseimageinforVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
/**
 * 4.2.快处快赔系统发送图片信息给保险公司
 */

public class Submitcaseimageinfor extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(Submitcaseimageinfor.class);
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired 
	HnfastPayInfoService hnfastPayInfoService;
    
    public Submitcaseimageinfor() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    SubmitcaseimageinforVo submitcaseimageinforVo = new SubmitcaseimageinforVo();
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonStr = "";
        PrpLcaseImageinfoMainVo mainVo=new PrpLcaseImageinfoMainVo();
        try{
            InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String temp = "";
            while(( temp = bufferedReader.readLine() )!=null){
                jsonStr += temp;
            }
            read.close();
            
            JSONObject rejson = JSONObject.fromObject(jsonStr);
            Map<String,Object> classMap = new HashMap<String,Object>();
            classMap.put("caseimagelist",CaseimageVo.class);
            classMap.put("casebanklist",CasebankVo.class);
            submitcaseimageinforVo = (SubmitcaseimageinforVo)JSONObject.toBean(rejson,SubmitcaseimageinforVo.class,classMap);
            checkRequest(submitcaseimageinforVo);
    	    out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.SUCCESS,"成功")));
    	    out.flush();
	        out.close();
	        
	     }catch(Exception e){
        	 out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.FAIL,"错误信息:"+e.getMessage())));
             e.printStackTrace();
        }
        try {
    		if(submitcaseimageinforVo!=null){
                 mainVo=hnfastPayInfoService.saveOfPrplCaseImageInfoMain(submitcaseimageinforVo);
    			logger.info("照片主表数据保存成功");
    		 }
		} catch (Exception e) {
			logger.error("河南快赔接口照片保存错误信息：",e);
			
        }
        
        
        try {
   	    //将河南快赔传过来的图片上传到影像系统
   	    if(submitcaseimageinforVo!=null){
   	    	ReqImageBaseRootVo imageBaseRootVo=params(submitcaseimageinforVo);
   	    	XydImageUploadAllMethodService xydImageUploadAllMethodService=new XydImageUploadAllMethodService();
   	    	List<ReqPhotoVo> photos = photos(submitcaseimageinforVo.getCaseimagelist());
			String ip=SpringProperties.getProperty("YX_URL");
   	    	String port=SpringProperties.getProperty("YX_PORT");
   	    	String key=SpringProperties.getProperty("YX_key");
   	    	String id=SpringProperties.getProperty("YX_ID");
   	    	ReqParameterVo parameterVo =new ReqParameterVo();
   	    	parameterVo.setAppCode(CodeConstants.APPCODEL2);
   	    	parameterVo.setSunIcmsIp(ip);
   	    	parameterVo.setSocketNo(port!=null?Integer.valueOf(port).intValue():null);
   	    	parameterVo.setKey(key);
   	    	parameterVo.setId(id);
   	    	String path=System.getProperty("user.dir");
   	    	String returnXml=xydImageUploadAllMethodService.saveImageLocation(imageBaseRootVo, photos, parameterVo,"hn",null,path);
   	    	if(mainVo!=null){
	   	    	 if(StringUtils.isNotBlank(returnXml) && returnXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
	   	    		mainVo.setUploadFlag("1");
	   	    		hnfastPayInfoService.updateOfPrplCaseImageInfoMain(mainVo);
	   	    	  }else{
	   	    		mainVo.setUploadFlag("0");
	   	    		hnfastPayInfoService.updateOfPrplCaseImageInfoMain(mainVo);
	   	    	  }
	   	       }
   	   }
   	 } catch (Exception e) {
   		logger.error("信雅达影像上传接口报错：",e);
		}
	    	
       }
	
	
	
	private void checkRequest(SubmitcaseimageinforVo submitcaseimageinforVo)throws Exception{
        if(StringUtils.isBlank(submitcaseimageinforVo.getCasenumber())){
            throw new IllegalArgumentException("快赔报案号不能为空");
        }
        if(StringUtils.isBlank(submitcaseimageinforVo.getInscaseno())){
            throw new IllegalArgumentException("保险报案号不能为空");
        }
        if(StringUtils.isBlank(submitcaseimageinforVo.getDatatype())){
            throw new IllegalArgumentException("接口数据类型为空");
        }
        
        if(submitcaseimageinforVo.getCaseimagelist()==null||submitcaseimageinforVo.getCaseimagelist().size()==0){
                throw new IllegalArgumentException("涉案照片信息不能为空");
            }
        
	   if(!"0".equals(submitcaseimageinforVo.getDatatype())){
          if(submitcaseimageinforVo.getCasebanklist()==null||submitcaseimageinforVo.getCasebanklist().size()==0){
            throw new IllegalArgumentException("银行卡补录信息不能为空");
          }
	}
    }
	
	
	/**
	 * 时间转换方法
	 * Date 类型转换 String类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate){
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	/**
	 * 组装请求数据
	 * @param submitcaseimageinforVo
	 * @return
	 */
	private ReqImageBaseRootVo params(SubmitcaseimageinforVo submitcaseimageinforVo){
		ReqImageBaseRootVo root=new ReqImageBaseRootVo();
		BaseDataVo baseDataVo=new BaseDataVo();
		ReqMetaDataVo metaDataVo=new ReqMetaDataVo();
		baseDataVo.setComCode("50000000");
		baseDataVo.setUserCode("0000000000");
		baseDataVo.setUserName("0000000000");
		baseDataVo.setOrgCode("DHIC");
		baseDataVo.setOrgName("鼎和保险");
		baseDataVo.setRoleCode(CodeConstants.APPROLE);
		ReqBatchVo batchVo=new ReqBatchVo();
		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
		batchVo.setBusiNo(submitcaseimageinforVo.getInscaseno());
		batchVo.setComCode("50000000");
		ReqVtreeVo vtreeVo=new ReqVtreeVo();
		vtreeVo.setAppCode(CodeConstants.APPCODECLAIM);
		vtreeVo.setAppName(CodeConstants.APPNAMECLAIM);
		
		ReqFatherNodeVo fatherNodeVo=new ReqFatherNodeVo();
		fatherNodeVo.setId(CodeConstants.APPCODEL2);
		fatherNodeVo.setName(CodeConstants.APPNAMEL2);
		fatherNodeVo.setBarCode("");
		fatherNodeVo.setChildFlag("0");
		fatherNodeVo.setMinpages("0");
		fatherNodeVo.setMaxpages("99999");
		fatherNodeVo.setReseize("800*600");
		fatherNodeVo.setRight(CodeConstants.YXRIGHTCODE);
		
		List<ReqSonNodeVo> sonNodeVos1=new ArrayList<ReqSonNodeVo>();
		ReqSonNodeVo sonNodeVo1=new ReqSonNodeVo();
		sonNodeVo1.setId(CodeConstants.HNKPFNODE);
		sonNodeVo1.setMaxpages("99999");
		sonNodeVo1.setMinpages("0");
		sonNodeVo1.setName("快赔案件影像资料");
		sonNodeVo1.setReseize("800*600");
		sonNodeVo1.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo1.setChildFlag("0");
		sonNodeVo1.setBarCode("");
		
		
		List<ReqSonNodeVo> sonNodeVos=new ArrayList<ReqSonNodeVo>();
		if(submitcaseimageinforVo.getCaseimagelist()!=null && submitcaseimageinforVo.getCaseimagelist().size()>0){
		  if("0".equals(submitcaseimageinforVo.getDatatype())){
			    commonParam(sonNodeVos,CodeConstants.HNKPSNODE1,"事故照片信息");
			}else if("1".equals(submitcaseimageinforVo.getDatatype())){
				commonParam(sonNodeVos,CodeConstants.HNKPSNODE2,"银行卡信息");
			}else{
				commonParam(sonNodeVos,CodeConstants.HNKPSNODE3,"定损照片信息");
			}
		}
		sonNodeVo1.setSonNodes(sonNodeVos);
		sonNodeVos1.add(sonNodeVo1);
		fatherNodeVo.setSonNodes(sonNodeVos1);
		List<ReqFatherNodeVo> fatherNodeVos=new ArrayList<ReqFatherNodeVo>();
		fatherNodeVos.add(fatherNodeVo);
		vtreeVo.setFatherNodeVos(fatherNodeVos);
		batchVo.setVtreeVo(vtreeVo);
		ReqPagesVo reqPagesVo=new ReqPagesVo();
		List<ReqPageNodeVo> pages=new ArrayList<ReqPageNodeVo>();
		if(sonNodeVos!=null && sonNodeVos.size()>0){
			for(ReqSonNodeVo sonNodeVo:sonNodeVos){
				ReqPageNodeVo pageNodeVo=new ReqPageNodeVo();
				pageNodeVo.setId(sonNodeVo.getId());
				pageNodeVo.setAction("ADD");
				List<ReqPageVo> pageVos=new ArrayList<ReqPageVo>();
				for(CaseimageVo photoVo:submitcaseimageinforVo.getCaseimagelist()){
						ReqPageVo pageVo=new ReqPageVo();
						if(StringUtils.isNotBlank(photoVo.getImageurl())){
							String[] str1=photoVo.getImageurl().split("/");
							pageVo.setFileName(str1[str1.length-1]);
							pageVo.setRemark("车损图片");
							pageVo.setUpUser("0000000000");
							pageVo.setUpUsername("0000000000");
							pageVo.setUpOrgname("总公司");
							pageVo.setUp_org("50000000");
							pageVo.setUpTime(DateFormatString(new Date()));
							pageVos.add(pageVo);
						}
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
	
	private List<ReqPhotoVo> photos(List<CaseimageVo> caseimageVos) throws Exception{
		List<ReqPhotoVo> photoVos=new ArrayList<ReqPhotoVo>();
		if(caseimageVos!=null && caseimageVos.size()>0){
			for(CaseimageVo caseimageVo:caseimageVos){
				ReqPhotoVo photoVo=new ReqPhotoVo();
				if(StringUtils.isNotBlank(caseimageVo.getImageurl())){
					photoVo.setCarCaseNo(caseimageVo.getCasecarno());
					String[] str1=caseimageVo.getImageurl().split("/");
					photoVo.setImageName(str1[str1.length-1]);
					photoVo.setImageUrl(caseimageVo.getImageurl());
					photoVos.add(photoVo);
				}
				
			}
		}
		return photoVos;
	}
	
	
	private List<ReqSonNodeVo> commonParam(List<ReqSonNodeVo> sonNodeVos,String nodeId,String nodeName){
		ReqSonNodeVo sonNodeVo=new ReqSonNodeVo();
		sonNodeVo.setId(nodeId);
		sonNodeVo.setName(nodeName);
		sonNodeVo.setRight(CodeConstants.YXRIGHTCODE);
		sonNodeVo.setReseize("800*600");
		sonNodeVo.setChildFlag("1");
		sonNodeVo.setBarCode("");
		sonNodeVo.setMaxpages("99999");
		sonNodeVo.setMinpages("0");
		sonNodeVos.add(sonNodeVo);
		return sonNodeVos;
	}
}
