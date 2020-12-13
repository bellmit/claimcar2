package ins.sino.claimcar.trafficplatform.service;

import ins.sino.claimcar.claim.vo.PrpLCompensateVo;


public interface SendVClaimToEWPlatformService {
	
	/**
	 * 理算核赔送山东预警平台
	 * <pre></pre>
	 * @param compensateVo
	 * @modified:
	 * ☆WLL(2018年6月11日 下午3:28:48): <br>
	 */
	 public void SendVClaimToEWPlatform(PrpLCompensateVo compensateVo);

}
