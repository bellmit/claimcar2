package ins.sino.claimcar.selfHelpClaimCar.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASEINFO")
public class SelfClaimCaseCarInfoResVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; 
	@XStreamAlias("ORDERNO")
	private String orderNo;//订单号

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
