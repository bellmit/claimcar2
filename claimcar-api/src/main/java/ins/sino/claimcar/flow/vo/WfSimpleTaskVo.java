/******************************************************************************
* CREATETIME : 2016年1月30日 下午6:33:55
******************************************************************************/
package ins.sino.claimcar.flow.vo;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月30日
 */
public class WfSimpleTaskVo implements java.io.Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	private String registNo;
	private String handlerIdKey;
	private String itemName;
	private String bussTag;
	private String showInfoXml;
	private String riskCode;
	private String claimNo;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getHandlerIdKey() {
		return handlerIdKey;
	}

	public void setHandlerIdKey(String handlerIdKey) {
		this.handlerIdKey = handlerIdKey;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	
	public String getBussTag() {
		return bussTag;
	}

	
	public void setBussTag(String bussTag) {
		this.bussTag = bussTag;
	}

	
	public String getShowInfoXml() {
		return showInfoXml;
	}

	
	public void setShowInfoXml(String showInfoXml) {
		this.showInfoXml = showInfoXml;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	

}
