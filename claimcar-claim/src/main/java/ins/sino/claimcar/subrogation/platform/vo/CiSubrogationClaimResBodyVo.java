package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * 代位求偿理赔查询  Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiSubrogationClaimResBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiSubrogationBasePartVo subrogationBaseVo;
	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<CiSubrogationDataVo> subrogationDataList;
	/**报案信息*/
	@XmlElement(name = "REPORT_DATA")
	private CiSubrogationReportDataVo reportDataVo;
	/**立案信息*/
	@XmlElement(name = "REGISTRATION_DATA")
	private CiSubrogationClaimDataVo claimDataVo;
	/**	查勘信息*/
	@XmlElement(name = "CHECK_DATA")
	private CiSubrogationCheckDataVo checkDataVo;
	/**定核损信息*/
	@XmlElement(name = "ESTIMATE_UNDER_DATA")
	private CiSubrogationEstimateDataVo estimateDataVo;
	/** 单证信息 */
	@XmlElement(name = "DOC_DATA")
	private CiSubrogationDocDataVo docDataVo;
	/** 理算核赔信息 */
	@XmlElement(name = "UNDER_WRITE_DATA")
	private CiSubrogationUnderWriteDataVo underWriteDataVo;
	/**  结案信息  */
	@XmlElement(name = "CLAIM_CLOSE_DATA")
	private CiSubrogationClaimCloseDataVo claimCloseDataVo;
	/** 重开赔案信息列表  */
	@XmlElementWrapper(name = "CLAIM_REOPEN_LIST")
	@XmlElement(name = "CLAIM_REOPEN_DATA")
	private List<CiSubrogationClaimReopenDataVo> claimReopenDataList;
	/** 追回款信息列表 */
	@XmlElementWrapper(name = "RECOVERY_CONFIRM_LIST")
	@XmlElement(name = "RECOVERY_CONFIRM_DATA")
	private  List<CiSubrogationRecoveryConfirmDataVo> recoveryConfirmDataList;
	
	public CiSubrogationBasePartVo getSubrogationBaseVo() {
		return subrogationBaseVo;
	}

	
	public void setSubrogationBaseVo(CiSubrogationBasePartVo subrogationBaseVo) {
		this.subrogationBaseVo = subrogationBaseVo;
	}

	
	public List<CiSubrogationDataVo> getSubrogationDataList() {
		return subrogationDataList;
	}

	
	public void setSubrogationDataList(List<CiSubrogationDataVo> subrogationDataList) {
		this.subrogationDataList = subrogationDataList;
	}

	public CiSubrogationReportDataVo getReportDataVo() {
		return reportDataVo;
	}
	
	public void setReportDataVo(CiSubrogationReportDataVo reportDataVo) {
		this.reportDataVo = reportDataVo;
	}
	
	public CiSubrogationClaimDataVo getClaimDataVo() {
		return claimDataVo;
	}
	
	public void setClaimDataVo(CiSubrogationClaimDataVo claimDataVo) {
		this.claimDataVo = claimDataVo;
	}
	
	public CiSubrogationCheckDataVo getCheckDataVo() {
		return checkDataVo;
	}
	
	public void setCheckDataVo(CiSubrogationCheckDataVo checkDataVo) {
		this.checkDataVo = checkDataVo;
	}
	
	public CiSubrogationEstimateDataVo getEstimateDataVo() {
		return estimateDataVo;
	}
	
	public void setEstimateDataVo(CiSubrogationEstimateDataVo estimateDataVo) {
		this.estimateDataVo = estimateDataVo;
	}
	
	public CiSubrogationDocDataVo getDocDataVo() {
		return docDataVo;
	}
	
	public void setDocDataVo(CiSubrogationDocDataVo docDataVo) {
		this.docDataVo = docDataVo;
	}
	
	public CiSubrogationUnderWriteDataVo getUnderWriteDataVo() {
		return underWriteDataVo;
	}
	
	public void setUnderWriteDataVo(CiSubrogationUnderWriteDataVo underWriteDataVo) {
		this.underWriteDataVo = underWriteDataVo;
	}
	
	public CiSubrogationClaimCloseDataVo getClaimCloseDataVo() {
		return claimCloseDataVo;
	}
	
	public void setClaimCloseDataVo(CiSubrogationClaimCloseDataVo claimCloseDataVo) {
		this.claimCloseDataVo = claimCloseDataVo;
	}
	
	public List<CiSubrogationClaimReopenDataVo> getClaimReopenDataList() {
		return claimReopenDataList;
	}
	
	public void setClaimReopenDataList(List<CiSubrogationClaimReopenDataVo> claimReopenDataList) {
		this.claimReopenDataList = claimReopenDataList;
	}
	
	public List<CiSubrogationRecoveryConfirmDataVo> getRecoveryConfirmDataList() {
		return recoveryConfirmDataList;
	}
	
	public void setRecoveryConfirmDataList(List<CiSubrogationRecoveryConfirmDataVo> recoveryConfirmDataList) {
		this.recoveryConfirmDataList = recoveryConfirmDataList;
	}
	
	
}
