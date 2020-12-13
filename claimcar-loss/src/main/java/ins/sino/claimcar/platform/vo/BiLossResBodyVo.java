package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiLossResBodyVo {

	@XmlElement(name = "LockedData")
	private List<BiLossResLockedDataVo> lockedDataVos;

	@XmlElement(name = "RiskInfo")
	private BiLossResRiskInfoVo riskInfoVo;

	public List<BiLossResLockedDataVo> getLockedDataVos() {
		return lockedDataVos;
	}

	public void setLockedDataVos(List<BiLossResLockedDataVo> lockedDataVos) {
		this.lockedDataVos = lockedDataVos;
	}

	public BiLossResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(BiLossResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
