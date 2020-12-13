/******************************************************************************
 * CREATETIME : 2016年4月22日 上午11:21:52
 ******************************************************************************/
package test;

import ins.sino.claimcar.job.service.ClaimAvgFloatService;
import ins.sino.claimcar.job.service.ClaimForceJobService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


/**
 * @author ★YangKun
 * @CreateTime 2016年4月22日
 */
public class ForceClaimTest extends BaseTestTrueRollback {
	@Autowired
	private PolicyViewService policyViewService;
	
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private ClaimForceJobService claimForceJobService;
	
	@Autowired
	private ClaimAvgFloatService claimAvgFloatService;
	
	@Test
	public void test() {
		claimForceJobService.doJob();
//		claimAvgFloatService.doFloatCarProp();
//		claimAvgFloatService.doFloatPers();
	}
	
}
