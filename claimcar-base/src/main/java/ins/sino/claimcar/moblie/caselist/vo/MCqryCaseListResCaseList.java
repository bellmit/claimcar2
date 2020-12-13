package ins.sino.claimcar.moblie.caselist.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class MCqryCaseListResCaseList implements Serializable{

	/**  */
	private static final long serialVersionUID = -4647834728948138874L;
	
	public List<MCqryCaseListResBody> getCaseList() {
		return caseList;
	}

	public void setCaseList(List<MCqryCaseListResBody> caseList) {
		this.caseList = caseList;
	}

	@XStreamAlias("CASELIST")
	private List<MCqryCaseListResBody> caseList;
}
