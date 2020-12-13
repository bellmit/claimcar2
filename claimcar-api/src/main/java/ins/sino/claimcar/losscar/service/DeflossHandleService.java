package ins.sino.claimcar.losscar.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.vo.DeflossActionVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface DeflossHandleService {

	public DeflossActionVo prepareAddDefLoss(double flowTaskId,SysUserVo userVo,String sign);

	/**
	 * 接收定损任务
	 * ☆yangkun(2016年2月17日 下午4:20:19): <br>
	 */
	public PrpLDlossCarMainVo acceptDefloss(Double flowTaskId, String registNo,
			SysUserVo userVo);

	/**
	 * 定损保存
	 * <pre></pre>
	 * @param deflossVo
	 * @throws Exception 
	 * @modified:
	 * ☆yangkun(2016年1月11日 上午11:32:55): <br>
	 */
	public void save(PrpLDlossCarMainVo lossCarMainVo,
			List<PrpLDlossChargeVo> lossChargeVos, PrpLClaimTextVo claimTextVo,
			PrpLSubrogationMainVo subrogationMainVo, PrpLWfTaskVo taskVo,
			SysUserVo userVo) throws Exception;

	/**
	 * 判定定损进入核价or 核损
	 * <pre></pre>
	 * @param lossCarMainVo
	 * @modified:
	 * ☆yangkun(2016年1月16日 下午5:02:29): <br>
	 */
	public void getDeflossNextCode(PrpLDlossCarMainVo lossCarMainVo);

	public List<PrpLCItemKindVo> initSubRisks(String registNo,
			String[] kindCodes);

	public List<PrpLDlossCarSubRiskVo> loadSubRisk(String[] kindCodes,
			String registNo);

	public List<PrpLDlossChargeVo> initChargeType(String[] chargeTypes);

	/**
	 * 核价初始化
	 * ☆yangkun(2016年1月6日 下午1:25:40): <br>
	 */
	public DeflossActionVo prepareAddVerifyPrice(Double flowTaskId,SysUserVo userVo);

	public DeflossActionVo prepareAddVerifyLoss(PrpLWfTaskVo taskVo,SysUserVo userVo);

	/**
	 * 
	 * 校验定损信息
	 * @modified:  定损 修改定损 追加定损 复检
	 * ☆yangkun(2016年1月10日 下午3:08:08): <br>
	 */
	public String validDefloss(PrpLDlossCarMainVo lossCarMainVo,
			PrpLDlossCarInfoVo carInfoVo,
			PrpLSubrogationMainVo subrogationMain, PrpLWfTaskVo taskVo);

	/**
	 * 是否赔付过 false 已赔付，true 未理算
	 * @param lossState
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月9日 下午3:58:56): <br>
	 */
	public boolean checkLossState(String lossState);

	/**
	 * 标的车判断
	 * 判定是否是互碰自赔案件
	 * 1 多车事故
	 * 2 仅涉及车辆损失（包括车上财产和车上货物）、不涉及人员伤亡和车外财产损失，各方损失金额均在2000元以内
	 * 不存在三者车的车和财定损 或则0提
	 * 3 各方均有责任
	 * 5 校验各方险别 
	 * ☆yangkun(2016年3月8日 下午9:27:42): <br>
	 */
	public String isClaimSelf(PrpLDlossCarMainVo lossCarMainVo,
			List<PrpLCheckDutyVo> checkDutyList, PrpLRegistVo registVo);

	/**
	 * 互碰自赔案件 三者车校验
	 * @param lossCarMainVo
	 * @param checkDutyList
	 * @param registVo
	 * @return
	 * @modified:
	 * ☆YangKun(2016年7月15日 上午10:29:35): <br>
	 */
	public String thirdCarClaimSelf(PrpLDlossCarMainVo lossCarMainVo);

	/**
	 * 核价保存
	 * ☆yangkun(2016年2月16日 下午3:39:16): <br>
	 */
	public void saveVerifyPrice(PrpLDlossCarMainVo lossCarMainVo,
			PrpLClaimTextVo claimTextVo, PrpLWfTaskVo taskVo, SysUserVo userVo);

	/**
	 * 核损保存
	 * ☆yangkun(2016年2月16日 下午3:40:12): <br>
	 */
	public void saveVerifyLoss(PrpLDlossCarMainVo lossCarMainVo,
			List<PrpLDlossChargeVo> lossChargeVos, PrpLClaimTextVo claimTextVo,
			PrpLWfTaskVo taskVo, SysUserVo userVo);

	/**
	 * 提交工作流
	 * <pre></pre>
	 * @param nextVo
	 * @return
	 * @throws Exception 
	 * @modified:
	 * ☆yangkun(2016年1月16日 下午8:28:34): <br>
	 */
	public List<PrpLWfTaskVo> submitNextNode(
			PrpLDlossCarMainVo lossCarMainVo, SubmitNextVo nextVo)
			throws Exception;

	/**
	 * 定损指标表 更新
	 * 1 首次定损 金额 时间 机构 定损员  
	 * 2 首次核价通过 金额 时间 机构 核损员
	 * 3 首次核损通过 金额 时间 机构 核损员
	 * 4 每次定损提交 记录(配件+辅料)金额 和总数量
	 * ☆yangkun(2016年1月22日 下午9:11:34): <br>
	 */
	public void saveLossIndex(PrpLDlossCarMainVo lossCarMainVo,
			SubmitNextVo nextVo);

	public SubmitNextVo organizeNextVo(Long lossMainId, String flowTaskId, String auditStatus,String isMobileCheck,SysUserVo userVo ,String isSubmitHeadOffice);

	/**
	 * 核价 核损提交上级或退回下级路径
	 * @param lossCarMainVo
	 * @param taskVo
	 * @param auditStatus
	 * @param verifyType 核价 "VPCar_LV"  核损 "VLCar_LV"
	 * @return
	 * @modified:
	 * ☆YangKun(2016年6月17日 上午9:36:32): <br>
	 */
	public Map<String, String> findVerifyLevelPath(
			PrpLDlossCarMainVo lossCarMainVo, PrpLWfTaskVo taskVo,
			String auditStatus, String verifyType);

	/**
	 * 查询指定定损任务的定损轨迹
	 */
	public Map<String, List> showDeflossHis(Long defLossMainId);

	public DeflossActionVo deflossView(String lossCarMainId);

	/**
	 * 刷新费用和附加险
	 * @param lossMainId
	 * @modified:
	 * ☆YangKun(2016年7月15日 下午7:43:42): <br>
	 */
	public DeflossActionVo refreshFee(Long lossMainId, String operateType);

	/**
	 * 复勘和复检默认原处理人，并可以选择同机构有权限的人员
	 * @param nextVo
	 * @param auditStatus
	 * @return
	 */
	public TreeMap<String,String> organizeUserMap(SubmitNextVo nextVo,String auditStatus);
	/**
	 * 用于单证查询车辆定损金额
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * *牛强(2017年2月15日 上午10:45:51): <br>
	 */
	public  List<PrpLDlossCarMainVo> findLossCarMainByRegistNo(String registNo);
	/**
	 * 用于单证查询人伤定损金额
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * *牛强(2017年2月15日 上午10:52:16): <br>
	 */
	public List<PrpLDlossPersTraceVo> findPersTraceListByRegistNo(String registNo);

	/**
	 * PrpLDlossCarMainVo
	 * <pre></pre>
	 * 更新发票金额
	 * @param PrpLDlossCarMainLsit
	 * @modified: *牛强(2017年2月16日 上午9:14:48): <br>
	 */
	public void updateLossCarMainInvoiceFee(List<PrpLDlossCarMainVo> PrpLDlossCarMainLsit);
	/**
	 * 根据报案号 更新发票金额
	 * <pre></pre>
	 * @param Registno
	 * @param invoiceFee
	 * @modified:
	 * *牛强(2017年2月15日 下午2:23:58): <br>
	 */
	public void updatePersTraceInvoiceFee(List<PrpLDlossPersTraceVo> PrpLDlossPersTraceList);

	/**
	 * 查询该员工是否具有自动审核的权限；
	 * 必传参数：usercode,node，如果money为空则不比较
	 * @param prpLAutoVerifyVo
	 * @return
	 */
	public Boolean isAutoVerifyUser(PrpLAutoVerifyVo prpLAutoVerifyVo);
	
	/**
	 * 判断是否存在标的车的定损任务
	 * @param registNo
	 * @return
	 */
	public Boolean existTargetCar(String registNo);
	
	/**
	 * 判断两次核价金额相等情况  修改页面背景颜色
	 * <pre></pre>
	 * @param deflossActionVo
	 * @modified:
	 * *牛强(2017年5月24日 下午3:58:43): <br>
	 */
	public DeflossActionVo isVerifyLossChanged(DeflossActionVo deflossActionVo);
	
	/**
	 * 查询人伤跟踪信息主表
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆LinYi(2017年8月15日 下午2:21:05): <br>
	 */
	public List<PrpLDlossPersTraceMainVo> findlossPersTraceMainByRegistNo(String registNo);
	
	/**
	 * 定损照片是否审核通过送快赔系统，checkStatus：0-不通过，1-审核通过
	 * @param registNo
	 * @param lossMainId
	 * @param photoStatus
	 * @param checkStatus
	 */
	public void photoVerifyToHNQC(String registNo,String lossMainId,String photoStatus,String checkStatus,String offLineHanding,String dataType,SysUserVo userVo) throws Exception;
	
	/**
	 * 全部车辆核损通过后送河南快赔
	 * @param registNo
	 */
	public void sendHNQC(String registNo,SysUserVo userVo);

	/**
	 * 注销定损任务，underwriteflag改为6
	 * @param id
	 */
	public void cancelCar(String id);
	public VerifyLossRuleVo organizeVerifyLossRule(PrpLDlossCarMainVo lossMainVo,String veriType);

	/**
	 * 查看是否精友2代案子
	 * @param reportTime
	 * @return
	 */
	public Boolean isJyTwo(Date reportTime);
	
	/**
	 * 根据报案号查询所有信息
	 * @param registNo
	 * @return
	 */
	public  List<PrpLDlossCarMainVo> findLossCarInfoByRegistNo(String registNo);
	
	/**
	 * 根据多个报案号查询所有信息
	 * @param registNos
	 * @return
	 */
	public List<PrpLDlossCarMainVo> findAllLossCarMainInfoByRegistNo(List<String> registNos);
	
	public void saveAcheckFeeTask(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo);

	/**
	 * 根据查勘费类型查找对应的定损主表
	 * @param taskType
	 * @param mainId
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年8月14日 上午9:11:27): <br>
	 */
	public Object findDlossMain(String taskType,Long mainId);

	/**
	 * 根据公估费类型添加人伤、车损、财损、查勘公估费任务
	 * @param mainVo
	 * @param userVo
	 * @param taskType
	 * @modified:
	 * ☆XiaoHuYao(2019年8月14日 上午10:30:22): <br>
	 */
	public void saveAssessorsTask(Object mainVo,SysUserVo userVo,String taskType);
	
}