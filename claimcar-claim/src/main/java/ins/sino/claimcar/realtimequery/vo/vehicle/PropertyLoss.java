package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class PropertyLoss implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String protectName;  //损失财产名称
	private String protectProperty;  //财产属性
	private String underDefLoss;  //核损金额（财产）
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
	public String getUnderDefLoss() {
		return underDefLoss;
	}
	public void setUnderDefLoss(String underDefLoss) {
		this.underDefLoss = underDefLoss;
	}
	
	
	
}
