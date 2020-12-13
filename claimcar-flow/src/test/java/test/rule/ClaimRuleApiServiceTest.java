package test.rule;

import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.LossPropToVerifyRuleVo;
import ins.sino.claimcar.rule.vo.VerifyClaimRuleVo;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.rule.vo.VerifyPersonRuleVo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


public class ClaimRuleApiServiceTest extends BaseTestTrueRollback {

	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Test
	public void testLossCarToVerifyRule() {
		VerifyLossRuleVo ruleVo = new VerifyLossRuleVo();
		ruleVo.setMaxPartFee(999999991d);
		ruleVo.setComCode("30000999");
		ruleVo.setSumLossFee(999999991d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("13000999");
		ruleVo.setSumLossFee(5005d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("11000999");
		ruleVo.setSumLossFee(4001d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("00000999");
		ruleVo.setTopComp("1");//
		ruleVo.setSumLossFee(999999d);
		//ruleVo.setSumLossFee(100000d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
		System.out.println(ruleVo.getBackLevel()+"   ==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		
//		ruleVo.setComCode("00000999");
//		ruleVo.setTopComp("0");
//		ruleVo.setSumLossFee(2001d);
//		ruleVo.setSelfConfigFlag(0);
//		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());

//		ruleVo.setBackMessage(null);
//		ruleVo.setSumLossFee(6000d);
//		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=1="+ruleVo.getBackMessage());
//
//		ruleVo.setBackMessage(null);
//		ruleVo.setMaxPartFee(6000d);
//		ruleVo.setSumLossFee(11000d);
//		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=2="+ruleVo.getBackMessage());
//
//		ruleVo.setBackMessage(null);
//		ruleVo.setMaxPartFee(6000d);
//		ruleVo.setSumLossFee(100000d);
//		ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=3="+ruleVo.getBackMessage());

	}
	
	//@Test
	public void testpropToVerifyRule() {
		
		LossPropToVerifyRuleVo ruleVo = new LossPropToVerifyRuleVo();
		ruleVo.setComCode("13010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=3="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("11010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=3="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("30010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=3="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("13010083");
		ruleVo.setTopComp("1");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=3="+ruleVo.getBackMessage());
		
	}
	//@Test
	public void testpersonToVeriyfRule() {
		VerifyPersonRuleVo ruleVo = new VerifyPersonRuleVo();
//		ruleVo.setComCode("13010083");
//		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
//		
//		ruleVo.setComCode("11010083");
//		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
//		
//		ruleVo.setComCode("30010083");
//		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
//		
//		ruleVo.setComCode("13010083");
//		ruleVo.setTopComp("1");
//		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
//		LossPropToVerifyRuleVo ruleVo = new LossPropToVerifyRuleVo();
//		ruleVo.setComCode("13010083");
//		ruleVo.setTopComp("1");
//		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.(ruleVo);
	}
	//@Test
	public void testpersonToPriceRule() {
		VerifyPersonRuleVo ruleVo = new VerifyPersonRuleVo();
		ruleVo.setComCode("13010083");
		ruleVo.setSumLossFee(99500d);
//		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
//		ruleVo.setSumLossFee(99500.0d);
		ruleVo.setClassCode("12");
		ruleVo.setRiskCode("1206");
		System.out.println(ruleVo.getSumLossFee());
		ruleVo = claimRuleApiService.lossPersonToPrice(ruleVo);
		
		
		ruleVo.setComCode("30010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
//		ruleVo.setSumLossFee(99500.0d);
		ruleVo.setClassCode("12");
		ruleVo.setRiskCode("1206");
		System.out.println(ruleVo.getSumLossFee());
		ruleVo = claimRuleApiService.lossPersonToPrice(ruleVo);
		
		
		ruleVo.setComCode("11010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
//		ruleVo.setSumLossFee(99500.0d);
		ruleVo.setClassCode("12");
		ruleVo.setRiskCode("1206");
		System.out.println(ruleVo.getSumLossFee());
		ruleVo = claimRuleApiService.lossPersonToPrice(ruleVo);
		
		ruleVo.setComCode("11010083");
		ruleVo.setSumLossFee(99500d);
		ruleVo = claimRuleApiService.lossPersonToVerify(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		ruleVo.setTopComp("1");
//		ruleVo.setSumLossFee(99500.0d);
		ruleVo.setClassCode("12");
		ruleVo.setRiskCode("1206");
		System.out.println(ruleVo.getSumLossFee());
		ruleVo = claimRuleApiService.lossPersonToPrice(ruleVo);
		
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackLevel());
	}

	//@Test
	public void testCompToVerifyRule() {
		VerifyClaimRuleVo ruleVo = new VerifyClaimRuleVo();
		ruleVo.setSumAmt(10000d);
		ruleVo.setClassCode("11");
		ruleVo.setComCode("30000999");
		ruleVo = claimRuleApiService.compToVClaim(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setSumAmt(10000d);
		ruleVo.setClassCode("11");
		ruleVo.setComCode("13000999");
		ruleVo = claimRuleApiService.compToVClaim(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setSumAmt(10000d);
		ruleVo.setComCode("11000999");
		ruleVo.setClassCode("11");
		ruleVo = claimRuleApiService.compToVClaim(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setSumAmt(10000d);
		ruleVo.setTopComp("1");
		ruleVo.setClassCode("11");
		ruleVo.setComCode("30000999");
		ruleVo = claimRuleApiService.compToVClaim(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		
	}
	
	//@Test
	public void testLossCarToPriceRule() {
		VerifyLossRuleVo ruleVo = new VerifyLossRuleVo();
		ruleVo.setMaxPartFee(1000d);
		ruleVo.setComCode("30000999");
		ruleVo.setSumLossFee(2001d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("13000999");
		ruleVo.setSumLossFee(5005d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("11000999");
		ruleVo.setSumLossFee(4001d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		ruleVo.setComCode("00000999");
		ruleVo.setTopComp("1");
		ruleVo.setSumLossFee(100000d);
		ruleVo.setSelfConfigFlag(0);
		ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		
//		ruleVo.setComCode("00000999");
//		ruleVo.setTopComp("0");
//		ruleVo.setSumLossFee(2001d);
//		ruleVo.setSelfConfigFlag(0);
//		ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());
		
		//LossPropToVerifyRuleVo ruleVo = new LossPropToVerifyRuleVo();
//		ruleVo.setSumLossFee(10000d);
//		ruleVo = claimRuleApiService.lossPropToVerifyRule(ruleVo);
//		System.out.println("==VerifyLossRuleVo.getBackMessage=0="+ruleVo.getBackMessage());

	}
}
