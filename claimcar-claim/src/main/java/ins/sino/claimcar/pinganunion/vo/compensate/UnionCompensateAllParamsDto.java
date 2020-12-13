package ins.sino.claimcar.pinganunion.vo.compensate;

import java.io.Serializable;
import java.util.List;

/**
 * 平安联盟中心-理算查询接口-通知下发接口下发参数
 *
 * @author mfn
 * @date 2020/8/6 10:07
 */
public class UnionCompensateAllParamsDto implements Serializable {
    private static final long serialVersionUID = 6700507601717398485L;

    /**  案件号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;
    /**  批次号  批次号和赔付类型子次数二选一  是否非空：CY  编码： */
    private String idClmBatch;
    /**  赔付类型  代码定义3.14  是否非空：CY  编码： */
    private String claimType;
    /**  子次数    是否非空：CY  编码： */
    private Integer subTimes;
    /** 支付信息主键列表		是否非空：Y	编码：N	 */
    private List<String> list;

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

    public Integer getSubTimes() {
        return subTimes;
    }

    public void setSubTimes(Integer subTimes) {
        this.subTimes = subTimes;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
