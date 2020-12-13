package ins.sino.claimcar.pinganunion.vo.payment;

import ins.sino.claimcar.pinganunion.vo.compensate.UnionCompensateRequestParamDto;

import java.io.Serializable;

/**
 * 平安联盟中心-支付信息查询接口-请求类
 *
 * @author mfn
 * @date 2020/8/6 11:06
 */
public class UnionPaymentRequestDto implements Serializable {
    private static final long serialVersionUID = -6439034569636823696L;

    /**  保险公司  密钥文件提供  是否非空：Y  编码：Y */
    private String insuranceCompanyNo;
    /**  操作员UM  记录操作人UM  是否非空：Y  编码：N */
    private String operatorUm;
    /**  查询入参数据    是否非空：Y  编码：N */
    private UnionPaymentRequestParamDto paramObj;

    public String getInsuranceCompanyNo() {
        return insuranceCompanyNo;
    }

    public void setInsuranceCompanyNo(String insuranceCompanyNo) {
        this.insuranceCompanyNo = insuranceCompanyNo;
    }

    public String getOperatorUm() {
        return operatorUm;
    }

    public void setOperatorUm(String operatorUm) {
        this.operatorUm = operatorUm;
    }

    public UnionPaymentRequestParamDto getParamObj() {
        return paramObj;
    }

    public void setParamObj(UnionPaymentRequestParamDto paramObj) {
        this.paramObj = paramObj;
    }
}
