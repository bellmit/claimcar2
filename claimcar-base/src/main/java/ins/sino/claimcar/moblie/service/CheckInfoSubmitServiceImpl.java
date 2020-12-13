/******************************************************************************
 * CREATETIME : 2015年12月8日 上午10:19:01
 ******************************************************************************/
package ins.sino.claimcar.moblie.service;

import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.IDCardUtil;
import ins.platform.utils.IDCardUtil.IDInfo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CheckClass;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.CheckActionVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDriverVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckExtVo;
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
import ins.sino.claimcar.mobile.check.vo.AccountInfoVo;
import ins.sino.claimcar.mobile.check.vo.CarInfoResVo;
import ins.sino.claimcar.mobile.check.vo.CarInfoVo;
import ins.sino.claimcar.mobile.check.vo.CheckExtendInfoVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoSubmitReqBodyVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoSubmitReqVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoSubmitResBodyVo;
import ins.sino.claimcar.mobile.check.vo.CheckInfoSubmitResVo;
import ins.sino.claimcar.mobile.check.vo.CheckResVo;
import ins.sino.claimcar.mobile.check.vo.CheckTaskInfoSubmitResVo;
import ins.sino.claimcar.mobile.check.vo.CheckTaskInfoSubmitVo;
import ins.sino.claimcar.mobile.check.vo.CheckVo;
import ins.sino.claimcar.mobile.check.vo.DriverResVo;
import ins.sino.claimcar.mobile.check.vo.DriverVo;
import ins.sino.claimcar.mobile.check.vo.PersonInfoResVo;
import ins.sino.claimcar.mobile.check.vo.PersonInfoVo;
import ins.sino.claimcar.mobile.check.vo.PhotoInfo;
import ins.sino.claimcar.mobile.check.vo.PropInfoResVo;
import ins.sino.claimcar.mobile.check.vo.PropInfoVo;
import ins.sino.claimcar.mobile.vo.FlowInfoInitReqVo;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.mobilecheck.service.LockService;
import ins.sino.claimcar.mobilecheck.service.MobileCheckService;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 移动端查勘提交/同步接口（快赔请求理赔）
 * @author ★zhujunde
 */
public class CheckInfoSubmitServiceImpl implements ServiceInterface {
	
	private static Logger logger = LoggerFactory.getLogger(CheckInfoSubmitServiceImpl.class);

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
	MobileCheckService mobileCheckService;
	@Autowired
	ScheduleService scheduleService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    LockService lockService;
    @Autowired 
    CodeTranService codeTranService;
    @Autowired
    ManagerService managerService;
    
	public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
	public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
	


	// 组织数据
	public Long  setCheckInfo(List<CarInfoResVo> carInfoResVo,CheckTaskInfoSubmitVo checkTaskInfoSubmitVo,List<CarInfoVo> carInfoVoList,List<PropInfoVo> propInfoVoList,
List<PersonInfoVo> personInfoVoList,List<AccountInfoVo> accountInfoVoList,String url,
								CheckExtendInfoVo extendInfoVo,List<PhotoInfo> photoInfoList,String resultMsg) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long chkId = (long) 0;
		PrpLCheckTaskVo checkTaskVo = new PrpLCheckTaskVo();
		// 保存或者更新车
		List<PrpLCheckCarVo> checkCarVos = new ArrayList<PrpLCheckCarVo>();
		checkCarVos = setCarInfoVoList(carInfoResVo,checkTaskVo,checkTaskInfoSubmitVo,carInfoVoList,resultMsg);
		//Map<String,String> serialNoMap = checkHandleService.getCarLossParty(checkTaskInfoSubmitVo.getRegistNo());
		

		Map<String,String> serialNoMap = new HashMap<String,String>();
		for(PrpLCheckCarVo carvo : checkCarVos){
			String serialNo = carvo.getSerialNo().toString();
			String licNo = carvo.getPrpLCheckCarInfo().getLicenseNo();
			serialNoMap.put(serialNo,1==carvo.getSerialNo() ? "标的车("+licNo+")" : "三者车("+licNo+")");
			serialNoMap.put("0","地面/路人损失");
		}
		
		PrpLCheckVo checkVo = new PrpLCheckVo();
		checkVo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
		if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getCheckId())){
			checkVo.setId(Long.valueOf(checkTaskInfoSubmitVo.getCheckId()));
		}
		checkVo.setSingleAccidentFlag(checkTaskInfoSubmitVo.getIsunilAccident());
		checkVo.setDamageTypeCode(checkTaskInfoSubmitVo.getDamageTypeCode());
		checkVo.setManageType(checkTaskInfoSubmitVo.getManageTypeName());
		// 查勘案件为上海案件时，希望移动查勘的查勘任务处理界面需增加录入事故原因，案件为上海承保保单出险时控制必录。
    	if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getAccidentReason())){
    	    checkVo.setAccidentReason(checkTaskInfoSubmitVo.getAccidentReason());
        }else{
            checkVo.setAccidentReason("");
        } 
		checkVo.setDamageCode(checkTaskInfoSubmitVo.getDamageCode());
		// checkVo.setClaimType(""); 待确定
		checkVo.setMercyFlag("1");
		checkVo.setLossType("0");// 损失类型
		checkVo.setCheckType(checkTaskInfoSubmitVo.getCheckType());
		// 查勘类别
		PrpdIntermMainVo intermVo = managerService.findIntermByUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
		if(intermVo!=null){
			checkVo.setRemark(intermVo.getIntermCode());
		}
		checkVo.setCheckClass(intermVo!=null ? CheckClass.CHECKCLASS_Y : CheckClass.CHECKCLASS_N);
		checkVo.setIsClaimSelf(checkTaskInfoSubmitVo.getExcessType());
		checkVo.setIsPropLoss(checkTaskInfoSubmitVo.getIsIncludeProp());
		checkVo.setIsPersonLoss(checkTaskInfoSubmitVo.getIsIncludePerson());
		checkVo.setIsSubRogation(checkTaskInfoSubmitVo.getSubrogateType());
		checkVo.setMajorCaseFlag(checkTaskInfoSubmitVo.getIsBigCase());
		checkVo.setChkSubmitTime(new Date());
		checkVo.setValidFlag("1");
//		checkVo.setCreateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
//		checkVo.setCreateTime(new Date());
		checkVo.setUpdateTime(new Date());
		checkVo.setUpdateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
		checkVo.setNoDutyFlag(checkTaskInfoSubmitVo.getNoDutyFlag());
		checkVo.setNoDutyReason(checkTaskInfoSubmitVo.getNoDutyReason());
        checkVo.setDamOtherCode(checkTaskInfoSubmitVo.getDamOtherCode());

        if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getTaskId())){
        	PrpLCheckVo prpLCheckVo = checkTaskService.findCheckVoByRegistNo(checkTaskInfoSubmitVo.getRegistNo());
        	PrpLCheckTaskVo tempCheckTaskVo = prpLCheckVo.getPrpLCheckTask();
        	if(tempCheckTaskVo != null){
        		checkTaskVo.setId(tempCheckTaskVo.getId());
				// 再查下初始化checkTask数据赋值
        		if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getCheckIdentifyNumber())){
					checkTaskVo.setCheckerIdfNo(checkTaskInfoSubmitVo.getCheckIdentifyNumber());// 查勘人身份证号
        		}else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerIdfNo())){
					checkTaskVo.setCheckerIdfNo(tempCheckTaskVo.getCheckerIdfNo());// 查勘人身份证号
        		}
        		if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getCheckPhoneNumber())){
					checkTaskVo.setCheckerPhone(checkTaskInfoSubmitVo.getCheckPhoneNumber());// 联系电话
                }else if(StringUtils.isNotBlank(tempCheckTaskVo.getCheckerPhone())){
					checkTaskVo.setCheckerPhone(tempCheckTaskVo.getCheckerPhone());// 联系电话
                }
                
        	}
		}
        checkTaskVo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(checkTaskInfoSubmitVo.getRegistNo());
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
			checkTaskVo.setCheckDate(formatter.parse(checkTaskInfoSubmitVo.getCheckDate()));
		} 
        catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		}
        checkTaskVo.setCheckAddress(checkTaskInfoSubmitVo.getCheckSite());
        checkTaskVo.setFirstAddressFlag(checkTaskInfoSubmitVo.getFirstSiteFlag());
        checkTaskVo.setCheckerCode(checkTaskInfoSubmitVo.getNextHandlerCode());
        checkTaskVo.setChecker(checkTaskInfoSubmitVo.getNextHandlerName());
        checkTaskVo.setContexts(checkTaskInfoSubmitVo.getTextType());
		/* 待确定
		 checkTaskVo.setSumRescueFee("");
		 checkTaskVo.setSumLossCarFee(sumLossCarFee);
		 checkTaskVo.setSumLossPropFee("");
		 checkTaskVo.setSumLossPersnFee("");*/
        checkTaskVo.setComCode(checkTaskInfoSubmitVo.getScheduleObjectId());
        checkTaskVo.setMakeCom(checkTaskInfoSubmitVo.getScheduleObjectId());
        checkTaskVo.setVerifyCheckFlag("1");
        //checkTaskVo.setUnderWriteState("");
        checkTaskVo.setDeflossRepairType("");
        //checkTaskVo.setRepairFee("");
        checkTaskVo.setUnderWriteDate(new Date());
        checkTaskVo.setUnderWriteUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
        //checkTaskVo.setVerifyCheckContext("");
        checkTaskVo.setValidFlag("1");
        checkTaskVo.setCreateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
        checkTaskVo.setCreateTime(new Date());
        checkTaskVo.setUpdateTime(new Date());
        checkTaskVo.setUpdateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
        

		SysUserVo userVo = new SysUserVo();
		userVo.setComCode(checkTaskInfoSubmitVo.getScheduleObjectId());
		userVo.setUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
		userVo.setUserName(checkTaskInfoSubmitVo.getNextHandlerName());
		userVo.setComName(checkTaskInfoSubmitVo.getScheduleObjectName());
		// List排空
	        List<PrpLCheckPropVo> newPropList = new ArrayList<PrpLCheckPropVo>();
	        BigDecimal sumLossPropFee = new BigDecimal(0);
	        if(propInfoVoList!=null){
	        	for(int i=0;i<propInfoVoList.size();i++) {
		        	PropInfoVo tempList = propInfoVoList.get(i);
		            if(tempList!=null){
		            	PrpLCheckPropVo prpLCheckPropVo = new PrpLCheckPropVo();
		            	if(StringUtils.isNotBlank(tempList.getPropId())){
		            		prpLCheckPropVo.setId(Long.valueOf(tempList.getPropId()));
		            	}
		            	else{
		            		prpLCheckPropVo.setId(null);
		            	}
		            	prpLCheckPropVo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
					// //待确定
		            	for(Iterator iter = serialNoMap.entrySet().iterator();iter.hasNext();){
		            		Map.Entry entry = (Entry) iter.next();
						if(entry.getValue().toString().contains(tempList.getLossType())){// 根据名称（车牌号）取序号模糊比较
		            			Long lossPartyId =  Long.parseLong((String) entry.getKey());
		            			prpLCheckPropVo.setLossPartyId(lossPartyId);
		            		}
		            	}
		            	prpLCheckPropVo.setPropSerialNo(tempList.getPropSerialNo());
		            	//prpLCheckPropVo.setLossPartyId(Long.valueOf("2"));
		            	prpLCheckPropVo.setLossPartyName(tempList.getLossType());
		            	prpLCheckPropVo.setLossItemName(tempList.getLossName());
		            	if(StringUtils.isNotBlank(tempList.getLossNum())){
		            		prpLCheckPropVo.setLossNum(String.valueOf(Double.valueOf(tempList.getLossNum()).intValue()));	
		            	}
					prpLCheckPropVo.setLossDegreeCode("01");// 默认类型
					// prpLCheckPropVo.setLossFeeType("");不传
		            	if(tempList.getPayAmount()!=null){
		            		sumLossPropFee = sumLossPropFee.add(new BigDecimal(tempList.getPayAmount()));
		            		prpLCheckPropVo.setLossFee(new BigDecimal(tempList.getPayAmount()));
		            	}
					prpLCheckPropVo.setLossFeeType(tempList.getLossFeeType());// 损失单位
					prpLCheckPropVo.setIsNoClaim(RadioValue.RADIO_NO);// 报案时传递给移动端的财产有损所以无需赔付为否
		            	prpLCheckPropVo.setValidFlag("1");
		            	prpLCheckPropVo.setCreateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
		            	prpLCheckPropVo.setCreateTime(new Date());
		            	prpLCheckPropVo.setUpdateTime(new Date());
		            	prpLCheckPropVo.setUpdateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
		            	
		            	
		            	newPropList.add(prpLCheckPropVo);
		            }
		        }
	        }
	        checkTaskVo.setSumLossPropFee(sumLossPropFee);
	        List<PrpLCheckPersonVo> newPersonList = new ArrayList<PrpLCheckPersonVo>();
	        BigDecimal sumLossPersnFee = new BigDecimal(0);
	        if(personInfoVoList != null){
	        	for(int i=0;i<personInfoVoList.size();i++) {
		        	PersonInfoVo tempList = personInfoVoList.get(i);
		            if(tempList!=null){
		            	checkVo.setReconcileFlag(tempList.getIsAdjust());
		            	checkVo.setPersHandleType(tempList.getPersHandleType());
		            	PrpLCheckPersonVo prpLCheckPersonVo = new PrpLCheckPersonVo();
		            	if(StringUtils.isNotBlank(tempList.getPersonId())){
		            		prpLCheckPersonVo.setId(Long.valueOf(tempList.getPersonId()));
		            	}
		            	else{
		            		prpLCheckPersonVo.setId(null);
		            	}
		            	
		            	prpLCheckPersonVo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
					// //待确定
		            	for(Map.Entry<String, String> serialNo : serialNoMap.entrySet()){
		            		if(serialNo.getValue().contains(tempList.getLosstype())){
		            			prpLCheckPersonVo.setLossPartyId(Long.valueOf(serialNo.getKey()));
		            		}
		            	}
		            	prpLCheckPersonVo.setPersonSerialNo(tempList.getPersonSerialNo());
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
		            	prpLCheckPersonVo.setCheckDispose(tempList.getSurverytpe());
		            	prpLCheckPersonVo.setWoundDetail(tempList.getDegreedesc());
		            	prpLCheckPersonVo.setValidFlag("1");
		            	prpLCheckPersonVo.setCreateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
		            	prpLCheckPersonVo.setCreateTime(new Date());
		            	prpLCheckPersonVo.setUpdateTime(new Date());
		            	prpLCheckPersonVo.setUpdateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
		            	prpLCheckPersonVo.setInjuredPart(tempList.getPart());
		            	newPersonList.add(prpLCheckPersonVo);
		            }
		        }
	        }
	        checkTaskVo.setSumLossPersnFee(sumLossPersnFee);
	        List<PrpLCheckExtVo> newExtList = new ArrayList<PrpLCheckExtVo>();
	        if(extendInfoVo!=null && StringUtils.isNotBlank(extendInfoVo.getLicenseEffective())){
	        	PrpLCheckExtVo checkExtVo = new PrpLCheckExtVo();
			checkExtVo.setCheckExtName("保险车辆行驶证是否有效");
		        checkExtVo.setColumnValue(extendInfoVo.getLicenseEffective());
		        newExtList.add(checkExtVo);
	        }
	        
		// 组织业务数据
			//checkHandleService.updateCheckMain(prpLcheckVo);
			checkTaskVo.setPrpLCheckProps(newPropList);
			checkTaskVo.setPrpLCheckPersons(newPersonList);
			checkTaskVo.setPrpLCheckExts(newExtList);
			checkVo.setPrpLCheckTask(checkTaskVo);
			
			/*if(disasterVo.getId()==null && "".equals(disasterVo.getDisasterCodeOne())
					&& "".equals(disasterVo.getDisasterCodeTwo())){
				disasterVo = null;
			}*/
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
				if("3".equals(checkTaskInfoSubmitVo.getOptionType())){// 提交
						this.checkSubmit(checkTaskInfoSubmitVo,scheduleDefLossVoList, chkId, prpLWfTaskVoList.get(0).getTaskId().longValue(), prpLWfTaskVoList.get(0).getFlowId(), 0.0,url,photoInfoList);
					}
					//this.checkSubmit(checkTaskInfoSubmitVo,scheduleDefLossVoList, chkId, prpLWfTaskVoList.get(0).getTaskId().longValue(), prpLWfTaskVoList.get(0).getFlowId(), 0.0,url);

				// System.out.println("是否提交====================+checkTaskInfoSubmitVo.getOptionType()"+checkTaskInfoSubmitVo.getOptionType());
				}
			// 提交工作流end=========
				
				
						
			} catch (Exception e) {
			    e.printStackTrace();
	            throw new RuntimeException(e);
		}// 暂存
            return chkId;
			
		// 更新标的车duty
			//checkHandleService.updateMainCarDuty(checkDutyVo,checkVo,userVo.getUserCode());
			
		// 保存代位信息
			/*Long subr = checkHandleService.saveSubrogationMain(subrogationMainVo,chkId,userVo.getUserCode());
		checkHandleService.saveCharge(lossChargeVos,chkId,userVo);//查勘费用赔款信息（除公估费）
		*/			
		/*//更新免赔率
		if(claimDeductVos != null && claimDeductVos.size() > 0){
			checkHandleService.updateClaimDeduct(claimDeductVos,checkVo.getRegistNo());
		}*/
			
			/*ajaxResult.setStatus(HttpStatus.SC_OK);
			ajaxResult.setData(chkId);
			ajaxResult.setStatusText(subr.toString());
			session.setAttribute("checkId",chkId);*/
			
			
	}
	
	
	public void checkSubmit(CheckTaskInfoSubmitVo checkTaskInfoSubmitVo,List<PrpLScheduleDefLossVo> defLossVos,
	        Long sub_checkId,Long sub_flowTaskId,String sub_flowId,double sub_LossFees,String url,List<PhotoInfo> photoInfoList){

		SysUserVo userVo = new SysUserVo();
		userVo.setComCode(checkTaskInfoSubmitVo.getScheduleObjectId());
		userVo.setUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
		userVo.setUserName(checkTaskInfoSubmitVo.getNextHandlerName());
		userVo.setComName(checkTaskInfoSubmitVo.getScheduleObjectName());
		//AjaxResult ajaxResult = new AjaxResult();
		/*String status = (String)session.getAttribute("checkSubmit_status");
		if("doing".equals(status)) {
			//提示前端有操作正在提交
			ajaxResult.setStatusText("当前操作人员有查勘任务正在提交，请等待或刷新后再试....");
		}*/
		PrpLWfTaskVo wfTaskVo=wfTaskHandleService.queryTask(sub_flowTaskId.doubleValue());
		if(CodeConstants.HandlerStatus.DOING.equals(wfTaskVo.getHandlerStatus())){
			//session.setAttribute("checkSubmit_status","doing");
			try{
				
				// 查勘提交
				checkHandleService.submitCheckToDloss(defLossVos,sub_checkId,sub_flowTaskId,sub_flowId,sub_LossFees,userVo,"1",photoInfoList);
				// 保存移动端案件显示标志
				PrpLWfTaskVo wfTaskOutVo=wfTaskHandleService.queryTask(sub_flowTaskId.doubleValue());
				String bussTag = wfTaskOutVo.getBussTag();
				JSONObject json = JSONObject.parseObject(bussTag);
				Map<String,String> map = JSONObject.toJavaObject(json,Map.class);
				map.put("isMobileCase", "1");
				wfTaskOutVo.setBussTag(JSONObject.toJSONString(map));
				wfTaskHandleService.updateTaskOut(wfTaskOutVo);
				
				// 保存公估信息
				PrpLCheckVo checkVo = checkHandleService.findPrpLCheckVoById(sub_checkId);
				
				checkHandleService.saveAssessor(checkVo.getRegistNo(),userVo);
				//查勘费
				checkHandleService.saveCheckFee(checkVo.getRegistNo(), userVo);
				// 返回下一节点处理界面
				/*if(wfTaskVos != null && wfTaskVos.size() > 0){
					String idList = "";
					for(PrpLWfTaskVo task : wfTaskVos){
						if( !FlowNode.Certi.name().equals(task.getNodeCode())){
							idList += task.getTaskId().toString()+",";
						}
					}
					if(StringUtils.isNotBlank(idList)){
						ajaxResult.setData(idList.substring(0,idList.length()-1));
					}
				}*/
				// 调用调度人员接口
				/*PrpLScheduleTaskVo prpLScheduleTaskVo = new PrpLScheduleTaskVo();
				List<PrpLScheduleTaskVo> prpLScheduleTaskVoList = scheduleService.getScheduleTaskByregistNo(checkVo.getRegistNo());
				for(PrpLScheduleTaskVo vo :prpLScheduleTaskVoList){
					if(vo.getCheckAddress().equals(checkVo.getPrpLCheckTask().getCheckAddress())){
						prpLScheduleTaskVo = vo;
								break;
					}
				}
				setReassignmentes(prpLScheduleTaskVo, "", defLossVos,url);*/
			}catch(Exception e){
				//ajaxResult.setStatusText(e.getMessage());
				logger.info("查勘提交失败！"+e.getMessage());
				e.printStackTrace();
		        throw new RuntimeException(e);
			}
			//session.removeAttribute("checkSubmit_status");
		}else if(CodeConstants.HandlerStatus.END.equals(wfTaskVo.getHandlerStatus())){
			// ajaxResult.setStatusText("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
			logger.info("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
			throw new RuntimeException("任务已提交或者注销！提交人："+wfTaskVo.getHandlerUser());
		}else{
			logger.info("任务错误！");
			// sajaxResult.setStatusText("任务错误！");
			throw new RuntimeException("任务错误！");
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
	
	/*
	 * 设置三者车信息
	 */
	public List<PrpLCheckCarVo> setCarInfoVoList(List<CarInfoResVo> carInfoResVo,PrpLCheckTaskVo checkTaskVo,
													CheckTaskInfoSubmitVo checkTaskInfoSubmitVo,List<CarInfoVo> carInfoVoList,String resultMsg) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<PrpLCheckCarVo> prpLCheckCarVoList = new ArrayList<PrpLCheckCarVo>();
		BigDecimal sumLossCarFee = new BigDecimal(0);
		
//		int i = 0;
//		for(CarInfoVo vo : carInfoVoList){
//			if(StringUtils.isNotBlank(vo.getCarId())){
//				++i;
//			}
//		}
		try {
			for(CarInfoVo vo : carInfoVoList){
				PrpLCheckCarVo prpLcheckCarVo = new PrpLCheckCarVo();
				PrpLCheckCarInfoVo prpLCheckCarInfo = new PrpLCheckCarInfoVo();
				PrpLCheckDriverVo prpLCheckDriver = new PrpLCheckDriverVo();
				CheckVo check = vo.getCheck();
				DriverVo driverVo = vo.getDriver();
				PrpLCheckDutyVo checkDutyVo = new PrpLCheckDutyVo();
				// 待确定
				//int j = ++i;
				//System.out.println("=========================="+j);
				
				prpLcheckCarVo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
				prpLcheckCarVo.setRescueFee(new BigDecimal(0));// 施救费默认为0
				if(StringUtils.isNotBlank(check.getLossId())){
					prpLcheckCarVo.setCarid(Long.valueOf(check.getLossId()));
                    prpLcheckCarVo.setSerialNo(Integer.valueOf(vo.getSerialNo()));
				}else{
					prpLcheckCarVo.setCarid(null);
					if("4".equals(checkTaskInfoSubmitVo.getIndemnityDuty())){// 无责
					    prpLcheckCarVo.setCiIndemDuty("0");
					}else{
					    prpLcheckCarVo.setCiIndemDuty("1");
					}
					
					// 序号start
					// 查勘的序号
	                List<PrpLCheckCarVo> prpLCheckCarVos = checkTaskService.findCheckCarVoByRegistNoAndSerialNo(checkTaskInfoSubmitVo.getRegistNo(),"serialNo");
					// 调度序号
	                List<PrpLScheduleDefLossVo> prpLScheduleDefLossVoList = scheduleTaskService.getPrpLScheduleDefLossesVoByRegistNo(checkTaskInfoSubmitVo.getRegistNo());
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
				
				
				
				// 新增的车的序号从移动端传过来
				//checkCarVo.setSerialNo(checkTaskVo.getPrpLCheckCars().size()+1);
				
				//prpLcheckCarVo.setSerialNo(Integer.valueOf(vo.getSerialNo()));
				/*if(StringUtils.isNotBlank(vo.getCarId())){
					prpLcheckCarVo.setSerialNo(Integer.valueOf(vo.getSerialNo()));
				}else{
					prpLcheckCarVo.setSerialNo(++i);
				}*/
				if(StringUtils.isNotBlank(checkTaskInfoSubmitVo.getTaskId())){
					prpLcheckCarVo.setCheckTaskId(Long.valueOf(checkTaskInfoSubmitVo.getTaskId()));
				}
				// 新理赔有/无损失，有为0，无为1，所以做下转换
				if(StringUtils.isNotBlank(check.getIsLoss())){
					if("1".equals(check.getIsLoss())){
						prpLcheckCarVo.setLossFlag("0");
					}else if("0".equals(check.getIsLoss())){
						prpLcheckCarVo.setLossFlag("1");
					}
				}else{
					prpLcheckCarVo.setLossFlag(check.getIsLoss());
				}
				prpLcheckCarVo.setCheckAdress(checkTaskInfoSubmitVo.getCheckSite());
				prpLcheckCarVo.setCheckTime(formatter.parse(checkTaskInfoSubmitVo.getCheckDate()));
				if(vo.getCheck().getEstimatedLoss()!=null){
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
					PrpLCItemKindVo itemKindVo = policyViewService.findItemKindByKindCode(checkTaskInfoSubmitVo.getRegistNo(),vo.getCheck()
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
				prpLcheckCarVo.setCreateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
				prpLcheckCarVo.setCreateTime(new Date());
				prpLcheckCarVo.setUpdateTime(new Date());
				prpLcheckCarVo.setUpdateUser(checkTaskInfoSubmitVo.getNextHandlerCode());
				
				prpLCheckCarInfo.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
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
				prpLCheckCarInfo.setPlatformCarKindCode(vo.getCarKindCode());
				//prpLCheckCarInfo.setInsurecomname("");
				prpLCheckCarInfo.setCarColorCode(vo.getColorCode());
				if(StringUtils.isNotBlank(vo.getRegisteDate())){
					prpLCheckCarInfo.setEnrollDate(formatter.parse(vo.getRegisteDate()));
				}
				//prpLCheckCarInfo.setCarKindCode(vo.getLicenseType());
				if(1 == prpLcheckCarVo.getSerialNo().intValue()){
					// 车型代码只获取标的的三者保存值
	                List<PrpLCMainVo> cMainList = policyViewService.getPolicyAllInfo(checkTaskInfoSubmitVo.getRegistNo());
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
				// 新理赔有/无损失，有为0，无为1，所以做下转换(prpLcheckCarVo上面已经赋值直接用)
				prpLCheckCarInfo.setLossFlag(prpLcheckCarVo.getLossFlag());
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
				
				prpLCheckDriver.setRegistNo(checkTaskInfoSubmitVo.getRegistNo());
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
				checkDutyVo.setIsClaimSelf(checkTaskInfoSubmitVo.getExcessType());
				//checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,checkTaskInfoSubmitVo.getNextHandlerCode());
				// 保存车损
				Long carId = (long)0;
				if(prpLcheckCarVo.getCarid() == null){
					carId = checkHandleService.savePrpLCheckCar(prpLcheckCarVo,checkDutyVo,checkTaskInfoSubmitVo.getNextHandlerCode(),"1");
				}else{
				    carId = checkHandleService.updatePrpLCheckCar(prpLcheckCarVo,checkDutyVo,"1");
				}
				PrpLCheckCarVo checkCarVo = checkHandleService.initPrpLCheckCar(carId);
				
				if("1".equals(checkCarVo.getSerialNo().toString())){
					// 标的
	                CarInfoResVo carInfoVo = new CarInfoResVo();
	                DriverResVo driverResVo = new DriverResVo();
	                CheckResVo checkResVo = new CheckResVo();
					carInfoVo.setIfObject("标的");
	                carInfoVo.setSerialNo("1");
	                carInfoVo.setCarId(String.valueOf(checkCarVo.getCarid()));
	                carInfoVo.setCarSerialNo(vo.getCarSerialNo());
	                driverResVo.setDriverId(String.valueOf(checkCarVo.getPrpLCheckDriver().getDriverId()));
	                //driverResVo.setDriverSerialNo("1");
	                driverResVo.setDriverSerialNo(vo.getDriver().getCarSerialNo());
	                driverResVo.setCarId(String.valueOf(checkCarVo.getCarid()));
	                carInfoVo.setDriver(driverResVo);
	                
	                checkResVo.setLossId(String.valueOf(checkCarVo.getPrpLCheckCarInfo().getCarid()));
	                //checkResVo.setLossSerialNo("1");
	                checkResVo.setLossSerialNo(vo.getCheck().getCarSerialNo());
	                checkResVo.setCarId(String.valueOf(checkCarVo.getCarid()));
	                carInfoVo.setCheck(checkResVo);
	                carInfoResVo.add(carInfoVo);
				}else{
				    CarInfoResVo carInfoThirdVo = new CarInfoResVo();
                    DriverResVo driverResThirdVo = new DriverResVo();
                    CheckResVo checkResThirdVo = new CheckResVo();
					carInfoThirdVo.setIfObject("三者");
                    carInfoThirdVo.setSerialNo(checkCarVo.getSerialNo().toString());
                    carInfoThirdVo.setCarSerialNo(vo.getCarSerialNo());

                    carInfoThirdVo.setCarId(String.valueOf(checkCarVo.getCarid()));
                    
                    driverResThirdVo.setDriverId(String.valueOf(checkCarVo.getPrpLCheckDriver().getDriverId()));
                    driverResThirdVo.setDriverSerialNo(vo.getDriver().getDriverSerialNo());
                    driverResThirdVo.setCarId(String.valueOf(checkCarVo.getCarid()));
                    carInfoThirdVo.setDriver(driverResThirdVo);
                    
                    checkResThirdVo.setLossId(String.valueOf(checkCarVo.getPrpLCheckCarInfo().getCarid()));
                    checkResThirdVo.setLossSerialNo(vo.getCheck().getLossSerialNo());
                    checkResThirdVo.setCarId(String.valueOf(checkCarVo.getCarid()));
                    
                    carInfoThirdVo.setCheck(checkResThirdVo);
                    carInfoResVo.add(carInfoThirdVo);
				}
				prpLCheckCarVoList.add(prpLcheckCarVo);// 车估损金额
			}
			 checkTaskVo.setSumLossCarFee(sumLossCarFee);
		} catch (Exception e) {
			// TODO: handle exception
		    e.printStackTrace();
		    throw new RuntimeException(e);
			
		}
		return prpLCheckCarVoList;
	}

	@Override
	public CheckInfoSubmitResVo service(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
		// 返回的vo
		CheckInfoSubmitResVo resVo = new CheckInfoSubmitResVo();
		List<CarInfoVo> carInfoVoList = new ArrayList<CarInfoVo>();
		List<PropInfoVo> propInfoVoList = new ArrayList<PropInfoVo>();
		List<PersonInfoVo> personInfoVoList = new ArrayList<PersonInfoVo>();
		List<AccountInfoVo> accountInfoVoList = new ArrayList<AccountInfoVo>();
		CheckExtendInfoVo extendInfoVo = new CheckExtendInfoVo();
		MobileCheckHead mobileCheckHead = new MobileCheckHead();
		MobileCheckResponseHead head = new MobileCheckResponseHead();
		CheckInfoSubmitResBodyVo resBody = new CheckInfoSubmitResBodyVo();
		CheckTaskInfoSubmitResVo checkTaskInfo = new CheckTaskInfoSubmitResVo();
		List<CarInfoResVo> carInfoResVo = new ArrayList<CarInfoResVo>(); // 车辆信息
		List<PersonInfoResVo> personInfoResVo = new ArrayList<PersonInfoResVo>(); // 人伤信息
		List<PropInfoResVo> propInfoResVo = new ArrayList<PropInfoResVo>();// 物损信息
		List<PhotoInfo> photoInfoList = new ArrayList<PhotoInfo>();// 照片信息
		// List<AccountInfoResVo> accountInfo = new ArrayList<AccountInfoResVo>(); //收款人信息
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");// 去掉 class属性
		String registNo ="";
		CheckTaskInfoSubmitVo checkTaskInfoSubmitVo = new CheckTaskInfoSubmitVo();
		String lockFlags = "1";
		String resultMsg = null;
		try{
			
			stream.processAnnotations(CheckInfoSubmitReqVo.class);
			CheckInfoSubmitReqVo checkInfoSubmitReqVo = (CheckInfoSubmitReqVo) arg1;
			stream.processAnnotations(FlowInfoInitReqVo.class);
			String xml = stream.toXML(checkInfoSubmitReqVo);
			logger.info("移动端查勘提交/同步接口接收报文: \n"+xml);
			//CheckInfoSubmitReqVo checkInfoSubmitReqVo = (CheckInfoSubmitReqVo)stream.fromXML(xml);
			CheckInfoSubmitReqBodyVo checkInfoSubmitReqBodyVo = checkInfoSubmitReqVo.getBody();
			checkTaskInfoSubmitVo = checkInfoSubmitReqBodyVo.getCheckTaskInfo();
			// 加锁开始
			registNo = checkTaskInfoSubmitVo.getRegistNo();
			Long lockYwMainId = Long.valueOf(checkTaskInfoSubmitVo.getCheckId());
			lockService.savePrplLockList(lockYwMainId, FlowNode.Check.name());
			lockFlags = "0";// 加锁
			carInfoVoList = checkInfoSubmitReqBodyVo.getCarInfo();
			propInfoVoList = checkInfoSubmitReqBodyVo.getPropInfo();
			personInfoVoList = checkInfoSubmitReqBodyVo.getPersonInfo();
			accountInfoVoList = checkInfoSubmitReqBodyVo.getAccountInfo();
			extendInfoVo = checkInfoSubmitReqBodyVo.getExtendInfoVo();
			photoInfoList = checkInfoSubmitReqBodyVo.getPhotoInfoList();

			mobileCheckHead = checkInfoSubmitReqVo.getHead();
			if (!"008".equals(mobileCheckHead.getRequestType())|| !"claim_user".equals(mobileCheckHead.getUser())|| !"claim_psd".equals(mobileCheckHead.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			if(StringUtils.isBlank(xml)){
				throw new IllegalArgumentException("报文为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("请求类型不能为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("用户名不能为空");
			}
			if(!StringUtils.isNotBlank(mobileCheckHead.getRequestType())){
				throw new IllegalArgumentException("密码不能为空");
			}
			if(!StringUtils.isNotBlank(checkTaskInfoSubmitVo.getNodeType())){
				throw new IllegalArgumentException("调度节点不能为空");
			}
			if(!StringUtils.isNotBlank(checkTaskInfoSubmitVo.getRegistNo())){
				throw new IllegalArgumentException("报案号不能为空");
			}
			// 查勘提交需要校验，同步不需要校验
			if("3".equals(checkTaskInfoSubmitVo.getOptionType())){
				checkRequest(checkInfoSubmitReqBodyVo);
			}
			
			PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
			if("3".equals(checkTaskInfoSubmitVo.getOptionType())){
				if("on".equals(checkVo.getOperateStatus())){
					throw new IllegalArgumentException("当前操作人员有查勘任务正在提交，请等待或刷新后再试");
				}
				checkVo.setOperateStatus("on");
	            checkHandleService.updateCheckMain(checkVo);
			}
			// 管控提交后不能再提交start
			List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(checkTaskInfoSubmitVo.getRegistNo(), FlowNode.Chk.toString());
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
					throw new IllegalArgumentException("任务已提交或者注销！提交人："+vo.getHandlerUser());
                }
            }
			// 管控提交后不能再提交end
            
			String url = SpringProperties.getProperty("MClaimPlatform_URL_IN");
			logger.info("移动查勘地址================="+url);
			Long checkId = setCheckInfo(carInfoResVo,checkTaskInfoSubmitVo,carInfoVoList,propInfoVoList,personInfoVoList,accountInfoVoList,url,
					extendInfoVo,photoInfoList,resultMsg);
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
			head.setResponseType(mobileCheckHead.getRequestType());
			head.setResponseCode("YES");
			head.setResponseMessage("Success");
			CheckActionVo checkActionVo = checkHandleService.initCheckByCheck(checkId);
			PrpLCheckTaskVo prpLCheckTaskVo = checkActionVo.getPrpLcheckTaskVo();
			List<PrpLCheckPersonVo> checkPersonList = checkActionVo.getCheckPersonList();
			List<PrpLCheckPropVo> checkPropList = checkActionVo.getCheckPropList();
			PrpLCheckVo prplCheckVo = checkActionVo.getPrpLcheckVo();
			checkTaskInfo.setRegistNo(prpLCheckTaskVo.getRegistNo());
			checkTaskInfo.setCheckId(String.valueOf(checkId));
			checkTaskInfo.setOptionType(checkTaskInfoSubmitVo.getOptionType());
			checkTaskInfo.setTaskId(String.valueOf(checkId));
			resBody.setCheckTaskInfo(checkTaskInfo);
			
            resBody.setCarInfo(carInfoResVo);
            
			for(int i = 0; i < checkPersonList.size() ; i++){
			    PersonInfoResVo personInfoVo = new PersonInfoResVo();
			    personInfoVo.setPersonId(checkPersonList.get(i).getId().toString());
			    personInfoVo.setPersonSerialNo(checkPersonList.get(i).getPersonSerialNo());
			    personInfoResVo.add(personInfoVo);
			}
			
			for(int i = 0; i < checkPropList.size() ; i++){
			    PropInfoResVo propInfoVo = new PropInfoResVo();
			    propInfoVo.setPropId(checkPropList.get(i).getId().toString());
			    propInfoVo.setPropSerialNo(checkPropList.get(i).getPropSerialNo());
			    propInfoResVo.add(propInfoVo);
            }
			
			resBody.setPersonInfo(personInfoResVo);
			resBody.setPropInfo(propInfoResVo);
			// 公估费生成
			SysUserVo userVo = new SysUserVo();
	        userVo.setComCode(checkTaskInfoSubmitVo.getScheduleObjectId());
	        userVo.setUserCode(checkTaskInfoSubmitVo.getNextHandlerCode());
	        userVo.setUserName(checkTaskInfoSubmitVo.getNextHandlerName());
	        userVo.setComName(checkTaskInfoSubmitVo.getScheduleObjectName());
			checkHandleService.saveAssessor(registNo,userVo);
			// 保存查勘费信息
			checkHandleService.saveCheckFee(registNo, userVo);
			if(StringUtils.isNotBlank(resultMsg)){
				head.setResponseMessage(resultMsg);
			}
			resVo.setBody(resBody);
			resVo.setHead(head);
			prplCheckVo.setOperateStatus("off");
			checkHandleService.updateCheckMain(prplCheckVo);
		}
		catch(Exception e){
			if(e.getMessage().contains("ORA-00001: unique constraint")){
        		head.setResponseType(mobileCheckHead.getRequestType());
    			head.setResponseCode("NO");
				head.setResponseMessage("移动端查勘提交/同步接口数据不能重复提交"+e.getMessage());
    			resVo.setHead(head);
    			PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
    			checkVo.setOperateStatus("off");
    			checkHandleService.updateCheckMain(checkVo);
				logger.info("移动端查勘提交/同步接口Insert或Update数据时违反了完整性：\n",e);
        	}else{
        		head.setResponseType(mobileCheckHead.getRequestType());
    			head.setResponseCode("NO");
    			head.setResponseMessage(e.getMessage());
    			resVo.setHead(head);
				logger.info("移动端查勘提交/同步接口异常信息：\n",e);
    			PrpLCheckVo checkVo = checkHandleService.queryPrpLCheckVo(registNo);
    			checkVo.setOperateStatus("off");
    			checkHandleService.updateCheckMain(checkVo);
        	}
		}finally{
			// 删除加锁
			if("0".equals(lockFlags)){
				lockService.deletePrplLockListById(Long.valueOf(checkTaskInfoSubmitVo.getCheckId()), FlowNode.Check.name());
			}
		}
		stream.processAnnotations(CheckInfoSubmitResVo.class);
		logger.info("移动端查勘提交/同步接口返回报文=========：\n"+stream.toXML(resVo));
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
		if(mobileCheckService==null){
			mobileCheckService = (MobileCheckService)Springs.getBean(MobileCheckService.class);
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
		if(lockService==null){
			lockService = (LockService)Springs.getBean(LockService.class);
        }
		if(codeTranService == null) {
		    codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(managerService == null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
	    }
	}
	
	public void checkRequest(CheckInfoSubmitReqBodyVo body){
		
		CheckTaskInfoSubmitVo checkTaskInfo = body.getCheckTaskInfo();
		List<CarInfoVo> carList = body.getCarInfo();
		List<PersonInfoVo> personList = body.getPersonInfo();
		List<PropInfoVo> propList = body.getPropInfo();
		CheckExtendInfoVo extendInfoVo = body.getExtendInfoVo();
		List<PhotoInfo> photoInfoList = body.getPhotoInfoList();
		
		if(extendInfoVo == null){
			throw new IllegalArgumentException("查勘扩展信息为空");
		}
		if(carList == null || carList.size() == 0){
			throw new IllegalArgumentException("车辆信息为空");
		}
		notNull(extendInfoVo.getLicenseEffective()," 保险车辆行驶证是否有效空  ");
		
		// 查勘记录
		notNull(checkTaskInfo.getDamageCode()," 出险原因为空  ");
		notNull(checkTaskInfo.getCheckSite()," 查勘地点为空  ");
		notNull(checkTaskInfo.getCheckType()," 查勘类型为空  ");
		notNull(checkTaskInfo.getCheckIdentifyNumber()," 查勘人身份证号为空  ");
		notExistLowercase(checkTaskInfo.getCheckIdentifyNumber()," 查勘人身份证号不能包含小写字母  ");
		notNull(checkTaskInfo.getCheckPhoneNumber()," 查勘人联系电话为空  ");
		notNull(checkTaskInfo.getFirstSiteFlag()," 是否第一现场查勘为空  ");
		notNull(checkTaskInfo.getExcessType()," 是否互碰自赔为空  ");
		notNull(checkTaskInfo.getIsIncludeProp()," 是否包含财损为空  ");
		notNull(checkTaskInfo.getIsIncludePerson()," 是否包含人伤为空  ");
		notNull(checkTaskInfo.getIndemnityDuty()," 事故责任为空  ");
		notNull(checkTaskInfo.getIndemnityDutyRate()," 事故责任比例为空  ");
		notNull(checkTaskInfo.getIsBigCase()," 是否重大赔案上报为空  ");
		notNull(checkTaskInfo.getCheckFee()," 查勘费用为空  ");
		notNull(checkTaskInfo.getSubrogateType()," 是否代位求偿为空  ");
		notNull(checkTaskInfo.getTextType()," 本次查勘报告为空  ");
		notNull(checkTaskInfo.getNoDutyFlag()," 是否免责情形为空  ");
		notNull(checkTaskInfo.getIsunilAccident(),"是否单车事故为空");

		//校验查勘人身份证是否正常
		IDCardUtil idUtils = new IDCardUtil();
		if(checkTaskInfo != null){
			String identifyNumber = checkTaskInfo.getCheckIdentifyNumber();
			if(StringUtils.isNotBlank(identifyNumber)){
				IDInfo info = idUtils.IDCardValidate(identifyNumber);
				if(StringUtils.isNotBlank(info.getErrMsg())){
					throw new IllegalArgumentException(info.getErrMsg());	
				}
			}
		}
		
		if(CodeConstants.RadioValue.RADIO_YES.equals(checkTaskInfo.getNoDutyFlag())){
			notNull(checkTaskInfo.getNoDutyReason()," 免责原因为空  ");
		}
		
		// 单车事故不允许传三者车，多车事故必须传三者车
		if(CodeConstants.IsSingleAccident.YES.equals(checkTaskInfo.getIsunilAccident()) && carList.size()>1){
			throw new IllegalArgumentException("单车事故不允许上传三者车信息");
		}else if(CodeConstants.IsSingleAccident.NOT.equals(checkTaskInfo.getIsunilAccident()) && carList.size()==1){
			throw new IllegalArgumentException("多车事故必须上传三者车信息");
		}
		
		if("1".equals(checkTaskInfo.getSubrogateType())){
			throw new IllegalArgumentException("代位求偿案件必须同步到电脑端处理，移动终端不处理此类型案件");
		}
		
		// 是否互碰自赔只能传0或1
		if(!CodeConstants.caseFlagType.NORMAL_CASE.equals(checkTaskInfo.getExcessType()) 
				&& !CodeConstants.caseFlagType.SELF_CASE.equals(checkTaskInfo.getExcessType())){
			throw new IllegalArgumentException("是否互碰自赔数据格式错误！");
		}
		
		// 车辆信息
		if(carList!=null && carList.size()>0){
			for(CarInfoVo carInfo:carList){
				notNull(carInfo.getIfObject()," 类别为空  ");
				notNull(carInfo.getLicenseNo()," 车牌号为空  ");
				//校验车牌长度
				if(carInfo.getLicenseNo().length() < 5 || carInfo.getLicenseNo().length() > 10){
					throw new IllegalArgumentException("车牌号过长或过短，请输入正确的车牌号！");
				}
				
				notNull(carInfo.getLicenseType()," 车牌种类为空  ");
				notNull(carInfo.getEngineNo()," 发动机号为空  ");
				notNull(carInfo.getFrameNo()," 车架号为空  ");
				notNull(carInfo.getVin()," VIN码为空  ");
				notNull(carInfo.getOwern()," 车主为空  ");
				notNull(carInfo.getCarKindCode(),"车辆种类为空  ");
				if(!codeTranService.validEffictiveValue(carInfo.getCarKindCode(), "VehicleType")){
					throw new IllegalArgumentException("车辆种类值不在有效范围内");
				}
				// 驾驶人信息
				DriverVo driver = carInfo.getDriver();
				if(StringUtils.isNotBlank(carInfo.getSerialNo()) && "1".equals(carInfo.getSerialNo())) {
					notNull(driver.getDriverName()," 驾驶人姓名为空  ");
				}
				notNull(driver.getDrivingCarType()," 准驾车型为空  ");
				notNull(driver.getCertiType()," 证件类型为空  ");
//				notNull(driver.getIdentifyNumber(), " 证件号码为空  ");
				notExistLowercase(driver.getIdentifyNumber()," 证件号码不能包含小写字母  ");
				if(StringUtils.isNotBlank(carInfo.getSerialNo()) && "1".equals(carInfo.getSerialNo())) {
					notNull(driver.getDrivingLicenseNo(), " 驾驶证号码为空  ");
				}
				notExistLowercase(driver.getDrivingLicenseNo()," 驾驶证号码不能包含小写字母  ");
				notNull(driver.getPhoneNumber()," 电话为空  ");
				//如果驾驶人证件号码为身份证类型，判断是否与查勘人员身份证号码是否存在相同
                if(checkTaskInfo.getCheckIdentifyNumber().equals(driver.getIdentifyNumber())){
                    throw new IllegalArgumentException("查勘人员身份证号不能和驾驶员身份证号一致！请修改！");
                }
				// 车辆查勘信息
				CheckVo check = carInfo.getCheck();
				notNull(check.getEstimatedLoss()," 估损金额为空  ");
				notNull(check.getIndemnityDuty()," 事故责任为空  ");
				notNull(check.getIndemnityDutyRate()," 责任比例为空  ");
				notNull(check.getLossPart()," 损失部位为空  ");
			}
			String message = checkHandleService.saveValidCars(carList);
			if(StringUtils.isNotBlank(message)){
				throw new IllegalArgumentException(message);
			}
		}
		
		// 人伤信息
		if(personList!=null && personList.size()>0){
			for(PersonInfoVo personInfo:personList){
				notNull(personInfo.getIsAdjust()," 是否现场调解为空  ");
				notNull(personInfo.getName()," 姓名为空  ");
				notNull(personInfo.getSex()," 性别为空  ");
				notNull(personInfo.getCertiType()," 证件类型为空  ");
				// notNull(personInfo.getIdentifyNumber(), " 证件号码为空  ");
				notExistLowercase(personInfo.getIdentifyNumber(),"伤员证件号码不能包含小写字母");
				//校验身份证是否正常
				if(StringUtils.isNotBlank(personInfo.getIdentifyNumber()) && "1".equals(personInfo.getCertiType())){
					IDInfo info = idUtils.IDCardValidate(personInfo.getIdentifyNumber());
					if(StringUtils.isNotBlank(info.getErrMsg())){
						throw new IllegalArgumentException(info.getErrMsg());
					}
				}
				//判断是否与查勘人员身份证号码是否存在相同
				if(checkTaskInfo.getCheckIdentifyNumber().equals(personInfo.getIdentifyNumber())){
					throw new IllegalArgumentException("伤员证件号码与查勘人员身份证号一致！请修改！");
				}
				notNull(personInfo.getDegree()," 伤情类别为空  ");
				notNull(personInfo.getSumClaim()," 估损金额为空  ");
				notNull(personInfo.getPersHandleType()," 案件处理类型为空  ");
			}
		}
		
		// 物损信息
		if(propList != null && propList.size()>0){
			for(PropInfoVo propInfo:propList){
				notNull(propInfo.getLossName()," 损失名称为空  ");
				notNull(propInfo.getLossNum()," 损失数量为空  ");
				notNull(propInfo.getPayAmount()," 赔偿金额为空  ");
			}
		}
		// 照片信息
		if(photoInfoList!=null && photoInfoList.size()>0){
			for(PhotoInfo photoInfo:photoInfoList){
				notNull(photoInfo.getPhotoType(),"单证类型为空");
				notNull(photoInfo.getPhotoNum(),"单证数量为空");
			}
		}
	}
	
	/**
	 * <pre>
	 * 设置收款人信息
	 * </pre>
	 * @param prpLPayCustomVo
	 * @param account
	 * @return
	 * @throws Exception
	 * @modified: ☆zhujunde(2017年6月14日 下午4:40:07): <br>
	 */
	/*	public Long setPrpLPayCustomVo(List<AccountInfoResVo> accountInfo,AccountInfoVo account,SysUserVo userVo) throws Exception{

	        PrpLPayCustomVo prpLPayCustomVo = new PrpLPayCustomVo();
	        AccountInfoResVo accountResVo = new AccountInfoResVo();
	        if(StringUtils.isNotBlank(account.getAccountId())){
	            prpLPayCustomVo.setId(Long.valueOf(account.getAccountId())); 
	        }
	        prpLPayCustomVo.setPayObjectType(account.getPayeeNature());
	        prpLPayCustomVo.setCertifyType(account.getIdentifyType());
	        prpLPayCustomVo.setPayObjectKind(account.getPayeeType());
	        prpLPayCustomVo.setPayeeName(account.getName());
	        prpLPayCustomVo.setPublicAndPrivate(account.getPubandPrilogo());
	        prpLPayCustomVo.setCertifyNo(account.getIdentifyNumber());
	        prpLPayCustomVo.setAccountNo(account.getAccountNo());
	        prpLPayCustomVo.setProvinceCode(Long.valueOf(account.getProvinceCode()));
	        prpLPayCustomVo.setCityCode(Long.valueOf(account.getCityCode()));
	        prpLPayCustomVo.setBankName(account.getBankName());
	        prpLPayCustomVo.setBankOutlets(account.getBranchName());
	        prpLPayCustomVo.setBankNo(account.getBankCode());
	        prpLPayCustomVo.setPriorityFlag(account.getTransferMode());
	        prpLPayCustomVo.setPayeeMobile(account.getPhone());
	        //去除空格
	        if(StringUtils.isNotBlank(account.getDigest())){
	            prpLPayCustomVo.setSummary(account.getDigest().replaceAll("\\s*", ""));
	        }
	        prpLPayCustomVo.setAccountNo(account.getAccountNo().replaceAll("\\s*", ""));
	        prpLPayCustomVo.setCertifyNo(account.getIdentifyNumber().replaceAll("\\s*", ""));
	        prpLPayCustomVo.setPayeeName(account.getName().replaceAll("\\s*", ""));
	        Long id = payCustomService.saveOrUpdatePayCustom(prpLPayCustomVo,userVo);
	        //设置返回的收款人
	        accountResVo.setAccountId(String.valueOf(id));
	        accountResVo.setAccountSerialNo(account.getAccountSerialNo());
	        accountInfo.add(accountResVo);
	        return id;
		    
		}*/

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
