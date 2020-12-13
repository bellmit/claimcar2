package ins.sino.claimcar.mobilecheck.service.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 案件涉损信息查询
 * 
 * <pre></pre>
 * 
 * @author ★niuqiang
 */

@XStreamAlias("CHECKLOSSINFO")
public class MC_CheckLossInfo implements Serializable {

	/**  */
	private static final long serialVersionUID = -1735274671756870787L;

	@XStreamAlias("LOSSTYPE")
	private String lossType; // 损失类型

	@XStreamAlias("LICENSENO")
	private String licenseNO; // 车牌号

	@XStreamAlias("HANDLECODE")
	private String handlerCode; // 处理人

	@XStreamAlias("CERAMOUNT")
	private String cerAmount; // 定损金额

	@XStreamAlias("PRIEAMOUNT")
	private String prieAmount; // 核价金额

	@XStreamAlias("VERIAMOUNT")
	private String veriAmount; // 核损金额

	public String getLossType() {
		return lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public String getLicenseNO() {
		return licenseNO;
	}

	public void setLicenseNO(String licenseNO) {
		this.licenseNO = licenseNO;
	}

	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getCerAmount() {
		return cerAmount;
	}

	public void setCerAmount(String cerAmount) {
		this.cerAmount = cerAmount;
	}

	public String getPrieAmount() {
		return prieAmount;
	}

	public void setPrieAmount(String prieAmount) {
		this.prieAmount = prieAmount;
	}

	public String getVeriAmount() {
		return veriAmount;
	}

	public void setVeriAmount(String veriAmount) {
		this.veriAmount = veriAmount;
	}

}
