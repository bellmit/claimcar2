package ins.sino.claimcar.newpayment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.utils.DateUtils;
import ins.platform.vo.SysCodeDictVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.service.ClaimService;
import ins.sino.claimcar.constant.Risk;
import ins.sino.claimcar.interf.vo.PayDataVo;
import ins.sino.claimcar.newpayment.vo.PayData;
import ins.sino.claimcar.newpayment.vo.PaymentConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.manager.service.ManagerService;
import ins.sino.claimcar.manager.vo.PrpLPayCustomVo;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.service.MsgModelService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.other.vo.PrpLCheckFeeVo;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.pinganUnion.service.PingAnPayCallBackService;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestListDto;
import ins.sino.claimcar.pinganunion.vo.payCallback.UnionPayCallbackRequestParamDto;
import ins.sino.claimcar.platform.service.SendPaymentToPlatformService;
import ins.sino.claimcar.newpayment.vo.BasePart;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import javax.ws.rs.Path;
import java.util.*;

/**
 * 收付回写理赔数据处理
 *
 * @author maofengning
 * @date 2020/5/16 15:17
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("paymentWriteBackService")
public class PaymentWriteBackServiceImpl implements PaymentWriteBackService {

    private static Logger logger = LoggerFactory.getLogger(PaymentWriteBackServiceImpl.class);
    @Autowired
    MsgModelService msgModelService;
    @Autowired
    private CompensateService compensateService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private PadPayService padPayService;
    @Autowired
    private AssessorService assessorService;
    @Autowired
    private AcheckService acheckService;
    @Autowired
	private SendPaymentToPlatformService sendPaymentToPlatformService;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    private PingAnPayCallBackService pingAnPayCallBackService;
    @Autowired
    private RegistQueryService registQueryService;
    @Override
    public ResponseDto writeBackPaymentToClaim(String json) throws Exception {

        logger.info("\n收付实收实付回写理赔报文： " + json + "\n");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
        responseDto.setErrorMessage("收付实收实付回写理赔失败！");

        Gson gson = new Gson();
        if (StringUtils.isBlank(json)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付实收实付回写理赔报文为空！");

            return responseDto;
        }

        BasePart basePart = null;
        try {
            basePart = gson.fromJson(json, BasePart.class);
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付实收实付回写理赔报文解析异常！" + e.getMessage());
        }

        if (basePart == null) {
            return responseDto;
        }

        // 收付实收实付回写理赔报文相关字段校验
        String errorMessage = writebackDataValid(basePart);
        if (StringUtils.isNotBlank(errorMessage)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(errorMessage);

            return responseDto;
        }

        // 更新结案短信状态
        updateEndCaseMsgStatus(basePart.getCertiNo());

        try {
            handleWriteBackData(basePart);
            responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
            responseDto.setErrorMessage("收付实收实付回写理赔成功！");
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(e.getMessage());
            logger.info("计算书号：" + basePart.getCertiNo() + "收付实收实付回写理赔处理异常!"  + " 实收实付回写报文：{}\n 异常信息：{}", json, e);
            throw e;
        }

        return responseDto;
    }

    /**
     * 短信发送状态置为已发送  1-已发送  0-未发送
     *
     * @param certiNo 计算书号
     */
    private void updateEndCaseMsgStatus(String certiNo) {
        logger.info("计算书号：{} 收付实收实付回写理赔结案短信状态修改开始...", certiNo);
        try {
            PrpLCompensateVo compensateVo = compensateService.findCompByPK(certiNo);
            if (compensateVo != null && compensateVo.getRegistNo() != null) {
                String registNo = compensateVo.getRegistNo();
                List<PrpsmsMessageVo> prpsmsMessageList = msgModelService.findPrpSmsMessageByBusinessNo(registNo);
                if (prpsmsMessageList != null && prpsmsMessageList.size() > 0) {
                    for (PrpsmsMessageVo vo : prpsmsMessageList) {
                        if (vo.getSendNodecode() != null && FlowNode.EndCas.name().equals(vo.getSendNodecode())) {
                            // 将结案短信的状态改为：1(推送成功)
                            vo.setStatus("1");
                            msgModelService.saveorUpdatePrpSmsMessage(vo);
                        }
                    }
                }
            }
            logger.info("业务号：{} 收付实收实付回写理赔结案短信状态更新完成！", certiNo);
        } catch (Exception e) {
            logger.info("业务号：{} 收付实收实付回写理赔结案短信状态更新异常！异常信息：{}", certiNo, e);
        }
    }

    /**
     * 收付回写理赔必录字段校验
     *
     * @param basePart 收付回写理赔报文对象
     * @return 返回相关字段校验结果
     */
    private String writebackDataValid(BasePart basePart) {
        String respMessage = "";
        if (basePart != null) {
            if (StringUtils.isBlank(basePart.getCertiNo())) {
                respMessage = "收付实收实付回写理赔业务号为空！";
            }

            if (StringUtils.isBlank(basePart.getCertiType())) {
                respMessage = "收付实收实付回写理赔业务类型为空！";
            }

            if (basePart.getPayData() == null || basePart.getPayData().size() == 0) {
                respMessage = "收付实收实付回写理赔支付数据为空！";
            }

            if (basePart.getPayData() != null) {
                for (PayData payData : basePart.getPayData()) {
                    if (payData != null && payData.getPayTime() != null) {
                        payData.setPayTime(payData.getPayTime().replace("\u00A0", " "));
                    }
                }
            }
        }

        return respMessage;
    }

    private void handleWriteBackData(BasePart basePart) throws Exception {
        String payReason = "";
        for (PayData payData : basePart.getPayData()) {
            payReason = payData.getPayRefReason();
        }
        boolean checkFlag = checkPingAnReport(basePart.getCertiNo());
        if (PaymentConstants.CERTITYPE_Y.equals(basePart.getCertiType())) {
            writeBackPrePayData(basePart);
        } else if (PaymentConstants.CERTITYPE_C.equals(basePart.getCertiType())) {
            if (PaymentConstants.PAYREASON_P6K.equals(payReason)) {
                writeBackPadPay(basePart);
            } else if (PaymentConstants.PAYREASON_P67.equals(payReason) && checkFlag) {
                writeBackAssessorFee(basePart);
            } else if (PaymentConstants.PAYREASON_P62.equals(payReason) && checkFlag) {
                writeBackCheckFee(basePart);
            } else {
                writeBackPayment(basePart);
            }
        }
    }

    /**
     * 收付回写预付数据
     *
     * @param basePart 收付回写数据对象
     */
    private void writeBackPrePayData(BasePart basePart) throws Exception {
        if (basePart != null) {
            try {
                logger.info("业务号：{} 收付实收实付回写理赔预付数据开始...", basePart.getCertiNo());
                PrpLCompensateVo compensateVo = compensateService.findCompByPK(basePart.getCertiNo());
                // 预付赔款
                List<PrpLPrePayVo> prePayPVos = compensateService.getPrePayVo(basePart.getCertiNo(), "P");
                // 预付费用
                List<PrpLPrePayVo> prePayFVos = compensateService.getPrePayVo(basePart.getCertiNo(), "F");
                // 要回写的数据
                List<PrpLPrePayVo> writeBackList = new ArrayList<PrpLPrePayVo>();
                List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
                Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                if (basePart.getPayData() != null && !basePart.getPayData().isEmpty()) {
                    for (PayData payData : basePart.getPayData()) {
                        // 赔款
                    	if (prePayPVos != null) {
                    		for (PrpLPrePayVo prePayPVo : prePayPVos) {
                                if (payData.getSerialNo() != null && payData.getSerialNo().toString().equals(prePayPVo.getSerialNo())
                                        && PaymentConstants.PAYREASON_P50.equals(payData.getPayRefReason())) {
                                    prePayPVo.setPayStatus("1");
                                    prePayPVo.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    writeBackList.add(prePayPVo);
                                    // 支付轨迹
                                    logger.info("收付实收实付回写理赔预付赔款支付轨迹开始！计算书号：" + prePayPVo.getCompensateNo() + " 系统时间" + new Date());
                                    this.savePrplPayHis(compensateVo.getClaimNo(), prePayPVo.getCompensateNo(), prePayPVo.getId(), "1", "Y",
                                            DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    logger.info("收付实收实付回写理赔预付赔款支付轨迹结束！计算书号：" + prePayPVo.getCompensateNo() + " 系统时间" + new Date());
                                    //获取平安支付主键
                                    String idClmPaymentResult = prePayPVo.getIdClmPaymentResult();
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(idClmPaymentResult)) {
                                        callbackRequestListDtos = addPingAnCallbackData(callbackRequestListDtos, payData, idClmPaymentResult);
                                    }
                                }
                            }
                    	}
                        
                    	if (prePayFVos != null) {
                    		// 费用
                            for (PrpLPrePayVo prePayFVo : prePayFVos) {
                                SysCodeDictVo sysCodeDictVo = dictTransMap.get(prePayFVo.getChargeCode());
                                if (payData.getSerialNo() != null && payData.getSerialNo().toString().equals(prePayFVo.getSerialNo()) 
                                		&& payData.getPayRefReason().equals(sysCodeDictVo.getProperty1())) {
                                    prePayFVo.setPayStatus("1");
                                    prePayFVo.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    writeBackList.add(prePayFVo);
                                    // 支付轨迹
                                    logger.info("收付实收实付回写理赔预付费用支付轨迹开始！计算书号：" + prePayFVo.getCompensateNo() + " 系统时间" + new Date());
                                    this.savePrplPayHis(compensateVo.getClaimNo(), prePayFVo.getCompensateNo(), prePayFVo.getId(), "1", "Y",
                                            DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    logger.info("收付实收实付回写理赔预付费用支付轨迹结束！计算书号：" + prePayFVo.getCompensateNo() + " 系统时间" + new Date());
                                    //获取平安支付主键
                                    String idClmPaymentResult = prePayFVo.getIdClmPaymentResult();
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(idClmPaymentResult)) {
                                        addPingAnCallbackData(callbackRequestListDtos, payData, idClmPaymentResult);
                                    }
                                }
                            }
                    	}
                    }
                }

                if (writeBackList.size() > 0) {
                    compensateService.writeBackPay(writeBackList, basePart.getCertiNo());
                }
                try {
                    //平安支付结果回调
                    if (!callbackRequestListDtos.isEmpty()) {
                        //平安支付结果回调Map
                        Map<String, Object> callBackDataMap = new HashMap<String, Object>();
                        UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
                        unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
                        callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
                        logger.info("收付回写平安支付结果开始！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                        pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
                        logger.info("收付回写平安支付结果结束！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                    }
                } catch (Exception e) {
                    logger.error("收付回写平安支付结果异常！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                    throw e;
                }
                logger.info("业务号：{} 收付实收实付回写理赔预付数据完成！", basePart.getCertiNo());
            } catch (Exception e) {
                logger.info("业务号：{} 收付实收实付回写理赔预付数据异常！异常信息：{}", basePart.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付回写理赔垫付数据
     *
     * @param basePart 收付回写数据对象
     * @throws Exception 回写异常
     */
    private void writeBackPadPay(BasePart basePart) throws Exception {
        if (basePart != null) {
            try {
                logger.info("业务号：{} 收付实收实付回写理赔垫付数据开始...", basePart.getCertiNo());
                PrpLPadPayMainVo padMainVo = padPayService.findPadPayMainByCompNo(basePart.getCertiNo());
                List<PrpLPadPayPersonVo> prplPadPayPersons = padMainVo.getPrpLPadPayPersons();
                List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
                for (PayData payDataVo : basePart.getPayData()) {
                    for (PrpLPadPayPersonVo personVo : prplPadPayPersons) {
                        if (payDataVo.getSerialNo() != null && personVo.getSerialNo().equals(payDataVo.getSerialNo().toString())) {
                            personVo.setPayStatus("1");
                            personVo.setPayTime(DateUtils.strToDate(payDataVo.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                            //支付轨迹
                            this.savePrplPayHis(padMainVo.getClaimNo(), padMainVo.getCompensateNo(), personVo.getId(), "1", "D",
                                    DateUtils.strToDate(payDataVo.getPayTime(), "yyyy-MM-dd HH:mm:ss")
                            );
                            //获取平安支付主键
                            String idClmPaymentResult = personVo.getIdClmPaymentResult();
                            if (org.apache.commons.lang.StringUtils.isNotBlank(idClmPaymentResult)) {
                                addPingAnCallbackData(callbackRequestListDtos, payDataVo, idClmPaymentResult);
                            }
                        }
                    }
                }
                padPayService.save(padMainVo, null, null);
                try {
                    //平安支付结果回调
                    if (!callbackRequestListDtos.isEmpty()) {
                        //平安支付结果回调Map
                        Map<String, Object> callBackDataMap = new HashMap<String, Object>();
                        UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
                        unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
                        callBackDataMap.put(padMainVo.getRegistNo(), unionPayCallbackRequestParamDto);
                        logger.info("收付回写平安支付结果开始！报案号：" + padMainVo.getRegistNo() + " 系统时间" + new Date());
                        pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
                        logger.info("收付回写平安支付结果结束！报案号：" + padMainVo.getRegistNo() + " 系统时间" + new Date());
                    }
                } catch (Exception e) {
                    logger.error("收付回写平安支付结果异常！报案号：" + padMainVo.getRegistNo() + " 系统时间" + new Date());
                    throw e;
                }
                logger.info("业务号：{} 收付实收实付回写理赔垫付数据完成！", basePart.getCertiNo());
            } catch (Exception e) {
                logger.info("业务号：{} 收付实收实付回写理赔垫付数据异常！异常信息：{}", basePart.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付回写理赔公估费数据
     *
     * @param basePart 收付回写数据对象
     * @throws Exception 回写异常
     */
    private void writeBackAssessorFee(BasePart basePart) throws Exception {
        if (basePart != null) {
            try {
                PrpLAssessorFeeVo assessorFee = assessorService.findAssessorFeeVoByComp(basePart.getCertiNo());
                if (assessorFee != null) {
                    if (basePart.getPayData() != null) {
                        PayData payData = basePart.getPayData().get(0);
                        assessorFee.setPayStatus("1");
                        assessorFee.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                        assessorFee.setTaskStatus("1");
                        logger.info("业务号：{} 收付实收实付回写理赔公估费数据开始...", basePart.getCertiNo());
                        assessorService.updateAssessorFee(assessorFee);
                        logger.info("业务号：{} 收付实收实付回写理赔公估费数据完成！", basePart.getCertiNo());
                    }
                }
            } catch (Exception e) {
                logger.info("业务号：{} 收付实收实付回写理赔公估费数据异常！异常信息：{}", basePart.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付回写查勘费
     *
     * @param basePart 收付回写数据对象
     * @throws Exception 回写异常
     */
    private void writeBackCheckFee(BasePart basePart) throws Exception {
        if (basePart != null) {
            try {
                PayData payData = basePart.getPayData().get(0);
                PrpLCheckFeeVo prplCheckFeeVo = acheckService.findCheckFeeVoByComp(basePart.getCertiNo());
                if (prplCheckFeeVo != null) {
                    prplCheckFeeVo.setPayStatus("1");
                    prplCheckFeeVo.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                    prplCheckFeeVo.setTaskStatus("1");
                    logger.info("业务号：{} 收付实收实付回写理赔查勘费数据开始...", basePart.getCertiNo());
                    acheckService.updateCheckFee(prplCheckFeeVo);
                    logger.info("业务号：{} 收付实收实付回写理赔查勘费数据完成！", basePart.getCertiNo());
                }
            } catch (Exception e) {
                logger.info("业务号：{} 收付实收实付回写理赔查勘费数据异常！异常信息：{}", basePart.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 收付回写理赔赔款
     *
     * @param basePart 收付回写数据对象
     * @throws Exception 回写异常
     */
    private void writeBackPayment(BasePart basePart) throws Exception {
        if (basePart != null) {
            try {
                PrpLCompensateVo compensateVo = compensateService.findCompByPK(basePart.getCertiNo());
                List<UnionPayCallbackRequestListDto> callbackRequestListDtos = new ArrayList<UnionPayCallbackRequestListDto>();
                for (PayData payData : basePart.getPayData()) {
                    if (PaymentConstants.PAYREASON_P60.equals(payData.getPayRefReason()) ||
                            PaymentConstants.PAYREASON_P6B.equals(payData.getPayRefReason())
                            || PaymentConstants.PAYREASON_P6D.equals(payData.getPayRefReason())) {
                        // 多个收款人时 收付同时
                        for (PrpLPaymentVo paymentVo : compensateVo.getPrpLPayments()) {
                            // 通过收付的账号序号 判断是否支付
                            if (payData.getSerialNo() != null && paymentVo.getSerialNo().equals(payData.getSerialNo().toString())) {
                                paymentVo.setPayStatus("1");
                                paymentVo.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                // 支付轨迹
                                this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), paymentVo.getId(), "1", "P",
                                        DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                //获取平安支付主键
                                String idClmPaymentResult = paymentVo.getIdClmPaymentResult();
                                if (org.apache.commons.lang.StringUtils.isNotBlank(idClmPaymentResult)) {
                                    addPingAnCallbackData(callbackRequestListDtos, payData, idClmPaymentResult);
                                }
                            }
                        }
                    } else {
                        // 费用
                        for (PrpLChargeVo chargeVo : compensateVo.getPrpLCharges()) {
                            if (chargeVo.getSerialNo().equals(payData.getSerialNo().toString())) {
                                Map<String, SysCodeDictVo> dictTransMap = codeTranService.findCodeDictTransMap("ChargeCode", "");
                                SysCodeDictVo sysCodeDictVo = dictTransMap.get(chargeVo.getChargeCode());
                                if (payData.getPayRefReason().equals(sysCodeDictVo.getProperty2())) {
                                    chargeVo.setPayStatus("1");
                                    chargeVo.setPayTime(DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    //支付轨迹
                                    this.savePrplPayHis(compensateVo.getClaimNo(), compensateVo.getCompensateNo(), chargeVo.getId(), "1", "F",
                                            DateUtils.strToDate(payData.getPayTime(), "yyyy-MM-dd HH:mm:ss"));
                                    //获取平安支付主键
                                    String idClmPaymentResult = chargeVo.getIdClmPaymentResult();
                                    if (org.apache.commons.lang.StringUtils.isNotBlank(idClmPaymentResult)) {
                                        addPingAnCallbackData(callbackRequestListDtos, payData, idClmPaymentResult);
                                    }
                                }
                            }
                        }
                    }
                    logger.info("业务号：{} 收付实收实付回写理赔赔款数据开始...", basePart.getCertiNo());
                    compensateTaskService.paymentWriteBackCompVo(compensateVo);
                    logger.info("业务号：{} 收付实收实付回写理赔赔款数据完成！", basePart.getCertiNo());
                    try {
                        if (!isMotoOrTractor(compensateVo)) {
                            logger.info("收付回写理赔赔款后送平台开始！计算书号：" + compensateVo.getCompensateNo());
                            sendPaymentToPlatformService.sendPlatform(compensateVo.getCompensateNo());
                            logger.info("收付回写理赔赔款后送平台结束！计算书号：" + compensateVo.getCompensateNo());
                        }
                    } catch (Exception e) {
                        logger.info("收付回写理赔赔款后送平台异常！计算书号：" + compensateVo.getCompensateNo(), e);
                    }
                    try {
                        //平安支付结果回调
                        if (!callbackRequestListDtos.isEmpty()) {
                            //平安支付结果回调Map
                            Map<String, Object> callBackDataMap = new HashMap<String, Object>();
                            UnionPayCallbackRequestParamDto unionPayCallbackRequestParamDto = new UnionPayCallbackRequestParamDto();
                            unionPayCallbackRequestParamDto.setList(callbackRequestListDtos);
                            callBackDataMap.put(compensateVo.getRegistNo(), unionPayCallbackRequestParamDto);
                            logger.info("收付回写平安支付结果开始！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                            pingAnPayCallBackService.payCallBackDataBuild(callBackDataMap);
                            logger.info("收付回写平安支付结果结束！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                        }
                    } catch (Exception e) {
                        logger.error("收付回写平安支付结果异常！报案号：" + compensateVo.getRegistNo() + " 系统时间" + new Date());
                        throw e;
                    }
                }
            } catch (Exception e) {
                logger.info("业务号：{} 收付实收实付回写理赔赔款数据异常！异常信息：{}", basePart.getCertiNo(), e);
                throw e;
            }
        }
    }

    /**
     * 支付轨迹回写
     */
    private void savePrplPayHis(String claimNo, String compensateNo, Long id, String flags, String hisType, Date payTime) {
        compensateService.savePrplPayHis(claimNo, compensateNo, id, flags, hisType, payTime);
    }

    /**
     * 判断是否为摩托车或拖拉机
     * @param compensateVo 计算书对象
     * @return true-是 false-否
     */
    private boolean isMotoOrTractor(PrpLCompensateVo compensateVo) {
        boolean isMor = true;
        PrpLCMainVo cMainVo = policyViewService.getPolicyInfo(compensateVo.getRegistNo(), compensateVo.getPolicyNo());
        //排除粤Z
        String comCode = SpringProperties.getProperty("NAMEVERIFIED_SMS_COM");
        if (StringUtils.isNotBlank(cMainVo.getComCode())
                && StringUtils.isNotBlank(comCode)
                && comCode.contains(cMainVo.getComCode().substring(0, 2))
                && StringUtils.isBlank(cMainVo.getValidNo())) {
            PrpLCItemCarVo cItemCarVo = cMainVo.getPrpCItemCars().get(0);
            if (cItemCarVo.getLicenseNo() != null && cItemCarVo.getLicenseNo().startsWith(CodeConstants.licenseNoStart)) {
                return false;
            }
        }
        //排除上海平台
        if (!cMainVo.getComCode().startsWith("22")) {
            String riskCode = cMainVo.getRiskCode();
            if (isMor2(cMainVo)) {
                if (Risk.DQZ.equals(riskCode)) {
                    isMor = false;
                } else {//商业平台
                    if (Risk.isDAC(riskCode)) {
                        isMor = false;
                    }
                }
            }
            if (Risk.isDBC(riskCode) || Risk.isDBT(riskCode)) {//全国的1208、1209不上平台
                isMor = false;
            }
        }
        return isMor;
    }


    private boolean isMor2(PrpLCMainVo cMainVo) {
        boolean mor = false;
        if (cMainVo != null) {
            PrpLCItemCarVo cItemCar = cMainVo.getPrpCItemCars().get(0);
            String carKindCode = cItemCar.getCarKindCode();
            if (StringUtils.isNotBlank(carKindCode) && "J,M".contains(carKindCode.substring(0, 1))) {
                mor = true;
            }
            if (Risk.isDAC(cMainVo.getRiskCode())) {
                mor = true;
            }
        }
        return mor;
    }
    private List<UnionPayCallbackRequestListDto> addPingAnCallbackData(List<UnionPayCallbackRequestListDto> callbackRequestListDtos, PayData payDataVo, String idClmPaymentResult) {
        UnionPayCallbackRequestListDto piccCallBakcData = new UnionPayCallbackRequestListDto();
        piccCallBakcData.setPayDate(payDataVo.getPayTime());
        piccCallBakcData.setIdClmPaymentResult(idClmPaymentResult);
        piccCallBakcData.setNoticeStatus("00");
        callbackRequestListDtos.add(piccCallBakcData);
        return callbackRequestListDtos;
    }

    private boolean checkPingAnReport(String certiNo){
        boolean flag = true ;
        PrpLCompensateVo compensateVo = compensateTaskService.findCompByPK(certiNo);
        if(compensateVo != null){
            PrpLRegistVo registVo = registQueryService.findByRegistNo(compensateVo.getRegistNo());
            if(registVo != null && registVo.getPaicReportNo() != null && registVo.getPaicReportNo() != ""){
                flag = false;
            }
        }else{
            PrpLPadPayMainVo padPayMainVo = padPayService.findPadPayMainByCompNo(certiNo);
            if(padPayMainVo != null){
                PrpLRegistVo registVo = registQueryService.findByRegistNo(padPayMainVo.getRegistNo());
                if(registVo != null && registVo.getPaicReportNo() != null && registVo.getPaicReportNo() != ""){
                    flag = false;
                }
            }
        }
        return flag;
    }
}
