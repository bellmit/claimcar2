package ins.sino.claimcar.selfHelpClaimCar.service;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.CodeConstants.WorkStatus;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTasklogVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.CaseCarState;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfCaseAcceptReqBodyVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfCaseAcceptReqVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfCaseAcceptResVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfCaseAcceptResponseHead;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

/**
 * 自助案件业务员受理接口
 * @author zhujunde
 *
 */
public class SelfAcceptCaseServiceImpl implements ServiceInterface{
	
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	ScheduleTaskService scheduleTaskService;
	private static Logger logger = LoggerFactory.getLogger(SelfAcceptCaseServiceImpl.class);

	@Override
	public Object service(String arg0, Object arg1) {
		init();
		SelfCaseAcceptResVo resPacket = new SelfCaseAcceptResVo();
		SelfCaseAcceptResponseHead resHead = new SelfCaseAcceptResponseHead();
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=============自助案件业务员受理接口请求报文："+reqXml);
			SelfCaseAcceptReqVo reqPacket = (SelfCaseAcceptReqVo)arg1;
			Assert.notNull(reqPacket, " 请求信息为空  ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"SELFCLAIM_010".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException("请求头参数错误  ");
			}
			SelfCaseAcceptReqBodyVo requestBody = reqPacket.getBody();
			List<CaseCarState> caseCarStates = requestBody.getCaseCarStates();
			for(CaseCarState caseCarState : caseCarStates){
				String registNo = caseCarState.getInscaseNo();
				String scheduledUserCode = caseCarState.getNextHandlerCode();
				String scheduledUserName = caseCarState.getNextHandlerName();
				String scheduledComcode = caseCarState.getScheduleObjectId();
                List<PrpLWfTaskVo> wfTaskCertiVoList = wfFlowQueryService.findPrpWfTaskVoForIn(registNo,"Check","Chk");
                if(wfTaskCertiVoList != null && !wfTaskCertiVoList.isEmpty()){
                	PrpLWfTaskVo prpLWfTaskVo = wfTaskCertiVoList.get(0);
                	prpLWfTaskVo.setAssignUser(scheduledUserCode);
                	prpLWfTaskVo.setAssignCom(scheduledComcode);
                    //更新查勘工作流流程
                    wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
                    PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findPrpLScheduleTaskVoById(Long.valueOf(prpLWfTaskVo.getHandlerIdKey()));	
                    if(scheduleTaskVo != null && scheduleTaskVo.getId() != null){
                        scheduleTaskVo.setScheduledUsercode(scheduledUserCode);
                 		scheduleTaskVo.setScheduledUsername(scheduledUserName);
                 		scheduleTaskVo.setUpdateUser(scheduledUserCode);
                 		for(PrpLScheduleItemsVo itemsVo : scheduleTaskVo.getPrpLScheduleItemses()){
                 			itemsVo.setScheduledUsercode(scheduledUserCode);
                 			itemsVo.setScheduledComcode(scheduledComcode);
                 			itemsVo.setUpdateUser(scheduledUserCode);
                 		}
                 		for(PrpLScheduleTasklogVo tasklogVo : scheduleTaskVo.getPrpLScheduleTasklogs()){
                 			tasklogVo.setScheduledUsercode(scheduledUserCode);
                 			tasklogVo.setScheduledComcode(scheduledComcode);
                 			tasklogVo.setUpdateUser(scheduledUserCode);
                 		}
                 		//更新调度表
                 		scheduleTaskService.saveScheduleTaskByVo(scheduleTaskVo);
                    }
                }
			}
			resHead.setResponseType("SELFCLAIM_010");
			resHead.setErrNo("1");
			resHead.setErrMsg("Success");
			resPacket.setHead(resHead);
		}catch (Exception e){
			resHead.setResponseType("009");
			resHead.setErrNo("NO");
			resHead.setErrMsg(e.getMessage());
			resPacket.setHead(resHead);
			logger.info("自助案件业务员受理接口报错信息： "+e.getMessage(),e);
		}
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=============自助案件业务员受理接口返回报文："+resXml);
		return resPacket;
	}

	private void init(){
		if(wfFlowQueryService == null){
			wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
		}
		if(wfTaskHandleService == null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(scheduleService == null){
			scheduleService = (ScheduleService)Springs.getBean(ScheduleService.class);
		}
		if(scheduleTaskService == null){
			scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
		}
	}
}
