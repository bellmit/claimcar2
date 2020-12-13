package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class CarLossDetailItemVo implements Serializable {
	private static final long serialVersionUID = -8151531823769758731L;
	@XStreamAlias("ThirdCarLossSerialNo")
	private String ThirdCarLossSerialNo;
	@XStreamAlias("ThirdCarLossItemNo")
	private String thirdCarLossItemNo;
	@XStreamAlias("ThirdCarLossLicenseNo")
	private String thirdCarLossLicenseNo;
	@XStreamAlias("KindCode")
	private String kindCode;
	@XStreamAlias("KindName")
	private String kindName;
	@XStreamAlias("LossPartCode")
	private String lossPartCode;
	@XStreamAlias("PartCode")
	private String partCode;
	@XStreamAlias("PartName")
	private String partName;
	@XStreamAlias("LossPartCodeDesc")
	private String lossPartCodeDesc;
	public String getThirdCarLossSerialNo() {
		return ThirdCarLossSerialNo;
	}

	public void setThirdCarLossSerialNo(String thirdCarLossSerialNo) {
		ThirdCarLossSerialNo = thirdCarLossSerialNo;
	}

	public String getThirdCarLossItemNo() {
		return thirdCarLossItemNo;
	}

	public void setThirdCarLossItemNo(String thirdCarLossItemNo) {
		this.thirdCarLossItemNo = thirdCarLossItemNo;
	}

	public String getThirdCarLossLicenseNo() {
		return thirdCarLossLicenseNo;
	}

	public void setThirdCarLossLicenseNo(String thirdCarLossLicenseNo) {
		this.thirdCarLossLicenseNo = thirdCarLossLicenseNo;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getLossPartCode() {
		return lossPartCode;
	}

	public void setLossPartCode(String lossPartCode) {
		this.lossPartCode = lossPartCode;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getLossPartCodeDesc() {
		return lossPartCodeDesc;
	}

	public void setLossPartCodeDesc(String lossPartCodeDesc) {
		this.lossPartCodeDesc = lossPartCodeDesc;
	}

}
