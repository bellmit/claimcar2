package ins.sino.claimcar.claim.claimjy.cleanData.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class JyCleanDataBodyVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("TotalLossInfo") 
     private TotalLossInfo totalLossInfo;
	public TotalLossInfo getTotalLossInfo() {
		return totalLossInfo;
	}
	public void setTotalLossInfo(TotalLossInfo totalLossInfo) {
		this.totalLossInfo = totalLossInfo;
	}

	 
	 
}
