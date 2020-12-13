package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;
import java.util.List;

public class ReportPhoneHistoryClaims implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BasicsInfo> BasicsInfo;
	private List<TeleAgg> TeleAgg;
	public List<BasicsInfo> getBasicsInfo() {
		return BasicsInfo;
	}
	public void setBasicsInfo(List<BasicsInfo> basicsInfo) {
		BasicsInfo = basicsInfo;
	}
	public List<TeleAgg> getTeleAgg() {
		return TeleAgg;
	}
	public void setTeleAgg(List<TeleAgg> teleAgg) {
		TeleAgg = teleAgg;
	}
	
	
	
}
