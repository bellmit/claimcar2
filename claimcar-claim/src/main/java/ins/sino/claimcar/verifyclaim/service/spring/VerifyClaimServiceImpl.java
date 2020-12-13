/******************************************************************************
* CREATETIME : 2016年3月14日 下午3:55:48
******************************************************************************/
package ins.sino.claimcar.verifyclaim.service.spring;
import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.ConfigUtil;
import ins.platform.utils.DataUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.PrpLConfigValueVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.CancelType;
import ins.sino.claimcar.CodeConstants.FeeType;
import ins.sino.claimcar.CodeConstants.FlowStatus;
import ins.sino.claimcar.CodeConstants.PayStatus;
import ins.sino.claimcar.CodeConstants.PolicyType;
import ins.sino.claimcar.CodeConstants.RadioValue;
import ins.sino.claimcar.CodeConstants.VClaimType;
import ins.sino.claimcar.CodeConstants.VerifyClaimTask;
import ins.sino.claimcar.CodeConstants.WRITEOFFFLAG;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.carplatform.vo.CiClaimPlatformTaskVo;
import ins.sino.claimcar.certify.service.CertifyPubService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.claim.po.PrplPayeeKindPayment;
import ins.sino.claimcar.claim.service.ClaimCancelService;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.ClaimToReinsuranceService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.services.ClaimInvoiceService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.PrpLClaimKindFeeVo;
import ins.sino.claimcar.claim.vo.PrpLClaimKindVo;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateExtVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrpLPaymentVo;
import ins.sino.claimcar.claim.vo.PrpLPrePayVo;
import ins.sino.claimcar.claim.vo.PrpLcancelTraceVo;
import ins.sino.claimcar.claim.vo.PrpLrejectClaimTextVo;
import ins.sino.claimcar.claim.vo.PrplPayeeKindPaymentVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.po.PrpLEndCase;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.endcase.service.ReOpenCaseService;
import ins.sino.claimcar.endcase.vo.PrpLEndCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLReCaseVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionMainVo;
import ins.sino.claimcar.endcase.vo.PrpLuwNotionVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.constant.SubmitType;
import ins.sino.claimcar.flow.service.AssignService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfSimpleTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLCaseComponentVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarInfoVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.middlestagequery.service.ClaimToMiddleStageOfCaseService;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.payment.service.ClaimToPaymentDetailService;
import ins.sino.claimcar.payment.service.ClaimToPaymentService;
import ins.sino.claimcar.platform.service.SendCancelToPlatformService;
import ins.sino.claimcar.platform.service.SendCancelToSHPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToPlatformService;
import ins.sino.claimcar.platform.service.SendEndCaseToSHPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToPlatformService;
import ins.sino.claimcar.platform.service.SendVClaimToSHPlatformService;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.trafficplatform.service.SendEndCaseToEWPlatformService;
import ins.sino.claimcar.trafficplatform.service.SendVClaimToEWPlatformService;
import ins.sino.claimcar.verifyclaim.po.PrpLuwNotion;
import ins.sino.claimcar.verifyclaim.po.PrpLuwNotionMain;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import ins.sino.claimcar.verifyclaim.vo.VerifyClaimPassVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * 核赔服务类
 * @author ★Luwei
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("verifyClaimService")
public class VerifyClaimServiceImpl implements VerifyClaimService {

	private static Logger log = LoggerFactory.getLogger(VerifyClaimServiceImpl.class);
	
	@Autowired
	DatabaseDao databaseDao;
	
	@Autowired
	PolicyViewService policyViewService;
	
	@Autowired
	CheckTaskService checkTaskService;
	
	@Autowired
	RegistQueryService registQueryService;
	
	@Autowired
	PadPayPubService padPayPubService;
	
	@Autowired
	WfTaskHandleService wfTaskHandleService;
	
	@Autowired
	CompensateService compensateService;
	
	@Autowired
	LossCarService lossCarService;
	
	@Autowired
	EndCasePubService endCasePubService;
	
	@Autowired
	PropTaskService propTaskService;
	
    @Autowired
    PersTraceDubboService persTraceService;

    @Autowired
    ClaimCancelService claimCancelService;

    @Autowired
    ClaimTaskService claimTaskService;

    @Autowired
    SendVClaimToPlatformService sendVClaimToAll;

    @Autowired
    SendVClaimToSHPlatformService sendVClaimToSH;

    @Autowired
    SendEndCaseToPlatformService sendEndCaseToAll;

    @Autowired
    SendEndCaseToSHPlatformService sendEndCaseToSH;

    @Autowired
    SendCancelToSHPlatformService sendCancelToSH;

    @Autowired
    SendCancelToPlatformService sendCancelToAll;

    @Autowired
    AssignService assignService;

    @Autowired
    ClaimToReinsuranceService claimToReinsuranceService;

    @Autowired
    CompensateTaskService compensateTaskService;

    @Autowired
    ClaimToPaymentService claimToPaymentService;

    @Autowired
    ClaimToPaymentDetailService claimToPaymentDetailService;
    @Autowired
    PadPayService padPayService;

    @Autowired
    ClaimInvoiceService claimInvoiceService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    CheckHandleService checkHandleService;
    
    @Autowired
    CodeTranService codeTranService;
    
    @Autowired
    BaseDaoService baseDaoService;
    
    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    CertifyPubService certifyPubService;
    @Autowired
    WfFlowQueryService wfFlowQueryService;
    @Autowired
    ReOpenCaseService reOpenCaseService;
    @Autowired
    SendVClaimToEWPlatformService sendVClaimToEWPlatformService;
    @Autowired
    SendEndCaseToEWPlatformService sendEndCaseToEWPlatformService;
    @Autowired
    ClaimToMiddleStageOfCaseService claimToMiddleStageOfCaseService;
    
    @Override
    public List<PrpLCMainVo> initPolicyInfo(String registNo,String policyType) {
        return policyViewService.getPolicyAllInfo(registNo);
    }

    @Override
    public PrpLuwNotionMainVo queryUwNotion(String registNo,String compensateNo) {
        PrpLuwNotionMainVo mainVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("compensateNo",compensateNo);
        List<PrpLuwNotionMain> uwNotionPos = databaseDao.findAll(PrpLuwNotionMain.class,queryRule);
        if(uwNotionPos!=null&&uwNotionPos.size()>0){
            mainVo = new PrpLuwNotionMainVo();
            mainVo = Beans.copyDepth().from(uwNotionPos.get(0)).to(PrpLuwNotionMainVo.class);
        }
        return mainVo;
    }

    @Override
    public PrpLuwNotionMainVo findUwNotion(String registNo,String compeNo,String taskType) {
        PrpLuwNotionMainVo mainVo = null;
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("compensateNo",compeNo);
        queryRule.addEqual("policyType",taskType);
        List<PrpLuwNotionMain> uwNotionPos = databaseDao.findAll(PrpLuwNotionMain.class,queryRule);
        if(uwNotionPos!=null&&uwNotionPos.size()>0){
            mainVo = new PrpLuwNotionMainVo();
            mainVo = Beans.copyDepth().from(uwNotionPos.get(0)).to(PrpLuwNotionMainVo.class);
        }
        return mainVo;
    }

    @Override
    public PrpLuwNotionMainVo findUwNotionMainByRegistNo(PrpLWfTaskVo wfTaskVo) {
        String taskInNode = wfTaskVo.getTaskInNode();
        String key = wfTaskVo.getHandlerIdKey();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",wfTaskVo.getRegistNo());
        if(StringUtils.isNotEmpty(key)){
            queryRule.addEqual("compensateNo",key);
        }
        if( !FlowNode.VClaim.equals(taskInNode.substring(0,6))){
            queryRule.addEqual("taskInNode",taskInNode);
        }
        
        List<PrpLuwNotionMain> uwNotionPos = databaseDao.findAll(PrpLuwNotionMain.class,queryRule);
        List<PrpLuwNotionMainVo> uwNotionVos=new ArrayList<PrpLuwNotionMainVo>();
        if(uwNotionPos!=null && uwNotionPos.size()>0){
        	uwNotionVos=Beans.copyDepth().from(uwNotionPos).toList(PrpLuwNotionMainVo.class);
        }
        //专门为了处理历史数据的
        List<PrpLuwNotionVo> untionVos=new ArrayList<PrpLuwNotionVo>();
        Set<Date> set=new HashSet<Date>();
        PrpLuwNotionMainVo notionMainVo=null;
        if(uwNotionVos!=null && uwNotionVos.size()>0){
        	int index=0;
        	for(PrpLuwNotionMainVo vo :uwNotionVos){
        		if((set.add(vo.getCreateTime()))){
        			if(index<=0){
            			index++;
            			notionMainVo=new PrpLuwNotionMainVo();
            			notionMainVo=vo;
            			if(vo.getPrpLuwNotions()!=null && vo.getPrpLuwNotions().size()>0){
            				untionVos=vo.getPrpLuwNotions();
            			}
            			
                     }else{
                    	 if(vo.getPrpLuwNotions()!=null && vo.getPrpLuwNotions().size()>0){
                    		 untionVos.addAll(vo.getPrpLuwNotions());
                    	 }
                    	 
                     }
        		}
             }
        	
        	notionMainVo.setPrpLuwNotions(untionVos);
        }
        return notionMainVo;
    }

    @Override
    public PrpLuwNotionMainVo finduwNotionByPK(Long id) {
        PrpLuwNotionMainVo uwNotionMainVo = null;
        PrpLuwNotionMain uwNotionPo = databaseDao.findByPK(PrpLuwNotionMain.class,id);
        if(uwNotionPo!=null){
            uwNotionMainVo = Beans.copyDepth().from(uwNotionPo).to(PrpLuwNotionMainVo.class);
        }
        return uwNotionMainVo;
    }

    @Override
    public Long saveVerifyClaim(PrpLuwNotionMainVo uwNotionMainVo,PrpLuwNotionVo uwNotionVo,PrpLWfTaskVo wfTaskVo,String comCode,String userCode,String sign) {
        Date date = new Date();
        PrpLuwNotionMain mainPo = null;
        if(uwNotionMainVo.getId()==null){
            mainPo = new PrpLuwNotionMain();
            Beans.copy().from(uwNotionMainVo).to(mainPo);
            mainPo.setTaskInNode(wfTaskVo.getTaskInNode());
            mainPo.setHandleUser(userCode);
            mainPo.setHandleTime(date);
            mainPo.setHandleUser(userCode);
            mainPo.setHandleTime(date);
            mainPo.setValidFlag(CodeConstants.ValidFlag.VALID);
            mainPo.setCreateTime(date);
            mainPo.setCreateUser(userCode);
            mainPo.setUpdateTime(date);
            mainPo.setUpdateUser(userCode);
            mainPo.setRemark(uwNotionVo.getHandleText());
           if(sign.equals("0")){//自助核赔的时候走这里
            List<PrpLuwNotion> uwNotionList = new ArrayList<PrpLuwNotion>();
            PrpLuwNotion uwNotion = new PrpLuwNotion();
            Beans.copy().from(uwNotionVo).to(uwNotion);
            uwNotion.setPrpLuwNotionMain(mainPo);
            uwNotion.setAuditor(userCode);
            uwNotion.setComCode(comCode);
            uwNotion.setPubTime(date);
            uwNotion.setValidFlag(CodeConstants.ValidFlag.VALID);
            uwNotionList.add(uwNotion);
            mainPo.setPrpLuwNotions(uwNotionList);
           }
        }else{
            mainPo = databaseDao.findByPK(PrpLuwNotionMain.class,uwNotionMainVo.getId());
            if( !FlowNode.VClaim.equals(wfTaskVo.getTaskInNode().substring(0,6))){// 不是核赔节点需要更新taskInNode
                Beans.copy().from(uwNotionMainVo).excludeNull().to(mainPo);
                mainPo.setTaskInNode(wfTaskVo.getYwTaskType());
                mainPo.setHandleUser(userCode);
                mainPo.setHandleTime(date);
                mainPo.setCreateTime(date);
                mainPo.setCreateUser(userCode);
                mainPo.setUpdateTime(date);
                mainPo.setUpdateUser(userCode);
            }
            if(StringUtils.isNotBlank(wfTaskVo.getYwTaskType())){
                mainPo.setTaskInNode(wfTaskVo.getYwTaskType());
            }
            mainPo.setRecoveries(uwNotionMainVo.getRecoveries());
            mainPo.setUpdateTime(date);
            mainPo.setUpdateUser(userCode);
            mainPo.setRemark(uwNotionVo.getHandleText());
            if(sign.equals("0")){
                  PrpLuwNotion uwNotion = new PrpLuwNotion();
                  uwNotion.setPrpLuwNotionMain(mainPo);
                  uwNotion.setHandle(uwNotionVo.getHandle());
                  uwNotion.setHandleText(uwNotionVo.getHandleText());
                  uwNotion.setVerifyText(uwNotionVo.getVerifyText());
                  uwNotion.setAmount(uwNotionVo.getAmount());
                  uwNotion.setPrpLuwNotionMain(mainPo);
                  uwNotion.setAuditor(userCode);
                  uwNotion.setComCode(comCode);
                  uwNotion.setPubTime(date);
                  uwNotion.setValidFlag(CodeConstants.ValidFlag.VALID);
                  List<PrpLuwNotion> uwNotionList = new ArrayList<PrpLuwNotion>();
                  if(mainPo.getPrpLuwNotions()!=null){
                	  uwNotionList=mainPo.getPrpLuwNotions();
                	  uwNotionList.add(uwNotion);
                	  mainPo.setPrpLuwNotions(uwNotionList);
                  }
            }
           
        }
        return compensateService.saveorUpdateNotionMain(mainPo);
    }

    /**
     * 调用权限轮询
     * @param nextNode
     * @param comCode
     * @return SysUserVo
     * @modified: ☆Luwei(2016年12月20日 下午5:16:07): <br>
     */
    private SysUserVo getExecuteUser(FlowNode nextNode,String comCode,SysUserVo userVo) {
        SysUserVo sysVo = null;
        try{
            sysVo = assignService.execute(nextNode,comCode,userVo, "");
        }
        catch(Exception e){
            throw new IllegalArgumentException("提交至"+nextNode.getName()+"调用权限执行错误！请检查"+nextNode.getName()+"节点是否有配置人员、权限！");
        }
        return sysVo;
    }

    @Override
    public List<PrpLWfTaskVo> verifyClaimSubmit(PrpLWfTaskVo wfTaskVo,Long uwNotionMainId,String currentNode,String nextNode,String action,
                                                SysUserVo userVo,String compeWfZeroFlag) throws Exception {
		Long currentDate = System.currentTimeMillis();
		log.info("verifyClaimSubmit begin... registNo={}",wfTaskVo.getRegistNo());
        String comCode = userVo.getComCode();
        String userCode = userVo.getUserCode();
        List<PrpLWfTaskVo> wfTaskVoList = null;
        PrpLuwNotionMainVo uwNotionMainVo = findUwNotionMain(uwNotionMainId);
        //承保机构
        String comCodereg=policyViewService.getPolicyComCode(uwNotionMainVo.getRegistNo());
        if(StringUtils.isBlank(comCodereg)){
        	comCodereg=comCode;
        }
        if( !FlowNode.CancelAppJuPei.name().equals(nextNode)){
            if(isHeadOffice(nextNode)){
                comCode = "00010000";// 总公司人员 机构用公司本部
                comCodereg = "00010000";// 总公司人员 机构用公司本部
            }
        }
        // PrpLuwNotionMain uwTion = databaseDao.findByPK(PrpLuwNotionMain.class,uwNotionMainId);
        String policyCom = policyViewService.getPolicyComCode(uwNotionMainVo.getRegistNo());

        if(VClaimType.VC_RETURN.equals(action)){// 退回
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            // String[] nodeLevel = FlowNode.valueOf(nextNode).toString().split("_");
            submitVo.setSubmitType(SubmitType.B);
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setComCode(policyCom);
            submitVo.setTaskInUser(userCode);
            submitVo.setTaskInKey(uwNotionMainVo.getCompensateNo());
            String node = uwNotionMainVo.getTaskInNode();

            boolean val = "CancelAppJuPei".equals(node)||"ClaimBI".equals(node)||"ClaimCI".equals(node);
            boolean idKey = SubmitType.B==submitVo.getSubmitType()&&val;
            submitVo.setHandleIdKey(idKey ? uwNotionMainVo.getClaimNo() : uwNotionMainVo.getCompensateNo());
            if(FlowNode.PadPay.name().equals(node)&&FlowNode.PadPay.name().equals(nextNode)){
                submitVo.setHandleIdKey(uwNotionMainVo.getCompensateNo());
            }
            if("CancelAppJuPei".equals(node)){
                List<PrpLrejectClaimTextVo> prpLrejectClaimTextVoList = claimCancelService.findByOpinionCode("02",uwNotionMainVo.getClaimNo());
                if(prpLrejectClaimTextVoList != null && prpLrejectClaimTextVoList.size() > 0 ){
                    submitVo.setHandleIdKey(prpLrejectClaimTextVoList.get(0).getId().toString());
                }
            }
            submitVo.setAssignUser(userCode);
            submitVo.setAssignCom(comCode);
            submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
            submitVo.setNextNode(FlowNode.valueOf(nextNode));
            //ilog-------------->start=============
            List<WfTaskSubmitVo> submitVos = new ArrayList<WfTaskSubmitVo>();
            submitVos.add(submitVo);
            //除了退回单证跟定损，其它的退回走之前代码
            if(nextNode.equals("Certi") || nextNode.equals("DLCar")){
                //考虑理算提上来的核赔有两个的情况下，另外一个核赔也要退回
                String subNodeCode = node.contains("BI") ? "VClaim_CI" : "VClaim_BI";
                List<PrpLWfTaskVo> prpLWfTaskVos = wfFlowQueryService.findTaskVoForInByNodeCode(uwNotionMainVo.getRegistNo(), FlowNode.VClaim.toString());
                if(prpLWfTaskVos != null && prpLWfTaskVos.size() > 0 ){
                    for(PrpLWfTaskVo vo : prpLWfTaskVos){
                        if(vo.getSubNodeCode().contains(subNodeCode)){
                            WfTaskSubmitVo submitClaimVo = new WfTaskSubmitVo();
                            // String[] nodeLevel = FlowNode.valueOf(nextNode).toString().split("_");
                            submitClaimVo.setSubmitType(SubmitType.B);
                            submitClaimVo.setFlowId(vo.getFlowId());
                            submitClaimVo.setFlowTaskId(vo.getTaskId());
                            submitClaimVo.setComCode(vo.getComCode());
                            submitClaimVo.setTaskInUser(vo.getTaskInUser());
                            submitClaimVo.setTaskInKey(vo.getTaskInKey());
                            submitClaimVo.setHandleIdKey(vo.getHandlerIdKey());
                            submitClaimVo.setAssignUser(userCode);
                            submitClaimVo.setAssignCom(comCode);
                            submitClaimVo.setCurrentNode(FlowNode.valueOf(currentNode));
                            submitClaimVo.setNextNode(FlowNode.valueOf(nextNode));
                            submitVos.add(submitClaimVo);
                        }
                    }
                }
                wfTaskVo.setBackFlags("0");
                wfTaskVoList = wfTaskHandleService.submitVclaimLevelByIlog(wfTaskVo,submitVos,userVo);
                wfTaskHandleService.backToCertiOrDLCar(wfTaskVoList.get(0),submitVos,userVo);
                if(nextNode.equals("Certi")){
                    List<PrpLWfTaskVo> wfTaskCertiVoList = wfFlowQueryService.findPrpWfTaskVoForIn(wfTaskVoList.get(0).getRegistNo(),"Certi","Certi"); 
                    if(wfTaskCertiVoList != null && wfTaskCertiVoList.size() > 0){
                        for(PrpLWfTaskVo vo : wfTaskCertiVoList){
                            vo.setTaskInUser(userCode);
                            if(StringUtils.isNotBlank(vo.getHandlerUser())){
                                vo.setAssignUser(vo.getHandlerUser());
                            }
                            
                        }
                        wfTaskVoList = wfTaskCertiVoList;
                    }
                }else{
                    List<PrpLWfTaskVo> wfTaskCertiVoList = wfFlowQueryService.findPrpWfTaskVoForIn(wfTaskVoList.get(0).getRegistNo(),"DLoss","DLCarMod"); 
                    if(wfTaskCertiVoList != null && wfTaskCertiVoList.size() > 0){
                        wfTaskVoList = wfTaskCertiVoList;
                    }
                }
              //ilog-------------->end=============
            }else{//除了退回单证跟定损，其它的退回走之前代码
                wfTaskVoList = wfTaskHandleService.submitVclaimLevel(wfTaskVo,submitVo);
            }
        }
        else if(VClaimType.VC_AUDIT.equals(action)){// 提交上级
            SysUserVo sysVo = getExecuteUser(FlowNode.valueOf(nextNode),comCodereg,userVo);
            if(sysVo==null){
                throw new IllegalArgumentException("操作失败："+FlowNode.valueOf(nextNode).getName()+"节点没有配置人员！");
            }
            // WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
            // taskVo.setRegistNo(registNo);
            // taskVo.setHandlerIdKey(key);
            // taskVo.setItemName(wfTaskVo.getItemName());
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setComCode(policyCom);
            submitVo.setTaskInUser(userCode);
            submitVo.setTaskInKey(wfTaskVo.getTaskInKey());
            submitVo.setHandleIdKey(wfTaskVo.getHandlerIdKey());
            if(FlowNode.CancelAppJuPei.name().equals(wfTaskVo.getYwTaskType())){
                submitVo.setHandleIdKey(wfTaskVo.getClaimNo());
            }
            // 新改动，核赔提交上级至总公司 （ 到岗 ）
            if( !isHeadOffice(nextNode)){
                submitVo.setAssignCom(sysVo.getComCode());
                submitVo.setAssignUser(sysVo.getUserCode());
            }
            else{
                submitVo.setAssignCom("00010000");
                submitVo.setAssignUser(null);
            }
            submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
            submitVo.setNextNode(FlowNode.valueOf(nextNode));
            wfTaskVoList = wfTaskHandleService.submitVclaimLevel(wfTaskVo,submitVo);
        }
        else{// 核赔通过
             // this.VC_Adopt(wfTaskVo,recFlag,taskType,comCode,userCode,currentNode,nextNode);
            SysUserVo sysVo = getExecuteUser(FlowNode.valueOf(nextNode),comCodereg,userVo);
            String compNo = wfTaskVo.getHandlerIdKey();
            String claimNo = wfTaskVo.getClaimNo();
            WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
            submitVo.setFlowId(wfTaskVo.getFlowId());
            submitVo.setFlowTaskId(wfTaskVo.getTaskId());
            submitVo.setComCode(policyCom);
            submitVo.setTaskInUser(userCode);
            submitVo.setTaskInKey(compNo);
            submitVo.setHandleIdKey(claimNo);
            submitVo.setAssignUser(sysVo!=null ? sysVo.getUserCode() : userVo.getUserCode());
            submitVo.setAssignCom(sysVo!=null ? sysVo.getComCode() : userVo.getComCode());
            submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
            submitVo.setNextNode(FlowNode.valueOf(nextNode));

            // 提交工作流
            this.submitTaskToEndCase(uwNotionMainVo,submitVo,policyCom,userCode,wfTaskVo,action,userVo,compeWfZeroFlag);
        }
		log.info("verifyClaimSubmit end... registNo={}, cost time={} ms ",wfTaskVo.getRegistNo(),System.currentTimeMillis()-currentDate);
        return wfTaskVoList;
    }

    /**
     * 判断下一节点是否是总公司级别
     * @param nextNode
     * @return false-不是总公司级别，true-是
     * @modified: ☆Luwei(2016年12月20日 下午5:15:29): <br>
     */
    public boolean isHeadOffice(String nextNode) {
        boolean isHeadOffice = false;// 默认为否
        String[] nodeLevel = FlowNode.valueOf(nextNode).toString().split("LV");
        if(nodeLevel!=null&&nodeLevel.length>1){
            if(StringUtils.isNotBlank(nodeLevel[1])&&Integer.parseInt(nodeLevel[1])>8){
                isHeadOffice = true;
            }
        }
        return isHeadOffice;
    }

    /**
     * <pre>
     * 提交任务到结案,预付、垫付、拒赔-结束任务,理算核赔-自动结案
     * </pre>
     * @param taskType,registNo,compNo
     * @param submitVo,uwTion,recFlag,compeVo
     * @param comCode,userCode,claimNo,wfTaskVo
     * @param compeNo,action
     * @throws ParseException
     * @modified: ☆Luwei(2016年8月29日 上午10:54:19): <br>
     */
    private void submitTaskToEndCase(PrpLuwNotionMainVo uwNotionMainVo,WfTaskSubmitVo submitVo,String comCode,String userCode,PrpLWfTaskVo wfTaskVo,
                                     String action,SysUserVo userVo,String compeWfZeroFlag) throws Exception {
		Long currentDate = System.currentTimeMillis();
		log.info("submitTaskToEndCase begin... registNo={} ",wfTaskVo.getRegistNo());
        String registNo = uwNotionMainVo.getRegistNo();
        String policyNo = uwNotionMainVo.getPolicyNo();
        String taskType = uwNotionMainVo.getPolicyType();
        String compeNo = uwNotionMainVo.getCompensateNo();
        String recFlag = StringUtils.isEmpty(uwNotionMainVo.getRecoveries()) ? "0" : uwNotionMainVo.getRecoveries();
        PrpLCMainVo prpLCMainVo = policyViewService.getPolicyInfo(registNo, policyNo);
        PrpLCompensateVo compeVo = null;
		if (!VerifyClaimTask.PADPAY.equals(taskType) && !VerifyClaimTask.CANCEL.equals(taskType)) {
			compeVo = compensateTaskService.findPrpLCompensateVoByPK(compeNo);
		}
		if (!VerifyClaimTask.CANCEL.equals(taskType)) {
            // 先回写,目前由于垮项目的事务问题,无法回滚--把回写执行在提交工作流之前
            this.writeBack(wfTaskVo,taskType,uwNotionMainVo.getTaskInNode(),compeNo,action,recFlag,userCode,uwNotionMainVo.getClaimNo(),userVo,
                    compeWfZeroFlag);
            // 工作流结束，预付、垫付、拒赔-结束任务
			if (!VerifyClaimTask.COMPE_CI.equals(taskType) && !VerifyClaimTask.COMPE_BI.equals(taskType)) {
				this.endTask(registNo, compeNo, uwNotionMainVo.getClaimNo(), submitVo, "预付/垫付核赔");
			} else { // 处理理算、理算冲销核赔任务

                String taskInNode = uwNotionMainVo.getTaskInNode();
				if ("1".equals(compeWfZeroFlag)) {
					taskInNode = "CompeWfZero";
				}
                // 交强理算冲销核赔,商业理算冲销核赔-结束任务
				if (FlowNode.CompeWfBI.equals(taskInNode) || FlowNode.CompeWfCI.equals(taskInNode)) {
                    this.endTask(registNo,compeNo,uwNotionMainVo.getClaimNo(),submitVo,"理算冲销核赔");
                } else { // 正常理算提交
                    if(RadioValue.RADIO_YES.equals(recFlag)){// 发起追偿
                        FlowNode[] flowNode = new FlowNode[1];
                        flowNode[0] = FlowNode.RecPay;
                        submitVo.setOthenNodes(flowNode);
                    }

                    List<PrpLWfTaskVo> wfTaskVos = null;
                    try{
                        // 核赔提交结案（核赔提交工作流产生结案任务）
                        wfTaskVos = wfTaskHandleService.submitVclaim(compeVo,submitVo);
                        // 理算核赔通过后送平台,送再保
                        verifyToPlatform(compeVo,uwNotionMainVo.getTaskInNode());    		
                        
                        // 自动结案（结案提交） (--结案失败回滚核赔的工作流，用户可以重新提交--)
                        PrpLEndCaseVo caseVo = null;//endWfTaskVo
                        caseVo = autoEndCase(wfTaskVos,compeVo,registNo,compeWfZeroFlag,comCode,userCode);

						if(SendPlatformUtil.isMor(prpLCMainVo)){
                        	//山东预警
							String warnswitch = SpringProperties.getProperty("WARN_SWITCH");// 62,10,50
							if(warnswitch.contains(prpLCMainVo.getComCode().substring(0,2))){// prpLCMainVo.getComCode().startsWith("62")
                            	try{
                            		//理算核赔送山东预警
                    				sendVClaimToEWPlatformService.SendVClaimToEWPlatform(compeVo);
                    				//结案核赔送山东预警
                    				sendEndCaseToEWPlatformService.SendEndCaseToEWPlatform(caseVo);
                            	}catch(Exception e) {
									log.info("预警核赔结案异常信息-------------->", e);
                    				e.printStackTrace();
                            	}
    	                        PrpLCertifyMainVo prpLCertifyMainVo = certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
    	                        if((compeVo.getRiskCode().startsWith(PolicyType.POLICY_DAA)&&"1".equals(prpLCertifyMainVo.getIsSYFraud()))||
    	                        		(compeVo.getRiskCode().startsWith(PolicyType.POLICY_DZA)&&"1".equals(prpLCertifyMainVo.getIsJQFraud()))	){
	                    			try {
	                    				//结案拒赔送山东预警
	                    				interfaceAsyncService.sendCaseCancleRegister(prpLCMainVo, "caseRefuse", prpLCertifyMainVo.getNewNotpaycause(), userVo);
	                    			} catch (Exception e) {
	                    				log.info("山东预警案件注销异常信息-------------->", e);
	                    				e.printStackTrace();
	                    			}
	                    			if("3".equals(prpLCertifyMainVo.getNewNotpaycause())){
	                    				try {
	                        				//如果是重复/虚假案件则要送山东预警
	                        				interfaceAsyncService.sendFalseCaseToEWByCancel(wfTaskVo.getHandlerIdKey());
	                    				} catch (Exception e) {
	                    					log.info("重复/虚假案件标记送山东预警异常信息-------------->", e);
	                    					e.printStackTrace();
	                    				}
	                    			}
    	                    	}
                            }
                        }
                    } catch(Exception e) {
                        log.info("核赔提交结案报错================================", e);
                        e.printStackTrace();
                        // 回滚工作流
                        // 1,回滚核赔
						if (wfTaskVos != null && !wfTaskVos.isEmpty()) {
							for (PrpLWfTaskVo taskVo : wfTaskVos) {
								wfTaskHandleService.rollBackTask(taskVo);
							}
						}
                        throw new IllegalArgumentException("理算核赔提交失败！", e);
                    }

                }
            }
        } else {// 拒赔核赔
            String claimNo = uwNotionMainVo.getClaimNo();

            // 回写注销、拒赔
            this.writeBackCancel(claimNo,action);

            submitVo.setNextNode(FlowNode.END);
            WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
            taskVo.setRegistNo(registNo);
            taskVo.setHandlerIdKey(submitVo.getHandleIdKey());
            taskVo.setItemName("拒赔核赔");
            taskVo.setClaimNo(wfTaskVo.getClaimNo());
            setFlowStatusByCancel(submitVo,registNo,claimNo);
            wfTaskHandleService.submitSimpleTask(taskVo,submitVo);

            // 挂起工作流
            // 判断立案是否可以全部注销其它节点
            cancleClaimForOther(registNo,userVo.getUserCode());

            // 拒赔核赔通过，相当于立案注销，送平台
            sendCancelToPlatform(claimNo);
            
            //山东预警
    		if(prpLCMainVo.getComCode().startsWith("62")){
    			try {
    				//结案拒赔送山东预警
    				PrpLcancelTraceVo cancelTraceVo = claimCancelService.findByClaimNo(claimNo);
    				interfaceAsyncService.sendCaseCancleRegister(prpLCMainVo, "caseRefuse",cancelTraceVo.getApplyReason(), userVo);
    			} catch (Exception e) {
    				log.info("山东预警案件注销异常信息-------------->", e);
    				e.printStackTrace();
    			}
    			try {
    				//如果是重复/虚假案件则要送山东预警
    				interfaceAsyncService.sendFalseCaseToEWByCancel(submitVo.getHandleIdKey());
				} catch (Exception e) {
					log.info("重复/虚假案件标记送山东预警异常信息-------------->", e);
					e.printStackTrace();
				}
    		}
        }
		log.info("submitTaskToEndCase end... registNo={}, cost time={} ms ",wfTaskVo.getRegistNo(),System.currentTimeMillis()-currentDate);
    }

    /**
     * <pre>
     * 自动结案
     * </pre>
     * @param wfTaskVos,compeVo,registNo
     * @param compeWfZeroFlag,comCode,userCode
     * @throws ParseException
     * @modified: ☆Luwei(2016年12月20日 下午5:13:57): <br>
     */
    private PrpLEndCaseVo autoEndCase(List<PrpLWfTaskVo> wfTaskVos,PrpLCompensateVo compeVo,String registNo,String compeWfZeroFlag,String comCode,
                                     String userCode)  {
		Long currentDate = System.currentTimeMillis();
		log.info("autoEndCase begin... registNo={} ",compeVo.getRegistNo());
        PrpLEndCaseVo endCaseVo = null;
        // 自动结案
        for(PrpLWfTaskVo wfTask:wfTaskVos){
            if(FlowNode.EndCas.toString().equals(wfTask.getSubNodeCode())){

                // 理算冲销0结案对compVo做特殊处理，再生成结案表
                if("1".equals(compeWfZeroFlag)){
                    // 结案表不存储计算书号
                    compeVo.setCompensateNo("CompeWfZero");
                    // 主表金额字段清0
                    compeVo.setSumAmt(BigDecimal.ZERO);
                    compeVo.setSumFee(BigDecimal.ZERO);
                    compeVo.setSumPaidAmt(BigDecimal.ZERO);
                    compeVo.setSumPaidFee(BigDecimal.ZERO);
                    compeVo.setSumBzPaid(BigDecimal.ZERO);
                    compeVo.setSumLoss(BigDecimal.ZERO);
                    // 子表数据清空
                    compeVo.setPrpLCharges(null);
                    compeVo.setPrpLPayments(null);
                    compeVo.setPrpLCompensateDefs(null);
                    compeVo.setPrpLLossItems(null);
                    compeVo.setPrpLLossProps(null);
                    compeVo.setPrpLLossPersons(null);
                    compeVo.setPrpLCompensateExt(null);

                }
                // 生成结案表
                endCaseVo = endCasePubService.autoEndCase(wfTask,compeVo);
                //零赔付计算书（本次赔款+本次费用 = 0）的支付时间回写为结案时间
                BigDecimal sumPay = compeVo.getSumPaidAmt().add(compeVo.getSumPaidFee());
                if(!"1".equals(compeWfZeroFlag)&&sumPay.compareTo(BigDecimal.ZERO)==0){
                	compensateService.updateCompPayTime(compeVo,endCaseVo.getEndCaseDate(),PayStatus.PAID);
                }

                // 结案送平台
                try{
                	//平台优化开关
                	PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PlatformOptimized,comCode);
                	if("1".equals(configValueVo.getConfigValue())){
                        // 异步送平台
                		sendEndCaseToAll.savePlatformTask(endCaseVo);
                	}else{
                		endCaseToPlatform(endCaseVo);
                	}
                } catch (Exception e) {
                    log.info("结案送平台失败！", e);
                    e.printStackTrace();
                    throw new IllegalArgumentException("结案送平台失败！"+e.getMessage());
                }
				log.info("autoEndCase endCaseToPlatform end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),
						System.currentTimeMillis()-currentDate);
            }
        }
        // 结案送再保 mfn 2019-11-18 21:24:48
        sendEndCaseToReins(endCaseVo.getClaimNo());
        // 结案之后送收付
        if( !RadioValue.RADIO_YES.equals(compeWfZeroFlag)){
            sendToPayment(compeVo,registNo);
        }
		log.info("autoEndCase sendToPayment end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),System.currentTimeMillis()-currentDate);
        
        SysUserVo userVo=new SysUserVo();
        userVo.setUserCode(userCode);
        userVo.setComCode(comCode);
        //商业保险/交强保险理赔信息写入接口
        List<PrpLWfTaskVo> prpLWfTaskVos= wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo, FlowNode.Chk.name());
        String scHanderUser=SpringProperties.getProperty("SC_HANDERUSER");//四川机构处理人
        if(prpLWfTaskVos!=null && prpLWfTaskVos.size()>0){
        	for(PrpLWfTaskVo taskVo:prpLWfTaskVos){
        		if("3".equals(taskVo.getWorkStatus())){
        			if(StringUtils.isNotBlank(scHanderUser) && StringUtils.isNotBlank(taskVo.getHandlerUser())){
        				if(scHanderUser.contains(taskVo.getHandlerUser())){
        					if("1101".equals(endCaseVo.getRiskCode())){
        			        	interfaceAsyncService.policyInfoRegister(registNo, "C", userVo);//交强
        			        }else{
        			        	interfaceAsyncService.policyInfoRegister(registNo, "B", userVo);//商业
        			        }
        				}
        			}
        		}
        		break;
        	}
        }
        
        //河南快赔案件将理赔结果通知河南快赔系统--CancelAppJuPei
        PrpLRegistVo registVo= registQueryService.findByRegistNo(registNo);
        List<PrpLCMainVo> prplcmainList=policyViewService.getPolicyAllInfo(registNo);//报案的保单信息
        List<PrpLReCaseVo> recaseList=endCasePubService.findReCaseListByClaimNo(compeVo.getClaimNo());
        Double sumMoney=sumpay(registNo,compeVo);
        if(registVo!=null && "1".equals(registVo.getIsQuickCase())){
        //单交强或单商业报案，重开前的理赔信息与河南快赔系统交互
         if(prplcmainList!=null && prplcmainList.size()==1 && (recaseList==null || recaseList.size()==0)){
        	  if(compeVo!=null){
             	PrpLCertifyMainVo prpLCertifyMainVo=certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
             	if(prpLCertifyMainVo!=null && "1101".equals(compeVo.getRiskCode()) && "1".equals(prpLCertifyMainVo.getIsJQFraud())){
             		    interfaceAsyncService.receivecpsresult(registNo,"2", userVo);//案件状态为拒赔
             	}else if(prpLCertifyMainVo!=null && "1101".equals(compeVo.getRiskCode()) && !"1".equals(prpLCertifyMainVo.getIsJQFraud())){
             		if(sumMoney.doubleValue()==0.0){
             			interfaceAsyncService.receivecpsresult(registNo,"3", userVo);//案件状态为零结
             		}else{
             			interfaceAsyncService.receivecpsresult(registNo,"1", userVo);//案件状态为结案
             		}
             	}else if(prpLCertifyMainVo!=null && !"1101".equals(compeVo.getRiskCode()) && "1".equals(prpLCertifyMainVo.getIsSYFraud())){
             		    interfaceAsyncService.receivecpsresult(registNo,"2", userVo);//案件状态为拒赔
             	}else if(prpLCertifyMainVo!=null && !"1101".equals(compeVo.getRiskCode()) && !"1".equals(prpLCertifyMainVo.getIsSYFraud())){
             		if(sumMoney.doubleValue()==0.0){
             			interfaceAsyncService.receivecpsresult(registNo,"3", userVo);//案件状态为零结
             		}else{
             			interfaceAsyncService.receivecpsresult(registNo,"1", userVo);//案件状态为结案
             		}
             	}
               }
        	  //交强商业一起报案，重开前的理赔信息与河南快赔系统交互，只送交强险信息
        	}else if(prplcmainList!=null && prplcmainList.size()>1 && (recaseList==null || recaseList.size()==0)){
        		 if(compeVo!=null){
                  	PrpLCertifyMainVo prpLCertifyMainVo=certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
                  	if(prpLCertifyMainVo!=null && "1101".equals(compeVo.getRiskCode()) && "1".equals(prpLCertifyMainVo.getIsJQFraud())){
                  		    interfaceAsyncService.receivecpsresult(registNo,"2", userVo);//案件状态为拒赔
                  	}else if(prpLCertifyMainVo!=null && "1101".equals(compeVo.getRiskCode()) && !"1".equals(prpLCertifyMainVo.getIsJQFraud())){
                  		if(sumMoney.doubleValue()==0.0){
                  			interfaceAsyncService.receivecpsresult(registNo,"3", userVo);//案件状态为零结
                  		}else{
                  			interfaceAsyncService.receivecpsresult(registNo,"1", userVo);//案件状态为结案
                  		}
                  	}
        		 }
        	}
        }
		log.info("autoEndCase interfaceAsyncService.receivecpsresult end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),
				System.currentTimeMillis()-currentDate);
        if(registVo!=null && "1".equals(registVo.getSelfClaimFlag())){
            //理赔信息与自助理赔系统交互 
             if(endCaseVo!=null){
                 	PrpLCertifyMainVo prpLCertifyMainVo=certifyPubService.findPrpLCertifyMainVoByRegistNo(registNo);
                 	if(prpLCertifyMainVo!=null && "1101".equals(endCaseVo.getRiskCode()) && "1".equals(prpLCertifyMainVo.getIsJQFraud())){
                 		    interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"2","3",endCaseVo.getPolicyNo());//案件状态为拒赔
                 	}else if(prpLCertifyMainVo!=null && "1101".equals(endCaseVo.getRiskCode()) && !"1".equals(prpLCertifyMainVo.getIsJQFraud())){
                 		if(sumMoney!=null && sumMoney.doubleValue()==0.0){
                 			interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"3","3",endCaseVo.getPolicyNo());//案件状态为零结
                 		}else{
                 			interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"1","3",endCaseVo.getPolicyNo());//案件状态为结案
                 		}
                 	}else if(prpLCertifyMainVo!=null && !"1101".equals(endCaseVo.getRiskCode()) && "1".equals(prpLCertifyMainVo.getIsSYFraud())){
                 		    interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"2","3",endCaseVo.getPolicyNo());//案件状态为拒赔
                 	}else if(prpLCertifyMainVo!=null && !"1101".equals(endCaseVo.getRiskCode()) && !"1".equals(prpLCertifyMainVo.getIsSYFraud())){
                 		if( sumMoney!=null && sumMoney.doubleValue()==0.0){
                 			interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"3","3",endCaseVo.getPolicyNo());//案件状态为零结
                 		}else{
                 			interfaceAsyncService.sendClaimResultToSelfClaim(registNo,userVo,"1","3",endCaseVo.getPolicyNo());//案件状态为结案
                 		}
                 	}
                 }
        }
		log.info("autoEndCase end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),System.currentTimeMillis()-currentDate);
        return endCaseVo; 
 
    }

    private void sendToPayment(PrpLCompensateVo compeVo,String registNo) {
        // 结案之后送收付
        try{
            PrpLCompensateVo compensateVo = compensateService.findCompByPK(compeVo.getCompensateNo());
            PrpLRegistVo registVo = registQueryService.findByRegistNo(registNo);
            String paicReportNo = registVo.getPaicReportNo();
            if(StringUtil.isNotBlank(paicReportNo)){
                interfaceAsyncService.compensateToPaymentPingAn(compeVo,registNo);
            }else{
                interfaceAsyncService.compensateToPayment(compensateVo);
            }
            // 预付数据更新接口
            // interfaceAsyncService.updatePrePayToPayment(compensateVo);
        }
        catch(Exception e){
			log.error("结案之后送收付失败！",e);  
            throw new IllegalArgumentException("结案之后送收付失败！"+e.getMessage());
        }
    }

    /**
     * 设值
     * @param submitVo,registNo,claimNo
     * @modified: ☆Luwei(2016年12月20日 下午5:14:29): <br>
     */
    private void setFlowStatusByCancel(WfTaskSubmitVo submitVo,String registNo,String claimNo) {
        List<PrpLClaimVo> prpLClaimVoList = claimTaskService.findClaimListByRegistNo(registNo);
        if(prpLClaimVoList!=null&&prpLClaimVoList.size()>1){// 判断另一个立案是否结案或者注销拒赔
            for(PrpLClaimVo prpLClaimVo:prpLClaimVoList){
                if( !prpLClaimVo.getClaimNo().equals(claimNo)){
                    if(( prpLClaimVo.getEndCaseTime()!=null&&StringUtils.isNotBlank(prpLClaimVo.getEndCaserCode())&&StringUtils
                            .isNotBlank(prpLClaimVo.getCaseNo()) )||( "2".equals(prpLClaimVo.getCancelCode())||"0"
                            .equals(prpLClaimVo.getCancelCode()) )){
                        submitVo.setFlowStatus(FlowStatus.END);
                    }
                }
            }
        }
        else if(prpLClaimVoList!=null&&prpLClaimVoList.size()==1){
            submitVo.setFlowStatus(FlowStatus.END);
        }
    }

    /**
     * 结束一个任务
     * @param registNo,compeNo
     * @param submitVo,itemName
     */
    private void endTask(String registNo,String compeNo,String claimNo,WfTaskSubmitVo submitVo,String itemName) {
        submitVo.setNextNode(FlowNode.END);
        WfSimpleTaskVo taskVo = new WfSimpleTaskVo();
        taskVo.setRegistNo(registNo);
        taskVo.setHandlerIdKey(compeNo);
        taskVo.setItemName(itemName);
        taskVo.setClaimNo(claimNo);
        wfTaskHandleService.submitSimpleTask(taskVo,submitVo);
    }

    /**
     * <pre>
     * 核赔任务通过回写-各节点标志位,
     * </pre>
     * @param taskType
     * @param taskInNode
     * @modified: ☆Luwei(2016年8月29日 上午10:42:15): <br>
     */
    private void writeBack(PrpLWfTaskVo wfTaskVo,String taskType,String taskInNode,String compeNo,String action,String recFlag,String userCode,
                           String claimNo,SysUserVo userVo,String compeWfZeroFlag) {
		Long currentDate = System.currentTimeMillis();
		log.info("writeBack begin... registNo={} ",wfTaskVo.getRegistNo());
        String registNo = wfTaskVo.getRegistNo();
        if("1".equals(compeWfZeroFlag)){
            taskInNode = "CompeWfZero";
        }
        if(VerifyClaimTask.COMPE_CI.equals(taskType)||VerifyClaimTask.COMPE_BI.equals(taskType)){// 理算
            // 1-交强理算核赔回写、交强理算冲销核赔回写，2-商业理算核赔回写、商业理算冲销核赔回写，
            this.writeBackDLossCar(registNo,taskType,taskInNode,action,claimNo);// 回写车辆定损
            this.writeBackDLossProp(registNo,taskType,taskInNode,action,claimNo);// 回写财产定损
            this.writeBackDLossPerson(registNo,taskType,taskInNode,action,claimNo);// 回写人伤跟踪
            this.writeBackCompesate(compeNo,taskType,action,recFlag,userCode,userVo);// 回写理算表数据
            // this.writeBackIsCompdeDuct(compeNo,action,userCode);

            // 商业理算、商业理算冲销写入冲减保额表
            if(VerifyClaimTask.COMPE_BI.equals(taskType)&&VClaimType.VC_ADOPT.equals(action)&& !"CompeWfZero".equals(taskInNode)){
                compensateService.savePrpLEndor(compeNo,userVo);
            }

            // 回写立案
			try {
				// 核赔审核通过、理算冲销核赔通过（PrpLClaimKind，PrpLClaimFee）
				wirteBackClaim(claimNo, taskInNode, userCode);
				if ("CompeWfZero".equals(taskInNode)) {
					// 理算冲销0结案刷立案方法单独写
					claimTaskService.updateClaimAfterCompeWfZero(compeNo, userCode, FlowNode.VClaim);
				}
				if (FlowNode.CompeBI.equals(taskInNode) || FlowNode.CompeCI.equals(taskInNode)) {
					// 理算核赔通过后刷新立案估损金额
					claimTaskService.updateClaimAfterCompe(compeNo, userCode, FlowNode.VClaim);
				}
			} catch (Exception e) {
				log.info("理算核赔通过后刷新立案估损金额失败！\n", e);
				e.printStackTrace();
				throw new IllegalArgumentException("理算核赔通过后刷新立案估损金额失败！<br/>" + e);
			}
        }
        else if(VerifyClaimTask.PREPAY_CI.equals(taskType)||VerifyClaimTask.PREPAY_BI.equals(taskType)){// 预付
            // 3-交强预付、冲销核赔，4-商业预付、冲销核赔
            this.writeBackCompesate(compeNo,taskType,action,recFlag,userCode,userVo);
            // this.writeBackIsCompdeDuct(compeNo,action,userCode);
            compensateService.savePrpLEndor(compeNo,userVo);

        }
        else if(VerifyClaimTask.PADPAY.equals(taskType)){// 垫付核赔任务，任务通过回写垫付
            this.writeBackPadPay(registNo,action,userCode);

        }
        else{// 注销、拒赔任务--回写
            this.writeBackCancel(wfTaskVo.getClaimNo(),action);
        }
		log.info("writeBack end... registNo={}, cost time={} ms ",wfTaskVo.getRegistNo(),System.currentTimeMillis()-currentDate);
    }

    // 1--回写车辆定损
    private void writeBackDLossCar(String registNo,String taskType,String taskInNode,String vc_action,String claimNo) {
        List<PrpLDlossCarMainVo> lossCarVos = lossCarService.findLossCarMainByRegistNo(registNo);
        if(lossCarVos!=null&&lossCarVos.size()>0){
            for(PrpLDlossCarMainVo lossCarVo:lossCarVos){
                String lossState = StringUtils.isBlank(lossCarVo.getLossState()) ? "00" : lossCarVo.getLossState();
                String state = this.getLossState(lossState,taskType,taskInNode,vc_action,claimNo);
                lossCarVo.setLossState(state);
                // 更新
                lossCarService.updateDlossCarMain(lossCarVo);

                // 核赔通过创建prplcasecomponent表
                List<PrpLCaseComponentVo> compVoList = new ArrayList<PrpLCaseComponentVo>();

                String frameNo = lossCarVo.getLossCarInfoVo().getFrameNo();
                List<PrpLDlossCarCompVo> dlossCarCompeVoList = lossCarVo.getPrpLDlossCarComps();
                if(dlossCarCompeVoList!=null&&dlossCarCompeVoList.size()>0){
                    for(PrpLDlossCarCompVo vo:dlossCarCompeVoList){
                        // 创建prplcasecomponent表
                        if("0".equals(vo.getSelfConfigFlag())){// 标准 点选则统计
                            PrpLCaseComponentVo caseVo = new PrpLCaseComponentVo();
                            caseVo.setCompCode(vo.getCompCode());
                            caseVo.setCompName(vo.getCompName());
                            caseVo.setFrameNo(frameNo);
                            caseVo.setRegistNo(registNo);
                            compVoList.add(caseVo);
                        }
                    }
                    lossCarService.createCaseComponent(compVoList);
                }
            }
        }
    }

    // 2--回写财产定损
    private void writeBackDLossProp(String registNo,String taskType,String taskInNode,String vc_action,String claimNo) {
        List<PrpLdlossPropMainVo> dlossPropVos = propTaskService.findPropMainListByRegistNo(registNo);
        if(dlossPropVos!=null&&dlossPropVos.size()>0){
            for(PrpLdlossPropMainVo dlossPropVo:dlossPropVos){
                String lossState = StringUtils.isBlank(dlossPropVo.getLossState()) ? "00" : dlossPropVo.getLossState();
                String state = this.getLossState(lossState,taskType,taskInNode,vc_action,claimNo);
                dlossPropVo.setLossState(state);
                // 更新
                propTaskService.updateDLossProp(dlossPropVo);
            }
        }
    }

    // 3--回写人伤定损
    private void writeBackDLossPerson(String registNo,String taskType,String taskInNode,String vc_action,String claimNo) {
        List<PrpLDlossPersTraceMainVo> DlossPersTraceVos = persTraceService.findPersTraceMainVoList(registNo);
        if(DlossPersTraceVos!=null&&DlossPersTraceVos.size()>0){
            for(PrpLDlossPersTraceMainVo DlossPersTraceVo:DlossPersTraceVos){
                String lossState = StringUtils.isBlank(DlossPersTraceVo.getLossState()) ? "00" : DlossPersTraceVo.getLossState();
                String state = this.getLossState(lossState,taskType,taskInNode,vc_action,claimNo);
                DlossPersTraceVo.setLossState(state);
                // 更新
                persTraceService.writeBackDLossPerson(DlossPersTraceVo);
            }
        }
    }

    // 4--回写理算
    private void writeBackCompesate(String compeNo,String taskType,String vc_action,String recFlag,String userCode,SysUserVo userVo) {
    	log.info("计算书号compeNo={},进入核赔通过回写方法",compeNo);
        PrpLCompensateVo compensateVo = compensateTaskService.queryCompByPK(compeNo);
    	log.info("计算书号compeNo={},根据核赔类型赋值核赔状态,核赔类型vc_action={}",compeNo,vc_action);
        compensateVo.setUnderwriteFlag(VClaimType.VC_ADOPT.equals(vc_action) ? "1" : "9");// 核赔审核通过
        compensateVo.setUnderwriteDate(new Date());// 核赔通过时间
        compensateVo.setUnderwriteUser(userCode);// 核赔处理人
        compensateVo.setRecoveryFlag(recFlag);// 是否发起追偿

        // 否理算扣减
        PrpLCompensateExtVo compeExtVo = compensateVo.getPrpLCompensateExt();
        if(compeExtVo!=null){
            boolean flag = VClaimType.VC_ADOPT.equals(vc_action);
            compeExtVo.setPayBackState(flag ? "1" : "0");
            // compeExtVo.setOppoCompensateNo(compeNo);// 写入理算或预付冲销的计算书号
            compensateVo.setPrpLCompensateExt(compeExtVo);
        }

        compensateService.saveOrUpdateCompensateVo(compensateVo,userVo);
        if(VerifyClaimTask.COMPE_CI.equals(taskType)||VerifyClaimTask.COMPE_BI.equals(taskType)){// 理算
            // 冲销计算书 回写原计算书任务
            if(WRITEOFFFLAG.ALLOFF.equals(compeExtVo.getWriteOffFlag())&&compeExtVo.getOppoCompensateNo()!=null){// 全部冲销

                PrpLCompensateVo compOriginVo = compensateService.findCompByPK(compeExtVo.getOppoCompensateNo());
                PrpLCompensateExtVo comExtVo = compOriginVo.getPrpLCompensateExt();
                comExtVo.setOppoCompensateNo(compeNo);
                compensateService.saveOrUpdateCompensateVo(compOriginVo,userVo);

                // TODO 如果之后考虑部分冲销 设计方式会有点问题 需要回写子表
                BigDecimal sumPre = DataUtils.NullToZero(compOriginVo.getSumPreAmt()).add(DataUtils.NullToZero(compOriginVo.getSumPreFee()));
                if(sumPre.compareTo(BigDecimal.ZERO)==1){// 原计算书中存在扣减的垫付和预付费用
                    List<PrpLCompensateVo> preCompList = compensateService.findNormalCompListByClaimNo(compensateVo.getClaimNo(),"Y","1");
                    if(preCompList!=null&& !preCompList.isEmpty()){
                        for(PrpLCompensateVo compVo:preCompList){
                            PrpLCompensateExtVo compExtVo = compVo.getPrpLCompensateExt();
                            compExtVo.setIsCompDeduct("0");

                            compensateService.saveOrUpdateCompensateVo(compVo,userVo);
                        }
                    }

                    if(VerifyClaimTask.COMPE_CI.equals(taskType)){
                        PrpLPadPayMainVo padPayVo = padPayPubService.findPadPay(compensateVo.getRegistNo());
                        if(padPayVo!=null){
                            padPayVo.setIsCompdeDuct("0");
                            // 更新
                            padPayPubService.updatePadPay(padPayVo);
                        }
                    }
                }
            }
            // 正常计算书 回写预付 已扣减
            if(CodeConstants.WRITEOFFFLAG.NORMAL.equals(compeExtVo.getWriteOffFlag())){
                BigDecimal sumPre = DataUtils.NullToZero(compensateVo.getSumPreAmt()).add(DataUtils.NullToZero(compensateVo.getSumPreFee()));
                if(sumPre.compareTo(BigDecimal.ZERO)==1){// 计算书中存在扣减的垫付和预付费用
                    List<PrpLCompensateVo> preCompList = compensateService.findNormalCompListByClaimNo(compensateVo.getClaimNo(),"Y","0");
                    if(preCompList!=null&& !preCompList.isEmpty()){
                        for(PrpLCompensateVo compVo:preCompList){
                            log.info("预付计算书号" + (compVo == null ? null : compVo.getCompensateNo()) + "进行回写预付已扣减");
                            PrpLCompensateExtVo compExtVo = compVo.getPrpLCompensateExt();
                            compExtVo.setIsCompDeduct("1");
                            compensateService.saveOrUpdateCompensateVo(compVo,userVo);
                            log.info("预付计算书号" + compVo.getCompensateNo() + "结束回写预付已扣减,预付已扣减值为" + (compExtVo == null? null : compExtVo.getIsCompDeduct()));
                        }
                    }
                    if(VerifyClaimTask.COMPE_CI.equals(taskType)){
                        PrpLPadPayMainVo padPayVo = padPayPubService.findPadPay(compensateVo.getRegistNo());
                        if(padPayVo!=null&& !RadioValue.RADIO_YES.equals(padPayVo.getIsCompdeDuct())){
                            padPayVo.setIsCompdeDuct(RadioValue.RADIO_YES);
                            padPayPubService.updatePadPay(padPayVo);
                        }
                    }
                }
            }
            // 正常理算核赔通过 把temp限额给最终的限额字段 ，冲销核赔通过 反之
            this.wirteBackCheckDuty(compensateVo.getRegistNo(),taskType);
        } else  if(VerifyClaimTask.PREPAY_BI.equals(taskType)||VerifyClaimTask.PREPAY_CI.equals(taskType)){//预付冲销回写对冲计算书号
        	if(compeExtVo != null){
            	if(WRITEOFFFLAG.ALLOFF.equals(compeExtVo.getWriteOffFlag())&&compeExtVo.getOppoCompensateNo()!=null){
          	      PrpLCompensateVo compOriginVo = compensateService.findCompByPK(compeExtVo.getOppoCompensateNo());
          	      	if(compOriginVo != null){
                        PrpLCompensateExtVo comExtVo = compOriginVo.getPrpLCompensateExt();
                        if(comExtVo != null){
                            comExtVo.setOppoCompensateNo(compeNo);
                            compensateService.saveOrUpdateCompensateVo(compOriginVo,userVo);	
                        }
          	      	}
            	}
        	}
        }
        log.info("计算书号compeNo={},结束核赔通过回写方法",compeNo);
    }

    // 5 -- 回写是否理算扣减
    // private void writeBackIsCompdeDuct(String compeNo,String vc_action,String userCode) {
    // //回写预付理算表
    // PrpLCompensateVo compensateVo = compTaskService.queryCompByPK(compeNo);
    // boolean flag = CodeConstants.VClaimType.VC_ADOPT.equals(vc_action);
    // compensateVo.setUnderwriteFlag(flag ? "1" : "9");// 核赔审核通过
    // compensateVo.setUnderwriteDate(new Date());// 核赔通过时间
    // compensateVo.setUnderwriteUser(userCode);// 核赔处理人
    // // 更新
    // compTaskService.writeBackCompesate(compensateVo);
    // }

    // 5 -- 回写立案
    @Override
    public void wirteBackClaim(String claimNo,String taskInNode,String userCode) {
        String compStatus = "0";
        PrpLClaimVo calimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
        List<PrpLClaimKindVo> kindVoLsit = calimVo.getPrpLClaimKinds();
        List<PrpLClaimKindFeeVo> feeVoList = calimVo.getPrpLClaimKindFees();
        if(FlowNode.CompeCI.equals(taskInNode)||FlowNode.CompeBI.equals(taskInNode)||"CompeWfZero".equals(taskInNode)){
            compStatus = "1";
        }
        if(kindVoLsit!=null&&kindVoLsit.size()>0){
            for(PrpLClaimKindVo kindVo:kindVoLsit){
                kindVo.setCompStatus(compStatus);
                kindVo.setUpdateUser(userCode);
            }
        }
        if(feeVoList!=null&&feeVoList.size()>0){
            for(PrpLClaimKindFeeVo feeVo:feeVoList){
                feeVo.setCompStatus(compStatus);
                feeVo.setUpdateUser(userCode);
            }
        }
        claimTaskService.wirteBackClaimKindAndFee(kindVoLsit,feeVoList);
    }

    // 6 -- 回写垫付
    private void writeBackPadPay(String registNo,String vc_action,String userCode) {
        PrpLPadPayMainVo padPayVo = padPayPubService.findPadPay(registNo);
        if(padPayVo!=null){
            boolean flag = CodeConstants.VClaimType.VC_ADOPT.equals(vc_action);
            // padPayVo.setIsCompdeDuct(flag ? "1" : "0");
            padPayVo.setUnderwriteFlag(flag ? "1" : "9");
            padPayVo.setUnderwriteDate(new Date());
            padPayVo.setUnderwriteUser(userCode);
            padPayPubService.updatePadPay(padPayVo);
        }
    }

    // 7 -- 回写注销、拒赔
    private void writeBackCancel(String claimNo,String vc_action) {
        String cancelCode = "";
        String cancelCom = "";
        List<PrpLcancelTraceVo> cancelTraceVos = claimCancelService.queryCancelTrace(claimNo);
        if(cancelTraceVos!=null&& !cancelTraceVos.isEmpty()){
            for(PrpLcancelTraceVo cancelTraceVo:cancelTraceVos){
                boolean flag = CodeConstants.VClaimType.VC_ADOPT.equals(vc_action);
                cancelTraceVo.setStatus(flag ? "1" : "2");
                boolean backFlag = CodeConstants.VClaimType.VC_RETURN.equals(vc_action);
                cancelTraceVo.setFlags(flag ? "7" : backFlag ? "6" : "0");
                //
                if(StringUtils.isNotBlank(cancelTraceVo.getCancelCode())){
                    cancelCode = cancelTraceVo.getCancelCode();
                }
                cancelCom = cancelTraceVo.getComCode();
            }
            claimCancelService.vclaimBackWrite(cancelTraceVos);
        }

        PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
        if(claimVo!=null){// 拒赔回写
            claimVo.setValidFlag("2");
            claimVo.setCancelTime(new Date());
            claimVo.setCancelCode(cancelCode);
            claimVo.setCancelCom(cancelCom);
            claimTaskService.claimWirteBack(claimVo);
        }
        
		//埋点把理赔信息推送到rabbitMq中间件，供中台使用
		claimToMiddleStageOfCaseService.middleStageQuery(claimVo.getRegistNo(), "cance");
        
    }

    /**
     * <pre>
     * 设置回写的标志位
     * </pre>
     * @param lossState-初始值-00
     * @param taskType-任务类型
     * @param taskInNode-提交核赔节点
     * @param action-核赔操作
     * @modified: ☆Luwei(2016年8月29日 上午9:42:57): <br>
     */
    private String getLossState(String lossState,String taskType,String taskInNode,String action,String claimNo) {
        String str = setActionToStr(action);
        //重开1个险别另一个重开，把另一个险别的标志位回写为1
        List<PrpLReCaseVo> reCaseList = reOpenCaseService.findReCaseByClaimNo(claimNo);
        if(VerifyClaimTask.COMPE_CI.equals(taskType)&&FlowNode.CompeCI.equals(taskInNode)){// 交强理算核赔
        	if(reCaseList!=null && reCaseList.size()>0){
        		PrpLClaimVo claimVo = claimService.findClaimVoByClassCode(reCaseList.get(0).getRegistNo(), CodeConstants.PolicyType.POLICY_DAA);
        		if(claimVo!=null && StringUtils.isNotBlank(claimVo.getCaseNo())){
        			lossState = str+CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE;
        		}else{
            		lossState = str+lossState.substring(1,2);
            	}
        	}else{
        		lossState = str+lossState.substring(1,2);
        	}
        }
        else if(VerifyClaimTask.COMPE_BI.equals(taskType)&&FlowNode.CompeBI.equals(taskInNode)){// 商业理算核赔
        	if(reCaseList!=null && reCaseList.size()>0){
        		PrpLClaimVo claimVo = claimService.findClaimVoByClassCode(reCaseList.get(0).getRegistNo(), CodeConstants.PolicyType.POLICY_DZA);
        		if(claimVo!=null && StringUtils.isNotBlank(claimVo.getCaseNo())){
        			lossState = CodeConstants.UnderWriteFlag.MANAL_UNDERWRITE+str;
        		}else{
        			lossState = lossState.substring(0,1)+str;
            	}
        	}else{
        		lossState = lossState.substring(0,1)+str;
        	}
        }
        else if(VerifyClaimTask.COMPE_CI.equals(taskType)&&FlowNode.CompeWfCI.equals(taskInNode)){// 理算冲销(交强)-核赔
            lossState = RadioValue.RADIO_NO+lossState.substring(1,2);
        }
        else if(VerifyClaimTask.COMPE_BI.equals(taskType)&&FlowNode.CompeWfBI.equals(taskInNode)){// 理算冲销(商业)-核赔
            lossState = lossState.substring(0,1)+RadioValue.RADIO_NO;
        }
        else{
            return lossState;
        }
        // else if(VerifyClaimTask.PREPAY_CI.equals(taskType)&&FlowNode.PrePayCI.equals(taskInNode)){//交强预付核赔
        // lossState = str + lossState.substring(1,2);
        // }else if(VerifyClaimTask.PREPAY_BI.equals(taskType)&&FlowNode.PrePayBI.equals(taskInNode)){//商业预付核赔
        // lossState = lossState.substring(0,1) + str;
        // }else if(VerifyClaimTask.PREPAY_CI.equals(taskType)&&FlowNode.PrePayWfCI.equals(taskInNode)){//预付冲销(交强)-核赔
        // lossState = RadioValue.RADIO_NO + lossState.substring(1,2);
        // }else if(VerifyClaimTask.PREPAY_BI.equals(taskType)&&FlowNode.PrePayWfBI.equals(taskInNode)){//预付冲销(商业)-核赔
        // lossState = lossState.substring(0,1) + RadioValue.RADIO_NO;
        // }else if(VerifyClaimTask.PADPAY.equals(taskType)){//垫付核赔
        // return lossState;
        // }else{//注销、拒赔
        //
        // }
        // action = StringUtils.isNotBlank(action)&&VClaimType.VC_ADOPT.equals(action) ? "1" : "0";
        // lossState = VerifyClaimTask.COMPE_CI.equals(taskType)
        // ? action + lossState.substring(1,2) : lossState.substring(0,1) + action;
        return lossState;
    }

    @Override
    public String setActionToStr(String action) {
        String returnStr = "";
        if(VClaimType.VC_ADOPT.equals(action)){
            returnStr = RadioValue.RADIO_YES;
        }
        else{
            returnStr = RadioValue.RADIO_NO;
        }
        return returnStr;
    }

    /**
     * 回写交强限额扣减数据
     */
    private void wirteBackCheckDuty(String registNo,String taskType) {
    	log.info("报案号={},理算核赔通过进入prplcheckduty回写剩余保额的方法",registNo);
        List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
        if(checkDutyList!=null&& !checkDutyList.isEmpty()){
            for(PrpLCheckDutyVo dutyVo:checkDutyList){
                if(VerifyClaimTask.COMPE_CI.equals(taskType)){// 交强
                    BigDecimal carLeftAmount = dutyVo.getCiCarLeftAmount();
                    BigDecimal medLeftAmount = dutyVo.getCiCarLeftAmount();
                    BigDecimal defLeftAmount = dutyVo.getCiCarLeftAmount();
                    dutyVo.setCiCarLeftAmount(dutyVo.getCiTCarLeftAmount());
                    dutyVo.setCiMedLeftAmount(dutyVo.getCiTMedLeftAmount());
                    dutyVo.setCiDehLeftAmount(dutyVo.getCiTDehLeftAmount());

                    dutyVo.setCiTCarLeftAmount(carLeftAmount);
                    dutyVo.setCiTMedLeftAmount(medLeftAmount);
                    dutyVo.setCiTDehLeftAmount(defLeftAmount);
                	log.info("报案号=" + registNo + "核赔类型为交强,checkduty的id="+(dutyVo== null? null:dutyVo.getId())
                	+ "赋值分别为CiTCarLeftAmount =" + (dutyVo == null?null:dutyVo.getCiCarLeftAmount()) 
                    + ",CiTMedLeftAmount= " +(dutyVo == null?null:dutyVo.getCiCarLeftAmount()) 
                    + ",CiTDehLeftAmount=" + (dutyVo == null?null:dutyVo.getCiCarLeftAmount())
                    + ",CiCarLeftAmount=" + (dutyVo == null?null:dutyVo.getCiTCarLeftAmount()) 
                    + ",CiMedLeftAmount=" + (dutyVo == null?null:dutyVo.getCiTMedLeftAmount())
                    + ",CiDehLeftAmount=" + (dutyVo == null?null:dutyVo.getCiTDehLeftAmount()));
                }
                else{
                    BigDecimal carLeftAmount = dutyVo.getBiCarLeftAmount();
                    BigDecimal medLeftAmount = dutyVo.getBiCarLeftAmount();
                    BigDecimal defLeftAmount = dutyVo.getBiCarLeftAmount();
                    dutyVo.setBiCarLeftAmount(dutyVo.getBiTCarLeftAmount());
                    dutyVo.setBiMedLeftAmount(dutyVo.getBiTMedLeftAmount());
                    dutyVo.setBiDehLeftAmount(dutyVo.getBiTDehLeftAmount());

                    dutyVo.setBiTCarLeftAmount(carLeftAmount);
                    dutyVo.setBiTMedLeftAmount(medLeftAmount);
                    dutyVo.setBiTDehLeftAmount(defLeftAmount);

                	log.info("报案号=" + registNo + "核赔类型为商业,checkduty的id="+(dutyVo== null? null:dutyVo.getId())
                	+ "赋值分别为BiTCarLeftAmount =" + (dutyVo == null?null:dutyVo.getBiCarLeftAmount())
                    + ",BiTMedLeftAmount= "+(dutyVo == null?null:dutyVo.getBiCarLeftAmount()) 
                    + ",BiTDehLeftAmount=" + (dutyVo == null?null:dutyVo.getBiCarLeftAmount())
                    + ",BiCarLeftAmount=" + (dutyVo == null?null:dutyVo.getBiTCarLeftAmount()) 
                    + ",BiMedLeftAmount=" + (dutyVo == null?null:dutyVo.getBiTMedLeftAmount())
                    + ",BiDehLeftAmount=" + (dutyVo == null?null:dutyVo.getBiTDehLeftAmount()));
                }
            }
            checkTaskService.saveCheckDutyList(checkDutyList);
        }
    	log.info("报案号="+ registNo + ",理算核赔通过结束prplcheckduty回写剩余保额的方法");
    }

    // 挂起工作流
    private void cancleClaimForOther(String registNo,String userCode) {
        List<PrpLClaimVo> prpLClaimVoList = claimTaskService.findClaimListByRegistNo(registNo);
        if(prpLClaimVoList!=null){
            // 挂起工作流
            // 判断立案是否可以全部注销其它节点
            if(prpLClaimVoList.size()==2){// 有一个注销了就可以注销其它节点
                if("0".equals(prpLClaimVoList.get(0).getValidFlag())||"2".equals(prpLClaimVoList.get(0).getValidFlag())){
                    if("0".equals(prpLClaimVoList.get(1).getValidFlag())||"2".equals(prpLClaimVoList.get(1).getValidFlag())){
                        claimTaskService.cancleClaimForOther(registNo,userCode);
                    }
                }
            }
            else{// 只有一条数据就可以注销其它节点
                claimTaskService.cancleClaimForOther(registNo,userCode);
            }
        }
    }

    /**
     * 理算核赔通过送平台
     */
    @Override
    public void verifyToPlatform(PrpLCompensateVo compeVo,String taskInNode) {
		Long currentDate = System.currentTimeMillis();
		log.info("verifyToPlatform begin... registNo={}",compeVo.getRegistNo());
        String compeNo = compeVo.getCompensateNo();
        String classType = Risk.DQZ.equals(compeVo.getRiskCode()) ? "11" : "12";
        String policyComCode = policyViewService.findPolicyComCode(compeVo.getRegistNo(),classType);
        PrpLRegistVo registVo = registQueryService.findByRegistNo(compeVo.getRegistNo());
        PrpLConfigValueVo configValueVo = ConfigUtil.findConfigByCode(CodeConstants.PlatformOptimized,registVo.getComCode());
        try{
        	//平台优化开关
        	if("1".equals(configValueVo.getConfigValue())){
        		// 联共保 从共，从联不交互平台
        		if(!(CodeConstants.GBFlag.MINORCOMMON.equals(registVo.getIsGBFlag())||CodeConstants.GBFlag.MINORRELATION.equals(registVo.getIsGBFlag()))){
        			sendVClaimToAll.savePlatformTask(compeVo,policyComCode);
        		}
        	}else{
        		if(policyComCode.startsWith("22")){
                    // 上海平台
                    sendVClaimToSH.sendVClaimToSH(compeVo,policyComCode,null);
                }
                else{// 全国平台
                    if(Risk.DQZ.equals(compeVo.getRiskCode())){// 交强
                        sendVClaimToAll.sendVClaimCIToPlatform(compeNo,null);
                    }
                    else{// 商业
                        sendVClaimToAll.sendVClaimBIToPlatform(compeNo,null);
                    }
                }
        	}
        }catch(Exception e){
        	log.info("计算书号："+compeNo+"，核赔通过送平台失败！系统交互时间：" + new Date(), e);
        	e.printStackTrace();
        }
		log.info("verifyToPlatform sendVClaimToPlatform end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),
				System.currentTimeMillis()-currentDate);
        PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compeVo.getClaimNo());
		try {
			// 核赔通过送再保已决数据分摊试算 niuqaing
			interfaceAsyncService.TransDataForCompensateVo(claimVo, compeVo);
		} catch (Exception e) {
			log.info("计算书号："+ compeNo +"，核赔通过送再保已决数据分摊试算失败！系统交互时间：" + new Date(), e);
		}
		log.info("verifyToPlatform end... registNo={}, cost time={} ms ",compeVo.getRegistNo(),System.currentTimeMillis()-currentDate);
    }

    /**
     * 结案送平台
     * @param endCaseVo
     */
    private void endCaseToPlatform(PrpLEndCaseVo endCaseVo) {
        try{
            String registNo = endCaseVo.getRegistNo();
            String claimNo = endCaseVo.getClaimNo();
            PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
            String classType = Risk.DQZ.equals(claimVo.getRiskCode()) ? "11" : "12";
            String policyComCode = policyViewService.findPolicyComCode(registNo,classType);

            if(policyComCode.startsWith("22")){// 上海平台
                sendEndCaseToSH.sendEndCaseToSHBySubmit(endCaseVo,null);
            }
            else{// 全国平台
                sendEndCaseToAll.sendEndCaseToPlatform(endCaseVo,null);
            }

        } catch(Exception e){
            e.printStackTrace();
            log.info("结案送平台失败！", e);
        }
    }

    // end

    // ------------- 垫付 --------------------//

    @Override
    public PrpLCMainVo getCMainInfo(String registNo) {
        PrpLCMainVo cMainVo = null;
        List<PrpLCMainVo> cMainVos = policyViewService.getPolicyAllInfo(registNo);
        for(PrpLCMainVo cMainVoTemp:cMainVos){
            if(Risk.DQZ.equals(cMainVoTemp.getRiskCode())){
                cMainVo = cMainVoTemp;
            }
        }
        return cMainVo;
    }

    @Override
    public PrpLCheckCarVo getCheckCarInfo(String registNo) {
        return checkTaskService.findCheckCarBySerialNo(registNo,1);
    }

    @Override
    public PrpLPadPayMainVo getPadPay(String registNo,String compeNo) {
        return padPayPubService.queryPadPay(registNo,compeNo);
    }

    @Override
    public PrpLDlossCarInfoVo getDlossCarInfo(String registNo) {
        PrpLDlossCarInfoVo dlossCarInfoVo = null;
        List<PrpLDlossCarMainVo> carMainVos = null;
        PrpLDlossCarMainVo DlossCarMainVo = null;
        carMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        if(carMainVos!=null&&carMainVos.size()>0){
            for(PrpLDlossCarMainVo carMainVo:carMainVos){
                if(carMainVo.getSerialNo()==1){
                    DlossCarMainVo = carMainVo;
                }
            }
        }
        if(DlossCarMainVo!=null){
            dlossCarInfoVo = DlossCarMainVo.getLossCarInfoVo();
        }
        return dlossCarInfoVo;
    }

    @Override
    public List<PrpLDlossCarMainVo> getLossCarInfo(String registNo,String bcFlag) {
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLDlossCarMainVo> reLossCarMainVos = new ArrayList<PrpLDlossCarMainVo>();
        if(lossCarMainVos!=null){
            for(PrpLDlossCarMainVo lossCarMainVo:lossCarMainVos){
                if(checkLossState(lossCarMainVo.getLossState(),bcFlag)&&lossCarMainVo.getUnderWriteFlag().equals("1")){
                    if(lossCarMainVo.getDeflossCarType().equals("1")// 自付
                       ||lossCarMainVo.getDeflossCarType().equals("3")){// 互碰自赔
                        reLossCarMainVos.add(lossCarMainVo);
                    }
                }
            }
        }
        return reLossCarMainVos;
    }

    /**
     * 判断数据表的理算状态
     * @param lossState
     * @param bcFlag
     * @return
     */
    private boolean checkLossState(String lossState,String bcFlag) {
        // 第一位-交强 第二位-商业 0-未理算 1-已理算
        if(StringUtils.isNotBlank(lossState)){
            if(bcFlag.equals("11")){
                if(lossState.equals("00")||lossState.equals("01")||lossState.equals("0")){
                    return true;// 交强未理算
                }
            }
            if(bcFlag.equals("12")){
                if(lossState.equals("00")||lossState.equals("10")||lossState.equals("0")){
                    return true;
                }
            }
        }
        return false;// 已理算
    }

    @Override
    public String getCheckDutyFlag(String registNo) {
        String checkDutyFlag = "0";
        List<PrpLCheckDutyVo> checkDutyList = checkTaskService.findCheckDutyByRegistNo(registNo);
        // PrpLCheckDutyVo checkDuty = new PrpLCheckDutyVo();
        if(checkDutyList!=null){
            for(PrpLCheckDutyVo checkDutyVo:checkDutyList){
                if(checkDutyVo.getSerialNo()==1){
                    // checkDuty = checkDutyVo;
                    if(checkDutyVo.getIndemnityDutyRate().compareTo(BigDecimal.ZERO)==1){
                        checkDutyFlag = "1";
                    }
                }
                else{
                    if(checkDutyVo.getCiDutyFlag()==null){
                        checkDutyFlag = "0";
                        break;
                    }
                    else{
                        if(checkDutyVo.getCiDutyFlag().equals("2")){
                            checkDutyFlag = "1";
                        }
                        else{
                            checkDutyFlag = "0";
                            break;// 跳出当前循环
                        }
                    }
                }
            }
        }
        return checkDutyFlag;
    }

    @Override
    public List<PrpLCItemKindVo> getOtherKind(String registNo,String flag) {
        // 不计免赔险
        List<PrpLCItemKindVo> claimKindMList = new ArrayList<PrpLCItemKindVo>();
        if(flag.equals("2")){
            List<PrpLCItemKindVo> cItemKindList = registQueryService.findCItemKindListByRegistNo(registNo);
            for(PrpLCItemKindVo cItemKindVo:cItemKindList){
                // 旧条款-是否不计免赔标志位
                // 新条款-kindCode以M结尾的都是不计免赔险
                if( !"1101".equals(cItemKindVo.getRiskCode())&& !CodeConstants.ISNEWCLAUSECODE_MAP.get(cItemKindVo.getRiskCode())&&cItemKindVo
                        .getFlag().length()>4&&String.valueOf(cItemKindVo.getFlag().charAt(4)).equals("1")){
                    claimKindMList.add(cItemKindVo);
                }
                else{
                    if(StringUtils.isNotBlank(cItemKindVo.getKindCode())&&cItemKindVo.getKindCode()
                            .substring(cItemKindVo.getKindCode().length()-1,cItemKindVo.getKindCode().length()).equals("M")){
                        claimKindMList.add(cItemKindVo);
                    }
                }
            }
        }
        return claimKindMList;
    }

    // 拒赔核赔通过，相当于立案注销，送平台
    private void sendCancelToPlatform(String claimNo) {
        PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
        if(claimVo!=null){
            String registNo = claimVo.getRegistNo();
            String policyType = Risk.DQZ.equals(claimVo.getRiskCode()) ? "11" : "12";
            String com = policyViewService.findPolicyComCode(registNo,policyType);
            if(com.startsWith("22")){// 上海平台
                sendCancelToSH.sendClaimCancelTo_SH(claimNo);
            }
            else{// 全国平台
                sendCancelToAll.sendClaimCancelToPlatform(claimVo.getRiskCode(),registNo,CancelType.CLAIM_CANCEL,claimVo.getSumClaim().doubleValue());
            }
        }
    }

    private PrpLuwNotionMainVo findUwNotionMain(Long uwNotionMainId) {
        PrpLuwNotionMainVo uwNotionVo = null;
        PrpLuwNotionMain uwTion = databaseDao.findByPK(PrpLuwNotionMain.class,uwNotionMainId);
        if(uwTion!=null){
            uwNotionVo = new PrpLuwNotionMainVo();
            Beans.copy().from(uwTion).to(uwNotionVo);
        }
        return uwNotionVo;
    }

    @Override
    public void sendCompensateToPayment(Long uwNotionMainId) throws Exception {
        // TODO Auto-generated method stub
        PrpLuwNotionMainVo uwNotionVo = this.findUwNotionMain(uwNotionMainId);
        String registNo = uwNotionVo.getRegistNo();
        String taskType = uwNotionVo.getPolicyType();
        String taskInNode = uwNotionVo.getTaskInNode();
        String compensateNo = uwNotionVo.getCompensateNo();
        String policyCom = policyViewService.getPolicyComCode(registNo);
        // 预付、垫付核赔通过后送收付
        if( !VerifyClaimTask.COMPE_CI.equals(taskType)&& !VerifyClaimTask.COMPE_BI.equals(taskType)){

            if(VerifyClaimTask.PREPAY_BI.equals(taskType)||VerifyClaimTask.PREPAY_CI.equals(taskType)){// 预付
                PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
                interfaceAsyncService.prePayToPayment(compensateVo);
                //送VAT
                log.info("推送发票费用接口start====================计算书号："+compensateVo.getCompensateNo());
                //interfaceAsyncService.pushPreCharge(compensateVo.getCompensateNo());
                log.info("推送发票费用接口end====================计算书号："+compensateVo.getCompensateNo());
            }

            if(VerifyClaimTask.PADPAY.equals(taskType)){// 垫付
                PrpLPadPayMainVo padPayMainVo = padPayService.findPadPayMainByRegistNo(registNo).get(0);
                padPayMainVo.setComCode(policyCom);
                interfaceAsyncService.padPayToPayment(padPayMainVo);
                //赔款送vat开关
                PrpLConfigValueVo configValuePayToVatVo = ConfigUtil.findConfigByCode(CodeConstants.PAYTOVAT,policyCom);
                if(configValuePayToVatVo != null && CodeConstants.IsSingleAccident.YES.equals(configValuePayToVatVo.getConfigValue())){
                	interfaceAsyncService.pushPadPay(padPayMainVo);
                }
            }
        }
        else{
            if(FlowNode.CompeWfBI.equals(taskInNode)||FlowNode.CompeWfCI.equals(taskInNode)){
                // 交强理算冲销核赔,商业理算冲销核赔-结束任务
                // 核赔通过后送收付
                // try{
                PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
                interfaceAsyncService.compensateToPayment(compensateVo);
                PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(compensateVo.getClaimNo());
                // 核赔通过送再保已决数据分摊试算 niuqaing
                interfaceAsyncService.transDataForWashTransaction(claimVo,compensateVo);
                // 推送发票费用接口 niuqiang
                //interfaceAsyncService.pushCharge(compensateNo);
                // }catch(Exception e){
                // log.info("核赔通过后送收付送收付失败！"+e.getMessage());
                // // throw new IllegalArgumentException("核赔通过后送收付失败！"+e.getMessage());
                // }
            }
        }
    }

    //

    @Override
    public Long autoVerifyClaimEndCase(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo) {

        String currentNode = FlowNode.VClaim_BI_LV0.toString();
        if("1101".equals(prpLCompensateVo.getRiskCode())){
            currentNode = FlowNode.VClaim_CI_LV0.toString();
        }
        // 查询当前在in表的核赔节点taskid
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLCompensateVo.getRegistNo(),currentNode);
        if(wfTaskVo==null){
            throw new IllegalArgumentException("没找到工作流数据！");
        }
        String nextNode = FlowNode.EndCas.toString();
        String action = VClaimType.VC_ADOPT;
        String compeWfZeroFlag = "0";
        String policyCom = policyViewService.getPolicyComCode(prpLCompensateVo.getRegistNo());
        String userCode = "AutoVClaim";
        // 保存核赔审核意见表

        // 组织审核意见主表数据
        PrpLuwNotionMainVo uwNotionMainVo = new PrpLuwNotionMainVo();
        uwNotionMainVo.setClaimNo(prpLCompensateVo.getClaimNo());
        uwNotionMainVo.setCompensateNo(prpLCompensateVo.getCompensateNo());
        uwNotionMainVo.setHandle("1");// 核赔通过
        uwNotionMainVo.setPolicyNo(prpLCompensateVo.getPolicyNo());
        if("1101".equals(prpLCompensateVo.getRiskCode())){
            uwNotionMainVo.setPolicyType(CodeConstants.VerifyClaimTask.COMPE_CI);
            uwNotionMainVo.setTaskInNode(FlowNode.CompeCI.toString());
        }
        else{
            uwNotionMainVo.setPolicyType(CodeConstants.VerifyClaimTask.COMPE_BI);
            uwNotionMainVo.setTaskInNode(FlowNode.CompeBI.toString());
        }
        uwNotionMainVo.setRecoveries(prpLCompensateVo.getRecoveryFlag());// 是否发起追偿
        uwNotionMainVo.setRegistNo(prpLCompensateVo.getRegistNo());
        uwNotionMainVo.setTaskId(wfTaskVo.getTaskId().longValue());
        PrpLuwNotionVo uwNotionVo = new PrpLuwNotionVo();
        // 组织审核意见子表数据
        BigDecimal sumAmt = prpLCompensateVo.getSumPaidAmt().add(prpLCompensateVo.getSumPaidFee());
        uwNotionVo.setAmount(sumAmt);
        uwNotionVo.setHandle(CodeConstants.VClaimType.VC_ADOPT);
        uwNotionVo.setHandleText("自动核赔，同意赔付");
        uwNotionVo.setVerifyText("01");// 核赔意见-同意赔付

        Long uwNotionMainId = this.saveVerifyClaim(uwNotionMainVo,uwNotionVo,wfTaskVo,policyCom,userCode,"0");

        // 核赔通过
        String compNo = wfTaskVo.getHandlerIdKey();
        String claimNo = wfTaskVo.getClaimNo();
        WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
        submitVo.setFlowId(wfTaskVo.getFlowId());
        submitVo.setFlowTaskId(wfTaskVo.getTaskId());
        submitVo.setComCode(policyCom);
        submitVo.setTaskInUser(userCode);// 自动核赔
        submitVo.setTaskInKey(compNo);
        submitVo.setHandleIdKey(claimNo);
        submitVo.setAssignUser(userCode);
        submitVo.setAssignCom(policyCom);
        submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
        submitVo.setNextNode(FlowNode.valueOf(nextNode));

        // 提交工作流
        try{
            this.submitTaskToEndCase(uwNotionMainVo,submitVo,policyCom,userCode,wfTaskVo,action,userVo,compeWfZeroFlag);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return uwNotionMainId;

    }

    @Override
    public List<PrpLWfTaskVo> autoVerifyClaimEndCaseForPingAn(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo) throws Exception {

        String currentNode = FlowNode.VClaim_BI_LV0.toString();
        if("1101".equals(prpLCompensateVo.getRiskCode())){
            currentNode = FlowNode.VClaim_CI_LV0.toString();
        }
        // 查询当前在in表的核赔节点taskid
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLCompensateVo.getRegistNo(),currentNode);
        if(wfTaskVo==null){
            throw new IllegalArgumentException("没找到工作流数据！");
        }
        String nextNode = FlowNode.EndCas.toString();
        String action = VClaimType.VC_ADOPT;
        String compeWfZeroFlag = "0";
        String policyCom = policyViewService.getPolicyComCode(prpLCompensateVo.getRegistNo());
        String userCode = "AutoVClaim";

        // 组织审核意见主表数据
        String policyType;
        String taskInNode;
        if("1101".equals(prpLCompensateVo.getRiskCode())){
            policyType = CodeConstants.VerifyClaimTask.COMPE_CI;
            taskInNode = FlowNode.CompeCI.toString();
        } else{
            policyType = CodeConstants.VerifyClaimTask.COMPE_BI;
            taskInNode = FlowNode.CompeBI.toString();
        }
        PrpLuwNotionMainVo uwNotionMainVo = this.findUwNotion(prpLCompensateVo.getRegistNo(),prpLCompensateVo.getCompensateNo(),policyType);
        if (uwNotionMainVo == null) {
            uwNotionMainVo = new PrpLuwNotionMainVo();
            uwNotionMainVo.setClaimNo(prpLCompensateVo.getClaimNo());
            uwNotionMainVo.setCompensateNo(prpLCompensateVo.getCompensateNo());
            uwNotionMainVo.setHandle("1");// 核赔通过
            uwNotionMainVo.setPolicyNo(prpLCompensateVo.getPolicyNo());
            uwNotionMainVo.setPolicyType(policyType);
            uwNotionMainVo.setTaskInNode(taskInNode);
            uwNotionMainVo.setRecoveries(prpLCompensateVo.getRecoveryFlag());// 是否发起追偿
            uwNotionMainVo.setRegistNo(prpLCompensateVo.getRegistNo());
            uwNotionMainVo.setTaskId(wfTaskVo.getTaskId().longValue());
            PrpLuwNotionVo uwNotionVo = new PrpLuwNotionVo();
            // 组织审核意见子表数据
            BigDecimal sumAmt = prpLCompensateVo.getSumPaidAmt().add(prpLCompensateVo.getSumPaidFee());
            uwNotionVo.setAmount(sumAmt);
            uwNotionVo.setHandle(CodeConstants.VClaimType.VC_ADOPT);
            uwNotionVo.setHandleText("自动核赔，同意赔付");
            uwNotionVo.setVerifyText("01");// 核赔意见-同意赔付
            // 保存核赔审核意见表
            Long uwNotionMainId = this.saveVerifyClaim(uwNotionMainVo,uwNotionVo,wfTaskVo,policyCom,userCode,"0");

            //回写理算表数据
            String taskType = uwNotionMainVo.getPolicyType();
            String compeNo = uwNotionMainVo.getCompensateNo();
            String recFlag = StringUtils.isEmpty(uwNotionMainVo.getRecoveries()) ? "0" : uwNotionMainVo.getRecoveries();
            this.writeBackCompesate(compeNo,taskType,action,recFlag,userCode,userVo);
            // 回写立案表数据，核赔审核通过、理算冲销核赔通过（PrpLClaimKind，PrpLClaimFee）
            this.wirteBackClaim(prpLCompensateVo.getClaimNo(), taskInNode, userCode);
        }

        // 核赔通过
        String compNo = wfTaskVo.getHandlerIdKey();
        String claimNo = wfTaskVo.getClaimNo();
        WfTaskSubmitVo submitVo = new WfTaskSubmitVo();
        submitVo.setFlowId(wfTaskVo.getFlowId());
        submitVo.setFlowTaskId(wfTaskVo.getTaskId());
        submitVo.setComCode(policyCom);
        submitVo.setTaskInUser(userCode);// 自动核赔
        submitVo.setTaskInKey(compNo);
        submitVo.setHandleIdKey(claimNo);
        submitVo.setAssignUser(userCode);
        submitVo.setAssignCom(policyCom);
        submitVo.setCurrentNode(FlowNode.valueOf(currentNode));
        submitVo.setNextNode(FlowNode.valueOf(nextNode));

        // 提交工作流
        PrpLCompensateVo compeVo = compensateTaskService.findPrpLCompensateVoByPK(uwNotionMainVo.getCompensateNo());
        // 核赔提交结案（核赔提交工作流产生结案任务）
        List<PrpLWfTaskVo> wfTaskVos = wfTaskHandleService.submitVclaim(compeVo,submitVo);
        return wfTaskVos;
    }

	@Override
    public ResultPage<VerifyClaimPassVo> findVerifyClaimPassList(VerifyClaimPassVo verifyClaimPassVo,int start,int length) {
        
        SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        sqlJoinUtils.append(" select plnm.registNo,plnm.claimNo,plnm.compensateNo,plnm.taskInNode,plnm.createTime,plnm.handleTime,plnm.policyNo,pcm.insuredName,item.sumRealPay,item.payeeName,item.bankOutLets,item.accountNo,item.payStatus,plnm.handleUser from PrpLuwNotionMain  plnm,PrpLCMain  pcm,( ");
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"1".equals(verifyClaimPassVo.getCompensateType())||"4".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT ppp.compensateNo AS compensateNo,ppp.payAmt AS sumRealPay,ppc.payeeName AS payeeName,ppc.bankOutLets AS bankOutLets,ppc.accountNo AS accountNo,ppp.payStatus AS payStatus FROM PrpLPayCustom ppc,PrpLPrePay ppp WHERE ppp.payeeId=ppc.id ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" union all ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"2".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT pppm.compensateNo AS compensateNo,pppp.costSum AS sumRealPay,pppp.payeeName AS payeeName,pppp.bankName AS bankOutLets,pppp.accountNo AS accountNo,pppp.payStatus AS payStatus FROM PrpLPadPayMain pppm,PrpLPadPayPerson pppp WHERE pppm.id = pppp.padPayId ");    
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" union all ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"3".equals(verifyClaimPassVo.getCompensateType())||"5".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT pp.compensateNo AS compensateNo,pp.sumRealPay AS sumRealPay,ppc.payeeName AS payeeName,ppc.bankOutLets AS bankOutLets,ppc.accountNo AS accountNo,pp.payStatus AS payStatus FROM PrpLPayCustom ppc,PrpLPayment pp WHERE pp.payeeId=ppc.id ");    
        }    
        
        sqlJoinUtils.append(" ) item WHERE plnm.registNo=pcm.registNo AND plnm.policyNo=pcm.policyNo AND plnm.handle ='1' AND plnm.compensateNo=item.compensateNo ");
        
        
        //加入机构限制
        sqlJoinUtils.andComSql("pcm","comCode",verifyClaimPassVo.getComCode());
        
        sqlJoinUtils.append(" AND plnm.taskinNode in('PrePayBI','PrePayCI','PadPay','CompeBI','CompeCI','PrePayWfBI','PrePayWfCI','CompeWfBI','CompeWfCI') ");
        
        if(StringUtils.isNotBlank(verifyClaimPassVo.getRegistNo())){
            sqlJoinUtils.append(" AND plnm.registNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getRegistNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getClaimNo())){
            sqlJoinUtils.append(" AND plnm.claimNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getClaimNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getCompensateNo())){
            sqlJoinUtils.append(" AND plnm.compensateNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getCompensateNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getInsuredName())){
            sqlJoinUtils.append(" AND pcm.insuredName like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getInsuredName()+"%");
        }
        
        sqlJoinUtils.append(" AND plnm.handleTime >= ? ");
        sqlJoinUtils.addParamValue(verifyClaimPassVo.getVerifyClaimPassTimeStart());
        sqlJoinUtils.append(" AND plnm.handleTime <= ? ");
        sqlJoinUtils.addParamValue(DateUtils.addDays(verifyClaimPassVo.getVerifyClaimPassTimeEnd(), 1));
        sqlJoinUtils.append(" AND plnm.createTime >= ? ");
        sqlJoinUtils.addParamValue(verifyClaimPassVo.getCreateTimeStart());
        sqlJoinUtils.append(" AND plnm.createTime <= ? ");
        sqlJoinUtils.addParamValue(DateUtils.addDays(verifyClaimPassVo.getCreateTimeEnd(), 1));
        
        
        if(StringUtils.isNotBlank(verifyClaimPassVo.getCompensateType())){
            if("1".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("PrePayBI");
                sqlJoinUtils.addParamValue("PrePayCI");
            }
            if("2".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode = ? ");
                sqlJoinUtils.addParamValue("PadPay");
            }
            if("3".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("CompeBI");
                sqlJoinUtils.addParamValue("CompeCI");
            }
            if("4".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("PrePayWfBI");
                sqlJoinUtils.addParamValue("PrePayWfCI");
            }
            if("5".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("CompeWfBI");
                sqlJoinUtils.addParamValue("CompeWfCI");
            }
        }
        
        if(StringUtils.isNotBlank(verifyClaimPassVo.getAutoType())){
        	if("0".equals(verifyClaimPassVo.getAutoType())){
        		 sqlJoinUtils.append(" AND plnm.handleUser != ? ");
        		 sqlJoinUtils.addParamValue("AutoVClaim");
        	}else if("1".equals(verifyClaimPassVo.getAutoType())){
        		 sqlJoinUtils.append(" AND plnm.handleUser = ? ");
        		 sqlJoinUtils.addParamValue("AutoVClaim");
        	}
        }

        sqlJoinUtils.append(" ORDER BY plnm.handleTime desc ");
        
        String sql = sqlJoinUtils.getSql();
        Object[] values = sqlJoinUtils.getParamValues();
        Page<Object[]> page = new Page<Object[]>();
        try{
            page = baseDaoService.pagedSQLQuery(sql,start,length,values);
        }catch(Exception e){
            e.printStackTrace();
        }

        List<VerifyClaimPassVo> resultVoList = new ArrayList<VerifyClaimPassVo>();
              
        for(int i = 0; i<page.getResult().size(); i++ ){
            VerifyClaimPassVo vo = new VerifyClaimPassVo();
            Object[] obj = (Object[])page.getResult().get(i);
            vo.setRegistNo((String)obj[0]);
            vo.setClaimNo((String)obj[1]);
            vo.setCompensateNo((String)obj[2]);
            vo.setCompensateType((String)obj[3]);
            vo.setCreateTime((Date)obj[4]);
            vo.setHandleTime((Date)obj[5]);
            vo.setPolicyNo((String)obj[6]);
            vo.setInsuredName((String)obj[7]);
            vo.setSumRealPay((BigDecimal)obj[8]);
            vo.setPayeeName((String)obj[9]);
            vo.setBankOutLets((String)obj[10]);
            vo.setAccountNo((String)obj[11]);
            vo.setPayStatus((String)obj[12]);
            vo.setHandleUser((String)obj[13]);
            resultVoList.add(vo);
        }
        
        ResultPage<VerifyClaimPassVo> resultPage = new ResultPage<VerifyClaimPassVo>(start,length,page.getTotalCount(),resultVoList);
        return resultPage;
    }

    @Override
    public List<VerifyClaimPassVo> getDatas(VerifyClaimPassVo verifyClaimPassVo) {
        
        SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
        sqlJoinUtils.append(" select plnm.registNo,plnm.claimNo,plnm.compensateNo,plnm.taskInNode,plnm.createTime,plnm.handleTime,plnm.policyNo,pcm.insuredName,item.sumRealPay,item.payeeName,item.bankOutLets,item.accountNo,item.payStatus from PrpLuwNotionMain  plnm,PrpLCMain  pcm,( ");
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"1".equals(verifyClaimPassVo.getCompensateType())||"4".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT ppp.compensateNo AS compensateNo,ppp.payAmt AS sumRealPay,ppc.payeeName AS payeeName,ppc.bankOutLets AS bankOutLets,ppc.accountNo AS accountNo,ppp.payStatus AS payStatus FROM PrpLPayCustom ppc,PrpLPrePay ppp WHERE ppp.payeeId=ppc.id ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" union all ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"2".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT pppm.compensateNo AS compensateNo,pppp.costSum AS sumRealPay,pppp.payeeName AS payeeName,pppp.bankName AS bankOutLets,pppp.accountNo AS accountNo,pppp.payStatus AS payStatus FROM PrpLPadPayMain pppm,PrpLPadPayPerson pppp WHERE pppm.id = pppp.padPayId ");    
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" union all ");
        }
        
        if(StringUtils.isBlank(verifyClaimPassVo.getCompensateType())||"3".equals(verifyClaimPassVo.getCompensateType())||"5".equals(verifyClaimPassVo.getCompensateType())){
            sqlJoinUtils.append(" SELECT pp.compensateNo AS compensateNo,pp.sumRealPay AS sumRealPay,ppc.payeeName AS payeeName,ppc.bankOutLets AS bankOutLets,ppc.accountNo AS accountNo,pp.payStatus AS payStatus FROM PrpLPayCustom ppc,PrpLPayment pp WHERE pp.payeeId=ppc.id ");    
        }    
        
        sqlJoinUtils.append(" ) item WHERE plnm.registNo=pcm.registNo AND plnm.policyNo=pcm.policyNo AND plnm.handle ='1' AND plnm.compensateNo=item.compensateNo ");
        
        //加入机构限制
        sqlJoinUtils.andComSql("pcm","comCode",verifyClaimPassVo.getComCode());
        
        sqlJoinUtils.append(" AND plnm.taskinNode in('PrePayBI','PrePayCI','PadPay','CompeBI','CompeCI','PrePayWfBI','PrePayWfCI','CompeWfBI','CompeWfCI') ");
        
        if(StringUtils.isNotBlank(verifyClaimPassVo.getRegistNo())){
            sqlJoinUtils.append(" AND plnm.registNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getRegistNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getClaimNo())){
            sqlJoinUtils.append(" AND plnm.claimNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getClaimNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getCompensateNo())){
            sqlJoinUtils.append(" AND plnm.compensateNo like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getCompensateNo()+"%");
        }
        if(StringUtils.isNotBlank(verifyClaimPassVo.getInsuredName())){
            sqlJoinUtils.append(" AND pcm.insuredName like ? ");
            sqlJoinUtils.addParamValue("%"+verifyClaimPassVo.getInsuredName()+"%");
        }

        sqlJoinUtils.append(" AND plnm.handleTime >= ? ");
        sqlJoinUtils.addParamValue(verifyClaimPassVo.getVerifyClaimPassTimeStart());
        sqlJoinUtils.append(" AND plnm.handleTime <= ? ");
        sqlJoinUtils.addParamValue(DateUtils.addDays(verifyClaimPassVo.getVerifyClaimPassTimeEnd(), 1));
        sqlJoinUtils.append(" AND plnm.createTime >= ? ");
        sqlJoinUtils.addParamValue(verifyClaimPassVo.getCreateTimeStart());
        sqlJoinUtils.append(" AND plnm.createTime <= ? ");
        sqlJoinUtils.addParamValue(DateUtils.addDays(verifyClaimPassVo.getCreateTimeEnd(), 1));
        
        
        if(StringUtils.isNotBlank(verifyClaimPassVo.getCompensateType())){
            if("1".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("PrePayBI");
                sqlJoinUtils.addParamValue("PrePayCI");
            }
            if("2".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode = ? ");
                sqlJoinUtils.addParamValue("PadPay");
            }
            if("3".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("CompeBI");
                sqlJoinUtils.addParamValue("CompeCI");
            }
            if("4".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("PrePayWfBI");
                sqlJoinUtils.addParamValue("PrePayWfCI");
            }
            if("5".equals(verifyClaimPassVo.getCompensateType())){
                sqlJoinUtils.append(" AND plnm.taskInNode in (?,?) ");
                sqlJoinUtils.addParamValue("CompeWfBI");
                sqlJoinUtils.addParamValue("CompeWfCI");
            }
        }
        
        sqlJoinUtils.append(" ORDER BY plnm.handleTime desc ");
        
        String sql = sqlJoinUtils.getSql();
        Object[] values = sqlJoinUtils.getParamValues();
        
        List<Object[]> objects = new ArrayList<Object[]>();
        try{
            objects = baseDaoService.findListBySql(sql,values);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        List<VerifyClaimPassVo> resultVoList = new ArrayList<VerifyClaimPassVo>();
        
        for(int i = 0; i<objects.size(); i++ ){
                 
            VerifyClaimPassVo vo = new VerifyClaimPassVo();

            vo.setRegistNo((String)objects.get(i)[0]);
            vo.setClaimNo((String)objects.get(i)[1]);
            vo.setCompensateNo((String)objects.get(i)[2]);
            vo.setCompensateType((String)objects.get(i)[3]);
            vo.setCreateTime((Date)objects.get(i)[4]);
            vo.setHandleTime((Date)objects.get(i)[5]);
            vo.setPolicyNo((String)objects.get(i)[6]);
            vo.setInsuredName((String)objects.get(i)[7]);
            vo.setSumRealPay((BigDecimal)objects.get(i)[8]);
            vo.setPayeeName((String)objects.get(i)[9]);
            vo.setBankOutLets((String)objects.get(i)[10]);
            vo.setAccountNo((String)objects.get(i)[11]);
            vo.setPayStatus((String)objects.get(i)[12]);           
           
            resultVoList.add(vo);           
        }
        return resultVoList;
    }

    @Override
    public List<Map<String,Object>> createExcelRecord(List<VerifyClaimPassVo> results) {
        List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        map.put("sheetName","sheet1");
        listmap.add(map);
        for(VerifyClaimPassVo resultVo:results){
            Map<String,Object> mapValue = new HashMap<String,Object>();
            mapValue.put("registNo",resultVo.getRegistNo());
            mapValue.put("claimNo",resultVo.getClaimNo());
            mapValue.put("compensateNo",resultVo.getCompensateNo());
            mapValue.put("insuredName",resultVo.getInsuredName());
            mapValue.put("compensateType",codeTranService.transCode("CompensateType",resultVo.getCompensateType()));
            mapValue.put("createTime",df.format(resultVo.getCreateTime()));
            mapValue.put("handleTime",df.format(resultVo.getHandleTime()));
            mapValue.put("policyNo", resultVo.getPolicyNo());
            mapValue.put("sumRealPay",resultVo.getSumRealPay());
            mapValue.put("payeeName",resultVo.getPayeeName());
            mapValue.put("bankOutLets",resultVo.getBankOutLets());
            mapValue.put("accountNo",resultVo.getAccountNo());
            mapValue.put("payStatus",codeTranService.transCode("PayStatus",resultVo.getPayStatus()));
           
            listmap.add(mapValue);
        }
        return listmap;
    }

    
    private Double sumpay(String registNo,PrpLCompensateVo compeVo){
    	 //理算信息
  		List<PrpLCompensateVo> PAY_CI = new ArrayList<PrpLCompensateVo>();//交强
  		List<PrpLCompensateVo> PAY_BI = new ArrayList<PrpLCompensateVo>();//商业
  		if(compeVo!=null){
  			if(Risk.DQZ.equals(compeVo.getRiskCode())){
  				PAY_CI.add(compeVo);
  			}else{
  				PAY_BI.add(compeVo);
  			}
  		}
  	    // 预付
		List<PrpLCompensateVo> compePrepayVos = compensateTaskService.queryCompensate(registNo,"Y");
		List<PrpLPrePayVo> prePay_ci = new ArrayList<PrpLPrePayVo>();// 交强预付
		List<PrpLPrePayVo> prePay_bi = new ArrayList<PrpLPrePayVo>();// 商业预付
		if(compePrepayVos != null && compePrepayVos.size() > 0){
			for(PrpLCompensateVo compePrepayVo : compePrepayVos){
				PrpLCompensateExtVo extVo = compePrepayVo.getPrpLCompensateExt();
				if(extVo != null && "1".equals(extVo.getIsCompDeduct())){
					String comNo = compePrepayVo.getCompensateNo();
					List<PrpLPrePayVo> prePayVos = compensateTaskService.queryPrePay(comNo);
					if(prePayVos != null && prePayVos.size() > 0){
						for(PrpLPrePayVo prePayVo:prePayVos){
							if(Risk.DQZ.equals(prePayVo.getRiskCode())){// 交强
								prePay_ci.add(prePayVo);
							}else{
								prePay_bi.add(prePayVo);
							}
						}
					}
				}
			}
		}
		 // 垫付
	    PrpLPadPayMainVo padPayMainVo = padPayPubService.queryPadPay(registNo,null);
	    Double sumpad=calculatePad(padPayMainVo);
	    Double sumPay_CI=0.00;
	    Double sumPay_BI=0.00;
	    Double sum=0.00;
	    if(compeVo!=null && "1101".equals(compeVo.getRiskCode())){
	        sumPay_CI=calculateSum(PAY_CI,prePay_ci,"1")+sumpad;
	        sum=sumPay_CI;
	    }else{
	    	sumPay_BI=calculateSum(PAY_BI,prePay_bi,"1");
	    	sum=sumPay_BI;
	    }
	    
	    
	    return sum;
    }
    
    /**
	 * <pre>计算垫付的金额</pre>
	 * @param padPayMainVo
	 * @modified:
	 * ☆Luwei(2016年9月30日 下午5:09:09): <br>
	 */
	private Double calculatePad(PrpLPadPayMainVo padPayMainVo){
		Double sumRealPay = 0.00;
		if(padPayMainVo != null){
			List<PrpLPadPayPersonVo> persons = padPayMainVo.getPrpLPadPayPersons();
			for(PrpLPadPayPersonVo person : persons){// 垫付
				sumRealPay += DataUtils.NullToZero(person.getCostSum()).doubleValue();
			}
		}
		return sumRealPay;
	}
	
	/**
	 * <pre>计算费用，赔款金额</pre>
	 * @param compeVoList
	 * @param prePay
	 * @param payType 1，赔款，2-费用
	 * ☆Luwei(2016年9月30日 下午5:16:14): <br>
	 */
	private Double calculateSum(List<PrpLCompensateVo> compeVoList,List<PrpLPrePayVo> prePay,String payType) {
		BigDecimal sumPay = new BigDecimal(0);
		if(compeVoList!=null&&compeVoList.size()>0){
		for(PrpLCompensateVo compeVo : compeVoList){
			if("1".equals(payType)){//赔款
				if(compeVo.getPrpLPayments()!=null && compeVo.getPrpLPayments().size()>0){
				for(PrpLPaymentVo sumRealPay : compeVo.getPrpLPayments()){// 实赔
					sumPay = sumPay.add(sumRealPay.getSumRealPay());
				}
				}
			}
			if("2".equals(payType)){//费用
				sumPay = sumPay.add(compeVo.getSumPaidFee());
//				for(PrpLChargeVo sumRealPay : compeVo.getPrpLCharges()){// 实赔
//					sumPay = sumPay.add(sumRealPay.getFeeAmt());
//				}
			}
			for(PrpLPrePayVo per : prePay){// 预付
				String feeType = per.getFeeType();
				if("2".equals(payType)&&FeeType.FEE.equals(feeType)){//费用
					sumPay = sumPay.add(per.getPayAmt());
				}
				if("1".equals(payType)&&FeeType.PAY.equals(feeType)){//赔款
					sumPay = sumPay.add(per.getPayAmt());
				}
			}
		}
		}
		return sumPay.doubleValue();
	}

	@Override
	public void updatePrpLuwNotionMainVo(PrpLuwNotionMainVo mainVo) {
		if(mainVo!=null && mainVo.getId()!=null){
		    PrpLuwNotionMain mainpo=new PrpLuwNotionMain();
		    mainpo=Beans.copyDepth().from(mainVo).to(PrpLuwNotionMain.class);
			compensateService.saveorUpdateNotionMain(mainpo);
		}
	}


    @Override
    public List<PrpLWfTaskVo> verifyToEndcasSubmit(PrpLWfTaskVo wfTaskVo,Long uwNotionMainId,String currentNode,String nextNode,String action,
                                                   SysUserVo userVo,String compeWfZeroFlag) {
		Long currentDate = System.currentTimeMillis();
		log.info("verifyToEndcasSubmit begin... ");
        String comCode = userVo.getComCode();
        String userCode = userVo.getUserCode();
        List<PrpLWfTaskVo> wfTaskVoList = null;
        if( !FlowNode.CancelAppJuPei.name().equals(nextNode)){
            if(isHeadOffice(nextNode)){
                comCode = "00010000";// 总公司人员 机构用公司本部
            }
        }
        PrpLuwNotionMainVo uwNotionMainVo = findUwNotionMain(uwNotionMainId);
        if((!VClaimType.VC_RETURN.equals(action)) && (!VClaimType.VC_AUDIT.equals(action))){// 退回 // 提交上级
			// 核赔通过
			// 提交工作流=================
			String registNo = uwNotionMainVo.getRegistNo();
			String taskType = uwNotionMainVo.getPolicyType();
			String compeNo = uwNotionMainVo.getCompensateNo();
			if( !VerifyClaimTask.CANCEL.equals(taskType)){
				if(VerifyClaimTask.COMPE_CI.equals(taskType)||VerifyClaimTask.COMPE_BI.equals(taskType)){// 处理理算、理算冲销核赔任务
					String taskInNode = uwNotionMainVo.getTaskInNode();
					if("1".equals(compeWfZeroFlag)){
						taskInNode = "CompeWfZero";
					}
					// 交强理算冲销核赔,商业理算冲销核赔-结束任务
					if( !FlowNode.CompeWfBI.equals(taskInNode)&& !FlowNode.CompeWfCI.equals(taskInNode)){
						List<PrpLWfTaskVo> wfTaskVos = new ArrayList<PrpLWfTaskVo>();
						PrpLWfTaskVo endWfTaskVo = null;
						try{
							PrpLEndCaseVo caseVo = new PrpLEndCaseVo();
							PrpLEndCase endCase = null;
							QueryRule queryRule = QueryRule.getInstance();
							queryRule.addEqual("registNo",registNo);
							queryRule.addEqual("compensateNo",compeNo);
							List<PrpLEndCase> endCasePos = databaseDao.findAll(PrpLEndCase.class,queryRule);
							if(endCasePos!=null&&endCasePos.size()>0&& !"1".equals(compeWfZeroFlag)){
								endCase = endCasePos.get(0);
							}else{// 0冲销
								QueryRule queryRule1 = QueryRule.getInstance();
								queryRule1.addEqual("registNo",registNo);
								queryRule1.addEqual("compensateNo","CompeWfZero");
								queryRule1.addDescOrder("endCaseDate");
								List<PrpLEndCase> endCaseZeroPos = databaseDao.findAll(PrpLEndCase.class,queryRule1);
								endCase = endCaseZeroPos.get(0);
							}
							if(endCase!=null){
								Beans.copy().from(endCase).to(caseVo);
								List<PrpLWfTaskVo> wfTaskEndCasVos = wfTaskHandleService.findInTask(caseVo.getRegistNo(),compeNo,
										FlowNode.EndCas.name());
								log.info("wfTaskEndCasVos size: "+( wfTaskEndCasVos!=null&&wfTaskEndCasVos.size()>0 ? wfTaskEndCasVos.size() : 0 ));
								if(wfTaskEndCasVos!=null&&wfTaskEndCasVos.size()>0){
									wfTaskVos.addAll(wfTaskEndCasVos);
								}
								List<PrpLWfTaskVo> wfTaskRecPayVos = wfTaskHandleService.findInTask(caseVo.getRegistNo(),compeNo,
										FlowNode.RecPay.name());
								if(wfTaskRecPayVos!=null&&wfTaskRecPayVos.size()>0){
									wfTaskVos.addAll(wfTaskRecPayVos);
								}
								if(wfTaskVos!=null&&wfTaskVos.size()>0){
									for(PrpLWfTaskVo wfTask:wfTaskVos){
										if(FlowNode.EndCas.toString().equals(wfTask.getSubNodeCode())){
											// 结案提交
											WfTaskSubmitVo submitEndCase = new WfTaskSubmitVo();
											submitEndCase.setFlowId(wfTask.getFlowId());
											submitEndCase.setFlowTaskId(wfTask.getTaskId());
											submitEndCase.setCurrentNode(FlowNode.EndCas);
											submitEndCase.setNextNode(FlowNode.END);
											submitEndCase.setComCode(comCode);
											submitEndCase.setTaskInUser(userCode);
											String endCaseNo = caseVo!=null ? caseVo.getEndCaseNo() : "";
											submitEndCase.setTaskInKey(endCaseNo);
											submitEndCase.setHandleIdKey(caseVo.getCompensateNo());
											setFlowStatusByCancel(submitEndCase,registNo,caseVo.getClaimNo());
											wfTaskVo = wfTaskHandleService.submitEndCase(caseVo,submitEndCase);
											endWfTaskVo = wfTaskVo;
										}
									}
								}
								if(RadioValue.RADIO_YES.equals(compeWfZeroFlag)){
									// 理算冲销核赔0结案后，查询该案件是否已经全部结案，全部结案则将单证节点提交为已处理
									List<PrpLClaimVo> claimVoList = claimTaskService.findClaimListByRegistNo(registNo,"1");
									boolean endCaseFlag = true;
									for(PrpLClaimVo claimVo:claimVoList){
										if(claimVo.getCaseNo()==null){
											endCaseFlag = false;
											break;
										}
									}
									if(endCaseFlag){
										// 提交单证节点为已处理
										wfTaskHandleService.submitCertifyAfterEndCase(registNo);
									}
								}
							}
						}catch(Exception e){
							log.error("结案提交报错：",e);
							// 回滚工作流
							// 1,回滚核赔2,删除wftaskIn的结案任务
							PrpLWfTaskVo taskInVo = null;
							if(endWfTaskVo!=null){
								// 把结案变正在处理
								PrpLWfTaskVo wfTaskVerifyVo = wfTaskHandleService.queryTask(endWfTaskVo.getUpperTaskId().doubleValue());// in表的结案taskid
								wfTaskVerifyVo = wfTaskHandleService.rollBackTaskByEndVo(wfTaskVerifyVo);
								try{
									taskInVo = wfTaskHandleService.findTaskIn(wfTaskVerifyVo.getUpperTaskId().doubleValue());// 核赔节点
								}catch(Exception e1){
										log.error("理算核赔提交回滚失败！",e);
									throw new IllegalArgumentException("理算核赔提交回滚失败！"+e.getMessage());
								}
								if(taskInVo==null){// in表没有核赔，已经处理
									wfTaskHandleService.rollBackTask(wfTaskVerifyVo);
								}else{
									// 直接删除结案节点
									wfTaskHandleService.deteleTaskVo(taskInVo);
								}
							}else{
								List<PrpLWfTaskVo> endTaskList = wfTaskHandleService.findEndTask(uwNotionMainVo.getRegistNo(),
										uwNotionMainVo.getCompensateNo(),FlowNode.EndCas);
								if(endTaskList!=null&&endTaskList.size()>0){
									endWfTaskVo = endTaskList.get(0);
									// 把结案变正在处理
									PrpLWfTaskVo wfTaskVerifyVo = wfTaskHandleService.queryTask(endWfTaskVo.getUpperTaskId().doubleValue());// in表的结案taskid
									wfTaskVerifyVo = wfTaskHandleService.rollBackTaskByEndVo(wfTaskVerifyVo);
									try{
										taskInVo = wfTaskHandleService.findTaskIn(wfTaskVerifyVo.getUpperTaskId().doubleValue());// 核赔节点
									}catch(Exception e1){
											log.error("理算核赔提交回滚失败！",e);
										throw new IllegalArgumentException("理算核赔提交回滚失败！"+e.getMessage());
									}
									if(taskInVo==null){// in表没有核赔，已经处理
										wfTaskHandleService.rollBackTask(wfTaskVerifyVo);
									}else{
										// 直接删除结案节点
										wfTaskHandleService.deteleTaskVo(wfTaskVerifyVo);
									}
								}else{
									List<PrpLWfTaskVo> wfTaskEndCasVos = wfTaskHandleService.findInTask(uwNotionMainVo.getRegistNo(),
											uwNotionMainVo.getCompensateNo(),FlowNode.EndCas.name());
									if(wfTaskEndCasVos!=null&&wfTaskEndCasVos.size()>0){
										endWfTaskVo = wfTaskEndCasVos.get(0);
										try{
											taskInVo = wfTaskHandleService.findTaskIn(endWfTaskVo.getUpperTaskId().doubleValue());// 核赔节点
										}catch(Exception e1){
											log.error("理算核赔提交回滚失败！",e);
											throw new IllegalArgumentException("理算核赔提交回滚失败！"+e.getMessage());
										}
										if(taskInVo==null){// in表没有核赔，已经处理
											wfTaskHandleService.rollBackTask(endWfTaskVo);
										}else{
											// 直接删除结案节点
											wfTaskHandleService.deteleTaskVo(endWfTaskVo);
										}
									}
								}
							}
							// 回滚追偿
							List<PrpLWfTaskVo> wfTaskRecPayEndVos = wfTaskHandleService.findEndTask(uwNotionMainVo.getRegistNo(),
									uwNotionMainVo.getCompensateNo(),FlowNode.RecPay);
							if(wfTaskRecPayEndVos!=null&&wfTaskRecPayEndVos.size()>0){
								// 直接删除追偿节点
								wfTaskHandleService.deteleTaskVo(wfTaskRecPayEndVos.get(0));
							}else{
								List<PrpLWfTaskVo> wfTaskRecPayInVos = wfTaskHandleService.findInTask(uwNotionMainVo.getRegistNo(),
										uwNotionMainVo.getCompensateNo(),FlowNode.RecPay.name());
								if(wfTaskRecPayInVos!=null&&wfTaskRecPayInVos.size()>0){
									// 直接删除追偿节点
									wfTaskHandleService.deteleTaskVo(wfTaskRecPayInVos.get(0));
								}
							}
							throw new IllegalArgumentException("理算核赔提交失败！"+e.getMessage());
						}
					}
				}
			}
		}
		log.info("verifyToEndcasSubmit end... , cost time={} ms ",System.currentTimeMillis()-currentDate);
		return wfTaskVoList;
	}

	
    @Override
    public void autoVerifyClaimToFlowEndCase(SysUserVo userVo,PrpLCompensateVo prpLCompensateVo,Long uwNotionMainId) {

        String currentNode = FlowNode.VClaim_BI_LV0.toString();
        if("1101".equals(prpLCompensateVo.getRiskCode())){
            currentNode = FlowNode.VClaim_CI_LV0.toString();
        }
        // 查询当前在in表的核赔节点taskid
        PrpLWfTaskVo wfTaskVo = wfTaskHandleService.findWftaskInByRegistnoAndSubnode(prpLCompensateVo.getRegistNo(),currentNode);
       /* if(wfTaskVo==null){
            throw new IllegalArgumentException("没找到工作流数据！");
        }*/
        String nextNode = FlowNode.EndCas.toString();
        String action = VClaimType.VC_ADOPT;
        String compeWfZeroFlag = "0";
        this.verifyToEndcasSubmit(wfTaskVo, uwNotionMainId, currentNode, nextNode, action, userVo, compeWfZeroFlag);
    }

	@Override
	public PrpLPadPayMainVo getPadPayByCompeNo(String registNo, String compeNo) {
		return padPayPubService.queryPadPayByCompeNo(registNo,compeNo);
	}
	
	@Override
	public void sendVClaimToSH(String compensateNo,String comCode,CiClaimPlatformTaskVo platformTaskVo){
		PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(compensateNo);
		sendVClaimToSH.sendVClaimToSH(compensateVo,comCode,platformTaskVo);
	}
	@Override
	public void sendVClaimCIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo){
		sendVClaimToAll.sendVClaimCIToPlatform(compeNo,platformTaskVo);
	}
	@Override
	public void sendVClaimBIToPlatform(String compeNo,CiClaimPlatformTaskVo platformTaskVo){
		sendVClaimToAll.sendVClaimBIToPlatform(compeNo,platformTaskVo);
	}
	@Override
	public void sendEndCaseToSHBySubmit(String endCaseNo,CiClaimPlatformTaskVo platformTaskVo){
		PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByPK(null,endCaseNo);
		sendEndCaseToSH.sendEndCaseToSHBySubmit(endCaseVo,platformTaskVo);
	}
	@Override
	public void sendEndCaseToPlatform(String endCaseNo,CiClaimPlatformTaskVo platformTaskVo){
		PrpLEndCaseVo endCaseVo = endCasePubService.findEndCaseByPK(null,endCaseNo);
		sendEndCaseToAll.sendEndCaseToPlatform(endCaseVo,platformTaskVo);
	}

    /**
     * 结案送再保
     * @param claimNo 立案号
     * @author mfn 2019-11-18 18:19:23
     */
	public void sendEndCaseToReins(String claimNo) {
        // 再保处理结案业务 niuqiang businessType=0
        try {
            if (claimNo != null) {
                PrpLClaimVo claimVo = claimTaskService.findClaimVoByClaimNo(claimNo);
                if (claimVo != null) {
                    ClaimInterfaceLogVo claimInterfaceLogVo = new ClaimInterfaceLogVo(); // 填写日志表
                    claimInterfaceLogVo.setClaimNo(claimVo.getClaimNo());
                    claimInterfaceLogVo.setRegistNo(claimVo.getRegistNo());
                    claimInterfaceLogVo.setComCode(claimVo.getComCode());
                    claimInterfaceLogVo.setCreateUser(claimVo.getCreateUser());
                    claimInterfaceLogVo.setCreateTime(new Date());
                    claimInterfaceLogVo.setOperateNode(FlowNode.EndCas.getName());
                    interfaceAsyncService.TransDataForReinsCaseVo("0", claimVo, claimInterfaceLogVo);
}
            }
        } catch (Exception e) {
            log.info("结案送再保失败！", e);
        }
    }
	
	/**
     * vat回写价税信息推送再保
     * @param registNo
     * @param riskCode
     */
	@Override
	public void sendVatBackSumAmountNTToReins(String registNo, String riskCode) {
		// vat 回写根据payeeid+riskCode+registNo+compensateNo确定次数

		// 查询报案号、险种下所有的未送再保的最小次数和最大次数
		Object[] timesObj = getNeedToReinsTimes(registNo, riskCode);

		if (timesObj != null && timesObj.length == 2) {

			BigDecimal minTimesBD =  timesObj[0] != null ? (BigDecimal)timesObj[0] : new BigDecimal("0");
			BigDecimal maxTimesBD =  timesObj[1] != null ? (BigDecimal)timesObj[1] : new BigDecimal("0");

			Integer minTimes = Integer.parseInt(minTimesBD.toString());
			Integer maxTimes = Integer.parseInt(maxTimesBD.toString());
			if (minTimes > 0 && maxTimes > 0) {
				// 获取核赔通过时的险别拆分记录
				List<PrplPayeeKindPaymentVo> lastTimesKindPaymentList = this.findPayeeKindPaymentsByTimes(registNo, riskCode, 0);

				for (int i = minTimes; i <= maxTimes; i++) {
					// 查询每次的数据和核赔通过时的做对比
					List<PrplPayeeKindPaymentVo> curTimesKindPaymentList = this.findPayeeKindPaymentsByTimes(registNo, riskCode, i);

					// 根据比对的结果判断是否要送再保
					compareTwoTimesPayeeRateAndToReins(registNo, riskCode, i,curTimesKindPaymentList, lastTimesKindPaymentList);

					// 更新轨迹表送再保标识
					updatePayeeKindPaymentToReinsFlag(curTimesKindPaymentList);
				}
			}
		}

	}
	
	
	/**
     * vat回写价税信息推送再保
     * @param keyString
     * @param casePayeeKindPayments
     */
	@Override
	public void sendVatBackSumAmountNTToReins(String keyString,List<PrplPayeeKindPaymentVo> casePayeeKindPayments) {

		String registNo = keyString.split("#")[0];
		String riskCode = keyString.split("#")[1];
		int curTimes = Integer.parseInt(keyString.split("#")[2]);
		if (!casePayeeKindPayments.isEmpty()) {
			// 获取核赔通过时的险别拆分记录
			List<PrplPayeeKindPaymentVo> lastTimesKindPaymentList = this.findPayeeKindPaymentsByTimes(registNo, riskCode, 0);

			// 根据比对的结果判断是否要送再保
			compareTwoTimesPayeeRateAndToReins(registNo, riskCode, curTimes,casePayeeKindPayments, lastTimesKindPaymentList);

			// 存储送再保险别记录
			saveCasePayeeKindPayments(casePayeeKindPayments);
	

		}

	}

	/**
	 * 获取需要送再保的最小次数和最大次数
	 * @param registNo
	 * @param riskCode
	 * @return
	 */
	private Object[] getNeedToReinsTimes(String registNo, String riskCode) {
		
		Object[] resultObj  = null;
		SqlJoinUtils sqlJoinUtils = new SqlJoinUtils();
		sqlJoinUtils.append(" select min(p.times),max(p.times) from claimuser.prplpayeekindpayment p where p.toreinsflag = '0' and p.registno = ? and p.riskcode = ? ");
		
		List<Object> paramValues = new ArrayList<Object>();
		
		paramValues.add(registNo);
		paramValues.add(riskCode);
		
		try {
			List<Object[]> objects = baseDaoService.findListBySql(sqlJoinUtils.getSql(),paramValues.toArray());
			
			if(objects != null && !objects.isEmpty()){
				resultObj = objects.get(0);
				
			}
			
		} catch (Exception e) {
			log.error("查询vat回写次数失败！sql:{}{}", sqlJoinUtils.getSql(),e.getMessage());
		}
		return resultObj;
		
	}
	
	/**
     * 获取指定次数的收款人险别拆分信息
     * @param registNo
     * @param riskCode
     * @param times
     * @return
     */
    public List<PrplPayeeKindPaymentVo> findPayeeKindPaymentsByTimes(String registNo,String riskCode,int times){
    	List<PrplPayeeKindPaymentVo> payeeKindPaymentList = new ArrayList<PrplPayeeKindPaymentVo>();
    	
    	QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo", registNo);
		queryRule.addEqual("riskCode", riskCode);
		queryRule.addEqual("times", Long.parseLong(times+""));
		
		List<PrplPayeeKindPayment> kindPaymentList = databaseDao.findAll(PrplPayeeKindPayment.class,queryRule);
		
		if(kindPaymentList!=null && kindPaymentList.size()>0){
			payeeKindPaymentList = Beans.copyDepth().from(kindPaymentList).toList(PrplPayeeKindPaymentVo.class);
		}
    	
    	return payeeKindPaymentList;
    } 
	
    /**
     * 比对两次的税率，如果有差值则需要送再保
     * @param curTimesKindPaymentList
     * @param lastTimesKindPaymentList
     */
	private void compareTwoTimesPayeeRateAndToReins(String registNo,String riskCode,int curTime,List<PrplPayeeKindPaymentVo> curTimesKindPaymentList,List<PrplPayeeKindPaymentVo> lastTimesKindPaymentList){
		
		//存在预付差值 key为 lossId
	    Map<Long,BigDecimal> kindPrePayTaxDiffrentValueMap = new HashMap<Long, BigDecimal>();
		
		//存放赔款差值 key为kindCode
		Map<String,BigDecimal> kindPaymentTaxDiffrentValueMap = new HashMap<String, BigDecimal>();
		//存在费用差值 key为 payeeId-kindCode-chargeCode
		Map<String,BigDecimal> kindChargeTaxDiffrentValueMap = new HashMap<String, BigDecimal>();
		
		//是否存在差值， 用来判断是否需要送再保
		boolean hasDiffrent = false;
		if(curTimesKindPaymentList != null  && !curTimesKindPaymentList.isEmpty() && lastTimesKindPaymentList != null && !lastTimesKindPaymentList.isEmpty()){
			for(PrplPayeeKindPaymentVo curPayeeKindPayment:curTimesKindPaymentList){
				for(PrplPayeeKindPaymentVo lastPayeeKindPayment:lastTimesKindPaymentList){
					// 同一个收款人、险别、计算书进行比较,需要考虑实际的抵扣金额
					if (curPayeeKindPayment.getPayeeId().equals(lastPayeeKindPayment.getPayeeId())
							&& curPayeeKindPayment.getKindCode().equals(lastPayeeKindPayment.getKindCode()) && curPayeeKindPayment.getCompensateNo().equals(lastPayeeKindPayment.getCompensateNo())) {
						
						//判断预赔,预付能够精确到险别以及标的，所以 预付的只能和预付的历史记录比较
						if(curPayeeKindPayment.getCompensateNo().startsWith("Y")){
							if (!curPayeeKindPayment.getAddTaxRate().equals(lastPayeeKindPayment.getAddTaxRate())) {
								hasDiffrent = true;
								
								//计算两者含税金额的比例
								BigDecimal comparePersents = curPayeeKindPayment.getKindSumPay().divide(lastPayeeKindPayment.getKindSumPay(),2,BigDecimal.ROUND_HALF_UP);
								
								BigDecimal taxDiffrentValue = curPayeeKindPayment.getAddTaxValue()
										.subtract((lastPayeeKindPayment.getAddTaxValue()).multiply(comparePersents));
								kindPrePayTaxDiffrentValueMap.put(curPayeeKindPayment.getLossId(), taxDiffrentValue);
								
								if(kindPrePayTaxDiffrentValueMap.containsKey(curPayeeKindPayment.getLossId())){
									kindPrePayTaxDiffrentValueMap.put(curPayeeKindPayment.getLossId(), kindPrePayTaxDiffrentValueMap.get(curPayeeKindPayment.getLossId()).add(taxDiffrentValue));
								}else{
									kindPrePayTaxDiffrentValueMap.put(curPayeeKindPayment.getLossId(),taxDiffrentValue);
								}
							}
							
							
						}else if((curPayeeKindPayment.getChargeCode()!=null && curPayeeKindPayment.getChargeCode().equals(lastPayeeKindPayment.getChargeCode()))){
							//判断费用
							StringBuffer keyBf =new StringBuffer(curPayeeKindPayment.getPayeeId().toString()).append("-").append(curPayeeKindPayment.getKindCode()).append("-").append(curPayeeKindPayment.getChargeCode());
							if (!curPayeeKindPayment.getAddTaxRate().equals(lastPayeeKindPayment.getAddTaxRate())) {
								hasDiffrent = true;
								
								//计算两者含税金额的比例
								BigDecimal comparePersents = curPayeeKindPayment.getKindSumPay().divide(lastPayeeKindPayment.getKindSumPay(),2,BigDecimal.ROUND_HALF_UP);
								
								BigDecimal taxDiffrentValue = curPayeeKindPayment.getAddTaxValue()
										.subtract((lastPayeeKindPayment.getAddTaxValue()).multiply(comparePersents));
								
								if(kindChargeTaxDiffrentValueMap.containsKey(keyBf.toString())){
									kindChargeTaxDiffrentValueMap.put(keyBf.toString(), kindChargeTaxDiffrentValueMap.get(keyBf.toString()).add(taxDiffrentValue));
								}else{
									kindChargeTaxDiffrentValueMap.put(keyBf.toString(),taxDiffrentValue);
								}
		
							}
						}else if(curPayeeKindPayment.getChargeCode()== null && lastPayeeKindPayment.getChargeCode() == null){
							//判断赔款
							if (!curPayeeKindPayment.getAddTaxRate().equals(lastPayeeKindPayment.getAddTaxRate())) {
								hasDiffrent = true;
								//回写的金额与 首次参与扣税金额的占比
								BigDecimal comparePersents = curPayeeKindPayment.getKindSumPay().divide(lastPayeeKindPayment.getKindSumPay(),2,BigDecimal.ROUND_HALF_UP);
								
								
								BigDecimal taxDiffrentValue = curPayeeKindPayment.getAddTaxValue()
								.subtract((lastPayeeKindPayment.getAddTaxValue()).multiply(comparePersents));
								if(kindPaymentTaxDiffrentValueMap.containsKey(curPayeeKindPayment.getKindCode())){
									kindPaymentTaxDiffrentValueMap.put(curPayeeKindPayment.getKindCode(), kindPaymentTaxDiffrentValueMap.get(curPayeeKindPayment.getKindCode()).add(taxDiffrentValue));
								}else{
									kindPaymentTaxDiffrentValueMap.put(curPayeeKindPayment.getKindCode(),taxDiffrentValue);
								}
								
							}
							
						}
	
					}
				}
			}
		}
		
		if(hasDiffrent){
            if ("1101".equals(riskCode)) {
                riskCode = "11";
            }
			 PrpLClaimVo claimVo = claimTaskService.getClaimVo(registNo, riskCode);
			 PrpLCompensateVo compensateVo = compensateService.findCompByClaimNo(claimVo.getClaimNo(), "N");
			 try {
				claimToReinsuranceService.vatWriteBackTransDataForCompensateVo(claimVo, compensateVo,curTime,kindPrePayTaxDiffrentValueMap,kindPaymentTaxDiffrentValueMap,kindChargeTaxDiffrentValueMap);
			 } catch (Exception e) {
				log.error("vat回写送再保异常,立案号{}{}",claimVo.getClaimNo(),e.getMessage());;
			 }
			 
		}
		
	}
	
	/**
	 * 修改拆分表送再保标识
	 * 
	 * @param curTimesKindPaymentList
	 */
	private void updatePayeeKindPaymentToReinsFlag(List<PrplPayeeKindPaymentVo> curTimesKindPaymentList){
		if(curTimesKindPaymentList != null && !curTimesKindPaymentList.isEmpty()){
			List<PrplPayeeKindPayment> kindPaymentList = Beans.copyDepth().from(curTimesKindPaymentList).toList(PrplPayeeKindPayment.class);
			for(PrplPayeeKindPayment payeeKindPayment:kindPaymentList){
				/*payeeKindPayment.setToReinsFlag(YN01.Y);
			    databaseDao.update(PrplPayeeKindPayment.class, payeeKindPayment);*/
			    
			    if(payeeKindPayment.getId()  != null){
					String sql = "update claimuser.prplpayeekindpayment p set p.toreinsflag = '1' where p.id = "+payeeKindPayment.getId();
					 baseDaoService.executeSQL(sql);
				}
			    
			}
		}
	}
	
	/**
	 * 保存vat推送的赔款险别记录
	 * @param caseKindPaymentList
	 */
	private void  saveCasePayeeKindPayments(List<PrplPayeeKindPaymentVo> caseKindPaymentList){
		List<PrplPayeeKindPayment> kindPaymentList = Beans.copyDepth().from(caseKindPaymentList).toList(PrplPayeeKindPayment.class);
		for(PrplPayeeKindPayment payeeKindPayment:kindPaymentList){
			payeeKindPayment.setToReinsFlag(YN01.Y);
		    databaseDao.save(PrplPayeeKindPayment.class, payeeKindPayment);
		}
	}
	
}


