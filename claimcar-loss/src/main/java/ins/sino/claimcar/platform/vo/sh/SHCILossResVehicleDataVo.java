/******************************************************************************
 * CREATETIME : 2016年5月26日 下午5:28:21
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 车型信息VEHICLE_LIST
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResVehicleDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 损失车辆号牌种类

	@XmlElement(name = "MADE_FACTORY", required = true)
	private String madeFactory;// 制造厂名称

	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 返回类型代码

	@XmlElement(name = "ERROR_MESSAGE", required = true)
	private String errorMessage;// 错误描述

	/**
	 * 配件信息（多条）FITTING_LIST隶属于车型信息
	 */
	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private List<SHCILossResFittingDataVo> fittingDataVo;// 配件信息（多条）

	/**
	 * 渠道信息（多条）SALES_CHANNLE_CODE_LIST
	 */
	@XmlElementWrapper(name = "SALES_CHANNLE_LIST")
	@XmlElement(name = "SALES_CHANNLE_DATA")
	private List<SHCILossResSalesChannleDataVo> salesChannleDataVo;// 渠道信息（多条）

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

	public List<SHCILossResFittingDataVo> getFittingDataVo() {
		return fittingDataVo;
	}

	public void setFittingDataVo(List<SHCILossResFittingDataVo> fittingDataVo) {
		this.fittingDataVo = fittingDataVo;
	}

	public List<SHCILossResSalesChannleDataVo> getSalesChannleDataVo() {
		return salesChannleDataVo;
	}

	public void setSalesChannleDataVo(List<SHCILossResSalesChannleDataVo> salesChannleDataVo) {
		this.salesChannleDataVo = salesChannleDataVo;
	}
}
