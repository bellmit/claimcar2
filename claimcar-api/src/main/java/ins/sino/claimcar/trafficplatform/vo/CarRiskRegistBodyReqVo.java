package ins.sino.claimcar.trafficplatform.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 山东预警报文
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("BODY")
public class CarRiskRegistBodyReqVo {

	/**
	 * 基本信息
	 */
    @XStreamAlias("BasePart")
	private CarRiskRegistBasePartReqVo carRiskRegistBasePartReqVo;
	
	/**
	 * 三者车辆情况列表
	 */
	//@XmlElementWrapper(name = "THIRD_VEHICLE_LIST")
    @XStreamImplicit(itemFieldName="ThirdVehicleData")
	private List<CarRiskRegistThirdVehicleDataReqVo> carRiskRegistThirdVehicleDataReqs;

	
	/**
	 * 损失情况列表
	 */
	//@XmlElementWrapper(name = "LOSS_LIST")
    @XStreamImplicit(itemFieldName="LossData")
	private List<CarRiskRegistLossDataReqVo> carRiskRegistLossDataReqs;
    
    

    public List<CarRiskRegistThirdVehicleDataReqVo> getCarRiskRegistThirdVehicleDataReqs() {
		return carRiskRegistThirdVehicleDataReqs;
	}

	public void setCarRiskRegistThirdVehicleDataReqs(
			List<CarRiskRegistThirdVehicleDataReqVo> carRiskRegistThirdVehicleDataReqs) {
		this.carRiskRegistThirdVehicleDataReqs = carRiskRegistThirdVehicleDataReqs;
	}

	public List<CarRiskRegistLossDataReqVo> getCarRiskRegistLossDataReqs() {
		return carRiskRegistLossDataReqs;
	}

	public void setCarRiskRegistLossDataReqs(
			List<CarRiskRegistLossDataReqVo> carRiskRegistLossDataReqs) {
		this.carRiskRegistLossDataReqs = carRiskRegistLossDataReqs;
	}

	public CarRiskRegistBasePartReqVo getCarRiskRegistBasePartReqVo() {
        return carRiskRegistBasePartReqVo;
    }

    public void setCarRiskRegistBasePartReqVo(CarRiskRegistBasePartReqVo carRiskRegistBasePartReqVo) {
        this.carRiskRegistBasePartReqVo = carRiskRegistBasePartReqVo;
    }

}
