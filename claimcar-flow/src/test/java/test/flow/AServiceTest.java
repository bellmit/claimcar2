package test.flow;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.AssignService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


public class AServiceTest extends BaseTestTrueRollback {

	@Autowired
	AssignService assignService;

	@Test
	public void testAss() {
		String comCode = "11020083";
	//	SysUserVo userCode = assignService.execute(FlowNode.Check,comCode);
		//System.out.println(userCode.getUserCode());

	}
}
