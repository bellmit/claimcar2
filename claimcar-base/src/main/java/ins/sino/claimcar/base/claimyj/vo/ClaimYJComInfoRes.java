package ins.sino.claimcar.base.claimyj.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PACKET")
public class ClaimYJComInfoRes {
	@XStreamAlias("HEAD")
	private ResHeadYJVo resHeadYJVo;

	public ResHeadYJVo getResHeadYJVo() {
		return resHeadYJVo;
	}

	public void setResHeadYJVo(ResHeadYJVo resHeadYJVo) {
		this.resHeadYJVo = resHeadYJVo;
	}
	
}
