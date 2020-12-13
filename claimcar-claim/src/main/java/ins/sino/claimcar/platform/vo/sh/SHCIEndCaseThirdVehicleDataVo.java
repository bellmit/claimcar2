/******************************************************************************
 * CREATETIME : 2016年6月6日 下午4:03:28
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseThirdVehicleDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 第三方车辆号牌号码

	@XmlElement(name = "VVEHICLE_TYPE", required = true)
	private String vvehicleType;// 第三方车辆号牌种类

	@XmlElement(name = "NIRRESPONSIBILITY_COMPANY", required = true)
	private String nirresponsibilityCompany;// 无责方保险公司代码

	@XmlElement(name = "ADVANCE_AMOUNT")
	private Double advanceAmount;// 垫付金额

	/**
	 * @return 返回 carMark 第三方车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 第三方车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vvehicleType 第三方车辆号牌种类
	 */
	public String getVvehicleType() {
		return vvehicleType;
	}

	/**
	 * @param vvehicleType 要设置的 第三方车辆号牌种类
	 */
	public void setVvehicleType(String vvehicleType) {
		this.vvehicleType = vvehicleType;
	}

	/**
	 * @return 返回 nirresponsibilityCompany 无责方保险公司代码
	 */
	public String getNirresponsibilityCompany() {
		return nirresponsibilityCompany;
	}

	/**
	 * @param nirresponsibilityCompany 要设置的 无责方保险公司代码
	 */
	public void setNirresponsibilityCompany(String nirresponsibilityCompany) {
		this.nirresponsibilityCompany = nirresponsibilityCompany;
	}

	/**
	 * @return 返回 advanceAmount 垫付金额
	 */
	public Double getAdvanceAmount() {
		return advanceAmount;
	}

	/**
	 * @param advanceAmount 要设置的 垫付金额
	 */
	public void setAdvanceAmount(Double advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

}
