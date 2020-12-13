/******************************************************************************
 * CREATETIME : 2015年12月10日 下午3:34:33
 * FILE       : ins.sino.claimcar.dloss.ClaimTextService
 ******************************************************************************/
package ins.sino.claimcar.common.losscharge.service;

import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeHisVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;

import java.util.List;


/**
 * <pre></pre>
 * 
 * @author ★yangkun
 */
public interface LossChargeService {
	
	/**
	 * saveChargeHis-是否保存费用轨迹表
	 * @param lossChargeVos
	 * @param saveChargeHis
	 */
	public void saveOrUpdte(List<PrpLDlossChargeVo> lossChargeVos);
	
	public List<PrpLDlossChargeVo> findLossChargeVos(Long businessId,String busineeType);
	
	public void saveLossCharge(PrpLDlossChargeVo lossChargeVo);
	
	public void updateLossCharge(PrpLDlossChargeVo lossChargeVo);
	
	public List<PrpLDlossChargeVo> findLossChargeVos(String registNo,Long businessId,String businessType);
	
	public List<PrpLDlossChargeVo> findLossChargeVoByRegistNo(String registNo);

	// 删除所有的费用
	public void delCharge(Long id,String name);
	
	/**
	 * 保存费用轨迹
	 * @param businessId
	 * @param busineeType
	 */
	public void saveChargeHis(Long businessId,String busineeType);
	
	/**
	 * 查询费用轨迹表，取创建日期最新的一条
	 * @param lossChargeId
	 * @param businessId
	 * @return
	 */
	public PrpLDlossChargeHisVo findLossChargeHisVo(Long lossChargeId,Long businessId);
}
