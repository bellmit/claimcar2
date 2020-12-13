package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ASSISTINFO")
public class DefLossAssistInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("ASSISTID")
	private String assistId;  //辅料明细主键
	@XStreamAlias("ITEMNAME")
	private String itemName;  //辅料名称
	@XStreamAlias("COUNT")
	private String count;  //数量
	@XStreamAlias("MATERIALFEE")
	private BigDecimal materialFee;  //定损材料费
	@XStreamAlias("EVALMATESUM")
	private BigDecimal evalmateSum;  //辅料合计
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;  //自定义辅料标记
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;  //险种代码
	@XStreamAlias("REMARK")
	private String remark;  //备注
	@XStreamAlias("LOSSFEE2")
	private BigDecimal lossFee2;//核损辅料合计
	public String getAssistId() {
		return assistId;
	}
	public void setAssistId(String assistId) {
		this.assistId = assistId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public BigDecimal getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(BigDecimal materialFee) {
		this.materialFee = materialFee;
	}
	public BigDecimal getEvalmateSum() {
		return evalmateSum;
	}
	public void setEvalmateSum(BigDecimal evalmateSum) {
		this.evalmateSum = evalmateSum;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getItemCoverCode() {
		return itemCoverCode;
	}
	public void setItemCoverCode(String itemCoverCode) {
		this.itemCoverCode = itemCoverCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(BigDecimal lossFee2) {
		this.lossFee2 = lossFee2;
	}
	
	
}
