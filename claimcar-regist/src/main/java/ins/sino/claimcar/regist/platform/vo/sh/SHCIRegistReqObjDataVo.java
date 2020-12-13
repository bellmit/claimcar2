/******************************************************************************
 * CREATETIME : 2016年5月24日 下午4:02:11
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交强报案请求平台信息损失情况列表（多条）-上海平台
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistReqObjDataVo {

	@XmlElement(name = "OBJ_NAME", required = true)
	private String objName;// 标的名称

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 号牌种类

	@XmlElement(name = "OBJ_TYPE", required = true)
	private String objType;// 标的损失类型

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

}
