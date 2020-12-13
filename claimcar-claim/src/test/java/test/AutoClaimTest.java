/******************************************************************************
 * CREATETIME : 2016年4月22日 上午11:21:52
 ******************************************************************************/
package test;

import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.services.ClaimServiceImpl;
import ins.sino.claimcar.claim.services.spring.CompensateTaskServiceImpl;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月22日
 */
public class AutoClaimTest extends BaseTestTrueRollback {
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private ClaimService claimService;
	
	@Autowired
	private LossChargeService lossChargeService;
	
	@Autowired
	VerifyClaimService verifyClaimService;
	
	@Autowired
	CompensateTaskServiceImpl sss;
	
	@Autowired
	ClaimServiceImpl claimServiceImpl;

	@Autowired
	ClaimTaskService claimTaskService;
	
	@Test
	public void test() {
		String registNo = "4000000201612080000007";
		String flowId = "F00000020161208000007";
		// verifyClaimService.writeBackIsCompdeDuct(registNo, compeNo, "vc_adopt", "012345678");
		// verifyClaimService.writeBackCompesate(compeNo,2,"vc_adopt","0","012345678");

		// claimServiceImpl.autoClaim("4000000201611010000627","211010020151206010243"); 4000000201612060000541 4000000201612080000012 4000000201612060000362
		// claimTaskService.updateClaimFee("4000000201612070000898","0000000000",FlowNode.PLoss);
		// claimServiceImpl.updateClaimAfterCompe(compensateNo,userCode,FlowNode.VClaim);
		// claimTaskService.submitClaim(registNo,flowId);
		// claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);

		// claimService.saveClaimSummary(claimMainVo);
		// claimService.updateClaimAfterCompe("71001002016110100000172","0000000000",FlowNode.VClaim);

	}
	
}
