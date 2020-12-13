/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:30:45
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIEndCaseObjDataVo {

	@XmlElement(name = "OBJ_NAME")
	private String objName;// 标的名称

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 号牌种类

	@XmlElement(name = "OBJ_TYPE", required = true)
	private String objType;// 标的损失类型

	@XmlElement(name = "MAIN_THIRD", required = true)
	private String mainThird;// 是否承保车辆

	/**
	 * @return 返回 objName 标的名称
	 */
	public String getObjName() {
		return objName;
	}

	/**
	 * @param objName 要设置的 标的名称
	 */
	public void setObjName(String objName) {
		this.objName = objName;
	}

	/**
	 * @return 返回 vehicleType 号牌种类
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 号牌种类
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 objType 标的损失类型
	 */
	public String getObjType() {
		return objType;
	}

	/**
	 * @param objType 要设置的 标的损失类型
	 */
	public void setObjType(String objType) {
		this.objType = objType;
	}

	/**
	 * @return 返回 mainThird 是否承保车辆
	 */
	public String getMainThird() {
		return mainThird;
	}

	/**
	 * @param mainThird 要设置的 是否承保车辆
	 */
	public void setMainThird(String mainThird) {
		this.mainThird = mainThird;
	}

}
