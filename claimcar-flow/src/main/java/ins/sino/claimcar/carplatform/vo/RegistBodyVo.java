package ins.sino.claimcar.carplatform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年3月14日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class RegistBodyVo {

	@XmlElement(name = "BASE_PART")
	private RegistBaseVo baseVo;

	@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
	@XmlElement(name = "THIRD_VEHICLE_DATA")
	private List<RegistCarVehiceleVo> carVehList;

	public RegistBaseVo getBaseVo() {
		return baseVo;
	}

	public void setBaseVo(RegistBaseVo baseVo) {
		this.baseVo = baseVo;
	}

	public List<RegistCarVehiceleVo> getCarVehList() {
		return carVehList;
	}

	public void setCarVehList(List<RegistCarVehiceleVo> carVehList) {
		this.carVehList = carVehList;
	}
}
