package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

/**
 *  代位赔偿查询返回页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationDataVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private List<SubrogationClaimReopenDataVo> subrogationClaimReopenDataVo;/**重开赔案信息列表*/
	private List<SubrogationRecoveryConfirmDataVo> subrogationRecoveryConfirmDataVo;/**追回款信息列表 */
	private SubrogationReportDataVo subrogationReportDataVo;/**报案信息*/
	private SubrogationClaimDataVo subrogationClaimDataVo;/**立案信息*/
	private SubrogationCheckDataVos subrogationCheckDataVo;/**	查勘信息*/
	private SubrogationEstimateDataVo subrogationEstimateDataVo;/**定核损信息*/
	private  List<SubrogationDocDataVo> subrogationDocDataVo;/** 单证信息 */
	private SubrogationClaimCloseDataVo subrogationClaimCloseDataVo;/**  结案信息  */
	private SubrogationBasePartVo subrogationBasePartVo;//基本数据
	
	private List<SubrogationDataVos> subrogationDataVos ;//代位信息列表
	private SubrogationUnderWriteDataVo  subrogationUnderWriteDataVo ;/** 理算核赔信息 */
	private List<UndwrtWriteAdjustmentDataVos> undwrtWriteAdjustmentDataVos;//理算核赔信息商业
	private SubrogationDocVo subrogationDocVo;
	public SubrogationReportDataVo getSubrogationReportDataVo() {
		return subrogationReportDataVo;
	}

	public void setSubrogationReportDataVo(
			SubrogationReportDataVo subrogationReportDataVo) {
		this.subrogationReportDataVo = subrogationReportDataVo;
	}

	public SubrogationClaimDataVo getSubrogationClaimDataVo() {
		return subrogationClaimDataVo;
	}

	public void setSubrogationClaimDataVo(
			SubrogationClaimDataVo subrogationClaimDataVo) {
		this.subrogationClaimDataVo = subrogationClaimDataVo;
	}

	public SubrogationCheckDataVos getSubrogationCheckDataVo() {
		return subrogationCheckDataVo;
	}

	public void setSubrogationCheckDataVo(
			SubrogationCheckDataVos subrogationCheckDataVo) {
		this.subrogationCheckDataVo = subrogationCheckDataVo;
	}

	public SubrogationEstimateDataVo getSubrogationEstimateDataVo() {
		return subrogationEstimateDataVo;
	}

	public void setSubrogationEstimateDataVo(
			SubrogationEstimateDataVo subrogationEstimateDataVo) {
		this.subrogationEstimateDataVo = subrogationEstimateDataVo;
	}



	public List<SubrogationDocDataVo> getSubrogationDocDataVo() {
		return subrogationDocDataVo;
	}

	public void setSubrogationDocDataVo(
			List<SubrogationDocDataVo> subrogationDocDataVo) {
		this.subrogationDocDataVo = subrogationDocDataVo;
	}

	public SubrogationClaimCloseDataVo getSubrogationClaimCloseDataVo() {
		return subrogationClaimCloseDataVo;
	}

	public void setSubrogationClaimCloseDataVo(
			SubrogationClaimCloseDataVo subrogationClaimCloseDataVo) {
		this.subrogationClaimCloseDataVo = subrogationClaimCloseDataVo;
	}

	public SubrogationBasePartVo getSubrogationBasePartVo() {
		return subrogationBasePartVo;
	}

	public void setSubrogationBasePartVo(SubrogationBasePartVo subrogationBasePartVo) {
		this.subrogationBasePartVo = subrogationBasePartVo;
	}



	public List<SubrogationDataVos> getSubrogationDataVos() {
		return subrogationDataVos;
	}

	public void setSubrogationDataVos(List<SubrogationDataVos> subrogationDataVos) {
		this.subrogationDataVos = subrogationDataVos;
	}

	public SubrogationUnderWriteDataVo getSubrogationUnderWriteDataVo() {
		return subrogationUnderWriteDataVo;
	}

	public void setSubrogationUnderWriteDataVo(
			SubrogationUnderWriteDataVo subrogationUnderWriteDataVo) {
		this.subrogationUnderWriteDataVo = subrogationUnderWriteDataVo;
	}

	public List<SubrogationClaimReopenDataVo> getSubrogationClaimReopenDataVo() {
		return subrogationClaimReopenDataVo;
	}

	public void setSubrogationClaimReopenDataVo(
			List<SubrogationClaimReopenDataVo> subrogationClaimReopenDataVo) {
		this.subrogationClaimReopenDataVo = subrogationClaimReopenDataVo;
	}

	public List<SubrogationRecoveryConfirmDataVo> getSubrogationRecoveryConfirmDataVo() {
		return subrogationRecoveryConfirmDataVo;
	}

	public void setSubrogationRecoveryConfirmDataVo(
			List<SubrogationRecoveryConfirmDataVo> subrogationRecoveryConfirmDataVo) {
		this.subrogationRecoveryConfirmDataVo = subrogationRecoveryConfirmDataVo;
	}

	public List<UndwrtWriteAdjustmentDataVos> getUndwrtWriteAdjustmentDataVos() {
		return undwrtWriteAdjustmentDataVos;
	}

	public void setUndwrtWriteAdjustmentDataVos(
			List<UndwrtWriteAdjustmentDataVos> undwrtWriteAdjustmentDataVos) {
		this.undwrtWriteAdjustmentDataVos = undwrtWriteAdjustmentDataVos;
	}

	public SubrogationDocVo getSubrogationDocVo() {
		return subrogationDocVo;
	}

	public void setSubrogationDocVo(SubrogationDocVo subrogationDocVo) {
		this.subrogationDocVo = subrogationDocVo;
	}


	
	

}
