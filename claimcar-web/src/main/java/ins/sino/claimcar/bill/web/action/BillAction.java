package ins.sino.claimcar.bill.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.platform.utils.ImportExcelUtil;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.vat.service.BilllclaimService;
import ins.sino.claimcar.vat.service.ExchangeVatService;
import ins.sino.claimcar.vat.vo.BillImageViewSonVo;
import ins.sino.claimcar.vat.vo.BillImageViewVo;
import ins.sino.claimcar.vat.vo.BindInvoiceReqDto;
import ins.sino.claimcar.vat.vo.ImagePageVo;
import ins.sino.claimcar.vat.vo.ImageResVo;
import ins.sino.claimcar.vat.vo.PrpJBatchDto;
import ins.sino.claimcar.vat.vo.PrpJecdInvoiceDto;
import ins.sino.claimcar.vat.vo.PrpLbillcontVo;
import ins.sino.claimcar.vat.vo.PrpLbillinfoVo;
import ins.sino.claimcar.vat.vo.PrplAcbillcontVo;
import ins.sino.claimcar.vat.vo.ResmessageVo;
import ins.sino.claimcar.vat.vo.UntyingInvoiceReqDtoVo;
import ins.sino.claimcar.vat.vo.UntyingReqDtoVo;
import ins.sino.claimcar.vat.vo.VatQueryViewVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
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

import com.alibaba.fastjson.JSONObject;
import com.sunyard.insurance.encode.client.EncodeAccessParam;

@Controller
@RequestMapping(value="/bill")
public class BillAction {
	
private static Logger logger = LoggerFactory.getLogger(BillAction.class);

private static final String FM_DATE_mm = "#yyyy-MM-dd HH:mm";

@Autowired
private CarXyImageService carXyImageService;
@Autowired
private BilllclaimService billlclaimService;
@Autowired
private BillNoService billNoService;
@Autowired
private ExchangeVatService exchangeVatService;
@Autowired
private CompensateTaskService compensateTaskService;
@Autowired
private CodeTranService codeTranService;
@Autowired
private PayCustomService payCustomService;
@Autowired
private ManagerService managerService;
 /**
  * 计算书管理页面
  * @return
  */
 @RequestMapping(value="/compenManagerQueryList.do")
 public ModelAndView compenManager(HttpServletRequest request){
	ModelAndView mvc=new ModelAndView();
	String regFlag=request.getParameter("regFlag");
	if(StringUtils.isBlank(regFlag)){
		regFlag="0";//1--表示在工作台的发票登记处跳转过来的，0--表示其它地方跳过来的请求
	}
	mvc.addObject("regFlag", regFlag);
	mvc.setViewName("vatView/vatCompMaQueryList");
	return mvc;
 }
 
 /**
  * 发票登记页面
  * @return
  */
 @RequestMapping(value="/billRegisterQueryList.do")
 public ModelAndView billRegister(){
	ModelAndView mvc=new ModelAndView();
	
	mvc.setViewName("vatView/vatBillRegisterQueryList");
	return mvc;
 }
 
 /**
  * 计算书(发票)关系页面
  * @return
  */
 @RequestMapping(value="/compenInfoList.do")
 public ModelAndView compenInfo(HttpServletRequest request){
	ModelAndView mvc=new ModelAndView();
	VatQueryViewVo vatQueryViewVo=new VatQueryViewVo();
	String registNo=request.getParameter("registNo");
	String compensateNo=request.getParameter("compensateNo");
	String bussType=request.getParameter("bussType");
	String feeCode=request.getParameter("feeCode");
	String payId=request.getParameter("payId");
	String sumAmt=request.getParameter("sumAmt");
	String registerNum=request.getParameter("registerNum");
	String bussId=request.getParameter("bussId");
	PrpLCompensateVo prpLCompensateVo=compensateTaskService.findCompByPK(compensateNo);
	vatQueryViewVo.setRegistNo(registNo);
	vatQueryViewVo.setCompensateNo(compensateNo);
	vatQueryViewVo.setBussType(bussType);
	vatQueryViewVo.setBussName(CodeConstants.backBussName(bussType));
	vatQueryViewVo.setFeeCode(feeCode);
	vatQueryViewVo.setFeeName(CodeConstants.backFeeName(feeCode));
	vatQueryViewVo.setPayId(payId);
	vatQueryViewVo.setSumAmt(new BigDecimal(sumAmt));
	if(StringUtils.isBlank(registerNum)){
		registerNum="0";
	}
	vatQueryViewVo.setRegisterNum(new BigDecimal(registerNum));
	vatQueryViewVo.setBussId(bussId);
	if(StringUtils.isNotBlank(payId)){
		PrpLPayCustomVo prpLPayCustomVo=payCustomService.findPayCustomVoById(Long.valueOf(payId));
		if(prpLPayCustomVo!=null){
			vatQueryViewVo.setPayName(prpLPayCustomVo.getPayeeName());
			vatQueryViewVo.setAccountNo(prpLPayCustomVo.getAccountNo());
		}
	}
	if(prpLCompensateVo!=null){
		vatQueryViewVo.setUnderwriteDate(prpLCompensateVo.getUnderwriteDate());
		String comName = codeTranService.transCode("ComCode",prpLCompensateVo.getComCode());
		vatQueryViewVo.setComName(comName);
	}
	mvc.addObject("vatQueryViewVo", vatQueryViewVo);
	mvc.setViewName("vatView/vatCompensateInfo");
	return mvc;
   }
 
 /**
  * 发票(计算书)关系页面
  * @return
  */
 @RequestMapping(value="/billInfoList.ajax")
 public ModelAndView billInfo(HttpServletRequest request){
	ModelAndView mvc=new ModelAndView();
	VatQueryViewVo vatQueryViewVo=new VatQueryViewVo();
	String billNo=request.getParameter("billNo");
	String billCode=request.getParameter("billCode");
	String rgisterFlag=request.getParameter("rgisterFlag");//标记请求是否在登记页面中过来
	PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoVoByParams(billNo, billCode);
	if(prpLbillinfoVo!=null){
		vatQueryViewVo.setBillId(prpLbillinfoVo.getId());
		vatQueryViewVo.setBillNo(prpLbillinfoVo.getBillNo());
		vatQueryViewVo.setBillCode(prpLbillinfoVo.getBillCode());
		vatQueryViewVo.setBillDate(prpLbillinfoVo.getBillDate());
		vatQueryViewVo.setSaleName(prpLbillinfoVo.getSaleName());
		vatQueryViewVo.setSaleNo(prpLbillinfoVo.getSaleNo());
		vatQueryViewVo.setBillNnum(prpLbillinfoVo.getBillNnum());
		vatQueryViewVo.setBillNum(prpLbillinfoVo.getBillNum());
		vatQueryViewVo.setBillSlName(percentChage(prpLbillinfoVo.getBillSl()));
		vatQueryViewVo.setBillSnum(prpLbillinfoVo.getBillSnum());
		vatQueryViewVo.setRegisterStatus(prpLbillinfoVo.getRegisterStatus());
	}
	mvc.addObject("vatQueryViewVo",vatQueryViewVo);
	mvc.addObject("rgisterFlag",rgisterFlag);
	mvc.setViewName("vatView/vatBillInfo");
	return mvc;
   }
 
 
 /**
  * 上传发票图片到影像系统(一般实赔与预付上传发票)
  * @param upFiles
  * @param request
  * @param response
  * @return
  */
@RequestMapping(value="/importExcel.ajax",method = RequestMethod.POST)
@ResponseBody
public AjaxResult importExcel(@RequestParam(value = "file",required=false) MultipartFile[] upFiles,//上传的文件
           HttpServletRequest request,HttpServletResponse response){
	    String bussNo=request.getParameter("compensateNo");//业务号(计算书号)
	    String bussType=request.getParameter("bussType");//业务类型
	    String feeCode=request.getParameter("feeCode");//费用类型                
	    String payId=request.getParameter("payId");//收款人id
	    String payName=request.getParameter("payName");//收款人
	    String bussId=request.getParameter("bussId");//业务表Id
		AjaxResult ajax=new AjaxResult();
		String dpath="";//图片删除路劲
		String dname="";//删除图片名称
		Map<String,File> fileMaps=new HashMap<String,File>();
		String path="";//图片路径
		List<String> fileNames=new ArrayList<String>();//图片名称集合
		SysUserVo userVo=WebUserUtils.getUser();

		try{
			// 如果没有文件上传，MultipartFile也不会为null，可以通过调用getSize()方法获取文件的大小来判断是否有上传文件
			if(upFiles!=null && upFiles.length > 0){
			    for(int i=0;i<upFiles.length;i++){
					// 得到项目在服务器的真实根路径，如：/home/tomcat/webapp/项目名/imageFees
					path = request.getSession().getServletContext().getRealPath("/imageFees/");
					// 得到文件的原始名称，如：美女.png
					logger.info("文件原始路径--------------------------------------------------》"+path);
					String fileName = upFiles[i].getOriginalFilename();
					SysUserVo sysUserVo=WebUserUtils.getUser();
					String fileNewName=fileName;
					Date datet=new Date();
					Long paySysdatet=datet.getTime();//取当前时间搓
					// 通过文件的原始名称，可以对上传文件类型做限制，如：只能上传jpg和png的图片文件
					if (StringUtils.isBlank(fileName)||(!fileName.endsWith("jpg") && !fileName.endsWith("png"))) {
					    throw new IllegalArgumentException("上传的图片格式只能为jpg或png!");
					}else{//重新命名图片名字,以方便每个年限的地区照片拥有唯一的的标识
					   if(fileName.endsWith("jpg")){
						  fileNewName=sysUserVo.getUserCode()+paySysdatet+i+".jpg";
					   }else{
						  fileNewName=sysUserVo.getUserCode()+paySysdatet+i+".png";
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
					
					Date date=new Date();
					Long paySysdate=date.getTime();//取当前时间搓
					String imageId=CodeConstants.BILLCODE+"_"+bussNo+paySysdate+"_"+i;
					    fileMaps.put(imageId,file);
					}
		}
		//业务类型
		Map<String,String> bussTypeMaps=new HashMap<String,String>();
	    if("0".equals(bussType)){
	    	bussTypeMaps.put("0", "赔款");
	    }else{
	    	bussTypeMaps.put("1", "费用");
	    }
		//费用与赔款类型编码
		Map<String,String> codeMaps=new HashMap<String,String>();
		codeMaps.put(feeCode,CodeConstants.backFeeName(feeCode));
		//收款人
		Map<String,String> payIdMaps=new HashMap<String,String>();
		payIdMaps.put(payId, payName);
		String returnXml=carXyImageService.upBillImageToXyd(bussNo, fileMaps,"pays", bussTypeMaps, codeMaps, payIdMaps, path);
		if(StringUtils.isNotBlank(returnXml)){
			ImageResVo imageResVo=ClaimBaseCoder.xmlToObj(returnXml,ImageResVo.class);
			if(imageResVo==null){
				throw new IllegalAccessException("上传失败，影像系统异常，请稍后再试!");
			}else{
				if(!"200".equals(imageResVo.getRESPONSE_CODE())){
					ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					ajax.setStatusText("错误信息提示："+imageResVo.getRESPONSE_MSG());
				}else{
					BillImageViewVo billImageViewVo=new BillImageViewVo();
					billImageViewVo.setSendTime(DateString(new Date()));
					billImageViewVo.setSystemType("102001");//系统编码
					List<BillImageViewSonVo> imageList=new ArrayList<BillImageViewSonVo>();
					if(imageResVo.getImagePageVos()!=null && imageResVo.getImagePageVos().size()>0){
						int i=0;
						for(ImagePageVo pageVo:imageResVo.getImagePageVos()){
							BillImageViewSonVo billImageViewSonVo=new BillImageViewSonVo();
							billImageViewSonVo.setBussNo(bussNo);//业务号
							billImageViewSonVo.setPaycustId(payId);//收款人id
							billImageViewSonVo.setBussType(bussType);//业务类型
							billImageViewSonVo.setChargeCode(CodeConstants.backvatFeeType(feeCode));//费用类型
							billImageViewSonVo.setIdEcdinvoiceocr(billNoService.getBillId(i+payId,"0"));//文件id
							billImageViewSonVo.setLinktableId(Long.valueOf(bussId));//业务表id
							if("0".equals(bussType)){
								if(bussNo.startsWith("7")){
									billImageViewSonVo.setLinktableName("1");//业务表
								}else{
									billImageViewSonVo.setLinktableName("2");
								}
							}else{
								if(bussNo.startsWith("7")){
									billImageViewSonVo.setLinktableName("3");
								}else{
									billImageViewSonVo.setLinktableName("2");
								}
							}
							billImageViewSonVo.setBillUrl(pageVo.getPageUrl());
							imageList.add(billImageViewSonVo);
							i++;
						}
							
					}
					billImageViewVo.setBillImageDtoList(imageList);
					//暂时注掉，避免产生垃圾数据
					int i=exchangeVatService.sendBillImageToVat(billImageViewVo, userVo);//影像图片信息推送Vat
					if(i!=1){
						ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
						ajax.setStatusText("错误信息提示:发票信息推送vat失败！");
					}
				}
			}
		}
		}catch(Exception e){
		  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		  ajax.setStatusText("错误信息提示："+e.getMessage());
		  logger.info("VAT发票图片上传报错信息：", e);
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
 *发票计算书管理查询页面 
 * <pre></pre>
 * @param queryVo
 * @param start
 * @param length
 * @return
 * @throws ParseException
 * @modified:
 * ☆yzy(2020年6月17日 上午11:20:37): <br>
 */
@RequestMapping("/billSupplymentSerach.do")
@ResponseBody
public String billSupplymentSerach(@FormModel("vatQueryViewVo") VatQueryViewVo queryVo,// 页面组装VO
						          @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			                      @RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
	//当前登录人
	SysUserVo sysUserVo=WebUserUtils.getUser();
	queryVo.setStart(start);
	queryVo.setLength(length);
	//权限控制
	queryVo=changeComcode(queryVo,sysUserVo);
	if(StringUtils.isBlank(queryVo.getComCode())){
		queryVo.setComCode(sysUserVo.getComCode());
	}
	ResultPage<VatQueryViewVo> resultPage;
	String jsonData = null;
	try{
		resultPage = billlclaimService.findBillPageForBillInfo(queryVo);
		jsonData = ResponseUtils.toDataTableJson(resultPage,"indexNo","registNo","compensateNo","bussType","bussName","feeCode","feeName","comCode"
				,"comName","underwriteDate"+FM_DATE_mm,"payName","accountNo","sumAmt","registerNum","registerNum","workFlag","workName","payId","bussId");
		logger.info("billSupplymentSerach.jsonData="+jsonData);
	}catch(Exception e){
		logger.error("发票计算书管理页面查询异常：",e);
	}
	return jsonData;

}

/**
 * 获取影像查看请求参数
 * @param bussNo
 * @return
 * @modified:
 * ☆yzy(2020年6月15日 下午10:50:26): <br>
 */
@RequestMapping(value="/urlReqQueryParam.do")
public ModelAndView urlReqQueryParam(String bussNo) {
	ModelAndView mv = new ModelAndView();
	//获取请求的xml参数
	try {
	SysUserVo user = WebUserUtils.getUser();
	String reqXml=carXyImageService.getReqQueryParam(user,CodeConstants.APPROLE,bussNo);
	String key=SpringProperties.getProperty("YX_key");
	String Qurl=SpringProperties.getProperty("YX_QUrl");
	//生成请求参数
	String param = EncodeAccessParam.getEncodeParam("format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqXml,20*1000,key);
	String url = Qurl+"?data="+param;
	mv.addObject("url",url);
	logger.info("信雅达接口调用参数:"+"format=xml&code="+CodeConstants.YXQUERYCODE+"&xml="+reqXml);
	} catch (Exception e) {
		logger.info("获取影像查看报错", e);
	}
	mv.setViewName("image/ImgView");
	return mv;
}

/**
 *	计算书(发票)关系页面 
 * <pre></pre>
 * @param queryVo
 * @param start
 * @param length
 * @return
 * @throws ParseException
 * @modified:
 * ☆yzy(2020年6月17日 上午11:20:37): <br>
 */
@RequestMapping("/vatCompenInfoSerach.do")
@ResponseBody
public String vatCompenInfoSerach(@FormModel("vatQueryViewVo") VatQueryViewVo queryVo,// 页面组装VO
						          @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			                      @RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
	//当前登录人
	queryVo.setStart(start);
	queryVo.setLength(length);
	ResultPage<VatQueryViewVo> resultPage;
	String jsonData = null;
	try{
		resultPage = billlclaimService.findVatPageForCompInfo(queryVo);
		jsonData = ResponseUtils.toDataTableJson(resultPage,"billNo","billCode","billDate"+FM_DATE_mm,"saleName","saleNo","billNnum","billSnum","billSl","billSlName"
				,"billNum","registerNum");
		logger.info("vatCompenInfoSerach.jsonData="+jsonData);
	}catch(Exception e){
		logger.error("计算书(发票)关系页面查询异常：",e);
	}
	return jsonData;

}

/**
 *	发票(计算书)关系页面 
 * <pre></pre>
 * @param queryVo
 * @param start
 * @param length
 * @return
 * @throws ParseException
 * @modified:
 * ☆yzy(2020年6月17日 上午11:20:37): <br>
 */
@RequestMapping("/vatBillfoSerach.do")
@ResponseBody
public String vatBillfoSerach(@FormModel("vatQueryViewVo") VatQueryViewVo queryVo,// 页面组装VO
						          @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			                      @RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
	//当前登录人
	queryVo.setStart(start);
	queryVo.setLength(length);
	ResultPage<VatQueryViewVo> resultPage;
	String jsonData = null;
	try{
		resultPage = billlclaimService.findVatPageForBillInfo(queryVo); 
		jsonData = ResponseUtils.toDataTableJson(resultPage,"indexNo","registNo","compensateNo","bussType","bussName","feeCode","feeName","underwriteDate"+FM_DATE_mm,"payName","accountNo","sumAmt","registerNum","registerNum1"
				,"comName","payId","bussId","billContId");
		logger.info("vatBillfoSerach.jsonData="+jsonData);
	}catch(Exception e){
		logger.error("发票(计算书)关系页面  异常：",e);
	}
	return jsonData;

}

/**
 *	发票登记(推送)查询
 * <pre></pre>
 * @param queryVo
 * @param start
 * @param length
 * @return
 * @throws ParseException
 * @modified:
 * ☆yzy(2020年6月18日 下午15:20:37): <br>
 */
@RequestMapping("/vatBillRegisterSerach.do")
@ResponseBody
public String vatBillRegisterSerach(@FormModel("vatQueryViewVo") VatQueryViewVo queryVo,// 页面组装VO
						          @RequestParam(value = "start", defaultValue = "0") Integer start,// 分页开始位置
			                      @RequestParam(value = "length", defaultValue = "10") Integer length) throws ParseException {
	//当前登录人
	queryVo.setStart(start);
	queryVo.setLength(length);
	//当前登录人
	SysUserVo sysUserVo=WebUserUtils.getUser();
	//权限控制
	queryVo=changeComcode(queryVo,sysUserVo);
	if(StringUtils.isBlank(queryVo.getComCode())){
		queryVo.setComCode(sysUserVo.getComCode());
	}
	ResultPage<VatQueryViewVo> resultPage;
	String jsonData = null;
	try{
		if("0".equals(queryVo.getRegisterStatus())){//登记
			resultPage = billlclaimService.findVatPageForBillRegister(queryVo);
		}else{//推送
			resultPage =billlclaimService.findVatPageForBillSend(queryVo);
		}
		
		jsonData = ResponseUtils.toDataTableJson(resultPage,"billNo","billCode","billDate"+FM_DATE_mm,"saleName","saleNo","billNnum","billSnum","billSl","billSlName"
				,"billNum","registerNum","underwriteDate"+FM_DATE_mm,"bussType","bussName","feeCode","feeName","payeeType","sumAmt","comCode","comName","payName",
				"accountNo","billContId","billId","policyNo","registNo","sendStatus","sendStatusName","registerStatus","backFlag","backCauseInfo","vidflag","vidflagName","backFlagName","compensateNo","payId","bussId");
		logger.info("vatBillRegisterSerach.jsonData="+jsonData);
	}catch(Exception e){
		logger.error("发票登记页面查询异常：",e);
	}
	return jsonData;

}

/**
 * 导出excel
 * @param response
 * @param params
 * @return
 * @throws Exception
 * @modified: ☆YZY(2020年6月21日 下午1:29:42): <br>
 */
@RequestMapping("/exportExcel.do")
@ResponseBody
public String exportExcel(HttpServletResponse response,String params) throws Exception {
	if(StringUtils.isNotBlank(params)  && params.endsWith(",")){
		params=params.substring(0, params.length()-1);
	}
    //存放计算书发票关系表与发票表Id
	String [] idsArry=null;
	if(StringUtils.isNotBlank(params)){
		idsArry=params.split(",");
	}
	List<VatQueryViewVo> results = billlclaimService.findVatRgisterInfo(idsArry);
	// 填充projects数据
	List<Map<String,Object>> list = billlclaimService.createExcelRecord(results);
	String fileDir = "ins/sino/claimcar/other/files/billAmountRegister.xlsx";
	String keys[] = {"indexNo","billNo","billCode","billDate","saleName","saleNo","billNnum","billSnum","billSl","billNum","registerNum","registNo","compensateNo","bussName","feeName","sumAmt","comName","underwriteDate","payName","accountNo","billContId","billId","bussId","busstableType","comCode"};// map中的key
	ByteArrayOutputStream os = new ByteArrayOutputStream();
	try{
		File file = ExportExcelUtils.getExcelDemoFile(fileDir);
		ExportExcelUtils.writeNewBillExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.YES).write(os);
	}catch(IOException e){
		e.printStackTrace();
	}
	byte[] content = os.toByteArray();
	InputStream is = new ByteArrayInputStream(content);
	// 设置response参数，可以打开下载页面
	response.reset();

	String fileName = "发票登记.xlsx";
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("Content-disposition","attachment;filename="+URLEncoder.encode(fileName,"utf-8"));

	ServletOutputStream out = response.getOutputStream();
	BufferedInputStream bis = null;
	BufferedOutputStream bos = null;
	try{
		bis = new BufferedInputStream(is);
		bos = new BufferedOutputStream(out);
		byte[] buff = new byte[2048];
		int bytesRead;
		// Simple read/write loop.
		while( -1!=( bytesRead = bis.read(buff,0,buff.length) )){
			bos.write(buff,0,bytesRead);
		}
	}catch(final IOException e){
		throw e;
	}
	finally{
		if(bis!=null) bis.close();
		if(bos!=null) bos.close();
	}

	return null;
}

/**
 * 导入Excel
 * @param file
 * @param request
 * @throws IOException
 * @modified: ☆YZY(2020年6月22日 上午8:47:30): <br>
 */
@RequestMapping(value = "/importbillExcel.ajax", method = RequestMethod.POST)
@ResponseBody
public void importExcel(@RequestParam MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws IOException {
	JSONObject json = new JSONObject();
	try{
		List<List<Object>> objects = ImportExcelUtil.getBankListByExcel(file.getInputStream(),file.getOriginalFilename());

		SysUserVo userVo = WebUserUtils.getUser();
		String succeedFlag= billlclaimService.applyBillTask(objects,userVo);

		json.put("data",succeedFlag);
		json.put("status",HttpStatus.SC_OK);
	}catch(Exception e){
		json.put("status",HttpStatus.SC_INTERNAL_SERVER_ERROR);
		json.put("msg",e.getMessage());
		logger.error("发票导入错误：",e);
	}
	response.setContentType("text/html;charset=UTF-8");
	response.getWriter().write(json.toJSONString());
}

/**
 * 逻辑删除发票上传记录
 * @param params
 * @return
 */
@RequestMapping(value = "/billdelete.ajax")
@ResponseBody
public AjaxResult billdelete(String params){
	if(StringUtils.isNotBlank(params)  && params.endsWith(",")){
		params=params.substring(0, params.length()-1);
	}
    AjaxResult ajax=new AjaxResult();
    //存放计算书发票关系表与发票表Id
    try{
    	String [] idsArry=null;
      	if(StringUtils.isNotBlank(params)){
      		idsArry=params.split(",");
      		if(idsArry!=null && idsArry.length>0){
      			for(int i=0;i<idsArry.length;i++){
      				String[] arryconId=idsArry[i].split("_");
      				PrpLbillcontVo prpLbillcontVo=billlclaimService.findPrpLbillcontById(Long.valueOf(arryconId[0]));
      	      		prpLbillcontVo.setStatus("0");//置为无效
      	      		billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
      			}
      		}
      		
      	}
      	ajax.setStatus(HttpStatus.SC_OK);
    }catch(Exception e){
    	ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    	ajax.setStatusText("错误信息："+e.getMessage());
    	logger.error("删除发票记录出错：",e);
    }
  	
   return ajax;
}

/**
 *绑定推送vat
 * @param params
 * @return
 */
@RequestMapping(value = "/sendBillregiterToVat.ajax")
@ResponseBody
public AjaxResult sendBillregiterToVat(String params){
	if(StringUtils.isNotBlank(params)  && params.endsWith(",")){
		params=params.substring(0, params.length()-1);
	}
	AjaxResult ajax=new AjaxResult();
	SysUserVo sysUserVo=WebUserUtils.getUser();
	BindInvoiceReqDto bindInvoiceReqDto=new BindInvoiceReqDto();
	try{
		bindInvoiceReqDto.setSendTime(DateString(new Date()));
		bindInvoiceReqDto.setSystemType("102001");//车理赔编码
		Set<String> billIdSet=new HashSet<String>();
		List<PrpJBatchDto> prpJBatchDtoList=new ArrayList<PrpJBatchDto>();
		String [] idsArry=null;
	  	if(StringUtils.isNotBlank(params)){
	  		idsArry=params.split(",");
	  		if(idsArry!=null && idsArry.length>0){
	  			for(int i=0;i<idsArry.length;i++){
	  				if(billIdSet.add(idsArry[i])){
	  					PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(idsArry[i]));
	  					if(!"1".equals(prpLbillinfoVo.getRegisterStatus())){
	  						throw new RuntimeException("发票号："+prpLbillinfoVo.getBillNo()+"已被人解绑，请核实数据后，再推送!");
	  					}else{
	  						PrpJBatchDto prpJBatchDto=new PrpJBatchDto();
	  						prpJBatchDto.setInvoiceId(prpLbillinfoVo.getVatbillId());//vat发票id
	  						List<PrpLbillcontVo> prpLbillcontVoList=billlclaimService.findPrpLbillcontByBillNoAndBillCode(prpLbillinfoVo.getBillNo(),prpLbillinfoVo.getBillCode());
	  						if((prpLbillcontVoList==null) || (prpLbillcontVoList!=null && prpLbillcontVoList.size()==0)){
	  							throw new RuntimeException("发票号："+prpLbillinfoVo.getBillNo()+"无绑定相关计算书费用的有效记录，请核实数据后，再推送!");
	  						}else{
	  							List<PrpJecdInvoiceDto> prpJecdInvoiceDtoList=new ArrayList<PrpJecdInvoiceDto>();
	  							for(PrpLbillcontVo prpLbillcontVo:prpLbillcontVoList){
	  								PrpJecdInvoiceDto prpJecdInvoiceDto=new PrpJecdInvoiceDto();
	  								prpJecdInvoiceDto.setCertiNo(prpLbillcontVo.getCompensateNo());
	  								prpJecdInvoiceDto.setChargeCode(prpLbillcontVo.getFeeType());//费用类型
	  								prpJecdInvoiceDto.setDeductionAmount(prpLbillcontVo.getRegisterNum().toString());
	  								prpJecdInvoiceDto.setInvoiceId(prpLbillinfoVo.getVatbillId());
	  								prpJecdInvoiceDto.setInvoiceType("1");//专票
	  								prpJecdInvoiceDto.setLossType(prpLbillcontVo.getBussType());//业务类型
	  								prpJecdInvoiceDto.setPayeeId(prpLbillcontVo.getPayId().toString());
	  								if("1".equals(prpLbillcontVo.getLinktableName())){
	  									PrpLPaymentVo prpLPaymentVo=billlclaimService.findPrpLPaymentById(prpLbillcontVo.getLinktableId());
	  									prpJecdInvoiceDto.setPlanFee(prpLPaymentVo.getSumRealPay().toString());//支付金额
	  								}else if("2".equals(prpLbillcontVo.getLinktableName())){
	  									PrpLPrePayVo prpLPrePayVo=billlclaimService.findPrpLPrePayById(prpLbillcontVo.getLinktableId());
	  									prpJecdInvoiceDto.setPlanFee(prpLPrePayVo.getPayAmt().toString());
	  								}else{
	  									PrpLChargeVo prpLChargeVo=billlclaimService.findPrpLChargeById(prpLbillcontVo.getLinktableId());
	  									prpJecdInvoiceDto.setPlanFee(prpLChargeVo.getFeeRealAmt().toString());
	  								}
	  								prpJecdInvoiceDto.setRegistNo(prpLbillcontVo.getRegistNo());
	  								prpJecdInvoiceDto.setPolicyNo(prpLbillcontVo.getPolicyNo());
	  								prpJecdInvoiceDtoList.add(prpJecdInvoiceDto);
	  							}
	  							prpJBatchDto.setPrpJecdInvoiceDtoList(prpJecdInvoiceDtoList);
	  							prpJBatchDtoList.add(prpJBatchDto);
	  						}
	  					}
	  				}
	  			}
	  		}
	  		
	  	}
	  
	  bindInvoiceReqDto.setPrpJBatchDtoList(prpJBatchDtoList);
	  //推送vat
	  Map<String,String> map=exchangeVatService.sendBillRegisterToVat(bindInvoiceReqDto, sysUserVo);
	  if("1".equals(map.get("flag"))){
		  ajax.setStatus(HttpStatus.SC_OK);
	  }else{
		  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	      ajax.setStatusText("推送发票绑定接口失败-->:"+map.get("returnMessage"));
	  }
	}catch(Exception e){
		 ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	     ajax.setStatusText("错误信息："+e.getMessage());
	     logger.error("推送发票绑定接口出错：",e);
	}
	
	
	return ajax;
}

/**
 *发票申请撤回
 * @param params
 * @return
 */
@RequestMapping(value = "/reqBackBillToVat.ajax")
@ResponseBody
public AjaxResult senBillregiterToVat(String params){
	if(StringUtils.isNotBlank(params)  && params.endsWith(",")){
		params=params.substring(0, params.length()-1);
	}
	AjaxResult ajax=new AjaxResult();
	SysUserVo sysUserVo=WebUserUtils.getUser();
	Set<String> billIdSet=new HashSet<String>();
	UntyingReqDtoVo untyingReqDtoVo=new UntyingReqDtoVo();
	untyingReqDtoVo.setSendTime(DateString(new Date()));
	untyingReqDtoVo.setSystemType("102001");
	ResmessageVo resmessageVo=new ResmessageVo();
	try{
		List<UntyingInvoiceReqDtoVo> untyingInvoiceReqDtoVos=new ArrayList<UntyingInvoiceReqDtoVo>();
		String [] idsArry=null;
	  	if(StringUtils.isNotBlank(params)){
	  		idsArry=params.split(",");
	  		if(idsArry!=null && idsArry.length>0){
	  			for(int i=0;i<idsArry.length;i++){
	  				if(billIdSet.add(idsArry[i])){
	  					PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(idsArry[i]));
	  					if(!"1".equals(prpLbillinfoVo.getSendstatus())){
	  						throw new RuntimeException("发票号："+prpLbillinfoVo.getBillNo()+"已被人撤回，请核实数据后，再发撤回申请!");
	  					}else{
	  						UntyingInvoiceReqDtoVo untyingInvoiceReqDtoVo=new UntyingInvoiceReqDtoVo();
	  						untyingInvoiceReqDtoVo.setInvoiceId(prpLbillinfoVo.getVatbillId());//vat发票id
	  						untyingInvoiceReqDtoVos.add(untyingInvoiceReqDtoVo);
	  					}
	  				}
	  			}
	  		}
	  	}
	  	untyingReqDtoVo.setUntyingInvoiceDtoList(untyingInvoiceReqDtoVos);
	    resmessageVo=exchangeVatService.sendUntyingInfoToVat(untyingReqDtoVo, sysUserVo);
	  if(resmessageVo!=null && "0000".equals(resmessageVo.getCode())){
		  ajax.setStatus(HttpStatus.SC_OK);
		  if(StringUtils.isNotBlank(resmessageVo.getOpenBillNos())){
		     ajax.setStatusText("撤回成功的发票号码:"+resmessageVo.getOpenBillNos()+";");
		  }
		  if(StringUtils.isNotBlank(resmessageVo.getCloseBillNos())){
			  ajax.setStatusText("撤回失败的发票号码:"+resmessageVo.getCloseBillNos()+";");
		  }
	  }else{
		  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	      ajax.setStatusText("撤回申请接口请求失败:"+resmessageVo.getErrormessage());
	  }
	}catch(Exception e){
		 ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	     ajax.setStatusText("错误信息："+e.getMessage());
	     logger.error("撤回申请接口请求出错：",e);
	}
	
	
	return ajax;
}

/**
 *发票解绑
 * @param params
 * @return
 */
@RequestMapping(value = "/openBillToVat.ajax")
@ResponseBody
public AjaxResult openBillToVat(String params){
	if(StringUtils.isNotBlank(params) && params.endsWith(",")){
		params=params.substring(0, params.length()-1);
	}
	AjaxResult ajax=new AjaxResult();
	String [] idsArray=params.split(",");
	try{
		if(idsArray!=null && idsArray.length>0){
			for(int i=0;i<idsArray.length;i++){
				PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(idsArray[i]));
				if(!"1".equals(prpLbillinfoVo.getRegisterStatus())){
					throw new RuntimeException("发票号："+prpLbillinfoVo.getBillNo()+"已被人解绑，请核实数据后，再解绑!");
				}
				
			}
		}
		if(idsArray!=null && idsArray.length>0){
			for(int i=0;i<idsArray.length;i++){
				PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(idsArray[i]));
				List<PrpLbillcontVo> prpLbillcontVoList=billlclaimService.findPrpLbillcontByBillNoAndBillCode(prpLbillinfoVo.getBillNo(), prpLbillinfoVo.getBillCode());
				if(prpLbillcontVoList!=null && prpLbillcontVoList.size()>0){
					for(PrpLbillcontVo prpLbillcontVo:prpLbillcontVoList){
						if("1".equals(prpLbillcontVo.getLinktableName())){
							billlclaimService.updatePrpLPayment(prpLbillcontVo.getLinktableId(), prpLbillcontVo.getRegisterNum(),"0");
							
						}else if("2".equals(prpLbillcontVo.getLinktableName())){
							billlclaimService.updatePrpLPrePay(prpLbillcontVo.getLinktableId(), prpLbillcontVo.getRegisterNum(),"0");
						}else if("3".equals(prpLbillcontVo.getLinktableName())){
							billlclaimService.updatePrpLCharge(prpLbillcontVo.getLinktableId(), prpLbillcontVo.getRegisterNum(),"0");
						}
						prpLbillcontVo.setRegisterNum(new BigDecimal("0"));
						billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
					}
				}
				prpLbillinfoVo.setRegisterStatus("0");
				billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
			}
		}
		
		ajax.setStatus(HttpStatus.SC_OK);
		
	}catch(Exception e){
		 ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	     ajax.setStatusText("解绑失败错误信息："+e.getMessage());
	     logger.error("解绑失败：",e);
	}
	
	
	return ajax;
}


/**
 * 时间转换方法
 * String 类型转换 Date 类型
 * @param strDate
 * @return
 * @throws ParseException2020-05-27 14:45:02
 */
private static String DateString(Date strDate){
	String date=null;
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  if(strDate!=null){
		  try {
			date=format.format(strDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return date;
}

/**
 * 转换为百分比
 * @param snum
 * @return
 */
private String percentChage(BigDecimal snum) {
	String strPercent="";
	if(snum==null){
		return strPercent;
	}else{
		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMaximumFractionDigits(2);
		strPercent=percent.format(snum.doubleValue());
	}
	
	return strPercent;
}

/**
 * 权限控制
 * @return
 */
private VatQueryViewVo changeComcode(VatQueryViewVo queryVo,SysUserVo sysUserVo){
	//页面所选机构权限只能小于或等于登录人机构权限，才能查出数据
	if(StringUtils.isNotBlank(queryVo.getComCode())){
		if("0002".equals(queryVo.getComCode().substring(0,4)) && "00".equals(queryVo.getComCode().substring(4,6))){
			if("0002".equals(sysUserVo.getComCode().substring(0,4)) && sysUserVo.getComCode().substring(4,6).equals(queryVo.getComCode().substring(4,6))){
				queryVo.setComCode(sysUserVo.getComCode());//机构
			}else{
				queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
			}
		}else if("0002".equals(queryVo.getComCode().substring(0,4)) && !"00".equals(queryVo.getComCode().substring(4,6))){
			if("0002".equalsIgnoreCase(sysUserVo.getComCode().substring(0,4)) && "00".equals(sysUserVo.getComCode().substring(4,6))){
				queryVo.setComCode(queryVo.getComCode());//机构
			}else if("0002".equalsIgnoreCase(sysUserVo.getComCode().substring(0,4)) && !"00".equals(sysUserVo.getComCode().substring(4,6))){
				if(sysUserVo.getComCode().substring(0, 6).equals(queryVo.getComCode().substring(0,6))){
					queryVo.setComCode(sysUserVo.getComCode());//机构
				}else{
					queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
				}
			}else{
				queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
			}
			
		}else if(!"0002".equals(queryVo.getComCode().substring(0,4)) && "00".equals(queryVo.getComCode().substring(2,4)) ){
			if(!"0002".equals(sysUserVo.getComCode().substring(0, 4)) && "00".equals(sysUserVo.getComCode().substring(2,4)) ){
				queryVo.setComCode(sysUserVo.getComCode());//机构
			}else{
				queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
			}
		}else if(!"0002".equals(queryVo.getComCode().substring(0,4)) && !"00".equals(queryVo.getComCode().substring(2,4)) ){
			if(!"0002".equals(sysUserVo.getComCode().substring(0,4)) && "00".equals(sysUserVo.getComCode().substring(2,4))){
				queryVo.setComCode(queryVo.getComCode());//机构
			}else if(!"0002".equals(sysUserVo.getComCode().substring(0,4)) && !"00".equals(sysUserVo.getComCode().substring(2,4))){
				if(sysUserVo.getComCode().substring(0, 4).equals(queryVo.getComCode().substring(0,4))){
					queryVo.setComCode(sysUserVo.getComCode());//机构
				}else{
					queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
				}
			}else{
				queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
			}
		}else{
			queryVo.setComCode("99999999");//超出登录人权限，故不能查出记录
		}
		}
	
	return queryVo;
}

/**
 * 单张发票登记
 * @param billId
 * @param vatQueryViewVoList
 * @return
 */
@RequestMapping("/saveoneBillEdit.do")
@ResponseBody
public AjaxResult saveCompensateEdit(
		@FormModel("billId") Long billId,
		@FormModel("vatQueryViewVo") List<VatQueryViewVo> vatQueryViewVoList){
	
	AjaxResult ajaxResult = new AjaxResult();
	//业务表id
	Set<String> bussidSet=new HashSet<String>();
	//存放计算书与业务表id的关系
	Map<String,String> busscompMap=new HashMap<String,String>();
	Map<String,BigDecimal> bussMap=new HashMap<String,BigDecimal>();
	try {
		PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(billId);
		if(prpLbillinfoVo!=null && "1".equals(prpLbillinfoVo.getRegisterStatus())){
			throw new RuntimeException("由于长时间未操作，该发票已被其他人绑定!");
		}
		if(vatQueryViewVoList!=null && vatQueryViewVoList.size()>0){
			for(VatQueryViewVo vatQueryViewVo:vatQueryViewVoList){
				PrpLbillcontVo prpLbillcontVo= billlclaimService.findPrpLbillcontById(vatQueryViewVo.getBillContId());
				if(prpLbillcontVo!=null && !"1".equals(prpLbillcontVo.getStatus())){
					throw new RuntimeException("由于长时间未操作，该计算书:"+prpLbillcontVo.getCompensateNo()+"已被人解除与该张发票的关系！(关系表id="+prpLbillcontVo.getId()+"),请核实后,重新操作！");
				}
				//存储每个费用记录的登记金额
				if(bussidSet.add(prpLbillcontVo.getLinktableName()+"_"+prpLbillcontVo.getLinktableId())){
					bussMap.put(prpLbillcontVo.getLinktableName()+"_"+prpLbillcontVo.getLinktableId(),DataUtils.NullToZero(vatQueryViewVo.getRegisterNum()));
				}else{
					BigDecimal amount=bussMap.get(prpLbillcontVo.getLinktableName()+"_"+prpLbillcontVo.getLinktableId());
					bussMap.put(prpLbillcontVo.getLinktableName()+"_"+prpLbillcontVo.getLinktableId(), amount.add(DataUtils.NullToZero(vatQueryViewVo.getRegisterNum())));
				}
				busscompMap.put(prpLbillcontVo.getLinktableName()+"_"+prpLbillcontVo.getLinktableId(),prpLbillcontVo.getCompensateNo());
			}
		}
		if(vatQueryViewVoList!=null && vatQueryViewVoList.size()>0){
			for(VatQueryViewVo vatQueryViewVo:vatQueryViewVoList){
				PrpLbillcontVo prpLbillcontVo= billlclaimService.findPrpLbillcontById(vatQueryViewVo.getBillContId());
				if(prpLbillcontVo!=null){
					prpLbillcontVo.setRegisterNum(vatQueryViewVo.getRegisterNum());
					billlclaimService.saveOrUpdatePrplbillcont(prpLbillcontVo);
				}
			}
		}
		
		//验证业务费用的总登记金额，不能大于赔付金额
		if(bussidSet!=null && bussidSet.size()>0){
			for(String buss:bussidSet){
				 String[] arry=buss.split("_");
				 if("1".equals(arry[0])){
					 PrpLPaymentVo prpLPaymentVo= billlclaimService.findPrpLPaymentById(Long.valueOf(arry[1]));
					 if(DataUtils.NullToZero(prpLPaymentVo.getSumRealPay()).compareTo(DataUtils.NullToZero(prpLPaymentVo.getRegsumAmount()).add(bussMap.get(buss)))<0){
						 throw new RuntimeException("计算书:"+busscompMap.get(buss)+"费用名称为实赔(修理厂)的记录登记总金额不能大于赔付金额");
					 }	 
				}else if("2".equals(arry[0])){
						PrpLPrePayVo prpLPrePayVo=billlclaimService.findPrpLPrePayById(Long.valueOf(arry[1]));
						if(DataUtils.NullToZero(prpLPrePayVo.getPayAmt()).compareTo(DataUtils.NullToZero(prpLPrePayVo.getRegsumAmount()).add(bussMap.get(buss)))<0){
							if("P".equals(prpLPrePayVo.getFeeType())){
								throw new RuntimeException("计算书:"+busscompMap.get(buss)+"费用名称为预付赔款(修理厂)的记录登记总金额不能大于赔付金额");
							}else{
								throw new RuntimeException("计算书:"+busscompMap.get(buss)+"费用名称为"+CodeConstants.backFeeName(prpLPrePayVo.getChargeCode())+"的记录登记总金额不能大于赔付金额");
								
							}
							
						}
				}else if("3".equals(arry[0])){
						 PrpLChargeVo prpLChargeVo=billlclaimService.findPrpLChargeById(Long.valueOf(arry[1]));
						 if(DataUtils.NullToZero(prpLChargeVo.getFeeRealAmt()).compareTo(DataUtils.NullToZero(prpLChargeVo.getRegsumAmount()).add(DataUtils.NullToZero(bussMap.get(buss))))<0){
							 throw new RuntimeException("计算书:"+busscompMap.get(buss)+"费用名称为"+CodeConstants.backFeeName(prpLChargeVo.getChargeCode())+"的记录登记总金额不能大于赔付金额");
						}
							 
						 
					 }
				 }
			    } 
		if(bussidSet!=null && bussidSet.size()>0){
			for(String buss:bussidSet){
			  String[] arry=buss.split("_");
			  if("1".equals(arry[0])){//实赔赔款表
				  billlclaimService.updatePrpLPayment(Long.valueOf(arry[1]), bussMap.get(buss),"1");
			  }else if("2".equals(arry[0])){//预付表
				  billlclaimService.updatePrpLPrePay(Long.valueOf(arry[1]), bussMap.get(buss),"1");
			  }else if("3".equals(arry[0])){//实赔费用表
				  billlclaimService.updatePrpLCharge(Long.valueOf(arry[1]), bussMap.get(buss),"1");
			  }
			}
		}
		prpLbillinfoVo.setRegisterStatus("1");
		billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
		ajaxResult.setStatus(HttpStatus.SC_OK);
	} catch (Exception e) {
		logger.info("单张发票登记出错",e);
		ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		ajaxResult.setStatusText(e.getMessage());
	}

	return ajaxResult;
}

/**
 * 上传发票图片到影像系统(查勘费与公估费上传发票)
 * @param upFiles
 * @param request
 * @param response
 * @return
 */
@RequestMapping(value="/asscheckimportExcel.ajax",method = RequestMethod.POST)
@ResponseBody
public AjaxResult asscheckimportExcel(@RequestParam(value = "file",required=false) MultipartFile[] upFiles,//上传的文件
          HttpServletRequest request,HttpServletResponse response){
	    String bussNo=request.getParameter("taskNo");//任务号
	    String code=request.getParameter("code");//查勘或公估机构代码
	    String codeName=request.getParameter("codeName");//查勘或公估机构名称
	    String taskType=request.getParameter("taskType");//查勘或公估机构类型(A-公估，C-查勘)
		AjaxResult ajax=new AjaxResult();
		String dpath="";//图片删除路劲
		String dname="";//删除图片名称
		Map<String,File> fileMaps=new HashMap<String,File>();
		String path="";//图片路径
		List<String> fileNames=new ArrayList<String>();//图片名称集合
		SysUserVo userVo=WebUserUtils.getUser();

		try{
			// 如果没有文件上传，MultipartFile也不会为null，可以通过调用getSize()方法获取文件的大小来判断是否有上传文件
			if(upFiles!=null && upFiles.length > 0){
			    for(int i=0;i<upFiles.length;i++){
					// 得到项目在服务器的真实根路径，如：/home/tomcat/webapp/项目名/imageFees
					path = request.getSession().getServletContext().getRealPath("/imageFees/");
					// 得到文件的原始名称，如：发票.png
					logger.info("文件原始路径--------------------------------------------------》"+path);
					String fileName = upFiles[i].getOriginalFilename();
					SysUserVo sysUserVo=WebUserUtils.getUser();
					String fileNewName=fileName;
					Date datet=new Date();
					Long paySysdatet=datet.getTime();//取当前时间搓
					// 通过文件的原始名称，可以对上传文件类型做限制，如：只能上传jpg和png的图片文件
					if (StringUtils.isBlank(fileName)||(!fileName.endsWith("jpg") && !fileName.endsWith("png"))) {
					    throw new IllegalArgumentException("上传的图片格式只能为jpg或png!");
					}else{//重新命名图片名字,以方便每个年限的地区照片拥有唯一的的标识
					   if(fileName.endsWith("jpg")){
						  fileNewName=sysUserVo.getUserCode()+paySysdatet+i+".jpg";
					   }else{
						  fileNewName=sysUserVo.getUserCode()+paySysdatet+i+".png";
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
					
					Date date=new Date();
					Long paySysdate=date.getTime();//取当前时间搓
					String imageId=CodeConstants.BILLCODE+"_"+bussNo+paySysdate+"_"+i;
					    fileMaps.put(imageId,file);
					}
		}
		//业务类型
		Map<String,String> bussTypeMaps=new HashMap<String,String>();
	    	bussTypeMaps.put(code, codeName);
		String returnXml=carXyImageService.upasscheckBillImageToXyd(bussNo, fileMaps,"pays",bussTypeMaps,path);
		if(StringUtils.isNotBlank(returnXml)){
			ImageResVo imageResVo=ClaimBaseCoder.xmlToObj(returnXml,ImageResVo.class);
			if(imageResVo==null){
				throw new IllegalAccessException("上传失败，影像系统异常，请稍后再试!");
			}else{
				if(!"200".equals(imageResVo.getRESPONSE_CODE())){
					ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
					ajax.setStatusText("错误信息提示："+imageResVo.getRESPONSE_MSG());
				}else{
					BillImageViewVo billImageViewVo=new BillImageViewVo();
					billImageViewVo.setSendTime(DateString(new Date()));
					billImageViewVo.setSystemType("102001");//系统编码
					List<BillImageViewSonVo> imageList=new ArrayList<BillImageViewSonVo>();
					if(imageResVo.getImagePageVos()!=null && imageResVo.getImagePageVos().size()>0){
						int i=0;
						for(ImagePageVo pageVo:imageResVo.getImagePageVos()){
							BillImageViewSonVo billImageViewSonVo=new BillImageViewSonVo();
							billImageViewSonVo.setBussNo(bussNo);//业务号
							billImageViewSonVo.setFlag("1");//查勘费或公估费
								billImageViewSonVo.setBussType("1");//业务类型
								if("A".equals(taskType)){
									billImageViewSonVo.setChargeCode("13");//公估费
									billImageViewSonVo.setIdEcdinvoiceocr(billNoService.getBillId(i+"","1"));//文件id
								}else{
									billImageViewSonVo.setChargeCode("04");//查勘费
									billImageViewSonVo.setIdEcdinvoiceocr(billNoService.getBillId(i+"","2"));//文件id
								}
							
							
							billImageViewSonVo.setBillUrl(pageVo.getPageUrl());
							imageList.add(billImageViewSonVo);
							i++;
						}
							
					}
					billImageViewVo.setBillImageDtoList(imageList);
					//暂时注掉，避免产生垃圾数据
					int i=exchangeVatService.sendBillImageToVat(billImageViewVo, userVo);//影像图片信息推送Vat
					if(i!=1){
						ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
						ajax.setStatusText("错误信息提示:发票信息推送vat失败！");
					}
				}
			}
		}
		}catch(Exception e){
		  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		  ajax.setStatusText("错误信息提示："+e.getMessage());
		  logger.info("VAT发票图片上传报错信息：", e);
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
 * 逻辑删除公估或查勘发票上传记录
 * @param params
 * @return
 */
@RequestMapping(value = "/assChebilldelete.ajax")
@ResponseBody
public AjaxResult assChebilldelete(String chooseValue,String chooseId){
	
    AjaxResult ajax=new AjaxResult();
    //存放计算书发票关系表与发票表Id
    try{
    	String [] idsArry=null;
    	String [] valueArry=null;
      	if(StringUtils.isNotBlank(chooseValue)){
      		idsArry=chooseValue.split("_");
      		if(idsArry!=null && idsArry.length>0){
      			if("0".equals(idsArry[0])){//验真实标
      				PrplAcbillcontVo prplAcbillcontVo=billlclaimService.findPrplAcbillcontById(Long.valueOf(idsArry[1]));
      				prplAcbillcontVo.setStatus("0");//置为无效
      				billlclaimService.saveOrUpdatePrplAcbillcont(prplAcbillcontVo);
      			}else if("1".equals(idsArry[0])){//验真成功
      				valueArry=chooseId.split("_");
      				if(StringUtils.isBlank(valueArry[1]) || StringUtils.isBlank(valueArry[2])){
      					throw new IllegalArgumentException("页面异常：发票号码或发票代码为空！");
      				}
      				PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(idsArry[1]));
      				if(prpLbillinfoVo!=null && "1".equals(prpLbillinfoVo.getRegisterStatus())){
      					throw new IllegalArgumentException("由于长时间页面未操作,该记录已被其他人登记,请重新刷新页面后，再操作！");
      				}
      				List<PrplAcbillcontVo> prplAcbillcontVos=billlclaimService.findPrplAcbillcontByBillNoAndBillCode(valueArry[1], valueArry[2], valueArry[3]);
      				if(prplAcbillcontVos!=null && prplAcbillcontVos.size()>0){
      					for(PrplAcbillcontVo vo:prplAcbillcontVos){
      						vo.setStatus("0");
      						billlclaimService.saveOrUpdatePrplAcbillcont(vo);
      					}
      				}
      			}
      		}
      		
      	}
      	ajax.setStatus(HttpStatus.SC_OK);
    }catch(Exception e){
    	ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    	ajax.setStatusText("错误信息："+e.getMessage());
    	logger.error("删除发票记录出错：",e);
    }
  	
   return ajax;
}

/**
 *发票(公估/查勘)解绑
 * @param params
 * @return
 */
@RequestMapping(value = "/assCheopenBillToVat.ajax")
@ResponseBody
public AjaxResult assCheopenBillToVat(String billId,String taskNo){
	AjaxResult ajax=new AjaxResult();
	try{
        PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));
		if(!"1".equals(prpLbillinfoVo.getRegisterStatus())){
			throw new RuntimeException("发票号："+prpLbillinfoVo.getBillNo()+"已被人解绑，请核实数据后，再解绑!");
		}
		SysUserVo sysUserVo=WebUserUtils.getUser();
		UntyingReqDtoVo untyingReqDtoVo=new UntyingReqDtoVo();
		untyingReqDtoVo.setSendTime(DateString(new Date()));
		untyingReqDtoVo.setSystemType("102001");
		ResmessageVo resmessageVo=new ResmessageVo();
		try{
			List<UntyingInvoiceReqDtoVo> untyingInvoiceReqDtoVos=new ArrayList<UntyingInvoiceReqDtoVo>();
			UntyingInvoiceReqDtoVo untyingInvoiceReqDtoVo=new UntyingInvoiceReqDtoVo();
			untyingInvoiceReqDtoVo.setInvoiceId(prpLbillinfoVo.getVatbillId());//vat发票id
			untyingInvoiceReqDtoVos.add(untyingInvoiceReqDtoVo);
		  	untyingReqDtoVo.setUntyingInvoiceDtoList(untyingInvoiceReqDtoVos);
		    resmessageVo=exchangeVatService.sendUntyingInfoToVat(untyingReqDtoVo, sysUserVo);
		  if(resmessageVo!=null && "0000".equals(resmessageVo.getCode())){
			  ajax.setStatus(HttpStatus.SC_OK);
			  if(StringUtils.isNotBlank(resmessageVo.getOpenBillNos())){
				  if(!prpLbillinfoVo.getBillNo().equals(resmessageVo.getOpenBillNos())){
					  throw new IllegalArgumentException("页面请求的解绑发票号码与vat返回的发票号码不一致，请核实！");
				  }
				  prpLbillinfoVo.setSendstatus("0");//解绑成功，将推送状态与登记状态置为未推送与未登记
				  prpLbillinfoVo.setRegisterStatus("0");
				  billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
				  if(taskNo.startsWith("F")){
					  billlclaimService.updatePrpLAssessorFee(prpLbillinfoVo.getBillNo(),prpLbillinfoVo.getBillCode());
				  }else if(taskNo.startsWith("C")){
					  billlclaimService.updatePrpLCheckFee(prpLbillinfoVo.getBillNo(),prpLbillinfoVo.getBillCode());
				  }
			     ajax.setStatusText("发票号码:"+resmessageVo.getOpenBillNos()+"解绑成功！");
			  }
			  if(StringUtils.isNotBlank(resmessageVo.getCloseBillNos())){
				  ajax.setStatusText("发票号码:"+resmessageVo.getCloseBillNos()+"不允许解绑！");
			  }
		  }else{
			  ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		      ajax.setStatusText("解绑申请接口请求失败:"+resmessageVo.getErrormessage());
		  }
		}catch(Exception e){
			 ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		     ajax.setStatusText("错误信息："+e.getMessage());
		     logger.error("解绑申请接口请求出错：",e);
		}
	}catch(Exception e){
		 ajax.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	     ajax.setStatusText("错误信息："+e.getMessage());
	     logger.error("解绑申请接口请求出错：",e);
	}
		
		return ajax;
}

	
	/**
	 * 公估费发票推送
	 * @param billId
	 * @param vatQueryViewVoList
	 * @return
	 */
	@RequestMapping("/saveassBillEdit.do")
	@ResponseBody
	public AjaxResult saveassBillEdit(
			@FormModel("billId") String billId,
			@FormModel("interCodeId") String interCodeId,
			@FormModel("taskNo") String taskNo,
			@FormModel("assessorFeeVo") List<PrpLAssessorFeeVo> prpLAssessorFeeVoList){
		
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo sysUserVo=WebUserUtils.getUser();
		BindInvoiceReqDto bindInvoiceReqDto=new BindInvoiceReqDto();
		try {
			    if(StringUtils.isBlank(billId) || StringUtils.isBlank(interCodeId) || (prpLAssessorFeeVoList==null || prpLAssessorFeeVoList.size()==0)){
			    	throw new RuntimeException("操作页面传入参数值有误，请核实后再操作！");
			    }
			    bindInvoiceReqDto.setSendTime(DateString(new Date()));
				bindInvoiceReqDto.setSystemType("102001");//车理赔编码
				List<PrpJBatchDto> prpJBatchDtoList=new ArrayList<PrpJBatchDto>();
				for(PrpLAssessorFeeVo feeVo:prpLAssessorFeeVoList){
					if(feeVo!=null){//防止页面传过来的对像为空对像
						if(feeVo.getId()==null){
							throw new RuntimeException("操作页面传入参数值有误，请核实后再操作！");
						}else{
							PrpLAssessorFeeVo prpLAssessorFeeVo=billlclaimService.findPrpLAssessorFeeById(feeVo.getId());
							if(prpLAssessorFeeVo!=null && StringUtils.isNotBlank(prpLAssessorFeeVo.getLinkBillNo())){
								throw new RuntimeException("由于页面长时间未操作,计算书号："+prpLAssessorFeeVo.getCompensateNo()+"已被其它发票绑定！请刷新页面重新操作！");
							}
							
						}
					}
					
				}
				PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));
				if("1".equals(prpLbillinfoVo.getRegisterStatus())){
					throw new RuntimeException("由于页面长时间未操作,发票号："+prpLbillinfoVo.getBillNo()+"已被其他人推送！");
				}else{
					PrpJBatchDto prpJBatchDto=new PrpJBatchDto();
					prpJBatchDto.setInvoiceId(prpLbillinfoVo.getVatbillId());//vat发票id
						List<PrpJecdInvoiceDto> prpJecdInvoiceDtoList=new ArrayList<PrpJecdInvoiceDto>();
						for(PrpLAssessorFeeVo feeVo:prpLAssessorFeeVoList){
							if(feeVo!=null){//防止页面传过来的对像为空对像
								PrpLAssessorFeeVo prpLAssessorFeeVo=billlclaimService.findPrpLAssessorFeeById(feeVo.getId());
								PrpJecdInvoiceDto prpJecdInvoiceDto=new PrpJecdInvoiceDto();
								prpJecdInvoiceDto.setCertiNo(prpLAssessorFeeVo.getCompensateNo());
								prpJecdInvoiceDto.setChargeCode("13");//费用类型
								prpJecdInvoiceDto.setDeductionAmount(prpLAssessorFeeVo.getAmount().toString());
								prpJecdInvoiceDto.setInvoiceId(prpLbillinfoVo.getVatbillId());
								prpJecdInvoiceDto.setInvoiceType("1");//专票
								prpJecdInvoiceDto.setLossType("1");//业务类型
								PrpdIntermMainVo intermMainVo = managerService.findIntermById(Long.valueOf(interCodeId));
								PrpdIntermBankVo intermBankVo = intermMainVo.getPrpdIntermBank();
								prpJecdInvoiceDto.setPlanFee(prpLAssessorFeeVo.getAmount().toString());//支付金额
								prpJecdInvoiceDto.setPayeeId(intermBankVo.getId().toString());//收款人id
								prpJecdInvoiceDto.setRegistNo(prpLAssessorFeeVo.getRegistNo());//报案号
								prpJecdInvoiceDto.setPolicyNo(prpLAssessorFeeVo.getPolicyNo());//保单号
								prpJecdInvoiceDtoList.add(prpJecdInvoiceDto);
							}
							
						}
						prpJBatchDto.setPrpJecdInvoiceDtoList(prpJecdInvoiceDtoList);
						prpJBatchDtoList.add(prpJBatchDto);
					}
	
			  
			  bindInvoiceReqDto.setPrpJBatchDtoList(prpJBatchDtoList);
			  //推送vat
			  Map<String,String> map=exchangeVatService.sendBillRegisterToVat(bindInvoiceReqDto, sysUserVo);
			  if("1".equals(map.get("flag"))){
				  for(PrpLAssessorFeeVo feeVo:prpLAssessorFeeVoList){
					 if(feeVo!=null) {
						  billlclaimService.savePrpLAssessorFee(feeVo.getId(),prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
						  prpLbillinfoVo.setSendstatus("1");
						  prpLbillinfoVo.setRegisterStatus("1");
						  billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
					 }
					  
				  }
				  
				  ajaxResult.setStatus(HttpStatus.SC_OK);
			  }else{
				  ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				  ajaxResult.setStatusText("推送公估费发票绑定接口失败-->:"+map.get("returnMessage"));
			  }
		} catch (Exception e) {
			logger.info("公估费发票推送出错",e);
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
		}

		return ajaxResult;
	}

	/**
	 * 查勘费发票推送
	 * @param billId
	 * @param vatQueryViewVoList
	 * @return
	 */
	@RequestMapping("/savecheBillEdit.do")
	@ResponseBody
	public AjaxResult savecheBillEdit(
			@FormModel("billId") String billId,
			@FormModel("interCodeId") String interCodeId,
			@FormModel("taskNo") String taskNo,
			@FormModel("checkFeeVo") List<PrpLCheckFeeVo> prpLCheckFeeVoList){
		
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo sysUserVo=WebUserUtils.getUser();
		BindInvoiceReqDto bindInvoiceReqDto=new BindInvoiceReqDto();
		try {
			    if(StringUtils.isBlank(billId) || StringUtils.isBlank(interCodeId) || (prpLCheckFeeVoList==null || prpLCheckFeeVoList.size()==0)){
			    	throw new RuntimeException("操作页面传入参数值有误，请核实后再操作！");
			    }
			    bindInvoiceReqDto.setSendTime(DateString(new Date()));
				bindInvoiceReqDto.setSystemType("102001");//车理赔编码
				List<PrpJBatchDto> prpJBatchDtoList=new ArrayList<PrpJBatchDto>();
				for(PrpLCheckFeeVo feeVo:prpLCheckFeeVoList){
					if(feeVo!=null){//防止页面传过来的对像为空对像
						if(feeVo.getId()==null){
							throw new RuntimeException("操作页面传入参数值有误，请核实后再操作！");
						}else{
							PrpLCheckFeeVo prpLCheckFeeVo=billlclaimService.findPrpLCheckFeeById(feeVo.getId());
							if(prpLCheckFeeVo!=null && StringUtils.isNotBlank(prpLCheckFeeVo.getLinkBillNo())){
								throw new RuntimeException("由于页面长时间未操作,计算书号："+prpLCheckFeeVo.getCompensateNo()+"已被其它发票绑定！请刷新页面重新操作！");
							}
							
						}
					}
					
				}
				PrpLbillinfoVo prpLbillinfoVo=billlclaimService.findPrpLbillinfoById(Long.valueOf(billId));
				if("1".equals(prpLbillinfoVo.getRegisterStatus())){
					throw new RuntimeException("由于页面长时间未操作,发票号："+prpLbillinfoVo.getBillNo()+"已被其他人推送！");
				}else{
					PrpJBatchDto prpJBatchDto=new PrpJBatchDto();
					prpJBatchDto.setInvoiceId(prpLbillinfoVo.getVatbillId());//vat发票id
						List<PrpJecdInvoiceDto> prpJecdInvoiceDtoList=new ArrayList<PrpJecdInvoiceDto>();
						for(PrpLCheckFeeVo feeVo:prpLCheckFeeVoList){
							if(feeVo!=null){//防止页面传过来的对像为空对像
								PrpLCheckFeeVo prpLCheckFeeVo=billlclaimService.findPrpLCheckFeeById(feeVo.getId());
								PrpJecdInvoiceDto prpJecdInvoiceDto=new PrpJecdInvoiceDto();
								prpJecdInvoiceDto.setCertiNo(prpLCheckFeeVo.getCompensateNo());
								prpJecdInvoiceDto.setChargeCode("04");//费用类型
								prpJecdInvoiceDto.setDeductionAmount(prpLCheckFeeVo.getAmount().toString());
								prpJecdInvoiceDto.setInvoiceId(prpLbillinfoVo.getVatbillId());
								prpJecdInvoiceDto.setInvoiceType("1");//专票
								prpJecdInvoiceDto.setLossType("1");//业务类型
								PrpdCheckBankMainVo intermMainVo = managerService.findCheckById(Long.valueOf(interCodeId));
								PrpdcheckBankVo intermBankVo = intermMainVo.getPrpdcheckBank();
								prpJecdInvoiceDto.setPlanFee(prpLCheckFeeVo.getAmount().toString());//支付金额
								prpJecdInvoiceDto.setPayeeId(intermBankVo.getId().toString());//收款人id
								prpJecdInvoiceDto.setRegistNo(prpLCheckFeeVo.getRegistNo());//报案号
								prpJecdInvoiceDto.setPolicyNo(prpLCheckFeeVo.getPolicyNo());//保单号
								prpJecdInvoiceDtoList.add(prpJecdInvoiceDto);
							}
							
						}
						prpJBatchDto.setPrpJecdInvoiceDtoList(prpJecdInvoiceDtoList);
						prpJBatchDtoList.add(prpJBatchDto);
					}
	
			  
			  bindInvoiceReqDto.setPrpJBatchDtoList(prpJBatchDtoList);
			  //推送vat
			  Map<String,String> map=exchangeVatService.sendBillRegisterToVat(bindInvoiceReqDto, sysUserVo);
			  if("1".equals(map.get("flag"))){
				  for(PrpLCheckFeeVo feeVo:prpLCheckFeeVoList){
					  if(feeVo!=null) {
						  billlclaimService.savePrpLCheckFee(feeVo.getId(),prpLbillinfoVo.getBillNo()+"_"+prpLbillinfoVo.getBillCode());
						  prpLbillinfoVo.setSendstatus("1");
						  prpLbillinfoVo.setRegisterStatus("1");
						  billlclaimService.saveOrUpdatePrpLbillinfoVo(prpLbillinfoVo);
					  }
					  
				  }
				  
				  ajaxResult.setStatus(HttpStatus.SC_OK);
			  }else{
				  ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				  ajaxResult.setStatusText("推送查勘费发票绑定接口失败-->:"+map.get("returnMessage"));
			  }
		} catch (Exception e) {
			logger.info("查勘费发票推送出错",e);
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
		}

		return ajaxResult;
	}

}

