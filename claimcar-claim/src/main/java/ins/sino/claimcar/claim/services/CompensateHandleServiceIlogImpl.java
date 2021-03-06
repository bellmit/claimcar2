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
 * ????????????????????????????????????
 * <pre></pre>
 * @author ???zhujunde
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
        // ?????????????????????
        vClaimReqVo.setIsTotalLoss("");
        // ???????????? 1-????????????  2-????????????
        vClaimReqVo.setOperateType(operateType);
        // ????????????
        vClaimReqVo.setDamageDate(format.format(prpLRegistVo.getDamageTime()));

        if(compensateVo.getSumAmt() != null){
            vClaimReqVo.setAdjustmentAmount(compensateVo.getSumAmt().abs().toString());//??????????????????
            vClaimReqVo.setAdjustAmount(compensateVo.getSumAmt().abs().toString());//?????????????????????
        }
        BigDecimal sumAmount = new BigDecimal(0);//?????????????????????
        if("04".equals(callNode)){
            if(compensateVo.getSumAmt() != null){
                sumAmount = sumAmount.add(compensateVo.getSumAmt().abs());
            }
            if(compensateVo.getSumFee() != null){
                sumAmount = sumAmount.add(compensateVo.getSumFee().abs());
            }
        }
        // ?????????????????????????????????????????????????????????
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        // callNode: 00-???????????? 01-?????? 02-?????? 03-???????????? 04-???????????? 05-?????????????????? 99-??????
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
            //vClaimReqVo.setCurpowerLevel("");//??????????????????
            // ???????????? ?????? ??????
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

            // ????????????????????????
            List<PrpLDlossPersTraceMainVo> prpLDlossPersTraceMainVoList = persTraceService.findPersTraceMainVo(registNo);
            if(prpLDlossPersTraceMainVoList != null && prpLDlossPersTraceMainVoList.size() > 0){
                vClaimReqVo.setCasualtiesCaseFlag("1");
            }else{
                vClaimReqVo.setCasualtiesCaseFlag("0");
            }

            // ??????????????? 001-??????????????? 002-???????????????  003-???????????????
            List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
            vClaimReqVo.setRepairFactoryType("0");
            if(prpLDlossCarMainVoList != null && prpLDlossCarMainVoList.size() > 0){
                for(PrpLDlossCarMainVo vo : prpLDlossCarMainVoList){
                    if("001".equals(vo.getRepairFactoryType())){//?????????
                        vClaimReqVo.setRepairFactoryType(vo.getRepairFactoryType());
                        // ??????????????????????????????????????????????????????????????????1???????????????????????????
                        break;
                    }
                }
            }

            // ?????????????????????????????? 0-???  1-???
            PrpLCertifyMainVo prpLCertifyMainVo = certifyService.findPrpLCertifyMainVo(registNo);
            vClaimReqVo.setIsJQFraud(prpLCertifyMainVo.getIsJQFraud());
            vClaimReqVo.setIsSYFraud(prpLCertifyMainVo.getIsSYFraud());

            // ???????????? 0-??? 1-???
            vClaimReqVo.setLawsuitFlag(compensateVo.getLawsuitFlag());
            if("3".equals(compensateVo.getCaseType())){
                vClaimReqVo.setSubrogationFlag("1");
            }else{
                vClaimReqVo.setSubrogationFlag("0");
            }
            // ????????????
            vClaimReqVo.setAllowFlag(compensateVo.getAllowFlag());
            // ????????????????????????
            vClaimReqVo.setRecoveryFlag(compensateVo.getRecoveryFlag());
            // ??????????????????
            vClaimReqVo.setIsCheat(prpLCertifyMainVo.getIsFraud());
            // ????????????????????????
            vClaimReqVo.setSurveyFlag(prpLCertifyMainVo.getSurveyFlag());

            // ??????????????????
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
                	// ????????????????????????
                    vClaimReqVo.setIsAssessmentSurvey("1");
                }
                if("1".equals(vo.getSubCheckFlag()) || "2".equals(vo.getSubCheckFlag())){
                    // ?????????????????????
                	vClaimReqVo.setIsSurveyCase("1");
                }
            }

            // ???????????????(?????????)
            BigDecimal propertySumRealFeeAmount = BigDecimal.ZERO;
            // ???????????????(?????????)
            BigDecimal personSumRealFeeAmount = BigDecimal.ZERO;

            // ???????????????????????????????????????????????????????????????
            BigDecimal rescueFeeAmount = new BigDecimal(0);
            Map<String,String> riskCodeMap = new HashMap<String,String>();//ItemKindList?????????????????????????????????????????????????????????
            List<PrpLLossItemVo> prpLLossItemVoList = compensateVo.getPrpLLossItems();//???
            for(PrpLLossItemVo vo : prpLLossItemVoList){
                if(vo.getRescueFee() != null){
                	// ????????????????????????????????????????????????
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }

                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//??????????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//??????????????????????????????????????????????????????0????????????????????????
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }

                propertySumRealFeeAmount.add(lossItem_sumRealPay);
            }
            List<PrpLLossPropVo>  prpLLossPropVoList = compensateVo.getPrpLLossProps();//???
            for(PrpLLossPropVo vo : prpLLossPropVoList){
                if(vo.getRescueFee() != null){
                	// ??????????????????????????????????????????
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }

                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//??????????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//??????????????????????????????????????????????????????0????????????????????????
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }
                propertySumRealFeeAmount.add(lossItem_sumRealPay);
            }

            List<PrpLLossPersonVo> prpLLossPersonVoList = compensateVo.getPrpLLossPersons();//???
            for(PrpLLossPersonVo vo : prpLLossPersonVoList){
                BigDecimal lossItem_sumRealPay = new BigDecimal(0);
                if(vo.getOffPreAmt() != null){//??????????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getOffPreAmt());
                }
                if(vo.getSumRealPay() != null){//????????????
                    lossItem_sumRealPay = lossItem_sumRealPay.add(vo.getSumRealPay());
                    if(lossItem_sumRealPay.compareTo(BigDecimal.ZERO)==1){//??????????????????????????????????????????????????????0????????????????????????
                        riskCodeMap.put(vo.getKindCode(),vo.getKindCode());
                    }
                }
                personSumRealFeeAmount.add(lossItem_sumRealPay);
            }
            vClaimReqVo.setRescueFeeAmount(rescueFeeAmount.toString());
            vClaimReqVo.setPropertySumAmount(propertySumRealFeeAmount.toString());
            vClaimReqVo.setPersonSumAmount(personSumRealFeeAmount.toString());


            // ??????????????????
            PrpLCompensateVo compensateYVo = compensateService.findCompByClaimNo(compensateVo.getClaimNo(),"Y");
            if(compensateYVo != null){
                vClaimReqVo.setIsPrepaidType("1");
            }else{
                vClaimReqVo.setIsPrepaidType("0");
            }

            // ???????????????????????? ??????????????????????????????????????????????????????????????????????????????????????????1
            vClaimReqVo.setMajorcaseFlag("0");
            //?????????????????????
            PrpLCheckVo checkVo = checkTaskService.findCheckVoByRegistNo(registNo);
            if("1".equals(checkVo.getMajorCaseFlag())){
                vClaimReqVo.setMajorcaseFlag("1");
            }
            //?????????????????????
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
            //?????????????????????
            if (!"1".equals(vClaimReqVo.getMajorcaseFlag())) {
            	List<PrpLDlossCarMainVo> prpLDlossCarMainList = lossCarService.findLossCarMainByRegistNo(registNo); //?????????????????????
            	for(PrpLDlossCarMainVo vo : prpLDlossCarMainList){
            		if("1".equals(vo.getIsMajorCase())){
            			vClaimReqVo.setMajorcaseFlag("1");
            		}
            	}
            }

            // ??????????????????????????????????????????????????????
            List<PrpLPayCustomVo> prpLPayCustomVo = payCustomService.findPayCustomVoByRegistNo(registNo);
            List<PaymentInfo> paymentInfoList = new ArrayList<PaymentInfo>();
            if(prpLPayCustomVo != null && prpLPayCustomVo.size() > 0){
                vClaimReqVo.setPayeeInfoNum(prpLPayCustomVo.size());
                // ???????????????????????????????????????????????????
                for (PrpLPayCustomVo payCustom : prpLPayCustomVo) {
                	PaymentInfo paymentInfo = new PaymentInfo();
                	paymentInfo.setPayobjectkind(payCustom.getPayObjectKind());
                	paymentInfoList.add(paymentInfo);
                }
            }else{
                vClaimReqVo.setPayeeInfoNum(0);
            }
            vClaimReqVo.setPaymentInfoList(paymentInfoList);

            // ????????????????????????
            // ??????????????????????????????
            List<PrpLDlossCarMainVo> prpLDlossCarMainVos = deflossHandleService.findLossCarMainByRegistNo(registNo);
            // ??????????????????
            List<RepairInfo> repairInfoList = new ArrayList<RepairInfo>();
            if (prpLDlossCarMainVos != null && prpLDlossCarMainVos.size() > 0) {
            	for (PrpLDlossCarMainVo vo : prpLDlossCarMainVos) {
            		List<PrpLDlossCarRepairVo> repairVoList = vo.getPrpLDlossCarRepairs();
            		if (repairVoList != null && repairVoList.size() > 0) {
            			// ??????????????????????????????????????????
            			for (PrpLDlossCarRepairVo repairVo : repairVoList) {
            				RepairInfo repairInfo = new RepairInfo();
            				repairInfo.setRepairFlag(repairVo.getRepairFlag());
            				repairInfoList.add(repairInfo);
            			}
            		}
            	}
            }
            vClaimReqVo.setRepairInfoList(repairInfoList);


            // ??????????????????
            Map<String, String> registRiskInfoMap = new HashMap<String, String>();
            if (!StringUtils.isEmpty(registNo)) {
                registRiskInfoMap = registService.findRegistRiskInfoByRegistNo(registNo);
            }

            // ???????????????????????????
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

            // ??????????????????????????????????????????
            if(compensateVo.getSumPaidFee() != null){
                vClaimReqVo.setSumPaidFee(compensateVo.getSumPaidFee().toString());
            }

            // ??????????????????????????????
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
            queryMap.put("checkStatus", "6");//????????????
            //????????????????????????????????? ???????????????
            List<String> claimNoList = endCasePubService.findPrpLReCaseVoList(queryMap);
            if(claimNoList != null && claimNoList.size() > 0){
                for(String claimNo:claimNoList){
                    if(claimNo.equals(compensateVo.getClaimNo())){
                        PrpLClaimVo prpLClaimVo  = claimTaskService.findClaimVoByClaimNo(claimNo);
                        //???????????????????????????????????????
                        if(StringUtils.isBlank(prpLClaimVo.getEndCaserCode()) && prpLClaimVo.getEndCaseTime() == null && StringUtils.isBlank(prpLClaimVo.getCaseNo())){
                            vClaimReqVo.setIsReopenClaim("1");
                            break;
                        }
                    }
                }
            }

            List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(registNo);
            String policyNo = "";

            vClaimReqVo.setCarKindCode(prpLCMainVoList.get(0).getPrpCItemCars().get(0).getCarKindCode());//??????????????????A
            List<ItemKind> itemKinds = new ArrayList<ItemKind>();
            if(prpLCMainVoList != null && prpLCMainVoList.size() > 1 ){
                for(PrpLCMainVo vo : prpLCMainVoList){
                    if("12".equals(vo.getRiskCode().substring(0,2))){//??????
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
                            if(riskCodeMap.containsKey(itemKindVo.getKindCode())){//??????riskCodeMap?????????
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
                if("11".equals(prpLCMainVo.getRiskCode().substring(0,2))){//??????
                    policyNo = prpLCMainVo.getPolicyNo();
                }else{
                    List<PrpLCItemKindVo> prpCItemKinds = prpLCMainVo.getPrpCItemKinds();
                    for(PrpLCItemKindVo itemKindVo : prpCItemKinds){
                        if(riskCodeMap.containsKey(itemKindVo.getKindCode())){//??????riskCodeMap?????????
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
            	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
            }
            vClaimReqVo.setItemKindList(itemKinds);
            List<PrpLClaimDeductVo> claimDeductVoList = new ArrayList<PrpLClaimDeductVo>();
            String deductCondCode = "";
            claimDeductVoList = registQueryService.findClaimDeductVoByRegistNo(registNo);// ???????????????????????????????????????
            if(claimDeductVoList != null && claimDeductVoList.size() > 0){
                for(PrpLClaimDeductVo vo : claimDeductVoList){
                    if("1".equals(vo.getIsCheck())){
                        deductCondCode = deductCondCode + "," +vo.getDeductCondCode();
                    }
                }
                if(deductCondCode.length()>0){
                    deductCondCode = deductCondCode.substring(1,deductCondCode.length());//?????????????????????
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
                if("1".equals(vo.getDeflossCarType())){//??????
                    vClaimReqVo.setIsTotalLoss(vo.getIsTotalLoss());//????????????
                    vClaimReqVo.setLossFeeType(vo.getLossFeeType());//????????????
                }
            }
            vClaimReqVo.setAccidentResponsibilityList(accidentResponsibilityList);

            //????????????,??????1??????0
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
        // ?????????????????????????????????????????????????????????????????????
        if (compensateVo != null) {
            if ((compensateVo.getCisderoAmout() != null && compensateVo.getCisderoAmout().compareTo(BigDecimal.ZERO) == 1)
                || (compensateVo.getPisderoAmout() != null && compensateVo.getPisderoAmout().compareTo(BigDecimal.ZERO) == 1)) {
                vClaimReqVo.setIsinvolvedincarlaitp("1");
            }
        }

        List<PrpLDlossCarMainVo> prpLDlossCarMainVoList = lossCarService.findLossCarMainByRegistNo(registNo);
        List<PrpLdlossPropMainVo> prpLdlossPropMainVoList = propTaskService.findPropMainListByRegistNo(registNo);
        // ???????????????????????????????????????
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

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;// ????????????????????????????????????ILOG??????

/*        XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// ?????? class??????
        stream.processAnnotations(VClaimReqVo.class);
        String requestXML = stream.toXML(vClaimReqVo);//??????????????????xml
*/
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
        	returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
/*        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo
        */
        //???????????????????????????????????????????????????
        if(StringUtils.isNotBlank(lIlogRuleResVo.getMinUndwrtNode()) && StringUtils.isNotBlank(lIlogRuleResVo.getMaxUndwrtNode())){
            if(Integer.parseInt(lIlogRuleResVo.getMinUndwrtNode()) > Integer.parseInt(lIlogRuleResVo.getMaxUndwrtNode()) ){
                compensateVo.getPrpLCompensateExt().setSubLevel(Integer.parseInt(lIlogRuleResVo.getMaxUndwrtNode()));
            }else{
                compensateVo.getPrpLCompensateExt().setSubLevel(Integer.parseInt(lIlogRuleResVo.getMinUndwrtNode()));
            }
            this.saveOrUpdateCompensateVo(compensateVo,userVo);
        }

        //??????????????????

        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//????????????
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType("0");//???????????? 0?????????; 1?????????
      /*  if("04".equals(callNode)){
            ilogDataProcessingVo.setRuleNode(FlowNode.CompeWf.name());//????????????
        }else{
            ilogDataProcessingVo.setRuleNode(FlowNode.Compe.name());//????????????
        }*/
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//????????????

     /*   ilogDataProcessingVo.setLossParty(vPriceReqVo.getLossPartyName());//?????????
        ilogDataProcessingVo.setLicenseNo(vClaimReqVo.get);//?????????????????????
*/      ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//??????
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//?????????
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//?????????????????????
            }
        }
        ruleReturnDataSaveService.dealILogResReturnData(lIlogRuleResVo,ilogDataProcessingVo);//????????????
        return lIlogRuleResVo;
    }

    @Override
    public LIlogRuleResVo organizaForPrePay(PrpLWfTaskVo prpLWfTaskVo,String operateType,String callNode,String triggerNode,SysUserVo userVo,String isSubmitHeadOffice) throws Exception{
        VClaimReqVo vClaimReqVo = new VClaimReqVo();
        List<PrpLCMainVo> prpLCMainVoList = prpLCMainService.findPrpLCMainsByRegistNo(prpLWfTaskVo.getRegistNo());
        LIlogRuleResVo vPriceResVo = new LIlogRuleResVo();
        if(prpLCMainVoList!=null && prpLCMainVoList.size()>0){
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
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
        vClaimReqVo.setOperateType(operateType);//????????????
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
        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//????????????
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType("0");//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//????????????
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLWfTaskVo.getRegistNo());
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//??????
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//?????????
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//?????????????????????
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(prpLWfTaskVo.getTaskId());//??????????????????????????????ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
        ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//????????????
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
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
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
        vClaimReqVo.setOperateType(operateType);//????????????
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

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//????????????
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType("0");//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//????????????
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(registNo);
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//??????
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//?????????
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//?????????????????????
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
       ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//????????????
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
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
        }
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        vClaimReqVo.setOperateType(operateType);//????????????
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
        // ????????????ILOG?????????????????????????????????ILOG???????????????????????????"??????????????????"
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

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr,200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        IlogDataProcessingVo ilogDataProcessingVo =new IlogDataProcessingVo();
        ilogDataProcessingVo.setBusinessNo(vClaimReqVo.getRegistNo());//????????? ?????????
        ilogDataProcessingVo.setCompensateNo(vClaimReqVo.getCompensateNo());//????????????
        ilogDataProcessingVo.setComCode(vClaimReqVo.getComCode());//??????????????????
        ilogDataProcessingVo.setRiskCode(vClaimReqVo.getRiskCode());//??????
        ilogDataProcessingVo.setOperateType(vClaimReqVo.getOperateType());//????????????  1?????????  2???????????????
        ilogDataProcessingVo.setRuleType("0");//???????????? 0?????????; 1?????????
        ilogDataProcessingVo.setRuleNode(FlowNode.VClaim.name());//????????????
        List<PrpLDlossCarMainVo> lossCarMainVos = lossCarService.findLossCarMainByRegistNo(prpLClaimVo.getRegistNo());
        for(PrpLDlossCarMainVo vo : lossCarMainVos){
            if("1".equals(vo.getDeflossCarType())){//??????
                ilogDataProcessingVo.setLossParty(vo.getSerialNo()>1?"2":vo.getSerialNo().toString());//????????? 0  ??????/????????????  1  ?????????  2  ?????????);//?????????
                ilogDataProcessingVo.setLicenseNo(vo.getLicenseNo());//?????????????????????
            }
        }
        ilogDataProcessingVo.setTriggerNode(triggerNode);//????????????  ?????????????????????????????????
        ilogDataProcessingVo.setTaskId(taskId);//??????????????????????????????ID
        ilogDataProcessingVo.setOperatorCode(userVo.getUserCode());//????????????
        ruleReturnDataSaveService.dealILogResReturnData(vPriceResVo,ilogDataProcessingVo);//????????????
        return vPriceResVo;
    }

    /**
     * ???????????????????????????
     *
     * @param compensateVo
     * @modified: ???XMSH(2016???4???5??? ??????5:38:33): <br>
     */
    public String saveOrUpdateCompensateVo(PrpLCompensateVo compensateVo,SysUserVo userVo){
        PrpLCompensate prpLCompensate = databaseDao.findByPK(PrpLCompensate.class,compensateVo.getCompensateNo());

        if(prpLCompensate==null){//??????
            prpLCompensate = new PrpLCompensate();
            prpLCompensate = Beans.copyDepth().from(compensateVo).to(PrpLCompensate.class);
            prpLCompensate.setCreateTime(new Date());
            prpLCompensate.setUpdateTime(new Date());
            prpLCompensate.setCreateUser(userVo.getUserCode());
            //?????????????????????
            PrpLCompensateExt ext = prpLCompensate.getPrpLCompensateExt();
            ext.setCreateTime(new Date());
            ext.setUpdateTime(new Date());
            ext.setPrpLCompensate(prpLCompensate);
        }else{//??????
            Beans.copy().from(compensateVo).excludeNull().to(prpLCompensate);
            prpLCompensate.setUpdateTime(new Date());
            //?????????????????????
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
            vClaimReqVo.setAdjustmentAmount(compensateVo.getSumAmt().abs().toString());//??????????????????
            vClaimReqVo.setAdjustAmount(compensateVo.getSumAmt().abs().toString());//?????????????????????
        }
        BigDecimal sumAmount = new BigDecimal(0);//?????????????????????
        if("04".equals(callNode)){
            if(compensateVo.getSumAmt() != null){
                sumAmount = sumAmount.add(compensateVo.getSumAmt().abs());
            }
            if(compensateVo.getSumFee() != null){
                sumAmount = sumAmount.add(compensateVo.getSumFee().abs());
            }
        }
        vClaimReqVo.setOperateType(operateType);//????????????
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
            vClaimReqVo.setOperateType(operateType);//????????????
            BigDecimal rescueFeeAmount = new BigDecimal(0);//???????????????
            List<PrpLLossItemVo> prpLLossItemVoList = compensateVo.getPrpLLossItems();//???
            for(PrpLLossItemVo vo : prpLLossItemVoList){
                if(vo.getRescueFee() != null){
                    rescueFeeAmount = rescueFeeAmount.add(vo.getRescueFee());
                }
            }
            List<PrpLLossPropVo>  prpLLossPropVoList = compensateVo.getPrpLLossProps();//???
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
        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;//??????????????????

        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
        	returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        lIlogRuleResVo= XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        //???????????????????????????????????????????????????
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
        	vClaimReqVo.setCoinsFlag("0".equals(prpLCMainVoList.get(0).getCoinsFlag()) ? "0":"1");//?????????????????????
        }
        vClaimReqVo.setCompensateNo(prpLWfTaskVo.getCompensateNo());
        vClaimReqVo.setRegistNo(prpLWfTaskVo.getRegistNo());
        if("1".equals(operateType)){
            vClaimReqVo.setIsAuto("01");
        }else if("2".equals(operateType)){
            vClaimReqVo.setIsAuto("02");
        }
        vClaimReqVo.setOperateType(operateType);//????????????
        vClaimReqVo.setComCode(prpLWfTaskVo.getComCode());
        vClaimReqVo.setRiskCode(prpLWfTaskVo.getRiskCode());
        if(prpLWfTaskVo.getMoney() != null){
            vClaimReqVo.setAdjustmentAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setAdjustAmount(prpLWfTaskVo.getMoney().toString());
            vClaimReqVo.setSumAmount(prpLWfTaskVo.getMoney().toString());
        }

        String urlStr = SpringProperties.getProperty("ILOG_SVR_URL");//??????ILOG??????????????????
        urlStr = urlStr + CheckRuleForCarClaimServlet;//??????????????????
        String requestXML = XstreamFactory.objToXml(vClaimReqVo);//??????????????????xml
        logger.info("??????-------------????????????????????????xml============" + requestXML);
        String returnXml = "";
        try{
            returnXml = requestClaimIlog(requestXML,urlStr, 200);
        }
        catch(Exception e){
            e.printStackTrace();
        }//??????ILOG
        logger.info("??????-------------????????????????????????xml============" + returnXml);
        vPriceResVo = XstreamFactory.xmlToObj(returnXml,LIlogRuleResVo.class);//??????????????????vo

        return vPriceResVo;
    }

	/**
	 * 	????????????????????????ILOG??????
	 * @param requestXML ????????????
	 * @param urlStr ????????????????????????
	 * @param seconds ?????????????????????
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
			throw new Exception("?????????ILOG??????????????????????????????????????????", ce);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("?????????ILOG??????????????????????????????", e);
		} finally {
			logger.warn("??????({})????????????{}ms", urlStr, System.currentTimeMillis() - t1);
		}
		return responseXml;
	}

}
