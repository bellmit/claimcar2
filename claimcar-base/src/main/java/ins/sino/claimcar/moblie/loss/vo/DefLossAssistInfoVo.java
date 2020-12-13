package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ASSISTINFO")
public class DefLossAssistInfoVo implements Serializable{
	private static final long serialVersionUID = 8423652723600188374L;	
	@XStreamAlias("ASSISTID")
	private String assistId;//辅料明细主键
	@XStreamAlias("ITEMNAME")
	private String itemName;//辅料名称
	@XStreamAlias("COUNT")
	private String count;//数量
	@XStreamAlias("MATERIALFEE")
	private String materialFee;//外修项目定损金额
	@XStreamAlias("EVALMATESUM")
	private String evalMateSum;//辅料合计
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;//自定义辅料标记 是/否 1/0
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;//险种代码
	@XStreamAlias("REMARK")
	private String remark;//备注
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
	public String getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(String materialFee) {
		this.materialFee = materialFee;
	}
	public String getEvalMateSum() {
		return evalMateSum;
	}
	public void setEvalMateSum(String evalMateSum) {
		this.evalMateSum = evalMateSum;
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
}
