package ins.sino.claimcar.pinganUnion.service;

import com.alibaba.fastjson.JSON;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.service.*;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.EndCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.pinganUnion.dto.EndCaseRespData;
import ins.sino.claimcar.pinganUnion.dto.WholeCaseBaseDTO;
import ins.sino.claimcar.pinganUnion.dto.ZeroCancelApplyDTO;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.regist.service.*;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 平安联盟-结案信息查询接口业务数据处理入口
 * @Author liuys
 * @Date 2020/7/21 9:03
 */
@Service("pingAnEndCaseHandleService")
public class PingAnEndCaseHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnEndCaseHandleServiceImpl.class);

    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private EndCasePubService endCasePubService;
    @Autowired
    private InterfaceAsyncService interfaceAsyncService;
    @Autowired
    private ClaimTaskService claimTaskService;
    @Autowired
    private CompensateHandleService compensateHandleService;
    @Autowired
    private ClaimService claimService;
    @Autowired
    private CheckTaskService checkTaskService;
    @Autowired
    private VerifyClaimService verifyClaimService;
    @Autowired
    private RegistService registService;
    @Autowired
    private RegistHandlerService registHandlerService;
    @Autowired
    private ClaimCancelService claimCancelService;
    @Autowired
    private PrpLCMainService prpLCMainService;
    @Autowired
    private EndCaseService endCaseService;

    /** 交强理算标志位 */
    public static final String COMP_CI = "1";
    /** 商业理算标志位 */
    public static final String COMP_BI = "2";
    /** 交强险种前缀 */
    public static final String PREFIX_CI = "11";
    /** 商业险种前缀 */
    public static final String PREFIX_BI = "12";

    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("平安联盟-结案信息查询接口业务数据处理入口--registNo={},respData={}", registNo,respData);
        ResultBean resultBean = ResultBean.success();

        try {
            //解析json字符串
            EndCaseRespData endCaseRespData = JSON.parseObject(respData,EndCaseRespData.class);
            //基本校验
            checkData(registNo,endCaseRespData);

            //创建默认用户
            SysUserVo userVo = new SysUserVo();
            userVo.setUserCode("AUTO");
            userVo.setUserName("AUTO");

            WholeCaseBaseDTO wholeCaseBaseDTO = endCaseRespData.getWholeCaseBaseDTO();
            ZeroCancelApplyDTO zeroCancelApplyDTO = endCaseRespData.getZeroCancelApplyDTO();
            //结案操作
            if ("1".equals(wholeCaseBaseDTO.getIndemnityConclusion())) {//1-赔付
                writeBackEndCaseDate(registNo,null,wholeCaseBaseDTO.getCaseTimes(),wholeCaseBaseDTO.getEndCaseDate());
            }else if ("2".equals(wholeCaseBaseDTO.getIndemnityConclusion())){//2-零结
                zeroAutoEndCase(registNo,null,wholeCaseBaseDTO.getEndCaseDate(),zeroCancelApplyDTO.getApplyReason(),userVo);
            }else if ("3".equals(wholeCaseBaseDTO.getIndemnityConclusion())){//3-商业险拒赔
                List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
                if (CollectionUtils.isNotEmpty(prpLCMainVoList)){
                    for (PrpLCMainVo prpLCMainVo : prpLCMainVoList){
                        if (Risk.isDQZ(prpLCMainVo.getRiskCode())){
                            continue;
                        }
                        writeBackEndCaseDate(registNo,prpLCMainVo.getPolicyNo(),wholeCaseBaseDTO.getCaseTimes(),wholeCaseBaseDTO.getEndCaseDate());
                    }
                }

            }else if ("4".equals(wholeCaseBaseDTO.getIndemnityConclusion())){//4-整案拒赔
                writeBackEndCaseDate(registNo,null,wholeCaseBaseDTO.getCaseTimes(),wholeCaseBaseDTO.getEndCaseDate());
            }else if ("5".equals(wholeCaseBaseDTO.getIndemnityConclusion())){//5-注销
                List<PrpLCMainVo> prpLClaimVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
                for (PrpLCMainVo prpLCMainVo : prpLClaimVoList){
                    FlowNode subNode = FlowNode.ClaimBI;
                    if (Risk.isDQZ(prpLCMainVo.getRiskCode())){
                        subNode = FlowNode.ClaimCI;
                    }
                    //判断是否立案，有立案则走立案注销
                    if (CollectionUtils.isNotEmpty(wfTaskHandleService.findEndTask(registNo, null, subNode))){
                        registerCancel(registNo, prpLCMainVo.getPolicyNo(),wholeCaseBaseDTO.getCaseCancelReason(),wholeCaseBaseDTO.getEndCaseDate(),zeroCancelApplyDTO.getApplyDate(),userVo);
                    }else{
                        reportCancel(registNo,wholeCaseBaseDTO.getCaseCancelReason(), wholeCaseBaseDTO.getEndCaseDate(), userVo);
                    }
                }
            }
        }catch (Exception e){
            logger.error("平安联盟-结案信息查询接口业务数据处理报错：registNo={},error={}", registNo,ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }

        return resultBean;
    }

    /**
     * 自动结案操作
     * @param registNo
     * @param policyNo
     * @param endCaseDate
     */
    public void autoEndCase(String registNo, String policyNo, Date endCaseDate) {
        //查询理算节点数据
        List<PrpLCompensateVo> compensateVoList;
        if (StringUtils.isNotBlank(policyNo)) {
            compensateVoList = compensateTaskService.findPrplCompensateByRegistNoAndPolicyNo(registNo,policyNo);
        }else {
            compensateVoList = compensateTaskService.findCompensatevosByRegistNo(registNo);
        }
        if (CollectionUtils.isEmpty(compensateVoList)){
            throw new IllegalArgumentException("查询理算数据不存在");
        }

        //查询结案流程，in表有则结案操作，没有则查询out表是否有结案流程，有不做处理，如果in表和out都没有则抛出异常
        List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
        for (PrpLCompensateVo prpLCompensateVo : compensateVoList){
            List<PrpLWfTaskVo> inTaskVoList = wfTaskHandleService.findTaskInVo(registNo,prpLCompensateVo.getCompensateNo(),FlowNode.EndCas.name());
            List<PrpLWfTaskVo> outTaskVoList = wfTaskHandleService.findOutTaskVo(registNo,prpLCompensateVo.getCompensateNo(),FlowNode.EndCas.name());
            if (CollectionUtils.isEmpty(inTaskVoList) && CollectionUtils.isEmpty(outTaskVoList)){
                    throw new IllegalArgumentException("没有查询到结案任务");
            }
            if (CollectionUtils.isNotEmpty(inTaskVoList)){
                prpLWfTaskVoList.addAll(inTaskVoList);
            }
        }

        //循环结案操作
        if (CollectionUtils.isNotEmpty(prpLWfTaskVoList)) {
            for (PrpLWfTaskVo wfTask : prpLWfTaskVoList) {
                PrpLCompensateVo prpLCompensateVo = compensateTaskService.findCompByPK(wfTask.getCompensateNo());
                // 生成结案表
                PrpLEndCaseVo endCaseVo = endCasePubService.autoEndCaseForPingAn(wfTask, prpLCompensateVo, endCaseDate);

                // 结案送再保 mfn 2019-11-18 21:24:48
                sendEndCaseToReins(endCaseVo.getClaimNo());

                // 结案提交工作流
                WfTaskSubmitVo submitEndCase = new WfTaskSubmitVo();
                submitEndCase.setFlowId(wfTask.getFlowId());
                submitEndCase.setFlowTaskId(wfTask.getTaskId());
                submitEndCase.setCurrentNode(FlowNode.EndCas);
                submitEndCase.setNextNode(FlowNode.END);
                submitEndCase.setComCode(wfTask.getComCode());
                submitEndCase.setTaskInUser(wfTask.getTaskInUser());
                String endCaseNo = endCaseVo != null ? endCaseVo.getEndCaseNo() : "";
                submitEndCase.setTaskInKey(endCaseNo);
                submitEndCase.setHandleIdKey(endCaseVo.getCompensateNo());
                submitEndCase.setFlowStatus(CodeConstants.FlowStatus.END);
                wfTaskHandleService.submitEndCase(endCaseVo, submitEndCase);
            }
        }
    }

    /**
     * 回写结案时间
     * @param registNo
     * @param policyNo
     * @param endCaseDate
     */
    public void writeBackEndCaseDate(String registNo, String policyNo, Integer caseTimes, Date endCaseDate) {
        //查询理算节点数据
        List<PrpLCompensateVo> compensateVoList = compensateTaskService.findPrplCompensateByRegistNoAndPolicyNo(registNo,policyNo,caseTimes);
        if (CollectionUtils.isEmpty(compensateVoList)){
            throw new IllegalArgumentException("查询理算数据不存在");
        }

        for (PrpLCompensateVo prpLCompensateVo : compensateVoList){
           //查询立案数据
            PrpLClaimVo prpLClaimVo = claimService.findByClaimNo(prpLCompensateVo.getClaimNo());
            prpLClaimVo.setEndDate(endCaseDate);
            claimTaskService.claimWirteBack(prpLClaimVo);
            //查询结案数据
            List<PrpLEndCaseVo> prpLEndCaseVoList = endCaseService.findEndCaseVoByRegistNoAndCompeNo(registNo,prpLCompensateVo.getCompensateNo());
            if (CollectionUtils.isEmpty(prpLEndCaseVoList)){
                throw new IllegalArgumentException("查询结案数据不存在");
            }
            for (PrpLEndCaseVo prpLEndCaseVo : prpLEndCaseVoList){
                prpLEndCaseVo.setEndCaseDate(endCaseDate);
                endCaseService.saveOrUpdateEndCase(prpLEndCaseVo);
            }
        }
    }

    /**
     * 立案注销
     * @param registNo
     * @param policyNo
     * @param cancelReason
     * @param cancelDate
     * @param applyDate
     * @param userVo
     */
    public void registerCancel(String registNo,
                                String policyNo,
                                String cancelReason,
                                Date cancelDate,
                                Date applyDate,
                                SysUserVo userVo){
        //查询立案数据
        PrpLClaimVo prpLClaimVo = claimService.findClaimVoByRegistNoAndPolicyNo(registNo,policyNo);
        if (prpLClaimVo == null) {
            // 立案环节还未立案
            throw new IllegalArgumentException("立案环节未立案");
        }
        if ("0".equals(prpLClaimVo.getValidFlag())){
            //立案节点被注销
            logger.info("报案号registNo={}已立案注销，不要重复操作", registNo);
            return;
        }
        userVo.setComCode(prpLClaimVo.getComCode());

        //立案注销申请
        PrpLcancelTraceVo prpLcancelTraceVo = new PrpLcancelTraceVo();
        prpLcancelTraceVo.setRegistNo(prpLClaimVo.getRegistNo());
        prpLcancelTraceVo.setClaimNo(prpLClaimVo.getClaimNo());
        prpLcancelTraceVo.setFlowTask(prpLClaimVo.getFlowId());
        prpLcancelTraceVo.setTextType("01".trim());//01：立案注销/拒赔 02：立案注销/拒赔恢复
        prpLcancelTraceVo.setApplyDate(applyDate);//设置申请时间
        prpLcancelTraceVo.setApplyUserCode(userVo.getUserCode());
        prpLcancelTraceVo.setOperaToRCode(userVo.getUserCode());
        prpLcancelTraceVo.setInputTime(applyDate);
        prpLcancelTraceVo.setMakeCom(prpLClaimVo.getComCode());
        prpLcancelTraceVo.setComCode(prpLClaimVo.getComCode());
        prpLcancelTraceVo.setValidFlag("1");//是否有效 默认有效
        prpLcancelTraceVo.setStatus("1");//审核状态 默认审核通过
        prpLcancelTraceVo.setInsertTimeForHis(new Date());
        prpLcancelTraceVo.setOperateTimeForHis(new Date());
        prpLcancelTraceVo.setFlags("5");// 0-提交标志,5-注销拒赔标识
        prpLcancelTraceVo.setRiskCode(prpLClaimVo.getRiskCode());
        prpLcancelTraceVo.setApplyReason("9");//默认“其他”，注销原因放到备注
        prpLcancelTraceVo.setCancelText(cancelReason);//注销原因
        prpLcancelTraceVo.setRemarks(cancelReason);//备注
        prpLcancelTraceVo.setDealReasoon("1");//任务类型 "客户申请注销" TODO 注销直接默认客户申请注销类型
        prpLcancelTraceVo.setInsertTimeForHis(new Date());//创建时间
        prpLcancelTraceVo.setOperateTimeForHis(new Date());//操作时间
        prpLcancelTraceVo.setOpinionCode("05");//意见代码 05-审核通过

        //保存立案注销申请记录
        BigDecimal A = claimCancelService.saveTrace(prpLcancelTraceVo, userVo, FlowNode.CancelApp.getName());
        Long id = A.longValue();
        String subNodeCode = FlowNode.ClaimBI.name();
        if ("1101".equals(prpLClaimVo.getRiskCode().trim())) {
            subNodeCode = FlowNode.ClaimCI.name();
        }
        // 查看立案工作流
        PrpLWfTaskVo prpClaimWfTaskVo = wfTaskHandleService.queryTaskId(
                prpLClaimVo.getFlowId(), subNodeCode);

        //组装Task
        WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
        taskVo.setRegistNo(prpClaimWfTaskVo.getRegistNo());
        taskVo.setHandlerIdKey(prpClaimWfTaskVo.getTaskId().toString());

        //新增一个节点CancelApp
        WfTaskSubmitVo submitVo1 = new WfTaskSubmitVo();
        submitVo1.setFlowId(prpClaimWfTaskVo.getFlowId());
        submitVo1.setCurrentNode(FlowNode.valueOf(subNodeCode));
        submitVo1.setAssignCom(prpLClaimVo.getComCode());
        submitVo1.setAssignUser(userVo.getUserCode());
        submitVo1.setComCode(prpLClaimVo.getComCode());
        submitVo1.setTaskInKey(prpClaimWfTaskVo.getRegistNo());
        submitVo1.setTaskInUser(userVo.getUserCode());
        submitVo1.setFlowTaskId(prpClaimWfTaskVo.getTaskId());
        submitVo1.setNextNode(FlowNode.CancelApp);
        submitVo1.setHandleIdKey(id.toString());
        PrpLClaimCancelVo pClaimCancelVo = new PrpLClaimCancelVo();
        pClaimCancelVo.setClaimCancelTime(cancelDate);//设置注销时间
        pClaimCancelVo.setClaimTime(prpLClaimVo.getClaimTime());
        pClaimCancelVo.setClaimNo(prpLClaimVo.getClaimNo());
        PrpLWfTaskVo prpCancelWfTask = wfTaskHandleService.addCancelTask(taskVo, submitVo1, pClaimCancelVo);

        //结束CancelApp节点
        taskVo.setHandlerIdKey(id.toString());
        WfTaskSubmitVo submitVo2 = new WfTaskSubmitVo();
        submitVo2.setFlowId(prpClaimWfTaskVo.getFlowId());
        submitVo2.setCurrentNode(FlowNode.CancelApp);
        submitVo2.setHandleruser(userVo.getUserCode());
        submitVo2.setHandlertime(new Date());
        submitVo2.setComCode(prpLClaimVo.getComCode());
        submitVo2.setTaskInKey(prpClaimWfTaskVo.getRegistNo());
        submitVo2.setTaskInUser(userVo.getUserCode());
        submitVo2.setAssignCom(prpLClaimVo.getComCode());
        submitVo2.setFlowTaskId(prpCancelWfTask.getTaskId());
        submitVo2.setNextNode(FlowNode.END);
        submitVo2.setHandleIdKey(id.toString());
        submitVo2.setAssignCom(prpLClaimVo.getComCode());
        submitVo2.setAssignUser(userVo.getUserCode());
        wfTaskHandleService.submitClaimTask(taskVo, submitVo2, pClaimCancelVo);

        //发起审核需求
        submitVo2.setNextNode(FlowNode.CancelLVrf_LV1);
        PrpLWfTaskVo prpCancelLVrfTaskVo = wfTaskHandleService.addCancelTask(taskVo, submitVo2, pClaimCancelVo);
        //自动结束审核
        WfTaskSubmitVo submitVo3 = new WfTaskSubmitVo();
        submitVo3.setFlowId(prpClaimWfTaskVo.getFlowId());
        submitVo3.setCurrentNode(FlowNode.CancelLVrf_LV1);
        submitVo3.setNextNode(FlowNode.END);
        submitVo3.setHandleruser(userVo.getUserCode());
        submitVo3.setHandlertime(new Date());
        submitVo3.setComCode(prpLClaimVo.getComCode());
        submitVo3.setTaskInKey(prpClaimWfTaskVo.getTaskInKey());
        submitVo3.setTaskInUser(userVo.getUserCode());
        submitVo3.setFlowTaskId(prpCancelLVrfTaskVo.getTaskId());
        submitVo3.setNextNode(FlowNode.END);
        submitVo3.setAssignCom(prpLClaimVo.getComCode());
        submitVo3.setAssignUser(userVo.getUserCode());
        wfTaskHandleService.submitClaimTask(taskVo, submitVo3, pClaimCancelVo);

        //判断立案是否可以全部注销其它节点
        List<PrpLClaimVo> prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
        if (prpLClaimVoList.size() == 2) {//有一个注销了就可以注销其它节点
            for (PrpLClaimVo vo : prpLClaimVoList) {
                if (vo.getValidFlag().equals("0") || vo.getValidFlag().equals("2")) {
                    claimTaskService.cancleClaimForOther(registNo, userVo.getUserCode());
                }
            }
        } else {//只有一条数据就可以注销其它节点
            claimTaskService.cancleClaimForOther(registNo, userVo.getUserCode());
        }
        //立案节点注销
        claimTaskService.cancleClaim(prpLcancelTraceVo.getClaimNo(), "0",
                prpClaimWfTaskVo.getTaskId(), userVo, prpClaimWfTaskVo.getRegistNo(), submitVo3);
        //回写注销时间
        prpLClaimVoList = claimService.findClaimListByRegistNo(registNo);
        for (PrpLClaimVo claimVo : prpLClaimVoList){
            claimVo.setCancelTime(cancelDate);
            claimTaskService.claimWirteBack(claimVo);
        }
    }

    /**
     * 报案注销
     * @param registNo
     * @param cancelReason
     * @param cancelDate
     * @param userVo
     */
    public void reportCancel(String registNo, String cancelReason, Date cancelDate, SysUserVo userVo){
        logger.info("报案号registNo={}进行报案注销回写RegistTaskFlag的方法", registNo);
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        if (CodeConstants.CancelFlag.CANCEL.equals(prpLRegistVo.getCancelFlag())){
            logger.info("报案号registNo={}已报案注销，不要重复操作", registNo);
            return;
        }
        prpLRegistVo.setUpdateUser(userVo.getUserCode());
        prpLRegistVo.setUpdateTime(new Date());
        prpLRegistVo.getPrpLRegistExt().setUpdateUser(userVo.getUserCode());
        prpLRegistVo.getPrpLRegistExt().setUpdateTime(new Date());
        logger.info("报案号registNo={}给报案任务状态RegistTaskFlag赋值,值为={}", registNo, CodeConstants.RegistTaskFlag.CANCELED);
        prpLRegistVo.setRegistTaskFlag(CodeConstants.RegistTaskFlag.CANCELED);
        prpLRegistVo.setCancelFlag(CodeConstants.CancelFlag.CANCEL);
        prpLRegistVo.getPrpLRegistExt().setCancelReason(cancelReason);
        prpLRegistVo.getPrpLRegistExt().setCancelUser(userVo.getUserCode());
        prpLRegistVo.setCancelTime(cancelDate);//设置注销时间
        prpLRegistVo = registHandlerService.reportCancel(prpLRegistVo);

        // 提交工作流操作
        WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
        submitVo.setFlowId(prpLRegistVo.getFlowId());
        submitVo.setTaskInKey(prpLRegistVo.getRegistNo());
        submitVo.setFlowTaskId(BigDecimal.ZERO);
        submitVo.setComCode(prpLRegistVo.getComCode());
        submitVo.setAssignUser(userVo.getUserCode());
        submitVo.setTaskInUser(userVo.getUserCode());
        submitVo.setAssignCom(prpLRegistVo.getComCode());
        submitVo.setSubmitType(SubmitType.C);
        // 提交工作流
        wfTaskHandleService.submitRegist(prpLRegistVo, submitVo);
    }

    /**
     * 零结案
     * @param registNo
     * @param policyNo
     * @param endCaseDate
     * @param applyReason
     * @param userVo
     * @throws Exception
     */
    public void zeroAutoEndCase(String registNo, String policyNo, Date endCaseDate, String applyReason, SysUserVo userVo) throws Exception {
        //查询理算流程节点
        List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
        if (StringUtils.isNotBlank(policyNo)){
            PrpLCMainVo prpLCMainVo = prpLCMainService.findPrpLCMain(registNo,policyNo);
            if (prpLCMainVo == null){
                throw new IllegalArgumentException("报案保单数据不存在");
            }
            FlowNode subNode = FlowNode.CompeBI;
            if (Risk.isDQZ(prpLCMainVo.getRiskCode())){
                subNode = FlowNode.CompeCI;
            }
            List<PrpLWfTaskVo> prpLWfTaskVo = wfTaskHandleService.findCompeByRegistNo(registNo,subNode.name());
            if (CollectionUtils.isNotEmpty(prpLWfTaskVo)) {
                prpLWfTaskVoList.addAll(prpLWfTaskVo);
            }
        }else{
            List<PrpLWfTaskVo> prpLWfTaskVoBI = wfTaskHandleService.findCompeByRegistNo(registNo,FlowNode.CompeBI.name());
            List<PrpLWfTaskVo> prpLWfTaskVoCI = wfTaskHandleService.findCompeByRegistNo(registNo,FlowNode.CompeCI.name());
            if (CollectionUtils.isNotEmpty(prpLWfTaskVoBI)) {
                prpLWfTaskVoList.addAll(prpLWfTaskVoBI);
            }
            if (CollectionUtils.isNotEmpty(prpLWfTaskVoCI)) {
                prpLWfTaskVoList.addAll(prpLWfTaskVoCI);
            }
        }
        //判断
        if (CollectionUtils.isEmpty(prpLWfTaskVoList)){
            throw new IllegalArgumentException("没有查询到理算任务");
        }

        //获取标的车责任
        PrpLCheckDutyVo checkDuty = new PrpLCheckDutyVo();
        List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
        if (CollectionUtils.isNotEmpty(checkDutyList)) {
            for (PrpLCheckDutyVo checkDutyVo : checkDutyList) {
                if (checkDutyVo.getSerialNo() == 1) {
                    checkDuty = checkDutyVo;
                }
            }
        }
        //查勘记录
        PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);

        //组装理算数据
        for (PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskVoList){
            String flag = COMP_BI;// 判断是商业还是交强计算书
            String bcFlag = PREFIX_BI;// 11-交强 12-商业
            String policyType = CodeConstants.PolicyType.POLICY_DAA;//商业险保单类型
            if (prpLWfTaskVo.getSubNodeCode().equals(FlowNode.CompeCI.toString())) {
                flag = COMP_CI;
                bcFlag = PREFIX_CI;
                policyType = CodeConstants.PolicyType.POLICY_DZA;
            }
            //查询立案数据
            PrpLClaimVo reClaimVo = claimService.findClaimVoByClassCode(registNo, policyType);

            //组装PrpLCompensateVo对象
            PrpLCompensateVo prpLCompensateVo = new PrpLCompensateVo();
            prpLCompensateVo.setCompensateNo(prpLWfTaskVo.getCompensateNo());
            prpLCompensateVo.setRegistNo(registNo);
            prpLCompensateVo.setClaimNo(reClaimVo.getClaimNo());
            prpLCompensateVo.setPolicyNo(reClaimVo.getPolicyNo());
            prpLCompensateVo.setRiskCode(reClaimVo.getRiskCode());
            prpLCompensateVo.setMakeCom(reClaimVo.getComCode());
            prpLCompensateVo.setComCode(reClaimVo.getComCode());
            // 案件类型
            if (reClaimVo.getCaseFlag() != null
                    && reClaimVo.getCaseFlag().equals("1")) {
                prpLCompensateVo.setCaseType("2");
            } else {
                prpLCompensateVo.setCaseType("1");
                if (reClaimVo.getIsSubRogation() != null
                        && reClaimVo.getIsSubRogation().equals("1")) {
                    prpLCompensateVo.setCaseType("3");
                }
            }
            prpLCompensateVo.setCompensateType("N");
            prpLCompensateVo.setIndemnityDuty(checkDuty.getIndemnityDuty());
            prpLCompensateVo.setIndemnityDutyRate(checkDuty.getIndemnityDutyRate());
            prpLCompensateVo.setSumAmt(BigDecimal.ZERO);//当次理算金额（总赔付金额）
            prpLCompensateVo.setSumPreAmt(BigDecimal.ZERO);//当次已预付金额 TODO
            prpLCompensateVo.setSumPaidAmt(BigDecimal.ZERO);//当次赔付金额（扣除预付）
            prpLCompensateVo.setSumFee(BigDecimal.ZERO);//当次理赔/费用
            prpLCompensateVo.setSumPreFee(BigDecimal.ZERO);//当次已预付理赔费用 TODO
            prpLCompensateVo.setSumPaidFee(BigDecimal.ZERO);//当次赔付理赔费用（扣除预付）
            prpLCompensateVo.setSumBzPaid(BigDecimal.ZERO);//总交强赔付 TODO
            prpLCompensateVo.setSumRealPay(BigDecimal.ZERO);//总实赔金额 TODO
            prpLCompensateVo.setAllLossFlag(checkVo.getLossType());//是否全损-取值查勘
            prpLCompensateVo.setRecoveryFlag(CodeConstants.ValidFlag.INVALID);//是否发起追偿 TODO 默认否
            prpLCompensateVo.setLawsuitFlag("0");//是否诉讼 TODO 默认否
            prpLCompensateVo.setAllowFlag("0");//是否通融 TODO 默认否
            prpLCompensateVo.setCreateUser(userVo.getUserCode());//理算人
            prpLCompensateVo.setUnderwriteFlag(CodeConstants.UnderWriteFlag.NORMAL);//核赔状态
            prpLCompensateVo.setCreateTime(new Date());
            prpLCompensateVo.setUpdateTime(new Date());
            prpLCompensateVo.setCompensateKind(CodeConstants.CompensateKind.BI_COMPENSATE_CHARGE);// 计算书类型1:商业实赔（车险）2.交强实赔（车险） 3:商业费用赔款计算书(车险)4:交强费用赔款计算书(车险)
            if (CodeConstants.PolicyType.POLICY_DZA.equals(policyType)){//交强
                prpLCompensateVo.setCompensateKind(CodeConstants.CompensateKind.CI_COMPENSATE_CHARGE);
            }
            prpLCompensateVo.setTimes(1);//次数
            prpLCompensateVo.setCurrency("CNY");
            prpLCompensateVo.setDeductType(CodeConstants.DeductType.DEDUCT_NONE);//判断扣除类型 TODO 先默认
            prpLCompensateVo.setHandler1Code(userVo.getUserCode());
            prpLCompensateVo.setRemark(applyReason);//备注

            //保存理算主表信息
            PrpLCompensateVo prpLCompensate = compensateHandleService.saveCompensateEdit(prpLWfTaskVo.getTaskId().doubleValue(), prpLCompensateVo,
                    new ArrayList<PrpLLossItemVo>(), new ArrayList<PrpLLossPropVo>(), new ArrayList<PrpLLossPropVo>(),
                    new ArrayList<PrpLLossPersonVo>(), new ArrayList<PrpLChargeVo>(), new ArrayList<PrpLPaymentVo>(),
                    new ArrayList<PrpLClaimDeductVo>(), new ArrayList<PrpLPlatLockVo>(),userVo,new ArrayList<PrpLCheckDutyVo>());
            prpLCompensateVo.setCompensateNo(prpLCompensate.getCompensateNo());
            //提交理算流程
            WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
            nextVo.setSubmitLevel(0);
            nextVo.setTaskInUser(userVo.getUserCode());
            String nextNode = null;
            if ("CompeCI".equals(prpLWfTaskVo.getSubNodeCode())) {
                nextNode = FlowNode.VClaim_CI_LV0.toString();
            } else {
                nextNode = FlowNode.VClaim_BI_LV0.toString();
            }
            nextVo.setFlowTaskId(prpLWfTaskVo.getTaskId());
            nextVo.setComCode(userVo.getComCode());
            nextVo.setFlowId(prpLWfTaskVo.getFlowId());
            nextVo.setTaskInUser(userVo.getUserCode());
            nextVo.setCurrentNode(FlowNode.valueOf(prpLWfTaskVo.getSubNodeCode()));
            nextVo.setNextNode(FlowNode.valueOf(nextNode));
            nextVo.setTaskInKey(prpLCompensate.getCompensateNo());
            nextVo.setAssignUser(userVo.getUserCode());
            nextVo.setAssignCom(reClaimVo.getComCode());
            nextVo.setComCode(reClaimVo.getComCode());
            PrpLWfTaskVo wfTaskVo = wfTaskHandleService.submitCompe(prpLCompensateVo, nextVo);
            // 自动核赔标识为true，理算提交后执行自动核赔
            try {
                verifyClaimService.autoVerifyClaimEndCaseForPingAn(userVo,prpLCompensateVo);
            }catch (Exception e){
                logger.info("核赔提交结案报错================================", e);
                wfTaskHandleService.rollBackTask(wfTaskVo);//核赔报错回滚理算工作流
                throw new IllegalArgumentException("理算核赔提交失败！", e);
            }

            //自动结案
            autoEndCase(registNo,policyNo,endCaseDate);
        }
    }

    /**
     * 结案送再保
     * @param claimNo 立案号
     * @author mfn 2019-11-18 18:19:23
     */
    private void sendEndCaseToReins(String claimNo) {
        // 再保处理结案业务 niuqiang businessType=0
        try {
            if (claimNo != null) {
                PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
                if (claimVo != null) {
                    ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
                    claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
                    claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
                    claimInterfaceLogVo.setComCode(claimVo.getComCode());
                    claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
                    claimInterfaceLogVo.setCreateTime(new Date());
                    claimInterfaceLogVo.setOperateNode(FlowNode.EndCas.getName());
                    interfaceAsyncService.TransDataForReinsCaseVo("0", claimVo, claimInterfaceLogVo);
                }
            }
        } catch (Exception e) {
            logger.info("结案送再保失败！", e);
        }
    }

    /**
     * 校验数据是否合法
     * @param registNo
     * @param endCaseRespData
     */
    private void checkData(String registNo,EndCaseRespData endCaseRespData) {
        if (StringUtils.isBlank(registNo)){
            throw new IllegalArgumentException("报案号registNo不存在");
        }
        if (endCaseRespData == null){
            throw new IllegalArgumentException("解析返回报文内容为空");
        }
        if (endCaseRespData.getWholeCaseBaseDTO() == null){
            throw new IllegalArgumentException("整案基本信息wholeCaseBaseDTO为空");
        }
        if (StringUtils.isBlank(endCaseRespData.getWholeCaseBaseDTO().getReportNo())){
            throw new IllegalArgumentException("案件号reportNo为空");
        }
        if (StringUtils.isBlank(endCaseRespData.getWholeCaseBaseDTO().getIndemnityConclusion())){
            throw new IllegalArgumentException("赔付结论indemnityConclusion为空");
        }
        if (endCaseRespData.getWholeCaseBaseDTO().getEndCaseDate() == null){
            throw new IllegalArgumentException("结案时间endCaseDate为空");
        }
    }
}

