package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class BillImageViewSonVo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String idEcdinvoiceocr;//进项发票ID
	private String bussNo;//业务号
	private String bussType;//业务类型
	private String chargeCode;//费用类型
	private String paycustId;//收款人唯一标识
	private String linktableName;//关联表名称
	private Long linktableId;//关联表Id
	private String billUrl;//发票url
	private String flag;//是否查勘费或公估费
	
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
	
	public String getPaycustId() {
		return paycustId;
	}
	public void setPaycustId(String paycustId) {
		this.paycustId = paycustId;
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
	public String getLinktableName() {
		return linktableName;
	}
	public void setLinktableName(String linktableName) {
		this.linktableName = linktableName;
	}
	public Long getLinktableId() {
		return linktableId;
	}
	public void setLinktableId(Long linktableId) {
		this.linktableId = linktableId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
