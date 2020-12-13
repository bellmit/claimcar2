package test;

import ins.framework.dao.database.DatabaseDao;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ConfigService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import service.ClaimTaskService;
//import ins.sino.claimcar.check.service.CheckTaskService;
//import ins.sino.claimcar.claim.service.ClaimTaskService;

public class ClaimTaskTest implements ClaimTaskService {

	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private ConfigService prpLDLimitService;
	@Autowired
	private ClaimService claimService;
	@Autowired
	private DatabaseDao databaseDao;
	
	@Test
	public void test() {

		//获取报案保单信息主表
		String registNo="4000000201612010000060";
		List<PrpLCMainVo> cMainList=policyViewService.getPolicyAllInfo(registNo);
		
		for(PrpLCMainVo cMainVo:cMainList){
			if(cMainVo.getRiskCode().substring(0,2).equals("11")){
				System.out.println("交强险");
                System.out.println(cMainVo.getRegistNo());
                System.out.println(cMainVo.getAgentCode());
                System.out.println(cMainVo.getRiskCode());
				}else if(cMainVo.getRiskCode().substring(0,2).equals("12")){
					System.out.println("商业险");
					 System.out.println(cMainVo.getRegistNo());
		             System.out.println(cMainVo.getAgentCode());
		             System.out.println(cMainVo.getRiskCode());
				}
			}
		
	}

	@Override
	public PrpLClaimVo submitCheck(PrpLScheduleTaskVo scheduleVo,
			PrpLCheckVo checkVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrpLClaimVo autoClaim(PrpLRegistVo registVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrpLClaimVo> findClaimListByRegistNo(String registNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrpLClaimVo> refreshPrpLClaimVo(String registNo,
			String policyNo, String businessNo, String nodeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrpLClaimVo findPrpLClaimVoByRegistNoAndPolicyNo(String registNo,
			String policyNo) {
		// TODO Auto-generated method stub
		return null;
	}
		
}	
		