package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 保单关联接口vo（理赔请求客服系统）
 * @author Luwei
 */
@XStreamAlias("OUTDATE")
public class PolicyRelationDateVo {

	/**
	 * 报案号
	 */
	@XStreamAlias("ClmNo")
	private String clmNo;

	/**
	 * 关联保单号
	 */
	@XStreamAlias("ConnectPolicyNo")
	private String connectPolicyNo;

	/**
	 * 取消/关联 标示,1=关联；0=取消
	 */
	@XStreamAlias("IsConnect")
	private String isConnect;

	
	
	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

	public String getConnectPolicyNo() {
		return connectPolicyNo;
	}

	public void setConnectPolicyNo(String connectPolicyNo) {
		this.connectPolicyNo = connectPolicyNo;
	}

	public String getIsConnect() {
		return isConnect;
	}

	public void setIsConnect(String isConnect) {
		this.isConnect = isConnect;
	}

}
