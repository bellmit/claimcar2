package ins.sino.claimcar.hnbxrest.vo;

import java.util.List;


public class ReceivegusunresultVo implements java.io.Serializable {
    /**
	 *   
	 */
	private static final long serialVersionUID = 1L;
	private String casenumber;//快处报案号
    private String inscaseno;//保险报案号
    private List<CasecarfeeVo> casecarfeelist;//车辆定损金额
    private String needtype;//是否需要商业险赔付 0 - 不需要1 - 需要
    private String username;//用户名
    private String password;//密码
    private String operateuser;//操作人员
    
    
    public String getInscaseno() {
        return inscaseno;
    }
    
    public void setInscaseno(String inscaseno) {
        this.inscaseno = inscaseno;
    }
    
    public List<CasecarfeeVo> getCasecarfeelist() {
        return casecarfeelist;
    }
    
    public void setCasecarfeelist(List<CasecarfeeVo> casecarfeelist) {
        this.casecarfeelist = casecarfeelist;
    }
    
    public String getNeedtype() {
        return needtype;
    }
    
    public void setNeedtype(String needtype) {
        this.needtype = needtype;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

	public String getCasenumber() {
		return casenumber;
	}

	public void setCasenumber(String casenumber) {
		this.casenumber = casenumber;
	}

	public String getOperateuser() {
		return operateuser;
	}

	public void setOperateuser(String operateuser) {
		this.operateuser = operateuser;
	}
    
}
