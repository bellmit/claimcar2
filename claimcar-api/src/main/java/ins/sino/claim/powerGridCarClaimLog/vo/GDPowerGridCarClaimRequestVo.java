package ins.sino.claim.powerGridCarClaimLog.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class GDPowerGridCarClaimRequestVo {
	
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CAR_NUM")
	private String carNum;//车牌号
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="BE_THREATENED_TIME", format="yyyy/MM/dd")
	private Date beThreatenedTime;//出险时间
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="BE_THREATENED_ADDRESS")
	private String beThreatenedAddress;//出险地点
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="BE_THREATENED_REASON")
	private String beThreatenedReason;//出险原因
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="REPORT_TIME", format="yyyy/MM/dd")
	private Date reportTime;//报案时间
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CASE_NUMBER")
	private String caseNumber;//立案号(理赔唯一案件号)
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CASE_TIME", format="yyyy/MM/dd")
	private Date caseTime;//立案时间
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CLOSING_TIME", format="yyyy/MM/dd")
	private Date closingTime;//结案时间
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CLAIM_TYPE")
	private String claimType;//理赔类型
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="FACTORY")
	private String factory;//维修厂名称
	@JSONField(serialzeFeatures=SerializerFeature.WriteMapNullValue, name="CLAIM_AMOUNT")
	private BigDecimal claimAmount;//理赔金额
	
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public Date getBeThreatenedTime() {
		return beThreatenedTime;
	}
	public void setBeThreatenedTime(Date beThreatenedTime) {
		this.beThreatenedTime = beThreatenedTime;
	}
	public String getBeThreatenedAddress() {
		return beThreatenedAddress;
	}
	public void setBeThreatenedAddress(String beThreatenedAddress) {
		this.beThreatenedAddress = beThreatenedAddress;
	}
	public String getBeThreatenedReason() {
		return beThreatenedReason;
	}
	public void setBeThreatenedReason(String beThreatenedReason) {
		this.beThreatenedReason = beThreatenedReason;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public Date getCaseTime() {
		return caseTime;
	}
	public void setCaseTime(Date caseTime) {
		this.caseTime = caseTime;
	}
	public Date getClosingTime() {
		return closingTime;
	}
	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}
}
