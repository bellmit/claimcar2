package ins.sino.claimcar.regist.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 交强报案平台返回信息BodyVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiRegistBodyResVo {
	
	@XmlElement(name="BASE_PART")
	private CiRegistBasePartResVo ciRegistBasePartResVo;
	
	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiRegistLockedDataResVo> ciRegistLockedDataRess;
	
	@XmlElement(name="RISK_INFO")
	private CiRegistRiskInfoResVo ciRegistRiskInfoResVo;

	public CiRegistBasePartResVo getCiRegistBasePartResVo() {
		return ciRegistBasePartResVo;
	}

	public void setCiRegistBasePartResVo(CiRegistBasePartResVo ciRegistBasePartResVo) {
		this.ciRegistBasePartResVo = ciRegistBasePartResVo;
	}

	public List<CiRegistLockedDataResVo> getCiRegistLockedDataRess() {
		return ciRegistLockedDataRess;
	}

	public void setCiRegistLockedDataRess(
			List<CiRegistLockedDataResVo> ciRegistLockedDataRess) {
		this.ciRegistLockedDataRess = ciRegistLockedDataRess;
	}

	public CiRegistRiskInfoResVo getCiRegistRiskInfoResVo() {
		return ciRegistRiskInfoResVo;
	}

	public void setCiRegistRiskInfoResVo(CiRegistRiskInfoResVo ciRegistRiskInfoResVo) {
		this.ciRegistRiskInfoResVo = ciRegistRiskInfoResVo;
	}
	
	

}
