package ins.sino.claimcar.schedule.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrpsmsMessageVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropLossHandleService;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleHandlerService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTasklogVo;
import ins.sino.claimcar.sms.service.SmsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path(value = "scheduleHandlerService")
public class ScheduleHandlerServiceImpl implements ScheduleHandlerService {
    private static Logger logger = LoggerFactory.getLogger(ScheduleHandlerServiceImpl.class);

    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    ManagerService managerService;
    @Autowired
    SendMsgService sendMsgService;
    @Autowired
    SmsService smsService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    RegistService registService;
    @Autowired
    CodeTranService codeTranService;
    @Autowired
    RepairFactoryService repairFactoryService;
    @Autowired
    DeflossHandleService deflossHandleService;
    @Autowired
    PropLossHandleService propLossHandleService;

    @Autowired
    MsgModelService msgModelService;
    @Autowired
    PropLossService propLossService;
    @Autowired
    LossCarService lossCarService;
    @Autowired
    PersTraceService persTraceService;
    @Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    PolicyQueryService policyQueryService;
    @Autowired
    PrpLCMainService prpLCMainService;

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#saveScheduleItemTask(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, java.util.List, ins.sino.claimcar.flow.vo.WfTaskSubmitVo)
     */
    @Override
    public void saveScheduleItemTask(PrpLScheduleTaskVo prpLScheduleTaskVo, List<PrpLScheduleItemsVo> prpLScheduleItemses, WfTaskSubmitVo submitVo) {
        //??????????????????
        PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        //??????????????????tasklist
        List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();

        //??????????????????
        PrpLScheduleTasklogVo personScheduleTasklogVo = new PrpLScheduleTasklogVo();
        //?????????????????????
        PrpLScheduleTasklogVo otherScheduleTasklogVo = new PrpLScheduleTasklogVo();

        //????????????????????????
        PrpLScheduleTaskVo otherScheduleTaskVo = Beans.copyDepth().from(prpLScheduleTaskVo).to(PrpLScheduleTaskVo.class);
        //???id??????
        otherScheduleTaskVo.setId(null);
        //???????????????????????????????????????1????????????????????????????????????????????????????????????validFlag???0
        otherScheduleTaskVo.setValidFlag("1");
        //?????????????????????
        otherScheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.CHECK_SCHEDULE);
        //????????????????????????
        otherScheduleTaskVo.setIsPersonFlag("0");
        if (StringUtils.isNotBlank(ServiceUserUtils.getUserCode())) {
            otherScheduleTaskVo.setCreateUser(ServiceUserUtils.getUserCode());
            otherScheduleTaskVo.setUpdateUser(ServiceUserUtils.getUserCode());
            otherScheduleTaskVo.setOperatorCode(ServiceUserUtils.getUserCode());
            otherScheduleTaskVo.setOperatorName(ServiceUserUtils.getUserName());
        } else {
            // ???????????????????????????????????????????????????????????????????????????
            otherScheduleTaskVo.setCreateUser(registVo.getCreateUser());
            otherScheduleTaskVo.setUpdateUser(registVo.getCreateUser());
            otherScheduleTaskVo.setOperatorCode(registVo.getCreateUser());
            otherScheduleTaskVo.setOperatorName(CodeTranUtil.transCode("UserCode", registVo.getCreateUser()));
        }
        otherScheduleTaskVo.setCreateTime(new Date());
        otherScheduleTaskVo.setUpdateTime(new Date());
        otherScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.CHECK_SCHEDULED);
        if ("1".equals(registVo.getIsQuickCase())) {
            otherScheduleTaskVo.setCreateUser("AUTO");
            otherScheduleTaskVo.setUpdateUser("AUTO");
            otherScheduleTaskVo.setOperatorCode("AUTO");
            otherScheduleTaskVo.setOperatorName("AUTO");
        }
        //???????????????????????????????????????1???????????????????????????
        if ("1".equals(registVo.getSelfRegistFlag())) {
            otherScheduleTaskVo.setCreateUser("AUTO");
            otherScheduleTaskVo.setUpdateUser("AUTO");
            otherScheduleTaskVo.setOperatorCode("AUTO");
            otherScheduleTaskVo.setOperatorName("AUTO");
        }
//		otherScheduleTaskVo.setScheduledTime(new Date());
        //??????????????????????????????????????????
        otherScheduleTasklogVo = Beans.copyDepth().from(otherScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
        otherScheduleTasklogVo.setScheduledTime(new Date());

        //?????????????????????
        PrpLScheduleTaskVo personScheduleTaskVo = Beans.copyDepth().from(otherScheduleTaskVo).to(PrpLScheduleTaskVo.class);
        //?????????????????????
        personScheduleTaskVo.setIsPersonFlag("1");
        //????????????item
        List<PrpLScheduleItemsVo> personScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();
        //????????????item
        List<PrpLScheduleItemsVo> otherScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();

        //??????prpLScheduleItemses??????item?????????????????????????????????????????????????????????????????????????????????
        if (prpLScheduleItemses != null && prpLScheduleItemses.size() > 0) {
            for (PrpLScheduleItemsVo vo : prpLScheduleItemses) {
                //???????????????????????????????????????
                if (StringUtils.isNotBlank(ServiceUserUtils.getUserCode())) {
                    vo.setAddoperatorCode(ServiceUserUtils.getUserCode());
                    vo.setUpdateUser(ServiceUserUtils.getUserCode());
                } else {
                    vo.setUpdateUser(vo.getUpdateUser());
                    vo.setAddoperatorCode(vo.getAddoperatorCode());
                }
                //??????????????????????????????
                vo.setUpdateTime(new Date());
                vo.setRegistNo(prpLScheduleTaskVo.getRegistNo());
                if ("1".equals(registVo.getIsQuickCase())) {
                    vo.setAddoperatorCode("AUTO");
                    vo.setUpdateUser("AUTO");
                }
                
                if ("1".equals(registVo.getSelfClaimFlag())) {
                    vo.setAddoperatorCode("AUTO");
                    vo.setUpdateUser("AUTO");
                }
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isBlank(vo.getCreateUser())) {
                    vo.setCreateUser(ServiceUserUtils.getUserCode());
                    vo.setCreateTime(new Date());
                }
                if(vo.getCreateTime()==null) {
                	vo.setCreateTime(new Date());
                }
                if (StringUtils.equals(vo.getItemType(), "4")) {//itemtype???4????????????vo????????????itemList???
                    vo.setItemsName("??????");
                    personScheduleTaskVo.setLossContent(vo.getItemsContent() + " ??? " + vo.getItemRemark() + " ???");

                    if (!StringUtils.equals(vo.getScheduleStatus(), "8")) {//itemtype???8??????????????????vo??????
                        //????????????????????????
                        vo.setScheduleStatus(personScheduleTaskVo.getScheduleStatus());
                        //??????????????????
                        vo.setScheduledUsercode(personScheduleTaskVo.getPersonScheduledUsercode());
                        vo.setScheduledComcode(personScheduleTaskVo.getPersonScheduledComcode());
                        personScheduleTaskVo.setScheduledUsercode(personScheduleTaskVo.getPersonScheduledUsercode());
                        personScheduleTaskVo.setScheduledComcode(personScheduleTaskVo.getPersonScheduledComcode());
                        personScheduleTaskVo.setScheduledUsername(personScheduleTaskVo.getPersonScheduledUsername());
                        personScheduleTaskVo.setScheduledComname(personScheduleTaskVo.getPersonScheduledComname());
                    }

                    personScheduleItemses.add(vo);
                } else {//????????????otherItemList???
                    if (!StringUtils.equals(vo.getScheduleStatus(), "8")) {//itemtype???8??????????????????vo??????
                        //????????????????????????
                        vo.setScheduleStatus(otherScheduleTaskVo.getScheduleStatus());
                        //??????????????????
                        vo.setScheduledUsercode(otherScheduleTaskVo.getScheduledUsercode());
                        vo.setScheduledComcode(otherScheduleTaskVo.getScheduledComcode());
                        StringBuffer str = new StringBuffer("");
                        if (!StringUtils.isEmpty(otherScheduleTaskVo.getLossContent())) {
                            str = str.append(otherScheduleTaskVo.getLossContent());
                            str = str.append(",");
                        }
                        otherScheduleTaskVo.setLossContent(str.toString() + vo.getItemsContent());
                    }

                    otherScheduleItemses.add(vo);
                }
            }
        }

        //size()>0???????????????????????????????????????items?????????????????????????????????
        if (personScheduleItemses.size() > 0) {
            //???????????????????????????????????????
            personScheduleTasklogVo = Beans.copyDepth().from(personScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
            List<PrpLScheduleTasklogVo> personTaskLogVoList = new ArrayList<PrpLScheduleTasklogVo>();
            personTaskLogVoList.add(personScheduleTasklogVo);
            personScheduleTaskVo.setPrpLScheduleItemses(personScheduleItemses);
            personScheduleTaskVo.setPrpLScheduleTasklogs(personTaskLogVoList);
            scheduleTaskVoList.add(scheduleTaskService.saveScheduleTaskByVo(personScheduleTaskVo));
        }

        //size()>0????????????????????????????????????items??????????????????????????????????????????????????????????????????????????????????????????
        if (otherScheduleItemses.size() > 0) {
            otherScheduleTasklogVo = Beans.copyDepth().from(otherScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
            List<PrpLScheduleTasklogVo> otherTaskLogVoList = new ArrayList<PrpLScheduleTasklogVo>();
            otherTaskLogVoList.add(otherScheduleTasklogVo);
            otherScheduleTaskVo.setPrpLScheduleItemses(otherScheduleItemses);
            otherScheduleTaskVo.setPrpLScheduleTasklogs(otherTaskLogVoList);
            scheduleTaskVoList.add(scheduleTaskService.saveScheduleTaskByVo(otherScheduleTaskVo));
        }
		
		/*if (!StringUtils.equals(prpLScheduleTaskVo.getScheduleStatus(), CodeConstants.ScheduleStatus.SCHEDULED_FINISH)) {
			prpLScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_FINISH);
			prpLScheduleTaskVo.setPrpLScheduleItemses(null);
			prpLScheduleTaskVo.setPrpLScheduleDefLosses(null);
			prpLScheduleTaskVo.setPrpLScheduleTasklogs(null);
			scheduleTaskService.saveScheduleTaskByVo(prpLScheduleTaskVo);
		}*/

        wfTaskHandleService.submitSchedule(registVo, scheduleTaskVoList, submitVo);
    }

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#updateScheduleStatus(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo)
     */
    @Override
    public void updateScheduleStatus(PrpLScheduleTaskVo prpLScheduleTaskVo) {

        //?????????????????????0-???????????????????????????????????????????????????1-????????????
        if (StringUtils.equals(prpLScheduleTaskVo.getScheduleStatus(), CodeConstants.ScheduleStatus.NOT_SCHEDULED)) {
            //???????????????????????????????????????????????????????????????
            prpLScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULING);

            //???????????????????????????????????????
            List<PrpLScheduleItemsVo> itemVoList = prpLScheduleTaskVo.getPrpLScheduleItemses();
            //????????????????????????????????????
            for (PrpLScheduleItemsVo vo : itemVoList) {
                vo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULING);
            }

            //???????????????????????????????????????
            List<PrpLScheduleDefLossVo> defLossVoList = prpLScheduleTaskVo.getPrpLScheduleDefLosses();
            //????????????????????????????????????
            for (PrpLScheduleDefLossVo vo : defLossVoList) {
                vo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULING);
            }

            scheduleTaskService.saveScheduleTaskByVo(prpLScheduleTaskVo);
        }
    }

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#saveScheduleDefLossTask(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, java.util.List, ins.sino.claimcar.flow.vo.WfTaskSubmitVo)
     */
    @Override
    public List<PrpLScheduleTaskVo> saveScheduleDefLossTask(PrpLScheduleTaskVo prpLScheduleTaskVo,
                                                            List<PrpLScheduleDefLossVo> prpLScheduleDefLosses, WfTaskSubmitVo submitVo) {
        Date nowDate = new Date();
        String userCode = ServiceUserUtils.getUserCode();
        //??????????????????
        PrpLRegistVo registVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        //??????????????????tasklist
        List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();

        //??????????????????
        PrpLScheduleTaskVo newScheduleTaskVo = Beans.copyDepth().from(prpLScheduleTaskVo).to(PrpLScheduleTaskVo.class);
        //???id??????
        newScheduleTaskVo.setId(null);
        //???????????????????????????????????????1????????????????????????????????????????????????????????????validFlag???0
        newScheduleTaskVo.setValidFlag("1");
        //???????????????????????????
        //newScheduleTaskVo.setScheduleStatus("5");
        //?????????????????????
        newScheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
        //????????????????????????
        newScheduleTaskVo.setIsPersonFlag("0");
        newScheduleTaskVo.setScheduledTime(nowDate);
        newScheduleTaskVo.setCreateUser(userCode);
        newScheduleTaskVo.setCreateTime(nowDate);
        newScheduleTaskVo.setUpdateUser(userCode);
        newScheduleTaskVo.setUpdateTime(nowDate);

        //????????????
        PrpLScheduleTasklogVo scheduleTasklogVo = Beans.copyDepth().from(newScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
        scheduleTasklogVo.setScheduledTime(nowDate);
        List<PrpLScheduleTasklogVo> scheduleTasklogVoList = new ArrayList<PrpLScheduleTasklogVo>();
        scheduleTasklogVoList.add(scheduleTasklogVo);

        //??????????????????????????????????????????
        List<PrpLScheduleDefLossVo> submitScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();

        //??????prpLScheduleDefLosses?????????????????????
        if (prpLScheduleDefLosses != null && prpLScheduleDefLosses.size() > 0) {
            for (PrpLScheduleDefLossVo vo : prpLScheduleDefLosses) {
                if (StringUtils.equals(vo.getScheduleFlag(), "1")) {
                    //????????????????????????
                    vo.setScheduleStatus(newScheduleTaskVo.getScheduleStatus());
                    //??????????????????
                    vo.setSourceFlag(CodeConstants.ScheduleDefSource.SCHEDULEDEF);
                    //???????????????????????????????????????
                    vo.setAddoperatorCode(userCode);
                    vo.setScheduledUsercode(prpLScheduleTaskVo.getScheduledUsercode());
                    vo.setScheduledComcode(prpLScheduleTaskVo.getScheduledComcode());
                    vo.setLicenseNo(vo.getItemsContent());
                    //??????????????????????????????
                    vo.setUpdateUser(userCode);
                    vo.setUpdateTime(nowDate);
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (StringUtils.isBlank(vo.getCreateUser())) {
                        vo.setCreateUser(userCode);
                        vo.setCreateTime(nowDate);
                    }
                    StringBuffer str = new StringBuffer("");
                    if (!StringUtils.isEmpty(scheduleTasklogVo.getLossContent())) {
                        str = str.append(scheduleTasklogVo.getLossContent());
                        str = str.append(",");
                    }
                    scheduleTasklogVo.setLossContent(str.toString() + vo.getItemsContent());
                    scheduleTasklogVo.setScheduledComcode(prpLScheduleTaskVo.getScheduledComcode());
                    scheduleTasklogVo.setScheduledComname(prpLScheduleTaskVo.getScheduledComname());
                    submitScheduleDefLosses.add(vo);
                }
            }
        }

        if (submitScheduleDefLosses.size() > 0) {
            newScheduleTaskVo.setPrpLScheduleDefLosses(submitScheduleDefLosses);
            newScheduleTaskVo.setPrpLScheduleTasklogs(scheduleTasklogVoList);
            scheduleTaskVoList.add(scheduleTaskService.saveScheduleTaskByVo(newScheduleTaskVo));
            wfTaskHandleService.submitSchedule(registVo, scheduleTaskVoList, submitVo);
            return scheduleTaskVoList;
        } else {
            throw new IllegalArgumentException("??????????????????????????????");
        }
    }

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#scheduleCancel(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, ins.sino.claimcar.flow.vo.WfTaskSubmitVo, java.util.List, java.util.List)
     */
    @Override
    public String scheduleCancel(PrpLScheduleTaskVo newScheduleTaskVo, WfTaskSubmitVo submitVo, Map<Long, BigDecimal> idMap, List<PrpLWfTaskVo> prpLWfTaskInVos) {
        // TODO Auto-generated method stub
        //?????????PrpLScheduleTaskVo
        Date nowDate = new Date();
        newScheduleTaskVo.setId(null);//id?????????
        newScheduleTaskVo.setCancelOrReassinModifyTime(nowDate);
        newScheduleTaskVo.setIsPersonFlag("0");//?????????????????????
        newScheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
        newScheduleTaskVo.setCreateUser(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setCreateTime(nowDate);
        newScheduleTaskVo.setUpdateUser(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setUpdateTime(nowDate);
        newScheduleTaskVo.setOperatorCode(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_CANCEL);
        newScheduleTaskVo.setScheduledTime(new Date());

        //??????????????????????????????id??????
        List<BigDecimal> flowTaskIds = new ArrayList<BigDecimal>();
        //???????????????????????????id??????
        List<Long> toUpdateIdList = new ArrayList<Long>();
        int index = 0;
        //???????????????????????????????????????taskIdList???????????????????????????????????????????????????????????????????????????
        for (Long key : idMap.keySet()) {
            for (PrpLWfTaskVo vo : prpLWfTaskInVos) {
                if (String.valueOf(vo.getTaskId()).equals(String.valueOf(idMap.get(key)))) {
                    toUpdateIdList.add(key);
                    flowTaskIds.add(vo.getTaskId());
                    index++;
                    break;
                }
            }
        }
        //?????????true????????????????????????????????????????????????
        if (idMap.size() > index) {
            return "2";
        }
        //??????????????????????????????????????????????????????????????????????????????????????????
        List<PrpLScheduleDefLossVo> defLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(newScheduleTaskVo.getRegistNo());
        List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(newScheduleTaskVo.getRegistNo());
        List<PrpLCompensateVo> compensateVoList = compensateTaskService.queryCompensateByOther(newScheduleTaskVo.getRegistNo(), "Y", null, null);
        List<PrpLPrePayVo> prePayVoList = new ArrayList<PrpLPrePayVo>();
        for (PrpLCompensateVo compensateVo : compensateVoList) {
            if (!"7".equals(compensateVo.getUnderwriteFlag())) {
                List<PrpLPrePayVo> resultVoList = compensateTaskService.getPrePayVo(compensateVo.getCompensateNo(), "P");
                if (resultVoList != null && resultVoList.size() > 0) {
                    prePayVoList.addAll(resultVoList);
                }
            }
        }
        for (PrpLWfTaskVo taskVo : prpLWfTaskInVos) {
            Integer serialNo = null;
            if (FlowNode.DLCar.name().equals(taskVo.getSubNodeCode()) || FlowNode.DLCarMod.name().equals(taskVo.getSubNodeCode())) {
                if ("0".equals(taskVo.getWorkStatus()) && "DLCar".equals(taskVo.getSubNodeCode())) {
                    PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(Long.valueOf(taskVo.getHandlerIdKey()));
                    serialNo = scheduleDefLossVo.getSerialNo();
                } else if (taskVo.getSubNodeCode().startsWith("DLCar")) {
                    PrpLDlossCarMainVo carVo = lossCarService.findLossCarMainById(Long.valueOf(taskVo.getHandlerIdKey()));
                    serialNo = carVo.getSerialNo();
                    if (!"00".equals(carVo.getLossState())) {
                        return "4";
                    }
                }
                //????????????
                String lossType = "";
                if (taskVo.getSubNodeCode().startsWith("DLCar")) {
                    lossType = "car";
                } else {
                    lossType = "prop";
                }
                for (PrpLPrePayVo prePayVo : prePayVoList) {
                    if (prePayVo.getLossType() != null && prePayVo.getLossType().startsWith(lossType) &&
                            prePayVo.getLossType().contains(taskVo.getHandlerIdKey())) {
                        return "5";
                    }
                }
                //??????
                for (PrpLScheduleDefLossVo defLossVo : defLossVoList) {
                    PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findPrpLScheduleTaskVoById(defLossVo.getScheduleTaskId());
                    if ("2".equals(defLossVo.getDeflossType()) &&
                            defLossVo.getSerialNo() == serialNo &&
                            "1".equals(scheduleTaskVo.getValidFlag())) {
                        return "3";
                    }
                }
                //??????
                if (persTraceVoList != null && persTraceVoList.size() > 0) {
                    for (PrpLDlossPersTraceVo vo : persTraceVoList) {
                        if ("1".equals(vo.getValidFlag()) && vo.getPrpLDlossPersInjured().getSerialNo() == serialNo) {
                            return "3";
                        }
                    }
                }
            }
        }

        //?????????????????????????????????????????????????????????????????????
        for (PrpLWfTaskVo taskVo : prpLWfTaskInVos) {
            if ((!"0".equals(taskVo.getWorkStatus()) && FlowNode.DLCar.equals(taskVo.getSubNodeCode())) || FlowNode.DLCarMod.name().equals(taskVo.getSubNodeCode())) {
                deflossHandleService.cancelCar(taskVo.getHandlerIdKey());
            } else if ((!"0".equals(taskVo.getWorkStatus()) && FlowNode.DLProp.equals(taskVo.getSubNodeCode())) || FlowNode.DLPropMod.name().equals(taskVo.getSubNodeCode())) {
                propLossHandleService.cancelProp(taskVo.getHandlerIdKey());
            }
        }
        scheduleTaskService.cancelDLossByDLossIds(newScheduleTaskVo, toUpdateIdList);
        //??????prplcheckduty ????????????????????????????????????
        scheduleTaskService.setCheckDutyByRegistNo(newScheduleTaskVo.getRegistNo(), toUpdateIdList);
        wfTaskHandleService.cancelTask(submitVo.getAssignUser(), flowTaskIds.toArray(new BigDecimal[flowTaskIds.size()]));

        //?????????????????????????????????handlerIDKey?????????PrpLScheduleDefLoss???ID
        for (BigDecimal taskId : flowTaskIds) {
            for (Long key : idMap.keySet()) {
                if (String.valueOf(taskId).equals(String.valueOf(idMap.get(key)))) {
                    wfTaskHandleService.updateTaskOut(Double.valueOf(taskId.toString()), key.toString());
                }
            }
        }
        //???????????????????????????????????????prplscheduleitems???flag?????????0??????????????????????????????????????????
        List<PrpLScheduleItemsVo> scheduleItemsList = scheduleTaskService.getPrpLScheduleItemsesVoByRegistNo(newScheduleTaskVo.getRegistNo());
        for (Long key : idMap.keySet()) {
            PrpLWfTaskVo prplWFTaskVo = wfTaskHandleService.queryTask(Double.valueOf(idMap.get(key).toString()));
            if (FlowNode.Sched.name().equals(prplWFTaskVo.getTaskInNode())) {
                PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(key);
                for (PrpLScheduleItemsVo vo : scheduleItemsList) {
                    if (vo.getSerialNo().equals(scheduleDefLossVo.getSerialNo().toString())) {
                        vo.setFlag("0");
                        scheduleTaskService.updatePrpLScheduleItems(vo);
                    }
                }
            }
        }


        return "1";
    }

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#reassignmentDefLossTask(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo, ins.sino.claimcar.flow.vo.WfTaskSubmitVo)
     */
    @Override
    public List<PrpLScheduleTaskVo> reassignmentDefLossTask(PrpLScheduleTaskVo newScheduleTaskVo,
                                                            PrpLScheduleDefLossVo scheduleDefLossVo, WfTaskSubmitVo submitVo) {
        //??????????????????tasklist
        List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
        //??????
        List<PrpLScheduleTasklogVo> scheduleTasklogVoList = new ArrayList<PrpLScheduleTasklogVo>();
        //?????????
        List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = new ArrayList<PrpLScheduleDefLossVo>();
		
		/*//??????????????????
		PrpLScheduleTaskVo newScheduleTaskVo = Beans.copyDepth().from(prpLScheduleTaskVo).to(PrpLScheduleTaskVo.class);*/
        //???id??????
        newScheduleTaskVo.setId(null);
        //???????????????????????????????????????1????????????????????????????????????????????????????????????validFlag???0
        newScheduleTaskVo.setValidFlag("1");
        //???????????????????????????
        //newScheduleTaskVo.setScheduleStatus("5");
        //?????????????????????
        newScheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.DEFLOSS_SCHEDULE);
        //????????????????????????
        newScheduleTaskVo.setIsPersonFlag("0");
        newScheduleTaskVo.setCreateUser(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setCreateTime(new Date());
        newScheduleTaskVo.setUpdateUser(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setUpdateTime(new Date());
        newScheduleTaskVo.setOperatorCode(ServiceUserUtils.getUserCode());
        newScheduleTaskVo.setOperatorName(ServiceUserUtils.getUserName());
        newScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_CHANGE);
        newScheduleTaskVo.setCancelOrReassinModifyTime(new Date());
        newScheduleTaskVo.setLossContent(scheduleDefLossVo.getItemsContent());

        //????????????
        PrpLScheduleTasklogVo scheduleTasklogVo = Beans.copyDepth().from(newScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
        scheduleTasklogVo.setScheduledTime(new Date());
        scheduleTasklogVoList.add(scheduleTasklogVo);

//		scheduleDefLossVo.setLicenseNo(scheduleDefLossVo.getItemsContent());

        //????????????????????????
        scheduleDefLossVo.setScheduleStatus(newScheduleTaskVo.getScheduleStatus());
        //???????????????????????????????????????
        scheduleDefLossVo.setAddoperatorCode(newScheduleTaskVo.getOperatorCode());
        scheduleDefLossVo.setScheduledUsercode(newScheduleTaskVo.getScheduledUsercode());
        scheduleDefLossVo.setScheduledComcode(newScheduleTaskVo.getScheduledComcode());
        //??????????????????????????????
        scheduleDefLossVo.setUpdateUser(newScheduleTaskVo.getOperatorCode());
        scheduleDefLossVo.setUpdateTime(newScheduleTaskVo.getUpdateTime());
        //??????vo
        prpLScheduleDefLossVoList.add(scheduleDefLossVo);
        newScheduleTaskVo.setPrpLScheduleDefLosses(prpLScheduleDefLossVoList);
        newScheduleTaskVo.setPrpLScheduleTasklogs(scheduleTasklogVoList);
        scheduleTaskVoList.add(scheduleTaskService.saveScheduleTaskByVo(newScheduleTaskVo));
        wfTaskHandleService.reassignmentTask(submitVo, null);//?????????????????????handlerIdKey
        return scheduleTaskVoList;
    }


    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#reassignmentScheduleItemTask(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, java.util.List, ins.sino.claimcar.flow.vo.WfTaskSubmitVo)
     */
    @Override
    public void reassignmentScheduleItemTask(PrpLScheduleTaskVo prpLScheduleTaskVo, List<PrpLScheduleItemsVo> prpLScheduleItemses, WfTaskSubmitVo submitVo) {
        String newId = "";
        //??????????????????
        PrpLScheduleTasklogVo personScheduleTasklogVo = new PrpLScheduleTasklogVo();
        //?????????????????????
        PrpLScheduleTasklogVo otherScheduleTasklogVo = new PrpLScheduleTasklogVo();
        Date nowDate = new Date();
        //????????????????????????
        PrpLScheduleTaskVo otherScheduleTaskVo = Beans.copyDepth().from(prpLScheduleTaskVo).to(PrpLScheduleTaskVo.class);
        //???id??????
        otherScheduleTaskVo.setId(null);
        //???????????????????????????????????????1????????????????????????????????????????????????????????????validFlag???0
        otherScheduleTaskVo.setValidFlag("1");
        //?????????????????????
        otherScheduleTaskVo.setScheduleType(CodeConstants.ScheduleType.CHECK_SCHEDULE);
        //????????????????????????
        otherScheduleTaskVo.setIsPersonFlag("0");
        otherScheduleTaskVo.setCreateUser(ServiceUserUtils.getUserCode());
        otherScheduleTaskVo.setCreateTime(nowDate);
        otherScheduleTaskVo.setUpdateUser(ServiceUserUtils.getUserCode());
        otherScheduleTaskVo.setCancelOrReassinModifyTime(nowDate);
        otherScheduleTaskVo.setUpdateTime(nowDate);
        otherScheduleTaskVo.setOperatorCode(ServiceUserUtils.getUserCode());
        otherScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_CHANGE);
        otherScheduleTaskVo.setScheduledTime(new Date());
        //?????????????????????
        PrpLScheduleTaskVo personScheduleTaskVo = Beans.copyDepth().from(otherScheduleTaskVo).to(PrpLScheduleTaskVo.class);
        //?????????????????????
        personScheduleTaskVo.setIsPersonFlag("1");
        //????????????item
        List<PrpLScheduleItemsVo> personScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();
        //????????????item
        List<PrpLScheduleItemsVo> otherScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();
        StringBuffer str = new StringBuffer();
        //??????prpLScheduleItemses??????item?????????????????????????????????????????????????????????????????????????????????
        if (prpLScheduleItemses != null && prpLScheduleItemses.size() > 0) {
            for (PrpLScheduleItemsVo vo : prpLScheduleItemses) {
                //???????????????????????????????????????
                vo.setAddoperatorCode(ServiceUserUtils.getUserCode());
                //??????????????????????????????
                vo.setUpdateUser(ServiceUserUtils.getUserCode());
                vo.setUpdateTime(new Date());
                vo.setRegistNo(prpLScheduleTaskVo.getRegistNo());
                //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                if (StringUtils.isBlank(vo.getCreateUser())) {
                    vo.setCreateUser(ServiceUserUtils.getUserCode());
                    vo.setCreateTime(new Date());
                }

                if (StringUtils.equals(vo.getItemType(), "4")) {//itemtype???4????????????vo????????????itemList???
                    vo.setItemsName("??????");
                    personScheduleTaskVo.setLossContent(vo.getItemsContent() + " ??? " + vo.getItemRemark() + " ???");

                    if (!StringUtils.equals(vo.getScheduleStatus(), "8")) {//itemtype???8??????????????????vo??????
                        //????????????????????????
                        vo.setScheduleStatus(personScheduleTaskVo.getScheduleStatus());
                        //??????????????????
                        vo.setScheduledUsercode(personScheduleTaskVo.getScheduledUsercode());
                        vo.setScheduledComcode(personScheduleTaskVo.getScheduledComcode());
						/*personScheduleTasklogVo.setScheduledUsercode(prpLScheduleTaskVo.getPersonScheduledUsercode());
						personScheduleTasklogVo.setScheduledComcode(prpLScheduleTaskVo.getPersonScheduledUsercode());*/
                    }

                    personScheduleItemses.add(vo);
                } else {//????????????otherItemList???
                    if (!StringUtils.equals(vo.getScheduleStatus(), "8")) {//itemtype???8??????????????????vo??????
                        //????????????????????????
                        vo.setScheduleStatus(otherScheduleTaskVo.getScheduleStatus());
                        //??????????????????
                        vo.setScheduledUsercode(otherScheduleTaskVo.getScheduledUsercode());
                        vo.setScheduledComcode(otherScheduleTaskVo.getScheduledComcode());
                        //StringBuffer str = new StringBuffer("");
						/*if (!StringUtils.isEmpty(otherScheduleTasklogVo.getLossContent())) {
							str = str.append(otherScheduleTasklogVo.getLossContent());
							str = str.append(vo.getItemsContent());
							str = str.append(",");
						}*/
                        str = str.append(vo.getItemsContent());
                        str = str.append(",");
                        otherScheduleTaskVo.setLossContent(str.toString());
                        //otherScheduleTaskVo.setLossContent(str.toString() + vo.getItemsContent());
						/*otherScheduleTasklogVo.setScheduledUsercode(prpLScheduleTaskVo.getScheduledUsercode());
						otherScheduleTasklogVo.setScheduledComcode(prpLScheduleTaskVo.getScheduledComcode());*/
                    }

                    otherScheduleItemses.add(vo);
                }
            }
        }
        if (str.length() > 0) {
            otherScheduleTaskVo.setLossContent(str.toString().substring(0, str.length() - 1));
        }
        //size()>0???????????????????????????????????????items?????????????????????????????????
        if (personScheduleItemses.size() > 0) {
            //???????????????????????????????????????
            personScheduleTasklogVo = Beans.copyDepth().from(personScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
            List<PrpLScheduleTasklogVo> personTaskLogVoList = new ArrayList<PrpLScheduleTasklogVo>();
            personTaskLogVoList.add(personScheduleTasklogVo);
            personScheduleTaskVo.setPrpLScheduleItemses(personScheduleItemses);
            personScheduleTaskVo.setPrpLScheduleTasklogs(personTaskLogVoList);
            //personScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_FINISH);
            scheduleTaskService.saveScheduleTaskByVo(personScheduleTaskVo);
        }

        //size()>0????????????????????????????????????items??????????????????????????????????????????????????????????????????????????????????????????
        if (otherScheduleItemses.size() > 0) {
            //??????????????????????????????????????????
            otherScheduleTasklogVo = Beans.copyDepth().from(otherScheduleTaskVo).to(PrpLScheduleTasklogVo.class);
            List<PrpLScheduleTasklogVo> otherTaskLogVoList = new ArrayList<PrpLScheduleTasklogVo>();
            otherTaskLogVoList.add(otherScheduleTasklogVo);
            otherScheduleTaskVo.setPrpLScheduleItemses(otherScheduleItemses);
            otherScheduleTaskVo.setPrpLScheduleTasklogs(otherTaskLogVoList);
            //otherScheduleTaskVo.setScheduleStatus(CodeConstants.ScheduleStatus.SCHEDULED_FINISH);
            //???????????????????????????id
            newId = scheduleTaskService.saveScheduleTaskByVo(otherScheduleTaskVo).getId().toString();
        }

        wfTaskHandleService.reassignmentTask(submitVo, newId);
    }

    /* (non-Javadoc)
     * @see ins.sino.claimcar.schedule.service.ScheduleHandlerService#sendMsg(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, ins.platform.vo.SysUserVo, java.lang.String)
     */
    @Override
    public void sendMsg(PrpLScheduleTaskVo prpLScheduleTaskVo, List<PrpLScheduleItemsVo> prpLScheduleItemses,
                        SysUserVo sysUserVo, String scheduleType) {
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLScheduleTaskVo.getRegistNo(),
                prpLRegistVo.getPolicyNo());
        String comCode = null;
        if (prpLCMain != null) {
            comCode = prpLCMain.getComCode();
        }
        //?????????????????????????????????????????????????????????
        String serviceMobile = "";
        Long repairId = null;//???????????????Id
        String BIPolicyNo = "";
        String CIPolicyNo = "";
        List<PrpLCMainVo> prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(prpLScheduleTaskVo.getRegistNo());
        if (prpLCMainVos != null && prpLCMainVos.size() > 0) {
            for (PrpLCMainVo mainVo : prpLCMainVos) {
                if ("1101".equals(mainVo.getRiskCode())) {
                    CIPolicyNo = mainVo.getPolicyNo();
                } else {
                    BIPolicyNo = mainVo.getPolicyNo();
                }
            }
        }

        if (StringUtils.isNotBlank(BIPolicyNo)) {
            serviceMobile = policyQueryService.findPrpCMian(BIPolicyNo);
        }
        if (StringUtils.isNotBlank(CIPolicyNo) && StringUtils.isBlank(serviceMobile)) {
            serviceMobile = policyQueryService.findPrpCMian(CIPolicyNo);
        }
        if (StringUtils.isNotBlank(serviceMobile)) {
            repairId = repairFactoryService.findRepairFactoryBy(serviceMobile);
        }
        prpLRegistVo.setRepairId(repairId);
        SendMsgParamVo msgParamVo = this.getsendMsgParamVo(prpLScheduleTaskVo, prpLRegistVo,
                prpLScheduleItemses, sysUserVo, scheduleType, prpLCMain);
        if (repairId != null && !"1".equals(prpLRegistVo.getSelfClaimFlag())) {
            //???????????????--????????????????????????
            SysMsgModelVo msgModelVo_13 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.check, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.repair);
            if (msgModelVo_13 != null && StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                msgParamVo.setModelType("13");
                msgParamVo.setSystemNode("2");
                Date sendTime_13 = sendMsgService.getSendTime(msgModelVo_13.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_13.getContent(), msgParamVo);
                msgParamVo.setModelType(null);
                msgParamVo.setSystemNode(null);
                String status = "";
                boolean index = false;
                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_13);
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_13, message, status);
            } else {
                String content = msgParamVo.getRegistNo() +
                        " ??????????????????????????????????????????!" +
                        " ModelTYpe:" + CodeConstants.ModelType.check +
                        " SystemNode:" + CodeConstants.SystemNode.addCheck +
                        "CaseType:" + CodeConstants.CaseType.repair;
                logger.info(content);
            }
        }


        if ("1".equals(scheduleType)) {//????????????
            //???????????????????????????
            SysMsgModelVo msgModelVo_1 = null;
            if (prpLRegistVo != null && "1".equals(prpLRegistVo.getSelfClaimFlag()) && !"1".equals(prpLRegistVo.getSelfRegistFlag())) {
                SysMsgModelVo msgModelVo_12 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.selfClaim);
                PrpLRegistExtVo prpLRegistExtVo = prpLRegistVo.getPrpLRegistExt();
                if (msgModelVo_12 != null && StringUtils.isNotBlank(msgModelVo_12.getContent()) && msgModelVo_12.getContent().contains("??????????????????") && prpLRegistExtVo != null && StringUtils.isNotBlank(prpLRegistExtVo.getOrderNo())) {
                    msgModelVo_1 = msgModelVo_12;
                }
                if (msgModelVo_1 == null) {
                    String content = msgParamVo.getRegistNo() +
                            " ?????????????????????????????????????????????????????????!" +
                            " ModelTYpe:" + CodeConstants.ModelType.report +
                            " SystemNode:" + CodeConstants.SystemNode.addCheck +
                            "CaseType:" + CodeConstants.CaseType.selfClaim;
                    logger.info(content);
                }
            } else {
                msgModelVo_1 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.normal);
                if (msgModelVo_1 == null) {
                    String content = msgParamVo.getRegistNo() +
                            " ??????????????????????????????!" +
                            " ModelTYpe:" + CodeConstants.ModelType.report +
                            " SystemNode:" + CodeConstants.SystemNode.addCheck +
                            "CaseType:" + CodeConstants.CaseType.normal;
                    logger.info(content);
                }
            }
            if (msgModelVo_1 != null) {
                Date sendTime_1 = sendMsgService.getSendTime(msgModelVo_1.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_1.getContent(), msgParamVo);
                String status = "";
                boolean index = false;
                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getLinkerMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_1);
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                if (StringUtils.isNotBlank(msgParamVo.getLinkerMobile())) {
                    putSmsmessage(msgParamVo, msgParamVo.getLinkerMobile(), sendTime_1, message, status);
                }

            }
            //???????????????
            String modelType = CodeConstants.ModelType.check;
            String caseType = "1".equals(prpLRegistVo.getIsQuickCase()) ? CodeConstants.CaseType.hnQuickCase : CodeConstants.CaseType.normal;
            SysMsgModelVo msgModelVo_2 = sendMsgService.findmsgModelVo(modelType, CodeConstants.SystemNode.addCheck, comCode, caseType);
            if (!"1".equals(prpLRegistVo.getSelfClaimFlag())) {
                if (msgModelVo_2 != null) {
                    msgParamVo.setModelType(modelType);
                    msgParamVo.setSystemNode("2");
                    Date sendTime_2 = sendMsgService.getSendTime(msgModelVo_2.getTimeType());
                    String message = sendMsgService.getMessage(msgModelVo_2.getContent(), msgParamVo);
                    msgParamVo.setModelType(null);
                    msgParamVo.setSystemNode(null);
                    if (msgParamVo.getScheduledMobile() != null) {
                        String status = "";
                        boolean index = false;
                        index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_2);
                        if (index) {
                            status = "1";//????????????????????????
                        } else {
                            status = "0";//????????????????????????
                        }
                        if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                            putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_2, message, status);
                        }
                    } else {
                        String content = msgParamVo.getRegistNo() +
                                " ????????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.check +
                                " SystemNode:" + CodeConstants.SystemNode.addCheck +
                                " CaseType:" + caseType;
                        logger.info(content);
                    }
                } else {
                    String content = msgParamVo.getRegistNo() +
                            " ?????????????????????????????????!" +
                            " ModelTYpe:" + CodeConstants.ModelType.check +
                            " SystemNode:" + CodeConstants.SystemNode.addCheck +
                            " CaseType:" + caseType;
                    logger.info(content);
                }
            }

            //??????????????????????????????
            SysMsgModelVo msgModelVo_4 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.repair, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.normal);
            if (msgModelVo_4 != null && msgParamVo.getRepairMobile() != null && !"".equals(msgParamVo.getRepairMobile())) {
                Date sendTime_4 = sendMsgService.getSendTime(msgModelVo_4.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_4.getContent(), msgParamVo);
                String status = "";
                boolean index = false;
                if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_4);
                }
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                    putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_4, message, status);
                }
            } else {
                String content = msgParamVo.getRegistNo() +
                        " ???????????????????????????????????????????????????????????????!" +
                        " ModelTYpe:" + CodeConstants.ModelType.repair +
                        " SystemNode:" + CodeConstants.SystemNode.addCheck +
                        " CaseType:" + CodeConstants.CaseType.normal;
                logger.info(content);
            }

            //?????????????????????????????????
            if (msgParamVo.getIsPersonLoss()) {
                //?????????????????????
                SysMsgModelVo msgModelVo_3 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.pLoss, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.normal);
                if (msgModelVo_3 != null) {
                    Date sendTime_3 = sendMsgService.getSendTime(msgModelVo_3.getTimeType());
                    String message = sendMsgService.getMessage(msgModelVo_3.getContent(), msgParamVo);
                    String status = "";
                    boolean index = false;
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getPersonScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_3);
                    if (index) {
                        status = "1";//????????????????????????
                    } else {
                        status = "0";//????????????????????????
                    }
                    if (StringUtils.isNotBlank(msgParamVo.getPersonScheduledMobile())) {
                        putSmsmessage(msgParamVo, msgParamVo.getPersonScheduledMobile(), sendTime_3, message, status);
                    } else {
                        String content = msgParamVo.getRegistNo() +
                                " ??????????????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.pLoss +
                                " SystemNode:" + CodeConstants.SystemNode.addCheck +
                                " CaseType:" + CodeConstants.CaseType.normal;
                        logger.info(content);
                    }
                } else {
                    String content = msgParamVo.getRegistNo() +
                            " ???????????????????????????????????????!" +
                            " ModelTYpe:" + CodeConstants.ModelType.pLoss +
                            " SystemNode:" + CodeConstants.SystemNode.addCheck +
                            " CaseType:" + CodeConstants.CaseType.normal;
                    logger.info(content);
                }
                //???????????????
                SysMsgModelVo msgModelVo_5 = null;
                if (prpLRegistVo != null && "1".equals(prpLRegistVo.getSelfClaimFlag()) && !"1".equals(prpLRegistVo.getSelfRegistFlag())) {
                    SysMsgModelVo msgModelVo_12 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addPLoss, comCode, CodeConstants.CaseType.selfClaim);
                    PrpLRegistExtVo prpLRegistExtVo = prpLRegistVo.getPrpLRegistExt();
                    if (msgModelVo_12 != null && StringUtils.isNotBlank(msgModelVo_12.getContent()) && msgModelVo_12.getContent().contains("??????????????????") && prpLRegistExtVo != null && StringUtils.isNotBlank(prpLRegistExtVo.getOrderNo())) {
                        msgModelVo_5 = msgModelVo_12;
                    }
                    if (msgModelVo_5 == null) {
                        String content = msgParamVo.getRegistNo() +
                                " ??????????????????????????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.report +
                                " SystemNode:" + CodeConstants.SystemNode.addPLoss +
                                " CaseType:" + CodeConstants.CaseType.selfClaim;
                        logger.info(content);
                    }
                } else {
                    msgModelVo_5 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addPLoss, comCode, CodeConstants.CaseType.normal);
                    if (msgModelVo_5 == null) {
                        String content = msgParamVo.getRegistNo() +
                                " ?????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.report +
                                " SystemNode:" + CodeConstants.SystemNode.addPLoss +
                                " CaseType:" + CodeConstants.CaseType.normal;
                        logger.info(content);
                    }
                }
                if (msgModelVo_5 != null) {
                    Date sendTime_5 = sendMsgService.getSendTime(msgModelVo_5.getTimeType());
                    String message = sendMsgService.getMessage(msgModelVo_5.getContent(), msgParamVo);
                    String status = "";
                    boolean index = false;
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getLinkerMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_5);
                    if (index) {
                        status = "1";//????????????????????????
                    } else {
                        status = "0";//????????????????????????
                    }
                    if (StringUtils.isNotBlank(msgParamVo.getLinkerMobile())) {
                        putSmsmessage(msgParamVo, msgParamVo.getLinkerMobile(), sendTime_5, message, status);
                    }
                }
            }
            //?????????????????????????????????????????????????????????
            if ("1".equals(prpLRegistVo.getSelfRegistFlag())) {
                List<String> mobileList = sendMsgService.getMobile(comCode, "2");
                if (mobileList != null && mobileList.size() > 0) {
                    SysMsgModelVo msgModelVo_10 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.staff, CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.selfClaim);
                    if (msgModelVo_10 != null) {
                        Date sendTime_10 = sendMsgService.getSendTime(msgModelVo_10.getTimeType());
                        String message_10 = sendMsgService.getMessage(msgModelVo_10.getContent(), msgParamVo);
                        String status = "";
                        boolean index = false;
                        for (String mobile : mobileList) {
                            if (StringUtils.isNotBlank(mobile)) {
                                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_10, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_10);
                                if (index) {
                                    status = "1";// ????????????????????????
                                } else {
                                    status = "0";// ????????????????????????
                                }
                                putSmsmessage(msgParamVo, mobile, sendTime_10, message_10, status);
                            }
                        }
                    } else {
                        String content = msgParamVo.getRegistNo() +
                                " ??????????????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.staff +
                                " SystemNode:" + CodeConstants.SystemNode.addCheck +
                                " CaseType:" + CodeConstants.CaseType.selfClaim;
                        logger.info(content);
                    }
                } else {
                    String content = msgParamVo.getRegistNo() +
                            " ?????????????????????????????????????????????!" +
                            " ModelTYpe:" + CodeConstants.ModelType.staff +
                            " SystemNode:" + CodeConstants.SystemNode.addCheck +
                            " CaseType:" + CodeConstants.CaseType.selfClaim;
                    logger.info(content);
                }
            }

            //maofengning  2054-??????????????????????????????????????????????????????
            if ("1".equals(prpLScheduleTaskVo.getIsAutoCheck())) {
                // ???????????????
                List<String> mobileList = sendMsgService.getMobile(comCode, CodeConstants.ModelType.autocheck);
                if (mobileList != null && mobileList.size() > 0) {
                    // ??????????????????
                    SysMsgModelVo msgModelVo_autocheck = sendMsgService.findmsgModelVo(CodeConstants.ModelType.autocheck,
                            CodeConstants.SystemNode.addCheck, comCode, CodeConstants.CaseType.selfClaim);
                    if (msgModelVo_autocheck != null) {
                        Date sendTime_autocheck = sendMsgService.getSendTime(msgModelVo_autocheck.getTimeType());
                        String message_autocheck = sendMsgService.getMessage(msgModelVo_autocheck.getContent(), msgParamVo);
                        String status = "";
                        boolean index = false;
                        for (String mobile : mobileList) {
                            if (StringUtils.isNotBlank(mobile)) {
                                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), mobile, message_autocheck, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_autocheck);
                                if (index) {
                                    status = "1";// ????????????????????????
                                } else {
                                    status = "0";// ????????????????????????
                                }
                                putSmsmessage(msgParamVo, mobile, sendTime_autocheck, message_autocheck, status);
                            }
                        }
                    } else {
                        String log = msgParamVo.getRegistNo() +
                                "??????????????????????????????????????????????????????" +
                                " ModelTYpe:" + CodeConstants.ModelType.autocheck +
                                " SystemNode:" + CodeConstants.SystemNode.addCheck +
                                " CaseType:" + CodeConstants.CaseType.normal +
                                " comcode:" + comCode;
                        logger.info(log);
                    }

                } else {
                    String log = msgParamVo.getRegistNo() +
                            "????????????????????????????????????????????????????????????" +
                            " ????????????comcode???" + comCode +
                            " ????????????????????????prpdaddress.flag???" + CodeConstants.ModelType.autocheck;
                    logger.info(log);
                }

            }


        } else {//????????????
            //????????????
            SysMsgModelVo msgModelVo_1 = null;
            if (prpLRegistVo != null && "1".equals(prpLRegistVo.getSelfClaimFlag()) && !"1".equals(prpLRegistVo.getSelfRegistFlag())) {
                SysMsgModelVo msgModelVo_12 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addDLoss, comCode, CodeConstants.CaseType.selfClaim);
                PrpLRegistExtVo prpLRegistExtVo = prpLRegistVo.getPrpLRegistExt();
                if (msgModelVo_12 != null && StringUtils.isNotBlank(msgModelVo_12.getContent()) && msgModelVo_12.getContent().contains("??????????????????") && prpLRegistExtVo != null && StringUtils.isNotBlank(prpLRegistExtVo.getOrderNo())) {
                    msgModelVo_1 = msgModelVo_12;
                }
            } else {
                msgModelVo_1 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.addDLoss, comCode, CodeConstants.CaseType.normal);
            }
            if (msgModelVo_1 != null) {
                Date sendTime_1 = sendMsgService.getSendTime(msgModelVo_1.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_1.getContent(), msgParamVo);
//				msgParamVo.setLinkerMobile("18603044943");//???????????????????????????
                String status = "";
                boolean index = false;
                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getLinkerMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_1);
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                if (StringUtils.isNotBlank(msgParamVo.getLinkerMobile())) {
                    putSmsmessage(msgParamVo, msgParamVo.getLinkerMobile(), sendTime_1, message, status);
                }
            } else {
                String content = msgParamVo.getRegistNo() +
                        " ?????????????????????????????????????????????!";
                logger.info(content);
            }
            //???????????????
            SysMsgModelVo msgModelVo_2 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.dLoss, CodeConstants.SystemNode.addDLoss, comCode, CodeConstants.CaseType.normal);
            if (msgModelVo_2 != null) {
                Date sendTime_2 = sendMsgService.getSendTime(msgModelVo_2.getTimeType());
                String message_2 = sendMsgService.getMessage(msgModelVo_2.getContent(), msgParamVo);
//				msgParamVo.setScheduledMobile("18603044943");//???????????????????????????
                if (msgParamVo.getScheduledMobile() != null) {
                    String status = "";
                    boolean index = false;
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message_2, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_2);
                    if (index) {
                        status = "1";//????????????????????????
                    } else {
                        status = "0";//????????????????????????
                    }
                    if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                        putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_2, message_2, status);
                    } else {
                        String content = msgParamVo.getRegistNo() +
                                " ????????????????????????????????????!" +
                                " ModelTYpe:" + CodeConstants.ModelType.dLoss +
                                " SystemNode:" + CodeConstants.SystemNode.addDLoss +
                                " CaseType:" + CodeConstants.CaseType.normal;
                        logger.info(content);
                    }
                }
            } else {
                String content = msgParamVo.getRegistNo() +
                        " ?????????????????????????????????????????????!" +
                        " ModelTYpe:" + CodeConstants.ModelType.dLoss +
                        " SystemNode:" + CodeConstants.SystemNode.addDLoss +
                        " CaseType:" + CodeConstants.CaseType.normal;
                logger.info(content);
            }
        }

    }
    @Override
    public void sendMsgByReassignment(PrpLScheduleTaskVo prpLScheduleTaskVo, String schType, SysUserVo sysUserVo, String scheduleType) {
        List<PrpLScheduleTaskVo> scheduleTaskVoList = scheduleTaskService.findScheduleTaskListByRegistNo(prpLScheduleTaskVo.getRegistNo());
        List<PrpLScheduleItemsVo> list = new ArrayList<PrpLScheduleItemsVo>();
        PrpLScheduleItemsVo prpLScheduleItemsVo = new PrpLScheduleItemsVo();
        for (PrpLScheduleTaskVo vo : scheduleTaskVoList) {//???????????????
            if ("1".equals(vo.getIsPersonFlag())) {
                String content = vo.getLossContent();
                content = content.replaceAll("\\s*", "");
                prpLScheduleItemsVo.setItemsContent(content.split("???")[0]);
                prpLScheduleItemsVo.setItemRemark(content.split("???")[1].split("???")[0]);
                prpLScheduleItemsVo.setItemType("4");
                list.add(prpLScheduleItemsVo);
                break;
            }
        }

        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        PrpLCMainVo prpLCMain = policyViewService.getPolicyInfo(prpLScheduleTaskVo.getRegistNo(),
                prpLRegistVo.getPolicyNo());
        String comCode = null;
        if (prpLCMain != null) {
            comCode = prpLCMain.getComCode();
        }
        SendMsgParamVo msgParamVo = this.getsendMsgParamVo(prpLScheduleTaskVo, prpLRegistVo, list,
                sysUserVo, scheduleType, prpLCMain);
        if (FlowNode.DLCar.equals(schType) || FlowNode.DLProp.equals(schType)) {//???????????????
            SysMsgModelVo msgModelVo_1 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.dLoss, CodeConstants.SystemNode.reassign, comCode, CodeConstants.CaseType.normal);
            if (msgModelVo_1 != null) {
                Date sendTime_1 = sendMsgService.getSendTime(msgModelVo_1.getTimeType());
                String message_1 = sendMsgService.getMessage(msgModelVo_1.getContent(), msgParamVo);
                if (msgParamVo.getScheduledMobile() != null) {
                    String status = "";
                    boolean index = false;
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message_1, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_1);
                    if (index) {
                        status = "1";//????????????????????????
                    } else {
                        status = "0";//????????????????????????
                    }
                    if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                        putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_1, message_1, status);
                    }
                }
            }
            //????????????
            SysMsgModelVo msgModelVo_4 = null;
            if (prpLRegistVo != null && "1".equals(prpLRegistVo.getSelfClaimFlag()) && !"1".equals(prpLRegistVo.getSelfRegistFlag())) {
                SysMsgModelVo msgModelVo_12 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.reassign, comCode, CodeConstants.CaseType.selfClaim);
                PrpLRegistExtVo prpLRegistExtVo = prpLRegistVo.getPrpLRegistExt();
                if (msgModelVo_12 != null && StringUtils.isNotBlank(msgModelVo_12.getContent()) && msgModelVo_12.getContent().contains("??????????????????") && prpLRegistExtVo != null && StringUtils.isNotBlank(prpLRegistExtVo.getOrderNo())) {
                    msgModelVo_4 = msgModelVo_12;
                }
            } else {
                msgModelVo_4 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.report, CodeConstants.SystemNode.reassign, comCode, CodeConstants.CaseType.normal);
            }
            if (msgModelVo_4 != null) {
                Date sendTime_4 = sendMsgService.getSendTime(msgModelVo_4.getTimeType());
                String message_4 = sendMsgService.getMessage(msgModelVo_4.getContent(), msgParamVo);
                String status = "";
                boolean index = false;
                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getLinkerMobile(), message_4, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_4);
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                if (StringUtils.isNotBlank(msgParamVo.getLinkerMobile())) {
                    putSmsmessage(msgParamVo, msgParamVo.getLinkerMobile(), sendTime_4, message_4, status);
                }
            }

        } else if (FlowNode.PLoss.equals(schType)) {//???????????????
            SysMsgModelVo msgModelVo_3 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.pLoss, CodeConstants.SystemNode.reassign, comCode, CodeConstants.CaseType.normal);
            if (msgModelVo_3 != null) {
                Date sendTime_3 = sendMsgService.getSendTime(msgModelVo_3.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_3.getContent(), msgParamVo);
                String status = "";
                boolean index = false;
                index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_3);
                if (index) {
                    status = "1";//????????????????????????
                } else {
                    status = "0";//????????????????????????
                }
                if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                    putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_3, message, status);
                }
            }
        } else {//???????????????
            SysMsgModelVo msgModelVo_2 = sendMsgService.findmsgModelVo(CodeConstants.ModelType.check, CodeConstants.SystemNode.reassign, comCode, CodeConstants.CaseType.normal);
            if (msgModelVo_2 != null) {
                Date sendTime_2 = sendMsgService.getSendTime(msgModelVo_2.getTimeType());
                String message = sendMsgService.getMessage(msgModelVo_2.getContent(), msgParamVo);
                if (msgParamVo.getScheduledMobile() != null) {
                    String status = "";
                    boolean index = false;
                    index = smsService.sendSMSContent(msgParamVo.getRegistNo(), msgParamVo.getScheduledMobile(), message, msgParamVo.getUseCode(), msgParamVo.getComCode(), sendTime_2);
                    if (index) {
                        status = "1";//????????????????????????
                    } else {
                        status = "0";//????????????????????????
                    }
                    if (StringUtils.isNotBlank(msgParamVo.getScheduledMobile())) {
                        putSmsmessage(msgParamVo, msgParamVo.getScheduledMobile(), sendTime_2, message, status);
                    }
                }
            }
        }
    }

    public SendMsgParamVo getsendMsgParamVo(PrpLScheduleTaskVo prpLScheduleTaskVo, PrpLRegistVo prpLRegistVo,
                                            List<PrpLScheduleItemsVo> prpLScheduleItemses, SysUserVo sysUserVo, String scheduleType, PrpLCMainVo prpLCMainVo) {
        SendMsgParamVo msgParamVo = new SendMsgParamVo();
        msgParamVo.setUseCode(sysUserVo.getUserCode());
        msgParamVo.setComCode(sysUserVo.getComCode());
        msgParamVo.setRegistNo(prpLScheduleTaskVo.getRegistNo());
        msgParamVo.setReportTime(DateUtils.dateToStr(prpLRegistVo.getReportTime(), DateUtils.YToSec));
        if (prpLScheduleTaskVo.getScheduledTime() != null) {
            msgParamVo.setScheduledTime(DateUtils.dateToStr(prpLScheduleTaskVo.getScheduledTime(), DateUtils.YToSec));
        }
        if (prpLRegistVo.getPolicyNo() != null && !"".equals(prpLRegistVo.getPolicyNo())) {
            msgParamVo.setPolicyNo(prpLRegistVo.getPolicyNo());
        }
        if (prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getInsuredName() != null) {
            msgParamVo.setInsuredName(prpLRegistVo.getPrpLRegistExt().getInsuredName());
        }
        if (prpLRegistVo.getInsuredPhone() != null) {
            msgParamVo.setMobile(prpLRegistVo.getInsuredPhone());
        }
        if (prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getDangerRemark() != null) {
            msgParamVo.setDangerRemark(prpLRegistVo.getPrpLRegistExt().getDangerRemark());
        }
        if (prpLRegistVo.getPrpLRegistCarLosses().size() >= 2) {//??????????????????
            for (PrpLRegistCarLossVo registCarLossVo : prpLRegistVo.getPrpLRegistCarLosses()) {
                if ("3".equals(registCarLossVo.getLossparty()) && registCarLossVo.getLicenseNo() != null) {
                    msgParamVo.setOtherLicenseNo(registCarLossVo.getLicenseNo());
                    break;
                }
            }
        }
        if (prpLRegistVo.getReportorName() != null) {
            msgParamVo.setReportorName(prpLRegistVo.getReportorName());
        }
        if (prpLRegistVo.getReportorPhone() != null && !"".equals(prpLRegistVo.getReportorPhone())) {//???????????????
            msgParamVo.setReportoMobile(prpLRegistVo.getReportorPhone());
        }
        //???????????????
        if (prpLRegistVo.getDriverName() != null && !"".equals(prpLRegistVo.getDriverName())) {
            msgParamVo.setDriverName(prpLRegistVo.getDriverName());
        }
        //???????????????
        if (prpLRegistVo.getDriverPhone() != null && !"".equals(prpLRegistVo.getDriverPhone())) {
            msgParamVo.setDriverPhone(prpLRegistVo.getDriverPhone());
        }
        if (prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getFrameNo() != null
                && !"".equals(prpLRegistVo.getPrpLRegistExt())) {
            msgParamVo.setFrameNo(prpLRegistVo.getPrpLRegistExt().getFrameNo());
        }
        if ("1101".equals(prpLRegistVo.getRiskCode())) {
            msgParamVo.setRiskType("??????");
        } else {
            msgParamVo.setRiskType("??????");
        }
        if (prpLScheduleTaskVo.getCheckAddress() != null) {//????????????
            msgParamVo.setCheckAddress(prpLScheduleTaskVo.getCheckAddress());
        } else {
            msgParamVo.setCheckAddress("");
        }
        if (prpLScheduleTaskVo.getCheckareaName() != null) {//????????????
            msgParamVo.setdLossAddress(prpLScheduleTaskVo.getCheckareaName());
        } else {
            msgParamVo.setdLossAddress("");//??????????????????
        }

        if (prpLRegistVo != null) {
            if (prpLRegistVo.getDamageCode() != null) {
                msgParamVo.setDamageReason(codeTranService.transCode("DamageCode", prpLRegistVo.getDamageCode()));
            } else {
                msgParamVo.setDamageReason("");
            }
            if (prpLRegistVo.getDamageTime() != null)
                msgParamVo.setDamageTime(prpLRegistVo.getDamageTime());
            else msgParamVo.setDamageTime(new Date());
            if (prpLRegistVo.getDamageAddress() != null)
                msgParamVo.setDamageAddress(prpLRegistVo.getDamageAddress());
            else msgParamVo.setDamageAddress("");
            if (prpLCMainVo != null) {
                if (prpLCMainVo.getPrpCItemCars() != null && prpLCMainVo.getPrpCItemCars().size() > 0) {
                    msgParamVo.setBrandName(prpLCMainVo.getPrpCItemCars().get(0).getBrandName());
                } else {
                    msgParamVo.setBrandName("");
                }
                // ?????????????????????????????????????????????????????????
                if (prpLCMainVo.getHandler1Code() != null || prpLCMainVo.getHandler1Name() != null) {
                    msgParamVo.setHandler(prpLCMainVo.getHandler1Code() + "-" + prpLCMainVo.getHandler1Name());
                } else {
                    msgParamVo.setHandler("");
                }
            }
            if (prpLCMainVo != null) {
                if (prpLCMainVo.getPrpCItemKinds() != null && prpLCMainVo.getPrpCItemKinds().size() > 0) {
                    msgParamVo.setPrpCItemKinds(prpLCMainVo.getPrpCItemKinds());
                }

				if (prpLCMainVo.getIsCoreCustomer() != null && "1".equals(prpLCMainVo.getIsCoreCustomer())) {
					msgParamVo.setIsCoreCustomer("???");
				} else {
					msgParamVo.setIsCoreCustomer("???");
				}
            } else {
				msgParamVo.setIsCoreCustomer("???");
			}
            if (prpLRegistVo.getPrpLRegistExt() != null && prpLRegistVo.getPrpLRegistExt().getLicenseNo() != null) {
                msgParamVo.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
            }
            List<PrpLCMainVo> cMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
            if (cMainVoList != null && cMainVoList.size() > 0) {
                String insuredDate = this.getInsuredDate(cMainVoList);
                msgParamVo.setInsuredDate(insuredDate);
                //????????????
                msgParamVo.setBusinessPlate(codeTranService.transCode("BusinessPlate", cMainVoList.get(0).getBusinessPlate()));
                //???????????????????????????????????????????????????????????????????????????(?????????????????????BusinessClassCheckMsg)
//				if(msgParamVo.getBusinessPlate().equals("????????????")){
                Map<String, String> businessClass = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
                for (int i = 0; i < cMainVoList.size(); i++) {
                    if (businessClass.containsKey(cMainVoList.get(i).getBusinessClass())) {
                        msgParamVo.setBusinessClassCheckMsg("(" + businessClass.get(cMainVoList.get(i).getBusinessClass()) + ")");
                        break;
                    }
                }
//				}			                
            }
            if (prpLRegistVo.getPrpLRegistCarLosses() != null && prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart() != null) {
                String lossPart = this.getLossPart(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart());
                msgParamVo.setLosspart(lossPart);//?????????
            }
        }
        if (prpLScheduleTaskVo.getLinkerMan() != null)
            msgParamVo.setLinkerName(prpLScheduleTaskVo.getLinkerMan());
        else msgParamVo.setLinkerName("");
        if (prpLScheduleTaskVo.getLinkerManPhone() != null)
            msgParamVo.setLinkerMobile(prpLScheduleTaskVo.getLinkerManPhone());
        else msgParamVo.setLinkerMobile("");
        if (prpLScheduleTaskVo.getRelateHandlerMobile() != null &&
                !"".equals(prpLScheduleTaskVo.getRelateHandlerMobile())) {//?????????????????????
            msgParamVo.setScheduledMobile(prpLScheduleTaskVo.getRelateHandlerMobile());
        } else {
            msgParamVo.setScheduledMobile("");
        }
        if (prpLScheduleTaskVo.getPersonRelateHandlerMobile() != null &&
                !"".equals(prpLScheduleTaskVo.getPersonRelateHandlerMobile())) {
            msgParamVo.setPersonScheduledMobile(prpLScheduleTaskVo.getPersonRelateHandlerMobile());
        } else {
            msgParamVo.setPersonScheduledMobile("");
        }
        if (prpLScheduleTaskVo.getPersonRelateHandlerName() != null &&
                !"".equals(prpLScheduleTaskVo.getPersonRelateHandlerName())) {
            msgParamVo.setPersonScheduledUsername(prpLScheduleTaskVo.getPersonRelateHandlerName());
        } else if (prpLScheduleTaskVo.getPersonScheduledUsername() != null &&
                !"".equals(prpLScheduleTaskVo.getPersonScheduledUsername())) {
            msgParamVo.setPersonScheduledUsername(prpLScheduleTaskVo.getPersonScheduledUsername());
        }
        if (prpLScheduleTaskVo.getScheduledUsername() != null) {//???????????????
            msgParamVo.setScheduledUsername(prpLScheduleTaskVo.getScheduledUsername());
        } else {
            msgParamVo.setScheduledUsername("");
        }
        String kindCode = sendMsgService.getKindName(prpLScheduleTaskVo.getRegistNo());
        msgParamVo.setKindCode(kindCode);
        //????????????
//		int injuredcount = 0;
//		int deathcount = 0;
//		Boolean bl = true;
//		msgParamVo.setIsPersonLoss(false);
//		if(prpLScheduleItemses != null && prpLScheduleItemses.size() > 0){
//			for(PrpLScheduleItemsVo prpLScheduleItemsVo:prpLScheduleItemses){
//				if("4".equals(prpLScheduleItemsVo.getItemType())){
//					injuredcount = injuredcount + Integer.valueOf(prpLScheduleItemsVo.getItemsContent());
//					deathcount = deathcount + Integer.valueOf(prpLScheduleItemsVo.getItemRemark());
//					bl = false;
//					msgParamVo.setIsPersonLoss(true);
//				}
//			}
//		}else if(prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size()>0 && bl){
//			for(PrpLRegistPersonLossVo registPersonLossVo:prpLRegistVo.getPrpLRegistPersonLosses()){
//				injuredcount = injuredcount + registPersonLossVo.getInjuredcount();
//				deathcount = deathcount + registPersonLossVo.getDeathcount();
//				msgParamVo.setIsPersonLoss(true);
//			}
//			
//		}
        msgParamVo.setIsPersonLoss(false);
        if (prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size() > 0) {
            int thridInjuredcount = 0;  //?????????????????????
            int thridDeathcount = 0;    //?????????????????????
            int ItemInjuredcount = 0;   //?????????????????????
            int ItemDeathcount = 0;     //?????????????????????
            for (PrpLRegistPersonLossVo registPersonLossVo : prpLRegistVo.getPrpLRegistPersonLosses()) {
                if ("1".equals(registPersonLossVo.getLossparty())) {
                    ItemInjuredcount = ItemInjuredcount + registPersonLossVo.getInjuredcount();
                    ItemDeathcount = ItemDeathcount + registPersonLossVo.getDeathcount();
                } else {
                    thridInjuredcount = thridInjuredcount + registPersonLossVo.getInjuredcount();
                    thridDeathcount = thridDeathcount + registPersonLossVo.getDeathcount();
                }
            }
            msgParamVo.setThridInjuredcount(String.valueOf(thridInjuredcount));
            msgParamVo.setThridDeathcount(String.valueOf(thridDeathcount));
            msgParamVo.setItemInjuredcount(String.valueOf(ItemInjuredcount));
            msgParamVo.setItemDeathcount(String.valueOf(ItemDeathcount));
            msgParamVo.setInjuredcount(String.valueOf(thridInjuredcount + ItemInjuredcount));
            msgParamVo.setDeathcount(String.valueOf(thridDeathcount + ItemDeathcount));
            if ((thridInjuredcount + thridDeathcount + ItemInjuredcount + ItemDeathcount) > 0) {
                msgParamVo.setIsPersonLoss(true);
            }
        }

        if ("2".equals(scheduleType)) {//????????????
            for (PrpLScheduleDefLossVo vo : prpLScheduleTaskVo.getPrpLScheduleDefLosses()) {
                if ("1".equals(vo.getScheduleFlag())) {
                    msgParamVo.setLicenseNo(vo.getItemsContent());
                    if (!"1".equals(vo.getLossitemType())) {
                        msgParamVo.setBrandName("");
                        msgParamVo.setInsuredDate("");
                        msgParamVo.setLosspart("");
                        msgParamVo.setKindCode("");
                    }
                    break;
                }
            }

        }
        //???????????????--?????????
        if (prpLRegistVo.getRepairId() != null) {
            PrpLRepairFactoryVo prpLRepairFactoryVo = repairFactoryService.findFactoryById(Long.toString(prpLRegistVo.getRepairId()));
            if (prpLRepairFactoryVo != null && prpLRepairFactoryVo.getId() != null) {
                if (prpLRepairFactoryVo.getFactoryName() != null) {
                    msgParamVo.setRepairName(prpLRepairFactoryVo.getFactoryName());
                }
                if (prpLRepairFactoryVo.getAddress() != null) {
                    String areaAdress = "";
                    if (StringUtils.isNotBlank(prpLRepairFactoryVo.getAreaCode())) {
                        areaAdress = codeTranService.transCode("AreaCode", prpLRepairFactoryVo.getAreaCode());
                    }
                    if (StringUtils.isNotBlank(areaAdress)) {
                        msgParamVo.setRepairAddress(areaAdress + prpLRepairFactoryVo.getAddress());
                    } else {
                        msgParamVo.setRepairAddress(prpLRepairFactoryVo.getAddress());
                    }
                }
                if (prpLRepairFactoryVo.getTelNo() != null) {
                    msgParamVo.setRepairMobile(prpLRepairFactoryVo.getTelNo());
                }
                if (prpLRepairFactoryVo.getLinker() != null) {
                    msgParamVo.setRepairLinker(prpLRepairFactoryVo.getLinker());
                }
                // ?????????????????????
                if (prpLRepairFactoryVo.getAgentPhone() != null) {
                    msgParamVo.setAgentPhone(prpLRepairFactoryVo.getAgentPhone());
                }
            }
        } else {//????????????
            if (prpLCMainVo != null && prpLCMainVo.getComCode() != null) {
                PrpLRepairFactoryVo repairFactoryVo = repairFactoryService.findFactory(prpLCMainVo.getComCode(),
                        prpLCMainVo.getAgentCode(), prpLCMainVo.getInsuredCode(), prpLCMainVo.getOperatorCode());
                if (repairFactoryVo != null && repairFactoryVo.getId() != null) {
                    if (repairFactoryVo.getFactoryName() != null) {
                        msgParamVo.setRepairName(repairFactoryVo.getFactoryName());
                    }
                    if (repairFactoryVo.getAddress() != null) {
                        String areaAdress = "";
                        if (StringUtils.isNotBlank(repairFactoryVo.getAreaCode())) {
                            areaAdress = codeTranService.transCode("AreaCode", repairFactoryVo.getAreaCode());
                        }
                        if (StringUtils.isNotBlank(areaAdress)) {
                            msgParamVo.setRepairAddress(areaAdress + repairFactoryVo.getAddress());
                        } else {
                            msgParamVo.setRepairAddress(repairFactoryVo.getAddress());
                        }
                    }
                    if (repairFactoryVo.getTelNo() != null) {
                        msgParamVo.setRepairMobile(repairFactoryVo.getTelNo());
                    }
                    if (repairFactoryVo.getLinker() != null) {
                        msgParamVo.setRepairLinker(repairFactoryVo.getLinker());
                    }
                    //?????????????????????
                    if (repairFactoryVo.getAgentPhone() != null) {
                        msgParamVo.setAgentPhone(repairFactoryVo.getAgentPhone());
                    }
                }
            }
        }

        //????????????
        Map<String, String> registTimesMap = registService.getRegistTimesByPolicyNo(prpLRegistVo.getPolicyNo());
        msgParamVo.setRegistTimes_BI(registTimesMap.get("registTimes_BI"));
        msgParamVo.setRegistTimes_CI(registTimesMap.get("registTimes_CI"));

        List<PrpLCMainVo> prpLCMainVoList  = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
        for (PrpLCMainVo prpl : prpLCMainVoList) {

		}

        return msgParamVo;
    }

    /**
     * ??????????????????
     *
     * @param cMainList
     * @return
     */
    public String getInsuredDate(List<PrpLCMainVo> cMainList) {
        String insuredDate = "";
        for (PrpLCMainVo prpLCMain : cMainList) {
            if ("1101".equals(prpLCMain.getRiskCode())) {
                insuredDate = insuredDate + "(??????)" + DateUtils.dateToStr(prpLCMain.getStartDate(), "yyyy-MM-dd") +
                        "???" + DateUtils.dateToStr(prpLCMain.getEndDate(), "yyyy-MM-dd");
            } else {
                insuredDate = insuredDate + "(??????)" + DateUtils.dateToStr(prpLCMain.getStartDate(), "yyyy-MM-dd") +
                        "???" + DateUtils.dateToStr(prpLCMain.getEndDate(), "yyyy-MM-dd");
            }
        }
        return insuredDate;
    }

    public String getLossPart(String code) {
        String lossPart = "";
        String[] temp = code.split(",");
        for (int i = 0; i < temp.length; i++) {
            if ("1".equals(temp[i])) {
                lossPart = lossPart + "??????";
            } else if ("2".equals(temp[i])) {
                lossPart = lossPart + "??????";
            } else if ("3".equals(temp[i])) {
                lossPart = lossPart + "??????";
            } else if ("4".equals(temp[i])) {
                lossPart = lossPart + "??????";
            } else if ("5".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("8".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("6".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("9".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("7".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("10".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            } else if ("11".equals(temp[i])) {
                lossPart = lossPart + "?????????";
            }
        }
        if (lossPart != "" || !"".equals(lossPart)) {
            lossPart = lossPart.substring(0, lossPart.length() - 1);
        }
        return lossPart;
    }

    //??????????????????yzy
    private void putSmsmessage(SendMsgParamVo msgParamVo, String moble, Date sendTime0, String smsContext, String status) {
        //DateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date trueSendTime = new Date();//??????????????????
        Date sendTime1 = null;
        Date nowTime = new Date();
        if (sendTime0 != null) {
            sendTime1 = DateUtils.addMinutes(sendTime0, -5);//????????????????????????
            if (nowTime.getTime() < sendTime1.getTime()) {
                trueSendTime = sendTime1;
            }
        }
        PrpsmsMessageVo prpsmsMessageVo = new PrpsmsMessageVo();
        if (msgParamVo != null) {
            prpsmsMessageVo.setBusinessNo(msgParamVo.getRegistNo());
            prpsmsMessageVo.setComCode(msgParamVo.getComCode());
            prpsmsMessageVo.setCreateTime(nowTime);
            prpsmsMessageVo.setPhoneCode(moble);
            prpsmsMessageVo.setSendNodecode(FlowNode.Sched.toString());
            prpsmsMessageVo.setSendText(smsContext);
            prpsmsMessageVo.setTruesendTime(trueSendTime);
            prpsmsMessageVo.setUserCode(msgParamVo.getUseCode());
            prpsmsMessageVo.setBackTime(nowTime);
            prpsmsMessageVo.setStatus(status);
        }
        msgModelService.saveorUpdatePrpSmsMessage(prpsmsMessageVo);
    }

}
