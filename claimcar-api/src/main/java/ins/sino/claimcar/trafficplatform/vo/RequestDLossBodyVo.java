package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("Body")
public class RequestDLossBodyVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XStreamAlias("BasePart")
	private RequestDLossBasePartVo basePartVo;

	@XStreamImplicit(itemFieldName="VehicleData")
	private List<VehicleDataVo> vehicleDataList;//车辆损失列表
	 
	@XStreamImplicit(itemFieldName="ProtectData")
	private List<ProtectDataVo> protectDataList;//财产损失列表
	 
	@XStreamImplicit(itemFieldName="PersonData")
	private List<PersonDataVo> personDataList;//人伤伤亡情况列表

	@XStreamImplicit(itemFieldName="InjuryIdentifyInfoData")
	private List<InjuryIdentifyInfoDataVo> injuryIdentifyInfoDataList;//伤残鉴定机构列表

	
	public RequestDLossBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(RequestDLossBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<VehicleDataVo> getVehicleDataList() {
		return vehicleDataList;
	}

	public void setVehicleDataList(List<VehicleDataVo> vehicleDataList) {
		this.vehicleDataList = vehicleDataList;
	}

	public List<ProtectDataVo> getProtectDataList() {
		return protectDataList;
	}

	public void setProtectDataList(List<ProtectDataVo> protectDataList) {
		this.protectDataList = protectDataList;
	}

	public List<PersonDataVo> getPersonDataList() {
		return personDataList;
	}

	public void setPersonDataList(List<PersonDataVo> personDataList) {
		this.personDataList = personDataList;
	}

	public List<InjuryIdentifyInfoDataVo> getInjuryIdentifyInfoDataList() {
		return injuryIdentifyInfoDataList;
	}

	public void setInjuryIdentifyInfoDataList(
			List<InjuryIdentifyInfoDataVo> injuryIdentifyInfoDataList) {
		this.injuryIdentifyInfoDataList = injuryIdentifyInfoDataList;
	}
	
}
