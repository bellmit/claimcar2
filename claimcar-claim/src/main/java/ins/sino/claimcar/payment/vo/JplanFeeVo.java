package ins.sino.claimcar.payment.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;
import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 收付主数据
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016年7月6日
 * @since (2016年7月6日 下午8:07:40): <br>
 */
@XStreamAlias("JplanFee")
public class JplanFeeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("SerialNo")
	private int serialNo;// 序号
	@XStreamAlias("PayRefReason")
	private String payRefReason;// 字符 收付原因 代码
	@XStreamAlias("Currency")
	private String currency;// 字符 币别
	@XStreamAlias("PlanFee")
	@XStreamConverter(value = SinosoftDoubleConverter.class, strings = {"0.00"})
	private Double planFee;// 货币 支付金额
	@XStreamAlias("UnderWriteDate")
	@XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyyMMdd"})
	private Date underWriteDate;// 时间 核赔通过时间,结案时间,当时时间
	@XStreamAlias("AppliCode")
	private String appliCode;// 字符 支付对象代码
	@XStreamAlias("AppliName")
	private String appliName;// 字符 支付对象名称
	@XStreamAlias("VoucherNo2")
	private String voucherNo2;// 字符 平台结算码 可以空
	@XStreamImplicit
	private List<JplanFeeDetailVo> jplanFeeDetailVos;// 收付明细

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getPayRefReason() {
		return payRefReason;
	}

	public void setPayRefReason(String payRefReason) {
		this.payRefReason = payRefReason;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getPlanFee() {
		return planFee;
	}

	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}

	public Date getUnderWriteDate() {
		return underWriteDate;
	}

	public void setUnderWriteDate(Date underWriteDate) {
		this.underWriteDate = underWriteDate;
	}

	public String getAppliCode() {
		return appliCode;
	}

	public void setAppliCode(String appliCode) {
		this.appliCode = appliCode;
	}

	public String getAppliName() {
		return appliName;
	}

	public void setAppliName(String appliName) {
		this.appliName = appliName;
	}

	public String getVoucherNo2() {
		return voucherNo2;
	}

	public void setVoucherNo2(String voucherNo2) {
		this.voucherNo2 = voucherNo2;
	}

	public List<JplanFeeDetailVo> getJplanFeeDetailVos() {
		return jplanFeeDetailVos;
	}

	public void setJplanFeeDetailVos(List<JplanFeeDetailVo> jplanFeeDetailVos) {
		this.jplanFeeDetailVos = jplanFeeDetailVos;
	}

}
