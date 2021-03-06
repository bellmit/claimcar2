package ins.sino.claimcar.regist.service;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.policyInfo.register.vo.BiPolicyInfoRegisterVo;
import ins.sino.claimcar.policyInfo.register.vo.CiPolicyInfoRegisterVo;
import ins.sino.claimcar.policyInfo.register.vo.CommercialClaimOutDangerVo;
import ins.sino.claimcar.policyInfo.register.vo.CommercialClaimPeerVo;
import ins.sino.claimcar.policyInfo.register.vo.CommercialClaimVo;
import ins.sino.claimcar.policyInfo.register.vo.InsuranceClaimOutDangerVo;
import ins.sino.claimcar.policyInfo.register.vo.InsuranceClaimPeerVo;
import ins.sino.claimcar.policyInfo.register.vo.InsuranceClaimVo;
import ins.sino.claimcar.policyInfo.register.vo.RespVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.scheduleinter.quickclaim.util.MD5Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;


@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path(value = "policyInfoRegisterService")
public class PolicyInfoRegisterServiceImpl implements PolicyInfoRegisterService{
	private static Logger logger = LoggerFactory.getLogger(PolicyInfoRegisterServiceImpl.class);
	
	@Autowired
	private PrpLCMainService prpLCMainService;
	@Autowired
	private RegistService registService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private EndCaseService endCaseService;
	@Autowired
	private CompensateTaskService compensateTaskService;
	@Autowired
	private CheckHandleService checkHandleService;
	@Autowired
	private WfTaskQueryService wfTaskQueryService;
	@Autowired
	private EndCasePubService endCasePubService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private ClaimInterfaceLogService claimInterfaceLogService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	

	
    /**
     * ??????????????????????????????
     */
	@Override
	public void policyInfoRegister(String registNo,String type,SysUserVo userVo) {
		String url="";//??????url
		String reqjsonString="";//????????????
		String resjsonString="";//????????????
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();//?????????
		logVo.setComCode(userVo.getComCode());
		logVo.setServiceType(type);;//B--?????????C--??????????????? 
		logVo.setRegistNo(registNo);//?????????
		logVo.setCreateTime(new Date());//????????????
		logVo.setCreateUser(userVo.getUserCode());
		logVo.setOperateNode("endCase");//??????
		List<PrpLCMainVo> prpLCMainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);//????????????
		PrpLCheckDutyVo prpLCheckDutyVo=checkTaskService.findCheckDuty(registNo, 1);
		PrpLCheckVo prpLCheckVo=checkHandleService.queryPrpLCheckVo(registNo);
		List<PrpLDlossCarMainVo> prpLDlossCarMainVos=deflossHandleService.findLossCarInfoByRegistNo(registNo);//???????????????
		PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
		List<PrpLCheckPersonVo> checkPersonVos=new ArrayList<PrpLCheckPersonVo>();
		if(prpLCheckVo!=null){
			prpLCheckTaskVo=prpLCheckVo.getPrpLCheckTask();
			checkPersonVos=prpLCheckTaskVo.getPrpLCheckPersons();
		}
		CiPolicyInfoRegisterVo ciPolicyInfoRegisterVo=new CiPolicyInfoRegisterVo();
		BiPolicyInfoRegisterVo biPolicyInfoRegisterVo=new BiPolicyInfoRegisterVo();
		List<PrpLClaimVo> prpLClaimVos=claimService.findClaimListByRegistNo(registNo);//????????????
		List<PrpLEndCaseVo> prpLEndCaseVos=endCaseService.findEndCaseVo(registNo);
		PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
		List<PrpLWfTaskVo> wftaskVos=wfFlowQueryService.findTaskVoForInAndOut(registNo,FlowNode.ReOpen.name(),FlowNode.EndCas.name());
		try{
		if(prpLCMainVos!=null && prpLCMainVos.size()>0){
			for(PrpLCMainVo cmainVo:prpLCMainVos){
				if("C".equals(type) && "1101".equals(cmainVo.getRiskCode())){//???????????????????????????
					List<SysCodeDictVo> sysCodeDictVos=wfTaskQueryService.findPrpDcompanyByUserCode(cmainVo.getComCode(),null);
					InsuranceClaimVo insuranceClaimVo=new InsuranceClaimVo();
					insuranceClaimVo.setCOMPANY_ID(CodeConstants.DHICCODE);//????????????
					insuranceClaimVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//????????????
					insuranceClaimVo.setCONFIRM_SEQUENCE_NO(cmainVo.getValidNo());//???????????????
					insuranceClaimVo.setPOLICY_NO(cmainVo.getPolicyNo());//????????????
					insuranceClaimVo.setREPORT_TIME(DateFormatString(prpLRegistVo.getReportTime()));//????????????
					insuranceClaimVo.setREPORT_NO(prpLRegistVo.getRegistNo());//?????????
					insuranceClaimVo.setREPORTER_NAME(prpLRegistVo.getReportorName());//???????????????
					insuranceClaimVo.setACCIDENT_TIME(DateFormatString(prpLRegistVo.getDamageTime()));//????????????
					insuranceClaimVo.setACCIDENT_PLACE(prpLRegistVo.getDamageAddress());//????????????
					insuranceClaimVo.setACCIDENT_DESCRIPTION(prpLRegistExtVo.getDangerRemark());//????????????
					insuranceClaimVo.setACCIDENT_CAUSE(damageCodeChanged(prpLCheckVo.getDamageCode()));//??????????????????
					insuranceClaimVo.setACCIDENT_LIABILITY(prpLCheckDutyVo!=null?indemnityDuty(prpLCheckDutyVo.getIndemnityDuty()):null);//?????????????????????????????????
					insuranceClaimVo.setMANAGE_TYPE(prpLRegistExtVo.getManageType());//?????????????????????????????????
					if(prpLClaimVos!=null && prpLClaimVos.size()>0){
						for(PrpLClaimVo claimVo:prpLClaimVos){
							if("1101".equals(claimVo.getRiskCode())){
								insuranceClaimVo.setREGISTRATION_NO(claimVo.getClaimNo());//?????????
								insuranceClaimVo.setREGISTRATION_TIME(DateFormatString(claimVo.getClaimTime()));//????????????
								insuranceClaimVo.setESTIMATE_AMOUNT(claimVo.getSumDefLoss()+"");//????????????
								insuranceClaimVo.setCLAIM_TYPE("1");//??????????????????
								break;
							}
						}
					}
					
					if(prpLEndCaseVos!=null && prpLEndCaseVos.size()>0){
						for(PrpLEndCaseVo caseVo:prpLEndCaseVos){
							if("1101".equals(caseVo.getRiskCode())){
								insuranceClaimVo.setENDCASE_DATE(DateFormatString(caseVo.getEndCaseDate()));
								
							}
						}
					}
					
					List<PrpLCompensateVo> prpLCompensateVos=findCompensateVoList(insuranceClaimVo.getREGISTRATION_NO(),"1101");
					BigDecimal claimAmount = BigDecimal.ZERO;//???????????????
					if(prpLCompensateVos != null && !prpLCompensateVos.isEmpty()){
						for(PrpLCompensateVo compensate : prpLCompensateVos){
							//???
							List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
							if(lossItemVoList != null && !lossItemVoList.isEmpty()){
								for(PrpLLossItemVo itemVo : lossItemVoList){// ???
									if(!PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){//??????????????????
										claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
										
									}
								}
								
								//??????
								List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
								if(lossPropVoList != null && !lossPropVoList.isEmpty()){
									for(PrpLLossPropVo propVo : lossPropVoList){
										claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
										
									}
								}
								
								//???
								List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
								if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
									for(PrpLLossPersonVo personVo : lossPersoVoList){// ???
										for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
										    claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
										}
									}
								}
							}
						}
					}
					
					insuranceClaimVo.setCLAIM_AMOUNT(claimAmount.toString());//???????????????
					if("1".equals(prpLCheckVo.getIsPersonLoss()) && !"4".equals(prpLCheckDutyVo.getIndemnityDuty())){
						if(checkPersonVos!=null && checkPersonVos.size()>0){
							for(PrpLCheckPersonVo personVo:checkPersonVos){
								if("2".equals(personVo.getPersonPayType())){
									insuranceClaimVo.setACCIDENT_DEATH("1");
									break;
								}
							}
							if(StringUtils.isBlank(insuranceClaimVo.getACCIDENT_DEATH())){
								insuranceClaimVo.setACCIDENT_DEATH("0");
							}
						}
					}else{
						insuranceClaimVo.setACCIDENT_DEATH("0");
					}
					insuranceClaimVo.setPAY_SELF_FLAG(prpLCheckVo.getIsClaimSelf());//??????????????????
					insuranceClaimVo.setACCIDENT_TYPE(riskCodeChanged(prpLCheckVo.getDamageCode()));//?????????????????????????????????
				    if(sysCodeDictVos!=null && sysCodeDictVos.size()>0){
				    	insuranceClaimVo.setCITY_CODE(StringUtils.isNotBlank(sysCodeDictVos.get(0).getSysAreaCode())?sysCodeDictVos.get(0).getSysAreaCode().substring(0, 4)+"00":null);//???????????????????????????
				    	insuranceClaimVo.setCOUNTY_CODE(sysCodeDictVos.get(0).getSysAreaCode());// ???????????????????????????
				    }
				    // ????????????????????????
				    String isReopenClaim="0";//0--?????????1--???
					if(wftaskVos!=null && wftaskVos.size()>0){
						for(PrpLWfTaskVo vo:wftaskVos){
							if("1101".equals(vo.getRiskCode())){
							     isReopenClaim="1";
								 break;
							}
							
						}
					}
					insuranceClaimVo.setREOPEN_CASE(isReopenClaim);//??????????????????
					ciPolicyInfoRegisterVo.setInsuranceClaim(insuranceClaimVo);//???????????????????????????
					InsuranceClaimOutDangerVo insuranceClaimOutDangerVo=new InsuranceClaimOutDangerVo();
					List<InsuranceClaimPeerVo> insuranceClaimPeers=new ArrayList<InsuranceClaimPeerVo>();
					insuranceClaimOutDangerVo.setCOMPANY_ID(CodeConstants.DHICCODE);//????????????
					insuranceClaimOutDangerVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//????????????
					if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
						for(PrpLDlossCarMainVo mainvo:prpLDlossCarMainVos){
							if("1".equals(mainvo.getDeflossCarType())){
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									insuranceClaimOutDangerVo.setVEHICLE_TYPE("99");//????????????????????????
								}else{
									insuranceClaimOutDangerVo.setVEHICLE_TYPE(prpLDlossCarInfoVo.getLicenseType());//????????????????????????
								}
								
								insuranceClaimOutDangerVo.setCAR_MARK(prpLDlossCarInfoVo.getLicenseNo());//????????????????????????
								PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(mainvo.getRegistNo());
									String platformCarKind = null;
									if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
										platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
												ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
										if(StringUtils.isBlank(platformCarKind)){
											platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
										}
										insuranceClaimOutDangerVo.setVEHICLE_CATEGORY(platformCarKind);
									} else{
										insuranceClaimOutDangerVo.setVEHICLE_CATEGORY(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
									}
									
									if(StringUtils.isBlank(insuranceClaimOutDangerVo.getVEHICLE_CATEGORY())){
								    	insuranceClaimOutDangerVo.setVEHICLE_CATEGORY("11");//????????????????????????--???????????????
								    }
									insuranceClaimOutDangerVo.setRACK_NO(prpLDlossCarInfoVo.getVinNo());//?????????????????????
									insuranceClaimOutDangerVo.setENGINE_NO(prpLDlossCarInfoVo.getEngineNo());//????????????????????????
									insuranceClaimOutDangerVo.setDRIVER_NAME(prpLDlossCarInfoVo.getDriveName());//?????????????????????
									insuranceClaimOutDangerVo.setDRIVER_LICENSE_NO(prpLDlossCarInfoVo.getDrivingLicenseNo());//???????????????????????????
									insuranceClaimOutDangerVo.setREPAIR_FACTORY_NAME(mainvo.getRepairFactoryName());//??????????????????
									if("03".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("1");//????????????--??????
									}else if("02".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("2");//????????????--????????????
									}else if("01".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("3");//????????????--????????????
									}else{
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("4");//????????????--??????
									}
									ciPolicyInfoRegisterVo.setInsuranceClaimOutDangers(insuranceClaimOutDangerVo);//???????????????????????????
									
								}else{
									InsuranceClaimPeerVo insuranceClaimPeerVo=new InsuranceClaimPeerVo();
									PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
									insuranceClaimPeerVo.setCOMPANY_ID(CodeConstants.DHICCODE);//????????????
									insuranceClaimPeerVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//????????????
									insuranceClaimPeerVo.setDRIVER_NAME(prpLDlossCarInfoVo.getDriveName());//????????????????????????
									insuranceClaimPeerVo.setCAR_MARK(prpLDlossCarInfoVo.getLicenseNo());//????????????????????????
									insuranceClaimPeerVo.setVEHICLE_TYPE(prpLDlossCarInfoVo.getLicenseType());//????????????????????????
									insuranceClaimPeerVo.setRACK_NO(prpLDlossCarInfoVo.getVinNo());//?????????
									insuranceClaimPeerVo.setENGINE_NO(prpLDlossCarInfoVo.getEngineNo());//????????????????????????
									insuranceClaimPeerVo.setREPAIR_FACTORY_NAME(mainvo.getRepairFactoryName());//??????????????????
									insuranceClaimPeerVo.setCERTI_CODE(prpLDlossCarInfoVo.getDrivingLicenseNo());//??????????????????????????????
									if("03".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("1");//????????????--??????
									}else if("02".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("2");//????????????--????????????
									}else if("01".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("3");//????????????--????????????
									}else{
										insuranceClaimPeerVo.setESTIMATE_TYPE("4");//????????????--??????
									}
									insuranceClaimPeers.add(insuranceClaimPeerVo);

							     }
							    
						}
						
						ciPolicyInfoRegisterVo.setInsuranceClaimPeers(insuranceClaimPeers);//????????????????????????????????????
					}
					
					
					
					
				}else if("B".equals(type) && !"1101".equals(cmainVo.getRiskCode())){//???????????????????????????
					List<SysCodeDictVo> sysCodeDictVos=wfTaskQueryService.findPrpDcompanyByUserCode(cmainVo.getComCode(),null);
					CommercialClaimVo commercialClaimVo=new CommercialClaimVo();
					commercialClaimVo.setInsurerCode(CodeConstants.DHICCODE);//????????????
					commercialClaimVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//????????????
					commercialClaimVo.setConfirmSequenceNo(cmainVo.getValidNo());//???????????????
					commercialClaimVo.setPolicyNo(cmainVo.getPolicyNo());//????????????
					commercialClaimVo.setNotificationTime(DateFormatString(prpLRegistVo.getReportTime()));//????????????
					commercialClaimVo.setClaimNotificationNo(cmainVo.getRegistNo());//?????????
					commercialClaimVo.setReporter(prpLRegistVo.getReportorName());//???????????????
					commercialClaimVo.setLossTime(DateFormatString(prpLRegistVo.getDamageTime()));//????????????
					commercialClaimVo.setLossArea(prpLRegistVo.getDamageAddress());//????????????
					commercialClaimVo.setLossDesc(prpLRegistExtVo.getDangerRemark());//????????????
					commercialClaimVo.setLossCauseCode(damageCodeChanged(prpLCheckVo.getDamageCode()));//??????????????????
					commercialClaimVo.setAccidentLiability(prpLCheckDutyVo!=null?indemnityDuty(prpLCheckDutyVo.getIndemnityDuty()):null);;//????????????????????????
					commercialClaimVo.setOptionType(prpLRegistExtVo.getManageType());//????????????????????????
					if(prpLClaimVos!=null && prpLClaimVos.size()>0){
						for(PrpLClaimVo claimVo:prpLClaimVos){
							if(!"1101".equals(claimVo.getRiskCode())){
								commercialClaimVo.setClaimRegistrationNo(claimVo.getClaimNo());//?????????
								commercialClaimVo.setClaimRegistrationTime(DateFormatString(claimVo.getClaimTime()));//????????????
								commercialClaimVo.setEstimatedLossAmount(claimVo.getSumDefLoss()+"");//????????????
								commercialClaimVo.setClaimType("1");//??????????????????
								break;
							}
						}
					}
					if(prpLEndCaseVos!=null && prpLEndCaseVos.size()>0){
						for(PrpLEndCaseVo caseVo:prpLEndCaseVos){
							if(!"1101".equals(caseVo.getRiskCode())){
								commercialClaimVo.setClaimCloseTime(DateFormatString(caseVo.getEndCaseDate()));
								
							}
						}
					}
					
					List<PrpLCompensateVo> prpLCompensateVos=findCompensateVoList(commercialClaimVo.getClaimNotificationNo(),cmainVo.getRiskCode());
					BigDecimal claimAmount = BigDecimal.ZERO;//???????????????
					if(prpLCompensateVos != null && !prpLCompensateVos.isEmpty()){
						for(PrpLCompensateVo compensate : prpLCompensateVos){
							//???
							List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
							if(lossItemVoList != null && !lossItemVoList.isEmpty()){
								for(PrpLLossItemVo itemVo : lossItemVoList){// ???
									if(!PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){//??????????????????
										claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
										
									}
								}
								
								//??????
								List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
								if(lossPropVoList != null && !lossPropVoList.isEmpty()){
									for(PrpLLossPropVo propVo : lossPropVoList){
										claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
										
									}
								}
								
								//???
								List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
								if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
									for(PrpLLossPersonVo personVo : lossPersoVoList){// ???
										for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
										    claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
										}
									}
								}
							}
						}
					}
					commercialClaimVo.setClaimAmount(claimAmount.toString());//???????????????
					if(!"4".equals(prpLCheckDutyVo.getIndemnityDuty())){//????????????????????????
						commercialClaimVo.setIsInsured("1");
					}else{
						commercialClaimVo.setIsInsured("0");
					}
					commercialClaimVo.setAccidentType(riskCodeChanged(prpLCheckVo.getDamageCode()));//??????????????????
					commercialClaimVo.setIsTotalLoss(prpLCheckVo.getLossType());//????????????
					if(sysCodeDictVos!=null && sysCodeDictVos.size()>0){
						commercialClaimVo.setCityCode(StringUtils.isNotBlank(sysCodeDictVos.get(0).getSysAreaCode())?sysCodeDictVos.get(0).getSysAreaCode().substring(0, 4)+"00":null);//???????????????????????????
						commercialClaimVo.setCountyCode(sysCodeDictVos.get(0).getSysAreaCode());// ???????????????????????????
				    }
					 // ????????????????????????
				    String isReopenClaim="0";//0--?????????1--???
				    if(wftaskVos!=null && wftaskVos.size()>0){
						for(PrpLWfTaskVo vo:wftaskVos){
							if(!"1101".equals(vo.getRiskCode())){
									isReopenClaim="1";
									break;
							}
							
						}
					}
					commercialClaimVo.setReopenCase(isReopenClaim);//??????????????????
					biPolicyInfoRegisterVo.setCommercialClaim(commercialClaimVo);
					List<CommercialClaimPeerVo> commercialClaimPeers=new ArrayList<CommercialClaimPeerVo>();//?????????????????????
					if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
						for(PrpLDlossCarMainVo mainvo:prpLDlossCarMainVos){
							if("1".equals(mainvo.getDeflossCarType())){
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								CommercialClaimOutDangerVo commercialClaimOutDangerVo=new CommercialClaimOutDangerVo();//???????????????????????????
								commercialClaimOutDangerVo.setInsurerCode(CodeConstants.DHICCODE);//????????????
								commercialClaimOutDangerVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//????????????
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									commercialClaimOutDangerVo.setLicensePlateType("99");//????????????????????????
								}else{
									commercialClaimOutDangerVo.setLicensePlateType(prpLDlossCarInfoVo.getLicenseType());//????????????????????????
								}
								commercialClaimOutDangerVo.setLicensePlateNo(prpLDlossCarInfoVo.getLicenseNo());//????????????????????????
								PrpLCItemCarVo ciItemCarVo = registQueryService.findCItemCarByRegistNo(mainvo.getRegistNo());
								String platformCarKind = null;
								if("011".equals(ciItemCarVo.getCarType()) || "016".equals(ciItemCarVo.getCarType())){
									platformCarKind = CodeConvertTool.getVehicleCategory(ciItemCarVo.getCarType(),
											ciItemCarVo.getExhaustScale(),ciItemCarVo.getTonCount());
									if(StringUtils.isBlank(platformCarKind)){
										platformCarKind = CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1();
									}
									commercialClaimOutDangerVo.setMotorTypeCode(platformCarKind);
								} else{
									commercialClaimOutDangerVo.setMotorTypeCode(CodeTranUtil.findTransCodeDictVo("VehicleTypeShow",ciItemCarVo.getCarType()).getProperty1());
								}
								
								if(StringUtils.isBlank(commercialClaimOutDangerVo.getMotorTypeCode())){
									commercialClaimOutDangerVo.setMotorTypeCode("11");//????????????????????????--???????????????
							    }
								commercialClaimOutDangerVo.setVIN(prpLDlossCarInfoVo.getVinNo());//?????????????????????
								commercialClaimOutDangerVo.setEngineNo(prpLDlossCarInfoVo.getEngineNo());//????????????????????????
								commercialClaimOutDangerVo.setDriverName(prpLDlossCarInfoVo.getDriveName());//?????????????????????
								commercialClaimOutDangerVo.setCertiCode(prpLDlossCarInfoVo.getDrivingLicenseNo());//???????????????????????????
								commercialClaimOutDangerVo.setRepairFactoryName(mainvo.getRepairFactoryName());//??????????????????
								if("03".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("1");//????????????--??????
								}else if("02".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("2");//????????????--????????????
								}else if("01".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("3");//????????????--????????????
								}else{
									commercialClaimOutDangerVo.setEstimateType("4");//????????????--??????
								}
								biPolicyInfoRegisterVo.setCommercialClaimOutDangers(commercialClaimOutDangerVo);//????????????????????????
							}else{
								CommercialClaimPeerVo commercialClaimPeerVo=new CommercialClaimPeerVo();
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								commercialClaimPeerVo.setInsurerCode(CodeConstants.DHICCODE);//????????????
								commercialClaimPeerVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//????????????
								commercialClaimPeerVo.setDriverName(prpLDlossCarInfoVo.getDriveName());//????????????????????????
								commercialClaimPeerVo.setCertiCode(prpLDlossCarInfoVo.getDrivingLicenseNo());//???????????????????????????
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									commercialClaimPeerVo.setLicensePlateType("99");//????????????????????????
								}else{
									commercialClaimPeerVo.setLicensePlateType(prpLDlossCarInfoVo.getLicenseType());//????????????????????????
								}
								commercialClaimPeerVo.setLicensePlateNo(prpLDlossCarInfoVo.getLicenseNo());//????????????????????????
								commercialClaimPeerVo.setVIN(prpLDlossCarInfoVo.getVinNo());//?????????????????????
								commercialClaimPeerVo.setEngineNo(prpLDlossCarInfoVo.getEngineNo());//????????????????????????
								commercialClaimPeerVo.setRepairFactoryName(mainvo.getRepairFactoryName());//??????????????????
								if("03".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("1");//????????????--??????
								}else if("02".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("2");//????????????--????????????
								}else if("01".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("3");//????????????--????????????
								}else{
									commercialClaimPeerVo.setEstimateType("4");//????????????--??????
								}
								commercialClaimPeers.add(commercialClaimPeerVo);//?????????????????????
							}
						}
					}
					
					biPolicyInfoRegisterVo.setCommercialClaimPeers(commercialClaimPeers);//????????????????????????
				}
			}
		}
		if("C".equals(type)){
			url = SpringProperties.getProperty("SC_CIURL");
			Gson gson = new Gson();
			reqjsonString=gson.toJson(ciPolicyInfoRegisterVo);
			logger.info("?????????????????????????????????????????????-----------???{}",reqjsonString);
			RespVo respVo=urlPost(reqjsonString,url);//?????????
			if(respVo!=null && JSONObject.fromObject(respVo)!=null){
				resjsonString=JSONObject.fromObject(respVo).toString();
			}
			if(respVo!=null && "E0000".equals(respVo.getErrcode())){
				logVo.setStatus("1");
				logVo.setErrorCode("E0000");
				logVo.setErrorMessage("??????");
			}else{
				if(respVo!=null){
					logVo.setStatus("0");
					logVo.setErrorCode(respVo.getErrcode());
					logVo.setErrorMessage(respVo.getMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("-->?????????????????????");
				}
				
			}
		}else{
			url = SpringProperties.getProperty("SC_BIURL");
			Gson gson = new Gson();
			reqjsonString=gson.toJson(biPolicyInfoRegisterVo);
			logger.info("?????????????????????????????????????????????-----------???{}",reqjsonString);
			RespVo respVo=urlPost(reqjsonString,url);//?????????
			if(respVo!=null && JSONObject.fromObject(respVo)!=null){
				resjsonString=JSONObject.fromObject(respVo).toString();
			}
			if(respVo!=null && "E0000".equals(respVo.getErrcode())){
				logVo.setStatus("1");
				logVo.setErrorCode("E0000");
				logVo.setErrorMessage("??????");
			}else{
				if(respVo!=null && StringUtils.isNotBlank(respVo.getErrcode())){
					logVo.setStatus("0");
					logVo.setErrorCode(respVo.getErrcode());
					logVo.setErrorMessage(respVo.getMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("-->?????????????????????");
				}
				
			}
		
		
		}
	   }catch(Exception e){
		    logVo.setStatus("0");
			logVo.setErrorMessage("catch??????:"+e.getMessage());
		    logger.error("???????????????????????????????????????-----------???",e);
	   }finally{
		   logVo.setBusinessType(BusinessType.SC_policyInfo.name());
		   logVo.setBusinessName(BusinessType.SC_policyInfo.getName());
		   logVo.setRequestUrl(url);
		   logVo.setRequestXml(reqjsonString);
		   logVo.setResponseXml(resjsonString);
		   claimInterfaceLogService.save(logVo);
	   }
	}
	//???????????????????????????
	private List<PrpLCompensateVo> findCompensateVoList(String claimNo,String riskCode){
		List<PrpLCompensateVo> list = new ArrayList<PrpLCompensateVo>();
		List<PrpLCompensateVo> temp = compensateTaskService.findCompListByClaimNo(claimNo, "N");
		if(temp != null && !temp.isEmpty()){
			for(PrpLCompensateVo t : temp){
				if(StringUtils.isNotBlank(riskCode) && riskCode.equals(t.getRiskCode()) ){
					list.add(t);
				}
			}
		}
		return list;
	}
	/**
	 * ????????????
	 * @param params
	 * @param type
	 * @return
	 */
	private RespVo urlPost(String params,String url)throws Exception{
		RespVo respVo=new RespVo();
		String code=CodeConstants.DHICCODE;
		String secret = SpringProperties.getProperty("SC_secret");
		String data = new String(Base64.encodeBase64(params.getBytes("UTF-8")));;//base64??????
		String reqData=URLEncoder.encode(data,"UTF-8");//??????????????????data??????URLEncode??????
        logger.info("??????????????????????????????url---------------------------"+url);
		//????????????
		String signed=MD5Util.getMd5(secret+data);
		String securityType=CodeConstants.secretType;//????????????
		String version=CodeConstants.SCVERSION;//??????
		String param ="code="+code+"&data="+reqData+"&signed="+signed+"&securityType="+securityType+"&version="+version;
		/*--------------------------------------------------------*/
		StringBuffer buffer = new StringBuffer();
		String strMessage = "";
		InputStream inputStream = null;
		OutputStream outputStream = null;
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		URL urlnet = null;
		HttpURLConnection connection = null;
		try {
			urlnet = new URL(url);
			connection = (HttpURLConnection)urlnet.openConnection();
			connection.setDoInput(true); // ??????????????????????????????
			connection.setDoOutput(true); // ??????????????????????????????
			connection.setRequestMethod("POST"); // ??????????????????
			connection.setConnectTimeout(10000);// ???????????????????????????
			connection.setReadTimeout(10000);// ????????????????????????????????????
			connection.setAllowUserInteraction(true); // ??????????????????????????????
			connection.connect();
			outputStream = connection.getOutputStream();
			writer = new OutputStreamWriter(outputStream,"UTF-8");
			writer.write(param);
			writer.flush(); // ????????????
			writer.close();
			inputStream = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")); // ??????????????????????????????
			while ((strMessage = reader.readLine()) != null) {
				buffer.append(strMessage);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			throw new Exception("???????????????????????????????????????", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		logger.info("????????????????????????????????????======="+buffer);
		JSONObject resJson= JSONObject.fromObject(buffer.toString());
		if(resJson!=null){
			respVo=(RespVo)JSONObject.toBean(resJson,RespVo.class);
		}
		
	    return respVo;
	}
	/**
	 * ???????????????
	 * @param str
	 * @return
	 */
	public static String rightTrim(String str) {
		if(str==null){
			return "";
		}
		int length = str.length();
		for(int i = length-1; i>=0; --i){
			if(str.charAt(i)!=' '){
				break;
			}
			--length;
		}
		return str.substring(0,length);
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
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
 /**
  * ????????????????????????
  * @param damageCode
  * @return
  */
 private String damageCodeChanged(String damageCode){
	 String damageCodeC="A10096";//????????????????????????
	 if("DM01".equals(damageCode)){
		 damageCodeC="A10029";//??????
	 }else if("DM02".equals(damageCode)){
		 damageCodeC="A10022";//??????????????????
	 }else if("DM03".equals(damageCode)){
		 damageCodeC="A10058";//????????????
	 }else if("DM04".equals(damageCode)){
		 damageCodeC="A10023";//????????????
	 }else if("DM05".equals(damageCode)){
		 damageCodeC="A10096";//??????????????????
	 }else if("DM51".equals(damageCode)){
		 damageCodeC="A10031";//??????
	 }else if("DM06".equals(damageCode)){
		 damageCodeC="A10003";//??????
	 }else if("DM07".equals(damageCode)){
		 damageCodeC="A10006";//??????
	 }else if("DM08".equals(damageCode)){
		 damageCodeC="A10025";//??????????????????
	 }else if("DM09".equals(damageCode)){
		 damageCodeC="A10001";//??????
	 }else if("DM10".equals(damageCode)){
		 damageCodeC="A19001";//??????
	 }else if("DM12".equals(damageCode)){
		 damageCodeC="A10096";//??????????????????
	 }
	 return damageCodeC;
 }
 /**
  * ??????????????????????????????????????????
  * @param damageCode
  * @return
  */
 private String riskCodeChanged(String damageCode){
	 String damageCodeC="100";//???????????????
	 if("DM01".equals(damageCode)){
		 damageCodeC="111";//??????
	 }else if("DM02".equals(damageCode)){
		 damageCodeC="221";//??????????????????
	 }else if("DM03".equals(damageCode)){
		 damageCodeC="222";//????????????
	 }else if("DM04".equals(damageCode)){
		 damageCodeC="204";//????????????
	 }else if("DM05".equals(damageCode)){
		 damageCodeC="299";//????????????????????????????????????
	 }else if("DM51".equals(damageCode)){
		 damageCodeC="299";//????????????????????????????????????
	 }else if("DM06".equals(damageCode)){
		 damageCodeC="504";//??????
	 }else if("DM07".equals(damageCode)){
		 damageCodeC="502";//??????
	 }else if("DM08".equals(damageCode)){
		 damageCodeC="242";//??????????????????
	 }else if("DM09".equals(damageCode)){
		 damageCodeC="201";//??????
	 }else if("DM10".equals(damageCode)){
		 damageCodeC="299";//??????
	 }else if("DM12".equals(damageCode)){
		 damageCodeC="900";//??????????????????
	 }
	 return damageCodeC;
 }
 /**
  *??????????????????
  * @param indemnityDuty
  * @return
  */
 private String indemnityDuty(String indemnityDuty){
	 String dutyCode="5";
	 if("0".equals(indemnityDuty)){
		 dutyCode="1";//??????
	 }else if("1".equals(indemnityDuty)){
		 dutyCode="2";//??????
	 }else if("2".equals(indemnityDuty)){
		 dutyCode="3";//??????
	 }else if("3".equals(indemnityDuty)){
		 dutyCode="4";//??????
	 }else if("4".equals(indemnityDuty)){
		 dutyCode="5";//??????
	 }
	 return dutyCode;
 }
 
}
