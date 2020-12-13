package ins.sino.claimcar.manager.vo;

/**
 * Custom VO class of PO PrpLRepairFactory
 */
public class PrpLRepairFactoryVo extends PrpLRepairFactoryVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String agentCode;
	private String agentPhone;
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getAgentPhone() {
		return agentPhone;
	}
	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

}
