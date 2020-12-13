package ins.sino.claimcar.pinganunion.vo.payCallback;

import java.io.Serializable;

/**
 * description: UnionPayCallbackRequestDto 平安联盟中心-支付结果回调请求类
 * date: 2020/8/18 8:40
 * author: lk
 * version: 1.0
 */
public class UnionPayCallbackRequestDto implements Serializable {
    private static final long serialVersionUID = -1537208455216609213L;
    /**
     * 保险公司  密钥文件提供  是否非空：Y  编码：Y
     */
    private String insuranceCompanyNo;
    /**
     * 操作员UM  记录操作人UM  是否非空：Y  编码：N
     */
    private String operatorUm;
    /**
     * 查询入参数据    是否非空：Y  编码：N
     */
    private UnionPayCallbackRequestParamDto paramObj;

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

    public UnionPayCallbackRequestParamDto getParamObj() {
        return paramObj;
    }

    public void setParamObj(UnionPayCallbackRequestParamDto paramObj) {
        this.paramObj = paramObj;
    }
}
