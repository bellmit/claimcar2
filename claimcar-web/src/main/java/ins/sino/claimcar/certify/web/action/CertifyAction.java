package ins.sino.claimcar.certify.web.action;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ins.framework.utils.Beans;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.VeriFlag;
import ins.sino.claimcar.MapVo;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyHandleService;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyCodeVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.certifyupload.vo.ImageFileIndexVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckExtVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.service.LawSiutService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.commom.vo.StringUtils;
import ins.sino.claimcar.common.constants.CertifyTypeCode;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.PlatformReUploadService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistRiskInfoService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;
import ins.sino.claimcar.sunyardimage.service.ImagesDownLoadService;
import ins.sino.claimcar.sunyardimage.vo.response.NodeNumVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResNumRootVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;


@Controller
@RequestMapping("/certify")
public class CertifyAction {

	private static final Logger logger = LoggerFactory.getLogger(CertifyAction.class);

	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private CertifyService certifyService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private CheckTaskService checkTaskService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	
	@Autowired
	private SubrogationService subrogationService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private EndCasePubService endCasePubService;
	
	@Autowired
	private ClaimTaskService claimTaskService;
	
	@Autowired
	private CertifyToPaltformService certifyToPaltformService;
	
	@Autowired
	private CertifyHandleService certifyHandleService;
	
	@Autowired
	private RegistRiskInfoService registRiskInfoService;
	
	@Autowired
	private PropTaskService propTaskService;
	
	@Autowired
	private CompensateTaskService compensateTaskService;
	
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	
	@Autowired
	private PadPayPubService padPayPubService;
	
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	PlatformReUploadService reUploadService;
    @Autowired
	LawSiutService lawsiutService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	CodeTranService codeTranService;
    @Autowired
    SaaUserPowerService saaUserPowerService;
    @Autowired
    CertifyIlogService certifyIlogService;
    @Autowired
    VerifyClaimService verifyClaimService;

	@Autowired
	ScheduleTaskService scheduleTaskService;
	
	@Autowired
	ImagesDownLoadService imagesDownLoadService;
	@RequestMapping("/certifyCollectEdit.do")
	public ModelAndView certifyCollectEdit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/certify/CertifyCollectEdit");
		return modelAndView;
	}

	@RequestMapping("/claimsStatementEdit.do")
	public ModelAndView claimsStatementEdit() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("base/certify/ClaimsStatementEdit");
		return modelAndView;
	}	
	
	/**
	 * ???????????????
	 * @param flowTaskId
	 * @param registNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/init.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initCertifyCollect(String flowTaskId,String registNo,String certifyMakeup) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		String userCode = WebUserUtils.getUserCode();
		// ???????????????
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(new Double(flowTaskId));
		// ??????????????????
		Map<String,Integer> imagesMap = new HashMap<String,Integer>();
		// ????????????
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		String policyNo = prpLRegistVo.getPolicyNo();
		String policyNoLink = prpLRegistVo.getPrpLRegistExt().getPolicyNoLink();

		List<PrpLLawSuitVo> lawSuitVos = lawsiutService.findByRegistNo(registNo);// ?????????????????????
		// ???????????? ????????????
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByUnderWriteFlag(registNo,CodeConstants.VeriFlag.CANCEL,"0");
		// ??????????????????
		PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNo);
		if(prpLCertifyMainVo == null){
			prpLCertifyMainVo = new PrpLCertifyMainVo();
			Date nowDate = new Date();
			prpLCertifyMainVo.setPolicyNo(policyNo);
			if(!StringUtils.isBlank(policyNoLink)){
				prpLCertifyMainVo.setPolicyNoLink(policyNoLink);
			}

			if(StringUtils.isBlank(prpLCertifyMainVo.getCollectFlag())){
				prpLCertifyMainVo.setCollectFlag("0");// ?????????"0"?????????
			}

			if(lossCarMainList!=null && lossCarMainList.size()>0){
				for(PrpLDlossCarMainVo vo :lossCarMainList){
					if("1".equals(vo.getDeflossCarType())){
						prpLCertifyMainVo.setCollectFlag(vo.getDirectFlag());// ?????????????????????-???????????????
						
					}
				}
			}
			
			prpLCertifyMainVo.setRegistNo(registNo);
			prpLCertifyMainVo.setStartTime(nowDate);
			prpLCertifyMainVo.setLawsuitFlag("0");// ????????????

			if(lawSuitVos!=null && lawSuitVos.size()>0){
				prpLCertifyMainVo.setLawsuitFlag("1");// ?????????????????????????????????????????????
			}else{
				prpLCertifyMainVo.setLawsuitFlag("0");// ????????????
			}

			prpLCertifyMainVo.setValidFlag("1");// ??????
			prpLCertifyMainVo.setCreateUser(userCode);
			prpLCertifyMainVo.setCreateTime(nowDate);
			prpLCertifyMainVo.setUpdateUser(userCode);
			prpLCertifyMainVo.setUpdateTime(nowDate);
			prpLCertifyMainVo = certifyService.submitCertify(prpLCertifyMainVo);
		}
		//this.matchUploadCertifyList(prpLCertifyMainVo,imagesMap);
		
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);// ??????
		modelAndView.addObject("subrogationMain",subrogationMainVo);
		// ??????????????????
		List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		List<PrpLCheckDutyVo> checkDutyVoList = new ArrayList<PrpLCheckDutyVo>();
		
		
		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findlossPropMainByUnderWriteFlag(registNo,CodeConstants.VeriFlag.CANCEL,"0");
		List<PrpLDlossPersTraceVo> lossPersTraceList = deflossHandleService.findPersTraceListByRegistNo(registNo);
		if(lossPersTraceList!=null && lossPersTraceList.size()>0){
			for(PrpLDlossPersTraceVo vo:lossPersTraceList){//
				if(vo.getPrpLDlossPersInjured()!=null && StringUtils.isNotBlank(vo.getPrpLDlossPersInjured().getLossItemType())){
					String lossItemName=codeTranService.transCode("LossItemType",vo.getPrpLDlossPersInjured().getLossItemType());
					vo.setRemark(lossItemName);
				}else{
					vo.setPersonName("");
				}
			}
		}
	    List<PrpLDlossPersTraceMainVo> lossPersTraceMainList = deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
		BigDecimal sumVeriLossFee = new BigDecimal("0");
		if(lossCarMainList != null&&lossCarMainList.size() > 0){
            for(PrpLDlossCarMainVo prpLDlossCarMainVo:lossCarMainList){
                if(VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
                    sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLDlossCarMainVo.getSumVeriLossFee()));
                }
            }
        }
		if(lossPropMainList != null&&lossPropMainList.size() > 0){
            for(PrpLdlossPropMainVo prpLdlossPropMainVo:lossPropMainList){
                if(VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
                    sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLdlossPropMainVo.getSumVeriLoss()));
                }
            }
        }
		
        if (lossPersTraceMainList != null&&lossPersTraceMainList.size() > 0) {
            for (PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : lossPersTraceMainList) {
                if(!CodeConstants.AuditStatus.SUBMITCHARGE.equals(prpLDlossPersTraceMainVo.getAuditStatus())
                        &&!CodeConstants.AuditStatus.SUBMITVERIFY.equals(prpLDlossPersTraceMainVo.getAuditStatus())
                        ||"10".equals(prpLDlossPersTraceMainVo.getCaseProcessType())){
					// ??????????????????????????????????????????????????????????????? ????????????????????????????????????
					// ?????????????????????????????? ??????????????????????????????????????????????????? ?????????????????????
                    continue;
                }
                if(CodeConstants.UnderWriteFlag.CANCELFLAG.equals(prpLDlossPersTraceMainVo.getUnderwriteFlag())){
                    continue;
                }
                List<PrpLDlossPersTraceVo> lossPersTraceVos = prpLDlossPersTraceMainVo.getPrpLDlossPersTraces();
                if (lossPersTraceVos != null&&lossPersTraceVos.size() > 0) {
                    for (PrpLDlossPersTraceVo prpLDlossPersTraceVo : lossPersTraceVos) {
                        if (CodeConstants.ValidFlag.INVALID.equals(prpLDlossPersTraceVo.getValidFlag())) {
                            continue;
                        }
                        List<PrpLDlossPersTraceFeeVo> lossPersTraceFeeList = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();
                        if (lossPersTraceFeeList != null&&lossPersTraceFeeList.size() > 0) {
                            for (PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo : lossPersTraceFeeList) {
                                sumVeriLossFee = sumVeriLossFee.add(NullToZero(prpLDlossPersTraceFeeVo.getVeriDefloss()));
                            }                   
                        }                   
                    }
                }
            }
        }
		Map<String,String> subLiNoMap = new HashMap<String,String>();//
		// ????????????
		PrpLCheckDutyVo prpLCheckDutyVo = null;
		for(PrpLCheckDutyVo checkDutyVo:prpLCheckDutyVoList){
			if(checkDutyVo.getSerialNo() == 1){
				prpLCheckDutyVo = checkDutyVo;
			}else{
				PrpLCheckCarVo prpLCheckCarVo = checkTaskService.findCheckCarBySerialNo(checkDutyVo.getRegistNo(),checkDutyVo.getSerialNo());
				if(prpLCheckCarVo!=null&&StringUtils.isBlank(checkDutyVo.getCiPolicyNo())){
					checkDutyVo.setCiPolicyNo(prpLCheckCarVo.getPrpLCheckCarInfo().getCiPolicyNo());
				}
				if(prpLCheckCarVo!=null&&StringUtils.isBlank(checkDutyVo.getCiInsureComCode())){
					checkDutyVo.setCiInsureComCode(prpLCheckCarVo.getPrpLCheckCarInfo().getCiInsureComCode());
				}
				checkDutyVoList.add(checkDutyVo);
			}
			if(checkDutyVo.getSerialNo()!=1){
				subLiNoMap.put(checkDutyVo.getSerialNo().toString()+","+checkDutyVo.getLicenseNo(),checkDutyVo.getLicenseNo());
			}
		}
		
		if(subLiNoMap.isEmpty()){
			subLiNoMap.put("","");
		}

		// ???????????????????????????????????????
		List<PrpLDlossCarMainVo> losscarList=lossCarService.findLossCarMainByRegistNo(registNo);
		if(checkDutyVoList!=null && checkDutyVoList.size()>0){
			for(PrpLCheckDutyVo vo:checkDutyVoList){
				if(losscarList!=null && losscarList.size()>0){
					for(PrpLDlossCarMainVo carVo:losscarList){
						if(StringUtils.isNotBlank(vo.getLicenseNo()) && vo.getLicenseNo().equals(carVo.getLicenseNo())){
							vo.setNoDutyPayFlag(carVo.getIsNodutypayFlag());
							break;
						}
					}
				}
				
			}
		}

		// ??????????????????????????????????????????????????????
		List<PrpLDlossCarInfoVo> lossCarInfoList = lossCarService.findPrpLDlossCarInfoVoListByRegistNo(registNo);// ????????????????????????
		if(lossCarInfoList!=null && lossCarInfoList.size()>0){
			for(PrpLDlossCarInfoVo infoVo:lossCarInfoList){
				if(checkDutyVoList!=null && checkDutyVoList.size()>0){
					for(PrpLCheckDutyVo dutyVo:checkDutyVoList){
						if(StringUtils.isNotBlank(infoVo.getLicenseNo()) && infoVo.getLicenseNo().equals(dutyVo.getLicenseNo())){
							if(StringUtils.isBlank(dutyVo.getCiPolicyNo()) && StringUtils.isBlank(dutyVo.getCiInsureComCode())){
								dutyVo.setCiPolicyNo(infoVo.getCiPolicyNo());
								dutyVo.setCiInsureComCode(infoVo.getCiInsureComCode());
							}
						}
					}
				}
			}
			
		}

		modelAndView.addObject("subLiNoMap",subLiNoMap);
		modelAndView.addObject("prpLCheckDutyVoList",checkDutyVoList);
		modelAndView.addObject("prpLCheckDutyVo",prpLCheckDutyVo);
		if(prpLCertifyMainVo.getLawsuitFlag().equals("0")){// ????????????
			// ??????????????????
			List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
			if(prpLDlossPersTraceMainVoList != null){
				for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
					String caseProcessType = prpLDlossPersTraceMainVo.getCaseProcessType();// 04????????????
					if("04".equals(caseProcessType)){
						prpLCertifyMainVo.setLawsuitFlag("1");
						break;
					}
				}
			}
		}
		
		// ???????????????
		List<PrpLPayCustomVo> prpLPayCustomVoList = managerService.findPayCustomVoByRegistNo(registNo);
		
		if(prpLCertifyMainVo.getAddNotifyTime() == null){
			prpLCertifyMainVo.setAddNotifyTime(new Date());
		}
		
		// ??????????????????
		String reCaseFlag = "0";// ???????????????
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo", registNo);
		queryMap.put("checkStatus","6");// ????????????
		// ????????????????????????????????? ???????????????
		List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
		if(claimNoList != null && claimNoList.size() > 0){
			for(String claimNo:claimNoList){
				PrpLClaimVo prpLClaimVo  = claimTaskService.findClaimVoByClaimNo(claimNo);
				// ???????????????????????????????????????
				if(StringUtils.isBlank(prpLClaimVo.getEndCaserCode()) && prpLClaimVo.getEndCaseTime() == null && StringUtils.isBlank(prpLClaimVo.getCaseNo())){
					reCaseFlag = "1";
					PrpLWfTaskVo scheduleTaskVo = wfTaskHandleService.findEndTask(registNo, null, FlowNode.Sched).get(0);
					modelAndView.addObject("scheduleTaskVo",scheduleTaskVo);
					
					break;
				}
			}
		}
		// ????????????????????????
//		Boolean isLock = false;
//		
		// //????????????
//		modelAndView.addObject("isLock",isLock ? "Y":"N");
		modelAndView.addObject("handlerUser", userCode);
		// ???????????????????????????1-????????????2-????????????3-???????????????????????????
		String caseFlag="";
		List<PrpLClaimVo> claimList=claimTaskService.findClaimListByRegistNo(registNo);
		if(claimList!=null && claimList.size()>0 ){
			if(claimList.size()==1){
				if("1101".equals(claimList.get(0).getRiskCode())){
					caseFlag="2";
				}else{
					caseFlag="1";
				}
			}else{
				caseFlag="3";
			}
		}
		// ????????????
		if(prpLCertifyMainVo!=null && ("1".equals(prpLCertifyMainVo.getIsJQFraud()) || "1".equals(prpLCertifyMainVo.getIsSYFraud())) && StringUtils.isBlank(prpLCertifyMainVo.getNewNotpaycause())){
			prpLCertifyMainVo.setNewNotpaycause("5");
			prpLCertifyMainVo.setOthernotPaycause(prpLCertifyMainVo.getFraudRefuseReason());
		}else if(prpLCertifyMainVo!=null && StringUtils.isBlank(prpLCertifyMainVo.getIsJQFraud()) && StringUtils.isBlank(prpLCertifyMainVo.getIsSYFraud())){
			if(lossCarMainList!=null && lossCarMainList.size()>0){
				for(PrpLDlossCarMainVo vo:lossCarMainList){
					if("1".equals(vo.getDeflossCarType())){
						prpLCertifyMainVo.setIsJQFraud(vo.getIsCInotpayFlag());
						prpLCertifyMainVo.setIsSYFraud(vo.getIsBInotpayFlag());
						prpLCertifyMainVo.setNewNotpaycause(vo.getNotpayCause());
						prpLCertifyMainVo.setOthernotPaycause(vo.getOtherNotpayCause());
					}
				}
			}
		}
				
		
		// ?????????
		List<PrpLClaimDeductVo> deductVos = new ArrayList<PrpLClaimDeductVo>();
		if( !CodeConstants.ReportType.CI.equals(prpLRegistVo.getReportType())){
			List<PrpLClaimDeductVo> claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);
			if(claimDeductVoList!=null&&claimDeductVoList.size()>0){
				Map<String,String> deductCondCode = new HashMap<String,String>();
				for(PrpLClaimDeductVo claimDeductVo:claimDeductVoList){
					String code = claimDeductVo.getDeductCondCode();
					boolean flag = true;
					for(String key:deductCondCode.keySet()){
						if(code.equals(key)){
							flag = false;
						}
					}
					if(flag){
						deductVos.add(claimDeductVo);
					}
					// ??????
					deductCondCode.put(code,code);
				}
			}
		}
		List<PrpLCMainVo> cmainList=policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		String policyType = "";// ???????????????1-????????????2-????????????3-
		for(PrpLCMainVo cMainVo : cmainList){
			if(cmainList.size()==1){
				policyType = Risk.DQZ.equals(cMainVo.getRiskCode())?"1":"2";
			}else{
				policyType = "3";
			}
			
		}
		// ?????????????????????????????????
		List<PrpLCheckExtVo> checkExtVos=checkTaskService.findPrpLcheckExtVoByRegistNo(registNo);
		if(checkExtVos!=null && checkExtVos.size()>0){
			for(PrpLCheckExtVo vo:checkExtVos){
				if(StringUtils.isNotBlank(vo.getCheckExtName())&&vo.getCheckExtName().contains("?????????????????????")){
					if("9".equals(vo.getColumnValue()) || "0".equals(vo.getColumnValue())){
						prpLCertifyMainVo.setDriveLicenceFlag("0");
					}else if("1".equals(vo.getColumnValue())){
						prpLCertifyMainVo.setDriveLicenceFlag("1");
					}
				}else if(StringUtils.isNotBlank(vo.getCheckExtName())&&vo.getCheckExtName().contains("?????????????????????")){
					if("9".equals(vo.getColumnValue()) || "0".equals(vo.getColumnValue())){
						prpLCertifyMainVo.setCarLicenceFlag("0");
					}else if("1".equals(vo.getColumnValue())){
						prpLCertifyMainVo.setCarLicenceFlag("1");
					}
				}
			}
		}
		
		// ???????????????ILOG????????????????????????
        String nodeCode=prpLWfTaskVo.getSubNodeCode();
        String roleFlag="0";
		SysUserVo userVo = WebUserUtils.getUser();
		String grades="";
		SaaUserPowerVo saaUserPowerVo=saaUserPowerService.findUserPower(userVo.getUserCode());		
		grades=FlowNode.valueOf(nodeCode).getRoleCode();
		
		if(saaUserPowerVo!=null&&saaUserPowerVo.getRoleList().size()>0){			
			for(String gradeId:saaUserPowerVo.getRoleList()){
				if(grades.indexOf(gradeId)>-1){
					roleFlag="1";
					break;
				}
			}
		}
		
	    String prpUrl = SpringProperties.getProperty("PRPCAR_IMAGEURL");
	    modelAndView.addObject("prpUrl",prpUrl);
		
		
		modelAndView.addObject("roleFlag",roleFlag);
		
		modelAndView.addObject("policyType",policyType);
		modelAndView.addObject("claimDeductVos",deductVos);
		modelAndView.addObject("caseFlag",caseFlag);
		modelAndView.addObject("lossCarMainList", lossCarMainList);
		modelAndView.addObject("lossPropMainList", lossPropMainList);
		modelAndView.addObject("lossPersTraceList", lossPersTraceList);
		modelAndView.addObject("certifyMakeup",certifyMakeup);// ??????????????????
		modelAndView.addObject("reCaseFlag",reCaseFlag);
		modelAndView.addObject("imagesMap",imagesMap);
		modelAndView.addObject("prpLPayCustomVoList",prpLPayCustomVoList);
		modelAndView.addObject("prpLWfTaskVo",prpLWfTaskVo);
		modelAndView.addObject("prpLCertifyMainVo",prpLCertifyMainVo);
		modelAndView.addObject("reportTime",prpLRegistVo.getReportTime());
		modelAndView.addObject("reportType",prpLRegistVo.getReportType());
		modelAndView.addObject("comCode",prpLRegistVo.getComCode());
	    modelAndView.addObject("sumVeriLossFee", sumVeriLossFee.toString());
		modelAndView.setViewName("certify/certifyEdit");
		return modelAndView;
	}

	/**
	 * ????????????
	 * @param registNo
	 * @param taskId
	 * @return
	 */
	@RequestMapping("/certifyListEdit.do")
	public ModelAndView certifyListEdit(String registNo,String taskId,String certifyMakeup) {
		ModelAndView modelAndView = new ModelAndView();
		
		// ???????????????
		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(new Double(taskId));
		
		// ????????????????????????
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleService.findPrpLScheduleDefLossList(registNo);
		// ????????????????????????
		List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = persTraceDubboService.findPrpLDlossPersTraceVoListByRegistNo(registNo);
		// ?????????????????? ???????????????????????????
		List<PrpLCertifyCodeVo> prpLCertifyCodeVoList = certifyService.findAllPrpLCertifyCodeList();
		// ??????????????????????????????
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("registNo",registNo);
		List<PrpLCertifyItemVo> prpLCertifyItemVoList = certifyService.findPrpLCertifyItemVoList(queryMap);
		// ?????????????????????
		List<PrpLCertifyCodeVo> showCertifyCodeVoList = new ArrayList<PrpLCertifyCodeVo>();
		
		this.createShowCertify(showCertifyCodeVoList,prpLCertifyCodeVoList,prpLScheduleDefLossVoList,prpLDlossPersTraceVoList);
		
		if(prpLCertifyItemVoList != null){
			this.signCertifyIsSelected(prpLWfTaskVo,showCertifyCodeVoList,prpLCertifyItemVoList);
	    }
		// ?????????????????????????????????????????????????????????
		List<PrpLCertifyCodeVo> CertifyCodeVoList = new ArrayList<PrpLCertifyCodeVo>();
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(registNo);
		if(prpLRegistVo!=null){
			if(!"1".equals(prpLRegistVo.getIsQuickCase())){
				if(showCertifyCodeVoList!=null && showCertifyCodeVoList.size()>0){
					for(PrpLCertifyCodeVo certifyCodeVo:showCertifyCodeVoList){
						if(!"C09".equals(certifyCodeVo.getRemark())){
							CertifyCodeVoList.add(certifyCodeVo);
						}
					}
				}
			}else{
				CertifyCodeVoList=showCertifyCodeVoList;
			}
		}
		
	    modelAndView.addObject("prpLWfTaskVo",prpLWfTaskVo);
	    modelAndView.addObject("prpLCertifyCodeVoList",CertifyCodeVoList);
		modelAndView.addObject("registNo",registNo);
		modelAndView.addObject("certifyMakeup",certifyMakeup);
		modelAndView.setViewName("certify/certifyListEdit");
		return modelAndView;
	}
	
	/**
	 * ??????????????????
	 * @param certifyDirects
	 * @param otherCertify
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/saveCertifyList.do")
	@ResponseBody
	public AjaxResult saveCertifyList(String certifyDirects,String otherCertify, String registNo,String subNodeCode,String allCertify,String checkCertify) {
		SysUserVo userVo = WebUserUtils.getUser();
	    Map<String,String> certifyItemMap = new HashMap<String,String>();
		// ???????????????????????????
	    List<Long> deleteCertifyDirect = new ArrayList<Long>();
		// ????????????????????????
	    List<String> deleteCertifyItem = new ArrayList<String>();
	    this.ceateDeleteCertifyList(deleteCertifyDirect,deleteCertifyItem,allCertify,checkCertify);
		// ???????????????????????????
		List<PrpLCertifyDirectVo> allCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
		this.createAllCertifyDirectVoList(allCertifyDirectVoList,subNodeCode,certifyItemMap,certifyDirects,otherCertify,registNo);
		// ?????????????????????????????????
		List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
		// ????????????????????????????????????
		List<PrpLCertifyItemVo> prpLCertifyItemVoList = new ArrayList<PrpLCertifyItemVo>();
		this.createPrpLCertifyItemVoList(registNo,prpLCertifyItemVoList,prpLCertifyDirectVoList,allCertifyDirectVoList,certifyItemMap);
		// ????????????
		List<PrpLCertifyDirectVo> otherCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
		this.createOtherCertifyItemVoList(otherCertifyDirectVoList,registNo,subNodeCode,otherCertify);
		try {
			if(prpLCertifyItemVoList.size() > 0){
		    	certifyService.saveAllPrpLCertifyItem(prpLCertifyItemVoList);
		    }
		    if(prpLCertifyDirectVoList.size() > 0){
		    	certifyService.saveAllPrpLCertifyDirect(prpLCertifyDirectVoList);
		    }
		    if(deleteCertifyDirect.size() > 0){
		    	certifyService.deleteAllCertifyDirect(registNo,deleteCertifyDirect);
		    	if(deleteCertifyItem.size() > 0){
		    		certifyService.deleteAllCertifyItem(registNo,deleteCertifyDirect,deleteCertifyItem);
		    	}
		    }
		    if(otherCertifyDirectVoList.size() > 0){
		    	certifyService.saveOtherPrpLCertifyDirect(otherCertifyDirectVoList,userVo);
		    }
		} catch (Exception e) {
			logger.info(registNo + "?????????????????????", e);
			e.printStackTrace();
		}
		String returnStr ="";
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(returnStr);
		
		return ajaxResult;
	}
	
	/**
	 * ????????????
	 * @param prpLCertifyMainVo
	 * @param prpLCertifyItemVos
	 * @param prpLPayCustomVos
	 * @param submitVo
	 * @param saveType
	 * @param isSubRogation
	 * @return
	 */
	@RequestMapping(value = "/saveCertifyMain.do")
	@ResponseBody
	public AjaxResult saveCertifyMain(@FormModel("prpLCertifyMainVo") PrpLCertifyMainVo prpLCertifyMainVo,
	                                  @FormModel("prpLCertifyItemVos") List<PrpLCertifyItemVo> prpLCertifyItemVos,
	                                  @FormModel("prpLPayCustomVos") List<PrpLPayCustomVo> prpLPayCustomVos,
	                                  @FormModel("submitVo") WfTaskSubmitVo submitVo,
	                                  @FormModel("prpLCheckDutyVo") PrpLCheckDutyVo prpLCheckDutyVo,
	                                  @FormModel("subrogationMain")PrpLSubrogationMainVo subrogationMainVo,
	                                  @FormModel("prpLCheckDutyVoList")List<PrpLCheckDutyVo> prpLCheckDutyVoList,
	                                  @FormModel("lossCarMainList")List<PrpLDlossCarMainVo> prpLDlossCarMainList,
	                                  @FormModel("lossPropMainList")List<PrpLdlossPropMainVo> prpLdlossPropMainList,
	                                  @FormModel("lossPersTraceList")List<PrpLDlossPersTraceVo> prpLDlossPersTraceList,
	                                  @FormModel("claimDeductVo") List<PrpLClaimDeductVo> claimDeductVos,
	                                  
	                                  String saveType,String isSubRogation,String isClaimSelf) {
		boolean directFlag = true;// ??????????????????
		Date nowDate = new Date();
		String userCode = WebUserUtils.getUserCode();
		String registNo = prpLCertifyMainVo.getRegistNo();
		
		for(PrpLCertifyItemVo prpLCertifyItemVo:prpLCertifyItemVos){
			if(!"1".equals(prpLCertifyItemVo.getDirectFlag())){
				directFlag = false;
				break;
			}
		}
		if("submit".equals(saveType)){
			String result = this.validateCerti(directFlag,registNo,subrogationMainVo,prpLCheckDutyVo,prpLCheckDutyVoList);
			if(result != null){
				AjaxResult ajaxResult = new AjaxResult();
				ajaxResult.setStatus(HttpStatus.SC_OK);
				ajaxResult.setData(result);
				return ajaxResult;
			}
		}
		if(directFlag&&"submit".equals(saveType)){// ???????????????????????? ?????????????????????
			prpLCertifyMainVo.setClaimEndTime(nowDate);
		}
		prpLCertifyMainVo.setOperatorCode(userCode);
		prpLCertifyMainVo.setOperatorDate(nowDate);
		prpLCertifyMainVo.setUpdateUser(userCode);
		prpLCertifyMainVo.setUpdateTime(nowDate);
		// ??????????????????
		deflossHandleService.updateLossCarMainInvoiceFee(prpLDlossCarMainList);
		deflossHandleService.updatePersTraceInvoiceFee(prpLDlossPersTraceList);
		propTaskService.updateDLossPropByRegistNo(prpLdlossPropMainList);
		// ?????????????????????????????????????????????????????????
		prpLCertifyMainVo.setCollectFlag("1");
		certifyService.updatePrpLCertifyMainVo(prpLCertifyMainVo);
		certifyService.updatePrpLCertifyItemList(prpLCertifyItemVos);
		// ??????????????????
		/*prpLCheckDutyVo.setUpdateUser(userCode);
		prpLCheckDutyVo.setUpdateTime(nowDate);
		checkTaskService.saveCheckDuty(prpLCheckDutyVo);*/
		prpLCheckDutyVoList.add(prpLCheckDutyVo);
		if(prpLCheckDutyVoList != null && !prpLCheckDutyVoList.isEmpty()){
			for(PrpLCheckDutyVo checkDutyVo:prpLCheckDutyVoList){
				if("4".equals(checkDutyVo.getIndemnityDuty())){// ??????
					checkDutyVo.setCiDutyFlag("0");
				}else{
					checkDutyVo.setCiDutyFlag("1");
				}
				if(StringUtils.isBlank(checkDutyVo.getNoDutyPayFlag())){
					checkDutyVo.setNoDutyPayFlag("0");
				}
				checkDutyVo.setUpdateUser(userCode);
				checkDutyVo.setUpdateTime(nowDate);
			}
			checkTaskService.saveOrUpdateCheckDutyList(prpLCheckDutyVoList);
		}
		
		// ???????????????
		if(claimDeductVos != null && claimDeductVos.size() > 0){
			checkHandleService.updateClaimDeduct(claimDeductVos,registNo);
		}
		
		// ??????????????????
		if( !subrogationMainVo.getSubrogationFlag().equals(isSubRogation)){// ????????????
			subrogationMainVo.setSourceFlag("1");// ???????????????????????????
			if("1".equals(subrogationMainVo.getSubrogationFlag())){
				List<PrpLSubrogationCarVo> prpLSubrogationCarVoList = subrogationMainVo.getPrpLSubrogationCars();
				if(prpLSubrogationCarVoList != null && prpLSubrogationCarVoList.size() > 0){
					for(PrpLSubrogationCarVo prpLSubrogationCarVo:prpLSubrogationCarVoList){
						String serialNoLicenseNo[] = prpLSubrogationCarVo.getLicenseNo().split(",");
						String serialNo = serialNoLicenseNo[0];
						String licenseNo = serialNoLicenseNo[1];
						prpLSubrogationCarVo.setSerialNo(Integer.valueOf(serialNo));
						prpLSubrogationCarVo.setLicenseNo(licenseNo);
					}
				}
				subrogationService.saveSubrogationInfo(subrogationMainVo);

				//????????????????????????????????????????????????????????????????????????
				List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(registNo);
				for(PrpLClaimVo claimVo : claimList){
					if(!subrogationMainVo.getSubrogationFlag().equals(claimVo.getIsSubRogation())){
						claimVo.setIsSubRogation(subrogationMainVo.getSubrogationFlag());
						claimTaskService.claimWirteBack(claimVo);
					}
				}

			}else{
				subrogationService.deleteSubrogationInfo(subrogationMainVo);
			}
			// ??????????????????
			registRiskInfoService.writePrpLRegistRiskInfo(registNo, subrogationMainVo.getSubrogationFlag(), userCode);
		}
		if("save".equals(saveType)){
			wfTaskHandleService.tempSaveTask(submitVo.getFlowTaskId().doubleValue(),prpLCertifyMainVo.getId().toString(),WebUserUtils.getUserCode(),WebUserUtils.getComCode());
		}else{
			boolean isLock =false;

			// ?????????????????? ????????????????????????????????????????????????
			if("1".equals(subrogationMainVo.getSubrogationFlag()) && !userCode.startsWith("22")&& subrogationMainVo.getPrpLSubrogationPersons()==null){

				List<PrpLPlatLockVo> platLockList = subrogationService.findPrpLPlatLockVoByRegistNo(registNo, null);
				if(platLockList !=null && !platLockList.isEmpty()){
					for(PrpLPlatLockVo platLockVo: platLockList){
						if( !"9".equals(platLockVo.getRecoveryCodeStatus())&&"1".equals(platLockVo.getRecoveryOrPayFlag())){// ???????????????9 ??????????????????????????????
							isLock = true;
							break;
						}
					}
				}
				
				if(!isLock){
					AjaxResult ajaxResult = new AjaxResult();
					ajaxResult.setStatus(HttpStatus.SC_OK);
					ajaxResult.setData("?????????????????????????????????????????????????????????????????????");
					return ajaxResult;
				}
			}
			// ?????????,???????????????????????????????????????????????????
			List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(registNo);
			for(PrpLClaimVo claimVo : claimList){
				if(!prpLCheckDutyVo.getIsClaimSelf().equals(claimVo.getCaseFlag())){
					claimVo.setCaseFlag(prpLCheckDutyVo.getIsClaimSelf());;
					claimTaskService.claimWirteBack(claimVo);
				}
			}
			submitVo.setTaskInUser(userCode);
			submitVo.setTaskInKey(registNo);
			submitVo.setAssignCom(submitVo.getComCode());
			wfTaskHandleService.submitCertify(prpLCertifyMainVo, submitVo);
			checkTaskService.saveCheckDutyHis(registNo,"????????????");
			
			// ???????????????
			interfaceAsyncService.certifyToPaltform(registNo,null);
			// ??????ilog???????????????????????????
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
			PrpLConfigValueVo configValueIRuleVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
			boolean NotExistObj = compensateTaskService.adjustNotExistObj(registNo);
			if("1".equals(configValueVo.getConfigValue())&&"0".equals(configValueIRuleVo.getConfigValue())
					&& !NotExistObj&&StringUtils.isBlank(prpLCertifyMainVo.getManCompenCause())){// ???????????????????????????????????????????????????
				//compensateTaskService.autoCompTask(registNo,WebUserUtils.getUser(),submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
				
				// ==============??????????????????
				SysUserVo userVo = WebUserUtils.getUser();
				LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
				
				if("1".equals(ruleResVo.getUnderwriterflag())){// ??????????????????
		            List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
		            if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
		                for(PrpLWfTaskVo taskVo:prpLWfTaskVoList){
		                    PrpLCompensateVo compVo = compensateTaskService.autoCompTask(taskVo,userVo);
		                    Boolean autoVerifyFlag = false;
		                    WfTaskSubmitVo nextVo = compensateTaskService.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo.getTaskId().doubleValue(),taskVo,userVo,autoVerifyFlag,CodeConstants.CommonConst.FALSE);
		                    if(nextVo.getSubmitLevel()==0){
		                        autoVerifyFlag = true;
		                    }
		                    if(autoVerifyFlag){
								// ?????????????????????true????????????????????????????????????
		                        Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,compVo);
								// ??????????????????
		                        verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, compVo,uwNotionMainId);
								// ??????????????????????????????
		                        try{
		                            verifyClaimService.sendCompensateToPayment(uwNotionMainId);
		                        }catch(Exception e){
		                            e.printStackTrace();
		                        }
		                    }
		                    
		                }
		            }
		        }
				// ==============??????????????????
			}
		}
		// ??????????????????????????????
		List<PrpLDlossCarMainVo> carMainVoList= lossCarService.findLossCarMainByRegistNo(registNo);
		if(prpLCheckDutyVoList!=null && prpLCheckDutyVoList.size()>0){
			for(PrpLCheckDutyVo vo:prpLCheckDutyVoList){
				PrpLCheckDutyVo dutyVo=checkTaskService.findprpLCheckDutyById(vo.getId());
				if(carMainVoList!=null && carMainVoList.size()>0){
					for(PrpLDlossCarMainVo carVo:carMainVoList){
						if(StringUtils.isNotBlank(dutyVo.getLicenseNo()) && dutyVo.getLicenseNo().equals(carVo.getLicenseNo())){
							if("1".equals(vo.getNoDutyPayFlag())){
								carVo.setIsNodutypayFlag("1");// ??????????????????
							}else{
								carVo.setIsNodutypayFlag("0");
							}
							break;
						}
					}
				}
				
			}
		}
		if(carMainVoList!=null && carMainVoList.size()>0){
			for(PrpLDlossCarMainVo mainVo:carMainVoList){
				if("1".equals(mainVo.getDeflossCarType())){
					if(prpLCertifyMainVo!=null){
						mainVo.setIsCInotpayFlag(prpLCertifyMainVo.getIsJQFraud());
						mainVo.setIsBInotpayFlag(prpLCertifyMainVo.getIsSYFraud());
						mainVo.setNotpayCause(prpLCertifyMainVo.getNewNotpaycause());
						mainVo.setOtherNotpayCause(prpLCertifyMainVo.getOthernotPaycause());
						mainVo.setDirectFlag("1");
					}
				}
			}
		}
		if(carMainVoList!=null && carMainVoList.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo:carMainVoList){
				lossCarService.updateDlossCarMain(lossCarMainVo);
			}
		}
		String returnStr = "";
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(returnStr);
		
		return ajaxResult;
	}
	
   @RequestMapping(value="/loadSubrationCar.ajax")
	public ModelAndView loadSubrationCar(int size,String registNo){
		ModelAndView mv = new ModelAndView();
		PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
		PrpLSubrogationCarVo carVo = new PrpLSubrogationCarVo();
		carVo.setRegistNo(registNo);
		
		List<PrpLSubrogationCarVo> carList = new ArrayList<PrpLSubrogationCarVo>();
		carList.add(carVo);
		
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		Map<String,String> subLiNoMap = new HashMap<String,String>();
		
		for(PrpLCheckDutyVo checkDuty : checkDutyList){
			if(checkDuty.getSerialNo()!=1){
				subLiNoMap.put(checkDuty.getSerialNo().toString()+","+checkDuty.getLicenseNo(),checkDuty.getLicenseNo());
			}
		}
		
		if(subLiNoMap.isEmpty()){
			subLiNoMap.put("","");
		}
		
		subrogationMain.setPrpLSubrogationCars(carList);
		mv.addObject("subrogationMain",subrogationMain);
		mv.addObject("size",size);
		mv.addObject("subLiNoMap",subLiNoMap);
		
		mv.setViewName("certify/certifyEdit_Subrogation_CarTr");
		
		return mv; 
	}
	
	@RequestMapping(value="/loadSubrationPers.ajax")
	public ModelAndView loadSubrationPers(int size,String registNo){
		ModelAndView mv = new ModelAndView();
		PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
		PrpLSubrogationPersonVo personVo = new PrpLSubrogationPersonVo();
		personVo.setRegistNo(registNo);
		List<PrpLSubrogationPersonVo> personList = new ArrayList<PrpLSubrogationPersonVo>();
		personList.add(personVo);
		
		subrogationMain.setPrpLSubrogationPersons(personList);
		mv.addObject("subrogationMain",subrogationMain);
		mv.addObject("size",size);
		mv.setViewName("certify/certifyEdit_Subrogation_PerTr");
		
		return mv; 
	}
	
	@RequestMapping(value="/loadCertifyItems.ajax")
	public ModelAndView loadCertifyItems(String registNo){
		ModelAndView mv = new ModelAndView();
		Map<String,Integer> imagesMap = new HashMap<String,Integer>();
		// ??????????????????
		PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNo);
		this.matchUploadCertifyList(prpLCertifyMainVo,imagesMap);
		mv.addObject("prpLCertifyMainVo",prpLCertifyMainVo);
		mv.setViewName("certify/certifyItems");
		return mv; 
	}
	
	@RequestMapping(value="/loadPayCustom.ajax")
	public ModelAndView loadPayCustom(String registNo){
		ModelAndView mv = new ModelAndView();
		// ???????????????
		List<PrpLPayCustomVo> prpLPayCustomVoList = managerService.findPayCustomVoByRegistNo(registNo);
		mv.addObject("prpLPayCustomVoList",prpLPayCustomVoList);
		mv.setViewName("certify/certifyPayCustom");
		return mv; 
	}
	
	
	
	
	
	
	/**
	 * ???????????????????????????????????????????????????????????????
	 * @param registNo
	 * @param prpLCertifyDirectVoList
	 * @param allCertifyDirectVoList
	 * @param certifyItem
	 * @return
	 */
	private void createPrpLCertifyItemVoList(String registNo,
			List<PrpLCertifyItemVo> prpLCertifyItemVoList,
			List<PrpLCertifyDirectVo> prpLCertifyDirectVoList,
			List<PrpLCertifyDirectVo> allCertifyDirectVoList,
			Map<String, String> certifyItem) {
		Date nowDate = new Date();
		// ????????????
		List<PrpLCertifyDirectVo> firstCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
		for (String key : certifyItem.keySet()) {
			PrpLCertifyItemVo prpLCertifyItemVo = new PrpLCertifyItemVo();
			prpLCertifyItemVo.setCertifyTypeCode(key);
			prpLCertifyItemVo.setCertifyTypeName(certifyItem.get(key));
			prpLCertifyItemVo.setRegistNo(registNo);
			prpLCertifyItemVo.setDirectFlag("0");// ???????????????
			prpLCertifyItemVo.setValidFlag("1");// ??????
			prpLCertifyItemVo.setCreateTime(nowDate);
			prpLCertifyItemVo.setCreateUser(WebUserUtils.getUserCode());
			prpLCertifyItemVo.setUpdateTime(nowDate);
			prpLCertifyItemVo.setUpdateUser(WebUserUtils.getUserCode());
			List<PrpLCertifyDirectVo> certifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
			for (PrpLCertifyDirectVo prpLCertifyDirectTemp : allCertifyDirectVoList) {
				if (prpLCertifyDirectTemp.getTypeCode().equals(key)) {
					prpLCertifyDirectTemp.setPrpLCertifyItem(prpLCertifyItemVo);
					certifyDirectVoList.add(prpLCertifyDirectTemp);
					firstCertifyDirectVoList.add(prpLCertifyDirectTemp);
				}
			}
			prpLCertifyItemVo.setPrpLCertifyDirects(certifyDirectVoList);
			prpLCertifyItemVoList.add(prpLCertifyItemVo);
		}
		// ??????
		List<PrpLCertifyDirectVo> leftCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>(
				allCertifyDirectVoList);
		if (firstCertifyDirectVoList.size() != 0) {
			for (PrpLCertifyDirectVo certifyDirectTemp : allCertifyDirectVoList) {
				for (PrpLCertifyDirectVo certifyDirect : firstCertifyDirectVoList) {
					if (certifyDirect.getLossItemCode().equals(
							certifyDirectTemp.getLossItemCode())) {
						leftCertifyDirectVoList.remove(certifyDirectTemp);
					}
				}
			}
		}
		for (PrpLCertifyDirectVo certifyDirectTemp : leftCertifyDirectVoList) {
			prpLCertifyDirectVoList.add(certifyDirectTemp);
		}
	}

	/**
	 * ?????????????????????
	 * @param certifyItem
	 * @param certifyDirects
	 * @param otherCertify
	 * @param registNo
	 * @return
	 */
	private void createAllCertifyDirectVoList(
			List<PrpLCertifyDirectVo> allCertifyDirectVoList,
			String subNodeCode, Map<String, String> certifyItem,
			String certifyDirects, String otherCertify, String registNo) {
		Date nowDate = new Date();
		if (StringUtils.isBlank(certifyDirects)) {
			return;
		}
		logger.info("========="+registNo+"????????????????????????"+certifyDirects);
		String certifyList[] = certifyDirects.split(",");
		for (String certify : certifyList) {
			String certifyKinde[] = certify.split("-");
			String certifyTypeCode = certifyKinde[0];// ????????????
			String certifyTypeName = certifyKinde[1];// ????????????
			String upCertifyCode = certifyKinde[2];// ????????????
			String upCertifyName = certifyKinde[3];// ????????????
			String mustUpload = certifyKinde[4];// ????????????
			String validFlag = certifyKinde[5];// ????????????
			
			String certifyLossCode = upCertifyCode + "_" + certifyTypeCode ;// ???????????????

			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("registNo", registNo);
			queryMap.put("certifyTypeCode", upCertifyCode);
			List<PrpLCertifyItemVo> prpLCertifyItemVoList = certifyService
					.findPrpLCertifyItemVoList(queryMap);
			if (prpLCertifyItemVoList == null
					|| prpLCertifyItemVoList.size() == 0) {
				certifyItem.put(upCertifyCode, upCertifyName);
			}

			PrpLCertifyDirectVo prpLCertifyDirectVo = new PrpLCertifyDirectVo();
			prpLCertifyDirectVo.setRegistNo(registNo);
			prpLCertifyDirectVo.setTypeCode(upCertifyCode);
			prpLCertifyDirectVo.setTypeName(upCertifyName);
			prpLCertifyDirectVo.setLossItemCode(certifyLossCode);
			prpLCertifyDirectVo.setLossItemName(certifyTypeName);
			prpLCertifyDirectVo.setCheckNode(subNodeCode);
			prpLCertifyDirectVo.setCheckUser(WebUserUtils.getUserCode());
			prpLCertifyDirectVo.setMustUpload(mustUpload);
			prpLCertifyDirectVo.setValidFlag(validFlag);
			prpLCertifyDirectVo.setProvideInd("0");// ?????????????????????
			prpLCertifyDirectVo.setCreateTime(nowDate);
			prpLCertifyDirectVo.setCreateUser(WebUserUtils.getUserCode());
			prpLCertifyDirectVo.setUpdateTime(nowDate);
			prpLCertifyDirectVo.setUpdateUser(WebUserUtils.getUserCode());
			allCertifyDirectVoList.add(prpLCertifyDirectVo);
		}
	}

	/**
	 * @param showCertifyCodeVoList ???????????????
	 * @param prpLCertifyCodeVoList ?????????????????????
	 * @param prpLCheckCarVoList ??????????????????
	 * @param prpLCheckPersonVoList ??????????????????
	 */
	private void createShowCertify(
			List<PrpLCertifyCodeVo> showCertifyCodeVoList,
			List<PrpLCertifyCodeVo> prpLCertifyCodeVoList,
			List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList,
			List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList) {
		for (PrpLCertifyCodeVo prpLCertifyCodeVo : prpLCertifyCodeVoList) {
			// ?????????????????????????????????
			if (prpLCertifyCodeVo.getCertifyTypeCode().equals(CertifyTypeCode.C05.toString())){// ??????????????????
				if (prpLScheduleDefLossVoList != null
						&& prpLScheduleDefLossVoList.size() > 0) {
					for (PrpLScheduleDefLossVo prpLScheduleDefLossVo : prpLScheduleDefLossVoList) {
						if (CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED.equals(prpLScheduleDefLossVo.getScheduleStatus())
								&& "1".equals(prpLScheduleDefLossVo.getDeflossType())
								&& prpLScheduleDefLossVo.getSerialNo() == 1 &&prpLScheduleDefLossVo.getAddDeflossId()==null){// ??????????????????,?????????
							PrpLCertifyCodeVo prpLCertifyCodeTemp = new PrpLCertifyCodeVo();
							prpLCertifyCodeTemp = Beans.copyDepth().from(prpLCertifyCodeVo).to(PrpLCertifyCodeVo.class);
							String certifyTypeName = prpLCertifyCodeTemp.getCertifyTypeName();
							String licenseNo = prpLScheduleDefLossVo.getLicenseNo();
							prpLCertifyCodeTemp.setCertifyTypeName(certifyTypeName + "("+ licenseNo + ")");
							showCertifyCodeVoList.add(prpLCertifyCodeTemp);
						}
					}
				}
			} else if (prpLCertifyCodeVo.getCertifyTypeCode().equals(CertifyTypeCode.C06.toString())){// ?????????
				if (prpLScheduleDefLossVoList != null
						&& prpLScheduleDefLossVoList.size() > 0) {
					for (PrpLScheduleDefLossVo prpLScheduleDefLossVo : prpLScheduleDefLossVoList) {
						if (CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED
								.equals(prpLScheduleDefLossVo
										.getScheduleStatus())
								&& "1".equals(prpLScheduleDefLossVo
										.getDeflossType())
								&& prpLScheduleDefLossVo.getSerialNo() > 1
&&prpLScheduleDefLossVo
								.getAddDeflossId()==null){// ??????????????????????????????
							PrpLCertifyCodeVo prpLCertifyCodeTemp = new PrpLCertifyCodeVo();
							prpLCertifyCodeTemp = Beans.copyDepth()
									.from(prpLCertifyCodeVo)
									.to(PrpLCertifyCodeVo.class);
							int serialNo = prpLScheduleDefLossVo.getSerialNo();
							String certifyTypeName = prpLCertifyCodeTemp
									.getCertifyTypeName();
							String licenseNo = prpLScheduleDefLossVo
									.getLicenseNo();
							prpLCertifyCodeTemp
									.setCertifyTypeName(certifyTypeName + "("
											+ licenseNo + ")");
							prpLCertifyCodeTemp
									.setCertifyTypeCode(prpLCertifyCodeVo
											.getCertifyTypeCode()
											+ "_"
											+ serialNo);
							showCertifyCodeVoList.add(prpLCertifyCodeTemp);
						}
					}
				}
			} else if (prpLCertifyCodeVo.getCertifyTypeCode().equals(
CertifyTypeCode.C07.toString())){// ??????
				if (prpLDlossPersTraceVoList != null
						&& prpLDlossPersTraceVoList.size() > 0) {
					for (PrpLDlossPersTraceVo prpLDlossPersTraceVo : prpLDlossPersTraceVoList) {
						PrpLCertifyCodeVo prpLCertifyCodeTemp = new PrpLCertifyCodeVo();
						prpLCertifyCodeTemp = Beans.copyDepth()
								.from(prpLCertifyCodeVo)
								.to(PrpLCertifyCodeVo.class);
						String certifyTypeName = prpLCertifyCodeTemp
								.getCertifyTypeName();
						String personName = prpLDlossPersTraceVo.getPrpLDlossPersInjured()
								.getPersonName();
						//???????????????????????????id?????????
						if(prpLDlossPersTraceVo.getPrpLDlossPersInjured().getId() < 0){
							prpLCertifyCodeTemp.setCertifyTypeCode(prpLCertifyCodeVo.getCertifyTypeCode()
									+ "_"
									+ prpLDlossPersTraceVo.getPrpLDlossPersInjured().getId()*(-1));
							 if("-".equals(personName)){
							    	personName = "";
							    }
						}else{
							prpLCertifyCodeTemp.setCertifyTypeCode(prpLCertifyCodeVo.getCertifyTypeCode()
									+ "_"
									+ prpLDlossPersTraceVo.getPrpLDlossPersInjured().getId());
						}
						prpLCertifyCodeTemp.setCertifyTypeName(certifyTypeName
								+ "(" + personName + ")");
						showCertifyCodeVoList.add(prpLCertifyCodeTemp);
					}
				}
			} else {
				PrpLCertifyCodeVo prpLCertifyCodeTemp = new PrpLCertifyCodeVo();
				prpLCertifyCodeTemp = Beans.copyDepth().from(prpLCertifyCodeVo)
						.to(PrpLCertifyCodeVo.class);
				showCertifyCodeVoList.add(prpLCertifyCodeTemp);
			}
		}
		
	}

	/**
	 * ???????????????????????????
	 * @param prpLWfTaskVo
	 * @param showCertifyCodeVoList
	 * @param prpLCertifyItemVoList
	 */
	private void signCertifyIsSelected(PrpLWfTaskVo prpLWfTaskVo,
			List<PrpLCertifyCodeVo> showCertifyCodeVoList,
			List<PrpLCertifyItemVo> prpLCertifyItemVoList) {
		/**
		 * ?????????????????????????????????????????????????????????????????????????????????????????? ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
		 */
		String subNodeCode = prpLWfTaskVo.getSubNodeCode();// ?????????????????????
		String userCode = WebUserUtils.getUserCode();// ???????????????
		String handlerStatus = prpLWfTaskVo.getHandlerStatus();
		// ????????????
		for (PrpLCertifyItemVo prpLCertifyItemVo : prpLCertifyItemVoList) {
			// ???????????????????????????
			for (PrpLCertifyCodeVo prpLCertifyCodeVo : showCertifyCodeVoList) {
				if (prpLCertifyItemVo.getCertifyTypeCode().equals(prpLCertifyCodeVo.getCertifyTypeCode())) {
					// ????????????
					List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = prpLCertifyItemVo.getPrpLCertifyDirects();
					List<PrpLCertifyCodeVo> certifyCodeVoList = prpLCertifyCodeVo.getPrpLCertifyCodeVoList();
					if(certifyCodeVoList.size()==0){// ??????????????????
						if (prpLCertifyDirectVoList.size() > 0) {
							List<PrpLCertifyCodeVo> otherCertifyList = new ArrayList<PrpLCertifyCodeVo>();
							for (PrpLCertifyDirectVo prpLCertifyDirectVo : prpLCertifyDirectVoList) {
								PrpLCertifyCodeVo otherCertify = new PrpLCertifyCodeVo();
								otherCertify.setId(prpLCertifyDirectVo.getId());
								otherCertify.setCertifyTypeCode(prpLCertifyDirectVo.getLossItemCode());
								otherCertify.setCertifyTypeName(prpLCertifyDirectVo.getLossItemName());
								String checkNode = prpLCertifyDirectVo.getCheckNode();
								String checkUser = prpLCertifyDirectVo.getCheckUser();
								if (checkNode.equals(subNodeCode)&& checkUser.equals(userCode)) {
									if (handlerStatus.equals("3")||handlerStatus.equals("9")){// ?????????????????????????????????
										otherCertify.setDisabled("1");// ?????????????????????
									}
								} else {
									otherCertify.setDisabled("1");// ?????????????????????
								}
								otherCertify.setIsSelected("1");
								otherCertify.setUpCertifyCode(prpLCertifyDirectVo.getTypeCode());
								otherCertifyList.add(otherCertify);
							}
							prpLCertifyCodeVo.setPrpLCertifyCodeVoList(otherCertifyList);
						}
					} else {
						for (PrpLCertifyDirectVo certifyDirectVo : prpLCertifyDirectVoList) {
							for (PrpLCertifyCodeVo certifyCodeVo : certifyCodeVoList) {
								if(StringUtils.isNotBlank(certifyDirectVo.getLossItemCode())){
									String itemcode="";
									String [] itemCodeArray=certifyDirectVo.getLossItemCode().split("_");
									if(itemCodeArray.length>1){
										itemcode=itemCodeArray[1];
										if (itemcode.equals(certifyCodeVo.getCertifyTypeCode()) 
												&& certifyDirectVo.getTypeCode().equals(prpLCertifyCodeVo.getCertifyTypeCode())) {
											certifyCodeVo.setIsSelected("1");// ???????????????
											certifyCodeVo.setId(certifyDirectVo.getId());// ????????????
											certifyCodeVo.setUpCertifyCode(prpLCertifyCodeVo.getCertifyTypeCode());
											String checkNode = certifyDirectVo.getCheckNode();
											String checkUser = certifyDirectVo.getCheckUser();
											if (checkNode.equals(subNodeCode)&& checkUser.equals(userCode)) {
												if (handlerStatus.equals("3")||handlerStatus.equals("9")){// ?????????????????????????????????
													certifyCodeVo.setDisabled("1");// ?????????????????????
												}
											} else {
												certifyCodeVo.setDisabled("1");// ?????????????????????
											}
										  break;
									   }
										
									}
								}
							
							}
						}
					}
					break;
				}
			}
		}
		
	}

	/**
	 * ??????????????????????????????
	 * @param deleteCertifyList
	 * @param allId
	 * @param checkId
	 */
	private void ceateDeleteCertifyList(List<Long> deleteCertifyDirect,
			List<String> deleteCertifyItem, String allCertify,
			String checkCertify) {
		if (allCertify == null) {
			return;
		}
		String[] allIdArray = allCertify.split(",");
		List<String> allIdList = Arrays.asList(allIdArray);
		List<String> checkIdList = new ArrayList<String>();
		if (checkCertify != null) {
			String[] checkIdArray = checkCertify.split(",");
			checkIdList = Arrays.asList(checkIdArray);
		}

		for (String str : allIdList) {
			if (!checkIdList.contains(str)) {
				String[] deleteCerty = str.split("-");
				deleteCertifyDirect.add(Long.parseLong(deleteCerty[2]));
				if (!deleteCertifyItem.contains(deleteCerty[1])) {
					deleteCertifyItem.add(deleteCerty[1]);
				}
			}
		}
	}

	/**
	 * ?????????????????????
	 * @param otherCertifyDirectVoList
	 * @param registNo
	 * @param subNodeCode
	 */
	private void createOtherCertifyItemVoList(
			List<PrpLCertifyDirectVo> otherCertifyDirectVoList,
			String registNo, String subNodeCode, String otherCertify) {
		if (otherCertify == null) {
			return;
		}
		long count = certifyService.findMaxItemLossCodeOfOtherDirect(
				CertifyTypeCode.C099.toString(), registNo);
		Date nowDate = new Date();
		String[] otherCertifyArray = otherCertify.split(",");
		for (String certifyName : otherCertifyArray) {
			count++;
			PrpLCertifyDirectVo prpLCertifyDirectVo = new PrpLCertifyDirectVo();
			prpLCertifyDirectVo.setRegistNo(registNo);
			prpLCertifyDirectVo.setTypeCode(CertifyTypeCode.C099.toString());
			prpLCertifyDirectVo.setTypeName("??????");
			prpLCertifyDirectVo.setLossItemCode(CertifyTypeCode.C099.toString()
+String.valueOf(count));// ??????
			prpLCertifyDirectVo.setLossItemName(certifyName);
			prpLCertifyDirectVo.setCheckNode(subNodeCode);
			prpLCertifyDirectVo.setCheckUser(WebUserUtils.getUserCode());
			prpLCertifyDirectVo.setMustUpload("N");
			prpLCertifyDirectVo.setValidFlag("1");
			prpLCertifyDirectVo.setProvideInd("0");// ?????????????????????
			prpLCertifyDirectVo.setCreateTime(nowDate);
			prpLCertifyDirectVo.setCreateUser(WebUserUtils.getUserCode());
			prpLCertifyDirectVo.setUpdateTime(nowDate);
			prpLCertifyDirectVo.setUpdateUser(WebUserUtils.getUserCode());
			otherCertifyDirectVoList.add(prpLCertifyDirectVo);
		}

	}

	/**
	 * ??????????????????
	 * @param directFlag
	 * @param registNo
	 * @param subrogationMainVo
	 * @param prpLCheckDutyVo ????????????????????????
	 * @param prpLCheckDutyVoList ???????????????????????????
	 * @return
	 */
	private String validateCerti(boolean directFlag, String registNo,
			PrpLSubrogationMainVo subrogationMainVo, PrpLCheckDutyVo prpLCheckDutyVo,List<PrpLCheckDutyVo> prpLCheckDutyVoList) {
		if (!directFlag) {
			return "???????????????????????????????????????";
		}
		if("1".equals(subrogationMainVo.getSubrogationFlag())){
			Map<String, String> map = new HashMap<String, String>();
			map.put("registNo", registNo);
			map.put("lossItemCode","C0102");// ?????????????????????????????????
			if (certifyService.findPrpLCertifyDirectVoList(map) == null) {
				return "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
			}
			Map<String, String> mapIn = new HashMap<String, String>();
			mapIn.put("registNo", registNo);
			mapIn.put("lossItemCode", "C0201,C0202,C0203");// ??????????????????????????????????????????????????????????????????????????????????????????
			if (certifyService.findPrpLCertifyDirectVoList(mapIn) == null) {
				return "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
			}
			/*
			//???????????? ????????????
			List<String>  prpLPlatLockLicenseNoList = new ArrayList<String>();//??????????????????????????????
			List<PrpLPlatLockVo> prpLPlatLockVoList = subrogationService
					.findPrpLPlatLockVoByRegistNo(registNo, "1");
			if(prpLPlatLockVoList != null && !prpLPlatLockVoList.isEmpty()){
				for(PrpLPlatLockVo prpLPlatLockVo:prpLPlatLockVoList){
					prpLPlatLockLicenseNoList.add(prpLPlatLockVo.getOppoentLicensePlateNo());
				}
			}
			if(subrogationMainVo.getPrpLSubrogationCars() != null && !subrogationMainVo.getPrpLSubrogationCars().isEmpty()){
				for(PrpLSubrogationCarVo prpLSubrogationCarVo:subrogationMainVo.getPrpLSubrogationCars()){
					String licenseNo = prpLSubrogationCarVo.getLicenseNo();
					if(!prpLPlatLockLicenseNoList.contains(licenseNo)){
						return "???????????????????????????????????????????????????"+licenseNo+"?????????????????????????????????????????????";
					}
				}
			}*/

		}
		
		if(!deflossHandleService.existTargetCar(registNo)){
			return "???????????????????????????????????????????????????????????????";
		}

		if (!propTaskService.isDLossAllPassed(registNo)) {
			return "??????????????????????????????????????????";
		}
		
		// ??????????????????
		if ("1".equals(prpLCheckDutyVo.getIsClaimSelf())) {
			prpLCheckDutyVoList.add(prpLCheckDutyVo);// ?????????????????????
			String returnInfo = isClaimSelf(registNo,prpLCheckDutyVoList);
			if(!StringUtils.isBlank(returnInfo)){
				return returnInfo;
			}
		}

		if (!persTraceDubboService.isDlossPersonAllPassed(registNo)) {
			return "????????????????????????????????????????????????";
		}

		// ??????????????????
		if (!compensateTaskService.isPrepayAllPassed(registNo)) {
			return "??????????????????????????????????????????????????????????????????????????????????????????";
		}

		// ??????????????????
		if (!padPayPubService.isPadPayAllPassed(registNo)) {
			return "??????????????????????????????????????????????????????????????????";
		}
		
		// ????????????????????????????????????????????? ?????????????????????
		if (!compensateTaskService.isCompepayAllPassed(registNo)){
			return "??????????????????????????????????????????????????????????????????????????????????????????";
		}
		
		// ???????????????????????????????????????????????????????????????
		List<PrpLWfTaskVo> cancleTaskList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Cancel.name());
		if(cancleTaskList != null && !cancleTaskList.isEmpty()){
			return "???????????????????????????????????????????????????????????????????????????????????????";
		}
		
		// ?????????????????????????????????????????????????????????????????????????????????
		int length = registNo.length();
		if(length!=21){
			if(!passPlatform(registNo)){
				// return "????????????????????????????????????????????????????????????????????????????????????";
				return "NoPassPlatform";
			}
		}
		return null;
	}
	
	/**
	 * ????????????????????????
	 * @param registNo
	 * @return
	 */
	public Boolean passPlatform(String registNo) {
		List<PrpLCMainVo> prplCMainList = policyViewService.getPolicyAllInfo(registNo);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		if( !CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())&& !CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			for(PrpLCMainVo prplCMain:prplCMainList){
				if(SendPlatformUtil.isMor(prplCMain)){
					String comCode = prplCMain.getComCode();
					String riskCode = prplCMain.getRiskCode();
					// ??????
					if(comCode.startsWith("22")){
						// ??????????????????
						String reqType_regist = Risk.DQZ.equals(riskCode) ? RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH
								.getCode();
						CiClaimPlatformLogVo registLog = reUploadService.findLogByBussNo(reqType_regist,registNo,comCode);

						// ????????????
						String reqType_claim = Risk.DQZ.equals(riskCode) ? RequestType.ClaimInfoCI_SH.getCode() : RequestType.ClaimInfoBI_SH
								.getCode();
						CiClaimPlatformLogVo claimLog = reUploadService.findLogByBussNo(reqType_claim,registNo,comCode);

						// ?????????????????????
						String reqType_loss = Risk.DQZ.equals(riskCode) ? RequestType.LossInfoCI_SH.getCode() : RequestType.LossInfoBI_SH.getCode();
						CiClaimPlatformLogVo lossLog = reUploadService.findLogByBussNo(reqType_loss,registNo,comCode);
						if(registLog==null||claimLog==null||lossLog==null){
							return false;
						}
					}else{

						PrpLClaimVo claimVo = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,prplCMain.getPolicyNo(),null).get(0);

						String reqType_regist = Risk.DQZ.equals(riskCode) ? RequestType.RegistInfoCI.getCode() : RequestType.RegistInfoBI.getCode();
						CiClaimPlatformLogVo registLog = reUploadService.findLogByBussNo(reqType_regist,registNo,comCode);

						String reqType_claim = Risk.DQZ.equals(riskCode) ? RequestType.ClaimCI.getCode() : RequestType.ClaimBI.getCode();
						CiClaimPlatformLogVo claimLog = null;
						if(Risk.DQZ.equals(riskCode)){
							claimLog = reUploadService.findLogByBussNo(reqType_claim,claimVo.getClaimNo(),comCode);
						}else{
							claimLog = reUploadService.findLogByBussNo(reqType_claim,claimVo.getClaimNo(),comCode);
						}

						String reqType_check = Risk.DQZ.equals(riskCode) ? RequestType.CheckCI.getCode() : RequestType.CheckBI.getCode();
						CiClaimPlatformLogVo checkLog = reUploadService.findLogByBussNo(reqType_check,registNo,comCode);

						String reqType_loss = Risk.DQZ.equals(riskCode) ? RequestType.LossInfoCI.getCode() : RequestType.LossInfoBI.getCode();
						CiClaimPlatformLogVo lossLog = reUploadService.findLogByBussNo(reqType_loss,registNo,comCode);
						if(registLog==null||claimLog==null||checkLog==null||lossLog==null){
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
	
	
	
	/**
	 * ??????????????? ????????????????????????????????? 1 ???????????? 2 ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????2000????????? ???????????????????????????????????? ??????0??? 3 ?????????????????? 5 ??????????????????
	 */
	private String isClaimSelf(String registNo,List<PrpLCheckDutyVo> checkDutyList){
		boolean isDQZ = false;// ????????????????????????
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if (cMainVoList != null && cMainVoList.size() > 0) {
			for (PrpLCMainVo cMainVo : cMainVoList) {
				if (Risk.DQZ.equals(cMainVo.getRiskCode())) {
					isDQZ = true;
				}
			}
		}
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(registNo);
		if (!isDQZ) {
			return "??????????????????????????????????????????????????????????????????";
		}
		if (carMainVoList != null && carMainVoList.size() == 1) {
			return "?????????????????????????????????????????????";
		}
		
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService
				.findPersTraceMainVoList(registNo);
		if (prpLDlossPersTraceMainVoList != null) {
			for (PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : prpLDlossPersTraceMainVoList) {
				if (!"10".equals(prpLDlossPersTraceMainVo
.getCaseProcessType())&& !"7".equals(prpLDlossPersTraceMainVo.getUnderwriteFlag())){// ?????????????????????????????????????????????
					return "???????????????????????????????????????????????????";
				}
			}
		} else {
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService
					.findUnAcceptTask(registNo, FlowNode.PLoss.name());
			if (prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0) {
				return "???????????????????????????????????????????????????";
			}
		}
		
		for(PrpLCheckDutyVo checkDutyVo : checkDutyList){
			if("0".equals(checkDutyVo.getIndemnityDuty())||
					"4".equals(checkDutyVo.getIndemnityDuty())){
				return "?????????"+checkDutyVo.getLicenseNo()+" ??????????????????????????????????????????????????????????????????";
			}
		}
		
		Map<Integer,BigDecimal> defMap = new HashMap<Integer,BigDecimal>();
		Map<Integer,String> licenseNoMap = new HashMap<Integer,String>();
		List<PrpLDlossCarMainVo> lossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
			for(PrpLDlossCarMainVo otherLossCar : lossCarMainList){
				BigDecimal otherLoss;
				// ??????????????????????????????????????????????????????????????????
				if(otherLossCar.getSumVeriLossFee()!=null && otherLossCar.getSumVeriRescueFee()!=null){
					otherLoss = NullToZero(otherLossCar.getSumVeriLossFee()).add(NullToZero(otherLossCar.getSumVeriRescueFee()));
				}else{
					otherLoss = NullToZero(otherLossCar.getSumLossFee()).add(NullToZero(otherLossCar.getSumRescueFee()));
				}
				
				if(otherLossCar.getSerialNo()==1){// ?????????
					licenseNoMap.put(otherLossCar.getSerialNo(),otherLossCar.getLicenseNo());
//					defMap.put(otherLossCar.getSerialNo(),otherLoss);
//					continue;
				}
				
				if(defMap.containsKey(otherLossCar.getSerialNo())){
					defMap.put(otherLossCar.getSerialNo(),defMap.get(otherLossCar.getSerialNo()).add(otherLoss));
				}else{
					defMap.put(otherLossCar.getSerialNo(),otherLoss);
					licenseNoMap.put(otherLossCar.getSerialNo(),otherLossCar.getLicenseNo());
				}
			}
		}
		
		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(registNo);
		String otherProp = null;
		if(lossPropMainList!=null && !lossPropMainList.isEmpty()){
			for(PrpLdlossPropMainVo propMainVo : lossPropMainList){
				if(propMainVo.getSerialNo() == 0){
					BigDecimal otherLoss;
					// ??????????????????????????????????????????????????????????????????
					if(propMainVo.getSumVeriLoss()!=null && propMainVo.getVerirescueFee()!=null 
							&& propMainVo.getSumVeriFee()!=null){
						otherLoss = NullToZero(propMainVo.getSumVeriLoss()).add(NullToZero(propMainVo.getVerirescueFee()))
								.add(NullToZero(propMainVo.getSumVeriFee()));
					}else{
						otherLoss = NullToZero(propMainVo.getSumDefloss()).add(NullToZero(propMainVo.getDefRescueFee()))
								.add(NullToZero(propMainVo.getSumLossFee()));
					}
					
					if(otherLoss.compareTo(BigDecimal.ZERO)==1){
						otherProp = propMainVo.getLicense();
						break;
					}
				}
				
				BigDecimal otherLoss;
				if(propMainVo.getSumVeriFee()!=null && propMainVo.getVerirescueFee()!=null){
					otherLoss = NullToZero(propMainVo.getSumVeriFee()).add(NullToZero(propMainVo.getVerirescueFee()));
				}else{
					otherLoss = NullToZero(propMainVo.getSumLossFee()).add(NullToZero(propMainVo.getDefRescueFee()));
				}
				
				if(defMap.containsKey(propMainVo.getSerialNo())){
					defMap.put(propMainVo.getSerialNo(),defMap.get(propMainVo.getSerialNo()).add(otherLoss));
				}else{
					defMap.put(propMainVo.getSerialNo(),otherLoss);
				}
			}
		}
		if(!StringUtils.isBlank(otherProp)){
			return "????????????????????????"+otherProp+",??????????????????????????????";
		}
		
		for (Integer key : defMap.keySet()) {
			if(defMap.get(key).compareTo(new BigDecimal("2000"))==1){
				if(key==1){
					return "????????????????????????????????????2000?????????????????????????????????";
				}else{
					return "?????????"+licenseNoMap.get(key)+"???????????????????????????2000?????????????????????????????????";
				}
				
			}
		}
		return null;
	}
	
	
	/**
	 * ???????????????????????????
	 * @param prpLCertifyMainVo
	 * @param imagesMap
	 */
	private void matchUploadCertifyList(PrpLCertifyMainVo prpLCertifyMainVo,
			Map<String, Integer> imagesMap) {
		//?????????????????????
		SysUserVo userVo = WebUserUtils.getUser();
		String url = SpringProperties.getProperty("YX_QUrl")+"?";
		ResNumRootVo claimVo = imagesDownLoadService.getReqImageNum(userVo, CodeConstants.APPROLE, prpLCertifyMainVo.getRegistNo(), "", url,
				CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
		//List<ImageFileIndexVo> imageFileIndexVoList = certifyHandleService.findImageFileIndexVoList(prpLCertifyMainVo.getRegistNo());
		for (PrpLCertifyItemVo prpLCertifyItemVo : prpLCertifyMainVo
				.getPrpLCertifyItems()) {
			List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = prpLCertifyItemVo
					.getPrpLCertifyDirects();
			int count = 0;
			for (PrpLCertifyDirectVo prpLCertifyDirectVo : prpLCertifyDirectVoList) {
				int imgNumber = 0;
				if(claimVo != null && claimVo.getResReturnDataVo()!=null && claimVo.getResReturnDataVo().getNodeNumVos() != null
						&&claimVo.getResReturnDataVo().getNodeNumVos().size() > 0){
					List<NodeNumVo> nodeNumVos = claimVo.getResReturnDataVo().getNodeNumVos();
					for(NodeNumVo nodeNumVo : nodeNumVos){
						String nodeId = nodeNumVo.getId();
						String[] arrayNodeId = nodeId.split("/");
						String nodeName = nodeNumVo.getName();
						String[] arrayNodeName = nodeName.split("/");
						if (prpLCertifyMainVo.getCustomClaimTime() == null) {
							if ("C0101".equals(arrayNodeId[arrayNodeId.length-1])) {// ????????????????????????
								//prpLCertifyMainVo.setCustomClaimTime(imageFileIndexVo.getCreateTime());
							}
						}
						if (nodeName != null && nodeId != null) {
//							if (nodeName.contains(prpLCertifyDirectVo.getTypeName())
//									&& prpLCertifyDirectVo.getLossItemCode().equals(arrayNodeId[arrayNodeId.length-1])) {
								if (nodeId.contains(prpLCertifyDirectVo.getLossItemCode())) {
								imgNumber = imgNumber + Integer.valueOf(nodeNumVo.getNum());
							}
						}
					}
				}
				/*for (ImageFileIndexVo imageFileIndexVo : imageFileIndexVoList) {
					if (prpLCertifyMainVo.getCustomClaimTime() == null) {
						if("C0101".equals(imageFileIndexVo.getTypePath4())){// ????????????????????????
							prpLCertifyMainVo
									.setCustomClaimTime(imageFileIndexVo
											.getCreateTime());
						}
					}
					if (imageFileIndexVo.getTypeName().contains(
							prpLCertifyDirectVo.getTypeName())
							&& prpLCertifyDirectVo.getLossItemCode().equals(
									imageFileIndexVo.getTypePath4())) {
						imgNumber++;
					}
				}*/
				prpLCertifyDirectVo.setImgNumber(imgNumber);// ??????????????????
				if (imgNumber > 0) {
					count++;
					prpLCertifyDirectVo.setProvideInd("1");// ???????????????
				}
			}
			if(count==prpLCertifyDirectVoList.size()){// ????????????
				prpLCertifyItemVo.setDirectFlag("1");
			}
		}
		if(claimVo != null && claimVo.getResReturnDataVo()!=null && claimVo.getResReturnDataVo().getNodeNumVos() != null
				&&claimVo.getResReturnDataVo().getNodeNumVos().size() > 0){
			List<NodeNumVo> lossNodeNumVos = claimVo.getResReturnDataVo().getNodeNumVos();
			for(NodeNumVo nodeNumVo : lossNodeNumVos){
				String nodeId = nodeNumVo.getId();
				String[] arrayNodeId = nodeId.split("/");
				if(CodeConstants.APPCODEL2.equals(arrayNodeId[0])){
					String typeName =nodeNumVo.getName();
					if (!imagesMap.containsKey(typeName)) {
						imagesMap.put(typeName, Integer.valueOf(nodeNumVo.getNum()));
					} else {
						imagesMap.put(typeName, imagesMap.get(typeName) + Integer.valueOf(nodeNumVo.getNum()));
					}
				}
			}
		}
		
	/*	for (ImageFileIndexVo imageFileIndexVo : imageFileIndexVoList) {
			if("picture".equals(imageFileIndexVo.getTypePath2())){// ???????????????
				String typeName =  imageFileIndexVo.getTypeName();
				if (!imagesMap.containsKey(typeName)) {
					imagesMap.put(typeName, 1);
				} else {
					imagesMap.put(typeName, imagesMap.get(typeName) + 1);
				}
			}
		}*/
	}
	
	
	private BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}

	@RequestMapping(value = "/initNoPassPlatformNodeBeforeCertify.do")
	public ModelAndView initNoPassPlatformNodeBeforeCertify(String registNo) {
		ModelAndView modelAndView = new ModelAndView();
		ArrayList<CiClaimPlatformLogVo> noPassPlatformNodeBeforeCertifyList = new ArrayList<CiClaimPlatformLogVo>();
		List<PrpLCMainVo> prplCMainList = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo prplCMain:prplCMainList){
			if(SendPlatformUtil.isMor(prplCMain)){
				String comCode = prplCMain.getComCode();
				String riskCode = prplCMain.getRiskCode();
				// ??????
				if(comCode.startsWith("22")){
					// ??????????????????
					String reqType_regist = Risk.DQZ.equals(riskCode) ? RequestType.RegistInfoCI_SH.getCode() : RequestType.RegistInfoBI_SH.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_regist);
					// ????????????
					String reqType_claim = Risk.DQZ.equals(riskCode) ? RequestType.ClaimInfoCI_SH.getCode() : RequestType.ClaimInfoBI_SH.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_claim);
					// ?????????????????????
					String reqType_loss = Risk.DQZ.equals(riskCode) ? RequestType.LossInfoCI_SH.getCode() : RequestType.LossInfoBI_SH.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_loss);
				}else{
					PrpLClaimVo claimVo = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,prplCMain.getPolicyNo(),null).get(0);

					String reqType_regist = Risk.DQZ.equals(riskCode) ? RequestType.RegistInfoCI.getCode() : RequestType.RegistInfoBI.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_regist);

					String reqType_claim = Risk.DQZ.equals(riskCode) ? RequestType.ClaimCI.getCode() : RequestType.ClaimBI.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,claimVo.getClaimNo(),comCode,
							reqType_claim);

					String reqType_check = Risk.DQZ.equals(riskCode) ? RequestType.CheckCI.getCode() : RequestType.CheckBI.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_check);

					String reqType_loss = Risk.DQZ.equals(riskCode) ? RequestType.LossInfoCI.getCode() : RequestType.LossInfoBI.getCode();
					noPassPlatformNodeBeforeCertifyList = appendLastestLog(noPassPlatformNodeBeforeCertifyList,registNo,comCode,reqType_loss);
				}
			}
		}
		boolean showUpdateVin = false;
		String platformErrorcodeVinStr = SpringProperties.getProperty(CodeConstants.SysConfigCodeKey.PLATFORM_ERRORCODE_VIN);
		Assert.notNull(platformErrorcodeVinStr,"????????????????????????VIN??????????????????????????????");
		List platformErrorcodeVinList = Arrays.asList(platformErrorcodeVinStr.split(","));
		for(CiClaimPlatformLogVo logVo:noPassPlatformNodeBeforeCertifyList){
			if( !ObjectUtils.isEmpty(logVo.getErrorCode())&&platformErrorcodeVinList.contains(logVo.getErrorCode())&&logVo.getErrorMessage()
					.contains("VIN???")){
				showUpdateVin = true;
				break;
			}
		}
		if(showUpdateVin){
			// ????????????????????????????????????VIN????????????????????????????????????VIN??????????????????
			List<PrpLDlossCarMainVo> resultList = new ArrayList<PrpLDlossCarMainVo>();
			List<PrpLScheduleDefLossVo> scheduleDefLossList = scheduleTaskService.getScheduleDefLossByLossType(registNo,"1");
			if(scheduleDefLossList!=null&&scheduleDefLossList.size()>0){
				for(PrpLScheduleDefLossVo scheduleDefLossVo:scheduleDefLossList){
					PrpLDlossCarMainVo resultVo = new PrpLDlossCarMainVo();
					PrpLCheckCarVo checkCarVo = checkHandleService.findCarIdBySerialNoAndRegistNo(scheduleDefLossVo.getSerialNo(),registNo);
					List<PrpLDlossCarMainVo> lossCarList = lossCarService.findLossCarMainBySerialNo(registNo,scheduleDefLossVo.getSerialNo());
					// ????????????????????????????????????????????????????????????
					if(checkCarVo!=null&&checkCarVo.getCarid()!=null){
						resultVo.setRegistNo(registNo);
						resultVo.setDeflossCarType(scheduleDefLossVo.getSerialNo()==1 ? "?????????" : "?????????");
						resultVo.setLicenseNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
						resultVo.setVinNo(checkCarVo.getPrpLCheckCarInfo().getVinNo());
						resultVo.setSerialNo(scheduleDefLossVo.getSerialNo());
						resultList.add(resultVo);
					}else if(lossCarList!=null&&lossCarList.size()>0){
						PrpLDlossCarInfoVo carInfoVo = lossCarService.findPrpLDlossCarInfoVoById(lossCarList.get(0).getCarId());
						if(carInfoVo!=null){
							resultVo.setRegistNo(registNo);
							resultVo.setDeflossCarType(scheduleDefLossVo.getSerialNo()==1 ? "?????????" : "?????????");
							resultVo.setLicenseNo(carInfoVo.getLicenseNo());
							resultVo.setVinNo(carInfoVo.getVinNo());
							resultVo.setSerialNo(scheduleDefLossVo.getSerialNo());
							resultList.add(resultVo);
						}
					}
				}
			}
			modelAndView.addObject("PrpLDlossCarMainVoList",resultList);
		}
		modelAndView.addObject("showUpdateVin",showUpdateVin);
		modelAndView.addObject("noPassPlatformNodeBeforeCertifyList",noPassPlatformNodeBeforeCertifyList);
		modelAndView.setViewName("certify/noPassPlatformBeforeCertifyEdit");
		return modelAndView;
	}

	private ArrayList<CiClaimPlatformLogVo> appendLastestLog(ArrayList<CiClaimPlatformLogVo> list,String BussNo,String comCode,String reqType) {
		CiClaimPlatformLogVo log = reUploadService.findLastestLogByReqTypeBussNoComCode(reqType,BussNo,comCode);
		if(ObjectUtils.isEmpty(log)){
			log = new CiClaimPlatformLogVo();
			log.setRequestType(reqType);
			for(RequestType reqestType:RequestType.values()){
				if(reqestType.getCode().equals(reqType)){
					String value = reqestType.toString();
					String indexOf = value.substring(value.length()-2,value.length());
					String name = "SH".equals(indexOf) ? "??????"+reqestType.getName() : reqestType.getName();
					log.setRequestName(name);
					break;
				}
			}
			log.setComCode(comCode);
			log.setBussNo(BussNo);
			log.setErrorMessage("?????????????????????");
			list.add(log);
		}else{
			if( !"1".equals(log.getStatus())){
				list.add(log);
			}
		}
		return list;
	}

    @RequestMapping(value="/reqPhotoInfo.do")
    @ResponseBody
	public AjaxResult reqPhotoInfo(String registNo,Long id,String flag){
    	AjaxResult ajax=new AjaxResult();
    	// ??????????????????
    	PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNo);
    	// ??????????????????
    	List<MapVo> mapVos=new ArrayList<MapVo>();
    	List<PrpLCertifyDirectVo> directVos=new ArrayList<PrpLCertifyDirectVo>();
    	Map<String,Integer> imagesMap = new HashMap<String,Integer>();
    	this.matchUploadCertifyList(prpLCertifyMainVo,imagesMap);
    	if(prpLCertifyMainVo !=null){
    		if("1".equals(flag)){//?????????????????????
    			List<PrpLCertifyItemVo> prpLCertifyItemVos=prpLCertifyMainVo.getPrpLCertifyItems();
        		if(prpLCertifyItemVos!=null && prpLCertifyItemVos.size()>0){
        			for(PrpLCertifyItemVo certifyitemVo:prpLCertifyItemVos){
        				if(certifyitemVo.getPrpLCertifyDirects()!=null && certifyitemVo.getPrpLCertifyDirects().size()>0){
        					for(PrpLCertifyDirectVo directVo:certifyitemVo.getPrpLCertifyDirects()){
        						if("Y".equals(directVo.getMustUpload())){
        							directVo.setMustUpload("???");
        						}else{
        							directVo.setMustUpload("???");
        						}
        						if("1".equals(directVo.getProvideInd())){
        							directVo.setProvideInd("???");
        						}else{
        							directVo.setProvideInd("???");
        						}
        						if(directVo.getImgNumber()==null){
        							directVo.setImgNumber(0);
        						}
        					}
        				}
        				if(id!=null && id.toString().equals(certifyitemVo.getId().toString())){
        					if(certifyitemVo.getPrpLCertifyDirects()!=null && certifyitemVo.getPrpLCertifyDirects().size()>0){
        						for(PrpLCertifyDirectVo vo:certifyitemVo.getPrpLCertifyDirects()){
        							PrpLCertifyDirectVo vo1=new PrpLCertifyDirectVo();
        							vo1.setLossItemName(vo.getLossItemName());
        							vo1.setMustUpload(vo.getMustUpload());
        							vo1.setProvideInd(vo.getProvideInd());
        							vo1.setImgNumber(vo.getImgNumber());
        							vo1.setLossItemCode(vo.getLossItemCode());
        							directVos.add(vo1);
        						}
        					}
        					ajax.setData(directVos);;
        					ajax.setStatus(100);
        				}
        			}
        		}
    		}
    		
    		if("2".equals(flag)){//????????????table???,????????????
	        	if(imagesMap!=null && imagesMap.size()>0){
	        		for(String key:imagesMap.keySet()){
	        			MapVo mapVo=new MapVo();
	        			mapVo.setKey(key);
	        			mapVo.setValue(imagesMap.get(key)==null?"":imagesMap.get(key).toString());
	        			mapVos.add(mapVo);
	        			ajax.setData(mapVos);
	        			ajax.setStatus(200);
	        		}
	        	}
    		}
    		
    		
    		
    	}
    	return ajax;
	}
}
