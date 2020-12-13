package ins.sino.claimcar.certifyIlog.service;





import ins.framework.utils.Beans;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.certify.AccidentResponsibilityVo;
import ins.sino.claimcar.ilog.certify.ReqRoot;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.RuleReturnDataSaveService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.ILPersonnelInfoVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "certifyIlogService")
public class CertifyIlogServiceImpl implements CertifyIlogService{
	private static Logger logger = LoggerFactory.getLogger(CertifyIlogServiceImpl.class);
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	private PropTaskService propTaskService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	ManagerService managerService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	private ClaimService claimService;
	@Autowired
    ClaimInterfaceLogService interfaceLogService;
	@Autowired
	RuleReturnDataSaveService ruleReturnDataSaveService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	private CertifyService certifyService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	PersTraceService persTraceService;
	
	@Override
	public LIlogRuleResVo sendAutoCertifyRule(String registNo,SysUserVo userVo,BigDecimal taskId,String codeNode) {
		ReqRoot root = reqRootForParams(registNo,userVo,taskId,codeNode);// 组装数据
		LIlogRuleResVo resRoot=new  LIlogRuleResVo();
		String url = SpringProperties.getProperty("ILOG_SVR_URL");// 获取ILOG规则服务地址
		
		if(FlowNode.Certi.toString().equals(codeNode)){
			url = url+"CheckRuleForCarAdjustmentServlet";// 自动理算请求的Url
		}else{
			url = url+"CheckRuleForCarDocumentServlet";// 自动单证请求的Url
		}
		String requestXML = "";// 请求的报文
		String responseXML = "";// 返回的报文
		requestXML = ClaimBaseCoder.objToXml(root);// 转为xml
		logger.info("ILOG车险单证/理算对外服务接口sendXML--------------------------->"+requestXML);
		
		try {
			responseXML = requestCertifyIlog(requestXML,url,200);// 返回报文
			logger.info("ILOG车险单证/理算对外服务接口返回XML--------------------------->"+responseXML);
			resRoot=ClaimBaseCoder.xmlToObj(responseXML, LIlogRuleResVo.class);
			saveIlogRule(resRoot,registNo,userVo,taskId,codeNode);// 保存Ilog规则返回信息
		} catch (Exception e) {
			logger.info("ILOG车险单证/理算对外服务接口返回错误信息："+e.getMessage());
			e.printStackTrace();
		}
		return resRoot;
	}

	/**
	 * 组装请求数据
	 * @param registNo
	 * @param comCode
	 * @return
	 */
	private ReqRoot reqRootForParams(String registNo,SysUserVo userVo,BigDecimal taskId,String codeNode){
		//FlowNode.Certi.toString().equals(codeNode)
		ReqRoot root=new ReqRoot();//CodeConstants
		PrpLCertifyMainVo prpLCertifyMainVo=certifyService.findPrpLCertifyMainVo(registNo);
		PrpLSubrogationMainVo subrogationMainVo=subrogationService.find(registNo);
		List<AccidentResponsibilityVo> bilityVoList=new ArrayList<AccidentResponsibilityVo>();
		// 定损总金额
		BigDecimal sumAmout=new BigDecimal("0");
		// 车物定损总金额
		BigDecimal cwSumAmout=new BigDecimal("0");
		// 人伤费用审核金额
		BigDecimal renSumAmout=new BigDecimal("0");
		// 责任比例之和
		BigDecimal sumProportion = new BigDecimal("0");
		int carTimes = 0;// 有效的车辆定损数
		String casualtiesCaseFlag = "0";// 是否涉及人伤0-否，1-是
		String directFlag = "0";// 是否单证齐全
		// 标的车车辆信息
		PrpLDlossCarMainVo losscarVo=new PrpLDlossCarMainVo();
		// 定损车辆信息
		List<PrpLDlossCarMainVo> losscarMainList=deflossHandleService.findLossCarMainByRegistNo(registNo);
		// 人伤定损信息
		List<PrpLDlossPersTraceMainVo> losspersTraceList=deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
		// 财产定损信息
		List<PrpLdlossPropMainVo> propmianList=propTaskService.findPropMainListByRegistNo(registNo);
		// 查勘主表信息
		PrpLCheckVo checkVo=checkTaskService.findCheckVoByRegistNo(registNo);
		// 查勘责任比例列表
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		
		// 收款人信息列表
		List<PrpLPayCustomVo> payCustomVos=payCustomService.findPayCustomVoByRegistNo(registNo);
		// 查看in和out表预付
		List<PrpLWfTaskVo>  preInVos=wfFlowQueryService.findTaskVoForInByNodeCode(registNo,FlowNode.PrePay.name());
		List<PrpLWfTaskVo>  preOutVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo,FlowNode.PrePay.name());
		// 查看in和out表预付
		List<PrpLWfTaskVo>  padInVos=wfFlowQueryService.findTaskVoForInByNodeCode(registNo,FlowNode.PadPay.name());
		List<PrpLWfTaskVo>  padOutVos=wfFlowQueryService.findTaskVoForOutByNodeCode(registNo,FlowNode.PadPay.name());
		// 保单表信息列表
		List<PrpLCMainVo> cmainVoList=policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		// 人伤案件查勘是否费用审核自动通过
		String isAutoRenflag="0";
		List<PrpLWfTaskVo> wfTaskVos=wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo,"PLCharge_LV0");
		if(wfTaskVos!=null && wfTaskVos.size()>0){
			for(PrpLWfTaskVo vo:wfTaskVos){
				if("3".equals(vo.getWorkStatus())){
					isAutoRenflag="1";
				}
			}
		}
		// 判断是否公估查勘
		List<String> nodeCodes=new ArrayList<String>();
		nodeCodes.add(FlowNode.Check.name());
		String isAssessmentSurvey = "0";// 是否公估查勘
		String isReopenClaim = "0";// 是否重开赔案
		// 查询out表
		List<PrpLWfTaskVo> taskOutVos=wfTaskHandleService.findPrpLWfTaskByregistNoAndnodeCode(registNo,"2",nodeCodes);
		if(taskOutVos!=null && taskOutVos.size()>0){
			for(PrpLWfTaskVo vo:taskOutVos){
				if(CodeConstants.WorkStatus.END.equals(vo.getWorkStatus())){
					PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(vo.getHandlerUser());
					if(prplIntermMainVo!=null){
						isAssessmentSurvey="1";
					}
				}
			}
		}
		List<String> nodes=new ArrayList<String>();
		nodes.add(FlowNode.Check.name());
		nodes.add(FlowNode.DLoss.name());
		nodes.add(FlowNode.PLoss.name());
		
		
		// 查询out表-判定是否为代查勘案件
		List<PrpLWfTaskVo> wftaskOutVos=wfTaskHandleService.findPrpLWfTaskByregistNoAndnodeCode(registNo,"2",nodes);
		String isSurveyCase = "0";// 是否代查勘案件0-不是，1-是
		if(wftaskOutVos!=null && wftaskOutVos.size()>0){
			for(PrpLWfTaskVo vo:wftaskOutVos){
				if("1".equals(vo.getSubCheckFlag()) || "2".equals(vo.getSubCheckFlag())){
					isSurveyCase="1";
					break;
				}
			}
		}
		
		String containsPre = "0";// 是否包含预付，0-不包含，1-包含
		String containsPad = "0";// 是否包含垫付，0-不包含，1-包含
		if(preInVos!=null && preInVos.size()>0){
			containsPre="1";
			
		}
		if(!"1".equals(containsPre)){
			if(preOutVos!=null && preOutVos.size()>0){
				for(PrpLWfTaskVo vo:preOutVos){
					if(!CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())){
						containsPre="1";
						break;
					}
				}
			}
		}
		if(padInVos!=null && padInVos.size()>0){
			containsPad="1";
				
		}
		if(!"1".equals(containsPad)){
			if(padOutVos!=null && padOutVos.size()>0){
				for(PrpLWfTaskVo vo:padOutVos){
					if(!CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())){
						containsPad="1";
						break;
					}
				}
			}
		}	
		wfFlowQueryService.findTaskVoForOutByNodeCode(registNo,FlowNode.PrePay.name());
		if(losscarMainList!=null && losscarMainList.size()>0){
			for(PrpLDlossCarMainVo vo:losscarMainList){
				sumAmout=sumAmout.add(new BigDecimal(vo.getSumVeriLossFee()==null? "0":vo.getSumVeriLossFee().toString()));
				cwSumAmout=cwSumAmout.add(new BigDecimal(vo.getSumVeriLossFee()==null? "0":vo.getSumVeriLossFee().toString()));
				if("1".equals(vo.getValidFlag())){
					carTimes++;
				}
				if("1".equals(vo.getDeflossCarType()) && !FlowNode.Certi.toString().equals(codeNode)){
					losscarVo=vo;
					directFlag=vo.getDirectFlag();
				}
			}
		}
		if(FlowNode.Certi.toString().equals(codeNode) && prpLCertifyMainVo!=null){
			directFlag=prpLCertifyMainVo.getCollectFlag();
			
		}

		if("1".equals(directFlag)){
			Map<String, String> map = new HashMap<String, String>();
			boolean res = false;
			map.put("registNo", registNo);
			List<PrpLCertifyItemVo> prpLCertifyItemVos = certifyService.findPrpLCertifyItemVoList(map);
			if(prpLCertifyItemVos==null||prpLCertifyItemVos.size()==0){
				directFlag="0";
			}else {
				for(PrpLCertifyItemVo prpLCertifyItemVo:prpLCertifyItemVos){
					List<PrpLCertifyDirectVo> prpLCertifyDirectVos = prpLCertifyItemVo.getPrpLCertifyDirects();
					if(prpLCertifyDirectVos!=null){
						for (PrpLCertifyDirectVo prpLCertifyDirectVo : prpLCertifyDirectVos) {
							if("C0101".equals(prpLCertifyDirectVo.getLossItemCode())){
								res = true;
							}
						}
					}
				}
			}
			if(!res){
				directFlag = "0";
			}
		}
		
		if(losspersTraceList!=null && losspersTraceList.size()>0){
			for(PrpLDlossPersTraceMainVo vo:losspersTraceList){
				if(vo.getPrpLDlossPersTraces()!=null && vo.getPrpLDlossPersTraces().size() > 0){
					for(PrpLDlossPersTraceVo traceVo : vo.getPrpLDlossPersTraces()){
						sumAmout=sumAmout.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
						casualtiesCaseFlag="1";
						renSumAmout = renSumAmout.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
					}
				}
			}
		}
		if(propmianList!=null && propmianList.size()>0){
			for(PrpLdlossPropMainVo vo:propmianList){
				sumAmout=sumAmout.add(new BigDecimal(vo.getSumVeriLoss()==null? "0":vo.getSumVeriLoss().toString()));
				cwSumAmout=cwSumAmout.add(new BigDecimal(vo.getSumVeriLoss()==null? "0":vo.getSumVeriLoss().toString()));
			}
		}
		// 判断是否重开赔案
		List<PrpLEndCaseVo> endCases=endCasePubService.queryAllByRegistNo(registNo);
		if(endCases!=null && endCases.size()>0){
			for(PrpLEndCaseVo vo:endCases){
				PrpLClaimVo claimVo=claimService.findByClaimNo(vo.getClaimNo());
				if(claimVo.getEndCaseTime()==null){
					isReopenClaim="1";
					break;
				}
			}
		}
		String flag="0";
		if(losscarMainList!=null && losscarMainList.size()>0){
			for(PrpLDlossCarMainVo vo:losscarMainList){
				if("1".equals(vo.getIsWhethertheloss())){
					flag="1";
					break;
				}
			}
		}
		if(!"1".equals(flag) && propmianList!=null && propmianList.size()>0){
			for(PrpLdlossPropMainVo propmainVo:propmianList){
				if("1".equals(propmainVo.getIsWhethertheloss())){
					flag="1";
					break;
				}
			}
			
		}
		root.setIsinvolvedincarlaitp(flag);// 是否新增是否涉及车物的减损金额
		
		root.setIsFlagN("0");// 是否可疑交易
		root.setRegistNo(registNo);// 报案号
		root.setComCode(userVo.getComCode());// 机构代码
		root.setAuditFeeSumAmount(sumAmout.toString());// 审核定损金额合计
		root.setValidityCarFeeMissionNum(carTimes+"");// 有效车辆定损任务数量
		root.setCasualtiesCaseFlag(casualtiesCaseFlag);// 是否涉及人伤
		if(cmainVoList!=null && cmainVoList.size()>0){
			PrpLCMainVo  cmainVo=cmainVoList.get(0);
			List<PrpLCItemCarVo> prpLCItemCarVos=cmainVo.getPrpCItemCars();
			root.setCoinsFlag("0".equals(cmainVo.getCoinsFlag()) ? "0" : "1");// 是否联共保案件
			if(prpLCItemCarVos!=null && prpLCItemCarVos.size()>0){
				for(PrpLCItemCarVo vo:prpLCItemCarVos){
					root.setCarKindCode(vo.getCarType());// 承保车辆种类
				}
			}
		}
		if(FlowNode.Certi.toString().equals(codeNode) && prpLCertifyMainVo!=null){
			if (prpLCertifyMainVo.getIsJQFraud() == null) {
				root.setIsJQFraud("0");
			} else {
				root.setIsJQFraud(prpLCertifyMainVo.getIsJQFraud());// 是否交强拒赔
			}
			
			if (prpLCertifyMainVo.getIsSYFraud() == null) {
				root.setIsSYFraud("0");
			} else {
				root.setIsSYFraud(prpLCertifyMainVo.getIsSYFraud());// 是否商业拒赔
			}
			if(subrogationMainVo!=null){
				root.setSubrogationFlag(subrogationMainVo.getSubrogationFlag() == null ? "0":subrogationMainVo.getSubrogationFlag());// 是否代位求偿
			}
			root.setSurveyFlag(prpLCertifyMainVo.getSurveyFlag() == null ? "0" : prpLCertifyMainVo.getSurveyFlag());// 是否调查
			root.setLawsuitFlag(prpLCertifyMainVo.getLawsuitFlag() == null ? "0" : prpLCertifyMainVo.getLawsuitFlag());// 是否诉讼
		}else{
			root.setIsJQFraud(losscarVo.getIsCInotpayFlag() == null ? "0" : losscarVo.getIsCInotpayFlag());// 是否交强拒赔
			root.setIsSYFraud(losscarVo.getIsBInotpayFlag() == null ? "0" : losscarVo.getIsBInotpayFlag());// 是否商业拒赔
			if(checkVo!=null){
				root.setSubrogationFlag(checkVo.getIsSubRogation() == null ? "0" : checkVo.getIsSubRogation());// 是否代位求偿
			}
			root.setSurveyFlag("0");// 是否调查
			root.setLawsuitFlag("0");// 是否诉讼
		}
		
		
		root.setIsPrepaidType(containsPre);// 是否预付
		root.setIsAdvancesCcase(containsPad);// 是否垫付
		root.setIsAssessmentSurvey(isAssessmentSurvey);// 是否公估
		if(payCustomVos!=null){
			root.setPayeeInfoNum(payCustomVos.size()+"");// 报案号下收款人信息条数
		}
		root.setVehicleCertainAmount(cwSumAmout.toString());// 车物定损总金额
		root.setPauditAmount(renSumAmout.toString());// 人伤费用审核定损金额
		root.setIsReopenClaim(isReopenClaim);// 是否重开赔案
		root.setDirectFlag(directFlag);// 是否单证齐全
		root.setIsAuto(isAutoRenflag);// 人伤案件查勘是否费用审核自动通过
		if(cmainVoList!=null && cmainVoList.size()>0){
			PrpLCMainVo  cmainVo=cmainVoList.get(0);
			List<PrpLCItemCarVo> prpLCItemCarVos=cmainVo.getPrpCItemCars();
			if(prpLCItemCarVos!=null && prpLCItemCarVos.size()>0){
				for(PrpLCItemCarVo vo:prpLCItemCarVos){
					root.setUseKindCode(vo.getUseKindCode());// 使用性质代码
				}
			}
		}
		root.setIsSpecialOperationLicense(losscarVo.getIsSpecialcarFlag());// 是否有特种车操作证
		root.setIsBusinessCarCertificate(losscarVo.getIsBusinesscarFlag());// 是否有营业车资格证
		root.setIsSurveyCase(isSurveyCase);// 是否代查勘案件
		root.setIsFlagN("0");
		root.setSysAuthorizationFlag("1");
		if(losscarMainList!=null && losscarMainList.size()>0){
			for(PrpLDlossCarMainVo vo:losscarMainList){
				if(checkDutyList!=null && checkDutyList.size()>0){
					for(PrpLCheckDutyVo dutyVo:checkDutyList){
						if(vo.getSerialNo()==dutyVo.getSerialNo()){
							vo.setIndemnityDuty(dutyVo.getIndemnityDuty());// 事故责任比例
							vo.setIndemnityDutyRate(dutyVo.getIndemnityDutyRate());// 事故责任百分比
						}
					}
				}
			}
		}
		
		if (checkDutyList != null && checkDutyList.size() > 0) {
			for (PrpLCheckDutyVo checkdutyVo : checkDutyList) {
				if (checkdutyVo.getIndemnityDutyRate() != null) {
					sumProportion = sumProportion.add(checkdutyVo.getIndemnityDutyRate());
				}
			}
		}
		root.setSumProportion(sumProportion.toString());
		
		
		List<PrpLDlossCarInfoVo> lossCarInfoList = lossCarService.findPrpLDlossCarInfoVoListByRegistNo(registNo);// 定损车辆信息列表
		if(losscarMainList!=null && losscarMainList.size()>0){
			for(PrpLDlossCarMainVo vo:losscarMainList){
				AccidentResponsibilityVo bilityVo=new AccidentResponsibilityVo();
				bilityVo.setAccidentVehicleNo(vo.getLicenseNo());// 事故车辆车牌号
				bilityVo.setAccidentVehicleType(vo.getDeflossCarType());// 事故车辆类型
				bilityVo.setIndemnityDuty(vo.getIndemnityDuty());// 事故责任类型
				bilityVo.setIndemnityDutyRate(vo.getIndemnityDutyRate()+"");// 事故责任比例
				if("1".equals(vo.getDeflossCarType())){
					bilityVo.setCiPolicyNo("");// 交强险保单号
					bilityVo.setCiInsuredMechanism("");// 交强险保险公司
				}else if("3".equals(vo.getDeflossCarType())){
					if(lossCarInfoList!=null && lossCarInfoList.size()>0){
						for(PrpLDlossCarInfoVo infoVo:lossCarInfoList){
							if(StringUtils.isNotBlank(vo.getLicenseNo()) && vo.getLicenseNo().equals(infoVo.getLicenseNo())){
								bilityVo.setCiPolicyNo(infoVo.getCiPolicyNo());// 交强险保单号
								bilityVo.setCiInsuredMechanism(infoVo.getCiInsureComCode());// 交强险保险公司
							}
						}
					}
							
				}
					
				if (vo.getIsNodutypayFlag() == null ) {
					bilityVo.setNoDutyPayFlag("0");
				} else {
					bilityVo.setNoDutyPayFlag(vo.getIsNodutypayFlag());// 是否无责代赔
				}
				bilityVoList.add(bilityVo);
			
			}
		}
		List<PrpLDlossPersTraceVo> personVoList = persTraceDubboService.findPrpLDlossPersTraceVoListByRegistNo(registNo);
		List<ILPersonnelInfoVo> personnelInfoList = new ArrayList<ILPersonnelInfoVo>();
		if(personVoList!=null && personVoList.size()>0){
			for(PrpLDlossPersTraceVo personVo:personVoList){
				if(ValidFlag.VALID.equals(personVo.getValidFlag())){
					ILPersonnelInfoVo personnelInfoVo = new ILPersonnelInfoVo();
					personnelInfoVo.setLossType("3");
					personnelInfoVo.setLossItemType(personVo.getPrpLDlossPersInjured().getLossItemType());
					String lossPartyName = null;
					if(personVo.getPrpLDlossPersInjured().getSerialNo()!=null && personVo.getPrpLDlossPersInjured().getSerialNo()>1){
						lossPartyName = "2";// 三者车统一标识为2
					}else{
						lossPartyName =personVo.getPrpLDlossPersInjured().getSerialNo()==null? "1": personVo.getPrpLDlossPersInjured().getSerialNo().toString();
					}
					personnelInfoVo.setLossPartyName(lossPartyName);
					personnelInfoList.add(personnelInfoVo);
				}
				
			}
		}
		root.setPersonnelInfos(personnelInfoList);
		root.setBilitys(bilityVoList);
		return root;
	}
	
	/**
	 * 接口组装数据
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestCertifyIlog(String requestXML,String urlStr,int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
	        try {    
	         
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
			// 设置请求方式（GET/POST）
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
	            httpUrlConn.setConnectTimeout(seconds * 1000);
		        
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
			// 当有数据需要提交时
	            if (null != outputStr) {    
				// 注意编码格式，防止中文乱码 outputStream.write
	                outputStream.write(outputStr.getBytes("GBK"));    
	            }    
	    
			// 将返回的输入流转换成字符串
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	    
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
				throw new Exception("车险单证对外服务接口返回数据失败");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
			// 释放资源
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            System.out.println(buffer);
	            responseXml=buffer.toString();
	            
	        } catch (ConnectException ce) {
			throw new Exception("与车险单证对外服务接口连接失败，请稍候再试",ce);
	        } catch (Exception e) {
	        	e.printStackTrace();
			throw new Exception("读车险单证对外服务接口返回数据失败",e);
	        	
	        } finally {
			logger.warn("接口({})调用耗时{}ms",urlStr,System.currentTimeMillis()-t1);
			}    
	        return responseXml;
	}
	
	/**
	 * 保存
	 */
	private void saveIlogRule(LIlogRuleResVo resVo,String registNo,SysUserVo userVo,BigDecimal taskId,String codeNode)throws Exception{
		IlogDataProcessingVo ilogDataVo=new IlogDataProcessingVo();
		ilogDataVo.setBusinessNo(registNo);// 报案号
		ilogDataVo.setCompensateNo("");// 计算书号
		ilogDataVo.setComCode(userVo.getComCode());// 业务归属机构
		ilogDataVo.setRiskCode("");// 险种
		ilogDataVo.setOperateType("1");// 操作类型
		ilogDataVo.setRuleType("0");// 任务类型
		ilogDataVo.setRuleNode(FlowNode.Certi.toString());// 任务节点
		if(FlowNode.Certi.toString().equals(codeNode)){
			ilogDataVo.setRuleNode(FlowNode.Compe.toString());// 在单证环节触发的是判断是否自动理算的规则
		}
		ilogDataVo.setLossParty("");// 损失方
		ilogDataVo.setLicenseNo("");// 损失方车牌号
		ilogDataVo.setTriggerNode(codeNode);// 触发节点
		ilogDataVo.setTaskId(taskId);// 触发节点对应的工作流
		ilogDataVo.setOperatorCode(userVo.getUserCode());// 操作人员
		ruleReturnDataSaveService.dealILogResReturnData(resVo,ilogDataVo);
		
	}
	@Override
	public WfTaskSubmitVo autoCertify(String registNo,SysUserVo userVo) {
		
		List<PrpLCMainVo> cmainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);// 保单信息
		List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findPrpWfTaskVo(registNo,FlowNode.Certi.name());
		List<PrpLClaimVo> claimVos=claimTaskService.findClaimListByRegistNo(registNo);
		List<PrpLDlossCarMainVo> prpldlossCarMainVoList=lossCarService.findLossCarMainByRegistNo(registNo);
		PrpLDlossCarMainVo carMainVo=null;
		WfTaskSubmitVo submitVo=new  WfTaskSubmitVo();
		if(prpldlossCarMainVoList!=null && prpldlossCarMainVoList.size()>0){
			for(PrpLDlossCarMainVo vo:prpldlossCarMainVoList){
				if("1".equals(vo.getDeflossCarType())){
					carMainVo=vo;
					break;
				}
			}
		}
		PrpLWfTaskVo wfTaskVo=new PrpLWfTaskVo();
		if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
			 wfTaskVo=prpLWfTaskVos.get(0);
		}
	if(StringUtils.isNotBlank(wfTaskVo.getFlowId())){
			PrpLCertifyMainVo prpLCertifyMainVo = new PrpLCertifyMainVo();
			Date nowDate = new Date();
			if(claimVos!=null && claimVos.size()==1){
				for(PrpLClaimVo vo:claimVos){
					prpLCertifyMainVo.setPolicyNo(vo.getPolicyNo());
				}
			}
			if(claimVos!=null && claimVos.size()==2){
				for(PrpLClaimVo vo:claimVos){
					if(!"1101".equals(vo.getRiskCode())){
						prpLCertifyMainVo.setPolicyNo(vo.getPolicyNo());
					}else{
						prpLCertifyMainVo.setPolicyNoLink(vo.getPolicyNo());
					}
					
				}
			}
			
			if(carMainVo!=null){
				prpLCertifyMainVo.setIsFraud("0");// 是否欺诈
				prpLCertifyMainVo.setSurveyFlag("0");// 是否调查
				prpLCertifyMainVo.setOperatorDate(new Date());// 操作时间
				prpLCertifyMainVo.setOperatorCode(userVo.getUserCode());// 操作人
				prpLCertifyMainVo.setCollectFlag(carMainVo.getDirectFlag());// 单证是否收集齐全
				prpLCertifyMainVo.setIsJQFraud(carMainVo.getIsCInotpayFlag());// 交强是否拒赔
				prpLCertifyMainVo.setIsSYFraud(carMainVo.getIsBInotpayFlag());// 商业是否拒赔
				prpLCertifyMainVo.setNewNotpaycause(carMainVo.getNotpayCause());// 拒赔原因
				prpLCertifyMainVo.setOthernotPaycause(carMainVo.getOtherNotpayCause());// 其它拒赔原因
				prpLCertifyMainVo.setIsSYFraud(carMainVo.getIsBInotpayFlag());
				prpLCertifyMainVo.setIsJQFraud(carMainVo.getIsCInotpayFlag());
			}
			
			// 人伤诉讼信息
			List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
			if(prpLDlossPersTraceMainVoList != null){
				for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
					String caseProcessType = prpLDlossPersTraceMainVo.getCaseProcessType();// 04标示诉讼
					if("04".equals(caseProcessType)){
						prpLCertifyMainVo.setLawsuitFlag("1");
						break;
					}else{
						prpLCertifyMainVo.setLawsuitFlag("0");// 默认为否
					}
				}
			}else{
				prpLCertifyMainVo.setLawsuitFlag("0");// 默认为否
			}
			prpLCertifyMainVo.setRegistNo(registNo);
			prpLCertifyMainVo.setStartTime(nowDate);
			prpLCertifyMainVo.setValidFlag("1");// 有效
			prpLCertifyMainVo.setCreateUser(userVo.getUserCode());
			prpLCertifyMainVo.setCreateTime(nowDate);
			prpLCertifyMainVo.setUpdateUser(userVo.getUserCode());
			prpLCertifyMainVo.setUpdateTime(nowDate);
			prpLCertifyMainVo.setClaimEndTime(nowDate);
			prpLCertifyMainVo.setCustomClaimTime(nowDate);
			prpLCertifyMainVo.setAddNotifyTime(nowDate);
			prpLCertifyMainVo.setAutoCertifyFlag("1");
			PrpLCertifyMainVo certifyMainVo=certifyService.findPrpLCertifyMainVo(registNo);
			if(certifyMainVo!=null){
				Beans.copy().excludeNull().from(prpLCertifyMainVo).to(certifyMainVo);
				certifyService.updatePrpLCertifyMainVo(certifyMainVo);
				prpLCertifyMainVo=certifyMainVo;
			}else{
				prpLCertifyMainVo = certifyService.submitCertify(prpLCertifyMainVo);
			}
	       
			
			
			if(cmainVoList!=null && cmainVoList.size()>0){
				for(PrpLCMainVo vo:cmainVoList){
					submitVo.setComCode(vo.getComCode());
					submitVo.setAssignCom(vo.getComCode());
					break;
					
				}
			}
			
			// 自动单证更新接收人员、处理人员
			wfTaskVo.setHandlerUser("AUTO");
            wfTaskVo.setAssignUser("AUTO");
            wfTaskHandleService.updateTaskIn(wfTaskVo);
			
			submitVo.setCurrentNode(FlowNode.Certi);
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setSubmitType(SubmitType.N);
			submitVo.setTaskInKey(registNo);
			submitVo.setTaskInUser("AUTO");
			wfTaskHandleService.submitCertify(prpLCertifyMainVo,submitVo);
			checkTaskService.saveCheckDutyHis(registNo,"单证提交");
			checkTaskService.updatePrpLCheckDuty(registNo);
		}
		
			return submitVo;
	}

	@Override
	public boolean validAllVLossPass(String registNo) {
		 Boolean flag=false;
		// 所有定核损任务是否完成
			List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
			List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
			List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
		// 校验定损任务
			boolean vLoss = true;
			if(prpLdlossPropMainVoList!=null && prpLdlossPropMainVoList.size()>0){
				for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
					if(!"1".equals(prpLdlossPropMainVo.getUnderWriteFlag())){
						vLoss = false;
					}
				}
			}

			if(prpLDlossCarMainVoList!=null && prpLDlossCarMainVoList.size()>0){
				for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
					if( !"1".equals(prpLDlossCarMainVo.getUnderWriteFlag())){
						vLoss = false;
					}
				}
			}

		if(vLoss){// 判断未接收的定损任务 排除复检
				List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),
						FlowNode.DLProp.name(),FlowNode.DLCar.name());
				if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
					for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskVoList){
						if( !FlowNode.DLChk.name().equals(prpLWfTaskVo.getSubNodeCode())){
							vLoss = false;
							break;
						}
					}
				}
			}
		// 校验人伤任务
			   boolean pLoss = true;
			   if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size()>0){
				   for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
					   if(!"2".equals(prpLDlossPersTraceMainVo.getUnderwriteFlag())){
						   pLoss = false;
					   }
				   } 
			   }
		if(pLoss){// 判断是否有未接收的人伤任务
				   List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.PLoss.name());
				   if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
					   pLoss =  false;
				   }
			   }


		if(vLoss&&pLoss){// 所有定核损都已经审核通过，可以请求ILOG消息
				flag=true;
			}
			return flag;
	}
}
