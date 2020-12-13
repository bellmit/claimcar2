package ins.sino.claimcar.realtimequery.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrpLPropertyLossVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String protectName;  //损失财产名称
	private String protectProperty;  //财产属性
	private BigDecimal underDefLoss;  //核损金额（财产）
	private Long upperId;  //维护一个父级关系表id
	private String reportPhoneNo;   //报案电话
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProtectName() {
		return protectName;
	}
	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}
	public String getProtectProperty() {
		return protectProperty;
	}
	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}
	
	public BigDecimal getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(BigDecimal underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	
	
	
}
