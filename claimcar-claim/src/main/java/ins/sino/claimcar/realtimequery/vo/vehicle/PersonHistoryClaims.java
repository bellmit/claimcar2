package ins.sino.claimcar.realtimequery.vo.vehicle;

import java.io.Serializable;
import java.util.List;

public class PersonHistoryClaims implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BasicsInfo> BasicsInfo;
	private List<VehicleInfo> VehicleInfo;
	private List<CasualtyInfor> CasualtyInfor;
	private List<PropertyLoss> PropertyLoss;
	public List<BasicsInfo> getBasicsInfo() {
		return BasicsInfo;
	}
	public void setBasicsInfo(List<BasicsInfo> basicsInfo) {
		BasicsInfo = basicsInfo;
	}
	public List<VehicleInfo> getVehicleInfo() {
		return VehicleInfo;
	}
	public void setVehicleInfo(List<VehicleInfo> vehicleInfo) {
		VehicleInfo = vehicleInfo;
	}
	public List<PropertyLoss> getPropertyLoss() {
		return PropertyLoss;
	}
	public void setPropertyLoss(List<PropertyLoss> propertyLoss) {
		PropertyLoss = propertyLoss;
	}
	public List<CasualtyInfor> getCasualtyInfor() {
		return CasualtyInfor;
	}
	public void setCasualtyInfor(List<CasualtyInfor> casualtyInfor) {
		CasualtyInfor = casualtyInfor;
	}
	
	
	
}
