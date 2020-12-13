package ins.sino.claimcar.regist.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 商业报案平台返回信息BodyVo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiRegistBodyResVo {
	
	@XmlElement(name="BasePart")
	private BiRegistBasePartResVo biRegistBasePartResVo;
	
	@XmlElement(name="RiskInfo")
	private BiRegistRiskInfoResVo biRegistRiskInfoResVo;
	
	@XmlElement(name = "LockedData")
	private List<BiRegistLockedDataResVo> biRegistLockedDataRess;

	public BiRegistBasePartResVo getBiRegistBasePartResVo() {
		return biRegistBasePartResVo;
	}

	public void setBiRegistBasePartResVo(BiRegistBasePartResVo biRegistBasePartResVo) {
		this.biRegistBasePartResVo = biRegistBasePartResVo;
	}

	public BiRegistRiskInfoResVo getBiRegistRiskInfoResVo() {
		return biRegistRiskInfoResVo;
	}

	public void setBiRegistRiskInfoResVo(BiRegistRiskInfoResVo biRegistRiskInfoResVo) {
		this.biRegistRiskInfoResVo = biRegistRiskInfoResVo;
	}

	public List<BiRegistLockedDataResVo> getBiRegistLockedDataRess() {
		return biRegistLockedDataRess;
	}

	public void setBiRegistLockedDataRess(
			List<BiRegistLockedDataResVo> biRegistLockedDataRess) {
		this.biRegistLockedDataRess = biRegistLockedDataRess;
	}
	
}
