/******************************************************************************
* CREATETIME : 2016年7月28日 下午12:05:55
******************************************************************************/
package ins.sino.claimcar.claim.service;

import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;

import java.util.List;




public interface PrpdLawFirmService {
	/**
	 * <pre></pre>
	 * @author zjd
	 */
	public List<PrpdLawFirmVo> findPrpdLawFirmVo(String comCode);
}
