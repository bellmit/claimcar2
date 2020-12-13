package ins.sino.claimcar.regist.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLTmpCMain
 */ 
public class PrpLTmpCMainVo extends PrpLTmpCMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Date damageTime;
	public Date getDamageTime() {
		return damageTime;
	}
	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}
	
}
