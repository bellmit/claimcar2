package ins.sino.claimcar.claim.calculator;

import ins.sino.claimcar.claim.vo.ClaimFeeExt;
import ins.sino.claimcar.claim.vo.CompensateListVo;

import java.util.List;

/**
 * 赔款理算完成后,刷新立案险别估计赔款
 * <pre></pre>
 * @author ★ZhouYanBin
 */
public interface RefreshClaimEstiPaidService {
	/**
	 * 刷新交强立案估损
	 * <pre></pre>
	 * @param compensateList
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 上午9:49:39): <br>
	 */
	public List<ClaimFeeExt> refreshClaimEstiPaid(CompensateListVo compensateList);
	
	/**
	 * 刷新商业立案估损
	 * <pre></pre>
	 * @param compensateList
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年4月14日 上午9:49:58): <br>
	 */
	public List<ClaimFeeExt> refreshClaimEstiPaidBI(CompensateListVo compensateList);
}
