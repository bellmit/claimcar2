package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class LossInsuredItemVo implements Serializable {
	private static final long serialVersionUID = -5957645506016444166L;
	@XStreamAlias("Id")
	private String id;
	@XStreamAlias("PolicyId")
	private String policyId;
	@XStreamAlias("RiskCode")
	private String riskCode;
	@XStreamAlias("RiskName")
	private String riskName;
	@XStreamAlias("ItemCode")
	private String itemCode;
	@XStreamAlias("ItemName")
	private String itemName;
	@XStreamAlias("InsuranceSuitYear")
	private String insuranceSuitYear;
	@XStreamAlias("TotalInsSum")
	private String totalInsSum;
	@XStreamAlias("TotalInsFee")
	private String totalInsFee;
	@XStreamAlias("InsuranceProperty")
	private String insuranceProperty;
	@XStreamAlias("ItemAttribute")
	private String itemAttribute;
	@XStreamAlias("NopayFlag")
	private String nopayFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getInsuranceSuitYear() {
		return insuranceSuitYear;
	}

	public void setInsuranceSuitYear(String insuranceSuitYear) {
		this.insuranceSuitYear = insuranceSuitYear;
	}

	public String getTotalInsSum() {
		return totalInsSum;
	}

	public void setTotalInsSum(String totalInsSum) {
		this.totalInsSum = totalInsSum;
	}

	public String getTotalInsFee() {
		return totalInsFee;
	}

	public void setTotalInsFee(String totalInsFee) {
		this.totalInsFee = totalInsFee;
	}

	public String getInsuranceProperty() {
		return insuranceProperty;
	}

	public void setInsuranceProperty(String insuranceProperty) {
		this.insuranceProperty = insuranceProperty;
	}

	public String getItemAttribute() {
		return itemAttribute;
	}

	public void setItemAttribute(String itemAttribute) {
		this.itemAttribute = itemAttribute;
	}

	public String getNopayFlag() {
		return nopayFlag;
	}

	public void setNopayFlag(String nopayFlag) {
		this.nopayFlag = nopayFlag;
	}
}

