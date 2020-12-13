package ins.sino.claimcar.mobile.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("KINDINFO")
public class KindInfoListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 子险名称
	 */
	@XStreamAlias("KINDNAME")
	private String kindName;
	
	/*
	 * 子险代码
	 */
	@XStreamAlias("KINDCODE")
	private String kindCode;
	
	/*
	 * 保额最新值
	 */
	@XStreamAlias("AMOUNT")
	private String amount;
	
	/*
	 * 免赔额
	 */
	@XStreamAlias("DEDUCT")
	private String deduct;
	
	/*
	 * 是否承保不计免赔
	 */
	@XStreamAlias("ISDEDUCT")
	private String isDeduct;

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDeduct() {
		return deduct;
	}

	public void setDeduct(String deduct) {
		this.deduct = deduct;
	}

	public String getIsDeduct() {
		return isDeduct;
	}

	public void setIsDeduct(String isDeduct) {
		this.isDeduct = isDeduct;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	
	
}
