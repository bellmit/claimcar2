package ins.sino.claimcar.realtimequery.vo;

import java.io.Serializable;

public class PrpLDamageInfoVoBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String lossPartCode;
	private PrpLVehicleInfoVo prpLVehicleInfo;
	private Long upperId;  //维护一个父级关系表id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLossPartCode() {
		return lossPartCode;
	}
	public void setLossPartCode(String lossPartCode) {
		this.lossPartCode = lossPartCode;
	}
	public PrpLVehicleInfoVo getPrpLVehicleInfo() {
		return prpLVehicleInfo;
	}
	public void setPrpLVehicleInfo(PrpLVehicleInfoVo prpLVehicleInfo) {
		this.prpLVehicleInfo = prpLVehicleInfo;
	}
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	

}
