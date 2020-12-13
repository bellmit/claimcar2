package ins.sino.claimcar.losscar.vo;


/**
 * Custom VO class of PO PrpLDlossCarSubRisk
 */ 
public class PrpLDlossCarSubRiskVo extends PrpLDlossCarSubRiskVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
    private String isVerifyLossChanged = "0"; // 核损金额是否改变

    public String getIsVerifyLossChanged() {
        return isVerifyLossChanged;
    }

    public void setIsVerifyLossChanged(String isVerifyLossChanged) {
        this.isVerifyLossChanged = isVerifyLossChanged;
    }
}
