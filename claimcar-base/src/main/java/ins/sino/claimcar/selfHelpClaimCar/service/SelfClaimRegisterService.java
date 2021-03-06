package ins.sino.claimcar.selfHelpClaimCar.service;

import ins.framework.lang.Springs;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ScheduleType;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimCaseCarInfoResVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimCaseCarInfoVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimCaseInfoBodyResVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimCaseInfoVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimRegisterReqVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.SelfClaimRegisterResVo;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneBodyReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneHeadReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneOutdateReq;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneReqVo;
import ins.sino.claimcar.mobilecheck.vo.CallPhoneResVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGBackReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleDOrGReqVo;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqDOrGBody;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleDOrG;
import ins.sino.claimcar.mobilecheck.vo.HandleScheduleReqScheduleItemDOrG;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.regist.service.FounderCustomService;
import ins.sino.claimcar.regist.service.PolicyQueryService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistHandlerService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleHandlerService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.BiCiPolicyVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpDScheduleDOrGMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;
import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * ??????????????????????????????
 * @author zhubin
 *
 */
public class SelfClaimRegisterService implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(SelfClaimRegisterService.class);

	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
    public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
    public static final String INSCOMCODE = "DHIC";
    public static final String INSCOMPANY = "????????????????????????????????????";

	@Autowired
	PolicyQueryService policyQueryService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	CodeTranService codeTranService;
	@Autowired
    RegistHandlerService registHandlerService;
	@Autowired
    WfTaskHandleService wfTaskHandleService;
	@Autowired
    private RegistService registService;
	@Autowired
    private InterfaceAsyncService interfaceAsyncService;
	@Autowired
    private ScheduleTaskService scheduleTaskService;
	@Autowired
    private QuickUserService quickUserService;
	@Autowired
    private PolicyViewService policyViewService;
	@Autowired
    private ScheduleHandlerService scheduleHandlerService;
	@Autowired
    private AreaDictService areaDictService;
	@Autowired
    private MobileCheckService mobileCheckService;
	@Autowired
    private FounderCustomService founderCustomService;
	@Autowired
    private WFMobileService wFMobileService;
	@Autowired
	WfMainService wfMainService;

	@Override
	public Object service(String arg0, Object arg1) {
		init();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		SelfClaimRegisterResVo resVo = new SelfClaimRegisterResVo();
		ResponseHeadVo resHead = new ResponseHeadVo();
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=============??????????????????????????????: \n"+reqXml);
			SelfClaimRegisterReqVo reqVo = (SelfClaimRegisterReqVo)arg1;
			Assert.notNull(reqVo, " ??????????????????  ");
			checkRequest(reqVo);
			SelfClaimCaseInfoVo caseInfo = reqVo.getBody().getAutoClaimCaseInfoVo();
			Date caseDate = DateUtils.strToDate(caseInfo.getCaseDate(), DateUtils.YToSec);
			Date damageTime = DateUtils.strToDate(caseInfo.getDamageTime(), DateUtils.YToSec);
			if(caseInfo.getAcccInfoVos() != null && caseInfo.getAcccInfoVos().size() > 0) {
				for(SelfClaimCaseCarInfoVo vo : caseInfo.getAcccInfoVos()) {
					if(StringUtils.isNotBlank(vo.getCaseCarType()) && "1".equals(vo.getCaseCarType())) {
						if(StringUtils.isBlank(vo.getInsComCode())) {
							throw new IllegalArgumentException("??????????????????????????????????????????");
						}
						if(StringUtils.isBlank(vo.getInsCompany())) {
							throw new IllegalArgumentException("??????????????????????????????????????????");
						}
						
					}
					
				}
				
			}
			/*
			 * 0 - ??????????????????????????????
				1 -  ?????????????????????????????????????????????(????????????)
				??????????????????????????????
			 */
			BiCiPolicyVo policyVo =new BiCiPolicyVo();
			policyVo.setBipolicyNo(caseInfo.getBiPolicyNo());
			policyVo.setCipolicyNo(caseInfo.getCiPolicyNo());
			policyVo.setDamageTime(damageTime);
			policyVo.setReportTime(caseDate);
			policyVo.setIsDamageFlag(caseInfo.getIsHumInjury());
			registService.validateRegist(policyVo);
			//????????????????????????
			List<String> policies = new ArrayList<String>();
			if(StringUtils.isNotBlank(caseInfo.getBiPolicyNo())){
				policies.add(caseInfo.getBiPolicyNo());
			}
			if(StringUtils.isNotBlank(caseInfo.getCiPolicyNo())){
				policies.add(caseInfo.getCiPolicyNo());
			}

			//???????????????????????????????????????????????????????????????????????????
			List<PrpLCMainVo> prpLCMains = new ArrayList<PrpLCMainVo>();
			String BIPolicyNo = null;
	        String CIPolicyNo = null;

			//??????????????????
			for(String policyNo:policies){
				PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyNo, caseDate);
				if(prpLCMainVo!=null){
					//????????????
					business(prpLCMainVo);
					prpLCMains.add(prpLCMainVo);
				}
			}
			if(prpLCMains!=null&&prpLCMains.size()>0){
                for(PrpLCMainVo prpLCMainVo:prpLCMains){
                    if(StringUtils.isNotBlank(prpLCMainVo.getRiskCode())){
                        if("1101".equals(prpLCMainVo.getRiskCode())){
                            CIPolicyNo = prpLCMainVo.getPolicyNo();
                        }
                        else{
                            BIPolicyNo = prpLCMainVo.getPolicyNo();
                        }
                    }
                }
            }else{
            	throw new Exception("???????????????");
            }
			PrpLRegistVo prpLRegistVo = convert(prpLCMains, caseInfo);
			//????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (!StringUtils.isEmpty(BIPolicyNo)) {
                prpLRegistVo.setPolicyNo(BIPolicyNo);
                if (!StringUtils.isEmpty(CIPolicyNo)) {
                    prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(CIPolicyNo);
                }
            } else {
                prpLRegistVo.setPolicyNo(CIPolicyNo);
            }
            prpLRegistVo.getPrpLRegistExt().setOrderNo(caseInfo.getOrderNo());
            prpLRegistVo = registHandlerService.save(prpLRegistVo,prpLCMains,true,BIPolicyNo,CIPolicyNo);
            //???????????????
            submitWf(prpLRegistVo);

            //??????????????????
            SelfClaimCaseInfoBodyResVo resCaseInfo = new SelfClaimCaseInfoBodyResVo();
            SelfClaimCaseCarInfoResVo detailVo = new SelfClaimCaseCarInfoResVo();
    		resHead.setResponseType("SELFCLAIM_002");
    		resHead.setErrno("1");
    		detailVo.setRegistNo(prpLRegistVo.getRegistNo());
    		detailVo.setOrderNo(caseInfo.getOrderNo());
    		resCaseInfo.setAutoClaimCaseCarInfoResVo(detailVo);
    		resVo.setHead(resHead);
    		resVo.setCaseInfo(resCaseInfo);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=============?????????????????????????????????"+e.getMessage(),e);
			resHead.setResponseType("SELFCLAIM_002");
			resHead.setErrno("0");
			if(StringUtils.isNotBlank(e.getMessage())){
				resHead.setErrmsg(e.getMessage());
			}else{
				resHead.setErrmsg("null");
			}
			resVo.setHead(resHead);
		}
		logger.info("=============???????????????????????????????????????"+stream.toXML(resVo));
		return resVo;
	}



	public void checkRequest(SelfClaimRegisterReqVo reqVo){
		ResquestHeadVo head = reqVo.getHead();
		SelfClaimCaseInfoVo caseInfo = reqVo.getBody().getAutoClaimCaseInfoVo();
		if(!"SELFCLAIM_002".equals(head.getRequestType()) || !"claim_user".equals(head.getUser())
				|| !"claim_psd".equals(head.getPassWord())){
			throw new IllegalArgumentException(" ?????????????????????  ");
		}
		if(StringUtils.isBlank(caseInfo.getBiPolicyNo()) && StringUtils.isBlank(caseInfo.getCiPolicyNo())){
			throw new IllegalArgumentException(" ???????????????????????????  ");
		}
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * ??????????????????????????????????????????????????????????????????
	 * 30-???????????????31-???????????????34-???????????????35-???????????????
	 * 36-????????????????????????5????????????????????????????????????????????????????????????
	 * @param prpLCMainVo
	 * @author huanggusheng
	 */
	 public void business(PrpLCMainVo prpLCMainVo){
			Map<String,String> businessClassCheckMsg = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
			if(businessClassCheckMsg.containsKey(prpLCMainVo.getBusinessClass())){
				prpLCMainVo.setMemberFlag("1");
			}
	 }


	 public PrpLRegistVo convert(List<PrpLCMainVo> prpLCMains,SelfClaimCaseInfoVo caseInfo) throws Exception{
		 PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
		 PrpLRegistExtVo prpLRegistExt = new PrpLRegistExtVo();
		 List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
		 List<SelfClaimCaseCarInfoVo> carInfoVolist = caseInfo.getAcccInfoVos();
		 PrpLCItemCarVo itemCarVo = prpLCMains.get(0).getPrpCItemCars().get(0);
		 //???????????????????????????1??????????????????????????????
		 prpLRegistVo.setSelfClaimFlag("1");
		 //?????????????????????????????????1???????????????????????????
		 prpLRegistVo.setSelfRegistFlag("1");
		 prpLRegistVo.setDamageTime(DateUtils.strToDate(caseInfo.getDamageTime(), DateUtils.YToSec));
		 prpLRegistVo.setReportTime(DateUtils.strToDate(caseInfo.getCaseDate(), DateUtils.YToSec));
		 //prpLRegistVo.setReportorPhone(caseInfo.getCaseTelephone());
		 prpLRegistVo.setLinkerMobile(caseInfo.getCaseTelephone());
		 prpLRegistVo.setDamageCode("DM01");// ??????????????????
		 prpLRegistVo.setDamageAreaCode(StringUtils.substring(caseInfo.getAreaId(),0,6));
		 prpLRegistVo.setDamageAddress(caseInfo.getCaseAddress());
		 prpLRegistVo.setDamageMapCode(caseInfo.getCaseLon()+","+caseInfo.getCaseLat());


		 for(PrpLCMainVo prpLCMain:prpLCMains){
             prpLRegistVo.setComCode(prpLCMain.getComCode());
             prpLRegistVo.setRiskCode(prpLCMain.getRiskCode());
             //prpLRegistVo.setReportorName(prpLCMain.getInsuredName());//?????????????????????????????????
             prpLRegistVo.setLinkerName(prpLCMain.getInsuredName());
             prpLRegistVo.setDriverName(prpLCMain.getInsuredName());
             prpLRegistExt.setInsuredCode(prpLCMain.getInsuredCode());
             prpLRegistExt.setInsuredName(prpLCMain.getInsuredName());
             if( !"1101".equals(prpLCMain.getRiskCode())){
                 break;
             }
         }
		 if(carInfoVolist!=null && carInfoVolist.size()>0){
			 prpLRegistExt.setIsCarLoss("1");
			 for(SelfClaimCaseCarInfoVo carInfoVo:carInfoVolist){
				 PrpLRegistCarLossVo prpLRegistCarLossVo = new PrpLRegistCarLossVo();
				 prpLRegistExt.setObliGation(carInfoVo.getDutyType());
				 prpLRegistCarLossVo.setLicenseNo(carInfoVo.getCaseCarNo());
				 prpLRegistCarLossVo.setFrameNo(carInfoVo.getFrameNo());
				 if("1".equals(carInfoVo.getCaseCarType())){//?????????
					 prpLRegistExt.setLicenseNo(carInfoVo.getCaseCarNo());
					 prpLRegistExt.setFrameNo(carInfoVo.getFrameNo());
					 prpLRegistCarLossVo.setLossparty("1");
				 }else{
					 prpLRegistCarLossVo.setLossparty("3");
				 }
				 //????????? ????????????????????????????????????????????????????????????????????????
				 if(carInfoVo.getCaseCarType().equals("1")){
					 prpLRegistVo.setReportorName(carInfoVo.getCarOwnName());
					 prpLRegistVo.setReportorPhone(carInfoVo.getCarOwnPhone());
				 }
				 prpLRegistCarLosses.add(prpLRegistCarLossVo);
			 }
		 }
		 prpLRegistExt.setCheckAddressCode(StringUtils.substring(caseInfo.getAreaId(),0,6));
		 prpLRegistExt.setCheckAddress(caseInfo.getCaseAddress());
		 prpLRegistExt.setCheckAddressMapCode(caseInfo.getCaseLon()+","+caseInfo.getCaseLat());
		 prpLRegistExt.setAccidentTypes(caseInfo.getAccIdentType());
		 prpLRegistExt.setManageType("1");
		 prpLRegistExt.setIsClaimSelf("0");
		 prpLRegistExt.setIsSubRogation("0");
		 prpLRegistExt.setIsPersonLoss("0");
		 prpLRegistExt.setIsPropLoss("0");
		 prpLRegistExt.setIsOnSitReport("1");
		 prpLRegistVo.setPrpLRegistExt(prpLRegistExt);
		 prpLRegistExt.setDangerRemark(getDangerRemark(prpLRegistVo, prpLRegistCarLosses));
		 prpLRegistVo.setPrpLRegistCarLosses(prpLRegistCarLosses);

		 return prpLRegistVo;
	 }

	 private String getDangerRemark(PrpLRegistVo prpLRegistVo,List<PrpLRegistCarLossVo> carLossVoList){
	    	StringBuffer dangerRemark = new StringBuffer();
	    	PrpLRegistCarLossVo carLossMianVo = new PrpLRegistCarLossVo();
	    	PrpLRegistExtVo prpLRegistExt = prpLRegistVo.getPrpLRegistExt();
	    	List<PrpLRegistCarLossVo> carLosttThirdVo = new ArrayList<PrpLRegistCarLossVo>();
	    	//???????????????????????????
	    	for(PrpLRegistCarLossVo carLossVo:carLossVoList){
	    		if("1".equals(carLossVo.getLossparty())){
	    			Beans.copy().from(carLossVo).to(carLossMianVo);
	    		}else{
	    			carLosttThirdVo.add(carLossVo);
	    		}
	    	}

	    	if(StringUtils.isNotBlank(prpLRegistVo.getDriverName())){
	    		dangerRemark.append("?????????"+prpLRegistVo.getDriverName());
	    	}
	    	if(prpLRegistVo.getDamageTime() != null){
	    		dangerRemark.append("???"+DateUtils.dateToStr(prpLRegistVo.getDamageTime(), DateUtils.YToSec));
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistVo.getDamageAddress())){
	    		dangerRemark.append("???"+prpLRegistVo.getDamageAddress()+"????????????????????????????????????");
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistVo.getDamageCode())){
	    		dangerRemark.append("??????"+codeTranService.transCode("DamageCode",prpLRegistVo.getDamageCode())+"???");
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLicenseNo())){
	    		dangerRemark.append("???????????????"+carLossMianVo.getLicenseNo());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getBrandName())){
	    		dangerRemark.append("???"+carLossMianVo.getBrandName());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLossremark())){
	    		dangerRemark.append("???"+carLossMianVo.getLossremark());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLosspart())){
	    		String[] lossParts = carLossMianVo.getLosspart().split(",");
	    		for(String lossPart:lossParts){
	    			dangerRemark.append("???"+codeTranService.transCode("LossPart",lossPart));
	    		}
	    	}
			dangerRemark.append("???");
	    	if(carLosttThirdVo!=null && carLosttThirdVo.size()>0){
	    		dangerRemark.append("????????????");
	    		for(PrpLRegistCarLossVo carLossVo:carLosttThirdVo){
	    			if(StringUtils.isNotBlank(carLossVo.getLicenseNo())){
	    				dangerRemark.append(carLossVo.getLicenseNo());
	    			}
	    			if(StringUtils.isNotBlank(carLossVo.getBrandName())){
	    				dangerRemark.append("???"+carLossVo.getBrandName());
	    			}
	    			if(StringUtils.isNotBlank(carLossVo.getLosspart())){
	    				String[] lossParts = carLossVo.getLosspart().split(",");
	    				for(String lossPart:lossParts){
	    	    			dangerRemark.append("???"+codeTranService.transCode("LossPart",lossPart));
	    	    		}
	    			}
	    			dangerRemark.append("???");
	    		}
	    	}
	    	dangerRemark.append("????????????????????????"+prpLRegistExt.getCheckAddress()+"???");
	    	if(StringUtils.isNotBlank(prpLRegistExt.getManageType())){
	    		dangerRemark.append("?????????"+codeTranService.transCode("AccidentManageType",prpLRegistExt.getManageType())+"???");
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistExt.getCheckType())){
	    		dangerRemark.append("??????"+codeTranService.transCode("CheckType",prpLRegistExt.getCheckType())+"???");
	    	}

	    	return dangerRemark.toString();
	    }

	 private void submitWf(PrpLRegistVo prpLRegistVo){

		 WfTaskSubmitVo submitVo1 = new WfTaskSubmitVo();
		 submitVo1.setFlowId(prpLRegistVo.getFlowId());
		 submitVo1.setTaskInKey(prpLRegistVo.getRegistNo());
		 submitVo1.setFlowTaskId(BigDecimal.ZERO);
		 submitVo1.setComCode(prpLRegistVo.getComCode());
		 submitVo1.setAssignUser("ANYONE");//??????????????????????????????,taskIn???assignUser???handlerUser?????????ANYONE,????????????????????????????????????
		 submitVo1.setTaskInUser("AUTO");
		 submitVo1.setAssignCom(prpLRegistVo.getComCode());
		 submitVo1.setSubmitType(SubmitType.TMP);//???????????????????????????
		 //???????????????
	     wfTaskHandleService.submitRegist(prpLRegistVo,submitVo1);

	     WfTaskSubmitVo submitVo2 = new WfTaskSubmitVo();
	     PrpLRegistVo registVo = registService.findRegistByRegistNo(prpLRegistVo.getRegistNo());
	     //??????????????????????????????
	     registVo.setRegistTaskFlag(CodeConstants.RegistTaskFlag.SUBMIT);
	     BigDecimal flowTaskId = BigDecimal.ZERO;
	     submitVo2.setFlowId(registVo.getFlowId());
	     submitVo2.setTaskInKey(registVo.getRegistNo());
	     submitVo2.setFlowTaskId(flowTaskId);
	     submitVo2.setTaskInUser("AUTO");
	     submitVo2.setComCode(prpLRegistVo.getComCode());
	     submitVo2.setAssignUser("AUTO");
	     submitVo2.setAssignCom(prpLRegistVo.getComCode());
	     submitVo2.setSubmitType(SubmitType.N);

	     PrpLScheduleTaskVo scheduleTaskVo1 = new PrpLScheduleTaskVo();
	     scheduleTaskVo1.setPosition("00010095");
	     scheduleTaskVo1.setCreateUser("AUTO");
	     scheduleTaskVo1.setCreateTime(new Date());
	     scheduleTaskVo1.setUpdateUser("AUTO");
	     scheduleTaskVo1.setUpdateTime(new Date());
	     String url = null;//SpringProperties.getProperty("MClaimPlatform_URL")+AUTOSCHEDULE_URL_METHOD;
	     try{
	         registService.submitSchedule(registVo, submitVo2, scheduleTaskVo1,url);
	     }
	     catch(Exception e){
	         logger.debug("??????????????????????????????????????????"+e.getMessage());
	         e.printStackTrace();
	     }
	     //???????????????
	     PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo2);
	     //????????????
	     interfaceAsyncService.sendRegistToPlatform(prpLRegistVo.getRegistNo());

	     //????????????????????????????????????(??????????????????)
	     try{
	         //scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
	         interfaceAsyncService.carRegistToFounder(prpLRegistVo.getRegistNo());
	     }catch(Exception e){
	         logger.debug("?????????????????????????????????????????????"+e.getMessage());
	     }

	     //????????????
	     registService.sendMsg(prpLRegistVo);

	     //???????????????
	     WfTaskSubmitVo submitVo3 = new WfTaskSubmitVo();
	     PrpLScheduleTaskVo scheduleTaskVo2 = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	     //??????????????????
	    // PrplQuickUserVo prplQuickUserVo = quickUserService.findQuickUser();
	     List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskVo2.getPrpLScheduleItemses();

	     submitVo3.setFlowId(taskVo.getFlowId());
	     submitVo3.setFlowTaskId(taskVo.getTaskId());
	     submitVo3.setTaskInKey(prpLRegistVo.getRegistNo());
	     submitVo3.setComCode(prpLRegistVo.getComCode());
	     submitVo3.setTaskInUser("AUTO");
	     submitVo3.setSubmitType(SubmitType.N);

	     String lngXlatY = registVo.getDamageMapCode();
	     String checkAreaCode = registVo.getDamageAreaCode();
	     String code = scheduleTaskVo2.getScheduledComcode();//????????????????????????
	     //????????????
	     List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
	     prpLCMainVoList = policyViewService.getPolicyAllInfo(scheduleTaskVo2.getRegistNo());
	     if(prpLCMainVoList.size() > 0 && prpLCMainVoList!=null){
	         //????????????
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
	         if(code != null && comCode!=""){
	             if("0002".equals(code.substring(0, 4))){//??????
	                 if(!code.substring(0, 4).equals(comCode.substring(0, 4))){

	                     registVo.setTpFlag("1");
	                     registVo.setIsoffSite("1");
	                     registService.saveOrUpdate(registVo);
	                 }

	             }else{
	                 if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
	                     registVo.setTpFlag("1");
	                     registVo.setIsoffSite("1");
	                     registService.saveOrUpdate(registVo);
	                 }
	             }
	         }
	     }
	     scheduleTaskVo2.setTypes("1");
	     scheduleTaskVo2.setScheduledTime(new Date());
	     scheduleHandlerService.saveScheduleItemTask(scheduleTaskVo2, prpLScheduleItemses, submitVo3);
	    /* //??????????????????????????????+1
	     quickUserService.updateTimes(prplQuickUserVo);*/
	     //????????????
	     SysUserVo sysUserVo = new SysUserVo();
	     sysUserVo.setUserCode(scheduleTaskVo2.getScheduledUsercode());
	     sysUserVo.setComCode(scheduleTaskVo2.getScheduledComcode());
	     //scheduleTaskVo2.setRelateHandlerMobile(prplQuickUserVo.getPhone());
	     scheduleHandlerService.sendMsg(scheduleTaskVo2,prpLScheduleItemses, sysUserVo,"1");

	     //???????????????????????????????????????????????????
	     try{
	         if(prpLScheduleItemses != null && prpLScheduleItemses.size() > 0){
	             for(PrpLScheduleItemsVo vo : prpLScheduleItemses){
	                 if(StringUtils.equals(vo.getItemType(),"4")){
	                     scheduleTaskVo2.setScheduleStatus("3");
	                     scheduleTaskVo2.setIsPersonFlag("1");
	                     scheduleTaskVo2.setPrpLScheduleItemses(prpLScheduleItemses);
	                 }
	             }
	         }
	         interfaceAsyncService.scheduleInfoToFounder(scheduleTaskVo2,ScheduleType.CHECK_SCHEDULE);
	     }catch(Exception e){
	    	 e.printStackTrace();
	     }

	   //??????????????????/????????????????????????????????????????????????List<PrpLScheduleItemsVo> prpLScheduleItemses
	     try{
	         this.setReassignments(scheduleTaskVo2, checkAreaCode, lngXlatY,"Check",prpLScheduleItemses);
	         //??????????????????
	         if(StringUtils.isNotBlank(scheduleTaskVo2.getCallNumber())){
	             CallPhoneReqVo vo = new CallPhoneReqVo();
	             CallPhoneResVo callPhoneResVo = new CallPhoneResVo();
	             CallPhoneBodyReq callPhoneBodyReq = new CallPhoneBodyReq();
	             CallPhoneHeadReq callPhoneHeadReq = new CallPhoneHeadReq();
	             callPhoneHeadReq.setSystemCode("CC1007");
	             CallPhoneOutdateReq callPhoneOutdateReq = new CallPhoneOutdateReq();
	             callPhoneOutdateReq.setClmNo(scheduleTaskVo2.getRegistNo());
	             callPhoneOutdateReq.setExaminePhone(scheduleTaskVo2.getCallNumber());
	             callPhoneBodyReq.setOutdate(callPhoneOutdateReq);
	             vo.setHead(callPhoneHeadReq);
	             vo.setBody(callPhoneBodyReq);
	             if(StringUtils.isEmpty(url)){
	                 throw new Exception("??????????????????????????????????????????");
	             }
	             callPhoneResVo = founderCustomService.sendCallPhoneToFounder(vo);
	         }
	     }
	     catch(Exception e){
	         e.printStackTrace();
	     }

	 }

		//??????????????????/?????????????????????????????????????????????????????????????????????????????????
		public void setReassignments(PrpLScheduleTaskVo prpLScheduleTaskVo,
				String checkAreaCode, String lngXlatY,String schType,List<PrpLScheduleItemsVo> prpLScheduleItemses) throws Exception {

			//??????????????????
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());

			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();

			HeadReq head = setHeadReq();//??????????????????

			HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
			HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
	        PrpDScheduleDOrGMainVo prpDScheduleDOrGMainVo = new PrpDScheduleDOrGMainVo();
	        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(prpLRegistVo.getRegistNo());
	        prpDScheduleDOrGMainVo.setScheduleDOrG(scheduleDOrG);
	        prpDScheduleDOrGMainVo.setPrpLCMainVoList(prpLCMainVoList);
	        prpDScheduleDOrGMainVo.setPrpLRegistVo(prpLRegistVo);
	        prpDScheduleDOrGMainVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
	        prpDScheduleDOrGMainVo.setSchType(schType);
	        prpDScheduleDOrGMainVo.setLngXlatY(lngXlatY);
	        prpDScheduleDOrGMainVo.setCheckAreaCode(checkAreaCode);
	        scheduleDOrG = scheduleTaskService.setScheduleDOrG(prpDScheduleDOrGMainVo);

			//?????????????????????
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        	"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//????????????
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//??????????????????
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
				if("1".equals(configValueVo.getConfigValue())){

					Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
					if(isMobileWhileListCase){//?????????????????????????????????????????????
						scheduleDOrG.setIsMobileCase("0");
					}else{
						PrpLWfMainVo lWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(prpLScheduleTaskVo.getRegistNo());
						if(lWfMainVo!=null){
						    if("1".equals(lWfMainVo.getIsMobileCase())){//???????????????
						        scheduleDOrG.setIsMobileCase("0");
						    }else{
						        scheduleDOrG.setIsMobileCase("1");
						    }
						}else{
						    scheduleDOrG.setIsMobileCase("1");
						}
					}
				}else{
					scheduleDOrG.setIsMobileCase("1");
				}
	        }


			//??????id
			int id = 1;
			String scheduleTaskId = "1";
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLRegistVo.getRegistNo(), FlowNode.Chk.toString());
			if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
				//?????????????????????
				Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
				@Override
				public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
						return o2.getTaskInTime().compareTo(o1.getTaskInTime());
					}
				});
				scheduleTaskId=prpLWfTaskVoList.get(0).getTaskId().toString();
			}
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemList = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			for(PrpLScheduleItemsVo ItemsVo :prpLScheduleItemses){
				HandleScheduleReqScheduleItemDOrG scheduleItemDOrG =new HandleScheduleReqScheduleItemDOrG();
						if("4".equals(ItemsVo.getItemType())){//??????
							scheduleItemDOrG.setTaskId(String.valueOf(id++));
							scheduleItemDOrG.setNodeType("PLoss");
						}else{//??????
							scheduleItemDOrG.setTaskId(scheduleTaskId);
							scheduleItemDOrG.setNodeType("Check");

						}
				if("3".equals(scheduleDOrG.getCaseFlag())){//????????????????????????????????????????????????
					scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
                    scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
                    scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
                    scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
				}else{
					scheduleItemDOrG.setNextHandlerCode("");
					scheduleItemDOrG.setNextHandlerName("");
					scheduleItemDOrG.setScheduleObjectId("");
					scheduleItemDOrG.setScheduleObjectName("");
				}

				scheduleItemDOrG.setIsObject("0");
				scheduleItemList.add(scheduleItemDOrG);
			}
			List<HandleScheduleReqScheduleItemDOrG> scheduleItemListResult = new ArrayList<HandleScheduleReqScheduleItemDOrG>();
			HandleScheduleReqScheduleItemDOrG scheduleItemResult = new HandleScheduleReqScheduleItemDOrG();
			HandleScheduleReqScheduleItemDOrG personScheduleItemResult = new HandleScheduleReqScheduleItemDOrG();
			//????????????????????????
			for(HandleScheduleReqScheduleItemDOrG vos : scheduleItemList){
				if(vos.getNodeType().equals("PLoss")){
					personScheduleItemResult = vos;
				}
				if(vos.getNodeType().equals("Check")){
					scheduleItemResult = vos;
				}
			}
			if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(personScheduleItemResult);
			}
			if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
				scheduleItemListResult.add(scheduleItemResult);
			}
			scheduleDOrG.setScheduleItemList(scheduleItemListResult);
			body.setScheduleDOrG(scheduleDOrG);
			reqVo.setHead(head);
			reqVo.setBody(body);

			//String xmlToSend = ClaimBaseCoder.objToXml(reqVo);
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+HANDLSCHEDDORGULE_URL_METHOD;
			HandleScheduleDOrGBackReqVo voS = mobileCheckService.getHandelScheduleDOrDUrl(reqVo,url);
			// ??????????????????????????????
			if (voS != null) {
				if (StringUtils.isNotBlank(voS.getBody().getScheduleSD().getOrderNo())) {
					prpLRegistVo.getPrpLRegistExt().setOrderNo(voS.getBody().getScheduleSD().getOrderNo());
					registService.updatePrpLRegistExt(prpLRegistVo.getPrpLRegistExt());
				}
			}
		}
	 /**
	  * ??????????????????
	  * @return
	  */
	 private HeadReq setHeadReq(){
	     HeadReq head = new HeadReq();
	     head.setRequestType("ScheduleSubmit");
	     head.setPassword("mclaim_psd");
	     head.setUser("mclaim_user");
	     return head;
	 }


	 public void init(){
		 if(policyQueryService==null){
			 policyQueryService = (PolicyQueryService)Springs.getBean(PolicyQueryService.class);
		 }
		 if(registQueryService==null){
			 registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		 }
		 if(prpLCMainService==null){
			 prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		 }
		 if(codeTranService==null){
			 codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		 }
		 if(registHandlerService==null){
			 registHandlerService = (RegistHandlerService)Springs.getBean(RegistHandlerService.class);
		 }
		 if(wfTaskHandleService==null){
			 wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		 }
		 if(registService==null){
			 registService = (RegistService)Springs.getBean(RegistService.class);
		 }
		 if(interfaceAsyncService==null){
				interfaceAsyncService = (InterfaceAsyncService)Springs.getBean(InterfaceAsyncService.class);
		 }
		 if(scheduleTaskService==null){
			 scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
		 }
		 if(quickUserService==null){
			 quickUserService = (QuickUserService)Springs.getBean("quickUserService");
		 }
		 if(policyViewService==null){
			 policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		 }
		 if(scheduleHandlerService==null){
			 scheduleHandlerService = (ScheduleHandlerService)Springs.getBean(ScheduleHandlerService.class);
		 }
		 if(areaDictService==null){
			 areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
		 }
		 if(mobileCheckService==null){
			 mobileCheckService = (MobileCheckService)Springs.getBean(MobileCheckService.class);
		 }
		 if(founderCustomService==null){
			 founderCustomService = (FounderCustomService)Springs.getBean(FounderCustomService.class);
		 }
		if(wFMobileService==null){
			wFMobileService=(WFMobileService)Springs.getBean(WFMobileService.class);
		}
		if(wfMainService==null){
			wfMainService=(WfMainService)Springs.getBean(WfMainService.class);
		}
	 }
}
