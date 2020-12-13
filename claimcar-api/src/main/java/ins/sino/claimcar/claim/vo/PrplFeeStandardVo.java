package ins.sino.claimcar.claim.vo;

/**
 * Custom VO class of PO PrplFeeStandard
 */ 
public class PrplFeeStandardVo extends PrplFeeStandardVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String imageUrl;
	private String imageUrlView;
	private String dateCode;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageUrlView() {
		return imageUrlView;
	}
	public void setImageUrlView(String imageUrlView) {
		this.imageUrlView = imageUrlView;
	}
	public String getDateCode() {
		return dateCode;
	}
	public void setDateCode(String dateCode) {
		this.dateCode = dateCode;
	}
	
	
}
