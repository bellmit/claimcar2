package ins.sino.claimcar.claim.calculator.kindCalculator;

import java.math.BigDecimal;
import java.util.List;


import ins.framework.lang.Springs;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;

/**
 * 第三者责任险计算器
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public class KindCalculatorB extends AbstractKindCalculator {
	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");
	
	public KindCalculatorB(CompensateVo compensateVo,Object lossItem,CompensateService compensateService){
		super(compensateVo,lossItem,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//		// 商业险责任比例代码。
//		final String indemnityDuty = this.prpLcompensate.getIndemnityDuty();
//		BigDecimal dutyDeductibleRate = null;
//		// 全责。
//		if(indemnityDuty.equals(CodeConstants.IndemnityDuty.FULL_DUTY)){
//			dutyDeductibleRate = new BigDecimal(20d);
//		}
//		// 主责。
//		if(indemnityDuty.equals(CodeConstants.IndemnityDuty.MAIN_DUTY)){
//			dutyDeductibleRate = new BigDecimal(15d);
//		}
//		// 同责。
//		if(indemnityDuty.equals(CodeConstants.IndemnityDuty.EQUAL_DUTY)){
//			dutyDeductibleRate = new BigDecimal(10d);
//		}
//		// 次责。
//		if(indemnityDuty.equals(CodeConstants.IndemnityDuty.SECOND_DUTY)){
//			dutyDeductibleRate = new BigDecimal(5d);
//		}
//		// 无责。
//		if(indemnityDuty.equals(CodeConstants.IndemnityDuty.NO_DUTY)){
//			dutyDeductibleRate = new BigDecimal(0d);
//		}
//
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal value) {
		BigDecimal selectDeductibleRate = new BigDecimal(0d);
		selectDeductibleRate = sommeDeductibleRate(selectDeductibleRate);
		return super.calSelectDeductibleRate(selectDeductibleRate);
	}

	/**
	 * 返回计算后的绝对免赔率
	 * @return BigDecimal
	 */

	public BigDecimal sommeDeductibleRate(BigDecimal selectDeductibleRate) {
		List<PrpLClaimDeductVo> prpLClaimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(this.compensateVo.getPrpLCompensateVo().getRegistNo());
		if(prpLClaimDeductVoList != null && !prpLClaimDeductVoList.isEmpty()){
			for(PrpLClaimDeductVo prpLClaimDeductVo:prpLClaimDeductVoList){
				// 选上就叠加 Yes为选上
				if("1".equals(prpLClaimDeductVo.getIsCheck())){
					// 选上就叠加 Yes为选上
					if("1".equals(prpLClaimDeductVo.getIsCheck()) && 
							CodeConstants.KINDCODE.KINDCODE_B.equals(prpLClaimDeductVo.getKindCode().trim())){
						selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
					}
				}
			}
		}
		return selectDeductibleRate;
	}

	@Override
	public BigDecimal calAmount(BigDecimal value) {
		value = super.queryOnceSubAmount();
		return super.calAmount(value);
	}

	public BigDecimal calIndemnityDutyRate(BigDecimal value) {
		if(value!=null){
			return super.calIndemnityDutyRate(value);
		}
		return super.calIndemnityDutyRate(super.queryMainCarIndemnityDutyRate());
	}
}
