package ins.sino.claimcar.common.losscharge.vo;

/**
 * Custom VO class of PO PrpLDlossCharge
 */ 
public class PrpLDlossChargeVo extends PrpLDlossChargeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String isVerify;//是否核损通过
    private String license;//车牌号码
    private int serialNo;//车辆标志
	/**
	 * @return the serialNo
	 */
	public int getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo the serialNo to set
	 */
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return the license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * @param license the license to set
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * @return the isVerify
	 */
	public String getIsVerify() {
		return isVerify;
	}

	/**
	 * @param isVerify the isVerify to set
	 */
	public void setIsVerify(String isVerify) {
		this.isVerify = isVerify;
	}
}
