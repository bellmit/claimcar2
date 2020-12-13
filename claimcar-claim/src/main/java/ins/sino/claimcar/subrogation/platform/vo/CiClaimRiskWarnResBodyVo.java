package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回风险预警理赔信息查询  交强 请求Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiClaimRiskWarnResBodyVo {

	@XmlElementWrapper(name = "REPORT_LIST")
	@XmlElement(name = "REPORT_DATA")
	private List<CiClaimRiskWarnDataVo> claimRiskWarnDataList;
	
	@XmlElementWrapper(name = "KSERRORPRO_LIST")
	@XmlElement(name = "KSERRORPRO_DATA")
	private List<CiKsErrorProVo> ksErrorPros;

	public List<CiClaimRiskWarnDataVo> getClaimRiskWarnDataList() {
		return claimRiskWarnDataList;
	}

	public void setClaimRiskWarnDataList(
			List<CiClaimRiskWarnDataVo> claimRiskWarnDataList) {
		this.claimRiskWarnDataList = claimRiskWarnDataList;
	}

	public List<CiKsErrorProVo> getKsErrorPros() {
		return ksErrorPros;
	}

	public void setKsErrorPros(List<CiKsErrorProVo> ksErrorPros) {
		this.ksErrorPros = ksErrorPros;
	}
	

	
	
	
}
