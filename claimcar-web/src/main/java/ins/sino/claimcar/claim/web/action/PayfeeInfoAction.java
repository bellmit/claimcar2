package ins.sino.claimcar.claim.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.claim.service.PayFeeStandardService;
import ins.sino.claimcar.claim.vo.PrplFeeStandardVo;
import ins.sino.claimcar.court.dlclaim.xyvo.ImageUrlbaseVo;
import ins.sino.claimcar.court.dlclaim.xyvo.LeafVo;
import ins.sino.claimcar.court.dlclaim.xyvo.NodeImageVo;
import ins.sino.claimcar.court.dlclaim.xyvo.PageVo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 人伤赔偿标准库
 * @author yzy
 *
 */
@Controller
@RequestMapping(value="/payfeeInfo")
public class PayfeeInfoAction {
	@Autowired
	private PayFeeStandardService payFeeStandardService;
	
	@Autowired
	private AreaDictService areaDictService;
	
	@Autowired
	private CarXyImageService carXyImageService;
	
	private static Logger logger = LoggerFactory.getLogger(PayfeeInfoAction.class);
	
	
    @RequestMapping(value="/payfeeInfoList.do")
	public ModelAndView PayfeeInfoList(){
		ModelAndView mv=new ModelAndView();
		Date startDate=new Date();
		mv.addObject("conFlag","add");//表示维护页面
		mv.addObject("taskInTimeStart",startDate);
		mv.setViewName("payFee/payFeeStandardQueryList");
		return mv;
	}
    
    
    /**
     * 查询人伤费用列表
     * @param prplFeeStandardVo
     * @param start
     * @param length
     * @return
     * @throws Exception 
     */
    @RequestMapping(value="/feeStandardList.do")
	@ResponseBody
	public String feeStandardList(
			@FormModel("prplFeeStandardVo") PrplFeeStandardVo prplFeeStandardVo,//页面组装VO
			@RequestParam(value = "start", defaultValue = "0") Integer start,//分页开始位置
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {//展示条数
    	ResultPage<PrplFeeStandardVo> page=payFeeStandardService.findAllFeeStandard(prplFeeStandardVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "imageBussNo","areaCode", "areaName", "dateCode");
		logger.debug(jsonData);
		return jsonData;
	}
    
    /**
	 * 保存人伤费用标准图片信息
	 * @param file
	 * @param request
	 * @throws IOException
	 * @modified: ☆yzy(2019年12月04日 下午16:50:29): <br>
	 */
	@RequestMapping(value = "/imageInfoSave.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult importExcel(@RequestParam(value = "upImageFile",required=false) MultipartFile[] upFiles,//上传的文件
			                      HttpServletRequest request,HttpServletResponse response
			                     ){
		AjaxResult ajax=new AjaxResult();
		String dpath="";//图片删除路劲
		String dname="";//删除图片名称
		String bussNo="";//业务号
		Map<String,File> fileMaps=new HashMap<String,File>();
		String path="";//图片路径
		List<String> fileNames=new ArrayList<String>();//图片名称集合
		
		try{
			//获取页面的参数值
			String areaCode=request.getParameter("prplFeeStandardVo.areaCode");//地区编码
			String yearCode=request.getParameter("prplFeeStandardVo.yearCode");//年份
			String monthCode=request.getParameter("prplFeeStandardVo.monthCode");//月份
			logger.info("++++++++++++++++++++++地区编码："+areaCode+",年限："+yearCode+",月份："+monthCode);
			// 如果没有文件上传，MultipartFile也不会为null，可以通过调用getSize()方法获取文件的大小来判断是否有上传文件
		      if (upFiles!=null && upFiles.length > 0) {
		    	  for(int i=0;i<upFiles.length;i++){
		    	  //文件不能大于1M
		    	  if(!checkFileSize(upFiles[i].getSize(),1,"M")){
		    		 throw new IllegalArgumentException("文件不能大于1M");
		    	  }
		    	  
		    	 /* PrplFeeStandardVo prplFeeStandardVo=payFeeStandardService.findFeeStandardVoByAreaCodeAndYearCode(areaCode, yearCode);
		    	  if(prplFeeStandardVo!=null){
			    	  throw new IllegalArgumentException("该年限的地区人伤赔偿标准图已存在,不能重复添加！");
			      }*/
		    	 // 得到项目在服务器的真实根路径，如：/home/tomcat/webapp/项目名/imageFees
		    	  path = request.getSession().getServletContext().getRealPath("/imageFees/")+"/";
		         // 得到文件的原始名称，如：美女.png
		    	 System.out.println("+++++++++++++++++++"+path);
		         String fileName = upFiles[i].getOriginalFilename();
		         String fileNewName=fileName;
		         Date datet=new Date();
		         Long paySysdatet=datet.getTime();//取当前时间搓
		         // 通过文件的原始名称，可以对上传文件类型做限制，如：只能上传jpg和png的图片文件
		         if (StringUtils.isBlank(fileName)||(!fileName.endsWith("jpg") && !fileName.endsWith("png"))) {
		        	 throw new IllegalArgumentException("上传的图片格式只能为jpg或png!");
		    	  }else{//重新命名图片名字,以方便每个年限的地区照片拥有唯一的的标识
		    		  if(fileName.endsWith("jpg")){
		    			  fileNewName=areaCode+yearCode+monthCode+paySysdatet+i+".jpg";
		    		  }else{
		    			  fileNewName=areaCode+yearCode+monthCode+paySysdatet+i+".png";
		    		  }
		    	  }
		         
		          File file = new File(path, fileNewName);
		          dpath=path;
		          dname=fileNewName;
		          fileNames.add(dname);
		          if(!file.exists()){
		            	upFiles[i].transferTo(file);
		          }else{
		        	  file.delete();
		        	  if(!file.exists()){
		        		 upFiles[i].transferTo(file);
		        	  }
		        	  
		          }
		          //人伤赔偿标准案件号，地区编码拼接年份，月份编码
		          bussNo=areaCode+yearCode+monthCode;
		          Date date=new Date();
		          Long paySysdate=date.getTime();//取当前时间搓
		          String imageId=CodeConstants.paystandard+"_"+bussNo+paySysdate+"_"+i;
		          fileMaps.put(imageId,file);
		       }
		    }
		      String returnXml=carXyImageService.upImageToXyd(bussNo,fileMaps,"pays",path);
		   	    	if(StringUtils.isNotBlank(returnXml)){//上传成功再保存数据库表
			   	    	 if(StringUtils.isNotBlank(returnXml) && returnXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
			   	    	   if(fileMaps!=null && fileMaps.size()>0){
			   	    		   for(Map.Entry<String,File> vo:fileMaps.entrySet()){
			   	    			 PrplFeeStandardVo standardVo=new PrplFeeStandardVo();
						    	  List<SysAreaDictVo> sysAreaDictVos=areaDictService.findAreaCode(areaCode);
						    	  if(sysAreaDictVos!=null && sysAreaDictVos.size()>0){
						    		  standardVo.setAreaCode(areaCode);
						    		  if(StringUtils.isNotBlank(sysAreaDictVos.get(0).getFullName())){
						    			  standardVo.setAreaName(sysAreaDictVos.get(0).getFullName()); 
						    		  }else{
						    			  standardVo.setAreaName(sysAreaDictVos.get(0).getAreaName()); 
						    		  }
						    		  
						    		  standardVo.setValidStatus("1");
						    		  standardVo.setYearCode(yearCode);
						    		  standardVo.setCreateTime(new Date());
						    		  standardVo.setImageId(vo.getKey());
						    		  standardVo.setImageBussNo(bussNo);
						    		  standardVo.setMonthCode(monthCode);
						    		  SysUserVo sysUserVo=WebUserUtils.getUser();
						    		  if(sysUserVo!=null){
						    			 standardVo.setCreateUser(sysUserVo.getUserCode());
						    		  }
						    		  payFeeStandardService.saveOrUpdatePrplFeeStandardVo(standardVo);
						    		  ajax.setStatus(HttpStatus.SC_OK);
						    	  }
			   	    		   }
			   	    	   }
			   	    		
			   	    	  }else{//失败则将错误信息赋予页面
			   	    		logger.info("人伤赔偿标准上传错误信息bussNo=："+bussNo+"---------》"+returnXml);
			   	    		ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			   	    		ajax.setStatusText("错误信息提示：上传失败，影像系统异常，请稍后再试！");
			   	    	  }
			   	       }else{
			   	    	ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			   	    	ajax.setStatusText("错误信息提示：上传失败，影像系统异常，请稍后再试！");
			   	       }
		    	 
		     
			
		 }catch(Exception e){
			ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajax.setStatusText("错误信息提示："+e.getMessage());
			logger.info("人伤费用标准报错信息：", e);
		}finally{
			//上传完即时删除文件，以免消耗服务器内存空间
			if(StringUtils.isNotBlank(dpath) && fileNames!=null && fileNames.size()>0){
				for(String fileName:fileNames){
					File dfile = new File(dpath, fileName);
					if(dfile!=null && dfile.exists()){
						dfile.delete();
					}
				}
				
			}
	
			 
		}
		return ajax;
	}
    
    /**
     * 查看人伤费用标准
     * @param id
     * @return
     */
    @RequestMapping(value="/feeStandardDetailViewList.do")
    public ModelAndView feeStandardDetailView(){
    	ModelAndView mv=new ModelAndView();
		Date startDate=new Date();
		mv.addObject("conFlag","view");//表示查看页面
		mv.addObject("taskInTimeStart",startDate);
		mv.setViewName("payFee/payFeeStandardQueryList");
		return mv;
    }
    /**
	 * 根据Id查看对应的人伤费用标准
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/imageShowView.do")
	public ModelAndView imageShowView(String imageBussNo){
		ModelAndView mv=new ModelAndView();
		//PrplFeeStandardVo  prplFeeStandardVo=payFeeStandardService.findPrplFeeStandardVoById(id);
		List<PrplFeeStandardVo> prplFeeStandardVos=payFeeStandardService.findPrplFeeStandardBybussNo(imageBussNo);
		
		//图片信息
		if(prplFeeStandardVos!=null && prplFeeStandardVos.size()>0){
			SysUserVo userVo=new SysUserVo();
			userVo.setComCode("00000000");
			userVo.setUserCode("0000000000");
			userVo.setUserName("系统管理员");
			String Qurl=SpringProperties.getProperty("YX_QUrl");
			String resXml=carXyImageService.reqResourceFromXy(userVo,imageBussNo,Qurl);
			System.out.println("报文+++++++++++"+resXml);
			//如果返回成功则显示到页面上
			if(StringUtils.isNotBlank(resXml) && resXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")){
				String imgXml=changeXml(resXml);
			  if(StringUtils.isNotBlank(imgXml)){
				//将xml转换为对象
				ImageUrlbaseVo imageUrlbaseVo=ClaimBaseCoder.xmlToObj(imgXml,ImageUrlbaseVo.class);
				if(imageUrlbaseVo!=null && imageUrlbaseVo.getSydVo()!=null && imageUrlbaseVo.getSydVo().getDocVo()!=null &&
				   imageUrlbaseVo.getSydVo().getDocVo().getDocInfoVo()!=null && imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo()!=null){
					List<NodeImageVo> nodeImageVos0=imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo().getNodeImageVos();
					if(nodeImageVos0!=null && nodeImageVos0.size()>0){
						for(NodeImageVo imageVo0:nodeImageVos0){
							if("claim-picture".equals(imageVo0.getId())){
								List<NodeImageVo> nodeImageVos1=imageVo0.getNodeImageVos();
								if(nodeImageVos1!=null && nodeImageVos1.size()>0){
									for(NodeImageVo imageVo1:nodeImageVos1){
											List<NodeImageVo> nodeImageVos2=imageVo1.getNodeImageVos();
											if(nodeImageVos2!=null && nodeImageVos2.size()>0){
												for(NodeImageVo imageVo2:nodeImageVos2){
													if(StringUtils.isNotBlank(imageVo2.getName())){
														List<LeafVo> leafVos=imageVo2.getLeafVos();
														if(leafVos!=null && leafVos.size()>0){
															for(LeafVo lfVo:leafVos){
																List<PageVo> pagevos=imageUrlbaseVo.getPagesVo();
																if(pagevos!=null && pagevos.size()>0){
																	for(PageVo pageVo:pagevos){
																		//如果图片的id与数据库存的id相同，则此张图片为该次查看图片
																		if(pageVo.getPageId().equals(lfVo.getValues())){
																			if(prplFeeStandardVos!=null && prplFeeStandardVos.size()>0){
																				for(PrplFeeStandardVo feeStandardVo:prplFeeStandardVos){
																					if(StringUtils.isNotBlank(feeStandardVo.getImageId()) && feeStandardVo.getImageId().equals(imageVo2.getId())){
																						String imgUrl=urlChange(pageVo.getPageUrl());
																						feeStandardVo.setImageUrlView(imgUrl);
																					}
																				}
																			}
																			
																			
																		}
																	}
																}
															}
														}
														
														
													}
												}
											}
									}
								}
							}
						}
					}
				}
			 }
			}
		}
		
		mv.addObject("prplFeeStandardVos",prplFeeStandardVos);
		mv.setViewName("payFee/payFeeStandardDetailView");
		return mv;
	}
    
    /**
     * 根据id去删除对应的人伤费用标准表信息
     * @param id
     * @return
     */
    @RequestMapping(value="/deleteFeeStandard.do")
    @ResponseBody
    public AjaxResult deleteFeeStandard(String imageBussNo){
    	AjaxResult ajax=new AjaxResult();
    	try{
    		payFeeStandardService.deletePrplFeeStandardVoByImagebussNo(imageBussNo);
    	 ajax.setStatus(HttpStatus.SC_OK);
    	}catch(Exception e){
    		ajax.setStatus(500);
    		ajax.setStatusText("删除失败:"+e.getMessage());
    		logger.info("删除失败！",e);
    	}
    	return ajax;
    }
    
    
    /**
     * 增加图片标准
     * @return
     */
    @RequestMapping(value="/addFeeStandardEdit.do")
    public ModelAndView addFeeStandard(){
    	ModelAndView mv=new ModelAndView();
    	mv.setViewName("payFee/addPayFeeStandardEdit");
    	return mv;
    }
    
    /**
     * 判断文件大小
     * yzy
     * @param len
     *            文件长度
     * @param size
     *            限制大小
     * @param unit
     *            限制单位（B,K,M,G）
     * @return
     */
    private boolean checkFileSize(Long len, int size, String unit) {
        //long len = file.length();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize >= size) {
            return false;
        }
        return true;
    }
    
    /**
     * 请求报文部分截取替换
     * @param reqXml
     * @return
     */
    private String changeXml(String reqXml){
    	int str1=reqXml.indexOf("<PAGES>");
		int str2=reqXml.indexOf("</PAGES>");
		String endStr="";
		if(str2>1){
			String stra=reqXml.substring(str1+7, str2+8);
			String strb=stra.replace("<PAGE","<UAGE");
			String strc="<PAGES>".concat(strb);
			String strd=reqXml.substring(str1, str2+8);
			endStr=reqXml.replace(strd, strc);
		}
		
		
    	return endStr;
    }
    /**
     * 转换地址
     * @param url
     * @return
     */
    private String urlChange(String url){
    	StringBuffer urlBuffer=new StringBuffer();
    	if(StringUtils.isNotBlank(url)){
    		String [] urlArry=url.split("\\?");
    		if(urlArry!=null && urlArry.length>0){
    			String ceUrl =SpringProperties.getProperty("PD_URL");
    			urlBuffer.append(ceUrl).append("?").append(urlArry[1]);
    		}
    	}
    	return urlBuffer.toString();
    }
}
