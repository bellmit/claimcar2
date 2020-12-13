package ins.sino.claimcar.selfHelpClaimCar.service;

import ins.sino.claimcar.selfHelpClaimCar.vo.DlossAmoutConfirmVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.PrpLAutocasestateInfoVo;

public interface SelfHelpClaimCarService {
	/**
	 * 保存或更新自助理赔的案件信息表
	 * @param reqVo
	 */
	public void updateOrSavePrpLAutocasestateInfo(DlossAmoutConfirmVo reqVo);
	/**
	 * 通过报案号和车牌号查询自助理赔的案件信息表
	 * @param registNo
	 * @param licenseNo
	 * @return
	 */
	public PrpLAutocasestateInfoVo findPrpLAutocasestateInfoByRegistNoAndLicenseNo(String registNo,String licenseNo);

}
