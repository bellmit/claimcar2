package ins.sino.claimcar.platform.vo.sh;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCiCertifyReqBodyVo {
	
	@XmlElement(name = "BASE_PART")
	private SHCiCertifyReqBasePartVo basePart;
	
	@XmlElementWrapper(name = "DOC_LIST")
	@XmlElement(name = "DOC_DATA")
	private List<SHCiCertifyReqDocDataVo> docDatas;
	
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCiCertifyReqSubrogationDataVo> subrogationDatas;
	
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHCiCertifyReqDisputeDataVo> disputeDatas;

	public SHCiCertifyReqBasePartVo getBasePart() {
		return basePart;
	}

	public void setBasePart(SHCiCertifyReqBasePartVo basePart) {
		this.basePart = basePart;
	}

	public List<SHCiCertifyReqDocDataVo> getDocDatas() {
		return docDatas;
	}

	public void setDocDatas(List<SHCiCertifyReqDocDataVo> docDatas) {
		this.docDatas = docDatas;
	}

	public List<SHCiCertifyReqSubrogationDataVo> getSubrogationDatas() {
		return subrogationDatas;
	}

	public void setSubrogationDatas(
			List<SHCiCertifyReqSubrogationDataVo> subrogationDatas) {
		this.subrogationDatas = subrogationDatas;
	}

	public List<SHCiCertifyReqDisputeDataVo> getDisputeDatas() {
		return disputeDatas;
	}

	public void setDisputeDatas(List<SHCiCertifyReqDisputeDataVo> disputeDatas) {
		this.disputeDatas = disputeDatas;
	}

}
