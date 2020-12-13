package ins.sino.claimcar.claim.services;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.utils.HttpClientHander;
import ins.platform.utils.XstreamFactory;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.certify.service.CertifyService;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.check.vo.PrpLCheckDutyVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.claim.po.PrpLCompensate;
import ins.sino.claimcar.claim.po.PrpLCompensateExt;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.service.CompensateHandleService;
import ins.sino.claimcar.claim.service.CompensateHandleServiceIlogService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayPersonVo;
import ins.sino.claimcar.claim.vo.PrplcomcontextVo;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.endcase.service.EndCasePubService;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfFlowQueryService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.ilog.defloss.vo.LIlogRuleResVo;
import ins.sino.claimcar.ilog.rule.service.RuleReturnDataSaveService;
import ins.sino.claimcar.ilog.rule.vo.IlogDataProcessingVo;
import ins.sino.claimcar.ilog.vclaim.vo.AccidentResponsibility;
import ins.sino.claimcar.ilog.vclaim.vo.ItemKind;
import ins.sino.claimcar.ilog.vclaim.vo.PaymentInfo;
import ins.sino.claimcar.ilog.vclaim.vo.RepairInfo;
import ins.sino.claimcar.ilog.vclaim.vo.VClaimReqVo;
import ins.sino.claimcar.losscar.service.DeflossHandleService;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarRepairVo;
import ins.sino.claimcar.lossperson.service.PersTraceDubboService;
import ins.sino.claimcar.lossperson.service.PersTraceService;
import ins.sino.claimcar.lossperson.vo.PrpLDlossPersTraceMainVo;
import ins.sino.claimcar.lossprop.service.PropTaskService;
import ins.sino.claimcar.lossprop.vo.PrpLdlossPropMainVo;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.manager.vo.PrpdIntermMainVo;
import ins.sino.claimcar.other.vo.PrpLAutoVerifyVo;
import ins.sino.claimcar.regist.service.PrpLCMainService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLClaimDeductVo;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 车险车辆核赔对外服务接口
 * <pre></pre>
 * @author ★zhujunde
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("compensateHandleServiceIlogService")
public class CompensateHandleServiceIlogImpl implements CompensateHandleServiceIlogService {
    private static Logger logger = LoggerFactory.getLogger(CompensateHandleServiceIlogImpl.class);

    private static final String CheckRuleForCarClaimServlet = "CheckRuleForCarClaimServlet";

    @Autowired
    RegistService registService;
    @Autowired
    CompensateService compensateService;
    @Autowired
    PersTraceService persTraceService;
    @Autowired
    LossCarService lossCarService;
    @Autowired
    CertifyService certifyService;
    @Autowired
    CompensateHandleService compensateHandleService;
    @Autowired
    PayCustomService payCustomService;
    @Autowired
    EndCasePubService endCasePubService;
    @Autowired
    ClaimTaskService claimService;
    @Autowired
    private CheckTaskService checkTaskService;
    @Autowired
    WfTaskHandleService wfTaskHandleService;
    @Autowired
    WfFlowQueryService wfFlowQueryService;
    @Autowired
    ManagerService managerService;
    @Autowired
    PrpLCMainService prpLCMainService;
    @Autowired
    RegistQueryService registQueryService;
    @Autowired
    RegistTmpService registTmpService;
    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    private RuleReturnDataSaveService ruleReturnDataSaveService;
    @Autowired
    PersTraceDubboService persTraceDubboService;
    @Autowired
    DeflossHandleService deflossHandleService;
    @Autowired
    private ClaimTaskService claimTaskService;
    @Autowired
    PropTaskService propTaskService;
    @Override
    public LIlogRuleResVo organizaForCompensate(String compensateId,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo,PrpLWfTaskVo wfTaskVo,String isSubmitHeadOffice) throws Exception{
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateId);
        String registNo = compensateVo.getRegistNo();
        PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        vClaimReqVo.setCompensateNo(compensateVo.getCompensateNo());
        vClaimReqVo.setRegistNo(registNo);
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        if(isSubmitHeadOffice != null){
        	vClaimReqVo.setExistHeadOffice(isSubmitHeadOffice);
        }  else{
        	vClaimReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        vClaimReqVo.setCallNode(callNode);
        vClaimReqVo.setComCode(compensateVo.getComCode());
        vClaimReqVo.setRiskCode(compensateVo.getRiskCode());
        // 标的车全损标识
        vClaimReqVo.setIsTotalLoss("");
        // 操作类型 1-自动审核  2-人工审核
        vClaimReqVo.setOperateType(operateType);
        // 出险日期
        vClaimReqVo.setDamageDate(format.format(prpLRegistVo.getDamageTime()));

        if(compensateVo.getSumAmt() != null){
            vClaimReqVo.setAdjustmentAmount(compensateVo.getSumAmt().abs().toString());//当次理算金额
            vClaimReqVo.setAdjustAmount(compensateVo.getSumAmt().abs().toString());//理算提交总赔款
        }
        BigDecimal sumAmount = new BigDecimal(0);//理算提交总金额
        if("04".equals(callNode)){
            if(compensateVo.getSumAmt() != null){
                sumAmount = sumAmount.add(compensateVo.getSumAmt().abs());
            }
            if(compensateVo.getSumFee() != null){
                sumAmount = sumAmount.add(compensateVo.getSumFee().abs());
            }
        }
        // 根据报案号查询车辆定损主表及其子表数据
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        // callNode: 00-正常核赔 01-预付 02-垫付 03-预付冲销 04-理算冲销 05-立案注销拒赔 99-其他
        if("00".equals(callNode)){
            List<PrpLCompensateVo> prpLCompensateVoList = compensateService.findCompensatevosByclaimNo(compensateVo.getClaimNo());
            for(PrpLCompensateVo lCompensateVo : prpLCompensateVoList){
                if(!"7".equals(lCompensateVo.getUnderwriteFlag())){
                    if(lCompensateVo.getSumAmt() != null){
                        sumAmount = sumAmount.add(lCompensateVo.getSumAmt().abs());
                    }
                    if(lCompensateVo.getSumFee() != null){
                        sumAmount = sumAmount.add(lCompensateVo.getSumFee().abs());
                    }
                }
            }
            //vClaimReqVo.setCurpowerLevel("");//当前核赔级别
            // 出险时间 小时 分钟
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String damageTime = format1.format(prpLRegistVo.getDamageTime());
            damageTime = damageTime.substring(11,damageTime.length());
            String[] damageHourMinute = damageTime.split(":");
            if(damageHourMinute.length > 1){
                String damageHour = damageHourMinute[0];
                vClaimReqVo.setDamageHour(damageHour);
                String damageMinute = damageHourMinute[1];
                vClaimReqVo.setDamageMinute(damageMinute);
            }

            // 是否涉及人伤标识
            List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
            if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size() > 0){
                vClaimReqVo.setCasualtiesCaseFlag("1");
            }else{
                vClaimReqVo.setCasualtiesCaseFlag("0");
            }

            // 修理厂类型 001-特约维修站 002-一类修理厂  003-二类修理厂
            List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
            vClaimReqVo.setRepairFactoryType("0");
            if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
                for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
                    if("001".equals(vo.getRepairFactoryType())){//是特约
                        vClaimReqVo.setRepairFactoryType(vo.getRepairFactoryType());
                        // 只要有一条记录是特约修理厂，该标识就会被置为1，后面的不用循环了
                        break;
                    }
                }
            }

            // 交强与商业的拒赔标识 0-否  1-是
            PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNo);
            vClaimReqVo.setIsJQFraud(prpLCertifyMainVo.getIsJQFraud());
            vClaimReqVo.setIsSYFraud(prpLCertifyMainVo.getIsSYFraud());

            // 诉讼标识 0-否 1-是
            vClaimReqVo.setLawsuitFlag(compensateVo.getLawsuitFlag());
            if("3".equals(compensateVo.getCaseType())){
                vClaimReqVo.setSubrogationFlag("1");
            }else{
                vClaimReqVo.setSubrogationFlag("0");
            }
            // 通融标识
            vClaimReqVo.setAllowFlag(compensateVo.getAllowFlag());
            // 是否发起追偿标识
            vClaimReqVo.setRecoveryFlag(compensateVo.getRecoveryFlag());
            // 是否欺诈标识
            vClaimReqVo.setIsCheat(prpLCertifyMainVo.getIsFraud());
            // 是否调查案件标识
            vClaimReqVo.setSurveyFlag(prpLCertifyMainVo.getSurveyFlag());

            // 是否可疑标识
            PrplcomcontextVo prplcomcontextVo = compensateHandleService.findPrplcomcontextByCompensateNo(compensateVo.getCompensateNo(),"C");
            if(prplcomcontextVo!=null&&StringUtils.isNotBlank(prplcomcontextVo.getFlag())){
                vClaimReqVo.setIsFlagN(prplcomcontextVo.getFlag());
            }else{
                vClaimReqVo.setIsFlagN("");
            }

            List<PrpLWfTaskVo>  outList = wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo, FlowNode.Chk.name());
            vClaimReqVo.setIsAssessmentSurvey("0");
            vClaimReqVo.setIsSurveyCase("0");
            if(outList != null && outList.size() > 0){
                PrpLWfTaskVo vo = outList.get(0);
                PrpdIntermMainVo prplIntermMainVo = managerService.findIntermByUserCode(vo.getHandlerUser());
                if(prplIntermMainVo != null){
                	// 是否公估查勘标识
                    vClaimReqVo.setIsAssessmentSurvey("1");
                }
                if("1".equals(vo.getSubCheckFlag()) || "2".equals(vo.getSubCheckFlag())){
                    // 是否代查勘标识
                	vClaimReqVo.setIsSurveyCase("1");
                }
            }

            // 车物总损失(含预付)
            BigDecimal propertySumRealFeeAmount = BigDecimal.ZERO;
            // 人伤总损失(含预付)
            BigDecimal personSumRealFeeAmount = BigDecimal.ZERO;

            // 施救费总金额（包含车辆施救费与财产施救费）
            BigDecimal rescueFeeAmount = new BigDecimal(0);
            Map<String,String> riskCodeMap = new HashMap<String,String>();//ItemKindList取值规则变更，取值本次理算赔款涉及险别
            List<PrpLLossItemVo> prpLLossItemVoList = compensateVo.getPrpLLossItems();//车
            for(PrpLLossItemVo vo : prpLLossItemVoList){
                if(vo.getRescueFee() != null){
                	// 统计所有车辆损失记录的施救费金额
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }

                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//预付垫付金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//核定金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//理算赔款金额（包含预付垫付金额）大于0的险别记录才上传
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }

                propertySumRealFeeAmount.add(lossItem_sumRealPay);
            }
            List<PrpLLossPropVo>  prpLLossPropVoList = compensateVo.getPrpLLossProps();//财
            for(PrpLLossPropVo vo : prpLLossPropVoList){
                if(vo.getRescueFee() != null){
                	// 统计所有财产损失的施救费金额
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }

                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//预付垫付金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//核定金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//理算赔款金额（包含预付垫付金额）大于0的险别记录才上传
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }
                propertySumRealFeeAmount.add(lossItem_sumRealPay);
            }

            List<PrpLLossPersonVo> prpLLossPersonVoList = compensateVo.getPrpLLossPersons();//人
            for(PrpLLossPersonVo vo : prpLLossPersonVoList){
                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//预付垫付金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//核定金额
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//理算赔款金额（包含预付垫付金额）大于0的险别记录才上传
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }
                personSumRealFeeAmount.add(lossItem_sumRealPay);
            }
            vClaimReqVo.setRescueFeeAmount(rescueFeeAmount.toString());
            vClaimReqVo.setPropertySumAmount(propertySumRealFeeAmount.toString());
            vClaimReqVo.setPersonSumAmount(personSumRealFeeAmount.toString());


            // 是否预付类型
            PrpLCompensateVo compensateYVo = compensateService.findCompByClaimNo(compensateVo.getClaimNo(),"Y");
            if(compensateYVo != null){
                vClaimReqVo.setIsPrepaidType("1");
            }else{
                vClaimReqVo.setIsPrepaidType("0");
            }

            // 重大赔案上报标识 （查勘、定损、人伤跟踪）有一个环节发起重案，该标识即会被置为1
            vClaimReqVo.setMajorcaseFlag("0");
            //查勘的重大赔案
            PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
            if("1".equals(checkVo.getMajorCaseFlag())){
                vClaimReqVo.setMajorcaseFlag("1");
            }
            //人伤的重大赔案
            if (!"1".equals(vClaimReqVo.getMajorcaseFlag())) {
            	List<PrpLDlossPersTraceMainVo> persTraceMainVos = persTraceDubboService.findPersTraceMainVoList(registNo);
            	if(persTraceMainVos != null && persTraceMainVos.size() > 0 ){
            		for(PrpLDlossPersTraceMainVo vo : persTraceMainVos){
            			if("1".equals(vo.getMajorcaseFlag())){
            				vClaimReqVo.setMajorcaseFlag("1");
            			}
            		}
            	}
            }
            //定损的重大赔案
            if (!"1".equals(vClaimReqVo.getMajorcaseFlag())) {
            	List<PrpLDlossCarMainVo> prpLDlossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo); //带入标的车车牌
            	for(PrpLDlossCarMainVo vo : prpLDlossCarMainList){
            		if("1".equals(vo.getIsMajorCase())){
            			vClaimReqVo.setMajorcaseFlag("1");
            		}
            	}
            }

            // 报案号下收款人条数和收款人类型的收集
            List<PrpLPayCustomVo> prpLPayCustomVo = payCustomService.findPayCustomVoByRegistNo(registNo);
            List<PaymentInfo> paymentInfoList = new ArrayList<PaymentInfo>();
            if(prpLPayCustomVo != null && prpLPayCustomVo.size() > 0){
                vClaimReqVo.setPayeeInfoNum(prpLPayCustomVo.size());
                // 收集收款人信息（暂包含收款人类型）
                for (PrpLPayCustomVo payCustom : prpLPayCustomVo) {
                	PaymentInfo paymentInfo = new PaymentInfo();
                	paymentInfo.setPayobjectkind(payCustom.getPayObjectKind());
                	paymentInfoList.add(paymentInfo);
                }
            }else{
                vClaimReqVo.setPayeeInfoNum(0);
            }
            vClaimReqVo.setPaymentInfoList(paymentInfoList);

            // 外修项目数据收集
            // 查询车辆定损主表数据
            List<PrpLDlossCarMainVo> prpLDlossCarMainVos = deflossHandleService.findLossCarMainByRegistNo(registNo);
            // 准备修理信息
            List<RepairInfo> repairInfoList = new ArrayList<RepairInfo>();
            if (prpLDlossCarMainVos != null && prpLDlossCarMainVos.size() > 0) {
            	for (PrpLDlossCarMainVo vo : prpLDlossCarMainVos) {
            		List<PrpLDlossCarRepairVo> repairVoList = vo.getPrpLDlossCarRepairs();
            		if (repairVoList != null && repairVoList.size() > 0) {
            			// 获取修理类型并装进修理信息中
            			for (PrpLDlossCarRepairVo repairVo : repairVoList) {
            				RepairInfo repairInfo = new RepairInfo();
            				repairInfo.setRepairFlag(repairVo.getRepairFlag());
            				repairInfoList.add(repairInfo);
            			}
            		}
            	}
            }
            vClaimReqVo.setRepairInfoList(repairInfoList);


            // 出险次数待续
            Map<String, String> registRiskInfoMap = new HashMap<String, String>();
            if (!StringUtils.isEmpty(registNo)) {
                registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
            }

            // 交强与商业出险次数
            String ciDangerNum = registRiskInfoMap.get("CI-DangerNum");
            if(StringUtils.isNotBlank(ciDangerNum)){
                vClaimReqVo.setJqDamagTime(ciDangerNum);
            }else{
                vClaimReqVo.setJqDamagTime("0");
            }
            String biDangerNum = registRiskInfoMap.get("BI-DangerNum");
            if(StringUtils.isNotBlank(biDangerNum)){
                vClaimReqVo.setSyDamagTime(biDangerNum);
            }else{
                vClaimReqVo.setSyDamagTime("0");
            }

            // 直接理赔费用（本次赔付费用）
            if(compensateVo.getSumPaidFee() != null){
                vClaimReqVo.setSumPaidFee(compensateVo.getSumPaidFee().toString());
            }

            // 有效车辆定损任务数量
            List<PrpLWfTaskVo>  dLCaroutList = wfFlowQueryService.findTaskVoForOutBySubNodeCode(registNo, FlowNode.DLCar.getName());
            int validityCarFeeMissionNum = 0;
            for(PrpLWfTaskVo vo : dLCaroutList){
                if(!CodeConstants.WorkStatus.CANCEL.equals(vo.getWorkStatus())){
                    validityCarFeeMissionNum +=1;
                }
            }
            vClaimReqVo.setValidityCarFeeMissionNum(String.valueOf(validityCarFeeMissionNum));
            //vClaimReqVo.setLossFeeType(lossFeeType);
            vClaimReqVo.setSumAmount(sumAmount.abs().toString());
            vClaimReqVo.setIsReopenClaim("0");
            Map<String,String> queryMap = new HashMap<String,String>();
            queryMap.put("registNo", registNo);
            queryMap.put("checkStatus", "6");//审核通过
            //查找审核通过的重开赔案 立案号列表
            List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
            if(claimNoList != null && claimNoList.size() > 0){
                for(String claimNo:claimNoList){
                    if(claimNo.equals(compensateVo.getClaimNo())){
                        PrpLClaimVo prpLClaimVo  = claimTaskService.findClaimVoByClaimNo(claimNo);
                        //判断是否有立案处于重开状态
                        if(StringUtils.isBlank(prpLClaimVo.getEndCaserCode()) && prpLClaimVo.getEndCaseTime() == null && StringUtils.isBlank(prpLClaimVo.getCaseNo())){
                            vClaimReqVo.setIsReopenClaim("1");
                            break;
                        }
                    }
                }
            }

            List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            String policyNo = "";

            vClaimReqVo.setCarKindCode(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getCarKindCode());//承保车辆种类A
            List<ItemKind> itemKinds = new ArrayList<ItemKind>();
            if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
                for(PrpLCMainVo vo : prpLCMainVoList){
                    if("12".equals(vo.getRiskCode().substring(0,2))){//商业
                        List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(vo.getPolicyNo());
                        //PrpLCMainVo prpLCMainVo = registQueryService.findPrpCmainByPolicyNo(vo.getPolicyNo());
                        PrpLCMainVo prpLCMainVo = vos.get(0);
                        if(prpLCMainVo.getStartDate() != null){
                            vClaimReqVo.setStartDate(format.format(prpLCMainVo.getStartDate()));
                        }
                        vClaimReqVo.setStartHour(prpLCMainVo.getStartHour()!=null ? prpLCMainVo.getStartHour().toString():"0");
                        vClaimReqVo.setStartMinute(prpLCMainVo.getStartMinute()!=null ? prpLCMainVo.getStartMinute().toString():"0");
                        if(prpLCMainVo.getEndDate() != null){
                            vClaimReqVo.setEndDate(format.format(prpLCMainVo.getEndDate()));
                        }
                        vClaimReqVo.setEndHour(prpLCMainVo.getEndHour()!=null ? prpLCMainVo.getEndHour().toString():"0");
                        vClaimReqVo.setEndMinute(prpLCMainVo.getEndMinute()!=null ? prpLCMainVo.getEndMinute().toString():"0");

                        List<PrpLCItemKindVo> prpCItemKinds = vo.getPrpCItemKinds();
                        for(PrpLCItemKindVo itemKindVo : prpCItemKinds){
                            if(riskCodeMap.containsKey(itemKindVo.getKindCode())){//存在riskCodeMap里才传
                                ItemKind itemKind = new ItemKind();
                                itemKind.setKindCode(itemKindVo.getKindCode());
                                itemKind.setKindName(itemKindVo.getKindName());
                                if(itemKindVo.getItemNo() != null){
                                    itemKind.setItemNo(itemKindVo.getItemNo().toString());
                                }
                                if(itemKindVo.getUnitAmount() != null){
                                    itemKind.setUnitAmount(itemKindVo.getUnitAmount().toString());
                                }
                                if(itemKindVo.getQuantity() != null){
                                    itemKind.setUnit(itemKindVo.getQuantity().toString());
                                }
                                if(itemKindVo.getAmount() != null){
                                    itemKind.setAmount(itemKindVo.getAmount().toString());
                                }
                                itemKinds.add(itemKind);
                            }
                        }
                    }else{
                        policyNo = vo.getPolicyNo();
                    }
                }
            }else if(prpLCMainVoList != null){
                List<PrpLCMainVo> vos = registTmpService.findAreadictByPolicyNo(prpLCMainVoList.get(0).getPolicyNo());
                PrpLCMainVo prpLCMainVo = vos.get(0);
                if(prpLCMainVo.getStartDate() != null){
                    vClaimReqVo.setStartDate(format.format(prpLCMainVo.getStartDate()));
                }
                vClaimReqVo.setStartHour(prpLCMainVo.getStartHour()!=null ? prpLCMainVo.getStartHour().toString():"0");
                vClaimReqVo.setStartMinute(prpLCMainVo.getStartMinute()!=null ? prpLCMainVo.getStartMinute().toString():"0");
                if(prpLCMainVo.getEndDate() != null){
                    vClaimReqVo.setEndDate(format.format(prpLCMainVo.getEndDate()));
                }
                vClaimReqVo.setEndHour(prpLCMainVo.getEndHour()!=null ? prpLCMainVo.getEndHour().toString():"0");
                vClaimReqVo.setEndMinute(prpLCMainVo.getEndMinute()!=null ? prpLCMainVo.getEndMinute().toString():"0");
                if("11".equals(prpLCMainVo.getRiskCode().substring(0,2))){//交强
                    policyNo = prpLCMainVo.getPolicyNo();
                }else{
                    List<PrpLCItemKindVo> prpCItemKinds = prpLCMainVo.getPrpCItemKinds();
                    for(PrpLCItemKindVo itemKindVo : prpCItemKinds){
                        if(riskCodeMap.containsKey(itemKindVo.getKindCode())){//存在riskCodeMap里才传
                            ItemKind itemKind = new ItemKind();
                            itemKind.setKindCode(itemKindVo.getKindCode());
                            itemKind.setKindName(itemKindVo.getKindName());
                            if(itemKindVo.getItemNo() != null){
                                itemKind.setItemNo(itemKindVo.getItemNo().toString());
                            }
                            if(itemKindVo.getUnitAmount() != null){
                                itemKind.setUnitAmount(itemKindVo.getUnitAmount().toString());
                            }
                            if(itemKindVo.getQuantity() != null){
                                itemKind.setUnit(itemKindVo.getQuantity().toString());
                            }
                            if(itemKindVo.getAmount() != null){
                                itemKind.setAmount(itemKindVo.getAmount().toString());
                            }
                            itemKinds.add(itemKind);
                        }
                    }
                }
            }
            if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
            	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
            }
            vClaimReqVo.setItemKindList(itemKinds);
            List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
            String deductCondCode = "";
            claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// 获取该报案号下所有免赔条件
            if(claimDeductVoList != null && claimDeductVoList.size() > 0){
                for(PrpLClaimDeductVo vo : claimDeductVoList){
                    if("1".equals(vo.getIsCheck())){
                        deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                    }
                }
                if(deductCondCode.length()>0){
                    deductCondCode = deductCondCode.substring(1,deductCondCode.length());//去除第一个逗号
                }
            }
            vClaimReqVo.setDeductCondCode(deductCondCode);

            List<PrpLCheckDutyVo> prpLCheckDutyVoList = checkTaskService.findCheckDutyByRegistNo(registNo);
            List<AccidentResponsibility> accidentResponsibilityList = new ArrayList<AccidentResponsibility>();
            for(PrpLCheckDutyVo vo : prpLCheckDutyVoList){
                AccidentResponsibility accidentResponsibility = new AccidentResponsibility();
                accidentResponsibility.setIndemnityDuty(vo.getIndemnityDuty());
                if(vo.getIndemnityDutyRate() != null){
                    accidentResponsibility.setIndemnityDutyRate(vo.getIndemnityDutyRate().toString());
                }
                List<PrpLCheckCarInfoVo> PrpLCheckCarVoInfoList = checkTaskService.findPrpLCheckCarInfoVoListByOther(registNo,vo.getLicenseNo());
                PrpLCheckCarInfoVo CheckCarVoInfo = null;
                if(PrpLCheckCarVoInfoList != null){
                    CheckCarVoInfo = PrpLCheckCarVoInfoList.get(0);
                }
                accidentResponsibility.setAccidentVehicleNo(vo.getLicenseNo());
                accidentResponsibility.setAccidentVehicleType(vo.getSerialNo()== 1 ? "1":"3");
                accidentResponsibility.setCiPolicyNo(policyNo);
                accidentResponsibility.setCiInsuredMechanism(CheckCarVoInfo != null ? CheckCarVoInfo.getCiInsureComCode():"");
                accidentResponsibility.setNoDutyPayFlag("0");
                for(PrpLLossItemVo prpLLossItemVo : prpLLossItemVoList){
                    if(vo.getLicenseNo().equals(prpLLossItemVo.getItemName())){
                        if("4".equals(prpLLossItemVo.getPayFlag()) && "1".equals(prpLLossItemVo.getNoDutyPayFlag()) ){
                            accidentResponsibility.setNoDutyPayFlag("1");
                        }
                    }
                }
                accidentResponsibilityList.add(accidentResponsibility);
            }

            for(PrpLDlossCarMainVo vo : lossCarMainVos){
                if("1".equals(vo.getDeflossCarType())){//标的
                    vClaimReqVo.setIsTotalLoss(vo.getIsTotalLoss());//是否全损
                    vClaimReqVo.setLossFeeType(vo.getLossFeeType());//损失类型
                }
            }
            vClaimReqVo.setAccidentResponsibilityList(accidentResponsibilityList);

            //是否授权,授权1，否0
            PrpLAutoVerifyVo prpLAutoVerifyVo =  new PrpLAutoVerifyVo();
            prpLAutoVerifyVo.setUserCode(userVo.getUserCode());
            prpLAutoVerifyVo.setNode(wfTaskVo.getSubNodeCode());
            Boolean isAutoVerifyUser = deflossHandleService.isAutoVerifyUser(prpLAutoVerifyVo);
            if(isAutoVerifyUser){
                vClaimReqVo.setSysAuthorizationFlag("1");
            }else{
                vClaimReqVo.setSysAuthorizationFlag("0");
            }
        }
        vClaimReqVo.setIsinvolvedincarlaitp("0");
        // 理算提交核赔，以理算环节的减损金额作为判断依据
        if (compensateVo != null) {
            if ((compensateVo.getCisderoAmout() != null && compensateVo.getCisderoAmout().compareTo(BigDecimal.ZERO) == 1)
                || (compensateVo.getPisderoAmout() != null && compensateVo.getPisderoAmout().compareTo(BigDecimal.ZERO) == 1)) {
                vClaimReqVo.setIsinvolvedincarlaitp("1");
            }
        }

        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        // 设置核损环节的施救费总金额
        BigDecimal rescueFeeOfVerifyLoss = new BigDecimal("0");
        if (prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0) {
        	for (PrpLDlossCarMainVo carMainVo : prpLDlossCarMainVoList) {
        		rescueFeeOfVerifyLoss = rescueFeeOfVerifyLoss.add(carMainVo.getSumRescueFee() == null ? BigDecimal.ZERO : carMainVo.getSumRescueFee());
        	}
        }
        if (prpLdlossPropMainVoList != null && prpLdlossPropMainVoList.size() > 0) {
        	for (PrpLdlossPropMainVo propMainVo : prpLdlossPropMainVoList) {
        		rescueFeeOfVerifyLoss = rescueFeeOfVerifyLoss.add(propMainVo.getVerirescueFee() == null ? BigDecimal.ZERO : propMainVo.getVerirescueFee());
        	}
        }
        vClaimReqVo.setRescueFeeOfVerifyLoss(rescueFeeOfVerifyLoss.toString());

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;// 整合交互地址，确定调用的ILOG接口

/*        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(VClaimReqVo.class);
        String requestXML = stream.toXML(vClaimReqVo);//请求报文转换xml
*/
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("理算-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
        	returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("理算-------------核赔返回报文转换xml============" + returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
/*        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo
        */
        //保存核赔审核需要的级别到理算扩展表
        if(StringUtils.isNotBlank(lIlogRuleResVo.getMinUndwrtNode()) && StringUtils.isNotBlank(lIlogRuleResVo.getMaxUndwrtNode())){
            if(Integer.parseInt(lIlogRuleResVo.getMinUndwrtNode()) > Integer.parseInt(lIlogRuleResVo.getMaxUndwrtNode()) ){
                compensateVo.getPrpLCompensateExt().setSubLevel(Integer.parseInt(lIlogRuleResVo.getMaxUndwrtNode()));
            }else{
                compensateVo.getPrpLCompensateExt().setSubLevel(Integer.parseInt(lIlogRuleResVo.getMinUndwrtNode()));
            }
            this.saveOrUpdateCompensateVo(compensateVo,userVo);
        }

        //返回保存待续

        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//计算书号
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType("0");//任务类型 0：车辆; 1：财产
      /*  if("04".equals(callNode)){
            ilogDataProcessingVo.setRuleNode(FlowNode.CompeWf.name());//任务节点
        }else{
            ilogDataProcessingVo.setRuleNode(FlowNode.Compe.name());//任务节点
        }*/
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//任务节点

     /*   ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//损失方
        ilogDataProcessingVo.setLicenseNo(vClaimReqVo.get);//损失方的车牌号
*/      ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//标的
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//损失方
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//损失方的车牌号
            }
        }
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//规则保存
        return lIlogRuleResVo;
    }

    @Override
    public LIlogRuleResVo organizaForPrePay(PrpLWfTaskVo prpLWfTaskVo,String operateType,String callNode,String triggerNode,SysUserVo userVo,String isSubmitHeadOffice) throws Exception{
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLWfTaskVo.getRegistNo());
        LIlogRuleResVo vPriceResVo = new LIlogRuleResVo();
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        vClaimReqVo.setCompensateNo(prpLWfTaskVo.getCompensateNo());
        vClaimReqVo.setRegistNo(prpLWfTaskVo.getRegistNo());
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        if(isSubmitHeadOffice != null){
        	vClaimReqVo.setExistHeadOffice(isSubmitHeadOffice);
        } else{
			vClaimReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
		}
        vClaimReqVo.setOperateType(operateType);//操作类型
        vClaimReqVo.setCallNode(callNode);
        vClaimReqVo.setComCode(prpLWfTaskVo.getComCode());
        vClaimReqVo.setRiskCode(prpLWfTaskVo.getRiskCode());
        if(prpLWfTaskVo.getMoney() != null){
            vClaimReqVo.setAdjustmentAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setAdjustAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setSumAmount(prpLWfTaskVo.getMoney().toString());
        }
        List<AccidentResponsibility> accidentResponsibilityList = new ArrayList<AccidentResponsibility>();
        List<ItemKind> itemKinds = new ArrayList<ItemKind>();
        AccidentResponsibility accidentResponsibility = new AccidentResponsibility();
        accidentResponsibility.setIndemnityDuty("");
        accidentResponsibility.setIndemnityDutyRate("");
        accidentResponsibility.setAccidentVehicleNo("");
        accidentResponsibility.setAccidentVehicleType("");
        accidentResponsibility.setCiPolicyNo("");
        accidentResponsibility.setCiInsuredMechanism("");
        accidentResponsibility.setNoDutyPayFlag("");
        accidentResponsibilityList.add(accidentResponsibility);
        vClaimReqVo.setAccidentResponsibilityList(accidentResponsibilityList);
        ItemKind itemKind = new ItemKind();
        itemKind.setAmount("");
        itemKind.setItemNo("");
        itemKind.setKindCode("");
        itemKind.setKindName("");
        itemKind.setUnit("");
        itemKind.setUnitAmount("");
        itemKinds.add(itemKind);
        vClaimReqVo.setItemKindList(itemKinds);
        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("预付-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("预付-------------核赔返回报文转换xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//计算书号
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType("0");//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//任务节点
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLWfTaskVo.getRegistNo());
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//标的
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//损失方
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//损失方的车牌号
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(prpLWfTaskVo.getTaskId());//触发节点对应的工作流ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
        ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//规则保存
        return vPriceResVo;
    }

    @Override
    public LIlogRuleResVo organizaForPadPay(PrpLPadPayMainVo padPayVo,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo,String isSubmitHeadOffice) throws Exception{
        Double fee = 0.0;
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(padPayVo.getRegistNo());
        List<PrpLPadPayPersonVo> perVoLost = padPayVo.getPrpLPadPayPersons();
        for (PrpLPadPayPersonVo perVo : perVoLost) {
            if (perVo.getCostSum() != null) {
                fee += perVo.getCostSum()==null?0D:perVo.getCostSum().doubleValue();
            }
        }
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        LIlogRuleResVo vPriceResVo = new LIlogRuleResVo();
        String registNo = padPayVo.getRegistNo();
        vClaimReqVo.setCompensateNo(padPayVo.getCompensateNo());
        vClaimReqVo.setRegistNo(registNo);
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        if(isSubmitHeadOffice != null){
            vClaimReqVo.setExistHeadOffice(isSubmitHeadOffice);
        }
        else{
        	vClaimReqVo.setExistHeadOffice(CodeConstants.CommonConst.FALSE);
        }
        vClaimReqVo.setOperateType(operateType);//操作类型
        vClaimReqVo.setCallNode(callNode);
        vClaimReqVo.setComCode(padPayVo.getComCode());
        vClaimReqVo.setRiskCode(Risk.DQZ);
        vClaimReqVo.setAdjustmentAmount(String.valueOf(fee));
        vClaimReqVo.setAdjustAmount(String.valueOf(fee));
        vClaimReqVo.setSumAmount(String.valueOf(fee));

        List<AccidentResponsibility> accidentResponsibilityList = new ArrayList<AccidentResponsibility>();
        List<ItemKind> itemKinds = new ArrayList<ItemKind>();
        AccidentResponsibility accidentResponsibility = new AccidentResponsibility();
        accidentResponsibility.setIndemnityDuty("");
        accidentResponsibility.setIndemnityDutyRate("");
        accidentResponsibility.setAccidentVehicleNo("");
        accidentResponsibility.setAccidentVehicleType("");
        accidentResponsibility.setCiPolicyNo("");
        accidentResponsibility.setCiInsuredMechanism("");
        accidentResponsibility.setNoDutyPayFlag("");
        accidentResponsibilityList.add(accidentResponsibility);
        vClaimReqVo.setAccidentResponsibilityList(accidentResponsibilityList);
        ItemKind itemKind = new ItemKind();
        itemKind.setAmount("");
        itemKind.setItemNo("");
        itemKind.setKindCode("");
        itemKind.setKindName("");
        itemKind.setUnit("");
        itemKind.setUnitAmount("");
        itemKinds.add(itemKind);
        vClaimReqVo.setItemKindList(itemKinds);

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("垫付-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("垫付-------------核赔返回报文转换xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//计算书号
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType("0");//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//任务节点
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//标的
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//损失方
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//损失方的车牌号
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
       ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//规则保存
        return vPriceResVo;
    }

    @Override
    public LIlogRuleResVo organizaForClaimCancelJuPei(PrpLClaimVo prpLClaimVo,BigDecimal adjustmentAmount,String operateType,String callNode,BigDecimal taskId,String triggerNode,SysUserVo userVo) throws Exception{
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLClaimVo.getRegistNo());
        LIlogRuleResVo vPriceResVo = new LIlogRuleResVo();
        vClaimReqVo.setCompensateNo("");
        vClaimReqVo.setRegistNo(prpLClaimVo.getRegistNo());
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        vClaimReqVo.setOperateType(operateType);//操作类型
        vClaimReqVo.setCallNode(callNode);
        vClaimReqVo.setComCode(prpLClaimVo.getComCode());
        vClaimReqVo.setRiskCode(prpLClaimVo.getRiskCode());
        if(adjustmentAmount != null){
            vClaimReqVo.setAdjustmentAmount(adjustmentAmount.toString());
            vClaimReqVo.setAdjustAmount(adjustmentAmount.toString());
            vClaimReqVo.setSumAmount(adjustmentAmount.toString());
        }

        List<AccidentResponsibility> accidentResponsibilityList = new ArrayList<AccidentResponsibility>();
        List<ItemKind> itemKinds = new ArrayList<ItemKind>();
        AccidentResponsibility accidentResponsibility = new AccidentResponsibility();
        accidentResponsibility.setIndemnityDuty("");
        accidentResponsibility.setIndemnityDutyRate("");
        accidentResponsibility.setAccidentVehicleNo("");
        accidentResponsibility.setAccidentVehicleType("");
        accidentResponsibility.setCiPolicyNo("");
        accidentResponsibility.setCiInsuredMechanism("");
        accidentResponsibility.setNoDutyPayFlag("");
        accidentResponsibilityList.add(accidentResponsibility);
        vClaimReqVo.setAccidentResponsibilityList(accidentResponsibilityList);
        ItemKind itemKind = new ItemKind();
        itemKind.setAmount("");
        itemKind.setItemNo("");
        itemKind.setKindCode("");
        itemKind.setKindName("");
        itemKind.setUnit("");
        itemKind.setUnitAmount("");
        itemKinds.add(itemKind);
        vClaimReqVo.setItemKindList(itemKinds);
        // 用以保证ILOG请求报文的完整性，否则ILOG解析请求报文会报错"规则解析异常"
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("registNo", prpLClaimVo.getRegistNo());
        if(taskId == null){
            params.put("taskId",BigDecimal.ZERO.doubleValue());
        } else{
            params.put("taskId", Double.valueOf(taskId+""));
        }
        params.put("riskCode", prpLClaimVo.getRiskCode());
        String isSubmitHeadOffice = managerService.isSubmitHeadOffice(params);
        vClaimReqVo.setExistHeadOffice(StringUtils.isBlank(isSubmitHeadOffice) ? "0" : isSubmitHeadOffice);

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("拒赔-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr,200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("拒赔-------------核赔返回报文转换xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//业务号 报案号
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//计算书号
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//业务归属机构
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//险种
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//操作类型  1：自动  2：人工权限
        ilogDataProcessingVo.setRuleType("0");//任务类型 0：车辆; 1：财产
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//任务节点
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLClaimVo.getRegistNo());
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//标的
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//损失方 0  地面/路人损失  1  标的车  2  三者车);//损失方
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//损失方的车牌号
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//触发节点  提交到任务节点的前节点
        ilogDataProcessingVo.setTaskId(taskId);//触发节点对应的工作流ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//操作人员
        ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//规则保存
        return vPriceResVo;
    }

    /**
     * 保存或更新理算主表
     *
     * @param compensateVo
     * @modified: ☆XMSH(2016年4月5日 下午5:38:33): <br>
     */
    public String saveOrUpdateCompensateVo(PrpLCompensateVo compensateVo,SysUserVo userVo){
        PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,compensateVo.getCompensateNo());

        if(prpLCompensate==null){//新增
            prpLCompensate = new PrpLCompensate();
            prpLCompensate = Beans.copyDepth().from(compensateVo).to(PrpLCompensate.class);
            prpLCompensate.setCreateTime(new Date());
            prpLCompensate.setUpdateTime(new Date());
            prpLCompensate.setCreateUser(userVo.getUserCode());
            //设置主子表关系
            PrpLCompensateExt ext = prpLCompensate.getPrpLCompensateExt();
            ext.setCreateTime(new Date());
            ext.setUpdateTime(new Date());
            ext.setPrpLCompensate(prpLCompensate);
        }else{//更新
            Beans.copy().from(compensateVo).excludeNull().to(prpLCompensate);
            prpLCompensate.setUpdateTime(new Date());
            //设置主子表关系
            PrpLCompensateExt ext = prpLCompensate.getPrpLCompensateExt();
            Beans.copy().from(compensateVo.getPrpLCompensateExt()).excludeNull().to(ext);
            ext.setPayBackState(compensateVo.getPrpLCompensateExt().getPayBackState());
            ext.setIsCompDeduct(compensateVo.getPrpLCompensateExt().getIsCompDeduct());
            if(ext!=null){
                ext.setUpdateTime(new Date());
                ext.setPrpLCompensate(prpLCompensate);
            }

        }
        databaseDao.save(PrpLCompensate.class,prpLCompensate);
        return prpLCompensate.getCompensateNo();
    }

	@Override
	public LIlogRuleResVo organizaForOldCompensate(String compensateId,String operateType,String callNode) throws Exception {
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        LIlogRuleResVo lIlogRuleResVo = new LIlogRuleResVo();
        PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateId);
        String registNo = compensateVo.getRegistNo();
        vClaimReqVo.setCompensateNo(compensateVo.getCompensateNo());
        vClaimReqVo.setRegistNo(registNo);
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        vClaimReqVo.setCallNode(callNode);
        vClaimReqVo.setComCode(compensateVo.getComCode());
        vClaimReqVo.setRiskCode(compensateVo.getRiskCode());

        if(compensateVo.getSumAmt() != null){
            vClaimReqVo.setAdjustmentAmount(compensateVo.getSumAmt().abs().toString());//当次理算金额
            vClaimReqVo.setAdjustAmount(compensateVo.getSumAmt().abs().toString());//理算提交总赔款
        }
        BigDecimal sumAmount = new BigDecimal(0);//理算提交总金额
        if("04".equals(callNode)){
            if(compensateVo.getSumAmt() != null){
                sumAmount = sumAmount.add(compensateVo.getSumAmt().abs());
            }
            if(compensateVo.getSumFee() != null){
                sumAmount = sumAmount.add(compensateVo.getSumFee().abs());
            }
        }
        vClaimReqVo.setOperateType(operateType);//操作类型
        if("00".equals(callNode)){
            List<PrpLCompensateVo> prpLCompensateVoList = compensateService.findCompensatevosByclaimNo(compensateVo.getClaimNo());
            for(PrpLCompensateVo lCompensateVo : prpLCompensateVoList){
                if(!"7".equals(lCompensateVo.getUnderwriteFlag())){
                    if(lCompensateVo.getSumAmt() != null){
                        sumAmount = sumAmount.add(lCompensateVo.getSumAmt().abs());
                    }
                    if(lCompensateVo.getSumFee() != null){
                        sumAmount = sumAmount.add(lCompensateVo.getSumFee().abs());
                    }
                }
                if(lCompensateVo.getSumFee() != null){
                    sumAmount = sumAmount.add(lCompensateVo.getSumFee().abs());
                }
            }
        }
        if("00".equals(callNode)){
            vClaimReqVo.setOperateType(operateType);//操作类型
            BigDecimal rescueFeeAmount = new BigDecimal(0);//施救费金额
            List<PrpLLossItemVo> prpLLossItemVoList = compensateVo.getPrpLLossItems();//车
            for(PrpLLossItemVo vo : prpLLossItemVoList){
                if(vo.getRescueFee() != null){
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }
            }
            List<PrpLLossPropVo>  prpLLossPropVoList = compensateVo.getPrpLLossProps();//财
            for(PrpLLossPropVo vo : prpLLossPropVoList){
                if(vo.getRescueFee() != null){
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }
            }
            vClaimReqVo.setRescueFeeAmount(rescueFeeAmount.toString());

            if(compensateVo.getSumPaidFee() != null){
                vClaimReqVo.setSumPaidFee(compensateVo.getSumPaidFee().toString());
            }
        }
        vClaimReqVo.setSumAmount(sumAmount.abs().toString());
        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;//整合交互地址

        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("理算-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
        	returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("理算-------------核赔返回报文转换xml============" + returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        //保存核赔审核需要的级别到理算扩展表
        if(lIlogRuleResVo != null && StringUtils.isNotBlank(lIlogRuleResVo.getMinUndwrtNode())){
            compensateVo.getPrpLCompensateExt().setSubLevel(Integer.parseInt(lIlogRuleResVo.getMinUndwrtNode()));
            SysUserVo userVo = new SysUserVo();
            userVo.setUserCode("0000000000");
            this.saveOrUpdateCompensateVo(compensateVo,userVo);
        }
        return lIlogRuleResVo;
    }

	@Override
	public LIlogRuleResVo organizaForOldPrePay(PrpLWfTaskVo prpLWfTaskVo,String operateType) throws Exception {
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLWfTaskVo.getRegistNo());
        LIlogRuleResVo vPriceResVo = new LIlogRuleResVo();
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//是否联共保案件
        }
        vClaimReqVo.setCompensateNo(prpLWfTaskVo.getCompensateNo());
        vClaimReqVo.setRegistNo(prpLWfTaskVo.getRegistNo());
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        vClaimReqVo.setOperateType(operateType);//操作类型
        vClaimReqVo.setComCode(prpLWfTaskVo.getComCode());
        vClaimReqVo.setRiskCode(prpLWfTaskVo.getRiskCode());
        if(prpLWfTaskVo.getMoney() != null){
            vClaimReqVo.setAdjustmentAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setAdjustAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setSumAmount(prpLWfTaskVo.getMoney().toString());
        }

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//获取ILOG规则服务地址
        urlStr = urlStr + CheckRuleForCarClaimServlet;//整合交互地址
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//请求报文转换xml
        logger.info("预付-------------核赔请求报文转换xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//推送ILOG
        logger.info("预付-------------核赔返回报文转换xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//返回报文转换vo

        return vPriceResVo;
    }

	/**
	 * 	理算提交核赔请求ILOG交互
	 * @param requestXML 请求报文
	 * @param urlStr 请求核赔接口地址
	 * @param seconds 超时时间（秒）
	 * @return
	 * @throws Exception
	 */
	private String requestClaimIlog(String requestXML, String urlStr, int seconds) throws Exception {
		long t1 = System.currentTimeMillis();
		String responseXml = "";
		StringBuffer buffer = new StringBuffer();
		try {

			URL url = new URL(urlStr);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("POST");
			httpUrlConn.setRequestProperty("Content-Type", "text/xml;charset=GBK");
			httpUrlConn.setConnectTimeout(seconds * 1000);

			httpUrlConn.connect();

			String outputStr = requestXML;

			OutputStream outputStream = httpUrlConn.getOutputStream();
			// 当有数据需要提交时
			if (null != outputStr) {
				// 注意编码格式，防止中文乱码 outputStream.write
				outputStream.write(outputStr.getBytes("GBK"));
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {

				buffer.append(str);
			}
			if (buffer.length() < 1) {
				throw new Exception("车险ILOG核赔接口返回数据失败");
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			System.out.println(buffer);
			responseXml = buffer.toString();

		} catch (ConnectException ce) {
			throw new Exception("与车险ILOG核赔接口连接失败，请稍候再试", ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读车险ILOG核赔接口返回数据失败", e);
		} finally {
			logger.warn("接口({})调用耗时{}ms", urlStr, System.currentTimeMillis() - t1);
		}
		return responseXml;
	}

}
