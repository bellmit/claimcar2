package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LOCKED_DATA")
public class SHCiCertifyResSubrogationDataVo {
	
	@XmlElement(name = "SUBROGATION_NO", required = false)
	private String subrogationNo;
	
	@XmlElement(name = "SUBROGATION_FLAG", required = false)
	private String subrogationFlag;
	
	@XmlElement(name = "DISPUTE_FLAG", required = false)
	private String disputeFlag;
	
	@XmlElementWrapper(name = "RECOVERY_LIST")
	@XmlElement(name = "RECOVERY_DATA")
	private List<SHCiCertifyResRecoveryDataVo> recoveryDataVos;
	
	@XmlElementWrapper(name = "BERECOVERY_LIST")
	@XmlElement(name = "BERECOVERY_DATA")
	private List<SHCiCertifyResBeRecoveryDataVo> beRecoveryDataVos;
	
	@XmlElementWrapper(name = "REASON_LIST")
	@XmlElement(name = "REASON_DATA")
	private List<SHCiCertifyResReasonDataVo> reasonDataVos;

	public List<SHCiCertifyResRecoveryDataVo> getRecoveryDataVos() {
		return recoveryDataVos;
	}

	public void setRecoveryDataVos(
			List<SHCiCertifyResRecoveryDataVo> recoveryDataVos) {
		this.recoveryDataVos = recoveryDataVos;
	}

	public List<SHCiCertifyResBeRecoveryDataVo> getBeRecoveryDataVos() {
		return beRecoveryDataVos;
	}

	public void setBeRecoveryDataVos(
			List<SHCiCertifyResBeRecoveryDataVo> beRecoveryDataVos) {
		this.beRecoveryDataVos = beRecoveryDataVos;
	}

	public List<SHCiCertifyResReasonDataVo> getReasonDataVos() {
		return reasonDataVos;
	}

	public void setReasonDataVos(List<SHCiCertifyResReasonDataVo> reasonDataVos) {
		this.reasonDataVos = reasonDataVos;
	}

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

}
