package ins.sino.claimcar.claim.calculator;

import ins.sino.claimcar.claim.vo.CompensateListVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;

import java.io.Serializable;


public interface Calculator extends Serializable {

	/**
	 * 赔款计算
	 */
	public CompensateListVo calculate(CompensateListVo compensateListVo);
	/**
	 * 交强计算公式(商业险需扣交强使用)
	 */
	public CompensateListVo calCulatorCi(CompensateVo compensateVo,PrpLClaimVo prpLClaimVo);
	
	
}
