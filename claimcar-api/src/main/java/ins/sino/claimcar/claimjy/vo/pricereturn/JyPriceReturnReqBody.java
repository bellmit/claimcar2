package ins.sino.claimcar.claimjy.vo.pricereturn;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BODY")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyPriceReturnReqBody  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "EvalLossInfo") 
	private JyPriceReturnReqLossInfo lossInfo;
	
	@XmlElementWrapper(name = "LossFitInfo")
	@XmlElement(name = "Item") 
	private List<JyPriceReturnReqFitInfo> fitInfoList;
	
	@XmlElementWrapper(name = "LossOuterRepairInfo") 
	@XmlElement(name = "Item") 
	private List<JyPriceReturnReqOutRepairInfo> outRepairInfoList;
	
	@XmlElementWrapper(name = "LossAssistInfo") 
	@XmlElement(name = "Item") 
	private List<JyPriceReturnReqAssistInfo> assistInfoList;

	public JyPriceReturnReqLossInfo getLossInfo() {
		return lossInfo;
	}

	public void setLossInfo(JyPriceReturnReqLossInfo lossInfo) {
		this.lossInfo = lossInfo;
	}

	public List<JyPriceReturnReqFitInfo> getFitInfoList() {
		return fitInfoList;
	}

	public void setFitInfoList(List<JyPriceReturnReqFitInfo> fitInfoList) {
		this.fitInfoList = fitInfoList;
	}

	public List<JyPriceReturnReqOutRepairInfo> getOutRepairInfoList() {
		return outRepairInfoList;
	}

	public void setOutRepairInfoList(
			List<JyPriceReturnReqOutRepairInfo> outRepairInfoList) {
		this.outRepairInfoList = outRepairInfoList;
	}

	public List<JyPriceReturnReqAssistInfo> getAssistInfoList() {
		return assistInfoList;
	}

	public void setAssistInfoList(List<JyPriceReturnReqAssistInfo> assistInfoList) {
		this.assistInfoList = assistInfoList;
	}
	
	
	

}
