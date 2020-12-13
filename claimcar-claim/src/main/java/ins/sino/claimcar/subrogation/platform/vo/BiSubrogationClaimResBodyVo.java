package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/***
 * 代位求偿理赔查询      商业  Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiSubrogationClaimResBodyVo {

	@XmlElement(name = "BasePart")
	private BiSubrogationBasePartVo subrogationBaseVo;
	//@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SubrogationData")//代位
	private List<BiSubrogationDataVo> subrogationDataList;
	/**报案信息*/
	@XmlElement(name = "NotificationData")
	private BiSubrogationReportDataVo reportDataVo;
	/**立案信息*/
	@XmlElement(name = "RegistrationData")
	private BiSubrogationClaimDataVo claimDataVo;
	/**	查勘信息*/
	@XmlElement(name = "CheckData")
	private BiSubrogationCheckDataVo checkDataVo;
	/**定核损信息*/
	@XmlElement(name = "EstimateUnderData")
	private BiSubrogationEstimateDataVo estimateDataVo;
	/** 单证信息 */
	@XmlElement(name = "DocData")
	private BiSubrogationDocDataVo docDataVo;
	/** 理算核赔信息 */
	@XmlElement(name = "UnderWriteData")
	private BiSubrogationUnderWriteDataVo underWriteDataVo;
	/**  结案信息  */
	@XmlElement(name = "ClaimCloseData")
	private BiSubrogationClaimCloseDataVo claimCloseDataVo;
	/** 重开赔案信息列表  */
	@XmlElement(name = "ClaimReopenData")
	private List<BiSubrogationClaimReopenDataVo> claimReopenDataList;
	/** 追回款信息列表 */
	@XmlElement(name = "RecoverConfirmData")
	private  List<BiSubrogationRecoveryConfirmDataVo> recoveryConfirmDataList;
	public BiSubrogationBasePartVo getSubrogationBaseVo() {
		return subrogationBaseVo;
	}
	public void setSubrogationBaseVo(BiSubrogationBasePartVo subrogationBaseVo) {
		this.subrogationBaseVo = subrogationBaseVo;
	}
	public List<BiSubrogationDataVo> getSubrogationDataList() {
		return subrogationDataList;
	}
	public void setSubrogationDataList(List<BiSubrogationDataVo> subrogationDataList) {
		this.subrogationDataList = subrogationDataList;
	}
	public BiSubrogationReportDataVo getReportDataVo() {
		return reportDataVo;
	}
	public void setReportDataVo(BiSubrogationReportDataVo reportDataVo) {
		this.reportDataVo = reportDataVo;
	}
	public BiSubrogationClaimDataVo getClaimDataVo() {
		return claimDataVo;
	}
	public void setClaimDataVo(BiSubrogationClaimDataVo claimDataVo) {
		this.claimDataVo = claimDataVo;
	}
	public BiSubrogationCheckDataVo getCheckDataVo() {
		return checkDataVo;
	}
	public void setCheckDataVo(BiSubrogationCheckDataVo checkDataVo) {
		this.checkDataVo = checkDataVo;
	}
	public BiSubrogationEstimateDataVo getEstimateDataVo() {
		return estimateDataVo;
	}
	public void setEstimateDataVo(BiSubrogationEstimateDataVo estimateDataVo) {
		this.estimateDataVo = estimateDataVo;
	}
	public BiSubrogationDocDataVo getDocDataVo() {
		return docDataVo;
	}
	public void setDocDataVo(BiSubrogationDocDataVo docDataVo) {
		this.docDataVo = docDataVo;
	}
	public BiSubrogationUnderWriteDataVo getUnderWriteDataVo() {
		return underWriteDataVo;
	}
	public void setUnderWriteDataVo(BiSubrogationUnderWriteDataVo underWriteDataVo) {
		this.underWriteDataVo = underWriteDataVo;
	}
	public BiSubrogationClaimCloseDataVo getClaimCloseDataVo() {
		return claimCloseDataVo;
	}
	public void setClaimCloseDataVo(BiSubrogationClaimCloseDataVo claimCloseDataVo) {
		this.claimCloseDataVo = claimCloseDataVo;
	}
	public List<BiSubrogationClaimReopenDataVo> getClaimReopenDataList() {
		return claimReopenDataList;
	}
	public void setClaimReopenDataList(
			List<BiSubrogationClaimReopenDataVo> claimReopenDataList) {
		this.claimReopenDataList = claimReopenDataList;
	}
	public List<BiSubrogationRecoveryConfirmDataVo> getRecoveryConfirmDataList() {
		return recoveryConfirmDataList;
	}
	public void setRecoveryConfirmDataList(
			List<BiSubrogationRecoveryConfirmDataVo> recoveryConfirmDataList) {
		this.recoveryConfirmDataList = recoveryConfirmDataList;
	}
	

}
