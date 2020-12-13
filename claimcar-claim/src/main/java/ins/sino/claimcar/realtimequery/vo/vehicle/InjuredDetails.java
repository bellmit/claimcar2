package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;

public class InjuredDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String injuryPart;  //受伤部位	
	private String injuryLevelCode;  //伤残程度代码	
	private String deathTime;  //死亡时间	
	public String getInjuryPart() {
		return injuryPart;
	}
	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}
	public String getInjuryLevelCode() {
		return injuryLevelCode;
	}
	public void setInjuryLevelCode(String injuryLevelCode) {
		this.injuryLevelCode = injuryLevelCode;
	}
	public String getDeathTime() {
		return deathTime;
	}
	public void setDeathTime(String deathTime) {
		this.deathTime = deathTime;
	}
	
	
}
