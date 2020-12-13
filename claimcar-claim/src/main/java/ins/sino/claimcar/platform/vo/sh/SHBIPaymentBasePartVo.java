/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:51:15
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIPaymentBasePartVo {

	@XmlElement(name = "CLAIM_CODE")
	private String claimSeqNo;//
	
	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 赔偿总金额

	@XmlElement(name = "PAY_METHOD", required = true)
	private String payMethod;// 支付方式

	@XmlElement(name = "BANK_NAME")
	private String bankName;// 赔款支付开户行

	@XmlElement(name = "BANK_ACCOUNT")
	private String bankAccount;// 赔款支付账户号

	@XmlElement(name = "BANK_ACCOUNT_NAME")
	private String bankAccountName;// 赔款支付账户名

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "PAY_TIME")
	private Date payTime;// 款项划付时间

	@XmlElement(name = "COMPE_NO", required = true)
	private String compensateNo;// 计算书号
	
	@XmlElement(name = "Version")
	private String version; // 标准地址库版本号
	
	@XmlElement(name = "Coordinate")
	private String coordinate; // 坐标数据
	
	@XmlElement(name = "CoordinateSystem")
	private String coordinateSystem; // 坐标系代码

	/**
	 * @return 返回标准地址库版本号
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version 标准地址库版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return 返回坐标数据
	 */
	public String getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate 坐标系数据
	 */
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * @return 返回坐标系代码
	 */
	public String getCoordinateSystem() {
		return coordinateSystem;
	}

	/**
	 * @param coordinateSystem	坐标系代码
	 */
	public void setCoordinateSystem(String coordinateSystem) {
		this.coordinateSystem = coordinateSystem;
	}

	/**
	 * @return 返回 claimAmount 赔偿总金额
	 */
	public Double getClaimAmount() {
		return claimAmount;
	}

	/**
	 * @param claimAmount
	 *            要设置的 赔偿总金额
	 */
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	/**
	 * @return 返回 payMethod 支付方式
	 */
	public String getPayMethod() {
		return payMethod;
	}

	/**
	 * @param payMethod
	 *            要设置的 支付方式
	 */
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	/**
	 * @return 返回 bankName 赔款支付开户行
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            要设置的 赔款支付开户行
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return 返回 bankAccount 赔款支付账户号
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * @param bankAccount
	 *            要设置的 赔款支付账户号
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	/**
	 * @return 返回 bankAccountName 赔款支付账户名
	 */
	public String getBankAccountName() {
		return bankAccountName;
	}

	/**
	 * @param bankAccountName
	 *            要设置的 赔款支付账户名
	 */
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	/**
	 * @return 返回 payTime 款项划付时间
	 */
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime
	 *            要设置的 款项划付时间
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getCompensateNo() {
		return compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	public String getClaimSeqNo() {
		return claimSeqNo;
	}

	public void setClaimSeqNo(String claimSeqNo) {
		this.claimSeqNo = claimSeqNo;
	}

}
