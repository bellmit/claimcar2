package ins.sino.claimcar.hnbxrest.vo;


public class CasecarcpsVo  implements java.io.Serializable{
	
	private static final long serialVersionUID = 8423652723600188374L;

    private String casecarno;//案件车牌号
    private String cpsmoney;//理赔金额
    private String cpstype;//理赔状态 1-结案 2-拒赔 3-零结 4-注销
    private String cpstime;//理赔时间
    private String cpsresult;//理赔说明
    
    public String getCasecarno() {
        return casecarno;
    }
    
    public void setCasecarno(String casecarno) {
        this.casecarno = casecarno;
    }
    
    public String getCpsmoney() {
        return cpsmoney;
    }
    
    public void setCpsmoney(String cpsmoney) {
        this.cpsmoney = cpsmoney;
    }
    
    public String getCpstype() {
        return cpstype;
    }
    
    public void setCpstype(String cpstype) {
        this.cpstype = cpstype;
    }
    
    public String getCpstime() {
        return cpstime;
    }
    
    public void setCpstime(String cpstime) {
        this.cpstime = cpstime;
    }
    
    public String getCpsresult() {
        return cpsresult;
    }
    
    public void setCpsresult(String cpsresult) {
        this.cpsresult = cpsresult;
    }
    
}
