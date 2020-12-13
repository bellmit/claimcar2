package ins.sino.claimcar.losscar.vo;


/**
 * Custom VO class of PO PrpLDlossCarComp
 */ 
public class PrpLDlossCarCompVo extends PrpLDlossCarCompVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String isVerifyPriceChanged = "0"; // 核价金额是否改变
	private String isVerifyLossChanged = "0"; // 核损金额是否改变
	private String isYangJie; // 是否送阳杰了;1为已送



    public String getIsVerifyPriceChanged() {
        return isVerifyPriceChanged;
    }

    public void setIsVerifyPriceChanged(String isVerifyPriceChanged) {
        this.isVerifyPriceChanged = isVerifyPriceChanged;
    }

    public String getIsVerifyLossChanged() {
        return isVerifyLossChanged;
    }

    public void setIsVerifyLossChanged(String isVerifyLossChanged) {
        this.isVerifyLossChanged = isVerifyLossChanged;
    }

	public String getIsYangJie() {
		return isYangJie;
	}

	public void setIsYangJie(String isYangJie) {
		this.isYangJie = isYangJie;
	}

}
