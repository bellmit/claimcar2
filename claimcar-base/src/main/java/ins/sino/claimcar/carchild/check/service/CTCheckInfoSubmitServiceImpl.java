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

	// 组织数据
    public Long  setCheckInfo(CTCheckInfoSubmitVo ctCheckInfoSubmitVo,List<CTCarInfoVo> carInfoVoList,List<CTPropInfoVo> propInfoVoList,
								List<CTPersonInfoVo> personInfoVoList,String url,String resultMsg) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long chkId = (long) 0;
		PrpLCheckTaskVo checkTaskVo = new PrpLCheckTaskVo();
		// 保存或者更新车
		List<PrpLCheckCarVo> checkCarVos = new ArrayList<PrpLCheckCarVo>();
		checkCarVos = setCarInfoVoList(checkTaskVo,ctCheckInfoSubmitVo,carInfoVoList,resultMsg);
		//Map<String,String> serialNoMap = checkHandleService.getCarLossParty(ctCheckInfoSubmitVo.getRegistNo());
		

		Map<String,String> serialNoMap = new HashMap<String,String>();
		for(PrpLCheckCarVo carvo : checkCarVos){
			String serialNo = carvo.getSerialNo().toString();
			String licNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
			serialNoMap.put(serialNo,1==carvo.getSerialNo() ? "标的车("+licNo+")" : "三者车("+licNo+")");
			serialNoMap.put("0","地面/路人损失");
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
		// checkVo.setClaimType(""); 待确定
		checkVo.setMercyFlag("1");
		checkVo.setLossType(ctCheckInfoSubmitVo.getIsLoss());
		checkVo.setCheckType(ctCheckInfoSubmitVo.getCheckType());
		// 查勘类别
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
		// 查勘案件为上海案件时，希望移动查勘的查勘任务处理界面需增加录入事故原因，案件为上海承保保单出险时控制必录。
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
				// 再查下初始化checkTask数据赋值
        		if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getCheckIdentifyNumber())){
					checkTaskVo.setCheckerIdfNo(ctCheckInfoSubmitVo.getCheckIdentifyNumber());// 查勘人身份证号
        		}else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerIdfNo())){
					checkTaskVo.setCheckerIdfNo(tempCheckTaskVo.getCheckerIdfNo());// 查勘人身份证号
        		}
        		if(StringUtils.isNotBlank(ctCheckInfoSubmitVo.getCheckPhoneNumber())){
					checkTaskVo.setCheckerPhone(ctCheckInfoSubmitVo.getCheckPhoneNumber());// 联系电话
                }else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerPhone())){
					checkTaskVo.setCheckerPhone(tempCheckTaskVo.getCheckerPhone());// 联系电话
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
		/* 待确定
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
		// List排空
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
					// //待确定
		            	for(Iterator iter = serialNoMap.entrySet().iterator();iter.hasNext();){
		            		Map.Entry entry = (Entry) iter.next();
						if(entry.getValue().toString().contains(tempList.getLossType())){// 根据名称（车牌号）取序号模糊比较
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
					// //待确定
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
					if(StringUtils.isNotBlank(tempList.getTherapeuticagency())){// 就诊医院
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
				chkId = checkHandleService.save(checkVo,disasterVo,userVo,"");// 暂存start=========
				}
			// 暂存end=========
			// 提交工作流start=========
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
				// 流入时间降序排
					Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
					@Override
					public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
							return o2.getTaskInTime().compareTo(o1.getTaskInTime());
						}
					});
					//wfTaskHandleService.tempSaveTask(prpLWfTaskVoList.get(0).getTaskId().doubleValue(),chkId.toString(),userVo.getUserCode(),userVo.getComCode());
				if("1".equals(ctCheckInfoSubmitVo.getOptionType())){// 提交
						this.checkSubmit(ctCheckInfoSubmitVo,scheduleDefLossVoList, chkId, prpLWfTaskVoList.get(0).getTaskId().longValue(), prpLWfTaskVoList.get(0).getFlowId(), 0.0,url);
					}
				}
			// 提交工作流end=========
			} catch (Exception e) {
				logger.info("车童网提交工作流保存异常信息：\n", e);
	            throw new RuntimeException(e);
		}// 暂存
            return chkId;
	}
    
    /*
	 * 设置三者车信息
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
				// 待确定
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
					if("4".equals(ctCheckInfoSubmitVo.getIndemnityDuty())){// 无责
					    prpLcheckCarVo.setCiIndemDuty("0");
					}else{
					    prpLcheckCarVo.setCiIndemDuty("1");
					}
					
					// 序号start
					// 查勘的序号
	                List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(ctCheckInfoSubmitVo.getRegistNo(),"serialNo");
					// 调度序号
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
					// 序号end
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
						resultMsg = "标的车损失险别在案件中不存在，已更新为案件保单承保的险别！";
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
					// 车型代码只获取标的的三者保存值
	                List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(ctCheckInfoSubmitVo.getRegistNo());
					if(cMainList!=null&&cMainList.size()>0){// 临时案件，无保单报案
	                    PrpLCMainVo cmainVo = cMainList.get(0);
	                    PrpLCItemCarVo cItemCarVo = cmainVo.getPrpCItemCars().get(0);
						prpLCheckCarInfo.setModelCode(cItemCarVo.getModelCode());// 车型代码
	                }else{
	                    prpLCheckCarInfo.setModelCode("");
	                }
				}else{
				    prpLCheckCarInfo.setModelCode("");
				}
				
				if(StringUtils.isNotBlank(vo.getVehiclemodleName())){
					prpLCheckCarInfo.setBrandName(vo.getVehiclemodleName());// 车型名称
				}else{
					prpLCheckCarInfo.setBrandName("");// 车型名称
				}
				prpLCheckCarInfo.setLossFlag(check.getIsLoss());
				//prpLCheckCarInfo.setUseYears("");
				prpLCheckCarInfo.setInsuredFlag("1");
				prpLCheckCarInfo.setValidFlag("1");
				// 三者相同
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
				if(StringUtils.isNotBlank(driverVo.getIdentiEffecTiveDate())){// 三者驾驶证有效日期
                    prpLCheckDriver.setDriverValidDate(formatter.parse(driverVo.getIdentiEffecTiveDate()));
                }
				prpLcheckCarVo.setPrpLCheckCarInfo(prpLCheckCarInfo);
				prpLcheckCarVo.setPrpLCheckDriver(prpLCheckDriver);
				// 待确定
				checkDutyVo.setIndemnityDuty(check.getIndemnityDuty());
				if(check.getIndemnityDuty()!=null && check.getIndemnityDutyRate() != null){
					checkDutyVo.setIndemnityDutyRate(new BigDecimal(check.getIndemnityDutyRate()));
				}
				checkDutyVo.setIsClaimSelf(ctCheckInfoSubmitVo.getExcessType());
				//checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,checkTaskInfoSubmitVo.getNextHandlerCode());
				// 保存车损
				Long carId = (long)0;
				if(prpLcheckCarVo.getCarid() == null){
					carId = checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,ctCheckInfoSubmitVo.getNextHandlerCode(),"1");
				}else{
				    carId = checkHandleService.updatePrpLCheckCar(prpLcheckCarVo,checkDutyVo,"1");
				}
				PrpLCheckCarVo checkCarVo = checkHandleService.initPrpLCheckCar(carId);
				
				prpLCheckCarVoList.add(prpLcheckCarVo);// 车估损金额
			}
			 checkTaskVo.setSumLossCarFee(sumLossCarFee);
		} catch (Exception e) {
			// TODO: handle exception
			logger.info("车童网查勘提交设置车辆信息异常报错：\n", e);
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
				// 保存公估信息
				checkHandleService.saveAssessor(ctCheckInfoSubmitVo.getRegistNo(),userVo);
				// 保存查勘费信息
				checkHandleService.saveCheckFee(ctCheckInfoSubmitVo.getRegistNo(), userVo);
				// 查勘提交
				checkHandleService.submitCheckToDloss(defLossVos,sub_checkId,sub_flowTaskId,sub_flowId,sub_LossFees,userVo,null,null);
			}catch(Exception e){
				logger.info("查勘提交失败！"+e.getMessage());
				e.printStackTrace();
		        throw new RuntimeException(e);
			}
		}else if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
			logger.info("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
			throw new RuntimeException("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
		}else{
			logger.info("任务错误！");
			throw new RuntimeException("任务错误！");
		}
	
	}
    
    @Override
	public CTCheckInfoSubmitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		// 返回的vo
		CTCheckInfoSubmitResVo resVo = new CTCheckInfoSubmitResVo();
		List<CTCarInfoVo> carInfoVoList = new ArrayList<CTCarInfoVo>();
		List<CTPropInfoVo> propInfoVoList = new ArrayList<CTPropInfoVo>();
		List<CTPersonInfoVo> personInfoVoList = new ArrayList<CTPersonInfoVo>();
		CarchildHeadVo carchildHeadVo = new CarchildHeadVo();
		CarchildResponseHeadVo head = new CarchildResponseHeadVo();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo ="";
		stream.processAnnotations(CTCheckInfoSubmitReqVo.class);
		CTCheckInfoSubmitReqVo checkInfoSubmitReqVo = (CTCheckInfoSubmitReqVo) arg1;
		String xml = stream.toXML(checkInfoSubmitReqVo);
		logger.info("车童网现场查勘接口接收报文: \n"+xml);
		CTCheckInfoSubmitVo ctCheckInfoSubmitVo = checkInfoSubmitReqVo.getBody().getCheckInfoSubmitVo();
		List<CTCertifyItem> certifyItemList = checkInfoSubmitReqVo.getBody().getCertifyItemlist();
		String resultMsg = null;
		// 加锁开始
		String lockFlags = "1";

		String errMsg = "";
		String errNo = "1";
		
		try{
			registNo = ctCheckInfoSubmitVo.getRegistNo();
			Long lockYwMainId = Long.valueOf(ctCheckInfoSubmitVo.getCheckId());
			lockService.savePrplLockList(lockYwMainId, FlowNode.Check.name());
			lockFlags = "0";// 加锁
			logger.info("加锁"+new Date());
			carInfoVoList = ctCheckInfoSubmitVo.getCarInfo();
			propInfoVoList = ctCheckInfoSubmitVo.getPropInfo();
			personInfoVoList = ctCheckInfoSubmitVo.getPersonInfo();

			carchildHeadVo = checkInfoSubmitReqVo.getHead();
			if (!"CT_004".equals(carchildHeadVo.getRequestType())|| !"claim_user".equals(carchildHeadVo.getUser())|| !"claim_psd".equals(carchildHeadVo.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			if(!StringUtils.isNotBlank(ctCheckInfoSubmitVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			// 查勘提交需要校验，同步不需要校验
			if("1".equals(ctCheckInfoSubmitVo.getOptionType())){
				checkRequest(ctCheckInfoSubmitVo);
			}
			
			
			PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
			if("1".equals(ctCheckInfoSubmitVo.getOptionType())){
				if("on".equals(checkVo.getOperateStatus())){
					throw new IllegalArgumentException("当前操作人员有查勘任务正在提交，请等待或刷新后再试");
				}
				checkVo.setOperateStatus("on");
	            checkHandleService.updateCheckMain(checkVo);
			}
			// 管控提交后不能再提交start
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(ctCheckInfoSubmitVo.getRegistNo(), FlowNode.Chk.toString());
            if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
				// 流入时间降序排
                Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                @Override
                public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                        return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                    }
                });
                
                PrpLWfTaskVo vo = prpLWfTaskVoList.get(0);
                if(CodeConstants.HandlerStatus.END.equals(vo.getHandlerStatus())){
					// throw new IllegalArgumentException("任务已提交或者注销！提交人："+vo.getHandlerUser());
                	errNo = "2";
					errMsg = "任务已提交或者注销！提交人："+vo.getHandlerUser();
                }else{
					if( !"1".equals(ctCheckInfoSubmitVo.getOptionType())){// 任务权限移交
                		vo.setIsMobileAccept("0");
                		wfTaskHandleService.updateTaskIn(vo);
                	}
                	String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
					logger.info("移动查勘地址================="+url);
					Long checkId = setCheckInfo(ctCheckInfoSubmitVo,carInfoVoList,propInfoVoList,personInfoVoList,url,resultMsg);
					// 查询是否有财产损失
        			if ( propInfoVoList == null || propInfoVoList.size() <= 0 ) {
        				checkVo.setIsPropLoss("0");
        			} else{
        				checkVo.setIsPropLoss("1");
        			}
					// 查询是否人伤
        			if ( personInfoVoList == null || personInfoVoList.size() <= 0 ) {
        				checkVo.setIsPersonLoss("0");
        			} else{
        				checkVo.setIsPersonLoss("1");
        			}
                }
            }else{
            	errNo = "0";
				errMsg = "没有查询到查勘任务！";
            }
			
			// 单证目录
			if(certifyItemList!=null && certifyItemList.size()>0 && "1".equals(errNo)){
				try{
					checkCertifyItemInfo(certifyItemList);
		            List<PrpLCertifyItemVo> prpLCertifyItemVoList = new ArrayList<PrpLCertifyItemVo>();
		            for(CTCertifyItem certifyItem : certifyItemList){
						// 主表
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
						// 子表
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
					errMsg = errMsg+"\n单证目录保存报错："+e.getMessage();
					logger.info("车童网单证目录保存异常信息：\n", e);
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
				errMsg = "车童网现场查勘接口数据不能重复提交:"+e.getMessage();
				logger.info("车童网现场查勘接口Insert或Update数据时违反了完整性：\n",e);
        	}else{
        		errNo = "0";
				errMsg = "现场查勘报错："+e.getMessage();
				logger.info("车童网现场查勘接口异常信息：\n ",e);
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
				logger.info("车童网现场查勘接口更新操作状态:registNo={},错误信息：{} ", registNo, e.getMessage());
			}
			// 删除加锁
			if("0".equals(lockFlags)){
				lockService.deletePrplLockListById(Long.valueOf(ctCheckInfoSubmitVo.getCheckId()), FlowNode.Check.name());
				logger.info("删除加锁"+new Date());
			}
		}

		
		stream.processAnnotations(CTCheckInfoSubmitResVo.class);
		logger.info("车童网现场查勘接口返回报文=========：\n"+stream.toXML(resVo));
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
		
		// 查勘记录
		notNull(CheckInfoSubmitVo.getDamageCode()," 出险原因为空  ");
		notNull(CheckInfoSubmitVo.getCheckSite()," 查勘地点为空  ");
		notNull(CheckInfoSubmitVo.getCheckType()," 查勘类型为空  ");
		notNull(CheckInfoSubmitVo.getCheckIdentifyNumber()," 查勘人身份证号为空  ");
		notExistLowercase(CheckInfoSubmitVo.getCheckIdentifyNumber()," 查勘人身份证号不能包含小写字母  ");
		notNull(CheckInfoSubmitVo.getCheckPhoneNumber()," 查勘人联系电话为空  ");
		notNull(CheckInfoSubmitVo.getFirstSiteFlag()," 是否第一现场查勘为空  ");
		notNull(CheckInfoSubmitVo.getExcessType()," 是否互碰自赔为空  ");
		notNull(CheckInfoSubmitVo.getIsIncludeProp()," 是否包含财损为空  ");
		notNull(CheckInfoSubmitVo.getIsIncludePerson()," 是否包含人伤为空  ");
		notNull(CheckInfoSubmitVo.getIsunilAccident(),"是否单车事故为空");
		
		//校验查勘人身份证是否正常
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
			throw new IllegalArgumentException("车辆信息为空");
		}
		// 单车事故不允许传三者车，多车事故必须传三者车
		if(CodeConstants.IsSingleAccident.YES.equals(CheckInfoSubmitVo.getIsunilAccident()) && carList.size()>1){
			throw new IllegalArgumentException("单车事故不允许上传三者车信息");
		}else if(CodeConstants.IsSingleAccident.NOT.equals(CheckInfoSubmitVo.getIsunilAccident()) && carList.size()==1){
			throw new IllegalArgumentException("多车事故必须上传三者车信息");
		}
		// 是否互碰自赔只能传0或1
		if(!CodeConstants.caseFlagType.NORMAL_CASE.equals(CheckInfoSubmitVo.getExcessType()) 
				&& !CodeConstants.caseFlagType.SELF_CASE.equals(CheckInfoSubmitVo.getExcessType())){
			throw new IllegalArgumentException("是否互碰自赔数据格式错误！");
		}
		//是否互碰自赔只能传0或1
		if(!"0".equals(CheckInfoSubmitVo.getExcessType()) && !"1".equals(CheckInfoSubmitVo.getExcessType())){
			throw new IllegalArgumentException("是否互碰自赔数据格式错误！");
		}
		if(StringUtils.isBlank(CheckInfoSubmitVo.getIndemnityDuty())){
			throw new IllegalArgumentException("事故责任为空");
		}
		if(StringUtils.isBlank(CheckInfoSubmitVo.getIndemnityDutyRate())){
			throw new IllegalArgumentException("事故责任比例为空");
		}
		notNull(CheckInfoSubmitVo.getIsBigCase()," 是否重大赔案上报为空  ");
		notNull(CheckInfoSubmitVo.getSubrogateType()," 是否代位求偿为空  ");
		notNull(CheckInfoSubmitVo.getTextType()," 本次查勘报告为空  ");
		
		if("1".equals(CheckInfoSubmitVo.getSubrogateType())){
			throw new IllegalArgumentException("代位求偿案件必须同步到电脑端处理，移动终端不处理此类型案件");
		}
		
		// 车辆信息
		if(carList!=null && carList.size()>0){
			for(CTCarInfoVo carInfo:carList){
				notNull(carInfo.getIfObject()," 类别为空  ");
				notNull(carInfo.getLicenseNo()," 车牌号为空  ");
				notNull(carInfo.getMotorTypeCode()," 车辆种类为空  ");
				notNull(carInfo.getLicenseType()," 车牌种类为空  ");
				notNull(carInfo.getEngineNo()," 发动机号为空  ");
				notNull(carInfo.getFrameNo()," 车架号为空  ");
				notNull(carInfo.getVin()," VIN码为空  ");
				notNull(carInfo.getOwern()," 车主为空  ");
				// 驾驶人信息
				CTDriverVo driver = carInfo.getDriver();
				notNull(driver.getDriverName()," 驾驶人姓名为空  ");
				notNull(driver.getCertiType()," 证件类型为空  ");
				notNull(driver.getIdentifyNumber()," 证件号码为空  ");
				notExistLowercase(driver.getIdentifyNumber()," 证件号码不能包含小写字母  ");
				notNull(driver.getDrivingLicenseNo()," 驾驶证号码为空  ");
				notExistLowercase(driver.getDrivingLicenseNo()," 驾驶证号码不能包含小写字母  ");
				notNull(driver.getPhoneNumber()," 电话为空  ");
				// 车辆查勘信息
				CTCheckVo check = carInfo.getCheck();
				notNull(check.getEstimatedLoss()," 估损金额为空  ");
				if(StringUtils.isBlank(check.getIndemnityDuty())){
					throw new IllegalArgumentException("事故责任为空");
				}
				if(StringUtils.isBlank(check.getIndemnityDutyRate())){
					throw new IllegalArgumentException("事故责任比例为空");
				}
				notNull(check.getLossPart()," 损失部位为空  ");
				//如果驾驶人证件号码为身份证类型，判断是否与查勘人员身份证号码是否存在相同
				if(CheckInfoSubmitVo.getCheckIdentifyNumber().equals(driver.getIdentifyNumber())){
					throw new IllegalArgumentException("查勘人员身份证号不能和驾驶员身份证号一致！请修改！");
				}
			}
			String message = checkHandleService.saveValidCars(carList);
			if(StringUtils.isNotBlank(message)){
				throw new IllegalArgumentException(message);
			}
		}
		
		// 人伤信息
		if(personList!=null && personList.size()>0){
			for(CTPersonInfoVo personInfo:personList){
				notNull(personInfo.getIsAdjust()," 是否现场调解为空  ");
				notNull(personInfo.getName()," 姓名为空  ");
				notNull(personInfo.getSex()," 性别为空  ");
				notNull(personInfo.getCertiType()," 证件类型为空  ");
				notNull(personInfo.getIdentifyNumber()," 证件号码为空  ");
				notExistLowercase(personInfo.getIdentifyNumber(),"伤员证件号码不能包含小写字母");
				//校验身份证是否正常
				if(StringUtils.isNotBlank(personInfo.getIdentifyNumber()) && "1".equals(personInfo.getCertiType())){
					IDInfo info = idUtils.IDCardValidate(personInfo.getIdentifyNumber());
					if(StringUtils.isNotBlank(info.getErrMsg())){
						throw new IllegalArgumentException(info.getErrMsg());
					}
				}
				//判断是否与查勘人员身份证号码是否存在相同
				if(CheckInfoSubmitVo.getCheckIdentifyNumber().equals(personInfo.getIdentifyNumber())){
					throw new IllegalArgumentException("伤员证件号码与查勘人员身份证号一致！请修改！");
				}
				notNull(personInfo.getDegree()," 伤情类别为空  ");
				notNull(personInfo.getSumClaim()," 估损金额为空  ");
			}
		}
		
		// 物损信息
		if(propList != null && propList.size()>0){
			for(CTPropInfoVo propInfo:propList){
				notNull(propInfo.getLossName()," 损失名称为空  ");
				notNull(propInfo.getLossNum()," 损失数量为空  ");
				notNull(propInfo.getPayAmount()," 赔偿金额为空  ");
			}
		}
		
	}
	
    public void checkCertifyItemInfo(List<CTCertifyItem> certifyItemList){
        for(CTCertifyItem certifyItem : certifyItemList){
            if(StringUtils.isBlank(certifyItem.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
            }
            if(StringUtils.isBlank(certifyItem.getCertifyTypeCode())){
				throw new IllegalArgumentException("单证分类代码不能为空");
            }
            if(StringUtils.isBlank(certifyItem.getCertifytypeName())){
				throw new IllegalArgumentException("单证分类名称不能为空");
            }
            if(StringUtils.isBlank(certifyItem.getDirectFlag())){
				throw new IllegalArgumentException("收集齐全标志不能为空");
            }
            if(StringUtils.isBlank(certifyItem.getCreateUser())){
				throw new IllegalArgumentException("上传人员不能为空");
            }
            if(certifyItem.getCreateTime()==null){
				throw new IllegalArgumentException("上传时间不能为空");
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
