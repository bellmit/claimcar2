/******************************************************************************
* CREATETIME : 2016年1月13日 下午3:34:33
******************************************************************************/
package ins.sino.claimcar.common.lossindex.service;

import ins.sino.claimcar.common.lossindex.vo.PrpLDlossIndexVo;


/**
 * <pre></pre>
 * @author ★yangkun
 */
public interface LossIndexService {
	
	public void saveOrUpdte(PrpLDlossIndexVo lossIndexVo);
	
	public PrpLDlossIndexVo findLossIndex(Long bussTaskId,String nodeCode);
}
