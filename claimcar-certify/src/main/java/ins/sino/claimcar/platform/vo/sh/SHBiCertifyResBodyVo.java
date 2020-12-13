package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBiCertifyResBodyVo {
	
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHBiCertifyResSubrogationDataVo> subrogationDataVo;

	public List<SHBiCertifyResSubrogationDataVo> getSubrogationDataVo() {
		return subrogationDataVo;
	}

	public void setSubrogationDataVo(
			List<SHBiCertifyResSubrogationDataVo> subrogationDataVo) {
		this.subrogationDataVo = subrogationDataVo;
	}

}
