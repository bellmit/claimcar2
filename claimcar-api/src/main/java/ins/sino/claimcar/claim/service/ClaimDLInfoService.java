package ins.sino.claimcar.claim.service;

import java.util.List;

import ins.sino.claimcar.claim.vo.PrplcecheckResultVo;

public interface ClaimDLInfoService {

	/**
	 * 保存德联易控返回信息
	 * @param prplcecheckResultVo
	 */
	public void saveCeInfo(PrplcecheckResultVo prplcecheckResultVo);
	
	/**
	 * 通过报案号查询德联易控返回信息
	 * @param registNo
	 * @return
	 */
	public List<PrplcecheckResultVo> findResultVoByRegistNo(String registNo);
	
	/**
	 * 通过报案号查询德联易控返回信息
	 * @param id
	 * @return
	 */
	public PrplcecheckResultVo findResultVoById(Long id);
	
}
