/******************************************************************************
* CREATETIME : 2016年8月16日 下午5:43:43
******************************************************************************/
package ins.sino.claimcar.other.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;


/**
 * @author ★XMSH
 */
public interface AssessorDubboService {

	public void saveOrUpdatePrpLAssessor(PrpLAssessorVo assessorVo,SysUserVo userVo);
	
	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType,Integer serialNo, String intermcode);

	public PrpLAssessorVo findAssessorByLossId(String registNo,String taskType, String intermcode);
}
