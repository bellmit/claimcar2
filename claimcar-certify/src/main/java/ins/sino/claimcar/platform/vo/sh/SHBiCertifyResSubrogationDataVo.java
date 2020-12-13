package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LOCKED_DATA")
public class SHBiCertifyResSubrogationDataVo {
	
	@XmlElement(name = "SUBROGATION_NO", required = false)
	private String subrogationNo;
	
	@XmlElement(name = "SUBROGATION_FLAG", required = false)
	private String subrogationFlag;
	
	@XmlElement(name = "DISPUTE_FLAG", required = false)
	private String disputeFlag;
	
	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHBiCertifyResRecoveryDataVo> recoveryDataVos;
	
	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHBiCertifyResBeRecoveryDataVo> beRecoveryDataVos;
	
	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<SHBiCertifyResReasonDataVo> reasonDataVos;

	public String getSubrogationNo() {
		return subrogationNo;
	}

	public void setSubrogationNo(String subrogationNo) {
		this.subrogationNo = subrogationNo;
	}

	public String getSubrogationFlag() {
		return subrogationFlag;
	}

	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}

	public String getDisputeFlag() {
		return disputeFlag;
	}

	public void setDisputeFlag(String disputeFlag) {
		this.disputeFlag = disputeFlag;
	}

	public List<SHBiCertifyResRecoveryDataVo> getRecoveryDataVos() {
		return recoveryDataVos;
	}

	public void setRecoveryDataVos(
			List<SHBiCertifyResRecoveryDataVo> recoveryDataVos) {
		this.recoveryDataVos = recoveryDataVos;
	}

	public List<SHBiCertifyResBeRecoveryDataVo> getBeRecoveryDataVos() {
		return beRecoveryDataVos;
	}

	public void setBeRecoveryDataVos(
			List<SHBiCertifyResBeRecoveryDataVo> beRecoveryDataVos) {
		this.beRecoveryDataVos = beRecoveryDataVos;
	}

	public List<SHBiCertifyResReasonDataVo> getReasonDataVos() {
		return reasonDataVos;
	}

	public void setReasonDataVos(List<SHBiCertifyResReasonDataVo> reasonDataVos) {
		this.reasonDataVos = reasonDataVos;
	}

}
