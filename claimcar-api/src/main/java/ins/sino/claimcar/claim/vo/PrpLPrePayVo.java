package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLPrePay
 */ 
public class PrpLPrePayVo extends PrpLPrePayVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private BigDecimal maxAmt;
	
	public BigDecimal getMaxAmt() {
		return maxAmt;
	}
	
	public void setMaxAmt(BigDecimal maxAmt) {
		this.maxAmt = maxAmt;
	}
	
}
