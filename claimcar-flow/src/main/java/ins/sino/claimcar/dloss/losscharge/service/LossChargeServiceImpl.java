/******************************************************************************
 * CREATETIME : 2015年12月10日 下午3:34:33
 * FILE       : ins.sino.claimcar.dloss.ClaimTextService
 ******************************************************************************/
package ins.sino.claimcar.dloss.losscharge.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeHisVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.dloss.losscharge.po.PrpLDlossCharge;
import ins.sino.claimcar.dloss.losscharge.po.PrpLDlossChargeHis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * 
 * @author ★yangkun
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("lossChargeService")
public class LossChargeServiceImpl implements  LossChargeService{
	
	@Autowired
	DatabaseDao databaseDao;
	
	public void saveLossCharge(PrpLDlossChargeVo lossChargeVo){
		PrpLDlossCharge lossCharge = new PrpLDlossCharge();
		Beans.copy().from(lossChargeVo).to(lossCharge);
		databaseDao.save(PrpLDlossCharge.class,lossCharge);
	}
	
	public void saveOrUpdte(List<PrpLDlossChargeVo> lossChargeVos){
		if(lossChargeVos!=null && lossChargeVos.size()>0){
			Long businessNo =lossChargeVos.get(0).getBusinessId();
			String busineeType = lossChargeVos.get(0).getBusinessType();
			List<PrpLDlossCharge> lossCharges = this.findLossCharges(businessNo,busineeType);
			this.mergeList(lossChargeVos,lossCharges,"id",PrpLDlossCharge.class);
			databaseDao.saveAll(PrpLDlossCharge.class,lossCharges);
		}
	}
	
	private List<PrpLDlossCharge> findLossCharges(Long businessId,String busineeType){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("businessId",businessId);
		queryRule.addEqual("businessType",busineeType);
		queryRule.addAscOrder("id");
		
		List<PrpLDlossCharge> lossCharges = databaseDao.findAll(PrpLDlossCharge.class,queryRule);
		
		return lossCharges;
	}
	
	
	public List<PrpLDlossChargeVo> findLossChargeVos(Long businessId,String busineeType){
		List<PrpLDlossCharge> lossCharges = this.findLossCharges(businessId,busineeType);
		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
		for(PrpLDlossCharge lossCharge : lossCharges){
			PrpLDlossChargeVo lossChargeVo = new PrpLDlossChargeVo();
			Beans.copy().from(lossCharge).to(lossChargeVo);
			lossChargeVos.add(lossChargeVo);
		}
		
		return lossChargeVos;
	}
	
	/**
	 * 查询查勘公估费信息
	 */
	@Override
	public List<PrpLDlossChargeVo> findLossChargeVos(String registNo,Long businessId,String businessType) {
		List<PrpLDlossChargeVo> lossChargeVo = new ArrayList<PrpLDlossChargeVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(businessId != null){
			queryRule.addEqual("businessId",businessId);
		}
		queryRule.addEqual("businessType",businessType);
		List<PrpLDlossCharge> lossChargePoList = databaseDao.findAll(PrpLDlossCharge.class,queryRule);
		if(lossChargePoList != null && !lossChargePoList.isEmpty()){
			lossChargeVo = Beans.copyDepth().from(lossChargePoList).toList(PrpLDlossChargeVo.class);
		}
		return lossChargeVo;
	}

	/**
	 * 根据报案号查询所有losscharge表
	 */
	@Override
	public List<PrpLDlossChargeVo> findLossChargeVoByRegistNo(String registNo) {

		List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLDlossCharge> lossCharges = databaseDao.findAll(
				PrpLDlossCharge.class, queryRule);
		if (lossCharges != null) {
			lossChargeVos = Beans.copyDepth().from(lossCharges).toList(PrpLDlossChargeVo.class);
		}
		return lossChargeVos;
	}

	/** 更新查勘公估 */
	@Override
	public void updateLossCharge(PrpLDlossChargeVo lossChargeVo) {
		PrpLDlossCharge lossCharge = databaseDao.findByPK(PrpLDlossCharge.class,lossChargeVo.getId());
		Beans.copy().from(lossChargeVo).excludeNull().to(lossCharge);
		databaseDao.update(PrpLDlossCharge.class,lossCharge);
	}
	
	/**
	 * 
	 * 子表vOList转为poList
	 * 
	 * @param voList
	 * @param poList
	 * @param idName
	 *            主键
	 * @param paramClass
	 *            子表class类
	 * @modified: ☆yangkun(2015年12月10日 上午11:32:05): <br>
	 */
	public void mergeList(List voList, List poList, String idName,Class paramClass){
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Integer, Object> keyMap = new HashMap<Integer, Object>();
		Map<Object, Object> poMap = new HashMap<Object, Object>();
		
		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			Object key;
			try {
				key = PropertyUtils.getProperty(element, idName);
				map.put(key, element);
				keyMap.put(i, key);
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		for (Iterator it = poList.iterator(); it.hasNext();) {
			Object element = (Object) it.next();
			try {
				Object key = PropertyUtils.getProperty(element, idName);
				poMap.put(key, null);
				if (!map.containsKey(key)) {
					//delete(element);
					databaseDao.deleteByObject(paramClass,element);
					it.remove();
				} else {
					Beans.copy().from(map.get(key)).excludeNull().to(element);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		for (int i = 0, count = voList.size(); i < count; i++) {
			Object element = voList.get(i);
			if (element == null) {
				continue;
			}
			try{
				Object poElement = paramClass.newInstance();
				Object key = keyMap.get(i);
				if (key == null || !poMap.containsKey(key)) {
					Beans.copy().from(element).to(poElement);
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	/**
	 * 删除所有的费用
	 */
	@Override
	public void delCharge(Long businessNo,String busineeType) {
		List<PrpLDlossCharge> lossCharges = this.findLossCharges(businessNo,busineeType);
		if(lossCharges!=null && lossCharges.size()>0){
			databaseDao.deleteAll(PrpLDlossCharge.class,lossCharges);
		}
		
	}

	/*
	 * 保存费用轨迹
	 */
	@Override
	public void saveChargeHis(Long businessId,String busineeType){
		List<PrpLDlossChargeVo> lossChargeList = this.findLossChargeVos(businessId, busineeType);
		List<PrpLDlossChargeHis> chargeHisList = null;
		Date date = new Date();
		if(lossChargeList != null && lossChargeList.size() > 0){
			chargeHisList = Beans.copyDepth().from(lossChargeList).toList(PrpLDlossChargeHis.class);
			for(PrpLDlossChargeHis chargeHis:chargeHisList){
				chargeHis.setLossChargeId(chargeHis.getId());
				chargeHis.setId(null);
				chargeHis.setCreateTime(date);
			}
			databaseDao.saveAll(PrpLDlossChargeHis.class, chargeHisList);
		}
	}
	
	@Override
	public PrpLDlossChargeHisVo findLossChargeHisVo(Long lossChargeId,Long businessId){
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("lossChargeId", lossChargeId);
		queryRule.addEqual("businessId", businessId);
		queryRule.addDescOrder("createTime");
		List<PrpLDlossChargeHis> lossChargeHisList = databaseDao.findAll(PrpLDlossChargeHis.class, queryRule);
		if(lossChargeHisList != null && lossChargeHisList.size() > 0){
			PrpLDlossChargeHisVo lossChargeHisVo = new PrpLDlossChargeHisVo();
			Beans.copy().from(lossChargeHisList.get(0)).to(lossChargeHisVo);
			return lossChargeHisVo;
		}
		return null;
	}
}
