package ins.sino.claimcar.interf.vo;

import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

@XStreamAlias("ClaimMain")
public class ClaimMainVo {

    /*** ������� */
    @XStreamAlias("CompensateNo")
    private String compensateNo;
    /*** ������ */
    @XStreamAlias("ClaimNo")
    private String claimNo;
    /*** ������ */
    @XStreamAlias("RegistNo")
    private String registNo;
    /*** ������ */
    @XStreamAlias("PolicyNo")
    private String policyNo;
    /*** ����ͨ��ʱ�� */
    @XStreamConverter(value=SinosoftDateConverter.class,strings={"yyyy-MM-dd"})
    @XStreamAlias("UnderWriteEndDate")
    private Date underWriteEndDate;
    /*** �᰸ʱ�� */
    @XStreamConverter(value=SinosoftDateConverter.class,strings={"yyyy-MM-dd"})
    @XStreamAlias("EndCaseDate")
    private Date endCaseDate;
    /*** ���ִ��� */
    @XStreamAlias("RiskCode")
    private String riskCode;
    /*** ����� */
    @XStreamAlias("ComCode")
    private String comCode;
    /*** ����Ա */
    @XStreamAlias("OperatorCode")
    private String operatorCode;
    
    public String getClaimNo() {
        return claimNo;
    }
    
    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }
    
    public Date getUnderWriteEndDate() {
        return underWriteEndDate;
    }
    
    public void setUnderWriteEndDate(Date underWriteEndDate) {
        this.underWriteEndDate = underWriteEndDate;
    }
    
    public String getRiskCode() {
        return riskCode;
    }
    
    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }
    
    public String getComCode() {
        return comCode;
    }
    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }
    
    public String getOperatorCode() {
        return operatorCode;
    }
    
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public Date getEndCaseDate() {
        return endCaseDate;
    }

    
    public void setEndCaseDate(Date endCaseDate) {
        this.endCaseDate = endCaseDate;
    }

    
    public String getCompensateNo() {
        return compensateNo;
    }

    
    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }
}
