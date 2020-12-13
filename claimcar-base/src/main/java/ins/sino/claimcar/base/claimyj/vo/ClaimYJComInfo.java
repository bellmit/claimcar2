package ins.sino.claimcar.base.claimyj.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class ClaimYJComInfo {
    @XStreamAlias("HEAD")
	private ReqHeadYJVo headVo;
    @XStreamAlias("BODY")
	private ClaimYJComInfoBodyVo bodyVo;
	public ReqHeadYJVo getHeadVo() {
		return headVo;
	}
	public void setHeadVo(ReqHeadYJVo headVo) {
		this.headVo = headVo;
	}
	public ClaimYJComInfoBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(ClaimYJComInfoBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}
    
}
