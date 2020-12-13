package ins.sino.claimcar.realtimequery.vo;


import java.io.Serializable;
import java.util.Date;

public class PrpLInjuredDetailsVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String injuryPart;  //受伤部位	
	private String injuryLevelCode;  //伤残程度代码	
	private Date deathTime;  //死亡时间	
	private PrpLCasualtyInforVo prpLCasualtyInforVo;
	private Long upperId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public Date getDeathTime() {
		return deathTime;
	}
	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}
	public PrpLCasualtyInforVo getPrpLCasualtyInforVo() {
		return prpLCasualtyInforVo;
	}
	public void setPrpLCasualtyInforVo(PrpLCasualtyInforVo prpLCasualtyInforVo) {
		this.prpLCasualtyInforVo = prpLCasualtyInforVo;
	}
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
	
}
