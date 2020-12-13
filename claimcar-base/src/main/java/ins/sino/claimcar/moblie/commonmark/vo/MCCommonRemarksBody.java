package ins.sino.claimcar.moblie.commonmark.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MCCommonRemarksBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("REGISTNO ")
	private String registNo;  //报案号
	
	@XStreamAlias("REMARKLIST")
	private List<MCCommonRemarksRemarkInfo> remarkList;
	
	@XStreamAlias("REMARKOPTIONLIST ")
	private List<MCCommonRemarksRemarksOptionInfo> remarkOptionList;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public List<MCCommonRemarksRemarkInfo> getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(List<MCCommonRemarksRemarkInfo> remarkList) {
		this.remarkList = remarkList;
	}

	public List<MCCommonRemarksRemarksOptionInfo> getRemarkOptionList() {
		return remarkOptionList;
	}

	public void setRemarkOptionList(
			List<MCCommonRemarksRemarksOptionInfo> remarkOptionList) {
		this.remarkOptionList = remarkOptionList;
	}
	
	
	

}
