package ins.sino.claimcar.genilex.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.HttpClientHander;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carchild.util.CarchildUtil;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.vo.PrpLDisasterVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.genilex.ReportInfoService;
import ins.sino.claimcar.genilex.comResVo.SoapEnvelopeVo;
import ins.sino.claimcar.genilex.po.PrpLFraudScore;
import ins.sino.claimcar.genilex.vo.Applicant;
import ins.sino.claimcar.genilex.vo.BodyVo;
import ins.sino.claimcar.genilex.vo.ClaimMain;
import ins.sino.claimcar.genilex.vo.ClaimReportLinker;
import ins.sino.claimcar.genilex.vo.ClaimSuccor;
import ins.sino.claimcar.genilex.vo.CoverageItem;
import ins.sino.claimcar.genilex.vo.DataPacketReqVo;
import ins.sino.claimcar.genilex.vo.Driver;
import ins.sino.claimcar.genilex.vo.Endorsement;
import ins.sino.claimcar.genilex.vo.EntitiesVo;
import ins.sino.claimcar.genilex.vo.Incident;
import ins.sino.claimcar.genilex.vo.Insured;
import ins.sino.claimcar.genilex.vo.Policy;
import ins.sino.claimcar.genilex.vo.PolicyAgent;
import ins.sino.claimcar.genilex.vo.Report;
import ins.sino.claimcar.genilex.vo.ReportPerson;
import ins.sino.claimcar.genilex.vo.SoapenVo;
import ins.sino.claimcar.genilex.vo.SpecifiedDriver;
import ins.sino.claimcar.genilex.vo.Vehicle;
import ins.sino.claimcar.genilex.vo.VehicleOwner;
import ins.sino.claimcar.genilex.vo.common.AccountInfo;
import ins.sino.claimcar.genilex.vo.common.FraudRequest;
import ins.sino.claimcar.genilex.vo.common.PrpLFraudScoreVo;
import ins.sino.claimcar.genilex.vo.common.Requestor;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpCcarDriverVo;
import ins.sino.claimcar.regist.vo.PrpDagentVo;
import ins.sino.claimcar.regist.vo.PrpLCInsuredVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPersonLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistPropLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("reportInfoService")
public class ReportInfoServiceImpl implements ReportInfoService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportInfoServiceImpl.class);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String FALSEFLAG = "false"; 
	@Autowired
	AreaDictService areaDictService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    CheckHandleService checkService;
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Override
    public SoapEnvelopeVo organizaForReport(PrpLRegistVo vo,SysUserVo userVo,PrpLCMainVo prpLCMainVo) {
        
        String xmlToSend = "";
        String url = "";
        String xmlReturn = "";
        SoapEnvelopeVo soapEnvelopeVo = new SoapEnvelopeVo();
        //报案信息start==========================================
        Report report = new Report();
        PrpLRegistExtVo prpLRegistExtVo = vo.getPrpLRegistExt();
        report.setReportNo(vo.getRegistNo());
        report.setReportTime(formatter.format(vo.getReportTime()));
        report.setReporterCode(vo.getReportorName());
        report.setReporter(vo.getReportorName());
        report.setSubrogationFlag(prpLRegistExtVo.getIsSubRogation());
        report.setReportTel(vo.getReportorPhone());
        List<PrpLCInsuredVo> prpLCInsuredVoList = prpLCMainVo.getPrpCInsureds();
        for(PrpLCInsuredVo prpLCInsuredVo : prpLCInsuredVoList){
            if("1".equals(prpLCInsuredVo.getInsuredFlag())){
                report.setMessageTel(prpLCInsuredVo.getMobile());//被保险人电话
                report.setInsuredTel(prpLCInsuredVo.getMobile());
                report.setInsrntCnm(prpLCInsuredVo.getInsuredName());
            }
        }
        report.setReporterMode("1");
        report.setRunningBegin("");
        report.setRunningEnd("");
        report.setAskingforRescueInd(prpLRegistExtVo.getIsRescue());
        report.setCatastropheLossInd("");
        report.setSpecialCaseInd("");
        report.setLaterReportInd("");
        //异地出险标志
        List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
        prpLCMainVoList = policyViewService.getPolicyAllInfo(vo.getRegistNo());
        if(!prpLCMainVoList.isEmpty()){
            //保单归属地编码
            if(prpLCMainVoList.get(0).getComCode() != null){
                String code = areaDictService.findAreaList("areaCode",prpLRegistExtVo.getCheckAddressCode());
                //承保地区
                String comCode = "";
                if(prpLCMainVoList.size()==2){
                    for(PrpLCMainVo cMainVo:prpLCMainVoList){
                        if(("12").equals(cMainVo.getRiskCode().substring(0, 2))){
                            comCode = cMainVo.getComCode();
                        }
                    }
                }else{
                    comCode = prpLCMainVoList.get(0).getComCode();
                }
                //是否异地案件
                if(code != null && comCode!=""){
                    if("0002".equals(code.substring(0, 4))){//深圳
                        if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
                            report.setUnlocalInd("1");//是
                        }else{
                            report.setUnlocalInd("0");//否
                        }
                    }else{
                        if("0002".equals(comCode.substring(0, 4))){//添加深圳的单这种情况
                            if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
                                report.setUnlocalInd("1");//是
                            }else{
                                report.setUnlocalInd("0");//否
                            }
                        }else{
                            if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
                                report.setUnlocalInd("1");//是
                            }else{
                                report.setUnlocalInd("0");//否
                            }
                        }
                    }
                }else{
                    report.setUnlocalInd("1");//是
                }
            }
        }else{
            report.setUnlocalInd("");//是
        }
        report.setUnlocalInd("");
        report.setCurrencyCode("CNY");
        report.setProbableIncurredAmt("");
        report.setReportedDeptCode(userVo.getComCode());
        report.setReportedBy("");
        report.setHandleTime(formatter.format(vo.getReportTime()));
        List<PrpLRegistCarLossVo> prpLRegistCarLossVoList = vo.getPrpLRegistCarLosses();
        List<PrpLRegistPersonLossVo> prpLRegistPersonLossVoList = vo.getPrpLRegistPersonLosses();
        List<PrpLRegistPropLossVo> prpLRegistPropLossVoList = vo.getPrpLRegistPropLosses();
        if(prpLRegistCarLossVoList != null && prpLRegistCarLossVoList.size() > 1){
            report.setLossTypeInd1("1");
            report.setLossTypeInd2("1");
            report.setThirdVehNumber(String.valueOf(prpLRegistCarLossVoList.size()-1));
        }else if(prpLRegistCarLossVoList != null && prpLRegistCarLossVoList.size() == 1){ 
            report.setLossTypeInd1("1");
        }
        report.setThirdPropertyLossNumber("");
        report.setOthersLossNumber("");
        report.setPropertyLossNumber("");
        if("1".equals(prpLRegistExtVo.getIsPropLoss())){
            int thirdPropertyLossNumber = 0;
            int othersLossNumber = 0;
            for(PrpLRegistPropLossVo propLossVo : prpLRegistPropLossVoList){
                if("1".equals(propLossVo.getLossparty()) && "1".equals(propLossVo.getDamagelevel())){
                    report.setPropertyLossNumber("1");
                    report.setLossTypeInd3("1");
                }else if("0".equals(propLossVo.getLossparty()) && "1".equals(propLossVo.getDamagelevel())){
                    othersLossNumber += 1;
                    report.setLossTypeInd3("1");
                }else if("1".equals(propLossVo.getDamagelevel())){
                    thirdPropertyLossNumber += 1;
                    report.setLossTypeInd3("1");
                }
            }
            report.setThirdPropertyLossNumber(String.valueOf(thirdPropertyLossNumber));
            report.setOthersLossNumber(String.valueOf(othersLossNumber));
        }
        if("1".equals(prpLRegistExtVo.getIsPersonLoss())){
            report.setInjuredNumber(String.valueOf(prpLRegistPersonLossVoList.get(0).getInjuredcount()+prpLRegistPersonLossVoList.get(0).getDeathcount()));
            report.setThirdInjurredNumber(String.valueOf(prpLRegistPersonLossVoList.get(1).getInjuredcount()+prpLRegistPersonLossVoList.get(1).getDeathcount()));
            report.setLossTypeInd4("1");
        }
        report.setTargetVehicleNumber("1");
        report.setPolicyNo(vo.getPolicyNo());
        report.setLicenceNo(prpLRegistExtVo.getLicenseNo());
        report.setBrandName(prpLRegistCarLossVoList.get(0).getBrandName());
        report.setEngineNo(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getEngineNo());
        report.setFrameNo(prpLRegistExtVo.getFrameNo());
        report.setCaseFlag("");
        report.setSpotOutInd(prpLRegistExtVo.getIsOnSitReport());
        report.setSpotOutReasion("");
        if("1".equals(prpLRegistExtVo.getCheckType())){
          report.setSelfSurveyType("1");
        }else{
            report.setSelfSurveyType("3");
        }
        report.setDriverCode(vo.getDriverName());
        report.setDriverName(vo.getDriverName());
        report.setRemark("");
        report.setCreateBy(vo.getCreateUser());
        report.setCreateTime(formatter.format(vo.getCreateTime()));
        report.setUpdateBy(vo.getUpdateUser());
        report.setUpdateTime(formatter.format(vo.getUpdateTime()));
        List<Incident> incidents = new ArrayList<Incident>();
        Incident incident = new Incident();
        incident.setDriverCode(vo.getDriverName());
        incident.setDriverName(vo.getDriverName());
        incident.setLossTime(formatter.format(vo.getDamageTime()));
        incident.setLossDesc(prpLRegistExtVo.getDangerRemark());
        //prpLRegistVo.damageAreaCode
        incident.setLossAreaProvince(vo.getDamageAreaCode().substring(0,2)+"0000");
        incident.setLossAreaCity(vo.getDamageAreaCode().substring(0,4)+"00");
        incident.setLossAreaRegion(vo.getDamageAreaCode());
        incident.setOcccurredCountryside(vo.getDamageAreaCode());
        incident.setOccurrencyAreaCode("");
        incident.setOccurrencyRoute(vo.getDamageAddress());
        incident.setDriverLicenseNo("");
        incident.setLossCauseCode(vo.getDamageCode());
        incident.setLicensePlateNo(prpLRegistCarLossVoList.get(0).getLicenseNo());
        incident.setLicensePlateType(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getLicenseKindCode());
        incident.setIncidentTypeCode(vo.getAccidentReason());
        incident.setOcccurredSpot(vo.getDamageAddress());
        incident.setOcccurredSpotAttr("");
        if("4".equals(prpLRegistExtVo.getManageType())){
            incident.setPoliceTreatment("1");
        }else{
            incident.setPoliceTreatment("0");
        }
        
        if("3".equals(prpLRegistExtVo.getCheckType())){//1 3
            if("1".equals(report.getUnlocalInd())){
                incident.setSpotTypeCode("3");//第一现场异地出险
            }else{
                incident.setSpotTypeCode("1");//第一现场异地出险
            }
        }else{//0  2
            if("1".equals(report.getUnlocalInd())){
                incident.setSpotTypeCode("2");//非第一现场异地出险
            }else{
                incident.setSpotTypeCode("0");//非第一现场非异地出险
            }
        }
        incident.setCreateBy(vo.getCreateUser());
        incident.setCreateTime(formatter.format(vo.getCreateTime()));
        incident.setUpdateBy(vo.getUpdateUser());
        incident.setUpdateTime(formatter.format(vo.getUpdateTime()));
        incidents.add(incident);
        report.setIncidentsList(incidents);
        
        ReportPerson reportPerson = new ReportPerson();
        reportPerson.setReporeCode(vo.getReportorName());
        reportPerson.setName(vo.getReportorName());
        reportPerson.setPhone(vo.getReportorPhone());
        reportPerson.setMobile("");
        reportPerson.setEmail("");
        reportPerson.setBirthday("");
        reportPerson.setSex("");
        reportPerson.setRelationWithInsured("");
        reportPerson.setRelationWithOwner("");
        reportPerson.setCreateBy(vo.getCreateUser());
        reportPerson.setCreateTime(formatter.format(vo.getCreateTime()));
        reportPerson.setUpdateBy(vo.getUpdateUser());
        reportPerson.setUpdateTime(formatter.format(vo.getUpdateTime()));
        report.setReportPerson(reportPerson);
        
        Driver driver = new Driver();
        driver.setDriverCode(vo.getDriverName());
        driver.setName(vo.getDriverName());
        driver.setPhone("");
        driver.setMobile("");
        driver.setEmail("");
        driver.setBirthday("");
        driver.setSex("");
        driver.setRelationWithInsured("");
        driver.setRelationWithOwner("");
        driver.setCreateBy(vo.getCreateUser());
        driver.setCreateTime(formatter.format(vo.getCreateTime()));
        driver.setUpdateBy(vo.getUpdateUser());
        driver.setUpdateTime(formatter.format(vo.getUpdateTime()));
        report.setDriver(driver);
        List<ClaimReportLinker> claimReportLinkers = new ArrayList<ClaimReportLinker>();
        ClaimReportLinker claimReportLinker = new ClaimReportLinker();
        claimReportLinker.setLinkerCode(vo.getLinkerName());
        claimReportLinker.setLinkerName(vo.getLinkerName());
        claimReportLinker.setPhone(vo.getLinkerPhone());
        claimReportLinker.setMobile(vo.getLinkerMobile());
        claimReportLinker.setEmail("");
        claimReportLinker.setBirthday("");
        claimReportLinker.setSex("");
        claimReportLinker.setRelationWithInsured("");
        claimReportLinker.setRelationWithOwner("");
        claimReportLinker.setCreateBy(vo.getCreateUser());
        claimReportLinker.setCreateTime(formatter.format(vo.getCreateTime()));
        claimReportLinker.setUpdateBy(vo.getUpdateUser());
        claimReportLinker.setUpdateTime(formatter.format(vo.getUpdateTime()));
        claimReportLinkers.add(claimReportLinker);
        report.setClaimReportLinker(claimReportLinkers);
        
        List<ClaimSuccor> claimSuccors = new ArrayList<ClaimSuccor>();
        for(int i = 1;i < prpLRegistCarLossVoList.size();i++){
            ClaimSuccor claimSuccor = new ClaimSuccor();
            claimSuccor.setSerialNo(String.valueOf(i));
            claimSuccor.setLinkmanName(vo.getLinkerName());
            claimSuccor.setLinkTel(vo.getLinkerPhone());
            claimSuccor.setSuccorDescribe("");
            claimSuccor.setSuccorSpot("");
            claimSuccor.setIssuccorflag("");
            claimSuccor.setAidCompany("");
            claimSuccor.setAidCompanyname("");
            claimSuccor.setServiceItem("");
            claimSuccor.setServiceItemName("");
            claimSuccor.setHandleTime(formatter.format(vo.getReportTime()));
            claimSuccor.setDispatchCenterNo("");
            claimSuccor.setRemark("");
            claimSuccor.setCreateBy(vo.getCreateUser());
            claimSuccor.setCreateTime(formatter.format(vo.getCreateTime()));
            claimSuccor.setUpdateBy(vo.getUpdateUser());
            claimSuccor.setUpdateTime(formatter.format(vo.getUpdateTime()));
            claimSuccors.add(claimSuccor);
        }
        report.setClaimSuccor(claimSuccors);
        List<Report> reports = new ArrayList<Report>();
        reports.add(report);
        //报案信息end==========================================
        
        //保单信息start==========================================
        List<Policy> policys = this.organizaForPolicyInfo(vo, userVo, prpLCMainVoList);
        //保单信息end============================================
        
        //赔案主档信息列表start==========================================
        List<ClaimMain> claimMains = this.organizaForClaimMain(vo, userVo, prpLCMainVoList);
        //赔案主档信息列表end============================================
        
        //发送报文start======================
        SoapenVo soapenVo = new SoapenVo();
        BodyVo bodyReqVo = new BodyVo();
        DataPacketReqVo dataPacketReqVo = new DataPacketReqVo();
        Requestor requestor = new Requestor();
        FraudRequest fraudRequest = new FraudRequest();
		List<PrpLWfTaskVo> voBIList = wfFlowQueryService.findPrpWfTaskVo(vo.getRegistNo(), "Regis", "Regis");
		if(voBIList != null && voBIList.size() > 0){
			requestor.setReference(voBIList.get(0).getTaskId().toString());
		}
        requestor.setTimestamp(formatter.format(vo.getReportTime()));
        requestor.setLineOfBusiness("PM");
        requestor.setPointOfRequest("POC");
        requestor.setTransactionType("C");
        requestor.setEchoEntities(FALSEFLAG);
        requestor.setEchoProductRequests(FALSEFLAG);
        AccountInfo accountInfo = new AccountInfo();
        String userName = SpringProperties.getProperty("GENILEX_USERNAME");
        String userPswd = SpringProperties.getProperty("GENILEX_USERPSWD");
        accountInfo.setUserName(userName);
        accountInfo.setUserPswd(userPswd);
        accountInfo.setCryptType("04");
        requestor.setAccountInfo(accountInfo);
        fraudRequest.setProductRequestId("01");
        fraudRequest.setReportNo(vo.getRegistNo());
        fraudRequest.setScoredType("01");
        fraudRequest.setServiceType("C001");
        EntitiesVo entitiesVo = new EntitiesVo();
        entitiesVo.setPolicys(policys);
        entitiesVo.setReports(reports);
        entitiesVo.setClaimMains(claimMains);
        List<FraudRequest> fraudRequests = new ArrayList<FraudRequest>();
        fraudRequests.add(fraudRequest);
        dataPacketReqVo.setFraudRequests(fraudRequests);
        dataPacketReqVo.setEntitiesVo(entitiesVo);
        dataPacketReqVo.setRequestorVo(requestor);
        bodyReqVo.setDataPacketVo(dataPacketReqVo);
        soapenVo.setBodyVo(bodyReqVo);
        xmlToSend = ClaimBaseCoder.objToXmlUtf(soapenVo);
        url = SpringProperties.getProperty("GENILEX_URL");
        LOGGER.info("报案信息提交send---------------------------"+xmlToSend);
        try {
			xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,"");
			LOGGER.info("报案信息返回报文send---------------------------"+xmlReturn);
			soapEnvelopeVo = ClaimBaseCoder.xmlToObj(xmlReturn, SoapEnvelopeVo.class);
		} catch (Exception e) {
			LOGGER.error("报案信息返回报文send异常",e);
		} finally {
			saveGenilexInterfaceLog(userVo, vo, soapenVo, soapEnvelopeVo, url);
		}
        //发送报文end======================
        return soapEnvelopeVo;
    }
	
    
    public List<Policy> organizaForPolicyInfo(PrpLRegistVo registVo ,SysUserVo userVo,List<PrpLCMainVo> prpLCMainVo) {
        List<Policy> policys = new ArrayList<Policy>();
        for(PrpLCMainVo vo : prpLCMainVo){
            Policy policy = new Policy();
            policy.setPolicyNo(vo.getPolicyNo());
            List<PrpLCInsuredVo> prpLCInsuredVos = vo.getPrpCInsureds();
            List<PrpLCengageVo> prpLCengageVos = vo.getPrpCengages();
            List<PrpLCItemCarVo> prpLCItemCarVos = vo.getPrpCItemCars();
            List<PrpLCItemKindVo> prpLCItemKindVos = vo.getPrpCItemKinds();
            List<Applicant> applicants = new ArrayList<Applicant>();//投保人信息列表
            List<Insured> insureds = new ArrayList<Insured>();//被保险人信息列表
            for(PrpLCInsuredVo insuredVo : prpLCInsuredVos){
              if("2".equals(insuredVo.getInsuredFlag())){//投保人
                  policy.setRelationWithInsured(insuredVo.getInsuredIdentity());
                  Applicant applicant = new Applicant();
                  applicant.setApplicantCode(insuredVo.getInsuredCode());
                  applicant.setApplicantName(insuredVo.getInsuredName());
                  applicant.setApplicantType(insuredVo.getInsuredType());
                  applicant.setApplicantNature(insuredVo.getInsuredNature());
                  applicant.setCredentialCode(insuredVo.getIdentifyType());
                  applicant.setCredentialNo(insuredVo.getIdentifyNumber());
                  applicant.setGenderCode(insuredVo.getSex());
                  applicant.setBirthday("");
                  applicant.setDeathDate("");
                  applicant.setHeight("");
                  applicant.setWeight("");
                  applicant.setMarriageStatus("");
                  applicant.setEducateLevel("");
                  applicant.setGuardianName("");
                  applicant.setCountry("");
                  applicant.setProvince("");
                  applicant.setCity("");
                  applicant.setAddress("");
                  applicant.setPostAddress(insuredVo.getPostAddress());
                  applicant.setZip(insuredVo.getPostCode());
                  applicant.setPhoneNo(insuredVo.getPhoneNumber());
                  applicant.setEmail(insuredVo.getEmail());
                  applicant.setOrganizationType("");
                  applicant.setCreditRating("");
                  applicant.setRemark("");
                  applicant.setCreateBy(vo.getCreateUser());
                  applicant.setCreateTime(formatter.format(vo.getCreateTime()));
                  applicant.setUpdateBy(vo.getUpdateUser());
                  applicant.setUpdateTime(formatter.format(vo.getUpdateTime()));
                  applicants.add(applicant);
              }
              if("1".equals(insuredVo.getInsuredFlag())){//投保人
                  policy.setRelationWithInsured(insuredVo.getInsuredIdentity());
                  Insured insured = new Insured();
                  insured.setInsuredCode(insuredVo.getInsuredCode());
                  insured.setInsuredName(insuredVo.getInsuredName());
                  insured.setInsuredNature(insuredVo.getInsuredNature());
                  insured.setCredentialCode(insuredVo.getIdentifyType());
                  insured.setCredentialNo(insuredVo.getIdentifyNumber());
                  insured.setGenderCode(insuredVo.getSex());
                  insured.setBirthday("");
                  insured.setDeathDate("");
                  insured.setHeight("");
                  insured.setWeight("");
                  insured.setMarriageStatus("");
                  insured.setEducateLevel("");
                  insured.setGuardianName("");
                  insured.setCountry("");
                  insured.setProvince("");
                  insured.setCity("");
                  insured.setAddress("");
                  insured.setPostAddress(insuredVo.getPostAddress());
                  insured.setZip(insuredVo.getPostCode());
                  insured.setPhoneNo(insuredVo.getPhoneNumber());
                  insured.setEmail(insuredVo.getEmail());
                  insured.setOrganizationType("");
                  insured.setCreditRating("");
                  insured.setRemark("");
                  insured.setCreateBy(vo.getCreateUser());
                  insured.setCreateTime(formatter.format(vo.getCreateTime()));
                  insured.setUpdateBy(vo.getUpdateUser());
                  insured.setUpdateTime(formatter.format(vo.getUpdateTime()));
                  insureds.add(insured);
              }
              
            }
            //1-交强险;2-商业险;
            if("12".equals(vo.getRiskCode().substring(0, 2))){
            	policy.setPolicyType("2");
            }else{
            	policy.setPolicyType("1");
            }
            if(vo.getEndorseTimes() != null){
            	policy.setEdrPrjNo(vo.getEndorseTimes().toString());
            }
            
            policy.setGroupFlag("");//团单标志
            policy.setLastPolicyNo("");//上年投保保单号
            policy.setConfirmSequenceNo("");
            policy.setApplicationFormNo(vo.getProposalNo());
            if(vo.getOperateDate() != null){
            	 policy.setBillDate(formatter.format(vo.getOperateDate()));
            }else{
            	 policy.setBillDate("");
            }
            if(vo.getStartDate() != null){
            	policy.setEffectiveDate(formatter.format(vo.getStartDate()));//没有加“分”
	        }else{
		        policy.setEffectiveDate("");//没有加“分”
		    }
            if(vo.getEndDate() != null){
            	policy.setExpireDate(formatter.format(vo.getEndDate()));
	        }else{
		        policy.setExpireDate("");//没有加“分”
		    }
            
            policy.setPeriodDesc("");
            String clauses = "";
            for(PrpLCengageVo cengageVo : prpLCengageVos){
                clauses = clauses + cengageVo.getClauses();
            }
            policy.setSpecialAgreementDesc(clauses);
            policy.setAgent(vo.getHandlerCode());
            policy.setOperator(vo.getOperatorCode());
            policy.setCurrencyCode("CNY");
            policy.setTotalBasicPremium("");
            policy.setTotalStandardPremium("");
            policy.setTotalPremium("");
            policy.setAreaFlag("");
            policy.setChannelCode("");
            policy.setPayType(vo.getPayMode());
            policy.setReinsrcFlg(vo.getReinsFlag());
            policy.setProductType("");
            policy.setCreateBy(vo.getCreateUser());
            policy.setCreateTime(formatter.format(vo.getCreateTime()));
            policy.setUpdateBy(vo.getUpdateUser());
            policy.setUpdateTime(formatter.format(vo.getUpdateTime()));
            policy.setApplicants(applicants);
            policy.setInsureds(insureds);
            policy.setSubordinateCode(vo.getComCode());//承保机构码
            List<Vehicle> vehicles = new ArrayList<Vehicle>();
            for(PrpLCItemCarVo prpLCItemCarVo : prpLCItemCarVos){
                VehicleOwner vehicleOwner = new VehicleOwner();
                vehicleOwner.setVehicleOwneCode(prpLCItemCarVo.getCarOwner());
                vehicleOwner.setVehicleOwnerName(prpLCItemCarVo.getCarOwner());
                vehicleOwner.setVehicleOwnerNature(prpLCItemCarVo.getUseNatureCode());
                vehicleOwner.setCredentialCode("");
                vehicleOwner.setCredentialNo("");
                vehicleOwner.setGenderCode("");
                vehicleOwner.setBirthday("");
                vehicleOwner.setDeathDate("");
                vehicleOwner.setHeight("");
                vehicleOwner.setWeight("");
                vehicleOwner.setMarriageStatus("");
                vehicleOwner.setEducateLevel("");
                vehicleOwner.setGuardianName("");
                vehicleOwner.setCountry("");
                vehicleOwner.setProvince("");
                vehicleOwner.setCity("");
                vehicleOwner.setAddress("");
                vehicleOwner.setPostAddress("");
                vehicleOwner.setZip("");
                vehicleOwner.setPhoneNo("");
                vehicleOwner.setEmail("");
                vehicleOwner.setOrganizationType("");
                vehicleOwner.setCreditRating("");
                vehicleOwner.setRemark("");
                vehicleOwner.setCreateBy(vo.getCreateUser());
                vehicleOwner.setCreateTime(formatter.format(vo.getCreateTime()));
                vehicleOwner.setUpdateBy(vo.getUpdateUser());
                vehicleOwner.setUpdateTime(formatter.format(vo.getUpdateTime()));
                policy.setVehicleOwner(vehicleOwner);
                
                
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleID(prpLCItemCarVo.getItemCarId().toString());
                vehicle.setLicensePlateNo(prpLCItemCarVo.getLicenseNo());
                vehicle.setLicensePlateColorCode(prpLCItemCarVo.getLicenseColorCode());
                vehicle.setLicensePlateType(prpLCItemCarVo.getLicenseKindCode());
                vehicle.setMotorTypeCode(prpLCItemCarVo.getCarType());
                vehicle.setMotorUsageTypeCode(prpLCItemCarVo.getUseNatureCode());
                if(prpLCItemCarVo.getEnrollDate()!=null){
                	vehicle.setFirstRegisterDate(formatter.format(prpLCItemCarVo.getEnrollDate()));
                }
                vehicle.setvIN(prpLCItemCarVo.getVinNo());
                vehicle.setEngineNo(prpLCItemCarVo.getEngineNo());
                vehicle.setBodyColor(prpLCItemCarVo.getColorCode());
                vehicle.setVehicleNature(prpLCItemCarVo.getCarAttachNature());
                vehicle.setImportFlag("");
                if(prpLCItemCarVo.getDamagePurchasePrice() != null){
                    vehicle.setVehicleValue(prpLCItemCarVo.getDamagePurchasePrice().toString()); 
                }
                if(prpLCItemCarVo.getWholeWeight() != null){
                	vehicle.setWholeWeight(prpLCItemCarVo.getWholeWeight().toString());
                }
                vehicle.setRatedPassengerCapacity("");
                vehicle.setTonnage("");
                vehicle.setMadeFactory("");
                vehicle.setModel(prpLCItemCarVo.getModelCode());
                vehicle.setBrandName(prpLCItemCarVo.getBrandName());
                vehicle.setBrandCode("");
                vehicle.setHaulage("");
                if(prpLCItemCarVo.getExhaustScale() != null){
                    vehicle.setDisplacement(prpLCItemCarVo.getExhaustScale().toString());
                }
                vehicle.setPower("");
                if(prpLCItemCarVo.getMakeDate() != null){
                	vehicle.setLfDate(formatter.format(prpLCItemCarVo.getMakeDate()));
                }else{
                	vehicle.setLfDate("");
                }
                
                vehicle.setGlassType("");
                vehicle.setPmQueryNo("");
                vehicle.setCreateBy(vo.getCreateUser());
                vehicle.setCreateTime(formatter.format(vo.getCreateTime()));
                vehicle.setUpdateBy(vo.getUpdateUser());
                vehicle.setUpdateTime(formatter.format(vo.getUpdateTime()));
                vehicles.add(vehicle);
            }
            policy.setVehicles(vehicles);
            List<CoverageItem> coverageItems = new ArrayList<CoverageItem>();
            for(PrpLCItemKindVo prpLCItemKindVo : prpLCItemKindVos){
                CoverageItem coverageItem = new CoverageItem();
                coverageItem.setPlanCode(prpLCItemKindVo.getRiskCode());
                coverageItem.setCoverageCode(prpLCItemKindVo.getKindCode());
                if("Y".equals(prpLCItemKindVo.getCalculateFlag())){//1-主险；2-附险；
                    coverageItem.setMainRiderFlag("1");
                }else{
                    coverageItem.setMainRiderFlag("2");
                }
                if(prpLCItemKindVo.getAmount() != null){
                    coverageItem.setSumLimit(prpLCItemKindVo.getAmount().toString());
                }
                if(prpLCItemKindVo.getDiscount() != null){
                    coverageItem.setDiscountValue(prpLCItemKindVo.getDiscount().toString());
                }
                if(prpLCItemKindVo.getBasePremium() != null){
                	coverageItem.setBasicPremium(prpLCItemKindVo.getBasePremium().toString());
                }
                if(prpLCItemKindVo.getBenchMarkPremium() != null){
                	coverageItem.setStandardPremuim(prpLCItemKindVo.getBenchMarkPremium().toString());
                }
                if(prpLCItemKindVo.getPremium() != null){
                	coverageItem.setPremuim(prpLCItemKindVo.getPremium().toString());
                }
                if(prpLCItemKindVo.getUnitAmount() != null){
                	 coverageItem.setLimitAmount(prpLCItemKindVo.getUnitAmount().toString());
                }
                if(prpLCItemKindVo.getEndTime() != null){
                	coverageItem.setEndDate(formatter.format(prpLCItemKindVo.getEndTime()));
                	coverageItem.setCancleDate(formatter.format(prpLCItemKindVo.getEndTime()));
                }else{
                	coverageItem.setEndDate("");
                	coverageItem.setCancleDate("");
                }
                if(prpLCItemKindVo.getDeductibleRate() != null){
                    coverageItem.setStraightDeductibleRate(prpLCItemKindVo.getDeductibleRate().toString());
                }
                if(prpLCItemKindVo.getDeductible() != null){
                	coverageItem.setStraightDeductible(prpLCItemKindVo.getDeductible().toString());
                }
                coverageItem.setAppSit(prpLCItemKindVo.getNoDutyFlag());
                coverageItem.setCurrency("CNY");
                coverageItem.setCreateBy(vo.getCreateUser());
                coverageItem.setCreateTime(formatter.format(vo.getCreateTime()));
                coverageItem.setUpdateBy(vo.getUpdateUser());
                coverageItem.setUpdateTime(formatter.format(vo.getUpdateTime()));
                coverageItems.add(coverageItem);
            }
            policy.setCoverageItems(coverageItems);
            
            //商业险批单信息
            List<PrppheadVo> vos = new ArrayList<PrppheadVo>();
            List<Endorsement> endorsements = new ArrayList<Endorsement>();
            vos = registQueryService.findByPolicyNo(vo.getPolicyNo());
            if(!vos.isEmpty()){
                for(PrppheadVo prppheadVo : vos){
                    Endorsement endorsement = new Endorsement();
                    endorsement.setEndorsementNo(prppheadVo.getEndorseNo());
                    endorsement.setEdrPrjNo(String.valueOf(prppheadVo.getEndorseTimes()));
                    endorsement.setEdrType(prppheadVo.getEndorType());//批改类型
                    endorsement.setEdrReson(prppheadVo.getReason());
                    if(prppheadVo.getValidDate() != null){
                    	endorsement.setEffectiveTime(formatter.format(prppheadVo.getValidDate()));
                    }else{
                    	endorsement.setEffectiveTime("");
                    }
                    endorsement.setAgent(prppheadVo.getHandlerName());
                    endorsement.setOperator(vo.getOperatorCode());
                    endorsement.setCreateBy(vo.getCreateUser());
                    endorsement.setCreateTime(formatter.format(vo.getCreateTime()));
                    endorsement.setUpdateBy(vo.getUpdateUser());
                    endorsement.setUpdateTime(formatter.format(vo.getUpdateTime()));
                    endorsements.add(endorsement);
                }
            }
            policy.setEndorsements(endorsements);
            List<PrpDagentVo> prpDagentVos = registQueryService.findPrpdagent(vo.getAgentCode());
            if(!prpDagentVos.isEmpty()){
                PolicyAgent policyAgent = new PolicyAgent();//代理人信息
                PrpDagentVo prpDagentVo = prpDagentVos.get(0);
                policyAgent.setAgentTypeCode(prpDagentVo.getAgentType());
                policyAgent.setAgentCode(vo.getAgentCode());
                policyAgent.setAgentName(vo.getAgentName());
                policyAgent.setCertificateNo(prpDagentVo.getPermitNo());
                policyAgent.setCreateBy(vo.getCreateUser());
                policyAgent.setCreateTime(formatter.format(vo.getCreateTime()));
                policyAgent.setUpdateBy(vo.getUpdateUser());
                policyAgent.setUpdateTime(formatter.format(vo.getUpdateTime()));
                policyAgent.setCreateBy(vo.getCreateUser());
                policyAgent.setCreateTime(formatter.format(vo.getCreateTime()));
                policyAgent.setUpdateBy(vo.getUpdateUser());
                policyAgent.setUpdateTime(formatter.format(vo.getUpdateTime()));
                policy.setPolicyAgent(policyAgent);
            }

            List<SpecifiedDriver> specifiedDrivers = new ArrayList<SpecifiedDriver>();
            List<PrpCcarDriverVo> prpCcarDriverVos = registQueryService.findPrpCcarDriver(vo.getPolicyNo());
            if(!prpCcarDriverVos.isEmpty()){
                for(PrpCcarDriverVo carDriverVo : prpCcarDriverVos){
                    SpecifiedDriver specifiedDriver = new SpecifiedDriver();
                    specifiedDriver.setDriverCode(carDriverVo.getDrivingLicenseNo());
                    specifiedDriver.setDriverName(carDriverVo.getDriverName());
                    specifiedDriver.setCredentialNo(carDriverVo.getIdentifynumber());
                    specifiedDriver.setLicenseNo(carDriverVo.getDrivingLicenseNo());
                    if(carDriverVo.getAcceptLicenseDate() != null){
                       specifiedDriver.setLicensedDate(formatter.format(carDriverVo.getAcceptLicenseDate()));
                    }
                    specifiedDriver.setGenderCode(carDriverVo.getSex());
                    specifiedDriver.setMarriageStatus(carDriverVo.getMarriage());
                    specifiedDriver.setAddress(carDriverVo.getDriverAddress());
                    specifiedDriver.setCreateBy(vo.getCreateUser());
                    specifiedDriver.setCreateTime(formatter.format(vo.getCreateTime()));
                    specifiedDriver.setUpdateBy(vo.getUpdateUser());
                    specifiedDriver.setUpdateTime(formatter.format(vo.getUpdateTime()));
                    specifiedDrivers.add(specifiedDriver);
                }
            }
            policy.setSpecifiedDrivers(specifiedDrivers);
            policys.add(policy);
        }
        return policys;
    }
    
    public List<ClaimMain> organizaForClaimMain(PrpLRegistVo vo,SysUserVo userVo,List<PrpLCMainVo> prpLCMainVo) {
        PrpLRegistExtVo prpLRegistExtVo  = vo.getPrpLRegistExt();
        List<ClaimMain> claimMains = new ArrayList<ClaimMain>();
        for(PrpLCMainVo cMainVo : prpLCMainVo){
            ClaimMain claimMain = new ClaimMain();
            claimMain.setReportNo(cMainVo.getRegistNo());
            claimMain.setClaimNo(cMainVo.getRegistNo()+"_"+cMainVo.getPolicyNo());
            claimMain.setClaimStatus("0");
            claimMain.setDepartmentCode(cMainVo.getComCode());
            claimMain.setPolicyNo(cMainVo.getPolicyNo());
            if(cMainVo.getEndorseTimes() != null){
            	claimMain.setEdrPrjNo(cMainVo.getEndorseTimes().toString());
            }
            claimMain.setPlanCode(cMainVo.getRiskCode());
            claimMain.setLossTime(formatter.format(vo.getDamageTime()));
            claimMain.setReportTime(formatter.format(vo.getReportTime()));
            PrpLDisasterVo disasterVo = checkService.findDisasterVoByRegistNo(vo.getRegistNo());
            if(disasterVo != null){
                claimMain.setHugeLossCode("1");
            }else{
                claimMain.setHugeLossCode("0");
            }
            claimMain.setCollidedThirdInd(prpLRegistExtVo.getIsClaimSelf());
            claimMain.setActiontype("0");
            claimMain.setSubrogationFlag(prpLRegistExtVo.getIsSubRogation());
            claimMain.setCreateBy(vo.getCreateUser());
            claimMain.setCreateTime(formatter.format(vo.getCreateTime()));
            claimMain.setUpdateBy(vo.getUpdateUser());
            claimMain.setUpdateTime(formatter.format(vo.getUpdateTime()));
            claimMains.add(claimMain);
        }
        return claimMains;
    }
    
    private void saveGenilexInterfaceLog(SysUserVo userVo,PrpLRegistVo registVo,SoapenVo reqVo,SoapEnvelopeVo resVo,String url){     
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        String requestXml = "";
        String responseXml = "";
        try{
        	logVo.setBusinessType(BusinessType.GENILEX_Regist.name());
            logVo.setBusinessName(BusinessType.GENILEX_Regist.getName());
            
            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null,"class");// 去掉 class属性
            requestXml = stream.toXML(reqVo);
            responseXml = stream.toXML(resVo);
            if("C".equals(resVo.getBodyVo().getDataPacketVo().getRequestor().getTransactionType())){
                logVo.setStatus("1");
                logVo.setErrorCode("true");
            }else {
                logVo.setStatus("0");
                logVo.setErrorCode(FALSEFLAG);
            }
            if(StringUtils.isNotEmpty(resVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallMessage())){
                logVo.setErrorMessage(resVo.getBodyVo().getDataPacketVo().getResponseSummaryVo().getOverallMessage());
            }
        }
        catch(Exception e){
            logVo.setStatus("0");
            logVo.setErrorCode(FALSEFLAG);
            LOGGER.error("报案信息返回报文send异常",e);
        }
        finally{
            logVo.setRegistNo(registVo.getRegistNo());
            logVo.setOperateNode(FlowNode.Regis.name());
            logVo.setComCode(userVo.getComCode());
            Date date = new Date();
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXml);
            logVo.setResponseXml(responseXml);
            interfaceLogService.save(logVo);
        }
       
    }


	@Override
	public PrpLFraudScoreVo findPrpLFraudScoreVoByRegistNo(String registNo,String scoreNode) {
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("reportNo",registNo);
		rule.addEqual("scoreNode",scoreNode);
		rule.addDescOrder("createTime");
		List<PrpLFraudScore> prpLFraudScores=databaseDao.findAll(PrpLFraudScore.class,rule);
		PrpLFraudScoreVo scoreVo=null;
		if(prpLFraudScores!=null && prpLFraudScores.size()>0){
			scoreVo=new PrpLFraudScoreVo();
			scoreVo=Beans.copyDepth().from(prpLFraudScores.get(0)).to(PrpLFraudScoreVo.class);
		}
		
		return scoreVo;
	}


	@Override
	public List<PrpLFraudScoreVo> findPrpLFraudScoreVoListByRegistNo(String registNo) {
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("reportNo",registNo);
		List<PrpLFraudScore> prpLFraudScores=databaseDao.findAll(PrpLFraudScore.class,rule);
		List<PrpLFraudScoreVo> voList=null;
		if(prpLFraudScores!=null && prpLFraudScores.size()>0){
			voList=new ArrayList<PrpLFraudScoreVo>();
			voList=Beans.copyDepth().from(prpLFraudScores).toList(PrpLFraudScoreVo.class);
		}
		return voList;
	}


	@Override
	public PrpLFraudScoreVo findPrpLFraudScoreById(Long fraudScoreID) {
		PrpLFraudScoreVo prpLFraudScoreVo = null;
		PrpLFraudScore prpLFraudScore = databaseDao.findByPK(PrpLFraudScore.class, fraudScoreID);
		if(prpLFraudScore!=null){
			prpLFraudScoreVo = new PrpLFraudScoreVo();
			prpLFraudScoreVo = Beans.copyDepth().from(prpLFraudScore).to(PrpLFraudScoreVo.class);
		}
		return prpLFraudScoreVo;
	}
	
    
    
} 
