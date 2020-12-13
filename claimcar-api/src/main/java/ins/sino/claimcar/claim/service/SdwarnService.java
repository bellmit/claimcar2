package ins.sino.claimcar.claim.service;

import java.util.List;

import ins.sino.claimcar.claim.vo.PrpLwarnInfoVo;

public interface SdwarnService {

	/**
	 *保存山东预警信息List<PrpLwarnInfoVo>
	 */
	public void savePrplwarninfo(List<PrpLwarnInfoVo> prpLwarnInfoVos);
	/**
	 * 通过报案号查询山东预警信息
	 * @param registNo
	 * @return
	 */
	public List<PrpLwarnInfoVo> findPrpLwarnInfoVosByRegistNo(String registNo);
	/**
	 * 通过理赔编码查询山东预警信息
	 * @param claimsequenceNo 理赔编号
	 * @param warnstageCode 理赔阶段
	 * @return
	 */
	public List<PrpLwarnInfoVo> findwarnVosByclaimsequenceNo(String claimsequenceNo,String warnstageCode);
}
