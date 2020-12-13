package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.math.BigDecimal;
import java.util.List;


/**
 * 车损险计算器
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public class KindCalculatorA extends AbstractKindCalculator {
	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");
	private LossCarService lossCarService =	(LossCarService)Springs.getBean("lossCarService");
	private SubrogationService subrogationService = (SubrogationService)Springs.getBean("subrogationService");
	private CheckTaskService checkTaskService = (CheckTaskService)Springs.getBean("checkTaskService");
	
	public KindCalculatorA(CompensateVo compensateVo,Object lossItem,CompensateService compensateService){
		super(compensateVo,lossItem,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//		// 车损险责任比例代码。
//		String indemnityDuty = this.prpLcompensate.getIndemnityDuty();
//		BigDecimal dutyDeductibleRate = null;
//		if(cprc){
//			// 全责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.FULL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(20d);
//			}
//			// 主责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.MAIN_DUTY)){
//				dutyDeductibleRate = new BigDecimal(15d);
//			}
//			// 同责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.EQUAL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(10d);
//			}
//			// 次责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.SECOND_DUTY)){
//				dutyDeductibleRate = new BigDecimal(5d);
//			}
//			// 无责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.NO_DUTY)){
//				dutyDeductibleRate = new BigDecimal(0d);
//			}
//		}else{
//			// 全责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.FULL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(15d);
//			}
//			// 主责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.MAIN_DUTY)){
//				dutyDeductibleRate = new BigDecimal(10d);
//			}
//			// 同责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.EQUAL_DUTY)){
//				dutyDeductibleRate = new BigDecimal(8d);
//			}
//			// 次责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.SECOND_DUTY)){
//				dutyDeductibleRate = new BigDecimal(5d);
//			}
//			// 无责。
//			if(indemnityDuty.equals(CodeConstants.IndemnityDuty.NO_DUTY)){
//				dutyDeductibleRate = new BigDecimal(0d);
//			}
//		}
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal value) {
		BigDecimal selectDeductibleRate = new BigDecimal(0d);
		selectDeductibleRate = this.sommeDeductibleRate(selectDeductibleRate);
		return super.calSelectDeductibleRate(selectDeductibleRate);
	}

	/**
	 * 返回计算后的绝对免赔率
	 * @return BigDecimal
	 */

	public BigDecimal sommeDeductibleRate(BigDecimal selectDeductibleRate) {
		List<PrpLClaimDeductVo> prpLClaimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(this.compensateVo.getPrpLCompensateVo().getRegistNo());
		if(prpLClaimDeductVoList != null && !prpLClaimDeductVoList.isEmpty()) {
			for(PrpLClaimDeductVo prpLClaimDeductVo:prpLClaimDeductVoList){
				// 选上就叠加 Yes为选上
				if("1".equals(prpLClaimDeductVo.getIsCheck()) && CodeConstants.KINDCODE.KINDCODE_A.equals(prpLClaimDeductVo.getKindCode().trim())){
					selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
				}
			}
		}
		return selectDeductibleRate;
	}

	/**
	 * 查询商业险责任比例。 主车-自付 PrpLcompensate.indemnityDutyRate。 主车-代位 取三者车商业险PrpLthirdPartyInsured.indemnityDutyRate。 三者车 取三者车商业险PrpLthirdPartyInsured.indemnityDutyRate。
	 * @return
	 */
	public BigDecimal calIndemnityDutyRate(BigDecimal value) {
		if(value!=null){
			return super.calIndemnityDutyRate(value);
		}

		BigDecimal indemnityDutyRate = null;
		PrpLLossItemVo prpLLossItemVo = (PrpLLossItemVo)lossItem;
		PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(prpLLossItemVo.getDlossId());
		PrpLCheckCarVo prpLCheckCarVo = checkTaskService.findByCheckId(prpLLossItemVo.getDlossId());
		Integer serialNo = null;
		if(prpLDlossCarMainVo != null){
			serialNo = prpLDlossCarMainVo.getSerialNo();
		}else{
			serialNo = prpLCheckCarVo.getSerialNo();
		}
		// 主车自付。
		if(serialNo == 1 && CodeConstants.PayFlagType.COMPENSATE_PAY.equals(prpLLossItemVo.getPayFlag())){
			indemnityDutyRate = super.queryMainCarIndemnityDutyRate();
		}else{
			// 主车追偿。
			if(prpLLossItemVo.getPayFlag().equals(CodeConstants.PayFlagType.INSTEAD_PAY)){
				// 被代位车辆。
				PrpLPlatLockVo prpLPlatLockVo = subrogationService.findPrpLPlatLockVoById(prpLLossItemVo.getSubrogationId());
				List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(this.prpLcompensate.getRegistNo());
				for(PrpLCheckDutyVo prpLCheckDutyVo:prpLCheckDutyVoList){
					if(prpLPlatLockVo.getOppoentLicensePlateNo().trim().equals(prpLCheckDutyVo.getLicenseNo())){
						indemnityDutyRate = prpLCheckDutyVo.getIndemnityDutyRate();
						break;
					}
				}
			}else{
				PrpLCheckDutyVo prpLCheckDutyVo = checkTaskService.findCheckDuty(this.prpLcompensate.getRegistNo(),serialNo);
				indemnityDutyRate = prpLCheckDutyVo.getIndemnityDutyRate();
			}
		}
		return super.calIndemnityDutyRate(indemnityDutyRate);
	}

	@Override
	public BigDecimal calAmount(BigDecimal value) {
		value = super.queryOnceSubAmount();
		return super.calAmount(value);
	}
}
