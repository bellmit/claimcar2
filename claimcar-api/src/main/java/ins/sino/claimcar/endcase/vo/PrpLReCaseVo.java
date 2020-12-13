package ins.sino.claimcar.endcase.vo;

/**
 * Custom VO class of PO PrpLReCase
 */ 
public class PrpLReCaseVo extends PrpLReCaseVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private PrpLReCaseTextVo prpLReCaseText = null;

	public PrpLReCaseTextVo getPrpLReCaseText() {
		if(prpLReCaseText==null&&!getPrpLReCaseTexts().isEmpty()){
			prpLReCaseText=getPrpLReCaseTexts().get(0);
		}
		return prpLReCaseText;
	}

	public void setPrpLReCaseText(PrpLReCaseTextVo prpLReCaseText) {
		this.prpLReCaseText = prpLReCaseText;
	}
	
}
