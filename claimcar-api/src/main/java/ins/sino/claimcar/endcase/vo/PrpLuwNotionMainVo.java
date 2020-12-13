package ins.sino.claimcar.endcase.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLuwNotionMain
 */
public class PrpLuwNotionMainVo extends PrpLuwNotionMainVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal pisderoAmout;//人伤减损金额
	private BigDecimal cisderoAmout;//车物减损金额
	
	public BigDecimal getPisderoAmout() {
		return pisderoAmout;
	}
	public void setPisderoAmout(BigDecimal pisderoAmout) {
		this.pisderoAmout = pisderoAmout;
	}
	public BigDecimal getCisderoAmout() {
		return cisderoAmout;
	}
	public void setCisderoAmout(BigDecimal cisderoAmout) {
		this.cisderoAmout = cisderoAmout;
	}
	

}
