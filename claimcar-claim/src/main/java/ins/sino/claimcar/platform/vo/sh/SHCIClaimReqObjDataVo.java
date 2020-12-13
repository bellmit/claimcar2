/******************************************************************************
 * CREATETIME : 2016年5月30日 下午12:00:47
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 损失情况列表（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIClaimReqObjDataVo {

	/**
	 * 标的名称
	 */
	@XmlElement(name = "OBJ_NAME", required = true)
	private String objName;// 第三方标的

	/**
	 * 号牌种类
	 */
	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 号牌种类

	/**
	 * 标的损失类型
	 */
	@XmlElement(name = "OBJ_TYPE", required = true)
	private String objType;// 标的损失类型

	
	
	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

}
