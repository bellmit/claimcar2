package ins.sino.claimcar.claimjy.vo.pricereturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyPriceReturnReqFitInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "PartId") 
	private String partId;

	@XmlElement(name = "AuditDiscountFee") 
	private String auditDiscountFee;

	@XmlElement(name = "AuditDiscount") 
	private String auditDiscount;

	@XmlElement(name = "AuditMaterialFee") 
	private String auditMaterialFee;

	@XmlElement(name = "ManageRate") 
	private String manageRate;

	@XmlElement(name = "ManageFee") 
	private String manageFee;

	@XmlElement(name = "Remark") 
	private String remark;

	@XmlElement(name = "EvalPartSumFirst") 
	private String evalPartSumFirst;
	
	@XmlElement(name = "EstiPartSum") 
	private String estiPartSum;

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getAuditDiscountFee() {
		return auditDiscountFee;
	}

	public void setAuditDiscountFee(String auditDiscountFee) {
		this.auditDiscountFee = auditDiscountFee;
	}

	public String getAuditDiscount() {
		return auditDiscount;
	}

	public void setAuditDiscount(String auditDiscount) {
		this.auditDiscount = auditDiscount;
	}

	public String getAuditMaterialFee() {
		return auditMaterialFee;
	}

	public void setAuditMaterialFee(String auditMaterialFee) {
		this.auditMaterialFee = auditMaterialFee;
	}

	public String getManageRate() {
		return manageRate;
	}

	public void setManageRate(String manageRate) {
		this.manageRate = manageRate;
	}

	public String getManageFee() {
		return manageFee;
	}

	public void setManageFee(String manageFee) {
		this.manageFee = manageFee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEvalPartSumFirst() {
		return evalPartSumFirst;
	}

	public void setEvalPartSumFirst(String evalPartSumFirst) {
		this.evalPartSumFirst = evalPartSumFirst;
	}

	public String getEstiPartSum() {
		return estiPartSum;
	}

	public void setEstiPartSum(String estiPartSum) {
		this.estiPartSum = estiPartSum;
	}

	

}
