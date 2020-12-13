package ins.sino.claimcar.hnbxrest.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PrplQuickCaseInforVoBase implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String casenumber;
    private String casecarno;
    private String casetelephone;
    private String caselon;
    private String caselat;
    private String caseaddress;
    private Date casedate;
    private Date restime;
    private String accidenttype;
    private String areaid;
    private String weather;
    private String type;
    private List<PrplQuickCaseCarVo> casecarlist = new ArrayList<PrplQuickCaseCarVo>();
    private String registno;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCasenumber() {
        return casenumber;
    }
    
    public void setCasenumber(String casenumber) {
        this.casenumber = casenumber;
    }
    
    public String getCasecarno() {
        return casecarno;
    }
    
    public void setCasecarno(String casecarno) {
        this.casecarno = casecarno;
    }
    
    public String getCasetelephone() {
        return casetelephone;
    }
    
    public void setCasetelephone(String casetelephone) {
        this.casetelephone = casetelephone;
    }
    
    public String getCaselon() {
        return caselon;
    }
    
    public void setCaselon(String caselon) {
        this.caselon = caselon;
    }
    
    public String getCaselat() {
        return caselat;
    }
    
    public void setCaselat(String caselat) {
        this.caselat = caselat;
    }
    
    public String getCaseaddress() {
        return caseaddress;
    }
    
    public void setCaseaddress(String caseaddress) {
        this.caseaddress = caseaddress;
    }
    
    public Date getCasedate() {
        return casedate;
    }
    
    public void setCasedate(Date casedate) {
        this.casedate = casedate;
    }
    
    public Date getRestime() {
        return restime;
    }
    
    public void setRestime(Date restime) {
        this.restime = restime;
    }
    
    public String getAccidenttype() {
        return accidenttype;
    }
    
    public void setAccidenttype(String accidenttype) {
        this.accidenttype = accidenttype;
    }
    
    public String getAreaid() {
        return areaid;
    }
    
    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }
    
    public String getWeather() {
        return weather;
    }
    
    public void setWeather(String weather) {
        this.weather = weather;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
  
    public String getRegistno() {
        return registno;
    }

    public void setRegistno(String registno) {
        this.registno = registno;
    }

	public List<PrplQuickCaseCarVo> getCasecarlist() {
		return casecarlist;
	}

	public void setCasecarlist(List<PrplQuickCaseCarVo> casecarlist) {
		this.casecarlist = casecarlist;
	}
  
}
