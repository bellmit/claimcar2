package ins.sino.claimcar.flow.service;

import ins.framework.common.ResultPage;
import ins.platform.saa.schema.SaaUserPermitCompany;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskQueryResultVo;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface WfTaskQueryService {

	public void save(PrpLWfTaskQueryVo taskQueryVo);

	/**
	 * flags为1要更新PolicyNoLink为空的情况
	 * <pre></pre>
	 * @param taskQueryVo
	 * @param flags
	 * @modified:
	 * ☆zhujunde(2017年7月20日 下午2:13:25): <br>
	 */
	public void update(PrpLWfTaskQueryVo taskQueryVo,String flags);

	/**
	 * 所有节点的任务查询
	 * 
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 * @modified: ☆LiuPing(2016年1月10日 下午7:47:42): <br>
	 */
	public ResultPage<WfTaskQueryResultVo> findTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception;

	/**
	 * 查找平级移交任务
	 * 
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	public ResultPage<WfTaskQueryResultVo> findHandoverTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception;
	
	public ResultPage<WfTaskQueryResultVo> findCancelTaskForPage(PrpLWfTaskQueryVo taskQueryVo)  throws Exception;

	public ResultPage<WfTaskQueryResultVo> findPLossTaskForPage(PrpLWfTaskQueryVo taskQueryVo) throws Exception;

	/**
	 * <pre>公估费任务查询</pre>
	 * @param taskQueryVo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public ResultPage<WfTaskQueryResultVo> findAssessorFeeTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException;

	/**
	 * <pre>公估费审核查询</pre>
	 * @param taskQueryVo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public ResultPage<WfTaskQueryResultVo> findAssessorFeeVeriTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException;

	/**
	 * <pre>查勘费任务查询</pre>
	 * @param taskQueryVo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public ResultPage<WfTaskQueryResultVo> findCheckFeeTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException;

	/**
	 * <pre>查勘费审核查询</pre>
	 * @param taskQueryVo
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public ResultPage<WfTaskQueryResultVo> findCheckFeeVeriTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws IllegalAccessException,InvocationTargetException,NoSuchMethodException;

	public List<SaaUserPermitCompany> findSaaUserPermitCompanyByUserCode(String userCode);
	
	/*
	 *  ywuser.PrpDcompany
	 * 	查询机构信息
	 */
	public List<SysCodeDictVo> findPrpDcompanyByUserCode(String comCode,String... comlevel);
	
	public List<SysCodeDictVo> findPermitcompanyByUserCode(String userCode);
	/**
	 * 
	 * 是否核损完成
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月6日 下午4:37:17): <br>
	 */
	public List<WfTaskQueryResultVo> findPrpLWfTaskInByRegistNo(String registNo,List<String> nodeCodeList);
//	public ResultPage<WfTaskQueryResultVo> findTaskForWork(PrpLWfTaskQueryVo taskQueryVo)  throws Exception;
	
/*	public ResultPage<PrpLClaimVo> findCancelAppTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws Exception;
	
	public ResultPage<PrpLClaimVo> findRecanAppTaskQuery(PrpLWfTaskQueryVo taskQueryVo) throws Exception;
*/
	
	/**
	 * 工作台非人伤查询
	 * @param taskQueryVo
	 * @return
	 * @throws Exception
	 */
	public ResultPage<WfTaskQueryResultVo> findTaskForWorkBench(PrpLWfTaskQueryVo taskQueryVo) throws Exception ; 
	
	/**
	 * 统计查询结果中超时任务的数量
	 * @param resultVoList
	 * @param handleStatus
	 * @return
	 */
	public int countTimeOut(PrpLWfTaskQueryVo taskQueryVo);
	
	public int countLossTimeOut(PrpLWfTaskQueryVo taskQueryVo);
	
	public List<PrpLWfTaskVo> checkHandlerRegistTask(String policyNo);
	
	/**
	 * 代查勘标识，0-正常案件，1-司内代查勘，2-公估代查勘，3-公估案件：
	 * areaCode传null则查询调度表
	 * @param registNo
	 * @param assignUser
	 * @param areaCode
	 * @return
	 */
	public String getSubCheckFlag(String registNo,String assignUser,String areaCode);
	
	/**
	 * 查询有效的定损任务
	 * @param subNode
	 * @param registNo
	 * @return
	 */
	public List<PrpLWfTaskVo> findTaskBySubNode(String subNode,String registNo);
	public void updateTaskIn(PrpLWfTaskVo taskInVo);
	public void updateTaskOut(PrpLWfTaskVo taskInVo);
	
	/**
	 * 根据userCode查询saa_permitcompany的机构
	 * <pre></pre>
	 * @param userCode
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年5月6日 上午9:04:21): <br>
	 */
	public List<SysCodeDictVo> findAllPermitcompanyByUserCode(String userCode);
	/**
	 * saa_permitcompany的机构,拼接机构字符串
	 * @param sysCodeList
	 * @param WebUserComCode
	 * @return
	 */
	public String getAllPermitcompanyByUserCode(List<SysCodeDictVo> sysCodeList,String WebUserComCode);
}
