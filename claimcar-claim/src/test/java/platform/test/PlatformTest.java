/******************************************************************************
* CREATETIME : 2016年4月27日 上午10:15:55
******************************************************************************/
package platform.test;

import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.platform.service.SendCancelToPlatformService;
import ins.sino.claimcar.platform.service.SendCancelToSHPlatformService;
import ins.sino.claimcar.platform.service.SendClaimToPlatformService;
import ins.sino.claimcar.platform.service.SendClaimToSHPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToSHPlatformService;
import ins.sino.claimcar.platform.service.SendReOpenAppToPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToSHPlatformService;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * @author ★Luwei
 */
public class PlatformTest extends BaseTestTrueRollback {
	
	private static final Logger logger = LoggerFactory.getLogger(PlatformTest.class);
	
	@Autowired
	SendClaimToSHPlatformService sendClaimToSH;
	
	@Autowired
	SendClaimToPlatformService sendClaimToPlatformService;
	
	@Autowired
	SendVClaimToPlatformService sendVClaimToAll;
	
	@Autowired
	SendVClaimToSHPlatformService sendVClaimToSH;
	
	@Autowired
	SendEndCaseToPlatformService sendEndCase;
	
	@Autowired
	SendCancelToPlatformService sendCancel;
	
	@Autowired
	SendCancelToSHPlatformService sendSH;
	
	@Autowired
	SendReOpenAppToPlatformService sendReOpenApp;
	
	@Autowired
	SendEndCaseToSHPlatformService sendEndCaseToSH;
	
	@Autowired
	ClaimTaskService claimTaskService;
	

	@Test
	public void test2(){
//		sendVClaimToAll.sendVClaimBIToPlatform("70002002016120600000089");
//		sendEndCase.sendEndCaseToPlatform("4000000201612060021008","5000200201611010000420");
	}
	
}
