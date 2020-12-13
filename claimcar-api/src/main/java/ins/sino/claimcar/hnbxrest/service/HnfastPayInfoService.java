package ins.sino.claimcar.hnbxrest.service;

import java.util.Map;

import ins.sino.claimcar.check.vo.PrpLcaseImageinfoMainVo;
import ins.sino.claimcar.check.vo.PrplcaseStateinfoVo;
import ins.sino.claimcar.hnbxrest.vo.SubmitcaseimageinforVo;
import ins.sino.claimcar.hnbxrest.vo.SubmitcasestateVo;

public interface HnfastPayInfoService {

	
	/**
	 * 保存图片主表信息
	 * @param prpLcaseImageinfoMainVo
	 * @return
	 */
	public PrpLcaseImageinfoMainVo saveOfPrplCaseImageInfoMain(SubmitcaseimageinforVo submitcaseimageinforVo)throws Exception;
	/**
	 * 更新图片主表与子表信息
	 * @param PrpLcaseImageinfoMainVo
	 */
	public void updateOfPrplCaseImageInfoMain(PrpLcaseImageinfoMainVo mainVo);
	/**
	 * 通过报案号查询图片主表
	 * @param registNo
	 * @return
	 */
	public PrpLcaseImageinfoMainVo findPrpLcaseImageinfoMainByRegistNo(String registNo);
	/**
	 * 更新案件状态表
	 * @param vo
	 */
	public void updateOfPrplcaseStateinfo(PrplcaseStateinfoVo vo,SubmitcasestateVo submitcasestateVo)throws Exception;
	/**
	 * 保存案件状态表
	 * @param vo
	 * @param submitcasestateVo
	 * @throws Exception
	 */
	public void saveOfPrplcaseStateinfo(PrplcaseStateinfoVo vo,SubmitcasestateVo submitcasestateVo)throws Exception;
	
	/**
	 * 通过报案号
	 * @param registNo
	 * @return
	 */
	public PrplcaseStateinfoVo findPrplcaseStateinfoVoByRegistNo(String registNo);
}
