package ins.sino.claimcar.subrogation.web.action;



import freemarker.core.ParseException;
import ins.framework.common.ResultPage;
import ins.framework.service.CodeTranService;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.regist.service.FestivalService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLfestivalVo;
import ins.sino.claimcar.subrogation.platform.service.SubrogationToPlatService;
import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.service.LockedPolicyService;
import ins.sino.claimcar.subrogation.service.PlatLockDubboService;
import ins.sino.claimcar.subrogation.service.SubrogationHandleService;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.sh.service.SubrogationSHHandleService;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationResultVo;
import ins.sino.claimcar.subrogation.sh.vo.CopyInformationSubrogationViewVo;
import ins.sino.claimcar.subrogation.sh.vo.SubrogationSHQueryVo;
import ins.sino.claimcar.subrogation.vo.ConfirmQueryVo;
import ins.sino.claimcar.subrogation.vo.PrpLLockedNotifyVo;
import ins.sino.claimcar.subrogation.vo.PrpLLockedPolicyVo;
import ins.sino.claimcar.subrogation.vo.PrpLLockedThirdPartyVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatCheckSubVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatCheckVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.RecoveryReturnConfirmVo;
import ins.sino.claimcar.subrogation.vo.SCheckQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/subrogationEdit")
public class SubrogationEditAction {
	
	private Logger logger = LoggerFactory.getLogger(SubrogationEditAction.class);
	
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private SubrogationToPlatService subrogationToPlatService;
	@Autowired
	private LockedPolicyService lockedPolicyService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
	private PlatLockDubboService platLockDubboService;

	@Autowired
	private SubrogationHandleService subrogationHandleService;
	@Autowired
	private SubrogationSHHandleService subrogationSHHandleService;
	@Autowired
	private PlatformReUploadService platformLogService;
	
	@Autowired
	private SubrogationSHHandleService handleService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	FestivalService festivalService;
	/**
	 * ????????????????????????
	 */
	@RequestMapping("/lockConfirmQueryList.do")
	public ModelAndView lockConfirmQuery() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/LockConfirmList");
		return modelAndView;
	}
	
	/**
	 * ??????????????????
	 * @return
	 * @modified:
	 * ???YangKun(2016???3???25??? ??????3:43:58): <br>
	 */
	@RequestMapping("/lockConfirmSerach.do")
	@ResponseBody
	public String lockConfirmSerach(SubrogationQueryVo queryVo)throws Exception {
//		queryVo.setComCode("11010083");
//		queryVo.setClaimSequenceNo("50DHIC450015001436321990297296");
//		queryVo.setRegistNo("4000000201612060000346");
		
		PrpLRegistVo registVo = registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(registVo == null){
			throw new  IllegalStateException("???????????????????????????");
		}
		
//		//??????????????????????????????
		if (CodeConstants.ReportType.CI.equals(registVo.getReportType())) {
			throw new IllegalStateException("????????????????????????????????????????????????????????????");
		}
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(queryVo.getRegistNo());
		if (subrogationMainVo == null || !"1".equals(subrogationMainVo.getSubrogationFlag())) {
			throw new  IllegalStateException("?????????????????????????????????");
		}
		//??????????????????????????????  
		String requestType = RequestType.RegistInfoBI.getCode();
		CiClaimPlatformLogVo formLogVo = platformLogService.findLogByBussNo(requestType, queryVo.getRegistNo(),registVo.getComCode());
		if(formLogVo == null){
			throw new IllegalStateException("?????????????????????????????????");
		}
		queryVo.setComCode(registVo.getComCode());
		queryVo.setClaimSequenceNo(formLogVo.getClaimSeqNo());
		
		try{
			List<PrpLLockedPolicyVo> lockedPolicyList = subrogationToPlatService.sendLockConfirmQueryBI(queryVo);
			
			if(lockedPolicyList==null || lockedPolicyList.isEmpty()){
//				Assert.notNull(lockedPolicyList);
//				throw new AssertionError("????????????????????????,????????????????????????");
//				throw new IllegalStateException("????????????????????????,????????????????????????");
				return "????????????????????????,????????????????????????";
			}
			//???????????????????????????
			if(validateFestivalRisk(registVo.getDamageTime(),registVo.getRegistNo())){
			
				if(lockedPolicyList!=null && lockedPolicyList.size()>0){
					for(PrpLLockedPolicyVo vo:lockedPolicyList){
						vo.setLimitAmount(DataUtils.NullToZero(vo.getLimitAmount()).add(DataUtils.NullToZero(vo.getFestivalAmount())));
					}
				}
				
			}
			ResultPage<PrpLLockedPolicyVo> resultPage = new ResultPage<PrpLLockedPolicyVo>(0,100,lockedPolicyList.size(),lockedPolicyList);
			
			String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","claimSequenceNo","insurerCode:DWInsurerCode","insurerArea:DWInsurerArea","coverageType:DWCoverageType",
					"policyNo","limitAmount","isInsuredCA:YN01","licensePlateNo","licensePlateType:LicenseKindCode","engineNo","vin","matchTimes","coverageType");
			logger.debug("lockConfirmSerach.jsonData="+jsonData);
			
			return jsonData;
		
		}catch(Exception e){
			logger.error("??????????????????", e);
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	/**
	 * ??????????????????
	 */
	@RequestMapping("/lockedNotify.do")
	public ModelAndView lockedNotify(String registNo,String claimSequenceNo,String oppoentPolicyNo,String coverageType) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("subrogation/LockedNotifyList");
		
		PrpLLockedPolicyVo lockedPolicyVo = lockedPolicyService.findLockedPolicy(registNo,claimSequenceNo,oppoentPolicyNo,coverageType);
		
		mv.addObject("lockedNotifyList",lockedPolicyVo.getPrplLockedNotifies());
		mv.addObject("lockedPolicyVo",lockedPolicyVo);
		return mv;
	}
	
	/**
	 * ????????????????????????
	 * @param registNo
	 * @param claimSequenceNo
	 * @param oppoentPolicyNo
	 * @param coverageType
	 * @return
	 * @modified:
	 * ???YangKun(2016???3???30??? ??????3:35:37): <br>
	 */
	@RequestMapping("/lockedThirdParty.do")
	public ModelAndView lockedThirdParty(String registNo,String claimSequenceNo,String oppoentPolicyNo,String coverageType,String claimNotificationNo) {
		ModelAndView mv = new ModelAndView();
		
		PrpLLockedPolicyVo lockedPolicyVo = lockedPolicyService.findLockedPolicy(registNo,claimSequenceNo,oppoentPolicyNo,coverageType);
		List<PrpLLockedThirdPartyVo> thirdPartyList = new ArrayList<PrpLLockedThirdPartyVo>();
		for(PrpLLockedNotifyVo lockedNotifyVo : lockedPolicyVo.getPrplLockedNotifies()){
			if(claimNotificationNo.equals(lockedNotifyVo.getClaimNotificationNo().trim())){
				thirdPartyList = lockedNotifyVo.getPrplLockedThirdParties();
			}
		}
		
		mv.addObject("thirdPartyList",thirdPartyList);
		mv.addObject("claimNotificationNo",claimNotificationNo);
		mv.setViewName("subrogation/LockedThirdPartyList");
		
		return mv;
	}
	
	/**
	 * ???????????? 
	 * @param registNo
	 * @param claimSequenceNo
	 * @param oppoentPolicyNo
	 * @param coverageType
	 * @return
	 * @modified:
	 * ???YangKun(2016???3???30??? ??????11:02:30): <br>
	 */
	@RequestMapping(value = "/lockedConfirm.do")
	@ResponseBody
	public AjaxResult lockedConfirm(@FormModel("lockedPolicyVo")PrpLLockedPolicyVo lockedPolicyVo) {
		
		AjaxResult ajaxResult = new AjaxResult();
		lockedPolicyVo = lockedPolicyService.findLockedPolicy(lockedPolicyVo.getRegistNo(),lockedPolicyVo.getClaimSequenceNo(),
				lockedPolicyVo.getPolicyNo(),lockedPolicyVo.getCoverageType());
		try{
			//?????????????????? TODO
			PrpLRegistVo registVo = registQueryService.findByRegistNo(lockedPolicyVo.getRegistNo());
			String comCode = registVo.getComCode();
			
			String recoveryCode = subrogationToPlatService.sendLockedConfirmBI(lockedPolicyVo,comCode);
			//?????????????????????????????????
			this.organizePlatLock(lockedPolicyVo,registVo,recoveryCode);
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(recoveryCode);
		}catch(Exception e){
			logger.error("???????????? ", e);
			ajaxResult.setStatusText(e.getMessage());
			//e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * ?????????????????????
	 * @param lockedPolicyVo
	 * @param registVo
	 * @param recoveryCode
	 * @modified:
	 * ???YangKun(2016???3???30??? ??????3:10:42): <br>
	 */
	private void organizePlatLock(PrpLLockedPolicyVo lockedPolicyVo,PrpLRegistVo registVo,String recoveryCode) {
		
		PrpLRecoveryOrPayVo recoveryVo = new PrpLRecoveryOrPayVo();
		recoveryVo.setRecoveryCode(recoveryCode);
		recoveryVo.setRegistNo(registVo.getRegistNo());
		recoveryVo.setValidFlag("1");
		recoveryVo.setComCode(registVo.getComCode());
		recoveryVo.setCreateTime(new Date());
		recoveryVo.setCreateUser(WebUserUtils.getUserCode());
		
		PrpLPlatLockVo	platLockVo = new PrpLPlatLockVo();
		PrpLLockedNotifyVo lockedNotifyVo = lockedPolicyVo.getPrplLockedNotifies().get(0);
		platLockVo.setRecoveryCode(recoveryCode);
		platLockVo.setClaimSequenceNo(lockedPolicyVo.getClaimSequenceNo());
		platLockVo.setRegistNo(lockedPolicyVo.getRegistNo());
		platLockVo.setPolicyNo(registVo.getPolicyNo());
		platLockVo.setRiskCode(registVo.getRiskCode());
		platLockVo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
		platLockVo.setInsureName(registVo.getPrpLRegistExt().getInsuredName());
		
		platLockVo.setRecoveryOrPayFlag("1");
		platLockVo.setRecoveryCodeStatus("1");
		platLockVo.setRecoveryOrPayType(lockedPolicyVo.getCoverageType());
		platLockVo.setOppoentRegistNo(lockedNotifyVo.getClaimNotificationNo());
		platLockVo.setOppoentInsurerCode(lockedPolicyVo.getInsurerCode());
		platLockVo.setOppoentInsurerArea(lockedPolicyVo.getInsurerArea());
		platLockVo.setCoverageType(lockedPolicyVo.getCoverageType());
		platLockVo.setOppoentPolicyNo(lockedPolicyVo.getPolicyNo());
		platLockVo.setOppoentLicensePlateNo(lockedPolicyVo.getLicensePlateNo());
		platLockVo.setOppoentLicensePlateType(lockedPolicyVo.getLicensePlateType());
		platLockVo.setOppoentEngineNo(lockedPolicyVo.getEngineNo());
		platLockVo.setOppoentVin(lockedPolicyVo.getVin());
		platLockVo.setLossTime(lockedNotifyVo.getLossTime());
		platLockVo.setLossArea(lockedNotifyVo.getLossArea());
		platLockVo.setLossDesc(lockedNotifyVo.getLossDesc());
		platLockVo.setRecoveryCodeStatus("1");
		platLockVo.setCreateTime(new Date());
		platLockVo.setCreateUser(WebUserUtils.getUserCode());
		
		List<PrpLRecoveryOrPayVo> recoveryList = new ArrayList<PrpLRecoveryOrPayVo>();
		recoveryList.add(recoveryVo);
		platLockVo.setPrpLRecoveryOrPays(recoveryList);
			
		platLockDubboService.firstSavePlatLock(platLockVo);
	}

	/**
	 * ????????????
	 */
	@RequestMapping("/lockCancelQueryList.do")
	public ModelAndView lockCancelQuery() {
		ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("subrogation/LockCancelList");
		return modelAndView;
	}
	
	/**
	 * ??????????????????
	 * @return
	 * @modified:
	 * ???YangKun(2016???3???25??? ??????3:43:58): <br>
	 */
	@RequestMapping("/lockCancelSerach.do")
	@ResponseBody
	public String lockCancelSerach(SubrogationQueryVo queryVo)throws Exception {
		
		try{
			List<PrpLPlatLockVo> platLockList = platLockDubboService.findLockCancelList(queryVo.getRegistNo(),queryVo.getRecoveryCode());
			ResultPage<PrpLPlatLockVo> resultPage = new ResultPage<PrpLPlatLockVo>(0,100,platLockList.size(),platLockList);
			
			String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","recoveryCode","coverageType:DWCoverageType",
					"oppoentLicensePlateNo","oppoentLicensePlateType:LicenseKindCode","recoveryCodeStatus:RecoveryCodeStatus","failureTime","failureCause:FailureCause");
			logger.debug("lockCancelSerach.jsonData="+jsonData);
			
			return jsonData;
		
		}catch(Exception e){
//			e.printStackTrace();
			logger.error("?????????????????? ", e);
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	/**
	 * ???????????? ????????????????????????
	 * @return
	 * @modified:
	 * ???YangKun(2016???3???30??? ??????6:20:09): <br>
	 */
	@RequestMapping("/lockededData.do")
	public ModelAndView lockededData(String registNo,String recoveryCode) {
		ModelAndView mv = new ModelAndView();
//		List<PrpLPlatLockVo> platLockList = platLockDubboService.findLockCancelList(registNo,recoveryCode);
		PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockVo(registNo, recoveryCode);
		mv.addObject("platLockVo",platLockVo);
		mv.setViewName("subrogation/LockCancelCaseData");
		
		return mv;
	}
	
	
	@RequestMapping(value = "/lockedCancel.do")
	@ResponseBody
	public AjaxResult lockedCancel(String registNo,String recoveryCode,String failureCause) {
		
		AjaxResult ajaxResult = new AjaxResult();
		try{
			//?????????????????? TODO
			PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
			String comCode = registVo.getComCode();
			
			String resStr = subrogationToPlatService.sendLockedCancelBI(recoveryCode,failureCause,comCode);
			//String resStr ="1";
			//?????????????????????????????????
			PrpLPlatLockVo platLockVo = platLockDubboService.findLockCancelList(registNo,recoveryCode).get(0);
			if (platLockVo!=null && "1".equals(platLockVo.getRecoveryCodeStatus())) {
				platLockVo.setFailureCause(failureCause);
				platLockVo.setFailureTime(new Date());
				platLockVo.setRecoveryCodeStatus("9");//?????????
				platLockVo.setUnlockedOperateCode(WebUserUtils.getUserCode());
				platLockVo.setUnlockedOperateName(WebUserUtils.getUserName());
				
				for(PrpLRecoveryOrPayVo recoveryVo : platLockVo.getPrpLRecoveryOrPays()){
					recoveryVo.setValidFlag("0");
					recoveryVo.setRemark("?????????????????????");
					recoveryVo.setUpdateUser(WebUserUtils.getUserCode());
					recoveryVo.setUpdateTime(new Date());
				}
				platLockDubboService.savePlatLock(platLockVo);//TODO
			}
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(resStr);
		}catch(Exception e){
			logger.error("?????????????????????", e);
			ajaxResult.setStatusText(e.getMessage());
			//e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("/subrogationCheckQueryList.do")
	public ModelAndView subrogationCheckQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/SubrogationCheckList");
		return modelAndView;
	}
	
	
	/**
	 * ??????????????????
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sCheckQueryList")
	@ResponseBody
	public String sCheckQueryList(SCheckQueryVo queryVo) throws Exception {
//		queryVo.setComCode("11010083");
		String jsonData = "";
		//??????????????????
		PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockByRecoveryCode(queryVo.getAccountsNo());
		if (platLockVo ==null) {
			throw new IllegalStateException("???????????????????????????,???????????????!");
		}
		
		try{
			List<SubrogationCheckVo> subrogationCheckList = new ArrayList<SubrogationCheckVo>();
			PrpLRegistVo registVo = registQueryService.findByRegistNo(platLockVo.getRegistNo());
			queryVo.setComCode(registVo.getComCode());
			subrogationCheckList = subrogationToPlatService.sendSubrogationCheck(queryVo); 
			//subrogationCheckList = subrogationAddService.findSubrogationCheck(queryVo.getAccountsNo());
			ResultPage<SubrogationCheckVo> resultPage = new ResultPage<SubrogationCheckVo>(0,100,subrogationCheckList.size(),subrogationCheckList);

			jsonData = ResponseUtils.toDataTableJson(resultPage,"recoveryCode","recoverDStart","recoverComCode:DWInsurerCode","recoverAreaCode:DWInsurerArea","compensateComCode:DWInsurerCode",
					"compensateAreaCode:DWInsurerArea","recoverReportNo","compensateReportNo","coverageCode","checkStats:CheckStats");
			logger.debug("forModifySeach.jsonData="+jsonData);
			
			return jsonData;
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("??????????????????", e);
			throw new IllegalStateException(e.getMessage());
		}
		
			
		
		
	}
	
	/**
	 * ??????????????????
	 * @param recoveryCode
	 * @param recoverReportNo
	 * @param compensateReportNo
	 * @return
	 */
	@RequestMapping("/showCheck.do")
	public ModelAndView showCheck(String recoveryCode,String recoverReportNo,String compensateReportNo) {
		ModelAndView modelAndView = new ModelAndView();
		SubrogationCheckVo subrogationQuery = new SubrogationCheckVo();
		subrogationQuery.setRecoveryCode(recoveryCode);
		subrogationQuery.setRecoverReportNo(recoverReportNo);
		subrogationQuery.setCompensateReportNo(compensateReportNo);
		
		List<PrpLPlatCheckVo> platCheckVoList = subrogationHandleService.findByOther(subrogationQuery);
		
		if(platCheckVoList!=null &&  !platCheckVoList.isEmpty()){
			PrpLPlatCheckVo checkVo = platCheckVoList.get(0);
			modelAndView.addObject("platCheckVoList", platCheckVoList.get(0));
			List<PrpLPlatCheckSubVo> prpLPlatCheckSubVoList = checkVo.getPrpLPlatCheckSubs();
			
			
			modelAndView.addObject("prpLPlatCheckSubVoList", prpLPlatCheckSubVoList);
		}
		modelAndView.setViewName("subrogation/ShowCheckList");
		return modelAndView;
	}
	
	
	/**
	 * ??????????????????
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkQueryList")
	@ResponseBody
	public AjaxResult checkQueryList(SCheckQueryVo queryVo) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockByRecoveryCode(queryVo.getAccountsNo());
			PrpLRegistVo registVo = registQueryService.findByRegistNo(platLockVo.getRegistNo());
			queryVo.setComCode(registVo.getComCode());
			subrogationToPlatService.sendCheck(queryVo);
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatusText(e.getMessage());
			logger.error("????????????????????????", e);
			
		}
		return ajaxResult;
		
	}
	
	/**
	 * ??????????????????
	 */
	@RequestMapping("/recoveryConfirmQueryList.do")
	public ModelAndView recoveryConfirmQuery() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("subrogation/RecoveryConfirmList");
		return modelAndView;
	}


	/**
	 * ??????????????????
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/startRecoverySearch")
	@ResponseBody
	public AjaxResult startRecoverySearch(ConfirmQueryVo queryVo) throws Exception {
		AjaxResult ar = new AjaxResult();
		
		PrpLRegistVo prpLRegistVo= registQueryService.findByRegistNo(queryVo.getRegistNo());
		if(prpLRegistVo==null){
			ar.setStatus(HttpStatus.SC_OK);
			ar.setData("??????????????????????????????,???????????????!");
			return ar;
		}
		
		try{
			queryVo.setComCode(prpLRegistVo.getComCode());
	//		queryVo.setComCode("11010083");
			String message  = subrogationToPlatService.sendRecoveryConfirmBI(queryVo);
			//message = queryVo.getAccountsNo();
			if(StringUtils.isBlank(message)){
				message = "??????????????????";
			}
			ar.setStatus(HttpStatus.SC_OK);
			ar.setData(message);
			return ar;
		}catch(Exception e){
			logger.error("????????????", e);
			throw new IllegalStateException(e.getMessage());
		}
		
		
	}
	
	/**
	 * ??????????????????
	 */
	@RequestMapping("/recoveryBackQueryList.do")
	public ModelAndView recoveryBackQuery() {
		String comCode=WebUserUtils.getComCode();
		ModelAndView modelAndView = new ModelAndView();
		if(comCode.startsWith("22")){
			modelAndView.setViewName("subrogation/RecoveryBackListSH");
		}else{
			modelAndView.setViewName("subrogation/RecoveryBackList");
		}
		
		
		return modelAndView;
	}
	
	/**
	 * ??????????????????
	 * @modified:
	 * ???YangKun(2016???3???31??? ??????8:02:14): <br>
	 */
	@RequestMapping("/recoverRetrunSearch.do")
	@ResponseBody
	public String recoverRetrunSearch(String registNo, String recoveryCode){
		List<PrpLPlatLockVo> lockList =	platLockDubboService.findPlatLockList(registNo,recoveryCode);
		
		List<RecoveryReturnConfirmVo> confirmList = new ArrayList<RecoveryReturnConfirmVo>();
		//??????times?????????????????????
		for(PrpLPlatLockVo lockVo : lockList ){
			if("1".equals(lockVo.getRecoveryOrPayFlag()) && !"9".equals(lockVo.getRecoveryCodeStatus())){
				List<PrpLRecoveryOrPayVo> recoveryList = lockVo.getPrpLRecoveryOrPays();
//				java.util.Collections.sort(recoveryList, new Comparator<PrpLRecoveryOrPayVo>() {
//					@Override
//					public int compare(PrpLRecoveryOrPayVo o1, PrpLRecoveryOrPayVo o2) {
//						return o1.getTimes().compareTo(o2.getTimes());
//					}
//				});
				
				PrpLRecoveryOrPayVo recoveryVo = recoveryList.get(0);
				RecoveryReturnConfirmVo confirmVo = new RecoveryReturnConfirmVo();
				PrpLRegistVo registVo = registQueryService.findByRegistNo(recoveryVo.getRegistNo());
				confirmVo.setRegistNo(recoveryVo.getRegistNo());
				confirmVo.setPolicyNo(lockVo.getPolicyNo());
				if(StringUtils.isNotBlank(lockVo.getOppoentInsurerCode()) && lockVo.getOppoentInsurerCode().endsWith("01")){
					 
					confirmVo.setInsuredCode(lockVo.getOppoentInsurerCode().substring(0,lockVo.getOppoentInsurerCode().length()-2));
					
				 }else if(StringUtils.isNotBlank(lockVo.getOppoentInsurerCode())){
					 confirmVo.setInsuredCode(lockVo.getOppoentInsurerCode());
				 }else{
					 
				 }  
				confirmVo.setLicenseNo(lockVo.getLicenseNo());
				confirmVo.setRecoveryCode(recoveryVo.getRecoveryCode());
				confirmVo.setReportTime(registVo.getReportTime());//recoveryCodeStatus RecoveryCodeStatus codeTranService
				if(StringUtils.isNotBlank(lockVo.getRecoveryCodeStatus())){
					confirmVo.setRecoveryCodeStatus(codeTranService.transCode("RecoveryCodeStatus",lockVo.getRecoveryCodeStatus()));
				}
				
				confirmList.add(confirmVo);
			}
		}
		
		ResultPage<RecoveryReturnConfirmVo> resultPage = new ResultPage<RecoveryReturnConfirmVo>(0,100,confirmList.size(),confirmList);

		String jsonData = ResponseUtils.toDataTableJson(resultPage,"registNo","policyNo","reportTime","insuredCode:DWInsurerCode","licenseNo","recoveryCode","recoveryCodeStatus");
		logger.debug("forReturnSeach.jsonData="+jsonData);
		
		return jsonData;
	}
	
	/**
	 * ?????????????????? 
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ???YangKun(2016???3???31??? ??????9:11:29): <br>
	 */
	@RequestMapping("/recoveryData.do")
	public ModelAndView recoveryData(String registNo,String recoveryCode) throws Exception {
		String comCode=WebUserUtils.getComCode();
		ModelAndView mv = new ModelAndView();
		CopyInformationSubrogationViewVo viewVo=new CopyInformationSubrogationViewVo();
		PrpLPlatLockVo platLockVo=new PrpLPlatLockVo();
		BigDecimal compensateAmount=new BigDecimal(0);////??????????????????	
		if(comCode.startsWith("22")){//yzy
		    viewVo=backPlatMessage(recoveryCode,registNo);
			 
		  }else{
			platLockVo = subrogationHandleService.sendBeforeRevoeryData(registNo,recoveryCode);
		}
		 if(comCode.startsWith("22")){
			 
			 if(viewVo!=null){
				 platLockVo.setRegistNo(viewVo.getClaimNotificationNo());
				 platLockVo.setPolicyNo(viewVo.getPolicyNo());
				 platLockVo.setOppoentRegistNo(viewVo.getBerecoveryClaimNotificationNo());
				 platLockVo.setOppoentPolicyNo(viewVo.getBerecoveryPolicyNo());
				 
				 if(StringUtils.isNotBlank(viewVo.getBerecoveryInsurerCode()) && viewVo.getBerecoveryInsurerCode().endsWith("01")){
					 
					 platLockVo.setOppoentInsurerCode(viewVo.getBerecoveryInsurerCode().substring(0,viewVo.getBerecoveryInsurerCode().length()-2 ));
					
				 }else if(StringUtils.isNotBlank(viewVo.getBerecoveryInsurerCode())){
					 platLockVo.setOppoentInsurerCode(viewVo.getBerecoveryInsurerCode());
				 }else{
					 
				 }
				 platLockVo.setOppoentInsurerArea(viewVo.getBerecoveryInsurerArea());
				 platLockVo.setRecoveryCode(viewVo.getRecoveryCode());
				 if(StringUtils.isNotBlank(viewVo.getRecoveryCodeStatus())){
					 platLockVo.setRecoveryCodeStatus(codeTranService.transCode("RecoveryCodeStatus",viewVo.getRecoveryCodeStatus()));
					}
				 PrpLPlatLockVo prpLPlatLockVo1 =handleService.findPlatLockByRecoveryCode(recoveryCode);//?????????????????????????????????
				 if(prpLPlatLockVo1!=null){
					  if(StringUtils.isNotBlank(viewVo.getRecoverAmount())){
							 platLockVo.setSumRealAmount(new BigDecimal(viewVo.getRecoverAmount()));
						 }else{
							 platLockVo.setSumRealAmount(new BigDecimal(0));
						 }
					  
					  if(prpLPlatLockVo1.getSumRealAmount()!=null){
						  compensateAmount=prpLPlatLockVo1.getSumRealAmount();//??????????????????	
					  }
				   
					
				 }
				 if(StringUtils.isNotBlank(viewVo.getCompensateAmount())){
					 platLockVo.setSumRecoveryAmount(new BigDecimal(viewVo.getCompensateAmount()));
				 }else{
					 platLockVo.setSumRecoveryAmount(new BigDecimal(0));
				 }
				 
			}
			 mv.addObject("platLockVo",platLockVo); 
			 mv.addObject("compensateAmount",compensateAmount);
			 mv.setViewName("subrogation/RecoveryReturnDataSH");
		 }else{
			 mv.addObject("platLockVo",platLockVo);
			 mv.setViewName("subrogation/RecoveryReturnData");
		 }
		
		return mv;
	}
	
	@RequestMapping("/sendRecoveryData.do")
	@ResponseBody
	public AjaxResult sendRecoveryData(String registNo,String recoveryCode) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		subrogationHandleService.recoveryConfirm(registNo,recoveryCode,null,userVo);
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData("???????????????????????????");
		return ajaxResult;
	}
	/**
	 * ??????????????????????????????
	 * @param registNo
	 * @param recoveryCode
	 * @return
	 */
	@RequestMapping("/sendRecoveryDataSH.do")
	@ResponseBody
	public AjaxResult sendRecoveryDataSH(String registNo,String recoveryCode)throws Exception{
		AjaxResult ajax = new AjaxResult();
		SysUserVo userVo = WebUserUtils.getUser();
		/*try{*/
			subrogationHandleService.recoveryConfirm(registNo,recoveryCode,null,userVo);
			ajax.setStatus(HttpStatus.SC_OK);
		/*}catch(Exception e){
			e.printStackTrace();
		}*/
		
		
		return ajax;
	}
	
	/**
	 * ?????????????????????
	 */
	@RequestMapping("/claimRecoveryQueryList.do")
	public ModelAndView claimRecoveryQuery() {
		String comCode=WebUserUtils.getComCode();
		ModelAndView modelAndView = new ModelAndView();
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -15);
		modelAndView.addObject("lockedTimeStart",startDate);
		modelAndView.addObject("lockedTimeEnd",endDate);
		if(comCode.startsWith("22")){
			modelAndView.setViewName("subrogation/ClaimRecoveryListSH");
		}else{
			modelAndView.setViewName("subrogation/ClaimRecoveryList");
		}
		
		return modelAndView;
	}
	
	/**
	 * ?????????????????????
	 * @return
	 * @modified:
	 * ???YangKun(2016???4???9??? ??????6:31:34): <br>
	 */
	@RequestMapping("/amountSearch")
	@ResponseBody
	public String amountConfirmSearch(@FormModel("subrogationQueryVo")SubrogationQueryVo queryVo) throws Exception{
		
		String comCode = WebUserUtils.getComCode();
		
		List<AccountsInfoVo> accountsInfoList = new ArrayList<AccountsInfoVo>();
		String jsonData="";
		if (comCode.startsWith("22")) {//??????????????????
			ResultPage<PrpLPlatLockVo> page = subrogationSHHandleService.claimDWrecoveryQuery(queryVo);
		    jsonData = ResponseUtils.toDataTableJson(page,"recoveryCode","recoveryCodeStatus","lossTime","oppoentInsurerCode","oppoentInsurerArea",
						"coverageType","sumRealAmount");
				logger.info("forAmountConfirmSeach.jsonData="+jsonData);
		} else {
			accountsInfoList = subrogationToPlatService.sendAccountSearch(comCode,queryVo);
			ResultPage<AccountsInfoVo> resultPage = new ResultPage<AccountsInfoVo>(0,100,accountsInfoList.size(),accountsInfoList);

		   jsonData = ResponseUtils.toDataTableJson(resultPage,"accountsNo","accountsNoStatus:RecoveryCodeStatus","accountsStartDate","recoverCompanyCode:DWInsurerCode","recoverAreaCode:DWInsurerArea",
					"compensateComCode:DWInsurerCode","compensationAreaCode:DWInsurerArea","coverageCode:DWCoverageType","recoverAmount","compensateAmount","accountAmount","lastCompensateAmount");
			logger.info("forAmountConfirmSeach.jsonData="+jsonData);
		}
		
		
		
		return jsonData;
	}
	
	/**
	 * ?????????????????????
	 * @param accountsNo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ???YangKun(2016???4???11??? ??????3:55:43): <br>
	 */
	@RequestMapping("/showQsConfirmInfo.do")
	public ModelAndView showQsConfirmInfo(String accountsNo) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		String comCode = WebUserUtils.getComCode();
//		AccountsQueryReqBodyVo bodyVo = new AccountsQueryReqBodyVo();
//		AccountsQueryBaseVo baseVo = new AccountsQueryBaseVo();
//		baseVo.setAccountsNo(accountsNo);
//		bodyVo.setAccountsQueryBaseVo(baseVo);
		List<AccountsInfoVo> accountsInfoList = new ArrayList<AccountsInfoVo>();
		AccountsInfoVo accountsInfovo=new AccountsInfoVo();
		if (comCode.startsWith("22")) {//????????????
			accountsInfovo= showAmoutview(accountsNo);//
		} else {
			accountsInfoList = subrogationToPlatService.sendAccountQuery(comCode,accountsNo);
		}
		if(comCode.startsWith("22")){
			mv.addObject("accountsInfoVo",accountsInfovo);
		}else{
			mv.addObject("accountsInfoVo",accountsInfoList.get(0));
		}
		
		if(comCode.startsWith("22")){
			mv.setViewName("subrogation/RecoveryQsConfirmSH");
		}else{
			mv.setViewName("subrogation/RecoveryQsConfirm");
		}
		
		
		return mv;
	}
	
	/**
	 * ????????????
	 * @return
	 * @modified:
	 * ???YangKun(2016???4???11??? ??????3:57:56): <br>
	 */
	@RequestMapping("/qsConfirm.do")
	@ResponseBody
	public AjaxResult qsConfirm(String recoveryCode,Double realAmount) {
		AjaxResult ajaxResult = new AjaxResult();
		
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			subrogationHandleService.qsConfirm(recoveryCode,realAmount,userVo);
			
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(recoveryCode);
		}catch(Exception e){
			logger.error("????????????", e);
			ajaxResult.setStatusText(e.getMessage());
//			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	/**
	 * ??????????????????????????????????????????
	 * @param recoveryCode
	 * @return
	 */
	@RequestMapping("/isShowView.ajax")
	@ResponseBody
	public AjaxResult isShowView(String recoveryCode){
		AjaxResult ajax=new AjaxResult();
		String sign="0";
		CiClaimPlatformLogVo biciLog=null;
		PrpLPlatLockVo prpLPlatLockVo =handleService.findPlatLockByRecoveryCode(recoveryCode);
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		CopyInformationSubrogationViewVo copyInformationSubrogationViewVo=new CopyInformationSubrogationViewVo();
		SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
		String comCode = WebUserUtils.getComCode();
		if(prpLPlatLockVo!=null){
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLPlatLockVo.getRegistNo());
			if (cMainVoList != null && !cMainVoList.isEmpty()) {
				comCode = cMainVoList.get(0).getComCode();
			}
		}
		
		if(prpLPlatLockVo!=null){
			if("1101".equals(prpLPlatLockVo.getRiskCode())){
				//??????????????????
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}else{
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}
			if(biciLog!=null){
				queryVo.setRegistNo(prpLPlatLockVo.getRegistNo());
				queryVo.setClaimSeqNo(biciLog.getClaimSeqNo());
				queryVo.setComCode(comCode);
				try {
					resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
					if(resultVoList!=null && resultVoList.size()>0){
						for(CopyInformationResultVo vo:resultVoList){
							if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode())){
								if(vo.getSubrogationViewVo().getRecoveryCode().equals(prpLPlatLockVo.getRecoveryCode())){
									copyInformationSubrogationViewVo=vo.getSubrogationViewVo();
									if("9".equals(copyInformationSubrogationViewVo.getRecoveryCodeStatus())){
										sign="1";
									}
									
								}
							}
						}
					}
					
					ajax.setStatus(HttpStatus.SC_OK);
					ajax.setData(sign);
				} catch (Exception e) {
					ajax.setStatusText(e.getMessage());
					e.printStackTrace();
				}
				
			}
			
		}
		
		
		return ajax;
	}
	/**
	 * ??????????????????????????????
	 * @param recoveryCode
	 * @return
	 * @throws Exception
	 */
	private AccountsInfoVo showAmoutview(String recoveryCode) throws Exception{
		CiClaimPlatformLogVo biciLog=null;
		PrpLPlatLockVo prpLPlatLockVo =handleService.findPlatLockByRecoveryCode(recoveryCode);
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		CopyInformationSubrogationViewVo copyInformationSubrogationViewVo=null;
		SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
		String comCode = WebUserUtils.getComCode();
		if(prpLPlatLockVo!=null){
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLPlatLockVo.getRegistNo());
			if (cMainVoList != null && !cMainVoList.isEmpty()) {
				comCode = cMainVoList.get(0).getComCode();
			}
		}
		
		if(prpLPlatLockVo!=null){
			if("1101".equals(prpLPlatLockVo.getRiskCode())){
				//??????????????????
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}else{
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}
			if(biciLog!=null){
				queryVo.setRegistNo(prpLPlatLockVo.getRegistNo());
				queryVo.setClaimSeqNo(biciLog.getClaimSeqNo());
				queryVo.setComCode(comCode);
				
					resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
					if(resultVoList!=null && resultVoList.size()>0){
						for(CopyInformationResultVo vo:resultVoList){
							if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode())){
								if(vo.getSubrogationViewVo().getRecoveryCode().equals(prpLPlatLockVo.getRecoveryCode())){
									copyInformationSubrogationViewVo=vo.getSubrogationViewVo();
									
									
								}
							}
						}
					}
	      }
			
   }
		//?????????????????????
		AccountsInfoVo vo=new AccountsInfoVo();
		if(copyInformationSubrogationViewVo!=null){
			vo.setAccountsNo(copyInformationSubrogationViewVo.getRecoveryCode());
			vo.setAccountsNoStatus(copyInformationSubrogationViewVo.getRecoveryCodeStatus());
			vo.setAccountsStartDate(copyInformationSubrogationViewVo.getRecoveryStartTime());
			vo.setCoverageCode(copyInformationSubrogationViewVo.getCoverageType());
			 if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getInsurerCode()) && copyInformationSubrogationViewVo.getInsurerCode().endsWith("01")){
				 
				 vo.setRecoverCompanyCode(copyInformationSubrogationViewVo.getInsurerCode().substring(0,copyInformationSubrogationViewVo.getInsurerCode().length()-2 ));
				
			 }else if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getInsurerCode())){
				 vo.setRecoverCompanyCode(copyInformationSubrogationViewVo.getInsurerCode());
			 }else{
				 
			 }
			vo.setRecoverAreaCode(copyInformationSubrogationViewVo.getInsurerArea());
			if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getBerecoveryInsurerCode()) && copyInformationSubrogationViewVo.getBerecoveryInsurerCode().endsWith("01")){
				 
				vo.setCompensateComCode(copyInformationSubrogationViewVo.getBerecoveryInsurerCode().substring(0,copyInformationSubrogationViewVo.getBerecoveryInsurerCode().length()-2 ));
				
			 }else if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getBerecoveryInsurerCode())){
				 vo.setCompensateComCode(copyInformationSubrogationViewVo.getBerecoveryInsurerCode());
			 }else{
				 
			 }
			vo.setCompensationAreaCode(copyInformationSubrogationViewVo.getBerecoveryInsurerArea());
			if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getRecoverAmount())){
				vo.setRecoverAmount(Double.valueOf(copyInformationSubrogationViewVo.getRecoverAmount()));
			}
			if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getCompensateAmount())){
				vo.setCompensateAmount(Double.valueOf(copyInformationSubrogationViewVo.getCompensateAmount()));
			}
			if(StringUtils.isNotBlank(copyInformationSubrogationViewVo.getCompensateAmount())){
				vo.setLastCompensateAmount(Double.valueOf(copyInformationSubrogationViewVo.getCompensateAmount()));
			}
		}
		
		
		
		
		return vo;
		}
	 
	/**
	 *??????????????????????????????????????????
	 * @param accountsNo
	 * @return
	 */
	@RequestMapping("/isvalid.ajax")
	@ResponseBody
	public AjaxResult isvalid(String accountsNo){
		AjaxResult ajax=new AjaxResult();
		String sign="0";
    PrpLPlatLockVo platLockVo = platLockDubboService.findPlatLockByRecoveryCode(accountsNo);
		try{
			if(platLockVo == null){
				throw new IllegalArgumentException("????????????"+accountsNo+"??????????????????????????????");
			}
			if(!"1".equals(platLockVo.getRecoverOrPayStatus()) && StringUtils.isNotBlank(platLockVo.getRecoverOrPayStatus())){//??????????????????
				sign="1";
			}
		ajax.setStatus(HttpStatus.SC_OK);
		ajax.setData(sign);
		}catch(Exception e){
			ajax.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		
		return ajax;
	}
	
	/**
	 * ??????????????????????????????--????????????
	 * @param recoveryCode
	 * @param registNo
	 * @return
	 * @throws Exception
	 */
	private CopyInformationSubrogationViewVo backPlatMessage(String recoveryCode,String registNo) throws Exception{
		
		CiClaimPlatformLogVo biciLog=null;
		PrpLPlatLockVo prpLPlatLockVo =handleService.findPlatLockByRecoveryCode(recoveryCode);
		List<CopyInformationResultVo> resultVoList = new ArrayList<CopyInformationResultVo>();
		CopyInformationSubrogationViewVo copyInformationSubrogationViewVo=null;
		SubrogationSHQueryVo queryVo = new SubrogationSHQueryVo();
		String comCode = WebUserUtils.getComCode();
		if(prpLPlatLockVo!=null){
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLPlatLockVo.getRegistNo());
			if (cMainVoList != null && !cMainVoList.isEmpty()) {
				comCode = cMainVoList.get(0).getComCode();
			}
		}
		
		if(prpLPlatLockVo!=null){
			if("1101".equals(prpLPlatLockVo.getRiskCode())){
				//??????????????????
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}else{
				//??????
				biciLog = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(),prpLPlatLockVo.getRegistNo(),comCode);
			}
			if(biciLog!=null){
				queryVo.setRegistNo(prpLPlatLockVo.getRegistNo());
				queryVo.setClaimSeqNo(biciLog.getClaimSeqNo());
				queryVo.setComCode(comCode);
				
					resultVoList = handleService.sendCopyInformationToSubrogationSH(queryVo);
					if(resultVoList!=null && resultVoList.size()>0){
						for(CopyInformationResultVo vo:resultVoList){
							if(StringUtils.isNotBlank(vo.getSubrogationViewVo().getRecoveryCode())){
								if(vo.getSubrogationViewVo().getRecoveryCode().equals(prpLPlatLockVo.getRecoveryCode())){
									copyInformationSubrogationViewVo=vo.getSubrogationViewVo();
									
									
								}
							}
						}
					}
	      }
			
   }
		return copyInformationSubrogationViewVo;
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


