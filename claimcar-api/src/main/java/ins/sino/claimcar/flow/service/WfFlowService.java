package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.platform.saa.vo.SaaFactorPowerVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfFlowNodeVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WfFlowService {

	/**
	 * 根据flowId得到所有的任务节点信息
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public abstract List<WfFlowNodeVo> findAllWfTaskByFlowId(String flowId) throws Exception;

	/**
	 * 流程查询
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	public abstract ResultPage<WfTaskQueryResultVo> findFlowForPage(PrpLWfTaskQueryVo taskQueryVo,Map<String,List<SaaFactorPowerVo>> factorPowerMap) throws Exception;

	/**
	 * 根据flowId查找PrpLWfTaskQuery信息
	 * @param flowId
	 * @return
	 */
	public abstract PrpLWfTaskQueryVo findPrpLWfTaskQueryByFlowId(String flowId);

	/**
	 * 根据taskId查找PrpLWfTaskVo信息
	 * <pre></pre>
	 * @param taskId
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年11月3日 上午9:49:23): <br>
	 */
	public abstract PrpLWfTaskVo findPrpLWfTaskQueryByTaskId(BigDecimal taskId);
	
	/**
	 * 旧理赔流程查询
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	public abstract ResultPage<WfTaskQueryResultVo> findOldFlowForPage(PrpLWfTaskQueryVo taskQueryVo,Map<String,List<SaaFactorPowerVo>> factorPowerMap) throws Exception;

	/**
	 * 该立案号下是否存在有效的预付
	 * @param claimNo
	 * @return
	 */
	public boolean findValidPrePay(String claimNo);
}
