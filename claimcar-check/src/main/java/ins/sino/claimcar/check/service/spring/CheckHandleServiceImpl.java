/******************************************************************************
 * CREATETIME : 2015年12月29日 下午12:39:56
 ******************************************************************************/
package ins.sino.claimcar.check.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AcheckUnderWriteFlag;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.CheckTaskType;
import ins.sino.claimcar.CodeConstants.IsSingleAccident;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.ScheduleItemType;
import ins.sino.claimcar.CodeConstants.ScheduleStatus;
import ins.sino.claimcar.CodeConstants.ValidFlag;
import ins.sino.claimcar.carchild.check.vo.CTCarInfoVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carplatform.util.CodeConvertTool;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.check.po.PrpLCheck;
import ins.sino.claimcar.check.po.PrpLCheckCar;
import ins.sino.claimcar.check.po.PrpLCheckCarInfo;
import ins.sino.claimcar.check.po.PrpLCheckDriver;
import ins.sino.claimcar.check.po.PrpLCheckPerson;
import ins.sino.claimcar.check.po.PrpLCheckProp;
import ins.sino.claimcar.check.po.PrpLCheckTask;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckExtVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.service.OtherInterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.lossperson.service.PersReqIlogService;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdCheckBankMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.manager.vo.PrpdIntermServerVo;
import ins.sino.claimcar.mobile.check.vo.CarInfoVo;
import ins.sino.claimcar.mobile.check.vo.PhotoInfo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqBody;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqScheduleItem;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqScheduleWF;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResScheduleItem;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationResVo;
import ins.sino.claimcar.mtainterface.check.vo.MTACarInfoVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorDubboService;
import ins.sino.claimcar.other.vo.PrpLAcheckVo;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.platform.service.CheckToPlatformService;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistRiskInfoService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 查勘预处理服务类
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("checkHandleService")
public class CheckHandleServiceImpl implements CheckHandleService {
	
	private Logger logger = LoggerFactory.getLogger(CheckHandleServiceImpl.class);
	
	@Autowired
	BaseDaoService baseDaoService;
	@Autowired
	CheckService checkService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ClaimTextService claimTextService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ManagerService managerService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	LossChargeService lossChargeService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
	SubrogationService subrogationService;
	@Autowired
	PersTraceDubboService persTraceService;
	@Autowired
	MobileCheckService mobileCheckService;
	@Autowired
	CheckToPlatformService checkToPlatformService;
	@Autowired
	AssessorDubboService assessorService;
	@Autowired
	SysUserService sysUserService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	RegistRiskInfoService registRiskInfoService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	@Autowired
	CertifyPubService certifyPubService;
    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    QuickUserService quickUserService;

    @Autowired
	SubmitcaseinforService submitcaseinforService;
    @Autowired
    PersReqIlogService persReqIlogService;
    @Autowired
	PersTraceHandleService persTraceHandleService;
    @Autowired
	private ClaimTaskService claimService;
    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Autowired
    OtherInterfaceAsyncService otherInterfaceAsyncService;
    @Autowired
    private WFMobileService wFMobileService;
    @Autowired
    private RegistService registService;
    @Autowired
	private IlogRuleService ilogRuleService;

	@Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private AcheckService acheckService;
    
    public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";

	// =================================== 初始化 ================================================

	/* 
	 * @see ins.sino.claimcar.check.service.
	 * CheckHandleService#initCheckByCheck
	 * (java.lang.Long, java.lang.String)
	 * @param checkId
	 * @param registNo
	 * @return
	 * @throws Exception
	 */
	@Override
	public CheckActionVo initCheckByCheck(Long checkId) throws Exception {
		CheckActionVo checkActionVo = new CheckActionVo();
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		String registNo = checkVo.getRegistNo();
		checkActionVo.setCheckTaskId(checkId);
		checkActionVo.setRegistNo(registNo);
		checkActionVo.setCheckflag(true);

		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		List<PrpLClaimTextVo> claimTextVoList = claimTextService.findClaimTextList
				(checkId,registNo,"Check");
		PrpLDisasterVo disasterVo = findDisasterVoByRegistNo(registNo);

		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		checkTaskVo.setId(checkId);

		PrpLCheckCarVo checkMainCarVo = null;// 标的车
		List<PrpLCheckCarVo> checkThirdCarList = new ArrayList<PrpLCheckCarVo>();// 三者车
		List<PrpLCheckPropVo> checkPropList = checkTaskVo.getPrpLCheckProps();// 财产
		List<PrpLCheckPersonVo> checkPersonList = checkTaskVo.getPrpLCheckPersons();// 人伤
		List<PrpLCheckExtVo> checkExtList = checkTaskVo.getPrpLCheckExts();// 扩展信息

		List<PrpLCheckCarVo> checkCarList = checkTaskVo.getPrpLCheckCars();
		for(PrpLCheckCarVo checkCarVo:checkCarList){
			checkCarVo.setCheckTaskId(checkId);
			int serialNo = checkCarVo.getSerialNo();
			if(serialNo==1){
				checkMainCarVo = checkCarVo;
			}else{
				checkThirdCarList.add(checkCarVo);
			}
		}

		// 保单承保险别（主险）
		Map<String,String> kindMap = new HashMap<String,String>();
		// =policyViewService.findItemKinds(registNo,"Y");
		List<PrpLCItemKindVo> cIemKindVoList = this.findItemKind(registNo,null);
		for(PrpLCItemKindVo cIemKindVo:cIemKindVoList){
			String kindCode = cIemKindVo.getKindCode();
			if(!CodeConstants.NOSUBRISK_MAP.containsKey(kindCode)&&!kindCode.endsWith("M")){
				kindMap.put(kindCode,cIemKindVo.getKindName());
			}
		}

		
		// 存在查勘公估费
//		if(CodeConstants.CheckClass.CHECKCLASS_Y.equals(checkVo.getCheckClass())){
//		}
		// 新增需求，查勘可以录入费用
		checkActionVo.setLossChargeVo(lossChargeService.findLossChargeVos(registNo,checkId,FlowNode.Check.name()));

		// 免赔率
		if( !CodeConstants.ReportType.CI.equals(registVo.getReportType())){
			List<PrpLClaimDeductVo> deductVos = new ArrayList<PrpLClaimDeductVo>();
			List<PrpLClaimDeductVo> claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);
			if(claimDeductVoList!=null&&claimDeductVoList.size()>0){
				Map<String,String> deductCondCode = new HashMap<String,String>();
				for(PrpLClaimDeductVo claimDeductVo:claimDeductVoList){
					String code = claimDeductVo.getDeductCondCode();
					boolean flag = true;
					for(String key:deductCondCode.keySet()){
						if(code.equals(key)){
							flag = false;
						}
					}
					if(flag){
						deductVos.add(claimDeductVo);
					}
					// 筛选
					deductCondCode.put(code,code);
				}
			}
			checkActionVo.setClaimDeductVoList(deductVos);
		}

		// 公估服务类型
		Map<String,String> GServiceType = new HashMap<String,String>();
		GServiceType.put("0","查勘");
		GServiceType.put("2","查勘定损");
		
		PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(registNo);// 代位
		
		// 保单信息
		List<PrpLCMainVo> policyInfo = 
		policyViewService.findPrpLCMainVoListByRegistNo(registNo);
		
		if(policyInfo!=null && policyInfo.size()>0){
			// 获取联共保和实收信息
			String[] plyNoArr = {policyInfo.get(0).getPolicyNo()};
			Set<Map<String,String>> sets = policyQueryService.findPrpjpayrefrecBySQL(plyNoArr);
			if(sets !=null && !sets.isEmpty()){
				for(Map<String,String> map3:sets){
					checkActionVo.setPayrefFlag(map3.get("payrefflag"));
				}
			}else{
				checkActionVo.setPayrefFlag("0");
			}
		}

		checkActionVo.setCoinsFlag("0".equals(registVo.getIsGBFlag()) ? "0":"1");
		checkActionVo.setPolicyInfo(policyInfo);
		checkActionVo.setPrpLregistVo(registVo);
		checkActionVo.setPrpLcheckVo(checkVo);
		checkActionVo.setPrpLcheckTaskVo(checkTaskVo);
		checkActionVo.setCheckMainCarVo(checkMainCarVo);
		checkActionVo.setCheckThirdCarList(checkThirdCarList);
		checkActionVo.setCheckDutyVo(checkTaskService.findCheckDuty(registNo,1));
		checkActionVo.setCheckPropList(checkPropList);
		checkActionVo.setCheckPersonList(checkPersonList);
		checkActionVo.setDisasterVo(disasterVo);
		checkActionVo.setClaimTextVoList(claimTextVoList);
		checkActionVo.setCheckExtList(checkExtList);
		checkActionVo.setKindMap(kindMap);
		checkActionVo.setGServiceType(GServiceType);
		checkActionVo.setSubrogationMainVo(subrogationMainVo);

		return checkActionVo;
	}

	// 保单险别
	/* 
	 * @see ins.sino.claimcar.check.service.
	 * CheckHandleService#findItemKind
	 * (java.lang.String, java.lang.String)
	 * @param registNo
	 * @param calculateFlag
	 * @return
	 */
	@Override
	public List<PrpLCItemKindVo> findItemKind(String registNo,String calculateFlag) {
		return policyViewService.findItemKinds(registNo,calculateFlag);
	}

	/* 
	 * @see ins.sino.claimcar.check.service.
	 * CheckHandleService#initCheckBySchedule
	 * (java.lang.Long, java.lang.String, ins.platform.vo.SysUserVo)
	 * @param scheduleTaskId
	 * @param registNo
	 * @param userVo
	 * @return
	 */
	@Override
	public CheckActionVo initCheckBySchedule(Long scheduleTaskId,String registNo,SysUserVo userVo) {
		String userCode = userVo.getUserCode();
		CheckActionVo checkActionVo = new CheckActionVo();
		// 调度信息
		PrpLScheduleTaskVo scheduleTaskVo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
		if(scheduleTaskVo==null){
			throw new IllegalArgumentException("调度提交工作流与调度的数据有错！");
		}
		// 报案信息
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);

		checkActionVo.setPrpLregistVo(registVo);
		checkActionVo.setCheckflag(false);

		// PrpLCheck查勘主表
		PrpLCheckVo checkMainVo = new PrpLCheckVo();
		copyRegistToCheckVo(checkMainVo,registVo,scheduleTaskVo,userVo);

		List<PrpLCheckPropVo> checkPropList = new ArrayList<PrpLCheckPropVo>();

		PrpLCheckCarVo checkMainCarVo = null;// 标的车
		List<PrpLCheckCarVo> checkThirdCarList = new ArrayList<PrpLCheckCarVo>();// 三者车

		// 查询调度，去报案取值
		for(PrpLScheduleItemsVo schduItemVo:scheduleTaskVo.getPrpLScheduleItemses()){
			String itemType = schduItemVo.getItemType();// 调度查勘类型
			if((ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR.equals(itemType)||( ScheduleItemType.SCHEDULEITEMTYPE_THIRDCAR )
					.equals(itemType) )&&(CodeConstants.ScheduleStatus.CHECK_SCHEDULED.equals(schduItemVo.getScheduleStatus())
							||CodeConstants.ScheduleStatus.SCHEDULED_CHANGE.equals(schduItemVo.getScheduleStatus()))){
				PrpLCheckCarVo prpLcheckCarVo = null;
				// 根据调度找到报案对应的车
				PrpLRegistCarLossVo registCarLossVo = registQueryService.findRegistCarLossById(schduItemVo
						.getItemsSourceId());
				// 承保标的车信息
				if(( ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR ).equals(schduItemVo.getSerialNo())){
					List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(scheduleTaskVo.getRegistNo());
					if(cMainList!=null&&cMainList.size()>0){// 临时案件，无保单报案
						PrpLCMainVo cmainVo = cMainList.get(0);
						prpLcheckCarVo = createCheckCarVo(scheduleTaskVo,schduItemVo,registVo,registCarLossVo,cmainVo,userCode);
						checkMainCarVo = prpLcheckCarVo;
						// 获取联共保和实收信息
						String[] plyNoArr = {cmainVo.getPolicyNo()};
						Set<Map<String,String>> sets = policyQueryService.findPrpjpayrefrecBySQL(plyNoArr);
						if(sets !=null && !sets.isEmpty()){
							for(Map<String,String> map3:sets){
								checkActionVo.setPayrefFlag(map3.get("payrefflag"));
							}
						}else{
							checkActionVo.setPayrefFlag("0");
						}
						checkActionVo.setCoinsFlag("0".equals(registVo.getIsGBFlag()) ? "0":"1");
					}
					
					/*
					 * // 事故责任比例 PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(schduItemVo.getRegistNo(),Integer.valueOf(schduItemVo.getSerialNo())); if(checkDutyVo==null){ checkDutyVo
					 * = new PrpLCheckDutyVo(); checkDutyVo.setCheckCarId(checkCarVo.getCarid()); checkDutyVo.setCarType(checkCarVo.getSerialNo().toString()); checkDutyVo.setRegistNo(registNo); --- }
					 */
				}else{
					if(!"0".equals(schduItemVo.getFlag())){
						prpLcheckCarVo = createCheckCarVo(scheduleTaskVo,schduItemVo,registVo,registCarLossVo,null,userCode);
						checkThirdCarList.add(prpLcheckCarVo);
					}
				}

			}else if(ScheduleItemType.SCHEDULEITEMTYPE_PROP.equals(itemType)
					&&(CodeConstants.ScheduleStatus.CHECK_SCHEDULED.equals(schduItemVo.getScheduleStatus())
					||CodeConstants.ScheduleStatus.SCHEDULED_CHANGE.equals(schduItemVo.getScheduleStatus()))){
				PrpLCheckPropVo checkPropVo = new PrpLCheckPropVo();
				PrpLRegistPropLossVo registPropLossVo = registQueryService.findRegistPropLossById(schduItemVo
						.getItemsSourceId());

				checkPropVo.setScheduleitem(schduItemVo.getId());// 来源id
				String serialNo = StringUtils.isEmpty(schduItemVo.getSerialNo())
						? "0" : schduItemVo.getSerialNo();
				checkPropVo.setLossPartyId(Long.parseLong(serialNo));// 损失方
				checkPropVo.setLossPartyName(schduItemVo.getLicenseNo());// 损失方名称

				checkPropVo.setLossItemName(registPropLossVo.getLossitemname());// 损失财产名称
				// checkPropVo.setLossDegreeCode(registPropLossVo.getDamagelevel());// 损失程度
				checkPropVo.setValidFlag(CodeConstants.ValidFlag.VALID);
				checkPropVo.setLossItemName(registPropLossVo.getLossitemname());
				// checkPropVo.setLossDegreeCode(registPropLossVo.getDamagelevel());

				checkPropList.add(checkPropVo);
			}
		}

		checkActionVo.setPrpLcheckVo(checkMainVo);
		// checkActionVo.setPrpLcheckTaskVo(checkTaskVo);
		checkActionVo.setCheckMainCarVo(checkMainCarVo);
		checkActionVo.setCheckThirdCarList(checkThirdCarList);
		checkActionVo.setCheckPropList(checkPropList);

		List<PrpLCheckPersonVo> checkPersonVoList = new ArrayList<PrpLCheckPersonVo>();
//		PrpLCheckPersonVo checkPersonVo = new PrpLCheckPersonVo();
//		checkPersonVoList.add(checkPersonVo);
		checkActionVo.setCheckPersonList(checkPersonVoList);
		return checkActionVo;
	}

	@Override
	public PrpLCheckCarVo initPrpLCheckCar(Long carId) {
		return checkService.initPrpLCheckCar(carId);
	};

	@Override
	public Long getCheckId(String registNo) {
		return checkService.getCheckId(registNo);
	}

	// =================================== 编辑 保存 ================================================

	@Override
	public void updateCheckMain(PrpLCheckVo checkVo){
		checkService.updateCheckMain(checkVo);
	}

	/* 
	 * @see ins.sino.claimcar.check.service.
	 * CheckHandleService#saveCheckOnAccept
	 * (ins.sino.claimcar.check.vo.CheckActionVo, ins.platform.vo.SysUserVo)
	 * @param checkActionVo
	 * @param userVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public Long saveCheckOnAccept(CheckActionVo checkActionVo,SysUserVo userVo) throws Exception {
		String userCode = userVo.getUserCode();
		String userName = userVo.getUserName();
		Date now = new Date();
		PrpLCheckVo checkMainVo = checkActionVo.getPrpLcheckVo();
		
		// 组织PO,用于保存
		PrpLCheck checkPo = new PrpLCheck();
		Beans.copy().from(checkMainVo).to(checkPo);
		checkPo.setCreateTime(now);
		checkPo.setCreateUser(userCode);
		checkPo.setUpdateTime(now);
		checkPo.setUpdateUser(userCode);

		PrpLCheckTask checkTaskPo = new PrpLCheckTask();
		Beans.copy().from(checkMainVo.getPrpLCheckTask()).to(checkTaskPo);
		checkTaskPo.setCheckerCode(userCode);// 查勘人代码
		checkTaskPo.setChecker(userName);// 查勘人名称
		checkTaskPo.setCreateUser(userCode);
		checkTaskPo.setCreateTime(now);
		checkTaskPo.setUpdateUser(userCode);
		checkTaskPo.setUpdateTime(now);
		checkTaskPo.setPrpLCheck(checkPo);
		checkTaskPo.setPrpLCheck(checkPo);

		List<PrpLCheckCar> checkCarPoList = new ArrayList<PrpLCheckCar>();// 车损
		List<PrpLCheckProp> checkPropPoList = new ArrayList<PrpLCheckProp>();// 物损

		// 标的车
		PrpLCheckCarVo mainCarVo = checkActionVo.getCheckMainCarVo();
		PrpLCheckCar mainCarPo = copyCarPoFromCarVo(mainCarVo,userCode);
		mainCarPo.setPrpLCheckTask(checkTaskPo);
		checkCarPoList.add(mainCarPo);
		PrpLCheckDutyVo checkDutyVo = this.createCarDuty(mainCarVo,userCode);// 事故责任比例
		checkDutyVo.setIsClaimSelf(checkPo.getIsClaimSelf());
		checkTaskService.saveCheckDuty(checkDutyVo);

		// 三者车
		for(PrpLCheckCarVo thirdCarVo:checkActionVo.getCheckThirdCarList()){
			PrpLCheckCar thirdCarPo = copyCarPoFromCarVo(thirdCarVo,userCode);
			thirdCarPo.setPrpLCheckTask(checkTaskPo);
			checkCarPoList.add(thirdCarPo);
			PrpLCheckDutyVo checkDutyVos = this.createCarDuty(thirdCarVo,userCode);// 事故责任比例
			checkDutyVos.setIsClaimSelf(checkPo.getIsClaimSelf());
			checkTaskService.saveCheckDuty(checkDutyVos);
		}

		// 财产
		for(PrpLCheckPropVo checkPropVo:checkActionVo.getCheckPropList()){
			PrpLCheckProp checkPropPo = new PrpLCheckProp();
			Beans.copy().from(checkPropVo).to(checkPropPo);
			checkPropPo.setRegistNo(checkMainVo.getRegistNo());
			checkPropPo.setPrpLCheckTask(checkTaskPo);
			checkPropPo.setCreateUser(userCode);
			checkPropPo.setCreateTime(now);
			checkPropPo.setUpdateUser(userCode);
			checkPropPo.setUpdateTime(now);
			checkPropPoList.add(checkPropPo);
		}

		checkTaskPo.setPrpLCheckCars(checkCarPoList);
		checkTaskPo.setPrpLCheckProps(checkPropPoList);
		checkPo.setPrpLCheckTask(checkTaskPo);
		
		return checkService.saveCheck(checkPo);
	}

	// 创建prpLcheckDuty
	private PrpLCheckDutyVo createCarDuty(PrpLCheckCarVo checkCarVo,String userCode) {
		logger.info("报案号={}，进入创建prpLcheckDuty回写标志位ValidFlag的方法",checkCarVo.getRegistNo());
		PrpLCheckDutyVo checkDutyVo = checkTaskService.findCheckDuty(checkCarVo.getRegistNo(),
				Integer.valueOf(checkCarVo.getSerialNo()));
		if(checkDutyVo==null){
			checkDutyVo = new PrpLCheckDutyVo();
			checkDutyVo.setRegistNo(checkCarVo.getRegistNo());
			checkDutyVo.setCarType(checkCarVo.getSerialNo().toString());

			PrpLRegistVo registVo = registQueryService.findByRegistNo(checkCarVo.getRegistNo());
			PrpLRegistExtVo registExt = registVo.getPrpLRegistExt();
			if(checkCarVo.getSerialNo()==1){
				String duty = registExt.getObliGation();
				checkDutyVo.setIndemnityDuty(duty);
				if("0".equals(duty)){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(100.0));
				}else if("1".equals(duty)){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(70.0));
				}else if("2".equals(duty)){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(50.0));
				}else if("3".equals(duty)){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(30.0));
				}else if("4".equals(duty)){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(0.0));
				}
				checkDutyVo.setCiDutyFlag(CodeConstants.DutyType.CIINDEMDUTY_Y);
			}
//			checkDutyVo.setLicenseNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
//			checkDutyVo.setValidFlag(CodeConstants.ValidFlag.VALID);
			checkDutyVo.setSerialNo(checkCarVo.getSerialNo());
			checkDutyVo.setCreateTime(new Date());
			checkDutyVo.setCreateUser(userCode);
		}else{
			checkDutyVo.setUpdateUser(userCode);
			checkDutyVo.setUpdateTime(new Date());
		}
		checkDutyVo.setLicenseNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());
		checkDutyVo.setValidFlag(CodeConstants.ValidFlag.VALID);
		logger.info("报案号={},结束创建prpLcheckDuty的方法,赋值的ValidFlag={}",checkCarVo.getRegistNo(),CodeConstants.ValidFlag.VALID);
		return checkDutyVo;
	};

	// 更新标的车Duty
	/* 
	 * @see ins.sino.claimcar.check.service.
	 * CheckHandleService#updateMainCarDuty
	 * (ins.sino.claimcar.check.vo.PrpLCheckDutyVo, 
	 * ins.sino.claimcar.check.vo.PrpLCheckVo, java.lang.String)
	 * @param checkDutyVo
	 * @param checkVo
	 * @param userCode
	 */
	@Override
	public void updateMainCarDuty(PrpLCheckDutyVo checkDutyVo,PrpLCheckVo checkVo,String userCode) {
		PrpLCheckDutyVo checkDuty = checkTaskService.findCheckDuty(checkVo.getRegistNo(),1);
		List<PrpLCheckCarVo> checkCarVoList = findPrpLcheckCarVoByRegistNo(checkVo.getRegistNo());
		if(checkVo!=null&&checkDuty!=null&&StringUtils.isNotEmpty(checkVo.getIsClaimSelf())){
			checkDuty.setIsClaimSelf(checkVo.getIsClaimSelf());// 互碰自赔
		}else{
			PrpLCheckCarVo checkCarVo = null;
			for(PrpLCheckCarVo checkCarVos:checkCarVoList){
				if(checkCarVos.getSerialNo()==1){
					checkCarVo = new PrpLCheckCarVo();
					checkCarVo = checkCarVos;
				}
			}
			checkDuty = this.createCarDuty(checkCarVo,userCode);
		}
		checkDuty.setCiDutyFlag(checkDutyVo.getCiDutyFlag());// 交强险是否有责
		checkDuty.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
		checkDuty.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
		checkTaskService.saveCheckDuty(checkDuty);
	}

	// 拷贝
	private PrpLCheckCar copyCarPoFromCarVo(PrpLCheckCarVo checkCarVo,String assignUser) throws Exception {
		Date now = new Date();

		PrpLCheckCar checkCarPo = new PrpLCheckCar();
		Beans.copy().from(checkCarVo).to(checkCarPo);

		checkCarPo.setCreateUser(assignUser);
		checkCarPo.setCreateTime(now);
		checkCarPo.setUpdateUser(assignUser);
		checkCarPo.setUpdateTime(now);

		PrpLCheckCarInfo carInfoPo = new PrpLCheckCarInfo();
		if(checkCarVo==null){
			throw new Exception("报案不存在标的车！");
		}
		Beans.copy().from(checkCarVo.getPrpLCheckCarInfo()).to(carInfoPo);
		carInfoPo.setPrpLCheckCar(checkCarPo);
		carInfoPo.setRegistNo(checkCarVo.getRegistNo());
		checkCarPo.setPrpLCheckCarInfo(carInfoPo);

		PrpLCheckDriver driverPo = new PrpLCheckDriver();
		Beans.copy().from(checkCarVo.getPrpLCheckDriver()).to(driverPo);
		driverPo.setPrpLCheckCar(checkCarPo);
		driverPo.setRegistNo(checkCarVo.getRegistNo());
		checkCarPo.setPrpLCheckDriver(driverPo);

		return checkCarPo;
	}

	@Override
	public Long savePrpLCheckCar(PrpLCheckCarVo prpLCheckCarVo,PrpLCheckDutyVo checkDutyVo,
			String userCode,String isMobileCase) throws Exception {
		PrpLCheckCar prpLcheckCar = new PrpLCheckCar();
		Beans.copy().from(prpLCheckCarVo).to(prpLcheckCar);
		String regNo = prpLCheckCarVo.getRegistNo();
		PrpLCheckTask prpLcheckTask = checkService.findCheckTaskByRegistNo(regNo);
		prpLcheckCar.setPrpLCheckTask(prpLcheckTask);
		prpLcheckCar.setCreateUser(userCode);
		prpLcheckCar.setFlag(CodeConstants.ValidFlag.VALID);
		prpLcheckCar.setCreateTime(new Date());
		prpLcheckCar.setUpdateUser(userCode);
		prpLcheckCar.setUpdateTime(new Date());
		// 获取serialNo的最大值start
		// 查勘的序号
        List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(regNo,"serialNo");
		// 调度序号
        List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(regNo);
        Integer serialNo = 0;
        for(PrpLScheduleDefLossVo prpLScheduleDefLossVo : prpLScheduleDefLossVoList){
            if(serialNo < prpLScheduleDefLossVo.getSerialNo()){
                serialNo = prpLScheduleDefLossVo.getSerialNo();
            }
        }
        if(prpLCheckCarVos != null && prpLCheckCarVos.size() > 0){
            if(serialNo < prpLCheckCarVos.get(0).getSerialNo()){
                serialNo = prpLCheckCarVos.get(0).getSerialNo();
            }
        }
		// 获取serialNo的最大值end
        prpLcheckCar.setSerialNo(serialNo+1);
		//prpLcheckCar.setSerialNo(checkService.getMaxSerialNo(regNo)+1);
		prpLcheckCar.setValidFlag(CodeConstants.ValidFlag.VALID);

		PrpLCheckCarInfo checkCarInfo = new PrpLCheckCarInfo();
		Beans.copy().from(prpLCheckCarVo.getPrpLCheckCarInfo()).to(checkCarInfo);
		checkCarInfo.setRegistNo(prpLCheckCarVo.getRegistNo());
		checkCarInfo.setValidFlag(CodeConstants.ValidFlag.VALID);
		checkCarInfo.setPrpLCheckCar(prpLcheckCar);

		PrpLCheckDriver checkDriver = new PrpLCheckDriver();
		Beans.copy().from(prpLCheckCarVo.getPrpLCheckDriver()).to(checkDriver);
		checkDriver.setRegistNo(prpLCheckCarVo.getRegistNo());
		checkDriver.setValidFlag(CodeConstants.ValidFlag.VALID);
		checkDriver.setPrpLCheckCar(prpLcheckCar);
		if(StringUtils.isNotBlank(checkDriver.getIdentifyNumber())){
			// 驾驶员身份证去空格和制表符 防止送平台校验不通过
			checkDriver.setIdentifyNumber(checkDriver.getIdentifyNumber().replaceAll("\\s*",""));
		}
		if(StringUtils.isNotBlank(checkDriver.getDrivingLicenseNo())){
			// 驾驶员证去空格和制表符 防止送平台校验不通过
			checkDriver.setDrivingLicenseNo(checkDriver.getDrivingLicenseNo().replaceAll("\\s*",""));
		}

		prpLcheckCar.setPrpLCheckCarInfo(checkCarInfo);
		prpLcheckCar.setPrpLCheckDriver(checkDriver);

		checkService.saveCheckCar(prpLcheckCar);

		// 保存checkDuty
		PrpLCheckDutyVo checkDuty = this.createCarDuty(prpLCheckCarVo,userCode);
		checkDuty.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
		checkDuty.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
		// 移动查勘案件特殊处理
		if("1".equals(isMobileCase)){
			checkDuty.setIsClaimSelf(checkDutyVo.getIsClaimSelf());
		}
		checkTaskService.saveCheckDuty(checkDuty);

		return prpLcheckCar.getCarid();
	};

	@Override
	public Long updatePrpLCheckCar(PrpLCheckCarVo checkCarVo,PrpLCheckDutyVo checkDutyVo,String isMobileCase) throws Exception {
		// 更新车辆信息
		PrpLCheckCar checkCar = checkService.updateCheckCar(checkCarVo);

		String registNo = checkCar.getRegistNo();
		Integer serialNo = checkCar.getSerialNo();
		PrpLCheckDutyVo checkDuty = checkTaskService.findCheckDuty(registNo,serialNo);
		// 更新checkDuty
		if(checkDuty==null){
			checkDuty = new PrpLCheckDutyVo();
		}
		checkDuty.setRegistNo(registNo);
		checkDuty.setSerialNo(serialNo);
		checkDuty.setCheckCarId(checkCarVo.getCarid());
		// 移动端案件特殊处理
		if("1".equals(isMobileCase)){
			checkDuty.setIsClaimSelf(checkDutyVo.getIsClaimSelf());
		}else{
			checkDuty.setIsClaimSelf(checkService.getIsClaimSelf(registNo));
		}
		checkDuty.setLicenseNo(checkCar.getPrpLCheckCarInfo().getLicenseNo());
		checkDuty.setIndemnityDuty(checkDutyVo.getIndemnityDuty());
		checkDuty.setIndemnityDutyRate(checkDutyVo.getIndemnityDutyRate());
		checkDuty.setUpdateUser(checkCarVo.getUpdateUser());
		checkDuty.setUpdateTime(new Date());
		if(checkCarVo.getSerialNo()==1){
			checkDuty.setCiDutyFlag(checkDutyVo.getCiDutyFlag());
		}
		checkTaskService.saveCheckDuty(checkDuty);
		return checkCar.getCarid();
	};

	@Override
	public void updateCheckCar(PrpLCheckCarVo checkCarVo) {
		PrpLCheckCar checkCar = checkService.updateCheckCar(checkCarVo);
	}

	public static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	}

	/* 
	 * 查勘保存
	 * @param prpLcheckVo
	 * @param disasterVo
	 * @param userVo
	 * @param saveType
	 * @return
	 * @throws Exception
	 */
	@Override
	public Long save(PrpLCheckVo checkVo,PrpLDisasterVo disasterVo,
			SysUserVo userVo,String saveType) throws Exception {
		String userCode = userVo.getUserCode();
		String registNo = checkVo.getRegistNo();
		// 巨灾
		this.saveDisaster(disasterVo,registNo,userCode);
		// 保存公估信息
//		this.saveAssessor(registNo,userVo);
		return checkService.save(checkVo,disasterVo,userVo,saveType);
	}

	// flag 节点未完成0,完成之后为1
	@Override
	public PrpLClaimTextVo createClaimText(Long checkId,String AuditStatus,String nodeCode,
			String flag,SysUserVo userVo) throws Exception {
		String userCode = userVo.getUserCode();
		String comCode = userVo.getComCode();
		Date date = new Date();
		PrpLClaimTextVo claimTextVo = claimTextService.findClaimTextByNode(checkId,nodeCode,"0");
		if(claimTextVo == null && !FlowNode.Chk.name().equals(nodeCode)){
			claimTextVo = claimTextService.findClaimTextByNode(checkId,nodeCode,"1");
		}
		PrpLCheck checkPo = checkService.findPrpLCheckById(checkId);
		String contexts = checkPo.getPrpLCheckTask().getContexts();// 意见
		if(claimTextVo==null){
			claimTextVo = new PrpLClaimTextVo();claimTextVo.setBussTaskId(checkPo.getId());
			claimTextVo.setRegistNo(checkPo.getRegistNo());claimTextVo.setBussNo(checkPo.getRegistNo());
			claimTextVo.setTextType(CodeConstants.ClaimText.OPINION);claimTextVo.setNodeCode(nodeCode.toString());
			claimTextVo.setOperatorCode(userCode);//claimTextVo.setOperatorName(userName);
			claimTextVo.setOperatorName(userVo.getUserName());
			claimTextVo.setComCode(comCode);//claimTextVo.setComName(comName);
			claimTextVo.setInputTime(date);claimTextVo.setBigNode(FlowNode.valueOf(nodeCode).getUpperNode().toString());
			claimTextVo.setDescription(contexts);claimTextVo.setStatus(AuditStatus);
			claimTextVo.setFlag(flag);claimTextVo.setCreateUser(userCode);
			claimTextVo.setCreateTime(date);claimTextVo.setUpdateUser(userCode);
			claimTextVo.setUpdateTime(date);
		}else{
			claimTextVo.setDescription(contexts);claimTextVo.setStatus(AuditStatus);
			claimTextVo.setFlag(flag);claimTextVo.setInputTime(date);
			claimTextVo.setUpdateUser(userCode);claimTextVo.setUpdateTime(date);
		}
		return claimTextVo;
	}

	private void saveDisaster(PrpLDisasterVo disasterVo,String registNo,String userCode) {
		Date date = new Date();
		if(disasterVo!=null)
		{
			if(disasterVo.getId()!=null)
			{
				disasterVo.setUpdateUser(userCode);
				disasterVo.setUpdateTime(date);
			}
			else
			{
				disasterVo.setRegistNo(registNo);
				disasterVo.setValidFlag(CodeConstants.ValidFlag.VALID);
				disasterVo.setCreateUser(userCode);
				disasterVo.setCreateTime(date);
				disasterVo.setUpdateUser(userCode);
				disasterVo.setUpdateTime(date);
			}
			saveDisaster(disasterVo);
		}
	}

	@Override
	public boolean getCarKind(String registNo) {
		List<PrpLCItemKindVo> cIemKindVoList = policyViewService.findItemKinds(registNo,"N");
		// 判断是否承保车上货物险
		boolean kindFlag = false;// 未承保
		for(PrpLCItemKindVo cIemKindVo:cIemKindVoList){
			String kindCode = cIemKindVo.getKindCode();
			if(StringUtils.isNotEmpty(kindCode)&&"D2".equals(kindCode)){
				kindFlag = true;
			}
		}
		return kindFlag;
	}
	
	/**
	 * 查勘提交汇总
	 * @param PrpLCheckVo,url
	 */
	@Override
	public List<PrpLScheduleDefLossVo> initCheckSubmitDloss(PrpLCheckVo checkVo,String url) throws Exception {
		List<PrpLScheduleDefLossVo> prpLScheduleDefLossList =  new ArrayList<PrpLScheduleDefLossVo>();
		//prpLScheduleDefLossList = scheduleService.findPrpLScheduleDefLossList(checkVo.getRegistNo());
		/*if(prpLScheduleDefLossList == null || prpLScheduleDefLossList.size() == 0){
			
		}*/
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		Long checkTaskId = checkTaskVo.getId();
		// 车损保存调度定损准备
		List<PrpLCheckCarVo> checkCarVoList = checkTaskVo.getPrpLCheckCars();
		// Long schTaskId = null;// 调度定损id
		if(checkCarVoList != null && checkCarVoList.size() > 0){
			for(PrpLCheckCarVo checkCarVo : checkCarVoList){
				PrpLScheduleDefLossVo defLossVo = null;// 三者车没有损失不生成定损任务,互碰自赔三者车不生成定损任务
				// 判断是否已生成定损任务
				Long scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),
						checkCarVo.getSerialNo(),ScheduleStatus.DEFLOSS_SCHEDULED);
				if(scheduleDefLossId == null){
					scheduleDefLossId = scheduleService.findCarDefLoss(checkCarVo.getRegistNo(),
							checkCarVo.getSerialNo(),ScheduleStatus.SCHEDULED_CHANGE);
				}
				if(scheduleDefLossId!=null){// 已调度的定损任务
					PrpLScheduleDefLossVo sdefLossVo = scheduleService.findScheduleDefLossByPk(scheduleDefLossId);
					String userCode = sdefLossVo.getScheduledUsercode();// 被调度人员代码
					defLossVo = createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
					defLossVo.setTaskFlag(checkCarVo.getSerialNo()==sdefLossVo.getSerialNo() ? "1" : "0");// 1-是，（已定损）
				}else{// 未生成调度定损的任务
					if(checkCarVo.getSerialNo()!=1){// 三者车没有损失不生成定损任务,互碰自赔生成三者车定损任务
						// checkCarVo 转为 defLossVo
						// CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf());//非互碰自赔
						// &&"0".equals(checkCarVo.getLossFlag())// 0-有损失 ，1-无损失
						if(CodeConstants.YN01.N.equals(checkVo.getSingleAccidentFlag())){// 非单车事故
							defLossVo = createDefLossFormCarVo(checkTaskId,checkCarVo,"");
							defLossVo.setTaskFlag("0");// 0-否(未生成定损任务)
						}
					}else{
						defLossVo = createDefLossFormCarVo(checkTaskId,checkCarVo,"");
						defLossVo.setTaskFlag("0");// 0-否(未生成定损任务)
					}
				}
				if(defLossVo != null){
					prpLScheduleDefLossList.add(defLossVo);
				}
			}
		}

		List<PrpLCheckPropVo> checkPropVoList = checkTaskVo.getPrpLCheckProps();
		// 保存标的序号对应的Long,PrpLScheduleDefLossVo
		if(checkPropVoList != null && checkPropVoList.size() > 0){
//			Long lossPartyId = null;
			Map<Long,String> lossPartyId = new HashMap<Long,String>();
			for(PrpLCheckPropVo checkPropVo : checkPropVoList){
				String isNoClaim = checkPropVo.getIsNoClaim();// 无需赔付不生成财产定损任务
				if(RadioValue.RADIO_YES.equals(isNoClaim)){
					continue;
				}
				PrpLScheduleDefLossVo defLossVo = null;
				// checkPropVo 转换为 defLossVo
				boolean kindFlag = getCarKind(checkVo.getRegistNo());
				defLossVo = createDefLossFormPropVo(checkTaskId,checkPropVo,checkPropVo.getUpdateUser());
				boolean isExist = scheduleService.isExistDefLossTask(checkPropVo.getRegistNo(),
						checkPropVo.getLossPartyId().intValue(),"2",ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
				defLossVo.setTaskFlag(isExist ? "1" : "0");
				if(checkPropVo.getLossPartyId()==1
&& !lossPartyId.containsKey(checkPropVo.getLossPartyId())){// 损失方（标的财）
					// 如果不是互碰自赔，判断标的车是否承保车上货物责任险，
					if(RadioValue.RADIO_NO.equals(checkVo.getIsClaimSelf())){
						if(kindFlag){// 标的车承保车上货物险
							prpLScheduleDefLossList.add(defLossVo);
						}
					}else{// 是互碰自赔，可以生成标的财定损任务
						prpLScheduleDefLossList.add(defLossVo);
					}
				}else{// 三者车财 （互碰自赔生成三者车财产定损）
					if(!lossPartyId.containsKey(checkPropVo.getLossPartyId())){
						prpLScheduleDefLossList.add(defLossVo);
					}
				}
				lossPartyId.put(checkPropVo.getLossPartyId(),checkPropVo.getLossPartyName());
			}
		}
		String regNo = checkVo.getRegistNo();
		PrpLScheduleTaskVo schTaskVo = scheduleService.getScheduleTask(regNo,"3");
		setDlossPerson(checkTaskVo,prpLScheduleDefLossList,schTaskVo,url);
		return prpLScheduleDefLossList;
	}

	// 设置指定定损人员
	private void setUserCode(String dfType,PrpLScheduleDefLossVo defLossVo,List<PrpLScheduleDefLossVo> defLossVos,
	                PrpLCheckCarVo checkCarVo,PrpLCheckPropVo checkPropVo)throws Exception{
		for(PrpLScheduleDefLossVo defLoss : defLossVos){
			
			if("1".equals(dfType)){// 车辆定损vo
				if("0".equals(defLoss.getTaskFlag())
						&&defLoss.getSerialNo()==checkCarVo.getSerialNo()){
					String[] tempU = defLoss.getScheduledUsercode().split(",");
					if(tempU!=null && tempU.length>1){
						String userCode = tempU[0];// 处理人代码
						String comCode = tempU[1];// 机构代码
						defLossVo.setScheduledComcode(comCode);
						defLossVo.setScheduledUsercode(userCode);
					}else{
						throw new IllegalArgumentException("无指定处理人！");
					}
					
				}
			}
			
			if("2".equals(dfType)){// 财产定损
				if("0".equals(defLoss.getTaskFlag())
						&&defLoss.getSerialNo().longValue()==checkPropVo.getLossPartyId()){
					String[] tempU = defLoss.getScheduledUsercode().split(",");
					if(tempU!=null && tempU.length>1){
						String userCode = tempU[0];// 处理人代码
						String comCode = tempU[1];// 机构代码
						defLossVo.setScheduledComcode(comCode);
						defLossVo.setScheduledUsercode(userCode);
					}else{
						throw new IllegalArgumentException("无指定处理人！");
					}
					
				}
			}
		}
	}
	
	@Override
	public List<PrpLWfTaskVo> submitCheckToDloss(List<PrpLScheduleDefLossVo> defLossVos,Long checkId,
	    Long flowTaskId,String flowId,double lossFees,SysUserVo userVo,String isMobileAccept,List<PhotoInfo> photoInfoList) throws Exception {
		String userCode = userVo.getUserCode();
		String comCode = userVo.getComCode();
		List<PrpLWfTaskVo> wfTaskVo = null;
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		Long checkTaskId = checkTaskVo.getId();
		Map<String,PrpLScheduleDefLossVo> carScheduleMap = new HashMap<String,PrpLScheduleDefLossVo>();
		// 车损保存调度定损准备
		List<PrpLCheckCarVo> checkCarVoList = checkTaskVo.getPrpLCheckCars();
		Long schTaskId = null;// 调度定损id
		if(checkCarVoList != null && checkCarVoList.size() > 0){
			int i = 1;
			for(PrpLCheckCarVo checkCarVo : checkCarVoList){
				// 判断是否已生成定损任务
				Integer serialNo = checkCarVo.getSerialNo();
				Long scheduleDefLossId = scheduleService.findCarDefLoss
						(checkCarVo.getRegistNo(),serialNo,ScheduleStatus.DEFLOSS_SCHEDULED);
				if(scheduleDefLossId == null){
					scheduleDefLossId = scheduleService.findCarDefLoss
							(checkCarVo.getRegistNo(),checkCarVo.getSerialNo(),ScheduleStatus.SCHEDULED_CHANGE);
				}
				if(scheduleDefLossId == null){
					PrpLScheduleDefLossVo defLossVo = null;
					if(serialNo!=1){// 三者车没有损失不生成定损任务,互碰自赔三者车生成定损任务
						// CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf());//非互碰自赔
						// &&"0".equals(checkCarVo.getLossFlag())// 0-有损失 ，1-无损失-------2016年10月13日的需求，车辆无损失也要生成定损任务
						if(CodeConstants.YN01.N.equals(checkVo.getSingleAccidentFlag())){// 非单车事故
							// checkCarVo 转为 defLossVo
							defLossVo = createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
							this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------加入指定处理人代码
						}
					}else{
						defLossVo = createDefLossFormCarVo(checkTaskId,checkCarVo,userCode);
						this.setUserCode("1",defLossVo,defLossVos,checkCarVo,null);// ----------加入指定处理人代码
					}
					if(defLossVo == null) continue;
					carScheduleMap.put(serialNo+"车损"+i,defLossVo);
					i++ ;
				}else{
					// 更新scheduleDefLoss
					PrpLScheduleDefLossVo scheduleDefLossVo = scheduleService.findScheduleDefLossByPk(scheduleDefLossId);
					scheduleDefLossVo.setCheckLossId(checkId);
					scheduleDefLossVo.setLossCarId(checkCarVo.getCarid());
					scheduleService.updateScheduleDefLoss(scheduleDefLossVo);
					schTaskId = scheduleService.findTaskIdByDefLossId(scheduleDefLossId);
				}

			}
		}

		List<PrpLCheckPropVo> checkPropVoList = checkTaskVo.getPrpLCheckProps();
		// 保存标的序号对应的Long,PrpLScheduleDefLossVo
		Map<Long,PrpLScheduleDefLossVo> propScheduleMap = new HashMap<Long,PrpLScheduleDefLossVo>();
		if(checkPropVoList != null && checkPropVoList.size() > 0){
			Long lossPartyId = null;
			for(PrpLCheckPropVo checkPropVo : checkPropVoList){
				String isNoClaim = checkPropVo.getIsNoClaim();// 无需赔付不生成财产定损任务
				if(RadioValue.RADIO_YES.equals(isNoClaim)){
					continue;
				}
				lossPartyId = checkPropVo.getLossPartyId();
				PrpLScheduleDefLossVo defLossVo = propScheduleMap.get(lossPartyId);
				
				// 新需求，查勘未处理前，可以调度三者车损或者物损，查勘需要做管控
				boolean isExist = scheduleService.isExistDefLossTask(checkPropVo.getRegistNo(),
						lossPartyId.intValue(),"2",ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
				if(defLossVo == null && !isExist){
					// checkPropVo 转换为 defLossVo
					boolean kindFlag = getCarKind(checkVo.getRegistNo());
					defLossVo = createDefLossFormPropVo(checkTaskId,checkPropVo,userCode);
					this.setUserCode("2",defLossVo,defLossVos,null,checkPropVo);// ----------加入指定处理人代码
					if(lossPartyId==1){// 损失方
						if(CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsClaimSelf())){
							propScheduleMap.put(lossPartyId,defLossVo);
						}else{
							if(kindFlag){// 标的车承保车上货物险
								propScheduleMap.put(lossPartyId,defLossVo);
							}
						}
					}else{// 三者车 （互碰自赔生成三者车财产定损）
						propScheduleMap.put(lossPartyId,defLossVo);
//						if(CodeConstants.YN01.N.equals(checkVo.getIsClaimSelf())){
//						}
					}
				}
			}
		}
		
		// 再调用工作流接口(查勘提交)
		WfTaskSubmitVo wfTaskSubmitVo = new WfTaskSubmitVo();
		wfTaskSubmitVo.setFlowTaskId(new BigDecimal(flowTaskId));
		wfTaskSubmitVo.setFlowId(flowId);
		wfTaskSubmitVo.setCurrentNode(FlowNode.Check);
		wfTaskSubmitVo.setComCode(policyViewService.getPolicyComCode(checkVo.getRegistNo()));
		wfTaskSubmitVo.setTaskInUser(userCode);
		wfTaskSubmitVo.setTaskInKey(checkVo.getRegistNo());
		wfTaskSubmitVo.setAssignUser(userCode);
		wfTaskSubmitVo.setAssignCom(comCode);
		wfTaskSubmitVo.setIsMobileAccept(isMobileAccept);
		if(lossFees>=50000||CodeConstants.VerifyCheckFlag.VERIFYCHECK_Y.equals(checkVo.getMajorCaseFlag())){// 提交大案审核
			FlowNode[] flowNode = new FlowNode[1];
			flowNode[0] = FlowNode.ChkBig_LV1;
			wfTaskSubmitVo.setOthenNodes(flowNode);
		}
		
		String registNo = checkVo.getRegistNo();
		registRiskInfoService.writePrpLRegistRiskInfo(registNo,checkVo.getIsSubRogation(),userCode);
		checkTaskService.saveCheckDutyHis(registNo,"查勘提交");
		Long scheduleTaskId = null;
		try{
			// 最终组合为PrpLscheduleTaskVo 送给调度接口保存
			scheduleTaskId = saveScheduleDefLossTask(checkId,userCode,carScheduleMap,propScheduleMap,schTaskId);
			PrpLScheduleTaskVo scheduleTaskVo = findAllScheduleDefLossByCheck(scheduleTaskId);
//			= scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
			try{
				// 查勘提交 --> 刷立案数据
				claimService.submitClaim(registNo,flowId);
			}catch(Exception e){
				// 回滚
				// for(PrpLWfTaskVo taskVo : wfTaskVo){
				// wfTaskHandleService.rollBackTask(taskVo); }
				logger.error("报案号"+ (checkVo == null ? null : checkVo.getRegistNo()) + "查勘提交刷立案失败！",e);
				throw new IllegalArgumentException("提交失败！查勘刷立案估损金额错误！<br/>",e);
			}
			wfTaskVo = wfTaskHandleService.submitCheck(scheduleTaskVo,checkVo,wfTaskSubmitVo);

             
			
			 String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
             url = url+AUTOSCHEDULE_URL_METHOD;
             interfaceAsyncService.reassignmentes(scheduleTaskVo, "", scheduleTaskVo.getPrpLScheduleDefLosses(),url);
             
			// 保存/更新意见列表
			PrpLClaimTextVo claimTextVo = createClaimText(checkVo.getId(),AuditStatus.SUBMITCHECK,FlowNode.Chk.toString(),"1",userVo);
			claimTextService.saveOrUpdte(claimTextVo);
			// 是否移动端案件
			Boolean isMobileWhileListCase = false;
			isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,scheduleTaskVo.getScheduledUsercode());
			// 异步调用车童民太安
			Boolean selfClaimFlag = false;
			PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
			if(IsSingleAccident.YES.equals(prpLRegistVo.getSelfClaimFlag())){
				selfClaimFlag = true;
			}
			if(!selfClaimFlag && !isMobileWhileListCase && scheduleTaskVo.getPrpLScheduleDefLosses() != null && scheduleTaskVo.getPrpLScheduleDefLosses().size() > 0){
	            RegistInformationVo registInformationVo = new RegistInformationVo();
                registInformationVo.setPrpLScheduleTaskVo(scheduleTaskVo);
                List<PrpLScheduleTaskVo> prpLScheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
                prpLScheduleTaskVoList.add(scheduleTaskVo);
                registInformationVo.setPrpLScheduleTaskVoList(prpLScheduleTaskVoList);
                registInformationVo.setSchType("DLoss");
                registInformationVo.setUser(userVo);
				interfaceAsyncService.sendRegistCTorMTA(registInformationVo);
			}
		}catch(Exception e){
			logger.error("报案号"+ (checkVo == null ? null : checkVo.getRegistNo()) + "查勘提交失败！",e);
			// 清空调度的数据
		    PrpLScheduleTaskVo vo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
		    scheduleService.rollBackDefLossTask(vo);
			// 回滚工作流表
			// 查勘
		    wfTaskHandleService.rollBackFlow(vo,flowId);
		    wfTaskHandleService.rollBackNodeAndLWfMain(vo,flowId);

			throw new IllegalArgumentException("提交失败！查勘任务提交失败！<br/>"+e);
		}
		// 查勘和立案送平台都在该方法中
		interfaceAsyncService.sendCheckSubmitToPlatform(registNo);
		
		// 送精励联讯
		otherInterfaceAsyncService.checkToGenilex(checkVo, flowTaskId);
		
		// 更新prplscheduletasklog表
		scheduleTaskService.writePrplScheduleTasklog(registNo,userVo,checkVo,defLossVos);
		
		// 送山东预警和各地交管所
		String warnswitch = SpringProperties.getProperty("WARN_SWITCH");//62,10,50
		String warnComcode = policyViewService.getPolicyComCode(registNo);
		if(warnswitch.contains(warnComcode.substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
		   try{
			interfaceAsyncService.claimToWarn(registNo,null);
		    interfaceAsyncService.checkToWarn(checkTaskId,flowTaskId,null);
		   }catch(Exception e){
				logger.error("报案号"+ (checkVo == null ? null : checkVo.getRegistNo()) + "立案送预警系统失败或者查勘送预警系统失败！",e);	   
		   }
		
		}

		// 若包含人伤，选择现场调解，判断人伤任务是否满足自核，满足则自动审核通过
		PrpLConfigValueVo iLogValueVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,comCode);
		PrpLConfigValueVo iLogRuleValueVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,comCode);
		if("1".equals(iLogValueVo.getConfigValue()) && "0".equals(iLogRuleValueVo.getConfigValue()) && checkTaskVo.getPrpLCheckPersons()!=null && 
				checkTaskVo.getPrpLCheckPersons().size()>0 && ("1".equals(checkVo.getReconcileFlag())
				||checkVo.getReconcileFlag()==null)){
			autoPersSubmitForChe(checkVo, userVo,photoInfoList);
		}
	
		return wfTaskVo;
	}
	
	// 查勘生成定损表
	@Override
	public Long saveScheduleDefLossTask(Long checkId,String userCode,
	    Map<String,PrpLScheduleDefLossVo> carScheduleMap,
		Map<Long,PrpLScheduleDefLossVo> propScheduleMap,Long schTaskId) {
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		String registNo = checkVo.getRegistNo();
		// Long scheduleTaskId = scheduleService.findCarDefLossBySerialNo(checkVo.getRegistNo(),1);
		PrpLScheduleTaskVo scheduleTaskVo = schTaskId==null 
				? new PrpLScheduleTaskVo() : scheduleService.findScheduleTaskVoByPK(schTaskId);
				
		if(schTaskId==null){
		    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleTaskService.findCheckScheduleTaskByRegistNo(registNo,"0");
		    scheduleTaskVo = prpLScheduleTaskVo;
		}
		scheduleTaskVo.setPrpLScheduleTasklogs(null);

		// 组织scheduleTaskVo数据
		scheduleTaskVo.setRegistNo(registNo);
		scheduleTaskVo.setScheduledTime(new Date());
		scheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
		scheduleTaskVo.setOperatorCode(userCode);
		scheduleTaskVo.setScheduledUsercode(userCode);
		
		// 回写调度表的查勘地区编码
		if(!StringUtils.isNotBlank(scheduleTaskVo.getCheckorDeflossAreaCode()) && checkVo.getPrpLCheckTask()!=null){
		    PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		    if(StringUtils.isNotBlank(checkTaskVo.getRemark())){
		        scheduleTaskVo.setCheckorDeflossAreaCode(checkTaskVo.getRemark());
		    }
		    if(StringUtils.isNotBlank(checkTaskVo.getCheckAddress())){
                scheduleTaskVo.setCheckAddress(checkTaskVo.getCheckAddress());
            }
		}
		// ...待续
		logger.info("查勘提交增加调度信息start....报案号="+registNo);
		List<PrpLScheduleDefLossVo> scheduleDefLossVo = new ArrayList<PrpLScheduleDefLossVo>();
		for(String key : carScheduleMap.keySet()){
			PrpLScheduleDefLossVo defLossVo = carScheduleMap.get(key);
			if (defLossVo != null) {
				Integer serialNo = defLossVo.getSerialNo();
				boolean exist = scheduleService.isExistDefLossTask(registNo,serialNo,"1",
						ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
				logger.info("查勘提交增加调度定损车信息ing....报案号="+registNo+";serialNo="+serialNo+"是否已经存在="+exist);
				if (!exist) {
					scheduleDefLossVo.add(defLossVo);
				}
			}
		}
		for(Long key : propScheduleMap.keySet()){
			PrpLScheduleDefLossVo defLossVo = propScheduleMap.get(key);
			if (defLossVo != null) {
				Integer serialNo = defLossVo.getSerialNo();
				boolean exist = scheduleService.isExistDefLossTask(registNo,serialNo,"2",
						ScheduleStatus.DEFLOSS_SCHEDULED,ScheduleStatus.SCHEDULED_CHANGE);
				logger.info("查勘提交增加调度定损财信息ing....报案号="+registNo+";serialNo="+serialNo+"是否已经存在="+exist);
				if (!exist) {
					scheduleDefLossVo.add(defLossVo);
				}
			}
		}

		//
		scheduleTaskVo.setPrpLScheduleDefLosses(scheduleDefLossVo);
		logger.info("查勘提交增加调度信息end....报案号="+registNo);
		return scheduleService.saveScheduleTaskByVo(scheduleTaskVo);
	}

	// 查询查勘生成的所有定损任务
	private PrpLScheduleTaskVo findAllScheduleDefLossByCheck(Long scheduleTaskId){
		PrpLScheduleTaskVo newScheduleTaskVo = new PrpLScheduleTaskVo();;
		PrpLScheduleTaskVo scheduleTaskVo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
		
		List<PrpLScheduleDefLossVo> defLossVoList = scheduleService
				.findScheduleDefLossByCheck(scheduleTaskVo.getRegistNo());
		if (defLossVoList != null && !defLossVoList.isEmpty()) {
			Beans.copy().from(scheduleTaskVo).to(newScheduleTaskVo);
			newScheduleTaskVo.setPrpLScheduleDefLosses(defLossVoList);
		} else {
			List<PrpLScheduleDefLossVo> voList = new ArrayList<PrpLScheduleDefLossVo>();
			Beans.copy().from(scheduleTaskVo).to(newScheduleTaskVo);
			newScheduleTaskVo.setPrpLScheduleDefLosses(voList);
		}
		return newScheduleTaskVo;
	}
	
	@Override
	public PrpLScheduleDefLossVo createDefLossFormPropVo(Long checkTaskId,
	       PrpLCheckPropVo checkPropVo,String userCode) {
		Date date = new Date();
		/*
		 * PrpLCheckTaskVo checkTaskVo=checkService.findPrpLCheckTaskVoById(checkTaskId); PrpLCheckPropVo checkPropVo=checkTaskVo.getPrpLCheckProps();
		 */
		PrpLScheduleDefLossVo defLossVo = new PrpLScheduleDefLossVo();
		defLossVo.setCheckLossId(checkTaskId);
		defLossVo.setSerialNo(checkPropVo.getLossPartyId().intValue());// 损失项序列号
		defLossVo.setItemsName(checkPropVo.getLossPartyName());// 损失项名称
		defLossVo.setRegistNo(checkPropVo.getRegistNo());
		defLossVo.setItemsContent(checkPropVo.getLossItemName());// 损失内容
		defLossVo.setScheduleStatus(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED);// 调度定损状态
		defLossVo.setDeflossType(CodeConstants.DeflossType.PropLoss);// 定损类型
		defLossVo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULECHECK);// 定损来源
		defLossVo.setLossitemType(checkPropVo.getLossPartyId().toString());// 损失方
		defLossVo.setAddoperatorCode(userCode);// 标的添加人代码
		defLossVo.setScheduledUsercode(userCode);// 被调度人员代码
		defLossVo.setCreateUser(userCode);
		defLossVo.setCreateTime(date);
		defLossVo.setUpdateUser(userCode);
		defLossVo.setUpdateTime(date);
		defLossVo.setLicenseNo(checkPropVo.getLossPartyName());
		/*
		 * if(checkPropVo.getLossPartyId()!=0){ PrpLCheckCarVo checkCarVo=checkService.findCarIdBySerialNoAndRegistNo (checkPropVo.getLossPartyId(),checkPropVo.getRegistNo());
		 * defLossVo.setLossCarId(checkCarVo.getCarid());//损失方车辆id defLossVo.setLicense(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());//损失方车牌号码 }
		 */
		// defLossVo.setLicense(license);
		return defLossVo;
	}

	// 创建定损carVo
	@Override
	public PrpLScheduleDefLossVo createDefLossFormCarVo(Long checkTaskId,PrpLCheckCarVo checkCarVo,String userCode) {
		Date date = new Date();
		PrpLScheduleDefLossVo defLossVo = new PrpLScheduleDefLossVo();
		defLossVo.setRegistNo(checkCarVo.getRegistNo());
		defLossVo.setSerialNo(checkCarVo.getSerialNo());// 车辆序号
		// defLossVo.setItemsName(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());//车牌号
		defLossVo.setItemsName(1==checkCarVo.getSerialNo() ? "标的车" : "三者车");
		defLossVo.setItemsContent(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());// 标的内容
		defLossVo.setScheduleStatus(CodeConstants.ScheduleStatus.DEFLOSS_SCHEDULED);// 调度定损状态
		defLossVo.setDeflossType(CodeConstants.DeflossType.CarLoss);// 定损类型
		defLossVo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULECHECK);// 定损来源
		defLossVo.setCheckLossId(checkTaskId);//
		defLossVo.setLossitemType(checkCarVo.getSerialNo().toString());// 损失方
		defLossVo.setAddoperatorCode(userCode);// 标的添加人代码
		defLossVo.setScheduledUsercode(userCode);// 被调度人员代码
		defLossVo.setCreateUser(userCode);
		defLossVo.setCreateTime(date);
		defLossVo.setUpdateUser(userCode);
		defLossVo.setUpdateTime(date);
		defLossVo.setLossCarId(checkCarVo.getCarid());// 损失方车辆id
		defLossVo.setLicenseNo(checkCarVo.getPrpLCheckCarInfo().getLicenseNo());// 损失方车牌号码
		return defLossVo;
	}

	// 复勘、大案审核提交
	@Override
	public void chkReOrBigSubmit(String codeName, Long checkId,Long flowTaskId,String chkBigUser,SysUserVo currentUserVo) {
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
		SysUserVo userVo = sysUserService.findByUserCode(chkBigUser);
		if(FlowNode.ChkRe.name().equals(codeName)){// 复勘提交
			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setComCode(policyViewService.getPolicyComCode(checkVo.getRegistNo()));
			submitVo.setTaskInUser(currentUserVo.getUserCode());
			submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
			submitVo.setTaskInKey(wfTaskVo.getHandlerIdKey());
			submitVo.setAssignCom(userVo.getComCode());
			submitVo.setAssignUser(chkBigUser);
			submitVo.setCurrentNode(FlowNode.ChkRe);
			submitVo.setNextNode(FlowNode.valueOf(wfTaskVo.getTaskInNode()));
			wfTaskHandleService.submitCheck(null, checkVo, submitVo);
		}else{// 大案审核
			WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
			taskVo.setRegistNo(checkVo.getRegistNo());
			taskVo.setHandlerIdKey(checkId.toString());// 查勘主表id
			taskVo.setItemName(wfTaskVo.getItemName());

			WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(new BigDecimal(flowTaskId));
			submitVo.setComCode(policyViewService.getPolicyComCode(checkVo.getRegistNo()));
			submitVo.setTaskInUser(currentUserVo.getUserCode());
			submitVo.setTaskInKey(checkVo.getRegistNo());

			// ，获取页面的指定处理人
			submitVo.setAssignUser(null);// 指定处理人
			if(FlowNode.ChkBig_LV1.name().equals(codeName)){
				submitVo.setAssignCom(CodeConstants.TOPCOM);// 指定处理机构
			}else{
				submitVo.setAssignCom(userVo.getComCode());// 指定处理机构
			}
			submitVo.setCurrentNode(FlowNode.valueOf(codeName));
			submitVo.setNextNode(getNextNodeCode(codeName,checkId));

			wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
		}
	}
	
	// 获取下一节点名称
	@Override
	public FlowNode getNextNodeCode(String codeName,Long checkId) {
		Double sum = getFeeSum(checkId);
		FlowNode flowNode = null;
		if(FlowNode.ChkBig_LV1.equals(codeName)){// 大案审核一级权限
			flowNode = sum < 50000 ? FlowNode.END : FlowNode.ChkBig_LV2;
		}else{
			flowNode = FlowNode.END;
		}
		// if(FlowNode.ChkBig_LV1.equals(codeName)){//大案审核一级权限
//			flowNode = sum < 100000 ? FlowNode.END : FlowNode.ChkBig_LV2;
		// }else if(FlowNode.ChkBig_LV2.equals(codeName)){//大案审核二级权限
//			flowNode = sum < 200000 ? FlowNode.END : FlowNode.ChkBig_LV3;
		// }else if(FlowNode.ChkBig_LV3.equals(codeName)){//大案审核三级权限
//			flowNode = sum < 300000 ? FlowNode.END : FlowNode.ChkBig_LV4;
		// }else if(FlowNode.ChkBig_LV4.equals(codeName)){//大案审核四级权限
//			flowNode = sum < 400000 ? FlowNode.END : FlowNode.ChkBig_LV5;
//		}else if(FlowNode.ChkBig_LV5.equals(codeName)){
//			flowNode = FlowNode.END;
//		}
		return flowNode;
	}

	private Double getFeeSum(Long checkId) {
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		PrpLCheckTaskVo taskVo = checkVo.getPrpLCheckTask();
		Double rescue = NullToZero(taskVo.getSumRescueFee()).doubleValue();
		Double car = NullToZero(taskVo.getSumLossCarFee()).doubleValue();
		Double prop = NullToZero(taskVo.getSumLossPropFee()).doubleValue();
		Double person = NullToZero(taskVo.getSumLossPersnFee()).doubleValue();
		return rescue+car+prop+person;
	}
	
	@Override
	public PrpLCheckCarVo initAddThirdCar(String registNo) {
		PrpLCheckCarVo checkCarVo = new PrpLCheckCarVo();
		PrpLCheckVo checkVo = queryPrpLCheckVo(registNo);
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		String tmpLicenNo = "";
		for(PrpLCheckCarVo carVo : checkTaskVo.getPrpLCheckCars()){
			if(carVo.getSerialNo()==1){
				tmpLicenNo = carVo.getPrpLCheckCarInfo().getLicenseNo().substring(0,2);
			}
		}
		checkCarVo.setRegistNo(registNo);
		// 查勘的序号
        List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(registNo,"serialNo");
		// 调度序号
        List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(registNo);
        Integer serialNo = 0;
        for(PrpLScheduleDefLossVo prpLScheduleDefLossVo : prpLScheduleDefLossVoList){
            if(serialNo < prpLScheduleDefLossVo.getSerialNo()){
                serialNo = prpLScheduleDefLossVo.getSerialNo();
            }
        }
        if(prpLCheckCarVos != null && prpLCheckCarVos.size() > 0){
            if(serialNo < prpLCheckCarVos.get(0).getSerialNo()){
                serialNo = prpLCheckCarVos.get(0).getSerialNo();
            }
        }
        checkCarVo.setSerialNo(serialNo+1);
		checkCarVo.setCheckAdress(checkTaskVo.getCheckAddress());
		checkCarVo.setCheckTime(new Date());
		PrpLCheckCarInfoVo carInfo = new PrpLCheckCarInfoVo();
		carInfo.setLicenseNo(tmpLicenNo);
		checkCarVo.setPrpLCheckCarInfo(carInfo);
		
		return checkCarVo;
	}

	@Override
	public void deleteThirdCar(Long thCarId) throws Exception {
		checkService.deleteThirdCarByPK(thCarId);
	}

	// 更新报案的出险经过
	@Override
	public void updateDangerRemark(String registNo,String dangerRemark,String userCode) {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		PrpLRegistExtVo prpLRegistExt = registVo.getPrpLRegistExt();
		prpLRegistExt.setDangerRemark(dangerRemark);
		prpLRegistExt.setUpdateUser(userCode);
		prpLRegistExt.setUpdateTime(new Date());
		registQueryService.updatePrpLRegistExt(prpLRegistExt);
	}

	// 更新checkTask总估损金额
	@Override
	public double saveSuccess(String registNo) throws Exception {
		PrpLCheckVo checkVo = checkService.findPrpLCheckByRegistNo(registNo);
		checkService.calculateFeeSum(checkVo);
		this.updateCheckMain(checkVo);
		// if(this.getSumLossFee(prpLcheckTask)>5000) throw new Exception("大案审核！");
		return getSumLossFee(checkVo);
	}

	// 获取大案审核标志
	@Override
	public String getMajorFlag(Long checkId) throws Exception {
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		return checkVo.getMajorCaseFlag();
	}
	
	// 获取费用总和
	@Override
	public double getLossFee(Long checkId) throws Exception {
		return getSumLossFee(checkService.findPrpLCheckVoById(checkId));
	}

	// 保存查勘费用
	@Override
	public void saveCharge(List<PrpLDlossChargeVo> lossChargeVos,Long checkId,SysUserVo userVo) throws Exception {
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		String registNo = checkVo.getRegistNo();
		if(lossChargeVos!=null && lossChargeVos.size()>0){
			Map<String,String> kindMap = new HashMap<String,String>(); 
			List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
			for(PrpLCItemKindVo itemKind :itemKinds){
				kindMap.put(itemKind.getKindCode(),itemKind.getKindName());
			}
			for(PrpLDlossChargeVo lossCharge : lossChargeVos){
				if(lossCharge.getId()!=null){
					lossCharge.setUpdateTime(new Date());
					lossCharge.setUpdateUser(userVo.getUserCode());
				}else{
					lossCharge.setBusinessId(checkId);
					lossCharge.setRegistNo(registNo);
					lossCharge.setBusinessType(FlowNode.Check.name());
					lossCharge.setKindCode(lossCharge.getKindCode());
					lossCharge.setKindName(kindMap.get(lossCharge.getKindCode()));
					lossCharge.setCreateTime(new Date());
					lossCharge.setCreateUser(userVo.getUserCode());
				}
			}
			lossChargeService.saveOrUpdte(lossChargeVos);
		}else{
			lossChargeService.delCharge(checkId,FlowNode.Check.name());
		}
	}
	
	// 更新免赔率
	@Override
	public void updateClaimDeduct(List<PrpLClaimDeductVo> claimDeductVos,String registNo) {
		List<PrpLClaimDeductVo> claimDeductQueryList = registQueryService
				.findClaimDeductVoByRegistNo(registNo);
		// 获取该报案号下所有免赔条件数据
		String riskCode = claimDeductVos.get(0).getRiskCode();
		List<PrpLClaimDeductVo> tempList = new ArrayList<PrpLClaimDeductVo>();
		// 根据险种筛选数据
		for (PrpLClaimDeductVo temp : claimDeductQueryList) {
			if (temp.getRiskCode().equals(riskCode)) {
				tempList.add(temp);
			}
		}
		// 根据提交过来的List更新免赔条件的isCheck字段
		for (PrpLClaimDeductVo claimDeductQ : tempList) {
			for (PrpLClaimDeductVo claimDeductS : claimDeductVos) {
				if (claimDeductQ.getDeductCondCode().equals(
						claimDeductS.getDeductCondCode())) {
					claimDeductQ.setIsCheck(claimDeductS.getIsCheck());
					break;
				}
			}
		}
		
		registQueryService.updateClaimDeduct(tempList);
	}
	
	@Override
	public String subRadioValid(String registNo){
		String retData = "ok";
		List<PrpLCMainVo> policyInfos = this.getPolicyAllInfo(registNo);
		boolean flag = false;
		for(PrpLCMainVo policyInfo : policyInfos){
			if(Risk.DQZ.equals(policyInfo.getRiskCode())){
				flag = true;
			}
		}
		if(!flag){
			return "此案件没有用交强报案，不能选择互碰自赔！";
		}
		
//		if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PLoss,null,"")){
		// return "存在人伤任务，不适用互碰自赔案件处理！";
//		}
		if(this.validPersonTask(registNo)){
			return "存在人伤任务，不适用互碰自赔案件处理！";
		}
		return retData;
	}

	
	// =================================== ext ================================================
	
	/* 
	 * @see ins.sino.claimcar.check.service.CheckHandleService#validPersonTask(java.lang.String)
	 * @param registNo
	 * @return
	 */
	@Override
	public boolean validPersonTask(String registNo){
		boolean returnVal = false;// 返回值--无人伤任务
		List<PrpLDlossPersTraceMainVo> dlossPersTraceVos = 
				persTraceService.findPersTraceMainVoList(registNo);
		if(dlossPersTraceVos!=null&&dlossPersTraceVos.size()>0){
			for(PrpLDlossPersTraceMainVo dlossPersTraceVo : dlossPersTraceVos){
				if(!"10".equals(dlossPersTraceVo.getCaseProcessType())&&
				        !"7".equals(dlossPersTraceVo.getUnderwriteFlag())){
					returnVal = true;// 存在人伤任务
				}
			}
		}else{
            List<PrpLWfTaskVo> prpLWfTaskVoList = wfFlowQueryService
                    .findUnAcceptTask(registNo, FlowNode.PLoss.name());
            if (prpLWfTaskVoList != null && prpLWfTaskVoList.size() > 0) {
				returnVal = true;// 存在人伤任务
            }
        }
		return returnVal;
	}

	@Override
	public PrpLPayCustomVo getPayCusInfo(Long id) {
		return managerService.findPayCustomVoById(id);
	}
	
	@Override
	public List<PrpLCMainVo> getPolicyAllInfo(String registNo){
		return policyViewService.getPolicyAllInfo(registNo);
	}

	// 获取收款人信息
	@Override
	public PrpLPayCustomVo findPayCustomVoByRegistNo(String registNo) {
		PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
		List<PrpLPayCustomVo> payCustomVos = managerService.findPayCustomVoByRegistNo(registNo);
		for(PrpLPayCustomVo payVo:payCustomVos){
			if("2".equals(payVo.getPayObjectKind())){
				payCustomVo = payVo;
			}
		}
		return payCustomVo;
	}
	
	// 获取公估资费标准
	@Override
	public Map<String,String> getIntermStanders(String userCode){
		Map<String,String> chargeStandard = new HashMap<String,String>();
		PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userCode);
		if(intermMainVo == null){
			// throw new IllegalArgumentException("公估没有配置！");
		}else{
			for(PrpdIntermServerVo intermServerVo : intermMainVo.getPrpdIntermServers()){
				String feeStand = intermServerVo.getFeeStandard();
				String feeName = intermMainVo.getIntermName()+"--"+intermServerVo.getFeeStandard();
				chargeStandard.put(feeStand,feeName);
			}
		}
		return chargeStandard;
	}
	
	/**
	 * <pre>
	 * 保存公估信息
	 * </pre>
	 * @param checkVo
	 * @param userCode
	 * @param userName
	 * @modified: ☆Luwei(2016年8月25日 下午1:03:10): <br>
	 */
	@Override
	public void saveAssessor(String registNo,SysUserVo userVo){
		PrpLCheckVo checkVo = checkService.findPrpLCheckByRegistNo(registNo);
		// 公估定损时新增一条公估信息
		if(CheckClass.CHECKCLASS_Y.equals(checkVo.getCheckClass())){
			// 判断是否有数据
			PrpLAssessorVo assessorOld = assessorService.findAssessorByLossId
					(registNo,"0",null,checkVo.getRemark());
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
				assessorVo.setRegistNo(registNo);
				assessorVo.setLossId(checkVo.getId());
				assessorVo.setTaskType("0");
				assessorVo.setKindCode(this.getCarKindCode(registNo));
				assessorVo.setLossDate(new Date());
				String code = checkVo.getRemark();
				assessorVo.setIntermcode(code);
				assessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",code));
				assessorVo.setLicenseNo("");
				assessorVo.setUnderWriteFlag("1");
				assessorVo.setCreateTime(new Date());
				assessorVo.setCreateUser(userVo.getUserCode());
				assessorVo.setComCode(userVo.getComCode());
				
				assessorService.saveOrUpdatePrpLAssessor(assessorVo,userVo);
			}
		}
	}
	
	@Override
	public void saveCheckFee(String registNo, SysUserVo userVo) {
		PrpdCheckBankMainVo prpdCheckBankMainVo=managerService.findCheckByUserCode(userVo.getUserCode());
		PrpLCheckVo checkVo = checkService.findPrpLCheckByRegistNo(registNo);
		PrpLRegistVo prpLRegistVo=registQueryService.findByRegistNo(registNo);
		// 新增一条查勘费任务信息
		if(prpdCheckBankMainVo!=null){
			// 判断是否有数据
			PrpLAcheckVo prpLAcheckOld=acheckService.findAcheckByLossId(registNo, CheckTaskType.CHK,null, prpdCheckBankMainVo.getCheckCode());
			if(prpLAcheckOld == null){
				PrpLAcheckVo acheckVo = new PrpLAcheckVo();
				acheckVo.setCheckmid(prpdCheckBankMainVo.getId());
				acheckVo.setCheckmnamedetail(prpdCheckBankMainVo.getCheckName());
				acheckVo.setRegistNo(registNo);
				acheckVo.setBussiId(checkVo.getId());
				acheckVo.setTaskType(CheckTaskType.CHK);
				acheckVo.setKindCode(this.getCarKindCode(registNo));
				acheckVo.setLossDate(new Date());
				acheckVo.setCheckcode(prpdCheckBankMainVo.getCheckCode());
				//翻译问题，确定后需改动
				acheckVo.setCheckname(codeTranService.transCode("CheckPayCode",prpdCheckBankMainVo.getCheckCode()));
				acheckVo.setLicenseNo("");
				acheckVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
				acheckVo.setCreateTime(new Date());
				acheckVo.setCreateUser(userVo.getUserCode());
				acheckVo.setComCode(prpLRegistVo.getComCode());
				acheckVo.setUnderWriteDate(new Date());
				acheckService.saveOrUpdatePrpLAcheck(acheckVo, userVo);
				checkVo.setCheckCode(prpdCheckBankMainVo.getCheckCode());
				checkService.updateCheckMain(checkVo);
			}
		}
	}
	
	/**
	 * <pre>
	 * 公估的险别
	 * </pre>
	 * @param registNo
	 */
	@Override
	public String getCarKindCode(String registNo) {
		String kindCode = "B";
		PrpLCheckCarVo checkCarVo = findCarIdBySerialNoAndRegistNo(1,registNo);
		if(checkCarVo!=null&&StringUtils.isNotBlank(checkCarVo.getKindCode())){
			kindCode = checkCarVo.getKindCode();
		}else{
			List<PrpLCMainVo> policyInfo = policyViewService.getPolicyAllInfo(registNo);
			if (policyInfo != null && policyInfo.size() == 1 && Risk.DQZ.equals(policyInfo.get(0).getRiskCode())) {
				// 单交强赋BZ险 其他赋值B险
				kindCode =  "BZ";
			}
			
		}
		return kindCode;
	}
	
	@Override
	public Map<String,String> getCustom(String registNo){
		Map<String,String> map = new HashMap<String,String>();
		List<PrpLPayCustomVo> payCustomVos = managerService.findPayCustomVoByRegistNo(registNo);
		if(payCustomVos != null && payCustomVos.size() > 0){
			for(PrpLPayCustomVo payCustomVo : payCustomVos){
				String bankNo = payCustomVo.getAccountNo();
				String idNo = bankNo.length()<4 ? bankNo : 
					bankNo.substring(bankNo.length()-4,bankNo.length());
				String value = payCustomVo.getPayeeName() + "--" + idNo;
				map.put(payCustomVo.getId().toString(),value);
			}
		}
		map.put("0","");
		return map;
	}

	// 车辆保存校验
	@Override
	public String validHandleCar(PrpLCheckVo checkVo,PrpLCheckDutyVo checkDuty,
	       PrpLSubrogationMainVo subrogationMainVo) {
		String retData = "ok";
		String registNo = checkVo.getRegistNo();
		List<PrpLCheckCarVo> checkCarVoList = findPrpLcheckCarVoByRegistNo(registNo);
		for(PrpLCheckCarVo checkCarVo:checkCarVoList){
			if(StringUtils.isEmpty(checkCarVo.getFlag())){
				String licNo = checkCarVo.getPrpLCheckCarInfo().getLicenseNo();
				return "车辆【"+licNo+"】还未处理，请先处理！";
			}
		}
		BigDecimal sumDutyRate = new BigDecimal("0");
		List<PrpLCheckDutyVo> checkDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
		for(PrpLCheckDutyVo checkDutyVo:checkDutyVoList){
			BigDecimal oldDuty = NullToZero(checkDuty.getIndemnityDutyRate());
			BigDecimal newDuty = NullToZero(checkDutyVo.getIndemnityDutyRate());
			sumDutyRate = sumDutyRate.add(1==checkDutyVo.getSerialNo() ? oldDuty : newDuty);
		}
		if(sumDutyRate.compareTo(new BigDecimal("100"))==1){
			return "车辆事故责任比例之和超过100%，请修改！";
		}
		
		// 判断是否有车损险
		Boolean iskindA = false;
		List<PrpLCMainVo> policyInfoList = policyViewService.getPolicyAllInfo(registNo);
		for(PrpLCMainVo policyInfo : policyInfoList){
			if(!Risk.DQZ.equals(policyInfo.getRiskCode())){
				for(PrpLCItemKindVo itemKind : policyInfo.getPrpCItemKinds()){
					if("A".equals(itemKind.getKindCode()) || "A1".equals(itemKind.getKindCode())){
						iskindA = true;
						break;
					}
				}
			}
		}
		
		// 代位校验 车牌号码 车牌类型 vin码 发动机号 一致
		if("1".equals(subrogationMainVo.getSubrogationFlag())){
			if(!iskindA){
				return "本车无车损险，不能选择代位求偿！";
			}
			List<PrpLCheckCarVo> checkCarList = findPrpLcheckCarVoByRegistNo(registNo);
			List<PrpLSubrogationCarVo> subrogationCarList = subrogationMainVo.getPrpLSubrogationCars();
			Map<Integer,String> checkCarMap = new HashMap<Integer,String>();
			for(PrpLCheckCarVo checkCarVo : checkCarList){
				PrpLCheckCarInfoVo carInfoVo =checkCarVo.getPrpLCheckCarInfo();
				String carInfoStr = carInfoVo.getLicenseNo().trim()+"-"+ carInfoVo.getLicenseType().trim()
						+"-"+carInfoVo.getVinNo().trim()+"-"+carInfoVo.getEngineNo().trim();
				checkCarMap.put(checkCarVo.getSerialNo(),carInfoStr);
			}
			
			
			for(PrpLSubrogationCarVo carVo : subrogationCarList){
				String subrogationStr = carVo.getLicenseNo().trim()+"-"+carVo.getLicenseType().trim()
						+"-"+carVo.getVinNo().trim()+"-"+carVo.getEngineNo().trim();
				if(!subrogationStr.equals(checkCarMap.get(carVo.getSerialNo()))){
//					System.out.println(subrogationStr);
//					System.out.println(checkCarMap.get(carVo.getSerialNo()));
					return "三者车"+carVo.getLicenseNo()+"代位信息与责任对方机动车信息必须保持一致！";
				}
			}
		}
		
//		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
//		if(equalsTime(registVo.getReportTime(),new Date())){
		// return "查勘日期不能小于报案日期，请修改！";
//		}
		
		return retData;
	}

	public boolean equalsTime(Date reportTime,Date checkTime){
		boolean retur = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Long str1 = Long.parseLong(sdf.format(checkTime).substring(0,8));
		Long str2 = Long.parseLong(sdf.format(reportTime).substring(0,8));
		// 查勘日期不能小于报案日期
		if(checkTime != null && str1 < str2){
			retur = true;
		}
		return retur;
	}
	
	@Override
	public String validCheckClaim(String registNo) {
		String retData = "ok";
		// 互碰自赔
//		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		Long checkId = getCheckId(registNo);
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();

		List<PrpLCheckPropVo> checkProps = checkTaskVo.getPrpLCheckProps();
		
		// 单车事故为否，三者车不能全为无损失（必须有一个有损失）
		boolean thirdLossFlag = false;
		for(PrpLCheckCarVo checkCarVo:checkTaskVo.getPrpLCheckCars()){
			String loss = checkCarVo.getLossFlag();
			// 必须有一个有损失
			if(CodeConstants.RadioValue.RADIO_NO.equals(loss)
					&&checkCarVo.getSerialNo()!=1){
				thirdLossFlag = true;
			}
			
			String idNo = checkCarVo.getPrpLCheckDriver().getIdentifyNumber();
			String driverNo = checkCarVo.getPrpLCheckDriver().getDrivingLicenseNo();
			String identifyType = checkCarVo.getPrpLCheckDriver().getIdentifyType();
			if(StringUtils.isNotBlank(identifyType) && "1".equals(identifyType) &&
					StringUtils.isNotBlank(idNo)&&idNo.equals(checkTaskVo.getCheckerIdfNo())){
				return "查勘人员身份证号不能和驾驶员身份证号一致！请修改！";
			}
			if(StringUtils.isNotBlank(identifyType) && "1".equals(identifyType) && 
					StringUtils.isNotBlank(driverNo) && driverNo.equals(checkTaskVo.getCheckerIdfNo())){
				return "查勘人员身份证号不能和车辆驾驶证号一致，请修改！！！";
			}
		}
		if(RadioValue.RADIO_NO.equals
				(checkVo.getSingleAccidentFlag())&&!thirdLossFlag){
			// return "不是单车事故，则至少有一个三者车有损失！";
		}
		
		// 重大案件上报选择是时，查勘处理不能选择无损失或估损金额为0，这个需求指的是整个案件，不特指某一辆车，当前系统是特指车
		if(RadioValue.RADIO_YES.equals(checkVo.getMajorCaseFlag())){
			Double sumLoss = 0.0;
			boolean lossFlag = false;
			for(PrpLCheckCarVo checkCarVo:checkTaskVo.getPrpLCheckCars()){
				String loss = checkCarVo.getLossFlag();
				sumLoss += checkCarVo.getLossFee().doubleValue();
				if(CodeConstants.RadioValue.RADIO_NO.equals(loss)){
					lossFlag = true;
				}
			}
			if(sumLoss==0.0)
			{
				return "该案件为重大案件，查勘处理车辆总的估损金额不能为零或空！";
			}
			if(!lossFlag)
			{
				return "该案件为重大案件，查勘车辆处理不能全选择为无损失！";
			}
		}
		
		List<PrpLCMainVo> policyInfo = policyViewService.getPolicyAllInfo(registNo);
		boolean policyFlag = false;
		for(PrpLCMainVo policy : policyInfo){
			if(Risk.DQZ.equals(policy.getRiskCode())){
				policyFlag = true;
			}
		}
		
		if(RadioValue.RADIO_YES.equals(checkVo.getIsClaimSelf())){

			if(checkTaskVo.getPrpLCheckCars().size()<2){
				return "该案件不存在三者车，不适用互碰自赔案件处理！";
			}
			
			BigDecimal partyLoss = new BigDecimal(0);// 标的车的财损
			BigDecimal thirdLoss = new BigDecimal(0);// 三者车的财损
			for(PrpLCheckPropVo checkProp : checkProps){
				// 存在地面财产，不能互碰自赔(如果是地面财选择无需赔付，则可以做互碰自赔。)
				if(checkProp.getLossPartyId() == 0 && RadioValue.RADIO_NO.equals(checkProp.getIsNoClaim())){
					return "存在车外财产损失，不适用互碰自赔案件处理！";
				}else if(checkProp.getLossPartyId()==1){// 标的车
					partyLoss = checkProp.getLossFee();
//					if(checkProp.getLossFee().compareTo(BigDecimal.valueOf(2000))==1){
					// return checkProp.getLossPartyName()+"的财产损失金额大于2000，不适用互碰自赔案件处理！";
//					}
				}else{// 三者车
					thirdLoss = checkProp.getLossFee();
				}
			}
			
			boolean singleFlag = false;
			
			// 各方损失金额均在2000元以内；
			for(PrpLCheckCarVo checkCarVo:checkTaskVo.getPrpLCheckCars()){
				Integer serNo = checkCarVo.getSerialNo();
				BigDecimal carLoss = serNo==1 ? partyLoss : thirdLoss;
				BigDecimal loss = checkCarVo.getLossFee().add(carLoss).add(checkCarVo.getRescueFee());
				boolean equals = loss.compareTo(BigDecimal.valueOf(2000))==1;
				
				String licenseNo = checkCarVo.getPrpLCheckCarInfo().getLicenseNo();
				String tempStr = serNo==1 ? "标的车【" : "三者车【";
				String qitStr = tempStr+licenseNo+"】的总估损金额大于2000元，不适用互碰自赔案件处理！";
				// 标的车、三者车的车损金额加施救费(包括财损)
				if(equals){
					return qitStr;
				}
				if(CodeConstants.RadioValue.RADIO_NO.equals(checkCarVo.getLossFlag())){
					singleFlag = true;
				}
			}
			
			// 如果是否单车事故为否，在查勘提交的时候管控则至少有一个三者车有损失
			if(!singleFlag&&CodeConstants.RadioValue.RADIO_NO.equals(checkVo.getSingleAccidentFlag())){
				return "不是单车事故，则至少有一个三者车有损失！";
			}
			
			if(CodeConstants.RadioValue.RADIO_YES.equals(checkVo.getIsPersonLoss())){
				return "互碰自赔案件，是否包含人伤不许为是！";
			}
			if(checkTaskVo.getPrpLCheckPersons().size()>0){
				return "查勘存在人伤损失项，不适用互碰自赔案件，请修改！";
			}
			
			// 工作流存在人伤也不能用和碰自赔,
//			if(wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PLoss,null,"")){
			// return "存在人伤任务，不适用互碰自赔案件处理！";
//			}
			if(this.validPersonTask(registNo)){
				return "存在人伤任务，不适用互碰自赔案件处理！";
			}
			
			// 事故责任(同等责任、主次责任)
			List<PrpLCheckDutyVo> checkDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
			for(PrpLCheckDutyVo checkDutyVo : checkDutyVoList){
				if("0".equals(checkDutyVo.getIndemnityDuty())||
						"4".equals(checkDutyVo.getIndemnityDuty())){
					return "车辆："+checkDutyVo.getLicenseNo()+" 事故责任为全责或者无责，不适用互碰自赔案件处理！请修改";
				}
			}
			
			if(!policyFlag){
				return "该案件没有用交强险报案，不适用互碰自赔案件处理！";
			}
		}else{// 非互碰自赔
			boolean carKind = this.getCarKind(registNo);
			for(PrpLCheckPropVo checkProp : checkProps){
				if(RadioValue.RADIO_YES.equals(checkProp.getIsNoClaim())){
					continue;
				}
				if(checkProp.getLossPartyId()==1&& !carKind){
					retData = "标的车未承保车上货物险，不能选择标的车作为财产损失方，请修改！";
				}
			}
			
			// 需求：若非互碰自赔案件只有商业险报案且查勘有三者车或者三者财或三者人，需提示请关联交强险，(前提，在本公司投保交强险)
			// 2016-11-3（查找到未起保的 可以关联的交强险保单，）--暂时放开管控
			if(policyInfo!=null&&policyInfo.size()==1){// 一张保单
				PrpLCMainVo cMainVo = policyInfo.get(0);
				boolean propFlag = false;
				for(PrpLCheckPropVo checkProp : checkProps){
					if(checkProp.getLossPartyId()!=1&&checkProp.getLossPartyId()!=0){
						propFlag = true;
					}
				}
				boolean personFlag = false;
				for(PrpLCheckPersonVo personVo:checkTaskVo.getPrpLCheckPersons()){
					if(personVo.getLossPartyId()!=1&&personVo.getLossPartyId()!=0){
						personFlag = true;
					}
				}
				
				if(checkTaskVo.getPrpLCheckCars().size()>1||personFlag||propFlag){
					// 单商业报案 且投保交强险
					int relatPolNo = policyViewService.findRelatedPolicyNo(cMainVo.getPolicyNo());
					if(!Risk.DQZ.equals(cMainVo.getRiskCode())&&relatPolNo>0){
						// return "非互碰自赔案件只有商业险报案且查勘有三者车或者三者财或三者人，请关联交强险！";
					}
				}
			}
			
		}
		
		if(policyFlag&&checkTaskVo.getPrpLCheckCars().size()==1
				&&checkTaskVo.getPrpLCheckProps().size()==0
				&&checkTaskVo.getPrpLCheckPersons().size()==0
			&&CodeConstants.RadioValue.RADIO_NO.equals(checkVo.getIsClaimSelf())){
			return "该案件不是互碰自赔，查勘只有标的车损失，请取消关联交强险!";
		}
		
		// 查勘存在人伤，调度没有人伤任务，需线下调度人伤任务
		if(!wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.PLoss,null,"")
				&&checkTaskVo.getPrpLCheckPersons().size()>0){
			return "查勘存在人伤损失，工作流没有人伤任务，请先调度人伤任务！";
		}
		
		// 代位案件
		if ( RadioValue.RADIO_YES.equals(checkVo.getIsSubRogation()) && !isSelected(registNo) ) {
			return "该案件为代位案件，请在上方公共按钮【索赔清单】勾选代位求偿案件索赔申请书！";
		}
		
//		checkTaskVo.getCheckerIdfNo();
		List<PrpLCheckPersonVo> personVoList = checkTaskVo.getPrpLCheckPersons();
		if (personVoList != null && personVoList.size() > 0){
			Map<String,String> idNoMap = new HashMap<String,String>();
			for(PrpLCheckPersonVo personVo : personVoList){
				boolean idTy = StringUtils.isNotBlank(personVo.getIdNo())
						&&CodeConstants.RadioValue.RADIO_YES.equals(personVo.getIdentifyType());
				if(idTy&&idNoMap.containsKey(personVo.getIdNo())){
					retData = "查勘信息：人员伤亡存在两名伤者的身份证号码相同，请修改！";
				}
				if(idTy){
					idNoMap.put(personVo.getIdNo(),personVo.getIdNo());
				}

				//校验查勘人员证件号码与伤亡人员身份证号不能一致
				if(StringUtils.isNotBlank(personVo.getIdNo()) && StringUtils.isNotBlank(checkTaskVo.getCheckerIdfNo()) &&
						personVo.getIdNo().equals(checkTaskVo.getCheckerIdfNo())){
					retData = "查勘信息：伤亡人员身份证号与查勘员身份证号相同，请修改！！！";
				}
			}
		}

		return retData;
	}

	// 查勘选择为代位案件则必须在查勘勾选索赔清单的（代位求偿案件索赔申请书）
	private boolean isSelected(String registNo){
		boolean isSelected = false;// 默认没有改选
		List<PrpLCertifyDirectVo> certifyDirectVoList = 
				certifyPubService.findCertifyDirectByRegistNo(registNo);
		if ( certifyDirectVoList != null && !certifyDirectVoList.isEmpty() ) {
			for ( PrpLCertifyDirectVo certifyDirectVo : certifyDirectVoList) {
				String itemCode = certifyDirectVo.getLossItemCode();
				// 新影像上线之后，单证编码前面会带上二级目录
				if (itemCode != null && itemCode.indexOf("C0102") > 0) {// 代位求偿案件索赔申请书
					isSelected = true;
				}
			}
		}
		return isSelected;
	}
	
	@Override
	public String saveValidCar(int seriNo,String registNo,String licenseNo,String frameNo,
	    String vinNo,Long carId,String idNoType,String idNo,Date checkTime, String licenseType,String drivingLicenseNo,
	    String carkindcode) {
		String msgData = "ok";
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		List<PrpLCheckCarVo> checkCarVoList = findPrpLcheckCarVoByRegistNo(registNo);
		if(seriNo != 1 && StringUtils.isBlank(carkindcode)){
			return "车辆种类不能为空！";
		}
		
		for(PrpLCheckCarVo checkCarVo : checkCarVoList){
			Long checkCarId = checkCarVo.getCarid();
			String carLicenseNo = checkCarVo.getPrpLCheckCarInfo().getLicenseNo();
			String carFrameNo = checkCarVo.getPrpLCheckCarInfo().getFrameNo();
			String carVinNo = checkCarVo.getPrpLCheckCarInfo().getVinNo();
			String carLicenseType = checkCarVo.getPrpLCheckCarInfo().getLicenseType();
			if(carId==0L||checkCarId.longValue()!=carId.longValue()){
				if(StringUtils.strip(licenseNo).equals(StringUtils.strip(carLicenseNo)) && StringUtils.strip(licenseType).equals(StringUtils.strip(carLicenseType))){
					return "该车辆车牌号码、号牌种类已存在，请修改！";
				}
				if(StringUtils.strip(frameNo).equals(StringUtils.strip(carFrameNo))){
					return "该车辆车架号已存在，请修改！存在的车架号【"+carFrameNo+"】";
				}
				if(StringUtils.strip(vinNo).equals(StringUtils.strip(carVinNo))){
					return "该车辆VIN码已存在，请修改！存在的VIN码【"+carVinNo+"】";
				}
				String oldIdNoType = checkCarVo.getPrpLCheckDriver().getIdentifyType();
				String oldIdNo = checkCarVo.getPrpLCheckDriver().getIdentifyNumber();
				String oldDrivingLicenseNo = checkCarVo.getPrpLCheckDriver().getDrivingLicenseNo();
				if(oldIdNoType.equals("1") && idNoType.equals("1") && StringUtils.strip(oldIdNo).equals(StringUtils.strip(idNo))){
					return "该车辆驾驶人证件号码已存在，请修改！存在的驾驶人证件号码【"+idNo+"】";
				}
				// 平台只校验证件号码是否一致，以平台校验为准
				if(StringUtils.strip(drivingLicenseNo).equals(StringUtils.strip(oldDrivingLicenseNo))){
					return "该车辆驾驶证号码已存在，请修改！存在的驾驶证号码【"+drivingLicenseNo+"】";
				}
			}
		}
		//驾驶员身份证号与驾驶证号应该一致
		if(StringUtils.isNotBlank(idNoType) && "1".equals(idNoType) && StringUtils.isNotBlank(idNo) 
				&& StringUtils.isNotBlank(drivingLicenseNo) && !idNo.equals(drivingLicenseNo)){
			return "驾驶员身份证号与驾驶证号应该相同，请修改！！！";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Long str1 = Long.parseLong(sdf.format(checkTime).substring(0,8));
		Long str2 = Long.parseLong(sdf.format(registVo.getReportTime()).substring(0,8));
		// 查勘日期不能小于报案日期
		if(checkTime != null && str1 < str2){
			return "查勘日期不能小于报案日期，请修改！";
		}
		return msgData;
	}

	public double getSumLossFee(PrpLCheckVo checkVo) {
		PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
		BigDecimal num = new BigDecimal(0);
		BigDecimal carFee = new BigDecimal(0);
		BigDecimal propFee = new BigDecimal(0);
		BigDecimal resFee = new BigDecimal(0);
		if(checkTaskVo.getSumLossCarFee()!=null){
			carFee = checkTaskVo.getSumLossCarFee();
		}
		if(checkTaskVo.getSumLossPropFee()!=null){
			propFee = checkTaskVo.getSumLossPropFee();
		}
		if(checkTaskVo.getSumRescueFee()!=null){
			resFee = checkTaskVo.getSumRescueFee();
		}
		num = NullToZero(carFee).add(NullToZero(propFee)).add(NullToZero(resFee));
		return num.doubleValue();
	}

	/** 复制报案主信息给查勘主表prplcheck和Task */
	private void copyRegistToCheckVo(PrpLCheckVo checkVo,PrpLRegistVo registVo,
	        PrpLScheduleTaskVo scheduleTaskVo,SysUserVo userVo) {
		Date nowDate = new Date();
		String userCode = userVo.getUserCode();
//		String comCode = userVo.getComCode();
		checkVo.setRegistNo(scheduleTaskVo.getRegistNo());
		int i = 0;
		for (PrpLScheduleItemsVo itemVo : scheduleTaskVo.getPrpLScheduleItemses()) {
			if (("1".equals(itemVo.getItemType())|| "2".equals(itemVo.getItemType()))
					&& itemVo.getFlag() == null) {
				i++;
			}
		}
		checkVo.setSingleAccidentFlag(i>1?RadioValue.RADIO_NO:RadioValue.RADIO_YES);
		checkVo.setMercyFlag(registVo.getMercyFlag());// 案件紧急程度
		checkVo.setDamageCode(registVo.getDamageCode());// 出险代码
		checkVo.setDamOtherCode(registVo.getDamageOtherCode()); //出险原因2
		checkVo.setLossType(RadioValue.RADIO_NO);// 是否全损
		checkVo.setMajorCaseFlag(RadioValue.RADIO_NO);// 是否重大赔案上报
		checkVo.setAccidentReason(registVo.getAccidentReason()); // 事故原因
		// 查勘类别
		PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(userCode);
		if(intermVo!=null){
			checkVo.setRemark(intermVo.getIntermCode());
		}
		checkVo.setCheckClass(intermVo!=null ? CheckClass.CHECKCLASS_Y : CheckClass.CHECKCLASS_N);
		PrpLRegistExtVo prpLregistExtVo = registVo.getPrpLRegistExt();
		if(prpLregistExtVo!=null){
			checkVo.setDamageTypeCode(prpLregistExtVo.getAccidentTypes());// 事故类型
			checkVo.setManageType(registVo.getPrpLRegistExt().getManageType());// 事故处理类型
			checkVo.setCheckType("1".equals(prpLregistExtVo.getIsOnSitReport()) ? "3" : "4");// 查勘类型
			// prpLcheckTemp.setClaimType("");
			checkVo.setIsClaimSelf(prpLregistExtVo.getIsClaimSelf());// 是否互碰自赔
			checkVo.setIsPropLoss(prpLregistExtVo.getIsPropLoss());// 是否有物损
			checkVo.setIsPersonLoss(prpLregistExtVo.getIsPersonLoss());// 是否有人伤
			checkVo.setIsSubRogation(prpLregistExtVo.getIsSubRogation());// 是否代位求偿
		}
		// Task
		PrpLCheckTaskVo checkTaskVo = new PrpLCheckTaskVo();
		checkTaskVo.setRegistNo(registVo.getRegistNo());// 报案号
		checkTaskVo.setLinkerName(registVo.getLinkerName());// 联系人
		checkTaskVo.setLinkerNumber(registVo.getLinkerPhone());//DATE
		checkTaskVo.setLinkerMobile(registVo.getLinkerMobile());
		checkTaskVo.setFirstAddressFlag(CodeConstants.RadioValue.RADIO_YES);// 是否第一现场查勘
		checkTaskVo.setCheckDate(nowDate);// 查勘日期
		// String areaCode = scheduleTaskVo.getCheckorDeflossAreaCode();//约定查勘地点省市区
		//String addressCode = codeTranService.transCode("AreaCode",areaCode);
		//String address = addressCode.replaceAll("-","");
//		String address = scheduleTaskVo.getCheckAddress();
		String address = scheduleTaskVo.getCheckareaName();
//		String checkAddress = address.substring(3,address.length());
		checkTaskVo.setRemark(scheduleTaskVo.getRegionCode());// 查勘片区代码
		checkTaskVo.setCheckAddress(address);// 查勘地点
		checkTaskVo.setCheckerIdfNo(userVo.getIdentifyNumber());// 查勘人身份证号 (当前登录用户/接收任务的机构和用户)
		checkTaskVo.setCheckerPhone(userVo.getMobile());// 查勘人联系电话
		checkVo.setPrpLCheckTask(checkTaskVo);

		checkVo.setRegistNo(scheduleTaskVo.getRegistNo());
		checkVo.setCreateUser(userCode);
		checkVo.setCreateTime(nowDate);
		checkVo.setUpdateUser(userCode);
		checkVo.setUpdateTime(nowDate);

	}

	@Override
	public Map<String,String> getCarLossParty(String registNo) {
		List<PrpLCheckCarVo> checkCarVos = findPrpLcheckCarVoByRegistNo(registNo);
		Map<String,String> result = new HashMap<String,String>();
		for(PrpLCheckCarVo carvo:checkCarVos){
			String serialNo = carvo.getSerialNo().toString();
			String licNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
			result.put(serialNo,1==carvo.getSerialNo() ? "标的车("+licNo+")" : "三者车("+licNo+")");
			result.put("0","地面/路人损失");
		}
		return result;
	}

	// 获取代位的三者车车牌号下拉框
	@Override
	public Map<String,String> getSubLicenseNo(String registNo){
		List<PrpLCheckCarVo> checkCarVos = findPrpLcheckCarVoByRegistNo(registNo);
		Map<String,String> result = new HashMap<String,String>();
		for(PrpLCheckCarVo carvo:checkCarVos){
			String serNo = carvo.getSerialNo().toString();
			String licNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
			if(1!=carvo.getSerialNo()){
				result.put(serNo,licNo);
			}
		}
		result.put("0","");
		return result;
	}
	
	/**
	 * 根据调度、查勘 时车辆信息 组织为查勘的车辆信息
	 * @param scheduleTaskVo
	 * @param registVo
	 * @return PrpLCheckCarVo
	 */
	private PrpLCheckCarVo createCheckCarVo(PrpLScheduleTaskVo scheduleTaskVo,PrpLScheduleItemsVo schduItemVo,
			PrpLRegistVo registVo,PrpLRegistCarLossVo registCarLossVo,PrpLCMainVo cmainVo,String assignUser) {
		PrpLCheckCarVo checkCarVo = new PrpLCheckCarVo();

		Date nowDate = new Date();
		
		checkCarVo.setRegistNo(schduItemVo.getRegistNo());// 报案号
		checkCarVo.setSerialNo(Integer.parseInt(schduItemVo.getSerialNo()));// 车辆序号
		checkCarVo.setLossPart(registCarLossVo.getLosspart());// 损失部位

		checkCarVo.setScheduleitem(schduItemVo.getId());
		// checkCarVo.setPrpLCheckTask(checkPo.getPrpLCheckTask());//
		checkCarVo.setRegistNo(schduItemVo.getRegistNo());// 报案号
		checkCarVo.setSerialNo(Integer.parseInt(schduItemVo.getSerialNo()));// 调度车辆序号
		checkCarVo.setLossFlag(CodeConstants.RadioValue.RADIO_NO);
		// String areaCode = scheduleTaskVo.getCheckorDeflossAreaCode();//约定查勘地点省市区代码
		//String addressCode = codeTranService.transCode("AreaCode",areaCode);
		//String address = addressCode.replaceAll("-","");
		String address = scheduleTaskVo.getCheckareaName();
//		String checkAddress = address.substring(3,address.length());
		checkCarVo.setCheckAdress(address);// 查勘地点
		checkCarVo.setCheckTime(nowDate);// 查勘时间
		String lossPart = registCarLossVo.getLosspart();
		if(StringUtils.isNotBlank(lossPart)){// 排除报案新增的无损失和不详
			lossPart = lossPart.replace("12","").replace("13","");
		}
		checkCarVo.setLossPart(lossPart);// 损失部位

		String reportType = registVo.getReportType();
		String duty = registVo.getPrpLRegistExt().getObliGation();
		if(CodeConstants.ReportType.BI.equals(reportType)){// 单商业报案
			checkCarVo.setCiIndemDuty("");
			checkCarVo.setIndemnityDuty(duty);
		}else if(CodeConstants.ReportType.CI.equals(reportType)){// 单交强报案时
			checkCarVo.setCiIndemDuty(CodeConstants.DutyType.CIINDEMDUTY_Y);
			checkCarVo.setIndemnityDuty("");
		}else{// 交强商业报案
			checkCarVo.setCiIndemDuty(CodeConstants.DutyType.CIINDEMDUTY_Y);
			checkCarVo.setIndemnityDuty(duty);
		}

		PrpLCheckCarInfoVo checkCarInfoVo = new PrpLCheckCarInfoVo();
		checkCarInfoVo.setLicenseNo(registCarLossVo.getLicenseNo());// 车牌号
		checkCarInfoVo.setBrandName(registCarLossVo.getBrandName());// 车型名称
		if(cmainVo!=null){// 标的车
			PrpLCItemCarVo cItemCarVo = cmainVo.getPrpCItemCars().get(0);
			checkCarInfoVo.setModelCode(cItemCarVo.getModelCode());// 车型代码
			checkCarInfoVo.setFrameNo(cItemCarVo.getFrameNo());// 车架号
			checkCarInfoVo.setVinNo(cItemCarVo.getVinNo());
			checkCarInfoVo.setStartDate(cmainVo.getStartDate());// 起保日期
			checkCarInfoVo.setCarOwner(cItemCarVo.getCarOwner());// 车主
			checkCarInfoVo.setEngineNo(cItemCarVo.getEngineNo());// 车架号
			checkCarInfoVo.setVinNo(cItemCarVo.getVinNo());// VIN号
			checkCarInfoVo.setLicenseColor(cItemCarVo.getLicenseColorCode());// 车牌底色代码
			checkCarInfoVo.setCarColorCode(cItemCarVo.getColorCode());// 车身颜色代码
			checkCarInfoVo.setEnrollDate(cItemCarVo.getEnrollDate());// 初次登记日期
			checkCarInfoVo.setInsurecomcode("");// 承保(机构)公司代码
			checkCarInfoVo.setLicenseType(CodeConvertTool.getVehicleCategory(cItemCarVo.getLicenseKindCode()));// 车牌种类

		}else if("1".equals(registVo.getIsQuickCase())){
			// 河南快赔三者车
			checkCarInfoVo.setVinNo(registCarLossVo.getFrameNo());
			checkCarInfoVo.setFrameNo(registCarLossVo.getFrameNo());
			checkCarInfoVo.setCarOwner(registCarLossVo.getThriddrivername());
			checkCarInfoVo.setPhone(registCarLossVo.getThriddriverphone());
		}

		
		checkCarInfoVo.setBiInsureComCode(registCarLossVo.getInspolicybi());// 商业险承保公司
		checkCarInfoVo.setCiInsureComCode(registCarLossVo.getInspolicyci());// 交强险承保公司

		PrpLCheckDriverVo checkDriverVo = new PrpLCheckDriverVo();
		checkDriverVo.setDriverName(registCarLossVo.getThriddrivername());// 驾驶人姓名
		checkDriverVo.setLinkPhoneNumber(registCarLossVo.getThriddriverphone());// 驾驶人电话
		checkDriverVo.setRegistNo(registVo.getRegistNo());
		checkDriverVo.setDriverSex(registCarLossVo.getThriddriversex());//
		checkDriverVo.setIdentifyNumber(registCarLossVo.getThriddrivingno());// 驾驶员身份证
		checkDriverVo.setDrivingLicenseNo(registCarLossVo.getThriddrivingno());// 驾驶证号
		checkDriverVo.setIdentifyType("1");// 证件类型
		String phone = registCarLossVo.getThriddriverphone();
		if(phone != null && phone.length() < 15){
			checkDriverVo.setLinkPhoneNumber(phone);// 驾驶人电话
		}
		checkDriverVo.setValidFlag(ValidFlag.VALID);

		
		checkCarVo.setCreateUser(assignUser);
		checkCarVo.setCreateTime(nowDate);
		checkCarVo.setUpdateUser(assignUser);
		checkCarVo.setUpdateTime(nowDate);

		checkCarVo.setPrpLCheckCarInfo(checkCarInfoVo);
		checkCarVo.setPrpLCheckDriver(checkDriverVo);
		
		return checkCarVo;
	}
	
	// 保存代位信息
	/* 
	 * @see ins.sino.claimcar.check.service.CheckHandleService#saveSubrogationMain(ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo, java.lang.Long, java.lang.String)
	 * @param subMainVo
	 * @param checkId
	 * @param assignUser
	 * @return
	 */
	@Override
	public Long saveSubrogationMain(PrpLSubrogationMainVo subMainVo,Long checkId,String assignUser){
		Long subr = null;
		Date nowDate = new Date();
		PrpLCheckVo checkVo = findPrpLCheckVoById(checkId);
		subMainVo.setRegistNo(checkVo.getRegistNo());
		subMainVo.setSubrogationFlag(checkVo.getIsSubRogation());
		subMainVo.setValidFlag(ValidFlag.VALID);
		
		if(subMainVo.getId() != null){
			subMainVo.setUpdateTime(nowDate);
			subMainVo.setUpdateUser(assignUser);
		}else{
			subMainVo.setCreateTime(nowDate);
			subMainVo.setCreateUser(assignUser);
		}
		for(PrpLSubrogationCarVo carVo : subMainVo.getPrpLSubrogationCars()){
			if(carVo.getId()!=null){
				carVo.setUpdateTime(nowDate);
				carVo.setUpdateUser(assignUser);
			}else{
				carVo.setCreateTime(nowDate);
				carVo.setCreateUser(assignUser);
			}
		}
		for(PrpLSubrogationPersonVo personVo : subMainVo.getPrpLSubrogationPersons()){
			if(personVo.getId()!=null){
				personVo.setUpdateTime(nowDate);
				personVo.setUpdateUser(assignUser);
			}else{
				personVo.setCreateTime(nowDate);
				personVo.setCreateUser(assignUser);
			}
		}
		if(subMainVo.getId()!=null && "0".equals(subMainVo.getSubrogationFlag())){
			subrogationService.deleteSubrogationInfo(subMainVo);
			subr = 0L;
		}else{
			subr = subrogationService.saveSubrogationInfo(subMainVo);
		}
		return subr;
	}
	
	/**
	 * 初始化定损最优人员
	 * @param prpLScheduleDefLossList
	 * @throws Exception
	 */
	private void setDlossPerson(PrpLCheckTaskVo checkTaskVo,List<PrpLScheduleDefLossVo> prpLScheduleDefLossList,
	                            PrpLScheduleTaskVo schTaskVo,String url) throws Exception{
		PrpLRegistVo registVo = registQueryService.findByRegistNo(checkTaskVo.getRegistNo());
        PersonnelInformationReqVo reqVo = new PersonnelInformationReqVo();
		
		HeadReq head = new HeadReq();
		
		head.setRequestType("PersonnelInformation");
		
		PersonnelInformationReqBody reqBody = new PersonnelInformationReqBody();
		
		List<PersonnelInformationReqScheduleItem> scheduleItemList = new ArrayList<PersonnelInformationReqScheduleItem>();
		
		PersonnelInformationReqScheduleWF reqScheduleWf = new PersonnelInformationReqScheduleWF();
		reqScheduleWf.setRegistNo(checkTaskVo.getRegistNo());
		reqScheduleWf.setPolicyNo(registVo.getPolicyNo());
		reqScheduleWf.setType("1");// 0-报案环节提交， 1-查勘提交调度
		setReqScheduleWf(reqScheduleWf, checkTaskVo.getRegistNo());
		/*reqScheduleWf.setBusinessType("all");
		reqScheduleWf.setCustomType("all");
		reqScheduleWf.setCaseType("all");*/
		
		for (PrpLScheduleDefLossVo vo : prpLScheduleDefLossList) {
			if(vo == null) continue;
			PersonnelInformationReqScheduleItem reqItem = new PersonnelInformationReqScheduleItem();
			String serialNo = vo.getSerialNo().toString();
			reqItem.setTaskId(serialNo);
			if(StringUtils.equals(vo.getDeflossType(), CodeConstants.DeflossType.PropLoss)) {
				reqItem.setNodeType(FlowNode.DLProp.toString());
				reqItem.setItemNo(serialNo);
			} else {
				reqItem.setNodeType(FlowNode.DLCar.toString());
				reqItem.setItemNo(serialNo);
			}
			reqItem.setItemName(vo.getItemsName());
			String regionCode = schTaskVo.getRegionCode();
			String[] code = areaDictService.findAreaByAreaCode(schTaskVo.getProvinceCityAreaCode(),"");
			if(code != null && code.length>0){
				reqItem.setProvinceCode(code[0]);
				reqItem.setCityCode(code[1]);
			}
//			String provinceCode = code[0];
//			String cityCode = code[1];
			reqItem.setRegionCode(regionCode);
			reqItem.setDamageAddress(schTaskVo.getCheckareaName());
			reqItem.setLngXlatY(schTaskVo.getCheckAddressMapCode());
			// 自定义区域编码
			// 调度保存最新自定义区域编码
			if(StringUtils.isNotEmpty(schTaskVo.getSelfDefinareaCode())){
				reqItem.setSelfDefinareaCode(schTaskVo.getSelfDefinareaCode());
			}
			scheduleItemList.add(reqItem);
		}
		
		reqBody.setScheduleWF(reqScheduleWf);
		reqBody.setScheduleItemList(scheduleItemList);
		
		reqVo.setHead(head);
		reqVo.setBody(reqBody);
		
		PersonnelInformationResVo resVo = mobileCheckService.getPersonnelInformation(reqVo,url);
		List<PersonnelInformationResScheduleItem> list = resVo.getBody().getScheduleItemList();
		PrplQuickUserVo prplQuickUserVo = new PrplQuickUserVo();
		if("1".equals(registVo.getIsQuickCase())){
		    prplQuickUserVo = quickUserService.findQuickUser();
        }
		// 默认设置推荐的人
		for (PrpLScheduleDefLossVo vo : prpLScheduleDefLossList) {
			if(vo == null) continue;
			Map<String,String> showMap = new HashMap<String,String>();
			if("0".equals(vo.getTaskFlag())){
			    if("1".equals(registVo.getIsQuickCase())){
			        vo.setScheduledUsercode(prplQuickUserVo.getUserCode());
                    vo.setScheduledComcode(prplQuickUserVo.getComCode());
                    showMap.put(prplQuickUserVo.getUserCode() +","+prplQuickUserVo.getComCode(),prplQuickUserVo.getUserName());
                    if(showMap.isEmpty()){
                        showMap.put("","");
                    }
                    vo.setShowMap(showMap);
                    continue;
                }
				// 如果是联共保案件，定损人员取查勘处理人员
				if(StringUtils.isNotEmpty(registVo.getIsGBFlag()) && "24".indexOf(registVo.getIsGBFlag()) != -1){
					PrpLWfTaskVo taskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(checkTaskVo.getRegistNo(), FlowNode.Chk.name());
					if(taskVo != null){
						vo.setScheduledUsercode(taskVo.getAssignUser());
						vo.setScheduledComcode(taskVo.getAssignCom());
						showMap.put(taskVo.getAssignUser() +","+taskVo.getAssignCom(),codeTranService.transCode("UserCode", taskVo.getAssignUser()));
					}
					if(showMap.isEmpty()){
                        showMap.put("","");
                    }
                    vo.setShowMap(showMap);
                    continue;
				}
				
				for(PersonnelInformationResScheduleItem item:list){
//					if(StringUtils.equals(item.getTaskId(), vo.getSerialNo().toString())){
//					}
					vo.setScheduledUsercode(item.getNextHandlerCode());
					vo.setScheduledComcode(item.getScheduleObjectId());
					showMap.put(item.getNextHandlerCode() +","+item.getScheduleObjectId(),item.getNextHandlerName());
					if(showMap.isEmpty()){
						showMap.put("","");
					}
				}
				
				vo.setShowMap(showMap);
			}
		}
	}
	
	/* 
	 * @see ins.sino.claimcar.check.service.CheckHandleService#sendCheckToPlatform(java.lang.String)
	 * @param registNo
	 * @throws Exception
	 */
	@Override
	public void sendCheckToPlatform( String registNo ) throws Exception
	{
		/* 保单关联与取消的问题，1-报案两张保单，查勘取消了一张，2-报案一张保单，查勘关联了另一张保单 */
		List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo( registNo );
		if ( cMainVos != null && cMainVos.size() > 0 )
		{
			for ( PrpLCMainVo cMainVo : cMainVos )
			{
				/* 查勘信息送平台,除上海机构 */
				if ( "22".equals( cMainVo.getComCode().substring( 0, 2 ) ) )
					continue;
				if(Risk.DQZ.equals(cMainVo.getRiskCode())) /* 交强保单 */
				{
					interfaceAsyncService.sendCheckToPlatformCI(registNo);
				} else {
					interfaceAsyncService.sendCheckToPlatformBI(registNo);
				}
				/* ---- */
			}
		}
	}
	
	private void setReqScheduleWf(PersonnelInformationReqScheduleWF reqScheduleWf,String registNo) {
		// 设置
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
		String businessPlate = "";
		if(prpLCMainVoList!=null&&prpLCMainVoList.size()>0){
			businessPlate = CodeTranUtil.transCode("businessPlate",prpLCMainVoList.get(0).getBusinessPlate());
			// 代理人编码
			if(prpLCMainVoList.get(0).getAgentCode()!=null){
				reqScheduleWf.setAgentCode(prpLCMainVoList.get(0).getAgentCode());
			}
			// 保单归属地编码
			if(prpLCMainVoList.get(0).getComCode()!=null){
					String code = areaDictService.findAreaList("areaCode",registVo.getPrpLRegistExt().getCheckAddressCode());
				// 承保地区
					String comCode = "";
					if(prpLCMainVoList.size()==2){
						for(PrpLCMainVo vo:prpLCMainVoList){
							if(("12").equals(vo.getRiskCode().substring(0, 2))){
								comCode = vo.getComCode();
							}
						}
					}else{
						comCode = prpLCMainVoList.get(0).getComCode();
					}
				// 保单归属地编码
					reqScheduleWf.setComCode(comCode);
				// 是否异地案件
					if(code != null && comCode!=""){
					if("0002".equals(code.substring(0,4))){// 深圳
							if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
							reqScheduleWf.setIsElseWhere("1");// 是
			 				}else{
							reqScheduleWf.setIsElseWhere("0");// 否
							}
						}else{
						if("0002".equals(comCode.substring(0,4))){// 添加深圳的单这种情况
								if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
								reqScheduleWf.setIsElseWhere("1");// 是
								}else{
								reqScheduleWf.setIsElseWhere("0");// 否
								}
							}else{
								if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
								reqScheduleWf.setIsElseWhere("1");// 是
								}else{
								reqScheduleWf.setIsElseWhere("0");// 否
								}
							}
						}
					}else{
					reqScheduleWf.setIsElseWhere("1");// 是
					}
				}
		}else{
			reqScheduleWf.setAgentCode("all");
			reqScheduleWf.setIsElseWhere("all");
		}

		// if(businessPlate !="" && businessPlate != null){
		if(StringUtils.isNotBlank(businessPlate)){
			reqScheduleWf.setBusinessType(businessPlate);// 业务类型
		}else{
			reqScheduleWf.setBusinessType("all");// 业务类型
		}
		// TODO 到时取大客户的值
		if(prpLCMainVoList!=null&&prpLCMainVoList.size()>0){
			if(StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentName()) ||
					StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentCode())){
				reqScheduleWf.setCustomType("2");// 客户分类
			}else{
				reqScheduleWf.setCustomType("3");// 客户分类
			}

		}else{
			reqScheduleWf.setCustomType("all");// 客户分类
		}
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		if("DM04".equals(prpLRegistVo.getDamageCode())){// 全车盗抢
			reqScheduleWf.setCaseType("3");
		}else if("DM02".equals(prpLRegistVo.getDamageCode())){// 玻璃案件
			reqScheduleWf.setCaseType("2");
		}else if(prpLRegistVo.getPrpLRegistPersonLosses()!=null&&prpLRegistVo.getPrpLRegistPersonLosses().size()>0){
			// 人伤
			reqScheduleWf.setCaseType("5");
		}else if("2".equals(prpLRegistVo.getPrpLRegistExt().getCheckType())){// 快处快赔
			reqScheduleWf.setCaseType("4");
		}else{
			reqScheduleWf.setCaseType("1");// 案件类型
		}
	}
	
	
	
	
	
	
	//--------------------------------------------------------------------------util---------------------------------
	
	@Override
	public List<PrpLCheckCarVo> findPrpLcheckCarVoByRegistNo(String registNo) {
		return checkService.findCheckCarVoByRegistNo(registNo);
	}
	
	@Override
	public void saveDisaster(PrpLDisasterVo disasterVo){
		checkService.saveDisaster(disasterVo);
	}
	
	@Override
	public PrpLCheckVo findPrpLCheckVoById(Long id) {
		return checkService.findPrpLCheckVoById(id);
	}
	
	@Override
	public PrpLCheckVo queryPrpLCheckVo(String registNo){
		return checkService.findPrpLCheckByRegistNo(registNo);
	}
	
	@Override
	public PrpLCheckCarVo findCarIdBySerialNoAndRegistNo(Integer serialNo,String registNo){
		return checkService.findCarIdBySerialNo(serialNo,registNo);
	}
	
	@Override
	public void dropPropLoss(Long id) throws Exception{
		checkService.deleteCheckProp(id);
	}
	
	@Override
	public void dropPersonLoss(Long id) throws Exception{
		checkService.deleteCheckPerson(id);
	}

	@Override
	public PrpLCheckCarVo findCheckCarById(Long carId) {
		return checkService.initPrpLCheckCar(carId);
	};
	
	@Override
	public PrpLDisasterVo findDisasterVoByRegistNo(String registNo){
		return checkService.findDisasterVoByRegistNo(registNo);
	}
	
	@Override
	public void updatePrplCheckCarInfo(PrpLCheckCarInfoVo prplCheckCarInfoVo){
		checkService.updatePrplCheckCarInfo(prplCheckCarInfoVo);
	}
	
	@Override
	public Map<String,String> findByChkBigAndGradeid(String nodeCode,String comCode,String level){
		Map<String,String> userMap = new HashMap<String,String>();
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("SELECT * FROM ywuser.prpduser WHERE usercode in ");
		sqlUtil.append("(SELECT grade.usercode FROM saa_usergrade grade ,  v_saa_userpermitcompany p ");
		sqlUtil.append("WHERE grade.usercode = p.usercode ");
		sqlUtil.append("AND grade.gradeid in (?,?,?,?,?) ");
		if("PLBig".equals(nodeCode)){
			sqlUtil.addParamValue("5128");
			sqlUtil.addParamValue("5128");
			sqlUtil.addParamValue("5128");
			sqlUtil.addParamValue("5128");
			sqlUtil.addParamValue("5128");
		}else{
			sqlUtil.addParamValue("5122");
			sqlUtil.addParamValue("5123");
			sqlUtil.addParamValue("5124");
			sqlUtil.addParamValue("5125");
			sqlUtil.addParamValue("5126");
		}
		if("1".equals(level)){// 分公司
			sqlUtil.append(" AND p.comCode like ? )");
			sqlUtil.addParamValue(comCode.substring(0,2)+"%");
		}else{
			sqlUtil.append(" AND p.comCode like ? ");
			sqlUtil.addParamValue("00%");
			sqlUtil.append(" AND p.comCode not like ? )");
			sqlUtil.addParamValue("0002%");
		}
		sqlUtil.append(" AND validStatus = ? ");
		sqlUtil.addParamValue("1");
		
		// 查询参数
		String sql = sqlUtil.getSql();
		logger.debug("SQL:"+sql);
		Object[] values = sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		// 对象转换
		for(int i = 0; i<objects.size(); i++ ){
			Object[] obj = objects.get(i);
			String userCode = obj[0]==null ? "0000000000" : obj[0].toString();
			String userName = obj[1]==null ? "系统管理员" : obj[1].toString();
			userMap.put(userCode,userName);
		}
		return userMap;
	}

    @Override
    public boolean isClaimForce(String registNo) {
        List<PrpLClaimVo> claimList = claimService.findClaimListByRegistNo(registNo);
        if(claimList != null && claimList.size() > 0){
            for(PrpLClaimVo claim : claimList){
				if("5".equals(claim.getClaimFlag())){ // 强制立案
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    public void photoVerifyToHNQC(String registNo,String photoStatus,String checkId,
    		String checkStatus,String dataType,SysUserVo userVo) throws Exception{
    	
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
    	vo.setIsass("1");
    	vo.setOperateuser(userVo.getUserCode());
    	try{
    		interfaceAsyncService.receiveauditingresult(vo);
    	}catch(Exception e){
			logger.info("发送定损照片审核结果报错信息： "+e.getMessage());
			e.printStackTrace();
    	}
		// 照片审核不通过才赋值给查勘主表的photoStatus字段，审核通过则字段为空
    	if("0".equals(dataType) && "0".equals(checkStatus)){
    		PrpLCheckVo checkVo = this.findPrpLCheckVoById(Long.valueOf(checkId));
    		checkVo.setPhotoStatus(photoStatus);
    		checkService.updateCheckMain(checkVo);
    	}
    }
    
	/** 查勘提交，人伤费用自动审核 **/
    public void autoPersSubmitForChe(PrpLCheckVo checkVo,SysUserVo userVo,List<PhotoInfo> photoInfoList){
    	String registNo = checkVo.getRegistNo();
    	PrpLWfTaskVo taskVo = null;
    	List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(registNo, FlowNode.PLFirst.name());
    	if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
			// 流入时间降序排
            Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
            @Override
            public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                    return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                }
            });
            taskVo = prpLWfTaskVoList.get(0);
        }
		// 人伤费用自核
		// 请求ILOG
    	LIlogRuleResVo persResVo = persReqIlogService.reqIlogByChe(checkVo, userVo,FlowNode.PLCharge.name(),taskVo,photoInfoList);
    	
    	String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
    	boolean finalAutoPass = true;
    	if ("1".equals(finalPowerFlag)) {
    		// 	兜底人员权限判断
    		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
    		// 	存在兜底人员
    		if (powerInfoVo != null) {
    			BigDecimal gradeAmount = powerInfoVo.getGradeAmount();
    			// 	兜底人员权限金额不为空
    			if (gradeAmount != null) {
    				List<PrpLCheckPersonVo> personVoList = checkVo.getPrpLCheckTask().getPrpLCheckPersons();
    				BigDecimal sumLossFee = BigDecimal.ZERO;
    				if (personVoList != null && personVoList.size() > 0) {
    					// 	统计人伤定损总金额
    					for (PrpLCheckPersonVo personVo : personVoList) {
    						sumLossFee = sumLossFee.add(NullToZero(personVo.getLossFee()));
    					}
    				}
    				//	总损失金额在权限范围内自动审核通过
    				if (sumLossFee.compareTo(gradeAmount) == 1) {
    					finalAutoPass = false;
    				}
    			}
    		} else {
    			finalAutoPass = false;
    		}
    	}
		// 人伤费用自核
    	if("1".equals(persResVo.getUnderwriterflag()) && finalAutoPass){
    		persTraceHandleService.autoPersSubmitForChe(taskVo,checkVo,userVo);
    		try{
				// 人伤节点完成送平台(注：需要根据保单的comCode来选择送不同的平台--需修改，)
//				logger.info("registNo:"+checkVo.getRegistNo());
//				interfaceAsyncService.sendLossToPlatform(registNo,null);
				// 刷新立案
				claimService.updateClaimFee(registNo,userVo.getUserCode(),FlowNode.PLCharge_LV0);
			}catch(Exception e){
				logger.error("报案号" + (checkVo == null ? null : checkVo.getRegistNo()) + "人伤节点自核 刷新立案 失败！",e);
			}
    	}
    }
    
    @Override
    public void updateSingleAccidentFlag(String registNo){
    	PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
    	int scheduleCarSize = scheduleService.findScheduleDefLossList(registNo, "1").size();
    	int scheduleDefLossSize = scheduleService.findScheduleDefLossList(registNo, null).size();
    	int schedulePersSize = scheduleService.findScheduleTask(registNo, "1", "1").size();
    	/*List<PrpLCheckDutyVo>  checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
    	List<PrpLWfTaskVo> carTaskVoList = wfFlowQueryService.findWfTaskVo(registNo, FlowNode.DLCar, "0");
    	int checkDutySize = checkDutyList!=null ? checkDutyList.size():0;
    	int carTaskSize = carTaskVoList!=null ?carTaskVoList.size():0;
    	if((checkDutySize==1 && (carTaskSize==0 || (carTaskSize==1 && ("2".equals(carTaskVoList.get(0).getWorkStatus()) || "6".equals(carTaskVoList.get(0).getWorkStatus()))))) || (checkDutySize==0 && carTaskSize==1)){
    		checkVo.setSingleAccidentFlag("1");
    		checkVo.setDamageTypeCode("01");
    		checkTaskService.updateCheck(checkVo);
    	}*/
    	if(checkVo != null){
			// 单车事故
    		if(scheduleCarSize>1){
        		checkVo.setSingleAccidentFlag("0");
        	}else{
        		checkVo.setSingleAccidentFlag("1");
        	}
			// 事故类型
        	if(scheduleDefLossSize==1 && schedulePersSize==0){
        		checkVo.setDamageTypeCode("01");
        	}else if((scheduleDefLossSize+schedulePersSize)==2){
        		checkVo.setDamageTypeCode("02");
        	}else if((scheduleDefLossSize+schedulePersSize)>2){
        		checkVo.setDamageTypeCode("03"); 
        	}else{
        		checkVo.setDamageTypeCode("01");
        	}
        	checkTaskService.updateCheck(checkVo);
    	}
    }

	@Override
	public List<PrpLCheckCarInfoVo> findAllInfoByRegistNo(String registNo) {
		return checkService.findAllInfoByRegistNo(registNo);
	}
	@Override
	public <E> String saveValidCars(List<E> carList) {
		for(int i = 0;i<carList.size();i++){
			E tempCar = carList.get(i);
		    if(tempCar instanceof CTCarInfoVo){
		    	 CTCarInfoVo carInfoVo = (CTCarInfoVo) tempCar;
		    	 String tempLicenseNo = carInfoVo.getLicenseNo();
		    	 String tempLinceseType = carInfoVo.getLicenseType();
		    	 String tempFrameNo =  carInfoVo.getFrameNo();
		    	 String drivingLicenseNo = carInfoVo.getDriver().getDrivingLicenseNo();
		    	 String tempVinNo = carInfoVo.getVin(); 
				 for(int j = i + 1;j<carList.size();j++){
					 if(carList.get(j) instanceof CTCarInfoVo){
						 CTCarInfoVo car = (CTCarInfoVo) carList.get(j);
						 if(StringUtils.strip(car.getLicenseNo()).equals(StringUtils.strip((tempLicenseNo))) 
								 && car.getLicenseType().equals(tempLinceseType)){
							return "该车辆车牌号码、号牌种类已存在，请修改！";
						 }
						 if(StringUtils.strip(tempVinNo).equals(StringUtils.strip(car.getVin()))){
							return "该车辆VIN码已存在，请修改！存在的VIN码【"+tempVinNo+"】";
						 }
						 if(StringUtils.strip(car.getFrameNo()).equals(StringUtils.strip(tempFrameNo))){
							return "该车辆车架号已存在，请修改！,存在的车架号【"+tempFrameNo+"】";
						 }
						 if(StringUtils.strip(car.getDriver().getDrivingLicenseNo()).equals(StringUtils.strip(drivingLicenseNo))){
							return "该车辆驾驶证件号码已存在，请修改！存在的驾驶证号码【"+drivingLicenseNo+"】";
						 }
					 }
				 }
			} else  if(tempCar instanceof MTACarInfoVo){
				MTACarInfoVo carInfoVo = (MTACarInfoVo) tempCar;
		    	 String tempLicenseNo = carInfoVo.getLicenseNo();
		    	 String tempLinceseType = carInfoVo.getLicenseType();
		    	 String tempFrameNo =  carInfoVo.getFrameNo();
		    	 String drivingLicenseNo = carInfoVo.getDriver().getDrivingLicenseNo();
		    	 String tempVinNo = carInfoVo.getVin(); 
				 for(int j = i + 1;j<carList.size();j++){
					 if(carList.get(j) instanceof MTACarInfoVo){
						 MTACarInfoVo car = (MTACarInfoVo) carList.get(j);	  
						 if(StringUtils.strip(car.getLicenseNo()).equals(StringUtils.strip((tempLicenseNo))) 
								 && car.getLicenseType().equals(tempLinceseType)){
							return "该车辆车牌号码、号牌种类已存在，请修改！";
						 }
						 if(StringUtils.strip(tempVinNo).equals(StringUtils.strip(car.getVin()))){
							return "该车辆VIN码已存在，请修改！存在的VIN码【"+tempVinNo+"】";
						 }
						 if(StringUtils.strip(car.getFrameNo()).equals(StringUtils.strip(tempFrameNo))){
							return "该车辆车架号已存在，请修改！,存在的车架号【"+tempFrameNo+"】";
						 }
						 if(StringUtils.strip(car.getDriver().getDrivingLicenseNo()).equals(StringUtils.strip(drivingLicenseNo))){
							return "该车辆驾驶证件号码已存在，请修改！存在的驾驶证号码【"+drivingLicenseNo+"】";
						 }
					 }
				 }
			} else if (tempCar instanceof CarInfoVo){
				 CarInfoVo carInfoVo = (CarInfoVo) tempCar;
		    	 String tempLicenseNo = carInfoVo.getLicenseNo();
		    	 String tempLinceseType = carInfoVo.getLicenseType();
		    	 String tempFrameNo =  carInfoVo.getFrameNo();
		    	 String drivingLicenseNo = carInfoVo.getDriver().getDrivingLicenseNo();
		    	 String tempVinNo = carInfoVo.getVin(); 
				 for(int j = i + 1;j<carList.size();j++){
					 if(carList.get(j) instanceof CarInfoVo){
						 CarInfoVo car = (CarInfoVo) carList.get(j);	  
						 if(StringUtils.strip(car.getLicenseNo()).equals(StringUtils.strip((tempLicenseNo))) 
								 && car.getLicenseType().equals(tempLinceseType)){
							return "该车辆车牌号码、号牌种类已存在，请修改！";
						 }
						 if(StringUtils.strip(tempVinNo).equals(StringUtils.strip(car.getVin()))){
							return "该车辆VIN码已存在，请修改！存在的VIN码【"+tempVinNo+"】";
						 }
						 if(StringUtils.strip(car.getFrameNo()).equals(StringUtils.strip(tempFrameNo))){
							return "该车辆车架号已存在，请修改！,存在的车架号【"+tempFrameNo+"】";
						 }
						 if(StringUtils.strip(car.getDriver().getDrivingLicenseNo()).equals(StringUtils.strip(drivingLicenseNo))){
							return "该车辆驾驶证件号码已存在，请修改！存在的驾驶证号码【"+drivingLicenseNo+"】";
						 }
					 }
				 }
			}
		}
	/*     Collections.sort(carList, new Comparator<E>(){ 
	    	 	@Override
	            public int compare(Object arg0, Object arg1) {   
	    	 		if(arg0 instanceof CTCarInfoVo && arg1 instanceof CTCarInfoVo){
	    	 			CTCarInfoVo a1 = (CTCarInfoVo) arg0;
	    	 			CTCarInfoVo a2 = (CTCarInfoVo) arg1;
	    	 			if(a1.getSerialNo().compareTo(a2.getSerialNo())>0){
	    	 				return 1;
	    	 			} else {
	    	 				return 0;
	    	 			}
	    	 		}
					return 0;
	    	 		
	            }
	        }); 
	     E tempCar = carList.get(0);
	     if(tempCar instanceof CTCarInfoVo){
	    	 CTCarInfoVo carInfoVo = (CTCarInfoVo) tempCar;
	    	 String tempLicenseNo = carInfoVo.getLicenseNo();
	    	 String tempLinceseType = carInfoVo.getLicenseType();
	    	 String tempFrameNo =  carInfoVo.getFrameNo();
	    	 String tempIdentifyNumber = carInfoVo.getDriver().getIdentifyNumber();
	    	 for(int i = 1;i<carList.size();i++){
	    		 E temp = carList.get(i);
				 if(temp instanceof CTCarInfoVo){
					 CTCarInfoVo car = (CTCarInfoVo) temp;	  
					 if(car.getLicenseNo().equals(tempLicenseNo) && car.getLicenseType().equals(tempLinceseType)){
						 return "";
					 }
					 if(car.getLicenseNo().equals(tempLicenseNo) && car.getLicenseType().equals(tempFrameNo)){
						 return "";
					 }
					 if(car.getDriver().getIdentifyNumber().equals(tempIdentifyNumber)){
						 return "";
					 }
				 }
	    	 }
	     } else  if(tempCar instanceof MTACarInfoVo){
	    	 MTACarInfoVo carInfoVo = (MTACarInfoVo) tempCar;
	    	 String tempLicenseNo = carInfoVo.getLicenseNo();
	    	 String tempLinceseType = carInfoVo.getLicenseType();
	    	 String tempFrameNo =  carInfoVo.getFrameNo();
	    	 String tempIdentifyNumber = carInfoVo.getDriver().getIdentifyNumber();
	    	 for(int i = 1;i<carList.size();i++){
	    		 E temp = carList.get(i);
				 if(temp instanceof CTCarInfoVo){
					 CTCarInfoVo car = (CTCarInfoVo) temp;	  
					 if(car.getLicenseNo().equals(tempLicenseNo) && car.getLicenseType().equals(tempLinceseType)){
						 return "";
					 }
					 if(car.getLicenseNo().equals(tempLicenseNo) && car.getLicenseType().equals(tempFrameNo)){
						 return "";
					 }
					 if(car.getDriver().getIdentifyNumber().equals(tempIdentifyNumber)){
						 return "";
					 }
				 }
	    	 }
	     }
	   */
		return null;
	}
	@Override
	public List<SysCodeDictVo> findByComCodeAndGradeid(String comCode,Long gradeid) {
		List<SysCodeDictVo> sysVos=new ArrayList<SysCodeDictVo>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" select distinct a.usercode, a.username, a.mobile from ywuser.prpduser a "
				+ "where a.usercode in(select b.usercode from saauser.saa_usergrade b "
				+ "where b.id in (select c.usergradeid from saauser.saa_permitcompany c "
				+ "where c.comcode like ?) and ? in (select d.gradeid from saauser.saa_usergrade d where "
				+ "d.usercode = b.usercode and d.validstatus= ?) and b.validstatus=?) and a.validstatus=? ");
		if(StringUtils.isNotBlank(comCode)){
			if("0002".equals(comCode.substring(0,4))){
				sqlUtil.addParamValue(comCode.substring(0,4)+"%");
			}else{
				sqlUtil.addParamValue(comCode.substring(0,2)+"%");
			}
		}
		sqlUtil.addParamValue(gradeid);
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("1");
		sqlUtil.addParamValue("1");
		String sql=sqlUtil.getSql();
		Object[] values=sqlUtil.getParamValues();
		// 执行查询
		List<Object[]> objects = baseDaoService.getAllBySql(sql,values);
		if(objects!=null && objects.size()>0){
			for(int i=0;i<objects.size();i++){
				SysCodeDictVo sysVo=new SysCodeDictVo();
				Object[] objs=objects.get(i);
				sysVo.setUserName(objs[1]!=null? objs[1].toString():"");
				sysVo.setLinkPhone(objs[2]!=null? objs[2].toString():"");
				sysVos.add(sysVo);
			}
		}
		return sysVos;
	}

	@Override
	public List<PrpLCheckCarInfoVo> findAllInfoByRegistNoAndFlag(String registNo, String flag) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("flag", flag);
		List<PrpLCheckCarInfo> prpLCheckCarInfoPos = databaseDao.findAll(PrpLCheckCarInfo.class,queryRule);
		List<PrpLCheckCarInfoVo> PrpLCheckCarInfoVos=null;
		if(prpLCheckCarInfoPos != null && prpLCheckCarInfoPos.size() > 0){
			PrpLCheckCarInfoVos= new ArrayList<PrpLCheckCarInfoVo>();
			PrpLCheckCarInfoVos = Beans.copyDepth().from(prpLCheckCarInfoPos).toList(PrpLCheckCarInfoVo.class);
		}
		return PrpLCheckCarInfoVos;
	}

	@Override
	public List<PrpLCheckPropVo> findAllPropByRegistNoAndFlag(String registNo,String flag) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("flag", flag);
		List<PrpLCheckProp> prpLCheckPropPos = databaseDao.findAll(PrpLCheckProp.class,queryRule);
		List<PrpLCheckPropVo> prpLCheckPropVos=null;
		if(prpLCheckPropPos != null && prpLCheckPropPos.size() > 0){
			prpLCheckPropVos= new ArrayList<PrpLCheckPropVo>();
			prpLCheckPropVos = Beans.copyDepth().from(prpLCheckPropPos).toList(PrpLCheckPropVo.class);
		}
		return prpLCheckPropVos;
	}

	@Override
	public List<PrpLCheckPersonVo> findAllPersonByRegistNoAndFlag(String registNo, String flag) {
		QueryRule queryRule=QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("flag", flag);
		List<PrpLCheckPerson> prpLCheckPersonPos = databaseDao.findAll(PrpLCheckPerson.class,queryRule);
		List<PrpLCheckPersonVo> prpLCheckPersonVos=null;
		if(prpLCheckPersonPos != null && prpLCheckPersonPos.size() > 0){
			prpLCheckPersonVos= new ArrayList<PrpLCheckPersonVo>();
			prpLCheckPersonVos = Beans.copyDepth().from(prpLCheckPersonPos).toList(PrpLCheckPersonVo.class);
		}
		return prpLCheckPersonVos;
	}

	@Override
	public void updatePrpLCheckCarInfo(Long id,String flag) {
		if(id!=null){
	        QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("carid",id);
	        PrpLCheckCarInfo prpLCheckCarInfo = databaseDao.findUnique(PrpLCheckCarInfo.class,queryRule);
	        if(prpLCheckCarInfo!=null){
	        	prpLCheckCarInfo.setFlag(flag);
	        }
		}
		
	}

	@Override
	public void updatePrpLCheckProp(Long id,String flag) {
		if(id!=null){
	        QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("id",id);
	        PrpLCheckProp prpLCheckProp = databaseDao.findUnique(PrpLCheckProp.class,queryRule);
	        if(prpLCheckProp!=null){
	        	prpLCheckProp.setFlag(flag);
	        }
		}
		
	}

	@Override
	public void updatePrpLCheckPerson(Long id,String flag) {
		if(id!=null){
	        QueryRule queryRule = QueryRule.getInstance();
	        queryRule.addEqual("id",id);
	        PrpLCheckPerson prpLCheckPerson = databaseDao.findUnique(PrpLCheckPerson.class,queryRule);
	        if(prpLCheckPerson!=null){
	        	prpLCheckPerson.setFlag(flag);
	        }
		}
		
	}

	/* 
	 * @see ins.sino.claimcar.check.service.CheckHandleService#saveAssessors(java.lang.Object, ins.platform.vo.SysUserVo)
	 * @param mainVo
	 * @param userVo
	 */
	@Override
	public void saveAssessors(String registNo,SysUserVo userVo) {
		PrpdIntermMainVo prpdIntermMainVo=managerService.findIntermByUserCode(userVo.getUserCode());
		PrpLCheckVo checkVo = checkService.findPrpLCheckByRegistNo(registNo);
		// 新增一条查勘费任务信息
		if(prpdIntermMainVo!=null){
			// 判断是否有数据
			PrpLAssessorVo prpLAssessorOld = assessorService.findAssessorByLossId(checkVo.getRegistNo(), CheckTaskType.CHK,null, prpdIntermMainVo.getIntermCode());
			if(prpLAssessorOld == null){
				PrpLAssessorVo prplAssessorVo = new PrpLAssessorVo();
				prplAssessorVo.setIntermId(prpdIntermMainVo.getId());
				prplAssessorVo.setIntermNameDetail(prpdIntermMainVo.getIntermName());
				prplAssessorVo.setRegistNo(checkVo.getRegistNo());
				prplAssessorVo.setLossId(checkVo.getId());
				prplAssessorVo.setTaskType(CheckTaskType.CHK);
				prplAssessorVo.setKindCode(this.getCarKindCode(checkVo.getRegistNo()));
				prplAssessorVo.setLossDate(new Date());
				prplAssessorVo.setIntermcode(prpdIntermMainVo.getIntermCode());
				//翻译问题，确定后需改动
				prplAssessorVo.setIntermname(codeTranService.transCode("GongGuPayCode",prpdIntermMainVo.getIntermCode()));
				prplAssessorVo.setLicenseNo("");
				prplAssessorVo.setUnderWriteFlag(AcheckUnderWriteFlag.Verify);
				prplAssessorVo.setCreateTime(new Date());
				prplAssessorVo.setCreateUser(userVo.getUserCode());
				prplAssessorVo.setComCode(userVo.getComCode());
				prplAssessorVo.setUnderWriteDate(new Date());
				assessorService.saveOrUpdatePrpLAssessor(prplAssessorVo, userVo);
				checkVo.setCheckClass( CheckClass.CHECKCLASS_Y);
				checkService.updateCheckMain(checkVo);
			}
		}
		
	}

	@Override
	public PrpLCheckDriverVo findPrplcheckcarinfoByRegistNoAndLicenseNo(String registNo, String licenseNo) {
		if(registNo!=null&&licenseNo!=null){
			QueryRule queryRule = QueryRule.getInstance();
			queryRule.addEqual("registNo",registNo);
			queryRule.addEqual("licenseNo",licenseNo);
			PrpLCheckCarInfo Prplcheckcarinfo = databaseDao.findUnique(PrpLCheckCarInfo.class,queryRule);
			if(Prplcheckcarinfo!=null){
				PrpLCheckDriverVo prpLCheckDriverVoById = checkTaskService.findPrpLCheckDriverVoById(Prplcheckcarinfo.getCarid());
				return prpLCheckDriverVoById;
			}
		}
		return null;
	}


}
