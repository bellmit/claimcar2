package ins.sino.claimcar.claim.calculator;

import ins.framework.lang.Datas;
import ins.framework.service.CodeTranService;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
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
import ins.sino.claimcar.claim.vo.MItemKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.claim.vo.ThirdPartyLossInfo;
import ins.sino.claimcar.claim.vo.ThirdPartyRecoveryInfo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("calculatorBI")
public class CalculatorBI implements Calculator {
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(CalculatorBI.class);
	@Autowired
	private RegistQueryService registQueryService;// 报案Service
	@Autowired
	private ClaimService claimService;// 立案service
	@Autowired
    private CheckTaskService checkTaskService;
	@Autowired
	private CompensateTaskService compensateTaskService;// 理算
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
	private CalculatorCI calculatorCI;
	@Autowired
	private ClaimTaskExtService claimTaskExtService;
	@Autowired
	CodeTranService codeTranService;
	/**
	 * 商业立案估计赔款刷新计算
	 */
	@Override
	public CompensateListVo calculate(CompensateListVo compensateListVo) {
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(compensateListVo.getClaimFeeCondition().getRegistNo(),
				compensateListVo.getClaimFeeCondition().getPolicyNo());
		ClaimFeeCondition claimCond = compensateListVo.getClaimFeeCondition();
		
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLCMainVo.getRegistNo());
		PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(prpLRegistVo.getRegistNo());
		PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(prpLRegistVo.getRegistNo(), prpLCMainVo.getPolicyNo());
		if(prpLClaimVo==null){
			prpLClaimVo = new PrpLClaimVo();
			prpLClaimVo.setRegistNo(prpLRegistVo.getRegistNo());
			prpLClaimVo.setPolicyNo(claimCond.getPolicyNo());
			prpLClaimVo.setComCode(prpLCMainVo.getComCode());
//			prpLClaimVo.setLflag(prpLRegistVo.getLflag());
			prpLClaimVo.setRiskCode(prpLCMainVo.getRiskCode());
			prpLClaimVo.setDamageTime(prpLRegistVo.getDamageTime());
			prpLClaimVo.setDamageCode(prpLCheckVo.getDamageCode());// 出险原因代码
		}
		prpLClaimVo.setCiIndemDuty(claimCond.getCiDutyFlag());
		prpLClaimVo.setClaimType(claimCond.getClaimType());
		prpLClaimVo.setIndemnityDuty(claimCond.getIndemnityDuty());
		prpLClaimVo.setIndemnityDutyRate(claimCond.getIndemnityDutyRate());
		// 组织损失项
		CompensateVo compensateVo = calculatorService.orgnizeCalculateData(prpLRegistVo,prpLCMainVo,prpLCheckVo,prpLClaimVo,"1");
		
		// 根据车财损失计算车损占比字段值carLossRate
		BigDecimal carLossRate = claimTaskExtService.getCarLossRate(compensateVo);
		
		// 组织规则入参，及规则调用
		CompensateListVo returnCompensateList = this.calCulatorBi(compensateVo,prpLClaimVo);
		returnCompensateList.setClaimFeeCondition(compensateListVo.getClaimFeeCondition());
		returnCompensateList.setCarLossRate(carLossRate);
		return returnCompensateList;
	}

	/**
	 * 数据组织完成后进行规则的调用
	 */
	public CompensateListVo calCulatorBi(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo){
		CompensateListVo compensateListVo = new CompensateListVo();
		PrpLCMainVo prpLCMainVo = compensateVo.getPrpLCMainVo();
		Timestamp beginTime = new Timestamp(new Date().getTime());
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo()) +"立案商业估计赔款理算开始，Start Time:"+beginTime);
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo()) +"立案商业估计赔款理算,扣交强开始：");
		// 商业理算前，进行交强险计算
		CompensateListVo returnCompensateListCi = this.calCulatorCi(compensateVo,prpLClaimVo);
		// 1.首先组织扣交强数据,将List<ThirdPartyLossinfo>的计算结果赋值给PrpLloss.sumBZpaid & sumRescueBZPaid.
		// 并将Loss重新组织成CompensateVo返回,至此商业计算书扣交强数据组织完成.
		compensateVo = this.rebuildBZCompensateVo(compensateVo,returnCompensateListCi);// 扣交强
		if(CodeConstants.ClaimType.EACHHIT_SELFLOSS_CICASE_SUB.equals(compensateVo.getPrpLCompensateVo().getCaseType())){
			if(compensateVo.getPrpLLossItemVoList() != null && !compensateVo.getPrpLLossItemVoList().isEmpty()){
				// 代商业
				PrpLLossItemVo prpLLossItemVoForABi = new PrpLLossItemVo(); 
				// 代交强
				PrpLLossItemVo prpLLossItemVoForACi = new PrpLLossItemVo();
				BigDecimal sumLossForMain = BigDecimal.ZERO;
//				BigDecimal sumRestForMain = BigDecimal.ZERO;
				BigDecimal rescueFeeForMain = BigDecimal.ZERO;
				BigDecimal rescueFeeBzPaid = BigDecimal.ZERO;
				BigDecimal bzPaid = BigDecimal.ZERO;
				boolean isHasA = false;// 是否存在A险
				
				for(PrpLLossItemVo prpLLossItemVo : compensateVo.getPrpLLossItemVoList()){
					// 将A险损失汇总 记录代商业 代交强部分
					if(CodeConstants.KINDCODE_A_LIST.contains(prpLLossItemVo.getKindCode().trim())){
						isHasA = true;
						sumLossForMain = sumLossForMain.add(prpLLossItemVo.getSumLoss());
//						sumRestForMain = sumRestForMain.add(prpLLossItemVo.getRescueFee());
						rescueFeeForMain = rescueFeeForMain.add(prpLLossItemVo.getRescueFee());
						rescueFeeBzPaid = rescueFeeBzPaid.add(prpLLossItemVo.getBzPaidRescueFee());
						bzPaid = bzPaid.add(prpLLossItemVo.getBzPaidLoss());
						Datas.copySimpleObjectToTargetFromSource(prpLLossItemVoForABi, prpLLossItemVo, false);
					}
					
					if(isHasA){
						prpLLossItemVoForABi.setSumLoss(sumLossForMain);
//						prpLLossPropVoForABi.setSumRest(sumRestForMain);
						prpLLossItemVoForABi.setRescueFee(rescueFeeForMain);
						prpLLossItemVoForABi.setBzPaidRescueFee(rescueFeeBzPaid);
						prpLLossItemVoForABi.setBzPaidLoss(bzPaid);
						Datas.copySimpleObjectToTargetFromSource(prpLLossItemVoForACi, prpLLossItemVoForABi, false);
					}
					compensateVo.getPrpLLossItemVoList().add(prpLLossItemVoForACi);
					compensateVo.getPrpLLossItemVoList().add(prpLLossItemVoForABi);
				}
			}
		}
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo()) +"立案商业估计赔款理算,扣交强结束！");
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo()) +"，调用事故免赔率与可选免赔率规则进行免赔率计算开始.");
		
		// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140910,BEGIN
		compensateVo.getPrpLCompensateVo().setPrpLLossProps(compensateVo.getPrpLLossPropVoList());
		
		// 根据损失信息判断是否需要带出无法找到第三方特约险 begin
		PrpLCMainVo prpLCMainVoTemp = policyViewService.getPolicyInfo(prpLClaimVo.getRegistNo(),prpLClaimVo.getPolicyNo());
		if(policyViewService.isNewClauseCode(prpLClaimVo.getRegistNo())){// 新条款
			// 新条款
			boolean isHasThirdKindCode = false;// 是否承保了无法找到第三方特约险
			boolean checkFindThirdParty = false;// 是否 承保了无法找到第三方特约险 且 勾选了无法找到第三方可选免赔条件
			String kindCode = "";
			Integer itemKindNo = null ;
			for (PrpLCItemKindVo itemKind : prpLCMainVoTemp.getPrpCItemKinds()) {
				if (itemKind.getKindCode() != null) {
					if (CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKind.getKindCode())) {
						isHasThirdKindCode = true;
						kindCode = itemKind.getKindCode();
//						itemKindNo = itemKind.getItemKindNo();
						break;
					}
				}
			}
			if(isHasThirdKindCode){// 承保了无法找到第三方特约险
				List<PrpLClaimDeductVo> prpLclaimDeductInfoList = registQueryService.findIsCheckClaimDeducts(prpLClaimVo.getPolicyNo());
			    if(prpLclaimDeductInfoList != null && prpLclaimDeductInfoList.size() > 0){
			    	for(PrpLClaimDeductVo prpLClaimDeductVo : prpLclaimDeductInfoList){
			    		if("120".equals(prpLClaimDeductVo.getDeductCondCode().trim())  
			    			||	"320".equals(prpLClaimDeductVo.getDeductCondCode().trim())){
							checkFindThirdParty = true;// 承保了无法找到第三方特约险 且 勾选了无法找到第三方可选免赔条件
			    			break;
			    		}
			    	}
			    }
			}
			if(checkFindThirdParty){// 承保了无法找到第三方特约险 且 勾选了无法找到第三方可选免赔条件
				// 计算无法找到第三方特约险估计赔款
				if(compensateVo.getPrpLLossItemVoList()!=null&& !compensateVo.getPrpLLossItemVoList().isEmpty()){// 存在车辆损失
					PrpLLossItemVo prpLlossForCar = new PrpLLossItemVo();
					Datas.copySimpleObjectToTargetFromSource(prpLlossForCar, compensateVo.getPrpLLossItemVoList().get(0), false);
					prpLlossForCar.setSumLoss(BigDecimal.ZERO);
//					prpLlossForCar.setSumRest(BigDecimal.ZERO);
					prpLlossForCar.setRescueFee(BigDecimal.ZERO);
					prpLlossForCar.setBzPaidLoss(BigDecimal.ZERO);
					prpLlossForCar.setBzPaidRescueFee(BigDecimal.ZERO);
					prpLlossForCar.setKindCode(kindCode);
//					prpLlossForCar.setItemKindNo(itemKindNo);
					boolean isAdd = false;
					for(PrpLLossItemVo prpLLossItemVo : compensateVo.getPrpLLossItemVoList()){
						if(CodeConstants.KINDCODE_A_LIST.contains(prpLLossItemVo.getKindCode())){
							isAdd = true;
							if(prpLLossItemVo.getSumLoss() != null){
								prpLlossForCar.setSumLoss(prpLlossForCar.getSumLoss().add(prpLLossItemVo.getSumLoss()));// 小计
							}
							if(prpLLossItemVo.getRescueFee() != null){
								prpLlossForCar.setRescueFee(prpLlossForCar.getRescueFee().add(prpLLossItemVo.getRescueFee()));// 施救费
							}
							if(prpLLossItemVo.getBzPaidLoss() != null){
								prpLlossForCar.setBzPaidLoss(prpLlossForCar.getBzPaidLoss().subtract(prpLLossItemVo.getBzPaidLoss()));// 定损扣交强
							}
							if(prpLLossItemVo.getBzPaidRescueFee() != null){
								prpLlossForCar.setBzPaidRescueFee(prpLlossForCar.getBzPaidRescueFee().subtract(prpLLossItemVo.getBzPaidRescueFee()));// 施救费扣交强
							}
//							if(prpLLossItemVo.getSumRest() != null ){
							// prpLlossForCar.setSumRest(prpLlossForCar.getSumRest().add(prpLloss.getSumRest()));//残值
//							}
								
						}
					}
					if(isAdd){
						compensateVo.getPrpLLossItemVoList().add(prpLlossForCar);
						compensateVo.getPrpLCompensateVo().setPrpLLossItems(compensateVo.getPrpLLossItemVoList());
					}
				}
				if(compensateVo.getPrpLLossPropVoList()!=null&& !compensateVo.getPrpLLossPropVoList().isEmpty()){// 存在车辆损失
					PrpLLossPropVo prpLpropLossForProp = new PrpLLossPropVo();
					Datas.copySimpleObjectToTargetFromSource(prpLpropLossForProp, compensateVo.getPrpLLossPropVoList().get(0), false);
					prpLpropLossForProp.setSumLoss(BigDecimal.ZERO);
					prpLpropLossForProp.setSumRest(BigDecimal.ZERO);
					prpLpropLossForProp.setRescueFee(BigDecimal.ZERO);
					prpLpropLossForProp.setBzPaidLoss(BigDecimal.ZERO);
					prpLpropLossForProp.setBzPaidRescueFee(BigDecimal.ZERO);
					prpLpropLossForProp.setKindCode(kindCode);
//					prpLpropLossForProp.setItemKindNo(itemKindNo);
					boolean isAdd = false;
					for(PrpLLossPropVo prpLLossPropVo : compensateVo.getPrpLLossPropVoList()){
						if(CodeConstants.KINDCODE.KINDCODE_X.equals(prpLLossPropVo.getKindCode().trim()) && "1".equals(prpLLossPropVo.getPropType())){
							isAdd = true;
							if(prpLLossPropVo.getSumLoss() != null){
								prpLpropLossForProp.setSumLoss(prpLpropLossForProp.getSumLoss().add(prpLLossPropVo.getSumLoss()));// 小计
							}
							if(prpLLossPropVo.getRescueFee() != null){
								prpLpropLossForProp.setRescueFee(prpLpropLossForProp.getRescueFee().add(prpLLossPropVo.getRescueFee()));// 施救费
							}
							if(prpLLossPropVo.getBzPaidLoss() != null){
								prpLpropLossForProp.setBzPaidLoss(prpLpropLossForProp.getBzPaidLoss().subtract(prpLLossPropVo.getBzPaidLoss()));// 定损扣交强
							}
							if(prpLLossPropVo.getBzPaidRescueFee() != null){
								prpLpropLossForProp.setBzPaidRescueFee(prpLpropLossForProp.getBzPaidRescueFee().subtract(
										prpLLossPropVo.getBzPaidRescueFee()));// 施救费扣交强
							}
							if(prpLLossPropVo.getSumRest() != null){
								prpLpropLossForProp.setSumRest(prpLpropLossForProp.getSumRest().add(prpLLossPropVo.getSumRest()));// 残值
							}
						}
					}
					if(isAdd){
						compensateVo.getPrpLLossPropVoList().add(prpLpropLossForProp);
						compensateVo.getPrpLCompensateVo().setPrpLLossProps(compensateVo.getPrpLLossPropVoList());
					}
				}
			}
			
		
		}
		// 根据损失信息判断是否需要带出无法找到第三方特约险 end
		
		// PNCCAR,D201409-14 2014条款改造,CHENRONG,UPDATE,20140910,END
		
		// 2.获取免赔率,并将免赔率结果组织到Loss中,并放入CompensateVo返回,以供商业试算.
		compensateService.executeRulesDeductRate(compensateVo, prpLClaimVo);
		// PNC-21345 立案修改主责为80%后立案刷新估计赔款仍然按照70%进行的计算 begin
		if(compensateVo !=null && compensateVo.getPrpLCompensateVo() != null) {
			compensateVo.getPrpLCompensateVo().setIndemnityDutyRate(prpLClaimVo.getIndemnityDutyRate());
		}
		// PNC-21345 立案修改主责为80%后立案刷新估计赔款仍然按照70%进行的计算 end
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo()) +"，调用事故免赔率与可选免赔率规则进行免赔率计算结束.");
		// 3.商业组织List<CompensateExp>
		List<CompensateExp> compensateExpList = this.compensateBIRuleData(compensateVo,"1",prpLClaimVo);
		// 4.商业理算前数据已经组织完毕,进行真正的商业试算.
		Boolean cprcCase = compensateService.isCprcCase(prpLClaimVo.getRegistNo());
		compensateVo.setIsMitemKindList(compensateService.getIsMitemKinds(compensateVo.getPrpLCItemKindVoList(),cprcCase));
		List<CompensateExp> biResultList = compensateService.executeRulesCompensate(compensateExpList,cprcCase);
		//计算公式
		compensateService.rebuildBiCompensateVo(compensateVo,biResultList);
		logger.info("报案号:"+ (prpLClaimVo == null ? null : prpLClaimVo.getRegistNo())+ "，LcText="
				+ ((compensateVo== null || compensateVo.getPrpLCompensateVo() == null) ? null : compensateVo.getPrpLCompensateVo().getLcText()));
	
		//保存日志 之后可以删除
		compensateService.saveClaimCalLog(prpLClaimVo.getRegistNo(), prpLClaimVo.getClaimNo(), compensateVo.getPrpLCompensateVo().getLcText());
		
		// 设置不计免赔
		List<MItemKindVo> mItemKindVos = compensateVo.getmExceptKinds();// 规则返回损失对应的不计免赔数据。
		mItemKindVos = this.takeOffNullObject(mItemKindVos);
		biResultList = this.takeOffNullObject(biResultList);
		List<CompensateExp> biResultListTemp = new ArrayList<CompensateExp>(0);// 不计免赔
		BigDecimal sumClaimExceptDeductible = new BigDecimal("0.0");// 不计免赔损失(050900)
		BigDecimal exceptDeductible = new BigDecimal("0.0");// 不计免赔金额(050900)
		if(biResultList != null && biResultList.size()>0){
			for(CompensateExp exp : biResultList){
				if(CodeConstants.KINDCODE.KINDCODE_D12.equals(exp.getKindCode().trim())){// 车上乘客超保额调整
					if(exp.getSumRealPay() > exp.getDamageAmount()){
						exp.setSumRealPay(exp.getDamageAmount()*exp.getDeductibleRate());
						exp.setMsumRealPay(exp.getDamageAmount()*exp.getExceptDeductRate());
					}
				}
				if(mItemKindVos != null && mItemKindVos.size()> 0){
					for(MItemKindVo mItemKindVo:mItemKindVos) {
						CompensateExp compensateExpM=new CompensateExp();
						if(mItemKindVo.getKindCode() != null && !"".equals(mItemKindVo.getKindCode().trim()) 
								&& exp.getKindCode() != null && !"".equals(exp.getKindCode().trim())){
							if(exp.getKindCode().trim().equals(mItemKindVo.getKindCode().trim())){// 匹配时
								compensateExpM.setExpType(exp.getExpType());
								compensateExpM.setMsumFlag(1);
								compensateExpM.setKindCode(exp.getKindCode().trim());
								compensateExpM.setSumLoss(exp.getMsumRealPay());// 损失金额
								sumClaimExceptDeductible = sumClaimExceptDeductible.add(new BigDecimal(exp.getMsumRealPay()));
								compensateExpM.setSumRealPay(exp.getMsumRealPay());// 估计赔款金额
								exceptDeductible = exceptDeductible.add(new BigDecimal(exp.getMsumRealPay()));
								biResultListTemp.add(compensateExpM);
								break;
							}
						}
					}
				}
			}
			CompensateExp compensateExpM = new CompensateExp();
			compensateExpM.setKindCode(CodeConstants.KINDCODE.KINDCODE_M);
			compensateExpM.setSumLoss(sumClaimExceptDeductible.doubleValue());// 损失金额
			compensateExpM.setSumRealPay(exceptDeductible.doubleValue());// 估计赔款金额
			biResultListTemp.add(compensateExpM);
		}
		biResultList.addAll(biResultListTemp);
		compensateListVo.setReturnCompensateList(biResultList);
		compensateListVo.setCompensateType(1);
		return compensateListVo;
	}
	
	@Override
	public CompensateListVo calCulatorCi(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo) {
		return calculatorCI.calCulatorCi(compensateVo,prpLClaimVo);
	}
	
	/**
	 * 商业扣交强部分
	 * @param compensateVo
	 * @param bZCompensateList
	 * @return
	 */
	public CompensateVo rebuildBZCompensateVo(CompensateVo compensateVo,CompensateListVo ciCompensateListVo){
		List<ThirdPartyLossInfo> thirdPartyLossInfolist = new ArrayList<ThirdPartyLossInfo>(0);
		thirdPartyLossInfolist = compensateService.getThirdPartyLossInfolistBz(ciCompensateListVo,"claim");// 进行一分钱调整
		for(ThirdPartyLossInfo thirdPartyLossInfo:thirdPartyLossInfolist) {
			if(CodeConstants.FeeTypeCode.PROPLOSS.equals(thirdPartyLossInfo.getDamageType().trim())){// 车财的
				if(thirdPartyLossInfo.getExpType().trim().equals(CodeConstants.ExpType.CAR)){// 车损
					if(compensateVo.getPrpLLossItemVoList()!=null&& !compensateVo.getPrpLLossItemVoList().isEmpty()){// 车损（互碰自赔车损或一般赔案的三者车损）
						for(PrpLLossItemVo prpLLossItemVo : compensateVo.getPrpLLossItemVoList()) {
							if(prpLLossItemVo.getDlossId().toString().trim().equals(thirdPartyLossInfo.getLossIndex().trim())) {
								if(thirdPartyLossInfo.getRescueFeeFlag()!=null&&thirdPartyLossInfo.getRescueFeeFlag().trim().equals("1")){// 表示施救费损失
									if (Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) || Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
										prpLLossItemVo.setBzPaidRescueFee(new BigDecimal("0.00"));
									} else {
										prpLLossItemVo.setBzPaidRescueFee(new BigDecimal(thirdPartyLossInfo.getSumRealPay()));
									}
								}else{// 定损
									if (Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) || Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
										prpLLossItemVo.setBzPaidLoss(new BigDecimal("0.00"));
									} else {
										prpLLossItemVo.setBzPaidLoss(new BigDecimal(thirdPartyLossInfo.getSumRealPay()));
									}
								}
							}
						}
					} 
				} else {
					if(compensateVo.getPrpLLossPropVoList()!=null&& !compensateVo.getPrpLLossPropVoList().isEmpty()){// 财损（互碰自赔财损或一般赔案的三者车财损）
						for(PrpLLossPropVo prpLLossPropVo : compensateVo.getPrpLLossPropVoList()) {
							if(prpLLossPropVo.getDlossId().toString().trim().equals(thirdPartyLossInfo.getLossIndex().trim()) && "1".equals(prpLLossPropVo.getPropType())) {
								if(thirdPartyLossInfo.getRescueFeeFlag()!=null&&thirdPartyLossInfo.getRescueFeeFlag().trim().equals("1")){// 表示施救费损失
									if (Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) || Double.isNaN(thirdPartyLossInfo.getSumRealPay())){	
										prpLLossPropVo.setBzPaidRescueFee(new BigDecimal("0.00"));
									 } else {
										 prpLLossPropVo.setBzPaidRescueFee(new BigDecimal(thirdPartyLossInfo.getSumRealPay()));
									 }
								}else{// 定损
									if (Double.isInfinite(thirdPartyLossInfo.getSumRealPay()) || Double.isNaN(thirdPartyLossInfo.getSumRealPay())){	
										prpLLossPropVo.setBzPaidLoss(new BigDecimal("0.00"));
									} else {
										prpLLossPropVo.setBzPaidLoss(new BigDecimal(thirdPartyLossInfo.getSumRealPay()));
									}
								}
							}
						}
					} 
				}
				
			}else if(CodeConstants.FeeTypeCode.MEDICAL_EXPENSES.equals(thirdPartyLossInfo.getDamageType().trim())||CodeConstants.FeeTypeCode.PERSONLOSS
					.equals(thirdPartyLossInfo.getDamageType().trim())){// 医疗 人伤的
				if(compensateVo.getPrpLLossPersonVoList() != null && !compensateVo.getPrpLLossPersonVoList().isEmpty()) {
					for(PrpLLossPersonVo prpLLossPersonVo:compensateVo.getPrpLLossPersonVoList()){
						BigDecimal personBzSum = BigDecimal.ZERO;
						if(prpLLossPersonVo.getPersonId().toString().trim().equals(thirdPartyLossInfo.getLossIndex().trim())){
							for(PrpLLossPersonFeeVo personFeeVo:prpLLossPersonVo.getPrpLLossPersonFees()){
								if(personFeeVo.getLossItemNo().equals(thirdPartyLossInfo.getDamageType())){
								if(Double.isInfinite(thirdPartyLossInfo.getSumRealPay())||Double.isNaN(thirdPartyLossInfo.getSumRealPay())){
									personFeeVo.setBzPaidLoss(new BigDecimal("0.00"));
								}else{
									personFeeVo.setBzPaidLoss(new BigDecimal(thirdPartyLossInfo.getSumRealPay()));
								}
								personBzSum = personBzSum.add(personFeeVo.getBzPaidLoss());
								}
							}
						}
						prpLLossPersonVo.setBzPaidLoss(personBzSum);
					}
				}
					
			}
			logger.debug("商业扣交强部分"+thirdPartyLossInfo.getLossName()+"——损失金额："+thirdPartyLossInfo.getSumLoss()+",交强赔付："+thirdPartyLossInfo
					.getSumRealPay());
		}
		return compensateVo;
	}
	
	/**
	 * 立案计算规则前数据组织
	 * @param compensateVo
	 * @param compensateType
	 * @param prpLclaim
	 * @return
	 */
	public List compensateBIRuleData(CompensateVo compensateVo,String compensateKind,PrpLClaimVo prpLClaimVo) {
		String registNo = prpLClaimVo.getRegistNo();
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		PrpLCompensateVo prpLCompensateVo = compensateVo.getPrpLCompensateVo();
		List<CompensateExp> compensateExpAry = new ArrayList<CompensateExp>(0);
		List<PrpLLossItemVo> prpLlosses = compensateVo.getPrpLLossItemVoZFList(); // 车辆损失项信息
		PrpLCompensateVo prpLcompensate = compensateVo.getPrpLCompensateVo();
		List<PrpLLossPropVo> prpLpropLosses = prpLcompensate.getPrpLLossProps(); // 财产损失项信息
		List<PrpLLossPersonVo> prpLpersonItems = prpLcompensate.getPrpLLossPersons(); // 人员损失项信息,刷新立案估计赔款使用（商业）
		prpLlosses = this.takeOffNullObject(prpLlosses);
		prpLpropLosses = this.takeOffNullObject(prpLpropLosses);
		prpLpersonItems = this.takeOffNullObject(prpLpersonItems);

		boolean isZcCompensate = compensateService.isZcCompensate(prpLCompensateVo);
		ThirdPartyDepreRate thirdPartyDepreRate = compensateVo.getThirdPartyDepreRate();

		logger.debug("-------------------------------------");
		logger.debug("条款代码："+thirdPartyDepreRate.getClauseType());
		logger.debug("折旧率："+thirdPartyDepreRate.getDepreRate());
		logger.debug("使用月数："+thirdPartyDepreRate.getUseMonths());
		logger.debug("使用年数："+thirdPartyDepreRate.getUseYears());
		logger.debug("车辆实际价值："+thirdPartyDepreRate.getActualValue());
		logger.debug("设备实际价值："+thirdPartyDepreRate.getDeviceActualValue());
		logger.debug("车型代码："+thirdPartyDepreRate.getCarKindCode());
		logger.debug("新设备购置价："+thirdPartyDepreRate.getDevicePurchasePrice());
		logger.debug("新车购置价："+thirdPartyDepreRate.getPurchasePrice());
		logger.debug("-------------------------------------");

		int index = 0;
		// 1.车辆,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CompensateExp对象
		boolean deductibleKindAFlag = false; // 车损险是否扣减了免赔额
		for(PrpLLossItemVo prpLLossItemVo:prpLlosses){
			CompensateExp compensateExp = new CompensateExp();
			compensateExp.setKindCode(prpLLossItemVo.getKindCode());
			compensateExp.setLicenseNo(prpLLossItemVo.getItemName()); // 车牌号码
			// compensateExp.setLossName(prpLLossItemVo.getLossName()); // 损失项名称
			//compensateExp.setRecoveryOrPayFlag(prpLLossItemVo.getPayFlag()); // 损失项代位类型：1代位,2清付,3自付
			compensateExp.setRecoveryOrPayFlag("3");//不考虑代位的情况
			compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
			compensateExp.setExpType(CodeConstants.ExpType.CAR); // 损失类型：车/财/人/其他

			// 给lossFeeType赋值
			compensateExp.setLossFeeType(prpLLossItemVo.getLossFeeType());
			
			// 安联计算书不分代位类型，与周文确认如果第一张商业计算书（只有第一张商业计算书有代位理算项）置为“1”，其余“0”。
			if(isZcCompensate){
				compensateExp.setPayType("1");
			}else{
				compensateExp.setPayType("0");
			}
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.CLEAR_PAY)){
				// 清付金额=核损费用 + 施救费 - 残值 - 其他扣除(鼎和的核损已经扣除残值了)。
				compensateExp.setRecoveryPay(prpLLossItemVo.getSumLoss().doubleValue()+prpLLossItemVo.getRescueFee().doubleValue());
				// compensateExp.setRecoveryPay(prpLLossItemVo.getSumLoss().doubleValue() +
				// prpLLossItemVo.getRescueFee().doubleValue() -
				// prpLLossItemVo.getRemnantfee().doubleValue() -
				// prpLLossItemVo.getOtherDeductFee().doubleValue());
			}

			// compensateExp.setCetainLossType(prpLDlossCarMainVo.getCetainLossType());
			if("1".equals(prpLLossItemVo.getOutRiskType())){
				compensateExp.setOppoentCoverageType(CodeConstants.PolicyType.POLICY_DZA); // 代位类型
			}else{
				compensateExp.setOppoentCoverageType(CodeConstants.PolicyType.POLICY_DZA); // 代位类型
			}
			// compensateExp.setSumRest(prpLLossItemVo.getRemnantfee().doubleValue()); // 残值
			compensateExp.setOtherDeductFee(DataUtils.NullToZero(prpLLossItemVo.getOtherDeductAmt()).doubleValue());// 其他扣除
			if(prpLLossItemVo.getOffPreAmt() != null){
				compensateExp.setSumPrePay(prpLLossItemVo.getOffPreAmt().doubleValue());
			}else{
				compensateExp.setSumPrePay(0.00);
			}
			compensateExp.setRescueFee(DataUtils.NullToZero(prpLLossItemVo.getRescueFee()).doubleValue()); // 施救费
			if(prpLLossItemVo.getBzPaidLoss() != null){
				compensateExp.setSumLossBZPaid(prpLLossItemVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
			}else{
				compensateExp.setSumLossBZPaid(0.00);
			}
			
			if(prpLLossItemVo.getBzPaidRescueFee() != null){
				compensateExp.setRescueFeeBZPaid(prpLLossItemVo.getBzPaidRescueFee().doubleValue()); // 施救费交强已赔付
			}else{
				compensateExp.setRescueFeeBZPaid(0.00);
			}
			compensateExp.setRiskCode(prpLLossItemVo.getRiskCode());
			compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
			//实际价值减去 已赔付金额
			Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
			BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
			if(kindPaidMap!=null && !kindPaidMap.isEmpty()){
				sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
				if(sumRealPay4Kind == null){//如果A未承保，确认是否承保的是A1
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A1);
				}
			}
			compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
			compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
			compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
			compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
			compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
			compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
			compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
			compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
			compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
//			compensateExp.setIsSuitFlag(compensateService.isSuitCase(prpLCompensateVo));
			if(compensateVo.getDeductibleValue() != null){
				compensateExp.setDeductFee(compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
			}else{
				compensateExp.setDeductFee(0.00);
			}
			
			if(CodeConstants.KINDCODE_A_LIST.contains(prpLLossItemVo.getKindCode())){
				if(compensateVo.isDeductibleFlag()){
					compensateExp.setDeductFee(0d);
				}else{
					deductibleKindAFlag = true;
				}
			}
			
			// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
			compensateExp.setIndemnityDutyRate(prpLLossItemVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
			compensateExp.setDutyDeductibleRate(prpLLossItemVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
			compensateExp.setSelectDeductibleRate(prpLLossItemVo.getDeductAddRate().doubleValue()/100); // 可选免赔率

			compensateExp.setDeductibleRate(prpLLossItemVo.getDeductibleRate().doubleValue() / 100);
			compensateExp.setExceptDeductRate(prpLLossItemVo.getDeductOffRate().doubleValue()/100); // 不计免赔率
			// compensateExp.setThirdCarDutyRate(prpLLossItemVo.getDutyRate().doubleValue()); //如果是代位案件，PrpLloss.indemnityDutyRate就是对方比例。
			compensateExp.setIndex(index++ ); // 对象索引
			compensateExp.setLossItem(prpLLossItemVo);
			// 代位案件需要从车辆中设置对方商业险责任比例。
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				compensateExp.setThirdCarDutyRate(prpLLossItemVo.getDutyRate().doubleValue()/100);
			}else{
				compensateExp.setThirdCarDutyRate(0d);
			}

			// 与周文确认，不计免赔都纳入赔款。
			compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
			compensateExp.setFeeTypeName(codeTranService.transCode("LossTypeCar", prpLLossItemVo.getLossType()));
			// PrpDcodeId prpDcodeId_LossFeeType = new PrpDcodeId();
			// prpDcodeId_LossFeeType.setCodeCode(prpLLossItemVo.getLossFeeType());
			// prpDcodeId_LossFeeType.setCodeType(CodeConstants.CodeType.LossFeeType.value);
			// PrpDcode result_LossFeeType = super.get(PrpDcode.class, prpDcodeId_LossFeeType);
			// compensateExp.setFeeTypeName(result_LossFeeType.getCodeCName());

			compensateExp.setCheckPolicyM("0");
			List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
			if(isMitemKinds != null && !isMitemKinds.isEmpty()){
				for(PrpLCItemKindVo prpLcItemKindVo:isMitemKinds){
					if(prpLcItemKindVo.getKindCode().equals(prpLLossItemVo.getKindCode())){
						compensateExp.setCheckPolicyM("1");
						break;
					}
				}
			}

			if("1".equals(compensateExp.getIsSuitFlag())){
				List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
				for(MItemKindVo mItemKindVo:mExceptKinds){
					if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
						compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
					}
				}
			}

			compensateExp.setAmount(prpLLossItemVo.getItemAmount().doubleValue());
			compensateExp.setDamageAmount(prpLLossItemVo.getItemAmount().doubleValue());
			compensateExp.setUnitAmount(0d);
			compensateExp.setQuantity(0d);

			// 商业车损险保额取保单保额，用于后续计算是否足额投保。
			PrpLCItemKindVo prpLcItemKindVo = compensateService.queryPrpLcItemKind4KindCode(compensateExp.getKindCode(),prpLCompensateVo);
			double amt = prpLcItemKindVo.getAmount().doubleValue();
			if(CodeConstants.KINDCODE_A_LIST.contains(compensateExp.getKindCode())){
				compensateExp.setAmount(amt);
			}

			if(CodeConstants.PayFlagType.INSTEAD_PAY.equals(compensateExp.getRecoveryOrPayFlag())&&CodeConstants.PolicyType.POLICY_DZA
					.equals(compensateExp.getOppoentCoverageType())){
				List<ThirdPartyRecoveryInfo> thirdPartyRecoveryInfoList = compensateVo.getThirdPartyRecoveryInfoList();
				for(ThirdPartyRecoveryInfo recoveryInfo:thirdPartyRecoveryInfoList){
					// 被代位车辆ID。
					Long thirdCarId = Long.valueOf(recoveryInfo.getThirdCarlicenseNo());
					// PrpLthirdParty thirdPartyCar = this.thirdPartyService.findPrpLthirdPartyByPK(thirdCarId);
					PrpLDlossCarMainVo thirdPartyCar = lossCarService.findLossCarMainById(thirdCarId);
					if(thirdPartyCar.getLicenseNo().trim().equals(compensateExp.getLicenseNo().trim())){
						compensateExp.setLockedBzRealPay(recoveryInfo.getRecoverySumRealPay().doubleValue());

						// TODO start。待修改，目前交强规则还没有完成，所以在代付交强险损失项时，可以将这个字段存入总的交强赔付金额。
						compensateExp.setLockedBzRealPay(prpLLossItemVo.getBzPaidLoss().doubleValue()+prpLLossItemVo.getBzPaidRescueFee()
								.doubleValue());
						recoveryInfo.setRecoverySumRealPay(new BigDecimal(prpLLossItemVo.getBzPaidLoss().doubleValue()+prpLLossItemVo
								.getBzPaidRescueFee().doubleValue()));
						// TODO end。待修改，目前交强规则还没有完成，所以在代付交强险损失项时，可以将这个字段存入总的交强赔付金额。

						break;
					}
				}
			}else{
				compensateExp.setLockedBzRealPay(0d);
			}

			/*PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
			if(prpLDlossCarMainVo != null && CodeConstants.CetainLossType.DEFLOSS_ALL.equals(prpLDlossCarMainVo.getCetainLossType())){
				// 此处原始代码已经注销 AJ
				// 主车车损险全损，理算时的损失金额取保额。
				// if(ClaimConst.ClaimSF.SF700.value.equals(compensateExp.getKindCode())){
				// compensateExp.setSumLoss(this.queryPrpLcItemKind4KindCode(ClaimConst.ClaimSF.SF700.value , prpLcompensate).getAmount().doubleValue()); // 承保保额
				// }
				// else{
				// compensateExp.setSumLoss(prpLloss.getLossFee().doubleValue()); // 核定损失
				// }
				// 逻辑修改为：推定全损，核损金额小于保额时取核损金额；核损金额大于等于保额时取保额。
				if(CodeConstants.KINDCODE.KINDCODE_A.equals(compensateExp.getKindCode())||CodeConstants.KINDCODE.KINDCODE_G.equals(compensateExp
						.getKindCode())||CodeConstants.KINDCODE.KINDCODE_Z.equals(compensateExp.getKindCode())){
					BigDecimal amount = prpLLossItemVo.getItemAmount();
					if(prpLLossItemVo.getSumLoss().compareTo(amount)<=0){
						compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue());
					}else{
						compensateExp.setSumLoss(amount.doubleValue());
					}
				}
			}else{
				compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue()); // 核定损失
			}*/
			compensateExp.setSumLoss(prpLLossItemVo.getSumLoss().doubleValue()); // 核定损失
			compensateExp.setSumRealPay(compensateExp.getSumLoss() - compensateExp.getSumPrePay());
			compensateExp.setRegistNo(registNo);
			compensateExpAry.add(compensateExp);
		}

		// 2.财产,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CopensateExp对象
		for(PrpLLossPropVo prpLLossPropVo:prpLpropLosses){
			if("1".equals(prpLLossPropVo.getPropType())){
				CompensateExp compensateExp = new CompensateExp();
				compensateExp.setKindCode(prpLLossPropVo.getKindCode().trim());
				// compensateExp.setFeeTypeCode(prpLLossPropVo.getFeeTypeCode()); // 损失类型代码
				int serialNo=0;
				// 根据 CalculatorServiceSpringImpl.getPrpLpropLosses set的 ItemId 判断 地面 标的 和三者
				System.out.println("==prpLLossPropVo.getItemId()=="+prpLLossPropVo.getItemId());
				if(prpLLossPropVo.getItemId().equals("0")){
					serialNo = 0;// 地面
				}else if(prpLLossPropVo.getItemId().equals("1")){
					serialNo = 1;// 标的车
				}else{
					// 先到定核损取财产表值
					PrpLdlossPropMainVo dlossPropVoTemp = propTaskService.findPropMainVoById(prpLLossPropVo.getDlossId());
					if(dlossPropVoTemp!=null){
						serialNo = dlossPropVoTemp.getSerialNo();
					}else{
						// 如果为空，到查勘的财产表取值
						PrpLCheckPropVo checkPropVo = checkTaskService.findByCheckPropVoById(prpLLossPropVo.getDlossId());
						if(checkPropVo!=null){
							serialNo = checkPropVo.getLossPartyId().intValue();
						}
					}
				}
				List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
				if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
					for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
						if(prpLDlossCarMainVo.getSerialNo()==serialNo){
							compensateExp.setLicenseNo(prpLDlossCarMainVo.getLicenseNo()); // 车牌号码
							break;
						}
					}
				}
				compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
				compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
				compensateExp.setExpType(CodeConstants.ExpType.PROP); // 损失类型：车/财/人/其他

				// 财产不代位，所以都是自付损失项。
				compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
				// 财产不代位，所以责任对方追偿金额都是0。
				compensateExp.setRecoveryPay(0d);

				// compensateExp.setSumRest(prpLLossPropVo.getRemnantfee().doubleValue()); // 残值
				compensateExp.setRiskCode(prpLLossPropVo.getRiskCode());
				compensateExp.setOtherDeductFee(DataUtils.NullToZero(prpLLossPropVo.getOtherDeductAmt()).doubleValue());// 其他扣除
				compensateExp.setSumLoss(prpLLossPropVo.getSumLoss().doubleValue()); // 核定损失
				if(prpLLossPropVo.getOffPreAmt() != null){
					compensateExp.setSumPrePay(prpLLossPropVo.getOffPreAmt().doubleValue());
				}else{
					compensateExp.setSumPrePay(0.00);
				}
				compensateExp.setRescueFee(prpLLossPropVo.getRescueFee().doubleValue()); // 施救费
				compensateExp.setSumLossBZPaid(prpLLossPropVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
				compensateExp.setRescueFeeBZPaid(DataUtils.NullToZero(prpLLossPropVo.getBzPaidRescueFee()).doubleValue()); // 施救费交强已赔付
				compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
				//实际价值减去 已赔付金额
				Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
				BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
				if(kindPaidMap!=null && !kindPaidMap.isEmpty()){
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
					if(sumRealPay4Kind == null){
						sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A1);
					}
				}
				compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
				
				
				compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
				compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
				compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
				compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
				compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
				compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
				compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
				compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
//				compensateExp.setIsSuitFlag(compensateService.isSuitCase(prpLCompensateVo));
				if(compensateVo.getDeductibleValue() != null){
					compensateExp.setDeductFee(compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
				}else{
					compensateExp.setDeductFee(0.00);
				}
				// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
				compensateExp.setIndemnityDutyRate(prpLLossPropVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
				compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
				compensateExp.setSelectDeductibleRate(prpLLossPropVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
				compensateExp.setDeductibleRate(prpLLossPropVo.getDeductibleRate().doubleValue() / 100);
				compensateExp.setExceptDeductRate(prpLLossPropVo.getDeductOffRate().doubleValue()/100); // 不计免赔率
				compensateExp.setIndex(index++ ); // 对象索引
				// 与周文确认，不计免赔都纳入赔款。
				compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
				compensateExp.setLossItem(prpLLossPropVo);
				// compensateExp.setLossName(this.makePropLossName(prpLLossPropVo.getPrpLpropMainId(), prpLLossPropVo.getKindCode()));
				compensateExp.setFeeTypeName(codeTranService.transCode("LossTypeProp", prpLLossPropVo.getLossType()));
				// PrpDcodeId prpDcodeId_LossFeeType = new PrpDcodeId();
				// prpDcodeId_LossFeeType.setCodeCode(prpLLossPropVo.getLossFeeType());
				// prpDcodeId_LossFeeType.setCodeType(CodeConstants.CodeType.LossFeeType.value);
				// PrpDcode result_LossFeeType = super.get(PrpDcode.class, prpDcodeId_LossFeeType);
				// compensateExp.setFeeTypeName(result_LossFeeType.getCodeCName());

				compensateExp.setCheckPolicyM("0");
				List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
				if(isMitemKinds != null && !isMitemKinds.isEmpty()){
					for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
						if(prpLcItemKind.getKindCode().equals(prpLLossPropVo.getKindCode())){
							compensateExp.setCheckPolicyM("1");
							break;
						}
					}
				}

				if("1".equals(compensateExp.getIsSuitFlag())){
					List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
					if(mExceptKinds != null && !mExceptKinds.isEmpty()){
						for(MItemKindVo mItemKindVo:mExceptKinds){
							if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
								compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
							}
						}
					}
				}

				compensateExp.setAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setDamageAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setUnitAmount(0d);
				compensateExp.setQuantity(0d);
				compensateExp.setSumRealPay(compensateExp.getSumLoss() - compensateExp.getSumPrePay());
				compensateExp.setRegistNo(registNo);
				compensateExpAry.add(compensateExp);
			}
		}

		// 3.人伤,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CompensateExp对象
		int prpLpersonItemIndex = -1;
		for(PrpLLossPersonVo prpLLossPersonVo:prpLpersonItems){
			// 判断费用中是否有确定的险别。
			List<PrpLLossPersonFeeVo> personFeeList = prpLLossPersonVo.getPrpLLossPersonFees();
	        for(PrpLLossPersonFeeVo personFeeVo:personFeeList){
	        	if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLLossPersonVo.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_D12.equals(prpLLossPersonVo.getKindCode())){
	                prpLpersonItemIndex++;
	                break;
	            }
	        }

			/*if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLLossPersonVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12
					.equals(prpLLossPersonVo.getKindCode())){
				prpLpersonItemIndex++ ;
			}*/

			int prpLpersonLossIndex = 0;
			double sumLoss = 0d;
			double otherDeductFee = 0d;
			double sumLossBZPaid = 0d;
			for(PrpLLossPersonFeeVo prpLLossPersonFeeVo:prpLLossPersonVo.getPrpLLossPersonFees()){
				sumLoss = sumLoss + prpLLossPersonFeeVo.getFeeLoss().doubleValue();
				otherDeductFee = otherDeductFee + DataUtils.NullToZero(prpLLossPersonFeeVo.getFeeOffLoss()).doubleValue();
				sumLossBZPaid = sumLossBZPaid + prpLLossPersonFeeVo.getBzPaidLoss().doubleValue();
			}
				CompensateExp compensateExp = new CompensateExp();

				if(CodeConstants.KINDCODE.KINDCODE_D11.equals(prpLLossPersonVo.getKindCode())||CodeConstants.KINDCODE.KINDCODE_D12
						.equals(prpLLossPersonVo.getKindCode())){
					compensateExp.setIntLossIndex(prpLpersonItemIndex);
					compensateExp.setLossNumPerPerson(prpLpersonLossIndex++ );
				}
				
				BigDecimal loss = BigDecimal.ZERO;
				if(prpLLossPersonVo.getSumOffLoss() != null){
					loss = prpLLossPersonVo.getSumLoss().subtract(prpLLossPersonVo.getSumOffLoss());
				}else{
					loss = prpLLossPersonVo.getSumLoss().subtract(BigDecimal.ZERO);
				}

				if(CodeConstants.KINDCODE.KINDCODE_B.equals(prpLLossPersonVo.getKindCode().trim())){
					compensateExp.setRescueFee(loss.doubleValue());
					compensateExp.setSumLoss(0d);
				}

//				compensateExp.setbZPaid(compensateExp.getSumLossBZPaid());
//				compensateExp.setIsSuitFlag(compensateService.isSuitCase(prpLCompensateVo));
				compensateExp.setKindCode(prpLLossPersonVo.getKindCode().trim());
				// compensateExp.setFeeTypeCode(prpLLossPersonVo.getLossFeeType()); // 损失类型代码
//				prpLLossPersonFeeVo.setPersonId(prpLLossPersonVo.getPersonId());
				compensateExp.setLossItem(prpLLossPersonVo);
				compensateExp.setFeeTypeName(codeTranService.transCode("LossType", prpLLossPersonVo.getLossType()));
				PrpLDlossPersTraceVo prpLDlossPersTraceVo = persTraceDubboService.findPersTraceByPK(prpLLossPersonVo.getPersonId());
				if(prpLDlossPersTraceVo!=null){
					compensateExp.setLicenseNo(prpLDlossPersTraceVo.getPrpLDlossPersInjured().getLicenseNo());
					compensateExp.setLossName(prpLDlossPersTraceVo.getPrpLDlossPersInjured().getPersonName());
				}

				if("1".equals(compensateExp.getIsSuitFlag())){
					List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
					if(mExceptKinds != null && !mExceptKinds.isEmpty()){
						for(MItemKindVo mItemKindVo:mExceptKinds){
							if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
								compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
							}
						}
					}
				}

				compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
				compensateExp.setLossQuantity(1); // 损失项数量，如果非手工增加的其他损失，则默认为1
				compensateExp.setExpType(CodeConstants.ExpType.PERSON); // 损失类型：车/财/人/其他
				compensateExp.setPersonChargeName("人伤费用");
				// 人伤不代位，所以都是自付损失项。
				compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
				// 人伤不代位，所以责任对方追偿金额都是0。
				compensateExp.setRecoveryPay(0d);
				compensateExp.setRiskCode(prpLLossPersonVo.getRiskCode());
				// compensateExp.setSumRest(prpLpersonLoss.getRemnantfee().doubleValue()); // 残值
				compensateExp.setSumLoss(sumLoss); // 核定损失
				compensateExp.setOtherDeductFee(otherDeductFee);// 其他扣除
				if(prpLLossPersonVo.getOffPreAmt() != null){
					compensateExp.setSumPrePay(prpLLossPersonVo.getOffPreAmt().doubleValue());
				}else{
					compensateExp.setSumPrePay(0.00);
				}
				compensateExp.setSumLossBZPaid(sumLossBZPaid); // 定损交强已赔付
				compensateExp.setRescueFee(0d); // 施救费
				compensateExp.setRescueFeeBZPaid(0d); // 施救费交强已赔付
				compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
				//实际价值减去 已赔付金额
				Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
				BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
				if(kindPaidMap!=null && !kindPaidMap.isEmpty()){
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
					if(sumRealPay4Kind == null){
						sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A1);
					}
				}
				compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
				compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
				compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
				compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
				compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
				compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
				compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
				compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
				compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
//				compensateExp.setIsSuitFlag(compensateService.isSuitCase(prpLCompensateVo));
				if(compensateVo.getDeductibleValue() != null){
					compensateExp.setDeductFee(compensateVo.getDeductibleValue().doubleValue()); // 商业车损险免赔额
				}else{
					compensateExp.setDeductFee(0.00); // 商业车损险免赔额
				}
				// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
				PrpLLossPersonFeeVo personFeeVo = prpLLossPersonVo.getPrpLLossPersonFees().get(0);
				compensateExp.setIndemnityDutyRate(personFeeVo.getDutyRate().doubleValue()/100); // 险别事故责任比例
				compensateExp.setDutyDeductibleRate(personFeeVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
				compensateExp.setSelectDeductibleRate(personFeeVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
				compensateExp.setDeductibleRate(personFeeVo.getDeductibleRate().doubleValue() / 100);
				compensateExp.setExceptDeductRate(personFeeVo.getDeductOffRate().doubleValue()/100); // 不计免赔率
				compensateExp.setIndex(index++ ); // 对象索引
				// 与周文确认，不计免赔都纳入赔款。
				compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否

				compensateExp.setCheckPolicyM("0");
				List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
				if(isMitemKinds != null && !isMitemKinds.isEmpty()){
					for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
						if(prpLcItemKind.getKindCode().equals(prpLLossPersonVo.getKindCode())){
							compensateExp.setCheckPolicyM("1");
							break;
						}
					}
				}

				compensateExp.setAmount(personFeeVo.getItemAmount().doubleValue());
				compensateExp.setDamageAmount(personFeeVo.getItemAmount().doubleValue());
				compensateExp.setUnitAmount(personFeeVo.getItemAmount().doubleValue());
				if(personFeeVo.getQuantity() ==null){
					compensateExp.setQuantity(0d);
				}else{
					compensateExp.setQuantity(personFeeVo.getQuantity().doubleValue());
				}
				
				compensateExp.setSumRealPay(compensateExp.getSumLoss() - compensateExp.getSumPrePay());
				compensateExp.setRegistNo(registNo);
				compensateExpAry.add(compensateExp);

//			}
		}

		// 4.其他,List<CompensateExp>对象组织前的准备：获取到所有损失项的KindCode，一个险别对应一个CopensateExp对象
		for(PrpLLossPropVo prpLLossPropVo:prpLpropLosses){
			if("9".equals(prpLLossPropVo.getPropType())){
				CompensateExp compensateExp = new CompensateExp();
				compensateExp.setKindCode(prpLLossPropVo.getKindCode().trim());
				// compensateExp.setFeeTypeCode(prpLLossPropVo.getFeeTypeCode()); // 损失类型代码
				int serialNo = 0;
				PrpLdlossPropMainVo propMain = propTaskService.findPropMainVoById(prpLLossPropVo.getDlossId());
				if(propMain!=null){
					serialNo = propMain.getSerialNo();
				}else{
					// 其他损失为附加险时取车辆定损主表
					PrpLDlossCarMainVo carMain = lossCarService.findLossCarMainById(prpLLossPropVo.getDlossId());
					serialNo = carMain.getSerialNo();
				}
				List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(prpLCompensateVo.getRegistNo());
				if(prpLDlossCarMainVoList!=null&& !prpLDlossCarMainVoList.isEmpty()){
					for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
						if(prpLDlossCarMainVo.getSerialNo()==serialNo){
							compensateExp.setLicenseNo(prpLDlossCarMainVo.getLicenseNo()); // 车牌号码
							break;
						}
					}
				}
				compensateExp.setRecoveryOrPayFlag(CodeConstants.PayFlagType.COMPENSATE_PAY); // 损失项代位类型：1代位,2清付,3自付
				compensateExp.setLossQuantity(prpLLossPropVo.getLossQuantity().intValue());
				compensateExp.setExpType(CodeConstants.ExpType.OTHER); // 损失类型：车/财/人/其他

				// 财产不代位，所以都是自付损失项。
				compensateExp.setPayType(CodeConstants.PayFlagType.COMPENSATE_PAY);
				// 财产不代位，所以责任对方追偿金额都是0。
				compensateExp.setRecoveryPay(0d);

				// compensateExp.setSumRest(prpLLossPropVo.getRemnantfee().doubleValue()); // 残值
				compensateExp.setRiskCode(prpLLossPropVo.getRiskCode());
				compensateExp.setOtherDeductFee(DataUtils.NullToZero(prpLLossPropVo.getOtherDeductAmt()).doubleValue());// 其他扣除
				compensateExp.setSumLoss(prpLLossPropVo.getSumLoss().doubleValue()); // 核定损失
				// compensateExp.setSumPrePay(DataUtils.NullToZero(prpLLossPropVo.getOffPreAmt()).doubleValue());
				// compensateExp.setRescueFee(prpLLossPropVo.getRescueFee().doubleValue()); // 施救费
				// compensateExp.setSumLossBZPaid(prpLLossPropVo.getBzPaidLoss().doubleValue()); // 定损交强已赔付
				// compensateExp.setRescueFeeBZPaid(prpLLossPropVo.getBzPaidRescueFee().doubleValue()); // 施救费交强已赔付
				// compensateExp.setbZPaid(compensateExp.getSumLossBZPaid()+compensateExp.getRescueFeeBZPaid()); // 总交强已赔付
				//实际价值减去 已赔付金额
				Map<String,BigDecimal> kindPaidMap = compensateVo.getKindPaidMap();
				BigDecimal sumRealPay4Kind = BigDecimal.ZERO;
				if(kindPaidMap!=null && !kindPaidMap.isEmpty()){
					sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A);
					if(sumRealPay4Kind == null){
						sumRealPay4Kind = kindPaidMap.get(CodeConstants.KINDCODE.KINDCODE_A1);
					}
				}
				compensateExp.setDamageItemRealCost(thirdPartyDepreRate.getActualValue()-DataUtils.NullToZero(sumRealPay4Kind).doubleValue()); // 车辆实际价值
				compensateExp.setEntryItemCarCost(thirdPartyDepreRate.getPurchasePrice()); // 保险车辆新车购置价
				// compensateExp.setDeviceActualValue(thirdPartyDepreRate.getDeviceActualValue()); // 设备实际价值
				// compensateExp.setDevicePurchasePrice(thirdPartyDepreRate.getDevicePurchasePrice()); // 新设备购置价
				Boolean cprcCase = compensateService.isCprcCase(prpLClaimVo.getRegistNo());
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(compensateExp.getKindCode())&& !cprcCase){
					Map<Long,String[]> deviceMap = thirdPartyDepreRate.getDeviceMap();
					// 新增设备MAP key deviceId,value 0 deviceName,1 新设备购置价 2 出险时新设备实际价值
					// TODO 此处的deviceMap取值是从cMain表的子表prplcCarDevice表获取的ID作为Map的Key,
					// TODO定损处应该在附加险选为新增设备的时候给子表某个字段赋值为prplcCarDevice表的ID，现在尚未处理，故取值错误
					String[] array = deviceMap.get(Long.parseLong(prpLLossPropVo.getItemName()));
					compensateExp.setDeviceActualValue(Double.parseDouble(array[2])); // 设备实际价值
					compensateExp.setDevicePurchasePrice(Double.parseDouble(array[1])); // 新设备购置价
				}
				compensateExp.setUseMonths(thirdPartyDepreRate.getUseMonths()); // 使用月数
				compensateExp.setDepreRate(thirdPartyDepreRate.getDepreRate()); // 折旧率
				compensateExp.setDamageDate(prpLRegistVo.getDamageTime()); // 出险日期
				compensateExp.setOperateDate(compensateVo.getOperateDate()); // 承保日期,prpLcMain.operateDate
				compensateExp.setCompensateType(Integer.parseInt(CodeConstants.CompensateKind.BI_COMPENSATE)); // 计算书类型,商业
//				compensateExp.setIsSuitFlag(compensateService.isSuitCase(prpLCompensateVo));
				compensateExp.setDeductFee(DataUtils.NullToZero(compensateVo.getDeductibleValue()).doubleValue()); // 商业车损险免赔额
				// compensateExp.setClaimType(prpLCompensateVo.getClaimType()); // 赔案类别
				compensateExp.setIndemnityDutyRate(prpLCompensateVo.getIndemnityDutyRate().doubleValue()/100); // 险别事故责任比例
				compensateExp.setDutyDeductibleRate(prpLLossPropVo.getDeductDutyRate().doubleValue()/100); // 事故责任免赔率
				compensateExp.setSelectDeductibleRate(prpLLossPropVo.getDeductAddRate().doubleValue()/100); // 可选免赔率
				 compensateExp.setDeductibleRate(prpLLossPropVo.getDeductibleRate().doubleValue() / 100);
				compensateExp.setExceptDeductRate(prpLLossPropVo.getDeductOffRate().doubleValue()/100); // 不计免赔率
				compensateExp.setIndex(index++ ); // 对象索引
				// 与周文确认，不计免赔都纳入赔款。
				compensateExp.setFlagOfM("1"); // 是否将不计免赔率纳入赔款 1,是;0,否
				compensateExp.setLossItem(prpLLossPropVo);
				compensateExp.setFeeTypeName("附加险");

				// PrpDcodeId prpDcodeId_LossFeeType = new PrpDcodeId();
				// prpDcodeId_LossFeeType.setCodeCode(prplotherloss.getLossFeeType());
				// prpDcodeId_LossFeeType.setCodeType(CodeConstants.CodeType.LossFeeType.value);
				// PrpDcode result_LossFeeType = super.get(PrpDcode.class, prpDcodeId_LossFeeType);
				// compensateExp.setFeeTypeName(result_LossFeeType.getCodeCName());

				compensateExp.setCheckPolicyM("0");
				List<PrpLCItemKindVo> isMitemKinds = compensateVo.getIsMitemKindList();
				if(isMitemKinds != null && !isMitemKinds.isEmpty()){
					for(PrpLCItemKindVo prpLcItemKind:isMitemKinds){
						if(prpLcItemKind.getKindCode().equals(prpLLossPropVo.getKindCode())){
							compensateExp.setCheckPolicyM("1");
							break;
						}
					}
				}

				List<PrpLCItemKindVo> itemKindList = compensateVo.getPrpLCItemKindVoList();
				compensateExp.setDamageAmount(prpLLossPropVo.getItemAmount().doubleValue());
				compensateExp.setQuantity(DataUtils.NullToZero(prpLLossPropVo.getMaxQuantity()).doubleValue());

				// 无法找到第三方 特殊处理
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(prpLLossPropVo.getKindCode())){
					//有车损险 或者 已扣减过免赔额
					if(deductibleKindAFlag || compensateVo.isDeductibleFlag()){
						compensateExp.setDeductFee(0d);
					}
					
					compensateExp.setNoFindThirdFlag("0");
					compensateExp.setKindCodeNTFlag("0");;
					List<PrpLClaimDeductVo> claimDeductList = compensateVo.getClaimDeductList();
					if(claimDeductList!=null&& !claimDeductList.isEmpty()){
						for(PrpLClaimDeductVo claimDeductVo:claimDeductList){
							if(( "120".equals(claimDeductVo.getDeductCondCode())||"320".equals(claimDeductVo.getDeductCondCode()) )){
								compensateExp.setNoFindThirdFlag("1");
								break;
							}
						}
					}
					if(cprcCase){
						for(PrpLCItemKindVo itemKindVo:compensateVo.getPrpLCItemKindVoList()){
							if(CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode())){
								compensateExp.setKindCodeNTFlag("1");
								break;
							}
						}
					}
				}

				if("1".equals(compensateExp.getIsSuitFlag())){
					List<MItemKindVo> mExceptKinds = compensateVo.getmExceptKinds();
					if(mExceptKinds != null && !mExceptKinds.isEmpty()){
						for(MItemKindVo mItemKindVo:mExceptKinds){
							if(compensateExp.getKindCode().equals(mItemKindVo.getKindCode())){
								compensateExp.setSumRealPayJud(mItemKindVo.getExceptKindDeductible());
							}
						}
					}
				}
				// TODO 精神附加险
				if(CodeConstants.KINDCODE.KINDCODE_R.equals(compensateExp.getKindCode())
					|| CodeConstants.KINDCODE.KINDCODE_SS.equals(compensateExp.getKindCode())){
					
					compensateExp.setUnitAmount(prpLLossPropVo.getItemAmount().doubleValue());
				}
				if(CodeConstants.KINDCODE.KINDCODE_RF.equals(compensateExp.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_C.equals(compensateExp.getKindCode())
						||CodeConstants.KINDCODE.KINDCODE_T.equals(compensateExp.getKindCode())){
					for(PrpLCItemKindVo itemKindVo:compensateVo.getPrpLCItemKindVoList()){
						if(itemKindVo.getKindCode().equals(compensateExp.getKindCode())){
							compensateExp.setUnitAmount(itemKindVo.getUnitAmount().doubleValue());
							compensateExp.setQuantity(itemKindVo.getQuantity().doubleValue());
							break;
						}
					}
				}

				compensateExp.setAmount(prpLLossPropVo.getItemAmount().doubleValue());
				// compensateExp.setQuantity(0d);
				compensateExp.setSumRealPay(compensateExp.getSumLoss() - compensateExp.getSumPrePay());
				compensateExp.setRegistNo(registNo);
				compensateExpAry.add(compensateExp);
			}
		}
		
		return compensateExpAry;
	}
	
	/**
	 * 去掉List中为空的对象
	 */
	@SuppressWarnings("unchecked")
	private List takeOffNullObject(List list) {
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
}
