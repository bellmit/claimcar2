package ins.sino.claimcar.claim.service;

import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ClaimToReinsuranceService {

	/**
	 * 送再保未决数据 接口
	 * <pre></pre>
	 * @param claimVo
	 * @param kindHisVoList
	 * @param claimInterfaceLogVo
	 * @throws Exception
	 * @modified:
	 * *牛强(2017年3月30日 上午10:40:06): <br>
	 */
	public abstract void TransDataForClaimVo(PrpLClaimVo claimVo,
			List<PrpLClaimKindHisVo> kindHisVoList,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception;


	/**
	 * 送再保已决数据接口
	 * <pre></pre>
	 * 
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 * @modified: *牛强(2017年3月30日 上午10:40:45): <br>
	 */
	public abstract void TransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception;

	/**
	 * 结案 重开 拒赔 注销 送再保接口
	 * <pre></pre>
	 * @param businessType
	 * @param claimVo
	 * @param claimInterfaceLogVo
	 * @throws Exception
	 * @modified:
	 * *牛强(2017年3月30日 上午10:41:29): <br>
	 */
	public abstract void TransDataForReinsCaseVo(String businessType,
			PrpLClaimVo claimVo,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception;
	/**
	 * 公估费送再保
	 * <pre></pre>
	 * @param claimVo
	 * @param prpLCompensate
	 * @param assessorFeeVo
	 * @throws Exception
	 * @modified:
	 * *牛强(2017年1月16日 下午3:57:38): <br>
	 */
	public void asseFeeToReins(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLAssessorFeeVo assessorFeeVo) throws Exception;
	/**
	 * 公估费送再保对外接口
	 * 针对已结案公估费数据 按照  重开-已决-重开 步骤送再保
	 * <pre></pre>
	 * @param claimVo
	 * @param prpLCompensate
	 * @param assessorFeeVo
	 * @throws Exception
	 * @modified:
	 * *牛强(2017年3月30日 上午10:38:16): <br>
	 */
	public void asseFeeToReinsOut(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLAssessorFeeVo assessorFeeVo) throws Exception;
	
	/**
	 * 查勘费送再保对外接口
	 * 针对已结案查勘费数据 按照  重开-已决-重开 步骤送再保
	 * <pre></pre>
	 * @param claimVo
	 * @param prpLCompensate
	 * @param PrpLCheckFeeVo
	 * @throws Exception
	 * @modified:
	 * *yez(2019年8月9日 上午10:38:16): <br>
	 */
	public void checkFeeToReinsOut(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,PrpLCheckFeeVo checkFeeVo) throws Exception;
	
	/**
	 * vat回写送再保
	 * @param claimVo
	 * @param prpLCompensate
	 * @param curTime
	 * @param kindPaymentTaxDiffrentValueMap
	 * @param kindChargeTaxDiffrentValueMap
	 * @throws Exception
	 */
	public  void vatWriteBackTransDataForCompensateVo(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate,int curTime,Map<Long,BigDecimal> kindPrePayTaxDiffrentValueMap,Map<String,BigDecimal> kindPaymentTaxDiffrentValueMap,Map<String,BigDecimal> kindChargeTaxDiffrentValueMap) throws Exception;
	
	/**
	 * 送再保冲销数据接口
	 * <pre></pre>
	 * 
	 * @param claimVo
	 * @param prpLCompensate
	 * @throws Exception
	 *
	 */
	public  void transDataForWashTransaction(PrpLClaimVo claimVo,PrpLCompensateVo prpLCompensate) throws Exception;
	
}