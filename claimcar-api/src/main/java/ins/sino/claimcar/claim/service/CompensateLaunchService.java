package ins.sino.claimcar.claim.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimResultVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLKindAmtSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.SubmitNextVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;

import java.util.List;

public interface CompensateLaunchService {

	/** 交强险种前缀*/
	public static final String PREFIX_CI = "11";
	/** 商业险种前缀*/
	public static final String PREFIX_BI = "12";

	/**
	 * 理算任务发起验证
	 * 
	 * @param registNo
	 * @return
	 */
	public abstract String validCheckComp(String registNo,String chkFlag);

	/**
	 * 理算自动发起校验
	 * @param registNo
	 * @return
	 */
	public abstract String validCheckCompAuto(String registNo);

	/**
	 * 查询发起的理算任务
	 */
	public abstract Page<PrpLClaimResultVo> findCompensateLaunchByHql(PrpLClaimVo prpLClaimVo,int start,int length);

	/**
	 * 查询可发起冲销的理算任务
	 */
	public abstract ResultPage<PrpLCompensateVo> findAllCompForWriteOff(PrpLCompensateVo compVo,int start,int length,SysUserVo userVo);

	/**
	 * 根据原计算书号查询是否存在对应的冲销计算书
	 * <pre></pre>
	 * @param compNo 原计算书号
	 * @return
	 * @modified:
	 * ☆WLL(2016年9月6日 上午10:38:20): <br>
	 */
	public abstract PrpLCompensateVo findWriteOffCompensateVoByOriCompNo(String compNo);
	
	public String submitPrePayWriteOff(PrpLCompensateVo compensateVo,List<PrpLLossItemVo> lossItemVos,List<PrpLLossPropVo> lossPropVos,
			List<PrpLLossPersonVo> lossPersons,List<PrpLChargeVo> charges,List<PrpLPaymentVo> payments,
			List<PrpLKindAmtSummaryVo> prpLKindAmtSummaryVoList,SubmitNextVo submitNextVo,SysUserVo userVo) throws Exception;
	
	public void getSubmitCompe(List<PrpLCompensateVo> prpLCompensateVos,String bcFlag,String handlerUser,SysUserVo userVo);
	
	public PrpLWfTaskVo getSubmit(PrpLClaimVo prpLClaimVo,String compNo,FlowNode nextNode,FlowNode currentNode,SysUserVo userVo);
	
	public List<PrpLPlatLockVo> organizeRecoveryList(PrpLCompensateVo compVo);

}
