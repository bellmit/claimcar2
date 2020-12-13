package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 人员信息
 * @author j2eel
 *
 */
@XStreamAlias("PERSONINFO")
public class PersonInfoVos implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PERSONID")
	private String personId;            //人员ID
	@XStreamAlias("OPERATIONTYPE")
	private String operationType;         //操作类型
	@XStreamAlias("CASETYPE")
	private String caseType;			//案件类型
	@XStreamAlias("NAME")
	private String name;				//伤者姓名
	@XStreamAlias("CREDENTIALSTYPE")
	private String credentialsType;		//证件类型
	@XStreamAlias("CREDENTIALSNO")
	private String credentialsNo;		//证件号码
	@XStreamAlias("PERSONATTR")
	private String personAttr;			//人员属性
	@XStreamAlias("AGE")
	private String age;					//年龄
	@XStreamAlias("SEX")
	private String sex;					//性别
	@XStreamAlias("INDUSTRYCODE")
	private String industryCode;	    //从事行业编码
	@XStreamAlias("INDUSTRYNAME")
	private String industryName;        //从事行业名称
	@XStreamAlias("INCOMETYPE")
	private String incomeType;			//收入状况
	@XStreamAlias("HUKOUTYPE")
	private String hukouType;			//户口性质
	@XStreamAlias("PHONE")
	private String phone;				//联系电话
	@XStreamAlias("DAMAGETYPE")
	private String damageType;			//伤情类别
	@XStreamAlias("LICENSENO")
	private String licenseNo;			//车牌号码
	@XStreamAlias("IDENTIFYCOMPANYNAME")
	private String identifyCompanyName; //鉴定机构名称
	@XStreamAlias("IDENTIFYCOMPANYCODE")
	private String identifyCompanyCode; //组织机构代码
	
	@XStreamAlias("PARTLIST")
	private List<PartInfoVo> partInfoVo;     
	@XStreamAlias("INPATIENTLIST")
	private List<InPantientInfoVo> inPantientInfoVo;
	@XStreamAlias("TRACEINFO")
	private TraceInfoVo traceInfoVo;
	@XStreamAlias("NURSELIST")
	private List<NurseInfoVo> nurseInfoVo;
	@XStreamAlias("RAISELIST")
	private List<RaiseInfoVo> raiseInfoVo;
	
	
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCredentialsType() {
		return credentialsType;
	}
	public void setCredentialsType(String credentialsType) {
		this.credentialsType = credentialsType;
	}
	public String getCredentialsNo() {
		return credentialsNo;
	}
	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}
	public String getPersonAttr() {
		return personAttr;
	}
	public void setPersonAttr(String personAttr) {
		this.personAttr = personAttr;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIndustryCode() {
		return industryCode;
	}
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getIncomeType() {
		return incomeType;
	}
	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
	}
	public String getHukouType() {
		return hukouType;
	}
	public void setHukouType(String hukouType) {
		this.hukouType = hukouType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDamageType() {
		return damageType;
	}
	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getIdentifyCompanyName() {
		return identifyCompanyName;
	}
	public void setIdentifyCompanyName(String identifyCompanyName) {
		this.identifyCompanyName = identifyCompanyName;
	}
	public String getIdentifyCompanyCode() {
		return identifyCompanyCode;
	}
	public void setIdentifyCompanyCode(String identifyCompanyCode) {
		this.identifyCompanyCode = identifyCompanyCode;
	}
	public List<PartInfoVo> getPartInfoVo() {
		return partInfoVo;
	}
	public void setPartInfoVo(List<PartInfoVo> partInfoVo) {
		this.partInfoVo = partInfoVo;
	}
	public List<InPantientInfoVo> getInPantientInfoVo() {
		return inPantientInfoVo;
	}
	public void setInPantientInfoVo(List<InPantientInfoVo> inPantientInfoVo) {
		this.inPantientInfoVo = inPantientInfoVo;
	}
	public TraceInfoVo getTraceInfoVo() {
		return traceInfoVo;
	}
	public void setTraceInfoVo(TraceInfoVo traceInfoVo) {
		this.traceInfoVo = traceInfoVo;
	}
	public List<NurseInfoVo> getNurseInfoVo() {
		return nurseInfoVo;
	}
	public void setNurseInfoVo(List<NurseInfoVo> nurseInfoVo) {
		this.nurseInfoVo = nurseInfoVo;
	}
	public List<RaiseInfoVo> getRaiseInfoVo() {
		return raiseInfoVo;
	}
	public void setRaiseInfoVo(List<RaiseInfoVo> raiseInfoVo) {
		this.raiseInfoVo = raiseInfoVo;
	}

}
