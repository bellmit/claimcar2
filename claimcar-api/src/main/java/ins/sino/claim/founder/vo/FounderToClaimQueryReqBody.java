package ins.sino.claim.founder.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class FounderToClaimQueryReqBody implements Serializable{

	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("RegistNo")
	private String registNo;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
}
