package ins.sino.claimcar.losscar.vo;


/**
 * Custom VO class of PO PrpLDlossCarRepair
 */ 
public class PrpLDlossCarRepairVo extends PrpLDlossCarRepairVoBase implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

	private String isVerifyLossChanged = "0"; // 核损金额是否改变

    public String getIsVerifyLossChanged() {
        return isVerifyLossChanged;
    }

    public void setIsVerifyLossChanged(String isVerifyLossChanged) {
        this.isVerifyLossChanged = isVerifyLossChanged;
    }


}
