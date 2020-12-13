package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("PROPINFO")
public class PropInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PROPID")
	private String propId; //损失方
	
	@XStreamAlias("PROPSERIALNO")
	private String propSerialNo; //损失方
	
	@XStreamAlias("LOSSTYPE")
	private String lossType; //损失方
	
	@XStreamAlias("LOSSNAME")
	private String lossName; //损失名称
	
	@XStreamAlias("LOSSNUM")
	private String lossNum; //损失数量
	
	@XStreamAlias("LOSSPRICE")
	private String lossPrice; //损失单价
	
	@XStreamAlias("SALVAGEAMOUNT")
	private String salvAgeamount; //残值金额
	
	@XStreamAlias("PAYAMOUNT")
	private String payAmount; //赔偿金额

	@XStreamAlias("LOSSFEETYPE")
    private String LossFeeType; //损失单位
	
	public String getPropId() {
		return propId;
	}

	public void setPropId(String propId) {
		this.propId = propId;
	}

	public String getPropSerialNo() {
		return propSerialNo;
	}

	public void setPropSerialNo(String propSerialNo) {
		this.propSerialNo = propSerialNo;
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

	public String getLossPrice() {
		return lossPrice;
	}

	public void setLossPrice(String lossPrice) {
		this.lossPrice = lossPrice;
	}

	public String getSalvAgeamount() {
		return salvAgeamount;
	}

	public void setSalvAgeamount(String salvAgeamount) {
		this.salvAgeamount = salvAgeamount;
	}

	public String getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
    
    public String getLossFeeType() {
        return LossFeeType;
    }
    
    public void setLossFeeType(String lossFeeType) {
        LossFeeType = lossFeeType;
    }
	
}
