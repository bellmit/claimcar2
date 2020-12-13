package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASECARFEE")
public class CasecarFeeVo {
	@XStreamAlias("CASECARTYPE")
	private String casecarType;//车辆类型，1标的,3三者
	@XStreamAlias("CASECARNO")
	private String casecarNo;//案件车牌号
	@XStreamAlias("FEEPAYMONEY")
	private String feepayMoney;//定损金额
	@XStreamAlias("FEETIME")
	private String feeTime;//定损时间
	@XStreamAlias("FEEINSTRUCTIONS")
	private String feeinstrucTions;//定损意见
	public String getCasecarNo() {
		return casecarNo;
	}
	public void setCasecarNo(String casecarNo) {
		this.casecarNo = casecarNo;
	}
	public String getFeepayMoney() {
		return feepayMoney;
	}
	public void setFeepayMoney(String feepayMoney) {
		this.feepayMoney = feepayMoney;
	}
	public String getFeeTime() {
		return feeTime;
	}
	public void setFeeTime(String feeTime) {
		this.feeTime = feeTime;
	}
	public String getFeeinstrucTions() {
		return feeinstrucTions;
	}
	public void setFeeinstrucTions(String feeinstrucTions) {
		this.feeinstrucTions = feeinstrucTions;
	}
	public String getCasecarType() {
		return casecarType;
	}
	public void setCasecarType(String casecarType) {
		this.casecarType = casecarType;
	}
	
	

}
