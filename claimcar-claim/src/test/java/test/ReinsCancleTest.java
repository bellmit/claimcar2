package test;

import java.math.BigDecimal;

import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

public class ReinsCancleTest extends BaseTestTrueRollback {
	@Autowired 
	private ClaimCancelService claimCancelService;
	@Test
	public void cancleTest(){
		BigDecimal id = claimCancelService.findId("1207", "5000200201612070001059");
		PrpLcancelTraceVo	traceVo = claimCancelService.findByCancelTraceId(id);
		traceVo.setCreateUser("aaaa");
		claimCancelService.sendToClaimPlatform(traceVo);
	}
}
