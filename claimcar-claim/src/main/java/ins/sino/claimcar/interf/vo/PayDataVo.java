/******************************************************************************
 * CREATETIME : 2016年8月10日 下午5:23:58
 ******************************************************************************/
package ins.sino.claimcar.interf.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * @author ★XMSH
 */
@XStreamAlias("PayData")
public class PayDataVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("PayeeId")
	private String payeeId;
	@XStreamAlias("AccountNo")
	private String accountNo;// 字符 收款人账户号
	@XStreamAlias("PayRefReason")
	private String payRefReason;// 字符 收付原因
	@XStreamAlias("SerialNo")
	private String serialNo;// 字符 序列号
	@XStreamAlias("PayTime")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
	private Date payTime;// 时间 支付时间

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPayRefReason() {
		return payRefReason;
	}

	public void setPayRefReason(String payRefReason) {
		this.payRefReason = payRefReason;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

}
