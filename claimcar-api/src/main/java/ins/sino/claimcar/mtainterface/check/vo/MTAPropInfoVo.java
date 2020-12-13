package ins.sino.claimcar.mtainterface.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PROPINFO")
public class MTAPropInfoVo  implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PROPID")
	private String propId; //损失方
	
	@XStreamAlias("LOSSTYPE")
	private String lossType; //损失方
	
	@XStreamAlias("LOSSNAME")
	private String lossName; //损失名称
	
	@XStreamAlias("LOSSNUM")
	private String lossNum; //损失数量
	
	@XStreamAlias("UNIT")
	private String unit; //损失单位
	
	@XStreamAlias("ISNOCLAIM")
	private String isNoClaim; //是否赔付
	
	@XStreamAlias("PAYAMOUNT")
	private String payAmount; //赔偿金额
	
	@XStreamAlias("LOSSDEGREECODE")
	private String lossDegreeCode; //损失程度

	public String getPropId() {
		return propId;
	}

	public void setPropId(String propId) {
		this.propId = propId;
	}

	public String getLossType() {
		return lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public String getLossName() {
		return lossName;
	}

	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	public String getLossNum() {
		return lossNum;
	}

	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getIsNoClaim() {
		return isNoClaim;
	}

	public void setIsNoClaim(String isNoClaim) {
		this.isNoClaim = isNoClaim;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getLossDegreeCode() {
		return lossDegreeCode;
	}

	public void setLossDegreeCode(String lossDegreeCode) {
		this.lossDegreeCode = lossDegreeCode;
	}
	
}
