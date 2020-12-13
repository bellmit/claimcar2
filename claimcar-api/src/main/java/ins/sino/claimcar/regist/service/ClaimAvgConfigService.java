package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.regist.vo.PrpDClaimAvgVo;

import java.math.BigDecimal;
import java.util.List;

public interface ClaimAvgConfigService {

	public abstract ResultPage<PrpDClaimAvgVo> findTaskForPage(
			PrpDClaimAvgVo prpDClaimAvgVo, Integer start, Integer length)
			throws Exception;

	public abstract String updates(PrpDClaimAvgVo prpDClaimAvgVo);

	public abstract List<PrpDClaimAvgVo> findAvgConfig(BigDecimal avgYear,
			String comCode, List<String> riskCodes);

	// 更新
	public abstract void updatePrpDClaimAvg(List<PrpDClaimAvgVo> prpDClaimAvgVoList);

}