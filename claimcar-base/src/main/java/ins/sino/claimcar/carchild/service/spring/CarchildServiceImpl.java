package ins.sino.claimcar.carchild.service.spring;


import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.common.util.ConfigUtil;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.sysuser.service.facade.SysUserService;
import ins.platform.utils.ClaimBaseCoder;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carchild.schedule.po.PrplCarchildSchedule;
import ins.sino.claimcar.carchild.service.CarchildService;
import ins.sino.claimcar.carchild.util.CarchildUtil;
import ins.sino.claimcar.carchild.vo.AccountVo;
import ins.sino.claimcar.carchild.vo.EndorInfoVo;
import ins.sino.claimcar.carchild.vo.EngageInfoVo;
import ins.sino.claimcar.carchild.vo.HisClaimVo;
import ins.sino.claimcar.carchild.vo.HisclaimInfoVo;
import ins.sino.claimcar.carchild.vo.KindInfoVo;
import ins.sino.claimcar.carchild.vo.PolicyInfoVo;
import ins.sino.claimcar.carchild.vo.PrplCarchildScheduleVo;
import ins.sino.claimcar.carchild.vo.PrplcarchildregistcancleVo;
import ins.sino.claimcar.carchild.vo.RegistBodyVo;
import ins.sino.claimcar.carchild.vo.RegistInfoReqVo;
import ins.sino.claimcar.carchild.vo.RegistInfoResVo;
import ins.sino.claimcar.carchild.vo.RegistInformationVo;
import ins.sino.claimcar.carchild.vo.ReportInfoVo;
import ins.sino.claimcar.carchild.vo.RevokeBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreBodyVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoReqVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreInfoResVo;
import ins.sino.claimcar.carchild.vo.RevokeRestoreTaskInfoVo;
import ins.sino.claimcar.carchild.vo.RevokeTaskInfoVo;
import ins.sino.claimcar.carchild.vo.ScheduleItemVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildHeadVo;
import ins.sino.claimcar.carchildCommon.vo.CarchildResponseHeadVo;
import ins.sino.claimcar.carchildren.po.Prplcarchildregistcancle;
import ins.sino.claimcar.carchildren.vo.CarchildReqHeadVo;
import ins.sino.claimcar.carchildren.vo.CarchildResHeadVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassReqBodyVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassReqVo;
import ins.sino.claimcar.carchildren.vo.CaseCancleNopassResVo;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.claim.service.ClaimSummaryService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimCancelVo;
import ins.sino.claimcar.claim.vo.PrpLClaimSummaryVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrploldCompensateVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskQueryVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossprop.service.PropLossService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLCengageVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrppMainVo;
import ins.sino.claimcar.regist.vo.PrppheadVo;
import ins.sino.claimcar.regist.vo.PrpptextVo;
import ins.sino.claimcar.schedule.service.ScheduleService;
import ins.sino.claimcar.schedule.vo.PrpLScheduleDefLossVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
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
import java.util.TreeMap;

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


/**
 * ???????????????
 * <pre></pre>
 * @author ???LinYi
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "carchildService")
public class CarchildServiceImpl implements CarchildService {
    
    private static Logger logger = LoggerFactory.getLogger(CarchildServiceImpl.class);
    private static final String CT_01="dhDockingService.saveReportInformation";
    private static final String CT_02="dhDockingService.cancelCase";
    private static final String CT_03="dhDockingService.rebackOrder";
    private static final String CT_04="dhDockingService.revokeOrder";
    
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private PrpLCMainService prpLCMainService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private RegistService registService;
    @Autowired
    private ClaimSummaryService claimSummaryService;
    @Autowired
    private PayCustomService payCustomService;
    @Autowired
    private WfMainService wfMainService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    
    @Autowired
    ClaimInterfaceLogService claimInterfaceLogService;

    @Autowired
    LossCarService lossCarService;
    @Autowired
    PropLossService propLossService;
    @Autowired
    ScheduleTaskService scheduleTaskService;
    @Autowired
    WfFlowQueryService wfFlowQueryService;
    @Autowired
    AreaDictService areaDictService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    SysUserService sysUserService;
    @Override
    public RegistInfoResVo sendRegistInformation(PrpdIntermMainVo prplIntermMainVo,PrpLRegistVo registVo,List<PrpLCMainVo> prpLCMainVoList,List<PrpLWfTaskVo> prpLWfTaskVoList
                                                 ,RegistInformationVo registInformationVo) throws Exception {
        
        //????????????????????????????????????????????????
        //PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByComCode(comCode);
        String url = null;
        RegistInfoReqVo reqVo = new RegistInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        RegistBodyVo body = new RegistBodyVo();
        String isMobileCase = null;
        
        String xmlToSend = "";
        String xmlReturn = "";
        RegistInfoResVo resVo = new RegistInfoResVo();
        try{
            if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                if("0003".equals(prplIntermMainVo.getIntermCode())){
                    //?????????????????????  
                    head.setRequestType("CT_001");
                    head.setUser("claim_user");
                    head.setPassWord("claim_psd");
                    isMobileCase = "2";
                    reqVo.setHead(head);
                    this.setRequestBody(registVo,body,prpLCMainVoList,registInformationVo);
                    reqVo.setBody(body);
                    //============????????????
                    xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
                    url=SpringProperties.getProperty("CT_URL")+CT_01;
                    //url ="http://10.0.10.3:8090/httpsproxy/carchildservice?type="+CT_01;
                    logger.info("??????????????????send---------------------------"+xmlToSend);
                    xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_01);
                    //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
                    logger.info("??????????????????return---------------------------"+xmlReturn);
                   
                }else {
                    //?????????????????????
                    head.setRequestType("MTA_001");
                    head.setUser("claim_user");
                    head.setPassWord("claim_psd");
                    url = SpringProperties.getProperty("MTA_URL_REPORTCASE");//????????????MTA_URL
                    logger.info("url=============="+url);
                   
                    isMobileCase = "3";
                    
                    reqVo.setHead(head);
                    this.setRequestBody(registVo,body,prpLCMainVoList,registInformationVo);
                    reqVo.setBody(body);
                    
                    xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
                    logger.info("??????????????????Body---------------------------???{}",reqVo.getBody().getScheduleItems()!=null?reqVo.getBody().getScheduleItems().size():0);
                    logger.info("??????????????????send---------------------------"+xmlToSend);
                    xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,head.getRequestType());
                    //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
                    logger.info("??????????????????return---------------------------"+xmlReturn);
                }
    
            }
            resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RegistInfoResVo.class);
        } 
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //???????????????/??????????????????
            /*if("1".equals(flag)){        
                PrpLWfMainVo prpLWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registVo.getRegistNo());
                if("0".equals(prpLWfMainVo.getIsMobileCase()) || StringUtils.isBlank(prpLWfMainVo.getIsMobileCase())){
                    prpLWfMainVo.setIsMobileCase(isMobileCase);
                    wfMainService.update(prpLWfMainVo);
                }
                
                //??????in???
                for(PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskVoList){
                    prpLWfTaskVo.setIsMobileAccept(isMobileCase);
                }
                wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
            }else if("2".equals(flag)){
                //??????in???
                for(PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskVoList){
                    prpLWfTaskVo.setIsMobileAccept(isMobileCase);
                }
                wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
            }*/
            PrpLWfMainVo prpLWfMainVo = wfMainService.findPrpLWfMainVoByRegistNo(registVo.getRegistNo());
            if("0".equals(prpLWfMainVo.getIsMobileCase()) || StringUtils.isBlank(prpLWfMainVo.getIsMobileCase())){
                prpLWfMainVo.setIsMobileCase(isMobileCase);
                prpLWfMainVo.setUpdateTime(new Date());
                wfMainService.update(prpLWfMainVo);
            }
            
            //????????????errNo??????????????????0-?????? 1-?????? 2-??????????????????
            //???001??????????????????errNo???0???2?????????????????????main????????????????????????????????????task?????????????????????????????????
            //??????in???
            if (resVo != null) {
            	CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
            	if(carChildResponseHead != null && "1".equals(carChildResponseHead.getErrNo())){
            		for(PrpLWfTaskVo prpLWfTaskVo : prpLWfTaskVoList){
            			prpLWfTaskVo.setIsMobileAccept(isMobileCase);
            		} 
            	}
            }
            
            wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
            //????????????
            SysUserVo userVo = new SysUserVo();
            userVo = registInformationVo.getUser();
            this.saveCarchildInterfaceLog(userVo,registVo,reqVo,resVo,url);
        }
        return resVo;
    }
    
    /**
     * ?????????????????????????????????
     * <pre></pre>
     * @param registVo
     * @param body
     * @param prpLCMainVoList
     * @modified:
     * ???LinYi(2017???10???12??? ??????8:59:49): <br>
     */
    private void setRequestBody(PrpLRegistVo registVo,RegistBodyVo body,List<PrpLCMainVo> prpLCMainVoList,RegistInformationVo registInformationVo) { ReportInfoVo reportInfo = new ReportInfoVo();
        List<ScheduleItemVo> scheduleItems = new ArrayList<ScheduleItemVo>();
        HisclaimInfoVo hisclaimInfo = new HisclaimInfoVo();
        List<HisClaimVo> hisClaims = new ArrayList<HisClaimVo>();
        SimpleDateFormat Timeformatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //????????????
        reportInfo.setRegistNo(registVo.getRegistNo());
        reportInfo.setInsuredName(registVo.getPrpLRegistExt().getInsuredName());
        reportInfo.setDriverName(registVo.getDriverName());
        reportInfo.setLicenseNo(registVo.getPrpLRegistExt().getLicenseNo());
        reportInfo.setBrandName(registVo.getPrpLRegistCarLosses().get(0).getBrandName());
        reportInfo.setReportorName(registVo.getReportorName());
        reportInfo.setReportPhoneNumber(registVo.getReportorPhone());
        reportInfo.setLinkerName(registVo.getLinkerName());
        reportInfo.setLinkerPhoneNumber(registVo.getLinkerMobile());
        reportInfo.setExigenceGree(registVo.getMercyFlag());
        /*reportInfo.setRiskWarning("");
        reportInfo.setTelCallTines();*/
        reportInfo.setReportDate(Timeformatter.format(registVo.getReportTime()));
        reportInfo.setDamageDate(Timeformatter.format(registVo.getDamageTime()));
        reportInfo.setDamageCode(registVo.getDamageCode());
        reportInfo.setDamageAdress(registVo.getDamageAddress());
        reportInfo.setAccidentDesc(registVo.getPrpLRegistExt().getDangerRemark());
        List<PrpLRegistCarLossVo> prpLRegistCarLossVos = registVo.getPrpLRegistCarLosses();
        if(prpLRegistCarLossVos!=null&&prpLRegistCarLossVos.size()>0){
            for(PrpLRegistCarLossVo prpLRegistCarLossVo:prpLRegistCarLossVos){
                if("1".equals(prpLRegistCarLossVo.getLosspart())){
                    reportInfo.setBrandName(prpLRegistCarLossVo.getBrandName());
                }
            }
        }
            
        body.setReportInfo(reportInfo);
        
        //????????????
/*        PrpLScheduleTaskVo scheduleTask = scheduleService.getScheduleTask(registVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
        ScheduleItemVo scheduleItem = new ScheduleItemVo();
        scheduleItem.setTaskId(scheduleTask.getId().toString());
        scheduleItem.setNodeType(scheduleTask.getScheduleType());
        scheduleItem.setItemNoName(registVo.getLicense());
        scheduleItem.setItemNo("1");
        scheduleItem.setDamageAddress(scheduleTask.getDamageAddress());
        scheduleItem.setNextHandlerCode(scheduleTask.getScheduledUsercode());
        scheduleItem.setNextHandlerName(scheduleTask.getScheduledUsername());
        scheduleItem.setScheduleObjectId(scheduleTask.getScheduledComcode());
        scheduleItem.setScheduleObjectName(scheduleTask.getScheduledComname());
        scheduleItems.add(scheduleItem);
        body.setScheduleItems(scheduleItems);*/
        
        try{
            scheduleItems = this.setReassignments(registInformationVo);
            logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????scheduleItems??????--------->{}",scheduleItems!=null?scheduleItems.size():0);
            body.setScheduleItems(scheduleItems);
        }
        catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Map<String, String> registRiskInfoMap = registRiskInfo(registVo.getRegistNo());
        //?????????vo
        List<PrpLCMainVo> prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(registVo.getRegistNo());
        List<PolicyInfoVo> policyInfoListVo = new ArrayList<PolicyInfoVo>();
        for(PrpLCMainVo vo : prpLCMainVos){
            if("12".equals(vo.getRiskCode().substring(0, 2))){
                policyInfoListVo = this.setPolicyInfo(policyInfoListVo, "B",vo);
            }else{
                policyInfoListVo = this.setPolicyInfo(policyInfoListVo, "C",vo);
            }
        }
        
        for(PolicyInfoVo vo : policyInfoListVo){
            if("??????".equals(vo.getClaimType())){
                if(StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))){
                    if(StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))){
                        vo.setDamageCount("0");
                    }else{
                        vo.setDamageCount(registRiskInfoMap.get("CI-DangerNum"));
                    }
                }
            }else{
                if(StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))){
                    if(StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))){
                        vo.setDamageCount("0");
                    }else{
                        vo.setDamageCount(registRiskInfoMap.get("BI-DangerNum"));
                    }
                }
            }
        }
        
        double claimSum = 0;
        int claimTimes = 0;
        //??????????????????
        for (PrpLCMainVo vo : prpLCMainVoList) {
            if ("12".equals(vo.getRiskCode().substring(0, 2))) {
                hisclaimInfo.setBusiPolicyNo(vo.getPolicyNo());
            } else {
                hisclaimInfo.setPolicyNo(vo.getPolicyNo());
            }

            // ?????????
            List<PrpLCompensateVo> compensateNewVoList = compensateTaskService.queryCompensateByOther(null, "N", vo.getPolicyNo(),"1");
            if (compensateNewVoList != null && compensateNewVoList.size() > 0) {
                for (PrpLCompensateVo compensateVo : compensateNewVoList) {
                    if ("1".equals(compensateVo.getUnderwriteFlag())) {
                        // claimSum =
                        // claimSum.add(compensateVo.getSumPaidAmt());
                        claimSum = claimSum + DataUtils.NullToZero(compensateVo.getSumPaidAmt()).doubleValue();
                        claimTimes = claimTimes + 1;
                    }
                }
            }

            // ?????????
            List<PrploldCompensateVo> oldcompensateVos = compensateTaskService
                    .findPrpoldCompensateBypolicyNo(vo.getPolicyNo());
            if (oldcompensateVos != null && oldcompensateVos.size() > 0) {
                for (PrploldCompensateVo oldVo : oldcompensateVos) {
                    if (oldVo.getEndcaseDate() != null) {
                        claimSum = claimSum + DataUtils.NullToZero(oldVo.getSumPaid()).doubleValue();
                        claimTimes = claimTimes + 1;
                    }
                }
            }
        }
        hisClaims = setHisClaimList(hisClaims,registVo,prpLCMainVoList);
        //Map<String, String> registRiskInfoMap = registRiskInfo(registVo.getRegistNo());
        int historicalAccident = 0;
        if (StringUtils.isNotEmpty(registRiskInfoMap.get("CI-No"))) {
            if (!StringUtils.isEmpty(registRiskInfoMap.get("CI-DangerNum"))) {
                historicalAccident = historicalAccident
                        + Integer.parseInt(registRiskInfoMap.get("CI-DangerNum"));
                for (PrpLCMainVo vo : prpLCMainVoList) {
                    if("1101".equals(vo.getRiskCode())){
                        historicalAccident = historicalAccident-1;
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(registRiskInfoMap.get("BI-No"))) {
            if (!StringUtils.isEmpty(registRiskInfoMap.get("BI-DangerNum"))) {
                historicalAccident = historicalAccident
                        + Integer.parseInt(registRiskInfoMap.get("BI-DangerNum"));
                for (PrpLCMainVo vo : prpLCMainVoList) {
                    if(!"1101".equals(vo.getRiskCode())){
                        historicalAccident = historicalAccident-1;
                    }
                }
            }
        }
        hisclaimInfo.setHistoriCalAccident(historicalAccident);
        hisclaimInfo.setHistoriCalClaimTimes(claimTimes);
        hisclaimInfo.setHistoriCalClaimSum(BigDecimal.valueOf(claimSum));
        hisclaimInfo.setHisClaims(hisClaims);
        body.setHisclaimInfo(hisclaimInfo);
        
        body.setPolicyInfos(policyInfoListVo);
        
    }
    
    /**
     * ??????????????????
     * <pre></pre>
     * @param hisClaimList
     * @param registVo
     * @param prpLCMainVoList
     * @return
     * @throws ParseException
     * @modified:
     * ???LinYi(2017???10???11??? ??????3:32:52): <br>
     */
    public List<HisClaimVo> setHisClaimList(
            List<HisClaimVo> hisClaimList,
            PrpLRegistVo registVo,
            List<PrpLCMainVo> prpLCMainVoList) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // ???????????????start
        List<PrpLClaimSummaryVo> prpLClaimSummaryVoList = new ArrayList<PrpLClaimSummaryVo>();

        List<PrpLRegistVo> oldPrpLRegistVoList = new ArrayList<PrpLRegistVo>();// ?????????
        List<PrploldCompensateVo> oldcompensateVos = new ArrayList<PrploldCompensateVo>();// ????????????????????????
        for (PrpLCMainVo vo : prpLCMainVoList) {
            List<PrpLClaimSummaryVo> claimSummaryVoList = claimSummaryService.findPrpLClaimSummaryVoList(vo.getPolicyNo(),vo.getRegistNo());
            if (claimSummaryVoList != null && claimSummaryVoList.size() > 0) {
                prpLClaimSummaryVoList.addAll(claimSummaryVoList);
            }

            List<PrpLRegistVo> registVoList = registQueryService.findOldRegistByPolicyNo(vo.getPolicyNo());
            if (registVoList != null && registVoList.size() > 0) {
                oldPrpLRegistVoList.addAll(registVoList);
            }
            List<PrploldCompensateVo> compensateVoList = compensateTaskService.findPrpoldCompensateBypolicyNo(vo.getPolicyNo());
            if (compensateVoList != null && compensateVoList.size() > 0) {
                oldcompensateVos.addAll(compensateVoList);
            }
        }
        Map<String, HisClaimVo> paymentAmount = new HashMap<String, HisClaimVo>();
        for (PrpLClaimSummaryVo vo : prpLClaimSummaryVoList) {
            BigDecimal newPaymentAmount = new BigDecimal(0);
            if (paymentAmount.containsKey(vo.getRegistNo())) {
                if (vo.getRealPay() != null) {
                    newPaymentAmount = newPaymentAmount.add(vo.getRealPay());
                }
                HisClaimVo hisClaimVo = paymentAmount.get(vo.getRegistNo());
                if (hisClaimVo.getCloseDate() != null && vo.getEndCaseTime() != null) {
                    try{
                        Date closeDate = formatter.parse(hisClaimVo.getCloseDate());
                        if (vo.getEndCaseTime().getTime() > closeDate.getTime()) {// ?????????????????????
                            PrpLCompensateVo prpLCompensateVo = compensateTaskService.searchCompByClaimNo(vo.getClaimNo());
                            if (prpLCompensateVo != null) {
                                if ("3".equals(prpLCompensateVo.getCaseType())) {
                                    hisClaimVo.setCaseType("??????");
                                } else {
                                    hisClaimVo.setCaseType("??????");
                                }
                            } else {
                                hisClaimVo.setCaseType("??????");
                            }
                            if (vo.getEndCaseTime() != null) {
                                String endCaseTime = formatter.format(vo.getEndCaseTime());
                                hisClaimVo.setCloseDate(endCaseTime);
                            }
                            if ("????????????".equals(hisClaimVo.getCaseState())) {
                                hisClaimVo.setCaseState("????????????");
                            } else if ("??????".equals(hisClaimVo.getCaseState())) {
                                if ("C".equals(vo.getCaseStatus())) {
                                    hisClaimVo.setCaseState("??????");
                                } else {
                                    hisClaimVo.setCaseState("????????????");
                                }
                            } else {
                                if ("E".equals(vo.getCaseStatus())) {
                                    hisClaimVo.setCaseState("??????");
                                } else {
                                    hisClaimVo.setCaseState("????????????");
                                }
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }                
                }
                if("12".equals(vo.getRiskCode().substring(0, 2))){
                    hisClaimVo.setBusiPolicyNo(vo.getPolicyNo());
                }else{
                    hisClaimVo.setPolicyNo(vo.getPolicyNo());
                }
                hisClaimVo.setPaymentAmount(newPaymentAmount.toString());
                paymentAmount.put(vo.getRegistNo(), hisClaimVo);
            } else {
                HisClaimVo hisclaim = new HisClaimVo();
                hisclaim.setRegistNo(vo.getRegistNo());
                hisclaim.setLicenseNo(vo.getLicenseNo());
                hisclaim.setDamageCode(vo.getDamageCode());
                String damageTime = formatter.format(vo.getDamageTime());
                hisclaim.setDamageDate(damageTime);
                hisclaim.setDamageAddress(vo.getDamageAddress());
                String reportTime = formatter.format(vo.getReportTime());
                if (vo.getEndCaseTime() != null) {
                    String endCaseTime = formatter.format(vo.getEndCaseTime());
                    hisclaim.setCloseDate(endCaseTime);
                }
                hisclaim.setReportTime(reportTime);

                if (vo.getRealPay() != null) {
                    hisclaim.setPaymentAmount(vo.getRealPay().toString());
                    newPaymentAmount.add(vo.getRealPay());
                }

                if ("N".equals(vo.getCaseStatus())) {
                    hisclaim.setCaseState("????????????");
                } else if ("C".equals(vo.getCaseStatus())) {
                    hisclaim.setCaseState("??????");
                } else if ("E".equals(vo.getCaseStatus())) {
                    hisclaim.setCaseState("??????");
                }
                PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registVo.getRegistNo());
                PrpLCompensateVo prpLCompensateVo = compensateTaskService.searchCompByClaimNo(vo.getClaimNo());
                if (prpLCompensateVo != null) {
                    if ("3".equals(prpLCompensateVo.getCaseType())) {
                        hisclaim.setCaseType("??????");
                    } else {
                        hisclaim.setCaseType("??????");
                    }
                } else {
                    hisclaim.setCaseType("??????");
                }
                if(StringUtils.isNotBlank(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart())){
                    hisclaim.setCollisionSite(prpLRegistVo.getPrpLRegistCarLosses().get(0).getLosspart());
                }else{
                    hisclaim.setCollisionSite("");
                }
               

                // ???????????????
                List<PrpLCompensateVo> prpLCompensateListVo = compensateTaskService.findCompListByClaimNo(vo.getClaimNo(), "N");
                List<AccountVo> accountList = new ArrayList<AccountVo>();
                for (PrpLCompensateVo compensateVo : prpLCompensateListVo) {
                    for (PrpLPaymentVo lPaymentVo : compensateVo.getPrpLPayments()) {
                        PrpLPayCustomVo prpLPayCustomVo = payCustomService.findPayCustomVoById(lPaymentVo.getPayeeId());
                        AccountVo account = new AccountVo();
                        account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
                        account.setName(prpLPayCustomVo.getPayeeName());
                        account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
                        account.setBankName(prpLPayCustomVo.getBankOutlets());
                        account.setAccountName(prpLPayCustomVo.getPayeeName());
                        account.setAccountNo(prpLPayCustomVo.getAccountNo());
                        account.setPhone(prpLPayCustomVo.getPayeeMobile());
                        accountList.add(account);
                    }

                    hisclaim.setAccounts(accountList);
                }

                if("12".equals(vo.getRiskCode().substring(0, 2))){
                    hisclaim.setBusiPolicyNo(vo.getPolicyNo());
                }else{
                    hisclaim.setPolicyNo(vo.getPolicyNo());
                }
                paymentAmount.put(vo.getRegistNo(), hisclaim);
            }
        }
        // ???map???????????????
        for (HisClaimVo vo : paymentAmount.values()) {
            hisClaimList.add(vo);
        }
        // ???????????????end

        // ?????????start
        for (PrpLRegistVo oldPrpLRegistVo : oldPrpLRegistVoList) {
            HisClaimVo hisclaim = new HisClaimVo();
            hisclaim.setRegistNo(oldPrpLRegistVo.getRegistNo());
            hisclaim.setLicenseNo(oldPrpLRegistVo.getLicense());
            hisclaim.setDamageCode(oldPrpLRegistVo.getDamageCode());
            String damageTime = formatter.format(oldPrpLRegistVo.getDamageTime());
            hisclaim.setDamageDate(damageTime);
            hisclaim.setDamageAddress(oldPrpLRegistVo.getDamageAddress());
            String reportTime = formatter.format(oldPrpLRegistVo.getReportTime());

            // ?????????
            List<PrploldCompensateVo> oldcompensateVoList = new ArrayList<PrploldCompensateVo>();
            List<PrpLRegistVo> oldPrpLRegistRPolicyList = registQueryService.findOldPrpLRegistRPolicy(oldPrpLRegistVo.getRegistNo());
            for (PrpLRegistVo prpLRegistRPolicy : oldPrpLRegistRPolicyList) {
                List<PrploldCompensateVo> compensateVoList = new ArrayList<PrploldCompensateVo>();
                compensateVoList = compensateTaskService.findPrpoldCompensateBypolicyNo(prpLRegistRPolicy.getPolicyNo());
                if (compensateVoList != null && compensateVoList.size() > 0) {
                    oldcompensateVoList.addAll(compensateVoList);
                }
                
                //????????????
                if("12".equals(prpLRegistRPolicy.getRiskCode().substring(0, 2))){
                    hisclaim.setBusiPolicyNo(prpLRegistRPolicy.getPolicyNo());
                }else{
                    hisclaim.setPolicyNo(prpLRegistRPolicy.getPolicyNo());
                }
            }
            double oldPaymentAmount = 0;
            if (oldcompensateVoList != null && oldcompensateVoList.size() > 0) {
                for (PrploldCompensateVo oldVo : oldcompensateVoList) {
                    if (oldVo.getEndcaseDate() != null) {
                        String endCaseTime = formatter.format(oldVo.getEndcaseDate());
                        hisclaim.setCloseDate(endCaseTime);
                    }
                    if (oldVo.getSumthisPaid() != null) {
                        oldPaymentAmount = oldPaymentAmount
                                + DataUtils.NullToZero(oldVo.getSumthisPaid()).doubleValue();
                    }
                }
            }
            hisclaim.setPaymentAmount(String.valueOf(oldPaymentAmount));
            List<PrpLClaimVo> prpLClaimVoList = registQueryService
                    .findOldPrpLClaimVo(oldPrpLRegistVo.getRegistNo());
            if (prpLClaimVoList != null && prpLClaimVoList.size() > 0) {
                if (prpLClaimVoList.get(0).getEndCaseTime() != null) {
                    String endCaseTime = formatter.format(prpLClaimVoList.get(0).getEndCaseTime());
                    hisclaim.setCloseDate(endCaseTime);
                } else {
                    hisclaim.setCloseDate("");
                }
                if (prpLClaimVoList.size() == 2) {
                    if (prpLClaimVoList.get(0).getEndCaseTime() != null
                            && prpLClaimVoList.get(1).getEndCaseTime() != null) {
                        hisclaim.setCaseState("??????");
                    } else if (prpLClaimVoList.get(0).getCancelTime() != null
                            && prpLClaimVoList.get(1).getCancelTime() != null) {
                        hisclaim.setCaseState("??????");
                    } else {
                        hisclaim.setCaseState("????????????");
                    }
                } else {
                    if (prpLClaimVoList.get(0).getEndCaseTime() != null) {
                        hisclaim.setCaseState("??????");
                    } else if (prpLClaimVoList.get(0).getCancelTime() != null) {
                        hisclaim.setCaseState("??????");
                    } else {
                        hisclaim.setCaseState("????????????");
                    }
                }

            } else {
                hisclaim.setCaseState("????????????");
            }

            hisclaim.setReportTime(reportTime);
            if ("L".equals(oldPrpLRegistVo.getFlag())) {
                hisclaim.setCaseType("??????");
            } else {
                hisclaim.setCaseType("??????");
            }

            // ????????????
            List<PrpLRegistCarLossVo> registCarLossVoList = registQueryService.findOldPrpLthirdCarLoss(oldPrpLRegistVo.getRegistNo(), "1");
            if (registCarLossVoList != null && registCarLossVoList.size() > 0) {
                if (StringUtils.isNotBlank(registCarLossVoList.get(0).getLosspart())) {
                    if (registCarLossVoList.get(0).getLosspart().length() > 1) {
                        if (!"10".equals(registCarLossVoList.get(0).getLosspart())) {
                            hisclaim.setCollisionSite(registCarLossVoList.get(0).getLosspart().substring(1, 2));
                        } else {
                            hisclaim.setCollisionSite(registCarLossVoList.get(0).getLosspart());
                        }
                    }
                }
            }
            // ???????????????

            List<AccountVo> accountList = new ArrayList<AccountVo>();
            for (PrploldCompensateVo vo1 : oldcompensateVos) {
                List<PrpLPayCustomVo> payCustomVoList = registQueryService.findOldPrpjlinkaccountByCertiNo(vo1.getCompensateNo());
                for (PrpLPayCustomVo payCustomVo : payCustomVoList) {
                    List<PrpLPayCustomVo> prpLPayCustomVoList = registQueryService.findOldAccountByAccountNo(payCustomVo.getAccountNo());
                    for (PrpLPayCustomVo prpLPayCustomVo : prpLPayCustomVoList) {
                        AccountVo account = new AccountVo();
                        account.setPayeeType(prpLPayCustomVo.getPayObjectKind());
                        account.setName(prpLPayCustomVo.getPayeeName());
                        account.setIdentifyNumber(prpLPayCustomVo.getCertifyNo());
                        account.setBankName(prpLPayCustomVo.getBankName());
                        account.setAccountName(prpLPayCustomVo.getPayeeName());
                        account.setAccountNo(prpLPayCustomVo.getAccountNo());
                        account.setPhone(prpLPayCustomVo.getPayeeMobile());
                        accountList.add(account);
                    }
                }
            }
            hisclaim.setAccounts(accountList);
            hisClaimList.add(hisclaim);
        }
        return hisClaimList;
    }
    
    public Map<String, String> registRiskInfo(String registNo){
        Map<String, String> registRiskInfoMap = new HashMap<String, String>();
        if (!StringUtils.isEmpty(registNo)) {
            registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
        }
        return registRiskInfoMap;
    }
    
    /**
     * ?????????????????????????????????
     * <pre></pre>
     * @param registVo
     * @param reqVo
     * @param resVo
     * @param url
     * @modified:
     * ???LinYi(2017???10???12??? ??????8:59:13): <br>
     */
    private void saveCarchildInterfaceLog(SysUserVo userVo,PrpLRegistVo registVo,RegistInfoReqVo reqVo,RegistInfoResVo resVo,String url) throws Exception{     
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        String requestXml = "";
        String responseXml = "";
        try{
            String requestType = reqVo.getHead().getRequestType();
            if(requestType.contains("CT")){
                logVo.setBusinessType(BusinessType.CT_Regist.name());
                logVo.setBusinessName(BusinessType.CT_Regist.getName());
            }else if (requestType.contains("MTA")) {
                logVo.setBusinessType(BusinessType.MTA_Regist.name());
                logVo.setBusinessName(BusinessType.MTA_Regist.getName());
            }
            
            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null,"class");// ?????? class??????
            requestXml = stream.toXML(reqVo);
            responseXml = stream.toXML(resVo);
            CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
            if("1".equals(carChildResponseHead.getErrNo()+"")){
                logVo.setStatus("1");
                logVo.setErrorCode("true");
            }else {
                logVo.setStatus("0");
                logVo.setErrorCode("false");
            }
            logVo.setErrorMessage(resVo.getHead().getErrMsg());
        }
        catch(Exception e){
            logVo.setStatus("0");
            logVo.setErrorCode("false");
            e.printStackTrace();
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
    public void saveScheduleInformation(PrplCarchildScheduleVo prplCarchildScheduleVo) throws Exception {
        PrplCarchildSchedule prplCarchildSchedule = null;
        if(prplCarchildScheduleVo!=null){
            prplCarchildSchedule = new PrplCarchildSchedule();
            prplCarchildSchedule = Beans.copyDepth().from(prplCarchildScheduleVo).to(PrplCarchildSchedule.class);
        }
        databaseDao.save(PrplCarchildSchedule.class,prplCarchildSchedule);
    }

    @Override
    public RevokeInfoResVo sendRevokeInformation(RevokeInfoReqVo reqVo,String url) throws Exception {
        String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
        String xmlReturn = "";
        if("CT_006".equals(reqVo.getHead().getRequestType())){
        	 url=SpringProperties.getProperty("CT_URL")+CT_02;
             logger.info("??????????????????send---------------------------"+xmlToSend);
             xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_02);
             //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
             logger.info("??????????????????return---------------------------"+xmlReturn);
        }else{
            logger.info("??????????????????send---------------------------"+xmlToSend);
            xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,reqVo.getHead().getRequestType());
            logger.info("??????????????????return---------------------------"+xmlReturn);
        }
        RevokeInfoResVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RevokeInfoResVo.class);
        return resVo;
    }

	@Override
	public void savePrplcarchildregistcancle(PrplcarchildregistcancleVo prplcarchildregistcancleVo) {
		Prplcarchildregistcancle po=new Prplcarchildregistcancle();
		Beans.copy().from(prplcarchildregistcancleVo).to(po);
		databaseDao.save(Prplcarchildregistcancle.class,po);
		
	}

	@Override
	public PrplcarchildregistcancleVo findPrplcarchildregistcancleVoById(Long id) {
		PrplcarchildregistcancleVo vo=new PrplcarchildregistcancleVo();
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("id",id);
		Prplcarchildregistcancle po= databaseDao.findUnique(Prplcarchildregistcancle.class,rule);
		if(po!=null){
			Beans.copy().from(po).to(vo);
		}
		return vo;
	}

	@Override
	public void updatePrplcarchildregistcancle(PrplcarchildregistcancleVo prplcarchildregistcancleVo) {
		Prplcarchildregistcancle po=new Prplcarchildregistcancle();
		if(prplcarchildregistcancleVo!=null){
			Beans.copy().from(prplcarchildregistcancleVo).to(po);
			databaseDao.update(Prplcarchildregistcancle.class,po);
		}
		
		
	}
	@Override
	public PrplcarchildregistcancleVo findPrplcarchildregistcancleVoByRegistNo(String registNo) {
		PrplcarchildregistcancleVo vo=null;
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("registNo",registNo);
		rule.addEqual("status","0");
		rule.addDescOrder("createTime");
		 List<Prplcarchildregistcancle> listpo=databaseDao.findAll(Prplcarchildregistcancle.class,rule);
		 if(listpo!=null && listpo.size()>0){
			 vo=new PrplcarchildregistcancleVo();
			 Prplcarchildregistcancle po=listpo.get(0);
			 Beans.copy().from(po).to(vo);
		 }
		return vo;
	}

    @Override
    public RevokeRestoreInfoResVo sendRevokeRestoreInformation(RevokeRestoreInfoReqVo reqVo,String url) throws Exception {
/*        String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
        
        url = url+"?xml=";
        
        logger.info("????????????????????????send---------------------------"+xmlToSend);
        String requestType = reqVo.getHead().getRequestType();
        String sourceType = requestType.substring(0,requestType.indexOf("_"));
        String xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
        logger.info("????????????????????????return---------------------------"+xmlReturn);
        
        RevokeRestoreInfoResVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RevokeRestoreInfoResVo.class);
        
        return resVo;*/

        String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
        String xmlReturn = "";
        if("CT_007".equals(reqVo.getHead().getRequestType())){
             url=SpringProperties.getProperty("CT_URL")+CT_03;
             logger.info("????????????????????????send---------------------------"+xmlToSend);
             xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_03);
             //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
             logger.info("????????????????????????return---------------------------"+xmlReturn);
        }else{
            logger.info("????????????????????????send---------------------------"+xmlToSend);
            xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,reqVo.getHead().getRequestType());
            logger.info("????????????????????????return---------------------------"+xmlReturn);
        }
        RevokeRestoreInfoResVo resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RevokeRestoreInfoResVo.class);
        return resVo;
    
    }

    @Override
    public List<PrplCarchildScheduleVo> findCarchildScheduleByRegistNo(String registNo) {
        List<PrplCarchildScheduleVo> prplCarchildScheduleVos = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addDescOrder("timeStamp");
        List<PrplCarchildSchedule> prplCarchildSchedules = databaseDao.findAll(PrplCarchildSchedule.class,queryRule);
        if(prplCarchildSchedules!=null&&prplCarchildSchedules.size()>0){
            prplCarchildScheduleVos = new ArrayList<PrplCarchildScheduleVo>();
            prplCarchildScheduleVos = Beans.copyDepth().from(prplCarchildSchedules).toList(PrplCarchildScheduleVo.class);
        }
        return prplCarchildScheduleVos;
    }


	@Override
	public ResultPage<PrplcarchildregistcancleVo> search(PrpLWfTaskQueryVo prplWfTaskQueryVo, int start, int length,
			String handleStatus) throws Exception {
		SqlJoinUtils sqlUtil=new SqlJoinUtils();
		sqlUtil.append(" from Prplcarchildregistcancle a where 1=1 ");
		//????????????
		if(StringUtils.isNotBlank(handleStatus)){
			if("0".equals(handleStatus)){
				sqlUtil.append(" and a.status= ? ");
				sqlUtil.addParamValue("0");
				
			}else{
				sqlUtil.append(" and a.status= ? ");
				sqlUtil.addParamValue("1");
			}
		}
		//?????????
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getRegistNo())){
		    sqlUtil.append(" and a.registNo like ? ");
		    sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getRegistNo()+"%");
		  
			
		}
		//?????????
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getPolicyNo())){
			sqlUtil.append(" and (a.bipolicyNo like ? or a.cipolicyNo like ? )");
			sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getPolicyNo()+"%");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getPolicyNo());
			}
		//????????????
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getInsuredName())){
			sqlUtil.append(" and a.flagLog= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getInsuredName());
			}
		
		//????????????
		if(prplWfTaskQueryVo.getTaskInTimeStart()!=null && prplWfTaskQueryVo.getTaskInTimeEnd()!=null){
			sqlUtil.append(" and a.cancleDate >= ? and a.cancleDate <= ? ");
			sqlUtil.addParamValue(prplWfTaskQueryVo.getTaskInTimeStart());
			sqlUtil.addParamValue(DateUtils.toDateEnd(prplWfTaskQueryVo.getTaskInTimeEnd()));
			}
		//?????????
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getReporterName())){
			sqlUtil.append("AND a.userCode=? ");
		    sqlUtil.addParamValue(prplWfTaskQueryVo.getReporterName());
		}
		//?????????
		if(StringUtils.isNotBlank(prplWfTaskQueryVo.getLicenseNo())){
			sqlUtil.append("AND a.licenseNo like ? ");
			 sqlUtil.addParamValue("%"+prplWfTaskQueryVo.getLicenseNo()+"%");
		}
		
		 //??????
		sqlUtil.append(" Order By a.createTime desc");
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();
		logger.info(sql);
		Page<Object[]> page = databaseDao.findPageByHql(sql,start/length+1,length,values);
		List<PrplcarchildregistcancleVo> cancleVoList = new ArrayList<PrplcarchildregistcancleVo>();
		for(int i=0;i<page.getResult().size();i++){
			Object obj = page.getResult().get(i);
			PrplcarchildregistcancleVo cancleVo=new PrplcarchildregistcancleVo();
			Prplcarchildregistcancle  canclepo=(Prplcarchildregistcancle)obj;
			Beans.copy().from(canclepo).to(cancleVo);
			if("0".equals(cancleVo.getExamineRusult())){
				cancleVo.setExamineRusult("???????????????");
			}else{
				cancleVo.setExamineRusult("????????????");
			}

			//???????????????????????????????????????????????????????????????
            PrpLRegistVo registVo = registService.findRegistByRegistNo(canclepo.getRegistNo());
            if(registVo != null && CodeConstants.RegistTaskFlag.CANCELED.equals(registVo.getRegistTaskFlag())){// ????????????
                if("0".equals(handleStatus)){
                    continue;
                }
            }
			cancleVoList.add(cancleVo);
		}
		ResultPage<PrplcarchildregistcancleVo> resultPage = new ResultPage<PrplcarchildregistcancleVo>(start, length, page.getTotalCount(), cancleVoList);
		return resultPage;
	
	} 

	@Override
	public String sendCaseInfoToCarchild(String registNo, String sign,SysUserVo userVo) {
		CaseCancleNopassReqVo reqVo=new CaseCancleNopassReqVo();
		CarchildReqHeadVo headVo=new CarchildReqHeadVo();
		CaseCancleNopassReqBodyVo bodyVo=new CaseCancleNopassReqBodyVo();
		CarchildResHeadVo resheadVo=new CarchildResHeadVo();
		CaseCancleNopassResVo resVo=new CaseCancleNopassResVo();
		String url="";//????????????
		try{
			params(headVo,bodyVo,userVo,registNo,sign);
			reqVo.setHead(headVo);
			reqVo.setBody(bodyVo);
			String xmlToSend = ClaimBaseCoder.objToXmlUtf(reqVo);
			logger.info("?????????Xml------------------"+xmlToSend);
			if(sign.contains("CT")){
				url=SpringProperties.getProperty("CT_URL")+CT_04;
	             logger.info("???????????????????????????send---------------------------"+xmlToSend);
	            String xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_04);
	             //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
	             logger.info("???????????????????????????return---------------------------"+xmlReturn);
				
					resVo= ClaimBaseCoder.xmlToObj(xmlReturn,CaseCancleNopassResVo.class);
					resheadVo=resVo.getHead();
				
				
				
			}else{
				    
				    url=SpringProperties.getProperty("MTA_URL_CANCELFAILED");//????????????MTA_CASENOPASS_URL
				   
				    String responseXml=requestPlatform(xmlToSend,url,200,sign);
				    logger.info("??????????????????Xml------------------"+responseXml);
				    resVo= ClaimBaseCoder.xmlToObj(responseXml,CaseCancleNopassResVo.class);
				    resheadVo=resVo.getHead();
				   
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("????????????"+e.getMessage());
		}finally{
			ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
	        logVo.setRegistNo(registNo);
	        if(sign.contains("CT")){
	        	logVo.setServiceType("CT"); //????????????--?????????
	        }else{
	        	logVo.setServiceType("MTA"); //????????????--?????????
	        }
	        logVo.setCreateUser(userVo.getUserCode());
	        logVo.setComCode(userVo.getComCode());
	        logVo.setRequestUrl(url);
	        this.logUtil(reqVo,resVo,logVo,sign);
		}
		if(StringUtils.isNotBlank(resheadVo.getErrNo()) && "1".equals(resheadVo.getErrNo())){
            List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
            //List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
            List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
            if(checkVolist != null && checkVolist.size() > 0){
                volist.addAll(checkVolist);
            }
           /* if(dLossVolist != null && dLossVolist.size() > 0){
                volist.addAll(dLossVolist);
            }*/
            List<PrpLWfTaskVo> prpLWfTaskVoList = new ArrayList<PrpLWfTaskVo>();
            if(volist != null && volist.size() > 0){
                for(PrpLWfTaskVo vo : volist){
                    if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                        if(sign.contains("CT")){
                            vo.setIsMobileAccept("2");
                        }else{
                            vo.setIsMobileAccept("3");
                        }
                        prpLWfTaskVoList.add(vo);
                    }
                }
            }
            try{
                wfTaskHandleService.updateIsMobileCaseByFlowId(prpLWfTaskVoList);
            }
            catch(Exception e){
                e.printStackTrace();
            }
		}
	    
		return resheadVo.getErrNo();
	}
	/**
	 * ?????????????????????
	 * @param headVo
	 * @param bodyVo
	 * @param userVo
	 * @param registNo
	 * @param sign
	 */
  private void params(CarchildReqHeadVo headVo,CaseCancleNopassReqBodyVo bodyVo,SysUserVo userVo,String registNo,String sign)throws Exception{
	  if(sign.contains("CT")){
		  headVo.setRequestType("CT_009");
	  }else{
		  headVo.setRequestType("MTA_009");
	  }
	  headVo.setUser("claim_user");
	  headVo.setPassWord("claim_psd");
	  bodyVo.setRegistNo(registNo);
	  bodyVo.setReviewDate(DateFormatString(new Date()));
	  bodyVo.setTimestamp(DateFormatString(new Date()));
	  bodyVo.setUsercode(userVo.getUserCode());
	 
  }
  
  /**
	 * ??????????????????
	 * Date ???????????? String??????
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private String DateFormatString(Date strDate) throws ParseException{
		String str="";
		SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		  if(strDate!=null){
			  str=format.format(strDate);
		}
		return str;
	}
	
	/**
	 * ??????????????????
	 * @param requestXML
	 * @param urlStr
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	private String requestPlatform(String requestXML,String urlStr,int seconds,String sign) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml="";
		  StringBuffer buffer = new StringBuffer();    
	        try {    
	         
	            URL url = new URL(urlStr);
	            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
	            httpUrlConn.setDoOutput(true);    
	            httpUrlConn.setDoInput(true);    
	            httpUrlConn.setUseCaches(false);    
	            // ?????????????????????GET/POST???    
	            httpUrlConn.setRequestMethod("POST"); 
	            httpUrlConn.setConnectTimeout(seconds * 1000);
		        httpUrlConn.setRequestProperty("Content-type","application/xml;charset=UTF-8");
		        if(sign.contains("MTA")){
		        	httpUrlConn.setRequestProperty("X-User-Mobile","18800001111");
		            httpUrlConn.setRequestProperty("X-User-Token","123456");
		        }
	            
	            httpUrlConn.connect();    
	    
	            String outputStr =requestXML;
	            			
	            OutputStream outputStream = httpUrlConn.getOutputStream();  		        
	            // ???????????????????????????    
	            if (null != outputStr) {    
	                // ???????????????????????????????????????    outputStream.write
	                outputStream.write(outputStr.getBytes("utf-8"));    
	            }    
	    
	            // ???????????????????????????????????????    
	            InputStream inputStream = httpUrlConn.getInputStream();    
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
	            
	            String str = null;    
	            while ((str = bufferedReader.readLine()) != null) {
	            	
	                buffer.append(str);    
	            } 
	            if (buffer.length() < 1) {
					throw new Exception("??????????????????");
				}
	            bufferedReader.close();    
	            inputStreamReader.close();    
	            // ????????????  
	            outputStream.flush();
	            outputStream.close();
	            inputStream.close();    
	            inputStream = null;    
	            httpUrlConn.disconnect(); 
	            responseXml=buffer.toString();
	            
	        } catch (ConnectException ce) {
	        	throw new Exception("??????????????????????????????", ce);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	throw new Exception("????????????????????????", e);
	        	
	        } finally {
				logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
			}    
	        return responseXml;
	}
	
	/**
	 * ??????????????????????????????
	 * @param reqObj
	 * @param resObj
	 * @param logVo
	 * @param flag
	 * @param errorMsg
	 */
	private  void logUtil(CaseCancleNopassReqVo reqObj,CaseCancleNopassResVo resObj,ClaimInterfaceLogVo logVo,String sign) {
	        try{
	        	CarchildResHeadVo headVo=new CarchildResHeadVo();
	        	headVo=resObj.getHead();
	            String reqXml = ClaimBaseCoder.objToXmlUtf(reqObj);
	            String resXml = ClaimBaseCoder.objToXmlUtf(resObj);
	            logger.info("===============?????????????????????===========");
	            logger.info(reqXml);
	            logger.info(resXml);
	            if(sign.contains("CT")){
	            	logVo.setBusinessType(BusinessType.CT_caseCancleNoPass.name());
	 	            logVo.setBusinessName(BusinessType.CT_caseCancleNoPass.getName());
	            }else{
	            	logVo.setBusinessType(BusinessType.MTA_caseCancleNoPass.name());
	 	            logVo.setBusinessName(BusinessType.MTA_caseCancleNoPass.getName());
	            }
	           
	            logVo.setRequestXml(reqXml);
	            logVo.setResponseXml(resXml);
	            logVo.setCreateTime(new Date());
	            logVo.setRequestTime(new Date());
	            if("1".equals(headVo.getErrNo())){
	                logVo.setStatus("1");
	            }else{
	                logVo.setStatus("0");
	                logVo.setErrorMessage(headVo.getErrMsg());
	                
	            }
	            

	        }catch(Exception e1){
	            e1.printStackTrace();
	        }finally{
	            if(interfaceLogService == null){
	                interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
	            }
	            interfaceLogService.save(logVo);
	        }
	 }
	
	
	public List<PolicyInfoVo> setPolicyInfo(List<PolicyInfoVo> policyInfoListVo,String flags,PrpLCMainVo prpLCMainVo){

	    PolicyInfoVo policyInfoVo = new PolicyInfoVo();
        List<KindInfoVo> kindInfoListVo = new ArrayList<KindInfoVo>();
        List<EngageInfoVo> engageInfoListVo = new ArrayList<EngageInfoVo>();
        List<EndorInfoVo> endorInfoListVo = new ArrayList<EndorInfoVo>();
        /*PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
        if("B".equals(flags)){
            prpLCMainVo = PrpLCMainService.findPrpLCMain(policyInfoReqBodyVo.getRegistNo(), policyInfoReqBodyVo.getBusiPolicyNo());
        }else{
            prpLCMainVo = PrpLCMainService.findPrpLCMain(policyInfoReqBodyVo.getRegistNo(), policyInfoReqBodyVo.getPolicyNo());
        }*/
        //policyInfoResBodyVo.setRegistNo(prpLCMainVo.getRegistNo());
        policyInfoVo.setRegistNo(prpLCMainVo.getRegistNo());
        policyInfoVo.setPolicyNo(prpLCMainVo.getPolicyNo());
        policyInfoVo.setLicenseNo(prpLCMainVo.getPrpCItemCars().get(0).getLicenseNo());
        policyInfoVo.setFrameNo(prpLCMainVo.getPrpCItemCars().get(0).getFrameNo());
        policyInfoVo.setEngineNo(prpLCMainVo.getPrpCItemCars().get(0).getEngineNo());
        policyInfoVo.setModelCode(prpLCMainVo.getPrpCItemCars().get(0).getBrandName());
        policyInfoVo.setInsuredName(prpLCMainVo.getInsuredName());
        policyInfoVo.setComCode(prpLCMainVo.getComCode());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = formatter.format(prpLCMainVo.getStartDate());
        String endDate = formatter.format(prpLCMainVo.getEndDate());
        policyInfoVo.setStartDate(startDate);
        policyInfoVo.setEndDate(endDate);
        if("B".equals(flags)){
            policyInfoVo.setClaimType("??????");
        }else{
            policyInfoVo.setClaimType("??????");
        }
        //policyInfoVo.setCustomerFlag("");????????????
        policyInfoVo.setIsVip("1");
        //policyInfoVo.setDamageCount(damageCount);??????????????????
        //????????????
        //List<PrpLCItemKindVo> cIemKindVoList = policyViewService.findItemKinds(policyInfoReqBodyVo.getRegistNo(), null);
        List<PrpLCItemKindVo> cIemKindVoList = new ArrayList<PrpLCItemKindVo>();
        if("B".equals(flags)){
            if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
                cIemKindVoList = prpLCMainVo.getPrpCItemKinds();
            }
            for(PrpLCItemKindVo vo :cIemKindVoList){
                KindInfoVo kindInfoVo = new KindInfoVo();
                kindInfoVo.setAmount(vo.getAmount());
                kindInfoVo.setKindName(vo.getKindName());
                kindInfoVo.setKindCode(vo.getKindCode());
//              if("1".equals(vo.getNoDutyFlag())){
//                  kindInfoVo.setIsDeduct("0");//???
//              }else{
//                  kindInfoVo.setIsDeduct("1");
//              }
                if(!"1101".equals(vo.getRiskCode())&&!CodeConstants.ISNEWCLAUSECODE_MAP.get(vo.getRiskCode())&&vo.getFlag().length() > 4
                        && String.valueOf(vo.getFlag().charAt(4)).equals("1")) {
                    kindInfoVo.setIsDeduct("0");//???
                }else if(StringUtils.isNotBlank(vo.getKindCode())&& vo.getKindCode().substring(vo.getKindCode().
                        length() - 1,vo.getKindCode().length()).equals("M")) {
                    kindInfoVo.setIsDeduct("0");//???
                }else{
                    kindInfoVo.setIsDeduct("1");
                }
                kindInfoVo.setDeduct(vo.getDeductible()!=null ? vo.getDeductible().toString():"0");
                kindInfoListVo.add(kindInfoVo);
            }
        }else{
            if(prpLCMainVo.getPrpCItemKinds()!=null && prpLCMainVo.getPrpCItemKinds().size()>0){
                cIemKindVoList.add(prpLCMainVo.getPrpCItemKinds().get(0));
            }
            for(PrpLCItemKindVo vo :cIemKindVoList){
                KindInfoVo kindInfoVo = new KindInfoVo();
                kindInfoVo.setAmount(vo.getAmount());
                kindInfoVo.setKindName(vo.getKindName());
                kindInfoVo.setIsDeduct("1");//????????????????????????
                kindInfoVo.setDeduct(vo.getDeductible()!=null ? vo.getDeductible().toString():"0");
                kindInfoVo.setKindCode(vo.getKindCode());
                kindInfoListVo.add(kindInfoVo);
            }
        }
        policyInfoVo.setKindInfos(kindInfoListVo);
        
        //?????????????????????
        List <PrpLCengageVo> prpLCengageVo_B = null;
        //????????????
        prpLCengageVo_B = prpLCMainVo.getPrpCengages();
        Map<String,String> prpLCengageVoMap_B = new TreeMap<String,String>();
        if(prpLCengageVo_B != null && prpLCengageVo_B.size() > 0){
            String name ="";
            for(PrpLCengageVo prpLCengageVo:prpLCengageVo_B){
              String code = prpLCengageVo.getClauseCode();
             // String name = prpLCengageVo.getClauseName();
              String clauses = prpLCengageVo.getClauses();
              String codeName = code;
              
              if("0".equals(prpLCengageVo.getTitleFlag())){
                     name = prpLCengageVo.getClauses();
                }
              if(StringUtils.isNotBlank(name)){
                 codeName = codeName + "--" + name;
              }
              //EngageInfoListVo engageInfoVo = new EngageInfoListVo();
              if(!"0".equals(prpLCengageVo.getTitleFlag())){
                     if(prpLCengageVoMap_B.containsKey(codeName)){
                         String clause = prpLCengageVoMap_B.get(codeName);
                         prpLCengageVoMap_B.put(codeName,clause+clauses);
                         /*engageInfoVo.setClauses(clause+clauses);
                         engageInfoVo.setClausesName(codeName);
                         engageInfoListVo.add(engageInfoVo);*/
                     }else{
                         prpLCengageVoMap_B.put(codeName,clauses);
                         /*engageInfoVo.setClauses(clauses);
                         engageInfoVo.setClausesName(codeName);
                         engageInfoListVo.add(engageInfoVo);*/
                     }
              }
            }
        }
        for(Iterator iter = prpLCengageVoMap_B.entrySet().iterator();iter.hasNext();){
            Map.Entry entry = (Entry) iter.next();
            EngageInfoVo engageInfoVo = new EngageInfoVo();
            engageInfoVo.setClauses(entry.getValue().toString());
            engageInfoVo.setClausesName((String) entry.getKey());
            engageInfoListVo.add(engageInfoVo);
        }
        
        policyInfoVo.setEngageInfos(engageInfoListVo);
        
        //????????????
        List<PrppheadVo> vos = new ArrayList<PrppheadVo>();
        vos = registQueryService.findByPolicyNo(prpLCMainVo.getPolicyNo());
        for(PrppheadVo vo:vos){
            EndorInfoVo endorInfoVo = new EndorInfoVo();
            endorInfoVo.setEndorNo(vo.getEndorseNo());
            String endorDate = formatter.format(vo.getEndorDate());
            String validDate = formatter.format(vo.getValidDate());
            endorInfoVo.setEndorDate(endorDate);
            endorInfoVo.setEndorVerifiedDate(validDate);
            List<PrpptextVo> prppTextVoList =registQueryService.findPrppTextByPolicyNo(vo.getEndorseNo());
            String endorContent = "";
            for(PrpptextVo prpptextVo :prppTextVoList){
                if(StringUtils.isNotBlank(prpptextVo.getEndorseText())){
                    endorContent = endorContent+prpptextVo.getEndorseText();
                }
            }
            endorInfoVo.setEndorContent(endorContent.toString());
            List<PrppMainVo> prppMainVoList = registQueryService.findprppMainByPolicyNo(vo.getEndorseNo());
            if(prppMainVoList!=null && prppMainVoList.size()>0){
                endorInfoVo.setAmountChange(prppMainVoList.get(0).getChgAmount().toString());
            }
            //endorInfoVo.setAmountChange("");
            /*endorInfoVo.setRiskChange("");*/
            endorInfoListVo.add(endorInfoVo);
        }
        policyInfoVo.setEndorInfos(endorInfoListVo);
        
        policyInfoListVo.add(policyInfoVo);
        
        return policyInfoListVo;
    
 }
    
    
    //??????????????????/?????????????????????????????????????????????????????????????????????????????????
    public List<ScheduleItemVo> setReassignments(RegistInformationVo registInformationVo) throws Exception {
        String schType = registInformationVo.getSchType();
        PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
        logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????setReassignments--------->{}",schType);
        if("Check".equals(schType)){//??????
            PrpLScheduleTaskVo prpLScheduleTaskVo = registInformationVo.getPrpLScheduleTaskVo();
            PrpLScheduleTaskVo scheduleTask = scheduleService.findScheduleTaskByOther(prpLScheduleTaskVo.getRegistNo(),"1","1");
            List<PrpLScheduleItemsVo> prpLScheduleItemses = registInformationVo.getScheduleItemVos();
            logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????prpLScheduleItemses?????????--------->{}",prpLScheduleItemses!=null?prpLScheduleItemses.size():0);
            //??????id
            int id = 1;
         /*   String scheduleTaskId = "1";
            List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLScheduleTaskVo.getRegistNo(), FlowNode.Chk.toString());
            if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
                //?????????????????????
                Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                @Override
                public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                        return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                    }
                });
                scheduleTaskId=prpLWfTaskVoList.get(0).getTaskId().toString();
            }*/
            String scheduleTaskId = scheduleTask.getId().toString();
            List<ScheduleItemVo> scheduleItemList = new ArrayList<ScheduleItemVo>();
            for(PrpLScheduleItemsVo ItemsVo :prpLScheduleItemses){
                ScheduleItemVo scheduleItem = new ScheduleItemVo();
                logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????ItemsVo.getItemType()--------->{}",ItemsVo.getItemType());
                    if("reassig".equals(schType)){
                        if(ItemsVo.getItemType().equals("4")){//??????
                        }else{//??????
                            scheduleItem.setTaskId(scheduleTaskId);
                            scheduleItem.setNodeType("Check");
                            if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                String fullName = "";
                                List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                if(sysAreaDictVoList != null){
                                    SysAreaDictVo vo = sysAreaDictVoList.get(0);
                                    fullName = vo.getFullName();
                                    if(!"".equals(fullName)){
                                        fullName = fullName.replaceAll("-","");
                                    }
                                }
                                scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                            }else{
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                            }
                            scheduleItem.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
                            scheduleItem.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
                            scheduleItem.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
                            scheduleItem.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
                        }
                    }else{
                        if(ItemsVo.getItemType().equals("4")){//??????
                        }else{//??????
                            scheduleItem.setTaskId(scheduleTaskId);
                            scheduleItem.setNodeType("Check");
                            if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                String fullName = "";
                                List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                if(sysAreaDictVoList != null){
                                    SysAreaDictVo vo = sysAreaDictVoList.get(0);
                                    fullName = vo.getFullName();
                                    if(!"".equals(fullName)){
                                        fullName = fullName.replaceAll("-","");
                                    }
                                }
                                scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                            }else{
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                            }
                            scheduleItem.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
                            scheduleItem.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
                            scheduleItem.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
                            scheduleItem.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
                        }
                    }
                scheduleItemList.add(scheduleItem);
            }
            List<ScheduleItemVo> scheduleItemListResult = new ArrayList<ScheduleItemVo>();
            ScheduleItemVo scheduleItemResult = new ScheduleItemVo();
            ScheduleItemVo personScheduleItemResult = new ScheduleItemVo();
            logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????scheduleItemList?????????--------->{}",scheduleItemList!=null?scheduleItemList.size():0);
            //????????????????????????
            for(ScheduleItemVo vos : scheduleItemList){
                /*if(vos.getNodeType().equals("PLoss")){
                    personScheduleItemResult = vos;
                }*/
                if("Check".equals(vos.getNodeType())){
                    scheduleItemResult = vos;
                }
            }
            if(personScheduleItemResult !=null && personScheduleItemResult.getNodeType() != null){
                scheduleItemListResult.add(personScheduleItemResult);
            }
            if(scheduleItemResult !=null && scheduleItemResult.getNodeType() != null){
                scheduleItemListResult.add(scheduleItemResult);
            }
            logger.info("??????????????????/?????????????????????????????????????????????????????????????????????????????????scheduleItemListResult?????????--------->{}",scheduleItemListResult!=null?scheduleItemListResult.size():0);
            return scheduleItemListResult;
        }else if("DLoss".equals(schType)){//??????????????????
            PrpLScheduleTaskVo prpLScheduleTaskVo = new PrpLScheduleTaskVo();
            if(registInformationVo.getPrpLScheduleTaskVoList()!=null && registInformationVo.getPrpLScheduleTaskVoList().size()>0){
                prpLScheduleTaskVo = registInformationVo.getPrpLScheduleTaskVoList().get(0);
            }
             List<PrpLScheduleDefLossVo> prpLScheduleDefLosses = prpLScheduleTaskVo.getPrpLScheduleDefLosses();
             //??????id
             int id = 1;
             List<ScheduleItemVo> scheduleItemList = new ArrayList<ScheduleItemVo>();
             for(PrpLScheduleDefLossVo ItemsVo :prpLScheduleDefLosses){
                 ScheduleItemVo scheduleItemDOrG =new ScheduleItemVo();
                 
                 //????????????taskid
                 String deflossType = ItemsVo.getDeflossType();
                 String subNodeCode = FlowNode.DLCar.toString();
                 if("1".equals(deflossType)){
                     subNodeCode = FlowNode.DLCar.toString();
                 }else{
                     subNodeCode = FlowNode.DLProp.toString();
                 }
/*                 List<PrpLWfTaskVo> PrpLWfTaskVos = wfTaskHandleService.findInTask(prpLScheduleTaskVo.getRegistNo(),String.valueOf(ItemsVo.getId()),subNodeCode);
                 if(PrpLWfTaskVos!=null && PrpLWfTaskVos.size()>0){
                     scheduleItemDOrG.setTaskId(String.valueOf(PrpLWfTaskVos.get(0).getTaskId()));
                 }else{
                     scheduleItemDOrG.setTaskId(String.valueOf(id++));
                 }*/
                 scheduleItemDOrG.setTaskId(ItemsVo.getId().toString());
                 if(ItemsVo.getDeflossType().equals("1")){
                     scheduleItemDOrG.setNodeType("DLCar");
                     if(ItemsVo.getLossitemType().equals("0")){
                         scheduleItemDOrG.setItemNo("0");
                         scheduleItemDOrG.setItemNoName("??????");
                     }else if(ItemsVo.getLossitemType().equals("1")){
                         scheduleItemDOrG.setItemNo("1");
                         scheduleItemDOrG.setItemNoName(ItemsVo.getItemsContent());
                     }else{
                         scheduleItemDOrG.setItemNo("2");
                         scheduleItemDOrG.setItemNoName(ItemsVo.getItemsContent());
                     }
                 }else if(ItemsVo.getDeflossType().equals("2")){
                     scheduleItemDOrG.setNodeType("DLProp");
                     if(ItemsVo.getLossitemType().equals("0")){
                         scheduleItemDOrG.setItemNo("0");
                         scheduleItemDOrG.setItemNoName("??????");
                     }else if(ItemsVo.getLossitemType().equals("1")){
                         scheduleItemDOrG.setItemNo("1");
                         scheduleItemDOrG.setItemNoName(ItemsVo.getItemsContent());
                     }else{
                         scheduleItemDOrG.setItemNo("2");
                         scheduleItemDOrG.setItemNoName(ItemsVo.getItemsContent());
                     }
                 }
                 if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                     String fullName = "";
                     List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                     if(sysAreaDictVoList != null){
                         SysAreaDictVo vo = sysAreaDictVoList.get(0);
                         fullName = vo.getFullName();
                         if(!"".equals(fullName)){
                             fullName = fullName.replaceAll("-","");
                         }
                     }
                     scheduleItemDOrG.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                 }else{
                     scheduleItemDOrG.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                 }
                 scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
                 scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
                 scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
                 scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
                 
                 scheduleItemList.add(scheduleItemDOrG);
             }
             return scheduleItemList;
        }else if("reassig".equals(schType)){//??????
            PrpLScheduleTaskVo prpLScheduleTaskVo = registInformationVo.getPrpLScheduleTaskVo();
            List<ScheduleItemVo> scheduleItemList = new ArrayList<ScheduleItemVo>();
                ScheduleItemVo scheduleItemDOrG =new ScheduleItemVo();
                scheduleItemDOrG.setTaskId(prpLScheduleTaskVo.getId().toString());
                scheduleItemDOrG.setNodeType("Check");
                if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                    String fullName = "";
                    List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                    if(sysAreaDictVoList != null){
                        SysAreaDictVo vo = sysAreaDictVoList.get(0);
                        fullName = vo.getFullName();
                        if(!"".equals(fullName)){
                            fullName = fullName.replaceAll("-","");
                        }
                    }
                    scheduleItemDOrG.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                }else{
                    scheduleItemDOrG.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                }
                scheduleItemDOrG.setNextHandlerCode(prpLScheduleTaskVo.getScheduledUsercode());
                scheduleItemDOrG.setNextHandlerName(prpLScheduleTaskVo.getScheduledUsername());
                scheduleItemDOrG.setScheduleObjectId(prpLScheduleTaskVo.getScheduledComcode());
                scheduleItemDOrG.setScheduleObjectName(prpLScheduleTaskVo.getScheduledComname());
                scheduleItemList.add(scheduleItemDOrG);
                return scheduleItemList;
        }else{
            if(registInformationVo.getScheduleItemList() != null && registInformationVo.getScheduleItemList().size() > 0){
                for(ScheduleItemVo vo : registInformationVo.getScheduleItemList()){
                    if("DLCar".equals(vo.getNodeType())){
                        PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(vo.getTaskId()));
                        if(ItemsVo.getLossitemType().equals("0")){
                            vo.setItemNo("0");
                            vo.setItemNoName("??????");
                        }else if(ItemsVo.getLossitemType().equals("1")){
                            vo.setItemNo("1");
                            vo.setItemNoName(ItemsVo.getItemsContent());
                        }else{
                            vo.setItemNo("2");
                            vo.setItemNoName(ItemsVo.getItemsContent());
                        }
                    }else if("DLProp".equals(vo.getNodeType())){
                        PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(vo.getTaskId()));
                        if(ItemsVo.getLossitemType().equals("0")){
                            vo.setItemNo("0");
                            vo.setItemNoName("??????");
                        }else if(ItemsVo.getLossitemType().equals("1")){
                            vo.setItemNo("1");
                            vo.setItemNoName(ItemsVo.getItemsContent());
                        }else{
                            vo.setItemNo("2");
                            vo.setItemNoName(ItemsVo.getItemsContent());
                        }
                    }
                }
                List<ScheduleItemVo> scheduleItemList = registInformationVo.getScheduleItemList();
                return scheduleItemList;
            }
        }
        return null;
    }
    
    public void saveCTorMTACarchildInterfaceLog(RegistInformationVo registInformationVo,String url,BusinessType businessType,SysUserVo userVo) {
        
        String requestXml = "";
        String responseXml = "";
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// ?????? class??????
        String registNo = "";
        try{
            if("2".equals(registInformationVo.getSchType())){
                RevokeRestoreInfoReqVo reqVo = registInformationVo.getReqVo();

                registNo = reqVo.getBody().getRevokeRestoreTaskInfos().get(0).getRegistNo();
                logVo.setRegistNo(reqVo.getBody().getRevokeRestoreTaskInfos().get(0).getRegistNo());
                logVo.setBusinessType(registInformationVo.getBusinessType());
                logVo.setBusinessName(BusinessType.MTA_claimCancelRestore.getName());
                logVo.setOperateNode(FlowNode.ReCanLVrf.name());
                requestXml = stream.toXML(reqVo);
                String requestType = reqVo.getHead().getRequestType();
                if(requestType.contains("CT")){
                    logVo.setBusinessName(BusinessType.CT_claimCancelRestore.getName());
                }else if (requestType.contains("MTA")) {
                    logVo.setBusinessName(BusinessType.MTA_claimCancelRestore.getName());
                }
                
                RevokeRestoreInfoResVo resVo = registInformationVo.getResVo();
                logVo.setErrorMessage(resVo.getHead().getErrMsg());
                responseXml = stream.toXML(resVo);
                CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
                if("1".equals(carChildResponseHead.getErrNo()+"")){
                    logVo.setStatus("1");
                    logVo.setErrorCode("true");
                }else {
                    logVo.setStatus("0");
                    logVo.setErrorCode("false");
                }
            }else{
                RevokeInfoReqVo reqVo = registInformationVo.getRevokeInfoReqVo();
               
                registNo = reqVo.getBody().getRevokeTaskInfos().get(0).getRegistNo();
                
                logVo.setRegistNo(reqVo.getBody().getRevokeTaskInfos().get(0).getRegistNo());
                
                if("4".equals(registInformationVo.getSchType())){
                    String requestType = reqVo.getHead().getRequestType();
                    if(requestType.contains("CT")){
                        logVo.setBusinessType(BusinessType.CT_registCancel.name());
                        logVo.setBusinessName(BusinessType.CT_registCancel.getName());
                    }else if (requestType.contains("MTA")) {
                        logVo.setBusinessType(BusinessType.MTA_registCancel.name());
                        logVo.setBusinessName(BusinessType.MTA_registCancel.getName());
                    }
                    logVo.setOperateNode(FlowNode.Cancel.name());
                }else if("3".equals(registInformationVo.getSchType())){
                    String requestType = reqVo.getHead().getRequestType();
                    if(requestType.contains("CT")){
                        logVo.setBusinessType(BusinessType.CT_claimCancel.name());
                        logVo.setBusinessName(BusinessType.CT_claimCancel.getName());
                    }else if (requestType.contains("MTA")) {
                        logVo.setBusinessType(BusinessType.MTA_claimCancel.name());
                        logVo.setBusinessName(BusinessType.MTA_claimCancel.getName());
                    }
                    logVo.setOperateNode(FlowNode.Cancel.name());
                }else{
                    logVo.setBusinessType(businessType.name());
                    logVo.setBusinessName(businessType.getName());
                    logVo.setOperateNode(registInformationVo.getOperateNode());
                } 
                requestXml = stream.toXML(reqVo);
                RevokeInfoResVo resVo = registInformationVo.getRevokeInfoResVo();
                logVo.setErrorMessage(resVo.getHead().getErrMsg());
                responseXml = stream.toXML(resVo);
                CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
                if("1".equals(carChildResponseHead.getErrNo()+"")){
                    logVo.setStatus("1");
                    logVo.setErrorCode("true");
                }else {
                    logVo.setStatus("0");
                    logVo.setErrorCode("false");
                }
                //1????????????2????????????3????????????4????????????5???????????????
            }
        
        }catch(Exception e){
            logVo.setRegistNo(registNo);
            logVo.setStatus("0");
            logVo.setErrorCode("false");
            e.printStackTrace();
        }
        finally{
            Date date = new Date();
            logVo.setComCode(userVo.getComCode());
            logVo.setRequestTime(date);
            logVo.setRequestUrl(url);
            logVo.setCreateUser(userVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXml);
            logVo.setResponseXml(responseXml);
            claimInterfaceLogService.save(logVo);
        }
    }

    @Override
    public void organizationCTorMTA(PrpLClaimCancelVo pClaimCancelVo,PrpLWfTaskVo prpLWfTaskVo,SysUserVo userVo) throws Exception {
        //???????????????????????????/?????????
        //PrpLWfMainVo prpLWfMainVo = sendMsgToMobileService.findPrpLWfMainVoByRegistNo(wfTaskVo.getRegistNo());
        //??????out?????????????????????/???????????????
        String flagCT = "0";
        String flagMTA = "0";
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(prpLWfTaskVo.getRegistNo());
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());

        List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(prpLWfTaskVo.getRegistNo(), FlowNode.Check.toString());
        List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForOutByNodeCode(prpLWfTaskVo.getRegistNo(), FlowNode.DLoss.toString());
        List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
        if(checkVolist != null && checkVolist.size() > 0){
            volist.addAll(checkVolist);
        }
        if(dLossVolist != null && dLossVolist.size() > 0){
            volist.addAll(dLossVolist);
        }
        List<PrpLWfTaskVo> prpLWfTaskCTVos = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> prpLWfTaskMTAVos = new ArrayList<PrpLWfTaskVo>();
        if(volist != null && volist.size() > 0){
            for(PrpLWfTaskVo vo : volist){
                if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                    if(configValueCTCheckVo != null && "2".equals(vo.getIsMobileAccept()) && "1".equals(configValueCTCheckVo.getConfigValue())){
                        flagCT = "1";
                        prpLWfTaskCTVos.add(vo);
                    }else if(configValueMTACheckVo != null && "3".equals(vo.getIsMobileAccept()) && "1".equals(configValueMTACheckVo.getConfigValue())){
                        flagMTA = "1";
                        prpLWfTaskMTAVos.add(vo);
                    }
                }
            }
        }
        RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        if("1".equals(flagCT)){
            //?????????????????????  
            head.setRequestType("CT_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head);
            pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
            if(prpLWfTaskCTVos != null && prpLWfTaskCTVos.size() > 0 ){
                this.organizationAndSendCTorMTA(prpLWfTaskCTVos,pClaimCancelVo,prpLWfTaskVo,reqVo,userVo);
            }
        }
        if("1".equals(flagMTA)){
            //?????????????????????
            head.setRequestType("MTA_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head); 
            pClaimCancelVo.setClaimRecoverTime(new Date());// ??????/??????????????????
            if(prpLWfTaskMTAVos != null && prpLWfTaskMTAVos.size() > 0 ){
                this.organizationAndSendCTorMTA(prpLWfTaskMTAVos,pClaimCancelVo,prpLWfTaskVo,reqVo,userVo);
            }
           
        }
    }
    
    public void organizationAndSendCTorMTA(List<PrpLWfTaskVo> prpLWfTaskCTVos,PrpLClaimCancelVo pClaimCancelVo,PrpLWfTaskVo wfTaskVo,RevokeInfoReqVo reqVo,SysUserVo userVo) {
        List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
        String url = null;
        RevokeBodyVo body = new RevokeBodyVo();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = pClaimCancelVo.getClaimRecoverTime();
        String timeStamp = dateFormat.format(date);
        if(prpLWfTaskCTVos!=null&&prpLWfTaskCTVos.size()>0){
            for(PrpLWfTaskVo prpLWfTask:prpLWfTaskCTVos){
                RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
                if("0".equals(prpLWfTask.getHandlerStatus())){
                    revokeTaskInfoVo.setTaskId(prpLWfTask.getHandlerIdKey());
                }else if ("2".equals(prpLWfTask.getHandlerStatus())) {
                    if(FlowNode.DLoss.name().equals(prpLWfTask.getNodeCode())){
                        if(FlowNode.DLCar.name().equals(prpLWfTask.getSubNodeCode())){
                            PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            revokeTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                        }else if(FlowNode.DLProp.name().equals(prpLWfTask.getSubNodeCode())){
                            PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            /*revokeTaskInfoVo.setTaskId(prpLdlossPropMainVo.getScheduleTaskId().toString());*/
                            //?????????????????????????????????????????????
                            PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                            revokeTaskInfoVo.setTaskId(vo.getId().toString());
                        }
                    }else {
                        //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(wfTaskVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLWfTask.getRegistNo(),"1","1");
                        revokeTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
                    }
                }    
                revokeTaskInfoVo.setRegistNo(prpLWfTask.getRegistNo());
                revokeTaskInfoVo.setNodeType(prpLWfTask.getNodeCode());
                revokeTaskInfoVo.setRevokeType("5");
                revokeTaskInfoVo.setReason(pClaimCancelVo.getApplyReason());
                revokeTaskInfoVo.setRemark("");
                revokeTaskInfoVo.setTimeStamp(timeStamp);
                revokeTaskInfoVos.add(revokeTaskInfoVo);
            }
        }      
        body.setRevokeTaskInfos(revokeTaskInfoVos);
        reqVo.setBody(body);
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        try{
            url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//????????????MTA_URL
            
            resVo = this.sendRevokeInformation(reqVo,url);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //??????????????????
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("3");
            if("CT_006".equals(reqVo.getHead().getRequestType())){
                url=SpringProperties.getProperty("CT_URL")+CT_02;
            }
            this.saveCTorMTACarchildInterfaceLog(informationVo,url,null,userVo);
          //????????????????????????
            CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
            if("1".equals(carChildResponseHead.getErrNo()+"")){
                PrplcarchildregistcancleVo prplcarchildregistcancleVo = new PrplcarchildregistcancleVo();
                    prplcarchildregistcancleVo.setExamineRusult("1");
                    prplcarchildregistcancleVo.setStatus("1");
                    prplcarchildregistcancleVo.setHandleUser(userVo.getUserName());
                    prplcarchildregistcancleVo.setHandleDate(date);
                    prplcarchildregistcancleVo.setRegistNo(wfTaskVo.getRegistNo());
                    updatePrplcarchildregist(prplcarchildregistcancleVo);
            }
        }
    }

    @Override
    public void sendClaimCancelRestoreCTorMTA(String registNo,SysUserVo userVo) throws Exception {
        //??????in?????????????????????/???????????????
        String flagCT = "0";
        String flagMTA = "0";
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        PrpLConfigValueVo configValueMTACheckVo = ConfigUtil.findConfigByCode(CodeConstants.MTACheck,prpLRegistVo.getComCode());
        PrpLConfigValueVo configValueCTCheckVo = ConfigUtil.findConfigByCode(CodeConstants.CTCheck,prpLRegistVo.getComCode());
        List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
        List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
        List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
        if(checkVolist != null && checkVolist.size() > 0){
            volist.addAll(checkVolist);
        }
        if(dLossVolist != null && dLossVolist.size() > 0){
            volist.addAll(dLossVolist);
        }
        List<PrpLWfTaskVo> prpLWfTaskCTVos = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> prpLWfTaskMTAVos = new ArrayList<PrpLWfTaskVo>();
        if(volist != null && volist.size() > 0){
            for(PrpLWfTaskVo vo : volist){
                if("0".equals(vo.getHandlerStatus()) || "2".equals(vo.getHandlerStatus())){
                    if(configValueCTCheckVo != null && "2".equals(vo.getIsMobileAccept()) && "1".equals(configValueCTCheckVo.getConfigValue())){
                        flagCT = "1";
                        prpLWfTaskCTVos.add(vo);
                    }else if(configValueMTACheckVo != null && "3".equals(vo.getIsMobileAccept()) && "1".equals(configValueMTACheckVo.getConfigValue())){
                        flagMTA = "1";
                        prpLWfTaskMTAVos.add(vo);
                    }
                }
            }
        }
        RevokeRestoreInfoReqVo reqVo = new RevokeRestoreInfoReqVo();
        if("1".equals(flagCT)){
            CarchildHeadVo head = new CarchildHeadVo();
            String businessType = null;
            //?????????????????????  
            head.setRequestType("CT_007");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            businessType = BusinessType.CT_claimCancelRestore.name();
            reqVo.setHead(head);
            if(prpLWfTaskCTVos != null && prpLWfTaskCTVos.size() > 0){
                this.organizationAndSendCTorMTA(prpLWfTaskCTVos,reqVo,businessType,userVo);
            }
            
        }
        if( "1".equals(flagMTA)){
            CarchildHeadVo head = new CarchildHeadVo();
            String businessType = null;
            //?????????????????????
            head.setRequestType("MTA_007");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            businessType = BusinessType.MTA_claimCancelRestore.name();
            reqVo.setHead(head);
            if(prpLWfTaskMTAVos != null && prpLWfTaskMTAVos.size() > 0){
                this.organizationAndSendCTorMTA(prpLWfTaskMTAVos,reqVo,businessType,userVo);
            }
        }
    }
    
    private void organizationAndSendCTorMTA(List<PrpLWfTaskVo> prpLWfTaskVos,RevokeRestoreInfoReqVo reqVo,String businessType,SysUserVo userVo) {
        RevokeRestoreBodyVo body = new RevokeRestoreBodyVo();
        List<RevokeRestoreTaskInfoVo> revokeRestoreTaskInfos = new ArrayList<RevokeRestoreTaskInfoVo>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
       if(prpLWfTaskVos != null && prpLWfTaskVos.size()>0){
            for(PrpLWfTaskVo prpLWfTask:prpLWfTaskVos){
                RevokeRestoreTaskInfoVo revokeRestoreTaskInfoVo = new RevokeRestoreTaskInfoVo(); 
                if("0".equals(prpLWfTask.getHandlerStatus())){
                    revokeRestoreTaskInfoVo.setTaskId(prpLWfTask.getHandlerIdKey());
                }else if ("2".equals(prpLWfTask.getHandlerStatus())) {
                    if(FlowNode.DLoss.name().equals(prpLWfTask.getNodeCode())){
                        if(FlowNode.DLCar.equals(prpLWfTask.getSubNodeCode())){
                            PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            revokeRestoreTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                        }else if(FlowNode.DLProp.name().equals(prpLWfTask.getSubNodeCode())){
                            PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            //revokeRestoreTaskInfoVo.setTaskId(prpLdlossPropMainVo.getId().toString());
                            //?????????????????????????????????????????????
                            PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                            revokeRestoreTaskInfoVo.setTaskId(vo.getId().toString());
                        }
                    }else if(FlowNode.Chk.equals(prpLWfTask.getSubNodeCode())){
                        //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLWfTask.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLWfTask.getRegistNo(),"1","1");
                        revokeRestoreTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
                    }
                }
                revokeRestoreTaskInfoVo.setRegistNo(prpLWfTask.getRegistNo());
                revokeRestoreTaskInfoVo.setTimeStamp(timeStamp);
                revokeRestoreTaskInfos.add(revokeRestoreTaskInfoVo);
            }
        }
        body.setRevokeRestoreTaskInfos(revokeRestoreTaskInfos);
        reqVo.setBody(body);
        
        String url = SpringProperties.getProperty("MTA_URL_RECOVERYCANCEL");
        RevokeRestoreInfoResVo resVo = new RevokeRestoreInfoResVo();
        try{
            resVo = this.sendRevokeRestoreInformation(reqVo,url);
        }
        catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            //??????????????????
            RegistInformationVo registInformationVo = new RegistInformationVo();
            registInformationVo.setReqVo(reqVo);
            registInformationVo.setResVo(resVo);
            registInformationVo.setBusinessType(businessType);
            registInformationVo.setSchType("2");
            if("CT_007".equals(reqVo.getHead().getRequestType())){
                url=SpringProperties.getProperty("CT_URL")+CT_03;
            }
            this.saveCTorMTACarchildInterfaceLog(registInformationVo,url,null,userVo);
        }
   }
    
    @Override
    public void updatePrplcarchildregist(PrplcarchildregistcancleVo prplcarchildregistcancleVo) {
        QueryRule rule=QueryRule.getInstance();
        rule.addEqual("registNo",prplcarchildregistcancleVo.getRegistNo());
        rule.addEqual("status","0");
        rule.addDescOrder("createTime");
         List<Prplcarchildregistcancle> listpo=databaseDao.findAll(Prplcarchildregistcancle.class,rule);
         if(listpo != null && listpo.size() > 0){
             Prplcarchildregistcancle po=listpo.get(0);
             if(prplcarchildregistcancleVo != null){
                 Beans.copy().excludeNull().from(prplcarchildregistcancleVo).to(po);
                 databaseDao.update(Prplcarchildregistcancle.class,po);
             }
         }
    }

    @Override
    public void registCancel(ClaimInterfaceLogVo logVo,SysUserVo userVo) {
        RevokeInfoReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RevokeInfoReqVo.class);
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(logVo.getRegistNo());
        String url = null;
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        try{
            url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//????????????MTA_URL
            logger.info("url=============="+url);
            resVo = sendRevokeInformation(reqVo,url);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //??????????????????
            Date date = prpLRegistVo.getCancelTime();
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("4");
            saveCTorMTACarchildInterfaceLog(informationVo,url,null,userVo);
            //????????????????????????
            CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
            if("1".equals(carChildResponseHead.getErrNo()+"")){
                PrplcarchildregistcancleVo prplcarchildregistcancleVo = findPrplcarchildregistcancleVoByRegistNo(prpLRegistVo.getRegistNo());
                if(prplcarchildregistcancleVo!=null){
                    prplcarchildregistcancleVo.setExamineRusult("1");
                    prplcarchildregistcancleVo.setStatus("1");
                    prplcarchildregistcancleVo.setHandleUser(WebUserUtils.getUserName());
                    prplcarchildregistcancleVo.setHandleDate(date);
                    updatePrplcarchildregistcancle(prplcarchildregistcancleVo);
                }
                
            }
        }
 /*       
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
      //??????out?????????????????????/???????????????
        String flagCT = "0";
        String flagMTA = "0";
        List<PrpLWfTaskVo> checkVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.Check.toString());
        List<PrpLWfTaskVo> dLossVolist = wfFlowQueryService.findTaskVoForInByNodeCode(registNo, FlowNode.DLoss.toString());
        List<PrpLWfTaskVo> volist = new ArrayList<PrpLWfTaskVo>();
        if(checkVolist != null && checkVolist.size() > 0){
            volist.addAll(checkVolist);
        }
        if(dLossVolist != null && dLossVolist.size() > 0){
            volist.addAll(dLossVolist);
        }
        List<PrpLWfTaskVo> prpLWfTaskCTVos = new ArrayList<PrpLWfTaskVo>();
        List<PrpLWfTaskVo> prpLWfTaskMTAVos = new ArrayList<PrpLWfTaskVo>();
        if(volist != null && volist.size() > 0){
            for(PrpLWfTaskVo vo : volist){
                    if("2".equals(vo.getIsMobileAccept())){
                        flagCT = "1";
                        prpLWfTaskCTVos.add(vo);
                    }else if("3".equals(vo.getIsMobileAccept())){
                        flagMTA = "1";
                        prpLWfTaskMTAVos.add(vo);
                    }
            }
        }
        RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        if("1".equals(flagCT)){
            //?????????????????????  
            head.setRequestType("CT_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head);
            //???????????????????????????/?????????
            registCancelSendCTorMTA(prpLWfTaskCTVos,prpLRegistVo,reqVo);
        }
        if("1".equals(flagMTA)){
            //?????????????????????
            head.setRequestType("MTA_006");
            head.setUser("claim_user");
            head.setPassWord("claim_psd");
            reqVo.setHead(head);
            //???????????????????????????/?????????
            registCancelSendCTorMTA(prpLWfTaskMTAVos,prpLRegistVo,reqVo);
        }*/
    }
    
/*    private void registCancelSendCTorMTA(List<PrpLWfTaskVo> prpLWfTaskVos,PrpLRegistVo prpLRegistVo,RevokeInfoReqVo reqVo) {
        List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
        String url = null;
        RevokeBodyVo body = new RevokeBodyVo();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = prpLRegistVo.getCancelTime();
        String timeStamp = dateFormat.format(date);
        if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0){
            for(PrpLWfTaskVo prpLWfTask:prpLWfTaskVos){
                RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
                if("0".equals(prpLWfTask.getHandlerStatus())){
                    revokeTaskInfoVo.setTaskId(prpLWfTask.getHandlerIdKey());
                }else if ("2".equals(prpLWfTask.getHandlerStatus())) {
                    if(FlowNode.DLoss.name().equals(prpLWfTask.getNodeCode())){
                        if(FlowNode.DLCar.name().equals(prpLWfTask.getSubNodeCode())){
                            PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            revokeTaskInfoVo.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                        }else if(FlowNode.DLProp.name().equals(prpLWfTask.getSubNodeCode())){
                            PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTask.getHandlerIdKey()));
                            //?????????????????????????????????????????????
                            PrpLScheduleDefLossVo vo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                            revokeTaskInfoVo.setTaskId(vo.getId().toString());
                        }
                    }else {
                        //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLRegistVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                        PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(prpLWfTask.getRegistNo(),"1","1");
                        revokeTaskInfoVo.setTaskId(prpLScheduleTaskVo.getId().toString());
                    }
                }    
                revokeTaskInfoVo.setRegistNo(prpLWfTask.getRegistNo());
                revokeTaskInfoVo.setNodeType(prpLWfTask.getNodeCode());
                revokeTaskInfoVo.setRevokeType("4");
                revokeTaskInfoVo.setReason(prpLRegistVo.getPrpLRegistExt().getCancelReason());
                revokeTaskInfoVo.setRemark("");
                revokeTaskInfoVo.setTimeStamp(timeStamp);
                revokeTaskInfoVos.add(revokeTaskInfoVo);
            }
        }      
        body.setRevokeTaskInfos(revokeTaskInfoVos);
        reqVo.setBody(body);
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        try{
            url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//????????????MTA_URL
            logger.info("url=============="+url);
            resVo = sendRevokeInformation(reqVo,url);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //??????????????????
            SysUserVo userVo = new SysUserVo();
            userVo.setComCode(WebUserUtils.getComCode());
            userVo.setUserCode(WebUserUtils.getUserCode());
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("4");
            saveCTorMTACarchildInterfaceLog(informationVo,url,null,userVo);
            //????????????????????????
            CarchildResponseHeadVo carChildResponseHead = resVo.getHead();
            if("1".equals(carChildResponseHead.getErrNo()+"")){
                PrplcarchildregistcancleVo prplcarchildregistcancleVo = findPrplcarchildregistcancleVoByRegistNo(prpLRegistVo.getRegistNo());
                if(prplcarchildregistcancleVo!=null){
                    prplcarchildregistcancleVo.setExamineRusult("1");
                    prplcarchildregistcancleVo.setStatus("1");
                    prplcarchildregistcancleVo.setHandleUser(WebUserUtils.getUserName());
                    prplcarchildregistcancleVo.setHandleDate(date);
                    updatePrplcarchildregistcancle(prplcarchildregistcancleVo);
                }
                
            }
        }
    }*/

    @Override
    public void scheduleDefLossCancel(ClaimInterfaceLogVo logVo,SysUserVo userVo) {
/*        List<RevokeTaskInfoVo> revokeTaskInfoVos = new ArrayList<RevokeTaskInfoVo>();
        RevokeInfoReqVo reqVo = new RevokeInfoReqVo();
        CarchildHeadVo head = new CarchildHeadVo();
        RevokeBodyVo body = new RevokeBodyVo();
        BusinessType businessType = null;
        String dLossId="";*/
        BusinessType businessType = null;
        RevokeInfoReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RevokeInfoReqVo.class);
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        String url = null;
        try{
            url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//????????????MTA_URL
            logger.info("url=============="+url);
            resVo = sendRevokeInformation(reqVo,url);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //??????????????????
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("5");
            informationVo.setOperateNode(FlowNode.DLoss.name());
            if("CT".contains(reqVo.getHead().getRequestType())){
                businessType = BusinessType.CT_cancelDfloss;
            }else{
                businessType = BusinessType.MTA_cancelDfloss;
            }
            saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
        }
/*        List<RevokeTaskInfoVo> revokeTaskInfos = revokeInfoReqVo.getBody().getRevokeTaskInfos();
        dLossId = revokeTaskInfos.get(0).getNewTaskId();
        List<PrpLWfTaskVo> prpLWfTaskInVos = wfFlowQueryService.findUnAcceptTask(logVo.getRegistNo(),FlowNode.DLoss.name());
        for(PrpLWfTaskVo wfTaskVo:prpLWfTaskInVos){
            if(dLossId.equals(wfTaskVo.getHandlerIdKey())){
                PrpLScheduleDefLossVo scheduleDefLossVo = scheduleTaskService.findPrpLScheduleDefLossVoById(Long.parseLong(dLossId));
                if(scheduleDefLossVo!=null && scheduleDefLossVo.getId()!=null){
                     
                     //???????????????????????????/?????????
                     if("2".equals(wfTaskVo.getIsMobileAccept())||"3".equals(wfTaskVo.getIsMobileAccept())){
                         
                         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                         Date date = new Date();
                         String timeStamp = dateFormat.format(date);
                         
                         if("2".equals(wfTaskVo.getIsMobileAccept())){
                             //?????????????????????  
                             head.setRequestType("CT_006");
                             head.setUser("claim_user");
                             head.setPassWord("claim_psd");
                             businessType = BusinessType.CT_cancelDfloss;
                         }else {
                             //?????????????????????
                             head.setRequestType("MTA_006");
                             head.setUser("claim_user");
                             head.setPassWord("claim_psd");
                             businessType = BusinessType.MTA_cancelDfloss;
                         }
                         reqVo.setHead(head);
                         RevokeTaskInfoVo revokeTaskInfoVo = new RevokeTaskInfoVo();
                         revokeTaskInfoVo.setTaskId(scheduleDefLossVo.getId().toString());
                         revokeTaskInfoVo.setRegistNo(scheduleDefLossVo.getRegistNo());
                         revokeTaskInfoVo.setNodeType(FlowNode.DLoss.name());
                         revokeTaskInfoVo.setRevokeType("3");
                         if(revokeTaskInfos !=null && revokeTaskInfos.size() > 0){
                             revokeTaskInfoVo.setReason(revokeTaskInfos.get(0).getReason());
                             revokeTaskInfoVo.setRemark(revokeTaskInfos.get(0).getRemark());
                         }
                         revokeTaskInfoVo.setTimeStamp(timeStamp);
                         revokeTaskInfoVos.add(revokeTaskInfoVo);
                         
                     }
                }
            }
        }
        if(revokeTaskInfoVos!=null && revokeTaskInfoVos.size() > 0){
            String url = null;
            url = SpringProperties.getProperty("");
            body.setRevokeTaskInfos(revokeTaskInfoVos);
            reqVo.setBody(body);
            RevokeInfoResVo resVo = new RevokeInfoResVo();
            try{
                url = SpringProperties.getProperty("MTA_URL_CANCELTASK");//????????????MTA_URL
                logger.info("url=============="+url);
                resVo = sendRevokeInformation(reqVo,url);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                //??????????????????
                RegistInformationVo informationVo = new RegistInformationVo();
                informationVo.setRevokeInfoReqVo(reqVo);
                informationVo.setRevokeInfoResVo(resVo);
                informationVo.setSchType("5");
                informationVo.setOperateNode(FlowNode.DLoss.name());
                saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
            }
        }*/
    }

    @Override
    public void registSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo) {
        RegistInfoReqVo registInfoReqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RegistInfoReqVo.class);
        RegistBodyVo bodyVo = registInfoReqVo.getBody();
        List<ScheduleItemVo> scheduleItemVos = bodyVo.getScheduleItems();
        String schType = "";
        List<PrpLScheduleItemsVo> prpLScheduleItemses = new ArrayList<PrpLScheduleItemsVo>();
        PrpLScheduleTaskVo prpLScheduleTaskVo = new PrpLScheduleTaskVo();
        List<PrpLScheduleTaskVo> prpLScheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
        List<PrpLScheduleDefLossVo> PrpLScheduleDefLosses = new ArrayList<PrpLScheduleDefLossVo>();
        PrpLRegistVo registVo = registQueryService.findByRegistNo(logVo.getRegistNo());
        List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(logVo.getRegistNo());
        for(ScheduleItemVo vo : scheduleItemVos){
            if("Check".equals(vo.getNodeType())){//??????
                schType = "Check";
                prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(Long.parseLong(vo.getTaskId()));
                prpLScheduleTaskVo.setScheduledUsercode(vo.getNextHandlerCode());
                prpLScheduleTaskVo.setScheduledUsername(vo.getNextHandlerName());
                prpLScheduleTaskVo.setScheduledComcode(vo.getScheduleObjectId());
                prpLScheduleTaskVo.setScheduledComname(vo.getScheduleObjectName());
                prpLScheduleItemses.addAll(prpLScheduleTaskVo.getPrpLScheduleItemses());
                break;
            }else{//??????
                PrpLScheduleDefLossVo prpLScheduleDefLossVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(vo.getTaskId()));
                PrpLScheduleDefLosses.add(prpLScheduleDefLossVo);
                Long scheduleId = scheduleService.findTaskIdByDefLossId(Long.parseLong(vo.getTaskId()));
                prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(scheduleId);
                prpLScheduleTaskVo.setScheduledUsercode(vo.getNextHandlerCode());
                prpLScheduleTaskVo.setScheduledUsername(vo.getNextHandlerName());
                prpLScheduleTaskVo.setScheduledComcode(vo.getScheduleObjectId());
                prpLScheduleTaskVo.setScheduledComname(vo.getScheduleObjectName());
                schType = "DLoss";
            }
            
        }

        PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(prpLScheduleTaskVo.getScheduledUsercode());
        if(prplIntermMainVo != null){
            if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
                RegistInformationVo registInformationVo = new RegistInformationVo();
                registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
                registInformationVo.setScheduleItemVos(prpLScheduleItemses);
                registInformationVo.setSchType(schType);
                registInformationVo.setUser(userVo);
                if("Check".equals(schType)){//??????
                    List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(prpLScheduleTaskVo.getRegistNo(), FlowNode.Chk.toString());
                    if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
                        //?????????????????????
                        Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                        @Override
                        public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                                return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                            }
                        });
                    }
                    prpLWfTaskVoResult.add(prpLWfTaskVoList.get(0));
                    try{
                        sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                    }
                    catch(Exception e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    //??????
                    prpLScheduleTaskVoList.add(prpLScheduleTaskVo);
                    registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
                    registInformationVo.setPrpLScheduleTaskVoList(prpLScheduleTaskVoList);
                    //??????prpLScheduleTaskVoList???id???????????????
                    for(PrpLScheduleDefLossVo vo : PrpLScheduleDefLosses){
                        List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findInTaskByOther(vo.getRegistNo(),vo.getId().toString(),"DLoss");
                        prpLWfTaskVoResult.addAll(prpLWfTaskVoList);
                    }
                    try{
                        sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                    }
                    catch(Exception e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                
            }
        }
    }

    @Override
    public void handOverCancelSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo) {
        String xmlToSend = logVo.getRequestXml();
        RevokeInfoReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RevokeInfoReqVo.class);
        String xmlReturn = "";
        String url = logVo.getRequestUrl();
        String flags = "1";//??????????????????????????????
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        BusinessType businessType = null;
        RevokeBodyVo revokeBodyVo = reqVo.getBody();
        List<RevokeTaskInfoVo> revokeTaskInfoVos = revokeBodyVo.getRevokeTaskInfos();
        RevokeTaskInfoVo revokeTaskInfoVo = revokeTaskInfoVos.get(0);
        try{
            if("CT_006".equals(reqVo.getHead().getRequestType())){
                businessType = BusinessType.CT_handOver;
                url=SpringProperties.getProperty("CT_URL")+CT_02;
                logger.info("??????????????????send---------------------------"+xmlToSend);
                xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_02);
                //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
                logger.info("??????????????????return---------------------------"+xmlReturn);
           }else{
               businessType = BusinessType.MTA_handOver;
               logger.info("??????????????????send---------------------------"+xmlToSend);
               xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,reqVo.getHead().getRequestType());
               logger.info("??????????????????return---------------------------"+xmlReturn);
           }
           resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RevokeInfoResVo.class);
        }
        catch(Exception e){
            flags = "0";
            e.printStackTrace();
        }
        finally{
            //??????????????????
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("1");
            informationVo.setOperateNode(logVo.getOperateNode());
            saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
            if(resVo != null && "1".equals(resVo.getHead().getErrNo())){
                flags = "1";
            }else{
                flags = "0";  
            }
        }
        if("1".equals(flags)){//??????????????????
            List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(logVo.getRegistNo());
            PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(revokeTaskInfoVo.getNewHandlerUser());
            PrpLRegistVo registVo = registQueryService.findByRegistNo(logVo.getRegistNo());
            if(prplIntermMainVo != null && "1".equals(flags)){
                PrpLWfTaskVo prpLWfTaskVo = null;
                RegistInformationVo registInformationVo = new RegistInformationVo();
                if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                    registInformationVo.setUser(userVo);
                    List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
                    
                    //revokeTaskInfoVo.getNewTaskId()??????????????????????????????
                    //???subnodecode??????
                    List<PrpLWfTaskVo> wfTaskVoList = null;
                    if("Check".equals(revokeTaskInfoVo.getNodeType())){
                        wfTaskVoList = wfTaskHandleService.findInTask(logVo.getRegistNo(),revokeTaskInfoVo.getNewTaskId(),FlowNode.Chk.name());
                    }else{
                        wfTaskVoList = wfTaskHandleService.findInTask(logVo.getRegistNo(),revokeTaskInfoVo.getNewTaskId(),revokeTaskInfoVo.getNodeType());
                    }
                    
                    if(wfTaskVoList != null && wfTaskVoList.size() > 0){
                        prpLWfTaskVo = wfTaskVoList.get(0);
                        prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                    }else{//??????????????????
                        // ????????????????????????????????????????????????ID??????????????????
                        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByScheduleDeflossId(logVo.getRegistNo(),Long.parseLong(revokeTaskInfoVo.getNewTaskId()));
                        if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0 ){
                            wfTaskVoList = wfTaskHandleService.findInTask(logVo.getRegistNo(),prpLDlossCarMainVoList.get(0).getId().toString(),revokeTaskInfoVo.getNodeType());
                            prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                            prpLWfTaskVo = wfTaskVoList.get(0);
                        }
                    }
                    
                    //??????id
                    int id = 1;
                    ScheduleItemVo scheduleItem =new ScheduleItemVo();
                    List<ScheduleItemVo> scheduleItemVoList = new ArrayList<ScheduleItemVo>();
                    PrpLConfigValueVo configValueVoByMap = ConfigUtil.findConfigByCode(CodeConstants.SWITCHMAP);
                    if(prpLWfTaskVo != null){
                        //???????????????
                        if("0".equals(prpLWfTaskVo.getHandlerStatus())){
                            scheduleItem.setTaskId(prpLWfTaskVo.getHandlerIdKey());
                            if(FlowNode.DLoss.name().equals(prpLWfTaskVo.getNodeCode())){
                                PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                                if(ItemsVo.getLossitemType().equals("0")){
                                    scheduleItem.setItemNo("0");
                                    scheduleItem.setItemNoName("??????");
                                }else if(ItemsVo.getLossitemType().equals("1")){
                                    scheduleItem.setItemNo("1");
                                    scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                                }else{
                                    scheduleItem.setItemNo("2");
                                    scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                                }
                                
                                Long  taskId = scheduleService.findTaskIdByDefLossId(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                                PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                                if(prpLScheduleTaskVo != null){
                                    if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                        String fullName = "";
                                        List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                        if(sysAreaDictVoList != null){
                                            SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                            fullName = sysAreaDictVo.getFullName();
                                            if(StringUtils.isNotBlank(fullName)){
                                                fullName = fullName.replaceAll("-","");
                                            }
                                        }
                                        scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                    }else{
                                        scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                    }
                                }else{
                                    scheduleItem.setDamageAddress("");
                                }
                            }else{
                                PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                                if(prpLScheduleTaskVo != null){
                                    if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                        String fullName = "";
                                        List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                        if(sysAreaDictVoList != null){
                                            SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                            fullName = sysAreaDictVo.getFullName();
                                            if(StringUtils.isNotBlank(fullName)){
                                                fullName = fullName.replaceAll("-","");
                                            }
                                        }
                                        scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                    }else{
                                        scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                    }
                                }else{
                                    scheduleItem.setDamageAddress("");
                                }
                            }
                        }else if ("2".equals(prpLWfTaskVo.getHandlerStatus())) {
                            if(FlowNode.DLoss.name().equals(prpLWfTaskVo.getNodeCode())){
                                if(FlowNode.DLCar.name().equals(prpLWfTaskVo.getSubNodeCode())){
                                    PrpLDlossCarMainVo prpLDlossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                                    scheduleItem.setTaskId(prpLDlossCarMainVo.getScheduleDeflossId().toString());
                                
                                    PrpLScheduleDefLossVo ItemsVo = scheduleService.findScheduleDefLossByPk(prpLDlossCarMainVo.getScheduleDeflossId());
                                    if(ItemsVo.getLossitemType().equals("0")){
                                        scheduleItem.setItemNo("0");
                                        scheduleItem.setItemNoName("??????");
                                    }else if(ItemsVo.getLossitemType().equals("1")){
                                        scheduleItem.setItemNo("1");
                                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                                    }else{
                                        scheduleItem.setItemNo("2");
                                        scheduleItem.setItemNoName(ItemsVo.getItemsContent());
                                    }
                                    
                                    Long  taskId = scheduleService.findTaskIdByDefLossId(prpLDlossCarMainVo.getScheduleDeflossId());
                                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(taskId);
                                    if(prpLScheduleTaskVo != null){
                                        if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                            String fullName = "";
                                            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                            if(sysAreaDictVoList != null){
                                                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                                fullName = sysAreaDictVo.getFullName();
                                                if(StringUtils.isNotBlank(fullName)){
                                                    fullName = fullName.replaceAll("-","");
                                                }
                                            }
                                            scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                        }else{
                                            scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                        }
                                    }else{
                                        scheduleItem.setDamageAddress("");
                                    }
                                }else if(FlowNode.DLProp.name().equals(prpLWfTaskVo.getSubNodeCode())){
                                    PrpLdlossPropMainVo prpLdlossPropMainVo = propLossService.findPropMainVoById(Long.parseLong(prpLWfTaskVo.getHandlerIdKey()));
                                    /*revokeTaskInfoVo.setTaskId(prpLdlossPropMainVo.getScheduleTaskId().toString());*/
                                    //?????????????????????????????????????????????
                                    PrpLScheduleDefLossVo defLossVo = scheduleTaskService.findPrpLScheduleDefLossVoByOther(prpLdlossPropMainVo.getRegistNo(),prpLdlossPropMainVo.getSerialNo(),"2");
                                    scheduleItem.setTaskId(defLossVo.getId().toString());
                                    
                                    if(defLossVo.getLossitemType().equals("0")){
                                        scheduleItem.setItemNo("0");
                                        scheduleItem.setItemNoName("??????");
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
                                        if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                            String fullName = "";
                                            List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                            if(sysAreaDictVoList != null){
                                                SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                                fullName = sysAreaDictVo.getFullName();
                                                if(StringUtils.isNotBlank(fullName)){
                                                    fullName = fullName.replaceAll("-","");
                                                }
                                            }
                                            scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                        }else{
                                            scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                        }
                                    }else{
                                        scheduleItem.setDamageAddress("");
                                    }
                                }
                            }else {
                                //PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.getScheduleTask(prpLWfTaskVo.getRegistNo(),ScheduleStatus.CHECK_SCHEDULED);
                                PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(logVo.getRegistNo(),"1","1");
                                scheduleItem.setTaskId(prpLScheduleTaskVo.getId().toString());
                                if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                    String fullName = "";
                                    List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                    if(sysAreaDictVoList != null){
                                        SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                        fullName = sysAreaDictVo.getFullName();
                                        if(StringUtils.isNotBlank(fullName)){
                                            fullName = fullName.replaceAll("-","");
                                        }
                                    }
                                    scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                }else{
                                    scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                }
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getDamageAddress());
                            }
                        }    
                    }else{
                        if("DLProp".equals(revokeTaskInfoVo.getNodeType())){//??????????????????
                            PrpLScheduleDefLossVo defLossVo = scheduleService.findScheduleDefLossByPk(Long.parseLong(revokeTaskInfoVo.getNewTaskId()));
                            scheduleItem.setTaskId(defLossVo.getId().toString());
                            
                            if(defLossVo.getLossitemType().equals("0")){
                                scheduleItem.setItemNo("0");
                                scheduleItem.setItemNoName("??????");
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
                                if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                    String fullName = "";
                                    List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                    if(sysAreaDictVoList != null){
                                        SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                        fullName = sysAreaDictVo.getFullName();
                                        if(StringUtils.isNotBlank(fullName)){
                                            fullName = fullName.replaceAll("-","");
                                        }
                                    }
                                    scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                                }else{
                                    scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                                }
                            }else{
                                scheduleItem.setDamageAddress("");
                            }
                        }else if("Check".equals(revokeTaskInfoVo.getNodeType())){//??????????????????
                            PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskByOther(logVo.getRegistNo(),"1","1");
                            scheduleItem.setTaskId(prpLScheduleTaskVo.getId().toString());
                            if(configValueVoByMap!=null && "1".equals(configValueVoByMap.getConfigValue())){//??????
                                String fullName = "";
                                List<SysAreaDictVo> sysAreaDictVoList = areaDictService.findAreaCode(prpLScheduleTaskVo.getRegionCode());
                                if(sysAreaDictVoList != null){
                                    SysAreaDictVo  sysAreaDictVo = sysAreaDictVoList.get(0);
                                    fullName = sysAreaDictVo.getFullName();
                                    if(StringUtils.isNotBlank(fullName)){
                                        fullName = fullName.replaceAll("-","");
                                    }
                                }
                                scheduleItem.setDamageAddress(fullName + prpLScheduleTaskVo.getCheckareaName());//????????????
                            }else{
                                scheduleItem.setDamageAddress(prpLScheduleTaskVo.getCheckareaName());//????????????
                            }
                            scheduleItem.setDamageAddress(prpLScheduleTaskVo.getDamageAddress());
                        }else{
                            scheduleItem.setTaskId(String.valueOf(id));
                            scheduleItem.setDamageAddress(registVo.getDamageAddress());
                        }
                    }
                    //?????????????????????????????????????????????????????????????????????
                    if(FlowNode.DLCar.name().equals(revokeTaskInfoVo.getNodeType())){
                        //??????
                        scheduleItem.setNodeType("DLCar");
                    }else if(FlowNode.DLProp.name().equals(revokeTaskInfoVo.getNodeType())){
                        //??????
                        scheduleItem.setNodeType("DLProp");
                    }else if(FlowNode.Check.name().equals(revokeTaskInfoVo.getNodeType())){
                        //??????
                        scheduleItem.setNodeType("Check");
                    }else{
                        //??????
                        scheduleItem.setNodeType("PLoss");
                    }
                    scheduleItem.setNextHandlerCode(revokeTaskInfoVo.getNewHandlerUser());
                    String  nextHandlerName= sysUserService.findByUserCode(revokeTaskInfoVo.getNewHandlerUser()).getUserName();
                    String assignComs = revokeTaskInfoVo.getNewTaskId();
                    String  assignName = CodeTranUtil.transCode("ComCode", assignComs);
                    scheduleItem.setNextHandlerName(nextHandlerName);
                    scheduleItem.setScheduleObjectId(assignComs);
                    scheduleItem.setScheduleObjectName(assignName);
                    scheduleItemVoList.add(scheduleItem);
                    registInformationVo.setScheduleItemList(scheduleItemVoList);
                    try{
                        sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                    }
                    catch(Exception e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void scheduleChangeSendCtOrMta(ClaimInterfaceLogVo logVo,SysUserVo userVo) {
        String xmlToSend = logVo.getRequestXml();
        RevokeInfoReqVo reqVo = ClaimBaseCoder.xmlToObj(logVo.getRequestXml(), RevokeInfoReqVo.class);
        RevokeBodyVo revokeBodyVo = reqVo.getBody();
        List<RevokeTaskInfoVo> revokeTaskInfoVos = revokeBodyVo.getRevokeTaskInfos();
        RevokeTaskInfoVo revokeTaskInfoVo = revokeTaskInfoVos.get(0);
        PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(revokeTaskInfoVo.getNewHandlerUser());
        String xmlReturn = "";
        String flags = "1";
        RevokeInfoResVo resVo = new RevokeInfoResVo();
        String url = logVo.getRequestUrl();
        BusinessType businessType = null;
        try{
            if("CT_006".equals(reqVo.getHead().getRequestType())){
                businessType = BusinessType.CT_scheduleChange;
                url = SpringProperties.getProperty("CT_URL")+CT_02;
                logger.info("??????????????????send---------------------------"+xmlToSend);
                xmlReturn = CarchildUtil.requestPlatformForCT(xmlToSend,url,CT_02);
                //xmlReturn = CarchildUtil.requestCarchild(xmlToSend, url, 50, sourceType);
                logger.info("??????????????????return---------------------------"+xmlReturn);
           }else{
               businessType = BusinessType.MTA_scheduleChange;
               logger.info("??????????????????send---------------------------"+xmlToSend);
               xmlReturn = CarchildUtil.requestPlatform(xmlToSend,url,reqVo.getHead().getRequestType());
               logger.info("??????????????????return---------------------------"+xmlReturn);
           }
           resVo = ClaimBaseCoder.xmlToObj(xmlReturn, RevokeInfoResVo.class);
        }
        catch(Exception e){
            flags = "0";
            e.printStackTrace();
        }
        finally{
            //??????????????????
            RegistInformationVo informationVo = new RegistInformationVo();
            informationVo.setRevokeInfoReqVo(reqVo);
            informationVo.setRevokeInfoResVo(resVo);
            informationVo.setSchType("1");
            saveCTorMTACarchildInterfaceLog(informationVo,url,businessType,userVo);
            if(resVo != null && "1".equals(resVo.getHead().getErrNo())){
                flags = "1";
            }else{
                flags = "0";  
            }
        }

        if(prplIntermMainVo != null && "1".equals(flags)){
            if("0003".equals(prplIntermMainVo.getIntermCode())||"0005".equals(prplIntermMainVo.getIntermCode())){
                List<PrpLCMainVo> prpLCMainVoList = policyViewService.getPolicyAllInfo(logVo.getRegistNo());
                PrpLRegistVo registVo = registQueryService.findByRegistNo(logVo.getRegistNo());
                if("Check".equals(revokeTaskInfoVo.getNodeType())){
                  //??????????????????????????????????????????????????????/????????????????????????
                    //??????taskid
                    String reassigTaskId = "";
                    List<PrpLWfTaskVo> prpLWfTaskVoList = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(logVo.getRegistNo(), FlowNode.Chk.toString());
                    if(prpLWfTaskVoList!=null && prpLWfTaskVoList.size()>0){
                        //?????????????????????
                        Collections.sort(prpLWfTaskVoList, new Comparator<PrpLWfTaskVo>() {
                        @Override
                        public int compare(PrpLWfTaskVo o1,PrpLWfTaskVo o2) {
                                return o2.getTaskInTime().compareTo(o1.getTaskInTime());
                            }
                        });
                        reassigTaskId = prpLWfTaskVoList.get(0).getHandlerIdKey().toString();
                    }
                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(Long.parseLong(revokeTaskInfoVo.getNewTaskId()));
                    RegistInformationVo registInformationVo = new RegistInformationVo();
                    registInformationVo.setNewTaskId(reassigTaskId);
                    registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
                    registInformationVo.setFlag("reassig");
                    
                    prpLScheduleTaskVo.setId(Long.valueOf(reassigTaskId));
                    registInformationVo.setPrpLScheduleTaskVo(prpLScheduleTaskVo);
                    registInformationVo.setSchType("reassig");
                    registInformationVo.setUser(userVo);
                    List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
                    //????????????in??????????????????
                    List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findInTaskByOther(logVo.getRegistNo(),reassigTaskId,revokeTaskInfoVo.getNodeType());
                    prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                    try{
                        sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                    }
                    catch(Exception e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                
                }else{//??????
                    //??????????????????????????????????????????????????????/????????????????????????
                    RegistInformationVo registInformationVo = new RegistInformationVo();
                    List<PrpLScheduleTaskVo> scheduleTaskVoList = new ArrayList<PrpLScheduleTaskVo>();
                    Long scheduleTaskId = scheduleService.findTaskIdByDefLossId(Long.parseLong(revokeTaskInfoVo.getNewTaskId()));
                    PrpLScheduleTaskVo prpLScheduleTaskVo = scheduleService.findScheduleTaskVoByPK(scheduleTaskId);
                    scheduleTaskVoList.add(prpLScheduleTaskVo);
                    registInformationVo.setNewHandlerUser(prpLScheduleTaskVo.getScheduledUsercode());
                    registInformationVo.setPrpLScheduleTaskVoList(scheduleTaskVoList);
                    //???????????????????????????DLoss?????????DLoss
                    registInformationVo.setSchType("DLoss");
                    registInformationVo.setUser(userVo);
                    List<PrpLWfTaskVo> prpLWfTaskVoResult = new ArrayList<PrpLWfTaskVo>();
                    //????????????in??????????????????
                    List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findInTask(logVo.getRegistNo(),revokeTaskInfoVo.getNewTaskId(),revokeTaskInfoVo.getNodeType());
                    prpLWfTaskVoResult.add(wfTaskVoList.get(0));
                    try{
                        sendRegistInformation(prplIntermMainVo,registVo,prpLCMainVoList,prpLWfTaskVoResult,registInformationVo);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
                
            }
        }
        
    }

    @Override
    public PrplcarchildregistcancleVo findCarchildregistcancleVoByRegistNo(String registNo) {
        PrplcarchildregistcancleVo vo = null;
        QueryRule rule = QueryRule.getInstance();
        rule.addEqual("registNo",registNo);
        rule.addDescOrder("createTime");
        List<Prplcarchildregistcancle> listpo = databaseDao.findAll(Prplcarchildregistcancle.class,rule);
         if(listpo != null && listpo.size() > 0){
             vo = new PrplcarchildregistcancleVo();
             Prplcarchildregistcancle po=listpo.get(0);
             Beans.copy().from(po).to(vo);
         }
        return vo;
    }
}
