package ins.sino.claimcar.pinganunion.vo.payment;

import java.io.Serializable;
import java.util.List;

/**
 * 平安联盟中心-支付信息查询接口-请求参数类
 *
 * @author mfn
 * @date 2020/8/6 11:08
 */
public class UnionPaymentRequestParamDto implements Serializable {
    private static final long serialVersionUID = 7813530646974864868L;

    /**  支付信息主键列表    是否非空：Y  编码：N */
    private List<String> list;
    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

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
}
