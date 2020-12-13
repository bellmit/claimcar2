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
 * 平安联盟中心-理算信息查询接口-业务处理
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
     * 处理理算与支付信息数据
     *
     * @param registNo           鼎和理赔系统报案号
     * @param pingAnDataNoticeVo 平安通知数据对象
     * @param paymentRespData    平安支付信息数据
     * @param compensateRespData 平安理算数据
     * @return 处理结果  UnionCompensateAllParamsDto allParamsDto
     */
    private ResultBean handleResponseData(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo,
                                          String paymentRespData, String compensateRespData)  {
        logger.info("平安联盟中心-理算查询接口-业务数据处理开始...报案号：{}  支付信息数据：{}  理算信息数据：{}", registNo, paymentRespData, compensateRespData);
        ResultBean resultBean = ResultBean.success();
        Gson gson = new Gson();

        try {
            // 平安联盟中心返回的支付信息查询结果数据
            UnionPaymentResponseDataDto paymentDataDto = gson.fromJson(paymentRespData, UnionPaymentResponseDataDto.class);
            // 平安联盟中心返回的理算信息查询结果数据
            UnionCompensateResponseDataDto compensateDataDto = gson.fromJson(compensateRespData, UnionCompensateResponseDataDto.class);
            // 请求参数
            UnionCompensateAllParamsDto allParamsDto = gson.fromJson(pingAnDataNoticeVo.getParamObj(), UnionCompensateAllParamsDto.class);

            // 数据简单校验
            validData(paymentDataDto, compensateDataDto);

            // 保单赔付信息
            List<UnionCompensatePolicyPayDto> policyPayDtoList = compensateDataDto.getPolicyPayDTOList();
            // 责任赔付信息
            List<UnionCompensatePlanDutyPayDto> planDutyPayDtoList = compensateDataDto.getPlanDutyPayDTOList();
            // 险种赔付信息
            List<UnionCompensatePlanPayDto> planPayDtoList = compensateDataDto.getPlanPayDTOList();
            // 保单批次赔付信息
            List<UnionCompensatePolicyBatchPayDto> policyBatchPayDtoList = compensateDataDto.getPolicyBatchPayDTOList();
            // 整案基本信息
            UnionCompensateWholeCaseBaseDto caseBaseDto = compensateDataDto.getWholeCaseBaseDTO();
            // 处理支付信息
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
//                throw new IllegalArgumentException("报案号：" + registNo + "收款人信息不存在！");
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


            // 鼎和报案保单信息
            List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);

            // 请求参数中的赔付次数-prplcompensate.times
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

            // 案件类型 1-正常案件  3-代位求偿
            String caseType;
            if ("Y".equals(caseBaseDto.getIsAgentCase())) {
                caseType = "3";
            } else {
                caseType = "1";
            }
            // 计算书号集合，用于推送VAT
            Set<String> compensateNoSet = new HashSet<String>(2);
            // 平安保单支付信息
            Map<String, UnionCompensatePolicyBatchPayDto> PApolicyMap = new HashMap<String, UnionCompensatePolicyBatchPayDto>();
            Map<String, Long> paycustomMap = new HashMap<String, Long>();
            for (UnionCompensatePolicyBatchPayDto policyPatchPayDto : policyBatchPayDtoList) {
                // 平安赔案号
                String caseNo = policyPatchPayDto.getCaseNo();
                // 查询鼎和报案信息
                PrpLRegistVo registVo = registService.findRegistByRegistNo(registNo);
                if (registVo == null) {
                    throw new IllegalArgumentException("鼎和报案号：" + registNo + "平安赔案号：" + policyPatchPayDto.getCaseNo() + " 报案信息不存在！");
                }

                // 获取当前保单对应的险种
                String planCode = "";
                // 责任系数
                String dutyRate = "";
                // 责任免赔率
                String nopayRate = "";
                // 当前保单对应的责任赔付信息
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
                // 鼎和险种代码
                String riskCode = "";
                if (null != codeDictVo) {
                    riskCode = codeDictVo.getDhCodeCode();
                }
                logger.info("平安联盟中心-理算查询接口-------------------------------->riskcode={}",riskCode);
                if (StringUtils.isBlank(riskCode)) {
                    throw new IllegalArgumentException("平安险种代码planCode: " + planCode + " 无法匹配到鼎和险种代码！");
                }

                PrpLCMainVo prpLCMainVo = new PrpLCMainVo();
                for (PrpLCMainVo cmainVo : prpLCMainVoList) {
                    if (riskCode != null && riskCode.equals(cmainVo.getRiskCode())) {
                        prpLCMainVo = cmainVo;
                        break;
                    }
                }

                // 查询鼎和立案信息
                List<PrpLClaimVo> claimVoList = claimService.findprpLClaimVoListByRegistAndPolicyNo(registNo, prpLCMainVo.getPolicyNo(), "1");
                PrpLClaimVo claimVo = new PrpLClaimVo();
                if (claimVoList != null && claimVoList.size() > 0) {
                    claimVo = claimVoList.get(0);
                }
                logger.info("平安联盟中心-理算查询接口------------------------------>comCode={}",prpLCMainVo.getComCode());
                // 生成计算书号
                String compensateNo = billNoService.getCompensateNo(prpLCMainVo.getComCode(), riskCode);
                Map<String,List<Map<Long,String>>> kindCodeMap = new HashMap<>();//加入缓存的MAP集合
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
                        //判断实赔金额和损失金额是否大于0，如果大于0则为有效数据
                        if(lossItemVo.getSumRealPay().compareTo(BigDecimal.ZERO)>0){
                            lossItemVoList.add(lossItemVo);
                        }
                    }
                }

                // 理算费用数据
                List<PrpLChargeVo> chargeVoList = new ArrayList<PrpLChargeVo>();
                BigDecimal sumPaidFee = BigDecimal.ZERO;
                int chargeIndex = 0;
                for (UnionCompensatePlanDutyPayDto planDutyPayDto : curPlanDutyPayList) {
                    String kindCode = "";
                    if (Risk.DQZ.equals(riskCode)) {
                        kindCode = CodeConstants.KINDCODE.KINDCODE_BZ;
                    } else {
                        codeDictVo = pingAnDictService.getDictData("dutyCode", planDutyPayDto.getDutyCode());
                        // 鼎和险别代码
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
                        chargeVo.setChargeName("仲裁费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getArbitrateFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getArbitrateFee()));
                        BigDecimal preArbitrateFee =planPreFeePayMap.get("仲裁费_"+riskCode+"_"+kindCode);
                        if(preArbitrateFee != null){
                            chargeVo.setOffPreAmt(preArbitrateFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("诉讼费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getLawsuitFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getLawsuitFee()));
                        BigDecimal preLawsuitFee =planPreFeePayMap.get("诉讼费_"+riskCode+"_"+kindCode);
                        if(preLawsuitFee != null){
                            chargeVo.setOffPreAmt(preLawsuitFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("律师费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getLawyerFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getLawyerFee()));
                        BigDecimal preLawyerFee =planPreFeePayMap.get("律师费_"+riskCode+"_"+kindCode);
                        if(preLawyerFee != null){
                            chargeVo.setOffPreAmt(preLawyerFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("检验费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getCheckFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getCheckFee()));
                        BigDecimal preCheckFee =planPreFeePayMap.get("检验费_"+riskCode+"_"+kindCode);
                        if(preCheckFee != null){
                            chargeVo.setOffPreAmt(preCheckFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("执行费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getExecuteFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getExecuteFee()));
                        BigDecimal preExecuteFee =planPreFeePayMap.get("执行费_"+riskCode+"_"+kindCode);
                        if(preExecuteFee != null){
                            chargeVo.setOffPreAmt(preExecuteFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("公估费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getEvaluationFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getEvaluationFee()));
                        BigDecimal preEvaluationFee =planPreFeePayMap.get("公估费_"+riskCode+"_"+kindCode);
                        if(preEvaluationFee != null){
                            chargeVo.setOffPreAmt(preEvaluationFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("查勘费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getSurveyFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getSurveyFee()));
                        BigDecimal preSurveyFee =planPreFeePayMap.get("查勘费_"+riskCode+"_"+kindCode);
                        if(preSurveyFee != null){
                            chargeVo.setOffPreAmt(preSurveyFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("专家鉴定费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getMavinAppraisalFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getMavinAppraisalFee()));
                        BigDecimal preMavinAppraisalFee =planPreFeePayMap.get("专家鉴定费_"+riskCode+"_"+kindCode);
                        if(preMavinAppraisalFee != null){
                            chargeVo.setOffPreAmt(preMavinAppraisalFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("调查取证费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getInquiryEvidenceFee()));
                        BigDecimal preInquiryEvidenceFee =planPreFeePayMap.get("调查取证费_"+riskCode+"_"+kindCode);
                        if(preInquiryEvidenceFee != null){
                            chargeVo.setOffPreAmt(preInquiryEvidenceFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("咨询费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getConsultFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getConsultFee()));
                        BigDecimal preConsultFee =planPreFeePayMap.get("咨询费_"+riskCode+"_"+kindCode);
                        if(preConsultFee != null){
                            chargeVo.setOffPreAmt(preConsultFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("租车费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getCarRentalFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getCarRentalFee()));
                        BigDecimal preCarRentalFee =planPreFeePayMap.get("租车费_"+riskCode+"_"+kindCode);
                        if(preCarRentalFee != null){
                            chargeVo.setOffPreAmt(preCarRentalFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("检测费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getDetectFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getDetectFee()));
                        BigDecimal preDetectFee =planPreFeePayMap.get("检测费_"+riskCode+"_"+kindCode);
                        if(preDetectFee != null){
                            chargeVo.setOffPreAmt(preDetectFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("差旅费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getTravelFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getTravelFee()));
                        BigDecimal preTravelFee =planPreFeePayMap.get("差旅费_"+riskCode+"_"+kindCode);
                        if(preTravelFee != null){
                            chargeVo.setOffPreAmt(preTravelFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("查勘补助费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getSurveySubsidyFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getSurveySubsidyFee()));
                        BigDecimal preSurveySubsidyFee =planPreFeePayMap.get("查勘补助费_"+riskCode+"_"+kindCode);
                        if(preSurveySubsidyFee != null){
                            chargeVo.setOffPreAmt(preSurveySubsidyFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("公估费（外包）");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getEvaluationOutFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getEvaluationOutFee()));
                        BigDecimal preEvaluationOutFee =planPreFeePayMap.get("公估费（外包）_"+riskCode+"_"+kindCode);
                        if(preEvaluationOutFee != null){
                            chargeVo.setOffPreAmt(preEvaluationOutFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("咨询费（对公）");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getConsultPubFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getConsultPubFee()));
                        BigDecimal preConsultPubFee =planPreFeePayMap.get("咨询费（对公）_"+riskCode+"_"+kindCode);
                        if(preConsultPubFee != null){
                            chargeVo.setOffPreAmt(preConsultPubFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("公估取证费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getNotarialFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getNotarialFee()));
                        BigDecimal preNotarialFee =planPreFeePayMap.get("公估取证费_"+riskCode+"_"+kindCode);
                        if(preNotarialFee != null){
                            chargeVo.setOffPreAmt(preNotarialFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        chargeVo.setChargeName("前置调查费");
                        chargeVo.setSerialNo("" + chargeIndex);
                        chargeIndex++;
                        chargeVo.setFeeAmt(new BigDecimal(planDutyPayDto.getPreInvestigateFee()));
                        chargeVo.setFeeRealAmt(new BigDecimal(planDutyPayDto.getPreInvestigateFee()));
                        BigDecimal prePreInvestigateFee =planPreFeePayMap.get("前置调查费_"+riskCode+"_"+kindCode);
                        if(prePreInvestigateFee != null){
                            chargeVo.setOffPreAmt(prePreInvestigateFee);//应扣预付金额
                            chargeVo.setFeeRealAmt(chargeVo.getFeeAmt().subtract(chargeVo.getOffPreAmt()));//扣除预付金额后应付的费用
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
                        //如果为相同的赔案号则将该收款人插入到该计算书号下
                        if (caseNo.equals(resultDto.getCaseNo())) {
                            // 保存收款人信息
                            Long payeeid;
                            String accountNo = resultDto.getClientBankAccount();
                            if (paycustomMap.get(accountNo) != null) {
                                payeeid = paycustomMap.get(accountNo);
                            } else {
                                payeeid = savePaycustoms(registNo, resultDto, prpLCMainVo.getComCode());
                                paycustomMap.put(resultDto.getClientBankAccount(), payeeid);
                            }
                            // 封装支付信息
                            PrpLPaymentVo paymentVo = new PrpLPaymentVo();
                            paymentVo.setCompensateNo(compensateNo);
                            paymentVo.setOtherFlag("0");
                            paymentVo.setSumRealPay(resultDto.getPaymentAmount());
                            paymentVo.setPayStatus("2");
                            paymentVo.setCreateTime(new Date());
                            paymentVo.setUpdateTime(new Date());
                            // 收款人信息id
                            paymentVo.setPayeeId(payeeid);
                            paymentVo.setSerialNo("" + paymentIndex);
                            paymentIndex++;
                            // 1-代付 2-清付 3-自付 4-无责代赔
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
                // 当前理算节点
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
                    throw new IllegalArgumentException("报案号：" + registNo + " 无法找到该案件的理算节点数据！");
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
                // 代位主表数据 start
                PrpLSubrogationMainVo subrogationMainVo = new PrpLSubrogationMainVo();
                subrogationMainVo.setRegistNo(registNo);
                subrogationMainVo.setSubrogationFlag("0");
                subrogationMainVo.setValidFlag("1");
                subrogationMainVo.setCreateUser("AUTO");
                subrogationMainVo.setCreateTime(new Date());
                subrogationMainVo.setUpdateUser("AUTO");
                subrogationMainVo.setUpdateTime(new Date());
                subrogationService.saveSubrogationInfo(subrogationMainVo);
                // 代位主表数据  end
                PrpLCompensateVo prpLCompensateVo = compensateTaskService.findPrpLCompensateVoByPK(nextVo.getTaskInKey());
                compensateTaskService.submitCompeWfTaskVo(prpLCompensateVo, compensateTaskVo, nextVo, "true", userVo);
                // 理算提交后执行自动核赔
                Long uwNotionMainId = verifyClaimService.autoVerifyClaimEndCase(userVo, prpLCompensateVo);
                // 核赔提交结案
                verifyClaimService.autoVerifyClaimToFlowEndCase(userVo, prpLCompensateVo, uwNotionMainId);

                compensateNoSet.add(compensateNo);
            }
            resultBean.setCompensateNo(compensateNoSet);
        } catch (Exception e) {
            logger.info("平安联盟中心-理算/支付信息查询接口返回结果处理异常!", e);
            resultBean.fail("平安联盟中心-理算/支付信息查询接口返回结果处理异常！");
        }
        logger.info("平安联盟中心-理算查询接口-业务数据处理完成...报案号：{}", registNo);

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
        logger.info("开始存入缓存数据组装。。。。。。计算书号:{}",compensateNo);
        if(lossItemVos != null && lossItemVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLLossItemVo lossItemVo : lossItemVos) {
                logger.info("开始存入缓存。。。。。。计算书号:{},ID值为:{},kindCode值为：{}",compensateNo,lossItemVo.getId(),lossItemVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(lossItemVo.getId(),lossItemVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("lossItemVos",kindCodeList);
        }
        if(lossPersonVos != null && lossPersonVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLLossPersonVo lossPersonVo : lossPersonVos) {
                logger.info("开始存入缓存。。。。。。计算书号:{},ID值为:{},kindCode值为：{}",compensateNo,lossPersonVo.getId(),lossPersonVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(lossPersonVo.getId(),lossPersonVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("lossPersonVos",kindCodeList);
        }
        if(chargeVos != null && chargeVos.size()>0){
            List<Map<Long,String>> kindCodeList = new ArrayList<>();
            for (PrpLChargeVo chargeVo : chargeVos) {
                logger.info("开始存入缓存。。。。。。计算书号:{},ID值为:{},kindCode值为：{}",compensateNo,chargeVo.getId(),chargeVo.getKindCode());
                Map<Long,String> chargeMap = new HashMap<>();
                chargeMap.put(chargeVo.getId(),chargeVo.getKindCode());
                kindCodeList.add(chargeMap);
            }
            cacheMap.put("chargeVos",kindCodeList);
        }
        pingAnDictService.saveKindCodeToCatche(compensateNo,cacheMap);
        logger.info("结束存入缓存数据组装。。。。。。计算书号:{}",compensateNo);
    }

    /**
     * 封装理算查询接口请求参数
     *
     * @param requestDto   理算请求对象
     * @param allParamsDto 通知下发接口传给理赔的请求参数对象
     * @return 返回请求参数json
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
     * 封装支付信息查询接口请求参数
     *
     * @param requestDto   支付信息请求对象
     * @param allParamsDto 通知下发接口传给理赔的请求参数对象
     * @return 返回请求参数json
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
     * 数据校验
     * @param paymentDataDto 支付信息数据
     * @param compensateDataDto 理算信息数据
     * @throws Exception 数据异常
     */
    private void validData(UnionPaymentResponseDataDto paymentDataDto, UnionCompensateResponseDataDto compensateDataDto) throws Exception {
        // 保单赔付信息
        List<UnionCompensatePolicyPayDto> policyPayDtoList = compensateDataDto.getPolicyPayDTOList();
        // 责任赔付信息
        List<UnionCompensatePlanDutyPayDto> planDutyPayDtoList = compensateDataDto.getPlanDutyPayDTOList();
        // 险种赔付信息
        List<UnionCompensatePlanPayDto> planPayDtoList = compensateDataDto.getPlanPayDTOList();
        // 保单批次赔付信息
        List<UnionCompensatePolicyBatchPayDto> policyBatchPayDtoList = compensateDataDto.getPolicyBatchPayDTOList();
        // 整案基本信息
        UnionCompensateWholeCaseBaseDto caseBaseDto = compensateDataDto.getWholeCaseBaseDTO();
        // 支付信息
        List<UnionPaymentResultDto> paymentResultDtoList = paymentDataDto.getPaymentResultList();

        if (paymentResultDtoList == null || paymentResultDtoList.size() == 0) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-支付信息为空！");
        }
        if (policyPayDtoList == null || policyPayDtoList.size() == 0) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-保单赔付信息为空！");
        }
        if (planDutyPayDtoList == null || planDutyPayDtoList.size() == 0) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-责任赔付信息为空！");
        }
        if (planPayDtoList == null || planPayDtoList.size() == 0) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-险种赔付信息为空！");
        }
        if (policyBatchPayDtoList == null || policyBatchPayDtoList.size() == 0) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-保单批次赔付信息为空！");
        }
        if (caseBaseDto == null) {
            throw new IllegalArgumentException("平安联盟中心-理算信息处理-理算整案基本信息为空！");
        }
    }

    /**
     * 根据事故责任免赔率判断事故责任类型
     *
     * @param dutyRate 事故责任免赔率
     * @return 责任类型
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
     * 保存收款人信息
     * @param registNo 鼎和理赔报案号
     * @param paymentResultDto  平安收款人信息
     * @return 返回收款人paycustom.id
     * @throws Exception 收款人信息保存异常
     */
    private Long savePaycustoms(String registNo, UnionPaymentResultDto paymentResultDto, String comCode) throws Exception {
        // 如果当前报案号下已存在该账号，直接返回该账号id
        String accountNo = paymentResultDto.getClientBankAccount();
        Long payeeid = getPaycustomIdWithSameAccountNo(registNo, accountNo);
        if (null != payeeid) {
            return payeeid;
        }
        PrpLPayCustomVo payCustomVo = new PrpLPayCustomVo();

        payCustomVo.setRegistNo(registNo);
        payCustomVo.setPaicclaimNo(paymentResultDto.getCaseNo());
        payCustomVo.setCaseTimes(paymentResultDto.getCaseTimes());
        // 证件类型
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
        // 根据联行号获取联行号归属省市数据
        if (paymentResultDto.getClientBankCode() != null && paymentResultDto.getClientBankName() != null) {
            AccBankNameVo accBankNameVo = payCustomService.findBankInfoFromName(paymentResultDto.getClientBankName(),paymentResultDto.getClientBankCode());
            if (accBankNameVo != null) {
                payCustomVo.setBankName(accBankNameVo.getBankCode());//取银行大类代码
                payCustomVo.setBankType(accBankNameVo.getBankCode());
                payCustomVo.setProvinceCode(Long.parseLong(accBankNameVo.getProvinceCode()));//省代码
                payCustomVo.setProvince(accBankNameVo.getProvincial());//省名称
                payCustomVo.setCityCode(Long.parseLong(accBankNameVo.getCityCode()));//市代码
                payCustomVo.setCity(accBankNameVo.getCity());//市名称
            }
        }
        payCustomVo.setPriorityFlag("N");
        payCustomVo.setPurpose("平安联盟支付信息");
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
     * commit为前缀的方法会新开事务进行处理，这样可以保证在推送VAT数据的时候，理算数据已经落入数据库中
     * @param pingAnDataNoticeVo
     * @param resultBean
     * @return
     */
    public ResultBean commitCompensateData(PingAnDataNoticeVo pingAnDataNoticeVo, ResultBean resultBean){
        Gson gson = new Gson();
        //根据数据通知环节Code获取对应的枚举类
        PingAnDataTypeEnum dataTypeEnum = PingAnDataTypeEnum.getEnumByCode(pingAnDataNoticeVo.getPushType());
        if (dataTypeEnum == null) {
            resultBean = resultBean.fail("数据通知环节类型不存在");
        }

        try {
            //获取固定参数
            String siteCode = SpringProperties.getProperty("pingan_siteCode");//HS理赔系统分配给保险平台的接入编码
            String insuranceCompanyNo = SpringProperties.getProperty("pingan_insuranceCompanyNo");//保险公司
            String operatorUm = SpringProperties.getProperty("pingan_operatorUm");//操作员UM
            String compensateResultUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_08_url");//请求URL
            String paymentResultUrl = SpringProperties.getProperty("pingan_url") + SpringProperties.getProperty("pingan_08_01_url");//请求URL

            // 平安通知下发中的请求参数
            String paramObj = pingAnDataNoticeVo.getParamObj();
            UnionCompensateAllParamsDto allParamsDto = gson.fromJson(paramObj, UnionCompensateAllParamsDto.class);

            // 支付信息查询接口请求参数
            UnionPaymentRequestDto paymentRequestDto = new UnionPaymentRequestDto();
            paymentRequestDto.setInsuranceCompanyNo(insuranceCompanyNo);
            paymentRequestDto.setOperatorUm(operatorUm);
            String paymentRequestData = getPaymentRequestParams(paymentRequestDto, allParamsDto);

            //对请求报文进数字签名
            String paymentEncodeRequestData = Base64EncodedUtil.encode(paymentRequestData);
            String paymentsignature = PingAnMD5Utils.sign(siteCode + paymentEncodeRequestData);//数字签名
            //组装接口请求参数
            Map<String, String> paymentParam = new HashMap<String, String>();
            paymentParam.put("siteCode", siteCode);//HS理赔系统分配给保险平台的接入编码
            paymentParam.put("reqData", paymentEncodeRequestData);//请求数据报文，json格式，采用base64编码
            paymentParam.put("signature", paymentsignature);//数字签名
            String paymentJsonString = gson.toJson(paymentParam);
            //发起请求
            logger.info("{}接口请求参数-requestUrl={},明文reqData={},密文param={}", "理算支付信息通知", paymentResultUrl, paymentRequestData, paymentJsonString);
            String paymentResult = HttpClientJsonUtil.postUtf(paymentResultUrl, paymentJsonString, "UTF-8");

            // 理算查询接口请求参数
            UnionCompensateRequestDto compensateRequestDto = new UnionCompensateRequestDto();
            compensateRequestDto.setInsuranceCompanyNo(insuranceCompanyNo);
            compensateRequestDto.setOperatorUm(operatorUm);
            String compensateRequestData = getCompensateRequestParams(compensateRequestDto, allParamsDto);
            //对请求报文进数字签名
            String compensateEncodeRequestData = Base64EncodedUtil.encode(compensateRequestData);
            String compensatesignature = PingAnMD5Utils.sign(siteCode + compensateEncodeRequestData);//数字签名
            //组装接口请求参数
            Map<String, String> compensateParam = new HashMap<String, String>();
            compensateParam.put("siteCode", siteCode);//HS理赔系统分配给保险平台的接入编码
            compensateParam.put("reqData", compensateEncodeRequestData);//请求数据报文，json格式，采用base64编码
            compensateParam.put("signature", compensatesignature);//数字签名
            String compensateJsonString = gson.toJson(compensateParam);
            //发起请求
            logger.info("{}接口请求参数-requestUrl={},明文reqData={},密文param={}", "理算支付信息", compensateResultUrl, compensateRequestData, compensateEncodeRequestData);
            String compensateResult = HttpClientJsonUtil.postUtf(compensateResultUrl, compensateJsonString, "UTF-8");

            //解析返回报文
            JsonObject paymentObj = (JsonObject) new JsonParser().parse(paymentResult);
            String paymentResultCode = paymentObj.get("code").getAsString();//响应码
            String paymentMsg = paymentObj.get("msg").getAsString();//响应具体信息
            String paymentRespData = paymentObj.get("respData").getAsString();//响应数据，Base64编码，传给具体处理类需进行解码
            String paymentRespSignature = paymentObj.get("signature").getAsString();//数字签名

            JsonObject compensateObj = (JsonObject) new JsonParser().parse(compensateResult);
            String compensateResultCode = compensateObj.get("code").getAsString();//响应码
            String compensateMsg = compensateObj.get("msg").getAsString();//响应具体信息
            String compensateRespData = compensateObj.get("respData").getAsString();//响应数据，Base64编码，传给具体处理类需进行解码
            String compensateRespSignature = compensateObj.get("signature").getAsString();//数字签名

            //请求成功进行数字签名验证
            if ("200".equals(paymentResultCode) && "200".equals(compensateResultCode)) {
                //验证数字签名是否正确
                String paymentdata = paymentResultCode + paymentMsg + paymentRespData;
                Boolean paymentIsVerified = PingAnMD5Utils.verifySign(paymentdata, paymentRespSignature);
                String compensatedata = compensateResultCode + compensateMsg + compensateRespData;
                Boolean compensateIsVerified = PingAnMD5Utils.verifySign(compensatedata, compensateRespSignature);
                //验签通过后，获取具体业务实现类进行处理业务
                if (paymentIsVerified && compensateIsVerified) {
                    //获取内部报案号
                    PrpLRegistVo prpLRegistVo = registService.findRegistByPaicReportNo(pingAnDataNoticeVo.getReportNo());
                    String registNo = prpLRegistVo != null ? prpLRegistVo.getRegistNo() : null;
                    resultBean = handleResponseData(registNo, pingAnDataNoticeVo, Base64EncodedUtil.decode(paymentRespData), Base64EncodedUtil.decode(compensateRespData));
                } else {
                    resultBean = resultBean.fail("接口返回报文验签失败");
                    logger.error("{}-{}接口返回报文验签失败,id={},reportNo={}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(), pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo());
                }
            } else {
                resultBean = resultBean.fail(compensateResultCode + ":" + compensateMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}-{}接口请求失败,id={},reportNo={},错误信息：{}", dataTypeEnum.getCode(), dataTypeEnum.getDesc(), pingAnDataNoticeVo.getId(), pingAnDataNoticeVo.getReportNo(), ExceptionUtils.getStackTrace(e));
            resultBean = resultBean.fail(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获取已经存在的收款人账号的id
     * @param registNo 报案号
     * @param accountNo 收款人账号
     * @return 收款人信息
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
     * 证件类型转换
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
