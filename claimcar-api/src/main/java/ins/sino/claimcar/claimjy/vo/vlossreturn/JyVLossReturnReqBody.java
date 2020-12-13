package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "BODY")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnReqBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "EvalLossInfo")
	private JyVLossReturnLossInfo lossInfo;
	
	@XmlElementWrapper(name = "LossFitInfo")
	@XmlElement(name = "Item") 
	private List<JyVLossReturnFitInfo> fitInfoList;
	
	@XmlElementWrapper(name = "LossRepairInfo")
	@XmlElement(name = "Item") 
	private List<JyVLossReturnRepairInfo> repairInfoList;
	
	@XmlElementWrapper(name = "LossRepairSumInfo")
	@XmlElement(name = "Item") 
	private List<JyVLossReturnRepairSumInfo> RepairSumInfoList;
	
	@XmlElementWrapper(name = "LossOuterRepairInfo")
	@XmlElement(name = "Item") 
	private List<JyVLossReturnOutRepairInfo> outRepairInfoList;
	
	@XmlElementWrapper(name = "LossAssistInfo")
	@XmlElement(name = "Item") 
	private List<JyVLossReturnAssistInfo> assistIfoList;

	public JyVLossReturnLossInfo getLossInfo() {
		return lossInfo;
	}

	public void setLossInfo(JyVLossReturnLossInfo lossInfo) {
		this.lossInfo = lossInfo;
	}

	public List<JyVLossReturnFitInfo> getFitInfoList() {
		return fitInfoList;
	}

	public void setFitInfoList(List<JyVLossReturnFitInfo> fitInfoList) {
		this.fitInfoList = fitInfoList;
	}

	public List<JyVLossReturnRepairInfo> getRepairInfoList() {
		return repairInfoList;
	}

	public void setRepairInfoList(List<JyVLossReturnRepairInfo> repairInfoList) {
		this.repairInfoList = repairInfoList;
	}

	public List<JyVLossReturnRepairSumInfo> getRepairSumInfoList() {
		return RepairSumInfoList;
	}

	public void setRepairSumInfoList(
			List<JyVLossReturnRepairSumInfo> repairSumInfoList) {
		RepairSumInfoList = repairSumInfoList;
	}

	public List<JyVLossReturnOutRepairInfo> getOutRepairInfoList() {
		return outRepairInfoList;
	}

	public void setOutRepairInfoList(
			List<JyVLossReturnOutRepairInfo> outRepairInfoList) {
		this.outRepairInfoList = outRepairInfoList;
	}

	public List<JyVLossReturnAssistInfo> getAssistIfoList() {
		return assistIfoList;
	}

	public void setAssistIfoList(List<JyVLossReturnAssistInfo> assistIfoList) {
		this.assistIfoList = assistIfoList;
	}
	
	

}
