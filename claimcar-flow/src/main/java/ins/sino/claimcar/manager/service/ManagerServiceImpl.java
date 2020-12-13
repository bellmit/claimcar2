package ins.sino.claimcar.manager.service;

import ins.framework.dao.database.support.Page;
import ins.framework.service.CodeTranService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.checkagency.service.CheckAgencyService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.po.PrpLWfTaskOut;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.SysMsgReceiverService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermBankVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.manager.vo.PrpdIntermUserVo;
import ins.sino.claimcar.manager.vo.PrpdcheckBankVo;
import ins.sino.claimcar.manager.vo.PrpdcheckServerVo;
import ins.sino.claimcar.manager.vo.PrpdcheckUserVo;
import ins.sino.claimcar.manager.vo.SysMsgContentVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.AssessorsTaskService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})

@Path("managerService")
public class ManagerServiceImpl implements ManagerService  {
	
	@Autowired
	private IntermediaryService intermediaryService;
	
	@Autowired
	private RepairFactoryService repairFactoryService;
	
	@Autowired
	private PayCustomService payCustomService;
	
	@Autowired
	private SysMsgReceiverService sysMsgReceiverService;
	@Autowired
	private CheckAgencyService checkAgencyService;
	
	@Autowired
	private AcheckTaskService acheckTaskService;
	
    @Autowired
    private DeflossHandleService deflossHandleService;
    
	@Autowired
	private AssessorsTaskService assessorTaskService;
	
	@Autowired
	private AcheckService acheckService;
	
	@Autowired
	private AssessorService assessorService;
	
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired 
	private CodeTranService codeTranService;
	


	@Override
	public PrpdIntermMainVo findIntermByCode(String intermCode,String comCode) {
		return intermediaryService.findIntermByCode(intermCode,comCode);
	}
	
	@Override
	public PrpdIntermMainVo findIntermByComCode(String comCode) {
		return intermediaryService.findIntermByComCode(comCode);
	}
	
	@Override
	public Page<PrpLRepairFactoryVo> findRepariFactory(PrpLRepairFactoryVo PrpLRepairFactoryVo, int start,int length,String index){
		Page<PrpLRepairFactoryVo> repairfactoryList = repairFactoryService.findRepariFactory(PrpLRepairFactoryVo,start,length,index);
		
		return repairfactoryList;
	}

	@Override
	public PrpLPayCustomVo findPayCustomVoById(Long id) {
		return payCustomService.findPayCustomVoById(id);
	}
	
	@Override
	public List<PrpLPayCustomVo> findPayCustomVoByRegistNo(String registNo) {
		return payCustomService.findPayCustomVoByRegistNo(registNo);
	}

	@Override
	public PrpLRepairFactoryVo findRepariFactoryByCode(String factoryCode,String factoryType) {
		return repairFactoryService.findFactoryByCode(factoryCode,factoryType);
	}
	
	@Override
	public PrpLRepairFactoryVo findRepariFactoryById(String factoryId){
		return repairFactoryService.findFactoryById(factoryId);
	}

	@Override
	public List<SysMsgContentVo> findMsgByUser(String userCode) {
		return sysMsgReceiverService.findMsgByUser(userCode);
	}

	@Override
	public List<PrpLRepairFactoryVo> findFactoryByArea(String areaCode,String brandName) {
		return repairFactoryService.findFactoryByAreaCode(areaCode,brandName);
	}

	@Override
	public List<PrpdIntermMainVo> findIntermListByComCode(String comCode) {
		
		return intermediaryService.findIntermListByCode(comCode);
		}
	@Override
	public List<PrpdIntermMainVo> findIntermListByHql(String comCode) {
		
		return intermediaryService.findIntermListByHql(comCode);
		}
	
	@Override
	public List<PrpdIntermMainVo> findIntermListByCaseType(String comCode,String caseType){
		return intermediaryService.findIntermListByCaseType(comCode,caseType);
	}

	@Override
	public PrpdIntermMainVo findIntermByUserCode(String userCode) {
		return intermediaryService.findIntermByUserCode(userCode);
	}

	@Override
	public PrpdIntermMainVo findIntermById(String id) {
		return intermediaryService.findIntermById(id);
	}
    
	@Override
	public Map<String,String> findUserCode(String comCode,String userInfo,String gradeId) {
		return intermediaryService.findUserCode(comCode,userInfo,gradeId);
	}

	@Override
	public PrpdIntermMainVo saveOrUpdateInterm(PrpdIntermMainVo intermMainVo,PrpdIntermBankVo intermBankVo,List<PrpdIntermUserVo> intermUserVos,SysUserVo userVo) throws Exception{
		return intermediaryService.saveOrUpdateInterm(intermMainVo,intermBankVo,intermUserVos,userVo);
	}

	@Override
	public void saveOrUpdateIntermServer(PrpdIntermMainVo reIntermMainVo,List<PrpdIntermServerVo> intermServerVos) {
		intermediaryService.saveOrUpdateIntermServer(reIntermMainVo,intermServerVos);
	}

	@Override
	public Page<PrpdIntermMainVo> find(PrpdIntermMainVo intermMainVo,int start,int length) {
		return intermediaryService.find(intermMainVo,start,length);
	}

	@Override
	public String existIntermCode(String intermCode) {
		return intermediaryService.existIntermCode(intermCode);
	}
	
	@Override
	public PrpLPayCustomVo adjustExistSamePayCusDifName(PrpLPayCustomVo payCustomVo) {
		return payCustomService.adjustExistSamePayCusDifName(payCustomVo);
	}

	@Override
	public PrpdIntermBankVo findPrpdIntermBankVoById(Long id) {
	 return intermediaryService.findPrpdIntermBankVoById(id);
	}

	@Override
	public List<PrpdIntermBankVo> findPrpdIntermBankVosByIntermId(
			String itermMianId) {
		
		return intermediaryService.findPrpdIntermBankVosByIntermId(itermMianId);
	}

	@Override
	public PrpdIntermBankVo findPrpdIntermBankVosByIntermMainIdAndAccountNo(
			Long intermMainId, String accountNo) {
		
		return intermediaryService.findPrpdIntermBankVosByIntermMainIdAndAccountNo(intermMainId,accountNo);
	}

	@Override
	public PrpdIntermMainVo findIntermById(Long id) {
		
		return intermediaryService.findIntermById(id);
	}

	@Override
	public PrpdCheckBankMainVo findCheckByCode(String checkCode, String comCode) {
	 return checkAgencyService.findCheckBankByCode(checkCode, comCode);
	}

	@Override
	public PrpdCheckBankMainVo findCheckByComCode(String comCode) {
		return checkAgencyService.findCheckBankByComCode(comCode);
	}

	@Override
	public PrpdCheckBankMainVo findCheckById(Long id) {
		return checkAgencyService.findCheckBankById(id);
	}

	@Override
	public List<PrpdCheckBankMainVo> findCheckListByComCode(String comCode) {
		return checkAgencyService.findCheckBankListByComCode(comCode);
	}

	@Override
	public List<PrpdCheckBankMainVo> findCheckListByHql(String comCode) {
		return checkAgencyService.findCheckBankListByHql(comCode);
	}

	@Override
	public List<PrpdCheckBankMainVo> findCheckListByCaseType(String comCode,String caseType) {
		return checkAgencyService.findCheckBankListByCaseType(comCode, caseType);
	}

	@Override
	public PrpdCheckBankMainVo findCheckByUserCode(String userCode) {
		return checkAgencyService.findCheckBankByUserCode(userCode);
	}

	@Override
	public PrpdCheckBankMainVo findCheckById(String id) {
		
		return checkAgencyService.findcheckById(id);
	}

	@Override
	public PrpdcheckBankVo findPrpdcheckBankVoById(Long id) {
		return checkAgencyService.findPrpdcheckBankVoById(id);
	}

	@Override
	public PrpdCheckBankMainVo saveOrUpdatecheckBank(
			PrpdCheckBankMainVo checkBankMainVo, PrpdcheckBankVo checkBankVo,
			List<PrpdcheckUserVo> checkUserVos, SysUserVo userVo)
			throws Exception {
		return checkAgencyService.saveOrUpdateCheck(checkBankMainVo, checkBankVo, checkUserVos, userVo);
	}

	@Override
	public void saveOrUpdateCheckServer(PrpdCheckBankMainVo reCheckBankMainVo,
			List<PrpdcheckServerVo> checkServerVos) {
		checkAgencyService.saveOrUpdateCheckServer(reCheckBankMainVo, checkServerVos);	
	}

	@Override
	public Page<PrpdCheckBankMainVo> find(PrpdCheckBankMainVo checkBankMainVo,
			int start, int length) {
		
		return checkAgencyService.find(checkBankMainVo, start, length);
	}

	@Override
	public List<PrpdcheckBankVo> findPrpdcheckBankVosByCheckId(
			String checkMianId) {
		return checkAgencyService.findPrpdcheckBankVosBycheckId(checkMianId);
	}

	@Override
	public PrpdcheckBankVo findPrpdcheckBankVosByCheckMainIdAndAccountNo(
			Long checkMainId, String accountNo) {
		return checkAgencyService.findPrpdcheckBankVosBycheckMainIdAndAccountNo(checkMainId, accountNo);
	}
	@Override
	public void saveCheckFee(String taskType,Long mainId,String registNo,SysUserVo userVo) {
		if(CodeConstants.CheckTaskType.CHK.equals(taskType)){//如果是查勘
			acheckTaskService.addCheckFeeTaskOfCheck(registNo,userVo);
		} else if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
			PrpLDlossCarMainVo  carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			//先补查勘费任务，如果已经完成车辆核损任务，则需要回写查勘费任务表的核损时间等内容
			acheckTaskService.addCheckFeeTaskOfDcar(carMainVo,userVo,"0");
			if(carMainVo.getUnderWriteEndDate() != null){
				carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
				acheckTaskService.addCheckFeeTaskOfDcar(carMainVo,userVo,"1");
			}
			
		} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//财产定损
			PrpLDlossPersTraceMainVo persTranceMainVo = (PrpLDlossPersTraceMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			//先补查勘费任务，如果已经完成人伤核损任务，则需要回写查勘费任务表的核损时间等内容
			acheckTaskService.addCheckFeeTaskOfDpers(persTranceMainVo,userVo,"0");
			if(persTranceMainVo.getUndwrtFeeEndDate() != null){
				persTranceMainVo = (PrpLDlossPersTraceMainVo)deflossHandleService.findDlossMain(taskType,mainId); 
				acheckTaskService.addCheckFeeTaskOfDpers(persTranceMainVo,userVo,"1");
			}
		} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//人伤定损
			PrpLdlossPropMainVo propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			//先补查勘费任务，如果已经完成财产核损任务，则需要回写查勘费任务表的核损时间等内容
			acheckTaskService.addCheckFeeTaskOfDprop(propMainMainVo,userVo,"0");
			if(propMainMainVo.getUnderWriteEndDate() != null){
				propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
				acheckTaskService.addCheckFeeTaskOfDprop(propMainMainVo,userVo,"1");
			}
		}
		
	}
	
	@Override
	public String existAccountAtCheckmBank(String accountNo){
		return intermediaryService.existAccountAtCheckmBank(accountNo);
	}
	
	/* 
	 * @see ins.sino.claimcar.other.service.AssessorService#supplementAssessors(java.lang.String, java.lang.Long, java.lang.String, ins.platform.vo.SysUserVo)
	 * @param taskType
	 * @param mainId
	 * @param registNo
	 * @param userVo
	 */
	@Override
	public void saveAssessors(String taskType,Long mainId,String registNo,SysUserVo userVo) {
		if(CodeConstants.CheckTaskType.CHK.equals(taskType)){//查勘
			assessorTaskService.addAssTaskOfCheck(registNo,userVo);
		} else{//定损
			Date underWriteEndDate = null;
			Object mainVo = null;
			if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
				PrpLDlossCarMainVo  carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
				underWriteEndDate = carMainVo.getUnderWriteEndDate();
				mainVo = carMainVo;
			} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//人伤定损
				PrpLDlossPersTraceMainVo persTranceMainVo = (PrpLDlossPersTraceMainVo)deflossHandleService.findDlossMain(taskType,mainId);
				underWriteEndDate = persTranceMainVo.getUndwrtFeeEndDate();
				mainVo = persTranceMainVo;
			} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//财产定损
				PrpLdlossPropMainVo propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
				underWriteEndDate = propMainMainVo.getUnderWriteEndDate();
				mainVo = propMainMainVo;
			}
			
			//先补查勘费任务，如果已经完成车辆核损任务，则需要回写查勘费任务表的核损时间等内容
			assessorTaskService.addAssTaskOfDLoss(mainVo,userVo,"0",taskType);
			if(underWriteEndDate != null){
				if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
					PrpLDlossCarMainVo  carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
					mainVo = carMainVo;
				} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//人伤定损
					PrpLDlossPersTraceMainVo persTranceMainVo = (PrpLDlossPersTraceMainVo)deflossHandleService.findDlossMain(taskType,mainId);
					underWriteEndDate = persTranceMainVo.getUndwrtFeeEndDate();
					mainVo = persTranceMainVo;
				} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//财产定损
					PrpLdlossPropMainVo propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
					underWriteEndDate = propMainMainVo.getUnderWriteEndDate();
					mainVo = propMainMainVo;
				}
				assessorTaskService.addAssTaskOfDLoss(mainVo,userVo,"1",taskType);
			}
		}
			
	
	}

	
	@Override
	public boolean findExistACheck(String taskType,Long mainId,String registNo, String userCode) {	
		PrpdCheckBankMainVo checkBankMainVo = this.findCheckByUserCode(userCode);	
		String serialNo = null;
		String licenseNo = null;
		if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
			PrpLDlossCarMainVo  carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			if(carMainVo != null){
				serialNo = String.valueOf(carMainVo.getSerialNo());
				licenseNo = String.valueOf(carMainVo.getLicenseNo());
			}
		} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//财产定损
			PrpLdlossPropMainVo propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			if(propMainMainVo != null){
				serialNo = String.valueOf(propMainMainVo.getSerialNo());
			}
		} 

		if (CodeConstants.CheckTaskType.CAR.equals(taskType) || CodeConstants.CheckTaskType.PROP.equals(taskType)){//车辆定损	
				PrpLAcheckVo prplAcheck = new PrpLAcheckVo();
				prplAcheck.setCheckcode(checkBankMainVo.getCheckCode());	
				prplAcheck.setLicenseNo(licenseNo);
				prplAcheck.setRegistNo(registNo);
				prplAcheck.setSerialNo(serialNo);
				prplAcheck.setTaskType(taskType);
				if(acheckService.findAcheckByParams(prplAcheck) != null){
						return true;
				}
		}
		return false;
		
	
	}


	@Override
	public boolean findExistAsessor(String taskType,Long mainId,String registNo,String userCode) {
		PrpdIntermMainVo itermMainVo = this.findIntermByUserCode(userCode);
		String serialNo = null;
		String licenseNo = null;
		if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
			PrpLDlossCarMainVo  carMainVo = (PrpLDlossCarMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			if(carMainVo != null){
				serialNo = String.valueOf(carMainVo.getSerialNo());
				licenseNo = carMainVo.getLicenseNo();
			}
		} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//财产定损
			PrpLdlossPropMainVo propMainMainVo = (PrpLdlossPropMainVo)deflossHandleService.findDlossMain(taskType,mainId);
			if(propMainMainVo != null){
				serialNo = String.valueOf(propMainMainVo.getSerialNo());
			}		
		} 
		if (CodeConstants.CheckTaskType.CAR.equals(taskType) || CodeConstants.CheckTaskType.PROP.equals(taskType)){//车辆定损
				PrpLAssessorVo prpLAssessorVo = new PrpLAssessorVo();
				prpLAssessorVo.setSerialNo(serialNo);	
				prpLAssessorVo.setIntermcode(itermMainVo.getIntermCode());		
				prpLAssessorVo.setLicenseNo(licenseNo);
				prpLAssessorVo.setTaskType(taskType);
				prpLAssessorVo.setRegistNo(registNo);
				if(assessorService.findAssessorByParams(prpLAssessorVo) != null){
					return true;
				}
		}
		return false;
	}

	/* 
	 * @see ins.sino.claimcar.manager.service.ManagerService#isSubmitHeadOffice(java.lang.String, java.lang.Long)
	 * @param registNo
	 * @param taskid
	 * @return
	 */
	@Override
	public String isSubmitHeadOffice(Map<String,Object> params) throws Exception {
		//判断是否总公司审核开关是否开启
		PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.headOfficeAudit);
		if (configValueVoByMap != null && CodeConstants.CommonConst.TRUE.equals(configValueVoByMap.getConfigValue())) {// 开关
			Double taskId = (Double) params.get("taskId");
			//首次理算冲销发起、首次预付冲销发起，则并没有总公司审核的情景
			if (taskId == null || taskId == 0) {
				return CodeConstants.CommonConst.FALSE;
			}
			PrpLWfTaskVo taskVo = wfTaskHandleService.findTaskIn(taskId);
			//如果是预付核赔、垫付核赔、预付冲销核赔节点、理算核赔、理算冲销核赔应取out表的数据
			if ((FlowNode.PrePayWf.name().equals(params.get("currentNode"))
					|| FlowNode.PrePay.name().equals(params.get("currentNode"))
					|| FlowNode.PadPay.name().equals(params.get("currentNode"))
					|| FlowNode.Compe.name().equals(params.get("currentNode"))
					|| FlowNode.CompeWf.name().equals(params.get("currentNode"))) && taskVo == null) {
				taskVo = wfTaskHandleService.findoutTaskVoByTaskId(taskId);
			}
			if (taskVo == null) {
				return CodeConstants.CommonConst.FALSE;
			}
			String currentCode = taskVo.getSubNodeCode();


			List<String> subnodecodes = new ArrayList<String>();

			if (FlowNode.DLProp.name().equals(currentCode)
					|| FlowNode.DLPropMod.name().equals(currentCode)
					|| (currentCode != null && currentCode.startsWith(FlowNode.VLProp.name()))) {//财损、车损
				subnodecodes.add(FlowNode.VLProp.name());
			} else if (FlowNode.DLCar.name().equals(currentCode) ||
					FlowNode.DLCarMod.name().equals(currentCode)
					|| (currentCode != null && currentCode.startsWith(FlowNode.VPCar.name()))
					|| (currentCode != null && currentCode.startsWith(FlowNode.VLCar.name()))) {//车辆定损、车辆定损修改 、车辆核价提交到核损
				subnodecodes.add(FlowNode.VLCar.name());
				subnodecodes.add(FlowNode.VPCar.name());
			} else if (FlowNode.PLNext.name().equals(currentCode)
					|| FlowNode.PLFirst.name().equals(currentCode)
					|| (currentCode != null && currentCode.startsWith(FlowNode.PLCharge.name()))
					|| (currentCode != null && currentCode.startsWith(FlowNode.PLVerify.name()))) {//人伤
				subnodecodes.add(FlowNode.PLCharge.name());
				subnodecodes.add(FlowNode.PLVerify.name());
			} else if (FlowNode.CompeBI.name().equals(currentCode)
					|| FlowNode.CompeWfBI.name().equals(currentCode)
					|| FlowNode.PrePayBI.name().equals(currentCode)
					|| FlowNode.PrePayWfBI.name().equals(currentCode)) {//商业理算 、商业理算冲销、商业预付冲销、商业预付
				subnodecodes.add("VClaim_BI");
			} else if (FlowNode.CompeCI.name().equals(currentCode)
					|| FlowNode.CompeWfCI.name().equals(currentCode)
					|| FlowNode.PrePayCI.name().equals(currentCode)
					|| FlowNode.PrePayWfCI.name().equals(currentCode)) {//交强理算、交强理算冲销、交强预付冲销、交强预付
				subnodecodes.add("VClaim_CI");
			} else if (FlowNode.PadPay.name().equals(currentCode)) {//垫付
				subnodecodes.add("VClaim_CI");
			} else if (FlowNode.CancelAppJuPei.name().equals(currentCode)) {
				String riskCode = params.get("riskCode") + "";
				if (StringUtils.isBlank(riskCode)) {
					return CodeConstants.CommonConst.FALSE;
				}
				if (Risk.DQZ.equals(riskCode)) {
					subnodecodes.add("VClaim_CI");
				} else {
					subnodecodes.add("VClaim_BI");
				}
			}
			//预付节点，handleridkey存进去的是立案号，但是核赔的时候handleridkey存的是预付号，故预付节点不能传handleridkey字段作为判断条件，应该使用compensateNo
			if (FlowNode.PrePay.name().equals(params.get("currentNode"))) {
				String compensateNo = taskVo.getCompensateNo();
				params.put("compensateNo", compensateNo);
			} else {
				String handlerIdKey = taskVo.getHandlerIdKey();
				params.put("handlerIdKey", handlerIdKey);
			}

			params.put("subnodecodes", subnodecodes);
			String[] workStatusList = new String[]{WorkStatus.BACK, WorkStatus.END};
			params.put("handlerStatus", HandlerStatus.END);
			params.put("workStatusList", workStatusList);
			String[] handeleComcodes = new String[]{CodeConstants.comCodeBegin.headOfficeBegin1, CodeConstants.comCodeBegin.headOfficeBegin2};
			params.put("handeleComcodes", handeleComcodes);

			Boolean flag = wfTaskHandleService.findOutTaskVos(params);
			if (flag) {
				return CodeConstants.CommonConst.TRUE;
			}
		}

		return CodeConstants.CommonConst.FALSE;
	}
}
