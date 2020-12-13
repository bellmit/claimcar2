package ins.sino.claimcar.hnbxrest.vo;


public class CaseimageVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private String casecarno;//案件车牌号
    private String dutytype;//责任类型 0-全责，1-无责，2-同责
    private String imageurl;//照片url链接地址
    private String imagetype;//照片类型
    
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
    
    public String getImageurl() {
        return imageurl;
    }
    
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    
    public String getImagetype() {
        return imagetype;
    }
    
    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }
    
}
