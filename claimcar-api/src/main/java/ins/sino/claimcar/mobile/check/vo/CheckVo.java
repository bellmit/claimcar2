package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECK")
public class CheckVo extends MobileCheckBody implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CARID")
	private String carId; //理赔车辆ID
	
	@XStreamAlias("LOSSID")
	private String lossId; //理赔车辆查勘ID
	
    @XStreamAlias("CARSERIALNO")
    private String carSerialNo; //移动端车辆序号
	
	@XStreamAlias("LOSSSERIALNO")
	private String lossSerialNo; //移动终端车辆查勘序号
	
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

    public String getCarSerialNo() {
        return carSerialNo;
    }

    
    public void setCarSerialNo(String carSerialNo) {
        this.carSerialNo = carSerialNo;
    }

    public String getLossSerialNo() {
		return lossSerialNo;
	}

	public void setLossSerialNo(String lossSerialNo) {
		this.lossSerialNo = lossSerialNo;
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
	
	
}
