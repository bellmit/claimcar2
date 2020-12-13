package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class DlossAskReqBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ReqInfo")
	private DlossAskReqInfo dlossAskReqInfo;// 消息信息
	@XStreamAlias("EvalLossInfo")
	private DlossAskEvalLossInfo dlossAskEvalLossInfo;// 定损信息
	@XStreamAlias("ItemCovers")
	private List<ItemCover> itemCovers;// 车辆信息
	@XStreamAlias("LossPolicy")
	private List<ExtraBusinessInfo> extraBusinessInfo;// 业务案件扩展信息
	@XStreamAlias("LossPolicy")
	private List<LossPolicyItemVo> lossPolicy;// 保单信息
	@XStreamAlias("LossCoverVehicle")
	private LossCoverVehicleVo lossCoverVehicle;// 承保车辆信息
	@XStreamAlias("LossInsured")
	private List<LossInsuredItemVo> LossInsured;// 承保险别
	@XStreamAlias("LossReporting")
	private LossReportingVo LossReporting;// 报案信息
	public DlossAskReqInfo getDlossAskReqInfo() {
		return dlossAskReqInfo;
	}
	public void setDlossAskReqInfo(DlossAskReqInfo dlossAskReqInfo) {
		this.dlossAskReqInfo = dlossAskReqInfo;
	}
	public DlossAskEvalLossInfo getDlossAskEvalLossInfo() {
		return dlossAskEvalLossInfo;
	}
	public void setDlossAskEvalLossInfo(DlossAskEvalLossInfo dlossAskEvalLossInfo) {
		this.dlossAskEvalLossInfo = dlossAskEvalLossInfo;
	}
	public List<ItemCover> getItemCovers() {
		return itemCovers;
	}
	public void setItemCovers(List<ItemCover> itemCovers) {
		this.itemCovers = itemCovers;
	}
	public List<ExtraBusinessInfo> getExtraBusinessInfo() {
		return extraBusinessInfo;
	}
	public void setExtraBusinessInfo(List<ExtraBusinessInfo> extraBusinessInfo) {
		this.extraBusinessInfo = extraBusinessInfo;
	}
	public List<LossPolicyItemVo> getLossPolicy() {
		return lossPolicy;
	}
	public void setLossPolicy(List<LossPolicyItemVo> lossPolicy) {
		this.lossPolicy = lossPolicy;
	}
	public LossCoverVehicleVo getLossCoverVehicle() {
		return lossCoverVehicle;
	}
	public void setLossCoverVehicle(LossCoverVehicleVo lossCoverVehicle) {
		this.lossCoverVehicle = lossCoverVehicle;
	}
	public List<LossInsuredItemVo> getLossInsured() {
		return LossInsured;
	}
	public void setLossInsured(List<LossInsuredItemVo> lossInsured) {
		LossInsured = lossInsured;
	}
	public LossReportingVo getLossReporting() {
		return LossReporting;
	}
	public void setLossReporting(LossReportingVo lossReporting) {
		LossReporting = lossReporting;
	}
}
