/******************************************************************************
* CREATETIME : 2015年12月10日 下午3:34:33
* FILE       : ins.sino.claimcar.dloss.ClaimTextService
******************************************************************************/
package ins.sino.claimcar.common.claimtext.service;

import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;

import java.util.List;
import java.util.Map;


/**
 * <pre></pre>
 * @author ★yangkun
 */

public interface ClaimTextService {

	public void saveOrUpdte(PrpLClaimTextVo claimTextVo);
	
	public List<PrpLClaimTextVo> findClaimTextList(Long bussTaskId,String registNo,String bigNode);
	
	/**
	 * flag 节点未完成0,完成之后为1 
	 * @modified:
	 * ☆yangkun(2016年1月19日 上午9:50:26): <br>
	 */
	public PrpLClaimTextVo findClaimTextByNode(Long bussTaskId,String nodeCode,String flag);
	
	/**
	 * 
	 * @param lossCarMainId
	 * @param nodeCode
	 * @return
	 */
	public PrpLClaimTextVo findClaimTextByLossCarMainIdAndNodeCode(Long lossCarMainId,String nodeCode);
	
	/**
	 * 查找意见列表
	 * @param map
	 * @return
	 */
	public List<PrpLClaimTextVo> findClaimTextList(Map<String,String> map);
	/**
	 * 
	 * @param bussTaskId
	 * @param registNo
	 * @return
	 */
	public List<PrpLClaimTextVo> findClaimTextByregistNoAndbussTaskId(String registNo,Long bussTaskId);
}
