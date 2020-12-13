/******************************************************************************
* CREATETIME : 2016年2月26日 上午11:47:55
******************************************************************************/
package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;

import java.util.List;

/**
 * 工作流查询接口
 * @author ★LiuPing
 * @CreateTime 2016年2月26日
 */
public interface WfFlowQueryService {

	/**
	 * 查询由 nodeCode 发起，但还未接收进行处理的任务
	 * @param registNo
	 * @param nodeCode
	 * @return
	 * @modified: ☆LiuPing(2016年2月26日 ): <br>
	 */
	public List<PrpLWfTaskVo> findUnAcceptTask(String registNo,String... nodeCode);
	
	/**
	 * 查找未处理完成的任务
	 * @param registNo
	 * @param nodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findPrpWfTaskVo(String registNo,String... nodeCode );
	
	/**
	 * 查勘是否完成
	 * @param registNo
	 * @return
	 */
	public boolean isCheckNodeEnd(String registNo);
	
	//根据报案号查询单证zjd
	public List<PrpLWfTaskVo> findCertiByRegistNo(String registNo);

	/**
	 * workStatus 0 表示查询 PrpLWfTaskIn表  ，1 表示查询 已处理的数据 PrpLWfTaskout表 
	 * @param registNo
	 * @param subNode
	 * @param workStatus
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月15日 下午3:54:54): <br>
	 */
	public List<PrpLWfTaskVo> findWfTaskVo(String registNo,FlowNode subNode,String workStatus);
	
	/**
	 * 根据报案号节点查找节点
	 * @param registNo
	 * @param nodeCode
	 * @param subNodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findPrpWfTaskVo(String registNo,String nodeCode,String subNodeCode);
	
	/**
	 * 查找out表的任务
	 * @param registNo
	 * @param nodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findPrpWfTaskVoForOut(String registNo,String... nodeCode );
	
	/**
	 * 查找out表的任务
	 * @param registNo
	 * @param nodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findTaskVoForInAndOut(String registNo,String  nodeCode,String... taskInNode);
	
	/**
	 * 查找out表的任务
	 * @param registNo
	 * @param subNodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findTaskVoForOutBySubNodeCode(String registNo,String subNodeCode);
	
	/**
	 * 查找工作流表的任务
	 * @param registNo
	 * @return
	 */
	public List<PrpLWfTaskVo> findTaskVoForInAndOutByRegistNo(String registNo);
	
	/**
	 * 根据报案号查询注销或挂起的节点，限查勘，定损节点
	 * @param registNo
	 * @return
	 */
	public List<PrpLWfTaskVo> findCancelByRegistNo(String registNo);
	
	  /**
	   * 根据registNo和nodeCode查询
	   * <pre></pre>
	   * @param registNo
	   * @param nodeCode
	   * @return
	   * @modified:
	   * ☆zhujunde(2017年11月3日 下午5:23:31): <br>
	   */
    public List<PrpLWfTaskVo> findTaskVoForOutByNodeCode(String registNo,String nodeCode);
    
    /**
     * 根据registNo和nodeCode查询
     * <pre></pre>
     * @param registNo
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年11月3日 下午5:23:31): <br>
     */
  public List<PrpLWfTaskVo> findTaskVoForInByNodeCode(String registNo,String nodeCode);
  
  /**
   * 
   * <pre></pre>
   * @param registNo
   * @param nodeCode
   * @param subNodeCode
   * @return
   * @modified:
   * ☆zhujunde(2018年1月25日 下午5:41:17): <br>
   */
  public List<PrpLWfTaskVo> findPrpWfTaskVoForIn(String registNo,String nodeCode,String subNodeCode);

  /**
   * 根据查询Vo中的条件查询工作流IN表或者OUT表的数据,根据流入或流出时间倒序排列
   * @param WfQueryVo 查询的入参Vo 至少需要 registNo 和 subNodeCode
   * @param inOutFlag 查询IN还是OUT的Flag: IN-0 OUT-1
   * @return List<PrpLWfTaskVo> 返回工作流VoList
   * @author willingRan
   */
  public List<PrpLWfTaskVo> findTaskVoForQueryVo(PrpLWfTaskVo WfQueryVo,String inOutFlag);
  /**
   *根据立案号与节点查询in表
   * <pre></pre>
   * @param claimNo
   * @param nodeCode
   * @return
   * @modified:
   * ☆yzy(2019年1月30日 下午6:27:11): <br>
   */
  public List<PrpLWfTaskVo> findTaskVoForInByClaimNo(String claimNo,String nodeCode);

/**
 * 查询当前节点外其他正在处理的节点
 * @param registNo
 * @param flowId
 * @return
 * @modified:
 * ☆XiaoHuYao(2019年8月17日 下午6:00:22): <br>
 */
public List<PrpLWfTaskVo> findOtherPrpWfTaskVo(String registNo,Long flowId);
  
}
