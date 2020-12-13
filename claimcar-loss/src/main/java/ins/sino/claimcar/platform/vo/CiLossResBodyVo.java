package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiLossResBodyVo {

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiLossResLockedDataVo> lockedDataVos;

	@XmlElement(name = "RISK_INFO")
	private CiLossResRiskInfoVo riskInfoVo;

	public List<CiLossResLockedDataVo> getLockedDataVos() {
		return lockedDataVos;
	}

	public void setLockedDataVos(List<CiLossResLockedDataVo> lockedDataVos) {
		this.lockedDataVos = lockedDataVos;
	}

	public CiLossResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(CiLossResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
