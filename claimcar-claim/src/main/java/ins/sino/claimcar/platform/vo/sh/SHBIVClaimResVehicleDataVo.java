/******************************************************************************
 * CREATETIME : 2016年6月1日 下午7:32:10
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIVClaimResVehicleDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 损失车辆号牌种类

	@XmlElement(name = "MADE_FACTORY")
	private String madeFactory;// 制造厂名称

	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 返回类型代码

	@XmlElement(name = "ERROR_MESSAGE")
	private String errorMessage;// 错误描述

	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHBIVClaimResFittingDataVo> fittingList;

	@XmlElementWrapper(name = "SALES_CHANNLE_LIST")
	@XmlElement(name = "SALES_CHANNLE_DATA")
	private List<SHBIVClaimResSalesChannleCodeDataVo> salesChannleCodeList;

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
	 * @return 返回 responseCode 返回类型代码
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode 要设置的 返回类型代码
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return 返回 errorMessage 错误描述
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage 要设置的 错误描述
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return 返回 fittingList。
	 */
	public List<SHBIVClaimResFittingDataVo> getFittingList() {
		return fittingList;
	}

	/**
	 * @param fittingList 要设置的 fittingList。
	 */
	public void setFittingList(List<SHBIVClaimResFittingDataVo> fittingList) {
		this.fittingList = fittingList;
	}

}
