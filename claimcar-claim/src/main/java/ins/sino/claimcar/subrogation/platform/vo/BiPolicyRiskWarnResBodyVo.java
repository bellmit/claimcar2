package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回风险预警承保信息查询 Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiPolicyRiskWarnResBodyVo {

	@XmlElement(name = "PolicyData")
	private List<BiPolicyRiskWarnDataVo> policyRiskWarnDataList;
	
	@XmlElement(name = "KsErrorPro")
	private List<BiKsErrorProVo> ksErrorPros;

	
	public List<BiPolicyRiskWarnDataVo> getPolicyRiskWarnDataList() {
		return policyRiskWarnDataList;
	}
	
	public void setPolicyRiskWarnDataList(List<BiPolicyRiskWarnDataVo> policyRiskWarnDataList) {
		this.policyRiskWarnDataList = policyRiskWarnDataList;
	}
	
	public List<BiKsErrorProVo> getKsErrorPros() {
		return ksErrorPros;
	}

	
	public void setKsErrorPros(List<BiKsErrorProVo> ksErrorPros) {
		this.ksErrorPros = ksErrorPros;
	}
	
}
