package ins.sino.claimcar.subrogation.web.action;



import freemarker.core.ParseException;
import ins.framework.common.ResultPage;
import ins.framework.web.util.ResponseUtils;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.regist.service.FestivalService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLfestivalVo;
import ins.sino.claimcar.subrogation.platform.service.SubrogationToPlatService;
import ins.sino.claimcar.subrogation.vo.BeSubrogationVo;
import ins.sino.claimcar.subrogation.vo.CClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.PolicyRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.RecoveryResultVo;
import ins.sino.claimcar.subrogation.vo.RiskWarnQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/subrogationQuery")
public class SubrogationQueryAction {
	
	private Logger logger = LoggerFactory.getLogger(SubrogationQueryAction.class);
	
	@Autowired
	private SubrogationToPlatService subrogationToPlatService;
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private PlatformReUploadService platformLogService;
	@Autowired
	FestivalService festivalService;

	/**
	 * 风险预警保单信息 
	 */
	@RequestMapping("/policyQueryList.do")
	public ModelAndView policyQuery(String registNo,String nodeCode) {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/PolicyQueryList");
		return modelAndView;
	}
	
	
	/**
	 * 风险预警保单查询
	 * ☆YangKun(2016年3月23日 上午11:42:46): <br>
	 */
	@RequestMapping(value = "/policyNoSeach")
	@ResponseBody
	public String policyNoSeach(RiskWarnQueryVo queryVo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo==null){
			throw new IllegalStateException("请输入正确的报案号！");
		}
		queryVo.setComCode(registVo.getComCode());
//		queryVo.setComCode("11010083");
		
		//CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, claimVo.getRegistNo(),claimVo.getComCode());
		try{
			List<PolicyRiskWarnVo> policyRiskWarnList = new ArrayList<PolicyRiskWarnVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// 交强险
				policyRiskWarnList = subrogationToPlatService.sendPolicyRiskWarnCI(queryVo);
	
			}else{
				policyRiskWarnList = subrogationToPlatService.sendPolicyRiskWarnBI(queryVo);
			}
			//假日翻倍险
			if(validateFestivalRisk(registVo.getDamageTime(),registVo.getRegistNo())){
				if(policyRiskWarnList!=null && policyRiskWarnList.size()>0){
					for(PolicyRiskWarnVo vo:policyRiskWarnList){
						vo.setLimitAmount(vo.getLimitAmount()+vo.getFestivalAmount());
					}
				}
			}
			
			ResultPage<PolicyRiskWarnVo> resultPage = new ResultPage<PolicyRiskWarnVo>(0,100,policyRiskWarnList.size(),policyRiskWarnList);
	
			String jsonData = ResponseUtils.toDataTableJson(resultPage,"insurerCode:DWInsurerCode","insurerArea:DWInsurerArea","coverageType:DWCoverageType","limitAmount","insuredCA:YN01");
			logger.debug("forModifySeach.jsonData="+jsonData);
			
			return jsonData;
		}catch(Exception e){
			//e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
	}
	
	
	/**
	 * 风险预警理赔信息
	 */
	@RequestMapping("/claimQueryList.do")
	public ModelAndView claimQuery(String registNo,String nodeCode) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/ClaimQueryList");
		return modelAndView;
	}
	
	
	/**
	 * 风险预警理赔查询
	 */
	@RequestMapping(value = "/claimSeach")
	@ResponseBody
	public String claimSeach(RiskWarnQueryVo queryVo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo==null){
			throw new IllegalStateException("请输入正确的报案号！");
		}
		queryVo.setComCode(registVo.getComCode());
//		queryVo.setComCode("11010083");
		try{
		List<ClaimRiskWarnVo> claimRiskWarnList = new ArrayList<ClaimRiskWarnVo>();
		if("11".equals(queryVo.getRiskCodeSub())){// 交强险
			claimRiskWarnList = subrogationToPlatService.sendClaimRiskWarnCI(queryVo);
		}else{
			claimRiskWarnList = subrogationToPlatService.sendClaimRiskWarnBI(queryVo);
		}
		
		ResultPage<ClaimRiskWarnVo> resultPage = new ResultPage<ClaimRiskWarnVo>(0,100,claimRiskWarnList.size(),claimRiskWarnList);

		String jsonData = ResponseUtils.toDataTableJson(resultPage,"insurerCode:DWInsurerCode","insurerArea:DWInsurerArea","vehicleProperty"
				,"notificationTime","lossTime","lossArea","lossDesc","claimStatus:ClaimStatus");
		logger.debug("forModifySeach.jsonData="+jsonData);
			return jsonData;
		}catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}
		
	}
	
	/**
	 * 被代为求偿 查询
	 */
	@RequestMapping("/subrogationQueryList.do")
	public ModelAndView subrogationQuery(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/SubrogationQueryList");
		return modelAndView;
	}
	
	/**
	 * 被代为求偿接口 查询
	 */
	@RequestMapping(value = "/beSubrogationSeach")
	@ResponseBody
	public String beSubrogationSeach(SubrogationQueryVo queryVo) throws Exception {
		String requestType = RequestType.RegistInfoBI.getCode();
		if("11".equals(queryVo.getRiskCodeSub())){//交强险
			requestType = RequestType.RegistInfoCI.getCode();
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		
		if(registVo==null){
			throw new IllegalStateException("请输入正确的报案号！");
		}
		queryVo.setComCode(registVo.getComCode());
		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, queryVo.getRegistNo(),registVo.getComCode());
		queryVo.setClaimCode(formLogVo.getClaimSeqNo());//理赔编码
		
		//根据保单号查询投保确认号 
		PrpLCMainVo prpLcMainVo =null;
		if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
			prpLcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(queryVo.getRegistNo(),queryVo.getPolicyNo());
			if(prpLcMainVo!=null){
				queryVo.setConfirmSequenceNo(prpLcMainVo.getValidNo());
			}
		}
		
		try{
			List<BeSubrogationVo> subrogationRiskWarnList = new ArrayList<BeSubrogationVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// 交强险
				subrogationRiskWarnList = subrogationToPlatService.sendBeSubrogationQueryCI(queryVo);
			}else{
				subrogationRiskWarnList = subrogationToPlatService.sendBeSubrogationQueryBI(queryVo);
			}
			
			ResultPage<BeSubrogationVo> resultPage = new ResultPage<BeSubrogationVo>(0,100,subrogationRiskWarnList.size(),subrogationRiskWarnList);
	
			String jsonData = ResponseUtils.toDataTableJson(resultPage,"insurerCode:DWInsurerCode","insurerArea:DWInsurerArea","coverageType:DWCoverageType","policyNo","claimNotificationNo"
					,"recoveryCode","lockedTime","claimStatus:ClaimStatus","recoveryCodeStatus:RecoveryCodeStatus");
			logger.debug("forModifySeach.jsonData="+jsonData);
			return jsonData;
		}catch(Exception e){
			throw new IllegalStateException(e.getMessage());
		}
		
	}
	/**
	 * 结算码查询
	 */
	@RequestMapping("/recoveryQueryList.do")
	public ModelAndView recoveryQuery(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/RecoveryQueryList");
		return modelAndView;
	}
	
	@RequestMapping(value = "/recoverySeach")
	@ResponseBody
	public String recoverySeach(SubrogationQueryVo queryVo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo==null){
			throw new IllegalStateException("请输入正确的报案号！");
		}
		
		String requestType = RequestType.RegistInfoBI.getCode();
		if("11".equals(queryVo.getRiskCodeSub())){//交强险
			requestType = RequestType.RegistInfoCI.getCode();
		}
		
		queryVo.setComCode(registVo.getComCode());
		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, queryVo.getRegistNo(),registVo.getComCode());
		if(formLogVo==null){
			throw new IllegalStateException("该案件理赔编码为空,请核查！");
		}
		queryVo.setClaimCode(formLogVo.getClaimSeqNo());//理赔编码
//		queryVo.setComCode("11010083");
		
		try{
			List<RecoveryResultVo> resultList = new ArrayList<RecoveryResultVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// 交强险
				resultList = subrogationToPlatService.sendRecoveryQueryCI(queryVo);
	
			}else{
				resultList = subrogationToPlatService.sendRecoveryQueryBI(queryVo);
			}
			
			ResultPage<RecoveryResultVo> resultPage = new ResultPage<RecoveryResultVo>(0,100,resultList.size(),resultList);
			
			String jsonData = ResponseUtils.toDataTableJson(resultPage,"recoveryCode","recoveryCodeStatus:RecoveryCodeStatus",
					"failureTime","failureCause:FailureCause","recoverStatus:RecoverStatus","claimNotificationNo","coverageType:DWCoverageType");
			logger.debug("forModifySeach.jsonData="+jsonData);
			return jsonData;
		}catch(Exception e){
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
	}
	
	/**
	 * 代位求偿理赔信息
	 */
	@RequestMapping("/claimPaidQueryList.do")
	public ModelAndView claimPaidQuery(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/ClaimPaidQueryList");
		return modelAndView;
	}
	
	/**
	 *  代位求偿理赔信息查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/claimRecoverySearch.do")
	@ResponseBody
	public ModelAndView claimRecoverySearch(String registNo,String riskCodeSub,String recoveryCode,String types) throws Exception {
		SubrogationQueryVo queryVo = new SubrogationQueryVo();
		PrpLRegistVo vo = registQueryService.findByRegistNo(registNo);
		queryVo.setRegistNo(registNo);
		queryVo.setRecoveryCode(recoveryCode);
		queryVo.setRiskCodeSub(riskCodeSub);
		queryVo.setComCode(vo.getComCode());
		ModelAndView modelAndView = new ModelAndView();
		try{
			SubrogationDataVo resultList = new SubrogationDataVo();
			if("1101".equals(queryVo.getRiskCodeSub())){// 交强险
				resultList = subrogationToPlatService.sendSubrogationClaimCI(queryVo);
				modelAndView.addObject("flags", "1");
			}else{
				resultList = subrogationToPlatService.sendSubrogationClaimBI(queryVo);
				modelAndView.addObject("flags", "2");
			}
		/*	if(resultList.getUndwrtWriteAdjustmentDataVos()==null){
				throw new IllegalStateException("没有数据！！");
			}*/
			
			//判断显示那个查询界面
			if(types.equals("base")){
				modelAndView.setViewName("subrogation/ShowList");
				/*modelAndView.setViewName("subrogation/ShowErrorList");*/
			}else if(types.equals("baodan")){
				modelAndView.setViewName("subrogation/ShowList");
			}else if(types.equals("daiwei")){
				modelAndView.setViewName("subrogation/daiweiList");
			}else if(types.equals("regist")){
				modelAndView.setViewName("subrogation/registList");
			}else if(types.equals("check")){
				modelAndView.setViewName("subrogation/CheckList");
			}else if(types.equals("estimate")){
				modelAndView.setViewName("subrogation/EstimateList");
			}else if(types.equals("doc")){
				modelAndView.setViewName("subrogation/DocList");
			}else if(types.equals("underWrite")){
				modelAndView.setViewName("subrogation/UnderWriteList");
			}else if(types.equals("claimClose")){
				modelAndView.setViewName("subrogation/ClaimCloseList");
			}
			else if(types.equals("claimReopen")){
				modelAndView.setViewName("subrogation/ClaimReopenList");
			}else if(types.equals("recoveryConfirm")){
				modelAndView.setViewName("subrogation/RecoveryConfirList");
			}
			modelAndView.addObject("resultList", resultList);
			modelAndView.addObject("rRegistNo", registNo);
			modelAndView.addObject("rRiskCodeSub", riskCodeSub);
			modelAndView.addObject("rRecoveryCode", recoveryCode);
			return modelAndView;
		}catch(Exception e){
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		
	}
	
	
	/**
	 *  代位求偿理赔保单信息查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/claimBaodanSearch.do")
	@ResponseBody
	public ModelAndView claimBaodanSearch(String registNo,String riskCodeSub,String recoveryCode) throws Exception {
		SubrogationQueryVo queryVo=new SubrogationQueryVo();
		PrpLRegistVo vo = registQueryService.findByRegistNo(registNo);
		queryVo.setRegistNo(registNo);
		queryVo.setRecoveryCode(recoveryCode);
		queryVo.setRiskCodeSub(riskCodeSub);
		queryVo.setComCode(vo.getComCode());
		try{
			ModelAndView modelAndView = new ModelAndView();
			if("1101".equals(queryVo.getRiskCodeSub())){// 交强险
				CClaimDataVo resultList = new CClaimDataVo();
				resultList = subrogationToPlatService.sendSubrogationBaodanCI(queryVo);
				modelAndView.setViewName("subrogation/BaoDanList");
				modelAndView.addObject("resultList", resultList);
			}else{
				ClaimDataVo resultList = new ClaimDataVo();
				resultList = subrogationToPlatService.sendSubrogationBaodanBI(queryVo);
				modelAndView.setViewName("subrogation/BiBaoDanList");
				modelAndView.addObject("resultList", resultList);
			}
			return modelAndView;
		}catch(Exception e){
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	
	@RequestMapping("/show.do")
	public ModelAndView show() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/ShowList");
		return modelAndView;
	}
	
	@RequestMapping("/baodan.do")
	public ModelAndView baodan() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/baodanList");
		return modelAndView;
	}
	
	 /**
	  * 验证是否符合节日翻倍险的应用条件
	  * @param damageDate
	  * @param registNo
	  * @return
	  */
	 private boolean validateFestivalRisk(Date damageDate,String registNo){
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(damageDate);
		 int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
		 int year = calendar.get(Calendar.YEAR);
		 boolean flag=false;
		 PrpLfestivalVo festivalVo1= festivalService.findPrpLfestivalVoByFestivalType("3",String.valueOf(year));
		 PrpLfestivalVo festivalVo2= festivalService.findPrpLfestivalVoByFestivalType("4",String.valueOf(year));
		//节日类型集合
		 String festivals=festivalVo1.getRemark();
		//工作日周末类型集合
		 String weekDays=festivalVo2.getRemark();
		
		       //判断是否符合假日翻倍险应用条件
		       //1、出险时间为国家法定节假日，并且承保法定节假日翻倍险则，三者险翻倍
		       //2、出险时间非国家法定节假日，或未承保法定节假日翻倍险则，三者险不翻倍
		    if(((weekDay==1 || weekDay==7) || festivals.contains(DateFormatString(damageDate))) && !weekDays.contains(DateFormatString(damageDate))){
		    	flag=true;
		    }
		     
		 return flag;
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
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
			  if(strDate!=null){
				  str=format.format(strDate);
			}
			  
			return str;
		}
}


