/******************************************************************************
* CREATETIME : 2016年9月22日 下午4:49:38
******************************************************************************/
package ins.sino.claimcar.recloss.service;

import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

import java.util.List;


/**
 * @author ★XMSH
 */
public interface RecLossDubboService {
	
	public List<PrpLRecLossVo> findPrpLRecLossListByMainId(String recLossMainId);

}
