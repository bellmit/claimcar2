package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimReportLinker")
public class ClaimReportLinker implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("LinkerCode")
	private String linkerCode;  //联系人代码
	
    @XStreamAlias("LinkerName")
    private String linkerName;  //联系人名称
    
    @XStreamAlias("Phone")
    private String phone;  //联系人手机
    
    @XStreamAlias("Mobile")
    private String mobile;  //联系人电话
    
    @XStreamAlias("Email")
    private String email;  //Email
    
    @XStreamAlias("Birthday")
    private String birthday;  //出生日期
    
    @XStreamAlias("sex")
    private String sex;  //性别
    
    @XStreamAlias("RelationWithInsured")
    private String relationWithInsured;  //与被保人关系
    
    @XStreamAlias("RelationWithOwner")
    private String relationWithOwner;  //与车主关系
       
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期

    
    public String getLinkerCode() {
        return linkerCode;
    }

    
    public void setLinkerCode(String linkerCode) {
        this.linkerCode = linkerCode;
    }

    
    public String getLinkerName() {
        return linkerName;
    }

    
    public void setLinkerName(String linkerName) {
        this.linkerName = linkerName;
    }

    
    public String getPhone() {
        return phone;
    }

    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    
    public String getMobile() {
        return mobile;
    }

    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getBirthday() {
        return birthday;
    }

    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    
    public String getSex() {
        return sex;
    }

    
    public void setSex(String sex) {
        this.sex = sex;
    }

    
    public String getRelationWithInsured() {
        return relationWithInsured;
    }

    
    public void setRelationWithInsured(String relationWithInsured) {
        this.relationWithInsured = relationWithInsured;
    }

    
    public String getRelationWithOwner() {
        return relationWithOwner;
    }

    
    public void setRelationWithOwner(String relationWithOwner) {
        this.relationWithOwner = relationWithOwner;
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
