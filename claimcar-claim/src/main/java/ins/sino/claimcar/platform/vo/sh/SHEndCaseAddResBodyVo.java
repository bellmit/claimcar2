/******************************************************************************
 * CREATETIME : 2016年6月1日 下午3:14:27
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 结案追加--请求上海平台信息--返回BodyVo类-交强
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHEndCaseAddResBodyVo {

	/**
	 * 结案追加码
	 */
	@XmlElement(name = "CLAIM_ADD_CODE", required = true)
	private String claimAddCode;// 结案追加码

	/**
	 * 业务结案校验码
	 */
	@XmlElement(name = "CLAIM_CONFIRM_CODE")
	private String claimConfirmCode;// 业务结案校验码

	/**
	 * 车型信息VEHICLE_LIST
	 */
	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHEndCaseAddResVehicleDataVo> vehicleDataVo;

	public String getClaimAddCode() {
		return claimAddCode;
	}

	public void setClaimAddCode(String claimAddCode) {
		this.claimAddCode = claimAddCode;
	}

	public String getClaimConfirmCode() {
		return claimConfirmCode;
	}

	public void setClaimConfirmCode(String claimConfirmCode) {
		this.claimConfirmCode = claimConfirmCode;
	}

	public List<SHEndCaseAddResVehicleDataVo> getVehicleDataVo() {
		return vehicleDataVo;
	}

	public void setVehicleDataVo(List<SHEndCaseAddResVehicleDataVo> vehicleDataVo) {
		this.vehicleDataVo = vehicleDataVo;
	}

}
