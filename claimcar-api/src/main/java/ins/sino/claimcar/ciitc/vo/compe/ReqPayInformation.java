package ins.sino.claimcar.ciitc.vo.compe;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ReqPayInformation")
public class ReqPayInformation  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("acciNo") 
	private String  acciNo; 
	
	@XStreamAlias("acciAreaCode") 
	private String  acciAreaCode; 

	@XStreamAlias("reportType") 
	private String  reportType; 

	@XStreamAlias("riskTypeCode") 
	private String  riskTypeCode; 

	@XStreamAlias("policyNo") 
	private String  policyNo; 

	@XStreamAlias("traAcciNo") 
	private String  traAcciNo; 

	@XStreamAlias("traAcciTime") 
	private String  traAcciTime; 

	@XStreamAlias("payMoney") 
	private String  payMoney; 

	@XStreamAlias("payTime") 
	private String  payTime;

	public String getAcciNo() {
		return acciNo;
	}

	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}

	public String getAcciAreaCode() {
		return acciAreaCode;
	}

	public void setAcciAreaCode(String acciAreaCode) {
		this.acciAreaCode = acciAreaCode;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getRiskTypeCode() {
		return riskTypeCode;
	}

	public void setRiskTypeCode(String riskTypeCode) {
		this.riskTypeCode = riskTypeCode;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getTraAcciNo() {
		return traAcciNo;
	}

	public void setTraAcciNo(String traAcciNo) {
		this.traAcciNo = traAcciNo;
	}

	public String getTraAcciTime() {
		return traAcciTime;
	}

	public void setTraAcciTime(String traAcciTime) {
		this.traAcciTime = traAcciTime;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	} 
	
	
}
