package ins.sino.claimcar.carinterface.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;

import java.text.ParseException;

public interface CaseLeapHNService {

	/**
	 * 产险立案：案件号、立案人姓名、立案人手机号码、险种、车牌号、车架号、被保险人身份证号、出险时间
	 * @param prpLRegistVo
	 * @param userVo
	 * @throws ParseException 
	 */
	public  ClaimInterfaceLogVo claimToHN(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws ParseException;
	/**
	 * 产险结案：案件号、立案人姓名、立案人手机号码、险种、结案时间
	 * @param endCaseVo
	 * @param userCode
	 * @throws ParseException
	 */
	public ClaimInterfaceLogVo endCaseToHN(PrpLEndCaseVo endCaseVo,String userCode);
}
