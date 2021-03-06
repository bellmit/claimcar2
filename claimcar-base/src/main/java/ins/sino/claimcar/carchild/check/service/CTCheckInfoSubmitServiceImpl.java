package ins.sino.claimcar.carchild.check.service;


import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.utils.DateUtils;
import ins.platform.utils.IDCardUtil;
import ins.platform.utils.IDCardUtil.IDInfo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.carchild.check.vo.CTCarInfoVo;
import ins.sino.claimcar.carchild.check.vo.CTCertifyDirect;
import ins.sino.claimcar.carchild.check.vo.CTCertifyItem;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoSubmitReqVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoSubmitResVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckInfoSubmitVo;
import ins.sino.claimcar.carchild.check.vo.CTCheckVo;
import ins.sino.claimcar.carchild.check.vo.CTDriverVo;
import ins.sino.claimcar.carchild.check.vo.CTPersonInfoVo;
import ins.sino.claimcar.carchild.check.vo.CTPropInfoVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyCodeVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckPropVo;
import ins.sino.claimcar.check.vo.PrpLCheckTaskVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobilecheck.service.LockService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class CTCheckInfoSubmitServiceImpl  implements ServiceInterface {

	private static Logger logger = LoggerFactory.getLogger(CTCheckInfoSubmitServiceImpl.class);
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	private PayCustomService payCustomService;
	@Autowired
	RegistQueryService registQueryService;
	@Autowired
	AreaDictService areaDictService;
	@Autowired
	PrpLCMainService prpLCMainService;
	@Autowired
	ScheduleService scheduleService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    CertifyService certifyService;
    @Autowired
    LockService lockService;
    @Autowired
    ManagerService managerService;
    public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";

	// ????????????
    public Long  setCheckInfo(CTCheckInfoSubmitVo ctCheckInfoSubmitVo,List<CTCarInfoVo> carInfoVoList,List<CTPropInfoVo> propInfoVoList,
								List<CTPersonInfoVo> personInfoVoList,String url,String resultMsg) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long chkId = (long) 0;
		PrpLCheckTaskVo checkTaskVo = new PrpLCheckTaskVo();
		// ?????????????????????
		List<PrpLCheckCarVo> checkCarVos = new ArrayList<PrpLCheckCarVo>();
		checkCarVos = setCarInfoVoList(checkTaskVo,ctCheckInfoSubmitVo,carInfoVoList,resultMsg);
		//Map<String,String> serialNoMap = checkHandleService.getCarLossParty(ctCheckInfoSubmitVo.getRegistNo());
		

		Map<String,String> serialNoMap = new HashMap<String,String>();
		for(PrpLCheckCarVo carvo : checkCarVos){
			String serialNo = carvo.getSerialNo().toString();
			String licNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
			serialNoMap.put(serialNo,1==carvo.getSerialNo() ? "?????????("+licNo+")" : "?????????("+licNo+")");
			serialNoMap.put("0","??????/????????????");
		}
		
		PrpLCheckVo checkVo = new PrpLCheckVo();
		checkVo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
		if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getCheckId())){
			checkVo.setId(Long.valueOf(ctCheckInfoSubmitVo.getCheckId()));
		}
		checkVo.setSingleAccidentFlag(ctCheckInfoSubmitVo.getIsunilAccident());
		checkVo.setDamageTypeCode(ctCheckInfoSubmitVo.getDamageTypeCode());
		checkVo.setManageType(ctCheckInfoSubmitVo.getManageTypeName());
		checkVo.setDamageCode(ctCheckInfoSubmitVo.getDamageCode());
		checkVo.setDamOtherCode(ctCheckInfoSubmitVo.getDamOtherCode());
		// checkVo.setClaimType(""); ?????????
		checkVo.setMercyFlag("1");
		checkVo.setLossType(ctCheckInfoSubmitVo.getIsLoss());
		checkVo.setCheckType(ctCheckInfoSubmitVo.getCheckType());
		// ????????????
		PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(ctCheckInfoSubmitVo.getNextHandlerCode());
		if(intermVo!=null){
			checkVo.setRemark(intermVo.getIntermCode());
		}
		checkVo.setCheckClass(intermVo!=null ? CheckClass.CHECKCLASS_Y : CheckClass.CHECKCLASS_N);
		checkVo.setIsClaimSelf(ctCheckInfoSubmitVo.getExcessType());
		checkVo.setIsPropLoss(ctCheckInfoSubmitVo.getIsIncludeProp());
		checkVo.setIsPersonLoss(ctCheckInfoSubmitVo.getIsIncludePerson());
		checkVo.setIsSubRogation(ctCheckInfoSubmitVo.getSubrogateType());
		checkVo.setMajorCaseFlag(ctCheckInfoSubmitVo.getIsBigCase());
		checkVo.setChkSubmitTime(new Date());
		checkVo.setValidFlag("1");
		checkVo.setCreateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		checkVo.setCreateTime(new Date());
		checkVo.setUpdateTime(new Date());
		checkVo.setUpdateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		checkVo.setWaterLoggingLevel(ctCheckInfoSubmitVo.getWaterLevel());
		// ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getAccidentReason())){
            checkVo.setAccidentReason(ctCheckInfoSubmitVo.getAccidentReason());
        }else{
            checkVo.setAccidentReason("");
        } 
        
        if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getTaskId())){
        	PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(ctCheckInfoSubmitVo.getRegistNo());
        	PrpLCheckTaskVo tempCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
        	if(tempCheckTaskVo != null){
        		checkTaskVo.setId(tempCheckTaskVo.getId());
				// ??????????????????checkTask????????????
        		if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getCheckIdentifyNumber())){
					checkTaskVo.setCheckerIdfNo(ctCheckInfoSubmitVo.getCheckIdentifyNumber());// ?????????????????????
        		}else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerIdfNo())){
					checkTaskVo.setCheckerIdfNo(tempCheckTaskVo.getCheckerIdfNo());// ?????????????????????
        		}
        		if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getCheckPhoneNumber())){
					checkTaskVo.setCheckerPhone(ctCheckInfoSubmitVo.getCheckPhoneNumber());// ????????????
                }else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerPhone())){
					checkTaskVo.setCheckerPhone(tempCheckTaskVo.getCheckerPhone());// ????????????
                }
                
        	}
		}
        checkTaskVo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(ctCheckInfoSubmitVo.getRegistNo());
        if(StringUtils.isNotBlank(prpLRegistVo.getLinkerName())){
        	checkTaskVo.setLinkerName(prpLRegistVo.getLinkerName());
        } 
        else {
        	checkTaskVo.setLinkerName("");
        }
        if(StringUtils.isNotBlank(prpLRegistVo.getLinkerPhone())){
        	 checkTaskVo.setLinkerNumber(prpLRegistVo.getLinkerPhone());
        }
        else{
        	 checkTaskVo.setLinkerNumber("");
        }
        if(StringUtils.isNotBlank(prpLRegistVo.getLinkerMobile())){
        	checkTaskVo.setLinkerMobile(prpLRegistVo.getLinkerMobile());
       }
        else{
    	   checkTaskVo.setLinkerMobile("");
       }
        
        try {
			checkTaskVo.setCheckDate(formatter.parse(ctCheckInfoSubmitVo.getCheckDate()));
		} 
        catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
        checkTaskVo.setCheckAddress(ctCheckInfoSubmitVo.getCheckSite());
        checkTaskVo.setFirstAddressFlag(ctCheckInfoSubmitVo.getFirstSiteFlag());
        checkTaskVo.setCheckerCode(ctCheckInfoSubmitVo.getNextHandlerCode());
        checkTaskVo.setChecker(ctCheckInfoSubmitVo.getNextHandlerName());
        checkTaskVo.setContexts(ctCheckInfoSubmitVo.getTextType());
		/* ?????????
		 checkTaskVo.setSumRescueFee("");
		 checkTaskVo.setSumLossCarFee(sumLossCarFee);
		 checkTaskVo.setSumLossPropFee("");
		 checkTaskVo.setSumLossPersnFee("");*/
        checkTaskVo.setComCode(ctCheckInfoSubmitVo.getScheduleObjectId());
        checkTaskVo.setMakeCom(ctCheckInfoSubmitVo.getScheduleObjectId());
        checkTaskVo.setVerifyCheckFlag("1");
        //checkTaskVo.setUnderWriteState("");
        checkTaskVo.setDeflossRepairType("");
        //checkTaskVo.setRepairFee("");
        checkTaskVo.setUnderWriteDate(new Date());
        checkTaskVo.setUnderWriteUserCode(ctCheckInfoSubmitVo.getNextHandlerCode());
        //checkTaskVo.setVerifyCheckContext("");
        checkTaskVo.setValidFlag("1");
        checkTaskVo.setCreateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
        checkTaskVo.setCreateTime(new Date());
        checkTaskVo.setUpdateTime(new Date());
        checkTaskVo.setUpdateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
        checkTaskVo.setClaimText(ctCheckInfoSubmitVo.getClaimTextBig());
        

		SysUserVo userVo = new SysUserVo();
		userVo.setComCode(ctCheckInfoSubmitVo.getScheduleObjectId());
		userVo.setUserCode(ctCheckInfoSubmitVo.getNextHandlerCode());
		userVo.setUserName(ctCheckInfoSubmitVo.getNextHandlerName());
		userVo.setComName(ctCheckInfoSubmitVo.getScheduleObjectName());
		// List??????
	        List<PrpLCheckPropVo> newPropList = new ArrayList<PrpLCheckPropVo>();
	        BigDecimal sumLossPropFee = new BigDecimal(0);
	        if(propInfoVoList!=null){
	        	for(int i=0;i<propInfoVoList.size();i++) {
		        	CTPropInfoVo tempList = propInfoVoList.get(i);
		            if(tempList!=null){
		            	PrpLCheckPropVo prpLCheckPropVo = new PrpLCheckPropVo();
		            	if(StringUtils.isNotBlank(tempList.getPropId())){
		            		prpLCheckPropVo.setId(Long.valueOf(tempList.getPropId()));
		            	}
		            	else{
		            		prpLCheckPropVo.setId(null);
		            	}
		            	prpLCheckPropVo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
					// //?????????
		            	for(Iterator iter = serialNoMap.entrySet().iterator();iter.hasNext();){
		            		Map.Entry entry = (Entry) iter.next();
						if(entry.getValue().toString().contains(tempList.getLossType())){// ????????????????????????????????????????????????
		            			Long lossPartyId =  Long.parseLong((String) entry.getKey());
		            			prpLCheckPropVo.setLossPartyId(lossPartyId);
		            		}
		            	}
//		            	prpLCheckPropVo.setPropSerialNo(tempList.getPropSerialNo());
//		            	prpLCheckPropVo.setLossPartyId(Long.valueOf("2"));
		            	prpLCheckPropVo.setLossPartyName(tempList.getLossType());
		            	prpLCheckPropVo.setLossItemName(tempList.getLossName());
		            	if(StringUtils.isNotBlank(tempList.getLossNum())){
			            	prpLCheckPropVo.setLossNum(String.valueOf(Double.valueOf(tempList.getLossNum()).intValue()));
		            	}
		            	prpLCheckPropVo.setLossDegreeCode(tempList.getLossDegreeCode());
		            	prpLCheckPropVo.setLossFeeType(tempList.getUnit());
		            	prpLCheckPropVo.setIsNoClaim(tempList.getIsNoClaim());
		            	if(tempList.getPayAmount()!=null){
		            		sumLossPropFee = sumLossPropFee.add(new BigDecimal(tempList.getPayAmount()));
		            		prpLCheckPropVo.setLossFee(new BigDecimal(tempList.getPayAmount()));
		            	}
		            	
		            	prpLCheckPropVo.setValidFlag("1");
		            	prpLCheckPropVo.setCreateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		            	prpLCheckPropVo.setCreateTime(new Date());
		            	prpLCheckPropVo.setUpdateTime(new Date());
		            	prpLCheckPropVo.setUpdateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		            	
		            	
		            	newPropList.add(prpLCheckPropVo);
		            }
		        }
	        }
	        checkTaskVo.setSumLossPropFee(sumLossPropFee);
	        List<PrpLCheckPersonVo> newPersonList = new ArrayList<PrpLCheckPersonVo>();
	        BigDecimal sumLossPersnFee = new BigDecimal(0);
	        if(personInfoVoList != null){
	        	for(int i=0;i<personInfoVoList.size();i++) {
		        	CTPersonInfoVo tempList = personInfoVoList.get(i);
		            if(tempList!=null){
		            	checkVo.setReconcileFlag(tempList.getIsAdjust());
		            	PrpLCheckPersonVo prpLCheckPersonVo = new PrpLCheckPersonVo();
		            	if(StringUtils.isNotBlank(tempList.getPersonId())){
		            		prpLCheckPersonVo.setId(Long.valueOf(tempList.getPersonId()));
		            	}
		            	else{
		            		prpLCheckPersonVo.setId(null);
		            	}
		            	
		            	prpLCheckPersonVo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
					// //?????????
		            	for(Map.Entry<String, String> serialNo : serialNoMap.entrySet()){
		            		if(serialNo.getValue().contains(tempList.getLosstype())){
		            			prpLCheckPersonVo.setLossPartyId(Long.valueOf(serialNo.getKey()));
		            		}
		            	}
//		            	prpLCheckPersonVo.setPersonSerialNo(tempList.getPersonSerialNo());
		            	//prpLCheckPersonVo.setLossPartyId(Long.valueOf("0"));
		            	prpLCheckPersonVo.setLossPartyName(tempList.getLosstype());
		            	prpLCheckPersonVo.setPersonName(tempList.getName());
		            	if(StringUtils.isNotBlank(tempList.getAge())){
		            		prpLCheckPersonVo.setPersonAge(new BigDecimal(tempList.getAge()));
		            	}
		            	prpLCheckPersonVo.setPersonPayType(tempList.getDegree());
		            	prpLCheckPersonVo.setTicCode(tempList.getIndustry());
		            	prpLCheckPersonVo.setPersonSex(tempList.getSex());
		            	prpLCheckPersonVo.setIdentifyType(tempList.getCertiType());
		            	prpLCheckPersonVo.setIdNo(tempList.getIdentifyNumber());
		            	prpLCheckPersonVo.setTreatType("");
		            	prpLCheckPersonVo.setPersonProp(tempList.getPersonAttributes());
					if(StringUtils.isNotBlank(tempList.getTherapeuticagency())){// ????????????
		            	    prpLCheckPersonVo.setHospital(tempList.getTherapeuticagency());
		            	}else{
		            	    prpLCheckPersonVo.setHospital("");
		            	}
		            	prpLCheckPersonVo.setLossFeeType("");
		            	if(tempList.getSumClaim()!=null){
		            		prpLCheckPersonVo.setLossFee(new BigDecimal(tempList.getSumClaim()));
			            	sumLossPersnFee = sumLossPersnFee.add(new BigDecimal(tempList.getSumClaim()));
		            	}
		            	prpLCheckPersonVo.setCheckDispose(tempList.getSurveyType());
		            	prpLCheckPersonVo.setWoundDetail(tempList.getDegreedesc());
		            	prpLCheckPersonVo.setValidFlag("1");
		            	prpLCheckPersonVo.setCreateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		            	prpLCheckPersonVo.setCreateTime(new Date());
		            	prpLCheckPersonVo.setUpdateTime(new Date());
		            	prpLCheckPersonVo.setUpdateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
		            	newPersonList.add(prpLCheckPersonVo);
		            }
		        }
	        }
	        checkTaskVo.setSumLossPersnFee(sumLossPersnFee);
			checkTaskVo.setPrpLCheckProps(newPropList);
			checkTaskVo.setPrpLCheckPersons(newPersonList);
			//checkTaskVo.setPrpLCheckExts(newExtList);
			checkVo.setPrpLCheckTask(checkTaskVo);
			
			PrpLDisasterVo disasterVo =  null;
			try {
				if(checkVo.getId()!=null){
				chkId = checkHandleService.save(checkVo,disasterVo,userVo,"");// ??????start=========
				}
			// ??????end=========
			// ???????????????start=========
				url = url+AUTOSCHEDULE_URL_METHOD;
				PrpLCheckVo prpLCheckVo = checkHandleService.findPrpLCheckVoById(chkId);
				List<PrpLScheduleDefLossVo> scheduleDefLossVoList = 
						checkHandleService.initCheckSubmitDloss(prpLCheckVo,url);
				for(PrpLScheduleDefLossVo vo :scheduleDefLossVoList){
					if(vo.getShowMap()!=null){
						//vo.setScheduledUsercode(vo.getShowMap().get(vo.getScheduledUsercode()));
						vo.setScheduledUsercode(vo.getScheduledUsercode()+","+vo.getScheduledComcode());
					}
				}
				List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLCheckVo.getRegistNo(), FlowNode.Chk.toString());
				if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
				// ?????????????????????
					Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
					@Override
					public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
							return o2.getTaskInTime().compareTo(o1.getTaskInTime());
						}
					});
					//wfTaskHandleService.tempSaveTask(prpLWfTaskVoList.get(0).getTaskId().doubleValue(),chkId.toString(),userVo.getUserCode(),userVo.getComCode());
				if("1".equals(ctCheckInfoSubmitVo.getOptionType())){// ??????
						this.checkSubmit(ctCheckInfoSubmitVo,scheduleDefLossVoList, chkId, prpLWfTaskVoList.get(0).getTaskId().longValue(), prpLWfTaskVoList.get(0).getFlowId(), 0.0,url);
					}
				}
			// ???????????????end=========
			} catch (Exception e) {
				logger.info("?????????????????????????????????????????????\n", e);
	            throw new RuntimeException(e);
		}// ??????
            return chkId;
	}
    
    /*
	 * ?????????????????????
	 */
	public List<PrpLCheckCarVo> setCarInfoVoList(PrpLCheckTaskVo checkTaskVo,CTCheckInfoSubmitVo ctCheckInfoSubmitVo,List<CTCarInfoVo> carInfoVoList,
													String resultMsg) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<PrpLCheckCarVo> prpLCheckCarVoList = new ArrayList<PrpLCheckCarVo>();
		BigDecimal sumLossCarFee = new BigDecimal(0);
		
		try {
			for(CTCarInfoVo vo : carInfoVoList){
				PrpLCheckCarVo prpLcheckCarVo = new PrpLCheckCarVo();
				PrpLCheckCarInfoVo prpLCheckCarInfo = new PrpLCheckCarInfoVo();
				PrpLCheckDriverVo prpLCheckDriver = new PrpLCheckDriverVo();
				CTCheckVo check = vo.getCheck();
				CTDriverVo driverVo = vo.getDriver();
				PrpLCheckDutyVo checkDutyVo = new PrpLCheckDutyVo();
				// ?????????
				//int j = ++i;
				//System.out.println("=========================="+j);
				
				prpLcheckCarVo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
				if(check.getRescueFee()!=null && !check.getRescueFee().isEmpty()){
					prpLcheckCarVo.setRescueFee(new BigDecimal(check.getRescueFee()));
				}else{
					prpLcheckCarVo.setRescueFee(new BigDecimal(0));
				}
				if(StringUtils.isNotBlank(check.getLossId())){
					prpLcheckCarVo.setCarid(Long.valueOf(check.getLossId()));
                    prpLcheckCarVo.setSerialNo(Integer.valueOf(vo.getSerialNo()));
				}else{
					prpLcheckCarVo.setCarid(null);
					if("4".equals(ctCheckInfoSubmitVo.getIndemnityDuty())){// ??????
					    prpLcheckCarVo.setCiIndemDuty("0");
					}else{
					    prpLcheckCarVo.setCiIndemDuty("1");
					}
					
					// ??????start
					// ???????????????
	                List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(ctCheckInfoSubmitVo.getRegistNo(),"serialNo");
					// ????????????
	                List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(ctCheckInfoSubmitVo.getRegistNo());
	                Integer serialNo = 0;
	                for(PrpLScheduleDefLossVo prpLScheduleDefLossVo : prpLScheduleDefLossVoList){
	                    if(serialNo < prpLScheduleDefLossVo.getSerialNo()){
	                        serialNo = prpLScheduleDefLossVo.getSerialNo();
	                    }
	                }
	                if(prpLCheckCarVos != null && prpLCheckCarVos.size() > 0){
	                    if(serialNo < prpLCheckCarVos.get(0).getSerialNo()){
	                        serialNo = prpLCheckCarVos.get(0).getSerialNo();
	                    }
	                }
					// ??????end
					prpLcheckCarVo.setSerialNo(serialNo+1);
				}
				
				if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getTaskId())){
					prpLcheckCarVo.setCheckTaskId(Long.valueOf(ctCheckInfoSubmitVo.getTaskId()));
				}
				prpLcheckCarVo.setLossFlag(check.getIsLoss());
				prpLcheckCarVo.setCheckAdress(ctCheckInfoSubmitVo.getCheckSite());
				prpLcheckCarVo.setCheckTime(formatter.parse(ctCheckInfoSubmitVo.getCheckDate()));
				if(StringUtils.isNotBlank(vo.getCheck().getEstimatedLoss())){
					prpLcheckCarVo.setLossFee(new BigDecimal(vo.getCheck().getEstimatedLoss()));
					sumLossCarFee = sumLossCarFee.add(new BigDecimal(vo.getCheck().getEstimatedLoss()));
				}
				prpLcheckCarVo.setLossPart(vo.getCheck().getLossPart());
				//prpLcheckCarVo.setRescueFee(rescueFee);
				prpLcheckCarVo.setIndemnityDuty(check.getIndemnityDuty());
				if(check.getIndemnityDuty()!=null && check.getIndemnityDutyRate() != null){
					prpLcheckCarVo.setIndemnityDutyRate(new BigDecimal(check.getIndemnityDutyRate()));
				}
				if(CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR.equals(vo.getSerialNo())){
					PrpLCItemKindVo itemKindVo = policyViewService.findItemKindByKindCode(ctCheckInfoSubmitVo.getRegistNo(),vo.getCheck()
							.getKindCode());
					if(itemKindVo==null){
						prpLcheckCarVo.setKindCode("");
						resultMsg = "???????????????????????????????????????????????????????????????????????????????????????";
					}else{
						prpLcheckCarVo.setKindCode(vo.getCheck().getKindCode());
					}
				}else{
					prpLcheckCarVo.setKindCode(vo.getCheck().getKindCode());
				}
				prpLcheckCarVo.setValidFlag("1");
				prpLcheckCarVo.setCreateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
				prpLcheckCarVo.setCreateTime(new Date());
				prpLcheckCarVo.setUpdateTime(new Date());
				prpLcheckCarVo.setUpdateUser(ctCheckInfoSubmitVo.getNextHandlerCode());
				
				prpLCheckCarInfo.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
				if(StringUtils.isNotBlank(vo.getCarId())){
					prpLCheckCarInfo.setCarid(Long.valueOf(vo.getCarId()));
				}else{
					prpLCheckCarInfo.setCarid(null);
				}
				
				prpLCheckCarInfo.setLicenseNo(vo.getLicenseNo());
				prpLCheckCarInfo.setCarOwner(vo.getOwern());
				prpLCheckCarInfo.setEngineNo(vo.getEngineNo());
				prpLCheckCarInfo.setFrameNo(vo.getFrameNo());
				prpLCheckCarInfo.setVinNo(vo.getVin());
				prpLCheckCarInfo.setLicenseColor(vo.getLicenseColor());
				prpLCheckCarInfo.setLicenseType(vo.getLicenseType());
				prpLCheckCarInfo.setInsurecomcode(vo.getInsurComcode());
				prpLCheckCarInfo.setPlatformCarKindCode(vo.getMotorTypeCode());
				//prpLCheckCarInfo.setInsurecomname("");
				prpLCheckCarInfo.setCarColorCode(vo.getColorCode());
				if(StringUtils.isNotBlank(vo.getRegisteDate())){
					prpLCheckCarInfo.setEnrollDate(formatter.parse(vo.getRegisteDate()));
				}
				//prpLCheckCarInfo.setCarKindCode(vo.getLicenseType());
				if(1 == prpLcheckCarVo.getSerialNo().intValue()){
					// ?????????????????????????????????????????????
	                List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(ctCheckInfoSubmitVo.getRegistNo());
					if(cMainList!=null&&cMainList.size()>0){// ??????????????????????????????
	                    PrpLCMainVo cmainVo = cMainList.get(0);
	                    PrpLCItemCarVo cItemCarVo = cmainVo.getPrpCItemCars().get(0);
						prpLCheckCarInfo.setModelCode(cItemCarVo.getModelCode());// ????????????
	                }else{
	                    prpLCheckCarInfo.setModelCode("");
	                }
				}else{
				    prpLCheckCarInfo.setModelCode("");
				}
				
				if(StringUtils.isNotBlank(vo.getVehiclemodleName())){
					prpLCheckCarInfo.setBrandName(vo.getVehiclemodleName());// ????????????
				}else{
					prpLCheckCarInfo.setBrandName("");// ????????????
				}
				prpLCheckCarInfo.setLossFlag(check.getIsLoss());
				//prpLCheckCarInfo.setUseYears("");
				prpLCheckCarInfo.setInsuredFlag("1");
				prpLCheckCarInfo.setValidFlag("1");
				// ????????????
				if(prpLcheckCarVo.getSerialNo().intValue() != 1){
				    prpLCheckCarInfo.setPhone(driverVo.getPhoneNumber());
				}
				//prpLCheckCarInfo.setFlag(flag);
				//prpLCheckCarInfo.setRemark(remark);
				//if(vo.getCarid() == null){
				
				prpLCheckDriver.setRegistNo(ctCheckInfoSubmitVo.getRegistNo());
				prpLCheckDriver.setDriverName(driverVo.getDriverName());
				if(StringUtils.isNotBlank(driverVo.getDriverId())){
					prpLCheckDriver.setDriverId(Long.valueOf(driverVo.getDriverId()));
				}else{
					prpLCheckDriver.setDriverId(null);
				}
				
				//prpLCheckDriver.setDriverSex("");
				//prpLCheckDriver.setDriverAge(driverVo.getd);
				prpLCheckDriver.setDrivingCarType(driverVo.getDrivingCarType());
				prpLCheckDriver.setIdentifyType(driverVo.getCertiType());
				prpLCheckDriver.setLinkPhoneNumber(driverVo.getPhoneNumber());
				prpLCheckDriver.setDrivingLicenseNo(driverVo.getDrivingLicenseNo());
				prpLCheckDriver.setIdentifyNumber(driverVo.getIdentifyNumber());
				if(StringUtils.isNotBlank(driverVo.getLicenseDate())){
					prpLCheckDriver.setAcceptLicenseDate(formatter.parse(driverVo.getLicenseDate()));
				}
				prpLCheckDriver.setValidFlag("1");
				if(StringUtils.isNotBlank(driverVo.getAge())){
				    prpLCheckDriver.setDriverAge(new BigDecimal(driverVo.getAge())); 
				}
				if(StringUtils.isNotBlank(driverVo.getIdentiEffecTiveDate())){// ???????????????????????????
                    prpLCheckDriver.setDriverValidDate(formatter.parse(driverVo.getIdentiEffecTiveDate()));
                }
				prpLcheckCarVo.setPrpLCheckCarInfo(prpLCheckCarInfo);
				prpLcheckCarVo.setPrpLCheckDriver(prpLCheckDriver);
				// ?????????
				checkDutyVo.setIndemnityDuty(check.getIndemnityDuty());
				if(check.getIndemnityDuty()!=null && check.getIndemnityDutyRate() != null){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(check.getIndemnityDutyRate()));
				}
				checkDutyVo.setIsClaimSelf(ctCheckInfoSubmitVo.getExcessType());
				//checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,checkTaskInfoSubmitVo.getNextHandlerCode());
				// ????????????
				Long carId = (long)0;
				if(prpLcheckCarVo.getCarid() == null){
					carId = checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,ctCheckInfoSubmitVo.getNextHandlerCode(),"1");
				}else{
				    carId = checkHandleService.updatePrpLCheckCar(prpLcheckCarVo,checkDutyVo,"1");
				}
				PrpLCheckCarVo checkCarVo = checkHandleService.initPrpLCheckCar(carId);
				
				prpLCheckCarVoList.add(prpLcheckCarVo);// ???????????????
			}
			 checkTaskVo.setSumLossCarFee(sumLossCarFee);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("??????????????????????????????????????????????????????\n", e);
		    throw new RuntimeException(e);
			
		}
		return prpLCheckCarVoList;
	}
    
    public void checkSubmit(CTCheckInfoSubmitVo ctCheckInfoSubmitVo,List<PrpLScheduleDefLossVo> defLossVos,
	        Long sub_checkId,Long sub_flowTaskId,String sub_flowId,double sub_LossFees,String url){

		SysUserVo userVo = new SysUserVo();
		userVo.setComCode(ctCheckInfoSubmitVo.getScheduleObjectId());
		userVo.setUserCode(ctCheckInfoSubmitVo.getNextHandlerCode());
		userVo.setUserName(ctCheckInfoSubmitVo.getNextHandlerName());
		userVo.setComName(ctCheckInfoSubmitVo.getScheduleObjectName());
		PrpLWfTaskVo wfTaskVo=wfTaskHandleService.queryTask(sub_flowTaskId.doubleValue());
		if(CodeConstants.HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())){
			try{
				// ??????????????????
				checkHandleService.saveAssessor(ctCheckInfoSubmitVo.getRegistNo(),userVo);
				// ?????????????????????
				checkHandleService.saveCheckFee(ctCheckInfoSubmitVo.getRegistNo(), userVo);
				// ????????????
				checkHandleService.submitCheckToDloss(defLossVos,sub_checkId,sub_flowTaskId,sub_flowId,sub_LossFees,userVo,null,null);
			}catch(Exception e){
				logger.info("?????????????????????"+e.getMessage());
				e.printStackTrace();
		        throw new RuntimeException(e);
			}
		}else if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
			logger.info("??????????????????????????????????????????"+wfTaskVo.getHandlerUser());
			throw new RuntimeException("??????????????????????????????????????????"+wfTaskVo.getHandlerUser());
		}else{
			logger.info("???????????????");
			throw new RuntimeException("???????????????");
		}
	
	}
    
    @Override
	public CTCheckInfoSubmitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		// ?????????vo
		CTCheckInfoSubmitResVo resVo = new CTCheckInfoSubmitResVo();
		List<CTCarInfoVo> carInfoVoList = new ArrayList<CTCarInfoVo>();
		List<CTPropInfoVo> propInfoVoList = new ArrayList<CTPropInfoVo>();
		List<CTPersonInfoVo> personInfoVoList = new ArrayList<CTPersonInfoVo>();
		CarchildHeadVo carchildHeadVo = new CarchildHeadVo();
		CarchildResponseHeadVo head = new CarchildResponseHeadVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// ?????? class??????
		String registNo ="";
		stream.processAnnotations(CTCheckInfoSubmitReqVo.class);
		CTCheckInfoSubmitReqVo checkInfoSubmitReqVo = (CTCheckInfoSubmitReqVo) arg1;
		String xml = stream.toXML(checkInfoSubmitReqVo);
		logger.info("???????????????????????????????????????: \n"+xml);
		CTCheckInfoSubmitVo ctCheckInfoSubmitVo = checkInfoSubmitReqVo.getBody().getCheckInfoSubmitVo();
		List<CTCertifyItem> certifyItemList = checkInfoSubmitReqVo.getBody().getCertifyItemlist();
		String resultMsg = null;
		// ????????????
		String lockFlags = "1";

		String errMsg = "";
		String errNo = "1";
		
		try{
			registNo = ctCheckInfoSubmitVo.getRegistNo();
			Long lockYwMainId = Long.valueOf(ctCheckInfoSubmitVo.getCheckId());
			lockService.savePrplLockList(lockYwMainId, FlowNode.Check.name());
			lockFlags = "0";// ??????
			logger.info("??????"+new Date());
			carInfoVoList = ctCheckInfoSubmitVo.getCarInfo();
			propInfoVoList = ctCheckInfoSubmitVo.getPropInfo();
			personInfoVoList = ctCheckInfoSubmitVo.getPersonInfo();

			carchildHeadVo = checkInfoSubmitReqVo.getHead();
			if (!"CT_004".equals(carchildHeadVo.getRequestType())|| !"claim_user".equals(carchildHeadVo.getUser())|| !"claim_psd".equals(carchildHeadVo.getPassWord())) {
				throw new IllegalArgumentException(" ?????????????????????  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("????????????");
			}
			if(!StringUtils.isNotBlank(ctCheckInfoSubmitVo.getRegistNo())){
				throw new IllegalArgumentException("?????????????????????");
			}
			// ????????????????????????????????????????????????
			if("1".equals(ctCheckInfoSubmitVo.getOptionType())){
				checkRequest(ctCheckInfoSubmitVo);
			}
			
			
			PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
			if("1".equals(ctCheckInfoSubmitVo.getOptionType())){
				if("on".equals(checkVo.getOperateStatus())){
					throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????");
				}
				checkVo.setOperateStatus("on");
	            checkHandleService.updateCheckMain(checkVo);
			}
			// ??????????????????????????????start
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(ctCheckInfoSubmitVo.getRegistNo(), FlowNode.Chk.toString());
            if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
				// ?????????????????????
                Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                @Override
                public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                        return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                    }
                });
                
                PrpLWfTaskVo vo = prpLWfTaskVoList.get(0);
                if(CodeConstants.HandlerStatus.END.equals(vo.getHandlerStatus())){
					// throw new IllegalArgumentException("??????????????????????????????????????????"+vo.getHandlerUser());
                	errNo = "2";
					errMsg = "??????????????????????????????????????????"+vo.getHandlerUser();
                }else{
					if( !"1".equals(ctCheckInfoSubmitVo.getOptionType())){// ??????????????????
                		vo.setIsMobileAccept("0");
                		wfTaskHandleService.updateTaskIn(vo);
                	}
                	String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
					logger.info("??????????????????================="+url);
					Long checkId = setCheckInfo(ctCheckInfoSubmitVo,carInfoVoList,propInfoVoList,personInfoVoList,url,resultMsg);
					// ???????????????????????????
        			if ( propInfoVoList == null || propInfoVoList.size() <= 0 ) {
        				checkVo.setIsPropLoss("0");
        			} else{
        				checkVo.setIsPropLoss("1");
        			}
					// ??????????????????
        			if ( personInfoVoList == null || personInfoVoList.size() <= 0 ) {
        				checkVo.setIsPersonLoss("0");
        			} else{
        				checkVo.setIsPersonLoss("1");
        			}
                }
            }else{
            	errNo = "0";
				errMsg = "??????????????????????????????";
            }
			
			// ????????????
			if(certifyItemList!=null && certifyItemList.size()>0 && "1".equals(errNo)){
				try{
					checkCertifyItemInfo(certifyItemList);
		            List<PrpLCertifyItemVo> prpLCertifyItemVoList = new ArrayList<PrpLCertifyItemVo>();
		            for(CTCertifyItem certifyItem : certifyItemList){
						// ??????
		                PrpLCertifyItemVo prpLCertifyItemVo = new PrpLCertifyItemVo();
		                prpLCertifyItemVo.setRegistNo(certifyItem.getRegistNo());
		                prpLCertifyItemVo.setCertifyTypeCode(certifyItem.getCertifyTypeCode());
		                prpLCertifyItemVo.setCertifyTypeName(certifyItem.getCertifytypeName());
		                prpLCertifyItemVo.setValidFlag("1");
		                prpLCertifyItemVo.setDirectFlag(certifyItem.getDirectFlag());
		                prpLCertifyItemVo.setCreateUser(certifyItem.getCreateUser());
		                prpLCertifyItemVo.setCreateTime(DateUtils.strToDate(certifyItem.getCreateTime()));
		                prpLCertifyItemVo.setUpdateTime(DateUtils.strToDate(certifyItem.getCreateTime()));
		                prpLCertifyItemVo.setUpdateUser(certifyItem.getCreateUser());
		                prpLCertifyItemVoList.add(prpLCertifyItemVo);
		            }
		            certifyService.savePrpLCertifyItem(prpLCertifyItemVoList);
		            for(CTCertifyItem certifyItem : certifyItemList){
		                List<CTCertifyDirect> certifyDirectList = certifyItem.getCertifyDirect();
		                List<PrpLCertifyDirectVo> prpLCertifyDirectVoList = new ArrayList<PrpLCertifyDirectVo>();
						// ??????
		                if(certifyDirectList!=null && certifyDirectList.size() > 0){
		                    for(CTCertifyDirect certifyDirect : certifyDirectList){
		                        PrpLCertifyDirectVo prpLCertifyDirectVo = new PrpLCertifyDirectVo();
		                        prpLCertifyDirectVo.setRegistNo(certifyDirect.getRegistNo());
		                        prpLCertifyDirectVo.setLossItemCode(certifyDirect.getLossItemCode());
		                        prpLCertifyDirectVo.setLossItemName(certifyDirect.getLossItemName());
		                        prpLCertifyDirectVo.setTypeCode(certifyDirect.getTypeCode());
		                        prpLCertifyDirectVo.setTypeName(certifyDirect.getTypeName());
		                        prpLCertifyDirectVo.setCheckNode(certifyDirect.getCheckNode());
		                        prpLCertifyDirectVo.setCheckUser(certifyDirect.getCheckUser());
		                        prpLCertifyDirectVo.setCreateTime(DateUtils.strToDate(certifyDirect.getCreateTime()));
		                        prpLCertifyDirectVo.setCreateUser(certifyDirect.getCheckUser());
		                        prpLCertifyDirectVo.setUpdateTime(DateUtils.strToDate(certifyDirect.getCreateTime()));
		                        prpLCertifyDirectVo.setUpdateUser(certifyDirect.getCheckUser());
		                        prpLCertifyDirectVo.setValidFlag("1");
		                        List<PrpLCertifyCodeVo> prpLCertifyCodeVoList = certifyService.findCertifyCodeVoByMustUpload(certifyDirect.getLossItemCode());
		                        if(prpLCertifyCodeVoList != null && prpLCertifyCodeVoList.size() > 0 ){
		                          prpLCertifyDirectVo.setMustUpload(prpLCertifyCodeVoList.get(0).getMustUpload()); 
		                        }
		                        prpLCertifyDirectVoList.add(prpLCertifyDirectVo);
		                    } 
		                }
		                certifyService.saveAllPrpLCertifyDirect(prpLCertifyDirectVoList);
		            }
				}catch(Exception e){
					errNo = "0";
					errMsg = errMsg+"\n???????????????????????????"+e.getMessage();
					logger.info("??????????????????????????????????????????\n", e);
				}
			}
			
			head.setResponseType(carchildHeadVo.getRequestType());
			head.setErrNo(errNo);
			if("1".equals(errNo)){
				head.setErrMsg("Success");
			}else{
				head.setErrMsg(errMsg);
			}
			if(StringUtils.isNotBlank(resultMsg)){
				head.setErrMsg(resultMsg);
			}
			resVo.setHead(head); 
		}catch(Exception e){
			if(e.getMessage().contains("ORA-00001: unique constraint")){
				errNo = "0";
				errMsg = "???????????????????????????????????????????????????:"+e.getMessage();
				logger.info("???????????????????????????Insert???Update??????????????????????????????\n",e);
        	}else{
        		errNo = "0";
				errMsg = "?????????????????????"+e.getMessage();
				logger.info("??????????????????????????????????????????\n ",e);
        	}
			head.setErrNo(errNo);
			head.setErrMsg(errMsg);
			resVo.setHead(head);
		}finally{
			try {
				PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
				checkVo.setOperateStatus("off");
				checkHandleService.updateCheckMain(checkVo);
			}catch (Exception e){
				logger.info("?????????????????????????????????????????????:registNo={},???????????????{} ", registNo, e.getMessage());
			}
			// ????????????
			if("0".equals(lockFlags)){
				lockService.deletePrplLockListById(Long.valueOf(ctCheckInfoSubmitVo.getCheckId()), FlowNode.Check.name());
				logger.info("????????????"+new Date());
			}
		}

		
		stream.processAnnotations(CTCheckInfoSubmitResVo.class);
		logger.info("???????????????????????????????????????=========???\n"+stream.toXML(resVo));
		return resVo;
	}
    
    private void init() {
		if(payCustomService==null){
			payCustomService = (PayCustomService)Springs.getBean(PayCustomService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(checkHandleService==null){
			checkHandleService = (CheckHandleService)Springs.getBean(CheckHandleService.class);
		}
		if(checkTaskService==null){
			checkTaskService = (CheckTaskService)Springs.getBean(CheckTaskService.class);
		}
		
		if(registQueryService==null){
			registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
		}
		if(areaDictService==null){
			areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
		}
		if(prpLCMainService==null){
			prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
		}
		if(scheduleService==null){
			scheduleService = (ScheduleService)Springs.getBean(ScheduleService.class);
		}
		if(scheduleTaskService==null){
		    scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
        }
		if(scheduleTaskService==null){
		    scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
        }
		if(policyViewService==null){
		    policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
		if(certifyService == null){
	        certifyService = (CertifyService)Springs.getBean(CertifyService.class);
	    }
		if(lockService == null){
			lockService = (LockService)Springs.getBean(LockService.class);
	    }
		if(managerService == null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
	    }
	}
    
    public void checkRequest(CTCheckInfoSubmitVo CheckInfoSubmitVo){
		
		List<CTCarInfoVo> carList = CheckInfoSubmitVo.getCarInfo();
		List<CTPersonInfoVo> personList = CheckInfoSubmitVo.getPersonInfo();
		List<CTPropInfoVo> propList = CheckInfoSubmitVo.getPropInfo();
		
		// ????????????
		notNull(CheckInfoSubmitVo.getDamageCode()," ??????????????????  ");
		notNull(CheckInfoSubmitVo.getCheckSite()," ??????????????????  ");
		notNull(CheckInfoSubmitVo.getCheckType()," ??????????????????  ");
		notNull(CheckInfoSubmitVo.getCheckIdentifyNumber()," ???????????????????????????  ");
		notExistLowercase(CheckInfoSubmitVo.getCheckIdentifyNumber()," ?????????????????????????????????????????????  ");
		notNull(CheckInfoSubmitVo.getCheckPhoneNumber()," ???????????????????????????  ");
		notNull(CheckInfoSubmitVo.getFirstSiteFlag()," ??????????????????????????????  ");
		notNull(CheckInfoSubmitVo.getExcessType()," ????????????????????????  ");
		notNull(CheckInfoSubmitVo.getIsIncludeProp()," ????????????????????????  ");
		notNull(CheckInfoSubmitVo.getIsIncludePerson()," ????????????????????????  ");
		notNull(CheckInfoSubmitVo.getIsunilAccident(),"????????????????????????");
		
		//????????????????????????????????????
		IDCardUtil idUtils = new IDCardUtil();
		if(CheckInfoSubmitVo != null){
			String identifyNumber = CheckInfoSubmitVo.getCheckIdentifyNumber();
			if(StringUtils.isNotBlank(identifyNumber)){
				IDInfo info = idUtils.IDCardValidate(identifyNumber);
				if(StringUtils.isNotBlank(info.getErrMsg())){
					throw new IllegalArgumentException(info.getErrMsg());	
				}
			}
		}
		
		if(carList == null || carList.size() == 0){
			throw new IllegalArgumentException("??????????????????");
		}
		// ??????????????????????????????????????????????????????????????????
		if(CodeConstants.IsSingleAccident.YES.equals(CheckInfoSubmitVo.getIsunilAccident()) && carList.size()>1){
			throw new IllegalArgumentException("??????????????????????????????????????????");
		}else if(CodeConstants.IsSingleAccident.NOT.equals(CheckInfoSubmitVo.getIsunilAccident()) && carList.size()==1){
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		// ???????????????????????????0???1
		if(!CodeConstants.caseFlagType.NORMAL_CASE.equals(CheckInfoSubmitVo.getExcessType()) 
				&& !CodeConstants.caseFlagType.SELF_CASE.equals(CheckInfoSubmitVo.getExcessType())){
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		//???????????????????????????0???1
		if(!"0".equals(CheckInfoSubmitVo.getExcessType()) && !"1".equals(CheckInfoSubmitVo.getExcessType())){
			throw new IllegalArgumentException("???????????????????????????????????????");
		}
		if(StringUtils.isBlank(CheckInfoSubmitVo.getIndemnityDuty())){
			throw new IllegalArgumentException("??????????????????");
		}
		if(StringUtils.isBlank(CheckInfoSubmitVo.getIndemnityDutyRate())){
			throw new IllegalArgumentException("????????????????????????");
		}
		notNull(CheckInfoSubmitVo.getIsBigCase()," ??????????????????????????????  ");
		notNull(CheckInfoSubmitVo.getSubrogateType()," ????????????????????????  ");
		notNull(CheckInfoSubmitVo.getTextType()," ????????????????????????  ");
		
		if("1".equals(CheckInfoSubmitVo.getSubrogateType())){
			throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????????????????");
		}
		
		// ????????????
		if(carList!=null && carList.size()>0){
			for(CTCarInfoVo carInfo:carList){
				notNull(carInfo.getIfObject()," ????????????  ");
				notNull(carInfo.getLicenseNo()," ???????????????  ");
				notNull(carInfo.getMotorTypeCode()," ??????????????????  ");
				notNull(carInfo.getLicenseType()," ??????????????????  ");
				notNull(carInfo.getEngineNo()," ??????????????????  ");
				notNull(carInfo.getFrameNo()," ???????????????  ");
				notNull(carInfo.getVin()," VIN?????????  ");
				notNull(carInfo.getOwern()," ????????????  ");
				// ???????????????
				CTDriverVo driver = carInfo.getDriver();
				notNull(driver.getDriverName()," ?????????????????????  ");
				notNull(driver.getCertiType()," ??????????????????  ");
				notNull(driver.getIdentifyNumber()," ??????????????????  ");
				notExistLowercase(driver.getIdentifyNumber()," ????????????????????????????????????  ");
				notNull(driver.getDrivingLicenseNo()," ?????????????????????  ");
				notExistLowercase(driver.getDrivingLicenseNo()," ???????????????????????????????????????  ");
				notNull(driver.getPhoneNumber()," ????????????  ");
				// ??????????????????
				CTCheckVo check = carInfo.getCheck();
				notNull(check.getEstimatedLoss()," ??????????????????  ");
				if(StringUtils.isBlank(check.getIndemnityDuty())){
					throw new IllegalArgumentException("??????????????????");
				}
				if(StringUtils.isBlank(check.getIndemnityDutyRate())){
					throw new IllegalArgumentException("????????????????????????");
				}
				notNull(check.getLossPart()," ??????????????????  ");
				//????????????????????????????????????????????????????????????????????????????????????????????????????????????
				if(CheckInfoSubmitVo.getCheckIdentifyNumber().equals(driver.getIdentifyNumber())){
					throw new IllegalArgumentException("???????????????????????????????????????????????????????????????????????????");
				}
			}
			String message = checkHandleService.saveValidCars(carList);
			if(StringUtils.isNotBlank(message)){
				throw new IllegalArgumentException(message);
			}
		}
		
		// ????????????
		if(personList!=null && personList.size()>0){
			for(CTPersonInfoVo personInfo:personList){
				notNull(personInfo.getIsAdjust()," ????????????????????????  ");
				notNull(personInfo.getName()," ????????????  ");
				notNull(personInfo.getSex()," ????????????  ");
				notNull(personInfo.getCertiType()," ??????????????????  ");
				notNull(personInfo.getIdentifyNumber()," ??????????????????  ");
				notExistLowercase(personInfo.getIdentifyNumber(),"??????????????????????????????????????????");
				//???????????????????????????
				if(StringUtils.isNotBlank(personInfo.getIdentifyNumber()) && "1".equals(personInfo.getCertiType())){
					IDInfo info = idUtils.IDCardValidate(personInfo.getIdentifyNumber());
					if(StringUtils.isNotBlank(info.getErrMsg())){
						throw new IllegalArgumentException(info.getErrMsg());
					}
				}
				//????????????????????????????????????????????????????????????
				if(CheckInfoSubmitVo.getCheckIdentifyNumber().equals(personInfo.getIdentifyNumber())){
					throw new IllegalArgumentException("??????????????????????????????????????????????????????????????????");
				}
				notNull(personInfo.getDegree()," ??????????????????  ");
				notNull(personInfo.getSumClaim()," ??????????????????  ");
			}
		}
		
		// ????????????
		if(propList != null && propList.size()>0){
			for(CTPropInfoVo propInfo:propList){
				notNull(propInfo.getLossName()," ??????????????????  ");
				notNull(propInfo.getLossNum()," ??????????????????  ");
				notNull(propInfo.getPayAmount()," ??????????????????  ");
			}
		}
		
	}
	
    public void checkCertifyItemInfo(List<CTCertifyItem> certifyItemList){
        for(CTCertifyItem certifyItem : certifyItemList){
            if(StringUtils.isBlank(certifyItem.getRegistNo())){
				throw new IllegalArgumentException("?????????????????????");
            }
            if(StringUtils.isBlank(certifyItem.getCertifyTypeCode())){
				throw new IllegalArgumentException("??????????????????????????????");
            }
            if(StringUtils.isBlank(certifyItem.getCertifytypeName())){
				throw new IllegalArgumentException("??????????????????????????????");
            }
            if(StringUtils.isBlank(certifyItem.getDirectFlag())){
				throw new IllegalArgumentException("??????????????????????????????");
            }
            if(StringUtils.isBlank(certifyItem.getCreateUser())){
				throw new IllegalArgumentException("????????????????????????");
            }
            if(certifyItem.getCreateTime()==null){
				throw new IllegalArgumentException("????????????????????????");
            }
        }

    }
    
    private void notExistLowercase(String str, String message){
		String regex=".*[a-z]+.*";
		Matcher m=Pattern.compile(regex).matcher(str);
		if(m.matches()){
			throw new IllegalArgumentException(message);
		}
	}
    private void notNull(String str, String message){
		if (StringUtils.isBlank(str)){
			throw new IllegalArgumentException(message);
		}
	}
}
