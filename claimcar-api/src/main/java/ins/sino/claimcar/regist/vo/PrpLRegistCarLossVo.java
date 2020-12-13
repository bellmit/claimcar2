package ins.sino.claimcar.regist.vo;

/**
 * Custom VO class of PO PrpLRegistCarLoss
 */ 
public class PrpLRegistCarLossVo extends PrpLRegistCarLossVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String loss;
	private String repairMobile;//修理厂联系人电话
	private String repairLinker;//修理厂联系人
	private String repairAddress;//修理厂地址
	public String getLoss() {
		return loss;
	}
	public void setLoss(String loss) {
		this.loss = loss;
	}
	public String getRepairMobile() {
		return repairMobile;
	}
	public void setRepairMobile(String repairMobile) {
		this.repairMobile = repairMobile;
	}
	public String getRepairLinker() {
		return repairLinker;
	}
	public void setRepairLinker(String repairLinker) {
		this.repairLinker = repairLinker;
	}
	public String getRepairAddress() {
		return repairAddress;
	}
	public void setRepairAddress(String repairAddress) {
		this.repairAddress = repairAddress;
	}
	
}
