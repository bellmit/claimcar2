package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BaseInfo")
public class CarRiskCaseWriteReqBasePartVo {

    @XStreamAlias("ClaimSequenceNo")
	private String ClaimSequenceNo;// 理赔编号

    @XStreamAlias("ClaimType")
	private String claimType;//案件类型
	
    @XStreamAlias("Remark")
    private String remark;//备注

    
    public String getClaimSequenceNo() {
        return ClaimSequenceNo;
    }

    
    public void setClaimSequenceNo(String claimSequenceNo) {
        ClaimSequenceNo = claimSequenceNo;
    }

    
    public String getClaimType() {
        return claimType;
    }

    
    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }
	

}
