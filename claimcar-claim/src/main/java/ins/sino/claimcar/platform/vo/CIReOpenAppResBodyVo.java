package ins.sino.claimcar.platform.vo;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CIReOpenAppResBodyVo {
	
	/** 锁定信息列表 **/
	@XmlElement(name = "LOCKED_LIST")
	private List<CIReOpenAppResLockedListVo> lockesListVo;
	
	/** 风险信息 **/
	@XmlElement(name = "RISK_INFO")
	private CIReOpenAppResRiskInfoVo riskInfoVo;

	public List<CIReOpenAppResLockedListVo> getLockesListVo() {
		return lockesListVo;
	}

	public void setLockesListVo(List<CIReOpenAppResLockedListVo> lockesListVo) {
		this.lockesListVo = lockesListVo;
	}

	public CIReOpenAppResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(CIReOpenAppResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
