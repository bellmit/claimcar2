package ins.sino.claimcar.trafficplatform.service;

import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;


public interface SendEndCaseToEWPlatformService {
	
	/**
	 * 结案送山东预警平台
	 * <pre></pre>
	 * @param endVo
	 * @modified:
	 * ☆WLL(2018年6月11日 下午3:28:27): <br>
	 */
	public void SendEndCaseToEWPlatform(PrpLEndCaseVo endVo);

}
