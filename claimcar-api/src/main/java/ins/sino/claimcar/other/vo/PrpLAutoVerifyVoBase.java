package ins.sino.claimcar.other.vo;

import java.math.BigDecimal;

public class PrpLAutoVerifyVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String userCode;
	private String comCode;
	private String validFlag;
	private BigDecimal money;
	private String node;
	
	protected PrpLAutoVerifyVoBase(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
	
	
}
