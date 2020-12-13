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
 * 车险自助理赔对接核心
 * @author zhubin
 *
 */
public class SelfClaimRegisterService implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(SelfClaimRegisterService.class);

	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
    public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
    public static final String INSCOMCODE = "DHIC";
    public static final String INSCOMPANY = "鼎和财产保险股份有限公司";

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
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		SelfClaimRegisterResVo resVo = new SelfClaimRegisterResVo();
		ResponseHeadVo resHead = new ResponseHeadVo();
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=============自助理赔报案接收报文: \n"+reqXml);
			SelfClaimRegisterReqVo reqVo = (SelfClaimRegisterReqVo)arg1;
			Assert.notNull(reqVo, " 请求信息为空  ");
			checkRequest(reqVo);
			SelfClaimCaseInfoVo caseInfo = reqVo.getBody().getAutoClaimCaseInfoVo();
			Date caseDate = DateUtils.strToDate(caseInfo.getCaseDate(), DateUtils.YToSec);
			Date damageTime = DateUtils.strToDate(caseInfo.getDamageTime(), DateUtils.YToSec);
			if(caseInfo.getAcccInfoVos() != null && caseInfo.getAcccInfoVos().size() > 0) {
				for(SelfClaimCaseCarInfoVo vo : caseInfo.getAcccInfoVos()) {
					if(StringUtils.isNotBlank(vo.getCaseCarType()) && "1".equals(vo.getCaseCarType())) {
						if(StringUtils.isBlank(vo.getInsComCode())) {
							throw new IllegalArgumentException("标记车承保公司代码不能为空！");
						}
						if(StringUtils.isBlank(vo.getInsCompany())) {
							throw new IllegalArgumentException("标记车承保公司名称不能为空！");
						}
						
					}
					
				}
				
			}
			/*
			 * 0 - 一键报案（初次报案）
				1 -  已电话报案，线下转线上继续处理(数据关联)
				自助理赔只做一键报案
			 */
			BiCiPolicyVo policyVo =new BiCiPolicyVo();
			policyVo.setBipolicyNo(caseInfo.getBiPolicyNo());
			policyVo.setCipolicyNo(caseInfo.getCiPolicyNo());
			policyVo.setDamageTime(damageTime);
			policyVo.setReportTime(caseDate);
			policyVo.setIsDamageFlag(caseInfo.getIsHumInjury());
			registService.validateRegist(policyVo);
			//校验保单是否有效
			List<String> policies = new ArrayList<String>();
			if(StringUtils.isNotBlank(caseInfo.getBiPolicyNo())){
				policies.add(caseInfo.getBiPolicyNo());
			}
			if(StringUtils.isNotBlank(caseInfo.getCiPolicyNo())){
				policies.add(caseInfo.getCiPolicyNo());
			}

			//自动报案处理，报案调度完成返回查勘员信息给自助理赔
			List<PrpLCMainVo> prpLCMains = new ArrayList<PrpLCMainVo>();
			String BIPolicyNo = null;
	        String CIPolicyNo = null;

			//调用存储过程
			for(String policyNo:policies){
				PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyNo, caseDate);
				if(prpLCMainVo!=null){
					//业务分类
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
            	throw new Exception("报案失败！");
            }
			PrpLRegistVo prpLRegistVo = convert(prpLCMains, caseInfo);
			//给报案信息表赋值保单号，主表优先保存商业保单号，如果没有商业险保单则存交强险保单号，关联报案时，报案扩展表存储交强保单，否则不存
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
            //提交工作流
            submitWf(prpLRegistVo);

            //组织返回报文
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
			logger.info("=============自助理赔报案接口报错："+e.getMessage(),e);
			resHead.setResponseType("SELFCLAIM_002");
			resHead.setErrno("0");
			if(StringUtils.isNotBlank(e.getMessage())){
				resHead.setErrmsg(e.getMessage());
			}else{
				resHead.setErrmsg("null");
			}
			resVo.setHead(resHead);
		}
		logger.info("=============自助理赔报案接口返回报文："+stream.toXML(resVo));
		return resVo;
	}



	public void checkRequest(SelfClaimRegisterReqVo reqVo){
		ResquestHeadVo head = reqVo.getHead();
		SelfClaimCaseInfoVo caseInfo = reqVo.getBody().getAutoClaimCaseInfoVo();
		if(!"SELFCLAIM_002".equals(head.getRequestType()) || !"claim_user".equals(head.getUser())
				|| !"claim_psd".equals(head.getPassWord())){
			throw new IllegalArgumentException(" 请求头参数错误  ");
		}
		if(StringUtils.isBlank(caseInfo.getBiPolicyNo()) && StringUtils.isBlank(caseInfo.getCiPolicyNo())){
			throw new IllegalArgumentException(" 至少存在一个保单号  ");
		}
	}

	/**
	 * 在报案、调度页面保单信息增加显示业务板块、
	 * 业务分类，若业务板块为会员业务，业务分类为“
	 * 30-南网员工、31-员工亲属、34-浦发员工、35-农行员工、
	 * 36-深圳海关员工”这5类客户，则业务板块和业务分类显示的值标红
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
		 //設置是否自助理赔未1，表示为自助理赔案件
		 prpLRegistVo.setSelfClaimFlag("1");
		 //设置是否自助报案案件为1，表示自助报案案件
		 prpLRegistVo.setSelfRegistFlag("1");
		 prpLRegistVo.setDamageTime(DateUtils.strToDate(caseInfo.getDamageTime(), DateUtils.YToSec));
		 prpLRegistVo.setReportTime(DateUtils.strToDate(caseInfo.getCaseDate(), DateUtils.YToSec));
		 //prpLRegistVo.setReportorPhone(caseInfo.getCaseTelephone());
		 prpLRegistVo.setLinkerMobile(caseInfo.getCaseTelephone());
		 prpLRegistVo.setDamageCode("DM01");// 出险原因代码
		 prpLRegistVo.setDamageAreaCode(StringUtils.substring(caseInfo.getAreaId(),0,6));
		 prpLRegistVo.setDamageAddress(caseInfo.getCaseAddress());
		 prpLRegistVo.setDamageMapCode(caseInfo.getCaseLon()+","+caseInfo.getCaseLat());


		 for(PrpLCMainVo prpLCMain:prpLCMains){
             prpLRegistVo.setComCode(prpLCMain.getComCode());
             prpLRegistVo.setRiskCode(prpLCMain.getRiskCode());
             //prpLRegistVo.setReportorName(prpLCMain.getInsuredName());//报案人暂取被保险人姓名
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
				 if("1".equals(carInfoVo.getCaseCarType())){//标的车
					 prpLRegistExt.setLicenseNo(carInfoVo.getCaseCarNo());
					 prpLRegistExt.setFrameNo(carInfoVo.getFrameNo());
					 prpLRegistCarLossVo.setLossparty("1");
				 }else{
					 prpLRegistCarLossVo.setLossparty("3");
				 }
				 //修改： 把标的车的车主和车主电话，设置为报案人和报案电话
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
	    	//区分三者车和标的车
	    	for(PrpLRegistCarLossVo carLossVo:carLossVoList){
	    		if("1".equals(carLossVo.getLossparty())){
	    			Beans.copy().from(carLossVo).to(carLossMianVo);
	    		}else{
	    			carLosttThirdVo.add(carLossVo);
	    		}
	    	}

	    	if(StringUtils.isNotBlank(prpLRegistVo.getDriverName())){
	    		dangerRemark.append("驾驶人"+prpLRegistVo.getDriverName());
	    	}
	    	if(prpLRegistVo.getDamageTime() != null){
	    		dangerRemark.append("于"+DateUtils.dateToStr(prpLRegistVo.getDamageTime(), DateUtils.YToSec));
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistVo.getDamageAddress())){
	    		dangerRemark.append("在"+prpLRegistVo.getDamageAddress()+"使用被保险机动车过程中，");
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistVo.getDamageCode())){
	    		dangerRemark.append("发生"+codeTranService.transCode("DamageCode",prpLRegistVo.getDamageCode())+"；");
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLicenseNo())){
	    		dangerRemark.append("造成标的："+carLossMianVo.getLicenseNo());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getBrandName())){
	    		dangerRemark.append("，"+carLossMianVo.getBrandName());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLossremark())){
	    		dangerRemark.append("，"+carLossMianVo.getLossremark());
	    	}
	    	if(StringUtils.isNotBlank(carLossMianVo.getLosspart())){
	    		String[] lossParts = carLossMianVo.getLosspart().split(",");
	    		for(String lossPart:lossParts){
	    			dangerRemark.append("，"+codeTranService.transCode("LossPart",lossPart));
	    		}
	    	}
			dangerRemark.append("；");
	    	if(carLosttThirdVo!=null && carLosttThirdVo.size()>0){
	    		dangerRemark.append("三者车：");
	    		for(PrpLRegistCarLossVo carLossVo:carLosttThirdVo){
	    			if(StringUtils.isNotBlank(carLossVo.getLicenseNo())){
	    				dangerRemark.append(carLossVo.getLicenseNo());
	    			}
	    			if(StringUtils.isNotBlank(carLossVo.getBrandName())){
	    				dangerRemark.append("，"+carLossVo.getBrandName());
	    			}
	    			if(StringUtils.isNotBlank(carLossVo.getLosspart())){
	    				String[] lossParts = carLossVo.getLosspart().split(",");
	    				for(String lossPart:lossParts){
	    	    			dangerRemark.append("，"+codeTranService.transCode("LossPart",lossPart));
	    	    		}
	    			}
	    			dangerRemark.append("；");
	    		}
	    	}
	    	dangerRemark.append("当前损失标的位于"+prpLRegistExt.getCheckAddress()+"；");
	    	if(StringUtils.isNotBlank(prpLRegistExt.getManageType())){
	    		dangerRemark.append("要求有"+codeTranService.transCode("AccidentManageType",prpLRegistExt.getManageType())+"，");
	    	}
	    	if(StringUtils.isNotBlank(prpLRegistExt.getCheckType())){
	    		dangerRemark.append("需要"+codeTranService.transCode("CheckType",prpLRegistExt.getCheckType())+"。");
	    	}

	    	return dangerRemark.toString();
	    }

	 private void submitWf(PrpLRegistVo prpLRegistVo){

		 WfTaskSubmitVo submitVo1 = new WfTaskSubmitVo();
		 submitVo1.setFlowId(prpLRegistVo.getFlowId());
		 submitVo1.setTaskInKey(prpLRegistVo.getRegistNo());
		 submitVo1.setFlowTaskId(BigDecimal.ZERO);
		 submitVo1.setComCode(prpLRegistVo.getComCode());
		 submitVo1.setAssignUser("ANYONE");//报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
		 submitVo1.setTaskInUser("AUTO");
		 submitVo1.setAssignCom(prpLRegistVo.getComCode());
		 submitVo1.setSubmitType(SubmitType.TMP);//修改报案工作流信息
		 //提交工作流
	     wfTaskHandleService.submitRegist(prpLRegistVo,submitVo1);

	     WfTaskSubmitVo submitVo2 = new WfTaskSubmitVo();
	     PrpLRegistVo registVo = registService.findRegistByRegistNo(prpLRegistVo.getRegistNo());
	     //将报案状态设为已提交
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
	         logger.debug("报案提交调用方正客服系统失败"+e.getMessage());
	         e.printStackTrace();
	     }
	     //提交工作流
	     PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo2);
	     //上传平台
	     interfaceAsyncService.sendRegistToPlatform(prpLRegistVo.getRegistNo());

	     //报案提交调用方正客服系统(车险报案接口)
	     try{
	         //scheduleTaskVo.setRelateHandlerMobile(item.getRelateHandlerMobile());
	         interfaceAsyncService.carRegistToFounder(prpLRegistVo.getRegistNo());
	     }catch(Exception e){
	         logger.debug("报案提交调用方正客服系统失败："+e.getMessage());
	     }

	     //发送短信
	     registService.sendMsg(prpLRegistVo);

	     //调度到查勘
	     WfTaskSubmitVo submitVo3 = new WfTaskSubmitVo();
	     PrpLScheduleTaskVo scheduleTaskVo2 = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	     //设置查勘人员
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
	     String code = scheduleTaskVo2.getScheduledComcode();//调度查勘员的机构
	     //调度地区
	     List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
	     prpLCMainVoList = policyViewService.getPolicyAllInfo(scheduleTaskVo2.getRegistNo());
	     if(prpLCMainVoList.size() > 0 && prpLCMainVoList!=null){
	         //承保地区
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
	             if("0002".equals(code.substring(0, 4))){//深圳
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
	    /* //快赔人员任务接受次数+1
	     quickUserService.updateTimes(prplQuickUserVo);*/
	     //发送短信
	     SysUserVo sysUserVo = new SysUserVo();
	     sysUserVo.setUserCode(scheduleTaskVo2.getScheduledUsercode());
	     sysUserVo.setComCode(scheduleTaskVo2.getScheduledComcode());
	     //scheduleTaskVo2.setRelateHandlerMobile(prplQuickUserVo.getPhone());
	     scheduleHandlerService.sendMsg(scheduleTaskVo2,prpLScheduleItemses, sysUserVo,"1");

	     //调度信息送方正客服系统（调度查勘）
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

	   //理赔调度提交/改派提交接口（理赔请求快赔系统）List<PrpLScheduleItemsVo> prpLScheduleItemses
	     try{
	         this.setReassignments(scheduleTaskVo2, checkAreaCode, lngXlatY,"Check",prpLScheduleItemses);
	         //新的一键呼出
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
	                 throw new Exception("未配置方正客服系统服务地址！");
	             }
	             callPhoneResVo = founderCustomService.sendCallPhoneToFounder(vo);
	         }
	     }
	     catch(Exception e){
	         e.printStackTrace();
	     }

	 }

		//理赔调度提交/改派提交接口（理赔请求快赔系统）调度初始化或者新增查勘
		public void setReassignments(PrpLScheduleTaskVo prpLScheduleTaskVo,
				String checkAreaCode, String lngXlatY,String schType,List<PrpLScheduleItemsVo> prpLScheduleItemses) throws Exception {

			//获取报案信息
			PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
			PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());

			HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();

			HeadReq head = setHeadReq();//设置头部信息

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

			//是否移动端案件
			PrpLScheduleTaskVo selfScheduleTaskVo = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        scheduleDOrG.setCaseFlag("3");
	        scheduleDOrG.setOrderNo(prpLRegistVo.getPrpLRegistExt().getOrderNo());
	        if("0".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        	"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//电话直赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("1");
	        }else if("1".equals(prpLRegistVo.getSelfRegistFlag()) &&
	        		"1".equals(selfScheduleTaskVo.getIsAutoCheck())){//微信自助理赔
	        	scheduleDOrG.setIsMobileCase("0");
	        	scheduleDOrG.setCaseFlag("2");
	        }else{
				PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.MobileCheck,vo.getComCode());
				if("1".equals(configValueVo.getConfigValue())){

					Boolean isMobileWhileListCase = wFMobileService.findWhileListCase(CodeConstants.WhiteList.ISMOBILE,prpLScheduleTaskVo.getScheduledUsercode());
					if(isMobileWhileListCase){//移动端案件，不送民太安车童接口
						scheduleDOrG.setIsMobileCase("0");
					}else{
						PrpLWfMainVo lWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(prpLScheduleTaskVo.getRegistNo());
						if(lWfMainVo!=null){
						    if("1".equals(lWfMainVo.getIsMobileCase())){//移动端案件
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


			//任务id
			int id = 1;
			String scheduleTaskId = "1";
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLRegistVo.getRegistNo(), FlowNode.Chk.toString());
			if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
				//流入时间降序排
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
						if("4".equals(ItemsVo.getItemType())){//人伤
							scheduleItemDOrG.setTaskId(String.valueOf(id++));
							scheduleItemDOrG.setNodeType("PLoss");
						}else{//查勘
							scheduleItemDOrG.setTaskId(scheduleTaskId);
							scheduleItemDOrG.setNodeType("Check");

						}
				if("3".equals(scheduleDOrG.getCaseFlag())){//案件标识为普通案件的时候，才传值
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
			//只传查勘或者人伤
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
			// 回写自助理赔的订单号
			if (voS != null) {
				if (StringUtils.isNotBlank(voS.getBody().getScheduleSD().getOrderNo())) {
					prpLRegistVo.getPrpLRegistExt().setOrderNo(voS.getBody().getScheduleSD().getOrderNo());
					registService.updatePrpLRegistExt(prpLRegistVo.getPrpLRegistExt());
				}
			}
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
