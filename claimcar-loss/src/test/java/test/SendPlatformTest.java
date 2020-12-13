/******************************************************************************
 * CREATETIME : 2016年5月30日 下午7:10:37
 ******************************************************************************/
package test;

import java.text.ParseException;
import java.util.Date;

import ins.platform.utils.DateUtils;
import ins.sino.claimcar.platform.service.LossToPlatformService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

/**
 * @author ★XMSH
 */
public class SendPlatformTest extends BaseTestTrueRollback {
	
	@Autowired
	LossToPlatformService lossToPlatformService;

	@Test
	public void sendRegistToPlatform() throws Exception {
		Date date = new Date();
		Date date1 = DateUtils.strToDate("2018-10-01", DateUtils.YToDay);
		long a = date.getTime()-date1.getTime();
		System.out.print("=========================="+a);
		if(a>0){
			System.out.println("==========================da");
		}else{
			System.out.println("==========================xiao");
		}

	}
}
