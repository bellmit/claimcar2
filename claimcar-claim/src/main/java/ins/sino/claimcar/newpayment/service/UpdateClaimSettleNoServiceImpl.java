package ins.sino.claimcar.newpayment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.vo.PrpLCompensateVo;
import ins.sino.claimcar.claim.vo.PrpLPadPayMainVo;
import ins.sino.claimcar.newpayment.vo.PaymentConstants;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.other.service.AcheckService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.newpayment.vo.BasePartDto;
import ins.sino.claimcar.newpayment.vo.ItemVo;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;

/**
 * 收付推送数据更新理赔结算单号
 *
 * @author maofengning
 * @date 2020/5/18 19:55
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("updateClaimSettleNoService")
public class UpdateClaimSettleNoServiceImpl implements UpdateClaimSettleNoService {

    private static Logger logger = LoggerFactory.getLogger(UpdateClaimSettleNoServiceImpl.class);
    @Autowired
    private PadPayPubService padPayPubService;
    @Autowired
    private CompensateTaskService compensateTaskService;
    @Autowired
    private AssessorService assessorService;
    @Autowired
    private AcheckService acheckService;
    @Autowired
    private RegistQueryService registQueryService;
    @Autowired
    private PadPayService padPayService;
    /**
     * 接收收付数据，更新理赔相关业务的结算单号
     *
     * @param json 收付数据
     * @return 返回回写情况（成功或异常）
     */
    @Override
    public ResponseDto updateClaimSettleNo(String json) throws Exception{
        logger.info("\n收付更新理赔结算单号报文： " + json + "\n");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
        responseDto.setErrorMessage("收付更新理赔结算单号失败！");
        Gson gson = new Gson();
        if (StringUtils.isBlank(json)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付更新理赔结算单号报文为空！");

            return responseDto;
        }

        // 将报文解析成理赔对象
        BasePartDto basePartDto = null;
        try {
            basePartDto = gson.fromJson(json, BasePartDto.class);
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setResponseCode("收付更新理赔结算单号报文解析异常！" + e.getMessage());
        }
        if (basePartDto == null) {
            return responseDto;
        }

        // 收付更新理赔结算单号报文相关字段校验
        String errorMessage = validSettleNoData(basePartDto);
        if (StringUtils.isNotBlank(errorMessage)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(errorMessage);

            return responseDto;
        }

        try {
            responseDto = this.updateSettleNo(basePartDto, responseDto);
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(e.getMessage());
            logger.info("结算单号：" + basePartDto.getSettleNo() + "收付更新理赔结算单号处理异常!" + " 收付更新结算单号报文：{}\n 理赔异常信息：{}", json, e);
            throw e;
        }
        return responseDto;
    }


    private String validSettleNoData(BasePartDto basePartDto) {
        String errMsg = "";
        if (basePartDto != null) {
            if (StringUtils.isBlank(basePartDto.getSettleNo())) {
                errMsg = "收付回写理赔结算单号为空！";
            }
            /*
            if (StringUtils.isBlank(basePartDto.getAccountCode())) {
                errMsg = "收付回写理赔结算单号银行账号为空！";
            }
            */
            if (StringUtils.isBlank(basePartDto.getOperateType())) {
                errMsg = "收付回写理赔结算单号操作类型为空！";
            }
            if (basePartDto.getItemVo() == null || basePartDto.getItemVo().size() == 0) {
                errMsg = "收付回写理赔结算单号明细数据为空！";
            }
        }
        return errMsg;
    }

    private ResponseDto updateSettleNo(BasePartDto basePartDto, ResponseDto responseDto) throws Exception {
        if (basePartDto != null) {
            // 结算单号
            String settleNo = basePartDto.getSettleNo();
            String operateType = basePartDto.getOperateType();
            String accountCode = basePartDto.getAccountCode();
            try {
                for (ItemVo itemVo : basePartDto.getItemVo()) {
                    String certiNo = itemVo.getCertiNo();
                    if (certiNo != null) {
                        logger.info("结算单号：{} 计算书号：{} 收付回写理赔结算单号开始...", settleNo, certiNo);
                        boolean checkFlag = checkPingAnReport(certiNo);
                        //如果为平安案件，不走该逻辑
                        if (PaymentConstants.PAYREASON_P67.equals(itemVo.getPayRefReason()) && checkFlag) {
                            // 公估费
                            int count = assessorService.updateSettleNo(certiNo, settleNo, operateType);
                            if (count == 0) {
                                responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                            } else {
                                responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                            }
                            responseDto.setErrorMessage("公估费结算单号回写，更新数据为" + count + "条！");
                            logger.info("结算单号：{} 计算书号：{} 收付回写理赔公估费结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                        } else if (PaymentConstants.PAYREASON_P62.equals(itemVo.getPayRefReason())  && checkFlag) {//如果为平安案件，不走该逻辑
                            // 查勘费
                            int count = acheckService.updateSettleNo(certiNo, settleNo, operateType);
                            if (count == 0) {
                                responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                            } else {
                                responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                            }
                            responseDto.setErrorMessage("查勘费结算单号回写，更新数据为" + count + "条！");
                            logger.info("结算单号：{} 计算书号：{} 收付回写理赔查勘费结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                        } else if (certiNo.startsWith(CodeConstants.CompensateType.padpay_type)) {
                            // 垫付
                            if (itemVo.getSerialNo() != null) {
                                int count = padPayPubService.saveOrUpdateSettleNo(settleNo, accountCode, operateType,
                                        certiNo, itemVo.getSerialNo().toString());
                                if (count == 0) {
                                    responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                                } else {
                                    responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                                }
                                responseDto.setErrorMessage("垫付结算单号回写，更新数据为" + count + "条！");
                                logger.info("结算单号：{} 计算书号：{} 收付回写理赔垫付结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                            }
                        } else if (certiNo.startsWith(CodeConstants.CompensateType.prepay_type)) {
                            if (itemVo.getSerialNo() != null) {
                                int count = compensateTaskService.updatePrePaySettleNo(settleNo, accountCode,
                                        operateType, certiNo, itemVo.getSerialNo().toString(), itemVo.getPayRefReason());
                                if (count == 0) {
                                    responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                                } else {
                                    responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                                }
                                responseDto.setErrorMessage("预付结算单号回写，更新数据为" + count + "条！");
                                logger.info("结算单号：{} 计算书号：{} 收付回写理赔预付结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                            }
                        } else if (PaymentConstants.PAYREASON_P60.equals(itemVo.getPayRefReason())) {
                            if (itemVo.getSerialNo() != null) {
                                int count = compensateTaskService.updateCompensateSettleNo(settleNo, accountCode,
                                        operateType, certiNo, itemVo.getSerialNo().toString(), itemVo.getPayRefReason());
                                if (count == 0) {
                                    responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                                } else {
                                    responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                                }
                                responseDto.setErrorMessage("理算赔款结算单号回写，更新数据为" + count + "条！");
                                logger.info("结算单号：{} 计算书号：{} 收付回写理赔理算赔款结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                            }
                        } else {
                            if (itemVo.getSerialNo() != null) {
                                int count = compensateTaskService.updateCompensateChargeSettleNo(settleNo, accountCode,
                                        operateType, certiNo, itemVo.getSerialNo().toString(), itemVo.getPayRefReason());
                                if (count == 0) {
                                    responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                                } else {
                                    responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                                }
                                responseDto.setErrorMessage("理算费用结算单号回写，更新数据" + count + "条！");
                                logger.info("结算单号：{} 计算书号：{} 收付回写理赔理算费用结算单号完成，更新数据" + count + "条！", settleNo, certiNo);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("结算单号：{} 理赔结算单号回写异常！异常信息：{}", settleNo, e);
                throw e;
            }
        }
        return responseDto;
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
