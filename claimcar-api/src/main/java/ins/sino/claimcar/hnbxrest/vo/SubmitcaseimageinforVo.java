package ins.sino.claimcar.hnbxrest.vo;

import java.util.List;


public class SubmitcaseimageinforVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
    private String casenumber;//快处报案号
    private String inscaseno;//保险报案号
    private List<CaseimageVo> caseimagelist;//涉案照片信息数组
    private String datatype;//接口数据类型 0 - 一键报案（初次报案） 1 -  已电话报案，线下转线上继续处理(数据关联)
    private List<CasebankVo> casebanklist;//银行卡补录信息
    
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
    
    public List<CaseimageVo> getCaseimagelist() {
        return caseimagelist;
    }
    
    public void setCaseimagelist(List<CaseimageVo> caseimagelist) {
        this.caseimagelist = caseimagelist;
    }
    
    public String getDatatype() {
        return datatype;
    }
    
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
    
    public List<CasebankVo> getCasebanklist() {
        return casebanklist;
    }
    
    public void setCasebanklist(List<CasebankVo> casebanklist) {
        this.casebanklist = casebanklist;
    }  

}
