package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BIReOpenAppResBodyVo {
	
	/** 锁定信息列表 **/
	@XmlElement(name = "LockedData")
	private List<BIReOpenAppResLockedDataVo> lockedDataVo;

	/** 风险信息 **/
	@XmlElement(name = "RiskInfo")
	private BIReOpenAppResRiskInfoVo riskInfoVo;

	public List<BIReOpenAppResLockedDataVo> getLockedDataVo() {
		return lockedDataVo;
	}

	public void setLockedDataVo(List<BIReOpenAppResLockedDataVo> lockedDataVo) {
		this.lockedDataVo = lockedDataVo;
	}

	public BIReOpenAppResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(BIReOpenAppResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
