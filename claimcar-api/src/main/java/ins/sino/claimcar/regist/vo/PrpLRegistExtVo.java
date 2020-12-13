package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO PrpLRegistExt
 */ 
public class PrpLRegistExtVo extends PrpLRegistExtVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String accidentTypes;
	private String orderNo;//订单号
	public String getAccidentTypes() {
		return accidentTypes;
	}
	public void setAccidentTypes(String accidentTypes) {
		this.accidentTypes = accidentTypes;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
