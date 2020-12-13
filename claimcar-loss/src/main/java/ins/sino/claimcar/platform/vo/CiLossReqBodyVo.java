package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiLossReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiLossBaseVo lossBaseVo;// 基本信息

	@XmlElementWrapper(name = "CAR_LIST")
	@XmlElement(name = "CAR_DATA")
	private List<CiLossCarDataVo> lossCarDataVos;// 车辆损失情况列表

	@XmlElementWrapper(name = "PROTECT_LIST")
	@XmlElement(name = "PROTECT_DATA")
	private List<CiLossProtectDataVo> lossProtectDataVos;// 财产损失情况列表

	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<CiLossPersonDataVo> lossPersonDataVos;// 人员损失情况列表

	public CiLossBaseVo getLossBaseVo() {
		return lossBaseVo;
	}

	public void setLossBaseVo(CiLossBaseVo lossBaseVo) {
		this.lossBaseVo = lossBaseVo;
	}

	public List<CiLossCarDataVo> getLossCarDataVos() {
		return lossCarDataVos;
	}

	public void setLossCarDataVos(List<CiLossCarDataVo> lossCarDataVos) {
		this.lossCarDataVos = lossCarDataVos;
	}

	public List<CiLossProtectDataVo> getLossProtectDataVos() {
		return lossProtectDataVos;
	}

	public void setLossProtectDataVos(List<CiLossProtectDataVo> lossProtectDataVos) {
		this.lossProtectDataVos = lossProtectDataVos;
	}

	public List<CiLossPersonDataVo> getLossPersonDataVos() {
		return lossPersonDataVos;
	}

	public void setLossPersonDataVos(List<CiLossPersonDataVo> lossPersonDataVos) {
		this.lossPersonDataVos = lossPersonDataVos;
	}

}
