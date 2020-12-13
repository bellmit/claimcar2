package ins.sino.claimcar.flow.vo;

import java.util.Date;


public class AMLThreeCertificateVo {
    
    private String identifyNumber;//组织机构代码
    private String revenueRegistNo;//税务登记号码
    private String businessCode;//营业执照编码
    private Date certifyStartDate;//营业执照开始有效期
    private Date certifyEndDate;//营业执照结束有效期
    private String certifyDate;

    private String favoreeIdentifyCode;//受益人机构代码（法人）
    private String favoreelRevenueRegistNo;//受益人税务登记号码（法人）
    private String favoreelBusinessCode;//受益人营业执照编码（法人）
    private Date favoreelCertifyStartDate;//受益人营业执照开始有效期（法人）
    private Date favoreelCertifyEndDate;//受益人营业执照结束有效期（法人）
    private String favoreelCertifyDate;
    
      
    public String getIdentifyNumber() {
        return identifyNumber;
    }
   
    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
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
   
    public Date getCertifyStartDate() {
        return certifyStartDate;
    }
    
    public void setCertifyStartDate(Date certifyStartDate) {
        this.certifyStartDate = certifyStartDate;
    }
 
    public Date getCertifyEndDate() {
        return certifyEndDate;
    }

    public void setCertifyEndDate(Date certifyEndDate) {
        this.certifyEndDate = certifyEndDate;
    }
    
    public String getCertifyDate() {
        return certifyDate;
    }
    
    public void setCertifyDate(String certifyDate) {
        this.certifyDate = certifyDate;
    }
    
    public String getFavoreeIdentifyCode() {
        return favoreeIdentifyCode;
    }
    
    public void setFavoreeIdentifyCode(String favoreeIdentifyCode) {
        this.favoreeIdentifyCode = favoreeIdentifyCode;
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

    public Date getFavoreelCertifyStartDate() {
        return favoreelCertifyStartDate;
    }
  
    public void setFavoreelCertifyStartDate(Date favoreelCertifyStartDate) {
        this.favoreelCertifyStartDate = favoreelCertifyStartDate;
    }

    public Date getFavoreelCertifyEndDate() {
        return favoreelCertifyEndDate;
    }

    public void setFavoreelCertifyEndDate(Date favoreelCertifyEndDate) {
        this.favoreelCertifyEndDate = favoreelCertifyEndDate;
    }

    public String getFavoreelCertifyDate() {
        return favoreelCertifyDate;
    }
   
    public void setFavoreelCertifyDate(String favoreelCertifyDate) {
        this.favoreelCertifyDate = favoreelCertifyDate;
    }  
}
