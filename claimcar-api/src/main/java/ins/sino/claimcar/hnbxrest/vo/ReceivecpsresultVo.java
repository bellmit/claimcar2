package ins.sino.claimcar.hnbxrest.vo;

import java.util.List;


public class ReceivecpsresultVo  implements java.io.Serializable{
	
	private static final long serialVersionUID = 8423652723600188374L;

    private String casenumber;//快处报案号 
    private String inscaseno;//保险报案号
    private List<CasecarcpsVo> casecarcpslist;//车列表
    private String username;//用户名
    private String password;//用户名
    
    public String getCasenumber() {
        return casenumber;
    }
    
    public void setCasenumber(String casenumber) {
        this.casenumber = casenumber;
    }
    
    public String getInscaseno() {
        return inscaseno;
    }
    
    public void setInscaseno(String inscaseno) {
        this.inscaseno = inscaseno;
    }
    
    public List<CasecarcpsVo> getCasecarcpslist() {
        return casecarcpslist;
    }
    
    public void setCasecarcpslist(List<CasecarcpsVo> casecarcpslist) {
        this.casecarcpslist = casecarcpslist;
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
    
}
