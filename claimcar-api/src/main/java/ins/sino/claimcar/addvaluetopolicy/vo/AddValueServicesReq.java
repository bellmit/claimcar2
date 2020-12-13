package ins.sino.claimcar.addvaluetopolicy.vo;


import java.io.Serializable;

/**
 * 车理赔对接车承保增值条款理赔次数查询    请求报文
 * @author Administrator
 *
 */
public class AddValueServicesReq implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policyNo;

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
}
