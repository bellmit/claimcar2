package ins.sino.claimcar.pinganunion.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.service.facade.BillNoService;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.utils.Base64EncodedUtil;
import ins.platform.utils.DataUtils;
import ins.platform.vo.PiccCodeDictVo;
import ins.platform.vo.SysAreaDictVo;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.carplatform.util.StringUtil;
import ins.sino.claimcar.claim.po.PrpLPayCustom;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.service.InterfaceAsyncService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.claimcarYJ.Util.vo.HttpClientJsonUtil;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.PayCustomService;
import ins.sino.claimcar.flow.service.WfTaskHandleService;
import ins.sino.claimcar.flow.vo.PrpLWfTaskVo;
import ins.sino.claimcar.flow.vo.WfTaskSubmitVo;
import ins.sino.claimcar.manager.vo.AccBankNameVo;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnDataTypeEnum;
import ins.sino.claimcar.pinganUnion.service.PingAnCompensateHandleService;
import ins.sino.claimcar.pinganUnion.util.PingAnMD5Utils;
import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganunion.vo.compensate.*;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentRequestDto;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentRequestParamDto;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentResponseDataDto;
import ins.sino.claimcar.pinganunion.vo.payment.UnionPaymentResultDto;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.subrogation.service.SubrogationService;
import ins.sino.claimcar.subrogation.vo.PrpLSubrogationMainVo;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.*;

/**
 * ??????????????????-????????????????????????-????????????
 *
 * @author mfn
 * @date 2020/7/29 16:01
 */
@Service(protocol = "dubbo", validation = "true", registry = {"default"})
@Path("pingAnCompensateHandleService")
public class PingAnCompensateHandleServiceImpl implements PingAnCompensateHandleService {
    private static Logger logger = LoggerFactory.getLogger(PingAnCompensateHandleServiceImpl.class);
    @Autowired
    private WfTaskHandleService wfTaskHandleService;
    @Autowired
    private BillNoService billNoService;
    @Autowired
    private PolicyViewService policyViewService;
    @Autowired
    PingAnDictService pingAnDictService;
    @Autowired
    private ClaimService claimService;
    @Autowired
    private RegistService registService;
    @Autowired
    private CompensateService compensatateService;
    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private VerifyClaimService verifyClaimService;
    @Autowired
    private PayCustomService payCustomService;
    @Autowired
    private AreaDictService areaDictService;
    @Autowired
    private SubrogationService subrogationService;
    @Autowired
    InterfaceAsyncService interfaceAsyncService;
    @Autowired
    private DatabaseDao databaseDao;
    @Override
    public ResultBean pingAnHandle(PingAnDataNoticeVo pingAnDataNoticeVo) {
        ResultBean resultBean = new ResultBean();
        resultBean = commitCompensateData(pingAnDataNoticeVo, resultBean);
        return resultBean;
    }

    /**
     * ?????????????????????????????????
     *
     * @param registNo           ???????????????????????????
     * @param pingAnDataNoticeVo ????????????????????????
     * @param paymentRespData    ????????????????????????
     * @param compensateRespData ??????????????????
     * @return ????????????  UnionCompensateAllParamsDto allParamsDto
     */
    private ResultBean handleResponseData(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo,
                                          String paymentRespData, String compensateRespData)  {
        logger.info("??????????????????-??????????????????-????????????????????????...????????????{}  ?????????????????????{}  ?????????????????????{}", registNo, paymentRespData, compensateRespData);
        ResultBean resultBean = ResultBean.success();
        Gson gson = new Gson();

        try {
            // ?????????????????????????????????????????????????????????
            UnionPaymentResponseDataDto paymentDataDto = gson.fromJson(paymentRespData, UnionPaymentResponseDataDto.class);
            // ?????????????????????????????????????????????????????????
            UnionCompensateResponseDataDto compensateDataDto = gson.fromJson(compensateRespData, UnionCompensateResponseDataDto.class);
            // ????????????
            UnionCompensateAllParamsDto allParamsDto = gson.fromJson(pingAnDataNoticeVo.getParamObj(), UnionCompensateAllParamsDto.class);

            // ??????????????????
            validData(paymentDataDto, compensateDataDto);

            // ??????????????????
            List<UnionCompensatePolicyPayDto> policyPayDtoList = compensateDataDto.getPolicyPayDTOList();
            // ??????????????????
            List<UnionCompensatePlanDutyPayDto> planDutyPayDtoList = compensateDataDto.getPlanDutyPayDTOList();
            // ??????????????????
            List<UnionCompensatePlanPayDto> planPayDtoList = compensateDataDto.getPlanPayDTOList();
            // ????????????????????????
            List<UnionCompensatePolicyBatchPayDto> policyBatchPayDtoList = compensateDataDto.getPolicyBatchPayDTOList();
            // ??????????????????
            UnionCompensateWholeCaseBaseDto caseBaseDto = compensateDataDto.getWholeCaseBaseDTO();
            // ??????????????????
            List<UnionPaymentResultDto> paymentResultDtoList = paymentDataDto.getPaymentResultList();
            // PingAnHandleService pingAnPaymentHandleService = (PingAnHandleService)SpringUtils.getObject("pingAnPaymentHandleServiceImpl");
            // pingAnPaymentHandleService.pingAnHandle(registNo, pingAnDataNoticeVo, paymentRespData);
//            List<PrpLPayCustomVo> payCustomVos = managerService.findPayCustomVoByRegistNo(registNo);
//            Map<String, Long> payeeidMap = new HashMap<String, Long>();
//            if (payCustomVos != null && payCustomVos.size() > 0) {
//                for (PrpLPayCustomVo customVo : payCustomVos) {
//                    payeeidMap.put(customVo.getPaicclaimNo() + "-" + customVo.getCaseTimes(), customVo.getId());
//                }
//            } else {
//                throw new IllegalArgumentException("????????????" + registNo + "???????????????????????????");
//            }

            Map<String, List<UnionCompensatePlanDutyPayDto>> planDutyPayMap = new HashMap<String, List<UnionCompensatePlanDutyPayDto>>();
            for (UnionCompensatePlanDutyPayDto planDutyPayDto : planDutyPayDtoList) {
                if (planDutyPayDto.getCaseNo() != null) {
                    String caseNo = planDutyPayDto.getCaseNo();
                    List<UnionCompensatePlanDutyPayDto> list = new ArrayList<UnionCompensatePlanDutyPayDto>();
                    if (planDutyPayMap.get(caseNo) == null) {
                        list.add(planDutyPayDto);
                        planDutyPayMap.put(planDutyPayDto.getCaseNo(), list);
                    } else {
                        list = planDutyPayMap.get(caseNo);
                        list.add(planDutyPayDto);
                        planDutyPayMap.put(caseNo, list);
                    }
                }
            }


            // ????????????????????????
            List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);

            // ??????????????????????????????-prplcompensate.times
            Integer caseTimes = allParamsDto.getCaseTimes();

            Map<String, BigDecimal> planPreFeePayMap = new HashMap<String, BigDecimal>();
            List<PrpLCompensateVo> listCompensates = compensatateService.findCompensatevosByRegistNo(registNo);
            if(listCompensates != null && listCompensates.size()>0){
                for (PrpLCompensateVo compensateVo : listCompensates ) {
                    if(CodeConstants.CompensateType.prepay_type.equals(compensateVo.getCompensateType()) && caseTimes==compensateVo.getTimes()){
                        List<PrpLPrePayVo> prePayVoList = compensatateService.queryPrePay(compensateVo.getCompensateNo());
                        if(prePayVoList != null && prePayVoList.size()>0){
                            for (PrpLPrePayVo prePayVo : prePayVoList) {
                                if(CodeConstants.FeeType.FEE.equals(prePayVo.getFeeType())) {
                                    if (planPreFeePayMap.containsKey(prePayVo.getChargeName()+"_"+prePayVo.getRiskCode()+"_"+prePayVo.getKindCode())) {
                                        BigDecimal chargeFee = planPreFeePayMap.get(prePayVo.getChargeName()+"_"+prePayVo.getRiskCode()+"_"+prePayVo.getKindCode());
                                        chargeFee = chargeFee.add(prePayVo.getPayAmt());
                                        planPreFeePayMap.put(prePayVo.getChargeName()+"_"+prePayVo.getRiskCode()+"_"+prePayVo.getKindCode(), chargeFee);
                                    } else {
                                        planPreFeePayMap.put(prePayVo.getChargeName()+"_"+prePayVo.getRiskCode()+"_"+prePayVo.getKindCode(), prePayVo.getPayAmt());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ???????????? 1-????????????  3-????????????
            String caseType;
            if ("Y".equals(caseBaseDto.getIsAgentCase())) {
                caseType = "3";
            } else {
                caseType = "1";
            }
            // ?????????????????????????????????VAT
            Set<String> compensateNoSet = new HashSet<String>(2);
            // ????????????????????????
            Map<String, UnionCompensatePolicyBatchPayDto> PApolicyMap = new HashMap<String, UnionCompensatePolicyBatchPayDto>();
            Map<String, Long> paycustomMap = new HashMap<String, Long>();
            for (UnionCompensatePolicyBatchPayDto policyPatchPayDto : policyBatchPayDtoList) {
                // ???????????????
                String caseNo = policyPatchPayDto.getCaseNo();
                // ????????????????????????
                PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
                if (registVo == null) {
                    throw new IllegalArgumentException("??????????????????" + registNo + "??????????????????" + policyPatchPayDto.getCaseNo() + " ????????????????????????");
                }

                // ?????????????????????????????????
                String planCode = "";
                // ????????????
                String dutyRate = "";
                // ???????????????
                String nopayRate = "";
                // ???????????????????????????????????????
                List<UnionCompensatePlanDutyPayDto> curPlanDutyPayList = planDutyPayMap.get(caseNo);
                if (curPlanDutyPayList != null && curPlanDutyPayList.size() > 0) {
                    for (UnionCompensatePlanDutyPayDto dutyPayDto : curPlanDutyPayList) {
                        if (StringUtils.isBlank(planCode) && StringUtils.isNotBlank(dutyPayDto.getPlanCode())) {
                            planCode =  dutyPayDto.getPlanCode();
                        }

                        if (StringUtils.isBlank(dutyRate) && StringUtils.isNotBlank(dutyPayDto.getDutyRate())) {
                            dutyRate = dutyPayDto.getDutyRate();
                        }

                        if (StringUtils.isBlank(nopayRate) && StringUtils.isNotBlank(dutyPayDto.getNopayRate())
                                && new BigDecimal(dutyPayDto.getNopayRate()).compareTo(BigDecimal.ZERO) > -1) {
                            nopayRate = dutyPayDto.getNopayRate();
                        }
                    }
                }
                PiccCodeDictVo codeDictVo = pingAnDictService.getDictData("planCode", planCode);
                // ??????????????????
                String riskCode = "";
                if (null != codeDictVo) {
                    riskCode = codeDictVo.getDhCodeCode();
                }
                logger.info("??????????????????-??????????????????-------------------------------->riskcode={}",riskCode);
                if (StringUtils.isBlank(riskCode)) {
                    throw new IllegalArgumentException("??????????????????planCode: " + planCode + " ????????????????????????????????????");
                }

                PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
                for (PrpLCMainVo cmainVo : prpLCMainVoList) {
                    if (riskCode != null && riskCode.equals(cmainVo.getRiskCode())) {
                        prpLCMainVo = cmainVo;
                        break;
                    }
                }

                // ????????????????????????
                List<PrpLClaimVo> claimVoList = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, prpLCMainVo.getPolicyNo(), "1");
                PrpLClaimVo claimVo = new PrpLClaimVo();
                if (claimVoList != null && claimVoList.size() > 0) {
                    claimVo = claimVoList.get(0);
                }
                logger.info("??????????????????-??????????????????------------------------------>comCode={}",prpLCMainVo.getComCode());
                // ??????????????????
                String compensateNo = billNoService.getCompensateNo(prpLCMainVo.getComCode(), riskCode);
                Map<String,List<Map<Long,String>>> kindCodeMap = new HashMap<>();//???????????????MAP??????
                PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                PrpLCompensateExtVo extVo = new PrpLCompensateExtVo();
                extVo.setRegistNo(registNo);
                extVo.setCheckDeductFlag("1");
                extVo.setPayBackState("1");
                extVo.setHurtNum(BigDecimal.ZERO);
                extVo.setDeathNum(BigDecimal.ZERO);
                extVo.setInjureNum(BigDecimal.ZERO);
                extVo.setCreateTime(new Date());
                extVo.setUpdateTime(new Date());
                extVo.setWriteOffFlag("0");
                extVo.setIsFastReparation("0");
                extVo.setIsAutoPay("0");
                extVo.setDisastersFlag("Y".equals(caseBaseDto.getIsHugeAccident()) ? "1" : "0");
                compensateVo.setPrpLCompensateExt(extVo);

                compensateVo.setCompensateNo(compensateNo);
                compensateVo.setClaimNo(claimVo.getClaimNo());
                compensateVo.setRegistNo(registNo);
                compensateVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                compensateVo.setRiskCode(riskCode);
                compensateVo.setMakeCom(prpLCMainVo.getComCode());
                compensateVo.setComCode(prpLCMainVo.getComCode());
                compensateVo.setCaseType(caseType);
                compensateVo.setCompensateType(CodeConstants.CompensateFlag.compensate);
                compensateVo.setIndemnityDuty(getIndeminityDuty(new BigDecimal(dutyRate)));
                compensateVo.setIndemnityDutyRate(new BigDecimal(nopayRate));
                compensateVo.setSumAmt(policyPatchPayDto.getPolicyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(policyPatchPayDto.getPolicyPayAmount()));
                compensateVo.setSumPaidAmt(policyPatchPayDto.getFinalPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(policyPatchPayDto.getFinalPayAmount()));
                compensateVo.setSumPreAmt(compensateVo.getSumAmt().subtract(compensateVo.getSumPaidAmt()));
                compensateVo.setSumFee(policyPatchPayDto.getPolicyFee() == null ? BigDecimal.ZERO : new BigDecimal(policyPatchPayDto.getPolicyFee()));
                compensateVo.setSumPaidFee(policyPatchPayDto.getFinalFee() == null ? BigDecimal.ZERO : new BigDecimal(policyPatchPayDto.getFinalFee()));
                compensateVo.setAllLossFlag("0");
                compensateVo.setLawsuitFlag("0");
                compensateVo.setRecoveryFlag("0");
                compensateVo.setAllowFlag("0");
                compensateVo.setCreateUser("AUTO");
                compensateVo.setUnderwriteFlag("1");
                compensateVo.setUnderwriteUser("AUTO");
                compensateVo.setUnderwriteDate(new Date());
                compensateVo.setCreateTime(new Date());
                compensateVo.setUpdateTime(new Date());
                compensateVo.setCompensateKind("1101".equals(riskCode) ? "2" : "1");
                compensateVo.setTimes(caseTimes);
                compensateVo.setCurrency("CNY");
                compensateVo.setDeductType("3");
                compensateVo.setHandler1Code(prpLCMainVo.getHandler1Code());
                compensateVo.setPisderoAmout(BigDecimal.ZERO);
                compensateVo.setCisderoAmout(BigDecimal.ZERO);

                List<PrpLLossPersonVo> lossPersonVoList = new ArrayList<PrpLLossPersonVo>();
                List<PrpLLossItemVo> lossItemVoList = new ArrayList<PrpLLossItemVo>();
                for(UnionCompensatePlanDutyPayDto dutyDto : curPlanDutyPayList) {
                    if (Risk.DQZ.equals(riskCode)) {
                        if (CodeConstants.PINGAN_UNION.DUTYCODE_DEATH.equals(dutyDto.getDutyCode())
                            || CodeConstants.PINGAN_UNION.DUTYCODE_MEDICAL.equals(dutyDto.getDutyCode())) {
                            PrpLLossPersonFeeDetailVo personFeeDetailVo = new PrpLLossPersonFeeDetailVo();
                            PrpLLossPersonFeeVo personFeeVo = new PrpLLossPersonFeeVo();
                            PrpLLossPersonVo personVo = new PrpLLossPersonVo();
                            if (CodeConstants.PINGAN_UNION.DUTYCODE_DEATH.equals(dutyDto.getDutyCode())) {
                                personFeeDetailVo.setLossItemNo("02");
                                personFeeVo.setLossItemNo("02");
                            } else {
                                personFeeDetailVo.setLossItemNo("03");
                                personFeeVo.setLossItemNo("03");
                            }
                            personFeeDetailVo.setFeeTypeCode("1");
                            personFeeDetailVo.setLossFee(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                            personFeeDetailVo.setRealPay(dutyDto.getDutyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getDutyPayAmount()));
                            List<PrpLLossPersonFeeDetailVo> personFeeDetailVoList = new ArrayList<PrpLLossPersonFeeDetailVo>();
                            personFeeDetailVoList.add(personFeeDetailVo);

                            personFeeVo.setCompensateNo(compensateNo);
                            personFeeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                            personFeeVo.setFeeLoss(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                            personFeeVo.setFeeRealPay(dutyDto.getDutyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getDutyPayAmount()));
                            personFeeVo.setPrpLLossPersonFeeDetails(personFeeDetailVoList);
                            List<PrpLLossPersonFeeVo> personFeeVoList = new ArrayList<PrpLLossPersonFeeVo>();
                            personFeeVoList.add(personFeeVo);

                            personVo.setRegistNo(registNo);
                            personVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                            personVo.setRiskCode(riskCode);
                            personVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
                            personVo.setLossType("1");
                            personVo.setDutyRate(StringUtils.isBlank(dutyRate) ? BigDecimal.ZERO : new BigDecimal(dutyRate));
                            personVo.setDeductDutyRate(StringUtils.isBlank(nopayRate) ? BigDecimal.ZERO : new BigDecimal(nopayRate));
                            personVo.setSumLoss(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                            personVo.setSumRealPay(dutyDto.getDutyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getDutyPayAmount()));
                            personVo.setCreateTime(new Date());
                            personVo.setUpdateTime(new Date());
                            personVo.setOriginLossFee(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                            personVo.setPrpLLossPersonFees(personFeeVoList);

                            lossPersonVoList.add(personVo);
                        } else {
                            PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
                            lossItemVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                            lossItemVo.setRiskCode(riskCode);
                            lossItemVo.setKindCode(CodeConstants.KINDCODE.KINDCODE_BZ);
                            lossItemVo.setItemAmount(StringUtils.isBlank(dutyDto.getDutyPayLimit()) ? null : new BigDecimal(dutyDto.getDutyPayLimit()));
                            lossItemVo.setLossType("3");
                            lossItemVo.setSumLoss(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                            lossItemVo.setSumRealPay(dutyDto.getDutyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getDutyPayAmount()));
                            lossItemVo.setDutyRate(StringUtils.isBlank(dutyRate) ? BigDecimal.ZERO : new BigDecimal(dutyRate));
                            lossItemVo.setDeductDutyRate(StringUtils.isBlank(nopayRate) ? BigDecimal.ZERO : new BigDecimal(nopayRate));
                            lossItemVo.setRescueFee(BigDecimal.ZERO);
                            lossItemVo.setPayFlag("3");
                            lossItemVo.setCreateTime(new Date());
                            lossItemVo.setUpdateTime(new Date());
                            lossItemVo.setRegistNo(registNo);
                            lossItemVo.setOriginLossFee(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));

                            lossItemVoList.add(lossItemVo);
                        }
                    } else {
                        PrpLLossItemVo lossItemVo = new PrpLLossItemVo();
                        lossItemVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        lossItemVo.setRiskCode(riskCode);
                        codeDictVo = pingAnDictService.getDictData("dutyCode", dutyDto.getDutyCode());
                        String dutyCode = "";
                        if (codeDictVo != null) {
                            dutyCode = codeDictVo.getDhCodeCode();
                        }
                        lossItemVo.setKindCode(dutyCode);
                        lossItemVo.setItemAmount(StringUtils.isBlank(dutyDto.getDutyPayLimit()) ? null : new BigDecimal(dutyDto.getDutyPayLimit()));
                        lossItemVo.setLossType("3");
                        lossItemVo.setSumLoss(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                        lossItemVo.setSumRealPay(dutyDto.getDutyPayAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getDutyPayAmount()));
                        lossItemVo.setDutyRate(StringUtils.isBlank(dutyRate) ? BigDecimal.ZERO : new BigDecimal(dutyRate));
                        lossItemVo.setDeductDutyRate(StringUtils.isBlank(nopayRate) ? BigDecimal.ZERO : new BigDecimal(nopayRate));
                        lossItemVo.setRescueFee(BigDecimal.ZERO);
                        lossItemVo.setPayFlag("3");
                        lossItemVo.setCreateTime(new Date());
                        lossItemVo.setUpdateTime(new Date());
                        lossItemVo.setRegistNo(registNo);
                        lossItemVo.setOriginLossFee(dutyDto.getLossAmount() == null ? BigDecimal.ZERO : new BigDecimal(dutyDto.getLossAmount()));
                        //?????????????????????????????????????????????0???????????????0??????????????????
                        if(lossItemVo.getSumRealPay().compareTo(BigDecimal.ZERO)>0){
                            lossItemVoList.add(lossItemVo);
                        }
                    }
                }

                // ??????????????????
                List<PrpLChargeVo> chargeVoList = new ArrayList<PrpLChargeVo>();
                BigDecimal sumPaidFee = BigDecimal.ZERO;
                int chargeIndex = 0;
                for (UnionCompensatePlanDutyPayDto planDutyPayDto : curPlanDutyPayList) {
                    String kindCode = "";
                    if (Risk.DQZ.equals(riskCode)) {
                        kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
                    } else {
                        codeDictVo = pingAnDictService.getDictData("dutyCode", planDutyPayDto.getDutyCode());
                        // ??????????????????
                        if (null != codeDictVo) {
                            kindCode = codeDictVo.getDhCodeCode();
                        }
                    }

                    if (planDutyPayDto.getArbitrateFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getArbitrateFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getArbitrateFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getArbitrateFee()));
                        BigDecimal preArbitrateFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preArbitrateFee != null){
                            chargeVo.setOffPreAmt(preArbitrateFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getArbitrateFee())));
                    }
                    if (planDutyPayDto.getLawsuitFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getLawsuitFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("07");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getLawsuitFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getLawsuitFee()));
                        BigDecimal preLawsuitFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preLawsuitFee != null){
                            chargeVo.setOffPreAmt(preLawsuitFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getLawsuitFee())));
                    }
                    if (planDutyPayDto.getLawyerFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getLawyerFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("06");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getLawyerFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getLawyerFee()));
                        BigDecimal preLawyerFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preLawyerFee != null){
                            chargeVo.setOffPreAmt(preLawyerFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getLawyerFee())));
                    }
                    if (planDutyPayDto.getCheckFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getCheckFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getCheckFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getCheckFee()));
                        BigDecimal preCheckFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preCheckFee != null){
                            chargeVo.setOffPreAmt(preCheckFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getCheckFee())));
                    }
                    if (planDutyPayDto.getExecuteFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getExecuteFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getExecuteFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getExecuteFee()));
                        BigDecimal preExecuteFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preExecuteFee != null){
                            chargeVo.setOffPreAmt(preExecuteFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getExecuteFee())));
                    }
                    if (planDutyPayDto.getEvaluationFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getEvaluationFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("13");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getEvaluationFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getEvaluationFee()));
                        BigDecimal preEvaluationFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preEvaluationFee != null){
                            chargeVo.setOffPreAmt(preEvaluationFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getEvaluationFee())));
                    }
                    if (planDutyPayDto.getSurveyFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getSurveyFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("04");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getSurveyFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getSurveyFee()));
                        BigDecimal preSurveyFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preSurveyFee != null){
                            chargeVo.setOffPreAmt(preSurveyFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getSurveyFee())));
                    }
                    if (planDutyPayDto.getMavinAppraisalFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getMavinAppraisalFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("18");
                        chargeVo.setChargeName("???????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getMavinAppraisalFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getMavinAppraisalFee()));
                        BigDecimal preMavinAppraisalFee =planPreFeePayMap.get("???????????????_"+riskCode+"_"+kindCode);
                        if(preMavinAppraisalFee != null){
                            chargeVo.setOffPreAmt(preMavinAppraisalFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getMavinAppraisalFee())));
                    }
                    if (planDutyPayDto.getInquiryEvidenceFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("???????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee()));
                        BigDecimal preInquiryEvidenceFee =planPreFeePayMap.get("???????????????_"+riskCode+"_"+kindCode);
                        if(preInquiryEvidenceFee != null){
                            chargeVo.setOffPreAmt(preInquiryEvidenceFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee())));
                    }
                    if (planDutyPayDto.getConsultFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getConsultFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("16");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getConsultFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getConsultFee()));
                        BigDecimal preConsultFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preConsultFee != null){
                            chargeVo.setOffPreAmt(preConsultFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getConsultFee())));
                    }
                    if (planDutyPayDto.getCarRentalFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getCarRentalFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getCarRentalFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getCarRentalFee()));
                        BigDecimal preCarRentalFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preCarRentalFee != null){
                            chargeVo.setOffPreAmt(preCarRentalFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getCarRentalFee())));
                    }
                    if (planDutyPayDto.getDetectFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getDetectFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("15");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getDetectFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getDetectFee()));
                        BigDecimal preDetectFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preDetectFee != null){
                            chargeVo.setOffPreAmt(preDetectFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getDetectFee())));
                    }
                    if (planDutyPayDto.getTravelFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getTravelFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("17");
                        chargeVo.setChargeName("?????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getTravelFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getTravelFee()));
                        BigDecimal preTravelFee =planPreFeePayMap.get("?????????_"+riskCode+"_"+kindCode);
                        if(preTravelFee != null){
                            chargeVo.setOffPreAmt(preTravelFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getTravelFee())));
                    }
                    if (planDutyPayDto.getSurveySubsidyFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getSurveySubsidyFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("???????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getSurveySubsidyFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getSurveySubsidyFee()));
                        BigDecimal preSurveySubsidyFee =planPreFeePayMap.get("???????????????_"+riskCode+"_"+kindCode);
                        if(preSurveySubsidyFee != null){
                            chargeVo.setOffPreAmt(preSurveySubsidyFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getSurveySubsidyFee())));
                    }
                    if (planDutyPayDto.getEvaluationOutFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getEvaluationOutFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("13");
                        chargeVo.setChargeName("?????????????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getEvaluationOutFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getEvaluationOutFee()));
                        BigDecimal preEvaluationOutFee =planPreFeePayMap.get("?????????????????????_"+riskCode+"_"+kindCode);
                        if(preEvaluationOutFee != null){
                            chargeVo.setOffPreAmt(preEvaluationOutFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getEvaluationOutFee())));
                    }
                    if (planDutyPayDto.getConsultPubFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getConsultPubFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("16");
                        chargeVo.setChargeName("?????????????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getConsultPubFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getConsultPubFee()));
                        BigDecimal preConsultPubFee =planPreFeePayMap.get("?????????????????????_"+riskCode+"_"+kindCode);
                        if(preConsultPubFee != null){
                            chargeVo.setOffPreAmt(preConsultPubFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getConsultPubFee())));
                    }
                    if (planDutyPayDto.getNotarialFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getNotarialFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("???????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getNotarialFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getNotarialFee()));
                        BigDecimal preNotarialFee =planPreFeePayMap.get("???????????????_"+riskCode+"_"+kindCode);
                        if(preNotarialFee != null){
                            chargeVo.setOffPreAmt(preNotarialFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getNotarialFee())));
                    }
                    if (planDutyPayDto.getPreInvestigateFee() != null && BigDecimal.ZERO.compareTo(new BigDecimal(planDutyPayDto.getPreInvestigateFee())) < 0) {
                        PrpLChargeVo chargeVo = new PrpLChargeVo();
                        chargeVo.setPolicyNo(prpLCMainVo.getPolicyNo());
                        chargeVo.setRiskCode(riskCode);
                        chargeVo.setKindCode(kindCode);
                        chargeVo.setCurrency("CNY");
                        chargeVo.setOffAmt(BigDecimal.ZERO);
                        chargeVo.setValidFlag("1");
                        chargeVo.setPayStatus("2");
                        chargeVo.setInvoiceType("000");
                        chargeVo.setChargeCode("99");
                        chargeVo.setChargeName("???????????????");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getPreInvestigateFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getPreInvestigateFee()));
                        BigDecimal prePreInvestigateFee =planPreFeePayMap.get("???????????????_"+riskCode+"_"+kindCode);
                        if(prePreInvestigateFee != null){
                            chargeVo.setOffPreAmt(prePreInvestigateFee);//??????????????????
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//????????????????????????????????????
                        }
                        chargeVoList.add(chargeVo);
                        sumPaidFee = sumPaidFee.add(DataUtils.NullToZero(new BigDecimal(planDutyPayDto.getPreInvestigateFee())));
                    }
                }
                compensateVo.setSumPreFee(compensateVo.getSumFee().subtract(compensateVo.getSumPaidFee()));
                List<PrpLPaymentVo> paymentVoList = new ArrayList<PrpLPaymentVo>();
                int paymentIndex = 0;
                if (paymentResultDtoList != null && paymentResultDtoList.size() > 0) {
                    for (UnionPaymentResultDto resultDto : paymentResultDtoList) {
                        //????????????????????????????????????????????????????????????????????????
                        if (caseNo.equals(resultDto.getCaseNo())) {
                            // ?????????????????????
                            Long payeeid;
                            String accountNo = resultDto.getClientBankAccount();
                            if (paycustomMap.get(accountNo) != null) {
                                payeeid = paycustomMap.get(accountNo);
                            } else {
                                payeeid = savePaycustoms(registNo, resultDto, prpLCMainVo.getComCode());
                                paycustomMap.put(resultDto.getClientBankAccount(), payeeid);
                            }
                            // ??????????????????
                            PrpLPaymentVo paymentVo = new PrpLPaymentVo();
                            paymentVo.setCompensateNo(compensateNo);
                            paymentVo.setOtherFlag("0");
                            paymentVo.setSumRealPay(resultDto.getPaymentAmount());
                            paymentVo.setPayStatus("2");
                            paymentVo.setCreateTime(new Date());
                            paymentVo.setUpdateTime(new Date());
                            // ???????????????id
                            paymentVo.setPayeeId(payeeid);
                            paymentVo.setSerialNo("" + paymentIndex);
                            paymentIndex++;
                            // 1-?????? 2-?????? 3-?????? 4-????????????
                            paymentVo.setPayFlag("3");
                            paymentVo.setIdClmPaymentResult(resultDto.getIdClmPaymentResult());
                            paymentVoList.add(paymentVo);
                        }
                    }
                }
                SysUserVo userVo = new SysUserVo();
                userVo.setUserCode("AUTO");
                userVo.setUserName("AUTO");
                userVo.setComCode(prpLCMainVo.getComCode());
                if (paymentVoList.size() > 0) {
                	Long customId = paymentVoList.get(0).getPayeeId();
                    String idClmPaymentResult = paymentVoList.get(0).getIdClmPaymentResult();
					for (PrpLChargeVo vo : chargeVoList) {
						vo.setPayeeId(customId);
						vo.setIdClmPaymentResult(idClmPaymentResult);
					}
				}
                compensatateService.saveCompensates(compensateVo, lossItemVoList, new ArrayList<PrpLLossPropVo>(), lossPersonVoList, chargeVoList, paymentVoList, userVo);
                List<PrpLWfTaskVo> wfTaskVoList = wfTaskHandleService.findPrpLWfTaskInByRegistNo(registNo);
                // ??????????????????
                PrpLWfTaskVo compensateTaskVo = null;
                FlowNode currentNode = Risk.DQZ.equals(riskCode) ? FlowNode.CompeCI : FlowNode.CompeBI;
                FlowNode nextNode = Risk.DQZ.equals(riskCode) ? FlowNode.VClaim_CI_LV0 : FlowNode.VClaim_BI_LV0;
                for (PrpLWfTaskVo tmpTaskVo : wfTaskVoList) {
                    if (currentNode.toString().equals(tmpTaskVo.getSubNodeCode())) {
                        compensateTaskVo = tmpTaskVo;
                        break;
                    }
                }
                if (compensateTaskVo == null) {
                    throw new IllegalArgumentException("????????????" + registNo + " ?????????????????????????????????????????????");
                }
                WfTaskSubmitVo nextVo = new WfTaskSubmitVo();
                nextVo.setSubmitLevel(0);
                nextVo.setTaskInUser("AUTO");
                nextVo.setFlowTaskId(compensateTaskVo.getTaskId());
                nextVo.setComCode(userVo.getComCode());
                nextVo.setFlowId(compensateTaskVo.getFlowId());
                nextVo.setTaskInUser(userVo.getUserCode());
                nextVo.setCurrentNode(currentNode);
                nextVo.setNextNode(nextNode);
                nextVo.setTaskInKey(compensateNo);
                // ?????????????????? start
                PrpLSubrogationMainVo subrogationMainVo = new PrpLSubrogationMainVo();
                subrogationMainVo.setRegistNo(registNo);
                subrogationMainVo.setSubrogationFlag("0");
                subrogationMainVo.setValidFlag("1");
                subrogationMainVo.setCreateUser("AUTO");
                subrogationMainVo.setCreateTime(new Date());
                subrogationMainVo.setUpdateUser("AUTO");
                subrogationMainVo.setUpdateTime(new Date());
                subrogationService.saveSubrogationInfo(subrogationMainVo);
                // ??????????????????  end
                PrpLCompensateVo prpLCompensateVo = compensateTaskService.findPrpLCompensateVoByPK(nextVo.getTaskInKey());
                compensateTaskService.submitCompeWfTaskVo(prpLCompensateVo, compensateTaskVo, nextVo, "true", userVo);
                // ?????????????????????????????????
                Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo, prpLCompensateVo);
                // ??????????????????
                verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, prpLCompensateVo, uwNotionMainId);

                compensateNoSet.add(compensateNo);
            }
            resultBean.setCompensateNo(compensateNoSet);
        } catch (Exception e) {
            logger.info("??????????????????-??????/????????????????????????????????????????????????!", e);
            resultBean.fail("??????????????????-??????/???????????????????????????????????????????????????");
        }
        logger.info("??????????????????-??????????????????-????????????????????????...????????????{}", registNo);

        return resultBean;
    }

    /**
     *
     * @param cacheMap
     * @param lossItemVos
     * @param lossPersonVos
     * @param chargeVos
     * @param compensateNo
     */
    private void saveToCatchOfKindCode(Map<String,List<Map<Long,String>>> cacheMap, List<PrpLLossItemVo> lossItemVos, List<PrpLLossPersonVo> lossPersonVos, List<PrpLChargeVo> chargeVos,String compensateNo) {
        logger.info("????????????????????????????????????????????????????????????:{}",compensateNo);
        if(lossItemVos != null && lossItemVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLLossItemVo lossItemVo : lossItemVos) {
                logger.info("????????????????????????????????????????????????:{},ID??????:{},kindCode?????????{}",compensateNo,lossItemVo.getId(),lossItemVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(lossItemVo.getId(),lossItemVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("lossItemVos",kindCodeList);
        }
        if(lossPersonVos != null && lossPersonVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLLossPersonVo lossPersonVo : lossPersonVos) {
                logger.info("????????????????????????????????????????????????:{},ID??????:{},kindCode?????????{}",compensateNo,lossPersonVo.getId(),lossPersonVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(lossPersonVo.getId(),lossPersonVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("lossPersonVos",kindCodeList);
        }
        if(chargeVos != null && chargeVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLChargeVo chargeVo : chargeVos) {
                logger.info("????????????????????????????????????????????????:{},ID??????:{},kindCode?????????{}",compensateNo,chargeVo.getId(),chargeVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(chargeVo.getId(),chargeVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("chargeVos",kindCodeList);
        }
        pingAnDictService.saveKindCodeToCatche(compensateNo,cacheMap);
        logger.info("????????????????????????????????????????????????????????????:{}",compensateNo);
    }

    /**
     * ????????????????????????????????????
     *
     * @param requestDto   ??????????????????
     * @param allParamsDto ???????????????????????????????????????????????????
     * @return ??????????????????json
     */
    private String getCompensateRequestParams(UnionCompensateRequestDto requestDto, UnionCompensateAllParamsDto allParamsDto) {
        if (allParamsDto != null) {
            Gson gson = new Gson();
            UnionCompensateRequestParamDto requestParamDto = new UnionCompensateRequestParamDto();
            requestParamDto.setReportNo(allParamsDto.getReportNo());
            requestParamDto.setCaseTimes(allParamsDto.getCaseTimes());
            requestParamDto.setIdClmBatch(allParamsDto.getIdClmBatch());
            requestParamDto.setClaimType(allParamsDto.getClaimType());

            requestDto.setParamObj(requestParamDto);
            return gson.toJson(requestDto);
        }
        return "";
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param requestDto   ????????????????????????
     * @param allParamsDto ???????????????????????????????????????????????????
     * @return ??????????????????json
     */
    private String getPaymentRequestParams(UnionPaymentRequestDto requestDto, UnionCompensateAllParamsDto allParamsDto) {
        if (allParamsDto != null) {
            Gson gson = new Gson();

            UnionPaymentRequestParamDto requestParamDto = new UnionPaymentRequestParamDto();
            requestParamDto.setReportNo(allParamsDto.getReportNo());
            requestParamDto.setCaseTimes(allParamsDto.getCaseTimes());
            requestParamDto.setList(allParamsDto.getList());

            requestDto.setParamObj(requestParamDto);
            return gson.toJson(requestDto);
        }
        return "";
    }

    /**
     * ????????????
     * @param paymentDataDto ??????????????????
     * @param compensateDataDto ??????????????????
     * @throws Exception ????????????
     */
    private void validData(UnionPaymentResponseDataDto paymentDataDto, UnionCompensateResponseDataDto compensateDataDto) throws Exception {
        // ??????????????????
        List<UnionCompensatePolicyPayDto> policyPayDtoList = compensateDataDto.getPolicyPayDTOList();
        // ??????????????????
        List<UnionCompensatePlanDutyPayDto> planDutyPayDtoList = compensateDataDto.getPlanDutyPayDTOList();
        // ??????????????????
        List<UnionCompensatePlanPayDto> planPayDtoList = compensateDataDto.getPlanPayDTOList();
        // ????????????????????????
        List<UnionCompensatePolicyBatchPayDto> policyBatchPayDtoList = compensateDataDto.getPolicyBatchPayDTOList();
        // ??????????????????
        UnionCompensateWholeCaseBaseDto caseBaseDto = compensateDataDto.getWholeCaseBaseDTO();
        // ????????????
        List<UnionPaymentResultDto> paymentResultDtoList = paymentDataDto.getPaymentResultList();

        if (paymentResultDtoList == null || paymentResultDtoList.size() == 0) {
            throw new IllegalArgumentException("??????????????????-??????????????????-?????????????????????");
        }
        if (policyPayDtoList == null || policyPayDtoList.size() == 0) {
            throw new IllegalArgumentException("??????????????????-??????????????????-???????????????????????????");
        }
        if (planDutyPayDtoList == null || planDutyPayDtoList.size() == 0) {
            throw new IllegalArgumentException("??????????????????-??????????????????-???????????????????????????");
        }
        if (planPayDtoList == null || planPayDtoList.size() == 0) {
            throw new IllegalArgumentException("??????????????????-??????????????????-???????????????????????????");
        }
        if (policyBatchPayDtoList == null || policyBatchPayDtoList.size() == 0) {
            throw new IllegalArgumentException("??????????????????-??????????????????-?????????????????????????????????");
        }
        if (caseBaseDto == null) {
            throw new IllegalArgumentException("??????????????????-??????????????????-?????????????????????????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param dutyRate ?????????????????????
     * @return ????????????
     */
    private String getIndeminityDuty(BigDecimal dutyRate) {
        if (dutyRate == null) {
            return "";
        }
        if (dutyRate.compareTo(new BigDecimal("100.00")) == 0) {
            return "0";
        } else if (dutyRate.compareTo(new BigDecimal("50.00")) > 0) {
            return "1";
        } else if (dutyRate.compareTo(new BigDecimal("50.00")) == 0) {
            return "2";
        } else if (dutyRate.compareTo(new BigDecimal("0.00")) > 0) {
            return "3";
        } else if (dutyRate.compareTo(new BigDecimal("0.00")) == 0) {
            return "4";
        } else {
            return "";
        }
    }
    
    /**
     * ?????????????????????
     * @param registNo ?????????????????????
     * @param paymentResultDto  ?????????????????????
     * @return ???????????????paycustom.id
     * @throws Exception ???????????????????????????
     */
    private Long savePaycustoms(String registNo, UnionPaymentResultDto paymentResultDto, String comCode) throws Exception {
        // ??????????????????????????????????????????????????????????????????id
        String accountNo = paymentResultDto.getClientBankAccount();
        Long payeeid = getPaycustomIdWithSameAccountNo(registNo, accountNo);
        if (null != payeeid) {
            return payeeid;
        }
        PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();

        payCustomVo.setRegistNo(registNo);
        payCustomVo.setPaicclaimNo(paymentResultDto.getCaseNo());
        payCustomVo.setCaseTimes(paymentResultDto.getCaseTimes());
        // ????????????
        String certifyType = "99";
        PiccCodeDictVo codeDictVo = pingAnDictService.getDictData("certificateType", paymentResultDto.getClientCertificateType());
        if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
            certifyType = codeDictVo.getDhCodeCode();
        }
        certifyType = this.transCertifyType(certifyType);
        payCustomVo.setCertifyType(certifyType);
        payCustomVo.setCertifyNo(paymentResultDto.getClientCertificateNo());
        payCustomVo.setPayObjectType("0".equals(paymentResultDto.getBankAccountAttribute()) ? "2" : "1");
        payCustomVo.setPayeeName(paymentResultDto.getClientName());
        payCustomVo.setPayeeMobile(StringUtils.isBlank(paymentResultDto.getClientMobile()) ? "13500000000" : paymentResultDto.getClientMobile());
        payCustomVo.setBankName("0");
        payCustomVo.setBankOutlets(paymentResultDto.getClientBankName());
        payCustomVo.setAccountNo(paymentResultDto.getClientBankAccount());
        // payCustomVo.setAccountNo("4206230007");
        payCustomVo.setBankNo(paymentResultDto.getClientBankCode());
        // ????????????????????????????????????????????????
        if (paymentResultDto.getClientBankCode() != null && paymentResultDto.getClientBankName() != null) {
            AccBankNameVo accBankNameVo = payCustomService.findBankInfoFromName(paymentResultDto.getClientBankName(),paymentResultDto.getClientBankCode());
            if (accBankNameVo != null) {
                payCustomVo.setBankName(accBankNameVo.getBankCode());//?????????????????????
                payCustomVo.setBankType(accBankNameVo.getBankCode());
                payCustomVo.setProvinceCode(Long.parseLong(accBankNameVo.getProvinceCode()));//?????????
                payCustomVo.setProvince(accBankNameVo.getProvincial());//?????????
                payCustomVo.setCityCode(Long.parseLong(accBankNameVo.getCityCode()));//?????????
                payCustomVo.setCity(accBankNameVo.getCity());//?????????
            }
        }
        payCustomVo.setPriorityFlag("N");
        payCustomVo.setPurpose("????????????????????????");
        payCustomVo.setValidFlag("1");
        payCustomVo.setFlag("0");
        payCustomVo.setRemark("N");
        payCustomVo.setCreateUser("AUTO");
        payCustomVo.setCreateTime(new Date());
        payCustomVo.setUpdateUser("AUTO");
        payCustomVo.setUpdateTime(new Date());
        String clientType = "99";
        codeDictVo = pingAnDictService.getDictData("clientType", paymentResultDto.getClientType());
        if (null != codeDictVo && codeDictVo.getDhCodeCode() != null) {
            clientType = codeDictVo.getDhCodeCode();
        }
        payCustomVo.setPayObjectKind(clientType);
        payCustomVo.setPublicAndPrivate("0".equals(paymentResultDto.getBankAccountAttribute()) ? "1" : "0");
        payCustomVo.setIdClmPaymentResult(paymentResultDto.getIdClmPaymentResult());

        SysUserVo userVo = new SysUserVo();
        userVo.setUserCode("AUTO");
        userVo.setComCode(comCode);

        return payCustomService.saveOrUpdatePayCustom(payCustomVo, userVo);
    }

    /**
     * commit???????????????????????????????????????????????????????????????????????????VAT??????????????????????????????????????????????????????
     * @param pingAnDataNoticeVo
     * @param resultBean
     * @return
     */
    public ResultBean commitCompensateData(PingAnDataNoticeVo pingAnDataNoticeVo, ResultBean resultBean){
        Gson gson = new Gson();
        //????????????????????????Code????????????????????????
        PingAnDataTypeEnum dataTypeEnum = PingAnDataTypeEnum.getEnumByCode(pingAnDataNoticeVo.getPushType());
        if (dataTypeEnum == null) {
            resultBean = resultBean.fail("?????????????????????????????????");
        }

        try {
            //??????????????????
            String siteCode = SpringProperties.getProperty("pingan_siteCode");//HS????????????????????????????????????????????????
            String insuranceCompanyNo = SpringProperties.getProperty("pingan_insuranceCompanyNo");//????????????
            String operatorUm = SpringProperties.getProperty("pingan_operatorUm");//?????????UM
            String compensateResultUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_08_url");//??????URL
            String paymentResultUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_08_01_url");//??????URL

            // ????????????????????????????????????
            String paramObj = pingAnDataNoticeVo.getParamObj();
            UnionCompensateAllParamsDto allParamsDto = gson.fromJson(paramObj, UnionCompensateAllParamsDto.class);

            // ????????????????????????????????????
            UnionPaymentRequestDto paymentRequestDto = new UnionPaymentRequestDto();
            paymentRequestDto.setInsuranceCompanyNo(insuranceCompanyNo);
            paymentRequestDto.setOperatorUm(operatorUm);
            String paymentRequestData = getPaymentRequestParams(paymentRequestDto, allParamsDto);

            //??????????????????????????????
            String paymentEncodeRequestData = Base64EncodedUtil.encode(paymentRequestData);
            String paymentsignature = PingAnMD5Utils.sign(siteCode + paymentEncodeRequestData);//????????????
            //????????????????????????
            Map<String, String> paymentParam = new HashMap<String, String>();
            paymentParam.put("siteCode", siteCode);//HS????????????????????????????????????????????????
            paymentParam.put("reqData", paymentEncodeRequestData);//?????????????????????json???????????????base64??????
            paymentParam.put("signature", paymentsignature);//????????????
            String paymentJsonString = gson.toJson(paymentParam);
            //????????????
            logger.info("{}??????????????????-requestUrl={},??????reqData={},??????param={}", "????????????????????????", paymentResultUrl, paymentRequestData, paymentJsonString);
            String paymentResult = HttpClientJsonUtil.postUtf(paymentResultUrl, paymentJsonString, "UTF-8");

            // ??????????????????????????????
            UnionCompensateRequestDto compensateRequestDto = new UnionCompensateRequestDto();
            compensateRequestDto.setInsuranceCompanyNo(insuranceCompanyNo);
            compensateRequestDto.setOperatorUm(operatorUm);
            String compensateRequestData = getCompensateRequestParams(compensateRequestDto, allParamsDto);
            //??????????????????????????????
            String compensateEncodeRequestData = Base64EncodedUtil.encode(compensateRequestData);
            String compensatesignature = PingAnMD5Utils.sign(siteCode + compensateEncodeRequestData);//????????????
            //????????????????????????
            Map<String, String> compensateParam = new HashMap<String, String>();
            compensateParam.put("siteCode", siteCode);//HS????????????????????????????????????????????????
            compensateParam.put("reqData", compensateEncodeRequestData);//?????????????????????json???????????????base64??????
            compensateParam.put("signature", compensatesignature);//????????????
            String compensateJsonString = gson.toJson(compensateParam);
            //????????????
            logger.info("{}??????????????????-requestUrl={},??????reqData={},??????param={}", "??????????????????", compensateResultUrl, compensateRequestData, compensateEncodeRequestData);
            String compensateResult = HttpClientJsonUtil.postUtf(compensateResultUrl, compensateJsonString, "UTF-8");

            //??????????????????
            JsonObject paymentObj = (JsonObject) new JsonParser().parse(paymentResult);
            String paymentResultCode = paymentObj.get("code").getAsString();//?????????
            String paymentMsg = paymentObj.get("msg").getAsString();//??????????????????
            String paymentRespData = paymentObj.get("respData").getAsString();//???????????????Base64?????????????????????????????????????????????
            String paymentRespSignature = paymentObj.get("signature").getAsString();//????????????

            JsonObject compensateObj = (JsonObject) new JsonParser().parse(compensateResult);
            String compensateResultCode = compensateObj.get("code").getAsString();//?????????
            String compensateMsg = compensateObj.get("msg").getAsString();//??????????????????
            String compensateRespData = compensateObj.get("respData").getAsString();//???????????????Base64?????????????????????????????????????????????
            String compensateRespSignature = compensateObj.get("signature").getAsString();//????????????

            //????????????????????????????????????
            if ("200".equals(paymentResultCode) && "200".equals(compensateResultCode)) {
                //??????????????????????????????
                String paymentdata = paymentResultCode + paymentMsg + paymentRespData;
                Boolean paymentIsVerified = PingAnMD5Utils.verifySign(paymentdata, paymentRespSignature);
                String compensatedata = compensateResultCode + compensateMsg + compensateRespData;
                Boolean compensateIsVerified = PingAnMD5Utils.verifySign(compensatedata, compensateRespSignature);
                //???????????????????????????????????????????????????????????????
                if (paymentIsVerified && compensateIsVerified) {
                    //?????????????????????
                    PrpLRegistVo prpLRegistVo = registService.findRegistByPaicReportNo(pingAnDataNoticeVo.getReportNo());
                    String registNo = prpLRegistVo != null ? prpLRegistVo.getRegistNo() : null;
                    resultBean = handleResponseData(registNo, pingAnDataNoticeVo, Base64EncodedUtil.decode(paymentRespData), Base64EncodedUtil.decode(compensateRespData));
                } else {
                    resultBean = resultBean.fail("??????????????????????????????");
                    logger.error("{}-{}??????????????????????????????,id={},reportNo={}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(), pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo());
                }
            } else {
                resultBean = resultBean.fail(compensateResultCode + ":" + compensateMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}-{}??????????????????,id={},reportNo={},???????????????{}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(), pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo(), ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }
        return resultBean;
    }

    /**
     * ???????????????????????????????????????id
     * @param registNo ?????????
     * @param accountNo ???????????????
     * @return ???????????????
     */
    public Long getPaycustomIdWithSameAccountNo(String registNo, String accountNo) {
        QueryRule queryrule = QueryRule.getInstance();
        queryrule.addEqual("registNo", registNo);
        queryrule.addEqual("accountNo", accountNo);
        queryrule.addEqual("validFlag", "1");

        List<PrpLPayCustom> payCusPoList = databaseDao.findAll(PrpLPayCustom.class, queryrule);
        if (payCusPoList != null && payCusPoList.size() > 0) {
            PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();
            Beans.copy().from(payCusPoList.get(0)).to(payCustomVo);
            return payCustomVo.getId();
        }

        return null;
    }

    /**
     * ??????????????????
     * @param oldCertifyType
     * @return
     */
    private String transCertifyType(String oldCertifyType){
        if(oldCertifyType != null && !"".equals(oldCertifyType)){
            String newCertifyType = "99";
            switch (oldCertifyType){
                case "1":
                    newCertifyType = "01";
                    break;
                case "7":
                    newCertifyType = "03";
                    break;
                case "41":
                    newCertifyType = "99";
                    break;
                case "3":
                    newCertifyType = "05";
                    break;
                case "43":
                    newCertifyType = "06";
                    break;
                case "553":
                    newCertifyType = "553";
                    break;
                case "550":
                    newCertifyType = "550";
                    break;
                case "5":
                    newCertifyType = "09";
                    break;
                default:
                    newCertifyType = "99";
                    break;
            }
            return newCertifyType;
        }else {
            return oldCertifyType;
        }
    }
}
