/******************************************************************************
 * CREATETIME : 2015年11月26日 上午9:39:47
 ******************************************************************************/
package ins.sino.claimcar.check.web.action;

import freemarker.core.ParseException;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.HandlerStatus;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.*;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.common.claimtext.service.ClaimTextService;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.mail.service.MailModelService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdEmailVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLAssessorMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationCarVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationPersonVo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ins.sino.claimcar.other.service.AcheckTaskService;

import com.alibaba.fastjson.JSONObject;

/**
 * checkAction
 *
 * @author ★Luwei
 */
@Controller
@RequestMapping(value = "/check")
public class CheckAction {

    private Logger logger = LoggerFactory.getLogger(CheckAction.class);
    // 服务装载
    @Autowired
    CheckHandleService checkHandleService;
	@Autowired
	SysUserService sysUserService;
    @Autowired
    RegistQueryService registQueryService;

    @Autowired
    WfTaskHandleService wfTaskHandleService;

    @Autowired
    ClaimTextService claimTextService;
    @Autowired
    CheckTaskService checkTaskService;

    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    LossCarService lossCarService;
    @Autowired
    PersTraceDubboService persTraceDubboService;

    @Autowired
    private AssessorService assessorService;

    @Autowired
    private WfMainService wfMainService;
    @Autowired
    private SendMsgToMobileService sendMsgToMobileService;

    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    ManagerService managerService;
    @Autowired
    MailModelService mailModelService;
    

	@Autowired
    private AcheckTaskService acheckTaskService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
	
	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
	
	/**
	 * 查勘处理
	 * @param flowTaskId
	 * @throws Exception
	 */
	@RequestMapping(value = "/initCheck.do")
	public ModelAndView init(Double flowTaskId) throws Exception { 
		String userCode = WebUserUtils.getUserCode();
		String comCode = WebUserUtils.getComCode();
		// 根据工作流的flowTaskId，查询一下这个任务的状态
		PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
        PrpLWfMainVo mainVo = wfMainService.findPrpLWfMainVoByFlowId(wfTaskVo.getFlowId());
        PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(wfTaskVo.getRegistNo());
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(wfTaskVo.getRegistNo());
        // 移动查勘案件查勘未处理 查勘未暂存 理赔不能处理
        if ((mainVo != null && StringUtils.isNotBlank(mainVo.getIsMobileCase()) && !"0".equals(mainVo.getIsMobileCase()) &&
                ((HandlerStatus.INIT
                        .equals(wfTaskVo.getHandlerStatus()) && "1".equals(wfTaskVo.getIsMobileAccept())) || // 移动查勘
                        ((HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus()) || HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())) &&
                                ("2".equals(wfTaskVo
                                        .getIsMobileAccept()) || "3".equals(wfTaskVo.getIsMobileAccept()))))) || (checkVo != null && // 民太安和车童
                checkVo.getPrpLCheckTask() == null && null == checkVo
                .getPrpLCheckTask().getValidFlag())) { // validFlag 在查勘提交时更新为1
            ModelAndView mav = new ModelAndView();
            mav.addObject("isMobileCase", wfTaskVo.getIsMobileAccept());
            mav.addObject("registVo", prpLRegistVo);
            mav.setViewName("check/checkEdit/CheckMainEdit");
            return mav;
        }
        if (FlowNode.Check.equals(wfTaskVo.getNodeCode())
                && FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {// 查勘
            if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// 未接受任务
                return view(wfTaskVo);
            } else {// 正在处理
                return edit(wfTaskVo);
            }
        } else if (FlowNode.ChkRe.equals(wfTaskVo.getSubNodeCode())) {// 复勘--
            if (CodeConstants.WorkStatus.INIT.equals(wfTaskVo.getHandlerStatus()))
                wfTaskVo = wfTaskHandleService.tempSaveTask(flowTaskId, wfTaskVo.getHandlerIdKey(), userCode, comCode);
            Long checkId = checkHandleService.getCheckId(wfTaskVo.getRegistNo());
            if (checkId == null) {
                throw new IllegalArgumentException("该任务不存在！");
            }
            wfTaskVo.setHandlerIdKey(checkId.toString());
            return edit(wfTaskVo);
        } else if (FlowNode.ChkBig.equals(wfTaskVo.getNodeCode())) {// --大案审核
            if (CodeConstants.WorkStatus.INIT.equals(wfTaskVo.getHandlerStatus()))
                wfTaskVo = wfTaskHandleService.tempSaveTask(flowTaskId, wfTaskVo.getHandlerIdKey(), userCode, comCode);
            Long checkId = checkHandleService.getCheckId(wfTaskVo.getRegistNo());
            wfTaskVo.setHandlerIdKey(checkId.toString());
            return edit(wfTaskVo);
        } else {
            throw new IllegalArgumentException("非查勘任务！");
        }
    }

    /**
     * 查勘未接受任务进入查勘显示页面
     *
     * @param checkActionVo
     * @return
     * @throws Exception
     */
    private ModelAndView view(PrpLWfTaskVo wfTaskVo) throws Exception {
        SysUserVo userVo = WebUserUtils.getUser();
        Long scheduleTaskId = Long.parseLong(wfTaskVo.getHandlerIdKey());
        // 查勘初始化
        CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule
                (scheduleTaskId, wfTaskVo.getRegistNo(), userVo);

        // 处理结果集
        ModelAndView mav = new ModelAndView();
        // if(wfFlowQueryService.isCheckNodeEnd(wfTaskVo.getRegistNo())){//查勘是否提交
//			mav.addObject("isCheckNodeEnd","1");
//		}
        mav.addObject("registVo", checkActionVo.getPrpLregistVo());
        mav.addObject("checkMainCarVo", checkActionVo.getCheckMainCarVo());
        mav.addObject("checkThirdCarVos", checkActionVo.getCheckThirdCarList());
        mav.addObject("checkPropVos", checkActionVo.getCheckPropList());
        mav.addObject("checkPersonVos", checkActionVo.getCheckPersonList());
        mav.addObject("checkVo", checkActionVo.getPrpLcheckVo());
        mav.addObject("checkTaskVo", checkActionVo.getPrpLcheckTaskVo());
        mav.addObject("claimTextVos", checkActionVo.getClaimTextVoList());
        mav.addObject("checkExtVos", checkActionVo.getCheckExtList());
        mav.addObject("loss", "");
        mav.addObject("taskParamVo", wfTaskVo);
        mav.addObject("oldClaim", checkActionVo.getPrpLregistVo().getFlag());
        mav.addObject("coinsFlag", checkActionVo.getCoinsFlag());
        if (StringUtils.isNotBlank(checkActionVo.getCoinsFlag()) &&
                "1234".contains(checkActionVo.getCoinsFlag())) {
            mav.addObject("isCoinsFlag", "1");
        } else {
            mav.addObject("isCoinsFlag", "0");
        }
        mav.addObject("payrefFlag", checkActionVo.getPayrefFlag());
        // 山东影像对比
        String claimSequenceNo = "";
        if (checkActionVo.getPolicyInfo() != null && checkActionVo.getPolicyInfo().size() > 1) {// 有商业取商业
            for (PrpLCMainVo vo : checkActionVo.getPolicyInfo()) {
                if ("12".equals(vo.getRiskCode().substring(0, 2))) {
                    if (StringUtils.isNotBlank(vo.getClaimSequenceNo())) {
                        claimSequenceNo = vo.getClaimSequenceNo();
                        mav.addObject("claimSequenceNo", claimSequenceNo);
                    }
                }
            }
        } else if (checkActionVo.getPolicyInfo() != null && checkActionVo.getPolicyInfo().size() == 1) {
            PrpLCMainVo vo = checkActionVo.getPolicyInfo().get(0);
            if (StringUtils.isNotBlank(vo.getClaimSequenceNo())) {
                claimSequenceNo = vo.getClaimSequenceNo();
                mav.addObject("claimSequenceNo", claimSequenceNo);
            }
        }
        String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
        String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
        String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
        mav.addObject("comparePicURL", carRiskUrl);
        String claimPeriod = "03";
        mav.addObject("claimPeriod", claimPeriod);
        mav.addObject("carRiskUserName", carRiskUserName);
        mav.addObject("carRiskPassWord", carRiskPassWord);
        mav.setViewName("check/checkEdit/CheckMainEdit");
        return mav;

    }

    /**
     * 查勘初始化
     *
     * @param checkActionVo
     * @return
     * @throws Exception
     */
    private ModelAndView edit(PrpLWfTaskVo wfTaskVo) throws Exception {
        Long checkTaskId = Long.parseLong(wfTaskVo.getHandlerIdKey());
        String registNo = wfTaskVo.getRegistNo();
        // 查勘初始化
        CheckActionVo checkActionVo = checkHandleService.initCheckByCheck(checkTaskId);
        List<PrpLClaimTextVo> claimTextVoList = claimTextService.findClaimTextList
                (checkTaskId, registNo, wfTaskVo.getNodeCode());
        checkActionVo.setClaimTextVoList(claimTextVoList);
        // 处理结果集
        ModelAndView mav = new ModelAndView();

        String policyType = "";// 保单类型，1-单交强，2-单商业，3-

        for (PrpLCMainVo cMainVo : checkActionVo.getPolicyInfo()) {
            if (checkActionVo.getPolicyInfo().size() == 1) {
                policyType = Risk.DQZ.equals(cMainVo.getRiskCode()) ? "1" : "2";
            } else {
                policyType = "3";
            }

        }


        // 脱敏处理
        if (CodeConstants.WorkStatus.END.equals(wfTaskVo.getWorkStatus())) {
            replacePrivacy(checkActionVo, mav);
        } else {
            mav.addObject("payCustomVo", checkHandleService.findPayCustomVoByRegistNo(registNo));// 收款人信息
        }
        String comCode = "";
        for (PrpLCMainVo cmain : checkActionVo.getPolicyInfo()) {
            if (!"1101".equals(cmain.getRiskCode())) {
                comCode = cmain.getComCode().substring(0, 2);
            }
            if ("".equals(comCode)) {
                comCode = cmain.getComCode().substring(0, 2);

            }
        }
        // 页面公估费查看按钮是否亮显
        String assessSign = "0";
        List<PrpLAssessorFeeVo> listFeeVo = assessorService.findPrpLAssessorFeeVoByRegistNo(registNo);
        if (listFeeVo != null && listFeeVo.size() > 0) {
            for (PrpLAssessorFeeVo vo : listFeeVo) {
                PrpLAssessorMainVo assessMainVo = assessorService.findAssessorMainVoById(vo.getAssessMainId());
                if (assessMainVo != null && "3".equals(assessMainVo.getUnderWriteFlag())) {
                    assessSign = "1";
                    break;
                }
            }

        }
        if (checkHandleService.isClaimForce(registNo)) { // 强制立案 置灰 多保单关联与取消 按钮
            checkActionVo.getPrpLregistVo().setTempRegistFlag("1");
        }
        // 山东预警信信息按钮是否显示，山东保单显示，其它不显示
        String policeInfoFlag = "0";// 显示标志
        String policyComCode = policyViewService.getPolicyComCode(registNo);
        if (policyComCode.startsWith("62")) {
            policeInfoFlag = "1";
            String carRiskUrl = SpringProperties.getProperty("CARRISK_URL");
            String carRiskUserName = SpringProperties.getProperty("SDWARN_YMUSER");
            String carRiskPassWord = SpringProperties.getProperty("SDWARN_YMPW");
            mav.addObject("comparePicURL", carRiskUrl);
            String claimPeriod = "03";
            mav.addObject("claimPeriod", claimPeriod);
            mav.addObject("carRiskUserName", carRiskUserName);
            mav.addObject("carRiskPassWord", carRiskPassWord);
            // 山东影像对比
            String claimSequenceNo = "";
            if (checkActionVo.getPolicyInfo() != null && checkActionVo.getPolicyInfo().size() > 1) {// 有商业取商业
                for (PrpLCMainVo vo : checkActionVo.getPolicyInfo()) {
                    if ("12".equals(vo.getRiskCode().substring(0, 2))) {
                        if (StringUtils.isNotBlank(vo.getClaimSequenceNo())) {
                            claimSequenceNo = vo.getClaimSequenceNo();
                            mav.addObject("claimSequenceNo", claimSequenceNo);
                        }
                    }
                }
            } else if (checkActionVo.getPolicyInfo() != null && checkActionVo.getPolicyInfo().size() == 1) {
                PrpLCMainVo vo = checkActionVo.getPolicyInfo().get(0);
                if (StringUtils.isNotBlank(vo.getClaimSequenceNo())) {
                    claimSequenceNo = vo.getClaimSequenceNo();
                    mav.addObject("claimSequenceNo", claimSequenceNo);
                }
            }
        }
        mav.addObject("policeInfoFlag", policeInfoFlag);
        mav.addObject("assessSign", assessSign);
        mav.addObject("comCode", comCode);
        mav.addObject("policyType", policyType);
        mav.addObject("registVo", checkActionVo.getPrpLregistVo());
        mav.addObject("policyInfo", checkActionVo.getPolicyInfo());// 保单信息
        mav.addObject("cItemCarVo", registQueryService.findCItemCarByRegistNo(registNo));
        mav.addObject("checkVo", checkActionVo.getPrpLcheckVo());
        mav.addObject("checkTaskVo", checkActionVo.getPrpLcheckTaskVo());
        mav.addObject("checkMainCarVo", checkActionVo.getCheckMainCarVo());
        mav.addObject("checkThirdCarVos", checkActionVo.getCheckThirdCarList());
        mav.addObject("checkPropVos", checkActionVo.getCheckPropList());
        mav.addObject("checkPersonVos", checkActionVo.getCheckPersonList());
        mav.addObject("disasterVo", checkActionVo.getDisasterVo());
        mav.addObject("claimTextVos", checkActionVo.getClaimTextVoList());
        mav.addObject("checkExtVos", checkActionVo.getCheckExtList());
        mav.addObject("taskParamVo", wfTaskVo);
        mav.addObject("checkDutyVo", checkActionVo.getCheckDutyVo());
        mav.addObject("loss", checkHandleService.getCarLossParty(registNo));// 损失方select
        mav.addObject("subLiNoMap", checkHandleService.getSubLicenseNo(registNo));// 代位
        mav.addObject("kindMap", checkActionVo.getKindMap());
        mav.addObject("GServiceType", checkActionVo.getGServiceType());// 公估服务类型
        Map<String, String> charMap = checkHandleService.getIntermStanders(WebUserUtils.getUserCode());
        mav.addObject("chargeStandard", charMap);// 获取公估机构资费标准(需要get公估机构代码)

        mav.addObject("customMap", checkHandleService.getCustom(registNo));// 获取公估费的收款人下拉框
        mav.addObject("lossChargeVos", checkActionVo.getLossChargeVo());
        mav.addObject("claimDeductVos", checkActionVo.getClaimDeductVoList());
        mav.addObject("subrogationMain", checkActionVo.getSubrogationMainVo());// 代位
        List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList
                (checkTaskId, registNo, FlowNode.ChkBig.name());
        mav.addObject("textSize", claimTextVos.size());//
        mav.addObject("coinsFlag", checkActionVo.getCoinsFlag());
        if (StringUtils.isNotBlank(checkActionVo.getCoinsFlag()) &&
                "1234".contains(checkActionVo.getCoinsFlag())) {
            mav.addObject("isCoinsFlag", "1");
        } else {
            mav.addObject("isCoinsFlag", "0");
        }
        mav.addObject("payrefFlag", checkActionVo.getPayrefFlag());

        mav.setViewName("check/checkEdit/CheckMainEdit");
        return mav;
    }

    // 设置脱敏
    private void replacePrivacy(CheckActionVo checkActionVo, ModelAndView mav) {
        //reportorPhone
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.STARFLAG);
        if (configValueVo != null && RadioValue.RADIO_YES.equals(configValueVo.getConfigValue())) {// 开关
            PrpLRegistVo registVo = checkActionVo.getPrpLregistVo();
            if (registVo != null) {
                registVo.setReportorPhone(DataUtils.replacePrivacy(registVo.getReportorPhone()));
                registVo.setLinkerPhone(DataUtils.replacePrivacy(registVo.getLinkerPhone()));
                registVo.setLinkerMobile(DataUtils.replacePrivacy(registVo.getLinkerMobile()));
            }
            PrpLCheckDriverVo driverVo = checkActionVo.getCheckMainCarVo().getPrpLCheckDriver();
            if (driverVo != null) {
                driverVo.setLinkPhoneNumber(DataUtils.replacePrivacy(driverVo.getLinkPhoneNumber()));
                driverVo.setIdentifyNumber(DataUtils.replacePrivacy(driverVo.getIdentifyNumber()));
            }
//			checkActionVo.getCheckMainCarVo().getPrpLCheckDriver().setLinkPhoneNumber
//			(DataUtils .replacePrivacy(checkActionVo.getCheckMainCarVo().getPrpLCheckDriver().getLinkPhoneNumber()));
//			checkActionVo.getCheckMainCarVo().getPrpLCheckDriver().setLinkPhoneNumber
//			(DataUtils .replacePrivacy(checkActionVo.getCheckMainCarVo().getPrpLCheckDriver().getLinkPhoneNumber()));

            for (PrpLCheckCarVo prpLCheckCarVo : checkActionVo.getCheckThirdCarList()) {
                prpLCheckCarVo.getPrpLCheckDriver().setLinkPhoneNumber
                        (DataUtils.replacePrivacy(prpLCheckCarVo.getPrpLCheckDriver().getLinkPhoneNumber()));
            }

            checkActionVo.getPrpLcheckTaskVo().setCheckerIdfNo
                    (DataUtils.replacePrivacy(checkActionVo.getPrpLcheckTaskVo().getCheckerIdfNo()));
            // 人伤
            for (PrpLCheckPersonVo vo : checkActionVo.getCheckPersonList()) {
                vo.setIdNo(DataUtils.replacePrivacy(vo.getIdNo()));
            }
            PrpLPayCustomVo payCustomVo = checkHandleService.findPayCustomVoByRegistNo(checkActionVo.getRegistNo());
            payCustomVo.setCertifyNo(DataUtils.replacePrivacy(payCustomVo.getCertifyNo()));
            payCustomVo.setPayeeMobile(DataUtils.replacePrivacy(payCustomVo.getPayeeMobile()));
            payCustomVo.setPayeeName(DataUtils.replacePrivacy(payCustomVo.getPayeeName()));
            payCustomVo.setAccountNo(DataUtils.replacePrivacy(payCustomVo.getAccountNo()));
            mav.addObject("payCustomVo", payCustomVo);// 收款人信息
        }
    }

    /**
     * 显示查勘详细信息
     *
     * @throws Exception
     */
    @RequestMapping(value = "/viewCheckInfo.do")
    public ModelAndView viewCheckInfo(HttpServletRequest request) throws Exception {
        PrpLWfTaskVo wfTaskVo = new PrpLWfTaskVo();
        String registNo = request.getParameter("registNo");
        wfTaskVo.setRegistNo(registNo);
        String checkTaskId = checkHandleService.getCheckId(registNo).toString();
        wfTaskVo.setHandlerIdKey(checkTaskId);
        wfTaskVo.setNodeCode(FlowNode.Check.name());
        return edit(wfTaskVo);
    }

    /**
     * 接收查勘任务
     *
     * @return
     */
    @RequestMapping(value = "/acceptCheckTask.do")
    @ResponseBody
    public AjaxResult acceptCheckTask(Double flowTaskId) throws ParseException {
        SysUserVo userVo = WebUserUtils.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        Long checkId = null;
        try {
            PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
            if (FlowNode.Check.equals(wfTaskVo.getNodeCode()) && FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {
                if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// 未接受任务

                    Long scheduleTaskId = Long.parseLong(wfTaskVo.getHandlerIdKey());
                    // 查勘初始化
                    CheckActionVo checkActionVo = checkHandleService.initCheckBySchedule
                            (scheduleTaskId, wfTaskVo.getRegistNo(), userVo);
                    // 保存
                    checkId = checkHandleService.saveCheckOnAccept(checkActionVo, userVo);

                } else {// 正在处理
                    throw new IllegalArgumentException("任务已被接收！接收人：" + wfTaskVo.getHandlerUser());
                }
            } else if (!FlowNode.Chk.equals(wfTaskVo.getSubNodeCode())) {
                if (HandlerStatus.INIT.equals(wfTaskVo.getHandlerStatus())) {// 未接受任务
                    //checkHandleService.initCheckByCheck(checkId,wfTaskVo.getRegistNo());
                    checkId = Long.parseLong(wfTaskVo.getHandlerIdKey());
                } else {// 正在处理
                    throw new IllegalArgumentException("任务已被接收！接收人：" + wfTaskVo.getHandlerUser());
                }
            } else {
                throw new IllegalArgumentException("非查勘任务！");
            }
            // 接收任务
            //wfTaskHandleService.acceptTask(flowTaskId,SecurityUtils.getUserCode(),SecurityUtils.getComCode());
            // 暂存（正在处理）
            wfTaskHandleService.tempSaveTask(flowTaskId, checkId.toString(),
                    userVo.getUserCode(), userVo.getComCode());
            //String urlParam = "?flowTaskId="+flowTaskId;
            // 移动端案件理赔处理要通知快赔 并写会理赔处理标识
            // 写会标志
            PrpLWfTaskVo taskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(wfTaskVo.getRegistNo(), new BigDecimal(flowTaskId));
            if (taskVo != null) { // 发送通知
                taskVo.setHandlerStatus("2");
                taskVo.setWorkStatus("2");
                taskVo.setMobileOperateType(CodeConstants.MobileOperationType.CHECKACCEPT);
                String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
                interfaceAsyncService.packMsg(taskVo, url);
            }
            ajaxResult.setStatus(HttpStatus.SC_OK);
            ajaxResult.setData(flowTaskId);
        } catch (Exception e) {
            logger.error("查勘接收任务失败！", e);
            ajaxResult.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
        }
        return ajaxResult;
    }

    /**
     * 增加财产损失项
     *
     * @return
     */
    @RequestMapping(value = "/addPropItem.ajax")
    @ResponseBody
    public ModelAndView addPropItem(int propSize, String registNo, String nodeCode) throws ParseException {
        ModelAndView mv = new ModelAndView();
        List<PrpLCheckPropVo> prpLcheckPropVo = new ArrayList<PrpLCheckPropVo>();
        PrpLCheckPropVo checkPropVo = new PrpLCheckPropVo();
        checkPropVo.setRegistNo(registNo);
        checkPropVo.setLossPartyId(0L);
        checkPropVo.setLossPartyName("地面损失");
        Map<String, String> loss = checkHandleService.getCarLossParty(registNo);
        prpLcheckPropVo.add(checkPropVo);
        mv.addObject("checkPropVos", prpLcheckPropVo);
        mv.addObject("propSize", propSize);
        mv.addObject("loss", loss);
        mv.addObject("nodeCode", nodeCode);
        //mv.addObject("checkPropVo.lossPartyName",loss.get("-1"));
        mv.setViewName("check/checkEdit/PropLossItem");
        return mv;
    }

    /**
     * 增加人伤损失项
     *
     * @return
     */
    @RequestMapping(value = "/addPersonItem.ajax")
    @ResponseBody
    public ModelAndView addPersonItem(int personSize, String registNo, String nodeCode) throws ParseException {
        ModelAndView mv = new ModelAndView();
        List<PrpLCheckPersonVo> prpLcheckPersonVoList = new ArrayList<PrpLCheckPersonVo>();
        PrpLCheckPersonVo checkPersonVo = new PrpLCheckPersonVo();
        Map<String, String> loss = checkHandleService.getCarLossParty(registNo);
        checkPersonVo.setRegistNo(registNo);
        checkPersonVo.setLossPartyId(0L);
        checkPersonVo.setLossPartyName("地面损失");
        prpLcheckPersonVoList.add(checkPersonVo);
        mv.addObject("checkPersonVos", prpLcheckPersonVoList);
        mv.addObject("personSize", personSize);
        mv.addObject("loss", loss);
        mv.addObject("nodeCode", nodeCode);
        //mv.addObject("checkPersonVo.lossPartyName",loss.get("-1"));
        mv.setViewName("check/checkEdit/PersonLossItem");
        return mv;
    }

    /**
     * 更新查勘主信息
     *
     * @param PrpLCheckVo
     * @param prpLcheckTask
     * @throws Exception
     */
    @RequestMapping(value = "/updateCheckMain.do")
    @ResponseBody
    public AjaxResult updateCheckMain(@FormModel("checkVo") PrpLCheckVo checkVo,
                                      @FormModel("checkTaskVo") PrpLCheckTaskVo checkTaskVo,
                                      @FormModel("checkDutyVo") PrpLCheckDutyVo checkDutyVo) throws Exception {
        String user = WebUserUtils.getUserCode();
        AjaxResult ajaxResult = new AjaxResult();
        try {
            checkVo.setPrpLCheckTask(checkTaskVo);
            checkHandleService.updateCheckMain(checkVo);

            // 更新标的车的Duty
            checkHandleService.updateMainCarDuty(checkDutyVo, checkVo, user);
            ajaxResult.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            logger.debug("更新查勘主信息失败！" + e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 暂存
     *
     * @param PrpLCheckVo,prpLcheckExtVo
     * @param PrpLCheckTaskVo
     * @param PrpLCheckPropVo,PrpLCheckPersonVo
     */
    @RequestMapping(value = "/saveCheck.do")
    @ResponseBody
    public AjaxResult saveCheck(@FormModel("checkVo") PrpLCheckVo checkVo,
                                @FormModel("checkTaskVo") PrpLCheckTaskVo checkTaskVo,
                                @FormModel("checkPropVos") List<PrpLCheckPropVo> checkPropVos,
                                @FormModel("checkPersonVos") List<PrpLCheckPersonVo> checkPersonVos,
                                @FormModel("checkDutyVo") PrpLCheckDutyVo checkDutyVo,
                                @FormModel("disasterVo") PrpLDisasterVo disasterVo,
                                @FormModel("checkExtVos") List<PrpLCheckExtVo> checkExtVos,
                                @FormModel("lossChargeVos") List<PrpLDlossChargeVo> lossChargeVos,
                                @FormModel("claimDeductVo") List<PrpLClaimDeductVo> claimDeductVos,
                                @FormModel("saveType") String saveType,
                                @FormModel("subrogationMain") PrpLSubrogationMainVo subrogationMainVo,
                                String flowTaskId,
                                HttpSession session) throws ParseException {
        SysUserVo userVo = WebUserUtils.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        try {
            // List排空
            List<PrpLCheckPropVo> newPropList = new ArrayList<PrpLCheckPropVo>();
            for (int i = 0; i < checkPropVos.size(); i++) {
                PrpLCheckPropVo tempList = checkPropVos.get(i);
                if (tempList != null) {
                    // 如果为小数，则将数量转成整数存储
                    int lossNum = Double.valueOf(tempList.getLossNum()).intValue();
                    tempList.setLossNum(String.valueOf(lossNum));
                    newPropList.add(tempList);
                }
            }
            List<PrpLCheckPersonVo> newPersonList = new ArrayList<PrpLCheckPersonVo>();
            for (int i = 0; i < checkPersonVos.size(); i++) {
                PrpLCheckPersonVo tempList = checkPersonVos.get(i);
                if (tempList != null) {
                    newPersonList.add(tempList);
                }
            }
            List<PrpLCheckExtVo> newExtList = new ArrayList<PrpLCheckExtVo>();
            for (int i = 0; i < checkExtVos.size(); i++) {
                PrpLCheckExtVo tempList = checkExtVos.get(i);
                if (tempList != null) {
                    newExtList.add(tempList);
                }
            }

            // 组织业务数据
            //checkHandleService.updateCheckMain(prpLcheckVo);
            checkTaskVo.setPrpLCheckProps(newPropList);
            checkTaskVo.setPrpLCheckPersons(newPersonList);
            checkTaskVo.setPrpLCheckExts(newExtList);
            checkVo.setPrpLCheckTask(checkTaskVo);

            if (disasterVo.getId() == null && "".equals(disasterVo.getDisasterCodeOne())
                    && "".equals(disasterVo.getDisasterCodeTwo())) {
                disasterVo = null;
            }
            // 移动端案件理赔处理要通知快赔 并写会理赔处理标识
            // 写会标志
            PrpLWfTaskVo wfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(checkVo.getRegistNo(), new BigDecimal(flowTaskId));
            if (wfTaskVo != null) { // 发送通知
                wfTaskVo.setHandlerStatus("2");
                wfTaskVo.setWorkStatus("2");
                wfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CHECKSAVE);
                String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
                interfaceAsyncService.packMsg(wfTaskVo, url);
            }

            Long chkId = checkHandleService.save(checkVo, disasterVo, userVo, saveType);// 暂存

            // 更新标的车duty
            checkHandleService.updateMainCarDuty(checkDutyVo, checkVo, userVo.getUserCode());

            // 保存代位信息
            Long subr = checkHandleService.saveSubrogationMain(subrogationMainVo, chkId, userVo.getUserCode());
            checkHandleService.saveCharge(lossChargeVos, chkId, userVo);// 查勘费用赔款信息（除公估费）

            // 更新免赔率
            if (claimDeductVos != null && claimDeductVos.size() > 0) {
                checkHandleService.updateClaimDeduct(claimDeductVos, checkVo.getRegistNo());
            }

            ajaxResult.setStatus(HttpStatus.SC_OK);
            ajaxResult.setData(chkId);
            ajaxResult.setStatusText(subr.toString());
            session.setAttribute("checkId", chkId);

        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setStatusText(e.getMessage());
            logger.debug("查勘暂存失败！" + e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 异步刷新查勘的财产或者人伤
     *
     * @param checkId
     * @return
     * @throws Exception
     * @modified: ☆Luwei(2016年7月5日 下午): <br>
     */
    @RequestMapping("/refreshProp.ajax")
    @ResponseBody
    public ModelAndView refreshProp(Long checkId, String registNo) {
        ModelAndView mav = new ModelAndView();
        List<PrpLCheckPropVo> propVos = null;
        PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(checkId);
        PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();

        if (checkTaskVo != null) {
            propVos = checkTaskVo.getPrpLCheckProps();
        }
        Map<String, String> loss = checkHandleService.getCarLossParty(registNo);
        mav.addObject("checkPropVos", propVos);
        mav.addObject("propSize", 0);
        mav.addObject("loss", loss);
        mav.addObject("nodeCode", "Chk");
        mav.setViewName("check/checkEdit/PropLossItem");
        return mav;
    }

    /**
     * 异步刷新查勘的财产或者人伤
     *
     * @param checkId
     * @return
     * @throws Exception
     * @modified: ☆Luwei(2016年7月5日 下午): <br>
     */
    @RequestMapping("/refreshPerson.ajax")
    @ResponseBody
    public ModelAndView refreshPerson(Long checkId, String registNo) {
        ModelAndView mav = new ModelAndView();
        List<PrpLCheckPersonVo> personVos = null;
        PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(checkId);
        PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();
        if (checkTaskVo != null) {
            personVos = checkTaskVo.getPrpLCheckPersons();
        }
        Map<String, String> loss = checkHandleService.getCarLossParty(registNo);
        mav.addObject("checkPersonVos", personVos);
        mav.addObject("propSize", 0);
        mav.addObject("loss", loss);
        mav.addObject("nodeCode", "Chk");
        mav.setViewName("check/checkEdit/PersonLossItem");
        return mav;
    }

    /**
     * 查勘提交汇总初始化
     *
     * @param checkId
     * @param session
     * @throws Exception
     */
    @RequestMapping(value = "/initCheckSubmit.do")
    public ModelAndView initCheckSubmit(Long checkId, HttpSession session) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 查勘提交
        //Map<String,String> checkLossMap = checkHandleService.initCheckSubmitDloss(checkId);
        String url = SpringProperties.getProperty("MClaimPlatform_URL_IN") + AUTOSCHEDULE_URL_METHOD;
        PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(checkId);
        List<PrpLScheduleDefLossVo> scheduleDefLossVoList =
                checkHandleService.initCheckSubmitDloss(checkVo, url);
//		List<PrpLScheduleDefLossVo> scheduleDefLossVos = new ArrayList<PrpLScheduleDefLossVo>();
//		for(PrpLScheduleDefLossVo scheduleDefLossVo : scheduleDefLossVoList){
//			if(scheduleDefLossVo == null) continue;
//			scheduleDefLossVos.add(scheduleDefLossVo);
//		}
        double lossFee = checkHandleService.getLossFee(checkId);
        String majorFlag = checkHandleService.getMajorFlag(checkId);
        mav.addObject("defLossList", scheduleDefLossVoList);
        mav.addObject("checkId", checkId);
        mav.addObject("lossFees", lossFee);
        mav.addObject("majorFlag", majorFlag);
        mav.addObject("userCode", WebUserUtils.getUserCode());
        mav.addObject("registNo", checkVo.getRegistNo());
        //mav.addObject("checkAreaCode","440303");
        // mav.addObject("checkAddress","测试");
        //mav.addObject("lngXlatY","114.172469,22.582734");
        Map<String, String> userMap = checkHandleService.findByChkBigAndGradeid("Chk", WebUserUtils.getComCode(), "1");
        mav.addObject("userMap", userMap);
        mav.setViewName("check/checkEdit/CheckSubmit");
        return mav;
    }

    /**
     * 查勘提交
     *
     * @param PrpLCheckVo
     */
    @RequestMapping(value = "/checkSubmit.do")
    @ResponseBody
    public AjaxResult checkSubmit(@FormModel("defLoss") List<PrpLScheduleDefLossVo> defLossVos,
                                  Long sub_checkId, Long sub_flowTaskId, String sub_flowId, double sub_LossFees, HttpSession session) {
        SysUserVo userVo = WebUserUtils.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        String status = (String) session.getAttribute("checkSubmit_status");
        if ("doing".equals(status)) {
            // 提示前端有操作正在提交
            ajaxResult.setStatusText("当前操作人员有查勘任务正在提交，请等待或刷新后再试....");
            return ajaxResult;
        }
        PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(sub_checkId);

        // 查询是否有财产损失
        if (checkVo.getPrpLCheckTask().getPrpLCheckProps() == null || checkVo.getPrpLCheckTask().getPrpLCheckProps().size() <= 0) {
            checkVo.setIsPropLoss("0");
        } else {
            checkVo.setIsPropLoss("1");
        }
        // 查询是否人伤
        if (checkVo.getPrpLCheckTask().getPrpLCheckPersons() == null || checkVo.getPrpLCheckTask().getPrpLCheckPersons().size() <= 0) {
            checkVo.setIsPersonLoss("0");
        } else {
            checkVo.setIsPersonLoss("1");
        }
        if ("on".equals(checkVo.getOperateStatus())) {
            ajaxResult.setStatusText("当前操作人员有查勘任务正在提交，请等待或刷新后再试....");
            return ajaxResult;
        }

        // 查勘类别
        PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(userVo.getUserCode());
        if (intermVo != null) {
            checkVo.setRemark(intermVo.getIntermCode());
        }
        checkVo.setCheckClass(intermVo != null ? CheckClass.CHECKCLASS_Y : CheckClass.CHECKCLASS_N);

        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(sub_flowTaskId.doubleValue());
        if (CodeConstants.HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())) {
            session.setAttribute("checkSubmit_status", "doing");
            try {
                checkVo.setOperateStatus("on");
                checkHandleService.updateCheckMain(checkVo);

                // 移动端案件理赔处理要通知快赔 并写会理赔处理标识
                // 写会标志
				/*PrpLWfTaskVo prpLWfTaskVo = sendMsgToMobileService.isMobileCaseAcceptInClaim(wfTaskVo.getRegistNo(),new BigDecimal(sub_flowTaskId));
				if(prpLWfTaskVo!=null){ // 发送通知
					prpLWfTaskVo.setHandlerStatus("3"); // 已处理
					prpLWfTaskVo.setWorkStatus("3"); // 提交
				     prpLWfTaskVo.setMobileOperateType(CodeConstants.MobileOperationType.CHECKSUBMIT);
				     String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
				     interfaceAsyncService.packMsg(prpLWfTaskVo,url);
				}*/
                // 查勘提交
                List<PrpLWfTaskVo> wfTaskVos = checkHandleService.submitCheckToDloss(defLossVos,
                        sub_checkId, sub_flowTaskId, sub_flowId, sub_LossFees, userVo, "0", null);
                // 保存移动端案件显示标志
                PrpLWfTaskVo wfTaskOutVo = wfTaskHandleService.queryTask(sub_flowTaskId.doubleValue());
                String bussTag = wfTaskOutVo.getBussTag();
                JSONObject json = JSONObject.parseObject(bussTag);
                Map<String, String> map = JSONObject.toJavaObject(json, Map.class);
                map.put("isMobileCase", "0");
                wfTaskOutVo.setBussTag(JSONObject.toJSONString(map));
                wfTaskHandleService.updateTaskOut(wfTaskOutVo);
                //yzy
                // 保存公估信息

                // 请求德联易控的接口
                // 查询开关表--2-表示查勘节点请求
                // 车辆信息yzy
				if(wfTaskVo!=null && "Chk".equals(wfTaskVo.getSubNodeCode())){
					if(checkVo.getPrpLCheckTask()!=null && checkVo.getPrpLCheckTask().getPrpLCheckCars()!=null){
						for(PrpLCheckCarVo vo:checkVo.getPrpLCheckTask().getPrpLCheckCars()){
								String Qurl=SpringProperties.getProperty("YX_QUrl");
					 		   interfaceAsyncService.SendControlExpert(checkVo.getRegistNo(), userVo, vo.getPrpLCheckCarInfo().getLicenseNo(), "", "03",Qurl);
						}

					}
				}


                // 返回下一节点处理界面
                if (wfTaskVos != null && wfTaskVos.size() > 0) {
                    String idList = "";
                    for (PrpLWfTaskVo task : wfTaskVos) {
                        if (!FlowNode.Certi.name().equals(task.getNodeCode())) {
                            idList += task.getTaskId().toString() + ",";
                        }
                    }
                    if (StringUtils.isNotBlank(idList)) {
                        ajaxResult.setData(idList.substring(0, idList.length() - 1));
                    }
//					for(int i = 0; i<wfTaskVos.size(); i++ ){
//						if( !"Certi".equals(wfTaskVos.get(i).getNodeCode())){
//							idList += wfTaskVos.get(i).getTaskId().toString()+",";
//						}
//					}
//					ajaxResult.setData(idList = idList.substring(0,idList.length()-1));
                    ajaxResult.setStatusText(wfTaskVos.get(0).getRegistNo());
                    // 判断定损人员是否是当前处理人(是-201)
                    boolean isSelf = userVo.getUserCode().equals(wfTaskVos.get(0).getAssignUser());
                    ajaxResult.setStatus(isSelf ? HttpStatus.SC_CREATED : HttpStatus.SC_OK);
                }
                if (checkVo != null) {// 自助理赔
                    PrpLRegistVo prplregistVo = registQueryService.findByRegistNo(checkVo.getRegistNo());
                    if (prplregistVo != null && "1".equals(prplregistVo.getSelfClaimFlag())) {
                        interfaceAsyncService.sendClaimResultToSelfClaim(checkVo.getRegistNo(), userVo, "5", "1", "");
                    }
                }
                
				// 移动端案件理赔处理要通知快赔 并写会理赔处理标识
				// 写会标志
				PrpLWfTaskVo wfTaskVoss = sendMsgToMobileService.isMobileCaseAcceptInClaim(checkVo.getRegistNo(),new BigDecimal(sub_flowTaskId));
				if(wfTaskVoss!=null){ // 发送通知
					wfTaskVoss.setHandlerStatus("3");
					wfTaskVoss.setWorkStatus("3");
					wfTaskVoss.setMobileOperateType(CodeConstants.MobileOperationType.CHECKSUBMIT);
					String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
					interfaceAsyncService.packMsg(wfTaskVoss,url);
				}
                // 如果险别已改变，需要更新并提示
                if (checkVo.getPrpLCheckTask() != null && checkVo.getPrpLCheckTask().getPrpLCheckCars() != null) {
                    for (PrpLCheckCarVo carVo : checkVo.getPrpLCheckTask().getPrpLCheckCars()) {
                        if (CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR.equals(carVo.getSerialNo().toString()) && StringUtils
                                .isNotEmpty(carVo.getKindCode())) {
                            PrpLCItemKindVo itemKindVo = policyViewService.findItemKindByKindCode(checkVo.getRegistNo(), carVo.getKindCode());
                            if (itemKindVo == null) {
                                Map<String, Object> resultMap = new HashMap<String, Object>();
                                resultMap.put("info", "标的车损失险别在案件中不存在，已更新为案件保单承保的险别！");
                                ajaxResult.setDatas(resultMap);
                                carVo.setKindCode("");
                                checkHandleService.updateCheckCar(carVo);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ajaxResult.setStatusText(e.getMessage());
                logger.error(checkVo.getRegistNo() + "查勘提交失败！", e);
            }
            session.removeAttribute("checkSubmit_status");
            checkVo.setOperateStatus("off");
            checkHandleService.updateCheckMain(checkVo);
            checkHandleService.saveAssessor(checkVo.getRegistNo(), userVo);
            
            // 保存查勘费信息
            acheckTaskService.addCheckFeeTaskOfCheck(checkVo.getRegistNo(), userVo);
            try {
                //调用影像系统“影像资料统计接口”，查询该工号在该任务中上传的影像数量并保存（异步执行）
                String url = SpringProperties.getProperty("YX_QUrl") + "?";
                interfaceAsyncService.getReqImageNum(userVo, CodeConstants.APPROLE, checkVo.getRegistNo(), "", url, CodeConstants.APPNAMECLAIM, CodeConstants.APPCODECLAIM);
                interfaceAsyncService.getReqCheckUserImageNum(userVo, CodeConstants.APPROLE, checkVo.getRegistNo(), url,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
            } catch (Exception e) {
                logger.info("查勘调用影像系统影像资料统计接口报错=============", e);
            }
            
			//埋点把理赔信息推送到rabbitMq中间件，供中台使用			
			claimToMiddleStageOfCaseService.middleStageQuery(wfTaskVo.getRegistNo(), "Check");

        } else if (CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())) {
            ajaxResult.setStatusText("任务已提交或者注销！提交人：" + wfTaskVo.getHandlerUser());
            // throw new IllegalArgumentException("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
        } else {
            ajaxResult.setStatusText("任务错误！");
            // throw new IllegalArgumentException("任务错误！");
        }
        return ajaxResult;
    }


    @RequestMapping(value = "/checkNextEdit.do")
    @ResponseBody
    public ModelAndView checkNextEdit(String idList) {
        ModelAndView mav = new ModelAndView();
        String[] idLists = idList.split(",");
        List<PrpLWfTaskVo> wfTaskVoList = new ArrayList<PrpLWfTaskVo>();
        for (int i = 0; i < idLists.length; i++) {
            PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(idLists[i]));
            wfTaskVoList.add(wfTaskVo);
        }
        mav.addObject("wfTaskVoList", wfTaskVoList);
        mav.setViewName("check/checkEdit/ViewLossEdit");
        return mav;
    }

    /**
     * 更新报案出险经过说明
     */
    @RequestMapping(value = "/updateDangerRemark.ajax")
    @ResponseBody
    public AjaxResult updateDangerRemark(String registNo, String dangerRemark) {
        String user = WebUserUtils.getUserCode();
        AjaxResult ajaxResult = new AjaxResult();
        try {
            checkHandleService.updateDangerRemark(registNo, dangerRemark, user);
            ajaxResult.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            logger.debug("更新报案出险经过说明失败！" + e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 1 校验责任比例 本案件的所有车辆的责任比例之和不能超过100% 2是否存在还有未处理的车辆
     */
    @RequestMapping(value = "/validCheck.do")
    @ResponseBody
    public AjaxResult validCheck(
            @FormModel("checkVo") PrpLCheckVo checkVo,
            @FormModel("checkDutyVo") PrpLCheckDutyVo checkDutyVo,
            @FormModel("subrogationMain") PrpLSubrogationMainVo subrogationMainVo) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String retData = checkHandleService.validHandleCar(checkVo,
                    checkDutyVo, subrogationMainVo);
            ajaxResult.setStatus(HttpStatus.SC_OK);
            ajaxResult.setData(retData);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            logger.debug("查勘提交校验失败！" + e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 1- 互碰自赔 校验 , 2 - 标的车是否承保承保车上货物险
     */
    @RequestMapping(value = "/validCheckClaim.do")
    @ResponseBody
    public AjaxResult validCheckClaim(String registNo) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String retData = checkHandleService.validCheckClaim(registNo);
            ajaxResult.setStatus(HttpStatus.SC_OK);
            ajaxResult.setData(retData);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            // logger.debug("互碰自赔 校验 "+e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * 暂存成功后更新金额，刷立案 @throws Exception
     */
    @RequestMapping(value = "/saveSuccess.do")
    @ResponseBody
    public AjaxResult saveSuccess(String registNo) throws Exception {
        double retData = checkHandleService.saveSuccess(registNo);
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setStatus(HttpStatus.SC_OK);
        ajaxResult.setData(retData);
        return ajaxResult;
    }

    /**
     * 互碰自赔的-->保单信息校验
     *
     * @throws Exception
     */
    @RequestMapping(value = "/policyInfoValid.ajax")
    @ResponseBody
    public AjaxResult policyInfoValid(String registNo) throws Exception {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            String retMsg = checkHandleService.subRadioValid(registNo);
            ajaxResult.setStatus(HttpStatus.SC_OK);
            ajaxResult.setData(retMsg);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            logger.debug("互碰自赔的-->保单信息校验失败！ " + e.getMessage());
        }
        return ajaxResult;
    }

    @RequestMapping(value = "/dropPropLoss.ajax")
    public void dropPropLoss(Long propId) throws Exception {
        checkHandleService.dropPropLoss(propId);
    }

    @RequestMapping(value = "/dropPersonLoss.ajax")
    public void dropPersonLoss(Long personId) throws Exception {
        checkHandleService.dropPersonLoss(personId);
    }


    /**
     * 大案审核,复勘
     *
     * @param checkId,flowTaskId,codeName
     * @throws Exception
     */
    @RequestMapping(value = "/chkBigSave.do")
    @ResponseBody
    public AjaxResult chkBigSave(Long checkId, Long flowTaskId, String codeName, String isTimeout,
                                 String deflossRepairType, String repairFee, String contextBig, String saveType) throws Exception {
        AjaxResult ajaxResult = new AjaxResult();
        SysUserVo userVo = WebUserUtils.getUser();
        // 组织业务数据
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
        String handleIdKey = wfTaskVo.getHandlerIdKey();
        PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(checkId);
        PrpLCheckTaskVo checkTaskVo = checkVo.getPrpLCheckTask();

        if (!"Check".equals(codeName) && !"Chk".equals(codeName)
                && !"ChkRe".equals(codeName)) {
            checkTaskVo.setVerifyCheckContext(contextBig);
            checkTaskVo.setDeflossRepairType(deflossRepairType);// 建议定损修复方式
            checkTaskVo.setIsTimeout(isTimeout);
            if (StringUtils.isEmpty(repairFee)) {
                repairFee = "0";
            }
            checkTaskVo.setRepairFee(new BigDecimal(repairFee));// 建议修复金额
            checkTaskVo.setVerifyCheckFlag(CodeConstants.VerifyCheckFlag.VERIFYCHECK_Y);
            checkTaskVo.setUnderWriteUserCode(userVo.getUserCode());// 查勘审核人员代码
            if (saveType.equals("save")) {
                checkTaskVo.setUnderWriteState("1");// 查勘审核状态 1-正在处理
            } else {
                checkTaskVo.setUnderWriteState("2");// 查勘审核状态 2-已处理
                checkTaskVo.setUnderWriteDate(new Date());// 查勘审核通过时间
            }
        } else if (codeName.equals("ChkRe")) {
            checkTaskVo.setContexts(contextBig);
        }
        checkVo.setPrpLCheckTask(checkTaskVo);

        try {
            // 更新
            checkHandleService.updateCheckMain(checkVo);
            // 创建意见列表
            saveClaimText(checkId, saveType, codeName, userVo, contextBig, handleIdKey);

            ajaxResult.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            throw new IllegalArgumentException("提交错误！" + e.getMessage());
        }
        return ajaxResult;
    }

    private void saveClaimText(Long checkId, String saveType, String codeName,
                               SysUserVo userVo, String contextBig, String handleIdKey) throws Exception {
        // 创建意见列表
        PrpLClaimTextVo claimTextVo = checkHandleService.createClaimText(
                checkId, saveType, codeName, "1", userVo);
        claimTextVo.setDescription(contextBig);
        if ("submit".equals(saveType)) {
            // 有权限 ： 审核通过
            // 无权限 ： 提交上级
            FlowNode nextNode = checkHandleService.getNextNodeCode(codeName, checkId);
            claimTextVo.setStatus(FlowNode.END.equals(nextNode)
                    ? AuditStatus.CHKBIGEND : AuditStatus.AUDIT);
        } else if ("submitChkRe".equals(saveType)) {
            // 复勘完成
            claimTextVo.setStatus(AuditStatus.SUBMITCHKRE);
        } else {
            claimTextVo.setStatus(AuditStatus.SAVE);
        }
        claimTextVo.setBussNo(handleIdKey);
        claimTextService.saveOrUpdte(claimTextVo);
    }

    /**
     * <pre>
     * 复勘或大案审核提交汇总
     * </pre>
     *
     * @param codeName
     * @param checkId
     * @param flowTaskId
     * @return
     * @modified: ☆Luwei(2016年9月20日 下午4:36:29): <br>
     */
    @RequestMapping(value = "/initChkBigSubmit.do")
    public ModelAndView chkBigSubmit(String codeName, Long checkId, Long flowTaskId) {
        ModelAndView mav = new ModelAndView();
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
        if ("ChkRe".equals(codeName)) {
            // 复勘提交规则
            PrpLWfTaskVo outTaskVo = getChkReNextNode(wfTaskVo.getUpperTaskId().doubleValue());
            String nextCodeName = FlowNode.valueOf(outTaskVo.getSubNodeCode()).getName();// 提交到复勘的节点名称
            mav.addObject("nextCodeName", nextCodeName);
            Map<String, String> taskUserMap = new HashMap<String, String>();
            if (StringUtils.isNotBlank(outTaskVo.getTaskOutUser())) {
                SysUserVo userVo = sysUserService.findByUserCode(outTaskVo.getTaskOutUser());
                taskUserMap.put(userVo.getUserCode(), userVo.getUserName());
            }
//			taskUserMap.put(WebUserUtils.getUserCode(),WebUserUtils.getUserName());
            mav.addObject("taskUserMap", taskUserMap);// 提交人
        } else {
            FlowNode nextNode = checkHandleService.getNextNodeCode(codeName, checkId);
            mav.addObject("nextCodeName", nextNode.getName());
//			Map<String,String> chkBigUserMap = checkHandleService.findByChkBigAndGradeid("ChkBig",WebUserUtils.getComCode(),"2");
//			mav.addObject("chkBigUserMap", chkBigUserMap);
        }
        mav.addObject("checkId", checkId);
        mav.addObject("flowTaskId", flowTaskId);
        mav.addObject("codeName", FlowNode.valueOf(codeName).getName());
        mav.addObject("currentNode", codeName);
        mav.addObject("nodeCode", wfTaskVo.getNodeCode());
        mav.setViewName("check/chkBig/ChkBigSubmit");
        return mav;
    }

    /**
     * <pre>
     * 复勘提交规则
     * </pre>
     *
     * @param flowTaskId
     * @return
     * @modified: ☆Luwei(2016年9月20日 下午4:30:34): <br>
     */
    private PrpLWfTaskVo getChkReNextNode(Double flowTaskId) {
        PrpLWfTaskVo outTaskVo = null;
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
        if (FlowNode.VLoss.toString().equals(wfTaskVo.getNodeCode())
                && !WorkStatus.TURN.equals(wfTaskVo.getWorkStatus())) {
            outTaskVo = wfTaskVo;
        } else {
            outTaskVo = getChkReNextNode(wfTaskVo.getUpperTaskId().doubleValue());
        }
        return outTaskVo;
    }

    /**
     * 大案审核提交
     *
     * @param PrpLCheckVo
     */
    @RequestMapping(value = "/chkBigSubmit.do")
    @ResponseBody
    public AjaxResult chkBigSubmit(Long checkId, Long flowTaskId, String codeName, String chkBigUser, String sendMail) throws ParseException {
        AjaxResult ajaxResult = new AjaxResult();
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(flowTaskId.doubleValue());
        if (HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())) {
            try {
                // 大案审核提交
                if (StringUtils.isEmpty(chkBigUser)) {
                    chkBigUser = WebUserUtils.getUserCode();
                }
                checkHandleService.chkReOrBigSubmit(codeName, checkId, flowTaskId, chkBigUser, WebUserUtils.getUser());
                ajaxResult.setStatus(HttpStatus.SC_OK);
                ajaxResult.setData("报案号：" + wfTaskVo.getRegistNo());
                try {
					boolean sendMailFlag = true;
                    List<PrpLWfTaskVo> wfTaskInList = wfTaskHandleService.findInTaskByOther(wfTaskVo.getRegistNo(), null, FlowNode.ChkBig.name());
                    if(wfTaskInList != null && wfTaskInList.size() > 0){
                        // 存在未审核通过的大案任务，不发送邮件
                        sendMailFlag = false;
                    }

					if(sendMailFlag){
                        List<PrpdEmailVo> prpdEmailVoList = mailModelService.sendMailNew(wfTaskVo, WebUserUtils.getUser());
                        String mailTableStr = mailModelService.getMailTable(prpdEmailVoList);
                        ajaxResult.setData(mailTableStr);
                    }
                } catch (Exception e) {
                    logger.error(wfTaskVo.getRegistNo() + "==============车物邮件上报报错：" + e);
                }
            } catch (Exception e) {
                ajaxResult.setStatusText(e.getMessage());
                logger.error(wfTaskVo.getRegistNo() + "==============车物提交报错：" + e);
                throw new IllegalArgumentException("提交错误！");
            }
        } else if (HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())) {
            throw new IllegalArgumentException("任务已提交或者注销！提交人：" + wfTaskVo.getHandlerUser());
        } else {
            throw new IllegalArgumentException("任务错误！");
        }
        return ajaxResult;
    }

    /**
     * 更新收款人DIV
     */
    @RequestMapping(value = "/loadPayCusInfo.ajax")
    public ModelAndView loadPayCusInfo(Long payCustomId) {
        ModelAndView mav = new ModelAndView();
        PrpLPayCustomVo payCustomVo = checkHandleService.getPayCusInfo(payCustomId);
        mav.addObject("payCustomVo", payCustomVo);
        mav.setViewName("check/checkEdit/CheckPayeeInfo");
        return mav;
    }

    /**
     * 更新公估费的收款人选项
     */
    @RequestMapping(value = "/refreshPayCustom.ajax")
    @ResponseBody
    public AjaxResult refreshPayCustom(String registNo) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            // 获取公估费的收款人下拉框
            ajaxResult.setData(checkHandleService.getCustom(registNo));
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
        }
        return ajaxResult;
    }

    /**
     * <pre>
     * 大案预报公共按钮
     * </pre>
     *
     * @param request
     * @return
     * @modified: ☆Luwei(2016年9月13日 上午10:38:30): <br>
     */
    @RequestMapping(value = "/viewBigOpinion.do")
    public ModelAndView viewBigOpinion(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        String registNo = request.getParameter("registNo");
        // String nodeCode = request.getParameter("nodeCode");
        // 查勘
        Long bussTaskId = checkHandleService.getCheckId(registNo);
        List<PrpLClaimTextVo> claimTextVos = claimTextService.findClaimTextList(bussTaskId, registNo, "ChkBig");
        // 查勘大案提交人意见
        List<PrpLCheckTaskVo> checkTaskVos = new ArrayList<PrpLCheckTaskVo>();
        // 定损大案提交人意见
        List<PrpLDlossCarMainVo> lossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
        if (claimTextVos != null && claimTextVos.size() > 0) {
            for (PrpLClaimTextVo claimTextVo : claimTextVos) {
                if (StringUtils.isNotBlank(claimTextVo.getBussTaskId() + "")) {
                    PrpLCheckTaskVo checkTaskVo = checkTaskService.findCheckTaskVoById(claimTextVo.getBussTaskId());
                    // 当查勘表中大案提交意见不为空时，则大案上报是在查勘环节发生的
                    if (StringUtils.isNotBlank(checkTaskVo.getClaimText())) {
                        checkTaskVos.add(checkTaskVo);
                    } else {
                        // 标的车定损时发起的大案预报,取大案预报上交的意见
                        List<PrpLDlossCarMainVo> prpLDlossCarMainVos = lossCarService.findLossCarMainByRegistNo(checkTaskVo.getRegistNo());
                        for (PrpLDlossCarMainVo prpLDlossCarMainVo : prpLDlossCarMainVos) {
                            if ("1".equals(prpLDlossCarMainVo.getDeflossCarType())) {
                                lossCarMainVos.add(prpLDlossCarMainVo);
                            }
                        }
                    }
                }
            }
        }

        // 人伤大案审核意见

        List<PrpLDlossPersTraceMainVo> prpLDLists = persTraceDubboService.findPersTraceMainVoList(registNo);

        List<Long> persBussTaskIds = new ArrayList<Long>();
        if (prpLDLists != null && prpLDLists.size() > 0) {
            for (PrpLDlossPersTraceMainVo TraceMainVo : prpLDLists) {
                persBussTaskIds.add(TraceMainVo.getId());
                //persBussTaskId=prpLDLists.get(0).getId();
            }
        }
        List<PrpLClaimTextVo> claimTextVoPLBigs = new ArrayList<PrpLClaimTextVo>();
        if (persBussTaskIds != null && persBussTaskIds.size() > 0) {
            for (Long persBussTaskId : persBussTaskIds) {
                List<PrpLClaimTextVo> claimTextVoPLBig = claimTextService.findClaimTextList(persBussTaskId, registNo, "PLBig");
                for (PrpLClaimTextVo prpLClaimTextVo : claimTextVoPLBig) {
                    claimTextVoPLBigs.add(prpLClaimTextVo);
                }
            }
        }

        // 人伤大案上报意见
        List<PrpLDlossPersTraceMainVo> persTraceMainVos = new ArrayList<PrpLDlossPersTraceMainVo>();
        if (claimTextVoPLBigs != null && claimTextVoPLBigs.size() > 0) {
            for (PrpLClaimTextVo ClaimTextVo : claimTextVoPLBigs) {
                PrpLDlossPersTraceMainVo lossPersTraceMainVo = persTraceDubboService.findPersTraceMainByPk(ClaimTextVo.getBussTaskId());
                lossPersTraceMainVo.setComCode(ClaimTextVo.getComCode());
                persTraceMainVos.add(lossPersTraceMainVo);
            }
        }
        mav.addObject("persTraceMainVos", persTraceMainVos);
        mav.addObject("lossCarMainVos", lossCarMainVos);
        mav.addObject("checkTaskVos", checkTaskVos);
        mav.addObject("claimTextVos", claimTextVos);
        mav.addObject("claimTextVoPLBigs", claimTextVoPLBigs);
        mav.setViewName("check/chkCommon/viewBigOpinion");
        return mav;
    }

    @RequestMapping(value = "/loadSubrationCar.ajax")
    public ModelAndView loadSubrationCar(int size, String registNo) {
        ModelAndView mv = new ModelAndView();
        PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
        PrpLSubrogationCarVo carVo = new PrpLSubrogationCarVo();
        carVo.setRegistNo(registNo);

        List<PrpLSubrogationCarVo> carList = new ArrayList<PrpLSubrogationCarVo>();
        carList.add(carVo);

        Map<String, String> subLiNoMap = checkHandleService.getSubLicenseNo(registNo);
        subrogationMain.setPrpLSubrogationCars(carList);
        mv.addObject("subrogationMain", subrogationMain);
        mv.addObject("subLiNoMap", subLiNoMap);
        mv.addObject("size", size);
        mv.setViewName("check/checkEdit/CheckEdit_Subrogation_CarTr");
        return mv;
    }

    @RequestMapping(value = "/loadSubrationPers.ajax")
    public ModelAndView loadSubrationPers(int size, String registNo) {
        ModelAndView mv = new ModelAndView();
        PrpLSubrogationMainVo subrogationMain = new PrpLSubrogationMainVo();
        PrpLSubrogationPersonVo personVo = new PrpLSubrogationPersonVo();
        personVo.setRegistNo(registNo);
        List<PrpLSubrogationPersonVo> personList = new ArrayList<PrpLSubrogationPersonVo>();
        personList.add(personVo);

        subrogationMain.setPrpLSubrogationPersons(personList);
        mv.addObject("subrogationMain", subrogationMain);
        mv.addObject("size", size);
        mv.setViewName("check/checkEdit/CheckEdit_Subrogation_PerTr");

        return mv;
    }


    @RequestMapping(value = "/refreshDutyRate.ajax")
    @ResponseBody
    public AjaxResult refreshDutyRate(String registNo) {
        AjaxResult ajaxResult = new AjaxResult();
        String policyType = "";// 保单类型，1-单交强，2-单商业，3-
        List<PrpLCMainVo> cMainVoList =
                policyViewService.getPolicyAllInfo(registNo);
        for (PrpLCMainVo cMainVo : cMainVoList) {
            policyType = Risk.DQZ.equals(cMainVo.getRiskCode()) ? "1" : "2";
        }
        if (cMainVoList != null && cMainVoList.size() == 2) {
            policyType = "3";
        }
        ajaxResult.setData(policyType);
        return ajaxResult;
    }

    /**
     * 验证本案是否河南快赔的无保单报案
     *
     * @param registNo
     * @return
     */
    @RequestMapping(value = "/nopolicyNoVerify.ajax")
    @ResponseBody
    public AjaxResult nopolicyNoVerify(String registNo) {
        String sign = "0";// 标记
        AjaxResult ajax = new AjaxResult();
        PrpLRegistVo prplregistVo = registQueryService.findByRegistNo(registNo);
        if (prplregistVo != null) {
            if ("1".equals(prplregistVo.getTempRegistFlag()) && "1".equals(prplregistVo.getIsQuickCase())) {
                sign = "1";
            }
        }
        ajax.setData(sign);
        return ajax;
    }

    @RequestMapping(value = "/PhotoVerify.do")
    @ResponseBody
    public AjaxResult photoVerify(@RequestParam("registNo") String registNo,
                                  @RequestParam("mainId") String checkId,
                                  @RequestParam("photoStatus") String photoStatus) {

        AjaxResult ajaxResult = new AjaxResult();
        try {
            SysUserVo userVo = WebUserUtils.getUser();
            // 定损照片审核不通过
            checkHandleService.photoVerifyToHNQC(registNo, photoStatus, checkId, CodeConstants.RadioValue.RADIO_NO, CodeConstants.HNQCDataType.PHOTOVERIFY, userVo);
            ajaxResult.setStatus(HttpStatus.SC_OK);
        } catch (Exception e) {
            ajaxResult.setStatusText(e.getMessage());
            e.printStackTrace();
        }

        return ajaxResult;
    }

    @RequestMapping("/queryFlowTaskIdIsGB.ajax")
    @ResponseBody
    public AjaxResult queryFlowTaskIdIsGB(String flowTaskId) {
        AjaxResult ajaxResult = null;
        if (StringUtils.isNotBlank(flowTaskId)) {
            Double flowId = Double.valueOf(flowTaskId);
            PrpLWfTaskVo task = wfTaskHandleService.queryTask(flowId);
            String registNo = task.getRegistNo();
            PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
            if (CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag()) || CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag())) {
                ajaxResult = new AjaxResult();
                ajaxResult.setStatusText("Y");// 是从共、从联
                ajaxResult.setData("此案件为从共/从联业务，请按照我方损失金额录入");
                ajaxResult.setStatus(Long.parseLong(task.getHandlerStatus()));
            }
        }
        if (ajaxResult == null) {
            ajaxResult = new AjaxResult();
            ajaxResult.setStatusText("N");
        }
        return ajaxResult;
    }


    /**
     * 获取查勘定损指标
     *
     * @param userCode
     * @return
     */
    @RequestMapping(value = "/checkIndexInit.do")
    public ModelAndView checkIndexInit(String userCode) throws IOException {
        ModelAndView mv = new ModelAndView();
        // 获取数据
        // 获取查勘指标逻辑
        PrpLIndiQuotaInfoVo prpLIndiQuotaInfoVo = checkTaskService.getCheckIndexInfoByUserCode(userCode);
        mv.addObject("indexInfo", prpLIndiQuotaInfoVo);
        mv.setViewName("check/checkIndex/checkIndexShow");
        return mv;
    }
}
