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
	 * ???????????????????????? 
	 */
	@RequestMapping("/policyQueryList.do")
	public ModelAndView policyQuery(String registNo,String nodeCode) {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/PolicyQueryList");
		return modelAndView;
	}
	
	
	/**
	 * ????????????????????????
	 * ???YangKun(2016???3???23??? ??????11:42:46): <br>
	 */
	@RequestMapping(value = "/policyNoSeach")
	@ResponseBody
	public String policyNoSeach(RiskWarnQueryVo queryVo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo==null){
			throw new IllegalStateException("??????????????????????????????");
		}
		queryVo.setComCode(registVo.getComCode());
//		queryVo.setComCode("11010083");
		
		//CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, claimVo.getRegistNo(),claimVo.getComCode());
		try{
			List<PolicyRiskWarnVo> policyRiskWarnList = new ArrayList<PolicyRiskWarnVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// ?????????
				policyRiskWarnList = subrogationToPlatService.sendPolicyRiskWarnCI(queryVo);
	
			}else{
				policyRiskWarnList = subrogationToPlatService.sendPolicyRiskWarnBI(queryVo);
			}
			//???????????????
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
	 * ????????????????????????
	 */
	@RequestMapping("/claimQueryList.do")
	public ModelAndView claimQuery(String registNo,String nodeCode) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/ClaimQueryList");
		return modelAndView;
	}
	
	
	/**
	 * ????????????????????????
	 */
	@RequestMapping(value = "/claimSeach")
	@ResponseBody
	public String claimSeach(RiskWarnQueryVo queryVo) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo==null){
			throw new IllegalStateException("??????????????????????????????");
		}
		queryVo.setComCode(registVo.getComCode());
//		queryVo.setComCode("11010083");
		try{
		List<ClaimRiskWarnVo> claimRiskWarnList = new ArrayList<ClaimRiskWarnVo>();
		if("11".equals(queryVo.getRiskCodeSub())){// ?????????
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
	 * ??????????????? ??????
	 */
	@RequestMapping("/subrogationQueryList.do")
	public ModelAndView subrogationQuery(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/SubrogationQueryList");
		return modelAndView;
	}
	
	/**
	 * ????????????????????? ??????
	 */
	@RequestMapping(value = "/beSubrogationSeach")
	@ResponseBody
	public String beSubrogationSeach(SubrogationQueryVo queryVo) throws Exception {
		String requestType = RequestType.RegistInfoBI.getCode();
		if("11".equals(queryVo.getRiskCodeSub())){//?????????
			requestType = RequestType.RegistInfoCI.getCode();
		}
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		
		if(registVo==null){
			throw new IllegalStateException("??????????????????????????????");
		}
		queryVo.setComCode(registVo.getComCode());
		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, queryVo.getRegistNo(),registVo.getComCode());
		queryVo.setClaimCode(formLogVo.getClaimSeqNo());//????????????
		
		//???????????????????????????????????? 
		PrpLCMainVo prpLcMainVo =null;
		if(StringUtils.isNotBlank(queryVo.getPolicyNo())){
			prpLcMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(queryVo.getRegistNo(),queryVo.getPolicyNo());
			if(prpLcMainVo!=null){
				queryVo.setConfirmSequenceNo(prpLcMainVo.getValidNo());
			}
		}
		
		try{
			List<BeSubrogationVo> subrogationRiskWarnList = new ArrayList<BeSubrogationVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// ?????????
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
	 * ???????????????
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
			throw new IllegalStateException("??????????????????????????????");
		}
		
		String requestType = RequestType.RegistInfoBI.getCode();
		if("11".equals(queryVo.getRiskCodeSub())){//?????????
			requestType = RequestType.RegistInfoCI.getCode();
		}
		
		queryVo.setComCode(registVo.getComCode());
		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, queryVo.getRegistNo(),registVo.getComCode());
		if(formLogVo==null){
			throw new IllegalStateException("???????????????????????????,????????????");
		}
		queryVo.setClaimCode(formLogVo.getClaimSeqNo());//????????????
//		queryVo.setComCode("11010083");
		
		try{
			List<RecoveryResultVo> resultList = new ArrayList<RecoveryResultVo>();
			if("11".equals(queryVo.getRiskCodeSub())){// ?????????
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
	 * ????????????????????????
	 */
	@RequestMapping("/claimPaidQueryList.do")
	public ModelAndView claimPaidQuery(String registNo,String nodeCode) {
	
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/ClaimPaidQueryList");
		return modelAndView;
	}
	
	/**
	 *  ??????????????????????????????
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
			if("1101".equals(queryVo.getRiskCodeSub())){// ?????????
				resultList = subrogationToPlatService.sendSubrogationClaimCI(queryVo);
				modelAndView.addObject("flags", "1");
			}else{
				resultList = subrogationToPlatService.sendSubrogationClaimBI(queryVo);
				modelAndView.addObject("flags", "2");
			}
		/*	if(resultList.getUndwrtWriteAdjustmentDataVos()==null){
				throw new IllegalStateException("??????????????????");
			}*/
			
			//??????????????????????????????
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
	 *  ????????????????????????????????????
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
			if("1101".equals(queryVo.getRiskCodeSub())){// ?????????
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
	  * ????????????????????????????????????????????????
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
		//??????????????????
		 String festivals=festivalVo1.getRemark();
		//???????????????????????????
		 String weekDays=festivalVo2.getRemark();
		
		       //?????????????????????????????????????????????
		       //1???????????????????????????????????????????????????????????????????????????????????????????????????
		       //2??????????????????????????????????????????????????????????????????????????????????????????????????????
		    if(((weekDay==1 || weekDay==7) || festivals.contains(DateFormatString(damageDate))) && !weekDays.contains(DateFormatString(damageDate))){
		    	flag=true;
		    }
		     
		 return flag;
	 }
	 
	 /**
		 * ??????????????????
		 * Date ???????????? String??????
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


