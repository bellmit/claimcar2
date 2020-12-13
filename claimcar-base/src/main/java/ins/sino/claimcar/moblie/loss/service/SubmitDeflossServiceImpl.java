package ins.sino.claimcar.moblie.loss.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.Assert;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.api.service.ServiceInterface;

import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.RepairFlag;
import ins.sino.claimcar.certify.service.CertifyIlogService;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.common.claimtext.vo.PrpLClaimTextVo;
import ins.sino.claimcar.common.losscharge.vo.PrpLDlossChargeVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.IlogRuleService;
import ins.sino.claimcar.ilogFinalpowerInfo.vo.IlogFinalPowerInfoVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMaterialVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.losscar.vo.SubmitNextVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckHead;
import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;
import ins.sino.claimcar.moblie.loss.vo.CertainsTaskRequestVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossAssistInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossCollisionParts;
import ins.sino.claimcar.moblie.loss.vo.DefLossFeeInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossManhourInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossOutRepairInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DefLossPartInfoVo;
import ins.sino.claimcar.moblie.loss.vo.DeflossRepairSumVo;
import ins.sino.claimcar.moblie.loss.vo.SubmitDeflossRequest;
import ins.sino.claimcar.moblie.loss.vo.SubmitDeflossRespond;
import ins.sino.claimcar.platform.service.CertifyToPaltformService;
import ins.sino.claimcar.platform.service.LossToPlatformService;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

public class SubmitDeflossServiceImpl implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(SubmitDeflossServiceImpl.class);
	private HashMap<String,PrpLCItemKindVo> itemKindMap = new HashMap<String,PrpLCItemKindVo>();
	@Autowired
	DeflossHandleService deflossHandleService;
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	@Autowired
	LossCarService lossCarService;
	@Autowired
	LossToPlatformService lossToPlatformService;
	@Autowired
	ClaimTaskService claimTaskService;
	@Autowired
	InterfaceAsyncService interfaceAsyncService;
	@Autowired
    CertifyIlogService certifyIlogService;
	@Autowired
	CertifyToPaltformService certifyToPaltformService;
	@Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    VerifyClaimService verifyClaimService;
    @Autowired
	SubrogationService subrogationService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    CertifyService certifyService;
    @Autowired
    CodeTranService codeTranService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    private IlogRuleService ilogRuleService;
    
    @Autowired
	private PropTaskService propTaskService;
    
    @Autowired
    private ManagerService managerService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		SubmitDeflossRespond resPacket = new SubmitDeflossRespond();
		MobileCheckResponseHead resHead = new MobileCheckResponseHead();
		try{
			String reqXml = ClaimBaseCoder.objToXmlUtf(arg1);
			logger.info("=================移动端定损提交接口请求报文："+reqXml);
			SubmitDeflossRequest reqPacket = (SubmitDeflossRequest)arg1;
			Assert.notNull(reqPacket," 请求信息为空  ");
			MobileCheckHead head = reqPacket.getHead();
			if (!"010".equals(head.getRequestType())|| !"claim_user".equals(head.getUser())|| !"claim_psd".equals(head.getPassWord())) {
				throw new IllegalArgumentException(" 请求头参数错误  ");
			}
			CertainsTaskRequestVo certainsTaskInfoVo = reqPacket.getBody().getCertainsTaskRequestVo();
			if(certainsTaskInfoVo!=null){
				// 如果标的承保了可以赔付的险别，则LossFeeType不能为空
				List<String> kindCodeList=lossCarService.kindCodes(certainsTaskInfoVo.getRegistNo());
				if(kindCodeList!=null&&kindCodeList.size()>0&&CodeConstants.LossParty.TARGET.equals(certainsTaskInfoVo.getIsObject())&&StringUtils
						.isBlank(certainsTaskInfoVo.getLossFeeType())){
					for(String kindCode:kindCodeList){
						if(CodeConstants.KindForSelfCar_List.contains(kindCode)){
							throw new IllegalArgumentException(" 标的车损失类型(LossFeeType)不能为空！");
						}
					}
				}
				// 表的车的LossFeeType不能选交强险
				if(CodeConstants.LossParty.TARGET.equals(certainsTaskInfoVo.getIsObject())&&CodeConstants.KINDCODE.KINDCODE_BZ
						.equals(certainsTaskInfoVo.getLossFeeType())){
					throw new IllegalArgumentException(" 标的车损失类型(LossFeeType)不能选择交强险！");
				}
			}
			
			resHead.setResponseType("010");
			resHead.setResponseCode("YES");
			resHead.setResponseMessage("Success");
			PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo();
			PrpLClaimTextVo claimTextVo = new PrpLClaimTextVo();
			List<PrpLDlossChargeVo> lossChargeVos = new ArrayList<PrpLDlossChargeVo>();
			PrpLDlossCarInfoVo lossCarInfoVo = new PrpLDlossCarInfoVo();
			PrpLWfTaskVo taskVo = wfTaskHandleService.queryTask(Double.valueOf(certainsTaskInfoVo.getTaskId()));
			PrpLSubrogationMainVo subrogationMainVo = subrogationService.find(taskVo.getRegistNo());
			SysUserVo userVo = new SysUserVo();
			
			organizeDefLossData(lossCarMainVo,claimTextVo,lossChargeVos,lossCarInfoVo,userVo,certainsTaskInfoVo);
			
			// 暂存
			deflossHandleService.save(lossCarMainVo, lossChargeVos, claimTextVo, subrogationMainVo, taskVo,userVo);
			// 定损提交
			if("3".equals(certainsTaskInfoVo.getOptionType())){
				Map<String,Object> params = new HashMap<String,Object>();
          		params.put("registNo",lossCarMainVo.getRegistNo());
          		if(certainsTaskInfoVo.getTaskId() == null){
          			params.put("taskId",BigDecimal.ZERO.doubleValue());	
          		} else{
              		params.put("taskId",Double.valueOf(certainsTaskInfoVo.getTaskId()));	
          		}
          		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
				// 定损提交需要校验字段，暂存不需校验
				checkRequest(certainsTaskInfoVo);
				
                SubmitNextVo nextVo = deflossHandleService.organizeNextVo(Long.valueOf(certainsTaskInfoVo.getCertainsId()),certainsTaskInfoVo.getTaskId(),null,"Y",userVo,isSubmitHeadOffice);
				
								nextVo.setNextNode(nextVo.getNodeLevel());
				if(nextVo.getNodeMap() != null){
					nextVo.setNextName(nextVo.getNodeMap().get(nextVo.getNodeLevel()));
				}
				nextVo.setFinalNextNode(nextVo.getNextNode());
				nextVo.setUserVo(userVo);
				lossCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(certainsTaskInfoVo.getCertainsId()));
				lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.SUBMITLOSS);
				nextVo.setAuditStatus(lossCarMainVo.getAuditStatus());
				String jyUrl = SpringProperties.getProperty("JY_URL");
				nextVo.setJyUrl(jyUrl);
				nextVo.setTaskInUser(userVo.getUserCode());
				lossCarMainVo.setIsMobileCase("1");
				
				deflossHandleService.submitNextNode(lossCarMainVo,nextVo);
				try{
					// 保存移动端案件显示标志，有待优化
					PrpLWfTaskVo wfTaskOutVo=wfTaskHandleService.queryTask(Double.valueOf(certainsTaskInfoVo.getTaskId()));
					String bussTag = wfTaskOutVo.getBussTag();
					Map<String, Object> map = JSONObject.parseObject(bussTag);
					map.put("isMobileCase", "1");
					wfTaskOutVo.setBussTag(JSONObject.toJSONString(map));
					wfTaskHandleService.updateTaskOut(wfTaskOutVo);
					// 更新单证的isMobileCase标识
					List<PrpLWfTaskVo> certiTaskVoList = wfTaskHandleService.findInTaskByOther(nextVo.getRegistNo(),null,FlowNode.Certi.name());
					if(certiTaskVoList!=null && certiTaskVoList.size()>0){
						for(PrpLWfTaskVo certiTaskVo:certiTaskVoList){
							String certiBussTag = certiTaskVo.getBussTag();
							Map<String, Object> certiMap = JSONObject.parseObject(certiBussTag);
							certiMap.put("isMobileCase", "1");
							certiTaskVo.setBussTag(JSONObject.toJSONString(certiMap));
							wfTaskHandleService.updateTaskOut(certiTaskVo);
						}
					}
				}catch(Exception e){
					logger.error("=================移动端定损提交接回写标识出错：",e);
				}
			}
			// 如果自动核损通过要送平台和刷立案
			lossCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(certainsTaskInfoVo.getCertainsId()));
			if("1".equals(lossCarMainVo.getUnderWriteFlag())){
				lossToPlatformService.sendLossToPlatform(lossCarMainVo.getRegistNo(),null);
				// 刷新立案
				claimTaskService.updateClaimFee(lossCarMainVo.getRegistNo(),certainsTaskInfoVo.getNextHandlerCode(),FlowNode.VLCar);
				try{
					//调用影像系统“影像资料统计接口”，查询该工号在该任务中上传的影像数量并保存（异步执行）
					String imageUrl = SpringProperties.getProperty("YX_QUrl")+"?";
					SysUserVo sysUserVo = new SysUserVo();
					sysUserVo.setUserCode(lossCarMainVo.getHandlerCode());
				    String userName = CodeTranUtil.transCode("UserCode", lossCarMainVo.getHandlerCode());
				    if(userName != null && userName.equals(lossCarMainVo.getHandlerCode())){
				    	userName = scheduleTaskService.findPrpduserByUserCode(lossCarMainVo.getHandlerCode(),"").getUserName(); 
				    }
				    sysUserVo.setUserName(userName);
				    sysUserVo.setComCode(userVo.getComCode());
					interfaceAsyncService.getReqImageNum(sysUserVo, CodeConstants.APPROLE, lossCarMainVo.getRegistNo(), "", imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
					interfaceAsyncService.getReqCheckUserImageNum(sysUserVo, CodeConstants.APPROLE, lossCarMainVo.getRegistNo(),imageUrl,CodeConstants.APPNAMECLAIM,CodeConstants.APPCODECLAIM);
				}catch(Exception e){
					logger.info("车辆核损调用影像系统影像资料统计接口报错=============", e);
				}
			}
			
			// 判断是否为最后一个核损，请求Ilog
			PrpLConfigValueVo configValueIlogVo = ConfigUtil.findConfigByCode(CodeConstants.ILOGVALIDFLAG,userVo.getComCode());
			PrpLConfigValueVo configValueIRuleVo = ConfigUtil.findConfigByCode(CodeConstants.RULEVALIDFLAG,userVo.getComCode());
			if("1".equals(configValueIlogVo.getConfigValue()) && "0".equals(configValueIRuleVo.getConfigValue())){
				Boolean flag=certifyIlogService.validAllVLossPass(lossCarMainVo.getRegistNo());
				if(flag){
					PrpLWfTaskVo taskVo2=wfTaskHandleService.findLastVlossTask(lossCarMainVo.getRegistNo());
				if(taskVo2!=null){
					LIlogRuleResVo resVo=certifyIlogService.sendAutoCertifyRule(lossCarMainVo.getRegistNo(),userVo,new BigDecimal(taskVo2.getTaskId().toString()),taskVo2.getNodeCode());
					if(resVo!=null && "1".equals(resVo.getUnderwriterflag()) && certifyService.isPassPlatform(lossCarMainVo.getRegistNo())){
							WfTaskSubmitVo submitVo = certifyIlogService.autoCertify(lossCarMainVo.getRegistNo(),userVo);// 自动单证
							// 单证送平台
						 try{
							 certifyToPaltformService.certifyToPaltform(lossCarMainVo.getRegistNo(),null);
						 }catch(Exception e){
								logger.info("自动单证送平台异常信息-------------->"+e.getMessage());
						 }
							// 调用ilog查询是否可自动理算
						boolean NotExistObj = compensateTaskService.adjustNotExistObj(lossCarMainVo.getRegistNo());
						if("1".equals(configValueIlogVo.getConfigValue()) && !StringUtils.isBlank(submitVo.getFlowId())&&!NotExistObj){
								// ==============事务问题开始
                            String registNo = lossCarMainVo.getRegistNo();
                            LIlogRuleResVo ruleResVo = certifyIlogService.sendAutoCertifyRule(registNo,userVo,submitVo.getFlowTaskId(),submitVo.getCurrentNode().toString());
                            /** 兜底人员权限判断 start **/
                            String finalPowerFlag =  SpringProperties.getProperty("FINALPOWERFLAG");
            	        	boolean finalAutoPass = true;
            	        	if ("1".equals(finalPowerFlag)) {
            	        		IlogFinalPowerInfoVo powerInfoVo = ilogRuleService.findByUserCode(userVo.getUserCode());
            	        		if (powerInfoVo != null) {
            	        			BigDecimal gradePower = powerInfoVo.getGradeAmount();
            	        			if (gradePower != null) {
            	        				// 总定损金额
            	        				BigDecimal sumAmount = BigDecimal.ZERO;
            	        				// 定损车辆信息
            	        				List<PrpLDlossCarMainVo> losscarMainList=deflossHandleService.findLossCarMainByRegistNo(registNo);
            	        				// 人伤定损信息
            	        				List<PrpLDlossPersTraceMainVo> losspersTraceList=deflossHandleService.findlossPersTraceMainByRegistNo(registNo);
            	        				// 财产定损信息
            	        				List<PrpLdlossPropMainVo> propmianList=propTaskService.findPropMainListByRegistNo(registNo);
            	        				if (losscarMainList != null && losscarMainList.size() > 0) {
            	        					for (PrpLDlossCarMainVo vo : losscarMainList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLossFee() == null ? "0" : vo.getSumVeriLossFee().toString()));
            	        					}
            	        				}
            	        				
            	        				if (losspersTraceList != null && losspersTraceList.size() > 0) {
            	        					for (PrpLDlossPersTraceMainVo vo : losspersTraceList) {
            	        						if (vo.getPrpLDlossPersTraces() != null && vo.getPrpLDlossPersTraces().size() > 0) {
            	        							for (PrpLDlossPersTraceVo traceVo : vo.getPrpLDlossPersTraces()) {
            	        								sumAmount = sumAmount.add(DataUtils.NullToZero(traceVo.getSumVeriDefloss()));
            	        							}
            	        						}
            	        					}
            	        				}
            	        				
            	        				if (propmianList != null && propmianList.size() > 0) {
            	        					for (PrpLdlossPropMainVo vo : propmianList) {
            	        						sumAmount = sumAmount.add(new BigDecimal(vo.getSumVeriLoss() == null ? "0" : vo.getSumVeriLoss().toString()));
            	        					}
            	        				}
            	        				
            	        				if (sumAmount.compareTo(gradePower) == 1) {
            	        					finalAutoPass = false;
            	        				}
            	        			}
            	        		} else {
            	        			finalAutoPass = false;
            	        		}
            	        	}
							/** 兜底人员权限判断  end  **/	
            	        	if("1".equals(ruleResVo.getUnderwriterflag()) && finalAutoPass){// 自动理算通过 
            	        	List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(registNo,null,FlowNode.Compe.toString());
                                if(prpLWfTaskVoList!=null&&prpLWfTaskVoList.size()>0){
                                    for(PrpLWfTaskVo taskVo1:prpLWfTaskVoList){
                                        PrpLCompensateVo compVo = compensateTaskService.autoCompTask(taskVo1,userVo);
                                        Boolean autoVerifyFlag = false;
                                        
                                        Map<String,Object> params = new HashMap<String,Object>();
                                		params.put("registNo",registNo);
                                		if(certainsTaskInfoVo.getTaskId() != null){
                                    		params.put("taskId",Double.valueOf(certainsTaskInfoVo.getTaskId()));
                                		} else{
                                			params.put("taskId",BigDecimal.ZERO.doubleValue());
                                		}
                                		String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
                                        WfTaskSubmitVo nextVo = compensateTaskService.getCompensateSubmitNextVo(compVo.getCompensateNo(),taskVo1.getTaskId().doubleValue(),taskVo1,userVo,autoVerifyFlag,isSubmitHeadOffice);
                                        if(nextVo.getSubmitLevel()==0){
                                            autoVerifyFlag = true;
                                        }
                                        if(autoVerifyFlag){
												// 自动核赔标识为true，理算提交后执行自动核赔
                                            Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo,compVo);
												// 核赔提交结案
                                            verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, compVo,uwNotionMainId);
												// 核赔通过送收付、再保
                                            try{
                                                verifyClaimService.sendCompensateToPayment(uwNotionMainId);
                                            }catch(Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        
                                    }
                                }
                            }
								// ==============事务问题结束
						}
					}
				}
			}
		}
		}catch (Exception e){
			resHead.setResponseType("010");
			resHead.setResponseCode("NO");
			resHead.setResponseMessage(e.getMessage());
			logger.info("定损提交接口报错信息： "+e.getMessage());
			e.printStackTrace();
		}
		
		resPacket.setHead(resHead);
		String resXml = ClaimBaseCoder.objToXmlUtf(resPacket);
		logger.info("=================移动端定损提交接口返回报文："+resXml);
		return resPacket;
	}
	
	public void organizeDefLossData(PrpLDlossCarMainVo lossCarMainVo,PrpLClaimTextVo claimTextVo,
			List<PrpLDlossChargeVo> lossChargeVos,PrpLDlossCarInfoVo lossCarInfoVo,
			SysUserVo userVo,CertainsTaskRequestVo certainsTaskInfoVo) throws Exception{
		
		PrpLDlossCarMainVo oldCarMainVo = lossCarService.findLossCarMainById(Long.valueOf(certainsTaskInfoVo.getCertainsId()));
		Beans.copy().from(oldCarMainVo).to(lossCarMainVo);
		
		userVo.setUserCode(certainsTaskInfoVo.getNextHandlerCode());
		userVo.setUserName(certainsTaskInfoVo.getNextHandlerName());
		userVo.setComCode(certainsTaskInfoVo.getScheduleObjectId());
		userVo.setComName(certainsTaskInfoVo.getScheduleObjectName());
		

        List<PrpLCItemKindVo> itemKinds =  policyViewService.findItemKinds(certainsTaskInfoVo.getRegistNo(),null);
		for(PrpLCItemKindVo itemKindVo : itemKinds){
			itemKindMap.put(itemKindVo.getKindCode(),itemKindVo);
		}
		
		// 主表
		logger.info("移动查勘定损提交报文解析中，报案号：" + certainsTaskInfoVo.getRegistNo() + " 修理厂ID：" + certainsTaskInfoVo.getRepairfacId()
				+ " 修理厂名称：" + certainsTaskInfoVo.getRepairfacName() + " 修理厂类型：" + certainsTaskInfoVo.getRepairFacType()
				+ " 修理厂电话：" + certainsTaskInfoVo.getRepairPhone());
		lossCarMainVo.setId(Long.valueOf(certainsTaskInfoVo.getCertainsId()));
		lossCarMainVo.setIsMobileCase("Y");
		lossCarMainVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
		lossCarMainVo.setMercyFlag(certainsTaskInfoVo.getExigenceGree());// 案件紧急程度
		lossCarMainVo.setCetainLossType(certainsTaskInfoVo.getEvalTypeCode());// 定损方式
		lossCarMainVo.setRepairFactoryName(certainsTaskInfoVo.getRepairfacName());// 修理厂名称
		lossCarMainVo.setRepairFactoryType(certainsTaskInfoVo.getRepairFacType());// 修理厂类型
		lossCarMainVo.setFactoryMobile(certainsTaskInfoVo.getRepairPhone());// 修理厂手机
		lossCarMainVo.setIsClaimSelf(certainsTaskInfoVo.getExcessType());// 是否互碰自赔
		lossCarMainVo.setLossFeeType(certainsTaskInfoVo.getLossFeeType());// 损失类别
		lossCarMainVo.setAuditStatus(CodeConstants.AuditStatus.SAVE);// 状态
		lossCarMainVo.setHandlerIdNo(certainsTaskInfoVo.getAssessIdentifyNo());// 定损员身份证号码
		lossCarMainVo.setDefSite(certainsTaskInfoVo.getAssessPlace());// 预约定损地点
		lossCarMainVo.setRepairFactoryCode(certainsTaskInfoVo.getRepairfacId());// 修理厂Id
		lossCarMainVo.setIsCInotpayFlag(certainsTaskInfoVo.getFroceExclusions());// 是否交强拒赔，0-否，1-是
		lossCarMainVo.setIsBInotpayFlag(certainsTaskInfoVo.getBusinessExclusions());// 是否商业拒赔，0-否，1-是
		lossCarMainVo.setNotpayCause(certainsTaskInfoVo.getExclusionsReason());// 拒赔原因
		lossCarMainVo.setDirectFlag(certainsTaskInfoVo.getDocumentsComplete());// 单证是否齐全
		lossCarMainVo.setIsSpecialcarFlag(certainsTaskInfoVo.getIsspecialOperation());// 是否有特种车操作证0-无，1-有
		lossCarMainVo.setIsBusinesscarFlag(certainsTaskInfoVo.getIsbusinessQualification());// 是否有营业车资格证0-无，1-有
		lossCarMainVo.setIsNodutypayFlag(certainsTaskInfoVo.getIsnoResponsibility());// 是否无责代赔0-否，1-是
		if("5".equals(certainsTaskInfoVo.getExclusionsReason())){
			lossCarMainVo.setOtherNotpayCause(certainsTaskInfoVo.getOtherexcReason());// 其它拒赔原因
		}
		if(CodeConstants.CetainLossType.DEFLOSS_ALL.equals(certainsTaskInfoVo.getExigenceGree())){
			lossCarMainVo.setIsTotalLoss("1");// 是否全损
		}else{
			lossCarMainVo.setIsTotalLoss("0");// 是否全损
		}
		List<DefLossCollisionParts> defLossCollisionPartses = certainsTaskInfoVo.getDefLossCollisionParts();// 损失部位
		lossCarMainVo.setIsHotSinceDetonation("0");// 是否火自爆 默认为否
		StringBuffer lossPart = new StringBuffer();// 损失部位
		if(defLossCollisionPartses!=null){
			for (int i = 0; i < defLossCollisionPartses.size(); i++) {	
				if(i == 0){
					lossPart.append(defLossCollisionPartses.get(i).getCollisionWay());
				}else {
					lossPart.append("," + defLossCollisionPartses.get(i).getCollisionWay());
				}
			}
		}
		if(StringUtils.isBlank(lossPart.toString())) {
			throw new IllegalArgumentException(certainsTaskInfoVo.getLicenseNo()+"车辆损失部位不能为空!");
		}
		lossCarMainVo.setRepairType(certainsTaskInfoVo.getPriceType());// 价格类型
		lossCarMainVo.setLossPart(lossPart.toString());// 损失部位
		lossCarMainVo.setIsWaterFloaded("0");// 是否水淹 默认为否
		lossCarMainVo.setSumCompFee(NullToZero(certainsTaskInfoVo.getEvalPartSum()));// 合计换件金额
		lossCarMainVo.setSumRepairFee(NullToZero(certainsTaskInfoVo.getEvalRepairSum()));// 合计修理金额
		lossCarMainVo.setSumMatFee(NullToZero(certainsTaskInfoVo.getEvalMateSum()));// 合计修理辅料费
		lossCarMainVo.setSumRemnant(NullToZero(certainsTaskInfoVo.getRemnantFee()));// 合计定损残值金额
		lossCarMainVo.setSumLossFee(NullToZero(certainsTaskInfoVo.getSumLossAmount()).subtract(NullToZero(certainsTaskInfoVo.getSalvageFee())));// 估损总金额
		lossCarMainVo.setSumRescueFee(NullToZero(certainsTaskInfoVo.getSalvageFee()));// 施救费用
		lossCarMainVo.setSumChargeFee(NullToZero(certainsTaskInfoVo.getSumFeeAmount()));// 总定损费用
		lossCarMainVo.setActualValue(NullToZero(certainsTaskInfoVo.getVirtualValue()));// 实际价值
		lossCarMainVo.setSumManageFee(NullToZero(certainsTaskInfoVo.getManageFee()));// 定损管理费合计
		lossCarMainVo.setSumSelfPayFee(NullToZero(certainsTaskInfoVo.getSelfPaySum()));// 定损自付合计
		lossCarMainVo.setSumOuterFee(NullToZero(certainsTaskInfoVo.getOuterSum()));// 定损外修合计
		lossCarMainVo.setSumOutFee(NullToZero(certainsTaskInfoVo.getOuterSum()));
		lossCarMainVo.setSumDerogationFee(NullToZero(certainsTaskInfoVo.getDerogationSum()));// 定损减损合计
		if(certainsTaskInfoVo.getVerSumFitsFee()!=null && !certainsTaskInfoVo.getVerSumFitsFee().isEmpty()){
			lossCarMainVo.setSumVeriCompFee(new BigDecimal(certainsTaskInfoVo.getVerSumFitsFee()));// 核损合计换件金额
		}
		if(certainsTaskInfoVo.getVerSumRepairFee()!=null && !certainsTaskInfoVo.getVerSumRepairFee().isEmpty()){
			lossCarMainVo.setSumVeriRepairFee(new BigDecimal(certainsTaskInfoVo.getVerSumRepairFee()));// 核损合计修理金额
		}
		if(certainsTaskInfoVo.getVerAccessorFee()!=null && !certainsTaskInfoVo.getVerAccessorFee().isEmpty()){
			lossCarMainVo.setSumVeriMatFee(new BigDecimal(certainsTaskInfoVo.getVerAccessorFee()));// 核损合计辅料金额
		}
		if(certainsTaskInfoVo.getVerSumSalvageFee()!=null && !certainsTaskInfoVo.getVerSumSalvageFee().isEmpty()){
			lossCarMainVo.setSumVeriRemnant(new BigDecimal(certainsTaskInfoVo.getVerSumSalvageFee()));// 核损合计残值金额
		}
		if(certainsTaskInfoVo.getSumVeriAmount()!=null && !certainsTaskInfoVo.getSumVeriAmount().isEmpty()){
			lossCarMainVo.setSumVeriLossFee(new BigDecimal(certainsTaskInfoVo.getSumVeriAmount()));// 核损定损合计金额
		}
		if(certainsTaskInfoVo.getIndemnityDutyRate()!=null && !certainsTaskInfoVo.getIndemnityDutyRate().isEmpty()){
			lossCarMainVo.setIndemnityDutyRate(new BigDecimal(certainsTaskInfoVo.getIndemnityDutyRate()));// 事故责任比例
		}
		lossCarMainVo.setIndemnityDuty(certainsTaskInfoVo.getIndemnityDuty());// 事故责任
		lossCarMainVo.setCiDutyFlag(certainsTaskInfoVo.getCiDutyFlag());// 交强险赔偿责任
		lossCarMainVo.setHandlerCode(certainsTaskInfoVo.getHandlerCode());// 定损员代码
		//新加
		lossCarMainVo.setIsGlassBroken(certainsTaskInfoVo.getIsGlassBroken());   //是否玻璃单独破碎
		lossCarMainVo.setIsNotFindThird(certainsTaskInfoVo.getIsNotFindThird());   //是否属于无法找到第三方
		// 车辆信息表
		lossCarInfoVo = lossCarService.findDefCarInfoByPk(oldCarMainVo.getCarId());
		lossCarInfoVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
		lossCarInfoVo.setCarOwner(certainsTaskInfoVo.getOwern());// 车主
		if(certainsTaskInfoVo.getEnrollDate() != null){
			lossCarInfoVo.setEnrollDate(DateUtils.strToDate(certainsTaskInfoVo.getEnrollDate(),DateUtils.YToSec));// 初登日期
		}
		lossCarInfoVo.setPlatformCarKindCode(certainsTaskInfoVo.getCarKindCode());// 车辆种类代码
		lossCarInfoVo.setFrameNo(certainsTaskInfoVo.getFrameNo());// 车架号
		lossCarInfoVo.setVinNo(certainsTaskInfoVo.getVin());// Vin号
		lossCarInfoVo.setEngineNo(certainsTaskInfoVo.getEngineNo());// 发动机号
		lossCarInfoVo.setLicenseType(certainsTaskInfoVo.getLicenseType());// 车牌种类
		lossCarInfoVo.setLicenseNo(certainsTaskInfoVo.getLicenseNo());// 车牌号码
		lossCarInfoVo.setModelCode(certainsTaskInfoVo.getVehicleModleCode());// 车型代码
		lossCarInfoVo.setModelName(certainsTaskInfoVo.getVehicleModleName());// 车型名称
		lossCarInfoVo.setModelCode(certainsTaskInfoVo.getVehCertainCode());// 车型代码
		lossCarInfoVo.setModelName(certainsTaskInfoVo.getVehCertainName());// 车型名称
		lossCarInfoVo.setDriveName(certainsTaskInfoVo.getDriveName());// 驾驶人
		lossCarInfoVo.setDrivingLicenseNo(certainsTaskInfoVo.getDrivingLicenseNo());// 驾照号码
		lossCarInfoVo.setIdentifyType(certainsTaskInfoVo.getIdentifyType());// 驾驶人证件类型
		lossCarInfoVo.setIdentifyNo(certainsTaskInfoVo.getIdentifyNo());// 驾驶人证件号码
		lossCarInfoVo.setGroupCode(certainsTaskInfoVo.getVehGroupCode());// 车组代码
		lossCarInfoVo.setGroupName(certainsTaskInfoVo.getGroupName());// 车组名称
		lossCarInfoVo.setSeriCode(certainsTaskInfoVo.getVehSeriCode());// 车系编码
		lossCarInfoVo.setSeriName(certainsTaskInfoVo.getVehSeriName());// 车系名称
		lossCarInfoVo.setYearType(certainsTaskInfoVo.getVehYearType());// 年款
		lossCarInfoVo.setFactoryCode(certainsTaskInfoVo.getVehFactoryCode());// 厂家编码
		lossCarInfoVo.setFactoryName(certainsTaskInfoVo.getVehFactoryName());// 厂家名称
		lossCarInfoVo.setBrandCode(certainsTaskInfoVo.getVehBrandCode());// 名牌编码
		lossCarInfoVo.setBrandName(certainsTaskInfoVo.getBrandName());// 名牌名称
		lossCarInfoVo.setSelfConfigFlag(certainsTaskInfoVo.getSelfConfigFlag());// 自定义车型标志
		lossCarInfoVo.setOperatorCode(certainsTaskInfoVo.getNextHandlerCode());// 操作员代码
		lossCarInfoVo.setRemark(certainsTaskInfoVo.getRemark());// 备注
		lossCarInfoVo.setValidFlag("1");// 有效标志
		lossCarInfoVo.setCiPolicyNo(certainsTaskInfoVo.getStIpolicyNo());// 交强险保单号
		lossCarInfoVo.setCiInsureComCode(certainsTaskInfoVo.getStInsurCom());// 交强承保机构
		lossCarInfoVo.setVehKindCode(certainsTaskInfoVo.getVehKindCode());// 定损车辆种类代码
		lossCarInfoVo.setVehKindName(certainsTaskInfoVo.getVehKindName());// 定损车辆种类名称
		lossCarMainVo.setLossCarInfoVo(lossCarInfoVo);
		// 意见表
		claimTextVo.setDescription(certainsTaskInfoVo.getLossOpinion());
		
		// 换件表
		List<DefLossPartInfoVo> partInfoVo = certainsTaskInfoVo.getDefLossPartInfoVos();
		List<PrpLDlossCarCompVo> compVoList = new ArrayList<PrpLDlossCarCompVo>();
		if(partInfoVo!=null && partInfoVo.size()>0){
			String kindCode_X = "";// 涉水险
			if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X1)){
				kindCode_X = CodeConstants.KINDCODE.KINDCODE_X1;
			}else if(itemKindMap.containsKey(CodeConstants.KINDCODE.KINDCODE_X2)){
				kindCode_X = CodeConstants.KINDCODE.KINDCODE_X2;
			}
			int serialNo = 1;
			for(DefLossPartInfoVo partInfo:partInfoVo){	
				PrpLDlossCarCompVo compVo = new PrpLDlossCarCompVo();
				compVo.setIndId(partInfo.getPartId());
				compVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				compVo.setSerialNo(serialNo);
				compVo.setOriginalId(partInfo.getPartCode());// 零配件原厂编码
				compVo.setOriginalName(partInfo.getItemName());// 零配件原厂名称
				compVo.setCompCode(partInfo.getPartCode());// 配件代码
				compVo.setCompName(partInfo.getItemName());// 配件名称
				
				compVo.setDirectSupplyFlag(partInfo.getDirectSupplyFlag());  //配件直供
				
				compVo.setLicenseNo(certainsTaskInfoVo.getLicenseNo());// 车牌号码
				compVo.setRecycleFlag(partInfo.getFitBackFlag());// 损余回收标志
				compVo.setPriceType(partInfo.getChgCompSetCode());// 价格类型
				compVo.setMaterialFee(NullToZero(partInfo.getMaterialFee()));// 材料费
				compVo.setQuantity(Integer.valueOf(partInfo.getCount()));// 数量
				compVo.setSelfConfigFlag(partInfo.getSelfConfigFlag());// 自定义配件标记
				compVo.setSysMarketPrice(NullToZero(partInfo.getSysMarketPrice()));// 系统市场价格
				compVo.setNativeMarketPrice(NullToZero(partInfo.getLocalMarketPrice()));// //本地市场价格
				compVo.setSys4SPrice(NullToZero(partInfo.getSysGuidePrice()));// 系统4S店价
				compVo.setNative4SPrice(NullToZero(partInfo.getLocalGuidePrice()));// 本地4S店价
				compVo.setRemark(partInfo.getRemark());// 备注
				compVo.setRestFee(NullToZero(partInfo.getRemainsPrice()));// 残值
				compVo.setSumDefLoss(NullToZero(partInfo.getEvalPartSum()).subtract(NullToZero(partInfo.getRemainsPrice())));// 核定损金额
				if("1".equals(partInfo.getChgCompSetCode())){// 价格方案系统价格的赋值
					compVo.setChgRefPrice(NullToZero(partInfo.getSysGuidePrice()));// 系统4S店价
				}else {
					compVo.setChgRefPrice(NullToZero(partInfo.getSysMarketPrice()));// 系统市场价
				}
				if(partInfo.getLossFee2()!=null){
					compVo.setSumVeriLoss(new BigDecimal(partInfo.getLossFee2()));// 核定损金额(核损)
				}
				compVo.setVeriRemark(partInfo.getRemark2());// 备注（核损）
				if(partInfo.getLocalPrice()!=null){
					compVo.setChgLocPrice(new BigDecimal(partInfo.getLocalPrice()));// 价格方案本地价格
				}
				String wadFlag = partInfo.getIfWading();// 涉水标识 0否 1是
				if("1".equals(wadFlag) && StringUtils.isNotEmpty(kindCode_X)){
					compVo.setWadFlag(wadFlag);
					compVo.setKindCode(kindCode_X);// 险别代码
					compVo.setKindName(itemKindMap.get(kindCode_X).getKindName());// 险别名称
				}else{
					compVo.setWadFlag("0");
				}
				compVo.setManageSingleRate(NullToZero(partInfo.getManageSingleRate()));// 管理费率
				compVo.setManageSingleFee(NullToZero(partInfo.getManageSingleFee()));// 管理费
				compVo.setSelfPayRate(NullToZero(partInfo.getSelfConfigFlag()));// 自付比例
				compVoList.add(compVo);
			}
			lossCarMainVo.setPrpLDlossCarComps(compVoList);
		}
		
		// 辅料表
		List<DefLossAssistInfoVo> defLossAssistInfoVos = certainsTaskInfoVo.getDefLossAssistInfoVo();
		List<PrpLDlossCarMaterialVo> materialList = new ArrayList<PrpLDlossCarMaterialVo>();
		if(defLossAssistInfoVos != null){
			int serialNo = 1;
			for(DefLossAssistInfoVo assistInfo:defLossAssistInfoVos){
				PrpLDlossCarMaterialVo materialVo = new PrpLDlossCarMaterialVo();
				materialVo.setAssistId(assistInfo.getAssistId());
				materialVo.setSerialNo(serialNo);
				materialVo.setMaterialName(assistInfo.getItemName());// 辅料名称
				materialVo.setUnitPrice(NullToZero(assistInfo.getMaterialFee()));// 辅料单价
				materialVo.setAssisCount(Integer.valueOf(assistInfo.getCount()));// 辅料数量
				materialVo.setMaterialFee(NullToZero(assistInfo.getEvalMateSum()));// 定损金额
				materialVo.setRemark(assistInfo.getRemark());// 备注
				materialVo.setSelfConfigFlag(assistInfo.getSelfConfigFlag());// 自定义辅料标志
				materialVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				materialVo.setKindCode(assistInfo.getItemCoverCode());// 险种代码
				if(StringUtils.isNotEmpty(materialVo.getKindCode())){
					materialVo.setRiskCode(itemKindMap.get(materialVo.getKindCode()).getRiskCode());
					materialVo.setPolicyNo(itemKindMap.get(materialVo.getKindCode()).getPolicyNo());
					materialVo.setItemKindNo(itemKindMap.get(materialVo.getKindCode()).getItemKindNo());
				}
				materialList.add(materialVo);
			}
			lossCarMainVo.setPrpLDlossCarMaterials(materialList);
		}
		
		// 修理信息表
		List<DefLossManhourInfoVo> defLossManhourInfoVos = certainsTaskInfoVo.getDefLossManhourInfoVos();// 内修
		List<DefLossOutRepairInfoVo> defLossOutRepairInfoVos = certainsTaskInfoVo.getDefLossOutRepairInfoVos();// 外修
		List<PrpLDlossCarRepairVo> prpLDlossCarRepairVos = new ArrayList<PrpLDlossCarRepairVo>();
		int serialNo = 1;
		if(defLossManhourInfoVos!=null){	
			for(DefLossManhourInfoVo manhourInfo:defLossManhourInfoVos){
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				repairVo.setRepairId(manhourInfo.getRepairId());
				repairVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				repairVo.setSerialNo(serialNo);
				repairVo.setCompCode(manhourInfo.getRepairModeCode());// 修理项目代码
				repairVo.setCompName(manhourInfo.getItemName());// 修理项目名称
				repairVo.setRepairCode(manhourInfo.getRepairModeCode());// 工种代码
				repairVo.setRepairName(this.getRepairName(manhourInfo.getRepairModeCode()));// 工种名称
				repairVo.setLicenseNo(certainsTaskInfoVo.getLicenseNo());// 车牌
				if(manhourInfo.getRepairLevelCode()!=null){
					repairVo.setLevelCode(new Integer(manhourInfo.getRepairLevelCode().toString()));// 修理项目代码
				}
				repairVo.setLevelName(manhourInfo.getRepairLevelName());// 损失程度名称
				repairVo.setManHour(NullToZero(manhourInfo.getEvalHour()));// 工时
				repairVo.setManHourUnitPrice(NullToZero(manhourInfo.getRepairUnitPrice()));// 工时单价
				repairVo.setKindCode(manhourInfo.getItemCoverCode());
				if(StringUtils.isNotEmpty(repairVo.getKindCode())){
					repairVo.setRiskCode(itemKindMap.get(repairVo.getKindCode()).getRiskCode());
					repairVo.setPolicyNo(itemKindMap.get(repairVo.getKindCode()).getPolicyNo());
					repairVo.setItemKindNo(itemKindMap.get(repairVo.getKindCode()).getItemKindNo());
				}
				repairVo.setSumDefLoss(NullToZero(manhourInfo.getManpowerFee()));// 核定损金额
				if(manhourInfo.getLossFee2()!=null){
					repairVo.setSumVeriLoss(new BigDecimal(manhourInfo.getLossFee2()));// 核定损金额(核损)
				}
				repairVo.setVeriRemark(manhourInfo.getRemark2());// 备注（核损）
				repairVo.setSelfConfigFlag(manhourInfo.getSelfConfigFlag());// 自定义修理件标记
				repairVo.setRepairFlag(RepairFlag.INNERREPAIR);// 内修
				serialNo ++;
				prpLDlossCarRepairVos.add(repairVo);
			}
		}
		
		if(defLossOutRepairInfoVos!=null){
			for (DefLossOutRepairInfoVo outRepairInfo:defLossOutRepairInfoVos) {
				PrpLDlossCarRepairVo repairVo = new PrpLDlossCarRepairVo();
				repairVo.setRepairId(outRepairInfo.getOuterId());
				repairVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				repairVo.setCompCode("");// 修理项目代码
				repairVo.setCompName(outRepairInfo.getOuterName());// 修理项目名称
				repairVo.setRepairCode("");// 工种代码 外修无
				repairVo.setRepairName("");// 工种名称 外修无
				repairVo.setManHour(NullToZero(outRepairInfo.getOutItemAmount()));// 外修数量
				repairVo.setManHourFee(NullToZero(outRepairInfo.getEvalOuterPirce()));// 外修项目单价
				repairVo.setSumDefLoss(NullToZero(outRepairInfo.getRepairOuterSum()));// 定损金额
				repairVo.setDerogationPrice(NullToZero(outRepairInfo.getDerogationPrice()));// 外修项目减损金额
				repairVo.setRepairType("");// 配件价格类型 外修无
				repairVo.setRepairFactoryCode(outRepairInfo.getRepairFactoryId());// 外修修理厂Id
				repairVo.setRepairFactoryName(outRepairInfo.getRepairFactoryName());// 外修修理厂名称
				repairVo.setSelfConfigFlag(outRepairInfo.getRepairHandaddFlag());// 自定义标记
				repairVo.setKindCode(outRepairInfo.getItemCoverCode());// 险别代码
				repairVo.setKindName(CodeTranUtil.transCode("RiskCode",outRepairInfo.getItemCoverCode()));// 险别名称
				if(StringUtils.isNotEmpty(repairVo.getKindCode())){
					repairVo.setRiskCode(itemKindMap.get(repairVo.getKindCode()).getRiskCode());
					repairVo.setPolicyNo(itemKindMap.get(repairVo.getKindCode()).getPolicyNo());
					repairVo.setItemKindNo(itemKindMap.get(repairVo.getKindCode()).getItemKindNo());
				}
				repairVo.setRemark(repairVo.getRemark()); // 备注
				repairVo.setRegistNo(lossCarMainVo.getRegistNo());// 报案号
				repairVo.setLicenseNo(lossCarMainVo.getLicenseNo());// 车牌
				repairVo.setRepairFlag(RepairFlag.OUTREPAIR); // 0-内修 1-外修
				prpLDlossCarRepairVos.add(repairVo);
				serialNo ++;
			}
		}
		lossCarMainVo.setPrpLDlossCarRepairs(prpLDlossCarRepairVos);
		
		// 费用表
		List<DefLossFeeInfoVo> defLossFeeInfoVos = certainsTaskInfoVo.getDefLossfeeInfoVos();
		if(defLossFeeInfoVos!=null){
			for(DefLossFeeInfoVo FeeInfo:defLossFeeInfoVos){
				PrpLDlossChargeVo chargeVo = new PrpLDlossChargeVo();
				chargeVo.setRegistNo(certainsTaskInfoVo.getRegistNo());
				chargeVo.setBusinessType(FlowNode.DLCar.name());
				chargeVo.setSerialNo(Integer.valueOf(FeeInfo.getSerialNo()));
				chargeVo.setKindCode(FeeInfo.getKindCode());
				chargeVo.setChargeCode(FeeInfo.getFeeType());
				chargeVo.setChargeName(CodeTranUtil.transCode("ChargeCode", FeeInfo.getFeeType()));
				if(FeeInfo.getFeeAmount()!=null){
					chargeVo.setChargeFee(new BigDecimal(FeeInfo.getFeeAmount()));
				}
				lossChargeVos.add(chargeVo);
			}
		}
	}
	
	private void init(){
		if(deflossHandleService==null){
			deflossHandleService = (DeflossHandleService)Springs.getBean(DeflossHandleService.class);
		}
		if(wfTaskHandleService==null){
			wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
		}
		if(lossCarService==null){
			lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
		}
		if(lossToPlatformService==null){
			lossToPlatformService = (LossToPlatformService)Springs.getBean(LossToPlatformService.class);
		}
		if(claimTaskService==null){
			claimTaskService = (ClaimTaskService)Springs.getBean(ClaimTaskService.class);
		}
		if(interfaceAsyncService==null){
			interfaceAsyncService = (InterfaceAsyncService)Springs.getBean(InterfaceAsyncService.class);
		}
		if(certifyIlogService==null){
			certifyIlogService = (CertifyIlogService)Springs.getBean(CertifyIlogService.class);
		}	
		if(certifyToPaltformService==null){
			certifyToPaltformService=(CertifyToPaltformService)Springs.getBean(CertifyToPaltformService.class);
		}
		if(compensateTaskService==null){
			compensateTaskService=(CompensateTaskService)Springs.getBean(CompensateTaskService.class);
		}
        if(verifyClaimService == null){
            verifyClaimService = (VerifyClaimService)Springs.getBean(VerifyClaimService.class);
        }
        if(subrogationService == null){
        	subrogationService = (SubrogationService)Springs.getBean(SubrogationService.class);
        }
        if(policyViewService == null){
        	policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
		if(codeTranService == null) {
		    codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
		}
		if(certifyService == null){
			certifyService = (CertifyService)Springs.getBean(CertifyService.class);
		}
		if(managerService == null){
			managerService = (ManagerService)Springs.getBean(ManagerService.class);
		}
	}
	
	private String getRepairName(String repairCode){
		String repairName = "";
		if("01".equals(repairCode)){
			repairName = "喷漆项目";
		}
		if("03".equals(repairCode)){
			repairName = "钣金项目";
		}
		if("04".equals(repairCode)){
			repairName = "电工项目";
		}
		if("05".equals(repairCode)){
			repairName = "机修项目";
		}
		if("06".equals(repairCode)){
			repairName = "拆装项目";
		}
		if("09".equals(repairCode)){
			repairName = "其他";
		}
		return repairName;
	}
	
	public void checkRequest(CertainsTaskRequestVo certainsTaskInfoVo){
		notNull(certainsTaskInfoVo.getRegistNo()," 报案号为空  ");
		notNull(certainsTaskInfoVo.getCertainsId()," 理赔定损ID为空  ");
		notNull(certainsTaskInfoVo.getTaskId()," 任务id为空  ");
		notNull(certainsTaskInfoVo.getNextHandlerCode()," 处理人代码为空  ");
		notNull(certainsTaskInfoVo.getNextHandlerName()," 处理人名称为空  ");
		notNull(certainsTaskInfoVo.getScheduleObjectId()," 处理人员归属机构编码为空  ");
		notNull(certainsTaskInfoVo.getScheduleObjectName()," 处理人员归属机构名称为空  ");
		notNull(certainsTaskInfoVo.getOptionType()," 操作类型为空  ");
		notNull(certainsTaskInfoVo.getExigenceGree()," 案件紧急程度为空  ");
		notNull(certainsTaskInfoVo.getOwern()," 车主为空  ");
		notNull(certainsTaskInfoVo.getCarKindCode()," 车辆种类为空  ");
		if(!codeTranService.validEffictiveValue(certainsTaskInfoVo.getCarKindCode(), "VehicleType")){
			throw new IllegalArgumentException("车辆种类值不在有效范围内");
		}
		notNull(certainsTaskInfoVo.getFrameNo()," 车架号为空  ");
		notNull(certainsTaskInfoVo.getVin()," VIN码为空  ");
		notNull(certainsTaskInfoVo.getLicenseType()," 车牌种类为空  ");
		notNull(certainsTaskInfoVo.getLicenseNo()," 车牌号码为空  ");
		notNull(certainsTaskInfoVo.getVehicleModleName()," 车型名称为空  ");
		notNull(certainsTaskInfoVo.getBrandName()," 厂牌型号为空  ");
		notNull(certainsTaskInfoVo.getEvalTypeCode()," 定损方式为空  ");
		notNull(certainsTaskInfoVo.getExcessType()," 是否互碰自赔为空  ");
		
/***************************************************新增的校验条件******************************************************/
		checkNull(certainsTaskInfoVo.getNodeType(), " 调度节点为空 ", certainsTaskInfoVo.getRegistNo());
		//nodetype调度节点为DLCar 和 DLProp时标的序号，标的名称不为空
		if(StringUtils.isNotEmpty(certainsTaskInfoVo.getNodeType()) && 
				( FlowNode.DLCar.equals(certainsTaskInfoVo.getNodeType()) || 
				  FlowNode.DLProp.equals(certainsTaskInfoVo.getNodeType()))){
			checkNull(certainsTaskInfoVo.getItemNo(), " 标的序号为空 ", certainsTaskInfoVo.getRegistNo());
			checkNull(certainsTaskInfoVo.getItemNoName(), " 标的名称为空 ", certainsTaskInfoVo.getRegistNo());
		}
		checkNull(certainsTaskInfoVo.getIsObject(), " 是否标的为空 ", certainsTaskInfoVo.getRegistNo());
		checkNull(certainsTaskInfoVo.getVehicleModleCode()," 车型编码为空 ", certainsTaskInfoVo.getRegistNo());
		checkNull(certainsTaskInfoVo.getIndemnityDuty(), " 事故责任为空 ", certainsTaskInfoVo.getRegistNo());
		checkNull(certainsTaskInfoVo.getFroceExclusions(), " 交强拒赔为空 ", certainsTaskInfoVo.getRegistNo());
		checkNull(certainsTaskInfoVo.getBusinessExclusions(), " 商业拒赔为空 ", certainsTaskInfoVo.getRegistNo());
		//当为商业拒赔
		if(StringUtils.isNotEmpty(certainsTaskInfoVo.getBusinessExclusions()) && 
				     certainsTaskInfoVo.getBusinessExclusions().equals("1")){
			checkNull(certainsTaskInfoVo.getExclusionsReason(), " 拒赔原因为空 ", certainsTaskInfoVo.getRegistNo());
		}
		checkNull(certainsTaskInfoVo.getDocumentsComplete()," 单证齐全为空  ", certainsTaskInfoVo.getRegistNo());
//		notNull(certainsTaskInfoVo.getEnrollDate(), " 初登日期为空 ");
//		notNull(certainsTaskInfoVo.getVirtualValue(), " 总施救财产实际价值为空 ");
//		notNull(certainsTaskInfoVo.getVehKindCode(), " 定损车辆种类代码为空 ");
//		notNull(certainsTaskInfoVo.getVehKindName(), " 定损车辆种类名称为空 ");
//		notNull(certainsTaskInfoVo.getIsnoResponsibility(), " 是否无责代赔为空 ");
		
		//损失部位校验
		List<DefLossCollisionParts> defLossParts = certainsTaskInfoVo.getDefLossCollisionParts();
		if(defLossParts != null && defLossParts.size() > 0){
			for(DefLossCollisionParts info : defLossParts){
				checkNull(info.getCollisionWay(), " 损失部位为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getCollisionDegree(), " 损失程度为空 ", certainsTaskInfoVo.getRegistNo());
			}
		}
		
		//换件信息校验
		List<DefLossPartInfoVo> defLossInfoList = certainsTaskInfoVo.getDefLossPartInfoVos();
		if(defLossInfoList != null && defLossInfoList.size() > 0){
			
			for(DefLossPartInfoVo info : defLossInfoList){
				checkNull(info.getPartId(), " 定损明细主键为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getItemName(), " 项目名称为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getMaterialFee(), " 定损材料费（定损单价）为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getSelfConfigFlag(), " 自定义配件标记为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getFitBackFlag(), " 回收标志为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRemainsPrice(), " 残值为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getEvalPartSum(), " 换件合计为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRecyclePartFlag(), " 回收方式为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getSelfPayRate(), " 自付比例为空 ", certainsTaskInfoVo.getRegistNo());
//				checkNull(info.getCount(), " 数量为空 ", certainsTaskInfoVo.getRegistNo());
			}
		}
		
		//定损修理信息
		List<DefLossManhourInfoVo> defLossManinfoVoList = certainsTaskInfoVo.getDefLossManhourInfoVos();
		if(defLossManinfoVoList != null && defLossManinfoVoList.size() > 0){
			
			for(DefLossManhourInfoVo info : defLossManinfoVoList){
				checkNull(info.getRepairId(), " 定损明细主键为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairModeCode(), " 修理方式代码为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getItemName(), " 项目名称为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getManpowerFee(), " 定损人工费为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getSelfConfigFlag(), " 自定义修理标记为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getEvalHour(), " 工时数为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairUnitPrice(), " 工时单价为空 ", certainsTaskInfoVo.getRegistNo());
			}
		}
		
		//定损外修信息
		List<DefLossOutRepairInfoVo> defLossOutList = certainsTaskInfoVo.getDefLossOutRepairInfoVos();
		if(defLossOutList != null && defLossOutList.size() > 0){
			
			for(DefLossOutRepairInfoVo info : defLossOutList){
				checkNull(info.getOuterId(), " 外修项目主键为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getOuterName(), " 外修项目名称为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairHandaddFlag(), " 自定义标记为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairLevelCode(), " 修理程度为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getEvalOuterPirce(), " 外修项目定损金额为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getDerogationPrice(), " 外修项目减损金额为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getPartPrice(), " 配件金额为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairFactoryId(), " 外修修理厂id为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getRepairFactoryCode(), " 外修修理厂代码为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getItemCoverCode(), " 险种代码为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getOutItemAmount(), " 外修数量为空 ", certainsTaskInfoVo.getRegistNo());
			}
		}
		
		//定损修理合计
		List<DeflossRepairSumVo> defLossSumList = certainsTaskInfoVo.getDeflossRepairSumVos();
		if(defLossSumList != null && defLossSumList.size() > 0){
			for(DeflossRepairSumVo info : defLossSumList){
				checkNull(info.getWorkTypeCode(), " 工种编码为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getDiscountRefPrice(), " 折后参考工时费为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getEvalRepairSum(), " 定损工时费为空 ", certainsTaskInfoVo.getRegistNo());
//				notNull(info.getItemCount(), " 项目数量为空 ");
//				notNull(info.getReferencePrice(), " 参考工时费为空 ");
//				notNull(info.getHourDiscount(), " 工种折扣 ");
			}
		}
		
		//辅料信息
		List<DefLossAssistInfoVo> defLossAssistList = certainsTaskInfoVo.getDefLossAssistInfoVo();
		if(defLossAssistList != null && defLossAssistList.size() > 0){
			for(DefLossAssistInfoVo info : defLossAssistList){
				checkNull(info.getAssistId(), " 辅料明细主键为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getItemName(), " 辅料名称为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getMaterialFee(), " 定损材料费为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getEvalMateSum(), " 辅料合计为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getSelfConfigFlag(), " 自定义辅料标记为空 ", certainsTaskInfoVo.getRegistNo());
				checkNull(info.getItemCoverCode(), " 险种代码为空 ", certainsTaskInfoVo.getRegistNo());
//				checkNull(info.getCount(), " 数量为空 ", certainsTaskInfoVo.getRegistNo());
			}
		}
		
		//费用信息
		List<DefLossFeeInfoVo> defLossFeeList = certainsTaskInfoVo.getDefLossfeeInfoVos();
		if(defLossFeeList != null && defLossFeeList.size() > 0){
			for(DefLossFeeInfoVo info : defLossFeeList){
				checkNull(info.getKindCode(), " 险别为空 ", certainsTaskInfoVo.getRegistNo());
//				checkNull(info.getSerialNo(), " 序号为空 ", certainsTaskInfoVo.getRegistNo());
//				checkNull(info.getFeeType(), " 费用类型为空 ", certainsTaskInfoVo.getRegistNo());
//				notNull(info.getFeeAmount(), " 费用金额为空 ");
			}
		}
		
/***********************************************新增的校验条件结束*******************************************************/

		
		Assert.notNull(certainsTaskInfoVo.getSumFeeAmount()," 费用合计为空  ");
		notNull(certainsTaskInfoVo.getEngineNo()," 发动机号为空  ");
		if(FlowNode.DLCar.equals(certainsTaskInfoVo.getNodeType())){
			if("1".equals(certainsTaskInfoVo.getItemNo())){// 标的
				notNull(certainsTaskInfoVo.getExcessType()," 是否互碰自赔为空  ");
				notNull(certainsTaskInfoVo.getDocumentsComplete()," 单证齐全为空  ");
				notNull(certainsTaskInfoVo.getIsbusinessQualification()," 是否有营业车资格证为空  ");
				notNull(certainsTaskInfoVo.getIsspecialOperation()," 是否有特种车操作证为空  ");
		    }
		}
		
		// 是否互碰自赔只能传0或1
		if(!CodeConstants.caseFlagType.NORMAL_CASE.equals(certainsTaskInfoVo.getExcessType()) 
				&& !CodeConstants.caseFlagType.SELF_CASE.equals(certainsTaskInfoVo.getExcessType())){
			throw new IllegalArgumentException("是否互碰自赔数据格式错误！");
		}

		// 如果是修复定损和被代位协议定损，则修理厂信息和精友交互信息必传
		if(CodeConstants.CetainLossType.DEFLOSS_REPAIR.equals(certainsTaskInfoVo.getEvalTypeCode()) || 
				CodeConstants.CetainLossType.DEFLOSS_AGREE.equals(certainsTaskInfoVo.getEvalTypeCode())){
			notNull(certainsTaskInfoVo.getRepairfacName()," 修理厂名称为空  ");
			notNull(certainsTaskInfoVo.getRepairFacType()," 修理厂类型为空  ");
			notNull(certainsTaskInfoVo.getRepairfacCode()," 修理厂代码为空  ");
			notNull(certainsTaskInfoVo.getVehCertainCode(),"  定损车型编码为空 ");
			notNull(certainsTaskInfoVo.getVehCertainName(),"  定损车型名称为空 ");
			notNull(certainsTaskInfoVo.getSelfConfigFlag(),"  自定义车型标志为空 ");
			notNull(certainsTaskInfoVo.getVehBrandCode(),"  定损品牌编码为空 ");
			notNull(certainsTaskInfoVo.getVehGroupCode(),"  车组编码为空 ");
			notNull(certainsTaskInfoVo.getGroupName(),"  车组名称为空 ");
			notNull(certainsTaskInfoVo.getSalvageFee()," 施救费为空  ");
			notNull(certainsTaskInfoVo.getRemnantFee()," 定损折扣残值为空  ");
			notNull(certainsTaskInfoVo.getManageFee(),"  定损管理费合计为空 ");
			notNull(certainsTaskInfoVo.getEvalPartSum()," 换件金额合计为空  ");
			notNull(certainsTaskInfoVo.getEvalRepairSum()," 维修金额合计为空  ");
			notNull(certainsTaskInfoVo.getEvalMateSum()," 辅料金额合计为空  ");
			notNull(certainsTaskInfoVo.getSelfPaySum()," 自付合计为空  ");
			notNull(certainsTaskInfoVo.getOuterSum(),"  定损外修合计为空 ");
			notNull(certainsTaskInfoVo.getDerogationSum(),"  减损合计为空 ");
			notNull(certainsTaskInfoVo.getHandlerCode()," 定损员代码为空  ");
			
			checkNull(certainsTaskInfoVo.getSumLossAmount(), " 定损合计为空 ", certainsTaskInfoVo.getRegistNo());
			checkNull(certainsTaskInfoVo.getSelfEstiFlag(), " 自核价标记为空 ", certainsTaskInfoVo.getRegistNo());
//			notNull(certainsTaskInfoVo.getRepairPhone(), " 修理厂手机号为空 ");
		}
		
		notNull(certainsTaskInfoVo.getLossOpinion()," 定损意见为空  ");
		notNull(certainsTaskInfoVo.getIndemnityDutyRate()," 事故责任比例为空  ");
		notNull(certainsTaskInfoVo.getDriveName()," 驾驶人姓名为空  ");
		notNull(certainsTaskInfoVo.getDrivingLicenseNo()," 驾照号码为空  ");
		notExistLowercase(certainsTaskInfoVo.getDrivingLicenseNo()," 驾照号码不能包含小写字母  ");
		notNull(certainsTaskInfoVo.getIdentifyType()," 驾驶人证件类型为空  ");
		notNull(certainsTaskInfoVo.getIdentifyNo()," 驾驶人证件号码为空  ");
		notExistLowercase(certainsTaskInfoVo.getIdentifyNo()," 驾驶人证件号码不能包含小写字母  ");
		notNull(certainsTaskInfoVo.getCiDutyFlag()," 交强责任为空  ");
		notNull(certainsTaskInfoVo.getAssessIdentifyNo()," 定损员身份证为空  ");
		notExistLowercase(certainsTaskInfoVo.getAssessIdentifyNo()," 定损员身份证不能包含小写字母  ");
		notNull(certainsTaskInfoVo.getAssessPlace()," 定损地点为空  ");
		
/**********************************定损员与驾驶员，驾驶员与驾驶员之间的 身份证不能一致*************************************/
//		//校验定损员身份证与驾驶员身份证，两者不能一样
//		if(certainsTaskInfoVo.getIdentifyNo().equals(certainsTaskInfoVo.getAssessIdentifyNo())){
//			 throw new IllegalArgumentException("定损员身份证号不能和驾驶员身份证号一致！请修改！");
//		}
//		//如果有三者车，三者车的驾驶员身份证不能与标的车的驾驶员的身份证号相同
////		if(certainsTaskInfoVo.getIdentifyType().equals("1")){
////			List<PrpLDlossCarMainVo> dlosscarMainList = lossCarService.findLossCarMainByRegistNo(certainsTaskInfoVo.getRegistNo());
////			if(dlosscarMainList != null && dlosscarMainList.size() > 0){
////				for(PrpLDlossCarMainVo dlosscarMain : dlosscarMainList){
////					logger.info("已存在的身份证号: " + dlosscarMain.getLossCarInfoVo().getIdentifyNo());
////					if(certainsTaskInfoVo.getIdentifyNo().equals(dlosscarMain.getLossCarInfoVo().getIdentifyNo())){
////						throw new IllegalArgumentException(" 该驾驶员的身份证号在标的或三者车已经存在，请修改！ ");
////					}
////				}
////			}
////		}
	}

	public void checkNull(String str, String message, String registNo){
		if(StringUtils.isBlank(str)){
			//如果检验为空，则在日志中记录一下
			logger.info("=========>报案号：" + registNo + " --- " + message);
		}
	}
	
	public void notNull(String str,String message){
		if(StringUtils.isBlank(str)){
			throw new IllegalArgumentException(message);
		}
	}
	private void notExistLowercase(String str, String message){
		String regex=".*[a-z]+.*";
		Matcher m=Pattern.compile(regex).matcher(str);
		if(m.matches()){
			throw new IllegalArgumentException(message);
		}
	}
	private static BigDecimal NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			strNum = "0";
		}
		return new BigDecimal(strNum);
	}

}
