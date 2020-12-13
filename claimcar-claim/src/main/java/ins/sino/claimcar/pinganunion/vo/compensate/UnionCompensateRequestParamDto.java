package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;

/**
 * 平安联盟中心-理算查询接口-查询参数类
 *
 * @author mfn
 * @date 2020/7/29 16:04
 */
public class UnionCompensateRequestParamDto implements Serializable {
    private static final long serialVersionUID = 379921517018966581L;

    /**  案件号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;
    /**  批次号  批次号和赔付类型子次数二选一  是否非空：CY  编码： */
    private String idClmBatch;
    /**  赔付类型  代码定义3.14  是否非空：CY  编码： */
    private String claimType;
    /**  子次数    是否非空：CY  编码： */
    private String subTimes;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Integer getCaseTimes() {
        return caseTimes;
    }

    public void setCaseTimes(Integer caseTimes) {
        this.caseTimes = caseTimes;
    }

    public String getIdClmBatch() {
        return idClmBatch;
    }

    public void setIdClmBatch(String idClmBatch) {
        this.idClmBatch = idClmBatch;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getSubTimes() {
        return subTimes;
    }

    public void setSubTimes(String subTimes) {
        this.subTimes = subTimes;
    }
}
