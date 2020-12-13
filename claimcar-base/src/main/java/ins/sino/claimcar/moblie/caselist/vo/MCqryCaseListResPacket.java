package ins.sino.claimcar.moblie.caselist.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckResponseHead;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class MCqryCaseListResPacket implements Serializable {

	/**  */
	private static final long serialVersionUID = -1512984710127352762L;
	
	@XStreamAlias("HEAD")
	private MobileCheckResponseHead head;
	
	@XStreamAlias("BODY")
	private MCqryCaseListResCaseList CaseList;

	public MobileCheckResponseHead getHead() {
		return head;
	}

	public void setHead(MobileCheckResponseHead head) {
		this.head = head;
	}

	public MCqryCaseListResCaseList getCaseList() {
		return CaseList;
	}

	public void setCaseList(MCqryCaseListResCaseList caseList) {
		CaseList = caseList;
	}


	
	

}
