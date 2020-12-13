package ins.sino.claimcar.flow.vo;

import ins.sino.claimcar.claim.vo.PrpLFxqFavoreeVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class AMLVo implements java.io.Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String policyNo;//保单号码
    private String insuredType;//被保险人类型
    private String educationCode;//国籍
    private String insuredName;//被保险人类型
    private String sex;//性别
    private String identifyType;//证件类型
    private String identifyNumber;//证件号码//组织机构代码
    private String certifyDate;//证件有效期
    private String profession;//职业
    private String mobile;//联系方式
    private String adressType;//住宅类型
    private String adress;//地址
    private String payAccountNo;//缴费账户
    private String acconutNo;//赔款账户
    private String isConsistent;//缴费与赔款账户一致性：
    private String insureRelation;//被保险人与投保人关系
    
    private String businessArea;//经营范围
    private String revenueRegistNo;//税务登记号码
    private String businessCode;//营业执照编码
    private String legalPerson;//法定代表人
    private String legalPhone;//法人联系方式
    private String legalIdentifyType;//法人证件类型
    private String legalIdentifyCode;//法人证件号码
    private Date legalCertifyStartDate;//法人证件开始有效期
    private Date legalCertifyEndDate;//法人证件结束有效期
    private String authorityName;//授权办理人姓名
    private String authorityPhone;//授权办理人联系方式
    private String authorityCertifyType;//授权办理人证件类型
    private String authorityNo;//授权办理人证件号码
    private Date authorityStartDate;//授权办理人证件开始有效期
    private Date authorityEndDate;//授权办理人证件结束有效期
    
    private String customerType;//受益人类型
    private String favoreeName;//受益人姓名
    private String favoreeAdress;//受益人住址
    private Date favoreeCertifyStartDate;//受益人证件开始有效期
    private Date favoreeCertifyEndDate;//受益人证件结束有效期
    private String favoreeCertifyDate;
    private String favoreenSex;//受益人性别（自然人）
    private String favoreenNatioNality;//受益人国籍（自然人）
    private String favoreeCertifyType;//受益人证件类型
    private String favoreeIdentifyCode;//受益人证件号码
    private String favoreenProfession;//受益人职业（自然人）
    private String favoreenPhone;//受益人联系方式（自然人）
    private String favoreenAdressType;//受益人住址类型（自然人）
    private String favoreelInsureRelation;//受益人与被保险关系
    private String favoreelBusinessArea;//受益人经营范围（法人）
    private String favoreelRevenueRegistNo;//受益人税务登记号码（法人）
    private String favoreelBusinessCode;//受益人营业执照编码（法人）
    
    private String fxqSeeflag;//核赔反洗钱是否查看标志SY--表示查看
    private PrpLFxqFavoreeVo favoreeVo;
    
    private String SUBREPORT_DIR;
    
    private List<AMLThreeCertificateVo> amlThreeCertificateList;//受益人三证
    private List<AMLOneCertificateVo> amlOneCertificateList;//受益人三证合一
    
    private BigDecimal fxqCustomId;//反洗钱的用户Id
    private BigDecimal fxqFavoreeId;//反洗钱受益人Id
    private String claimNo;//立案号
       
    public String getPolicyNo() {
        return policyNo;
    }
   
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
    
    public String getInsuredType() {
        return insuredType;
    }
    
    public void setInsuredType(String insuredType) {
        this.insuredType = insuredType;
    }

    public String getEducationCode() {
        return educationCode;
    }
    
    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public String getInsuredName() {
        return insuredName;
    }
    
    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getIdentifyType() {
        return identifyType;
    }
    
    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }
    
    public String getIdentifyNumber() {
        return identifyNumber;
    }
    
    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
    }   
    
    public String getCertifyDate() {
        return certifyDate;
    }
    
    public void setCertifyDate(String certifyDate) {
        this.certifyDate = certifyDate;
    }

    public String getProfession() {
        return profession;
    }
    
    public void setProfession(String profession) {
        this.profession = profession;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getAdressType() {
        return adressType;
    }
    
    public void setAdressType(String adressType) {
        this.adressType = adressType;
    }
    
    public String getAdress() {
        return adress;
    }
    
    public void setAdress(String adress) {
        this.adress = adress;
    }
    
    public String getPayAccountNo() {
        return payAccountNo;
    }
    
    public void setPayAccountNo(String payAccountNo) {
        this.payAccountNo = payAccountNo;
    }
    
    public String getAcconutNo() {
        return acconutNo;
    }
    
    public void setAcconutNo(String acconutNo) {
        this.acconutNo = acconutNo;
    } 
    
    public String getIsConsistent() {
        return isConsistent;
    }
    
    public void setIsConsistent(String isConsistent) {
        this.isConsistent = isConsistent;
    }

    public String getInsureRelation() {
        return insureRelation;
    }
    
    public void setInsureRelation(String insureRelation) {
        this.insureRelation = insureRelation;
    }
    
    public String getBusinessArea() {
        return businessArea;
    }
    
    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
    
    public String getRevenueRegistNo() {
        return revenueRegistNo;
    }
    
    public void setRevenueRegistNo(String revenueRegistNo) {
        this.revenueRegistNo = revenueRegistNo;
    }
    
    public String getBusinessCode() {
        return businessCode;
    }
    
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
    
    public String getLegalPerson() {
        return legalPerson;
    }
    
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
    
    public String getLegalPhone() {
        return legalPhone;
    }
    
    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }
    
    public String getLegalIdentifyType() {
        return legalIdentifyType;
    }
    
    public void setLegalIdentifyType(String legalIdentifyType) {
        this.legalIdentifyType = legalIdentifyType;
    }
    
    public String getLegalIdentifyCode() {
        return legalIdentifyCode;
    }
    
    public void setLegalIdentifyCode(String legalIdentifyCode) {
        this.legalIdentifyCode = legalIdentifyCode;
    }
    
    public Date getLegalCertifyStartDate() {
        return legalCertifyStartDate;
    }
    
    public void setLegalCertifyStartDate(Date legalCertifyStartDate) {
        this.legalCertifyStartDate = legalCertifyStartDate;
    }
    
    public Date getLegalCertifyEndDate() {
        return legalCertifyEndDate;
    }
    
    public void setLegalCertifyEndDate(Date legalCertifyEndDate) {
        this.legalCertifyEndDate = legalCertifyEndDate;
    }
    
    public String getAuthorityName() {
        return authorityName;
    }
    
    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
    
    public String getAuthorityPhone() {
        return authorityPhone;
    }
    
    public void setAuthorityPhone(String authorityPhone) {
        this.authorityPhone = authorityPhone;
    }
    
    public String getAuthorityCertifyType() {
        return authorityCertifyType;
    }
    
    public void setAuthorityCertifyType(String authorityCertifyType) {
        this.authorityCertifyType = authorityCertifyType;
    }
    
    public String getAuthorityNo() {
        return authorityNo;
    }
    
    public void setAuthorityNo(String authorityNo) {
        this.authorityNo = authorityNo;
    }
    
    public Date getAuthorityStartDate() {
        return authorityStartDate;
    }
    
    public void setAuthorityStartDate(Date authorityStartDate) {
        this.authorityStartDate = authorityStartDate;
    }
    
    public Date getAuthorityEndDate() {
        return authorityEndDate;
    }
    
    public void setAuthorityEndDate(Date authorityEndDate) {
        this.authorityEndDate = authorityEndDate;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getFavoreeName() {
        return favoreeName;
    }
   
    public void setFavoreeName(String favoreeName) {
        this.favoreeName = favoreeName;
    }
 
    public String getFavoreenSex() {
        return favoreenSex;
    }
 
    public void setFavoreenSex(String favoreenSex) {
        this.favoreenSex = favoreenSex;
    }

    public String getFavoreenNatioNality() {
        return favoreenNatioNality;
    }

    public void setFavoreenNatioNality(String favoreenNatioNality) {
        this.favoreenNatioNality = favoreenNatioNality;
    }

    public String getFavoreeCertifyType() {
        return favoreeCertifyType;
    }

    public void setFavoreeCertifyType(String favoreeCertifyType) {
        this.favoreeCertifyType = favoreeCertifyType;
    }
 
    public String getFavoreeIdentifyCode() {
        return favoreeIdentifyCode;
    }

    public void setFavoreeIdentifyCode(String favoreeIdentifyCode) {
        this.favoreeIdentifyCode = favoreeIdentifyCode;
    }

    public Date getFavoreeCertifyStartDate() {
        return favoreeCertifyStartDate;
    }

    public void setFavoreeCertifyStartDate(Date favoreeCertifyStartDate) {
        this.favoreeCertifyStartDate = favoreeCertifyStartDate;
    }

    public Date getFavoreeCertifyEndDate() {
        return favoreeCertifyEndDate;
    }

    public void setFavoreeCertifyEndDate(Date favoreeCertifyEndDate) {
        this.favoreeCertifyEndDate = favoreeCertifyEndDate;
    }
    
    public String getFavoreeCertifyDate() {
        return favoreeCertifyDate;
    }
    
    public void setFavoreeCertifyDate(String favoreeCertifyDate) {
        this.favoreeCertifyDate = favoreeCertifyDate;
    }

    public String getFavoreenProfession() {
        return favoreenProfession;
    }

    public void setFavoreenProfession(String favoreenProfession) {
        this.favoreenProfession = favoreenProfession;
    }

    public String getFavoreenPhone() {
        return favoreenPhone;
    }

    public void setFavoreenPhone(String favoreenPhone) {
        this.favoreenPhone = favoreenPhone;
    }

    public String getFavoreenAdressType() {
        return favoreenAdressType;
    }
    
    public String getFavoreeAdress() {
        return favoreeAdress;
    }
    
    public void setFavoreeAdress(String favoreeAdress) {
        this.favoreeAdress = favoreeAdress;
    }

    public void setFavoreenAdressType(String favoreenAdressType) {
        this.favoreenAdressType = favoreenAdressType;
    }

    public String getFavoreelInsureRelation() {
        return favoreelInsureRelation;
    }

    public void setFavoreelInsureRelation(String favoreelInsureRelation) {
        this.favoreelInsureRelation = favoreelInsureRelation;
    }

    public String getFavoreelBusinessArea() {
        return favoreelBusinessArea;
    }

    public void setFavoreelBusinessArea(String favoreelBusinessArea) {
        this.favoreelBusinessArea = favoreelBusinessArea;
    }

    public String getFavoreelRevenueRegistNo() {
        return favoreelRevenueRegistNo;
    }

    public void setFavoreelRevenueRegistNo(String favoreelRevenueRegistNo) {
        this.favoreelRevenueRegistNo = favoreelRevenueRegistNo;
    }

    public String getFavoreelBusinessCode() {
        return favoreelBusinessCode;
    }

    public void setFavoreelBusinessCode(String favoreelBusinessCode) {
        this.favoreelBusinessCode = favoreelBusinessCode;
    }
  
    public String getSUBREPORT_DIR() {
        return SUBREPORT_DIR;
    }
    
    public void setSUBREPORT_DIR(String sUBREPORT_DIR) {
        SUBREPORT_DIR = sUBREPORT_DIR;
    }
  
    public List<AMLThreeCertificateVo> getAmlThreeCertificateList() {
        return amlThreeCertificateList;
    }

    public void setAmlThreeCertificateList(List<AMLThreeCertificateVo> amlThreeCertificateList) {
        this.amlThreeCertificateList = amlThreeCertificateList;
    }
  
    public List<AMLOneCertificateVo> getAmlOneCertificateList() {
        return amlOneCertificateList;
    }
   
    public void setAmlOneCertificateList(List<AMLOneCertificateVo> amlOneCertificateList) {
        this.amlOneCertificateList = amlOneCertificateList;
    }
    
    public BigDecimal getFxqCustomId() {
        return fxqCustomId;
    }
    
    public void setFxqCustomId(BigDecimal fxqCustomId) {
        this.fxqCustomId = fxqCustomId;
    }
  
    public BigDecimal getFxqFavoreeId() {
        return fxqFavoreeId;
    }
  
    public void setFxqFavoreeId(BigDecimal fxqFavoreeId) {
        this.fxqFavoreeId = fxqFavoreeId;
    }

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getFxqSeeflag() {
		return fxqSeeflag;
	}

	public void setFxqSeeflag(String fxqSeeflag) {
		this.fxqSeeflag = fxqSeeflag;
	}

	public PrpLFxqFavoreeVo getFavoreeVo() {
		return favoreeVo;
	}

	public void setFavoreeVo(PrpLFxqFavoreeVo favoreeVo) {
		this.favoreeVo = favoreeVo;
	}
    
	
    
}
