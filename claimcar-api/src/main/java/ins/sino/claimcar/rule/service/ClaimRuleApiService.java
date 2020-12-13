/******************************************************************************
* CREATETIME : 2016年2月20日 下午2:46:11
******************************************************************************/
package ins.sino.claimcar.rule.service;

import ins.sino.claimcar.rule.vo.LossPropToVerifyRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimCancelRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.rule.vo.VerifyPersonRuleVo;




/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年2月20日
 */
public interface ClaimRuleApiService {

	/**
	 * 车辆定损提交核损规则
	 * @return
	 * @modified: ☆LiuPing(2016年2月25日 ): <br>
	 */
	public VerifyLossRuleVo lossCarToVerifyRule(VerifyLossRuleVo ruleVo);

	/**
	 * 车辆定损提交核价规则
	 * @return
	 * @modified: ☆LiuPing(2016年2月25日 ): <br>
	 */
	public VerifyLossRuleVo lossCarToPriceRule(VerifyLossRuleVo ruleVo);

	/**
	 * 人伤跟踪审核规则
	 * @return 需要第几级人伤跟踪审核权限
	 * @modified: ☆LiuPing(2016年2月27日 ): <br>
	 */
	public VerifyPersonRuleVo lossPersonToVerify(VerifyPersonRuleVo ruleVo);

	/**
	 * 人伤费用审核
	 * @return 需要第几级人伤费用审核权限
	 * @modified: ☆LiuPing(2016年2月27日 ): <br>
	 */
	public VerifyPersonRuleVo lossPersonToPrice(VerifyPersonRuleVo ruleVo);

	/**
	 * 理算或预付提交核赔时规则
	 * @return 需要第几级人核赔岗位
	 * @modified: ☆LiuPing(2016年4月27日 ): <br>
	 */
	public VerifyClaimRuleVo compToVClaim(VerifyClaimRuleVo ruleVo);
	
	/**
	 * 理算或预付的交强险提交核赔规则
	 * @param ruleVo
	 * @return 需要第几级人核赔岗位
	 */
//	public VerifyClaimRuleVo compCIToVClaim(VerifyClaimRuleVo ruleVo);
	
	/**
	 * 立案注销拒赔时规则
	 * @return 需要第几级人核赔岗位
	 * @modified: ☆LiuPing(2016年4月27日 ): <br>
	 */
	public VerifyClaimCancelRuleVo claimCanCelToPriceRule(VerifyClaimCancelRuleVo ruleVo);
	
	/**
	 * 财产定损提交核损规则
	 * @param ruleVo
	 * @return
	 */
	public LossPropToVerifyRuleVo lossPropToVerifyRule(LossPropToVerifyRuleVo ruleVo);
}
