/******************************************************************************
 * CREATETIME : 2015年12月21日 上午10:21:07
 ******************************************************************************/
package ins.sino.claimcar.lossperson.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.PrpLLawSuitVo;
import ins.platform.vo.SysCodeDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.ClaimText;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.PersVeriFlag;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.service.LawSiutService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.service.LossChargeService;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceHandleService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceHisVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 人伤首次跟踪和后续跟踪
 * @author ★XMSH
 */
@Controller
@RequestMapping("/persTraceEdit")
public class PersTraceEditAction {

	private static Logger logger = LoggerFactory.getLogger(PersTraceEditAction.class);

	@Autowired
	private PersTraceService persTraceService;
	@Autowired
	private ClaimTextService claimTextSerVice;
	@Autowired
	private LossChargeService lossChargeService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	private CheckTaskService checkTaskService;
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private PersTraceHandleService persTraceHandleService;
	@Autowired
	private LossToPlatformService lossToPlatformService;
	@Autowired
	private ClaimTaskService claimTaskService;
	@Autowired
	private DeflossHandleService deflossHandleService;
	@Autowired
	private AssessorService assessorServices;
	@Autowired
	private InterfaceAsyncService interfaceAsyncService;
	@Autowired
	LawSiutService lawsiutService;
	@Autowired
    CertifyIlogService certifyIlogService;
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	private CertifyService certifyService;
	@Autowired
	PropTaskService propTaskService;
	@Autowired
	private LossCarService lossCarService;
	@Autowired
    CertifyToPaltformService certifyToPaltformService;
	@Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    VerifyClaimService verifyClaimService;
    @Autowired
    private PadPayService padPayService;
    @Autowired
	RegistService registService;
    
    @Autowired
    private IlogRuleService ilogRuleService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
	@Autowired
	PersTraceDubboService persTraceDubboService;
	@Autowired
	SendMsgToMobileService sendMsgToMobileService;
	/**
	 * 人伤跟踪初始化
	 * @param flowTaskId
	 * @return
	 * @modified: ☆XMSH(2016年2月29日 上午9:41:06): <br>
	 */
	@RequestMapping(value = "/persTraceEdit.do", method = RequestMethod.GET)
	public ModelAndView persTraceEdit(Double flowTaskId) {

		ModelAndView mav = new ModelAndView();
		
		SysUserVo userVo = WebUserUtils.getUser();

		PrpLDlossPersTraceMainVo persTraceMainVo = null;// 人伤主表
		List<PrpLDlossPersTraceVo> persTraceVos = null;// 人员跟踪表
		PrpLDlossPersTraceVo prpLDlossPersTraceVo = null;
		PrpLClaimTextVo prpLClaimTextVo = null;// 当前节点意见
		List<PrpLDlossChargeVo> prpLDlossChargeVos = null;// 费用赔款信息
		List<PrpLClaimTextVo> prpLClaimTextVos = null; // 意见列表
		PrpLCheckDutyVo prpLCheckDutyVo = null;// 车辆事故责任表
		String isChecked = "";// 查勘是否完成
		String isChkBigEnd = "";// 查勘大案审核是否通过
		String reconcileFlag = "";// 是否现场调解
		String majorCaseFlag = "";// 是否查勘大案审核
		Double sumCheckLossFee = 0d;// 查勘估损总金额
		String intermediaryFlag = "";
		String IntermediaryInfoId = "";

		PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(flowTaskId);// 人伤任务
		String handlerStatus = taskVo.getHandlerStatus();// 工作流状态
		String registNo = taskVo.getRegistNo();
		PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo); // 报案信息

		if(checkVo!=null){
			reconcileFlag = checkVo.getReconcileFlag();// 是否现场调解
			majorCaseFlag = checkVo.getMajorCaseFlag();// 是否查勘大案审核
			mav.addObject("checkDate",checkVo.getChkSubmitTime());
		}

		// 现场调解获取查勘估损金额
		if("1".equals(reconcileFlag)){
			List<PrpLCheckPersonVo> checkPersons = checkVo.getPrpLCheckTask().getPrpLCheckPersons();
			for(PrpLCheckPersonVo checkPerson:checkPersons){
				sumCheckLossFee += DataUtils.NullToZero(checkPerson.getLossFee()).doubleValue();
			}
		}

		// 查勘未结束，不能提交
		List<PrpLWfTaskVo> wfChkTaskVoList = wfTaskHandleService.findEndTask(registNo,null,FlowNode.Chk);
		if(wfChkTaskVoList==null||wfChkTaskVoList.size()==0){
			isChecked = "N";
		}else{
			isChecked = "Y";
		}

		// 查勘大案审核是否审核通过
		if("1".equals(majorCaseFlag)){
			List<PrpLWfTaskVo> wfChkBigTaskVoList = wfTaskHandleService.findEndTask(registNo,null,FlowNode.ChkBig);
			if(wfChkBigTaskVoList==null||wfChkBigTaskVoList.size()==0){
				isChkBigEnd = "N";
			}else{
				isChkBigEnd = "Y";
			}
		}

		if(FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())&&"0,1".contains(handlerStatus)){// 首次认识跟踪且未处理任务
			persTraceMainVo = new PrpLDlossPersTraceMainVo();
			persTraceMainVo.setRegistNo(registNo);
			persTraceMainVo.setRiskCode(registVo.getRiskCode());
			persTraceMainVo.setReportType(registVo.getReportType());// 报案类型 1-单商业，2-单交强，3-关联报案
			persTraceMainVo.setMercyFlag(registVo.getMercyFlag());// 案件紧急程度

			// 定损员是否是公估机构人员, prpduser的usercode 是prpdintermmain 公估表的INTERMCODE 公估公司代码
			// 1 通过prpduser.usercode 查找prpdintermmain 数据 ,不为空 则为公估定损
			PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
			boolean isIntermediary = intermMainVo==null?false:true;
			if(isIntermediary){
				intermediaryFlag = CheckClass.CHECKCLASS_Y;
				IntermediaryInfoId = intermMainVo.getIntermCode();
				persTraceMainVo.setIntermediaryFlag(CheckClass.CHECKCLASS_Y);// 公估定损
				persTraceMainVo.setIntermediaryInfoId(intermMainVo.getIntermCode());// 公估机构代码
			}else{
				intermediaryFlag = CheckClass.CHECKCLASS_N;
				persTraceMainVo.setIntermediaryFlag(CheckClass.CHECKCLASS_N);// 司内定损
				persTraceMainVo.setPlfName(userVo.getUserName());// 首次跟踪人姓名
				persTraceMainVo.setPlCode(userVo.getUserName());// 首次跟踪人代码
				persTraceMainVo.setPlfCertiCode(userVo.getIdentifyNumber());// 首次跟踪人身份证号
			}
		}else{
			persTraceMainVo = persTraceService.findPersTraceMainVoById(Long.decode(taskVo.getHandlerIdKey()));
			prpLClaimTextVo = claimTextSerVice.findClaimTextByNode(persTraceMainVo.getId(),taskVo.getSubNodeCode(),"0");// 暂存时flag=0，提交flag=1
			prpLClaimTextVos = claimTextSerVice.findClaimTextList(persTraceMainVo.getId(),
					persTraceMainVo.getRegistNo(),FlowNode.PLoss.name());// 意见列表
			prpLDlossChargeVos = lossChargeService.findLossChargeVos(persTraceMainVo.getId(),FlowNode.PLoss.name());// 费用赔款信息
			persTraceVos = persTraceService.findPersTraceVo(registNo,persTraceMainVo.getId());// 人伤跟踪人员信息
			// 定损员是否是公估机构人员, prpduser的usercode 是prpdintermmain 公估表的INTERMCODE 公估公司代码
			// 1 通过prpduser.usercode 查找prpdintermmain 数据 ,不为空 则为公估定损
			if(FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())&&"2".contains(handlerStatus)){//正在处理的任务
					PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
					boolean isIntermediary = intermMainVo==null?false:true;
					if(isIntermediary){
						intermediaryFlag = CheckClass.CHECKCLASS_Y;
						IntermediaryInfoId = intermMainVo.getIntermCode();
						persTraceMainVo.setIntermediaryFlag(CheckClass.CHECKCLASS_Y);// 公估定损
						persTraceMainVo.setIntermediaryInfoId(intermMainVo.getIntermCode());// 公估机构代码
						if(!userVo.getUserCode().equals(persTraceMainVo.getOperatorCode())){
							persTraceMainVo.setOperatorCode(userVo.getUserCode());
							persTraceMainVo.setPlCode(userVo.getUserCode());
			                persTraceMainVo.setPlfName("");
							persTraceMainVo.setPlfCertiCode("");		
						}
					}else{
						intermediaryFlag = CheckClass.CHECKCLASS_N;
						persTraceMainVo.setIntermediaryFlag(CheckClass.CHECKCLASS_N);// 司内定损
						persTraceMainVo.setPlfName(userVo.getUserName());// 首次跟踪人姓名
						persTraceMainVo.setPlfCertiCode(userVo.getIdentifyNumber());// 首次跟踪人身份证号
						persTraceMainVo.setPlCode(userVo.getUserCode());
						persTraceMainVo.setOperatorCertiCode(userVo.getIdentifyNumber());
						persTraceMainVo.setOperatorCode(userVo.getUserCode());
						persTraceMainVo.setOperatorName(userVo.getUserName());
					}
			}
			if(!FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())
					&& !"3".equals(taskVo.getHandlerStatus())){
				PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
				boolean isIntermediary = intermMainVo==null?false:true;
				if(isIntermediary){
					intermediaryFlag = CheckClass.CHECKCLASS_Y;
					IntermediaryInfoId = intermMainVo.getIntermCode();
					if(!userVo.getUserCode().equals(persTraceMainVo.getOperatorCode())){
						persTraceMainVo.setOperatorCode(userVo.getUserCode());
		                persTraceMainVo.setOperatorName("");
						persTraceMainVo.setOperatorCertiCode("");		
					}
				}else{
					intermediaryFlag = CheckClass.CHECKCLASS_N;
					persTraceMainVo.setOperatorCode(userVo.getUserCode());
	                persTraceMainVo.setOperatorName(userVo.getUserName());
					persTraceMainVo.setOperatorCertiCode(userVo.getIdentifyNumber());	
				}
			}
			if("3".equals(taskVo.getHandlerStatus())){
				if(!FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode())){
	                List<PrpLDlossPersTraceHisVo> loDlossPersTraceHisList = persTraceService.findPersTraceHisVo(registNo,persTraceMainVo.getId(),"");
	                if(loDlossPersTraceHisList!=null&&loDlossPersTraceHisList.size()>0){
	                    String userCode = loDlossPersTraceHisList.get(loDlossPersTraceHisList.size()-1).getOperatorCode();
	                	PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(userCode);
	    				boolean isIntermediary = intermMainVo==null?false:true;
	    				if(isIntermediary){
	    					intermediaryFlag = CheckClass.CHECKCLASS_Y;
	    					IntermediaryInfoId = intermMainVo.getIntermCode();
	    				}else{
	    					intermediaryFlag = CheckClass.CHECKCLASS_N;
	    				}
	                }
				} else{
					intermediaryFlag = persTraceMainVo.getIntermediaryFlag();
					IntermediaryInfoId = persTraceMainVo.getIntermediaryInfoId();
				}
            }	
			
			if(persTraceVos!=null&&persTraceVos.size()>0){
				prpLDlossPersTraceVo = persTraceVos.get(0);
			}
			if(HandlerStatus.INIT.equals(handlerStatus)){// 后续跟踪未处理，点击变成正在处理
				wfTaskHandleService.tempSaveTask(flowTaskId,persTraceMainVo.getId().toString(),
						WebUserUtils.getUserCode(),WebUserUtils.getComCode());
			}

			//费用险别
			List<SysCodeDictVo> dictVos = new ArrayList<SysCodeDictVo>();
			List<PrpLCItemKindVo> itemKinds = policyViewService.findItemKinds(registNo,"Y");
			for(PrpLCItemKindVo itemKind:itemKinds){
				SysCodeDictVo dictVo = new SysCodeDictVo();
				dictVo.setCodeCode(itemKind.getKindCode());
				dictVo.setCodeName(itemKind.getKindName());
				dictVos.add(dictVo);
			}
			mav.addObject("dictVos",dictVos);
		}

		// 查询其他节点的交强险责任类型和事故责任比例--人伤只有标的车
		prpLCheckDutyVo = checkTaskService.findCheckDuty(registNo,1);
		if(prpLCheckDutyVo==null||prpLCheckDutyVo.getId()==null){
			prpLCheckDutyVo = new PrpLCheckDutyVo();
			prpLCheckDutyVo.setRegistNo(registNo);
			prpLCheckDutyVo.setSerialNo(1);
		}

		if(prpLClaimTextVo==null||prpLClaimTextVo.getId()==null){
			prpLClaimTextVo = new PrpLClaimTextVo();
			prpLClaimTextVo.setRegistNo(registNo);
			prpLClaimTextVo.setTextType(ClaimText.OPINION);
			prpLClaimTextVo.setNodeCode(taskVo.getSubNodeCode());
		}

		Map<String,String> dataSourceMap = checkTaskService.getCarLossParty(registNo);
		//修改bug，增加单独调度出来的三者车辆
		List<PrpLDlossCarMainVo> carMainVoList = deflossHandleService.findLossCarMainByRegistNo(registNo);
		if (carMainVoList != null && !carMainVoList.isEmpty()) {
			for (PrpLDlossCarMainVo carMainVo : carMainVoList) {
				Integer serialNo = carMainVo.getSerialNo();
				if (!dataSourceMap.isEmpty() && !dataSourceMap.containsKey(serialNo.toString())) {
					String serialNoStr = serialNo == 1 ? "标的车" : "三者车";
					dataSourceMap.put(serialNo.toString(),serialNoStr+"(" + carMainVo.getLicenseNo() + ")");
				}
			}
		}
		
		//脱敏处理
		if(CodeConstants.WorkStatus.END.equals(taskVo.getWorkStatus())){
			//reportorPhone
			PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
			if(configValueVo!=null && "1".equals(configValueVo.getConfigValue()) && persTraceVos != null){//开关
				for(PrpLDlossPersTraceVo vo : persTraceVos){
					vo.getPrpLDlossPersInjured().setCertiCode(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getCertiCode()));
					vo.getPrpLDlossPersInjured().setPhoneNumber(DataUtils.replacePrivacy(vo.getPrpLDlossPersInjured().getPhoneNumber()));
				}
			}
		}
		
		//页面公估费查看按钮是否亮显
		String assessSign="0";
		List<PrpLAssessorFeeVo> listFeeVo=assessorServices.findPrpLAssessorFeeVoByRegistNo(registNo);
		if(listFeeVo!=null && listFeeVo.size()>0){
			for(PrpLAssessorFeeVo vo :listFeeVo){
				PrpLAssessorMainVo assessMainVo=assessorServices.findAssessorMainVoById(vo.getAssessMainId());
				if(assessMainVo!=null && "3".equals(assessMainVo.getUnderWriteFlag())){
					assessSign="1";
					break;
				}
			}
		}
		
		//重开赔案后的人伤不显示是否减损等选项
		String reOpenFlag="0";//0-重开前，1-重开后
		PrpLWfTaskVo taskVo2=wfTaskHandleService.findOutWfTaskVo(FlowNode.ReOpen.name(),registNo);
		if(taskVo2!=null){
			if(taskVo.getTaskInTime().getTime()>=taskVo2.getTaskOutTime().getTime()){
				reOpenFlag="1";
			}
		}
		mav.addObject("intermediaryFlag",intermediaryFlag);
		mav.addObject("IntermediaryInfoId",IntermediaryInfoId);
		mav.addObject("reOpenFlag",reOpenFlag);
		mav.addObject("assessSign",assessSign);
		mav.addObject("handlerStatus",handlerStatus);
		mav.addObject("flowTaskId",flowTaskId);
		mav.addObject("flowNodeCode",taskVo.getSubNodeCode());
		mav.addObject("flowNodeName",taskVo.getTaskName());
		mav.addObject("dataSourceMap",dataSourceMap);
		mav.addObject("prpLDlossPersTraceVo",prpLDlossPersTraceVo);
		mav.addObject("reconcileFlag",reconcileFlag);

		mav.addObject("sumCheckLossFee",sumCheckLossFee);// 查勘估损总金额
		mav.addObject("isChecked",isChecked);
		mav.addObject("majorCaseFlag",majorCaseFlag);
		mav.addObject("isChkBigEnd",isChkBigEnd);
		mav.addObject("registVo",registVo);
		mav.addObject("comCode",registVo.getComCode());

		persTraceMainVo = persTraceService.calculateSumAmt(persTraceMainVo);
		mav.addObject("prpLDlossPersTraceMainVo",persTraceMainVo);
		mav.addObject("prpLDlossPersTraces",persTraceVos);
		mav.addObject("prpLCheckDutyVo",prpLCheckDutyVo);
		mav.addObject("lossChargeVos",prpLDlossChargeVos);
		mav.addObject("prpLClaimTextVo",prpLClaimTextVo);
		mav.addObject("prpLClaimTextVos",prpLClaimTextVos);
		mav.addObject("taskVo",taskVo);
		Boolean type1 = wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.ClaimBI,registNo,"1");
		Boolean type2 = wfTaskHandleService.existTaskByNodeCode(registNo,FlowNode.ClaimCI,registNo,"1");
		Boolean types = false;
		if(type1||type2){
			types = true;
		}
		if(type1||type2){
			types = true;
		}
		mav.addObject("types",types);
		mav.setViewName("lossperson/persTraceEdit/PersTraceEdit");

		return mav;
	}

	/**
	 * 首次人伤跟踪接收任务
	 * @param flowTaskId
	 * @param registNo
	 * @return
	 * @modified: ☆XMSH(2016年2月29日 上午11:56:24): <br>
	 */
	@RequestMapping(value = "/acceptPersTraceTask.do")
	@ResponseBody
	public AjaxResult acceptPersTraceTask(String flowTaskId,String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			logger.debug("flowTaskId:"+flowTaskId);
			PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(flowTaskId));
			if(HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())){
				Long traceMainId = persTraceHandleService.acceptPersTraceTask(flowTaskId,registNo,userVo);
				Map<String,Object> datas = new HashMap<String,Object>();
				datas.put("traceMainId",traceMainId);
				ajaxResult.setDatas(datas);
				//移动端案件理赔处理，要通知移动端
				PrpLWfTaskVo prplWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(registNo, new BigDecimal(flowTaskId));
				if(prplWfTaskVo != null){
					prplWfTaskVo.setMobileNo("-1");
					prplWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PLOSSACCEPT);
					String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
					interfaceAsyncService.packMsg(prplWfTaskVo, url);
				}
			}else{
				throw new IllegalArgumentException("该任务已被接收，请刷新页面后再试！"); 
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData("accept");
		}
		catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}

	/**
	 * 暂存人伤信息
	 * @return
	 * @modified: ☆XMSH(2016年1月25日 上午9:38:07): <br>
	 */
	@RequestMapping(value = "/saveCasualtyInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveCasualtyInfo(@FormModel("prpLDlossPersTraceVo") PrpLDlossPersTraceVo persTraceVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			String saveType = "";
			if(persTraceVo.getId()==null){
				saveType = "add";
			}else{
				saveType = "update";
			}				
			SysUserVo userVo = WebUserUtils.getUser();
			Long bussTaskId = persTraceHandleService.saveCasualtyInfo(persTraceVo,userVo);
			
			ajaxResult.setData(bussTaskId);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("bussTaskId",bussTaskId);
			map.put("personName",persTraceVo.getPrpLDlossPersInjured().getPersonName());
			map.put("saveType",saveType);
			ajaxResult.setDatas(map);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		}catch(Exception e){
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
			
	}
	

	/**
	 * 人伤跟踪提交
	 * @param prpLDlossPersTraceMainVo
	 * @return
	 * @modified: ☆XMSH(2015年12月23日 下午3:42:51): <br>
	 */
	@RequestMapping(value = "/saveOrSubmit.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveOrSubmit(	@FormModel("prpLDlossPersTraceMainVo") PrpLDlossPersTraceMainVo prpLDlossPersTraceMainVo,
									@FormModel("prpLCheckDutyVo") PrpLCheckDutyVo prpLCheckDutyVo,
									@FormModel("lossChargeVos") List<PrpLDlossChargeVo> prpLDlossChargeVos,
									@FormModel("prpLClaimTextVo") PrpLClaimTextVo prpLClaimTextVo,
									@FormModel("submitNextVo") SubmitNextVo submitNextVo) {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SysUserVo userVo = WebUserUtils.getUser();
			prpLClaimTextVo = persTraceHandleService.saveOrSubmitPLEdit
					(prpLDlossPersTraceMainVo,prpLCheckDutyVo,prpLDlossChargeVos,prpLClaimTextVo,submitNextVo,userVo);
			
			String registNos = prpLDlossPersTraceMainVo.getRegistNo();
			String flowTaskId = submitNextVo.getFlowTaskId();
			
			//移动端案件理赔处理要通知
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
			PrpLWfTaskVo prplWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(registNos, new BigDecimal(flowTaskId));
			if(prplWfTaskVo != null){
				if(submitNextVo.getAuditStatus() != null && !"".equals(submitNextVo.getAuditStatus())){ //提交
					prplWfTaskVo.setMobileNo("-1");
					prplWfTaskVo.setHandlerStatus("3");
					prplWfTaskVo.setWorkStatus("2");
					prplWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PLOSSSUBMIT);
					interfaceAsyncService.packMsg(prplWfTaskVo, url);
					
				}else{ //暂存
					prplWfTaskVo.setHandlerStatus("2");
					prplWfTaskVo.setWorkStatus("2");
					prplWfTaskVo.setMobileNo("-1");
					prplWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.PLOSSSAVE);
					interfaceAsyncService.packMsg(prplWfTaskVo, url);
				}
			}
			
			ajaxResult.setData(prpLClaimTextVo);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			if(FlowNode.PLCharge_LV0.name().equals(submitNextVo.getNextNode())){// 自动审核
				//修改定损最后一个核损任务 发起理算
				if(PersVeriFlag.CHARGEADJUST.equals(prpLDlossPersTraceMainVo.getFlag())){
					lossCarService.modifyToSubMitComp(prpLDlossPersTraceMainVo.getRegistNo(),userVo);
				}
				try{
					//人伤节点完成送平台(注：需要根据保单的comCode来选择送不同的平台--需修改，)
					String registNo = prpLDlossPersTraceMainVo.getRegistNo();
					interfaceAsyncService.sendLossToPlatform(registNo,null);
					// 刷新立案
					claimTaskService.updateClaimFee(registNo,userVo.getUserCode(),FlowNode.valueOf(submitNextVo.getNextNode()));
				}catch(Exception e){
					logger.error("报案号" + (prpLDlossPersTraceMainVo == null ? null: prpLDlossPersTraceMainVo.getRegistNo())+ "人伤节点提交刷新立案 失败！",e);
				}
				try{
					//调用影像系统“影像资料统计接口”，查询该工号在该任务中上传的影像数量并保存（异步执行）
					String imageUrl = SpringProperties.getProperty("YX_QUrl")+"?";
					SysUserVo sysUserVo = new SysUserVo();
					PrpLDlossPersTraceMainVo  persTraceMainVo = persTraceService.findPersTraceMainVoById(prpLDlossPersTraceMainVo.getId());

					sysUserVo.setUserCode(persTraceMainVo.getCreateUser());
				    String userName = CodeTranUtil.transCode("UserCode", persTraceMainVo.getCreateUser());
				    if(userName != null && userName.equals(persTraceMainVo.getCreateUser())){
				    	userName = scheduleTaskService.findPrpduserByUserCode(persTraceMainVo.getCreateUser(),"").getUserName(); 
				    }
				    sysUserVo.setUserName(userName);
				    sysUserVo.setComCode(userVo.getComCode());
					interfaceAsyncService.getReqImageNum(sysUserVo, CodeConstants.APPROLE, prpLDlossPersTraceMainVo.getRegistNo(), "", imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
					interfaceAsyncService.getReqCheckUserImageNum(sysUserVo, CodeConstants.APPROLE, prpLDlossPersTraceMainVo.getRegistNo(),imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
				}catch(Exception e){
					logger.info("人伤调用影像系统影像资料统计接口报错=============", e);
				}
				if(prpLDlossPersTraceMainVo!=null){//自助理赔
					PrpLRegistVo prpLRegistVo=registService.findRegistByRegistNo(prpLDlossPersTraceMainVo.getRegistNo());
					if(prpLRegistVo!=null && "1".equals(prpLRegistVo.getSelfClaimFlag())){  
						Boolean flag=certifyIlogService.validAllVLossPass(prpLDlossPersTraceMainVo.getRegistNo());
						if(flag){
							interfaceAsyncService.sendClaimResultToSelfClaim(prpLDlossPersTraceMainVo.getRegistNo(), userVo,"5","2","");
						}
						
					}
				}
				
				//判断是否为最后一个核损(车财人)，请求Ilog
				PrpLConfigValueVo configValueIlogVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,WebUserUtils.getComCode());
				PrpLConfigValueVo configValueIRuleVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,WebUserUtils.getComCode());
				if("1".equals(configValueIlogVo.getConfigValue()) && "0".equals(configValueIRuleVo.getConfigValue())){
					Boolean flag=certifyIlogService.validAllVLossPass(prpLDlossPersTraceMainVo.getRegistNo());
				   if(flag){
					 LIlogRuleResVo resVo=certifyIlogService.sendAutoCertifyRule(prpLDlossPersTraceMainVo.getRegistNo(),userVo,new BigDecimal(submitNextVo.getFlowTaskId()),submitNextVo.getCurrentNode());
					 if(resVo!=null && "1".equals(resVo.getUnderwriterflag()) && certifyService.isPassPlatform(prpLDlossPersTraceMainVo.getRegistNo())){
						 WfTaskSubmitVo submitVo=certifyIlogService.autoCertify(prpLDlossPersTraceMainVo.getRegistNo(),userVo);//自动单证
							// 单证送平台
						 try{
							 certifyToPaltformService.certifyToPaltform(prpLDlossPersTraceMainVo.getRegistNo(),null);
						 }catch(Exception e){
							 logger.error("报案号" + (prpLDlossPersTraceMainVo == null ? null: prpLDlossPersTraceMainVo.getRegistNo()) + "人伤节点，进行自动单证送平台报错，异常信息-------------->",e);
						 }
							
							// 调用ilog查询是否可自动理算
						 	boolean NotExistObj = compensateTaskService.adjustNotExistObj(prpLDlossPersTraceMainVo.getRegistNo());
							if("1".equals(configValueIlogVo.getConfigValue()) && StringUtils.isNotBlank(submitVo.getFlowId())&& !NotExistObj){
								//==============事务问题开始
								String registNo = prpLDlossPersTraceMainVo.getRegistNo();
				                LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
				                /** 兜底人员权限判断 start **/
				                String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
					        	boolean finalAutoPass = true;
					        	if ("1".equals(finalPowerFlag)) {
					        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
					        		if (powerInfoVo != null) {
					        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
					        			if (gradePower != null) {
					        				// 总定损金额
					        				BigDecimal sumAmount = BigDecimal.ZERO;
					        				// 定损车辆信息
					        				List<PrpLDlossCarMainVo> losscarMainList=deflossHandleService.findLossCarMainByRegistNo(registNo);
					        				// 人伤定损信息
					        				List<PrpLDlossPersTraceMainVo> losspersTraceList=deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
					        				// 财产定损信息
					        				List<PrpLdlossPropMainVo> propmianList=propTaskService.findPropMainListByRegistNo(registNo);
					        				if (losscarMainList != null && losscarMainList.size() > 0) {
					        					for (PrpLDlossCarMainVo vo : losscarMainList) {
					        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLossFee() == null ? "0" : vo.getSumVeriLossFee().toString()));
					        					}
					        				}
					        				
					        				if (losspersTraceList != null && losspersTraceList.size() > 0) {
					        					for (PrpLDlossPersTraceMainVo vo : losspersTraceList) {
					        						if (vo.getPrpLDlossPersTraces() != null && vo.getPrpLDlossPersTraces().size() > 0) {
					        							for (PrpLDlossPersTraceVo traceVo : vo.getPrpLDlossPersTraces()) {
					        								sumAmount = sumAmount.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
					        							}
					        						}
					        					}
					        				}
					        				
					        				if (propmianList != null && propmianList.size() > 0) {
					        					for (PrpLdlossPropMainVo vo : propmianList) {
					        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLoss() == null ? "0" : vo.getSumVeriLoss().toString()));
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
								/** 兜底人员权限判断  end  **/
				                if("1".equals(ruleResVo.getUnderwriterflag()) && finalAutoPass){//自动理算通过
				                    List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
				                    if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
				                        for(PrpLWfTaskVo taskVo:prpLWfTaskVoList){
				                            PrpLCompensateVo compVo = compensateTaskService.autoCompTask(taskVo,userVo);
				                            Boolean autoVerifyFlag = false;
				                            Map<String,Object> params = new HashMap<String,Object>();
				                    		params.put("registNo",taskVo.getRegistNo());
				                    		if(taskVo.getTaskId() == null){
					                    		params.put("taskId",BigDecimal.ZERO.doubleValue());	
				                    		} else{
					                    		params.put("taskId",taskVo.getTaskId().doubleValue());
				                    			
				                    		}
				                    		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
				                            WfTaskSubmitVo nextVo = compensateTaskService.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo.getTaskId().doubleValue(),taskVo,userVo,autoVerifyFlag,isSubmitHeadOffice);
				                            if(nextVo.getSubmitLevel()==0){
				                                autoVerifyFlag = true;
				                            }
				                            if(autoVerifyFlag){
				                                // 自动核赔标识为true，理算提交后执行自动核赔
				                                Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,compVo);
				                                //核赔提交结案
				                                verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, compVo,uwNotionMainId);
				                                //核赔通过送收付、再保
				                                try{
				                                    verifyClaimService.sendCompensateToPayment(uwNotionMainId);
				                                }catch(Exception e){
				                                	 logger.error("报案号" + (prpLDlossPersTraceMainVo == null ? null: prpLDlossPersTraceMainVo.getRegistNo()) + "人伤节点进行自动理算，核赔通过送收付、再保报错，异常信息-------------->",e);
				                 					
				                                }
				                            }
				                            
				                        }
				                    }
				                }
				                //==============事务问题结束
							}
					 }
				   }
				}
			}
		} catch(Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			logger.error("报案号" + (prpLDlossPersTraceMainVo == null ? null: prpLDlossPersTraceMainVo.getRegistNo()) + "人伤节点提交报错",e);
		}
		return ajaxResult;
	}

	/**
	 * 激活注销跟踪人员
	 * @param id
	 * validFlag
	 * @return
	 * @modified: ☆XMSH(2016年1月16日 下午3:19:16): <br>
	 */
	@RequestMapping(value = "/ActiveOrCancel.do")
	@ResponseBody
	public AjaxResult ActiveOrCancel(String id,String validFlag) {
		String flag = "1";
		try{
			persTraceHandleService.ActiveOrCancelPersTrace(id,validFlag);
		}
		catch(Exception e){
			e.printStackTrace();
			flag = "0";
		}
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(flag);

		return ajaxResult;
	}
	
	/**
	 * <pre>异步查询所有人伤损失金额</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆Luwei(2017年1月3日 下午3:44:49): <br>
	 */
	@RequestMapping("/getAllSumFee.ajax")
	@ResponseBody
	public AjaxResult getAllSumFee(String registNo) {
		AjaxResult ajaxResult = new AjaxResult();
		double allSumReportFee = 0;// 所有人员合计总估损金额
		double allSumdefLoss = 0;// 所有人员合计总定损金额
		//直接通过报案号查询出所有的人伤信息
		List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(registNo);
		if(persTraceVoList != null && !persTraceVoList.isEmpty()){
			for(PrpLDlossPersTraceVo persTraceVo : persTraceVoList){
				allSumReportFee += DataUtils.NullToZero(persTraceVo.getSumReportFee()).doubleValue();
				allSumdefLoss += DataUtils.NullToZero(persTraceVo.getSumdefLoss()).doubleValue();
			}
		}
		ajaxResult.setData(allSumReportFee);
		ajaxResult.setStatusText(allSumdefLoss+"");
		return ajaxResult;
	}


	/**
	 * 该案件是否有诉讼信息
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/lawFlag.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult lawFlag(String registNo){
		AjaxResult ajax=new AjaxResult();
		String lawSign="0";
		List<PrpLLawSuitVo> suitVoList=lawsiutService.findByRegistNo(registNo);
		if(suitVoList!=null && suitVoList.size()>0){
			lawSign="1";
		}
		ajax.setData(lawSign);
		return ajax;
	}

	/**
	 * 自动单证
	 */
	private void autoCertify(String registNo){
		List<PrpLCMainVo> cmainVoList=policyViewService.findPrpLCMainVoListByRegistNo(registNo);//保单信息
		List<PrpLWfTaskVo> prpLWfTaskVos=wfFlowQueryService.findPrpWfTaskVo(registNo,FlowNode.Certi.name());
		List<PrpLClaimVo> claimVos=claimTaskService.findClaimListByRegistNo(registNo);
		PrpLWfTaskVo wfTaskVo=new PrpLWfTaskVo();
		if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
			 wfTaskVo=prpLWfTaskVos.get(0);
		}
		PrpLCertifyMainVo prpLCertifyMainVo = new PrpLCertifyMainVo();
			String userCode = WebUserUtils.getUserCode();
			Date nowDate = new Date();
			if(claimVos!=null && claimVos.size()==1){
				for(PrpLClaimVo vo:claimVos){
					prpLCertifyMainVo.setPolicyNo(vo.getPolicyNo());
				}
			}
			if(claimVos!=null && claimVos.size()==2){
				for(PrpLClaimVo vo:claimVos){
					if(!"1101".equals(vo.getRiskCode())){
						prpLCertifyMainVo.setPolicyNo(vo.getPolicyNo());
					}else{
						prpLCertifyMainVo.setPolicyNoLink(vo.getPolicyNo());;
					}
					
				}
			}
			prpLCertifyMainVo.setCollectFlag("1");//单证是收齐全
			prpLCertifyMainVo.setRegistNo(registNo);
			prpLCertifyMainVo.setStartTime(nowDate);
			prpLCertifyMainVo.setLawsuitFlag("0");//默认为否
			prpLCertifyMainVo.setValidFlag("1");//有效
			prpLCertifyMainVo.setCreateUser(userCode);
			prpLCertifyMainVo.setCreateTime(nowDate);
			prpLCertifyMainVo.setUpdateUser(userCode);
			prpLCertifyMainVo.setUpdateTime(nowDate);
			prpLCertifyMainVo.setClaimEndTime(nowDate);
			prpLCertifyMainVo.setCustomClaimTime(nowDate);
			prpLCertifyMainVo.setAddNotifyTime(nowDate);
			prpLCertifyMainVo.setAutoCertifyFlag("1");
			prpLCertifyMainVo = certifyService.submitCertify(prpLCertifyMainVo);
			WfTaskSubmitVo submitVo=new  WfTaskSubmitVo();
			if(cmainVoList!=null && cmainVoList.size()>0){
				for(PrpLCMainVo vo:cmainVoList){
					if(!"1101".equals(vo.getRiskCode())){
						submitVo.setComCode(vo.getComCode());
						submitVo.setAssignCom(WebUserUtils.getComCode());
					}
				}
			}
			
			submitVo.setCurrentNode(FlowNode.Certi);
			submitVo.setFlowId(wfTaskVo.getFlowId());
			submitVo.setFlowTaskId(wfTaskVo.getTaskId());
			submitVo.setSubmitType(SubmitType.N);
			submitVo.setTaskInKey(registNo);
			submitVo.setTaskInUser("AUTO");
			wfTaskHandleService.submitCertify(prpLCertifyMainVo,submitVo);
			checkTaskService.saveCheckDutyHis(registNo,"单证提交");
			// 单证送平台
			interfaceAsyncService.certifyToPaltform(registNo,null);
	}

	
	/**
	 * 人伤注销
	 * <pre></pre>
	 * @param registNo
	 * @param flowTaskId
	 * @return
	 * @modified:
	 * ☆zhujunde(2018年5月28日 上午11:40:17): <br>
	 */
    @RequestMapping("/compCancelPerson.do")
    @ResponseBody
    public AjaxResult compCancelPerson(String registNo, String flowTaskId,String persTraceMainId) {
        AjaxResult ajaxResult = new AjaxResult();
      
        SysUserVo userVo = WebUserUtils.getUser();
        String status = "1";
        
        //除当前任务之外，没有其他可操作任务，禁止注销！
    	List<PrpLWfTaskVo> otherPrpLWfTaskIns = wfFlowQueryService.findOtherPrpWfTaskVo(registNo,Long.valueOf(flowTaskId));
      	if(otherPrpLWfTaskIns == null || otherPrpLWfTaskIns.isEmpty()){
    		   ajaxResult.setStatusText("除当前任务之外，没有其他可操作任务，禁止注销！");
    	        status = "0";
    	}
        PrpLDlossPersTraceMainVo persTraceMainVo = new PrpLDlossPersTraceMainVo();
        if(StringUtils.isNotBlank(persTraceMainVo.getLossState()) && persTraceMainVo.getLossState().contains("1")){
          //已理算的人伤跟踪任务不可注销 lossstate
            ajaxResult.setStatusText("已理算的人伤跟踪任务不可注销");
            status = "0";
        }
        //已被垫付的人伤不能注销，如果垫付被注销，可以注销人伤任务
        List<PrpLPadPayMainVo> padPayMainVos = padPayService.findPadPayMainByRegistNo(registNo);
        if(!padPayMainVos.isEmpty()){
            for(PrpLPadPayMainVo prpLPadPayMainVo : padPayMainVos){
                if((!prpLPadPayMainVo.getPrpLPadPayPersons().isEmpty()) && (!"7".equals(prpLPadPayMainVo.getUnderwriteFlag()))){
                    ajaxResult.setStatusText("已被垫付的人伤不可注销");
                    status = "0";
                }
            }
        }
        //已发起预付的人伤不能注销，如果预付被注销或冲销，可以注销人伤任务
        List<PrpLCompensateVo> compList = new ArrayList<PrpLCompensateVo>();
        compList = compensateTaskService.queryCompensate(registNo,"Y");
        if(compList !=null && compList.size() > 0){
            for(PrpLCompensateVo prpLCompensateVo : compList){
                if(("Y".equals(prpLCompensateVo.getCompensateType())) && (!"7".equals(prpLCompensateVo.getUnderwriteFlag()))
                        && (StringUtils.isEmpty(prpLCompensateVo.getPrpLCompensateExt().getOppoCompensateNo()))){
                    List<PrpLPrePayVo> prePayPVos = new ArrayList<PrpLPrePayVo>();
                    prePayPVos = compensateTaskService.getPrePayVo(prpLCompensateVo.getCompensateNo(),"P");
                    if(prePayPVos != null && prePayPVos.size() > 0){
                        for(PrpLPrePayVo prpLPrePayVo : prePayPVos){
                            if(prpLPrePayVo.getLossType().contains("pers_") && prpLPrePayVo.getPayAmt().compareTo(BigDecimal.ZERO) >= 0){//不是冲销
                                ajaxResult.setStatusText("已发起预付的人伤不能注销");
                                status = "0";
                            }
                        }
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(persTraceMainId)){
            persTraceMainVo = persTraceService.findPersTraceMainVoById(Long.valueOf(persTraceMainId));
        }else{
            List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
            if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size() > 0){
                persTraceMainVo = prpLDlossPersTraceMainVoList.get(0);
            }
        }
        try{
            if("1".equals(status)){
                persTraceHandleService.cancelPerson(persTraceMainVo,new BigDecimal(flowTaskId),userVo);
                ajaxResult.setStatus(HttpStatus.SC_OK);
                logger.debug("人伤注销送平台registNo:"+persTraceMainVo.getRegistNo());
                lossToPlatformService.sendLossToPlatform(persTraceMainVo.getRegistNo(),null);  
            }else{
                ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            
        }
        catch(Exception e){
            ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            ajaxResult.setStatusText("人伤跟踪任务注销失败");
        }
        return ajaxResult;
    }
}
