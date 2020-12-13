package ins.sino.claimcar.hnbxrest.vo;


public class CasebankVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private String casecarno;//案件车牌号
    private String dutytype;//责任类型 0-全责，1-无责，2-同责
    private String bankcardtype;//银行卡类型
    private String bankcardno;//银行卡卡号
    private String bankcardaddress;//银行卡开户地址
    
    public String getCasecarno() {
        return casecarno;
    }
    
    public void setCasecarno(String casecarno) {
        this.casecarno = casecarno;
    }
    
    public String getDutytype() {
        return dutytype;
    }
    
    public void setDutytype(String dutytype) {
        this.dutytype = dutytype;
    }
    
    public String getBankcardtype() {
        return bankcardtype;
    }
    
    public void setBankcardtype(String bankcardtype) {
        this.bankcardtype = bankcardtype;
    }
    
    public String getBankcardno() {
        return bankcardno;
    }
    
    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }
    
    public String getBankcardaddress() {
        return bankcardaddress;
    }
    
    public void setBankcardaddress(String bankcardaddress) {
        this.bankcardaddress = bankcardaddress;
    }    
    
}
