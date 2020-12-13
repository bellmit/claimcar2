/******************************************************************************
 * CREATETIME : 2016年5月26日 下午5:17:47
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 查勘、定损、核损请求平台信息返回BodyVo类-上海平台
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCILossResBodyVo {

	/**
	 * 车型信息VEHICLE_LIST
	 */
	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private SHCILossResVehicleDataVo vehicleDataVo;// 车型信息

	/**
	 * CLAIM_COVER_LIST
	 */
	@XmlElementWrapper(name = "CLAIM_COVER_LIST")
	@XmlElement(name = "CLAIM_COVER_DATA")
	private SHCILossResClaimCoverDataVo claimCoverDataVo;//

	/**
	 * 代位求偿码信息（多条）
	 */
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private SHCILossResSubrogationDataVo SubrogationDataVo;// 代位求偿码信息（多条）

	
	public SHCILossResVehicleDataVo getVehicleDataVo() {
		return vehicleDataVo;
	}

	public void setVehicleDataVo(SHCILossResVehicleDataVo vehicleDataVo) {
		this.vehicleDataVo = vehicleDataVo;
	}

	public SHCILossResClaimCoverDataVo getClaimCoverDataVo() {
		return claimCoverDataVo;
	}

	public void setClaimCoverDataVo(SHCILossResClaimCoverDataVo claimCoverDataVo) {
		this.claimCoverDataVo = claimCoverDataVo;
	}

	public SHCILossResSubrogationDataVo getSubrogationDataVo() {
		return SubrogationDataVo;
	}

	public void setSubrogationDataVo(SHCILossResSubrogationDataVo subrogationDataVo) {
		SubrogationDataVo = subrogationDataVo;
	}

}
