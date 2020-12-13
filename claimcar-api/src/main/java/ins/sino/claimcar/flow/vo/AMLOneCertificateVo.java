package ins.sino.claimcar.flow.vo;

import java.util.Date;


public class AMLOneCertificateVo {
    
    private String identifyNumber;//营业执照编码
    private Date certifyStartDate;//营业执照开始有效期
    private Date certifyEndDate;//营业执照结束有效期
    private String certifyDate;

    private String favoreeIdentifyCode;//受益人营业执照编码（法人）
    private Date favoreelCertifyStartDate;//受益人营业执照开始有效期（法人）
    private Date favoreelCertifyEndDate;//受益人营业执照结束有效期（法人）
    private String favoreelCertifyDate;

    
    
    public String getIdentifyNumber() {
        return identifyNumber;
    }
    
    public void setIdentifyNumber(String identifyNumber) {
        this.identifyNumber = identifyNumber;
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
