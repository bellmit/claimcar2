package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCMainVo;

import java.util.List;

public interface RegistTmpService {

	public String registTmpSave(PrpLTmpCMainVo tmpCMainVo,
			List<PrpLTmpCItemKindVo> tmpCItemKindVos,
			PrpLTmpCItemCarVo tmpCItemCarVo,
            PrpLRegistVo prpLRegistVo);

	public List<PrpLCMainVo> findTempPolicyByRegistNo(String registNo);

	public List<PrpLCMainVo> findAreadictByPolicyNo(String policyNo);

	public List<PrpLCMainVo> deleteByRegistNoAndRiskCode(String registNo,
			String riskCode);

	public List<PrpLCMainVo> queryByClaimsequenceNo(String claimsequenceNo);
}