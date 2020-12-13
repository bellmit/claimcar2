package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/*操作不规范提示列表*/
@XStreamAlias("NonStandardOperation")
public class NonStandardOperationVo {
	/*规则编码*/
	@XStreamAlias("FactCode")
	private String factCode;
	/*规则名称*/
	@XStreamAlias("FactName")
	private String factName;
	/*风险描述*/
	@XStreamAlias("RiskDesc")
	private String riskDesc;
	/*检测建议*/
	@XStreamAlias("Suggest")
	private String suggest;
	public String getFactCode() {
		return factCode;
	}
	public void setFactCode(String factCode) {
		this.factCode = factCode;
	}
	public String getFactName() {
		return factName;
	}
	public void setFactName(String factName) {
		this.factName = factName;
	}
	public String getRiskDesc() {
		return riskDesc;
	}
	public void setRiskDesc(String riskDesc) {
		this.riskDesc = riskDesc;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
}
