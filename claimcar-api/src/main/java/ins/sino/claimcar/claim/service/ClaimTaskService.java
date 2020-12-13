/**
 * 
 */
package ins.sino.claimcar.claim.service;

import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.EstimatedLossAmountInfoVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrplTestinfoMainVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @author CMQ
 */
public interface ClaimTaskService {

	/**
	 * 根据报案号查找立案列表
	 * @param registNo
	 * @return
	 */
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo);
	
	/*根据报案号查立案险别金融表*/
	public List<PrpLClaimKindHisVo> findPrpLClaimKindHisByRegistNo(String registNo);
	
	/**
	 * <pre>根据条件查询立案</pre>
	 * @param registNo
	 * @param policyNo
	 * @param validFlag--是否有效
	 * @return
	 * @modified: ☆ZhouYanBin(2016年5月13日 下午2:30:09): <br>
	 */
	public List<PrpLClaimVo> findprpLClaimVoListByRegistAndPolicyNo(
			String registNo, String policyNo, String validFlag);

	/**
	 * 根据立案号查找立案信息
	 * @param claimNo
	 * @return
	 */
	public PrpLClaimVo findClaimVoByClaimNo(String claimNo);

	public void submitClaim(String registNo, String flowId) throws ParseException ;
	


	/**
	 * Quartz 调用的自动立案Job
	 * @return
	 * @modified: ☆LiuPing(2016年5月12日 ): <br>
	 */
	public void claimForceJob(String registNo, String policyNo, String flowId) throws ParseException ;
	
	/**
	 * 立案送平台
	 * @param claimVo
	 * @modified: ☆Luwei(2017年2月9日 上午10:39:31): <br>
	 */
	public void sendClaimToPlatform(PrpLClaimVo claimVo);

	/**
	 * <pre>15天案均上浮，刷新车财估损</pre>
	 * @param registNo
	 * @modified: ☆ZhouYanBin(2016年5月18日 上午11:22:40): <br>
	 */
	public void updateClaimFeeForFifteen(PrpLRegistVo registVo);
	
	/**
	 * <pre>判断该案件的车物核损是否通过  用于案均上浮</pre>
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月17日 下午4:18:06): <br>
	 */
	public boolean adjustPropLossState(String registNo);
	
	/**
	 * 30天案均上浮 人伤估损翻倍
	 * <pre></pre>
	 * @param registVo
	 * @modified:
	 * ☆WLL(2016年8月17日 下午5:46:37): <br>
	 */
	public void updateClaimFeeForThirty(PrpLRegistVo registVo);
	
	/**
	 * 判断案件是否符合30天人伤上浮条件
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月17日 下午5:27:59): <br>
	 */
	public boolean adjustPersLossState(String registNo);
	
	/**
	 * 其他环境触发立案刷新
	 * @param registNo
	 * @param userCode 是谁触发的
	 * @param node 是哪个节点
	 * @modified:
	 */
	public void updateClaimFee(String registNo,String userCode,FlowNode node) throws Exception;

	/**
	 * 核赔-->结案回写立案标志
	 * @param claimVo
	 * @modified: ☆Luwei(2016年6月7日 上午9:22:38): <br>
	 */
	public void claimWirteBack(PrpLClaimVo claimVo);
	
	/**
	 * <pre>理算核赔，理算冲销核赔通过回写CompStatus</pre>
	 * @param PrpLClaimKindVo kindVo
	 * @param PrpLClaimKindFeeVo feeVo
	 * ☆Luwei(2016年8月29日 下午1:00:39): <br>
	 */
	public void wirteBackClaimKindAndFee(List<PrpLClaimKindVo> kindVo,List<PrpLClaimKindFeeVo> feeVo);
	
	/**
	 * 重开回写立案
	 */
	public void reOpenClaimWirteBack(String claimNo,FlowNode node,String endCaserCode);

	/**
	 * 理算核赔通过后刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年6月22日 ): <br>
	 */
	public void updateClaimAfterCompe(String compensateNo,String userCode,FlowNode node);
	
	/**
	 * 理算冲销核赔通过后0结案，刷新立案估损金额
	 * @param registNo
	 * @param userCode
	 * @param node
	 * @modified: ☆weilanlei(2016年12月1日 ): <br>
	 */
	public void updateClaimAfterCompeWfZero(String compensateNo,String userCode,FlowNode node);

	/**
	 * 历史出险信息 根据报案号获取立案案件信息表
	 * @param registNo
	 * @return
	 * @modified: ☆wei(2016年6月24日 ): <br>
	 */
	public List<PrpLClaimSummaryVo> findPrpLClaimSummaryByRegistNo(String registNo);
	
	public List<PrpLLawSuitVo> findPrpLLawSuitVoByRegistNo(String RegistNo);
	
	public PrpLClaimSummaryVo findPrpLClaimSummaryVoByRegistNo(String claimNo);
	
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo,String validFlag);
	
	public PrpLClaimVo getClaimVo(String registNo,String riskCode);
	
	/**
	 * 结案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	
	public void calcClaimKindHisByEndCase(String claimNo);
	
	/**
	 * 重开赔案刷新轨迹
	 * @param claimNo
	 * @return
	 * @modified: ☆LiuPing(2016年6月3日 ): <br>
	 */
	public void calcClaimKindHisByReOpen(String claimNo);
	
	public void cancleClaim(String claimNo,String validFlag,BigDecimal flowTaskId,SysUserVo userVo,String registNo,WfTaskSubmitVo submitVo);
	
	public void cancleClaim(BigDecimal flowTaskId,String userCode,SysUserVo userVo);
	
	public List<EstimatedLossAmountInfoVo> getClaimVoByEstimatedLossAmount(String comCode);
	
	public void cancleClaimForOther(String registNo,String userCode);
	
	public void recoverClaimForOther(String registNo,String userCode,BigDecimal flowTaskId);
	
	/**

	 * 未决案件最新估损金额上传[为了不影响保险公司出单业务及系统的稳定运行，除每日20：00至次日6:00外，其他时间该接口停止使用。]
	 * 查询出当天需要上传的立案信息
	 * @return List<PrpLClaimVo>
	 * @modified:
	 * ☆Luwei(2016年12月21日 上午11:21:47): <br>
	 */
	
	
	/*public void sendEstimatedByTimer();*/
	/**

	 * 通过报案号和车牌号码查询德联易控信息主表
	 * @param registNo
	 * @param licenseNo
	 * @param lossCarMainId--当时定损修改时，方便找到是在那条数据上进行的修改
	 * @param nodeFlag--节点名称，1-报案，2-查勘，3-定损
	 * @return
	 */
	public PrplTestinfoMainVo findPrplTestinfoMainByRegistNoAndLicenseNo(String registNo,String licenseNo,Long lossCarMainId,String nodeFlag);
	
	
	/**
	 * 请求德联易控接口
	 * @param registNo
	 * @param licenseNo
	 * @param lossFlag
	 * @throws Exception
	 */
	public void SendControlExpertInterface(String registNo,String licenseNo,SysUserVo userVo,String lossCarMainId,String nodeFlag)throws Exception;
	/**
	 * 通过报案号和节点名称查询德联易控信息主表
	 * @param registNo
	 * @param nodeFlag
	 * @return
	 */
	public List<PrplTestinfoMainVo> findPrplTestinfoMainByRegistNoAndNodeFlag(String registNo,String nodeFlag);
}
