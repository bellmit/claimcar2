package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;

/**
 *  车辆配件明细列表（隶属于车辆损失情况）
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class EstimateCarFittingDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 更换或修理标志 **/ 
	private String changeOrRepair; 
	
	/** 更换/修理配件名称 **/ 
	private String fittingName; 

	/** 更换/修理配件件数 **/ 
	private String fittingNum;

	public String getChangeOrRepair() {
		return changeOrRepair;
	}

	public void setChangeOrRepair(String changeOrRepair) {
		this.changeOrRepair = changeOrRepair;
	}

	public String getFittingName() {
		return fittingName;
	}

	public void setFittingName(String fittingName) {
		this.fittingName = fittingName;
	}

	public String getFittingNum() {
		return fittingNum;
	}

	public void setFittingNum(String fittingNum) {
		this.fittingNum = fittingNum;
	}

	

}
