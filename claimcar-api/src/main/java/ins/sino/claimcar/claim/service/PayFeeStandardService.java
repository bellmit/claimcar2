package ins.sino.claimcar.claim.service;

import java.util.List;

import ins.framework.common.ResultPage;
import ins.sino.claimcar.claim.vo.PrplFeeStandardVo;

public interface PayFeeStandardService {
	/**
	 * 通过地址编码与年份查找费用标准表
	 * @param areaCode
	 * @param yearCode
	 * @return
	 */
	public PrplFeeStandardVo findFeeStandardVoByAreaCodeAndYearCode(String areaCode,String yearCode);
	/**
	 * 保存费用标准表
	 * @param prplFeeStandardVo
	 */
	public void saveOrUpdatePrplFeeStandardVo(PrplFeeStandardVo prplFeeStandardVo);
	
	/*
	 * 查询各地区费用标准
	 */
	public ResultPage<PrplFeeStandardVo> findAllFeeStandard(PrplFeeStandardVo prplFeeStandardVo, int start, int length) throws Exception;
	/**
	 *根据id查人伤费用标准表
	 * @param id
	 * @return
	 */
	public PrplFeeStandardVo findPrplFeeStandardVoById(Long id);
	/**
	 * 根据id删除人伤费用标准表
	 * @param id
	 */
	public void deletePrplFeeStandardVoById(Long id);
	/**
	 * 根据ImagebussNo删除人伤费用标准表
	 * @param ImagebussNo
	 */
	public void deletePrplFeeStandardVoByImagebussNo(String ImagebussNo);
	/**
	 * 根据业务号查询人伤费用标准表
	 * @param bussNo
	 * @return
	 */
	public List<PrplFeeStandardVo> findPrplFeeStandardBybussNo(String bussNo);

}
