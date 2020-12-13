package ins.sino.claimcar.newpayment.service;

import ins.sino.claimcar.newpayment.vo.ResponseDto;

/**
 * 收付回写理赔
 *
 * @author maofengning
 * @date 2020/5/16 14:26
 */
public interface PaymentWriteBackService {

    ResponseDto writeBackPaymentToClaim(String json) throws Exception;
}
