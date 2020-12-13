/******************************************************************************
 * CREATETIME : 2016年6月1日 下午3:19:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 结案追加. 车型信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHEndCaseAddResVehicleDataVo {

	/**
	 * 配件信息（多条）FITTING_LIST隶属于车型信息
	 */
	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private SHEndCaseAddResFittingDataVo fittingDataVo;

	/**
	 * 渠道信息（多条）SALES_CHANNLE_CODE_LIST
	 */
	@XmlElementWrapper(name = "SALES_CHANNLE_LIST")
	@XmlElement(name = "SALES_CHANNLE_DATA")
	private SHEndCaseAddResSalesChannleDataVo salesChannleDataVo;

	/**
	 * 损失车辆号牌号码
	 */
	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失车辆号牌号码

	/**
	 * 损失车辆号牌种类
	 */
	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 损失车辆号牌种类

	/**
	 * 制造厂名称
	 */
	@XmlElement(name = "MADE_FACTORY", required = true)
	private String madeFactory;// 制造厂名称

	/**
	 * 返回类型代码
	 */
	@XmlElement(name = "RESPONSE_CODE", required = true)
	private String responseCode;// 返回类型代码

	/**
	 * 错误描述
	 */
	@XmlElement(name = "ERROR_MESSAGE", required = true)
	private String errorMessage;// 错误描述

	public SHEndCaseAddResFittingDataVo getFittingDataVo() {
		return fittingDataVo;
	}

	public void setFittingDataVo(SHEndCaseAddResFittingDataVo fittingDataVo) {
		this.fittingDataVo = fittingDataVo;
	}

	public SHEndCaseAddResSalesChannleDataVo getSalesChannleDataVo() {
		return salesChannleDataVo;
	}

	public void setSalesChannleDataVo(SHEndCaseAddResSalesChannleDataVo salesChannleDataVo) {
		this.salesChannleDataVo = salesChannleDataVo;
	}

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getMadeFactory() {
		return madeFactory;
	}

	public void setMadeFactory(String madeFactory) {
		this.madeFactory = madeFactory;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
