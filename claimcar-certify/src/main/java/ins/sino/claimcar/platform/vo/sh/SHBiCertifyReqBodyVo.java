package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBiCertifyReqBodyVo {
	
	@XmlElement(name = "BASE_PART")
	private SHBiCertifyReqBasePartVo basePart;
	
	@XmlElementWrapper(name = "DOC_LIST")
	@XmlElement(name = "DOC_DATA")
	private List<SHBiCertifyReqDocDataVo> docDatas;
	
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHBiCertifyReqSubrogationDataVo> subrogationDatas;
	
	@XmlElementWrapper(name = "DISPUTE_LIST")
	@XmlElement(name = "DISPUTE_DATA")
	private List<SHBiCertifyReqDisputeDataVo> disputeDatas;

	public SHBiCertifyReqBasePartVo getBasePart() {
		return basePart;
	}

	public void setBasePart(SHBiCertifyReqBasePartVo basePart) {
		this.basePart = basePart;
	}

	public List<SHBiCertifyReqDocDataVo> getDocDatas() {
		return docDatas;
	}

	public void setDocDatas(List<SHBiCertifyReqDocDataVo> docDatas) {
		this.docDatas = docDatas;
	}

	public List<SHBiCertifyReqSubrogationDataVo> getSubrogationDatas() {
		return subrogationDatas;
	}

	public void setSubrogationDatas(
			List<SHBiCertifyReqSubrogationDataVo> subrogationDatas) {
		this.subrogationDatas = subrogationDatas;
	}

	public List<SHBiCertifyReqDisputeDataVo> getDisputeDatas() {
		return disputeDatas;
	}

	public void setDisputeDatas(List<SHBiCertifyReqDisputeDataVo> disputeDatas) {
		this.disputeDatas = disputeDatas;
	}

}
