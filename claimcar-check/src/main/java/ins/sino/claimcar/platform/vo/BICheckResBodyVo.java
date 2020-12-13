package ins.sino.claimcar.platform.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo -->商业-->返回报文body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BICheckResBodyVo {

	/** 锁定信息列表 **/
	@XmlElement(name = "LockedData")
	private List<BICheckResLockedDataVo> lockedDataVo;

	/** 风险信息 **/
	@XmlElement(name = "RiskInfo")
	private BICheckResRiskInfoVo riskInfoVo;

	
	
	public List<BICheckResLockedDataVo> getLockedDataVo() {
		return lockedDataVo;
	}

	public void setLockedDataVo(List<BICheckResLockedDataVo> lockedDataVo) {
		this.lockedDataVo = lockedDataVo;
	}

	public BICheckResRiskInfoVo getRiskInfoVo() {
		return riskInfoVo;
	}

	public void setRiskInfoVo(BICheckResRiskInfoVo riskInfoVo) {
		this.riskInfoVo = riskInfoVo;
	}

}
