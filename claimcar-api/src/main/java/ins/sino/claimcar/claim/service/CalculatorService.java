package ins.sino.claimcar.claim.service;

import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;

public interface CalculatorService {
	
	/**
	 * 组织理算数据
	 */
	public CompensateVo orgnizeCalculateData(PrpLRegistVo prpLregistVo, PrpLCMainVo prpLcMainVo, PrpLCheckVo prpLCheckVo, PrpLClaimVo prpLClaimVo, String compensateType);

	/**
	 * 返回填充n个空格的字符串
	 */
	public String space(int n);

	/**
	 * 获取车辆信息
	 */
	public List<PrpLDlossCarInfoVo> getThirdPartiesInfo(PrpLClaimVo prpLClaimVo, String registNo, String compensateType);
	
	/**
	 * 交强 商业页面组织理算数据
	 */
	public CompensateVo orgnizeCompensateData(PrpLCompensateVo prpLCompensateVo,List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
	                                          List<PrpLLossPersonVo> prpLLossPersonVoList, PrpLClaimVo prpLClaimVo,String compensateType);
	/**
	 * 判断是否承保该险别
	 * <pre></pre>
	 * @param kindCode
	 * @param cMainVo
	 * @return
	 * @modified:
	 * ☆WLL(2018年3月6日 下午7:20:28): <br>
	 */
	public boolean HadBuyTheKind(String kindCode,PrpLCMainVo cMainVo);
	
}
