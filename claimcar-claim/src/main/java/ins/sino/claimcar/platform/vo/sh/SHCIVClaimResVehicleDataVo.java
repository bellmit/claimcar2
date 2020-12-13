/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:35:40
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 车型信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimResVehicleDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 损失车辆号牌种类

	@XmlElement(name = "MADE_FACTORY")
	private String madeFactory;// 制造厂名称

	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 1-

	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;// 1-

	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private List<SHCIVClaimResFittingDataVo> fittingList;

	/**
	 * @return 返回 carMark 损失车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 损失车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 损失车辆号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 损失车辆号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 madeFactory 制造厂名称
	 */
	public String getMadeFactory() {
		return madeFactory;
	}

	/**
	 * @param madeFactory 要设置的 制造厂名称
	 */
	public void setMadeFactory(String madeFactory) {
		this.madeFactory = madeFactory;
	}

	/**
	 * @return 返回 responseCode 1-
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode 要设置的 1-
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return 返回 errorMessage 1-
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage 要设置的 1-
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return 返回 fittingList。
	 */
	public List<SHCIVClaimResFittingDataVo> getFittingList() {
		return fittingList;
	}

	/**
	 * @param fittingList 要设置的 fittingList。
	 */
	public void setFittingList(List<SHCIVClaimResFittingDataVo> fittingList) {
		this.fittingList = fittingList;
	}

}
