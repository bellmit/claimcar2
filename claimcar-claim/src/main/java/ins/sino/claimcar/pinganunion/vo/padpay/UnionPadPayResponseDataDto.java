package ins.sino.claimcar.pinganunion.vo.padpay;

import java.io.Serializable;
import java.util.List;

/**
 * 垫付信息查询接口-返回数据
 *
 * @author mfn
 * @date 2020/7/22 11:17
 */
public class UnionPadPayResponseDataDto implements Serializable {
    private static final long serialVersionUID = -9182209918326623470L;

    /**  报案号    是否非空：Y  编码：N */
    private String reportNo;
    /**  赔付次数    是否非空：Y  编码：N */
    private Integer caseTimes;
    /**  交强险保单号    是否非空：Y  编码：N */
    private String policyNo;
    /**  垫付次数    是否非空：Y  编码：N */
    private Integer advanceTimes;
    /**  垫付状态  (0-暂存1-垫付待审批 2-审批同意 3-垫付退回 6-退回后放弃(垫付作废))  是否非空：Y  编码：Y */
    private String advanceStatus;
    /**  垫付申请人UM    是否非空：Y  编码：N */
    private String applyUserUm;
    /**  垫付申请时间    是否非空：Y  编码：N */
    private String applyTime;
    /**  是否责任内垫付  (Y:责任内垫付 N:责任外垫付)  是否非空：Y  编码：Y */
    private String isAdvanceOnDuty;
    /**  责任外垫付原因  (1-无证驾驶 2-驾驶证有效期已满 3-驾驶车型与驾驶证准驾不符 4-醉酒驾驶 5-被盗抢期间肇事 6-被保险人故意行为)  是否非空：N  编码：Y */
    private String advanceReason;
    /**  数据来源  申请来源(1-PC端,2-E理赔,3-小程序,4-好车主)  是否非空：N  编码：Y */
    private String dataSource;
    /**  垫付人伤信息列表    是否非空：N  编码：N */
    private List<UnionPadPayAdvancePersonDto> advancePersonList;

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

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Integer getAdvanceTimes() {
        return advanceTimes;
    }

    public void setAdvanceTimes(Integer advanceTimes) {
        this.advanceTimes = advanceTimes;
    }

    public String getAdvanceStatus() {
        return advanceStatus;
    }

    public void setAdvanceStatus(String advanceStatus) {
        this.advanceStatus = advanceStatus;
    }

    public String getApplyUserUm() {
        return applyUserUm;
    }

    public void setApplyUserUm(String applyUserUm) {
        this.applyUserUm = applyUserUm;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getIsAdvanceOnDuty() {
        return isAdvanceOnDuty;
    }

    public void setIsAdvanceOnDuty(String isAdvanceOnDuty) {
        this.isAdvanceOnDuty = isAdvanceOnDuty;
    }

    public String getAdvanceReason() {
        return advanceReason;
    }

    public void setAdvanceReason(String advanceReason) {
        this.advanceReason = advanceReason;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<UnionPadPayAdvancePersonDto> getAdvancePersonList() {
        return advancePersonList;
    }

    public void setAdvancePersonList(List<UnionPadPayAdvancePersonDto> advancePersonList) {
        this.advancePersonList = advancePersonList;
    }
}
