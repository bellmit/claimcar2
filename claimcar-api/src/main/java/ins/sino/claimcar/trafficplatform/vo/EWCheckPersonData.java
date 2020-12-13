package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PersonData")
public class EWCheckPersonData implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("PersonPayType")
	private String personPayType;
	
	@XStreamAlias("EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;

	@XStreamAlias("PersonProperty")
	private String personProperty;

	@XStreamAlias("TrafficType")
	private String trafficType;

	@XStreamAlias("CheckerName")
	private String checkerName;

	@XStreamAlias("CheckerCode")
	private String checkerCode;

	@XStreamAlias("CheckerCertiCode")
	private String checkerCertiCode;

	@XStreamAlias("CheckStartTime")
	private String checkStartTime;

	@XStreamAlias("CheckEndTime")
	private String checkEndTime;

	@XStreamAlias("CheckAddr")
	private String checkAddr;

	@XStreamAlias("CheckDesc")
	private String checkDesc;

	@XStreamAlias("PersonName")
	private String personName;

	@XStreamAlias("CertiType")
	private String certiType;
	
	@XStreamAlias("CertiCode")
	private String certiCode;

	public String getPersonPayType() {
		return personPayType;
	}

	public void setPersonPayType(String personPayType) {
		this.personPayType = personPayType;
	}

	public BigDecimal getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getPersonProperty() {
		return personProperty;
	}

	public void setPersonProperty(String personProperty) {
		this.personProperty = personProperty;
	}

	public String getTrafficType() {
		return trafficType;
	}

	public void setTrafficType(String trafficType) {
		this.trafficType = trafficType;
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

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getCertiCode() {
		return certiCode;
	}

	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}
	
}
