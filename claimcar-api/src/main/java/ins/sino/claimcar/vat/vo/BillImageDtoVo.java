package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class BillImageDtoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String idEcdinvoiceocr;//进项发票ID
	private String bussNo;//业务号
	private String bussType;//业务类型
	private String chargeCode;//费用类型
	private String billUrl;//发票url
	
	public String getIdEcdinvoiceocr() {
		return idEcdinvoiceocr;
	}
	public void setIdEcdinvoiceocr(String idEcdinvoiceocr) {
		this.idEcdinvoiceocr = idEcdinvoiceocr;
	}
	public String getBussNo() {
		return bussNo;
	}
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	public String getBussType() {
		return bussType;
	}
	public void setBussType(String bussType) {
		this.bussType = bussType;
	}
	public String getBillUrl() {
		return billUrl;
	}
	public void setBillUrl(String billUrl) {
		this.billUrl = billUrl;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
}
