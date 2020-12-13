/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.check.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.HttpClientHander;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.po.*;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.*;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.util.Assert;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 查勘接口实现类
 * 
 * <pre></pre>
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path("checkTaskService")
public class CheckTaskServiceImpl implements CheckTaskService {

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	CheckHandleService checkService;
	@Autowired
	private LossCarService lossCarService;

	/** 根据ID查勘查勘车损表(PrpLCheckCar) */
	@Override
	public PrpLCheckCarVo findByCheckId(Long checkId) {
		PrpLCheckCarVo checkCarVo = null;
		Assert.notNull(checkId,"id must have value.");
		PrpLCheckCar checkCarpo = databaseDao.findByPK(PrpLCheckCar.class,checkId);
		if(checkCarpo!=null){
			checkCarVo = Beans.copyDepth().from(checkCarpo).to(PrpLCheckCarVo.class);
			checkCarVo.setCheckTaskId(checkCarpo.getPrpLCheckTask().getId());
		}
		return checkCarVo;
	}

	/** 根据ID查勘propVo */
	@Override
	public PrpLCheckPropVo findByCheckPropVoById(Long id) {
		PrpLCheckPropVo checkPropVo = null;
		Assert.notNull(id,"id must have value.");
		PrpLCheckProp checkPropPo = databaseDao.findByPK(PrpLCheckProp.class,id);
		if(checkPropPo!=null){
			checkPropVo = Beans.copyDepth().from(checkPropPo).to(PrpLCheckPropVo.class);
		}
		return checkPropVo;
	}
	
	/** 根据ID查勘propVo */
	@Override
	public PrpLCheckPersonVo findCheckPersonVpById(Long id) {
		PrpLCheckPersonVo checkPersonVo = null;
		Assert.notNull(id,"id must have value.");
		PrpLCheckPerson checkPersonPo = databaseDao.findByPK(PrpLCheckPerson.class,id);
		if(checkPersonPo!=null){
			checkPersonVo = Beans.copyDepth().from(checkPersonPo).to(PrpLCheckPersonVo.class);
		}
		return checkPersonVo;
	}
	
	/** 根据ID查勘CheckTaskVo */
	@Override
	public PrpLCheckTaskVo findCheckTaskVoById(Long id) {
		PrpLCheckTaskVo prpLCheckTaskVo = null;
		PrpLCheckTask prpLCheckTask = databaseDao.findByPK(PrpLCheckTask.class,id);
		if(prpLCheckTask != null){
			prpLCheckTaskVo = Beans.copyDepth().from(prpLCheckTask).to(PrpLCheckTaskVo.class);
		}
		return prpLCheckTaskVo;
	}
	
	@Override
	public List<PrpLCheckCarVo> findCheckCarVo(String registNo) {
		List<PrpLCheckCarVo> checkCarVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCheckCar> checkCarPos = databaseDao.findAll(PrpLCheckCar.class,queryRule);
		if(checkCarPos != null && checkCarPos.size() > 0){
			checkCarVos = Beans.copyDepth().from(checkCarPos).toList(PrpLCheckCarVo.class);
		}
		return checkCarVos;
	}
	
	@Override
	public List<PrpLCheckPersonVo> findCheckPersonVo(String registNo){
		List<PrpLCheckPersonVo> checkPersonVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCheckPerson> checkPersonPos = databaseDao.findAll(PrpLCheckPerson.class,queryRule);
		if(checkPersonPos != null && checkPersonPos.size() > 0){
			checkPersonVos = Beans.copyDepth().from(checkPersonPos).toList(PrpLCheckPersonVo.class);
		}
		return checkPersonVos;
	}

	@Override
	public List<PrpLCheckPropVo> findCheckPropVo(String registNo) {
		List<PrpLCheckPropVo> checkPropVos = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCheckProp> checkProps = databaseDao.findAll(PrpLCheckProp.class,queryRule);
		if(checkProps != null && checkProps.size() > 0){
			checkPropVos = Beans.copyDepth().from(checkProps).toList(PrpLCheckPropVo.class);
		}
		return checkPropVos;
	}
	
	
	/** 根据ID查勘CheckVo */
	@Override
	public PrpLCheckVo findCheckVoById(Long id) {
		PrpLCheckVo prpLCheckVo = null;
		PrpLCheck prpLCheck = databaseDao.findByPK(PrpLCheck.class,id);
		if(prpLCheck != null){
			prpLCheckVo = Beans.copyDepth().from(prpLCheck).to(PrpLCheckVo.class);
		}
		return prpLCheckVo;
	}

	/** 根据报案号查询查勘PrpLCheck */
	@Override
	public PrpLCheckVo findCheckVoByRegistNo(String registNo) {
		PrpLCheckVo prpLCheckVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		PrpLCheck prpLCheck = databaseDao.findUnique(PrpLCheck.class,queryRule);
		if(prpLCheck != null){
			prpLCheckVo=Beans.copyDepth().from(prpLCheck).to(PrpLCheckVo.class);
		}
		return prpLCheckVo;
	}

	/** 查询车辆责任表(PrpLCheckDuty) */
	@Override
	public PrpLCheckDutyVo findCheckDuty(String registNo,Integer serialNo) {
		PrpLCheckDutyVo checkDutyVo = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("serialNo", serialNo);
		PrpLCheckDuty checkDuty = databaseDao.findUnique(PrpLCheckDuty.class,queryRule);
		if(checkDuty!=null){
			checkDutyVo = new PrpLCheckDutyVo();
			Beans.copy().from(checkDuty).to(checkDutyVo);
		}
		return checkDutyVo;
	}

	/** 根据报案号查询未被注销的车辆的checkDutyList */
	@Override
	public List<PrpLCheckDutyVo> findCheckDutyByRegistNo(String registNo) {
		List<PrpLCheckDutyVo> checkDutyVoList = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addNotEqual("validFlag","0");// 已经被定损注销的车辆会被回写成0
		List<PrpLCheckDuty> checkDutyList = databaseDao.findAll(PrpLCheckDuty.class,queryRule);
		if(checkDutyList!=null&&checkDutyList.size()>0){
			checkDutyVoList = Beans.copyDepth().from(checkDutyList).toList(PrpLCheckDutyVo.class);
		}
		return checkDutyVoList;
	}

	/** 保存查勘车辆责任表 */
	@Override
	public void saveCheckDuty(PrpLCheckDutyVo checkDutyVo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", checkDutyVo.getRegistNo());
		queryRule.addEqual("serialNo", checkDutyVo.getSerialNo());
		PrpLCheckDuty checkDutyPo = databaseDao.findUnique(PrpLCheckDuty.class,queryRule);
		//String registNo = checkDutyVo.getRegistNo();
		//Integer serialNo = checkDutyVo.getSerialNo();
		//PrpLCheckDuty checkDutyPo = this.findCheckDutyPo(registNo,serialNo);
		String user=checkDutyVo.getUpdateUser();
		Date date = new Date();
		if(checkDutyPo == null){
			checkDutyPo = new PrpLCheckDuty();
			checkDutyPo.setCreateUser(user);
			checkDutyPo.setCreateTime(date);
		}
		Beans.copy().from(checkDutyVo).excludeNull().to(checkDutyPo);
		checkDutyPo.setUpdateTime(date);
		checkDutyPo.setUpdateUser(user);
		// 三者车交强是否有责根据商业责任比例大于0则判定
		BigDecimal dutyRate=checkDutyVo.getIndemnityDutyRate();
		if(checkDutyVo.getSerialNo()!=1&&dutyRate!=null){
			checkDutyPo.setCiDutyFlag(dutyRate.compareTo(BigDecimal.ZERO)==1?"1":"0");
		}

		databaseDao.save(PrpLCheckDuty.class,checkDutyPo);
	}
	
	public  PrpLCheckDuty findCheckDutyPo(String registNo,Integer serialNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("serialNo", serialNo);
		return databaseDao.findUnique(PrpLCheckDuty.class,queryRule);
	}
	
	/** 根据报案号、车辆序号查询车损表 */
	@Override
	public PrpLCheckCarVo findCheckCarBySerialNo(String registNo,Integer serialNo) {
		PrpLCheckCarVo checkCarVo = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("serialNo", serialNo);
		PrpLCheckCar checkCarPo = databaseDao.findUnique(PrpLCheckCar.class,queryRule);
		if(checkCarPo!=null){
			checkCarVo = Beans.copyDepth().from(checkCarPo).to(PrpLCheckCarVo.class);
		}
		return checkCarVo;
	}

	@Override
	public void deleteCheckDutyByCheckId(String registNo,Integer serialNo) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("serialNo", serialNo);
		PrpLCheckDuty checkDuty = databaseDao.findUnique(PrpLCheckDuty.class,queryRule);
		if(checkDuty!=null){
			databaseDao.deleteByPK(PrpLCheckDuty.class,checkDuty.getId());
		}
	}

	/**
	 * 根据报案号查询查勘车辆信息
	 * 
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月9日 下午6:27:03): <br>
	 */
	@Override
	public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByRegistNo(String registNo) {
		List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheckCarInfo> prpLCheckCarInfoList = databaseDao.findAll(PrpLCheckCarInfo.class,queryRule);
		if(prpLCheckCarInfoList != null && !prpLCheckCarInfoList.isEmpty()){
			prpLCheckCarInfoVoList = Beans.copyDepth().from(prpLCheckCarInfoList).toList(PrpLCheckCarInfoVo.class);
		}
		return prpLCheckCarInfoVoList;
	}

	/**
	 * 根据主键查询唯一一条记录
	 * 
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月11日 上午11:10:11): <br>
	 */
	@Override
	public PrpLCheckCarVo findPrpLCheckCarVoById(Long id){
		PrpLCheckCarVo prpLCheckCarVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("carid",id);
		PrpLCheckCar prpLCheckCar = databaseDao.findUnique(PrpLCheckCar.class,queryRule);
		if(prpLCheckCar != null){
			prpLCheckCarVo = Beans.copyDepth().from(prpLCheckCar).to(PrpLCheckCarVo.class);
		}
		return prpLCheckCarVo;
	}

	/**
	 * 根据主键查询唯一一条记录
	 * 
	 * <pre></pre>
	 * @param id
	 * @return
	 * @modified: ☆ZhouYanBin(2016年4月12日 上午9:29:07): <br>
	 */
	@Override
	public PrpLCheckCarInfoVo findPrpLCheckCarInfoVoById(Long id) {
		PrpLCheckCarInfoVo prpLCheckCarInfoVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("carid",id);
		PrpLCheckCarInfo prpLCheckCarInfo = databaseDao.findUnique(PrpLCheckCarInfo.class,queryRule);
		if(prpLCheckCarInfo != null){
			prpLCheckCarInfoVo = Beans.copyDepth().from(prpLCheckCarInfo).to(PrpLCheckCarInfoVo.class);
		}
		return prpLCheckCarInfoVo;
	}
	
	public Map<String,String> getCarLossParty(String registNo) {
		Map<String,String> result = new HashMap<String,String>();
		List<PrpLCheckCarVo> checkCarVos = 
				checkService.findPrpLcheckCarVoByRegistNo(registNo);
		for(PrpLCheckCarVo carvo:checkCarVos){
			if("1".equals(carvo.getValidFlag())){
				Integer serialNo = carvo.getSerialNo();
				String licenseNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
				String serialNoStr = serialNo.toString();
				if(serialNo == 1){
					result.put(serialNoStr,"标的车("+licenseNo+")");
				}else{
					result.put(serialNoStr,"三者车("+licenseNo+")");
				}
			}
		}
		result.put("0","地面/路人损失");
		return result;
	}

	@Override
	public void saveCheckDutyList(List<PrpLCheckDutyVo> checkDutyVoList) {
		List<PrpLCheckDuty> checkDutyList = 
				Beans.copyDepth().from(checkDutyVoList).toList(PrpLCheckDuty.class);
		databaseDao.saveAll(PrpLCheckDuty.class,checkDutyList);
	}

	/**
	 * 查询巨灾信息
	 * <pre></pre>
	 * @param registNo
	 * @return
	 * @modified: ☆WLL(2016年7月22日 上午10:29:19): <br>
	 */
	public PrpLDisasterVo findDisasterVoByRegistNo(String registNo) {
		PrpLDisasterVo disasterVo = checkService.findDisasterVoByRegistNo(registNo);
		return disasterVo;
	}

	@Override
	public void saveDisasterVo(PrpLDisasterVo prpLDisasterVo) {
		checkService.saveDisaster(prpLDisasterVo);
	}

	@Override
	public void deleteDisasterVo(Long id) {
		databaseDao.deleteByPK(PrpLDisaster.class,id);
	}
	
	public void deleteCheckCar(PrpLCheckCarVo checkCarVo){
		databaseDao.deleteByPK(PrpLCheckCar.class,checkCarVo.getCarid());
	}

	@Override
	public void saveOrUpdateCheckDutyList(List<PrpLCheckDutyVo> checkDutyVoList) {
		for(PrpLCheckDutyVo prpLCheckDutyVo:checkDutyVoList){
			if(prpLCheckDutyVo.getId() != null){
				PrpLCheckDuty prpLCheckDuty = databaseDao.findByPK(PrpLCheckDuty.class, prpLCheckDutyVo.getId());
				Beans.copy().excludeEmpty().excludeNull().from(prpLCheckDutyVo).to(prpLCheckDuty);
				databaseDao.update(PrpLCheckDuty.class, prpLCheckDuty);
			}else{
				PrpLCheckDuty prpLCheckDuty = null;
				Beans.copy().excludeEmpty().excludeNull().from(prpLCheckDutyVo).to(prpLCheckDuty);
				databaseDao.save(PrpLCheckDuty.class, prpLCheckDuty);
			}
		}
	}

	
	@Override
	public PrpLCheckCarVo findCheckCarVoByRegistNoAndId(String registNo,Long id) {
		PrpLCheckCarVo checkCarVo = null;
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("id",id);
		PrpLCheckCar checkCarPo = databaseDao.findUnique(PrpLCheckCar.class,queryRule);
		if(checkCarPo != null ){
			checkCarVo = Beans.copyDepth().from(checkCarPo).to(PrpLCheckCarVo.class);
		}
		return checkCarVo;
	}

	@Override
	public void saveCheckDutyHis(String registNo, String remark) {
		// TODO Auto-generated method stub
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		List<PrpLCheckDuty> checkDutyList = databaseDao.findAll(PrpLCheckDuty.class,queryRule);
		if(checkDutyList!=null && !checkDutyList.isEmpty()){
			List<PrpLCheckDutyHis>  checkDutyHisList = Beans.copyDepth().from(checkDutyList).toList(PrpLCheckDutyHis.class);
			for(PrpLCheckDutyHis dutyHis : checkDutyHisList){
				dutyHis.setId(null);
				dutyHis.setRemark(remark);
				dutyHis.setCreateTime(new Date());
				dutyHis.setUpdateTime(new Date());
			}
			
			databaseDao.saveAll(PrpLCheckDutyHis.class, checkDutyHisList);
		}
		
	}

	@Override
	public void updateCheckCar(PrpLCheckCarVo checkCarVo) {
		PrpLCheckCar checkCarPo = databaseDao.findByPK(PrpLCheckCar.class,checkCarVo.getCarid());
		Beans.copy().from(checkCarVo).excludeNull().to(checkCarPo);
		if(StringUtils.isEmpty(checkCarVo.getLossPart())){
			checkCarPo.setLossPart("");
		}
		checkCarPo.setFlag(CodeConstants.ValidFlag.VALID);
		Beans.copy().from(checkCarVo.getPrpLCheckCarInfo()).excludeNull().to(checkCarPo.getPrpLCheckCarInfo());
		Beans.copy().from(checkCarVo.getPrpLCheckDriver()).excludeNull().to(checkCarPo.getPrpLCheckDriver());
		databaseDao.update(PrpLCheckCar.class,checkCarPo);
	}
	
	@Override
	public void updateCheck(PrpLCheckVo checkVo){
		PrpLCheck checkPo = databaseDao.findByPK(PrpLCheck.class,checkVo.getId());
		Beans.copy().from(checkVo).excludeNull().to(checkPo);
		databaseDao.update(PrpLCheck.class,checkPo);
	}

    @Override
    public List<PrpLCheckCarVo> findCheckCarVoByRegistNoAndSerialNo(String registNo,String serialNo) {
        List<PrpLCheckCarVo> checkCarVos = new ArrayList<PrpLCheckCarVo>();
        QueryRule queryRule=QueryRule.getInstance();
        queryRule.addEqual("registNo", registNo);
        queryRule.addDescOrder(serialNo);
        List<PrpLCheckCar> checkCarPos = databaseDao.findAll(PrpLCheckCar.class,queryRule);
        if(checkCarPos != null && checkCarPos.size() > 0){
            checkCarVos = Beans.copyDepth().from(checkCarPos).toList(PrpLCheckCarVo.class);
        }
        return checkCarVos;
    }

	@Override
	public PrpLCheckDriverVo findPrpLCheckDriverVoById(Long id) {
		PrpLCheckDriverVo prpLCheckDriverVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("driverId",id); 
		PrpLCheckDriver prpLCheckDriver = databaseDao.findUnique(PrpLCheckDriver.class,queryRule);
		if(prpLCheckDriver != null){
			prpLCheckDriverVo = Beans.copyDepth().from(prpLCheckDriver).to(PrpLCheckDriverVo.class);
		}
		return prpLCheckDriverVo;
				
	}
	@Override
	public List<PrpLCheckExtVo> findPrpLcheckExtVoByRegistNo(String registNo) {
		List<PrpLCheckExtVo> extVos=new ArrayList<PrpLCheckExtVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheckExt> extpos=databaseDao.findAll(PrpLCheckExt.class,queryRule);
		if(extpos!= null && extpos.size()>0){
			extVos=Beans.copyDepth().from(extpos).toList(PrpLCheckExtVo.class);
		}
		return extVos;
	}


    @Override
    public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByOther(String registNo,String licenseNo) {
        List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoList = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("licenseNo",licenseNo);
        List<PrpLCheckCarInfo> prpLCheckCarInfoList = databaseDao.findAll(PrpLCheckCarInfo.class,queryRule);
        if(prpLCheckCarInfoList != null && prpLCheckCarInfoList.size() > 0){
            prpLCheckCarInfoVoList = Beans.copyDepth().from(prpLCheckCarInfoList).toList(PrpLCheckCarInfoVo.class);
        }
        return prpLCheckCarInfoVoList;
                
    }

	@Override
	public PrpLCheckDutyVo findprpLCheckDutyById(Long id) {
		PrpLCheckDutyVo prpLCheckDutyVo = null;
		PrpLCheckDuty prpLCheckDuty= databaseDao.findByPK(PrpLCheckDuty.class,id);
		if(prpLCheckDuty != null){
			prpLCheckDutyVo = Beans.copyDepth().from(prpLCheckDuty).to(PrpLCheckDutyVo.class);
		}
		return prpLCheckDutyVo;
	}

	@Override
	public void updatePrpLCheckDuty(String registNo) {
		QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        List<PrpLDlossCarMainVo> carmainList= lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLCheckDuty> checkdutyList=databaseDao.findAll(PrpLCheckDuty.class,queryRule);
		if(carmainList!= null && carmainList.size()>0){
			for(PrpLDlossCarMainVo mainvo:carmainList){
				if(!"1".equals(mainvo.getDeflossCarType())){
					if(checkdutyList!=null && checkdutyList.size()>0){
						for(PrpLCheckDuty duty:checkdutyList){
							if(StringUtils.isNotBlank(mainvo.getLicenseNo()) && mainvo.getLicenseNo().equals(duty.getLicenseNo())){
								duty.setNoDutyPayFlag(mainvo.getIsNodutypayFlag());
							}
						}
					}
				}
			}
		
		
		}
		
		if(carmainList!=null && carmainList.size()>0){
			for(PrpLCheckDuty duty:checkdutyList){
				databaseDao.update(PrpLCheckDuty.class,duty);
			}
		}
		
	}

	@Override
	public void updatePrpLCheckCarInfo(Long id, String flag) {
		checkService.updatePrpLCheckCarInfo(id, flag);
		
	}

	@Override
	public void updatePrpLCheckProp(Long id, String flag) {
		checkService.updatePrpLCheckProp(id, flag);
		}

	@Override
	public void updatePrpLCheckPerson(Long id, String flag) {
		checkService.updatePrpLCheckPerson(id, flag);
		
	}

	@Override
	public PrpLIndiQuotaInfoVo getCheckIndexInfoByUserCode(String userCode) throws IOException {
		String url = SpringProperties.getProperty("RUOYI_CHECK_INDEX");
		HttpClientHander httpClientHander = new HttpClientHander();
		String res = httpClientHander.doPost(url, "userCode=" + userCode);
		if (StringUtils.isNotBlank(res)) {
			JSONObject resJson = JSONObject.fromObject(res);
			PrpLIndiQuotaInfoVo prpLIndiQuotaInfoVo = (PrpLIndiQuotaInfoVo) JSONObject.toBean(resJson, PrpLIndiQuotaInfoVo.class);
			return prpLIndiQuotaInfoVo;
		}
		return null;
	}

	@Override
	public List<PrpLCheckDriverVo> findPrpLCheckDriverVoByRegistNo(String registNo) {
		List<PrpLCheckDriverVo> prpLCheckDriverVoList = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        List<PrpLCheckDriver> prpLCheckDriverList = databaseDao.findAll(PrpLCheckDriver.class,queryRule);
        if(prpLCheckDriverList != null && prpLCheckDriverList.size() > 0){
        	prpLCheckDriverVoList = Beans.copyDepth().from(prpLCheckDriverList).toList(PrpLCheckDriverVo.class);
        }
        return prpLCheckDriverVoList;
	}
}
