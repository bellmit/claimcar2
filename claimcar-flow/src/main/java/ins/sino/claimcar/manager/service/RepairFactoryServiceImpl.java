package ins.sino.claimcar.manager.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claimjy.util.JyHttpUtil;
import ins.sino.claimcar.claimjy.vo.JyReqHeadVo;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.claimjy.vo.repair.FactoryInfo;
import ins.sino.claimcar.claimjy.vo.repair.Item;
import ins.sino.claimcar.claimjy.vo.repair.JyFactoryBrandAndInfoBodyVo;
import ins.sino.claimcar.claimjy.vo.repair.JyFactoryBrandAndInfoPacketVo;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.manager.po.PrpLAgentFactory;
import ins.sino.claimcar.manager.po.PrpLInsuredFactory;
import ins.sino.claimcar.manager.po.PrpLRepairBrand;
import ins.sino.claimcar.manager.po.PrpLRepairFactory;
import ins.sino.claimcar.manager.po.PrpLRepairPhone;
import ins.sino.claimcar.manager.po.PrplSysAgentfactory;
import ins.sino.claimcar.manager.vo.PrpLAgentFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLInsuredFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairBrandVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairPhoneVo;
import ins.sino.claimcar.manager.vo.PrplSysAgentfactoryVo;
import ins.sino.claimcar.regist.po.PrplCMain;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.Path;

import ins.sino.claimcar.utils.HttpUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;


@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("RepairFactoryService")
public class RepairFactoryServiceImpl implements RepairFactoryService {

	@Autowired
	DatabaseDao dataBaseDao;
	
	@Autowired
	BaseDaoService baseDaoService;
	
	@Autowired
	CodeTranService codeTranService;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(RepairFactoryServiceImpl.class);
	
	/*
	 * 查询所有修理厂
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public Page<PrpLRepairFactoryVo> findAllFactory(PrpLRepairFactoryVo repairFactoryVo, int start,int length) {
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpLRepairFactory pf where 1=1 ");
		// 类型
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryType())) {
			queryString.append(" AND pf.factoryType = ? ");
			paramValues.add(repairFactoryVo.getFactoryType());
		}
		// 代码
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryCode())) {
			queryString.append(" AND pf.factoryCode like ? ");
			paramValues.add(repairFactoryVo.getFactoryCode() + "%");
		}
		// 有效无效标志
		if(StringUtils.isNotBlank(repairFactoryVo.getValidStatus())){
			queryString.append(" AND pf.validStatus = ? ");
			paramValues.add(repairFactoryVo.getValidStatus());
		}
		// 名称
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryName())) {
			queryString.append(" AND pf.factoryName like ? ");
			paramValues.add("%" + repairFactoryVo.getFactoryName() + "%");
		}
		// 地址
		if (StringUtils.isNotBlank(repairFactoryVo.getAddress())) {
			queryString.append(" AND pf.address like ? ");
			paramValues.add("%" + repairFactoryVo.getAddress() + "%");
		}
		// 删除状态的修理厂不展示
		queryString.append(" and pf.validStatus != 2 ");
		queryString.append(" Order By pf.id ");
		
		// 执行查询
		Page page = dataBaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
		Page pageReturn = assemblyPolicyInfo(page, repairFactoryVo);
		// 返回结果ResultPage
		return page;
	}
	
	

	/*
	 * 查询所有修理厂
	 */
	@Override
	public List<PrpLRepairFactoryVo> findAllFactory(PrpLRepairFactoryVo repairFactoryVo) {
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpLRepairFactory pf where 1=1 ");
		// 类型
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryType())) {
			queryString.append(" AND pf.factoryType = ? ");
			paramValues.add(repairFactoryVo.getFactoryType());
		}
		// 代码
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryCode())) {
			queryString.append(" AND pf.factoryCode like ? ");
			paramValues.add(repairFactoryVo.getFactoryCode() + "%");
		}
		// 有效无效标志
		if(StringUtils.isNotBlank(repairFactoryVo.getValidStatus())){
			queryString.append(" AND pf.validStatus = ? ");
			paramValues.add(repairFactoryVo.getValidStatus());
		}
		// 名称
		if (StringUtils.isNotBlank(repairFactoryVo.getFactoryName())) {
			queryString.append(" AND pf.factoryName like ? ");
			paramValues.add(repairFactoryVo.getFactoryName() + "%");
		}
		// 地址
		if (StringUtils.isNotBlank(repairFactoryVo.getAddress())) {
			queryString.append(" AND pf.address like ? ");
			paramValues.add(repairFactoryVo.getAddress() + "%");
		}
        // 删除状态的修理厂不展示
        queryString.append(" and pf.validStatus != 2 ");
		queryString.append(" Order By pf.id ");
		// 执行查询
		List<PrpLRepairFactoryVo> prpLRepairFactoryList = new ArrayList<PrpLRepairFactoryVo>();
		List<PrpLRepairFactory> prpLRepairFactoryPo =  dataBaseDao.findAllByHql(PrpLRepairFactory.class,queryString.toString(), paramValues.toArray());
//		Date d1 = new Date();
		if(prpLRepairFactoryPo != null && prpLRepairFactoryPo.size() > 0){
			for(PrpLRepairFactory po : prpLRepairFactoryPo){
				PrpLRepairFactoryVo prpLRepairFactoryVo = new PrpLRepairFactoryVo();
				prpLRepairFactoryVo = Beans.copyDepth().from(po).to(PrpLRepairFactoryVo.class);
				//翻译
				String comCodeName = codeTranService.transCode("ComCodeFull", prpLRepairFactoryVo.getComCode());
				prpLRepairFactoryVo.setComCode(comCodeName);
				if("001".equals(prpLRepairFactoryVo.getFactoryType())){ // 翻译修理厂类型 
					prpLRepairFactoryVo.setFactoryType("特约维修站");
				}else if("002".equals(prpLRepairFactoryVo.getFactoryType())){
					prpLRepairFactoryVo.setFactoryType("一类修理厂");
				}else{
					prpLRepairFactoryVo.setFactoryType("二类修理厂");
				}
				if("0".equals(prpLRepairFactoryVo.getValidStatus())){
					prpLRepairFactoryVo.setValidStatus("无效");
				}else if("1".equals(prpLRepairFactoryVo.getValidStatus())){
					prpLRepairFactoryVo.setValidStatus("有效");
				}else{
					prpLRepairFactoryVo.setValidStatus("所有");
				}
				prpLRepairFactoryList.add(prpLRepairFactoryVo);
			}
		}
//		Date d2 = new Date();
//		System.out.println("翻译耗时：     "+(d2.getTime() - d1.getTime())/1000+"    秒");
		return prpLRepairFactoryList;
	}
	/*
	 *  循环查询结果集合
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Page assemblyPolicyInfo(Page page,PrpLRepairFactoryVo repairFactoryVo) {
		for (int i = 0; i < page.getResult().size(); i++) {
			PrpLRepairFactoryVo plyVo = new PrpLRepairFactoryVo();
			PrpLRepairFactory pm = (PrpLRepairFactory) page.getResult().get(i);
			Beans.copy().from(pm).to(plyVo);
			page.getResult().set(i, plyVo);
		}
		return page;
	}
	
	
	@Override
	public PrpLRepairFactoryVo findFactoryById(String repairId){
		PrpLRepairFactoryVo plyVo = null;
		if(StringUtils.isNotBlank(repairId)){
			Long intermID = Long.parseLong(repairId);
			PrpLRepairFactory repairFactory = dataBaseDao.findByPK(PrpLRepairFactory.class,intermID);
			if(repairFactory != null){
				plyVo = new PrpLRepairFactoryVo();
				plyVo = Beans.copyDepth().from(repairFactory).to(PrpLRepairFactoryVo.class);
			}
		}
		
		return plyVo;
	}
	
	/*
	 * 异步查询factorycode是否在数据库中存在
	 */
	@Override
	public boolean findFactoryCode(String factoryCode,Long id){
		QueryRule qr=QueryRule.getInstance();
		qr.addEqual("factoryCode",factoryCode);
		qr.addNotEqual("validStatus", "2");
		if(id!=null && StringUtils.isNotBlank(id.toString())){
	    qr.addNotEqual("id",id);
		}
		List<PrpLRepairFactory> list=dataBaseDao.findAll(PrpLRepairFactory.class,qr);
		return list.size()>0 ? false : true;
	}
	
	public PrpLRepairFactoryVo savaOrUpData(PrpLRepairFactoryVo repairFactoryVo,
	      List<PrpLRepairBrandVo> repairBrandVos,List<PrpLRepairPhoneVo> repairPhoneVos,String userCode){
		Long repairId = repairFactoryVo.getId();
		Date date = new Date();
		repairFactoryVo.setUpdateUser(userCode);
		repairFactoryVo.setUpdateTime(date);
		repairFactoryVo.setCreateUser(userCode);
		repairFactoryVo.setCreateTime(date);
		
		PrpLRepairFactory repairFactory = null;
		List<PrpLRepairBrand> brandPos = null;
		List<PrpLRepairPhone> phonePos = null;
		if(repairId == null){
			repairFactory = Beans.copyDepth().from(repairFactoryVo).to(PrpLRepairFactory.class);
			repairFactory.setFlag(CodeConstants.ValidFlag.VALID);
			
			brandPos = new ArrayList<PrpLRepairBrand>();
			for(PrpLRepairBrandVo repairBrandVo : repairBrandVos){
				PrpLRepairBrand brandPo = new PrpLRepairBrand();
				Beans.copy().from(repairBrandVo).to(brandPo);
				brandPo.setPrpLRepairFactory(repairFactory);
				brandPos.add(brandPo);
			}
			
			phonePos = new ArrayList<PrpLRepairPhone>();
			for(PrpLRepairPhoneVo repairPhoneVo : repairPhoneVos){
				PrpLRepairPhone phonePo = new PrpLRepairPhone();
				repairPhoneVo.setCreateTime(new Date());
				repairPhoneVo.setUpdateTime(new Date());
				Beans.copy().from(repairPhoneVo).to(phonePo);
				phonePo.setPrpLRepairFactory(repairFactory);
				phonePos.add(phonePo);
			}
			repairFactory.setPrpLRepairPhones(phonePos);
		}else{
			repairFactory = dataBaseDao.findByPK(PrpLRepairFactory.class,repairId);
			Beans.copy().from(repairFactoryVo).excludeNull().to(repairFactory);
			
			brandPos = repairFactory.getPrpLRepairBrands();
			this.mergeList(repairFactory,repairBrandVos,brandPos,"id",PrpLRepairBrand.class,"setPrpLRepairFactory");
		
			phonePos = repairFactory.getPrpLRepairPhones();
			for(PrpLRepairPhoneVo repairPhoneVo : repairPhoneVos){
				repairPhoneVo.setUpdateTime(new Date());
			}
			this.mergeList(repairFactory,repairPhoneVos,phonePos,"id",PrpLRepairPhone.class,"setPrpLRepairFactory");
			
		}
		
		// PoPropertyUtil.setProperty(PrpLRepairFactory.class,repairFactory);
		dataBaseDao.save(PrpLRepairFactory.class,repairFactory);
		repairFactoryVo.setId(repairFactory.getId());
		return repairFactoryVo;
	}
	
	@Override
	public void updateRepairFactory(Long repairId,String userCode){
		PrpLRepairFactory repairPo = null;
		repairPo = dataBaseDao.findByPK(PrpLRepairFactory.class,repairId);
		String oldStatus = repairPo.getValidStatus();
		repairPo.setValidStatus(StringUtils.isEmpty(oldStatus)||"1".equals(oldStatus) ? "0" : "1");
		repairPo.setUpdateUser(userCode);
		repairPo.setUpdateTime(new Date());
		dataBaseDao.update(PrpLRepairFactory.class,repairPo);
	}
	
	@Override
	public PrpLRepairFactoryVo findFactoryByCode(String factoryCode,String factoryType){
		PrpLRepairFactoryVo repairFactoryVo = null;
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("factoryCode",factoryCode);
		if(StringUtils.isNotEmpty(factoryType)){
			rule.addEqual("factoryType",factoryType);
		}
		List<PrpLRepairFactory> repairFactory = 
		dataBaseDao.findAll(PrpLRepairFactory.class,rule);
		if(repairFactory != null && repairFactory.size() > 0){
			repairFactoryVo = Beans.copyDepth().from(repairFactory.get(0))
					.to(PrpLRepairFactoryVo.class);
		}
		return repairFactoryVo;
	}
	
	@Override
	public List<PrpLRepairFactoryVo> findFactoryByAreaCode(String areaCode,String brandName){
		List<PrpLRepairFactoryVo> voList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("areaCode",areaCode);
		queryRule.addNotEqual("validStatus", "2");
		List<PrpLRepairFactory> list = dataBaseDao.findAll(PrpLRepairFactory.class,queryRule);
		if(list!=null&&list.size()>0){
			voList = new ArrayList<PrpLRepairFactoryVo>();
			for(PrpLRepairFactory repairFactory : list){
				boolean flag = false;
				if(brandName != null){
					for(PrpLRepairBrand brand : repairFactory.getPrpLRepairBrands()){
						if(brandName.equals(brand.getBrandName())){
							flag = true;
						}
					}
				}
				if(flag){
					PrpLRepairFactoryVo repairVo = new PrpLRepairFactoryVo();
					repairVo = Beans.copyDepth().from(repairFactory).to(PrpLRepairFactoryVo.class);
					voList.add(repairVo);
				}
			}
		}
		return voList;
	}
	
	/*
	 * 车辆定损页面查询修理厂
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<PrpLRepairFactoryVo> findRepariFactory(PrpLRepairFactoryVo PrpLRepairFactoryVo, int start,int length,String index) {
		// 定义参数list，ps：执行查询时需要转换成object数组
		List<Object> paramValues = new ArrayList<Object>();
		// hql查询语句
		StringBuffer queryString = new StringBuffer("from PrpLRepairFactory pf where 1=1 ");
		// 代码
		if(StringUtils.isNotBlank(PrpLRepairFactoryVo.getAreaCode())){
			if(index.equals("0")){
			  queryString.append("AND pf.areaCode = ? ");
			  paramValues.add(PrpLRepairFactoryVo.getAreaCode());
			}else{
		      String areaCodeStr=PrpLRepairFactoryVo.getAreaCode().substring(0,4);
		      queryString.append("AND pf.areaCode like ? ");
		      paramValues.add(areaCodeStr+"%");
		   }
			
		}
		
		//不控制机构
//		if(StringUtils.isNotBlank(PrpLRepairFactoryVo.getComCode())){
//			queryString.append("AND pf.comCode = ? ");
//			String comCode = PrpLRepairFactoryVo.getComCode();
//			comCode = comCode.substring(0,4)+"0000";
//			paramValues.add(comCode);
//		}
		// 名称
		if(StringUtils.isNotBlank(PrpLRepairFactoryVo.getFactoryName())){
			queryString.append(" AND ( pf.factoryName like ? ");
			paramValues.add("%"+PrpLRepairFactoryVo.getFactoryName() + "%");
		
			queryString.append(" or pf.factoryCode like ? ) ");
			paramValues.add("%"+PrpLRepairFactoryVo.getFactoryName() + "%");
		}
		queryString.append(" AND  pf.validStatus = ? ) ");
		paramValues.add("1");
		
		
		queryString.append(" Order By pf.id ");
		
		logger.debug("sql="+queryString);
		logger.debug("ParamValues="+ArrayUtils.toString(paramValues));
		// 执行查询
		Page page = dataBaseDao.findPageByHql(queryString.toString(), (start
				/ length + 1), length, paramValues.toArray());
		Page pageReturn = assemblyPolicyInfo(page, PrpLRepairFactoryVo);
		// 返回结果ResultPage
		return pageReturn;
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	private void mergeList(PrpLRepairFactory repairFactory,List voList, List poList, String idName,Class paramClass, String method){
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
					dataBaseDao.deleteByObject(paramClass,element);
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
					setMethod = paramClass.getDeclaredMethod(method, repairFactory.getClass());
					setMethod.invoke(poElement,repairFactory);
					
					poList.add(poElement);
				}
			}catch(Exception e){
				throw new IllegalArgumentException(e);
			}
		}
	}
	public Map<String, String> findAgentCode(String comCode){
		//String sql = "select a.agentcode from prpdagent a where 1=1";
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT t.agentCode,t.agentName FROM prpdagent t WHERE 1=1 ");
		sqlUtil.append("AND  t.comcode like ?");
		sqlUtil.addParamValue(comCode.substring(0, 2)+"%");
		
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql, values);
		Map<String, String> resultMap = new HashMap<String, String>();
		for(int i = 0 ; i < objects.size() ; i++){
			Object[] obj = objects.get(i);
			resultMap.put(obj[0].toString(), obj[1].toString());
		}
				
				
		return resultMap;
	}

	// todo 调度修理厂
	@Override
	public PrpLRepairFactoryVo findFactory(String comCode,String agentCode,String insuredCode , String userCode){
		if(comCode == null || "".equals(comCode)){
			return null;
		}
		String subComcode = comCode.substring(0, 2);
		if("00".equals(subComcode)){
			subComcode = comCode.substring(0, 4);
		}
		//先根据代理人查询有没有配置被保险人
		QueryRule qr=QueryRule.getInstance();
		qr.addEqual("agentCode", agentCode);
		List<PrpLInsuredFactory> prpLInsuredFactoryList = dataBaseDao.findAll(PrpLInsuredFactory.class, qr);
		/*查询修理厂开始*/
		PrpLRepairFactoryVo prpLRepairFactoryVo = new PrpLRepairFactoryVo();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		if(prpLInsuredFactoryList!=null && prpLInsuredFactoryList.size()>0){
			sqlUtil.append(" from PrpLRepairFactory repair,PrpLAgentFactory agent,PrpLInsuredFactory insured where 1=1 ");
			sqlUtil.append(" and repair.id=agent.factoryId and repair.id=insured.factoryId ");
			sqlUtil.append(" and agent.factoryId=insured.factoryId ");
			if(StringUtils.isNotBlank(insuredCode)){
				sqlUtil.append(" and insured.insuredCode = ?");
				sqlUtil.addParamValue(insuredCode);
			}
		}else{
			sqlUtil.append(" from PrpLRepairFactory repair,PrpLAgentFactory agent where 1=1 ");
			sqlUtil.append(" and repair.id=agent.factoryId  ");
		}
		sqlUtil.append(" and repair.validStatus=?");
		sqlUtil.addParamValue("1");
		sqlUtil.append(" and repair.comCode like ?");
		sqlUtil.addParamValue(subComcode+"%");
		//增加出单工号限定条件
		sqlUtil.append(" and agent.userCode =?");
		sqlUtil.addParamValue(userCode);
		sqlUtil.append(" order by repair.createTime desc ");
		String sql = sqlUtil.getSql();
		System.out.println("**********************"+sql);
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> list = dataBaseDao.findAllByHql(sql, values);
		if(list.size() > 0){
			Object[] obj = list.get(0);
			PrpLRepairFactory prpLRepairFactory = (PrpLRepairFactory)obj[0];
			PrpLAgentFactory prpLAgentFactory = (PrpLAgentFactory)obj[1];
			Beans.copy().from(prpLRepairFactory).to(prpLRepairFactoryVo);
			prpLRepairFactoryVo.setAgentPhone(prpLAgentFactory.getAgentPhone());
		}else{
			//如果没有查到数据，则增加代理人，出单人为空查询条件再查一次
			SqlJoinUtils sqlUtil_second=new SqlJoinUtils();
			if(prpLInsuredFactoryList!=null && prpLInsuredFactoryList.size()>0){
				sqlUtil_second.append(" from PrpLRepairFactory repair,PrpLAgentFactory agent,PrpLInsuredFactory insured where 1=1 ");
				sqlUtil_second.append(" and repair.id=agent.factoryId  and repair.id=insured.factoryId ");
				if(StringUtils.isNotBlank(insuredCode)){
					sqlUtil_second.append(" and insured.insuredCode = ?");
					sqlUtil_second.addParamValue(insuredCode);
				}
			}else{
				sqlUtil_second.append(" from PrpLRepairFactory repair,PrpLAgentFactory agent where 1=1 ");
				sqlUtil_second.append(" and repair.id=agent.factoryId  ");
			}
			sqlUtil_second.append(" and agent.agentCode=?");
			sqlUtil_second.addParamValue(agentCode);
			sqlUtil_second.append(" and repair.validStatus=?");
			sqlUtil_second.addParamValue("1");
			sqlUtil_second.append(" and repair.comCode like ?");
			sqlUtil_second.addParamValue(subComcode+"%");
			//增加出单工号限定条件
			sqlUtil_second.append(" and agent.userCode is null");
			sqlUtil_second.append(" order by repair.createTime desc ");
			String sql_third = sqlUtil_second.getSql();
			Object[] values_third = sqlUtil_second.getParamValues();
			List<Object[]> list_second = dataBaseDao.findAllByHql(sql_third, values_third);
			if(list_second.size() > 0){
				//随机取查询结果中的一个修理厂
				Random random = new Random();
				int index = random.nextInt(list_second.size());
				Object[] obj = list_second.get(index);
				PrpLRepairFactory prpLRepairFactory = (PrpLRepairFactory)obj[0];
				PrpLAgentFactory prpLAgentFactory = (PrpLAgentFactory)obj[1];
				Beans.copy().from(prpLRepairFactory).to(prpLRepairFactoryVo);
				prpLRepairFactoryVo.setAgentPhone(prpLAgentFactory.getAgentPhone());
			}
		}
		/*查询修理厂结束*/
		return prpLRepairFactoryVo;
	}
	

	/**
	 * 根据修理厂代码查询修理厂表信息
	 */
	@Override
	public PrpLRepairFactoryVo findFactoryByCode(String factoryCode) {
		QueryRule qr=QueryRule.getInstance();
		qr.addEqual("factoryCode",factoryCode);
		PrpLRepairFactoryVo prpLRepairFactoryVo=null;
		PrpLRepairFactory prpLRepairFactory=new PrpLRepairFactory();
		List<PrpLRepairFactory> repairFactorys=dataBaseDao.findAll(PrpLRepairFactory.class,qr);
		if(repairFactorys!=null&& repairFactorys.size()>0){
			prpLRepairFactory=repairFactorys.get(0);
			prpLRepairFactoryVo = new PrpLRepairFactoryVo();
			Beans.copy().from(prpLRepairFactory).to(prpLRepairFactoryVo);
		}
		return prpLRepairFactoryVo;
	}


	@Override
	public String findByFactoryName(String factoryName) {
		QueryRule query = QueryRule.getInstance();
		String factoryCode = "";
		query.addEqual("factoryName", factoryName);
		PrpLRepairFactoryVo prpLRepairFactoryVo = null;
		PrpLRepairFactory prpLRepairFactory = null;
		List<PrpLRepairFactory> repairFactorys = dataBaseDao.findAll(
				PrpLRepairFactory.class, query);
		if (repairFactorys != null && repairFactorys.size() > 0) {
			prpLRepairFactory = new PrpLRepairFactory();
			prpLRepairFactoryVo = new PrpLRepairFactoryVo();
			prpLRepairFactory = repairFactorys.get(0);
			Beans.copy().from(prpLRepairFactory).to(prpLRepairFactoryVo);
			factoryCode = prpLRepairFactoryVo.getFactoryCode();
		}
		return factoryCode;
	}

	@Override
	public Map<String, String> findByAgentCode(String userInfo,String agentCode) {
		Map<String, String> map = new HashMap<String, String>();
	/*	QueryRule rule = QueryRule.getInstance();
		rule.addEqual("agentCode", agentCode);
		List<PrplCMain> cMainList = dataBaseDao.findAll(PrplCMain.class, rule);
		if (cMainList != null && cMainList.size() > 0) {
			for (PrplCMain cMain : cMainList) {
				if (StringUtils.isNotBlank(cMain.getInsuredCode())) {
					map.put(cMain.getInsuredCode(), cMain.getInsuredName());
				}
			}
		}*/
		
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select insuredcode ,insuredname  from prpcmain where 1=1 ");	
		sqlUtil.append(" and insuredcode is not null  ");
		sqlUtil.append(" and insuredname is not null  ");
		sqlUtil.append(" and agentcode = ? ");
		sqlUtil.addParamValue(agentCode);
		sqlUtil.append(" and insuredcode like ? ");
		sqlUtil.addParamValue("%"+userInfo+"%");
		sqlUtil.append(" and riskCode like ? ");
		sqlUtil.addParamValue("1%");//车险
//		sqlUtil.append(" and validFlag = ? ");
//		sqlUtil.addParamValue("1");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> listcmain = baseDaoService.getAllBySql(sql, values);
		if (listcmain != null && listcmain.size() > 0) {
			for (Object[] obj : listcmain) {
				map.put((String) obj[0], (String) obj[1]);
			}
		}
		return map;
	}

	@Override
	public List<PrpLAgentFactoryVo> findAgentFactoryByFactoryId(
			Long factoryId) {
		List<PrpLAgentFactoryVo> agentFactoryVoList = new ArrayList<PrpLAgentFactoryVo>();
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("factoryId", factoryId);
		rule.addAscOrder("createTime");
		List<PrpLAgentFactory> agentFactoryPoList = dataBaseDao.findAll(
				PrpLAgentFactory.class, rule);
		if (agentFactoryPoList != null && agentFactoryPoList.size() > 0) {
			agentFactoryVoList = Beans.copyDepth().from(agentFactoryPoList)
					.toList(PrpLAgentFactoryVo.class);
		}
		return agentFactoryVoList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void saveOrUpdateAgentFactory(List<PrpLAgentFactoryVo> agentFactoryList,PrpLRepairFactoryVo repairFactoryVo) {
		if(repairFactoryVo != null){
			QueryRule rule = QueryRule.getInstance();
			rule.addEqual("factoryId", repairFactoryVo.getId());
			List<PrpLAgentFactory> agentFactoryPoList = dataBaseDao.findAll(PrpLAgentFactory.class, rule);
			if(agentFactoryPoList != null && agentFactoryPoList.size() > 0){ //删除
				for (Iterator it = agentFactoryPoList.iterator(); it.hasNext();) {
					PrpLAgentFactory agentFactoryPo = (PrpLAgentFactory)it.next();
					boolean flag = true;
					for(PrpLAgentFactoryVo agentFactoryVo : agentFactoryList){
						if(agentFactoryVo.getId() != null && agentFactoryPo.getId().equals(agentFactoryVo.getId())){
							flag = false;
							break;
						}
					}
					if(true == flag){
						dataBaseDao.deleteByPK(PrpLAgentFactory.class, agentFactoryPo.getId());
						it.remove();
					}
				}
			}
			
			for (PrpLAgentFactoryVo agentFactoryVo : agentFactoryList) {
				Long id = agentFactoryVo.getId();
				if (id == null || "".equals(id)) { // 保存
					PrpLAgentFactory agentFactoryPo = new PrpLAgentFactory();
					Beans.copy().from(agentFactoryVo).to(agentFactoryPo);
					Date date = new Date();
					agentFactoryPo.setCreateTime(date);
					agentFactoryPo.setUpdateTime(date);
					dataBaseDao.save(PrpLAgentFactory.class, agentFactoryPo);
				} else { // 更新
					for (PrpLAgentFactory agentFactory : agentFactoryPoList) {
						if (agentFactoryVo.getId().equals(agentFactory.getId())) {
							Beans.copy().from(agentFactoryVo).excludeNull().to(agentFactory);
							agentFactory.setUpdateTime(new Date());
							dataBaseDao.update(PrpLAgentFactory.class,agentFactory);
						}
					}
				}
		
				}
			}
		}

	@SuppressWarnings("rawtypes")
	@Override
	public void saveInsuredFactory(List<PrpLInsuredFactoryVo> insuredFactoryVos,Long agentId) throws Exception{
		PrpLAgentFactory agentFactory = new PrpLAgentFactory();
		if(!"".equals(agentId)){
			 agentFactory = dataBaseDao.findByPK(PrpLAgentFactory.class, agentId);
			 List<PrpLInsuredFactory> insuredFactoryPos =  agentFactory.getPrpLInsuredFactories();
			 if(insuredFactoryPos!=null && insuredFactoryPos.size() > 0){
				for(Iterator it = insuredFactoryPos.iterator();it.hasNext();){ //删除
					PrpLInsuredFactory insuredFactoryPo = (PrpLInsuredFactory)it.next();
					boolean flag = true;
					if(insuredFactoryVos != null && insuredFactoryVos.size() > 0){
						for(PrpLInsuredFactoryVo insuredFactoryVo : insuredFactoryVos){
							if( insuredFactoryVo.getId() != null &&insuredFactoryVo.getId().equals(insuredFactoryPo.getId()) ){
								flag = false;
								break;
							}
						}
					}
					if(true == flag){
						dataBaseDao.deleteByPK(PrpLInsuredFactory.class, insuredFactoryPo.getId());
						it.remove();
					}
				}	 
				 }
			 
			 Date date = new Date();
			 if(insuredFactoryVos != null && insuredFactoryVos.size() > 0){
				 for(PrpLInsuredFactoryVo insuredFactoryVo : insuredFactoryVos){
					 if(insuredFactoryVo.getId() == null || "".equals(insuredFactoryVo.getId())){  //新增
						 QueryRule rule = QueryRule.getInstance();
							rule.addEqual("insuredCode", insuredFactoryVo.getInsuredCode());
							List<PrpLInsuredFactory> insuredFactoryPoList = dataBaseDao.findAll(PrpLInsuredFactory.class, rule);
							if(insuredFactoryPoList != null && insuredFactoryPoList.size() > 0){ //校验被保险人是否被维护
								//Long factoryId = insuredFactoryPoList.get(0).getFactoryId();
								PrpLAgentFactory agentpo = dataBaseDao.findByPK(PrpLAgentFactory.class, insuredFactoryPoList.get(0).getPrpLAgentFactory().getId());
								 throw new IllegalArgumentException("该被保险人已被维护("+insuredFactoryVo.getInsuredName()+")!<br/>修理厂代码-名称 : "+agentpo.getFactoryCode()+"-"+agentpo.getFactoryName()+"<br/>"+"代理人代码-名称 : "+ agentpo.getAgentCode()+"-"+agentpo.getAgentName());
							}

						 PrpLInsuredFactory insuredFacPo = new PrpLInsuredFactory();
						 Beans.copy().from(insuredFactoryVo).to(insuredFacPo);
						 insuredFacPo.setAgentCode(agentFactory.getAgentCode());
						 insuredFacPo.setAgentName(agentFactory.getAgentName());
						 insuredFacPo.setFactoryId(agentFactory.getFactoryId());
						 insuredFacPo.setComCode(agentFactory.getComCode());
						 insuredFacPo.setPrpLAgentFactory(agentFactory);
						 
						 insuredFacPo.setCreateTime(date);
						 dataBaseDao.save(PrpLInsuredFactory.class, insuredFacPo);
					 }else{ // 更新
						 PrpLInsuredFactory insuredFacPo = dataBaseDao.findByPK(PrpLInsuredFactory.class, insuredFactoryVo.getId());
						 if(!insuredFacPo.getInsuredCode().equals(insuredFactoryVo.getInsuredCode())){
							 QueryRule rule = QueryRule.getInstance();
								rule.addEqual("insuredCode", insuredFactoryVo.getInsuredCode());
								List<PrpLInsuredFactory> insuredFactoryPoList = dataBaseDao.findAll(PrpLInsuredFactory.class, rule);
								if(insuredFactoryPoList != null && insuredFactoryPoList.size() > 0){ //校验被保险人是否被维护
									//Long factoryId = insuredFactoryPoList.get(0).getFactoryId();
									PrpLAgentFactory agentpo = dataBaseDao.findByPK(PrpLAgentFactory.class, insuredFactoryPoList.get(0).getPrpLAgentFactory().getId());
									 throw new IllegalArgumentException("该被保险人已被维护("+insuredFactoryVo.getInsuredName()+")!<br/>修理厂代码-名称 : "+agentpo.getFactoryCode()+"-"+agentpo.getFactoryName()+"<br/>"+"代理人代码-名称 : "+ agentpo.getAgentCode()+"-"+agentpo.getAgentName());
								}
						 }
						 Beans.copy().from(insuredFactoryVo).excludeNull().to(insuredFacPo);
						 dataBaseDao.update(PrpLInsuredFactory.class, insuredFacPo);
					 }
				 }
			 }
			 
			 }
		
		
		}
		


	@Override
	public List<PrpLInsuredFactoryVo> findInsuredFactoryByAgentId(
			String agentId) {
		List<PrpLInsuredFactoryVo> insuredFactoryVoList = new ArrayList<PrpLInsuredFactoryVo>();
		if (agentId != null) {
			QueryRule rule = QueryRule.getInstance();
			rule.addEqual("agentId", agentId);
			PrpLAgentFactory agentFactory = dataBaseDao.findByPK(PrpLAgentFactory.class, Long.parseLong(agentId));
			//List<PrpLAgentFactory> agentFactoryPoList = dataBaseDao.findAll(PrpLAgentFactory.class, rule);
			if (agentFactory != null && agentFactory.getPrpLInsuredFactories().size() > 0) {
				insuredFactoryVoList = Beans.copyDepth().from(agentFactory.getPrpLInsuredFactories()).toList(PrpLInsuredFactoryVo.class);
			}
		}
		return insuredFactoryVoList;
	}

	@Override
	public Map<String, String> findInsuredFactoryByAgentCode(String agentCode) {
		Map<String, String> resultMap = new HashMap<String, String>();
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("agentCode", agentCode);
		rule.addIsNotEmpty("agentCode");
		rule.addLike("riskCode", "1%");
		List<PrplCMain> cMainList = dataBaseDao.findAll(PrplCMain.class, rule);
		if (cMainList != null && cMainList.size() > 0) {
			for (PrplCMain cMain : cMainList) {
				if (StringUtils.isNotBlank(cMain.getInsuredCode())) {
					resultMap.put(cMain.getInsuredCode(),cMain.getInsuredName());
				}
			}
		}
        return resultMap;
	}



	@Override
	public Map<String, String> findAgentInfoFromPrpLCmain() {
/*	//	Map<String, String> resultMap = new HashMap<String, String>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select distinct(agentCode)  from prpcmain where 1=1 ");	
		sqlUtil.append(" and agentCode is not null  ");
//		sqlUtil.append(" and validFlag = ? ");
//		sqlUtil.addParamValue("1");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		List<Object[]> listcmain = baseDaoService.getAllBySql(sql, values);
		
		if(listcmain != null && listcmain.size() > 0){
			for(Object cmain : listcmain){
				System.out.println(codeTranService.transCode("AgentCode",(String)cmain));
				resultMap.put((String)cmain,codeTranService.transCode("AgentCode",(String)cmain));
			}
		}*/
		Map<String, String> resultMap = new HashMap<String, String>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select agentcode ,agentname  from ywuser.PrpDagent where 1=1 ");	
		List<Object[]> transList = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		
		if(transList != null && transList.size() > 0){
			for(Object[] cmain : transList){
				resultMap.put((String)cmain[0],(String)cmain[1]);
			}
		}
		return resultMap;
	}
	
	@SuppressWarnings("unused")
	private Map<String, String> transCode(List<Object[]> listObj){
		Map<String, String> resultMap = new HashMap<String, String>();
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" select agentcode ,agentname  from ywuser.PrpDagent where 1=1 ");	
		List<Object[]> transList = baseDaoService.getAllBySql(sqlUtil.getSql(), sqlUtil.getParamValues());
		
		if (listObj != null && listObj.size() > 0) {
			for (Object obj : listObj) {
				boolean flag = true;
				if (transList != null && transList.size() > 0) {
					for ( Object[] transObj : transList) {
						if ( ( (String) obj).equals( (String) transObj[0] ) ) {
							resultMap.put((String) obj, (String) transObj[1]);
							flag = false;
							continue;
						}
					}
				}
				if (flag) {
					resultMap.put((String) obj, (String) obj);
				}
			}
		}
		return resultMap;
	}



    @Override
    public Page<PrplSysAgentfactoryVo> findLAgentFactory(PrpLAgentFactoryVo lAgentFactoryVo,int start,int length) {

        // 定义参数list，ps：执行查询时需要转换成object数组
        List<Object> paramValues = new ArrayList<Object>();
        // hql查询语句
      /*  SqlJoinUtils sqlUtil=new SqlJoinUtils();
        sqlUtil.append(" select agentcode ,agentname  from ywuser.PrpDagent where 1=1 ");
        if(StringUtils.isNotBlank(lAgentFactoryVo.getAgentName())){
            sqlUtil.append(" and AGENTNAME = ? ");
            sqlUtil.addParamValue(lAgentFactoryVo.getAgentName());
        }
        if(StringUtils.isNotBlank(lAgentFactoryVo.getAgentCode())){
            sqlUtil.append(" and AGENTCODE = ? ");
            sqlUtil.addParamValue(lAgentFactoryVo.getAgentCode());
        }*/
        
         StringBuffer queryString = new StringBuffer("from PrplSysAgentfactory af where 1=1 ");

         if(StringUtils.isNotBlank(lAgentFactoryVo.getAgentName())){
			 queryString.append(" AND INSTR(af.agentName, ?) > 0");
             paramValues.add(lAgentFactoryVo.getAgentName());
         }
        
         if(StringUtils.isNotBlank(lAgentFactoryVo.getAgentCode())){
			 queryString.append(" AND INSTR(af.agentCode, ?) > 0 ");
             paramValues.add(lAgentFactoryVo.getAgentCode());
         }
         // 执行查询
         Page page = dataBaseDao.findPageByHql(queryString.toString(), (start/ length + 1), length, paramValues.toArray());
         Page pageReturn = assemblyPrplSysAgentfactoryInfo(page);
         // 返回结果ResultPage
         //return page;
        return pageReturn;
    
    }

	@Override
	public ResultPage<PrpLRepairBrandVo> findRepairBrand(PrpLRepairBrandVo prpLRepairBrandVo, int start, int length) throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" select zc.ppbm, zc.ppmc from ZC_CLPPXXB zc where 1=1");
		if (StringUtils.isNotBlank(prpLRepairBrandVo.getBrandCode())) {
			sqlUtil.append(" AND  instr(zc.ppbm , '" + prpLRepairBrandVo.getBrandCode() + "') > 0");
		}

		if (StringUtils.isNotBlank(prpLRepairBrandVo.getBrandName())) {
			sqlUtil.append(" AND  instr(zc.ppmc , '" + prpLRepairBrandVo.getBrandName() + "') > 0");
		}
		Page<Object[]> page = baseDaoService.pagedSQLQuery(sqlUtil.getSql(), start, length, null);
		List<PrpLRepairBrandVo> queryVoList = new ArrayList<PrpLRepairBrandVo>();
		for (Object[] object : page.getResult()) {
			PrpLRepairBrandVo vo = new PrpLRepairBrandVo();
			vo.setBrandCode((String) object[0]);
			vo.setBrandName((String) object[1]);
			queryVoList.add(vo);
		}
		ResultPage<PrpLRepairBrandVo> resultPage =
				new ResultPage<PrpLRepairBrandVo>(start, length, page.getTotalCount(), queryVoList);
		return resultPage;
	}

    /*
     *  循环查询结果集合
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Page assemblyPrplSysAgentfactoryInfo(Page page) {
        for (int i = 0; i < page.getResult().size(); i++) {
            PrplSysAgentfactoryVo sysAgentfactoryVo = new PrplSysAgentfactoryVo();
            PrplSysAgentfactory pm = (PrplSysAgentfactory) page.getResult().get(i);
            Beans.copy().from(pm).to(sysAgentfactoryVo);
            page.getResult().set(i, sysAgentfactoryVo);
        }
        return page;
    }



    @Override
    public List<PrplSysAgentfactoryVo> findLAgentFactoryByOther(String agentCode) {
        List<PrplSysAgentfactoryVo> voList = new ArrayList<PrplSysAgentfactoryVo>();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("agentCode",agentCode);
        List<PrplSysAgentfactory> list = dataBaseDao.findAll(PrplSysAgentfactory.class,queryRule);
        if(list != null && list.size() > 0){
            for(PrplSysAgentfactory agentfactory : list){
                PrplSysAgentfactoryVo vo = new PrplSysAgentfactoryVo();
                vo = Beans.copyDepth().from(agentfactory).to(PrplSysAgentfactoryVo.class);
                voList.add(vo);
            }
        }
        return voList;
    }

    /**
     * 修理厂信息修改同步精友 （朱彬）
     */
	@Override
	public JyResVo factorySyncJy(PrpLRepairFactoryVo repairFactory,List<PrpLRepairBrandVo> repairBrandVos,
			List<PrpLAgentFactoryVo> agentFactoryVos,String status) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JyFactoryBrandAndInfoPacketVo reqVo = new JyFactoryBrandAndInfoPacketVo();
		JyResVo resVo = new JyResVo();
		String url = "";
		try {
			JyReqHeadVo head = new JyReqHeadVo();
			String userCode = SpringProperties.getProperty("JY_USER");
			head.setUserCode(userCode);
			String passWord = SpringProperties.getProperty("JY_PAW");
			head.setPassWord(passWord);
			head.setRequestSourceCode("DHIC");
			head.setRequestSourceName("鼎和财产保险股份有限公司");
			head.setRequestType("101");
			head.setOperatingTime(sdf.format(new Date()));
			
			JyFactoryBrandAndInfoBodyVo body = new JyFactoryBrandAndInfoBodyVo();
			FactoryInfo factoryInfo = new FactoryInfo();
			factoryInfo.setFactoryId(agentFactoryVos.isEmpty()?"":agentFactoryVos.get(0).getFactoryId().toString());
			String comCode = repairFactory.getComCode();
			String comName = CodeTranUtil.transCode("ComCode", comCode);
			factoryInfo.setComCode(comCode);
			factoryInfo.setComName(comName);
			factoryInfo.setCityCode("");
			factoryInfo.setCityName("");
			if(repairFactory.getId()!=null){// 修理厂code传修理厂ID
				factoryInfo.setFactoryCode(repairFactory.getId().toString());
				factoryInfo.setFactoryId(repairFactory.getId().toString());
			}
			factoryInfo.setFactoryName(repairFactory.getFactoryName());
			factoryInfo.setRankCode("");
			String factoryType = repairFactory.getFactoryType();
			if(factoryType.equals("002")){
				factoryInfo.setFactoryQualification("1");
			}else if(factoryType.equals("003")){
				factoryInfo.setFactoryQualification("2");
			}else{
				factoryInfo.setFactoryQualification("");
			}
			//1：4S店(特约="001")  0：非4S店
			factoryInfo.setFactoryType(factoryType.equals("001")?"1":"0");
			factoryInfo.setCooperateType(repairFactory.getCooperateType());
			factoryInfo.setLinkMan(repairFactory.getLinker());
			factoryInfo.setMobile(repairFactory.getMobile());
			factoryInfo.setTelNo(repairFactory.getTelNo());
			factoryInfo.setAddress(repairFactory.getAddress());
			factoryInfo.setValidStatus(repairFactory.getValidStatus());
			factoryInfo.setValidBeginDate("");
			factoryInfo.setValidEndDate("");
			factoryInfo.setCooperateFactory(repairFactory.getCooperateFactory());
			factoryInfo.setBlacklistFlag(repairFactory.getBlackListFlag());
			factoryInfo.setRemark(repairFactory.getRemark());
			factoryInfo.setStatus(status);
			factoryInfo.setFitsDiscountRate(repairFactory.getCompRate().toString());
			factoryInfo.setRepairDiscountRate(repairFactory.getHourRate().toString());
			factoryInfo.setAssemblyFacManHour("");
			factoryInfo.setPaintFacManHour("");
			factoryInfo.setRepairFacManHour("");

			// 国标省级行政编码
			factoryInfo.setProvinceCode(HttpUtils.getGBCode(repairFactory.getAreaCode(), CodeConstants.AreaCodeModel.GB_PROVINCECODE));
			// 国标市级行政编码
			factoryInfo.setCity(HttpUtils.getGBCode(repairFactory.getAreaCode(), CodeConstants.AreaCodeModel.GB_CITYCODE));
			// 价格方案模式
			factoryInfo.setPriceSchemaMode(repairFactory.getPriceSchemeMode());
			// 目前没有外修修理厂，暂时默认传0-否  2019年9月24日19:20:42
			factoryInfo.setOuterFactory("0");

			List<Item> fbs = new ArrayList<Item>();
			Item item = null;
			String specialFlag = factoryType.equals("001")?"1":"0";
			if(repairBrandVos!=null && repairBrandVos.size()>0){
				for(PrpLRepairBrandVo prb:repairBrandVos){
					item = new Item();
					item.setBrandCode(prb.getBrandCode());
					item.setBrandName(prb.getBrandName());
					item.setBrandPartDiscountRate(prb.getCompRate().toString());
					item.setBrandRepairDiscountRate(prb.getHourRate().toString());
					item.setSpecialFlag(specialFlag);
					fbs.add(item);
				}
			}
			
			body.setFactoryInfo(factoryInfo);
			body.setItem(fbs);
			
			reqVo.setHead(head);
			reqVo.setBody(body);

			String xmlToSend = "";
			String xmlReturn = "";
			xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			url = SpringProperties.getProperty("JY2_REPAIR");
			logger.info("\n修理厂同步接口提交send---------------------------\n" + xmlToSend);
			xmlReturn = JyHttpUtil.sendData(xmlToSend, url, 15);
			logger.info("\n修理厂同步接口提交return-------------------------\n" + xmlReturn);
			resVo = ClaimBaseCoder.xmlToObj(xmlReturn, JyResVo.class);
			
		} catch (Exception e) {
			logger.error("推定全损数据清空接口返回报文send异常", e);
		}
		return resVo;
	}




	@Override
	public Long findRepairFactoryBy(String telephoneNumber) {
		QueryRule query=QueryRule.getInstance();
		query.addEqual("phoneNumber",telephoneNumber);
		List<PrpLRepairPhone> listVos=dataBaseDao.findAll(PrpLRepairPhone.class,query);
		if(listVos!=null && listVos.size()>0){
			for (PrpLRepairPhone prpLRepairPhone : listVos) {
				if (null != prpLRepairPhone.getPrpLRepairFactory().getValidStatus() &&
						"1".equals(prpLRepairPhone.getPrpLRepairFactory().getValidStatus())) {
					return prpLRepairPhone.getPrpLRepairFactory().getId();
				}
				continue;
			}
			return null;
		}else{
			return null;
		}
		
	}



	@Override
	public Map<String, String> findAllPhone(
			List<PrpLRepairPhoneVo> repairPhoneVos) {
		int size = repairPhoneVos.size();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT PHONE.PHONENUMBER,PHONE.REPAIRID,PHONE.remark FROM PRPLREPAIRPHONE PHONE WHERE 1=1 ");
		
		if(size > 0){
			sqlUtil.append("AND  PHONE.PHONENUMBER IN (");
			for(int i = 0;i < size; i++){
				if(i<1){
					sqlUtil.append("?");
					sqlUtil.addParamValue(repairPhoneVos.get(i).getPhoneNumber());
				}else{
					sqlUtil.append(",?");
					sqlUtil.addParamValue(repairPhoneVos.get(i).getPhoneNumber());
				}
			}
			sqlUtil.append(" )");
		}
		
		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql, values);
		Map<String, String> resultMap = new HashMap<String, String>();
		for(int i = 0 ; i < objects.size() ; i++){
			Object[] obj = objects.get(i);
			if(obj[1] != null){
				resultMap.put(obj[0].toString(), obj[1].toString());
			}else{
				resultMap.put(obj[0].toString(), "");
			}
			
		}
		return resultMap;
	}
 
	
	
}
