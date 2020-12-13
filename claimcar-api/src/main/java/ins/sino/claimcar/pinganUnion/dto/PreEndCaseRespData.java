package ins.sino.claimcar.pinganUnion.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 保单先结返回对象
 * @Author liuys
 * @Date 2020/8/4 8:59
 */
public class PreEndCaseRespData implements Serializable {
    //赔付次数
    private Integer caseTimes;
    //保单号
    private String policyNo;
    //赔案号
    private String caseNo;
    //案件状态 0-已结案 1-已报案 2-已理算
    private String caseStatus;
    //赔付结论 1-赔付 2-零结 3-拒赔 4-立案注销 5-报案注销
    private String indemnityConclusion;
    //结案时间
    private Date endCaseDate;
    //结案人
    private String caseFinisherUm;

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

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getIndemnityConclusion() {
        return indemnityConclusion;
    }

    public void setIndemnityConclusion(String indemnityConclusion) {
        this.indemnityConclusion = indemnityConclusion;
    }

    public Date getEndCaseDate() {
        return endCaseDate;
    }

    public void setEndCaseDate(Date endCaseDate) {
        this.endCaseDate = endCaseDate;
    }

    public String getCaseFinisherUm() {
        return caseFinisherUm;
    }

    public void setCaseFinisherUm(String caseFinisherUm) {
        this.caseFinisherUm = caseFinisherUm;
    }
}
