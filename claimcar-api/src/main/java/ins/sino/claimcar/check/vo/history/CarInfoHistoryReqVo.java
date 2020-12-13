package ins.sino.claimcar.check.vo.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class CarInfoHistoryReqVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "BasePart")
	private CarInfoHistoryReqBasePartVo ReqBasePartVo;// 号牌种类代码；参见代码

	/**
	 * @return the reqBasePartVo
	 */
	public CarInfoHistoryReqBasePartVo getReqBasePartVo() {
		return ReqBasePartVo;
	}

	/**
	 * @param reqBasePartVo the reqBasePartVo to set
	 */
	public void setReqBasePartVo(CarInfoHistoryReqBasePartVo reqBasePartVo) {
		ReqBasePartVo = reqBasePartVo;
	}

	

}
