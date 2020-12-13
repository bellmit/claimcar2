package ins.sino.claimcar.base.claimyj.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OFFER")
public class OfferVo implements Serializable{
	@XStreamAlias("NOSTOCK")
	private String noStock;
	@XStreamAlias("QUALITY")
	private String quality;
	@XStreamAlias("PARTENQUIRYPRICEID")
	private String partEnquiryPriceId;
	@XStreamAlias("BRAND")
	private String brand;
	@XStreamAlias("PRICEWITHTAX")
	private String priceWithTax;
	@XStreamAlias("PRICEWITHOUTTAX")
	private String priceWithoutTax;
	@XStreamAlias("LEADDAY")
	private String leadDay;
	@XStreamAlias("QUALITYASSURANCEPERIOD")
	private String qualityAssurancePeriod;
	@XStreamAlias("REMARK")
	private String remark;
	@XStreamAlias("QUALITY2")
	private String quality2;
	@XStreamAlias("PRICEWITHTAX2")
	private String priceWithTax2;
	@XStreamAlias("PRICEWITHOUTTAX2")
	private String priceWithoutTax2;
	@XStreamAlias("LEADDAY2")
	private String leadDay2;
	@XStreamAlias("QUALITYASSURANCEPERIOD2")
	private String qualityAssurancePeriod2;
	@XStreamAlias("REMARK2")
	private String remark2;
	@XStreamAlias("PARTENQUIRYPRICEID2")
	private String partEnquiryPriceId2;
	public String getNoStock() {
		return noStock;
	}
	public void setNoStock(String noStock) {
		this.noStock = noStock;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getPartEnquiryPriceId() {
		return partEnquiryPriceId;
	}
	public void setPartEnquiryPriceId(String partEnquiryPriceId) {
		this.partEnquiryPriceId = partEnquiryPriceId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getQuality2() {
		return quality2;
	}
	public void setQuality2(String quality2) {
		this.quality2 = quality2;
	}
	public String getPriceWithTax2() {
		return priceWithTax2;
	}
	public void setPriceWithTax2(String priceWithTax2) {
		this.priceWithTax2 = priceWithTax2;
	}
	public String getPriceWithoutTax2() {
		return priceWithoutTax2;
	}
	public void setPriceWithoutTax2(String priceWithoutTax2) {
		this.priceWithoutTax2 = priceWithoutTax2;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getPartEnquiryPriceId2() {
		return partEnquiryPriceId2;
	}
	public void setPartEnquiryPriceId2(String partEnquiryPriceId2) {
		this.partEnquiryPriceId2 = partEnquiryPriceId2;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getPriceWithTax() {
		return priceWithTax;
	}
	public void setPriceWithTax(String priceWithTax) {
		this.priceWithTax = priceWithTax;
	}
	public String getPriceWithoutTax() {
		return priceWithoutTax;
	}
	public void setPriceWithoutTax(String priceWithoutTax) {
		this.priceWithoutTax = priceWithoutTax;
	}
	public String getLeadDay() {
		return leadDay;
	}
	public void setLeadDay(String leadDay) {
		this.leadDay = leadDay;
	}
	public String getQualityAssurancePeriod() {
		return qualityAssurancePeriod;
	}
	public void setQualityAssurancePeriod(String qualityAssurancePeriod) {
		this.qualityAssurancePeriod = qualityAssurancePeriod;
	}
	public String getLeadDay2() {
		return leadDay2;
	}
	public void setLeadDay2(String leadDay2) {
		this.leadDay2 = leadDay2;
	}
	public String getQualityAssurancePeriod2() {
		return qualityAssurancePeriod2;
	}
	public void setQualityAssurancePeriod2(String qualityAssurancePeriod2) {
		this.qualityAssurancePeriod2 = qualityAssurancePeriod2;
	}
	
}
