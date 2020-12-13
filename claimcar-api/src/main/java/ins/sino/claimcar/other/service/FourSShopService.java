package ins.sino.claimcar.other.service;

import ins.framework.dao.database.support.Page;
import ins.sino.claimcar.manager.vo.PrpLFourSShopInfoVo;

public interface FourSShopService {


	/**
	 * 根据主键find PrpLFourSShopInfoVo
	 * 
	 * @param id
	 * @return
	 */
	public abstract PrpLFourSShopInfoVo findFourSShopByPK(Long id);

	/**
	 * 根据hql语句拼装条件查询数据
	 * 
	 * @param prpLFourSShopInfoVo
	 * @param start
	 * @param length
	 * @return
	 */
	public abstract Page<PrpLFourSShopInfoVo> findAllFourSShopByHql(
			PrpLFourSShopInfoVo prpLFourSShopInfoVo, int start, int length);

	/**
	 * 根据主键删除4S店信息
	 * 
	 * @param id
	 */
	public abstract void deleteFourSShopByPK(Long id);
	
	public void saveOrUpdatefourSShop(PrpLFourSShopInfoVo fourSShopInfoVo);

}