package ins.sino.claimcar.carchild.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PERSONINFO")
public class CTPersonInfoVo  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PERSONID")
	private String personId; //理赔人伤ID
	
	@XStreamAlias("ISADJUST")
	private String isAdjust; //是否现场调解
	
	@XStreamAlias("LOSSTYPE")
	private String losstype; //损失方
	
	@XStreamAlias("NAME")
	private String name; //姓名
	
	@XStreamAlias("SEX")
	private String sex; //性别
	
	@XStreamAlias("CERTITYPE")
	private String certiType; //证件类型
	
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber; //证件号码
	
	@XStreamAlias("AGE")
	private String age; //年龄
	
	@XStreamAlias("DEGREE")
	private String degree; //伤情类别
	
	@XStreamAlias("SURVEYTYPE")
	private String surveyType; //查勘处理方式
	
	@XStreamAlias("INDUSTRY")
	private String industry; //从事行业
	
	@XStreamAlias("THERAPEUTICAGENCY")
	private String therapeuticagency; //治疗机构
	
	@XStreamAlias("SUMCLAIM")
	private String sumClaim; //估损金额
	
	@XStreamAlias("DEGREEDESC")
	private String degreedesc; //伤情描述
	
	@XStreamAlias("PERSONATTRIBUTES")
	private String personAttributes; //人员属性
	
	@XStreamAlias("INJUREDPART")
	private String injuredPart; //受伤部位

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getIsAdjust() {
		return isAdjust;
	}

	public void setIsAdjust(String isAdjust) {
		this.isAdjust = isAdjust;
	}

	public String getLosstype() {
		return losstype;
	}

	public void setLosstype(String losstype) {
		this.losstype = losstype;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getTherapeuticagency() {
		return therapeuticagency;
	}

	public void setTherapeuticagency(String therapeuticagency) {
		this.therapeuticagency = therapeuticagency;
	}

	public String getSumClaim() {
		return sumClaim;
	}

	public void setSumClaim(String sumClaim) {
		this.sumClaim = sumClaim;
	}

	public String getDegreedesc() {
		return degreedesc;
	}

	public void setDegreedesc(String degreedesc) {
		this.degreedesc = degreedesc;
	}

	public String getPersonAttributes() {
		return personAttributes;
	}

	public void setPersonAttributes(String personAttributes) {
		this.personAttributes = personAttributes;
	}

	public String getInjuredPart() {
		return injuredPart;
	}

	public void setInjuredPart(String injuredPart) {
		this.injuredPart = injuredPart;
	}

	public String getSurveyType() {
		return surveyType;
	}

	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}
}
