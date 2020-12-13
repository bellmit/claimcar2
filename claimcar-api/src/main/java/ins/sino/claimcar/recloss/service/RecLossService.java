/******************************************************************************
 * CREATETIME : 2016年9月24日 下午5:32:38
 ******************************************************************************/
package ins.sino.claimcar.recloss.service;

import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.recloss.vo.PrpLRecLossVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface RecLossService {

	/**
	 * 保存损余回收
	 * @param prpLRecLossVo
	 * @return
	 */
	public abstract String saveOrUpdate(PrpLRecLossVo prpLRecLossVo);

	/**
	 * 主键查找损余回收信息
	 * @param recLossId
	 * @return
	 */
	public abstract PrpLRecLossVo findRecLossByRecLossId(String recLossId);

	/**
	 * 构建车损损余回收信息
	 * @param prpLDlossCarMainVo
	 * @param operatorCode
	 * @param operatorName
	 * @return
	 */
	public abstract PrpLRecLossVo createCarPrpLRecLossVo(PrpLDlossCarMainVo prpLDlossCarMainVo,String operatorCode);

	/**
	 * 初始化车辆修改损余回收信息
	 * @param prpLDlossCarMainVo
	 * @param prpLRecLossVo
	 * @param operatorCode
	 */
	public abstract PrpLRecLossVo initUpdateCarRecLossInfo(PrpLDlossCarMainVo prpLDlossCarMainVo,PrpLRecLossVo prpLRecLossVo,
													String operatorCode);

	/**
	 * 构建物损损余回收信息
	 * @param lossPropMainVo
	 * @param operatorCode
	 * @param operatorName
	 * @return
	 */
	public abstract PrpLRecLossVo createPropPrpLRecLossVo(PrpLdlossPropMainVo lossPropMainVo,String operatorCode);

	/**
	 * 初始化财产修改损余回收信息
	 * @param lossPropMainVo
	 * @param prpLRecLossVo
	 * @param operatorCode
	 */
	public abstract PrpLRecLossVo initUpdatePropRecLossInfo(PrpLdlossPropMainVo lossPropMainVo,PrpLRecLossVo prpLRecLossVo,
													String operatorCode);

	/**
	 * 通过recLossMainId查找损余回收列表
	 * @param recLossMainId
	 * @return
	 */
	public abstract List<PrpLRecLossVo> findPrpLRecLossListByMainId(String recLossMainId);

	/**
	 * 根据报案号  损余回收类型 损余回收定损主表id 查找唯一记录
	 * @param registNo
	 * @param recLossType
	 * @param lossMainId
	 * @return
	 */
	public abstract PrpLRecLossVo findPrpLRecLoss(String registNo,String recLossType,Long lossMainId);

	/**
	 * <pre></pre>
	 * @param handlerIdKeys
	 * @param name
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午5:42:02): <br>
	 */
	public abstract List<PrpLWfTaskVo> queryTaskList(List<String> handlerIdKeys,String name,String registNo);

	/**
	 * <pre></pre>
	 * @param taskIds
	 * @return
	 * @modified:
	 * ☆Luwei(2016年9月24日 下午5:44:20): <br>
	 */
	public abstract List<PrpLWfTaskVo> queryTaskList(List<BigDecimal> taskIds);

}
