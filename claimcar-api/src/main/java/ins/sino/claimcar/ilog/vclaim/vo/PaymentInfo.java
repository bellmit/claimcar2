package ins.sino.claimcar.ilog.vclaim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PaymentInfo")
public class PaymentInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("payobjectkind")
	private String payobjectkind = ""; // 收款人类型

	public String getPayobjectkind() {
		return payobjectkind;
	}

	public void setPayobjectkind(String payobjectkind) {
		this.payobjectkind = payobjectkind;
	}
	
	
	
}
