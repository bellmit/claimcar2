package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("info")
public class CaseDetailInfoVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("data")
	private CaseDetailDataVo data;

	public CaseDetailDataVo getData() {
		return data;
	}

	public void setData(CaseDetailDataVo data) {
		this.data = data;
	}
	
	
	
}
