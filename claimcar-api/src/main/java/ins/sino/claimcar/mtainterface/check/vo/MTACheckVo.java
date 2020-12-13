package ins.sino.claimcar.mtainterface.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("CHECK")
public class MTACheckVo  implements Serializable{

private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CARID")
	private String carId; //理赔车辆ID
	
	@XStreamAlias("LOSSID")
	private String lossId; //理赔车辆查勘ID
	
	@XStreamAlias("ISLOSS")
	private String isLoss; //有/无损失
	
	@XStreamAlias("ESTIMATEDLOSS")
	private String estimatedLoss; //估损金额
	
	@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty; //责任比例
	
    @XStreamAlias("INDEMNITYDUTYRATE")
    private String indemnityDutyRate; //事故比例
    
	@XStreamAlias("KINDCODE")
	private String kindCode; //险别
	
	@XStreamAlias("LOSSPART")
	private String lossPart; //损失部位
	
	@XStreamAlias("RESCUEFEE")
	private String rescueFee; //施救费

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getLossId() {
		return lossId;
	}

	public void setLossId(String lossId) {
		this.lossId = lossId;
	}

	public String getIsLoss() {
		return isLoss;
	}

	public void setIsLoss(String isLoss) {
		this.isLoss = isLoss;
	}

	public String getEstimatedLoss() {
		return estimatedLoss;
	}

	public void setEstimatedLoss(String estimatedLoss) {
		this.estimatedLoss = estimatedLoss;
	}

	public String getIndemnityDuty() {
		return indemnityDuty;
	}

	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}

	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}

	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getLossPart() {
		return lossPart;
	}

	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

	public String getRescueFee() {
		return rescueFee;
	}

	public void setRescueFee(String rescueFee) {
		this.rescueFee = rescueFee;
	}
	
}
