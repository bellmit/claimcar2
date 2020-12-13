/******************************************************************************
 * CREATETIME : 2016年4月27日 上午11:36:33
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 车险信息平台交强险V6.0.0接口 -->查勘登记vo -->商业-->请求报文body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BICheckReqBodyVo {

	/** 基本信息 **/
	@XmlElement(name = "BasePart")
	private BICheckReqBasePartVo basePartVo;

	/** 车辆损失情况列表 **/
	@XmlElement(name = "VehicleData")
	private List<BICheckReqVehicleDataVo> vehicleDataVo;

	/** 财产损失情况列表 **/
	@XmlElement(name = "ProtectData")
	private List<BICheckReqProtectDataVo> protectDataVo;

	/** 人员损失情况列表 **/
	@XmlElement(name = "PersonData")
	private List<BICheckReqPersonDataVo> personDataVo;

	/** 代位信息列表 **/
	@XmlElement(name = "SubrogationData")
	private List<BICheckReqSubrogationDataVo> subrogationDataVo;

	
	
	public BICheckReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(BICheckReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

	public List<BICheckReqVehicleDataVo> getVehicleDataVo() {
		return vehicleDataVo;
	}

	public void setVehicleDataVo(List<BICheckReqVehicleDataVo> vehicleDataVo) {
		this.vehicleDataVo = vehicleDataVo;
	}

	public List<BICheckReqProtectDataVo> getProtectDataVo() {
		return protectDataVo;
	}

	public void setProtectDataVo(List<BICheckReqProtectDataVo> protectDataVo) {
		this.protectDataVo = protectDataVo;
	}

	public List<BICheckReqPersonDataVo> getPersonDataVo() {
		return personDataVo;
	}

	public void setPersonDataVo(List<BICheckReqPersonDataVo> personDataVo) {
		this.personDataVo = personDataVo;
	}

	public List<BICheckReqSubrogationDataVo> getSubrogationDataVo() {
		return subrogationDataVo;
	}

	public void setSubrogationDataVo(List<BICheckReqSubrogationDataVo> subrogationDataVo) {
		this.subrogationDataVo = subrogationDataVo;
	}

}
