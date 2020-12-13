package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceClaimVo implements Serializable{
	private static final long serialVersionUID = 1L;
    private String COMPANY_ID;//公司代码
    private String CLAIM_CODE;//理赔编码
    private String CONFIRM_SEQUENCE_NO;//投保确认码
    private String POLICY_NO;//保单号码
    private String REPORT_TIME;//报案时间
    private String REPORT_NO;//报案号
    private String REPORTER_NAME;//报案人姓名
    private String ACCIDENT_TIME;//出险时间
    private String ACCIDENT_PLACE;//出险地点
    private String ACCIDENT_DESCRIPTION;//出险经过
    private String ACCIDENT_CAUSE;//出险原因代码
    private String ACCIDENT_LIABILITY;//事故责任划分代码
    private String MANAGE_TYPE;//事故处理方式代码
    private String REGISTRATION_NO;//立案号
    private String REGISTRATION_TIME;//立案时间
    private String ESTIMATE_AMOUNT;//估损金额
    private String CLAIM_TYPE;//理赔类型代码
    private String ENDCASE_DATE;//结案时间
    private String CLAIM_AMOUNT;//赔款总金额
    private String ACCIDENT_DEATH;//是否发生有责交通死亡事故
    private String PAY_SELF_FLAG;//互碰自赔标志
    private String ACCIDENT_TYPE;//保险事故类型
    private String CITY_CODE;//保单归属地（地市）
    private String COUNTY_CODE;//保单归属地（区县）
    private String REOPEN_CASE;//是否重开赔案
	public String getCOMPANY_ID() {
		return COMPANY_ID;
	}
	public void setCOMPANY_ID(String cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}
	public String getCLAIM_CODE() {
		return CLAIM_CODE;
	}
	public void setCLAIM_CODE(String cLAIM_CODE) {
		CLAIM_CODE = cLAIM_CODE;
	}
	public String getCONFIRM_SEQUENCE_NO() {
		return CONFIRM_SEQUENCE_NO;
	}
	public void setCONFIRM_SEQUENCE_NO(String cONFIRM_SEQUENCE_NO) {
		CONFIRM_SEQUENCE_NO = cONFIRM_SEQUENCE_NO;
	}
	public String getPOLICY_NO() {
		return POLICY_NO;
	}
	public void setPOLICY_NO(String pOLICY_NO) {
		POLICY_NO = pOLICY_NO;
	}
	public String getREPORT_TIME() {
		return REPORT_TIME;
	}
	public void setREPORT_TIME(String rEPORT_TIME) {
		REPORT_TIME = rEPORT_TIME;
	}
	public String getREPORT_NO() {
		return REPORT_NO;
	}
	public void setREPORT_NO(String rEPORT_NO) {
		REPORT_NO = rEPORT_NO;
	}
	public String getREPORTER_NAME() {
		return REPORTER_NAME;
	}
	public void setREPORTER_NAME(String rEPORTER_NAME) {
		REPORTER_NAME = rEPORTER_NAME;
	}
	public String getACCIDENT_TIME() {
		return ACCIDENT_TIME;
	}
	public void setACCIDENT_TIME(String aCCIDENT_TIME) {
		ACCIDENT_TIME = aCCIDENT_TIME;
	}
	public String getACCIDENT_PLACE() {
		return ACCIDENT_PLACE;
	}
	public void setACCIDENT_PLACE(String aCCIDENT_PLACE) {
		ACCIDENT_PLACE = aCCIDENT_PLACE;
	}
	public String getACCIDENT_DESCRIPTION() {
		return ACCIDENT_DESCRIPTION;
	}
	public void setACCIDENT_DESCRIPTION(String aCCIDENT_DESCRIPTION) {
		ACCIDENT_DESCRIPTION = aCCIDENT_DESCRIPTION;
	}
	public String getACCIDENT_CAUSE() {
		return ACCIDENT_CAUSE;
	}
	public void setACCIDENT_CAUSE(String aCCIDENT_CAUSE) {
		ACCIDENT_CAUSE = aCCIDENT_CAUSE;
	}
	public String getACCIDENT_LIABILITY() {
		return ACCIDENT_LIABILITY;
	}
	public void setACCIDENT_LIABILITY(String aCCIDENT_LIABILITY) {
		ACCIDENT_LIABILITY = aCCIDENT_LIABILITY;
	}
	public String getMANAGE_TYPE() {
		return MANAGE_TYPE;
	}
	public void setMANAGE_TYPE(String mANAGE_TYPE) {
		MANAGE_TYPE = mANAGE_TYPE;
	}
	public String getREGISTRATION_NO() {
		return REGISTRATION_NO;
	}
	public void setREGISTRATION_NO(String rEGISTRATION_NO) {
		REGISTRATION_NO = rEGISTRATION_NO;
	}
	public String getREGISTRATION_TIME() {
		return REGISTRATION_TIME;
	}
	public void setREGISTRATION_TIME(String rEGISTRATION_TIME) {
		REGISTRATION_TIME = rEGISTRATION_TIME;
	}
	public String getESTIMATE_AMOUNT() {
		return ESTIMATE_AMOUNT;
	}
	public void setESTIMATE_AMOUNT(String eSTIMATE_AMOUNT) {
		ESTIMATE_AMOUNT = eSTIMATE_AMOUNT;
	}
	public String getCLAIM_TYPE() {
		return CLAIM_TYPE;
	}
	public void setCLAIM_TYPE(String cLAIM_TYPE) {
		CLAIM_TYPE = cLAIM_TYPE;
	}
	public String getENDCASE_DATE() {
		return ENDCASE_DATE;
	}
	public void setENDCASE_DATE(String eNDCASE_DATE) {
		ENDCASE_DATE = eNDCASE_DATE;
	}
	public String getCLAIM_AMOUNT() {
		return CLAIM_AMOUNT;
	}
	public void setCLAIM_AMOUNT(String cLAIM_AMOUNT) {
		CLAIM_AMOUNT = cLAIM_AMOUNT;
	}
	public String getACCIDENT_DEATH() {
		return ACCIDENT_DEATH;
	}
	public void setACCIDENT_DEATH(String aCCIDENT_DEATH) {
		ACCIDENT_DEATH = aCCIDENT_DEATH;
	}
	public String getPAY_SELF_FLAG() {
		return PAY_SELF_FLAG;
	}
	public void setPAY_SELF_FLAG(String pAY_SELF_FLAG) {
		PAY_SELF_FLAG = pAY_SELF_FLAG;
	}
	public String getACCIDENT_TYPE() {
		return ACCIDENT_TYPE;
	}
	public void setACCIDENT_TYPE(String aCCIDENT_TYPE) {
		ACCIDENT_TYPE = aCCIDENT_TYPE;
	}
	public String getCITY_CODE() {
		return CITY_CODE;
	}
	public void setCITY_CODE(String cITY_CODE) {
		CITY_CODE = cITY_CODE;
	}
	public String getCOUNTY_CODE() {
		return COUNTY_CODE;
	}
	public void setCOUNTY_CODE(String cOUNTY_CODE) {
		COUNTY_CODE = cOUNTY_CODE;
	}
	public String getREOPEN_CASE() {
		return REOPEN_CASE;
	}
	public void setREOPEN_CASE(String rEOPEN_CASE) {
		REOPEN_CASE = rEOPEN_CASE;
	}
    
    
}
