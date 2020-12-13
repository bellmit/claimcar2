package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO VprppheadId
 */ 
public class PrppheadVo extends PrppheadVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
    private String endNoContext;//批单内容
	public String getEndNoContext() {
		return endNoContext;
	}
	public void setEndNoContext(String endNoContext) {
		this.endNoContext = endNoContext;
	}
    
	
}
