/******************************************************************************
 * CREATETIME : 2016年5月30日 下午7:10:37
 ******************************************************************************/
package test;

import ins.sino.claimcar.platform.service.RegistToPaltformService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * @author ★XMSH
 */
public class SendPlatformTest extends BaseTestTrueRollback {

	@Autowired
	private RegistToPaltformService registToPaltformService;

	@Test
	public void sendRegistToPlatform() {
		String registNo = "4110100201612060000002";
		registToPaltformService.sendRegistToPlatform(registNo);
	}
}
