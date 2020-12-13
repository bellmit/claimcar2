package ins.sino.claimcar.manager.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom VO class of PO PrpLInsuredFactory
 */ 
public class PrpLInsuredFactoryVo extends PrpLInsuredFactoryVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private Map<String, String> insuredMap = new HashMap<String, String>();
	
	public Map<String, String> getInsuredMap() {
		return insuredMap;
	}
	public void setInsuredMap(Map<String, String> insuredMap) {
		this.insuredMap = insuredMap;
	}
	
	public void setInsuredMap(String insuredCode,String insuredName) {
		this.insuredMap.put(insuredCode, insuredName);
	}
}
