package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ProtectData")
public class EWCheckPropData implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("ProtectName")
	private String protectName;
	
	@XStreamAlias("LossDesc")
	private String lossDesc;
	
	@XStreamAlias("LossNum")
	private String lossNum;
	
	@XStreamAlias("EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;
	
	@XStreamAlias("Owner")
	private String owner;
	
	@XStreamAlias("ProtectProperty")
	private String protectProperty;
	
	@XStreamAlias("FieldType")
	private String fieldType;
	
	@XStreamAlias("CheckStartTime")
	private String checkStartTime;
	
	@XStreamAlias("CheckEndTime")
	private String checkEndTime;
	
	@XStreamAlias("CheckerName")
	private String checkerName;
	
	@XStreamAlias("CheckerCode")
	private String checkerCode;
	
	@XStreamAlias("CheckerCertiCode")
	private String checkerCertiCode;
	
	@XStreamAlias("CheckAddr")
	private String checkAddr;
	
	@XStreamAlias("CheckDesc")
	private String checkDesc;

	public String getProtectName() {
		return protectName;
	}

	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}

	public String getLossDesc() {
		return lossDesc;
	}

	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	public String getLossNum() {
		return lossNum;
	}

	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}

	public BigDecimal getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProtectProperty() {
		return protectProperty;
	}

	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getCheckStartTime() {
		return checkStartTime;
	}

	public void setCheckStartTime(String checkStartTime) {
		this.checkStartTime = checkStartTime;
	}

	public String getCheckEndTime() {
		return checkEndTime;
	}

	public void setCheckEndTime(String checkEndTime) {
		this.checkEndTime = checkEndTime;
	}

	public String getCheckerName() {
		return checkerName;
	}

	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	public String getCheckerCode() {
		return checkerCode;
	}

	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	public String getCheckAddr() {
		return checkAddr;
	}

	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	
}
