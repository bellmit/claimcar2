/******************************************************************************
 * CREATETIME : 2016年9月27日 下午4:07:20
 ******************************************************************************/
package ins.sino.claimcar.payment.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

/**
 * @author ★XMSH
 */
public interface ClaimToPaymentService {

	/**
	 * 预付调用收付接口
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:11:36): <br>
	 */
	public void prePayToPayment(PrpLCompensateVo compensateVo) throws Exception;

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
	 * 追偿调用收付接口
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:12:44): <br>
	 */
	public void recPayToPayment(PrplReplevyMainVo prplReplevyMainVo) throws Exception;

	/**
	 * 损余回收调用收付接口
	 * @throws Exception 
	 * @modified:
	 * ☆XMSH(2016年7月16日 下午4:12:53): <br>
	 */
	public void recLossToPayment(PrpLRecLossVo recLossVo) throws Exception;
	
	/**
	 * 公估费送收付
	 * @param recLossVo
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月29日 下午2:43:42): <br>
	 */
	public void assessorToPayment(PrpLAssessorMainVo assessorMainVo,PrpLAssessorFeeVo assessorFeeVo) throws Exception;
	
	
	/**
	 * 查勘费送发票
	 * <pre></pre>
	 * @param PrpLCheckFeeVo
	 * @param userVo
	 * @throws Exception
	 * @modified:
	 * *yzy(2019年8月7日 上午9:18:19): <br>
	 */
	public boolean pushCheckFee(PrpLCheckFeeVo checkFeeVo,SysUserVo userVo) throws Exception;
	
    /**
     * <pre></pre>
     * @param copensateno
     * @return
     * @throws Exception
     * @modified:
     * *牛强(2017年1月4日 上午9:18:33): <br>
     */
	
	public boolean pushCharge(String compensateNo,String serialNoSend) throws Exception;
	
	public boolean pushPreCharge(String compensateNo,String serialNoSend) throws Exception;
	
	/**
	 * 公估费送发票
	 * <pre></pre>
	 * @param assessorVo
	 * @param userVo
	 * @throws Exception
	 * @modified:
	 * *牛强(2017年1月4日 上午9:18:19): <br>
	 */
	public boolean pushAssessorFee(PrpLAssessorFeeVo assessorVo,SysUserVo userVo) throws Exception;
	
	/**
	 * 垫付送vat
	 * <pre></pre>
	 * @param padPayMainVo
	 * @param serialNoSend
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年3月21日 上午9:23:23): <br>
	 */
	public Boolean pushPadPay(PrpLPadPayMainVo padPayMainVo,String serialNoSend);
}
