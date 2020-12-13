package ins.sino.claimcar.platform.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.platform.service.PlatformTaskUploadService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 平台交互查询、平台补送
 */
@Controller
@RequestMapping(value = "/platformAlternately")
public class PlatformReuploadAction {
	private static Logger logger = LoggerFactory.getLogger(PlatformReuploadAction.class);

	@Autowired
	PlatformReUploadService reUploadService;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	RegistService registService;
	
	@Autowired
	PlatformTaskUploadService platformTaskUploadService;

	// 平台查询初始化
	@RequestMapping(value = "/platformQueryList.do")
	@ResponseBody
	public ModelAndView platformLogQuery() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Map<String, String> uploadNodeMap = new HashMap<String, String>();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		for (RequestType reqestType : RequestType.values()) {
			String value = reqestType.toString();
			String indexOf = value.substring(value.length() - 2, value.length());
			String code = reqestType.getCode();
			String name = "SH".equals(indexOf) ? "上海"+reqestType.getName()
					: reqestType.getName();
			uploadNodeMap.put(code, name);
		}
		mav.addObject("uploadNodeMap", uploadNodeMap);
		mav.addObject("requestTimeStart", startDate);
		mav.addObject("requestTimeEnd", endDate);
		mav.addObject("comCode", comCode);
		mav.setViewName("base/platform/PlatformQueryList");
		return mav;
	}
	
	
	@RequestMapping(value = "/platformTaskQueryList.do")
	@ResponseBody
	public ModelAndView platformTaskQuery() {
		ModelAndView mav = new ModelAndView();
		String comCode = WebUserUtils.getComCode();
		Map<String, String> uploadNodeMap = new HashMap<String, String>();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		for (RequestType reqestType : RequestType.values()) {
			String value = reqestType.toString();
			String indexOf = value.substring(value.length() - 2, value.length());
			String code = reqestType.getCode();
			String name = "SH".equals(indexOf) ? "上海"+reqestType.getName()
					: reqestType.getName();
			uploadNodeMap.put(code, name);
		}
		mav.addObject("uploadNodeMap", uploadNodeMap);
		mav.addObject("requestTimeStart", startDate);
		mav.addObject("requestTimeEnd", endDate);
		mav.addObject("comCode", comCode);
		mav.setViewName("base/platformTask/PlatformTaskQueryList");
		return mav;
	}

	/**
	 * 平台交互查询
	 */
	@RequestMapping(value = "/search.do")
	@ResponseBody
	public String searchPlatformLog(
			@FormModel(value = "platformQueryVo") CiClaimPlatformLogVo queryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length)throws Exception {
		String userCode = WebUserUtils.getUserCode();
		queryVo.setUserCode(userCode);
		ResultPage<CiClaimPlatformLogVo> resultPage = reUploadService.findPaltformInfoForPage(queryVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id", "comCode:ComCodeFull",  
				"claimSeqNo", "bussNo", "status", "requestName", "errorCode",
				"errorMessage","createTime");
		return jsonData;
	}
	
	
	/**
	 * 平台交互查询
	 */
	@RequestMapping(value = "/searchTask.do")
	@ResponseBody
	public String searchPlatformTask(
			@FormModel(value = "platformQueryVo") CiClaimPlatformTaskVo queryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length)throws Exception {
		String userCode = WebUserUtils.getUserCode();
		queryVo.setUserCode(userCode);
		ResultPage<CiClaimPlatformTaskVo> resultPage = reUploadService.findPaltformTaskForPage(queryVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(resultPage, "id",   
				"claimSeqNo", "bussNo", "status", "requestName", "remark","lastDate");
		return jsonData;
	}

	
	/**
	 * 确定补传 -- 重新在组装一次报文，发送
	 * @param logId
	 * @throws Exception
	 */
	@RequestMapping(value = "/platformReload.do")
	@ResponseBody
	public AjaxResult platformReload(Long logId) throws Exception{
		AjaxResult ajaxResult = new AjaxResult();
		try {
			reUploadService.platformReUpload(logId);
//			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText("补传成功！");
			
			// 补传成功后，把该条数据的状态改为 - 已补送，
			reUploadService.platformLogUpdate(logId);
		} catch (IllegalArgumentException e) {
			ajaxResult.setStatusText(e.toString());
		}
		return ajaxResult;
	}
	
	
	@RequestMapping(value = "/platformTaskReload.do")
	@ResponseBody
	public AjaxResult platformTaskReload(Long logId,Long taskId) throws Exception{
		AjaxResult ajaxResult = new AjaxResult();
		try {
			//如果ciclaimplatformlog表的ID不为空，则用原来的补传方法
			if(logId!=null){
				reUploadService.platformReUpload(logId);
				// 补传成功后，把该条数据的状态改为 - 已补送，
				reUploadService.platformLogUpdate(logId);
			}else{
				platformTaskUploadService.uploadPlatformByTaskId(taskId);
			}
			
			ajaxResult.setStatusText("补传成功！");
		} catch (IllegalArgumentException e) {
			ajaxResult.setStatusText(e.toString());
		}
		return ajaxResult;
	}
	
	/** 补送界面初始化 **/
	@RequestMapping(value = "/platformReloadInit.do")
	public ModelAndView platformReloadInit(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Long logId = Long.parseLong(request.getParameter("logId"));
		CiClaimPlatformLogVo logVo = reUploadService.findLogById(logId);
		String comCode = logVo.getComCode().substring(0, 2);
//		String reqType = logVo.getRequestType();
		String reqName = logVo.getRequestName();
		logVo.setRequestName("22".equals(comCode) ? "上海"+reqName : reqName);
		mav.addObject("logVo", logVo);
		mav.addObject("reqXml", replaceXml(logVo.getRequestXml()));
		
		String resXml = logVo.getResponseXml();
		if(StringUtils.isEmpty(resXml)){
			resXml = "<没有返回信息！>";
		}
		mav.addObject("resXml", replaceXml(resXml));
		mav.setViewName("base/platform/PlatformReloadEdit");
		return mav;
	}
	
	/** 补送自动任务的界面初始化 **/
	@RequestMapping(value = "/platformTaskReloadInit.do")
	public ModelAndView platformTaskReloadInit(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		Long taskId = Long.parseLong(request.getParameter("taskId"));
		CiClaimPlatformLogVo logVo = reUploadService.findLogByTaskId(taskId);
		CiClaimPlatformTaskVo taskVo = reUploadService.findPlatformTaskById(taskId);
		PrpLRegistVo registVo = registService.findRegistByRegistNo(taskVo.getRegistNo());
		taskVo.setComCode(registVo.getComCode());
		mav.addObject("taskVo", taskVo);
		if(logVo!=null){
			mav.addObject("reqXml", replaceXml(logVo.getRequestXml()));
			String resXml = logVo.getResponseXml();
			if(StringUtils.isEmpty(resXml)){
				resXml = "<没有返回信息！>";
			}
			mav.addObject("resXml", replaceXml(resXml));
			mav.addObject("logId",logVo.getId());
		}
		
		mav.setViewName("base/platformTask/PlatformTaskReloadEdit");
		return mav;
	}
	
	private String replaceXml(String xml){
		return xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	
	/**
	 * 针对上海的保单，理算提交时，若理算前的节点未上传成功，不允许提交，硬控制，理算前节点包括（报案、立案、定核损、单证收集）
	 * @param registNo
	 * @param policyNo
	 * @return
	 */
	@RequestMapping(value = "/isPlatformSH.ajax")
	@ResponseBody
	public AjaxResult isPlatformSH(String registNo,String policyNo){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(true);// 不校验
		PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		if(cMainVo != null && cMainVo.getComCode().startsWith("22")){
			String riskCode = cMainVo.getRiskCode();
			String comCode = cMainVo.getComCode();
			// 上海报案节点
			String reqType_regist = Risk.DQZ.equals(riskCode) ? 
					RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH.getCode();
			CiClaimPlatformLogVo registLog = reUploadService.findLogByBussNo(reqType_regist, registNo, comCode);
			
			// 上海立案
			String reqType_claim = Risk.DQZ.equals(riskCode) ? 
					RequestType.ClaimInfoCI_SH.getCode() : RequestType.ClaimInfoBI_SH.getCode();
			CiClaimPlatformLogVo claimLog = reUploadService.findLogByBussNo(reqType_claim, registNo, comCode);
			
			// 上海查勘定核损
			String reqType_loss = Risk.DQZ.equals(riskCode) ? 
					RequestType.LossInfoCI_SH.getCode() : RequestType.LossInfoBI_SH.getCode();
			CiClaimPlatformLogVo lossLog = reUploadService.findLogByBussNo(reqType_loss, registNo, comCode);
			
			// 上海单证
			String reqType_certify = Risk.DQZ.equals(riskCode) ? 
					RequestType.CertifyCI_SH.getCode() : RequestType.CertifyBI_SH.getCode();
			CiClaimPlatformLogVo certifyLog = reUploadService.findLogByBussNo(reqType_certify, registNo, comCode);
			
			// 全部存在值,true
			 
			if(registLog != null && claimLog != null && lossLog != null && certifyLog != null){
				ajaxResult.setData(true);
			}else{
				ajaxResult.setData(false);// 报案登记、立案登记、查勘定核损、单证节点未全部上传成功
			}
		}
		return ajaxResult;
	}
	
	/**
	 * 平台补送数据初始化 ☆Luwei(2017年3月8日 下午6:18:53): <br>
	 */
	@RequestMapping(value = "/platformReloadData.do")
	public ModelAndView platformReloadDataInit() {
		ModelAndView modelAndView = new ModelAndView();
		Map<String, String> uploadNodeMap = new HashMap<String, String>();
		
//		for (RequestType reqestType : RequestType.values()) {
//			String value = reqestType.toString();
//			String indexOf = value.substring(value.length() - 2, value.length());
//			String code = "SH".equals(indexOf) ? reqestType.getCode() + "_sh"
//					: reqestType.getCode();
		// String name = "SH".equals(indexOf) ? "上海" + reqestType.getName()
//					: reqestType.getName();
//			uploadNodeMap.put(code, name);
//		}
		
		// 第一版 暂时先 写入赔款支付
		// uploadNodeMap.put(RequestType.PaymentCI.getCode(),RequestType.PaymentCI.getName());//交强赔款支付(全国)
		// uploadNodeMap.put(RequestType.PaymentBI.getCode(),RequestType.PaymentBI.getName());//商业赔款支付(全国)
		// uploadNodeMap.put(RequestType.PaymentCI_SH.getCode()+"CI",RequestType.PaymentCI_SH.getName());//交强赔款支付(上海)
		// uploadNodeMap.put(RequestType.PaymentBI_SH.getCode()+"BI",RequestType.PaymentBI_SH.getName());//商业赔款支付(上海)
		
		uploadNodeMap.put("platform_reSendRegist","报案补送平台（全国/根据报案号补传）");
		uploadNodeMap.put("payment","赔款支付（全国）");
		uploadNodeMap.put("paymentSH","赔款支付（上海）");
		uploadNodeMap.put("HNIS_claim","立案送河南消保");
		uploadNodeMap.put("HNIS_endCase","结案送河南消保");
		uploadNodeMap.put("GZIS_claim","立案送贵州消保");
		uploadNodeMap.put("GZIS_endCase","结案送贵州消保");
		uploadNodeMap.put("platform_reSendAll","全国/上海平台(按理赔编码补传)");
		uploadNodeMap.put("Invoice","营改增推送发票");
		uploadNodeMap.put("AssessorFee_Invoice","公估费送发票");
		uploadNodeMap.put("SDEW_regist","山东风险预警系统(报案登记)");
		uploadNodeMap.put("SZClaim","理赔赔案信息送深圳警保");
		uploadNodeMap.put("SZReg","深圳警保报案信息上传");
		uploadNodeMap.put("genilex","精励联讯上传");
		modelAndView.addObject("uploadNodeMap", uploadNodeMap);
		modelAndView.setViewName("base/platform/ReloadPlatformData");
		return modelAndView; 
	}
	
	/**
	 * 平台补送数据 后台处理（批量补送） ☆Luwei(2017年3月9日 上午9:26:57): <br>
	 */
	@RequestMapping(value = "dataReloadSend.ajax")
	@ResponseBody
	public AjaxResult dataReloadSend(String uploadNode, String bussNoArray)throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try{
		if ((StringUtils.isNotEmpty(uploadNode) && StringUtils.isNotEmpty(bussNoArray))) {
			SysUserVo userVo = WebUserUtils.getUser();
			String bussNoList = reUploadService.dataReloadSend(uploadNode,bussNoArray,userVo);
			ajaxResult.setData(bussNoList);
				ajaxResult.setStatusText("补送完成！");
		} else {
				ajaxResult.setStatusText("补送失败！上传节点或者业务号列表不正确！");
		}
		}catch(RuntimeException e ){
			ajaxResult.setStatusText("补送失败！"+e.getMessage());
		}
		return ajaxResult;
	}

	/**
	 * 后台补传(含缺失环节) -- 重新在组装一次报文，发送
	 * @param logId
	 * @throws Exception
	 */
	@RequestMapping(value = "/platformReupload.do")
	@ResponseBody
	public AjaxResult platformReupload(Long logId,String requestType,String comCode,String bussNo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			if( !ObjectUtils.isEmpty(logId)){
				reUploadService.platformReUpload(logId);
				// 补传成功后，把该条数据的状态改为 - 已补送，
				reUploadService.platformLogUpdate(logId);
			}else{
				CiClaimPlatformLogVo logVo = new CiClaimPlatformLogVo();
				logVo.setRequestType(requestType);
				logVo.setComCode(comCode);
				logVo.setBussNo(bussNo);
				reUploadService.platformFirstUpload(logVo);
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setStatusText("补传完成！");
		}catch(Exception e){
			logger.error("platform Reupload error: ", e);
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText("补传失败！");
		}
		return ajaxResult;
	}
}
