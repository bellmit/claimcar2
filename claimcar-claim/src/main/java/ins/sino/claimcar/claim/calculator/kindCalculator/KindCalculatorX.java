package ins.sino.claimcar.claim.calculator.kindCalculator;

import ins.framework.lang.Springs;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.ThirdPartyDepreRate;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.math.BigDecimal;


public class KindCalculatorX extends KindCalculatorA {
	private PolicyViewService policyViewService = (PolicyViewService)Springs.getBean("policyViewService");

	public KindCalculatorX(CompensateVo compensateVo,Object object,CompensateService compensateService){
		super(compensateVo,object,compensateService);
	}

	public BigDecimal calDepreRate(BigDecimal value) {
		// 总折旧率折旧率取0（不折旧）。
		return super.calDepreRate(new BigDecimal(0));
	}

	public BigDecimal calItemValue(BigDecimal value) {
		// 实际价值取新设备价值。
		PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(prpLcItemKind.getRegistNo(),prpLcItemKind.getPolicyNo());
		ThirdPartyDepreRate thirdPartyDepreRate = this.compensateService.queryThirdPartyDepreRate(this.prpLcompensate.getRegistNo());
		return super.calItemValue(BigDecimal.valueOf(thirdPartyDepreRate.getDeviceActualValue()));
	}

	@Override
	public BigDecimal calAmount(BigDecimal value) {
		return super.calAmount(super.queryOnceSubAmount());
	}
}
