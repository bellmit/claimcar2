package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.vo.PrpLWfNodeVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeShowVo;

import java.math.BigDecimal;
import java.util.List;

public interface WfNodeService {

	/**
	 * 保存或更新node表
	 * @param wfNodeVo
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public void save(PrpLWfNodeVo wfNodeVo);



	/**
	 * 更新节点数据
	 * @param schedNodeVo
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	public void update(PrpLWfNodeVo schedNodeVo);

	/**
	 * 查找节点图node信息
	 * @param flowId
	 * @return
	 */
	public List<WfFlowNodeShowVo> findWfFlowNodeShowList(String flowId);

	/**
	 * 删除PrpLWfNode
	 * <pre></pre>
	 * @param id
	 * @modified:
	 * ☆zhujunde(2018年3月19日 下午3:25:12): <br>
	 */
	public void delete(BigDecimal id);
	
	/**
	 * 查询PrpLWfNodeVo
	 * <pre></pre>
	 * @param flowId
	 * @param nodeCode
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年3月19日 下午3:30:14): <br>
	 */
	public List<PrpLWfNodeVo> findBydFlowIdAndNodeCode(String flowId,String nodeCode);
}
