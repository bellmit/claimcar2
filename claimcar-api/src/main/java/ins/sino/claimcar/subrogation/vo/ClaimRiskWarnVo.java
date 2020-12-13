/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;
import java.util.Date;



/**
 * 风险预警理赔信息查询 页面返回VO
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
public class ClaimRiskWarnVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**保险公司 **/
	private String insurerCode;
	/**承保地区 **/
	private String insurerArea;
	
	/** 本车/三者车，车辆属性 **/ 
	private String vehicleProperty; 

	/** 报案时间 **/ 
	private Date notificationTime; 

	/** 出险时间 **/ 
	private Date lossTime; 

	/** 出险地点 **/ 
	private String lossArea; 

	/** 出险经过 **/ 
	private String lossDesc; 

	/** 案件状态代码 **/ 
	private String claimStatus; 

	/** 保单类型代码 **/ 
	private String riskType; 

	/** 号牌种类代码 **/ 
	private String licensePlateType; 

	/** 号牌号码 **/ 
	private String licensePlateNo;

	
	public String getInsurerCode() {
		return insurerCode;
	}

	
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	
	public String getInsurerArea() {
		return insurerArea;
	}

	
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}

	
	public String getVehicleProperty() {
		return vehicleProperty;
	}

	
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	
	public Date getNotificationTime() {
		return notificationTime;
	}

	
	public void setNotificationTime(Date notificationTime) {
		this.notificationTime = notificationTime;
	}

	
	public Date getLossTime() {
		return lossTime;
	}

	
	public void setLossTime(Date lossTime) {
		this.lossTime = lossTime;
	}

	
	public String getLossArea() {
		return lossArea;
	}

	
	public void setLossArea(String lossArea) {
		this.lossArea = lossArea;
	}

	
	public String getLossDesc() {
		return lossDesc;
	}

	
	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	
	public String getClaimStatus() {
		return claimStatus;
	}

	
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	
	public String getRiskType() {
		return riskType;
	}

	
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	
	public String getLicensePlateType() {
		return licensePlateType;
	}

	
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	} 
	
}
