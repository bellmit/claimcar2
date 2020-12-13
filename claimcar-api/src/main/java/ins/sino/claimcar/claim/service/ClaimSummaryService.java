/******************************************************************************
* CREATETIME : 2016年7月28日 下午12:05:55
******************************************************************************/
package ins.sino.claimcar.claim.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;

import java.util.List;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年7月28日
 */
public interface ClaimSummaryService {


	/**
	 * 立案刷新ClaimSummary
	 * @param registVo
	 * @return
	 * @modified: ☆LiuPing(2016年7月28日 ): <br>
	 */
	public PrpLClaimSummaryVo updateByClaim(PrpLClaimVo claimVo);

	/**
	 * 结案刷新ClaimSummary
	 * @param registVo
	 * @return
	 * @modified: ☆LiuPing(2016年7月28日 ): <br>
	 */
	public PrpLClaimSummaryVo updateByEndCase(PrpLCompensateVo compensateVo);
	
	/**
	 * 历史出险次数(根据保单查询PrpLClaimSummary记录数，目前只查询新理赔的数据库的数据)
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月18日 下午5:38:54): <br>
	 */
	public int claimNumber(String policyNo);

	/**
	 * 历史出险信息公共按钮
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月18日 下午5:43:28): <br>
	 */
	public ResultPage<PrpLClaimSummaryVo> findPageForHistory(String policyNo,int start,int length);

	
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryVoList(String policyNo,String excludeRegistNo);

	
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryVoList(String policyNo);
	
	public PrpLClaimSummaryVo findPrpLClaimSummaryDescVo(String policyNo);

}
