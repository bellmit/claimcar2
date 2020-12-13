package ins.sino.claimcar.check.vo.history;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CarInfoHistoryResBodyVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "NotificationData")
	private List<CarInfoHistoryResBasePartDataVo> thirdCarDataListVo;

	@XmlElement(name = "KsErrorPro")
	private List<CarInfoHistoryResKsBasePartDataVo> ksErrorProListVo;

	/**
	 * @return the thirdCarDataListVo
	 */
	public List<CarInfoHistoryResBasePartDataVo> getThirdCarDataListVo() {
		return thirdCarDataListVo;
	}

	/**
	 * @param thirdCarDataListVo
	 *            the thirdCarDataListVo to set
	 */
	public void setThirdCarDataListVo(
			List<CarInfoHistoryResBasePartDataVo> thirdCarDataListVo) {
		this.thirdCarDataListVo = thirdCarDataListVo;
	}

	/**
	 * @return the ksErrorProListVo
	 */
	public List<CarInfoHistoryResKsBasePartDataVo> getKsErrorProListVo() {
		return ksErrorProListVo;
	}

	/**
	 * @param ksErrorProListVo
	 *            the ksErrorProListVo to set
	 */
	public void setKsErrorProListVo(
			List<CarInfoHistoryResKsBasePartDataVo> ksErrorProListVo) {
		this.ksErrorProListVo = ksErrorProListVo;
	}

}
