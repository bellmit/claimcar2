package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FittingData")
public class FittingDataVo implements Serializable{
	
private static final long serialVersionUID = 1L;
/**
 * 更换或修理标志
 */
@XStreamAlias("ChangeOrRepair")
private String changeOrRepair;
/**
 * 更换/修理配件名称
 */
@XStreamAlias("FittingName")
private String fittingName;
/**
 * 更换/修理配件件数
 */
@XStreamAlias("FittingNum")
private String fittingNum;

/**
 * 更换/修理配件材料费（单价）
 */
@XStreamAlias("MaterialFee")
private String materialFee;

/**
 *  更换/修理配件工时
 */
@XStreamAlias("ManHour")
private String manHour;

/**
 *  更换/修理配件人工费
 */
@XStreamAlias("ManPowerFee")
private String manPowerFee;

/**
 *  是否增补
 */
@XStreamAlias("IsSubjoin")
private String isSubjoin;

public String getChangeOrRepair() {
	return changeOrRepair;
}

public void setChangeOrRepair(String changeOrRepair) {
	this.changeOrRepair = changeOrRepair;
}

public String getFittingName() {
	return fittingName;
}

public void setFittingName(String fittingName) {
	this.fittingName = fittingName;
}

public String getFittingNum() {
	return fittingNum;
}

public void setFittingNum(String fittingNum) {
	this.fittingNum = fittingNum;
}

public String getMaterialFee() {
	return materialFee;
}

public void setMaterialFee(String materialFee) {
	this.materialFee = materialFee;
}

public String getManHour() {
	return manHour;
}

public void setManHour(String manHour) {
	this.manHour = manHour;
}

public String getManPowerFee() {
	return manPowerFee;
}

public void setManPowerFee(String manPowerFee) {
	this.manPowerFee = manPowerFee;
}

public String getIsSubjoin() {
	return isSubjoin;
}

public void setIsSubjoin(String isSubjoin) {
	this.isSubjoin = isSubjoin;
}





}
