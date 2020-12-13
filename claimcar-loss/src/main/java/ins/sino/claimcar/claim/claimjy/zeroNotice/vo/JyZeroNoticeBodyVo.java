package ins.sino.claimcar.claim.claimjy.zeroNotice.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class JyZeroNoticeBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ZeroLossInfo") 
    private ZeroLossInfo zeroLossInfo;
	public ZeroLossInfo getZeroLossInfo() {
		return zeroLossInfo;
	}
	public void setZeroLossInfo(ZeroLossInfo zeroLossInfo) {
		this.zeroLossInfo = zeroLossInfo;
	}
	
}
