/******************************************************************************
* CREATETIME : 2016年7月15日 下午2:59:27
******************************************************************************/
package test;

import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrplReplevyMainVo;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pay.service.RecPayService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.platform.service.SendPaymentToPlatformService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;


/**
 * 
 * @author ★XMSH
 */
public class CallPaymentTest extends BaseTestTrueRollback {
	
	@Autowired
	CompensateService compensateService;
	@Autowired
	ClaimToPaymentService claimToPaymentServiceImpl;
	@Autowired
	PadPayService padPayService;
	@Autowired
	RecPayService recPayService;
//	@Autowired
//	RecLossDubboService recLossDubboService;
	@Autowired
	SendPaymentToPlatformService sendPaymentToPlatformService;
	@Autowired
	AssessorService assessorService;

	/**
	 * 垫付送收付单元测试
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月13日 下午2:59:54): <br>
	 */
	@Test
	public void testPadPay() throws Exception{
		
		String registNo = "4000000201612070000898";
		
		PrpLPadPayMainVo padPayMainVo = padPayService.findPadPayMainByRegistNo(registNo).get(0);
		
		claimToPaymentServiceImpl.padPayToPayment(padPayMainVo);
	}
	
	/**
	 * 预付送收付单元测试
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月13日 下午2:59:39): <br>
	 */
	@Test
	public void testPrePay() throws Exception{
		
		String compensateNo = "Y00000020161207000057";
		
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		
//		claimToPaymentService.prePayToPayment(compensateVo,prePayVos);

		claimToPaymentServiceImpl.prePayToPayment(compensateVo);

	}
	
	/**
	 * 赔款送收付单元测试
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月13日 下午2:59:22): <br>
	 */
	@Test
	public void testCompensate() throws Exception{
		
		String compensateNo = "71101002016120700000011";
		PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
		
		claimToPaymentServiceImpl.compensateToPayment(compensateVo);
	}
	
	/**
	 * 追偿送收付单元测试
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月13日 下午2:58:47): <br>
	 */
	@Test
	public void testRecPay() throws Exception{
		String claimNo = "70002002016120600000089";
		
		PrplReplevyMainVo replevyMainVo = recPayService.findPrplReplevyMainVoByClaimNo(claimNo);
		
		claimToPaymentServiceImpl.recPayToPayment(replevyMainVo);
	}
	
	/**
	 * 损余回收送收付单元测试
	 * @throws Exception
	 * @modified:
	 * ☆XMSH(2016年9月27日 下午4:00:54): <br>
	 */
//	@Test
//	public void testRecLoss() throws Exception{
//		String recLossMainId = "R2016000000000007";
//		List<PrpLRecLossVo> recLossVos = recLossDubboService.findPrpLRecLossListByMainId(recLossMainId);
//		
//		for(PrpLRecLossVo recLossVo: recLossVos){
//			claimToPaymentServiceImpl.recLossToPayment(recLossVo);
//		}
//	}
	
	@Test
	public void testSendPaymentToPlatform() throws Exception{
		
//		sendPaymentToPlatformService.sendPaymentCIToPlatform("70002002016110100000026");
		sendPaymentToPlatformService.sendPaymentBIToPlatform("70002002016120600000021");
	}
	
	@Test
	public void testAssessorPaymentToPlatform() throws Exception{
		String taskid = "F012016000200000003";
		
		PrpLAssessorMainVo mainVo = assessorService.findAssessorMainVoByTaskNo(taskid);
		
		for(PrpLAssessorFeeVo feeVo : mainVo.getPrpLAssessorFees()){
			claimToPaymentServiceImpl.assessorToPayment(mainVo,feeVo);
		}
		
	}

}
