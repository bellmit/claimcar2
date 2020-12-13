/******************************************************************************
* CREATETIME : 2016年6月20日 下午3:49:08
******************************************************************************/
package test.sms;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ins.sino.claimcar.sms.service.SmsService;
import base.BaseTestTrueRollback;


/**
 * 
 * @author ★XMSH
 */
public class SmsServiceTest extends BaseTestTrueRollback {
	
	@Autowired
	SmsService smsService;

	@Test
	public void testSendSMSContent(){
		smsService.sendSMSContent("1234","18679052168","test");
	}

}
