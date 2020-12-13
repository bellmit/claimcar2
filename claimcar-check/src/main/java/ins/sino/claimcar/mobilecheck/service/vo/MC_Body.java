package ins.sino.claimcar.mobilecheck.service.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("BODY")
public class MC_Body implements Serializable {

	/**  */
	private static final long serialVersionUID = 8719552477164983050L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; // 报案号
	
	@XStreamAlias("FIRSTSITEFLAG")
	private String firstSiteFlag; // 是否第一现场
	
	@XStreamAlias("DAMAGECODE")
	private String damageCode; // 出险原因
	
	@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty; // 事故责任  
	
	@XStreamAlias("INDEMNITYDUTYRATE")
	private String indemnityDutyRate; // 事故责任比例  
	
	@XStreamAlias("ISROBBERY")
	private String isRobbery; // 是否盗抢
	
	@XStreamAlias("ISMAJORCASE")
	private String isMajorCase; // 是否重大赔案
	
	@XStreamAlias("EXCESSTYPE")
	private String excessType; // 互碰自赔
	
	@XStreamAlias("SUBROGATETYPE")
	private String isSubrogateType; // 互碰代位求偿
	
	@XStreamAlias("BIZLOSSAMOUNT")
	private String bizLossAmount; // 商业险损失金额
	
	@XStreamAlias("LOSSAMOUNT")
	private String lossAmount; // 交强险损失金额
	
	@XStreamAlias("ISREJECTED")
	private String isRejected; // 是否建议拒赔
	
	@XStreamAlias("DANGERTYPE")
	private String damageType; // 出险类型
	
	@XStreamAlias("DRIVINGCARTYPE")
	private String drivingCarType; // 标准车型
	
	@XStreamAlias("TEXTTYPE")
	private String textType; // 查勘报告
	
	@XStreamAlias("CHECKLOSSLIST")
	private List<MC_CheckLossInfo> checkLossList;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getFirstSiteFlag() {
		return firstSiteFlag;
	}

	public void setFirstSiteFlag(String firstSiteFlag) {
		this.firstSiteFlag = firstSiteFlag;
	}

	public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getIndemnityDuty() {
		return indemnityDuty;
	}

	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}

	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	public String getIsRobbery() {
		return isRobbery;
	}

	public void setIsRobbery(String isRobbery) {
		this.isRobbery = isRobbery;
	}

	public String getIsMajorCase() {
		return isMajorCase;
	}

	public void setIsMajorCase(String isMajorCase) {
		this.isMajorCase = isMajorCase;
	}

	public String getExcessType() {
		return excessType;
	}

	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}

	public String getIsSubrogateType() {
		return isSubrogateType;
	}

	public void setIsSubrogateType(String isSubrogateType) {
		this.isSubrogateType = isSubrogateType;
	}

	public String getBizLossAmount() {
		return bizLossAmount;
	}

	public void setBizLossAmount(String bizLossAmount) {
		this.bizLossAmount = bizLossAmount;
	}

	public String getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}

	public String getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(String isRejected) {
		this.isRejected = isRejected;
	}

	public String getDamageType() {
		return damageType;
	}

	public void setDamageType(String damageType) {
		this.damageType = damageType;
	}

	public String getDrivingCarType() {
		return drivingCarType;
	}

	public void setDrivingCarType(String drivingCarType) {
		this.drivingCarType = drivingCarType;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}

	public List<MC_CheckLossInfo> getCheckLossList() {
		return checkLossList;
	}

	public void setCheckLossList(List<MC_CheckLossInfo> checkLossList) {
		this.checkLossList = checkLossList;
	}
	


}
