package ins.sino.claimcar.claim.calculator;

import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.CalculatorService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskExtService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.ClaimFeeCondition;
import ins.sino.claimcar.claim.vo.CompensateExp;
import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.DefLossInfoOfA;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.ThirdPartyInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyRecoveryInfo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交强险赔款计算器
 * 
 */
@Service("calculatorCI")
public class CalculatorCI implements Calculator{
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(CalculatorCI.class);
	@Autowired
	private RegistQueryService registQueryService;//报案Service
	@Autowired
	private ClaimService claimService;//立案service
	@Autowired
    private CheckTaskService checkTaskService;
	@Autowired
    private CompensateTaskService compensateTaskService;//理算
	@Autowired
    private CalculatorService calculatorService;
	@Autowired
	private  PolicyViewService policyViewService;
	@Autowired
	private LossCarService lossCarService;// 车辆定损
	@Autowired
	private PropTaskService propTaskService;// 财产定损
	@Autowired
	private PersTraceDubboService persTraceDubboService;
	@Autowired
	private CompensateService compensateService;
	@Autowired
	private ClaimTaskExtService claimTaskExtService;

	/**
	 * 交强险赔款计算
	 */
	public CompensateListVo calculate(CompensateListVo compensateListVo) {
		PrpLCMainVo prpLCMainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(
				compensateListVo.getPrpLCItemKindVo().getRegistNo(),compensateListVo.getPrpLCItemKindVo().getPolicyNo());
		ClaimFeeCondition claimCond = compensateListVo.getClaimFeeCondition();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLCMainVo.getRegistNo());
		PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(prpLRegistVo.getRegistNo());
		PrpLClaimVo prpLClaimVo = new PrpLClaimVo();
		prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLRegistVo.getRegistNo(), prpLCMainVo.getPolicyNo());
		if(prpLClaimVo==null) {
			PrpLClaimVo prpLclaimTemp = new PrpLClaimVo();
			prpLclaimTemp.setClaimType(claimCond.getClaimType());
			prpLclaimTemp.setRegistNo(prpLRegistVo.getRegistNo());
			prpLclaimTemp.setPolicyNo(claimCond.getPolicyNo());
			prpLclaimTemp.setClaimType(claimCond.getClaimType());
			prpLclaimTemp.setIndemnityDuty(claimCond.getIndemnityDuty());
			prpLclaimTemp.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
			prpLclaimTemp.setCiIndemDuty(claimCond.getCiDutyFlag());
			prpLclaimTemp.setComCode(prpLCMainVo.getComCode());
//			prpLclaimTemp.setMakeCom(claimCond.getMakeCom());
//			prpLclaimTemp.setLflag(prpLRegistVo.getLflag());
			prpLclaimTemp.setDamageTime(prpLRegistVo.getDamageTime());
			prpLclaimTemp.setRiskCode(prpLCMainVo.getRiskCode());
			prpLClaimVo = prpLclaimTemp;
		}
		prpLClaimVo.setCiIndemDuty(claimCond.getCiDutyFlag());
		prpLClaimVo.setClaimType(claimCond.getClaimType());
		CompensateVo compensateVo=calculatorService.orgnizeCalculateData(prpLRegistVo, prpLCMainVo, prpLCheckVo, prpLClaimVo,"2");//损失组织
		
		// 根据车财损失计算车损占比字段值carLossRate
		BigDecimal carLossRate = claimTaskExtService.getCarLossRate(compensateVo);
				
		CompensateListVo returnCompensateList=this.calCulatorCi(compensateVo,prpLClaimVo);
		returnCompensateList.setClaimFeeCondition(compensateListVo.getClaimFeeCondition());
		returnCompensateList.setPrpLCItemKindVo(compensateListVo.getPrpLCItemKindVo());
		returnCompensateList.setCarLossRate(carLossRate);
		return returnCompensateList;
	}

	/**
	 * 交强试算
	 */
	public CompensateListVo calCulator(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
	                                   List<PrpLLossPersonVo> prpLLossPersonVoList,String compensateType){
		
		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLCompensateVo.getRegistNo(), prpLCompensateVo.getPolicyNo());
		CompensateVo compensateVo=calculatorService.orgnizeCompensateData(prpLCompensateVo,prpLLossItemVoList,prpLLossPropVoList,
				prpLLossPersonVoList,prpLClaimVo,compensateType);
		CompensateListVo returnCompensateList=this.calCulatorCi(compensateVo,prpLClaimVo);
		
		return returnCompensateList;
	}
	
	
	/**
	 * 交强规则入参数据整理——>交强理算前数据整理——>交强理算公式方法
	 */
	public CompensateListVo calCulatorCi(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo){
		Timestamp beginTime = new Timestamp(new Date().getTime());
		logger.debug("立案交强估计赔款理算开始，Start Time:" + beginTime);
		CompensateExp[] compensateExpArray = this.calCulatorCiData(compensateVo, prpLClaimVo);
		CompensateListVo compensateListVo = this.compensateList(compensateVo,compensateExpArray);
		compensateListVo.setRegistNo(prpLClaimVo.getRegistNo());
		CompensateListVo returnCompensateList = this.calculateCi(compensateListVo);
		return returnCompensateList;
	}
	/**
	 * 交强计算前的规则参数组织
	 */
	public CompensateExp[] calCulatorCiData(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo) {
		List<CompensateExp> compensateExpAry = new ArrayList<CompensateExp>(0);
		List<ThirdPartyInfo> thirdPartyInfos = new ArrayList<ThirdPartyInfo>(0);
		List<ThirdPartyLossInfo> cpThirdPartyLossInfos = new ArrayList<ThirdPartyLossInfo>(0);
		List<ThirdPartyLossInfo> iThirdPartyLossInfos = new ArrayList<ThirdPartyLossInfo>(0);
		List<ThirdPartyLossInfo> ddThirdPartyLossInfos = new ArrayList<ThirdPartyLossInfo>(0);
		ThirdPartyInfo thirdPartyInfoTemp; // 组织涉案信息
		ThirdPartyLossInfo cpThirdPartyLossInfoTemp; // 车,财损失信息
		ThirdPartyLossInfo iThirdPartyLossInfoTemp;//医疗费
		ThirdPartyLossInfo ddThirdPartyLossInfoTemp;//死亡
		PrpLCompensateVo prpLCompensateVo = compensateVo.getPrpLCompensateVo();
		List<PrpLLossItemVo> prpLLossItemVoList = compensateVo.getPrpLLossItemVoList(); // 车辆损失项信息
		List<PrpLLossPropVo> prpLLossPropVoList = prpLCompensateVo.getPrpLLossProps(); // 财产损失项信息
		List<PrpLLossPersonVo> prpLLossPersonVoList = prpLCompensateVo.getPrpLLossPersons(); // 人员损失项信息
		List<DefLossInfoOfA> carDefLossInfoOfAList = compensateVo.getCarDefLossInfoOfAList();// 属于本车车辆的定损信息
		List<DefLossInfoOfA> propDefLossInfoOfAList = compensateVo.getPropDefLossInfoOfAList();// 属于本车财产的定损信息
		// 组合过的所有涉案车辆信息
		List<PrpLDlossCarInfoVo> prpLDlossCarInfoVoList = calculatorService.getThirdPartiesInfo(prpLClaimVo, prpLClaimVo.getRegistNo(), "2");
		
		// 交强试算前的数据组织
		if (prpLDlossCarInfoVoList != null && !prpLDlossCarInfoVoList.isEmpty()) {
			//第一条为标的车
			PrpLDlossCarInfoVo carInfoVo = prpLDlossCarInfoVoList.get(0);
			PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(carInfoVo.getRegistNo(),carInfoVo.getSerialNo());
			thirdPartyInfoTemp = new ThirdPartyInfo();
			thirdPartyInfoTemp.setLicenseNo(carInfoVo.getId().toString());// 涉案表id
			thirdPartyInfoTemp.setBzDutyType(prpLCheckDutyVo.getCiDutyFlag());// 交强有责无责
			if(prpLCheckDutyVo.getCiDutyFlag()!=null && prpLCheckDutyVo.getCiDutyFlag().trim().equals("1")){
				thirdPartyInfoTemp.setBzDutyFlag(true);
			}
			
			thirdPartyInfoTemp.setInsuredFlag("1");// 承保标志 默认承保了
			thirdPartyInfoTemp.setLossItemType(carInfoVo.getLossItemType());// 损失类型
			thirdPartyInfoTemp.setSerialNo(carInfoVo.getSerialNo());//车辆序号
			thirdPartyInfos.add(thirdPartyInfoTemp);
			
			for (PrpLDlossCarInfoVo prpLDlossCarInfoVo : prpLDlossCarInfoVoList) {
				if (prpLDlossCarInfoVo.getSerialNo() > 1){
					//取统一的车辆责任表
					prpLCheckDutyVo = checkTaskService.findCheckDuty(prpLDlossCarInfoVo.getRegistNo(),prpLDlossCarInfoVo.getSerialNo());
					thirdPartyInfoTemp = new ThirdPartyInfo();
					thirdPartyInfoTemp.setLicenseNo(prpLDlossCarInfoVo.getId().toString());// 涉案表id
					thirdPartyInfoTemp.setBzDutyType(prpLCheckDutyVo.getCiDutyFlag());// 交强有责无责
					thirdPartyInfoTemp.setSerialNo(prpLDlossCarInfoVo.getSerialNo());//车辆序号
					if(prpLCheckDutyVo.getCiDutyFlag()!=null && prpLCheckDutyVo.getCiDutyFlag().trim().equals("1")){
						thirdPartyInfoTemp.setBzDutyFlag(true);
					}else{//无责   是否无责代赔 商业交强 ，正常扣除，交强在理算拆分时确定是否展示  by yk 20161230
 						thirdPartyInfoTemp.setNodutyPayFlag("1");
//						if("claim".equals(compensateVo.getCalculateType())){
//							thirdPartyInfoTemp.setNodutyPayFlag("1");
//						}else{
//							if("2".equals(prpLCompensateVo.getCompensateKind())){//交强
//								if (prpLLossItemVoList != null && !prpLLossItemVoList.isEmpty()) {
//									for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVoList) {
//										if(thirdPartyInfoTemp.getSerialNo().toString().equals(prpLLossItemVo.getItemId().trim())){
//											thirdPartyInfoTemp.setNodutyPayFlag(prpLLossItemVo.getNoDutyPayFlag());
//										}
//									}
//								}
//							}else{
//								thirdPartyInfoTemp.setNodutyPayFlag("1");
//							}
//						}
					}
					
					thirdPartyInfoTemp.setInsuredFlag("1");// 承保标志
					thirdPartyInfoTemp.setLossItemType(prpLDlossCarInfoVo.getLossItemType());// 损失类型
					thirdPartyInfos.add(thirdPartyInfoTemp);
				}
			}
		}
		int cpIndex = 0;//车,财个数
		int objectIndex = 0;
		int iIndex=0;
		int ddIndex=0;
    	// 车辆损失项信息  thirdPartyInfos
		if (prpLLossItemVoList != null && !prpLLossItemVoList.isEmpty()) {
			for (PrpLLossItemVo prpLLossItemVo : prpLLossItemVoList) {
				// 定损小计的损失项信息
				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
				BigDecimal rescueFee = DataUtils.NullToZero(prpLLossItemVo.getRescueFee());
				BigDecimal loss = DataUtils.NullToZero(prpLLossItemVo.getSumLoss()).add(rescueFee);
				
				cpThirdPartyLossInfoTemp.setRescueFeeFlag("0");//sumLoss为损失金额-残值
				cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
				cpThirdPartyLossInfoTemp.setRescueFee(rescueFee.doubleValue());
				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
				cpThirdPartyLossInfoTemp.setLicenseNo(prpLLossItemVo.getDlossId().toString());
				cpThirdPartyLossInfoTemp.setSerialNo(Integer.valueOf(prpLLossItemVo.getItemId()));
				cpThirdPartyLossInfoTemp.setLossIndex(prpLLossItemVo.getDlossId().toString());
				
				PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
				if(prpLDlossCarMainVo != null && prpLDlossCarMainVo.getLossCarInfoVo() != null){
					// 获得承保机构
					cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarMainVo.getLossCarInfoVo().getInsureComCode());
				}
				cpThirdPartyLossInfoTemp.setLossName(prpLLossItemVo.getItemName());
				cpThirdPartyLossInfoTemp.setLossFeeName("车辆定损金额");
				cpThirdPartyLossInfoTemp.setExpType("car");
				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
				if (prpLLossItemVo.getSumRealPay() != null) {
					cpThirdPartyLossInfoTemp.setSumRealPayJud(prpLLossItemVo.getSumRealPay().doubleValue());
				} else {
					cpThirdPartyLossInfoTemp.setSumRealPayJud(0.0);
				}
//				cpThirdPartyLossInfoTemp.setItemKindNo(prpLLossItemVo.getItemKindNo());
				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);

				// 相应的施救费信息
//				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
//				if (prpLLossItemVo.getRescueFee() != null) {
//					cpThirdPartyLossInfoTemp.setSumLoss(prpLLossItemVo.getRescueFee().doubleValue());
//				} else {
//					cpThirdPartyLossInfoTemp.setSumLoss(0);
//				}
//				cpThirdPartyLossInfoTemp.setRescueFeeFlag("1");//表示施救费
//				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
//				cpThirdPartyLossInfoTemp.setLicenseNo(prpLLossItemVo.getDlossId().toString());
//				cpThirdPartyLossInfoTemp.setSerialNo(Integer.valueOf(prpLLossItemVo.getItemId()));
//				cpThirdPartyLossInfoTemp.setLossIndex(prpLLossItemVo.getDlossId().toString());
//				if(prpLDlossCarMainVo != null && prpLDlossCarMainVo.getLossCarInfoVo() != null){
//					// 获得承保机构
//					cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarMainVo.getLossCarInfoVo().getInsureComCode());
//				}
//				cpThirdPartyLossInfoTemp.setLossName(prpLLossItemVo.getItemName());
//				cpThirdPartyLossInfoTemp.setLossFeeName("施救费");
//				cpThirdPartyLossInfoTemp.setExpType("car");
//				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
////				cpThirdPartyLossInfoTemp.setItemKindNo(prpLLossItemVo.getItemKindNo());
//				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);
				cpIndex++;
			}
		}
		
		//本车车辆损失
		if (carDefLossInfoOfAList != null && !carDefLossInfoOfAList.isEmpty()) {
			for (DefLossInfoOfA defLossInfoOfA : carDefLossInfoOfAList) {
				// 定损小计的损失项信息
				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
//				if (defLossInfoOfA.getSumLoss() != null) {
//					BigDecimal loss = defLossInfoOfA.getSumLoss().subtract(new BigDecimal(defLossInfoOfA.getSumRest()));
//					cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
//				} else {
//					cpThirdPartyLossInfoTemp.setSumLoss(0.00);
//				}
				BigDecimal rescueFee = DataUtils.NullToZero(defLossInfoOfA.getRescueFee());
				BigDecimal loss = DataUtils.NullToZero(defLossInfoOfA.getSumLoss()).add(rescueFee);
				cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
				cpThirdPartyLossInfoTemp.setRescueFee(rescueFee.doubleValue());
				cpThirdPartyLossInfoTemp.setRescueFeeFlag("0");//损失金额-残值
				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
				cpThirdPartyLossInfoTemp.setLicenseNo(defLossInfoOfA.getPrpLthirdPartyId());
				cpThirdPartyLossInfoTemp.setLossIndex(defLossInfoOfA.getLossIndex().toString());
				PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(defLossInfoOfA.getPrpLthirdPartyId()));
				if(prpLDlossCarMainVo != null){
					cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarMainVo.getLossCarInfoVo().getInsureComCode());
				}
				cpThirdPartyLossInfoTemp.setLossName(defLossInfoOfA.getLossName());
				cpThirdPartyLossInfoTemp.setLossFeeName("车辆定损金额");
				cpThirdPartyLossInfoTemp.setExpType("car");
				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
				cpThirdPartyLossInfoTemp.setSumRealPayJud(0.0);
				cpThirdPartyLossInfoTemp.setItemKindNo(null);
				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);

				// 相应的施救费信息
//				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
//				if (defLossInfoOfA.getRescueFee() != null) {
//					cpThirdPartyLossInfoTemp.setSumLoss(defLossInfoOfA
//							.getRescueFee().doubleValue());
//				} else {
//					cpThirdPartyLossInfoTemp.setSumLoss(0);
//				}
//				cpThirdPartyLossInfoTemp.setRescueFeeFlag("1");// 施救费
//				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
//				cpThirdPartyLossInfoTemp.setLicenseNo(defLossInfoOfA.getPrpLthirdPartyId());
//				cpThirdPartyLossInfoTemp.setLossIndex(defLossInfoOfA.getLossIndex().toString());
//				if(prpLDlossCarMainVo != null){
//					cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarMainVo.getLossCarInfoVo().getInsureComCode());
//				}
//				cpThirdPartyLossInfoTemp.setLossName(defLossInfoOfA.getLossName());
//				cpThirdPartyLossInfoTemp.setLossFeeName("施救费");
//				cpThirdPartyLossInfoTemp.setExpType("car");
//				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
//				cpThirdPartyLossInfoTemp.setItemKindNo(null);
//				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);
				cpIndex++;
			}
		}

		// 财产损失项信息
		if (prpLLossPropVoList != null && !prpLLossPropVoList.isEmpty()) {
			for (PrpLLossPropVo prpLLossPropVo : prpLLossPropVoList) {
				if("9".equals(prpLLossPropVo.getPropType())){
					continue;
				}
				// 定损小计的损失项信息
				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
//				if(prpLLossPropVo.getSumLoss() != null) {
//					BigDecimal loss = prpLLossPropVo.getSumLoss();
//					if(prpLLossPropVo.getOtherDeductAmt() != null) {
//						loss = prpLLossPropVo.getSumLoss().subtract(prpLLossPropVo.getOtherDeductAmt());
//					} 
//					if(prpLLossPropVo.getSumRest() != null) {
//						loss = loss.subtract(prpLLossPropVo.getSumRest());
//					}
//					cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
//				} else {
//					cpThirdPartyLossInfoTemp.setSumLoss(0.00);
//				}
				
				BigDecimal rescueFee = DataUtils.NullToZero(prpLLossPropVo.getRescueFee());
				BigDecimal loss = DataUtils.NullToZero(prpLLossPropVo.getSumLoss()).add(rescueFee);
				cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
				cpThirdPartyLossInfoTemp.setRescueFee(rescueFee.doubleValue());
				if(prpLLossPropVo.getLossType()!=null) {
					cpThirdPartyLossInfoTemp.setLossItemType(prpLLossPropVo.getLossType());
				}
				
//				cpThirdPartyLossInfoTemp.setSumRest(prpLLossPropVo.getSumRest().doubleValue());
				cpThirdPartyLossInfoTemp.setRescueFeeFlag("0");//损失金额-残值
				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
				cpThirdPartyLossInfoTemp.setLicenseNo(prpLLossPropVo.getDlossId().toString());
				cpThirdPartyLossInfoTemp.setSerialNo(new Integer(prpLLossPropVo.getItemId()));
				cpThirdPartyLossInfoTemp.setLossIndex(prpLLossPropVo.getDlossId().toString());
				PrpLdlossPropMainVo prpLdlossPropMainVo = propTaskService.findPropMainVoById(prpLLossPropVo.getDlossId());
				if(prpLdlossPropMainVo != null){
					cpThirdPartyLossInfoTemp.setInsureComCode(prpLdlossPropMainVo.getComCode());
				}
				cpThirdPartyLossInfoTemp.setLossName(prpLLossPropVo.getItemName());
				cpThirdPartyLossInfoTemp.setLossFeeName("财产定损金额");
				cpThirdPartyLossInfoTemp.setExpType("prop");
				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
				if (prpLLossPropVo.getSumRealPay() != null) {
					cpThirdPartyLossInfoTemp.setSumRealPayJud(prpLLossPropVo.getSumRealPay().doubleValue());
				} else {
					cpThirdPartyLossInfoTemp.setSumRealPayJud(0.00);
				}
				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);

				// 相应的施救费信息
//				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
//				if (prpLLossPropVo.getRescueFee() != null) {
//					cpThirdPartyLossInfoTemp.setSumLoss(prpLLossPropVo.getRescueFee().doubleValue());
//				} else {
//					cpThirdPartyLossInfoTemp.setSumLoss(0);
//				}
//				cpThirdPartyLossInfoTemp.setRescueFeeFlag("1");//施救费
//				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
//				cpThirdPartyLossInfoTemp.setLicenseNo(prpLLossPropVo.getDlossId().toString());
//				cpThirdPartyLossInfoTemp.setSerialNo(new Integer(prpLLossPropVo.getItemId()));
//				cpThirdPartyLossInfoTemp.setLossIndex(prpLLossPropVo.getDlossId().toString());
//				if(prpLdlossPropMainVo != null){
//					cpThirdPartyLossInfoTemp.setInsureComCode(prpLdlossPropMainVo.getComCode());
//				}
//				if(prpLLossPropVo.getLossType()!=null) {
//					cpThirdPartyLossInfoTemp.setLossItemType(prpLLossPropVo.getLossType());
//				}
//				cpThirdPartyLossInfoTemp.setLossName(prpLLossPropVo.getItemName());
//				cpThirdPartyLossInfoTemp.setLossFeeName("施救费");
//				cpThirdPartyLossInfoTemp.setExpType("prop");
//				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
////				cpThirdPartyLossInfoTemp.setItemKindNo(prpLLossPropVo.getItemKindNo());
//				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);
				cpIndex++;
			}
		}
        //本车财产损失
		if (propDefLossInfoOfAList != null) {
			for (DefLossInfoOfA defLossInfoOfA : propDefLossInfoOfAList) {
				// 定损小计的损失项信息
				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
				if (defLossInfoOfA.getSumLoss() != null) {
					BigDecimal loss = defLossInfoOfA.getSumLoss().subtract(new BigDecimal(defLossInfoOfA.getSumRest()));
					cpThirdPartyLossInfoTemp.setSumLoss(loss.doubleValue());
				} else {
					cpThirdPartyLossInfoTemp.setSumLoss(0.00);
				}
				cpThirdPartyLossInfoTemp.setRescueFeeFlag("0");
				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
				cpThirdPartyLossInfoTemp.setLicenseNo(defLossInfoOfA.getPrpLthirdPartyId());
				cpThirdPartyLossInfoTemp.setLossIndex(defLossInfoOfA.getLossIndex().toString());
				PrpLdlossPropMainVo prpLdlossPropMainVo = propTaskService.findPropMainVoById(Long.parseLong(defLossInfoOfA.getPrpLthirdPartyId()));
				if(prpLdlossPropMainVo != null){
					cpThirdPartyLossInfoTemp.setInsureComCode(prpLdlossPropMainVo.getComCode());
				}
//				if (prpLDlossCarInfoVoList != null && !prpLDlossCarInfoVoList.isEmpty()) {
//					for (PrpLDlossCarInfoVo prpLDlossCarInfoVo : prpLDlossCarInfoVoList) {
//						if (prpLDlossCarInfoVo.getId().toString().equals(cpThirdPartyLossInfoTemp.getLicenseNo())) {
//							cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarInfoVo.getInsureComCode());// 获得承保机构
//							break;
//						} else {
//							cpThirdPartyLossInfoTemp.setInsureComCode("");
//						}
//					}
//				}
				cpThirdPartyLossInfoTemp.setLossName(defLossInfoOfA.getLossName());
				cpThirdPartyLossInfoTemp.setLossFeeName("定损金额");
				cpThirdPartyLossInfoTemp.setExpType("prop");
				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
				cpThirdPartyLossInfoTemp.setSumRealPayJud(0);
				cpThirdPartyLossInfoTemp.setItemKindNo(null);
				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);

				// 相应的施救费信息
				cpThirdPartyLossInfoTemp = new ThirdPartyLossInfo();
				if (defLossInfoOfA.getRescueFee() != null) {
					cpThirdPartyLossInfoTemp.setSumLoss(defLossInfoOfA.getRescueFee().doubleValue());
				} else {
					cpThirdPartyLossInfoTemp.setSumLoss(0);
				}
				cpThirdPartyLossInfoTemp.setRescueFeeFlag("1");
				cpThirdPartyLossInfoTemp.setObjectIndex((objectIndex++)+"");
				cpThirdPartyLossInfoTemp.setLicenseNo(defLossInfoOfA.getPrpLthirdPartyId());
				if(prpLdlossPropMainVo != null){
					cpThirdPartyLossInfoTemp.setInsureComCode(prpLdlossPropMainVo.getComCode());
				}
				cpThirdPartyLossInfoTemp.setLossIndex(defLossInfoOfA.getLossIndex().toString());
//				if (prpLDlossCarInfoVoList != null && !prpLDlossCarInfoVoList.isEmpty()) {
//					for (PrpLDlossCarInfoVo prpLDlossCarInfoVo : prpLDlossCarInfoVoList) {
//						if (prpLDlossCarInfoVo.getId().toString().equals(cpThirdPartyLossInfoTemp.getLicenseNo())) {
//							cpThirdPartyLossInfoTemp.setInsureComCode(prpLDlossCarInfoVo.getInsureComCode());// 获得承保机构
//							break;
//						} else {
//							cpThirdPartyLossInfoTemp.setInsureComCode("");
//						}
//					}
//				}
				cpThirdPartyLossInfoTemp.setLossName(defLossInfoOfA.getLossName());
				cpThirdPartyLossInfoTemp.setLossFeeName("施救费");
				cpThirdPartyLossInfoTemp.setExpType("prop");
				cpThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS);
				cpThirdPartyLossInfoTemp.setItemKindNo(null);
				cpThirdPartyLossInfos.add(cpThirdPartyLossInfoTemp);
				cpIndex++;
			}
		}
		
//		prpLLossPersonFeeVoList.add(medFeeVo);
//		prpLLossPersonFeeVoList.add(dehFeeVo);
//		prpLLossPersonVo.setPrpLLossPersonFees(prpLLossPersonFeeVoList);

		if(prpLLossPersonVoList !=null && prpLLossPersonVoList.size()>0) {
			for(PrpLLossPersonVo prpLLossPersonVo:prpLLossPersonVoList){
				iThirdPartyLossInfoTemp = new ThirdPartyLossInfo();// 医疗
				ddThirdPartyLossInfoTemp = new ThirdPartyLossInfo();// 死亡
				PrpLDlossPersTraceVo prpLDlossPersTraceVo = persTraceDubboService.findPersTraceByPK(prpLLossPersonVo.getPersonId());
				for(PrpLLossPersonFeeVo personFeeVo : prpLLossPersonVo.getPrpLLossPersonFees()){
					if(personFeeVo.getLossItemNo().equals(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES)){// 医疗
						if(personFeeVo.getFeeLoss()!=null){
							iThirdPartyLossInfoTemp.setSumLoss(personFeeVo.getFeeLoss().doubleValue());
						}
						iThirdPartyLossInfoTemp.setObjectIndex(( objectIndex++ )+"");
						iThirdPartyLossInfoTemp.setLicenseNo(prpLLossPersonVo.getPersonId().toString());
						iThirdPartyLossInfoTemp.setSerialNo(new Integer(prpLLossPersonVo.getItemId()));
						iThirdPartyLossInfoTemp.setInsureComCode(prpLDlossPersTraceVo.getComCode());
//						if(prpLLossPersonVo.getItemId() !=null && ( prpLLossPersonVo.getLossType().trim()
//								.equals(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS)||prpLLossPersonVo.getLossType().trim()
//								.equals(CodeConstants.LossFeeType.THIS_PERSON_LOSS) )){
//							for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfos){
//								if(thirdPartyInfo.getLossItemType().equals("050")){
//									iThirdPartyLossInfoTemp.setLicenseNo(thirdPartyInfo.getLicenseNo());
//									break;
//								}
//							}
//						}
						iThirdPartyLossInfoTemp.setLossName(prpLLossPersonVo.getPersonName());
						iThirdPartyLossInfoTemp.setLossFeeName("医疗费");
						iThirdPartyLossInfoTemp.setExpType("person");
						iThirdPartyLossInfoTemp.setRescueFeeFlag("0");
						iThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES);
						iThirdPartyLossInfoTemp.setSumRealPayJud(0);
						iThirdPartyLossInfoTemp.setLossIndex(prpLLossPersonVo.getPersonId().toString());
//						iThirdPartyLossInfoTemp.setItemKindNo(prpLLossPersonVo.getItemKindNo());
						iThirdPartyLossInfos.add(iThirdPartyLossInfoTemp);
						iIndex++ ;
					}else{
						if(personFeeVo.getFeeLoss()!=null){
							ddThirdPartyLossInfoTemp.setSumLoss(personFeeVo.getFeeLoss().doubleValue());
						}
						ddThirdPartyLossInfoTemp.setObjectIndex(( objectIndex++ )+"");
						ddThirdPartyLossInfoTemp.setLicenseNo(prpLLossPersonVo.getDlossId().toString());
						ddThirdPartyLossInfoTemp.setSerialNo(new Integer(prpLLossPersonVo.getItemId()));
						ddThirdPartyLossInfoTemp.setInsureComCode(prpLDlossPersTraceVo.getComCode());
//						if(prpLLossPersonVo.getLossType()!=null&&( prpLLossPersonVo.getLossType().trim()
//								.equals(CodeConstants.LossFeeType.THIS_PERSON_DRIVER_LOSS)||prpLLossPersonVo.getLossType().trim()
//								.equals(CodeConstants.LossFeeType.THIS_PERSON_LOSS) )){
//							for(ThirdPartyInfo thirdPartyInfo:thirdPartyInfos){
//								if(thirdPartyInfo.getLossItemType().equals("050")){
//									ddThirdPartyLossInfoTemp.setLicenseNo(thirdPartyInfo.getLicenseNo());
//									break;
//								}
//							}
//						}
						ddThirdPartyLossInfoTemp.setLossName(prpLLossPersonVo.getPersonName());
						ddThirdPartyLossInfoTemp.setLossFeeName("死亡伤残");
						ddThirdPartyLossInfoTemp.setExpType("person");
						ddThirdPartyLossInfoTemp.setRescueFeeFlag("0");
						ddThirdPartyLossInfoTemp.setDamageType(CodeConstants.FeeTypeCode.PERSONLOSS);
						ddThirdPartyLossInfoTemp.setSumRealPayJud(0);
//						ddThirdPartyLossInfoTemp.setItemKindNo(prpLLossPersonVo.getItemKindNo());
						ddThirdPartyLossInfoTemp.setLossIndex(prpLLossPersonVo.getPersonId().toString());
						ddThirdPartyLossInfos.add(ddThirdPartyLossInfoTemp);
						ddIndex++ ;
					}
				}
			}
		}
		if (cpIndex > 0) {
			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setDamageType(CodeConstants.FeeTypeCode.PROPLOSS); // 涉案损失项类型
			compensateExp.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ); // 交强险险别
			compensateExp.setCheckPolicyM("0");// 是否不记免赔 BZ险没有免赔
			if (thirdPartyInfos != null && !thirdPartyInfos.isEmpty()) {
				ThirdPartyInfo[] thirdPartyInfoAry = new ThirdPartyInfo[thirdPartyInfos.size()];
				thirdPartyInfos.toArray(thirdPartyInfoAry);
				compensateExp.setThirdPartyInfoAry(thirdPartyInfoAry);// 涉案信息
			}
			if (cpThirdPartyLossInfos != null && !cpThirdPartyLossInfos.isEmpty()) {
				ThirdPartyLossInfo[] thirdPartyLossInfoAry = new ThirdPartyLossInfo[cpThirdPartyLossInfos.size()];
				cpThirdPartyLossInfos.toArray(thirdPartyLossInfoAry);
				compensateExp.setThirdPartyLossInfoAry(thirdPartyLossInfoAry);// 损失信息
			}
			compensateExp.setCompensateType(Integer.parseInt("2"));// 计算书类型
			compensateExp.setPayType("3");
			compensateExp.setDamageDate(prpLClaimVo.getDamageTime());
			if(compensateVo.getPrpLCompensateVo().getCaseType() != null){
				compensateExp.setClaimType(compensateVo.getPrpLCompensateVo().getCaseType().trim());// 赔案类别
			}
//			compensateExp.setIsSuitFlag(compensateVo.getPrpLCompensateVo().getLawsuitFlag());// 诉讼案标示
			compensateExp.setComCode(compensateVo.getPrpLCompensateVo().getMakeCom());
			compensateExpAry.add(compensateExp);
		} 
		if (iIndex > 0) {
			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setDamageType(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES); // 涉案损失项类型
			compensateExp.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ); // 交强险险别
			compensateExp.setCheckPolicyM("0");// 是否不记免赔 BZ险没有免赔
			if (thirdPartyInfos != null && !thirdPartyInfos.isEmpty()) {
				ThirdPartyInfo[] thirdPartyInfoAry = new ThirdPartyInfo[thirdPartyInfos.size()];
				thirdPartyInfos.toArray(thirdPartyInfoAry);
				compensateExp.setThirdPartyInfoAry(thirdPartyInfoAry);// 涉案信息
			}
			if (iThirdPartyLossInfos != null && !iThirdPartyLossInfos.isEmpty()) {
				ThirdPartyLossInfo[] thirdPartyLossInfoAry = new ThirdPartyLossInfo[iThirdPartyLossInfos.size()];
				iThirdPartyLossInfos.toArray(thirdPartyLossInfoAry);
				compensateExp.setThirdPartyLossInfoAry(thirdPartyLossInfoAry);// 损失信息
			}
			compensateExp.setCompensateType(Integer.parseInt("2"));// 计算书类型
			compensateExp.setPayType("3");
			compensateExp.setDamageDate(prpLClaimVo.getDamageTime());
			if(compensateVo.getPrpLCompensateVo().getCaseType() != null){
				compensateExp.setClaimType(compensateVo.getPrpLCompensateVo().getCaseType().trim());// 赔案类别
			}
//			compensateExp.setIsSuitFlag(compensateVo.getPrpLCompensateVo().getLawsuitFlag());// 诉讼案标示
			compensateExp.setComCode(compensateVo.getPrpLCompensateVo().getMakeCom());
			compensateExpAry.add(compensateExp);
		}
		if (ddIndex > 0) {
			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setDamageType(CodeConstants.FeeTypeCode.PERSONLOSS); // 涉案损失项类型
			compensateExp.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ); // 交强险险别
			compensateExp.setCheckPolicyM("0");// 是否不记免赔 BZ险没有免赔
			if (thirdPartyInfos != null && !thirdPartyInfos.isEmpty()) {
				ThirdPartyInfo[] thirdPartyInfoAry = new ThirdPartyInfo[thirdPartyInfos.size()];
				thirdPartyInfos.toArray(thirdPartyInfoAry);
				compensateExp.setThirdPartyInfoAry(thirdPartyInfoAry);// 涉案信息
			}
			if (ddThirdPartyLossInfos != null && !ddThirdPartyLossInfos.isEmpty()) {
				ThirdPartyLossInfo[] thirdPartyLossInfoAry = new ThirdPartyLossInfo[ddThirdPartyLossInfos.size()];
				ddThirdPartyLossInfos.toArray(thirdPartyLossInfoAry);
				compensateExp.setThirdPartyLossInfoAry(thirdPartyLossInfoAry);// 损失信息
			}
			compensateExp.setCompensateType(Integer.parseInt("2"));// 计算书类型
			compensateExp.setPayType("3");
			compensateExp.setDamageDate(prpLClaimVo.getDamageTime());
			if(compensateVo.getPrpLCompensateVo().getCaseType() != null){
				compensateExp.setClaimType(compensateVo.getPrpLCompensateVo().getCaseType().trim());// 赔案类别
			}
//			compensateExp.setIsSuitFlag(compensateVo.getPrpLCompensateVo().getLawsuitFlag());// 诉讼案标示
			compensateExp.setComCode(compensateVo.getPrpLCompensateVo().getMakeCom());
			compensateExpAry.add(compensateExp);
		}
		// 1.交强试算数据组织完毕,进行交强理算试算.
    	CompensateExp[] compensateExpArray = new CompensateExp[compensateExpAry.size()];
    	compensateExpAry.toArray(compensateExpArray);
    	return compensateExpArray;
    	
	}
	/**
	 * 交强理算前数据整理
	 * @param compensateVo
	 * @param compensateExpArray
	 * @return
	 */
	public CompensateListVo compensateList(CompensateVo compensateVo,CompensateExp[] compensateExpArray) {
    	List<CompensateExp> compensateExpList = new ArrayList<CompensateExp>(0);
    	CompensateListVo compensateListVo = new CompensateListVo();
    	
    	String registNo = compensateVo.getPrpLCompensateVo().getRegistNo();
    	compensateListVo.setRegistNo(registNo);
    	compensateListVo.setCalculateType(compensateVo.getCalculateType());
		if(compensateVo.getPrpLCompensateVo().getCompensateKind().trim().equals("1")) {//判断是否为商业扣交强部分计算
			compensateListVo.setIsBiPCi("1");//商业扣交强
		} else {
			compensateListVo.setIsBiPCi("0");//交强理算
		}
		int i = 0;
		int k = -1;// 为了取得D1险人员个数
		Map<Integer, Integer> map = new HashMap();// 为了取得每个人员下面的损失项数目
		List<ThirdPartyInfo> thirdPartyInfosTemp;
		List<ThirdPartyLossInfo> thirdPartyLossInfosTemp;
		List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoTemp;
		ThirdPartyInfo[] thirdPartyInfoAryTemp;
		ThirdPartyLossInfo[] thirdPartyLossInfoAryTemp;
		ThirdPartyRecoveryInfo[] thirdPartyRecoveryInfoAryTemp;
		String bzDutyCar = "0";
//			boolean isBZPay = false;
		int tempVar = 0;
		for (CompensateExp exp : compensateExpArray) {
			if (CodeConstants.KINDCODE.KINDCODE_R.equals(exp.getKindCode())
					|| CodeConstants.KINDCODE.KINDCODE_D11.equals(exp.getKindCode()) 
					|| CodeConstants.KINDCODE.KINDCODE_D12.equals(exp.getKindCode())) {//机上人员责任保险
				tempVar = exp.getIntLossIndex();
				if (map.containsKey(tempVar)) {
					int v = map.get(tempVar);
					map.put(tempVar, v + 1);
				} else {
					map.put(tempVar, 1);
				}
			}
		}
		for (CompensateExp exp : compensateExpArray) {
			if (exp.getPayType() != null && "1".equals(exp.getPayType().trim())) {//追偿
				if (exp.getRecoveryOrPayFlag() != null && "1".equals(exp.getRecoveryOrPayFlag().trim())) {
					if (exp.getKindCode() != null && CodeConstants.KINDCODE.KINDCODE_A.equals(exp.getKindCode())) {//机上人员责任保险
						// 计算书类型传入ilog
						compensateListVo.setCompensateType(exp.getCompensateType());
						compensateListVo.setPayType(exp.getPayType());// 代位类型：1:追偿,2:清付,3:自付(上代位后的新案件)；历史案件为null或””:普通理算
						compensateListVo.setIsSuitFlag(exp.getIsSuitFlag());
						compensateListVo.setBzDutyCar(bzDutyCar);
						if (exp.getOppoentCoverageType() == null) {
							exp.setOppoentCoverageType("");
						}
						compensateExpList.add(exp);
						i++;
					}
				}
			}
		}
		for (CompensateExp exp : compensateExpArray) {
			if (exp.getPayType() != null && "1".equals(exp.getPayType().trim())) {
				if (exp.getRecoveryOrPayFlag() != null && "1".equals(exp.getRecoveryOrPayFlag().trim())) {
					if (exp.getKindCode() != null && CodeConstants.KINDCODE.KINDCODE_A.equals(exp.getKindCode())) {//机上人员责任保险
						continue;
					}
				}
			}
			// 如果是交强险，需要将DWR取得的两个数组转换成List 如果是商业险，需要传承保日期字段
			if(CodeConstants.KINDCODE.KINDCODE_BZ.equals(exp.getKindCode())){
				thirdPartyInfosTemp = new ArrayList<ThirdPartyInfo>(0);
				thirdPartyLossInfosTemp = new ArrayList<ThirdPartyLossInfo>(0);
				thirdPartyRecoveryInfoTemp = new ArrayList<ThirdPartyRecoveryInfo>(0);
				thirdPartyInfoAryTemp = exp.getThirdPartyInfoAry();
				thirdPartyLossInfoAryTemp = exp.getThirdPartyLossInfoAry();
				thirdPartyRecoveryInfoAryTemp = exp.getThirdPartyRecoveryInfoAry();
				for(int j = 0; j<thirdPartyInfoAryTemp.length; j++ ){
					// bzDutyType在数据库属于decimal(19.4)类型，在这里需要将最后的.0000五位取消掉
//					String bzDutyTypeTemp = thirdPartyInfoAryTemp[j].getBzDutyType();
//					if(bzDutyTypeTemp.length()<=0||"".equals(bzDutyTypeTemp)){
//						bzDutyTypeTemp = "0.0";
//					}
	//				thirdPartyInfoAryTemp[j].setBzDutyType(StringUtils.split(bzDutyTypeTemp,".")[0]);
					thirdPartyInfosTemp.add(thirdPartyInfoAryTemp[j]);
				}
				for(int j = 0; j<thirdPartyLossInfoAryTemp.length; j++ ){
					thirdPartyLossInfoAryTemp[j].setInsteadFlag("0");
					thirdPartyLossInfosTemp.add(thirdPartyLossInfoAryTemp[j]);
				}
				if(thirdPartyRecoveryInfoAryTemp!=null){
					for(int j = 0; j<thirdPartyRecoveryInfoAryTemp.length; j++ ){
						thirdPartyRecoveryInfoTemp.add(thirdPartyRecoveryInfoAryTemp[j]);
					}
				}
				thirdPartyInfosTemp = this.takeOffNullObject(thirdPartyInfosTemp);
				thirdPartyLossInfosTemp = this.takeOffNullObject(thirdPartyLossInfosTemp);
				thirdPartyRecoveryInfoTemp = this.takeOffNullObject(thirdPartyRecoveryInfoTemp);
				exp.setThirdPartyInfos(thirdPartyInfosTemp);
				exp.setThirdPartyLossInfos(thirdPartyLossInfosTemp);
				exp.setThirdPartyRecoveryInfos(thirdPartyRecoveryInfoTemp);
				// isBZPay = true;
				for(Iterator it = exp.getThirdPartyInfos().iterator(); it.hasNext();){
					ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo)it.next();
					if(thirdPartyInfo.getBzDutyType()!=null && "1".equals(thirdPartyInfo.getBzDutyType())){
						bzDutyCar = "1";
					}
				}
				for(Iterator it = exp.getThirdPartyLossInfos().iterator(); it.hasNext();){
					ThirdPartyLossInfo thirdPartyLossInfo = (ThirdPartyLossInfo)it.next();
					if(thirdPartyLossInfo.getInsureComCode()==null){
						thirdPartyLossInfo.setInsureComCode("null");
					}
				}
				compensateListVo.setThirdPartyRecoveryInfoAry(thirdPartyRecoveryInfoTemp);
			}
			
			
			if (CodeConstants.KINDCODE.KINDCODE_R.equals(exp.getKindCode())) {//机上人员责任保险
				if (exp.getIntLossIndex() > k) {
					k = exp.getIntLossIndex();
				}
				exp.setLossAmountPerPerson(map.get(k));
			}
			// 计算书类型传入ilog
			compensateListVo.setCompensateType(exp.getCompensateType());
			compensateListVo.setPayType(exp.getPayType());// 代位类型：1:追偿,2:清付,3:自付(上代位后的新案件)；历史案件为null或””:普通理算
			if (exp.getOppoentCoverageType() == null) {
				exp.setOppoentCoverageType("");
			}
			if (exp.getRecoveryOrPayFlag() == null) {
				exp.setRecoveryOrPayFlag("");
			}
			compensateListVo.setIsSuitFlag(exp.getIsSuitFlag());
			compensateListVo.setBzDutyCar(bzDutyCar);
			compensateExpList.add(exp);
			i++;
		}
		
		if (compensateListVo.getIsSuitFlag() == null) {
			compensateListVo.setIsSuitFlag("0");
		}
		compensateListVo.setCompensateList(compensateExpList);
		compensateListVo.setListAmount(i);
		if (k >= 0) {
			compensateListVo.setLossPersonsNum(k + 1);
		}
		
		boolean firstCompFlag = true;////第一张 商业/交强计算书
		//compensateListVo.setIsBiPCi("1"); 商业
		//核赔通过的计算书 TODO
		List<PrpLCompensateVo> compensateList = compensateService.findCompensateByClaimno(compensateVo.getPrpLCompensateVo().getClaimNo(),"N");
		for(PrpLCompensateVo compVo : compensateList){
			PrpLCompensateExtVo compExtVo = compVo.getPrpLCompensateExt();
			if(compExtVo.getWriteOffFlag()==null ||"0".equals(compExtVo.getWriteOffFlag())){//不是冲销的案件
				if(compExtVo.getOppoCompensateNo() == null){
					firstCompFlag = false;
					break;
				}
			}	
		}
		
		if(!firstCompFlag){//交强险核赔通过计算书，并没有冲销 注销
			compensateListVo.setReCaseFlag("1");
			
			List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(compensateListVo.getRegistNo());
			compensateListVo.setCheckDutyList(checkDutyList);
		}
		
		return compensateListVo;
	}
	
	/**
	 * 交强理算方法
	 * @param compensateList
	 * @return
	 */
	private CompensateListVo calculateCi (CompensateListVo compensateListVo) {
	    List<CompensateExp> compensateListCompensateList = compensateListVo.getCompensateList();
	    for (CompensateExp compensateExp : compensateListCompensateList) {
	    	 logger.debug("start----------------------");
	        //交强计算书。
	        //只要险别是交强险都按交强险计算，因为在商业险理算有可能做获取交强理算金额的操作。
	    	compensateService.calculateCi(compensateListVo, compensateExp);
//	    	if("1".equals(compensateListVo.getReCaseFlag())){
//	    		compensateService.calculateReCaseCi(compensateListVo, compensateExp);	
//			}else{
//				compensateService.calculateCi(compensateListVo, compensateExp);
//			}
//	         this.calculateCi(compensateListVo, compensateExp);
	         
	         
	         logger.debug("end----------------------");
	        //人保规则：设置返回的理算大对象List。
	        {
	        	compensateListVo.setCompensateExpInfo(compensateExp);
	        }
	    }

	    return compensateListVo;
	}
	/**
	 * 计算。用于代替规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险部分。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
//	public CompensateExp calculateCi(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//
//	    //人保规则：获取交强赔付限额。
//	    {
//	    	boolean BzDutyType=false;
//	        List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
//	        for (ThirdPartyInfo thirdPartyInfo : thirdPartyInfos) {
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		BzDutyType=true;
//		    	} 
//	        	
//	            thirdPartyInfo.setDutyAmount(this.calBzAmount(compensateExp.getDamageType(),BzDutyType ));
//	        }
//	    }
//
//	    if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {//车、财
//            this.calculateCi4Common(compensateListVo, compensateExp);
//	    } else {//人
//	        this.calculateCi4CommonPerson(compensateListVo, compensateExp);
//	    }
//	    
//	    return compensateExp;
//	}
//	private CompensateExp calculateCi(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//
//	    //人保规则：获取交强赔付限额。
//	    {
//	    	boolean BzDutyType=false;
//	        List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
//	        for (ThirdPartyInfo thirdPartyInfo : thirdPartyInfos) {
//	        	BzDutyType=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		BzDutyType=true;
//		    	} 
//	        	
//	            thirdPartyInfo.setDutyAmount(this.calBzAmount(compensateExp.getDamageType(),BzDutyType ));
//	        }
//	    }
//
//	    if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {//车、财
//            this.calculateCi4Common(compensateListVo, compensateExp);
//	    } else {//人
//	        this.calculateCi4CommonPerson(compensateListVo, compensateExp);
//	    }
//	    
//	    return compensateExp;
//	}
	/**
	 * 计算。用于代替规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险非互陪自赔部分理算。
	 * @param compensateList
	 * @param compensateExp
	 * @return
	 */
//	private CompensateExp calculateCi4Common(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//        logger.debug("交强险非互碰自赔(车财损失)部分理算");
//	    boolean bzFlag = false; // 本车交强责任类型
//	    double allDamageAmount = 0.00d; // 所有限额
//	    int noDutyCarNum = 0; // 无责方车辆个数
//	    int dutyCarNum = 0; // 有责方车辆个数
//	    int dutyCarNumWithLoss = 0;//有损失的有责方车辆数量。
//	    final String compensateType = compensateListVo.getCompensateType()+"";
//	    
//	    List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//	    List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//	    
////		    ThirdPartyInfo mainCar = null;
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        if (thirdPartyInfo.getSerialNo() == 1) { // 标的车
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlag = true;
//		    	} 
//	        	// 2.3初始化本车交强责任类型
////		            mainCar = thirdPartyInfo;
//	        }
//	    }
//
//	    // 2.4累计赔付限额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        allDamageAmount += thirdPartyInfo.getDutyAmount();
//	    }
//
//	    // 2.5获取损失项归属车辆限额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())) { // 车牌号相同
//	                thirdPartyLossInfo.setDamageAmount(thirdPartyInfo.getDutyAmount()); // 归属车辆赔限额
//	                thirdPartyLossInfo.setLossItemType(thirdPartyInfo.getLossItemType()); // 归属车辆类型
//	                thirdPartyLossInfo.setBzDutyType(thirdPartyInfo.getBzDutyType()); // 归属车辆交强责任类型
//	                thirdPartyLossInfo.setBzDutyFlag(thirdPartyInfo.getBzDutyFlag());
//	            } else if(thirdPartyLossInfo.getSerialNo() == 0) {//地面   获取损失项为第三者财产损失时，则为无责方
//	                thirdPartyLossInfo.setBzDutyType("0"); // 归属车辆交强责任类型
//	                thirdPartyLossInfo.setBzDutyFlag(false);
//	            } 
//	        }
//	    }
//
//	    // 2.6计算赔付第三方金额
//	    //计算第三方为本损失可赔付的比例。
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	    	Boolean bzFlagTemp=false;
//	    	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	    		bzFlagTemp=true;
//	    	}
//	    	
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (!thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())) {
//	                //无责车不赔付路面损失 和 无责方的损失。
//	                //zhongyuhai 20121023
//	    	    	Boolean bzFlagLoss=false;
//	    	    	if(thirdPartyLossInfo.getBzDutyType()!=null && thirdPartyLossInfo.getBzDutyType().trim().equals("1")) {
//	    	    		bzFlagLoss=true;
//	    	    	}
//
//	                if((!bzFlagTemp&&  (thirdPartyLossInfo.getSerialNo()==0 || !bzFlagLoss))
//	                	&& "1".equals(compensateListVo.getBzDutyCar())){//车无责，损失项也无责，并且损失为路面财
//	                    continue;
//	                }else{
//	                    String bzCompensateText = String.format("\n交强险:", 
//	                                                            thirdPartyLossInfo.getLossName() , 
//	                                                            thirdPartyLossInfo.getLossFeeName() , 
//	                                                            thirdPartyInfo.getDutyAmount() , 
//	                                                            thirdPartyLossInfo.getSumLoss() , 
//	                                                            allDamageAmount - thirdPartyLossInfo.getDamageAmount());
//	                    double payAmount = thirdPartyLossInfo.getSumLoss() * thirdPartyInfo.getDutyAmount() / (allDamageAmount - thirdPartyLossInfo.getDamageAmount());
//	                    thirdPartyInfo.addThirdPartyPayAmount(payAmount, bzCompensateText, thirdPartyLossInfo, compensateExp);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 初始化代赔标志位。
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	        for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                carThirdPartyCompInfo.setInsteadFlag("0"); // 非代赔
//	        }
//	    }
//	    //TODO
//	    if (compensateType.trim().equals("2") && !("1").equals(compensateListVo.getIsBiPCi())) {//商业计算书返回true，交强计算书返回false
//	        // 2.23设置代赔标志位
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp=true;
//	        	}
//	        	
//	            String insteadFlag = "4";
//	            if(thirdPartyInfo.getSerialNo()>1){ //三者车                   
//	                //三者无责、主车有责。
//	                if(!bzFlagTemp &&bzFlag){
//	                    insteadFlag = "1";//无责代赔
//	                }else 
//	                //无保有责才算无保代赔。
//	                if((thirdPartyInfo.getInsuredFlag().equals("0")||thirdPartyInfo.getInsuredFlag().equals("1")) 
//	                		&& bzFlagTemp){
//	                        insteadFlag = "2";//无保代赔
//	                }
//	            }
//	            
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	            	Boolean carThirdPartyCompInfoBzFlagTemp = false;
//	            	if(carThirdPartyCompInfo.getBzDutyType()!=null && carThirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		carThirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (carThirdPartyCompInfo.getSerialNo()==1 && carThirdPartyCompInfoBzFlagTemp) {//本车上损失，有责
//	                    carThirdPartyCompInfo.setInsteadFlag(insteadFlag);
//	                } else {
//	                    if(carThirdPartyCompInfo.getSerialNo()==1){//本车上损失，但无责
//	                        carThirdPartyCompInfo.setInsteadFlag("0"); // 非代赔
//	                    }
//	                    else{
//	                        carThirdPartyCompInfo.setInsteadFlag("4"); // 三者代赔三者
//	                    }
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 交强试算_20080201_财产_代赔
//	    if ("1".equals(compensateListVo.getBzDutyCar())) { // 存在有责车辆
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp=true;
//	        	}
//	            // 2.7.1无责方车辆个数
//	            if (!bzFlagTemp &&
//	                compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) { // 无责,车财损失
//	                noDutyCarNum++;
//	            }
//	            // 2.7.2有责方车辆个数
//	            if (bzFlagTemp &&
//	                compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) { // 有责,车财损失
//	                dutyCarNum++;
//	            }
//	        }
//
//	        // 2.7.3有车辆的损失个数。
//	        // 统计车辆的损失项数量。 统计各自车的损失项数量
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                    thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo()) &&
//	                    thirdPartyLossInfo.getSumLoss() != 0.00) { // 车财损失
//	                    thirdPartyInfo.setCarLossNum(thirdPartyInfo.getCarLossNum() + 1);
//	                }
//	            }
//	        }
//	        
//	        //计算有损有责车辆数量。
//	        //zhongyuhai 20121017
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//
//	        	if(thirdPartyInfo.getCarLossNum() > 0 && bzFlagTemp){
//	                dutyCarNumWithLoss++;
//	            }
//	        }
//
//	        // 2.7.5无责方赔付有责方
//	        // 人保规则：rule 新财产计算$40$1_a无责方赔付有责方$41$
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	            	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		thirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && // 车财损失
//	                    !bzFlagTemp && // 赔付损失车辆无责
//	                    thirdPartyCompInfoBzFlagTemp) { // 归属车辆交强有责
//	                    
//	                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss() / noDutyCarNum); // 损失金额 / 无责方车辆个数
//	                    
//	                    String bzCompensateText = String.format("\n交强险无责代赔 : %s \n本项理算金额 = 损失金额 / 无责方车辆个数 \n "+
//	                    		                                "= %s / %s \n "+
//	                    		                                "= %s" , 
//	                                                            thirdPartyCompInfo.getLossName() , 
//	                                                            thirdPartyCompInfo.getSumLoss() , 
//	                                                            noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum);
//	                    thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.6无责方赔付有责方_本车    本车为无责方赔付有责方
//	        // 人保规则：rule 新财产计算$40$1_a1无责方赔付有责方$_$本车$41$
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	            	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		thirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && // 车财损失
//	                		thirdPartyCompInfoBzFlagTemp &&
//	                    !bzFlagTemp && // 赔付损失车辆无责
//	                    thirdPartyInfo.getSerialNo()==1 // 涉案车辆为标的车
//	                    ) { // 归属车辆交强有责
//	                    
//	                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss() / noDutyCarNum); // 损失金额 / 无责方车辆个数
//	                    
//	                    String bzCompensateText = String.format("\n交强险 : %s 的 %s \n本项理算金额 = 损失金额 / 无责方车辆个数 \n " +
//                                                                "= %s / %s \n" +
//                                                                "= %s" , 
//	                                                            thirdPartyCompInfo.getLossName() , 
//	                                                            thirdPartyCompInfo.getLossFeeName() , 
//	                                                            thirdPartyCompInfo.getSumLoss(), 
//	                                                            noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum);
//	                    thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.7赔付损失项赋值。
//	        // 人保规则：rule 新财产计算$40$1_b1赔付损失项赋值$41$。
//	        // 无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//
//	        	List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            if (!bzFlagTemp) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp &&
//	                        compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {
//	                        thirdPartyCompInfo.setPayAmount(thirdPartyInfo.getDutyAmount() / dutyCarNumWithLoss);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.8无责代赔限额控制
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            if (!bzFlagTemp) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp &&
//	                        compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                        thirdPartyCompInfo.getSumRealPay() > thirdPartyCompInfo.getPayAmount()) { // 实赔金额 > 损失车辆 的 责任限额 / 有责方车辆个数
//	                        //无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
//	                        thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount());
//	                        
//	                        String bzCompensateText = String.format("\n无责代赔金额超过无责方赔付限额 ， 故：\n本项理算金额 = 责任限额 / 有责方车辆个数 \n " +
//                                                                    "= %s / %s \n " +
//                                                                    "= %s", 
//	                                                                thirdPartyInfo.getDutyAmount(), 
//	                                                                dutyCarNumWithLoss , 
//	                                                                thirdPartyCompInfo.getPayAmount());
//	                        thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                        logger.debug(bzCompensateText);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.9车辆的损失金额合计（1/2/3）
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                if (thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo()) &&
//	                    compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {
//	                    // 赔付损失车辆 的车辆的总损失金额为 赔付损失车辆 的 车辆的总损失金额 + 赔付损失项 的 损失金额
//	                    thirdPartyInfo.setCarLossSum(thirdPartyInfo.getCarLossSum() + thirdPartyLossInfo.getSumLoss());
//	                }
//	            }
//	        }
//	        for(ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos){
//	            //路面理算项。
//	            if(thirdPartyLossInfo.getSerialNo()==0){
//	                thirdPartyLossInfo.setCarLossSum(thirdPartyLossInfo.getSumLoss());
//	            }
//	            else{
//	                for(ThirdPartyInfo thirdPartyInfo : infos){
//	                    if(thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())){
//	                        thirdPartyLossInfo.setCarLossSum(thirdPartyInfo.getCarLossSum());
//	                        break;
//	                    }
//	                }
//	            }
//	        }
//	        
//	        for(ThirdPartyInfo thirdPartyInfo : infos){
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                        thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        thirdPartyCompInfo.setCarLossSum(thirdPartyLossInfo.getCarLossSum()); // 赔付损失分项 的归属车辆的总损失金额为 赔付损失项 的 归属车辆的总损失金额
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.10车辆的赔付金额合计
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                !bzFlagTemp) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    thirdPartyInfo.setCarPaySum(thirdPartyInfo.getCarPaySum() + thirdPartyCompInfo.getSumRealPay()); // 赔付损失车辆 的总赔付金额为 赔付损失车辆 的 总赔付金额 + 赔付损失分项 的 实赔金额
//	                }
//	            }
//	        }
//
//	        // 2.7.11财产损失超限额调整--无责
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                !bzFlagTemp && 
//	                thirdPartyInfo.getCarPaySum() > thirdPartyInfo.getDutyAmount()) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 赔付车辆限额 * ( 赔付损失分项 的 损失金额 / 无责方车辆个数 ) / ( 赔付损失分项 的 归属车辆的总损失金额 / 无责方车辆个数 )
//	                    if(thirdPartyCompInfo.getCarLossSum() > 0) {
//	                		thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount() * (thirdPartyCompInfo.getSumLoss() / noDutyCarNum) / (thirdPartyCompInfo.getCarLossSum() / noDutyCarNum));
//	                	} else {
//	                		thirdPartyCompInfo.setSumRealPay(0.00);
//	                	}
//	                    String bzCompensateText = String.format("\n损失项之和超限额,进行超限额调整" +
//                                                                "\n本项实赔金额 = 赔付损失车辆交强限额 × 分项赔付金额 / 总赔付金额之和\n" +
//                                                                " = %s × %s / %s \n" +
//                                                                " = %s", 
//	                                                            thirdPartyCompInfo.getPayCarAmount() , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getCarLossSum() / noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumRealPay());
//	                    thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.12无责方赔付金额初始化
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            thirdPartyLossInfo.setNoDutyCarPay(0d);
//	        }
//
//	        // 2.7.13无责方赔付有责方金额合计
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            if (!bzFlagTemp) { // 无责
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp) { // 有责
//	                        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                                thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                                // 赔付损失项 的无责方赔付金额为 赔付损失项 的 无责方赔付金额 + 赔付损失分项 的 实赔金额
//	                                thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getNoDutyCarPay() + thirdPartyCompInfo.getSumRealPay());
//	                            }
//	                        }
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.14无责方赔付有责方金额调整
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                thirdPartyLossInfo.getNoDutyCarPay() > thirdPartyLossInfo.getSumLoss()) {
//	                thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getSumLoss());
//	            }
//	        }
//
//	        // 2.7.15有责方赔付无责方
//	        // 2.7.16有责方赔付路面财产
//	        // 赔付无责方与赔付路财的理算公式一致
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && bzFlagTemp) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//				    	
//	                    if (!thirdPartyCompInfoBzFlagTemp) {
//	                        thirdPartyCompInfo.setPayAmount(thirdPartyCompInfo.getSumLoss() / dutyCarNum);
//	                        String bzCompensateText = String.format("\n交强险 : %s 的 %s " +
//	                                                                "\n本项理算金额= 损失金额 / 有责方车辆个数\n" +
//                                                                    "= %s / %s \n" +
//                                                                    "= %s", 
//	                                                                thirdPartyCompInfo.getLossName() , 
//	                                                                thirdPartyCompInfo.getLossFeeName() , 
//	                                                                thirdPartyCompInfo.getSumLoss(),
//	                                                                dutyCarNum , 
//	                                                                thirdPartyCompInfo.getPayAmount());
//	                        thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                        logger.debug(bzCompensateText);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.17有责方赔付有责方
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && bzFlagTemp) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=thirdPartyCompInfo.getBzDutyFlag();
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp) {
//	                        
//	                        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                            if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                                
//	                                thirdPartyCompInfo.setPayAmount((thirdPartyCompInfo.getSumLoss() - thirdPartyLossInfo.getNoDutyCarPay()) / (dutyCarNum - 1));
//	                                
//	                                String bzCompensateText = String.format("\n交强险 : %s 的 %s " +
//	                                                                        "\n本项理算金额 = (损失金额 - 无责方赔付金额) / (有责方车辆个数 - 1)" +
//	                                                                        "\n= (%s - %s) / (%s - 1)" +
//	                                                                        "\n= %s", 
//	                                                                        thirdPartyCompInfo.getLossName() , 
//	                                                                        thirdPartyCompInfo.getLossFeeName() , 
//	                                                                        thirdPartyCompInfo.getSumLoss() , 
//	                                                                        thirdPartyLossInfo.getNoDutyCarPay() ,
//	                                                                        dutyCarNum , 
//	                                                                        thirdPartyCompInfo.getPayAmount());
//	                                thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                                logger.debug(bzCompensateText);
//	                            }
//	                        }
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.8累计赔付第三方的金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                // 赔付损失车辆 的限额比例赔偿金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失车辆 的 限额比例赔偿金额
//	                thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount() + thirdPartyCompInfo.getPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.9超限额调整
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	            // 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 限额比例赔偿金额
//	            if (thirdPartyInfo.getDutyAmount() < thirdPartyInfo.getPayAmount()) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyInfo.getDutyAmount() *
//	                                                       thirdPartyCompInfo.getPayAmount() / 
//	                                                       thirdPartyInfo.getPayAmount());
//	                    
//	                    String bzCompensateText = String.format("\n损失金额超过赔付限额，进行超限额调整 : " +
//	                                                            "\n= 本车责任限额 × 损失项的限额比例赔偿金额 / 损失项的限额比例赔偿金额之和" +
//	                                                            "\n= %s × %s / %s" +
//	                                                            "\n= %s", 
//	                                                            thirdPartyInfo.getDutyAmount() , 
//	                                                            thirdPartyCompInfo.getPayAmount() ,
//	                                                            thirdPartyInfo.getPayAmount() ,
//	                                                            thirdPartyCompInfo.getAdjPayAmount()
//	                                                            );
//	                    thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            } else {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    // 赔付损失分项 的超限额赔付调整金额为 赔付损失分项 的 限额比例赔付金额
//	                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyCompInfo.getPayAmount());
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.10累计赔付金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                // 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
//	                thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount() + thirdPartyCompInfo.getAdjPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.11累计未赔付金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            // 损失赔付车辆 的 责任限额 is more than 损失赔付车辆 的 累计赔付金额
//	            if (thirdPartyInfo.getDutyAmount() > thirdPartyInfo.getAdjPayAmount()) {
//	                // 损失赔付车辆 的剩余限额为 损失赔付车辆 的 责任限额 - 损失赔付车辆 的 累计赔付金额
//	                thirdPartyInfo.setLeftAmount(thirdPartyInfo.getDutyAmount() - thirdPartyInfo.getAdjPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.12车辆项下分项损失转换为损失项下分项损失 TODO
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        
//	                        thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),
//	                                                                  thirdPartyCompInfo.getObjectIndex(),
//	                                                                  thirdPartyCompInfo.getAdjPayAmount(),
//	                                                                  thirdPartyCompInfo.getBzCompensateText(),
//	                                                                  thirdPartyLossInfo.getSerialNo()
//	                                                                  );
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (!thirdPartyInfo.getBzDutyFlag()) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        // 赔付损失项 .增加赔付分项损失(赔付损失项 的 车牌号码 , 车辆赔付损失分项 的 损失项序号 , 车辆赔付损失分项 的 实赔金额 , 车辆赔付损失分项 的 交强赔付公式 )
//	                        thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),
//	                                                                  thirdPartyCompInfo.getObjectIndex(),
//	                                                                  thirdPartyCompInfo.getSumRealPay(),
//	                                                                  thirdPartyCompInfo.getBzCompensateText(),
//	                                                                  thirdPartyLossInfo.getSerialNo()
//	                                                                  );
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.13合计损失项已赔付金额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//	        for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            // 赔付损失项 涉案车辆限额赔付金额为 赔付损失项 的 涉案车辆限额赔付金 + 赔付损失分项 的 超限额赔付调整金额
//	            thirdPartyLossInfo.setThirdPartyLimitPay(thirdPartyLossInfo.getThirdPartyLimitPay() + thirdPartyCompInfo.getAdjPayAmount());
//	        }
//	    }
//
//	    // 2.14分项损失未赔足金额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//	        for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            // 赔付损失分项 的未赔足金额为 赔付损失项 的 损失金额 - 赔付损失项 的 涉案车辆限额赔付金
//	            thirdPartyCompInfo.setLeftPay(thirdPartyLossInfo.getSumLoss() - thirdPartyLossInfo.getThirdPartyLimitPay());
//	        }
//	    }
//
//	    // 2.15损失项下分项损失转换为车辆项下分项损失
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()){
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> lossThirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 损失项下赔付损失分项
//	                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                    for (ThirdPartyCompInfo lossThirdPartyCompInfo : lossThirdPartyCompInfos) { // 损失项下赔付损失分项
//	                        if (carThirdPartyCompInfo.getObjectIndex().equals(lossThirdPartyCompInfo.getObjectIndex())) {
//	                            // 车辆赔付损失分项 的未赔足金额为 损失赔付损失分项 的 未赔足金额
//	                            carThirdPartyCompInfo.setLeftPay(lossThirdPartyCompInfo.getLeftPay());
//	                        }
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.16合计未赔足金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                // 赔付损失车辆 的赔付损失项未赔足金额为 赔付损失车辆 的 赔付损失项未赔足金额 + 赔付损失分项 的 未赔足金额
//	                thirdPartyInfo.setSumNoenoughPay(thirdPartyInfo.getSumNoenoughPay() + carThirdPartyCompInfo.getLeftPay());
//	            }
//	        }
//	    }
//
//	    // 2.17剩余限额调整金额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if ((thirdPartyInfo.getDutyAmount() > (thirdPartyInfo.getAdjPayAmount() + 0.005)) &&
//	                    carThirdPartyCompInfo.getLeftPay() > 0.005) {
//	                    //赔付损失分项 的剩余限额赔付金额为 赔付损失车辆 的 剩余限额 * 赔付损失分项 的 未赔足金额 / 赔付损失车辆 的 赔付损失项未赔足金额
//	                    carThirdPartyCompInfo.setLeftPayAmount(thirdPartyInfo.getLeftAmount() * 
//	                                                           carThirdPartyCompInfo.getLeftPay() / 
//	                                                           thirdPartyInfo.getSumNoenoughPay());
//	                    
//	                    String bzCompensateText = String.format("\n损失存在剩余限额 , 进行剩余限额分摊 : " +
//	                                                            "\n= 赔付剩余限额 × 损失项的未赔足金额 / 所有未赔付金额" +
//	                                                            "\n= %s × %s / %s" +
//	                                                            "\n= %s", 
//	                                                            thirdPartyInfo.getLeftAmount() , 
//	                                                            carThirdPartyCompInfo.getLeftPay() ,
//	                                                            thirdPartyInfo.getSumNoenoughPay(),
//	                                                            carThirdPartyCompInfo.getLeftPayAmount());
//	                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.19超损失调整
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if ((carThirdPartyCompInfo.getLeftPayAmount() > carThirdPartyCompInfo.getLeftPay()) &&
//	                    (carThirdPartyCompInfo.getLeftPay() > 0.000001)) {
//	                    
//	                    carThirdPartyCompInfo.setLeftPayAmount(new BigDecimal(carThirdPartyCompInfo.getLeftPay()).doubleValue());
//	                    
//	                    String bzCompensateText = String.format("\n剩余限额赔付金额超过未赔足金额 ， 所以 = %s", 
//	                                                            carThirdPartyCompInfo.getLeftPay());
//	                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.20实际赔付金额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if (carThirdPartyCompInfo.getAdjPayAmount() > 0) {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失分项 的 剩余限额赔付金额
//	                    carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getAdjPayAmount() + carThirdPartyCompInfo.getLeftPayAmount());
//	                    if("2".equals(compensateListVo.getPayType())){
//	                        carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
//	                    }
//	                } else {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失分项 的 剩余限额赔付金额
//	                    carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getPayAmount() + carThirdPartyCompInfo.getLeftPayAmount());
//	                    if("2".equals(compensateListVo.getPayType())){
//	                        carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
//	                    }
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 2.21组织返回信息<交强>
//	    // 商业计算书。
// 	    if (compensateType!=null&&(compensateType.trim().equals("1")||compensateType.trim().equals("2"))) { 
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	            	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_14组织返回信息(本车损失)");
//	            }
//	        }
//	    } 
//	    
//	    // 交强计算书。
//	    if (compensateType!=null&&!((compensateType.trim().equals("1")||compensateType.trim().equals("2")))) { 
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            if ("1".equals(thirdPartyInfo.getLossItemType())) {
//	                List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_14组织返回信息");
//	                }
//	            }
//	        }
//	        
//	        // 2.24组织返回信息
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                    !"1".equals(thirdPartyInfo.getLossItemType()) &&
//	                    "1".equals(carThirdPartyCompInfo.getLossItemType()) &&
//	                    ("1".equals(carThirdPartyCompInfo.getInsteadFlag()) || "2".equals(carThirdPartyCompInfo.getInsteadFlag()))) {
//	                	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_16组织返回信息");
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.25按损失项顺序返回实赔
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                     ("0".equals(thirdPartyCompInfo.getInsteadFlag()) || 
//	                      "1".equals(thirdPartyCompInfo.getInsteadFlag()) || 
//	                      "2".equals(thirdPartyCompInfo.getInsteadFlag())||
//	                      ("1".equals(compensateListVo.getIsBiPCi())&&"4".equals(thirdPartyCompInfo.getInsteadFlag())))) {//商业扣交强时需要将三者赔付算上
//	                    
//	                    thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay() + thirdPartyCompInfo.getSumRealPay());
//	                    thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + "\n" + thirdPartyCompInfo.getBzCompensateText());
//	                    thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
//	                    thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount()); // 赔付损失项 的理算金额为 返回损失分项 的 限额比例赔付金额
//	                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//	                    thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
//	                    compensateListVo.setDutyCarNum(dutyCarNum);
//	                    thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
//	                    if("2".equals(thirdPartyCompInfo.getInsteadFlag())){
//	                        thirdPartyLossInfo.setNoPolicySumRealPaid(thirdPartyLossInfo.getNoPolicySumRealPaid() + thirdPartyCompInfo.getSumRealPay());
//	                        //thirdPartyLossInfo.addNoPolicyInsteadThirdPartyCompInfos(thirdPartyCompInfo);
//	                    }
//	                    logger.debug("交强理算："+thirdPartyLossInfo.getLicenseNo()+","+thirdPartyLossInfo.getLossName()+","+thirdPartyLossInfo.getSumRealPay());
//	                }
//	            }
//	    }
//
//	    // 2.26设置赔付损失项的超限额标志位
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                    thirdPartyCompInfo.getPayAmount() > thirdPartyCompInfo.getAdjPayAmount()) {
//	                    thirdPartyLossInfo.setOverFlag("1");
//	                }
//	            }
//	    }
//
//	    // 2.27设置赔付损失项的剩余限额赔付标志位
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                    thirdPartyCompInfo.getLeftPayAmount() > 0) {
//	                    thirdPartyLossInfo.setOverFlag("2");
//	                }
//	            }
//	    }
//
//	    // 2.29设置交强险无保代赔的所有限额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//	                }
//	            }
//	    }
//
//	    // 2.30屏蔽本车损失以及空的理算公式
//	    if ("2".equals(compensateListVo.getPayType())) { // 清付
//	        
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        if (("0".equals(thirdPartyCompInfo.getInsteadFlag()) || "2".equals(thirdPartyCompInfo.getInsteadFlag())) &&
//	                            thirdPartyLossInfo.getBzCompensateText() != null) {
//	                            String bzCompensateText = String.format("\n实际赔付金额 = (限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额)" +
//	                                                                    "\n= %s", 
//	                                                                    thirdPartyLossInfo.getSumRealPay());
//	                            thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
//	                            logger.debug(bzCompensateText);
//	                        }
//	                    }
//	                }
//	        }
//	    } else {
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (thirdPartyLossInfo.getBzCompensateText()!=null &&
//	                ("0".equals(thirdPartyLossInfo.getInsteadFlag()) || "2".equals(thirdPartyLossInfo.getInsteadFlag())) && 
//	                bzFlag) {
//	                String bzCompensateText = String.format("\n实际赔付金额 = 限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额" +
//                                                            "\n=%s", 
//	                                                        thirdPartyLossInfo.getSumRealPay());
//	                thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
//	                logger.debug(bzCompensateText);
//	            }
//	        }
//	    }
//
//	    // 17屏蔽本车损失(屏蔽空的理算公式)
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        if (thirdPartyLossInfo.getBzCompensateText()==null) {
//	            thirdPartyLossInfo.setBzCompensateText("");
//	        }
//	    }
//	    
//	    // 2.31清空赔付损失车辆信息
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        thirdPartyInfo.setLeftAmount(0); //剩余限额
//	        thirdPartyInfo.setLeftPayAmount(0); //剩余限额赔偿金额
//	        thirdPartyInfo.setAdjPayAmount(0); //累计赔付金额
//	        thirdPartyInfo.setDutyCompPay(0); //责任限额计算金额
//	        thirdPartyInfo.setSumNoenoughPay(0); //赔付损失项未赔足金额
//	        thirdPartyInfo.setPayAmount(0); //限额比例赔偿金额
//	        thirdPartyInfo.deleteThirdPartyPayAmount(); //赔付损失车辆.清空赔付损失分项
//	        thirdPartyInfo.setSumRealPay(0); //车辆合计赔付金额
//	        thirdPartyInfo.setCarPaySum(0); //总赔付金额
//	        thirdPartyInfo.setCarLossNum(0); //车辆损失个数
//	        thirdPartyInfo.setCarLossSum(0); //车辆的总损失金额
//	    }
//	    
//	    // 2.32清空赔付损失项
//	    compensateListVo.deleteReturnBZCompensateList();
//
//	    return compensateExp;
//	}
//	private CompensateExp calculateCi4Common(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//        logger.debug("交强险非互碰自赔(车财损失)部分理算");
//	    boolean bzFlag = false; // 本车交强责任类型
//	    double allDamageAmount = 0.00d; // 所有限额
//	    int noDutyCarNum = 0; // 无责方车辆个数
//	    int dutyCarNum = 0; // 有责方车辆个数
//	    int dutyCarNumWithLoss = 0;//有损失的有责方车辆数量。
//	    final String compensateType = compensateListVo.getCompensateType()+"";
//	    
//	    List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//	    List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//	    
////		    ThirdPartyInfo mainCar = null;
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        if (thirdPartyInfo.getSerialNo() == 1) { // 标的车
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlag = true;
//		    	} 
//	        	// 2.3初始化本车交强责任类型
////		            mainCar = thirdPartyInfo;
//	        }
//	    }
//
//	    // 2.4累计赔付限额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        allDamageAmount += thirdPartyInfo.getDutyAmount();
//	    }
//
//	    // 2.5获取损失项归属车辆限额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())) { // 车牌号相同
//	                thirdPartyLossInfo.setDamageAmount(thirdPartyInfo.getDutyAmount()); // 归属车辆赔限额
//	                thirdPartyLossInfo.setLossItemType(thirdPartyInfo.getLossItemType()); // 归属车辆类型
//	                thirdPartyLossInfo.setBzDutyType(thirdPartyInfo.getBzDutyType()); // 归属车辆交强责任类型
//	                thirdPartyLossInfo.setBzDutyFlag(thirdPartyInfo.getBzDutyFlag());
//	            } else if(thirdPartyLossInfo.getSerialNo() == 0) {//地面   获取损失项为第三者财产损失时，则为无责方
//	                thirdPartyLossInfo.setBzDutyType("0"); // 归属车辆交强责任类型
//	                thirdPartyLossInfo.setBzDutyFlag(false);
//	            } 
//	        }
//	    }
//
//	    // 2.6计算赔付第三方金额
//	    //计算第三方为本损失可赔付的比例。
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	    	Boolean bzFlagTemp=false;
//	    	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	    		bzFlagTemp=true;
//	    	}
//	    	
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (!thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())) {
//	                //无责车不赔付路面损失 和 无责方的损失。
//	                //zhongyuhai 20121023
//	    	    	Boolean bzFlagLoss=false;
//	    	    	if(thirdPartyLossInfo.getBzDutyType()!=null && thirdPartyLossInfo.getBzDutyType().trim().equals("1")) {
//	    	    		bzFlagLoss=true;
//	    	    	}
//
//	                if((!bzFlagTemp&&  (thirdPartyLossInfo.getSerialNo()==0 || !bzFlagLoss))
//	                	&& "1".equals(compensateListVo.getBzDutyCar())){//车无责，损失项也无责，并且损失为路面财
//	                    continue;
//	                }else{
//	                    String bzCompensateText = String.format("\n交强险:", 
//	                                                            thirdPartyLossInfo.getLossName() , 
//	                                                            thirdPartyLossInfo.getLossFeeName() , 
//	                                                            thirdPartyInfo.getDutyAmount() ,
//	                                                            thirdPartyLossInfo.getSumLoss() , 
//	                                                            allDamageAmount - thirdPartyLossInfo.getDamageAmount());
//	                    double payAmount = thirdPartyLossInfo.getSumLoss() * thirdPartyInfo.getDutyAmount() / (allDamageAmount - thirdPartyLossInfo.getDamageAmount());
//	                    thirdPartyInfo.addThirdPartyPayAmount(payAmount, bzCompensateText, thirdPartyLossInfo, compensateExp);
////	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 初始化代赔标志位。
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	        for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                carThirdPartyCompInfo.setInsteadFlag("0"); // 非代赔
//	        }
//	    }
//	    //TODO
//	    if (compensateType.trim().equals("2") && !("1").equals(compensateListVo.getIsBiPCi())) {//商业计算书返回true，交强计算书返回false
//	        // 2.23设置代赔标志位
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp=true;
//	        	}
//	        	
//	            String insteadFlag = "4";
//	            if(thirdPartyInfo.getSerialNo()>1){ //三者车                   
//	                //三者无责、主车有责。
//	                if(!bzFlagTemp &&bzFlag){
//	                    insteadFlag = "1";//无责代赔
//	                }else 
//	                //无保有责才算无保代赔。
//	                if((thirdPartyInfo.getInsuredFlag().equals("0")||thirdPartyInfo.getInsuredFlag().equals("1")) 
//	                		&& bzFlagTemp){
//	                        insteadFlag = "2";//无保代赔
//	                }
//	            }
//	            
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	            	Boolean carThirdPartyCompInfoBzFlagTemp = false;
//	            	if(carThirdPartyCompInfo.getBzDutyType()!=null && carThirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		carThirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (carThirdPartyCompInfo.getSerialNo()==1 && carThirdPartyCompInfoBzFlagTemp) {//本车上损失，有责
//	                    carThirdPartyCompInfo.setInsteadFlag(insteadFlag);
//	                } else {
//	                    if(carThirdPartyCompInfo.getSerialNo()==1){//本车上损失，但无责
//	                        carThirdPartyCompInfo.setInsteadFlag("0"); // 非代赔
//	                    }
//	                    else{
//	                        carThirdPartyCompInfo.setInsteadFlag("4"); // 三者代赔三者
//	                    }
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 交强试算_20080201_财产_代赔
//	    if ("1".equals(compensateListVo.getBzDutyCar())) { // 存在有责车辆
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp=true;
//	        	}
//	            // 2.7.1无责方车辆个数
//	            if (!bzFlagTemp &&
//	                compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) { // 无责,车财损失
//	                noDutyCarNum++;
//	            }
//	            // 2.7.2有责方车辆个数
//	            if (bzFlagTemp &&
//	                compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) { // 有责,车财损失
//	                dutyCarNum++;
//	            }
//	        }
//
//	        // 2.7.3有车辆的损失个数。
//	        // 统计车辆的损失项数量。 统计各自车的损失项数量
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                    thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo()) &&
//	                    thirdPartyLossInfo.getSumLoss() != 0.00) { // 车财损失
//	                    thirdPartyInfo.setCarLossNum(thirdPartyInfo.getCarLossNum() + 1);
//	                }
//	            }
//	        }
//	        
//	        //计算有损有责车辆数量。
//	        //zhongyuhai 20121017
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//
//	        	if(thirdPartyInfo.getCarLossNum() > 0 && bzFlagTemp){
//	                dutyCarNumWithLoss++;
//	            }
//	        }
//
//	        // 2.7.5无责方赔付有责方
//	        // 人保规则：rule 新财产计算$40$1_a无责方赔付有责方$41$
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	            	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		thirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && // 车财损失
//	                    !bzFlagTemp && // 赔付损失车辆无责
//	                    thirdPartyCompInfoBzFlagTemp) { // 归属车辆交强有责
//	                    
//	                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss() / noDutyCarNum); // 损失金额 / 无责方车辆个数
//	                    
//	                    String bzCompensateText = String.format("\n交强险无责代赔 : %s \n本项理算金额 = 损失金额 / 无责方车辆个数 \n "+
//	                    		                                "= %s / %s \n "+
//	                    		                                "= %s" , 
//	                                                            thirdPartyCompInfo.getLossName() , 
//	                                                            thirdPartyCompInfo.getSumLoss() , 
//	                                                            noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum);
//	                    thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.6无责方赔付有责方_本车    本车为无责方赔付有责方
//	        // 人保规则：rule 新财产计算$40$1_a1无责方赔付有责方$_$本车$41$
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	            	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	            		thirdPartyCompInfoBzFlagTemp = true;
//		        	}
//	            	
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && // 车财损失
//	                		thirdPartyCompInfoBzFlagTemp &&
//	                    !bzFlagTemp && // 赔付损失车辆无责
//	                    thirdPartyInfo.getSerialNo()==1 // 涉案车辆为标的车
//	                    ) { // 归属车辆交强有责
//	                    
//	                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getSumLoss() / noDutyCarNum); // 损失金额 / 无责方车辆个数
//	                    
//	                    String bzCompensateText = String.format("\n交强险 : %s 的 %s \n本项理算金额 = 损失金额 / 无责方车辆个数 \n " +
//                                                                "= %s / %s \n" +
//                                                                "= %s" , 
//	                                                            thirdPartyCompInfo.getLossName() , 
//	                                                            thirdPartyCompInfo.getLossFeeName() , 
//	                                                            thirdPartyCompInfo.getSumLoss(), 
//	                                                            noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum);
//	                    thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.7赔付损失项赋值。
//	        // 人保规则：rule 新财产计算$40$1_b1赔付损失项赋值$41$。
//	        // 无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//
//	        	List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            if (!bzFlagTemp) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp &&
//	                        compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {
//	                        thirdPartyCompInfo.setPayAmount(thirdPartyInfo.getDutyAmount() / dutyCarNumWithLoss);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.8无责代赔限额控制
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	            if (!bzFlagTemp) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp &&
//	                        compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                        thirdPartyCompInfo.getSumRealPay() > thirdPartyCompInfo.getPayAmount()) { // 实赔金额 > 损失车辆 的 责任限额 / 有责方车辆个数
//	                        //无责方赔付有责方损失，该损失在无责方能取得的限额应该是无责方责任限额除以有责方数量以做平分。
//	                        thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount());
//	                        
//	                        String bzCompensateText = String.format("\n无责代赔金额超过无责方赔付限额 ， 故：\n本项理算金额 = 责任限额 / 有责方车辆个数 \n " +
//                                                                    "= %s / %s \n " +
//                                                                    "= %s", 
//	                                                                thirdPartyInfo.getDutyAmount(), 
//	                                                                dutyCarNumWithLoss , 
//	                                                                thirdPartyCompInfo.getPayAmount());
//	                        thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                        logger.debug(bzCompensateText);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.9车辆的损失金额合计（1/2/3）
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                if (thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo()) &&
//	                    compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {
//	                    // 赔付损失车辆 的车辆的总损失金额为 赔付损失车辆 的 车辆的总损失金额 + 赔付损失项 的 损失金额
//	                    thirdPartyInfo.setCarLossSum(thirdPartyInfo.getCarLossSum() + thirdPartyLossInfo.getSumLoss());
//	                }
//	            }
//	        }
//	        for(ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos){
//	            //路面理算项。
//	            if(thirdPartyLossInfo.getSerialNo()==0){
//	                thirdPartyLossInfo.setCarLossSum(thirdPartyLossInfo.getSumLoss());
//	            }
//	            else{
//	                for(ThirdPartyInfo thirdPartyInfo : infos){
//	                    if(thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())){
//	                        thirdPartyLossInfo.setCarLossSum(thirdPartyInfo.getCarLossSum());
//	                        break;
//	                    }
//	                }
//	            }
//	        }
//	        
//	        for(ThirdPartyInfo thirdPartyInfo : infos){
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                        thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        thirdPartyCompInfo.setCarLossSum(thirdPartyLossInfo.getCarLossSum()); // 赔付损失分项 的归属车辆的总损失金额为 赔付损失项 的 归属车辆的总损失金额
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.10车辆的赔付金额合计
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                !bzFlagTemp) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    thirdPartyInfo.setCarPaySum(thirdPartyInfo.getCarPaySum() + thirdPartyCompInfo.getSumRealPay()); // 赔付损失车辆 的总赔付金额为 赔付损失车辆 的 总赔付金额 + 赔付损失分项 的 实赔金额
//	                }
//	            }
//	        }
//
//	        // 2.7.11财产损失超限额调整--无责
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                !bzFlagTemp && 
//	                thirdPartyInfo.getCarPaySum() > thirdPartyInfo.getDutyAmount()) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 赔付车辆限额 * ( 赔付损失分项 的 损失金额 / 无责方车辆个数 ) / ( 赔付损失分项 的 归属车辆的总损失金额 / 无责方车辆个数 )
//	                    if(thirdPartyCompInfo.getCarLossSum() > 0) {
//	                		thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount() * (thirdPartyCompInfo.getSumLoss() / noDutyCarNum) / (thirdPartyCompInfo.getCarLossSum() / noDutyCarNum));
//	                	} else {
//	                		thirdPartyCompInfo.setSumRealPay(0.00);
//	                	}
//	                    String bzCompensateText = String.format("\n损失项之和超限额,进行超限额调整" +
//                                                                "\n本项实赔金额 = 赔付损失车辆交强限额 × 分项赔付金额 / 总赔付金额之和\n" +
//                                                                " = %s × %s / %s \n" +
//                                                                " = %s", 
//	                                                            thirdPartyCompInfo.getPayCarAmount() , 
//	                                                            thirdPartyCompInfo.getSumLoss() / noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getCarLossSum() / noDutyCarNum , 
//	                                                            thirdPartyCompInfo.getSumRealPay());
//	                    thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//
//	        // 2.7.12无责方赔付金额初始化
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            thirdPartyLossInfo.setNoDutyCarPay(0d);
//	        }
//
//	        // 2.7.13无责方赔付有责方金额合计
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            if (!bzFlagTemp) { // 无责
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp) { // 有责
//	                        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                                thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                                // 赔付损失项 的无责方赔付金额为 赔付损失项 的 无责方赔付金额 + 赔付损失分项 的 实赔金额
//	                                thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getNoDutyCarPay() + thirdPartyCompInfo.getSumRealPay());
//	                            }
//	                        }
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.14无责方赔付有责方金额调整
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                thirdPartyLossInfo.getNoDutyCarPay() > thirdPartyLossInfo.getSumLoss()) {
//	                thirdPartyLossInfo.setNoDutyCarPay(thirdPartyLossInfo.getSumLoss());
//	            }
//	        }
//
//	        // 2.7.15有责方赔付无责方
//	        // 2.7.16有责方赔付路面财产
//	        // 赔付无责方与赔付路财的理算公式一致
//	        for (ThirdPartyInfo thirdPartyInfo : infos) {
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	        	
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && bzFlagTemp) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=false;
//	                	if(thirdPartyCompInfo.getBzDutyType()!=null && thirdPartyCompInfo.getBzDutyType().trim().equals("1")) {
//	                		thirdPartyCompInfoBzFlagTemp = true;
//	    	        	}
//				    	
//	                    if (!thirdPartyCompInfoBzFlagTemp) {
//	                        thirdPartyCompInfo.setPayAmount(thirdPartyCompInfo.getSumLoss() / dutyCarNum);
//	                        String bzCompensateText = String.format("\n交强险 : %s 的 %s " +
//	                                                                "\n本项理算金额= 损失金额 / 有责方车辆个数\n" +
//                                                                    "= %s / %s \n" +
//                                                                    "= %s", 
//	                                                                thirdPartyCompInfo.getLossName() , 
//	                                                                thirdPartyCompInfo.getLossFeeName() , 
//	                                                                thirdPartyCompInfo.getSumLoss(),
//	                                                                dutyCarNum , 
//	                                                                thirdPartyCompInfo.getPayAmount());
//	                        thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                        logger.debug(bzCompensateText);
//	                    }
//	                }
//	            }
//	        }
//
//	        // 2.7.17有责方赔付有责方
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        	Boolean bzFlagTemp=false;
//	        	if(thirdPartyInfo.getBzDutyType()!=null && thirdPartyInfo.getBzDutyType().trim().equals("1")) {
//	        		bzFlagTemp = true;
//	        	}
//	            if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) && bzFlagTemp) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                	Boolean thirdPartyCompInfoBzFlagTemp=thirdPartyCompInfo.getBzDutyFlag();
//	                	
//	                    if (thirdPartyCompInfoBzFlagTemp) {
//	                        
//	                        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                            if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                                
//	                                thirdPartyCompInfo.setPayAmount((thirdPartyCompInfo.getSumLoss() - thirdPartyLossInfo.getNoDutyCarPay()) / (dutyCarNum - 1));
//	                                
//	                                String bzCompensateText = String.format("\n交强险 : %s 的 %s " +
//	                                                                        "\n本项理算金额 = (损失金额 - 无责方赔付金额) / (有责方车辆个数 - 1)" +
//	                                                                        "\n= (%s - %s) / (%s - 1)" +
//	                                                                        "\n= %s", 
//	                                                                        thirdPartyCompInfo.getLossName() , 
//	                                                                        thirdPartyCompInfo.getLossFeeName() , 
//	                                                                        thirdPartyCompInfo.getSumLoss() , 
//	                                                                        thirdPartyLossInfo.getNoDutyCarPay() ,
//	                                                                        dutyCarNum , 
//	                                                                        thirdPartyCompInfo.getPayAmount());
//	                                thirdPartyCompInfo.setBzCompensateText(bzCompensateText);
//	                                logger.debug(bzCompensateText);
//	                            }
//	                        }
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.8累计赔付第三方的金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                // 赔付损失车辆 的限额比例赔偿金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失车辆 的 限额比例赔偿金额
//	                thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount() + thirdPartyCompInfo.getPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.9超限额调整
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	            // 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 限额比例赔偿金额
//	            if (thirdPartyInfo.getDutyAmount() < thirdPartyInfo.getPayAmount()) {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyInfo.getDutyAmount() *
//	                                                       thirdPartyCompInfo.getPayAmount() / 
//	                                                       thirdPartyInfo.getPayAmount());
//	                    
//	                    String bzCompensateText = String.format("\n损失金额超过赔付限额，进行超限额调整 : " +
//	                                                            "\n= 本车责任限额 × 损失项的限额比例赔偿金额 / 损失项的限额比例赔偿金额之和" +
//	                                                            "\n= %s × %s / %s" +
//	                                                            "\n= %s", 
//	                                                            thirdPartyInfo.getDutyAmount() , 
//	                                                            thirdPartyCompInfo.getPayAmount() ,
//	                                                            thirdPartyInfo.getPayAmount() ,
//	                                                            thirdPartyCompInfo.getAdjPayAmount()
//	                                                            );
//	                    thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            } else {
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    // 赔付损失分项 的超限额赔付调整金额为 赔付损失分项 的 限额比例赔付金额
//	                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyCompInfo.getPayAmount());
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.10累计赔付金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                // 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
//	                thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount() + thirdPartyCompInfo.getAdjPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.11累计未赔付金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            // 损失赔付车辆 的 责任限额 is more than 损失赔付车辆 的 累计赔付金额
//	            if (thirdPartyInfo.getDutyAmount() > thirdPartyInfo.getAdjPayAmount()) {
//	                // 损失赔付车辆 的剩余限额为 损失赔付车辆 的 责任限额 - 损失赔付车辆 的 累计赔付金额
//	                thirdPartyInfo.setLeftAmount(thirdPartyInfo.getDutyAmount() - thirdPartyInfo.getAdjPayAmount());
//	            }
//	        }
//	    }
//
//	    // 2.12车辆项下分项损失转换为损失项下分项损失 TODO
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        
//	                        thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),
//	                                                                  thirdPartyCompInfo.getObjectIndex(),
//	                                                                  thirdPartyCompInfo.getAdjPayAmount(),
//	                                                                  thirdPartyCompInfo.getBzCompensateText(),
//	                                                                  thirdPartyLossInfo.getSerialNo()
//	                                                                  );
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (!thirdPartyInfo.getBzDutyFlag()) {
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        // 赔付损失项 .增加赔付分项损失(赔付损失项 的 车牌号码 , 车辆赔付损失分项 的 损失项序号 , 车辆赔付损失分项 的 实赔金额 , 车辆赔付损失分项 的 交强赔付公式 )
//	                        thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),
//	                                                                  thirdPartyCompInfo.getObjectIndex(),
//	                                                                  thirdPartyCompInfo.getSumRealPay(),
//	                                                                  thirdPartyCompInfo.getBzCompensateText(),
//	                                                                  thirdPartyLossInfo.getSerialNo()
//	                                                                  );
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.13合计损失项已赔付金额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//	        for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            // 赔付损失项 涉案车辆限额赔付金额为 赔付损失项 的 涉案车辆限额赔付金 + 赔付损失分项 的 超限额赔付调整金额
//	            thirdPartyLossInfo.setThirdPartyLimitPay(thirdPartyLossInfo.getThirdPartyLimitPay() + thirdPartyCompInfo.getAdjPayAmount());
//	        }
//	    }
//
//	    // 2.14分项损失未赔足金额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//	        for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//	            // 赔付损失分项 的未赔足金额为 赔付损失项 的 损失金额 - 赔付损失项 的 涉案车辆限额赔付金
//	            thirdPartyCompInfo.setLeftPay(thirdPartyLossInfo.getSumLoss() - thirdPartyLossInfo.getThirdPartyLimitPay());
//	        }
//	    }
//
//	    // 2.15损失项下分项损失转换为车辆项下分项损失
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()){
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> lossThirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 损失项下赔付损失分项
//	                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                    for (ThirdPartyCompInfo lossThirdPartyCompInfo : lossThirdPartyCompInfos) { // 损失项下赔付损失分项
//	                        if (carThirdPartyCompInfo.getObjectIndex().equals(lossThirdPartyCompInfo.getObjectIndex())) {
//	                            // 车辆赔付损失分项 的未赔足金额为 损失赔付损失分项 的 未赔足金额
//	                            carThirdPartyCompInfo.setLeftPay(lossThirdPartyCompInfo.getLeftPay());
//	                        }
//	                    }
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.16合计未赔足金额
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                // 赔付损失车辆 的赔付损失项未赔足金额为 赔付损失车辆 的 赔付损失项未赔足金额 + 赔付损失分项 的 未赔足金额
//	                thirdPartyInfo.setSumNoenoughPay(thirdPartyInfo.getSumNoenoughPay() + carThirdPartyCompInfo.getLeftPay());
//	            }
//	        }
//	    }
//
//	    // 2.17剩余限额调整金额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if ((thirdPartyInfo.getDutyAmount() > (thirdPartyInfo.getAdjPayAmount() + 0.005)) &&
//	                    carThirdPartyCompInfo.getLeftPay() > 0.005) {
//	                    //赔付损失分项 的剩余限额赔付金额为 赔付损失车辆 的 剩余限额 * 赔付损失分项 的 未赔足金额 / 赔付损失车辆 的 赔付损失项未赔足金额
//	                    carThirdPartyCompInfo.setLeftPayAmount(thirdPartyInfo.getLeftAmount() * 
//	                                                           carThirdPartyCompInfo.getLeftPay() / 
//	                                                           thirdPartyInfo.getSumNoenoughPay());
//	                    
//	                    String bzCompensateText = String.format("\n损失存在剩余限额 , 进行剩余限额分摊 : " +
//	                                                            "\n= 赔付剩余限额 × 损失项的未赔足金额 / 所有未赔付金额" +
//	                                                            "\n= %s × %s / %s" +
//	                                                            "\n= %s", 
//	                                                            thirdPartyInfo.getLeftAmount() , 
//	                                                            carThirdPartyCompInfo.getLeftPay() ,
//	                                                            thirdPartyInfo.getSumNoenoughPay(),
//	                                                            carThirdPartyCompInfo.getLeftPayAmount());
//	                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.19超损失调整
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag()) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if ((carThirdPartyCompInfo.getLeftPayAmount() > carThirdPartyCompInfo.getLeftPay()) &&
//	                    (carThirdPartyCompInfo.getLeftPay() > 0.000001)) {
//	                    
//	                    carThirdPartyCompInfo.setLeftPayAmount(new BigDecimal(carThirdPartyCompInfo.getLeftPay()).doubleValue());
//	                    
//	                    String bzCompensateText = String.format("\n剩余限额赔付金额超过未赔足金额 ， 所以 = %s", 
//	                                                            carThirdPartyCompInfo.getLeftPay());
//	                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//	                    logger.debug(bzCompensateText);
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.20实际赔付金额计算
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        if (thirdPartyInfo.getBzDutyFlag() || !"1".equals(compensateListVo.getBzDutyCar())) {
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if (carThirdPartyCompInfo.getAdjPayAmount() > 0) {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失分项 的 剩余限额赔付金额
//	                    carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getAdjPayAmount() + carThirdPartyCompInfo.getLeftPayAmount());
//	                    if("2".equals(compensateListVo.getPayType())){
//	                        carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
//	                    }
//	                } else {
//	                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失分项 的 剩余限额赔付金额
//	                    carThirdPartyCompInfo.setSumRealPay(carThirdPartyCompInfo.getPayAmount() + carThirdPartyCompInfo.getLeftPayAmount());
//	                    if("2".equals(compensateListVo.getPayType())){
//	                        carThirdPartyCompInfo.setNoRecoveryPay(carThirdPartyCompInfo.getSumRealPay());
//	                    }
//	                }
//	            }
//	        }
//	    }
//	    
//	    // 2.21组织返回信息<交强>
//	    // 商业计算书。
// 	    if (compensateType!=null&&(compensateType.trim().equals("1")||compensateType.trim().equals("2"))) { 
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	            	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_14组织返回信息(本车损失)");
//	            }
//	        }
//	    } 
//	    
//	    // 交强计算书。
//	    if (compensateType!=null&&!((compensateType.trim().equals("1")||compensateType.trim().equals("2")))) { 
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            if ("1".equals(thirdPartyInfo.getLossItemType())) {
//	                List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_14组织返回信息");
//	                }
//	            }
//	        }
//	        
//	        // 2.24组织返回信息
//	        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//	            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//	                if (compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//	                    !"1".equals(thirdPartyInfo.getLossItemType()) &&
//	                    "1".equals(carThirdPartyCompInfo.getLossItemType()) &&
//	                    ("1".equals(carThirdPartyCompInfo.getInsteadFlag()) || "2".equals(carThirdPartyCompInfo.getInsteadFlag()))) {
//	                	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_财产_交强_16组织返回信息");
//	                }
//	            }
//	        }
//	    }
//
//	    // 2.25按损失项顺序返回实赔
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                     ("0".equals(thirdPartyCompInfo.getInsteadFlag()) || 
//	                      "1".equals(thirdPartyCompInfo.getInsteadFlag()) || 
//	                      "2".equals(thirdPartyCompInfo.getInsteadFlag())||
//	                      ("1".equals(compensateListVo.getIsBiPCi())&&"4".equals(thirdPartyCompInfo.getInsteadFlag())))) {//商业扣交强时需要将三者赔付算上
//	                    
//	                    thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay() + thirdPartyCompInfo.getSumRealPay());
//	                    thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + "\n" + thirdPartyCompInfo.getBzCompensateText());
//	                    thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
//	                    thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount()); // 赔付损失项 的理算金额为 返回损失分项 的 限额比例赔付金额
//	                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//	                    thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
//	                    compensateListVo.setDutyCarNum(dutyCarNum);
//	                    thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
//	                    if("2".equals(thirdPartyCompInfo.getInsteadFlag())){
//	                        thirdPartyLossInfo.setNoPolicySumRealPaid(thirdPartyLossInfo.getNoPolicySumRealPaid() + thirdPartyCompInfo.getSumRealPay());
//	                        //thirdPartyLossInfo.addNoPolicyInsteadThirdPartyCompInfos(thirdPartyCompInfo);
//	                    }
//	                    logger.debug("交强理算："+thirdPartyLossInfo.getLicenseNo()+","+thirdPartyLossInfo.getLossName()+","+thirdPartyLossInfo.getSumRealPay());
//	                }
//	            }
//	    }
//
//	    // 2.26设置赔付损失项的超限额标志位
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                    thirdPartyCompInfo.getPayAmount() > thirdPartyCompInfo.getAdjPayAmount()) {
//	                    thirdPartyLossInfo.setOverFlag("1");
//	                }
//	            }
//	    }
//
//	    // 2.27设置赔付损失项的剩余限额赔付标志位
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//	                    thirdPartyCompInfo.getLeftPayAmount() > 0) {
//	                    thirdPartyLossInfo.setOverFlag("2");
//	                }
//	            }
//	    }
//
//	    // 2.29设置交强险无保代赔的所有限额
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//	                }
//	            }
//	    }
//
//	    // 2.30屏蔽本车损失以及空的理算公式
//	    if ("2".equals(compensateListVo.getPayType())) { // 清付
//	        
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	                List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//	                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//	                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//	                        if (("0".equals(thirdPartyCompInfo.getInsteadFlag()) || "2".equals(thirdPartyCompInfo.getInsteadFlag())) &&
//	                            thirdPartyLossInfo.getBzCompensateText() != null) {
//	                            String bzCompensateText = String.format("\n实际赔付金额 = (限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额)" +
//	                                                                    "\n= %s", 
//	                                                                    thirdPartyLossInfo.getSumRealPay());
//	                            thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
//	                            logger.debug(bzCompensateText);
//	                        }
//	                    }
//	                }
//	        }
//	    } else {
//	        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	            if (thirdPartyLossInfo.getBzCompensateText()!=null &&
//	                ("0".equals(thirdPartyLossInfo.getInsteadFlag()) || "2".equals(thirdPartyLossInfo.getInsteadFlag())) && 
//	                bzFlag) {
//	                String bzCompensateText = String.format("\n实际赔付金额 = 限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额" +
//                                                            "\n=%s", 
//	                                                        thirdPartyLossInfo.getSumRealPay());
//	                thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
//	                logger.debug(bzCompensateText);
//	            }
//	        }
//	    }
//
//	    // 17屏蔽本车损失(屏蔽空的理算公式)
//	    for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//	        if (thirdPartyLossInfo.getBzCompensateText()==null) {
//	            thirdPartyLossInfo.setBzCompensateText("");
//	        }
//	    }
//	    
//	    // 2.31清空赔付损失车辆信息
//	    for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//	        thirdPartyInfo.setLeftAmount(0); //剩余限额
//	        thirdPartyInfo.setLeftPayAmount(0); //剩余限额赔偿金额
//	        thirdPartyInfo.setAdjPayAmount(0); //累计赔付金额
//	        thirdPartyInfo.setDutyCompPay(0); //责任限额计算金额
//	        thirdPartyInfo.setSumNoenoughPay(0); //赔付损失项未赔足金额
//	        thirdPartyInfo.setPayAmount(0); //限额比例赔偿金额
//	        thirdPartyInfo.deleteThirdPartyPayAmount(); //赔付损失车辆.清空赔付损失分项
//	        thirdPartyInfo.setSumRealPay(0); //车辆合计赔付金额
//	        thirdPartyInfo.setCarPaySum(0); //总赔付金额
//	        thirdPartyInfo.setCarLossNum(0); //车辆损失个数
//	        thirdPartyInfo.setCarLossSum(0); //车辆的总损失金额
//	    }
//	    
//	    // 2.32清空赔付损失项
//	    compensateListVo.deleteReturnBZCompensateList();
//
//	    return compensateExp;
//	}
	  /**
     * 计算。用于代替规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险非互陪自赔部分人伤理算。
     * @param compensateList
     * @param compensateExp
     * @return
     */
//    private CompensateExp calculateCi4CommonPerson(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//    	logger.debug("交强险非互陪自赔部分人伤理算"+compensateExp.getDamageType());
//        double allDamageAmount = 0.00d; // 所有限额
//        List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//        List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//        final String compensateType = compensateListVo.getCompensateType()+"";
//
//        // 1累计赔付限额计算
//        // 人保规则：rule $49$累计赔付限额计算。
//        for (ThirdPartyInfo thirdPartyInfo : infos) {
//            allDamageAmount += thirdPartyInfo.getDutyAmount();
//        }
//
//        // 2获取损失项归属车辆限额
//        // 人保规则：rule $50$获取损失项归属车辆限额
//        for (ThirdPartyInfo thirdPartyInfo : infos) {
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                if (thirdPartyInfo.getSerialNo().equals(thirdPartyLossInfo.getSerialNo())) { // 车牌号相同
//                    thirdPartyLossInfo.setDamageAmount(thirdPartyInfo.getDutyAmount()); // 归属车辆赔限额
//                    thirdPartyLossInfo.setLossItemType(thirdPartyInfo.getLossItemType()); // 归属车辆类型
//                    thirdPartyLossInfo.setBzDutyType(thirdPartyInfo.getBzDutyType()); // 归属车辆交强责任类型
//                    thirdPartyLossInfo.setBzDutyFlag(thirdPartyInfo.getBzDutyFlag());
//                } else if (thirdPartyLossInfo.getSerialNo()==0) {//路面人是么有LicenseNo的，为无责方
//                    thirdPartyLossInfo.setBzDutyType("0"); // 归属车辆交强责任类型
//                    thirdPartyLossInfo.setBzDutyFlag(false);
//                }
//            }
//        }
//
//        // 3计算赔付第三方金额
//        // 人保规则：rule $51$计算赔付第三方金额
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                if (!thirdPartyLossInfo.getSerialNo().equals(thirdPartyInfo.getSerialNo())) {
//                	double payAmount = thirdPartyLossInfo.getSumLoss() * thirdPartyInfo.getDutyAmount() / (allDamageAmount - thirdPartyLossInfo.getDamageAmount());
//                    thirdPartyInfo.addThirdPartyPayAmount(payAmount,"", thirdPartyLossInfo, compensateExp);
//                }
//            }
//        }
//        
//        for(ThirdPartyInfo thirdPartyInfo : infos){
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                    carThirdPartyCompInfo.setInsteadFlag("0");
//            }
//        }
//        // 交强判断
//        if (!false) {
//            // 2.23设置代赔标志位
//            for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//                
//                String insteadFlag = "4";
//                if(!"1".equals(thirdPartyInfo.getLossItemType())){    
//                    //无保且有责才是无保代赔。
//                    if(thirdPartyInfo.getInsuredFlag().equals("0")||thirdPartyInfo.getInsuredFlag().equals("1")){
//                            insteadFlag = "2";
//                    }
//                }
//                
//                List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                    if ("1".equals(carThirdPartyCompInfo.getLossItemType())) {
//                        carThirdPartyCompInfo.setInsteadFlag(insteadFlag);
//                    } else {
//                        if("1".equals(thirdPartyInfo.getLossItemType())){
//                            carThirdPartyCompInfo.setInsteadFlag("0"); // 三者代赔三者
//                        }
//                        else{
//                            carThirdPartyCompInfo.setInsteadFlag("4"); // 三者代赔三者
//                        }
//                    }
//                }
//            }
//        }
//        
//        // 4累计赔付第三方金额
//        // 人保规则：rule $52$累计赔付第三方金额
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//            	for(ThirdPartyLossInfo thirdPartyLossInfoTemp : thirdPartyLossInfos) {
//            		if(thirdPartyLossInfoTemp.getObjectIndex().trim().equals(thirdPartyCompInfo.getObjectIndex().trim())) {
//            			String bzCompensateText =
//            				String.format("\n交强险 : %s \n本项理算金额 = 本车限额 × (%s / 分摊责任限额之和) \n"+
//            						"= %s × (%s / %s) \n", 
//            						thirdPartyCompInfo.getLossName(),
//            						thirdPartyCompInfo.getLossFeeName(),
//            						thirdPartyInfo.getDutyAmount(),
//            						thirdPartyCompInfo.getSumLoss(),
//            						allDamageAmount - thirdPartyLossInfoTemp.getDamageAmount());
//            			
//            			// 赔付损失车辆 的限额比例赔偿金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失车辆 的 限额比例赔偿金额
//            			thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount() + thirdPartyCompInfo.getPayAmount());
//            			// 赔付损失分项 的交强赔付公式为 赔付损失分项 的 交强赔付公式 + "                   ＝" + 理算大对象 . 数据格式化 ( 赔付损失分项 的 限额比例赔付金额 )
//            			thirdPartyCompInfo.setBzCompensateText(bzCompensateText +
//            					"=" +  
//            					thirdPartyCompInfo.getPayAmount());
//            			logger.debug(thirdPartyCompInfo.getBzCompensateText());
//            			logger.debug("此时的损失项的限额比例赔偿金额之和:"+thirdPartyInfo.getPayAmount());
//            		}
//            	}
//            }
//        }
//
//        // 5超限额调整
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//            // 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 限额比例赔偿金额
//            if (thirdPartyInfo.getDutyAmount() < thirdPartyInfo.getPayAmount()) {
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    // 赔付损失分项 的超限额赔付调整金额为 赔付损失车辆 的 责任限额 * 赔付损失分项 的 限额比例赔付金额 / 赔付损失车辆 的 限额比例赔偿金额
//                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyInfo.getDutyAmount() * 
//                                                       thirdPartyCompInfo.getPayAmount() / 
//                                                       thirdPartyInfo.getPayAmount());
//                    
//                    String bzCompensateText = String.format("\n损失金额超过赔付限额，进行超限额调整 : " +
//                                                            "\n= 本车责任限额 × 损失项的限额比例赔偿金额 / 损失项的限额比例赔偿金额之和" +
//                                                            "\n= %s × %s / %s" +
//                                                            "\n= %s", 
//                                                            thirdPartyInfo.getDutyAmount() , 
//                                                            thirdPartyCompInfo.getPayAmount() ,
//                                                            thirdPartyInfo.getPayAmount() ,
//                                                            thirdPartyCompInfo.getAdjPayAmount());
//                    
//                    thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//                    logger.debug(bzCompensateText);
//                }
//            } else {
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    // 赔付损失分项 的超限额赔付调整金额为 赔付损失分项 的 限额比例赔付金额
//                    thirdPartyCompInfo.setAdjPayAmount(thirdPartyCompInfo.getPayAmount());
//                }
//            }
//        }
//
//        // 6累计赔付金额
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//            // 赔付损失车辆 的 责任限额 is less than 赔付损失车辆 的 责任限额计算金额
//            if (thirdPartyInfo.getDutyAmount() < thirdPartyInfo.getDutyCompPay()) {
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    // 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
//                    thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount() + thirdPartyCompInfo.getAdjPayAmount());
//                    StringBuffer bzCompensateText = new StringBuffer();
//                    // "\n" + "                   ＝" + 理算大对象 .数据格式化( 赔付损失分项 的 超限额赔付调整金额 )
//                    bzCompensateText.append(thirdPartyCompInfo.getBzCompensateText() + 
//                    		                "\n" + 
//                    		                calculatorService.space(21) + 
//                                            thirdPartyCompInfo.getAdjPayAmount());
//                    thirdPartyCompInfo.setBzCompensateText(bzCompensateText.toString());
//                    logger.debug(bzCompensateText);
//                }
//            } else {
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    // 赔付损失车辆 的累计赔付金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失车辆 的 累计赔付金额
//                    thirdPartyInfo.setAdjPayAmount(thirdPartyInfo.getAdjPayAmount() + thirdPartyCompInfo.getAdjPayAmount());
//                }
//            }
//        }
//
//        // 7累计未赔付金额
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            // 损失赔付车辆 的 责任限额 is more than 损失赔付车辆 的 累计赔付金额
//            if (thirdPartyInfo.getDutyAmount() > thirdPartyInfo.getAdjPayAmount()) {
//                // 损失赔付车辆 的剩余限额为 损失赔付车辆 的 责任限额 - 损失赔付车辆 的 累计赔付金额
//                thirdPartyInfo.setLeftAmount(thirdPartyInfo.getDutyAmount() - thirdPartyInfo.getAdjPayAmount());
//            }
//        }
//
//        // 8车辆项下分项损失转换为损失项下分项损失
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 赔付损失分项
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//                        // 赔付损失项 .增加赔付分项损失(赔付损失项 的 车牌号码 , 车辆赔付损失分项 的 损失项序号 , 车辆赔付损失分项 的 超限额赔付调整金额 , 车辆赔付损失分项 的 交强赔付公式 )
//                        thirdPartyLossInfo.addThirdPartyPayAmount(thirdPartyLossInfo.getLicenseNo(),
//                                                                  thirdPartyCompInfo.getObjectIndex(),
//                                                                  thirdPartyCompInfo.getAdjPayAmount(),
//                                                                  thirdPartyCompInfo.getBzCompensateText(),
//                                                                  thirdPartyLossInfo.getSerialNo());
//                    }
//                }
//            }
//        }
//
//        // 9合计损失项已赔付金额
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                // 赔付损失项 涉案车辆限额赔付金额为 赔付损失项 的 涉案车辆限额赔付金 + 赔付损失分项 的 超限额赔付调整金额
//                thirdPartyLossInfo.setThirdPartyLimitPay(thirdPartyLossInfo.getThirdPartyLimitPay() + thirdPartyCompInfo.getAdjPayAmount());
//            }
//        }
//
//        // 10分项损失未赔足金额
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 赔付损失项 的 赔付损失分项
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                // 赔付损失分项 的未赔足金额为 赔付损失项 的 损失金额 - 赔付损失项 的 涉案车辆限额赔付金
//                thirdPartyCompInfo.setLeftPay(thirdPartyLossInfo.getSumLoss() - thirdPartyLossInfo.getThirdPartyLimitPay());
//                logger.debug("thirdPartyLossInfo.getSumLoss() - thirdPartyLossInfo.getThirdPartyLimitPay()=thirdPartyCompInfo.getLeftPay()");
//                logger.debug(thirdPartyLossInfo.getSumLoss()+" - " + thirdPartyLossInfo.getThirdPartyLimitPay()+" = "+thirdPartyCompInfo.getLeftPay()+"\n");
//            }
//        }
//
//        // 11损失项下分项损失转换为车辆项下分项损失
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                List<ThirdPartyCompInfo> lossThirdPartyCompInfos = thirdPartyLossInfo.getThirdPartyCompInfos(); // 损失项下赔付损失分项
//                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                    for (ThirdPartyCompInfo lossThirdPartyCompInfo : lossThirdPartyCompInfos) { // 损失项下赔付损失分项
//                        if (carThirdPartyCompInfo.getObjectIndex().equals(lossThirdPartyCompInfo.getObjectIndex())) {
//                            // 车辆赔付损失分项 的未赔足金额为 损失赔付损失分项 的 未赔足金额
//                            carThirdPartyCompInfo.setLeftPay(lossThirdPartyCompInfo.getLeftPay());
//                        }
//                    }
//                }
//            }
//        }
//
//        // 12合计未赔足金额
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                // 赔付损失车辆 的赔付损失项未赔足金额为 赔付损失车辆 的 赔付损失项未赔足金额 + 赔付损失分项 的 未赔足金额
//                thirdPartyInfo.setSumNoenoughPay(thirdPartyInfo.getSumNoenoughPay() + carThirdPartyCompInfo.getLeftPay());
//            }
//        }
//
//        // 13剩余限额调整金额计算
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                if ((thirdPartyInfo.getDutyAmount() > (thirdPartyInfo.getAdjPayAmount() + 0.005))
//                	  &&  carThirdPartyCompInfo.getLeftPay() > 0.005) {
//
//                    //赔付损失分项 的剩余限额赔付金额为 赔付损失车辆 的 剩余限额 * 赔付损失分项 的 未赔足金额 / 赔付损失车辆 的 赔付损失项未赔足金额
//                    carThirdPartyCompInfo.setLeftPayAmount(thirdPartyInfo.getLeftAmount() * 
//                                                           carThirdPartyCompInfo.getLeftPay() / 
//                                                           thirdPartyInfo.getSumNoenoughPay());
//                    String bzCompensateText = String.format("\n损失存在剩余限额 , 进行剩余限额分摊 : " +
//                                                            "\n= 赔付剩余限额 × 损失项的未赔足金额 / 所有未赔付金额" +
//                                                            "\n= %s × %s / %s" +
//                                                            "\n= %s", 
//                                                            thirdPartyInfo.getLeftAmount() , 
//                                                            carThirdPartyCompInfo.getLeftPay() ,
//                                                            thirdPartyInfo.getSumNoenoughPay(),
//                                                            carThirdPartyCompInfo.getLeftPayAmount());
//                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//                    logger.debug(bzCompensateText);
//                }
//            }
//        }
//
//        // 13剩余限额金额展示
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                if (carThirdPartyCompInfo.getLeftPayAmount() > 0.005) {
//                    String bzCompensateText = carThirdPartyCompInfo.getBzCompensateText() + 
//                                              "\n" + 
//                                              "=" +
//                                              carThirdPartyCompInfo.getLeftPayAmount();
//                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//                    logger.debug(bzCompensateText);
//                }
//            }
//        }
//
//        // 13超损失调整
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//            for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                if (carThirdPartyCompInfo.getLeftPayAmount() > (carThirdPartyCompInfo.getLeftPay() + 0.005)) {
//                    carThirdPartyCompInfo.setLeftPayAmount(carThirdPartyCompInfo.getLeftPay());
//                    String bzCompensateText = String.format("\n剩余限额赔付金额超过未赔足金额 ， 所以 = %s", 
//                                                            carThirdPartyCompInfo.getLeftPay());
//                    carThirdPartyCompInfo.setBzCompensateText(carThirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//                    logger.debug(bzCompensateText);
//                }
//            }
//        }
//
//        // 14组织返回信息
//        if (compensateListVo.getIsBiPCi()!=null&&"1".equals(compensateListVo.getIsBiPCi())) {//商业扣交强
//            for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//                List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_其它_14组织返回信息(本车损失)");
//                }
//            }
//        } 
//        
//        if (compensateType!=null&&(compensateType.trim().equals("2"))&&(compensateListVo.getIsBiPCi()==null||!"1".equals(compensateListVo.getIsBiPCi()))) {//交强，并且不是商业扣交强
//            for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//                if (!compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS) &&
//                    "1".equals(thirdPartyInfo.getLossItemType())) {
//                    List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//                    for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                    	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo, "交强试算_20080201_其它_14组织返回信息");
//                    }
//                }
//            }
//            
//            for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//                List<ThirdPartyCompInfo> carThirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos(); // 车辆下赔付损失分项
//                for (ThirdPartyCompInfo carThirdPartyCompInfo : carThirdPartyCompInfos) { // 车辆下赔付损失分项
//                    if("2".equals(carThirdPartyCompInfo.getInsteadFlag())){
//                    	compensateListVo.setreturnBZCompensateList(carThirdPartyCompInfo, thirdPartyInfo,
//                                                                 "交强试算_20080201_其它_14组织返回信息");
//                    }
//                }
//            }
//        }
//
//        // 15实际赔付金额计算
//        for(ThirdPartyInfo thirdPartyInfo : infos){
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//                if (thirdPartyCompInfo.getAdjPayAmount() > 0) {
//                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 超限额赔付调整金额 + 赔付损失分项 的 剩余限额赔付金额
//                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getAdjPayAmount() + thirdPartyCompInfo.getLeftPayAmount());
//                } else {
//                    // 赔付损失分项 的实赔金额为 赔付损失分项 的 限额比例赔付金额 + 赔付损失分项 的 剩余限额赔付金额
//                    thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount() + thirdPartyCompInfo.getLeftPayAmount());
//                }
//                thirdPartyCompInfo.setSumRealPay(new BigDecimal(thirdPartyCompInfo.getSumRealPay()).doubleValue());
//            }            
//        }
//
//        // 16按损失项顺序返回实赔
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//                    thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay() + thirdPartyCompInfo.getSumRealPay());
//                    thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + thirdPartyCompInfo.getBzCompensateText());
//                    thirdPartyLossInfo.setInsteadFlag(thirdPartyCompInfo.getInsteadFlag());
//                    thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount()); // 赔付损失项 的理算金额为 返回损失分项 的 限额比例赔付金额
//                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//                    thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
//                    thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
//                    
//                    if("2".equals(thirdPartyCompInfo.getInsteadFlag())){
//                    	thirdPartyLossInfo.setNoPolicySumRealPaid(thirdPartyLossInfo.getNoPolicySumRealPaid() + thirdPartyCompInfo.getSumRealPay());
//                        //thirdPartyLossInfo.addNoPolicyInsteadThirdPartyCompInfos(thirdPartyCompInfo);
//                    }
//                    logger.debug(thirdPartyLossInfo.getLicenseNo()+",交强责任："+thirdPartyCompInfo.getBzDutyType()+",损失名称："+thirdPartyLossInfo.getLossName()==null?"null":thirdPartyLossInfo.getLossName().trim()+",赔款金额："+thirdPartyLossInfo.getSumRealPay());
//                }
//            }
//        }
//
//        // 16设置赔付损失项的超限额标志位
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//                    thirdPartyCompInfo.getPayAmount() > thirdPartyCompInfo.getAdjPayAmount()) {
//                    thirdPartyLossInfo.setOverFlag("1");
//                }
//            }
//        }
//
//        // 16设置赔付损失项的剩余限额赔付标志位
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex()) &&
//                    thirdPartyCompInfo.getLeftPayAmount() > 0) {
//                    thirdPartyLossInfo.setOverFlag("");
//                }
//            }
//        }
//
//        // 16设置交强险无保代赔的所有限额
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            List<ThirdPartyCompInfo> thirdPartyCompInfos = compensateListVo.getReturnBZCompensateList();
//            for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) { // 损失项清单 的 返回损失分项
//                if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//                    thirdPartyLossInfo.setAllAmount(allDamageAmount);
//                }
//            }
//        }
//
//        // 17屏蔽本车损失
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            if ((thirdPartyLossInfo.getBzCompensateText()==null||thirdPartyLossInfo.getBzCompensateText().equals("")) &&
//                compensateExp.getDamageType().equals(CodeConstants.FeeTypeCode.PROPLOSS)) {
//                thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText());
//            } else {
//                String bzCompensateText = String.format("\n实际赔付金额 = 限额赔付比例赔付金额 + 超限额调整金额 + 剩余限额赔付金额" +
//                                                        "\n=%s", 
//                                                        thirdPartyLossInfo.getSumRealPay());
//                thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
//                logger.debug(bzCompensateText);
//            }
//        }
//
//        // 17屏蔽本车损失(屏蔽空的理算公式)
//        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//            if (thirdPartyLossInfo.getBzCompensateText()==null||thirdPartyLossInfo.getBzCompensateText().equals("") ||
//                ("1".equals(thirdPartyLossInfo.getLossItemType()) && !thirdPartyLossInfo.getInsteadFlag().equals("2"))
//            ) {
//                thirdPartyLossInfo.setBzCompensateText("");
//            }
//        }
//        
//        //扣减交强支付金额。
//        this.compulsorySubPrePay(compensateExp.getThirdPartyLossInfos());
//        
//        //this.calculateCi4CommonReCaseAdjust(thirdPartyLossInfos , compensateExp);
//        
//        // 2.31清空赔付损失车辆信息
//        for (ThirdPartyInfo thirdPartyInfo : infos) { // 车
//            thirdPartyInfo.setLeftAmount(0); //剩余限额
//            thirdPartyInfo.setLeftPayAmount(0); //剩余限额赔偿金额
//            thirdPartyInfo.setAdjPayAmount(0); //累计赔付金额
//            thirdPartyInfo.setDutyCompPay(0); //责任限额计算金额
//            thirdPartyInfo.setSumNoenoughPay(0); //赔付损失项未赔足金额
//            thirdPartyInfo.setPayAmount(0); //限额比例赔偿金额
//            thirdPartyInfo.deleteThirdPartyPayAmount(); //赔付损失车辆.清空赔付损失分项
//            thirdPartyInfo.setSumRealPay(0); //车辆合计赔付金额
//            thirdPartyInfo.setCarPaySum(0); //总赔付金额
//            thirdPartyInfo.setCarLossNum(0); //车辆损失个数
//            thirdPartyInfo.setCarLossSum(0); //车辆的总损失金额
//        }
//        
//        // 2.32清空赔付损失项
//        compensateListVo.deleteReturnBZCompensateList();
//
//        return compensateExp;
//    }

    /**
     * 计算。用于代替规则引擎方法/Compensate_getSumRealPayApp/Compensate_getSumRealPayRule的交强险互陪自赔部分。
     * @param compensateList
     * @param compensateExp
     * @return
     */
//    private CompensateExp calculateCi4Self(CompensateListVo compensateListVo, CompensateExp compensateExp) {
//        //人保规则：交强试算$_$20080201$_$财产$_$互碰自赔。
//        logger.debug("交强自保自赔部分：");
//        //1.初始化损失项车牌号。
//        {
//            List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                thirdPartyLossInfo.setLossItemType("1");
//                thirdPartyLossInfo.setBzDutyType(null);
//                thirdPartyLossInfo.setBzDutyFlag(false);
//            }
//        }
//
//        //2.初始化本车损失项。
//        //功能：将本车车损各损失项
//        {
//            List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//            List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//            for (ThirdPartyInfo thirdPartyInfo : infos) {
//                for (ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
//                    //代码中使用了licenseNo字段来存放PrpLthirdParty.id字段。
//                    if (thirdPartyInfo.getLicenseNo().equals(lossInfo.getLicenseNo())) {
//
//                        String bzCompensateText =
//                            String.format("\n" +
//                                          "交强险互碰自赔: %s \n" +
//                                          "本项理算金额= %s \n" +
//                                          "          = %s" + "\n", 
//                                          lossInfo.getLossName(),
//                                          lossInfo.getLossFeeName(),
//                                          lossInfo.getSumLoss());
//
//                        thirdPartyInfo.addThirdPartyPayAmount(lossInfo.getSumLoss(),bzCompensateText, lossInfo,compensateExp);
//                        logger.debug(bzCompensateText);
//                    }
//                }
//            }
//        }
//
//        //3.本车赔偿金额合计。
//        {
//            List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//            for (ThirdPartyInfo thirdPartyInfo : infos) {
//                List<ThirdPartyCompInfo> compInfos = thirdPartyInfo.getThirdPartyCompInfos();
//                for (ThirdPartyCompInfo lossInfo : compInfos) {
//                    thirdPartyInfo.setPayAmount(thirdPartyInfo.getPayAmount() + lossInfo.getPayAmount());
//                }
//            }
//        }
//
//        //4.设置超限额标志位。
//        {
//            List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//            List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//            for (ThirdPartyInfo thirdPartyInfo : infos) {
//                if (thirdPartyInfo.getPayAmount() > thirdPartyInfo.getDutyAmount()) {
//                    List<ThirdPartyCompInfo> compInfos = thirdPartyInfo.getThirdPartyCompInfos();
//                    for (ThirdPartyCompInfo lossInfo : compInfos) {
//                        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                            if (lossInfo.getObjectIndex().equals(thirdPartyLossInfo.getObjectIndex())) {
//                                thirdPartyLossInfo.setOverFlag("1");
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        //5.超限额调整。
//        //人保规则：rule $54$累计赔付金额
//        {
//            List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//            for (ThirdPartyInfo thirdPartyInfo : infos) {
//                List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//                for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                    if (thirdPartyInfo.getDutyAmount() < thirdPartyInfo.getPayAmount()) {
//                        thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount() *
//                                                         thirdPartyInfo.getDutyAmount() /
//                                                         thirdPartyInfo.getPayAmount());
//
//                        String bzCompensateText =
//                            String.format("%s赔付超限额，进行超限额调整:\n" +
//                                          "= 本项赔偿金额 × 本车责任限额 / 本车赔偿金额合计\n" +
//                                          "= %s × %s / %s \n" +
//                                          "= %s", 
//                                          thirdPartyCompInfo.getBzCompensateText(),
//                                          thirdPartyCompInfo.getPayAmount(),
//                                          thirdPartyInfo.getDutyAmount(),
//                                          thirdPartyInfo.getPayAmount(),
//                                          thirdPartyCompInfo.getSumRealPay());
//                        thirdPartyCompInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText() + bzCompensateText);
//                        logger.debug(bzCompensateText);
//                    } else {
//                        thirdPartyCompInfo.setSumRealPay(thirdPartyCompInfo.getPayAmount());
//                    }
//                }
//            }
//        }
//
//        //6.按损失项顺序返回实赔。
//        //人保规则：
//        {
//            List<ThirdPartyLossInfo> thirdPartyLossInfos = compensateExp.getThirdPartyLossInfos();
//            for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
//                List<ThirdPartyInfo> thirdPartyInfos = compensateExp.getThirdPartyInfos();
//                for (ThirdPartyInfo thirdPartyInfo : thirdPartyInfos) {
//                    List<ThirdPartyCompInfo> thirdPartyCompInfos = thirdPartyInfo.getThirdPartyCompInfos();
//                    for (ThirdPartyCompInfo thirdPartyCompInfo : thirdPartyCompInfos) {
//                        if (thirdPartyLossInfo.getObjectIndex().equals(thirdPartyCompInfo.getObjectIndex())) {
//                            thirdPartyLossInfo.setSumRealPay(thirdPartyCompInfo.getSumRealPay() +
//                                                             thirdPartyLossInfo.getSumRealPay());
//                            thirdPartyLossInfo.setBzCompensateText(thirdPartyCompInfo.getBzCompensateText());
//                            thirdPartyLossInfo.setSumDefLoss(thirdPartyCompInfo.getPayAmount());
//                            thirdPartyLossInfo.setAllAmount(thirdPartyInfo.getPayAmount());
//                            thirdPartyLossInfo.setPayCarAmout(thirdPartyCompInfo.getPayCarAmount());
//                            thirdPartyLossInfo.setDamageType(compensateExp.getDamageType());
//                            logger.debug(thirdPartyLossInfo.getLicenseNo()+","+thirdPartyLossInfo.getLossName()+","+thirdPartyLossInfo.getSumRealPay());
//                        }
//                    }
//                }
//            }
//        }
//        
//        //扣减交强支付金额。
//        this.compulsorySubPrePay(compensateExp.getThirdPartyLossInfos());
//        
//        //7.清空赔付损失车辆信息。
//        {
//            List<ThirdPartyInfo> infos = compensateExp.getThirdPartyInfos();
//            for (ThirdPartyInfo thirdPartyInfo : infos) {
//                thirdPartyInfo.setLeftAmount(0d);
//                thirdPartyInfo.setLeftPayAmount(0d);
//                thirdPartyInfo.setAdjPayAmount(0d);
//                thirdPartyInfo.setDutyCompPay(0d);
//                thirdPartyInfo.setSumNoenoughPay(0d);
//                thirdPartyInfo.setPayAmount(0d);
//                thirdPartyInfo.setSumRealPay(0d);
//                thirdPartyInfo.setCarPaySum(0d);
//                thirdPartyInfo.setCarLossNum(0d);
//                thirdPartyInfo.setCarLossSum(0d);
//                thirdPartyInfo.deleteThirdPartyPayAmount();
//            }
//        }
//
//        return compensateExp;
//    }
    /**
     * 获取交强险限额。(废弃使用，统一使用ConfigService.calBzAmount)
     * @param damageType 损失类型（车+财、医疗费、死亡伤残）。
     * @param isBzDutyType 交强险责任（有责、无责）。
     * @return
     */
    public static Double calBzAmount(String damageType , boolean isBzDutyType){
        Double amount = null;
        
        if(CodeConstants.FeeTypeCode.PROPLOSS.equals(damageType)){
            if(isBzDutyType){
                amount = 2000d;
            }
            else{
                amount = 100d;
            }
        }
        
        if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(damageType)){
            if(isBzDutyType){
                amount = 10000d;
            }
            else{
                amount = 1000d;
            }
        }
        
        if(CodeConstants.FeeTypeCode.PERSONLOSS.equals(damageType)){
            if(isBzDutyType){
                amount = 110000d;
            }
            else{
                amount = 11000d;
            }
        }
        return amount;
    }
    private void compulsorySubPrePay(List<ThirdPartyLossInfo> thirdPartyLossInfos){
        
        for (ThirdPartyLossInfo thirdPartyLossInfo : thirdPartyLossInfos) {
            if(thirdPartyLossInfo.getBzCompensateText()!=null){
                double sumRealPay = thirdPartyLossInfo.getSumRealPay() - 0.00;
                sumRealPay = sumRealPay == 0d ? 0 :sumRealPay;
                String bzCompensateText =
                    String.format("\n赔款 = 实赔金额 - 交强支付金额" +
                                  "\n=%s - %s" +
                                  "\n=%s", 
                                  thirdPartyLossInfo.getSumRealPay(),
                                  0.00,
                                  sumRealPay);
                thirdPartyLossInfo.setSumRealPay(sumRealPay);
                thirdPartyLossInfo.setBzCompensateText(thirdPartyLossInfo.getBzCompensateText() + bzCompensateText);
            }
        }
    }
    /**
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = BigDecimal.ONE;
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 去掉List中为空的对象
	 */
	@SuppressWarnings("unchecked")
	public List takeOffNullObject(List list) {
		List newList = new ArrayList();
		if(list != null){
			for (Object object : list) {
				if (object != null) {
					newList.add(object);
				}
			}
		}
		return newList;
	}
	
	/**
	 * 理算交强险一分钱调整
	 * @param returnCompensateList
	 * @param calculateType
	 * @return
	 */
	public List<ThirdPartyLossInfo> getThirdPartyLossInfolistBz(CompensateListVo returnCompensateList,String calculateType) {//type compensate 表示理算 ； claim 表示立案
		List<CompensateExp> returnCompensateExpList = returnCompensateList.getCompensateList();
		List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = returnCompensateList.getThirdPartyRecoveryInfoAry();// 代交强赔付信息
		List<ThirdPartyLossInfo> thirdPartyLossInfos;
		List<ThirdPartyInfo> thirdPartyInfos;
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		Integer serialNo = -1;
		 //理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较总的赔付金额存在的误差
        Double thirdPartyLossSumRealPayFour = 0.0000;//用每个理算大对象的实赔金额之和保留四位小数的进行相加
        Double thirdPartyLossSumRealPayTwo = 0.00;//用每个理算大对象的实赔金额之和保留两位小数的进行相加
        Double allDiffer=0.00;//本次赔付总金额相差金额
        // 理算一分钱误差处理 add by chenrong 2013-09-22 end 比较总的赔付金额存在的误差
		for (CompensateExp exp : returnCompensateExpList) {
			thirdPartyLossInfos = exp.getThirdPartyLossInfos();
			thirdPartyInfos = exp.getThirdPartyInfos();
			if(!"claim".equals(calculateType)) {
				for (ThirdPartyInfo carInfo : thirdPartyInfos) {
					if (carInfo.getSerialNo()==1) {
						serialNo = carInfo.getSerialNo();
					}
				}
			}
			//理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较每个理算大对象存在的误差
            Double  differ = 0.00;//保留四位小数相加后的结果四舍五入取小数点后两位-保留两位小数相加之和
            Double  thirdPartyLossInfoAllSumRealPayFour = 0.0000;//该理算大对象的所有损失项实赔金额之和（保留四位小数相加）
            Double  thirdPartyLossInfoAllSumRealPayTwo = 0.00;//该理算大对象的所有损失项实赔金额之和(保留两位小数相加)
            Double  thirdPartyLossInfoAllSumRealPay = 0.000;//防止超限额后再对此理算对象进行一分钱处理
            if(thirdPartyLossInfos != null && thirdPartyLossInfos.size() > 0) {
                for(ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
                    if (!Double.isInfinite(lossInfo.getSumRealPay()) && !Double.isNaN(lossInfo.getSumRealPay())) {
                        // sumRealPay可能为NaN
                    	thirdPartyLossInfoAllSumRealPay = thirdPartyLossInfoAllSumRealPay+DataUtils.round(lossInfo.getSumRealPay(),6);
                        thirdPartyLossInfoAllSumRealPayFour = thirdPartyLossInfoAllSumRealPayFour+DataUtils.round(lossInfo.getSumRealPay(),4);
                        Double sumRealPayTemp = new BigDecimal(lossInfo.getSumRealPay()).setScale(2, 4).doubleValue();
                        thirdPartyLossInfoAllSumRealPayTwo = thirdPartyLossInfoAllSumRealPayTwo + sumRealPayTemp;
                        if(sumRealPayTemp > lossInfo.getSumRealPay()) {//五入
							lossInfo.setRoundFlag("2");
						} else if (sumRealPayTemp < lossInfo.getSumRealPay()) {//四舍
							lossInfo.setRoundFlag("1");
						} else {
							lossInfo.setRoundFlag("0");
						}
                    }
                }
            }
            thirdPartyLossSumRealPayFour = thirdPartyLossSumRealPayFour+DataUtils.round(thirdPartyLossInfoAllSumRealPayFour,4);
            thirdPartyLossInfoAllSumRealPayFour = DataUtils.round(thirdPartyLossInfoAllSumRealPayFour,2);
            thirdPartyLossSumRealPayTwo = thirdPartyLossSumRealPayTwo + thirdPartyLossInfoAllSumRealPayFour;
            
            thirdPartyLossInfoAllSumRealPayTwo = DataUtils.round(thirdPartyLossInfoAllSumRealPayTwo,2);
            differ = thirdPartyLossInfoAllSumRealPayFour - thirdPartyLossInfoAllSumRealPayTwo;
            differ = DataUtils.round(differ, 2);
            allDiffer = thirdPartyLossSumRealPayFour - thirdPartyLossSumRealPayTwo;
            allDiffer = DataUtils.round(allDiffer,2);
            thirdPartyLossInfoAllSumRealPay = DataUtils.round(thirdPartyLossInfoAllSumRealPay,4);
            if(thirdPartyLossInfoAllSumRealPay > thirdPartyLossInfoAllSumRealPayFour) {//该理算大对象进行了四舍
            	if(thirdPartyLossInfos!=null && thirdPartyLossInfos.size()>0) {
	                for(ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
	                	lossInfo.setRoundFlagExp("1"); 
	                }
                }
            } else if(thirdPartyLossInfoAllSumRealPay < thirdPartyLossInfoAllSumRealPayFour) {//该理算大对象进行了五入
            	if(thirdPartyLossInfos!=null && thirdPartyLossInfos.size()>0) {
	                for(ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
	                	lossInfo.setRoundFlagExp("2"); 
	                }
                }
            } else {
            	if(thirdPartyLossInfos != null && thirdPartyLossInfos.size() > 0) {
	                for(ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
	                	lossInfo.setRoundFlagExp("0"); 
	                }
                }
            }
            //理算一分钱误差处理 add by chenrong 2013-09-22 end 比较每个理算大对象存在的误差
			for (ThirdPartyLossInfo lossInfo : thirdPartyLossInfos) {
				if(!"claim".equals(calculateType)) {
					if ("3".equals(String.valueOf(exp.getCompensateType()))) {
						lossInfo.setInsteadFlag("0");
					}
				}
				lossInfo.setDamageType(exp.getDamageType());
				if (!Double.isInfinite(lossInfo.getSumRealPay()) && !Double.isNaN(lossInfo.getSumRealPay())) {
					// sumRealPay可能为NaN
					Double sumRealPay = new BigDecimal(lossInfo.getSumRealPay()).setScale(2, 4).doubleValue();
					logger.debug(sumRealPay);
					//理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较每个理算大对象存在的误差
					//lossInfo.setSumRealPay(sumRealPay);
					if(differ>0 && lossInfo.getRoundFlag().trim().equals("1")) {//舍得多，将进行舍得加一
						lossInfo.setSumRealPay(sumRealPay+0.01);
						lossInfo.setRoundFlag("0");
						differ = differ - 0.01;
					} else if (differ<0 && lossInfo.getRoundFlag().trim().equals("2")) {//进的多，将进行进位的减一
						lossInfo.setSumRealPay(sumRealPay - 0.01);
						lossInfo.setRoundFlag("0");
						differ = differ + 0.01;
					} else {
						lossInfo.setSumRealPay(sumRealPay);
					}
					//理算一分钱误差处理 add by chenrong 2013-09-22 end 比较每个理算大对象存在的误差
				}
				lossInfo.setExgratiaFee(returnCompensateList.getExgratiaFee());
				if(!"claim".equals(calculateType)) {
					lossInfo.setThirdPartyRecoveryInfoAry(thirdPartyRecoveryInfoList);
					if ("2".equals(exp.getClaimType())){//互碰自赔，指加入标的车损失数据
//							|| (exp.getClaimType() != null && ("A".equals(exp.getClaimType().trim()) || "D".equals(exp.getClaimType().trim()))
//									&& !(exp.getComCode().startsWith("11")) && !(exp.getComCode().startsWith("4403")) && !(exp.getComCode().startsWith("50")))) {
						if (serialNo.equals(lossInfo.getSerialNo())) {
							thirdPartyLossInfolist.add(lossInfo);
						}
					} else {
						thirdPartyLossInfolist.add(lossInfo);
					}
				} else {
					thirdPartyLossInfolist.add(lossInfo);
				}
			}
		}
		//理算一分钱误差处理 add by chenrong 2013-09-22 begin 比较总的赔付金额存在的误差
		if(allDiffer != 0) {
			for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfolist) {
				if(allDiffer>0 && thirdPartyLossInfo.getRoundFlagExp().trim().equals("1") && thirdPartyLossInfo.getRoundFlag().trim().equals("1")) {//舍得多，将进行舍得加一
					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()+0.01);
					thirdPartyLossInfo.setRoundFlag("0");
					allDiffer=allDiffer-0.01;
				} else if(allDiffer<0 && thirdPartyLossInfo.getRoundFlagExp().trim().equals("2") && thirdPartyLossInfo.getRoundFlag().trim().equals("2")) {//进得多，将进行进的减一
					thirdPartyLossInfo.setSumRealPay(thirdPartyLossInfo.getSumRealPay()-0.01);
					thirdPartyLossInfo.setRoundFlag("0");
					allDiffer=allDiffer+0.01;
				} 
				if(allDiffer==0) {
					break;
				}
			}
		}
		//理算一分钱误差处理 add by chenrong 2013-09-22 end 比较总的赔付金额存在的误差
		return thirdPartyLossInfolist;
	}
}

