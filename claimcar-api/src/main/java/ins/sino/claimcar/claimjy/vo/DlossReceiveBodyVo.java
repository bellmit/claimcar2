package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "BODY")
@XmlAccessorType(XmlAccessType.FIELD)
public class DlossReceiveBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "EvalLossInfo")
	private ReceiveEvalLossInfo receiveEvalLossInfo;//消息信息
	@XmlElement(name = "VehicleInfo")
	private VehicleInfo vehicleInfo;//车辆信息	
	@XmlElementWrapper(name = "CollisionPartsList")
	@XmlElement(name = "CollisionParts")
	private List<CollisionParts> collisionParts;//损失部位信息
	@XmlElementWrapper(name = "LossFitInfo")
	@XmlElement(name = "Item")
	private List<LossFitInfoItem> LossFitInfoItemItems;//定损换件信息
	@XmlElementWrapper(name = "LossRepairInfo")
	@XmlElement(name = "Item")
	private List<LossRepairInfoItem> lossRepairInfoItems;//定损修理信息
	@XmlElementWrapper(name = "LossOuterRepairInfo")
	@XmlElement(name = "Item")
	private List<LossOuterRepairInfoItem> lossOuterRepairInfoItems;//定损外修信息
	@XmlElementWrapper(name = "LossRepairSumInfoInfo")
	@XmlElement(name = "Item")
	private List<LossRepairSumInfoItem> lossRepairSumInfoItems;//定损修理合计信息
	@XmlElementWrapper(name = "LossAssistInfo")
	@XmlElement(name = "Item")
	private List<LossAssistInfoItem> lossAssistInfoItems;//定损辅料信息
	@XmlElement(name = "Rules")
	private DlossRules dlossRules;//定损规则信息
	
	public ReceiveEvalLossInfo getReceiveEvalLossInfo() {
		return receiveEvalLossInfo;
	}
	public void setReceiveEvalLossInfo(ReceiveEvalLossInfo receiveEvalLossInfo) {
		this.receiveEvalLossInfo = receiveEvalLossInfo;
	}
	public VehicleInfo getVehicleInfo() {
		return vehicleInfo;
	}
	public void setVehicleInfo(VehicleInfo vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
	}
	public List<CollisionParts> getCollisionParts() {
		return collisionParts;
	}
	public void setCollisionParts(List<CollisionParts> collisionParts) {
		this.collisionParts = collisionParts;
	}
	public List<LossFitInfoItem> getLossFitInfoItemItems() {
		return LossFitInfoItemItems;
	}
	public void setLossFitInfoItemItems(List<LossFitInfoItem> lossFitInfoItemItems) {
		LossFitInfoItemItems = lossFitInfoItemItems;
	}
	public List<LossRepairInfoItem> getLossRepairInfoItems() {
		return lossRepairInfoItems;
	}
	public void setLossRepairInfoItems(List<LossRepairInfoItem> lossRepairInfoItems) {
		this.lossRepairInfoItems = lossRepairInfoItems;
	}
	public List<LossOuterRepairInfoItem> getLossOuterRepairInfoItems() {
		return lossOuterRepairInfoItems;
	}
	public void setLossOuterRepairInfoItems(
			List<LossOuterRepairInfoItem> lossOuterRepairInfoItems) {
		this.lossOuterRepairInfoItems = lossOuterRepairInfoItems;
	}
	public List<LossRepairSumInfoItem> getLossRepairSumInfoItems() {
		return lossRepairSumInfoItems;
	}
	public void setLossRepairSumInfoItems(
			List<LossRepairSumInfoItem> lossRepairSumInfoItems) {
		this.lossRepairSumInfoItems = lossRepairSumInfoItems;
	}
	public List<LossAssistInfoItem> getLossAssistInfoItems() {
		return lossAssistInfoItems;
	}
	public void setLossAssistInfoItems(List<LossAssistInfoItem> lossAssistInfoItems) {
		this.lossAssistInfoItems = lossAssistInfoItems;
	}
	public DlossRules getDlossRules() {
		return dlossRules;
	}
	public void setDlossRules(DlossRules dlossRules) {
		this.dlossRules = dlossRules;
	}
}
