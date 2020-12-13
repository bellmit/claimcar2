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
 * 全车盗抢险计算器
 * 
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public class KindCalculatorG extends AbstractKindCalculator {

	private RegistQueryService registQueryService = (RegistQueryService)Springs.getBean("registQueryService");

	public KindCalculatorG(CompensateVo compensateVo,Object object,CompensateService compensateService){
		super(compensateVo,object,compensateService);
	}

//	@Override
//	public BigDecimal calDutyDeductibleRate(BigDecimal value) {
//		BigDecimal dutyDeductibleRate = new BigDecimal(20d);
//		return super.calDutyDeductibleRate(dutyDeductibleRate);
//	}

	@Override
	public BigDecimal calSelectDeductibleRate(BigDecimal value) {
		BigDecimal selectDeductibleRate = sommeDeductibleRate(new BigDecimal(0d));
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
				if("1".equals(prpLClaimDeductVo.getIsCheck()) && CodeConstants.KINDCODE.KINDCODE_G.equals(prpLClaimDeductVo.getKindCode().trim())){
					selectDeductibleRate = selectDeductibleRate.add(prpLClaimDeductVo.getDeductRate());
				}
			}
		}
		return selectDeductibleRate;
	}
}
