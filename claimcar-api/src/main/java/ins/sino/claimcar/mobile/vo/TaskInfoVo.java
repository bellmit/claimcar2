package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("TASKINFO")
public class TaskInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CLAIMTYPE")
	private String claimType; //理赔节点
	
	@XStreamAlias("IFOBJECT")
	private String ifObject; //类别
	
	@XStreamAlias("LOSSNAME")
	private String lossName; //标的名称
	
	@XStreamAlias("HANDLERCODE")
	private String handlerCode; //处理人员代码
	
	@XStreamAlias("HANDLERNAME")
	private String handlerName; //处理人员名称
	
	@XStreamAlias("CREATEDATE")
	private String createDate; //受理时间
	
	@XStreamAlias("FINSHDATE")
	private String finshDate; //完成时间
	
	@XStreamAlias("LOSSAMOUNT")
	private String lossAmount; //金额

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getIfObject() {
		return ifObject;
	}

	public void setIfObject(String ifObject) {
		this.ifObject = ifObject;
	}

	public String getLossName() {
		return lossName;
	}

	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getFinshDate() {
		return finshDate;
	}

	public void setFinshDate(String finshDate) {
		this.finshDate = finshDate;
	}


	public String getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(String lossAmount) {
		this.lossAmount = lossAmount;
	}
	
	
	
}
