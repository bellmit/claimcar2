package ins.sino.claimcar.realtimequery.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpLAntiFraudVo extends PrpLAntiFraudVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date antiFraudTime;  //风险信息入库时间
	private String antiFraudName;  //人员姓名	
	public Date getAntiFraudTime() {
		return antiFraudTime;
	}
	public void setAntiFraudTime(Date antiFraudTime) {
		this.antiFraudTime = antiFraudTime;
	}
	public String getAntiFraudName() {
		return antiFraudName;
	}
	public void setAntiFraudName(String antiFraudName) {
		this.antiFraudName = antiFraudName;
	}
	
	
	
}
