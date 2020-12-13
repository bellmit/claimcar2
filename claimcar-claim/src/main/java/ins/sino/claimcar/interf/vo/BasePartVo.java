/******************************************************************************
 * CREATETIME : 2016年8月10日 下午5:19:21
 ******************************************************************************/
package ins.sino.claimcar.interf.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author ★XMSH
 */
@XStreamAlias("BasePart")
public class BasePartVo implements Serializable {

	private static final long serialVersionUID = 1L;
	@XStreamAlias("CertiType")
	private String certiType;// 字符 业务类型（Y预赔、C实赔、Z追偿）
	@XStreamAlias("CertiNo")
	private String certiNo;// 字符 业务号（计算书号、预赔款）
	@XStreamAlias("PolicyNo")
	private String policyNo;// 字符 保单号
	@XStreamAlias("RegistNo")
	private String registNo;// 字符 报案号
	@XStreamAlias("ClaimNo")
	private String claimNo;// 字符 立案号
	
	@XStreamAlias("RiskCode")
	private String riskCode;// 字符  险种
	@XStreamAlias("ComCode")
	private String comCode;// 字符 机构
	@XStreamAlias("UserCode")
	private String userCode;// 字符 用户

	@XStreamImplicit
	private List<PayDataVo> PayDataVo;// 支付明细信息

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getCertiNo() {
		return certiNo;
	}

	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public List<PayDataVo> getPayDataVo() {
		return PayDataVo;
	}

	public void setPayDataVo(List<PayDataVo> payDataVo) {
		PayDataVo = payDataVo;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	

}
