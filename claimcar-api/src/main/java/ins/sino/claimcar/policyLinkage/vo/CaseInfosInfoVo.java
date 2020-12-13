package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("info")
public class CaseInfosInfoVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("data")
	private String data;
	
	//@XStreamAlias("dataList")
	@XStreamImplicit
	private List<CaseInfosDataListVo> dataList;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<CaseInfosDataListVo> getDataList() {
		return dataList;
	}

	public void setDataList(List<CaseInfosDataListVo> dataList) {
		this.dataList = dataList;
	}


	
	

}
