package ins.sino.claimcar.moblie.msgNotified.service;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.ScheduleItemVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.MsgNotifiedBody;
import ins.sino.claimcar.flow.vo.MsgNotifiedResBody;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.msgNotified.vo.MsgNotifiedPacket;
import ins.sino.claimcar.moblie.msgNotified.vo.MsgNotifiedResPacket;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;

/**
 * 快赔请求理赔通知接口
 * <pre></pre>
 * @author ★niuqiang
 */
public class MsgNotifiedServiceImpl implements ServiceInterface {

	private static Logger logger = LoggerFactory.getLogger(MsgNotifiedServiceImpl.class);
	
	@Autowired
	private WFMobileService wFMobileService;
    @Autowired
    PolicyViewService policyViewService;
    
    @Autowired
    ManagerService managerService;
    
    @Autowired
    RegistQueryService registQueryService;
    
    @Autowired
    CarchildService carchildService;
    
    @Autowired
    WfFlowService wfFlowService;
    
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    
    @Autowired
    ScheduleTaskService scheduleTaskService;
    
    @Autowired
    ScheduleService scheduleService;
    
    @Autowired
    AreaDictService areaDictService;
    
    @Autowired
    LossCarService lossCarService;
    
    @Autowired
    PropLossService propLossService;
	@Override
	public Object service(String arg0, Object arg1) {
	    init();
		MsgNotifiedResPacket resPacket = new MsgNotifiedResPacket();
		MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		resHead.setResponseCode("16");
		String registNo = "";
		MsgNotifiedBody bodyCtMta = new MsgNotifiedBody();
		MsgNotifiedResBody resBody = new MsgNotifiedResBody();
		String flags = "NO";
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("============终端通知理赔系统接口（快赔请求理赔）接口请求报文："+reqXml);
			MsgNotifiedPacket packet = (MsgNotifiedPacket)arg1;
			MobileCheckHead head = packet.getHead();
			if (!"016".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			MsgNotifiedBody body =packet.getBody();
			bodyCtMta = body;
			Assert.notNull(body, "数据部分为空");
			if(StringUtils.isBlank(body.getRegistNo()))
				throw new IllegalArgumentException(" 报案号为空  ");
			  registNo = body.getRegistNo();
			if(StringUtils.isBlank(body.getNodeType()))
				throw new IllegalArgumentException(" 节点号为空  ");
			if(!"CheckPLossDLCarDLProp".contains(body.getNodeType())){
			    throw new IllegalArgumentException(" 节点错误：此节点非查勘、人伤、财产定损、车辆定损节点  ");
			}
			 String licenseName = "";
			if(!"Check".equals(body.getNodeType()) && !"PLoss".equals(body.getNodeType())){
				if(StringUtils.isBlank(body.getItemNo()))
					throw new IllegalArgumentException(" 标的序号为空  ");
				if(StringUtils.isBlank(body.getItemName()))
					throw new IllegalArgumentException(" 标的名称为空  ");
				if(StringUtils.isBlank(body.getItemNo())){
	                if("0".equals(body.getItemNo())){
	                    licenseName = "地面";
	                }else if("1".equals(body.getItemNo())){
	                    licenseName = "标的车";
	                }else if("2".equals(body.getItemNo())){
	                    licenseName = "三者车";
	                }
	                body.setItemName(licenseName+body.getItemName());
	            }
			}
			if(StringUtils.isBlank(body.getCheckStatus())){
			    throw new IllegalArgumentException(" 案件状态为空  ");
			}else{
			    String workStatus = body.getCheckStatus();
			    if("4".equals(workStatus)){ //已处理
			        body.setCheckStatus("3");
			    }
			    if(!"0254".contains(workStatus)){
			        throw new IllegalArgumentException(" 案件状态错误  ");
			    }
			}
			if("7".equals(body.getType())){
				if(StringUtils.isBlank(body.getHandlerCode()))
					throw new IllegalArgumentException(" 原始处理人编码为空  ");
				if(StringUtils.isBlank(body.getHandlerName()))
					throw new IllegalArgumentException(" 原始处理人名称为空  ");
				if(StringUtils.isBlank(body.getNextHandlerCode()))
					throw new IllegalArgumentException(" 处理人编码为空  ");
				if(StringUtils.isBlank(body.getNextHandlerName()))
					throw new IllegalArgumentException(" 处理人名称为空 ");
			}
			if(StringUtils.isBlank(body.getTaskId()))
				throw new IllegalArgumentException(" 任务ID为空 ");
			if(null == wFMobileService){
			    wFMobileService = (WFMobileService)Springs.getBean(WFMobileService.class);
			}
			Map<String, MsgNotifiedResBody> returnMap = new HashMap<String, MsgNotifiedResBody>();
			String returnMsg = new String();
			
			returnMap = wFMobileService.mobileUpdateWFTaskIn(body);
			for(String key:returnMap.keySet()){
				returnMsg = key;
				resBody = returnMap.get(key);
			}
			if("操作成功".equals(returnMsg.trim())){
			    resHead.setResponseCode("YES");
		        resHead.setResponseMessage("Success");
			}else{
			    resHead.setResponseCode("NO");
                resHead.setResponseMessage(returnMsg);
			}
			resPacket.setHead(resHead);
			resPacket.setResBody(resBody);
		}catch(Exception e){
			resHead.setResponseType("016");
			resHead.setResponseCode("NO");
			resHead.setResponseMessage(e.getMessage());
			resPacket.setHead(resHead);
			logger.info("终端通知理赔系统接口（快赔请求理赔））报错信息： ");
			 e.printStackTrace();
		}
		
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("============终端通知理赔系统接口（快赔请求理赔）接口返回报文："+resXml);
		MobileCheckResponseHead mobileCheckResponseHead = resPacket.getHead();
		flags = mobileCheckResponseHead.getResponseCode();
		if("YES".equals(flags)){
			try{
			    //理赔报案信息接口（理赔请求车童网/民太安）
		        //车童/民太安
		        RegistInformationVo registInformationVo = new RegistInformationVo();
		        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
		        //获取报案信息
		        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
		        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
		        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
		        logger.info(resBody.getRegistNo()+"============resBody.getNewTaskId()==========="+resBody.getNewTaskId());
		        if(StringUtils.isNotBlank(resBody.getNewTaskId())){
		            PrpLWfTaskVo prpLWfTaskInVo = wfFlowService.findPrpLWfTaskQueryByTaskId(new BigDecimal(resBody.getNewTaskId()));
		            if(prpLWfTaskInVo != null){
		                String cTMTACheck = "0";
		                
		                //出险地址
		                ScheduleItemVo scheduleItem =new ScheduleItemVo();
		                scheduleItem = setScheduleItemVo(scheduleItem,prpLWfTaskInVo,prpLRegistVo);
		                if(FlowNode.DLoss.name().equals(prpLWfTaskInVo.getNodeCode())){
		                   if(FlowNode.DLCar.name().equals(prpLWfTaskInVo.getSubNodeCode())){
		                       scheduleItem.setNodeType("DLCar");
		                   }else{
		                       scheduleItem.setNodeType("DLProp");
		                   }
		                }else{
		                   scheduleItem.setNodeType("Check");
		                }
		                PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(bodyCtMta.getNextHandlerCode());
	                    if(prplIntermMainVo!=null&&"0003".equals(prplIntermMainVo.getIntermCode())){
	                        if(configValueCTCheckVo!=null && "1".equals(configValueCTCheckVo.getConfigValue())){
	                            cTMTACheck = "1";
	                        }
	                    }else if(prplIntermMainVo!=null && "0005".equals(prplIntermMainVo.getIntermCode())){
	                        if(configValueMTACheckVo!=null && "1".equals(configValueMTACheckVo.getConfigValue())){
	                            cTMTACheck = "1";
	                        }
	                    }else{
	                        prpLWfTaskInVo.setIsMobileAccept("0");
	                        wfTaskHandleService.updateTaskIn(prpLWfTaskInVo);
	                    }
	                    //待续

	                    List<ScheduleItemVo> scheduleItemVoList = new ArrayList<ScheduleItemVo>();
	                    scheduleItem.setNextHandlerCode(bodyCtMta.getNextHandlerCode());
	                    scheduleItem.setNextHandlerName(bodyCtMta.getNextHandlerName());
	                    SysUserVo sysUserVo = scheduleTaskService.findPrpduserByUserCode(bodyCtMta.getNextHandlerCode(),"");
	                    String comCodeName = CodeTranUtil.transCode("ComCode",sysUserVo.getComCode());
	                    scheduleItem.setScheduleObjectId(sysUserVo.getComCode());
	                    scheduleItem.setScheduleObjectName(comCodeName);
	                    scheduleItemVoList.add(scheduleItem);
	                    registInformationVo.setScheduleItemList(scheduleItemVoList);
	                    //待续
	                    Boolean isMobileWhileListCase = true;
	                    if(StringUtils.isNotBlank(bodyCtMta.getNextHandlerCode())){
		                    isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,bodyCtMta.getNextHandlerCode());
	                    }
	                    if(!isMobileWhileListCase &&
	                    		prplIntermMainVo != null && "1".equals(cTMTACheck)){
	                        if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
	                            SysUserVo userVo = new SysUserVo();
	                            userVo.setComCode("");
	                            userVo.setUserCode(bodyCtMta.getHandlerCode());
	                            registInformationVo.setUser(userVo);
	                            List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
	                            prpLWfTaskVoResult.add(prpLWfTaskInVo);
	                            carchildService.sendRegistInformation(prplIntermMainVo,prpLRegistVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
	                        }
	                    }
		            }
		        }
		        
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return resPacket;
	}

	private ScheduleItemVo setScheduleItemVo(ScheduleItemVo scheduleItem,PrpLWfTaskVo prpLWfTaskInVo,PrpLRegistVo prpLRegistVo){
	   //任务id
       int id = 1;
       if(prpLWfTaskInVo != null){
            //车童民太安
            PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
            if("0".equals(prpLWfTaskInVo.getHandlerStatus())){
                scheduleItem.setTaskId(prpLWfTaskInVo.getHandlerIdKey());
                if(FlowNode.DLoss.name().equals(prpLWfTaskInVo.getNodeCode())){
                    PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
                    if(ItemsVo.getLossitemType().equals("0")){
                        scheduleItem.setItemNo("0");
                        scheduleItem.setItemNoName("地面");
                    }else if(ItemsVo.getLossitemType().equals("1")){
                        scheduleItem.setItemNo("1");
                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                    }else{
                        scheduleItem.setItemNo("2");
                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                    }

                    Long  taskId = scheduleService.findTaskIdByDefLossId(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                    if(prpLScheduleTaskVo != null){
                        if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//开关
                            String fullName = "";
                            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                            if(sysAreaDictVoList != null){
                                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                fullName = sysAreaDictVo.getFullName();
                                if(!"".equals(fullName)){
                                    fullName = fullName.replaceAll("-","");
                                }
                            }
                            scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//查勘片区
                        }else{
                            scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//查勘片区
                        }
                    }else{
                        scheduleItem.setDamageAddress("");
                    }
                }else{
                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
                    if(prpLScheduleTaskVo != null){
                        if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//开关
                            String fullName = "";
                            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                            if(sysAreaDictVoList != null){
                                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                fullName = sysAreaDictVo.getFullName();
                                if(!"".equals(fullName)){
                                    fullName = fullName.replaceAll("-","");
                                }
                            }
                            scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//查勘片区
                        }else{
                            scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//查勘片区
                        }
                    }else{
                        scheduleItem.setDamageAddress("");
                    }
                }
            }else if ("2".equals(prpLWfTaskInVo.getHandlerStatus())) {
                if(FlowNode.DLoss.name().equals(prpLWfTaskInVo.getNodeCode())){
                    if(FlowNode.DLCar.name().equals(prpLWfTaskInVo.getSubNodeCode())){
                        PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
                        scheduleItem.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                        
                        //待定
                        PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(prpLDlossCarMainVo.getScheduleDeflossId());
                        if(ItemsVo.getLossitemType().equals("0")){
                            scheduleItem.setItemNo("0");
                            scheduleItem.setItemNoName("地面");
                        }else if(ItemsVo.getLossitemType().equals("1")){
                            scheduleItem.setItemNo("1");
                            scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                        }else{
                            scheduleItem.setItemNo("2");
                            scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                        }
                        
                        Long  taskId = scheduleService.findTaskIdByDefLossId(prpLDlossCarMainVo.getScheduleDeflossId());
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                        if(prpLScheduleTaskVo != null){
                            if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//开关
                                String fullName = "";
                                List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                if(sysAreaDictVoList != null){
                                    SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                    fullName = sysAreaDictVo.getFullName();
                                    if(!"".equals(fullName)){
                                        fullName = fullName.replaceAll("-","");
                                    }
                                }
                                scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//查勘片区
                            }else{
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//查勘片区
                            }
                        }else{
                            scheduleItem.setDamageAddress("");
                        }
                    }else if(FlowNode.DLProp.name().equals(prpLWfTaskInVo.getSubNodeCode())){
                        PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
                        /*revokeTaskInfoVo.setTaskId(prpLdlossPropMainVo.getScheduleTaskId().toString());*/
                        //根据序号，报案号，定损类别查询
                        PrpLScheduleDefLossVo defLossVo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                        scheduleItem.setTaskId(defLossVo.getId().toString());
                        
                        if(defLossVo.getLossitemType().equals("0")){
                            scheduleItem.setItemNo("0");
                            scheduleItem.setItemNoName("地面");
                        }else if(defLossVo.getLossitemType().equals("1")){
                            scheduleItem.setItemNo("1");
                            scheduleItem.setItemNoName(defLossVo.getItemsContent());
                        }else{
                            scheduleItem.setItemNo("2");
                            scheduleItem.setItemNoName(defLossVo.getItemsContent());
                        }
                        
                        Long  taskId = scheduleService.findTaskIdByDefLossId(defLossVo.getId());
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                        if(prpLScheduleTaskVo != null){
                            if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//开关
                                String fullName = "";
                                List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                if(sysAreaDictVoList != null){
                                    SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                    fullName = sysAreaDictVo.getFullName();
                                    if(!"".equals(fullName)){
                                        fullName = fullName.replaceAll("-","");
                                    }
                                }
                                scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//查勘片区
                            }else{
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//查勘片区
                            }
                        }else{
                            scheduleItem.setDamageAddress("");
                        }
                    }
                }else {
                    //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLWfTaskVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLRegistVo.getRegistNo(),"1","1");
                    scheduleItem.setTaskId(prpLScheduleTaskVo.getId().toString());
                    if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//开关
                        String fullName = "";
                        List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                        if(sysAreaDictVoList != null){
                            SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                            fullName = sysAreaDictVo.getFullName();
                            if(!"".equals(fullName)){
                                fullName = fullName.replaceAll("-","");
                            }
                        }
                        scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//查勘片区
                    }else{
                        scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//查勘片区
                    }
                    scheduleItem.setDamageAddress(prpLScheduleTaskVo.getDamageAddress());
                }
            }
        }else{
            scheduleItem.setTaskId(String.valueOf(id));
            scheduleItem.setDamageAddress(prpLRegistVo.getDamageAddress());
        }
       return scheduleItem;
	}
	   private void init(){
           if(propLossService == null){
               propLossService = (PropLossService)Springs.getBean(PropLossService.class);
           }
           if(lossCarService == null){
               lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
           }
           if(areaDictService == null){
               areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
           }
           if(scheduleService == null){
               scheduleService = (ScheduleService)Springs.getBean(ScheduleService.class);
           }
           if(scheduleTaskService == null){
               scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
           }
           if(wfTaskHandleService == null){
               wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
           }
           if(wfFlowService == null){
               wfFlowService = (WfFlowService)Springs.getBean(WfFlowService.class);
           }
           if(scheduleTaskService == null){
               scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
           }
           if(carchildService == null){
               carchildService = (CarchildService)Springs.getBean(CarchildService.class);
           }
           if(registQueryService == null){
               registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
           }
           if(managerService == null){
               managerService = (ManagerService)Springs.getBean(ManagerService.class);
           }
           if(policyViewService == null){
               policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
           }
	   }
}
