package ins.sino.claimcar.hnbxrest.vo;

import java.io.Serializable;


public class PrplQuickCaseCarVoBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private PrplQuickCaseInforVo prplquickcaseinforVo;
    private String casecarno;
    private String dutytype;
    private String carownname;
    private String carownphone;
    private String inscomcode;
    private String inscompany;
    private String cardno;
    private String driverfileno;
    private String driverlicence;
    private String cartype;
    private String frameno;
    private String signtime;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    } 
    
    public PrplQuickCaseInforVo getPrplquickcaseinforVo() {
        return prplquickcaseinforVo;
    }
    
    public void setPrplquickcaseinforVo(PrplQuickCaseInforVo prplquickcaseinforVo) {
        this.prplquickcaseinforVo = prplquickcaseinforVo;
    }

    public String getCasecarno() {
        return casecarno;
    }
    
    public void setCasecarno(String casecarNo) {
        this.casecarno = casecarNo;
    }
    
    public String getDutytype() {
        return dutytype;
    }
    
    public void setDutytype(String dutytype) {
        this.dutytype = dutytype;
    }
    
    public String getCarownname() {
        return carownname;
    }
    
    public void setCarownname(String carownname) {
        this.carownname = carownname;
    }
    
    public String getCarownphone() {
        return carownphone;
    }
    
    public void setCarownphone(String carownphone) {
        this.carownphone = carownphone;
    }
    
    public String getInscomcode() {
        return inscomcode;
    }
    
    public void setInscomcode(String inscomcode) {
        this.inscomcode = inscomcode;
    }
    
    public String getInscompany() {
        return inscompany;
    }
    
    public void setInscompany(String inscompany) {
        this.inscompany = inscompany;
    }
    
    public String getCardno() {
        return cardno;
    }
    
    public void setCardno(String cardno) {
        this.cardno = cardno;
    }
    
    public String getDriverfileno() {
        return driverfileno;
    }
    
    public void setDriverfileno(String driverfileno) {
        this.driverfileno = driverfileno;
    }
    
    public String getDriverlicence() {
        return driverlicence;
    }
    
    public void setDriverlicence(String driverlicence) {
        this.driverlicence = driverlicence;
    }
    
    public String getCartype() {
        return cartype;
    }
    
    public void setCartype(String cartype) {
        this.cartype = cartype;
    }
    
    public String getFrameno() {
        return frameno;
    }
    
    public void setFrameno(String frameno) {
        this.frameno = frameno;
    }
    
    public String getSigntime() {
        return signtime;
    }
    
    public void setSigntime(String signtime) {
        this.signtime = signtime;
    }
    
}
