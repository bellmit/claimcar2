/******************************************************************************
* CREATETIME : 2016年4月27日 上午10:15:55
******************************************************************************/
package platform.test;


import ins.sino.claimcar.platform.service.CertifyToPaltformService;

import java.text.ParseException;

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
	CertifyToPaltformService certifyToPaltformService;
	
	
	@Test
	public void test() throws ParseException {

		String registNo = "4000000201612060000632";

		try{
			certifyToPaltformService.certifyToPaltform(registNo,null);
//			certifyToAll.sendCertifyCIToPlarformService(registNo);
//			certifyToAll.sendCertifyBIToPlarformService(registNo);
//			certifyToAll.sendCertifyCIToPlarformService(registNo);
//			certifyToSH.sendCertifyToPlatformSH_BI(registNo);
		}
		catch(Exception e){
			logger.info("发送失败："+e.getMessage());
			e.printStackTrace();
		}
		

	}

}
