package ins.sino.claimcar.regist.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.manager.vo.PrpLInsuredFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.regist.po.PrpCItemCar;
import ins.sino.claimcar.regist.po.PrpCMain;
import ins.sino.claimcar.regist.po.PrpDDeductCond;
import ins.sino.claimcar.regist.po.PrpLCItemKind;
import ins.sino.claimcar.regist.po.PrpLCMain;
import ins.sino.claimcar.regist.po.PrpLClaimDeduct;
import ins.sino.claimcar.regist.po.PrpLRegist;
import ins.sino.claimcar.regist.po.PrpLRegistAvg;
import ins.sino.claimcar.regist.po.PrpLRegistCarLoss;
import ins.sino.claimcar.regist.po.PrpLRegistExt;
import ins.sino.claimcar.regist.po.PrpLRegistPersonLoss;
import ins.sino.claimcar.regist.po.PrpLRegistPropLoss;
import ins.sino.claimcar.regist.po.PrpLRegistRelationshipHis;
import ins.sino.claimcar.regist.po.PrpLRegistRiskInfo;
import ins.sino.claimcar.regist.po.PrplOldClaimRiskInfo;
import ins.sino.claimcar.regist.vo.BiCiPolicyVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAndCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistAvgVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo;
import ins.sino.claimcar.regist.vo.PrpLRegistRiskInfoVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrplOldClaimRiskInfoVo;
import ins.sino.claimcar.registinter.quickclaim.service.PersonnelInformationService;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.sms.service.SmsService;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 
 * @author ZhangYu
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path(value = "registService")
public class RegistServiceImpl implements RegistService {
	public Logger logger = LoggerFactory.getLogger(RegistServiceImpl.class);
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BillNoService billNoService;
	@Autowired
	ScheduleService scheduleService;  
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	PersonnelInformationService personnelInformationService;
	@Autowired
	SendMsgService sendMsgService;
	@Autowired
	SmsService smsService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	RepairFactoryService repairFactoryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	MsgModelService msgModelService;
	@Autowired
    BaseDaoService baseDaoService;
	@Autowired
	QuickUserService quickUserService;
	@Autowired
	RegistQueryService registQueryService;
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#saveOrUpdate(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public PrpLRegistVo saveOrUpdate(PrpLRegistVo prpLRegistVo) {
		//firstregusercode
		// 将vo拷贝到po
		PrpLRegist prpLRegist = Beans.copyDepth().from(prpLRegistVo).to(PrpLRegist.class);
		
		
		PrpLRegistExt registExt = prpLRegist.getPrpLRegistExt();
		// 主表新增，子表新增
		if ( StringUtils.isEmpty(prpLRegist.getRegistTaskFlag()) ) {
			if (StringUtils.isEmpty(prpLRegist.getRegistNo())) {
				// 生成报案号
				String registNo = billNoService.getRegistNo(prpLRegist.getComCode(),prpLRegist.getRiskCode());
				prpLRegist.setRegistNo(registNo);
			}
			// 生成工作流号
			String flowNo = billNoService.getFlowId(prpLRegist.getComCode(),prpLRegist.getRiskCode());
			// 将报案号和工作流号赋值给
			prpLRegist.setFlowId(flowNo);
			if (StringUtils.isEmpty(prpLRegist.getRegistTaskFlag())) {
				prpLRegist.setRegistTaskFlag("0");
			}
			
			registExt.setRegistNo(prpLRegist.getRegistNo());
			registExt.setPrpLRegist(prpLRegist);
			
			//判断 是否现场报案 字段是否为空，避免为空时，导致无法进入精友定损系统；默认页面传‘1’过来
			if(registExt.getIsOnSitReport() != null){
				logger.info("报案号： " + (prpLRegist.getRegistNo() != null ? prpLRegist.getRegistNo():"null") + " 是否现场报案： " + registExt.getIsOnSitReport());
			}else{
				registExt.setIsOnSitReport("1");
			}

			
			// 判断是否有车损，如果没有，则只保存标的车车辆信息
			if (registExt.getIsCarLoss() == null || registExt.getIsCarLoss().equals("0")) {
				List<PrpLRegistCarLoss> list = new ArrayList<PrpLRegistCarLoss>();
				prpLRegist.getPrpLRegistCarLosses().get(0).setPrpLRegist(prpLRegist);
				list.add(prpLRegist.getPrpLRegistCarLosses().get(0));
				prpLRegist.setPrpLRegistCarLosses(list);
				// 判断是否有车损，如果有，则迭代所有车损信息设置级联关系
			} else if (prpLRegist.getPrpLRegistCarLosses() != null) {
				for (PrpLRegistCarLoss carLossPo : prpLRegist.getPrpLRegistCarLosses()) {
					if (carLossPo != null) {
						carLossPo.setPrpLRegist(prpLRegist);
					}
				}
			}
			
			// 判断是否有人伤，如果没有，则不保存人伤信息
			if (registExt.getIsPersonLoss() == null || registExt.getIsPersonLoss().equals("0")) {
				prpLRegist.setPrpLRegistPersonLosses(null);
				// 判断是否有人伤，如果有，则保存伤亡数量信息，迭代设置级联关系
			} else if (prpLRegist.getPrpLRegistPersonLosses() != null) {
				for (PrpLRegistPersonLoss personLossPo : prpLRegist.getPrpLRegistPersonLosses()) {
					if (personLossPo != null) {
						personLossPo.setPrpLRegist(prpLRegist);
					}
				}
			}
			
			// 判断是否有物损，如果没有，则不保存物损信息
			if (registExt.getIsPropLoss() == null || registExt.getIsPropLoss().equals("0")) {
				prpLRegist.setPrpLRegistPropLosses(null);
				// 判断是否有物损，如果没有，则保存物损信息，迭代设置级联关系
			} else if (prpLRegist.getPrpLRegistPropLosses() != null) {
				for (PrpLRegistPropLoss propLossPo : prpLRegist.getPrpLRegistPropLosses()) {
					if (propLossPo != null) {
						propLossPo.setPrpLRegist(prpLRegist);
					}
				}
			}
			databaseDao.save(PrpLRegist.class, prpLRegist);
		//	databaseDao.save(PrpLRegistExt.class, registExt);
			prpLRegistVo = Beans.copyDepth().from(prpLRegist).to(PrpLRegistVo.class);
			// 主表更新，子表新增、更新、删除
		} else {
			PrpLRegist prpLRegistPo = databaseDao.findByPK(PrpLRegist.class, prpLRegistVo.getRegistNo());
			Beans.copy().from(prpLRegistVo).excludeNull().to(prpLRegistPo);

			
			Beans.copy().from(prpLRegistVo.getPrpLRegistExt()).excludeNull().to(prpLRegistPo.getPrpLRegistExt());
			//if(prpLRegistVo.getPrpLRegistCarLosses() != null ){
				mergeList(prpLRegistPo, prpLRegistVo.getPrpLRegistCarLosses(), prpLRegistPo.getPrpLRegistCarLosses(), "id", PrpLRegistCarLoss.class, "setPrpLRegist");
			//}
			//if(prpLRegistVo.getPrpLRegistPersonLosses() != null ){
				mergeList(prpLRegistPo, prpLRegistVo.getPrpLRegistPersonLosses(), prpLRegistPo.getPrpLRegistPersonLosses(), "id", PrpLRegistPersonLoss.class, "setPrpLRegist");
			//}
			//if(prpLRegistVo.getPrpLRegistPropLosses() != null ){
				mergeList(prpLRegistPo, prpLRegistVo.getPrpLRegistPropLosses(), prpLRegistPo.getPrpLRegistPropLosses(), "id", PrpLRegistPropLoss.class, "setPrpLRegist");
			//}
//			prpLRegist.setPrpLRegistCarLosses(prpLRegistPo.getPrpLRegistCarLosses());
//			prpLRegist.setPrpLRegistPropLosses(prpLRegistPo.getPrpLRegistPropLosses());
//			prpLRegist.setPrpLRegistPersonLosses(prpLRegistPo.getPrpLRegistPersonLosses());
//			databaseDao.clear();
			//System.out.println("prpLRegistVo.getPrpLRegistCarLosses().size()"+prpLRegistVo.getPrpLRegistCarLosses().size());
			/*	for(int i=0;i < prpLRegistVo.getPrpLRegistCarLosses().size();i++){
					if(!"其他".equals(prpLRegistVo.getPrpLRegistCarLosses().get(i).getLoss())){
						prpLRegistPo.getPrpLRegistCarLosses().get(i).setLossremark(null);
					}
				}*/
			if(prpLRegistVo.getPrpLRegistCarLosses() != null ){
				for(PrpLRegistCarLossVo vo:prpLRegistVo.getPrpLRegistCarLosses()){
					if( !"其他".equals(vo.getLoss())){
						vo.setLossremark(null);
					}
				}
			}
			if(prpLRegistVo.getPrpLRegistExt().getPolicyNoLink()==null){
				prpLRegistPo.getPrpLRegistExt().setPolicyNoLink(prpLRegistVo.getPrpLRegistExt().getPolicyNoLink());
			}
			databaseDao.update(PrpLRegist.class,prpLRegistPo);
//			databaseDao.update(PrpLRegistExt.class, registExt);
			prpLRegistVo = Beans.copyDepth().from(prpLRegistPo).to(PrpLRegistVo.class);
		}
		
		return prpLRegistVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#mergeList(ins.sino.claimcar.regist.po.PrpLRegist, java.util.List, java.util.List, java.lang.String, java.lang.Class, java.lang.String)
	 */
	private void mergeList(PrpLRegist prpLRegist,List voList, List poList, String idName,Class paramClass, String method){
		Map<Object, Object> map = new HashMap<Object, Object>();
		Map<Integer, Object> keyMap = new HashMap<Integer, Object>();
		Map<Object, Object> poMap = new HashMap<Object, Object>();

		if (CollectionUtils.isNotEmpty(voList)) {
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
		}

		if (CollectionUtils.isNotEmpty(poList)) {
			for (Iterator it = poList.iterator(); it.hasNext(); ) {
				Object element = (Object) it.next();
				try {
					if (element == null) {
						continue;
					}
					Object key = PropertyUtils.getProperty(element, idName);
					poMap.put(key, null);
					if (!map.containsKey(key)) {
						//delete(element);
						databaseDao.deleteByObject(paramClass, element);
						it.remove();
					} else {
						Beans.copy().from(map.get(key)).excludeNull().to(element);
					}
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(voList)) {
			for (int i = 0, count = voList.size(); i < count; i++) {
				Object element = voList.get(i);
				if (element == null) {
					continue;
				}
				try {
					Object poElement = paramClass.newInstance();
					Object key = keyMap.get(i);
					if (key == null || !poMap.containsKey(key)) {
						Method setMethod;
						Beans.copy().from(element).to(poElement);
						setMethod = paramClass.getDeclaredMethod(method, prpLRegist.getClass());
						setMethod.invoke(poElement, prpLRegist);

						poList.add(poElement);
					}
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findRegistByRegistNo(java.lang.String)
	 */
	@Override
	public PrpLRegistVo findRegistByRegistNo(String registNo) {
		PrpLRegist registPo = databaseDao.findByPK(PrpLRegist.class, registNo);
		PrpLRegistVo vo = Beans.copyDepth().from(registPo).to(PrpLRegistVo.class);
		return vo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#submitSchedule(ins.sino.claimcar.regist.vo.PrpLRegistVo, ins.sino.claimcar.flow.vo.WfTaskSubmitVo, ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo)
	 */
	@Override
	public void submitSchedule(PrpLRegistVo registVo, WfTaskSubmitVo submitVo, PrpLScheduleTaskVo scheduleTaskVo,String url) throws Exception {
		
		List<PrplQuickUserVo> checkPerson = null;
		// 拼装调度表信息
		scheduleTaskVo = supplementInfo(registVo, scheduleTaskVo,url);
		if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
			checkPerson = getGBCheckPerson(registVo,scheduleTaskVo.getPrpLScheduleItemses(),scheduleTaskVo);
		}
		scheduleService.saveScheduleTaskByVo(scheduleTaskVo);
		// 更新报案信息
		registVo.setIsPeopleflag("0");
		saveOrUpdate(registVo);
		if(checkPerson!=null && !checkPerson.isEmpty()){
			for(PrplQuickUserVo userVo : checkPerson){
				quickUserService.updateTimes(userVo);
			}
		}
		//return taskVo;
	}

	/**
	 * 拼装调度表信息
	 * @param registVo
	 * @param scheduleTaskVo
	 * @return
	 * @throws Exception
	 */
	private PrpLScheduleTaskVo supplementInfo(PrpLRegistVo registVo, PrpLScheduleTaskVo scheduleTaskVo,String url) throws Exception {
		
		// 设置调度主表信息
		String checkAddressCode = registVo.getPrpLRegistExt().getCheckAddressCode();
		scheduleTaskVo.setRegistNo(registVo.getRegistNo());
		scheduleTaskVo.setLinkerMan(registVo.getLinkerName());
		scheduleTaskVo.setLinkerManPhone(registVo.getLinkerMobile());
		scheduleTaskVo.setDamageAreaCode(registVo.getDamageAreaCode());
		scheduleTaskVo.setDamageAddress(registVo.getDamageAddress());
		scheduleTaskVo.setCheckorDeflossAreaCode(checkAddressCode);
		scheduleTaskVo.setLinkerManPhone2(registVo.getLinkerPhone());
		scheduleTaskVo.setCheckAddress(registVo.getPrpLRegistExt().getCheckAddress());
		scheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.NOT_SCHEDULED);
		scheduleTaskVo.setScheduledTime(new Date());
		scheduleTaskVo.setMercyFlag(registVo.getMercyFlag());
		scheduleTaskVo.setValidFlag(CodeConstants.ValidFlag.INVALID);// 只是负责展示，无实际意义
		// 根据地区代码查省市区代码
		String[] a = areaDictService.findAreaByAreaCode(checkAddressCode,"");
			if(a!=null && a.length>0){
				scheduleTaskVo.setProvinceCityAreaCode(a[1]);
			}
		
		//scheduleTaskVo.setProvinceCityAreaCode(StringUtils.substring(checkAddressCode, 0, 4) + "00".trim());
		scheduleTaskVo.setRegionCode(checkAddressCode);
		scheduleTaskVo.setCheckareaName(registVo.getPrpLRegistExt().getCheckAddress());
		scheduleTaskVo.setCheckAddressMapCode(registVo.getPrpLRegistExt().getCheckAddressMapCode());
		scheduleTaskVo.setOperatorCode(ServiceUserUtils.getUserCode());
		// 是否自助查勘赋值
		if("1".equals(registVo.getSelfRegistFlag())){
			scheduleTaskVo.setIsAutoCheck("1");
		}
		
		// 创建调度查勘list
		List<PrpLScheduleItemsVo> itemsVoList = new ArrayList<PrpLScheduleItemsVo>();
		// 组装Map key-车牌号，value-serialNo
		Map<String,String> car_serialMap = new HashMap<String,String>();
		// 新增车辆损失项
		if (registVo.getPrpLRegistCarLosses() != null 
				&& registVo.getPrpLRegistCarLosses().size() > 0) {
			int serialNo = 2;// 序号为空则非车辆损失，序号为1则为标的车，序号>1则为三者车
			for (PrpLRegistCarLossVo vo : registVo.getPrpLRegistCarLosses()) {
				PrpLScheduleItemsVo itemsVo = new PrpLScheduleItemsVo();
				if ("1".equals(vo.getLossparty())) {
					itemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR);
					itemsVo.setItemsName("标的车");
					itemsVo.setSerialNo("1");
				} else {
					itemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_THIRDCAR);
					itemsVo.setItemsName("三者车");
					itemsVo.setSerialNo(String.valueOf(serialNo));
					serialNo ++;
				}

				itemsVo.setRegistNo(registVo.getRegistNo());
				itemsVo.setItemsContent(vo.getLicenseNo());
				itemsVo.setItemsSourceId(vo.getId());
				itemsVo.setScheduleStatus(CodeConstants.ScheduleStatus.NOT_SCHEDULED);
				itemsVo.setCreateUser(scheduleTaskVo.getCreateUser());
				itemsVo.setCreateTime(new Date());
				itemsVo.setUpdateUser(scheduleTaskVo.getUpdateUser());
				itemsVo.setUpdateTime(new Date());
				itemsVo.setLicenseNo(vo.getLicenseNo());
				itemsVoList.add(itemsVo);
				
				car_serialMap.put(vo.getLicenseNo(), String.valueOf(serialNo));
			}
		}
		
		boolean kindFlag = false;// 未承保
		if("1".equals(registVo.getPrpLRegistExt().getIsPersonLoss()) && "0".equals(registVo.getIsPeopleflag())){
			if("12".equals(registVo.getRiskCode().substring(0,2))){// 有商业
				// 没有承保车上人员责任险
				List<PrpLCItemKindVo> cIemKindVoList = policyViewService.findItemKinds(registVo.getRegistNo(),null);
				// 判断是否承保车上人员责任险
				for(PrpLCItemKindVo cIemKindVo:cIemKindVoList){
					String kindCode = cIemKindVo.getKindCode();
					if(StringUtils.isNotEmpty(kindCode)){
						if("D12".equals(kindCode) || "D11".equals(kindCode)){
							kindFlag = true;
						}
					}
				}
				if(registVo.getPrpLRegistPersonLosses().size()>1){// 三者
					if("3".equals(registVo.getPrpLRegistPersonLosses().get(1).getLossparty())){
						if(registVo.getPrpLRegistPersonLosses().get(1).getDeathcount()>0||
								registVo.getPrpLRegistPersonLosses().get(1).getInjuredcount()>0){
							kindFlag = true;
						}
					}else{
						if(registVo.getPrpLRegistPersonLosses().get(0).getDeathcount()>0||
								registVo.getPrpLRegistPersonLosses().get(0).getInjuredcount()>0){
							kindFlag = true;
						}
					}
					
				}
			}else{
				kindFlag = true;
			}
			if(kindFlag){
				// 新增人伤损失项
				if (registVo.getPrpLRegistPersonLosses() != null 
						&& registVo.getPrpLRegistPersonLosses().size() > 0) {
					int injured = 0;
					int death = 0;
					
					for (PrpLRegistPersonLossVo vo : registVo.getPrpLRegistPersonLosses()) {
						injured += vo.getInjuredcount();
						death += vo.getDeathcount();
					}
					// 只有当有人员伤亡数量的时候在生成查勘人伤调度任务
					//if (injured > 0 || death > 0) {
					// 如果有人伤只保存一条记录，包括伤亡人数即可
						PrpLScheduleItemsVo itemsVo = new PrpLScheduleItemsVo();
						itemsVo.setRegistNo(registVo.getRegistNo());
						itemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON);
					itemsVo.setItemsName("人伤");
						itemsVo.setSerialNo("-1");
						itemsVo.setItemsContent(String.valueOf(injured));
						itemsVo.setItemRemark(String.valueOf(death));
						itemsVo.setScheduleStatus(CodeConstants.ScheduleStatus.NOT_SCHEDULED);
						itemsVo.setCreateUser(scheduleTaskVo.getCreateUser());
						itemsVo.setCreateTime(new Date());
						itemsVo.setUpdateUser(scheduleTaskVo.getUpdateUser());
						itemsVo.setUpdateTime(new Date());
						itemsVoList.add(itemsVo);
					//}
				}
			}
		}
		
		// 新增财产损失损失项
		if (registVo.getPrpLRegistPropLosses() != null 
				&& registVo.getPrpLRegistPropLosses().size() > 0) {
				for (PrpLRegistPropLossVo vo : registVo.getPrpLRegistPropLosses()) {
				if("1".equals(vo.getDamagelevel())){// 有物损
						PrpLScheduleItemsVo itemsVo = new PrpLScheduleItemsVo();
						itemsVo.setRegistNo(registVo.getRegistNo());
						itemsVo.setItemType(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PROP);
					itemsVo.setItemsName("物损");
						if (StringUtils.equals(vo.getLicenseNo(), "0")) {
							itemsVo.setSerialNo("0");
						itemsVo.setLicenseNo("地面");
						} else {
							itemsVo.setLicenseNo(vo.getLicenseNo());
							itemsVo.setSerialNo(car_serialMap.get(vo.getLicenseNo()));
						}
						itemsVo.setItemsContent(vo.getLossitemname());
						itemsVo.setScheduleStatus(CodeConstants.ScheduleStatus.NOT_SCHEDULED);
						itemsVo.setItemsSourceId(vo.getId());
						itemsVo.setCreateUser(scheduleTaskVo.getCreateUser());
						itemsVo.setCreateTime(new Date());
						itemsVo.setUpdateUser(scheduleTaskVo.getUpdateUser());
						itemsVo.setUpdateTime(new Date());
						itemsVoList.add(itemsVo);
					}
			}
		}
		// 从移动查勘获取查勘员和人伤跟踪员
		try{
			if(url !=null ){
			    if (!StringUtils.equals(registVo.getTempRegistFlag(), CodeConstants.TempReport.TEMPREPORT)) {
					// 判断是否联共保案件：报案、调度提交不与移动排班系统交互，根据预配置调度人员提交调度；
			    	if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
						// 业务给出查勘人员
			    		//getGBCheckPerson(registVo,itemsVoList,scheduleTaskVo);
			    	}else{
			    		personnelInformationService.getPersonnelInformation(registVo, itemsVoList,scheduleTaskVo,url);
			    	}
			    }
			}
        }
        catch(Exception e){
            e.printStackTrace();
        }
		scheduleTaskVo.setPrpLScheduleItemses(itemsVoList);
		scheduleTaskVo.setTypes("1");
		if(registVo.getSelfDefinareaCode()!=null&&registVo.getSelfDefinareaCode()!=""){
			scheduleTaskVo.setSelfDefinareaCode(registVo.getSelfDefinareaCode());
		}
		return scheduleTaskVo;
	}
	
	// 通过报案号查询风险信息表
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findRegistRiskInfoByRegistNo(java.lang.String)
	 */
	@Override
	public Map<String, String> findRegistRiskInfoByRegistNo(
			String registNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		List<PrpLRegistRiskInfo> registRiskInfos = databaseDao.findAll(
				PrpLRegistRiskInfo.class, qr);
		Map<String, String> registRiskInfoMap = new HashMap<String, String>();
		// 使用Map集合获取要在页面上显示的信息名称和内容
		for (PrpLRegistRiskInfo registRiskInfoPo : registRiskInfos) {
			PrpLRegistRiskInfoVo registtRiskInfoVo = Beans.copyDepth()
					.from(registRiskInfoPo).to(PrpLRegistRiskInfoVo.class);
			// 将code和value按照键值对方式存储
			registRiskInfoMap.put(registtRiskInfoVo.getFactorcode(),
					registtRiskInfoVo.getFactorvalue());
		}
		return registRiskInfoMap;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#cancelPolicy(java.util.List, ins.sino.claimcar.regist.vo.PrpLRegistVo, ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo)
	 */
	@Override
	public void cancelPolicy(List<PrpLCMainVo> prpLCMainVoList, PrpLRegistVo registVo, PrpLRegistRelationshipHisVo relationshipHisVo) {
		prpLCMainService.saveOrUpdate(prpLCMainVoList, registVo.getRegistNo());
		updatePrpLRegist(registVo);
		saveRelationshipHis(relationshipHisVo);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#updatePrpLRegist(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public PrpLRegistVo updatePrpLRegist(PrpLRegistVo registVo) {
		if (registVo != null) {
			PrpLRegist po = Beans.copyDepth().from(registVo).to(PrpLRegist.class);
			List<PrpLRegistPropLoss> prpLRegistPropLosses = Beans.copyDepth().from(registVo.getPrpLRegistPropLosses()).toList(PrpLRegistPropLoss.class);
			List<PrpLRegistCarLoss> prpLRegistCarLosses = Beans.copyDepth().from(registVo.getPrpLRegistCarLosses()).toList(PrpLRegistCarLoss.class);
			List<PrpLRegistPersonLoss> prpLRegistPersonLosses = Beans.copyDepth().from(registVo.getPrpLRegistPersonLosses()).toList(PrpLRegistPersonLoss.class);
			PrpLRegistExt prpLRegistExt = Beans.copyDepth().from(registVo.getPrpLRegistExt()).to(PrpLRegistExt.class);
			po.setPrpLRegistPropLosses(prpLRegistPropLosses);
			po.setPrpLRegistCarLosses(prpLRegistCarLosses);
			po.setPrpLRegistPersonLosses(prpLRegistPersonLosses);
			po.setPrpLRegistExt(prpLRegistExt);
			if(prpLRegistCarLosses!=null && prpLRegistCarLosses.size()>0){
				for(PrpLRegistCarLoss carLoss:prpLRegistCarLosses){
					carLoss.setPrpLRegist(po);
				}
			}
			if(prpLRegistPropLosses!=null && prpLRegistPropLosses.size()>0){
				for(PrpLRegistPropLoss propLoss:prpLRegistPropLosses){
					propLoss.setPrpLRegist(po);
				}
			}
			if(prpLRegistPersonLosses!=null && prpLRegistPersonLosses.size()>0){
				for(PrpLRegistPersonLoss personLoss:prpLRegistPersonLosses){
					personLoss.setPrpLRegist(po);
				}
			}
			
			prpLRegistExt.setPrpLRegist(po);
			
			databaseDao.update(PrpLRegist.class,po);
			registVo = Beans.copyDepth().from(po).to(PrpLRegistVo.class);
		}
		return registVo;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#saveRelationshipHis(ins.sino.claimcar.regist.vo.PrpLRegistRelationshipHisVo)
	 */
	@Override
	public void saveRelationshipHis(PrpLRegistRelationshipHisVo relationshipHisVo) {
		if(relationshipHisVo != null){
			logger.info("保单取消或者关联保存relationshipHis数据开始=============================Operationafter="+relationshipHisVo.getOperationafter()+"registno"+
					relationshipHisVo.getRegistNo());
		}
		PrpLRegistRelationshipHis po = Beans.copyDepth().from(relationshipHisVo).to(PrpLRegistRelationshipHis.class);
		databaseDao.save(PrpLRegistRelationshipHis.class, po);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findRelationshipHisByRegistNo(java.lang.String)
	 */
	@Override
	public List<PrpLRegistRelationshipHisVo> findRelationshipHisByRegistNo(String registNo) {
		List<PrpLRegistRelationshipHisVo> returnRelationHisVos = new ArrayList<PrpLRegistRelationshipHisVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		qr.addAscOrder("createTime");
		List<PrpLRegistRelationshipHis> returnRelationHises = databaseDao.findAll(PrpLRegistRelationshipHis.class, qr);
		for (PrpLRegistRelationshipHis po : returnRelationHises) {
			PrpLRegistRelationshipHisVo vo = Beans.copyDepth().from(po).to(PrpLRegistRelationshipHisVo.class);
			returnRelationHisVos.add(vo);
		}
		return returnRelationHisVos;
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#saveRiskInfos(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.util.Map)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void saveRiskInfos(PrpLRegistVo prpLRegistVo, Map<String, String> registRiskInfoMap) {
		List<PrpLRegistRiskInfo> riskInfos = new ArrayList<PrpLRegistRiskInfo>();
		for(Iterator it = registRiskInfoMap.entrySet().iterator(); it.hasNext(); ){
			Map.Entry e = (Map.Entry)it.next();
			PrpLRegistRiskInfo riskInfo = new PrpLRegistRiskInfo();
			riskInfo.setRegistNo(prpLRegistVo.getRegistNo());
			riskInfo.setNodename(FlowNode.Regis.toString());
			riskInfo.setCreateUser(prpLRegistVo.getCreateUser());
			riskInfo.setCreateTime(new Date());
			riskInfo.setUpdateUser(prpLRegistVo.getCreateUser());
			riskInfo.setUpdateTime(new Date());
			riskInfo.setFactorcode((String) e.getKey());
			riskInfo.setFactorvalue((String) e.getValue());
 			riskInfos.add(riskInfo);
		}
		databaseDao.saveAll(PrpLRegistRiskInfo.class, riskInfos);
		
		
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#updateRiskInfoForSubRogation(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.lang.String)
	 */
	@Override
	public void updateRiskInfoForSubRogation(PrpLRegistVo prpLRegistVo, String flag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", prpLRegistVo.getRegistNo());
		qr.addEqual("factorcode", "DWQC");
		PrpLRegistRiskInfo po = databaseDao.findUnique(PrpLRegistRiskInfo.class, qr);
		if (po != null) {
			po.setFactorvalue(flag);
			databaseDao.update(PrpLRegistRiskInfo.class, po);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#updatePrpLRegistAvgByVos(java.util.List)
	 */
	@Override
	public void updatePrpLRegistAvgByVos(List<PrpLRegistAvgVo> registAvgVos) {
		List<PrpLRegistAvg> pos = Beans.copyDepth().from(registAvgVos).toList(PrpLRegistAvg.class);
		databaseDao.saveAll(PrpLRegistAvg.class, pos);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#saveDeductCond(java.lang.String)
	 */
	@Override
	public void saveDeductCond(String registNo) {
		QueryRule queryRu = QueryRule.getInstance();
		queryRu.addEqual("registNo",registNo);
		List<PrpLClaimDeduct> claimDeducts = databaseDao.findAll(PrpLClaimDeduct.class,queryRu);
		if(claimDeducts!=null&&claimDeducts.size()>0){return;}

		String policyNo = "";
		String riskCode = "";
		List<PrpLCItemKindVo> cItemKinds = null;
		List<PrpLCMainVo> policyVos = null;
		policyVos = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
		for(PrpLCMainVo policyVo:policyVos){
			if( !"1101".equals(policyVo.getRiskCode())){
				policyNo = policyVo.getPolicyNo();
				riskCode = policyVo.getRiskCode();
				cItemKinds = policyVo.getPrpCItemKinds();
			}
		}
		
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("riskCode",riskCode);
		
		String[] kind = new String[50];
		if(cItemKinds!=null&&cItemKinds.size()>0){
			for(int i = 0; i < cItemKinds.size(); i++ ){
				kind[i] = cItemKinds.get(i).getKindCode();
			}
		}
		qr.addIn("kindCode",kind);
		
		// getPrpCMain
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("policyNo",policyNo);
		PrpCMain prpCMain = databaseDao.findUnique(PrpCMain.class,queryRule);
		String[] clauseType = new String[20];
		if(prpCMain!=null){
			List<PrpCItemCar> cItemCars = prpCMain.getPrpCItemCars();
			for(int i = 0; i < cItemCars.size(); i++){
				clauseType[i] = cItemCars.get(i).getClauseType();
			}
		}
		qr.addIn("clauseType",clauseType);
		qr.addEqual("flag","1");// 增加有效的数据
		
		List<PrpDDeductCond> PrpDDeductCond = databaseDao.findAll(PrpDDeductCond.class,qr);
		
		Integer i = 1;
		for(PrpDDeductCond deductCond : PrpDDeductCond){
			
			PrpLClaimDeduct claimDeductPo = new PrpLClaimDeduct();
			claimDeductPo.setRegistNo(registNo);
			claimDeductPo.setPolicyNo(policyNo);
			claimDeductPo.setSerialNo(i);i++;
			claimDeductPo.setRiskCode(riskCode);
			claimDeductPo.setKindCode(deductCond.getKindCode());
			claimDeductPo.setClauseType(deductCond.getClauseType());
			claimDeductPo.setDeductCondCode(deductCond.getDeductCondCode());
			claimDeductPo.setDeductCondName(deductCond.getDeductCondName());
			claimDeductPo.setDeductIble(deductCond.getDeductIble());
			claimDeductPo.setDeductRate(deductCond.getDeductRate());
			claimDeductPo.setIsCheck(CodeConstants.RadioValue.RADIO_NO);
			claimDeductPo.setValidDate(new Date());
			claimDeductPo.setFlag(CodeConstants.ValidFlag.VALID);
			
			claimDeducts.add(claimDeductPo);
		}

		databaseDao.saveAll(PrpLClaimDeduct.class,claimDeducts);
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#updateRiskInfo(ins.sino.claimcar.regist.vo.PrpLRegistVo, java.lang.String)
	 */
	@Override
	public void updateRiskInfo(PrpLRegistVo prpLRegistVo, String flag) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", prpLRegistVo.getRegistNo());
		qr.addEqual("factorcode", flag);
		PrpLRegistRiskInfo po = databaseDao.findUnique(PrpLRegistRiskInfo.class, qr);
		if (po != null) {
			if("11".equals(prpLRegistVo.getRiskCode().substring(0, 2))){
				po.setFactorcode("CI-No");
			}else{
				po.setFactorcode("BI-No");
			}
			po.setFactorvalue(prpLRegistVo.getPolicyNo());
			databaseDao.update(PrpLRegistRiskInfo.class, po);
		}
		
		
	}
	
	
	
	// 无保转有保删除风险因子
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#deleteByRegistNo(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public void deleteByRegistNo(PrpLRegistVo registVo) {
		// TODO Auto-generated method stub
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addIn("registNo", registVo.getRegistNo());
		List<PrpLRegistRiskInfo> prpLRegistRiskInfos = databaseDao.findAll(PrpLRegistRiskInfo.class, queryRule);
		databaseDao.deleteAll(PrpLRegistRiskInfo.class, prpLRegistRiskInfos);
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findPrpLCItemKindByOther(java.lang.String)
	 */
	@Override
	public List<PrpLCItemKindVo> findPrpLCItemKindByOther(String BIPolicyNo,String BIRiskCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", BIPolicyNo);
		qr.addEqual("kindCode", "G");
		List<PrpLCItemKind> prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qr);
		//盗抢险如果是1230保了车损险可以成功报案，但1231、1232、1233如果未保盗抢险不可以成功报案
		if((prpLCItemKinds == null || prpLCItemKinds.size()==0) && "1230".equals(BIRiskCode)){
			QueryRule qrN = QueryRule.getInstance();
			qrN.addEqual("policyNo", BIPolicyNo);
			qrN.addEqual("kindCode", "A");
			prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qrN);
		}
		List<PrpLCItemKindVo> prpLCItemKindVos = new ArrayList<PrpLCItemKindVo>();
		for(PrpLCItemKind prpLCItemKind:prpLCItemKinds){
			PrpLCItemKindVo vo =Beans.copyDepth().from(prpLCItemKind).to(PrpLCItemKindVo.class);
			prpLCItemKindVos.add(vo);
		}
		return prpLCItemKindVos;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findPrpLCItemKindByOthers(java.lang.String, java.lang.String)
	 */
	@Override
	public List<PrpLCItemKindVo> findPrpLCItemKindByOthers(String BIPolicyNo,String kindCode,String BIRiskCode) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", BIPolicyNo);
		qr.addEqual("kindCode", kindCode);
		List<PrpLCItemKind> prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qr);
		//玻璃单独破碎如果是1230及以上险种（1230/1231/1232/1233）保了车损险即可成功报案
		if((prpLCItemKinds == null || prpLCItemKinds.size()==0) && ("1230".equals(BIRiskCode) || "1231".equals(BIRiskCode)
				|| "1232".equals(BIRiskCode) || "1233".equals(BIRiskCode)) && "F".equals(kindCode)){
			QueryRule qrN = QueryRule.getInstance();
			qrN.addEqual("policyNo", BIPolicyNo);
			qrN.addEqual("kindCode", "A");
			prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qrN);
		}
		List<PrpLCItemKindVo> prpLCItemKindVos = new ArrayList<PrpLCItemKindVo>();
		for(PrpLCItemKind prpLCItemKind:prpLCItemKinds){
			PrpLCItemKindVo vo =Beans.copyDepth().from(prpLCItemKind).to(PrpLCItemKindVo.class);
			prpLCItemKindVos.add(vo);
		}
		return prpLCItemKindVos;
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#sendMsg(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public void sendMsg(PrpLRegistVo prpLRegistVo){
		PrpLCMainVo prpLCMainVo=policyViewService.getPolicyInfo(prpLRegistVo.getRegistNo(), prpLRegistVo.getPolicyNo());
		String comCode = null;
		if(prpLCMainVo!=null){
			comCode = prpLCMainVo.getComCode();
		}
		SendMsgParamVo msgParamVo = this.getsendMsgParamVo(prpLRegistVo,prpLCMainVo);
		logger.info("****************************"+msgParamVo.toString());
		/**
		 * 推送修
		 */
		if(prpLRegistVo.getRepairId()!=null){
			// 给修理厂发短信模板
			PrpLRepairFactoryVo prpLRepairFactoryVo=repairFactoryService.findFactoryById(Long.toString(prpLRegistVo.getRepairId()));
			if(prpLRepairFactoryVo!=null && StringUtils.isNotBlank(prpLRepairFactoryVo.getMobile())){
				SysMsgModelVo msgModelVo_7 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.repair, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.normal);
				if(msgModelVo_7 != null && StringUtils.isNotBlank(prpLRepairFactoryVo.getMobile())){
					Date sendTime_7 = sendMsgService.getSendTime(msgModelVo_7.getTimeType());
					String message_7 = sendMsgService.getMessage(msgModelVo_7.getContent(), msgParamVo);
					String status="";
					boolean	 index=false;
					index=smsService.sendSMSContent(msgParamVo.getRegistNo(),prpLRepairFactoryVo.getMobile(), message_7, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_7);
					if(index){
						status = "1";// 推送短信平台成功
					}else{
						status = "0";// 推送短信平台失败
					}
					putSmsmessage(msgParamVo,prpLRepairFactoryVo.getMobile(),sendTime_7,message_7,status);
							
				} else {
					if (msgModelVo_7 == null) {
						String content = prpLRegistVo.getRegistNo() +
								" 修理厂短信模板为空!" +
								" ModelTYpe:" + CodeConstants.ModelType.repair +
								" SystemNode:" + CodeConstants.SystemNode.report +
								"CaseType:" + CodeConstants.CaseType.normal;
						logger.info(content);
					}
					if (StringUtils.isBlank(prpLRepairFactoryVo.getMobile())) {
						logger.info(prpLRegistVo.getRegistNo() + " 修理厂电话为空！ 修理厂 prpLRegistVo.getRepairId() PrpLRepairFactory.id:" + prpLRegistVo.getRepairId());
					}
				}
			}
			// 发送短信至推送修手机号
			SysMsgModelVo msgModelVo_13 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.repair, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.repair);
			if(msgModelVo_13 != null){
				Date sendTime_13 = sendMsgService.getSendTime(msgModelVo_13.getTimeType());
				String message_13 = sendMsgService.getMessage(msgModelVo_13.getContent(), msgParamVo);

				String status="";
				boolean	 index=false;
				index=smsService.sendSMSContent(msgParamVo.getRegistNo(),prpLRegistVo.getServiceMobile(), message_13, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_13);
				if(index){
					status = "1";// 推送短信平台成功
				}else{
					status = "0";// 推送短信平台失败
				}
				putSmsmessage(msgParamVo,prpLRegistVo.getServiceMobile(),sendTime_13,message_13,status);
						
			} else {
				String content = prpLRegistVo.getRegistNo() +
						" 推修短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.repair +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.repair;
				logger.info(content);
			}
			
			// 发送短信至报案人手机号
			SysMsgModelVo msgModelVo_1 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.repair);
			if(msgModelVo_1 != null && StringUtils.isNotBlank(msgParamVo.getReportoMobile())){
				Date sendTime_1 = sendMsgService.getSendTime(msgModelVo_1.getTimeType());
				String message_1 = sendMsgService.getMessage(msgModelVo_1.getContent(), msgParamVo);
				String status="";
				boolean	 index=false;
				index=smsService.sendSMSContent(msgParamVo.getRegistNo(),msgParamVo.getReportoMobile(), message_1, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_1);
				if(index){
					status = "1";// 推送短信平台成功
				}else{
					status = "0";// 推送短信平台失败
				}
				putSmsmessage(msgParamVo,msgParamVo.getReportoMobile(),sendTime_1,message_1,status);
						
			} else {
				String content = prpLRegistVo.getRegistNo() +
						" 报案人短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.report +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.repair;
				logger.info(content);
			}
		}
		
		
		// 根据机构获取领导的手机号码
		List<String> mobiles = sendMsgService.getMobile(comCode,"1");
		// 全车盗抢发给分公司和总公司领导
		if("DM04".equals(prpLRegistVo.getDamageCode())){
			SysMsgModelVo msgModelVo_2 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.steal, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.normal);
			if(msgModelVo_2 != null){
				Date sendTime_2 = sendMsgService.getSendTime(msgModelVo_2.getTimeType());
				String message_2 = sendMsgService.getMessage(msgModelVo_2.getContent(), msgParamVo);
				if(mobiles != null && mobiles.size() > 0){
					for(String mobile:mobiles){
						String status="";
						boolean	 index=false;
						index=smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_2, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_2);
						if(index){
							status = "1";// 推送短信平台成功
						}else{
							status = "0";// 推送短信平台失败
						}
						if(StringUtils.isNotBlank(mobile)){
						putSmsmessage(msgParamVo,mobile,sendTime_2,message_2,status);
						}
					}
				} else {
					String content = prpLRegistVo.getRegistNo() +
							" 全车盗抢发给分公司领导手机号为空!" +
							" ModelTYpe:" + CodeConstants.ModelType.steal +
							" SystemNode:" + CodeConstants.SystemNode.report +
							"CaseType:" + CodeConstants.CaseType.normal;
					logger.info(content);
				}
			} else {
				String content = prpLRegistVo.getRegistNo() +
						" 全车盗抢发给分公司领导短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.steal +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
			// 如果不是总公司机构，则还要发给总公司的领导
			if(!(comCode.startsWith("0000")||comCode.startsWith("0001"))){
				SysMsgModelVo msgModelVo_7 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.steal, CodeConstants.SystemNode.report,"00000000",CodeConstants.CaseType.normal);
				if(msgModelVo_7 != null){
					Date sendTime_7 = sendMsgService.getSendTime(msgModelVo_7.getTimeType());
					String message_7 = sendMsgService.getMessage(msgModelVo_7.getContent(), msgParamVo);
					List<String> mobileList = sendMsgService.getMobile("00000000","1");
					if(mobileList != null && mobileList.size() > 0){
						for(String mobile:mobileList){
							String status="";
							boolean	 index=false;
							index=smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_7, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_7);
							if(index){
								status = "1";// 推送短信平台成功
							}else{
								status = "0";// 推送短信平台失败
							}
							if(StringUtils.isNotBlank(mobile)){
							putSmsmessage(msgParamVo,mobile,sendTime_7,message_7,status);
							}
						}
					} else {
						String content = prpLRegistVo.getRegistNo() +
								" 全车盗抢发短信给总公司领导手机号为空!" +
								" ModelTYpe:" + CodeConstants.ModelType.steal +
								" SystemNode:" + CodeConstants.SystemNode.report +
								"CaseType:" + CodeConstants.CaseType.normal;
						logger.info(content);
					}
				} else {
					String content = prpLRegistVo.getRegistNo() +
							" 全车盗抢发短信给总公司领导短信模板为空!" +
							" ModelTYpe:" + CodeConstants.ModelType.steal +
							" SystemNode:" + CodeConstants.SystemNode.report +
							"CaseType:" + CodeConstants.CaseType.normal;
					logger.info(content);
				}
			}
		}
		// 有人员死亡，发给分公司和总公司领导
		if(!"".equals(msgParamVo.getDeathcount())&&!"0".equals(msgParamVo.getDeathcount())){
			SysMsgModelVo msgModelVo_6 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.pLoss, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.normal);
			if(msgModelVo_6 != null){
				Date sendTime_6 = sendMsgService.getSendTime(msgModelVo_6.getTimeType());
				String message_6 = sendMsgService.getMessage(msgModelVo_6.getContent(), msgParamVo);
				if(mobiles != null && mobiles.size() > 0){
					for(String mobile:mobiles){
						String status="";
						boolean	 index=false;
						index=smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_6, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_6);
						if(index){
							status = "1";// 推送短信平台成功
						}else{
							status = "0";// 推送短信平台失败
						}
						if(StringUtils.isNotBlank(mobile)){
						putSmsmessage(msgParamVo,mobile,sendTime_6,message_6,status);
						}
					}
				} else {
					String content = prpLRegistVo.getRegistNo() +
							" 有人员伤亡发短信给分公司领导手机号为空!" +
							" ModelTYpe:" + CodeConstants.ModelType.pLoss +
							" SystemNode:" + CodeConstants.SystemNode.report +
							"CaseType:" + CodeConstants.CaseType.normal;
					logger.info(content);
				}
			} else {
				String content = prpLRegistVo.getRegistNo() +
						" 有人员伤亡发短信给分公司领导短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.pLoss +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
			// 如果不是总公司机构，则还要发给总公司的领导
			if(!(comCode.startsWith("0000")||comCode.startsWith("0001"))){
				SysMsgModelVo msgModelVo_8 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.pLoss, CodeConstants.SystemNode.report,"00000000",CodeConstants.CaseType.normal);
				if(msgModelVo_8 != null){
					Date sendTime_8 = sendMsgService.getSendTime(msgModelVo_8.getTimeType());
					String message_8 = sendMsgService.getMessage(msgModelVo_8.getContent(), msgParamVo);
					List<String> mobileList = sendMsgService.getMobile("00000000","1");
					if(mobileList != null && mobileList.size() > 0){
						for(String mobile:mobileList){
							String status="";
							boolean	 index=false;
							index=smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_8, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_8);
							if(index){
								status = "1";// 推送短信平台成功
							}else{
								status = "0";// 推送短信平台失败
							}
							if(StringUtils.isNotBlank(mobile)){
							putSmsmessage(msgParamVo,mobile,sendTime_8,message_8,status);
							}
						}
					} else {
						String content = prpLRegistVo.getRegistNo() +
								" 有人员伤亡发短信给总公司领导手机号为空!" +
								" ModelTYpe:" + CodeConstants.ModelType.pLoss +
								" SystemNode:" + CodeConstants.SystemNode.report +
								"CaseType:" + CodeConstants.CaseType.normal;
						logger.info(content);
					}
				} else {
					String content = prpLRegistVo.getRegistNo() +
							" 有人员伤亡发短信给总公司领导短信模板为空!" +
							" ModelTYpe:" + CodeConstants.ModelType.pLoss +
							" SystemNode:" + CodeConstants.SystemNode.report +
							"CaseType:" + CodeConstants.CaseType.normal;
					logger.info(content);
				}
			}
		}
		// 发给报案人
		String comCode_1 = null;
		// 如果修理厂的名字是空就发总公司模板
		if(msgParamVo.getRepairName()==null || "".equals(msgParamVo.getRepairName())){
			comCode_1 = "00000000";
		}else{
			comCode_1 = prpLCMainVo.getComCode();
		}
		SysMsgModelVo msgModelVo_3=null;
		if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag()) && !"1".equals(prpLRegistVo.getSelfRegistFlag())){
			SysMsgModelVo msgModelVo_12 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.report,comCode_1,CodeConstants.CaseType.selfClaim);
			PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
			if(msgModelVo_12!=null&&StringUtils.isNotBlank(msgModelVo_12.getContent())&&msgModelVo_12.getContent().contains("自助视频链接")&&prpLRegistExtVo!=null&&StringUtils
					.isNotBlank(prpLRegistExtVo.getOrderNo())){
				msgModelVo_3=msgModelVo_12;
			}
			if (msgModelVo_3 == null) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信（含自助视频链接）给报案人短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.report +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.selfClaim;
				logger.info(content);
			}
			
		}else{
			msgModelVo_3 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.report,comCode_1,CodeConstants.CaseType.normal);
			if (msgModelVo_3 == null) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给报案人短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.report +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
		}
		
		
		if(msgModelVo_3 != null && StringUtils.isNotBlank(msgParamVo.getLinkerMobile())){
			Date sendTime_3 = sendMsgService.getSendTime(msgModelVo_3.getTimeType());
			String message_3 = sendMsgService.getMessage(msgModelVo_3.getContent(), msgParamVo);
			String status="";
			boolean	 index=false;
		    index=smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getLinkerMobile(), message_3, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_3);
			if(index){
				status = "1";// 推送短信平台成功
			}else{
				status = "0";// 推送短信平台失败
			}
			putSmsmessage(msgParamVo,msgParamVo.getLinkerMobile(),sendTime_3,message_3,status);
		}
		// 发给被保险人
		SysMsgModelVo msgModelVo_5 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.insured, CodeConstants.SystemNode.report,comCode_1,CodeConstants.CaseType.normal);
		if(msgModelVo_5 != null && StringUtils.isNotBlank(msgParamVo.getMobile())){
			Date sendTime_5 = sendMsgService.getSendTime(msgModelVo_5.getTimeType());
			String message_5 = sendMsgService.getMessage(msgModelVo_5.getContent(), msgParamVo);
			String status="";
			boolean	 index=false;
			index=smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getMobile(), message_5, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_5);
			if(index){
				status = "1";// 推送短信平台成功
			}else{
				status = "0";// 推送短信平台失败
			}
			putSmsmessage(msgParamVo,msgParamVo.getMobile(),sendTime_5,message_5,status);
		} else {
			if (msgModelVo_5 == null) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给被保险人短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.insured +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
			if (StringUtils.isBlank(msgParamVo.getMobile())) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给被保险人手机号为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.insured +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}

		}
		// 发送给修理厂
		SysMsgModelVo msgModelVo_4 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.repair, CodeConstants.SystemNode.report,comCode,CodeConstants.CaseType.normal);
		if(msgModelVo_4 != null && StringUtils.isNotBlank(msgParamVo.getRepairMobile())){
			Date sendTime_4 = sendMsgService.getSendTime(msgModelVo_4.getTimeType());
			String message_4 = sendMsgService.getMessage(msgModelVo_4.getContent(), msgParamVo);
			String status="";
			boolean	 index=false;
		    index=smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getRepairMobile(), message_4, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_4);
			if(index){
				status = "1";// 推送短信平台成功
			}else{
				status = "0";// 推送短信平台失败
			}
			putSmsmessage(msgParamVo,msgParamVo.getRepairMobile(),sendTime_4,message_4,status);
		} else {
			if (msgModelVo_4 == null) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给修理厂短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.repair +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
			if (StringUtils.isBlank(msgParamVo.getRepairMobile())) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给修理厂手机号为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.repair +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}

		}
		// 发给代理人
		SysMsgModelVo msgModelVo_9 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.agent,CodeConstants.SystemNode.report,comCode,
				CodeConstants.CaseType.normal);
		if(msgModelVo_9 != null && StringUtils.isNotBlank(msgParamVo.getAgentPhone())){
			Date sendTime_9 = sendMsgService.getSendTime(msgModelVo_9.getTimeType());
			String message_9 = sendMsgService.getMessage(msgModelVo_9.getContent(), msgParamVo);
			String status="";
			boolean	 index=false;
		    index=smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getAgentPhone(), message_9, msgParamVo.getUseCode(), msgParamVo.getComCode(),sendTime_9);
			if(index){
				status = "1";// 推送短信平台成功
			}else{
				status = "0";// 推送短信平台失败
			}
			putSmsmessage(msgParamVo,msgParamVo.getAgentPhone(),sendTime_9,message_9,status);
		} else {
			if (msgModelVo_9 == null) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给代理人短信模板为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.agent +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}
			if (StringUtils.isBlank(msgParamVo.getAgentPhone())) {
				String content = prpLRegistVo.getRegistNo() +
						" 发短信给代理人手机号为空!" +
						" ModelTYpe:" + CodeConstants.ModelType.agent +
						" SystemNode:" + CodeConstants.SystemNode.report +
						"CaseType:" + CodeConstants.CaseType.normal;
				logger.info(content);
			}

		}
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#getsendMsgParamVo(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	public SendMsgParamVo getsendMsgParamVo(PrpLRegistVo prpLRegistVo,PrpLCMainVo prpLCMainVo){
		SendMsgParamVo msgParamVo = new  SendMsgParamVo();
		msgParamVo.setUseCode(prpLRegistVo.getCreateUser());
		msgParamVo.setComCode(prpLRegistVo.getComCode());
		msgParamVo.setRegistNo(prpLRegistVo.getRegistNo());
		msgParamVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(), DateUtils.YToSec));
		if(prpLRegistVo.getPrpLRegistExt()!=null&&prpLRegistVo.getPrpLRegistExt().getDangerRemark()!=null){// 出险经过
			msgParamVo.setDangerRemark(prpLRegistVo.getPrpLRegistExt().getDangerRemark());
		}
		if(prpLRegistVo.getPolicyNo() != null && !"".equals(prpLRegistVo.getPolicyNo())){
			msgParamVo.setPolicyNo(prpLRegistVo.getPolicyNo());
		}
		if(prpLRegistVo.getPrpLRegistCarLosses().size()>=2){// 取三者车牌号
			for(PrpLRegistCarLossVo registCarLossVo:prpLRegistVo.getPrpLRegistCarLosses()){
				if("3".equals(registCarLossVo.getLossparty())&&registCarLossVo.getLicenseNo()!=null){
					msgParamVo.setOtherLicenseNo(registCarLossVo.getLicenseNo());
					break;
				}
			}
		}
		if(prpLRegistVo.getReportorName()!=null&& !"".equals(prpLRegistVo.getReportorName())){// 报案人
			msgParamVo.setReportorName(prpLRegistVo.getReportorName());
		}
		if(prpLRegistVo.getReportorPhone()!=null&& !"".equals(prpLRegistVo.getReportorPhone())){// 报案人电话
			msgParamVo.setReportoMobile(prpLRegistVo.getReportorPhone());
		}
		// 驾驶人名称
		if(prpLRegistVo.getDriverName()!=null&&!"".equals(prpLRegistVo.getDriverName())){
			msgParamVo.setDriverName(prpLRegistVo.getDriverName());
		}
		// 驾驶人电话
		if(prpLRegistVo.getDriverPhone()!=null&&!"".equals(prpLRegistVo.getDriverPhone())){
			msgParamVo.setDriverPhone(prpLRegistVo.getDriverPhone());
		}
		if(prpLRegistVo.getPrpLRegistExt().getFrameNo() != null && 
				!"".equals(prpLRegistVo.getPrpLRegistExt().getFrameNo())){
			msgParamVo.setFrameNo(prpLRegistVo.getPrpLRegistExt().getFrameNo());
		}
		if(prpLRegistVo.getDamageCode()!=null){// 出险原因
			msgParamVo.setDamageReason(codeTranService.transCode("DamageCode", prpLRegistVo.getDamageCode()));
		}else{
			msgParamVo.setDamageReason("");
		}
		if(prpLRegistVo.getPrpLRegistExt() != null&&prpLRegistVo.getPrpLRegistExt().getInsuredName() != null)
			msgParamVo.setInsuredName(prpLRegistVo.getPrpLRegistExt().getInsuredName());
		else msgParamVo.setInsuredName("");
		if(prpLRegistVo.getInsuredPhone() != null)
			msgParamVo.setMobile(prpLRegistVo.getInsuredPhone());
		else 	msgParamVo.setMobile("");
		if(prpLRegistVo.getPrpLRegistExt() != null&&prpLRegistVo.getPrpLRegistExt().getLicenseNo() != null)
			msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
		else	msgParamVo.setLicenseNo("");
		msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
		if(prpLRegistVo.getDamageAddress() != null)
			msgParamVo.setDamageAddress(prpLRegistVo.getDamageAddress());
		else	msgParamVo.setDamageAddress("");
		if(prpLRegistVo.getLinkerName() != null)
			msgParamVo.setLinkerName(prpLRegistVo.getLinkerName());
		else 	msgParamVo.setLinkerName("");
		if(prpLRegistVo.getLinkerMobile() != null)
			msgParamVo.setLinkerMobile(prpLRegistVo.getLinkerMobile());
		else	msgParamVo.setLinkerMobile("");
		if(prpLRegistVo.getPrpLRegistCarLosses() != null && prpLRegistVo.getPrpLRegistCarLosses().size()>0){
			if(prpLRegistVo.getPrpLRegistCarLosses().get(0).getBrandName() != null){
				msgParamVo.setBrandName(prpLRegistVo.getPrpLRegistCarLosses().get(0).getBrandName());
			}
		}
		if("1101".equals(prpLRegistVo.getRiskCode())){
			msgParamVo.setRiskType("交强");
		}else{
			msgParamVo.setRiskType("商业");
		}
		// 主险金额
		if(prpLCMainVo != null){
		    if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
	            msgParamVo.setPrpCItemKinds(prpLCMainVo.getPrpCItemKinds());
	        }
		    if ("1".equals(prpLCMainVo.getIsCoreCustomer())) {
				msgParamVo.setIsCoreCustomer("是");
			} else {
				msgParamVo.setIsCoreCustomer("否");
			}
        } else {
			msgParamVo.setIsCoreCustomer("否");
		}
		
		// 保险期间
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
		if(cMainVoList!=null&&cMainVoList.size()>0){
			String insuredDate = this.getInsuredDate(cMainVoList);
			msgParamVo.setInsuredDate(insuredDate);
		}
		// 修理厂信息--推送修案件
		if(prpLRegistVo.getRepairId()!=null){
			PrpLRepairFactoryVo prpLRepairFactoryVo=repairFactoryService.findFactoryById(Long.toString(prpLRegistVo.getRepairId()));
			if(prpLRepairFactoryVo!=null&&prpLRepairFactoryVo.getId() != null){
				if(prpLRepairFactoryVo.getFactoryName() != null){
					msgParamVo.setRepairName(prpLRepairFactoryVo.getFactoryName());
				}
				if(prpLRepairFactoryVo.getAddress() != null){
					String areaAdress="";
					if(StringUtils.isNotBlank(prpLRepairFactoryVo.getAreaCode())){
						areaAdress=codeTranService.transCode("AreaCode",prpLRepairFactoryVo.getAreaCode());
					}
					if(StringUtils.isNotBlank(areaAdress)){
						msgParamVo.setRepairAddress(areaAdress+prpLRepairFactoryVo.getAddress());
					}else{
						msgParamVo.setRepairAddress(prpLRepairFactoryVo.getAddress());
					}
					
				}
				if(prpLRepairFactoryVo.getTelNo() != null){
					msgParamVo.setRepairMobile(prpLRepairFactoryVo.getTelNo());
				}
				if(prpLRepairFactoryVo.getLinker() != null){
					msgParamVo.setRepairLinker(prpLRepairFactoryVo.getLinker());
				}
			 }
		}else{// 普通案件
			if(prpLCMainVo!=null&&prpLCMainVo.getComCode()!=null){
				PrpLRepairFactoryVo repairFactoryVo = repairFactoryService.findFactory(prpLCMainVo.getComCode(), 
						prpLCMainVo.getAgentCode(),prpLCMainVo.getInsuredCode(), prpLCMainVo.getOperatorCode());
				if(repairFactoryVo!=null&&repairFactoryVo.getId() != null){
					if(repairFactoryVo.getFactoryName() != null){
						msgParamVo.setRepairName(repairFactoryVo.getFactoryName());
					}
					if(repairFactoryVo.getAddress() != null){
						String areaAdress="";
						if(StringUtils.isNotBlank(repairFactoryVo.getAreaCode())){
							areaAdress=codeTranService.transCode("AreaCode",repairFactoryVo.getAreaCode());
						}
						if(StringUtils.isNotBlank(areaAdress)){
							msgParamVo.setRepairAddress(areaAdress+repairFactoryVo.getAddress());
						}else{
							msgParamVo.setRepairAddress(repairFactoryVo.getAddress());
						}
					}
					if(repairFactoryVo.getTelNo() != null){
						msgParamVo.setRepairMobile(repairFactoryVo.getTelNo());
					}
					if(repairFactoryVo.getLinker() != null){
						msgParamVo.setRepairLinker(repairFactoryVo.getLinker());
					}
					// 代理人手机号码
					if(repairFactoryVo.getAgentPhone() != null){
						msgParamVo.setAgentPhone(repairFactoryVo.getAgentPhone());
					}
				} 
			}
		}
		
		// 伤亡人数
		if(prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size()>0){
			int thridInjuredcount = 0; // 三者车受伤人数
			int thridDeathcount = 0; // 三者车死亡人数
			int ItemInjuredcount = 0; // 标的车受伤人数
			int ItemDeathcount = 0; // 标的车死亡人数
			for(PrpLRegistPersonLossVo registPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
				if("1".equals(registPersonLossVo.getLossparty())){
					ItemInjuredcount=ItemInjuredcount+registPersonLossVo.getInjuredcount();
					ItemDeathcount=ItemDeathcount+registPersonLossVo.getDeathcount();
				}else{
					thridInjuredcount=thridInjuredcount+registPersonLossVo.getInjuredcount();
					thridDeathcount=thridDeathcount+registPersonLossVo.getDeathcount();
				}
			}
			msgParamVo.setThridInjuredcount(String.valueOf(thridInjuredcount));
			msgParamVo.setThridDeathcount(String.valueOf(thridDeathcount));
			msgParamVo.setItemInjuredcount(String.valueOf(ItemInjuredcount));
			msgParamVo.setItemDeathcount(String.valueOf(ItemDeathcount));
			msgParamVo.setInjuredcount(String.valueOf(thridInjuredcount+ItemInjuredcount));
			msgParamVo.setDeathcount(String.valueOf(thridDeathcount+ItemDeathcount));
		}
		// 出险次数
		Map<String,String> registTimesMap = this.getRegistTimesByPolicyNo(prpLRegistVo.getPolicyNo());
		msgParamVo.setRegistTimes_BI(registTimesMap.get("registTimes_BI"));
		msgParamVo.setRegistTimes_CI(registTimesMap.get("registTimes_CI"));
		
		// 承保险种
		String kindCode = sendMsgService.getKindName(prpLRegistVo.getRegistNo());
		msgParamVo.setKindCode(kindCode);
		
		return msgParamVo;
	}
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#saveOrUpdatePrpLRegist(ins.sino.claimcar.regist.vo.PrpLRegistVo)
	 */
	@Override
	public PrpLRegistVo saveOrUpdatePrpLRegist(PrpLRegistVo prpLRegistVo) {
		PrpLRegist prpLRegistPo = databaseDao.findByPK(PrpLRegist.class, prpLRegistVo.getRegistNo());
		Beans.copy().from(prpLRegistVo).excludeNull().to(prpLRegistPo);
		
		Beans.copy().from(prpLRegistVo.getPrpLRegistExt()).excludeNull().to(prpLRegistPo.getPrpLRegistExt());
		if(prpLRegistVo.getPrpLRegistExt().getPolicyNoLink()==null){
			prpLRegistPo.getPrpLRegistExt().setPolicyNoLink(prpLRegistVo.getPrpLRegistExt().getPolicyNoLink());
		}
		
		databaseDao.update(PrpLRegist.class,prpLRegistPo);
		prpLRegistVo = Beans.copyDepth().from(prpLRegistPo).to(PrpLRegistVo.class);
		return prpLRegistVo;
		
		
	}
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findPrploldclaimriskinfoByPolicyNo(java.lang.String)
	 */
	@Override
	public List<PrplOldClaimRiskInfoVo> findPrploldclaimriskinfoByPolicyNo(String policyNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", policyNo);
		List<PrplOldClaimRiskInfo> prpLRegistPos = databaseDao.findAll(PrplOldClaimRiskInfo.class, qr);
		List<PrplOldClaimRiskInfoVo> returnList = new ArrayList<PrplOldClaimRiskInfoVo>();
		for (PrplOldClaimRiskInfo po : prpLRegistPos) {
			if (po != null) {
				returnList.add(Beans.copyDepth().from(po).to(PrplOldClaimRiskInfoVo.class));
			}
		}
		return returnList;
	}
	
	
	/* (non-Javadoc)
	 * @see ins.sino.claimcar.regist.service.RegistService#findPrpLCMainVoByPolicyNo(java.lang.String)
	 */
	@Override
	public PrpLRegistAndCMainVo findPrpLCMainVoByPolicyNo(String policyNo) {
		long start = System.currentTimeMillis();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" FROM PrpLRegist regist,PrpLCMain prpLCMain");
		sqlUtil.append(" WHERE prpLCMain.registNo = regist.registNo ");
		
		sqlUtil.append(" AND regist.registTaskFlag != ?");
		sqlUtil.addParamValue("7");
	
		sqlUtil.append(" AND prpLCMain.policyNo = ?");
		sqlUtil.addParamValue(policyNo);

		
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		
		
		//Page<Object[]> page =(Page<Object[]>) databaseDao.findAllBySql(sql, values);
		List<Object[]> page = databaseDao.findAllByHql(sql, values);

		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@==aaaa====="+( System.currentTimeMillis()-start ));
		start = System.currentTimeMillis();
		List<PrpLCMainVo> prpLCMainVos = new ArrayList<PrpLCMainVo>();
		List<PrpLRegistVo> prpLRegistVos = new ArrayList<PrpLRegistVo>();
		PrpLRegistAndCMainVo resultVoList = new PrpLRegistAndCMainVo();
		for(int i = 0; i<page.size(); i++ ){
			PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
			PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
			Object[] obj = page.get(i);
			
			PrpLRegist prpLRegist = (PrpLRegist)obj[0];
			PrpLCMain prpLCMain = (PrpLCMain)obj[1];
			Beans.copy().from(prpLRegist).to(prpLRegistVo);
			Beans.copy().from(prpLCMain).to(prpLCMainVo);
			prpLRegistVos.add(prpLRegistVo);
			prpLCMainVos.add(prpLCMainVo);
			
		}
		resultVoList.setPrpLCMainVo(prpLCMainVos);
		resultVoList.setPrpLRegistVo(prpLRegistVos);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@==222====="+( System.currentTimeMillis()-start ));
		start = System.currentTimeMillis();
		return resultVoList;
	}
	
	public List<PrpLCItemKindVo> findCItemKindByPolicyNo(String registNo){
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		List<PrpLCItemKind> prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qr);
		List<PrpLCItemKindVo> prpLCItemKindVos = new ArrayList<PrpLCItemKindVo>();
		for(PrpLCItemKind prpLCItemKind:prpLCItemKinds){
			PrpLCItemKindVo vo =Beans.copyDepth().from(prpLCItemKind).to(PrpLCItemKindVo.class);
			prpLCItemKindVos.add(vo);
		}
		return prpLCItemKindVos;
	}
	
	/**
	 * 返回保险期间
	 * @param cMainList
	 * @return
	 */
	public String getInsuredDate(List<PrpLCMainVo> cMainList){
		String insuredDate = "";
		for(PrpLCMainVo prpLCMain:cMainList){
			if("1101".equals(prpLCMain.getRiskCode())){
				insuredDate = insuredDate+"(交强)"+DateUtils.dateToStr(prpLCMain.getStartDate(),"yyyy-MM-dd")+"至"+DateUtils.dateToStr(
						prpLCMain.getEndDate(),"yyyy-MM-dd");
			}else{
				insuredDate = insuredDate+"(商业)"+DateUtils.dateToStr(prpLCMain.getStartDate(),"yyyy-MM-dd")+"至"+DateUtils.dateToStr(
						prpLCMain.getEndDate(),"yyyy-MM-dd");
			}
		}
		return insuredDate;
	}
	
	@Override
	public Map<String,String> getRegistTimesByPolicyNo(String PolicyNo){
		Map<String,String> registTimesMap = new HashMap<String,String>();
		int registTimes_BI = 0;// 商业
		int registTimes_CI = 0;// 交强
		String relatedPolicyNo = policyQueryService.getRelatedPolicyNo(PolicyNo);
		List<String> policyNoList = new ArrayList<String>();
		policyNoList.add(PolicyNo);
		if(relatedPolicyNo!=null && !StringUtils.isEmpty(relatedPolicyNo)){
			policyNoList.add(relatedPolicyNo);
		}
		for(String policyno:policyNoList){
			// 查询旧理赔的出险次数
			List<PrplOldClaimRiskInfoVo> oldClaimList = this.findPrploldclaimriskinfoByPolicyNo(policyno);
			if(oldClaimList!=null&&oldClaimList.size()>0){
				if("1101".equals(oldClaimList.get(0).getRiskCode())){
					registTimes_CI = registTimes_CI+oldClaimList.size();
				}else{
					registTimes_BI = registTimes_BI+oldClaimList.size();
				}
			}
			// 查询新理赔的出险次数
			PrpLRegistAndCMainVo registAndCMainVo = this.findPrpLCMainVoByPolicyNo(policyno);
			List<PrpLCMainVo> prpLCMainVoList = registAndCMainVo.getPrpLCMainVo();
			if(prpLCMainVoList!=null&&prpLCMainVoList.size()>0){
				if("1101".equals(prpLCMainVoList.get(0).getRiskCode())){
					registTimes_CI = registTimes_CI+prpLCMainVoList.size();
				}else{
					registTimes_BI = registTimes_BI+prpLCMainVoList.size();
				}
			}
		}
		registTimesMap.put("registTimes_BI", String.valueOf(registTimes_BI));
		registTimesMap.put("registTimes_CI", String.valueOf(registTimes_CI));
		
		return registTimesMap;
	}
	
	// 保存短信内容yzy
	private void putSmsmessage(SendMsgParamVo msgParamVo,String moble,Date sendTime0,String smsContext,String status){
		//DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date trueSendTime = new Date();// 真实发送时间
		Date sendTime1=null;
		Date nowTime=new Date();
		if(sendTime0!=null){
			sendTime1 = DateUtils.addMinutes(sendTime0, -5);// 短信平台发送时间
		   if(nowTime.getTime()<sendTime1.getTime()){
			    trueSendTime=sendTime1;
			}
		}
		PrpsmsMessageVo prpsmsMessageVo=null;
		if(msgParamVo!=null){
			prpsmsMessageVo=new PrpsmsMessageVo();
			prpsmsMessageVo.setBusinessNo(msgParamVo.getRegistNo());
			prpsmsMessageVo.setComCode(msgParamVo.getComCode());
			prpsmsMessageVo.setCreateTime(nowTime);
			prpsmsMessageVo.setPhoneCode(moble);
			prpsmsMessageVo.setSendNodecode(FlowNode.Regis.toString());
			prpsmsMessageVo.setSendText(smsContext);
			prpsmsMessageVo.setTruesendTime(trueSendTime);
			prpsmsMessageVo.setUserCode(msgParamVo.getUseCode());
			prpsmsMessageVo.setBackTime(nowTime);
			prpsmsMessageVo.setStatus(status);
			msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
		}
	}
	
    @Override
    public ResultPage<PrpLCMainVo> findPrpLCMainVo(PrpLInsuredFactoryVo prpLInsuredFactoryVo,int start,int length) {
        SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append("select insuredCode, insuredName from PrpCMain cmain where 1=1 and insuredcode is not null  and insuredname is not null ");
        if(StringUtils.isNotBlank(prpLInsuredFactoryVo.getInsuredCode())){
            sqlUtil.append(" AND instr(cmain.insuredCode, ?) > 0 ");
            sqlUtil.addParamValue(prpLInsuredFactoryVo.getInsuredCode());
        }
       
        if(StringUtils.isNotBlank(prpLInsuredFactoryVo.getInsuredName())){
            sqlUtil.append(" AND instr(cmain.insuredName, ?) > 0 ");
            sqlUtil.addParamValue(prpLInsuredFactoryVo.getInsuredName());
        }
        sqlUtil.append(" AND cmain.agentCode = ? ");
        sqlUtil.addParamValue(prpLInsuredFactoryVo.getAgentCode());
        sqlUtil.append(" and instr(riskcode,?) = 1 ");
        sqlUtil.addParamValue("1");
        sqlUtil.append(" group by cmain.insuredCode,cmain.insuredName order by null");

        String sql = sqlUtil.getSql();
        Object[] values = sqlUtil.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
        if(page != null && page.getTotalCount() > 0){
            for(Object[] obj : page.getResult()){
                PrpLCMainVo lCMainVo = new PrpLCMainVo();
                lCMainVo.setInsuredCode(obj[0].toString());
                lCMainVo.setInsuredName(obj[1].toString());
                
                prpLCMainVoList.add(lCMainVo);
            }
        }

		// 返回结果ResultPage
        ResultPage<PrpLCMainVo> pageReturn = new ResultPage<PrpLCMainVo>(start,length,page.getTotalCount(),prpLCMainVoList);

        return pageReturn;
		/*        // 定义参数list，ps：执行查询时需要转换成object数组
		        List<Object> paramValues = new ArrayList<Object>();
		        // hql查询语句
		         StringBuffer queryString = new StringBuffer("select insuredCode, insuredName from PrpCMain cmain where 1=1 and insuredcode is not null  and insuredname is not null ");

		         if(StringUtils.isNotBlank(prpLInsuredFactoryVo.getInsuredCode())){
		             queryString.append(" AND cmain.insuredCode like ? ");
		             paramValues.add("%"+prpLInsuredFactoryVo.getInsuredCode()+"%");
		         }
		        
		         if(StringUtils.isNotBlank(prpLInsuredFactoryVo.getInsuredName())){
		             queryString.append(" AND cmain.insuredName like ? ");
		             paramValues.add("%"+prpLInsuredFactoryVo.getInsuredName()+"%");
		         }
		         queryString.append(" AND cmain.agentCode = ? ");
		         paramValues.add(prpLInsuredFactoryVo.getAgentCode());
		         sqlUtil.append(" and insuredcode like ? ");
		         sqlUtil.addParamValue("%"+userInfo+"%");
		         queryString.append(" and riskCode like ? ");
		         paramValues.add("1%");
		         // 执行查询
		         Page page = databaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		         Page pageReturn = assemblyPrpCmainInfo(page);
		        return pageReturn;*/
    
    }

    @Override
    public void updateScheduleTask(PrpLRegistVo registVo,PrpLScheduleTaskVo scheduleTaskVo,String url) {
    	List<PrplQuickUserVo> checkPerson = null;
        try{
			// 判断是否联共保案件：报案、调度提交不与移动排班系统交互，根据预配置调度人员提交调度；
        	if(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())){
				// 业务给出查勘人员
        		checkPerson = getGBCheckPerson(registVo,scheduleTaskVo.getPrpLScheduleItemses(),scheduleTaskVo);
        	}else{
        		personnelInformationService.getPersonnelInformation(registVo, scheduleTaskVo.getPrpLScheduleItemses(),scheduleTaskVo,url);
        	}
            scheduleService.saveScheduleTaskByVo(scheduleTaskVo);
            if(checkPerson!=null && !checkPerson.isEmpty()){
    			for(PrplQuickUserVo userVo : checkPerson){
    				quickUserService.updateTimes(userVo);
    			}
    		}
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<PrpLRegistVo> findPrplregistNo(String flag,Date startDate,Date endDate) {
        QueryRule qr = QueryRule.getInstance();
        qr.addEqual("flag", flag);
        qr.addGreaterThan("reportTime",startDate);
        qr.addLessThan("reportTime",endDate);
        List<PrpLRegist> prpLRegistList = databaseDao.findAll(
                PrpLRegist.class, qr);
        List<PrpLRegistVo> prpLRegistListVo = new ArrayList<PrpLRegistVo>();
        for(PrpLRegist po:prpLRegistList){
            PrpLRegistVo vo =Beans.copyDepth().from(po).to(PrpLRegistVo.class);
            prpLRegistListVo.add(vo);
        }
        return prpLRegistListVo;
    }
    
    
    /**
     * select  reg.*,ext.dangerremark ,carloss.licenseno ,carloss.licensetype  
	   from claimuser.prplregist reg ,claimuser.prplregistext ext , claimuser.prplregistcarloss carloss 
       where  reg.registno=ext.registno and reg.registno=carloss.registno 
          	   and not exists(select 1 from claimuser.prplregist reg1 where reg1.registno=reg.registno and reg1.registtaskflag='7' )
               and not exists(select 1 from claimuser.ciclaimplatformlog where reg.registno=bussno and requesttype='RegistBI' and status='1')
               and reporttime >=to_date('2018/9/13 00:00:00','YYYY/MM/DD hh24:MI:SS') 
               and reporttime <=to_date('2018/9/14 00:00:00','YYYY/MM/DD hh24:MI:SS')
               and comcode like'11%' ;
     */
	@Override
	public List<PrpLRegistVo> findPrpLRegistByHql(Map<String,String> map) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 定义参数list，ps：执行查询时需要转换成object数组
		SqlJoinUtils sqlutil = new SqlJoinUtils();
		List<Object> paramValues = new ArrayList<Object>();
		sqlutil.append(" from PrpLRegist reg ,PrpLRegistExt ext,PrpLRegistCarLoss carloss where  reg.registNo=ext.registNo and reg.registNo=carloss.prpLRegist.registNo");
		// 筛选出有效报案号
		sqlutil.append(" and not exists(select 1 from PrpLRegist reg1 where reg1.registNo=reg.registNo and reg1.registTaskFlag= ? )");
		sqlutil.addParamValue(map.get("registTaskFlag").trim());
		// 筛选出未送或者未送成功报案号
		sqlutil.append(" and not exists(select 1 from ClaimInterfaceLog where reg.registNo=registNo and businessType in (?,?) and status in (?,?) )");
		sqlutil.addParamValue(map.get("requestType1").trim());
		sqlutil.addParamValue(map.get("requestType2").trim());
		sqlutil.addParamValue(map.get("status1").trim());
		sqlutil.addParamValue(map.get("status2").trim());
		// 筛选指定时间
		sqlutil.append(" and reg.reportTime >=?");
		sqlutil.append(" and reg.reportTime <=?");
		try{
			sqlutil.addParamValue(sdf.parse(map.get("startDate").trim()));
			sqlutil.addParamValue(sdf.parse(map.get("endDate").trim()));
		}catch(ParseException e){
			e.printStackTrace();
		}
		// 筛选出accidentno为空的
		sqlutil.append(" and accidentno is null");
		// 筛选出出险机构
		sqlutil.append(" and comCode like ?");
		sqlutil.addParamValue(map.get("comCode").trim()+"%");
		String sql = sqlutil.getSql();
		Object[] objs = sqlutil.getParamValues();

		List<Object[]> object = databaseDao.findAllByHql(sql,objs);
		List<PrpLRegistVo> prplRegistVos = assemblyRegistInfo(object);
		return prplRegistVos;
	}

	private List<PrpLRegistVo> assemblyRegistInfo(List<Object[]> object) {
		Set<PrpLRegist> prpLRegists = new HashSet<PrpLRegist>();
		List<PrpLRegistCarLoss> carLosss = new ArrayList<PrpLRegistCarLoss>();
		if(object!=null&& !object.isEmpty()){
			for(int i = 0; i<object.size(); i++ ){
				Object[] objects = object.get(i);
				if(objects[0]!=null&&objects[1]!=null&&objects[2]!=null){
					PrpLRegist reg = (PrpLRegist)objects[0];
					PrpLRegistExt ext = (PrpLRegistExt)objects[1];
					PrpLRegistCarLoss loss = (PrpLRegistCarLoss)objects[2];
					// 三张表拿到，将regVo放进set
					reg.setPrpLRegistExt(ext);
					prpLRegists.add(reg);
					carLosss.add(loss);
				}
			}
			// 通过报案号将carloss设置进Regist
			for(PrpLRegist prpLRegist:prpLRegists){
				List<PrpLRegistCarLoss> carlossList = new ArrayList<PrpLRegistCarLoss>();
				for(PrpLRegistCarLoss prplCarLoss:carLosss){
					if(prpLRegist.getRegistNo().equals(prplCarLoss.getPrpLRegist().getRegistNo())){
						carlossList.add(prplCarLoss);
					}
				}
				prpLRegist.setPrpLRegistCarLosses(carlossList);
			}
		}
		List<PrpLRegist> prpLRegistList = new ArrayList<PrpLRegist>();
		prpLRegistList.addAll(prpLRegists);
		List<PrpLRegistVo> list = Beans.copyDepth().from(prpLRegistList).toList(PrpLRegistVo.class);
		if(list!=null&& !list.isEmpty()){
			return list;
		}
		return null;
	}

	private List<PrplQuickUserVo> getGBCheckPerson(PrpLRegistVo registVo,List<PrpLScheduleItemsVo> itemsVoList,PrpLScheduleTaskVo scheduleTaskVo) {
		List<PrplQuickUserVo> list = new ArrayList<PrplQuickUserVo>();
		// 业务给出查勘人员,此处只有查勘
			for(PrpLScheduleItemsVo sceduleItem:itemsVoList){
				PrplQuickUserVo checkPerson = null;
				if("4".equals(sceduleItem.getItemType())){
					checkPerson = quickUserService.findQuickUserByGBFlag("2",registVo.getComCode());
				}else {
					checkPerson = quickUserService.findQuickUserByGBFlag("1",registVo.getComCode());
				}
				if(checkPerson!=null){
					list.add(checkPerson);
					sceduleItem.setScheduledUsercode(checkPerson.getUserCode());
					sceduleItem.setScheduledComcode(checkPerson.getComCode());
					if(StringUtils.isNotBlank(checkPerson.getPhone())){
						sceduleItem.setMoblie(checkPerson.getPhone());
						scheduleTaskVo.setRelateHandlerMobile(checkPerson.getPhone());
					}
				}
			}
		return list;
	}

	@Override
	public void validateRegist(BiCiPolicyVo policyVo) throws Exception {
		PrpLCMainVo prpLCMainVoB = new PrpLCMainVo();// 商业保单信息
		PrpLCMainVo prpLCMainVoC=new PrpLCMainVo();
		Date nowDate=new Date();
		if(policyVo!=null && StringUtils.isNotBlank(policyVo.getBipolicyNo())){
			prpLCMainVoB=registQueryService.findPrpCmainByPolicyNo(policyVo.getBipolicyNo());
		}
		if(policyVo!=null && StringUtils.isNotBlank(policyVo.getCipolicyNo())){
			prpLCMainVoC=registQueryService.findPrpCmainByPolicyNo(policyVo.getBipolicyNo());
		}
		if(policyVo!=null && "1".equals(policyVo.getIsDamageFlag())){
			throw new Exception("自助案件不能包含人伤！");
		}
		
//		if(StringUtils.isNotBlank(policyVo.getCipolicyNo())){
//			throw new Exception("单车事故，请取消关联交强险！");
//		}
		if(StringUtils.isBlank(policyVo.getBipolicyNo())){
			throw new Exception("自助案件,保单不能为空！");
		}
		
		// 出险时间不能大于系统当前时间
		if(policyVo!=null && policyVo.getDamageTime()!=null){
			if(policyVo.getDamageTime().getTime()>nowDate.getTime()){
				throw new Exception("出险时间不能大于系统当前时间！"+nowDate);
			}
		}
		if(policyVo!=null && policyVo.getDamageTime()!=null && policyVo.getReportTime()!=null){
			if(policyVo.getReportTime().getTime()<policyVo.getDamageTime().getTime()){
				throw new Exception("报案时间不能早于出险时间！");
			}
		}
		// 交强险商业险保单不是同一家分公司
		if(prpLCMainVoB!=null && prpLCMainVoC!=null && prpLCMainVoB.getId()!=null && prpLCMainVoC.getId()!=null && StringUtils.isNotBlank(prpLCMainVoB.getComCode()) && StringUtils.isNotBlank(prpLCMainVoC.getComCode())){
			if("0002".equals(prpLCMainVoB.getComCode().substring(0,4))||"0002".equals(prpLCMainVoC.getComCode().substring(0,4))){// 深圳
				if(!prpLCMainVoB.getComCode().substring(0,4).equals(prpLCMainVoC.getComCode().substring(0,4))){
					throw new Exception("交强险商业险保单不是同一家分公司,不能报案");
				  }
			}else{
				if(!prpLCMainVoB.getComCode().substring(0,2).equals(prpLCMainVoC.getComCode().substring(0,2))){
					throw new Exception("交强险商业险保单不是同一家分公司,不能报案");
					
				}
			}
		}
		// 保单车架号不一致不能报案
		if(prpLCMainVoB!=null && prpLCMainVoC!=null && prpLCMainVoB.getId()!=null && prpLCMainVoC.getId()!=null){
			List<PrpLCItemCarVo> carVos=prpLCMainVoB.getPrpCItemCars();
			if(carVos!=null && carVos.size()>1){
				PrpLCItemCarVo itemCarVoA=carVos.get(0);
				PrpLCItemCarVo itemCarVoB=carVos.get(1);
				if(itemCarVoA!=null && itemCarVoB!=null){
					if(StringUtils.isNotBlank(itemCarVoA.getFrameNo()) && !itemCarVoA.getFrameNo().equals(itemCarVoB.getFrameNo())){
						throw new Exception("保单车架号不一致,不能报案");
					}
				}
			}
		}
		
		// 校验保单是否有效
		List<String> policies = new ArrayList<String>();
		if(StringUtils.isNotBlank(policyVo.getBipolicyNo())){
			boolean validFlag = policyQueryService.existPolicy(policyVo.getBipolicyNo(), policyVo.getDamageTime());
			if(!validFlag){
				throw new Exception(policyVo.getBipolicyNo()+"出险时间不在保单起保与终保时间内,不能报案!");
			}else{
				policies.add(policyVo.getBipolicyNo());
			}
		}
		if(StringUtils.isNotBlank(policyVo.getCipolicyNo())){
			boolean validFlag = policyQueryService.existPolicy(policyVo.getCipolicyNo(),policyVo.getDamageTime());
			if(!validFlag){
				throw new Exception(policyVo.getCipolicyNo()+"出险时间不在保单起保与终保时间内,不能报案!");
			}else{
				policies.add(policyVo.getCipolicyNo());
			}
		}
		// 判断是否出险时间48小时内已进行报案，案件状态为有效（未注销，未结案）
		List<PrpLRegistVo> registVoList = registQueryService.findValidCase(policies,policyVo.getDamageTime());
		if(registVoList!=null && registVoList.size()>0){
			throw new Exception("该车48小时内已报过案，请联系保险公司进行处理!");
		}
		
	}

	@Override
	public void updatePrpLRegistExt(PrpLRegistExtVo prpLRegistExtVo) {
		if(StringUtils.isNotBlank(prpLRegistExtVo.getRegistNo())){
			PrpLRegistExt po=Beans.copyDepth().from(prpLRegistExtVo).to(PrpLRegistExt.class);
			databaseDao.update(PrpLRegistExt.class, po);
		}
		
	}
	
	/**
	 * 判断案件是否是单车事故，无人伤无物损
	 */
	@Override
	public Boolean isSelfHelpSurVey(String registNo) {
		PrpLRegistVo registVo = findRegistByRegistNo(registNo);
		if(registVo != null){
			boolean isDamage = false;
			boolean isPersonLoss = false;
			if(registVo.getPrpLRegistPropLosses() != null && registVo.getPrpLRegistPropLosses().size()>0){
				for(PrpLRegistPropLossVo prpLRegistPropLossVo : registVo.getPrpLRegistPropLosses()){
					if("1".equals(prpLRegistPropLossVo.getDamagelevel())){// 只要一个物损为有损失，就是有物损
						isDamage = true;
						break;
					}
				}
			}
			if(registVo.getPrpLRegistPersonLosses() != null && registVo.getPrpLRegistPersonLosses().size() >0){
				for(PrpLRegistPersonLossVo prplRegistPersonLossVo : registVo.getPrpLRegistPersonLosses()){
					if(prplRegistPersonLossVo.getInjuredcount()+prplRegistPersonLossVo.getDeathcount()>0){
						isPersonLoss = true;
						break;
					}
				}
			}
			if((registVo.getPrpLRegistPropLosses() == null || registVo.getPrpLRegistPropLosses().size() <= 0 || !isDamage) 
					&& (registVo.getPrpLRegistPersonLosses() == null || registVo.getPrpLRegistPersonLosses().size() <= 0 || !isPersonLoss)
					&& (registVo.getPrpLRegistCarLosses() != null && registVo.getPrpLRegistCarLosses().size() == 1)){
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> findLevelTwoDisasterInfo(String disasterOneCode) {
		List<String> list = new ArrayList<String>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select b.catastrophetype, b.name from ywuser.prpdcatastrophe b ");
		sqlUtil.append(" where b.catastrophetype = ?   ");
		sqlUtil.addParamValue(disasterOneCode);

		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<Object[]> objects = databaseDao.findRangeBySql(sql, 0, 1000, values);
		for (Object[] object : objects) {
			String codecode = (String) object[0];
			String codename = (String) object[1];
			list.add(codecode+"-"+codename);
		}

		return list;
	}
	
	@Override
	public List<PrpLCItemKindVo> findPrpLCItemKindByPolicyNo(String policyNo) {
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("policyNo", policyNo);
		List<PrpLCItemKind> prpLCItemKinds = databaseDao.findAll(PrpLCItemKind.class, qr);
		List<PrpLCItemKindVo> prpLCItemKindVos = new ArrayList<PrpLCItemKindVo>();
		for(PrpLCItemKind prpLCItemKind:prpLCItemKinds){
			PrpLCItemKindVo vo =Beans.copyDepth().from(prpLCItemKind).to(PrpLCItemKindVo.class);
			prpLCItemKindVos.add(vo);
		}
		return prpLCItemKindVos;
	}

	/**
	 * 根据平安联盟案件号查询是否已存在
	 * @param paicReportNo
	 * @return
	 */
	@Override
	public PrpLRegistVo findRegistByPaicReportNo(String paicReportNo) {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("paicReportNo",paicReportNo);
		List<PrpLRegist> prpLRegistList = databaseDao.findAll(PrpLRegist.class, queryRule);

		PrpLRegistVo vo = null;
		if(prpLRegistList !=null && prpLRegistList.size()>0){
			vo = Beans.copyDepth().from(prpLRegistList.get(0)).to(PrpLRegistVo.class);
		}
		return vo;
	}
}
