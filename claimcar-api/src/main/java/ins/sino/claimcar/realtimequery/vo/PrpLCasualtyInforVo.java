package ins.sino.claimcar.realtimequery.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpLCasualtyInforVo extends PrpLCasualtyInforVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date changeTime;
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
}
