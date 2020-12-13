/**
 * 
 */
package service;

import java.util.List;

import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

/**
 * @author CMQ
 */
public interface ClaimTaskService {
	
	
	/**
	 * 查看完成生成立案数据
	 * @param scheduleVo
	 * @param checkVo
	 * @return
	 */
	public PrpLClaimVo submitCheck(PrpLScheduleTaskVo scheduleVo,PrpLCheckVo checkVo);
	
	/**
	 * 自动立案
	 * @param scheduleVo
	 * @param checkVo
	 * @return
	 */
	public PrpLClaimVo autoClaim(PrpLRegistVo registVo);
	
	/**
	 * 根据报案号查找立案列表
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年3月26日 下午5:02:45): <br>
	 */
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo);
	
	/**
	 * 刷新或者生产立案(强制立案、查勘提交、人伤审核、核损通过、立案修改等) ，其中查勘提交时，policyNo、businessId可以为空
	 * 
	 * <pre></pre>
	 * @param registNo 报案号
	 * @param policyNo 保单号
	 * @param businessNo 任务主表id
	 * @param nodeCode 节点名称
	 * @return
	 * @modified: ☆ZhouYanBin(2016年3月19日 下午3:16:02): <br>
	 */
	public List<PrpLClaimVo> refreshPrpLClaimVo(String registNo,String policyNo,String businessNo,String nodeCode);
	
	/**
	 * 根据保单号和立案号查找有效立案
	 * <pre></pre>
	 * @param registNo
	 * @param policyNo
	 * @return
	 * @modified:
	 * ☆ZhouYanBin(2016年3月26日 下午5:04:20): <br>
	 */
	public PrpLClaimVo findPrpLClaimVoByRegistNoAndPolicyNo(String registNo,String policyNo);

}
