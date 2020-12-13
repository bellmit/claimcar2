/******************************************************************************
 * CREATETIME : 2016年9月24日 下午5:55:56
 ******************************************************************************/
package ins.sino.claimcar.recloss.service;

import ins.sino.claimcar.recloss.vo.PrpLHandReasonVo;

import java.math.BigDecimal;

/**
 * <pre></pre>
 * @author ★Luwei
 */
public interface WfHandReasonServices {

	public abstract void saveSysMsg(PrpLHandReasonVo prpLHandReasonVo);

	/*
	 * 根据id查询
	 */
	public abstract PrpLHandReasonVo findHandReasonById(BigDecimal taskId);

}
