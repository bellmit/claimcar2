package ins.sino.claimcar.newpayment.service;

import ins.sino.claimcar.newpayment.vo.ResponseDto;

/**
 * 收付推送数据更新理赔结算单号
 *
 * @author maofengning
 * @date 2020/5/18 19:53
 */
public interface UpdateClaimSettleNoService {
    ResponseDto updateClaimSettleNo(String json) throws Exception;
}
