package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import ins.sino.claimcar.genilex.vo.common.AccountInfo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Requestor")
public class RequestorVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  @XStreamAlias("Reference")	
	  private String reference;//交易唯一标识
	  @XStreamAlias("Timestamp")	
	  private String timestamp;//交易日期
	  @XStreamAlias("LineOfBusiness")	
	  private String lineOfBusiness;//产品线						
	  @XStreamAlias("PointOfRequest")	
	  private String pointOfRequest;//业务环节							 
	  @XStreamAlias("TransactionType")	
	  private String TransactionType;//交易类型					
	  @XStreamAlias("EchoEntities")	
	  private String echoEntities;//是否回传实体							
	  @XStreamAlias("EchoProductRequests")	
	  private String echoProductRequests;//是否回传产品请求
	  @XStreamAlias("AccountInfo") 
	  private AccountInfo  accountInfo;
	  
		public String getReference() {
			return reference;
		}
		public void setReference(String reference) {
			this.reference = reference;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getLineOfBusiness() {
			return lineOfBusiness;
		}
		public void setLineOfBusiness(String lineOfBusiness) {
			this.lineOfBusiness = lineOfBusiness;
		}
		public String getPointOfRequest() {
			return pointOfRequest;
		}
		public void setPointOfRequest(String pointOfRequest) {
			this.pointOfRequest = pointOfRequest;
		}
		public String getTransactionType() {
			return TransactionType;
		}
		public void setTransactionType(String transactionType) {
			TransactionType = transactionType;
		}
		public String getEchoEntities() {
			return echoEntities;
		}
		public void setEchoEntities(String echoEntities) {
			this.echoEntities = echoEntities;
		}
		public String getEchoProductRequests() {
			return echoProductRequests;
		}
		public void setEchoProductRequests(String echoProductRequests) {
			this.echoProductRequests = echoProductRequests;
		}
		public AccountInfo getAccountInfo() {
			return accountInfo;
		}
		public void setAccountInfo(AccountInfo accountInfo) {
			this.accountInfo = accountInfo;
		}
}
