package ins.sino.claimcar.payment.vo;

import ins.platform.utils.xstreamconverters.SinosoftDoubleConverter;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * 收付明细VO
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2016年7月6日
 * @since (2016年7月6日 下午8:04:12): <br>
 */
@XStreamAlias("JplanFeeDetail")
public class JplanFeeDetailVo implements Serializable {

	private static final long serialVersionUID = -5505245044836566089L;
	@XStreamAlias("KindCode")
	private String kindCode;// 字符 险别代码
	@XStreamAlias("Planfee")
	@XStreamConverter(value = SinosoftDoubleConverter.class,strings = {"0.00"})
	private Double planfee;// 货币 支付金额

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public Double getPlanfee() {
		return planfee;
	}

	public void setPlanfee(Double planfee) {
		this.planfee = planfee;
	}

}
