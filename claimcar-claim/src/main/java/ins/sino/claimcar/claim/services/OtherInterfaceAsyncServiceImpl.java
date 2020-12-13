package ins.sino.claimcar.claim.services;

import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.service.CaseLeapHNService;
import ins.sino.claimcar.carinterface.service.CaseLeapService;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.ciitc.service.CompeInterfaceService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.OtherInterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.genilex.service.ClaimToGenilexService;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.dubbo.config.annotation.Service;

@Async("asyncExecutor")
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("otherInterfaceAsyncService")
public class OtherInterfaceAsyncServiceImpl implements OtherInterfaceAsyncService{

	@Autowired
	CaseLeapHNService caseLeapHNService;
	@Autowired
	CaseLeapService caseLeapService;
	@Autowired
	ClaimToReinsuranceService claimToReinsuranceService;
	@Autowired
    ClaimToGenilexService claimToGenilexService;
	@Autowired
    CompeInterfaceService compeInterfaceService;
	
	@Override
	public void claimToHN(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws Exception{
		caseLeapHNService.claimToHN(prpLClaimVo, userVo);
	}
	
	@Override
	public void endCaseToHN(PrpLEndCaseVo endCaseVo,String userCode){
		caseLeapHNService.endCaseToHN(endCaseVo, userCode);
	}
	
	@Override
	public void claimToGZ(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws Exception{
		caseLeapService.claimToGZ(prpLClaimVo, userVo);
	}
	
	@Override
	public void endCaseToGZ(PrpLEndCaseVo endCaseVo,String userCode){
		caseLeapService.endCaseToGZ(endCaseVo, userCode);
	}
	
	@Override
    public void TransDataForReinsCaseVo(String businessType,
			PrpLClaimVo claimVo,ClaimInterfaceLogVo claimInterfaceLogVo) throws Exception{
    	claimToReinsuranceService.TransDataForReinsCaseVo(businessType, claimVo, claimInterfaceLogVo);
    }
	
	@Override
	public void checkToGenilex(PrpLCheckVo checkVo,Long flowTaskId){
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.GENILEX,"");
        if("1".equals(configValueVo.getConfigValue())){
        	claimToGenilexService.checkToGenilex(checkVo, flowTaskId);
        }
	}
	
	@Override
	public void endCaseToGenilex(PrpLEndCaseVo endCaseVo,PrpLCompensateVo compensateVo,String taskId){
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.GENILEX,"");
        if("1".equals(configValueVo.getConfigValue())){
        	claimToGenilexService.endCaseToGenilex(endCaseVo,compensateVo,taskId);
        }
	}
	
	@Override
	public void reqByEndCase(PrpLEndCaseVo endCaseVo,SysUserVo userVo){
		compeInterfaceService.reqByEndCase(endCaseVo, userVo);
	}
	
}
