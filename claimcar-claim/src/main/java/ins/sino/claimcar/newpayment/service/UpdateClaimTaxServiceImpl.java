package ins.sino.claimcar.newpayment.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

import ins.framework.dao.database.DatabaseDao;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.YN01;
import ins.sino.claimcar.claim.vo.*;
import ins.sino.claimcar.newpayment.vo.PaymentConstants;
import ins.sino.claimcar.claim.po.PrplPayeeKindPayment;
import ins.sino.claimcar.claim.service.CompensateTaskService;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.other.service.AssessorService;
import ins.sino.claimcar.other.vo.PrpLAssessorFeeVo;
import ins.sino.claimcar.newpayment.vo.BasePartDetail;
import ins.sino.claimcar.newpayment.vo.BasePartTax;
import ins.sino.claimcar.newpayment.vo.InvoiceDetailVo;
import ins.sino.claimcar.newpayment.vo.InvoiceTransReturnVo;
import ins.sino.claimcar.newpayment.vo.PrpJlossPlanSubVatVo;
import ins.sino.claimcar.newpayment.vo.ResponseDto;
import ins.sino.claimcar.pay.service.PadPayPubService;
import ins.sino.claimcar.pay.service.PadPayService;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 收付更新车理赔价税数据
 *
 * @author maofengning
 * @date 2020/5/19 19:42
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("updateClaimTaxService")
public class UpdateClaimTaxServiceImpl implements UpdateClaimTaxService {

    private static Logger logger = LoggerFactory.getLogger(UpdateClaimTaxServiceImpl.class);
    @Autowired
    private AssessorService assessorService;
    @Autowired
    CompensateTaskService compensateTaskService;
    @Autowired
    CompensateService compensateService;
    @Autowired
    PadPayService padPayService;
    @Autowired
    PadPayPubService padPayPubService;
	@Autowired
	VerifyClaimService verifyClaimService;
    @Autowired
	DatabaseDao databaseDao;
    @Autowired
    BaseDaoService baseDaoService;
    


    @Override
    public ResponseDto updateClaimTax(String json) {
        logger.info("收付更新理赔价税数据报文：{}", json);
        ResponseDto responseDto = new ResponseDto();
        Gson gson = new Gson();
        if (StringUtils.isBlank(json)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付更新理赔价税数据报文为空！");

            return responseDto;
        }

        // 将报文解析成理赔对象
        BasePartTax basePartTax = null;
        try {
            basePartTax = gson.fromJson(json, BasePartTax.class);
        } catch (Exception e) {
            responseDto.setErrorMessage(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage("收付更新理赔结算单号报文解析异常！" + e.getMessage());
            logger.info("收付更新理赔结算单号报文解析异常！报文：{} \n异常信息：{}", json, e);
        }
        if (basePartTax == null) {
            return responseDto;
        }

        // 收付退票理赔报文相关字段校验
        String errorMessage = validTaxData(basePartTax);
        if (StringUtils.isNotBlank(errorMessage)) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(errorMessage);

            return responseDto;
        }

        try {
            this.updateTax(basePartTax);
            responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
            responseDto.setErrorMessage("收付更新理赔价税数据成功！");
        } catch (Exception e) {
            responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
            responseDto.setErrorMessage(e.getMessage());
            logger.info("结算单号：" + basePartTax.getSettleNo() + "收付更新理赔价税数据处理异常!" + " 收付报文：{}\n 理赔异常信息：{}", json, e);
        }

        return responseDto;
    }

    private String validTaxData(BasePartTax basePartTax) {
        String errMsg = "";
        if (basePartTax != null) {
            if (basePartTax.getSettleNo() == null) {
                errMsg = "收付更新理赔价税数据报文结算单号为空！";
            }
            if (basePartTax.getBasePartDetails() == null || basePartTax.getBasePartDetails().size() == 0) {
                errMsg = "收付更新理赔价税数据报文价税明细为空！";
            } else {
                for (int i = 0; i < basePartTax.getBasePartDetails().size(); i++) {
                    BasePartDetail detail = basePartTax.getBasePartDetails().get(i);
                    if (StringUtils.isBlank(detail.getCertiNo())) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条业务号为空！";
                    }
                    if (detail.getSerialNo() == null) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条序号为空！";
                    }
                    if (StringUtils.isBlank(detail.getInvoicetype())) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条发票类型为空！";
                    }
                    if (detail.getSumAmountNT() == null) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条不含税金额为空！";
                    }
                    if (detail.getTaxRate() == null) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条税率为空！";
                    }
                    if (detail.getSumAmountTax() == null) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条总税额为空！";
                    }
                    if (StringUtils.isBlank(detail.getPayRefReason())) {
                        errMsg = "收付更新理赔价税数据报文价税明细第" + i + "条收付原因为空！";
                    }
                }
            }
        }

        return errMsg;
    }

    /**
     * 更新理赔价税数据
     * @param basePartTax 收付回写理赔价税报文数据对象
     * @throws Exception 价税回写异常
     */
    private void updateTax(BasePartTax basePartTax) throws Exception {
        if (basePartTax != null) {
            List<BasePartDetail> basePartDetails = basePartTax.getBasePartDetails();
            for (BasePartDetail basePartDetail : basePartDetails) {
                // TODO 2020-5-19 20:16:15  待确定
                if (PaymentConstants.PAYREASON_P67.equals(basePartDetail.getPayRefReason())) {
                    updateAssessorFeeTax(basePartDetail);
                    continue;
                }
                if (basePartDetail.getCertiNo().startsWith("Y")) {
                    updatePreTax(basePartDetail);
                    continue;
                }
                if (basePartDetail.getCertiNo().startsWith("D")) {
                    updatePadTax(basePartDetail);
                    continue;
                }
                updateCompensateTax(basePartDetail);
            }
        }
    }

    /**
     * 更新公估费的价税数据
     *
     * @param basePartDetail 价税明细数据
     * @throws Exception 公估费价税数据回写异常
     */
    private void updateAssessorFeeTax(BasePartDetail basePartDetail) throws Exception {
        if (basePartDetail != null) {
            String certiNo = basePartDetail.getCertiNo();
            try {
                PrpLAssessorFeeVo feeVo = assessorService.findAssessorFeeVoByComp(certiNo);
                if (feeVo != null) {
                    logger.info("业务号：{} 理赔公估费价税数据回写开始...", certiNo);
                    feeVo.setInvoiceType(basePartDetail.getInvoicetype());
                    feeVo.setAddTaxRate(String.valueOf(basePartDetail.getTaxRate()));
                    feeVo.setAddTaxValue(String.valueOf(basePartDetail.getSumAmountTax()));
                    feeVo.setNoTaxValue(String.valueOf(basePartDetail.getSumAmountNT()));
                    assessorService.updateAssessorFee(feeVo);
                    logger.info("业务号：{} 理赔公估费价税数据回写完成！", certiNo);
                }
            } catch (Exception e) {
                logger.info("业务号：" + certiNo + " 理赔公估费价税数据回写异常！", e);
                throw e;
            }
        }
    }

    /**
     * 预付价税数据回写
     *
     * @param basePartDetail 价税明细数据
     * @throws Exception 预付价税数据回写异常
     */
    private void updatePreTax(BasePartDetail basePartDetail) throws Exception {
        if (basePartDetail != null) {
            logger.info("业务号：{} 理赔预付价税数据回写开始...", basePartDetail.getCertiNo());
            try {
                List<PrpLPrePayVo> prePayVoList = new ArrayList<PrpLPrePayVo>();
                if (PaymentConstants.PAYREASON_P50.equals(basePartDetail.getPayRefReason())) {
                    prePayVoList = compensateTaskService.getPrePayVo(basePartDetail.getCertiNo(), "P");
                } else {
                    prePayVoList = compensateTaskService.getPrePayVo(basePartDetail.getCertiNo(), "F");
                }
                List<PrpLPrePayVo> prePayVoSerialNoList = new ArrayList<PrpLPrePayVo>();
                // 总预付（赔款或费用）金额
                BigDecimal countSumAmt = BigDecimal.ZERO;
                for (PrpLPrePayVo prepayVo : prePayVoList) {
                    if (prepayVo.getSerialNo().equals(basePartDetail.getSerialNo() + "")) {
                        countSumAmt = countSumAmt.add(prepayVo.getPayAmt());
                        prePayVoSerialNoList.add(prepayVo);
                    }
                }

                // 集合当前遍历索引值（用来判断是否是集合的最后一个元素）
                int listIndex = 1;
                // 价税明细不含税金额
                BigDecimal sumAmountNT = basePartDetail.getSumAmountNT();
                // 税明细总税额
                BigDecimal sumTax = basePartDetail.getSumAmountTax();
                BigDecimal addupTax = BigDecimal.ZERO;
                BigDecimal addupNoTax = BigDecimal.ZERO;
                for (PrpLPrePayVo prepayVo : prePayVoSerialNoList) {
                    prepayVo.setInvoiceType(basePartDetail.getInvoicetype());
                    prepayVo.setAddTaxRate(String.valueOf(basePartDetail.getTaxRate()));
                    if (listIndex == prePayVoSerialNoList.size()) {
                        // 预付最后一项的税额与不含税金额采用减法
                        prepayVo.setAddTaxValue(String.valueOf(sumTax.subtract(addupTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                        prepayVo.setNoTaxValue(String.valueOf(sumAmountNT.subtract(addupNoTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else {
                        // 预付赔款金额
                        BigDecimal curPayAmt = prepayVo.getPayAmt();
                        // 当前预付赔款金额占总赔款的比例
                        BigDecimal rate = curPayAmt.divide(countSumAmt, 4, BigDecimal.ROUND_HALF_UP);
                        // 根据当前预付赔款占总赔款的比例分别计算出当前预付对象的税额与不含税金额
                        BigDecimal taxValue = sumTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal noTaxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        prepayVo.setAddTaxValue(String.valueOf(taxValue));
                        prepayVo.setNoTaxValue(String.valueOf(noTaxValue));
                        addupTax = addupTax.add(taxValue);
                        addupNoTax = addupNoTax.add(noTaxValue);
                    }
                    listIndex++;
                    compensateService.updatePrpLPrePay(prepayVo);
                }
                logger.info("业务号：{} 理赔预付价税数据回写完成！", basePartDetail.getCertiNo());
            } catch (Exception e) {
                logger.info("业务号：" + basePartDetail.getCertiNo() + " 理赔预付价税数据回写异常！{}", e);
                throw e;
            }
        }
    }

    /**
     * 更新垫付价税数据
     *
     * @param basePartDetail 价税明细数据
     * @throws Exception 垫付价税回写异常
     */
    private void updatePadTax(BasePartDetail basePartDetail) throws Exception {
        if (basePartDetail != null) {
            logger.info("业务号：{} 理赔垫付价税数据回写开始...", basePartDetail.getCertiNo());
            try {
                // 垫付赔款
                if (PaymentConstants.PAYREASON_P6K.equals(basePartDetail.getPayRefReason())) {
                    PrpLPadPayMainVo padPayMainVo = padPayService.findPadPayMainByCompNo(basePartDetail.getCertiNo());
                    List<PrpLPadPayPersonVo> prpLPadPayPersonVos = padPayMainVo.getPrpLPadPayPersons();
                    List<PrpLPadPayPersonVo> padPayPersonSerialNoVos = new ArrayList<PrpLPadPayPersonVo>();
                    for (PrpLPadPayPersonVo vo : prpLPadPayPersonVos) {
                        if (vo.getSerialNo().equals(basePartDetail.getSerialNo() + "")) {
                            padPayPersonSerialNoVos.add(vo);
                        }
                    }

                    int listIndex = 1;
                    // 明细数据中总不含税金额
                    BigDecimal sumAmountNT = basePartDetail.getSumAmountNT();
                    // 明细数据总税额
                    BigDecimal sumTax = basePartDetail.getSumAmountTax();
                    BigDecimal addupTax = BigDecimal.ZERO;
                    BigDecimal addupNoTax = BigDecimal.ZERO;

                    for (PrpLPadPayPersonVo padPayPersonVo : padPayPersonSerialNoVos) {
                        padPayPersonVo.setInvoiceType(basePartDetail.getInvoicetype());
                        padPayPersonVo.setAddTaxRate(String.valueOf(basePartDetail.getTaxRate()));
                        if (listIndex == padPayPersonSerialNoVos.size()) {
                            padPayPersonVo.setAddTaxValue(String.valueOf(sumTax.subtract(addupTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                            padPayPersonVo.setNoTaxValue(String.valueOf(sumAmountNT.subtract(addupNoTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                        } else {
                            BigDecimal rate = new BigDecimal("1.0000");
                            BigDecimal taxValue = sumTax.multiply(rate);
                            BigDecimal noTaxValue = sumAmountNT.multiply(rate);
                            padPayPersonVo.setAddTaxValue(String.valueOf(taxValue.setScale(2, BigDecimal.ROUND_HALF_UP)));
                            padPayPersonVo.setNoTaxValue(String.valueOf(noTaxValue.setScale(2, BigDecimal.ROUND_HALF_UP)));
                            addupTax = addupTax.add(taxValue);
                            addupNoTax = addupNoTax.add(noTaxValue);
                        }
                        listIndex++;
                        padPayPubService.updatePadPay(padPayPersonVo);
                    }
                    logger.info("业务号：{} 理赔垫付价税数据回写完成！", basePartDetail.getCertiNo());
                }
            } catch (Exception e) {
                logger.info("业务号：" + basePartDetail.getCertiNo() + " 理赔垫付价税数据回写异常！", e);
                throw e;
            }

        }
    }

    /**
     * 回写理算价税数据
     * @param basePartDetail 收付价税明细数据
     * @throws Exception 理算价税数据回写异常
     */
    private void updateCompensateTax(BasePartDetail basePartDetail) throws Exception {
        if (basePartDetail != null) {
            logger.info("业务号：{} 理赔理算价税数据回写开始...", basePartDetail.getCertiNo());
            try {
                PrpLCompensateVo compensateVo = compensateService.findCompByPK(basePartDetail.getCertiNo());
                List<PrpLPaymentVo> paymentVos = new ArrayList<PrpLPaymentVo>();
                List<PrpLChargeVo> chargeVos = new ArrayList<PrpLChargeVo>();
                if (PaymentConstants.PAYREASON_P60.equals(basePartDetail.getPayRefReason())) {
                    paymentVos = compensateVo.getPrpLPayments();
                    updatePaymenTax(basePartDetail, paymentVos);
                } else {
                    chargeVos = compensateVo.getPrpLCharges();
                    updateChargeTax(basePartDetail, chargeVos);
                }
                logger.info("业务号：{} 理赔理算价税数据回写完成！", basePartDetail.getCertiNo());
            } catch (Exception e) {
                logger.info("业务号：{} 理赔理算价税数据回写异常！", basePartDetail.getCertiNo());
                throw e;
            }
        }
    }

    /**
     * 收付回写理算赔款价税数据
     *
     * @param basePartDetail 收付价税明细数据
     * @param paymentVos     理算赔款数据
     * @throws Exception 赔款价税回写异常
     */
    private void updatePaymenTax(BasePartDetail basePartDetail, List<PrpLPaymentVo> paymentVos) throws Exception {
        if (basePartDetail != null && paymentVos != null && paymentVos.size() > 0) {
            logger.info("业务号：{} 理赔理算赔款价税数据回写开始...", basePartDetail.getCertiNo());
            try {
                List<PrpLPaymentVo> paymentSerialNoVos = new ArrayList<PrpLPaymentVo>();
                // 统计与收付价税明细数据序号一致的收款人总金额
                BigDecimal addUpSumRealPay = BigDecimal.ZERO;
                for (PrpLPaymentVo paymentVo : paymentVos) {
                    if (paymentVo.getSerialNo().equals(basePartDetail.getSerialNo() + "")) {
                        addUpSumRealPay = addUpSumRealPay.add(DataUtils.NullToZero(paymentVo.getSumRealPay()));
                        paymentSerialNoVos.add(paymentVo);
                    }
                }

                int listIndex = 1;
                // 价税明细数据总不含税金额
                BigDecimal sumAmountNT = basePartDetail.getSumAmountNT();
                // 价税明细数据总税额
                BigDecimal sumTax = basePartDetail.getSumAmountTax();
                BigDecimal addupTax = BigDecimal.ZERO;
                BigDecimal addupNoTax = BigDecimal.ZERO;
                for (PrpLPaymentVo payment : paymentSerialNoVos) {
                    payment.setInvoiceType(basePartDetail.getInvoicetype());
                    payment.setAddTaxRate(String.valueOf(basePartDetail.getTaxRate()));
                    if (listIndex == paymentSerialNoVos.size()) {
                        payment.setAddTaxValue(String.valueOf(sumTax.subtract(addupTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                        payment.setNoTaxValue(String.valueOf(sumAmountNT.subtract(addupNoTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else {
                        BigDecimal sumRealPay = payment.getSumRealPay();
                        BigDecimal rate = sumRealPay.divide(addUpSumRealPay, 4, BigDecimal.ROUND_HALF_UP);
                        BigDecimal taxValue = sumTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal noTaxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        payment.setAddTaxValue(String.valueOf(taxValue));
                        payment.setNoTaxValue(String.valueOf(noTaxValue));
                        addupTax = addupTax.add(taxValue);
                        addupNoTax = addupNoTax.add(noTaxValue);
                    }
                    listIndex++;
                    // 写入数据库
                    compensateService.updatePrpLPaymentVo(payment);
                }
                logger.info("业务号：{} 理赔理算赔款价税数据回写完成！", basePartDetail.getCertiNo());
            } catch (Exception e) {
                logger.info("业务号：" + basePartDetail.getCertiNo() + " 理赔理算赔款价税数据回写异常！", e);
                throw e;
            }
        }
    }

    /**
     * 收付回写理算费用价税数据
     * @param basePartDetail 收付价税明细数据
     * @param chargeVos 理算费用对象集合
     * @throws Exception 理算费用价税数据回写异常
     */
    private void updateChargeTax(BasePartDetail basePartDetail, List<PrpLChargeVo> chargeVos) throws Exception {
        if (basePartDetail != null && chargeVos != null && chargeVos.size() > 0) {
            logger.info("业务号：{} 理赔理算费用价税数据回写开始...", basePartDetail.getCertiNo());
            try {
                List<PrpLChargeVo> prpLChargeSerialNoVos = new ArrayList<PrpLChargeVo>();
                // 统计与收付价税明细数据序号一致的收款人总金额
                BigDecimal addUpSumFeeAmt = BigDecimal.ZERO;
                for (PrpLChargeVo chargeVo : chargeVos) {
                    if (chargeVo.getSerialNo().equals(basePartDetail.getSerialNo() + "")) {
                        addUpSumFeeAmt = addUpSumFeeAmt.add(DataUtils.NullToZero(chargeVo.getFeeAmt()))
                                .subtract(DataUtils.NullToZero(chargeVo.getOffAmt()))
                                .subtract(DataUtils.NullToZero(chargeVo.getOffPreAmt()));
                        prpLChargeSerialNoVos.add(chargeVo);
                    }
                }

                int listIndex = 1;
                // 价税明细数据总不含税金额
                BigDecimal sumAmountNT = basePartDetail.getSumAmountNT();
                // 价税明细数据总税额
                BigDecimal sumTax = basePartDetail.getSumAmountTax();
                BigDecimal addupTax = BigDecimal.ZERO;
                BigDecimal addupNoTax = BigDecimal.ZERO;
                for (PrpLChargeVo prpLChargeVo : prpLChargeSerialNoVos) {
                    prpLChargeVo.setInvoiceType(basePartDetail.getInvoicetype());
                    prpLChargeVo.setAddTaxRate(String.valueOf(basePartDetail.getTaxRate()));
                    if (listIndex == prpLChargeSerialNoVos.size()) {
                        prpLChargeVo.setAddTaxValue(String.valueOf(sumTax.subtract(addupTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                        prpLChargeVo.setNoTaxValue(String.valueOf(sumAmountNT.subtract(addupNoTax).setScale(2, BigDecimal.ROUND_HALF_UP)));
                    } else {
                        BigDecimal curFeeAmt = DataUtils.NullToZero(prpLChargeVo.getFeeAmt())
                                .subtract(DataUtils.NullToZero(prpLChargeVo.getOffAmt()))
                                .subtract(DataUtils.NullToZero(prpLChargeVo.getOffPreAmt()));
                        BigDecimal rate = curFeeAmt.divide(addUpSumFeeAmt, 4, BigDecimal.ROUND_HALF_UP);
                        BigDecimal taxValue = sumTax.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        BigDecimal noTaxValue = sumAmountNT.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        prpLChargeVo.setAddTaxValue(String.valueOf(taxValue));
                        prpLChargeVo.setNoTaxValue(String.valueOf(noTaxValue));
                        addupTax = addupTax.add(taxValue);
                        addupNoTax = addupNoTax.add(noTaxValue);
                    }
                    listIndex++;
                    // 写入数据库
                    compensateService.updatePrpLCharges(prpLChargeVo);
                }
                logger.info("业务号：{} 理赔理算费用价税数据回写完成！", basePartDetail.getCertiNo());
            } catch (Exception e) {
                logger.error("业务号：" + basePartDetail.getCertiNo() + " 理赔理算费用价税数据回写异常！", e);
                throw e;
            }
        }
    }
    
    /**
     * 校验vat回写的数据
     */
    public ResponseDto verifyVatBackClaimKindPay(String json){
    	logger.error("VAT推送理赔价税拆分报文{} ", json);
    	  ResponseDto responseDto = new ResponseDto();
    	  Gson gson = new Gson();
          if (StringUtils.isBlank(json)) {
              responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
              responseDto.setErrorMessage("VAT推送理赔价税拆分报文为空！");
              return responseDto;
          }

          // 将报文解析成理赔对象
          InvoiceTransReturnVo basePart = null;
          try {
              basePart = gson.fromJson(json, InvoiceTransReturnVo.class);
          } catch (Exception e) {
              responseDto.setErrorMessage(PaymentConstants.RESP_FAILED);
              responseDto.setErrorMessage("VAT推送理赔价税拆分报文解析异常！" + e.getMessage());
              logger.error("VAT推送理赔价税拆分报文解析异常！报文：{} \n异常信息：{}", json, e);
              return responseDto;
          }
          if (basePart == null) {
        	  responseDto.setErrorMessage(PaymentConstants.RESP_FAILED);
              responseDto.setErrorMessage("VAT推送理赔价税拆分报文解析异常！" );
              return responseDto;
          }else{
        	  String verifyResult = verifyVatBackData(basePart);
        	  if(StringUtils.isBlank(verifyResult)){
        		  responseDto.setResponseCode(PaymentConstants.RESP_SUCCESS);
                  responseDto.setErrorMessage("VAT推送理赔价税拆分数据成功！");
        	  }else{
        		  responseDto.setResponseCode(PaymentConstants.RESP_FAILED);
                  responseDto.setErrorMessage(verifyResult);
        	  }
          }
         
    	return responseDto;
    }
    
    /**
     * 校验vat回写的参数
     * @param invoiceTransReturnVo
     * @return
     */
	private String verifyVatBackData(InvoiceTransReturnVo invoiceTransReturnVo) {
		
		StringBuffer errorMsg=new StringBuffer("");
		List<InvoiceDetailVo> prpJecdInvoiceDtoList = invoiceTransReturnVo.getPrpJecdInvoiceDtoList();
		if (prpJecdInvoiceDtoList == null || prpJecdInvoiceDtoList.size() == 0) {
			errorMsg.append("发票绑定清单为空") ;
		}else{
			int i=1;
			for(InvoiceDetailVo invoiceDetail:prpJecdInvoiceDtoList){
				
				 if (StringUtils.isBlank(invoiceDetail.getCertiNo())) {
					 errorMsg .append("发票绑定清单第").append(i).append("条业务号为空！");
                 }
                 if (StringUtils.isBlank(invoiceDetail.getRegistNo())) {
                	 errorMsg .append("发票绑定清单第").append(i).append("条报案号为空！");
                 }
                 if (StringUtils.isBlank(invoiceDetail.getRiskCode())) {
                	 errorMsg .append("发票绑定清单第").append(i).append("条险种为空！");
                 }
                 if (StringUtils.isBlank(invoiceDetail.getPayeeId())) {
                	 errorMsg .append("发票绑定清单第").append(i).append("条支付对象id为空！");
                 }
                 if (StringUtils.isBlank(invoiceDetail.getLossType())) {
                	 errorMsg .append("发票绑定清单第").append(i).append("条赔付类型为空！");
                 }
                 if (invoiceDetail.getDeductionAmount()==null) {
                	 errorMsg .append("发票绑定清单第").append(i).append("条抵扣金额为空！");
                 }
                 //价税拆分险别信息
                 List<PrpJlossPlanSubVatVo> prpJlossPlanSubDtos = invoiceDetail.getPrpJlossPlanSubDtos();
                 if(prpJlossPlanSubDtos==null || prpJlossPlanSubDtos.size()==0){
                	 errorMsg .append("发票绑定清单第").append(i).append("条价税拆分险别信息为空！");
                 }else{
                	 int j = 1;
                	 for(PrpJlossPlanSubVatVo prpJlossPlanSub:prpJlossPlanSubDtos){
                		 if (StringUtils.isBlank(prpJlossPlanSub.getKindCode())) {
                        	 errorMsg .append("发票绑定清单第").append(i).append("条中的价税拆分险别信息第").append(j).append("条条款代码为空！");
                         }
                		 if (prpJlossPlanSub.getPlanFee() == null) {
                        	 errorMsg .append("发票绑定清单第").append(i).append("条中的价税拆分险别信息第").append(j).append("条收付金额为空！");
                         }
                		 if (prpJlossPlanSub.getSumAmountNT() == null) {
                        	 errorMsg .append("发票绑定清单第").append(i).append("条中的价税拆分险别信息第").append(j).append("条不含税金额为空！");
                         }
                		 if (prpJlossPlanSub.getTaxRate() == null) {
                        	 errorMsg .append("发票绑定清单第").append(i).append("条中的价税拆分险别信息第").append(j).append("条税率为空！");
                         }
                		 j++;
                	 }
                 }
				
				i++;
			}
		}
		return errorMsg.toString();

	}
    
    /**
     * 记录收付推送的险别价税拆分数据
     */
	public Set<String> recordClaimKindPayTrace(String json,ResponseDto responseDto) {
		// 需要送再保的set集合
		Set<String> toReinsSet = null;
		if (PaymentConstants.RESP_SUCCESS.equals(responseDto.getResponseCode())) {
			// 价税拆分数据推送给理赔存储，送再保
			Gson gson = new Gson();

			// 将报文解析成理赔对象
			InvoiceTransReturnVo basePart = null;
			try {
				basePart = gson.fromJson(json, InvoiceTransReturnVo.class);
			} catch (Exception e) {

				logger.error("收付推送理赔价税拆分报文解析异常！报文：{} \n异常信息：{}", json, e);
			}

			try {
				toReinsSet = this.saveInvoiceVatRecord(basePart);

			} catch (Exception e) {

				logger.error("发票ID：" + basePart.getInvoiceId()
						+ "收付推送理赔价税拆分数据处理异常!" + " 收付报文：{}\n 理赔异常信息：{}", json, e);
			}
		}
		return toReinsSet;

	}
    
    /**
     * 存储收付推送的价税拆分数据
     * @param invoiceTransReturnVo
     */
    private Set<String> saveInvoiceVatRecord(InvoiceTransReturnVo invoiceTransReturnVo){
    	// 存储价税拆分数据,如果预赔先于核赔回写，则只存数，不推送再保
       
    	List<InvoiceDetailVo> prpJecdInvoiceDtoList = invoiceTransReturnVo.getPrpJecdInvoiceDtoList();
    	
    	//需要推送再保的记录，存放报案号+险种+次数，用#号分割
    	Set<String> needToReinsSet = new HashSet<String>();
    	
		if (prpJecdInvoiceDtoList != null && !prpJecdInvoiceDtoList.isEmpty()) {
			for (InvoiceDetailVo invoiceDetail : prpJecdInvoiceDtoList) {
				
				//根据registNo、riskCode、compensateNo、payeeId获取次数
				int times = getRecordTimes(invoiceDetail.getRegistNo(),invoiceDetail.getRiskCode(),invoiceDetail.getCertiNo(),invoiceDetail.getPayeeId());
				
				String toReinsKeyWord = invoiceDetail.getRegistNo()+"#"+invoiceDetail.getRiskCode()+"#"+times;
				if(invoiceDetail.getCertiNo().startsWith("Y")){
					//需要判断是否有核赔，通过查询首次推送再保的拆分轨迹来判断
					if(isUnderWrite(invoiceDetail.getCertiNo())){
						needToReinsSet.add(toReinsKeyWord);
					}
				}else{
				   needToReinsSet.add(toReinsKeyWord);
				}
				
				
				List<PrpJlossPlanSubVatVo> prpJlossPlanSubDtos = invoiceDetail.getPrpJlossPlanSubDtos();
				
				if(prpJlossPlanSubDtos != null && !prpJlossPlanSubDtos.isEmpty()){
					for(PrpJlossPlanSubVatVo prpJlossPlanSubVat:prpJlossPlanSubDtos){
						PrplPayeeKindPayment payeeKindPayment  = new PrplPayeeKindPayment();
						payeeKindPayment.setCompensateNo(prpJlossPlanSubVat.getCertiNo());
						// 预赔需要存储lossId
						if(invoiceDetail.getCertiNo().startsWith("Y")){
							List<PrpLPrePayVo> prpLPrePays = this.compensateService.queryPrePay(invoiceDetail.getCertiNo());
							if(!prpLPrePays.isEmpty()){
								PrpLPrePayVo prpLPrePay =prpLPrePays.get(0);
								String lossType = prpLPrePay.getLossType();
                                if (org.apache.commons.lang.StringUtils.isNotBlank(lossType)) {
                                    int i = lossType.lastIndexOf("_");
                                    if (i != -1) {
                                        String lossId = lossType.substring(i + 1);
                                        try {
                                            payeeKindPayment.setLossId(Long.parseLong(lossId));
                                        } catch (NumberFormatException e) {
                                            logger.error("PrpLPrePay lossType 截取到的lossId非数字类型", e);
                                        }
                                    }

                                }
							}
						}
						payeeKindPayment.setPayeeId(Long.parseLong(invoiceDetail.getPayeeId()));
						payeeKindPayment.setRegistNo(invoiceDetail.getRegistNo());
						payeeKindPayment.setRiskCode(invoiceDetail.getRiskCode());
						payeeKindPayment.setKindCode(prpJlossPlanSubVat.getKindCode());
						payeeKindPayment.setTimes(Long.parseLong(times+""));
						if("1".equals(invoiceDetail.getLossType())){//费用
						  payeeKindPayment.setChargeCode(CodeConstants.backFeeType(invoiceDetail.getChargeCode()));
						}
						payeeKindPayment.setKindSumPay(prpJlossPlanSubVat.getPlanFee());
						payeeKindPayment.setAddTaxRate(prpJlossPlanSubVat.getTaxRate().toString());
						payeeKindPayment.setAddTaxValue(prpJlossPlanSubVat.getSumAmountTax());
						payeeKindPayment.setNoTaxValue(prpJlossPlanSubVat.getSumAmountNT());
						payeeKindPayment.setToReinsFlag(YN01.N);
						payeeKindPayment.setCreateTime(new Date());
						payeeKindPayment.setUpdatetime(new Date());
						this.databaseDao.save(PrplPayeeKindPayment.class, payeeKindPayment);
					} 
				}
			}
			
		}
		return needToReinsSet;
    }
    
    /**
     * 判断是否存在核赔时存储的价税拆分记录
     * @param compensateNo
     * @return
     */
	private boolean isUnderWrite(String compensateNo) {
		if (compensateNo != null && !"".equals(compensateNo)) {
			List<Object> paramValues = new ArrayList<Object>();
			StringBuffer queryString = new StringBuffer(" select count(1) from claimuser.prplpayeekindpayment p where p.times = 0 ");

			queryString.append(" and p.compensateno = ?");
			paramValues.add(compensateNo.trim());

			Object singleObj = null;
			try {
				singleObj = baseDaoService.findListBySql(queryString.toString(), paramValues.toArray()).get(0);
			} catch (Exception e) {

				logger.error("根据计算书号获取记录存储次数异常!" + " 执行sql：{}\n 异常信息：{}",queryString.toString(), e);
			}
			if (singleObj != null) {
				int countResult = (Integer) singleObj;
				if (countResult > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		   return false;
		
	}

	/**
	 * 根据条件获取记录的次数
	 * 
	 * @param registNo
	 * @param riskCode
	 * @param compensateNo
	 * @param payeeId
	 * @return
	 */
	private int getRecordTimes(String registNo, String riskCode,String compensateNo, String payeeId) {
		List<Object> paramValues = new ArrayList<Object>();
		StringBuffer queryString = new StringBuffer(" select nvl(max(p.times),0)+1 from claimuser.prplpayeekindpayment p where 1=1 ");
		if(!registNo.isEmpty()){
			queryString.append(" and p.registno = ?");
			paramValues.add(registNo.trim());
		}
		if(!riskCode.isEmpty()){
			queryString.append(" and p.riskCode = ?");
			paramValues.add(riskCode.trim());
		}
		if(!compensateNo.isEmpty()){
			queryString.append(" and p.compensateno = ?");
			paramValues.add(compensateNo.trim());
		}
		if(!payeeId.isEmpty()){
			queryString.append(" and p.payeeid = ? ");
			paramValues.add(payeeId.trim());
		}
	
		Object singleObj = null;
		try {
			singleObj = baseDaoService.findListBySql(queryString.toString(),paramValues.toArray()).get(0);
		} catch (Exception e) {
			
			logger.error("获取记录存储次数异常!" + " 执行sql：{}\n 异常信息：{}", queryString.toString(), e);
		}
		if (singleObj != null) {
			BigDecimal valueResult = (BigDecimal)singleObj;
			return Integer.parseInt(valueResult.toString()) ;
		} else {
			return 1;
		}
		
	}
	
	/**
	 * 推送再保
	 * @param needToReinsSet 需要送再保数据的key集合
	 * @param toReinsMap 数据集合
	 */
	private void sendVatBackSumAmountNTToReins(Set<String> needToReinsSet,Map<String,List<PrplPayeeKindPaymentVo>> toReinsMap){
		if (!needToReinsSet.isEmpty()) {
			for (String keyWord : needToReinsSet) {
				List<PrplPayeeKindPaymentVo> casePayeeKindPayments = toReinsMap.get(keyWord);
				verifyClaimService.sendVatBackSumAmountNTToReins(keyWord,casePayeeKindPayments);
			}
		}
	}
	
	/**
	 * 推送再保
	 * @param needToReinsSet
	 */
	public void sendVatBackSumAmountNTToReins(Set<String> needToReinsSet){
		if (!needToReinsSet.isEmpty()) {
			for (String keyWord : needToReinsSet) {
				String registNo = keyWord.split("#")[0];
				String riskCode = keyWord.split("#")[1];
				verifyClaimService.sendVatBackSumAmountNTToReins(registNo, riskCode);
			}
		}
	}
}
