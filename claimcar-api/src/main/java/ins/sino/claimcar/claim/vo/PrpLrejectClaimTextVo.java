package ins.sino.claimcar.claim.vo;

/**
 * Custom VO class of PO PrpLrejectClaimText
 */ 
public class PrpLrejectClaimTextVo extends PrpLrejectClaimTextVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String stationName;//岗位
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
}
