/******************************************************************************
 * CREATETIME : 2015年12月9日 上午11:26:35
 * FILE       : ins.sino.claimcar.losscar.service.DeflossHandleService
 ******************************************************************************/
package ins.sino.claimcar.losscar.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Path;

import ins.platform.sysuser.service.facade.SysUserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.IDCardUtil;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.CodeConstants.KINDCODE;
import ins.sino.claimcar.CodeConstants.LossItemParty;
import ins.sino.claimcar.CodeConstants.LossParty;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.UnderWriteFlag;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeHisVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.common.lossindex.service.LossIndexService;
import ins.sino.claimcar.common.lossindex.vo.PrpLDlossIndexVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.fitting.service.ClaimFittingInterService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.AssignUserVo;
import ins.sino.claimcar.flow.vo.PrpDNodeVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.po.PrpLAutoVerify;
import ins.sino.claimcar.losscar.po.PrpLDlossCarMain;
import ins.sino.claimcar.losscar.vo.DefCommonVo;
import ins.sino.claimcar.losscar.vo.DeflossActionVo;
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
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossperson.po.PrpLDlossPersTrace;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersHospitalVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AcheckTaskService;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistRiskInfoService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLcCarDeviceVo;
import ins.sino.claimcar.rule.service.ClaimRuleApiService;
import ins.sino.claimcar.rule.vo.VerifyLossRuleVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;


/**
 * <pre></pre>
 * @author ★yangkun
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("deflossTaskService")
public class DeflossHandleServiceImpl implements DeflossHandleService {

	@Autowired
	DeflossService deflossService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	CodeDictService codeDictService;
	@Autowired
	LossChargeService lossChargeService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	LossIndexService lossIndexService;
	@Autowired
	ManagerService managerService;
	@Autowired
	ClaimFittingInterService fittingInterService;
	@Autowired
	ClaimRuleApiService claimRuleApiService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	SubrogationService subrogationService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	CompensateTaskService compensateTaskService;
	@Autowired
	private WfFlowQueryService wfFlowQueryService;
	@Autowired
	PropLossService propLossService;

	@Autowired
	AssignService assignService;
	@Autowired
	AssessorDubboService assessorService;
	@Autowired
	RegistRiskInfoService registRiskInfoService;
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
	SubmitcaseinforService submitcaseinforService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
	DeflossHandleIlogService deflossHandleIlogService;
	@Autowired
	private IlogRuleService ilogRuleService;
	@Autowired
	private CheckHandleService checkHandleService;
	@Autowired
	private AcheckService acheckService;

	@Autowired
    private AcheckTaskService acheckTaskService;

	@Autowired
	private PersTraceService persTranceService;

	private static Logger logger = LoggerFactory.getLogger(DeflossHandleServiceImpl.class);

	@Override
	public DeflossActionVo prepareAddDefLoss(double flowTaskId,SysUserVo userVo,String sign) {
		DeflossActionVo deflossVo = new DeflossActionVo();
		deflossVo.setUserVo(userVo);

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 重新查询工作流
		String registNo = taskVo.getRegistNo();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		DefCommonVo commonVo = new DefCommonVo();
		// 踢除复检和退回定损的情况
		if((FlowNode.DLCar.name().equals(taskVo.getSubNodeCode()) ||
				FlowNode.DLCarAdd.name().equals(taskVo.getSubNodeCode()))&&
			!CodeConstants.WorkStatus.BYBACK.equals(taskVo.getWorkStatus())&&
CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){// 定损的未处理

			  this.getDefloss(deflossVo,registVo,taskVo);

			if(FlowNode.DLCar.name().equals(taskVo.getSubNodeCode())){
				commonVo.setAcceptFlag("0");// 未接收
			}
		}else{
			if("1".equals(sign)){
				this.getcheckSource(deflossVo,taskVo);// 此方法在定损页面点击同查勘按钮后执行
			}else{
			    this.initDefloss(deflossVo,taskVo);
			}
			if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
				wfTaskHandleService.tempSaveTask(flowTaskId,taskVo.getHandlerIdKey(),userVo.getUserCode(),userVo.getComCode());
			}
		}

		PrpLDlossCarMainVo lossCarMainVo = deflossVo.getLossCarMainVo();
		lossCarMainVo.setFlowFlag(taskVo.getSubNodeCode());

		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(lossCarMainVo.getRegistNo());
		Map<String,String> thirdCarMap = new HashMap<String,String>();
		PrpLCheckDutyVo checkDutyVo = null ;
		if(checkDutyList!=null && !checkDutyList.isEmpty()){
			for(PrpLCheckDutyVo checkDuty : checkDutyList){
				if(checkDuty.getSerialNo()==lossCarMainVo.getSerialNo()){
					checkDutyVo = checkDuty;
				}

				if(checkDuty.getSerialNo()!=1){
					thirdCarMap.put(checkDuty.getSerialNo().toString(),checkDuty.getLicenseNo());
				}

			}
		}
		// 事故责任比例
		if(checkDutyVo!=null){
			lossCarMainVo.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
			lossCarMainVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			lossCarMainVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
			lossCarMainVo.setIsClaimSelf(checkDutyVo.getIsClaimSelf());
		}else{
			lossCarMainVo.setCiDutyFlag("1");// 默认有责
		}

		// 未查勘完成，先定损，绑定checkCarID
		if(lossCarMainVo.getCheckCarId() == null){
			PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(registNo,lossCarMainVo.getSerialNo());
			if(checkCarVo!=null){
				lossCarMainVo.setCheckCarId(checkCarVo.getCarid());
			}
		}

		Map<String,String> kindMap = new HashMap<String,String>();
		List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(registNo,null);
		// 保单的车损险和盗抢险保额
		for(PrpLCItemKindVo itemKindVo : itemKindVoList){
			if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
				if("A".equals(itemKindVo.getKindCode())||"A1".equals(itemKindVo.getKindCode())){// 车损险保额
					commonVo.setCarAmount(itemKindVo.getAmount());
				}else if("G".equals(itemKindVo.getKindCode())){// 盗抢险保额
					commonVo.setTheftAmount(itemKindVo.getAmount());
				}
			}

			if(!itemKindVo.getKindCode().endsWith("M")
					&& !itemKindVo.getKindCode().equals(CodeConstants.KINDCODE.KINDCODE_BZ)
					&& !CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo.getKindCode())){
				kindMap.put(itemKindVo.getKindCode(),itemKindVo.getKindName());
			}
		}
		// 定损不能发起复勘
//		List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),null,FlowNode.Chk);
		// 是否存在复勘 只有定损才能发起复勘
//		if(taskVo.getSubNodeCode().equals(FlowNode.DLCar.name())){
//			if(wfTaskVoList == null || wfTaskVoList.isEmpty()
//					|| wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.ChkRe,"","")){
//			}else{
//				commonVo.setRecheckExist(true);
//			}
//		}

		// 外修特别处理
		List<PrpLDlossCarRepairVo> carRepairList = lossCarMainVo.getPrpLDlossCarRepairs();
		if(carRepairList!=null && !carRepairList.isEmpty()){
			List<PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
			for(PrpLDlossCarRepairVo carRepairVo : carRepairList){
				if("1".equals(carRepairVo.getRepairFlag())){
					outRepairList.add(carRepairVo);
				}
			}
			lossCarMainVo.setOutRepairList(outRepairList);
		}

		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(taskVo.getRegistNo());
		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLDlossPersTraceVo> traceList = findPersTraceList(registNo);
		// 已赔付的金额
		commonVo.setSumpaidDef(getSumPaidDef(lossCarMainList,lossCarMainVo));
		commonVo.setLossCarMainList(lossCarMainList);
		commonVo.setLossPropMainList(lossPropMainList);
		commonVo.setLossPersTraceList(traceList);
		commonVo.setClaimType("0");// 案件类型暂时
		commonVo.setThirdCarMap(thirdCarMap);
		commonVo.setDamageDate(registVo.getDamageTime());
		List<PrpLCMainVo> cmainList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		for(PrpLCMainVo cmainVo : cmainList){
			if(!Risk.isDQZ(cmainVo.getRiskCode()) && !CodeConstants.ISNEWCLAUSECODE_MAP.get(cmainVo.getRiskCode())){
				List<PrpLcCarDeviceVo> carDeviceList = cmainVo.getPrpLcCarDevices();
				if(carDeviceList!=null && !carDeviceList.isEmpty()){
					Map<String,String> deviceMap = new HashMap<String,String>();
					for(PrpLcCarDeviceVo carDeviceVo : carDeviceList){
						deviceMap.put(carDeviceVo.getDeviceId().toString(),carDeviceVo.getDeviceName());
					}
					commonVo.setDeviceMap(deviceMap);
				}
			}
		}
		String kindCode = "";
		if(StringUtils.isNotBlank(lossCarMainVo.getLossFeeType())){
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
				kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
			}else{
				kindCode = CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
			}
			
		}else{// 标的车 如果没有选择损失类别 判断是否单商业报案，有 配件则展示交强险
			if(lossCarMainVo.getSerialNo()==1 &&
					!CodeConstants.ReportType.BI.equals(registVo.getReportType())){
				kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
			}
		}
		commonVo.setCompKindName(kindCode);

		// 查询代位求偿
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(taskVo.getRegistNo());
//		if(subrogationMainVo.getId()==null){
//			subrogationMainVo.setSubrogationFlag("0");
//		}
		deflossVo.setSubrogationMainVo(subrogationMainVo);
		//end
		deflossVo.setTaskVo(taskVo);
		deflossVo.setKindMap(kindMap);
		deflossVo.setCommonVo(commonVo);
		deflossVo.setRegistVo(registVo);

		return deflossVo;
	}

	private List<PrpLDlossPersTraceVo> findPersTraceList(String registNo){
		List<PrpLDlossPersTraceVo> traceList = new ArrayList<PrpLDlossPersTraceVo>();

		List<PrpLDlossPersTraceMainVo> lossPersMainList = persTraceDubboService.findPersTraceMainVoList(registNo);
		// 组织医院信息
		if(lossPersMainList!=null && lossPersMainList.size()>0){
			for(PrpLDlossPersTraceMainVo traceMainVo : lossPersMainList){
				List<PrpLDlossPersTraceVo> traces = traceMainVo.getPrpLDlossPersTraces();
				if(traces!=null && traces.size()>0){
					for(PrpLDlossPersTraceVo traceVo: traces){
						PrpLDlossPersInjuredVo persInjured = traceVo.getPrpLDlossPersInjured();
						List<PrpLDlossPersHospitalVo> hostitalList = persInjured.getPrpLDlossPersHospitals();
						java.util.Collections.sort(hostitalList, new Comparator<PrpLDlossPersHospitalVo>() {
							@Override
							public int compare(PrpLDlossPersHospitalVo o1, PrpLDlossPersHospitalVo o2) {
								if(o1.getInHospitalDate()!=null&&o2.getInHospitalDate()!=null){
									return o1.getInHospitalDate().compareTo(o2.getInHospitalDate());
								}
								return 0;
							}
						});
						if(hostitalList!=null && hostitalList.size()>0){
							persInjured.setHospitalCode(hostitalList.get(0).getHospitalCode());
							persInjured.setHospitalCity(hostitalList.get(0).getHospitalCity());
						}
						// 取人伤跟踪次数
						int traceTimes = persTraceDubboService.getTraceTimes(registNo, persInjured.getId());
						traceVo.setRemark(String.valueOf(traceTimes));
						traceList.add(traceVo);
					}
				}
			}
		}

		return traceList;
	}

	/**
	 * 状态为“未处理”时调用该方法初始化车辆定损,调度到车俩
	 *
	 * <pre></pre>
	 * @modified: ☆yangkun(2015年12月1日 下午4:00:44): <br>
	 */
	private void getDefloss(DeflossActionVo deflossVo,PrpLRegistVo registVo,PrpLWfTaskVo taskVo) {
		PrpLDlossCarInfoVo carInfoVo = new PrpLDlossCarInfoVo();
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo();
		SysUserVo userVo = deflossVo.getUserVo();

		lossCarMainVo.setRegistNo(taskVo.getRegistNo());
		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());
		lossCarMainVo.setScheduleDeflossId(businessId);
		lossCarMainVo.setMercyFlag(registVo.getMercyFlag());// 如果查勘后，则取查勘的紧急程度
		PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(businessId);
		if(CodeConstants.ScheduleDefSource.SCHEDULEDEF.equals(scheduleDefLossVo.getSourceFlag())){ // 调度直接发起定损
			// 调度发起定损，未处理,查勘提交后才处理定损
			if(scheduleDefLossVo.getLossCarId()!=null){
				this.checkAfterDef(scheduleDefLossVo,carInfoVo,lossCarMainVo);
			}else{
				carInfoVo.setLicenseNo(scheduleDefLossVo.getItemsContent());
//				lossCarMainVo.setDeflossCarType(scheduleDefLossVo.getLossitemType());
				lossCarMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS);
				if(scheduleDefLossVo.getSerialNo()==1){
					lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.TARGET);
				}else{
					lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.THIRD);
				}
			}
		}else if(CodeConstants.ScheduleDefSource.SCHEDULEADD.equals(scheduleDefLossVo.getSourceFlag())){// 追加定损
			PrpLDlossCarMainVo lossCarMainOld = deflossService.findDeflossByPk(scheduleDefLossVo.getAddDeflossId());
			if(lossCarMainOld!=null){
				carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainOld.getCarId());
			}
			lossCarMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS);
			lossCarMainVo.setReLossCarId(lossCarMainOld.getId());
			lossCarMainVo.setMercyFlag(lossCarMainOld.getMercyFlag());
			lossCarMainVo.setDeflossCarType(lossCarMainOld.getDeflossCarType());
		}else{// SourceFlag==0 查勘后定损
			this.checkAfterDef(scheduleDefLossVo,carInfoVo,lossCarMainVo);
		}
		// 初始化默认为修复定损
		lossCarMainVo.setCetainLossType("01");
		if("1".equals(registVo.getTpFlag())){
			lossCarMainVo.setLflag("T");
		}else{
			lossCarMainVo.setLflag("L");
		}

		// 定损员号信息(保留 接收保存需要用到)
		lossCarMainVo.setHandlerCode(userVo.getUserCode());
		lossCarMainVo.setHandlerName(userVo.getUserName());
		lossCarMainVo.setHandlerIdNo(userVo.getIdentifyNumber());
		lossCarMainVo.setMakeCom(userVo.getComCode());
		lossCarMainVo.setComCode(registVo.getComCode());
		lossCarMainVo.setRiskCode(registVo.getRiskCode());
		lossCarMainVo.setValidFlag("1");
		lossCarMainVo.setSerialNo(scheduleDefLossVo.getSerialNo());
		// 定损日期默认当期日期
		lossCarMainVo.setDeflossDate(new Date());

		// 1 通过prpduser.usercode 查找prpdintermmain 数据 ,不为空 则为公估定损
		// PrpdIntermMainVo子表有一个公估和用户的子表
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
		if(intermMainVo !=null){
			lossCarMainVo.setIntermFlag("1");// 公估定损
			lossCarMainVo.setIntermCode(intermMainVo.getIntermCode());// 公估机构代码
		}else{
			lossCarMainVo.setIntermFlag("0");
		}
		carInfoVo.setRegistNo(taskVo.getRegistNo());
		carInfoVo.setOperatorCode(userVo.getUserCode());
		carInfoVo.setValidFlag("1");

		if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
			PrpLCMainVo prplcmain = policyViewService.getPolicyAllInfo(lossCarMainVo.getRegistNo()).get(0);
			PrpLCItemCarVo itemCarVo = prplcmain.getPrpCItemCars().get(0);
			carInfoVo.setInsureComCode(prplcmain.getComCode());
			carInfoVo.setInsureComName(codeTranService.transCode("ComCodeFull",prplcmain.getComCode()));
			//t.CarKindCode,t.licensekindcode,t. * FROM prplcitemcar
			if(StringUtils.isBlank(carInfoVo.getCarKindCode())){
				carInfoVo.setCarKindCode(itemCarVo.getCarKindCode());
			}
			if(StringUtils.isBlank(carInfoVo.getLicenseType())){
				carInfoVo.setLicenseType(CodeConvertTool.getVehicleCategory(itemCarVo.getLicenseKindCode()));
			}
		}

		// 默认修理厂
		PrpLRegistCarLossVo tempVo = null;
		for(PrpLRegistCarLossVo vo : registVo.getPrpLRegistCarLosses()){
			String tempSeriaNo = vo.getLossparty();
			String licenseNo = vo.getLicenseNo();
			if(tempSeriaNo.equals(lossCarMainVo.getDeflossCarType())
					&&licenseNo.equals(lossCarMainVo.getLicenseNo())){
				tempVo = vo;
			}
		}
//		String repairCode = tempVo.getRepairCode();
		if (tempVo != null && StringUtils.isNotBlank(tempVo.getRepairCode())) {
			String repairCode = tempVo.getRepairCode();
			PrpLRepairFactoryVo repairVo = managerService.findRepariFactoryById(repairCode);
			if(repairVo != null){
				lossCarMainVo.setRepairFactoryCode(repairVo.getId().toString());
				lossCarMainVo.setRepairFactoryName(repairVo.getFactoryName());
				lossCarMainVo.setRepairFactoryType(repairVo.getFactoryType());
				lossCarMainVo.setFactoryMobile(repairVo.getMobile());
				lossCarMainVo.setRecommendFlag(RadioValue.RADIO_YES);// 临时是否推送修成功
			}
		}

		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		// 追加定损
		if(FlowNode.DLCarAdd.name().equals(taskVo.getSubNodeCode())){
			Double flowTaskId = taskVo.getTaskId().doubleValue();
			lossCarMainVo.setFlowFlag(taskVo.getSubNodeCode());

			this.saveDeflossMain(lossCarMainVo, deflossVo.getUserVo(),FlowNode.DLCarAdd.name());
			String carMainId = deflossVo.getLossCarMainVo().getId().toString();
			wfTaskHandleService.tempSaveTask(flowTaskId,carMainId,userVo.getUserCode(),userVo.getComCode());
		}
	}

	/**
	 * 查勘后定损 初始化定损数据 ☆yangkun(2016年1月28日 下午5:01:38): <br>
	 */
	private void checkAfterDef(PrpLScheduleDefLossVo scheduleDefLossVo,PrpLDlossCarInfoVo carInfoVo,PrpLDlossCarMainVo lossCarMainVo){
		PrpLCheckCarVo checkCarVo = checkTaskService.findByCheckId(scheduleDefLossVo.getLossCarId());
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(lossCarMainVo.getRegistNo());
		PrpLCheckCarInfoVo checkThirdParty = checkCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo driverVo = checkCarVo.getPrpLCheckDriver();
		Beans.copy().from(checkThirdParty).to(carInfoVo);
		carInfoVo.setId(null);
		carInfoVo.setModelName(checkThirdParty.getBrandName());
		carInfoVo.setDriveName(driverVo.getDriverName());
		carInfoVo.setDrivingLicenseNo(driverVo.getDrivingLicenseNo());
		carInfoVo.setIdentifyType(driverVo.getIdentifyType());
		carInfoVo.setIdentifyNo(driverVo.getIdentifyNumber());

		lossCarMainVo.setCheckCarId(checkCarVo.getCarid());
		if(checkCarVo.getSerialNo()==1){
			lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.TARGET);
		}else{
			lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.THIRD);
		}
		lossCarMainVo.setMercyFlag(checkVo.getMercyFlag());
		lossCarMainVo.setSumRescueFee(checkCarVo.getRescueFee());// 施救费来自查勘/
		lossCarMainVo.setDeflossSourceFlag(CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS);
		lossCarMainVo.setLossPart(checkCarVo.getLossPart());

	}

	/**
	 * 正在处理 ☆yangkun(2016年1月19日 下午11:10:56): <br>
	 */
	private void initDefloss(DeflossActionVo deflossVo,PrpLWfTaskVo taskVo){
		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());
		SysUserVo userVo = deflossVo.getUserVo();
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(businessId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		// 定损员号信息
		if(!CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			if(FlowNode.DLChk.name().equals(taskVo.getSubNodeCode())){
		        lossCarMainVo.setIsWhethertheloss(StringUtils.isBlank(lossCarMainVo.getIsWhethertheloss())? "0":lossCarMainVo.getIsWhethertheloss());
			    if(LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
			        lossCarMainVo.setIsSpecialcarFlag(StringUtils.isBlank(lossCarMainVo.getIsSpecialcarFlag())?"0":lossCarMainVo.getIsSpecialcarFlag());
			        lossCarMainVo.setIsBusinesscarFlag(StringUtils.isBlank(lossCarMainVo.getIsBusinesscarFlag())?"0":lossCarMainVo.getIsBusinesscarFlag());
			        lossCarMainVo.setDirectFlag(StringUtils.isBlank(lossCarMainVo.getDirectFlag())?"0":lossCarMainVo.getDirectFlag());
			    }else if(LossParty.THIRD.equals(lossCarMainVo.getDeflossCarType())){
			        lossCarMainVo.setIsNodutypayFlag(StringUtils.isBlank(lossCarMainVo.getIsNodutypayFlag())?"0":lossCarMainVo.getIsNodutypayFlag());
			    }
			    deflossService.updateDefloss(lossCarMainVo);
				if(!userVo.getUserCode().equals(lossCarMainVo.getReCheckCode())){
					lossCarMainVo.setReCheckCode(userVo.getUserCode());
					lossCarMainVo.setReCheckIdNo(userVo.getIdentifyNumber());
					lossCarMainVo.setReCheckName(userVo.getUserName());
				}
			}else {
				if(!userVo.getUserCode().equals(lossCarMainVo.getHandlerCode())){
					lossCarMainVo.setHandlerCode(userVo.getUserCode());
					lossCarMainVo.setHandlerName(userVo.getUserName());
					lossCarMainVo.setHandlerIdNo(userVo.getIdentifyNumber());
					lossCarMainVo.setMakeCom(userVo.getComCode());
				}
			}
		}

		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,lossCarMainVo.getRegistNo(),FlowNode.DLCar.name());
		String flag ="0";
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			flag="1";
		}
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag);
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(businessId,FlowNode.DLCar.name());

		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setLossChargeVos(lossChargeVos);
	}

	// 如果定损页面点击同查勘按钮，则走此方法
	private void getcheckSource(DeflossActionVo deflossVo,PrpLWfTaskVo taskVo){


		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());
		SysUserVo userVo = deflossVo.getUserVo();
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(businessId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(lossCarMainVo.getRegistNo());

		PrpLCheckCarVo checkCarVo = checkTaskService.findCheckCarBySerialNo(lossCarMainVo.getRegistNo(), lossCarMainVo.getSerialNo());
		PrpLCheckCarInfoVo checkThirdParty = checkCarVo.getPrpLCheckCarInfo();
		PrpLCheckDriverVo driverVo = checkCarVo.getPrpLCheckDriver();
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());

		if(checkThirdParty!=null){
			Beans.copy().from(checkThirdParty).to(carInfoVo);
			carInfoVo.setModelName(checkThirdParty.getBrandName());
		}

      if(driverVo!=null){
    	  carInfoVo.setDriveName(driverVo.getDriverName());
    	  carInfoVo.setIdentifyType(driverVo.getIdentifyType());
    	  carInfoVo.setIdentifyNo(driverVo.getIdentifyNumber());
    	  carInfoVo.setDrivingLicenseNo(driverVo.getDrivingLicenseNo());
		}
      if(checkVo!=null){
    	  lossCarMainVo.setMercyFlag(checkVo.getMercyFlag());
      }
      if(checkCarVo!=null){
    	  if(checkCarVo.getSerialNo()==1){
  			lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.TARGET);
  		}else{
  			lossCarMainVo.setDeflossCarType(CodeConstants.LossParty.THIRD);
  		}
      }

		// 事故责任比例
	 if(checkDutyVo!=null){
		lossCarMainVo.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
		lossCarMainVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
		lossCarMainVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
		}
		lossCarMainVo.setLossCarInfoVo(carInfoVo);

		// 定损员号信息
		if(!CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			if(FlowNode.DLChk.name().equals(taskVo.getSubNodeCode())){
				if(!userVo.getUserCode().equals(lossCarMainVo.getReCheckCode())){
					lossCarMainVo.setReCheckCode(userVo.getUserCode());
					lossCarMainVo.setReCheckIdNo(userVo.getIdentifyNumber());
					lossCarMainVo.setReCheckName(userVo.getUserName());
				}
			}else {
				if(!userVo.getUserCode().equals(lossCarMainVo.getHandlerCode())){
					lossCarMainVo.setHandlerCode(userVo.getUserCode());
					lossCarMainVo.setHandlerName(userVo.getUserName());
					lossCarMainVo.setHandlerIdNo(userVo.getIdentifyNumber());
					lossCarMainVo.setMakeCom(userVo.getComCode());
				}
			}
		}

		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,lossCarMainVo.getRegistNo(),FlowNode.DLCar.name());
		String flag ="0";
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			flag="1";
		}
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag);
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(businessId,FlowNode.DLCar.name());

		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setLossChargeVos(lossChargeVos);
	}

	/**
	 * 接收定损任务 ☆yangkun(2016年2月17日 下午4:20:19): <br>
	 */
	@Override
	public PrpLDlossCarMainVo acceptDefloss(Double flowTaskId,String registNo,SysUserVo userVo) {
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		if(CodeConstants.HandlerStatus.DOING.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("该任务已接收，不能重复接收");
		}

		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		DeflossActionVo deflossVo = new DeflossActionVo();
		deflossVo.setUserVo(userVo);

		this.getDefloss(deflossVo,registVo,taskVo);

		PrpLDlossCarMainVo lossCarMainVo = deflossVo.getLossCarMainVo();
		lossCarMainVo.setFlowTaskId(flowTaskId);
//		lossCarMainVo.setNodeCode(taskVo.getSubNodeCode());
		lossCarMainVo.setFlowFlag(taskVo.getSubNodeCode());
		this.saveDeflossMain(lossCarMainVo, deflossVo.getUserVo(),FlowNode.DLCar.name());
		if(lossCarMainVo.getSerialNo()==1){
			PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(taskVo.getRegistNo());
			if(subrogationMainVo == null){
				subrogationMainVo = new PrpLSubrogationMainVo();
				subrogationMainVo.setSubrogationFlag("0");

				this.saveSubrogation(subrogationMainVo, lossCarMainVo, userVo);
			}
		}

		String businessId = deflossVo.getLossCarMainVo().getId().toString();
		wfTaskHandleService.tempSaveTask(flowTaskId,businessId,userVo.getUserCode(),userVo.getComCode());
		return deflossVo.getLossCarMainVo();
	}

	/**
	 * 校验三者车和标的车数据
	 * @param lossId
	 * @param licenseNo
	 * @param licenseType
	 * @param frameNo
	 * @param identifyNo
	 * @param registNo
	 * @param idNoType
	 * @return
	 */
	private String  saveValid(Integer serialNo,Long lossId,String licenseNo,String licenseType,String frameNo,String identifyNo,
			String registNo,String idNoType,String driverLicenseNo,String vinNo,String carKind,String lossType,String flowflag){
		if( !FlowNode.DLChk.name().equals(flowflag)&&"3".equals(lossType)&&StringUtils.isBlank(carKind)){// 三者车做车辆种类的校验
			return "车辆种类不能为空！";
		}
		List<PrpLDlossCarMainVo> carMainList = lossCarService.findLossCarMainByRegistNo(registNo);// 获取标的车的信息
		if(!carMainList.isEmpty() && carMainList.size()>0){
			for(PrpLDlossCarMainVo lossCarMainVo : carMainList){
				if(lossCarMainVo.getLossCarInfoVo() == null ||
					StringUtils.isBlank(lossCarMainVo.getLossCarInfoVo().getVinNo())
					|| StringUtils.isBlank(lossCarMainVo.getLossCarInfoVo().getFrameNo())
					|| StringUtils.isBlank(lossCarMainVo.getLossCarInfoVo().getLicenseNo())
					|| StringUtils.isBlank(lossCarMainVo.getLossCarInfoVo().getLicenseType())){
					continue;
				}
				PrpLDlossCarInfoVo targetLossCarInfoVo =  lossCarMainVo.getLossCarInfoVo();
				String targetLicenseNo = targetLossCarInfoVo.getLicenseNo().trim();
				String targetLicenseType = targetLossCarInfoVo.getLicenseType().trim();
				Long targetLossId = lossCarMainVo.getId();
				String targetFrameNo = targetLossCarInfoVo.getFrameNo();
				String targetVinNo = lossCarMainVo.getLossCarInfoVo().getVinNo();
				if(lossId.longValue() != targetLossId.longValue() && serialNo != lossCarMainVo.getSerialNo()){
					if(StringUtils.strip(licenseNo).equals(StringUtils.strip(targetLicenseNo))
							&& StringUtils.strip(licenseType).equals(StringUtils.strip(targetLicenseType))){
						return "案件出险车辆的号牌种类和号牌号码不能相同，请修改！";
					}
					if(StringUtils.strip(frameNo).equals(StringUtils.strip(targetFrameNo))){
						return "该车辆车架号【"+targetFrameNo+"】已存在，请修改！,";
					}
					if(StringUtils.strip(vinNo).equals(StringUtils.strip(targetVinNo))){
						return "该车辆VIN【"+targetFrameNo+"】已存在，请修改！";
					}
				/*	String oldIdNoType = targetLossCarInfoVo.getIdentifyType();*/
					String oldIdNo = targetLossCarInfoVo.getIdentifyNo();
					// 平台只校验证件号码，只要证件号码不一致则提示错误，以平台为准
					if(StringUtils.isNotBlank(oldIdNo) && StringUtils.strip(identifyNo).equals(StringUtils.strip(oldIdNo))){
						return "该车辆驾驶人证件号码【"+identifyNo+"】已存在，请修改!";
					}
					// 平台只校校验驾驶证号码，只要驾驶证号码不一致则提示错误，以平台为准
					String oldDriverLicenseNo = targetLossCarInfoVo.getDrivingLicenseNo();
					if(StringUtils.isNotBlank(oldDriverLicenseNo) && StringUtils.strip(oldDriverLicenseNo).equals(StringUtils.strip(driverLicenseNo))){
						return "该车辆驾驶证号码【"+driverLicenseNo+"】已存在，请修改!";
					}

				}
			}
		}
		return null;
	}

	/**
	 * 定损保存
	 *
	 * <pre></pre>
	 * @param deflossVo
	 * @modified: ☆yangkun(2016年1月11日 上午11:32:55): <br>
	 */
	@Override
	public void save(PrpLDlossCarMainVo lossCarMainVo,List<PrpLDlossChargeVo> lossChargeVos,
			PrpLClaimTextVo claimTextVo,PrpLSubrogationMainVo subrogationMainVo,PrpLWfTaskVo taskVo,SysUserVo userVo) throws Exception {
        logger.info("案件号={},进入定损保存回写prplcheckduty标志位ValidFlag的方法", lossCarMainVo.getRegistNo());
        String currentNode = taskVo.getSubNodeCode();
        if (!FlowNode.DLChk.name().equals(currentNode)) {// 如果不是复检
            // 校验
            if (validIdCard(lossCarMainVo.getHandlerIdNo()) != null) {
                throw new IllegalAccessException("定损员" + validIdCard(lossCarMainVo.getHandlerIdNo()));
            }
            if ("1".equals(lossCarMainVo.getLossCarInfoVo().getIdentifyType()) && validIdCard(lossCarMainVo.getLossCarInfoVo().getIdentifyNo()) != null) {
                throw new IllegalAccessException("驾驶员" + validIdCard(lossCarMainVo.getLossCarInfoVo().getIdentifyNo()));
            }
            String message = saveValid(lossCarMainVo.getSerialNo(), lossCarMainVo.getId(), lossCarMainVo.getLossCarInfoVo().getLicenseNo(),
                    lossCarMainVo.getLossCarInfoVo().getLicenseType(), lossCarMainVo.getLossCarInfoVo().getFrameNo(),
                    lossCarMainVo.getLossCarInfoVo().getIdentifyNo(), lossCarMainVo.getRegistNo(), lossCarMainVo.getLossCarInfoVo().getIdentifyType(),
                    lossCarMainVo.getLossCarInfoVo().getDrivingLicenseNo(), lossCarMainVo.getLossCarInfoVo().getVinNo(),
                    lossCarMainVo.getLossCarInfoVo().getPlatformCarKindCode(), lossCarMainVo.getDeflossCarType(), currentNode);
            if (message != null) {
                throw new IllegalAccessException(message);
            }
        }
        claimTextVo.setNodeCode(currentNode);
        if (!currentNode.equals(FlowNode.DLChk.name())) {
            PrpLDlossCarInfoVo carInfoVo = lossCarMainVo.getLossCarInfoVo();
            this.saveDeflossMain(lossCarMainVo, userVo, currentNode);
            // 事故责任比例
            PrpLCheckDutyVo checkDutyVo = new PrpLCheckDutyVo();
            checkDutyVo.setSerialNo(lossCarMainVo.getSerialNo());
            checkDutyVo.setCiDutyFlag(lossCarMainVo.getCiDutyFlag());
            checkDutyVo.setDeflossCarId(lossCarMainVo.getId());
            checkDutyVo.setIndemnityDuty(lossCarMainVo.getIndemnityDuty());
            checkDutyVo.setIndemnityDutyRate(lossCarMainVo.getIndemnityDutyRate());
            checkDutyVo.setIsClaimSelf(lossCarMainVo.getIsClaimSelf());
            checkDutyVo.setRegistNo(lossCarMainVo.getRegistNo());
            checkDutyVo.setLicenseNo(carInfoVo.getLicenseNo());
            checkDutyVo.setValidFlag("1");
            checkDutyVo.setUpdateUser(userVo.getUserCode());
            checkTaskService.saveCheckDuty(checkDutyVo);

            // 意见
            this.saveClaimText(lossCarMainVo, claimTextVo, userVo);

            if (lossChargeVos != null && lossChargeVos.size() > 0) {
                Map<String, String> kindMap = new HashMap<String, String>();
                List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(lossCarMainVo.getRegistNo(), "Y");
                for (PrpLCItemKindVo itemKind : itemKinds) {
                    kindMap.put(itemKind.getKindCode(), itemKind.getKindName());
                }
                for (PrpLDlossChargeVo lossCharge : lossChargeVos) {
                    if (lossCharge.getId() != null) {
                        lossCharge.setUpdateTime(new Date());
                        lossCharge.setUpdateUser(userVo.getUserCode());
                    } else {
                        lossCharge.setBusinessId(lossCarMainVo.getId());
                        lossCharge.setRegistNo(lossCarMainVo.getRegistNo());
                        lossCharge.setBusinessType(FlowNode.DLCar.name());
                        lossCharge.setKindName(kindMap.get(lossCharge.getKindCode()));
                        lossCharge.setCreateTime(new Date());
                        lossCharge.setCreateUser(userVo.getUserCode());
                    }

                }
                lossChargeService.saveOrUpdte(lossChargeVos);
            } else {
                lossChargeService.delCharge(lossCarMainVo.getId(), FlowNode.DLCar.name());
            }
            // 判断是否代位求偿
            if (lossCarMainVo.getSerialNo() == 1) {
                this.saveSubrogation(subrogationMainVo, lossCarMainVo, userVo);
            }
            isComAssessment(lossCarMainVo, userVo); // 公估定损
        } else {
            this.saveDeflossMain(lossCarMainVo, userVo, FlowNode.DLChk.name());
            this.saveClaimText(lossCarMainVo, claimTextVo, userVo);
        }
        logger.info("案件号={},结束定损保存回写prplcheckduty标志位ValidFlag的方法", lossCarMainVo.getRegistNo());
    }
	private String validIdCard(String idcardNo){
		if(StringUtils.isBlank(idcardNo)){
			return "身份证号码不能为空";
		} else {
			IDCardUtil idCardUtil = new IDCardUtil();
			return  idCardUtil.IDCardValidate(idcardNo).getErrMsg();
		}
	}

	/**
	 * 保存代位信息
	 * @param deflossVo
	 * @modified: ☆YangKun(2016年4月12日 下午8:20:55): <br>
	 */
	private void saveSubrogation(PrpLSubrogationMainVo subrogationMainVo,PrpLDlossCarMainVo lossCarMainVo, SysUserVo userVo) {
		String assignUser = userVo.getUserCode();
		if(subrogationMainVo.getId() == null){
			subrogationMainVo.setValidFlag("1");
			subrogationMainVo.setRegistNo(lossCarMainVo.getRegistNo());
			subrogationMainVo.setCreateTime(new Date());
			subrogationMainVo.setCreateUser(assignUser);
		}else{
			subrogationMainVo.setUpdateTime(new Date());
			subrogationMainVo.setUpdateUser(assignUser);
		}

		for(PrpLSubrogationCarVo carVo : subrogationMainVo.getPrpLSubrogationCars()){
			if(carVo.getId()!=null){
				carVo.setCreateTime(new Date());
				carVo.setCreateUser(assignUser);
			}else{
				carVo.setUpdateTime(new Date());
				carVo.setUpdateUser(assignUser);
			}
		}
		for(PrpLSubrogationPersonVo personVo : subrogationMainVo.getPrpLSubrogationPersons()){
			if(personVo.getId()!=null){
				personVo.setCreateTime(new Date());
				personVo.setCreateUser(assignUser);
			}else{
				personVo.setUpdateTime(new Date());
				personVo.setUpdateUser(assignUser);
			}
		}

		if(subrogationMainVo.getId()!=null && "0".equals(subrogationMainVo.getSubrogationFlag())){
			subrogationService.deleteSubrogationInfo(subrogationMainVo);
		}else{

			subrogationService.saveSubrogationInfo(subrogationMainVo);
		}

//		subrogationService.saveSubrogationInfo(subrogationMainVo);
	}

	/**
	 * 保存定损主表信息 ☆yangkun(2016年2月17日 下午8:25:21): <br>
	 */
	private void saveDeflossMain(PrpLDlossCarMainVo lossCarMainVo, SysUserVo userVo,String nodeCode){
		PrpLDlossCarInfoVo carInfoVo = lossCarMainVo.getLossCarInfoVo();
		if(carInfoVo.getId()!=null){
			carInfoVo.setUpdateUser(userVo.getUserCode());
			carInfoVo.setUpdateTime(new Date());
		}else{
			carInfoVo.setCreateUser(userVo.getUserCode());
			carInfoVo.setCreateTime(new Date());
		}
		if(StringUtils.isNotBlank(lossCarMainVo.getHandlerIdNo())){
			// 定损员身份证去空格和制表符 防止送平台校验不通过
			lossCarMainVo.setHandlerIdNo(lossCarMainVo.getHandlerIdNo().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(carInfoVo.getIdentifyNo())){
			// 驾驶员身份证去空格和制表符 防止送平台校验不通过
			carInfoVo.setIdentifyNo(carInfoVo.getIdentifyNo().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(carInfoVo.getDrivingLicenseNo())){
			// 驾驶员证去空格和制表符 防止送平台校验不通过
			carInfoVo.setDrivingLicenseNo(carInfoVo.getDrivingLicenseNo().replaceAll("\\s*",""));
		}
		// 保存车辆信息
		Long carId = deflossService.saveOrUpdateDefCarInfo(carInfoVo);

		// 保存定损主表
		if(lossCarMainVo.getId() ==null){
			lossCarMainVo.setCarId(carId);
			lossCarMainVo.setMakeCom(userVo.getComCode());
			lossCarMainVo.setValidFlag("1");
			lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.INIT);
			lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.INIT);
			lossCarMainVo.setLossState("00");// 理赔状态00：未理算通过
			lossCarMainVo.setCreateUser(userVo.getUserCode());
			lossCarMainVo.setCreateTime(new Date());
			
			//定损初始化时，先给directflag（单证齐全标志）字段一个默认值：‘0’(不齐全)，避免因为该字段为空，导致点击单证节点乱码
			lossCarMainVo.setDirectFlag("0");
		}else{
			lossCarMainVo.setUpdateUser(userVo.getUserCode());
			lossCarMainVo.setUpdateTime(new Date());
		}
		lossCarMainVo.setLicenseNo(carInfoVo.getLicenseNo());
		// 判断是否移动端处理的案子
		if("Y".equals(lossCarMainVo.getIsMobileCase())){
			deflossService.saveByJyDefloss(lossCarMainVo);
		}else{
			deflossService.saveOrUpdateDefloss(lossCarMainVo,nodeCode);
		}

		// 追加定损 并且 原定损表ReLossCarId 不为空则重新置为空
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) &&
				lossCarMainVo.getReLossCarId()!=null){
			PrpLDlossCarMainVo lossMainOldVo = deflossService.findDeflossByPk(lossCarMainVo.getReLossCarId());
			if(lossMainOldVo.getReLossCarId()!=null){
				lossMainOldVo.setReLossCarId(null);
				deflossService.updateDefloss(lossMainOldVo);
			}
		}
	}

	/**
	 * 判定定损进入核价or 核损
	 *
	 * <pre></pre>
	 * @param lossCarMainVo
	 * @modified: ☆yangkun(2016年1月16日 下午5:02:29): <br>
	 */
	@Override
	public void getDeflossNextCode(PrpLDlossCarMainVo lossCarMainVo){
		BigDecimal zero = new BigDecimal("0");
		if(lossCarMainVo.getSumCompFee().compareTo(zero)==0 &&
			lossCarMainVo.getSumMatFee().compareTo(zero)==0){

		}
	}

	@Override
	public List<PrpLCItemKindVo> initSubRisks(String registNo,String[] kindCodes){
		List<PrpLCItemKindVo> itemKindList = new ArrayList<PrpLCItemKindVo>();

		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"N");
		List<String> kindCodeList = new ArrayList<String>();
		if(kindCodes.length>0){
			for(String kindCode : kindCodes){
				kindCodeList.add(kindCode);
			}
		}
		kindCodeList.add("--");

		for(PrpLCItemKindVo itemKindVo : itemKinds){
			if(!kindCodeList.contains(itemKindVo.getKindCode())
				&& !itemKindVo.getKindCode().endsWith("M")
				&& !CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo.getKindCode())){
				itemKindList.add(itemKindVo);
			}
		}
		return itemKindList;
	}


	@Override
	public List<PrpLDlossCarSubRiskVo> loadSubRisk(String[] kindCodes,String registNo){
		Map<String,PrpLCItemKindVo> kindMap = new HashMap<String,PrpLCItemKindVo>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"N");
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			kindMap.put(itemKindVo.getKindCode(),itemKindVo);
		}
		List<PrpLDlossCarSubRiskVo> subRiskVoList =new ArrayList<PrpLDlossCarSubRiskVo>();
		if(kindCodes!=null){
			for(String kindCode:kindCodes){
				PrpLDlossCarSubRiskVo subRiskVo = new PrpLDlossCarSubRiskVo();
				subRiskVo.setRegistNo(registNo);
				subRiskVo.setRiskCode(kindMap.get(kindCode).getRiskCode());
				subRiskVo.setKindCode(kindCode);
				subRiskVo.setKindName(kindMap.get(kindCode).getKindName());
				subRiskVo.setUnitAmount(kindMap.get(kindCode).getUnitAmount());
				subRiskVoList.add(subRiskVo);
			}
		}

		return subRiskVoList;
	}

	@Override
	public List<PrpLDlossChargeVo> initChargeType(String[] chargeTypes) {
//		codeDictService.findCodeListByRiskCom(codeType,riskCode,comCode);
		return null;
	}

	/**
	 * 核价初始化 ☆yangkun(2016年1月6日 下午1:25:40): <br>
	 */
	@Override
	public DeflossActionVo prepareAddVerifyPrice(Double flowTaskId,SysUserVo userVo) {
		DeflossActionVo deflossVo = new DeflossActionVo();
		deflossVo.setUserVo(userVo);
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);
		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());

		PrpLRegistVo registVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(businessId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,lossCarMainVo.getRegistNo(),FlowNode.DLCar.name());
		if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
			wfTaskHandleService.tempSaveTask(flowTaskId,businessId.toString(),userVo.getUserCode(),userVo.getComCode());
		}

		String handlerIdNo = lossCarMainVo.getHandlerIdNo();
		if(StringUtils.isNotBlank(handlerIdNo) && handlerIdNo.length()>6){
			handlerIdNo=lossCarMainVo.getHandlerIdNo().substring(0,6)+"************";
			lossCarMainVo.setHandlerIdNo(handlerIdNo);
		}

		DefCommonVo commonVo = new DefCommonVo();
		// 查勘信息
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(lossCarMainVo.getRegistNo());
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		commonVo.setFirstAddressFlag(checkTaskVo.getFirstAddressFlag());
		commonVo.setChecker(checkTaskVo.getChecker());
		commonVo.setCheckerIdfNo(checkTaskVo.getCheckerIdfNo());
		commonVo.setCheckAddress(checkTaskVo.getCheckAddress());
		commonVo.setCheckTime(checkTaskVo.getCheckDate());

		if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
			List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(lossCarMainVo.getRegistNo(),"Y");
			for(PrpLCItemKindVo itemKindVo : itemKindVoList){
				if("A".equals(itemKindVo.getKindCode())){// 车损险保额
					commonVo.setCarAmount(itemKindVo.getAmount());
					break;
				}
			}
		}

		// 偏差核价金额 和偏差核价比例
		BigDecimal offsetVeriRate = new BigDecimal("0");
		BigDecimal offsetVeri = new BigDecimal("0");
		PrpLDlossIndexVo lossIndex = lossIndexService.findLossIndex(lossCarMainVo.getId(),FlowNode.DLCar.name());
		if(lossCarMainVo.getSumVeripLoss()!=null){
			BigDecimal sumVerip = lossCarMainVo.getSumVeripLoss() ;//lossCarMainVo.getSumVeripCompFee().add(lossCarMainVo.getSumVeripMatFee());
			offsetVeri = lossIndex.getFirstVeriLoss().subtract(sumVerip).abs();
			if(sumVerip.compareTo(new BigDecimal("0"))!=0){
				offsetVeriRate = offsetVeri.multiply(new BigDecimal("100")).divide(sumVerip,2,BigDecimal.ROUND_HALF_UP);
			}
		}

		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(taskVo.getRegistNo());
		// 已赔付的金额
		commonVo.setSumpaidDef(getSumPaidDef(lossCarMainList,lossCarMainVo));
		commonVo.setOtherAmount(NullToZero(lossCarMainVo.getSumRepairFee().add(NullToZero(lossCarMainVo.getSumOutFee()))));
		commonVo.setOffsetVeri(offsetVeri);// 偏差核价金额
		commonVo.setOffsetVeriRate(offsetVeriRate);// 偏差核价比例
		commonVo.setClaimType("0");// 案件类型暂时
		commonVo.setDamageDate(registVo.getDamageTime());
		commonVo.setDamageAreaCode(registVo.getDamageAreaCode());
		commonVo.setDamageAddress(registVo.getDamageAddress());
		// 配件险别
		String kindCode = null;
		if(StringUtils.isNotBlank(lossCarMainVo.getLossFeeType())){
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
				kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
			}else{
				kindCode = CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
			}
			
		}else{// 标的车 如果没有选择损失类别 判断是否单商业报案，有 配件则展示交强险
			if(lossCarMainVo.getSerialNo()==1 &&
					!CodeConstants.ReportType.BI.equals(registVo.getReportType())){
				kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
			}
		}
		commonVo.setCompKindName(kindCode);
		// 按钮控制
		int currencyLevel = Integer.parseInt(taskVo.getSubNodeCode().substring("VPCar_LV".length()));
		commonVo.setCurrencyLevel(currencyLevel);
		if(currencyLevel <=8){
			List<PrpDNodeVo> lowNodeList = wfTaskHandleService.findLowerNode(taskVo.getUpperTaskId().doubleValue(),taskVo.getSubNodeCode(),FlowNode.VPCar.name());
			if(lowNodeList!=null && !lowNodeList.isEmpty()){
				commonVo.setLowerButton(true);
			}
		}else{
			commonVo.setLowerButton(true);
		}

		if(lossCarMainVo.getVerifyLevel()!=null && currencyLevel >= lossCarMainVo.getVerifyLevel()){
			commonVo.setVerifyPassFlag(true);
		}

		String flag ="0";
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			flag="1";
		}
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag);
		if(claimTextVo==null){
			claimTextVo = new PrpLClaimTextVo();
			claimTextVo.setOpinionCode("a");
		}

		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setTaskVo(taskVo);
		deflossVo.setCommonVo(commonVo);

		return deflossVo;
	}

	/**
	 * 获得已赔付的定损金额
	 * @param lossCarMainList
	 * @param lossCarMainVo
	 * @modified: ☆YangKun(2016年4月13日 下午4:56:37): <br>
	 */
	private BigDecimal getSumPaidDef(List<PrpLDlossCarMainVo> lossCarMainList,PrpLDlossCarMainVo lossCarMainVo){
		BigDecimal sumpaidDef = new BigDecimal("0");
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) && lossCarMainVo.getId()!=null){
			for(PrpLDlossCarMainVo lossCarMain : lossCarMainList){
				// 同一辆车的定损任务 排除当前
				if(!lossCarMain.getId().equals(lossCarMainVo.getId()) && lossCarMain.getCarId().equals(lossCarMainVo.getCarId())){
					sumpaidDef = sumpaidDef.add(lossCarMain.getSumVeriLossFee());
				}
			}
		}

		return sumpaidDef;
	}

	@Override
	public DeflossActionVo prepareAddVerifyLoss(PrpLWfTaskVo taskVo,SysUserVo userVo) {
		DeflossActionVo deflossVo = new DeflossActionVo();
		Long businessId = Long.parseLong(taskVo.getHandlerIdKey());
		deflossVo.setUserVo(userVo);

		PrpLRegistVo registVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(businessId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(businessId,lossCarMainVo.getRegistNo(),FlowNode.DLCar.name());
		if(CodeConstants.HandlerStatus.INIT.equals(taskVo.getHandlerStatus())){
			wfTaskHandleService.tempSaveTask(taskVo.getTaskId().doubleValue(),businessId.toString(),userVo.getUserCode(),userVo.getComCode());
		}
		// 核赔人身份证
		String underWiteIdNo = lossCarMainVo.getUnderWiteIdNo();
		if(StringUtils.isBlank(underWiteIdNo)){
			underWiteIdNo = userVo.getIdentifyNumber();
			lossCarMainVo.setUnderWriteName(userVo.getUserName());
		}

		if(StringUtils.isNotBlank(underWiteIdNo) && underWiteIdNo.length()>6){
			underWiteIdNo=underWiteIdNo.substring(0,6)+"************";
			lossCarMainVo.setUnderWiteIdNo(underWiteIdNo);
		}
		// 定损人 身份证
		String handlerIdNo = lossCarMainVo.getHandlerIdNo();
		if(StringUtils.isNotBlank(handlerIdNo) && handlerIdNo.length()>6){
			handlerIdNo=lossCarMainVo.getHandlerIdNo().substring(0,6)+"************";
			lossCarMainVo.setHandlerIdNo(handlerIdNo);
		}

		DefCommonVo commonVo = new DefCommonVo();
		// 查勘信息
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(lossCarMainVo.getRegistNo());
		commonVo.setFirstAddressFlag(checkVo.getPrpLCheckTask().getFirstAddressFlag());

		List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(taskVo.getRegistNo(),"Y");
		// 保单的车损险和盗抢险保额
		for(PrpLCItemKindVo itemKindVo : itemKindVoList){
			if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
				if("A".equals(itemKindVo.getKindCode())){// 车损险保额
					commonVo.setCarAmount(itemKindVo.getAmount());
				}
			}
		}

		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
		// 事故责任比例
		lossCarMainVo.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
		lossCarMainVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
		lossCarMainVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
		lossCarMainVo.setIsClaimSelf(checkDutyVo.getIsClaimSelf());

		// 外修特别处理
		List<PrpLDlossCarRepairVo> carRepairList = lossCarMainVo.getPrpLDlossCarRepairs();
		if(carRepairList!=null && !carRepairList.isEmpty()){
			List<PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
			for(PrpLDlossCarRepairVo carRepairVo : carRepairList){
				if("1".equals(carRepairVo.getRepairFlag())){
					outRepairList.add(carRepairVo);
				}
			}
			lossCarMainVo.setOutRepairList(outRepairList);
		}

		// 附加险处理
		List<PrpLDlossCarSubRiskVo> subRiskList = lossCarMainVo.getPrpLDlossCarSubRisks();
		if(!Risk.isDQZ(registVo.getRiskCode())){
			this.handleSubRisk(registVo, subRiskList);
		}

		// 配件险别
		String kindCode = null;
		if(StringUtils.isNotBlank(lossCarMainVo.getLossFeeType())){
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
				kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
			}else{
				kindCode = CodeConstants.LossFeeKind_Map.get(lossCarMainVo.getLossFeeType());
			}
			
		}else{// 标的车 如果没有选择损失类别 判断是否单商业报案，有 配件则展示交强险
			if(lossCarMainVo.getSerialNo()==1 &&
					!CodeConstants.ReportType.BI.equals(registVo.getReportType())){
				kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
			}
		}
		commonVo.setCompKindName(kindCode);
		// 偏差核损金额 和偏差核损比例
		BigDecimal offsetVeriRate = new BigDecimal("0");
		BigDecimal offsetVeri = new BigDecimal("0");
		PrpLDlossIndexVo lossIndex = lossIndexService.findLossIndex(lossCarMainVo.getId(),FlowNode.DLCar.name());
		if(lossCarMainVo.getSumVeriLossFee()!=null){
			offsetVeri = lossIndex.getFirstDefLoss().subtract(lossCarMainVo.getSumVeriLossFee()).abs();
			if(lossCarMainVo.getSumVeriLossFee().compareTo(new BigDecimal("0"))!=0){
				offsetVeriRate = offsetVeri.multiply(new BigDecimal("100")).divide(lossCarMainVo.getSumVeriLossFee(),2,BigDecimal.ROUND_HALF_UP);
			}
		}

		// 损失信息页面
		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(taskVo.getRegistNo());

		// 查询代位求偿
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(taskVo.getRegistNo());
		deflossVo.setSubrogationMainVo(subrogationMainVo);

		// 已赔付的金额
		commonVo.setSumpaidDef(getSumPaidDef(lossCarMainList,lossCarMainVo));
		commonVo.setOffsetVeri(offsetVeri);
		commonVo.setOffsetVeriRate(offsetVeriRate);
		commonVo.setClaimType("0");// 案件类型暂时
		commonVo.setDamageDate(registVo.getDamageTime());

		// 按钮控制
		if(!CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			int currencyLevel = Integer.parseInt(taskVo.getSubNodeCode().substring("VLCar_LV".length()));
			commonVo.setCurrencyLevel(currencyLevel);
			if(currencyLevel<=8){// 分公司
				List<PrpDNodeVo> lowNodeList = wfTaskHandleService.findLowerNode(taskVo.getUpperTaskId().doubleValue(),taskVo.getSubNodeCode(),FlowNode.VLCar.name());
				if(lowNodeList!=null && !lowNodeList.isEmpty()){
					commonVo.setLowerButton(true);
				}
			}else{// 总公司
				commonVo.setLowerButton(true);
			}

			if(lossCarMainVo.getVerifyLevel()!=null && currencyLevel >= lossCarMainVo.getVerifyLevel()){
				commonVo.setVerifyPassFlag(true);
			}

			// 是否存在复勘 只有定损流程才能发起复勘 总公司不能发起复勘
			if(!CodeConstants.defLossSourceFlag.SCHEDULDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) ||
					wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.ChkRe,"","")||
					currencyLevel>8){
				commonVo.setRecheckExist(true);
			}
			// 是否可以发起复检 追加不能发起复检
			//CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) ||
			if(wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.DLChk,lossCarMainVo.getId().toString(),"")){
				commonVo.setReLossFlag("1");
			}
			// 核价不同意，不能发起复检
			if(CodeConstants.VeriFlag.NOSUBMIT.equals(lossCarMainVo.getVeriPriceFlag())){
				commonVo.setReLossFlag("2");
			}
		}

		String flag = "0";// 获取意见信息 节点未完成0,完成之后为1
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			flag="1";
		}
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(businessId,FlowNode.DLCar.name());

		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(businessId,taskVo.getSubNodeCode(),flag);
		if(claimTextVo==null){
			claimTextVo = new PrpLClaimTextVo();
			claimTextVo.setOpinionCode("a");
		}

		List<PrpLDlossPersTraceVo> traceList = findPersTraceList(taskVo.getRegistNo());
		commonVo.setLossPersTraceList(traceList);

		List<PrpLdlossPropMainVo> lossPropMainVos = propLossService.findPropMainByRegistNo(taskVo.getRegistNo());
		deflossVo.setLossPropMainVos(lossPropMainVos);
		commonVo.setLossCarMainList(lossCarMainList);
		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setLossChargeVos(lossChargeVos);
		deflossVo.setTaskVo(taskVo);
		deflossVo.setCommonVo(commonVo);
		deflossVo.setRegistVo(registVo);

		return deflossVo;
	}

	private void handleSubRisk(PrpLRegistVo registVo,List<PrpLDlossCarSubRiskVo> subRiskList){
		if(!CodeConstants.ISNEWCLAUSECODE_MAP.get(registVo.getRiskCode()) && subRiskList!=null && !subRiskList.isEmpty()){
			boolean haveKindX = false;
			for(PrpLDlossCarSubRiskVo subRiskVo : subRiskList){
				if(CodeConstants.KINDCODE.KINDCODE_X.equals(subRiskVo.getKindCode())){
					haveKindX = true;
					break;
				}
			}
			if(haveKindX){
				PrpLCMainVo cmainVo = policyViewService.getPrpLCMainByRegistNoAndPolicyNo(registVo.getRegistNo(),registVo.getPolicyNo());
				List<PrpLcCarDeviceVo> carDeviceList = cmainVo.getPrpLcCarDevices();
				Map<String,String> deviceMap = new HashMap<String,String>();
				if(carDeviceList!=null && !carDeviceList.isEmpty()){
					for(PrpLcCarDeviceVo carDeviceVo : carDeviceList){
						deviceMap.put(carDeviceVo.getDeviceId().toString(),carDeviceVo.getDeviceName());
					}
				}
				for(PrpLDlossCarSubRiskVo subRiskVo : subRiskList){
					if(CodeConstants.KINDCODE.KINDCODE_X.equals(subRiskVo.getKindCode())){
						subRiskVo.setItemName(deviceMap.get(subRiskVo.getItemName()));
					}
				}
			}
		}
	}

	/**
	 * 校验定损信息
	 * @modified: 定损 修改定损 追加定损 复检 ☆yangkun(2016年1月10日 下午3:08:08): <br>
	 */
	@Override
	public String validDefloss(PrpLDlossCarMainVo lossCarMainVo,PrpLDlossCarInfoVo carInfoVo,PrpLSubrogationMainVo subrogationMain,PrpLWfTaskVo taskVo) {
		String retData ="ok";
		if( !FlowNode.DLChk.name().equals(taskVo.getSubNodeCode())&&"3".equals(lossCarMainVo.getDeflossCarType())&&StringUtils.isBlank(carInfoVo
				.getPlatformCarKindCode())){// 三者车做车辆种类的校验
			return "车辆种类不能为空！";
		}
		// 查勘未结束，不能提交
		PrpLRegistVo registVo = registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
		List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),null,FlowNode.Chk);
		if(wfTaskVoList ==null || wfTaskVoList.size()==0){
			return "查勘未提交，定损不能提交！";
		}

		// 校验车架号
		List<PrpLDlossCarMainVo> carMainList = lossCarService.findLossCarMainByRegistNo(lossCarMainVo.getRegistNo());
		//List<PrpLDlossCarInfoVo> carInfoList = deflossService.findLossCarInfoByRegistNo(lossCarMainVo.getRegistNo());
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(lossCarMainVo.getRegistNo());
		PrpLCheckDutyVo checkDutyVo = null;
		String frameNo = carInfoVo.getFrameNo();
		String driverLicenseNo = carInfoVo.getDrivingLicenseNo();  //驾驶证号码
		String identifyNo = carInfoVo.getIdentifyNo();			   //身份证号
		String identifyType = carInfoVo.getIdentifyType();
		int serialNo = lossCarMainVo.getSerialNo();
		String vin = carInfoVo.getVinNo();
		if(!FlowNode.DLChk.name().equals(taskVo.getSubNodeCode())
			&&	frameNo !=null){
			for(PrpLDlossCarMainVo carMain : carMainList){
				PrpLDlossCarInfoVo carInfo = carMain.getLossCarInfoVo();
				if(!carInfo.getId().equals(carInfoVo.getId()) && serialNo != carMain.getSerialNo()){
					if(StringUtils.strip(frameNo).equals(StringUtils.strip(carInfo.getFrameNo()))){
						return "该车辆的车架号【"+carInfo.getFrameNo()+"】已存在，请修改！";
					}
					if(StringUtils.strip(vin).equals(StringUtils.strip(carInfo.getVinNo()))){
						return "该车辆的vin码【"+carInfo.getVinNo()+"】已存在，请修改！";
					}
					// 判断车牌号是否存在
	                if(StringUtils.strip(carInfoVo.getLicenseNo()).equals(StringUtils.strip(carInfo.getLicenseNo()))
	                		&& carInfoVo.getLicenseType().equals(carInfo.getLicenseType())){
						return "案件出险车辆的号牌种类和号牌号码不能相同，请修改！";
	                }
	                if(StringUtils.isNotBlank(driverLicenseNo) && StringUtils.strip(driverLicenseNo).equals(StringUtils.strip(carInfo.getDrivingLicenseNo()))){
						return "该车辆的驾驶证号码【"+driverLicenseNo+"】已存在，请修改！";
	                }
					// 判断驾驶员证件号是否一致//平台只校验证件号
	                if(StringUtils.isNotBlank(identifyNo) &&
	                		StringUtils.strip(identifyNo).equals(StringUtils.strip(carInfo.getIdentifyNo()))){
						return "该车辆的驾驶员证件号码【"+identifyNo+"】已存在，请修改！";
	                }
				}

			}
			//校验驾驶员证件号码和驾驶证号码不能与定损员、跟踪人员、医疗审核人员、伤亡人员证件号码相同
			if(StringUtils.isNotBlank(identifyType) && ("1".equals(identifyType) || "3".equals(identifyType))){
				if(StringUtils.isNotBlank(lossCarMainVo.getHandlerIdNo())){
					if(StringUtils.isNotBlank(identifyNo) && identifyNo.equals(lossCarMainVo.getHandlerIdNo())){
						return "该车辆驾驶员证件号码【"+ identifyNo + "】与定损员证件号码不能相同，请修改！！！";
					}
					if(StringUtils.isNotBlank(driverLicenseNo) && driverLicenseNo.equals(lossCarMainVo.getHandlerIdNo())){
						return "该车辆驾驶证号码【" + driverLicenseNo + "】与定损员证件号码不能相同，请修改！！！";
					}
					
				}
				List<PrpLDlossPersTraceMainVo> persMainList = persTraceDubboService.findPersTraceMainVoList(lossCarMainVo.getRegistNo());
				if(persMainList != null && persMainList.size() > 0){
					for(PrpLDlossPersTraceMainVo persTraceMain : persMainList){
						if(StringUtils.isNotBlank(persTraceMain.getPlfCertiCode())){
							if(StringUtils.isNotBlank(identifyNo) && identifyNo.equals(persTraceMain.getPlfCertiCode())){
								return "该车辆驾驶员证件号码【"+ identifyNo + "】与人伤跟踪员证件号码不能相同，请修改！！！";
							}
							if(StringUtils.isNotBlank(driverLicenseNo) && driverLicenseNo.equals(persTraceMain.getPlfCertiCode())){
								return "该车辆驾驶证号码【" + driverLicenseNo + "】与人伤跟踪员证件号码不能相同，请修改！！！";
							}
						}
						if(StringUtils.isNotBlank(persTraceMain.getOperatorCertiCode())){
							if(StringUtils.isNotBlank(identifyNo) && identifyNo.equals(persTraceMain.getOperatorCertiCode())){
								return "该车辆驾驶员证件号码【" + identifyNo + "】与人伤定损员证件号码不能相同，请修改！！！";
							}
							if(StringUtils.isNotBlank(driverLicenseNo) && driverLicenseNo.equals(persTraceMain.getOperatorCertiCode())){
								return "该车辆驾驶员证件号码【" + driverLicenseNo + "】与人伤定损员证件号码不能相同，请修改！！！";
							}
						}
						if(StringUtils.isNotBlank(persTraceMain.getVerifyCertiCode())){
							if(StringUtils.isNotBlank(identifyNo) && identifyNo.equals(persTraceMain.getVerifyCertiCode())){
								return "该车辆驾驶员证件号码【"+ identifyNo + "】与医疗审核人员证件号码不能相同，请修改！！！";
							}
							if(StringUtils.isNotBlank(driverLicenseNo) && driverLicenseNo.equals(persTraceMain.getVerifyCertiCode())){
								return "该车辆驾驶证号码【" + driverLicenseNo + "】与医疗审核人员证件号码不能相同，请修改！！！";
							}
						}
					}
				}
			}

			// 校验案件的责任比例
			BigDecimal sumDutyRate = new BigDecimal("0");
			for(PrpLCheckDutyVo checkDuty : checkDutyList){
				if(checkDuty.getSerialNo()!=lossCarMainVo.getSerialNo()){
					sumDutyRate = sumDutyRate.add(checkDuty.getIndemnityDutyRate());
				}
				if(checkDuty.getSerialNo() == 1){
					checkDutyVo = checkDuty;
				}
			}
			if(lossCarMainVo.getIndemnityDutyRate() !=null){
				sumDutyRate = sumDutyRate.add(lossCarMainVo.getIndemnityDutyRate());
				if(sumDutyRate.compareTo(new BigDecimal("100")) == 1){
					return "事故责任比例超过100%，请修改!";
				}
			}
		}


		// 校验是否进入精友
		if("01".equals(lossCarMainVo.getCetainLossType()) || "04".equals(lossCarMainVo.getCetainLossType())){
			List<PrpLDlossCarMainVo> losscarMainList = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
			PrpLDlossCarMainVo losscarMain = losscarMainList.get(0);
			if(losscarMain==null || losscarMain.getFlag()==null){
				return "请您进入精友配件系统进行定损！ ";
			}
		}

		PrpLDlossCarMainVo lossCarMain = deflossService.findDeflossByPk(lossCarMainVo.getId());

		String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
		Date timeStamp = null;
		try {
			timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(registVo.getReportTime().getTime()<timeStamp.getTime()){// 精友1代校验
			List<PrpLDlossCarCompVo> carCompList = lossCarMain.getPrpLDlossCarComps();
			if(carCompList!=null && carCompList.size()>0){
				String factoryType = carCompList.get(0).getPriceType();
				if(!lossCarMainVo.getRepairFactoryType().equals(factoryType)){
					return "修理厂类型和精友系统不一致请修改";
				}
			}
		}

		// 校验代位求偿的车辆
		if("1".equals(subrogationMain.getSubrogationFlag())){
			List<PrpLCheckCarVo> checkCarList = checkTaskService.findCheckCarVo(lossCarMainVo.getRegistNo());
			// 定损三者车
			Map<Integer,String> thirdCarMap = new HashMap<Integer,String>();
			// 查勘三者车
			Map<Integer,String> checkCarMap = new HashMap<Integer,String>();
			// 车辆序号 - 车牌号码-车牌类型-vin码-发动机号
			for(PrpLDlossCarMainVo carMain : carMainList){
				PrpLDlossCarInfoVo carInfo = carMain.getLossCarInfoVo();
				thirdCarMap.put(carMain.getSerialNo(),carInfo.getLicenseNo()+"-"+
						carInfo.getLicenseType()+"-"+carInfo.getVinNo()+"-"+carInfo.getEngineNo());
			}

			for(PrpLCheckCarVo checkCar : checkCarList){
				PrpLCheckCarInfoVo carInfo = checkCar.getPrpLCheckCarInfo();
				checkCarMap.put(checkCar.getSerialNo(),carInfo.getLicenseNo()+"-"+
						carInfo.getLicenseType()+"-"+carInfo.getVinNo()+"-"+carInfo.getEngineNo());
			}

			for(PrpLSubrogationCarVo subrogationCar : subrogationMain.getPrpLSubrogationCars()){
				String carStr = subrogationCar.getLicenseNo()+"-"+
						subrogationCar.getLicenseType()+"-"+subrogationCar.getVinNo()+"-"+subrogationCar.getEngineNo();
				if(thirdCarMap.get(subrogationCar.getSerialNo())!=null){// 定损中有
					if(!carStr.equals(thirdCarMap.get(subrogationCar.getSerialNo()))){
						logger.debug("代位 "+carStr);
						logger.debug("定损 "+thirdCarMap.get(subrogationCar.getSerialNo()));
						return "代位求偿信息中的"+subrogationCar.getLicenseNo()+"的车牌号码，车辆种类，Vin码和发动机号需要和定损的三者车一致";
					}
				}else{// 查勘中有
					if(!carStr.equals(checkCarMap.get(subrogationCar.getSerialNo()))){
						logger.debug("代位 "+carStr);
						logger.debug("查勘 "+checkCarMap.get(subrogationCar.getSerialNo()));
						return "代位求偿信息中的"+subrogationCar.getLicenseNo()+"车牌号码，车辆种类，Vin码和发动机号需要和查勘的三者车一致";
					}
				}
			}
		}


		// 追加定损不能损余回收
		if(FlowNode.DLCarAdd.name().equals(taskVo.getSubNodeCode()) && "1".equals(lossCarMain.getRecycleFlag())){
			return "追加定损不能有损余回收,请修改";
		}

		if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
			// 标的车需要校验是否 是否满足互碰自赔案件，所选择的险别是否合理
			if("1".equals(lossCarMainVo.getIsClaimSelf())){
				String selfStr = isClaimSelf(lossCarMainVo,checkDutyList,registVo);
				if(StringUtils.isNotBlank(selfStr)){
					return selfStr;
				}
			}
		}else{// 三者车 互碰自赔 只能0定损
			if("1".equals(checkDutyVo.getIsClaimSelf())){
				String selfStr = thirdCarClaimSelf(lossCarMainVo);
				if(StringUtils.isNotBlank(selfStr)){
					return selfStr;
				}
			}
		}

		List<PrpLClaimVo> claimList = claimTaskService.findClaimListByRegistNo(lossCarMainVo.getRegistNo());
		PrpLClaimVo prpLclaimVo = null;
		for(PrpLClaimVo claimVo : claimList){
			if(!Risk.isDQZ(claimVo.getRiskCode())){
				prpLclaimVo = claimVo;
			}
		}
		if(lossCarMainVo.getSerialNo()==1 && prpLclaimVo!=null){
			retData = this.validLossFeeCompareAmount(prpLclaimVo,lossCarMainVo);
		}

		return retData;
	}

	/**
	 * 是否赔付过 false 已赔付，true 未理算
	 * @param lossState
	 * @return
	 * @modified: ☆YangKun(2016年7月9日 下午3:58:56): <br>
	 */
	@Override
	public boolean checkLossState(String lossState) {
		// 第一位-交强 第二位-商业 0-未理算 1-已理算
		if (StringUtils.isNotBlank(lossState)) {
			if (lossState.equals("00") || lossState.equals("10")
					|| lossState.equals("0")) {
				return true;
			}
		}
		return false;// 已理算
	}

	/**
	 * 校验附加险和保额
	 * @param lossCarMainVo
	 * @return
	 * @modified: ☆YangKun(2016年7月9日 下午3:07:52): <br>
	 */
	private String validLossFeeCompareAmount(PrpLClaimVo claimVo,PrpLDlossCarMainVo lossCarMainVo){
		Map<String,BigDecimal> sumPaidMap = new HashMap<String,BigDecimal>();
		// 查询险别保额数据
		Map<String,PrpLCItemKindVo> kindMap = new HashMap<String,PrpLCItemKindVo>();
		List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(lossCarMainVo.getRegistNo(),null);
		BigDecimal amountKindA = BigDecimal.ZERO;
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			if(CodeConstants.KINDCODE.KINDCODE_A.equals(itemKindVo.getKindCode().trim())){
				amountKindA = itemKindVo.getAmount();
				break;
			}
		}

		for(PrpLCItemKindVo itemKindVo:itemKinds){// 发动机特别损 玻璃破碎 用车损险保额
			if("N".equals(itemKindVo.getCalculateFlag())){
				if(CodeConstants.KINDCODE.KINDCODE_X1.equals(itemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_X2.equals(itemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_F.equals(itemKindVo.getKindCode().trim()) ||
						CodeConstants.KINDCODE.KINDCODE_NT.equals(itemKindVo.getKindCode().trim())){
					itemKindVo.setAmount(amountKindA);
				}
				kindMap.put(itemKindVo.getKindCode(),itemKindVo);
			}
		}
		// 保单冲减金额
		Map<String,BigDecimal> eMotorMap = compensateTaskService.queryEmotorMap(claimVo.getPolicyNo());

		// 如果是追加定损 则则加上标的车原来车辆定损的核损金额
		// 追加定损 应该是用已赔付金额
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
			// 已赔付的金额
			sumPaidMap = compensateTaskService.querySumRealPay(claimVo.getClaimNo(),null);
			List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
			if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
				for(PrpLDlossCarMainVo lossCarVo : lossCarMainList ){
					if(!lossCarMainVo.getId().equals(lossCarVo.getId()) &&
							checkLossState(lossCarVo.getLossState())){

						thisCaseLossFee(lossCarVo,sumPaidMap,false);
					}
				}
			}
		}
		// 本定损
		this.thisCaseLossFee(lossCarMainVo,sumPaidMap,true);

		for(String kindCode : sumPaidMap.keySet()){
			PrpLCItemKindVo itemKindVo = kindMap.get(kindCode);
			// eMotor扣减。
			BigDecimal eMotorAmount = BigDecimal.ZERO;
			if(eMotorMap.containsKey(kindCode)){
				eMotorAmount = eMotorMap.get(kindCode);
			}

			if(CodeConstants.KINDCODE.KINDCODE_T.equals(kindCode)
					|| CodeConstants.KINDCODE.KINDCODE_C.equals(kindCode)
||CodeConstants.KINDCODE.KINDCODE_RF
					.equals(kindCode)){// 计算剩余天数


				BigDecimal paidDay = eMotorAmount.divide(itemKindVo.getUnitAmount(),2,BigDecimal.ROUND_HALF_UP);
				BigDecimal leftDay = itemKindVo.getQuantity().subtract(paidDay);
				if(leftDay.compareTo(BigDecimal.ZERO)!=1){
					leftDay = BigDecimal.ZERO;
				}
				if(sumPaidMap.get(kindCode).compareTo(leftDay)==1){
					return itemKindVo.getKindName()+"剩余最大赔付天数"+leftDay.intValue()+",请修改";
				}

			}else if(CodeConstants.KINDCODE.KINDCODE_L.equals(kindCode)
					|| CodeConstants.KINDCODE.KINDCODE_NZ.equals(kindCode)
||CodeConstants.KINDCODE.KINDCODE_Z2
					.equals(kindCode)){// 这些条款会冲减保额
				// 剩余限额
				BigDecimal leftAmount = itemKindVo.getAmount().subtract(eMotorAmount);

				if(leftAmount.compareTo(sumPaidMap.get(kindCode))== -1){
					return itemKindVo.getKindName()+"剩余限额"+leftAmount+",请修改";
				}
			}else{
				if(itemKindVo.getAmount().compareTo(sumPaidMap.get(kindCode))== -1){// 如果是追加定损 需要加上原定损
					return itemKindVo.getKindName()+"定损金额超过保额";
				}
			}
		}

		return "ok";
	}

	/**
	 * this 是否是本次定损任务
	 * @param lossCarVo
	 * @param sumPaidMap
	 * @param thisLoss
	 * @return
	 * @modified: ☆YangKun(2016年7月9日 下午4:12:28): <br>
	 */
	private Map<String,BigDecimal> thisCaseLossFee(PrpLDlossCarMainVo lossCarVo,Map<String,BigDecimal> sumPaidMap,Boolean thisLoss){
		String kindCode ="";
		BigDecimal lossFee = BigDecimal.ZERO;
		if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarVo.getCetainLossType())){
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarVo.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarVo.getRiskCode())){
				kindCode = CodeConstants.LossFee2020Kind_Map.get(lossCarVo.getRiskCode()+lossCarVo.getLossFeeType());
			}else{
				kindCode = CodeConstants.LossFeeKind_Map.get(lossCarVo.getLossFeeType());
			}
			
			if(StringUtils.isNotBlank(kindCode)
				&& !CodeConstants.KINDCODE.KINDCODE_A.equals(kindCode)
				&& !CodeConstants.KINDCODE.KINDCODE_G.equals(kindCode)){
				if(thisLoss){
					lossFee = lossCarVo.getSumLossFee();
				}else{
					lossFee = lossCarVo.getSumVeriLossFee();
				}

				if(sumPaidMap.containsKey(kindCode)){// 审核通过用核损金额，
					sumPaidMap.put(kindCode,sumPaidMap.get(kindCode).add(lossFee));
				}else{
					sumPaidMap.put(kindCode,lossFee);
				}
			}
		}
		List<PrpLDlossCarSubRiskVo> subRiskList = lossCarVo.getPrpLDlossCarSubRisks();
		if(subRiskList!=null && !subRiskList.isEmpty()){
			for(PrpLDlossCarSubRiskVo subRiskVo : subRiskList){
				kindCode = subRiskVo.getKindCode();
				if(CodeConstants.KINDCODE.KINDCODE_T.equals(kindCode)
						|| CodeConstants.KINDCODE.KINDCODE_C.equals(kindCode)
						|| CodeConstants.KINDCODE.KINDCODE_RF.equals(kindCode)){
					if(thisLoss){
						lossFee = subRiskVo.getCount();
					}else{
						lossFee = subRiskVo.getVeriCount();
					}

				}else{
					if(thisLoss){
						lossFee = subRiskVo.getSubRiskFee();
					}else{
						lossFee = subRiskVo.getVeriSubRiskFee();
					}
				}

				if(sumPaidMap.containsKey(kindCode)){
					sumPaidMap.put(kindCode,sumPaidMap.get(kindCode).add(lossFee));
				}else{
					sumPaidMap.put(kindCode,lossFee);
				}
			}
		}

		return sumPaidMap;
	}

	/**
	 * 标的车判断 判定是否是互碰自赔案件 1 多车事故 2 仅涉及车辆损失（包括车上财产和车上货物）、不涉及人员伤亡和车外财产损失，各方损失金额均在2000元以内 不存在三者车的车和财定损 或则0提 3 各方均有责任 5 校验各方险别 ☆yangkun(2016年3月8日 下午9:27:42): <br>
	 */
	@Override
	public String isClaimSelf(PrpLDlossCarMainVo lossCarMainVo,List<PrpLCheckDutyVo> checkDutyList,PrpLRegistVo registVo){
		String selfStr = "";
		String registNo = lossCarMainVo.getRegistNo();
		if(lossCarMainVo.getSumLossFee() == null){
			lossCarMainVo.setSumLossFee(new BigDecimal("0"));
		}
		if(lossCarMainVo.getSumRescueFee() == null){
			lossCarMainVo.setSumRescueFee(new BigDecimal("0"));
		}
		boolean isRiskDQZ = false;
		List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if (cMainVoList != null && !cMainVoList.isEmpty()) {
			for (PrpLCMainVo cMainVo : cMainVoList) {
				if (Risk.DQZ.equals(cMainVo.getRiskCode())) {
					isRiskDQZ = true;
				}
			}
		}
//		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);

		if(!isRiskDQZ){
			return "该案件没有用交强险报案，不符合互碰自赔条件！";
		}
//		if("1".equals(checkVo.getSingleAccidentFlag())){
		// return "单车事故，不符合互碰自赔条件！";
//		}

		List<PrpLDlossPersTraceMainVo> traceMainList = persTraceDubboService.findPersTraceMainVoList(registNo);
		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PLFirst,null,"")){
			if(traceMainList!=null && !traceMainList.isEmpty()){
				for(PrpLDlossPersTraceMainVo traceMainVo : traceMainList){
					if(!"10".equals(traceMainVo.getCaseProcessType()) &&
 !"7".equals(traceMainVo.getUnderwriteFlag())){// 10 无需赔付并且没有注销
						return "该案件涉及人员伤亡，不符合互碰自赔条件！";
					}
				}
			}else{// 发起了人伤但是未处理
				return "该案件涉及人员伤亡，不符合互碰自赔条件！";
			}
		}


		// 有地面财不行
		for(PrpLCheckDutyVo checkDutyVo : checkDutyList){
			if("0".equals(checkDutyVo.getIndemnityDuty())||
					"4".equals(checkDutyVo.getIndemnityDuty())){
				return "车辆："+checkDutyVo.getLicenseNo()+" 事故责任为全责或者无责，不符合互碰自赔条件！";
			}
		}

		Map<Integer,BigDecimal> defMap = new HashMap<Integer,BigDecimal>();
		Map<Integer,String> licenseNoMap = new HashMap<Integer,String>();
		licenseNoMap.put(lossCarMainVo.getSerialNo(),lossCarMainVo.getLicenseNo());
		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(registNo);
		List<PrpLScheduleDefLossVo> scheduleCarLossList = scheduleService.findPrpLScheduleCarLossList(registNo);
		defMap.put(lossCarMainVo.getSerialNo(),lossCarMainVo.getSumLossFee().add(lossCarMainVo.getSumRescueFee()));
		if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
			if(scheduleCarLossList != null && scheduleCarLossList.size() == 1){
				return "单车事故，不符合互碰自赔条件！";
			}
			for(PrpLDlossCarMainVo otherLossCar : lossCarMainList){
				if(otherLossCar.getId().equals(lossCarMainVo.getId())){
					licenseNoMap.put(otherLossCar.getSerialNo(),otherLossCar.getLicenseNo());
					continue;
				}

				BigDecimal otherLoss = otherLossCar.getSumLossFee().add(otherLossCar.getSumRescueFee());
				if(defMap.containsKey(otherLossCar.getSerialNo())){
					defMap.put(otherLossCar.getSerialNo(),defMap.get(otherLossCar.getSerialNo()).add(otherLoss));
				}else{
					defMap.put(otherLossCar.getSerialNo(),otherLoss);
					licenseNoMap.put(otherLossCar.getSerialNo(),otherLossCar.getLicenseNo());
				}
			}
		}

		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(registNo);
		if(lossPropMainList!=null && !lossPropMainList.isEmpty()){
			for(PrpLdlossPropMainVo propMainVo : lossPropMainList){
				if(propMainVo.getSerialNo()==0){// 地面财
					BigDecimal otherLoss = propMainVo.getSumDefloss().add(NullToZero(propMainVo.getDefRescueFee()))
										.add(NullToZero(propMainVo.getSumLossFee()));
					if(otherLoss.compareTo(BigDecimal.ZERO)==1){
						return "存在地面财损失，不符合互碰自赔条件！";
					}
				}else{
					BigDecimal otherLoss = propMainVo.getSumDefloss().add(NullToZero(propMainVo.getDefRescueFee()));
					if(defMap.containsKey(propMainVo.getSerialNo())){
						defMap.put(propMainVo.getSerialNo(),defMap.get(propMainVo.getSerialNo()).add(otherLoss));
					}else{
						defMap.put(propMainVo.getSerialNo(),otherLoss);
					}
				}
			}
		}

		for (Integer key : defMap.keySet()) {
			if(defMap.get(key).compareTo(new BigDecimal("2000"))==1){
				if(key==1){
					return "标的车的车财损失之和超过2000，不符合互碰自赔条件！";
				}else{
					return "三者车"+licenseNoMap.get(key)+"的车财损失之和超过2000，不符合互碰自赔条件！";
				}

			}
		}

		return selfStr;
	}

	/**
	 * 互碰自赔案件 三者车校验
	 * @param lossCarMainVo
	 * @return
	 * @modified: ☆YangKun(2016年7月15日 上午10:29:35): <br>
	 */
	@Override
	public String thirdCarClaimSelf(PrpLDlossCarMainVo lossCarMainVo){
		String selfStr = "";

		BigDecimal thisCarSumPaid = BigDecimal.ZERO;
		thisCarSumPaid = thisCarSumPaid.add(NullToZero(lossCarMainVo.getSumLossFee()));
		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(lossCarMainVo.getRegistNo());
		if(lossCarMainList!=null && !lossCarMainList.isEmpty()){
			for(PrpLDlossCarMainVo otherLossCar : lossCarMainList){
				if(otherLossCar.getId().equals(lossCarMainVo.getId())){
					continue;
				}

				if(otherLossCar.getSerialNo().equals(lossCarMainVo.getSerialNo())){
					thisCarSumPaid = thisCarSumPaid.add(otherLossCar.getSumLossFee()).add(otherLossCar.getSumRescueFee());
				}
			}
		}

		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(lossCarMainVo.getRegistNo());
		if(lossPropMainList!=null && !lossPropMainList.isEmpty()){
			for(PrpLdlossPropMainVo propMainVo : lossPropMainList){
				if(propMainVo.getSerialNo().equals(lossCarMainVo.getSerialNo())){
					thisCarSumPaid = thisCarSumPaid.add(NullToZero(propMainVo.getSumDefloss()))
							.add(NullToZero(propMainVo.getDefRescueFee()));
				}
			}
		}

		if(thisCarSumPaid.compareTo(new BigDecimal("2000"))==1){
			return "该车辆的车财损失之和超过2000，不符合互碰自赔条件！";
		}

		return selfStr;
	}

	/**
	 * 核价保存 ☆yangkun(2016年2月16日 下午3:39:16): <br>
	 */
	@Override
	public void saveVerifyPrice(PrpLDlossCarMainVo lossCarMainVo, PrpLClaimTextVo claimTextVo, PrpLWfTaskVo taskVo,SysUserVo userVo) {
		// 保存定损主表
		lossCarMainVo.setUpdateTime(new Date());
		lossCarMainVo.setUpdateUser(userVo.getUserCode());
		String opinionCode = claimTextVo.getOpinionCode();
		if(StringUtils.isNotBlank(opinionCode)){
			if(!"a".equals(opinionCode) && CodeConstants.AuditStatus.SUBMITVPRICE.equals(lossCarMainVo.getAuditStatus())){
				lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.SUBMITVPRICENO);
			}
		}

		lossCarMainVo.setVeripCom(userVo.getComCode());
		lossCarMainVo.setVeripHandlerCode(userVo.getUserCode());
		lossCarMainVo.setVeripHandlerName(userVo.getUserName());
		lossCarMainVo.setVeripIdNo(userVo.getIdentifyNumber());
		deflossService.saveOrUpdateDefloss(lossCarMainVo, taskVo.getSubNodeCode());

		// 意见
		claimTextVo.setNodeCode(taskVo.getSubNodeCode());
		this.saveClaimText(lossCarMainVo, claimTextVo, userVo);
	}

	/**
	 * 核损保存 ☆yangkun(2016年2月16日 下午3:40:12): <br>
	 */
	@Override
	public void saveVerifyLoss(PrpLDlossCarMainVo lossCarMainVo, List<PrpLDlossChargeVo> lossChargeVos,
			PrpLClaimTextVo claimTextVo, PrpLWfTaskVo taskVo, SysUserVo userVo) {
		String currentNode = taskVo.getSubNodeCode();
		// 保存定损主表
		lossCarMainVo.setUpdateTime(new Date());
		lossCarMainVo.setUpdateUser(userVo.getUserCode());

		lossCarMainVo.setUnderWriteCom(userVo.getComCode());
		lossCarMainVo.setUnderWriteCode(userVo.getUserCode());;
		lossCarMainVo.setUnderWriteName(userVo.getUserName());
		lossCarMainVo.setUnderWiteIdNo(userVo.getIdentifyNumber());
		// 无损失 核损的残值 施救费 核损总金额赋值为0
		if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(lossCarMainVo.getCetainLossType())){
			lossCarMainVo.setSumVeriCompFee(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriMatFee(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriRepairFee(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriOutFee(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriRemnant(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriLossFee(BigDecimal.ZERO);
			lossCarMainVo.setSumVeriRescueFee(BigDecimal.ZERO);
		}
		
		String registNo = lossCarMainVo.getRegistNo();
		List<PrpLDlossCarInfoVo> infoVo = null;
		if(StringUtils.isNotBlank(registNo)){
			infoVo = lossCarService.findPrpLDlossCarInfoVoListByRegistNo( registNo);
		}
		if(infoVo != null && infoVo.size() > 0){
			for(PrpLDlossCarInfoVo vo : infoVo){
				if(StringUtils.isNotBlank(lossCarMainVo.getUnderWiteIdNo())){
					if(StringUtils.isNotBlank(vo.getIdentifyType()) && ("1".equals(vo.getIdentifyType()) || "3".equals(vo.getIdentifyType())) 
							&& StringUtils.isNotBlank(vo.getIdentifyNo()) && vo.getIdentifyNo().equals(lossCarMainVo.getUnderWiteIdNo())){
						throw new IllegalArgumentException("核损人身份证号不能与驾驶员身份证号相同，请修改！！！");
					}
					if(StringUtils.isNotBlank(vo.getIdentifyType()) && ("1".equals(vo.getIdentifyType()) || "3".equals(vo.getIdentifyType())) 
							&& StringUtils.isNotBlank(vo.getDrivingLicenseNo()) && vo.getDrivingLicenseNo().equals(lossCarMainVo.getUnderWiteIdNo())){
						throw new IllegalArgumentException("核损人身份证号不能与驾驶证号相同，请修改！！！");
					}
					
				}
			}
		}
		
		deflossService.saveOrUpdateDefloss(lossCarMainVo,currentNode);

		// 意见
		claimTextVo.setNodeCode(currentNode);
		this.saveClaimText(lossCarMainVo, claimTextVo, userVo);
		if(lossChargeVos!=null && lossChargeVos.size()>0){
			lossChargeService.saveOrUpdte(lossChargeVos);
		}
	}

	private void saveClaimText(PrpLDlossCarMainVo lossCarMain,PrpLClaimTextVo claimTextVo,SysUserVo userVo){
		String nodeCode = claimTextVo.getNodeCode();
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(lossCarMain.getId());// 取最新的数据
		PrpLClaimTextVo claimText = claimTextService.findClaimTextByNode(lossCarMainVo.getId(),nodeCode,"0");
		if(claimText!=null){
			Beans.copy().from(claimText).excludes("description").to(claimTextVo);
			claimTextVo.setInputTime(new Date());
			claimTextVo.setUpdateTime(new Date());
			claimTextVo.setUpdateUser(userVo.getUserCode());
		}else{
			claimTextVo.setBussTaskId(lossCarMainVo.getId());
			claimTextVo.setRegistNo(lossCarMainVo.getRegistNo());
			claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);
//			claimTextVo.setNodeCode(nodeCode);
			claimTextVo.setBigNode(FlowNode.DLCar.name());
			claimTextVo.setOperatorCode(userVo.getUserCode());
			claimTextVo.setOperatorName(userVo.getUserName());
			claimTextVo.setComCode(userVo.getComCode());
//			claimTextVo.setComName(userVo.getComName());
			claimTextVo.setInputTime(new Date());
			claimTextVo.setCreateTime(new Date());
			claimTextVo.setCreateUser(userVo.getUserCode());
		}

		BigDecimal sumMaterFee = new BigDecimal("0");
		BigDecimal otherFee = new BigDecimal("0");
		BigDecimal sumLossFee = new BigDecimal("0");
		// 配件 + 辅料 是材料金额
		if(FlowNode.DLoss == FlowNode.valueOf(nodeCode).getRootNode()){
			if(lossCarMainVo.getSumLossFee()!=null){
				otherFee = NullToZero(lossCarMainVo.getSumRepairFee()).add(NullToZero(lossCarMainVo.getSumOutFee()))
						.add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
				sumMaterFee = lossCarMainVo.getSumLossFee().subtract(otherFee).add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
				sumLossFee = NullToZero(lossCarMainVo.getSumLossFee()).add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}
		}else if(nodeCode.startsWith(FlowNode.VPCar.name())){
			// 不进入精友
			if(lossCarMainVo.getSumVeripLoss() ==null || CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){
				otherFee = NullToZero(lossCarMainVo.getSumRepairFee()).add(NullToZero(lossCarMainVo.getSumOutFee()))
						.add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
				sumMaterFee = NullToZero(lossCarMainVo.getSumCompFee()).add(NullToZero(lossCarMainVo.getSumMatFee()));
				sumLossFee = lossCarMainVo.getSumLossFee().add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
			}else{
				otherFee = NullToZero(lossCarMainVo.getSumRepairFee()).add(NullToZero(lossCarMainVo.getSumOutFee()))
						.add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
				sumMaterFee = NullToZero(lossCarMainVo.getSumVeripLoss());
				sumLossFee = sumMaterFee.add(otherFee);
			}
		}else {
			// 不进入精友 或核损金额为空
			if(lossCarMainVo.getSumVeriLossFee() == null ||
					((CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
					||	CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))
					&& CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag()))){
				otherFee = NullToZero(lossCarMainVo.getSumRepairFee()).add(NullToZero(lossCarMainVo.getSumOutFee()))
						.add(NullToZero(lossCarMainVo.getSumSubRiskFee()));
				sumMaterFee = NullToZero(lossCarMainVo.getSumVeripLoss());// 可能没有配件和辅料金额
				sumLossFee = sumMaterFee.add(otherFee);
			}else{
				sumLossFee = NullToZero(lossCarMainVo.getSumVeriLossFee()).add(NullToZero(lossCarMainVo.getSumVeriSubRiskFee()));
				otherFee = NullToZero(lossCarMainVo.getSumVeriRepairFee()).add(NullToZero(lossCarMainVo.getSumVeriOutFee()))
						.add(NullToZero(lossCarMainVo.getSumVeriSubRiskFee()));
				sumMaterFee = sumLossFee.subtract(otherFee);
			}
		}
		claimTextVo.setSumLossFee(sumLossFee);
		claimTextVo.setSumMaterFee(sumMaterFee);
		claimTextVo.setSumOtherfee(otherFee);
		if(nodeCode.equals(FlowNode.VPCar_LV0.name()) || nodeCode.equals(FlowNode.VLCar_LV0.name())){
			claimTextVo.setFlag("1");
		}else{
			claimTextVo.setStatus(CodeConstants.AuditStatus.SAVE);
			claimTextVo.setFlag("0");// 未提交都传0，1 表示已处理完该节点
		}

		claimTextService.saveOrUpdte(claimTextVo);
	}

	private static BigDecimal NullToZero(BigDecimal strNum) {
		if(strNum==null){
			return new BigDecimal("0");
		}
		return strNum;
	}

	/**
	 * 提交工作流
	 *
	 * <pre></pre>
	 * @param nextVo
	 * @return
	 * @throws Exception
	 * @modified: ☆yangkun(2016年1月16日 下午8:28:34): <br>
	 */
	@Override
	public List<PrpLWfTaskVo> submitNextNode(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception {

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
		String currentNode = nextVo.getCurrentNode();
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("该任务已提交，不能重复提交");
		}
		String auditStatus = lossCarMainVo.getAuditStatus();
//		if((FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.VPCar.name())||
//				FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.VLCar.name())) &&
//				CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus())){
//
//			String nextNode = nextVo.getFinalNextNode();
//			if(nextNode.indexOf("_")!=-1){
//				String[] array =nextVo.getFinalNextNode().split("_");
//				if(Integer.parseInt(array[1].substring(2))>=9){
//					boolean havUser =assignService.existsGradeUser(FlowNode.valueOf(nextVo.getFinalNextNode()), nextVo.getComCode());
//					if(!havUser){
		// throw new IllegalArgumentException("该级别没有配置人员,请提交到其他级别！");
//					}
//				}
//			}
//		}

		// 保存业务数据 和 指标数据 轨迹表数据
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(lossCarMainVo.getId(),nextVo.getCurrentNode(),"0");
		lossCarMainVo.setLossCarInfoVo(carInfoVo);

		// 能够审核通过的级别
		if(!StringUtils.isBlank(nextVo.getVerifyLevel())){
			lossCarMainVo.setVerifyLevel(Integer.valueOf(nextVo.getVerifyLevel()));
		}
		// 分公司最高级别
		if(!StringUtils.isBlank(nextVo.getMaxLevel())){
			lossCarMainVo.setMaxLevel(Integer.valueOf(nextVo.getMaxLevel()));
		}

		List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();
		if(FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.DLoss.name()) ||
FlowNode.valueOf(currentNode).getUpperNode()
				.equals(FlowNode.DLCar.name())){// 定损或复检提交
			wfTaskVoList = defSubmitNext(lossCarMainVo,nextVo);
		}else if(FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.VPCar.name())){// 核价
			wfTaskVoList = veriPriceSubmitNext(lossCarMainVo,nextVo);

		}else{// 核损
			wfTaskVoList = verLossSubmitNext(lossCarMainVo,nextVo);
			if(claimTextVo!=null){
				claimTextVo.setSumLossFee(NullToZero(lossCarMainVo.getSumVeriLossFee()).add(NullToZero(lossCarMainVo.getSumVeriSubRiskFee())));
				claimTextVo.setSumMaterFee(NullToZero(lossCarMainVo.getSumVeriCompFee()).add(NullToZero(lossCarMainVo.getSumVeriMatFee())));
				claimTextVo.setSumOtherfee(NullToZero(lossCarMainVo.getSumVeriRepairFee()).add(NullToZero(lossCarMainVo.getSumVeriOutFee())).add(NullToZero(lossCarMainVo.getSumVeriSubRiskFee())));
			}
		}

		// 回写意见表数据
		if(!FlowNode.ChkRe.name().equals(nextVo.getNextNode()) && claimTextVo!=null){
			claimTextVo.setFlag("1");
			claimTextVo.setStatus(auditStatus);
			claimTextService.saveOrUpdte(claimTextVo);
		}
		// 核损通过后 写入PRPLASSESSOR
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus()) || "1".equals(nextVo.getAutoLossFlag())){
			if("1".equals(lossCarMainVo.getIntermFlag())){// 公估定损
				// 判断是否有数据
				PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId(lossCarMainVo.getRegistNo(), CodeConstants.TaskType.CAR,lossCarMainVo.getSerialNo(), lossCarMainVo.getIntermCode());
				if(assessorOld != null &&  CodeConstants.AssessorUnderWriteFlag.Loss.equals(assessorOld.getUnderWriteFlag())){
					assessorOld.setUnderWriteFlag(CodeConstants.AssessorUnderWriteFlag.Verify);
					assessorOld.setLossDate(lossCarMainVo.getUnderWriteEndDate());

					String kindCode = "";
					if(StringUtils.isNotBlank(lossCarMainVo.getLossFeeType())){
						if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode()) != null &&
								CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossCarMainVo.getRiskCode())){
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ.equals(lossCarMainVo.getLossFeeType()) ? lossCarMainVo.getLossFeeType() : CodeConstants.LossFee2020Kind_Map
									.get(lossCarMainVo.getRiskCode()+lossCarMainVo.getLossFeeType());
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ.equals(lossCarMainVo.getLossFeeType()) ? lossCarMainVo.getLossFeeType() : CodeConstants.LossFeeKind_Map
									.get(lossCarMainVo.getLossFeeType());
						}
						
					}else{
						PrpLRegistVo registVo = registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
						if(CodeConstants.ReportType.BI.equals(registVo.getReportType())){
							kindCode = CodeConstants.KINDCODE.KINDCODE_B;
						}else{
							kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
						}
					}
					assessorOld.setAssessorFee(lossCarMainVo.getAssessorFee());
					assessorOld.setVeriLoss(lossCarMainVo.getSumVeriLossFee());
					assessorOld.setKindCode(kindCode);
					assessorService.saveOrUpdatePrpLAssessor(assessorOld,nextVo.getUserVo());

				}
			}
			if(StringUtils.isNotBlank(lossCarMainVo.getCheckCode())){
				acheckTaskService.addCheckFeeTaskOfDcar(lossCarMainVo,nextVo.getUserVo(),"1");
			}
			// 保存核通过时 轨迹表
			checkTaskService.saveCheckDutyHis(lossCarMainVo.getRegistNo(),"车辆核损提交");
		}
		// 回写风险提示信息表
		registRiskInfoService.writeRiskInfoByLossCar(lossCarMainVo.getRegistNo(), nextVo.getUserVo().getUserCode());

		return wfTaskVoList;

	}

	/**
	 * lossCarMainVo 定损，核价或者核损 流程结束到下一个节点 精友标志flag重新置为0 核损提交上级 ☆yangkun(2016年1月26日 上午11:49:26): <br>
	 * @throws Exception
	 */
	private List<PrpLWfTaskVo> verLossSubmitNext(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception {
		logger.info("案件号registno={},进入车辆核损提交上级UnderWriteFlag方法。",lossCarMainVo.getRegistNo());
		nextVo.setNextNode(nextVo.getFinalNextNode());
		if("1".equals(nextVo.getEndFlag())){// 案件结束
			nextVo.setNextNode(FlowNode.END.name());
		}else{
			nextVo.setNextNode(nextVo.getFinalNextNode());
		}
		if(FlowNode.ChkRe.name().equals(nextVo.getNextNode())){
			lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.RECHECK);
		}else{
			if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())||
				CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType())){

				this.organizeVeriLoss(lossCarMainVo,nextVo);
			}
		}
		logger.info("案件号registno={},根据审核状态回写核损标志位，审核状态AuditStatus={}",lossCarMainVo.getRegistNo(),lossCarMainVo.getAuditStatus());
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.PASS);
			lossCarMainVo.setUnderWriteEndDate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}else if(CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.BACK);
			lossCarMainVo.setUnderWriteEndDate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}else if(CodeConstants.AuditStatus.RELOSS.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.RELOSS);
			lossCarMainVo.setUnderWriteEndDate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}

		this.updateDeflossData(lossCarMainVo,nextVo);
		logger.info("案件号registno={},结束车辆核损提交上级方法，回写标志位UnderWriteFlag ={}",lossCarMainVo.getRegistNo(),lossCarMainVo.getUnderWriteFlag());
		// 提交工作流
		return  submitNextTask(lossCarMainVo,nextVo); 
	}

	/**
	 * 核价提交 ☆yangkun(2016年1月26日 上午11:49:26): <br>
	 * @throws Exception
	 */
	private List<PrpLWfTaskVo> veriPriceSubmitNext(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception {

		List<PrpLWfTaskVo> wfTaskList = new ArrayList<PrpLWfTaskVo>();
		if("1".equals(nextVo.getAutoLossFlag())){// 自动核损
			nextVo.setNextNode(FlowNode.VLCar_LV0.name());
		}else{
			nextVo.setNextNode(nextVo.getFinalNextNode());
		}
		// 核价提交核损
		this.organizeVeriPriceLoss(lossCarMainVo,nextVo);
		if(CodeConstants.AuditStatus.SUBMITVPRICE.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.PASS);
			lossCarMainVo.setVeripEnddate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}else if(CodeConstants.AuditStatus.SUBMITVPRICENO.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.NOSUBMIT);
			lossCarMainVo.setVeripEnddate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}else if(CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())){
			lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.BACK);
			lossCarMainVo.setVeripEnddate(new Date());
			lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		}

		wfTaskList = submitNextTask(lossCarMainVo,nextVo);
		this.updateDeflossData(lossCarMainVo,nextVo);

		// 自动核损 提交到end节点
		if("1".equals(nextVo.getAutoLossFlag())){
			String currentTaskId = wfTaskList.get(0).getTaskId().toString();
			wfTaskList = autoVerLossSubmitNext(lossCarMainVo,nextVo,currentTaskId,"2");
		}

		return wfTaskList;
	}

	/**
	 * 不进入精友 核价自动赋值 并且不是提交下级 上一个节点不是核价节点 ☆yangkun(2016年2月16日 下午3:13:52): <br>
	 * @throws Exception
	 */
	private void organizeVeriPriceLoss(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception{
		String jyUrl = nextVo.getJyUrl();
		BigDecimal deflossLoss = NullToZero(lossCarMainVo.getSumCompFee())
								.add(NullToZero(lossCarMainVo.getSumMatFee()))
								.subtract(NullToZero(lossCarMainVo.getSumRemnant()));
//		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
		Boolean priceFlag = false;
		// 自动核价 或者 未进入精友 同意核价
		if(FlowNode.VPCar_LV0.name().equals(nextVo.getCurrentNode()) ||
			((CodeConstants.AuditStatus.SUBMITVPRICE.equals(lossCarMainVo.getAuditStatus())
				|| CodeConstants.AuditStatus.SUBMITVPRICENO.equals(lossCarMainVo.getAuditStatus()))
			 && CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag()))){

			priceFlag = true;
		}
		// 定损修改，且没有操作赔款和费用，则核价金额不变
		if((nextVo.getNotModPrice()!= null && nextVo.getNotModPrice())||"06".equals(lossCarMainVo.getCetainLossType())){
			priceFlag = false;
		}

		if((lossCarMainVo.getSumVeripLoss()==null || priceFlag) && !"06".equals(lossCarMainVo.getCetainLossType())){
			// 核价与定损保持一致
			for(PrpLDlossCarCompVo compVo: lossCarMainVo.getPrpLDlossCarComps()){
				compVo.setChgLocPrice(compVo.getChgRefPrice());// 本地价格用系统厂方价格赋值
				compVo.setVeripMaterFee(compVo.getMaterialFee());
				compVo.setAuditCount(new BigDecimal(compVo.getQuantity()));
				compVo.setVeripRestFee(compVo.getRestFee());
				compVo.setSumCheckLoss(compVo.getSumDefLoss());
			}

			for(PrpLDlossCarMaterialVo materialVo: lossCarMainVo.getPrpLDlossCarMaterials()){
				materialVo.setAuditPrice(materialVo.getUnitPrice());
				materialVo.setAuditCount(materialVo.getAssisCount());
				materialVo.setAuditMaterialFee(materialVo.getMaterialFee());
			}
			lossCarMainVo.setSumVeripCompFee(lossCarMainVo.getSumCompFee());
			lossCarMainVo.setSumVeripMatFee(lossCarMainVo.getSumMatFee());
			lossCarMainVo.setSumVeripRemnant(lossCarMainVo.getSumRemnant());
			lossCarMainVo.setSumVeripLoss(deflossLoss);
			if(CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus())){
				lossCarMainVo.setFlag(CodeConstants.JyFlag.AUTO);// 精友标志位置为2
			}
		}

		// 自动核价 或者 未进入精友 同意核价
		if(priceFlag){
			PrpLRegistVo registVo = registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
			String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
			Date timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
			if(registVo.getReportTime().getTime()<timeStamp.getTime()){// 如果报案日期小于时间戳则请求精友一代
				fittingInterService.agreeToJy("verifyPrice",lossCarMainVo.getRegistNo(),lossCarMainVo.getId().toString(),jyUrl);
			}
		}
	}

	/**
	 * 修复定损和代协议定损 不进入精友 核损自动赋值 (核损金额为空或者核损金额比核价后定损金额大(退回定损的情况)的赋值) ☆yangkun(2016年2月16日 下午3:13:52): <br>
	 * @throws Exception
	 */
	private void organizeVeriLoss(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception{
		logger.info("organizeVeriLoss begin, registNo: "+lossCarMainVo.getRegistNo()+" ,sumVeriLossFee: "+lossCarMainVo.getSumVeriLossFee());
		String jyUrl = nextVo.getJyUrl();
		BigDecimal deflossLoss = NullToZero(lossCarMainVo.getSumVeripLoss())
				.add(NullToZero(lossCarMainVo.getSumRepairFee()))
				.add(NullToZero(lossCarMainVo.getSumOutFee()));

		Boolean veriLossFlag = false;
		// 发送精友接口 自动核损或者未进入精友 核损同意
		if(FlowNode.VLCar_LV0.name().equals(nextVo.getCurrentNode()) ||
			(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus())
			 && CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag()))){

			veriLossFlag = true;
		}

		if(lossCarMainVo.getSumVeriLossFee()==null || veriLossFlag ){
			// 核价与核损保持一致 如果配件和辅料的金额为0 则走核损
			if(DataUtils.NullToZero(lossCarMainVo.getSumVeripCompFee()).compareTo(BigDecimal.ZERO)==1
				|| DataUtils.NullToZero(lossCarMainVo.getSumVeripMatFee()).compareTo(BigDecimal.ZERO)==1){
				for(PrpLDlossCarCompVo compVo: lossCarMainVo.getPrpLDlossCarComps()){
					if(compVo.getVeriMaterFee()==null){compVo.setVeriMaterFee(compVo.getVeripMaterFee());}
					if(compVo.getAuditCount() != null){
						if(compVo.getVeriQuantity()==null){compVo.setVeriQuantity(compVo.getAuditCount().intValue());}
					}else{
						throw new Exception("核价数据出错，请重新核价！");
					}
					if(compVo.getVeriRestFee()==null){compVo.setVeriRestFee(compVo.getVeripRestFee());}
					if(compVo.getSumVeriLoss()==null){compVo.setSumVeriLoss(compVo.getSumCheckLoss());}
				}

				for(PrpLDlossCarMaterialVo materialVo: lossCarMainVo.getPrpLDlossCarMaterials()){
					if(materialVo.getAuditLossPrice()==null){materialVo.setAuditLossPrice(materialVo.getAuditPrice());}
					if(materialVo.getAuditLossCount()==null){materialVo.setAuditLossCount(materialVo.getAuditCount());}
					if(materialVo.getAuditLossMaterialFee()==null){materialVo.setAuditLossMaterialFee(materialVo.getAuditMaterialFee());}
				}
			}else{
				for(PrpLDlossCarCompVo compVo: lossCarMainVo.getPrpLDlossCarComps()){
					if(compVo.getVeriMaterFee()==null){compVo.setVeriMaterFee(compVo.getMaterialFee());}
					if(compVo.getQuantity() != null){
						if(compVo.getVeriQuantity()==null){compVo.setVeriQuantity(compVo.getQuantity().intValue());}
					}else{
						throw new Exception("定损数据出错，请退回定损！");
					}
					if(compVo.getVeriRestFee()==null){compVo.setVeriRestFee(compVo.getRestFee());}
					if(compVo.getSumVeriLoss()==null){compVo.setSumVeriLoss(compVo.getSumDefLoss());}
				}

				for(PrpLDlossCarMaterialVo materialVo: lossCarMainVo.getPrpLDlossCarMaterials()){
					if(materialVo.getAuditLossPrice()==null){materialVo.setAuditLossPrice(materialVo.getUnitPrice());}
					if(materialVo.getAuditLossCount()==null){materialVo.setAuditLossCount(materialVo.getAssisCount());}
					if(materialVo.getAuditLossMaterialFee()==null){materialVo.setAuditLossMaterialFee(materialVo.getMaterialFee());}
				}
			}

			for(PrpLDlossCarRepairVo repairVo:lossCarMainVo.getPrpLDlossCarRepairs()){
				if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairVo.getRepairFlag())){
					if(repairVo.getVeriManHour()==null){repairVo.setVeriManHour(repairVo.getManHour());}
					if(repairVo.getVeriManUnitPrice()==null){repairVo.setVeriManUnitPrice(repairVo.getManHourUnitPrice());}
					if(repairVo.getSumVeriLoss()==null){repairVo.setSumVeriLoss(repairVo.getSumDefLoss());}
					if(repairVo.getVeriManHourFee()==null){repairVo.setVeriManHourFee(repairVo.getManHourFee());}
				}else{
					if(repairVo.getSumVeriLoss()==null){repairVo.setSumVeriLoss(repairVo.getSumDefLoss());}
				}
			}

			if(lossCarMainVo.getSumVeriCompFee()==null){lossCarMainVo.setSumVeriCompFee(lossCarMainVo.getSumVeripCompFee());}
			if(lossCarMainVo.getSumVeriMatFee()==null){lossCarMainVo.setSumVeriMatFee(lossCarMainVo.getSumVeripMatFee());}
			if(lossCarMainVo.getSumVeriRepairFee()==null){lossCarMainVo.setSumVeriRepairFee(lossCarMainVo.getSumRepairFee());}
			if(lossCarMainVo.getSumVeriOutFee()==null){lossCarMainVo.setSumVeriOutFee(lossCarMainVo.getSumOutFee());}
			if(lossCarMainVo.getSumVeriRemnant()==null){lossCarMainVo.setSumVeriRemnant(lossCarMainVo.getSumVeripRemnant());}
			if(lossCarMainVo.getSumVeriLossFee()==null){
				lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumVeriCompFee())
						.add(NullToZero(lossCarMainVo.getSumVeriMatFee()))
						.add(NullToZero(lossCarMainVo.getSumVeriRepairFee()))
						.add(NullToZero(lossCarMainVo.getSumVeriOutFee()))
						.subtract(NullToZero(lossCarMainVo.getSumVeriRemnant())));
			}
			if(CodeConstants.AuditStatus.AUDIT.equals(lossCarMainVo.getAuditStatus())){
				lossCarMainVo.setFlag(CodeConstants.JyFlag.AUTO);// 精友标志位置为2
			}
		}

		if(veriLossFlag){
			PrpLRegistVo registVo = registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
			String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
			Date timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
			if(registVo.getReportTime().getTime()<timeStamp.getTime()){// 如果报案日期小于时间戳则请求精友一代
				fittingInterService.agreeToJy("verifyLoss",lossCarMainVo.getRegistNo(),lossCarMainVo.getId().toString(),jyUrl);
			}
		}
		logger.info("organizeVeriLoss end, registNo: "+lossCarMainVo.getRegistNo()+" ,sumVeriLossFee: "+lossCarMainVo.getSumVeriLossFee());
		// //核损 赋值 涉水险金额
// 		BigDecimal sumWadFee = BigDecimal.ZERO;
//		for(PrpLDlossCarCompVo compVo: lossCarMainVo.getPrpLDlossCarComps()){
//			if("1".equals(compVo.getWadFlag())){
//			//  	sumWadFee = compVo
//			}
//		}
	}

	/**
	 * 推定全损和盗抢折旧 自动核损组织数据
	 * @param lossCarMainVo
	 * @param flowTaskId
	 * @throws Exception
	 * @modified: ☆YangKun(2016年4月18日 上午11:28:33): <br>
	 */
	private void organizeALlLossVeriLoss(PrpLDlossCarMainVo lossCarMainVo,String flowTaskId) throws Exception{
		if(CodeConstants.CetainLossType.DEFLOSS_NULL.equals(lossCarMainVo.getCetainLossType())){
			lossCarMainVo.setSumVeriCompFee(lossCarMainVo.getSumVeripCompFee());
			lossCarMainVo.setSumVeriMatFee(lossCarMainVo.getSumVeripMatFee());
			lossCarMainVo.setSumVeriRepairFee(lossCarMainVo.getSumRepairFee());
			lossCarMainVo.setSumVeriOutFee(lossCarMainVo.getSumOutFee());
			lossCarMainVo.setSumVeriRemnant(lossCarMainVo.getSumVeripRemnant());
			lossCarMainVo.setSumVeriLossFee(lossCarMainVo.getSumLossFee());
			lossCarMainVo.setSumVeriRescueFee(lossCarMainVo.getSumRescueFee());
		}else{
			lossCarMainVo.setSumVeriLossFee(lossCarMainVo.getSumLossFee());
			lossCarMainVo.setSumVeriRemnant(lossCarMainVo.getSumRemnant());
			lossCarMainVo.setVeriActualValue(lossCarMainVo.getActualValue());
			lossCarMainVo.setVeriOtherFee(lossCarMainVo.getOtherFee());
			lossCarMainVo.setSumVeriRescueFee(lossCarMainVo.getSumRescueFee());
		}
	}

	/**
	 * 定损提交 ☆yangkun(2016年1月25日 下午9:36:59): <br>
	 * @throws Exception
	 */
	private List<PrpLWfTaskVo> defSubmitNext(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) throws Exception{
		List <PrpLWfTaskVo> wfTaskList = new ArrayList<PrpLWfTaskVo>();
		lossCarMainVo.setDefEndDate(new Date());
		lossCarMainVo.setFlag(CodeConstants.JyFlag.NOIN);// 重置进入精友标识
		if(lossCarMainVo.getSumLossFee()==null){
			lossCarMainVo.setSumLossFee(new BigDecimal("0"));
		}
		logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,nextVo.autoPriceFlag: "+nextVo.getAutoPriceFlag()+" ,nextVo.autoLossFlag: "+nextVo
				.getAutoLossFlag());
		//查勘费任务的生成
		acheckTaskService.addCheckFeeTaskOfDcar(lossCarMainVo, nextVo.getUserVo(), "0");
		if("1".equals(nextVo.getAutoPriceFlag())){
			// 定损提交自动核价
			nextVo.setNextNode(FlowNode.VPCar_LV0.name());
			wfTaskList = submitNextTask(lossCarMainVo,nextVo);
			this.updateDeflossData(lossCarMainVo,nextVo);

			// 自动核价节点提交核损节点
			if("1".equals(nextVo.getAutoLossFlag())){
				nextVo.setNextNode(FlowNode.VLCar_LV0.name());
			}else{
				nextVo.setNextNode(nextVo.getFinalNextNode());
			}
			wfTaskList = autoPriceSubmitNext(lossCarMainVo,nextVo,wfTaskList.get(0).getTaskId().toString());

			// 自动核损提交下一个节点
			if("1".equals(nextVo.getAutoLossFlag())){
				wfTaskList = autoVerLossSubmitNext(lossCarMainVo,nextVo,wfTaskList.get(0).getTaskId().toString(),"3");
			}else if("2".equals(nextVo.getAutoLossFlag())){// 2表示自动核价，核损不自动
			  /*  nextVo.setCurrentNode(FlowNode.VPCar_LV0.name());
			    veriPriceSubmitNext(lossCarMainVo,nextVo);*/
			}
		}else{
			// 自动核损提交下一个节点
			if("1".equals(nextVo.getAutoLossFlag())){
				nextVo.setNextNode(FlowNode.VLCar_LV0.name());
				// 定损提交到自动核损节点
				logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,定损提交到自动核损节点开始");
				wfTaskList = submitNextTask(lossCarMainVo,nextVo);
				this.updateDeflossData(lossCarMainVo,nextVo);
				logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,定损提交到自动核损节点结束 ，自动核损提交到end节点开始");
				String currentTaskId = wfTaskList.get(0).getTaskId().toString();
				// 自动核损提交到end节点
				wfTaskList = autoVerLossSubmitNext(lossCarMainVo,nextVo,currentTaskId,"1");
				logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,自动核损提交到end节点结束");
			}else{
				nextVo.setNextNode(nextVo.getFinalNextNode());
				// 定损提交到核价节点
				wfTaskList = submitNextTask(lossCarMainVo,nextVo);
				if(FlowNode.ChkRe.name().equals(nextVo.getNextNode())){
					lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.RECHECK);
				}
				this.updateDeflossData(lossCarMainVo,nextVo);
			}

		}

		return  wfTaskList;
	}

	/**
	 * 保存定损主表 保存轨迹表 更新指标表 ☆yangkun(2016年1月27日 下午5:56:32): <br>
	 */
	private void updateDeflossData(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo){
		// 保存定损业务表
		//lossCarMainVo.setFlag("0");
		deflossService.updateDefloss(lossCarMainVo);
		if(!FlowNode.ChkRe.name().equals(nextVo.getNextNode())){
			// 定损提交保存指标数据
			this.saveLossIndex(lossCarMainVo,nextVo);
			// 保存轨迹轨迹
			deflossService.saveDeflossHis(lossCarMainVo,nextVo.getCurrentNode());
		}
		lossChargeService.saveChargeHis(lossCarMainVo.getId(), FlowNode.DLCar.name());
	}


	/**
	 * 自动核价提交 ☆yangkun(2016年1月26日 上午9:52:21): <br>
	 * @throws Exception
	 */
	private List<PrpLWfTaskVo> autoPriceSubmitNext(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo,String flowTaskId) throws Exception {

		lossCarMainVo.setVeriPriceFlag(CodeConstants.VeriFlag.PASS);
		lossCarMainVo.setVeripEnddate(new Date());
		lossCarMainVo.setVeripHandlerCode("AUTO");
//		lossCarMainVo.setNodeCode(FlowNode.VPCar_LV0.name());
		lossCarMainVo.setVeripHandlerName("AUTO");
//		lossCarMainVo.setVeripIdNo("511702197409284963");
		lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.SUBMITLOSS);
		nextVo.setCurrentNode(FlowNode.VPCar_LV0.name());
		// 组织核价金额
		this.organizeVeriPriceLoss(lossCarMainVo,nextVo);
		nextVo.setAuditStatus(CodeConstants.VeriFlag.PASS);
		nextVo.setFlowTaskId(flowTaskId);
		SysUserVo userVo = new SysUserVo();
		userVo.setUserCode(lossCarMainVo.getVeripHandlerCode());
		userVo.setUserName(lossCarMainVo.getVeripHandlerName());
		userVo.setComCode(lossCarMainVo.getVeripCom());
		nextVo.setUserVo(userVo);
		nextVo.setTaskInUser("AUTO");
		List<PrpLWfTaskVo> wfTaskList = submitNextTask(lossCarMainVo,nextVo);
		this.updateDeflossData(lossCarMainVo,nextVo);

		// 保存自动核价意见
		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		claimTextVo.setDescription("自动核价");
		claimTextVo.setStatus(CodeConstants.AuditStatus.SUBMITVPRICE);
		claimTextVo.setNodeCode(FlowNode.VPCar_LV0.name());
		this.saveClaimText(lossCarMainVo, claimTextVo, userVo);

		return wfTaskList;

	}

	/**
	 * 自动核损提交 ☆yangkun(2016年1月26日 上午9:52:21): <br>
	 * 自动核损人员 信息TODO
	 * @param flag 1-定损-》自动核损 2-定损-》核价-》自动核损 3-定损-》自动核价-》自动核损
	 * @throws Exception
	 */
	private List<PrpLWfTaskVo> autoVerLossSubmitNext(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo,String flowTaskId,String flag) throws Exception {
		logger.info("报案号registno = {},进入车辆自动核损回写UnderWriteFlag提交方法",lossCarMainVo.getRegistNo());
		lossCarMainVo.setUnderWriteFlag(CodeConstants.VeriFlag.PASS);
		lossCarMainVo.setUnderWriteEndDate(new Date());
		lossCarMainVo.setUnderWriteCode("AUTO");
//		lossCarMainVo.setNodeCode(FlowNode.VLCar_LV0.name());
		lossCarMainVo.setUnderWriteName("AUTO");
//		lossCarMainVo.setUnderWiteIdNo("511702197409284963");
		lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.SUBMITVLOSS);
		nextVo.setCurrentNode(FlowNode.VLCar_LV0.name());
		// TODO 组织核价核损金额的代码有待整理啊！
		logger.info("registNo: "+lossCarMainVo.getRegistNo()+" ,nextVo.getNotModPrice:  "+nextVo.getNotModPrice()+" ,flag: "+flag+" ,cetainLossType: "+lossCarMainVo
				.getCetainLossType());
		// 定损修改，且没有操作赔款和费用，则核损金额不变
		if(nextVo.getNotModPrice()==null || !nextVo.getNotModPrice()){
			// 更新精友核损金额
			if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())||
					CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType())){
				this.organizeVeriLoss(lossCarMainVo,nextVo);
				// 自动核损才更新核损施救费金额
				lossCarMainVo.setSumVeriRescueFee(lossCarMainVo.getSumRescueFee());
			}else{// 推定全损 和 盗抢折旧
				this.organizeALlLossVeriLoss(lossCarMainVo,flowTaskId);
			}

			// 附加险
			List<PrpLDlossCarSubRiskVo> subRiskList = lossCarMainVo.getPrpLDlossCarSubRisks();
			if(subRiskList!=null && subRiskList.size()>0){
				for(PrpLDlossCarSubRiskVo subRiskVo : subRiskList){
					subRiskVo.setVeriCount(subRiskVo.getCount());
					subRiskVo.setVeriSubRiskFee(subRiskVo.getSubRiskFee());
				}
			}
			// 费用信息
			List<PrpLDlossChargeVo> lossChargeList = lossChargeService.findLossChargeVos(lossCarMainVo.getId(),FlowNode.DLCar.name());
			if(lossChargeList!=null && lossChargeList.size()>0){
				for(PrpLDlossChargeVo chargeVo : lossChargeList){
					chargeVo.setVeriChargeFee(chargeVo.getChargeFee());
				}
				lossChargeService.saveOrUpdte(lossChargeList);
				lossCarMainVo.setSumVeriChargeFee(lossCarMainVo.getSumChargeFee());
			}
			if("1".equals(flag)){
				lossCarMainVo.setSumVeriCompFee(NullToZero(lossCarMainVo.getSumCompFee()));
				lossCarMainVo.setSumVeriMatFee(NullToZero(lossCarMainVo.getSumMatFee()));
				lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumLossFee()));
				lossCarMainVo.setSumVeriRepairFee(NullToZero(lossCarMainVo.getSumRepairFee()));
			}else if("2".equals(flag)){
				lossCarMainVo.setSumVeriCompFee(NullToZero(lossCarMainVo.getSumVeripCompFee()));
				lossCarMainVo.setSumVeriMatFee(NullToZero(lossCarMainVo.getSumVeripMatFee()));
				lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumVeripLoss()).add(NullToZero(lossCarMainVo.getSumRepairFee())).add(NullToZero(lossCarMainVo.getSumOutFee())));
				lossCarMainVo.setSumVeriRepairFee(NullToZero(lossCarMainVo.getSumRepairFee()));
			}else if("3".equals(flag)){
				lossCarMainVo.setSumVeriCompFee(NullToZero(lossCarMainVo.getSumCompFee()));
				lossCarMainVo.setSumVeriMatFee(NullToZero(lossCarMainVo.getSumMatFee()));
				lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumLossFee()));
				lossCarMainVo.setSumVeriRepairFee(NullToZero(lossCarMainVo.getSumRepairFee()));
			}


			for(PrpLDlossCarMaterialVo materialVo:lossCarMainVo.getPrpLDlossCarMaterials()){
				materialVo.setAuditLossCount(materialVo.getAssisCount());
				materialVo.setAuditLossPrice(NullToZero(materialVo.getUnitPrice()));
				materialVo.setAuditLossMaterialFee(NullToZero(materialVo.getMaterialFee()));
			}

			for(PrpLDlossCarCompVo compVo:lossCarMainVo.getPrpLDlossCarComps()){
				compVo.setSumCheckLoss(NullToZero(compVo.getSumDefLoss()));
				compVo.setVeriMaterFee(NullToZero(compVo.getMaterialFee()));
				compVo.setSumVeriLoss(NullToZero(compVo.getSumDefLoss()));
				compVo.setAuditCount(new BigDecimal(compVo.getQuantity()));
				compVo.setVeriQuantity(compVo.getQuantity());
				compVo.setVeriRestFee(NullToZero(compVo.getRestFee()));
			}
		}else{
			// 判断定损修改的时候
			if(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag()) &&
					nextVo.getNotModPrice() != null && nextVo.getNotModPrice()){
				if(lossCarMainVo.getSumVeriLossFee() == null){
					if("1".equals(flag) || "3".equals(flag)){
						lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumLossFee()));
					}else if("2".equals(flag)){
						lossCarMainVo.setSumVeriLossFee(NullToZero(lossCarMainVo.getSumVeripLoss()).add(NullToZero(lossCarMainVo.getSumRepairFee())).add(NullToZero(lossCarMainVo.getSumOutFee())));
					}
				}
			}

		}

		nextVo.setNextNode(FlowNode.END.name());
		nextVo.setAuditStatus(CodeConstants.VeriFlag.PASS);
		nextVo.setFlowTaskId(flowTaskId);
		SysUserVo userVo = new SysUserVo();
		userVo.setUserCode(lossCarMainVo.getUnderWriteCode());
		userVo.setUserName(lossCarMainVo.getUnderWriteName());
		userVo.setComCode(lossCarMainVo.getUnderWriteCom());
		nextVo.setUserVo(userVo);
		nextVo.setTaskInUser("AUTO");
		List<PrpLWfTaskVo> wfTaskList = submitNextTask(lossCarMainVo,nextVo);
		this.updateDeflossData(lossCarMainVo,nextVo);

		// 保存自动核损意见
		PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
		claimTextVo.setDescription("自动核损");
		claimTextVo.setStatus(CodeConstants.AuditStatus.SUBMITVLOSS);
		claimTextVo.setNodeCode(FlowNode.VLCar_LV0.name());
		this.saveClaimText(lossCarMainVo, claimTextVo, userVo);
		logger.info("报案号registno = {},结束自动核损提交方法，核损标志位UnderWriteFlag={}",lossCarMainVo.getRegistNo(),lossCarMainVo.getUnderWriteFlag());
		return wfTaskList;

	}

	private List<PrpLWfTaskVo> submitNextTask(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo) {
		WfTaskSubmitVo taskSubmitVo = new WfTaskSubmitVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));

		if(FlowNode.DLCar.name().equals(nextVo.getCurrentNode()) ||
				FlowNode.DLCarMod.name().equals(nextVo.getCurrentNode()) ||
				FlowNode.DLCarAdd.name().equals(nextVo.getCurrentNode())){
			PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(nextVo.getUserVo().getUserCode());
			if(intermMainVo !=null){
				lossCarMainVo.setIntermFlag("1");// 公估定损
				lossCarMainVo.setIntermCode(intermMainVo.getIntermCode());// 公估机构代码
			}else{
				lossCarMainVo.setIntermFlag("0");
				lossCarMainVo.setIntermCode("");// 公估机构代码
			}
		}
		Beans.copy().from(nextVo).to(taskSubmitVo);

		taskSubmitVo.setCurrentNode(FlowNode.valueOf(nextVo.getCurrentNode()));
		taskSubmitVo.setFlowId(taskVo.getFlowId());
		taskSubmitVo.setFlowTaskId(new BigDecimal(nextVo.getFlowTaskId()));
		taskSubmitVo.setNextNode(FlowNode.valueOf(nextVo.getNextNode()));
		taskSubmitVo.setHandleruser(nextVo.getUserVo().getUserCode());

		if(CodeConstants.AuditStatus.BACKLOSS.equals(lossCarMainVo.getAuditStatus())||
				CodeConstants.AuditStatus.BACKLOWER.equals(lossCarMainVo.getAuditStatus())){
			taskSubmitVo.setSubmitType(SubmitType.B);
			taskSubmitVo.setHandleIdKey(lossCarMainVo.getId().toString());
		}

		if(lossCarMainVo.getSerialNo()>1 && FlowNode.DLCar.name().equals(nextVo.getCurrentNode())){
			wfTaskHandleService.tempSaveTask(Double.parseDouble(nextVo.getFlowTaskId()),"三者车"+lossCarMainVo.getLicenseNo());
		}

		// 如果存在历史处理工号，那么判断该工号现在是否仍有处理权限
 		boolean oldAssignUsercouldHandle = false;
		if (StringUtils.isNotBlank(taskSubmitVo.getAssignUser())) {
			// true-工号有效且没有在休假中 false-工号无效或者正在休假中
			boolean isUserValid = assignService.validUserCode(taskSubmitVo.getAssignUser());
			if (isUserValid) {
				oldAssignUsercouldHandle = assignService.isUserCouldHandle(FlowNode.valueOf(nextVo.getNextNode()), nextVo.getComCode(), taskSubmitVo.getAssignUser());
			}
		}

		if(CodeConstants.AuditStatus.BACKLOWER.equals(lossCarMainVo.getAuditStatus())){
			PrpLWfTaskVo preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(),
					FlowNode.valueOf(nextVo.getNextNode())).get(0);
			taskSubmitVo.setAssignUser(preTaskVo.getHandlerUser());
			taskSubmitVo.setAssignCom(preTaskVo.getHandlerCom());

		}else if(CodeConstants.AuditStatus.RELOSS.equals(lossCarMainVo.getAuditStatus())){
//			SysUserVo assUserVo = assignService.execute(FlowNode.valueOf(nextVo.getNextNode()),lossCarMainVo.getMakeCom());
//			if(assUserVo == null){
			// throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"未配置人员 ！");
//			}
			taskSubmitVo.setAssignUser(nextVo.getAssignUser());
			taskSubmitVo.setAssignCom(nextVo.getAssignCom());

		}else if(StringUtils.isBlank(taskSubmitVo.getAssignUser()) || !oldAssignUsercouldHandle){
			if(!FlowNode.VPCar_LV0.name().equals(nextVo.getNextNode())
					&& !FlowNode.VLCar_LV0.name().equals(nextVo.getNextNode())
					&& !FlowNode.END.name().equals(nextVo.getNextNode())){
				String nextNode = nextVo.getNextNode();
				String comCode = nextVo.getComCode();
				if(nextNode.indexOf("_")!=-1){
					String[] array =nextVo.getNextNode().split("_");
					int nextLevel = Integer.parseInt(array[1].substring(2));
					if(nextLevel >=9){
						comCode = "00010000";// 总公司人员 机构用公司本部
						taskSubmitVo.setAssignCom(comCode);
					}else{
						// nextVo.getUserVo 在action层设置为当前user
						SysUserVo assUserVo = null;
						// 2110 关于车险理赔系统审核任务分配规则优化的申请 定损提交下一节点，指定处理人不能是定损处理人
                        String currentNode = nextVo.getCurrentNode();
						if (FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.DLoss.name()) ||
                                FlowNode.valueOf(currentNode).getUpperNode()
                                        .equals(FlowNode.DLCar.name())) {
							//传 0 代表 排除当前处理人
							assUserVo = assignService.execute(FlowNode.valueOf(nextVo.getNextNode()),comCode,nextVo.getUserVo(), "0");
						} else {
							// 下一节点任务指定给当前人开关 2019-12-8 19:56:12  需求1706
							PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ISSPECIFIEDUSER, comCode);
							if ("1".equals(configValueVo.getConfigValue()) && nextVo.getUserVo() != null && nextVo.getUserVo().getUserCode() != null) {
								boolean userCouldHandle = assignService.isUserCouldHandle(FlowNode.valueOf(nextVo.getNextNode()), comCode, nextVo.getUserVo().getUserCode());
								if (userCouldHandle) {
									assUserVo = nextVo.getUserVo();
								} else {
									assUserVo = assignService.execute(FlowNode.valueOf(nextVo.getNextNode()),comCode,nextVo.getUserVo(), "");
								}
							} else {
								assUserVo = assignService.execute(FlowNode.valueOf(nextVo.getNextNode()),comCode,nextVo.getUserVo(), "");
							}
						}
						if(assUserVo == null){
							if(nextLevel ==lossCarMainVo.getMaxLevel()){
								throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"未配置人员 ！");
							}else{
								throw new IllegalArgumentException(FlowNode.valueOf(nextVo.getNextNode()).getName()+"未配置人员，请选择其他级别");
							}

						}
						taskSubmitVo.setAssignUser(assUserVo.getUserCode());
						taskSubmitVo.setAssignCom(assUserVo.getComCode());
					}
				}
			}
		}
		// 情况handleuser
		taskSubmitVo.setHandleruser(null);
		List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.submitLossCar(lossCarMainVo,taskSubmitVo);

		// 发起大案审核
		if(FlowNode.DLCar.name().equals(nextVo.getCurrentNode())
				&& RadioValue.RADIO_YES.equals(lossCarMainVo.getIsMajorCase())){
			submitChkBig(taskVo, nextVo);
		}

		// 修改定损 最后一个核损任务 发起理算任务
		//CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())
		if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(lossCarMainVo.getAuditStatus())){

			List<PrpLWfTaskVo> comTaskList = lossCarService.modifyToSubMitComp(lossCarMainVo.getRegistNo(),nextVo.getUserVo());
			wfTaskVoList.addAll(comTaskList);
		}

		return wfTaskVoList;
	}

	// 发起大案审核
	private PrpLWfTaskVo submitChkBig(PrpLWfTaskVo wfTaskVo,SubmitNextVo nextVo){
		String registNo = wfTaskVo.getRegistNo();
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
		taskVo.setItemName(wfTaskVo.getItemName());
		taskVo.setBussTag(wfTaskVo.getBussTag());
		taskVo.setRegistNo(registNo);
		taskVo.setHandlerIdKey(checkVo.getId().toString());
		taskVo.setShowInfoXml(wfTaskVo.getShowInfoXML());

		WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
		submitVo.setFlowId(wfTaskVo.getFlowId());
		submitVo.setFlowTaskId(wfTaskVo.getTaskId());
		submitVo.setComCode(nextVo.getComCode());
		submitVo.setTaskInUser(nextVo.getTaskInUser());
		submitVo.setTaskInKey(registNo);
		submitVo.setHandleIdKey(checkVo.getId().toString());

		submitVo.setAssignUser("");// 大案审核无需指定人
		submitVo.setAssignCom(nextVo.getComCode());

		submitVo.setCurrentNode(FlowNode.valueOf(nextVo.getCurrentNode()));
		submitVo.setNextNode(FlowNode.ChkBig_LV1);

		PrpLWfTaskVo newWfTaskVo =  wfTaskHandleService.addSimpleTask(taskVo, submitVo);
		return newWfTaskVo;
	}

	/**
	 * 定损指标表 更新 1 首次定损 金额 时间 机构 定损员 2 首次核价通过 金额 时间 机构 核损员 3 首次核损通过 金额 时间 机构 核损员 4 每次定损提交 记录(配件+辅料)金额 和总数量 ☆yangkun(2016年1月22日 下午9:11:34): <br>
	 */
	@Override
	public void saveLossIndex(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo){
		String currentNode = nextVo.getCurrentNode();
		PrpLDlossIndexVo lossIndexVo = lossIndexService.findLossIndex(lossCarMainVo.getId(),FlowNode.DLCar.name());
		// 零配件更换费用与零配件辅料价格的总金额
		BigDecimal firstVeriLoss = new BigDecimal("0");
		Integer quantity = new Integer(0);

		if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType()) ||
				CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType())){

			firstVeriLoss = NullToZero(lossCarMainVo.getSumCompFee()).add(NullToZero(lossCarMainVo.getSumMatFee()))
					.subtract(NullToZero(lossCarMainVo.getSumRemnant()));

			List<PrpLDlossCarCompVo> carCompList = lossCarMainVo.getPrpLDlossCarComps();
			if(carCompList!=null && !carCompList.isEmpty()){
				quantity = carCompList.size();
			}
			// 换件+辅料 总条数
			List<PrpLDlossCarMaterialVo> materialList = lossCarMainVo.getPrpLDlossCarMaterials();
			if(materialList!=null && !materialList.isEmpty()){
				quantity = quantity + materialList.size();
			}
		}

		if(lossIndexVo == null){
			lossIndexVo = new PrpLDlossIndexVo();
			lossCarMainVo.setSumLossFee(NullToZero(lossCarMainVo.getSumLossFee()));
			BigDecimal firstDefLoss = lossCarMainVo.getSumLossFee();

			lossIndexVo.setBussTaskId(lossCarMainVo.getId());
			lossIndexVo.setRegistNo(lossCarMainVo.getRegistNo());
			lossIndexVo.setNodeCode(FlowNode.DLCar.name());
			String linceNo = lossCarMainVo.getLossCarInfoVo().getLicenseNo();
			if(lossCarMainVo.getSerialNo()==1){
				lossIndexVo.setNodeItem("标的车("+linceNo+")");
			}else{
				lossIndexVo.setNodeItem("三者车("+linceNo+")");
			}

			lossIndexVo.setHandlerCode(lossCarMainVo.getHandlerCode());
			lossIndexVo.setHandlerName(lossCarMainVo.getHandlerName());
			lossIndexVo.setHandlerIdNo(lossCarMainVo.getHandlerIdNo());
			lossIndexVo.setComCode(lossCarMainVo.getComCode());
			lossIndexVo.setDefEndDate(lossCarMainVo.getDefEndDate());
			lossIndexVo.setFirstDefLoss(firstDefLoss);
			lossIndexVo.setFirstVeriLoss(firstVeriLoss);
			lossIndexVo.setComCode(lossCarMainVo.getComCode());

			lossIndexVo.setPreSumFee(firstVeriLoss);
			lossIndexVo.setQuantity(quantity);
		}else{
			// 第一次核价通过时，保存初次核价金额
			if(FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.VPCar.name())
				&& CodeConstants.AuditStatus.SUBMITVPRICE.equals(nextVo.getAuditStatus())
				&& lossIndexVo.getVeripEnddate()==null){
					lossIndexVo.setVeripHandlerCode(lossCarMainVo.getVeripHandlerCode());
					lossIndexVo.setVeripHandlerName(lossCarMainVo.getVeripHandlerName());
					lossIndexVo.setVeripIdNo(lossCarMainVo.getVeripIdNo());
					lossIndexVo.setVeripCom(lossCarMainVo.getVeripCom());
					lossIndexVo.setVeripEnddate(lossCarMainVo.getVeripEnddate());
					lossIndexVo.setSumVeripLoss(lossCarMainVo.getSumVeripLoss());
			}else if(FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.VLCar.name())
					&& CodeConstants.AuditStatus.SUBMITVLOSS.equals(nextVo.getAuditStatus())
&&lossIndexVo.getUnderWriteEndDate()==null){// 第一次核损通过时，保存初次核损金额
				lossIndexVo.setUnderWriteCode(lossCarMainVo.getUnderWriteCode());
				lossIndexVo.setUnderWriteName(lossCarMainVo.getUnderWriteName());
				lossIndexVo.setUnderWiteIdNo(lossCarMainVo.getUnderWiteIdNo());
				lossIndexVo.setUnderWriteCom(lossCarMainVo.getUnderWriteCom());
				lossIndexVo.setUnderWriteEndDate(lossCarMainVo.getUnderWriteEndDate());
				lossIndexVo.setSumVeriLossFee(lossCarMainVo.getSumVeriLossFee());
			}else if((FlowNode.DLCar.name().equals(nextVo.getCurrentNode()) ||
					FlowNode.DLCar.name().equals(FlowNode.valueOf(currentNode).getUpperNode()))
					&& CodeConstants.AuditStatus.SUBMITLOSS.equals(nextVo.getAuditStatus())){
				lossIndexVo.setPreSumFee(firstVeriLoss);
				lossIndexVo.setQuantity(quantity);
			}
		}

		lossIndexService.saveOrUpdte(lossIndexVo);
	}

	@Override
	public SubmitNextVo organizeNextVo(Long lossMainId,String flowTaskId,String auditStatus,String isMobileCheck,SysUserVo userVo,String isSubmitHeadOffice){
		SubmitNextVo nextVo = new SubmitNextVo();
		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
		if(CodeConstants.HandlerStatus.END.equals(taskVo.getHandlerStatus())){
			throw new IllegalArgumentException("该任务已处理完成，请刷新页面！");
		}

		String currentName = FlowNode.valueOf(taskVo.getSubNodeCode()).getName();
		nextVo.setCurrentName(currentName);
		nextVo.setCurrentNode(taskVo.getSubNodeCode());
//		nextVo.setComCode(userVo.getComCode());
		if(CodeConstants.AuditStatus.RECHECK.equals(auditStatus)){
			nextVo.setNextNode(FlowNode.ChkRe.name());
		}

		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(lossMainId);
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		lossCarMainVo.setLossCarInfoVo(carInfoVo);
		nextVo.setComCode(lossCarMainVo.getMakeCom());
		if( !CodeConstants.AuditStatus.RELOSS.equals(lossCarMainVo.getAuditStatus())
				&& "1".equals(lossCarMainVo.getReCheckFlag())
				&& !wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.DLChk,lossCarMainVo.getId().toString(),"")){

			throw new IllegalArgumentException("车辆选择了复检，请走复检流程！");
		}
		nextVo.setUserVo(userVo);
		nextVo.setFlowTaskId(flowTaskId);
		this.getNodePath(nextVo,lossCarMainVo,taskVo,isMobileCheck,isSubmitHeadOffice);

		nextVo.setTaskInKey(lossMainId.toString());
		nextVo.setRegistNo(lossCarMainVo.getRegistNo());
		nextVo.setAuditStatus(lossCarMainVo.getAuditStatus());


		return nextVo;
	}

	/**
	 * 查找提交路径
	 * @modified: ☆yangkun(2016年1月19日 下午4:36:14): <br>
	 */
	private void getNodePath(SubmitNextVo nextVo,PrpLDlossCarMainVo lossCarMainVo,PrpLWfTaskVo taskVo,String isMobileCheck,String isSubmitHeadOffice){
		String currentNode = nextVo.getCurrentNode();
		Map<String,String> nextNodeMap = new TreeMap<String,String>();
		String auditStatus = lossCarMainVo.getAuditStatus();
		// 复勘提交给原查勘人员
		if(FlowNode.ChkRe.name().equals(nextVo.getNextNode())){// 复勘
			nextNodeMap.put(FlowNode.ChkRe.name(),FlowNode.ChkRe.getName());
			nextVo.setNodeMap(nextNodeMap);
			PrpLWfTaskVo preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), null,FlowNode.Chk).get(0);
			nextVo.setAssignCom(preTaskVo.getHandlerCom());
			nextVo.setAssignUser(preTaskVo.getHandlerUser());
		}else if(FlowNode.DLChk.name().equals(currentNode)){// 复检提交到原提交的核损人员
			PrpLWfTaskVo preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(),
					FlowNode.VLCar).get(0);
			nextNodeMap.put(preTaskVo.getSubNodeCode(),FlowNode.valueOf(preTaskVo.getSubNodeCode()).getName());
			// TODO 判定是否在岗,不在岗，则根据调用的级别统一用轮询机制赋值
			nextVo.setAssignCom(preTaskVo.getHandlerCom());
			nextVo.setAssignUser(preTaskVo.getHandlerUser());
			nextVo.setNodeMap(nextNodeMap);
		}else if(FlowNode.DLCar.name().equals(currentNode)
				|| FlowNode.valueOf(currentNode).getUpperNode().equals(FlowNode.DLCar.name())){

			if(CodeConstants.VeriFlag.INIT.equals(lossCarMainVo.getVeriPriceFlag())
					&& CodeConstants.VeriFlag.INIT.equals(lossCarMainVo.getUnderWriteFlag())){
				BigDecimal veripFee = NullToZero(lossCarMainVo.getSumVeripCompFee()).add(NullToZero(lossCarMainVo.getSumVeripMatFee()));
				BigDecimal lossFee = NullToZero(lossCarMainVo.getSumCompFee()).add(NullToZero(lossCarMainVo.getSumMatFee()));

				// 判断是否简易赔案方法，返回1代表自动核价核损，2代表自动核损，0是非简易赔案
				String simpleClaimFlag = this.isSimpleClaim(nextVo.getUserVo(), lossCarMainVo, taskVo,isMobileCheck);
	            PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,lossCarMainVo.getComCode());
				Boolean bl = this.notModPrice(lossCarMainVo);
				nextVo.setNotModPrice(bl);
				// 定损修改，且没有操作赔款和费用就自动审核通过
				if(bl){
					if(lossFee.compareTo(BigDecimal.ZERO)==1||veripFee.compareTo(BigDecimal.ZERO)==1){// 辅料和配件金额大于0，走核价环节
						nextVo.setAutoPriceFlag("1");
						nextVo.setAutoLossFlag("1");
					}else{
						nextVo.setAutoLossFlag("1");
					}
				}else{
					// 考虑核价大于1 是由于修改定损 TODO修定损 建议不能核价核损
					if ((CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
							|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))
							&& (lossFee.compareTo(BigDecimal.ZERO) == 1 || veripFee.compareTo(BigDecimal.ZERO) == 1)) {// 辅料和配件金额大于0，走核价环节
						deflossToPrice(lossCarMainVo,nextVo,isSubmitHeadOffice);
					}else{// 核损
						deflossToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
					}

					// 修改定损 如果退回的人和当前提交的级别是同一个级别 则给原核价或核损人员 自动核损不走该逻辑
					if(FlowNode.DLCarMod.name().equals(currentNode) &&  !"1".equals(nextVo.getAutoLossFlag())){
						this.backToOldUser(nextVo, lossCarMainVo,null);
					}
				}
				// 简易赔案
				if ("1".equals(configRuleValueVo.getConfigValue()) && ("1".equals(simpleClaimFlag) || "2".equals(simpleClaimFlag))) {
					if ("1".equals(simpleClaimFlag)) {
						nextVo.setAutoPriceFlag("1");
						nextVo.setAutoLossFlag("1");
					} else {
						nextVo.setAutoLossFlag("1");
					}
				}
			}else{// 退回定损后提交
					// 核价不同意，直接先走核价
				if((CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
						||CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))
						&& (CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getVeriPriceFlag()) ||
						CodeConstants.VeriFlag.NOSUBMIT.equals(lossCarMainVo.getVeriPriceFlag()))){

					deflossToPrice(lossCarMainVo,nextVo,isSubmitHeadOffice);

				}else{// 比较辅料和配件金额 和条数与上次定损的金额 条数是否一致，不一致则提交到核价，否则核损
					if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
							||CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType())){
						PrpLDlossIndexVo lossIndexVo = lossIndexService.findLossIndex(lossCarMainVo.getId(),FlowNode.DLCar.name());
						// 零配件更换费用与零配件辅料价格的总金额
						BigDecimal firstVeriLoss = new BigDecimal("0");
						firstVeriLoss = NullToZero(lossCarMainVo.getSumCompFee()).add(NullToZero(lossCarMainVo.getSumMatFee()))
								.subtract(NullToZero(lossCarMainVo.getSumRemnant()));
						Integer quantity = new Integer(0);
						if(lossCarMainVo.getPrpLDlossCarComps()!=null && lossCarMainVo.getPrpLDlossCarComps().size()>0){
							quantity = lossCarMainVo.getPrpLDlossCarComps().size();
						}
						// 换件+辅料 总条数
						if(lossCarMainVo.getPrpLDlossCarMaterials()!=null && lossCarMainVo.getPrpLDlossCarMaterials().size()>0){
							quantity = quantity + lossCarMainVo.getPrpLDlossCarMaterials().size();
						}
						if(firstVeriLoss.compareTo(lossIndexVo.getPreSumFee())==0 &&
							quantity.equals(lossIndexVo.getQuantity())){

							deflossToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
						}
						else{

							deflossToPrice(lossCarMainVo,nextVo,isSubmitHeadOffice);
						}
					}else{
						deflossToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
					}

				}
				// 退回定损 如果退回的人和当前提交的级别是同一个级别 则给原核价或核损人员 自动核损不走该逻辑
				if(!"1".equals(nextVo.getAutoLossFlag())){
				    this.backToOldUser(nextVo, lossCarMainVo,null);
				}
			}
		}else if(currentNode.startsWith(FlowNode.VPCar.name())){// 核价
			if(CodeConstants.AuditStatus.SUBMITVPRICE.equals(auditStatus) ||
					CodeConstants.AuditStatus.SUBMITVPRICENO.equals(auditStatus)){// 提交核价

				nextNodeMap = this.priceToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
				// 退回定损或修改定损 如果退回的人拥有当前提交级别的权限（拥有权限级别大于等于当前提交级别） 则给原核价或核损人员
				if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())
						|| CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
					this.backToOldUser(nextVo, lossCarMainVo,FlowNode.VLCar);
				}

			}else if(CodeConstants.AuditStatus.AUDIT.equals(auditStatus)
					||CodeConstants.AuditStatus.BACKLOWER.equals(auditStatus)){// 提交上级 或退回下级
				nextNodeMap = findVerifyLevelPath(lossCarMainVo,taskVo,auditStatus,"VPCar_LV");

			}else if(CodeConstants.AuditStatus.BACKLOSS.equals(auditStatus)){// 退回定损
				nextNodeMap.put(lossCarMainVo.getFlowFlag(),FlowNode.valueOf(lossCarMainVo.getFlowFlag()).getName());

			}

			nextVo.setNodeMap(nextNodeMap);
		}else{// 核损
			if(CodeConstants.AuditStatus.SUBMITVLOSS.equals(auditStatus)){// 同意核损
				if(wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.ChkBig,null,"0")){
					throw new IllegalArgumentException("大案审核未提交，核损不能提交！");
				}

				if(CodeConstants.VeriFlag.NOSUBMIT.equals(lossCarMainVo.getVeriPriceFlag())){
					throw new IllegalArgumentException("核价不同意，核损不能通过！");
				}
				if(!FlowNode.VLCar_LV9.name().equals(currentNode) && !FlowNode.VLCar_LV10.name().equals(currentNode)
						&& !FlowNode.VLCar_LV11.name().equals(currentNode) && !FlowNode.VLCar_LV12.name().equals(currentNode)
						&& CodeConstants.CommonConst.TRUE.equals(isSubmitHeadOffice)){
					throw new IllegalArgumentException("该任务存在总公司审核操作历史，需提交上级！");
				}
				nextVo.setEndFlag("1");
			}else if(CodeConstants.AuditStatus.AUDIT.equals(auditStatus)
					|| CodeConstants.AuditStatus.BACKLOWER.equals(auditStatus)){// 提交上级 或退回下级
				nextNodeMap = findVerifyLevelPath(lossCarMainVo,taskVo,auditStatus,"VLCar_LV");

			}else if(CodeConstants.AuditStatus.BACKLOSS.equals(auditStatus)){// 退回定损
				String flowFlag = lossCarMainVo.getFlowFlag();
				if(flowFlag.equals(FlowNode.DLChk.name())){
					flowFlag = FlowNode.DLCar.name();
				}

				nextNodeMap.put(flowFlag,FlowNode.valueOf(flowFlag).getName());

			}else if(CodeConstants.AuditStatus.RELOSS.equals(auditStatus)){// 复检提交到原提交定损人员
				nextNodeMap.put(FlowNode.DLChk.name(),FlowNode.DLChk.getName());
				PrpLWfTaskVo preTaskVo = new PrpLWfTaskVo();
				if(CodeConstants.ScheduleDefSource.SCHEDULEADD.equals(lossCarMainVo.getDeflossSourceFlag())){// 定损追加
					preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(),
							FlowNode.DLCarAdd).get(0);
				}else{// 普通定损
					preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), lossCarMainVo.getId().toString(),
							FlowNode.DLCar).get(0);
				}
				nextVo.setAssignCom(preTaskVo.getHandlerCom());
				nextVo.setAssignUser(preTaskVo.getHandlerUser());
			}else if(CodeConstants.AuditStatus.RECHECK.equals(auditStatus)){
				nextNodeMap.put(FlowNode.ChkRe.name(),FlowNode.ChkRe.getName());
				PrpLWfTaskVo preTaskVo = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(), null,FlowNode.Chk).get(0);
				nextVo.setAssignCom(preTaskVo.getHandlerCom());
				nextVo.setAssignUser(preTaskVo.getHandlerUser());
			}
			nextVo.setNodeMap(nextNodeMap);
		}

		if("1".equals(nextVo.getAutoLossFlag())){
			if(wfTaskHandleService.existTaskByNodeCode(lossCarMainVo.getRegistNo(),FlowNode.ChkBig,null,"0")){
				throw new IllegalArgumentException("该案件满足自动核损条件，但大案审核未提交，核损不能提交！");
			}
		}

	}

	/**
	 * 退回定损修改定损 如果退回的人和当前提交的级别是同一个级别 则给原核价或核损人员
	 * @param nextVo
	 * @param lossCarMainVo
	 */
	private void backToOldUser(SubmitNextVo nextVo,PrpLDlossCarMainVo lossCarMainVo,FlowNode flowNode){
		//FlowNode flowNode = null;
		if(flowNode == null){
			if(nextVo.getNodeLevel()!=null && nextVo.getNodeLevel().startsWith("VPCar_LV")){
				flowNode = FlowNode.VPCar;
			}else{
				flowNode = FlowNode.VLCar;
			}
		}
		if(flowNode!=null){
			List<PrpLWfTaskVo> taskList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),
					lossCarMainVo.getId().toString(),flowNode);
			if(taskList!=null &&  !taskList.isEmpty()){
				PrpLWfTaskVo preTaskVo = taskList.get(0);
				// 获取所有子节点
				List<FlowNode> nodeList = flowNode.getChildrenNodes();
				for (FlowNode node : nodeList) {
					if (node.name().equals(nextVo.getNodeLevel())) {
						flowNode = node;
						break;
					}
				}
				boolean isUserCouldHandle = assignService.isUserCouldHandle(flowNode, nextVo.getComCode(), preTaskVo.getHandlerUser());
				if (isUserCouldHandle) {
					// 判断是否休假或离职
					if (assignService.validUserCode(preTaskVo.getHandlerUser())) {
						nextVo.setAssignCom(preTaskVo.getHandlerCom());
						nextVo.setAssignUser(preTaskVo.getHandlerUser());
					}
				}
			}
		}
	}

	/**
	 * 核价 核损提交上级或退回下级路径
	 * @param lossCarMainVo
	 * @param taskVo
	 * @param auditStatus
	 * @param verifyType 核价 "VPCar_LV" 核损 "VLCar_LV"
	 * @return
	 * @modified: ☆YangKun(2016年6月17日 上午9:36:32): <br>
	 */
	@Override
	public Map<String,String> findVerifyLevelPath(PrpLDlossCarMainVo lossCarMainVo,PrpLWfTaskVo taskVo,String auditStatus,String verifyType){
		String currentNode = taskVo.getSubNodeCode();
		Map<String,String> nextNodeMap = new TreeMap<String,String>();
		if(CodeConstants.AuditStatus.AUDIT.equals(auditStatus)){// 提交上级
			int currencyLevel = Integer.parseInt(currentNode.substring(verifyType.length()));
			if(currencyLevel>=8||currencyLevel==lossCarMainVo.getMaxLevel()){// 总公司级别
				int headLevel = 8;// 分公司最高级，lossCarMainVo.getMaxLevel() 公司配置的分公司最高级
				if(currencyLevel>8){
					headLevel = currencyLevel;
				}

				String next = verifyType+(headLevel+1);
				nextNodeMap.put(next,FlowNode.valueOf(next).getName());
			}else{
				Integer maxLevel = lossCarMainVo.getMaxLevel();
				if(Integer.valueOf(currencyLevel)<maxLevel){
					for(int i=currencyLevel+1;i<=maxLevel;i++){
						String next = verifyType+i;
						nextNodeMap.put(next,FlowNode.valueOf(next).getName());
					}
				}
			}
		}else if(CodeConstants.AuditStatus.BACKLOWER.equals(auditStatus)){
			int currencyLevel = Integer.parseInt(currentNode.substring(verifyType.length()));
			if(currencyLevel>9){// 总公司级别
				String next = verifyType+(currencyLevel-1);
				nextNodeMap.put(next,FlowNode.valueOf(next).getName());
			}else if(currencyLevel==9){
				String next = verifyType+lossCarMainVo.getMaxLevel();
				nextNodeMap.put(next,FlowNode.valueOf(next).getName());
			}else{
				String upperNode =FlowNode.VPCar.name();
				if(verifyType.equals("VLCar_LV")){
					upperNode = FlowNode.VLCar.name();
				}


				List<PrpDNodeVo> nodeList = wfTaskHandleService.findLowerNode(taskVo.getUpperTaskId().doubleValue(),
						taskVo.getSubNodeCode(),upperNode);
				for(PrpDNodeVo nodeVo : nodeList ){
					nextNodeMap.put(nodeVo.getNodeCode(),nodeVo.getNodeName());
				}
			}
		}
		return nextNodeMap;
	}


	/**
	 * 自动核价规则 a. 车辆损失：单个配件不超过2000元，单个核价任务总金额不超过5000元。 b. 车辆性质：家庭自用 UseNature 8A，非营运 UseKind 002（从保单信息带入检测）； 三者车的 TODO c. 配件为全部一次点选的配件（精友返回）； d. 不同客户等级（维度），规则不同。 e.
	 * 自动核价、自动核损、自动理算时，针对定损追加提交时，匹配规则需考虑原定损任务金额。
	 */
	private void deflossToPrice(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo,String isSubmitHeadOffice){
		String nodeCode = "";

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
		String comCode = lossCarMainVo.getComCode();
        if("3".equals(taskVo.getSubCheckFlag())){
			// 如果是公估案件，分配给承保地人员
            nextVo.setComCode(lossCarMainVo.getComCode());
        }

		boolean autoPrice = false; // 自动核价
		boolean backFlag = false; // 是否退回
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getVeriPriceFlag()) ||
				CodeConstants.VeriFlag.NOSUBMIT.equals(lossCarMainVo.getVeriPriceFlag())){
			backFlag = true;
		}

		if(!autoPrice){
			Map<String,String> nextNodeMap = new TreeMap<String,String>();
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,comCode);
			if("1".equals(configValueVo.getConfigValue())){// 使用ilog
			    lossCarMainVo.setComCode(nextVo.getComCode());
			    LIlogRuleResVo vPriceResVo = null;
	            try{
	                vPriceResVo = deflossHandleIlogService.organizaVprice(lossCarMainVo,"1",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),FlowNode.DLCar.toString(),nextVo,isSubmitHeadOffice);
	            } catch(Exception e){
	                e.printStackTrace();
	            }

	            /*	兜底人员权限判断 start  */
	            String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
	        	boolean finalAutoPass = true;
	        	if ("1".equals(finalPowerFlag)) {
	        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(nextVo.getUserVo().getUserCode());
	        		if (powerInfoVo != null) {
	        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
	        			if (gradePower != null) {
	        				// 总定损金额
	        				BigDecimal sumAmount = BigDecimal.ZERO;
	        				sumAmount = lossCarMainVo.getSumLossFee();
	        				if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
	        					List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
	        					for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
	        						if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
	        							sumAmount = sumAmount
	        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
	        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
	        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
	        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
	        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
	        						}
	        					}
	        				}

	        				if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
	        					sumAmount = sumAmount
	        							.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
	        							.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
	        				}else{
	        					if(lossCarMainVo.getSumVeripLoss()!=null
	        							&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
	        									|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
	        						sumAmount = sumAmount
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
	        					}else{
	        						sumAmount = sumAmount
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
	        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
	        					}
	        				}

	        				if (sumAmount.compareTo(gradePower) == 1) {
	        					finalAutoPass = false;
	        				}
	        			}
	        		} else {
	        			finalAutoPass = false;
	        		}
	        	}
	        	/*	兜底人员权限判断  end   */

			    if(("1".equals(vPriceResVo.getUnderwriterflag())||"4".equals(vPriceResVo.getUnderwriterflag())) && finalAutoPass){
			    	nextVo.setAutoPriceFlag("1");
			        nextVo.setAutoLossFlag("2");
			        this.deflossToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
				}else{// 再次调用获取级别
			    	try{
	                    vPriceResVo = deflossHandleIlogService.organizaVprice(lossCarMainVo,"2",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),FlowNode.DLCar.toString(),nextVo,isSubmitHeadOffice);
	                }
	                catch(Exception e){
	                    e.printStackTrace();
					}// 传2人工
	                int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
                    int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
                    boolean haveUser = false;
                    int level = backLevel;
					while( !haveUser&&level<=maxLevel){// 判断该级别是否有人，没人逐级上传
                        haveUser = assignService.existsGradeUser(FlowNode.valueOf("VPCar_LV"+level), nextVo.getComCode());
                        if(!haveUser){
                            level ++;
                        }
                    }

                    if(level>maxLevel){
                        level = maxLevel;
                    }
                    nodeCode = "VPCar_LV"+level;
                    nextVo.setVerifyLevel(String.valueOf(backLevel));
                    nextVo.setMaxLevel(vPriceResVo.getMaxUndwrtNode()+"");
                    nextVo.setNodeLevel(nodeCode);
                    nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
                    nextVo.setNodeMap(nextNodeMap);
			    }
			    lossCarMainVo.setComCode(comCode);

			}
            PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,comCode);
            if("1".equals(configRuleValueVo.getConfigValue())){
            	nextNodeMap = new TreeMap<String,String>();
                nextVo.setAutoPriceFlag("");
                nextVo.setAutoLossFlag("");
    			VerifyLossRuleVo ruleVo = this.organizeVerifyLossRule(lossCarMainVo,"0");
    			ruleVo.setComCode(nextVo.getComCode());
    			ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
    			int backLevel = ruleVo.getBackLevel();
    			boolean haveUser = false;
    			int level = backLevel;
				while( !haveUser&&level<=ruleVo.getMaxLevel()){// 判断该级别是否有人，没人逐级上传
    				haveUser = assignService.existsGradeUser(FlowNode.valueOf("VPCar_LV"+level), nextVo.getComCode());
    				if(!haveUser){
    					level ++;
    				}
    			}

    			nextVo.setVerifyLevel(level+"");
    			nextVo.setMaxLevel(ruleVo.getMaxLevel()+"");
    			if(level>8){
    				level = ruleVo.getMaxLevel();
    				ruleVo.setTopComp("1");
    				ruleVo = claimRuleApiService.lossCarToPriceRule(ruleVo);
    				nextVo.setVerifyLevel(ruleVo.getBackLevel()+"");
    			}


    			nodeCode = "VPCar_LV"+level;
    			nextVo.setNodeLevel(nodeCode);
    			nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
    			nextVo.setNodeMap(nextNodeMap);
            }
		}else{
			nextVo.setAutoPriceFlag("1");
			this.deflossToVeriLoss(lossCarMainVo,nextVo,isSubmitHeadOffice);
		}

	}

	/**
	 * ①　定损0提交可自动核损； ②　单个核损任务的总工时费不超过2000元，总精友金额不超过5000元； 可设置工号：设置指定工号提交的案件才能走自动核损规则。 车辆性质：家庭自用，非营运（从保单信息带入检测）。 配件全部为一次点选的配件（精友返回）。 不同客户等级（维度），规则不同。
	 */
	private void deflossToVeriLoss(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo,String isSubmitHeadOffice ){
		String nodeCode = "";
		Map<String,String> nextNodeMap = new TreeMap<String,String>();
		boolean autoVeriFlag = false; // 自动核损
		boolean backFlag = false; // 是否退回

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.parseDouble(nextVo.getFlowTaskId()));
        String comCode = lossCarMainVo.getComCode();
        if("3".equals(taskVo.getSubCheckFlag())){
			// 如果是公估案件，分配给承保地人员
            nextVo.setComCode(lossCarMainVo.getComCode());
        }

		// 退回的案件 不能自动核价
		// 暂时不自动核价
//		if(!backFlag && ruleVo.getMaxPartFee()<=2000
//			&& ruleVo.getSumLossFee()<=5000
//			&& ruleVo.getSelfConfigFlag()==0){
//			if(lossCarMainVo.getSerialNo()==1){
//				if("002".equals(ruleVo.getUseKindCode()) && "8A".equals(ruleVo.getUseNatureCode())){
//					autoVeriFlag = true;
//				}
//			}else{
//				if("A0".equals(ruleVo.getCarKindCode())){
//					autoVeriFlag = true;
//				}
//			}
//		}

        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,comCode);
		if("1".equals(configValueVo.getConfigValue())){// 使用ilog
	        lossCarMainVo.setComCode(nextVo.getComCode());
	        LIlogRuleResVo vPriceResVo = null;
	        try{
	            vPriceResVo = deflossHandleIlogService.organizaVehicleLoss(lossCarMainVo,"1",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode(),isSubmitHeadOffice);
	        } catch(Exception e){
	            e.printStackTrace();
	        }

	        /*	兜底人员权限判断 start  */
	        String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
        	boolean finalAutoPass = true;
        	if ("1".equals(finalPowerFlag)) {
        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(nextVo.getUserVo().getUserCode());
        		if (powerInfoVo != null) {
        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
        			if (gradePower != null) {
        				// 总定损金额
        				BigDecimal sumAmount = BigDecimal.ZERO;
        				if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
        					List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
        					for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
        						if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){

        							sumAmount = sumAmount
        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
        						}
        					}
        				}

        				if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
        					sumAmount = sumAmount
        							.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
        							.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
        				}else{
        					if(lossCarMainVo.getSumVeripLoss()!=null
        							&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
        									|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
        						sumAmount = sumAmount
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
        					}else{
        						sumAmount = sumAmount
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
        					}
        				}

        				if (sumAmount.compareTo(gradePower) == 1) {
        					finalAutoPass = false;
        				}
        			}
        		} else {
        			finalAutoPass = false;
        		}
        	}
        	/*	兜底人员权限判断   end   */

	        if(("1".equals(vPriceResVo.getUnderwriterflag())||"4".equals(vPriceResVo.getUnderwriterflag())) && finalAutoPass){
	            autoVeriFlag = true;
	        }
			if(autoVeriFlag){
				nextVo.setAutoLossFlag("1");
			}else{
				try{
	                vPriceResVo = deflossHandleIlogService.organizaVehicleLoss(lossCarMainVo,"2",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode(),isSubmitHeadOffice);
	            }
	            catch(Exception e){
	                e.printStackTrace();
				}// 传2人工
                lossCarMainVo.setComCode(comCode);
                int backLevel = Integer.parseInt(vPriceResVo.getMinUndwrtNode());
                int maxLevel = Integer.parseInt(vPriceResVo.getMaxUndwrtNode());
                boolean haveUser = false;
                int level = backLevel;
				while( !haveUser&&level<=maxLevel){// 判断该级别是否有人，没人逐级上传
                    haveUser = assignService.existsGradeUser(FlowNode.valueOf("VLCar_LV"+level), nextVo.getComCode());
                    if(!haveUser){
                        level ++;
                    }
                }
                if(level>maxLevel){
                    level = maxLevel;
                }
                nodeCode = "VLCar_LV"+level;
                nextVo.setVerifyLevel(String.valueOf(backLevel));
                nextVo.setMaxLevel(vPriceResVo.getMaxUndwrtNode()+"");
                nextVo.setNodeLevel(nodeCode);
                nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
                nextVo.setNodeMap(nextNodeMap);
			}
		}// else{//使用规则引擎
        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,comCode);
        if("1".equals(configRuleValueVo.getConfigValue())){
        	nextNodeMap = new TreeMap<String,String>();
        	nextVo.setAutoLossFlag("0");
			autoVeriFlag = false; // 自动核损
    		lossCarMainVo.setComCode(comCode);
            VerifyLossRuleVo ruleVo = this.organizeVerifyLossRule(lossCarMainVo,"1");
            if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())){
            	backFlag = true;
            }
            if(!backFlag && ruleVo.getSumLossFee()==0d){
    			autoVeriFlag = true;
    		}
            ruleVo.setComCode(nextVo.getComCode());
            if(!autoVeriFlag){
            	ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
    			int backLevel = ruleVo.getBackLevel();
    			boolean haveUser = false;
    			int level = backLevel;
				while( !haveUser&&level<=ruleVo.getMaxLevel()){// 判断该级别是否有人，没人逐级上传
    				haveUser = assignService.existsGradeUser(FlowNode.valueOf("VLCar_LV"+level), nextVo.getComCode());
    				if(!haveUser){
    					level ++;
    				}
    			}

    			nextVo.setVerifyLevel(level+"");
    			nextVo.setMaxLevel(ruleVo.getMaxLevel()+"");
				if(level>8){// 大于分公司最高级别8 则提交到分公司最高级别
    				level = ruleVo.getMaxLevel();
    				ruleVo.setTopComp("1");
    				ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
    				nextVo.setVerifyLevel(ruleVo.getBackLevel()+"");
    			}
    			nodeCode = "VLCar_LV"+level;
    			nextVo.setNodeLevel(nodeCode);
    			nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
    			nextVo.setNodeMap(nextNodeMap);

            }else{
    			nextVo.setAutoLossFlag("1");
    		}
        }
		//}

	}

	private Map<String,String> priceToVeriLoss(PrpLDlossCarMainVo lossCarMainVo,SubmitNextVo nextVo ,String isSubmitHeadOffice){
		String nodeCode = "";
		Map<String,String> nextNodeMap = new TreeMap<String,String>();
		List<PrpLWfTaskVo> taskVoList = new ArrayList<PrpLWfTaskVo>();
		PrpLWfTaskVo taskVo = new PrpLWfTaskVo();
		taskVoList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),lossCarMainVo.getId().toString(),FlowNode.DLCar);
		if(taskVoList!=null && taskVoList.size()>0){
			taskVo = taskVoList.get(0);
		}else{
			taskVoList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),lossCarMainVo.getId().toString(),FlowNode.DLCarMod);
			if(taskVoList!=null && taskVoList.size()>0){
				taskVo = taskVoList.get(0);
			}else{
				taskVoList = wfTaskHandleService.findEndTask(lossCarMainVo.getRegistNo(),lossCarMainVo.getId().toString(),FlowNode.DLCarAdd);
				taskVo = taskVoList.get(0);
			}
		}
		// 如果是公估案件，分配给承保地人员
		if("3".equals(taskVo.getSubCheckFlag())){
			nextVo.setComCode(lossCarMainVo.getComCode());
		}
		boolean autoVeriFlag = false; // 自动核损
		boolean backFlag = false; // 是否退回
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())){
			backFlag = true;
		}
		// 退回的案件 不能自动核价
		// 暂时不自动核价
//		if(!backFlag && ruleVo.getMaxPartFee()<=2000 
//			&& ruleVo.getSumLossFee()<=5000 
//			&& ruleVo.getSelfConfigFlag()==0){
//			
//			if(lossCarMainVo.getSerialNo()==1){
//				if("002".equals(ruleVo.getUseKindCode()) && "8A".equals(ruleVo.getUseNatureCode())){
//					autoVeriFlag = true;
//				}
//			}else{
//				if("A0".equals(ruleVo.getCarKindCode())){
//					autoVeriFlag = true;
//				}
//			}
//		}
		// //TODO 修改定损 不允许自动核价核损
//		BigDecimal veriLossFee = NullToZero(lossCarMainVo.getSumVeriLossFee());
//		if(autoVeriFlag && veriLossFee.compareTo(BigDecimal.ZERO)==1){
//			autoVeriFlag = false;
//		}
		PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,lossCarMainVo.getComCode());
		if(!autoVeriFlag){
			if("1".equals(configValueVo.getConfigValue())){// 使用ilog
			LIlogRuleResVo ilogRuleResVo=null;
			int backLevel=0;
			try{
				ilogRuleResVo=deflossHandleIlogService.organizaVehicleLoss(lossCarMainVo,"1",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode(),isSubmitHeadOffice);
				if(ilogRuleResVo!=null&&"1".equals(ilogRuleResVo.getState())){

					/*	兜底人员权限判断 start  */
					String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
		        	boolean finalAutoPass = true;
		        	if ("1".equals(finalPowerFlag)) {
		        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(nextVo.getUserVo().getUserCode());
		        		if (powerInfoVo != null) {
		        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
		        			if (gradePower != null) {
		        				// 总定损金额
		        				BigDecimal sumAmount = BigDecimal.ZERO;
		        				if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
		        					List<PrpLDlossCarMainVo> prpLDlossCarMains = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
		        					for(PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMains){
		        						if(UnderWriteFlag.MANAL_UNDERWRITE.equals(prpLDlossCarMainVo.getUnderWriteFlag())){

		        							sumAmount = sumAmount
		        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumLossFee()))
		        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumChargeFee()))//费用
		        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumSubRiskFee()))//附加险
		        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumRescueFee()))//施救费
		        									.add(DataUtils.NullToZero(prpLDlossCarMainVo.getSumOutFee()));//外修
		        						}
		        					}
		        				}

		        				if(CodeConstants.JyFlag.NOIN.equals(lossCarMainVo.getFlag())){//不走精友，同意核价提交核损 加上定损费用
		        					sumAmount = sumAmount
		        							.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
		        							.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		        				}else{
		        					if(lossCarMainVo.getSumVeripLoss()!=null
		        							&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossCarMainVo.getCetainLossType())
		        									|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossCarMainVo.getCetainLossType()))){
		        						sumAmount = sumAmount
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))//费用
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()))//附加险
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumRescueFee()))//施救费
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumOutFee()));//外修
		        					}else{
		        						sumAmount = sumAmount
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumChargeFee()))
		        								.add(DataUtils.NullToZero(lossCarMainVo.getSumSubRiskFee()));
		        					}
		        				}

		        				if (sumAmount.compareTo(gradePower) == 1) {
		        					finalAutoPass = false;
		        				}
		        			}
		        		} else {
		        			finalAutoPass = false;
		        		}
		        	}
		            /*	兜底人员权限判断   end   */

					if(("1".equals(ilogRuleResVo.getUnderwriterflag())||"4".equals(ilogRuleResVo.getUnderwriterflag())) && finalAutoPass){
							// 自核通过
							nodeCode = "VLCar_LV"+backLevel;
							nextVo.setAutoLossFlag("1");
					}else{
						try{
							ilogRuleResVo=deflossHandleIlogService.organizaVehicleLoss(lossCarMainVo,"2",nextVo.getUserVo(),new BigDecimal(nextVo.getFlowTaskId()),nextVo.getCurrentNode(),isSubmitHeadOffice);
							if(ilogRuleResVo==null||"0".equals(ilogRuleResVo.getState())){
									throw new IllegalArgumentException("ILOG车辆核损人工权限交互出现异常");
							}
							backLevel = Integer.parseInt(ilogRuleResVo.getMinUndwrtNode());
			                int maxLevel = Integer.parseInt(ilogRuleResVo.getMaxUndwrtNode());
			                nextVo.setVerifyLevel(backLevel+"");
					        nextVo.setMaxLevel(maxLevel+"");
			                boolean haveUser = false;
								while( !haveUser&&backLevel<=maxLevel){// 判断该级别是否有人，没人逐级上传
			                    haveUser = assignService.existsGradeUser(FlowNode.valueOf("VLCar_LV"+backLevel), nextVo.getComCode());
			                    if(!haveUser){
			                    	backLevel ++;
			                    }
			                }
			                if(backLevel > maxLevel){
			                	backLevel = maxLevel;
			                }
					          nodeCode = "VLCar_LV"+backLevel;
							}
							catch(Exception e){
								e.printStackTrace();
								throw new IllegalArgumentException("ILOG车辆核损人工权限交互异常:"+e.getMessage()+"");
							}
						}
				}else{
						throw new IllegalArgumentException("ILOG车辆核损自动权限交互出现异常");
				}
			}
			catch(Exception e){
				e.printStackTrace();
					throw new IllegalArgumentException("ILOG车辆核损交互异常:"+e.getMessage()+"");
			}
			nextVo.setNodeLevel(nodeCode);
			nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
			nextVo.setNodeMap(nextNodeMap);

			}
			// 使用规则引擎
	        PrpLConfigValueVo configRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,lossCarMainVo.getComCode());
	        if("1".equals(configRuleValueVo.getConfigValue())){
				// 规则引擎打开的时候，按照规则引擎规则，需要清空之前ilog赋值的信息
	        	nextNodeMap = new HashMap<String,String>();
	        	nextVo.setAutoLossFlag("0");

				VerifyLossRuleVo ruleVo = this.organizeVerifyLossRule(lossCarMainVo,"1");
				ruleVo.setComCode(nextVo.getComCode());
				ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
				int backLevel = ruleVo.getBackLevel();
				boolean haveUser = false;
				int level = backLevel;
				while( !haveUser&&level<=ruleVo.getMaxLevel()){// 判断该级别是否有人，没人逐级上传
					haveUser = assignService.existsGradeUser(FlowNode.valueOf("VLCar_LV"+level), nextVo.getComCode());
					if(!haveUser){
						level ++;
					}
				}

				nextVo.setVerifyLevel(level+"");
				nextVo.setMaxLevel(ruleVo.getMaxLevel()+"");
				if(level>8){// 大于分公司最高级别8 则提交到分公司最高级别
					level = ruleVo.getMaxLevel();
					ruleVo.setTopComp("1");
					ruleVo = claimRuleApiService.lossCarToVerifyRule(ruleVo);
					nextVo.setVerifyLevel(ruleVo.getBackLevel()+"");
				}
				nodeCode = "VLCar_LV"+level;
				nextVo.setNodeLevel(nodeCode);
				nextNodeMap.put(nodeCode,FlowNode.valueOf(nodeCode).getName());
				nextVo.setNodeMap(nextNodeMap);
			}
		}else{
			nextVo.setAutoLossFlag("1");
		}

		return nextNodeMap;
	}

	/**
	 * 组织核价 核损规则vo veriType 0 核价 1 核损 ☆yangkun(2016年2月27日 下午5:00:38): <br>
	 */
	@Override
	public VerifyLossRuleVo organizeVerifyLossRule(
			PrpLDlossCarMainVo lossMainVo, String veriType) {
		PrpLDlossCarInfoVo carInfoVo = lossMainVo.getLossCarInfoVo();
		VerifyLossRuleVo ruleVo = new VerifyLossRuleVo();
		if(CodeConstants.LossParty.TARGET.equals(lossMainVo.getDeflossCarType())){
			PrpLCMainVo prplcmain = policyViewService.getPolicyAllInfo(lossMainVo.getRegistNo()).get(0);
			PrpLCItemCarVo itemCarVo = prplcmain.getPrpCItemCars().get(0);
			ruleVo.setUseKindCode(itemCarVo.getUseKindCode());
			ruleVo.setUseNatureCode(itemCarVo.getUseNatureCode());
		}else{
			if(lossMainVo.getDeflossCarType().equals("1")){// 如果是标的车则取标的车的数据
				PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(lossMainVo.getRegistNo());
				ruleVo.setCarKindCode(prpLCItemCarVo.getCarType());
			}else{// 如果是三者车则取定损表的数据
				ruleVo.setCarKindCode(carInfoVo.getPlatformCarKindCode());
			}
		}

		BigDecimal sumFee = new BigDecimal("0");
		// 加上上次定损金额
		if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossMainVo.getDeflossSourceFlag())){
			List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainBySerialNo(lossMainVo.getRegistNo(),lossMainVo.getSerialNo());
			for(PrpLDlossCarMainVo prpLDlossCarMainVo : lossCarMainList){
				if("0".equals(veriType) && "1".equals(prpLDlossCarMainVo.getVeriPriceFlag())){
					sumFee = sumFee.add(NullToZero(prpLDlossCarMainVo.getSumVeripLoss()));
				}else if("1".equals(prpLDlossCarMainVo.getUnderWriteFlag())){
					sumFee = sumFee.add(NullToZero(prpLDlossCarMainVo.getSumVeriLossFee()))
							.add(NullToZero(lossMainVo.getSumVeriChargeFee()))
							.add(NullToZero(lossMainVo.getSumVeriSubRiskFee()));
				}
			}
		}

		if("0".equals(veriType)){// 核价
			sumFee = sumFee.add(NullToZero(lossMainVo.getSumCompFee()))
					.add(NullToZero(lossMainVo.getSumMatFee()))
					.subtract(NullToZero(lossMainVo.getSumRemnant()));
			if(sumFee.compareTo(BigDecimal.ZERO) < 0){
				sumFee = BigDecimal.ZERO;
			}
		}else{
			if(CodeConstants.JyFlag.NOIN.equals(lossMainVo.getFlag())){// 不走精友，同意核价提交核损 加上定损费用
				sumFee = sumFee.add(NullToZero(lossMainVo.getSumLossFee()))
						.add(NullToZero(lossMainVo.getSumChargeFee()))
						.add(NullToZero(lossMainVo.getSumSubRiskFee()));
			}else{
				if(lossMainVo.getSumVeripLoss()!=null
						&& (CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(lossMainVo.getCetainLossType())
						|| CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(lossMainVo.getCetainLossType()))){
					sumFee = sumFee.add(lossMainVo.getSumVeripLoss())
							.add(NullToZero(lossMainVo.getSumRepairFee()))
							.add(NullToZero(lossMainVo.getSumOutFee()))
							.add(NullToZero(lossMainVo.getSumChargeFee()))
							.add(NullToZero(lossMainVo.getSumSubRiskFee()));
				}else{
					sumFee = sumFee.add(NullToZero(lossMainVo.getSumLossFee()))
							.add(NullToZero(lossMainVo.getSumChargeFee()))
							.add(NullToZero(lossMainVo.getSumSubRiskFee()));
				}
			}

		}


		ruleVo.setSumLossFee(sumFee.doubleValue());
		// 配件最大金额
		BigDecimal maxCompFee = new BigDecimal("0");
		BigDecimal maxRepairFee = new BigDecimal("0");
		Boolean selfConfigFlag = true ;
		List<PrpLDlossCarCompVo> compList = lossMainVo.getPrpLDlossCarComps();
		for(PrpLDlossCarCompVo compVo : compList){
			if(compVo.getSumDefLoss().compareTo(maxCompFee)==1){
				maxCompFee = compVo.getSumDefLoss();
			}
			if(compVo.getSelfConfigFlag()!=null){
				int quantity = Integer.parseInt(compVo.getSelfConfigFlag());// 0 标准(一次点选); 1 手工; 2 二次点选
				if(quantity >0){
					selfConfigFlag = false;
				}
			}
		}
		if("0".equals(veriType)){
			ruleVo.setMaxPartFee(maxCompFee.doubleValue());
		}else{
			List<PrpLDlossCarRepairVo> repairList = lossMainVo.getPrpLDlossCarRepairs();
			for(PrpLDlossCarRepairVo reparirVo : repairList){
				if(CodeConstants.RepairFlag.INNERREPAIR.equals(reparirVo.getRepairFlag())){
					if(reparirVo.getSumDefLoss().compareTo(maxRepairFee)==1){
						maxRepairFee = reparirVo.getSumDefLoss();
					}

				}
			}
			ruleVo.setMaxPartFee(maxRepairFee.doubleValue());
		}

		if(selfConfigFlag){
			ruleVo.setSelfConfigFlag(new Integer(0));
		}else{
			ruleVo.setSelfConfigFlag(new Integer(1));
		}

		return ruleVo;
	}

	/**
	 * 查询指定定损任务的定损轨迹
	 */
	@Override
	public Map<String, List> showDeflossHis(Long defLossMainId) {

		PrpLDlossCarMainVo lossCarMain = deflossService.findDeflossByPk(defLossMainId);
		// 找出所有环节
		List<PrpLDlossCarMainHisVo> lossMainHisList = deflossService.findDeflossHisByMainId(defLossMainId);
		int count = lossMainHisList.size();
		List<List> processList = new ArrayList();// Object[0]:PrpLDlossCarMainHisVo.id Object[1]:环节名称、人名 Object[2]环节类型
		for (int i = 0; i < count; i++){
			List process = new ArrayList();
			process.add(0, lossMainHisList.get(i).getId());
			String userCode = lossMainHisList.get(i).getCreateUser();
			String userName = userCode;// TODO 名称翻译

			process.add(1, FlowNode.valueOf(lossMainHisList.get(i).getNodeCode()).getName() +"("+userName+")");
			process.add(2, lossMainHisList.get(i).getNodeCode());
			processList.add(process);
		}
		List rowList = new ArrayList();// 行

		// 找出所有换件
		List<PrpLDlossCarCompVo> carCompList = lossCarMain.getPrpLDlossCarComps();
		// 根据换件去找流程的价格
		for(Iterator<PrpLDlossCarCompVo> componentIter = carCompList.iterator();componentIter.hasNext();){
			PrpLDlossCarCompVo carComp = componentIter.next();
			List<String> row = new ArrayList<String>();
			row.add(carComp.getCompName());// 配件名称
			// 查找每个环节的金额
			for(Iterator<List> processIter = processList.iterator();processIter.hasNext();){
				// 找到每个环节的价格，如果没有add("")
				List process = processIter.next();
				Long lossMainHisId = (Long)process.get(0);
				// 根据PrpLDlossCarMainHis表 id 和carCompId 查找
				PrpLDlossCarCompHisVo prpLcomponentHis = deflossService.findCompHisByMainHisId(lossMainHisId,carComp.getId());
				String processType = (String)process.get(2);//Nodename
				if(prpLcomponentHis == null){
					row.add("");
				}else if(FlowNode.DLCar.name().equals(processType)){
					row.add(prpLcomponentHis.getSumDefLoss() == null ? "" : prpLcomponentHis.getSumDefLoss().toString());
				}else if(processType.startsWith(FlowNode.VPCar.name())){
					row.add(prpLcomponentHis.getSumCheckLoss() == null ? "" : prpLcomponentHis.getSumCheckLoss().toString());
				}else if(processType.startsWith(FlowNode.VLCar.name())){
					row.add(prpLcomponentHis.getSumVeriLoss() == null ? "" : prpLcomponentHis.getSumVeriLoss().toString());
				}else{
					row.add("");
				}
			}
			rowList.add(row);// 行的list
		}

		// 找出所有辅料
		List<PrpLDlossCarMaterialVo> materialList = lossCarMain.getPrpLDlossCarMaterials();
		// 根据辅料去找流程的价格
		for(Iterator<PrpLDlossCarMaterialVo> materialIter = materialList.iterator(); materialIter.hasNext(); ){
			PrpLDlossCarMaterialVo carMaterial = materialIter.next();
			List<String> row = new ArrayList<String>();
			row.add(carMaterial.getMaterialName());// 名字
			// 查找每个环节的金额
			for(Iterator<List> processIter = processList.iterator();processIter.hasNext();){
				// 找到每个环节的价格，如果没有add("")
				List process = processIter.next();
				Long lossMainHisId = (Long)process.get(0);
				// 根据PrpLDlossCarMainHis表 id 和 materialId 查找
				PrpLDlossCarMaterialHisVo materialHis = deflossService.findMaterialHisByMainHisId(lossMainHisId,carMaterial.getId());

				String processType = (String)process.get(2);//Nodename
				if(materialHis == null){
					row.add("");
				}else if(FlowNode.valueOf(processType).getUpperNode().equals(FlowNode.DLoss.name())){
					row.add(materialHis.getMaterialFee() == null ? "" : materialHis.getMaterialFee().toString());
				}else if(FlowNode.VPCar.name().equals(processType)){
					row.add(materialHis.getAuditMaterialFee() == null ? "" : materialHis.getAuditMaterialFee().toString());
				}else if(FlowNode.VLCar.name().equals(processType)){
					row.add(materialHis.getAuditLossMaterialFee() == null ? "" : materialHis.getAuditLossMaterialFee().toString());
				}else{
					row.add("");
				}
			}
			rowList.add(row);// 行的list
		}

		// 找出所有修理项目
		List<PrpLDlossCarRepairVo> repairFeeList = lossCarMain.getPrpLDlossCarRepairs();
		// 根据修理项目去找流程的价格
		for(Iterator<PrpLDlossCarRepairVo> repairFeeIter = repairFeeList.iterator(); repairFeeIter.hasNext(); ){
			PrpLDlossCarRepairVo repairFee = repairFeeIter.next();
			List<String> row = new ArrayList<String>();
			row.add(repairFee.getCompName());// 名字
			// 查找每个环节的金额
			for(Iterator<List> processIter = processList.iterator();processIter.hasNext();){
				// 找到每个环节的价格，如果没有add("")
				List process = processIter.next();
				Long lossMainHisId = (Long)process.get(0);
				// 根据PrpLDlossCarMainHis表 id 和carRepairId 查找
				PrpLDlossCarRepairHisVo repairFeeHis = deflossService.findRepairHisByMainHisId(lossMainHisId,repairFee.getId());
				String processType = (String)process.get(2);//Nodename
				if(repairFeeHis == null){
					row.add("");
				}else if(FlowNode.valueOf(processType).getUpperNode().equals(FlowNode.DLoss.name())){
					if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairFeeHis.getRepairFlag())){
						row.add(repairFeeHis.getManHourFee() == null ? "" : repairFeeHis.getManHourFee().toString());
					}else{
						row.add(repairFeeHis.getSumDefLoss() == null ? "" : repairFeeHis.getSumDefLoss().toString());
					}

				}else if(FlowNode.VLCar.name().equals(processType)){
					if(CodeConstants.RepairFlag.INNERREPAIR.equals(repairFeeHis.getRepairFlag())){
						row.add(repairFeeHis.getVeriManHourFee() == null ? "" : repairFeeHis.getVeriManHourFee().toString());
					}else{
						row.add(repairFeeHis.getSumVeriLoss() == null ? "" : repairFeeHis.getSumVeriLoss().toString());
					}
					row.add(repairFeeHis.getVeriManUnitPrice() == null ? "" : repairFeeHis.getVeriManUnitPrice().toString());
				}else{
					row.add("");
				}
			}
			rowList.add(row);// 行的list
		}

		// 组装返回的map
		Map<String, List> deflossHisMap = new HashMap();
		deflossHisMap.put("column", processList);
		deflossHisMap.put("row", rowList);

		return deflossHisMap;

	}

	@Override
	public DeflossActionVo deflossView(String lossCarMainId) {

		DeflossActionVo deflossVo = new DeflossActionVo();
		PrpLDlossCarMainVo lossCarMainVo = deflossService.findDeflossByPk(Long.parseLong(lossCarMainId));
		PrpLDlossCarInfoVo carInfoVo = deflossService.findDefCarInfoByPk(lossCarMainVo.getCarId());
		List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(lossCarMainVo.getId(),lossCarMainVo.getRegistNo(),FlowNode.DLCar.name());

		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(lossCarMainVo.getId(),FlowNode.DLCar.name(),"1");
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(lossCarMainVo.getId(),FlowNode.DLCar.name());
//		List<PrpLDlossChargeVo> checkChargeVo = lossChargeService.findLossChargeVos(lossCarMainVo.getRegistNo(), null,FlowNode.Check.name());
//		if(checkChargeVo != null && !checkChargeVo.isEmpty()){
//			deflossVo.setCheckChargeVo(checkChargeVo.get(0));
//		}

		deflossVo.setLossCarMainVo(lossCarMainVo);
		deflossVo.setCarInfoVo(carInfoVo);
		deflossVo.setClaimTextVo(claimTextVo);
		deflossVo.setClaimTextVos(claimTextVos);
		deflossVo.setLossChargeVos(lossChargeVos);

		String registNo = lossCarMainVo.getRegistNo();

		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		DefCommonVo commonVo = new DefCommonVo();


		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(lossCarMainVo.getRegistNo());
		Map<String,String> thirdCarMap = new HashMap<String,String>();
		PrpLCheckDutyVo checkDutyVo = null ;
		for(PrpLCheckDutyVo checkDuty : checkDutyList){
			if(checkDuty.getSerialNo()==lossCarMainVo.getSerialNo()){
				checkDutyVo = checkDuty;
			}

			if(checkDuty.getSerialNo()!=1){
				thirdCarMap.put(checkDuty.getSerialNo().toString(),checkDuty.getLicenseNo());
			}

		}
		// 事故责任比例
		if(checkDutyVo!=null){
			lossCarMainVo.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
			lossCarMainVo.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
			lossCarMainVo.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
			lossCarMainVo.setIsClaimSelf(checkDutyVo.getIsClaimSelf());
		}


		Map<String,String> kindMap = new HashMap<String,String>();
		List<PrpLCItemKindVo> itemKindVoList = policyViewService.findItemKinds(registNo,null);
		// 保单的车损险和盗抢险保额
		for(PrpLCItemKindVo itemKindVo : itemKindVoList){
			if(CodeConstants.LossParty.TARGET.equals(lossCarMainVo.getDeflossCarType())){
				if("A".equals(itemKindVo.getKindCode())){// 车损险保额
					commonVo.setCarAmount(itemKindVo.getAmount());
				}else if("G".equals(itemKindVo.getKindCode())){// 盗抢险保额
					commonVo.setTheftAmount(itemKindVo.getAmount());
				}
			}

			if(!itemKindVo.getKindCode().endsWith("M") && !CodeConstants.NOSUBRISK_MAP.containsKey(itemKindVo.getKindCode())){
				kindMap.put(itemKindVo.getKindCode(),itemKindVo.getKindName());
			}
		}


		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainByRegistNo(registNo);
		List<PrpLdlossPropMainVo> lossPropMainList = propTaskService.findPropMainListByRegistNo(registNo);
		List<PrpLDlossPersTraceVo> traceList = findPersTraceList(registNo);
		// 已赔付的金额
//		commonVo.setSumpaidDef(getSumPaidDef(lossCarMainList,lossCarMainVo));
		commonVo.setLossCarMainList(lossCarMainList);
		commonVo.setLossPropMainList(lossPropMainList);
		commonVo.setLossPersTraceList(traceList);
		commonVo.setClaimType("0");// 案件类型暂时
		commonVo.setThirdCarMap(thirdCarMap);
		commonVo.setDamageDate(registVo.getDamageTime());

		// 查询代位求偿
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);
		if(subrogationMainVo.getId()==null){
			subrogationMainVo.setSubrogationFlag("0");
		}
		deflossVo.setSubrogationMainVo(subrogationMainVo);
		//end
		deflossVo.setKindMap(kindMap);
		deflossVo.setCommonVo(commonVo);
		deflossVo.setRegistVo(registVo);

		return deflossVo;
	}

	/**
	 * 刷新费用和附加险
	 * @param lossMainId
	 * @modified: ☆YangKun(2016年7月15日 下午7:43:42): <br>
	 */
	@Override
	public DeflossActionVo refreshFee(Long lossMainId,String operateType) {
		DeflossActionVo deflossVo = new DeflossActionVo();
		PrpLDlossCarMainVo lossMainVo = deflossService.findDeflossByPk(lossMainId);
		List<PrpLDlossChargeVo> lossChargeVos = lossChargeService.findLossChargeVos(lossMainId,FlowNode.DLCar.name());
		DefCommonVo commonVo = new DefCommonVo();
		List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(lossMainVo.getRegistNo());
		Map<String,String> thirdCarMap = new HashMap<String,String>();
		//PrpLCheckDutyVo checkDutyVo = null ;
		if(checkDutyList!=null && !checkDutyList.isEmpty()){
			for(PrpLCheckDutyVo checkDuty : checkDutyList){
//				if(checkDuty.getSerialNo()==lossMainVo.getSerialNo()){
//					checkDutyVo = checkDuty;
//				}

				if(checkDuty.getSerialNo()!=1){
					thirdCarMap.put(checkDuty.getSerialNo().toString(),checkDuty.getLicenseNo());
				}
			}
		}
		commonVo.setThirdCarMap(thirdCarMap);
		// 配件险别
		if(StringUtils.isNotBlank(lossMainVo.getLossFeeType())){
			String kindCode = "";
			if(CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossMainVo.getRiskCode()) != null &&
					CodeConstants.ISNEWCLAUSECODE2020_MAP.get(lossMainVo.getRiskCode())){
				kindCode=CodeConstants.LossFee2020Kind_Map.get(lossMainVo.getRiskCode()+lossMainVo.getLossFeeType());
			}else{
				kindCode=CodeConstants.LossFeeKind_Map.get(lossMainVo.getLossFeeType());	
			}
				
			commonVo.setCompKindName(kindCode);
		}


		if("certa".equals(operateType)){
			// 查询代位求偿
			PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(lossMainVo.getRegistNo());
			if(subrogationMainVo!=null && subrogationMainVo.getId()==null){
				subrogationMainVo.setSubrogationFlag("0");
			}
			deflossVo.setSubrogationMainVo(subrogationMainVo);
			List<PrpLCMainVo> cmainList = policyViewService.findPrpLCMainVoListByRegistNo(lossMainVo.getRegistNo());
			for(PrpLCMainVo cmainVo : cmainList){
				if(!Risk.isDQZ(cmainVo.getRiskCode()) && !CodeConstants.ISNEWCLAUSECODE_MAP.get(cmainVo.getRiskCode())){
					List<PrpLcCarDeviceVo> carDeviceList = cmainVo.getPrpLcCarDevices();
					if(carDeviceList!=null && !carDeviceList.isEmpty()){
						Map<String,String> deviceMap = new HashMap<String,String>();
						for(PrpLcCarDeviceVo carDeviceVo : carDeviceList){
							deviceMap.put(carDeviceVo.getDeviceId().toString(),carDeviceVo.getDeviceName());
						}
						commonVo.setDeviceMap(deviceMap);
					}
				}
			}

			// 外修特别处理
			List<PrpLDlossCarRepairVo> carRepairList = lossMainVo.getPrpLDlossCarRepairs();
			if(carRepairList!=null && !carRepairList.isEmpty()){
				List<PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
				for(PrpLDlossCarRepairVo carRepairVo : carRepairList){
					if("1".equals(carRepairVo.getRepairFlag())){
						outRepairList.add(carRepairVo);
					}
				}
				lossMainVo.setOutRepairList(outRepairList);
			}
		}else if("verifyPrice".equals(operateType)){
			// 偏差核价金额 和偏差核价比例
			BigDecimal offsetVeriRate = new BigDecimal("0");
			BigDecimal offsetVeri = new BigDecimal("0");
			PrpLDlossIndexVo lossIndex = lossIndexService.findLossIndex(lossMainVo.getId(),FlowNode.DLCar.name());
			if(lossMainVo.getSumVeripLoss()!=null){
				BigDecimal sumVerip = lossMainVo.getSumVeripLoss() ;//lossCarMainVo.getSumVeripCompFee().add(lossCarMainVo.getSumVeripMatFee());
				offsetVeri = lossIndex.getFirstVeriLoss().subtract(sumVerip).abs();
				if(sumVerip.compareTo(new BigDecimal("0"))!=0){
					offsetVeriRate = offsetVeri.multiply(new BigDecimal("100")).divide(sumVerip,2,BigDecimal.ROUND_HALF_UP);
				}
			}

			commonVo.setOffsetVeri(offsetVeri);
			commonVo.setOffsetVeriRate(offsetVeriRate);
		}else if("verifyLoss".equals(operateType)){
			// 偏差核损金额 和偏差核损比例
			PrpLRegistVo registVo = registQueryService.findByRegistNo(lossMainVo.getRegistNo());
			BigDecimal offsetVeriRate = new BigDecimal("0");
			BigDecimal offsetVeri = new BigDecimal("0");
			PrpLDlossIndexVo lossIndex = lossIndexService.findLossIndex(lossMainVo.getId(),FlowNode.DLCar.name());
			if(lossMainVo.getSumVeriLossFee()!=null){
				offsetVeri = lossIndex.getFirstDefLoss().subtract(lossMainVo.getSumVeriLossFee()).abs();
				if(lossMainVo.getSumVeriLossFee().compareTo(new BigDecimal("0"))!=0){
					offsetVeriRate = offsetVeri.multiply(new BigDecimal("100")).divide(lossMainVo.getSumVeriLossFee(),2,BigDecimal.ROUND_HALF_UP);
				}
			}
			commonVo.setOffsetVeri(offsetVeri);
			commonVo.setOffsetVeriRate(offsetVeriRate);

			List<PrpLDlossCarSubRiskVo> subRiskList = lossMainVo.getPrpLDlossCarSubRisks();
			if(!Risk.isDQZ(registVo.getRiskCode())){
				this.handleSubRisk(registVo, subRiskList);
			}
			// 外修特别处理
			List<PrpLDlossCarRepairVo> carRepairList = lossMainVo.getPrpLDlossCarRepairs();
			if(carRepairList!=null && !carRepairList.isEmpty()){
				List<PrpLDlossCarRepairVo> outRepairList = new ArrayList<PrpLDlossCarRepairVo>();
				for(PrpLDlossCarRepairVo carRepairVo : carRepairList){
					if("1".equals(carRepairVo.getRepairFlag())){
						outRepairList.add(carRepairVo);
					}
				}
				lossMainVo.setOutRepairList(outRepairList);
			}
		}

		deflossVo.setCommonVo(commonVo);
		deflossVo.setLossCarMainVo(lossMainVo);
		deflossVo.setLossChargeVos(lossChargeVos);

		return deflossVo;
	}

	public void isComAssessment(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo){
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
		if(intermMainVo !=null){
			PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId(lossCarMainVo.getRegistNo(), CodeConstants.TaskType.CAR
					,lossCarMainVo.getSerialNo(), intermMainVo.getIntermCode());
			if(assessorOld == null){
				PrpLAssessorVo assessorVo = new PrpLAssessorVo();
				PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(userVo.getUserCode());
				if(intermVo!=null){ // else 接收人是公估人员，处理人不是公估人员
					assessorVo.setIntermId(intermVo.getId());
					assessorVo.setIntermNameDetail(intermVo.getIntermName());
				}else{
					assessorVo.setIntermId(null);
					assessorVo.setIntermNameDetail("接收人是公估人员，处理人不是公估人员");
				}
				assessorVo.setRegistNo(lossCarMainVo.getRegistNo());
				assessorVo.setLossId(lossCarMainVo.getId());
				assessorVo.setTaskType(CodeConstants.TaskType.CAR);
				assessorVo.setUnderWriteFlag(CodeConstants.AssessorUnderWriteFlag.Loss);
				assessorVo.setSerialNo(lossCarMainVo.getSerialNo().toString());
				assessorVo.setLossDate(lossCarMainVo.getDeflossDate());
				assessorVo.setIntermcode(intermMainVo.getIntermCode());
				assessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",intermMainVo.getIntermCode()));
				assessorVo.setLicenseNo(lossCarMainVo.getLicenseNo());
				assessorVo.setCreateTime(new Date());
				assessorVo.setCreateUser(userVo.getUserCode());
				assessorVo.setKindCode(checkHandleService.getCarKindCode(lossCarMainVo.getRegistNo()));
				assessorVo.setComCode(userVo.getComCode());
				assessorVo.setAssessorFee(lossCarMainVo.getAssessorFee());
				assessorVo.setVeriLoss(lossCarMainVo.getSumVeriLossFee());
				assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
			}
		}
	}


	@Override
	public TreeMap<String,String> organizeUserMap(SubmitNextVo nextVo,String auditStatus){
		TreeMap<String,String> userMap = new TreeMap<String,String>();
		FlowNode node;
		if(CodeConstants.AuditStatus.RECHECK.equals(auditStatus)){
			node = FlowNode.Check;
		}else{
			node = FlowNode.DLChk;
		}
		List<AssignUserVo> assignUserVoList=assignService.returnUserVoByGrade(node, nextVo.getComCode());
		if(assignUserVoList!=null){
			for(AssignUserVo assignUserVo:assignUserVoList){
				String userName = codeTranService.transCode("UserCode", assignUserVo.getUserCode());
				userMap.put(assignUserVo.getUserCode(), userName);
			}
		}
		return userMap;
	}

	@Override
	public List<PrpLDlossCarMainVo> findLossCarMainByRegistNo(String registNo) {
		// TODO Auto-generated method stub
		return  deflossService.findLossCarMainByRegistNo(registNo);
	}

	@Override
    public List<PrpLDlossPersTraceMainVo> findlossPersTraceMainByRegistNo(
            String registNo) {
        // TODO Auto-generated method stub
        return persTraceDubboService.findPersTraceMainVoList(registNo);
    }

	@Override
	public List<PrpLDlossPersTraceVo> findPersTraceListByRegistNo(
			String registNo) {
		// TODO Auto-generated method stub
		return findPersTraceList(registNo);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updateLossCarMainInvoiceFee(List<PrpLDlossCarMainVo> PrpLDlossCarMainLsit) {
		if (PrpLDlossCarMainLsit != null && PrpLDlossCarMainLsit.size() > 0) {
			for (PrpLDlossCarMainVo prpLDlossCarMainVo : PrpLDlossCarMainLsit) {
				PrpLDlossCarMain lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, prpLDlossCarMainVo.getId());
				if(prpLDlossCarMainVo.getInvoiceFee()==null&&lossCarMain.getInvoiceFee()==null){
					continue;
				}else{
					lossCarMain.setInvoiceFee(prpLDlossCarMainVo.getInvoiceFee());
					deflossService.updateLossCarMain(lossCarMain);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW )
	public void updatePersTraceInvoiceFee(List<PrpLDlossPersTraceVo> PrpLDlossPersTraceList) {
		if(PrpLDlossPersTraceList != null && PrpLDlossPersTraceList.size() > 0){
			for(PrpLDlossPersTraceVo PrpLDlossPersTraceVo : PrpLDlossPersTraceList){
				PrpLDlossPersTrace prpLDlossPersTrace = databaseDao.findByPK(PrpLDlossPersTrace.class, PrpLDlossPersTraceVo.getId());
				if(PrpLDlossPersTraceVo.getInvoiceFee()==null&&prpLDlossPersTrace.getInvoiceFee()==null){
					continue;
				}else{
					prpLDlossPersTrace.setInvoiceFee(PrpLDlossPersTraceVo.getInvoiceFee());
                    deflossService.updateDlossPersTrace(prpLDlossPersTrace);
				}
			}
		}
	}

	@Override
	public Boolean isAutoVerifyUser(PrpLAutoVerifyVo prpLAutoVerifyVo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("userCode", prpLAutoVerifyVo.getUserCode());
		queryRule.addEqual("validFlag", "1");
		if(prpLAutoVerifyVo.getMoney() != null){
			queryRule.addGreaterEqual("money", prpLAutoVerifyVo.getMoney());
		}
		queryRule.addEqual("node", prpLAutoVerifyVo.getNode());
		List<PrpLAutoVerify> list = databaseDao.findAll(PrpLAutoVerify.class, queryRule);
		System.out.println("系统授权查询结果：userCode=" + prpLAutoVerifyVo.getUserCode() + "  node=" + prpLAutoVerifyVo.getNode() + "  结果集大小为:" + list.size());
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断是否简易赔案方法，返回1代表自动核价核损，2代表自动核损，0是非简易赔案
	 * @param userVo
	 * @param lossCarMainVo
	 * @param taskVo
	 * @return
	 */
	public String isSimpleClaim(SysUserVo userVo,PrpLDlossCarMainVo lossCarMainVo,PrpLWfTaskVo taskVo,String isMobileCheck){
		// 移动查勘的定损任务不走简易
		/*if("Y".equals(isMobileCheck)){
			return "0";
		}*/
		// 推定全损，盗抢折旧不能自动核价核损
		if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(lossCarMainVo.getCetainLossType()) ||
				CodeConstants.CetainLossType.DEFLOSS_ROBBERY.equals(lossCarMainVo.getCetainLossType())){
			return "0";
		}
		// 非核价核损退回案件
		if(CodeConstants.VeriFlag.BACK.equals(lossCarMainVo.getUnderWriteFlag())){
			return "0";
		}
		// 单个车辆定损任务，零配件费用+辅料费用+修理工时费用之和小于等于2000；
		BigDecimal sumFee = new BigDecimal("0");
		// 加上上次定损金额
		/*if(CodeConstants.defLossSourceFlag.ADDTIONALDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
			List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
			for(PrpLDlossCarMainVo prpLDlossCarMainVo : lossCarMainList){
				sumFee = sumFee.add(NullToZero(prpLDlossCarMainVo.getSumCompFee()))
						.add(NullToZero(prpLDlossCarMainVo.getSumMatFee()))
						.add(NullToZero(prpLDlossCarMainVo.getSumRepairFee()));
			}
		}else{
			sumFee = sumFee.add(NullToZero(lossCarMainVo.getSumCompFee()))
					.add(NullToZero(lossCarMainVo.getSumMatFee()))
					.add(NullToZero(lossCarMainVo.getSumRepairFee()));
		}*/
		List<PrpLDlossCarMainVo> lossCarMainList = deflossService.findLossCarMainBySerialNo(lossCarMainVo.getRegistNo(),lossCarMainVo.getSerialNo());
		if(lossCarMainList != null && lossCarMainList.size() > 1){
			for(PrpLDlossCarMainVo prpLDlossCarMainVo : lossCarMainList){
				sumFee = sumFee.add(NullToZero(prpLDlossCarMainVo.getSumCompFee()))
						.add(NullToZero(prpLDlossCarMainVo.getSumMatFee()))
						.add(NullToZero(prpLDlossCarMainVo.getSumRepairFee()));
			}
		}else{
			sumFee = sumFee.add(NullToZero(lossCarMainVo.getSumCompFee()))
					.add(NullToZero(lossCarMainVo.getSumMatFee()))
					.add(NullToZero(lossCarMainVo.getSumRepairFee()));
		}

		Boolean isAutoVerifyUser = false;
		// 定损操作人员在总公司提供的白名单内（具有自动核价核损权限）
		PrpLAutoVerifyVo prpLAutoVerifyVo = new PrpLAutoVerifyVo();
		prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
		prpLAutoVerifyVo.setNode(taskVo.getSubNodeCode());
		prpLAutoVerifyVo.setMoney(sumFee);
		isAutoVerifyUser=this.isAutoVerifyUser(prpLAutoVerifyVo);
		if(!isAutoVerifyUser){
			return "0";
		}

		// 首次定损为自动核价核损，并且定损修改满足自动核价核损
		if(CodeConstants.defLossSourceFlag.MODIFYDEFLOSS.equals(lossCarMainVo.getDeflossSourceFlag())){
			PrpLWfTaskVo uppertaskVo = wfTaskHandleService.queryTask(taskVo.getUpperTaskId().doubleValue());
			if(!FlowNode.VLCar_LV0.name().equals(uppertaskVo.getSubNodeCode())){
				return "0";
			}
		}

		// 单个车辆定损任务，无自定义配件，无二次点选配件；
		List<PrpLDlossCarCompVo> compList = lossCarMainVo.getPrpLDlossCarComps();
		for(PrpLDlossCarCompVo compVo : compList){
			if(compVo.getSelfConfigFlag()!=null){
				int quantity = Integer.parseInt(compVo.getSelfConfigFlag());// 0 标准(一次点选); 1 手工; 2 二次点选
				if(quantity >0){
					return "0";
				}
			}
		}
		// 车辆定损若包含零配件，辅料且金额不为0，定损提交，自动核价核损；
		sumFee = NullToZero(lossCarMainVo.getSumCompFee()).add(NullToZero(lossCarMainVo.getSumMatFee()));
		if(sumFee.compareTo(new BigDecimal(0))>0){
			return "1";
		}else{
			return "2";
		}
	}

	/**
	 * 定损修改，定损金额与核价核损金额一致，且没有操作赔款和费用就返回true
	 * @param lossCarMainVo
	 * @return
	 */
	public Boolean notModPrice(PrpLDlossCarMainVo lossCarMainVo){
		// 是否定损修改
		if(!"3".equals(lossCarMainVo.getDeflossSourceFlag())){
			return false;
		}

		PrpLDlossCarMainHisVo lossCarMainHisVo = deflossService.findDeflossHisVo(lossCarMainVo.getId());
		if(lossCarMainHisVo == null){
			return false;
		}
		// 改了损失类别返回false
		if(!((lossCarMainVo.getLossFeeType()==null&&lossCarMainHisVo.getLossFeeType()==null)||
				(lossCarMainVo.getLossFeeType()!=null&&lossCarMainHisVo.getLossFeeType()!=null&&lossCarMainHisVo.getLossFeeType().equals(lossCarMainVo.getLossFeeType())))){
			return false;
		}

		if(lossCarMainHisVo.getPrpLDlossCarCompHises().size()!=lossCarMainVo.getPrpLDlossCarComps().size()||
				lossCarMainHisVo.getPrpLDlossCarMaterialHises().size()!=lossCarMainVo.getPrpLDlossCarMaterials().size()||
				lossCarMainHisVo.getPrpLDlossCarRepairHises().size()!=lossCarMainVo.getPrpLDlossCarRepairs().size()||
				lossCarMainHisVo.getPrpLDlossCarSubRiskHises().size()!=lossCarMainVo.getPrpLDlossCarSubRisks().size()){
			return false;
		}

		if(NullToZero(lossCarMainVo.getActualValue()).compareTo(NullToZero(lossCarMainHisVo.getActualValue()))!=0||
				NullToZero(lossCarMainVo.getSumRemnant()).compareTo(NullToZero(lossCarMainHisVo.getSumRemnant()))!=0||
				NullToZero(lossCarMainVo.getOtherFee()).compareTo(NullToZero(lossCarMainHisVo.getOtherFee()))!=0||
				NullToZero(lossCarMainVo.getSumLossFee()).compareTo(lossCarMainHisVo.getSumLossFee())!=0||
				NullToZero(lossCarMainVo.getSumRescueFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriRescueFee()))!=0){
			return false;
		}

		// 原定损金额不等于核价核损金额返回false
		// 原定损有核价
		if("1".equals(lossCarMainHisVo.getVeriPriceFlag())&&
				(NullToZero(lossCarMainHisVo.getSumCompFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeripCompFee()))!=0||
				NullToZero(lossCarMainHisVo.getSumMatFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeripMatFee()))!=0||
				NullToZero(lossCarMainHisVo.getSumRemnant()).compareTo(NullToZero(lossCarMainHisVo.getSumVeripRemnant()))!=0)){
			return false;
		}
		if(NullToZero(lossCarMainHisVo.getSumCompFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriCompFee()))!=0||
				NullToZero(lossCarMainHisVo.getSumMatFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriMatFee()))!=0||
				NullToZero(lossCarMainHisVo.getSumRemnant()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriRemnant()))!=0||
				NullToZero(lossCarMainHisVo.getSumRepairFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriRepairFee()))!=0||
				NullToZero(lossCarMainHisVo.getSumRescueFee()).compareTo(NullToZero(lossCarMainHisVo.getSumVeriRescueFee()))!=0){
			return false;
		}

		Long lossMainHisId = lossCarMainHisVo.getId();

		// 零配件数量，定损小计，残值，单价（材料费）不一致返回false
		for(PrpLDlossCarCompVo lossCarCompVo:lossCarMainVo.getPrpLDlossCarComps()){
			PrpLDlossCarCompHisVo lossCarCompHisVo = deflossService.findCompHisByMainHisId(lossMainHisId, lossCarCompVo.getId());
			if(lossCarCompHisVo == null){
				return false;
			}
			if(lossCarCompVo.getQuantity()!=lossCarCompHisVo.getQuantity()||
				NullToZero(lossCarCompVo.getSumDefLoss()).compareTo(NullToZero(lossCarCompHisVo.getSumDefLoss()))!=0||
				NullToZero(lossCarCompVo.getRestFee()).compareTo(NullToZero(lossCarCompHisVo.getRestFee()))!=0||
				NullToZero(lossCarCompVo.getMaterialFee()).compareTo(NullToZero(lossCarCompHisVo.getMaterialFee()))!=0){
				return false;
			}
		}
		// 修理费 工时，工时单价，定损工时总价不一致返回false
		for(PrpLDlossCarRepairVo lossCarRepairVo:lossCarMainVo.getPrpLDlossCarRepairs()){
			PrpLDlossCarRepairHisVo lossCarRepairHisVo = deflossService.findRepairHisByMainHisId(lossMainHisId, lossCarRepairVo.getId());
			if(lossCarRepairHisVo == null){
				return false;
			}
			if(NullToZero(lossCarRepairVo.getManHour()).compareTo(NullToZero(lossCarRepairHisVo.getManHour()))!=0||
				NullToZero(lossCarRepairVo.getManHourUnitPrice()).compareTo(NullToZero(lossCarRepairHisVo.getManHourUnitPrice()))!=0||
				NullToZero(lossCarRepairVo.getSumDefLoss()).compareTo(NullToZero(lossCarRepairHisVo.getSumDefLoss()))!=0){
				return false;
			}
		}

		// 辅料 单价，数量，金额不一致返回false
		for(PrpLDlossCarMaterialVo lossCarMaterialVo:lossCarMainVo.getPrpLDlossCarMaterials()){
			PrpLDlossCarMaterialHisVo lossCarMaterialHisVo = deflossService.findMaterialHisByMainHisId(lossMainHisId, lossCarMaterialVo.getId());
			if(lossCarMaterialHisVo == null){
				return false;
			}
			if(NullToZero(lossCarMaterialVo.getUnitPrice()).compareTo(NullToZero(lossCarMaterialHisVo.getUnitPrice()))!=0||
					lossCarMaterialVo.getAssisCount() != lossCarMaterialHisVo.getAssisCount() ||
					NullToZero(lossCarMaterialVo.getMaterialFee()).compareTo(NullToZero(lossCarMaterialHisVo.getMaterialFee()))!=0){
				return false;
			}
		}
		// 附加险 数量，金额不一致返回false
		for(PrpLDlossCarSubRiskVo lossCarSubRiskVo:lossCarMainVo.getPrpLDlossCarSubRisks()){
			PrpLDlossCarSubRiskHisVo lossCarSubRiskHisVo = deflossService.findSubRiskHisByMainHisId(lossMainHisId, lossCarSubRiskVo.getId());
			if(lossCarSubRiskHisVo == null){
				return false;
			}
			if(NullToZero(lossCarSubRiskVo.getCount()).compareTo(NullToZero(lossCarSubRiskHisVo.getCount()))!=0||
					NullToZero(lossCarSubRiskVo.getSubRiskFee()).compareTo(NullToZero(lossCarSubRiskHisVo.getSubRiskFee()))!=0){
				return false;
			}
			if(!lossCarSubRiskVo.getKindCode().equals(lossCarSubRiskHisVo.getKindCode())){
				return false;
			}
		}
		// 费用不一致返回false
		if(NullToZero(lossCarMainVo.getSumChargeFee()).compareTo(NullToZero(lossCarMainHisVo.getSumChargeFee()))!=0){
			return false;
		}
		List<PrpLDlossChargeVo> lossChargeVoList = lossChargeService.findLossChargeVos(lossCarMainVo.getId(), FlowNode.DLCar.name());
		if(lossChargeVoList != null && lossChargeVoList.size() > 0 ){
			for(PrpLDlossChargeVo lossChargeVo:lossChargeVoList){
				PrpLDlossChargeHisVo lossChargeHisVo = lossChargeService.findLossChargeHisVo(lossChargeVo.getId(), lossChargeVo.getBusinessId());
				if(lossChargeHisVo==null){
					return false;
				}
				if(NullToZero(lossChargeVo.getChargeFee()).compareTo(NullToZero(lossChargeHisVo.getChargeFee()))!=0){
					return false;
				}
				if(!lossChargeVo.getKindCode().equals(lossChargeHisVo.getKindCode())){
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Boolean existTargetCar(String registNo){
		Boolean bl = false;
		List<PrpLWfTaskVo> taskVoList=wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.DLoss.name(), FlowNode.DLCar.name());
		if(taskVoList!=null && taskVoList.size()>0){
			for(PrpLWfTaskVo taskVo:taskVoList){
				// 旧理赔
				int length = registNo.length();
				if(length == 21){
					// 标的车不是注销
					if(!CodeConstants.HandlerStatus.CANCEL.equals(taskVo.getHandlerStatus()) && StringUtils.isNotBlank(taskVo.getHandlerIdKey())){
						PrpLDlossCarMainVo vo = lossCarService.findLossCarMainById(Long.valueOf(taskVo.getHandlerIdKey()));
						if(vo == null){
							PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(Long.valueOf(taskVo.getHandlerIdKey()));
							if(scheduleDefLossVo != null && scheduleDefLossVo.getSerialNo() != null
									&& "1".equals(scheduleDefLossVo.getSerialNo().toString())){
								bl = true;
								break;
							}
						}else if(vo.getSerialNo() != null
								&& "1".equals(vo.getSerialNo().toString())){
							bl = true;
							break;
						}
					}
				}else{
					if(taskVo.getItemName()!=null&&taskVo.getItemName().startsWith("标的车")){
						bl = true;
						break;
					}
				}
			}
		}else{
			bl = false;
		}

		return bl;
	}

    @Override
    public DeflossActionVo isVerifyLossChanged(DeflossActionVo deflossActionVo) {
        if(deflossActionVo!=null){
            if(deflossActionVo.getLossCarMainVo()!=null){
                PrpLDlossCarMainVo dlossCarMain = deflossActionVo.getLossCarMainVo();
                if(dlossCarMain!=null){
                    PrpLDlossCarMainHisVo dlossCarMainHisVo = deflossService.findLastVerPriceDeflossHisVo(dlossCarMain.getId());
                    if(dlossCarMainHisVo!=null){
                        List<PrpLDlossCarMaterialHisVo> prpLDlossCarMaterialHisList = dlossCarMainHisVo.getPrpLDlossCarMaterialHises();
                        List<PrpLDlossCarCompHisVo> prpLDlossCarCompHisList = dlossCarMainHisVo.getPrpLDlossCarCompHises();


                        List<PrpLDlossCarMaterialVo> dlossCarMaterialList = dlossCarMain.getPrpLDlossCarMaterials();
						if(dlossCarMaterialList!=null&&dlossCarMaterialList.size()>0){ // 辅料
                            for(PrpLDlossCarMaterialVo dlossCarMaterialVo:dlossCarMaterialList){
                                if(prpLDlossCarMaterialHisList!=null&&prpLDlossCarMaterialHisList.size()>0){
                                    for(PrpLDlossCarMaterialHisVo prpLDlossCarMaterialHisVo:prpLDlossCarMaterialHisList){
                                        if(dlossCarMaterialVo.getId().equals(prpLDlossCarMaterialHisVo.getMaterialId())){
                                            if(dlossCarMaterialVo.getAuditPrice() != null && prpLDlossCarMaterialHisVo.getAuditPrice() != null){
                                                if(dlossCarMaterialVo.getAuditPrice().compareTo(prpLDlossCarMaterialHisVo.getAuditPrice())!=0){
													dlossCarMaterialVo.setIsVerifyPriceChanged("1"); // 核价颜色改变
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        List<PrpLDlossCarCompVo> prpLDlossCarCompList = dlossCarMain.getPrpLDlossCarComps();
						if(prpLDlossCarCompList!=null&&prpLDlossCarCompList.size()>0){ // 换件
                            for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarCompList){
                                if(prpLDlossCarCompHisList!=null&&prpLDlossCarCompHisList.size()>0){
                                    for(PrpLDlossCarCompHisVo prpLDlossCarCompHisVo:prpLDlossCarCompHisList){
                                        if(prpLDlossCarCompVo.getId().equals(prpLDlossCarCompHisVo.getCarCompId())){
                                            if(prpLDlossCarCompVo.getVeripMaterFee() != null && prpLDlossCarCompHisVo.getVeripMaterFee() != null){
                                                if(prpLDlossCarCompVo.getVeripMaterFee().compareTo(prpLDlossCarCompHisVo.getVeripMaterFee())!=0){
													prpLDlossCarCompVo.setIsVerifyPriceChanged("1"); // 核价颜色改变
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                        PrpLDlossCarMainHisVo  dlossCarMainHisVo2 = deflossService.findLastVerLossDeflossHisVo(dlossCarMain.getId());
                        if(dlossCarMainHisVo2!=null){
                        List<PrpLDlossCarMaterialHisVo> prpLDlossCarMaterialHisList2 = dlossCarMainHisVo2.getPrpLDlossCarMaterialHises();
                        List<PrpLDlossCarMaterialVo> dlossCarMaterialList = dlossCarMain.getPrpLDlossCarMaterials();
						if(dlossCarMaterialList!=null&&dlossCarMaterialList.size()>0){ // 辅料
                            for(PrpLDlossCarMaterialVo dlossCarMaterialVo:dlossCarMaterialList){
                                if(prpLDlossCarMaterialHisList2!=null&&prpLDlossCarMaterialHisList2.size()>0){
                                    for(PrpLDlossCarMaterialHisVo prpLDlossCarMaterialHisVo:prpLDlossCarMaterialHisList2){
                                        if(dlossCarMaterialVo.getId().equals(prpLDlossCarMaterialHisVo.getMaterialId())){
                                           if(dlossCarMaterialVo.getAuditLossPrice() != null && prpLDlossCarMaterialHisVo.getAuditLossPrice() !=null ){
                                                if(dlossCarMaterialVo.getAuditLossPrice().compareTo(prpLDlossCarMaterialHisVo.getAuditLossPrice())!=0){
													dlossCarMaterialVo.setIsVerifyLossChanged("1"); // 核损颜色改变
                                                }
                                           }
                                        }
                                    }
                                }
                            }
                        }
                        List<PrpLDlossCarCompHisVo> prpLDlossCarCompHisList2 = dlossCarMainHisVo2.getPrpLDlossCarCompHises();
                        List<PrpLDlossCarCompVo> prpLDlossCarCompList = dlossCarMain.getPrpLDlossCarComps();
						if(prpLDlossCarCompList!=null&&prpLDlossCarCompList.size()>0){ // 换件
                            for(PrpLDlossCarCompVo prpLDlossCarCompVo:prpLDlossCarCompList){
                                if(prpLDlossCarCompHisList2!=null&&prpLDlossCarCompHisList2.size()>0){
                                    for(PrpLDlossCarCompHisVo prpLDlossCarCompHisVo:prpLDlossCarCompHisList2){
                                        if(prpLDlossCarCompVo.getId().equals(prpLDlossCarCompHisVo.getCarCompId())){
                                            if(prpLDlossCarCompVo.getVeriMaterFee()!=null&&prpLDlossCarCompHisVo.getVeriMaterFee()!=null){
                                                if(prpLDlossCarCompVo.getVeriMaterFee().compareTo(prpLDlossCarCompHisVo.getVeriMaterFee())!=0){
													prpLDlossCarCompVo.setIsVerifyLossChanged("1"); // 核损金额改变
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        List<PrpLDlossCarRepairHisVo> prpLDlossCarRepairHisList = dlossCarMainHisVo2.getPrpLDlossCarRepairHises();
                        List<PrpLDlossCarRepairVo> prpLDlossCarRepairList = dlossCarMain.getPrpLDlossCarRepairs();
						if(prpLDlossCarRepairList!=null&&prpLDlossCarRepairList.size()>0){ // 修理 内修和外修
                            for(PrpLDlossCarRepairVo prpLDlossCarRepairVo:prpLDlossCarRepairList){
                                if(prpLDlossCarRepairHisList!=null&&prpLDlossCarRepairHisList.size()>0){
                                    for(PrpLDlossCarRepairHisVo prpLDlossCarRepairHisVo:prpLDlossCarRepairHisList){
                                        if(prpLDlossCarRepairVo.getId().equals(prpLDlossCarRepairHisVo.getCarRepairId())){
											if("1".equals(prpLDlossCarRepairVo.getRepairFlag())){ // 外修
                                                if(prpLDlossCarRepairVo.getSumVeriLoss()!=null&&prpLDlossCarRepairHisVo.getSumVeriLoss()!=null){
                                                    if(prpLDlossCarRepairVo.getSumVeriLoss().compareTo(prpLDlossCarRepairHisVo.getSumVeriLoss())!=0){
                                                        prpLDlossCarRepairVo.setIsVerifyLossChanged("1");
                                                    }
                                                }
else{ // 内修
                                                    if(prpLDlossCarRepairVo.getVeriManUnitPrice()!=null&&prpLDlossCarRepairHisVo
                                                            .getVeriManUnitPrice()!=null){
                                                        if(prpLDlossCarRepairVo.getVeriManUnitPrice().compareTo(prpLDlossCarRepairHisVo.getVeriManUnitPrice())!=0){
															prpLDlossCarRepairVo.setIsVerifyLossChanged("1"); // 核损金额改变
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        }

                }
            }
        }
        return deflossActionVo;
    }

    @Override
    public void photoVerifyToHNQC(String registNo,String lossMainId,String photoStatus,String checkStatus,
    		String offLineHanding,String dataType,SysUserVo userVo) throws Exception{
    	ReceiveauditingresultVo vo = new ReceiveauditingresultVo();
    	PrplQuickCaseInforVo prplQuickCaseInforVo = submitcaseinforService.findByRegistNo(registNo);
    	if(prplQuickCaseInforVo == null){
			throw new Exception("不是河南快赔案件！");
    	}
    	vo.setCasenumber(prplQuickCaseInforVo.getCasenumber());
    	vo.setInscaseno(registNo);
    	vo.setDatatype(dataType);
    	vo.setIsqualify(checkStatus);
    	vo.setImagetype(photoStatus);
    	vo.setIsass("0".equals(offLineHanding) ? "1":"0");
    	vo.setOperateuser(userVo.getUserCode());
    	try{
    		interfaceAsyncService.receiveauditingresult(vo);
    	}catch(Exception e){
			logger.info("发送定损照片审核结果报错信息： "+e.getMessage());
			e.printStackTrace();
    	}
		// 照片审核不同过才赋值给车损主表的photoStatus字段，审核通过则字段为空
    	if("0".equals(dataType) && "0".equals(checkStatus)){
    		PrpLDlossCarMainVo mainVo = deflossService.findDeflossByPk(Long.valueOf(lossMainId));
    		mainVo.setPhotoStatus(photoStatus);
    		deflossService.updateDefloss(mainVo);
    	}

    }

    @Override
    public void sendHNQC(String registNo,SysUserVo userVo){

    	PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		// 判断是否河南快赔案件
    	if(!"1".equals(registVo.getIsQuickCase())){
    		return ;
    	}

    	List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
		Boolean VLoss = true;// 所有车辆核损通过标识
		if(prpLDlossCarMainVoList != null){
		    for(PrpLDlossCarMainVo prpLDlossCarMainVo:prpLDlossCarMainVoList){
			    if(!CodeConstants.VeriFlag.PASS.equals(prpLDlossCarMainVo.getUnderWriteFlag())){
			    	VLoss = false;
			    	break;
			    }
		    }
	    }
		if(VLoss){// 判断未接收的定损任务 排除复检 财产定损
		    List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService.findUnAcceptTask(registNo,FlowNode.DLoss.name(),FlowNode.DLCar.name());
		    if(prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0){
			    for(PrpLWfTaskVo WfTaskVo:prpLWfTaskVoList){
				    if(!WfTaskVo.getSubNodeCode().equals(FlowNode.DLChk.name()) &&
				    		!WfTaskVo.getSubNodeCode().equals(FlowNode.DLProp.name()) &&
				    		!WfTaskVo.getSubNodeCode().equals(FlowNode.DLPropMod.name()) &&
				    		!WfTaskVo.getSubNodeCode().equals(FlowNode.DLPropAdd.name())){

				    	VLoss =  false;
					    break;
				    }
			    }
		    }
	    }
		if(!VLoss){
			return ;
		}
		// 查询标的车
		PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainBySerialNo(registNo, 1).get(0);
		String photoStatus = "";
		if(prpLDlossCarMainVoList.size()>1){
			photoStatus = "11,12";
		}else{
			photoStatus = "11";
		}

		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);

		// PhotoStatus为空说明没有点照片审核不通过，需要通知快赔照片审核通过
		try{
			if(carMainVo.getPhotoStatus()==null && checkVo.getPhotoStatus()==null){
				photoVerifyToHNQC(registNo, carMainVo.getId().toString(), photoStatus,
						CodeConstants.RadioValue.RADIO_YES, carMainVo.getOffLineHanding(),
						CodeConstants.HNQCDataType.PHOTOVERIFY,userVo);
				photoVerifyToHNQC(registNo, carMainVo.getId().toString(), photoStatus,
						CodeConstants.RadioValue.RADIO_YES, carMainVo.getOffLineHanding(),
						CodeConstants.HNQCDataType.OFFLINEHANDING,userVo);
			}else{
				photoVerifyToHNQC(registNo, carMainVo.getId().toString(), carMainVo.getPhotoStatus(),
						CodeConstants.RadioValue.RADIO_NO, carMainVo.getOffLineHanding(),
						CodeConstants.HNQCDataType.OFFLINEHANDING,userVo);
			}
		}catch(Exception e){
			logger.info("发送定损照片审核结果和是否线下处理报错信息： "+e.getMessage());
			e.printStackTrace();
    	}

		try{
			interfaceAsyncService.receivegusunresult(registNo,userVo);
    	}catch(Exception e){
			logger.info("发送河南快赔定损金额报错信息： "+e.getMessage());
			e.printStackTrace();
    	}
    }

	@Override
	public void cancelCar(String id){
		logger.info("车损id={},进入车损注销任务方法.",id);
		PrpLDlossCarMain lossCarMain = databaseDao.findByPK(PrpLDlossCarMain.class, Long.valueOf(id));
		lossCarMain.setUnderWriteFlag(CodeConstants.VeriFlag.CANCEL);
		logger.info("车损id={}的车损赋值标志位值UnderWriteFlag={}",id,CodeConstants.VeriFlag.CANCEL);
		lossCarMain.setValidFlag("0");
		deflossService.updateLossCarMain(lossCarMain);
		logger.info("车损id={},结束车损注销任务方法,标志位UnderWriteFlag={}",id,lossCarMain.getUnderWriteFlag());
	}

	@Override
	public Boolean isJyTwo(Date reportTime) {
		String JY_TimeStamp = SpringProperties.getProperty("JY_TIMESTAMP");
        Date timeStamp;
        try {
			timeStamp = DateUtils.strToDate(JY_TimeStamp, DateUtils.YToSec);
			if(reportTime.getTime() > timeStamp.getTime()){
	        	return true;
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<PrpLDlossCarMainVo> findLossCarInfoByRegistNo(String registNo) {
		return  deflossService.findLossCarMainInfoByRegistNo(registNo);
	}

	@Override
	public List<PrpLDlossCarMainVo> findAllLossCarMainInfoByRegistNo(List<String> registNos) {
		return  deflossService.findAllLossCarMainInfoByRegistNo(registNos);
	}
	/**
	 * 生成车辆查勘费的任务
	 * @param lossCarMainVo
	 * @param userVo
	 */
	@Override
	public void saveAcheckFeeTask(PrpLDlossCarMainVo lossCarMainVo,SysUserVo userVo){
		PrpdCheckBankMainVo prpdCheckBankMainVo=managerService.findCheckByUserCode(userVo.getUserCode());
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(lossCarMainVo.getRegistNo());
		if(prpdCheckBankMainVo !=null){
			PrpLAcheckVo prpLAcheckOldVo=acheckService.findAcheckByLossId(lossCarMainVo.getRegistNo(),  CodeConstants.CheckTaskType.CAR, lossCarMainVo.getSerialNo(), prpdCheckBankMainVo.getCheckCode());
			if(prpLAcheckOldVo == null){
				PrpLAcheckVo acheckVo = new PrpLAcheckVo();
				acheckVo.setCheckmid(prpdCheckBankMainVo.getId());
				acheckVo.setCheckmnamedetail(prpdCheckBankMainVo.getCheckName());
				acheckVo.setRegistNo(lossCarMainVo.getRegistNo());
				acheckVo.setBussiId(lossCarMainVo.getId());
				acheckVo.setTaskType(CodeConstants.CheckTaskType.CAR);
				acheckVo.setUnderWriteFlag(CodeConstants.AcheckUnderWriteFlag.Loss);
				acheckVo.setSerialNo(lossCarMainVo.getSerialNo().toString());
				acheckVo.setLossDate(lossCarMainVo.getDeflossDate());
				acheckVo.setCheckcode(prpdCheckBankMainVo.getCheckCode());
				acheckVo.setCheckname(codeTranService.transCode("CheckPayCode",prpdCheckBankMainVo.getCheckCode()));
				acheckVo.setLicenseNo(lossCarMainVo.getLicenseNo());
				acheckVo.setCreateTime(new Date());
				acheckVo.setCreateUser(userVo.getUserCode());
				acheckVo.setKindCode(checkHandleService.getCarKindCode(lossCarMainVo.getRegistNo()));
				acheckVo.setComCode(prpLRegistVo.getComCode());
				acheckVo.setCheckfee(lossCarMainVo.getCheckFee());
				acheckVo.setVeriLoss(lossCarMainVo.getSumVeriLossFee());
				acheckService.saveOrUpdatePrpLAcheck(acheckVo,userVo);
				lossCarMainVo.setCheckCode(prpdCheckBankMainVo.getCheckCode());
				deflossService.updateDefloss(lossCarMainVo);
			}
		}
	}

	@Override
	public Object findDlossMain(String taskType,Long mainId) {
		if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
			PrpLDlossCarMainVo  carMainVo = deflossService.findDeflossByPk(mainId);
			return carMainVo;
		} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//财产定损
			PrpLDlossPersTraceMainVo persTranceMainVo = persTranceService.findPersTraceMainVoById(mainId);
			return persTranceMainVo;
		} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//人伤定损
			PrpLdlossPropMainVo propMainMainVo = propLossService.findVoByKey(mainId);
			return propMainMainVo;
		}
		return null;
	}

	/*
	 * @see ins.sino.claimcar.losscar.service.DeflossHandleService#saveAssessorsTask(java.lang.Object, ins.platform.vo.SysUserVo, java.lang.String)
	 * @param mainVo
	 * @param userVo
	 * @param taskType
	 */
	@Override
	public void saveAssessorsTask(Object mainVo,SysUserVo userVo,String taskType) {
		PrpdIntermMainVo intermMainVo=managerService.findIntermByUserCode(userVo.getUserCode());
		String registNo = null;
		Integer serialNo = null;
		String intermCode = null;
		String licenseNo = null;
		String kindCode = null;
		Long lossId = null;
		BigDecimal veriLoss = null;
		BigDecimal assessorFee = null;
		PrpLDlossCarMainVo lossCarMainVo = null;
		PrpLdlossPropMainVo lossPropMainVo = null;
		PrpLDlossPersTraceMainVo persTraceMainVo = null;
		Date lossDate = null;
		if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
			lossCarMainVo = (PrpLDlossCarMainVo)mainVo;
			registNo = lossCarMainVo.getRegistNo();
			intermCode = lossCarMainVo.getIntermCode();
			serialNo = 	lossCarMainVo.getSerialNo();
			lossId = lossCarMainVo.getId();
			licenseNo = lossCarMainVo.getLicenseNo();
			assessorFee = lossCarMainVo.getAssessorFee();
			veriLoss = lossCarMainVo.getSumVeriLossFee();
			lossDate = lossCarMainVo.getDeflossDate();
			kindCode = checkHandleService.getCarKindCode(lossCarMainVo.getRegistNo());
		} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//财损
			lossPropMainVo = (PrpLdlossPropMainVo)mainVo;
			serialNo = lossPropMainVo.getSerialNo();
			lossDate = lossPropMainVo.getDefLossDate();
			lossId = lossPropMainVo.getId();
			registNo = lossPropMainVo.getRegistNo();
			veriLoss = lossPropMainVo.getSumVeriLoss();
			assessorFee = lossPropMainVo.getAssessorFee();
			kindCode = checkHandleService.getCarKindCode(lossPropMainVo.getRegistNo());
		} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){//人伤
			persTraceMainVo = (PrpLDlossPersTraceMainVo)mainVo;
			registNo = persTraceMainVo.getRegistNo();
			lossId = persTraceMainVo.getId();
			lossDate = persTraceMainVo.getCreateTime();
			PrpLRegistVo registVo = registQueryService.findByRegistNo(persTraceMainVo.getRegistNo());
			if(persTraceMainVo.getPrpLDlossPersTraces()!=null && persTraceMainVo.getPrpLDlossPersTraces().size()>0){
				PrpLDlossPersTraceVo persTraceVo = persTraceMainVo.getPrpLDlossPersTraces().get(0);
				if(LossItemParty.OUTSIDE_CAR_PERSON.equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//车外人员
					if("2".equals(registVo.getReportType())){
						kindCode = KINDCODE.KINDCODE_BZ;
					}else{
						kindCode = KINDCODE.KINDCODE_B;
					}
				}else if(LossItemParty.THIS_CAR_PASSAGER.equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车乘客
					kindCode = KINDCODE.KINDCODE_D12;
				}else if(LossItemParty.THIS_CAR_DRIVER.equals(persTraceVo.getPrpLDlossPersInjured().getLossItemType())){//本车司机
					kindCode = KINDCODE.KINDCODE_D11;
				}
			}else{
				if("2".equals(registVo.getReportType())){
					kindCode = KINDCODE.KINDCODE_BZ;
				}else{
					kindCode = KINDCODE.KINDCODE_B;
				}
			}
			veriLoss = BigDecimal.ZERO;
			if(persTraceMainVo.getPrpLDlossPersTraces() != null){
				for(PrpLDlossPersTraceVo prpLDlossPersTrace : persTraceMainVo.getPrpLDlossPersTraces() ){
					if(CodeConstants.CommonConst.TRUE.equals(prpLDlossPersTrace.getValidFlag())){
						veriLoss = veriLoss.add(DataUtils.NullToZero(prpLDlossPersTrace.getSumVeriDefloss()));
					}
				}
			}
		}
		if(intermMainVo !=null){
			PrpLAssessorVo prpLAssessorOldVo = assessorService.findAssessorByLossId(registNo,taskType, serialNo, intermCode);
			if(prpLAssessorOldVo == null){
				PrpLAssessorVo assessorVo = new PrpLAssessorVo();
				assessorVo.setIntermId(intermMainVo.getId());
				assessorVo.setIntermNameDetail(intermMainVo.getIntermName());
				assessorVo.setRegistNo(registNo);
				assessorVo.setLossId(lossId);
				assessorVo.setTaskType(taskType);
				assessorVo.setUnderWriteFlag(CodeConstants.AcheckUnderWriteFlag.Loss);
				assessorVo.setSerialNo((serialNo == null ?  "" : serialNo.toString()));
				assessorVo.setLossDate(lossDate);
				assessorVo.setIntermcode(intermMainVo.getIntermCode());
				assessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",intermMainVo.getIntermCode()));
				assessorVo.setLicenseNo(licenseNo);
				assessorVo.setCreateTime(new Date());
				assessorVo.setCreateUser(userVo.getUserCode());
				assessorVo.setKindCode(kindCode);
				assessorVo.setComCode(userVo.getComCode());
				assessorVo.setAssessorFee(assessorFee);
				assessorVo.setVeriLoss(veriLoss);
				assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
				if (CodeConstants.CheckTaskType.CAR.equals(taskType)){//车辆定损
					lossCarMainVo.setIntermFlag(CodeConstants.CommonConst.TRUE);
					lossCarMainVo.setIntermCode(intermMainVo.getIntermCode());
					deflossService.updateDefloss(lossCarMainVo);
				} else if(CodeConstants.CheckTaskType.PROP.equals(taskType)){//人伤
					lossPropMainVo.setInterMediaryFlag(CodeConstants.CommonConst.TRUE);
					lossPropMainVo.setInterMediaryinfoId(intermMainVo.getIntermCode());
					propLossService.updatePropMain(lossPropMainVo);
				} else if(CodeConstants.CheckTaskType.PERS.equals(taskType)){
					PrpLDlossPersTraceMainVo traceMainVo=new PrpLDlossPersTraceMainVo();
					traceMainVo.setIntermediaryInfoId(intermMainVo.getIntermCode());
					traceMainVo.setId(persTraceMainVo.getId());
					traceMainVo.setIntermediaryFlag(CodeConstants.CommonConst.TRUE);
					persTranceService.saveOrUpdatePersTraceMain(traceMainVo);
				}

			}
		}

	}

}
