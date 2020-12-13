package test;

import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindHisVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import base.BaseTestTrueRollback;

public class CallWebServiceTest extends BaseTestTrueRollback {
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CompensateTaskService compensateTaskService;

	private static Logger logger = LoggerFactory.getLogger(CallWebServiceTest.class);
	
	@Test
	public void test() throws Exception {
		String registNo1 = "4000000201612010000218";
		String registNo2 = "4000000201612010000208";
		String registNo3 = "4000000201612010000219";
		String claimNo1="5100100201612010000069";
		String claimNo2 = "5100100201611010000069";
		String claimNo3 = "5100100201612010000088";
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo1);
		List<PrpLClaimKindHisVo> kindHisVoList = claimTaskService 
				.findPrpLClaimKindHisByRegistNo(registNo1);
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		if (kindHisVoList != null && kindHisVoList.size() > 0) {
			claimToReinsuranceService.TransDataForClaimVo(claimVo,
					kindHisVoList,claimInterfaceLogVo);
		}
	}
	
	public void testCompensate() throws Exception{
		//String registNo = "4000000201612010000218";
		String claimNo = "5100100201611010000099";
		String claimNo2 = "5000200201612060000042";
		String claimNo1 = "5130100201611010000001";
		String claimNo3="5110100201612060000057";
		String claimNo4="5000200201612060000112";   //
		String claimNo5="5000200201611010000123";   //
		String claimNo6="5110100201611010000244";
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo5);
		PrpLCompensateVo prpLCompensateVo =  compensateTaskService.findCompByClaimNo(claimNo5);
		logger.debug("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimToReinsuranceService.TransDataForCompensateVo(claimVo, prpLCompensateVo);
		
	}
	
	public void testReinsCase() throws Exception{
		String claimNo = "5130100201611010000001";
		String claimNo1 = "5100100201612010000074";
		String claimNo2 = "5100100201612010000074";
		//String comCode="comCode";
		String  businessType="2";
		PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo1);
		ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); //填写日志表
		claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
		claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
		claimInterfaceLogVo.setComCode(claimVo.getComCode());
		claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
		claimInterfaceLogVo.setCreateTime(new Date());
		claimToReinsuranceService.TransDataForReinsCaseVo(businessType, claimVo,claimInterfaceLogVo);
		logger.debug("结束！！！！！！！！！\n");
	}

}
