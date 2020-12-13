package ins.sino.claimcar.pay.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.List;
import java.util.Map;

public interface PadPayService {

	/**初始化垫付 基本信息
	 * @param claimVoList
	 * @return
	 * @modified: ☆Luwei(2016年3月4日 下午6:34:08): <br>
	 */
	public PrpLCMainVo initCMainInfo(String registNo);

	/** 初始化垫付任务申请登记
	 * @param registNo
	 * @param claimNo
	 * @param policyNo
	 * @return
	 * @modified: ☆Luwei(2016年3月7日 上午11:54:05): <br>
	 */
	public PrpLPadPayMainVo initPadPay(String registNo,String claimNo,PrpLWfTaskVo wfTaskVo);

	/** 获取人伤损失信息 --> 姓名下拉框map **/
	public Map<String,String> getPersonNameMap(String registNo,String claimNo);

	/** 报案信息
	 * @param registNo
	 * @modified: ☆Luwei(2016年3月7日 下午12:08:16): <br>
	 */
	public PrpLRegistVo getRegistVo(String registNo);

	/** 根据报案号获取立案信息
	 *  @param registNo */
	public PrpLClaimVo getClaimNo(String claimNo);

	/** 获取checkDuty表信息  **/
	public PrpLCheckDutyVo queryCheckDuty(String registNo);

	/**  获取号牌底色 **/
	public String getColorCode(String registNo);

	public Map<String,String> getLicenseNo(String registNo);

	/**
	 * 获取收款人
	 * @param registNo
	 * @modified: ☆Luwei(2016年3月10日 下午3:30:27): <br>
	 */
	public Map<String,String> getCustom(String registNo);

	// 获取收款放账号和开户银行
	public Map<String,String> getPayCustom(Long payCusId);

	/** 获取人伤姓名  */
	public Map<String,String> getPersonName(Long id);

	/** 垫付保存
	 * @param padPayMainVo
	 * @modified: ☆Luwei(2016年3月9日 下午12:59:07): <br>
	 */
	public PrpLPadPayMainVo save(PrpLPadPayMainVo padPayMainVo,String userCode,String comCode);

	/**
	 * 生成垫付节点(发起垫付任务)
	 * @param registNo,claimNo,comCode,userCode
	 * @return String
	 */
	public String LaunchPadPayTask(String registNo,String claimNo,String comCode,String userCode);

	/**
	 * ---垫付处理提交
	 * @param taskId-工作流任务id
	 * @param padId--垫付主表id
	 * @param level--提交的核赔级别
	 */
	public void submitPadPay(String taskId,Long padId,Integer level,String comCode,String userCode,String nextUserCode,String nextComCode)throws Exception;

	/**
	 * 获取垫付主表信息
	 * @param registNo
	 * @param claimNo
	 * @return
	 */
	public PrpLPadPayMainVo getPadPayInfo(String registNo,String claimNo);

	/**	@param id 
	 * @modified: ☆Luwei(2016年3月10日 上午9:42:05):  <br> 
	 * **/
	public void dropPerByPK(Long id);

	/**	 根据报案号查询垫付主表,@param registNo,@return **/
	public List<PrpLPadPayMainVo> findPadPayMainByRegistNo(String registNo);

	/** 
	 * 垫付申请校验 ,
	 * @param registNo
	 **/
	public String padPayTaskVlaid(String registNo);

	/**
	 * <pre>垫付注销</pre>
	 * @param taskId
	 * @param user
	 */
	public void padPayCancel(String taskId,SysUserVo user) throws Exception;
	
	/**
	 * 根据报案号 和计算书查询
	 * @param registNo
	 * @param compensateNo
	 * @return
	 */
	public PrpLPadPayMainVo findPadPayMainByComp(String registNo,String compensateNo);
	
	
	public PrpLWfTaskVo findTaskIn(String registNo,String handleIdKey,FlowNode nextNode)throws Exception;

	/**
	 * 根据计算书查询
	 * @param compensateNo
	 * @return
	 */
	public PrpLPadPayMainVo findPadPayMainByCompNo(String compensateNo);
	
	/**
	 * 
	 * 结算单号查询PrpLPadPayPersonVo
	 * @param settleNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月20日 下午5:07:48): <br>
	 */
	public List<PrpLPadPayPersonVo> findPrpLPadPayPersonBySettleNo(String settleNo);
	
	/**
	 * 结算单号查询PrpLPadPayMainVo
	 * @param settleNo
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年6月20日 下午7:15:13): <br>
	 */
	public List<PrpLPadPayMainVo> findPadPayMainBySettleNo(String settleNo);
	/**
	 * 垫付提交后台校验收款人账户
	 * @param prpPrePayVos
	 * @return
	 */
	public boolean saveBeforeCheck(List<PrpLPadPayPersonVo> prpLPadPayPersonVos);
	

    /**
     * 获取垫付主表信息
     * <pre></pre>
     * @param claimNo
     * @return
     * @modified:
     * ☆zhujunde(2019年2月14日 下午5:00:37): <br>
     */
	public PrpLPadPayMainVo getPadPayInfoByClaimNo(String claimNo);

	/**
	 * 获取医疗费有责垫付限额
	 * @param registNo
	 * @return
	 */
	public double getPadLimitAmount(String registNo);
}
