package ins.sino.claimcar.platform.service.spring;

import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.StringOperationUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CetainLossType;
import ins.sino.claimcar.CodeConstants.CheckStatus;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.service.CiClaimPlatformLogService;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformLogVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersExtVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.platform.service.CiCodeTranService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.platform.vo.BiLossBaseVo;
import ins.sino.claimcar.platform.vo.BiLossCarFittingDataVo;
import ins.sino.claimcar.platform.vo.BiLossCarHourDataVo;
import ins.sino.claimcar.platform.vo.BiLossCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.BiLossCarVehicleDataVo;
import ins.sino.claimcar.platform.vo.BiLossPersonDataVo;
import ins.sino.claimcar.platform.vo.BiLossPersonHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.BiLossPersonInjuryDataVo;
import ins.sino.claimcar.platform.vo.BiLossPersonInjuryIdentifyInfoDataVo;
import ins.sino.claimcar.platform.vo.BiLossPersonLossFeeDataVo;
import ins.sino.claimcar.platform.vo.BiLossProtectDataVo;
import ins.sino.claimcar.platform.vo.BiLossReqBodyVo;
import ins.sino.claimcar.platform.vo.BiLossSubrogationDataVo;
import ins.sino.claimcar.platform.vo.CiLossBaseVo;
import ins.sino.claimcar.platform.vo.CiLossCarDataVo;
import ins.sino.claimcar.platform.vo.CiLossCarFittingDataVo;
import ins.sino.claimcar.platform.vo.CiLossCarHourDataVo;
import ins.sino.claimcar.platform.vo.CiLossCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.CiLossPersonDataVo;
import ins.sino.claimcar.platform.vo.CiLossPersonHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.CiLossPersonInjuryDataVo;
import ins.sino.claimcar.platform.vo.CiLossPersonInjuryIdentifyInfoDataVo;
import ins.sino.claimcar.platform.vo.CiLossPersonLossFeeDataVo;
import ins.sino.claimcar.platform.vo.CiLossProtectDataVo;
import ins.sino.claimcar.platform.vo.CiLossReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHBILossReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHBILossReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqActuralRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqBasePartVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqBodyVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqCarLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqFittingDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqHospitalInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqInjuryDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqInjuryIdentifyInfoDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqObjDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqPersonDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqPersonLossPartDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqRepairDataVo;
import ins.sino.claimcar.platform.vo.sh.SHCILossReqVehicleDataVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * ???????????????????????????????????????
 * @author ???Luwei
 * @CreateTime 2016???7???1???
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" }, timeout = 1000000)
@Path("lossToPlatformService")
public class LossToPlatformServiceImpl implements LossToPlatformService {

	private Logger logger = LoggerFactory.getLogger(LossToPlatformServiceImpl.class);
	
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	CiClaimPlatformLogService platformLogService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	PersTraceService traceService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	SubrogationService subrogationService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	RegistQueryService registService;
	@Autowired
	CertifyPubService certifyPubService;
	@Autowired
	DeflossService deflossService;
	@Autowired
    private CiCodeTranService ciCodeTranService;
	@Autowired
	private InterfaceAsyncService interfaceAsyncService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	EndCasePubService endCasePubService;
	
	/**
	 * 
	 * <pre>??????????????????</pre>
	 * @param registNo
	 * @modified:
	 * ???Luwei(2016???9???23??? ??????8:31:11): <br>
	 */
	@Override
	public void sendLossToPlatform(String registNo,String riskCode) {
		// ?????????????????????
		try{
			String comCode = policyViewService.getPolicyComCode(registNo);
			if(StringUtils.isNotBlank(comCode)){
				if(comCode.startsWith("22")){
					sendLossToPlatFormSH(registNo,riskCode);// ????????????
				}else{
					lossToPaltform(registNo,riskCode);
				}
			}
		}catch(Exception e){
			logger.error("?????????=" + registNo + "???????????????????????????" , e);
		}
	}
	
	/**
	 * ???????????????
	 * @param CiClaimPlatformLogVo
	 * @throws Exception 
	 */
	@Override
	public void uploadLossToPaltform(CiClaimPlatformLogVo logVo) throws Exception{
		String bussNo = logVo.getBussNo();
		String requestType = logVo.getRequestType();
		String comCode = logVo.getComCode();
		if ("22".equals(comCode.substring(0, 2))) {// ????????????
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(bussNo);
			for(PrpLCMainVo cMainVo:cMainVoList){
				if(Risk.DQZ.equals(cMainVo.getRiskCode())&&RequestType.LossInfoCI_SH.getCode().equals(requestType)){
					CiClaimPlatformLogVo log_ci = platformLogService.findLogByBussNo(RequestType.RegistInfoCI_SH.getCode(), bussNo, comCode);
					if(log_ci != null){
						sendLossToSHPlatform(log_ci.getClaimSeqNo(), comCode, cMainVo);
					}
				}
				if(!Risk.DQZ.equals(cMainVo.getRiskCode())&&RequestType.LossInfoBI_SH.getCode().equals(requestType)){
					CiClaimPlatformLogVo log_bi = platformLogService.findLogByBussNo(RequestType.RegistInfoBI_SH.getCode(), bussNo, comCode);
					if(log_bi != null){
						sendLossToSHPlatform(log_bi.getClaimSeqNo(), comCode, cMainVo);
					}
				}
			}
//			sendLossToPlatFormSH(bussNo);
		}else{
//			lossToPaltform(bussNo);
			/* ????????????????????????????????? */
			List<PrpLdlossPropMainVo> dlossPropMainVoList = propTaskService.findPropMainListByRegistNo(bussNo);

			List<PrpLDlossCarMainVo> dlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(bussNo);

			List<PrpLDlossPersTraceMainVo> dlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(bussNo);
			if(dlossPersTraceMainVoList != null && dlossPersTraceMainVoList.size() > 0){
				for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : dlossPersTraceMainVoList){
					List<PrpLDlossPersTraceVo> traceVoList = persTraceService.findPersTraceVo(bussNo,prpLDlossPersTraceMainVo.getId());
					prpLDlossPersTraceMainVo.setPrpLDlossPersTraces(traceVoList);
				}
			}
			List<PrpLDlossCarMainVo> newVoList = getDlossCarMainList(dlossCarMainVoList);
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(bussNo);
			for(PrpLCMainVo cMainVo:cMainVoList){
				if(Risk.DQZ.equals(cMainVo.getRiskCode())&&RequestType.LossInfoCI.getCode().equals(requestType)){
					sendLossToCIPlatForm(bussNo,newVoList,dlossPropMainVoList,dlossPersTraceMainVoList);
				}
				if(!Risk.DQZ.equals(cMainVo.getRiskCode())&&RequestType.LossInfoBI.getCode().equals(requestType)){
					sendLossToBIPlatForm(bussNo,newVoList,dlossPropMainVoList,dlossPersTraceMainVoList);
				}
			}
		}
	}
	
	//------------------------------????????????--------------------------------------//
	
	public void lossToPaltform(String registNo,String riskCode) throws Exception {

		/* ????????????????????????????????? */
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);

		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);

		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
		if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size() > 0){
			for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo : prpLDlossPersTraceMainVoList){
				List<PrpLDlossPersTraceVo> traceVoList = persTraceService.findPersTraceVo(registNo,prpLDlossPersTraceMainVo.getId());
				prpLDlossPersTraceMainVo.setPrpLDlossPersTraces(traceVoList);
			}
		}

	    boolean vLoss = true;
	    if(prpLDlossCarMainVoList != null){
		    for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
			    if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
				   vLoss = false;
			    }
		    }
	    }
	    if(prpLdlossPropMainVoList != null){
		    for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
			    if(!CodeConstants.VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
			 	   vLoss = false;
			    }
		    } 
	    }
	    if(vLoss){//??????????????????????????????   ????????????
		    List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),FlowNode.DLProp.name(),FlowNode.DLCar.name());
		    if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
			    for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
				    if(!prpLWfTaskVo.getSubNodeCode().equals(FlowNode.DLChk.name())){
					    vLoss =  false;
					    break;
				    }
			    }
		    }
	    }
		
		/* ?????????????????? */
		boolean pLoss = true;
		if(prpLDlossPersTraceMainVoList!=null){
			for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
				if( !CodeConstants.AuditStatus.SUBMITCHARGE.equals(prpLDlossPersTraceMainVo.getAuditStatus()) &&
				        !CodeConstants.UnderWriteFlag.CANCELFLAG.equals(prpLDlossPersTraceMainVo.getUnderwriteFlag())){
					pLoss = false;
				}
			}
		}
		
		if(pLoss) /* ??????????????????????????????????????? */
		{
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.PLoss.name());
			if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
				pLoss = false;
			}
		}

		if(vLoss&&pLoss) /* ??????????????????????????????????????????????????????????????? */
		{
			List<PrpLDlossCarMainVo> newVoList = getDlossCarMainList(prpLDlossCarMainVoList);
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);


			int flag = 0;
			List<PrpLClaimVo> claimVos = claimTaskService.findClaimListByRegistNo(registNo,ValidFlag.VALID);
			Map<String,PrpLClaimVo> claimVoMap = new HashMap<String,PrpLClaimVo>();
			if(claimVos != null && claimVos.size() >0){
				for(PrpLClaimVo claimVo:claimVos){
					claimVoMap.put(claimVo.getRiskCode(), claimVo);
				}	
			}
			Map<String,String> queryMap = new HashMap<String,String>();
			queryMap.put("registNo", registNo);
			queryMap.put("checkStatus",CheckStatus.Pass);// ????????????
			// ?????????????????????????????????
			List<PrpLReCaseVo> prpLReCaseVoList = endCasePubService.findReCaseVoListByqueryMap(queryMap);
			if(prpLReCaseVoList!=null&&prpLReCaseVoList.size()>0){// ??????
				flag = 1;
			}
			for(PrpLCMainVo cMainVo:cMainVoList){
				int value = 0;
				if(riskCode!=null&&!riskCode.equals(cMainVo.getRiskCode())){
					// ??????????????????????????????????????????????????????
					continue;
				}
				PrpLClaimVo claimVo = claimVoMap.get(cMainVo.getRiskCode());
				for(PrpLReCaseVo prpLReCaseVo: prpLReCaseVoList){//????????????????????????????????????
					if(claimVo.getEndCaseTime()==null && claimVo != null 
							&& StringUtils.isNotBlank(claimVo.getClaimNo())
							&& claimVo.getClaimNo().equals(prpLReCaseVo.getClaimNo())){
						value = 1; 
						break;
					}
				}
				if(flag == 0 || (value == 1 && flag == 1)){
					if(Risk.DQZ.equals(cMainVo.getRiskCode())){
						if(SendPlatformUtil.isMor(cMainVo)){
								sendLossToCIPlatForm(registNo,newVoList,prpLdlossPropMainVoList,
										prpLDlossPersTraceMainVoList);
						}
					}else{
						if(SendPlatformUtil.isMor(cMainVo)){
								sendLossToBIPlatForm(registNo,newVoList,prpLdlossPropMainVoList,
										prpLDlossPersTraceMainVoList);
							}
					}
				}
				if(SendPlatformUtil.isMor(cMainVo)){
					SysUserVo userVo = new SysUserVo();
					userVo.setComCode("00000000");
					userVo.setUserCode("0000000000");
					String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
					if(warnswitch.contains(cMainVo.getComCode().substring(0,2))){
						//???????????????????????????????????????????????????
						PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,cMainVo.getPolicyNo());// ??????????????????			
						try {
							if("2".equals(prpLClaimVo.getValidFlag()) && prpLClaimVo.getComCode().startsWith("10")){
								//?????????????????????
								logger.info("????????????:??????????????????????????????????????????????????????"+registNo);
							}else{
								interfaceAsyncService.sendDlossRegister(cMainVo,userVo);
							}
						} catch (Exception e) {
							logger.error("?????????=" + registNo +"?????????????????????????????????-------------->" , e);
						}
					}
				}
			}
			//????????????????????????taskid
			SysUserVo userVo = new SysUserVo();
			userVo.setComCode("00000000");
			userVo.setUserCode("0000000000");
	        List<PrpLWfTaskVo> vLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.VLoss.toString());
	        List<PrpLWfTaskVo> pLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.PLoss.toString());
	        List<PrpLWfTaskVo> allVolist = new ArrayList<PrpLWfTaskVo>();
	        if(vLossVolist != null && vLossVolist.size() > 0){
	        	allVolist.add(vLossVolist.get(0));
	        }
	        if(pLossVolist != null && pLossVolist.size() > 0){
	        	allVolist.add(pLossVolist.get(0));
	        }
	        //??????????????????
			Collections.sort(allVolist, new Comparator<PrpLWfTaskVo>() {
			@Override
			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
					return o2.getTaskOutTime().compareTo(o1.getTaskOutTime());
				}
			});
            interfaceAsyncService.sendCarLossForGenilex(registNo,allVolist.get(0).getTaskId().toString(), userVo);
			//????????????????????????
            logger.info("????????????????????????-------------->");
            interfaceAsyncService.carRiskImagesUpdate(registNo,userVo,riskCode,null);
		}
	}

	private List<PrpLDlossCarMainVo> getDlossCarMainList(List<PrpLDlossCarMainVo> dlossCarMainVoList){
		List<PrpLDlossCarMainVo> newVoList = new ArrayList<PrpLDlossCarMainVo>();
		Map<Integer,PrpLDlossCarMainVo> carMap = new HashMap<Integer,PrpLDlossCarMainVo>();
		if(dlossCarMainVoList!=null && !dlossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo carMainVo : dlossCarMainVoList){
				if(carMap.containsKey(carMainVo.getSerialNo())){
					PrpLDlossCarMainVo oldCar = carMap.get(carMainVo.getSerialNo());
					oldCar.setSumVeriRemnant(DataUtils.NullToZero(oldCar.getSumVeriRemnant()).add(DataUtils.NullToZero(carMainVo.getSumVeriRemnant())));
					oldCar.setSumVeriLossFee(DataUtils.NullToZero(oldCar.getSumVeriLossFee()).add(DataUtils.NullToZero(carMainVo.getSumVeriLossFee())));
					oldCar.setSumVeripLoss(DataUtils.NullToZero(oldCar.getSumVeripLoss()).add(DataUtils.NullToZero(carMainVo.getSumVeripLoss())));
					
					oldCar.getLossCarInfoVo().setIdentifyType(carMainVo.getLossCarInfoVo().getIdentifyType());
					oldCar.getLossCarInfoVo().setIdentifyNo(carMainVo.getLossCarInfoVo().getIdentifyNo());
					
					//?????????????????????????????????????????????????????????????????????????????????????????????
					if(FlowNode.DLCarAdd.getName().equals(carMainVo.getFlowFlag()) && carMainVo.getUnderWriteEndDate()!=null && oldCar.getUnderWriteEndDate()!=null && carMainVo.getUnderWriteEndDate().getTime()>oldCar.getUnderWriteEndDate().getTime()){
						oldCar.setUnderWriteEndDate(carMainVo.getUnderWriteEndDate());
						oldCar.setCreateTime(carMainVo.getCreateTime());
					}
					
					
					//??????????????????
					List<PrpLDlossCarRepairVo> carRepairVoList = carMainVo.getPrpLDlossCarRepairs();
					if(carRepairVoList != null && !carRepairVoList.isEmpty()){
						for(PrpLDlossCarRepairVo carRepairVo : carRepairVoList){
							oldCar.getPrpLDlossCarRepairs().add(carRepairVo);
						}
					}
					//??????????????????
					List<PrpLDlossCarCompVo> compVoList = carMainVo.getPrpLDlossCarComps();
					if(compVoList != null && !compVoList.isEmpty()){
						for(PrpLDlossCarCompVo compVo : compVoList){
							oldCar.getPrpLDlossCarComps().add(compVo);
						}
					}
				}else{
					carMap.put(carMainVo.getSerialNo(),carMainVo);
				}
			}
		}
		for(Map.Entry<Integer,PrpLDlossCarMainVo> entry : carMap.entrySet()){
			newVoList.add(entry.getValue());
		}
		return newVoList;
	}
	
	/**
	 * ??????????????????????????????
	 * @param lossCarMainVos
	 * @param lossPropMainVos
	 * @param lossPersTraceMainVos
	 * @throws Exception
	 * @modified: ???XMSH(2016???5???4??? ??????3:09:06): <br>
	 */
	private void sendLossToCIPlatForm(String registNo,List<PrpLDlossCarMainVo> lossCarMainVos,
		List<PrpLdlossPropMainVo> lossPropMainVos,List<PrpLDlossPersTraceMainVo> lossPersTraceMainVos) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		/* ?????????????????? */
		String comCode = policyViewService.findPolicyComCode(registNo,"11");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(RequestType.RegistInfoCI.getCode(),registNo,
				comCode);
		if(logVo==null){
			logger.info("?????????=" +registNo + "??????????????????????????????????????????????????????????????????????????????????????????????????????");
		}
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();
		String validNo = policyViewService.findValidNo(registNo,"11");

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.LossInfoCI);

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);

		CiLossReqBodyVo bodyVo = new CiLossReqBodyVo();
		// ????????????
		CiLossBaseVo baseVo = new CiLossBaseVo();
		// ????????????????????????
		List<CiLossCarDataVo> lossCarDataVos = new ArrayList<CiLossCarDataVo>();
		// ????????????????????????
		List<CiLossProtectDataVo> lossProtectDataVos = new ArrayList<CiLossProtectDataVo>();
		// ????????????????????????
		List<CiLossPersonDataVo> lossPersonDataVos = new ArrayList<CiLossPersonDataVo>();

		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		baseVo.setConfirmSequenceNo(validNo);
		baseVo.setClaimCode(claimSeqNo);
		baseVo.setReportNo(registNo);
		// baseVo.setAccidentType("100");???????????????-?????????
		// ???????????????????????????1--??????????????????
		String duty = checkDutyVo.getIndemnityDuty();
		Long dut = Long.parseLong(duty)+1L;
		baseVo.setAccidentLiability(dut.toString());
		baseVo.setManageType(checkVo.getManageType());
		baseVo.setPaySelfFlag(checkVo.getIsClaimSelf());// ????????????
		baseVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());
		
		String isProtectLoss = "0";
		if(lossPropMainVos != null && lossPropMainVos.size() > 0){
			//List<PrpLdlossPropFeeVo> feeVoList = lossPropMainVos.get(0).getPrpLdlossPropFees();//????????????????????????????????????get0
			for(PrpLdlossPropMainVo propMainVo : lossPropMainVos ){//????????????????????????
				if(!CodeConstants.VeriFlag.CANCEL.equals(propMainVo.getUnderWriteFlag())){
					if(propMainVo.getPrpLdlossPropFees() != null && propMainVo.getPrpLdlossPropFees().size() > 0){
						isProtectLoss = "1";
						break;
					}
				}
			}
		}
		baseVo.setIsProtectLoss(isProtectLoss);

		Integer carSum = 0;
		BigDecimal sumDefLoss = BigDecimal.ZERO;
		String isPersonInjured = "0";
		String isSubjoin = "0";
		if(lossCarMainVos!=null&&lossCarMainVos.size()>0){
			for(PrpLDlossCarMainVo carMainVo:lossCarMainVos){
				//???????????????????????????????????????????????????????????????-- ???????????????????????????
				if(LossParty.THIRD.equals(carMainVo.getDeflossCarType()) && 
						(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(carMainVo.getCetainLossType()) ||
								CodeConstants.VeriFlag.CANCEL.equals(carMainVo.getUnderWriteFlag()))){
					continue;
				}
				carSum += 1;
				sumDefLoss = sumDefLoss.add(NullToZero(carMainVo.getSumVeriLossFee()));// ???????????????????????????
				
				//????????????????????????????????????????????????
				if(FlowNode.DLCarAdd.name().equals(carMainVo.getFlowFlag())){
					isSubjoin = "1";
				}else{
					isSubjoin = "0";
				}
				
				//????????????
				List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos= checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo, carMainVo.getLicenseNo());
				PrpLCheckDriverVo prpLCheckDriverVo=null;//???????????????
				if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
					prpLCheckDriverVo=checkTaskService.findPrpLCheckDriverVoById(prpLCheckCarInfoVos.get(0).getCarid());
				}
				
				CiLossCarDataVo carData = new CiLossCarDataVo();
				carData.setCarMark(toTrimLicno(carMainVo.getLicenseNo()));
				String licenseType = carMainVo.getLossCarInfoVo().getLicenseType();
				if("25".equals(licenseType) || "99".equals(licenseType) || StringUtils.isBlank(licenseType)){
					licenseType = "02";
				}
				if("82".equals(licenseType)){
					licenseType = "32";//?????????????????????????????????????????????????????????
				}
				if("81".equals(licenseType)) {
					licenseType = "31";
				}
				if("32".equals(licenseType)) {
					licenseType = "07";
				}
				carData.setVehicleType(licenseType);
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getEngineNo())){
					carData.setEngineNo(carMainVo.getLossCarInfoVo().getEngineNo());
				}else{
					if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
						carData.setEngineNo(prpLCheckCarInfoVos.get(0).getEngineNo());
					}
					
				}
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getVinNo())){
					carData.setRackNo(carMainVo.getLossCarInfoVo().getVinNo());
				}else{
					if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
						carData.setRackNo(prpLCheckCarInfoVos.get(0).getVinNo());
					}
				}
				
				carData.setVehicleModel(carMainVo.getLossCarInfoVo().getModelCode());
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getDriveName())){
					carData.setDriverName(carMainVo.getLossCarInfoVo().getDriveName());
				}else{
					if(prpLCheckDriverVo!=null){
						carData.setDriverName(prpLCheckDriverVo.getDriverName());
					}
				}
				
				// carData.setCertiType(carMainVo.getLossCarInfoVo().getIdentifyType());
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo
						("IdentifyType",carMainVo.getLossCarInfoVo().getIdentifyType());
				carData.setCertiType(sysVo==null?"99":sysVo.getProperty2());
//				carData.setCertiType("01");// ??????
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getIdentifyNo())){
					carData.setCertiCode(carMainVo.getLossCarInfoVo().getIdentifyNo());
				}else{
					if(prpLCheckDriverVo!=null){
						carData.setCertiCode(prpLCheckDriverVo.getIdentifyNumber());
					}
				}
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getDrivingLicenseNo())){
					carData.setDriverLicenseNo(carMainVo.getLossCarInfoVo().getDrivingLicenseNo());
				}else{
					if(prpLCheckDriverVo!=null){
						carData.setDriverLicenseNo(prpLCheckDriverVo.getDrivingLicenseNo());
					}
				}
				
				carData.setUnderDefLoss(NullToZero(carMainVo.getSumVeriLossFee()).toString());
				carData.setVehicleProperty(carMainVo.getSerialNo()==1 ? "1" : "2");
				carData.setIsRobber("03".equals(carMainVo.getCetainLossType()) ? "1" : "0");// 03????????????
				carData.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");// ?????????????????????????????????
				carData.setEstimateName(carMainVo.getHandlerName());
				carData.setEstimateCode(carMainVo.getHandlerCode());
				carData.setUnderWriteName(carMainVo.getUnderWriteName());
				carData.setUnderWriteCode(carMainVo.getUnderWriteCode());
				// carData.setEstimateStartTime(carMainVo.getDeflossDate());
				carData.setEstimateStartTime(carMainVo.getCreateTime());
				if(carMainVo.getDeflossCarType().equals("1")){//??????????????????
					PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(carMainVo.getRegistNo());
					String platformCarKind = null;
					if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
						platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
								ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
						if(StringUtils.isBlank(platformCarKind)){
							platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
						}
						carData.setCarKind(platformCarKind);
					} else{
						carData.setCarKind(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
					}
				} else{//??????????????????
					if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getPlatformCarKindCode())){
						carData.setCarKind(carMainVo.getLossCarInfoVo().getPlatformCarKindCode());//????????????
					}else{
						if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
							carData.setCarKind(prpLCheckCarInfoVos.get(0).getPlatformCarKindCode());
						}
						
					}
					
					//??????????????????????????????????????????????????????
					PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
					List<PrpLCheckCarVo> checkCarVos = checkTaskVo.getPrpLCheckCars();
					boolean carFlag = false;
					if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
						for(PrpLCheckCarVo checkCarVo : checkCarVos){
							//????????????
							if(!"1".equals(checkCarVo.getSerialNo()) &&
									StringUtils.isNotBlank(checkCarVo.getPrpLCheckCarInfo().getLicenseNo()) &&
									carMainVo.getLicenseNo().equals(checkCarVo.getPrpLCheckCarInfo().getLicenseNo())){
								if(checkCarVo.getPrpLCheckCarInfo().getEnrollDate() != null){
									carData.setVehicleRegisterFirstDate(formate.format(checkCarVo.getPrpLCheckCarInfo().getEnrollDate()));
									carFlag = true;
								}
							}
							//?????????????????????
							if(carFlag && checkCarVo.getPrpLCheckDriver().getAcceptLicenseDate() != null){
								carData.setDrivingLicenseDate(formate.format(checkCarVo.getPrpLCheckDriver().getAcceptLicenseDate()));
							}
						}
					}else{
						PrpLDlossCarInfoVo prpLDlossCarInfoVo = deflossService.findDefCarInfoByPk(carMainVo.getCarId());
						//????????????
						carData.setVehicleRegisterFirstDate(formate.format(prpLDlossCarInfoVo.getEnrollDate()));
						//??????????????????
						carData.setDrivingLicenseDate(formate.format(prpLDlossCarInfoVo.getAcceptLicenseDate()));
					}

					
				}
				Date endDate = carMainVo.getUnderWriteEndDate();
				if(endDate==null){
					endDate = new Date();
				}
				carData.setUnderEndTime(endDate);
				carData.setEstimatePlace(carMainVo.getDefSite());
				carData.setRemnant(NullToZero(carMainVo.getSumVeriRemnant()).toString());// ???????????????????????????
				Double totalManHour = 0.0;
				List<PrpLDlossCarRepairVo> carRepairVoList = carMainVo.getPrpLDlossCarRepairs();
				if(carRepairVoList != null && carRepairVoList.size() > 0){
					for(PrpLDlossCarRepairVo repairVo : carRepairVoList){
						totalManHour += DataUtils.NullToZero(repairVo.getVeriManHour()).doubleValue();
					}
				}
				if (totalManHour < 0){
					totalManHour = 0.0;
				}
				String strArray = totalManHour.toString();
				carData.setTotalManHour(strArray.substring(0,strArray.indexOf(".")));
				String isChangeOrRepair = "0";
				if(( carMainVo.getPrpLDlossCarComps()!=null&&carMainVo.getPrpLDlossCarComps().size()>0 )||( carMainVo
						.getPrpLDlossCarRepairs()!=null&&carMainVo.getPrpLDlossCarRepairs().size()>0 )){
					isChangeOrRepair = "1";
				}
				carData.setIsChangeOrRepair(isChangeOrRepair);
				String factoryN = carMainVo.getRepairFactoryName();
				//????????????????????????????????????????????????????????????
				if(StringUtils.isNotBlank(factoryN) && factoryN.length()>25){
					factoryN=factoryN.substring(0,25);
				}
				String factoryC = carMainVo.getRepairFactoryCode();
				String factoryT = carMainVo.getRepairFactoryType();
				carData.setRepairFactoryName(StringUtils.isNotBlank(factoryN) ? factoryN : "??????");
//				carData.setRepairFactoryCode(StringUtils.isNotBlank(factoryC) ? factoryC : "000");
//				carData.setRepairFactoryType(StringUtils.isNotBlank(factoryT) ? factoryT : "003");
				carData.setRepairFactoryCode("11111111X");
				carData.setRepairFactoryType("0");
				carData.setEstimateCertiCode(carMainVo.getHandlerIdNo());
				carData.setUnderWriteCertiCode(carMainVo.getUnderWiteIdNo());
				
				//??????????????????
				BigDecimal sumFittingPrice = BigDecimal.ZERO;
				//??????/??????????????????
				BigDecimal sumHourAccessoriesTotalPrice = BigDecimal.ZERO;

				// ????????????????????????
				String lossPartList = carMainVo.getLossPart();
				if(StringUtils.isNotBlank(lossPartList)){
					String[] lossPartArray = lossPartList.split(",");
					List<CiLossCarLossPartDataVo> lossCarLossPartDataVos = new ArrayList<CiLossCarLossPartDataVo>();
					for(int i = 0; i<lossPartArray.length; i++ ){
						String lossPart = codeTranService.findTransCodeDictVo("LossPart",lossPartArray[i]).getProperty3();
						if(StringUtils.isNotBlank(lossPart)){
							CiLossCarLossPartDataVo lossPartDataVo = new CiLossCarLossPartDataVo();
							lossPartDataVo.setLossPart(lossPart);
							lossCarLossPartDataVos.add(lossPartDataVo);
						}
					}
					if(lossCarLossPartDataVos == null || lossCarLossPartDataVos.size() == 0){
						CiLossCarLossPartDataVo lossPartDataVo = new CiLossCarLossPartDataVo();
						lossPartDataVo.setLossPart("01");
						lossCarLossPartDataVos.add(lossPartDataVo);
					}
					carData.setLossCarLossPartDataVos(lossCarLossPartDataVos);
				}
				
				// ??????????????????
				List<CiLossCarFittingDataVo> lossCarFittingDataVos = new ArrayList<CiLossCarFittingDataVo>();
				List<CiLossCarHourDataVo> lossCarHourDataVos = new ArrayList<CiLossCarHourDataVo>();
				for(PrpLDlossCarRepairVo repairVo:carMainVo.getPrpLDlossCarRepairs()){// ??????
					/*CiLossCarFittingDataVo fittingDataVo = new CiLossCarFittingDataVo();
					`
					fittingDataVo.setChangeOrRepair("2");
					String compName = repairVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);
					fittingDataVo.setFittingCode("999999");
					fittingDataVo.setFittingHostCode("000000000");//??????/???????????????????????????
					fittingDataVo.setFittingPrice(NullToZero(repairVo.getVeriManUnitPrice()).toString());
					if ("001".equals(factoryT)) {//?????????????????????????????????????????????
						fittingDataVo.setFittingPriceType("0000001");
					} else {
						fittingDataVo.setFittingPriceType("0000002");
					}
					fittingDataVo.setFittingNum(DataUtils.NullToZero(repairVo.getVeriMaterQuantity()).toString());
					fittingDataVo.setMaterialFee(NullToZero(repairVo.getVeriManUnitPrice()).toString());
					fittingDataVo.setManHour(NullToZero(repairVo.getManHour()).toString());
					fittingDataVo.setManPowerFee(NullToZero(repairVo.getVeriManHourFee()).toString());
					fittingDataVo.setIsSubjoin(isSubjoin);
					lossCarFittingDataVos.add(fittingDataVo);*/
					
					//??????
					CiLossCarHourDataVo hourDataVo = new CiLossCarHourDataVo();
					if(StringUtils.isNotBlank(repairVo.getRepairFlag()) && !"1".equals(repairVo.getRepairFlag())){
						hourDataVo.setManHourCode("999999999");	 //??????????????????
						hourDataVo.setManHourName(repairVo.getCompName());   //????????????
						hourDataVo.setManHourPrice(NullToZero(repairVo.getManHourFee()).toString()); //?????????
						BigDecimal materialPrice = BigDecimal.ZERO;
						String materialName = "";
						if(carMainVo.getPrpLDlossCarMaterials() != null && carMainVo.getPrpLDlossCarMaterials().size() > 0){
							materialPrice = carMainVo.getPrpLDlossCarMaterials().get(0).getMaterialFee();
							materialName = carMainVo.getPrpLDlossCarMaterials().get(0).getMaterialName();
						}
						hourDataVo.setAccessoriesName(StringUtils.isNotBlank(materialName) ? materialName : "??????");
						hourDataVo.setAccessoriesPrice(NullToZero(materialPrice).toString());

						//???????????????
						sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(repairVo.getManHourFee()));
						//??????????????????
						sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(materialPrice));

						lossCarHourDataVos.add(hourDataVo);
					}
				}
				carData.setLossCarHourDataVos(lossCarHourDataVos);
				
				for(PrpLDlossCarCompVo compVo:carMainVo.getPrpLDlossCarComps()){// ??????
					CiLossCarFittingDataVo fittingDataVo = new CiLossCarFittingDataVo();
					fittingDataVo.setChangeOrRepair("1");
					String compName = compVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);
					//TODO liming fittingCode????????????
					//fittingDataVo.setFittingCode(compVo.getCompCode());
					fittingDataVo.setFittingCode("999999");
					fittingDataVo.setFittingNum(DataUtils.NullToZeroInt(compVo.getVeriQuantity()).toString());
					fittingDataVo.setMaterialFee(NullToZero(compVo.getVeriMaterFee()).toString());
					fittingDataVo.setManHour(NullToZero(compVo.getManHour()).toString());
					fittingDataVo.setManPowerFee(NullToZero(compVo.getVeriManHourFee()).toString());
					//??????????????????????????????????????????
					fittingDataVo.setIsSubjoin(isSubjoin);
					fittingDataVo.setFittingPrice(NullToZero(compVo.getVeriMaterFee()).toString());
					fittingDataVo.setFittingHostCode(compVo.getOriginalId());//??????/???????????????????????????
					if ("001".equals(factoryT)) {//?????????????????????????????????????????????
						fittingDataVo.setFittingPriceType("0000001");
					} else {
						fittingDataVo.setFittingPriceType("0000002");
					}
					//??????????????????
					sumFittingPrice = sumFittingPrice.add(NullToZero(compVo.getVeriMaterFee()).multiply(new BigDecimal(DataUtils.NullToZeroInt(compVo.getVeriQuantity()))));
					
					lossCarFittingDataVos.add(fittingDataVo);
				}
				carData.setFittingTotalPrice(sumFittingPrice.toString());
				carData.setLossCarFittingDataVos(lossCarFittingDataVos);
//				//????????????
//				for(PrpLDlossCarMaterialVo carMateralVo : carMainVo.getPrpLDlossCarMaterials()){
//					CiLossCarHourDataVo hourDataVo = new CiLossCarHourDataVo();
//					hourDataVo.setManHourCode("999999999");
//					hourDataVo.setManHourPrice("0");
//					hourDataVo.setAccessoriesName(carMateralVo.getMaterialName());
//					hourDataVo.setAccessoriesPrice(NullToZero(carMateralVo.getMaterialFee()).toString());
//					//???????????????
//					sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(carMateralVo.getMaterialFee()));
//					lossCarHourDataVos.add(hourDataVo);
//				}
				carData.setManHourAccessoriesTotalPrice(sumHourAccessoriesTotalPrice.toString());
//				carData.setLossCarHourDataVos(lossCarHourDataVos);

				lossCarDataVos.add(carData);
			}
		}
		if(lossPropMainVos!=null&&lossPropMainVos.size()>0){
			for(PrpLdlossPropMainVo propMainVo:lossPropMainVos){
				//???????????????????????????
				if(CodeConstants.VeriFlag.CANCEL.equals(propMainVo.getUnderWriteFlag())){
					continue;
				}
				sumDefLoss = sumDefLoss.add(NullToZero(propMainVo.getSumVeriLoss()));

				for(PrpLdlossPropFeeVo feeVo:propMainVo.getPrpLdlossPropFees()){
					CiLossProtectDataVo lossProtectDataVo = new CiLossProtectDataVo();
					String cutOutLossItemName = StringOperationUtil.cutOutString(feeVo.getLossItemName(),30);
					lossProtectDataVo.setProtectName(cutOutLossItemName);
					lossProtectDataVo.setOwner(feeVo.getOwner());
					lossProtectDataVo.setLossDesc(cutOutLossItemName);//????????????
					lossProtectDataVo.setLossNum(DataUtils.NullToZero(feeVo.getLossQuantity()).toString());
					lossProtectDataVo.setUnderDefLoss(NullToZero(feeVo.getSumVeriLoss()).toString());
					// lossProtectDataVo.setOwner(owner);
					lossProtectDataVo.setProtectProperty("01".equals(propMainVo.getLossType()) ? "1" : "2");
					lossProtectDataVo.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");
					lossProtectDataVo.setEstimateName(propMainVo.getHandlerName());
					lossProtectDataVo.setEstimateCode(propMainVo.getHandlerCode());
					lossProtectDataVo.setEstimateCertiCode(propMainVo.getHandlerIdCard());
					lossProtectDataVo.setUnderWriteName(propMainVo.getUnderWriteName());
					lossProtectDataVo.setUnderWriteCode(propMainVo.getUnderWriteCode());
					lossProtectDataVo.setUnderWriteCertiCode(propMainVo.getUnderWriteIdCard());
					lossProtectDataVo.setEstimateStartTime(propMainVo.getCreateTime());
					lossProtectDataVo.setUnderEndTime(propMainVo.getUnderWriteEndDate());
					// lossProtectDataVo.setEstimatePlace(estimatePlace);
					lossProtectDataVos.add(lossProtectDataVo);
				}
			}
		}
		if(lossPersTraceMainVos!=null&&lossPersTraceMainVos.size()>0){
			for(PrpLDlossPersTraceMainVo persTraceMainVo:lossPersTraceMainVos){
			    if("7".equals(persTraceMainVo.getUnderwriteFlag())){
                    continue;
                }
				List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
				if( !"10".equals(persTraceMainVo.getCaseProcessType())&&persTraceVoList!=null&&persTraceVoList.size()>0){
					for(PrpLDlossPersTraceVo persTraceVo:persTraceMainVo.getPrpLDlossPersTraces()){
						if("1".equals(persTraceVo.getValidFlag())){//??????????????????????????????????????????
							isPersonInjured = "1";//??????????????????????????????????????????
							sumDefLoss = sumDefLoss.add(NullToZero(persTraceVo.getSumVeriDefloss()));
	
							CiLossPersonDataVo personDataVo = new CiLossPersonDataVo();
							personDataVo.setPersonName(persTraceVo.getPrpLDlossPersInjured().getPersonName());
							String certiType = persTraceVo.getPrpLDlossPersInjured().getCertiType();
							//certiType = "1".equals(certiType) ? "01" : "99";
							SysCodeDictVo sysdicVo = codeTranService.findTransCodeDictVo
			                        ("IdentifyType",certiType);
							personDataVo.setCertiType(sysdicVo==null?"99":sysdicVo.getProperty3());
							personDataVo.setCertiCode(persTraceVo.getPrpLDlossPersInjured().getCertiCode());
							personDataVo.setPersonProperty(ciCodeTranService.findTranCodeCode("LossItemType",persTraceVo.getPrpLDlossPersInjured().getLossItemType()));
							personDataVo.setMedicalType(persTraceVo.getPrpLDlossPersInjured().getTreatSituation());//???????????? ?????????
	//						Date inHospitalDate = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()
	//								.get(0).getInHospitalDate();
	//						personDataVo.setAddmissionTime(inHospitalDate);
	//						String injuryType = "";
	//						String woundCode = persTraceVo.getPrpLDlossPersInjured().getWoundCode();
	//						if("A".equals(woundCode)){// ???
	//							injuryType = "01";
	//						}else if("B".equals(woundCode)){// ???
	//							injuryType = "02";
	//						}else if("C".equals(woundCode)){// ???
	//							injuryType = "03";
	//						}
							String woundCode = persTraceVo.getPrpLDlossPersInjured().getWoundCode();
							personDataVo.setInjuryType(StringUtils.isEmpty(woundCode) ? "01" : woundCode);
							// personDataVo.setInjuryLevel(injuryLevel);
							personDataVo.setUnderDefLoss(NullToZero(persTraceVo.getSumVeriDefloss()).toString());
							personDataVo.setEstimateName(persTraceMainVo.getOperatorName());
							personDataVo.setEstimateCode(persTraceMainVo.getOperatorCode());
							if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
								personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
							} else {
								personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
							}
							personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
							personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
							personDataVo.setEstimateStartTime(persTraceMainVo.getCreateTime());
							personDataVo.setUnderEndTime(persTraceMainVo.getUndwrtFeeEndDate());
							personDataVo.setUnderWriteCertiCode(persTraceMainVo.getVerifyCertiCode());
							// personDataVo.setEstimatePlace(estimatePlace);
							// personDataVo.setUnderWriteCode(persTraceMainVo.getun);//?????????
							Date deathTime = persTraceVo.getPrpLDlossPersInjured().getDeathTime();
							if(deathTime == null && "3".equals(persTraceVo.getPrpLDlossPersInjured().getTreatSituation())){
								deathTime = new Date();
							}
							Date deathDate = persTraceVo.getPrpLDlossPersInjured().getDeathTime();
							if(deathDate != null){
								personDataVo.setDeathTime(deathDate);
							}
	
							// ??????????????????????????????
							List<CiLossPersonLossFeeDataVo> feeVoList = new ArrayList<CiLossPersonLossFeeDataVo>();
							for(PrpLDlossPersTraceFeeVo persTraceFeeVo:persTraceVo.getPrpLDlossPersTraceFees()){
								CiLossPersonLossFeeDataVo lossFeeDataVo = new CiLossPersonLossFeeDataVo();
								SysCodeDictVo sysVo = codeTranService.findCodeDictVo("FeeType",persTraceFeeVo.getFeeTypeCode());
								if(sysVo != null){
									lossFeeDataVo.setFeeType(sysVo.getProperty1());
								}
								lossFeeDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).toString());
								feeVoList.add(lossFeeDataVo);
							}
							personDataVo.setLossPersonLossFeeDataVos(feeVoList);
	
							// ??????????????????
							List<CiLossPersonInjuryDataVo> injuryVoList = new ArrayList<CiLossPersonInjuryDataVo>();
							for(PrpLDlossPersExtVo persExtVo:persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersExts()){
								CiLossPersonInjuryDataVo injuryDataVo = new CiLossPersonInjuryDataVo();
								injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());
								String woundGrade = persExtVo.getWoundGrade();
								if(StringUtils.isNotBlank(woundGrade) && !"10".equals(woundGrade)){
									woundGrade = "0" + woundGrade;
								}else{
									woundGrade = "01";
								}
								injuryDataVo.setInjuryLevelCode(woundGrade);
								injuryVoList.add(injuryDataVo);
							}
							personDataVo.setLossPersonInjuryDataVos(injuryVoList);
	
							// ??????????????????
							List<CiLossPersonHospitalInfoDataVo> hospitalVoList = new ArrayList<CiLossPersonHospitalInfoDataVo>();
							for (PrpLDlossPersHospitalVo persHospitalVo : persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()) {
								CiLossPersonHospitalInfoDataVo hospitalInfoDataVo = new CiLossPersonHospitalInfoDataVo();
								String hospitalName = persHospitalVo.getHospitalName();
								if (StringUtils.isEmpty(hospitalName)) {
									hospitalName = "????????????????????????";
								}
								//??????????????????????????????
								hospitalName = StringOperationUtil.cutOutString(hospitalName,30);
								hospitalInfoDataVo.setHospitalName(hospitalName);
								// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.getHospitalCode());
								hospitalVoList.add(hospitalInfoDataVo);
								personDataVo.setEstimatePlace(hospitalName);//??????????????????
								if(persHospitalVo.getInHospitalDate() != null ){
								    personDataVo.setAddmissionTime(formate.format(persHospitalVo.getInHospitalDate()));//????????????
								}
							}
							// ???????????????????????????????????????????????????????????????????????????
							if(hospitalVoList == null || hospitalVoList.size() == 0){
								CiLossPersonHospitalInfoDataVo hospitalInfoDataVo = new CiLossPersonHospitalInfoDataVo();
								hospitalInfoDataVo.setHospitalName("????????????????????????");
								// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.get);
								hospitalVoList.add(hospitalInfoDataVo);
							}
							personDataVo.setLossPersonHospitalInfoDataVos(hospitalVoList);
	
							// ????????????????????????,,,--????????????????????????????????????????????????????????????????????????
							if("02".equals(persTraceVo.getPrpLDlossPersInjured().getWoundCode())){
								List<CiLossPersonInjuryIdentifyInfoDataVo> identify = new ArrayList<CiLossPersonInjuryIdentifyInfoDataVo>();
								CiLossPersonInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new CiLossPersonInjuryIdentifyInfoDataVo();
								String chkName = persTraceVo.getPrpLDlossPersInjured().getChkComName();
								if(StringUtils.isEmpty(chkName)){
									chkName = "??????????????????????????????";
								}
								chkName = StringOperationUtil.cutOutString(chkName,30);
								injuryIdentifyInfoDataVo.setInjuryIdentifyName(chkName);
	//							injuryIdentifyInfoDataVo.setInjuryIdentifyCerticode(persTraceVo.getPrpLDlossPersInjured().getChkComCode());
								identify.add(injuryIdentifyInfoDataVo);
								
								personDataVo.setLossPersonInjuryIdentifyInfoDataVos(identify);
								personDataVo.getLossPersonInjuryIdentifyInfoDataVos().add(injuryIdentifyInfoDataVo);
							}
							
							//
							lossPersonDataVos.add(personDataVo);
						}
					}
				}
			}
		}
		baseVo.setUnderTotalDefLoss(sumDefLoss.compareTo(BigDecimal.ZERO)==0 ? "0.0":sumDefLoss.toString());
		baseVo.setIsPersonInjured(isPersonInjured);
		baseVo.setIsSingleAccident(carSum > 1 ? "0" : "1");
		
		bodyVo.setLossBaseVo(baseVo);
		bodyVo.setLossCarDataVos(lossCarDataVos);
		bodyVo.setLossProtectDataVos(lossProtectDataVos);
		bodyVo.setLossPersonDataVos(lossPersonDataVos);

		controller.callPlatform(bodyVo);
	}

	/**
	 * ??????????????????????????????
	 * @param lossCarMainVos
	 * @param lossPropMainVos
	 * @param lossPersTraceMainVos
	 * @throws Exception
	 * @modified: ???XMSH(2016???5???4??? ??????3:10:22): <br>
	 */
	private void sendLossToBIPlatForm(String registNo,List<PrpLDlossCarMainVo> lossCarMainVos,
		List<PrpLdlossPropMainVo> lossPropMainVos,List<PrpLDlossPersTraceMainVo> lossPersTraceMainVos) throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// ????????? ??????????????????????????????
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			return;
		}
		// ??????????????????
		String comCode = policyViewService.findPolicyComCode(registNo,"12");
		CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo
				(RequestType.RegistInfoBI.getCode(),registNo,comCode);
		if(logVo==null){
			logger.info("?????????=" + registNo + "??????????????????????????????????????????????????????????????????????????????????????????????????????");
		}
		String claimSeqNo = logVo==null ? "" : logVo.getClaimSeqNo();
		String validNo = policyViewService.findValidNo(registNo,"12");

		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.LossInfoBI);

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);

		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		BiLossReqBodyVo bodyVo = new BiLossReqBodyVo();
		// ????????????
		BiLossBaseVo baseVo = new BiLossBaseVo();
		// ????????????????????????
		List<BiLossCarVehicleDataVo> vehicleDataVos = new ArrayList<BiLossCarVehicleDataVo>();
		// ????????????????????????
		List<BiLossProtectDataVo> protectDataVos = new ArrayList<BiLossProtectDataVo>();
		// ????????????????????????
		List<BiLossPersonDataVo> personDataVos = new ArrayList<BiLossPersonDataVo>();

		baseVo.setConfirmSequenceNo(validNo);
		baseVo.setClaimSequenceNo(claimSeqNo);
		baseVo.setClaimNotificationNo(registNo);
		// baseVo.setAccidentType("100");???????????????-?????????
		// ???????????????????????????1--??????????????????
		String duty = checkDutyVo.getIndemnityDuty();
		Long dut = Long.parseLong(duty)+1L;
		baseVo.setAccidentLiability(dut.toString());

		baseVo.setOptionType(checkVo.getManageType());
		// ????????????
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		String subflag = StringUtils.isEmpty(subrogationMainVo.getSubrogationFlag())
				?"0":subrogationMainVo.getSubrogationFlag();
		baseVo.setSubrogationFlag(subflag);
		baseVo.setSubCertiType(getCertiType(registNo,"1"));//
		baseVo.setSubClaimFlag("0".equals(subflag) ? "0" : getCertiType(registNo,"2"));//

		baseVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());
		baseVo.setUnderTotalDefLoss("0.0");
		String isProtectLoss = "0";
		if(lossPropMainVos != null && lossPropMainVos.size() > 0){
			// ????????????????????????????????????????????????????????????????????????
			for (PrpLdlossPropMainVo propMainVo : lossPropMainVos) {
				if(!CodeConstants.VeriFlag.CANCEL.equals(propMainVo.getUnderWriteFlag())){
					List<PrpLdlossPropFeeVo> feeVoList = propMainVo.getPrpLdlossPropFees();
					if (feeVoList != null && feeVoList.size() > 0) {
						isProtectLoss = "1";
					}
				}
				
			}
		}
		baseVo.setIsProtectLoss(isProtectLoss);
		baseVo.setIsPersonInjured(lossPersTraceMainVos!=null&&lossPersTraceMainVos.size()>0?"1":"0");

		Integer carSum = 0;
		BigDecimal sumDefLoss = BigDecimal.ZERO;
		String isPersonInjured = "0";
		String isSubjoin = "0";
		if(lossCarMainVos!=null&&lossCarMainVos.size()>0){
			for(PrpLDlossCarMainVo carMainVo:lossCarMainVos){
				//??????????????????????????????????????????????????????????????????????????????????????????
				if(LossParty.THIRD.equals(carMainVo.getDeflossCarType()) && 
						(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(carMainVo.getCetainLossType()) ||
								CodeConstants.VeriFlag.CANCEL.equals(carMainVo.getUnderWriteFlag()))){
					continue; 
				}
				carSum += 1;
				sumDefLoss = sumDefLoss.add(NullToZero(carMainVo.getSumVeriLossFee()));
				
				//????????????????????????????????????????????????
				if(FlowNode.DLCarAdd.name().equals(carMainVo.getFlowFlag())){
					isSubjoin = "1";
				}else{
					isSubjoin = "0";
				}
				//????????????
				List<PrpLCheckCarInfoVo> prpLCheckCarInfoVos= checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo, carMainVo.getLicenseNo());
				PrpLCheckDriverVo prpLCheckDriverVo=null;//???????????????
				if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
					prpLCheckDriverVo=checkTaskService.findPrpLCheckDriverVoById(prpLCheckCarInfoVos.get(0).getCarid());
				}
				BiLossCarVehicleDataVo carVehicleData = new BiLossCarVehicleDataVo();
				carVehicleData.setLicensePlateNo(toTrimLicno(carMainVo.getLossCarInfoVo().getLicenseNo()==null?carMainVo.getLossCarInfoVo().getLicenseNo():carMainVo.getLossCarInfoVo().getLicenseNo().trim()));
				String licenseType = carMainVo.getLossCarInfoVo().getLicenseType();
				if("25".equals(licenseType) || "99".equals(licenseType) || StringUtils.isBlank(licenseType)){
					licenseType = "02";
				}
				if("82".equals(licenseType)){
					licenseType = "32";//?????????????????????????????????????????????????????????
				}
				if("81".equals(licenseType)) {
					licenseType = "31";
				}
				if("32".equals(licenseType)) {
					licenseType = "07";
				}
				if(carMainVo.getDeflossCarType().equals("1")){//??????????????????
					PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(carMainVo.getRegistNo());
					String platformCarKind = null;
					if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
						platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
								ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
						if(StringUtils.isBlank(platformCarKind)){
							platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
						}
						carVehicleData.setMotorTypeCode(platformCarKind);
					} else{
						carVehicleData.setMotorTypeCode(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
					}
					
					//????????????????????????????????????????????????????????????Vin???
					if(StringUtils.isNotBlank(ciItemCarVo.getVinNo())){
						carVehicleData.setVIN(ciItemCarVo.getVinNo());
					}else{
						if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getVinNo())){
							carVehicleData.setVIN(carMainVo.getLossCarInfoVo().getVinNo());
						}else{
							if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
								carVehicleData.setVIN(prpLCheckCarInfoVos.get(0).getVinNo());
							}
						}
					}

				} else{//?????????????????????????????????????????????????????????????????????
					if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getPlatformCarKindCode())){
						carVehicleData.setMotorTypeCode(carMainVo.getLossCarInfoVo().getPlatformCarKindCode());//????????????
					}else{
						if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
							carVehicleData.setMotorTypeCode(prpLCheckCarInfoVos.get(0).getPlatformCarKindCode());
						}
					}
					
					//???????????????????????????????????????Vin???
					if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getVinNo())){
						carVehicleData.setVIN(carMainVo.getLossCarInfoVo().getVinNo());
					}else{
						if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
							carVehicleData.setVIN(prpLCheckCarInfoVos.get(0).getVinNo());
						}
					}
					
					//??????????????????????????????????????????????????????
					PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
					List<PrpLCheckCarVo> checkCarVos = checkTaskVo.getPrpLCheckCars();
					boolean carFlag = false;
					if (prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0) {
						for (PrpLCheckCarVo checkCarVo : checkCarVos) {
							//????????????
							if (!"1".equals(checkCarVo.getSerialNo()) &&
									StringUtils.isNotBlank(checkCarVo.getPrpLCheckCarInfo().getLicenseNo()) &&
									carMainVo.getLicenseNo().equals(checkCarVo.getPrpLCheckCarInfo().getLicenseNo())) {
								if (checkCarVo.getPrpLCheckCarInfo().getEnrollDate() != null) {
									carVehicleData.setVehicleRegisterFirstDate(formate.format(checkCarVo.getPrpLCheckCarInfo().getEnrollDate()));
									carFlag = true;
								}
							}
							//?????????????????????
							if (carFlag && checkCarVo.getPrpLCheckDriver().getAcceptLicenseDate() != null) {
								carVehicleData.setDrivingLicenseDate(formate.format(checkCarVo.getPrpLCheckDriver().getAcceptLicenseDate()));
							}
						}
					}else {
						PrpLDlossCarInfoVo prpLDlossCarInfoVo = deflossService.findDefCarInfoByPk(carMainVo.getCarId());
						//????????????
						carVehicleData.setVehicleRegisterFirstDate(formate.format(prpLDlossCarInfoVo.getEnrollDate()));
						//??????????????????
						carVehicleData.setDrivingLicenseDate(formate.format(prpLDlossCarInfoVo.getAcceptLicenseDate()));
					}
				}
		
				carVehicleData.setLicensePlateType(licenseType);
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getEngineNo())){
					carVehicleData.setEngineNo(carMainVo.getLossCarInfoVo().getEngineNo());
				}else{
					if(prpLCheckCarInfoVos!=null && prpLCheckCarInfoVos.size()>0){
						carVehicleData.setEngineNo(prpLCheckCarInfoVos.get(0).getEngineNo());
					}
					
				}
				carVehicleData.setModel(carMainVo.getLossCarInfoVo().getModelCode());
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getDriveName())){
					carVehicleData.setDriverName(carMainVo.getLossCarInfoVo().getDriveName());
				}else{
					if(prpLCheckDriverVo!=null){
						carVehicleData.setDriverName(prpLCheckDriverVo.getDriverName());
					}
				}
				String idNoType = carMainVo.getLossCarInfoVo().getIdentifyType();
				SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("IdentifyType",idNoType);
				carVehicleData.setCertiType(sysVo==null?"99":sysVo.getProperty3());
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getIdentifyNo())){
					carVehicleData.setCertiCode(carMainVo.getLossCarInfoVo().getIdentifyNo());
				}else{
					if(prpLCheckDriverVo!=null){
						carVehicleData.setCertiCode(prpLCheckDriverVo.getIdentifyNumber());
					}
				}
				if(StringUtils.isNotBlank(carMainVo.getLossCarInfoVo().getDrivingLicenseNo())){
					carVehicleData.setDriverLicenseNo(carMainVo.getLossCarInfoVo().getDrivingLicenseNo());
				}else{
					if(prpLCheckDriverVo!=null){
						carVehicleData.setDriverLicenseNo(prpLCheckDriverVo.getDrivingLicenseNo());
					}
				}
//				carVehicleData.setCertiType("01");// ??????
				carVehicleData.setUnderDefLoss(NullToZero(carMainVo.getSumVeriLossFee()).toString());
				carVehicleData.setVehicleProperty(carMainVo.getSerialNo()==1 ? "1" : "2");
				carVehicleData.setIsRobber("03".equals(carMainVo.getCetainLossType()) ? "1" : "0");// 03????????????
				carVehicleData.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");// ?????????????????????????????????
				carVehicleData.setEstimateName(carMainVo.getHandlerName());
				carVehicleData.setEstimateCode(carMainVo.getHandlerCode());
				carVehicleData.setEstimateCertiCode(carMainVo.getHandlerIdNo());
				carVehicleData.setUnderWriteCode(carMainVo.getUnderWriteCode());
				carVehicleData.setUnderWriteName(carMainVo.getUnderWriteName());
				carVehicleData.setUnderWriteCertiCode(carMainVo.getUnderWiteIdNo());
				Date endDate = carMainVo.getUnderWriteEndDate();
				if(endDate==null){
					endDate = new Date();
				}
				carVehicleData.setEstimateStartTime(carMainVo.getCreateTime());
				carVehicleData.setUnderEndTime(endDate);
				carVehicleData.setEstimateAddr(carMainVo.getDefSite());
				carVehicleData.setRemnant(NullToZero(carMainVo.getSumVeriRemnant()).toString());// ???????????????????????????
				
				carVehicleData.setIsTotalLoss(carMainVo.getIsTotalLoss());
				carVehicleData.setIsHotSinceDetonation(carMainVo.getIsHotSinceDetonation());
				carVehicleData.setIsWaterFlooded(carMainVo.getIsWaterFloaded());
				carVehicleData.setWaterFloodedLevel("1".equals(carMainVo.getIsWaterFloaded())?carMainVo.getWaterFloodedLevel():"");
				
				Double totalManHour = 0.0;
				for(PrpLDlossCarRepairVo repairVo:carMainVo.getPrpLDlossCarRepairs()){
					totalManHour += DataUtils.NullToZero(repairVo.getVeriManHour()).doubleValue();
				}
				String strArray = totalManHour.toString();
				carVehicleData.setTotalManHour(strArray.substring(0,strArray.indexOf(".")));
				String isChangeOrRepair = "0";
				if(( carMainVo.getPrpLDlossCarComps()!=null&&carMainVo.getPrpLDlossCarComps().size()>0 )||( carMainVo
						.getPrpLDlossCarRepairs()!=null&&carMainVo.getPrpLDlossCarRepairs().size()>0 )){
					isChangeOrRepair = "1";
				}
				carVehicleData.setIsChangeOrRepair(isChangeOrRepair);
				String factoryN = carMainVo.getRepairFactoryName();
				//????????????????????????????????????????????????????????????
				if(StringUtils.isNotBlank(factoryN) && factoryN.length()>25){
					factoryN=factoryN.substring(0,25);
				}
				String factoryC = carMainVo.getRepairFactoryCode();
				String factoryT = carMainVo.getRepairFactoryType();
				carVehicleData.setRepairFactoryName(StringUtils.isNotBlank(factoryN) ? factoryN : "??????");
//				carVehicleData.setRepairFactoryCertiCode(StringUtils.isNotBlank(factoryC) ? factoryC : "00000000");
				carVehicleData.setRepairFactoryCertiCode("11111111X");
//				carVehicleData.setRepairFactoryType(StringUtils.isBlank(factoryT) ? "003" : factoryT);
				carVehicleData.setRepairFactoryType("0");
				//TODO xiaofei  ---????????????????????????, ??????????????????????????????, ??????????????????, ??????/??????????????????
				//??????????????????
				BigDecimal sumFittingPrice = BigDecimal.ZERO;
				//??????/??????????????????
				BigDecimal sumHourAccessoriesTotalPrice = BigDecimal.ZERO;

				// ????????????????????????
				String lossPartList = carMainVo.getLossPart();
				if(StringUtils.isNotBlank(lossPartList)){
					String[] lossPartArray = lossPartList.split(",");
					
					List<BiLossCarLossPartDataVo> lossCarLossPartDataVos = new ArrayList<BiLossCarLossPartDataVo>();
					for(int i = 0; i<lossPartArray.length; i++ ){
						String lossPart = codeTranService.findTransCodeDictVo("LossPart",lossPartArray[i]).getProperty3();
						if(StringUtils.isNotBlank(lossPart)){
							BiLossCarLossPartDataVo lossPartDataVo = new BiLossCarLossPartDataVo();
							lossPartDataVo.setLossPart(lossPart);
							lossCarLossPartDataVos.add(lossPartDataVo);
						}	
					}
					if(lossCarLossPartDataVos == null || lossCarLossPartDataVos.size() == 0){
						BiLossCarLossPartDataVo lossPartDataVo = new BiLossCarLossPartDataVo();
						lossPartDataVo.setLossPart("01");
						lossCarLossPartDataVos.add(lossPartDataVo);
					}
					carVehicleData.setLossPartDataVos(lossCarLossPartDataVos);
				}
				
				//TODO xiaofei  -- ??????/?????????????????? --??????---  ---??????????????????????????????????????????

				// ??????????????????
				List<BiLossCarFittingDataVo> lossCarFittingDataVos = new ArrayList<BiLossCarFittingDataVo>();
				List<BiLossCarHourDataVo> lossCarHourDataVos = new ArrayList<BiLossCarHourDataVo>();
				for(PrpLDlossCarRepairVo repairVo:carMainVo.getPrpLDlossCarRepairs()){// ??????
					/*BiLossCarFittingDataVo fittingDataVo = new BiLossCarFittingDataVo();
					
					fittingDataVo.setChangeOrRepair("2");
					String compName = repairVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);
					fittingDataVo.setFittingCode("999999");
					fittingDataVo.setFittingHostCode("000000000");//??????/???????????????????????????
					fittingDataVo.setFittingPrice(NullToZero(repairVo.getVeriManUnitPrice()).toString());
					if ("001".equals(factoryT)) {//?????????????????????????????????????????????
						fittingDataVo.setFittingPriceType("0000001");
					} else {
						fittingDataVo.setFittingPriceType("0000002");
					}
					fittingDataVo.setFittingNum(DataUtils.NullToZero(repairVo.getVeriMaterQuantity()).toString());
					fittingDataVo.setMaterialFee(NullToZero(repairVo.getVeriManUnitPrice()).toString());
					fittingDataVo.setManHour(DataUtils.NullToZero(repairVo.getVeriManHour()).toString());
					fittingDataVo.setManPowerFee(NullToZero(repairVo.getVeriManHourFee()).toString());
					 fittingDataVo.setIsSubjoin(isSubjoin);
					lossCarFittingDataVos.add(fittingDataVo);*/
					
					//??????
					BiLossCarHourDataVo hourDataVo = new BiLossCarHourDataVo();
					if(StringUtils.isNotBlank(repairVo.getRepairFlag()) && !"1".equals(repairVo.getRepairFlag())){
//						hourDataVo.setManHourCode(repairVo.getCompCode());	 //??????????????????
						hourDataVo.setManHourCode("999999999");	 //??????????????????
						hourDataVo.setManHourName(repairVo.getCompName());   //????????????
						hourDataVo.setManHourPrice(NullToZero(repairVo.getManHourFee()).toString()); //?????????
						BigDecimal materialPrice = BigDecimal.ZERO;
						String materialName = "";
						if(carMainVo.getPrpLDlossCarMaterials() != null && carMainVo.getPrpLDlossCarMaterials().size() > 0){
							materialPrice = carMainVo.getPrpLDlossCarMaterials().get(0).getMaterialFee();
							materialName = carMainVo.getPrpLDlossCarMaterials().get(0).getMaterialName();
						}
						hourDataVo.setAccessoriesName(StringUtils.isNotBlank(materialName) ? materialName : "??????");
						hourDataVo.setAccessoriesPrice(NullToZero(materialPrice).toString());

						//???????????????
						sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(repairVo.getManHourFee()));
						//??????????????????
						sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(materialPrice));

						lossCarHourDataVos.add(hourDataVo);
					}
				}
				carVehicleData.setLossCarHourDataVos(lossCarHourDataVos);
				
				for(PrpLDlossCarCompVo compVo:carMainVo.getPrpLDlossCarComps()){// ??????
					BiLossCarFittingDataVo fittingDataVo = new BiLossCarFittingDataVo();
					fittingDataVo.setChangeOrRepair("1");
					String compName = compVo.getCompName();
					if(StringUtils.isNotBlank(compName) && compName.length() > 50){
						compName = compName.substring(0,49);
					}
					fittingDataVo.setFittingName(compName);
					//TODO liming fittingCode????????????
					//fittingDataVo.setFittingCode(compVo.getCompCode());
					fittingDataVo.setFittingCode("999999");
					fittingDataVo.setFittingNum(DataUtils.NullToZeroInt(compVo.getVeriQuantity()).toString());
					fittingDataVo.setMaterialFee(NullToZero(compVo.getVeriMaterFee()).toString());
					fittingDataVo.setManHour(DataUtils.NullToZero(compVo.getManHour()).toString());
					fittingDataVo.setManPowerFee(NullToZero(compVo.getVeriManHourFee()).toString());
					fittingDataVo.setFittingHostCode(compVo.getOriginalId());//??????/???????????????????????????
					fittingDataVo.setFittingPrice(NullToZero(compVo.getVeriMaterFee()).toString());
					if ("001".equals(factoryT)) {//?????????????????????????????????????????????
						fittingDataVo.setFittingPriceType("0000001");
					} else {
						fittingDataVo.setFittingPriceType("0000002");
					}
					fittingDataVo.setIsSubjoin(isSubjoin);
					//??????????????????
					sumFittingPrice = sumFittingPrice.add((NullToZero(compVo.getVeriMaterFee()).multiply(new BigDecimal(DataUtils.NullToZeroInt(compVo.getVeriQuantity())))));
					lossCarFittingDataVos.add(fittingDataVo);
				}
				carVehicleData.setFittingTotalPrice(sumFittingPrice.toString());
				carVehicleData.setFittingDataVos(lossCarFittingDataVos);

//				//????????????
//				for(PrpLDlossCarMaterialVo carMateralVo : carMainVo.getPrpLDlossCarMaterials()){
//					BiLossCarHourDataVo hourDataVo = new BiLossCarHourDataVo();
//					hourDataVo.setManHourCode("999999999");
//					hourDataVo.setManHourPrice("0");
//					hourDataVo.setAccessoriesName(carMateralVo.getMaterialName());
//					hourDataVo.setAccessoriesPrice(NullToZero(carMateralVo.getMaterialFee()).toString());
//					//???????????????
//					sumHourAccessoriesTotalPrice = sumHourAccessoriesTotalPrice.add(NullToZero(carMateralVo.getMaterialFee()));
//					lossCarHourDataVos.add(hourDataVo);
//				}
				carVehicleData.setManHourAccessoriesTotalPrice(sumHourAccessoriesTotalPrice.toString()); //??????/??????????????????
//				carVehicleData.setLossCarHourDataVos(lossCarHourDataVos);  
				
				//??????  ???????????????????????????????????????????????????????????? ????????????
				//13.???2020??????????????????????????????????????????????????????????????????????????????????????????????????????????????????14.???2014?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
				carVehicleData.setIsNotFindThird(StringUtils.isNotBlank(carMainVo.getIsNotFindThird())?carMainVo.getIsNotFindThird():"0");
				carVehicleData.setIsGlassBroken(StringUtils.isNotBlank(carMainVo.getIsGlassBroken())?carMainVo.getIsGlassBroken():"0");
				vehicleDataVos.add(carVehicleData);
			}

			if(lossPropMainVos!=null&&lossPropMainVos.size()>0){
				for(PrpLdlossPropMainVo propMainVo:lossPropMainVos){
					//???????????????????????????
					if(CodeConstants.VeriFlag.CANCEL.equals(propMainVo.getUnderWriteFlag())){
						continue;
					}
					sumDefLoss = sumDefLoss.add(NullToZero(propMainVo.getSumVeriLoss()));

					for(PrpLdlossPropFeeVo feeVo:propMainVo.getPrpLdlossPropFees()){
						BiLossProtectDataVo lossProtectDataVo = new BiLossProtectDataVo();
						String cutOutLossItemName = StringOperationUtil.cutOutString(feeVo.getLossItemName(),30);
						lossProtectDataVo.setProtectName(cutOutLossItemName);
						 lossProtectDataVo.setLossDesc(cutOutLossItemName);//????????????
						lossProtectDataVo.setLossNum(DataUtils.NullToZero(feeVo.getLossQuantity()).toString());
						lossProtectDataVo.setUnderDefLoss(NullToZero(feeVo.getSumVeriLoss()).toString());
						 lossProtectDataVo.setOwner(feeVo.getOwner());
						lossProtectDataVo.setProtectProperty("01".equals(propMainVo.getLossType()) ? "1" : "2");
						lossProtectDataVo.setFieldType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "2" : "3");
						lossProtectDataVo.setEstimateName(propMainVo.getHandlerName());
						lossProtectDataVo.setEstimateCode(propMainVo.getHandlerCode());
						lossProtectDataVo.setEstimateCertiCode(propMainVo.getHandlerIdCard());
						lossProtectDataVo.setUnderWriteName(propMainVo.getUnderWriteName());
						lossProtectDataVo.setUnderWriteCode(propMainVo.getUnderWriteCode());
						lossProtectDataVo.setUnderWriteCertiCode(propMainVo.getUnderWriteIdCard());
						lossProtectDataVo.setEstimateStartTime(propMainVo.getCreateTime());
						Date endDate = propMainVo.getUnderWriteEndDate();
						if(endDate == null){
							endDate = new Date();
						}
						lossProtectDataVo.setUnderEndTime(endDate);
						// lossProtectDataVo.setEstimatePlace(estimatePlace);
						protectDataVos.add(lossProtectDataVo);
					}
				}
			}

			if(lossPersTraceMainVos!=null&&lossPersTraceMainVos.size()>0){
				for(PrpLDlossPersTraceMainVo persTraceMainVo:lossPersTraceMainVos){
				    if("7".equals(persTraceMainVo.getUnderwriteFlag())){
	                    continue;
	                }
					List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
					if( !"10".equals(persTraceMainVo.getCaseProcessType())&&persTraceVoList!=null&&persTraceVoList.size()>0){
						for(PrpLDlossPersTraceVo persTraceVo:persTraceMainVo.getPrpLDlossPersTraces()){
							if("1".equals(persTraceVo.getValidFlag())){//??????????????????????????????????????????
								isPersonInjured = "1";
								sumDefLoss = sumDefLoss.add(NullToZero(persTraceVo.getSumVeriDefloss()));
	
								BiLossPersonDataVo personDataVo = new BiLossPersonDataVo();
								personDataVo.setPersonName(persTraceVo.getPrpLDlossPersInjured().getPersonName());
								SysCodeDictVo sys = codeTranService.findTransCodeDictVo
										("IdentifyType",persTraceVo.getPrpLDlossPersInjured().getCertiType());
								personDataVo.setCertiType(sys==null?"99":sys.getProperty3());
								personDataVo.setCertiCode(persTraceVo.getPrpLDlossPersInjured().getCertiCode());
	
								String lossItemType = persTraceVo.getPrpLDlossPersInjured().getLossItemType();
								personDataVo.setPersonProperty(ciCodeTranService.findTranCodeCode("LossItemType",lossItemType));
								personDataVo.setMedicalType(persTraceVo.getPrpLDlossPersInjured().getTreatSituation());//???????????? ?????????
	//							Date inHospitalDate = persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()
	//									.get(0).getInHospitalDate();
	//							personDataVo.setAddmissionTime(inHospitalDate);
	//							String injuryType = "";
	//							String woundCode = persTraceVo.getPrpLDlossPersInjured().getWoundCode();
	//							if("A".equals(woundCode)){// ???
	//								injuryType = "01";
	//							}else if("B".equals(woundCode)){// ???
	//								injuryType = "02";
	//							}else if("C".equals(woundCode)){// ???
	//								injuryType = "03";
	//							}
								String woundCode = persTraceVo.getPrpLDlossPersInjured().getWoundCode();
								personDataVo.setInjuryType(StringUtils.isEmpty(woundCode) ? "01" : woundCode);
	//							personDataVo.setInjuryType(persTraceVo.getPrpLDlossPersInjured().getWoundCode());
								// personDataVo.setInjuryLevel(injuryLevel);
								personDataVo.setUnderDefLoss(NullToZero(persTraceVo.getSumVeriDefloss()).toString());
								personDataVo.setEstimateName(persTraceMainVo.getOperatorName());
								personDataVo.setEstimateCode(persTraceMainVo.getOperatorCode());
								if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
									personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
								} else {
									personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
								}
								personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
								personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
								personDataVo.setUnderWriteCertiCode(persTraceMainVo.getVerifyCertiCode());
								personDataVo.setEstimateStartTime(persTraceMainVo.getCreateTime());
								personDataVo.setUnderEndTime(persTraceMainVo.getUndwrtFeeEndDate());
								// personDataVo.setEstimateAddr(estimateAddr);
								// personDataVo.setUnderWriteCode(persTraceMainVo.getun);//?????????
								Date deathDate = persTraceVo.getPrpLDlossPersInjured().getDeathTime();
								if(deathDate != null){
									personDataVo.setDeathTime(deathDate);
	//								personDataVo.setDeathTime(persTraceVo.getPrpLDlossPersInjured().getDeathTime());
								}
	
								// ??????????????????????????????
								List<BiLossPersonLossFeeDataVo> feeDataVoList = new ArrayList<BiLossPersonLossFeeDataVo>();
								for(PrpLDlossPersTraceFeeVo persTraceFeeVo:persTraceVo.getPrpLDlossPersTraceFees()){
									BiLossPersonLossFeeDataVo lossFeeDataVo = new BiLossPersonLossFeeDataVo();
									SysCodeDictVo sysVo = codeTranService.findCodeDictVo("FeeType",persTraceFeeVo.getFeeTypeCode());
									if(sysVo != null){
										lossFeeDataVo.setFeeType(sysVo.getProperty1());// ???????????????
									}
									lossFeeDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).toString());
									feeDataVoList.add(lossFeeDataVo);
								}
								personDataVo.setLossFeeDataVos(feeDataVoList);
	
								// ??????????????????
								List<BiLossPersonInjuryDataVo> injuryDataVoList = new ArrayList<BiLossPersonInjuryDataVo>();
								for(PrpLDlossPersExtVo persExtVo:persTraceVo.getPrpLDlossPersInjured()
										.getPrpLDlossPersExts()){
									BiLossPersonInjuryDataVo injuryDataVo = new BiLossPersonInjuryDataVo();
									injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());
									String woundGrade = persExtVo.getWoundGrade();
									if(StringUtils.isNotBlank(woundGrade) && !"10".equals(woundGrade)){
										woundGrade = "0" + woundGrade;
									}else{
										woundGrade = "01";//???????????????
									}
									injuryDataVo.setInjuryLevelCode(woundGrade);
									injuryDataVoList.add(injuryDataVo);
								}
								personDataVo.setInjuryDataVos(injuryDataVoList);
	
								// ??????????????????
								List<BiLossPersonHospitalInfoDataVo> hospitalInfoVoList = new ArrayList<BiLossPersonHospitalInfoDataVo>();
								// ???????????????????????????????????????????????????????????????????????????
								for(PrpLDlossPersHospitalVo persHospitalVo:persTraceVo.getPrpLDlossPersInjured().getPrpLDlossPersHospitals()){
									BiLossPersonHospitalInfoDataVo hospitalInfoDataVo = new BiLossPersonHospitalInfoDataVo();
									String hospitalName = persHospitalVo.getHospitalName();
									if(StringUtils.isBlank(hospitalName)){
										hospitalName = "????????????????????????";
									}
									//??????????????????????????????
									hospitalName = StringOperationUtil.cutOutString(hospitalName,30);
									hospitalInfoDataVo.setHospitalName(hospitalName);
									personDataVo.setEstimateAddr(hospitalName);
									if(persHospitalVo.getInHospitalDate() != null){
									    personDataVo.setAddmissionTime(formate.format(persHospitalVo.getInHospitalDate()));
									}
									// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.getHospitalCode());
									hospitalInfoVoList.add(hospitalInfoDataVo);
								}
								// ???????????????????????????????????????????????????????????????????????????
								if(hospitalInfoVoList == null || hospitalInfoVoList.size() == 0){
									BiLossPersonHospitalInfoDataVo hospitalInfoDataVo = new BiLossPersonHospitalInfoDataVo();
									hospitalInfoDataVo.setHospitalName("????????????????????????");
									// hospitalInfoDataVo.setHospitalFactoryCertiCode(persHospitalVo.get);
									hospitalInfoVoList.add(hospitalInfoDataVo);
								}
								personDataVo.setHospitalInfoDataVos(hospitalInfoVoList);
	
								// ????????????????????????,,,--????????????????????????????????????????????????????????????????????????
								//if("02".equals(persTraceVo.getPrpLDlossPersInjured().getWoundCode())){
									List<BiLossPersonInjuryIdentifyInfoDataVo> identfyVoList = new ArrayList<BiLossPersonInjuryIdentifyInfoDataVo>();
									BiLossPersonInjuryIdentifyInfoDataVo identifyInfoDataVo = new BiLossPersonInjuryIdentifyInfoDataVo();
									String chkName = persTraceVo.getPrpLDlossPersInjured().getChkComName();
									if(StringUtils.isEmpty(chkName)){
										chkName = "??????????????????????????????";
									}
									chkName = StringOperationUtil.cutOutString(chkName,30);
									identifyInfoDataVo.setInjuryIdentifyName(chkName);
									//identifyInfoDataVo.setInjuryIdentifyCertiCode(persTraceVo.getPrpLDlossPersInjured().getChkComCode());
									identfyVoList.add(identifyInfoDataVo);
									personDataVo.setInjuryIdentifyInfoDataVos(identfyVoList);
								//}
								
								//
								personDataVos.add(personDataVo);
							}
						}
					}
				}
			}

			// ??????????????????
			List<BiLossSubrogationDataVo> subrogationDataVos = new ArrayList<BiLossSubrogationDataVo>();
			if("1".equals(subrogationMainVo.getSubrogationFlag())){
				if(subrogationMainVo.getPrpLSubrogationCars()!=null&&subrogationMainVo.getPrpLSubrogationCars().size()>0){
					for(PrpLSubrogationCarVo carVo:subrogationMainVo.getPrpLSubrogationCars()){
						BiLossSubrogationDataVo subrogationDataVo = new BiLossSubrogationDataVo();
						subrogationDataVo.setLinkerName(carVo.getLinkerName());
						subrogationDataVo.setLicensePlateNo(carVo.getLicenseNo());
						String licenseType = carVo.getLicenseType();
						if("25".equals(licenseType) || licenseType==null){
							licenseType = "99";
						}
						subrogationDataVo.setLicensePlateType(licenseType);
						subrogationDataVo.setVIN(carVo.getVinNo());
						subrogationDataVo.setEngineNo(carVo.getEngineNo());
						subrogationDataVo.setCaInsurerCode(carVo.getBiInsurerCode());
						subrogationDataVo.setCaInsurerArea(carVo.getBiInsurerArea());
						subrogationDataVo.setIaInsurerCode(carVo.getCiInsurerCode());
						subrogationDataVo.setIaInsurerArea(carVo.getCiInsurerArea());
						subrogationDataVos.add(subrogationDataVo);
					}
				}
				
				if(subrogationMainVo.getPrpLSubrogationPersons()!=null&&subrogationMainVo.getPrpLSubrogationPersons().size()>0){
					for(PrpLSubrogationPersonVo personVo : subrogationMainVo.getPrpLSubrogationPersons()){
						BiLossSubrogationDataVo subrogationDataVo = new BiLossSubrogationDataVo();
						subrogationDataVo.setLinkerName(personVo.getName());
						subrogationDataVos.add(subrogationDataVo);
					}
				}
			}

			String sumTot = new DecimalFormat("######0.00").format(sumDefLoss);
			baseVo.setUnderTotalDefLoss(sumDefLoss.compareTo(BigDecimal.ZERO)==0 ? "0.0":sumDefLoss.toString());
			baseVo.setIsPersonInjured(isPersonInjured);
			baseVo.setIsSingleAccident(carSum > 1 ? "0" : "1");
			
			bodyVo.setLossBaseVo(baseVo);
			bodyVo.setVehicleDataVos(vehicleDataVos);
			bodyVo.setProtectDataVos(protectDataVos);
			bodyVo.setPersonDataVos(personDataVos);
			bodyVo.setSubrogationDataVos(subrogationDataVos);
			controller.callPlatform(bodyVo);

		}
	}

	private String getCertiType(String registNo,String reqVal) {
		PrpLCertifyMainVo certify = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
		String returnVal1 = "1";
		String returnVal2 = "0";
		if(certify!=null){
			List<PrpLCertifyItemVo> itemVoList = certify.getPrpLCertifyItems();
			if(itemVoList!=null&&itemVoList.size()>0){
				for(PrpLCertifyItemVo itemVo:itemVoList){
					List<PrpLCertifyDirectVo> directVoList = itemVo.getPrpLCertifyDirects();
					for(PrpLCertifyDirectVo directVo:directVoList){
						String code = directVo.getLossItemCode();
						if("C0201".equals(code)){
							returnVal1 = "1";
						}else if("C0202".equals(code)){
							returnVal1 = "2";
						}else if("C0203".equals(code)){
							returnVal1 = "3";
						}
						if("C0102".equals(code)){
							returnVal2 = "1";
						}
					}
				}
			}
		}
		return "1".equals(reqVal) ? returnVal1 : returnVal2;
	}
	
	
	//------------------------------????????????--------------------------------------//
	
	public void sendLossToPlatFormSH(String registNo,String riskCode) {

		// ?????????????????????????????????
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = traceService.findPersTraceMainVo(registNo);
		// ??????????????????
		boolean vLoss = true;
		if(prpLdlossPropMainVoList!=null){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				if(!CodeConstants.VeriFlag.PASS.equals(prpLdlossPropMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLdlossPropMainVo.getUnderWriteFlag())){
					vLoss = false;
				}
			}
		}

		if(prpLDlossCarMainVoList!=null){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag()) &&
			    		!CodeConstants.VeriFlag.CANCEL.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
					vLoss = false;
				}
			}
		}

		if(vLoss){// ?????????????????????????????? ????????????
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),
					FlowNode.DLProp.name(),FlowNode.DLCar.name());
			if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
				for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
					if( !prpLWfTaskVo.getSubNodeCode().equals(FlowNode.DLChk.name())){
						vLoss = false;
						break;
					}
				}
			}
		}
		 //??????????????????
		   boolean pLoss = true;
		   if(prpLDlossPersTraceMainVoList != null){
			   for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
				   if(!prpLDlossPersTraceMainVo.getAuditStatus().equals(CodeConstants.AuditStatus.SUBMITCHARGE) &&
				           !CodeConstants.UnderWriteFlag.CANCELFLAG.equals(prpLDlossPersTraceMainVo.getUnderwriteFlag())){
					   pLoss = false;
				   }
			   } 
		   }
		   if(pLoss){//???????????????????????????????????????
			   List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.PLoss.name());
			   if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
				   pLoss =  false;
			   }
		   }


		if(vLoss&&pLoss){// ???????????????????????????????????????????????????????????????
			List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
			List<PrpLEndCaseVo> endCaseVoList = endCasePubService.queryAllByRegistNo(registNo);
			if(endCaseVoList == null || endCaseVoList.size() <= 0){//????????????
				for(PrpLCMainVo cMainVo : cMainVoList){
					if(riskCode!=null&&!riskCode.equals(cMainVo.getRiskCode())){
						// ??????????????????????????????????????????????????????
						continue;
					}
					String code = Risk.DQZ.equals(cMainVo.getRiskCode())?
					RequestType.RegistInfoCI_SH.getCode():RequestType.RegistInfoBI_SH.getCode();
					CiClaimPlatformLogVo logVo = platformLogService.findLogByBussNo(code,registNo,cMainVo.getComCode());
					
					String claimSeqNo = logVo==null?"":logVo.getClaimSeqNo();
					String comCode = logVo.getComCode();
					this.sendLossToSHPlatform(claimSeqNo,comCode,cMainVo);
					
					if(SendPlatformUtil.isMor(cMainVo)){
						//????????????  ???????????????
						SysUserVo userVo = new SysUserVo();
						userVo.setComCode("00000000");
						userVo.setUserCode("0000000000");
						String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
						if(warnswitch.contains(cMainVo.getComCode().substring(0,2))){
							// ???????????????????????????????????????????????????
							PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,cMainVo.getPolicyNo());// ??????????????????
							try {
								if("2".equals(prpLClaimVo.getValidFlag())&&prpLClaimVo.getComCode().startsWith("10")){
									// ?????????????????????
									logger.info("????????????:??????????????????????????????????????????????????????"+registNo);
								}else{
									interfaceAsyncService.sendDlossRegister(cMainVo,userVo);
								}
							} catch (Exception e) {
								logger.error("?????????registno=" + registNo + "?????????????????????????????????-------------->" , e);
							}
						}
					}
				}
			}
			
			//????????????????????????taskid
			SysUserVo userVo = new SysUserVo();
			userVo.setComCode("00000000");
			userVo.setUserCode("0000000000");
	        List<PrpLWfTaskVo> vLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.VLoss.toString());
	        List<PrpLWfTaskVo> pLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(registNo, FlowNode.PLoss.toString());
	        List<PrpLWfTaskVo> allVolist = new ArrayList<PrpLWfTaskVo>();
	        if(vLossVolist != null && vLossVolist.size() > 0){
	        	allVolist.add(vLossVolist.get(0));
	        }
	        if(pLossVolist != null && pLossVolist.size() > 0){
	        	allVolist.add(pLossVolist.get(0));
	        }
	        //??????????????????
			Collections.sort(allVolist, new Comparator<PrpLWfTaskVo>() {
			@Override
			public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
					return o2.getTaskOutTime().compareTo(o1.getTaskOutTime());
				}
			});
            interfaceAsyncService.sendCarLossForGenilex(registNo,allVolist.get(0).getTaskId().toString(), userVo);
			interfaceAsyncService.carRiskImagesUpdate(registNo,userVo,riskCode,null);
		}else{
			logger.info("?????????=" + registNo + "???????????????????????????????????????");
		}

	}


	/**
	 * ???????????????????????? -- ???????????????
	 * @param registNo
	 * @throws Exception
	 */
	public void sendLossToSHPlatform(String claimSeqNo,String comCode,PrpLCMainVo cMainVo) {
		PlatformController controller = null;
		if(Risk.DQZ.equals(cMainVo.getRiskCode())){// ??????
			controller = PlatformFactory.getInstance(comCode,RequestType.LossInfoCI_SH);
			SHCILossReqBodyVo request_BodyVo = this.setBodyCI_SH(claimSeqNo,cMainVo);
			// ????????????
			controller.callPlatform(request_BodyVo);
		}else{// ??????
			controller = PlatformFactory.getInstance(comCode,RequestType.LossInfoBI_SH);
			SHBILossReqBodyVo request_BodyVo = this.setBodyBI_SH(claimSeqNo,cMainVo);
			// ????????????
			controller.callPlatform(request_BodyVo);
		}

		// ??????
//		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
//		if( !"1".equals(resHeadVo.getResponseCode())){
//			logger.info("?????????????????????????????????");
////			throw new IllegalArgumentException("?????????????????????????????????");
//		}
//		SHCILossResBodyVo response_BodyVo = controller.getBodyVo(SHCILossResBodyVo.class);
		// String claimCode =
		// response_BodyVo.getResponse_BasePartVo().getClaimCode();
		// this.saveResposeInfo(claimCode,registNo,cMainVo);*/
	}

	private List<PrpLDlossPersTraceMainVo> findPersTraceMainVo(String registNo){
		List<PrpLDlossPersTraceMainVo> persTraceMainVos = persTraceService.findPersTraceMainVo(registNo);
		if(persTraceMainVos != null && persTraceMainVos.size() > 0){
			for(PrpLDlossPersTraceMainVo mainVo : persTraceMainVos){
				List<PrpLDlossPersTraceVo> traceVoList = persTraceService.findPersTraceVo(registNo,mainVo.getId());
				mainVo.setPrpLDlossPersTraces(traceVoList);
			}
		}
		return persTraceMainVos;
	}
	
	/**
	 * ?????????????????????????????????????????????
	 * @param registNo
	 * @return
	 * @modified: ???Luwei(2016???5???26??? ??????7:37:32): <br>
	 */
	public SHCILossReqBodyVo setBodyCI_SH(String claimSeqNo,PrpLCMainVo cMainVo) {
		String registNo = cMainVo.getRegistNo();
		SHCILossReqBodyVo bodyVo = new SHCILossReqBodyVo();

		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
		// PrpLCItemCarVo cItemCarVo = policyViewService.findItemCarByRegistNoAndPolicyNo(registNo,policyNo);
		PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PrpLClaimVo claimVo = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,cMainVo.getPolicyNo(),"1").get(0);
		//???????????????
		PrpLDlossCarMainVo mainCarVo = lossCarService.findLossCarMainBySerialNo(registNo, 1).get(0);
		PrpLDlossCarInfoVo prpLDlossCarInfoVo = deflossService.findDefCarInfoByPk(mainCarVo.getCarId());
		// ????????????
		SHCILossReqBasePartVo basePartVo = new SHCILossReqBasePartVo();

		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setManageType(checkVo.getManageType());
		Integer duty = Integer.parseInt(checkDutyVo.getIndemnityDuty())+1;
		basePartVo.setLiabilityAmount(duty.toString());
		basePartVo.setEstimateAmount(this.calculateFeeSum(registNo));
		basePartVo.setConfirmSequenceNo(cMainVo.getValidNo());
		//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????6???
		if("1".equals(mainCarVo.getDeflossCarType()) && prpLDlossCarInfoVo.getLicenseNo()!=null&&prpLDlossCarInfoVo.getLicenseNo().length()>=6&&
				("15".equals(prpLDlossCarInfoVo.getLicenseType())||"16".equals(prpLDlossCarInfoVo.getLicenseType())||
				"17".equals(prpLDlossCarInfoVo.getLicenseType())||"23".equals(prpLDlossCarInfoVo.getLicenseType())||
				"31".equals(prpLDlossCarInfoVo.getLicenseType())||"04".equals(prpLDlossCarInfoVo.getLicenseType())||
				"10".equals(prpLDlossCarInfoVo.getLicenseType()))){
			if(StringUtils.isNotBlank(cItemCarVo.getLicenseNo())){
				basePartVo.setCarMark(cItemCarVo.getLicenseNo().substring(0, 6));
			}else{
				basePartVo.setCarMark(prpLDlossCarInfoVo.getLicenseNo().substring(0, 6));
			}
			
		}else{
			
		   basePartVo.setCarMark(cItemCarVo.getLicenseNo());
		}
		basePartVo.setVehicleType(cItemCarVo.getLicenseKindCode());
		basePartVo.setSubrogationFlag(claimVo==null ? "" : claimVo.getIsSubRogation());
		basePartVo.setRemark(registVo.getPrpLRegistExt().getDangerRemark());
		basePartVo.setReportNo(registNo);
		
		BigDecimal  underDefLoss = BigDecimal.ZERO;
		String isPersonInjured = "0"; 
		String isProtectLoss = "0";
		String isSingleAccident = "0";
		
		// ??????????????????????????????
		List<SHCILossReqPersonDataVo> personDataVos = new ArrayList<SHCILossReqPersonDataVo>();

		List<PrpLDlossPersTraceMainVo> persTraceMainVos = findPersTraceMainVo(registNo);
		if(persTraceMainVos !=null && persTraceMainVos.size()>0 ){
		for(PrpLDlossPersTraceMainVo persTraceMainVo : persTraceMainVos){
		    if("7".equals(persTraceMainVo.getUnderwriteFlag())){
                continue;
            }
			List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
			if( !"10".equals(persTraceMainVo.getCaseProcessType())&&persTraceVoList!=null&&persTraceVoList.size()>0){
				isPersonInjured = "1";
			for(PrpLDlossPersTraceVo dlossPersTraceVo : persTraceVoList){
				if("1".equals(dlossPersTraceVo.getValidFlag())){//??????????????????????????????????????????
					PrpLDlossPersInjuredVo persInjuredVo = dlossPersTraceVo.getPrpLDlossPersInjured();
					// ??????????????????
					SHCILossReqPersonDataVo personDataVo = new SHCILossReqPersonDataVo();
					personDataVo.setPersonName(persInjuredVo.getPersonName());
					personDataVo.setPersonId(persInjuredVo.getCertiCode());
					personDataVo.setAge(persInjuredVo.getPersonAge().intValue());
					personDataVo.setLossAmount(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).toString());
					personDataVo.setMainThird("1".equals(dlossPersTraceVo.getLossFeeType()) ? "1" : "0");
					personDataVo.setSurveyType("0".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "3" : "1");
					personDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
					personDataVo.setSurveyStartTime(checkVo.getCreateTime());
					personDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
					personDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
					personDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
	//				personDataVo.setEstimateName(persTraceMainVo.getCreateUser());
					String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
					if(StringUtils.isEmpty(codeCName)){
						codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
					}
					personDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
					personDataVo.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
					personDataVo.setEstimateEndTime(persTraceMainVo.getUndwrtFeeEndDate());
					personDataVo.setAssesorName(persTraceMainVo.getUndwrtFeeName());
					personDataVo.setAssesorStartTime(persTraceMainVo.getCreateTime());
					personDataVo.setAssesorEndTime(persTraceMainVo.getUpdateTime());
					personDataVo.setInjuryType("");
					//personDataVo.setInjuryLevel(persInjuredVo.getWoundCode());
					personDataVo.setMedicalType(persInjuredVo.getTreatSituation());
					personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
					personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
					personDataVo.setEstimateAddr("");
	//				personDataVo.setAddmissionTime(persInjuredVo.getPrpLDlossPersHospitals()==null ? null : persInjuredVo
	//						.getPrpLDlossPersHospitals().get(0).getInHospitalDate());
					personDataVo.setAddmissionTime(null);
					personDataVo.setUnderDefloss(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).toString());
					
					personDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
					personDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
					if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
						personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
					} else {
						personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
					}
					personDataVo.setDeathTime(dlossPersTraceVo.getPrpLDlossPersInjured().getDeathTime());
					personDataVo.setCountryInjuryType(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode());
					
					underDefLoss = underDefLoss.add(NullToZero(dlossPersTraceVo.getSumVeriDefloss()));
					
					// ????????????????????????????????????
					List<SHCILossReqPersonLossPartDataVo> partDataListVo = new ArrayList<SHCILossReqPersonLossPartDataVo>();
					for(PrpLDlossPersTraceFeeVo persTraceFeeVo:dlossPersTraceVo.getPrpLDlossPersTraceFees()){
						SHCILossReqPersonLossPartDataVo partDataVo = new SHCILossReqPersonLossPartDataVo();
						SysCodeDictVo vo = codeTranService.findCodeDictVo("SHFeetype",persTraceFeeVo.getFeeTypeCode());
						partDataVo.setFeeType(vo != null ? vo.getProperty2() : "");// SHFeetype
						partDataVo.setLossAmount(NullToZero(persTraceFeeVo.getVeriDefloss()).toString());
						partDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).toString());
						partDataListVo.add(partDataVo);
					}
					personDataVo.setLossPartDataVo(partDataListVo);
	
					// ??????????????????????????????
					List<SHCILossReqInjuryDataVo> injuryDataListVo = new ArrayList<SHCILossReqInjuryDataVo>();
					for(PrpLDlossPersExtVo persExtVo:persInjuredVo.getPrpLDlossPersExts()){
						SHCILossReqInjuryDataVo injuryDataVo = new SHCILossReqInjuryDataVo();
						injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());//
						String injuryLevelCode =persExtVo.getWoundGrade();
						if(StringUtils.isBlank(persExtVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
							injuryLevelCode ="10";
						}else{
							injuryLevelCode ="0" + injuryLevelCode;
						}
						injuryDataVo.setInjuryLevelCode(injuryLevelCode); //??????????????????
						injuryDataListVo.add(injuryDataVo);
					}
					personDataVo.setInjuryDataVo(injuryDataListVo);
	
					// ??????????????????????????????
					if("2".equals(persInjuredVo.getTreatSituation())){
					    List<SHCILossReqHospitalInfoDataVo> hospitalInfoDataListVo = new ArrayList<SHCILossReqHospitalInfoDataVo>();
		                for(PrpLDlossPersHospitalVo hospitalVo:persInjuredVo.getPrpLDlossPersHospitals()){
		                    SHCILossReqHospitalInfoDataVo hospitalInfoDataVo = new SHCILossReqHospitalInfoDataVo();
		                    String hospitalName = StringOperationUtil.cutOutString(hospitalVo.getHospitalName(),30);
		                    hospitalInfoDataVo.setHospitalName(hospitalName);
		                    //hospitalInfoDataVo.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());
		                    
		                    hospitalInfoDataListVo.add(hospitalInfoDataVo);
		                }
		                personDataVo.setHospitalInfoDataVo(hospitalInfoDataListVo);
					}
					
	                //??????????????????
					if("02".equals(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode())){
					    List<SHCILossReqInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCILossReqInjuryIdentifyInfoDataVo>();
		                SHCILossReqInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCILossReqInjuryIdentifyInfoDataVo();
		                String chkComName = StringOperationUtil.cutOutString(persInjuredVo.getChkComName(),30);
		                injuryIdentifyInfoDataVo.setInjuryIdentifyName(chkComName);
		                injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persInjuredVo.getChkComCode());
		                ideInfoDataList.add(injuryIdentifyInfoDataVo);
		                personDataVo.setIdentifyInfoDataVo(ideInfoDataList);
					}
					personDataVos.add(personDataVo);
				}
			}
		}
		}
		}

		// ??????????????????????????????
		List<SHCILossReqVehicleDataVo> vehicleDataListVo = new ArrayList<SHCILossReqVehicleDataVo>();
		List<PrpLDlossCarMainVo> dlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLDlossCarMainVo> newVoList = getDlossCarMainList(dlossCarMainVos);
		if(newVoList != null && newVoList.size() > 0){
		for(PrpLDlossCarMainVo dlossCarMainVo : newVoList){
			isSingleAccident = "1";
			//?????????????????????????????????????????????????????????????????????????????????
			if(CetainLossType.DEFLOSS_NULL.equals(dlossCarMainVo.getCetainLossType()) ||
					CodeConstants.VeriFlag.CANCEL.equals(dlossCarMainVo.getUnderWriteFlag())){
				continue;
			}
			//???????????????????????????0?????????????????????????????????????????????????????????
			if(NullToZero(dlossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
				continue;
			}
			PrpLDlossCarInfoVo carInfoVo = dlossCarMainVo.getLossCarInfoVo();
			// ??????????????????
			SHCILossReqVehicleDataVo vehicleDataVo = new SHCILossReqVehicleDataVo();
			//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????6???
			if(carInfoVo.getLicenseNo()!=null&&carInfoVo.getLicenseNo().length()>=6&&
					("15".equals(carInfoVo.getLicenseType())||"16".equals(carInfoVo.getLicenseType())||
					"17".equals(carInfoVo.getLicenseType())||"23".equals(carInfoVo.getLicenseType())||
					"31".equals(carInfoVo.getLicenseType())||"04".equals(carInfoVo.getLicenseType())||
					"10".equals(carInfoVo.getLicenseType()))){
				if("1".equals(dlossCarMainVo.getDeflossCarType()) && StringUtils.isNotBlank(cItemCarVo.getLicenseNo())){
					vehicleDataVo.setCarMark(cItemCarVo.getLicenseNo().substring(0, 6));
				}else{
					vehicleDataVo.setCarMark(dlossCarMainVo.getLicenseNo().substring(0, 6));
				}
				
			}else{//?????????????????????????????????
				if("1".equals(dlossCarMainVo.getDeflossCarType())){
					vehicleDataVo.setCarMark(cItemCarVo.getLicenseNo());
				}else{
					vehicleDataVo.setCarMark(dlossCarMainVo.getLicenseNo());
				}
				
			}
//			vehicleDataVo.setCarMark("1".equals(dlossCarMainVo.getDeflossCarType()) ? cItemCarVo.getLicenseNo():carInfoVo.getLicenseNo());
			SysCodeDictVo dictVo = new SysCodeDictVo();
			if(!StringUtils.isBlank(carInfoVo.getLicenseType())){
				dictVo = codeTranService.findCodeDictVo("LicenseKindCode",carInfoVo.getLicenseType());
			}else{
				dictVo = codeTranService.findCodeDictVo("LicenseKindCode","02");
			}
			vehicleDataVo.setVehicleType("1".equals(dlossCarMainVo.getDeflossCarType()) ? cItemCarVo.getLicenseKindCode():dictVo.getProperty1());
			vehicleDataVo.setLossAmount(NullToZero(dlossCarMainVo.getSumVeriLossFee()).toString());//
			vehicleDataVo.setMainThird("1".equals(dlossCarMainVo.getDeflossCarType()) ? "1" : "0");//
			vehicleDataVo.setRobber("03".equals(dlossCarMainVo.getCetainLossType()) ? "1" : "0");
			vehicleDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
			vehicleDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
			vehicleDataVo.setSurveyStartTime(checkVo.getCreateTime());
			vehicleDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
			vehicleDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
			vehicleDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
			String codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getHandlerCode());
			if(StringUtils.isEmpty(codeCName)){
				codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getCreateUser());
			}
			vehicleDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
//			vehicleDataVo.setEstimateName(dlossCarMainVo.getHandlerCode());
//			vehicleDataVo.setEstimateName("GSU0000001");
			vehicleDataVo.setEstimateStartTime(dlossCarMainVo.getCreateTime());// ??????????????????
			vehicleDataVo.setEstimateEndTime(dlossCarMainVo.getDefEndDate());// ??????????????????
			vehicleDataVo.setAssesorName(dlossCarMainVo.getUnderWriteName());
			vehicleDataVo.setAssesorStartTime(null);// ??????????????????
			vehicleDataVo.setAssesorEndTime(dlossCarMainVo.getUnderWriteEndDate());// ??????????????????
			vehicleDataVo.setRemnant("0.0");
			vehicleDataVo.setCharg4eFee("0.0");
			BigDecimal repairSum = BigDecimal.ZERO;
			for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
				repairSum = repairSum.add(NullToZero(carRepairVo.getSumVeriLoss()));//
			}
			vehicleDataVo.setTotalWorkingHour(repairSum.compareTo(BigDecimal.ZERO)==0 ? "0.0":repairSum.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
			
			String flag = (dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0)
					||(dlossCarMainVo.getPrpLDlossCarComps()!=null&&dlossCarMainVo.getPrpLDlossCarComps().size()>0) ? "1" : "0";
			vehicleDataVo.setChangeOrRepairPart(flag);
			vehicleDataVo.setJyVehicleCode(this.jy_vehicle_CodeConvert(carInfoVo.getModelCode()));
			vehicleDataVo.setClaimVehicleCode(carInfoVo.getModelCode());
			vehicleDataVo.setClaimVehicleName(carInfoVo.getModelName());
			String facName = dlossCarMainVo.getRepairFactoryName();
			//????????????????????????????????????????????????????????????
			if(StringUtils.isNotBlank(facName) && facName.length()>25){
				facName=facName.substring(0,25);
			}
			vehicleDataVo.setMadeFactory(StringUtils.isNotBlank(facName) ? facName : "??????");
			vehicleDataVo.setVehicleBrandCode(carInfoVo.getBrandCode());
			vehicleDataVo.setVehicleCatenaCode(carInfoVo.getSeriCode());
			vehicleDataVo.setVehicleGroupCode(carInfoVo.getGroupCode());
			if(StringUtils.isNotEmpty(dlossCarMainVo.getRepairFactoryType())){
				vehicleDataVo.setPriceSltCode(codeTranService.findTransCodeDictVo("RepairFactoryType",
						dlossCarMainVo.getRepairFactoryType()).getProperty2());
			}else{
				vehicleDataVo.setPriceSltCode("02");
			}
			String defineFlag = dlossCarMainVo.getPrpLDlossCarComps().size()>0 
					? "1".equals(dlossCarMainVo.getPrpLDlossCarComps().get(0).getSelfConfigFlag()) ? "1" : "0" : "0";
			vehicleDataVo.setDefineFlag(defineFlag);
			vehicleDataVo.setVin(carInfoVo.getVinNo());
			vehicleDataVo.setEngineNo(carInfoVo.getEngineNo());
			vehicleDataVo.setModel(carInfoVo.getBrandName());
			vehicleDataVo.setDriverName(carInfoVo.getDriveName());
			String prop3 = codeTranService.findTransCodeDictVo("IdentifyType",carInfoVo.getIdentifyType()).getProperty4();
			vehicleDataVo.setCertiType(prop3);
			vehicleDataVo.setCertiCode(carInfoVo.getIdentifyNo());
			vehicleDataVo.setDriverLicenseNo(carInfoVo.getDrivingLicenseNo());
			vehicleDataVo.setTemporaryFlag(StringUtils.isEmpty(cItemCarVo.getLicenseNo()) ? "1" : "0");
			
			vehicleDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
			vehicleDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
			vehicleDataVo.setUnderWriteCode(dlossCarMainVo.getUnderWriteCode());
			vehicleDataVo.setUnderWriteCertiCode(dlossCarMainVo.getUnderWiteIdNo());
			vehicleDataVo.setUnderDefLoss(NullToZero(dlossCarMainVo.getSumVeriChargeFee()).toString());
			
			underDefLoss = underDefLoss.add(NullToZero(dlossCarMainVo.getSumVeriChargeFee()));
			// ??????????????????????????????
			List<SHCILossReqCarLossPartDataVo> carLossPartDataListVo = new ArrayList<SHCILossReqCarLossPartDataVo>();
			for(PrpLDlossCarMainVo carMainVo : newVoList){
				// ????????????????????????
				String lossPartStr = carMainVo.getLossPart();
				if(StringUtils.isNotBlank(lossPartStr)){
					String[] lossPartArray = lossPartStr.split(",");
					for(int i = 0; i<lossPartArray.length; i++ ){
						SHCILossReqCarLossPartDataVo lossPartDataVo = new SHCILossReqCarLossPartDataVo();
						String lossPart = codeTranService.findTransCodeDictVo("LossPart",lossPartArray[i]).getProperty3();
						lossPartDataVo.setLossPart(lossPart);
						carLossPartDataListVo.add(lossPartDataVo);
					}
				}
			}
			vehicleDataVo.setCarLossPartDataVo(carLossPartDataListVo);

			// ??????????????????????????????
			List<SHCILossReqFittingDataVo> fittingDataListVo = new ArrayList<SHCILossReqFittingDataVo>();
			if(dlossCarMainVo.getPrpLDlossCarComps()!=null&&dlossCarMainVo.getPrpLDlossCarComps().size()>0&&"1".equals(flag)){
				for(PrpLDlossCarCompVo carCompVo:dlossCarMainVo.getPrpLDlossCarComps()){// ??????
					SHCILossReqFittingDataVo dataVo = new SHCILossReqFittingDataVo();
					dataVo.setChangePartName(carCompVo.getCompName());
					dataVo.setChangePartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()));
					dataVo.setChangePartFee(NullToZero(carCompVo.getManHourUnitPrice()).toString());
					dataVo.setChangePartTime(NullToZero(carCompVo.getManHour()).toString());
					dataVo.setChangePartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).toString());
					dataVo.setRepairPartName(carCompVo.getCompName());
					dataVo.setRepairPartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()));
					dataVo.setRepairPartFee(NullToZero(carCompVo.getManHourUnitPrice()).toString());
					dataVo.setRepairPartTime(NullToZero(carCompVo.getManHour()).toString());
					dataVo.setRepairPartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).toString());
					dataVo.setRepairMethord(null);

					dataVo.setJyPartCode(carCompVo.getCompCode());
					dataVo.setOemPartCode(StringUtils.isEmpty(carCompVo.getOriginalId()) ? "???" : carCompVo.getOriginalId());
					dataVo.setDefineFlag("1".equals(carCompVo.getSelfConfigFlag()) ? "1" : "0");

					dataVo.setSubjionFlag("0");

					fittingDataListVo.add(dataVo);
				}
			}
			if(dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0&&"1".equals(flag)){
				for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){// ??????
					SHCILossReqFittingDataVo dataVo = new SHCILossReqFittingDataVo();
					String compName = carRepairVo.getCompName();
					if(compName.length()>50){
						compName = compName.substring(0, 50);
					}
					dataVo.setChangePartName(compName);
					dataVo.setChangePartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
					dataVo.setChangePartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).toString());
					dataVo.setChangePartTime(NullToZero(carRepairVo.getVeriManHour()).toString());
					dataVo.setChangePartManpowerFee(NullToZero(carRepairVo.getVeriManUnitPrice()).toString());
					dataVo.setRepairPartName(carRepairVo.getPartName());
					dataVo.setRepairPartNum(NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
					dataVo.setRepairPartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).toString());
					dataVo.setRepairPartTime(NullToZero(carRepairVo.getVeriManHour()).toString());
					dataVo.setRepairPartManpowerFee(NullToZero(carRepairVo.getVeriManHourFee()).toString());

					dataVo.setRepairMethord(carRepairVo.getRepairType());

					dataVo.setJyPartCode(carRepairVo.getCompCode());
					dataVo.setOemPartCode(StringUtils.isEmpty(carRepairVo.getPartCode()) ? "???" : carRepairVo.getPartCode());
					dataVo.setDefineFlag("1".equals(carRepairVo.getSelfConfigFlag()) ? "1" : "0");

					dataVo.setSubjionFlag("0");

					fittingDataListVo.add(dataVo);
				}
			}
			
			// for(PrpLDlossCarMaterialVo carMaterialVo :dlossCarMainVo.getPrpLDlossCarMaterials()){//??????
			// SH_CI_Request_FittingDataVo dataVo = new SH_CI_Request_FittingDataVo();
			// dataVo.setChangePartName(null);
			// dataVo.setChangePartNum(null);
			// dataVo.setChangePartFee(carMaterialVo.getAuditLossPrice().doubleValue());
			// dataVo.setChangePartTime(null);
			// dataVo.setChangePartManpowerFee(null);
			// dataVo.setRepairPartName(null);
			// dataVo.setRepairPartNum(null);
			// dataVo.setRepairPartFee(null);
			// dataVo.setRepairPartTime(null);
			// dataVo.setRepairPartManpowerFee(null);
			//
			// dataVo.setRepairMethord(carMaterialVo.get.getRepairType());
			//
			// dataVo.setJyPartCode(carCompVo.getCompCode());
			// dataVo.setOemPartCode(carMaterialVo.getAssistId());
			// dataVo.setDefineFlag("1".equals(carMaterialVo.getSelfConfigFlag()) ? "1" : "0");
			//
			// dataVo.setSubjionFlag("0");
			// }

			vehicleDataVo.setFittingDataVo(fittingDataListVo);
			
			if("1".equals(flag)){
				List<SHCILossReqRepairDataVo> repartDataVoList = new ArrayList<SHCILossReqRepairDataVo>();
				SHCILossReqRepairDataVo repartVo = new SHCILossReqRepairDataVo();
//				repartVo.setRepairOrg("JY00008782");
				repartVo.setRepairOrg("");
				repartDataVoList.add(repartVo);
				vehicleDataVo.setRepairDataVo(repartDataVoList);
			}
			if("1".equals(flag)){
				List<SHCILossReqActuralRepairDataVo> acturalRepairList = new ArrayList<SHCILossReqActuralRepairDataVo>();
				SHCILossReqActuralRepairDataVo repairDataVo = new SHCILossReqActuralRepairDataVo();
//				repairDataVo.setActuralRepairOrg("JY00008782");
				repairDataVo.setActuralRepairOrg("");
				acturalRepairList.add(repairDataVo);
				vehicleDataVo.setActuralRepairDataVo(acturalRepairList);
			}

//			// ??????????????????????????????
//			List<SHCILossReqRepairDataVo> repairDataListVo = new ArrayList<SHCILossReqRepairDataVo>();
//			if(dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0){
//				for(PrpLDlossCarRepairVo repairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
//					SHCILossReqRepairDataVo repairDataVo = new SHCILossReqRepairDataVo();
//					repairDataVo.setRepairOrg(repairVo.getRepairCode());
//				}
//			}
//			
//			vehicleDataVo.setRepairDataVo(repairDataListVo);
//
//			// ??????????????????????????????
//			vehicleDataVo.setActuralRepairDataVo(null);

			// ??????
			vehicleDataListVo.add(vehicleDataVo);
		}
		}

		// ??????????????????????????????
		List<SHCILossReqObjDataVo> objDataListVo = new ArrayList<SHCILossReqObjDataVo>();
		List<PrpLdlossPropMainVo> propLossVos = propTaskService.findPropMainListByRegistNo(registNo);
		if (propLossVos != null && propLossVos.size() > 0) {
			for (PrpLdlossPropMainVo propLossMainVo : propLossVos) {
				isProtectLoss = "1";
				//???????????????????????????
				if (propLossMainVo.getPrpLdlossPropFees() == null ||
						CodeConstants.VeriFlag.CANCEL.equals(propLossMainVo.getUnderWriteFlag())){
					continue;
				}
				for (PrpLdlossPropFeeVo propFee : propLossMainVo.getPrpLdlossPropFees()) {
					// ??????????????????
					SHCILossReqObjDataVo objDataVo = new SHCILossReqObjDataVo();
					String cutOutLossItemName = StringOperationUtil.cutOutString(propFee.getLossItemName(),30);
					objDataVo.setObjectDesc(cutOutLossItemName);
					objDataVo.setLossNum(propFee.getLossQuantity().intValue());
					objDataVo.setLossAmount(NullToZero(propFee.getVeriUnitPrice()).toString());
					objDataVo.setMainThird("1".equals(propLossMainVo.getLossType()) ? "1" : "0");
					objDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1": "3");
					objDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
					objDataVo.setSurveyStartTime(checkVo.getCreateTime());
					objDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
					objDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
					objDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
					String codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getHandlerCode());
					if(StringUtils.isBlank(codeCName)){
						codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getCreateUser());
					}
					objDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
//					objDataVo.setEstimateName(propLossMainVo.getHandlerCode());
//					objDataVo.setEstimateName("GSU0000001");
					objDataVo.setEstimateStartTime(propLossMainVo.getCreateTime());
					objDataVo.setEstimateEndTime(propLossMainVo.getUpdateTime());
					objDataVo.setAssesorName(propLossMainVo.getUnderWriteName());
					objDataVo.setAssesorStartTime(propLossMainVo.getUpdateTime());
					objDataVo.setAssesorEndTime(propLossMainVo.getUnderWriteEndDate());
					
					String protectProperty = "1";
					if("1".equals(propLossMainVo.getLossType())){
						protectProperty = "1";  //????????????
					}else{
						protectProperty = "2";  // ????????????
					}
					objDataVo.setProtectProperty(protectProperty);
					objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
					objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
					objDataVo.setUnderWriteCode(propLossMainVo.getUnderWriteCode());
					objDataVo.setUnderWriteCertiCode(propLossMainVo.getUnderWriteIdCard());
					objDataVo.setUnderDefLoss(NullToZero(propFee.getSumVeriLoss()).toString());
					underDefLoss = underDefLoss.add(NullToZero(propFee.getSumVeriLoss()));
					
					objDataListVo.add(objDataVo);
				}
			}
		}
		basePartVo.setIsSingleAccident(isSingleAccident);
		basePartVo.setIsPersonInjured(isPersonInjured);
		basePartVo.setIsProtectLoss(isProtectLoss);
		basePartVo.setUnderDefLoss(underDefLoss.compareTo(BigDecimal.ZERO)==0 ? "0.0":underDefLoss.toString());
		bodyVo.setBasePartVo(basePartVo);// ????????????
		bodyVo.setPersonDataVo(personDataVos);// ??????????????????
		bodyVo.setVehicleDataVo(vehicleDataListVo);// ??????????????????
		bodyVo.setObjDataVo(objDataListVo);// ??????????????????
		bodyVo.setSubrogationDataVo(null);
		bodyVo.setDisputeDataVo(null);

		return bodyVo;
	}

	// ?????????????????????
	public String calculateFeeSum(String registNo) {
		BigDecimal sum = new BigDecimal(0);// ???????????????
		List<PrpLDlossCarMainVo> carLossVos = lossCarService.findLossCarMainByRegistNo(registNo);
		if(carLossVos!=null&&carLossVos.size()>0){
			for(PrpLDlossCarMainVo carLossVo:carLossVos){
//				sum += NullToZero(carLossVo.getSumVeriLossFee()).doubleValue();
				sum = sum.add(NullToZero(carLossVo.getSumVeriLossFee()));
			}
		}
		List<PrpLdlossPropMainVo> propLossVos = propTaskService.findPropMainListByRegistNo(registNo);
		if(propLossVos!=null&&propLossVos.size()>0){
			for(PrpLdlossPropMainVo propLossVo:propLossVos){
//				sum += NullToZero(propLossVo.getSumVeriLoss()).doubleValue();
				sum = sum.add(NullToZero(propLossVo.getSumVeriLoss()));
			}
		}
		List<PrpLDlossPersTraceVo> persVos = persTraceService.findPersTraceVoByRegistNo(registNo);
		if(persVos!=null&&persVos.size()>0){
			for(PrpLDlossPersTraceVo persVo:persVos){
//				sum += NullToZero(persVo.getSumVeriDefloss()).doubleValue();
				sum = sum.add(NullToZero(persVo.getSumVeriDefloss()));
			}
		}
		return sum.compareTo(BigDecimal.ZERO)==0 ? "0.0":sum.toString();
	}

	public static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0.0");
		}else if(strNum.compareTo(BigDecimal.ZERO)==0){
			return new BigDecimal("0.0");
		}
		return strNum;
	}

	/**
	 * ?????????????????????????????????????????????
	 * @param registNo
	 * @return
	 * @modified: ???Luwei(2016???5???26??? ??????7:37:32): <br>
	 */
	public SHBILossReqBodyVo setBodyBI_SH(String claimSeqNo,PrpLCMainVo cMainVo) {
		String registNo = cMainVo.getRegistNo();
		SHBILossReqBodyVo bodyVo = new SHBILossReqBodyVo();

		PrpLRegistVo registVo = registService.findByRegistNo(registNo);
		PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(registNo,1);
		PrpLClaimVo claimVo = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,cMainVo.getPolicyNo(),"1").get(0);
		//???????????????
		PrpLDlossCarMainVo mainCarVo = lossCarService.findLossCarMainBySerialNo(registNo, 1).get(0);
		PrpLDlossCarInfoVo prpLDlossCarInfoVo = deflossService.findDefCarInfoByPk(mainCarVo.getCarId());
		// ????????????
		SHBILossReqBasePartVo basePartVo = new SHBILossReqBasePartVo();

		basePartVo.setClaimCode(claimSeqNo);
		basePartVo.setManageType(checkVo.getManageType());
		
		Integer duty = Integer.parseInt(checkDutyVo.getIndemnityDuty())+1;
		basePartVo.setLiabilityAmount(duty.toString());
		basePartVo.setEstimateAmount(this.calculateFeeSum(registNo));
		basePartVo.setConfirmSequenceNo(cMainVo.getValidNo());
		//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????6???
		if("1".equals(mainCarVo.getDeflossCarType()) && prpLDlossCarInfoVo.getLicenseNo()!=null&&prpLDlossCarInfoVo.getLicenseNo().length()>=6&&
				("15".equals(prpLDlossCarInfoVo.getLicenseType())||"16".equals(prpLDlossCarInfoVo.getLicenseType())||
				"17".equals(prpLDlossCarInfoVo.getLicenseType())||"23".equals(prpLDlossCarInfoVo.getLicenseType())||
				"31".equals(prpLDlossCarInfoVo.getLicenseType())||"04".equals(prpLDlossCarInfoVo.getLicenseType())||
				"10".equals(prpLDlossCarInfoVo.getLicenseType()))){
			    if(StringUtils.isNotBlank(cItemCarVo.getLicenseNo())){
			    	basePartVo.setCarMark(cItemCarVo.getLicenseNo().substring(0, 6));
			    }else{
			    	basePartVo.setCarMark(prpLDlossCarInfoVo.getLicenseNo().substring(0, 6));
			    }
				
	
		}else{
				basePartVo.setCarMark(cItemCarVo.getLicenseNo());
		}
		basePartVo.setVehicleType(cItemCarVo.getLicenseKindCode());
		basePartVo.setSubrogationFlag(claimVo==null ? "" : claimVo.getIsSubRogation());
		basePartVo.setRemark(registVo.getPrpLRegistExt().getDangerRemark());
//		basePartVo.setIsSingleAccident(checkVo.getSingleAccidentFlag());
		basePartVo.setReportNo(registNo); 
		String isPersonInjured = "0"; 
		String isProtectLoss = "0";
		BigDecimal underDefLoss = BigDecimal.ZERO;
		

		// ??????????????????????????????
		List<SHCILossReqPersonDataVo> personDataVos = new ArrayList<SHCILossReqPersonDataVo>();

		List<PrpLDlossPersTraceMainVo> persTraceMainVos = findPersTraceMainVo(registNo);
		if(persTraceMainVos != null && persTraceMainVos.size() > 0){
		for(PrpLDlossPersTraceMainVo persTraceMainVo : persTraceMainVos){
		    if("7".equals(persTraceMainVo.getUnderwriteFlag())){
                continue;
            }
			List<PrpLDlossPersTraceVo> persTraceVoList = persTraceMainVo.getPrpLDlossPersTraces();
			if( !"10".equals(persTraceMainVo.getCaseProcessType())&&persTraceVoList!=null&&persTraceVoList.size()>0){
				isPersonInjured = "1";
			for(PrpLDlossPersTraceVo dlossPersTraceVo : persTraceVoList){
				if("1".equals(dlossPersTraceVo.getValidFlag())){//??????????????????????????????????????????
					PrpLDlossPersInjuredVo persInjuredVo = dlossPersTraceVo.getPrpLDlossPersInjured();
	
					// ??????????????????
					SHCILossReqPersonDataVo personDataVo = new SHCILossReqPersonDataVo();
					personDataVo.setPersonName(persInjuredVo.getPersonName());
					personDataVo.setPersonId(persInjuredVo.getCertiCode());
					personDataVo.setAge(persInjuredVo.getPersonAge() != null ? persInjuredVo.getPersonAge().intValue(): null);
					personDataVo.setLossAmount(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).toString());
					personDataVo.setMainThird("1".equals(dlossPersTraceVo.getLossFeeType()) ? "1" : "0");
					personDataVo.setSurveyType("0".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "3" : "1");
					personDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
					personDataVo.setSurveyStartTime(checkVo.getCreateTime());
					personDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
					personDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
					personDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
	//				personDataVo.setEstimateName(persTraceMainVo.getCreateUser());
					String codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getCreateUser());
					if(StringUtils.isBlank(codeCName)){
						codeCName = codeTranService.findCodeName("UserCodeSH",persTraceMainVo.getOperatorCode());
					}
					personDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
					personDataVo.setEstimateStartTime(persTraceMainVo.getPlfSubTime());
					personDataVo.setEstimateEndTime(persTraceMainVo.getUndwrtFeeEndDate());
					personDataVo.setAssesorName(persTraceMainVo.getUndwrtFeeName());
					personDataVo.setAssesorStartTime(persTraceMainVo.getCreateTime());
					personDataVo.setAssesorEndTime(persTraceMainVo.getUpdateTime());
					personDataVo.setInjuryType("");
					//personDataVo.setInjuryLevel(persInjuredVo.getWoundCode());
					personDataVo.setMedicalType(persInjuredVo.getTreatSituation());
					personDataVo.setUnderWriteName(persTraceMainVo.getUndwrtFeeName());
					personDataVo.setUnderWriteCode(persTraceMainVo.getUndwrtFeeCode());
					personDataVo.setEstimateAddr("");
	//				personDataVo.setAddmissionTime(persInjuredVo.getPrpLDlossPersHospitals()==null ? null : persInjuredVo
	//						.getPrpLDlossPersHospitals().get(0).getInHospitalDate());
					personDataVo.setAddmissionTime(null);
					personDataVo.setUnderDefloss(NullToZero(dlossPersTraceVo.getSumVeriDefloss()).toString());
					
					underDefLoss = underDefLoss.add(NullToZero(dlossPersTraceVo.getSumVeriDefloss()));
					personDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
					personDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
					if(StringUtils.isBlank(persTraceMainVo.getOperatorCertiCode())){
						personDataVo.setEstimateCertiCode(persTraceMainVo.getPlfCertiCode());
					} else {
						personDataVo.setEstimateCertiCode(persTraceMainVo.getOperatorCertiCode());
					}
					personDataVo.setDeathTime(dlossPersTraceVo.getPrpLDlossPersInjured().getDeathTime());
					personDataVo.setCountryInjuryType(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode());
				
					// ????????????????????????????????????
					List<SHCILossReqPersonLossPartDataVo> partDataListVo = new ArrayList<SHCILossReqPersonLossPartDataVo>();
					for(PrpLDlossPersTraceFeeVo persTraceFeeVo:dlossPersTraceVo.getPrpLDlossPersTraceFees()){
						SHCILossReqPersonLossPartDataVo partDataVo = new SHCILossReqPersonLossPartDataVo();
						SysCodeDictVo vo = codeTranService.findCodeDictVo("SHFeetype",persTraceFeeVo.getFeeTypeCode());
						partDataVo.setFeeType(vo != null ? vo.getProperty2() : "");// SHFeetype
						partDataVo.setLossAmount(NullToZero(persTraceFeeVo.getVeriDefloss()).toString());
						partDataVo.setUnderDefLoss(NullToZero(persTraceFeeVo.getVeriDefloss()).toString()); //????????????
						partDataListVo.add(partDataVo);
					}
					personDataVo.setLossPartDataVo(partDataListVo);
	
					// ??????????????????????????????
					List<SHCILossReqInjuryDataVo> injuryDataListVo = new ArrayList<SHCILossReqInjuryDataVo>();
					for(PrpLDlossPersExtVo persExtVo:persInjuredVo.getPrpLDlossPersExts()){
						SHCILossReqInjuryDataVo injuryDataVo = new SHCILossReqInjuryDataVo();
						injuryDataVo.setInjuryPart(persExtVo.getInjuredPart());//
						String injuryLevelCode =persExtVo.getWoundGrade();
						if(StringUtils.isBlank(persExtVo.getWoundGrade()) ||"10".equals(injuryLevelCode)){
							injuryLevelCode ="10";
						}else{
							injuryLevelCode ="0" + injuryLevelCode;
						}
						injuryDataVo.setInjuryLevelCode(injuryLevelCode); //??????????????????
						
						injuryDataListVo.add(injuryDataVo);
					}
					personDataVo.setInjuryDataVo(injuryDataListVo);
	
					// ??????????????????????????????
					if("2".equals(persInjuredVo.getTreatSituation())){
					    List<SHCILossReqHospitalInfoDataVo> hospitalInfoDataListVo = new ArrayList<SHCILossReqHospitalInfoDataVo>();
		                for(PrpLDlossPersHospitalVo hospitalVo:persInjuredVo.getPrpLDlossPersHospitals()){
		                    SHCILossReqHospitalInfoDataVo hospitalInfoDataVo = new SHCILossReqHospitalInfoDataVo();
		                    String hospitalName = StringOperationUtil.cutOutString(hospitalVo.getHospitalName(),30);
		                    hospitalInfoDataVo.setHospitalName(hospitalName);
		                    //hospitalInfoDataVo.setHospitalFactoryCertiCode(hospitalVo.getHospitalCode());  //??????????????????????????????
		                    
		                    hospitalInfoDataListVo.add(hospitalInfoDataVo);
		                }
		                personDataVo.setHospitalInfoDataVo(hospitalInfoDataListVo);
					}
				
					//???????????????????????????????????????????????????????????????
					if("02".equals(dlossPersTraceVo.getPrpLDlossPersInjured().getWoundCode())){
					    List<SHCILossReqInjuryIdentifyInfoDataVo> ideInfoDataList = new ArrayList<SHCILossReqInjuryIdentifyInfoDataVo>();
		                SHCILossReqInjuryIdentifyInfoDataVo injuryIdentifyInfoDataVo = new SHCILossReqInjuryIdentifyInfoDataVo();
		                String chkComName = StringOperationUtil.cutOutString(persInjuredVo.getChkComName(),30);
		                injuryIdentifyInfoDataVo.setInjuryIdentifyName(chkComName);
		                injuryIdentifyInfoDataVo.setInjuryIdentifyCertiCode(persInjuredVo.getChkComCode());
		                ideInfoDataList.add(injuryIdentifyInfoDataVo);
		                personDataVo.setIdentifyInfoDataVo(ideInfoDataList);
		                
					}
	                personDataVos.add(personDataVo);
				}
			}
			}
		 }
		}

		Integer carSum = 0;

		// ??????????????????????????????
		List<SHCILossReqVehicleDataVo> vehicleDataListVo = new ArrayList<SHCILossReqVehicleDataVo>();
		List<PrpLDlossCarMainVo> dlossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLDlossCarMainVo> newVoList = getDlossCarMainList(dlossCarMainVos);
		if(newVoList != null && newVoList.size() > 0){
			for(PrpLDlossCarMainVo dlossCarMainVo : newVoList){
				//?????????????????????????????????????????????????????????????????????????????????
				if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(dlossCarMainVo.getCetainLossType()) ||
						CodeConstants.VeriFlag.CANCEL.equals(dlossCarMainVo.getUnderWriteFlag())){
					continue;
				}
				//???????????????????????????0?????????????????????????????????????????????????????????
				if(NullToZero(dlossCarMainVo.getSumVeriLossFee()).compareTo(BigDecimal.ZERO) == 0){
					continue;
				}
				carSum += 1;
				PrpLDlossCarInfoVo carInfoVo = dlossCarMainVo.getLossCarInfoVo();
				// ??????????????????
				SHCILossReqVehicleDataVo vehicleDataVo = new SHCILossReqVehicleDataVo();
//				vehicleDataVo.setCarMark("1".equals(dlossCarMainVo.getDeflossCarType()) 
//						? cItemCarVo.getLicenseNo():carInfoVo.getLicenseNo());
				vehicleDataVo.setCarMark(dlossCarMainVo.getLicenseNo());
				//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????6???
				if(dlossCarMainVo.getLicenseNo()!=null&&dlossCarMainVo.getLicenseNo().length()>=6&&
						("15".equals(carInfoVo.getLicenseType())||"16".equals(carInfoVo.getLicenseType())||
						"17".equals(carInfoVo.getLicenseType())||"23".equals(carInfoVo.getLicenseType())||
						"31".equals(carInfoVo.getLicenseType())||"04".equals(carInfoVo.getLicenseType())||
						"10".equals(carInfoVo.getLicenseType()))){
					if("1".equals(dlossCarMainVo.getDeflossCarType()) && StringUtils.isNotBlank(cItemCarVo.getLicenseNo())){
						vehicleDataVo.setCarMark(cItemCarVo.getLicenseNo().substring(0, 6));
					}else{
						vehicleDataVo.setCarMark(dlossCarMainVo.getLicenseNo().substring(0, 6));
					}
					
				}else{//?????????????????????????????????
					if("1".equals(dlossCarMainVo.getDeflossCarType())){
						vehicleDataVo.setCarMark(toTrimLicno(cItemCarVo.getLicenseNo()));
					}else{
						vehicleDataVo.setCarMark(toTrimLicno(dlossCarMainVo.getLicenseNo()));
					}
					
				}
				SysCodeDictVo dictVo = new SysCodeDictVo();
				if(!StringUtils.isBlank(carInfoVo.getLicenseType())){
					dictVo = codeTranService.findCodeDictVo("LicenseKindCode",carInfoVo.getLicenseType());
				}else{
					dictVo = codeTranService.findCodeDictVo("LicenseKindCode","02");
				}
				vehicleDataVo.setVehicleType("1".equals(dlossCarMainVo.getDeflossCarType()) ? cItemCarVo.getLicenseKindCode():dictVo.getProperty1());
//				vehicleDataVo.setVehicleType("1".equals(dlossCarMainVo.getDeflossCarType()) ? cItemCarVo.getLicenseKindCode():carInfoVo.getLicenseType());
				vehicleDataVo.setLossAmount(dlossCarMainVo.getSumVeriLossFee().toString());//
				vehicleDataVo.setMainThird("1".equals(dlossCarMainVo.getDeflossCarType()) ? "1" : "0");//
				vehicleDataVo.setRobber("03".equals(dlossCarMainVo.getCetainLossType()) ? "1" : "0");
				vehicleDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
				vehicleDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
				vehicleDataVo.setSurveyStartTime(checkVo.getCreateTime());
				vehicleDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
				vehicleDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
				vehicleDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
				//"GSU0000001"
//				vehicleDataVo.setEstimateName(dlossCarMainVo.getCreateUser());
				String codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getCreateUser());
				if(StringUtils.isBlank(codeCName)){
					codeCName = codeTranService.findCodeName("UserCodeSH",dlossCarMainVo.getHandlerCode());
				}
				vehicleDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
				vehicleDataVo.setEstimateStartTime(dlossCarMainVo.getCreateTime());// ??????????????????
				vehicleDataVo.setEstimateEndTime(dlossCarMainVo.getDefEndDate());// ??????????????????
				vehicleDataVo.setAssesorName(dlossCarMainVo.getUnderWriteName());
				vehicleDataVo.setAssesorStartTime(dlossCarMainVo.getUpdateTime());// ??????????????????
				vehicleDataVo.setAssesorEndTime(dlossCarMainVo.getUnderWriteEndDate());// ??????????????????
				vehicleDataVo.setRemnant("0.0");
				vehicleDataVo.setCharg4eFee("0.0");
				BigDecimal repairSum = BigDecimal.ZERO;
				for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
					repairSum = repairSum.add(NullToZero(carRepairVo.getSumVeriLoss()));//
				}
				vehicleDataVo.setTotalWorkingHour(repairSum.compareTo(BigDecimal.ZERO)==0 ? "0.0" : repairSum.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
				String flag = (dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0)
						||(dlossCarMainVo.getPrpLDlossCarComps()!=null&&dlossCarMainVo.getPrpLDlossCarComps().size()>0) ? "1" : "0";
				vehicleDataVo.setChangeOrRepairPart(flag);
				vehicleDataVo.setJyVehicleCode(this.jy_vehicle_CodeConvert(carInfoVo.getModelCode()));
				vehicleDataVo.setClaimVehicleCode(carInfoVo.getModelCode());
				vehicleDataVo.setClaimVehicleName(carInfoVo.getModelName());
				String factoryN = dlossCarMainVo.getRepairFactoryName();
				//????????????????????????????????????????????????????????????
				if(StringUtils.isNotBlank(factoryN) && factoryN.length()>25){
					factoryN=factoryN.substring(0,25);
				}
				vehicleDataVo.setMadeFactory(StringUtils.isNotBlank(factoryN) ? factoryN : "??????");
				vehicleDataVo.setVehicleBrandCode(carInfoVo.getBrandCode());
				vehicleDataVo.setVehicleCatenaCode(carInfoVo.getSeriCode());
				vehicleDataVo.setVehicleGroupCode(carInfoVo.getGroupCode());
				if(StringUtils.isNotEmpty(dlossCarMainVo.getRepairFactoryType())){
					vehicleDataVo.setPriceSltCode(codeTranService.findTransCodeDictVo
							("RepairFactoryType",dlossCarMainVo.getRepairFactoryType()).getProperty2());
				}else{
					vehicleDataVo.setPriceSltCode("02");
				}
				String defineFlag = dlossCarMainVo.getPrpLDlossCarComps().size()>0?
						"1".equals(dlossCarMainVo.getPrpLDlossCarComps().get(0).getSelfConfigFlag()) ? "1" : "0" : "0";
				vehicleDataVo.setDefineFlag(defineFlag);
				
				vehicleDataVo.setVin(carInfoVo.getVinNo());
				vehicleDataVo.setEngineNo(carInfoVo.getEngineNo());
				vehicleDataVo.setModel(carInfoVo.getBrandName());
				vehicleDataVo.setDriverName(carInfoVo.getDriveName());
				
				String prop3 = codeTranService.findTransCodeDictVo("IdentifyType",carInfoVo.getIdentifyType()).getProperty4();
				vehicleDataVo.setCertiType(prop3);
				vehicleDataVo.setCertiCode(carInfoVo.getIdentifyNo());
				vehicleDataVo.setDriverLicenseNo(carInfoVo.getDrivingLicenseNo());
				vehicleDataVo.setTemporaryFlag(StringUtils.isEmpty(cItemCarVo.getLicenseNo()) ? "1" : "0");

				vehicleDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
				vehicleDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
				vehicleDataVo.setUnderWriteCode(dlossCarMainVo.getUnderWriteCode());
				vehicleDataVo.setUnderWriteCertiCode(dlossCarMainVo.getUnderWiteIdNo());
				vehicleDataVo.setUnderDefLoss(NullToZero(dlossCarMainVo.getSumVeriChargeFee()).toString());
				vehicleDataVo.setIsTotalLoss(dlossCarMainVo.getIsTotalLoss());
				vehicleDataVo.setIsHotSinceDetonation(dlossCarMainVo.getIsHotSinceDetonation());
				vehicleDataVo.setIsWaterFlooded(dlossCarMainVo.getIsWaterFloaded());
				vehicleDataVo.setWaterFloodedLevel("1".equals(dlossCarMainVo.getIsWaterFloaded())?dlossCarMainVo.getWaterFloodedLevel():"");
				underDefLoss =underDefLoss.add(NullToZero(dlossCarMainVo.getSumVeriChargeFee()));
				
				// ??????????????????????????????
				List<SHCILossReqCarLossPartDataVo> carLossPartDataListVo = new ArrayList<SHCILossReqCarLossPartDataVo>();
				for(PrpLDlossCarMainVo carMainVo : newVoList){
					// ????????????????????????
					String lossPartStr = carMainVo.getLossPart();
					if(StringUtils.isNotBlank(lossPartStr)){
						String[] lossPartArray = lossPartStr.split(",");
						for(int i = 0; i<lossPartArray.length; i++ ){
							SHCILossReqCarLossPartDataVo lossPartDataVo = new SHCILossReqCarLossPartDataVo();
							String lossPart = codeTranService.findTransCodeDictVo("LossPart",lossPartArray[i]).getProperty3();
							lossPartDataVo.setLossPart(lossPart);
							carLossPartDataListVo.add(lossPartDataVo);
						}
					}
				}
				vehicleDataVo.setCarLossPartDataVo(carLossPartDataListVo);

				// ??????????????????????????????
				List<SHCILossReqFittingDataVo> fittingDataListVo = new ArrayList<SHCILossReqFittingDataVo>();
				if("1".equals(flag)&&dlossCarMainVo.getPrpLDlossCarComps()!=null&&dlossCarMainVo.getPrpLDlossCarComps().size()>0){
					for(PrpLDlossCarCompVo carCompVo:dlossCarMainVo.getPrpLDlossCarComps()){// ??????
						SHCILossReqFittingDataVo dataVo = new SHCILossReqFittingDataVo();
						dataVo.setChangePartName(carCompVo.getCompName());
						dataVo.setChangePartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()));
						dataVo.setChangePartFee(NullToZero(carCompVo.getManHourUnitPrice()).toString());
						BigDecimal manHour = NullToZero(carCompVo.getManHour());
						dataVo.setChangePartTime(manHour.compareTo(BigDecimal.ZERO)==0 ? "0.0":manHour.toString());
						dataVo.setChangePartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).toString());
						dataVo.setRepairPartName(carCompVo.getCompName());
						dataVo.setRepairPartNum(DataUtils.NullToZeroInt(carCompVo.getQuantity()));
						dataVo.setRepairPartFee(NullToZero(carCompVo.getManHourUnitPrice()).toString());
						dataVo.setRepairPartTime(NullToZero(carCompVo.getManHour()).toString());
						dataVo.setRepairPartManpowerFee(NullToZero(carCompVo.getVeriManHourFee()).toString());
						dataVo.setRepairMethord(null);

						dataVo.setJyPartCode(carCompVo.getCompCode());
						dataVo.setOemPartCode(StringUtils.isEmpty(carCompVo.getOriginalId()) ? "???" : carCompVo.getOriginalId());
						dataVo.setDefineFlag("1".equals(carCompVo.getSelfConfigFlag()) ? "1" : "0");

						dataVo.setSubjionFlag("0");

						fittingDataListVo.add(dataVo);
					}
				}
				
				if("1".equals(flag)&&dlossCarMainVo.getPrpLDlossCarRepairs()!=null&&dlossCarMainVo.getPrpLDlossCarRepairs().size()>0){
					for(PrpLDlossCarRepairVo carRepairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){// ??????
						SHCILossReqFittingDataVo dataVo = new SHCILossReqFittingDataVo();
//						if(!"1".equals(dlossCarMainVo.getDeflossCarType())){
							String compName = carRepairVo.getCompName();
							if(compName.length()>50){
								compName = compName.substring(0, 50);
							}
							dataVo.setChangePartName(compName);
							dataVo.setChangePartNum(DataUtils.NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
							dataVo.setChangePartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).toString());
							dataVo.setChangePartTime(NullToZero(carRepairVo.getVeriManHour()).toString());
							dataVo.setChangePartManpowerFee(NullToZero(carRepairVo.getVeriManUnitPrice()).toString());
							dataVo.setRepairPartName(carRepairVo.getPartName());
							dataVo.setRepairPartNum(DataUtils.NullToZero(carRepairVo.getVeriMaterQuantity()).intValue());
							dataVo.setRepairPartFee(NullToZero(carRepairVo.getVeriMaterUnitPrice()).toString());
							dataVo.setRepairPartTime(NullToZero(carRepairVo.getVeriManHour()).toString());
							dataVo.setRepairPartManpowerFee(NullToZero(carRepairVo.getVeriManHourFee()).toString());

							dataVo.setRepairMethord(carRepairVo.getRepairType());

							dataVo.setJyPartCode(carRepairVo.getCompCode());
							dataVo.setOemPartCode(StringUtils.isEmpty(carRepairVo.getPartCode()) ? "???" : carRepairVo.getPartCode());
							dataVo.setDefineFlag("1".equals(carRepairVo.getSelfConfigFlag()) ? "1" : "0");

							dataVo.setSubjionFlag("0");

							fittingDataListVo.add(dataVo);
//						}
					}
				}
				
				vehicleDataVo.setFittingDataVo(fittingDataListVo);
				
				if("1".equals(flag)){
					List<SHCILossReqRepairDataVo> repartDataVoList = new ArrayList<SHCILossReqRepairDataVo>();
					SHCILossReqRepairDataVo repartVo = new SHCILossReqRepairDataVo();
//					repartVo.setRepairOrg("JY00008782");
					repartVo.setRepairOrg("");
					repartDataVoList.add(repartVo);
					vehicleDataVo.setRepairDataVo(repartDataVoList);
				}
				if("1".equals(flag)){
					List<SHCILossReqActuralRepairDataVo> acturalRepairList = new ArrayList<SHCILossReqActuralRepairDataVo>();
					SHCILossReqActuralRepairDataVo repairDataVo = new SHCILossReqActuralRepairDataVo();
//					repairDataVo.setActuralRepairOrg("JY00008782");
					repairDataVo.setActuralRepairOrg("");
					acturalRepairList.add(repairDataVo);
					vehicleDataVo.setActuralRepairDataVo(acturalRepairList);
				}
				
//				// ??????????????????????????????
//				List<SHCILossReqRepairDataVo> repairDataListVo = new ArrayList<SHCILossReqRepairDataVo>();
//				for(PrpLDlossCarRepairVo repairVo:dlossCarMainVo.getPrpLDlossCarRepairs()){
//					SHCILossReqRepairDataVo repairDataVo = new SHCILossReqRepairDataVo();
//					repairDataVo.setRepairOrg(repairVo.getRepairCode());
//				}
//				vehicleDataVo.setRepairDataVo(repairDataListVo);
//
//				// ??????????????????????????????
//				vehicleDataVo.setActuralRepairDataVo(null);
				
				//???????????? ????????????????????????  ?????????????????????????????????
				vehicleDataVo.setIsGlassBroken(StringUtils.isNotBlank(dlossCarMainVo.getIsGlassBroken())?dlossCarMainVo.getIsGlassBroken():"0");
				vehicleDataVo.setIsNotFindThird(StringUtils.isNotBlank(dlossCarMainVo.getIsNotFindThird())?dlossCarMainVo.getIsNotFindThird():"0");

				// ??????
				vehicleDataListVo.add(vehicleDataVo);
			}
		}
		
		basePartVo.setIsSingleAccident(carSum > 1 ? "0" : "1");

		// ??????????????????????????????
		List<SHCILossReqObjDataVo> objDataListVo = new ArrayList<SHCILossReqObjDataVo>();
		List<PrpLdlossPropMainVo> propLossVos = propTaskService.findPropMainListByRegistNo(registNo);
		if(propLossVos!=null&&propLossVos.size()>0){
			for(PrpLdlossPropMainVo propLossMainVo:propLossVos){
				//???????????????0??????????????????????????????
				if(propLossMainVo.getPrpLdlossPropFees()==null ||
						CodeConstants.VeriFlag.CANCEL.equals(propLossMainVo.getUnderWriteFlag())){
					continue;
				}
				for(PrpLdlossPropFeeVo propFee:propLossMainVo.getPrpLdlossPropFees()){
					// ??????????????????
					isProtectLoss = "1";
					SHCILossReqObjDataVo objDataVo = new SHCILossReqObjDataVo();
					String cutOutLossItemName = StringOperationUtil.cutOutString(propFee.getLossItemName(),30);
					objDataVo.setObjectDesc(cutOutLossItemName);
					objDataVo.setLossNum(propFee.getLossQuantity().intValue());
					objDataVo.setLossAmount(NullToZero(propFee.getVeriUnitPrice()).toString());
					objDataVo.setMainThird("1".equals(propLossMainVo.getLossType()) ? "1" : "0");
					objDataVo.setSurveyType("1".equals(checkVo.getPrpLCheckTask().getFirstAddressFlag()) ? "1" : "3");
					objDataVo.setSurveyName(checkVo.getPrpLCheckTask().getChecker());
					objDataVo.setSurveyStartTime(checkVo.getCreateTime());
					objDataVo.setSurveyEndTime(checkVo.getChkSubmitTime());
					objDataVo.setSurveyPlace(checkVo.getPrpLCheckTask().getCheckAddress());
					objDataVo.setSurveyDes(checkVo.getPrpLCheckTask().getContexts());
//					objDataVo.setEstimateName(propLossMainVo.getCreateUser());
					String codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getCreateUser());
					if(StringUtils.isBlank(codeCName)){
						codeCName = codeTranService.findCodeName("UserCodeSH",propLossMainVo.getHandlerCode());
					}
					objDataVo.setEstimateName(StringUtils.isNotBlank(codeCName) ? codeCName : "GJ99999999");
					objDataVo.setEstimateStartTime(propLossMainVo.getCreateTime());
					objDataVo.setEstimateEndTime(propLossMainVo.getUpdateTime());
					objDataVo.setAssesorName(propLossMainVo.getUnderWriteName());
					objDataVo.setAssesorStartTime(propLossMainVo.getDefEndDate());
					objDataVo.setAssesorEndTime(propLossMainVo.getUnderWriteEndDate());

					String protectProperty = "1";
					if("1".equals(propLossMainVo.getLossType())){
						protectProperty = "1";  //????????????
					}else{
						protectProperty = "2";  // ????????????
					}
					objDataVo.setProtectProperty(protectProperty);
					objDataVo.setCheckerCode(checkVo.getPrpLCheckTask().getCheckerCode());
					objDataVo.setCheckerCertiCode(checkVo.getPrpLCheckTask().getCheckerIdfNo());
					objDataVo.setUnderWriteCode(propLossMainVo.getUnderWriteCode());
					objDataVo.setUnderWriteCertiCode(propLossMainVo.getUnderWriteIdCard());
					objDataVo.setUnderDefLoss(NullToZero(propFee.getSumVeriLoss()).toString());
					underDefLoss = underDefLoss.add(NullToZero(propFee.getSumVeriLoss()));
					
					objDataListVo.add(objDataVo);
				}
			}
		}
		basePartVo.setIsPersonInjured(isPersonInjured);
		basePartVo.setIsProtectLoss(isProtectLoss);
		basePartVo.setUnderDefLoss(underDefLoss.compareTo(BigDecimal.ZERO)==0 ? "0.0":underDefLoss.toString());
		bodyVo.setBasePartVo(basePartVo);// ????????????
		bodyVo.setPersonDataVo(personDataVos);// ??????????????????
		bodyVo.setVehicleDataVo(vehicleDataListVo);// ??????????????????
		bodyVo.setObjDataVo(objDataListVo);// ??????????????????
		bodyVo.setSubrogationDataVo(null);
		bodyVo.setDisputeDataVo(null);

		return bodyVo;
	}
	//??????????????????
	public String jy_vehicle_CodeConvert(String vehicleCode){
	    if(StringUtils.isNotBlank(vehicleCode)){
	        vehicleCode = vehicleCode.replaceAll("[^a-z^A-Z^0-9]", "");
	        if(vehicleCode.length() > 32){
	            vehicleCode = vehicleCode.substring(0,31);
	        }
	    }
	    return vehicleCode;
	}
	
//	public static void main(String[] args) {
//		Double doubles = 0.0;
//		String strArray = doubles.toString().substring(0,doubles.toString().indexOf("."));
//		System.out.println(doubles.toString() + "====" +strArray);
//		
//		double a = 0.00;
//		double b = 180.00;
//		double c = 435.65;
//		
//		double s = a+b+c;
//		
//		
//		BigDecimal a1 = new BigDecimal("0.00");
//		BigDecimal a2 = new BigDecimal("180.00");
//		BigDecimal a3 = new BigDecimal("435.68");
//		BigDecimal a4 = a1.add(a2).add(a3);
//		
//		System.out.println("=====:===="+s);
//		System.out.println("=====BigDecimal--1====:"+a4);
//		System.out.println("============BigDecimal--2=======:"+a4.doubleValue());
//		System.out.println(new BigDecimal(1836.81).doubleValue());
//	}
	//???????????????????????????
	private  String toTrimLicno(String licenseNo) {
		if(StringUtils.isNotBlank(licenseNo)){
			licenseNo = licenseNo.replace((char)12288,' ').replace(" " ,"");
			return licenseNo;
		}else{
			return licenseNo;
		}
		
		
	}
}
