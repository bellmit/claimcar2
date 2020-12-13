package ins.sino.claimcar.common.web.action;

import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.IsSingleAccident;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.RevokeBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeTaskInfoVo;
import ins.sino.claimcar.carchild.vo.ScheduleItemVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfFlowService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqDOrGBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemDOrG;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * 
 * @author dengkk
 *
 */
@Controller
@RequestMapping("/handoverTask")
public class HandOverTaskAction {

	private static Logger logger = LoggerFactory.getLogger(HandOverTaskAction.class);
	@Autowired
	private WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	PrpLCMainService prpLCMainService;
	
	@Autowired
	MobileCheckService mobileCheckService;
	
    @Autowired
    WfMainService wfMainService;
    
    @Autowired
	SysUserService sysUserService;
    
    @Autowired
    private SendMsgToMobileService sendMsgToMobileService;
    @Autowired
    CarchildService carchildService;
    @Autowired
    LossCarService lossCarService;
    @Autowired
    PropLossService propLossService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ClaimInterfaceLogService claimInterfaceLogService;
    @Autowired
    PolicyViewService policyViewService;
    
    @Autowired
    ManagerService managerService;
    
    @Autowired
    ScheduleTaskService scheduleTaskService;
    
    @Autowired
    WfFlowService wfFlowService;
    
    @Autowired
    WfTaskQueryService wfTaskQueryService;
    
    @Autowired
    AreaDictService areaDictService;
    
    @Autowired
    PersTraceService persTraceService;
    
    @Autowired
    private WFMobileService wFMobileService;
    @Autowired
    AssessorService assessorService;
    
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	@Autowired
	RegistService registService;
	public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
    public static final String INSCOMCODE = "DHIC";
    public static final String INSCOMPANY = "鼎和财产保险股份有限公司";
    
	@RequestMapping(value = "/handoverTaskEdit.do", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView handoverTaskEdit(@RequestParam(value = "flowTaskId")Double flowTaskId,String queryRange) {
		ModelAndView mav = new ModelAndView();
		PrpLWfTaskVo  prpLWfTaskVo = wfTaskHandleService.queryTask(flowTaskId);
		//如果已经不是未处理或者不是正在处理
		if(!"0".equals(prpLWfTaskVo.getHandlerStatus()) && !"2".equals(prpLWfTaskVo.getHandlerStatus())){
			throw new IllegalArgumentException(prpLWfTaskVo.getTaskName()+"任务已不是未处理或正在处理状态");
		}
		String nodeCode = prpLWfTaskVo.getNodeCode();
		String subNodeCode = prpLWfTaskVo.getSubNodeCode();
		FlowNode flowNode = Enum.valueOf(FlowNode.class, subNodeCode);
		String gradeId = flowNode.getRoleCode();
		//损失方：只有车、物的定损、核价、核损节点的处理页面显示此元素
		if(nodeCode.equals(FlowNode.DLoss.name()) || nodeCode.equals(FlowNode.VLoss.name()) ||  nodeCode.equals(FlowNode.VPrice.name())){
			String itemName = prpLWfTaskVo.getItemName();
			mav.addObject("itemName",itemName);
		}
		if(StringUtils.isNotBlank(subNodeCode) && (subNodeCode.contains(FlowNode.DLCar.name()) || subNodeCode.startsWith("VPCar") || subNodeCode.startsWith("VLCar"))){
			String licenseNo=prpLWfTaskVo.getItemName();
			if(StringUtils.isNotBlank(prpLWfTaskVo.getItemName()) && ( prpLWfTaskVo.getItemName().startsWith("标的") || prpLWfTaskVo.getItemName().startsWith("三者"))){
				licenseNo=prpLWfTaskVo.getItemName().substring(3);
			}
			mav.addObject("licenseNo",licenseNo);
		}
		String comCode = WebUserUtils.getComCode();
		if(comCode.startsWith("0001")){//总公司特殊处理
			comCode = "00000000";
		}else if(comCode.startsWith("00")){
			comCode = comCode.substring(0, 4);
			comCode = comCode + "0000";
		}else{
			comCode = comCode.substring(0, 2);
			comCode = comCode + "000000";
		}
		mav.addObject("comCode",comCode);
		mav.addObject("userCode",WebUserUtils.getUserCode());
		mav.addObject("prpLWfTaskVo",prpLWfTaskVo);
		mav.addObject("gradeId",gradeId);
		mav.addObject("queryRange",queryRange);
		String forwordPage = "handoverTask/handoverTaskEdit";
		mav.setViewName(forwordPage);
		return mav;
	}
	
	
	@RequestMapping(value = "/handoverTaskSubmit.do")
	@ResponseBody
	public AjaxResult handoverTaskSubmit(@FormModel("wfTaskSubmitVo") WfTaskSubmitVo wfTaskSubmitVo,String handoverTaskReason,String registNo,String assignComs){
		wfTaskSubmitVo.setTaskInUser(WebUserUtils.getUserCode());
		BigDecimal flowTaskId = wfTaskSubmitVo.getFlowTaskId();
		PrpLWfTaskVo prpLWfTaskInVo = wfFlowService.findPrpLWfTaskQueryByTaskId(flowTaskId);
		String currentNode="";//移交，如果存在对应的公估费，则把公估费状态回写成7
		if(wfTaskSubmitVo.getCurrentNode()!=null){
			currentNode=wfTaskSubmitVo.getCurrentNode().name();
			if(FlowNode.Chk.name().equals(currentNode)){
				assginMothod(registNo, "0", "0", "");
			}else if(currentNode.contains(FlowNode.DLCar.name()) || (currentNode.startsWith("VPCar") || currentNode.startsWith("VLCar"))){
				assginMothod(registNo, "1", "0", wfTaskSubmitVo.getLicenseNo());
			}else if(FlowNode.DLProp.name().equals(currentNode) || currentNode.startsWith("VLProp")){
				assginMothod(registNo, "2", "0", "");
			}else if(currentNode.startsWith("PL")){
				assginMothod(registNo, "3", "0", "");
			}
		}
		//获取报案信息
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
        //自助理赔不送公估（车童网/民太安）
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
        //是否移动端案件
		Boolean isMobileWhileListCase = false;
        String cTMTACheck = "0";
        Boolean cTMTAFlags = false;//是否能送公估
        Boolean isUpdateMobileAccept = false;//是否需要更新isMobileAccept为0，false不要更新
    	if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLCar.name()) || wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLProp.name())
				|| wfTaskSubmitVo.getCurrentNode().equals(FlowNode.PLFirst.name()) || wfTaskSubmitVo.getCurrentNode().equals(FlowNode.PLNext.name())
				||wfTaskSubmitVo.getCurrentNode().equals(FlowNode.Chk.name())){
            //移动端案件，不送民太安车童接口
    		isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,wfTaskSubmitVo.getAssignUser());
    		PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(wfTaskSubmitVo.getAssignUser());
    		if(prplIntermMainVo != null && StringUtils.isNotBlank(prpLRegistVo.getSelfClaimFlag()) &&
        			CodeConstants.IsSingleAccident.YES.equals(prpLRegistVo.getSelfClaimFlag())){
        		AjaxResult ajaxResult = new AjaxResult();
  				ajaxResult.setData("自助理赔案件不能移交公估！");
  				ajaxResult.setStatus(org.apache.http.HttpStatus.SC_OK);
  				return ajaxResult;
        	}
            if(!isMobileWhileListCase){//如果不是白名单的公估
                 if(prplIntermMainVo!=null&&"0003".equals(prplIntermMainVo.getIntermCode())){
                     if(configValueCTCheckVo!=null && "1".equals(configValueCTCheckVo.getConfigValue())){
                         cTMTACheck = "1";
                     }
                 }else if(prplIntermMainVo!=null && "0005".equals(prplIntermMainVo.getIntermCode())){
                     if(configValueMTACheckVo!=null && "1".equals(configValueMTACheckVo.getConfigValue())){
                         cTMTACheck = "1";
                     }
                 }else{
                	 isUpdateMobileAccept = true;
                 }
                 if(prplIntermMainVo != null && "1".equals(cTMTACheck)){
                     if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                    	 cTMTAFlags = true;
                     }
                 }
            }
    	}

        
		SysUserVo sysUserVo= sysUserService.findByUserCode(wfTaskSubmitVo.getAssignUser());
		wfTaskSubmitVo.setAssignCom(sysUserVo.getComCode());
		String  assignName= CodeTranUtil.transCode("ComCode", assignComs);
		//判断是否为代查勘案件
		String subCheckFlag = wfTaskQueryService.getSubCheckFlag(registNo, wfTaskSubmitVo.getAssignUser(),null);
		wfTaskSubmitVo.setSubCheckFlag(subCheckFlag);
		PrpLWfTaskVo prpLWfTaskVo =wfTaskHandleService.handOverTask(wfTaskSubmitVo,handoverTaskReason);

        
        //车童/民太安
        RegistInformationVo registInformationVo = new RegistInformationVo();
		//平级移交查勘任务、定损任务、人伤任务的时候发送报文
		if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLCar.name()) || wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLProp.name())
				|| wfTaskSubmitVo.getCurrentNode().equals(FlowNode.PLFirst.name()) || wfTaskSubmitVo.getCurrentNode().equals(FlowNode.PLNext.name())
				||wfTaskSubmitVo.getCurrentNode().equals(FlowNode.Chk.name())){
			
			HeadReq head = setHeadReq();//设置头部信息
			
			HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
			HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
			PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLRegistVo.getRegistNo(), prpLRegistVo.getPolicyNo());
			scheduleDOrG.setRegistNo(registNo);
			scheduleDOrG.setScheduleType("2");
			setScheduleDOrG(scheduleDOrG, registNo);
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        
			//判断当前工号有无自助查勘岗的Id----5195
			String powerFlag = scheduleService.findSelfCheckPower(wfTaskSubmitVo.getAssignUser());
			if(IsSingleAccident.YES.equals(prpLRegistVo.getSelfClaimFlag()) &&
					!IsSingleAccident.YES.equals(powerFlag)){
				prpLRegistVo.setSelfClaimFlag(IsSingleAccident.NOT);
				registService.saveOrUpdate(prpLRegistVo);
			}
		    if(IsSingleAccident.NOT.equals(powerFlag)){
	        	selfScheduleTaskVo.setIsAutoCheck("0");
	        }
			
	        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//电话直赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//微信自助理赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{
				//是否移动端案件
				Boolean	isMobileCase = false;
				//移动端案件，不送民太安车童接口
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
				if(!isMobileWhileListCase){
					isMobileCase = sendMsgToMobileService.isMobileCase(prpLRegistVo, wfTaskSubmitVo.getAssignUser());
				}else{
					isMobileCase = true;
				}
				if("1".equals(configValueVo.getConfigValue())){
	          	    if(isMobileCase){//移动端案件
	                    scheduleDOrG.setIsMobileCase("1");
	                }else{
	                    scheduleDOrG.setIsMobileCase("0");
	                }
				}else{
	              scheduleDOrG.setIsMobileCase("0");
				}
	        }
	        
			scheduleDOrG.setDamageTime(prpLRegistVo.getDamageTime());
			scheduleDOrG.setDamagePlace(prpLRegistVo.getDamageAddress());
			if(vo!=null){
				scheduleDOrG.setInuredName(vo.getInsuredName());
			}
			scheduleDOrG.setInuredPhone(prpLRegistVo.getInsuredPhone());
			scheduleDOrG.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
			scheduleDOrG.setPolicyNo(prpLRegistVo.getPolicyNo());
			
			scheduleDOrG.setReportorName(prpLRegistVo.getReportorName());
			scheduleDOrG.setReportorPhone(prpLRegistVo.getReportorPhone());
			scheduleDOrG.setReportTime(prpLRegistVo.getReportTime());
			
	        //自助理赔start
	        PrpLRegistExtVo registExtVo = prpLRegistVo.getPrpLRegistExt();
	        scheduleDOrG.setAccidentType(registExtVo.getAccidentTypes());
	        scheduleDOrG.setDamageCode(prpLRegistVo.getDamageCode());
	        scheduleDOrG.setWeather("");
	        scheduleDOrG.setAccidentDesc(registExtVo.getDangerRemark());
	        scheduleDOrG.setDutyType(registExtVo.getObliGation());
	        scheduleDOrG.setInsComcode(INSCOMCODE);
	        scheduleDOrG.setInsCompany(INSCOMPANY);
	        PrpLCItemCarVo prpLCItemCarVo = registQueryService.findCItemCarByRegistNo(registNo);
	        scheduleDOrG.setFrameNo(prpLCItemCarVo.getFrameNo());
	        scheduleDOrG.setEngineNo(prpLCItemCarVo.getEngineNo());
	        scheduleDOrG.setCarownName(prpLCItemCarVo.getCarOwner());
	        scheduleDOrG.setCarownPhone("");
	        //自助理赔end
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			//任务id
			int id = 1;
			HandleScheduleReqScheduleItemDOrG scheduleItemDOrG =new HandleScheduleReqScheduleItemDOrG();
			ScheduleItemVo scheduleItem =new ScheduleItemVo();
			List<ScheduleItemVo> scheduleItemVoList = new ArrayList<ScheduleItemVo>();
			if(prpLWfTaskVo != null){
			    scheduleItemDOrG.setTaskId(String.valueOf(prpLWfTaskVo.getTaskId()));

			    //车童民太安
		        PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
			    if("0".equals(prpLWfTaskVo.getHandlerStatus())){
			        scheduleItem.setTaskId(prpLWfTaskVo.getHandlerIdKey());
			        if(FlowNode.DLoss.name().equals(prpLWfTaskVo.getNodeCode())){
			        	PrpLScheduleDefLossVo ItemsVo = null;
			        	if(CodeConstants.WorkStatus.INIT.equals(prpLWfTaskInVo.getWorkStatus())){
			        		ItemsVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
			        	}else{
			        		PrpLDlossCarMainVo carMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTaskInVo.getHandlerIdKey()));
			        		ItemsVo = scheduleService.findScheduleDefLossByPk(carMainVo.getScheduleDeflossId());
			        	}
			            
	                    if("0".equals(ItemsVo.getLossitemType())){
	                        scheduleItem.setItemNo("0");
	                        scheduleItem.setItemNoName("地面");
	                    }else if("1".equals(ItemsVo.getLossitemType())){
	                        scheduleItem.setItemNo("1");
	                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
	                    }else{
	                        scheduleItem.setItemNo("2");
	                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
	                    }
	                    
			            Long  taskId = scheduleService.findTaskIdByDefLossId(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
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
			            PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
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
			    }else if ("2".equals(prpLWfTaskVo.getHandlerStatus())) {
			    	PrpdIntermMainVo intermMainVo = managerService.findIntermByUserCode(wfTaskSubmitVo.getAssignUser());
                    if(FlowNode.DLoss.name().equals(prpLWfTaskVo.getNodeCode())){
                        if(FlowNode.DLCar.name().equals(prpLWfTaskVo.getSubNodeCode())){
                            PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                            scheduleItem.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                            
                            if(intermMainVo !=null){
                            	prpLDlossCarMainVo.setIntermFlag("1");//公估定损
                            	prpLDlossCarMainVo.setIntermCode(intermMainVo.getIntermCode());//公估机构代码
                    		}else{
                    			prpLDlossCarMainVo.setIntermFlag("0");
                    		}
                            lossCarService.updateJyDlossCarMain(prpLDlossCarMainVo);
                            
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
                            //待续
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
                        }else if(FlowNode.DLProp.name().equals(prpLWfTaskVo.getSubNodeCode())){
                            PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                            /*revokeTaskInfoVo.setTaskId(prpLdlossPropMainVo.getScheduleTaskId().toString());*/
                            //根据序号，报案号，定损类别查询
                            PrpLScheduleDefLossVo defLossVo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                            scheduleItem.setTaskId(defLossVo.getId().toString());
                            
                            if(intermMainVo !=null){
                            	prpLdlossPropMainVo.setInterMediaryFlag("1");//公估定损
                            	prpLdlossPropMainVo.setInterMediaryinfoId(intermMainVo.getIntermCode());//公估机构代码对应 prpdintermmain.intermCode
                    		}else{
                    			prpLdlossPropMainVo.setInterMediaryFlag("0");
                    		}
                            propLossService.saveOrUpdatePropMain(prpLdlossPropMainVo, WebUserUtils.getUser());
                            
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
                    	if(FlowNode.PLoss.name().equals(prpLWfTaskVo.getNodeCode())){
                    		PrpLDlossPersTraceMainVo persTraceMainVo = persTraceService.findPersTraceMainVoById(Long.decode(prpLWfTaskVo.getHandlerIdKey()));
                    		if(FlowNode.PLFirst.name().equals(prpLWfTaskVo.getSubNodeCode())){//只有人伤首次跟踪才修改人伤定损标识
                        		if(intermMainVo !=null){
                        			persTraceMainVo.setIntermediaryFlag("1");// 公估定损
                        			persTraceMainVo.setIntermediaryInfoId(intermMainVo.getIntermCode());// 公估机构代码
                        		}else{
                        			persTraceMainVo.setIntermediaryFlag("0");// 司内定损
                        		}
                    		}
                    		persTraceService.saveOrUpdatePersTraceMain(persTraceMainVo);
                    	}
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(registNo,"1","1");
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
			    scheduleItemDOrG.setTaskId(String.valueOf(id));
			    scheduleItem.setTaskId(String.valueOf(id));
			    scheduleItem.setDamageAddress(prpLRegistVo.getDamageAddress());
			}
			scheduleItemDOrG.setOriginalTaskId(String.valueOf(wfTaskSubmitVo.getFlowTaskId()));
			//注意：第一版先写死标的序号，标的名称，是否标的
			if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLCar.name())){
				//车辆
				scheduleItemDOrG.setNodeType("DLCar");
				scheduleItemDOrG.setItemNo("1");
				scheduleItemDOrG.setItemNo("标的车");
				scheduleItemDOrG.setIsObject("1");
				
				scheduleItem.setNodeType("DLCar");
				/*scheduleItem.setItemNo("1");
				scheduleItem.setItemNoName("标的车");*/
			}else if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLProp.name())){
				//财产
				scheduleItemDOrG.setNodeType("DLProp");
				scheduleItemDOrG.setItemNo("1");
				scheduleItemDOrG.setItemNo("标的财");
				scheduleItemDOrG.setIsObject("1");
				
				scheduleItem.setNodeType("DLProp");
				/*scheduleItem.setItemNo("1");
				scheduleItem.setItemNoName("标的财");*/
			}else if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.Chk.name())){
				//查勘
				scheduleItemDOrG.setNodeType("Check");
				
				scheduleItem.setNodeType("Check");
			}else{
				//人伤
				scheduleItemDOrG.setNodeType("PLoss");
				
				scheduleItem.setNodeType("PLoss");
			}
			/*String  nextHandlerName= CodeTranUtil.transCode("UserCode", wfTaskSubmitVo.getAssignUser());
			String  assignName= CodeTranUtil.transCode("ComCode", wfTaskSubmitVo.getAssignCom());*/
			if("3".equals(scheduleDOrG.getCaseFlag())){
				scheduleItemDOrG.setNextHandlerCode(wfTaskSubmitVo.getAssignUser());
				scheduleItemDOrG.setNextHandlerName(sysUserVo.getUserName());
				scheduleItemDOrG.setScheduleObjectId(assignComs);
				scheduleItemDOrG.setScheduleObjectName(assignName);
			}else{
				scheduleItemDOrG.setNextHandlerCode("");
				scheduleItemDOrG.setNextHandlerName("");
				scheduleItemDOrG.setScheduleObjectId("");
				scheduleItemDOrG.setScheduleObjectName("");
			}
			
			scheduleItemList.add(scheduleItemDOrG);
			
			scheduleDOrG.setScheduleItemList(scheduleItemList);
			body.setScheduleDOrG(scheduleDOrG);
			reqVo.setHead(head);
			reqVo.setBody(body);
			
			
			scheduleItem.setNextHandlerCode(wfTaskSubmitVo.getAssignUser());
			scheduleItem.setNextHandlerName(sysUserVo.getUserName());
			scheduleItem.setScheduleObjectId(assignComs);
			scheduleItem.setScheduleObjectName(assignName);
			scheduleItemVoList.add(scheduleItem);
			registInformationVo.setScheduleItemList(scheduleItemVoList);
			
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+HANDLSCHEDDORGULE_URL_METHOD;
			try {
				mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
        
		if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLCar.name()) || wfTaskSubmitVo.getCurrentNode().equals(FlowNode.DLProp.name())
                ||wfTaskSubmitVo.getCurrentNode().equals(FlowNode.Chk.name())){

            String cTMTACheckCancel = "0";
		    String flags = "1";//民太安车童送撤销接口是否成功标识
	        if( "2".equals(prpLWfTaskInVo.getIsMobileAccept()) || "3".equals(prpLWfTaskInVo.getIsMobileAccept())){    
	            String url = null;
	            RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
	            CarchildHeadVo head = new CarchildHeadVo();
	            RevokeBodyVo body = new RevokeBodyVo();
	            List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	            Date date = new Date();
	            String timeStamp =  dateFormat.format(date);
	            BusinessType businessType = null;
	            if(configValueCTCheckVo != null && "2".equals(prpLWfTaskInVo.getIsMobileAccept()) && "1".equals(configValueCTCheckVo.getConfigValue())){
	                //理赔请求车童网  
	                head.setRequestType("CT_006");
	                head.setUser("claim_user");
	                head.setPassWord("claim_psd");
	                url = SpringProperties.getProperty("");
	                businessType = BusinessType.CT_handOver;
	                cTMTACheckCancel = "1";
	            }else if(configValueMTACheckVo != null && "3".equals(prpLWfTaskInVo.getIsMobileAccept()) && "1".equals(configValueMTACheckVo.getConfigValue())){
	                //理赔请求民太安
	                head.setRequestType("MTA_006");
	                head.setUser("claim_user");
	                head.setPassWord("claim_psd");
	                url = SpringProperties.getProperty("");
	                businessType = BusinessType.MTA_handOver;
	                cTMTACheckCancel = "1";
	            }
	            reqVo.setHead(head);                 
	            RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
                if("0".equals(prpLWfTaskVo.getHandlerStatus())){
                    revokeTaskInfoVo.setTaskId(prpLWfTaskVo.getHandlerIdKey());
                }else if ("2".equals(prpLWfTaskVo.getHandlerStatus())) {
                    if(FlowNode.DLoss.name().equals(prpLWfTaskVo.getNodeCode())){
                        if(FlowNode.DLCar.name().equals(prpLWfTaskVo.getSubNodeCode())){
                            PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                            revokeTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                        }else if(FlowNode.DLProp.name().equals(prpLWfTaskVo.getSubNodeCode())){
                            PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                            //根据序号，报案号，定损类别查询
                            PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                            revokeTaskInfoVo.setTaskId(vo.getId().toString());
                        }
                    }else {
                        //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(registNo,ScheduleStatus.CHECK_SCHEDULED);
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(registNo,"1","1");
                        revokeTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
                    }
                }     
	            revokeTaskInfoVo.setRegistNo(registNo);
	            if(wfTaskSubmitVo.getCurrentNode().equals(FlowNode.Chk.name())){
	                revokeTaskInfoVo.setNodeType(wfTaskSubmitVo.getCurrentNode().getUpperNode());
	            }else{
	                revokeTaskInfoVo.setNodeType(wfTaskSubmitVo.getCurrentNode().name());
	            }
	            
	            revokeTaskInfoVo.setRevokeType("2");
	            revokeTaskInfoVo.setReason("");
	            revokeTaskInfoVo.setRemark(handoverTaskReason);
	            //新增字段
	            revokeTaskInfoVo.setNewHandlerUser(wfTaskSubmitVo.getAssignUser());
	            revokeTaskInfoVo.setNewTaskId(revokeTaskInfoVo.getTaskId());
	            revokeTaskInfoVo.setTimeStamp(timeStamp);
	            revokeTaskInfoVos.add(revokeTaskInfoVo);
	            body.setRevokeTaskInfos(revokeTaskInfoVos);
	            reqVo.setBody(body);
	            url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//请求地址
                logger.info("url=============="+url);
	            RevokeInfoResVo resVo = new RevokeInfoResVo();
	            if("1".equals(cTMTACheckCancel)){
                    try{
                        resVo = carchildService.sendRevokeInformation(reqVo,url);
                    }
                    catch(Exception e){
                        flags = "0";
                        e.printStackTrace();
                    }
                    finally{
                        //交互日志保存
                        SysUserVo userVo = new SysUserVo();
                        userVo.setComCode(WebUserUtils.getComCode());
                        userVo.setUserCode(WebUserUtils.getUserCode());
                        RegistInformationVo informationVo = new RegistInformationVo();
                        informationVo.setRevokeInfoReqVo(reqVo);
                        informationVo.setRevokeInfoResVo(resVo);
                        informationVo.setSchType("1");
                        informationVo.setOperateNode(prpLWfTaskVo.getNodeCode());
                        carchildService.saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
                        if(resVo != null && "1".equals(resVo.getHead().getErrNo())){
                            flags = "1";
                        }else{
                            flags = "0";  
                        }
                    }
                    if("1".equals(flags)){
                    	prpLWfTaskVo.setIsMobileAccept("0");
                    	wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
                    }
	            }
	        }
	        //自助理赔不送公估（车童网/民太安）
	        if(!"1".equals(prpLRegistVo.getSelfClaimFlag()) && !isMobileWhileListCase){//如果不是白名单的公估
		        //理赔报案信息接口（理赔请求车童网/民太安）
		        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
	            try{
	            	if(cTMTAFlags && "1".equals(flags)){
		                PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(wfTaskSubmitVo.getAssignUser());
                        SysUserVo userVo = new SysUserVo();
                        userVo.setComCode(WebUserUtils.getComCode());
                        userVo.setUserCode(WebUserUtils.getUserCode());
                        registInformationVo.setUser(userVo);
                        List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
                        prpLWfTaskVoResult.add(prpLWfTaskVo);
                        carchildService.sendRegistInformation(prplIntermMainVo,prpLRegistVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
	            	}
	            	if(isUpdateMobileAccept){
	            		prpLWfTaskVo.setIsMobileAccept("0");
	                	wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
	            	}
	            }
	            catch(Exception e){
	                e.printStackTrace();
	            }
	        }
		}
		String returnStr ="";
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setStatus(HttpStatus.SC_OK);
		ajaxResult.setData(returnStr);
		
		return ajaxResult;
	}
	
	/**
	 * 车童网/民太安撤销信息交互日志保存
	 * <pre></pre>
	 * @param registNo
	 * @param comCode
	 * @param reqVo
	 * @param resVo
	 * @param url
	 * @param businessType
	 * @modified:
	 * ☆LinYi(2017年10月17日 下午5:49:52): <br>
	 */
    private void saveCarchildInterfaceLog(RevokeInfoReqVo reqVo,RevokeInfoResVo resVo,String url,BusinessType businessType,Date date) {
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        String requestXml = stream.toXML(reqVo);
        String responseXml = stream.toXML(resVo);
        CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
        if("1".equals(carChildResponseHead.getErrNo())){
            logVo.setStatus("1");
            logVo.setErrorCode("true");
        }else {
            logVo.setStatus("0");
            logVo.setErrorCode("false");
        }
        logVo.setErrorMessage(resVo.getHead().getErrMsg());
        logVo.setRegistNo(reqVo.getBody().getRevokeTaskInfos().get(0).getRegistNo());
        logVo.setBusinessType(businessType.name());
        logVo.setBusinessName(businessType.getName());
        logVo.setOperateNode(FlowNode.Handover.name());
        logVo.setComCode(WebUserUtils.getComCode());
        logVo.setRequestTime(date);
        logVo.setRequestUrl(url);
        logVo.setCreateUser(WebUserUtils.getUserCode());
        logVo.setCreateTime(date);
        logVo.setRequestXml(requestXml);
        logVo.setResponseXml(responseXml);
        claimInterfaceLogService.save(logVo);
    }
	
	/**
	 * 设置头部信息
	 * @return
	 */
	private HeadReq setHeadReq(){
		HeadReq head = new HeadReq();
		head.setRequestType("ScheduleSubmit");
		head.setPassword("mclaim_psd");
		head.setUser("mclaim_user");
		return head;
	}
	
	/**
	 * 判断查勘自助案件有没有接收
	 * @param registNo
	 * @return
	 */
	@RequestMapping(value = "/checkAcceptSelfCase.do")
	@ResponseBody
	public AjaxResult checkAcceptSelfCase(String registNo){
		AjaxResult ajaxResult = new AjaxResult();
		String returnStr = "";
		try {
			
			List<PrpLWfTaskVo> wfTaskVos = wfFlowQueryService.findPrpWfTaskVoForIn(registNo,"Check","Chk");
            PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registNo);
            if("1".equals(prpLRegistVo.getSelfClaimFlag()) && wfTaskVos != null && wfTaskVos.size() > 0){
            	PrpLWfTaskVo prpLWfTaskVo = wfTaskVos.get(0);
            	if(StringUtils.isEmpty(prpLWfTaskVo.getAssignUser())){
            		returnStr = "自助理赔案件未接收不能改派和移交!";
    	        	ajaxResult.setData("0");
            	}
            }else{
	        	ajaxResult.setData("1");
	        }
			ajaxResult.setStatus(HttpStatus.SC_OK);
			
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setData("0");
			returnStr = "请求失败";
		}
		ajaxResult.setStatusText(returnStr);
		return ajaxResult;
	}
	/**
	 * 设置业务类型，客户分类，代理人编码，保单归属地
	 * @param scheduleDOrG
	 * @param registNo
	 * @return
	 */
	private HandleScheduleReqScheduleDOrG setScheduleDOrG(HandleScheduleReqScheduleDOrG scheduleDOrG,String registNo){
		PrpLCMainVo cMainVo = new PrpLCMainVo();
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(registNo);
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(prpLCMainVoList.size()==2){
				for(PrpLCMainVo prpLCMainVo : prpLCMainVoList){
					if(("12").equals(prpLCMainVo.getRiskCode().substring(0, 2))){
						cMainVo = prpLCMainVoList.get(0);
					}
				}
			}else{
				cMainVo = prpLCMainVoList.get(0);
			}
		}else{
			scheduleDOrG.setAgentCode("all");
		}
		String businessPlate ="";
		businessPlate = CodeTranUtil.transCode("businessPlate", cMainVo.getBusinessPlate());
		//代理人编码
		if(cMainVo.getAgentCode()!=null){
			scheduleDOrG.setAgentCode(cMainVo.getAgentCode());
		}
		//保单归属地编码
		if(cMainVo.getComCode() != null){
			//保单归属地编码
			scheduleDOrG.setComCode(cMainVo.getComCode());
		}
		
		if(businessPlate !="" && businessPlate != null){
			scheduleDOrG.setBusiNessType(businessPlate);//业务类型
		}else{
			scheduleDOrG.setBusiNessType("all");//业务类型
		}
		//TODO 到时取大客户的值
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(StringUtils.isNotBlank(cMainVo.getAgentName()) ||
						StringUtils.isNotBlank(cMainVo.getAgentCode())){
				scheduleDOrG.setCustomType("2");//客户分类
			}else{
				scheduleDOrG.setCustomType("3");//客户分类
			}
		
		}else{
			scheduleDOrG.setCustomType("all");//客户分类
		}
		List<PrpLCInsuredVo> prpLCInsuredVos = cMainVo.getPrpCInsureds();
		if(prpLCInsuredVos != null && prpLCInsuredVos.size() >0 ){
			for(PrpLCInsuredVo lCInsuredVo : prpLCInsuredVos){
				if("1".equals(lCInsuredVo.getInsuredFlag())){
					scheduleDOrG.setIdentifyType(lCInsuredVo.getIdentifyType());//被保人证件类型
					scheduleDOrG.setIdentifyNumber(lCInsuredVo.getIdentifyNumber());//被保人证件号码
					break;
				}
			}
		}
		return scheduleDOrG;
	}
	
	public void assginMothod(String registNo,String taskType,String status,String licenseNo){
		PrpLAssessorVo prpLAssessorVo=assessorService.findPrpLAssessorVo(registNo, taskType, status,licenseNo);
		if(prpLAssessorVo!=null){
			prpLAssessorVo.setUnderWriteFlag("7");//移交，将状态位改为移交
			assessorService.updatePrpLAssessor(prpLAssessorVo);
		}
		
	}
}
