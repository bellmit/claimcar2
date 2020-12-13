package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("CASECARSTATE")
public class casecarStateVo {
	@XStreamAlias("INSCASENO")
	private String inscaseNo;//保险报案号
	@XStreamAlias("CASECARTYPE")
	private String casecarType;//涉案车辆类型
	@XStreamAlias("CASECARNO")
	private String casecarNo;//案件车牌号
	@XStreamAlias("FEEPAYMONEY")
	private String feepayMoney;//自助定损金额
	@XStreamAlias("STATE")
	private String state;//案件状态
	public String getInscaseNo() {
		return inscaseNo;
	}
	public void setInscaseNo(String inscaseNo) {
		this.inscaseNo = inscaseNo;
	}
	public String getCasecarType() {
		return casecarType;
	}
	public void setCasecarType(String casecarType) {
		this.casecarType = casecarType;
	}
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
    
}
