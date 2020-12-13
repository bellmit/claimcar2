/******************************************************************************
* CREATETIME : 2016年10月20日 下午7:21:16
******************************************************************************/
package test;

import ins.sino.claimcar.regist.service.RegistService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年10月20日
 */
public class RegistServiceTest extends BaseTestTrueRollback {

	@Autowired
	private RegistService registService;

	@Test
	public void findPrpLCMainVoByPolicyNo() {
		long start = System.currentTimeMillis();
		String registNo = "200020020161206004207";
		registService.findPrpLCMainVoByPolicyNo(registNo);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@==207941====="+( System.currentTimeMillis()-start ));
		start = System.currentTimeMillis();
		registNo = "21001002015120300000x";
		registService.findPrpLCMainVoByPolicyNo(registNo);

		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@==207942====="+( System.currentTimeMillis()-start ));

	}

}
