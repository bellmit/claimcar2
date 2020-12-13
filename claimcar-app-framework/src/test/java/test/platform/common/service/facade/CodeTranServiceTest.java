package test.platform.common.service.facade;


import ins.platform.common.service.facade.CodeTranService;
import ins.platform.vo.SysCodeDictVo;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


public class CodeTranServiceTest extends BaseTestTrueRollback {

	@Autowired
	CodeTranService codeTranService;

	@Test
	public void testCodeDict() {
		Map<String,SysCodeDictVo> dictMap = codeTranService.findCodeDictTransMap("UnHandReasonA","B");
		System.out.println(dictMap);
	}

	@Test
	public void testTrans() {
		Map<String,SysCodeDictVo> dictMap = codeTranService.findCodeDictTransMap("RiskCode",null);
		System.out.println(dictMap);
	}
}
