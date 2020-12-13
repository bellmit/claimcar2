package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo -->交强-->返回报文body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CICheckResBodyVo {

	/** 锁定信息列表 **/
	@XmlElement(name = "LOCKED_LIST")
	private List<CICheckResLockedListVo> lockesListVo;

	/** 风险信息 **/
	@XmlElement(name = "RISK_INFO")
	private CICheckResRiskInfoVo riskInfoVo;
	
	

	public List<CICheckResLockedListVo> getLockesListVo() {
		return lockesListVo;
	}

	public void setLockesListVo(List<CICheckResLockedListVo> lockesListVo) {
		this.lockesListVo = lockesListVo;
	}

	public CICheckResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(CICheckResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
