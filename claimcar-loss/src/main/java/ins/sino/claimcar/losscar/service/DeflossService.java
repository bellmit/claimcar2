/******************************************************************************
 * CREATETIME : 2015年12月1日 下午3:02:17
 * FILE       : ins.sino.claimcar.losscar.service.DeflossService
 ******************************************************************************/
package ins.sino.claimcar.losscar.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RepairFlag;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.losscar.po.PrpLDlossCarComp;
import ins.sino.claimcar.losscar.po.PrpLDlossCarCompHis;
import ins.sino.claimcar.losscar.po.PrpLDlossCarInfo;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMain;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMainHis;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMaterial;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMaterialHis;
import ins.sino.claimcar.losscar.po.PrpLDlossCarRepair;
import ins.sino.claimcar.losscar.po.PrpLDlossCarRepairHis;
import ins.sino.claimcar.losscar.po.PrpLDlossCarSubRisk;
import ins.sino.claimcar.losscar.po.PrpLDlossCarSubRiskHis;
import ins.sino.claimcar.losscar.po.PrpLJingYouLog;
import ins.sino.claimcar.losscar.po.PrpLLossRepairSumInfo;
import ins.sino.claimcar.losscar.po.PrpLLossRepairSumInfoHis;
import ins.sino.claimcar.losscar.po.PrplLossRule;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskHisVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarSubRiskVo;
import ins.sino.claimcar.losscar.vo.PrpLJingYouLogVo;
import ins.sino.claimcar.losscar.vo.PrpLLossRepairSumInfoVo;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTrace;
import ins.sino.claimcar.lossprop.po.PrpLdlossPropMain;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service("deflossService")
public class DeflossService {
	
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	private RegistService registService;
	
	private Logger logger = LoggerFactory.getLogger(DeflossService.class);

	public PrpLDlossCarMainVo findDeflossByPk(Long id){
		PrpLDlossCarMain po = databaseDao.findByPK(PrpLDlossCarMain.class,id);
		if(po == null){
			return null;
		}
		PrpLDlossCarMainVo lossCarMainVo = Beans.copyDepth().from(po).to(PrpLDlossCarMainVo.class);
		
		return lossCarMainVo;
	}
	
	public PrpLDlossCarInfoVo findDefCarInfoByPk(Long id){
		
		PrpLDlossCarInfo po = databaseDao.findByPK(PrpLDlossCarInfo.class,id);
		if(po == null){
			return null;
		}
		PrpLDlossCarInfoVo carInfoVo = Beans.copyDepth().from(po).to(PrpLDlossCarInfoVo.class);
		
		return carInfoVo;
	}

	public Long saveOrUpdateDefCarInfo(PrpLDlossCarInfoVo carInfoVo){
		PrpLDlossCarInfo carInfo = null;
		if(carInfoVo.getId()==null){
			carInfo = new PrpLDlossCarInfo();
			Beans.copy().from(carInfoVo).to(carInfo);
		}else{
			carInfo = databaseDao.findByPK(PrpLDlossCarInfo.class,carInfoVo.getId());
			Beans.copy().from(carInfoVo).excludeNull().to(carInfo);
		}
		
//		PoPropertyUtil.setProperty(PrpLDlossCarInfo.class,carInfo);
		databaseDao.save(PrpLDlossCarInfo.class,carInfo);
		
		return carInfo.getId();
	}
	
	public void saveOrUpdateDefloss(PrpLDlossCarMainVo lossCarMainVo,String nodeCode){
		PrpLDlossCarMain lossCarMain=null;
		// id是空的直接copy对象
		if(lossCarMainVo.getId()==null){			
			logger.info("该条数据不存在，保存该条数据========》  " + " 报案号： " + lossCarMainVo.getRegistNo() + " 车牌号： " + ((lossCarMainVo.getLicenseNo()!= null) ? lossCarMainVo.getLicenseNo() : "null") + " 定损车辆表id： " + ((lossCarMainVo.getCarId() != null) ? lossCarMainVo.getCarId() : "null"));
			
			lossCarMain = Beans.copyDepth().from(lossCarMainVo).to(PrpLDlossCarMain.class);
			// 维护主子表关系
			for(PrpLDlossCarSubRisk subRisk:lossCarMain.getPrpLDlossCarSubRisks()){
				subRisk.setPrpLDlossCarMain(lossCarMain);
			}
		}else{			
			logger.info("该条数据已存在，更新该条数据========》  " + " 报案号： " + lossCarMainVo.getRegistNo() + " 车牌号： " + ((lossCarMainVo.getLicenseNo()!= null) ? lossCarMainVo.getLicenseNo() : "null") + " 定损主表id： " + ((lossCarMainVo.getId() != null) ? lossCarMainVo.getId() : "null"));

			lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, lossCarMainVo.getId());
			// 检查页面传过来的精友数据是否错误
			if("01".equals(lossCarMainVo.getCetainLossType())||"04".equals(lossCarMainVo.getCetainLossType())){
				List<PrpLDlossCarComp> compList = lossCarMain.getPrpLDlossCarComps();
				List<PrpLDlossCarMaterial> materialList = lossCarMain.getPrpLDlossCarMaterials();
				List<PrpLDlossCarRepair> repairList = lossCarMain.getPrpLDlossCarRepairs();
				List<PrpLDlossCarRepairVo> outRepairList = lossCarMainVo.getOutRepairList();
				BigDecimal sumCompFee = BigDecimal.ZERO;
				BigDecimal sumVeriCompFee = BigDecimal.ZERO;
				BigDecimal sumMatFee = BigDecimal.ZERO;
				BigDecimal sumVeriMatFee = BigDecimal.ZERO;
				BigDecimal sumRepairFee = BigDecimal.ZERO;
				BigDecimal sumVeriRepairFee = BigDecimal.ZERO;
				BigDecimal sumOutRepairFee = BigDecimal.ZERO;
				BigDecimal sumOutRepairVeriFee = BigDecimal.ZERO;
				BigDecimal sumMangenge = BigDecimal.ZERO;
				Boolean sumOutRepairVeriFlag = false;
				for(PrpLDlossCarComp comp:compList){
					sumCompFee = sumCompFee.add(NullToZero(comp.getMaterialFee()).multiply(new BigDecimal(comp.getQuantity())));
					sumVeriCompFee = sumVeriCompFee.add(NullToZero(comp.getVeriMaterFee()).multiply(new BigDecimal(NullToZero(comp.getVeriQuantity()))));
					sumMangenge=sumMangenge.add(NullToZero(comp.getManageSingleFee()));
				}
				for(PrpLDlossCarMaterial material:materialList){
					sumMatFee = sumMatFee.add(NullToZero(material.getMaterialFee()));
					sumVeriMatFee = sumVeriMatFee.add(NullToZero(material.getAuditLossMaterialFee()));
				}
				for(PrpLDlossCarRepair repair:repairList){
					if("0".equals(repair.getRepairFlag())){
						sumRepairFee = sumRepairFee.add(NullToZero(repair.getSumDefLoss()));
						sumVeriRepairFee = sumVeriRepairFee.add(NullToZero(repair.getSumVeriLoss()));
					}
				}
				if(outRepairList!=null && outRepairList.size()>0){
					for(PrpLDlossCarRepairVo outRepair:outRepairList){
						sumOutRepairFee = sumOutRepairFee.add(NullToZero(outRepair.getSumDefLoss()));
						if(outRepair.getSumVeriLoss() != null){
							sumOutRepairVeriFlag = true;
							sumOutRepairVeriFee = sumOutRepairVeriFee.add(outRepair.getSumVeriLoss());
						}
					}
				}
				if(sumCompFee.compareTo(NullToZero(lossCarMainVo.getSumCompFee()))!=0){
					lossCarMainVo.setSumCompFee(sumCompFee);
				}
				if(sumMatFee.compareTo(NullToZero(lossCarMainVo.getSumMatFee()))!=0){
					lossCarMainVo.setSumMatFee(sumMatFee);
				}
				if(sumRepairFee.compareTo(NullToZero(lossCarMainVo.getSumRepairFee()))!=0){
					lossCarMainVo.setSumRepairFee(sumRepairFee);
				}
				lossCarMainVo.setSumLossFee(sumCompFee.add(sumMatFee).add(sumRepairFee).add(sumOutRepairFee).subtract(NullToZero(lossCarMain.getSumRemnant())).add(NullToZero(sumMangenge)));
				if(sumOutRepairVeriFlag){
					lossCarMainVo.setSumVeriOutFee(sumOutRepairVeriFee);
				}
				/*if(nodeCode.startsWith(FlowNode.VLCar.name())){
					if(lossCarMain.getSumVeriLossFee()==null){
						lossCarMainVo.setSumVeriLossFee(sumCompFee.add(sumMatFee).add(sumRepairFee).
								add(sumOutRepairVeriFee).subtract(NullToZero(lossCarMain.getSumRemnant())));
					}else{
						lossCarMainVo.setSumVeriLossFee(sumVeriCompFee.add(sumVeriMatFee).add(sumVeriRepairFee).
								add(sumOutRepairVeriFee).subtract(NullToZero(lossCarMain.getSumVeriRemnant())));
					}
					if(lossCarMain.getSumVeriCompFee()==null){
						lossCarMainVo.setSumVeriCompFee(sumCompFee);
					}else{
						lossCarMainVo.setSumVeriCompFee(sumVeriCompFee);
					}
					if(lossCarMain.getSumVeriMatFee()==null){
						lossCarMainVo.setSumVeriMatFee(sumMatFee);
					}else{
						lossCarMainVo.setSumVeriMatFee(sumVeriMatFee);
					}
					if(lossCarMain.getSumVeriRepairFee()==null){
						lossCarMainVo.setSumVeriRepairFee(sumRepairFee);
					}else{
						lossCarMainVo.setSumVeriRepairFee(sumVeriRepairFee);
					}
				}*/
			}
			
			Beans.copy().from(lossCarMainVo).excludeNull().to(lossCarMain);
			
			if(!nodeCode.startsWith(FlowNode.VPCar.name()) && !nodeCode.equals(FlowNode.DLChk.name())){
				List<PrpLDlossCarSubRisk> carSubRiskList = lossCarMain.getPrpLDlossCarSubRisks();
				for(PrpLDlossCarSubRiskVo carSubRiskVo:lossCarMainVo.getPrpLDlossCarSubRisks()){
					if(carSubRiskVo.getId()==null){
						carSubRiskVo.setRegistNo(lossCarMain.getRegistNo());
					}
				}
				
				this.mergeList(lossCarMain,lossCarMainVo.getPrpLDlossCarSubRisks(),carSubRiskList,"id",PrpLDlossCarSubRisk.class,"setPrpLDlossCarMain");
				
				// List<PrpLDlossCarRepairVo> outRepariList = lossCarMainVo.getOutRepairList();
				List<PrpLDlossCarRepair> prpLDlossCarRepairs = lossCarMain.getPrpLDlossCarRepairs();
				List<PrpLDlossCarRepairVo> outRepariList = Beans.copyDepth().from(prpLDlossCarRepairs).toList(PrpLDlossCarRepairVo.class);
				List<PrpLDlossCarRepair> oldRepairList = lossCarMain.getPrpLDlossCarRepairs();
				List<Long> repairId = new ArrayList<Long>();
				// 更新外修表数据
				if(outRepariList!=null && !outRepariList.isEmpty()){
					// 增加 修改 删除
					for(PrpLDlossCarRepairVo carRepairVo : outRepariList){
						if(carRepairVo.getId()==null){// 新增
							carRepairVo.setRegistNo(lossCarMainVo.getRegistNo());
							carRepairVo.setRepairFlag("1");
							carRepairVo.setRepairCode(lossCarMainVo.getRiskCode());
							carRepairVo.setLicenseNo(lossCarMainVo.getLicenseNo());
							String kindCode = "";
							if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
									CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
								kindCode=CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
							}else{
								kindCode=CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
							}
							
							if(kindCode ==null){
								kindCode ="BZ";
							}
							carRepairVo.setKindCode(kindCode);
							carRepairVo.setKindName(CodeConstants.KINDCODE_MAP.get(kindCode));
							
							PrpLDlossCarRepair carRepair = Beans.copyDepth().from(carRepairVo).to(PrpLDlossCarRepair.class);
							carRepair.setPrpLDlossCarMain(lossCarMain);
							oldRepairList.add(carRepair);
						}else{// 修改
							for(PrpLDlossCarRepair carRepair : oldRepairList){
								if("1".equals(carRepair.getRepairFlag()) && carRepair.getId().equals(carRepairVo.getId())){
									Beans.copy().from(carRepairVo).excludeNull().to(carRepair);
									repairId.add(carRepairVo.getId());
								}
							}
						}
					}
				}
				
				if(oldRepairList!=null && !oldRepairList.isEmpty()){
					Iterator<PrpLDlossCarRepair> it = oldRepairList.iterator();
					if(it.hasNext()){
						PrpLDlossCarRepair carRepair = it.next();
						if("1".equals(carRepair.getRepairFlag()) &&  carRepair.getId()!=null &&
								(repairId.isEmpty() || !repairId.contains(carRepair.getId()))){
							oldRepairList.remove(carRepair);
							databaseDao.deleteByObject(PrpLDlossCarRepair.class,carRepair);
						}
					}
				}
			}
		}
//		PoPropertyUtil.setProperty(PrpLDlossCarMain.class,lossCarMain);
		
		databaseDao.save(PrpLDlossCarMain.class,lossCarMain);
		lossCarMainVo.setId(lossCarMain.getId());
	}
	
	
	/**
	 * 子表vOList转为 b
	 * @param lossCarMain 主表
	 * @param voList
	 * @param poList
	 * @param idName 主键
	 * @param paramClass 子表class类
	 * @param method 主子表关联方法如"setPrpLDlossCarMain"
	 * @modified: ☆yangkun(2015年12月10日 上午11:32:05): <br>
	 */
	public void mergeList(PrpLDlossCarMain lossCarMain,List voList, List poList, String idName,Class paramClass, String method){
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
					Method setMethod;
					Beans.copy().from(element).to(poElement);
					setMethod = paramClass.getDeclaredMethod(method, lossCarMain.getClass());
					setMethod.invoke(poElement,lossCarMain);
					
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}
	
	/**
	 * 更新定损主表信息 该方法只提供提交后回写数据使用（vo是查询处理的，非页面获得）
	 * 
	 * <pre></pre>
	 * @param lossCarMainVo
	 * @modified: ☆yangkun(2016年2月15日 下午4:52:18): <br>
	 */
	public void updateDefloss(PrpLDlossCarMainVo lossCarMainVo){
		PrpLDlossCarMain lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, lossCarMainVo.getId());
		Beans.copy().from(lossCarMainVo).excludeNull().to(lossCarMain);
		this.mergeList(lossCarMain,lossCarMainVo.getPrpLDlossCarSubRisks(),lossCarMain.getPrpLDlossCarSubRisks(),"id",
				PrpLDlossCarSubRisk.class,"setPrpLDlossCarMain");
		this.mergeList(lossCarMain,lossCarMainVo.getPrpLDlossCarComps(),lossCarMain.getPrpLDlossCarComps(),"id",
				PrpLDlossCarComp.class,"setPrpLDlossCarMain");
		this.mergeList(lossCarMain,lossCarMainVo.getPrpLDlossCarMaterials(),lossCarMain.getPrpLDlossCarMaterials(),"id",
				PrpLDlossCarMaterial.class,"setPrpLDlossCarMain");
		this.mergeList(lossCarMain,lossCarMainVo.getPrpLDlossCarRepairs(),lossCarMain.getPrpLDlossCarRepairs(),"id",
				PrpLDlossCarRepair.class,"setPrpLDlossCarMain");
		databaseDao.update(PrpLDlossCarMain.class,lossCarMain);
		lossCarMainVo.setId(lossCarMain.getId());
	}
	
	/**
	 * 精友定损保存数据 先删除所有的换件，辅料，修理数据，再重新生成 修改定损增加数据，不能删除 待修改 ☆yangkun(2015年12月21日 下午8:49:15): <br>
	 */
	public void saveByJyDefloss(PrpLDlossCarMainVo lossCarVo) {
		if (lossCarVo != null) {
			logger.info("定损提交报文解析完成即将进行数据拷贝，报案号：" + lossCarVo.getRegistNo() + " 修理厂ID：" + lossCarVo.getRepairFactoryCode()
					+ " 修理厂名称：" + lossCarVo.getRepairFactoryName() + " 修理厂类型：" + lossCarVo.getRepairFactoryType()
					+ " 修理厂电话：" + lossCarVo.getFactoryMobile() + "定损主表ID：" + lossCarVo.getId());
		}
		PrpLDlossCarMain lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, lossCarVo.getId());
		Beans.copy().from(lossCarVo).excludeNull().to(lossCarMain);
		if (lossCarMain != null) {
			logger.info("定损提交报文数据拷贝完成，报案号：" + lossCarMain.getRegistNo() + " 修理厂ID：" + lossCarMain.getRepairFactoryCode()
					+ " 修理厂名称：" + lossCarMain.getRepairFactoryName() + " 修理厂类型：" + lossCarMain.getRepairFactoryType()
					+ " 修理厂电话：" + lossCarMain.getFactoryMobile() + "定损主表ID：" + lossCarMain.getId());
		}
		
		this.mergeList(lossCarMain,lossCarVo.getPrpLDlossCarComps(),lossCarMain.getPrpLDlossCarComps(),"indId",
				PrpLDlossCarComp.class,"setPrpLDlossCarMain");
		if(!FlowNode.DLChk.name().equals(lossCarMain.getFlowFlag())){
			
			this.mergeList(lossCarMain,lossCarVo.getPrpLDlossCarMaterials(),lossCarMain.getPrpLDlossCarMaterials(),"assistId",
					PrpLDlossCarMaterial.class,"setPrpLDlossCarMain");
			
			this.mergeList(lossCarMain,lossCarVo.getPrpLDlossCarRepairs(),lossCarMain.getPrpLDlossCarRepairs(),"repairId",
				PrpLDlossCarRepair.class,"setPrpLDlossCarMain");
			// 修理费用包括内修外修， 精友只返回内修的
			/*List<PrpLDlossCarRepair> carRepairList = lossCarMain.getPrpLDlossCarRepairs();
			Map<String,PrpLDlossCarRepairVo> repariMap = new HashMap<String, PrpLDlossCarRepairVo>();
			for(PrpLDlossCarRepairVo repairVo : lossCarVo.getPrpLDlossCarRepairs()){
				Boolean addFlag = true;
				for(PrpLDlossCarRepair repairPo : carRepairList){
					if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairPo.getRepairFlag()) 
							&& repairVo.getRepairId().equals(repairPo.getRepairId())){
						Beans.copy().from(repairVo).excludeNull().to(repairPo);
						addFlag = false;
						continue;
					}
				}
				if(addFlag){
					PrpLDlossCarRepair repairPo  = Beans.copyDepth().from(repairVo).to(PrpLDlossCarRepair.class);
					repairPo.setPrpLDlossCarMain(lossCarMain);
					carRepairList.add(repairPo);
				}
				
				repariMap.put(repairVo.getRepairId(), repairVo);
			}
			
			for (Iterator it = carRepairList.iterator(); it.hasNext();) {
				PrpLDlossCarRepair repairPo = (PrpLDlossCarRepair)it.next();
				if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairPo.getRepairFlag())) {
					if(!repariMap.containsKey(repairPo.getRepairId())){
						databaseDao.deleteByObject(PrpLDlossCarRepair.class,repairPo);
						it.remove();
					}
				}
			}*/
			
		}

		logger.info("定损提交即将进行数据保存，报案号：" + lossCarMain.getRegistNo() + " 修理厂ID：" + lossCarMain.getRepairFactoryCode()
				+ " 修理厂名称：" + lossCarMain.getRepairFactoryName() + " 修理厂类型：" + lossCarMain.getRepairFactoryType()
				+ " 修理厂电话：" + lossCarMain.getFactoryMobile() + "定损主表ID：" + lossCarMain.getId());
		//车辆定损主表添加数据时，先检查数据库有没有该条数据，如果没有就添加；如果有，就更新
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", lossCarMain.getRegistNo());
		queryRule.addEqual("scheduleDeflossId", lossCarMain.getScheduleDeflossId());
		queryRule.addEqual("licenseNo", lossCarMain.getLicenseNo());
		List<PrpLDlossCarMain> carMain = databaseDao.findAll(PrpLDlossCarMain.class, queryRule);
		if(carMain != null && carMain.size() > 0){
			logger.info("已存在该条数据，更新该条数据=========>" + "报案号: " + lossCarMain.getRegistNo() + " 定损调度表id： " + lossCarMain.getScheduleDeflossId() + " 车牌号： " + lossCarMain.getLicenseNo());
			databaseDao.update(PrpLDlossCarMain.class, lossCarMain);
		}else{
			logger.info("不存在该条数据，可以插入该条数据=========>" + "报案号: " + lossCarMain.getRegistNo() + " 定损调度表id： " + lossCarMain.getScheduleDeflossId() + " 车牌号： " + lossCarMain.getLicenseNo());
			databaseDao.save(PrpLDlossCarMain.class,lossCarMain);
		}

		
//		databaseDao.save(PrpLDlossCarMain.class,lossCarMain);
	}
	
	/**
	 * 精友2代 全量信息保存
	 * @param prpLDlossCarMainVo
	 */
	public void saveByJy2Defloss(PrpLDlossCarMainVo prpLDlossCarMainVo) throws Exception{
		PrpLDlossCarMain prpLDlossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, prpLDlossCarMainVo.getId());
		logger.info("精友报文解析完成即将进行数据拷贝，报案号：" + prpLDlossCarMainVo.getRegistNo() + " 修理厂ID：" + prpLDlossCarMainVo.getRepairFactoryCode()
				+ " 修理厂名称：" + prpLDlossCarMainVo.getRepairFactoryName() + " 修理厂类型：" + prpLDlossCarMainVo.getRepairFactoryType()
				+ " 修理厂电话：" + prpLDlossCarMainVo.getFactoryMobile());

		Beans.copy().from(prpLDlossCarMainVo).excludeNull().to(prpLDlossCarMain);
		if (prpLDlossCarMain != null) {
			logger.info("精友报文解析完成数据拷贝完成，报案号：" + prpLDlossCarMain.getRegistNo() + " 修理厂ID：" + prpLDlossCarMain.getRepairFactoryCode()
					+ " 修理厂名称：" + prpLDlossCarMain.getRepairFactoryName() + " 修理厂类型：" + prpLDlossCarMain.getRepairFactoryType()
					+ " 修理厂电话：" + prpLDlossCarMain.getFactoryMobile());
		} else {
			logger.info("精友定损完成提交给理赔，理赔即将全量保存定损数据！id为"+ prpLDlossCarMainVo.getId() +"的prpLDlossCarMain对象为空！");
		}

		//退回定损/定损修改进入精友后清空核价信息
		prpLDlossCarMain.setSumVeripCompFee(prpLDlossCarMain.getSumCompFee());
		prpLDlossCarMain.setSumVeripMatFee(prpLDlossCarMain.getSumMatFee());
		prpLDlossCarMain.setSumVeripRemnant(prpLDlossCarMain.getSumRemnant());
		prpLDlossCarMain.setSumVeriPManage(prpLDlossCarMain.getSumManageFee());
		//BigDecimal sumVeripLoss = NullToZero(prpLDlossCarMain.getSumVeripCompFee()).add(prpLDlossCarMain.getSumVeripMatFee()).subtract(prpLDlossCarMain.getSumVeripRemnant());
		prpLDlossCarMain.setSumVeripLoss(NullToZero(prpLDlossCarMain.getSumLossFee()).subtract(NullToZero(prpLDlossCarMain.getSumRepairFee())).subtract(NullToZero(prpLDlossCarMain.getSumRescueFee())));
		prpLDlossCarMain.setVeripEnddate(null);
		//prpLDlossCarMain.setVeriPriceFlag("");
		
		//退回定损/定损修改进入精友后清空核价信息
		prpLDlossCarMain.setSumVeriCompFee(null);
		prpLDlossCarMain.setSumVeriRepairFee(null);
		prpLDlossCarMain.setSumVeriOutFee(null);
		prpLDlossCarMain.setSumVeriMatFee(null);
		prpLDlossCarMain.setSumVeriRemnant(null);
		prpLDlossCarMain.setSumVeriLossFee(null);
		//prpLDlossCarMain.setUnderWriteFlag("");   
		
		// 配件信息
		if(prpLDlossCarMain.getPrpLDlossCarComps()!=null){
			databaseDao.deleteAll(PrpLDlossCarComp.class, prpLDlossCarMain.getPrpLDlossCarComps());
		}
		List<PrpLDlossCarComp> prpLDlossCarComps = new ArrayList<PrpLDlossCarComp>();
		prpLDlossCarComps = Beans.copyDepth().from(prpLDlossCarMainVo.getPrpLDlossCarComps()).toList(PrpLDlossCarComp.class);
		if(prpLDlossCarComps!=null){
			for (PrpLDlossCarComp prpLDlossCarComp : prpLDlossCarComps) {
				prpLDlossCarComp.setVeripMaterFee(prpLDlossCarComp.getMaterialFee());
				prpLDlossCarComp.setVeripManageRate(prpLDlossCarComp.getManageSingleRate());
				prpLDlossCarComp.setVeripManageFee(prpLDlossCarComp.getManageSingleFee());
				prpLDlossCarComp.setVeripRestFee(prpLDlossCarComp.getRestFee());
				prpLDlossCarComp.setAuditCount(new BigDecimal(prpLDlossCarComp.getQuantity()));
				prpLDlossCarComp.setSumCheckLoss(prpLDlossCarComp.getSumDefLoss());
				prpLDlossCarComp.setPrpLDlossCarMain(prpLDlossCarMain);
			}
		}
		prpLDlossCarMain.setPrpLDlossCarComps(prpLDlossCarComps);
		
		// 修理信息
		if(prpLDlossCarMain.getPrpLDlossCarRepairs()!=null){
			databaseDao.deleteAll(PrpLDlossCarRepair.class, prpLDlossCarMain.getPrpLDlossCarRepairs());
		}
		List<PrpLDlossCarRepair> prpLDlossCarRepairs = new ArrayList<PrpLDlossCarRepair>();
		prpLDlossCarRepairs = Beans.copyDepth().from(prpLDlossCarMainVo.getPrpLDlossCarRepairs()).toList(PrpLDlossCarRepair.class);
		if(prpLDlossCarRepairs!=null){
			for (PrpLDlossCarRepair prpLDlossCarRepair : prpLDlossCarRepairs) {
				prpLDlossCarRepair.setPrpLDlossCarMain(prpLDlossCarMain);
			}
		}
		prpLDlossCarMain.setPrpLDlossCarRepairs(prpLDlossCarRepairs);
		
		// 辅料信息
		if(prpLDlossCarMain.getPrpLDlossCarMaterials()!=null){
			databaseDao.deleteAll(PrpLDlossCarMaterial.class, prpLDlossCarMain.getPrpLDlossCarMaterials());
		}
		List<PrpLDlossCarMaterial> prpLDlossCarMaterials = new ArrayList<PrpLDlossCarMaterial>();
		prpLDlossCarMaterials = Beans.copyDepth().from(prpLDlossCarMainVo.getPrpLDlossCarMaterials()).toList(PrpLDlossCarMaterial.class);
		if(prpLDlossCarMaterials!=null){
			for (PrpLDlossCarMaterial prpLDlossCarMaterial : prpLDlossCarMaterials) {
				prpLDlossCarMaterial.setAuditCount(prpLDlossCarMaterial.getAssisCount());
				prpLDlossCarMaterial.setAuditPrice(prpLDlossCarMaterial.getUnitPrice());
				prpLDlossCarMaterial.setAuditMaterialFee(prpLDlossCarMaterial.getMaterialFee());
				prpLDlossCarMaterial.setPrpLDlossCarMain(prpLDlossCarMain);
			}
		}
		prpLDlossCarMain.setPrpLDlossCarMaterials(prpLDlossCarMaterials);
		
		// 工种合计的修理费用
		if(prpLDlossCarMain.getPrpLLossRepairSumInfos()!=null){
			databaseDao.deleteAll(PrpLLossRepairSumInfo.class, prpLDlossCarMain.getPrpLLossRepairSumInfos());	
		}
		List<PrpLLossRepairSumInfo> prpLLossRepairSumInfos = new ArrayList<PrpLLossRepairSumInfo>();
		prpLLossRepairSumInfos = Beans.copyDepth().from(prpLDlossCarMainVo.getPrpLLossRepairSumInfoVos()).toList(PrpLLossRepairSumInfo.class);
		if(prpLLossRepairSumInfos!=null){
			for (PrpLLossRepairSumInfo prpLLossRepairSumInfo : prpLLossRepairSumInfos) {
				prpLLossRepairSumInfo.setPrpLDlossCarMain(prpLDlossCarMain);
			}
		}
		prpLDlossCarMain.setPrpLLossRepairSumInfos(prpLLossRepairSumInfos);
		
		// 风险规则信息
		if(prpLDlossCarMain.getPrplLossRule()!=null){
			databaseDao.deleteByObject(PrplLossRule.class, prpLDlossCarMain.getPrplLossRule());
		}
		PrplLossRule prplLossRule = new PrplLossRule();
		prplLossRule = Beans.copyDepth().from(prpLDlossCarMainVo.getPrplLossRuleVo()).to(PrplLossRule.class);
		if(prplLossRule!=null){
			prplLossRule.getPrplLossRuleSub().setPrplLossRule(prplLossRule);
			prplLossRule.setPrpLDlossCarMain(prpLDlossCarMain);
		}
		prpLDlossCarMain.setPrplLossRule(prplLossRule);

		logger.info("精友报文解析完成理赔进行数据保存，报案号：" + prpLDlossCarMain.getRegistNo() + " 修理厂ID：" + prpLDlossCarMain.getRepairFactoryCode()
				+ " 修理厂名称：" + prpLDlossCarMain.getRepairFactoryName() + " 修理厂类型：" + prpLDlossCarMain.getRepairFactoryType()
				+ " 修理厂电话：" + prpLDlossCarMain.getFactoryMobile());
		databaseDao.save(PrpLDlossCarMain.class,prpLDlossCarMain);
	}
		
	
	// 修改定损和复检 明细 只能修改 增加不能删除
	public void modifyJyData(PrpLDlossCarMain lossCarMain,PrpLDlossCarMainVo lossCarVo){
		List<PrpLDlossCarComp> carCompList = lossCarMain.getPrpLDlossCarComps();
		List<PrpLDlossCarMaterial> carMaterialList = lossCarMain.getPrpLDlossCarMaterials();
		List<PrpLDlossCarRepair> carRepairList = lossCarMain.getPrpLDlossCarRepairs();
		for(PrpLDlossCarCompVo compVo : lossCarVo.getPrpLDlossCarComps()){
			Boolean addFlag = true;
			for(PrpLDlossCarComp comPo : carCompList){
				if(compVo.getIndId().equals(comPo.getIndId())){
					Beans.copy().from(compVo).excludeNull().to(comPo);
					addFlag = false;
					continue;
				}
			}
			if(addFlag){
				PrpLDlossCarComp comPo = Beans.copyDepth().from(compVo).to(PrpLDlossCarComp.class);
				carCompList.add(comPo);
			}
		}
		if(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossCarMain.getDeflossSourceFlag())){
			for(PrpLDlossCarMaterialVo materialVo : lossCarVo.getPrpLDlossCarMaterials()){
				Boolean addFlag = true;
				for(PrpLDlossCarMaterial materialPo : carMaterialList){
					if(materialVo.getAssistId().equals(materialPo.getAssistId())){
						Beans.copy().from(materialVo).excludeNull().to(materialPo);
						addFlag = false;
						continue;
					}
				}
				if(addFlag){
					PrpLDlossCarMaterial materialPo = Beans.copyDepth().from(materialVo).to(PrpLDlossCarMaterial.class);
					carMaterialList.add(materialPo);
				}
			}
			
			for(PrpLDlossCarRepairVo repairVo : lossCarVo.getPrpLDlossCarRepairs()){
				Boolean addFlag = true;
				for(PrpLDlossCarRepair repairPo : carRepairList){
					if(repairVo.getRepairId().equals(repairPo.getRepairId()) && 
							repairVo.getRepairFlag().equals(repairPo.getRepairFlag())){
						Beans.copy().from(repairVo).excludeNull().to(repairPo);
						addFlag = false;
						continue;
					}
				}
				if(addFlag){
					PrpLDlossCarRepair repairPo = Beans.copyDepth().from(repairVo).to(PrpLDlossCarRepair.class);
					carRepairList.add(repairPo);
				}
			}
		}
	}
	
	/**
	 * 精友核价,核损保存数据 CheckLoss :核损 , CheckPrice： 核价 ☆yangkun(2015年12月25日 下午5:13:48): <br>
	 */
	public void saveByJyDeflossCheck(PrpLDlossCarMainVo lossCarMainVo,String type) {
		PrpLDlossCarMain lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, lossCarMainVo.getId());
		List<PrpLDlossCarComp> carCompList = lossCarMain.getPrpLDlossCarComps();
		List<PrpLDlossCarMaterial> carMaterialList = lossCarMain.getPrpLDlossCarMaterials();
		List<PrpLDlossCarRepair> carRepairList = lossCarMain.getPrpLDlossCarRepairs();
		
		Beans.copy().from(lossCarMainVo).excludeNull().to(lossCarMain);
		for(PrpLDlossCarCompVo compVo : lossCarMainVo.getPrpLDlossCarComps()){
			for(PrpLDlossCarComp comPo : carCompList){
				if(compVo.getIndId().equals(comPo.getIndId())){
					Beans.copy().from(compVo).excludeNull().to(comPo);
					continue;
				}
			}
		}
		
		for(PrpLDlossCarMaterialVo materialVo : lossCarMainVo.getPrpLDlossCarMaterials()){
			for(PrpLDlossCarMaterial materialPo : carMaterialList){
				if(materialVo.getAssistId().equals(materialPo.getAssistId())){
					Beans.copy().from(materialVo).excludeNull().to(materialPo);
					continue;
				}
			}
		}
		
		if("CheckLoss".equals(type)){// 核损
			for(PrpLDlossCarRepairVo repairVo : lossCarMainVo.getPrpLDlossCarRepairs()){
				for(PrpLDlossCarRepair repairPo : carRepairList){
					if(repairVo.getRepairId().equals(repairPo.getRepairId())){
						
						Beans.copy().from(repairVo).excludeNull().to(repairPo);
						continue;
					}
				}
			}
			PrpLRegistVo registVo = registService.findRegistByRegistNo(lossCarMain.getRegistNo());
			String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
			Date timeStamp = null;
			try {
				timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if( !( registVo.getReportTime().getTime()>timeStamp.getTime() )){// 如果报案日期大于时间戳 精友二代
				// 精友记的总金额加上理赔的外修金额
				BigDecimal sumVirLoss = lossCarMainVo.getSumVeriLossFee();
				sumVirLoss = sumVirLoss.add(DataUtils.NullToZero(lossCarMain.getSumVeriOutFee()));
				lossCarMain.setSumVeriLossFee(sumVirLoss);
			}
		}
		databaseDao.save(PrpLDlossCarMain.class,lossCarMain);
	}
	
	/**
	 * 通过报案号和车辆序号查找定损信息 ☆yangkun(2016年1月21日 下午6:37:23): <br>
	 */
	public List<PrpLDlossCarMainVo> findLossCarMainBySerialNo(String registNo,Integer serialNo){
		List<PrpLDlossCarMainVo> lossCarMainVoList = new ArrayList<PrpLDlossCarMainVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		
		List<PrpLDlossCarMain> lossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
			lossCarMainVoList = Beans.copyDepth().from(lossCarMainList).toList(PrpLDlossCarMainVo.class);
		}
		
		return lossCarMainVoList;
	}
	
	/**
	 * 通过报案号和车辆序号查找定损信息 ☆yangkun(2016年1月21日 下午6:37:23): <br>
	 */
	public List<PrpLDlossCarMainVo> findLossCarNoComp(String registNo,Integer serialNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("serialNo",serialNo);
		queryRule.addEqual("lossState", "00");
		
		List<PrpLDlossCarMain> lossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		List<PrpLDlossCarMainVo> lossCarMainVoList = Beans.copyDepth().from(lossCarMainList).toList(PrpLDlossCarMainVo.class);
		
		return lossCarMainVoList;
	}
	
	
	public List<PrpLDlossCarMainVo> findLossCarMainByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addNotEqual("underWriteFlag", CodeConstants.VeriFlag.CANCEL);
		List<PrpLDlossCarMain> lossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		List<PrpLDlossCarMainVo> lossCarMainVoList = Beans.copyDepth().from(lossCarMainList).toList(PrpLDlossCarMainVo.class);
		
		return lossCarMainVoList;
	}
	
	public List<PrpLDlossCarInfoVo> findLossCarInfoByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		
		List<PrpLDlossCarInfo> carInfoList = databaseDao.findAll(PrpLDlossCarInfo.class,queryRule);
		List<PrpLDlossCarInfoVo> lossCarMainVoList = Beans.copyDepth().from(carInfoList).toList(PrpLDlossCarInfoVo.class);
		
		return lossCarMainVoList;
	}
	
	/**
	 * 保存轨迹表 ☆yangkun(2016年1月27日 下午6:29:57): <br>
	 */
	public void saveDeflossHis(PrpLDlossCarMainVo lossCarMainVo,String nodeCode) {
		PrpLDlossCarMainHis lossMainHis = new PrpLDlossCarMainHis();
		Beans.copy().from(lossCarMainVo).to(lossMainHis);
		lossMainHis.setId(null);
		lossMainHis.setLossCarMainId(lossCarMainVo.getId());
		lossMainHis.setNodeCode(nodeCode);
		lossMainHis.setCreateTime(lossCarMainVo.getUpdateTime());
		lossMainHis.setCreateUser(lossCarMainVo.getUpdateUser());
		
		List<PrpLDlossCarCompVo> compList = lossCarMainVo.getPrpLDlossCarComps();
		List<PrpLDlossCarCompHis> compHisList = Beans.copyDepth().from(compList).toList(PrpLDlossCarCompHis.class);
		for(PrpLDlossCarCompHis compHis : compHisList){
			compHis.setCarCompId(compHis.getId());
			compHis.setId(null);
			compHis.setPrpLDlossCarMainHis(lossMainHis);
		}
		lossMainHis.setPrpLDlossCarCompHises(compHisList);
		
		List<PrpLDlossCarMaterialVo> materialList = lossCarMainVo.getPrpLDlossCarMaterials();
		List<PrpLDlossCarMaterialHis> materHisList = Beans.copyDepth().from(materialList).toList(PrpLDlossCarMaterialHis.class);
		for(PrpLDlossCarMaterialHis materialHis : materHisList){
			materialHis.setMaterialId(materialHis.getId());
			materialHis.setId(null);
			materialHis.setPrpLDlossCarMainHis(lossMainHis);
		}
		lossMainHis.setPrpLDlossCarMaterialHises(materHisList);
		
		List<PrpLDlossCarRepairVo> repairList = lossCarMainVo.getPrpLDlossCarRepairs();
		List<PrpLDlossCarRepairHis> repairHisList = Beans.copyDepth().from(repairList).toList(PrpLDlossCarRepairHis.class);
		for(PrpLDlossCarRepairHis repairHis : repairHisList){
			repairHis.setCarRepairId(repairHis.getId());
			repairHis.setId(null);
			repairHis.setPrpLDlossCarMainHis(lossMainHis);
		}
		lossMainHis.setPrpLDlossCarRepairHises(repairHisList);
		
		List<PrpLLossRepairSumInfoVo> repairSumList = lossCarMainVo.getPrpLLossRepairSumInfoVos();
		List<PrpLLossRepairSumInfoHis> repairSumHisList = Beans.copyDepth().from(repairSumList).toList(PrpLLossRepairSumInfoHis.class);
		for (PrpLLossRepairSumInfoHis RepairSumHis : repairSumHisList) {
			RepairSumHis.setId(null);
			RepairSumHis.setPrpLDlossCarMainHis(lossMainHis);
		}
		
		List<PrpLDlossCarSubRiskVo> subRiskList = lossCarMainVo.getPrpLDlossCarSubRisks();
		List<PrpLDlossCarSubRiskHis> subRiskHisList = Beans.copyDepth().from(subRiskList).toList(PrpLDlossCarSubRiskHis.class);
		for(PrpLDlossCarSubRiskHis subRiskHis : subRiskHisList){
			subRiskHis.setSubRiskId(subRiskHis.getId());
			subRiskHis.setId(null);
			subRiskHis.setPrpLDlossCarMainHis(lossMainHis);
		}
		lossMainHis.setPrpLDlossCarSubRiskHises(subRiskHisList);
		
		databaseDao.save(PrpLDlossCarMainHis.class,lossMainHis);
		
	}

	public List<PrpLDlossCarMainHisVo> findDeflossHisByMainId(Long defLossMainId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("lossCarMainId",defLossMainId);
		queryRule.addAscOrder("createTime");
		
		List<PrpLDlossCarMainHis> carMainHisList = databaseDao.findAll(PrpLDlossCarMainHis.class,queryRule);
		List<PrpLDlossCarMainHisVo> carMainHisVoList = new ArrayList<PrpLDlossCarMainHisVo>();
		
		carMainHisVoList = Beans.copyDepth().from(carMainHisList).toList(PrpLDlossCarMainHisVo.class);
		
		
		return carMainHisVoList;
	}
	
	/**
	 * 根据车辆主表ID查询轨迹表，返回创建日期最新的一条数据，没有数据则返回空
	 * @param defLossMainId
	 * @return
	 */
	public PrpLDlossCarMainHisVo findDeflossHisVo(Long defLossMainId){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("lossCarMainId",defLossMainId);
		queryRule.addDescOrder("createTime");
		List<PrpLDlossCarMainHis> carMainHisList = databaseDao.findAll(PrpLDlossCarMainHis.class,queryRule);
		
		if(carMainHisList != null && carMainHisList.size() > 0){
			PrpLDlossCarMainHisVo lossCarMainHisVo = 
					Beans.copyDepth().from(carMainHisList.get(0)).to(PrpLDlossCarMainHisVo.class);
			return lossCarMainHisVo;
		}
		
		return null;
	}

	/**
	 * 查找轨迹表 上上次核价 信息
	 * 
	 * <pre></pre>
	 * @param defLossMainId
	 * @return
	 * @modified: *牛强(2017年5月25日 上午10:30:35): <br>
	 */
	   public PrpLDlossCarMainHisVo findLastVerPriceDeflossHisVo(Long defLossMainId){
	        QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("lossCarMainId",defLossMainId);
	        queryRule.addLike("nodeCode","VPCar%");
	        queryRule.addDescOrder("createTime");
	       
	        List<PrpLDlossCarMainHis> carMainHisList = databaseDao.findAll(PrpLDlossCarMainHis.class,queryRule);
	        
	        if(carMainHisList != null && carMainHisList.size() >=2){
	            PrpLDlossCarMainHisVo lossCarMainHisVo = 
	                    Beans.copyDepth().from(carMainHisList.get(1)).to(PrpLDlossCarMainHisVo.class);
	            return lossCarMainHisVo;
	        }
	        
	        return null;
	    }
	   
	/**
	 * 查找定损轨迹 第一次核损信息
	 * 
	 * <pre></pre>
	 * @param defLossMainId
	 * @return
	 * @modified: *牛强(2017年5月26日 上午11:24:04): <br>
	 */
	   public PrpLDlossCarMainHisVo findLastVerLossDeflossHisVo(Long defLossMainId){
           QueryRule queryRule = QueryRule.getInstance();
           queryRule.addEqual("lossCarMainId",defLossMainId);
           queryRule.addLike("nodeCode","VLCar%");
           queryRule.addDescOrder("createTime");
           List<PrpLDlossCarMainHis> carMainHisList = databaseDao.findAll(PrpLDlossCarMainHis.class,queryRule);
           
           if(carMainHisList != null && carMainHisList.size() >= 2){
               PrpLDlossCarMainHisVo lossCarMainHisVo = 
                       Beans.copyDepth().from(carMainHisList.get(1)).to(PrpLDlossCarMainHisVo.class);
               return lossCarMainHisVo;
           }
           
           return null;
       }
	
	/**
	 * 轨迹表查找配件信息 ☆yangkun(2016年1月28日 下午2:44:53): <br>
	 */
	public PrpLDlossCarCompHisVo findCompHisByMainHisId(Long lossMainHisId,Long carCompId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("prpLDlossCarMainHis.id",lossMainHisId);
		queryRule.addEqual("carCompId",carCompId);
		
		PrpLDlossCarCompHis carMainHis = databaseDao.findUnique(PrpLDlossCarCompHis.class,queryRule);
		if(carMainHis == null){
			return null;
		}
		
		PrpLDlossCarCompHisVo carMainHisVo = Beans.copyDepth().from(carMainHis).to(PrpLDlossCarCompHisVo.class);
		
		return carMainHisVo;
	}
	
	/**
	 * 轨迹表查找辅料信息 ☆yangkun(2016年1月28日 下午2:44:53): <br>
	 */
	public PrpLDlossCarMaterialHisVo findMaterialHisByMainHisId(Long lossMainHisId,Long materialId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("prpLDlossCarMainHis.id",lossMainHisId);
		queryRule.addEqual("materialId",materialId);
		
		PrpLDlossCarMaterialHis materialHis = databaseDao.findUnique(PrpLDlossCarMaterialHis.class,queryRule);
		if(materialHis == null){
			return null;
		}
		
		PrpLDlossCarMaterialHisVo carMainHisVo = Beans.copyDepth().from(materialHis).to(PrpLDlossCarMaterialHisVo.class);
		
		return carMainHisVo;
	}
	
	/**
	 * 轨迹表查找修理信息 ☆yangkun(2016年1月28日 下午2:44:53): <br>
	 */
	public PrpLDlossCarRepairHisVo findRepairHisByMainHisId(Long lossMainHisId,Long carRepairId) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("prpLDlossCarMainHis.id",lossMainHisId);
		queryRule.addEqual("carRepairId",carRepairId);
		
		PrpLDlossCarRepairHis carRepairHis = databaseDao.findUnique(PrpLDlossCarRepairHis.class,queryRule);
		if(carRepairHis == null){
			return null;
		}
		
		PrpLDlossCarRepairHisVo carRepairHisVo = Beans.copyDepth().from(carRepairHis).to(PrpLDlossCarRepairHisVo.class);
		
		return carRepairHisVo;
	}
	
	public PrpLDlossCarSubRiskHisVo findSubRiskHisByMainHisId(Long lossMainHisId,Long carSubRiskId){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("prpLDlossCarMainHis.id",lossMainHisId);
		queryRule.addEqual("subRiskId",carSubRiskId);
		
		PrpLDlossCarSubRiskHis carSubRiskHis = databaseDao.findUnique(PrpLDlossCarSubRiskHis.class,queryRule);
		if(carSubRiskHis == null){
			return null;
		}
		
		PrpLDlossCarSubRiskHisVo carSubRiskHisVo = Beans.copyDepth().from(carSubRiskHis).to(PrpLDlossCarSubRiskHisVo.class);
		
		return carSubRiskHisVo;
	}

	/**
	 * 保存精友交互日志 ☆yangkun(2016年2月20日 下午7:51:54): <br>
	 */
	public void saveLog(PrpLJingYouLogVo logVo) {
		PrpLJingYouLog jyLog = Beans.copyDepth().from(logVo).to(PrpLJingYouLog.class);
		databaseDao.save(PrpLJingYouLog.class,jyLog);
		
	}
	
	public void updateLossCarInfo(PrpLDlossCarInfoVo lossCarInfo){
		PrpLDlossCarInfo lossCarInfoPo = new PrpLDlossCarInfo();
		Beans.copy().from(lossCarInfo).to(lossCarInfoPo);
		databaseDao.update(PrpLDlossCarInfo.class, lossCarInfoPo);
	}
	
	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}
	
	public static Integer NullToZero(Integer num){
		if(num == null){
			return new Integer("0");
		}
		return num;
	}
	
   public void updateLossCarMain(PrpLDlossCarMain lossCarMain){
        databaseDao.update(PrpLDlossCarMain.class,lossCarMain);
    }
   
   public void updateDlossPersTrace(PrpLDlossPersTrace dlossPersTrace){
       databaseDao.update(PrpLDlossPersTrace.class,dlossPersTrace);
   }
   
   public void updateDlossPropMain(PrpLdlossPropMain dlossPropMain){
       databaseDao.update(PrpLDlossPersTrace.class,dlossPropMain);
   }
   
	public boolean findCarMaterialByLicenseNo(String licenseNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("licenseNo",licenseNo);
		
		List<PrpLDlossCarMaterial> prpLDlossCarMaterials = databaseDao.findAll(PrpLDlossCarMaterial.class,queryRule);
		if(prpLDlossCarMaterials != null && prpLDlossCarMaterials.size() > 0){
			return true;
		}
		List<PrpLDlossCarComp> prpLDlossCarComps = databaseDao.findAll(PrpLDlossCarComp.class,queryRule);
		if(prpLDlossCarComps != null && prpLDlossCarComps.size() > 0){
			return true;
		}
		List<PrpLDlossCarRepair> prpLDlossCarRepairs = databaseDao.findAll(PrpLDlossCarRepair.class,queryRule);
		if(prpLDlossCarRepairs != null && prpLDlossCarRepairs.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 查询PrpLDlossCarMain
	 * @param registNo
	 * @param serialNo
	 * @param licenseNo
	 * @return
	 */
	public List<PrpLDlossCarMainVo> findPrpLDlossCarMainVoByOther(String registNo,Integer serialNo,String licenseNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		if(StringUtils.isNotBlank(licenseNo)){// 车牌号
			queryRule.addEqual("licenseNo",licenseNo);
		}
		if(serialNo!=null){// 类型
			if(serialNo==1){// 标的
				queryRule.addEqual("serialNo", 1);
			}else{// 三者
				queryRule.addNotEqual("serialNo", 1);
			}
		}
		List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = new ArrayList<PrpLDlossCarMainVo>();
		
		List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		if(prpLDlossCarMainList != null && !prpLDlossCarMainList.isEmpty()){
			prpLDlossCarMainVoList = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
		}
		return prpLDlossCarMainVoList;
	
	}

	public List<PrpLDlossCarMainVo> findLossCarMainByUnderWriteFlag(String registNo,String underWriteFlag,String flag){
		 List<PrpLDlossCarMainVo> lossCarMainVoList = new ArrayList<PrpLDlossCarMainVo>();
		 QueryRule queryRule = QueryRule.getInstance();
		 queryRule.addEqual("registNo",registNo);
		 if("1".equals(flag)){
		 queryRule.addEqual("underWriteFlag",underWriteFlag);
		 }else if("0".equals(flag)){
		 queryRule.addNotEqual("underWriteFlag",underWriteFlag);
		 }
		 List<PrpLDlossCarMain> prpLDlossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		 if(prpLDlossCarMainList!=null&& !prpLDlossCarMainList.isEmpty()){
		 lossCarMainVoList = Beans.copyDepth().from(prpLDlossCarMainList).toList(PrpLDlossCarMainVo.class);
		 }
		 return lossCarMainVoList;
	}

	public List<PrpLDlossCarMainVo> findLossCarMainInfoByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLDlossCarMain> lossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		List<PrpLDlossCarMainVo> lossCarMainVoList = Beans.copyDepth().from(lossCarMainList).toList(PrpLDlossCarMainVo.class);
		return lossCarMainVoList;
	}
	
	public List<PrpLDlossCarMainVo> findAllLossCarMainInfoByRegistNo(List<String> registNos){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registNos);
		List<PrpLDlossCarMain> lossCarMainList = databaseDao.findAll(PrpLDlossCarMain.class,queryRule);
		List<PrpLDlossCarMainVo> lossCarMainVoList = new ArrayList<PrpLDlossCarMainVo>();
		if(lossCarMainList != null && !lossCarMainList.isEmpty()){
			for(PrpLDlossCarMain main : lossCarMainList){
				PrpLDlossCarMainVo vo = new PrpLDlossCarMainVo();
				Beans.copy().from(main).to(vo);
				lossCarMainVoList.add(vo);
			}
		}
		return lossCarMainVoList;
	}
	
	public void saveOrUpdateCarComp(List<PrpLDlossCarCompVo> prpLDlossCarCompVos,PrpLDlossCarMainVo dlossCarMainVo){
		List<PrpLDlossCarComp> prpLDlossCarCompList = new ArrayList<PrpLDlossCarComp>();
		prpLDlossCarCompList = Beans.copyDepth().from(prpLDlossCarCompVos).toList(PrpLDlossCarComp.class);
		if(prpLDlossCarCompList != null && prpLDlossCarCompList.size() > 0){
			PrpLDlossCarMain lossCarMain = Beans.copyDepth().from(dlossCarMainVo).to(PrpLDlossCarMain.class);
			for(PrpLDlossCarComp prpLDlossCarComp : prpLDlossCarCompList){
				prpLDlossCarComp.setPrpLDlossCarMain(lossCarMain);
				databaseDao.update(PrpLDlossCarComp.class, prpLDlossCarComp);
			}
		}
		
	}

}
