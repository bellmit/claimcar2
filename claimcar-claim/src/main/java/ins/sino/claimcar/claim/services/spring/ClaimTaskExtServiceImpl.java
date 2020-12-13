package ins.sino.claimcar.claim.services.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.exception.BusinessException;
import ins.framework.lang.Datas;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.utils.DataUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.calculator.Calculator;
import ins.sino.claimcar.claim.calculator.RefreshClaimEstiPaidService;
import ins.sino.claimcar.claim.po.PrpLClaim;
import ins.sino.claimcar.claim.po.PrpLClaimKind;
import ins.sino.claimcar.claim.po.PrpLLawSuit;
import ins.sino.claimcar.claim.po.PrplFraudriskInfo;
import ins.sino.claimcar.claim.po.PrplLaborInfo;
import ins.sino.claimcar.claim.po.PrplOperationInfo;
import ins.sino.claimcar.claim.po.PrplPartsInfo;
import ins.sino.claimcar.claim.po.PrplRiskpointInfo;
import ins.sino.claimcar.claim.po.PrplTestinfoMain;
import ins.sino.claimcar.claim.service.ClaimKindHisService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.service.OtherInterfaceAsyncService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.ClaimFeeCondition;
import ins.sino.claimcar.claim.vo.ClaimFeeExt;
import ins.sino.claimcar.claim.vo.ClaimKindFreshVo;
import ins.sino.claimcar.claim.vo.ClaimVO;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.LossInfo;
import ins.sino.claimcar.claim.vo.LossItem;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLDLimitVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrplTestinfoMainVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropFeeVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.pinganUnion.dto.DutyEstimateDTO;
import ins.sino.claimcar.pinganUnion.dto.EstimateRespData;
import ins.sino.claimcar.pinganUnion.enums.EstimateTypeEnum;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.core.SpringProperties;


@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path("claimTaskExtService")
public class ClaimTaskExtServiceImpl implements ClaimTaskExtService {

	Logger logger = LoggerFactory.getLogger(ClaimTaskExtService.class);
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private BillNoService billNoService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private LossCarService lossCarService;// 车辆定损
	@Autowired
	private PropTaskService propTaskService;// 财产定损
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private RefreshClaimEstiPaidService refreshClaimEstiPaidService;
	@Autowired
	private Calculator calculatorCI;
	@Autowired
	private Calculator calculatorBI;
	@Autowired
	private ClaimKindHisService claimKindHisService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private SubrogationService subrogationService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CaseLeapHNService caseLeapHNService;
	@Autowired
	CaseLeapService caseLeapService;
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	WfFlowQueryService wfFlowQueryService; 

	@Autowired 
	CheckToPlatformService checkToPlatformService;
	@Autowired 
	LossToPlatformService lossToPlatformService;
	@Autowired
	CertifyToPaltformService certifyToPaltformService;

	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	OtherInterfaceAsyncService otherInterfaceAsyncService;
	@Autowired
	private PingAnDictService pingAnDictService;
	
	// 强制立案
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#forceClaim(java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @param flowId
	 * @return
	 */
	@Override
	public PrpLClaimVo forceClaim(String registNo,String policyNo,String flowId) {
		PrpLClaimVo prpLClaimVo = this.organizePrpLClaimVo(registNo,policyNo,flowId);
		List<PrpLClaimKindVo> prpLClaimKindVoList = this.organizeForceClaimKindVo(registNo,policyNo,prpLClaimVo.getClaimNo());
		prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
		// 估损金额
		BigDecimal sumDefLoss = BigDecimal.ZERO;
		BigDecimal sumClaim = BigDecimal.ZERO;
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(prpLClaimKindVo.getDefLoss()));
			sumClaim = sumClaim.add(DataUtils.NullToZero(prpLClaimKindVo.getClaimLoss()));
			//logger.debug("=====强制立案===="+prpLClaimKindVo.getKindCode()+"==="+prpLClaimKindVo.getDefLoss());
		}
		prpLClaimVo.setSumDefLoss(sumDefLoss);
		prpLClaimVo.setClaimFlag("5");
		prpLClaimVo.setSumClaim(sumClaim);
		prpLClaimVo.setSumRescueFee(BigDecimal.ZERO);
		prpLClaimVo.setSumPaid(BigDecimal.ZERO);
		prpLClaimVo.setSumReplevy(BigDecimal.ZERO);
		prpLClaimVo.setSumChargeFee(BigDecimal.ZERO);
		prpLClaimVo.setEstiTimes(1);

		claimService.saveClaim(prpLClaimVo);
		// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
		claimKindHisService.saveClaimKindHis(prpLClaimVo);
		return prpLClaimVo;
	}

	/**
	 * 组织PrpLClaimVo基础数据(强制立案使用)
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月1日 下午2:53:08): <br>
	 */
	private PrpLClaimVo organizePrpLClaimVo(String registNo,String policyNo,String flowId) {
		PrpLClaimVo prpLClaimVo = null;
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);
		if(prpLClaimVo != null){
			logger.error("该保单立案已经存在，不能进行强制立案，报案号："+registNo+"，保单号:"+policyNo);
			throw new BusinessException("改保单立案已经存在，不能进行强制立案，报案号："+registNo+"，保单号:"+policyNo,false);
		}
		prpLClaimVo = new PrpLClaimVo();
		String claimNo = billNoService.getClaimNo(prpLCMainVo.getComCode(),prpLCMainVo.getRiskCode());
		prpLClaimVo.setClaimNo(claimNo);
		prpLClaimVo.setFlowId(flowId);
		prpLClaimVo.setRegistNo(registNo);
		prpLClaimVo.setPolicyNo(policyNo);
		prpLClaimVo.setComCode(prpLCMainVo.getComCode());
		prpLClaimVo.setRiskCode(prpLCMainVo.getRiskCode());
		prpLClaimVo.setClassCode(prpLCMainVo.getClassCode());
		prpLClaimVo.setReportTime(prpLRegistVo.getReportTime());
		prpLClaimVo.setClaimTime(new Date());
		prpLClaimVo.setDamageTime(prpLRegistVo.getDamageTime());
		prpLClaimVo.setDamageCode(prpLRegistVo.getDamageCode());//出险原因
		prpLClaimVo.setDamageTypeCode(prpLRegistVo.getPrpLRegistExt().getAccidentTypes());//事故类型
		prpLClaimVo.setCiIndemDuty("1");//强制立案默认有责
		prpLClaimVo.setIndemnityDuty("0");//商业全责
		prpLClaimVo.setIndemnityDutyRate(BigDecimal.valueOf(100.00));
		prpLClaimVo.setDeductibleRate(BigDecimal.ZERO);
		prpLClaimVo.setDamageAreaCode(prpLRegistVo.getDamageAreaCode());
		prpLClaimVo.setClaimType(CodeConstants.ClaimType.NORMAL);
		prpLClaimVo.setIsTotalLoss("0");//是否全损
		prpLClaimVo.setIsSubRogation("0");
		prpLClaimVo.setMercyFlag(prpLRegistVo.getMercyFlag());
		prpLClaimVo.setIsAlarm(prpLRegistVo.getPrpLRegistExt().getIsAlarm());//是否报警
		prpLClaimVo.setTpFlag(prpLRegistVo.getTpFlag());//通赔标识
		prpLClaimVo.setValidFlag("1");
		prpLClaimVo.setCreateUser(CodeConstants.AutoPass.FORCECLAIM);
		prpLClaimVo.setCreateTime(new Date());
		prpLClaimVo.setUpdateUser(CodeConstants.AutoPass.FORCECLAIM);
		prpLClaimVo.setUpdateTime(new Date());
		prpLClaimVo.setEstiTimes(1);
		return prpLClaimVo;
	}
	
	/**
	 * 组织PrpLClaimKindVo数据
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @param businessNo 任务主表号
	 * @param nodeCode 节点
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月1日 下午2:53:53): <br>
	 */
	private List<PrpLClaimKindVo> organizeForceClaimKindVo(String registNo,String policyNo,String claimNo) {
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(prpLRegistVo.getDamageTime());
		Integer demageYear = Integer.parseInt(dateString.substring(0,4));
		String comCode = prpLCMainVo.getComCode();
		// 强制立案
		List<PrpLCItemKindVo> prpLCItemKindVoList = policyViewService.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(prpLCItemKindVoList==null||prpLCItemKindVoList.isEmpty()){
			logger.error("案件无对应的险别信息，报案号为："+registNo);
			throw new BusinessException("案件无对应的险别信息，报案号为："+registNo,false);
		}
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
			if("1101".equals(prpLCMainVo.getRiskCode())){
				if(prpLCItemKindVo.getRiskCode().startsWith("11")){
					List<PrpLDLimitVo> prpLDLimitVoList = configService.findPrpLDLimitList(CodeConstants.DutyType.CIINDEMDUTY_Y,registNo);// 交强险责任限额表，默认有责
					for(PrpLDLimitVo prpLDLimitVo:prpLDLimitVoList){
						PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
						prpLClaimKindVo.setRegistNo(prpLRegistVo.getRegistNo());
						prpLClaimKindVo.setClaimNo(claimNo);
						prpLClaimKindVo.setRiskCode(prpLCItemKindVo.getRiskCode());
						prpLClaimKindVo.setNodeName("报案");
						prpLClaimKindVo.setKindCode(prpLCItemKindVo.getKindCode());
						prpLClaimKindVo.setKindName(prpLCItemKindVo.getKindName());
						prpLClaimKindVo.setFeeType(CodeConstants.FeeType.PAY);
						prpLClaimKindVo.setCurrency("CNY");
						prpLClaimKindVo.setExchRate(BigDecimal.ONE);
						prpLClaimKindVo.setPaidLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setDeductible(BigDecimal.ZERO);
						prpLClaimKindVo.setDeductibleRate(BigDecimal.ZERO);
						prpLClaimKindVo.setAdjustTime(new Date());
						prpLClaimKindVo.setAdjustReason("强制立案");
						prpLClaimKindVo.setCompStatus("0");
						prpLClaimKindVo.setCancelFlag("0");
						prpLClaimKindVo.setValidFlag("1");
						prpLClaimKindVo.setCreateTime(new Date());
						prpLClaimKindVo.setCreateUser(CodeConstants.AutoPass.FORCECLAIM);
						prpLClaimKindVo.setUpdateTime(new Date());
						prpLClaimKindVo.setUpdateUser(CodeConstants.AutoPass.FORCECLAIM);
						if(CodeConstants.FeeTypeCode.PROPLOSS.equals(prpLDLimitVo.getLossItemNo().trim())){
							prpLClaimKindVo.setLossItemNo(prpLDLimitVo.getLossItemNo());
							prpLClaimKindVo.setLossItemName(prpLDLimitVo.getLossItemName());
							prpLClaimKindVo.setAmount(prpLDLimitVo.getLimitFee());
							boolean flag = false;
							//判断是否存在车财损失
							if(prpLRegistVo.getPrpLRegistCarLosses()!=null&&!prpLRegistVo.getPrpLRegistCarLosses().isEmpty()){
								for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
									if( !"1".equals(prpLRegistCarLossVo.getLossparty())){
										flag = true;
										break;
									}
								}
								if( !flag){
									if(prpLRegistVo.getPrpLRegistPropLosses()!=null&& !prpLRegistVo.getPrpLRegistPropLosses().isEmpty()){
										for(PrpLRegistPropLossVo prpLRegistPropLossVo:prpLRegistVo.getPrpLRegistPropLosses()){
											if( !"1".equals(prpLRegistPropLossVo.getLossparty())){
												flag = true;
												break;
											}
										}
									}
								}
							}
							if(flag){
								List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_BZ,
										prpLDLimitVo.getLossItemNo(),demageYear,comCode);
								PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
								//控制案均估计赔款不能超过责任限额
								prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLDLimitVo.getLimitFee()));
								prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
							}else{
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							}
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVoList.add(prpLClaimKindVo);
						}else{
							prpLClaimKindVo.setLossItemNo(prpLDLimitVo.getLossItemNo());
							prpLClaimKindVo.setLossItemName(prpLDLimitVo.getLossItemName());
							prpLClaimKindVo.setAmount(prpLDLimitVo.getLimitFee());
							if(prpLRegistVo.getPrpLRegistPersonLosses()==null||prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							}else{
								boolean persFlag = false;
								for(PrpLRegistPersonLossVo prpLRegistPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
									if("3".equals(prpLRegistPersonLossVo.getLossparty().trim())
											&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
										persFlag = true;
										// 此处取"案均值"
										logger.debug("==============>"+prpLCItemKindVo.getRiskCode()+"=="+prpLDLimitVo.getLossItemNo()+"==="+demageYear+"=="+comCode);
										String lossItemNo = prpLDLimitVo.getLossItemNo();
										//原来案均数据缺少医疗费案均，取了人伤案均，现已修改
										/*if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(prpLDLimitVo.getLossItemNo())){
											lossItemNo = CodeConstants.FeeTypeCode.PERSONLOSS;
										}else{
											lossItemNo = prpLDLimitVo.getLossItemNo();
										}*/
										List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_BZ,
												lossItemNo,demageYear,comCode);
										PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
										// 控制案均不超过责任限额
										prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLDLimitVo.getLimitFee()));
										prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
										break;
									}
								}
								if(!persFlag){
									prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
									prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
								}
								
							}
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVoList.add(prpLClaimKindVo);
						}
					}
				}
				}else{ 
					if(prpLCItemKindVo.getRiskCode().startsWith("12")){
					PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
					prpLClaimKindVo.setRegistNo(prpLRegistVo.getRegistNo());
					// prpLClaimKindVo.setClaimNo(prpLClaimVo.getClaimNo());
					prpLClaimKindVo.setRiskCode(prpLCItemKindVo.getRiskCode());
					prpLClaimKindVo.setNodeName("报案");
					prpLClaimKindVo.setLossItemName("——");
					prpLClaimKindVo.setKindCode(prpLCItemKindVo.getKindCode());
					prpLClaimKindVo.setKindName(prpLCItemKindVo.getKindName());
					prpLClaimKindVo.setFeeType(CodeConstants.FeeType.PAY);
					prpLClaimKindVo.setCurrency("CNY");
					prpLClaimKindVo.setExchRate(BigDecimal.ONE);
					prpLClaimKindVo.setPaidLoss(BigDecimal.ZERO);
					prpLClaimKindVo.setDeductible(BigDecimal.ZERO);
					prpLClaimKindVo.setDeductibleRate(BigDecimal.ZERO);
					prpLClaimKindVo.setAdjustTime(new Date());
					prpLClaimKindVo.setAdjustReason("强制立案");
					prpLClaimKindVo.setCompStatus("0");
					prpLClaimKindVo.setCancelFlag("0");
					prpLClaimKindVo.setValidFlag("1");
					prpLClaimKindVo.setCreateTime(new Date());
					prpLClaimKindVo.setCreateUser(CodeConstants.AutoPass.FORCECLAIM);
					prpLClaimKindVo.setUpdateTime(new Date());
					prpLClaimKindVo.setUpdateUser(CodeConstants.AutoPass.FORCECLAIM);
					if(CodeConstants.KINDCODE_A_LIST.contains(prpLCItemKindVo.getKindCode().trim())){
						// 当存在标的车车损，且出险原因不是“盗抢”、“车身划痕”、“玻璃单独破碎”、“自燃”时，赋值A险
						if(prpLRegistVo.getPrpLRegistCarLosses()!=null&& !prpLRegistVo.getPrpLRegistCarLosses().isEmpty()
								&&!"DM02".equals(prpLRegistVo.getDamageCode().trim())&&!"DM03".equals(prpLRegistVo.getDamageCode().trim())
								&&!"DM04".equals(prpLRegistVo.getDamageCode().trim())&&!"DM05".equals(prpLRegistVo.getDamageCode().trim())){
							// 车损险
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							// 此处案均值
							List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),prpLCItemKindVo.getKindCode(),
									null,demageYear,comCode);
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
							prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
							
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLCItemKindVo.getKindCode().trim())){
						// 商三险
						if((prpLRegistVo.getPrpLRegistCarLosses()!=null&& !prpLRegistVo.getPrpLRegistCarLosses().isEmpty())
								||(prpLRegistVo.getPrpLRegistPropLosses()!=null&&!prpLRegistVo.getPrpLRegistPropLosses().isEmpty())){
							int carLossCount = 0;
							for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
								if("3".equals(prpLRegistCarLossVo.getLossparty().trim())){
									carLossCount++ ;
									prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
									//prpLClaimKindVo.setLossItemNo("01");
									prpLClaimKindVo.setLossItemName("车物");
									logger.debug("kindB案均值条件c："+prpLCItemKindVo.getRiskCode()+demageYear+comCode);
									// 此处取"案均值"
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_B,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = null;
									for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
										if("01".equals(avgVo.getAvgType())){
											prpDClaimAvgVo = avgVo;
										}
									}
									prpLClaimKindVo.setDefLoss(prpDClaimAvgVo.getAvgAmount());
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount());
									break;
								}
							}
	
							if(carLossCount==0){
								if(prpLRegistVo.getPrpLRegistPropLosses()!=null&&!prpLRegistVo.getPrpLRegistPropLosses().isEmpty()){
									for(PrpLRegistPropLossVo prpLRegistPropLossVo:prpLRegistVo.getPrpLRegistPropLosses()){
										if(!"1".equals(prpLRegistPropLossVo.getLossparty().trim())){
											carLossCount++ ;
											prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
											//prpLClaimKindVo.setLossItemNo("01");
											prpLClaimKindVo.setLossItemName("车物");
											logger.debug("kindB案均值条件c："+prpLCItemKindVo.getRiskCode()+demageYear+comCode);
											// 此处取"案均值"
											List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_B,
													null,demageYear,comCode);
											PrpDClaimAvgVo prpDClaimAvgVo = null;
											for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
												if("01".equals(avgVo.getAvgType())){
													prpDClaimAvgVo = avgVo;
												}
											}
											prpLClaimKindVo.setDefLoss(prpDClaimAvgVo.getAvgAmount());
											prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
											prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
											// 强制立案时估计赔款和估损金额一致
											prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount());
											break;
										}
									}
								}
								
								if(carLossCount==0){
									//prpLClaimKindVo.setLossItemNo("01");
									prpLClaimKindVo.setLossItemName("车物");
									prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
									prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
									
								}
							}
						}else{
							//prpLClaimKindVo.setLossItemNo("01");
							prpLClaimKindVo.setLossItemName("车物");
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						PrpLClaimKindVo claimVo = new PrpLClaimKindVo();
						Beans.copy().from(prpLClaimKindVo).to(claimVo);
						if(prpLRegistVo.getPrpLRegistPersonLosses()!=null&& !prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
							int personLossCount = 0;
							for(PrpLRegistPersonLossVo prpLRegistPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
								if("3".equals(prpLRegistPersonLossVo.getLossparty().trim())
										&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
									personLossCount++ ;
									claimVo.setAmount(prpLCItemKindVo.getAmount());
									//claimVo.setLossItemNo("02");
									claimVo.setLossItemName("人");
									logger.debug("kindB案均值条件p："+prpLCItemKindVo.getRiskCode()+demageYear+comCode);
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_B,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = null;
									for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
										if("02".equals(avgVo.getAvgType())){
											prpDClaimAvgVo = avgVo;
										}
									}
									claimVo.setDefLoss(prpDClaimAvgVo.getAvgAmount());
									claimVo.setRescueFee(BigDecimal.ZERO);
									claimVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									claimVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount());
									break;
								}
							}
	
							if(personLossCount==0){
								//claimVo.setLossItemNo("02");
								claimVo.setLossItemName("人");
								claimVo.setAmount(prpLCItemKindVo.getAmount());
								claimVo.setDefLoss(BigDecimal.ZERO);
								claimVo.setRescueFee(BigDecimal.ZERO);
								claimVo.setRestFee(BigDecimal.ZERO);
								claimVo.setClaimLoss(BigDecimal.ZERO);
							}
						}else{
							//claimVo.setLossItemNo("02");
							claimVo.setLossItemName("人");
							claimVo.setAmount(prpLCItemKindVo.getAmount());
							claimVo.setDefLoss(BigDecimal.ZERO);
							claimVo.setRescueFee(BigDecimal.ZERO);
							claimVo.setRestFee(BigDecimal.ZERO);
							claimVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(claimVo);
						// 强制立案的时候需要控制如果车物+人的赔款超过保额，按照赔款占比分摊保额
						Map<String, BigDecimal> lossKindBMap = this.getClaimLossOfKindB(registNo,claimVo,prpLClaimKindVo);
						for(PrpLClaimKindVo claimKind:prpLClaimKindVoList){
							if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKind.getKindCode())){
								claimKind.setClaimLoss(lossKindBMap.get(claimKind.getLossItemName()));
							}
						}
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLCItemKindVo.getKindCode().trim())){
						if(prpLRegistVo.getPrpLRegistPersonLosses()!=null&& !prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
							int personLossCount = 0;
							for(PrpLRegistPersonLossVo prpLRegistPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
								if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())&&"1".equals(prpLRegistPersonLossVo.getDriverflag())){
									personLossCount++ ;
									prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
									// 车上人员责任险(司机)
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_D11,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
									prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
									break;
								}
							}
							if(personLossCount==0){
								prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							}
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLCItemKindVo.getKindCode().trim())){
						// 车上人员责任险(乘客)
						if(prpLRegistVo.getPrpLRegistPersonLosses()!=null&& !prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
							int personLossCount = 0;
							for(PrpLRegistPersonLossVo prpLRegistPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
								if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())){
									boolean D12Flag = false;
									if("1".equals(prpLRegistPersonLossVo.getDriverflag())
											&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()-1>0){
										D12Flag = true;
									}
									if(!"1".equals(prpLRegistPersonLossVo.getDriverflag())
											&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
										D12Flag = true;
									}
									if(!D12Flag){
										continue;
									}
									personLossCount++ ;
									prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
									// 此处取计算过后的值
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_D12,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
									prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
	
								}
							}
							if(personLossCount==0){
								prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							}
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_D2.equals(prpLCItemKindVo.getKindCode().trim())){
						// 车上货物责任险
						if(prpLRegistVo.getPrpLRegistPropLosses()!=null&& !prpLRegistVo.getPrpLRegistPropLosses().isEmpty()){
							int popLossCount = 0;
							for(PrpLRegistPropLossVo prpLRegistPropLossVo:prpLRegistVo.getPrpLRegistPropLosses()){
								if("1".equals(prpLRegistPropLossVo.getLossparty().trim())){
									popLossCount++ ;
									prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
									// 此处取计算过后的值
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_D2,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
									prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
									break;
								}
							}
							if(popLossCount==0){
								prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							}
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_F.equals(prpLCItemKindVo.getKindCode().trim())){
						// 玻璃单独破碎险
						if("DM02".equals(prpLRegistVo.getDamageCode().trim())){
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							// 此处取计算过后的值
							logger.debug("==============>"+prpLCItemKindVo.getRiskCode()+"==="+demageYear+"=="+comCode);
							List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_F,
									null,demageYear,comCode);
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
							prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_G.equals(prpLCItemKindVo.getKindCode().trim())){
						// 全车盗抢保险
						if("DM04".equals(prpLRegistVo.getDamageCode().trim())){
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							// 此处取计算过后的值
							List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_G,
									null,demageYear,comCode);
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
							prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_L.equals(prpLCItemKindVo.getKindCode().trim())){
						// 车身划痕损失险
						if("DM03".equals(prpLRegistVo.getDamageCode().trim())){
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							// 此处取计算过后的值
							List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_L,
									null,demageYear,comCode);
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
							prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else if(CodeConstants.KINDCODE.KINDCODE_Z.equals(prpLCItemKindVo.getKindCode().trim())){
						// 自燃损失险
						if("DM05".equals(prpLRegistVo.getDamageCode().trim())){
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							// 此处取计算过后的值
							List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_Z,
									null,demageYear,comCode);
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							// 强制立案时估计赔款和估损金额一致
							prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
							prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						}
						prpLClaimKindVoList.add(prpLClaimKindVo);
						continue;
					}else{
						prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
						// 此处取计算过后的值
						List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLCItemKindVo.getRiskCode(),prpLCItemKindVo.getKindCode(),
								null,demageYear,comCode);
						if(prpDClaimAvgVoList!=null&&prpDClaimAvgVoList.size()>0){
							PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
							if(prpDClaimAvgVo == null || prpDClaimAvgVo.getAvgAmount() == null){
								prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
								prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
								prpLClaimKindVoList.add(prpLClaimKindVo);
								continue;
							}else{
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								// 强制立案时估计赔款和估损金额一致
								prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLCItemKindVo.getAmount()));
								prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
								prpLClaimKindVoList.add(prpLClaimKindVo);
								continue;
							}
						}else{
							prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
							prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
							prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
							prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
							prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
							prpLClaimKindVoList.add(prpLClaimKindVo);
							continue;
						}
					}
				}
			}
		}
		return prpLClaimKindVoList;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#submitClaim(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @param flowId
	 * @param claimFlag
	 */
	@Override
	public PrpLClaimVo submitClaim(String registNo,String policyNo,String flowId,String claimFlag) throws ParseException{
		PrpLClaimVo prpLClaimVo = null;
		if(CodeConstants.ClaimFlag.AUTOCLAIM.equals(claimFlag)){
			ClaimVO claimVO = this.autoClaim(registNo,policyNo);
			prpLClaimVo = claimVO.getPrpLClaimVo();
		}else if(CodeConstants.ClaimFlag.CLAIMFORCE.equals(claimFlag)){
			prpLClaimVo = this.forceClaim(registNo,policyNo,flowId);
		}
		//立案提交工作流
		wfTaskHandleService.submitClaim(flowId,prpLClaimVo);
		return prpLClaimVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#autoClaim(java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @return
	 */
	@Override
	public ClaimVO autoClaim(String registNo,String policyNo) throws ParseException {
		logger.info("报案号 registNo={},保单号={},进入自动立案保存数据方法",registNo,policyNo);
		PrpLCMainVo prpLCMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,policyNo);
		if(prpLCMainVo==null){
			logger.error("无法获取保单信息，报案号为"+registNo);
			throw new BusinessException("无法获取保单信息，报案号为"+registNo,false);
		}
		ClaimVO claimVO = new ClaimVO();
		claimVO.setPrpLCMainVo(prpLCMainVo);
		claimVO = loadClaimRelate(claimVO,registNo,policyNo);

		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);// 获取立案数据
		PrpLCheckVo prpLCheckVo = claimVO.getPrpLCheckVo();
		PrpLRegistVo prpLRegistVo = claimVO.getPrpLRegistVo();

		claimVO.setClaimFeeCond(loadSumClaimCondition(prpLRegistVo,prpLCMainVo,prpLCheckVo,prpLClaimVo));// 初始化立案估损的中间结果
		if(prpLClaimVo==null){// 立案不存在
			prpLClaimVo = this.loadClaim(prpLRegistVo,prpLCMainVo,prpLCheckVo,claimVO.getClaimFeeCond());
		}
		// 初始化立案分险别、损失类别估损金额
		List<ClaimFeeExt> claimFeeExts = new ArrayList<ClaimFeeExt>(0);
		// 新旧立案Service调整 add by chenrong 2014-09-16 begin
		if(prpLCheckVo!=null){
			claimFeeExts = this.getKindLossByCondAndPolicyForNewClaimRefresh(prpLCMainVo,claimVO.getClaimFeeCond());
		}else{
			claimFeeExts = this.getKindLossByCondAndPolicy(prpLCMainVo,claimVO.getClaimFeeCond());
		}
		
		List<PrpLClaimKindVo> prpLClaimKindVoList = loadClaimFee(claimFeeExts,"",registNo,FlowNode.Check);
		
		if(prpLClaimVo.getPrpLClaimKinds() == null || prpLClaimVo.getPrpLClaimKinds().isEmpty()){
			for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
				prpLClaimKindVo.setClaimNo(prpLClaimVo.getClaimNo());
				prpLClaimKindVo.setRegistNo(prpLClaimVo.getRegistNo());
				prpLClaimKindVo.setAdjustReason(FlowNode.Check.getName());
			}
			prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
		}else{
			if(prpLClaimKindVoList != null && !prpLClaimKindVoList.isEmpty()){
				for(PrpLClaimKindVo prpLClaimKindVo :prpLClaimKindVoList){
					prpLClaimKindVo.setAdjustReason(FlowNode.Check.getName());
					if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
						for(PrpLClaimKindVo prpLClaimKind : prpLClaimVo.getPrpLClaimKinds()){
							if(!"01".equals(prpLClaimKindVo.getLossItemNo())){
								// 环节为查勘，不刷新死亡和医疗
								prpLClaimKind.setAdjustReason(FlowNode.Check.getName());
								continue;
							}
							if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKind.getKindCode().trim()) && 
									prpLClaimKindVo.getLossItemNo().trim().equals(prpLClaimKind.getLossItemNo().trim())){
								Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
							}
						}
					}else{
						for(PrpLClaimKindVo prpLClaimKind : prpLClaimVo.getPrpLClaimKinds()){
							// 环节为查勘，不刷新人伤相关的险别的数据（查勘不组织人伤数据，刷新会将强制立案的人伤金额覆盖成0）
							if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKind.getKindCode())
									||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKind.getKindCode())){
								prpLClaimKind.setAdjustReason(FlowNode.Check.getName());
								continue;
							}
							/*if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKind.getKindCode())
									&&"人".equals(prpLClaimKind.getLossItemName())){
								prpLClaimKind.setAdjustReason(FlowNode.Check.getName());
								continue;
							}*/
							if((prpLClaimKind.getKindCode().trim()).equals(prpLClaimKindVo.getKindCode().trim())){
								if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKind.getKindCode().trim())){
									if((prpLClaimKind.getLossItemName().trim()).equals(prpLClaimKindVo.getLossItemName().trim())){
										Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
									}
								}else{
									Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
								}
							}
						}
					}
				}
			}			
		}
		claimVO.setClaimFeeExts(claimFeeExts);

		BigDecimal sumClaim = BigDecimal.ZERO;
		BigDecimal sumDefLoss = BigDecimal.ZERO;
		BigDecimal sumRescueFee = BigDecimal.ZERO;
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			sumClaim = sumClaim.add(prpLClaimKindVo.getClaimLoss());
			sumDefLoss = sumDefLoss.add(prpLClaimKindVo.getDefLoss());
			sumRescueFee = sumRescueFee.add(prpLClaimKindVo.getRescueFee());
		}
		
		prpLClaimVo.setSumClaim(sumClaim);
		prpLClaimVo.setSumDefLoss(sumDefLoss);
		prpLClaimVo.setSumRescueFee(sumRescueFee);
		if(prpLClaimVo.getEstiTimes() == null){
			prpLClaimVo.setEstiTimes(1);
		}/*else{
			prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);// 估损次数增加
		}*/
		
		claimVO.setPrpLClaimVo(prpLClaimVo);
		if(claimVO.getPrpLClaimVo().getClaimNo()==null){
			claimVO.getPrpLClaimVo().setCreateUser(CodeConstants.AutoPass.CLAIM);
			claimVO.getPrpLClaimVo().setCreateTime(new Date());
			claimVO.getPrpLClaimVo().setClaimTime(new Date());// 增加立案时间到秒
		}
		claimVO.getPrpLClaimVo().setUpdateUser(CodeConstants.AutoPass.CLAIM);
		claimVO.getPrpLClaimVo().setUpdateTime(new Date());
		updateClaimFeeByClaim(prpLClaimVo,prpLClaimVo.getPrpLClaimKinds());// 主子表关联
		claimVO.setPrpLClaimVo(addOrUpdateClaim(claimVO.getPrpLClaimVo(),null,null));

		try{
			//立案送河南消保
			SysUserVo userVo = new SysUserVo();
			userVo.setUserCode(CodeConstants.AutoPass.CLAIM);
//			caseLeapHNService.claimToHN(claimVO.getPrpLClaimVo(), userVo);
			otherInterfaceAsyncService.claimToHN(claimVO.getPrpLClaimVo(), userVo);
			//立案送贵州消保
//			caseLeapService.claimToGZ(claimVO.getPrpLClaimVo(), userVo);
			otherInterfaceAsyncService.claimToGZ(claimVO.getPrpLClaimVo(), userVo);
		}catch (Exception e) {
			logger.error("报案号 registNo="+ registNo + "立案送消保失败",e);
		}
		logger.info("报案号 registNo={},保单号={},结束自动立案保存数据方法",registNo,policyNo);
		return claimVO;
	}

	/**
	 * 初始化与立案相关的信息:报案，抄单，查勘，车辆信息
	 * @param claimVO
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified:
	 */
	private ClaimVO loadClaimRelate(ClaimVO claimVO,String registNo,String policyNo) {
		logger.debug("初始化与立案相关的信息:报案，抄单，查勘，车辆信息:registNo"+registNo+",policyNo"+policyNo);
		if(claimVO==null){
			claimVO = new ClaimVO();
		}

		if(claimVO.getPrpLCMainVo()==null){
			claimVO.setPrpLCMainVo(policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registNo,policyNo));
		}

		if(claimVO.getPrpLRegistVo()==null){
			claimVO.setPrpLRegistVo(registQueryService.findByRegistNo(registNo));
		}

		if(claimVO.getPrpLCheckVo()==null){
			claimVO.setPrpLCheckVo(checkTaskService.findCheckVoByRegistNo(registNo));
			// claimVO.setPrpLcheckDuty(findDutyByCheck(claimVO.getPrpLcheck(), null));
		}

		if(claimVO.getPrpLClaimVo()==null){
			claimVO.setPrpLClaimVo(claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo));
		}
		if(claimVO.getPrpLCMainVo().getPrpCItemCars() == null || claimVO.getPrpLCMainVo().getPrpCItemCars().size()==0){
			throw new BusinessException("保单"+claimVO.getPrpLCMainVo().getPolicyNo()+"无车辆信息数据!",false);
		}
		// claimVO.setRegistCount(this.findValidClaimByPolicyNo(claimVO.getPrpLCMainVo().getPolicyNo()).size());
		claimVO.setPrpLCItemCarVo(claimVO.getPrpLCMainVo().getPrpCItemCars().get(0));
		return claimVO;
	}

	/**
	 * 分险别初始化立案估损的中间计算结果
 	 */
	private ClaimFeeCondition loadSumClaimCondition(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo,PrpLCheckVo prpLCheckVo,PrpLClaimVo prpLClaimVo) {
		ClaimFeeCondition claimCond = new ClaimFeeCondition();
		if(( prpLCheckVo==null )||( prpLRegistVo==null )||( prpLCMainVo==null )){
			logger.debug("初始化立案计算的中间结果失败!");
			throw new BusinessException("初始化立案计算的中间结果失败!",false);
		}
		claimCond.setRegistNo(prpLCMainVo.getRegistNo());
		claimCond.setPolicyNo(prpLCMainVo.getPolicyNo());
		PrpLCheckDutyVo prpLcheckDutyVo = findDutyByCheck(prpLCheckVo,1);
		if (prpLcheckDutyVo == null) {
			logger.info("初始化立案计算的中间结果失败, 查勘任务表或车辆查勘信息表或查勘车辆责任表为空！报案号：" + prpLCMainVo.getRegistNo());
			throw new BusinessException("初始化立案计算的中间结果失败, 查勘任务或车辆查勘信息或查勘车辆定责信息为空！", false);
		}
		claimCond.setLossType(prpLCheckVo.getLossType()); // 查勘保证数据非空
		claimCond.setTotalLossFlag(prpLCheckVo.getLossType());
		claimCond.setClaimType(StringUtils.trimToEmpty(prpLCheckVo.getClaimType())); // 目前的赔付类型，从查勘取
		claimCond.setIndemnityDuty(prpLcheckDutyVo.getIndemnityDuty());
		claimCond.setIndemnityDutyRate(prpLcheckDutyVo.getIndemnityDutyRate());
		claimCond.setClaimSlefFlag(prpLcheckDutyVo.getIsClaimSelf());
		claimCond.setCiDutyFlag(prpLcheckDutyVo.getCiDutyFlag()); // 交强险有无赔偿责任
		if("1".equals(prpLcheckDutyVo.getCiDutyFlag())){
			claimCond.setCiIndemDuty(new BigDecimal(100.00));
		}else{
			claimCond.setCiIndemDuty(BigDecimal.ZERO);
		}
		// 代位求偿信息
		PrpLSubrogationMainVo subrogationVo = subrogationService.find(prpLCMainVo.getRegistNo());
		if(subrogationVo!=null){
			claimCond.setSubRogationFlag(subrogationVo.getSubrogationFlag());
		}else{
			claimCond.setSubRogationFlag(prpLRegistVo.getPrpLRegistExt().getIsSubRogation());
		}
		if(prpLClaimVo==null){
			// 出险标志预判
			if(prpLCMainVo.getRiskCode().startsWith("11")){
				claimCond.setDamageFlag(CodeConstants.DamageFlag.CI_CLAIM);
			}else{
				claimCond.setDamageFlag(CodeConstants.DamageFlag.BI_CLAIM);
			}
		}
		// 获取分损失项的估损、施救费、残值
		claimCond = loadSumLossMap(claimCond, prpLRegistVo, prpLCMainVo, prpLCheckVo, prpLClaimVo);
		String checkMakeCom = "";
		if(prpLCheckVo!=null){
			checkMakeCom = prpLCheckVo.getPrpLCheckTask().getMakeCom();
		}
		claimCond.setMakeCom(checkMakeCom);

		return claimCond;
	}

	// 查勘的定责信息，只返回一条信息
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#findDutyByCheck(ins.sino.claimcar.check.vo.PrpLCheckVo, java.lang.Integer)
	 * @param prpLCheckVo
	 * @param serialNo
	 * @return
	 */
	@Override
	public PrpLCheckDutyVo findDutyByCheck(PrpLCheckVo prpLCheckVo,Integer serialNo) {
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
		PrpLCheckDutyVo prpLCheckDutyVo = null;
		// 防止下面一行操作空指针异常，prpLCheckVo 在调用处已做null判断，这里不需要再进行判断
		if (prpLCheckTaskVo == null) {
			return null;
		}
		List<PrpLCheckCarVo> prpLCheckCarVoList = prpLCheckTaskVo.getPrpLCheckCars();
		if(prpLCheckCarVoList==null||prpLCheckCarVoList.isEmpty()){
			return null;
		}

		for(PrpLCheckCarVo prpLCheckCarVo:prpLCheckCarVoList){
			if(serialNo.equals(prpLCheckCarVo.getSerialNo())){
				prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLCheckCarVo.getRegistNo(),serialNo);
				break;
			}
		}
		return prpLCheckDutyVo;
	}

	/**
	 * 获取分损失类的估损、施救费、残值
	 * @param claimCond
	 * @param prpLRegistVo
	 * @param prpLCMainVo
	 * @param prpLCheckVo
	 * @param prpLClaimVo
	 * @return
	 */
	private ClaimFeeCondition loadSumLossMap(ClaimFeeCondition claimCond, PrpLRegistVo prpLRegistVo, PrpLCMainVo prpLCMainVo, PrpLCheckVo prpLCheckVo,
											 PrpLClaimVo prpLClaimVo) {
		// 从查勘、定损、人伤跟踪获取损失项列表
		List<LossItem> lossItems = loadLossItems(claimCond,prpLRegistVo,prpLCMainVo,prpLCheckVo,prpLClaimVo);
		for(LossItem item:lossItems){
			// 估损金额
			if(claimCond.getSumClaimMap().get(item.getLossItemType())==null){
				claimCond.getSumClaimMap().put(item.getLossItemType(),item.getSumClaim());
			}else{
				claimCond.getSumClaimMap().put(item.getLossItemType(),
						BigDecimal.valueOf(claimCond.getSumClaimMap().get(item.getLossItemType()).doubleValue()+item.getSumClaim().doubleValue()));
			}
			// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
			// 目前想到在LossItem对象中增加一个医疗费，在这个方法中判断是人伤，并且是医疗费的时候提取出来
			if(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS_Medical.equals(item.getLossItemTypeMedical())){
				if(claimCond.getSumClaimMap().get(item.getLossItemTypeMedical())==null){
					claimCond.getSumClaimMap().put(item.getLossItemTypeMedical(),item.getSumClaimForMedical());
				}else{
					claimCond.getSumClaimMap().put(
							item.getLossItemTypeMedical(),
							BigDecimal.valueOf(claimCond.getSumClaimMap().get(item.getLossItemTypeMedical()).doubleValue()+item
									.getSumClaimForMedical().doubleValue()));
				}
			}
			// 施救费
			if(claimCond.getRescueFeeMap().get(item.getLossItemType())==null){
				claimCond.getRescueFeeMap().put(item.getLossItemType(),item.getRescueFee());
			}else{
				claimCond.getRescueFeeMap().put(item.getLossItemType(),
						BigDecimal.valueOf(claimCond.getRescueFeeMap().get(item.getLossItemType()).doubleValue()+item.getRescueFee().doubleValue()));
			}
			// 残值
			// if(claimCond.getRejectFeeMap().get(item.getLossItemType())==null){
			// claimCond.getRejectFeeMap().put(item.getLossItemType(),item.getRejectFee());
			// }else{
			// claimCond.getRejectFeeMap().put(item.getLossItemType(),
			// BigDecimal.valueOf(claimCond.getRejectFeeMap().get(item.getLossItemType()).doubleValue()+item.getRejectFee().doubleValue()));
			// }
		}
		List<PrpLClaimKindVo> prpLClaimKindVoList;
		String claimNo = null;
		if(prpLClaimVo!=null){
			claimNo = prpLClaimVo.getClaimNo();
		}
		// 查找原来已经立案的分险别估损信息
		prpLClaimKindVoList = findOthClaimFeeByRegistNo(prpLCMainVo.getRegistNo(),claimNo);
		// 52 财产损失
		claimCond.setOldSumClaimB1(BigDecimal.valueOf(Datas.round(
				getBzSumClaimByLossFeeType(prpLClaimKindVoList,CodeConstants.FeeTypeCode.PROPLOSS),2)));
		claimCond.setOldRescueFeeB1(BigDecimal.valueOf(Datas.round(
				getBzRescueFeeByLossFeeType(prpLClaimKindVoList,CodeConstants.FeeTypeCode.PROPLOSS),2)));
		// 51 人员伤亡
		claimCond.setOldSumClaimB2(BigDecimal.valueOf(Datas.round(
				getBzSumClaimByLossFeeType(prpLClaimKindVoList,CodeConstants.FeeTypeCode.PERSONLOSS),2)));
		claimCond.setOldSumClaimMedical(BigDecimal.valueOf(Datas.round(
				getBzSumClaimByLossFeeType(prpLClaimKindVoList,CodeConstants.FeeTypeCode.MEDICAL_EXPENSES),2)));
		logger.debug("报案号："+prpLCMainVo.getRegistNo()+"保单号:"+prpLCMainVo.getPolicyNo()+",OldSumClaimB1:"+claimCond.getOldSumClaimB1()+",OldRescueFeeB1:"+claimCond
				.getOldRescueFeeB1()+",OldSumClaimB2"+claimCond.getOldSumClaimB2()+",OldSumClaimMedical:"+claimCond.getOldSumClaimMedical());
		return claimCond;
	}

	/**
	 * 从查勘、定损、人伤跟踪获取损失项列表
	 * @param claimCond
	 * @param prpLRegistVo 报案信息，
	 * @param prpLCMainVo
	 * @param prpLCheckVo
	 * @param prpLClaimVo
	 * @return
	 */
	private List<LossItem> loadLossItems(ClaimFeeCondition claimCond,PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo,PrpLCheckVo prpLCheckVo,
											PrpLClaimVo prpLClaimVo) {
		List<LossItem> lossItems = new ArrayList<LossItem>(0);
		logger.debug("从查勘、定损、人伤跟踪获取损失项列表RegistNo"+prpLRegistVo.getRegistNo());
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLRegistVo.getRegistNo());
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(prpLRegistVo.getRegistNo());
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(prpLRegistVo.getRegistNo());
		// 暂时先取保单的归属机构，这个入参涉及到配置参数的获取，目前有些地方通过登录人员的session取，这里获取不到，暂时先这样吧
		claimCond.setCarLossItems(findCarLossInfo(prpLCheckVo,prpLDlossCarMainVoList,prpLCMainVo));
		claimCond.setPropLossItems(findPropLossInfo(prpLCheckVo,prpLdlossPropMainVoList,prpLCMainVo));
		claimCond.setPersonLossItems(findPersonLossInfo(prpLCheckVo,prpLDlossPersTraceMainVoList,prpLCMainVo));
		lossItems.addAll(claimCond.getCarLossItems());
		lossItems.addAll(claimCond.getPropLossItems());
		lossItems.addAll(claimCond.getPersonLossItems());
		return lossItems;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#loadClaimFee(java.util.List, java.lang.String, java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param claimFeeExts
	 * @param lossType
	 * @param registNo
	 * @param node
	 * @return
	 */
	@Override
	public List<PrpLClaimKindVo> loadClaimFee(List<ClaimFeeExt> claimFeeExts,String lossType,String registNo,FlowNode node) {
		logger.debug("将ClaimFeeExt转为PrpLclaimFee");
		if(claimFeeExts==null||claimFeeExts.size()==0){
			throw new BusinessException("立案页面险别估损信息为空，请确定该保单是否已经退保!",false);
		}
//		List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo);
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>(0);
		for(ClaimFeeExt claimFeeExt:claimFeeExts){
			if(claimFeeExt.getSumClaim()==null){// 估损为0的不能保存
				claimFeeExt.setSumClaim(BigDecimal.valueOf(0));
			}
			if(claimFeeExt.getEstiPaid()==null){
				claimFeeExt.setEstiPaid(BigDecimal.valueOf(0));
			}
			PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
			prpLClaimKindVo.setId(claimFeeExt.getId());
			prpLClaimKindVo.setKindCode(claimFeeExt.getKindCode());
			prpLClaimKindVo.setKindName(CodeConstants.KINDCODE_MAP.get(claimFeeExt.getKindCode()));
			if(claimFeeExt.getFeeTypeCode() != null){
				prpLClaimKindVo.setLossItemNo(claimFeeExt.getFeeTypeCode().toString());
			}
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(claimFeeExt.getKindCode())){
				if(CodeConstants.FeeTypeCode.PROPLOSS.equals(claimFeeExt.getFeeTypeCode())){
					prpLClaimKindVo.setLossItemName("车财损失");
				}else if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(claimFeeExt.getFeeTypeCode())){
					prpLClaimKindVo.setLossItemName("死亡伤残");
				}else if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(claimFeeExt.getFeeTypeCode())){
					prpLClaimKindVo.setLossItemName("医疗费用");
				}
			}else if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimFeeExt.getKindCode())){
				if(claimFeeExt.getFeeTypeCode()!=null&&claimFeeExt.getFeeTypeCode()=="pers"){
					prpLClaimKindVo.setLossItemName("人");
				}else{
					prpLClaimKindVo.setLossItemName("车物");
				}
			}else{
				prpLClaimKindVo.setLossItemName("——");
			}
			// prpLClaimKindVo.setItemKindNo(claimFeeExt.getItemKindNo());
			prpLClaimKindVo.setDeductibleRate(claimFeeExt.getDeductRate());
			prpLClaimKindVo.setDeductible(claimFeeExt.getDeductLoss());
			prpLClaimKindVo.setClaimLoss(claimFeeExt.getEstiPaid());
			prpLClaimKindVo.setDefLoss(claimFeeExt.getSumClaim());
			prpLClaimKindVo.setRescueFee(claimFeeExt.getRescueFee());
			prpLClaimKindVo.setFeeType(CodeConstants.FeeType.PAY);
			prpLClaimKindVo.setRiskCode(claimFeeExt.getRiskCode());
			prpLClaimKindVo.setRestFee(claimFeeExt.getSumRest());
			//TODO Amount=UnitAmount是否有问题
			prpLClaimKindVo.setAmount(claimFeeExt.getUnitAmount());
			// prpLClaimKindVo.setPersonId(Long.valueOf(1));
			// prpLClaimKindVo.setPersonName("");
			prpLClaimKindVo.setCurrency("CNY");
//			prpLClaimKindVo.setFlag("I");
			prpLClaimKindVo.setAdjustReason(node.getName());
			prpLClaimKindVo.setAdjustTime(new Date());
			prpLClaimKindVo.setCreateTime(new Date());
			prpLClaimKindVo.setCreateUser(CodeConstants.AutoPass.CLAIM);
			prpLClaimKindVo.setUpdateTime(null);
			prpLClaimKindVo.setUpdateUser(null);
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimFeeExt.getKindCode())){
				if(lossType!=null&& !lossType.trim().equals("")){
					if(lossType.trim().equals("personloss")){
						if(claimFeeExt.getPersonSumClaim()!=null){
							prpLClaimKindVo.setDefLoss(claimFeeExt.getPersonSumClaim());// 直接取人员损失与赔款
						}else{
							prpLClaimKindVo.setDefLoss(BigDecimal.valueOf(0));// 直接取人员损失与赔款
						}
						if(claimFeeExt.getPersonEstiPaid()!=null){
							prpLClaimKindVo.setClaimLoss(claimFeeExt.getPersonEstiPaid());
						}else{
							prpLClaimKindVo.setClaimLoss(BigDecimal.valueOf(0));
						}
					}else if(lossType.trim().equals("proploss")){
						if(claimFeeExt.getPersonSumClaim()!=null&&claimFeeExt.getSumClaim().subtract(claimFeeExt.getPersonSumClaim())
								.compareTo(BigDecimal.valueOf(0))==1){
							prpLClaimKindVo.setDefLoss(claimFeeExt.getSumClaim().subtract(claimFeeExt.getPersonSumClaim()));// 直接取财产损失与赔款(总的损失与赔款-人伤的损失与赔款得到)
						}
						if(claimFeeExt.getPersonEstiPaid()!=null&&claimFeeExt.getEstiPaid().subtract(claimFeeExt.getPersonEstiPaid())
								.compareTo(BigDecimal.valueOf(0))==1){
							prpLClaimKindVo.setClaimLoss(claimFeeExt.getEstiPaid().subtract(claimFeeExt.getPersonEstiPaid()));// 直接取财产损失与赔款(总的损失与赔款-人伤的损失与赔款得到)
						}
					}
				}
			}
			//当B险有人伤损失和车物损失的时候，拆分B险为两条记录
			
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimFeeExt.getKindCode())
					&&claimFeeExt.getPersonSumClaim().compareTo(BigDecimal.ZERO)==1
					&&"车物".equals(prpLClaimKindVo.getLossItemName())){
				PrpLClaimKindVo prpLClaimKindVoBPers = new PrpLClaimKindVo();//B险人伤Vo
				Beans.copy().from(prpLClaimKindVo).to(prpLClaimKindVoBPers);
				//赋值人伤B险记录的赔款和估损
				prpLClaimKindVoBPers.setLossItemName("人");
				prpLClaimKindVoBPers.setLossItemNo("pers");
				prpLClaimKindVoBPers.setFeeType("pers");
				prpLClaimKindVoBPers.setClaimLoss(claimFeeExt.getPersonEstiPaid());
				prpLClaimKindVoBPers.setDefLoss(claimFeeExt.getPersonSumClaim());
				prpLClaimKindVoBPers.setRescueFee(BigDecimal.ZERO);
				//车物B险记录减掉人伤
				prpLClaimKindVo.setLossItemName("车物");
				prpLClaimKindVo.setFeeType("carp");
				prpLClaimKindVo.setLossItemNo("carp");
				if(claimFeeExt.getSumClaim().compareTo(prpLClaimKindVoBPers.getDefLoss())==1){
					prpLClaimKindVo.setDefLoss(claimFeeExt.getSumClaim().subtract(claimFeeExt.getPersonSumClaim()));
				}else{
					prpLClaimKindVo.setDefLoss(claimFeeExt.getSumClaim());
				}
				prpLClaimKindVo.setClaimLoss(claimFeeExt.getEstiPaid().subtract(claimFeeExt.getPersonEstiPaid()));
				prpLClaimKindVoList.add(prpLClaimKindVoBPers);
			}
			// 如果存在人伤任务且未核损通过，且未注销，刷立案也需要加上人伤的未决（取案均）
			// 刷不刷人伤在方法中会根据报案信息判断
			boolean PLossUnderwriteFlag = true;
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())
					||CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())
					||CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKindVo.getKindCode())
					||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKindVo.getKindCode())){
				// 查询out表 是否存在已处理的人伤核损任务（人伤跟踪审核 人伤费用审核） 且状态不为注销  
				PLossUnderwriteFlag = this.adjustPLossUnderwrite(registNo);
			}
			
			//查勘刷立案时 如果报案有人伤 需要加上人伤的未决 估损和赔款金额直接取人伤的案均 
			if(!PLossUnderwriteFlag){
				/**
				 * 2017-3-6 任何环节刷立案 只要人伤是审核通过的状态 都不再代入人伤案均值
				 */
				//正常查勘刷立案（根据报案人伤信息给人伤未决赋值  B险-新增一条人伤  D11 D12-覆盖金额）
				PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
				if(registVo.getPrpLRegistPersonLosses()!=null&&registVo.getPrpLRegistPersonLosses().size()>0){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(registVo.getDamageTime());
					Integer demageYear = Integer.parseInt(dateString.substring(0,4));
					PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registVo.getRegistNo(),registVo.getPolicyNo());
					String comCode = prpLCMainVo.getComCode();
					for(PrpLRegistPersonLossVo prpLRegistPersonLossVo : registVo.getPrpLRegistPersonLosses()){
						
						if("3".equals(prpLRegistPersonLossVo.getLossparty().trim())
								&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
							if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){

								// 死伤和医疗都赋值案均  有责  无责做超保额管控
								if(!"01".equals(prpLClaimKindVo.getLossItemNo())){
									logger.debug("==============>"+prpLClaimKindVo.getRiskCode()+"=="+prpLClaimKindVo.getLossItemNo()+"==="+demageYear+"=="+comCode);
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLClaimKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_BZ,
											prpLClaimKindVo.getLossItemNo(),demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
									//控制案均估计赔款不能超过责任限额
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLClaimKindVo.getAmount()));
									prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
								}
								
							}else if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())){
								PrpLClaimKindVo prpLClaimKindVoBPers = new PrpLClaimKindVo();
								Beans.copy().from(prpLClaimKindVo).to(prpLClaimKindVoBPers);
								prpLClaimKindVoBPers.setLossItemName("人");
								// 估损和赔款取按均值
								List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLClaimKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_B,
										null,demageYear,comCode);
								PrpDClaimAvgVo prpDClaimAvgVo = null;
								for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
									if("02".equals(avgVo.getAvgType())){
										prpDClaimAvgVo = avgVo;
									}
								}
								prpLClaimKindVoBPers.setDefLoss(prpDClaimAvgVo.getAvgAmount());
								prpLClaimKindVoBPers.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVoBPers.setRestFee(BigDecimal.ZERO);
								prpLClaimKindVoBPers.setClaimLoss(prpDClaimAvgVo.getAvgAmount());
								
								// 和车物B险一起做超保额管控
								Map<String,BigDecimal> lossClaimLossMap = this.getClaimLossOfKindB(registNo,prpLClaimKindVoBPers,prpLClaimKindVo);
								prpLClaimKindVoBPers.setClaimLoss(lossClaimLossMap.get("人"));
								prpLClaimKindVo.setClaimLoss(lossClaimLossMap.get("车物"));
								prpLClaimKindVoList.add(prpLClaimKindVoBPers);
							}
						}else{
							if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())
									&&"1".equals(prpLRegistPersonLossVo.getDriverflag())
									&&CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKindVo.getKindCode())){
								// 车上人员责任险(司机)
								List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLClaimKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_D11,
										null,demageYear,comCode);
								PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
								prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
								prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
								// 强制立案时估计赔款和估损金额一致
								prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLClaimKindVo.getAmount()));
								prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
							}
							if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())
									&&CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKindVo.getKindCode())){
								boolean D12Flag = false;
								if("1".equals(prpLRegistPersonLossVo.getDriverflag())
										&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()-1>0){
									D12Flag = true;
								}
								if(!"1".equals(prpLRegistPersonLossVo.getDriverflag())
										&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
									D12Flag = true;
								}
								if(D12Flag){
									// 车上人员责任险(乘客)
									List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(prpLClaimKindVo.getRiskCode(),CodeConstants.KINDCODE.KINDCODE_D12,
											null,demageYear,comCode);
									PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
									prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
									prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
									// 强制立案时估计赔款和估损金额一致
									prpLClaimKindVo.setClaimLoss(prpDClaimAvgVo.getAvgAmount().min(prpLClaimKindVo.getAmount()));
									prpLClaimKindVo.setDefLoss(prpLClaimKindVo.getClaimLoss());
								}
							}
						}
					}
				}		
			}
			if((CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())
					&&"车物".equals(prpLClaimKindVo.getLossItemName()))
					||(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())
							&&CodeConstants.FeeTypeCode.PROPLOSS.equals(prpLClaimKindVo.getLossItemNo()))){
				// 在车财合并计算的险别中赋值车辆损失比例字段   方便保监提数区分车财
				logger.debug(prpLClaimKindVo.getKindCode()+"车辆损失比例==ClaimUpd=="+claimFeeExt.getCarLossRate());
				prpLClaimKindVo.setCarLossRate(claimFeeExt.getCarLossRate());
				
			}
			prpLClaimKindVoList.add(prpLClaimKindVo);
		}
		return prpLClaimKindVoList;
	}
	/**
	 * 查询工作流人伤节点信息  判断是否有核损通过的人伤任务
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2017年1月10日 下午3:40:47): <br>
	 */
	public boolean adjustPLossUnderwrite(String registNo){
		//select * from prplwftaskout where HANDLERSTATUS = '3' and ( subnodecode like  'PLVerify%' or subnodecode like  'PLCharge%') and registno = ?
		boolean PLossUnderwriteFlag = false;
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select * from prplwftaskout where HANDLERSTATUS = '3' and ( subnodecode like  'PLVerify%' or subnodecode like  'PLCharge%') ");
		sqlUtil.append(" and registno = ?  ");
		sqlUtil.addParamValue(registNo);
		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		System.out.println(sql);
		int start = 0;
		int length = 10;
		Page<Object[]> page = new Page<Object[]>();
		try{
			page = baseDaoService.pagedSQLQuery(sql,start,length,values);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(page!=null&&page.getResult().size()>0){
			// 查询到已完成的人伤任务，则刷立案时不带人伤案均值
			PLossUnderwriteFlag = true;
		}
		return PLossUnderwriteFlag;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getClaimLossOfKindB(java.lang.String, ins.sino.claimcar.claim.vo.PrpLClaimKindVo, ins.sino.claimcar.claim.vo.PrpLClaimKindVo)
	 * @param registNo
	 * @param prpLClaimKindVoBPers
	 * @param prpLClaimKindVoBProp
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> getClaimLossOfKindB(String registNo,PrpLClaimKindVo prpLClaimKindVoBPers,PrpLClaimKindVo prpLClaimKindVoBProp){
		Map<String,BigDecimal> lossClaimLossMap = new HashMap<String,BigDecimal>();
		lossClaimLossMap.put("车物",DataUtils.NullToZero(prpLClaimKindVoBProp.getClaimLoss()));
		lossClaimLossMap.put("人",DataUtils.NullToZero(prpLClaimKindVoBPers.getClaimLoss()));
		BigDecimal claimLossProp = lossClaimLossMap.get("车物");
		BigDecimal claimLossPers = lossClaimLossMap.get("人");
		
		BigDecimal amountB = prpLClaimKindVoBProp.getAmount();
		BigDecimal claimLossSum = claimLossProp.add(claimLossPers);
		if(amountB.compareTo(claimLossSum)==-1){
			// 大于保额 按照估计赔款占比分摊保额
			BigDecimal proportPropInSum = (prpLClaimKindVoBProp.getClaimLoss()).divide(claimLossSum,5,BigDecimal.ROUND_HALF_UP);//车物占总赔款比例
			claimLossProp = amountB.multiply(proportPropInSum);
			claimLossPers = amountB.subtract(claimLossProp);
		}
		lossClaimLossMap.put("车物",claimLossProp);
		lossClaimLossMap.put("人",claimLossPers);
		return lossClaimLossMap;
	}
	
	// 根据报案号查询除本立案外的其他估损信息
	private List<PrpLClaimKindVo> findOthClaimFeeByRegistNo(String registNo,String claimNo) {
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>();
		logger.debug("报案号:"+registNo+"除本立案外的其他估损信息，本立案"+claimNo);
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(claimNo!=null){
			queryRule.addNotEqual("prpLClaim.claimNo",claimNo);
		}
		List<PrpLClaimKind> PrpLClaimKindPoList = databaseDao.findAll(PrpLClaimKind.class,queryRule);
//		List<PrpLClaimVo> prpLClaimVoList = null;
//		List<PrpLClaim> prpLClaimList = databaseDao.findAll(PrpLClaim.class,queryRule);
//		if(prpLClaimList!=null&& !prpLClaimList.isEmpty()){
//			prpLClaimVoList = Beans.copyDepth().from(prpLClaimList).toList(PrpLClaimVo.class);
//		}
//		if(prpLClaimVoList!=null){
//			for(PrpLClaimVo prpLClaimVo:prpLClaimVoList){
//				prpLClaimKindVoList.addAll(prpLClaimVo.getPrpLClaimKinds());
//			}
//		}
		if(PrpLClaimKindPoList!=null&& !PrpLClaimKindPoList.isEmpty()){
			for(PrpLClaimKind claimkind:PrpLClaimKindPoList){
				PrpLClaimKindVo claimkindVo = new PrpLClaimKindVo();
				Beans.copy().from(claimkind).to(claimkindVo);
				prpLClaimKindVoList.add(claimkindVo);
			}		
		}
		return prpLClaimKindVoList;
	}
	private PrpLClaimVo loadClaim(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo,PrpLCheckVo prpLCheckVo,ClaimFeeCondition claimCond) {
		PrpLClaimVo prpLClaimVo = new PrpLClaimVo();
		// 从报案对象复制同名属性
		Datas.copySimpleObjectToTargetFromSource(prpLClaimVo,prpLRegistVo,false);
		Datas.copySimpleObjectToTargetFromSource(prpLClaimVo,prpLCMainVo,false);
		prpLClaimVo.setFlag("");
		// prpLClaimVo.setDamageAddress(prpLCheckVo.getPrpLCheckTask().getCheckAddress());
		// prpLClaimVo.setLossType(claimCond.getLossType());
		// prpLClaimVo.setDamageFlag(claimCond.getDamageFlag());
		prpLClaimVo.setCiIndemDuty(claimCond.getCiDutyFlag());
		prpLClaimVo.setIndemnityDuty(claimCond.getIndemnityDuty());
		prpLClaimVo.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		prpLClaimVo.setCaseFlag(claimCond.getClaimSlefFlag());
		prpLClaimVo.setIsSubRogation(claimCond.getSubRogationFlag());
		prpLClaimVo.setIsTotalLoss(claimCond.getTotalLossFlag());
		prpLClaimVo.setSumRescueFee(new BigDecimal("0.00"));
		prpLClaimVo.setDamageTime(prpLRegistVo.getDamageTime());
		prpLClaimVo.setIsAlarm(prpLRegistVo.getPrpLRegistExt().getIsAlarm());//是否报警
		prpLClaimVo.setTpFlag(prpLRegistVo.getTpFlag());//通赔标识
		
		// prpLClaimVo.setDamageStartHour(Integer.valueOf(StringUtils.split(prpLregist.getDamageHour(), ":")[0]));
		if(prpLCheckVo!=null){
			prpLClaimVo.setDamageCode(prpLCheckVo.getDamageCode());
			// prpLClaimVo.setDamageName(prpLCheckVo.getDamageName());
			prpLClaimVo.setDamageTypeCode(prpLCheckVo.getDamageTypeCode());
			// prpLClaimVo.setDamageAreaCode(prpLCheckVo.getPrpLCheckTask().get);
			// prpLClaimVo.setDamageAddressType(prpLCheckVo.getDamageAddressType());
			prpLClaimVo.setClaimType(prpLCheckVo.getClaimType());
			// prpLClaimVo.setHandler1Code("");
			// prpLClaimVo.setHandlerCode("");
			// prpLClaimVo.setEscapeFlag("  ");// 默认为不是逃逸案
			if("".equals(StringUtils.trimToEmpty(prpLCheckVo.getLossType()))){// 损失类型，为空时默认为非全损
				prpLClaimVo.setIsTotalLoss("0");
			}else{
				prpLClaimVo.setIsTotalLoss("1");
			}
		}
		
		// flag第四位弄为1
		if("".equals(StringUtils.trimToEmpty(prpLClaimVo.getFlag()))){
			prpLClaimVo.setFlag("   10");// 第四位为1,第五位为0
		}else{
			prpLClaimVo.setFlag(prpLClaimVo.getFlag().substring(0,3)+"10"+prpLClaimVo.getFlag().substring(5,prpLClaimVo.getFlag().length()));
		}

		return prpLClaimVo;
	}
	/**
	 * 核损通过标志List
	 */
	public static List<String> UnderWritePassList = new ArrayList<String>();
	static{
		UnderWritePassList.add(CodeConstants.VeriFlag.PASS);
		UnderWritePassList.add(CodeConstants.VeriFlag.AUTOPASS);
	}

	/**
	 * 刷估损根据报案号获得车辆损失项列表 查勘和定损都有的取定损的损失信息
 	 */
	private List<LossItem> findCarLossInfo(PrpLCheckVo prpLCheckVo,List<PrpLDlossCarMainVo> prpLDlossCarMainVoList,PrpLCMainVo prpLCMainVo) {
		LossInfo lossInfo = new LossInfo();
		// 定损车辆损失数据
		if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				Map<String,Object> map = new HashMap<String,Object>();
				if(!UnderWritePassList.contains(prpLDlossCarMainVo.getUnderWriteFlag())){
					// 核损金额为空或者未通过核损不组织该数据
					continue;
				}
				// TODO 是否需要管控理算已扣减不组织
				if("1101".equals(prpLCMainVo.getRiskCode())){
					// 理算已扣减不组织该数据,查勘也不组织
					if( !prpLDlossCarMainVo.getLossState().substring(0,1).equals("0")){
						// 程序走到这段代码的时候，map里面再存值已经没有什么意义了
//						map.put("registNo",prpLDlossCarMainVo.getRegistNo());
//						map.put("prpLthirdPartyId",prpLDlossCarMainVo.getSerialNo());
						continue;
					}
				}else{
					if( !prpLDlossCarMainVo.getLossState().substring(1,2).equals("0")){
						// 理算已扣减不组织该数据,查勘也不组织   map里面存值没有什么意义
//						map.put("registNo",prpLDlossCarMainVo.getRegistNo());
//						map.put("prpLthirdPartyId",prpLDlossCarMainVo.getSerialNo());
						continue;
					}
				}
				LossItem lossItem = new LossItem();

				PrpLDlossCarInfoVo prpLDlossCarInfoVo = lossCarService.findPrpLDlossCarInfoVoById(prpLDlossCarMainVo.getCarId());
				if(prpLDlossCarInfoVo==null){
					logger.debug("RegistNo["+prpLDlossCarMainVo.getRegistNo()+"]:无法找到主键为"+prpLDlossCarMainVo.getCheckCarId()+"的prpLDlossCarInfo");
					throw new BusinessException(
							"RegistNo["+prpLDlossCarMainVo.getRegistNo()+"]:无法找到主键为"+prpLDlossCarMainVo.getCheckCarId()+"的prpLDlossCarInfo",false);
				}
				String lossItemType = null;
				if("1".equals(prpLDlossCarMainVo.getSerialNo().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_LOSS;
				}else{
					lossItemType = CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS;
				}
				lossItem.setLossItemType(lossItemType);
				lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(lossItemType));
				lossItem.setLossName(StringUtils.trim(prpLDlossCarInfoVo.getLicenseNo()));
				if(prpLDlossCarMainVo.getDeflossCarType().equals("1")){//标的车取保单信息表的车辆种类字段
					lossItem.setLossType(registQueryService.findCItemCarByRegistNo(prpLDlossCarMainVo.getRegistNo()).getCarType());
				} else{//三者车取losscarinfo表的数据
					lossItem.setLossType(prpLDlossCarInfoVo.getPlatformCarKindCode());	
				}
				// 取统一的车辆责任表
				PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLDlossCarMainVo.getRegistNo(),prpLDlossCarMainVo.getSerialNo());
				BigDecimal ciIndemDuty = BigDecimal.ZERO;

				if(prpLCheckDutyVo==null){
					ciIndemDuty = new BigDecimal("0.00");
				}else if("1".equals(prpLCheckDutyVo.getCiDutyFlag())){
					ciIndemDuty = new BigDecimal("100.00");
				}
				lossItem.setCiIndemDuty(ciIndemDuty);
				// 取核损估损
				lossItem.setSumClaim(prpLDlossCarMainVo.getSumVeriLossFee() == null ? BigDecimal.ZERO : prpLDlossCarMainVo.getSumVeriLossFee());
				// 取核损施救费,立案改造.GAOANXU.20100817
				lossItem.setRescueFee(prpLDlossCarMainVo.getSumVeriRescueFee() == null ? BigDecimal.ZERO : prpLDlossCarMainVo.getSumVeriRescueFee());
				lossItem.setDataSource("车辆核损");

				
				map.put("registNo",prpLDlossCarMainVo.getRegistNo());
				map.put("prpLthirdPartyId",prpLDlossCarMainVo.getSerialNo());
				// 添加商业险责任比例
				lossItem.setIndemnityDuty(prpLCheckDutyVo.getIndemnityDuty());
				lossItem.setIndemnityDutyRate(prpLCheckDutyVo.getIndemnityDutyRate());
				lossItem.setItemKey(map);
				lossItem.setKeyId(prpLDlossCarMainVo.getId());
				logger.info("车辆定损刷估损数据 报案号"+prpLDlossCarMainVo.getRegistNo()+"lossItem.getLossName():"+lossItem.getLossName()
						+ ",lossItem.getDataSource():"+lossItem.getDataSource()+",lossItem.getSumClaim():"+lossItem.getSumClaim()
						+",lossItem.getRescueFee():"+lossItem.getRescueFee()+",lossItem.getIndemnityDutyRate():"+lossItem.getIndemnityDutyRate());
				lossInfo.addItem(lossItem);
			}
		}

		// 从查勘获取车辆损失信息
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> prpLCheckCarVoList = prpLCheckTaskVo.getPrpLCheckCars();
		for(PrpLCheckCarVo checkCarVo:prpLCheckCarVoList){
			LossItem lossItem = new LossItem();
			lossItem.setDataSource("查勘");
			lossItem.setSumClaim(checkCarVo.getLossFee());
			lossItem.setRescueFee(checkCarVo.getRescueFee());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("registNo",checkCarVo.getRegistNo());
			map.put("prpLthirdPartyId",checkCarVo.getSerialNo());
			if( !lossInfo.isExistKey(map)){
				String lossItemType = null;
				if("1".equals(checkCarVo.getSerialNo().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_LOSS;
				}else{
					lossItemType = CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS;
				}
				lossItem.setLossItemType(lossItemType);
				lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(lossItemType));
				lossItem.setLossName(StringUtils.trim(checkCarVo.getPrpLCheckCarInfo().getLicenseNo()));
				lossItem.setItemKey(map);
				lossItem.setIndemnityDuty(checkCarVo.getIndemnityDuty());
				lossItem.setIndemnityDutyRate(checkCarVo.getIndemnityDutyRate());
				BigDecimal ciIndemDuty = BigDecimal.ZERO;
				if("1".equals(checkCarVo.getCiIndemDuty())){
					ciIndemDuty = new BigDecimal("100.00");
				}
				lossItem.setCiIndemDuty(ciIndemDuty);
				// 没有定损，存查勘的id
				if(lossItem.getKeyId()==null){
					lossItem.setKeyId(checkCarVo.getCarid());
				}
				logger.info("车辆查勘刷估损数据 报案号"+checkCarVo.getRegistNo()+"lossItem.getLossName():"+lossItem.getLossName()
						+",lossItem.getDataSource():"+lossItem.getDataSource()+",lossItem.getSumClaim():"+lossItem.getSumClaim()
						+",lossItem.getRescueFee():"+lossItem.getRescueFee()+",lossItem.getIndemnityDutyRate():"+lossItem.getIndemnityDutyRate());
				lossInfo.addItem(lossItem);
			}
		}
		return lossInfo.getLossItems();
	}

	// 根据报案号获得财产损失项列表 两者都有的取定损的损失信息
	private List<LossItem> findPropLossInfo(PrpLCheckVo prpLCheckVo,List<PrpLdlossPropMainVo> prpLdlossPropMainVoList,PrpLCMainVo prpLCMainVo) {
		LossInfo lossInfo = new LossInfo();
		String lossName = "";
		String lossTypeName = "";
		if(prpLdlossPropMainVoList!=null&& !prpLdlossPropMainVoList.isEmpty()){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				Map<String,Object> map = new HashMap<String,Object>();
				if(!UnderWritePassList.contains(prpLdlossPropMainVo.getUnderWriteFlag())){
					// 核损金额为空或者未通过核损不组织该数据
					continue;
				}
				// TODO 是否需要管控理算已扣减不组织
				if("1101".equals(prpLCMainVo.getRiskCode())){
					// 理算已扣减不组织该数据，查勘也不组织
					if( !prpLdlossPropMainVo.getLossState().substring(0,1).equals("0")){
						// 程序走到这段代码的时候，map里面存值没有什么意义，下一次循环map是新建的对象
//						map.put("registNo",prpLdlossPropMainVo.getRegistNo());
//						// 因为财产定损环节没有存 查勘环节Prplcheckprop的id，所以此处只能通过SerialNo去判断（某一损失项，查勘定损环节存的SerialNo一样）
//						map.put("checkPropId",prpLdlossPropMainVo.getSerialNo());
						continue;
					}
				}else{
					if( !prpLdlossPropMainVo.getLossState().substring(1,2).equals("0")){
						// 理算已扣减不组织该数据，查勘也不组织
//						map.put("registNo",prpLdlossPropMainVo.getRegistNo());
//						// 因为财产定损环节没有存 查勘环节Prplcheckprop的id，所以此处只能通过SerialNo去判断（某一损失项，查勘定损环节存的SerialNo一样）
//						map.put("checkPropId",prpLdlossPropMainVo.getSerialNo());
						continue;
					}
				}
				
				map.put("registNo",prpLdlossPropMainVo.getRegistNo());
				// 因为财产定损环节没有存 查勘环节Prplcheckprop的id，所以此处只能通过SerialNo去判断（某一损失项，查勘定损环节存的SerialNo一样）
				map.put("checkPropId",prpLdlossPropMainVo.getSerialNo());
				lossName = "";
				lossTypeName = "";
				for(PrpLdlossPropFeeVo prpLdlossPropFeeVo:prpLdlossPropMainVo.getPrpLdlossPropFees()){
					if( !"".equals(StringUtils.trimToEmpty(prpLdlossPropFeeVo.getLossItemName()))){
						lossName += prpLdlossPropFeeVo.getLossItemName();
						lossName += ",";
					}
					if( !"".equals(StringUtils.trimToEmpty(prpLdlossPropFeeVo.getLossSpeciesName()))){
						lossTypeName += prpLdlossPropFeeVo.getLossSpeciesName();
						lossTypeName += ",";
					}
				}

				if(lossName.length()>1){
					lossName = lossName.substring(0,lossName.length()-1);
				}
				if(lossTypeName.length()>1){
					lossTypeName = lossTypeName.substring(0,lossTypeName.length()-1);
				}
				// 取统一的车辆责任
				PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo());
				BigDecimal ciIndemDuty = BigDecimal.ZERO;

				if(prpLCheckDutyVo==null){
					ciIndemDuty = new BigDecimal("0.00");
				}else if("1".equals(prpLCheckDutyVo.getCiDutyFlag())){
					ciIndemDuty = new BigDecimal("100.00");
				}

				LossItem lossItem = new LossItem();
				String lossItemType = null;
				if("1".equals(prpLdlossPropMainVo.getSerialNo().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
				}else if("0".equals(prpLdlossPropMainVo.getSerialNo().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
				}else{
					lossItemType = CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP;
				}

				lossItem.setLossItemType(lossItemType);
				lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(lossItemType));
				lossItem.setCiIndemDuty(ciIndemDuty);
				// 生成损失项目描述字符串,此处默认三十个字
				lossItem.setLossName(simpleStr(lossName,30));
				lossItem.setLossTypeName(simpleStr(lossTypeName,30));
				lossItem.setSumClaim(prpLdlossPropMainVo.getSumVeriLoss() == null ? BigDecimal.ZERO : prpLdlossPropMainVo.getSumVeriLoss());// 财产核损金额
				lossItem.setRescueFee(prpLdlossPropMainVo.getVerirescueFee() == null ? BigDecimal.ZERO : prpLdlossPropMainVo.getVerirescueFee());// 财产核损施救费
				lossItem.setDataSource("财产核损");

				lossItem.setKeyId(prpLdlossPropMainVo.getId());
				lossItem.setItemKey(map);
				logger.info("财产定损刷估损数据 报案号"+prpLdlossPropMainVo.getRegistNo()+"lossItem.getLossName():"+lossItem.getLossName()
						+",lossItem.getDataSource():"+lossItem.getDataSource()+",lossItem.getSumClaim():"+lossItem.getSumClaim()
						+",lossItem.getRescueFee():"+lossItem.getRescueFee()+",lossItem.getIndemnityDutyRate():"+lossItem.getIndemnityDutyRate());
				lossInfo.addItem(lossItem);
			}
		}
		// 从查勘获取
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask();

		List<PrpLCheckPropVo> prpLCheckPropVoList = prpLCheckTaskVo.getPrpLCheckProps();
		for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropVoList){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("registNo",prpLCheckPropVo.getRegistNo());
			map.put("checkPropId",prpLCheckPropVo.getLossPartyId());
			if( !lossInfo.isExistKey(map)){// 判断定损中是否已经存在该项目
				lossName = prpLCheckPropVo.getLossItemName();
				// lossTypeName = prpLCheckPropVo.getLossSpeciesName();
				String lossItemType = null;
				if("1".equals(prpLCheckPropVo.getLossPartyId().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
				}else if("0".equals(prpLCheckPropVo.getLossPartyId().toString().trim())){
					lossItemType = CodeConstants.LossFeeType.THIS_CAR_PROP;
				}else{
					lossItemType = CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP;
				}

				// 取统一的车辆责任
				PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLCheckPropVo.getRegistNo(),prpLCheckPropVo.getLossPartyId()
						.intValue());
				BigDecimal ciIndemDuty = BigDecimal.ZERO;

				if(prpLCheckDutyVo==null){
					ciIndemDuty = new BigDecimal("0.00");
				}else if("1".equals(prpLCheckDutyVo.getCiDutyFlag())){
					ciIndemDuty = new BigDecimal("100.00");
				}

				LossItem lossItem = new LossItem();
				lossItem.setLossItemType(lossItemType);
				lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(lossItemType));
				lossItem.setCiIndemDuty(ciIndemDuty);
				lossItem.setDataSource("查勘");
				// 生成损失项目描述字符串,此处默认三十个字
				lossItem.setLossName(simpleStr(lossName,30));
				lossItem.setLossTypeName(simpleStr(lossTypeName,30));
				lossItem.setSumClaim(prpLCheckPropVo.getLossFee());
				lossItem.setRescueFee(BigDecimal.ZERO);
				lossItem.setItemKey(map);
				lossItem.setKeyId(prpLCheckPropVo.getId());
				logger.info("财产查勘刷估损数据 报案号"+prpLCheckPropVo.getRegistNo()+"lossItem.getLossName():"+lossItem.getLossName()
						+",lossItem.getDataSource():"+lossItem.getDataSource()+",lossItem.getSumClaim():"+lossItem.getSumClaim()
						+",lossItem.getRescueFee():"+lossItem.getRescueFee()+",lossItem.getIndemnityDutyRate():"+lossItem.getIndemnityDutyRate());
				lossInfo.addItem(lossItem);
			}
		}
		return lossInfo.getLossItems();
	}

	// 根据报案号获得人伤跟踪列表
	private List<LossItem> findPersonLossInfo(PrpLCheckVo prpLCheckVo,List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList,PrpLCMainVo prpLCMainVo) {
		List<LossItem> lossItems = new ArrayList<LossItem>(0);
		PrpLClaimVo prpLClaim = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCMainVo.getRegistNo(),prpLCMainVo.getPolicyNo());
		BigDecimal sumClaim = BigDecimal.ZERO;
		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		BigDecimal sumClaimForMedical = BigDecimal.ZERO;// 医疗费
		if(prpLDlossPersTraceMainVoList!=null&& !prpLDlossPersTraceMainVoList.isEmpty()){
			for(PrpLDlossPersTraceMainVo traceMainVo:prpLDlossPersTraceMainVoList){
				if(traceMainVo.getPrpLDlossPersTraces()!=null&& !traceMainVo.getPrpLDlossPersTraces().isEmpty()){
					for(PrpLDlossPersTraceVo prpLDlossPersTraceVo:traceMainVo.getPrpLDlossPersTraces()){
						Map<String,Object> map = new HashMap<String,Object>();
						if(!CodeConstants.PersVeriFlag.VERIFYPASS.equals(traceMainVo.getUnderwriteFlag())
								&&!CodeConstants.PersVeriFlag.CHARGEPASS.equals(traceMainVo.getUnderwriteFlag())
								/*&&!CodeConstants.PersVeriFlag.AUTOPASS.equals(traceMainVo.getUnderwriteFlag())*/){
							// 未核损通过不组织数据  人伤只有无需赔付时才会自动审核通过 此时人伤不组织
							continue;
						}
						// TODO 是否需要管控理算已扣减不组织
						if("1101".equals(prpLCMainVo.getRiskCode())){
							// 理算已扣减不组织该数据
							if( !traceMainVo.getLossState().substring(0,1).equals("0")){
//								map.put("registNo",prpLDlossPersTraceVo.getRegistNo());
//								map.put("personId",prpLDlossPersTraceVo.getCheckPersonId());
								continue;
							}
						}else{
							if( !traceMainVo.getLossState().substring(1,2).equals("0")){
								// 理算已扣减不组织该数据
//								map.put("registNo",prpLDlossPersTraceVo.getRegistNo());
//								map.put("personId",prpLDlossPersTraceVo.getCheckPersonId());
								continue;
							}
						}
						LossItem lossItem = new LossItem();
						lossItem.setDataSource("人伤");

						// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
						PrpLDlossPersInjuredVo prpLDlossPersInjuredVo = prpLDlossPersTraceVo.getPrpLDlossPersInjured();
						if(prpLDlossPersInjuredVo==null){
							throw new BusinessException("RegistNo["+prpLDlossPersTraceVo.getRegistNo()+"]:无法找到主键为"+prpLDlossPersTraceVo.getInjuredId()
									.toString()+"的PrpLinjured",false);
						}
						// 取统一的车辆责任
						PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLDlossPersInjuredVo.getRegistNo(),
								prpLDlossPersInjuredVo.getSerialNo());
						BigDecimal ciIndemDuty = BigDecimal.ZERO;

						if(prpLCheckDutyVo==null){
							ciIndemDuty = new BigDecimal("0.00");
						}else if("1".equals(prpLCheckDutyVo.getCiDutyFlag())){
							ciIndemDuty = new BigDecimal("100.00");
						}
						lossItem.setCiIndemDuty(ciIndemDuty);
						String lossItemType = null;
						if("1".equals(prpLDlossPersInjuredVo.getSerialNo().toString().trim())){
							if("2".equals(prpLDlossPersInjuredVo.getLossItemType().trim())){
								// 本车乘客
								lossItemType = CodeConstants.LossFeeType.THIS_PERSON_LOSS;
							}else if("3".equals(prpLDlossPersInjuredVo.getLossItemType().trim())){
								// 本车司机
								lossItemType = CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS;
							}
						}else if("0".equals(prpLDlossPersInjuredVo.getSerialNo().toString().trim())){
							// 地面人员
							lossItemType = CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON;
						}else{
							// 三者人伤
							lossItemType = CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS;
						}
						lossItem.setLossItemType(lossItemType);
						lossItem.setLossTypeName(CodeConstants.LossFeeType_Map.get(lossItemType));
						// 人伤核损未录入定损金额且录入了估损金额（审核）则取后者的值
						if (prpLDlossPersTraceVo.getSumVeriDefloss() == null && prpLDlossPersTraceVo.getSumVeriReportFee() != null) {
							sumClaim = DataUtils.NullToZero(prpLDlossPersTraceVo.getSumVeriReportFee());
						} else {
							// 防止出现空指针
							sumClaim = DataUtils.NullToZero(prpLDlossPersTraceVo.getSumVeriDefloss());
						}
						// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中——只三者人拆分
						if("0".equals(prpLDlossPersInjuredVo.getSerialNo().toString().trim())){
							List<PrpLDlossPersTraceFeeVo> prpLDlossPersTraceFeeVoList = prpLDlossPersTraceVo.getPrpLDlossPersTraceFees();
							if(prpLDlossPersTraceFeeVoList!=null&& !prpLDlossPersTraceFeeVoList.isEmpty()){
								for(PrpLDlossPersTraceFeeVo prpLDlossPersTraceFeeVo:prpLDlossPersTraceFeeVoList){
									if(prpLDlossPersTraceFeeVo.getFeeTypeCode()!=null&& !"".equals(prpLDlossPersTraceFeeVo.getFeeTypeCode())){
										// 1、医疗费；2、续医费；3、营养费；4、住院伙食补助；5、整容费 统一加入到医药费里
										if(CodeConstants.MedicalFee_Map.containsKey(prpLDlossPersTraceFeeVo.getFeeTypeCode())){
											// 人伤核损未录入定损金额且录入了估损金额（审核）则取后者的值
											BigDecimal veriDefloss = BigDecimal.ZERO;
											if(prpLDlossPersTraceFeeVo.getVeriDefloss()==null&&prpLDlossPersTraceFeeVo.getVeriReportFee()!=null){
												veriDefloss = DataUtils.NullToZero(prpLDlossPersTraceFeeVo.getVeriReportFee());
											}else{
												veriDefloss = DataUtils.NullToZero(prpLDlossPersTraceFeeVo.getVeriDefloss());
											}
											sumClaimForMedical = sumClaimForMedical.add(veriDefloss);
										}
									}
									lossItem.setLossItemTypeMedical(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS_Medical);// 单独为医疗费增加一个
								}
							}
						}

						lossItem.setSumClaim(sumClaim.setScale(2,BigDecimal.ROUND_HALF_UP));
						lossItem.setSumClaimForMedical(sumClaimForMedical.setScale(2,BigDecimal.ROUND_HALF_UP));
						// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
						lossItem.setRejectFee(BigDecimal.ZERO);
						lossItem.setLossTypeName(prpLDlossPersTraceVo.getPersonName());// 人员名称
						lossItem.setLossName(prpLDlossPersInjuredVo.getTicName());// 行业
						
						map.put("registNo",prpLDlossPersInjuredVo.getRegistNo());
						map.put("personId",prpLDlossPersTraceVo.getCheckPersonId());
						lossItem.setItemKey(map);
						lossItem.setKeyId(prpLDlossPersInjuredVo.getId());
						logger.info("人伤损失项刷估损数据 报案号: "+prpLDlossPersInjuredVo.getRegistNo()+",lossItem.getLossTypeName():"+lossItem.getLossTypeName()+",lossItem.getDataSource():"+lossItem.getDataSource()
								+",lossItem.getSumClaim():"+lossItem.getSumClaim()+",lossItem.getRejectFee():"+lossItem.getRejectFee()
								+",lossItem.getLossItemType():"+lossItem.getLossItemType()+",lossItem.getSumClaimForMedical():"+lossItem.getSumClaimForMedical()
								+",lossItem.getLossItemTypeMedical():"+lossItem.getLossItemTypeMedical());
						lossItems.add(lossItem);
					}
				}
			}
			return lossItems;
		}
		

		//20160805 查勘自动立案不从案均值初始化人伤数据
		// 查勘直接取案均值
		/*PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLCheckVo.getRegistNo());
		if(prpLRegistVo.getPrpLRegistPersonLosses()==null||prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
			return lossItems;
		}
		
		
		String riskCode = prpLCMainVo.getRiskCode().trim();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(prpLRegistVo.getDamageTime());
		Integer demageYear = Integer.parseInt(dateString.substring(0,4));
		String comCode = prpLCMainVo.getComCode();
		if(riskCode.startsWith("11")){
			List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_BZ,
					null,demageYear,comCode);
			
			for(PrpDClaimAvgVo prpDClaimAvgVo:prpDClaimAvgVoList){
				LossItem lossItem = new LossItem();
				lossItem.setCiIndemDuty(BigDecimal.valueOf(100.00));
				lossItem.setIndemnityDuty("1");
				lossItem.setIndemnityDutyRate(BigDecimal.valueOf(100.00));
				lossItem.setSumClaim(prpDClaimAvgVo.getAvgAmount());
				lossItems.add(lossItem);
			}
		}else if(riskCode.startsWith("12")){
			// 此处需要给三者人、车上司机、车上乘客取案均值
			for(int i = 0; i<3; i++ ){
				LossItem lossItem = new LossItem();
				if(i==0){
					// 三者人
					List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_B,
							null,demageYear,comCode);
					PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
					lossItem.setLossItemType(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS);
					lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS));
					lossItem.setCiIndemDuty(BigDecimal.ZERO);
					lossItem.setIndemnityDuty("0");
					lossItem.setIndemnityDutyRate(BigDecimal.ZERO);
					lossItem.setSumClaim(prpDClaimAvgVo.getAvgAmount());
				}else if(i==1){
					// 本车司机
					List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_D11,
							null,demageYear,comCode);
					PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
					lossItem.setLossItemType(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS);
					lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS));
					lossItem.setCiIndemDuty(BigDecimal.valueOf(100.00));
					lossItem.setIndemnityDuty("1");
					lossItem.setIndemnityDutyRate(BigDecimal.valueOf(100.00));
					lossItem.setSumClaim(prpDClaimAvgVo.getAvgAmount());
				}else if(i==2){
					// 本车乘客
					List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_D12,
							null,demageYear,comCode);
					PrpDClaimAvgVo prpDClaimAvgVo = prpDClaimAvgVoList.get(0);
					lossItem.setLossItemType(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS);
					lossItem.setLossItemTypeName(CodeConstants.LossFeeType_Map.get(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS));
					lossItem.setCiIndemDuty(BigDecimal.valueOf(100.00));
					lossItem.setIndemnityDuty("1");
					lossItem.setIndemnityDutyRate(BigDecimal.valueOf(100.00));
					lossItem.setSumClaim(prpDClaimAvgVo.getAvgAmount());
				}
				lossItems.add(lossItem);
			}
		}*/
		return lossItems;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#addOrUpdateClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo, ins.sino.claimcar.flow.vo.PrpLWfTaskVo, java.lang.String)
	 * @param prpLClaimVo
	 * @param prpLWfTaskVo
	 * @param taskId
	 * @return
	 */
	@Override
	public PrpLClaimVo addOrUpdateClaim(PrpLClaimVo prpLClaimVo,PrpLWfTaskVo prpLWfTaskVo,String taskId) {
		logger.debug("报案号:"+prpLClaimVo.getRegistNo()+",taskId"+taskId+"保存或更新立案："+prpLClaimVo.getClaimNo()+"，更新估损次数！");
		PrpLClaimVo oldPrpLclaim = claimService.findClaimVoByRegistNoAndPolicyNo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());
		
		if(oldPrpLclaim!=null){// 立案已存在
			prpLClaimVo.setClaimNo(prpLClaimVo.getClaimNo());
			prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);

			claimService.updateClaim(prpLClaimVo);

			// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
			claimKindHisService.saveClaimKindHis(prpLClaimVo);

		}else{ // 立案不存在
			Calendar cl = Calendar.getInstance();
			Date today = new Date();
			cl.setTime(today);
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLClaimVo.getRegistNo());
			PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());
			PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(prpLClaimVo.getRegistNo());
			ClaimFeeCondition claimCond = loadSumClaimCondition(prpLRegistVo,prpLCMainVo,prpLCheckVo,null);
			
			PrpLClaimVo tPrpLclaim = this.loadClaim(prpLRegistVo,prpLCMainVo,prpLCheckVo,claimCond);
			Datas.copySimpleObjectToTargetFromSource(tPrpLclaim,prpLClaimVo,false);
			tPrpLclaim.setPrpLClaimKinds(prpLClaimVo.getPrpLClaimKinds());// 分险别估损
			// tPrpLclaim.setPrpLdisasters(prpLClaimVo.getPrpLdisasters());// 巨灾信息
			// if (prpLClaimVo.getPrpLclaimOpinion() != null) {// 二级立案意见
			// tPrpLclaim.setPrpLclaimOpinion(prpLClaimVo.getPrpLclaimOpinion());
			// }
			prpLClaimVo = tPrpLclaim;
			// prpLClaimVo.setOperatorName(codeService.translateCode(CodeService.USERCODE, prpLClaimVo.getOperatorCode(), prpLClaimVo.getRiskCode(), "C"));

			// 统一获取单号入参传登录人员的归属机构
			String claimNo = "";

			claimNo = billNoService.getClaimNo(prpLCMainVo.getComCode(),prpLCMainVo.getRiskCode());
			this.updateClaimFeeByClaim(prpLClaimVo,prpLClaimVo.getPrpLClaimKinds());
			// prpLClaimVo.setPrpLdisasters(tPrpLclaim.getPrpLdisasters());
			// for (PrpLdisaster prpLdisaster : prpLClaimVo.getPrpLdisasters()) {
			// prpLdisaster.setPrpLclaim(prpLclaim);
			// }
			prpLClaimVo.setClaimNo(claimNo);
			// prpLClaimVo.getPrpLclaimExt().setClaimNo(claimNo);
			// if (prpLClaimVo.getPrpLclaimOpinion() != null) {
			// prpLClaimVo.getPrpLclaimOpinion().setClaimNo(claimNo);
			// }
			// 问题分析报告 PICCTP-8058 PNC-23467 立案注销日期发生变化 modified by chenrong 2014-05-29 begin
			// if ((prpLclaim.getCancelDate() != null) && (prpLclaim.getCaseType() != null)) {// 注销、拒赔立案
			// // 统一单号生成规则为传操作人员的归属机构
			// String comCode = prpLclaim.getMakeCom();
			// cancelClaim(prpLclaim, null, comCode);
			// }
			// 问题分析报告 PICCTP-8058 PNC-23467 立案注销日期发生变化 modified by chenrong 2014-05-29 end
			prpLCMainVo.setClaimNo(claimNo);
			if(prpLClaimVo.getMercyFlag()==null){
				prpLClaimVo.setMercyFlag("0");
			}
			
			prpLClaimVo.setEstiTimes(1);

			logger.debug("报案号："+prpLClaimVo.getRegistNo()+"，保单号："+prpLClaimVo.getPolicyNo()+"立案不存在，将保存立案，生成立案号："+claimNo+",估损金额："+prpLClaimVo
					.getSumDefLoss()+",总估计赔款"+prpLClaimVo.getSumPaid());
			claimService.saveClaim(prpLClaimVo);

			// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
			claimKindHisService.saveClaimKindHis(prpLClaimVo);
		}
		return prpLClaimVo;
	}

	// 主表子表关联
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeByClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo, java.util.List)
	 * @param prpLClaimVo
	 * @param prpLClaimKindVoList
	 * @return
	 */
	@Override
	public PrpLClaimVo updateClaimFeeByClaim(PrpLClaimVo prpLClaimVo,List<PrpLClaimKindVo> prpLClaimKindVoList) {
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			prpLClaimKindVo.setCreateTime(new Date());
			prpLClaimKindVo.setCreateUser(prpLClaimVo.getCreateUser());
			prpLClaimKindVo.setUpdateTime(prpLClaimVo.getUpdateTime());
			prpLClaimKindVo.setUpdateUser(prpLClaimVo.getUpdateUser());
			prpLClaimKindVo.setRiskCode(prpLClaimVo.getRiskCode());
			prpLClaimKindVo.setClaimNo(prpLClaimVo.getClaimNo());
			prpLClaimKindVo.setCurrency("CNY");
			if(prpLClaimKindVo.getValidFlag()==null){
				prpLClaimKindVo.setValidFlag("1");
			}
			if(prpLClaimKindVo.getFlag()==null){
				prpLClaimKindVo.setFlag("I");
			}
		}

		prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
		return prpLClaimVo;
	}

	// 主表子表关联方法重载,关联两个子表
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeByClaim(ins.sino.claimcar.claim.vo.PrpLClaimVo, java.util.List, java.util.List)
	 * @param prpLClaimVo
	 * @param prpLClaimKindVoList
	 * @param prpLClaimKindFeeVoList
	 * @return
	 */
	@Override
	public PrpLClaimVo updateClaimFeeByClaim(PrpLClaimVo prpLClaimVo,List<PrpLClaimKindVo> prpLClaimKindVoList,
												List<PrpLClaimKindFeeVo> prpLClaimKindFeeVoList) {
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			prpLClaimKindVo.setCreateTime(new Date());
			prpLClaimKindVo.setCreateUser(prpLClaimVo.getCreateUser());
			prpLClaimKindVo.setUpdateTime(prpLClaimVo.getUpdateTime());
			prpLClaimKindVo.setUpdateUser(prpLClaimVo.getUpdateUser());
			prpLClaimKindVo.setRiskCode(prpLClaimVo.getRiskCode());
			prpLClaimKindVo.setClaimNo(prpLClaimVo.getClaimNo());
			prpLClaimKindVo.setCurrency("CNY");
			if(prpLClaimKindVo.getValidFlag()==null){
				prpLClaimKindVo.setValidFlag("1");
			}
			if(prpLClaimKindVo.getFlag()==null){
				prpLClaimKindVo.setFlag("I");
			}
		}

		// claimKindFee子表与主表关联
		for(PrpLClaimKindFeeVo prpLClaimKindFeeVo:prpLClaimKindFeeVoList){
			prpLClaimKindFeeVo.setCreateTime(new Date());
			prpLClaimKindFeeVo.setCreateUser(prpLClaimVo.getCreateUser());
			prpLClaimKindFeeVo.setUpdateTime(prpLClaimVo.getUpdateTime());
			prpLClaimKindFeeVo.setUpdateUser(prpLClaimVo.getUpdateUser());
			prpLClaimKindFeeVo.setRiskCode(prpLClaimVo.getRiskCode());
			prpLClaimKindFeeVo.setClaimNo(prpLClaimVo.getClaimNo());
			prpLClaimKindFeeVo.setCurrency("CNY");
			if(prpLClaimKindFeeVo.getValidFlag()==null){
				prpLClaimKindFeeVo.setValidFlag("1");
			}
			if(prpLClaimKindFeeVo.getFlag()==null){
				prpLClaimKindFeeVo.setFlag("I");
			}
		}
		prpLClaimVo.setPrpLClaimKindFees(prpLClaimKindFeeVoList);
		prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
		return prpLClaimVo;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getKindLossByCondAndPolicyForNewClaimRefresh(ins.sino.claimcar.regist.vo.PrpLCMainVo, ins.sino.claimcar.claim.vo.ClaimFeeCondition)
	 * @param prpLCMainVo
	 * @param claimFeeCondition
	 * @return
	 */
	@Override
	public List<ClaimFeeExt> getKindLossByCondAndPolicyForNewClaimRefresh(PrpLCMainVo prpLCMainVo,ClaimFeeCondition claimFeeCondition) {
		logger.debug("registNo:"+claimFeeCondition.getRegistNo()+",policyNo:"+prpLCMainVo.getPolicyNo()+",ClaimType"+claimFeeCondition.getClaimType());
		List<ClaimFeeExt> fees = new ArrayList<ClaimFeeExt>(0);
		// if (claimCond.getIndemnityDutyRate() == null) {
		// claimCond.setIndemnityDutyRate(claimService.getIndemnityDutyRate(claimCond.getIndemnityDutyRate()));
		// }
		// if (CodeConstants.ClaimType.EACHHIT_SELFLOSS_CICASE_B.equals(claimCond.getClaimType())) {
		// claimCond.setIndemnityDutyRate(new BigDecimal(100));
		// }
		if(claimFeeCondition.getSubRogationFlag()==null){// 说明是后台刷新，而非前台页面点选触发后台刷新
			claimFeeCondition.setSubRogationFlag("0");
		}
		// if(claimCond.getSubRogationFlag() != null && claimCond.getSubRogationFlag().trim().equals("1")) {//北分代位求偿案件按100%计算
		// claimCond.setIndemnityDutyRate(new BigDecimal(100));
		// claimCond.setIndemnityDuty("0");
		// }
		BigDecimal carLossRate = BigDecimal.ZERO;
		if(prpLCMainVo.getRiskCode().startsWith("11")){
			logger.debug("交强计算");
			for(PrpLCItemKindVo prpLCItemKindVo:prpLCMainVo.getPrpCItemKinds()){
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLCItemKindVo.getKindCode().trim())){// 交强计算
					CompensateListVo compensateList = new CompensateListVo();
					CompensateListVo compensateListTemp = new CompensateListVo();
					compensateListTemp.setPrpLCItemKindVo(prpLCItemKindVo);
					compensateListTemp.setClaimFeeCondition(claimFeeCondition);
					compensateList = calculatorCI.calculate(compensateListTemp);
					carLossRate = compensateList.getCarLossRate();
					fees = refreshClaimEstiPaidService.refreshClaimEstiPaid(compensateList);
				}
			}
		}
		List<ClaimFeeExt> feesBi = new ArrayList<ClaimFeeExt>(0);
		if(prpLCMainVo.getRiskCode().startsWith("12")){// 商业保单或商业加交强保单
			logger.debug("商业保单或商业加交强保单");
			CompensateListVo compensateList = new CompensateListVo();
			CompensateListVo compensateListTemp = new CompensateListVo();
			compensateListTemp.setPrpLCItemKindVo(prpLCMainVo.getPrpCItemKinds().get(0));
			compensateListTemp.setClaimFeeCondition(claimFeeCondition);
			compensateList=calculatorBI.calculate(compensateListTemp);
			carLossRate = compensateList.getCarLossRate();
			feesBi=refreshClaimEstiPaidService.refreshClaimEstiPaidBI(compensateList);
		}
		if(feesBi!=null&&feesBi.size()>0){
			for(ClaimFeeExt claimFeeExt:feesBi){
				fees.add(claimFeeExt);
			}
		}
		if(fees!=null&&fees.size()>0){
			// B BZ险需要赋值车损占车财比例字段
			for(ClaimFeeExt claimFeeExt:fees){
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimFeeExt.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_BZ.equals(claimFeeExt.getKindCode())){
					claimFeeExt.setCarLossRate(carLossRate);
				}
			}
		}
		return fees;
	}

	// 根据分损失类计算的中间结果生成分险别估损信息(初始化使用) ----改造
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getKindLossByCondAndPolicy(ins.sino.claimcar.regist.vo.PrpLCMainVo, ins.sino.claimcar.claim.vo.ClaimFeeCondition)
	 * @param prpLCMainVo
	 * @param claimFeeCondition
	 * @return
	 */
	@Override
	public List<ClaimFeeExt> getKindLossByCondAndPolicy(PrpLCMainVo prpLCMainVo,ClaimFeeCondition claimFeeCondition) {
		logger.debug("registNo:"+claimFeeCondition.getRegistNo()+",policyNo:"+prpLCMainVo.getPolicyNo());
		List<ClaimFeeExt> fees = new ArrayList<ClaimFeeExt>(0);
		Map<String,BigDecimal> needSub = new HashMap<String,BigDecimal>(0);

		if(policyViewService.isInsuredKindCode(prpLCMainVo,CodeConstants.KINDCODE.KINDCODE_BZ)){
			logger.debug(prpLCMainVo.getPolicyNo()+"保单承保了BZ险");
			boolean kindcodeB = false;
			for(PrpLCItemKindVo itemKind:prpLCMainVo.getPrpCItemKinds()){
				List<PrpLCMainVo> prpLcMains = policyViewService.findPrpLCMainVoListByRegistNo(prpLCMainVo.getRegistNo());
				for(PrpLCMainVo prpLcMainTmp:prpLcMains){
					if(policyViewService.isInsuredKindCode(prpLcMainTmp,CodeConstants.KINDCODE.KINDCODE_B)){
						kindcodeB = true;
						break;
					}
				}
				logger.debug("itemKind.getKindCode()"+itemKind.getKindCode()+",kindcodeB"+kindcodeB);
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(itemKind.getKindCode())){
					needSub = getSumClaimTypeBz(fees,itemKind,claimFeeCondition,prpLCMainVo,kindcodeB);
					break;
				}
			}
		}

		PrpLCItemKindVo itemKindB = null;
		PrpLCItemKindVo itemKindM = null;
		boolean isItemKindM = false;
		// 不计免赔金额
		double excpetPay = 0;

		List<PrpLCItemKindVo> prpLcItemKinds = prpLCMainVo.getPrpCItemKinds();
		Map<String,Double> dutyDeductRateMap = registQueryService.getDeductRate(prpLCMainVo.getRegistNo());// 免赔率

		// 用于M险拆分后获得某个险别承保的M险
		Map<String,PrpLCItemKindVo> mKindCodeMap = new HashMap<String,PrpLCItemKindVo>();
		for(PrpLCItemKindVo itemKind:prpLcItemKinds){
			String kindCode = itemKind.getKindCode().trim();
			if(policyViewService.isMKindCode(prpLCMainVo.getRegistNo(),kindCode)){
				mKindCodeMap.put(kindCode,itemKind);
			}
		}

		for(PrpLCItemKindVo itemKind:prpLcItemKinds){
			String kindCode = itemKind.getKindCode().trim();
			boolean isMKindCode = policyViewService.isMKindCode(prpLCMainVo.getRegistNo(),kindCode);
			PrpLCItemKindVo itemKindMTmp = mKindCodeMap.get(kindCode);

			if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G)||kindCode
					.equals(CodeConstants.KINDCODE.KINDCODE_F)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)||kindCode
					.equals(CodeConstants.KINDCODE.KINDCODE_E)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)){
				excpetPay = getSumClaimTypeA(fees,itemKind,claimFeeCondition,needSub,isMKindCode,
						dutyDeductRateMap.get(kindCode)==null ? 0 : dutyDeductRateMap.get(kindCode),excpetPay);
//			}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D1)){
//				excpetPay = getSumClaimTypeD1(fees,itemKind,claimFeeCondition,needSub,isMKindCode,
//						dutyDeductRateMap.get(kindCode)==null ? 0 : dutyDeductRateMap.get(kindCode),excpetPay);
			}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				itemKindB = itemKind;
				if(isMKindCode){
					isItemKindM = true;
				}
			}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_D2)){
				excpetPay = getSumClaimTypeD2(fees,itemKind,claimFeeCondition,needSub,isMKindCode,
						dutyDeductRateMap.get(kindCode)==null ? 0 : dutyDeductRateMap.get(kindCode),excpetPay);
			}else if(isMKindCode){// 不管是历史的M险，还是新的拆分的险种都匹配上
				itemKindM = itemKind;
			}else{
				ClaimFeeExt fee = new ClaimFeeExt();
				fee.setKindCode(kindCode);
				fee.setRiskCode(itemKind.getRiskCode());
				fee.setItemKindNo(itemKind.getItemKindNo().intValue());
				fee.setModeCode(itemKind.getModeCode());
				fee.setSumClaim(BigDecimal.valueOf(0));
				fee.setRescueFee(BigDecimal.valueOf(0));
				fee.setEstiPaid(BigDecimal.valueOf(0));
				fee.setSumAmount(itemKind.getAmount());
				fee.setUnitAmount(fee.getSumAmount());
				if(dutyDeductRateMap.get(kindCode)!=null){
					fee.setDeductRate(BigDecimal.valueOf(dutyDeductRateMap.get(kindCode)));
				}else{
					fee.setDeductRate(BigDecimal.valueOf(0));
				}
				// fee.setDeductRate(BigDecimal.valueOf(getDeductRate(itemKind.getRiskCode(), kindCode, claimCond.getIndemnityDuty(), "Y", itemKind.getRegistNo())));
				fee.setSumRest(BigDecimal.valueOf(0));
				fee.setIndemnityDutyRate(claimFeeCondition.getIndemnityDutyRate());
				fee.setmKindCode(isItemKindM);
				logger.debug("fee.getKindCode()"+fee.getKindCode()+",fee.getSumClaim()"+fee.getSumClaim()+",fee.isMKindCode()"+fee.ismKindCode()+",fee.getEstiPaid()"+fee
						.getEstiPaid());
				fees.add(fee);
			}
			// 由于B险在后面会调用此方法，为了避免向peplclaimfee表中添加重复的B险别记录，在此过滤掉B险。
			if(itemKindMTmp!=null&& !kindCode.equals(CodeConstants.KINDCODE.KINDCODE_B)){
				this.addMFee(fees,itemKindMTmp,excpetPay);
				excpetPay = 0;
			}
		}

		if(itemKindB!=null){// 承保了商业三者
			PrpLCItemKindVo itemKindMTmp = mKindCodeMap.get(itemKindB.getKindCode());
			if(itemKindMTmp!=null){
				excpetPay = 0;
			}
			excpetPay = getSumClaimTypeB(fees,itemKindB,claimFeeCondition,needSub,isItemKindM,
					dutyDeductRateMap.get(itemKindB.getKindCode())==null ? 0 : dutyDeductRateMap.get(itemKindB.getKindCode()),excpetPay);
			if(itemKindMTmp!=null){
				this.addMFee(fees,itemKindMTmp,excpetPay);
			}
		}

		// PNCCAR,add by zhouwen,吉林分公司有一案件在进行立案500元估损时,不计免赔项目代入估损金额呈现负数,BEGIN.
		if(excpetPay<0){
			excpetPay = 0;
		}
		// PNCCAR,add by zhouwen,吉林分公司有一案件在进行立案500元估损时,不计免赔项目代入估损金额呈现负数,END.

		// 判断是否承保了不计免赔特约险
		if(itemKindM!=null&&mKindCodeMap.size()==0){
			ClaimFeeExt fee = new ClaimFeeExt();
			fee.setKindCode(itemKindM.getKindCode());// 不管新老，直接取kindcode值
			fee.setRiskCode(itemKindM.getRiskCode());
			fee.setItemKindNo(itemKindM.getItemKindNo().intValue());
			fee.setModeCode(itemKindM.getModeCode());
			fee.setSumClaim(new BigDecimal(excpetPay).setScale(2,BigDecimal.ROUND_HALF_UP));
			fee.setRescueFee(BigDecimal.valueOf(0));
			fee.setEstiPaid(new BigDecimal(excpetPay).setScale(2,BigDecimal.ROUND_HALF_UP));
			fee.setSumAmount(itemKindM.getAmount());
			fee.setUnitAmount(itemKindM.getUnitAmount());
			fee.setSumRest(BigDecimal.valueOf(0));
			logger.debug("fee.getKindCode()"+fee.getKindCode()+",fee.getSumClaim()"+fee.getSumClaim()+",fee.isMKindCode()"+fee.ismKindCode()+",fee.getEstiPaid()"+fee
					.getEstiPaid());
			fees.add(fee);
		}

		for(ClaimFeeExt claimFeeExt:fees){
			// 判断是否承保了不计免赔险begin
			if(mKindCodeMap.containsKey(claimFeeExt.getKindCode())){
				claimFeeExt.setmKindCodeOld(claimFeeExt.getKindCode());// 判断该不计免赔险为哪个险别的不计免赔
			}
			// 判断是否承保了不计免赔险end
		}
		return fees;
	}

	// 获取交强类的估损 参数fees为null,表示只需要计算交强险的赔款金额
	private Map<String,BigDecimal> getSumClaimTypeBz(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,
														PrpLCMainVo prpLcMain,boolean kindcodeB) {
		logger.debug("获取交强类的估损 参数fees为null,表示只需要计算交强险的赔款金额");
		Map<String,BigDecimal> needSub = new HashMap<String,BigDecimal>(0);// 需要扣减的交强险估损
		double sumClaimB1 = 0.00;
		double sumClaimB2 = 0.00;
		double sumClaimMedical = 0.00;

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中,交强险算上施救费和残值
		double rescueFeeB1 = 0.00;
		double rejectFeeB1 = 0.00;
		double rejectFeeB2 = 0.00;
		double bzPayA = 0.00;
		double bzPayD2 = 0.00;
		double bzPayD101 = 0.00; // 司机
		double bzPayD102 = 0.00; // 乘客

		bzPayA = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_LOSS).doubleValue();
		bzPayD2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_PROP).doubleValue();
		bzPayD101 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue();
		bzPayD102 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue();

		sumClaimB1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue()-claimCond.getOldSumClaimB1().doubleValue();

		sumClaimB2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue()-claimCond.getOldSumClaimB2().doubleValue();

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		sumClaimMedical = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS_Medical).doubleValue();

		rescueFeeB1 = claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getRescueFee(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue();

		rejectFeeB1 = claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getRejectFee(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue();

		rejectFeeB2 = claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getRejectFee(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue();

		double tSumClaimB;
		double tRescueFeeB1;// PNCCAR.人伤跟踪。取施救费时，判断是否有B险.GAOANXU.20100826
		double tSumClaimAD2;
		double tSumClaimA;
		double tSumClaimD2;
		double tSumClaimD101;
		double tSumClaimD102;

		// 人伤跟踪：交强超限部分金额
		double sumClaimB2Exceed;// 死亡伤残超限额金额
		double sumClaimMedicalExceed;// 医疗费超限额金额

		PrpLRegistVo prpLregist = registQueryService.findByRegistNo(prpLcMain.getRegistNo());
		Date damageTime = prpLregist.getDamageTime();
		ClaimFeeExt fee = new ClaimFeeExt();
		double sumAmountBZ = Double.valueOf(0);
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.PROPLOSS,claimCond.getCiIndemDuty(),damageTime);
		BigDecimal ciIndemDuty = claimCond.getCiIndemDuty();//责任比例
		Boolean isCiIndemDuty;//是否有责
		if (ciIndemDuty.compareTo(BigDecimal.ZERO) == 1) {
			isCiIndemDuty = true;//有责
		} else {
			isCiIndemDuty = false;//无责
		}
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,isCiIndemDuty,prpLregist.getRegistNo());
		// 人伤跟踪：立案改造——有施救费时，比较限额时算上施救费，取估损的时候不算施救费
		tSumClaimB = sumClaimB1;
		tRescueFeeB1 = rescueFeeB1;// 人伤跟踪：取施救费时，判断是否有B险.有B险计算分摊计算，没有取实际损失金额
		double tEstiPaidB = 0;
		double tSumClaimBall = sumClaimB1+tRescueFeeB1;
		if(tSumClaimBall>sumAmountBZ){
			// 立案改造：判断交强估损（估损+施救）是否大于限额，如果大于限额，需计算分项
			tSumClaimB = sumAmountBZ*( sumClaimB1/( sumClaimB1+tRescueFeeB1 ) );
			tRescueFeeB1 = sumAmountBZ*( tRescueFeeB1/( sumClaimB1+tRescueFeeB1 ) );

			// 立案改造：施救费算入交强险中，估计赔款取值规则为：估损金额+施救费再乘以责任比例
			// 如果施救费有值，估计赔款=计算后的估损+施救，即限额。
			if(tRescueFeeB1>0){
				tEstiPaidB = tSumClaimB+tRescueFeeB1;
			}else{
				tEstiPaidB = tSumClaimB;
			}
		}else{// 估损+施救不大于限额，估计赔款=估损+施救费
			tEstiPaidB = tSumClaimBall;
		}
		tSumClaimA = bzPayA;
		tSumClaimD2 = bzPayD2;
		tSumClaimAD2 = bzPayA+bzPayD2;
		if(tSumClaimAD2>sumAmountBZ){
			tSumClaimA = sumAmountBZ*bzPayA/tSumClaimAD2;
			tSumClaimD2 = sumAmountBZ*bzPayD2/tSumClaimAD2;
			tSumClaimAD2 = sumAmountBZ;
		}
		fee.setKindCode(itemKind.getKindCode());
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setUnitAmount(BigDecimal.valueOf(sumAmountBZ));
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setDeductRate(BigDecimal.ZERO);
		fee.setSumRest(BigDecimal.valueOf(Datas.round(rejectFeeB1+rejectFeeB2,2)));
		// 互碰自赔 财产损失 只赔付本车 并且按照本次的限额来算
		if(CodeConstants.DamageFlag.BI_CLAIM.equals(StringUtils.trimToEmpty(claimCond.getDamageFlag()))){
			fee.setEstiPaid(BigDecimal.valueOf(0));
			fee.setSumClaim(BigDecimal.valueOf(0));
			fee.setRescueFee(BigDecimal.valueOf(0));
		}else{
			if(CodeConstants.ClaimType.SPAY_CASE.equals(StringUtils.trimToEmpty(claimCond.getClaimType()))){
				// 因为加入施救费所以估计赔款特殊计算
				fee.setEstiPaid(BigDecimal.valueOf(Datas.round(tEstiPaidB,2)));

				// 人伤跟踪：立案中估损金额不进行限额调整，显示实际损失，只有估计赔款进行限额调整——2010-08-11调整：商业险保了B险估损，交强险进行限额调整，否则不进行限额调整
				if(kindcodeB){
					fee.setSumClaim(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));

					// PNCCAR.人伤跟踪。取施救费时，判断是否有B险.有B险计算分摊计算，没有取实际损失金额.TANJIANWEI.20100929
					fee.setRescueFee(BigDecimal.valueOf(Datas.round(tRescueFeeB1,2)));
				}else{
					fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimB1,2)));

					// PNCCAR.人伤跟踪。取施救费时，判断是否有B险.有B险计算分摊计算，没有取实际损失金额.TANJIANWEI.20100929
					fee.setRescueFee(BigDecimal.valueOf(Datas.round(rescueFeeB1,2)));
				}
			}else{
				// 因为加入施救费所以估计赔款特殊计算
				fee.setEstiPaid(BigDecimal.valueOf(Datas.round(tEstiPaidB,2)));

				// 人伤跟踪：立案中估损金额不进行限额调整，显示实际损失，只有估计赔款进行限额调整——2010-08-11调整：商业险保了B险估损，交强险进行限额调整，否则不进行限额调整
				if(kindcodeB){
					fee.setSumClaim(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));

					// PNCCAR.人伤跟踪。取施救费时，判断是否有B险.有B险计算分摊计算，没有取实际损失金额.GAOANXU.20100826
					fee.setRescueFee(BigDecimal.valueOf(Datas.round(tRescueFeeB1,2)));
				}else{
					fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimB1,2)));

					// PNCCAR.人伤跟踪。取施救费时，判断是否有B险.有B险计算分摊计算，没有取实际损失金额.GAOANXU.20100826
					fee.setRescueFee(BigDecimal.valueOf(Datas.round(rescueFeeB1,2)));
				}
			}
		}
		fee.setFeeTypeCode(CodeConstants.FeeTypeCode.PROPLOSS);
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		if(fees!=null){
			fees.add(fee);
		}
		needSub.put("sumClaimB1",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimB1:"+tSumClaimB);
		needSub.put("subPayA",BigDecimal.valueOf(tSumClaimA));
		logger.debug("subPayA:"+tSumClaimA);
		needSub.put("subPayD2",BigDecimal.valueOf(tSumClaimD2));
		logger.debug("subPayD2:"+tSumClaimD2);

		// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816
		needSub.put("rescueFeeB1",BigDecimal.valueOf(rescueFeeB1));// 因为人伤没有施救费和残值，无需+rescueFeeB2
		logger.debug("rescueFeeB1:"+rescueFeeB1);
		needSub.put("rejectFeeB1",BigDecimal.valueOf(rejectFeeB1));// 因为人伤没有施救费和残值，无需+rejectFeeB2
		logger.debug("rejectFeeB1:"+rejectFeeB1);
		fee = new ClaimFeeExt();

		// 取交抢险保额sumAmountBZ 51 人伤
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.PERSONLOSS,claimCond.getCiIndemDuty(),damageTime);
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,isCiIndemDuty,prpLregist.getRegistNo());
		tSumClaimB = sumClaimB2;
		if(tSumClaimB>sumAmountBZ){
			tSumClaimB = sumAmountBZ;
			sumClaimB2Exceed = sumClaimB2-sumAmountBZ;// 死亡伤残超限额金额
			needSub.put("sumClaimB2Exceed",BigDecimal.valueOf(sumClaimB2Exceed));// 死亡伤残超限额金额
			logger.debug("sumClaimB2Exceed:"+sumClaimB2Exceed);
		}
		tSumClaimD101 = bzPayD101;
		if(tSumClaimD101>sumAmountBZ){
			tSumClaimD101 = sumAmountBZ;
		}
		tSumClaimD102 = bzPayD102;
		if(tSumClaimD102>sumAmountBZ){
			tSumClaimD102 = sumAmountBZ;
		}
		fee.setKindCode(itemKind.getKindCode());
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setUnitAmount(BigDecimal.valueOf(sumAmountBZ));
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setDeductRate(BigDecimal.ZERO);
		fee.setRescueFee(BigDecimal.valueOf(0));
		fee.setSumRest(BigDecimal.valueOf(0));
		if(CodeConstants.DamageFlag.BI_CLAIM.equals(StringUtils.trimToEmpty(claimCond.getDamageFlag()))){
			fee.setEstiPaid(BigDecimal.valueOf(0));
			fee.setSumClaim(BigDecimal.valueOf(0));
		}else{
			fee.setEstiPaid(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));
			// 人伤跟踪：立案中估损金额不进行限额调整，显示实际损失，只有估计赔款进行限额调整——2010-08-11调整：商业险保了B险估损，交强险进行限额调整，否则不进行限额调整
			if(kindcodeB){
				fee.setSumClaim(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));
			}else{
				fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimB2,2)));
			}
		}
		fee.setFeeTypeCode(CodeConstants.FeeTypeCode.PERSONLOSS);
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		if(fees!=null){
			fees.add(fee);
		}
		needSub.put("sumClaimB2",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimB2:"+tSumClaimB);
		needSub.put("subPayD101",BigDecimal.valueOf(tSumClaimD101));
		logger.debug("subPayD101:"+tSumClaimD101);
		needSub.put("subPayD102",BigDecimal.valueOf(tSumClaimD102));
		logger.debug("subPayD102:"+tSumClaimD102);

		fee = new ClaimFeeExt();

		// 取交抢险保额sumAmountBZ 49,医疗费不自动估损——人上改造后医疗费需要自动估损
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,claimCond.getCiIndemDuty(),damageTime);
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,isCiIndemDuty,prpLregist.getRegistNo());

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		tSumClaimB = sumClaimMedical;
		if(tSumClaimB>sumAmountBZ){
			tSumClaimB = sumAmountBZ;
			sumClaimMedicalExceed = sumClaimMedical-sumAmountBZ;// 医疗费超限额金额
			needSub.put("sumClaimMedicalExceed",BigDecimal.valueOf(sumClaimMedicalExceed));
			logger.debug("sumClaimMedicalExceed:"+sumClaimMedicalExceed);
		}
		fee.setKindCode(itemKind.getKindCode());
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setUnitAmount(BigDecimal.valueOf(sumAmountBZ));
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setDeductRate(BigDecimal.ZERO);
		fee.setRescueFee(BigDecimal.valueOf(0));
		fee.setSumRest(BigDecimal.valueOf(0));

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		if(CodeConstants.DamageFlag.BI_CLAIM.equals(StringUtils.trimToEmpty(claimCond.getDamageFlag()))){
			fee.setEstiPaid(BigDecimal.valueOf(0));
			fee.setSumClaim(BigDecimal.valueOf(0));
		}else{
			fee.setEstiPaid(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));

			// 人伤跟踪：立案中估损金额不进行限额调整，显示实际损失，只有估计赔款进行限额调整——2010-08-11调整：商业险保了B险估损，交强险进行限额调整，否则不进行限额调整
			if(kindcodeB){
				fee.setSumClaim(BigDecimal.valueOf(Datas.round(tSumClaimB,2)));
			}else{
				fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimMedical,2)));
			}
		}
		fee.setFeeTypeCode(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		if(fees!=null){
			fees.add(fee);
		}
		needSub.put("sumClaimMedical",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimMedical:"+sumClaimMedical);
		return needSub;
	}

	// 获取车损类的估损
	private double getSumClaimTypeA(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,Map<String,BigDecimal> subPay,
									boolean isMKindCode,double deductRate,double exceptPay) {
		double subPayA = 0; // 需要扣减的交强险估损

		double sumAmount = 0;
		if(itemKind.getAmount()!=null){
			sumAmount = itemKind.getAmount().doubleValue();
		}
		double estiPaid = 0;
		double sumClaimA = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_LOSS).doubleValue();
		double rescueFeeA = claimCond.getRescueFee(CodeConstants.LossFeeType.THIS_CAR_LOSS).doubleValue();
		double rejectFeeA = claimCond.getRejectFee(CodeConstants.LossFeeType.THIS_CAR_LOSS).doubleValue();
		String kindCode = StringUtils.trimToEmpty(itemKind.getKindCode());
		// double deductRate = getDeductRate(itemKind.getRiskCode(), kindCode, claimCond.getIndemnityDuty(), "Y", itemKind.getRegistNo());

		ClaimFeeExt fee = new ClaimFeeExt();
		fee.setKindCode(kindCode);
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setSumRest(BigDecimal.valueOf(Datas.round(rejectFeeA,2)));
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setUnitAmount(fee.getSumAmount());
		fee.setDeductRate(BigDecimal.valueOf(deductRate));
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());

		if(( kindCode.equals(CodeConstants.KINDCODE.KINDCODE_G) )){ // 盗抢
			// 立案改造：盗抢、玻璃单独破碎、车身划痕需要计入施救费
			if(( sumClaimA+rescueFeeA-rejectFeeA )>sumAmount){
				estiPaid = sumAmount*( 1-deductRate/PERCENT );
				if(isMKindCode){// 如果承保了不计免赔险,需要计算不计免赔金额
					exceptPay += sumAmount*deductRate/PERCENT;
				}
			}else{
				estiPaid = ( sumClaimA+rescueFeeA-rejectFeeA )*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay += ( sumClaimA+rescueFeeA-rejectFeeA )*( deductRate/PERCENT );
				}
			}
		}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_F)){// 玻璃单独破碎
			// 对F险特殊判断，如是F险不取限额取车辆实际价格。——由于获取车辆实际价值的规则需要立案对象，但存在未立案就选择F险的情况，所以改用新车购置价对比.
			String policyNo = "";
			String registNo = "";
			if(claimCond!=null&&claimCond.getPolicyNo()!=null&&claimCond.getRegistNo()!=null){
				policyNo = claimCond.getPolicyNo();
				registNo = claimCond.getRegistNo();
			}
			PrpLCItemCarVo prpLcItemCar = policyViewService.findItemCarByRegistNoAndPolicyNo(registNo,policyNo);
			if(prpLcItemCar!=null&&prpLcItemCar.getPurchasePrice()!=null){
				sumAmount = prpLcItemCar.getPurchasePrice().doubleValue();
			}
			if(( sumClaimA+rescueFeeA-rejectFeeA )>sumAmount){
				estiPaid = sumAmount*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay += sumAmount*deductRate/PERCENT;
				}
			}else{
				estiPaid = ( sumClaimA+rescueFeeA-rejectFeeA )*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay += ( sumClaimA+rescueFeeA-rejectFeeA )*( deductRate/PERCENT );
				}
			}
		}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_L)){// 车身划痕
			if(( sumClaimA+rescueFeeA-rejectFeeA )>sumAmount){
				estiPaid = sumAmount*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay += sumAmount*deductRate/PERCENT;
				}
			}else{
				estiPaid = ( sumClaimA+rescueFeeA-rejectFeeA )*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay += ( sumClaimA+rescueFeeA-rejectFeeA )*( deductRate/PERCENT );
				}
			}
		}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_Z)||kindCode.equals(CodeConstants.KINDCODE.KINDCODE_E)){
			if(( sumClaimA-rejectFeeA )>sumAmount){
				estiPaid = sumAmount*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay = sumAmount*deductRate/PERCENT;
				}
			}else{
				estiPaid = ( sumClaimA-rejectFeeA )*( 1-deductRate/PERCENT );
				if(isMKindCode){
					exceptPay = ( sumClaimA-rejectFeeA )*( deductRate/PERCENT );
				}
			}
		}else if(kindCode.equals(CodeConstants.KINDCODE.KINDCODE_A)){
			// 立案改造：估损加上施救费
			double sumClaimAall = sumClaimA+rescueFeeA-subPayA;
			// if (sumClaimAall > sumAmount) {
			// sumClaimAall = sumAmount;
			// estiPaid = sumClaimAall * claimCond.getIndemnityDutyRate().doubleValue() / PERCENT * (1 - deductRate / PERCENT);
			// } else {
			// estiPaid = sumClaimAall * claimCond.getIndemnityDutyRate().doubleValue() / PERCENT * (1 - deductRate / PERCENT);
			// }
			estiPaid = sumClaimAall*claimCond.getIndemnityDutyRate().doubleValue()/PERCENT*( 1-deductRate/PERCENT );
			if(estiPaid>2*sumAmount){
				estiPaid = 2*sumAmount;
			}
			// 增加代位求偿案件计算方式
			if("B".equals(claimCond.getClaimType().trim())){
				if(itemKind.getDeductible()!=null){
					estiPaid = sumClaimAall*( 1-deductRate/PERCENT )-itemKind.getDeductible().doubleValue();
				}else{
					estiPaid = sumClaimAall*( 1-deductRate/PERCENT );
				}
			}
			if(isMKindCode){
				// 增加代位求偿案件计算方式
				if("B".equals(claimCond.getClaimType().trim())){
					exceptPay = sumClaimAall*deductRate/PERCENT;
				}else{
					exceptPay = sumClaimAall*claimCond.getIndemnityDutyRate().doubleValue()/PERCENT*( deductRate/PERCENT );
				}
			}
		}else{
			sumClaimA = 0;
			estiPaid = 0;
			rescueFeeA = 0;
		}
		fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimA,2)));
		fee.setEstiPaid(BigDecimal.valueOf(Datas.round(estiPaid,2)));
		fee.setRescueFee(BigDecimal.valueOf(Datas.round(rescueFeeA,2)));
		fee.setmKindCode(isMKindCode);
		fees.add(fee);
		logger.debug(claimCond.getLossType()+"--"+fee.getKindCode()+"---"+fee.getSumClaim()+"---"+fee.getEstiPaid());
		return exceptPay;
	}

	private double getSumClaimTypeD2(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,Map<String,BigDecimal> subPay,
										boolean isMKindCode,double deductRate,double excpetPay) {
		double subPayD2 = 0; // 需要扣减的交强险估损
		// double deductRate = getDeductRate(itemKind.getRiskCode(), "D2", claimCond.getIndemnityDuty(), "Y", itemKind.getRegistNo());
		ClaimFeeExt fee = new ClaimFeeExt();
		// if(this.commonService.isPrpAllUpgrage(null,null)){
		// fee.setKindCode(this.commonService.translateKindCode("D2","1",itemKind.getPolicyNo()));
		// }else{
		// fee.setKindCode("D2");
		// }
		fee.setKindCode(itemKind.getKindCode());
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setUnitAmount(fee.getSumAmount());
		fee.setDeductRate(BigDecimal.valueOf(Datas.round(deductRate,2)));
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		fee.setmKindCode(isMKindCode);
		double sumClaimD2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_PROP).doubleValue()-subPayD2;
		fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimD2,2)));
		fee.setRescueFee(claimCond.getRescueFee(CodeConstants.LossFeeType.THIS_CAR_PROP));
		fee.setSumRest(claimCond.getRejectFee(CodeConstants.LossFeeType.THIS_CAR_PROP));
		// D2险的估计赔款没有限额控制，估损金额不进行限额调整，损失多少估损多少。但估计赔款进行限额控制。
		double rescueFeeD2 = fee.getRescueFee().doubleValue();
		double estiPaid;
		if(sumClaimD2+rescueFeeD2>fee.getSumAmount().doubleValue()){
			estiPaid = fee.getSumAmount().doubleValue()-fee.getSumRest().doubleValue();
		}else{
			estiPaid = fee.getSumClaim().doubleValue()+fee.getRescueFee().doubleValue()-fee.getSumRest().doubleValue();
		}
		if(isMKindCode){
			excpetPay += estiPaid*deductRate/PERCENT;
		}
		estiPaid = estiPaid*claimCond.getIndemnityDutyRate().doubleValue()/PERCENT;
		estiPaid = estiPaid*( 1-deductRate/PERCENT );
		fee.setEstiPaid(BigDecimal.valueOf(Datas.round(estiPaid,2)));
		logger.debug(claimCond.getLossType()+"--"+fee.getKindCode()+"---"+fee.getSumClaim()+"---"+fee.getEstiPaid());
		fees.add(fee);
		return excpetPay;
	}

	private double getSumClaimTypeD1(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,Map<String,BigDecimal> subPay,
										boolean isMKindCode,double deductRate,double excpetPay) {
		double sumClaimD1;
		// double deductRate = getDeductRate(itemKind.getRiskCode(), "D1", claimCond.getIndemnityDuty(), "Y", itemKind.getRegistNo());
		ClaimFeeExt fee = new ClaimFeeExt();
		fee.setKindCode(itemKind.getKindCode());// 无论新老案件，直接获取prplcitemkind中的kindcode
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setUnitAmount(itemKind.getAmount());
		fee.setModeCode(itemKind.getModeCode());// 01.司机 02.乘客
		fee.setDeductRate(BigDecimal.valueOf(Datas.round(deductRate,2)));
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		fee.setmKindCode(isMKindCode);
		// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140909,BEGIN
		// if ("050701".equals(itemKind.getKindCode())) {// 司机
		if(CodeConstants.KINDCODE.KINDCODE_D11.equals(itemKind.getKindCode().trim())){// 司机
			// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140909,END
			sumClaimD1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue();
			fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimD1,2)));
			fee.setRescueFee(claimCond.getRescueFee(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS));
			fee.setSumRest(claimCond.getRejectFee(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS));
			// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140909,BEGIN
			// } else if ("050702".equals(itemKind.getKindCode())) {// 乘客
		}else if(CodeConstants.KINDCODE.KINDCODE_D11.equals(itemKind.getKindCode().trim())){// 乘客
			// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140909,END
			sumClaimD1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue();
			fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimD1,2)));
			fee.setRescueFee(claimCond.getRescueFee(CodeConstants.LossFeeType.THIS_PERSON_LOSS));
			fee.setSumRest(claimCond.getRejectFee(CodeConstants.LossFeeType.THIS_PERSON_LOSS));

		}else{// 旧条款 兼容08年的只有D1的条款
			sumClaimD1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue()+claimCond.getSumClaim(
					CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue();
			fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimD1,2)));
			fee.setRescueFee(BigDecimal.valueOf(Datas.round(
					claimCond.getRescueFee(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue()+claimCond.getRescueFee(
							CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue(),2)));
			fee.setSumRest(BigDecimal.valueOf(Datas.round(
					claimCond.getRejectFee(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue()+claimCond.getRejectFee(
							CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue(),2)));
		}
		double estiPaid;
		if(sumClaimD1>fee.getSumAmount().doubleValue()){
			estiPaid = fee.getSumAmount().doubleValue()+fee.getRescueFee().doubleValue()-fee.getSumRest().doubleValue();
		}else{
			estiPaid = fee.getSumClaim().doubleValue()+fee.getRescueFee().doubleValue()-fee.getSumRest().doubleValue();
		}
		if(isMKindCode){
			excpetPay += estiPaid*deductRate/PERCENT;
		}
		estiPaid = estiPaid*claimCond.getIndemnityDutyRate().doubleValue()/PERCENT;
		estiPaid = estiPaid*( 1-deductRate/PERCENT );
		fee.setEstiPaid(BigDecimal.valueOf(Datas.round(estiPaid,2)));
		logger.debug(claimCond.getLossType()+"--"+fee.getKindCode()+"---"+fee.getSumClaim()+"---"+fee.getEstiPaid());
		fees.add(fee);
		return excpetPay;
	}

	// 获取三者类的估损
	private double getSumClaimTypeB(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,Map<String,BigDecimal> needSub,
									boolean isMKindCode,double deductRate,double excpetPay) {
		logger.debug("policyNo:"+claimCond.getPolicyNo()+",registNo:"+claimCond.getRegistNo()+",isMKindCode"+isMKindCode+",excpetPay"+excpetPay);
		double subSumClaimB1 = 0; // 需要扣减的交强险财产估损
		double subSumClaimB2 = 0; // 需要扣减的交强险人伤估损

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		double subSumClaimMedical = 0;// 需要扣减的交强险人伤估损——医疗费
		double subRescueFeeB1 = 0;

		// 为了多保单关联时,商业险能够获得交强险的估损金额，在这调用getSumClaimTypeBZ获取交强类的估损方法。
		String registNo = null;
		if(claimCond!=null&&claimCond.getRegistNo()!=null&&claimCond.getPolicyNo()!=null){
			registNo = claimCond.getRegistNo();
		}

		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		if(prpLCMainVoList==null||prpLCMainVoList.isEmpty()){
			logger.error("此案件无对应险别信息，报案号："+registNo);
			throw new BusinessException("此案件无对应险别信息，报案号："+registNo,false);
		}

		for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
			// 判断除本立案外其他立案是否有BZ险的立案
			if(policyViewService.isInsuredKindCode(prpLCMainVo,CodeConstants.KINDCODE.KINDCODE_BZ)){
				boolean kindcodeB = true;
				for(PrpLCItemKindVo itemKindBZ:prpLCMainVo.getPrpCItemKinds()){
					needSub = getSumClaimTypeBzForB(itemKindBZ,claimCond,prpLCMainVo,kindcodeB);
					break;
				}
			}
		}

		if(needSub.get("sumClaimB1")!=null){
			subSumClaimB1 = needSub.get("sumClaimB1").doubleValue();
		}
		if(needSub.get("sumClaimB2")!=null){
			subSumClaimB2 = needSub.get("sumClaimB2").doubleValue();
		}
		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		if(needSub.get("sumClaimMedical")!=null){
			subSumClaimMedical = needSub.get("sumClaimMedical").doubleValue();
		}
		// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816
		if(needSub.get("rescueFeeB1")!=null){
			subRescueFeeB1 = needSub.get("rescueFeeB1").doubleValue();
		}

		double rescueFeeB1 = claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getRescueFee(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue()-subRescueFeeB1;// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816

		double rescueFeeB2 = claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getRescueFee(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue();

		double rejectFeeB1 = claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getRejectFee(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue();

		double rejectFeeB2 = claimCond.getRejectFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getRejectFee(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue();

		double sumClaimB1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue()-subSumClaimB1;

		double sumClaimB2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue()-subSumClaimB2;

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		double sumClaimMedical = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS_Medical).doubleValue()-subSumClaimMedical;

		ClaimFeeExt fee = new ClaimFeeExt();
		// 取商业三者保额sumAmountB
		double sumAmountB = itemKind.getAmount().doubleValue();
		// double deductRate = getDeductRate(itemKind.getRiskCode(), "B", claimCond.getIndemnityDuty(), claimCond.getLossType(), itemKind.getRegistNo());
		double sumClaimB = 0;
		sumClaimB = ( sumClaimB1+sumClaimB2+sumClaimMedical );
		// 商业无责，估损有值，估计赔款为0
		double sumClaimBall = 0;
		sumClaimBall = ( sumClaimB1+sumClaimB2+sumClaimMedical+rescueFeeB1+rescueFeeB2 )*claimCond.getIndemnityDutyRate().doubleValue()/PERCENT;
		if(sumClaimBall>sumAmountB){
			fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimB,2)));
			fee.setPersonSumClaim(BigDecimal.valueOf(Datas.round(sumClaimMedical,2)));// 记录人伤部分损失
			if(CodeConstants.ClaimType.SPAY_CASE.equals(StringUtils.trimToEmpty(claimCond.getClaimType()))){
				fee.setEstiPaid(BigDecimal.valueOf(0));
				fee.setPersonEstiPaid(BigDecimal.valueOf(0));// 记录人伤部分估计赔款
			}else{
				fee.setEstiPaid(BigDecimal.valueOf(Datas.round(
						sumAmountB*( claimCond.getIndemnityDutyRate().doubleValue()/PERCENT )*( 1-deductRate/PERCENT ),2)));
				fee.setPersonEstiPaid(BigDecimal.valueOf(Datas.round(
						sumAmountB*( claimCond.getIndemnityDutyRate().doubleValue()/PERCENT )*( 1-deductRate/PERCENT )*( sumClaimMedical/sumClaimB ),
						2)));// 记录人伤部分估计赔款
				excpetPay += Datas.round(sumAmountB*( claimCond.getIndemnityDutyRate().doubleValue()/PERCENT )*( deductRate/PERCENT ),2);
			}
		}else{
			fee.setSumClaim(BigDecimal.valueOf(Datas.round(sumClaimB,2)));// 人伤跟踪：加入医疗费sumClaimMedical
			fee.setPersonSumClaim(BigDecimal.valueOf(Datas.round(sumClaimMedical,2)));// 记录人伤部分损失
			if(CodeConstants.ClaimType.SPAY_CASE.equals(StringUtils.trimToEmpty(claimCond.getClaimType()))){
				fee.setEstiPaid(BigDecimal.valueOf(0));
				fee.setPersonEstiPaid(BigDecimal.valueOf(0));// 记录人伤部分估计赔款
			}else{
				// 立案改造：有施救费并估损+施救不大于限额时，估计赔款取估损金额+施救费×责任比例
				fee.setEstiPaid(BigDecimal.valueOf(Datas.round(sumClaimBall*( 1-deductRate/PERCENT ),2)));
				fee.setPersonEstiPaid(BigDecimal.valueOf(Datas.round(sumClaimBall*( 1-deductRate/PERCENT )*( sumClaimMedical/sumClaimB ),2)));// 记录人伤部分估计赔款
				excpetPay += Datas.round(sumClaimBall*( deductRate/PERCENT ),2);
			}
		}

		fee.setKindCode(CodeConstants.KINDCODE.KINDCODE_B);
		fee.setRiskCode(itemKind.getRiskCode());
		fee.setUnitAmount(BigDecimal.valueOf(sumAmountB));
		fee.setItemKindNo(itemKind.getItemKindNo().intValue());
		fee.setSumAmount(itemKind.getAmount());
		fee.setDeductRate(BigDecimal.valueOf(deductRate));
		fee.setRescueFee(BigDecimal.valueOf(Datas.round(rescueFeeB1+rescueFeeB2,2)));
		fee.setSumRest(BigDecimal.valueOf(Datas.round(rejectFeeB1+rejectFeeB2,2)));
		fee.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		logger.debug("fee.SumClaim:"+fee.getSumClaim()+",fee.EstiPaid:"+fee.getEstiPaid()+",fee.IndemnityDutyRate:"+fee.getIndemnityDutyRate());
		fees.add(fee);
		logger.debug("excpetPay:"+excpetPay);
		return excpetPay;
	}

	// 获取交强类的估损——交强商业混保时商业险用到的获取交强估损的方法
	private Map<String,BigDecimal> getSumClaimTypeBzForB(PrpLCItemKindVo itemKind,ClaimFeeCondition claimCond,PrpLCMainVo prpLcMain,boolean kindcodeB) {
		logger.debug("获取交强类的估损——交强商业混保时商业险用到的获取交强估损的方法");
		Map<String,BigDecimal> needSub = new HashMap<String,BigDecimal>(0);// 需要扣减的交强险估损
		double sumClaimB1 = 0.00;
		double sumClaimB2 = 0.00;
		double sumClaimMedical = 0.00;// PNCCAR.人伤跟踪.GAOANXU.2010-08-09.ADD

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816
		double rescueFeeB1 = 0.00;
		double bzPayA = 0.00;
		double bzPayD2 = 0.00;
		double bzPayD101 = 0.00; // 司机
		double bzPayD102 = 0.00; // 乘客

		bzPayA = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_LOSS).doubleValue();
		bzPayD2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_CAR_PROP).doubleValue();
		bzPayD101 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS).doubleValue();
		bzPayD102 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIS_PERSON_LOSS).doubleValue();

		sumClaimB1 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue();

		sumClaimB2 = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_OTH_PERSON).doubleValue()+claimCond.getSumClaim(
				CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS).doubleValue();

		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		sumClaimMedical = claimCond.getSumClaim(CodeConstants.LossFeeType.THIRDPARTY_PERSON_LOSS_Medical).doubleValue();

		rescueFeeB1 = claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_CAR_LOSS).doubleValue()+claimCond.getRescueFee(
				CodeConstants.LossFeeType.THIRDPARTY_CAR_PROP).doubleValue()+claimCond.getRescueFee(CodeConstants.LossFeeType.THIRDPARTY_OTH_PROP)
				.doubleValue();

		double tSumClaimB;
		double tSumClaimAD2;
		double tSumClaimA;
		double tSumClaimD2;
		double tSumClaimD101;
		double tSumClaimD102;

		// 人伤跟踪：交强超限部分金额
		double sumClaimB2Exceed;// 死亡伤残超限额金额
		double sumClaimMedicalExceed;// 医疗费超限额金额

		// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816
		// 由于人伤没有施救费和残值，所以不算人伤的施救和残值。
		double tSumClaimBandRescueFeeB1;
		PrpLRegistVo prpLregist = registQueryService.findByRegistNo(prpLcMain.getRegistNo());
		Date damageTime = prpLregist.getDamageTime();
		// 取交强险保额sumAmountBZ 52 财产
		double sumAmountBZ = Double.valueOf(0);
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.PROPLOSS,claimCond.getCiIndemDuty(),damageTime);
		BigDecimal ciIndemDuty = claimCond.getCiIndemDuty();//责任比例
		Boolean isCiIndemDuty;//是否有责
		if (ciIndemDuty.compareTo(BigDecimal.ZERO) == 1) {
			isCiIndemDuty = true;//有责
		} else {
			isCiIndemDuty = false;//无责
		}
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.PROPLOSS,isCiIndemDuty,prpLregist.getRegistNo());

		tSumClaimBandRescueFeeB1 = sumClaimB1+rescueFeeB1;
		if(tSumClaimBandRescueFeeB1>sumAmountBZ){
			// PNCCAR.人伤跟踪.立案改造：判断较强估损（估损+施救+残值）是否大于限额，如果大于限额，需计算分项。GAOANXU.20100817
			tSumClaimB = sumAmountBZ*( sumClaimB1/( sumClaimB1+rescueFeeB1 ) );
			rescueFeeB1 = sumAmountBZ*( rescueFeeB1/( sumClaimB1+rescueFeeB1 ) );
		}else{
			tSumClaimB = sumClaimB1;
		}
		tSumClaimA = bzPayA;
		tSumClaimD2 = bzPayD2;
		tSumClaimAD2 = bzPayA+bzPayD2;
		if(tSumClaimAD2>sumAmountBZ){
			tSumClaimA = sumAmountBZ*bzPayA/tSumClaimAD2;
			tSumClaimD2 = sumAmountBZ*bzPayD2/tSumClaimAD2;
			tSumClaimAD2 = sumAmountBZ;
		}
		needSub.put("sumClaimB1",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimB1:"+tSumClaimB);
		needSub.put("subPayA",BigDecimal.valueOf(tSumClaimA));
		logger.debug("subPayA:"+tSumClaimA);
		needSub.put("subPayD2",BigDecimal.valueOf(tSumClaimD2));
		logger.debug("subPayD2:"+tSumClaimD2);
		// 交强险没有算施救费和残值,改造：交强险算上施救费和残值。GAOANXU.20100816
		needSub.put("rescueFeeB1",BigDecimal.valueOf(rescueFeeB1));// 因为人伤没有施救费和残值，无需+rescueFeeB2
		logger.debug("rescueFeeB1:"+rescueFeeB1);
		// 取交抢险保额sumAmountBZ 51 人伤——PNCCAR.人伤跟踪：值需要needmap
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.PERSONLOSS,claimCond.getCiIndemDuty(),damageTime);
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.PERSONLOSS,isCiIndemDuty,prpLregist.getRegistNo());
		tSumClaimB = sumClaimB2;
		if(tSumClaimB>sumAmountBZ){
			tSumClaimB = sumAmountBZ;
			sumClaimB2Exceed = sumClaimB2-sumAmountBZ;// 死亡伤残超限额金额
			needSub.put("sumClaimB2Exceed",BigDecimal.valueOf(sumClaimB2Exceed));// 死亡伤残超限额金额
			logger.debug("sumClaimB2Exceed:"+sumClaimB2Exceed);
		}
		tSumClaimD101 = bzPayD101;
		if(tSumClaimD101>sumAmountBZ){
			tSumClaimD101 = sumAmountBZ;
		}
		tSumClaimD102 = bzPayD102;
		if(tSumClaimD102>sumAmountBZ){
			tSumClaimD102 = sumAmountBZ;
		}
		needSub.put("sumClaimB2",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimB2:"+tSumClaimB);
		needSub.put("subPayD101",BigDecimal.valueOf(tSumClaimD101));
		logger.debug("subPayD101:"+tSumClaimD101);
		needSub.put("tSumClaimD101",BigDecimal.valueOf(tSumClaimD102));
		logger.debug("tSumClaimD101:"+tSumClaimD102);
		// 取交抢险保额sumAmountBZ 49,医疗费不自动估损——人上改造后医疗费需要自动估损
		//sumAmountBZ = getBzSumAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,claimCond.getCiIndemDuty(),damageTime);
		sumAmountBZ = configService.calBzAmount(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES,isCiIndemDuty,prpLregist.getRegistNo());
		// 人伤金额拆分：将医药费、续医费、伙食补助费加入到医疗费中
		tSumClaimB = sumClaimMedical;
		if(tSumClaimB>sumAmountBZ){
			tSumClaimB = sumAmountBZ;
			sumClaimMedicalExceed = sumClaimMedical-sumAmountBZ;// 医疗费超限额金额
			// 医疗费超限额金额
			needSub.put("sumClaimMedicalExceed",BigDecimal.valueOf(sumClaimMedicalExceed));
			logger.debug("sumClaimMedicalExceed:"+sumClaimMedicalExceed);
		}
		needSub.put("sumClaimMedical",BigDecimal.valueOf(tSumClaimB));
		logger.debug("sumClaimMedical:"+tSumClaimB);
		return needSub;
	}

	// 按险别汇总已经立案的估损金额
	private double getBzSumClaimByLossFeeType(List<PrpLClaimKindVo> prpLClaimKindVoList,String lossFeeType) {
		double ret = 0;
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			if(StringUtils.trimToEmpty(prpLClaimKindVo.getLossItemNo()).equals(lossFeeType)){
				ret += prpLClaimKindVo.getDefLoss().doubleValue();
			}
		}
		logger.debug("lossFeeType:"+lossFeeType+"按险别汇总已经立案的估损金额"+ret);
		return ret;
	}

	// 按险别汇总已经立案的施救费金额——PNCCAR.人伤跟踪.立案改造.增加施救费.GAOANXU.20100817
	private double getBzRescueFeeByLossFeeType(List<PrpLClaimKindVo> prpLClaimKindVoList,String lossFeeType) {
		double retRescueFee = 0;
		for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
			if(StringUtils.trimToEmpty(prpLClaimKindVo.getLossItemNo()).equals(lossFeeType)){
				retRescueFee += prpLClaimKindVo.getRescueFee().doubleValue();
			}
		}
		logger.debug("lossFeeType:"+lossFeeType+"按险别汇总已经立案的施救费金额"+retRescueFee);
		return retRescueFee;
	}

	// 生成损失项目描述字符串，超长的用...表示
	private String simpleStr(String str,int maxLength) {
		if(str==null){
			return "";
		}
		if(str.length()>=maxLength){
			return str.substring(0,maxLength);
		}
		return str;
	}

	// 根据交强险有责无责和出险日期获取估损(废弃，统一使用ConfigService.calBzAmount)
	private double getBzSumAmount(String damageType,BigDecimal ciIndemDuty,Date damageDate) {
		String key = "";
		if(damageDate==null){
			throw new BusinessException("该立案的的出险日期DamageStartDate,或报案的出险日期DamageDate字段为空,请修改报案和立案信息,或联系数据库管理员！",false);
		}
		if(CodeConstants.DAMAGE_DATE.before(damageDate)){
			key += "N";
		}
		if(ciIndemDuty.compareTo(new BigDecimal("0.00"))==1){
			key += "1";
		}else{
			key += "0";
		}
		return CodeConstants.BZ_LIMIT_AMOUNT.get(key+damageType).doubleValue();
	}

	/**
	 * 对于M险拆分后，每条M险存一条prpLclaimFee记录
	 * @param prpLcItemKind
	 */
	private void addMFee(List<ClaimFeeExt> fees,PrpLCItemKindVo itemKindM,double excpetPay) {
		ClaimFeeExt fee = new ClaimFeeExt();
		fee.setKindCode(itemKindM.getKindCode());
		fee.setRiskCode(itemKindM.getRiskCode());
		fee.setItemKindNo(itemKindM.getItemKindNo().intValue());
		fee.setModeCode(itemKindM.getModeCode());
		fee.setSumClaim(new BigDecimal(excpetPay).setScale(2,BigDecimal.ROUND_HALF_UP));
		fee.setRescueFee(BigDecimal.valueOf(0));
		fee.setEstiPaid(new BigDecimal(excpetPay).setScale(2,BigDecimal.ROUND_HALF_UP));
		fee.setSumAmount(itemKindM.getAmount());
		fee.setUnitAmount(itemKindM.getUnitAmount());
		fee.setSumRest(BigDecimal.valueOf(0));
		fees.add(fee);
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFee(java.lang.String, java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param registNo
	 * @param userCode
	 * @param node
	 */
	@Override
	public void updateClaimFee(String registNo, String userCode, FlowNode node) throws Exception {
		logger.info("报案号registNo={} {} 进入其他环节用来刷新立案估损金额的方法 ", registNo, node == null ? "节点参数为空!" : node.getName());
		List<PrpLCMainVo> prpLCMainVoTempList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
		if(prpLCMainVoTempList == null || prpLCMainVoTempList.size() <= 0){
			logger.info("保单主表无保单信息!报案号为：" + registNo);
			return;
		}

		// 将保单排序，交强险保单在前面
		PrpLCMainVo cMianBI = null;
		PrpLCMainVo cMianCI = null;
		if(prpLCMainVoTempList.size()>1){
			for(PrpLCMainVo prpLCMainVo:prpLCMainVoTempList){
				if("1101".equals(prpLCMainVo.getRiskCode())){
					cMianCI = prpLCMainVo;
				}else{
					cMianBI = prpLCMainVo;
				}
			}
			prpLCMainVoList.add(cMianCI);
			prpLCMainVoList.add(cMianBI);
		}else{
			prpLCMainVoList.add(prpLCMainVoTempList.get(0));
		}

		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		if (prpLRegistVo == null) {
			logger.info("报案表"+registNo+"无数据!");
			throw new BusinessException("报案"+registNo+"无数据!",false);
		}
		for (PrpLCMainVo prpLCMainVo : prpLCMainVoList) {
			ClaimVO claimVO = new ClaimVO();
			claimVO.setPrpLRegistVo(prpLRegistVo);
			claimVO.setPrpLCMainVo(prpLCMainVo);
			claimVO.setPrpLCheckVo(prpLCheckVo);
			PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo, prpLCMainVo.getPolicyNo());// 获取立案数据
			if(prpLClaimVo==null||"0".equals(prpLClaimVo.getValidFlag())){
				// 立案环节还未立案  或 立案节点被注销 不刷新金额
				continue;
			}
//			if (CodeConstants.ClaimFlag.CLAIMFORCE.equals(prpLClaimVo.getClaimFlag())) {
//				continue;
//			}
			claimVO = this.loadClaimRelate(claimVO, registNo, prpLCMainVo.getPolicyNo());
			prpLCheckVo = claimVO.getPrpLCheckVo();
			ClaimFeeCondition claimFeeCondVo = loadSumClaimCondition(prpLRegistVo,prpLCMainVo,prpLCheckVo,prpLClaimVo);
			claimVO.setClaimFeeCond(claimFeeCondVo);// 初始化立案估损的中间结果

			// 初始化立案分险别、损失类别估损金额
			// 新旧立案Service调整 add by chenrong 2014-09-16 begin
//			List<ClaimFeeExt> claimFeeExts = this.getKindLossByCondAndPolicy(prpLcMain, claimVO.getClaimFeeCond());
			List<ClaimFeeExt> claimFeeExts = this.getKindLossByCondAndPolicyForNewClaimRefresh(prpLCMainVo, claimVO.getClaimFeeCond());
			// 新旧立案Service调整 add by chenrong 2014-09-16 end

			List<PrpLClaimKindVo> prpLclaimFees = loadClaimFee(claimFeeExts, "",registNo,node);
			
			//统计B险在原数据表和新计算结果中的记录条数
			Map<String,Integer> KindBSumMap = getKindBSum(prpLClaimVo,prpLclaimFees);
			
			if(prpLclaimFees != null && !prpLclaimFees.isEmpty()){
				for(PrpLClaimKindVo prpLClaimKindVo :prpLclaimFees){
					prpLClaimKindVo.setAdjustReason(node.getName());
					prpLClaimKindVo.setRegistNo(registNo);
					prpLClaimKindVo.setUpdateUser(userCode);
					if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
						for(PrpLClaimKindVo prpLClaimKind : prpLClaimVo.getPrpLClaimKinds()){
							if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKind.getKindCode().trim()) && 
									prpLClaimKindVo.getLossItemNo().trim().equals(prpLClaimKind.getLossItemNo().trim())){
								Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
							}
						}
					}else{
						List<String> kindListofClaimKind = new ArrayList<String>();
						for(PrpLClaimKindVo prpLClaimKind : prpLClaimVo.getPrpLClaimKinds()){
							kindListofClaimKind.add(prpLClaimKind.getKindCode());
						}
						//是否增加一条B险到ClaimVo的标志位
						boolean addKindBFlag = false;
						for(PrpLClaimKindVo prpLClaimKind : prpLClaimVo.getPrpLClaimKinds()){
							if(FlowNode.Check.getName().equals(node.getName())){
								// 如果当前刷立案的环节为查勘，不刷新人伤相关的险别的数据（查勘不组织人伤数据，刷新会将强制立案的人伤金额覆盖成0）
								if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKind.getKindCode())
										||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKind.getKindCode())){
									prpLClaimKind.setAdjustReason(node.getName());
									continue;
								}
								//TODO 查勘刷立案对B险有保额管控，是否还是刷人伤？检查loadclaimExt方法
								if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKind.getKindCode())
										&&"人".equals(prpLClaimKind.getLossItemName())){
									prpLClaimKind.setAdjustReason(node.getName());
									continue;
								}
							}

							if((prpLClaimKind.getKindCode().trim()).equals(prpLClaimKindVo.getKindCode().trim())){
								if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())){
									prpLClaimKind.setAdjustReason(node.getName());
									//如果claimKindList中有两条B险记录，则进行特殊处理
									if(KindBSumMap.get("claimKindListSum")==2){
										if(KindBSumMap.get("claimSum")==1){
											//claimVo中有1条记录，存在的那一条重新赋值金额，新增的那条增加到claimVo的子表中
											if(prpLClaimKindVo.getLossItemName().equals(prpLClaimKind.getLossItemName())){
												prpLClaimKind.setDefLoss(prpLClaimKindVo.getDefLoss());// 估损金额
												prpLClaimKind.setRescueFee(prpLClaimKindVo.getRescueFee());// 人伤无施救费
												prpLClaimKind.setClaimLoss(prpLClaimKindVo.getClaimLoss());// 估计赔款
												prpLClaimKind.setCarLossRate(prpLClaimKindVo.getCarLossRate());;// 车损占比
											}else{
												//新增的人伤记录加到claimVo子表中，由于不能在遍历List时改变List，所以添加一个标志位在循环结束后判断是否添加新Vo
												addKindBFlag = true;
											}
										}else if(KindBSumMap.get("claimSum")==2){
											//claimVo中有2条记录,对比lossItemName赋值
											if(prpLClaimKindVo.getLossItemName().equals(prpLClaimKind.getLossItemName())){
												prpLClaimKind.setDefLoss(prpLClaimKindVo.getDefLoss());// 估损金额
												prpLClaimKind.setRescueFee(prpLClaimKindVo.getRescueFee());// 人伤无施救费
												prpLClaimKind.setClaimLoss(prpLClaimKindVo.getClaimLoss());// 估计赔款
												prpLClaimKind.setCarLossRate(prpLClaimKindVo.getCarLossRate());;// 车损占比
											}
										}
									}else{
										// 是否在此考虑如果原来claimVo中有人和车物，但是人伤修改定损或者车物  0提了 这个时候人伤数据刷成全0
										if(KindBSumMap.get("claimKindListSum")==1 && KindBSumMap.get("claimSum")==2){
											if(prpLClaimKindVo.getLossItemName().equals(prpLClaimKind.getLossItemName())){
												prpLClaimKind.setDefLoss(prpLClaimKindVo.getDefLoss());// 估损金额
												prpLClaimKind.setRescueFee(prpLClaimKindVo.getRescueFee());// 人伤无施救费
												prpLClaimKind.setClaimLoss(prpLClaimKindVo.getClaimLoss());// 估计赔款
												prpLClaimKind.setCarLossRate(prpLClaimKindVo.getCarLossRate());;// 车损占比
											}else{
												prpLClaimKind.setDefLoss(BigDecimal.ZERO);// 估损金额
												prpLClaimKind.setRescueFee(BigDecimal.ZERO);// 人伤无施救费
												prpLClaimKind.setClaimLoss(BigDecimal.ZERO);// 估计赔款
												prpLClaimKind.setCarLossRate(prpLClaimKindVo.getCarLossRate());;// 车损占比
											}
										}else{
											Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
										}
									}
								}else{
									Datas.copySimpleObjectToTargetFromSource(prpLClaimKind,prpLClaimKindVo,false);
								}
							}
						}
						if(addKindBFlag){
							//将新增B险记录添加到claimVo子表
							List<PrpLClaimKindVo> claimKindListTemp = prpLClaimVo.getPrpLClaimKinds();
							claimKindListTemp.add(prpLClaimKindVo);
							prpLClaimVo.setPrpLClaimKinds(claimKindListTemp);
							KindBSumMap.put("claimSum",KindBSumMap.get("claimSum")+1);
						}
					}
				}
			}
			
			// 判断如果当前环节不是人伤刷新，人伤估损及赔款取上一次人伤审核轨迹的金额，如果没有，就直接刷0;
			List<PrpLClaimKindHisVo> pLossKindHisNewestVoList = null;// 人伤刷新的最新一次立案轨迹 
			if(!FlowNode.PLoss.equals(node.getRootNode())){
				pLossKindHisNewestVoList = claimService.findPLossKindHisNewest(registNo);
			}
			for(PrpLClaimKindVo prpLClaimKindVo : prpLClaimVo.getPrpLClaimKinds()){
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKindVo.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKindVo.getKindCode())){
					// 判断如果当前环节不是人伤刷新，取上一次人伤审核轨迹的金额，如果没有，就直接刷0;
					if(!FlowNode.PLoss.equals(node.getRootNode())&&pLossKindHisNewestVoList!=null&&pLossKindHisNewestVoList.size()>0){
						for(PrpLClaimKindHisVo claimKindHisPLoss : pLossKindHisNewestVoList){
							if(claimKindHisPLoss.getKindCode().equals(prpLClaimKindVo.getKindCode())
									&&claimKindHisPLoss.getLossItemName().equals(prpLClaimKindVo.getLossItemName())
									&&!prpLClaimKindVo.getLossItemName().equals("车财损失")
									&&!prpLClaimKindVo.getLossItemName().equals("车物")){
						
								prpLClaimKindVo.setDefLoss(claimKindHisPLoss.getDefLoss());
								prpLClaimKindVo.setClaimLoss(claimKindHisPLoss.getClaimLoss());
							}
						}
					}
				}
			}
			
			// 根据险别汇总核损或查勘的费用数据
			List<PrpLClaimKindFeeVo> claimKindFeeList = updateClaimFeeAmt(prpLClaimVo,registNo,userCode,node);

			//将费用数据赋值到claimVo的子表
			if(claimKindFeeList != null && !claimKindFeeList.isEmpty()){
				//获取原数据中已存在的费用险别信息
				List<String> kindFeeList = new ArrayList<String>();
				for(PrpLClaimKindFeeVo claimKindFeeVo:prpLClaimVo.getPrpLClaimKindFees()){
					kindFeeList.add(claimKindFeeVo.getKindCode()+claimKindFeeVo.getFeeCode());
				}
				List<String> newKindFeeList = new ArrayList<String>();
				List<PrpLClaimKindFeeVo> claimKindFeeListTemp = new ArrayList<PrpLClaimKindFeeVo>();//用于新增和删除费用记录的临时List
				for(PrpLClaimKindFeeVo prpLClaimKindFeeVo :claimKindFeeList){
					// 新汇总的费用记录
					newKindFeeList.add(prpLClaimKindFeeVo.getKindCode()+prpLClaimKindFeeVo.getFeeCode());
					
					prpLClaimKindFeeVo.setAdjustReason(node.getName());
					prpLClaimKindFeeVo.setUpdateUser(userCode);
					for(PrpLClaimKindFeeVo claimKindFeeVo:prpLClaimVo.getPrpLClaimKindFees()){
						claimKindFeeVo.setAdjustReason(node.getName());
						if((prpLClaimKindFeeVo.getKindCode().trim()).equals(claimKindFeeVo.getKindCode().trim())
								&&(prpLClaimKindFeeVo.getFeeCode().trim()).equals(claimKindFeeVo.getFeeCode().trim())){
							Datas.copySimpleObjectToTargetFromSource(claimKindFeeVo,prpLClaimKindFeeVo,false);
						}
						
					}
					if(!kindFeeList.contains(prpLClaimKindFeeVo.getKindCode()+prpLClaimKindFeeVo.getFeeCode())){
						//属于新增的险别费用信息
						claimKindFeeListTemp = prpLClaimVo.getPrpLClaimKindFees();
						claimKindFeeListTemp.add(prpLClaimKindFeeVo);
						prpLClaimVo.setPrpLClaimKindFees(claimKindFeeListTemp);
					}
					
				}
				
				//处理删除掉的费用记录
				claimKindFeeListTemp = new ArrayList<PrpLClaimKindFeeVo>();
				for(PrpLClaimKindFeeVo claimKindFee:prpLClaimVo.getPrpLClaimKindFees()){
					if(newKindFeeList.contains(claimKindFee.getKindCode()+claimKindFee.getFeeCode())){
						claimKindFeeListTemp.add(claimKindFee);
					}
				}
				prpLClaimVo.setPrpLClaimKindFees(claimKindFeeListTemp);
				
			}else{
				// 此时费用列表为空
				prpLClaimVo.setPrpLClaimKindFees(claimKindFeeList);
			}
			
			if (prpLClaimVo.getEstiTimes() == null) {
				logger.info("报案号=" + registNo + " 立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!");
				throw new BusinessException("立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!",false);
			}

			prpLClaimVo.setSumDefLoss(this.getSumDefLoss(prpLClaimVo));// 刷新车辆、财产、人伤的损失金额
			prpLClaimVo.setSumClaim(this.getSumClaim(prpLClaimVo));// 刷新车辆、财产、人伤的估计赔款
			//更新立案标志位
			prpLClaimVo.setCiIndemDuty(claimFeeCondVo.getCiDutyFlag());
			prpLClaimVo.setIndemnityDuty(claimFeeCondVo.getIndemnityDuty());
			prpLClaimVo.setIndemnityDutyRate(claimFeeCondVo.getIndemnityDutyRate());
			prpLClaimVo.setCaseFlag(claimFeeCondVo.getClaimSlefFlag());
			prpLClaimVo.setIsSubRogation(claimFeeCondVo.getSubRogationFlag());
			prpLClaimVo.setIsTotalLoss(claimFeeCondVo.getTotalLossFlag());
			
			prpLClaimVo.setUpdateUser(userCode);
			prpLClaimVo.setUpdateTime(new Date());
			prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);// 估损次数增加
			updateClaimFeeByClaim(prpLClaimVo,prpLClaimVo.getPrpLClaimKinds(),prpLClaimVo.getPrpLClaimKindFees());// 主子表关联

			logger.info("报案号："+prpLClaimVo.getRegistNo()+ "更新完立案金额，" + "保单号："+prpLClaimVo.getPolicyNo()+
					"prpLclaim.getClaimNo():"+prpLClaimVo.getClaimNo()+",prpLclaim.getSumDefLoss():"+prpLClaimVo.getSumDefLoss()+
					",prpLclaim.getSumClaim():"+prpLClaimVo.getSumClaim());


			this.saveOrUpdatePrpLClaimVo(prpLClaimVo);
			// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
			claimKindHisService.saveClaimKindHis(prpLClaimVo);
			
			//立案刷新送再保 立案送再保未决数据分摊试算   niuqiang
			try{
				List<PrpLClaimKindHisVo> kindHisVoList = 
				claimKindHisService.findKindHisVoList(prpLClaimVo.getClaimNo(), "1");
				ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
				claimInterfaceLogVo.setClaimNo(prpLClaimVo.getClaimNo());
				claimInterfaceLogVo.setRegistNo(registNo);
				claimInterfaceLogVo.setComCode(prpLClaimVo.getComCode());
				claimInterfaceLogVo.setCreateUser(userCode);
				claimInterfaceLogVo.setCreateTime(new Date());
				claimInterfaceLogVo.setOperateNode(FlowNode.Claim.getName());
				if (kindHisVoList != null && kindHisVoList.size() > 0) {
					interfaceAsyncService.TransDataForClaimVo(prpLClaimVo,
							kindHisVoList,claimInterfaceLogVo);
			}
			}catch(Exception e){
				logger.error("报案号=" +registNo + "立案送再保未决数据分摊试算失败",e);
				//throw new IllegalArgumentException("立案送再保未决数据分摊试算！"+e);
			}	
		}
		logger.info("报案号registNo={}，结束其他环节用来刷新立案估损金额的方法 ",registNo);
	}
	/**
	 * 累加立案子表中的估计赔款
	 * <pre></pre>
	 * @param claimVo
	 * @return
	 * @modified:
	 * ☆Weilanlei(2016年7月12日 下午7:43:35): <br>
	 */
	private BigDecimal getSumClaim(PrpLClaimVo claimVo){
		BigDecimal sumClaim = BigDecimal.ZERO;
		//赔款的估计赔款累加
		for(PrpLClaimKindVo claimKind:claimVo.getPrpLClaimKinds()){
			if("1101".equals(claimVo.getRiskCode())){
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(claimKind.getKindCode())){
					sumClaim = sumClaim.add(claimKind.getClaimLoss());
				}
			}else{
				if(!CodeConstants.KINDCODE.KINDCODE_BZ.equals(claimKind.getKindCode())){
					sumClaim = sumClaim.add(claimKind.getClaimLoss());
				}
			}
		}
		/*//费用的估计赔款累加
				for(PrpLClaimKindFeeVo claimKindFee:claimVo.getPrpLClaimKindFees()){
					if(claimKindFee.getRiskCode().equals(claimVo.getRiskCode())){
						sumClaim = sumClaim.add(claimKindFee.getPayFee());
					}
				}*/
		logger.debug("立案刷新估计赔款sumClaim："+sumClaim);
		return sumClaim;
	}
	
	/**
	 * 统计claimVo子表ClaimKind中B险记录条数和传过来的claimKindList中B险条数
	 * 作为拆分B险后特殊处理的参考
	 * <pre></pre>
	 * @param claimVo
	 * @param claimKindVoList
	 * @return
	 * @modified:
	 * ☆WLL(2016年7月8日 上午10:14:04): <br>
	 */
	private Map<String,Integer> getKindBSum(PrpLClaimVo claimVo,List<PrpLClaimKindVo> claimKindVoList){
		Map<String,Integer> KindBSumMap = new HashMap<String,Integer>();
		int claimSum = 0;//claimVo中子表ClaimKind中B险记录数
		int claimKindListSum = 0;//claimKindList中B险条数
		for(PrpLClaimKindVo claimClaimKind:claimVo.getPrpLClaimKinds()){
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimClaimKind.getKindCode())){
				claimSum++;
			}
		}
		for(PrpLClaimKindVo claimKind:claimKindVoList){
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKind.getKindCode())){
				claimKindListSum++;
			}
		}
		KindBSumMap.put("claimSum",claimSum);
		KindBSumMap.put("claimKindListSum",claimKindListSum);
		return KindBSumMap;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimAfterCompe(java.lang.String, java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param compensateNo
	 * @param userCode
	 * @param node
	 */
	@Override
	public void updateClaimAfterCompe(String compensateNo,String userCode,FlowNode node) {
		// 获取理算主表，区分理算是商业理算还是交强
		PrpLCompensateVo compeVo = compensateService.findCompByPK(compensateNo);
		List<PrpLLossItemVo> PrpLLossItemVoList = compeVo.getPrpLLossItems();
		List<PrpLLossPropVo> PrpLLossPropVoList = compeVo.getPrpLLossProps();
		List<PrpLLossPersonVo> PrpLLossPersonVoList = compeVo.getPrpLLossPersons();
		List<ClaimKindFreshVo> claimKindFreshVoList = new ArrayList<ClaimKindFreshVo>();

		// 交强理算---获取理算车财人表，拆分成车财损失，医疗费用和死亡伤残三类的定损金额，施救费，实际赔偿金额
		if("1101".equals(compeVo.getRiskCode())){
			ClaimKindFreshVo carClaimKind = new ClaimKindFreshVo();
			ClaimKindFreshVo mediClaimKind = new ClaimKindFreshVo();
			ClaimKindFreshVo deathClaimKind = new ClaimKindFreshVo();
			
			carClaimKind.setItemName(CodeConstants.FeeTypeCode.PROPLOSS);
			for(PrpLLossItemVo lossItem : PrpLLossItemVoList){
				carClaimKind.setDefLoss(carClaimKind.getDefLoss().add(lossItem.getSumLoss()));
				carClaimKind.setClaimLoss(carClaimKind.getClaimLoss().add(lossItem.getSumRealPay()));
				if(lossItem.getRescueFee()!=null){
					carClaimKind.setRescueFee(carClaimKind.getRescueFee().add(lossItem.getRescueFee()));
				}
			}
			for(PrpLLossPropVo lossProp:PrpLLossPropVoList){
				carClaimKind.setDefLoss(carClaimKind.getDefLoss().add(lossProp.getSumLoss()));
				carClaimKind.setClaimLoss(carClaimKind.getClaimLoss().add(lossProp.getSumRealPay()));
				if(lossProp.getRescueFee()!=null){
					carClaimKind.setRescueFee(carClaimKind.getRescueFee().add(lossProp.getRescueFee()));
				}
			}
			mediClaimKind.setItemName(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
			deathClaimKind.setItemName(CodeConstants.FeeTypeCode.PERSONLOSS);
			for(PrpLLossPersonVo lossPerson:PrpLLossPersonVoList){
				for(PrpLLossPersonFeeVo lossPersonFee:lossPerson.getPrpLLossPersonFees()){
					if(CodeConstants.LossTypePersComp.PERSON_LOSS_MEDICAL.equals(lossPersonFee.getLossItemNo())){
						mediClaimKind.setDefLoss(mediClaimKind.getDefLoss().add(lossPersonFee.getFeeLoss()));
						mediClaimKind.setClaimLoss(mediClaimKind.getClaimLoss().add(lossPersonFee.getFeeRealPay()));
					}
					if(CodeConstants.LossTypePersComp.PERSON_LOSS_DEATHDIS.equals(lossPersonFee.getLossItemNo())){
						deathClaimKind.setDefLoss(deathClaimKind.getDefLoss().add(lossPersonFee.getFeeLoss()));
						deathClaimKind.setClaimLoss(deathClaimKind.getClaimLoss().add(lossPersonFee.getFeeRealPay()));
					}
				}

			}
			claimKindFreshVoList.add(carClaimKind);
			claimKindFreshVoList.add(mediClaimKind);
			claimKindFreshVoList.add(deathClaimKind);
			logger.debug("理算核赔通过刷新立案carClaimKind："+carClaimKind.getClaimLoss());
			logger.debug("理算核赔通过刷新立案mediClaimKind："+mediClaimKind.getClaimLoss());
			logger.debug("理算核赔通过刷新立案deathClaimKind："+deathClaimKind.getClaimLoss());
		}else{
			// 商业理算---获取理算车财人表，根据险别拆分出该险别的定损金额，施救费，实际赔偿金额
			logger.debug("商业理算核赔通过刷新立案");
			Map<String,ClaimKindFreshVo> kindAmtMap = new HashMap<String,ClaimKindFreshVo>();
			for(PrpLLossItemVo item:PrpLLossItemVoList){
				String kindCode = item.getKindCode();
				// 对B险需要特殊处理，分为车财和人伤
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(kindCode)){
					kindCode = kindCode+"Loss";//车辆损失为BLoss，人伤损失为BPers
				}
				if(kindAmtMap==null||kindAmtMap.size()==0){
					// 如果Map为空，直接添加新的元素到Map中
					ClaimKindFreshVo orgClaimKind = createNewClaimKindVo(kindCode,item.getSumLoss(),item.getRescueFee(),
							item.getSumRealPay());
					kindAmtMap.put(kindCode,orgClaimKind);
				}else{
					if(kindAmtMap.containsKey(kindCode)){
						// 如果Map中包含该险别，则累加金额
						ClaimKindFreshVo updClaimKind = kindAmtMap.get(kindCode);
						updClaimKind.setDefLoss(updClaimKind.getDefLoss().add(item.getSumLoss()));
						updClaimKind.setClaimLoss(updClaimKind.getClaimLoss().add(item.getSumRealPay()));
						if(item.getRescueFee()!=null){
							updClaimKind.setRescueFee(updClaimKind.getRescueFee().add(item.getRescueFee()));
						}
						kindAmtMap.put(kindCode,updClaimKind);
					}else{
						// 如果Map中不包含该险别，则新加元素
						ClaimKindFreshVo newClaimKind = createNewClaimKindVo(kindCode,item.getSumLoss(),item.getRescueFee(),
								item.getSumRealPay());
						kindAmtMap.put(kindCode,newClaimKind);
					}
				}
			}
			
			for(PrpLLossPropVo prop:PrpLLossPropVoList){
				String kindCode = prop.getKindCode();
				// 对B险需要特殊处理，分为车财和人伤
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(kindCode)){
					kindCode = kindCode+"Loss";//车财损失为BLoss，人伤损失为BPers
				}
				if(kindAmtMap==null||kindAmtMap.size()==0){
					// 如果Map为空，直接添加新的元素到Map中
					ClaimKindFreshVo orgClaimKind = createNewClaimKindVo(kindCode,prop.getSumLoss(),prop.getRescueFee(),
							prop.getSumRealPay());
					kindAmtMap.put(kindCode,orgClaimKind);
				}else{
					if(kindAmtMap.containsKey(kindCode)){
						// 如果Map中包含该险别，则累加金额
						ClaimKindFreshVo updClaimKind = kindAmtMap.get(kindCode);
						updClaimKind.setDefLoss(updClaimKind.getDefLoss().add(prop.getSumLoss()));
						updClaimKind.setClaimLoss(updClaimKind.getClaimLoss().add(prop.getSumRealPay()));
						if(prop.getRescueFee()!=null){
							updClaimKind.setRescueFee(updClaimKind.getRescueFee().add(prop.getRescueFee()));
						}
						kindAmtMap.put(kindCode,updClaimKind);
					}else{
						// 如果Map中不包含该险别，则新加元素
						ClaimKindFreshVo newClaimKind = createNewClaimKindVo(kindCode,prop.getSumLoss(),prop.getRescueFee(),
								prop.getSumRealPay());
						kindAmtMap.put(kindCode,newClaimKind);
					}
				}
			}
			
			for(PrpLLossPersonVo person:PrpLLossPersonVoList){
				String kindCode = person.getKindCode();
				// 对B险需要特殊处理，分为车财和人伤
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(kindCode)){
					kindCode = kindCode+"Pers";//车财损失为BLoss，人伤损失为BPers
				}
				if(kindAmtMap==null||kindAmtMap.size()==0){
					// 如果Map为空，直接添加新的元素到Map中
					ClaimKindFreshVo orgClaimKind = createNewClaimKindVo(kindCode,person.getSumLoss(),BigDecimal.ZERO,
							person.getSumRealPay());
					kindAmtMap.put(kindCode,orgClaimKind);
				}else{
					if(kindAmtMap.containsKey(kindCode)){
						// 如果Map中包含该险别，则累加金额
						ClaimKindFreshVo updClaimKind = kindAmtMap.get(kindCode);
						updClaimKind.setDefLoss(updClaimKind.getDefLoss().add(person.getSumLoss()));
						updClaimKind.setClaimLoss(updClaimKind.getClaimLoss().add(person.getSumRealPay()));
						// 人伤没有施救费
						kindAmtMap.put(kindCode,updClaimKind);
					}else{
						// 如果Map中不包含该险别，则新加元素
						ClaimKindFreshVo newClaimKind = createNewClaimKindVo(kindCode,person.getSumLoss(),BigDecimal.ZERO,
								person.getSumRealPay());
						kindAmtMap.put(kindCode,newClaimKind);
					}
				}
			}
			// 遍历map将vo添加到volist中
			for(ClaimKindFreshVo claimKindFre:kindAmtMap.values()){
				claimKindFreshVoList.add(claimKindFre);
			}
		}
		// 获取立案主表
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(compeVo.getClaimNo());
		// 把理算表整合的数据刷新到立案claimKind表的字段
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>();
		
		// 获取车损占比字段值
		CompensateVo compensateVo = new CompensateVo();
		compensateVo.setPrpLLossItemVoList(compeVo.getPrpLLossItems());
		compensateVo.setPrpLLossPropVoList(compeVo.getPrpLLossProps());
		BigDecimal carLossRate = this.getCarLossRate(compensateVo);
		
		for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
			claimKindVo.setAdjustReason(node.getName());
			for(ClaimKindFreshVo ckFreshVo:claimKindFreshVoList){
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())){
					// B险需要对人和车物做特殊处理
					if(ckFreshVo.getItemName().startsWith("B")){
						if("Loss".equals(ckFreshVo.getItemName().substring(1))
								&&"车物".equals(claimKindVo.getLossItemName())){
							claimKindVo.setDefLoss(ckFreshVo.getDefLoss());
							claimKindVo.setClaimLoss(ckFreshVo.getClaimLoss());
							claimKindVo.setRescueFee(ckFreshVo.getRescueFee());
						}
						if("Pers".equals(ckFreshVo.getItemName().substring(1))
								&&"人".equals(claimKindVo.getLossItemName())){
							claimKindVo.setDefLoss(ckFreshVo.getDefLoss());
							claimKindVo.setClaimLoss(ckFreshVo.getClaimLoss());
							claimKindVo.setRescueFee(ckFreshVo.getRescueFee());
						}
					}
				}else{
					if(ckFreshVo.getItemName().equals(claimKindVo.getLossItemNo())
							||claimKindVo.getKindCode().equals(ckFreshVo.getItemName())){
						claimKindVo.setDefLoss(ckFreshVo.getDefLoss());
						claimKindVo.setClaimLoss(ckFreshVo.getClaimLoss());
						claimKindVo.setRescueFee(ckFreshVo.getRescueFee());
					}
				}
				
			}
			if((CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())
					&&"车物".equals(claimKindVo.getLossItemName()))
					||(CodeConstants.KINDCODE.KINDCODE_BZ.equals(claimKindVo.getKindCode())
							&&CodeConstants.FeeTypeCode.PROPLOSS.equals(claimKindVo.getLossItemNo()))){
				// 在车财合并计算的险别中赋值车辆损失比例字段   方便保监提数区分车财
				logger.debug(claimKindVo.getKindCode()+"车辆损失比例==Comp=="+carLossRate);
				claimKindVo.setCarLossRate(carLossRate);
				
			}
			prpLClaimKindVoList.add(claimKindVo);
		}
		List<PrpLClaimKindFeeVo> claimKindFeeList = updateClaimFeeAmtAfterComp(prpLClaimVo,compeVo,compeVo.getRegistNo(),userCode,node);
		// 更新立案主子表
		if(prpLClaimVo.getEstiTimes()==null){
			logger.error("立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!");
			throw new BusinessException("立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!",false);
		}

		if(compeVo.getSumLoss()!=null){
			prpLClaimVo.setSumDefLoss(compeVo.getSumLoss());// 刷新车辆、财产、人伤的损失金额
		}
		//立案主表字段更新
		prpLClaimVo.setSumClaim(DataUtils.NullToZero(compeVo.getSumPaidAmt()));
		prpLClaimVo.setUpdateUser(userCode);
		prpLClaimVo.setUpdateTime(new Date());
		prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);// 估损次数增加
		prpLClaimVo = updateClaimFeeByClaim(prpLClaimVo,prpLClaimKindVoList,claimKindFeeList);// 主子表关联
		logger.debug("报案号："+prpLClaimVo.getRegistNo()+"保单号："+prpLClaimVo.getPolicyNo()+"prpLclaim.getClaimNo():"+prpLClaimVo.getClaimNo()+",prpLclaim.getSumDefLoss():"+prpLClaimVo
				.getSumDefLoss()+",prpLclaim.getSumClaim():"+prpLClaimVo.getSumClaim());
		this.saveOrUpdatePrpLClaimVo(prpLClaimVo);

		// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
		claimKindHisService.saveClaimKindHis(prpLClaimVo);
	}
	/**
	 * 根据车财损失计算车损占比字段值carLossRate
	 * <pre></pre>
	 * @param compensateVo 组织汇总了车损和财损数据的Vo
	 * @return
	 * @modified:
	 * ☆WLL(2017年1月13日 下午5:25:47): <br>
	 */
	@Override
	public BigDecimal getCarLossRate(CompensateVo compensateVo){
		/**
		 *  根据车财损失计算车损占比字段值carLossRate
		 */
		BigDecimal sumPropLoss = BigDecimal.ZERO; // 财损总金额
		BigDecimal sumCarLoss = BigDecimal.ZERO; // 车损总金额
		BigDecimal carLossRate = BigDecimal.ZERO; // 车损在车财中占比
		
		if(compensateVo.getPrpLLossItemVoList()!=null&&compensateVo.getPrpLLossItemVoList().size()>0){
			for(PrpLLossItemVo lossItemVo:compensateVo.getPrpLLossItemVoList()){
				if(!CodeConstants.LossParty.TARGET.equals(lossItemVo.getItemId())){
					sumCarLoss = sumCarLoss.add(DataUtils.NullToZero(lossItemVo.getSumRealPay()).add(DataUtils.NullToZero(lossItemVo.getRescueFee())));
				}
			}
		}
		if(compensateVo.getPrpLLossPropVoList()!=null&&compensateVo.getPrpLLossPropVoList().size()>0){
			for(PrpLLossPropVo lossPropVo:compensateVo.getPrpLLossPropVoList()){
				if(!CodeConstants.LossParty.TARGET.equals(lossPropVo.getItemId())&&!"9".equals(lossPropVo.getPropType())){
					sumPropLoss = sumPropLoss.add(DataUtils.NullToZero(lossPropVo.getSumRealPay()).add(DataUtils.NullToZero(lossPropVo.getRescueFee())));
				}
			}
		}
		if(BigDecimal.ZERO.compareTo(sumCarLoss)==-1){ 
			carLossRate = sumCarLoss.divide(sumCarLoss.add(sumPropLoss),2,BigDecimal.ROUND_HALF_UP);
			logger.debug("计算车损占比：车损总金额="+sumCarLoss+";物损总金额="+sumPropLoss+"占比="+carLossRate);
		}
		return carLossRate;
	}

	/**
	 * 判断费用险别是否为保单所对应险别
	 * <pre></pre>
	 * @param chargeVo
	 * @param claimVo
	 * @return
	 * @modified:
	 * ☆WLL(2016年7月22日 下午4:40:04): <br>
	 */
	private boolean adjustChargeKind(PrpLDlossChargeVo chargeVo,String riskCode){
		if("1101".equals(riskCode)){
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(chargeVo.getKindCode())){
				return true;
			}else{
				return false;
			}
		}else{
			if(!CodeConstants.KINDCODE.KINDCODE_BZ.equals(chargeVo.getKindCode())){
				return true;
			}else{
				return false;
			}
		}
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#adjustUnderWriDeductForCharge(ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo, java.lang.String)
	 * @param chargeVo
	 * @param riskCode
	 * @return
	 */
	@Override
	public boolean adjustUnderWriDeductForCharge(PrpLDlossChargeVo chargeVo,String riskCode){
		String bussType = chargeVo.getBusinessType();
		if("DLProp".equals(bussType)){
			PrpLdlossPropMainVo propMainVo = propTaskService.findPropMainVoById(chargeVo.getBusinessId());
			if("1101".equals(riskCode)){
				if(propMainVo.getLossState().startsWith("0")
						&&(CodeConstants.VeriFlag.PASS.equals(propMainVo.getUnderWriteFlag())
								||CodeConstants.VeriFlag.AUTOPASS.equals(propMainVo.getUnderWriteFlag()))){
					return true;
				}
			}else{
				if(propMainVo.getLossState().endsWith("0")
						&&(CodeConstants.VeriFlag.PASS.equals(propMainVo.getUnderWriteFlag())
								||CodeConstants.VeriFlag.AUTOPASS.equals(propMainVo.getUnderWriteFlag()))){
					return true;
				}
			}
		}
		if("DLCar".equals(bussType)){
			PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(chargeVo.getBusinessId());
			if("1101".equals(riskCode)){
				if(carMainVo.getLossState().startsWith("0")
						&&(CodeConstants.VeriFlag.PASS.equals(carMainVo.getUnderWriteFlag())
								||CodeConstants.VeriFlag.AUTOPASS.equals(carMainVo.getUnderWriteFlag()))){
					return true;
				}
			}else{
				if(carMainVo.getLossState().endsWith("0")
						&&(CodeConstants.VeriFlag.PASS.equals(carMainVo.getUnderWriteFlag())
								||CodeConstants.VeriFlag.AUTOPASS.equals(carMainVo.getUnderWriteFlag()))){
					return true;
				}
			}
		}
		if("PLoss".equals(bussType)){
			PrpLDlossPersTraceMainVo persMainVo = persTraceDubboService.findPersTraceMainByPk(chargeVo.getBusinessId());
			if("1101".equals(riskCode)){
				if(persMainVo.getLossState().startsWith("0")
						&&(CodeConstants.PersVeriFlag.VERIFYPASS.equals(persMainVo.getUnderwriteFlag())
							||CodeConstants.PersVeriFlag.AUTOPASS.equals(persMainVo.getUnderwriteFlag())
							||CodeConstants.PersVeriFlag.CHARGEPASS.equals(persMainVo.getUnderwriteFlag()))){
					return true;
				}
			}else{
				if(persMainVo.getLossState().endsWith("0")
						&&(CodeConstants.PersVeriFlag.VERIFYPASS.equals(persMainVo.getUnderwriteFlag())
								||CodeConstants.PersVeriFlag.AUTOPASS.equals(persMainVo.getUnderwriteFlag())
								||CodeConstants.PersVeriFlag.CHARGEPASS.equals(persMainVo.getUnderwriteFlag()))){
					return true;
				}
			}
		}
		return false;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeAmt(ins.sino.claimcar.claim.vo.PrpLClaimVo, java.lang.String, java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param claimVo
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @return
	 */
	@Override
	public List<PrpLClaimKindFeeVo> updateClaimFeeAmt(PrpLClaimVo claimVo,String registNo,String userCode,FlowNode node) {
		// 定核损和查勘之后刷新立案费用数据的方法---立案费用刷新和插入按照险别分类

		// 核损、查勘的立案费用为插入操作（人伤只刷最后一次核损）
		List<PrpLDlossChargeVo> dlossChargeVoList = lossChargeService.findLossChargeVoByRegistNo(registNo);

		// 费用按照险别和费用名称 string 的格式为kindCode+ChargeCode
		Map<String,PrpLClaimKindFeeVo> kindAndFeeMap = new HashMap<String,PrpLClaimKindFeeVo>();
		List<PrpLClaimKindFeeVo> claimKindFeeList = new ArrayList<PrpLClaimKindFeeVo>();

				for(PrpLDlossChargeVo chargeVo:dlossChargeVoList){
					if(chargeVo.getVeriChargeFee()==null&&!"Check".equals(chargeVo.getBusinessType())){
						// 费用未审核通过不刷新立案
						continue;
					}

					boolean isNotUnderWriOrDeduct = this.adjustUnderWriDeductForCharge(chargeVo,claimVo.getRiskCode());
					// 判断该条费用是否已经理算扣减 或核损未通过  是则不组织
					if(!isNotUnderWriOrDeduct){
						continue;
					}
					if(adjustChargeKind(chargeVo,claimVo.getRiskCode())){
						
						String mapKey = chargeVo.getKindCode()+chargeVo.getChargeCode();
						
						if(kindAndFeeMap==null||kindAndFeeMap.size()==0){
							// 如果Map为空，直接添加新的元素到Map中
							BigDecimal chargeFee = BigDecimal.ZERO;
							if("Check".equals(chargeVo.getBusinessType())){
								chargeFee = DataUtils.NullToZero(chargeVo.getChargeFee());
							}else{
								chargeFee = chargeVo.getVeriChargeFee();
							}
							PrpLClaimKindFeeVo claimFeeVo = creatNewClaimKindFeeVo(registNo,claimVo,chargeVo.getKindCode(),chargeFee,
									chargeVo.getFloatReason(),userCode,node,chargeVo.getChargeCode());
							kindAndFeeMap.put(mapKey,claimFeeVo);
						}else{
							if(kindAndFeeMap.containsKey(mapKey)){
								// 如果Map中包含该险别，则累加金额
								PrpLClaimKindFeeVo updclaimFeeVo = kindAndFeeMap.get(mapKey);
								if(chargeVo.getVeriChargeFee()!=null){
									updclaimFeeVo.setPayFee(updclaimFeeVo.getPayFee().add(chargeVo.getVeriChargeFee()));
								}else{
									//查勘时取ChargeFee字段VeriChargeFee字段是空的
									if("Check".equals(chargeVo.getBusinessType())){
										updclaimFeeVo.setPayFee(updclaimFeeVo.getPayFee().add(chargeVo.getChargeFee()));
									}
								}
								kindAndFeeMap.put(mapKey,updclaimFeeVo);
							}else{
								BigDecimal chargeFeeNew = BigDecimal.ZERO;
								if("Check".equals(chargeVo.getBusinessType())){
									chargeFeeNew = DataUtils.NullToZero(chargeVo.getChargeFee());
								}else{
									chargeFeeNew = chargeVo.getVeriChargeFee();
								}
								// 如果Map中不包含该险别，则新加元素
								PrpLClaimKindFeeVo newClaimFeeVo = creatNewClaimKindFeeVo(registNo,claimVo,chargeVo.getKindCode(),chargeFeeNew,
								chargeVo.getFloatReason(),userCode,node,chargeVo.getChargeCode());
								kindAndFeeMap.put(mapKey,newClaimFeeVo);
							}
						}
					}
				}
		// 遍历map将vo添加到volist中
		for(PrpLClaimKindFeeVo claimKindFee:kindAndFeeMap.values()){
			claimKindFeeList.add(claimKindFee);
		}

		return claimKindFeeList;
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeAmtAfterComp(ins.sino.claimcar.claim.vo.PrpLClaimVo, ins.sino.claimcar.claim.vo.PrpLCompensateVo, java.lang.String, java.lang.String, ins.sino.claimcar.flow.constant.FlowNode)
	 * @param claimVo
	 * @param compeVo
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @return
	 */
	@Override
	public List<PrpLClaimKindFeeVo> updateClaimFeeAmtAfterComp(PrpLClaimVo claimVo,PrpLCompensateVo compeVo,String registNo,String userCode,
																FlowNode node) {
		// 立案费用刷新和插入按照险别分类
		logger.debug("=======核赔通过立案刷新费用=====");
		List<PrpLChargeVo> chargeVoList = compeVo.getPrpLCharges();
		Map<String,PrpLClaimKindFeeVo> kindAndFeeMap = new HashMap<String,PrpLClaimKindFeeVo>();
		List<PrpLClaimKindFeeVo> claimKindFeeList = new ArrayList<PrpLClaimKindFeeVo>();
		String floatReason = node.getName();
		for(PrpLChargeVo chargeVo:chargeVoList){
			if(kindAndFeeMap==null||kindAndFeeMap.size()==0){
				// 如果Map为空，直接添加新的元素到Map中
				PrpLClaimKindFeeVo claimFeeVo = creatNewClaimKindFeeVo(registNo,claimVo,chargeVo.getKindCode(),chargeVo.getFeeAmt(),floatReason,
						userCode,node,chargeVo.getChargeCode());
				kindAndFeeMap.put(chargeVo.getKindCode(),claimFeeVo);
			}else{
				if(kindAndFeeMap.containsKey(chargeVo.getKindCode())){
					// 如果Map中包含该险别，则累加金额
					PrpLClaimKindFeeVo updclaimFeeVo = kindAndFeeMap.get(chargeVo.getKindCode());
					if(chargeVo.getFeeAmt()!=null){
						updclaimFeeVo.setPayFee(updclaimFeeVo.getPayFee().add(chargeVo.getFeeAmt()));
					}
					kindAndFeeMap.put(chargeVo.getKindCode(),updclaimFeeVo);
				}else{
					// 如果Map中不包含该险别，则新加元素
					PrpLClaimKindFeeVo newClaimFeeVo = creatNewClaimKindFeeVo(registNo,claimVo,chargeVo.getKindCode(),chargeVo.getFeeAmt(),
							floatReason,userCode,node,chargeVo.getChargeCode());
					kindAndFeeMap.put(chargeVo.getKindCode(),newClaimFeeVo);
				}
			}
		}
		// 遍历map将vo添加到volist中
		for(PrpLClaimKindFeeVo claimKindFee:kindAndFeeMap.values()){
			claimKindFeeList.add(claimKindFee);
		}

		return claimKindFeeList;
	}

	/**
	 * 创建claimFeeVo并赋值
	 * @param registNo
	 * @param cMainVo
	 * @param chargeVo
	 * @param userCode
	 * @param node
	 * @return
	 * @modified: ☆LiuPing(2016年6月23日 ): <br>
	 */
	private PrpLClaimKindFeeVo creatNewClaimKindFeeVo(String registNo,PrpLClaimVo claimVo,String kindCode,BigDecimal PayFee,String adjustReason,
														String userCode,FlowNode node,String chargeCode) {
		PrpLClaimKindFeeVo claimFeeVo = new PrpLClaimKindFeeVo();
		claimFeeVo.setRegistNo(registNo);
		claimFeeVo.setRiskCode(claimVo.getRiskCode());
		claimFeeVo.setNodeName(node.name());
		claimFeeVo.setKindCode(kindCode);
		claimFeeVo.setFeeCode(chargeCode);
		claimFeeVo.setCurrency("CNY");
		claimFeeVo.setExchRate(BigDecimal.ONE);
		claimFeeVo.setPayFee(PayFee);
		claimFeeVo.setAdjustTime(new Date());
		claimFeeVo.setAdjustReason(node.getName());
		claimFeeVo.setCreateTime(new Date());
		claimFeeVo.setCreateUser(userCode);
		claimFeeVo.setUpdateTime(new Date());
		claimFeeVo.setUpdateUser(userCode);
		return claimFeeVo;
	}

	/**
	 * 创建一个新的ClaimKindFreshVo并赋值
	 * @param itemName
	 * @param defLoss
	 * @param rescueFee
	 * @param claimLoss
	 * @return
	 * @modified: ☆weilanlei(2016年6月22日 ): <br>
	 */
	private ClaimKindFreshVo createNewClaimKindVo(String itemName,BigDecimal defLoss,BigDecimal rescueFee,BigDecimal claimLoss){
			ClaimKindFreshVo newClaimKind = new ClaimKindFreshVo();
			newClaimKind.setItemName(itemName);
			newClaimKind.setDefLoss(defLoss);
			if(rescueFee!=null){
				newClaimKind.setRescueFee(rescueFee);
			}
			newClaimKind.setClaimLoss(claimLoss);
			return newClaimKind;
	}

	/**
	 * 获得案件下的所有车辆、财产、人伤跟踪的核损金额
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月4日 下午4:05:46): <br>
	 */
	private BigDecimal getSumDefLoss(String registNo,PrpLCMainVo prpLCMainVo){
		PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(registNo);
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLDlossPersTraceVo> prpLDlossPersTraceVoList = persTraceDubboService.findPrpLDlossPersTraceVoListByRegistNo(registNo);
		List<PrpLDlossChargeVo> prpLDlossChargeVoList = lossChargeService.findLossChargeVoByRegistNo(registNo);
		BigDecimal carSumLossFee = BigDecimal.ZERO;
		BigDecimal propSumLossFee = BigDecimal.ZERO;
		BigDecimal personSumLossFee = BigDecimal.ZERO;
		BigDecimal SumChargeFee = BigDecimal.ZERO;//费用总计
		Map<Integer,Integer> carSerialNoMap = new HashMap<Integer,Integer>();
		Map<Integer,Integer> propSerialNoMap = new HashMap<Integer,Integer>();
		
		// 车辆估算金额
		if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				// 如果核损金额为null ,取查勘
				if(prpLDlossCarMainVo.getSumVeriLossFee()==null&&prpLDlossCarMainVo.getSumVeriRescueFee()==null){
					continue;
				}else{
					carSumLossFee = carSumLossFee.add(prpLDlossCarMainVo.getSumVeriLossFee().add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumVeriRescueFee())));
				}
				//附加险也需要累加
				if(prpLDlossCarMainVo.getPrpLDlossCarSubRisks()!=null&&prpLDlossCarMainVo.getPrpLDlossCarSubRisks().size()>0){
					for(PrpLDlossCarSubRiskVo carSubRiskVo:prpLDlossCarMainVo.getPrpLDlossCarSubRisks()){
						carSumLossFee = carSumLossFee.add(carSubRiskVo.getVeriSubRiskFee());
					}
				}
				carSerialNoMap.put(prpLDlossCarMainVo.getSerialNo(),prpLDlossCarMainVo.getSerialNo());
			}
		}

		// 从查勘获取车辆损失信息
		PrpLCheckTaskVo prpLCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
		List<PrpLCheckCarVo> prpLCheckCarVoList = prpLCheckTaskVo.getPrpLCheckCars();
		for(PrpLCheckCarVo checkCarVo:prpLCheckCarVoList){
			if( !carSerialNoMap.containsKey(checkCarVo.getSerialNo())){
				carSumLossFee = carSumLossFee.add(checkCarVo.getLossFee().add(DataUtils.NullToZero(checkCarVo.getRescueFee())));
			}
		}
		
		// 财产估损金额
		if(prpLdlossPropMainVoList != null && !prpLdlossPropMainVoList.isEmpty()){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				// 如果核损金额为null ,取查勘
				if(prpLdlossPropMainVo.getSumVeriLoss() == null){
					continue;
				}else{
					propSumLossFee = propSumLossFee.add(prpLdlossPropMainVo.getSumVeriLoss());
				}
				propSerialNoMap.put(prpLdlossPropMainVo.getSerialNo(),prpLdlossPropMainVo.getSerialNo());
			}
		}
		
		// 从查勘获取财产损失信息
		List<PrpLCheckPropVo> prpLCheckPropVoList = prpLCheckTaskVo.getPrpLCheckProps();
		for(PrpLCheckPropVo prpLCheckPropVo:prpLCheckPropVoList){
			if( !propSerialNoMap.containsKey(prpLCheckPropVo.getLossPartyId().intValue())){
				propSumLossFee = propSumLossFee.add(prpLCheckPropVo.getLossFee());
			}
		}
		
		// 人伤估损金额
		if(prpLDlossPersTraceVoList != null && !prpLDlossPersTraceVoList.isEmpty()){
			for(PrpLDlossPersTraceVo prpLDlossPersTraceVo : prpLDlossPersTraceVoList){
				if(prpLDlossPersTraceVo.getSumVeriDefloss() != null){
					personSumLossFee = personSumLossFee.add(prpLDlossPersTraceVo.getSumVeriDefloss());
				}
			}
		}/*else{//人伤核损表中无值也不从案均值取
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(prpLRegistVo.getDamageTime());
			Integer demageYear = Integer.parseInt(dateString.substring(0,4));
			String comCode = prpLCMainVo.getComCode();
			String riskCode = prpLCMainVo.getRiskCode();
			if(!Risk.isDQZ(riskCode)){
				List<PrpDClaimAvgVo> prpDClaimAvgVoListB = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_B,
						null,demageYear,comCode);
				PrpDClaimAvgVo prpDClaimAvgVoB = prpDClaimAvgVoListB.get(0);
				
				List<PrpDClaimAvgVo> prpDClaimAvgVoListD11 = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_D11,
						null,demageYear,comCode);
				PrpDClaimAvgVo prpDClaimAvgVoD11 = prpDClaimAvgVoListD11.get(0);
				
				List<PrpDClaimAvgVo> prpDClaimAvgVoListD12 = registQueryService.findForceClaimAverageValue(riskCode,CodeConstants.KINDCODE.KINDCODE_D12,
						null,demageYear,comCode);
				PrpDClaimAvgVo prpDClaimAvgVoD12 = prpDClaimAvgVoListD12.get(0);
				
				personSumLossFee = prpDClaimAvgVoB.getAvgAmount().add(prpDClaimAvgVoD11.getAvgAmount().add(prpDClaimAvgVoD12.getAvgAmount()));
			}
		}*/
		//累加费用
		for(PrpLDlossChargeVo dlossCharge : prpLDlossChargeVoList){
			if("1101".equals(prpLCMainVo.getRiskCode())){
				//交强险统计交强险费用
				if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(dlossCharge.getKindCode())){
					if("Check".equals(dlossCharge.getBusinessType())){
						SumChargeFee = SumChargeFee.add(DataUtils.NullToZero(dlossCharge.getChargeFee()));
					}else{
						SumChargeFee = SumChargeFee.add(dlossCharge.getVeriChargeFee());
					}
				}
			}else{
				if(!CodeConstants.KINDCODE.KINDCODE_BZ.equals(dlossCharge.getKindCode())){
					if("Check".equals(dlossCharge.getBusinessType())){
						SumChargeFee = SumChargeFee.add(DataUtils.NullToZero(dlossCharge.getChargeFee()));
					}else{
						SumChargeFee = SumChargeFee.add(dlossCharge.getVeriChargeFee());
					}
				}
			}
		}
		logger.debug("立案刷新sumDefLoss："+carSumLossFee+"===="+propSumLossFee+"===="+personSumLossFee+"===="+SumChargeFee);
		return carSumLossFee.add(propSumLossFee).add(personSumLossFee).add(SumChargeFee);
	}
	/**
	 * 立案刷新汇总子表的估损金额
	 * <pre></pre>
	 * @param claimVo
	 * @return
	 * @modified:
	 * ☆WLL(2016年7月27日 上午10:33:34): <br>
	 */
	private BigDecimal getSumDefLoss(PrpLClaimVo claimVo){
		BigDecimal sumDefLoss = BigDecimal.ZERO;
		// 累加估损金额和施救费
		if(claimVo.getPrpLClaimKinds()!=null&&claimVo.getPrpLClaimKinds().size()>0){
			for(PrpLClaimKindVo claimKind:claimVo.getPrpLClaimKinds()){
				sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(claimKind.getDefLoss()));
				sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(claimKind.getRescueFee()));
			}
		}
/*		// 累加费用
		if(claimVo.getPrpLClaimKindFees()!=null&&claimVo.getPrpLClaimKindFees().size()>0){
			for(PrpLClaimKindFeeVo claimKindFee:claimVo.getPrpLClaimKindFees()){
				sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(claimKindFee.getPayFee()));
			}
		}*/
		logger.debug("立案刷新sumDefLoss："+sumDefLoss);
		return sumDefLoss;
	}
	
	// 保存或者更新立案
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#saveOrUpdatePrpLClaimVo(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param prpLClaimVo
	 */
	@Override
	public void saveOrUpdatePrpLClaimVo(PrpLClaimVo prpLClaimVo){
		PrpLClaim prpLClaim = databaseDao.findByPK(PrpLClaim.class,prpLClaimVo.getClaimNo());

		if(prpLClaim!=null){
			claimService.updateClaim(prpLClaimVo);
		}else{
			claimService.saveClaim(prpLClaimVo);
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#cancleClaim(java.lang.String, java.lang.String, java.math.BigDecimal)
	 * @param claimNo
	 * @param validFlag
	 * @param flowTaskId
	 */
	@Override
	public void cancleClaim(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo,WfTaskSubmitVo submitVo){
		claimService.cancleClaimByClaimNo(claimNo,validFlag,userVo);
		wfTaskHandleService.cancelTaskClaim(submitVo,userVo.getUserCode(),flowTaskId);
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#cancleClaimRecover(java.lang.String, java.lang.String, java.math.BigDecimal)
	 * @param claimNo
	 * @param validFlag
	 * @param flowTaskId
	 */
	@Override
	public void cancleClaimRecover(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo,WfTaskSubmitVo submitVo){
		claimService.cancleClaimByClaimNo(claimNo,validFlag,userVo);
		wfTaskHandleService.cancelTaskClaimRecover(submitVo,userVo.getUserCode(),flowTaskId);
	}

	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeForFifteen(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 * @param registVo
	 */
	@Override
	public void updateClaimFeeForFifteen(PrpLRegistVo registVo){
		PrpLRegistVo prpLRegistVo = registVo;
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registVo.getRegistNo());
		if(prpLClaimVoList==null||prpLClaimVoList.size()==0){
			logger.debug("案件未立案，不执行上浮");
			return;
		}
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registVo.getRegistNo());
		// 交强
		PrpLCMainVo prpLCMainVoDZA = null;
		// 商业
		PrpLCMainVo prpLCMainVoDAA = null;
		for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
			if(prpLCMainVo.getRiskCode().trim().startsWith("11")){
				prpLCMainVoDZA = prpLCMainVo;
			}else{
				prpLCMainVoDAA = prpLCMainVo;
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(prpLRegistVo.getDamageTime());
		Integer demageYear = Integer.parseInt(dateString.substring(0,4));
		
		Map<String,BigDecimal> kindMap = new HashMap<String,BigDecimal>();
		Set<String> allKindSet = new HashSet<String>();
		// 如果查勘表无数据，说明为强制立案，直接将有估损金额的险别记录翻倍
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registVo.getRegistNo());
		if(checkVo==null){
			for(PrpLClaimVo claimVo:prpLClaimVoList){
				for(PrpLClaimKindVo claimKindVo:claimVo.getPrpLClaimKinds()){
					if(claimKindVo.getClaimLoss()!=null&&claimKindVo.getClaimLoss().compareTo(BigDecimal.ZERO)==1){
						allKindSet.add(claimKindVo.getKindCode());
					}
				}
			}
		}else{
			// 车辆核损全部完成标识
			Set<String> kindCarSet = this.allCarEnd(registVo.getRegistNo());
			// 财产核损全部完成标识
			Set<String> kindPropSet = this.allPropEnd(registVo.getRegistNo());
			
			if(kindCarSet != null){
				for(String kindCode : kindCarSet){
					allKindSet.add(kindCode);
				}
				for(String kindCode : kindPropSet){
					allKindSet.add(kindCode);
				}
			}
		}
		
		
		for(String kindCode : allKindSet){
			String riskCode = null;
			String comCode = null;
			BigDecimal claimLoss = BigDecimal.ZERO;
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
				riskCode = prpLCMainVoDZA.getRiskCode();
				comCode = prpLCMainVoDZA.getComCode();
				List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,kindCode,null,demageYear,comCode);
				
				// 翻倍的案均值和保额比较，取较小的那一个，交强先赋值翻倍案均，后面管控
				for(PrpLCItemKindVo cItemKind:prpLCMainVoDZA.getPrpCItemKinds()){
					if(kindCode.equals(cItemKind.getKindCode())){
						claimLoss = prpDClaimAvgVoList.get(0).getAvgAmount().add(prpDClaimAvgVoList.get(0).getAvgAmount());
						logger.debug(kindCode+"案均上浮金额取翻倍案均"+claimLoss);
						break;
					}
				}
				kindMap.put(kindCode,claimLoss);
			}else{
				riskCode = prpLCMainVoDAA.getRiskCode();
				comCode = prpLCMainVoDAA.getComCode();
				List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,kindCode,null,demageYear,comCode);
				// 翻倍的案均值和保额比较，取较小的那一个
				for(PrpLCItemKindVo cItemKind:prpLCMainVoDAA.getPrpCItemKinds()){
					if(kindCode.equals(cItemKind.getKindCode())){
						if(CodeConstants.KINDCODE.KINDCODE_B.equals(cItemKind.getKindCode())){
							claimLoss = cItemKind.getAmount();
							//B险直接赋值案均翻倍值，最后赋值时会管控
							for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
								if("01".equals(avgVo.getAvgType())){
									claimLoss = avgVo.getAvgAmount().multiply(new BigDecimal(2));
									logger.debug(kindCode+"案均上浮金额先设置为翻倍案均"+claimLoss);
								}
							}
						}else{
							if(cItemKind.getAmount().compareTo(prpDClaimAvgVoList.get(0).getAvgAmount().add(prpDClaimAvgVoList.get(0).getAvgAmount()))==-1){
								claimLoss = cItemKind.getAmount();
								logger.debug(kindCode+"案均上浮金额取保额"+claimLoss);
							}else{
								claimLoss = prpDClaimAvgVoList.get(0).getAvgAmount().add(prpDClaimAvgVoList.get(0).getAvgAmount());
								logger.debug(kindCode+"案均上浮金额取翻倍案均"+claimLoss);
							}
						}
						
						break;
					}
				}
				
				kindMap.put(kindCode,claimLoss);
			}
		}
		
		if(prpLClaimVoList == null || prpLClaimVoList.isEmpty()){
			return;
		}
		if(kindMap!=null&&!kindMap.isEmpty()){
			for(PrpLClaimVo prpLClaimVo : prpLClaimVoList){
				if("0".equals(prpLClaimVo.getValidFlag())){
					logger.debug("被注销的立案不执行车物上浮"+prpLClaimVo.getClaimNo());
					continue;
				}else{
					boolean notFloatFlag = false;
					for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
						if("车物案均上浮".equals(claimKindVo.getAdjustReason())){
							logger.debug("已经上浮过的案件不再执行车物上浮");
							notFloatFlag = true;
							break;
						}
					}
					if(notFloatFlag){
						continue;
					}
				}
				if(prpLClaimVo.getPrpLClaimKinds() != null && !prpLClaimVo.getPrpLClaimKinds().isEmpty()){
					for(PrpLClaimKindVo prpLClaimKindVo : prpLClaimVo.getPrpLClaimKinds()){
						prpLClaimKindVo.setAdjustReason("车物案均上浮");
						if(kindMap.containsKey(prpLClaimKindVo.getKindCode())){
							if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLClaimKindVo.getKindCode())
									||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLClaimKindVo.getKindCode())){
								// 车物案均上浮不上浮人伤相关数据
								continue;
							}
							if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
								if(CodeConstants.FeeTypeCode.PROPLOSS.equals(prpLClaimKindVo.getLossItemNo())){
									// 翻倍之后要与查勘的估计赔款比，取大者
									BigDecimal claimLoss = kindMap.get(prpLClaimKindVo.getKindCode().trim()).max(prpLClaimKindVo.getClaimLoss());
									// 与保额比较
									//Map<String,BigDecimal> dutyAmtMap = this.getCIDutyAmt(registVo.getRegistNo());
									Map<String,BigDecimal> dutyAmtMap = configService.getCIDutyAmt(registVo.getRegistNo());
									if(claimLoss.compareTo(dutyAmtMap.get("carLoss"))==1){
										claimLoss = dutyAmtMap.get("carLoss");
										logger.debug("BZ车物超限额取保额"+claimLoss);
									}
									logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
									prpLClaimKindVo.setClaimLoss(claimLoss);
								}
							}else if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())){
								if("车物".equals(prpLClaimKindVo.getLossItemName())){
									BigDecimal claimLoss = kindMap.get(prpLClaimKindVo.getKindCode().trim()).max(prpLClaimKindVo.getClaimLoss());
									// 查勘之后翻倍之后要与查勘的估计赔款比，取大者
									//B险上浮需要对人伤和车物估损的总和进行管控，人伤+物损>保额，物损的估损和赔款按照比例和人伤分摊
									Map<String,BigDecimal> lossClaimLossMap = this.getPropLossFeeKindB(registVo.getRegistNo(),claimLoss,prpLCMainVoDAA);
									claimLoss = lossClaimLossMap.get("车物");
									logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
									prpLClaimKindVo.setClaimLoss(claimLoss);
									// 人伤B险的估计赔款也需要同时刷新
									for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
										if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())
												&&"人".equals(claimKindVo.getLossItemName())){
											claimKindVo.setClaimLoss(lossClaimLossMap.get("人"));
											logger.debug("B险保额控制-刷新人伤赔款："+claimKindVo.getKindCode()+"="+claimKindVo.getClaimLoss());
											break;
										}
									}
								}
							}else{
								BigDecimal claimLoss = kindMap.get(prpLClaimKindVo.getKindCode().trim()).max(prpLClaimKindVo.getClaimLoss());
								if(prpLClaimKindVo.getClaimLoss().compareTo(claimLoss)==1){
									claimLoss = prpLClaimKindVo.getClaimLoss();
								}
								logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
								prpLClaimKindVo.setClaimLoss(claimLoss);
							}
						}	
					}
				}
				prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);
				this.saveOrUpdatePrpLClaimVo(prpLClaimVo);
				// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
				claimKindHisService.saveClaimKindHis(prpLClaimVo);
				
				//立案刷新送再保 立案送再保未决数据分摊试算   niuqiang
				try{
					List<PrpLClaimKindHisVo> kindHisVoList = 
					claimKindHisService.findKindHisVoList(prpLClaimVo.getClaimNo(), "1");
					ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
					claimInterfaceLogVo.setClaimNo(prpLClaimVo.getClaimNo());
					claimInterfaceLogVo.setRegistNo(prpLClaimVo.getRegistNo());
					claimInterfaceLogVo.setComCode(prpLClaimVo.getComCode());
					claimInterfaceLogVo.setCreateUser(prpLClaimVo.getUpdateUser());
					claimInterfaceLogVo.setCreateTime(new Date());
					claimInterfaceLogVo.setOperateNode(FlowNode.Claim.getName());
					if (kindHisVoList != null && kindHisVoList.size() > 0) {
						interfaceAsyncService.TransDataForClaimVo(prpLClaimVo,
								kindHisVoList,claimInterfaceLogVo);
				}
				}catch(Exception e){
					e.printStackTrace();
					//throw new IllegalArgumentException("立案送再保未决数据分摊试算！"+e);
				}
			}
		}
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getPersLossFeeKindB(java.lang.String, java.math.BigDecimal, ins.sino.claimcar.regist.vo.PrpLCMainVo)
	 * @param registNo
	 * @param claimLossPers
	 * @param cMainVo
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> getPersLossFeeKindB(String registNo,BigDecimal claimLossPers,PrpLCMainVo cMainVo){
		Map<String,BigDecimal> lossClaimLossMap = new HashMap<String,BigDecimal>();
		BigDecimal claimLossProp = BigDecimal.ZERO;
		lossClaimLossMap.put("人",claimLossPers);
		lossClaimLossMap.put("车物",claimLossProp);
		
		BigDecimal amountB = BigDecimal.ZERO;
		for(PrpLCItemKindVo cItemKind:cMainVo.getPrpCItemKinds()){
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(cItemKind.getKindCode())){
				amountB = cItemKind.getAmount();
				break;
			}
		}
		// 获取车辆估损赔款信息，直接从立案险别金额表获取
		List<PrpLClaimKindVo> PrpLClaimKindVoList = claimService.findClaimKindVoListByRegistNo(registNo);
		if(PrpLClaimKindVoList!=null&&PrpLClaimKindVoList.size()>0){
			for(PrpLClaimKindVo claimKind:PrpLClaimKindVoList){
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKind.getKindCode())&&"车物".equals(claimKind.getLossItemName())){
					claimLossProp = claimLossProp.add(claimKind.getClaimLoss());
					break;
				}
			}
		}
		
		// 判断人伤+物损是否大于保额，大于则按照估损比例分摊赔款
		if(amountB.compareTo(claimLossProp.add(claimLossPers))==-1){
			BigDecimal sumDefLoss = claimLossProp.add(claimLossPers);//总赔款
			BigDecimal proportPersInSum = claimLossPers.divide(sumDefLoss,5,BigDecimal.ROUND_HALF_UP);//车物占总赔款比例
			claimLossPers = amountB.multiply(proportPersInSum);
			claimLossProp = amountB.subtract(claimLossPers);
		}
		lossClaimLossMap.put("人",claimLossPers);
		lossClaimLossMap.put("车物",claimLossProp);
		return lossClaimLossMap;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getPropLossFeeKindB(java.lang.String, java.math.BigDecimal, ins.sino.claimcar.regist.vo.PrpLCMainVo)
	 * @param registNo
	 * @param claimLossProp
	 * @param cMainVo
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> getPropLossFeeKindB(String registNo,BigDecimal claimLossProp,PrpLCMainVo cMainVo){
		Map<String,BigDecimal> lossClaimLossMap = new HashMap<String,BigDecimal>();
		BigDecimal claimLossPers = BigDecimal.ZERO;
		lossClaimLossMap.put("人",claimLossPers);
		lossClaimLossMap.put("车物",claimLossProp);
		
		BigDecimal amountB = BigDecimal.ZERO;
		for(PrpLCItemKindVo cItemKind:cMainVo.getPrpCItemKinds()){
			if(CodeConstants.KINDCODE.KINDCODE_B.equals(cItemKind.getKindCode())){
				amountB = cItemKind.getAmount();
				break;
			}
		}
		boolean getLossflag = false;
		// 取人伤B险的估损和赔款，立案险别金额没有则从人伤定损取值
		List<PrpLClaimKindVo> PrpLClaimKindVoList = claimService.findClaimKindVoListByRegistNo(registNo);
		if(PrpLClaimKindVoList!=null&&PrpLClaimKindVoList.size()>0){
			for(PrpLClaimKindVo claimKind:PrpLClaimKindVoList){
				if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKind.getKindCode())&&"人".equals(claimKind.getLossItemName())){
					claimLossPers = claimLossPers.add(claimKind.getClaimLoss());
					getLossflag = true;
					break;
				}
			}
		}
		if(!getLossflag){
			List<PrpLDlossPersTraceMainVo> traceMainList = persTraceDubboService.findPersTraceMainVoList(registNo);
			if(traceMainList!=null&&traceMainList.size()>0){
				for(PrpLDlossPersTraceMainVo persTraceMain:traceMainList){
					for(PrpLDlossPersTraceVo persTrace:persTraceMain.getPrpLDlossPersTraces()){
						if(persTrace.getPrpLDlossPersInjured().getSerialNo()!=1){
							// 累加三者人估损
							if(persTrace.getSumVeriDefloss()!=null){
								claimLossPers = claimLossPers.add(persTrace.getSumVeriDefloss());
							}else{
								claimLossPers = claimLossPers.add(persTrace.getSumdefLoss());
							}
						}
					}
				}
			}
		}
		// 判断人伤+物损是否大于保额，大于则按照估损比例分摊赔款
		if(amountB.compareTo(claimLossProp.add(claimLossPers))==-1){
			BigDecimal sumDefLoss = claimLossProp.add(claimLossPers);//总赔款
			BigDecimal proportPropInSum = claimLossProp.divide(sumDefLoss,5,BigDecimal.ROUND_HALF_UP);//车物占总赔款比例
			claimLossProp = amountB.multiply(proportPropInSum);
			claimLossPers = amountB.subtract(claimLossProp);
		}
		lossClaimLossMap.put("人",claimLossPers);
		lossClaimLossMap.put("车物",claimLossProp);
		return lossClaimLossMap;
	}
	
	/**
	 * 车辆核损全部完成
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月18日 上午11:43:37): <br>
	 */
	private Set<String> allCarEnd(String registNo){
		Set<String> kindSet = new HashSet<String>();
		String kindCode = "";
		boolean allCarEndFlag = true;
		// 车辆未完全核损通过
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(prpLDlossCarMainVoList != null && !prpLDlossCarMainVoList.isEmpty()){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
				if(prpLDlossCarMainVo.getUnderWriteFlag() != null && "0".equals(prpLDlossCarMainVo.getUnderWriteFlag().trim())){
					allCarEndFlag = false;
					break;
				}
			}
		}else{
			allCarEndFlag = false;
		}
		if(!allCarEndFlag){
			// 有定损未核损也取查勘的险别
			// 有查勘未定损
			List<PrpLCheckCarVo> checkCarVoList = checkTaskService.findCheckCarVo(registNo);
			if(checkCarVoList != null && !checkCarVoList.isEmpty()){
				for(PrpLCheckCarVo prpLCheckCarVo:checkCarVoList){
					if(prpLCheckCarVo.getSerialNo() == 1){
						if(prpLCheckCarVo.getKindCode()==null||!CodeConstants.KindForSelfCar_List.contains(prpLCheckCarVo.getKindCode())){
							// 标的车险别为空或者对应的险别不是AZLGF，即标的车没有购买赔付标的的险别，则不带入计算
							logger.debug("标的车没有购买赔付标的的险别，不上浮估损");
							continue;
						}else{
							// 主车默认带出处理页面勾选的险别
							kindCode = prpLCheckCarVo.getKindCode();
							kindSet.add(kindCode);
							logger.debug("查勘标的车代入上浮-险别："+prpLCheckCarVo.getKindCode());
						}
					}else{
						kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						kindSet.add(kindCode);
						// 三者车默认三者险
						kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						kindSet.add(kindCode);
					}
				}
			}
		}
		return kindSet;
	}
	
	/**
	 * 财产核损全部完成
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月18日 上午11:45:21): <br>
	 */
	private Set<String> allPropEnd(String registNo){
		Set<String> kindSet = new HashSet<String>();
		String kindCode = "";
		boolean allPropEndFlag = true;
		List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
		if(prpLdlossPropMainVoList != null && !prpLdlossPropMainVoList.isEmpty()){
			for(PrpLdlossPropMainVo prpLdlossPropMainVo:prpLdlossPropMainVoList){
				if(prpLdlossPropMainVo.getUnderWriteFlag() != null && "0".equals(prpLdlossPropMainVo.getUnderWriteFlag().trim())){
					allPropEndFlag = false;
					break;
				}
			}
		}else{
			List<PrpLCheckPropVo> checkPropList = checkTaskService.findCheckPropVo(registNo);
			if(checkPropList != null && !checkPropList.isEmpty()){
				allPropEndFlag = false;
			}
		}
		
		if(!allPropEndFlag){
			List<PrpLCheckPropVo> propVoList = checkTaskService.findCheckPropVo(registNo);
			if(propVoList != null && !propVoList.isEmpty()){
				for(PrpLCheckPropVo prpLCheckPropVo:propVoList){
					if(prpLCheckPropVo.getLossPartyId() == 1){
						kindCode = CodeConstants.KINDCODE.KINDCODE_D2;
						kindSet.add(kindCode);
					}else{
						kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						kindSet.add(kindCode);
						kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						kindSet.add(kindCode);
					}
				}
			}
		}
		return kindSet;
	}
	
	/**
	 * 人伤核损全部完成
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月18日 上午11:46:27): <br>
	 */
	private Set<String> allPersonEnd(String registNo){
		Set<String> kindSet = new HashSet<String>();
		String kindCode = "";
		boolean allPersonEndFlag = true;
		List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
		if(prpLDlossPersTraceMainVoList != null && !prpLDlossPersTraceMainVoList.isEmpty()){
			for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
				if(prpLDlossPersTraceMainVo.getUnderwriteFlag() != null && "0".equals(prpLDlossPersTraceMainVo.getUnderwriteFlag().trim())){
					allPersonEndFlag = false;
					break;
				}
			}
		}else{//业务表没有人伤
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
			if(prpLRegistVo.getPrpLRegistPersonLosses()!=null&& !prpLRegistVo.getPrpLRegistPersonLosses().isEmpty()){
				for(PrpLRegistPersonLossVo prpLRegistPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
					if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())&&"1".equals(prpLRegistPersonLossVo.getDriverflag())){
						//KINDCODE_D11
						kindCode = CodeConstants.KINDCODE.KINDCODE_D11;
						kindSet.add(kindCode);
					}
					
					if("1".equals(prpLRegistPersonLossVo.getLossparty().trim())){
						if("1".equals(prpLRegistPersonLossVo.getDriverflag())
								&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()-1>0){
							//KINDCODE_D12
							kindCode = CodeConstants.KINDCODE.KINDCODE_D12;
							kindSet.add(kindCode);
						}
						if(!"1".equals(prpLRegistPersonLossVo.getDriverflag())
								&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
							kindCode = CodeConstants.KINDCODE.KINDCODE_D12;
							kindSet.add(kindCode);
						}//KINDCODE_D12
					}
					if("3".equals(prpLRegistPersonLossVo.getLossparty().trim())
							&&prpLRegistPersonLossVo.getInjuredcount()+prpLRegistPersonLossVo.getDeathcount()>0){
						kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						kindSet.add(kindCode);
						kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						kindSet.add(kindCode);
					}
				}
			}
		}
		if(!allPersonEndFlag){
			if(prpLDlossPersTraceMainVoList!=null&&!prpLDlossPersTraceMainVoList.isEmpty()){
				for(PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo:prpLDlossPersTraceMainVoList){
					for(PrpLDlossPersTraceVo persTraceVo:prpLDlossPersTraceMainVo.getPrpLDlossPersTraces()){
						if(persTraceVo.getPrpLDlossPersInjured().getSerialNo()==1){
							if("2".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){
								kindCode = CodeConstants.KINDCODE.KINDCODE_D12;
								kindSet.add(kindCode);
							}else if("3".equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){
								kindCode = CodeConstants.KINDCODE.KINDCODE_D11;
								kindSet.add(kindCode);
							}
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_B;
							kindSet.add(kindCode);
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
							kindSet.add(kindCode);
						}
					}
				}
			}
		}
		return kindSet;
	}
	
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#claimWirteBack(ins.sino.claimcar.claim.vo.PrpLClaimVo)
	 * @param claimVo
	 */
	@Override
	public void claimWirteBack(PrpLClaimVo claimVo) {
		PrpLClaim claimPo = databaseDao.findByPK(PrpLClaim.class,claimVo.getClaimNo());
		if(claimPo != null){
			Beans.copy().from(claimVo).includeNull().to(claimPo);
			databaseDao.save(PrpLClaim.class,claimPo);;
		}
	}
	
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#findPrpLLawSuitVoByRegistNo(java.lang.String)
	 * @param RegistNo
	 * @return
	 */
	@Override
	public List<PrpLLawSuitVo> findPrpLLawSuitVoByRegistNo(String RegistNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", RegistNo);
		List<PrpLLawSuit> lawSuitList= databaseDao.findAll(PrpLLawSuit.class,queryRule);
		List<PrpLLawSuitVo> lawSuitVoList = new ArrayList<PrpLLawSuitVo>();
		for(int i=0;i<lawSuitList.size();i++){
			PrpLLawSuitVo lawSuitVo=Beans.copyDepth().from(lawSuitList.get(i)).to(PrpLLawSuitVo.class);
			lawSuitVoList.add(lawSuitVo);
		}
	return lawSuitVoList;
		}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#reOpenClaimWirteBack(java.lang.String, ins.sino.claimcar.flow.constant.FlowNode, java.lang.String)
	 * @param claimNo
	 * @param node
	 * @param endCaserCode
	 */
	@Override
	public void reOpenClaimWirteBack(String claimNo,FlowNode node,String endCaserCode) {
		PrpLClaim claimPo = databaseDao.findByPK(PrpLClaim.class,claimNo);
		if(claimPo != null){
			if(FlowNode.ReOpenVrf.getName().equals(node.getName()) && endCaserCode == ""){
				claimPo.setEndCaserCode(null);
				claimPo.setEndCaseTime(null);
				claimPo.setCaseNo(null);
			}else if(endCaserCode == ""){
				claimPo.setEndCaserCode(null);
			}else{
				claimPo.setEndCaserCode(endCaserCode);
			}
			databaseDao.update(PrpLClaim.class,claimPo);
		}
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#adjustPropLossState(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public boolean adjustPropLossState(String registNo){
		// 查询是否存在有效的未被注销的立案节点，如果不存在，不需要上浮
		List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo,"1");
		if(claimVoList==null||claimVoList.size()==0){
			logger.debug("无有效的立案节点不执行车物上浮");
			return false;
		}
		List<PrpLDlossCarMainVo> carMainList = lossCarService.findLossCarMainByRegistNo(registNo);
		if(carMainList==null||carMainList.isEmpty()){
			return true;
		}else{
			for(PrpLDlossCarMainVo carMainVo:carMainList){
				if(CodeConstants.VeriFlag.INIT.equals(carMainVo.getUnderWriteFlag())){
					return true;
				}
			}
		}
		List<PrpLdlossPropMainVo> propMainList = propTaskService.findPropMainListByRegistNo(registNo);
		if(propMainList!=null&&propMainList.size()>0){
			for(PrpLdlossPropMainVo propMainVo:propMainList){
				if(CodeConstants.VeriFlag.INIT.equals(propMainVo.getUnderWriteFlag())){
					return true;
				}
			}
		}
		
		return false;
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#updateClaimFeeForThirty(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 * @param registVo
	 */
	@Override
	public void updateClaimFeeForThirty(PrpLRegistVo registVo){
		PrpLRegistVo prpLRegistVo = registVo;
		List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registVo.getRegistNo());
		if(prpLClaimVoList==null||prpLClaimVoList.size()==0){
			logger.debug("案件未立案，不执行上浮");
			return;
		}
		List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registVo.getRegistNo());
		// 交强
		PrpLCMainVo prpLCMainVoDZA = null;
		// 商业
		PrpLCMainVo prpLCMainVoDAA = null;
		for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
			if(prpLCMainVo.getRiskCode().trim().startsWith("11")){
				prpLCMainVoDZA = prpLCMainVo;
			}else{
				prpLCMainVoDAA = prpLCMainVo;
			}
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(prpLRegistVo.getDamageTime());
		Integer demageYear = Integer.parseInt(dateString.substring(0,4));
		Map<String,BigDecimal> kindMap = new HashMap<String,BigDecimal>();

		// 人伤核损全部完成标识
		Set<String> kindPersonSet = this.allPersonEnd(registVo.getRegistNo());
		
		for(String kindCode : kindPersonSet){
			String riskCode = null;
			String comCode = null;
			BigDecimal claimLoss = BigDecimal.ZERO;
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(kindCode)){
				if(prpLCMainVoDZA==null){
					continue;
				}
				riskCode = prpLCMainVoDZA.getRiskCode();
				comCode = prpLCMainVoDZA.getComCode();
				List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,kindCode,null,demageYear,comCode);
				
				// 翻倍的案均值和保额比较，取较小的那一个，交强险根据有责和无责限额不同
				for(PrpLCItemKindVo cItemKind:prpLCMainVoDZA.getPrpCItemKinds()){
					if(kindCode.equals(cItemKind.getKindCode())){
						// 此处直接取翻倍案均，到险别赋值管控保额
						//区分医疗费跟死亡伤残
						for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
							if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(avgVo.getAvgType())){
								claimLoss = avgVo.getAvgAmount().multiply(new BigDecimal(2.5));
								kindMap.put(kindCode+"03",claimLoss);//// 医疗费
								logger.debug(kindCode+"03案均上浮金额先设置为翻倍案均"+claimLoss);
							}
							if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(avgVo.getAvgType())){
								claimLoss = avgVo.getAvgAmount().multiply(new BigDecimal(2.5));
								kindMap.put(kindCode+"02",claimLoss);// 死亡伤残
								logger.debug(kindCode+"02案均上浮金额先设置为翻倍案均"+claimLoss);
							}
						}
						//claimLoss = prpDClaimAvgVoList.get(0).getAvgAmount().multiply(new BigDecimal(2.5));
						logger.debug(kindCode+"案均上浮金额取翻倍案均"+claimLoss);
						break;
					}
				}
				//kindMap.put(kindCode,claimLoss);
			}else{
				if(prpLCMainVoDAA==null){
					continue;
				}
				riskCode = prpLCMainVoDAA.getRiskCode();
				comCode = prpLCMainVoDAA.getComCode();
				List<PrpDClaimAvgVo> prpDClaimAvgVoList = registQueryService.findForceClaimAverageValue(riskCode,kindCode,null,demageYear,comCode);
				// 翻倍的案均值和保额比较，取较小的那一个
				for(PrpLCItemKindVo cItemKind:prpLCMainVoDAA.getPrpCItemKinds()){
					if(kindCode.equals(cItemKind.getKindCode())){
						if(CodeConstants.KINDCODE.KINDCODE_B.equals(cItemKind.getKindCode())){
							claimLoss = cItemKind.getAmount();
							//B险直接赋值案均翻倍值，最后赋值时会管控
							for(PrpDClaimAvgVo avgVo:prpDClaimAvgVoList){
								if("02".equals(avgVo.getAvgType())){
									claimLoss = avgVo.getAvgAmount().multiply(new BigDecimal(2.5));
									logger.debug(kindCode+"案均上浮金额先设置为翻倍案均"+claimLoss);
								}
							}
							
						}else{
							if(cItemKind.getAmount().compareTo(prpDClaimAvgVoList.get(0).getAvgAmount().multiply(new BigDecimal(2.5)))==-1){
								claimLoss = cItemKind.getAmount();
								logger.debug(kindCode+"案均上浮金额取保额"+claimLoss);
							}else{
								claimLoss = prpDClaimAvgVoList.get(0).getAvgAmount().multiply(new BigDecimal(2.5));
								logger.debug(kindCode+"案均上浮金额取翻倍案均"+claimLoss);
							}
						}
						break;
					}
				}
				
				kindMap.put(kindCode,claimLoss);
			}
		}
		
		if(prpLClaimVoList == null || prpLClaimVoList.isEmpty()){
			return;
		}
		
		for(PrpLClaimVo prpLClaimVo : prpLClaimVoList){
			if("0".equals(prpLClaimVo.getValidFlag())){
				logger.debug("被注销的立案不执行人伤上浮"+prpLClaimVo.getClaimNo());
				continue;
			}else{
				boolean notFloatFlag = false;
				for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
					if("人伤案均上浮".equals(claimKindVo.getAdjustReason())){
						logger.debug("已经上浮过的案件不再执行人伤上浮");
						notFloatFlag = true;
						break;
					}
				}
				if(notFloatFlag){
					continue;
				}
			}
			if(prpLClaimVo.getPrpLClaimKinds() != null && !prpLClaimVo.getPrpLClaimKinds().isEmpty()){
				boolean hasPersLossBFlag = false;//判断原claimKind表中是否有人伤记录，没有则新增一条记录
				for(PrpLClaimKindVo prpLClaimKindVo : prpLClaimVo.getPrpLClaimKinds()){
					prpLClaimKindVo.setAdjustReason("人伤案均上浮");
					if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
						if(kindMap.containsKey(prpLClaimKindVo.getKindCode()+prpLClaimKindVo.getLossItemNo())){
							if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(prpLClaimKindVo.getKindCode())){
								if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(prpLClaimKindVo.getLossItemNo())
										||CodeConstants.FeeTypeCode.PERSONLOSS.equals(prpLClaimKindVo.getLossItemNo())){
									BigDecimal claimLoss = new BigDecimal(0);
									if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(prpLClaimKindVo.getLossItemNo())){
										String kindCode = prpLClaimKindVo.getKindCode().trim()+"03";
										claimLoss = kindMap.get(kindCode.trim()).max(prpLClaimKindVo.getClaimLoss());
									}else{
										String kindCode = prpLClaimKindVo.getKindCode().trim()+"02";
										claimLoss = kindMap.get(kindCode.trim()).max(prpLClaimKindVo.getClaimLoss());
									}
									
									// 查勘之后翻倍之后要与查勘的估计赔款比，取大者
									// 上浮的金额与保额比较,不能超过保额
									//Map<String,BigDecimal> dutyAmtMap = this.getCIDutyAmt(registVo.getRegistNo());
									Map<String,BigDecimal> dutyAmtMap = configService.getCIDutyAmt(registVo.getRegistNo());
									if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(prpLClaimKindVo.getLossItemNo())){
										if(claimLoss.compareTo(dutyAmtMap.get("mediLoss"))==1){
											claimLoss = dutyAmtMap.get("mediLoss");
											logger.debug("BZ医疗超限额取保额"+claimLoss);
										}
									}
									if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(prpLClaimKindVo.getLossItemNo())){
										if(claimLoss.compareTo(dutyAmtMap.get("persLoss"))==1){
											claimLoss = dutyAmtMap.get("persLoss");
											logger.debug("BZ死伤超限额取保额"+claimLoss);
										}
									}
									prpLClaimKindVo.setClaimLoss(claimLoss);
									logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
								}
							}
						}
					}else{
						if(kindMap.containsKey(prpLClaimKindVo.getKindCode())){
								if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimKindVo.getKindCode())){
									if("人".equals(prpLClaimKindVo.getLossItemName())){
										hasPersLossBFlag = true;
										BigDecimal claimLoss = kindMap.get(prpLClaimKindVo.getKindCode().trim()).max(prpLClaimKindVo.getClaimLoss());
										// 查勘之后翻倍之后要与查勘的估计赔款比，取大者
										//B险上浮需要对人伤和车物估损的总和进行管控，人伤+物损>保额，物损的估损和赔款按照比例和人伤分摊
										
										Map<String,BigDecimal> lossClaimLossMap = this.getPersLossFeeKindB(registVo.getRegistNo(),claimLoss,prpLCMainVoDAA);
										claimLoss = lossClaimLossMap.get("人");
										logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
										prpLClaimKindVo.setClaimLoss(claimLoss);
										//定损金额取上浮值
										prpLClaimKindVo.setDefLoss(kindMap.get(prpLClaimKindVo.getKindCode()));
										// 物损B险的估计赔款也需要同时刷新
										for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
											if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())
													&&"车物".equals(claimKindVo.getLossItemName())){
												claimKindVo.setClaimLoss(lossClaimLossMap.get("车物"));
												logger.debug("B险保额控制-刷新车物赔款："+claimKindVo.getKindCode()+"="+claimKindVo.getClaimLoss());
												break;
											}
										}
										
										prpLClaimKindVo.setClaimLoss(claimLoss);
										logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
									}
								}else{
									BigDecimal claimLoss = kindMap.get(prpLClaimKindVo.getKindCode().trim()).max(prpLClaimKindVo.getClaimLoss());
									// 查勘之后翻倍之后要与查勘的估计赔款比，取大者
									prpLClaimKindVo.setClaimLoss(claimLoss);
									logger.debug("案均上浮："+prpLClaimKindVo.getKindCode()+"="+claimLoss);
								}
							}
					}
				}
				if(!"1101".equals(prpLClaimVo.getRiskCode())&&kindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_B)&&!hasPersLossBFlag){
					// 如果刷新险别中包括B险，判断原claimKind表中是否有人伤记录，没有则新增一条记录
					BigDecimal claimLoss = kindMap.get(CodeConstants.KINDCODE.KINDCODE_B);
					//B险上浮需要对人伤和车物估损的总和进行管控，人伤+物损>保额，物损的估损和赔款按照比例和人伤分摊
					
					Map<String,BigDecimal> lossClaimLossMap = this.getPersLossFeeKindB(registVo.getRegistNo(),claimLoss,prpLCMainVoDAA);
					claimLoss = lossClaimLossMap.get("人");
					PrpLClaimKindVo claimKindVoPers = new PrpLClaimKindVo();
					for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
						if(CodeConstants.KINDCODE.KINDCODE_B.equals(claimKindVo.getKindCode())){
							if("车物".equals(claimKindVo.getLossItemName())){
								Beans.copy().from(claimKindVo).to(claimKindVoPers);
								claimKindVoPers.setId(null);
								claimKindVoPers.setLossItemName("人");
								claimKindVoPers.setClaimLoss(claimLoss);
								//定损金额取上浮值
								claimKindVoPers.setDefLoss(kindMap.get(CodeConstants.KINDCODE.KINDCODE_B));
								claimKindVo.setClaimLoss(lossClaimLossMap.get("车物"));
								logger.debug("B险保额控制-刷新车物赔款："+claimKindVo.getKindCode()+"="+claimKindVo.getClaimLoss());
								break;
							}
						}
					}
					List<PrpLClaimKindVo> claimKindVoListTemp = new ArrayList<PrpLClaimKindVo>();
					claimKindVoListTemp.addAll(prpLClaimVo.getPrpLClaimKinds());
					claimKindVoListTemp.add(claimKindVoPers);
					prpLClaimVo.setPrpLClaimKinds(claimKindVoListTemp);
				}
			}
			prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);
			this.saveOrUpdatePrpLClaimVo(prpLClaimVo);
			// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
			claimKindHisService.saveClaimKindHis(prpLClaimVo);
			
			//立案刷新送再保 立案送再保未决数据分摊试算   niuqiang
			try{
				List<PrpLClaimKindHisVo> kindHisVoList = 
				claimKindHisService.findKindHisVoList(prpLClaimVo.getClaimNo(), "1");
				ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
				claimInterfaceLogVo.setClaimNo(prpLClaimVo.getClaimNo());
				claimInterfaceLogVo.setRegistNo(prpLClaimVo.getRegistNo());
				claimInterfaceLogVo.setComCode(prpLClaimVo.getComCode());
				claimInterfaceLogVo.setCreateUser(prpLClaimVo.getUpdateUser());
				claimInterfaceLogVo.setCreateTime(new Date());
				claimInterfaceLogVo.setOperateNode(FlowNode.Claim.getName());
				if (kindHisVoList != null && kindHisVoList.size() > 0) {
					interfaceAsyncService.TransDataForClaimVo(prpLClaimVo,
							kindHisVoList,claimInterfaceLogVo);
			}
			}catch(Exception e){
				e.printStackTrace();
				//throw new IllegalArgumentException("立案送再保未决数据分摊试算！"+e);
			}
			
		}
		
	}
	
	/*
	 *（废弃，统一使用ConfigService.getCIDutyAmt）
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#getCIDutyAmt(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> getCIDutyAmt(String registNo){
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
		Map<String,BigDecimal> dutyAmtMap = new HashMap<String,BigDecimal>();
		if(checkDutyList!=null&&!checkDutyList.isEmpty()){
			for(PrpLCheckDutyVo checkDutyVo:checkDutyList ){
				if(checkDutyVo.getSerialNo()==1){
					if("1".equals(checkDutyVo.getCiDutyFlag())){
						dutyAmtMap.put("carLoss",new BigDecimal(2000));  
						dutyAmtMap.put("persLoss",new BigDecimal(110000));
						dutyAmtMap.put("mediLoss",new BigDecimal(10000));
					}else{
						dutyAmtMap.put("carLoss",new BigDecimal(100));  
						dutyAmtMap.put("persLoss",new BigDecimal(11000));
						dutyAmtMap.put("mediLoss",new BigDecimal(1000));
					}
					break;
				}
			}
		}else{
			dutyAmtMap.put("carLoss",new BigDecimal(2000));  
			dutyAmtMap.put("persLoss",new BigDecimal(110000));
			dutyAmtMap.put("mediLoss",new BigDecimal(10000));
		}
		return dutyAmtMap;
	}
	
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#adjustPersLossState(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public boolean adjustPersLossState(String registNo){
		// 查询是否存在有效的未被注销的立案节点，如果不存在，不需要上浮
		List<PrpLClaimVo> claimVoList = claimService.findClaimListByRegistNo(registNo,"1");
		if(claimVoList==null||claimVoList.size()==0){
			logger.debug("无有效的立案节点不执行人伤上浮");
			return false;
		}
		
		//结案不能上浮
		List<PrpLEndCaseVo> listVo = endCasePubService.queryAllByRegistNo(registNo);
		if(listVo!=null && listVo.size()>0){
			logger.info("结案不执行人伤上浮");
			return false;
		}
		// 产生人伤定损主表了才上浮人伤
		List<PrpLDlossPersTraceMainVo> persTraceMainVoList = persTraceDubboService.findPersTraceMainVoList(registNo);
		if(persTraceMainVoList==null||persTraceMainVoList.isEmpty()){
			//如果报案有人伤，但是没有做人伤定损，但是此时立案表人伤相关的数据有金额时，上浮
			PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
			if(registVo.getPrpLRegistPersonLosses()!=null&&registVo.getPrpLRegistPersonLosses().size()>0){
				for(PrpLRegistPersonLossVo vo : registVo.getPrpLRegistPersonLosses()){
					if( vo.getDeathcount() > 0 || vo.getInjuredcount() > 0){
						return true;
					}
				}
			}
			// 如果工作流存在人伤任务节点 执行人伤上浮-在排除了定损和报案之后只需判断in表是否存在调度新增的人伤未处理任务
			boolean persTaskExist = wfTaskHandleService.existCancelByNAndH(registNo, FlowNode.PLFirst.toString());
			if(persTaskExist){
				return true;
			}
		}else{
			for(PrpLDlossPersTraceMainVo persVo:persTraceMainVoList){
				if(CodeConstants.VeriFlag.INIT.equals(persVo.getUnderwriteFlag())){
					return true;
				}
			}
		}
		return false;
	}
	/* 
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#cancleClaim(java.math.BigDecimal, java.lang.String)
	 * @param flowTaskId
	 * @param userCode
	 */
	@Override
	public void cancleClaim(BigDecimal flowTaskId,String userCode,SysUserVo userVo){
		//wfTaskHandleService.cancelTaskClaim(userCode,flowTaskId);
		// 调用工作流注销方法
		wfTaskHandleService.cancelTask(userCode, flowTaskId);
	}

	/**
	 * 理算冲销核赔通过后0结案，刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年12月1日 ): <br>
	 */
	@Override
	public void updateClaimAfterCompeWfZero(String compensateNo,String userCode,FlowNode node) {
		logger.debug("理算冲销核赔0结案刷立案估损================>");
		// 获取理算主表
		PrpLCompensateVo compeVo = compensateService.findCompByPK(compensateNo);
		// 获取立案主表
		PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(compeVo.getClaimNo());
		// 所有估损和赔款全部取0
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>();
		for(PrpLClaimKindVo claimKindVo:prpLClaimVo.getPrpLClaimKinds()){
			claimKindVo.setAdjustReason("冲销核赔零结案");
			claimKindVo.setDefLoss(BigDecimal.ZERO);
			claimKindVo.setClaimLoss(BigDecimal.ZERO);
			claimKindVo.setRescueFee(BigDecimal.ZERO);
			prpLClaimKindVoList.add(claimKindVo);
		}
		List<PrpLClaimKindFeeVo> claimKindFeeList = new ArrayList<PrpLClaimKindFeeVo>();
		for(PrpLClaimKindFeeVo claimKindFeeVo:prpLClaimVo.getPrpLClaimKindFees()){
			claimKindFeeVo.setAdjustReason("冲销核赔零结案");
			claimKindFeeVo.setPayFee(BigDecimal.ZERO);
			claimKindFeeList.add(claimKindFeeVo);
		}
		// 更新立案主子表
		if(prpLClaimVo.getEstiTimes()==null){
			logger.error("立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!");
			throw new BusinessException("立案"+prpLClaimVo.getClaimNo()+"估损次数为空，立案更新失败!",false);
		}

		prpLClaimVo.setSumDefLoss(BigDecimal.ZERO);// 刷新车辆、财产、人伤的损失金额
		//立案主表字段更新
		prpLClaimVo.setSumClaim(BigDecimal.ZERO);
		prpLClaimVo.setUpdateUser(userCode);
		prpLClaimVo.setUpdateTime(new Date());
		prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes()+1);// 估损次数增加
		prpLClaimVo = updateClaimFeeByClaim(prpLClaimVo,prpLClaimKindVoList,claimKindFeeList);// 主子表关联
		logger.debug("案件冲销0结案-报案号："+prpLClaimVo.getRegistNo()+"保单号："+prpLClaimVo.getPolicyNo()+"prpLclaim.getClaimNo():"+prpLClaimVo.getClaimNo()+",prpLclaim.getSumDefLoss():"+prpLClaimVo
				.getSumDefLoss()+",prpLclaim.getSumClaim():"+prpLClaimVo.getSumClaim());
		this.saveOrUpdatePrpLClaimVo(prpLClaimVo);

		// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
		claimKindHisService.saveClaimKindHis(prpLClaimVo);
		
	}
	@Override
	public void regsitAddCaseAfterClaim(String registNo,String policyNo){
		
		// 生成补登的立案未处理节点后 如果查勘节点已提交  调用自动立案方法 生成立案数据并送平台
		boolean checkEndFlag = wfFlowQueryService.isCheckNodeEnd(registNo);
		String claimFlag = null;
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		String flowId = registVo.getFlowId();
		if(!checkEndFlag){
			// 如果查勘节点未提交  检查案件是否符合强制立案要求 符合则调用强制立案方法
			boolean forceClaimFlag = this.adjustForceClaimOrFloatAvg(registVo,"forceClaim");
			if(forceClaimFlag){
				claimFlag = CodeConstants.ClaimFlag.CLAIMFORCE;
			}
		}else{
			claimFlag = CodeConstants.ClaimFlag.AUTOCLAIM;
		}
		PrpLClaimVo claimVo = null;
		try{
			
			if(claimFlag!=null){
				// 调用提交立案方法
				claimVo = this.submitClaim(registNo,policyNo,flowId,claimFlag);
			}
			//立案送平台
			if(claimVo != null){
				claimTaskService.sendClaimToPlatform(claimVo);
			}
			if(checkEndFlag){
				// 查勘节点已提交  查勘送平台
				if(!"22".equals(registVo.getComCode().substring( 0, 2 ))){
					// 上海机构查勘不送平台   查勘在定损时一起送
					if("1101".equals(claimVo.getRiskCode())){
						interfaceAsyncService.sendCheckToPlatformCI(registNo);
					}else{
						interfaceAsyncService.sendCheckToPlatformBI(registNo);
					}
				}
			}
		}catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 判断当前案件已经处理到的最新环节，需要送平台的环节需要补传
		
		//定损环节送平台
		List<PrpLWfTaskVo> prpLWfTaskVoLossList = wfFlowQueryService.findPrpWfTaskVoForOut(registNo,FlowNode.VLoss.toString());
		if(prpLWfTaskVoLossList!=null&&prpLWfTaskVoLossList.size()>0){
			interfaceAsyncService.sendLossToPlatform(registNo,claimVo.getRiskCode());
			
		}
		//单证环节送平台
		List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findPrpWfTaskVoForOut(registNo,FlowNode.Certi.toString());
		if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
			interfaceAsyncService.certifyToPaltform(registNo,claimVo.getRiskCode());
		}
		
	}
	
	/**
	 * 判断案件时间是否符合强制立案、人伤或车物案均上浮条件
	 * <pre></pre>
	 * @param registVo
	 * @return
	 * @modified:
	 * ☆WLL(2017年3月1日 下午4:58:02): <br>
	 */
	private boolean adjustForceClaimOrFloatAvg(PrpLRegistVo registVo,String adjustFlag){
		boolean forceFlag = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(registVo.getReportTime());
		if("forceClaim".equals(adjustFlag)){
			// 报案后48小时未立案  执行强制立案
			cal.add(Calendar.DATE, 2);
		}
		if("pers".equals(adjustFlag)){
			// 报案后30天未人伤核损  执行人伤上浮
			cal.add(Calendar.DATE, 30);
		}
		if("prop".equals(adjustFlag)){
			// 报案后15天未车物核损  执行车物上浮
			cal.add(Calendar.DATE, 15);
		}
		Date forceClaimDay = cal.getTime();
		Date today = new Date();
		if(forceClaimDay.compareTo(today)<=0){
//			forceFlag = forceClaimDay.before(today);
			forceFlag = true;
		}
		
		return forceFlag;
	}

	@Override
	public void saveToPrplTestinfoMain(PrplTestinfoMainVo prplTestinfoMainVo) {
		if(prplTestinfoMainVo!=null){
			PrplTestinfoMain prplTestinfoMain=new PrplTestinfoMain();
			prplTestinfoMain=Beans.copyDepth().from(prplTestinfoMainVo).to(PrplTestinfoMain.class);
			if(prplTestinfoMain.getPrplFraudriskInfos()!=null && prplTestinfoMain.getPrplFraudriskInfos().size()>0){
				for(PrplFraudriskInfo infoPo:prplTestinfoMain.getPrplFraudriskInfos()){
					infoPo.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(prplTestinfoMain.getPrplLaborInfos()!=null && prplTestinfoMain.getPrplLaborInfos().size()>0){
				for(PrplLaborInfo infoPo:prplTestinfoMain.getPrplLaborInfos()){
					infoPo.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(prplTestinfoMain.getPrplOperationInfos()!=null && prplTestinfoMain.getPrplOperationInfos().size()>0){
				for(PrplOperationInfo infoPo:prplTestinfoMain.getPrplOperationInfos()){
					infoPo.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(prplTestinfoMain.getPrplPartsInfos()!=null && prplTestinfoMain.getPrplPartsInfos().size()>0){
				for(PrplPartsInfo infoPo:prplTestinfoMain.getPrplPartsInfos()){
					infoPo.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			if(prplTestinfoMain.getPrplRiskpointInfos()!=null && prplTestinfoMain.getPrplRiskpointInfos().size()>0){
				for(PrplRiskpointInfo infoPo:prplTestinfoMain.getPrplRiskpointInfos()){
					infoPo.setPrplTestinfoMain(prplTestinfoMain);
				}
			}
			
			databaseDao.save(PrplTestinfoMain.class,prplTestinfoMain);
		}
		
	}

	/*
	 * @see ins.sino.claimcar.claim.services.spring.ClaimTaskExtService#submitClaim(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param policyNo
	 * @param flowId
	 * @param claimFlag
	 */
	@Override
	public PrpLClaimVo autoClaimForPingAnCase(String registNo, String policyNo, String flowId, Date registerDate){
		logger.info("报案号 registNo={},保单号={},进入自动立案保存数据方法",registNo,policyNo);
		//查询报案数据
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		// 获取立案主表
		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo, policyNo);
		if (prpLClaimVo == null) {
			prpLClaimVo = this.organizePrpLClaimVo(registNo,policyNo,prpLRegistVo.getFlowId(),CodeConstants.AutoPass.CLAIM);
			List<PrpLClaimKindVo> prpLClaimKindVoList = this.organizeClaimKindVo(registNo,policyNo,prpLClaimVo.getClaimNo(),CodeConstants.AutoPass.CLAIM);
			prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
			// 估损金额
			BigDecimal sumDefLoss = BigDecimal.ZERO;
			BigDecimal sumClaim = BigDecimal.ZERO;
			for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
				sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(prpLClaimKindVo.getDefLoss()));
				sumClaim = sumClaim.add(DataUtils.NullToZero(prpLClaimKindVo.getClaimLoss()));
			}
			prpLClaimVo.setSumDefLoss(sumDefLoss);
			prpLClaimVo.setClaimFlag(CodeConstants.ClaimFlag.AUTOCLAIM);//3-自动立案
			prpLClaimVo.setSumClaim(sumClaim);
			prpLClaimVo.setSumRescueFee(BigDecimal.ZERO);
			prpLClaimVo.setSumPaid(BigDecimal.ZERO);
			prpLClaimVo.setSumReplevy(BigDecimal.ZERO);
			prpLClaimVo.setSumChargeFee(BigDecimal.ZERO);
		}
		prpLClaimVo.setClaimTime(registerDate);//设置立案时间
		prpLClaimVo.setUpdateUser("AUTO");
		prpLClaimVo.setUpdateTime(new Date());
		this.saveOrUpdatePrpLClaimVo(prpLClaimVo);
		logger.info("报案号 registNo={},保单号={},结束自动立案保存数据方法",registNo,policyNo);
		return prpLClaimVo;
	}

	/**
	 * 刷新立案估损金额（平安联盟对接）
	 * @param dutyEstimateDTOList
	 */
	@Override
	public void updateClaimFeeForPingAnCase(String registNo, EstimateRespData estimateRespData) {
		logger.info("平安联盟未决变动信息================>registNo={}",registNo);

		List<DutyEstimateDTO> dutyEstimateDTOList = estimateRespData.getDutyEstimateList();
		//查询报案数据
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		//险别未决信息
		if (CollectionUtils.isNotEmpty(dutyEstimateDTOList)) {
			//先获取本次更新的立案记录
			Set<String> keys = new HashSet<String>();
			String adjustReason = "";
			for (DutyEstimateDTO dutyEstimateDTO : dutyEstimateDTOList) {
				keys.add(CodeConstants.PINGAN_UNION.POLICY_PREFIX + dutyEstimateDTO.getPolicyNo());
				adjustReason = EstimateTypeEnum.getDescByCode(dutyEstimateDTO.getEstimateType());
			}

			for (String policyNo : keys) {
				// 获取立案主表
				PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo, policyNo);
				if (prpLClaimVo == null) {
					prpLClaimVo = this.organizePrpLClaimVo(registNo,policyNo,prpLRegistVo.getFlowId(),CodeConstants.AutoPass.CLAIM);
					List<PrpLClaimKindVo> prpLClaimKindVoList = this.organizeClaimKindVo(registNo,policyNo,prpLClaimVo.getClaimNo(),CodeConstants.AutoPass.CLAIM);
					prpLClaimVo.setPrpLClaimKinds(prpLClaimKindVoList);
					// 估损金额
					BigDecimal sumDefLoss = BigDecimal.ZERO;
					BigDecimal sumClaim = BigDecimal.ZERO;
					for(PrpLClaimKindVo prpLClaimKindVo:prpLClaimKindVoList){
						sumDefLoss = sumDefLoss.add(DataUtils.NullToZero(prpLClaimKindVo.getDefLoss()));
						sumClaim = sumClaim.add(DataUtils.NullToZero(prpLClaimKindVo.getClaimLoss()));
					}
					prpLClaimVo.setSumDefLoss(sumDefLoss);
					prpLClaimVo.setClaimFlag(CodeConstants.ClaimFlag.AUTOCLAIM);//3-自动立案
					prpLClaimVo.setSumClaim(sumClaim);
					prpLClaimVo.setSumRescueFee(BigDecimal.ZERO);
					prpLClaimVo.setSumPaid(BigDecimal.ZERO);
					prpLClaimVo.setSumReplevy(BigDecimal.ZERO);
					prpLClaimVo.setSumChargeFee(BigDecimal.ZERO);
				}

				//判断估损次数
				if (prpLClaimVo.getEstiTimes() == null) {
					logger.error("立案" + prpLClaimVo.getClaimNo() + "估损次数为空，立案更新失败!");
					throw new IllegalArgumentException("立案" + prpLClaimVo.getClaimNo() + "估损次数为空，立案更新失败!");
				}

				//设置估损金额
				for (PrpLClaimKindVo claimKindVo : prpLClaimVo.getPrpLClaimKinds()) {
					for (DutyEstimateDTO dutyEstimateDTO : dutyEstimateDTOList) {
						String dutyCode = dutyEstimateDTO.getDutyCode();//险别代码  需转换成鼎和代码
						PiccCodeDictVo piccCodeDictVo = pingAnDictService.getDictData(PingAnCodeTypeEnum.XB.getCodeType(), dutyCode);
						if (piccCodeDictVo == null || StringUtils.isBlank(piccCodeDictVo.getDhCodeCode())) {
							throw new IllegalArgumentException("未映射到鼎和险别代码!");
						}
						dutyCode = piccCodeDictVo.getDhCodeCode();

						//交强险
						if (Risk.isDQZ(claimKindVo.getRiskCode())){
							if (dutyCode.equals(claimKindVo.getKindCode()) && claimKindVo.getLossItemName().equals(piccCodeDictVo.getPiccCodeName())){
								if (dutyCode.equals(claimKindVo.getKindCode())) {
									claimKindVo.setDefLoss(new BigDecimal(dutyEstimateDTO.getDutyEstimateAmount()));//估损金额(核损)
									claimKindVo.setClaimLoss(new BigDecimal(dutyEstimateDTO.getDutyEstimateAmount()));//估计赔款（立案）
									break;
								}
							}
						}else {//商业险
							if (dutyCode.equals(claimKindVo.getKindCode())) {
								claimKindVo.setDefLoss(new BigDecimal(dutyEstimateDTO.getDutyEstimateAmount()));//估损金额(核损)
								claimKindVo.setClaimLoss(new BigDecimal(dutyEstimateDTO.getDutyEstimateAmount()));//估计赔款（立案）
								break;
							}
						}
					}
					claimKindVo.setNodeName(adjustReason);//调整环节
					claimKindVo.setAdjustReason(adjustReason);//调整原因
				}

				prpLClaimVo.setSumDefLoss(this.getSumDefLoss(prpLClaimVo));// 刷新车辆、财产、人伤的损失金额
				prpLClaimVo.setSumClaim(this.getSumClaim(prpLClaimVo));// 刷新车辆、财产、人伤的估计赔款
				prpLClaimVo.setUpdateUser("AUTO");
				prpLClaimVo.setUpdateTime(new Date());
				prpLClaimVo.setEstiTimes(prpLClaimVo.getEstiTimes() + 1);// 估损次数增加
				logger.debug("平安联盟未决变动信息-报案号：" + prpLClaimVo.getRegistNo() + "保单号：" + prpLClaimVo.getPolicyNo() + "prpLclaim.getClaimNo():" + prpLClaimVo.getClaimNo() + ",prpLclaim.getSumDefLoss():" + prpLClaimVo
						.getSumDefLoss() + ",prpLclaim.getSumClaim():" + prpLClaimVo.getSumClaim());
				this.saveOrUpdatePrpLClaimVo(prpLClaimVo);
				// liuping 2016年6月3日 保存立案信息后，计算并保存立案轨迹信息
				claimKindHisService.saveClaimKindHis(prpLClaimVo);

				//立案刷新送再保 立案送再保未决数据分摊试算   niuqiang
				try {
					List<PrpLClaimKindHisVo> kindHisVoList =
							claimKindHisService.findKindHisVoList(prpLClaimVo.getClaimNo(), "1");
					ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
					claimInterfaceLogVo.setClaimNo(prpLClaimVo.getClaimNo());
					claimInterfaceLogVo.setRegistNo(registNo);
					claimInterfaceLogVo.setComCode(prpLClaimVo.getComCode());
					claimInterfaceLogVo.setCreateUser("AUTO");
					claimInterfaceLogVo.setCreateTime(new Date());
					claimInterfaceLogVo.setOperateNode(FlowNode.Claim.getName());
					if (kindHisVoList != null && kindHisVoList.size() > 0) {
						interfaceAsyncService.TransDataForClaimVo(prpLClaimVo,
								kindHisVoList, claimInterfaceLogVo);
					}
				} catch (Exception e) {
					logger.error("平安联盟未决变动信息-报案号=" + registNo + "立案送再保未决数据分摊试算失败", e);
				}
			}
		}
	}


	/**
	 * 组织PrpLClaimVo基础数据(初始化)
	 *
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月1日 下午2:53:08): <br>
	 */
	private PrpLClaimVo organizePrpLClaimVo(String registNo,String policyNo,String flowId,String createUser) {
		PrpLClaimVo prpLClaimVo = null;
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		if(prpLCMainVo!=null) {
			logger.info("平安联盟未决变动信息prplcmain不为空！");
		}else {
			logger.info("平安联盟未决变动信息prplcmain==null");
		}
		logger.info("平安联盟未决变动信息----报案号=={}---保单号==={}",registNo,policyNo);
		prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);
		if(prpLClaimVo != null){
			logger.error("该保单立案已经存在，不能进行强制立案，报案号："+registNo+"，保单号:"+policyNo);
			throw new BusinessException("改保单立案已经存在，不能进行强制立案，报案号："+registNo+"，保单号:"+policyNo,false);
		}
		prpLClaimVo = new PrpLClaimVo();
		String claimNo = billNoService.getClaimNo(prpLCMainVo.getComCode(),prpLCMainVo.getRiskCode());
		prpLClaimVo.setClaimNo(claimNo);
		prpLClaimVo.setFlowId(flowId);
		prpLClaimVo.setRegistNo(registNo);
		prpLClaimVo.setPolicyNo(policyNo);
		prpLClaimVo.setComCode(prpLCMainVo.getComCode());
		prpLClaimVo.setRiskCode(prpLCMainVo.getRiskCode());
		prpLClaimVo.setClassCode(prpLCMainVo.getClassCode());
		prpLClaimVo.setReportTime(prpLRegistVo.getReportTime());
		prpLClaimVo.setClaimTime(new Date());
		prpLClaimVo.setDamageTime(prpLRegistVo.getDamageTime());
		prpLClaimVo.setDamageCode(prpLRegistVo.getDamageCode());//出险原因
		prpLClaimVo.setDamageTypeCode(prpLRegistVo.getPrpLRegistExt().getAccidentTypes());//事故类型
		prpLClaimVo.setCiIndemDuty("1");//强制立案默认有责
		prpLClaimVo.setIndemnityDuty("0");//商业全责
		prpLClaimVo.setIndemnityDutyRate(BigDecimal.valueOf(100.00));
		prpLClaimVo.setDeductibleRate(BigDecimal.ZERO);
		prpLClaimVo.setDamageAreaCode(prpLRegistVo.getDamageAreaCode());
		prpLClaimVo.setClaimType(CodeConstants.ClaimType.NORMAL);
		prpLClaimVo.setIsTotalLoss("0");//是否全损
		prpLClaimVo.setIsSubRogation(prpLRegistVo.getPrpLRegistExt().getIsSubRogation());
		prpLClaimVo.setCaseFlag(prpLRegistVo.getCaseFlag());//互碰自赔标识
		prpLClaimVo.setMercyFlag(prpLRegistVo.getMercyFlag());//案件紧急程度
		prpLClaimVo.setIsAlarm(prpLRegistVo.getPrpLRegistExt().getIsAlarm());//是否报警
		prpLClaimVo.setTpFlag(prpLRegistVo.getTpFlag());//通赔标识
		prpLClaimVo.setIsMajorCase(prpLRegistVo.getIsMajorCase());//是否重大案件
		prpLClaimVo.setValidFlag("1");
		prpLClaimVo.setCreateUser(createUser);
		prpLClaimVo.setCreateTime(new Date());
		prpLClaimVo.setUpdateUser(createUser);
		prpLClaimVo.setUpdateTime(new Date());
		prpLClaimVo.setEstiTimes(0);
		return prpLClaimVo;
	}

	/**
	 * 组织PrpLClaimKindVo数据(初始化)
	 *
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @param createUser 创建人
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月1日 下午2:53:53): <br>
	 */
	private List<PrpLClaimKindVo> organizeClaimKindVo(String registNo,String policyNo,String claimNo,String createUser) {
		List<PrpLClaimKindVo> prpLClaimKindVoList = new ArrayList<PrpLClaimKindVo>();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo,policyNo);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(prpLRegistVo.getDamageTime());
		Integer demageYear = Integer.parseInt(dateString.substring(0,4));
		String comCode = prpLCMainVo.getComCode();
		// 强制立案
		List<PrpLCItemKindVo> prpLCItemKindVoList = policyViewService.findItemKindVoListByRegistNoAndPolicyNo(registNo,null);
		if(prpLCItemKindVoList==null||prpLCItemKindVoList.isEmpty()){
			logger.error("案件无对应的险别信息，报案号为："+registNo);
			throw new BusinessException("案件无对应的险别信息，报案号为："+registNo,false);
		}
		for(PrpLCItemKindVo prpLCItemKindVo:prpLCItemKindVoList){
			if("1101".equals(prpLCMainVo.getRiskCode())){
				if(prpLCItemKindVo.getRiskCode().startsWith("11")){
					List<PrpLDLimitVo> prpLDLimitVoList = configService.findPrpLDLimitList(CodeConstants.DutyType.CIINDEMDUTY_Y,registNo);// 交强险责任限额表，默认有责
					for(PrpLDLimitVo prpLDLimitVo:prpLDLimitVoList){
						PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
						prpLClaimKindVo.setRegistNo(prpLRegistVo.getRegistNo());
						prpLClaimKindVo.setClaimNo(claimNo);
						prpLClaimKindVo.setRiskCode(prpLCItemKindVo.getRiskCode());
						prpLClaimKindVo.setNodeName("报案");
						prpLClaimKindVo.setKindCode(prpLCItemKindVo.getKindCode());
						prpLClaimKindVo.setKindName(prpLCItemKindVo.getKindName());
						prpLClaimKindVo.setFeeType(CodeConstants.FeeType.PAY);
						prpLClaimKindVo.setCurrency("CNY");
						prpLClaimKindVo.setExchRate(BigDecimal.ONE);
						prpLClaimKindVo.setPaidLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setDeductible(BigDecimal.ZERO);
						prpLClaimKindVo.setDeductibleRate(BigDecimal.ZERO);
						prpLClaimKindVo.setAdjustTime(new Date());
						//prpLClaimKindVo.setAdjustReason(adjustReason);
						prpLClaimKindVo.setCompStatus("0");
						prpLClaimKindVo.setCancelFlag("0");
						prpLClaimKindVo.setValidFlag("1");
						prpLClaimKindVo.setCreateTime(new Date());
						prpLClaimKindVo.setCreateUser(createUser);
						prpLClaimKindVo.setUpdateTime(new Date());
						prpLClaimKindVo.setUpdateUser(createUser);
						prpLClaimKindVo.setLossItemNo(prpLDLimitVo.getLossItemNo());
						prpLClaimKindVo.setLossItemName(prpLDLimitVo.getLossItemName());
						prpLClaimKindVo.setAmount(prpLDLimitVo.getLimitFee());
						prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
						prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
						prpLClaimKindVoList.add(prpLClaimKindVo);
					}
				}
			}else{
				if(prpLCItemKindVo.getRiskCode().startsWith("12")){
					PrpLClaimKindVo prpLClaimKindVo = new PrpLClaimKindVo();
					prpLClaimKindVo.setRegistNo(prpLRegistVo.getRegistNo());
					// prpLClaimKindVo.setClaimNo(prpLClaimVo.getClaimNo());
					prpLClaimKindVo.setRiskCode(prpLCItemKindVo.getRiskCode());
					prpLClaimKindVo.setNodeName("报案");
					prpLClaimKindVo.setLossItemName("——");
					prpLClaimKindVo.setKindCode(prpLCItemKindVo.getKindCode());
					prpLClaimKindVo.setKindName(prpLCItemKindVo.getKindName());
					prpLClaimKindVo.setFeeType(CodeConstants.FeeType.PAY);
					prpLClaimKindVo.setCurrency("CNY");
					prpLClaimKindVo.setExchRate(BigDecimal.ONE);
					prpLClaimKindVo.setPaidLoss(BigDecimal.ZERO);
					prpLClaimKindVo.setDeductible(BigDecimal.ZERO);
					prpLClaimKindVo.setDeductibleRate(BigDecimal.ZERO);
					prpLClaimKindVo.setAdjustTime(new Date());
					//prpLClaimKindVo.setAdjustReason(adjustReason);
					prpLClaimKindVo.setCompStatus("0");
					prpLClaimKindVo.setCancelFlag("0");
					prpLClaimKindVo.setValidFlag("1");
					prpLClaimKindVo.setCreateTime(new Date());
					prpLClaimKindVo.setCreateUser(createUser);
					prpLClaimKindVo.setUpdateTime(new Date());
					prpLClaimKindVo.setUpdateUser(createUser);
					//TODO 第三者责任保险要不要拆分车物和人，目前平安送的险别未决区分不了车物和人
					/*if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLCItemKindVo.getKindCode().trim())){
						prpLClaimKindVo.setLossItemName("车物");
						prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
						prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
						prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
						prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						prpLClaimKindVoList.add(prpLClaimKindVo);

						PrpLClaimKindVo claimVo = new PrpLClaimKindVo();
						Beans.copy().from(prpLClaimKindVo).to(claimVo);
						claimVo.setLossItemName("人");
						claimVo.setAmount(prpLCItemKindVo.getAmount());
						claimVo.setDefLoss(BigDecimal.ZERO);
						claimVo.setRescueFee(BigDecimal.ZERO);
						claimVo.setRestFee(BigDecimal.ZERO);
						claimVo.setClaimLoss(BigDecimal.ZERO);
						prpLClaimKindVoList.add(claimVo);

					}else {
						prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
						prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
						prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
						prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
						prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
						prpLClaimKindVoList.add(prpLClaimKindVo);
					}*/
					prpLClaimKindVo.setAmount(prpLCItemKindVo.getAmount());
					prpLClaimKindVo.setDefLoss(BigDecimal.ZERO);
					prpLClaimKindVo.setRescueFee(BigDecimal.ZERO);
					prpLClaimKindVo.setRestFee(BigDecimal.ZERO);
					prpLClaimKindVo.setClaimLoss(BigDecimal.ZERO);
					prpLClaimKindVoList.add(prpLClaimKindVo);
				}
			}
		}
		return prpLClaimKindVoList;
	}
}
