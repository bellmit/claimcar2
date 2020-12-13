/******************************************************************************
* CREATETIME : 2015年10月22日 上午9:44:55
******************************************************************************/
package ins.sino.claimcar.flow.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskUserInfoVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.other.vo.PrpLAcheckMainVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 工作流处理服务接口
 * @author ★LiuPing
 * @CreateTime 2016年1月8日
 */
public interface WfTaskHandleService {

	/**
	 * 查询任务
	 * @throws Exception ☆LiuPing(2016年1月9日 下午2:05:32): <br>
	 */
	public PrpLWfTaskVo queryTask(Double flowTaskId);
	
	/**
	 * 查询待提交的任务
	 * @param flowTaskId
	 * @return
	 * @modified:
	 * ☆XMSH(2016年7月28日 下午4:00:11): <br>
	 */
	public PrpLWfTaskVo findTaskIn(Double flowTaskId)throws Exception;
	/**
	 * 保存工作流In数据
	 * @param prpLWfTaskVo
	 * @return
	 */
	public PrpLWfTaskVo saveTaskIn(PrpLWfTaskVo prpLWfTaskVo);

	/**
	 * 接收单个任务
	 * @throws Exception ☆LiuPing(2016年1月9日 下午2:05:32): <br>
	 */
	public PrpLWfTaskVo acceptTask(Double flowTaskId,String assignUser,String assignCom);

	/**
	 * 注销单个或多个任务
	 * @throws Exception ☆LiuPing(2016年1月9日 下午2:05:32): <br>
	 */
	public void cancelTask(String userCode,BigDecimal... flowTaskId);

	/**
	 * 暂存任务,将任务变为正在处理
	 * @throws Exception ☆LiuPing(2016年1月9日 下午2:05:32): <br>
	 */
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom);
	
	//暂存追偿
	public PrpLWfTaskVo tempSaveTaskByRecPay(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom,PrplReplevyMainVo prplReplevyMainVo);

	/**
	 * 报案提交 ☆LiuPing(2016年1月9日 下午4:13:53): <br>
	 */
	public PrpLWfTaskVo submitRegist(PrpLRegistVo registVo,WfTaskSubmitVo submitVo);

	/**
	 * 调度提交
	 * @modified: ☆LiuPing(2016年1月11日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitSchedule(PrpLRegistVo registVo,List<PrpLScheduleTaskVo> scheduleTaskVoList,WfTaskSubmitVo submitVo);

	/**
	 * 查勘提交
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitCheck(PrpLScheduleTaskVo scheduleVo,PrpLCheckVo checkVo,WfTaskSubmitVo submitVo);

	/**
	 * 车辆 定损、核损、核价 提交
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitLossCar(PrpLDlossCarMainVo lossCarMainVo,WfTaskSubmitVo submitVo);

	/**
	 * 财产 定损、核损、提交
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitLossProp(PrpLdlossPropMainVo lossPropMainVo,WfTaskSubmitVo submitVo);

	/**
	 * 人伤跟踪、审核 提交
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitLossPerson(PrpLDlossPersTraceMainVo lossPersonMainVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 立案提交
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitClaim(String flowId,PrpLClaimVo claimVo);

	/**
	 * 单证提交理算
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitCertify(PrpLCertifyMainVo certifyVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 发起预付垫付任务
	 * @modified:
	 * ☆XMSH(2016年4月14日 下午2:55:44): <br>
	 */
	public PrpLWfTaskVo addPrePayTask(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 理算提交核赔
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitCompe(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 预付提交到核赔
	 * @modified:
	 * ☆XMSH(2016年4月14日 上午9:25:38): <br>
	 */
	public PrpLWfTaskVo submitPrepay(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 垫付提交到核赔
	 * @modified:
	 * ☆XMSH(2016年4月14日 上午10:10:04): <br>
	 */
	public PrpLWfTaskVo submitPadpay(PrpLPadPayMainVo padPayVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 追偿提交
	 * @param rePlevyVo
	 * @param submitVo
	 * @return
	 * @modified:
	 * ☆XMSH(2016年4月14日 下午3:56:24): <br>
	 */
	public PrpLWfTaskVo submitRecPay(PrplReplevyMainVo rePlevyVo,WfTaskSubmitVo submitVo);

	/**
	 * 核赔提交结案
	 * @throws Exception 
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public List<PrpLWfTaskVo> submitVclaim(PrpLCompensateVo compVo,WfTaskSubmitVo submitVo) throws Exception;
	
	/**
	 * 核赔提交上级
	 */
	public List<PrpLWfTaskVo> submitVclaimLevel(PrpLWfTaskVo wfTaskVo,WfTaskSubmitVo submitVo);

	/**
	 * 结案
	 * @modified: ☆LiuPing(2016年4月9日 ): <br>
	 */
	public PrpLWfTaskVo submitEndCase(PrpLEndCaseVo endCaseVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 产生一个重开赔案登记节点--已处理
	 * ☆XMSH(2016年5月31日 上午10:13:21): <br>
	 */
	public PrpLWfTaskVo addReOpenAppTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);
	
	
	public PrpLWfTaskVo submitReOpenApp(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);
	
    public PrpLWfTaskVo addAcheckTask(PrpLAcheckMainVo prpLAcheckMainVo,WfTaskSubmitVo submitVo);
	
	public PrpLWfTaskVo submitAcheckTask(PrpLWfTaskVo taskVo,WfTaskSubmitVo submitVo);
	
	
	public PrpLWfTaskVo addAssessorTask(PrpLAssessorMainVo prpLAssessorMainVo,WfTaskSubmitVo submitVo);
	
	public PrpLWfTaskVo submitAssessorTask(PrpLWfTaskVo taskVo,WfTaskSubmitVo submitVo);
	
	/**
	 * 重开赔案
	 * @param flowTaskId
	 * @modified:
	 * ☆XMSH(2016年5月17日 上午10:52:22): <br>
	 */
	public PrpLWfTaskVo submitReOpenTask(BigDecimal flowTaskId,WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);

	/**
	 * 新增一个周边的简单节点
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public PrpLWfTaskVo addSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);

	/**
	 * 提交一个周边节点，此节点变为已处理，如没有后续节点 nextNode=FlowNode.END
	 * @modified: ☆LiuPing(2016年1月15日 ): <br>
	 */
	public PrpLWfTaskVo submitSimpleTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);

	/**
	 * 提交上级相应节点 ☆yangkun(2016年1月19日 上午10:09:33): <br>
	 */
	public List<PrpDNodeVo> findNode(String uppernode,String currencyNode,String nextFlag);
	
	/**
	 * 退回节点 ☆yangkun(2016年1月19日 上午10:09:33): <br>
	 */
	public List<PrpDNodeVo> findLowerNode(Double taskId,String currencyNode,String upperNode);
	
	/**
	 * 查询已处理的节点任务,结果按时间降序
	 * @CreateTime 2016年1月21日 上午10:11:50
	 * @author lichen
	 */
	public List<PrpLWfTaskVo> findEndTask(String registNo,String handlerIdKey,FlowNode nodeCode);

	/**
	 * 是否存在该节点
	 *  workStatus 0 表示查询 PrpLWfTaskIn表  ，1 表示查询 已处理的数据 PrpLWfTaskout表 ,"" 都需要查询
	 * ☆yangkun(2016年2月18日 上午10:31:12): <br>
	 */
	public Boolean existTaskByNodeCode(String registNo,FlowNode nodeCode,String taskInKey,String workStatus);
	
	/**
	 * 任务移交
	 * @param submitVo
	 * @return
	 */
	public PrpLWfTaskVo handOverTask(WfTaskSubmitVo submitVo,String handoverTaskReason);
	
	/**
	 * 查询立案的taskid
	 */
	public PrpLWfTaskVo queryTaskId(String flowId,String subNodeCode);
	
	/**
	 * 立案注销申请提交工作流
	 */
	public PrpLWfTaskVo addCancelTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo);

	/**
	 * 退回处理
	 */
	public void backCancelHandle(WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo);
	// 分公司退回
	public PrpLWfTaskVo backCancel(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo);
	//查询taskid
	public BigDecimal findTaskId(String claimNo,String subNodeCode);
	
	
	/**
	 * 任务改派
	 * @param submitVo
	 * @param handlerIdKey
	 */
	public void reassignmentTask(WfTaskSubmitVo submitVo,String handlerIdKey);
	
	/**
	 * 恢复注销的单个或多个任务
	 * @throws Exception ☆zhujunde(2016年1月9日 下午2:05:32): <br>
	 */
	public void cancelTaskClaimRecover(WfTaskSubmitVo submitVo,String userCode,BigDecimal... flowTaskId);
	/**
	 * 立案注销单个或多个任务
	 * @throws Exception ☆zhujunde(2016年1月9日 下午2:05:32): <br>
	 */
	public void cancelTaskClaim(WfTaskSubmitVo submitVo,String userCode, BigDecimal... flowTaskId);
	
	/**
	 * 发起理算任务
	 * @modified:
	 */
	public PrpLWfTaskVo addCompenTask(PrpLClaimVo claimVo,WfTaskSubmitVo submitVo);
	/**
	 * 发起理算冲销任务
	 * @modified:
	 */
	public PrpLWfTaskVo addPrePayWriteOffTask(PrpLCompensateVo newCompVo,WfTaskSubmitVo submitVo,WfSimpleTaskVo taskVo);
	/**
	 * 暂存任务,将任务变为正在处理，可以多次提交理算
	 * 
	 */
	public PrpLWfTaskVo tempSaveTaskByComp(Double flowTaskId,String handlerIdKey,String handlerUser,String handlerCom);
	/**
	 * 查询立案注销的发起案件
	 */
	public List<PrpLWfTaskVo> findCanCelTask(String claimNo,FlowNode nodeCode);
	
	/**
	 * 更新工作流的itemName
	 */
	public PrpLWfTaskVo tempSaveTask(Double flowTaskId,String itemName);
	
	/**
	 * 更新prplwftaskout表的handlerIdKey
	 * @param flowTaskId
	 * @param handlerIdKey
	 */
	public void updateTaskOut(Double flowTaskId,String handlerIdKey);

	/**
	 * 查询理算任务
	 */
	public List<PrpLWfTaskVo> findCompeByRegistNo(String registNo,String subNodecode);

	/**
	 * 回滚工作流：一般用于提交工作流后又其他事物出现异常，在catch中调用工作流回滚
	 * @modified:
	 */
	public PrpLWfTaskVo rollBackTask(PrpLWfTaskVo taskVo);
	/**
	 * 无保单关联与取消时删除之前的立案节点
	 * @modified:
	 */
	public BigDecimal deleteByRegist(PrpLRegistVo registVo);
	/**
	 * 无保单关联与取消时增加立案节点
	 * @modified:
	 */
	public PrpLWfTaskVo submitClaimHandl(PrpLRegistVo registVo,WfTaskSubmitVo submitVo);
	public PrpLWfTaskVo submitClaimHandls(PrpLRegistVo registVo,WfTaskSubmitVo submitVo);
	public BigDecimal findByRegist(PrpLRegistVo registVo);
	public String findByRegistNoAndNode(PrpLRegistVo registVo,String nodeCode);
	public Boolean existTaskByNodeList(String registNo,List<String> nodeCodeList) ;
	public Boolean existTaskByNode(String registNo,String nodeCode) ;
	/**
	 * 结束此节点变为已处理，如没有后续节点 nextNode=FlowNode.END
	 * 
	 */
	public PrpLWfTaskVo submitClaimTask(WfSimpleTaskVo taskVo,WfTaskSubmitVo submitVo,PrpLClaimCancelVo pClaimCancelVo);

	//查询理算是否存在未注销
	public Boolean existCancelByNode(String registNo,String nodeCode,String handlerstatus) ;
	public Boolean existCancelByNAndH(String registNo, String nodeCode);
	/**
	 * 无保单关联与取消时删除之前的立案节点
	 * @modified:
	 */
	public BigDecimal deleteByRegists(PrpLRegistVo registVo,String subNodeCode);
	
	public List<PrpLWfTaskVo> findPrpLWfTaskInByRegistNo(String registNo);
	//按照任务流入时间降序排列
	public List<PrpLWfTaskVo> findPrpLWfTaskInTimeDescByRegistNo(String registNo);
	
	public List<PrpLWfTaskVo> findPrpLWfTaskOutByRegistNo(String registNo);
	//按照任务流入时间降序排列
	public List<PrpLWfTaskVo> findPrpLWfTaskOutTimeDescByRegistNo(String registNo);
	
	/**
	 * 注销单个或多个任务立案专用
	 */
	public void cancelTaskForOther(String registNo, String userCode);
	public void recoverClaimForOther(String registNo, String userCode,BigDecimal flowTaskId);
	
	public PrpLWfTaskVo queryTaskForHandlerStatus(Double handlerStatus);
	/**
	 * 根据报案号，立案号，节点，工作流状态查询唯一taskid
	 * <pre></pre>
	 * @param registNo
	 * @param claimNo
	 * @param subNodeCode
	 * @param handlerStatus
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年3月1日 上午11:00:34): <br>
	 */
	public PrpLWfTaskVo queryTaskByAny(String registNo,String claimNo,String subNodeCode,String handlerStatus);
	/**
	 * 理算冲销核赔0结案后将单证节点变成已处理
	 * <pre></pre>
	 * @modified:
	 * ☆WLL(2016年12月2日 下午5:05:16): <br>
	 */
	public void submitCertifyAfterEndCase(String registNo);
	public void moveOutToIn(BigDecimal flowTaskId);
	/**
	 * 通过报案号 节点 查找工作流In表数据
	 * <pre></pre>
	 * @param registNo
	 * @param subNodeCode
	 * @return In表数据
	 * @modified:
	 * *牛强(2017年2月27日 下午3:51:14): <br>
	 */
	public PrpLWfTaskVo findWftaskInByRegistnoAndSubnode(String registNo,String subNodeCode);
	/**
	 * 通过报案号节点查找工作流表数据
	 * @param registNo
	 * @param nodeCode
	 * @return
	 */
	public List<PrpLWfTaskVo> findWftaskByRegistNoAndNodeCode(String registNo,String subNodeCode);
	/**
	 * taskIn 表是否存在未处理节点 根据subnodecode判断
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @author WLL
	 */
	public Boolean existTaskInBySubNodeCode(String registNo,String subNodeCode);
	
	/**
	 * taskIn 表是否存在未处理节点 根据nodeCode判断
	 * @param registNo
	 * @param nodeCodeList
	 * @return
	 * @author 
	 */
	public Boolean existTaskInByNodeCode(String registNo,String nodeCode);
	/**
	 * 通过任务号和节点查工作流表
	 * @param nodeCode
	 * @param flowId
	 * @return
	 */
	public PrpLWfTaskVo findprplwftaskVoByNodeCodeAndflowId(String nodeCode,String flowId);
	
    
    /**
     * 恢复被挂起的工作流
     * <pre></pre>
     * @param registNo
     * @param userCode
     * @modified:
     * ☆zhujunde(2017年6月19日 下午6:02:48): <br>
     */
    /*public void recoverFlow(String registNo, String userCode);*/
	
	/**
     * 
     * 查询in表数据，根据流入时间排序
     * @param registNo
     * @param handlerIdKey
     * @param subNodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年6月15日 下午3:38:54): <br>
     */
    public List<PrpLWfTaskVo> findInTask(String registNo,String handlerIdKey,String subNodeCode);
    
    /**
     * 
     * <pre>过滤已处理理算任务</pre>
     * @param prpLCompensatesVos
     * @return
     * @modified:
     * ☆LinYi(2017年7月20日 上午11:52:41): <br>
     */
    public List<PrpLCompensateVo> getPrplComepensate(List<PrpLCompensateVo> prpLCompensatesVos);
    
    /**
     * 通过compensateNo查询in表数据，根据流入时间排序
     * <pre></pre>
     * @param registNo
     * @param compensateNo
     * @param subNodeCode
     * @return
     * @modified:
     * ☆LinYi(2017年8月2日 下午3:44:31): <br>
     */
    List<PrpLWfTaskVo> findInTaskbyCompensateNo(String registNo,String compensateNo,String subNodeCode);
    /**
     * 根据taskid更新In表的标志位
     * <pre></pre>
     * @param taskId
     * @param flag
     * @return
     * @modified:
     * ☆WLL(2017年8月28日 下午4:22:29): <br>
     */
    public PrpLWfTaskVo updateTaskInFlag(Double taskId,String flag) throws Exception;
    
    /**
     * 更新工作流表
     * <pre></pre>
     * @param prpLWfTaskVo
     * @return
     * @throws Exception
     * @modified:
     * ☆LinYi(2017年9月19日 下午5:26:48): <br>
     */
    public void updateTaskByFlowId(PrpLWfTaskVo prpLWfTaskVo) throws Exception;

    /**
     * 更新taskin表
     * @param prpLWfTaskVo
     */
    public void updateTaskIn(PrpLWfTaskVo prpLWfTaskVo);
    
    /**
     * 更新taskout表
     * @param prpLWfTaskVo
     */
    public void updateTaskOut(PrpLWfTaskVo prpLWfTaskVo);
    
    /**
     * 更新工作流表IsMobileCase
     * <pre></pre>
     * @param prpLWfTaskVo
     * @throws Exception
     * @modified:
     * ☆zhujunde(2017年11月1日 上午10:32:51): <br>
     */
    public void updateIsMobileCaseByFlowId(List<PrpLWfTaskVo> prpLWfTaskVoList) throws Exception;
    
    /**
     * 查询in表数据，根据流入时间排序
     * <pre></pre>
     * @param registNo
     * @param handlerIdKey
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2017年11月1日 下午5:52:53): <br>
     */
    public List<PrpLWfTaskVo> findInTaskByOther(String registNo,String handlerIdKey,String nodeCode);
    /**
     * 通过flowId和节点查询out表或in表（flag=1表示查in表，flag=2表示查out表）
     * @param flowId
     * @param nodeCode
     * @param flag
     * @return
     */
    public List<PrpLWfTaskVo> findPrpLWfTaskByFlowIdAndnodeCode(String flowId,String flag,List<String> nodeCodes);
    /**
     * 通过registNo和节点查询out表或in表（flag=1表示查in表，flag=2表示查out表）
     * @param flowId
     * @param nodeCode
     * @param flag
     * @return
     */
    public List<PrpLWfTaskVo> findPrpLWfTaskByregistNoAndnodeCode(String registNo,String flag,List<String> nodeCodes);
    
    /**
     * 查勘工作流回滚
     * <pre></pre>
     * @param vo
     * @param flowId
     * @modified:
     * ☆zhujunde(2018年3月20日 上午10:46:24): <br>
     */
    public void rollBackFlow(PrpLScheduleTaskVo vo,String flowId);
    /**
     * 查勘回滚PrpLWfMain，PrpLWfNode表
     * <pre></pre>
     * @param vo
     * @param flowId
     * @modified:
     * ☆zhujunde(2018年3月20日 上午10:46:33): <br>
     */
    public void rollBackNodeAndLWfMain(PrpLScheduleTaskVo vo,String flowId);
    /**
     * 查询节点生成记录信息(按时间倒序，取第一条记录)
     * @param nodeCode
     * @param registNo
     * @return
     */
    public PrpLWfTaskVo findOutWfTaskVo(String nodeCode,String registNo);
    
    /**
     *根据报案号，工作流节点查询in表
     * <pre></pre>
     * @param registNo
     * @param compensateNo
     * @param nodeCode
     * @return
     * @modified:
     * (2018年2月2日 上午10:20:54): <br>
     */
    public List<PrpLWfTaskVo> findTaskInVo(String registNo,String compensateNo,String nodeCode);
    
    /**
     *根据报案号，工作流节点查询Out表
     * <pre></pre>
     * @param registNo
     * @param compensateNo
     * @param nodeCode
     * @return
     *(2018年2月2日 上午10:20:54): <br>
     */
    public List<PrpLWfTaskVo> findTaskOutVo(String registNo,String nodeCode);
        

    /**
     * 退回理算
     * <pre></pre>
     * @param wfTaskVo
     * @param submitVos
     * @param sysUserVo
     * @return
     * @modified:
     * ☆zhujunde(2018年1月24日 下午5:17:37): <br>
     */
    public List<PrpLWfTaskVo> submitVclaimLevelByIlog(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo);
    
    /**
     *退回单证或者定损
     * <pre></pre>
     * @param wfTaskVo
     * @param submitVos
     * @param sysUserVo
     * @modified:
     * ☆zhujunde(2018年1月25日 上午9:16:52): <br>
     */
    public void backToCertiOrDLCar(PrpLWfTaskVo wfTaskVo,List<WfTaskSubmitVo> submitVos,SysUserVo sysUserVo);
    /**
     *根据报案号，理算书号，工作流节点查询in表
     * <pre></pre>
     * @param registNo
     * @param compensateNo
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2018年2月2日 上午10:20:54): <br>
     */
    public List<PrpLWfTaskVo> findInTaskVo(String registNo,String compensateNo,String nodeCode);
    
    /**
     *根据报案号，理算书号，工作流节点查询Out表
     * <pre></pre>
     * @param registNo
     * @param compensateNo
     * @param nodeCode
     * @return
     * @modified:
     * ☆zhujunde(2018年2月2日 上午10:20:54): <br>
     */
    public List<PrpLWfTaskVo> findOutTaskVo(String registNo,String compensateNo,String nodeCode);

    
    
    /**
     * 回滚当前节点为正在处理
     * <pre></pre>
     * @param taskVo
     * @return
     * @modified:
     * ☆zhujunde(2018年4月18日 下午4:59:57): <br>
     */
    public PrpLWfTaskVo rollBackTaskByEndVo(PrpLWfTaskVo taskVo);
    
    /**
     * 删除节点
     * <pre></pre>
     * @param taskVo
     * @return
     * @modified:
     * ☆zhujunde(2018年4月18日 下午4:13:42): <br>
     */
    public PrpLWfTaskVo deteleTaskVo(PrpLWfTaskVo taskVo);
    /**
     * 根据报案号查询车辆与财产核损的最后一个完成任务
     * @param registNo
     * @return
     */
    public PrpLWfTaskVo findLastVlossTask(String registNo);
    /**
     * 根据OutTime查询PrpLWfTaskVo
     * <pre></pre>
     * @param registNo
     * @param claimNo
     * @param subNodeCode
     * @param handlerStatus
     * @return
     * @modified:
     * ☆zhujunde(2018年6月27日 上午10:09:39): <br>
     */
    public List<PrpLWfTaskVo> queryTaskByAnyOrderOutTime(String registNo,String claimNo,String subNodeCode,String handlerStatus);
    
    /**
     * 旧理赔重开生成工作流
     * @param registNo
     * @return
     */
    public Boolean generateFlow(String registNo);
 
    /**
     * 更新工作流级别
     * @param registNo
     * @return
     */
    public Boolean generateFlowLevel(String registNo);
    
    /**
     * 追偿发起
     * @param 
     * @param 
     * @return
     */
    public BigDecimal recPayLaunch(PrpLWfTaskVo prpLWfTaskVo);
    
    /**
     * 根据registNo，upperTaskId，subNodeCode查询工作流
     * @param registNo
     * @param upperTaskId
     * @param subNodeCode
     * @return
     */
    public List<PrpLWfTaskVo> findAllTaskVo(String registNo, BigDecimal upperTaskId,String subNodeCode);
    
    /**
	 * 是否存在该节点
	 *  workStatus 0 表示查询 PrpLWfTaskIn表  ，1 表示查询 已处理的数据 PrpLWfTaskout表 ,"" 都需要查询
	 *  在业务冲销前检查时调用，屏蔽移交节点
	 * ☆xiaohuyao(2018年12月5日 晚上22:54:12): <br>
	 */
	public Boolean existTaskByNodeCodeYJ(String registNo,FlowNode nodeCode,String taskInKey,String workStatus);

	/**
	 * 是否存在除单证之外的未处理完的节点
	 * <pre></pre>
	 * @param ristCode
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆yzy(2019年3月5日 下午4:07:31): <br>
	 */
	public Boolean existTaskIn(String riskCode,String registNo);
	
	/**
	 * 更新PrpLWfTaskUserInfoVo
	 * <pre></pre>
	 * @param fTaskUserInfoVo
	 * @modified:
	 * ☆zhujunde(2019年4月8日 下午3:10:36): <br>
	 */
	public void updateTaskUserInfo(PrpLWfTaskUserInfoVo fTaskUserInfoVo);
	
	/**
	 * 查询PrpLWfTaskUserInfoVo
	 * <pre></pre>
	 * @param flowId
	 * @return
	 * @modified:
	 * ☆zhujunde(2019年4月8日 下午3:11:24): <br>
	 */
	public List<PrpLWfTaskUserInfoVo> queryTaskUserInfo(String flowId);
	/**
	 * PrpLWfTaskOut 点击流程图重开赔案任务id排序位置
	 * @param wfTaskVo
	 * @return
	 */
	public Integer findTaskReOpenCount(PrpLWfTaskVo wfTaskVo);

	/**
	 * <pre></pre>
	 * @param taskId
	 * @return 
	 * @modified:
	 * ☆XiaoHuYao(2019年8月29日 下午4:34:53): <br>
	 */
	public PrpLWfTaskVo findoutTaskVoByTaskId(Double taskId);

	/**
	 *根据报案号、子节点、业务主表id查询是有总公司已经处理或者退回的节点
	 * @param registNo
	 * @param handlerIdKey
	 * @param subnodecodes
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月29日 下午4:51:59): <br>
	 */
	public Boolean findOutTaskVos(Map<String,Object> params);
	/**
	 * 
	 * @param nodeCode
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	public PrpLWfTaskVo findOutWfTaskVo(String nodeCode, String registNo,String riskCode);
	
}
