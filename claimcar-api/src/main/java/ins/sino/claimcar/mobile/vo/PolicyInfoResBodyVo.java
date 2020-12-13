package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class PolicyInfoResBodyVo  implements Serializable{
	
	/**
	 * 报案号
	 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	/**
	 * 数据部分
	 */
	@XStreamAlias("POLICYLIST")
	private List<PolicyInfoListVo> policyList;
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public List<PolicyInfoListVo> getPolicyList() {
		return policyList;
	}
	public void setPolicyList(List<PolicyInfoListVo> policyList) {
		this.policyList = policyList;
	}
	
	
}
