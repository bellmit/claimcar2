package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BasePart")
public class EWCheckBasePart implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;  //理赔编号
	
	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;  //报案号
	
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;  //投保确认码
	
	@XStreamAlias("AccidentType")
	private String accidentType;  //保险事故分类
	
	@XStreamAlias("AccidentLiability")
	private String accidentLiability;  //事故责任划分
	
	@XStreamAlias("OptionType")
	private String optionType;  //事故处理方式
	
	@XStreamAlias("LossCauseCode")
	private String lossCauseCode;  //出险原因代码
	
	@XStreamAlias("SubCertiType")
	private String subCertiType;  //责任认定书类型
	
	@XStreamAlias("IsSingleAccident")
	private String isSingleAccident;  //是否单车事故
	
	@XStreamAlias("LongLaType")
	private String longLaType;  //经纬度类型
	
	@XStreamAlias("Longitude")
	private BigDecimal longitude;  //出险地点经度
	
	@XStreamAlias("Latitude")
	private BigDecimal latitude;  //出险地点纬度

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getAccidentLiability() {
		return accidentLiability;
	}

	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getLossCauseCode() {
		return lossCauseCode;
	}

	public void setLossCauseCode(String lossCauseCode) {
		this.lossCauseCode = lossCauseCode;
	}

	public String getSubCertiType() {
		return subCertiType;
	}

	public void setSubCertiType(String subCertiType) {
		this.subCertiType = subCertiType;
	}

	public String getIsSingleAccident() {
		return isSingleAccident;
	}

	public void setIsSingleAccident(String isSingleAccident) {
		this.isSingleAccident = isSingleAccident;
	}

	public String getLongLaType() {
		return longLaType;
	}

	public void setLongLaType(String longLaType) {
		this.longLaType = longLaType;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	
}
