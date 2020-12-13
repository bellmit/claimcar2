package ins.sino.claimcar.claim.vo;


public class DeductCondInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private String clauseType;
	/** 免赔条件代码1*/
	private String codeOne;
	/** 免赔条件名称1*/
	private String nameOne;
	/** 免赔条件名称1是否选中*/
	private String validFalgOne;
	/** 免赔条件1是否有次数  0:无、1：有*/
	private String timeFlagOne;
	private String policyNoOne;
	private String policyNoTwo;
	/** 免赔条件代码2*/
	private String codeTwo;
	/** 免赔条件名称2*/
	private String nameTwo;
	/** 免赔条件名称2是否选中*/
	private String validFalgTwo;
	/** 免赔条件2是否有次数  0:无、1：有*/
	private String timeFlagTwo;
	/** 赔付次数*/
	private String timesOne;
	/** 赔付次数*/
	private String timesTwo;
	
	public String getClauseType() {
		return clauseType;
	}
	
	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}
	
	public String getCodeOne() {
		return codeOne;
	}
	
	public void setCodeOne(String codeOne) {
		this.codeOne = codeOne;
	}
	
	public String getNameOne() {
		return nameOne;
	}
	
	public void setNameOne(String nameOne) {
		this.nameOne = nameOne;
	}
	
	public String getValidFalgOne() {
		return validFalgOne;
	}
	
	public void setValidFalgOne(String validFalgOne) {
		this.validFalgOne = validFalgOne;
	}
	
	public String getTimeFlagOne() {
		return timeFlagOne;
	}
	
	public void setTimeFlagOne(String timeFlagOne) {
		this.timeFlagOne = timeFlagOne;
	}
	
	public String getPolicyNoOne() {
		return policyNoOne;
	}
	
	public void setPolicyNoOne(String policyNoOne) {
		this.policyNoOne = policyNoOne;
	}
	
	public String getPolicyNoTwo() {
		return policyNoTwo;
	}
	
	public void setPolicyNoTwo(String policyNoTwo) {
		this.policyNoTwo = policyNoTwo;
	}
	
	public String getCodeTwo() {
		return codeTwo;
	}
	
	public void setCodeTwo(String codeTwo) {
		this.codeTwo = codeTwo;
	}
	
	public String getNameTwo() {
		return nameTwo;
	}
	
	public void setNameTwo(String nameTwo) {
		this.nameTwo = nameTwo;
	}
	
	public String getValidFalgTwo() {
		return validFalgTwo;
	}
	
	public void setValidFalgTwo(String validFalgTwo) {
		this.validFalgTwo = validFalgTwo;
	}
	
	public String getTimeFlagTwo() {
		return timeFlagTwo;
	}
	
	public void setTimeFlagTwo(String timeFlagTwo) {
		this.timeFlagTwo = timeFlagTwo;
	}
	
	public String getTimesOne() {
		return timesOne;
	}
	
	public void setTimesOne(String timesOne) {
		this.timesOne = timesOne;
	}
	
	public String getTimesTwo() {
		return timesTwo;
	}
	
	public void setTimesTwo(String timesTwo) {
		this.timesTwo = timesTwo;
	}

}
