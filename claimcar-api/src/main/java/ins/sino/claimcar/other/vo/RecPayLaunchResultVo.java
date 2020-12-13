package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.util.Date;

public class RecPayLaunchResultVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mercyFlagName;  //案件紧急程度
	private String registNoHtml;   //报案号
	private String policyNoHtml;  //保单号
	private String claimNo;   //立案号
	private String policyType;  //保单类型
	private Date endTime;   //结束时间
	public String getMercyFlagName() {
		return mercyFlagName;
	}
	public void setMercyFlagName(String mercyFlagName) {
		this.mercyFlagName = mercyFlagName;
	}
	public String getRegistNoHtml() {
		return registNoHtml;
	}
	public void setRegistNoHtml(String registNoHtml) {
		this.registNoHtml = registNoHtml;
	}
	public String getPolicyNoHtml() {
		return policyNoHtml;
	}
	public void setPolicyNoHtml(String policyNoHtml) {
		this.policyNoHtml = policyNoHtml;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
