/******************************************************************************
 * CREATETIME : 2016年9月27日 下午4:07:20
 ******************************************************************************/
package ins.sino.claimcar.payment.service;

import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;

import java.util.List;

/**
 * @author ★XMSH
 */
public interface ClaimToPaymentDetailService {

	/**
	 * 预付调用收付接口
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:11:36): <br>
	 */
	public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception;

	/**
	 * 平安预付送收付
	 * @param compensateVo
	 * @param prePayVos
	 * @throws Exception
	 */
	public void pingAnPrePayToPayment(PrpLCompensateVo compensateVo, List<PrpLPrePayVo> prePayVos) throws Exception;
	/**
	 * 垫付调用收付接口
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:12:21): <br>
	 */
	public void padPayToPayment(PrpLPadPayMainVo padPayMainVo) throws Exception;

	/**
	 * 理算调用收付接口
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:12:33): <br>
	 */
	public void compensateToPayment(PrpLCompensateVo compensateVo) throws Exception;
	
	/**
	 * 预付数据更新接口，排除重开的结案
	 * @param compensateVo
	 * @throws Exception
	 */
	public void updatePrePayToPayment(PrpLCompensateVo compensateVo) throws Exception;
	
	/**
	 * 公估费送收付
	 * @param recLossVo
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月29日 下午2:43:42): <br>
	 */
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo,PrpLAssessorFeeVo assessorFeeVo) throws Exception;
	/**
	 * 查勘费送收付
	 * @param checkMainVo
	 * @param checkFeeVo
	 * @throws Exception
	 */
	public void checkFeeToPayment(PrpLAcheckMainVo checkMainVo,PrpLCheckFeeVo checkFeeVo) throws Exception;

}
