package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Insured")
public class Insured implements Serializable {

	/**被保险人信息  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("InsuredCode")
	private String insuredCode;  //被保险人编码
	
    @XStreamAlias("InsuredName")
    private String insuredName;  //被保险人名称
    
    @XStreamAlias("InsuredNature")
    private String insuredNature;  //被保险人性质代码
    
    @XStreamAlias("CredentialCode")
    private String credentialCode;  //证件类型代码
    
    @XStreamAlias("CredentialNo")
    private String credentialNo;  //证件号码
    
    @XStreamAlias("GenderCode")
    private String genderCode;  //性别代码
    
    @XStreamAlias("Birthday")
    private String birthday;  //出生年月
    
    @XStreamAlias("DeathDate")
    private String deathDate;  //死亡日期
	   
    @XStreamAlias("Height")
    private String height;  //身高
    
    @XStreamAlias("Weight")
    private String weight;  //体重
    
    @XStreamAlias("MarriageStatus")
    private String marriageStatus;  //婚姻状态
    
    @XStreamAlias("EducateLevel")
    private String educateLevel;  //教育水平
    
    @XStreamAlias("GuardianName")
    private String guardianName;  //监护人
    
    @XStreamAlias("Country")
    private String country;  //国家
    
    @XStreamAlias("Province")
    private String province;  //省份
    
    @XStreamAlias("City")
    private String city;  //城市
    
    @XStreamAlias("Address")
    private String address;  //住址
    
    @XStreamAlias("PostAddress")
    private String postAddress;  //通讯地址
    
    @XStreamAlias("Zip")
    private String zip;  //邮政编码
    
    @XStreamAlias("PhoneNo")
    private String phoneNo;  //联系电话
    
    @XStreamAlias("Email")
    private String email;  //Email
    
    @XStreamAlias("OrganizationType")
    private String organizationType;  //单位性质代码

    @XStreamAlias("CreditRating")
    private String creditRating;  //客户信用评级
    
    @XStreamAlias("Remark")
    private String remark;  //备注
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getInsuredCode() {
        return insuredCode;
    }

    
    public void setInsuredCode(String insuredCode) {
        this.insuredCode = insuredCode;
    }

    
    public String getInsuredName() {
        return insuredName;
    }

    
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    
    public String getInsuredNature() {
        return insuredNature;
    }

    
    public void setInsuredNature(String insuredNature) {
        this.insuredNature = insuredNature;
    }

    
    public String getCredentialCode() {
        return credentialCode;
    }

    
    public void setCredentialCode(String credentialCode) {
        this.credentialCode = credentialCode;
    }

    
    public String getCredentialNo() {
        return credentialNo;
    }

    
    public void setCredentialNo(String credentialNo) {
        this.credentialNo = credentialNo;
    }

    
    public String getGenderCode() {
        return genderCode;
    }

    
    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    
    public String getBirthday() {
        return birthday;
    }

    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    
    public String getDeathDate() {
        return deathDate;
    }

    
    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    
    public String getHeight() {
        return height;
    }

    
    public void setHeight(String height) {
        this.height = height;
    }

    
    public String getWeight() {
        return weight;
    }

    
    public void setWeight(String weight) {
        this.weight = weight;
    }

    
    public String getMarriageStatus() {
        return marriageStatus;
    }

    
    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    
    public String getEducateLevel() {
        return educateLevel;
    }

    
    public void setEducateLevel(String educateLevel) {
        this.educateLevel = educateLevel;
    }

    
    public String getGuardianName() {
        return guardianName;
    }

    
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    
    public String getCountry() {
        return country;
    }

    
    public void setCountry(String country) {
        this.country = country;
    }

    
    public String getProvince() {
        return province;
    }

    
    public void setProvince(String province) {
        this.province = province;
    }

    
    public String getCity() {
        return city;
    }

    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getPostAddress() {
        return postAddress;
    }

    
    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    
    public String getZip() {
        return zip;
    }

    
    public void setZip(String zip) {
        this.zip = zip;
    }

    
    public String getPhoneNo() {
        return phoneNo;
    }

    
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getOrganizationType() {
        return organizationType;
    }

    
    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    
    public String getCreditRating() {
        return creditRating;
    }

    
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    
  
    
}
