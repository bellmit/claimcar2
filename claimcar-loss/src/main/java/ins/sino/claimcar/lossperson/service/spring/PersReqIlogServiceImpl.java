package ins.sino.claimcar.lossperson.service.spring;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.AuditStatus;
import ins.sino.claimcar.certify.service.CertifyHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckPersonVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfTaskQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.RuleReturnDataSaveService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.lossperson.service.PersReqIlogService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.ILPersReqVo;
import ins.sino.claimcar.lossperson.vo.ILPersonnelBasic;
import ins.sino.claimcar.lossperson.vo.ILPersonnelInfoVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceFeeVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceVo;
import ins.sino.claimcar.lossperson.vo.SubmitNextVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.mobile.check.vo.PhotoInfo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.EarlyWarnService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("persReqIlogService")
public class PersReqIlogServiceImpl implements PersReqIlogService {

	private static Logger logger = LoggerFactory.getLogger(PersReqIlogServiceImpl.class);
	
	@Autowired
	ManagerService managerService;
	@Autowired
    RegistService registService;
	@Autowired
	WfTaskQueryService wfTaskQueryService;
	@Autowired
	CertifyHandleService certifyHandleService;
	@Autowired
	EarlyWarnService earlyWarnService;
	@Autowired
	RuleReturnDataSaveService ruleReturnDataSaveService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	EndCasePubService endCasePubService;
	@Autowired
	PersTraceService persTraceService;
	@Autowired
	SaaUserPowerService saaUserPowerService;
	@Autowired
    PrpLCMainService prpLCMainService;
	
	@Override
	public LIlogRuleResVo reqIlogByChe(PrpLCheckVo checkVo, SysUserVo userVo,String ruleNode,PrpLWfTaskVo taskVo,
			List<PhotoInfo> photoInfoList) {
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		LIlogRuleResVo resultVo = new LIlogRuleResVo();
		String reqXML = "";
		String resXML = "";
		String url = SpringProperties.getProperty("ILOG_SVR_URL")+"CheckRuleForPeopleHurtVerifyLossServlet";
		Date date = new Date();
		String registNo = checkVo.getRegistNo();
		PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
		try{
			PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
			List<PrpLCheckPersonVo> personVoList = checkVo.getPrpLCheckTask().getPrpLCheckPersons();
			//????????????
			Map<String, String> registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
			List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
			String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");//????????????
			String biDangerNum = registRiskInfoMap.get("BI-DangerNum");//????????????????????????
			int validityCarFeeMissionNum = checkVo.getPrpLCheckTask().getPrpLCheckCars().size();	//???????????????????????????
			int peoHurtImageNum = 0;	//??????????????????????????????
			int conciliationImageNum = 0;	//???????????????????????????
			int sceneImageNum = 0;	//????????????????????????
			int surveyImage = 0;	//??????????????????
			//??????????????????????????????
			if(photoInfoList!=null && photoInfoList.size()>0){
				for(PhotoInfo photoInfo:photoInfoList){
					if(photoInfo.getPhotoType().startsWith("claim-picture-personLoss")){
						peoHurtImageNum += Integer.valueOf(photoInfo.getPhotoNum());
					}else if("claim-certify-C02-C0204".equals(photoInfo.getPhotoType())){
						conciliationImageNum += Integer.valueOf(photoInfo.getPhotoNum());
					}else if("claim-picture-scenePicture".equals(photoInfo.getPhotoType())){
						sceneImageNum += Integer.valueOf(photoInfo.getPhotoNum());
					}else if("claim-picture-checkReport".equals(photoInfo.getPhotoType())){
						surveyImage += Integer.valueOf(photoInfo.getPhotoNum());
					}
				}
			}else{
				peoHurtImageNum = certifyHandleService.findImageFileBytypePaths(registNo, "claim","picture","personLoss").size();
				conciliationImageNum = certifyHandleService.findImageFileByTypePath(registNo, "claim-certify-C02-C0204").size();
				sceneImageNum = certifyHandleService.findImageFileByTypePath(registNo, "claim-picture-scenePicture").size();
				surveyImage = certifyHandleService.findImageFileByTypePath(registNo, "claim-picture-checkReport").size();
			}
			
			ILPersReqVo reqVo = new ILPersReqVo();
			reqVo.setRegistNo(registNo);
			reqVo.setCasualtyType("1");
			reqVo.setOperateType("1");
			reqVo.setComCode(userVo.getComCode());
			reqVo.setRequestSource("01");
			if(prplIntermMainVo!=null){
				reqVo.setCompanyFlag("0");
			}else{
				reqVo.setCompanyFlag("1");
			}
			if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
				reqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");
			}
			reqVo.setDisclaimerFlag(checkVo.getNoDutyFlag());
			reqVo.setIsReopenClaim("0");
			reqVo.setEmployeeId(userVo.getUserCode());
			reqVo.setReconcileFlag(checkVo.getReconcileFlag());
//			reqVo.setLossPartyName(lossPartyName);
			reqVo.setJQDamagTime(ciDangerNum==null ? 0 : Integer.valueOf(ciDangerNum));
			reqVo.setSYDamagTime(biDangerNum==null ? 0 : Integer.valueOf(biDangerNum));
			reqVo.setValidityCarFeeMissionNum(validityCarFeeMissionNum);
			reqVo.setPeoHurtImageNum(peoHurtImageNum);
			reqVo.setConciliationImageNum(conciliationImageNum);
			reqVo.setSceneImageNum(sceneImageNum);
			reqVo.setSurveyImage(surveyImage);
			reqVo.setSurveySubDate(DateUtils.strToDate(DateUtils.dateToStr(date, DateUtils.YToDay), DateUtils.YToDay));
			reqVo.setSurveySubHour(DateUtils.getFieldValue(date, DateUtils.HOUR));
			reqVo.setSurveySubMinute(DateUtils.getFieldValue(date, DateUtils.MINUTE));
			reqVo.setReportDate(DateUtils.strToDate(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToDay), DateUtils.YToDay));
			reqVo.setReportHour(DateUtils.getFieldValue(registVo.getReportTime(), DateUtils.HOUR));
			reqVo.setReportMinute(DateUtils.getFieldValue(registVo.getReportTime(), DateUtils.MINUTE));
			reqVo.setCaseProcessType(checkVo.getPersHandleType());
			BigDecimal sumLossFee = BigDecimal.ZERO;
			List<ILPersonnelInfoVo> personnelInfoList = new ArrayList<ILPersonnelInfoVo>();
			if(personVoList!=null && personVoList.size()>0){
				for(PrpLCheckPersonVo personVo:personVoList){
					sumLossFee = sumLossFee.add(NullToZero(personVo.getLossFee()));
					ILPersonnelInfoVo personnelInfoVo = new ILPersonnelInfoVo();
					personnelInfoVo.setLossType("3");
					personnelInfoVo.setLossItemType(personVo.getPersonProp());
					personnelInfoVo.setLossPartyName(personVo.getLossPartyId().toString());
					personnelInfoList.add(personnelInfoVo);
				}
			}
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userVo.getUserCode());
			List<String> roleList = userPowerVo.getRoleList();
			String numAccess = "";
			if(roleList.contains(FlowNode.PLTrack_LV1.getRoleCode())){
				numAccess = "CK1,";
			}
			if(roleList.contains(FlowNode.PLTrack_LV2.getRoleCode())){
				numAccess = numAccess+"CK2,";
			}
			if(roleList.contains(FlowNode.PLInjured_LV1.getRoleCode())){
				numAccess = "RS1,";
			}
			if(roleList.contains(FlowNode.PLInjured_LV2.getRoleCode())){
				numAccess = numAccess+"RS2,";
			}
			if(!"".equals(numAccess)){
				numAccess = numAccess.substring(0,numAccess.length()-1);
			}
			reqVo.setNumAccess(numAccess);
			reqVo.setSumAmount(sumLossFee);
			//???????????????????????????????????????????????????
			reqVo.setSurveyFlag("0");
			reqVo.setIsFlagN("0");
			reqVo.setSureLossAmount(sumLossFee);
			reqVo.setPersonnelInfoList(personnelInfoList); 
			reqVo.setMajorcaseFlag(checkVo.getMajorCaseFlag());
			reqVo.setSumPaidFee(BigDecimal.ZERO);
			reqVo.setIsWhethertheloss("0");
			
			//????????????
			reqXML = stream.toXML(reqVo);
			logger.info("=================???????????????ILOG????????????"+reqXML);
			resXML = requestPeopleHurtIlog(reqXML, url, 200);
			System.out.println("=================???????????????ILOG????????????"+resXML);
			logger.info("=================???????????????ILOG????????????"+resXML);
			stream.processAnnotations(LIlogRuleResVo.class);
			resultVo = (LIlogRuleResVo) stream.fromXML(resXML);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=================???????????????ILOG??????"+e.getMessage());
		}
		try{
			IlogDataProcessingVo ilogDataProcessingVo = new IlogDataProcessingVo();
			ilogDataProcessingVo.setBusinessNo(registNo);
			ilogDataProcessingVo.setComCode(userVo.getComCode());
			ilogDataProcessingVo.setRiskCode(registVo.getRiskCode());
			ilogDataProcessingVo.setOperateType("1");
			ilogDataProcessingVo.setRuleType("0");
			ilogDataProcessingVo.setRuleNode(ruleNode);
			ilogDataProcessingVo.setTaskId(taskVo.getTaskId());
			ilogDataProcessingVo.setTriggerNode(FlowNode.Chk.name());
			ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());
			ruleReturnDataSaveService.dealILogResReturnData(resultVo, ilogDataProcessingVo);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=================???????????????ILOG????????????????????????"+e.getMessage());
		}
		
		return resultVo;
	}
	
	@Override
	public LIlogRuleResVo reqIlogByPers(PrpLDlossPersTraceMainVo traceMainVo,PrpLWfTaskVo taskVo,SubmitNextVo submitNextVo,
			SysUserVo userVo,String operateType,String existHeadOffice){
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		LIlogRuleResVo resultVo = new LIlogRuleResVo();
		String reqXML = "";
		String resXML = "";
		String url = SpringProperties.getProperty("ILOG_SVR_URL")+"CheckRuleForPeopleHurtVerifyLossServlet";
		Date date = new Date();
		String registNo = traceMainVo.getRegistNo();
		String auditStatus = submitNextVo.getAuditStatus();
		
		try{
			PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(userVo.getUserCode());
			PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
			List<PrpLEndCaseVo> endCaseVoList = endCasePubService.queryAllByRegistNo(registNo);
			List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(registNo);
			Map<String, String> registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
			PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
			List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);//??????????????????
			
			ILPersReqVo reqVo = new ILPersReqVo();
			List<ILPersonnelBasic> persBasicList = new ArrayList<ILPersonnelBasic>();
			List<ILPersonnelInfoVo> persInfoList = new ArrayList<ILPersonnelInfoVo>();
			reqVo.setRegistNo(registNo);
			if("2".equals(operateType)){//?????????????????????????????????
				if(taskVo.getSubNodeCode().startsWith("PLVerify_LV") || taskVo.getSubNodeCode().startsWith("PLCharge_LV")){
					reqVo.setCurrentNodeNo(Integer.valueOf(taskVo.getSubNodeCode().split("_LV")[1]));
				}
			}
			if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
				reqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");
			}
			if(existHeadOffice != null){
				reqVo.setExistHeadOffice(existHeadOffice);
			} else{
				reqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
			}
			reqVo.setCasualtyType(AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus) ? "1":"0");
			reqVo.setOperateType(operateType);
			reqVo.setComCode(userVo.getComCode());
			reqVo.setSurveySubDate(DateUtils.strToDate(DateUtils.dateToStr(date, DateUtils.YToDay), DateUtils.YToDay));
			reqVo.setSurveySubHour(DateUtils.getFieldValue(date, DateUtils.HOUR));
			reqVo.setSurveySubMinute(DateUtils.getFieldValue(date, DateUtils.MINUTE));
			reqVo.setReportDate(DateUtils.strToDate(DateUtils.dateToStr(registVo.getReportTime(), DateUtils.YToDay), DateUtils.YToDay));
			reqVo.setReportHour(DateUtils.getFieldValue(registVo.getReportTime(), DateUtils.HOUR));
			reqVo.setReportMinute(DateUtils.getFieldValue(registVo.getReportTime(), DateUtils.MINUTE));
			if(FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode()) &&
					AuditStatus.SUBPLCHARGE.equalsIgnoreCase(auditStatus)){
				reqVo.setRequestSource("02");
			}else if(FlowNode.PLFirst.name().equals(taskVo.getSubNodeCode()) &&
					AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus)){
				reqVo.setRequestSource("03");
			}else if(FlowNode.PLNext.name().equals(taskVo.getSubNodeCode()) &&
					AuditStatus.SUBPLCHARGE.equalsIgnoreCase(auditStatus)){
				reqVo.setRequestSource("04");
			}else if(FlowNode.PLNext.name().equals(taskVo.getSubNodeCode()) &&
					AuditStatus.SUBPLVERIFY.equalsIgnoreCase(auditStatus)){
				reqVo.setRequestSource("05");
			}
			if(prplIntermMainVo!=null){
				reqVo.setCompanyFlag("0");
			}else{
				reqVo.setCompanyFlag("1");
			}
			reqVo.setDisclaimerFlag(checkVo.getNoDutyFlag());
			reqVo.setIsReopenClaim(endCaseVoList!=null ? "1":"0");
			reqVo.setEmployeeId(userVo.getUserCode());
			//????????????
			BigDecimal evaluateLossAmount = BigDecimal.ZERO;//??????????????????
			BigDecimal sureLossAmount = BigDecimal.ZERO;//??????????????????
			BigDecimal sumAmount = BigDecimal.ZERO;//???????????????
			if(persTraceVoList!=null && persTraceVoList.size()>0){
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVoList){
					if("1".equals(persTraceVo.getValidFlag())){
						evaluateLossAmount = evaluateLossAmount.add(NullToZero(persTraceVo.getSumReportFee()));
						sureLossAmount = sureLossAmount.add(NullToZero(persTraceVo.getSumdefLoss()));
						if(persTraceVo.getSumdefLoss() != null){
							sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumdefLoss()));
						}else{
							sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumReportFee()));
						}
					}
				}
			}
			reqVo.setEvaluateLossAmount(evaluateLossAmount);
			reqVo.setSureLossAmount(sureLossAmount);
			//????????????
			String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");//????????????
			String biDangerNum = registRiskInfoMap.get("BI-DangerNum");//????????????????????????
			reqVo.setJQDamagTime(ciDangerNum==null ? 0 : Integer.valueOf(ciDangerNum));
			reqVo.setSYDamagTime(biDangerNum==null ? 0 : Integer.valueOf(biDangerNum));
			//????????????????????????????????????????????????
			List<String> carList = new ArrayList<String>();
			List<PrpLWfTaskVo> wfTaskList = wfTaskQueryService.findTaskBySubNode(FlowNode.DLCar.name(),registNo);
			for(PrpLWfTaskVo wfTaskVo:wfTaskList){
				if(!carList.contains(wfTaskVo.getItemName())){
					carList.add(wfTaskVo.getItemName());
				}
			}
			reqVo.setValidityCarFeeMissionNum(carList.size());
			reqVo.setValidityCasualtiesNum(persTraceVoList!=null ? persTraceVoList.size():0);
			//??????????????????????????????
			int peoHurtImageNum = certifyHandleService.findImageFileBytypePaths(registNo, "claim","picture","personLoss").size();
			reqVo.setPeoHurtImageNum(peoHurtImageNum);
			//???????????????????????????
			int conciliationImageNum = certifyHandleService.findImageFileByTypePath(registNo, "claim-certify-C02-C0204").size();
			reqVo.setConciliationImageNum(conciliationImageNum);
			//????????????????????????
			int sceneImageNum = certifyHandleService.findImageFileByTypePath(registNo, "claim-picture-scenePicture").size();
			reqVo.setSceneImageNum(sceneImageNum);
			//??????????????????
			int surveyImage = certifyHandleService.findImageFileByTypePath(registNo, "claim-picture-checkReport").size();
			reqVo.setSurveyImage(surveyImage);
			//?????????????????????????????????
			int responsibilityNum = certifyHandleService.findImageFileByTypePath(registNo, "claim-certify-C02-C0201").size();
			reqVo.setResponsibilityNum(responsibilityNum);
			//????????????????????????????????????
			int diagnosticCertificateNum = certifyHandleService.findImageFileByTypeName(registNo, "??????????????????????????????").size();
			reqVo.setDiagnosticCertificateNum(diagnosticCertificateNum);
			//???????????????????????????
			int reimbursementNum = certifyHandleService.findImageFileByTypeName(registNo, "?????????????????????").size();
			reqVo.setReimbursementNum(reimbursementNum);
			//????????????????????????????????????
			int feeListImage = certifyHandleService.findImageFileByTypeName(registNo, "????????????????????????").size();
			reqVo.setFeeListImage(feeListImage);
			String feeCheckFlag = "";
			if(persTraceVoList!=null && persTraceVoList.size()>0){
				for(PrpLDlossPersTraceVo traceVo:persTraceVoList){
					List<PrpLDlossPersTraceFeeVo> traceFeeVoList = traceVo.getPrpLDlossPersTraceFees();
					if(traceFeeVoList!=null && traceFeeVoList.size()>0){
						for(PrpLDlossPersTraceFeeVo traceFeeVo:traceFeeVoList){
							if(StringUtils.isNotBlank(traceFeeVo.getFeeTypeCode())){
								String feeTypeName = "";
								if("22".equals(submitNextVo.getComCode().substring(0,2))){
									feeTypeName = CodeTranUtil.transCode("SHFeetype", traceFeeVo.getFeeTypeCode());
								}else{
									feeTypeName = CodeTranUtil.transCode("FeeType", traceFeeVo.getFeeTypeCode());
								}
								if(StringUtils.isNotBlank(feeTypeName)&&!feeCheckFlag.contains(feeTypeName)){
									feeCheckFlag = feeCheckFlag+feeTypeName+",";
								}
								
							}
						}
					}
					ILPersonnelBasic personnelBasic = new ILPersonnelBasic();
					ILPersonnelInfoVo persInfoVo = new ILPersonnelInfoVo();
					personnelBasic.setTreatSituation(traceVo.getPrpLDlossPersInjured().getTreatSituation());
					personnelBasic.setWoundCode(traceVo.getPrpLDlossPersInjured().getWoundCode());
					persBasicList.add(personnelBasic);
					persInfoVo.setLossType("3");
					persInfoVo.setLossItemType(traceVo.getPrpLDlossPersInjured().getLossItemType());
					persInfoVo.setLossPartyName(traceVo.getPrpLDlossPersInjured().getSerialNo().toString());
					persInfoList.add(persInfoVo);
				}
			}
			reqVo.setPersonelBasic(persBasicList);
			reqVo.setPersonnelInfoList(persInfoList);
			if(!"".equals(feeCheckFlag)){
				feeCheckFlag = feeCheckFlag.substring(0, feeCheckFlag.length()-1);
			}
			reqVo.setFeeCheckFlag(feeCheckFlag);
			reqVo.setCaseProcessType(traceMainVo.getCaseProcessType());
			reqVo.setSumAmount(sumAmount.add(NullToZero(traceMainVo.getSumChargeFee())));
			//???????????????????????????????????????????????????
			reqVo.setSurveyFlag("0");
			reqVo.setIsFlagN("0");
			SaaUserPowerVo userPowerVo = saaUserPowerService.findUserPower(userVo.getUserCode());
			List<String> roleList = userPowerVo.getRoleList();
			if (roleList != null) {
				logger.info("?????????"+registNo+", userVo.getUserCode()"+userVo.getUserCode()+"?????????????????????"+roleList.toString());
			}
			String numAccess = "";
			if(roleList.contains(FlowNode.PLTrack_LV1.getRoleCode())){
				numAccess = "CK1,";
			}
			if(roleList.contains(FlowNode.PLTrack_LV2.getRoleCode())){
				numAccess = numAccess+"CK2,";
			}
			if(roleList.contains(FlowNode.PLInjured_LV1.getRoleCode())){
				numAccess = "RS1,";
			}
			if(roleList.contains(FlowNode.PLInjured_LV2.getRoleCode())){
				numAccess = numAccess+"RS2,";
			}
			if(!"".equals(numAccess)){
				numAccess = numAccess.substring(0,numAccess.length()-1);
			}
			reqVo.setNumAccess(numAccess);
			reqVo.setMajorcaseFlag(traceMainVo.getMajorcaseFlag());
			reqVo.setSumPaidFee(traceMainVo.getSumChargeFee()!=null ? traceMainVo.getSumChargeFee():BigDecimal.ZERO);
			reqVo.setIsWhethertheloss(traceMainVo.getIsDeroFlag());
			
			
			//????????????
			reqXML = stream.toXML(reqVo);
			logger.info("=================???????????????ILOG????????????"+reqXML);
			resXML = requestPeopleHurtIlog(reqXML, url, 200);
			System.out.println("=================???????????????ILOG????????????"+resXML);
			logger.info("=================???????????????ILOG????????????"+resXML);
			stream.processAnnotations(LIlogRuleResVo.class);
			resultVo = (LIlogRuleResVo) stream.fromXML(resXML);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=================???????????????ILOG??????"+e.getMessage());
			throw new IllegalArgumentException("??????ILOG?????????"+e.getMessage());
		}
		try{
			IlogDataProcessingVo ilogDataProcessingVo = new IlogDataProcessingVo();
			ilogDataProcessingVo.setBusinessNo(registNo);
			ilogDataProcessingVo.setComCode(userVo.getComCode());
			ilogDataProcessingVo.setRiskCode(traceMainVo.getRiskCode());
			ilogDataProcessingVo.setOperateType(operateType);
			ilogDataProcessingVo.setRuleType("0");
			ilogDataProcessingVo.setRuleNode(submitNextVo.getAuditStatus().substring(3,submitNextVo.getAuditStatus().length()));//???????????????????????????????????????????????????
			ilogDataProcessingVo.setTaskId(taskVo.getTaskId());
			ilogDataProcessingVo.setTriggerNode(taskVo.getSubNodeCode());//????????????
			ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());
			ruleReturnDataSaveService.dealILogResReturnData(resultVo, ilogDataProcessingVo);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=================???????????????ILOG????????????????????????"+e.getMessage());
		}
		
		return resultVo;
	}
	
	
	public static BigDecimal NullToZero(BigDecimal strNum) {
		return strNum==null ? new BigDecimal("0") : strNum;
	}

	@Override
	public LIlogRuleResVo reqIlogByOldPers(PrpLDlossPersTraceMainVo traceMainVo,String policyComCode,String operateType) {
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		stream.aliasSystemAttribute(null,"class");
		LIlogRuleResVo resultVo = new LIlogRuleResVo();
		String reqXML = "";
		String resXML = "";
		String url = SpringProperties.getProperty("ILOG_SVR_URL")+"CheckRuleForPeopleHurtVerifyLossServlet";
		String registNo = traceMainVo.getRegistNo();
		try{
			List<PrpLDlossPersTraceVo> persTraceVoList = persTraceService.findPersTraceVoByRegistNo(registNo);
			ILPersReqVo reqVo = new ILPersReqVo();
			reqVo.setRegistNo(registNo);
			reqVo.setComCode(policyComCode);
			
			reqVo.setOperateType(operateType);
			reqVo.setCasualtyType("0");
			
			//????????????
			BigDecimal evaluateLossAmount = BigDecimal.ZERO;//??????????????????
			BigDecimal sureLossAmount = BigDecimal.ZERO;//??????????????????
			BigDecimal sumAmount = BigDecimal.ZERO;//???????????????
			if(persTraceVoList!=null && persTraceVoList.size()>0){
				for(PrpLDlossPersTraceVo persTraceVo:persTraceVoList){
					if("1".equals(persTraceVo.getValidFlag())){
						evaluateLossAmount = evaluateLossAmount.add(NullToZero(persTraceVo.getSumVeriReportFee()));
						sureLossAmount = sureLossAmount.add(NullToZero(persTraceVo.getSumVeriDefloss()));
						if(persTraceVo.getSumVeriDefloss() != null){
							sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumVeriDefloss()));
						}else{
							sumAmount = sumAmount.add(NullToZero(persTraceVo.getSumVeriReportFee()));
						}
					}
				}
			}
			reqVo.setEvaluateLossAmount(evaluateLossAmount);
			reqVo.setSureLossAmount(sureLossAmount);
			reqVo.setSumAmount(sumAmount.add(NullToZero(traceMainVo.getSumVeriChargeFee())));
			
			
			reqVo.setMajorcaseFlag(traceMainVo.getMajorcaseFlag());
			reqVo.setSumPaidFee(traceMainVo.getSumChargeFee()!=null ? traceMainVo.getSumChargeFee():BigDecimal.ZERO);
			reqVo.setIsWhethertheloss(traceMainVo.getIsDeroFlag());
			
			
			//????????????
			reqXML = stream.toXML(reqVo);
			logger.info("=================???????????????ILOG????????????"+reqXML);
			resXML = requestPeopleHurtIlog(reqXML, url, 200);
			System.out.println("=================???????????????ILOG????????????"+resXML);
			logger.info("=================???????????????ILOG????????????"+resXML);
			stream.processAnnotations(LIlogRuleResVo.class);
			resultVo = (LIlogRuleResVo) stream.fromXML(resXML);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("=================???????????????ILOG??????"+e.getMessage());
			throw new IllegalArgumentException("??????ILOG?????????"+e.getMessage());
		}
		
		return resultVo;
	}
	
	/**
	 * 	??????????????????ILOG??????
	 * @param requestXML ????????????
	 * @param urlStr ????????????????????????
	 * @param seconds ?????????????????????
	 * @return
	 * @throws Exception
	 */
	private String requestPeopleHurtIlog(String requestXML, String urlStr, int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml = "";
		StringBuffer buffer = new StringBuffer();
		try {

			URL url = new URL(urlStr);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// ?????????????????????GET/POST???
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
			httpUrlConn.setConnectTimeout(seconds * 1000);

			httpUrlConn.connect();

			String outputStr = requestXML;

			OutputStream outputStream = httpUrlConn.getOutputStream();
			// ???????????????????????????
			if (null != outputStr) {
				// ??????????????????????????????????????? outputStream.write
				outputStream.write(outputStr.getBytes("GBK"));
			}

			// ???????????????????????????????????????
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {

				buffer.append(str);
			}
			if (buffer.length() < 1) {
				throw new Exception("??????ILOG??????????????????????????????");
			}
			bufferedReader.close();
			inputStreamReader.close();
			// ????????????
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			System.out.println(buffer);
			responseXml = buffer.toString();

		} catch (ConnectException ce) {
			throw new Exception("??????ILOG??????????????????????????????????????????", ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("?????????ILOG??????????????????????????????", e);
		} finally {
			logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
		}
		return responseXml;
	}

}
