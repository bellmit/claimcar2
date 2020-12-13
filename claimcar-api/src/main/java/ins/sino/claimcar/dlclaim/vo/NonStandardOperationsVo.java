package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

public class NonStandardOperationsVo implements Serializable{
private static final long serialVersionUID = 1L;

private String factCode;//序号
private String riskDesc;//风险描述
private String suggest;//检测建议
public String getFactCode() {
	return factCode;
}
public void setFactCode(String factCode) {
	this.factCode = factCode;
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
