package ins.sino.claimcar.claim.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.vo.CompensateActionVo;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLChargeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrplcomcontextVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;

import java.util.List;
import java.util.Map;

public interface CompensateHandleService {

	public abstract PrpLCompensateVo saveCompensateEdit(Double flowTaskId,PrpLCompensateVo prpLCompensate,List<PrpLLossItemVo> prpLLossItemVoList,
											List<PrpLLossPropVo> prpLLossPropVoList,List<PrpLLossPropVo> otherLossList,
											List<PrpLLossPersonVo> prpLLossPersonVoList,List<PrpLChargeVo> prpLChargeVoList,
											List<PrpLPaymentVo> prpLPaymentVoList,List<PrpLClaimDeductVo> claimDeductVoList,
											List<PrpLPlatLockVo> platLockVoList,SysUserVo userVo,List<PrpLCheckDutyVo> prpLCheckDutyVoList) throws Exception;

	/**
	 * 校验代位案件是否锁定对方
	 * @throws Exception 
	 */
	public abstract List<PrpLPlatLockVo> organizeRecoveryList(PrpLRegistVo registVo,PrpLCMainVo prpLcMainVo,PrpLClaimVo claimVo,
																PrpLCompensateVo compVo,SysUserVo userVo);

	public CompensateActionVo compensateEdit(Double flowTaskId,SysUserVo userVo) throws Exception;
	
	public PrpLLossItemVo processLossCarVoList(PrpLDlossCarMainVo carMainVo,
			List<PrpLCItemKindVo> itemKindVoList, String kindCode, String flag);
	
	public List<PrpLLossPropVo> processLossPropVoList(List<PrpLdlossPropMainVo> reLossPropMainVos,
			List<PrpLCItemKindVo> itemKindVoList, String flag);
	
	public List<PrpLLossPersonVo> processLossPersonVoList(List<PrpLDlossPersTraceVo> lossPersTraceList, String flag);

	public abstract CompensateVo compensateGetAllRate(PrpLCompensateVo prpLCompensate,List<PrpLClaimDeductVo> claimDeductVoList,
			List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,List<PrpLLossPersonVo> prpLLossPersonVoList);
	
	public CompensateVo calculateBI(PrpLCompensateVo prpLCompensate,List<PrpLClaimDeductVo> claimDeductVoList,
			List<PrpLLossItemVo> prpLLossItemVoList,List<PrpLLossPropVo> prpLLossPropVoList,
			List<PrpLLossPropVo> otherLossList,List<PrpLLossPersonVo> prpLLossPersonVoList);
	
	public List<PrpLLossItemVo> getCarLossInfo(PrpLCompensateVo compensateVo,List<PrpLCheckDutyVo> checkDutyList,SysUserVo userVo);
	
	/**
	 * 单证校验代位案件是否锁定对方
	 * @throws Exception 
	 */
	public abstract List<PrpLPlatLockVo> organizeRecoveryListByCerti(PrpLRegistVo registVo,PrpLCMainVo prpLcMainVo,PrpLClaimVo claimVo,
																SysUserVo userVo);
	
	/**
	 * 同步 被保险人电话
	 * <pre></pre>
	 * @param registNo
	 * @param insuredPhone
	 * @modified:
	 * *牛强(2017年6月21日 下午5:12:12): <br>
	 */
	public void syncInsuredPhone(String registNo,String insuredPhone);
	/**
	 * 保存或更新反洗钱特征表
	 * @param vo
	 */
	public void saveOrUpdatePrplcomcontext(PrplcomcontextVo vo)throws Exception;
	/**
	 * 通过计算书号查反洗钱特征表
	 * @return
	 */
	public PrplcomcontextVo findPrplcomcontextByCompensateNo(String compensateNo,String type);
	
	/**
	 * 通过Id查询反洗钱特征表
	 * @param id
	 * @return
	 */
	public PrplcomcontextVo findPrplcomcontextById(Long id);

	/**
	 * 判断代位求偿是否锁定
	 * <pre></pre>
	 * @param subrogationMainVo
	 * @param policyNo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆LinYi(2018年4月19日 下午8:41:16): <br>
	 */
    boolean subrogationIsLock(PrpLSubrogationMainVo subrogationMainVo,String policyNo) throws Exception;
    
    /**
     * 判断非被保险人支付例外是否为空
     * @param prpLPaymentVoList
     * @return
     */
	public  boolean saveBeforeCheck(
			List<PrpLPaymentVo> prpLPaymentVoList);
	/**
	 * 
	 * @param prpLPaymentVoList
	 * @param prpLCompensate
	 * @return
	 */
	public  boolean isAmontEqual(List<PrpLPaymentVo> prpLPaymentVoList,
			PrpLCompensateVo prpLCompensate);

	/**
	 * <pre>校验数据</pre>
	 * @param itemMap
	 * @return
	 * @modified:
	 * ☆XiaoHuYao(2019年1月26日 下午2:25:22): <br>
	 */
	public void validData(Map<String,Object> itemMap) throws Exception;
	
	/**
	 * 更新理算表
	 * <pre></pre>
	 * @param vo
	 * @modified:
	 * ☆zhujunde(2019年4月16日 下午12:00:47): <br>
	 */
	public void updatePrpLCompensate(PrpLCompensateVo vo);
}
