package ins.sino.claimcar.court.service;

import java.util.List;

import ins.sino.claimcar.court.vo.PrpLCourtClaimVo;
import ins.sino.claimcar.court.vo.PrpLCourtCompensationVo;
import ins.sino.claimcar.court.vo.PrpLCourtConfirmVo;
import ins.sino.claimcar.court.vo.PrpLCourtIdentifyVo;
import ins.sino.claimcar.court.vo.PrpLCourtLitigationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMediationVo;
import ins.sino.claimcar.court.vo.PrpLCourtMessageVo;
import ins.sino.claimcar.court.vo.PrpLCourtPartyVo;


/**
 * 高院信息服务接口
 * @author wurh
 *
 */
public interface CourtService {
	
	/**
	 * 高院查询接口
	 * @param courtMsgVo
	 */
	public void seniorCourtQuery(PrpLCourtMessageVo courtMsgVo);

	public void saveByCourtMessage(PrpLCourtMessageVo prpLCourtMessageVo);
	
	/**
	 * 
	 * @param status
	 * @param maxTimes
	 * @return
	 */
	public List<PrpLCourtMessageVo> searchPrpLCourtMessageVo(String status,String maxTimes);
	
	/**
	 * 
	 * @param registNo
	 * @param status
	 * @return
	 */
	public PrpLCourtMessageVo findCourtMessage(String registNo, int status); 
	
	/**
	 * 查询当事人信息
	 * @param id
	 * @return
	 */
	public PrpLCourtPartyVo findCourtParty(Long id);
	/**
	 * 查询鉴定表信息
	 * @param id
	 * @return
	 */
	public PrpLCourtIdentifyVo findCourtIdentify(Long id);

	public PrpLCourtCompensationVo findCourtCompensate(Long id);

	public PrpLCourtClaimVo findCourtClaim(Long id);

	public PrpLCourtLitigationVo findCourtLitigation(Long id);

	public PrpLCourtConfirmVo findCourtConfirm(Long id);

	public PrpLCourtMediationVo findCourtMediation(Long id);
	
}
