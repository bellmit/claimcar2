package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersInjuredVo;
import ins.sino.claimcar.padpay.po.PrpLPadPayMain;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pinganUnion.service.PingAnHandleService;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.padpay.UnionPadPayAdvancePersonDto;
import ins.sino.claimcar.pinganunion.vo.padpay.UnionPadPayRequestParamDto;
import ins.sino.claimcar.pinganunion.vo.padpay.UnionPadPayResponseDataDto;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 平安联盟中心-垫付信息查询接口-业务处理
 *
 * @author mfn
 * @date 2020/7/23 18:12
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, group = "pingAnPadPayHandleService")
@Path("pingAnPadPayHandleService")
public class PingAnPadPayHandleServiceImpl implements PingAnHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnPrePayHandleServiceImpl.class);

    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    private ClaimService claimService;
    @Autowired
    PadPayService padPayService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    private BillNoService billNoService;
    @Autowired
    PingAnDictService pingAnDictService;
    @Autowired
    PersTraceDubboService persTraceDubboService;

    /**
     * 接口具体业务处理方法
     *
     * @param respData 平安接口返回数据
     * @return 数据处理结果
     */
    @Override
    public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
        logger.info("报案号：" + registNo + " 请求参数：" + pingAnDataNoticeVo.getParamObj() + " 平安返回的垫付数据：" + respData);
        ResultBean resultBean = ResultBean.success();

        if (null != respData) {
            // 预付接口返回数据结果
            UnionPadPayResponseDataDto dataDto;
            List<UnionPadPayAdvancePersonDto> personDtoList;
            UnionPadPayRequestParamDto requestParamDto;
            try {
                Gson gson = new Gson();
                requestParamDto = gson.fromJson(pingAnDataNoticeVo.getParamObj(), UnionPadPayRequestParamDto.class);
                dataDto = gson.fromJson(respData, UnionPadPayResponseDataDto.class);
                if (null != dataDto) {
                    String riskCode = Risk.DQZ;
                    String policyNo = null;
                    Date date = new Date();
                    personDtoList = dataDto.getAdvancePersonList();
                    // 组织业务数据
                    PrpLPadPayMainVo padPayMainVo = new PrpLPadPayMainVo();
                    String padPayNo = billNoService.getPadPayNo(policyViewService.getPolicyComCode(registNo), Risk.DQZ);
                    // 鼎和报案保单信息
                    List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
                    for (PrpLCMainVo cmainVo : prpLCMainVoList) {
                        if (Risk.DQZ.equals(cmainVo.getRiskCode())) {
                            policyNo = cmainVo.getPolicyNo();
                            break;
                        }
                    }
                    if (null == policyNo) {
                        throw new IllegalArgumentException("报案号：" + registNo + " 无法找到对应的交强险保单！");
                    }
                    List<PrpLClaimVo> claimVoList = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, policyNo, "1");
                    PrpLClaimVo claimVo = new PrpLClaimVo();
                    if (claimVoList != null && claimVoList.size() > 0) {
                        claimVo = claimVoList.get(0);
                    }
                    String claimNo = claimVo.getClaimNo();
                    padPayMainVo.setCompensateNo(padPayNo);
                    padPayMainVo.setRegistNo(registNo);
                    padPayMainVo.setPolicyNo(policyNo);
                    padPayMainVo.setClaimNo(claimNo);
                    padPayMainVo.setNoticeDate(date);
                    padPayMainVo.setValidFlag("1");
                    padPayMainVo.setFlag("submit");
                    padPayMainVo.setRemark("平安联盟中心推送数据无此字段信息！");
                    padPayMainVo.setCreateUser("AUTO");
                    padPayMainVo.setCreateTime(date);
                    padPayMainVo.setUpdateUser("AUTO");
                    padPayMainVo.setUpdateTime(date);
                    padPayMainVo.setUnderwriteFlag(CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE);
                    padPayMainVo.setUnderwriteUser("AUTO");
                    padPayMainVo.setUnderwriteDate(date);
                    padPayMainVo.setComCode(claimVo.getComCode());
                    padPayMainVo.setRescueReport("平安联盟中心推送数据无此字段信息！");
                    padPayMainVo.setIsAutoPay("0");
                    padPayMainVo.setIsFastReparation("0");
                    padPayMainVo.setCaseTimes(dataDto.getCaseTimes());
                    padPayMainVo.setAdvanceTimes(dataDto.getAdvanceTimes());


                    List<PrpLPadPayPersonVo> personVos = new ArrayList<PrpLPadPayPersonVo>();
                    for (UnionPadPayAdvancePersonDto personDto : personDtoList) {
                        PrpLPadPayPersonVo personVo = new PrpLPadPayPersonVo();
                        List<PrpLDlossPersInjuredVo> dlossPersInjuredVos = persTraceDubboService.findPersInjuredByRegistNo(registNo);
                        if(dlossPersInjuredVos != null && !dlossPersInjuredVos.isEmpty()){
                            for (PrpLDlossPersInjuredVo dlossPersInjuredVo : dlossPersInjuredVos) {
                                //对比表中得数据取出对应得ID值存入到垫付人信息表中
                                if(personDto.getIdClmChannelProcess().equals(dlossPersInjuredVo.getIdClmChannelProcess())){
                                    if(dlossPersInjuredVo.getId() != null){
                                        personVo.setPersonName(String.valueOf(dlossPersInjuredVo.getId()));
                                    }
                                    // 平安推送数据没有这个字段，0-未知
                                    if(dlossPersInjuredVo.getPersonAge() != null){
                                        personVo.setPersonAge(dlossPersInjuredVo.getPersonAge().intValue());
                                    }
                                    personVo.setPersonSex(dlossPersInjuredVo.getPersonSex());
                                    personVo.setInjuryType("1");
                                    personVo.setLicenseNo("0");
                                    personVo.setRiskCode(Risk.DQZ);
                                    personVo.setCostSum(personDto.getTreatSum());
                                    personVo.setOtherFlag("1");
                                    personVo.setOtherCause("19");
                                    personVo.setFeeNameCode("1");
                                    personVo.setSerialNo("0");
                                    personVos.add(personVo);
                                    break;
                                }
                            }
                        }

                    }
                    padPayMainVo.setPrpLPadPayPersons(personVos);

                    PrpLPadPayMainVo padMainVo = padPayService.save(padPayMainVo, "AUTO", claimVo.getComCode());
                    PrpLWfTaskVo wfTaskVo = this.LaunchPadPayTask(registNo, claimNo);
                    String taskid = wfTaskVo.getTaskId().toString();
                    // 把垫付任务变为正在处理
                    if (StringUtils.isNotBlank(taskid)) {
                        String compeNo = padMainVo.getCompensateNo();
                        wfTaskHandleService.tempSaveTask(Double.parseDouble(taskid), compeNo, "AUTO", claimVo.getComCode());
                    }

                    // 返回核赔节点的工作流对象
                    wfTaskVo = this.submitPadPay(taskid, padMainVo.getId(), claimVo.getComCode());
                    // 审核通过
                    this.submitVclaimTask(padMainVo, wfTaskVo);

                }
            } catch (Exception e) {
                logger.info("平安联盟-垫付查询接口数据结果处理异常！", e);
                resultBean.fail("平安联盟-垫付付查询接口数据结果处理异常！");
            }
        } else {
            resultBean.fail("平安联盟-垫付付查询接口数据结果为空！");
        }

        return resultBean;
    }

    /**
     * 生成垫付节点数据
     *
     * @param registNo 报案号
     * @param claimNo  立案号
     * @return 工作流主键
     */
    private PrpLWfTaskVo LaunchPadPayTask(String registNo, String claimNo) throws Exception {

        try {
            PrpLClaimVo claimVo = claimService.findByClaimNo(claimNo);
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            submitVo.setCurrentNode(FlowNode.ClaimCI);
            submitVo.setNextNode(FlowNode.PadPay);
            // 查询立案已处理
            List<PrpLWfTaskVo> wfTaskVos = null;
            wfTaskVos = wfTaskHandleService.findEndTask(registNo, claimNo, FlowNode.ClaimCI);
            if (wfTaskVos != null && wfTaskVos.size() > 0) {
                submitVo.setFlowId(wfTaskVos.get(0).getFlowId());
                submitVo.setFlowTaskId(wfTaskVos.get(0).getTaskId());
            }
            submitVo.setComCode(policyViewService.getPolicyComCode(claimVo.getRegistNo()));
            submitVo.setTaskInUser("AUTO");
            submitVo.setTaskInKey(claimNo);
            submitVo.setAssignCom(policyViewService.getPolicyComCode(registNo));

            // 垫付提交
            return wfTaskHandleService.addPrePayTask(claimVo, submitVo);
        } catch (Exception e) {
            logger.info("平安联盟-垫付节点生成异常！", e);
            throw e;
        }
    }

    /**
     * 垫付任务提交
     *
     * @param taskId
     * @param padId
     * @param nextComCode
     * @throws Exception
     */
    private PrpLWfTaskVo submitPadPay(String taskId, Long padId, String nextComCode) throws Exception {
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.queryTask(Double.parseDouble(taskId));
        PrpLPadPayMain padPayPo = databaseDao.findByPK(PrpLPadPayMain.class, padId);
        PrpLPadPayMainVo padPayVo = Beans.copyDepth().from(padPayPo).to(PrpLPadPayMainVo.class);
        String registNo = padPayVo.getRegistNo();

        WfTaskSubmitVo submitVo = new WfTaskSubmitVo();

        submitVo.setFlowId(wfTaskVo.getFlowId());
        submitVo.setFlowTaskId(new BigDecimal(taskId));
        submitVo.setComCode(policyViewService.getPolicyComCode(registNo));
        submitVo.setTaskInKey(padPayVo.getClaimNo());
        submitVo.setTaskInUser("AUTO");

        submitVo.setCurrentNode(FlowNode.PadPay);
        submitVo.setNextNode(FlowNode.VClaim_CI_LV1);

        PrpLWfTaskVo oldTaskVo = findTaskIn(registNo, padPayVo.getCompensateNo());
        if (oldTaskVo != null && FlowNode.PadPay.name().equals(oldTaskVo.getYwTaskType())) {
            submitVo.setAssignCom(oldTaskVo.getHandlerCom());
            submitVo.setAssignUser("AUTO");
        } else {
            submitVo.setAssignCom(nextComCode);
            submitVo.setAssignUser("AUTO");
        }
        // 返回核赔节点的工作流对象
        return wfTaskHandleService.submitPadpay(padPayVo, submitVo);
    }

    /**
     * 查找处理中的任务
     *
     * @param registNo    报案号
     * @param handleIdKey 业务号
     * @return 工作流数据
     * @throws Exception 处理异常
     */
    private PrpLWfTaskVo findTaskIn(String registNo, String handleIdKey) throws Exception {
        PrpLWfTaskVo oldTaskVo = null;
        List<PrpLWfTaskVo> endTaskVoList = wfTaskHandleService.findEndTask(registNo, handleIdKey, FlowNode.VClaim_CI_LV1);
        if (endTaskVoList == null || endTaskVoList.size() == 0) {
            endTaskVoList = wfTaskHandleService.findEndTask(registNo, handleIdKey, FlowNode.VClaim_CI_LV1);
        }
        if (endTaskVoList != null && endTaskVoList.size() > 0) {
            oldTaskVo = endTaskVoList.get(0);
        }
        return oldTaskVo;
    }

    /**
     * 垫付审核通过
     *
     * @param padPayMainVo 垫付主表对象
     * @param wfTaskVo     工作流表数据
     * @throws Exception 数据处理异常
     */
    private void submitVclaimTask(PrpLPadPayMainVo padPayMainVo, PrpLWfTaskVo wfTaskVo) throws Exception {
        try {
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setComCode(padPayMainVo.getComCode());
            submitVo.setTaskInUser("AUTO");
            submitVo.setTaskInKey(padPayMainVo.getCompensateNo());
            submitVo.setHandleIdKey(padPayMainVo.getClaimNo());
            submitVo.setAssignUser("AUTO");
            submitVo.setAssignCom(padPayMainVo.getComCode());
            submitVo.setCurrentNode(FlowNode.VClaim);
            submitVo.setNextNode(FlowNode.END);
            WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
            taskVo.setRegistNo(padPayMainVo.getRegistNo());
            taskVo.setHandlerIdKey(padPayMainVo.getCompensateNo());
            taskVo.setItemName("预付核赔");
            taskVo.setClaimNo(padPayMainVo.getClaimNo());
            wfTaskHandleService.submitSimpleTask(taskVo, submitVo);
        } catch (Exception e) {
            logger.info("平安联盟-业务号：{} 预付提交异常！{}", padPayMainVo.getCompensateNo(), e);
            throw e;
        }
    }
}
