package ins.sino.claimcar.base.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.po.PrpLFourSShopInfo;
import ins.sino.claimcar.manager.vo.PrpLFourSShopInfoVo;
import ins.sino.claimcar.other.service.FourSShopService;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "fourSShopService")
public class FourSShopServiceImpl implements FourSShopService  {
	@Autowired
	DatabaseDao databaseDao;

	/**
	 * 保存或更新4S店信息
	 * 
	 * @param fourSShopInfoVo
	 * @return
	 */
	@Override
	public void saveOrUpdatefourSShop(PrpLFourSShopInfoVo fourSShopInfoVo) {
			
			PrpLFourSShopInfo fourSShopInfoPo = Beans.copyDepth().from(fourSShopInfoVo)
				.to(PrpLFourSShopInfo.class);
			
		if (fourSShopInfoPo.getId() != null) {
				databaseDao.update(PrpLFourSShopInfo.class, fourSShopInfoPo);
		} else {
			databaseDao.save(PrpLFourSShopInfo.class, fourSShopInfoPo);
			}
			
//		return fourSShopInfoPo;

	}

	/**
	 * 根据主键find PrpLFourSShopInfoVo
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public PrpLFourSShopInfoVo findFourSShopByPK(Long id) {

		PrpLFourSShopInfoVo reVo = new PrpLFourSShopInfoVo();
		if (id != null) {
			PrpLFourSShopInfo fourSShopInfoPo = databaseDao.findByPK(
					PrpLFourSShopInfo.class, id);

			reVo = Beans.copyDepth().from(fourSShopInfoPo)
					.to(PrpLFourSShopInfoVo.class);
		}

		return reVo;

	}

	/**
	 * 根据hql语句拼装条件查询数据
	 * 
	 * @param prpLFourSShopInfoVo
	 * @param start
	 * @param length
	 * @return
	 */
	@Override
	public Page<PrpLFourSShopInfoVo> findAllFourSShopByHql(
			PrpLFourSShopInfoVo prpLFourSShopInfoVo, int start, int length) {
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer(
				"from PrpLFourSShopInfo pf where ");

		queryString.append(" pf.validStatus = ? ");
		// 有效无效标志 必填
		paramValues.add(prpLFourSShopInfoVo.getValidStatus());
		// 归属机构
		if (StringUtils.isNotBlank(prpLFourSShopInfoVo.getComCode())) {
			queryString.append(" AND pf.comCode = ? ");
			paramValues.add(prpLFourSShopInfoVo.getComCode());
		}
		// 出单合作店名称
		if (StringUtils.isNotBlank(prpLFourSShopInfoVo.getFourSShopName())) {
			queryString.append(" AND pf.fourSShopName like ? ");
			paramValues.add(prpLFourSShopInfoVo.getFourSShopName() + "%");
		}
		// 修理厂名称
		if (StringUtils.isNotBlank(prpLFourSShopInfoVo.getFactoryName())) {
			queryString.append(" AND pf.factoryName like ? ");
			paramValues.add(prpLFourSShopInfoVo.getFactoryName() + "%");
		}
		// 出单合作店等级
		if (StringUtils.isNotBlank(prpLFourSShopInfoVo.getFoursLevel())) {
			queryString.append(" AND pf.foursLevel = ? ");
			paramValues.add(prpLFourSShopInfoVo.getFoursLevel());
		}
		// 送修支持
		if (StringUtils.isNotBlank(prpLFourSShopInfoVo.getSendRepair())) {
			queryString.append(" AND pf.sendRepair = ? ");
			paramValues.add(prpLFourSShopInfoVo.getSendRepair());
		}
		queryString.append(" Order By pf.id ");
		// 执行查询
		Page page = databaseDao.findPageByHql(queryString.toString(), (start
				/ length + 1), length, paramValues.toArray());
		// Page pageReturn = assemblyPolicyInfo(page, prpLFourSShopInfoVo);
		// 返回结果ResultPage
		return page;
	}

	/**
	 * 根据主键删除4S店信息
	 * 
	 * @param id
	 */
	@Override
	public void deleteFourSShopByPK(Long id) {
		if (id != null) {
			databaseDao.deleteByPK(PrpLFourSShopInfo.class, id);

		}
	}

}
