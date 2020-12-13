package ins.sino.claimcar.newpayment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.newpayment.vo.PaymentConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AccountInfoService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.vo.PrpDAccRollBackAccountVo;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.newpayment.vo.AccRollbackAccountMainDto;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import ins.sino.claimcar.pinganUnion.service.PingAnPayCallBackService;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestListDto;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestParamDto;
import ins.sino.claimcar.platform.service.SendPaymentToPlatformService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.util.*;

/**
 * 收付退票数据处理
 *
 * @author maofengning
 * @date 2020/5/16 14:16
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("paymentRefundService")
public class PaymentRefundServiceImpl implements PaymentRefundService {

    private static Logger logger = LoggerFactory.getLogger(PaymentRefundServiceImpl.class);

    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private SendPaymentToPlatformService sendPaymentToPlatformService;
    @Autowired
    private CompensateService compensateService;
    @Autowired
    private PadPayService padPayService;
    @Autowired
    private AssessorService assessorService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private AcheckService acheckService;
    @Autowired
    private MsgModelService msgModelService;
    @Autowired
    private AccountInfoService accountInfoService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private PingAnPayCallBackService pingAnPayCallBackService;
    @Override
    public ResponseDto paymentRefund(String json) throws Exception {
        logger.info("\n收付退票通知报文： " + json + "\n");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
        responseDto.setResponseCode("收付退票处理失败！");
        Gson gson = new Gson();
        if (StringUtils.isBlank(json)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付退票通知报文为空，退票失败！");

            return responseDto;
        }
        // 将报文解析成理赔对象
        AccRollbackAccountMainDto rollbackAccountDto = null;
        try {
            rollbackAccountDto = gson.fromJson(json, AccRollbackAccountMainDto.class);
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付退票通知报文解析异常，退票失败！" + e.getMessage());
        }
        if (rollbackAccountDto == null) {
            return responseDto;
        }

        // 收付退票理赔报文相关字段校验，同时去除退票时间中的特殊字符
        String errorMessage = rollbackAccountDataValid(rollbackAccountDto);
        if (StringUtils.isNotBlank(errorMessage)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(errorMessage);

            return responseDto;
        }

        errorMessage = validateDuplicateRefund(rollbackAccountDto);
        if (StringUtils.isNotBlank(errorMessage)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(errorMessage);

            return responseDto;
        }

        try {
            this.handleRollBackAccountInfo(rollbackAccountDto);
            responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
            responseDto.setErrorMessage("收付退票成功！");
        } catch (Exception e) {
            logger.info("计算书号：" + rollbackAccountDto.getCertiNo() + "收付退票理赔处理异常!"  + " 退票报文：{}\n 异常信息：{}", json, e);
            throw e;
        }
        return responseDto;
    }

    /**
     * 收付退票报文数据处理
     *
     * @param rollbackAccountDto 收付退票报文数据
     * @throws Exception 异常信息
     */
    private void handleRollBackAccountInfo(AccRollbackAccountMainDto rollbackAccountDto) throws Exception {
        Map<String,String> resultMap = checkPingAnCase(rollbackAccountDto.getCertiNo());
        String idClmPaymentResult = resultMap.get("idClmPaymentResult");
        // 保存退票信息到
        logger.info("保存计算书：{} 的退票信息开始...", rollbackAccountDto.getCertiNo());
        saveRollBackAccount(rollbackAccountDto);
        logger.info("保存计算书：{} 的退票信息完成！", rollbackAccountDto.getCertiNo());
        //平安支付主键不为空
        if(!StringUtils.isBlank(idClmPaymentResult)){
            //平安支付结果回调列表
            List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
            if (rollbackAccountDto.getCertiNo().startsWith("P") || rollbackAccountDto.getCertiNo().startsWith("G") || rollbackAccountDto.getCertiNo().startsWith("J")) {
                // 结算单号退票
                if (PaymentConstants.PAYREASON_P6K.equals(rollbackAccountDto.getPayRefReason())) {
                    updatePadPayStatusByJSP(rollbackAccountDto);
                } else if (PaymentConstants.PAYREASON_P50.equals(rollbackAccountDto.getPayRefReason())) {
                    updatePrePayStatusByJSP(rollbackAccountDto);
                } else {
                    updateCompensateByJSP(rollbackAccountDto);
                }
            } else {
                if (rollbackAccountDto.getCertiNo().startsWith(CodeConstants.CompensateType.padpay_type)) {
                    // 垫付计算书
                    updatePadPayStatus(rollbackAccountDto);
                } else if (rollbackAccountDto.getCertiNo().startsWith(CodeConstants.CompensateType.prepay_type)) {
                    updatePrePayStatus(rollbackAccountDto);
                } else {
                    updateCompensate(rollbackAccountDto);
                }
            }
            addPingAnCallbackData(rollbackAccountDto,callbackRequestListDtos,idClmPaymentResult);
            if (!callbackRequestListDtos.isEmpty()) {
                Map<String, Object> callBackDataMap = new HashMap<>();
                UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
                unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
                String registNo = resultMap.get("registNo");
                callBackDataMap.put(registNo, unionPayCallbackRequestParamDto);
                //平安支付结果回调
                logger.info("预付收付回写平安退票结果开始！ 系统时间" + new Date());
                pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
                logger.info("预付收付回写平安退票结果结束！ 系统时间" + new Date());
            }
        }else{
            // 收付退票查勘费
            if (PaymentConstants.PAYREASON_P62.equals(rollbackAccountDto.getPayRefReason())) {
                updateCheckFeeStatus(rollbackAccountDto.getCertiNo());
            }
            // 收付退票公估费
            if (PaymentConstants.PAYREASON_P67.equals(rollbackAccountDto.getPayRefReason())) {
                updateAssessorStatus(rollbackAccountDto.getCertiNo());
            }

            if (!PaymentConstants.PAYREASON_P62.equals(rollbackAccountDto.getPayRefReason())
                    && !PaymentConstants.PAYREASON_P67.equals(rollbackAccountDto.getPayRefReason())) {
                if (rollbackAccountDto.getCertiNo().startsWith("P") || rollbackAccountDto.getCertiNo().startsWith("G") || rollbackAccountDto.getCertiNo().startsWith("J")) {
                    // 结算单号退票
                    if (PaymentConstants.PAYREASON_P6K.equals(rollbackAccountDto.getPayRefReason())) {
                        updatePadPayStatusByJSP(rollbackAccountDto);
                    } else if (PaymentConstants.PAYREASON_P50.equals(rollbackAccountDto.getPayRefReason())) {
                        updatePrePayStatusByJSP(rollbackAccountDto);
                    } else {
                        updateCompensateByJSP(rollbackAccountDto);
                    }
                } else {
                    if (rollbackAccountDto.getCertiNo().startsWith(CodeConstants.CompensateType.padpay_type)) {
                        // 垫付计算书
                        updatePadPayStatus(rollbackAccountDto);
                    } else if (rollbackAccountDto.getCertiNo().startsWith(CodeConstants.CompensateType.prepay_type)) {
                        updatePrePayStatus(rollbackAccountDto);
                    } else {
                        updateCompensate(rollbackAccountDto);
                    }
                }
            }
        }
    }

    /**
     * 保存退票信息
     * @date 2020年5月13日10:31:58
     */
    private void saveRollBackAccount(AccRollbackAccountMainDto rollbackAccountDto) throws Exception {
        try {
            PrpDAccRollBackAccountVo accRollBackVo = new PrpDAccRollBackAccountVo();
            accRollBackVo.setCertiNo(rollbackAccountDto.getCertiNo());
            accRollBackVo.setErrorMessage(rollbackAccountDto.getErrorMessage());
            accRollBackVo.setAccountName(rollbackAccountDto.getAccountName());
            accRollBackVo.setBankCode(rollbackAccountDto.getBankCode());
            accRollBackVo.setRollBackTime(DateUtils.strToDate(rollbackAccountDto.getRollBackTime()));
            accRollBackVo.setStatus(rollbackAccountDto.getStatus());
            accRollBackVo.setRollbackCode(rollbackAccountDto.getRollBackCode());
            accRollBackVo.setErrorType(rollbackAccountDto.getErrorType());
            accRollBackVo.setModifyType(rollbackAccountDto.getModifyType());
            accRollBackVo.setAccountCode(rollbackAccountDto.getAccountCode());
            accRollBackVo.setIsAutoPay(rollbackAccountDto.getIsAutoPay());
            accRollBackVo.setPayType(rollbackAccountDto.getPayRefReason());
            accRollBackVo.setSerialNo(rollbackAccountDto.getSerialNo().toString());
            accRollBackVo.setAccountId(rollbackAccountDto.getPayeeId());
            accountInfoService.saveAccRollBackAccount(accRollBackVo);
        } catch (Exception e) {
            logger.info("计算书号/结算单号: " + rollbackAccountDto.getCertiNo() + " 退票保存失败！", e);
            throw e;
        }
    }

    /**
     * 退票更新查勘费状态
     * @param certiNo 计算书号
     */
    private void updateCheckFeeStatus(String certiNo) throws Exception {
        if (certiNo != null) {
            try {
                logger.info("计算书号：{} 收付退票查勘费状态回写开始...", certiNo);
                List<PrpLCheckFeeVo> checkFees = acheckService.findPrpLCheckFeeVoByBussNo(certiNo);
                if (checkFees != null && checkFees.size() > 0) {
                    for (PrpLCheckFeeVo checkFeeVo : checkFees) {
                        checkFeeVo.setTaskStatus("2");
                        acheckService.updateCheckFee(checkFeeVo);
                    }
                }
                logger.info("计算书号：{} 收付退票查勘费状态回写完成！", certiNo);
            } catch (Exception e) {
                logger.info("计算书号：" + certiNo + "收付退票查勘费状态回写异常！异常信息：{}", e);
                throw e;
            }
        }
    }

    /**
     * 退票更新公估费状态
     * @param certiNo 计算书号
     */
    private void updateAssessorStatus(String certiNo) throws Exception {
        if (certiNo != null) {
            try {
                logger.info("计算书号：{} 收付退票公估费状态回写开始...", certiNo);
                List<PrpLAssessorFeeVo> asseorFeeVos = assessorService.findPrpLAssessorFeeVoByCompensateNoOrEndNo(certiNo);
                if (asseorFeeVos != null && asseorFeeVos.size() > 0) {
                    for (PrpLAssessorFeeVo asseorFeeVo : asseorFeeVos) {
                        asseorFeeVo.setTaskStatus("2");
                        assessorService.updateAssessorFee(asseorFeeVo);
                    }
                }
                logger.info("计算书号：{} 收付退票公估费状态回写完成！", certiNo);
            } catch (Exception e) {
                logger.info("计算书号：" + certiNo + "收付退票公估费状态回写异常！异常信息：{}", e);
                throw e;
            }
        }
    }

    /**
     * 收付退票回写垫付相关数据（结算单号）
     * @param rollbackAccountMainDto 退票数据对象
     */
    private void updatePadPayStatusByJSP (AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            try {
                logger.info("计算书号：{} 收付退票垫付状态回写updatePadPayStatusByJSP开始...", rollbackAccountMainDto.getCertiNo());
                List<PrpLPadPayMainVo> padPayVoList = padPayService.findPadPayMainBySettleNo(rollbackAccountMainDto.getCertiNo());
                // 垫付计算书号
                if (padPayVoList != null && padPayVoList.size() > 0) {
                    for (PrpLPadPayMainVo padPayMainVo : padPayVoList) {
                        Map<String, Long> payIdMap = new HashMap<String, Long>(4);
                        List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(padPayMainVo.getRegistNo());
                        if (customList != null && !customList.isEmpty()) {
                            for (PrpLPayCustomVo customVo : customList) {
                                payIdMap.put(customVo.getAccountNo(), customVo.getId());
                            }
                        }

                        //支付轨迹
                        for (PrpLPadPayPersonVo padPayPersonVo : padPayMainVo.getPrpLPadPayPersons()) {
                            //通过收付的账号 判断是否支付
                            Long payeeId = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                            if (padPayPersonVo.getPayeeId().equals(payeeId)) {
                                if (BigDecimal.ZERO.compareTo(padPayPersonVo.getCostSum()) == -1) {
                                    padPayPersonVo.setPayStatus("3");
                                    this.savePrplPayHis(padPayMainVo.getClaimNo(), padPayMainVo.getCompensateNo(), padPayPersonVo.getId(), "3", "D", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                } else {
                                    this.savePrplPayHis(padPayMainVo.getClaimNo(), padPayMainVo.getCompensateNo(), padPayPersonVo.getId(), "0", "D", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                }
                            }
                        }
                        padPayService.save(padPayMainVo, null, null);
                    }
                }
                logger.info("计算书号：{} 收付退票垫付状态回写updatePadPayStatusByJSP完成！", rollbackAccountMainDto.getCertiNo());
            } catch (Exception e) {
                logger.info("计算书号：{} 收付退票垫付状态回写异常！异常信息：{}", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付退票垫付数据回写（计算书号）
     * @param rollbackAccountMainDto 收付退票数据对象
     */
    private void updatePadPayStatus(AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            try {
                logger.info("计算书号：{} 收付退票垫付状态回写updatePadPayStatus开始...", rollbackAccountMainDto.getCertiNo());
                PrpLPadPayMainVo padPayVo = padPayService.findPadPayMainByCompNo(rollbackAccountMainDto.getCertiNo());
                if (padPayVo != null) {
                    Map<String, Long> payIdMap = new HashMap<String, Long>(4);
                    List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(padPayVo.getRegistNo());
                    if (customList != null && !customList.isEmpty()) {
                        for (PrpLPayCustomVo customVo : customList) {
                            payIdMap.put(customVo.getAccountNo(), customVo.getId());
                        }
                    }
                    //支付轨迹
                    for (PrpLPadPayPersonVo PadPayPersonVo : padPayVo.getPrpLPadPayPersons()) {
                        //通过收付的账号id 判断是否支付
                        Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                        if (PadPayPersonVo.getPayeeId().equals(payeeid)) {
                            if (BigDecimal.ZERO.compareTo(PadPayPersonVo.getCostSum()) == -1) {
                                PadPayPersonVo.setPayStatus("3");
                                this.savePrplPayHis(padPayVo.getClaimNo(), padPayVo.getCompensateNo(), PadPayPersonVo.getId(), "3", "D", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            } else {
                                this.savePrplPayHis(padPayVo.getClaimNo(), padPayVo.getCompensateNo(), PadPayPersonVo.getId(), "0", "D", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            }
                        }
                    }
                    padPayService.save(padPayVo, null, null);
                    logger.info("计算书号：{} 收付退票垫付状态回写updatePadPayStatus完成", rollbackAccountMainDto.getCertiNo());
                }
            } catch (Exception e) {
                logger.info("计算书号：{} 收付退票垫付状态回写updatePadPayStatus异常！异常信息：{}", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付退票更新预付相关数据（结算单号）
     * @param rollbackAccountMainDto 收付退票报文对象
     */
    private void updatePrePayStatusByJSP (AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            logger.info("预付计算书号：{} 收付退票回写预付相关数据updatePrePayStatusByJSP开始...", rollbackAccountMainDto.getCertiNo());
            try {
                List<PrpLPrePayVo> prePayVoList = compensateTaskService.findPrpLPrePayVoBySettleNo(rollbackAccountMainDto.getCertiNo());
                for (PrpLPrePayVo prePayVo : prePayVoList) {
                    //通过收付的账号 判断是否支付
                    PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(prePayVo.getCompensateNo());
                    Map<String,Long> payIdMap = new HashMap<String, Long>(4);
                    List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                    if(customList!=null && !customList.isEmpty()){
                        for(PrpLPayCustomVo customVo : customList){
                            payIdMap.put(customVo.getAccountNo(), customVo.getId());
                        }
                    }
                    Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                    if (PaymentConstants.FEETYPE_P.equals(prePayVo.getFeeType())) {
                        // 预付赔款
                        if (prePayVo.getPayeeId().equals(payeeid)) {
                            if (BigDecimal.ZERO.compareTo(prePayVo.getPayAmt()) == -1) {
                                prePayVo.setPayStatus("3");
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "3", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                compensateService.updatePrpLPrePay(prePayVo, "3");
                            } else {
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "0", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            }
                        }
                    } else {
                        // 预付费用
                        Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                        SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayVo.getChargeCode());
                        if (prePayVo.getPayeeId().equals(payeeid) && rollbackAccountMainDto.getPayRefReason().equals(sysCodeDictVo.getProperty1())) {
                            if (BigDecimal.ZERO.compareTo(prePayVo.getPayAmt()) == -1) {
                                prePayVo.setPayStatus("3");
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "3", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                compensateService.updatePrpLPrePay(prePayVo, "3");
                            } else {
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayVo.getCompensateNo(), prePayVo.getId(), "0", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            }
                        }
                    }
                }
                logger.info("预付计算书号：{} 收付退票回写预付相关数据updatePrePayStatusByJSP完成！", rollbackAccountMainDto.getCertiNo());
            } catch (Exception e) {
                logger.info("预付计算书号：{} 收付退票回写预付相关数据异常！异常信息：{}", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付退票预付数据更新（计算书号）
     * @param rollbackAccountMainDto 收付退票数据对象
     */
    private void updatePrePayStatus(AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            try {
                logger.info("预付计算书号：{} 收付退票回写预付相关数据updatePrePayStatus开始...", rollbackAccountMainDto.getCertiNo());
                Map<String, Long> payIdMap = new HashMap<String, Long>(4);
                PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(rollbackAccountMainDto.getCertiNo());
                List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                if(customList!=null && !customList.isEmpty()){
                    for(PrpLPayCustomVo customVo : customList){
                        payIdMap.put(customVo.getAccountNo(), customVo.getId());
                    }
                }
                List<PrpLPrePayVo> prePayVoList = compensateTaskService.queryPrePay(rollbackAccountMainDto.getCertiNo());
                for (PrpLPrePayVo prePayFVo : prePayVoList) {
                    // 通过收付的账号 判断是否支付
                    Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                    if (PaymentConstants.FEETYPE_P.equals(prePayFVo.getFeeType())) {
                        if (prePayFVo.getPayeeId().equals(payeeid)) {
                            if (BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt()) == -1) {
                                prePayFVo.setPayStatus("3");
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayFVo.getCompensateNo(), prePayFVo.getId(), "3", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            } else {
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayFVo.getCompensateNo(), prePayFVo.getId(), "0", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            }
                        }
                    } else {
                        Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                        SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayFVo.getChargeCode());
                        if (prePayFVo.getPayeeId().equals(payeeid) && rollbackAccountMainDto.getPayRefReason().equals(sysCodeDictVo.getProperty1())) {
                            if (BigDecimal.ZERO.compareTo(prePayFVo.getPayAmt()) == -1) {
                                prePayFVo.setPayStatus("3");
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayFVo.getCompensateNo(), prePayFVo.getId(), "3", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            } else {
                                this.savePrplPayHis(compensateVo.getClaimNo(), prePayFVo.getCompensateNo(), prePayFVo.getId(), "0", "Y", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                            }
                        }
                    }
                }
                compensateService.writeBackPay(prePayVoList, rollbackAccountMainDto.getCertiNo());
                logger.info("预付计算书号：{} 收付退票回写预付相关数据updatePrePayStatus完成！", rollbackAccountMainDto.getCertiNo());
            } catch (Exception e) {
                logger.info("预付计算书号：{} 收付退票回写预付相关数据updatePrePayStatus异常！异常信息：{}", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 结算单号下更新赔款信息
     * @param rollbackAccountMainDto 收付退票数据对象
     */
    private void updateCompensateByJSP(AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            try {
                logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensateByJSP开始...", rollbackAccountMainDto.getCertiNo());
                // 赔款支付信息
                List<PrpLPaymentVo> prpLPaymentVoList = compensateTaskService.findPrpLPaymentVoBySettleNo(rollbackAccountMainDto.getCertiNo());
                // 费用支付信息
                List<PrpLChargeVo> prpLChargeVoList = compensateTaskService.findPrpLChargeVoBySettleNo(rollbackAccountMainDto.getCertiNo());

                PrpLCompensateVo compensateVo = new PrpLCompensateVo();
                Map<String, PrpLCompensateVo> compensateVoMap = new HashMap<String, PrpLCompensateVo>();
                if (prpLPaymentVoList != null && prpLPaymentVoList.size() > 0) {
                    List<PrpLCompensateVo> prpLCompensateVoList = compensateTaskService.findCompensateBySettleNo(rollbackAccountMainDto.getCertiNo(), "P");
                    if (prpLCompensateVoList != null && prpLCompensateVoList.size() > 0) {
                        for (PrpLCompensateVo vo : prpLCompensateVoList) {
                            // 排除相同的理算主表数据
                            if (!compensateVoMap.containsKey(vo.getCompensateNo())) {
                                compensateVoMap.put(vo.getCompensateNo(), vo);
                            }
                        }
                    }
                }
                if (prpLChargeVoList != null && prpLChargeVoList.size() > 0) {
                    List<PrpLCompensateVo> prpLCompensateVoList = compensateTaskService.findCompensateBySettleNo(rollbackAccountMainDto.getCertiNo(), "F");
                    if (prpLCompensateVoList != null && prpLCompensateVoList.size() > 0) {
                        for (PrpLCompensateVo vo : prpLCompensateVoList) {
                            if (!compensateVoMap.containsKey(vo.getCompensateNo())) {
                                compensateVoMap.put(vo.getCompensateNo(), vo);
                            }
                        }
                    }
                }

                if (!compensateVoMap.isEmpty()) {
                    Set<String> keSet = compensateVoMap.keySet();
                    for (String compensateNo : keSet) {
                        compensateVo = compensateVoMap.get(compensateNo);
                        Map<String, Long> payIdMap = new HashMap<String, Long>();
                        List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                        if (customList != null && !customList.isEmpty()) {
                            for (PrpLPayCustomVo customVo : customList) {
                                payIdMap.put(customVo.getAccountNo(), customVo.getId());
                            }
                        }

                        boolean payFlags = false;
                        boolean feeFlags = false;
                        if (compensateVo.getPrpLPayments() != null && compensateVo.getPrpLPayments().size() > 0) {
                            payFlags = true;
                        }
                        if (compensateVo.getPrpLCharges() != null && compensateVo.getPrpLCharges().size() > 0) {
                            feeFlags = true;
                        }
                        if (payFlags || feeFlags) {
                            if (PaymentConstants.PAYREASON_P60.equals(rollbackAccountMainDto.getPayRefReason())
                                    || PaymentConstants.PAYREASON_P6B.equals(rollbackAccountMainDto.getPayRefReason())
                                    || PaymentConstants.PAYREASON_P6D.equals(rollbackAccountMainDto.getPayRefReason())) {
                                // 赔款
                                for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                                    //通过收付的账号id 判断是否支付
                                    Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                                    if (paymentVo.getPayeeId().equals(payeeid)) {
                                        if (BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay()) == -1) {
                                            paymentVo.setPayStatus("3");
                                            compensateService.updatePrpLPaymentVo(paymentVo);
                                            this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "3", "P", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                        } else {
                                            this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "0", "P", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                        }
                                    }
                                }
                            } else {
                                for (PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()) {
                                    //通过收付的账号id 判断是否支付
                                    Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                                    SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
                                    Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                                    if (chargeVo.getPayeeId().equals(payeeid) && rollbackAccountMainDto.getPayRefReason().equals(sysCodeDictVo.getProperty2())) {
                                        if (BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt()) == -1) {
                                            chargeVo.setPayStatus("3");
                                            compensateService.updatePrpLCharges(chargeVo);
                                            this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "3", "F", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                        } else {
                                            this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "0", "F", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                        }
                                    }
                                }
                            }
                            compensateTaskService.paymentWriteBackCompVo(compensateVo);
                        }
                    }
                }
                logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensateByJSP完成！", rollbackAccountMainDto.getCertiNo());
            } catch (Exception e) {
                logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensateByJSP异常！异常信息：", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    private void updateCompensate(AccRollbackAccountMainDto rollbackAccountMainDto) throws Exception {
        if (rollbackAccountMainDto != null) {
            try {
                logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensate开始...", rollbackAccountMainDto.getCertiNo());
                PrpLCompensateVo compensateVo = compensateTaskService.findPrpLCompensateVoByPK(rollbackAccountMainDto.getCertiNo());
                List<PrpLPayCustomVo> customList = managerService.findPayCustomVoByRegistNo(compensateVo.getRegistNo());
                Map<String, Long> payIdMap = new HashMap<String, Long>(4);
                if (customList != null && !customList.isEmpty()) {
                    for (PrpLPayCustomVo customVo : customList) {
                        payIdMap.put(customVo.getAccountNo(), customVo.getId());
                    }
                }
                boolean payFlags = false;
                boolean feeFlags = false;
                if (compensateVo.getPrpLPayments() != null && compensateVo.getPrpLPayments().size() > 0) {
                    payFlags = true;
                }
                if (compensateVo.getPrpLCharges() != null && compensateVo.getPrpLCharges().size() > 0) {
                    feeFlags = true;
                }
                if ((!rollbackAccountMainDto.getCertiNo().startsWith("Y")) && (payFlags || feeFlags)) {
                    // 区分费用跟赔款
                    if (PaymentConstants.PAYREASON_P60.equals(rollbackAccountMainDto.getPayRefReason())
                            || PaymentConstants.PAYREASON_P6B.equals(rollbackAccountMainDto.getPayRefReason())
                            || PaymentConstants.PAYREASON_P6D.equals(rollbackAccountMainDto.getPayRefReason())) {
                        // 赔款
                        for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                            //通过收付的账号id 判断是否支付
                            Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                            if (paymentVo.getPayeeId().equals(payeeid)) {
                                if (BigDecimal.ZERO.compareTo(paymentVo.getSumRealPay()) == -1) {
                                    paymentVo.setPayStatus("3");
                                    compensateService.updatePrpLPaymentVo(paymentVo);
                                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "3", "P", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                } else {
                                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "0", "P", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                }
                            }
                        }
                    } else {
                        for (PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()) {
                            //通过收付的账号id 判断是否支付
                            Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                            SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
                            Long payeeid = payIdMap.get(rollbackAccountMainDto.getAccountCode());
                            if (chargeVo.getPayeeId().equals(payeeid) && rollbackAccountMainDto.getPayRefReason().equals(sysCodeDictVo.getProperty2())) {
                                if (BigDecimal.ZERO.compareTo(chargeVo.getFeeRealAmt()) == -1) {
                                    chargeVo.setPayStatus("3");
                                    compensateService.updatePrpLCharges(chargeVo);
                                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "3", "F", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                } else {
                                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "0", "F", DateUtils.strToDate(rollbackAccountMainDto.getRollBackTime()));
                                }
                            }
                        }
                    }
                    compensateTaskService.paymentWriteBackCompVo(compensateVo);
                    logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensate完成！", rollbackAccountMainDto.getCertiNo());
                }
            } catch (Exception e) {
                logger.info("计算书号：{} 收付退票回写理赔赔款或费用相关数据updateCompensate异常！异常信息：", rollbackAccountMainDto.getCertiNo(), e);
                throw e;
            }
        }
    }

    public void savePrplPayHis(String claimNo, String compensateNo, Long id, String flags, String hisType, Date payTime) {
        compensateService.savePrplPayHis(claimNo, compensateNo, id, flags, hisType, payTime);
    }

    /**
     * 通过计算书号更新短信状态
     *
     * @param compensateNo 计算书号
     */
    private void updateSmsStatus(String compensateNo) {
        PrpLCompensateVo compensateVo = compensateService.findCompByPK(compensateNo);
        try {
            List<PrpsmsMessageVo> prpsmsMessageList = msgModelService.findPrpSmsMessageByBusinessNo(compensateVo.getRegistNo());
            if (prpsmsMessageList != null && prpsmsMessageList.size() > 0) {
                for (PrpsmsMessageVo vo : prpsmsMessageList) {
                    if (vo.getSendNodecode() != null && FlowNode.EndCas.name().equals(vo.getSendNodecode())) {
                        vo.setStatus("1");
                        msgModelService.saveorUpdatePrpSmsMessage(vo);
                    }
                }
            }
        } catch (Exception e) {
            String msg = "";
            if (compensateVo == null) {
                msg = "计算书号：" + compensateNo;
            } else {
                msg = "报案号：" + compensateVo.getRegistNo();
            }
            logger.info(msg + " 收付退票理赔系统更新结案短信状态异常！", e);
        }
    }

    /**
     * 收付退票必录字段校验
     *
     * @param rollbackDto 收付退票报文对象
     * @return 返回相关字段校验结果
     */
    private String rollbackAccountDataValid(AccRollbackAccountMainDto rollbackDto) {
        String respMessage = "";
        if (rollbackDto != null) {
            if (StringUtils.isBlank(rollbackDto.getCertiNo())) {
                respMessage = "收付退票业务号为空！";
            }
            if (rollbackDto.getSerialNo() == null) {
                respMessage = "收付退票序号为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getPayRefReason())) {
                respMessage = "收付退票收付原因为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getErrorMessage())) {
                respMessage = "收付退票退票原因为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getBankName())) {
                respMessage = "收付退票开户银行名称为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getAccountName())) {
                respMessage = "收付退票账户名称为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getBankCode())) {
                respMessage = "收付退票银行代码为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getRollBackTime())) {
                respMessage = "收付退票退票时间为空！";
            } else {
                rollbackDto.setRollBackTime(rollbackDto.getRollBackTime().replace("\u00A0", " "));
            }
            if (StringUtils.isBlank(rollbackDto.getStatus())) {
                respMessage = "收付退票状态码为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getIsAutoPay())) {
                respMessage = "收付退票送资金标识为空！";
            }
            if (StringUtils.isBlank(rollbackDto.getPayeeId())) {
                respMessage = "收付退票收款人ID为空！";
            }
        }

        return respMessage;
    }

    /**
     * 校验是否重复退票
     * @param rollbackDto 收付退票数据对象
     * @return 校验结果
     */
    private String validateDuplicateRefund(AccRollbackAccountMainDto rollbackDto) {
        String errorMessage = "";
        PrpDAccRollBackAccountVo rollBackAccountVo =  accountInfoService.findRollBackBySerialNo(rollbackDto.getCertiNo(), rollbackDto.getSerialNo().toString(), rollbackDto.getPayRefReason());
        if ("2".equals(rollBackAccountVo.getStatus())) {
            errorMessage = "计算书：" + rollbackDto.getCertiNo() + " 序号："+ rollbackDto.getSerialNo() +" 已存在未处理的退票任务，请勿重复退票！";
        }

        return errorMessage;
    }

    /**
     * 用于取出平安案件需要的相关信息
     * @param ceritNo
     * @return
     */
    private Map<String,String> checkPingAnCase(String ceritNo){
        Map<String,String> resultMap = new HashMap<String,String>();
        String idClmPaymentResult= "";
        String registNo = "";
        if (ceritNo.startsWith("P") || ceritNo.startsWith("G") || ceritNo.startsWith("J")) {
            List<PrpLPadPayMainVo> padPayVoList = padPayService.findPadPayMainBySettleNo(ceritNo);
            if(padPayVoList != null && padPayVoList.size()>0){//垫付
                for (PrpLPadPayMainVo prpLPadPayMainVo : padPayVoList ) {
                    List<PrpLPadPayPersonVo> prpLPadPayPersonVoList= prpLPadPayMainVo.getPrpLPadPayPersons();
                    if(prpLPadPayPersonVoList != null && prpLPadPayPersonVoList.size()>0){
                        for (PrpLPadPayPersonVo prpLPadPayPersonVo : prpLPadPayPersonVoList) {
                            if(!StringUtils.isBlank(prpLPadPayPersonVo.getIdClmPaymentResult())){
                                idClmPaymentResult = prpLPadPayPersonVo.getIdClmPaymentResult();
                                registNo = prpLPadPayMainVo.getRegistNo();
                                break;
                            }
                        }
                    }
                }
            }else{
                List<PrpLPrePayVo> prePayVoList = compensateTaskService.findPrpLPrePayVoBySettleNo(ceritNo);
                if(prePayVoList != null && prePayVoList.size()>0){//预付
                    for (PrpLPrePayVo prpLPrePayVo : prePayVoList) {
                        //判断是否有通道号 有的话则为平安案件
                        if(!StringUtils.isBlank(prpLPrePayVo.getIdClmPaymentResult())){
                            idClmPaymentResult = prpLPrePayVo.getIdClmPaymentResult();
                            PrpLCompensateVo compensateVo = compensateService.findCompByPK(prpLPrePayVo.getCompensateNo());
                            if(compensateVo != null ){
                                registNo = compensateVo.getRegistNo();
                            }
                            break;
                        }
                    }
                }else{
                    List<PrpLPaymentVo> prpLPaymentVoList = compensateTaskService.findPrpLPaymentVoBySettleNo(ceritNo);
                    if(prpLPaymentVoList != null && prpLPaymentVoList.size()>0){//赔款
                        for (PrpLPaymentVo prpLPaymentVo : prpLPaymentVoList) {
                            //判断是否有通道号 有的话则为平安案件
                            if(!StringUtils.isBlank(prpLPaymentVo.getIdClmPaymentResult())){
                                idClmPaymentResult = prpLPaymentVo.getIdClmPaymentResult();
                                PrpLCompensateVo compensateVo = compensateService.findCompByPK(prpLPaymentVo.getCompensateNo());
                                if(compensateVo != null ){
                                    registNo = compensateVo.getRegistNo();
                                }
                                break;
                            }
                        }
                    }else{
                        List<PrpLChargeVo> prpLChargeVoList = compensateTaskService.findPrpLChargeVoBySettleNo(ceritNo);
                        if(prpLChargeVoList != null && prpLChargeVoList.size()>0){//费用
                            for (PrpLChargeVo prpLChargeVo : prpLChargeVoList) {
                                //判断是否有通道号 有的话则为平安案件
                                if(!StringUtils.isBlank(prpLChargeVo.getIdClmPaymentResult())){
                                    idClmPaymentResult = prpLChargeVo.getIdClmPaymentResult();
                                    PrpLCompensateVo compensateVo = compensateService.findCompByPK(prpLChargeVo.getCompensateNo());
                                    if(compensateVo != null ){
                                        registNo = compensateVo.getRegistNo();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        resultMap.put("registNo",registNo);
        resultMap.put("idClmPaymentResult",idClmPaymentResult);
        return resultMap;
    }

    /**
     * 组装平安任务
     * @param rollbackAccountDto
     * @param callbackRequestListDtos
     * @param idClmPaymentResult
     */
    private void addPingAnCallbackData(AccRollbackAccountMainDto rollbackAccountDto, List<UnionPayCallbackRequestListDto> callbackRequestListDtos, String idClmPaymentResult) {
        UnionPayCallbackRequestListDto piccCallBakcData = new UnionPayCallbackRequestListDto();
        piccCallBakcData.setBackDate(rollbackAccountDto.getRollBackTime());
        piccCallBakcData.setIdClmPaymentResult(idClmPaymentResult);
        piccCallBakcData.setBackReason(rollbackAccountDto.getErrorMessage());
        piccCallBakcData.setNoticeStatus("01");
        callbackRequestListDtos.add(piccCallBakcData);
    }
}
