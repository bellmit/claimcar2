package ins.sino.claimcar.newpayment.service;

import java.util.Set;

import ins.sino.claimcar.newpayment.vo.ResponseDto;

/**
 * 更新理赔价税数据
 *
 * @author maofengning
 * @date 2020/5/19 19:39
 */
public interface UpdateClaimTaxService {

    ResponseDto updateClaimTax(String json);
    
    /**
     * 校验vat推送过来的数据
     * @param json
     * @return
     */
    ResponseDto verifyVatBackClaimKindPay(String json);
    
    /**
     * 保存推送的数据
     * @param json
     * @param responseDto
     * @return
     */
    Set<String> recordClaimKindPayTrace(String json, ResponseDto responseDto);
    
    /**
	 * 推送再保
	 * @param needToReinsSet
	 */
	void sendVatBackSumAmountNTToReins(Set<String> needToReinsSet);
    
	
}
