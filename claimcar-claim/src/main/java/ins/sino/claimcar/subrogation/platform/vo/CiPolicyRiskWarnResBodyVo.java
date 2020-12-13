package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回风险预警承保信息查询 Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiPolicyRiskWarnResBodyVo {

	@XmlElementWrapper(name = "POLICY_LIST")
	@XmlElement(name = "POLICY_DATA")
	private List<CiPolicyRiskWarnDataVo> policyRiskWarnDataList;
	
	@XmlElementWrapper(name = "KSERRORPRO_LIST")
	@XmlElement(name = "KSERRORPRO_DATA")
	private List<CiKsErrorProVo> ksErrorPros;

	
	public List<CiPolicyRiskWarnDataVo> getPolicyRiskWarnDataList() {
		return policyRiskWarnDataList;
	}

	
	public void setPolicyRiskWarnDataList(List<CiPolicyRiskWarnDataVo> policyRiskWarnDataList) {
		this.policyRiskWarnDataList = policyRiskWarnDataList;
	}

	
	public List<CiKsErrorProVo> getKsErrorPros() {
		return ksErrorPros;
	}

	
	public void setKsErrorPros(List<CiKsErrorProVo> ksErrorPros) {
		this.ksErrorPros = ksErrorPros;
	}
	
	
	
}
