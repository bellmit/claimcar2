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
public class BiClaimRiskWarnResBodyVo {

	@XmlElement(name = "NotificationData")
	private List<BiClaimRiskWarnDataVo> biClaimRiskWarnDataList;
	
	@XmlElement(name = "KsErrorPro")
	private List<BiKsErrorProVo> ksErrorPros;

	
	
	
	public List<BiClaimRiskWarnDataVo> getBiClaimRiskWarnDataList() {
		return biClaimRiskWarnDataList;
	}


	public void setBiClaimRiskWarnDataList(
			List<BiClaimRiskWarnDataVo> biClaimRiskWarnDataList) {
		this.biClaimRiskWarnDataList = biClaimRiskWarnDataList;
	}


	public List<BiKsErrorProVo> getKsErrorPros() {
		return ksErrorPros;
	}

	
	public void setKsErrorPros(List<BiKsErrorProVo> ksErrorPros) {
		this.ksErrorPros = ksErrorPros;
	}
	
}
