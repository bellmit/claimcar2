package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.util.Date;

public class RecPayLaunchVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String claimNo; //立案号
	private String policyNo;//保单号
	private String registNo;//报案号
	private Date endCaseDateStart;//流入时间
	private Date endCaseDateEnd;//流出时间
	private String comCode;//归属机构
	private String mercyFlag;//案件紧急程度
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getMercyFlag() {
		return mercyFlag;
	}
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}
	public Date getEndCaseDateStart() {
		return endCaseDateStart;
	}
	public void setEndCaseDateStart(Date endCaseDateStart) {
		this.endCaseDateStart = endCaseDateStart;
	}
	public Date getEndCaseDateEnd() {
		return endCaseDateEnd;
	}
	public void setEndCaseDateEnd(Date endCaseDateEnd) {
		this.endCaseDateEnd = endCaseDateEnd;
	}
	@Override
	public String toString() {
		return "RecPayLaunchVo [claimNo=" + claimNo + ", policyNo=" + policyNo
				+ ", registNo=" + registNo + ", endCaseDateStart="
				+ endCaseDateStart + ", endCaseDateEnd=" + endCaseDateEnd
				+ ", comCode=" + comCode + ", mercyFlag=" + mercyFlag + "]";
	}
	
	
	
	
}
