/******************************************************************************
 * CREATETIME : 2016年4月29日 下午3:31:04
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车险信息平台交强险V7.0.7接口 --> 查勘登记vo -->商业-->返回报文body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckResRiskInfoVo {

	public String getRiskSystemInfo() {
		return riskSystemInfo;
	}

	public void setRiskSystemInfo(String riskSystemInfo) {
		this.riskSystemInfo = riskSystemInfo;
	}

	public List<BICheckResVehicleRiskInfoVo> getVehicleRiskInfoVo() {
		return VehicleRiskInfoVo;
	}

	public void setVehicleRiskInfoVo(
			List<BICheckResVehicleRiskInfoVo> vehicleRiskInfoVo) {
		VehicleRiskInfoVo = vehicleRiskInfoVo;
	}

	public List<BICheckResPersonRiskInfoVo> getPersonRiskInfoVo() {
		return PersonRiskInfoVo;
	}

	public void setPersonRiskInfoVo(
			List<BICheckResPersonRiskInfoVo> personRiskInfoVo) {
		PersonRiskInfoVo = personRiskInfoVo;
	}

	public List<BICheckResInstituionRiskInfoVo> getInstituionRiskInfoVo() {
		return InstituionRiskInfoVo;
	}

	public void setInstituionRiskInfoVo(
			List<BICheckResInstituionRiskInfoVo> instituionRiskInfoVo) {
		InstituionRiskInfoVo = instituionRiskInfoVo;
	}

	@XmlElement(name = "RiskSystemInfo")
	private String riskSystemInfo;// 系统提示信息；参见代码

	@XmlElement(name = "VehicleRiskInfo")
	private List<BICheckResVehicleRiskInfoVo> VehicleRiskInfoVo;// 车辆风险类型提示信息

	@XmlElement(name = "PersonRiskInfo")
	private List<BICheckResPersonRiskInfoVo> PersonRiskInfoVo;// 人员风险类型提示信息

	@XmlElement(name = "InstituionRiskInfo")
	private List<BICheckResInstituionRiskInfoVo> InstituionRiskInfoVo;// 机构风险类型提示信息

}
