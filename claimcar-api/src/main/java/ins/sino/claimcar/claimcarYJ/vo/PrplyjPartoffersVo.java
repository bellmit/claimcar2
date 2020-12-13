package ins.sino.claimcar.claimcarYJ.vo;

/**
 * Custom VO class of PO PrplyjPartoffers
 */ 
public class PrplyjPartoffersVo extends PrplyjPartoffersVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String enquiryId;//三方询价单编号
	private String enquiryno;//阳杰报价单编号
	public String getEnquiryId() {
		return enquiryId;
	}
	public void setEnquiryId(String enquiryId) {
		this.enquiryId = enquiryId;
	}
	public String getEnquiryno() {
		return enquiryno;
	}
	public void setEnquiryno(String enquiryno) {
		this.enquiryno = enquiryno;
	}
	
	
}
