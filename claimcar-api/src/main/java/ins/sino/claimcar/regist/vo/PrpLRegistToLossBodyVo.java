package ins.sino.claimcar.regist.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("BODY")
public class PrpLRegistToLossBodyVo implements Serializable {
	private static final long serialVersionUID = -2466171728951051441L;
	@XStreamAlias("LossPolicy")
	private List<LossPolicyItemVo> lossPolicy;
	@XStreamAlias("LossCoverVehicle")
	private LossCoverVehicleVo lossCoverVehicle;
	@XStreamAlias("LossInsured")
	private List<LossInsuredItemVo> LossInsured;
	@XStreamAlias("LossReporting")
	private LossReportingVo LossReporting;
	@XStreamAlias("CarLossDetail")
	private List<CarLossDetailItemVo> carLossDetail;
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
	public List<CarLossDetailItemVo> getCarLossDetail() {
		return carLossDetail;
	}
	public void setCarLossDetail(List<CarLossDetailItemVo> carLossDetail) {
		this.carLossDetail = carLossDetail;
	}
}
