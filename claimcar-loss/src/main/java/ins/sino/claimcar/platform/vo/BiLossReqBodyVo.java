package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiLossReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiLossBaseVo lossBaseVo;// 基本信息

	@XmlElement(name = "VehicleData")
	private List<BiLossCarVehicleDataVo> vehicleDataVos;//车辆损失情况列表
	
	@XmlElement(name = "ProtectData")
	private List<BiLossProtectDataVo> protectDataVos;//财产损失情况列表
	
	@XmlElement(name = "PersonData")
	private List<BiLossPersonDataVo> personDataVos;//人员伤亡情况列表

	@XmlElement(name = "SubrogationData")
	private List<BiLossSubrogationDataVo> subrogationDataVos;//代位信息列表

	
	public BiLossBaseVo getLossBaseVo() {
		return lossBaseVo;
	}

	
	public void setLossBaseVo(BiLossBaseVo lossBaseVo) {
		this.lossBaseVo = lossBaseVo;
	}

	
	public List<BiLossCarVehicleDataVo> getVehicleDataVos() {
		return vehicleDataVos;
	}

	
	public void setVehicleDataVos(List<BiLossCarVehicleDataVo> vehicleDataVos) {
		this.vehicleDataVos = vehicleDataVos;
	}

	
	public List<BiLossProtectDataVo> getProtectDataVos() {
		return protectDataVos;
	}

	
	public void setProtectDataVos(List<BiLossProtectDataVo> protectDataVos) {
		this.protectDataVos = protectDataVos;
	}

	
	public List<BiLossPersonDataVo> getPersonDataVos() {
		return personDataVos;
	}

	
	public void setPersonDataVos(List<BiLossPersonDataVo> personDataVos) {
		this.personDataVos = personDataVos;
	}

	
	public List<BiLossSubrogationDataVo> getSubrogationDataVos() {
		return subrogationDataVos;
	}

	
	public void setSubrogationDataVos(List<BiLossSubrogationDataVo> subrogationDataVos) {
		this.subrogationDataVos = subrogationDataVos;
	}
	
	
}
