package ins.sino.claimcar.pinganunion.vo.padpay;

import java.io.Serializable;

/**
 * 平安联盟中心-垫付请求类
 *
 * @author mfn
 * @date 2020/7/28 18:07
 */
public class UnionPadPayRequestDto implements Serializable {
    private static final long serialVersionUID = 1676359330447692353L;

    /**  保险公司  密钥文件提供  是否非空：Y  编码：Y */
    private String insuranceCompanyNo;
    /**  操作员UM  记录操作人UM  是否非空：Y  编码：N */
    private String operatorUm;
    /**  查询入参数据    是否非空：Y  编码：N */
    private UnionPadPayRequestParamDto paramObj;

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

    public UnionPadPayRequestParamDto getParamObj() {
        return paramObj;
    }

    public void setParamObj(UnionPadPayRequestParamDto paramObj) {
        this.paramObj = paramObj;
    }
}
