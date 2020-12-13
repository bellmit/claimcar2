package ins.sino.claimcar.other.service;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrpdLawFirmVo;

import java.util.List;

public interface LawFirmService {

	/**
	 * 保存或更新律师事务所
	 * 
	 * @param PrpdLawFirmVo
	 * @return prpdLawFirm
	 */
	public abstract void saveOrUpdatePrpdLawFirm(
			PrpdLawFirmVo prpdLawFirmVo);

	/**
	 * 根据主键find PrpdLawFirmVo
	 * 
	 * @param id
	 * @return prpdLawFirmVo
	 */
	public abstract PrpdLawFirmVo findPrpdLawFirmVoByPK(Long id);

	/**
	 * find PrpdLawFirmVo
	 * @param id
	 * @return prpdLawFirmVos
	 */
	public abstract ResultPage<PrpdLawFirmVo> findAllPrpdLawFirmByHql(
			PrpdLawFirmVo prpdLawFirmVo, int start, int length);

	public abstract List<PrpdLawFirmVo> findAllPrpdLawFirm();

	public abstract String updates(PrpdLawFirmVo prpdLawFirmVo);

	/**
	 * 创建一个Excel
	 * @return
	 * @throws Exception
	 */
	public abstract List<PrpdLawFirmVo> getPrpdLawFirm() throws Exception;

}