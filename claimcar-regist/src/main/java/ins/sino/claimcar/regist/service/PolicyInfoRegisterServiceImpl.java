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
     * 保单信息写入平台接口
     */
	@Override
	public void policyInfoRegister(String registNo,String type,SysUserVo userVo) {
		String url="";//请求url
		String reqjsonString="";//请求报文
		String resjsonString="";//返回报文
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();//日志类
		logVo.setComCode(userVo.getComCode());
		logVo.setServiceType(type);;//B--商业险C--交强险标志 
		logVo.setRegistNo(registNo);//报案号
		logVo.setCreateTime(new Date());//请求时间
		logVo.setCreateUser(userVo.getUserCode());
		logVo.setOperateNode("endCase");//结案
		List<PrpLCMainVo> prpLCMainVos= prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(registNo);//报案信息
		PrpLCheckDutyVo prpLCheckDutyVo=checkTaskService.findCheckDuty(registNo, 1);
		PrpLCheckVo prpLCheckVo=checkHandleService.queryPrpLCheckVo(registNo);
		List<PrpLDlossCarMainVo> prpLDlossCarMainVos=deflossHandleService.findLossCarInfoByRegistNo(registNo);//定损信息表
		PrpLCheckTaskVo prpLCheckTaskVo=new PrpLCheckTaskVo();
		List<PrpLCheckPersonVo> checkPersonVos=new ArrayList<PrpLCheckPersonVo>();
		if(prpLCheckVo!=null){
			prpLCheckTaskVo=prpLCheckVo.getPrpLCheckTask();
			checkPersonVos=prpLCheckTaskVo.getPrpLCheckPersons();
		}
		CiPolicyInfoRegisterVo ciPolicyInfoRegisterVo=new CiPolicyInfoRegisterVo();
		BiPolicyInfoRegisterVo biPolicyInfoRegisterVo=new BiPolicyInfoRegisterVo();
		List<PrpLClaimVo> prpLClaimVos=claimService.findClaimListByRegistNo(registNo);//立案信息
		List<PrpLEndCaseVo> prpLEndCaseVos=endCaseService.findEndCaseVo(registNo);
		PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
		List<PrpLWfTaskVo> wftaskVos=wfFlowQueryService.findTaskVoForInAndOut(registNo,FlowNode.ReOpen.name(),FlowNode.EndCas.name());
		try{
		if(prpLCMainVos!=null && prpLCMainVos.size()>0){
			for(PrpLCMainVo cmainVo:prpLCMainVos){
				if("C".equals(type) && "1101".equals(cmainVo.getRiskCode())){//交强险保单信息写入
					List<SysCodeDictVo> sysCodeDictVos=wfTaskQueryService.findPrpDcompanyByUserCode(cmainVo.getComCode(),null);
					InsuranceClaimVo insuranceClaimVo=new InsuranceClaimVo();
					insuranceClaimVo.setCOMPANY_ID(CodeConstants.DHICCODE);//公司代码
					insuranceClaimVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//理赔编码
					insuranceClaimVo.setCONFIRM_SEQUENCE_NO(cmainVo.getValidNo());//投保确认码
					insuranceClaimVo.setPOLICY_NO(cmainVo.getPolicyNo());//保单号码
					insuranceClaimVo.setREPORT_TIME(DateFormatString(prpLRegistVo.getReportTime()));//报案时间
					insuranceClaimVo.setREPORT_NO(prpLRegistVo.getRegistNo());//报案号
					insuranceClaimVo.setREPORTER_NAME(prpLRegistVo.getReportorName());//报案人姓名
					insuranceClaimVo.setACCIDENT_TIME(DateFormatString(prpLRegistVo.getDamageTime()));//出险时间
					insuranceClaimVo.setACCIDENT_PLACE(prpLRegistVo.getDamageAddress());//出险地点
					insuranceClaimVo.setACCIDENT_DESCRIPTION(prpLRegistExtVo.getDangerRemark());//出险经过
					insuranceClaimVo.setACCIDENT_CAUSE(damageCodeChanged(prpLCheckVo.getDamageCode()));//出险原因代码
					insuranceClaimVo.setACCIDENT_LIABILITY(prpLCheckDutyVo!=null?indemnityDuty(prpLCheckDutyVo.getIndemnityDuty()):null);//交强险事故责任划分代码
					insuranceClaimVo.setMANAGE_TYPE(prpLRegistExtVo.getManageType());//交强险事故处理方式代码
					if(prpLClaimVos!=null && prpLClaimVos.size()>0){
						for(PrpLClaimVo claimVo:prpLClaimVos){
							if("1101".equals(claimVo.getRiskCode())){
								insuranceClaimVo.setREGISTRATION_NO(claimVo.getClaimNo());//立案号
								insuranceClaimVo.setREGISTRATION_TIME(DateFormatString(claimVo.getClaimTime()));//立案时间
								insuranceClaimVo.setESTIMATE_AMOUNT(claimVo.getSumDefLoss()+"");//估损金额
								insuranceClaimVo.setCLAIM_TYPE("1");//理赔类型代码
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
					BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
					if(prpLCompensateVos != null && !prpLCompensateVos.isEmpty()){
						for(PrpLCompensateVo compensate : prpLCompensateVos){
							//车
							List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
							if(lossItemVoList != null && !lossItemVoList.isEmpty()){
								for(PrpLLossItemVo itemVo : lossItemVoList){// 车
									if(!PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){//排除无责代赔
										claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
										
									}
								}
								
								//财产
								List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
								if(lossPropVoList != null && !lossPropVoList.isEmpty()){
									for(PrpLLossPropVo propVo : lossPropVoList){
										claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
										
									}
								}
								
								//人
								List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
								if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
									for(PrpLLossPersonVo personVo : lossPersoVoList){// 人
										for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
										    claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
										}
									}
								}
							}
						}
					}
					
					insuranceClaimVo.setCLAIM_AMOUNT(claimAmount.toString());//赔款总金额
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
					insuranceClaimVo.setPAY_SELF_FLAG(prpLCheckVo.getIsClaimSelf());//互碰自赔标志
					insuranceClaimVo.setACCIDENT_TYPE(riskCodeChanged(prpLCheckVo.getDamageCode()));//交强险保险事故分类代码
				    if(sysCodeDictVos!=null && sysCodeDictVos.size()>0){
				    	insuranceClaimVo.setCITY_CODE(StringUtils.isNotBlank(sysCodeDictVos.get(0).getSysAreaCode())?sysCodeDictVos.get(0).getSysAreaCode().substring(0, 4)+"00":null);//保单归属地（地市）
				    	insuranceClaimVo.setCOUNTY_CODE(sysCodeDictVos.get(0).getSysAreaCode());// 保单归属地（区县）
				    }
				    // 判断是否重开赔案
				    String isReopenClaim="0";//0--不是，1--是
					if(wftaskVos!=null && wftaskVos.size()>0){
						for(PrpLWfTaskVo vo:wftaskVos){
							if("1101".equals(vo.getRiskCode())){
							     isReopenClaim="1";
								 break;
							}
							
						}
					}
					insuranceClaimVo.setREOPEN_CASE(isReopenClaim);//是否重开赔案
					ciPolicyInfoRegisterVo.setInsuranceClaim(insuranceClaimVo);//交强险理赔详情信息
					InsuranceClaimOutDangerVo insuranceClaimOutDangerVo=new InsuranceClaimOutDangerVo();
					List<InsuranceClaimPeerVo> insuranceClaimPeers=new ArrayList<InsuranceClaimPeerVo>();
					insuranceClaimOutDangerVo.setCOMPANY_ID(CodeConstants.DHICCODE);//公司代码
					insuranceClaimOutDangerVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//理赔编码
					if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
						for(PrpLDlossCarMainVo mainvo:prpLDlossCarMainVos){
							if("1".equals(mainvo.getDeflossCarType())){
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									insuranceClaimOutDangerVo.setVEHICLE_TYPE("99");//出险车辆号牌种类
								}else{
									insuranceClaimOutDangerVo.setVEHICLE_TYPE(prpLDlossCarInfoVo.getLicenseType());//出险车辆号牌种类
								}
								
								insuranceClaimOutDangerVo.setCAR_MARK(prpLDlossCarInfoVo.getLicenseNo());//出险车辆号牌号码
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
								    	insuranceClaimOutDangerVo.setVEHICLE_CATEGORY("11");//出险车辆种类代码--为空，默认
								    }
									insuranceClaimOutDangerVo.setRACK_NO(prpLDlossCarInfoVo.getVinNo());//出险车辆车架号
									insuranceClaimOutDangerVo.setENGINE_NO(prpLDlossCarInfoVo.getEngineNo());//出险车辆发动机号
									insuranceClaimOutDangerVo.setDRIVER_NAME(prpLDlossCarInfoVo.getDriveName());//出险驾驶员姓名
									insuranceClaimOutDangerVo.setDRIVER_LICENSE_NO(prpLDlossCarInfoVo.getDrivingLicenseNo());//出险驾驶员证件号码
									insuranceClaimOutDangerVo.setREPAIR_FACTORY_NAME(mainvo.getRepairFactoryName());//修理机构名称
									if("03".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("1");//定损方式--全损
									}else if("02".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("2");//定损方式--推定全损
									}else if("01".equals(mainvo.getCetainLossType())){
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("3");//定损方式--部分损失
									}else{
										insuranceClaimOutDangerVo.setESTIMATE_TYPE("4");//定损方式--其他
									}
									ciPolicyInfoRegisterVo.setInsuranceClaimOutDangers(insuranceClaimOutDangerVo);//标的车出险车辆信息
									
								}else{
									InsuranceClaimPeerVo insuranceClaimPeerVo=new InsuranceClaimPeerVo();
									PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
									insuranceClaimPeerVo.setCOMPANY_ID(CodeConstants.DHICCODE);//公司代码
									insuranceClaimPeerVo.setCLAIM_CODE(cmainVo.getClaimSequenceNo());//理赔编码
									insuranceClaimPeerVo.setDRIVER_NAME(prpLDlossCarInfoVo.getDriveName());//三者车驾驶员姓名
									insuranceClaimPeerVo.setCAR_MARK(prpLDlossCarInfoVo.getLicenseNo());//三者车辆号牌号码
									insuranceClaimPeerVo.setVEHICLE_TYPE(prpLDlossCarInfoVo.getLicenseType());//三者车辆号码种类
									insuranceClaimPeerVo.setRACK_NO(prpLDlossCarInfoVo.getVinNo());//车架号
									insuranceClaimPeerVo.setENGINE_NO(prpLDlossCarInfoVo.getEngineNo());//三者车辆发动机号
									insuranceClaimPeerVo.setREPAIR_FACTORY_NAME(mainvo.getRepairFactoryName());//修理机构名称
									insuranceClaimPeerVo.setCERTI_CODE(prpLDlossCarInfoVo.getDrivingLicenseNo());//三者车驾驶员证件号码
									if("03".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("1");//定损方式--全损
									}else if("02".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("2");//定损方式--推定全损
									}else if("01".equals(mainvo.getCetainLossType())){
										insuranceClaimPeerVo.setESTIMATE_TYPE("3");//定损方式--部分损失
									}else{
										insuranceClaimPeerVo.setESTIMATE_TYPE("4");//定损方式--其他
									}
									insuranceClaimPeers.add(insuranceClaimPeerVo);

							     }
							    
						}
						
						ciPolicyInfoRegisterVo.setInsuranceClaimPeers(insuranceClaimPeers);//交强险报案第三方车辆信息
					}
					
					
					
					
				}else if("B".equals(type) && !"1101".equals(cmainVo.getRiskCode())){//商业险保单信息写入
					List<SysCodeDictVo> sysCodeDictVos=wfTaskQueryService.findPrpDcompanyByUserCode(cmainVo.getComCode(),null);
					CommercialClaimVo commercialClaimVo=new CommercialClaimVo();
					commercialClaimVo.setInsurerCode(CodeConstants.DHICCODE);//公司代码
					commercialClaimVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//理赔编码
					commercialClaimVo.setConfirmSequenceNo(cmainVo.getValidNo());//投保确认码
					commercialClaimVo.setPolicyNo(cmainVo.getPolicyNo());//保单号码
					commercialClaimVo.setNotificationTime(DateFormatString(prpLRegistVo.getReportTime()));//报案时间
					commercialClaimVo.setClaimNotificationNo(cmainVo.getRegistNo());//报案号
					commercialClaimVo.setReporter(prpLRegistVo.getReportorName());//报案人姓名
					commercialClaimVo.setLossTime(DateFormatString(prpLRegistVo.getDamageTime()));//出险时间
					commercialClaimVo.setLossArea(prpLRegistVo.getDamageAddress());//出险地点
					commercialClaimVo.setLossDesc(prpLRegistExtVo.getDangerRemark());//出险经过
					commercialClaimVo.setLossCauseCode(damageCodeChanged(prpLCheckVo.getDamageCode()));//出险原因代码
					commercialClaimVo.setAccidentLiability(prpLCheckDutyVo!=null?indemnityDuty(prpLCheckDutyVo.getIndemnityDuty()):null);;//事故责任划分代码
					commercialClaimVo.setOptionType(prpLRegistExtVo.getManageType());//事故处理方式代码
					if(prpLClaimVos!=null && prpLClaimVos.size()>0){
						for(PrpLClaimVo claimVo:prpLClaimVos){
							if(!"1101".equals(claimVo.getRiskCode())){
								commercialClaimVo.setClaimRegistrationNo(claimVo.getClaimNo());//立案号
								commercialClaimVo.setClaimRegistrationTime(DateFormatString(claimVo.getClaimTime()));//立案时间
								commercialClaimVo.setEstimatedLossAmount(claimVo.getSumDefLoss()+"");//估损金额
								commercialClaimVo.setClaimType("1");//理赔类型代码
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
					BigDecimal claimAmount = BigDecimal.ZERO;//总赔款金额
					if(prpLCompensateVos != null && !prpLCompensateVos.isEmpty()){
						for(PrpLCompensateVo compensate : prpLCompensateVos){
							//车
							List<PrpLLossItemVo> lossItemVoList = compensate.getPrpLLossItems();
							if(lossItemVoList != null && !lossItemVoList.isEmpty()){
								for(PrpLLossItemVo itemVo : lossItemVoList){// 车
									if(!PayFlagType.NODUTY_INSTEAD_PAY.equals(itemVo.getPayFlag())){//排除无责代赔
										claimAmount = claimAmount.add(DataUtils.NullToZero(itemVo.getSumRealPay()));
										
									}
								}
								
								//财产
								List<PrpLLossPropVo> lossPropVoList = compensate.getPrpLLossProps();
								if(lossPropVoList != null && !lossPropVoList.isEmpty()){
									for(PrpLLossPropVo propVo : lossPropVoList){
										claimAmount = claimAmount.add(DataUtils.NullToZero(propVo.getSumRealPay()));
										
									}
								}
								
								//人
								List<PrpLLossPersonVo> lossPersoVoList = compensate.getPrpLLossPersons();
								if(lossPersoVoList != null && !lossPersoVoList.isEmpty()){
									for(PrpLLossPersonVo personVo : lossPersoVoList){// 人
										for(PrpLLossPersonFeeVo feeVo:personVo.getPrpLLossPersonFees()){
										    claimAmount = claimAmount.add(DataUtils.NullToZero(feeVo.getFeeRealPay()));
										}
									}
								}
							}
						}
					}
					commercialClaimVo.setClaimAmount(claimAmount.toString());//赔款总金额
					if(!"4".equals(prpLCheckDutyVo.getIndemnityDuty())){//是否属于保险责任
						commercialClaimVo.setIsInsured("1");
					}else{
						commercialClaimVo.setIsInsured("0");
					}
					commercialClaimVo.setAccidentType(riskCodeChanged(prpLCheckVo.getDamageCode()));//保险事故分类
					commercialClaimVo.setIsTotalLoss(prpLCheckVo.getLossType());//是否全损
					if(sysCodeDictVos!=null && sysCodeDictVos.size()>0){
						commercialClaimVo.setCityCode(StringUtils.isNotBlank(sysCodeDictVos.get(0).getSysAreaCode())?sysCodeDictVos.get(0).getSysAreaCode().substring(0, 4)+"00":null);//保单归属地（地市）
						commercialClaimVo.setCountyCode(sysCodeDictVos.get(0).getSysAreaCode());// 保单归属地（区县）
				    }
					 // 判断是否重开赔案
				    String isReopenClaim="0";//0--不是，1--是
				    if(wftaskVos!=null && wftaskVos.size()>0){
						for(PrpLWfTaskVo vo:wftaskVos){
							if(!"1101".equals(vo.getRiskCode())){
									isReopenClaim="1";
									break;
							}
							
						}
					}
					commercialClaimVo.setReopenCase(isReopenClaim);//是否重开赔案
					biPolicyInfoRegisterVo.setCommercialClaim(commercialClaimVo);
					List<CommercialClaimPeerVo> commercialClaimPeers=new ArrayList<CommercialClaimPeerVo>();//三者车出险信息
					if(prpLDlossCarMainVos!=null && prpLDlossCarMainVos.size()>0){
						for(PrpLDlossCarMainVo mainvo:prpLDlossCarMainVos){
							if("1".equals(mainvo.getDeflossCarType())){
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								CommercialClaimOutDangerVo commercialClaimOutDangerVo=new CommercialClaimOutDangerVo();//标的车出现车辆信息
								commercialClaimOutDangerVo.setInsurerCode(CodeConstants.DHICCODE);//公司代码
								commercialClaimOutDangerVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//理赔编码
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									commercialClaimOutDangerVo.setLicensePlateType("99");//出险车辆号牌种类
								}else{
									commercialClaimOutDangerVo.setLicensePlateType(prpLDlossCarInfoVo.getLicenseType());//出险车辆号牌种类
								}
								commercialClaimOutDangerVo.setLicensePlateNo(prpLDlossCarInfoVo.getLicenseNo());//出险车辆号牌号码
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
									commercialClaimOutDangerVo.setMotorTypeCode("11");//出险车辆种类代码--为空，默认
							    }
								commercialClaimOutDangerVo.setVIN(prpLDlossCarInfoVo.getVinNo());//出险车辆车架号
								commercialClaimOutDangerVo.setEngineNo(prpLDlossCarInfoVo.getEngineNo());//出险车辆发动机号
								commercialClaimOutDangerVo.setDriverName(prpLDlossCarInfoVo.getDriveName());//出险驾驶员姓名
								commercialClaimOutDangerVo.setCertiCode(prpLDlossCarInfoVo.getDrivingLicenseNo());//出险驾驶员证件号码
								commercialClaimOutDangerVo.setRepairFactoryName(mainvo.getRepairFactoryName());//修理机构名称
								if("03".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("1");//定损方式--全损
								}else if("02".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("2");//定损方式--推定全损
								}else if("01".equals(mainvo.getCetainLossType())){
									commercialClaimOutDangerVo.setEstimateType("3");//定损方式--部分损失
								}else{
									commercialClaimOutDangerVo.setEstimateType("4");//定损方式--其他
								}
								biPolicyInfoRegisterVo.setCommercialClaimOutDangers(commercialClaimOutDangerVo);//商业险标的车信息
							}else{
								CommercialClaimPeerVo commercialClaimPeerVo=new CommercialClaimPeerVo();
								PrpLDlossCarInfoVo prpLDlossCarInfoVo=lossCarService.findDefCarInfoByPk(mainvo.getCarId());
								commercialClaimPeerVo.setInsurerCode(CodeConstants.DHICCODE);//公司代码
								commercialClaimPeerVo.setClaimSequenceNo(cmainVo.getClaimSequenceNo());//理赔编码
								commercialClaimPeerVo.setDriverName(prpLDlossCarInfoVo.getDriveName());//三者车驾驶员姓名
								commercialClaimPeerVo.setCertiCode(prpLDlossCarInfoVo.getDrivingLicenseNo());//三者车驾驶证件号码
								if("25".equals(prpLDlossCarInfoVo.getLicenseType())){
									commercialClaimPeerVo.setLicensePlateType("99");//出险车辆号牌种类
								}else{
									commercialClaimPeerVo.setLicensePlateType(prpLDlossCarInfoVo.getLicenseType());//出险车辆号牌种类
								}
								commercialClaimPeerVo.setLicensePlateNo(prpLDlossCarInfoVo.getLicenseNo());//三者车辆号牌号码
								commercialClaimPeerVo.setVIN(prpLDlossCarInfoVo.getVinNo());//三者车辆车架号
								commercialClaimPeerVo.setEngineNo(prpLDlossCarInfoVo.getEngineNo());//三者车辆发动机号
								commercialClaimPeerVo.setRepairFactoryName(mainvo.getRepairFactoryName());//修理机构名称
								if("03".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("1");//定损方式--全损
								}else if("02".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("2");//定损方式--推定全损
								}else if("01".equals(mainvo.getCetainLossType())){
									commercialClaimPeerVo.setEstimateType("3");//定损方式--部分损失
								}else{
									commercialClaimPeerVo.setEstimateType("4");//定损方式--其他
								}
								commercialClaimPeers.add(commercialClaimPeerVo);//出险三者车信息
							}
						}
					}
					
					biPolicyInfoRegisterVo.setCommercialClaimPeers(commercialClaimPeers);//商业险三者车信息
				}
			}
		}
		if("C".equals(type)){
			url = SpringProperties.getProperty("SC_CIURL");
			Gson gson = new Gson();
			reqjsonString=gson.toJson(ciPolicyInfoRegisterVo);
			logger.info("交强保险信息写入接口请求报文：-----------》{}",reqjsonString);
			RespVo respVo=urlPost(reqjsonString,url);//返回值
			if(respVo!=null && JSONObject.fromObject(respVo)!=null){
				resjsonString=JSONObject.fromObject(respVo).toString();
			}
			if(respVo!=null && "E0000".equals(respVo.getErrcode())){
				logVo.setStatus("1");
				logVo.setErrorCode("E0000");
				logVo.setErrorMessage("成功");
			}else{
				if(respVo!=null){
					logVo.setStatus("0");
					logVo.setErrorCode(respVo.getErrcode());
					logVo.setErrorMessage(respVo.getMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("-->网络请求出错！");
				}
				
			}
		}else{
			url = SpringProperties.getProperty("SC_BIURL");
			Gson gson = new Gson();
			reqjsonString=gson.toJson(biPolicyInfoRegisterVo);
			logger.info("商业保险信息写入接口请求报文：-----------》{}",reqjsonString);
			RespVo respVo=urlPost(reqjsonString,url);//返回值
			if(respVo!=null && JSONObject.fromObject(respVo)!=null){
				resjsonString=JSONObject.fromObject(respVo).toString();
			}
			if(respVo!=null && "E0000".equals(respVo.getErrcode())){
				logVo.setStatus("1");
				logVo.setErrorCode("E0000");
				logVo.setErrorMessage("成功");
			}else{
				if(respVo!=null && StringUtils.isNotBlank(respVo.getErrcode())){
					logVo.setStatus("0");
					logVo.setErrorCode(respVo.getErrcode());
					logVo.setErrorMessage(respVo.getMessage());
				}else{
					logVo.setStatus("0");
					logVo.setErrorMessage("-->网络请求出错！");
				}
				
			}
		
		
		}
	   }catch(Exception e){
		    logVo.setStatus("0");
			logVo.setErrorMessage("catch异常:"+e.getMessage());
		    logger.error("保险信息写入接口异常信息：-----------》",e);
	   }finally{
		   logVo.setBusinessType(BusinessType.SC_policyInfo.name());
		   logVo.setBusinessName(BusinessType.SC_policyInfo.getName());
		   logVo.setRequestUrl(url);
		   logVo.setRequestXml(reqjsonString);
		   logVo.setResponseXml(resjsonString);
		   claimInterfaceLogService.save(logVo);
	   }
	}
	//获取有效的理算数据
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
	 * 请求方法
	 * @param params
	 * @param type
	 * @return
	 */
	private RespVo urlPost(String params,String url)throws Exception{
		RespVo respVo=new RespVo();
		String code=CodeConstants.DHICCODE;
		String secret = SpringProperties.getProperty("SC_secret");
		String data = new String(Base64.encodeBase64(params.getBytes("UTF-8")));;//base64处理
		String reqData=URLEncoder.encode(data,"UTF-8");//上传时需要对data进行URLEncode编码
        logger.info("请求保险信息写入接口url---------------------------"+url);
		//准备参数
		String signed=MD5Util.getMd5(secret+data);
		String securityType=CodeConstants.secretType;//加密方式
		String version=CodeConstants.SCVERSION;//版本
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
			connection.setDoInput(true); // 设置可以进行输入操作
			connection.setDoOutput(true); // 设置可以进行输出操作
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setConnectTimeout(10000);// 设置连接主机的时间
			connection.setReadTimeout(10000);// 设置从主机读取文件的时间
			connection.setAllowUserInteraction(true); // 设置可以进行交互操作
			connection.connect();
			outputStream = connection.getOutputStream();
			writer = new OutputStreamWriter(outputStream,"UTF-8");
			writer.write(param);
			writer.flush(); // 立即输出
			writer.close();
			inputStream = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8")); // 为了进行一行读取操作
			while ((strMessage = reader.readLine()) != null) {
				buffer.append(strMessage);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			throw new Exception("与平台连接失败，请稍候再试", ex);
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
		logger.info("保险信息写入接口返回报文======="+buffer);
		JSONObject resJson= JSONObject.fromObject(buffer.toString());
		if(resJson!=null){
			respVo=(RespVo)JSONObject.toBean(resJson,RespVo.class);
		}
		
	    return respVo;
	}
	/**
	 * 去掉右空格
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
  * 出险原因编码转换
  * @param damageCode
  * @return
  */
 private String damageCodeChanged(String damageCode){
	 String damageCodeC="A10096";//其他机损意外事故
	 if("DM01".equals(damageCode)){
		 damageCodeC="A10029";//碰撞
	 }else if("DM02".equals(damageCode)){
		 damageCodeC="A10022";//玻璃单独破碎
	 }else if("DM03".equals(damageCode)){
		 damageCodeC="A10058";//车身划痕
	 }else if("DM04".equals(damageCode)){
		 damageCodeC="A10023";//全车盗抢
	 }else if("DM05".equals(damageCode)){
		 damageCodeC="A10096";//其他意外事故
	 }else if("DM51".equals(damageCode)){
		 damageCodeC="A10031";//倾覆
	 }else if("DM06".equals(damageCode)){
		 damageCodeC="A10003";//暴雨
	 }else if("DM07".equals(damageCode)){
		 damageCodeC="A10006";//台风
	 }else if("DM08".equals(damageCode)){
		 damageCodeC="A10025";//外界物体坠落
	 }else if("DM09".equals(damageCode)){
		 damageCodeC="A10001";//火灾
	 }else if("DM10".equals(damageCode)){
		 damageCodeC="A19001";//高坠
	 }else if("DM12".equals(damageCode)){
		 damageCodeC="A10096";//其他意外事故
	 }
	 return damageCodeC;
 }
 /**
  * 交强保险事故分类代码编码转换
  * @param damageCode
  * @return
  */
 private String riskCodeChanged(String damageCode){
	 String damageCodeC="100";//交通事故类
	 if("DM01".equals(damageCode)){
		 damageCodeC="111";//碰撞
	 }else if("DM02".equals(damageCode)){
		 damageCodeC="221";//玻璃单独破碎
	 }else if("DM03".equals(damageCode)){
		 damageCodeC="222";//车身划痕
	 }else if("DM04".equals(damageCode)){
		 damageCodeC="204";//全车盗抢
	 }else if("DM05".equals(damageCode)){
		 damageCodeC="299";//其它非交通事故类保险事故
	 }else if("DM51".equals(damageCode)){
		 damageCodeC="299";//其它非交通事故类保险事故
	 }else if("DM06".equals(damageCode)){
		 damageCodeC="504";//暴雨
	 }else if("DM07".equals(damageCode)){
		 damageCodeC="502";//暴风
	 }else if("DM08".equals(damageCode)){
		 damageCodeC="242";//外界物体坠落
	 }else if("DM09".equals(damageCode)){
		 damageCodeC="201";//火灾
	 }else if("DM10".equals(damageCode)){
		 damageCodeC="299";//高坠
	 }else if("DM12".equals(damageCode)){
		 damageCodeC="900";//其它保险事故
	 }
	 return damageCodeC;
 }
 /**
  *责任比例转化
  * @param indemnityDuty
  * @return
  */
 private String indemnityDuty(String indemnityDuty){
	 String dutyCode="5";
	 if("0".equals(indemnityDuty)){
		 dutyCode="1";//全责
	 }else if("1".equals(indemnityDuty)){
		 dutyCode="2";//主责
	 }else if("2".equals(indemnityDuty)){
		 dutyCode="3";//同责
	 }else if("3".equals(indemnityDuty)){
		 dutyCode="4";//次责
	 }else if("4".equals(indemnityDuty)){
		 dutyCode="5";//无责
	 }
	 return dutyCode;
 }
 
}
