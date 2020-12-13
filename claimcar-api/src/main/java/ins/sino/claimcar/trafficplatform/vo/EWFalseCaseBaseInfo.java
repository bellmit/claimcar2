package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamImplicitCollection;

@XStreamAlias("BaseInfo")
public class EWFalseCaseBaseInfo  implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("ClaimSequenceNo")
	private String claimSequenceNo;  //理赔编号
	
	@XStreamAlias("ClaimType")
	private String claimType;  
	
	@XStreamAlias("Remark")
	private String remark;  
	
	@XStreamImplicit(itemFieldName="Vehicle")
	private List<EWVehicleInfo> vehicleInfoList;

	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<EWVehicleInfo> getVehicleInfoList() {
		return vehicleInfoList;
	}

	public void setVehicleInfoList(List<EWVehicleInfo> vehicleInfoList) {
		this.vehicleInfoList = vehicleInfoList;
	}
	
}
