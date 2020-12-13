package ins.sino.claimcar.carinterface.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;

import java.text.ParseException;

public interface CaseLeapService {

	/**
	 * 产险立案：地区、保单号、险种、车牌号、车架号、三责车辆车牌号、三责车辆车架号、出险时间、立案号、立案人姓名、手机号
	 * @param prpLClaimVo
	 * @param userVo
	 * @throws ParseException
	 */
	public  ClaimInterfaceLogVo claimToGZ(PrpLClaimVo prpLClaimVo,SysUserVo userVo) throws ParseException;
	/**
	 * 产险结案：地区、保单号、险种、事故号、被保险人身份证号、结案时间、赔款金额	、手机号
	 * @param endCaseVo
	 * @param userCode
	 * @throws ParseException
	 */
	public ClaimInterfaceLogVo endCaseToGZ(PrpLEndCaseVo endCaseVo,String userCode);
}
