package ins.sino.claimcar.ilog.vclaim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RepairInfo")
public class RepairInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@XStreamAlias("repairFlag")
	private String repairFlag = ""; // 维修类型

	public String getRepairFlag() {
		return repairFlag;
	}

	public void setRepairFlag(String repairFlag) {
		this.repairFlag = repairFlag;
	}
	
	
}
