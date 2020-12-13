package ins.sino.claimcar.manager.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom VO class of PO PrpdIntermMain
 */ 
public class PrpdIntermMainVo extends PrpdIntermMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private PrpdIntermBankVo prpdIntermBank=null;
	
	public PrpdIntermBankVo getPrpdIntermBank(){
		if(prpdIntermBank==null&&!getPrpdIntermBanks().isEmpty()){
			for(PrpdIntermBankVo vo:getPrpdIntermBanks()){
				if("1".equals(vo.getVaildFlag())){
					prpdIntermBank=vo;
				}
			}
			
		}
		return prpdIntermBank;
	}
	public void setPrpdIntermBank(PrpdIntermBankVo prpdIntermBank) {
		this.prpdIntermBank = prpdIntermBank;
	}
	
}
