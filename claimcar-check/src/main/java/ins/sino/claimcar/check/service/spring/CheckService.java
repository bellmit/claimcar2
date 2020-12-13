/******************************************************************************

 * CREATETIME : 2016年9月26日 下午6:52:56
 ******************************************************************************/
package ins.sino.claimcar.check.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.check.po.PrpLCheck;
import ins.sino.claimcar.check.po.PrpLCheckCar;
import ins.sino.claimcar.check.po.PrpLCheckCarInfo;
import ins.sino.claimcar.check.po.PrpLCheckExt;
import ins.sino.claimcar.check.po.PrpLCheckPerson;
import ins.sino.claimcar.check.po.PrpLCheckProp;
import ins.sino.claimcar.check.po.PrpLCheckTask;
import ins.sino.claimcar.check.po.PrpLDisaster;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckExtVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre></pre>
 *
 * @author ★Luwei
 */
@Service("checkService")
public class CheckService {

	@Autowired
	private DatabaseDao databaseDao;

	private Logger logger = LoggerFactory.getLogger(CheckService.class);

	public PrpLCheckVo findPrpLCheckVoById(Long id) {
		logger.debug("checkId:"+id);
		PrpLCheckVo checkVo = null;
		PrpLCheck checkPo = databaseDao.findByPK(PrpLCheck.class,id);
		if(checkPo!=null){
			checkVo = Beans.copyDepth().from(checkPo).to(PrpLCheckVo.class);
		}
		return checkVo;
	}

	public PrpLCheckVo findPrpLCheckByRegistNo(String registNo) {
		PrpLCheckVo checkVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheck> checkPos = databaseDao.findAll(PrpLCheck.class,queryRule);
		if(checkPos!=null&&checkPos.size()>0){
			checkVo = Beans.copyDepth().from(checkPos.get(0)).to(PrpLCheckVo.class);
		}
		return checkVo;
	}

	public PrpLCheck findPrpLCheckById(Long checkId) {
		return databaseDao.findByPK(PrpLCheck.class,checkId);
	}

	public String getIsClaimSelf(String registNo) {
		return findPrpLCheckByRegistNo(registNo).getIsClaimSelf();
	}

	public Long save(PrpLCheckVo prpLcheckVo,PrpLDisasterVo disasterVo,SysUserVo userVo,String saveType) throws Exception {
		String userCode = userVo.getUserCode();
		String comCode = userVo.getComCode();
		Date nowDate = new Date();
		PrpLCheck checkPo = findPrpLCheckById(prpLcheckVo.getId());
		checkPo.setChkSubmitTime(nowDate);// 查勘提交时间

		PrpLCheckTask checkTaskPo = checkPo.getPrpLCheckTask();
		Beans.copy().from(prpLcheckVo).excludeNull().to(checkPo);
		checkPo.setReconcileFlag(prpLcheckVo.getReconcileFlag());

		Beans.copy().from(prpLcheckVo.getPrpLCheckTask()).excludeNull().to(checkTaskPo);
		calculateFeeSum(findPrpLCheckVoById(prpLcheckVo.getId()));// 计算总损失金额

		checkTaskPo.setComCode(comCode);// 机构
		checkTaskPo.setMakeCom(comCode);// 业务归属机构（待定）
		checkTaskPo.setValidFlag(CodeConstants.ValidFlag.VALID);// 是否有效
		if(StringUtils.isNotBlank(checkTaskPo.getCheckerIdfNo())){
			//查勘员身份证去空格和制表符  防止送平台校验不通过
			checkTaskPo.setCheckerIdfNo(checkTaskPo.getCheckerIdfNo().replaceAll("\\s*",""));
		}

		// 计算费用总和
		BigDecimal propFee = checkTaskPo.getSumLossPropFee();
		BigDecimal repairFee = checkTaskPo.getRepairFee();
		BigDecimal carFee = checkTaskPo.getSumLossCarFee();
		BigDecimal num = NullToZero(propFee).add(NullToZero(repairFee)).add(NullToZero(carFee));
		if(num.longValue()>=50000){
			checkTaskPo.setVerifyCheckFlag(CodeConstants.VerifyCheckFlag.VERIFYCHECK_Y);// 查勘审核标志
			checkTaskPo.setUnderWriteState("0");// 查勘审核状态未接收
		}else{
			checkTaskPo.setVerifyCheckFlag(CodeConstants.VerifyCheckFlag.VERIFYCHECK_N);
		}

		PrpLCheckTaskVo checkTaskVo = prpLcheckVo.getPrpLCheckTask();
		List<PrpLCheckPropVo> checkProVoList = checkTaskVo.getPrpLCheckProps();
		if(checkProVoList!=null&&checkProVoList.size()>0){
			List<PrpLCheckProp> checkPropList = checkTaskPo.getPrpLCheckProps();

			for(PrpLCheckPropVo checkPropVo:checkProVoList){
				if(checkPropVo==null) continue;
				if(checkPropVo.getId()==null){
					String registNo = checkPo.getRegistNo();
					PrpLCheckProp checkProp = new PrpLCheckProp();
					checkProp = Beans.copyDepth().from(checkPropVo).to(PrpLCheckProp.class);
					// 损失方名称
					String partyName = this.getLossPartyName(registNo,checkPropVo.getLossPartyId());
					checkProp.setLossPartyName(0==checkPropVo.getLossPartyId() ? "地面损失" : partyName);
					checkProp.setRegistNo(checkPo.getRegistNo());
					checkProp.setPrpLCheckTask(checkTaskPo);
					checkProp.setCreateUser(userCode);
					checkProp.setCreateTime(nowDate);
					checkProp.setUpdateUser(userCode);
					checkProp.setUpdateTime(nowDate);
					checkPropList.add(checkProp);
				}else{
					updateCheckProp(checkPropVo);
				}
			}
			checkTaskPo.setPrpLCheckProps(checkPropList);
		}

		List<PrpLCheckPerson> checkPersonList = new ArrayList<PrpLCheckPerson>();
		List<PrpLCheckPersonVo> checkPersonVoList = checkTaskVo.getPrpLCheckPersons();
		for(PrpLCheckPersonVo checkPersonVo:checkPersonVoList){
			if(checkPersonVo==null) continue;
			if(checkPersonVo.getId()==null){
				PrpLCheckPerson checkPerson = new PrpLCheckPerson();
				checkPerson = Beans.copyDepth().from(checkPersonVo).to(PrpLCheckPerson.class);
				// 损失方名称
				if(checkPersonVo.getLossPartyId()==0){
					checkPerson.setLossPartyName("地面损失");
				}else{
					String partyName = this.getLossPartyName(checkPo.getRegistNo(),checkPersonVo.getLossPartyId());
					checkPerson.setLossPartyName(partyName);
				}
				checkPerson.setRegistNo(checkPo.getRegistNo());
				checkPerson.setPrpLCheckTask(checkTaskPo);
				checkPerson.setCreateUser(userCode);
				checkPerson.setCreateTime(nowDate);
				checkPerson.setUpdateUser(userCode);
				checkPerson.setUpdateTime(nowDate);
				checkPersonList.add(checkPerson);
			}else{
				updateCheckPerson(checkPersonVo);
			}
		}
		checkTaskPo.setPrpLCheckPersons(checkPersonList);

		List<PrpLCheckExt> checkExtList = new ArrayList<PrpLCheckExt>();
		List<PrpLCheckExtVo> checkExtVoList = checkTaskVo.getPrpLCheckExts();

		for(PrpLCheckExtVo checkExtVo:checkExtVoList){
			if(checkExtVo.getId()==null){
				PrpLCheckExt checkExt = new PrpLCheckExt();
				checkExt = Beans.copyDepth().from(checkExtVo).to(PrpLCheckExt.class);
				checkExt.setRegistNo(checkTaskPo.getRegistNo());
				checkExt.setPrpLCheckTask(checkTaskPo);
				checkExtList.add(checkExt);
			}else{
				updateCheckExt(checkExtVo);
			}
		}

		checkTaskPo.setPrpLCheckExts(checkExtList);

		checkPo.setPrpLCheckTask(checkTaskPo);

		return saveCheck(checkPo);
	}

	public void updateCheckMain(PrpLCheckVo checkVo) {
		PrpLCheck check = databaseDao.findByPK(PrpLCheck.class,checkVo.getId());
		Beans.copy().from(checkVo).excludeNull().to(check);
		Beans.copy().from(checkVo.getPrpLCheckTask()).excludeNull().to(check.getPrpLCheckTask());
		databaseDao.update(PrpLCheck.class,check);
	}

	public PrpLCheckCarVo initPrpLCheckCar(Long carId) {
		PrpLCheckCarVo checkCarVo = null;
		PrpLCheckCar checkCarPo = databaseDao.findByPK(PrpLCheckCar.class,carId);
		if(checkCarPo!=null){
			checkCarVo = Beans.copyDepth().from(checkCarPo).to(PrpLCheckCarVo.class);
		}
		return checkCarVo;
	}

	private void updateCheckProp(PrpLCheckPropVo checkPropVo) {
		Long propId = checkPropVo.getId();
		PrpLCheckProp pdateCheckProp = databaseDao.findByPK(PrpLCheckProp.class,propId);
		Beans.copy().from(checkPropVo).excludeNull().to(pdateCheckProp);
		databaseDao.update(PrpLCheckProp.class,pdateCheckProp);
	}

	private void updateCheckPerson(PrpLCheckPersonVo checkPersonVo) {
		Long personId = checkPersonVo.getId();
		PrpLCheckPerson checkPerson = databaseDao.findByPK(PrpLCheckPerson.class,personId);
		Beans.copy().from(checkPersonVo).excludeNull().to(checkPerson);
		if(checkPersonVo.getLossFee() == null){
			checkPerson.setLossFee(null);
		}
		databaseDao.update(PrpLCheckProp.class,checkPerson);
	}

	private void updateCheckExt(PrpLCheckExtVo checkExtVo) {
		PrpLCheckExt checkExt = databaseDao.findByPK(PrpLCheckExt.class,checkExtVo.getId());
		Beans.copy().from(checkExtVo).excludeNull().to(checkExt);
		databaseDao.update(PrpLCheckExt.class,checkExt);
	}

	public Long getCheckId(String registNo) {
		Long id = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheck> checkPos = databaseDao.findAll(PrpLCheck.class,queryRule);
		if(checkPos!=null&&checkPos.size()>0){
			id = checkPos.get(0).getId();
		}
		return id;
	}

	public Long saveCheck(PrpLCheck checkPo) {
		// 15203 4500400201911010000285移动查勘案件提交查勘报错  为解决此bug对本方法做如下改动
		/*Long checkId = this.getCheckId(checkPo.getRegistNo());
		if (checkId == null) {// 限制多次接收未处理的任务
			databaseDao.save(PrpLCheck.class,checkPo);
		} else {
			checkPo.setId(checkId);
			databaseDao.update(PrpLCheck.class,checkPo);
		}*/
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", checkPo.getRegistNo());
		List<PrpLCheck> checkPos = databaseDao.findAll(PrpLCheck.class, queryRule);
		if (checkPos != null && checkPos.size() > 0) {
			Beans.copy().from(checkPo).excludeNull().to(checkPos.get(0));
			checkPo.setId(checkPos.get(0).getId());
		} else {
			databaseDao.save(PrpLCheck.class, checkPo);
		}

		return checkPo.getId();
	}

	public void saveCheckCar(PrpLCheckCar checkCar) {
		databaseDao.save(PrpLCheckCar.class,checkCar);
	}

	public PrpLCheckCar updateCheckCar(PrpLCheckCarVo checkCarVo) {
		PrpLCheckCar checkCarPo = databaseDao.findByPK(PrpLCheckCar.class,checkCarVo.getCarid());
		Beans.copy().from(checkCarVo).excludeNull().to(checkCarPo);
		if(StringUtils.isEmpty(checkCarVo.getLossPart())){
			checkCarPo.setLossPart("");
		}
		checkCarPo.setFlag(CodeConstants.ValidFlag.VALID);
		Beans.copy().from(checkCarVo.getPrpLCheckCarInfo()).excludeNull().to(checkCarPo.getPrpLCheckCarInfo());
		Beans.copy().from(checkCarVo.getPrpLCheckDriver()).excludeNull().to(checkCarPo.getPrpLCheckDriver());
		databaseDao.update(PrpLCheckCar.class,checkCarPo);
		return checkCarPo;
	}

	public void deleteThirdCarByPK(Long thCarId) {
		databaseDao.deleteByPK(PrpLCheckCar.class,thCarId);
	}

	public void deleteCheckProp(Long propId) throws Exception {
		databaseDao.deleteByPK(PrpLCheckProp.class,propId);
	}

	public void deleteCheckPerson(Long personId) throws Exception {
		databaseDao.deleteByPK(PrpLCheckPerson.class,personId);
	}

	public List<PrpLCheckCarVo> findCheckCarVoByRegistNo(String registNo) {
		List<PrpLCheckCarVo> checkCarVoList = new ArrayList<PrpLCheckCarVo>();
		PrpLCheckCarVo carVo = new PrpLCheckCarVo();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheckCar> carPoList = databaseDao.findAll(PrpLCheckCar.class,queryRule);
		if(carPoList!=null&&carPoList.size()>0){
			for(PrpLCheckCar carPo:carPoList){
				carVo = Beans.copyDepth().from(carPo).to(PrpLCheckCarVo.class);
				checkCarVoList.add(carVo);
			}
		}
		return checkCarVoList;
	}

	public PrpLCheckTask findCheckTaskByRegistNo(String registNo) {
		PrpLCheckTask task = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheckTask> checkTask = databaseDao.findAll(PrpLCheckTask.class,queryRule);
		if(checkTask!=null&&checkTask.size()>0){
			task = checkTask.get(0);
		}
		return task;
	}

	public PrpLDisasterVo findDisasterVoByRegistNo(String registNo) {
		PrpLDisasterVo disasterVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLDisaster> disasterPos = databaseDao.findAll(PrpLDisaster.class,queryRule);
		if(disasterPos!=null&&disasterPos.size()>0){
			disasterVo = new PrpLDisasterVo();
			Beans.copy().from(disasterPos.get(0)).to(disasterVo);
		}
		return disasterVo;
	}

	public void saveDisaster(PrpLDisasterVo disasterVo) {
		PrpLDisaster disaster = null;
		if(disasterVo.getId()!=null){
			disaster = databaseDao.findByPK(PrpLDisaster.class,disasterVo.getId());
			Beans.copy().from(disasterVo).to(disaster);
		}else{
			disaster = Beans.copyDepth().from(disasterVo).to(PrpLDisaster.class);
		}
		databaseDao.save(PrpLDisaster.class,disaster);
	}

	public PrpLCheckCarVo findCarIdBySerialNo(Integer serialNo,String registNo) {
		PrpLCheckCarVo checkCarVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("serialNo",serialNo);
		queryRule.addEqual("registNo",registNo);
		PrpLCheckCar checkCar = databaseDao.findUnique(PrpLCheckCar.class,queryRule);
		if(checkCar!=null){
			checkCarVo = new PrpLCheckCarVo();
			checkCarVo = Beans.copyDepth().from(checkCar).to(PrpLCheckCarVo.class);
		}
		return checkCarVo;
	}

	//----------------------------------util--------------------------//

	/**
	 * 获取财产的损失方名称
	 */
	private String getLossPartyName(String registNo,Long lossPartyId) {
		String lossPartyName = "";
		PrpLCheckCarVo checkCarVo = findCarIdBySerialNo(lossPartyId.intValue(),registNo);
		if(checkCarVo==null) return "地面/路人损失";
		Integer serNo = checkCarVo.getSerialNo();
		String licNo = checkCarVo.getPrpLCheckCarInfo().getLicenseNo();
		if(checkCarVo!=null&&serNo==0){
			lossPartyName = "地面/路人损失";
		}else if(checkCarVo!=null&&serNo==1){
			lossPartyName = "标的车("+licNo+")";
		}else{
			lossPartyName = "三者车("+licNo+")";
		}
		return lossPartyName;
	}

	public static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	}

	// 获取serialNo的最大值
	public Integer getMaxSerialNo(String registNo) {
		List<PrpLCheckCarVo> carList = findCheckCarVoByRegistNo(registNo);
		Integer maxSerialNo = 0;
		for(PrpLCheckCarVo checkCar : carList){
			if(checkCar.getSerialNo()>maxSerialNo){
				maxSerialNo = checkCar.getSerialNo();
			}
		}
		return maxSerialNo;
	}

	/**
	 * <pre>计算损失金额</pre>
	 * @param checkVo
	 * @modified:
	 * ☆Luwei(2016年9月26日 下午8:37:40): <br>
	 */
	public void calculateFeeSum(PrpLCheckVo checkVo) {
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		BigDecimal numRescueFee = new BigDecimal(0);// 总施救费金额
		BigDecimal carLossFee = new BigDecimal(0);// 车损总金额
		BigDecimal propLossFee = new BigDecimal(0);// 物损总金额
		BigDecimal personLossFee = new BigDecimal(0);// 人伤总金额
		List<PrpLCheckCarVo> checkCarVos = checkTaskVo.getPrpLCheckCars();
		for(PrpLCheckCarVo checkCar : checkCarVos){
			if(checkCar.getLossFee()!=null){
				carLossFee = carLossFee.add(checkCar.getLossFee());
			}
			if(checkCar.getRescueFee()!=null){
				numRescueFee = numRescueFee.add(checkCar.getRescueFee());
			}
		}
		List<PrpLCheckPropVo> checkPropVos = checkTaskVo.getPrpLCheckProps();
		for(PrpLCheckPropVo checkProp : checkPropVos){
			if(checkProp.getLossFee()!=null){
				propLossFee = propLossFee.add(checkProp.getLossFee());
			}
		}
		List<PrpLCheckPersonVo> checkPersonVos = checkTaskVo.getPrpLCheckPersons();
		for(PrpLCheckPersonVo checkPerson : checkPersonVos){
			if(checkPerson.getLossFee()!=null){
				personLossFee = personLossFee.add(checkPerson.getLossFee());
			}
		}

		checkTaskVo.setSumLossCarFee(carLossFee);// //车损总损失金额
		checkTaskVo.setSumLossPropFee(propLossFee);// 物损总损失金额
		checkTaskVo.setSumLossPersnFee(personLossFee);// 人伤总损失金额
		checkTaskVo.setSumRescueFee(numRescueFee);// 总施救费
	}

	public void updatePrplCheckCarInfo(PrpLCheckCarInfoVo prplCheckCarInfoVo){
		PrpLCheckCarInfo checkCarInfo = new PrpLCheckCarInfo();
		Beans.copy().from(prplCheckCarInfoVo).to(checkCarInfo);
		databaseDao.update(PrpLCheckCarInfo.class, checkCarInfo);
	}

	public List<PrpLCheckCarInfoVo> findAllInfoByRegistNo(String registNo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		List<PrpLCheckCarInfo> prpLCheckCarInfoLists = databaseDao.findAll(PrpLCheckCarInfo.class,queryRule);
		List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoLists = Beans.copyDepth().from(prpLCheckCarInfoLists).toList(PrpLCheckCarInfoVo.class);
		return prpLCheckCarInfoVoLists;
	}
}
