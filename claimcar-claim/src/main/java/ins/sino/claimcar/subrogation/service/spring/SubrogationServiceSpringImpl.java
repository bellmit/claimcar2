package ins.sino.claimcar.subrogation.service.spring;


import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.CodeConstants.PayFlagType;
import ins.sino.claimcar.subrogation.po.PrpLPlatLock;
import ins.sino.claimcar.subrogation.po.PrpLSubrogationCar;
import ins.sino.claimcar.subrogation.po.PrpLSubrogationMain;
import ins.sino.claimcar.subrogation.po.PrpLSubrogationPerson;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("subrogationService")
public class SubrogationServiceSpringImpl  implements SubrogationService{
	
	@Autowired
	DatabaseDao databaseDao;
	public void deleteSubrogationInfo(PrpLSubrogationMainVo subrogationMainVo){
		
		PrpLSubrogationMain subrogationMain = databaseDao.findByPK(PrpLSubrogationMain.class, subrogationMainVo.getId());
		Beans.copy().from(subrogationMainVo).excludeNull().to(subrogationMain);
		databaseDao.save(PrpLSubrogationMain.class,subrogationMain);
		
		List<PrpLSubrogationCarVo> carList = subrogationMainVo.getPrpLSubrogationCars();
		List<PrpLSubrogationPersonVo> personList = subrogationMainVo.getPrpLSubrogationPersons();
		if(carList != null && carList.size() > 0){
			for(PrpLSubrogationCarVo carVo : carList){
				if(carVo.getId()!=null){
					databaseDao.deleteByPK(PrpLSubrogationCar.class, carVo.getId());
				}
			}
		}
		
		
		if(personList != null && personList.size() > 0){
			for(PrpLSubrogationPersonVo personVo : personList){
				if(personVo.getId()!=null){
					databaseDao.deleteByPK(PrpLSubrogationPerson.class, personVo.getId());
				}
			}
		}
	}
	
	public Long saveSubrogationInfo(PrpLSubrogationMainVo subrogationMainVo){
		Long subrId = null;
		PrpLSubrogationMain subrogationMain = null;
		if(subrogationMainVo.getId() !=null){
			subrogationMain = databaseDao.findByPK(PrpLSubrogationMain.class, subrogationMainVo.getId());
			Beans.copy().from(subrogationMainVo).excludeNull().to(subrogationMain);
			
			List<PrpLSubrogationCar> carList = subrogationMain.getPrpLSubrogationCars();
			List<PrpLSubrogationPerson> personList = subrogationMain.getPrpLSubrogationPersons();

			this.mergeList(subrogationMain,subrogationMainVo.getPrpLSubrogationCars(),
					carList,"id",PrpLSubrogationCar.class,"setPrpLSubrogationMain");
			for(PrpLSubrogationCar car:carList){
				car.setPrpLSubrogationMain(subrogationMain);;
			}
			this.mergeList(subrogationMain,subrogationMainVo.getPrpLSubrogationPersons(),
					personList,"id",PrpLSubrogationPerson.class,"setPrpLSubrogationMain");
			for(PrpLSubrogationPerson person:personList){
				person.setPrpLSubrogationMain(subrogationMain);;
			}
			
			databaseDao.save(PrpLSubrogationMain.class,subrogationMain);
			subrId = subrogationMain.getId();
		}else{
			subrogationMain = new PrpLSubrogationMain();
			subrogationMain = Beans.copyDepth().from(subrogationMainVo).to(PrpLSubrogationMain.class);
			//责任对方内容（责任方为机动车）
			List<PrpLSubrogationCar> carPoList = subrogationMain.getPrpLSubrogationCars();
			if(carPoList!=null&&carPoList.size()>0){
				for(PrpLSubrogationCar carPo : carPoList){
					carPo.setPrpLSubrogationMain(subrogationMain);
				}
			}
			//责任对方内容（责任方为非机动车）
			List<PrpLSubrogationPerson> personPoList = subrogationMain.getPrpLSubrogationPersons();
			if(personPoList!=null&&personPoList.size()>0){
				for(PrpLSubrogationPerson personPo : personPoList){
					personPo.setPrpLSubrogationMain(subrogationMain);
				}
			}
			databaseDao.save(PrpLSubrogationMain.class,subrogationMain);
			subrId = subrogationMain.getId();
		}
		return subrId;
	}
	
	public void mergeList(PrpLSubrogationMain lossCarMain,List voList, List poList, String idName,Class paramClass, String method){
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

	@Override
	public PrpLSubrogationMainVo find(String registNo) {
		PrpLSubrogationMainVo subrogationMainVo = null;
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("registNo", registNo);
		List<PrpLSubrogationMain> subrogationMainList =  databaseDao.findAll(PrpLSubrogationMain.class, qr);
		if(subrogationMainList!=null && !subrogationMainList.isEmpty()){
			subrogationMainVo = Beans.copyDepth().from(subrogationMainList.get(0)).to(PrpLSubrogationMainVo.class);
		}
		
		return subrogationMainVo;
	}

	/**
     * 查询被锁定信息
     * <pre></pre>
     * @param recoveryOrPayFlag 1追偿 (锁定对方车辆) 2 清付(被对方锁定)
     * @param registNo
     * @param oppoentLicensePlateNo 责任对方车牌号
     * @param policyType 保单类型  1商业 2交强
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年4月26日 上午8:45:34): <br>
     */
	@Override
	public List<PrpLPlatLockVo> findByPrpLPlatLockVo(String recoveryOrPayFlag,String registNo,String oppoentLicensePlateNo,String policyType) {
		List<PrpLPlatLockVo> prpLPlatLockVoList = new ArrayList<PrpLPlatLockVo>();
		PrpLPlatLockVo prpLPlatLockVo = new PrpLPlatLockVo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("recoveryOrPayFlag",recoveryOrPayFlag);
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("oppoentLicensePlateNo",oppoentLicensePlateNo);
		List<PrpLPlatLock> prpLPlatLockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		if(prpLPlatLockList != null && !prpLPlatLockList.isEmpty()){
			for(PrpLPlatLock prpLPlatLock:prpLPlatLockList){
				if(prpLPlatLock.getPolicyNo().startsWith("11") && "2".equals(policyType)){
					prpLPlatLockVo = Beans.copyDepth().from(prpLPlatLock).to(PrpLPlatLockVo.class);
					prpLPlatLockVoList.add(prpLPlatLockVo);
				}else if(prpLPlatLock.getPolicyNo().startsWith("12") && "1".equals(policyType)){
					prpLPlatLockVo = Beans.copyDepth().from(prpLPlatLock).to(PrpLPlatLockVo.class);
					prpLPlatLockVoList.add(prpLPlatLockVo);
				}
			}
		}
		return prpLPlatLockVoList;
	}

	/**
     * 根据报案号查询信息
     * <pre></pre>
     * @param registNo
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年4月26日 下午3:13:03): <br>
     */
	@Override
	public List<PrpLPlatLockVo> findPrpLPlatLockVoByRegistNo(String registNo,String recoveryOrPayFlag) {
		List<PrpLPlatLockVo> prpLPlatLockVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqualIfExist("recoveryOrPayFlag",recoveryOrPayFlag);
		List<PrpLPlatLock> prpLPlatLockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		if(prpLPlatLockList != null && !prpLPlatLockList.isEmpty()){
			prpLPlatLockVoList = Beans.copyDepth().from(prpLPlatLockList).toList(PrpLPlatLockVo.class);
		}
		return prpLPlatLockVoList;
	}
	
	@Override
	public List<PrpLPlatLockVo> findPlatLockVoByPayFlag(String registNo){
		List<PrpLPlatLockVo> platLockVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLPlatLock> platLockList = databaseDao.findAll(PrpLPlatLock.class,queryRule);
		if(platLockList != null && platLockList.size() > 0 ){
			platLockVoList = new ArrayList<PrpLPlatLockVo>();
			for(PrpLPlatLock platLock : platLockList){
				String payFlag = platLock.getRecoveryOrPayFlag();
				if(!PayFlagType.COMPENSATE_PAY.equals(payFlag)
						&& !PayFlagType.NODUTY_INSTEAD_PAY.endsWith(payFlag)){
					PrpLPlatLockVo platLockVo = new PrpLPlatLockVo(); 
					platLockVo = Beans.copyDepth().from(platLock).to(PrpLPlatLockVo.class);
					platLockVoList.add(platLockVo);
				}
			}
		}
		return platLockVoList;
	}

	/**
     * 根据主键查询数据
     * <pre></pre>
     * @return
     * @modified:
     * ☆ZhouYanBin(2016年5月3日 下午2:39:58): <br>
     */
	@Override
	public PrpLPlatLockVo findPrpLPlatLockVoById(Long id) {
		PrpLPlatLockVo prpLPlatLockVo = null;
		PrpLPlatLock prpLPlatLock = databaseDao.findByPK(PrpLPlatLock.class,id);
		if(prpLPlatLock != null){
			prpLPlatLockVo = Beans.copyDepth().from(prpLPlatLock).to(PrpLPlatLockVo.class);
		}
		return prpLPlatLockVo;
	}
	/**
	 * 保存或更新非机动车代位信息
	 * <pre></pre>
	 * @param subrogationPersonVoList
	 * @modified:
	 * ☆WLL(2017年4月28日 下午10:06:03): <br>
	 */
	@Override
	public void saveSubrogationPers(List<PrpLSubrogationPersonVo> subrogationPersonVoList){
		for(PrpLSubrogationPersonVo persVo : subrogationPersonVoList){
			if(persVo.getId()==null){
				PrpLSubrogationPerson persPo = new PrpLSubrogationPerson();
				Beans.copy().from(persVo).to(persPo);
				databaseDao.save(PrpLSubrogationPerson.class,persPo);
			}else{
				PrpLSubrogationPerson persPo = databaseDao.findByPK(PrpLSubrogationPerson.class,persVo.getId());
				Beans.copy().from(persVo).excludeNull().to(persPo);
				databaseDao.update(PrpLSubrogationPerson.class,persPo);
			}
		}
	}
}
