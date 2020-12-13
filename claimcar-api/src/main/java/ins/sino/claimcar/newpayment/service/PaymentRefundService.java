package ins.sino.claimcar.newpayment.service;

import ins.sino.claimcar.newpayment.vo.ResponseDto;

/**
 * 收付退票理赔处理类
 *
 * @author maofengning
 * @date 2020/5/16 11:52
 */
public interface PaymentRefundService {

    ResponseDto paymentRefund(String json) throws Exception;
}
