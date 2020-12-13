package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;

/**
 * 平安联盟中心-理算查询接口请求类
 *
 * @author mfn
 * @date 2020/8/6 10:53
 */
public class UnionCompensateRequestDto implements Serializable {
    private static final long serialVersionUID = 6872030137341360780L;

    /**  保险公司  密钥文件提供  是否非空：Y  编码：Y */
    private String insuranceCompanyNo;
    /**  操作员UM  记录操作人UM  是否非空：Y  编码：N */
    private String operatorUm;
    /**  查询入参数据    是否非空：Y  编码：N */
    private UnionCompensateRequestParamDto paramObj;

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

    public UnionCompensateRequestParamDto getParamObj() {
        return paramObj;
    }

    public void setParamObj(UnionCompensateRequestParamDto paramObj) {
        this.paramObj = paramObj;
    }
}
