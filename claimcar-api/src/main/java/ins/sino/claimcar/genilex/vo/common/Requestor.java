package ins.sino.claimcar.genilex.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Requestor")
public class Requestor  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Reference") 
	private String  reference;

	@XStreamAlias("Timestamp") 
	private String  timestamp;
	
	@XStreamAlias("LineOfBusiness") 
	private String  lineOfBusiness;

	@XStreamAlias("PointOfRequest") 
	private String  pointOfRequest;

	@XStreamAlias("TransactionType") 
	private String  transactionType;

	@XStreamAlias("EchoEntities") 
	private String  echoEntities;

	@XStreamAlias("EchoProductRequests") 
	private String  echoProductRequests;
	
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
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
