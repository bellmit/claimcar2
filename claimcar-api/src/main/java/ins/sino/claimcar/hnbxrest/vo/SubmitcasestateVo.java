package ins.sino.claimcar.hnbxrest.vo;


public class SubmitcasestateVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private String casenumber;//快处报案号
    private String inscaseno;//保险报案号
    private String state;//案件状态 0-电话报案，1-认可定损，2-金额较小放弃理赔，3-不认可，前往理赔中心定损
    
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
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
}
