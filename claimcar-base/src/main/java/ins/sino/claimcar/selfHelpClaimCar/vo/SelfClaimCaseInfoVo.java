package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASEINFO")
public class SelfClaimCaseInfoVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ORDERNO")
	private String orderNo;//订单号
	
	@XStreamAlias("BIPOLICYNO")
	private String biPolicyNo; 
	
	@XStreamAlias("CIPOLICYNO")
	private String ciPolicyNo; 
	
	@XStreamAlias("CASECARNO")
	private String caseCarNo; 

	@XStreamAlias("CASETELEPHONE")
	private String caseTelephone; 

	@XStreamAlias("CASELON")
	private String caseLon; 

	@XStreamAlias("CASELAT")
	private String caseLat; 

	@XStreamAlias("CASEADDRESS")
	private String caseAddress; 

	@XStreamAlias("CASEDATE")
	private String caseDate; 

	@XStreamAlias("RESTIME")
	private String resTime; 

	@XStreamAlias("ACCIDENTTYPE")
	private String accIdentType; 
	
	@XStreamAlias("AREAID")
	private String areaId; 

	@XStreamAlias("WEATHER")
	private String weather; 

	@XStreamAlias("TYPE")
	private String type; 
	
	@XStreamAlias("DAMAGECODE")
	private String damageCode;
	
	@XStreamAlias("ISDVIVE")
	private String isDvive;
	
	@XStreamAlias("ISHUMINJURY")
	private String isHumInjury;
	
	@XStreamAlias("CASECARLIST")
	private List<SelfClaimCaseCarInfoVo> acccInfoVos;
	
	@XStreamAlias("DAMAGETIME")
	private String damageTime;

	@XStreamAlias("DAMAGETYPE")
	private String damageType;
	
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBiPolicyNo() {
		return biPolicyNo;
	}

	public void setBiPolicyNo(String biPolicyNo) {
		this.biPolicyNo = biPolicyNo;
	}

	public String getCiPolicyNo() {
		return ciPolicyNo;
	}

	public void setCiPolicyNo(String ciPolicyNo) {
		this.ciPolicyNo = ciPolicyNo;
	}

	public String getCaseCarNo() {
		return caseCarNo;
	}

	public void setCaseCarNo(String caseCarNo) {
		this.caseCarNo = caseCarNo;
	}

	public String getCaseTelephone() {
		return caseTelephone;
	}

	public void setCaseTelephone(String caseTelephone) {
		this.caseTelephone = caseTelephone;
	}

	public String getCaseLon() {
		return caseLon;
	}

	public void setCaseLon(String caseLon) {
		this.caseLon = caseLon;
	}

	public String getCaseLat() {
		return caseLat;
	}

	public void setCaseLat(String caseLat) {
		this.caseLat = caseLat;
	}

	public String getCaseAddress() {
		return caseAddress;
	}

	public void setCaseAddress(String caseAddress) {
		this.caseAddress = caseAddress;
	}

	public String getCaseDate() {
		return caseDate;
	}

	public void setCaseDate(String caseDate) {
		this.caseDate = caseDate;
	}

	public String getResTime() {
		return resTime;
	}

	public void setResTime(String resTime) {
		this.resTime = resTime;
	}

	public String getAccIdentType() {
		return accIdentType;
	}

	public void setAccIdentType(String accIdentType) {
		this.accIdentType = accIdentType;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getIsDvive() {
		return isDvive;
	}

	public void setIsDvive(String isDvive) {
		this.isDvive = isDvive;
	}

	public String getIsHumInjury() {
		return isHumInjury;
	}

	public void setIsHumInjury(String isHumInjury) {
		this.isHumInjury = isHumInjury;
	}

	public List<SelfClaimCaseCarInfoVo> getAcccInfoVos() {
		return acccInfoVos;
	}

	public void setAcccInfoVos(List<SelfClaimCaseCarInfoVo> acccInfoVos) {
		this.acccInfoVos = acccInfoVos;
	}

	public String getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(String damageTime) {
		this.damageTime = damageTime;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}
	
	
	
}
