/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//三者车辆情况列表(隶属于报案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiReportThirdVehicleDataVo {
	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//三者车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//三者车辆号牌种类代码；参见代码

	@XmlElement(name = "DriverName")
	private String driverName;//三者车辆驾驶员姓名


	/** 
	 * @return 返回 licensePlateNo  三者车辆号牌号码
	 */ 
	public String getLicensePlateNo(){ 
	    return licensePlateNo;
	}

	/** 
	 * @param licensePlateNo 要设置的 三者车辆号牌号码
	 */ 
	public void setLicensePlateNo(String licensePlateNo){ 
	    this.licensePlateNo=licensePlateNo;
	}

	/** 
	 * @return 返回 licensePlateType  三者车辆号牌种类代码；参见代码
	 */ 
	public String getLicensePlateType(){ 
	    return licensePlateType;
	}

	/** 
	 * @param licensePlateType 要设置的 三者车辆号牌种类代码；参见代码
	 */ 
	public void setLicensePlateType(String licensePlateType){ 
	    this.licensePlateType=licensePlateType;
	}

	/** 
	 * @return 返回 driverName  三者车辆驾驶员姓名
	 */ 
	public String getDriverName(){ 
	    return driverName;
	}

	/** 
	 * @param driverName 要设置的 三者车辆驾驶员姓名
	 */ 
	public void setDriverName(String driverName){ 
	    this.driverName=driverName;
	}



}
