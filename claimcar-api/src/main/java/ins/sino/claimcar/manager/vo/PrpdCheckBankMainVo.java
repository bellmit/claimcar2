package ins.sino.claimcar.manager.vo;

public class PrpdCheckBankMainVo extends PrpdCheckBankMainVoBase implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private PrpdcheckBankVo prpdcheckBank=null;
	
	public PrpdcheckBankVo getPrpdcheckBank(){
		if(prpdcheckBank==null&&!getPrpdcheckBanks().isEmpty()){
			for(PrpdcheckBankVo vo:getPrpdcheckBanks()){
				if("1".equals(vo.getVaildFlag())){
					prpdcheckBank=vo;
				}
			}
			
		}
		return prpdcheckBank;
	}
	public void setPrpdcheckBank(PrpdcheckBankVo prpdcheckBank) {
		this.prpdcheckBank = prpdcheckBank;
	}

}
