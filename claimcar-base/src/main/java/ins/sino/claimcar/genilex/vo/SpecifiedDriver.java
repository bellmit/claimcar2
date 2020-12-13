package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("SpecifiedDriver")
public class SpecifiedDriver implements Serializable {

    /**被保险人信息  */
    private static final long serialVersionUID = 6606450331852430142L;
    
    @XStreamAlias("DriverCode")
    private String driverCode="";  //驾驶员编号
    
    @XStreamAlias("DriverName")
    private String driverName="";  //驾驶员名称
    
    @XStreamAlias("CredentialCode")
    private String credentialCode="";  //证件类型代码
    
    @XStreamAlias("CredentialNo")
    private String credentialNo="";  //证件号码
    
    @XStreamAlias("DriverTypeCode")
    private String driverTypeCode="";  //驾驶证类型
    
    @XStreamAlias("LicenseDesc")
    private String licenseDesc="";  //驾驶证描述
       
    @XStreamAlias("LicenseNo")
    private String licenseNo="";  //驾驶证号
    
    @XStreamAlias("LicenseStatusCode")
    private String licenseStatusCode="";  //驾驶证状态代码
    
    @XStreamAlias("LicensedDate")
    private String licensedDate="";  //驾驶证首次发证日期
    
    @XStreamAlias("MajorDriverFlag")
    private String majorDriverFlag="";  //主副驾驶员标志
    
    @XStreamAlias("Birthday")
    private String birthday="";  //出生年月
    
    @XStreamAlias("GenderCode")
    private String genderCode="";  //性别代码
    
    @XStreamAlias("MarriageStatus")
    private String marriageStatus="";  //婚姻状态
    
    @XStreamAlias("GuardianName")
    private String guardianName="";  //监护人
    
    @XStreamAlias("Country")
    private String country="";  //国家
    
    @XStreamAlias("Province")
    private String province="";  //省份
    
    @XStreamAlias("City")
    private String city="";  //城市
    
    @XStreamAlias("Address")
    private String address="";  //住址
    
    @XStreamAlias("PostAddress")
    private String postAddress="";  //通讯地址
    
    @XStreamAlias("Zip")
    private String zip="";  //邮政编码
    
    @XStreamAlias("PhoneNo")
    private String phoneNo="";  //联系电话
    
    @XStreamAlias("Email")
    private String email="";  //Email

    @XStreamAlias("CreditRating")
    private String creditRating="";  //客户信用评级
    
    @XStreamAlias("Remark")
    private String remark="";  //备注
    
    @XStreamAlias("CreateBy")
    private String createBy="";  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime="";  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy="";  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime="";  //更新日期

    
    public String getDriverCode() {
        return driverCode;
    }

    
    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
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

    
    public String getDriverTypeCode() {
        return driverTypeCode;
    }

    
    public void setDriverTypeCode(String driverTypeCode) {
        this.driverTypeCode = driverTypeCode;
    }

    
    public String getLicenseDesc() {
        return licenseDesc;
    }

    
    public void setLicenseDesc(String licenseDesc) {
        this.licenseDesc = licenseDesc;
    }

    
    public String getLicenseNo() {
        return licenseNo;
    }

    
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    
    public String getLicenseStatusCode() {
        return licenseStatusCode;
    }

    
    public void setLicenseStatusCode(String licenseStatusCode) {
        this.licenseStatusCode = licenseStatusCode;
    }

    
    public String getLicensedDate() {
        return licensedDate;
    }

    
    public void setLicensedDate(String licensedDate) {
        this.licensedDate = licensedDate;
    }

    
    public String getMajorDriverFlag() {
        return majorDriverFlag;
    }

    
    public void setMajorDriverFlag(String majorDriverFlag) {
        this.majorDriverFlag = majorDriverFlag;
    }

    
    public String getBirthday() {
        return birthday;
    }

    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    
    public String getGenderCode() {
        return genderCode;
    }

    
    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    
    public String getMarriageStatus() {
        return marriageStatus;
    }

    
    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
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
