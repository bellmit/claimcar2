package ins.sino.claimcar.recloss.vo;

/**
 * Custom VO class of PO PrpLRecLoss
 */ 
public class PrpLRecLossVo extends PrpLRecLossVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String recLossInd;//是否全部回收完毕

	public String getRecLossInd() {
		return recLossInd;
	}

	public void setRecLossInd(String recLossInd) {
		this.recLossInd = recLossInd;
	}
	
	
	
	
}
