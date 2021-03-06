package ins.sino.claimcar.founder.service;


import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.service.utils.ServiceUserUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.ScheduleStatus;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.util.XmlElementParser;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.founder.vo.CancelDflossTaskBodyVo;
import ins.sino.claimcar.founder.vo.CancelDflossTaskReqOutDateVo;
import ins.sino.claimcar.founder.vo.CancelDflossTaskReqVo;
import ins.sino.claimcar.founder.vo.CarRegistReqBodyVo;
import ins.sino.claimcar.founder.vo.CarRegistReqOutDateVo;
import ins.sino.claimcar.founder.vo.CarRegistReqVo;
import ins.sino.claimcar.founder.vo.CarRegistResVo;
import ins.sino.claimcar.founder.vo.CommonReqHeadVo;
import ins.sino.claimcar.founder.vo.PolicyRelationBodyVo;
import ins.sino.claimcar.founder.vo.PolicyRelationDateVo;
import ins.sino.claimcar.founder.vo.PolicyRelationReqVo;
import ins.sino.claimcar.founder.vo.RegistCancelBodyVo;
import ins.sino.claimcar.founder.vo.RegistCancelCtAtnInputdataVo;
import ins.sino.claimcar.founder.vo.RegistCancelCtAtnReqBodyVo;
import ins.sino.claimcar.founder.vo.RegistCancelCtAtnReqVo;
import ins.sino.claimcar.founder.vo.RegistCancelOutDateVo;
import ins.sino.claimcar.founder.vo.RegistCancelReqVo;
import ins.sino.claimcar.founder.vo.RegistPhoneReqBodyVo;
import ins.sino.claimcar.founder.vo.RegistPhoneReqOutDateVo;
import ins.sino.claimcar.founder.vo.RegistPhoneReqVo;
import ins.sino.claimcar.founder.vo.RegistPhoneResVo;
import ins.sino.claimcar.founder.vo.ScheduleInfoReqBodyVo;
import ins.sino.claimcar.founder.vo.ScheduleInfoReqExamineListVo;
import ins.sino.claimcar.founder.vo.ScheduleInfoReqInputDataVo;
import ins.sino.claimcar.founder.vo.ScheduleInfoReqVo;
import ins.sino.claimcar.founder.webservice.CallFounderWebService;
import ins.sino.claimcar.mobilecheck.service.SendMsgToMobileService;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneResVo;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * ???????????????????????????????????????
 * ????????????????????????
 * @author Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path("founderCustomService")
public class FounderCustomServiceImpl implements FounderCustomService {

	private static Logger logger = LoggerFactory.getLogger(FounderCustomServiceImpl.class);

	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	ClaimInterfaceLogService logService;
	
	@Autowired
	CallFounderWebService callFounderWebService;
	
	@Autowired
	private CodeTranService codeTranService;
	
	@Autowired
	ScheduleTaskService scheduleTaskService;
	
	@Autowired
	WfFlowQueryService wfFlowQueryService;
	
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    
    @Autowired
    private SendMsgToMobileService sendMsgToMobileService;
	/**
	 * XStream---ObjToXml
	 * @param reqVo
	 * @return 0-?????????1-??????
	 * @throws Exception 
	 * @modified:
	 * ???Luwei(2016???8???4??? ??????6:02:07): <br>
	 */
	private String objToXml(Object requestVo,String bussNo) throws Exception{
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		String headXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		
		String requestXml = headXml+stream.toXML(requestVo);
		logger.debug(requestXml+"\n");
		logger.info("?????????????????????===========>" + requestXml);
		
		//??????
		CarRegistResVo returnVo = 
		callFounderWebService.callFounderForClient(requestXml);
		
		//???????????????
		if(returnVo!=null){
			this.saveLog(requestVo,returnVo,requestXml,stream.toXML(returnVo),bussNo);
		}
		
		logger.debug("?????????"+returnVo.getHead().getResponseType());
		return returnVo==null?"0":returnVo.getHead().getResponseType();
	}
	
	/**
	 * ?????????????????????Vo
	 * @param req
	 * @return
	 */
	private RegistPhoneResVo resToxml(RegistPhoneReqVo req)throws Exception{
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		String headXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
		String requestXml=headXml+stream.toXML(req);//?????????????????????Xml
		logger.info("????????????????????????????????????XML"+requestXml+"\n");
		System.out.print("????????????????????????????????????XML"+requestXml+"\n");
		RegistPhoneResVo resVo=callFounderWebService.PhoneFounderForClient(requestXml);
		if(resVo==null){
			logger.info("?????????Vo??????");
		}
				
		return resVo;
	}
	
	/**
	 * <pre>???????????????</pre>
	 * @param returnVo
	 * @param requesXml
	 * @param returnXml
	 * @throws Exception
	 * @modified:
	 * ???Luwei(2016???9???22??? ??????11:32:39): <br>
	 */
	private void saveLog(Object requestVo,CarRegistResVo returnVo,String requesXml,String returnXml,String bussNo) throws Exception{
		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
		
		XmlElementParser xmlElmParser = new XmlElementParser();
		// TODO XuMS ????????????????????????????????????????????????????????????????????????
		xmlElmParser.findElementValue(requestVo,"clmNo","comCode");
		String registNo = xmlElmParser.getString(1);// ??????????????? REPORT_NO
//		String comCode = xmlElmParser.getString(2);// ?????? ??????comCode
		
		String systemCode = returnVo.getHead().getSystemCode();
		
		logVo.setBusinessType(getFounderName(systemCode).toString());
		logVo.setBusinessName(getFounderName(systemCode).getName());
		if(StringUtils.isEmpty(registNo)){
			String clmNo = returnVo.getBody().getOutDate().getClmNo();
			if(StringUtils.isEmpty(clmNo)){
				clmNo = bussNo;
			}
			registNo = clmNo;
		}
		logVo.setRegistNo(registNo);
		logVo.setServiceType(systemCode);
		String operateNode = FlowNode.Regis.toString();
		if("CC1002".equals(systemCode)||"CC1004".equals(systemCode)){
			operateNode = FlowNode.Sched.name();
		}
		logVo.setCompensateNo(bussNo);
		logVo.setOperateNode(operateNode);
		String comCode = policyViewService.getPolicyComCode(registNo);
		if(StringUtils.isEmpty(comCode)){
			comCode = ServiceUserUtils.getComCode();
		}
		logVo.setComCode(comCode);
		logVo.setStatus(returnVo.getHead().getResponseType());
		logVo.setErrorCode(returnVo.getHead().getErrorCode());
		logVo.setErrorMessage(returnVo.getHead().getErrorMessage());
		logVo.setRequestTime(new Date());
		logVo.setRequestUrl(SpringProperties.getProperty("FOUNDER_URL"));
		logVo.setRequestXml(requesXml);
		logVo.setResponseXml(returnXml);
		String userCode = ServiceUserUtils.getUserCode();
		if(StringUtils.isEmpty(userCode)){
			userCode = "0000000000";
		}
		logVo.setCreateUser(userCode);
		//????????????
		ClaimInterfaceLogVo log = logService.save(logVo);
		logger.debug(log.toString());
	}
	
	private BusinessType getFounderName(String systemCode){
		BusinessType name = BusinessType.Founder_carRegist;
		if("CC1001".equals(systemCode)){
			name = BusinessType.Founder_carRegist;
		}else if("CC1002".equals(systemCode)){
			name = BusinessType.Founder_scheduleInfo;
		}else if("CC1003".equals(systemCode)){
			name = BusinessType.Founder_registCancel;
		}else if("CC1004".equals(systemCode)){
			name = BusinessType.Founder_cancelDfloss;
		}else if("CC1005".equals(systemCode)){
			name = BusinessType.Founder_PolicyRelation;
		}else if("CC1011".equals(systemCode)){
            name = BusinessType.Founder_registCancelCtAtn;
        }
		return name;
	}
	
	/* 
	 * @see ins.sino.claimcar.founder.service.
	 * FounderCustomService#carRegistToFounder(java.lang.String)
	 * @param registNo
	 * @throws Exception
	 */
	@Override
	public void carRegistToFounder(String registNo) throws Exception{
		PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
		int x = registQueryService.findRiskInfoByRegistNo(registNo,"1");
		int y = registQueryService.findRiskInfoByRegistNo(registNo,"2");
		boolean shareType = false;
		String policyNo = "";
		String RelaPolicyNo = "";
		//body
        CarRegistReqBodyVo bodyVo = new CarRegistReqBodyVo();
        CarRegistReqOutDateVo outDateVo = new CarRegistReqOutDateVo();
        
        
        
		if("1".equals(registVo.getIsQuickCase())){
		    policyNo = registVo.getPolicyNo();
		    RelaPolicyNo = registVo.getPrpLRegistExt().getPolicyNoLink();
		    outDateVo.setPolicyBeginDate(null);
            outDateVo.setPolicyEndDate(null);       
            outDateVo.setDepartmentNo(registVo.getComCode());//??????ID      
            outDateVo.setOtherBusinessType("");
            outDateVo.setEngineNo("");
            outDateVo.setFrameNo(registVo.getPrpLRegistExt().getFrameNo());
            outDateVo.setInsuredName(registVo.getPrpLRegistExt().getInsuredName());
            outDateVo.setBrandName("");
        }else {
            List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo(registNo);
            PrpLCMainVo cMainVo = cMainVos.get(0);
            
            if(cMainVos!=null&&cMainVos.size()>0){
                policyNo = cMainVo.getPolicyNo();
            }
            if(cMainVos!=null&&cMainVos.size()>1){
                RelaPolicyNo = cMainVos.get(1).getPolicyNo();
            }
            outDateVo.setPolicyBeginDate(cMainVo.getStartDate());
            outDateVo.setPolicyEndDate(cMainVo.getEndDate());       
            outDateVo.setDepartmentNo(cMainVo.getComCode());//??????ID
            shareType = CodeConstants.RadioValue.RADIO_YES.equals
            (cMainVo.getShareHolderFlag()) ? true : false;
            
            String other = cMainVo.getOtherBusinessFlag();
            outDateVo.setOtherBusinessType(StringUtils.isNotBlank(other) ? other : "2");
            outDateVo.setEngineNo(cMainVo.getPrpCItemCars().get(0).getEngineNo());
            outDateVo.setFrameNo(cMainVo.getPrpCItemCars().get(0).getFrameNo());
            outDateVo.setInsuredName(cMainVo.getInsuredName());
            outDateVo.setBrandName(cMainVo.getPrpCItemCars().get(0).getBrandName());
        }
		
		outDateVo.setClmNo(registNo);
        outDateVo.setPolicyNo(policyNo);
		outDateVo.setConnectPolicyNo(RelaPolicyNo); 
        outDateVo.setLinkerName(registVo.getLinkerName());
        outDateVo.setLinkerPhone(registVo.getLinkerMobile());
        outDateVo.setLinkerAddress("");//???????????????
        //????????????????????????
        List<PrpLWfTaskVo> PrpLWfTaskVoList = wfFlowQueryService.findPrpWfTaskVo(registNo, FlowNode.Regis.toString(), FlowNode.Regis.toString());
        if(PrpLWfTaskVoList!=null && PrpLWfTaskVoList.size() > 0){
            if(PrpLWfTaskVoList.get(0).getTaskOutTime()!=null){
                outDateVo.setReportTime(PrpLWfTaskVoList.get(0).getTaskOutTime());
            }
        }
		outDateVo.setLossAmount(0.0);//??????????????????
        outDateVo.setPeril(registVo.getPrpLRegistExt().getDangerRemark());
        outDateVo.setExamineAddress(registVo.getPrpLRegistExt().getCheckAddress());
        outDateVo.setDamageTime(registVo.getDamageTime());
        for(PrpLRegistCarLossVo carVo:registVo.getPrpLRegistCarLosses()){
            if(CodeConstants.LossParty.TARGET.equals(carVo.getLossparty())){
                outDateVo.setLicenseNo(carVo.getLicenseNo());
            }
        }
		outDateVo.setAccidentCourse(registVo.getPrpLRegistExt().getDangerRemark());
		outDateVo.setShareholderType(shareType);
        outDateVo.setBusinessType("01");//cMainVo.getBusinessFlag()
		outDateVo.setDriverName(registVo.getDriverName());//???????????????
        outDateVo.setPerilCount(x+y);
        outDateVo.setRobberyFlag("DM04".equals(registVo.getDamageCode())?"1":"0");
        outDateVo.setIsPersonDeathAccident(registVo.getPrpLRegistExt().getIsPersonLoss());
        outDateVo.setIsGlass("DM02".equals(registVo.getDamageCode())?"1":"0");
        outDateVo.setIsScene(registVo.getPrpLRegistExt().getIsOnSitReport());
        outDateVo.setAgentId(registVo.getFirstRegUserCode());
        outDateVo.setDamageTypeCode("999");
        outDateVo.setCallId(registVo.getCallId());
        
        //???????????????
        outDateVo.setSubCertiType("1");
        outDateVo.setFirstSiteFlag(registVo.getPrpLRegistExt().getIsOnSitReport());

        //????????? ???????????????,??????????????????
        outDateVo.setHappenedAddress(registVo.getDamageAddress());  //????????????
        List<PrpLCItemCarVo> carVo = policyViewService.findPrpcItemcarByRegistNo(registNo);
        outDateVo.setSeatNum(carVo.get(0).getCarType());  //???????????????
		
		CarRegistReqVo reqVo = new CarRegistReqVo();
		
		//??????
		CommonReqHeadVo headVo = new CommonReqHeadVo();
		headVo.setSystemCode("CC1001");
		headVo.setUserCode("");
		headVo.setPassword("");
		
		
		
		//????????????????????????,???????????????
		List<PrpLScheduleTaskVo> scheduleTaskListVo = scheduleTaskService.findScheduleTaskListByRegistNo(registNo);
		PrpLScheduleTaskVo scheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(registNo);
	/*	if(scheduleTaskVo!=null && scheduleTaskVo.getPrpLScheduleItemses().size() > 0){
			for(PrpLScheduleItemsVo vo : scheduleTaskVo.getPrpLScheduleItemses()){
				if("1".equals(vo.getItemType())){//??????
					//??????
					PrpdIntermMainVo prpdIntermMainVo = payCustomService.findIntermByUserCode(vo.getScheduledUsercode());
					if(prpdIntermMainVo!=null){//??????
						if(prpdIntermMainVo.getTelNo()!=null){
							outDateVo.setExaminePhone(prpdIntermMainVo.getTelNo().replace("-", ""));
						}
					}else{//?????????
						PrpLScheduleTaskVo scheduleVo = new PrpLScheduleTaskVo();
						if(scheduleTaskListVo!=null&&scheduleTaskListVo.size()>0){
							for(PrpLScheduleTaskVo scheduleTask :scheduleTaskListVo){
								if("1".equals(scheduleTask.getTypes())){
									scheduleVo = scheduleTask;
								}
							}
						}
						if(scheduleVo != null){
							if(StringUtils.isNoneBlank(scheduleVo.getRelateHandlerMobile())){
							outDateVo.setExaminePhone(scheduleVo.getRelateHandlerMobile().replace("-", ""));
							}
						}
						
					}
					
				}
			}
		}*/
		if(scheduleTaskVo!=null && StringUtils.isNotBlank(scheduleTaskVo.getCallNumber())){
			outDateVo.setExaminePhone(scheduleTaskVo.getCallNumber().replace("-", ""));
		}
		outDateVo.setReportBeginTime(registVo.getReportTime());
		
		
		SysCodeDictVo sysVo = codeTranService.findTransCodeDictVo("DamageCode",registVo.getDamageCode());
		outDateVo.setDamageCode(sysVo==null?"830":sysVo.getProperty2());
		if("1".equals(registVo.getSelfClaimFlag())){
			outDateVo.setSelfClaimFlag("1");
		}else{
			outDateVo.setSelfClaimFlag("0");
		}
		
		bodyVo.setOutDate(outDateVo);
		
		reqVo.setHead(headVo);
		reqVo.setBody(bodyVo);

		String resultVal = this.objToXml(reqVo,registNo);
		logger.debug("???????????????????????????"+("1".equals(resultVal)?"?????????":"?????????"));
	}
	
	/* 
	 * @see ins.sino.claimcar.founder.service.FounderCustomService#scheduleInfoToFounder
	 * (ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo, java.lang.String)
	 * @param taskVo
	 * @param scheduleType
	 * @throws Exception
	 */
	@Override
	public void scheduleInfoToFounder(PrpLScheduleTaskVo taskVo,String scheduleType)throws Exception {
		PrpLRegistVo registVo = registQueryService.findByRegistNo(taskVo.getRegistNo());
		ScheduleInfoReqVo reqVo = new ScheduleInfoReqVo();

		// ??????
		CommonReqHeadVo headVo = new CommonReqHeadVo();
		headVo.setSystemCode("CC1002");
		headVo.setUserCode("");
		headVo.setPassword("");
		
		// body
		ScheduleInfoReqBodyVo bodyVo = new ScheduleInfoReqBodyVo();
		
		ScheduleInfoReqInputDataVo inputDataVo = new ScheduleInfoReqInputDataVo();
		if("1".equals(registVo.getSelfClaimFlag())){
			inputDataVo.setSelfClaimFlag("1");
		}else{
			inputDataVo.setSelfClaimFlag("0");
		}
		inputDataVo.setClmNo(taskVo.getRegistNo());
		inputDataVo.setAttemperTime(taskVo.getScheduledTime());
		
		List<ScheduleInfoReqExamineListVo> listVo = new ArrayList<ScheduleInfoReqExamineListVo>();
		
		//????????????
		List<PrpLScheduleItemsVo> itemsVoList = taskVo.getPrpLScheduleItemses();
		if(itemsVoList != null && itemsVoList.size() > 0){
			for(PrpLScheduleItemsVo itemsVo : itemsVoList){
				ScheduleInfoReqExamineListVo exVo = new ScheduleInfoReqExamineListVo();
				exVo.setExamineId(itemsVo.getId().toString());
				String type = itemsVo.getItemType();
				exVo.setType("4".equals(type) ? 4 : 1);
				exVo.setExamineDepartmentCode(taskVo.getScheduledComcode());
				exVo.setExamineDepartmentName(taskVo.getScheduledComname());
				exVo.setExamineAddress(taskVo.getCheckareaName());
				exVo.setExaminePrompt(taskVo.getCancelOrReassignContent());
				exVo.setExamineCode(taskVo.getScheduledUsercode());
				exVo.setExamineName(taskVo.getScheduledUsername());
				exVo.setLossAmount(null);
				exVo.setPhone(null);
				String state = StringUtils.isEmpty(taskVo.getMercyFlag())
						||"0".equals(taskVo.getMercyFlag()) ? "1" : "0";
				exVo.setState(state);
				exVo.setDevolveflag("1".equals(registVo.getTpFlag())?true:false);
				exVo.setAgentId(registVo==null?"":registVo.getFirstRegUserCode());
				exVo.setIsReassignment(CodeConstants.ScheduleStatus.SCHEDULED_CHANGE.equals
						(taskVo.getScheduleStatus())?"1":"0");
				exVo.setCallId(registVo.getCallId());
	            Boolean isMobileCase = sendMsgToMobileService.isMobileCase(registVo, taskVo.getScheduledUsercode());
	            if(isMobileCase){//??????????????????1-??????0-???
	                exVo.setIsSelf("1");
                }else{
                    exVo.setIsSelf("0");
                }
				listVo.add(exVo);
			}
		}
		
		//????????????
		List<PrpLScheduleDefLossVo> defLossVoList = taskVo.getPrpLScheduleDefLosses();
		if(defLossVoList != null && defLossVoList.size() > 0){
			for(PrpLScheduleDefLossVo defLossVo : defLossVoList){
				ScheduleInfoReqExamineListVo exVo = new ScheduleInfoReqExamineListVo();
				exVo.setExamineId(defLossVo.getId().toString());
				exVo.setType(getDefLossType(defLossVo.getDeflossType(),defLossVo.getSerialNo()));
				exVo.setExamineDepartmentCode(taskVo.getScheduledComcode());
				exVo.setExamineDepartmentName(taskVo.getScheduledComname());
				exVo.setExamineAddress(taskVo.getCheckareaName());
				exVo.setExaminePrompt(taskVo.getCancelOrReassignContent());
				exVo.setExamineCode(taskVo.getScheduledUsercode());
				exVo.setExamineName(taskVo.getScheduledUsername());
				exVo.setLossAmount(null);
				exVo.setPhone(null);
				String state = StringUtils.isEmpty(taskVo.getMercyFlag())
						||"0".equals(taskVo.getMercyFlag()) ? "1" : "0";
				exVo.setState(state);
				exVo.setDevolveflag("1".equals(registVo.getTpFlag())?true:false);
				exVo.setAgentId(registVo==null?"":registVo.getFirstRegUserCode());
				exVo.setIsReassignment(ScheduleStatus.SCHEDULED_CHANGE.equals(taskVo.getScheduleStatus())?"1":"0");
				exVo.setCallId(registVo.getCallId());
				listVo.add(exVo);
			}
		}
		
		inputDataVo.setExamineList(listVo);
		
		bodyVo.setInputData(inputDataVo);
		
		reqVo.setHead(headVo);
		reqVo.setBody(bodyVo);
		
		String resultVal = this.objToXml(reqVo,taskVo.getId().toString());
		logger.debug("???????????????????????????"+("1".equals(resultVal)?"?????????":"?????????"));
	}
	
	private Integer getDefLossType(String defLossType,Integer serialNo){
		Integer type = serialNo == 1 ? 2 : 3;
		return RadioValue.RADIO_YES.equals(defLossType) ? type : 5;
	}
	
	/* 
	 * @see ins.sino.claimcar.founder.service.
	 * FounderCustomService#registCancelToFounder(java.lang.String)
	 * @param registNo
	 * @throws Exception
	 */
	@Override
	public void registCancelToFounder(String registNo) throws Exception{
		RegistCancelReqVo reqVo = new RegistCancelReqVo();
		
		// ??????
		CommonReqHeadVo headVo = new CommonReqHeadVo();
		headVo.setSystemCode("CC1003");
		headVo.setUserCode("");
		headVo.setPassword("");
		
		//body
		RegistCancelBodyVo bodyVo = new RegistCancelBodyVo();
		
		RegistCancelOutDateVo outDateVo = new RegistCancelOutDateVo();
		outDateVo.setClmNo(registNo);
		
		bodyVo.setOutDate(outDateVo);
		
		reqVo.setHead(headVo);
		reqVo.setBody(bodyVo);
				
		String resultVal = this.objToXml(reqVo,registNo);
		logger.debug("???????????????????????????"+("1".equals(resultVal)?"?????????":"?????????"));
	}
	
	/* 
	 * @see ins.sino.claimcar.founder.service.FounderCustomService#PolicyRelationToFounder(java.lang.String, java.lang.String, java.lang.String)
	 * @param registNo
	 * @param conPolicyNo
	 * @param isConnect
	 * @throws Exception
	 */
	@Override
	public void PolicyRelationToFounder(String registNo,
	       String conPolicyNo,String isConnect) throws Exception {

		PolicyRelationReqVo reqVo = new PolicyRelationReqVo();

		// ??????
		CommonReqHeadVo headVo = new CommonReqHeadVo();
		headVo.setSystemCode("CC1005");
		headVo.setUserCode("");
		headVo.setPassword("");
		
		//body
		PolicyRelationBodyVo bodyVo = new PolicyRelationBodyVo();
		
		PolicyRelationDateVo dateVo = new PolicyRelationDateVo();
		dateVo.setClmNo(registNo);
		dateVo.setConnectPolicyNo(conPolicyNo);
		dateVo.setIsConnect(isConnect);
		
		bodyVo.setOutDate(dateVo);
		
		reqVo.setHead(headVo);
		reqVo.setBody(bodyVo);
		
		String resultVal = this.objToXml(reqVo,registNo);
		logger.debug("???????????????????????????"+("1".equals(resultVal)?"?????????":"?????????"));
	}
	
	/* 
	 * @see ins.sino.claimcar.founder.service.FounderCustomService#cancelDflossTaskToFounder(ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo)
	 * @param taskVo
	 * @throws Exception
	 */
	@Override
	public void cancelDflossTaskToFounder(PrpLScheduleTaskVo taskVo) throws Exception{
		CancelDflossTaskReqVo reqVo = new CancelDflossTaskReqVo();
		
		// ??????
		CommonReqHeadVo headVo = new CommonReqHeadVo();
		headVo.setSystemCode("CC1004");
		headVo.setUserCode("");
		headVo.setPassword("");
		
		//body
		CancelDflossTaskBodyVo bodyVo = new CancelDflossTaskBodyVo();
	
		CancelDflossTaskReqOutDateVo  dataVo = new CancelDflossTaskReqOutDateVo();
		dataVo.setClmNo(taskVo.getRegistNo());
		dataVo.setExamineId(taskVo.getId().toString());
		
		bodyVo.setOutDate(dataVo);
		
		reqVo.setHead(headVo);
		reqVo.setBodyVo(bodyVo);
		
		String resultVal = this.objToXml(reqVo,taskVo.getId().toString());
		logger.debug("???????????????????????????"+("1".equals(resultVal)?"?????????":"?????????"));
	}



	@Override
	public String carRegistPhoneToFounder() throws Exception {
		String telephone = "";//?????????????????????
		RegistPhoneReqVo reqVo=new RegistPhoneReqVo();
		
		CommonReqHeadVo reqHead=new CommonReqHeadVo();
		reqHead.setSystemCode("CC1006");//????????????
		reqHead.setUserCode("");//????????????
		reqHead.setPassword("");//??????
		RegistPhoneReqOutDateVo reqOutDate=new RegistPhoneReqOutDateVo();
		String userCode=ServiceUserUtils.getUserCode();
		reqOutDate.setUserCode(userCode);//??????????????????
		RegistPhoneReqBodyVo  reqBody=new RegistPhoneReqBodyVo();
		reqBody.setOutDate(reqOutDate);
		reqVo.setHead(reqHead);
		reqVo.setBody(reqBody);
		RegistPhoneResVo resVo=this.resToxml(reqVo);
		if(resVo!=null){
			if("1".equals(resVo.getHead().getResponseType())){
				logger.debug("????????????");
			}else{
				logger.debug("?????????????????????????????????"+resVo.getHead().getErrorMessage());
			}
			
			telephone=resVo.getBody().getOutdate().getPhone();
		}
		
		if ( StringUtils.isNotBlank(telephone) ) {//?????????????????????????????????(??????)
			if ( telephone.length() < 9 ) {
				//???????????????
				telephone = "0755-" + telephone;
			} else if ( telephone.startsWith("0") ) {//???????????????
				telephone = formatTelephoneNumber(telephone);
			}
		}
		return telephone;
	}
	
	//????????????
	private String formatTelephoneNumber(String phone){
		StringBuilder newPhone = new StringBuilder(phone);
		//?????????010???????????????020???????????????021???????????????022???????????????023??????
		//?????????024??????  ?????????025???????????????027???????????????028???????????????029???
		//?????????????????? 
		//?????????????????????4???
		if ( phone.startsWith("010") 
				|| phone.startsWith("020")
				|| phone.startsWith("021")
				|| phone.startsWith("022")
				|| phone.startsWith("023")
				|| phone.startsWith("024")
				|| phone.startsWith("025")
				|| phone.startsWith("027")
				|| phone.startsWith("028")
				|| phone.startsWith("029") ) {
			newPhone.insert(3,"-");
		} else {
			newPhone.insert(4,"-");
		}
		return newPhone.toString();
	}
	
	public CallPhoneResVo sendCallPhoneToFounder(CallPhoneReqVo callPhoneReqVo) throws Exception {
		CallPhoneResVo vo = callFounderWebService.sendCallPhone(callPhoneReqVo);
		return vo;
	}

    @Override
    public String registCancelCtAtnToFounder(String registNo) throws Exception {
        RegistCancelCtAtnReqVo reqVo = new RegistCancelCtAtnReqVo();
    
        // ??????
        CommonReqHeadVo headVo = new CommonReqHeadVo();
        headVo.setSystemCode("CC1011");
        headVo.setUserCode("");
        headVo.setPassword("");
        
        //body
        RegistCancelCtAtnReqBodyVo bodyVo = new RegistCancelCtAtnReqBodyVo();
        RegistCancelCtAtnInputdataVo registCancelCtAtnInputdataVo = new RegistCancelCtAtnInputdataVo();
        registCancelCtAtnInputdataVo.setClmNo(registNo);
        bodyVo.setRegistCancelCtAtnInputdataVo(registCancelCtAtnInputdataVo);
        
        reqVo.setHead(headVo);
        reqVo.setBody(bodyVo);
        logger.info("????????????????????????(????????????????????????)??????=================???"+registNo);
        String resultVal = this.objToXml(reqVo,registNo);
        
        //??????
        //if("1".equals(resultVal)){
        List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
        //List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
        List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
        if(checkVolist != null && checkVolist.size() > 0){
            volist.addAll(checkVolist);
        }
/*        if(dLossVolist != null && dLossVolist.size() > 0){
            volist.addAll(dLossVolist);
        }*/
        List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
        if(volist != null && volist.size() > 0){
            for(PrpLWfTaskVo vo : volist){
                if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                    if("2".equals(vo.getIsMobileAccept()) || "3".equals(vo.getIsMobileAccept())){
                        vo.setIsMobileAccept("0");
                        prpLWfTaskVoList.add(vo);
                    }
                }
            }
        }
        wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
        //}

        logger.info("????????????????????????(????????????????????????)???"+("1".equals(resultVal)?"?????????":"?????????"));
        return resultVal;
    }
}
