package test.platform.common.service.facade;

import ins.platform.common.service.facade.BillNoService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestFalseRollback;


public class BillNoServiceTest extends BaseTestFalseRollback {
	
	@Autowired
	private BillNoService billNoService;

	@Test
	public void testRegistNo() {
		String billNO = billNoService.getRegistNo("00020000","1201");
		System.out.println(billNO);

	}
	
	@Test
	public void testClaimNo() {
		String claimNo = billNoService.getClaimNo("00020000","1201");
		System.out.println(claimNo);

	}
	
	@Test
	public void testFlowId() {
		String flowId = billNoService.getFlowId("00020000","1201");
		System.out.println(flowId);

	}

}
