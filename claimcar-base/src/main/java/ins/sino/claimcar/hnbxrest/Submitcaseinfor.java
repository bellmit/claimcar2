package ins.sino.claimcar.hnbxrest;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.framework.lang.Springs;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DateUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ScheduleType;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WFMobileService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfMainService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.util.TaskExtMapUtils;
import ins.sino.claimcar.flow.vo.PrpLWfMainVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskUserInfoVo;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.service.spring.QuickUserServiceImpl;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseCarVo;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;
import ins.sino.claimcar.hnbxrest.vo.RspCode;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
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
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.service.ScheduleHandlerService;
import ins.sino.claimcar.regist.service.ScheduleTaskService;
import ins.sino.claimcar.regist.vo.PolicyInfoVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistCarLossVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLTmpCMainVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleTaskVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

/**
 * 4.1.快处快赔系统发送案件信息给保险公司
 */
public class Submitcaseinfor extends HttpServlet {

    @Autowired
    private RegistTmpService registTmpService;
    @Autowired
    private WfMainService wfMainService;
    @Autowired
    private SubmitcaseinforService submitcaseinforService;
    @Autowired
    private PolicyQueryService policyQueryService;
    @Autowired
    private PrpLCMainService prpLCMainService;
    @Autowired
    private RegistHandlerService registHandlerService;
    @Autowired
    private RegistService registService;
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    private InterfaceAsyncService interfaceAsyncService;
    @Autowired
    private ScheduleTaskService scheduleTaskService;
    @Autowired
    private WfFlowQueryService wfFlowQueryService;
    @Autowired
    private FounderCustomService founderCustomService;
    @Autowired
    private ScheduleHandlerService scheduleHandlerService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private MobileCheckService mobileCheckService;
    @Autowired
    private AreaDictService areaDictService;
    @Autowired
    private QuickUserService quickUserService;
    @Autowired
    private LossCarService lossCarService;
    @Autowired
    private WFMobileService wFMobileService;
    @Autowired
    private AssignService assignService;
    
    private static final long serialVersionUID = 1L;
    public static final String AUTOSCHEDULE_URL_METHOD = "prplschedule/autoSchedule.do";
    public static final String HANDLSCHEDDORGULE_URL_METHOD = "prplschedule/claimSubmissionOrReassignment.do";
    private static Logger logger = LoggerFactory.getLogger(Submitcaseinfor.class);


    @Override
    public void init() throws ServletException {
        if(registTmpService==null){
            registTmpService = (RegistTmpService)Springs.getBean(RegistTmpService.class);
        }
        if(wfMainService==null){
            wfMainService = (WfMainService)Springs.getBean(WfMainService.class);
        }
        if(submitcaseinforService==null){
            submitcaseinforService = (SubmitcaseinforService)Springs.getBean("submitcaseinforService");
        }
        if(policyQueryService==null){
            policyQueryService = (PolicyQueryService)Springs.getBean(PolicyQueryService.class);
        }
        if(prpLCMainService==null){
            prpLCMainService = (PrpLCMainService)Springs.getBean(PrpLCMainService.class);
        }
        if(registHandlerService==null){
            registHandlerService = (RegistHandlerService)Springs.getBean(RegistHandlerService.class);
        }
        if(registService==null){
            registService = (RegistService)Springs.getBean(RegistService.class);
        }
        if(interfaceLogService==null){
            interfaceLogService = (ClaimInterfaceLogService)Springs.getBean(ClaimInterfaceLogService.class);
        }
        if(codeTranService==null){
            codeTranService = (CodeTranService)Springs.getBean(CodeTranService.class);
        }
        if(wfTaskHandleService==null){
            wfTaskHandleService = (WfTaskHandleService)Springs.getBean(WfTaskHandleService.class);
        }
        if(policyViewService==null){
            policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
        }
        if(interfaceAsyncService==null){
            interfaceAsyncService = (InterfaceAsyncService)Springs.getBean(InterfaceAsyncService.class);
        }
        if(scheduleTaskService==null){
            scheduleTaskService = (ScheduleTaskService)Springs.getBean(ScheduleTaskService.class);
        }
        if(wfFlowQueryService==null){
            wfFlowQueryService = (WfFlowQueryService)Springs.getBean(WfFlowQueryService.class);
        }
        if(founderCustomService==null){
            founderCustomService = (FounderCustomService)Springs.getBean(FounderCustomService.class);
        }
        if(scheduleHandlerService==null){
            scheduleHandlerService = (ScheduleHandlerService)Springs.getBean(ScheduleHandlerService.class);
        }
        if(registQueryService==null){
            registQueryService = (RegistQueryService)Springs.getBean(RegistQueryService.class);
        }
        if(mobileCheckService==null){
            mobileCheckService = (MobileCheckService)Springs.getBean(MobileCheckService.class);
        }
        if(areaDictService==null){
            areaDictService = (AreaDictService)Springs.getBean(AreaDictService.class);
        }
        if(quickUserService==null){
            quickUserService = (QuickUserService)Springs.getBean(QuickUserServiceImpl.class);
        }
        if(lossCarService==null){
            lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
        }
        if(wFMobileService==null){
        	wFMobileService=(WFMobileService)Springs.getBean(WFMobileService.class);
        }
        if(assignService==null){
        	assignService=(AssignService)Springs.getBean(AssignService.class);
        }
        
    }

    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
        PrplQuickCaseInforVo quickCaseInforVo = new PrplQuickCaseInforVo();
        List<PrpLCMainVo> prpLCMainVos = new ArrayList<PrpLCMainVo>();
        PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out =  response.getWriter();
        String jsonStr = "";
        try{
            InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String temp = "";
            while(( temp = bufferedReader.readLine() )!=null){
                jsonStr += temp;
            }
            read.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        String businessno = null;
        String trafficinsno = null;
        String BIPolicyNo = null;
        String CIPolicyNo = null;
        String registNo = null;
        Map<String,String> tmap = new HashMap<String,String>();
        Map<String,String> bmap = new HashMap<String,String>();
        Map<String,Object> classMap = new HashMap<String,Object>();
        
        try{
        	JSONObject rejson = JSONObject.fromObject(jsonStr);
            //保存交互日志
            logger.debug(request.getRequestURL().toString());
            logger.debug(rejson.toString());
            classMap.put("casecarlist",PrplQuickCaseCarVo.class);
            quickCaseInforVo = (PrplQuickCaseInforVo)JSONObject.toBean(rejson,PrplQuickCaseInforVo.class,classMap);
           
            checkRequest(quickCaseInforVo);     
            //
            PrpLWfMainVo prpLWfMainVo = wfMainService.findPrpLWfMainVoByLicenseNo(quickCaseInforVo.getCasecarno());
            if(prpLWfMainVo==null){
                // A.在河南快赔报案，推送新理赔
                // 报案
                // 根据车牌号查询保单
                PolicyInfoVo policyInfoVo = new PolicyInfoVo();
                List<PolicyInfoVo> policyInfoVos = new ArrayList<PolicyInfoVo>();
                policyInfoVo.setLicenseNo(quickCaseInforVo.getCasecarno());
                policyInfoVo.setCheckFlag("2");
                policyInfoVo.setOnlyValid("1");
                policyInfoVo.setDamageTime(quickCaseInforVo.getCasedate());
                Page<PolicyInfoVo> page = policyQueryService.findPrpCMainForPage(policyInfoVo,0,10);
                if(page.getResult().size()>0){
                    for(int i = 0; i<page.getResult().size(); i++ ){
                        policyInfoVos.add((PolicyInfoVo)page.getResult().get(i));
                    }
                }
                // 有效保单报案
                if(policyInfoVos!=null&&policyInfoVos.size()>0){
                    List<PrpLCMainVo> prpLCMains = new ArrayList<PrpLCMainVo>();
                    PolicyInfoVo policyInfo = policyInfoVos.get(0);
                    // 调用存储过程
                    if(StringUtils.isNotBlank(policyInfo.getPolicyNo())){
                        PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyInfo.getPolicyNo(),quickCaseInforVo.getCasedate());
                        if(prpLCMainVo!=null){
                            // 业务分类
                            business(prpLCMainVo);
                            prpLCMains.add(prpLCMainVo);
                        }
                    }
                    if(StringUtils.isNotBlank(policyInfo.getRelatedPolicyNo())){
                        PrpLCMainVo prpLCMainVo = prpLCMainService.findRegistPolicy(policyInfo.getRelatedPolicyNo(),quickCaseInforVo.getCasedate());
                        if(prpLCMainVo!=null){
                            // 业务分类
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
                    }
                    
                    prpLRegistVo = convert(quickCaseInforVo,prpLCMains);
                    //给报案信息表赋值保单号，主表优先保存商业保单号，如果没有商业险保单则存交强险保单号，关联报案时，报案扩展表存储交强保单，否则不存
                    if (!StringUtils.isEmpty(BIPolicyNo)) {
                        prpLRegistVo.setPolicyNo(BIPolicyNo);
                        if (!StringUtils.isEmpty(CIPolicyNo)) {
                            prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(CIPolicyNo);
                        }
                    } else {
                        prpLRegistVo.setPolicyNo(CIPolicyNo);
                    }
                    prpLRegistVo = registHandlerService.save(prpLRegistVo,prpLCMains,true,BIPolicyNo,CIPolicyNo);    
                    registNo = prpLRegistVo.getRegistNo();        
                    //提交工作流
                    submitWf(prpLRegistVo,true);             
                    
                    //判断商业交强是否报案
                    prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
                    if(prpLCMainVos != null && prpLCMainVos.size() > 0){
                        for(PrpLCMainVo prpLCMainVo:prpLCMainVos){
                            if(!"1101".equals(prpLCMainVo.getRiskCode())){
                                businessno = prpLCMainVo.getRegistNo();
                            }else {
                                trafficinsno = prpLCMainVo.getRegistNo(); 
                            }
                        }
                    }
                }else {
                    // 临时保单报案
                    PrpLTmpCMainVo tmpCMainVo = new PrpLTmpCMainVo();
                    List<PrpLTmpCItemKindVo> tmpCItemKindVos = new ArrayList<PrpLTmpCItemKindVo>();
                    PrpLTmpCItemCarVo tmpCItemCarVo = new PrpLTmpCItemCarVo();
                    PrpLRegistVo prpLRegist = new PrpLRegistVo();
                    prpLRegist.setIsQuickCase("1");
                    setTmp(quickCaseInforVo,tmpCMainVo,tmpCItemKindVos,tmpCItemCarVo);
                    registNo = registTmpService.registTmpSave(tmpCMainVo,tmpCItemKindVos,tmpCItemCarVo,prpLRegist);
                    List<PrpLCMainVo> prpLCMains = registTmpService.findTempPolicyByRegistNo(registNo);
                    for (PrpLCMainVo mainVo : prpLCMains) {
                        if(StringUtils.isNotBlank(mainVo.getRiskCode())){
                            if("1101".equals(mainVo.getRiskCode())){
                                CIPolicyNo = mainVo.getPolicyNo();
                            }
                            else{
                                BIPolicyNo = mainVo.getPolicyNo();
                            }
                        }
                        mainVo.setId(null);
                        if (mainVo.getPrpCItemCars() != null && mainVo.getPrpCItemCars().size() > 0) {
                            for (PrpLCItemCarVo carVo :mainVo.getPrpCItemCars()) {
                                carVo.setItemCarId(null);
                            }
                        }
                        if (mainVo.getPrpCItemKinds() != null && mainVo.getPrpCItemKinds().size() > 0) {
                            for (PrpLCItemKindVo kindVo : mainVo.getPrpCItemKinds()) {
                                kindVo.setItemKindId(null);
                            }
                        }
                    }       
                    //更新临时保单
                    if(StringUtils.isEmpty(BIPolicyNo)){//删除商业
                        registTmpService.deleteByRegistNoAndRiskCode(registNo, "12");
                    }
                    if(StringUtils.isEmpty(CIPolicyNo)){//删除交强
                        registTmpService.deleteByRegistNoAndRiskCode(registNo, "11");
                    }
                    
                    prpLRegistVo = convert(quickCaseInforVo,prpLCMains);
                    //给报案信息表赋值保单号，主表优先保存商业保单号，如果没有商业险保单则存交强险保单号，关联报案时，报案扩展表存储交强保单，否则不存
                    if (!StringUtils.isEmpty(BIPolicyNo)) {
                        prpLRegistVo.setPolicyNo(BIPolicyNo);
                        if (!StringUtils.isEmpty(CIPolicyNo)) {
                            prpLRegistVo.getPrpLRegistExt().setPolicyNoLink(CIPolicyNo);
                        }
                    } else {
                        prpLRegistVo.setPolicyNo(CIPolicyNo);
                    }
                    prpLRegistVo.setRegistNo(registNo);
                    prpLRegistVo.setTempRegistFlag("1");            
                    prpLRegistVo = registHandlerService.save(prpLRegistVo,prpLCMains,true,BIPolicyNo,CIPolicyNo);    
                    registNo = prpLRegistVo.getRegistNo();
                    //提交工作流
                    submitWf(prpLRegistVo,false);
                    businessno = registNo;
                    trafficinsno = registNo;
                    
                }         
            }
            else{
                // B.在新理赔系统报案，快赔系统推送数据过来关联。
                //直接关联已存在报案数据
                registNo = prpLWfMainVo.getRegistNo();
                ResultPage<PrpLDlossCarMainVo>  resultPage = lossCarService.findLossCarMainPageByRegistNo(registNo,0,10);
                List<PrpLDlossCarMainVo> prpLDlossCarMainVos = resultPage.getData();
                if(prpLDlossCarMainVos!=null&&prpLDlossCarMainVos.size()>0){
                    throw new IllegalArgumentException("已线下办理");
                }else {
                    PrpLRegistVo prpLRegist = registService.findRegistByRegistNo(registNo);
                    prpLRegist.setIsQuickCase("1");
                    registService.saveOrUpdate(prpLRegist);
                    List<PrpLWfTaskVo> prpLWfTaskList = wfFlowQueryService.findTaskVoForInAndOutByRegistNo(registNo);
                    Map<String,String> otherTagMap = new HashMap<String,String>();
                    otherTagMap.put("isQuickCase","1");
                    if(prpLWfTaskList!=null&&prpLWfTaskList.size()>0){
                        for(PrpLWfTaskVo prpLWfTaskVo:prpLWfTaskList){         
                            String bussTag = TaskExtMapUtils.joinBussTag(prpLWfTaskVo.getBussTag(),otherTagMap);
                            prpLWfTaskVo.setBussTag(bussTag);
                            wfTaskHandleService.updateTaskByFlowId(prpLWfTaskVo);
                        }
                    }
                    
                    //判断商业交强是否报案
                    prpLCMainVos = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
                    if(prpLCMainVos != null && prpLCMainVos.size() > 0){
                        for(PrpLCMainVo prpLCMainVo:prpLCMainVos){
                            if(!"1101".equals(prpLCMainVo.getRiskCode())){
                                businessno = prpLCMainVo.getRegistNo();
                            }else {
                                trafficinsno = prpLCMainVo.getRegistNo();                 
                            }
                        }
                    } 
                }    
            }
            
            //保存快赔信息
            quickCaseInforVo.setRegistno(registNo);
            submitcaseinforService.save(quickCaseInforVo);
            
            /*// 保存日志表
            ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
            logVo.setRegistNo(registNo);
            logVo.setBusinessType(BusinessType.HNQC_regist.name());
            logVo.setBusinessName(BusinessType.HNQC_regist.getName());
            logVo.setOperateNode(FlowNode.Regis.getName());
            logVo.setComCode("00010095");
            logVo.setErrorCode("true");
            logVo.setStatus("1");
            Date date = new Date();
            logVo.setRequestTime(date);
            logVo.setRequestUrl(request.getRequestURL().toString());
            logVo.setCreateUser("AUTO");
            logVo.setCreateTime(date);
            logVo.setRequestXml(jsonStr);
            logVo.setResponseXml(data.toString());
            interfaceLogService.save(logVo);*/
            
            tmap.put("trafficinsno",trafficinsno);
            bmap.put("businessno",businessno);
            
            List<Map<String,String>> list = new ArrayList<Map<String,String>>();
            if(StringUtils.isBlank(trafficinsno)){
                list.add(null);
            }else {
                list.add(tmap);
            }
            if(StringUtils.isBlank(businessno)){
                list.add(null);
            }else {
                list.add(bmap);
            }
            out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.SUCCESS,"成功",list)));
        }
        catch(Exception e){
            e.printStackTrace();
            out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.FAIL,"错误信息:"+e.getMessage())));
        }
        finally{
            out.flush();
            out.close();
        }
    }


    private void checkRequest(PrplQuickCaseInforVo quickCaseInforVo) {       
        if(StringUtils.isBlank(quickCaseInforVo.getCasenumber())){
            throw new IllegalArgumentException("快处报案号不能为空");
        }
        if(submitcaseinforService.findByCasenumber(quickCaseInforVo.getCasenumber())!=null){
            throw new IllegalArgumentException("快赔案件已存在");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getCasecarno())){
            throw new IllegalArgumentException("案件车牌号不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getCasetelephone())){
            throw new IllegalArgumentException("报案手机号不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getCaselon())){
            throw new IllegalArgumentException("经度不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getCaselat())){
            throw new IllegalArgumentException("纬度不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getCaseaddress())){
            throw new IllegalArgumentException("地址描述不能为空");
        }
        if(quickCaseInforVo.getCasedate()==null){
            throw new IllegalArgumentException("报案时间不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getAccidenttype())){
            throw new IllegalArgumentException("事故情形不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getAreaid())){
            throw new IllegalArgumentException("区域不能为空");
        }
        if(quickCaseInforVo.getCasecarlist()==null||quickCaseInforVo.getCasecarlist().size()==0){
            throw new IllegalArgumentException("涉案车辆信息不能为空");
        }
        if(StringUtils.isBlank(quickCaseInforVo.getAccidenttype())){
            throw new IllegalArgumentException("接口类型不能为空");
        }
    }

    /**
     * 在报案、调度页面保单信息增加显示业务板块、 业务分类，若业务板块为会员业务，业务分类为“ 30-南网员工、31-员工亲属、34-浦发员工、35-农行员工、 36-深圳海关员工”这5类客户，则业务板块和业务分类显示的值标红
     * @param prpLCMainVo
     * @author huanggusheng
     */
    public void business(PrpLCMainVo prpLCMainVo) {
        Map<String,String> businessClassCheckMsg = codeTranService.findCodeNameMap("BusinessClassCheckMsg");
        if(businessClassCheckMsg.containsKey(prpLCMainVo.getBusinessClass())){
            prpLCMainVo.setMemberFlag("1");
        }
    }

    private PrpLRegistVo convert(PrplQuickCaseInforVo quickCaseInforVo,List<PrpLCMainVo> prpLCMains) {
        PrpLRegistVo prpLRegistVo = new PrpLRegistVo();
        List<PrplQuickCaseCarVo> prplquickcasecars = quickCaseInforVo.getCasecarlist();
        PrpLRegistExtVo prpLRegistExt = new PrpLRegistExtVo();
        List<PrpLRegistCarLossVo> prpLRegistCarLosses = new ArrayList<PrpLRegistCarLossVo>();
        prpLRegistVo.setIsQuickCase("1");
        if(prpLCMains!=null&&prpLCMains.size()>0){
            for(PrpLCMainVo prpLCMain:prpLCMains){
                prpLRegistVo.setComCode(prpLCMain.getComCode());
                prpLRegistVo.setRiskCode(prpLCMain.getRiskCode());
                if( !"1101".equals(prpLCMain.getRiskCode())){
                    break;
                }
            }
        }

        if(prplquickcasecars!=null&&prplquickcasecars.size()>0){
            if(prplquickcasecars.size()>1){
                prpLRegistExt.setIsCarLoss("1");
            }
            for(PrplQuickCaseCarVo prplQuickCaseCar:prplquickcasecars){
                PrpLRegistCarLossVo prpLRegistCarLossVo = new PrpLRegistCarLossVo();
                if(quickCaseInforVo.getCasecarno().equals(prplQuickCaseCar.getCasecarno())){
                    prpLRegistVo.setReportorName(prplQuickCaseCar.getCarownname());// 报案人姓名
                    // 事故责任划分
                    if("1".equals(prplQuickCaseCar.getDutytype())){
                        prpLRegistExt.setObliGation("4");
                    }
                    else{
                        prpLRegistExt.setObliGation(prplQuickCaseCar.getDutytype());
                    }
                    prpLRegistExt.setLicenseNo(prplQuickCaseCar.getCasecarno());
                    prpLRegistExt.setFrameNo(prplQuickCaseCar.getFrameno());
                    prpLRegistExt.setInsuredName(prplQuickCaseCar.getCarownname());
                    prpLRegistCarLossVo.setLossparty("1");// 损失方
                }
                else{
                    prpLRegistCarLossVo.setLossparty("3");// 损失方
                }
                // 车辆损失信息表

                prpLRegistCarLossVo.setLicenseNo(prplQuickCaseCar.getCasecarno());// 车牌号
                prpLRegistCarLossVo.setFrameNo(prplQuickCaseCar.getFrameno());// 车架号
                prpLRegistCarLossVo.setThriddrivername(prplQuickCaseCar.getCarownname());
                prpLRegistCarLossVo.setThriddriverphone(prplQuickCaseCar.getCarownphone());
                prpLRegistCarLossVo.setThriddrivingno(prplQuickCaseCar.getDriverlicence());
                // 保险公司
                if(StringUtils.isNotBlank(prplQuickCaseCar.getInscompany())){          
                    prpLRegistCarLossVo.setInspolicybi("DHIC");
                }
                if(StringUtils.isNotBlank(prplQuickCaseCar.getInscompany())){
                    prpLRegistCarLossVo.setInspolicyci("DHIC");
                }         
                prpLRegistCarLosses.add(prpLRegistCarLossVo);
            }
        }

        prpLRegistVo.setReportorPhone(quickCaseInforVo.getCasetelephone());// 报案人电话
        prpLRegistVo.setLinkerName(prpLRegistVo.getReportorName());// 联系人姓名
        prpLRegistVo.setDriverName(prpLRegistVo.getReportorName());//驾驶人姓名
        prpLRegistVo.setLinkerMobile(quickCaseInforVo.getCasetelephone());// 联系人电话
        prpLRegistVo.setDamageTime(quickCaseInforVo.getCasedate());// 出险时间
        prpLRegistVo.setReportTime(quickCaseInforVo.getCasedate());// 报案时间
        
        prpLRegistVo.setDamageCode("DM01");// 出险原因代码
        prpLRegistVo.setDamageAreaCode(StringUtils.substring(quickCaseInforVo.getAreaid(),0,6));// 出险区域编码
        prpLRegistVo.setDamageAddress(quickCaseInforVo.getCaseaddress());// 出险地址
        prpLRegistVo.setDamageMapCode(quickCaseInforVo.getCaselon()+","+quickCaseInforVo.getCaselat());// 出险地图编码

        prpLRegistExt.setCheckAddressCode(StringUtils.substring(quickCaseInforVo.getAreaid(),0,6));// 查勘区域编码
        prpLRegistExt.setCheckAddress(quickCaseInforVo.getCaseaddress());// 查勘地址
        prpLRegistExt.setCheckAddressMapCode(quickCaseInforVo.getCaselon()+","+quickCaseInforVo.getCaselat());// 查勘地区地图编号
        String[] accidentType = StringUtils.split(quickCaseInforVo.getAccidenttype(),",");
        StringBuffer registRemark = new StringBuffer();
        for(int i = 0; i<accidentType.length; i++ ){
           if(i>0){
        	   registRemark.append(",");
           }
           registRemark.append(codeTranService.transCode("accidenType",accidentType[i]));
        }
        prpLRegistExt.setRegistRemark(registRemark.toString());
        prpLRegistExt.setAccidentTypes("01");
        prpLRegistExt.setManageType("1");
        prpLRegistExt.setIsClaimSelf("0");
        prpLRegistExt.setIsSubRogation("0");
        //是否人伤默认为否
        prpLRegistExt.setIsPersonLoss("0");
        prpLRegistVo.setPrpLRegistExt(prpLRegistExt);
        prpLRegistExt.setDangerRemark(getDangerRemark(prpLRegistVo, prpLRegistCarLosses));
        prpLRegistVo.setPrpLRegistCarLosses(prpLRegistCarLosses);
        //拼装出险经过
        

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
    
    //无保单报案赋值现场
    private void setTmp(PrplQuickCaseInforVo quickCaseInforVo,PrpLTmpCMainVo tmpCMainVo,List<PrpLTmpCItemKindVo> tmpCItemKindVos,PrpLTmpCItemCarVo tmpCItemCarVo) {
        List<PrplQuickCaseCarVo> prplquickcasecars = quickCaseInforVo.getCasecarlist();
        if(prplquickcasecars!=null&&prplquickcasecars.size()>0){
            for(PrplQuickCaseCarVo prplQuickCaseCar:prplquickcasecars){
                if(quickCaseInforVo.getCasecarno().equals(prplQuickCaseCar.getCasecarno())){
                    tmpCMainVo.setInsuredName(prplQuickCaseCar.getCarownname());// 被保险人
                    tmpCItemCarVo.setFrameNo(prplQuickCaseCar.getFrameno());//车架号
                    tmpCItemCarVo.setLicenseNo(prplQuickCaseCar.getCasecarno());//车牌号
                }
            }
        }
        tmpCMainVo.setComCode("000000");
        tmpCMainVo.setDamageTime(quickCaseInforVo.getCasedate());//出险时间
        tmpCMainVo.setRiskCode("1101,1206");
    }
    
    private void submitWf(PrpLRegistVo prpLRegistVo,Boolean IsValid){
        WfTaskSubmitVo submitVo1 = new WfTaskSubmitVo();
        submitVo1.setFlowId(prpLRegistVo.getFlowId());
        submitVo1.setTaskInKey(prpLRegistVo.getRegistNo());
        submitVo1.setFlowTaskId(BigDecimal.ZERO);
        submitVo1.setComCode("00010095");
        submitVo1.setAssignUser("ANYONE");//报案案件查询特殊处理,taskIn表assignUser和handlerUser赋值为ANYONE,允许全部报案权限工号查询
        submitVo1.setTaskInUser("AUTO");
        submitVo1.setAssignCom("00010095");
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
        submitVo2.setComCode("00010095");
        submitVo2.setAssignUser("AUTO");
        submitVo2.setAssignCom("00010095");
        submitVo2.setSubmitType(SubmitType.N);       
        
        PrpLScheduleTaskVo scheduleTaskVo1 = new PrpLScheduleTaskVo();
        
        scheduleTaskVo1.setPosition("00010095");
        scheduleTaskVo1.setCreateUser("AUTO");
        scheduleTaskVo1.setCreateTime(new Date());
        scheduleTaskVo1.setUpdateUser("AUTO");
        scheduleTaskVo1.setUpdateTime(new Date());
        SysUserVo assUserVo = new SysUserVo();
        if(!IsValid){
        	//无保单的人员用轮询
        	SysUserVo userVo = new SysUserVo();
        	userVo.setUserCode("AUTO");
            assUserVo = assignService.execute(FlowNode.Sched,"00010095",userVo, "");
            String assUserCode = assUserVo.getUserCode();
            scheduleTaskVo1.setCreateUser(assUserCode);
            scheduleTaskVo1.setUpdateUser(assUserCode);
        }
        
        String url = SpringProperties.getProperty("MClaimPlatform_URL_IN")+AUTOSCHEDULE_URL_METHOD;
        try{
            registService.submitSchedule(registVo, submitVo2, scheduleTaskVo1,url);
        }
        catch(Exception e){
            logger.debug("报案提交调用方正客服系统失败"+e.getMessage());
            e.printStackTrace();
        }
        //提交工作流
        PrpLWfTaskVo taskVo = wfTaskHandleService.submitRegist(registVo,submitVo2);
        if(!IsValid){
        	//再去更新调度节点的处理人
        	try{
        		PrpLWfTaskVo prpLWfTaskVo = wfTaskHandleService.queryTask(taskVo.getTaskId().doubleValue());
    			if(prpLWfTaskVo == null){
    				List<PrpLWfTaskVo> prpLWfTaskVos = wfTaskHandleService.findWftaskByRegistNoAndNodeCode(taskVo.getRegistNo(),"Chk");
    				prpLWfTaskVo = prpLWfTaskVos.get(0);
    			}
    			String assUserCode = assUserVo.getUserCode();
    			prpLWfTaskVo.setTaskInUser(assUserCode);
    			prpLWfTaskVo.setAssignUser(assUserCode);
            	wfTaskHandleService.updateTaskIn(prpLWfTaskVo);
            	
            	if(StringUtils.isNotBlank(prpLWfTaskVo.getFlowId())){
            		List<PrpLWfTaskUserInfoVo> prpLWfTaskUserInfoVos = wfTaskHandleService.queryTaskUserInfo(prpLWfTaskVo.getFlowId());
                	if(prpLWfTaskUserInfoVos != null && prpLWfTaskUserInfoVos.size() > 0 ){
                		PrpLWfTaskUserInfoVo fTaskUserInfoVo = prpLWfTaskUserInfoVos.get(0);
                		fTaskUserInfoVo.setUserCode(assUserVo.getUserCode());
                		wfTaskHandleService.updateTaskUserInfo(fTaskUserInfoVo);
                	}
            	}
        	}catch(Exception e){
				logger.info("快处快赔系统发送案件信息给保险公司无保单更新调度接收人报错",e);
			}
		
        }
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
        
        
        //有效保单才能提交查勘start
        if(IsValid){
	        //调度到查勘
	        WfTaskSubmitVo submitVo3 = new WfTaskSubmitVo(); 
	        PrpLScheduleTaskVo scheduleTaskVo2 = scheduleTaskService.findScheduleTaskByRegistNo(prpLRegistVo.getRegistNo());
	        //设置查勘人员
	        PrplQuickUserVo prplQuickUserVo = quickUserService.findQuickUser();
	        scheduleTaskVo2.setScheduledComcode(prplQuickUserVo.getComCode());
	        scheduleTaskVo2.setScheduledComname(prplQuickUserVo.getComName());
	        scheduleTaskVo2.setScheduledUsercode(prplQuickUserVo.getUserCode());
	        scheduleTaskVo2.setScheduledUsername(prplQuickUserVo.getUserName());
	        scheduleTaskVo2.setRelateHandlerMobile(prplQuickUserVo.getPhone());
	        List<PrpLScheduleItemsVo> prpLScheduleItemses = scheduleTaskVo2.getPrpLScheduleItemses();
	
	        
	        submitVo3.setFlowId(taskVo.getFlowId());
	        submitVo3.setFlowTaskId(taskVo.getTaskId());
	        submitVo3.setTaskInKey(prpLRegistVo.getRegistNo());
	        submitVo3.setComCode("00010095");
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
	        //快赔人员任务接受次数+1
	        quickUserService.updateTimes(prplQuickUserVo);
	        //发送短信
	        SysUserVo sysUserVo = new SysUserVo();
	        sysUserVo.setUserCode("AUTO");
	        sysUserVo.setComCode("00010095");
	        scheduleTaskVo2.setRelateHandlerMobile(prplQuickUserVo.getPhone());
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
	            // TODO: handle exception
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
	                String urlfz = SpringProperties.getProperty("FOUNDER_URL");
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
    }
    
    
    //理赔调度提交/改派提交接口（理赔请求快赔系统）调度初始化或者新增查勘
    public void setReassignments(PrpLScheduleTaskVo prpLScheduleTaskVo,
            String checkAreaCode, String lngXlatY,String schType,List<PrpLScheduleItemsVo> prpLScheduleItemses) throws Exception {
        
        //PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
        //获取报案信息
        PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(prpLScheduleTaskVo.getRegistNo());
        PrpLCMainVo vo = prpLCMainService.findPrpLCMain(prpLScheduleTaskVo.getRegistNo(), prpLRegistVo.getPolicyNo());
        String[] code = areaDictService.findAreaByAreaCode(checkAreaCode,"");
        String provinceCode = code[0];
        String cityCode = code[1];
        String regionCode = checkAreaCode;
        
        HandleScheduleDOrGReqVo reqVo = new HandleScheduleDOrGReqVo();
        
        HeadReq head = setHeadReq();//设置头部信息
        
        HandleScheduleReqDOrGBody body = new HandleScheduleReqDOrGBody();
        HandleScheduleReqScheduleDOrG scheduleDOrG = new HandleScheduleReqScheduleDOrG();
        //是否移动端案件
        scheduleDOrG.setIsMobileCase("0");
        
        scheduleDOrG.setRegistNo(prpLRegistVo.getRegistNo());
        if("reassig".equals(schType)){
            scheduleDOrG.setScheduleType("1");
        }else{
            scheduleDOrG.setScheduleType("0");
        }
        scheduleDOrG.setDamageTime(prpLRegistVo.getDamageTime());
        scheduleDOrG.setDamagePlace(prpLScheduleTaskVo.getDamageAddress());
        if(vo!=null){
            scheduleDOrG.setInuredName(vo.getInsuredName());
        }else{
            scheduleDOrG.setInuredName(prpLRegistVo.getPrpLRegistExt().getInsuredName());
            /*if("1".equals(prpLRegistVo.getTempRegistFlag())){
                List<PrpLCMainVo> prpLCMainVos = policyViewService.getPolicyForPrpLTmpCMain(prpLScheduleTaskVo.getRegistNo());
                if(prpLCMainVos!=null && prpLCMainVos.size() > 0){
                    if(StringUtils.isNotBlank(prpLCMainVos.get(0).getInsuredName())){
                        scheduleDOrG.setInuredName(prpLCMainVos.get(0).getInsuredName());
                    }
                }
            }*/
        }
        scheduleDOrG.setInuredPhone(prpLRegistVo.getInsuredPhone());
        scheduleDOrG.setLicenseNo(prpLRegistVo.getPrpLRegistExt().getLicenseNo());
        scheduleDOrG.setLinkerName(prpLScheduleTaskVo.getLinkerMan());
        scheduleDOrG.setLinkerPhone(prpLScheduleTaskVo.getLinkerManPhone());
        //二期改造start
        if("1".equals(prpLRegistVo.getIsQuickCase())){
            scheduleDOrG.setPolicyNo(prpLRegistVo.getPolicyNo());
            scheduleDOrG.setBusiPolicyNo(prpLRegistVo.getPolicyNo());
        }else {
            List<PrpLCMainVo> cMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLRegistVo.getRegistNo());
            for(PrpLCMainVo cMainVo : cMainVoList){
                if("12".equals(cMainVo.getRiskCode().subSequence(0,2))){
                    scheduleDOrG.setBusiPolicyNo(cMainVo.getPolicyNo());
                }else{
                    scheduleDOrG.setPolicyNo(cMainVo.getPolicyNo());
                }
            }
        }
        
        //scheduleDOrG.setPolicyNo(prpLRegistVo.getPolicyNo());
        //二期改造end
        scheduleDOrG.setProvinceCode(provinceCode);
        scheduleDOrG.setCityCode(cityCode);
        scheduleDOrG.setRegionCode(regionCode);
        scheduleDOrG.setReportorName(prpLRegistVo.getReportorName());
        scheduleDOrG.setReportorPhone(prpLRegistVo.getReportorPhone());
        scheduleDOrG.setReportTime(prpLRegistVo.getReportTime());
        scheduleDOrG.setLngXlatY(lngXlatY != null ? lngXlatY : "");
        //设置自定义区域代码
        if(StringUtils.isNotEmpty(prpLScheduleTaskVo.getSelfDefinareaCode())){
            scheduleDOrG.setSelfDefinareaCode(prpLScheduleTaskVo.getSelfDefinareaCode());
        }
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
            if("reassig".equals(schType)){
                if(ItemsVo.getItemType().equals("4")){//人伤
                    scheduleItemDOrG.setTaskId(String.valueOf(id++));
                    scheduleItemDOrG.setNodeType("PLoss");
                    if("3".equals(scheduleDOrG.getCaseFlag())){
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
                    
                }else{//查勘
                    scheduleItemDOrG.setTaskId(scheduleTaskId);
                    scheduleItemDOrG.setNodeType("Check");
                    if("3".equals(scheduleDOrG.getCaseFlag())){
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
                    
                }
            }else{
                if(ItemsVo.getItemType().equals("4")){//人伤
                    scheduleItemDOrG.setTaskId(String.valueOf(id++));
                    scheduleItemDOrG.setNodeType("PLoss");
                    if("3".equals(scheduleDOrG.getCaseFlag())){
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
                }else{//查勘
                    scheduleItemDOrG.setTaskId(scheduleTaskId);
                    scheduleItemDOrG.setNodeType("Check");
                    if("3".equals(scheduleDOrG.getCaseFlag())){
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
                }
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
    
}
