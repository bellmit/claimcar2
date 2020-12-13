/******************************************************************************
 * CREATETIME : 2016年9月6日 下午7:04:13
 ******************************************************************************/
package ins.sino.claimcar.regist.vo;



/**
 * <pre>批单信息vo</pre>
 * @author ★Luwei
 */
public class PolicyEndorseInfoVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private int serialNo;// 序号

	private String endorseNo;// 批单号码

	private String policyNo;// 保单号

	private String endorseTimes;// 保单批改次数
	
	private String   endorDate;//批单日期
	
	private String chgAmount;//保额变化量
	
	private String chgPremium;//保费变化量
	
	public String getEndorDate() {
		return endorDate;
	}

	public void setEndorDate(String endorDate) {
		this.endorDate = endorDate;
	}

	public String getChgAmount() {
		return chgAmount;
	}

	public void setChgAmount(String chgAmount) {
		this.chgAmount = chgAmount;
	}

	public String getChgPremium() {
		return chgPremium;
	}

	public void setChgPremium(String chgPremium) {
		this.chgPremium = chgPremium;
	}

	
	 
	 

	/**
	 * @return 返回 serialNo。
	 */
	public int getSerialNo() {
		return serialNo;
	}

	/**
	 * @param serialNo 要设置的 serialNo。
	 */
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	/**
	 * @return 返回 endorseNo。
	 */
	public String getEndorseNo() {
		return endorseNo;
	}

	/**
	 * @param endorseNo 要设置的 endorseNo。
	 */
	public void setEndorseNo(String endorseNo) {
		this.endorseNo = endorseNo;
	}

	/**
	 * @return 返回 policyNo。
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 policyNo。
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 endorseTimes。
	 */
	public String getEndorseTimes() {
		return endorseTimes;
	}

	/**
	 * @param endorseTimes 要设置的 endorseTimes。
	 */
	public void setEndorseTimes(String endorseTimes) {
		this.endorseTimes = endorseTimes;
	}

}
